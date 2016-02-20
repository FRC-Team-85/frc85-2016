var express = require('express');
var app = express();

var redis = require('redis');
var client = redis.createClient('/tmp/redis.sock');

var fields = ["a_reach"]

app.get('/', function(req, res) {
    res.send("Default page");
});

app.get('/api/:teamid/get', function(req, res) {
    var teamid = res.teamid;
    client.hgetall("team:" . teamid, function(err, reply) {
        if (err) {
            res.json({});
            res.status(500).json({ error: "It broke."});
        } else {
            res.json(reply);
        }
    });
});

app.get('/api/:teamid/set', function(req, res) { //should be put eventually
  var teamid = res.teamid;
  for (var i = 0; i < fields.length; i++) {
      var value = (req.param(fields[i]));
      client.hset("team:85", fields[i], value);
      res.json(req.params);
      console.log(JSON.stringify(req.params));
  }
});

app.listen(80, function() {
    console.log('Server is up');
});
