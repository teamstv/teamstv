const https = require("https");

const options = {
  method: "GET",
  hostname: "api.weather.yandex.ru",
  path: "/v1/forecast?lat=59.941369&lon=30.275588&limit=1&hours=false&lang=en_US",
  port: 443,
  headers: {
    "X-Yandex-API-Key": "53ad075a-bbbf-479e-831a-40dd59a5a97d"
  }
};

var request = https.request(options, (resource) => {
    resource.on("data", (data) => {
        process.stdout.write(data);
    });
});
request.end();

request.on("error", (error) => {
    console.error(error);
});
