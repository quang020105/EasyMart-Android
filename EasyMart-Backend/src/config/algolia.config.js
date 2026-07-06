const env = require("./env");
const { assertAlgoliaSearchConfig, getAlgoliaClient } = require("./algolia");

const algoliaConfig = {
  indexName: env.algolia.defaultIndex || "products",
  hitsPerQuery: 10,
};

function assertImageSearchAlgoliaConfig() {
  assertAlgoliaSearchConfig();

  if (!env.algolia.adminKey) {
    throw new Error("Missing Algolia environment variable: ALGOLIA_ADMIN_KEY");
  }
}

module.exports = {
  algoliaConfig,
  assertImageSearchAlgoliaConfig,
  getAlgoliaClient,
};
