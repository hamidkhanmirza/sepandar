var parseTagTextItems = function(inputData) {
    var json = JSON.parse(inputData);
    json["rFID"] = json["RFID"];
    return JSON.stringify(json);
}