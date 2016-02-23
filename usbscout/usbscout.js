var express = require('express');
var app = express();

var redis = require('redis');

//Uses redis socket at /tmp/redis.sock or specified by command-line argument
redisSock = (process.argv.length > 2) ? process.argv[2] : '/tmp/redis.sock';
var client = redis.createClient(redisSock);


//teamlist is an associative array where the keys are the team numbers
//and the values are arrays of the matches that redis has data on
var teamlist = {};

//fields is an array of fields (duh). This is used to determine what data to
//keep track of later on
var fields = ['a_reach']

//refreshTeamlist scrapes redis for team data and updates the teamlist variable
function refreshTeamlist() {
    client.keys('team:*', function(err, reply) {
        teamlist = {};
        for (var i = 0; i < reply.length; i++) {
            var teamid = reply[i].split(':')[1];
            var matchid = reply[i].split(':')[2];

            if (teamid === null) break;

            if (teamlist[teamid] === undefined) {
                teamlist[teamid] = [];
            }
            teamlist[teamid].push(matchid);
            console.log('Added team ' + teamid + ' match ' + matchid + ' to js teamlist');
        }
    });
}

function doesTeamExist(teamid) {
    return teamlist[teamid] !== undefined;
}

function doesMatchDataExist(teamid, matchid) {
    return teamlist[teamid].indexOf(matchid)!=-1;
}


// BEGIN HANDLERS
function checkTeamid(req, res, next) {
    var teamid = req.params.teamid;
    if (teamid === undefined) {
        res.status(500).json({ error: 'No team specified.'});
        return;
    } else next();
}

function checkMatchid(req, res, next) {
    var matchid = req.params.matchid;
    if (matchid === undefined) {
        res.status(500).json({ error: 'No match specified.'});
        return;
    } else next();
}

function getInfo(req, res) {
    var teamid = req.params.teamid;
    var matchid = req.params.matchid;

    var teamexists = doesTeamExist(teamid);
    var matchexists = teamexists && doesMatchDataExist(teamid, matchid);

    if (teamexists && matchexists) {
        client.hgetall('team:' + teamid + ':' + matchid, function(err, reply) {
            if (err) {
                res.json({});
                res.status(500).json({ error: 'It broke.'});
            } else {
                res.json(reply);
            }
        });
    } else if (teamexists) {
        res.status(404).json({ error: 'No such match.'});
    } else {
        res.status(404).json({ error: 'No such team.'});
    }
}

function setInfo(req, res) {
    var teamid = req.params.teamid;
    var matchid = req.params.matchid;
    var overwrite = (req.query.overwrite !== undefined);

    var teamexists = doesTeamExist(teamid);
    var matchexists = teamexists && doesMatchDataExist(teamid, matchid);

    var modified = false;
    if (overwrite || !matchexists) {
        for (var i = 0; i < fields.length; i++) {
            var value = (req.params[fields[i]]);
            if (value !== undefined && value != '') {
                client.hset('team:' + teamid + ':' + matchid, fields[i], value);
                modified = true;
            }
        }
        if (modified) {
            if (!teamexists) {
                teamlist[teamid] = [];
            }
            teamlist[teamid].push(matchid);

            res.status(matchexists ? 204 : 201).json({ success: 'The team information was added successfully.'});
        } else {
            res.status(200).json({ success: 'No information provided.'});
        }
    } else {
        res.status(500).json({ error: 'Match data already exists.'});
    }
}

function delMatch(req, res) {
    var teamid = req.params.teamid;
    var matchid = req.params.matchid;

    var teamexists = doesTeamExist(teamid);
    var matchexists = teamexists && doesMatchDataExist(teamid, matchid);

    if (matchexists) {
        client.del('team:' + teamid + ':' + matchid);
        teamlist[teamid].remove(teamlist[teamid].indexOf(matchid));
        if (teamlist[teamid].length == 0) {
            delete teamlist[teamid];
        }
        res.status(200).json({ success: 'The match was removed successfully.'});
    } else if (teamexists) {
        res.status(404).json({ error: 'No such team.'});
    } else {
        res.status(404).json({ error: 'No such match.'});
    }
}

function delTeam(req, res) {
    var teamid = req.params.teamid;

    var teamexists = doesTeamExist(teamid);

    if (teamexists){
        var i;
        for (i = 0; i < teamlist[teamid].length; i++) {
            client.del('team:' + teamid + ':' + teamlist[teamid][i]);
        }
        res.status(200).json({ success: 'The team was removed successfully.'});
    } else {
        res.status(404).json({ error: 'No such team.'});
    }

}

function listTeams(req, res) {
    res.status(200).json(teamlist);
}

function listMatches(req, res) {
    var teamid = req.params.teamid;

    var teamexists = doesTeamExist(teamid);

    if (teamexists) {
        res.status(200).json(teamlist[teamid]);
    } else {
        res.status(404).json({error: 'No such team.'});
    }
}
// END HANDLERS


// BEGIN 'MOUNT'
app.get('/api/:teamid/:matchid/get', checkTeamid, checkMatchid, getInfo);

app.get('/api/:teamid/:matchid/set', checkTeamid, checkMatchid, setInfo);

app.get('/api/:teamid/:matchid/del', checkTeamid, checkMatchid, delMatch);

app.get('/api/tlist', listTeams);

app.get('/api/:teamid/mlist', checkTeamid, listMatches);

app.get('/dev/frtl', function(req, res) { //*F*orce *R*efresh *T*eam *L*ist
    refreshTeamlist();
    res.status(200).json({ success: "Lol ok."});
});

app.use('/', express.static('client'));

// END 'MOUNT'

refreshTeamlist();

app.engine('html', require('ejs').renderFile);
app.set('views', __dirname + '/client');

app.listen(80, function() {
    console.log('Server is up');
});
