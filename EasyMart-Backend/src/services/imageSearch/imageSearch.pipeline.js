const AppError = require("../../utils/AppError");
const logger = require("../../utils/logger");
const AlgoliaService = require("./algolia.service");
const GeminiService = require("./gemini.service");
const ProductFilterService = require("./productFilter.service");
const RankingService = require("./ranking.service");
const ResponseParser = require("./response.parser");
const SearchPlanBuilder = require("./searchPlan.builder");
const { validateImageBuffer } = require("./validation.service");

function summarizeProduct(product) {
  return {
    id: product.id,
    name: product.name || product.title,
    brand: product.brand,
    category: product.category,
    stockQuantity: product.stockQuantity,
    isVisible: product.isVisible,
    score: product.score,
  };
}

function summarizeProducts(products, limit = 5) {
  return (products || []).slice(0, limit).map(summarizeProduct);
}

class ImageSearchPipeline {
  static async execute(imageBuffer) {
    validateImageBuffer(imageBuffer);

    try {
      logger.info("ImageSearch input accepted", {
        bytes: imageBuffer.length,
      });

      const rawResponse = await GeminiService.analyzeImage(imageBuffer);
      logger.debug("ImageSearch Gemini raw response received", {
        candidateCount: rawResponse?.candidates?.length || 0,
        hasPromptFeedback: Boolean(rawResponse?.promptFeedback),
      });

      const intent = ResponseParser.parse(rawResponse);
      logger.info("ImageSearch AI analysis parsed", {
        productName: intent.productName,
        brand: intent.brand,
        category: intent.category,
        confidence: intent.confidence,
      });

      const searchPlan = SearchPlanBuilder.build(intent);
      logger.info("ImageSearch search plan built", {
        queries: searchPlan.queries,
        filters: searchPlan.filters,
        category: searchPlan.category,
      });

      const candidateProducts = await AlgoliaService.searchCandidates(searchPlan);
      logger.info("ImageSearch candidate products received", {
        count: candidateProducts.length,
        sample: summarizeProducts(candidateProducts),
      });

      const filteredProducts = ProductFilterService.filter(candidateProducts);
      logger.info("ImageSearch products filtered", {
        before: candidateProducts.length,
        after: filteredProducts.length,
        removed: candidateProducts.length - filteredProducts.length,
        sample: summarizeProducts(filteredProducts),
      });

      const products = RankingService.rank(filteredProducts, intent);
      logger.info("ImageSearch products ranked", {
        count: products.length,
        sample: summarizeProducts(products),
      });

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
