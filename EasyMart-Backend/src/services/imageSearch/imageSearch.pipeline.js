const AppError = require("../../utils/AppError");
const GeminiService = require("./gemini.service");
const ResponseParser = require("./response.parser");

class ImageSearchPipeline {
  static async execute(imageBuffer) {
    if (!Buffer.isBuffer(imageBuffer) || imageBuffer.length === 0) {
      throw new AppError("A valid image buffer is required", 400);
    }

    const rawResponse = await GeminiService.analyzeImage(imageBuffer);
    return ResponseParser.parse(rawResponse);
  }
}

module.exports = ImageSearchPipeline;
