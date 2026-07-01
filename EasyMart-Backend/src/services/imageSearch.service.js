const crypto = require("crypto");
const env = require("../config/env");
const { assertAlgoliaSearchConfig } = require("../config/algolia");
const AppError = require("../utils/AppError");

function generateSecuredApiKey(searchKey, restrictions) {
  const queryString = Object.keys(restrictions)
    .sort()
    .map((key) => {
      const value = Array.isArray(restrictions[key])
        ? restrictions[key].join(",")
        : restrictions[key];

      return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
    })
    .join("&");

  const hash = crypto
    .createHmac("sha256", searchKey)
    .update(queryString)
    .digest("hex");

  return Buffer.from(`${hash}${queryString}`).toString("base64");
}

function createSecuredSearchKey(payload = {}) {
  assertAlgoliaSearchConfig();

  const index = String(payload.index || env.algolia.defaultIndex).trim();
  const ttlSeconds = Number(payload.ttlSeconds || env.algolia.defaultTtlSeconds);

  if (!index) {
    throw new AppError("index is required", 400);
  }

  if (!Number.isFinite(ttlSeconds) || ttlSeconds <= 0) {
    throw new AppError("ttlSeconds must be greater than 0", 400);
  }

  const validUntil = Math.floor(Date.now() / 1000) + ttlSeconds;
  const apiKey = generateSecuredApiKey(env.algolia.searchKey, {
    restrictIndices: index,
    validUntil,
  });

  return {
    appId: env.algolia.appId,
    apiKey,
    index,
    validUntil,
  };
}

module.exports = {
  createSecuredSearchKey,
  generateSecuredApiKey,
};
