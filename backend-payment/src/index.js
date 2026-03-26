import express from "express";
import dotenv from "dotenv";
import { PayOS } from "@payos/node";

dotenv.config();

const app = express();
app.use(express.json());

const payOS = new PayOS({
  clientId: process.env.PAYOS_CLIENT_ID,
  apiKey: process.env.PAYOS_API_KEY,
  checksumKey: process.env.PAYOS_CHECKSUM_KEY,
});


const orders = new Map();

/**
 * 1) Tạo link thanh toán
 */

app.post("/create-payment", async (req, res) => {
  try {
    const { amount = 10000, description = "Thanh toán cho EasyMart", localOrderId , items = []} = req.body || {};
    const orderCode = Date.now();

    const returnUrl = `easymart://payos/return?localOrderId=${localOrderId}&orderCode=${orderCode}`;
    const cancelUrl = `easymart://payos/cancel?localOrderId=${localOrderId}&orderCode=${orderCode}`;


    //sản phẩm từ clinet hiển thị lên trang thanh toán PayOS
    const paymentItems = items.length > 0
      ? items.map((item) => ({
          name: item.name,
          quantity: Number(item.quantity),
          price: Number(item.price),
        }))
      : [
          {
            name: "Các sản phẩm từ EasyMart",
            quantity: 1,
            price: Number(amount),
          },
        ];


    const paymentData = {
      orderCode,
      amount: Number(amount),
      description,
      items: paymentItems,
      cancelUrl: cancelUrl,
      returnUrl: returnUrl
    };

    const paymentLink = await payOS.paymentRequests.create(paymentData);

    orders.set(orderCode, {
      orderCode,
      localOrderId,
      amount: Number(amount),
      status: "PENDING",
      paymentLinkId: paymentLink.paymentLinkId,
      checkoutUrl: paymentLink.checkoutUrl,
      items: paymentItems,
    });

    return res.json({
      orderCode,
      status: "PENDING",
      checkoutUrl: paymentLink.checkoutUrl,
      qrCode: paymentLink.qrCode,
      paymentLinkId: paymentLink.paymentLinkId,
    });
  } catch (error) {
    console.error("create-payment error:", error);
    return res.status(500).json({
      message: error.message,
    });
  }
});

/**
 * 2) Trang success
 * PayOS sẽ redirect về đây sau khi thanh toán thành công.
 */
app.get("/success", (req, res) => {
  const { orderCode, status, code, id, cancel } = req.query;

  if (orderCode && orders.has(Number(orderCode))) {
    const order = orders.get(Number(orderCode));
    order.status = status || "PAID";
    orders.set(Number(orderCode), order);
  }

  res.send(`
    <h1>Thanh toán thành công</h1>
    <p>orderCode: ${orderCode || ""}</p>
    <p>status: ${status || ""}</p>
    <p>code: ${code || ""}</p>
    <p>paymentLinkId: ${id || ""}</p>
    <p>cancel: ${cancel || ""}</p>
  `);
});

/**
 * 3) Trang cancel
 */
app.get("/cancel", (req, res) => {
  const { orderCode, status, code, id, cancel } = req.query;

  if (orderCode && orders.has(Number(orderCode))) {
    const order = orders.get(Number(orderCode));
    order.status = "CANCELLED";
    orders.set(Number(orderCode), order);
  }

  res.send(`
    <h1>Đã hủy thanh toán</h1>
    <p>orderCode: ${orderCode || ""}</p>
    <p>status: ${status || ""}</p>
    <p>code: ${code || ""}</p>
    <p>paymentLinkId: ${id || ""}</p>
    <p>cancel: ${cancel || ""}</p>
  `);
});

/**
 * 4) Webhook
 * PayOS sẽ gửi dữ liệu thanh toán về đây.
 * SDK có webhooks.verify() để xác thực dữ liệu webhook.
 */
app.post("/webhook", (req, res) => {
  try {
    const webhookData = payOS.webhooks.verify(req.body);
    console.log("Webhook verified:", webhookData);

    const orderCode = Number(webhookData.orderCode);

    if (orders.has(orderCode)) {
      const order = orders.get(orderCode);
      order.status = webhookData.code === "00" ? "PAID" : "FAILED";
      order.webhookData = webhookData;
      orders.set(orderCode, order);
    }

    return res.status(200).send("OK");
  } catch (error) {
    console.error("Invalid webhook:", error.message);
    return res.status(400).send("Invalid webhook");
  }
});

/**
 * 5) Xem trạng thái đơn hàng
 */
app.get("/order/:orderCode", (req, res) => {
  const orderCode = Number(req.params.orderCode);
  const order = orders.get(orderCode);

  if (!order) {
    return res.status(404).json({ message: "Order not found" });
  }

  return res.json(order);
});

app.get("/", (req, res) => {
  res.send("Payment server is running");
});

app.listen(3000, () => {
  console.log("Server running at http://localhost:3000");
});

// app.listen(3000, "0.0.0.0", () => {
//   console.log("Server running at http://0.0.0.0:3000");
// });