function normalizeNumber(value, fallback = 0) {
  const number = Number(value);
  return Number.isFinite(number) ? number : fallback;
}

function normalizeConfidence(value) {
  const confidence = normalizeNumber(value, 0);
  return Math.min(Math.max(confidence, 0), 1);
}

function normalizeString(value) {
  return String(value || "").trim();
}

function mapRatingDto(rating) {
  return {
    rate: normalizeNumber(rating?.rate, 0),
    count: normalizeNumber(rating?.count, 0),
  };
}

function mapProductDto(product) {
  const name = normalizeString(product.name || product.title);
  const imageUrl = normalizeString(product.imageUrl || product.image);

  const priceVnd = normalizeNumber(product.priceVnd ?? product.price, 0);

  return {
    id: product.id,
    name,
    title: name,
    price: priceVnd,
    priceVnd,
    currency: "VND",
    description: normalizeString(product.description),
    category: normalizeString(product.category),
    imageUrl,
    image: imageUrl,
    imageUrls: Array.isArray(product.imageUrls)
      ? product.imageUrls.map(normalizeString).filter(Boolean)
      : [],
    rating: mapRatingDto(product.rating),
    score: normalizeNumber(product.score, 0),
  };
}

class ResponseMapper {
  static toImageSearchResponse(result, message = "Image analyzed successfully") {
    const products = Array.isArray(result?.products) ? result.products : [];

    return {
      success: true,
      message,
      data: {
        confidence: normalizeConfidence(result?.confidence),
        products: products.map(mapProductDto),
      },
    };
  }
}

module.exports = ResponseMapper;
module.exports._internals = {
  mapProductDto,
  mapRatingDto,
  normalizeConfidence,
};
