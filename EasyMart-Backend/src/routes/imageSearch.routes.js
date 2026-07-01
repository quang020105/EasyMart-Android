const express = require("express");
const imageSearchController = require("../controllers/imageSearch.controller");
const { uploadImage } = require("../config/multer.config");
const asyncHandler = require("../utils/asyncHandler");

const router = express.Router();

router.post(
  "/image",
  uploadImage,
  asyncHandler(imageSearchController.searchByImage)
);
router.post("/key", asyncHandler(imageSearchController.createSecuredKey));
router.post(
  "/algolia/key",
  asyncHandler(imageSearchController.createSecuredKey)
);

module.exports = router;
