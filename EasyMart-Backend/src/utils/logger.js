const env = require("../config/env");

const LEVELS = {
  error: 0,
  warn: 1,
  info: 2,
  debug: 3,
};

const configuredLevel =
  process.env.LOG_LEVEL ||
  (env.nodeEnv === "production" ? "info" : "debug");
const activeLevel = LEVELS[configuredLevel] ?? LEVELS.info;

function shouldLog(level) {
  return LEVELS[level] <= activeLevel;
}

function serializeMeta(meta) {
  if (!meta) {
    return "";
  }

  try {
    return ` ${JSON.stringify(meta)}`;
  } catch (_) {
    return " [unserializable-meta]";
  }
}

function write(level, message, meta) {
  if (!shouldLog(level)) {
    return;
  }

  const timestamp = new Date().toISOString();
  const line = `[${timestamp}] ${level.toUpperCase()} ${message}${serializeMeta(
    meta
  )}`;

  if (level === "error") {
    console.error(line);
    return;
  }

  if (level === "warn") {
    console.warn(line);
    return;
  }

  console.log(line);
}

module.exports = {
  debug(message, meta) {
    write("debug", message, meta);
  },
  error(message, meta) {
    write("error", message, meta);
  },
  info(message, meta) {
    write("info", message, meta);
  },
  warn(message, meta) {
    write("warn", message, meta);
  },
};
