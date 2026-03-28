import express from "express";
import dotenv from "dotenv";
import cors from "cors";
import { PayOS } from "@payos/node";

dotenv.config();

const app = express();

// PayOS webhook thường gửi JSON
app.use(express.json());

// Cho phép app Android gọi local backend khi dev
app.use(
  cors({
    origin: "*",
    methods: ["GET", "POST", "OPTIONS"],
    allowedHeaders: ["Content-Type"],
  })
);

const payOS = new PayOS({
  clientId: process.env.PAYOS_CLIENT_ID,
  apiKey: process.env.PAYOS_API_KEY,
  checksumKey: process.env.PAYOS_CHECKSUM_KEY,
});

// Demo storage (in-memory). Làm CV ok, nhưng khi restart server sẽ mất dữ liệu.
// Key: orderCode (number)
const orders = new Map();

const OrderStatus = {
  PENDING: "PENDING",
  PAID: "PAID",
  FAILED: "FAILED",
  CANCELLED: "CANCELLED",
};

function nowMs() {
  return Date.now();
}

function normalizeStatus(input) {
  const s = String(input || "").toUpperCase();
  if (s === "PAID" || s === "SUCCESS") return OrderStatus.PAID;
  if (s === "CANCELLED" || s === "CANCELED") return OrderStatus.CANCELLED;
  if (s === "FAILED" || s === "FAIL") return OrderStatus.FAILED;
  if (s === "PENDING" || s === "PROCESSING") return OrderStatus.PENDING;
  return OrderStatus.PENDING;
}

/**
 * 0) Health
 */
app.get("/", (req, res) => {
  res.send("Payment server is running");
});

/**
 * 1) Tạo link thanh toán
 * Body: { amount, description, localOrderId, items[] }
 * Response: { orderCode, status, checkoutUrl, qrCode, paymentLinkId }
 */
app.post("/create-payment", async (req, res) => {
  try {
    const {
      amount = 10000,
      description = "Thanh toán cho EasyMart",
      localOrderId,
      items = [],
    } = req.body || {};

    if (localOrderId === undefined || localOrderId === null) {
      return res.status(400).json({ message: "localOrderId is required" });
    }

    const amt = Number(amount);
    if (!Number.isFinite(amt) || amt <= 0) {
      return res.status(400).json({ message: "amount must be > 0" });
    }

    // PayOS orderCode yêu cầu số nguyên, unique
    const orderCode = nowMs();

    // Deeplink trả về app
    // Lưu ý: app Android phải khai báo intent-filter cho scheme easymart, host payos, path /return và /cancel
    const returnUrl = `easymart://payos/return?localOrderId=${encodeURIComponent(
      String(localOrderId)
    )}&orderCode=${encodeURIComponent(String(orderCode))}`;
    const cancelUrl = `easymart://payos/cancel?localOrderId=${encodeURIComponent(
      String(localOrderId)
    )}&orderCode=${encodeURIComponent(String(orderCode))}`;

    const paymentItems = Array.isArray(items) && items.length > 0
      ? items.map((item) => ({
          name: String(item.name || "Sản phẩm"),
          quantity: Number(item.quantity || 1),
          price: Number(item.price || 0),
        }))
      : [
          {
            name: "Các sản phẩm từ EasyMart",
            quantity: 1,
            price: amt,
          },
        ];

    const paymentData = {
      orderCode,
      amount: amt,
      description,
      items: paymentItems,
      cancelUrl,
      returnUrl,
    };

    const paymentLink = await payOS.paymentRequests.create(paymentData);

    const record = {
      orderCode,
      localOrderId: Number(localOrderId),
      amount: amt,
      status: OrderStatus.PENDING,
      paymentLinkId: paymentLink.paymentLinkId,
      checkoutUrl: paymentLink.checkoutUrl,
      qrCode: paymentLink.qrCode,
      items: paymentItems,
      createdAt: nowMs(),
      updatedAt: nowMs(),
      webhookData: null,
    };

    orders.set(orderCode, record);

    return res.json({
      orderCode,
      status: record.status,
      checkoutUrl: record.checkoutUrl,
      qrCode: record.qrCode,
      paymentLinkId: record.paymentLinkId,
    });
  } catch (error) {
    console.error("create-payment error:", error);
    return res.status(500).json({
      message: error?.message || "Internal error",
    });
  }
});

/**
 * 2) Webhook
 * PayOS sẽ gửi dữ liệu thanh toán về đây.
 * SDK có webhooks.verify() để xác thực dữ liệu webhook.
 * NOTE: Webhook là nguồn sự thật (source of truth). Return/cancel chỉ là redirect UI.
 */
// app.post("/webhook", (req, res) => {

//   try {
//     console.log("🔥 Webhook received:", req.body);

//     const webhookData = payOS.webhooks.verify(req.body);
//     console.log("✅ Webhook verified:", webhookData);

//     const orderCode = Number(webhookData.orderCode);
//     const nextStatus = webhookData.code === "00" ? OrderStatus.PAID : OrderStatus.FAILED;

