const imageSearchService = require("../services/imageSearch.service");
const ImageSearchPipeline = require("../services/imageSearch/imageSearch.pipeline");
const {
  buildImageSearchResponse,
} = require("../dto/imageSearch.response");
const AppError = require("../utils/AppError");
const logger = require("../utils/logger");

function createSecuredKey(req, res) {
  const result = imageSearchService.createSecuredSearchKey(req.body);
  res.json(result);
}

async function searchByImage(req, res) {
  if (!req.file || !req.file.buffer) {
    throw new AppError("Image file is required in field: image", 400);
  }

  logger.info("ImageSearch request received", {
    file: {
      fieldname: req.file.fieldname,
      originalname: req.file.originalname,
      mimetype: req.file.mimetype,
      size: req.file.size,
    },
    contentLength: req.headers["content-length"],
  });

  const result = await ImageSearchPipeline.execute(req.file.buffer);
  const response = buildImageSearchResponse(result);

  logger.info("ImageSearch response ready", {
    success: response.success,
    message: response.message,
    confidence: response.data.confidence,
    productCount: response.data.products.length,
    products: response.data.products.slice(0, 5).map((product) => ({
      id: product.id,
      name: product.name,
      category: product.category,
      score: product.score,
    })),
  });
  logger.debug("ImageSearch response payload", response);

  res.json(response);
}

module.exports = {
  createSecuredKey,
  searchByImage,
};
