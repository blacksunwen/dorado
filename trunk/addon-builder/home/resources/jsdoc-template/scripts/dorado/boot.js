
var $import, $load, $packagesConfig = window.$packagesConfig || {};
$packagesConfig.defaultContentType = $packagesConfig.defaultContentType || "text/javascript";
(function () {
    var failsafe = {};
    var Browser = {};
    var ua = navigator.userAgent.toLowerCase(), s;
    (s = ua.match(/msie ([\d.]+)/)) ? Browser.msie = s[1] : (s = ua.match(/firefox\/([\d.]+)/)) ? Browser.mozilla = s[1] : (s = ua.match(/chrome\/([\d.]+)/)) ? Browser.chrome = s[1] : (s = ua.match(/opera.([\d.]+)/)) ? Browser.opera = s[1] : (s = ua.match(/version\/([\d.]+).*safari/)) ? Browser.safari = s[1] : 0;
    var head;
    function findHead() {
        head = document.getElementsByTagName("head")[0] || document.documentElement;
    }
    var loadedPackages = {};
    function getNeededs(pkgs) {
        function findNeededs(pkgs, context) {
            for (var i = 0; i < pkgs.length; i++) {
                var pkg = pkgs[i];
                var def = packages[pkg];
                if (def && def.depends) {
                    var depends = def.depends;
                    findNeededs(depends instanceof Array ? depends : depends.split(","), context);
                }
                if (!loadedPackages[pkg] && !context.added[pkg]) {
                    context.added[pkg] = true;
                    context.needed.push(pkg);
                }
            }
        }
        var packages = $packagesConfig.packages || failsafe, context = {added:{}, needed:[]};
        findNeededs(pkgs, context);
        return context.needed;
    }
    function getRequests(pkgs) {
        function mergePkgs(request) {
            var pattern = request.pattern;
            var fileNames = request["package"].join(",");
            request.url = pattern.url.replace(/\$\{fileName\}/g, encodeURI(fileNames).replace(/\//g, "^"));
        }
        var patterns = $packagesConfig.patterns || failsafe;
        var packages = $packagesConfig.packages || failsafe;
        var defaultPattern = patterns["default"] || failsafe;
        var tempRequests = [], toLast;
        for (var i = 0; i < pkgs.length; i++) {
            var pkg = pkgs[i];
            var def = packages[pkg], pattern, fileNames, contentType, charset;
            if (def) {
                pattern = patterns[def.pattern];
                fileNames = def.fileName;
                contentType = def.contentType;
                charset = def.charset;
            } else {
                alert("Unknown package [" + pkg + "].");
            }
            pattern = pattern || defaultPattern;
            if (!fileNames) {
                fileNames = pkg;
            }
            if (!contentType) {
                contentType = pattern.contentType || $packagesConfig.defaultContentType;
            }
            if (!charset) {
                charset = pattern.charset || $packagesConfig.defaultCharset;
            }
            if (typeof fileNames == "string") {
                fileNames = fileNames.split(",");
            }
            for (var j = 0; j < fileNames.length; j++) {
                var fileName = fileNames[j];
                if (fileName.indexOf("(none)") >= 0) {
                    continue;
                }
                var request = {"package":pkg, url:(pattern.url ? pattern.url.replace(/\$\{fileName\}/g, fileName) : fileName), contentType:contentType, charset:charset, pattern:pattern};
                if (isStyleSheet(contentType)) {
                    if (!toLast) {
                        toLast = [];
                    }
                    toLast.push(request);
                } else {
                    tempRequests.push(request);
                }
            }
        }
        if (toLast) {
            tempRequests.push.apply(tempRequests, toLast);
        }
        var requests = [], mergeRequest;
        for (var i = 0; i < tempRequests.length; i++) {
            var request = tempRequests[i];
            if (mergeRequest && mergeRequest.pattern != request.pattern) {
                mergePkgs(mergeRequest);
                mergeRequest = null;
            }
            if (request.pattern.mergeRequests) {
                var pkg = request["package"];
                if (!mergeRequest) {
                    mergeRequest = request;
                    request["package"] = [];
                    requests.push(request);
                }
                mergeRequest["package"].push(pkg);
            } else {
                requests.push(request);
            }
        }
        if (mergeRequest) {
            mergePkgs(mergeRequest);
        }
        for (var i = 0; i < requests.length; i++) {
            var request = requests[i];
            if (request.url.charAt(0) == ">") {
                var s1 = $packagesConfig.contextPath || "", s2 = request.url.substring(1);
                if (s1) {
                    if (s1.charAt(s1.length - 1) == "/") {
                        if (s2.charAt(0) == "/") {
                            s2 = s2.substring(1);
                        }
                    } else {
                        if (s2.charAt(0) != "/") {
                            s2 = "/" + s2;
                        }
                    }
                }
                request.url = s1 + s2;
            }
        }
        return requests;
    }
    var $readyState;
    if ((Browser.mozilla || Browser.opera) && document.readyState != "loading") {
        function onLoad() {
            $readyState = "complete";
            document.removeEventListener("DOMContentLoaded", onLoad, false);
        }
        $readyState = "loading";
        document.addEventListener("DOMContentLoaded", onLoad, false);
    }
    function isStyleSheet(contentType) {
        return contentType == "text/css";
    }
    function markRequestLoaded(request) {
        var pkg = request["package"];
        if (pkg instanceof Array) {
            for (var j = 0; j < pkg.length; j++) {
                loadedPackages[pkg[j]] = true;
            }
        } else {
            loadedPackages[pkg] = true;
        }
    }
    function loadResourceAsync(request, options, callback) {
        function onLoaded(element) {
            element.onreadystatechange = element.onload = null;
            head.removeChild(element);
        }
        var element;
        if (isStyleSheet(request.contentType)) {
            element = document.createElement("link");
            element.rel = "stylesheet";
            element.type = request.contentType;
            element.href = request.url;
            if (callback) {
                callback(request);
            }
        } else {
            element = document.createElement("script");
            if (Browser.msie) {
                element.onreadystatechange = function () {
                    if (/loaded|complete/.test(this.readyState)) {
                        if (callback) {
                            callback(request);
                        }
                        onLoaded(this);
                    }
                };
            } else {
                element.onload = function () {
                    if (callback) {
                        callback(request);
                    }
                    onLoaded(this);
                };
            }
            element.language = "JavaScript";
            element.type = request.contentType;
            element.charset = request.charset;
            element.src = request.url;
            element._request = request;
        }
        head.insertBefore(element, head.firstChild);
        markRequestLoaded(request);
    }
    function loadResourcesAsync(requests, options, callback) {
        function scriptCallback(request) {
            if (++loaded < requests.length) {
                loadResourceAsync(requests[loaded], options, scriptCallback);
            } else {
                callback.call(scope);
            }
        }
        var scope = options ? options.scope : null, loaded = 0;
        findHead();
        loadResourceAsync(requests[loaded], options, scriptCallback);
    }
    function loadResource(request, options) {
        var typeAndCharset = "type=\"" + request.contentType + "\" " + (request.charset ? "charset=\"" + request.charset + "\" " : "");
        if (isStyleSheet(request.contentType)) {
            if (Browser.mozilla) {
                findHead();
                loadResourceAsync(request, options);
            } else {
                document.writeln("<link rel=\"stylesheet\" " + typeAndCharset + "href=\"" + request.url + "\" />");
            }
        } else {
            document.writeln("<script language=\"JavaScript\" " + typeAndCharset + "src=\"" + request.url + "\"></script>");
        }
        markRequestLoaded(request);
    }
    function loadResources(requests, options) {
        for (var i = 0; i < requests.length; i++) {
            var request = requests[i];
            loadResource(request, options);
        }
    }
    function doLoadResources(requests, options, callback) {
        try {
            if (callback) {
                var scope = options ? options.scope : null;
                if (requests.length) {
                    loadResourcesAsync(requests, options, callback);
                } else {
                    callback.call(options ? options.scope : null);
                }
            } else {
                if (requests.length) {
                    if (!(/loaded|complete/.test($readyState || document.readyState))) {
                        loadResources(requests, options);
                    } else {
                        throw new Error("Can not load script synchronous after the document is loaded.");
                    }
                }
            }
        }
        catch (e) {
            alert(e.description || e);
        }
    }
    $import = function (pkgs, options) {
        function getOption(p) {
            return ((!options || typeof options[p] == "undefined") ? options : $packagesConfig)[p];
        }
        var callback;
        if (options instanceof Function) {
            callback = options;
            options = null;
        } else {
            if (options instanceof Object) {
                callback = options.callback;
            }
        }
        if (!pkgs) {
            if (callback) {
                callback.call(options ? options.scope : null);
            }
            return;
        }
        if (pkgs instanceof Array) {
            var v = [];
            for (var i = 0; i < pkgs.length; i++) {
                v.concat(pkgs[i].split(","));
            }
            pkgs = v;
        } else {
            pkgs = pkgs.split(",");
        }
        pkgs = getNeededs(pkgs);
        doLoadResources(getRequests(pkgs), options, callback);
    };
    $load = function (urls, options) {
        if (urls instanceof Array) {
            var v = [];
            for (var i = 0; i < urls.length; i++) {
                v.concat(urls[i].split(","));
            }
            urls = v;
        } else {
            urls = urls.split(",");
        }
        var type, callback;
        if (typeof options == "string") {
            type = options;
            options = null;
        } else {
            if (options instanceof Function) {
                callback = options;
                options = null;
            } else {
                if (options instanceof Object) {
                    callback = options.callback;
                }
            }
        }
        var requests = [], options = options || {};
        for (var i = 0; i < urls.length; i++) {
            var url = urls[i], contentType;
            if (!url) {
                continue;
            }
            if (type == "css" || url.toLowerCase().match("css$") == "css") {
                contentType = "text/css";
            }
            requests.push({url:url, charset:options.charset || $packagesConfig.defaultCharset, contentType:contentType || options.contentType || $packagesConfig.defaultContentType});
        }
        doLoadResources(requests, options, callback);
    };
})();

