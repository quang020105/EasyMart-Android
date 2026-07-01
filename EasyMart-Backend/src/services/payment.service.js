const { getPayOSClient } = require("../config/payos");
const AppError = require("../utils/AppError");

const ORDER_STATUS = Object.freeze({
  PENDING: "PENDING",
  PAID: "PAID",
  FAILED: "FAILED",
  CANCELLED: "CANCELLED",
});

const TERMINAL_ORDER_STATUSES = Object.freeze([
  ORDER_STATUS.PAID,
  ORDER_STATUS.FAILED,
  ORDER_STATUS.CANCELLED,
]);

const orders = new Map();

function nowMs() {
  return Date.now();
}

function normalizeStatus(input) {
  const status = String(input || "").toUpperCase();

  if (status === "PAID" || status === "SUCCESS") return ORDER_STATUS.PAID;
  if (status === "CANCELLED" || status === "CANCELED") return ORDER_STATUS.CANCELLED;
  if (status === "FAILED" || status === "FAIL") return ORDER_STATUS.FAILED;
  if (status === "PENDING" || status === "PROCESSING") return ORDER_STATUS.PENDING;

  return ORDER_STATUS.PENDING;
}

function createOrderCode() {
  let orderCode = nowMs();

  while (orders.has(orderCode)) {
    orderCode += 1;
  }

  return orderCode;
}

function buildPaymentItems(items, amount) {
  if (Array.isArray(items) && items.length > 0) {
    return items.map((item) => ({
      name: String(item.name || "San pham"),
      quantity: Number(item.quantity || 1),
      price: Number(item.price || 0),
    }));
  }

  return [
    {
      name: "San pham EasyMart",
      quantity: 1,
      price: amount,
    },
  ];
}

async function createPayment(payload = {}) {
  const {
    amount = 10000,
    description = "Thanh toan cho EasyMart",
    localOrderId,
    items = [],
  } = payload;

  if (localOrderId === undefined || localOrderId === null) {
    throw new AppError("localOrderId is required", 400);
  }

  const normalizedAmount = Number(amount);
  if (!Number.isFinite(normalizedAmount) || normalizedAmount <= 0) {
    throw new AppError("amount must be greater than 0", 400);
  }

  const orderCode = createOrderCode();
  const encodedLocalOrderId = encodeURIComponent(String(localOrderId));
  const encodedOrderCode = encodeURIComponent(String(orderCode));
  const returnUrl = `easymart://payos/return?localOrderId=${encodedLocalOrderId}&orderCode=${encodedOrderCode}`;
  const cancelUrl = `easymart://payos/cancel?localOrderId=${encodedLocalOrderId}&orderCode=${encodedOrderCode}`;
  const paymentItems = buildPaymentItems(items, normalizedAmount);
  const paymentLink = await getPayOSClient().paymentRequests.create({
    orderCode,
    amount: normalizedAmount,
    description,
    items: paymentItems,
    cancelUrl,
    returnUrl,
  });

  const record = {
    orderCode,
    localOrderId: Number(localOrderId),
    amount: normalizedAmount,
    status: ORDER_STATUS.PENDING,
    paymentLinkId: paymentLink.paymentLinkId,
    checkoutUrl: paymentLink.checkoutUrl,
    qrCode: paymentLink.qrCode,
    items: paymentItems,
    createdAt: nowMs(),
    updatedAt: nowMs(),
    webhookData: null,
  };

  orders.set(orderCode, record);

  return {
    orderCode,
    status: record.status,
    checkoutUrl: record.checkoutUrl,
    qrCode: record.qrCode,
    paymentLinkId: record.paymentLinkId,
  };
}

async function handleWebhook(payload) {
  const webhookData = await getPayOSClient().webhooks.verify(payload);
  const orderCode = Number(webhookData.orderCode);

  if (!orderCode) {
    throw new AppError("Invalid webhook data: missing orderCode", 400);
  }

  const nextStatus =
    webhookData.code === "00" ? ORDER_STATUS.PAID : ORDER_STATUS.FAILED;
  const existing = orders.get(orderCode);

  if (existing) {
    if (!TERMINAL_ORDER_STATUSES.includes(existing.status)) {
      existing.status = nextStatus;
    }

    existing.webhookData = webhookData;
    existing.updatedAt = nowMs();
    orders.set(orderCode, existing);

    return existing;
  }

  const createdFromWebhook = {
    orderCode,
    localOrderId: null,
    amount: webhookData.amount || null,
    status: nextStatus,
    paymentLinkId: webhookData.paymentLinkId || null,
    checkoutUrl: null,
    qrCode: null,
    items: [],
    createdAt: nowMs(),
    updatedAt: nowMs(),
    webhookData,
  };

  orders.set(orderCode, createdFromWebhook);
  return createdFromWebhook;
}

async function getPaymentStatus(orderCodeInput) {
  const orderCode = Number(orderCodeInput);
  const order = orders.get(orderCode);

  if (!order) {
    throw new AppError("Order not found", 404);
  }

  if (order.status === ORDER_STATUS.PENDING) {
    const payosOrder = await getPayOSClient().paymentRequests.get(orderCode);
    order.status = normalizeStatus(payosOrder.status);
    order.updatedAt = nowMs();
    orders.set(orderCode, order);
  }

  return {
    orderCode: order.orderCode,
    localOrderId: order.localOrderId,
    status: normalizeStatus(order.status),
    paymentLinkId: order.paymentLinkId,
    updatedAt: order.updatedAt,
  };
}

function getOrder(orderCodeInput) {
  const orderCode = Number(orderCodeInput);
  const order = orders.get(orderCode);

  if (!order) {
    throw new AppError("Order not found", 404);
  }

  return order;
}

function markCancelled(orderCodeInput) {
  const orderCode = Number(orderCodeInput);
  const order = orders.get(orderCode);

  if (!order) {
    throw new AppError("Order not found", 404);
  }

  if (![ORDER_STATUS.PAID, ORDER_STATUS.FAILED].includes(order.status)) {
    order.status = ORDER_STATUS.CANCELLED;
    order.updatedAt = nowMs();
    orders.set(orderCode, order);
  }

  return {
    orderCode,
    status: order.status,
  };
}

module.exports = {
  createPayment,
  handleWebhook,
  getPaymentStatus,
  getOrder,
  markCancelled,
  normalizeStatus,
};
