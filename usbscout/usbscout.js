var express = require('express');
var app = express();

var redis = require('redis');
redisSock = (process.argv.length > 2) ? process.argv[2] : '/tmp/redis.sock';
var client = redis.createClient(redisSock);

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
}


// BEGIN HANDLERS
function getInfo(teamid, req, res) {
    if (teamid === undefined) {
        res.status(500).json({ error: "No team specified."});
        return;
    }
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
    if (teamid === undefined) {
        res.status(500).json({ error: "No team specified."});
        return;
    }
    var teamexists = doesTeamExist(teamid);

    var modified = false;
    if (overwrite || !teamexists) {
        for (var i = 0; i < fields.length; i++) {
            var value = (req.param(fields[i]));
            if (value !== undefined && value != "") {
                client.hset("team:" + teamid, fields[i], value);
                modified = true;
            }
        }
        if (modified) {
            teamlist.push(teamid);
            res.status(teamexists ? 204 : 201).json({ success: "The team information was added successfully."});
        } else {
            res.status(200).json({ success: "No information provided."});
        }
    } else {
        res.status(500).json({ error: "Team already exists."});
    }
}

function delTeam(teamid, req, res) {
    if (teamid === undefined) {
        res.status(500).json({ error: "No team specified."});
        return;
    }
    var teamexists = doesTeamExist(teamid);

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
app.get('/api/:teamid/get', function(req, res) {
    var teamid = req.params.teamid;
    console.log(JSON.stringify(res.teamid));
    getInfo(teamid, req, res);
});

app.get('/api/get', function(req, res) {
    var teamid = req.query.teamid;
    getInfo(teamid, req, res);
});

app.get('/api/:teamid/set', function(req, res) {
    var teamid = req.params.teamid;
    setInfo(teamid, req, res);
});

app.get('/api/set', function(req, res) {
    var teamid = req.query.teamid;
    setInfo(teamid, req, res);
});

app.put('/api/:teamid/set', function(req, res) {
    var teamid = req.params.teamid;
    setInfo(teamid, req, res);
});

app.get('/api/:teamid/del', function(req, res) {
    var teamid = req.params.teamid;
    delTeam(teamid, req, res);
});

app.get('/api/del', function(req, res) {
    var teamid = req.query.teamid;
    delTeam(teamid, req, res);
});

app.delete("/api/:teamid/del", function(req, res) {
    var teamid = req.params.teamid;
    delTeam(teamid);
})

app.get('/api/list', function(req, res) {
    listTeams(req, res);
});

app.use('/', express.static("client"));

// END "MOUNT"


refreshTeamlist();

app.engine('html', require('ejs').renderFile);
app.set('views', __dirname + '/client');

app.listen(80, function() {
    console.log('Server is up');
});
