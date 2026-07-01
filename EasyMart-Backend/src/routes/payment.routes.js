const express = require("express");
const paymentController = require("../controllers/payment.controller");
const asyncHandler = require("../utils/asyncHandler");

const router = express.Router();

router.get("/", paymentController.health);
router.post("/create-payment", asyncHandler(paymentController.createPayment));
router.post("/webhook", asyncHandler(paymentController.handleWebhook));
router.get(
  "/payment-status/:orderCode",
  asyncHandler(paymentController.getPaymentStatus)
);
router.get("/order/:orderCode", paymentController.getOrder);
router.post("/mark-cancelled", paymentController.markCancelled);

module.exports = router;
