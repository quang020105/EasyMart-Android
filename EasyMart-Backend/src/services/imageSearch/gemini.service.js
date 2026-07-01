const geminiConfig = require("../../config/gemini.config");
const AppError = require("../../utils/AppError");
const PromptBuilder = require("./prompt.builder");

function detectImageMimeType(imageBuffer) {
  if (
    imageBuffer.length >= 3 &&
    imageBuffer[0] === 0xff &&
    imageBuffer[1] === 0xd8 &&
    imageBuffer[2] === 0xff
  ) {
    return "image/jpeg";
  }

  if (
    imageBuffer.length >= 8 &&
    imageBuffer[0] === 0x89 &&
    imageBuffer[1] === 0x50 &&
    imageBuffer[2] === 0x4e &&
    imageBuffer[3] === 0x47
  ) {
    return "image/png";
  }

  if (
    imageBuffer.length >= 12 &&
    imageBuffer.slice(0, 4).toString("ascii") === "RIFF" &&
    imageBuffer.slice(8, 12).toString("ascii") === "WEBP"
  ) {
    return "image/webp";
  }

  throw new AppError("Unsupported or invalid image format", 400);
}

class GeminiService {
  static async analyzeImage(imageBuffer) {
    if (!Buffer.isBuffer(imageBuffer) || imageBuffer.length === 0) {
      throw new AppError("A valid image buffer is required", 400);
    }

    const mimeType = detectImageMimeType(imageBuffer);
    const response = await fetch(geminiConfig.getGenerateContentUrl(), {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        contents: [
          {
            role: "user",
            parts: [
              {
                text: PromptBuilder.buildProductRecognitionPrompt(),
              },
              {
                inlineData: {
                  mimeType,
                  data: imageBuffer.toString("base64"),
                },
              },
            ],
          },
        ],
        generationConfig: {
          temperature: 0,
          responseMimeType: "application/json",
        },
      }),
    });

    const rawResponse = await response.json().catch(() => null);

    if (!response.ok) {
      throw new AppError("Gemini Vision request failed", 502, {
        status: response.status,
        error: rawResponse?.error?.message || response.statusText,
      });
    }

    return rawResponse;
  }
}

module.exports = GeminiService;
