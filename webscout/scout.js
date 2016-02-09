var teamList;
var dataArr;

function registerClickEvents() {
    document.getElementById("set").onclick = set;
    document.getElementById("get").onclick = get;
    document.getElementById("export").onclick = exportData;
    document.getElementById("import").onclick = importData;
}

function onload() {
    if (localStorage.teamList) {
        teamList = JSON.parse(localStorage.teamList);
        dataArr = JSON.parse(localStorage.cache);
    } else {
        teamList = [];
        dataArr = [];
    }
    registerClickEvents();
}

function jSONify() {
    var teamID = document.getElementById("teamID").value;
    var fields = document.getElementsByClassName("var");

    var dataObject = {};
    dataObject["teamID"] = teamID;

    console.log(fields.length);

    for (var i = 0; i < fields.length; i++) {
        var field = fields[i];
        if (field.tagName.toLowerCase() != "input") break;
        var fieldType = field.type;

        switch (fieldType) {
            case "checkbox":
            case "radio":
                console.log(field.id + " set to " + field.checked);
                dataObject[field.id] = field.checked;
                break;
            case "number":
            case "text":
                console.log(field.id + " set to " + field.value);
                dataObject[field.id] = field.value;
                break;
        }
    }
    return dataObject;
}

function set() {
    var data = jSONify();

    if (!teamList.includes(data["teamID"])) {
        teamList.push(data["teamID"]);
        localStorage.teamList = JSON.stringify(teamList);

        dataArr.push(data);

    } else {
        dataArr[getTeamIndex(data["teamID"])] = data;
    }

    localStorage.cache = JSON.stringify(dataArr);
}

function unJSONify(dataObject) {
    var fields = document.getElementsByClassName("var");

    for (var i = 0; i < fields.length; i++) {
        var field = fields[i];
        if (field.tagName.toLowerCase() != "input") break;
        var fieldType = field.type;

        switch (fieldType) {
            case "checkbox":
            case "radio":
                field.checked = dataObject[field.id];
                break;
            case "number":
            case "text":
                field.value = dataObject[field.id];
                break;
        }
    }
}

function getTeamIndex(teamID) {
    for (var i = 0; i < dataArr.length; i++) {
        if (dataArr[i]["teamID"] == teamID) return i;
    }
}

function get() {
    var teamID = document.getElementById("teamID").value;
    if (!teamList.includes(teamID)) return;

    var dataObject = dataArr[getTeamIndex(teamID)];
    unJSONify(dataObject);
}

function exportData() {
    var textbox = document.getElementById("jsonData");
    textbox.value = JSON.stringify(dataArr);
}

function importData(overwrite = true) {
    var textbox = document.getElementById("jsonData");
    var dataObject = JSON.parse(textbox.value);

    for (var i = 0; i < dataObject.length; i++) {
        if (!teamList.includes(dataObject[i]["teamID"])) {
            dataArr.push(dataObject[i]);
            teamList.push(dataObject[i]["teamID"]);

        } else if (overwrite) {
            dataArr[getTeamIndex(dataObject[i]["teamID"])] = dataObject[i];
        }
    }
    localStorage.teamList = JSON.stringify(teamList);
    localStorage.cache = JSON.stringify(dataArr);
}

function update(event) {
    window.applicationCache.swapCache();
}
