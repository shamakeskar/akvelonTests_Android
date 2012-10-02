#!/usr/bin/env node

/**
 * HOW TO USE VCR
 *
 * Do not use vcr with proxy.  VCR does the proxy for you.
 * 
 * Host name default = touch.www.linkedin.com
 * Host name port = 8080
 *
 * Proxy mode:
 *   sudo ./scripts/vcr.js proxy [host name] [host port]
 * Record mode (record all AJAX calls to disk in fixtures folder):
 *   sudo ./scripts/vcr.js record [host name] [host port]
 * Record mode w/ key:
 *   sudo ./scripts/vcr.js record,layouttests [host name] [host port]
 * Replay mode:
 *   sudo ./scripts/vcr.js replay
 *   If you recorded with key, make sure you readjust TLI.config.serviceUrlBase to point there.
 *
 */

//GLOBALS
var http = require('http');
var https = require('https');
var fs = require('fs');
var sys = require('sys');
var path = require('path');

//header simulation for reading static file data for replay mode
var headers = {
    server: 'nginx',
    'content-type': 'application/json',
    'transfer-encoding': 'chunked',
    connection: 'close',
    'x-powered-by': 'Express',
    'x-linkedin': 'true',
    pragma: 'no-cache',
    'cache-control': 'no-cache, no-store, max-age=0, must-revalidate',
    expires: '0',
    //do not use gzip to create human reabable data
    //  'content-encoding': 'gzip'
};

//CONSTANTS
var FIXTURES_DIR = "fixtures/";
var TEST_NUMBER_PARAM_NAME = "testNumber";
var EMPTY_STRING = "";
var OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE = "ok";

