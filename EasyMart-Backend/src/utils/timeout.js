const AppError = require("./AppError");

function isAbortError(error) {
  return error?.name === "AbortError";
}

function createTimeoutError(label, timeoutMs) {
  return new AppError(`${label} timed out`, 504, {
    timeoutMs,
  });
}

async function withTimeout(promise, timeoutMs, label) {
  if (!Number.isFinite(timeoutMs) || timeoutMs <= 0) {
    return promise;
  }

  let timeoutId;
  const timeoutPromise = new Promise((_, reject) => {
    timeoutId = setTimeout(() => {
      reject(createTimeoutError(label, timeoutMs));
    }, timeoutMs);
  });

  try {
    return await Promise.race([promise, timeoutPromise]);
  } finally {
    clearTimeout(timeoutId);
  }
}

module.exports = {
  createTimeoutError,
  isAbortError,
  withTimeout,
};
