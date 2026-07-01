const paymentService = require("../services/payment.service");

function health(req, res) {
  res.send("Payment server is running");
}

async function createPayment(req, res) {
  const result = await paymentService.createPayment(req.body);
  res.json(result);
}

async function handleWebhook(req, res) {
  await paymentService.handleWebhook(req.body);
  res.status(200).send("OK");
}

async function getPaymentStatus(req, res) {
  const result = await paymentService.getPaymentStatus(req.params.orderCode);
  res.json(result);
}

function getOrder(req, res) {
  const result = paymentService.getOrder(req.params.orderCode);
  res.json(result);
}

function markCancelled(req, res) {
  const result = paymentService.markCancelled(req.body && req.body.orderCode);
  res.json(result);
}

module.exports = {
  health,
  createPayment,
  handleWebhook,
  getPaymentStatus,
  getOrder,
  markCancelled,
};