//     const existing = orders.get(orderCode);
//     if (existing) {
//       // Idempotent: nếu đã chốt PAID/CANCELLED/FAILED thì không lùi về PENDING
//       const terminal = [OrderStatus.PAID, OrderStatus.CANCELLED, OrderStatus.FAILED];
//       if (!terminal.includes(existing.status)) {
//         existing.status = nextStatus;
//       }
//       existing.webhookData = webhookData;
//       existing.updatedAt = nowMs();
//       orders.set(orderCode, existing);
//     } else {
//       // Trường hợp: webhook tới trước khi /create-payment lưu map (hiếm)
//       orders.set(orderCode, {
//         orderCode,
//         localOrderId: null,
//         amount: null,
//         status: nextStatus,
//         paymentLinkId: webhookData.paymentLinkId || null,
//         checkoutUrl: null,
//         qrCode: null,
//         items: [],
//         createdAt: nowMs(),
//         updatedAt: nowMs(),
//         webhookData,
//       });
//     }

//     console.log("Webhook verified:", webhookData);
//     return res.status(200).send("OK");
//   } catch (error) {
//     console.error("Invalid webhook:", error?.message || error);
//     return res.status(400).send("Invalid webhook");
//   }
// });


app.post("/webhook", async (req, res) => {
  try {
    console.log("🔥 Webhook received:", req.body);

    // await
    const webhookData = await payOS.webhooks.verify(req.body);

    console.log("✅ Webhook verified:", webhookData);

    // lấy đúng data
    const orderCode = Number(webhookData.orderCode);
    const paymentLinkId = webhookData.paymentLinkId;

    // xác định trạng thái
    const nextStatus =
      webhookData.code === "00"
        ? OrderStatus.PAID
        : OrderStatus.FAILED;

    if (!orderCode) {
      console.log("Missing orderCode in webhook");
      return res.status(400).send("Invalid data");
    }

    const existing = orders.get(orderCode);

    if (existing) {
      const terminal = [
        OrderStatus.PAID,
        OrderStatus.CANCELLED,
        OrderStatus.FAILED,
      ];

      // idempotent: không overwrite trạng thái cuối
      if (!terminal.includes(existing.status)) {
        existing.status = nextStatus;
      }

      existing.webhookData = webhookData;
      existing.updatedAt = Date.now();

      orders.set(orderCode, existing);

      console.log("Updated order:", orderCode, "=>", existing.status);
    } else {
      // fallback nếu webhook tới trước create-payment
      orders.set(orderCode, {
        orderCode,
        localOrderId: null,
        amount: webhookData.amount || null,
        status: nextStatus,
        paymentLinkId,
        checkoutUrl: null,
        qrCode: null,
        items: [],
        createdAt: Date.now(),
        updatedAt: Date.now(),
        webhookData,
      });

      console.log(" Created order from webhook:", orderCode);
    }

    return res.status(200).send("OK");
  } catch (error) {
    console.error(" Invalid webhook:", error?.message || error);
    return res.status(400).send("Invalid webhook");
  }
});








/**
 * 3) App polling: lấy trạng thái theo orderCode
 * Response chuẩn: { orderCode, localOrderId, status }
 */
app.get("/payment-status/:orderCode", (req, res) => {
  const orderCode = Number(req.params.orderCode);
  const order = orders.get(orderCode);
  if (!order) {
    return res.status(404).json({ message: "Order not found" });
  }
  return res.json({
    orderCode: order.orderCode,
    localOrderId: order.localOrderId,
    status: normalizeStatus(order.status),
    paymentLinkId: order.paymentLinkId,
    updatedAt: order.updatedAt,
  });
});

/**
 * 4) Debug: xem record đầy đủ (giữ lại endpoint cũ để không phá app đang gọi)
 */
app.get("/order/:orderCode", (req, res) => {
  const orderCode = Number(req.params.orderCode);
  const order = orders.get(orderCode);

  if (!order) {
    return res.status(404).json({ message: "Order not found" });
  }

  return res.json(order);
});

/**
 * 5) (Tuỳ chọn) Khi user bấm "Huỷ" bên phía PayOS.
 * Không nên dùng endpoint này để quyết định thành công/thất bại, chỉ hỗ trợ UI.
 */
app.post("/mark-cancelled", (req, res) => {
  const { orderCode } = req.body || {};
  const oc = Number(orderCode);
  const order = orders.get(oc);
  if (!order) {
    return res.status(404).json({ message: "Order not found" });
  }

  const terminal = [OrderStatus.PAID, OrderStatus.FAILED];
  if (!terminal.includes(order.status)) {
    order.status = OrderStatus.CANCELLED;
    order.updatedAt = nowMs();
    orders.set(oc, order);
  }

  return res.json({ orderCode: oc, status: order.status });
});

const PORT = Number(process.env.PORT || 3000);
const HOST = process.env.HOST || "0.0.0.0";
app.listen(PORT, HOST, () => {
  console.log(`Server running at http://${HOST}:${PORT}`);
});

