const path = require("path");
const fs = require("fs");
const dotenv = require("dotenv");

dotenv.config({
  path: process.env.DOTENV_CONFIG_PATH || path.resolve(__dirname, "../../.env"),
  quiet: true,
});

function loadSelectedEnv(filePath, keys) {
  if (!fs.existsSync(filePath)) {
    return;
  }

  const parsed = dotenv.parse(fs.readFileSync(filePath));

  keys.forEach((key) => {
    if (process.env[key] === undefined && parsed[key] !== undefined) {
      process.env[key] = parsed[key];
    }
  });
}

loadSelectedEnv(path.resolve(__dirname, "../../../backend-payment/.env"), [
  "PAYOS_CLIENT_ID",
  "PAYOS_API_KEY",
  "PAYOS_CHECKSUM_KEY",
]);

loadSelectedEnv(path.resolve(__dirname, "../../../backend-search/.env"), [
  "ALGOLIA_APP_ID",
  "ALGOLIA_ADMIN_KEY",
  "ALGOLIA_SEARCH_KEY",
  "ALGOLIA_DEFAULT_INDEX",
  "ALGOLIA_KEY_TTL_SECONDS",
]);

function toNumber(value, fallback) {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}

function toCorsOrigin(value) {
  if (!value || value === "*") {
    return "*";
  }

  return value
    .split(",")
    .map((origin) => origin.trim())
    .filter(Boolean);
}

const env = {
  nodeEnv: process.env.NODE_ENV || "development",
  host: process.env.HOST || "0.0.0.0",
  port: toNumber(process.env.PORT, 3000),
  requestLimit: process.env.REQUEST_LIMIT || "1mb",
  cors: {
    origin: toCorsOrigin(process.env.CORS_ORIGIN || "*"),
    methods: ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"],
    allowedHeaders: ["Content-Type", "Authorization"],
  },
  payos: {
    clientId: process.env.PAYOS_CLIENT_ID,
    apiKey: process.env.PAYOS_API_KEY,
    checksumKey: process.env.PAYOS_CHECKSUM_KEY,
  },
  algolia: {
    appId: process.env.ALGOLIA_APP_ID,
    adminKey: process.env.ALGOLIA_ADMIN_KEY,
    searchKey: process.env.ALGOLIA_SEARCH_KEY,
    defaultIndex: process.env.ALGOLIA_DEFAULT_INDEX || "products",
    defaultTtlSeconds: toNumber(process.env.ALGOLIA_KEY_TTL_SECONDS, 300),
  },
  imageSearch: {
    geminiTimeoutMs: toNumber(process.env.IMAGE_SEARCH_GEMINI_TIMEOUT_MS, 15000),
    algoliaTimeoutMs: toNumber(process.env.IMAGE_SEARCH_ALGOLIA_TIMEOUT_MS, 5000),
  },
};

module.exports = env;
