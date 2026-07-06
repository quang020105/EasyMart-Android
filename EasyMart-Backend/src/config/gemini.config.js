const env = require("./env");
const AppError = require("../utils/AppError");

const geminiConfig = {
  apiKey: process.env.GEMINI_API_KEY,
  model: process.env.GEMINI_MODEL || "gemini-1.5-flash",
  apiVersion: process.env.GEMINI_API_VERSION || "v1beta",
};

function assertGeminiConfig() {
  if (!geminiConfig.apiKey) {
    throw new AppError("Gemini is not configured", 503, {
      reason: "Missing Gemini environment variable: GEMINI_API_KEY",
    });
  }
}

function getGenerateContentUrl() {
  assertGeminiConfig();

  const encodedModel = encodeURIComponent(geminiConfig.model);
  const encodedApiKey = encodeURIComponent(geminiConfig.apiKey);

  return `https://generativelanguage.googleapis.com/${geminiConfig.apiVersion}/models/${encodedModel}:generateContent?key=${encodedApiKey}`;
}

module.exports = {
  ...geminiConfig,
  nodeEnv: env.nodeEnv,
  getGenerateContentUrl,
};