// HTTP Server.
http.createServer(function (request, response) {
    
    var requestUrl = request.url;
    var isNewTestStarted = false;

    if (requestUrl.match(/^\/startFixturesForTest/)) {
        isNewTestStarted = setCurrentTestNumber(requestUrl, response);
    }

    if (isNewTestStarted) {
        prepareForTest();
    }

    if (requestUrl.match(/^\/stopFixturesForCurrentTest/) && recordkey != EMPTY_STRING) {
        console.log("Following test finished: " + recordkey);
        recordkey = EMPTY_STRING;
        response.write(OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE);
        response.end();
    }
    
    //TODO stop server when all tests complete

    //console.log(request);
    //If url is data request, we want to intercept and proxy it
    if (request.url.match(/^\/data/) && recordkey != EMPTY_STRING) {

        //Remove data identifier from url
        var url = request.url.replace(/^\/data/, "");
        //console.log("[HTTP/REQUEST/Data] - " + url);
        var language = request.headers['accept-language'];
        //console.log('Language - ' + language);
        //If recording/replaying open file
        var fd = null,
            folderkey = null,
            filename = null;
            
        if (record || replay) {
            //Filter out unnecessary parameters to record
            filename = url.replace(/nc\=[0-9]+/, "").replace(/since\=[0-9]+/, "").replace(/isince\=[0-9]+/, "").replace(/\//g, "\.").replace(/:/, "\.").replace(/authToken=\=[a-zA-Z0-9]+/, "");
            if (filename.indexOf(".") === 0) {
                filename = filename.substring(1);
            }

            folderkey = "fixtures/" + recordkey + "/" + language;
            if (!path.existsSync(folderkey)) {
                if (!path.existsSync(path.dirname(folderkey))) {
                    fs.mkdirSync(path.dirname(folderkey, "0777"));
                }
                fs.mkdirSync(folderkey, "0777");
            }

            //Replace depecated in windows file names symbol '?' to symbol '#' and symbol '*' to symbol '%'.
            filename = filename.replace(/\?/g, "#").replace(/\*/g, "%");
            filename = folderkey + "/" + filename;

            var id;

            if (names.indexOf(filename.toString()) == - 1) {
                id = names.length;
                names[id] = filename;
                counters[id] = 1;
            } else {
                id = names.indexOf(filename.toString());
                counters[id]++;
            }
            filename += "_";
            if (counters[id] < 10) filename += "0";
            filename += counters[id];


            if (record) {
                fd = fs.openSync(filename, "w");
            } else if (replay) {
                try {
                    fd = fs.readFileSync(filename, "binary");
                    console.log("Replay(" + language + "): " + filename);
                } catch (e) {
                    console.log("[ERROR/Data] - " + filename);
                    return;
                }
            }
        }

        //Replay to simulate recorded ajax calls from disk
        if (replay) {
            // headers['content-encoding'] = "gzip";
            response.writeHead("200", headers);
            response.write(fd, "binary");
            response.end();
        } else {
            //Create proxy server pointing to touch or whatever server host was passed in
            var proxy = http.createClient(port, host);
            //Create proxy request, maintain same method as request and same headers, but pass url
            //remove gzip encoding
            delete request.headers['accept-encoding'];
            var proxy_request = proxy.request(request.method, url, request.headers);

            //Attach listener to response, when the AJAX response comes back
            proxy_request.addListener('response', function (proxy_response) {
                //Listener for data received, we want to write that data out
                proxy_response.addListener('data', function (chunk) {
                    response.write(chunk, 'binary');

                    //If in record mode, we want to write out the data to disk
                    if (record) {
                        //console.log("[HTTP/RESPONSE/Write] - ", chunk.length);
                        fs.writeSync(fd, chunk, 0, chunk.length, null);
                    }

                });
                //Listener for when response is finished, we want to close the response out
                proxy_response.addListener('end', function () {
                    response.end();

                    //If in record mode, we want to close out writing data to disk
                    if (record) {
                        //console.log("[HTTP/RESPONSE/Write] - ", filename);
                        console.log("Record(" + language + "): " + filename);
                        fs.closeSync(fd);
                    }
                });

                //Write out the headers and statusCode for proper response
                response.writeHead(proxy_response.statusCode, proxy_response.headers);
            });

            //Listeners to dispatch and close in proxy
            request.addListener('data', function (chunk) {
                proxy_request.write(chunk, 'binary');
            });
            request.addListener('end', function () {
                proxy_request.end();
            });
        }

        //Non-AJAX call, photos, index.html, etc.  
    } else {
        var filePath = request.url;
        //console.log("[REQUEST/filePath] - " + filePath);

        if (filePath == "/") filePath = "/index.html";
        var file = fs.readFile("build/" + filePath, "utf8", function (err, data) {
            if (err) {
                response.write("Not Found: " + err);
            } else {
                response.write(data);
            }
            response.end();
        });
    }
}).listen(8080);

function setCurrentTestNumber(requestUrl, response)
{
    console.log(EMPTY_STRING);
    var testNumber = getCurrentTestNumberFromRequestUrl(requestUrl);
    if (null == response) {
        console.log("[ERROR/currentTest] - can not resporse to test (response object is null)");
        return false;
    }
    if (!isFixturesExistForTest(testNumber)) {
        recordkey = EMPTY_STRING;
        var errorMessage = "[ERROR/currentTest] - There is no fixtures for following test: " + testNumber;
        console.log(errorMessage);
        response.write(errorMessage);
        response.end();
        return false;
    }
    recordkey = testNumber; 
    response.write(OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE);
    response.end();
    console.log("Start fixtures for test: " + recordkey);
    return true;
}

function getCurrentTestNumberFromRequestUrl(requestUrl) {
    var requestParamName_Values = getRequestParamName_ValuePairs(requestUrl);
    if (null == requestParamName_Values) {
        console.log("[ERROR/currentTest] - Wrong 'set current test' request format: " + requestUrl);
        return null;
    }

    var testNumber = requestParamName_Values[TEST_NUMBER_PARAM_NAME];
    return testNumber;
}

function getRequestParamName_ValuePairs(requestUrl) {
    var mainRequestUrl_ParamsExpectedLength = 2;
    var paramsIndexInMainRequestUrl_Params = 1; 

    if (null == requestUrl) {
        return null;
    }
    var mainRequestUrl_Params = requestUrl.split("?");
    if (!isArrayLengthGreaterOrEqualsTo(mainRequestUrl_Params, mainRequestUrl_ParamsExpectedLength)) {
        return null;
    }
    var requestUrlParams = mainRequestUrl_Params[paramsIndexInMainRequestUrl_Params];
    return separateParamNameValuePairs(requestUrlParams);
}

function separateParamNameValuePairs(joinedParamName_ValuePairs) {
    var urlName_ValuePairsSeparator = "&";

    if (null == joinedParamName_ValuePairs) {
        return null;
    }
    var arrayOfJoinedParamName_ValuePairs = joinedParamName_ValuePairs.split(urlName_ValuePairsSeparator);
    return splitParamNamesFromValues(arrayOfJoinedParamName_ValuePairs);
}

function splitParamNamesFromValues(arrayOfJoinedParamName_ValuePairs) {
    var urlName_ValueSeparator = "=";
    var separatedName_ValueArrayExpectedLength = 2;
    var nameIndexInSeparatedName_ValueArray = 0;
    var valueIndexInSeparatedName_ValueArray = 1;

    var separatedName_ValuePairs = {};
    if (null == arrayOfJoinedParamName_ValuePairs) {
        return null;
    }

    for (var i = 0; i < arrayOfJoinedParamName_ValuePairs.length; i++) {
        var joinedName_Value = arrayOfJoinedParamName_ValuePairs[i];
        var separatedName_Value = joinedName_Value.split(urlName_ValueSeparator);
        if (!isArrayLengthGreaterOrEqualsTo(separatedName_Value,separatedName_ValueArrayExpectedLength)) {
            continue;
        }
        var name = separatedName_Value[nameIndexInSeparatedName_ValueArray];
        var value = separatedName_Value[valueIndexInSeparatedName_ValueArray];
        separatedName_ValuePairs[name] = value;
    }

    return separatedName_ValuePairs;
}

function isFixturesExistForTest(testNumber) {
    if (null == testNumber || EMPTY_STRING === testNumber) {
        return false;
    }
    var pathToTestFixtures = FIXTURES_DIR + testNumber;
    return path.existsSync(pathToTestFixtures);
}

function isArrayLengthGreaterOrEqualsTo(array, expectedLength) {
    return null != array && null != expectedLength && array.length >= expectedLength;
}

function prepareForTest() {
    names = new Array();
    counters = new Array();
}

// MAIN METHOD.
// Pulling arguments.
var mode = process.argv[2] || "proxy",
    host = process.argv[3] || "touch.www.linkedin.com",
    port = process.argv[4] || 80,
    names = new Array(),
    counters = new Array(),
    mode_arr = mode.split(","),

    record = mode_arr[0] === "record",
    replay = mode_arr[0] === "replay",
    recordkey = mode_arr[1];
if (recordkey == undefined) {
    recordkey = "";
}

var proxyModeMessage = "Proxying mode: " + mode_arr[0];
if (recordkey != EMPTY_STRING) {
    proxyModeMessage += ", directory: " + (recordkey ? recordkey : "");
}
console.log(proxyModeMessage);
console.log("Proxying locahost:8080 => " + host + ":" + port);
console.log("Proxying locahost:443 => " + host + ":" + 443);
