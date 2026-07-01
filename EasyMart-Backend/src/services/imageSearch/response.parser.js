const AppError = require("../../utils/AppError");

function extractResponseText(rawResponse) {
  const parts = rawResponse?.candidates?.[0]?.content?.parts || [];
  const text = parts
    .map((part) => part.text)
    .filter(Boolean)
    .join("\n")
    .trim();

  if (!text) {
    throw new AppError("Gemini response does not contain text", 502);
  }

  return text;
}

function stripJsonFences(text) {
  return text
    .replace(/^```(?:json)?/i, "")
    .replace(/```$/i, "")
    .trim();
}

function parseJson(text) {
  try {
    return JSON.parse(stripJsonFences(text));
  } catch (error) {
    throw new AppError("Gemini response is not valid JSON", 502, {
      parserError: error.message,
    });
  }
}

function normalizeConfidence(value) {
  const confidence = Number(value);

  if (!Number.isFinite(confidence)) {
    return 0;
  }

  return Math.min(Math.max(confidence, 0), 1);
}

function toImageSearchIntent(payload) {
  if (!payload || typeof payload !== "object" || Array.isArray(payload)) {
    throw new AppError("Gemini JSON response must be an object", 502);
  }

  return {
    productName: String(payload.productName || "").trim(),
    brand: String(payload.brand || "").trim(),
    category: String(payload.category || "").trim(),
    confidence: normalizeConfidence(payload.confidence),
  };
}

class ResponseParser {
  static parse(rawResponse) {
    const responseText = extractResponseText(rawResponse);
    const payload = parseJson(responseText);

    return toImageSearchIntent(payload);
  }
}

module.exports = ResponseParser;
