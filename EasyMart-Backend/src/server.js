const app = require("./app");
const env = require("./config/env");

app.listen(env.port, env.host, () => {
  console.log(`EasyMart Backend running at http://${env.host}:${env.port}`);
});
