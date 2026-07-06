const AppError = require("../../utils/AppError");
const env = require("../../config/env");
const logger = require("../../utils/logger");
const { withTimeout } = require("../../utils/timeout");
const {
  algoliaConfig,
  assertImageSearchAlgoliaConfig,
  getAlgoliaClient,
} = require("../../config/algolia.config");
const {
  validateCandidateProducts,
  validateSearchPlan,
} = require("./validation.service");

function getProductKey(hit) {
  if (hit?.id != null) {
    return String(hit.id);
  }

  if (hit?.objectID != null) {
    return String(hit.objectID);
  }

  return null;
}

function toCandidateProduct(hit) {
  const rawId = hit.id ?? hit.objectID;
  const id = Number(rawId);
  const title = String(hit.title || hit.name || "").trim();
  const image = String(hit.image || hit.imageUrl || "").trim();

  return {
    id: Number.isFinite(id) ? id : rawId,
    title,
    name: String(hit.name || title).trim(),
    brand: String(hit.brand || "").trim(),
    price: Number(hit.price) || 0,
    description: String(hit.description || "").trim(),
    category: String(hit.category || "").trim(),
    image,
    imageUrl: String(hit.imageUrl || image).trim(),
    imageUrls: Array.isArray(hit.imageUrls) ? hit.imageUrls : [],
    stockQuantity: Number(hit.stockQuantity) || 0,
    soldQuantity: Number(hit.soldQuantity) || 0,
    popularity: Number(hit.popularity || hit.popularityScore) || 0,
    isVisible: hit.isVisible !== false,
    isDeleted: hit.isDeleted === true,
    status: hit.status,
    rating: {
      rate: Number(hit.rating?.rate) || 0,
      count: Number(hit.rating?.count) || 0,
    },
  };
}

function mergeUniqueCandidates(resultSets) {
  const candidates = [];
  const seenKeys = new Set();

  resultSets.forEach((hits) => {
    (hits || []).forEach((hit) => {
      const key = getProductKey(hit);

      if (!key || seenKeys.has(key)) {
        return;
      }

      seenKeys.add(key);
      candidates.push(toCandidateProduct(hit));
    });
  });

  return candidates;
}

async function searchByQuery(client, query) {
  const searchPromise = client.searchSingleIndex({
    indexName: algoliaConfig.indexName,
    searchParams: {
      query,
      hitsPerPage: algoliaConfig.hitsPerQuery,
    },
  });
  const response = await withTimeout(
    searchPromise,
    env.imageSearch.algoliaTimeoutMs,
    "Algolia search request"
  );

  return response.hits || [];
}

class AlgoliaService {
  static async searchCandidates(searchPlan) {
    validateSearchPlan(searchPlan);

    const queries = (searchPlan?.queries || []).filter(Boolean);

    if (queries.length === 0) {
      return [];
    }

    try {
      assertImageSearchAlgoliaConfig();
    } catch (error) {
      throw new AppError("Algolia is not configured", 503, {
        reason: error.message,
      });
    }

    const client = getAlgoliaClient();

    try {
      logger.debug("Searching Algolia candidates", {
        indexName: algoliaConfig.indexName,
        queryCount: queries.length,
        timeoutMs: env.imageSearch.algoliaTimeoutMs,
      });

      const resultSets = await Promise.all(
        queries.map((query) => searchByQuery(client, query))
      );

      const candidateProducts = mergeUniqueCandidates(resultSets);
      validateCandidateProducts(candidateProducts);

      logger.debug("Algolia candidates received", {
        candidateCount: candidateProducts.length,
      });

      return candidateProducts;
    } catch (error) {
      if (error instanceof AppError) {
        throw error;
      }

      throw new AppError("Algolia search failed", 502, {
        reason: error.message,
      });
    }
  }
}

module.exports = AlgoliaService;
module.exports._internals = {
  getProductKey,
  mergeUniqueCandidates,
  toCandidateProduct,
};
