const imageSearchService = require("../services/imageSearch.service");
const ImageSearchPipeline = require("../services/imageSearch/imageSearch.pipeline");
const {
  buildImageSearchResponse,
} = require("../dto/imageSearch.response");
const AppError = require("../utils/AppError");

function createSecuredKey(req, res) {
  const result = imageSearchService.createSecuredSearchKey(req.body);
  res.json(result);
}

async function searchByImage(req, res) {
  if (!req.file || !req.file.buffer) {
    throw new AppError("Image file is required in field: image", 400);
  }

  const result = await ImageSearchPipeline.execute(req.file.buffer);
  res.json(buildImageSearchResponse(result));
}

module.exports = {
  createSecuredKey,
  searchByImage,
};
