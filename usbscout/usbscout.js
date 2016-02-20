var express = require('express');
var app = express();

var redis = require('redis');
var client = redis.createClient('/tmp/redis.sock');

var teamlist = [];

var fields = ["a_reach"]

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

function refreshTeamlist() {
    client.keys("team:*", function(err, reply) {
        teamlist = [];
        for (var i = 0; i < reply.length; i++) {
            console.log("Added to teamlist:" + reply[i].split(":")[1])
            teamlist.push(reply[i].split(":")[1]);
        }
    });
}

function doesTeamExist(teamid) {
    return (teamlist.indexOf(teamid) != -1);
    console.log("team " + teamid + " not found")
    console.log(teamlist.indexOf(teamid));
}


// BEGIN HANDLERS
function getInfo(teamid, req, res) {
    var teamexists = doesTeamExist(teamid);

    if (teamexists) {
        client.hgetall("team:" . teamid, function(err, reply) {
            if (err) {
                res.json({});
                res.status(500).json({ error: "It broke."});
            } else {
                res.json(reply);
            }
        });
    } else {
        res.status(404).json({ error: "No such team."});
    }
}

function setInfo(teamid, req, res) {
    var overwrite = (req.param("overwrite") !== undefined);
    var teamexists = doesTeamExist(teamid);

    if (overwrite || !teamexists) {
        for (var i = 0; i < fields.length; i++) {
            var value = (req.param(fields[i]));
            var b = client.hset("team:" + teamid, fields[i], value);
        }
        teamlist.push(teamid);
        res.status(teamexists ? 204 : 201).json({ success: "The team information was added successfully."});
    } else {
        res.status(500).json({ error: "Team already exists."});
    }
}

function delTeam(teamid, req, res) {
    var teamexists = doesTeamExist(teamid)

    if (teamexists) {
        client.del("team:" + teamid);
        teamlist.remove(teamlist.indexOf(teamid));
        res.status(200).json({ success: "The team was removed successfully."})
    } else {
        res.status(404).json({ error: "No such team."});
    }
}

function listTeams(req, res) {
    res.status(200).json(teamlist);
}
// END HANDLERS


// BEGIN "MOUNT"
app.get('/', function(req, res) {
    res.send("Default page");
});

app.get('/api/:teamid/get', function(req, res) {
    var teamid = req.params.teamid;
    console.log(JSON.stringify(res.teamid));
    getInfo(teamid, req, res);
});

app.get('/api/get', function(req, res) {
    var teamid = req.param('teamid');
    getInfo(teamid, req, res);
});

app.get('/api/:teamid/set', function(req, res) {
    var teamid = req.params.teamid;
    setInfo(teamid, req, res);
});

app.get('/api/set', function(req, res) {
    var teamid = req.param("teamid");
    setInfo(teamid, req, res);
});

app.put('/api/set', function(req, res) {
    var teamid = req.param("teamid");
    setInfo(teamid, req, res);
});

app.get('/api/:teamid/del', function(req, res) {
    var teamid = req.params.teamid;
    delTeam(teamid, req, res);
});

app.get('/api/del', function(req, res) {
    var teamid = req.param("teamid");
    delTeam(teamid, req, res);
});

app.delete("/api/del", function(req, res) {
    var teamid = req.params("teamId")
})

app.get('/api/list', function(req, res) {
    listTeams(req, res);
});
// END "MOUNT"


refreshTeamlist();

app.listen(80, function() {
    console.log('Server is up');
});
