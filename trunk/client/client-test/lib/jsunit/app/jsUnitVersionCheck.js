/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
var versionRequest;

function isOutOfDate(newVersionNumber) {
    return JSUNIT_VERSION < newVersionNumber;
}

function sendRequestForLatestVersion(url) {
    versionRequest = createXmlHttpRequest();
    if (versionRequest) {
        versionRequest.onreadystatechange = requestStateChanged;
        versionRequest.open("GET", url, true);
        versionRequest.send(null);
    }
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest)
        return new XMLHttpRequest();
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}

function requestStateChanged() {
    if (versionRequest && versionRequest.readyState == 4) {
        if (versionRequest.status == 200) {
            var latestVersion = versionRequest.responseText;
            if (isOutOfDate(latestVersion))
                versionNotLatest(latestVersion);
            else
                versionLatest();
        } else
            versionCheckError();
    }
}

function checkForLatestVersion(url) {
    setLatestVersionDivHTML("Checking for newer version...");
    try {
        sendRequestForLatestVersion(url);
    } catch (e) {
        setLatestVersionDivHTML("An error occurred while checking for a newer version: " + e.message);
    }
}

function versionNotLatest(latestVersion) {
    setLatestVersionDivHTML('<font color="red">A newer version of JsUnit, version ' + latestVersion + ', is available.</font>');
}

function versionLatest() {
    setLatestVersionDivHTML("You are running the latest version of JsUnit.");
}

function setLatestVersionDivHTML(string) {
    document.getElementById("versionCheckDiv").innerHTML = string;
}

function versionCheckError() {
    setLatestVersionDivHTML("An error occurred while checking for a newer version.");
}
