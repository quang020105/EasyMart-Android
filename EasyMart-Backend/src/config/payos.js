const { PayOS } = require("@payos/node");
const env = require("./env");

let payOSClient;

function assertPayOSConfig() {
  const missing = [];

  if (!env.payos.clientId) missing.push("PAYOS_CLIENT_ID");
  if (!env.payos.apiKey) missing.push("PAYOS_API_KEY");
  if (!env.payos.checksumKey) missing.push("PAYOS_CHECKSUM_KEY");

  if (missing.length > 0) {
    throw new Error(`Missing PayOS environment variables: ${missing.join(", ")}`);
  }
}

function getPayOSClient() {
  if (!payOSClient) {
    assertPayOSConfig();
    payOSClient = new PayOS({
      clientId: env.payos.clientId,
      apiKey: env.payos.apiKey,
      checksumKey: env.payos.checksumKey,
    });
  }

  return payOSClient;
}

module.exports = {
  getPayOSClient,
};
