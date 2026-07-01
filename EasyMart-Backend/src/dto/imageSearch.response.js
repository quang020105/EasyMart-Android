function buildImageSearchResponse(data) {
  return {
    success: true,
    message: "Image analyzed successfully",
    data: {
      productName: data.productName,
      brand: data.brand,
      category: data.category,
      confidence: data.confidence,
      products: [],
    },
  };
}

module.exports = {
  buildImageSearchResponse,
};
