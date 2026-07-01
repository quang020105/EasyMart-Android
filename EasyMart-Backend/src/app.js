const express = require("express");
const cors = require("cors");
const morgan = require("morgan");
const env = require("./config/env");
const paymentRoutes = require("./routes/payment.routes");
const imageSearchRoutes = require("./routes/imageSearch.routes");
const AppError = require("./utils/AppError");

const app = express();

app.disable("x-powered-by");

app.use(cors(env.cors));
app.use(express.json({ limit: env.requestLimit }));
app.use(express.urlencoded({ extended: true }));

if (env.nodeEnv !== "test") {
  app.use(morgan("dev"));
}

app.get("/health", (req, res) => {
  res.json({
    service: "EasyMart Backend",
    status: "ok",
    environment: env.nodeEnv,
  });
});

app.use("/", paymentRoutes);
app.use("/", imageSearchRoutes);
app.use("/api/payments", paymentRoutes);
app.use("/api/search", imageSearchRoutes);

app.use((req, res, next) => {
  next(new AppError(`Route not found: ${req.method} ${req.originalUrl}`, 404));
});

app.use((err, req, res, next) => {
  const statusCode = err.statusCode || 500;
  const payload = {
    message: err.message || "Internal server error",
  };

  if (err.details) {
    payload.details = err.details;
  }

  if (env.nodeEnv !== "production" && statusCode >= 500) {
    payload.stack = err.stack;
  }

  if (statusCode >= 500) {
    console.error(err);
  }

  res.status(statusCode).json(payload);
});

module.exports = app;
