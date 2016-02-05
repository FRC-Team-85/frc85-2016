var teamList;

function registerClickEvents() {
    document.getElementById("set").onclick = set;
    document.getElementById("get").onclick = get;
    document.getElementById("export").onclick=exportData;
    //document.getElementById("import").onclick=importData;
}

function onload() {
    if (localStorage.teamList) {
        teamList = JSON.parse(localStorage.teamList);
    } else {
        teamList = [];
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

    if (!teamList.includes(data["teamID"])) teamList.push(data["teamID"]);

    localStorage.setItem(data["teamID"], JSON.stringify(data));
    localStorage.teamList = JSON.stringify(teamList);

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

function get() {
    var teamID = document.getElementById("teamID").value;
    if (!teamList.includes(teamID)) return;

    var dataObject = JSON.parse(localStorage.getItem(teamID));
    unJSONify(dataObject);
}

function cacheDump() {
    var cacheJSON = [];
    for (var i = 0; i < teamList.length; i++) {
        cacheJSON.push(localStorage.getItem(teamList[i]));
    }
    return cacheJSON;
}

function exportData() {
    var textbox = document.getElementById("jsonData");
    textbox.value = JSON.stringify(cacheDump());
}

function importData(overwrite) {
    var textbox = document.getElementById("jsonData");
    var dataObject = JSON.parse(textbox.value);
    for (var i = 0; i < dataObject.length; i++) {
        if (!teamList.includes(dataObject[i]["teamID"])) {
            localStorage[dataObject[i]["teamID"]] = dataObject[i];
            teamList.push(dataObject[i]["teamID"]);
        } else if (override) {
            localStorage[dataObject[i]["teamID"]] = dataObject[i];
        }
    }
    localStorage.teamList = teamList;
}

function update(event) {
    window.applicationCache.swapCache();
}
