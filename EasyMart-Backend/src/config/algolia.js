const { algoliasearch } = require("algoliasearch");
const env = require("./env");

let algoliaClient;

function assertAlgoliaSearchConfig() {
  const missing = [];

  if (!env.algolia.appId) missing.push("ALGOLIA_APP_ID");
  if (!env.algolia.searchKey) missing.push("ALGOLIA_SEARCH_KEY");

  if (missing.length > 0) {
    throw new Error(`Missing Algolia environment variables: ${missing.join(", ")}`);
  }
}

function assertAlgoliaAdminConfig() {
  assertAlgoliaSearchConfig();

  if (!env.algolia.adminKey) {
    throw new Error("Missing Algolia environment variable: ALGOLIA_ADMIN_KEY");
  }
}

function getAlgoliaClient() {
  if (!algoliaClient) {
    assertAlgoliaAdminConfig();
    algoliaClient = algoliasearch(env.algolia.appId, env.algolia.adminKey);
  }

  return algoliaClient;
}

module.exports = {
  assertAlgoliaSearchConfig,
  getAlgoliaClient,
};
