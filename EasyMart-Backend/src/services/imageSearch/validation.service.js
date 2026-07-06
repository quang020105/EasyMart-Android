const AppError = require("../../utils/AppError");

const MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;
const MIN_IMAGE_SIZE_BYTES = 12;
const ALLOWED_IMAGE_MIME_TYPES = new Set([
  "image/jpeg",
  "image/png",
  "image/webp",
]);

function validateImageBuffer(imageBuffer) {
  if (!Buffer.isBuffer(imageBuffer) || imageBuffer.length === 0) {
    throw new AppError("A valid image buffer is required", 400);
  }

  if (imageBuffer.length < MIN_IMAGE_SIZE_BYTES) {
    throw new AppError("Image file is too small or invalid", 400);
  }

  if (imageBuffer.length > MAX_IMAGE_SIZE_BYTES) {
    throw new AppError("Image size must be less than or equal to 5MB", 400);
  }
}

function validateSearchPlan(searchPlan) {
  if (
    !searchPlan ||
    typeof searchPlan !== "object" ||
    Array.isArray(searchPlan)
  ) {
    throw new AppError("Search plan must be an object", 500);
  }

  if (!Array.isArray(searchPlan.queries)) {
    throw new AppError("Search plan queries must be an array", 500);
  }
}

function validateCandidateProducts(candidateProducts) {
  if (!Array.isArray(candidateProducts)) {
    throw new AppError("Candidate products must be an array", 502);
  }
}

module.exports = {
  ALLOWED_IMAGE_MIME_TYPES,
  MAX_IMAGE_SIZE_BYTES,
  validateCandidateProducts,
  validateImageBuffer,
  validateSearchPlan,
};
