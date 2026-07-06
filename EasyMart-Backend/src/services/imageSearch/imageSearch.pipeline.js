const AppError = require("../../utils/AppError");
const logger = require("../../utils/logger");
const AlgoliaService = require("./algolia.service");
const GeminiService = require("./gemini.service");
const ProductFilterService = require("./productFilter.service");
const RankingService = require("./ranking.service");
const ResponseParser = require("./response.parser");
const SearchPlanBuilder = require("./searchPlan.builder");
const { validateImageBuffer } = require("./validation.service");

class ImageSearchPipeline {
  static async execute(imageBuffer) {
    validateImageBuffer(imageBuffer);

    try {
      logger.info("Image search started", {
        bytes: imageBuffer.length,
      });

      const rawResponse = await GeminiService.analyzeImage(imageBuffer);
      const intent = ResponseParser.parse(rawResponse);
      const searchPlan = SearchPlanBuilder.build(intent);
      const candidateProducts = await AlgoliaService.searchCandidates(searchPlan);
      const filteredProducts = ProductFilterService.filter(candidateProducts);
      const products = RankingService.rank(filteredProducts, intent);

      logger.info("Image search completed", {
        candidateCount: candidateProducts.length,
        filteredCount: filteredProducts.length,
        resultCount: products.length,
        confidence: intent.confidence,
      });

      return {
        ...intent,
        products,
      };
    } catch (error) {
      logger.warn("Image search failed", {
        message: error.message,
        statusCode: error.statusCode,
      });

      if (error instanceof AppError) {
        throw error;
      }

      throw new AppError("Image search failed", 500, {
        reason: error.message,
      });
    }
  }
}

module.exports = ImageSearchPipeline;
