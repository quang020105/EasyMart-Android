const multer = require("multer");
const AppError = require("../utils/AppError");

const MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;
const ALLOWED_IMAGE_MIME_TYPES = new Set([
  "image/jpeg",
  "image/png",
  "image/webp",
]);

const upload = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: MAX_IMAGE_SIZE_BYTES,
    files: 1,
  },
  fileFilter(req, file, callback) {
    if (!ALLOWED_IMAGE_MIME_TYPES.has(file.mimetype)) {
      callback(new AppError("Only JPEG, PNG, and WEBP images are allowed", 400));
      return;
    }

    callback(null, true);
  },
});

function uploadImage(req, res, next) {
  upload.single("image")(req, res, (err) => {
    if (!err) {
      next();
      return;
    }

    if (err instanceof multer.MulterError) {
      const message =
        err.code === "LIMIT_FILE_SIZE"
          ? "Image size must be less than or equal to 5MB"
          : err.message;

      next(new AppError(message, 400));
      return;
    }

    next(err);
  });
}

module.exports = {
  uploadImage,
  MAX_IMAGE_SIZE_BYTES,
  ALLOWED_IMAGE_MIME_TYPES,
};
