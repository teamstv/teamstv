const https = require("https");
const express = require("express");
const cors = require("cors");
const app = express();

app.use(cors());

const options = {
  method: "GET",
  hostname: "api.weather.yandex.ru",
  path: "/v1/forecast?lat=59.941369&lon=30.275588&limit=1&hours=false&lang=en_US",
  port: 443,
  headers: {
    "X-Yandex-API-Key": "53ad075a-bbbf-479e-831a-40dd59a5a97d",
    "Content-Type": "application/json"
  }
};

app.get("/", (req, res, next) => {

  console.log("Get weather", new Date());

  res.set("Content-Type", "application/json");
  res.set("Connection", "close");

  const request = https.request(options, (resource) => {
    resource.on("data", (data) => {
      res.status(200).write(data, () => {
        setTimeout(() => { 
          res.end(); 
        }, 3000);
      });
    });
  });
  request.end();

  request.on("error", (error) => {
      console.error(error);
  });
});
 
app.listen(3000, () => {
  console.log("Weather server started on port 3000");
});
