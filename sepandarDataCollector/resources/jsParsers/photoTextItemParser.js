var assignSplitteds = function(splitted) {
    var tempJson = {};
    var tempSplitted = splitted;
    tempJson["imageCode"] = tempSplitted[0];
    tempJson["plateType"] = tempSplitted[1];
    tempJson["plateText"] = tempSplitted[2];
    tempJson["laneCode"] = tempSplitted[3];
    tempJson["dateTime"] = tempSplitted[4];
    tempJson["camAccuracy"] = tempSplitted[5];
    tempJson["camCode"] = tempSplitted[6];
    tempJson["direction"] = tempSplitted[7];
    tempJson["classType"] = tempSplitted[8];
    tempJson["classAccuracy"] = tempSplitted[9];
    tempJson["tspCode"] = tempSplitted[10];
    tempJson["passageCode"] = tempSplitted[11];
    var p = tempSplitted[12].substr(0, tempSplitted[12].indexOf("."));
    tempJson["firstPicDateTime"] = p;
    tempJson["id"] = tempSplitted.join('_');
    return tempJson;
}

var parsePhotoTextItems = function(inputData) {
    var json = JSON.parse(inputData);

    var fco = json["FCO"];
    var fir = json["FIR"];
    var pfr = json["PFR"];
    var bco = json["BCO"];
    var bir = json["BIR"];
    var pba = json["PBA"];

    var fcoSplitted = fco.split("_");
    var firSplitted = fir.split("_");
    var pfrSplitted = pfr.split("_");
    var bcoSplitted = bco.split("_");
    var birSplitted = bir.split("_");
    var pbaSplitted = pba.split("_");

    var retJsonArray = [];

    retJsonArray.push(assignSplitteds(fcoSplitted));
    retJsonArray.push(assignSplitteds(firSplitted));
    retJsonArray.push(assignSplitteds(pfrSplitted));
    retJsonArray.push(assignSplitteds(bcoSplitted));
    retJsonArray.push(assignSplitteds(birSplitted));
    retJsonArray.push(assignSplitteds(pbaSplitted));

    return JSON.stringify(retJsonArray);
}