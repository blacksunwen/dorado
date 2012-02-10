
(function () {
    try {
        if (HTMLElement && !HTMLElement.prototype.innerText) {
            HTMLElement.prototype.__defineGetter__("innerText", function () {
                var text = this.textContent;
                if (text) {
                    text = text.replace(/<BR>/g, "\n");
                }
                return text;
            });
            HTMLElement.prototype.__defineSetter__("innerText", function (text) {
                if (text && text.constructor == String) {
                    var sections = text.split("\n");
                    if (sections.length > 1) {
                        this.innerHTML = "";
                        for (var i = 0; i < sections.length; i++) {
                            if (i > 0) {
                                this.appendChild(document.createElement("BR"));
                            }
                            this.appendChild(document.createTextNode(sections[i]));
                        }
                        return;
                    }
                }
                this.textContent = text;
            });
        }
    }
    catch (ex) {
    }
    if (!Array.prototype.push) {
        Array.prototype.push = function (element) {
            this[this.length] = element;
        };
    }
    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function (element) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == element) {
                    return i;
                }
            }
            return -1;
        };
    }
    if (!Array.prototype.remove) {
        Array.prototype.remove = function (element) {
            var i = this.indexOf(element);
            if (i >= 0) {
                this.splice(i, 1);
            }
            return i;
        };
    }
    if (!Array.prototype.removeAt) {
        Array.prototype.removeAt = function (i) {
            this.splice(i, 1);
        };
    }
    if (!Array.prototype.insert) {
        Array.prototype.insert = function (element, i) {
            this.splice(i || 0, 0, element);
        };
    }
    if (!Array.prototype.peek) {
        Array.prototype.peek = function () {
            return this[this.length - 1];
        };
    }
    if (!Array.prototype.each) {
        Array.prototype.each = function (fn) {
            for (var i = 0; i < this.length; i++) {
                if (fn.call(this, this[i], i) === false) {
                    break;
                }
            }
        };
    }
})();
(function ($) {
    var superReady = $.prototype.ready;
    $.prototype.ready = function (fn) {
        if (jQuery.browser.chrome) {
            var self = this;
            function waitForReady() {
                if (document.readyState !== "complete") {
                    setTimeout(arguments.callee, 20);
                } else {
                    superReady.call(self, fn);
                }
            }
            waitForReady();
        } else {
            superReady.call(this, fn);
        }
    };
    var flyableElem = $();
    flyableElem.length = 1;
    var flyableArray = $();
    $fly = function (elems) {
        if (elems instanceof Array) {
            Array.prototype.splice.call(flyableArray, 0, flyableArray.length);
            Array.prototype.push.apply(flyableArray, elems);
            return flyableArray;
        } else {
            flyableElem[0] = elems;
            return flyableElem;
        }
    };
})(jQuery);
var dorado = {id:"_" + parseInt(Math.random() * Math.pow(10, 8)), _ID_SEED:0, _TIMESTAMP_SEED:0, _GET_ID:function (obj) {
    return obj._id;
}, _GET_NAME:function (obj) {
    return obj._name;
}, _NULL_FUNCTION:function () {
}, _UNSUPPORTED_FUNCTION:function () {
    return function () {
        throw new dorado.ResourceException("dorado.core.OperationNotSupported", dorado.getFunctionName(arguments.callee));
    };
}, Browser:$.browser, defaultToString:function (obj) {
    var s = obj.constructor.className || "[Object]";
    if (obj.id) {
        s += (" id=" + obj.id);
    }
    if (obj.name) {
        s += (" name=" + obj.name);
    }
}, getFunctionName:function (fn) {
    var defintion = fn.toString().split("\n")[0], name;
    if (fn.methodName) {
        var className;
        if (fn.declaringClass) {
            className = fn.declaringClass.className;
        }
        name = (className ? (className + ".") : "function ") + fn.methodName;
    } else {
        var regexpResult = defintion.match(/function (\w*)/);
        name = "function " + (regexpResult && regexpResult[1] || "anonymous");
    }
    var regexpResult = defintion.match(/\((.*)\)/);
    return name + (regexpResult && regexpResult[0]);
}};
if (dorado.Browser.webkit) {
    var ua = navigator.userAgent.toLowerCase();
    dorado.Browser.chrome = /chrome/.test(ua);
    if (dorado.Browser.chrome) {
        var match = /(chrome)[ \/]([\w.]+)/.exec(ua);
        dorado.Browser.version = match[2];
    }
}
dorado.Core = {VERSION:"%version%", newId:function () {
    return "_uid_" + (++dorado._ID_SEED);
}, getTimestamp:function () {
    return ++dorado._TIMESTAMP_SEED;
}, scopify:function (scope, fn) {
    if (fn instanceof Function) {
        return function () {
            return fn.apply(scope, arguments);
        };
    } else {
        return function () {
            return eval("(function(){return(" + fn + ")}).call(scope)");
        };
    }
}, setTimeout:function (scope, fn, timeMillis) {
    return setTimeout(dorado.Core.scopify(scope, fn), timeMillis);
}, setInterval:function (scope, fn, timeMillis) {
    return setInterval(dorado.Core.scopify(scope, fn), timeMillis);
}, _clone:function (obj) {
    if (!(obj instanceof Object)) {
        return obj;
    }
    var constr = obj.constructor;
    var cloned = new constr();
    for (var attr in obj) {
        var v = obj[attr];
        if (v instanceof Object && v.clone instanceof Function) {
            v = v.clone();
        }
        cloned[attr] = v;
    }
    return cloned;
}, clone:function (obj) {
    if (obj == null) {
        return obj;
    }
    if (obj.clone instanceof Function) {
        return obj.clone();
    }
    return this._clone(obj);
}};
(function () {
    window.$create = (dorado.Browser.msie) ? document.createElement : function (arg) {
        return document.createElement(arg);
    };
    window.$scopify = dorado.Core.scopify;
    window.$setTimeout = dorado.Core.setTimeout;
    window.$setInterval = dorado.Core.setInterval;
})();
(function () {
    var CLASS_REPOSITORY = {};
    var UNNAMED_CLASS = "#UnnamedClass";
    function newClassName(prefix) {
        var i = 1;
        while (CLASS_REPOSITORY[prefix + i]) {
            i++;
        }
        return prefix + i;
    }
    function adapterFunction(fn) {
        var adapter = function () {
            return fn.apply(this, arguments);
        };
        adapter._doradoAdapter = true;
        return adapter;
    }
    function cloneDefintions(defs) {
        var newDefs = {};
        for (var p in defs) {
            if (defs.hasOwnProperty(p)) {
                newDefs[p] = dorado.Object.apply({}, defs[p]);
            }
        }
        return newDefs;
    }
    function overrideDefintions(subClass, defProp, defs, overwrite) {
        if (!defs) {
            return;
        }
        var sdefs = subClass.prototype[defProp];
        if (!sdefs) {
            subClass.prototype[defProp] = cloneDefintions(defs);
        } else {
            for (var p in defs) {
                if (defs.hasOwnProperty(p)) {
                    var odef = defs[p];
                    if (odef === undefined) {
                        return;
                    }
                    var cdef = sdefs[p];
                    if (cdef === undefined) {
                        sdefs[p] = cdef = {};
                    }
                    for (var m in odef) {
                        if (odef.hasOwnProperty(m) && (overwrite || cdef[m] === undefined)) {
                            var odefv = odef[m];
                            if (odefv instanceof Function) {
                                if (odefv.declaringClass) {
                                    odefv = adapterFunction(odefv);
                                }
                                odefv.declaringClass = subClass;
                                odefv.methodName = m;
                                odefv.definitionType = defProp;
                                odefv.definitionName = p;
                            }
                            cdef[m] = odefv;
                        }
                    }
                }
            }
        }
    }
    function override(subClass, overrides, overwrite) {
        if (!overrides) {
            return;
        }
        if (overwrite === undefined) {
            overwrite = true;
        }
        var subp = subClass.prototype;
        for (var p in overrides) {
            var override = overrides[p];
            if (p == "ATTRIBUTES" || p == "EVENTS") {
                overrideDefintions(subClass, p, override, overwrite);
                continue;
            }
            if (subp[p] === undefined || overwrite) {
                if (override instanceof Function) {
                    if (override.declaringClass) {
                        override = adapterFunction(override);
                    }
                    override.declaringClass = subClass;
                    override.methodName = p;
                }
                subp[p] = override;
            }
        }
    }
    dorado.Object = {createNamespace:function (name) {
        var names = name.split(".");
        var parent = window;
        for (var i = 0; i < names.length; i++) {
            var n = names[i];
            var p = parent[n];
            if (p === undefined) {
                parent[n] = p = {};
            }
            parent = p;
        }
        return parent;
    }, createClass:function (p) {
        var constr = p.constructor;
        if (constr === Object) {
            constr = new Function();
        }
        constr.className = p.$className || newClassName(UNNAMED_CLASS);
        delete p.$className;
        for (var m in p) {
            if (p.hasOwnProperty(m)) {
                var v = p[m];
                if (v instanceof Function) {
                    if (v.declaringClass) {
                        p[m] = v = adapterFunction(v);
                    }
                    v.declaringClass = constr;
                    v.methodName = m;
                }
            }
        }
        constr.prototype = p;
        CLASS_REPOSITORY[constr.className] = constr;
        return constr;
    }, override:override, extend:(function () {
        var oc = Object.prototype.constructor;
        return function (superClass, overrides) {
            var sc, scs;
            if (superClass instanceof Array) {
                scs = superClass;
                sc = superClass[0];
            } else {
                sc = superClass;
            }
            var subClass = (overrides && overrides.constructor != oc) ? overrides.constructor : function () {
                sc.apply(this, arguments);
            };
            var fn = new Function();
            var sp = fn.prototype = sc.prototype;
            if (!sc.className) {
                sp.constructor = sc;
                sc.className = newClassName(UNNAMED_CLASS);
                sc.declaringClass = sp;
                sc.methodName = "constructor";
            }
            var subp = subClass.prototype = new fn();
            subp.constructor = subClass;
            subClass.className = overrides.$className || newClassName((sc.$className || UNNAMED_CLASS) + "$");
            subClass.superClass = sc;
            subClass.declaringClass = subClass;
            subClass.methodName = "constructor";
            delete overrides.$className;
            delete overrides.constructor;
            var attrs = subp["ATTRIBUTES"];
            if (attrs) {
                subp["ATTRIBUTES"] = cloneDefintions(attrs);
            }
            var events = subp["EVENTS"];
            if (events) {
                subp["EVENTS"] = cloneDefintions(events);
            }
            var ps = [sc];
            if (scs) {
                for (var i = 1, p; i < scs.length; i++) {
                    p = scs[i].prototype;
                    override(subClass, p, false);
                    ps.push(scs[i]);
                }
            }
            subClass.superClasses = ps;
            override(subClass, overrides, true);
            CLASS_REPOSITORY[subClass.className] = subClass;
            return subClass;
        };
    })(), eachProperty:function (object, fn) {
        if (object && fn) {
            for (var p in object) {
                fn.call(object, p, object[p]);
            }
        }
    }, apply:function (target, source, options) {
        if (source) {
            for (var p in source) {
                if (options instanceof Function && options.call(target, p, source[p]) === false) {
                    continue;
                }
                if (options === false && target[p] !== undefined) {
                    continue;
                }
                target[p] = source[p];
            }
        }
        return target;
    }, isInstanceOf:function (object, type) {
        function hasSuperClass(superClasses) {
            if (!superClasses) {
                return false;
            }
            if (superClasses.indexOf(type) >= 0) {
                return true;
            }
            for (var i = 0; i < superClasses.length; i++) {
                if (hasSuperClass(superClasses[i].superClasses)) {
                    return true;
                }
            }
            return false;
        }
        if (!object) {
            return false;
        }
        var b = false;
        if (type.className) {
            eval("b = object instanceof " + type.className);
        }
        if (!b) {
            var t = object.constructor;
            if (t) {
                b = hasSuperClass(t.superClasses);
            }
        }
        return b;
    }, clone:function (object, options) {
        if (object instanceof Object) {
            var objClone, options = options || {};
            if (options.onCreate) {
                objClone = new options.onCreate(object);
            } else {
                objClone = new object.constructor();
            }
            for (var p in object) {
                if (!options.onCopyProperty || options.onCopyProperty(p, object, objClone)) {
                    objClone[p] = object[p];
                }
            }
            objClone.toString = object.toString;
            objClone.valueOf = object.valueOf;
            return objClone;
        } else {
            return object;
        }
    }};
    window.$namespace = dorado.Object.createNamespace;
    window.$class = dorado.Object.createClass;
    window.$extend = dorado.Object.extend;
    window.$getSuperClass = function () {
        var fn = arguments.callee.caller, superClass;
        if (fn.declaringClass) {
            superClass = fn.declaringClass.superClass;
        }
        return superClass || {};
    };
    window.$getSuperClasses = function () {
        var fn = arguments.callee.caller, superClass;
        if (dorado.Browser.opera && dorado.Browser.version < "10") {
            fn = fn.caller;
        }
        if (fn.caller && fn.caller._doradoAdapter) {
            fn = fn.caller;
        }
        if (fn.declaringClass) {
            superClasses = fn.declaringClass.superClasses;
        }
        return superClasses || [];
    };
    window.$invokeSuper = function (args) {
        var fn = arguments.callee.caller;
        if (dorado.Browser.opera && dorado.Browser.version < "10") {
            fn = fn.caller;
        }
        if (fn.caller && fn.caller._doradoAdapter) {
            fn = fn.caller;
        }
        if (fn.declaringClass) {
            var superClasses = fn.declaringClass.superClasses;
            if (!superClasses) {
                return;
            }
            for (var i = 0; i < superClasses.length; i++) {
                var superClass = superClasses[i].prototype;
                var superFn;
                if (fn.definitionType) {
                    superFn = superClass[fn.definitionType][fn.definitionName][fn.methodName];
                } else {
                    superFn = superClass[fn.methodName];
                }
                if (superFn) {
                    return superFn.apply(this, args || []);
                }
            }
        }
    };
    window.$invokeSuper.methodName = "$invokeSuper";
})();
(function () {
    var doradoServierURI = ">dorado/view-service";
    dorado.Setting = {"common.defaultDateFormat":"Y-m-d", "common.defaultTimeFormat":"Y-m-d\\TH:i:s\\Z", "ajax.defaultOptions":null, "ajax.autoBatchSupportedUris":[doradoServierURI], "ajax.dataTypeRepositoryOptions":{url:doradoServierURI, method:"POST"}, "ajax.dataProviderOptions":{url:doradoServierURI, method:"POST"}, "ajax.dataResolverOptions":{url:doradoServierURI, method:"POST"}, "ajax.remoteServiceOptions":{url:doradoServierURI, method:"POST"}, "dom.useCssShadow":true, "widget.skin":"~current", "widget.panel.useCssCurveBorder":true};
    if (window.$setting instanceof Object) {
        dorado.Object.apply(dorado.Setting, $setting);
    }
    var contextPath = dorado.Setting["common.contextPath"];
    if (contextPath && contextPath.charAt(contextPath.length - 1) != "/") {
        dorado.Setting["common.contextPath"] = contextPath + "/";
    }
    window.$setting = dorado.Setting;
})();
dorado.Exception = $class({$className:"dorado.Exception", constructor:function (message) {
    this.message = message;
    this._buildStackTrace();
    dorado.Exception.EXCEPTION_STACK.push(this);
    if (dorado.Browser.msie || dorado.Browser.mozilla) {
        window.onerror = function (message, url, line) {
            var result = false;
            if (dorado.Exception.EXCEPTION_STACK.length > 0) {
                var e;
                while (e = dorado.Exception.EXCEPTION_STACK.peek()) {
                    dorado.Exception.processException(e);
                }
                result = true;
            }
            window.onerror = null;
            return result;
        };
    }
    $setTimeout(this, function () {
        if (dorado.Exception.EXCEPTION_STACK.indexOf(this) >= 0) {
            dorado.Exception.processException(this);
        }
    }, 0);
}, _buildStackTrace:function () {
    var stack = [];
    var funcCaller = dorado.Exception.caller, callers = [];
    while (funcCaller && callers.indexOf(funcCaller) < 0) {
        callers.push(funcCaller);
        stack.push(dorado.getFunctionName(funcCaller));
        funcCaller = funcCaller.caller;
    }
    this.stack = stack;
    if (dorado.Browser.mozilla || dorado.Browser.chrome) {
        var stack = new Error().stack;
        if (stack) {
            stack = stack.split("\n");
            this.systemStack = stack.slice(2, stack.length - 1);
        }
    }
}, formatStack:function (stack) {
    var msg = "";
    if (stack) {
        for (var i = 0; i < stack.length; i++) {
            if (i > 0) {
                msg += "\n";
            }
            msg += " > " + stack[i];
            if (i > 10) {
                msg += "\n > ... ... ...";
                break;
            }
        }
    }
    return msg;
}, toString:function () {
    return this.message;
}});
dorado.AbortException = $extend(dorado.Exception, {$className:"dorado.AbortException"});
dorado.ResourceException = $extend(dorado.Exception, {$className:"dorado.ResourceException", constructor:function () {
    $invokeSuper.call(this, [$resource.apply(this, arguments)]);
}});
dorado.RemoteException = $extend(dorado.Exception, {$className:"dorado.RemoteException", constructor:function (message, exceptionType, remoteStack) {
    $invokeSuper.call(this, [message]);
    this.exceptionType = exceptionType;
    this.remoteStack = remoteStack;
}});
dorado.Exception.EXCEPTION_STACK = [];
dorado.Exception.getExceptionMessage = function (e) {
    if (!e || e instanceof dorado.AbortException) {
        return null;
    }
    var msg;
    if (e instanceof dorado.Exception) {
        msg = e.message;
    } else {
        if (e instanceof Error) {
            msg = e.message;
        } else {
            msg = e;
        }
    }
    return msg;
};
dorado.Exception.processException = function (e) {
    dorado.Exception.removeException(e);
    if (!e || e instanceof dorado.AbortException) {
        return;
    }
    var msg = dorado.Exception.getExceptionMessage(e);
    if (e instanceof dorado.Exception) {
        if (e.stack) {
            msg += "\n\nStack:\n" + e.formatStack(e.stack);
        }
        if (e.remoteStack) {
            msg += "\n\nRemote Stack:\n" + e.formatStack(e.remoteStack);
        }
        if (e.systemStack) {
            msg += "\n\nSystem Stack:\n" + e.formatStack(e.systemStack);
        }
    } else {
        if (e instanceof Error) {
            if (e.stack) {
                msg += "\n\nSystem Stack:\n" + e.stack;
            }
        }
    }
    if (!dorado.MessageBox || !document.body) {
        alert(msg);
    } else {
        dorado.MessageBox.alert(msg, {icon:dorado.MessageBox.ERROR_ICON});
    }
};
dorado.Exception.removeException = function (e) {
    dorado.Exception.EXCEPTION_STACK.remove(e);
};
(function () {
    dorado.AttributeException = $extend(dorado.ResourceException, {$className:"dorado.AttributeException"});
    dorado.AttributeSupport = $class({$className:"dorado.AttributeSupport", constructor:function () {
        var defs = this.ATTRIBUTES;
        for (var p in defs) {
            if (defs.hasOwnProperty(p) && defs[p].defaultValue != undefined && this["_" + p] == undefined) {
                var dv = defs[p].defaultValue;
                this["_" + p] = (dv instanceof Function && !defs[p].neverEvalDefaultValue) ? dv() : dv;
            }
        }
    }, ATTRIBUTES:{tags:{setter:function (p, tags) {
        if (typeof tags == "string") {
            tags = tags.split(",");
        }
        if (this._tags) {
            dorado.TagManager.unregister(this);
        }
        this._tags = tags;
        if (tags) {
            dorado.TagManager.register(this);
        }
    }}}, EVENTS:{onAttributeChange:{}}, getAttributeWatcher:function () {
        if (!this.attributeWatcher) {
            this.attributeWatcher = new dorado.AttributeWatcher();
        }
        return this.attributeWatcher;
    }, get:function (attr) {
        var attrs = attr.split("."), result;
        for (var i = 0; i < attrs.length; i++) {
            attr = attrs[i];
            if (i == 0) {
                var def = this.ATTRIBUTES[attr];
                if (def) {
                    if (def.writeOnly) {
                        throw new dorado.AttributeException("dorado.core.AttributeWriteOnly", attr);
                    }
                    if (def.getter) {
                        result = def.getter.call(this, attr);
                    } else {
                        if (def.path) {
                            var sections = def.path.split("."), owner = this;
                            for (var i = 0; i < sections.length; i++) {
                                var section = sections[i];
                                owner = (section.charAt(0) == "_") ? owner[section] : owner.get(section);
                                if (owner == null || i == sections.length - 1) {
                                    result = owner;
                                    break;
                                }
                            }
                        } else {
                            result = this["_" + attr];
                        }
                    }
                } else {
                    throw new dorado.AttributeException("dorado.core.UnknownAttribute", attr);
                }
            } else {
                if (!result) {
                    break;
                }
                result = (result.get instanceof Function) ? result.get(attr) : result[attr];
            }
        }
        return result;
    }, set:function (attr, value, options) {
        var skipUnknownAttribute, tryNextOnError, preventOverwriting, lockWritingTimes;
        if (attr && typeof attr == "object") {
            options = value;
        }
        if (options && typeof options == "object") {
            skipUnknownAttribute = options.skipUnknownAttribute;
            tryNextOnError = options.tryNextOnError;
            preventOverwriting = options.preventOverwriting;
            lockWritingTimes = options.lockWritingTimes;
        }
        if (attr.constructor != String) {
            var attrInfos = [];
            for (var p in attr) {
                if (attr.hasOwnProperty(p)) {
                    var v = attr[p], attrInfo = {attr:p, value:v};
                    if (p == "listener" || v instanceof Function) {
                        attrInfos.insert(attrInfo);
                    } else {
                        attrInfos.push(attrInfo);
                    }
                }
            }
            if (preventOverwriting) {
                watcher = this.getAttributeWatcher();
            }
            for (var i = 0; i < attrInfos.length; i++) {
                attrInfo = attrInfos[i];
                if (preventOverwriting && watcher.getWritingTimes(attrInfo.attr)) {
                    continue;
                }
                try {
                    this.doSet(attrInfo.attr, attrInfo.value, skipUnknownAttribute, lockWritingTimes);
                }
                catch (e) {
                    if (e instanceof dorado.AttributeException) {
                        dorado.Exception.removeException(e);
                    } else {
                        if (!tryNextOnError) {
                            throw e;
                        }
                    }
                }
            }
        } else {
            if (preventOverwriting) {
                if (this.getAttributeWatcher().getWritingTimes(attr)) {
                    return this;
                }
            }
            this.doSet(attr, value, skipUnknownAttribute, lockWritingTimes);
        }
        return this;
    }, doSet:function (attr, value, skipUnknownAttribute, lockWritingTimes) {
        if (attr.charAt(0) == "$") {
            return;
        }
        var def = this.ATTRIBUTES[attr];
        if (def) {
            if (def.readOnly) {
                throw new dorado.AttributeException("dorado.core.AttributeReadOnly", attr);
            }
            var attributeWatcher = this.getAttributeWatcher();
            if (def.writeOnce && attributeWatcher.getWritingTimes(attr) > 0) {
                throw new dorado.AttributeException("dorado.core.AttributeWriteOnce", attr);
            }
            if (!lockWritingTimes) {
                attributeWatcher.incWritingTimes(attr);
            }
            if (def.setter) {
                def.setter.call(this, attr, value);
            } else {
                if (def.path) {
                    var sections = def.path.split("."), owner = this;
                    for (var i = 0; i < sections.length - 1 && owner != null; i++) {
                        var section = sections[i];
                        owner = (section.charAt(0) == "_") ? owner[section] : owner.get(section);
                    }
                    if (owner != null) {
                        var section = sections[sections.length - 1];
                        (section.charAt(0) == "_") ? (owner[section] = value) : owner.set(section, value);
                    } else {
                        this["_" + attr] = value;
                    }
                } else {
                    this["_" + attr] = value;
                }
            }
            if (this.fireEvent) {
                this.fireEvent("onAttributeChange", this, {attribute:attr, value:value});
            }
        } else {
            if (value instanceof Object && dorado.Object.isInstanceOf(this, dorado.EventSupport) && this.EVENTS[attr]) {
                if (value instanceof Function) {
                    this.addListener(attr, value);
                } else {
                    if (value.listener) {
                        this.addListener(attr, value.listener, value.options);
                    }
                }
            } else {
                if (!skipUnknownAttribute) {
                    throw new dorado.AttributeException("dorado.core.UnknownAttribute", attr);
                }
            }
        }
    }, hasTag:function (tag) {
        if (this._tags) {
            return this._tags.indexOf(tag) >= 0;
        } else {
            return false;
        }
    }});
    dorado.AttributeWatcher = $class({$className:"dorado.AttributeWatcher", getWritingTimes:function (attr) {
        return this[attr + ".writingTimes"] || 0;
    }, incWritingTimes:function (attr) {
        this[attr + ".writingTimes"] = (this[attr + ".writingTimes"] || 0) + 1;
    }, setWritingTimes:function (attr, n) {
        this[attr + ".writingTimes"] = n;
    }});
})();
dorado.Callback = {};
window.$callback = dorado.Callback.invokeCallback = function (callback, success, arg, options) {
    function invoke(fn, args) {
        if (delay > 0) {
            setTimeout(function () {
                fn.apply(scope, args);
            }, delay);
        } else {
            fn.apply(scope, args);
        }
    }
    if (!callback) {
        return;
    }
    if (success == null) {
        success = true;
    }
    var scope, delay;
    if (options) {
        scope = options.scope;
        delay = options.delay;
    }
    if (callback instanceof Function) {
        if (!success) {
            return;
        }
        invoke(callback, [arg]);
    } else {
        scope = callback.scope || scope || window;
        delay = callback.delay || delay;
        if (callback.callback instanceof Function) {
            invoke(callback.callback, [success, arg]);
        }
        var name = (success) ? "success" : "failure";
        if (callback[name] instanceof Function) {
            invoke(callback.callback, [arg]);
        }
    }
};
dorado.Callback.simultaneousCallbacks = function (tasks, callback) {
    function getSimultaneousCallback(task) {
        var fn = function () {
            suspendedTasks.push({task:task, scope:this, args:arguments});
            if (taskReg[task.id]) {
                delete taskReg[task.id];
                taskNum--;
                if (taskNum == 0) {
                    jQuery.each(suspendedTasks, function (i, suspendedTask) {
                        suspendedTask.task.callback.apply(suspendedTask.scope, suspendedTask.args);
                    });
                    $callback(callback, true);
                }
            }
        };
        return fn;
    }
    var taskReg = {}, taskNum = tasks.length, suspendedTasks = [];
    if (taskNum > 0) {
        jQuery.each(tasks, function (i, task) {
            if (!task.id) {
                task.id = dorado.Core.newId();
            }
            var simCallback = getSimultaneousCallback(task);
            taskReg[task.id] = callback;
            task.run(simCallback);
        });
    } else {
        $callback(callback, true);
    }
};
dorado.EventSupport = $class({$className:"dorado.EventSupport", ATTRIBUTES:{listener:{setter:function (a, v) {
    if (!v) {
        return;
    }
    if (v instanceof Array) {
        var self = this;
        jQuery.each(v, function (i, l) {
            self.addListener(l.name, l.listener, l.options);
        });
    } else {
        for (var p in v) {
            if (v.hasOwnProperty(p)) {
                this.addListener(p, v[p]);
            }
        }
    }
}, writeOnly:true}}, EVENTS:{}, _disableListenersCounter:0, addListener:function (name, listener, options) {
    var def = this.EVENTS[name];
    if (!def) {
        throw new dorado.ResourceException("dorado.core.UnknownEvent", name);
    }
    var handler = dorado.Object.apply({}, options);
    handler.listener = listener;
    if (!this._events) {
        this._events = {};
    }
    var handlers = this._events[name];
    if (handlers) {
        if (def.disallowMultiListeners && handlers.length) {
            new dorado.ResourceException("dorado.core.MultiListenersNotSupport", name);
        }
        handlers.push(handler);
    } else {
        this._events[name] = [handler];
    }
    return this;
}, removeListener:function (name, listener) {
    var def = this.EVENTS[name];
    if (!def) {
        throw new dorado.ResourceException("dorado.core.UnknownEvent", name);
    }
    if (!this._events) {
        return;
    }
    var handlers = this._events[name];
    if (handlers) {
        var len = handlers.length;
        for (var i = len - 1; i >= 0; i--) {
            if (handlers[i].listener == listener) {
                handlers.removeAt(i);
            }
        }
    }
}, clearListeners:function (name) {
    if (!this._events) {
        return;
    }
    this._events[name] = null;
}, disableListeners:function () {
    this._disableListenersCounter++;
}, enableListeners:function () {
    if (this._disableListenersCounter > 0) {
        this._disableListenersCounter--;
    }
}, fireEvent:function (name) {
    var def = this.EVENTS[name];
    if (!def) {
        throw new dorado.ResourceException("dorado.core.UnknownEvent", name);
    }
    var interceptor = (def.interceptor instanceof Function) ? def.interceptor : null;
    var handlers;
    if (this._events) {
        handlers = this._events[name];
    }
    var self = this;
    var superFire = function () {
        if (handlers) {
            for (var i = 0; i < handlers.length; ) {
                var handler = handlers[i];
                if (handler.once) {
                    handlers.removeAt(i);
                } else {
                    i++;
                }
                if (self.notifyListener(handler, arguments) === false) {
                    return false;
                }
            }
        }
        return true;
    };
    try {
        if (interceptor) {
            arguments[0] = superFire;
            return interceptor.apply(this, arguments);
        } else {
            if (handlers && this._disableListenersCounter == 0) {
                return superFire.apply(this, Array.prototype.slice.call(arguments, 1));
            }
        }
    }
    catch (e) {
        if (def.processException) {
            dorado.Exception.processException(e);
        } else {
            throw e;
        }
    }
    return true;
}, getListenerCount:function (name) {
    var handlers = null;
    if (this._events) {
        handlers = this._events[name];
    }
    return (handlers) ? handlers.length : 0;
}, notifyListener:function (handler, args) {
    var listener = handler.listener;
    var scope = handler.scope;
    if (!scope && this.getListenerScope) {
        scope = this.getListenerScope();
    }
    scope = scope || this;
    var delay = handler.delay;
    if (delay >= 0) {
        setTimeout(function () {
            listener.apply(scope, args);
        }, delay);
    } else {
        return listener.apply(scope, args);
    }
}});
dorado.TagManager = {_map:{}, _register:function (tag, object) {
    if (!object._id) {
        object._id = dorado.Core.newId();
    }
    var info = this._map[tag];
    if (info) {
        if (!info.idMap[object._id]) {
            info.list.push(object);
            info.idMap[object._id] = object;
        }
    } else {
        this._map[tag] = info = {list:[object], idMap:{}};
        info.idMap[object._id] = object;
    }
}, _unregister:function (tag, object) {
    var info = this._map[tag];
    if (info) {
        if (!info.idMap[object._id]) {
            delete info.idMap[object._id];
            info.list.remove(object);
        }
    }
}, _regOrUnreg:function (object, remove) {
    var tags = object._tags;
    if (tags) {
        if (typeof tags == "string") {
            tags = tags.split(",");
        }
        if (tags instanceof Array) {
            for (var i = 0; i < tags.length; i++) {
                var tag = tags[i];
                if (typeof tag == "string" && tag.length > 0) {
                    remove ? this._unregister(tag, object) : this._register(tag, object);
                }
            }
        }
    }
}, register:function (object) {
    this._regOrUnreg(object);
}, unregister:function (object) {
    this._regOrUnreg(object, true);
}, find:function (tags) {
    var info = this._map[tags];
    return new dorado.ObjectGroup(info ? info.list : null);
}};
dorado.ObjectGroup = $class({constructor:function (objects) {
    if (objects && !(objects instanceof Array)) {
        objects = [objects];
    }
    this.objects = objects || [];
}, set:function (attr, value) {
    if (!this.objects) {
        return;
    }
    for (var i = 0; i < this.objects.length; i++) {
        var object = this.objects[i];
        if (object) {
            object.set(attr, value, true);
        }
    }
    return this;
}, invoke:function (methodName) {
    if (!this.objects) {
        return;
    }
    for (var i = 0; i < this.objects.length; i++) {
        var object = this.objects[i];
        if (object) {
            var method = object[methodName];
            if (method instanceof Function) {
                method.apply(object, Array.prototype.slice.call(arguments, 1));
            }
        }
    }
}});
window.$group = function () {
    return new dorado.ObjectGroup(Array.prototype.slice.call(arguments));
};
window.$tags = function (tags) {
    return dorado.TagManager.find(tags);
};
dorado.Renderer = $class({$className:"dorado.Renderer", render:function (dom, arg) {
}});
dorado.Renderer.NONE_RENDERER = new dorado.Renderer();
dorado.Renderer.render = function (renderer, dom, arg) {
    if (renderer instanceof dorado.Renderer) {
        renderer.render(dom, arg);
    } else {
        if (renderer instanceof Function) {
            renderer(dom, arg);
        }
    }
};
dorado.RenderableElement = $extend(dorado.AttributeSupport, {$className:"dorado.RenderableElement", _ignoreRefresh:0, ATTRIBUTES:{className:{}, exClassName:{skipRefresh:true, setter:function (p, v) {
    if (this._rendered && this._exClassName) {
        $fly(this.getDom()).removeClass(this._exClassName);
    }
    this._exClassName = v;
    if (this._rendered && v) {
        $fly(this.getDom()).addClass(v);
    }
}}, width:{setter:function (p, v) {
    this["_" + p] = isFinite(v) ? parseInt(v) : v;
}}, height:{setter:function (p, v) {
    this["_" + p] = isFinite(v) ? parseInt(v) : v;
}}, style:{setter:function (p, v) {
    if (this._style && v) {
        dorado.Object.apply(this._style, v);
    } else {
        this._style = v;
    }
}}, rendered:{readOnly:true}}, doSet:function (attr, value) {
    $invokeSuper.call(this, arguments);
    var def = this.ATTRIBUTES[attr];
    if (this._rendered && this._ignoreRefresh < 1 && def && !def.skipRefresh) {
        dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", this.refresh, 50);
    }
}, createDom:function () {
    return document.createElement("DIV");
}, refreshDom:function (dom) {
    if (dom.nodeType != 3) {
        var el = $fly(dom);
        if (this._className) {
            el.addClass(this._className);
        }
        if (this._exClassName) {
            el.addClass(this._exClassName);
        }
        if (this._floating) {
            el.addClass(this._floatingClassName);
        }
        if (this._style) {
            var style = this._style;
            if (typeof this._style == "string") {
                var map = {};
                jQuery.each(style.split(";"), function (i, section) {
                    var v = section.split(":");
                    map[jQuery.trim(v[0])] = jQuery.trim(v[1]);
                });
                style = map;
            }
            el.css(style);
            delete this._style;
        }
        this.resetDimension();
    }
}, resetDimension:function (forced) {
    var el = $fly(this.getDom()), changed = false;
    var width = this.getRealWidth();
    var height = this.getRealHeight();
    if (forced || width && this._currentWidth != width) {
        this._currentWidth = width;
        el.outerWidth(width);
        changed = true;
    }
    if (forced || height && this._currentHeight != height) {
        this._currentHeight = height;
        el.outerHeight(height);
        changed = true;
    }
    return changed;
}, getRealWidth:function () {
    return (this._realWidth == null) ? this._width : this._realWidth;
}, getRealHeight:function () {
    return (this._realHeight == null) ? this._height : this._realHeight;
}, getDom:function () {
    if (!this._dom) {
        this._dom = this.createDom();
    }
    return this._dom;
}, doRenderToOrReplace:function (replace, element, nextChildElement) {
    var dom = this.getDom();
    if (!dom) {
        return;
    }
    if (replace) {
        if (!element.parentNode) {
            return;
        }
        element.parentNode.replaceChild(dom, element);
    } else {
        if (!element) {
            element = document.body;
        }
        if (dom.parentNode != element || (nextChildElement && dom.nextSibling != nextChildElement)) {
            if (nextChildElement) {
                element.insertBefore(dom, nextChildElement);
            } else {
                element.appendChild(dom);
            }
        }
    }
    if (this._attached || !this.renderUtilAttached) {
        this.refreshDom(dom);
    }
    this._rendered = true;
}, render:function (containerElement, nextChildElement) {
    this.doRenderToOrReplace(false, containerElement, nextChildElement);
}, replace:function (elmenent) {
    this.doRenderToOrReplace(true, elmenent);
}, unrender:function () {
    var dom = this.getDom();
    if (dom) {
        jQuery(dom).remove();
    }
}, refresh:function (delay) {
    if (!this._rendered || (!this._attached && this.renderUtilAttached)) {
        return;
    }
    if (delay) {
        dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", function () {
            dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");
            this.refreshDom(this.getDom());
        }, 50);
    } else {
        dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");
        this.refreshDom(this.getDom());
    }
}});
dorado.Toolkits = {ajaxDefaultOptions:$setting["ajax.defaultOptions"], ajaxs:{}, getAjax:function (options) {
    var defaultOptions = {};
    if (this.ajaxDefaultOptions) {
        dorado.Object.apply(defaultOptions, this.ajaxDefaultOptions);
    }
    dorado.Object.apply(defaultOptions, options);
    var key = defaultOptions.url || "#EMPTY";
    var ajax = this.ajaxs[key];
    if (ajax === undefined) {
        ajax = new dorado.util.AjaxEngine();
        ajax.set("defaultOptions", defaultOptions);
        autoBatchSupportedUris = $setting["ajax.autoBatchSupportedUris"];
        if (autoBatchSupportedUris) {
            for (var i = 0; i < autoBatchSupportedUris.length; i++) {
                if (key.match(new RegExp(autoBatchSupportedUris[i]))) {
                    ajax.set("autoBatchEnabled", true);
                    break;
                }
            }
        }
        this.ajaxs[key] = ajax;
    }
    return ajax;
}, typesRegistry:{}, typeTranslators:{}, registerPrototype:function (namespace, name, constr) {
    function register(namespace, name, constr) {
        this.typesRegistry[namespace + "." + name] = constr;
    }
    if (typeof name == "object") {
        for (var p in name) {
            if (name.hasOwnProperty(p)) {
                register.call(this, namespace, p, name[p]);
            }
        }
    } else {
        register.call(this, namespace, name, constr);
    }
}, registerTypeTranslator:function (namespace, typeTranslator) {
    this.typeTranslators[namespace] = typeTranslator;
}, getPrototype:function (namespace, name) {
    var ns = namespace.split(",");
    for (var i = 0; i < ns.length; i++) {
        var n = ns[i], constr = this.typesRegistry[n + "." + (name || "Default")];
        if (!constr) {
            var typeTranslator = this.typeTranslators[n];
            if (typeTranslator && typeTranslator instanceof Function) {
                constr = typeTranslator(name);
            }
        }
        if (constr) {
            return constr;
        }
    }
}, createInstance:function (namespace, config, typeTranslator) {
    var type;
    if (typeof config == "string") {
        type = config;
        config = null;
    } else {
        type = config ? config.$type : undefined;
    }
    var constr = this.getPrototype(namespace, type);
    if (!constr) {
        if (typeTranslator && typeTranslator.constructor == String) {
            type = typeTranslator + type;
        }
        if (!constr) {
            if (typeTranslator && typeTranslator instanceof Function) {
                constr = typeTranslator(type);
            }
            if (!constr) {
                if (type) {
                    constr = dorado.util.Common.getClassType(type);
                } else {
                    throw new dorado.ResourceException("dorado.core.TypeUndefined");
                }
            }
        }
        if (constr && type) {
            this.registerPrototype(namespace, type, constr);
        }
    }
    if (!constr) {
        throw new dorado.ResourceException("dorado.core.UnknownType", type);
    }
    return new constr(config);
}, URL_VARS:{}, translateURL:function (url) {
    if (url && url.charAt(0) == ">") {
        var reg = /^\>.*\>/, m = url.match(reg);
        if (m) {
            m = m[0];
            var varName = m.substring(1, m.length - 1);
            var s1 = this.URL_VARS[varName] || "", s2 = url.substring(m.length);
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
            url = s1 + s2;
            if (url.charAt(0) == ">") {
                url = this.translateURL(url);
            }
        } else {
            url = $setting["common.contextPath"] + url.substring(1);
        }
    }
    return url;
}, setDelayedAction:function (owner, actionId, fn, timeMillis) {
    actionId = actionId || dorado.Core.newId();
    this.cancelDelayedAction(owner, actionId);
    owner[actionId] = $setTimeout(owner, fn, timeMillis);
}, cancelDelayedAction:function (owner, actionId) {
    if (owner[actionId]) {
        clearTimeout(owner[actionId]);
        delete owner[actionId];
        return true;
    }
    return false;
}, parseFilterValue:function (text) {
    var operator, c = text.charAt(0);
    if ("=" == c) {
        operator = c;
        text = text.substring(1);
    } else {
        if ("><".indexOf(c) >= 0) {
            operator = c;
            c = text.charAt(1);
            if ("=" == c) {
                operator += c;
                text = text.substring(2);
            } else {
                text = text.substring(1);
            }
        }
    }
    return [operator || "", text];
}, STATE_CODE:{info:0, ok:1, warn:2, error:3}, getTopMessage:function (messages) {
    if (!messages) {
        return null;
    }
    var topMessage = null, topStateCode = -1;
    for (var i = 0; i < messages.length; i++) {
        var message = messages[i];
        var code = this.STATE_CODE[message.state];
        if (code > topStateCode) {
            topStateCode = code;
            topMessage = message;
        }
    }
    return topMessage;
}, getTopMessageState:function (messages) {
    if (!messages) {
        return null;
    }
    var topMessage = this.getTopMessage(messages);
    return topMessage ? topMessage.state : null;
}, trimSingleMessage:function (message, defaultState) {
    if (!message) {
        return null;
    }
    if (typeof message == "string") {
        message = {state:defaultState, text:message};
    } else {
        message.state = message.state || defaultState;
    }
    return message;
}, trimMessages:function (message, defaultState) {
    if (!message) {
        return null;
    }
    var result;
    if (message instanceof Array) {
        var array = [];
        for (var i = 0; i < message.length; i++) {
            var m = this.trimSingleMessage(message[i], defaultState);
            if (!m) {
                continue;
            }
            array.push(m);
        }
        result = (array.length) ? array : null;
    } else {
        result = [this.trimSingleMessage(message, defaultState)];
    }
    return result;
}};
window.$url = function (url) {
    return dorado.Toolkits.translateURL(url);
};
dorado.Toolkits.URL_VARS.skin = $url($setting["widget.skinRoot"] + ($setting["widget.skin"] ? ($setting["widget.skin"] + "/") : ""));
dorado.util = {};
Date.parseFunctions = {count:0};
Date.parseRegexes = [];
Date.formatFunctions = {count:0};
Date.prototype.dateFormat = function (format) {
    if (Date.formatFunctions[format] == null) {
        Date.createNewFormat(format);
    }
    var func = Date.formatFunctions[format];
    return this[func]();
};
Date.createNewFormat = function (format) {
    var funcName = "format" + Date.formatFunctions.count++;
    Date.formatFunctions[format] = funcName;
    var code = "Date.prototype." + funcName + " = function(){return ";
    var special = false;
    var ch = "";
    for (var i = 0; i < format.length; ++i) {
        ch = format.charAt(i);
        if (!special && ch == "\\") {
            special = true;
        } else {
            if (special) {
                special = false;
                code += "'" + String.escape(ch) + "' + ";
            } else {
                code += Date.getFormatCode(ch);
            }
        }
    }
    eval(code.substring(0, code.length - 3) + ";}");
};
Date.getFormatCode = function (character) {
    switch (character) {
      case "d":
        return "String.leftPad(this.getDate(), 2, '0') + ";
      case "D":
        return "Date.dayNames[this.getDay()].substring(0, 3) + ";
      case "j":
        return "this.getDate() + ";
      case "l":
        return "Date.dayNames[this.getDay()] + ";
      case "S":
        return "this.getSuffix() + ";
      case "w":
        return "this.getDay() + ";
      case "z":
        return "this.getDayOfYear() + ";
      case "W":
        return "this.getWeekOfYear() + ";
      case "F":
        return "Date.monthNames[this.getMonth()] + ";
      case "m":
        return "String.leftPad(this.getMonth() + 1, 2, '0') + ";
      case "M":
        return "Date.monthNames[this.getMonth()].substring(0, 3) + ";
      case "n":
        return "(this.getMonth() + 1) + ";
      case "t":
        return "this.getDaysInMonth() + ";
      case "L":
        return "(this.isLeapYear() ? 1 : 0) + ";
      case "Y":
        return "this.getFullYear() + ";
      case "y":
        return "('' + this.getFullYear()).substring(2, 4) + ";
      case "a":
        return "(this.getHours() < 12 ? 'am' : 'pm') + ";
      case "A":
        return "(this.getHours() < 12 ? 'AM' : 'PM') + ";
      case "g":
        return "((this.getHours() %12) ? this.getHours() % 12 : 12) + ";
      case "G":
        return "this.getHours() + ";
      case "h":
        return "String.leftPad((this.getHours() %12) ? this.getHours() % 12 : 12, 2, '0') + ";
      case "H":
        return "String.leftPad(this.getHours(), 2, '0') + ";
      case "i":
        return "String.leftPad(this.getMinutes(), 2, '0') + ";
      case "s":
        return "String.leftPad(this.getSeconds(), 2, '0') + ";
      case "O":
        return "this.getGMTOffset() + ";
      case "T":
        return "this.getTimezone() + ";
      case "Z":
        return "(this.getTimezoneOffset() * -60) + ";
      default:
        return "'" + String.escape(character) + "' + ";
    }
};
Date.parseDate = function (input, format) {
    if (Date.parseFunctions[format] == null) {
        Date.createParser(format);
    }
    var func = Date.parseFunctions[format];
    return Date[func](input);
};
Date.createParser = function (format) {
    var funcName = "parse" + Date.parseFunctions.count++;
    var regexNum = Date.parseRegexes.length;
    var currentGroup = 1;
    Date.parseFunctions[format] = funcName;
    var code = "Date." + funcName + " = function(input){\n" + "var y = -1, m = -1, d = -1, h = -1, i = -1, s = -1;\n" + "var d = new Date();\n" + "y = d.getFullYear();\n" + "m = d.getMonth();\n" + "d = d.getDate();\n" + "var results = input.match(Date.parseRegexes[" + regexNum + "]);\n" + "if (results && results.length > 0) {";
    var regex = "";
    var special = false;
    var ch = "";
    for (var i = 0; i < format.length; ++i) {
        ch = format.charAt(i);
        if (!special && ch == "\\") {
            special = true;
        } else {
            if (special) {
                special = false;
                regex += String.escape(ch);
            } else {
                obj = Date.formatCodeToRegex(ch, currentGroup);
                currentGroup += obj.g;
                regex += obj.s;
                if (obj.g && obj.c) {
                    code += obj.c;
                }
            }
        }
    }
    code += "if (y > 0 && m >= 0 && d > 0 && h >= 0 && i >= 0 && s >= 0)\n" + "{return new Date(y, m, d, h, i, s);}\n" + "else if (y > 0 && m >= 0 && d > 0 && h >= 0 && i >= 0)\n" + "{return new Date(y, m, d, h, i);}\n" + "else if (y > 0 && m >= 0 && d > 0 && h >= 0)\n" + "{return new Date(y, m, d, h);}\n" + "else if (y > 0 && m >= 0 && d > 0)\n" + "{return new Date(y, m, d);}\n" + "else if (y > 0 && m >= 0)\n" + "{return new Date(y, m);}\n" + "else if (y > 0)\n" + "{return new Date(y);}\n" + "}return null;}";
    Date.parseRegexes[regexNum] = new RegExp("^" + regex + "$");
    eval(code);
};
Date.formatCodeToRegex = function (character, currentGroup) {
    switch (character) {
      case "D":
        return {g:0, c:null, s:"(?:Sun|Mon|Tue|Wed|Thu|Fri|Sat)"};
      case "j":
      case "d":
        return {g:1, c:"d = parseInt(results[" + currentGroup + "], 10);\n", s:"(\\d{1,2})"};
      case "l":
        return {g:0, c:null, s:"(?:" + Date.dayNames.join("|") + ")"};
      case "S":
        return {g:0, c:null, s:"(?:st|nd|rd|th)"};
      case "w":
        return {g:0, c:null, s:"\\d"};
      case "z":
        return {g:0, c:null, s:"(?:\\d{1,3})"};
      case "W":
        return {g:0, c:null, s:"(?:\\d{2})"};
      case "F":
        return {g:1, c:"m = parseInt(Date.monthNumbers[results[" + currentGroup + "].substring(0, 3)], 10);\n", s:"(" + Date.monthNames.join("|") + ")"};
      case "M":
        return {g:1, c:"m = parseInt(Date.monthNumbers[results[" + currentGroup + "]], 10);\n", s:"(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)"};
      case "n":
      case "m":
        return {g:1, c:"m = parseInt(results[" + currentGroup + "], 10) - 1;\n", s:"(\\d{1,2})"};
      case "t":
        return {g:0, c:null, s:"\\d{1,2}"};
      case "L":
        return {g:0, c:null, s:"(?:1|0)"};
      case "Y":
        return {g:1, c:"y = parseInt(results[" + currentGroup + "], 10);\n", s:"(\\d{4})"};
      case "y":
        return {g:1, c:"var ty = parseInt(results[" + currentGroup + "], 10);\n" + "y = ty > Date.y2kYear ? 1900 + ty : 2000 + ty;\n", s:"(\\d{1,2})"};
      case "a":
        return {g:1, c:"if (results[" + currentGroup + "] == 'am') {\n" + "if (h == 12) { h = 0; }\n" + "} else { if (h < 12) { h += 12; }}", s:"(am|pm)"};
      case "A":
        return {g:1, c:"if (results[" + currentGroup + "] == 'AM') {\n" + "if (h == 12) { h = 0; }\n" + "} else { if (h < 12) { h += 12; }}", s:"(AM|PM)"};
      case "g":
      case "G":
      case "h":
      case "H":
        return {g:1, c:"h = parseInt(results[" + currentGroup + "], 10);\n", s:"(\\d{1,2})"};
      case "i":
        return {g:1, c:"i = parseInt(results[" + currentGroup + "], 10);\n", s:"(\\d{2})"};
      case "s":
        return {g:1, c:"s = parseInt(results[" + currentGroup + "], 10);\n", s:"(\\d{2})"};
      case "O":
        return {g:0, c:null, s:"[+-]\\d{4}"};
      case "T":
        return {g:0, c:null, s:"[A-Z]{3}"};
      case "Z":
        return {g:0, c:null, s:"[+-]\\d{1,5}"};
      default:
        return {g:0, c:null, s:String.escape(character)};
    }
};
Date.prototype.getTimezone = function () {
    return this.toString().replace(/^.*? ([A-Z]{3}) [0-9]{4}.*$/, "$1").replace(/^.*?\(([A-Z])[a-z]+ ([A-Z])[a-z]+ ([A-Z])[a-z]+\)$/, "$1$2$3");
};
Date.prototype.getGMTOffset = function () {
    return (this.getTimezoneOffset() > 0 ? "-" : "+") + String.leftPad(Math.floor(this.getTimezoneOffset() / 60), 2, "0") + String.leftPad(this.getTimezoneOffset() % 60, 2, "0");
};
Date.prototype.getDayOfYear = function () {
    var num = 0;
    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
    for (var i = 0; i < this.getMonth(); ++i) {
        num += Date.daysInMonth[i];
    }
    return num + this.getDate() - 1;
};
Date.prototype.getWeekOfYear = function () {
    var now = this.getDayOfYear() + (4 - this.getDay());
    var jan1 = new Date(this.getFullYear(), 0, 1);
    var then = (7 - jan1.getDay() + 4);
    document.write(then);
    return String.leftPad(((now - then) / 7) + 1, 2, "0");
};
Date.prototype.isLeapYear = function () {
    var year = this.getFullYear();
    return ((year & 3) == 0 && (year % 100 || (year % 400 == 0 && year)));
};
Date.prototype.getFirstDayOfMonth = function () {
    var day = (this.getDay() - (this.getDate() - 1)) % 7;
    return (day < 0) ? (day + 7) : day;
};
Date.prototype.getLastDayOfMonth = function () {
    var day = (this.getDay() + (Date.daysInMonth[this.getMonth()] - this.getDate())) % 7;
    return (day < 0) ? (day + 7) : day;
};
Date.prototype.getDaysInMonth = function () {
    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
    return Date.daysInMonth[this.getMonth()];
};
Date.prototype.getSuffix = function () {
    switch (this.getDate()) {
      case 1:
      case 21:
      case 31:
        return "st";
      case 2:
      case 22:
        return "nd";
      case 3:
      case 23:
        return "rd";
      default:
        return "th";
    }
};
String.escape = function (string) {
    return string.replace(/('|\\)/g, "\\$1");
};
String.leftPad = function (val, size, ch) {
    var result = new String(val);
    if (ch == null) {
        ch = " ";
    }
    while (result.length < size) {
        result = ch + result;
    }
    return result;
};
Date.daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
Date.monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
Date.dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
Date.y2kYear = 50;
Date.monthNumbers = {Jan:0, Feb:1, Mar:2, Apr:3, May:4, Jun:5, Jul:6, Aug:7, Sep:8, Oct:9, Nov:10, Dec:11};
Date.patterns = {ISO8601LongPattern:"Y-m-d H:i:s", ISO8601ShortPattern:"Y-m-d", ShortDatePattern:"n/j/Y", LongDatePattern:"l, F d, Y", FullDateTimePattern:"l, F d, Y g:i:s A", MonthDayPattern:"F d", ShortTimePattern:"g:i A", LongTimePattern:"g:i:s A", SortableDateTimePattern:"Y-m-d\\TH:i:s", UniversalSortableDateTimePattern:"Y-m-d H:i:sO", YearMonthPattern:"F, Y"};
dorado.util.Common = {formatFloat:function (n, format) {
    function formatInt(n, format, dec) {
        if (!format) {
            return (parseInt(n.substring(0, nfs.length)) + 1) + "";
        }
        var c, f, r = "", j = 0, prefix = "";
        var fv = format.split("");
        for (var i = 0; i < fv.length; i++) {
            f = fv[i];
            if (f == "#" || f == "0" || f == "`") {
                fv = fv.slice(i);
                break;
            }
            prefix += f;
        }
        fv = fv.reverse();
        var cv = n.split("").reverse();
        for (var i = 0; i < fv.length; i++) {
            f = fv[i];
            if (f == "#") {
                if (j < cv.length) {
                    if (n == "0") {
                        j = cv.length;
                    } else {
                        if (n == "-0") {
                            if (dec) {
                                r += "-";
                            }
                            j = cv.length;
                        } else {
                            r += cv[j++];
                        }
                    }
                }
            } else {
                if (f == "0") {
                    if (j < cv.length) {
                        r += cv[j++];
                    } else {
                        r += f;
                    }
                } else {
                    if (f == "`") {
                        var commaCount = 3;
                        while (j < cv.length) {
                            var c = cv[j++];
                            if (commaCount == 3 && c != "-") {
                                r += ",";
                                commaCount = 0;
                            }
                            r += c;
                            commaCount++;
                        }
                    } else {
                        r += f;
                    }
                }
            }
        }
        while (j < cv.length) {
            r += cv[j++];
        }
        return prefix + r.split("").reverse().join("");
    }
    function formatDecimal(n, format) {
        var nfs = (format) ? format.match(/[\#0]/g) : null;
        if (nfs === null) {
            return [format, (n && n.charAt(0) > "4")];
        } else {
            if (n && n.length > nfs.length && n.charAt(nfs.length) > "4") {
                n = (parseInt(n.substring(0, nfs.length)) + 1) + "";
            }
        }
        var f, r = "", j = 0;
        for (var i = 0; i < format.length; i++) {
            f = format.charAt(i);
            if (f == "#" || f == "0") {
                if (n && j < n.length) {
                    r += n.charAt(j++);
                } else {
                    if (f == "0") {
                        r += f;
                    }
                }
            } else {
                r += f;
            }
        }
        return [r, false];
    }
    if (n == null || isNaN(n)) {
        return "";
    }
    n = n + "";
    if (!format) {
        return n;
    }
    var n1, n2, f1, f2, i;
    i = n.indexOf(".");
    if (i > 0) {
        n1 = n.substring(0, i);
        n2 = n.substring(i + 1);
    } else {
        n1 = n;
    }
    i = format.indexOf(".");
    if (i > 0) {
        f1 = format.substring(0, i);
        f2 = format.substring(i + 1);
    } else {
        f1 = format;
    }
    f1 = f1.replace(/\#,/g, "`");
    var r = formatDecimal(n2, f2);
    var dec = r[0];
    if (r[1]) {
        n1 = (parseInt(n1) + ((n1.charAt(0) == "-") ? -1 : 1)) + "";
    }
    return formatInt(n1, f1, dec) + ((dec) ? ("." + dec) : "");
}, parseFloat:function (s) {
    if (!s) {
        return Number.NaN;
    }
    var ns = (s + "").match(/[-\d\.]/g);
    if (!ns) {
        return Number.NaN;
    }
    return parseFloat(ns.join(""));
}, getClassType:function (type, silence) {
    var classType = null;
    try {
        classType = eval(type);
    }
    catch (e) {
        if (!silence) {
            throw new dorado.ResourceException("dorado.core.UnknownType", type);
        }
    }
    return classType;
}, singletonInstance:{}, getSingletonInstance:function (factory) {
    var typeName;
    if (typeof factory == "string") {
        typeName = factory;
    } else {
        typeName = factory._singletonId;
        if (!typeName) {
            factory._singletonId = typeName = dorado.Core.newId();
        }
    }
    var instance = this.singletonInstance[typeName];
    if (!instance) {
        if (typeof factory == "string") {
            var classType = dorado.util.Common.getClassType(typeName);
            instance = new classType();
        } else {
            instance = new factory();
        }
        this.singletonInstance[typeName] = instance;
    }
    return instance;
}};
window.$singleton = function (factory) {
    return dorado.util.Common.getSingletonInstance(factory);
};
dorado.util.Resource = {append:function (namespace, items) {
    if (arguments.length == 1 && namespace && namespace.constructor != String) {
        items = namespace;
        namespace = null;
    }
    var nItems = this, done;
    if (namespace) {
        var ns = namespace.split(".");
        for (var i = 0; i < ns.length; i++) {
            var n = ns[i];
            var t = nItems[n];
            if (t == null) {
                if (i == ns.length - 1) {
                    t = items;
                    done = true;
                } else {
                    t = {};
                }
                nItems[n] = t;
            }
            nItems = t;
        }
    }
    if (!done && items) {
        dorado.Object.apply(nItems, items);
    }
}, sprintf:function () {
    var num = arguments.length;
    var s = arguments[0];
    for (var i = 1; i < num; i++) {
        var pattern = "\\{" + (i - 1) + "\\}";
        var re = new RegExp(pattern, "g");
        s = s.replace(re, arguments[i]);
    }
    return s;
}, get:function (path) {
    var ns = path.split(".");
    var nItems = this;
    for (var i = 0; nItems != null && i < ns.length; i++) {
        nItems = nItems[ns[i]];
    }
    if (nItems == null) {
        return "";
    } else {
        nItems += "";
        if (arguments.length > 1) {
            arguments[0] = nItems;
            return this.sprintf.apply(this, arguments);
        } else {
            return nItems;
        }
    }
}};
window.$resource = function (path, args) {
    return dorado.util.Resource.get.apply(dorado.util.Resource, arguments);
};
(function () {
    dorado.util.Iterator = $class({$className:"dorado.util.Iterator", first:dorado._NULL_FUNCTION, last:dorado._NULL_FUNCTION, hasPrevious:dorado._NULL_FUNCTION, hasNext:dorado._NULL_FUNCTION, previous:dorado._NULL_FUNCTION, next:dorado._NULL_FUNCTION, current:dorado._NULL_FUNCTION, createBookmark:dorado._UNSUPPORTED_FUNCTION(), restoreBookmark:dorado._UNSUPPORTED_FUNCTION()});
    dorado.util.ArrayIterator = $extend(dorado.util.Iterator, {$className:"dorado.util.ArrayIterator", constructor:function (v, nextIndex) {
        this._v = v;
        this._current = (nextIndex || 0) - 1;
    }, first:function () {
        this._current = -1;
    }, last:function () {
        this._current = this._v.length;
    }, hasPrevious:function () {
        return this._current > 0;
    }, hasNext:function () {
        return this._current < (this._v.length - 1);
    }, previous:function () {
        return (this._current < 0) ? null : this._v[--this._current];
    }, next:function () {
        return (this._current >= this._v.length) ? null : this._v[++this._current];
    }, current:function () {
        return this._v[this._current];
    }, setNextIndex:function (nextIndex) {
        this._current = nextIndex - 1;
    }, createBookmark:function () {
        return this._current;
    }, restoreBookmark:function (bookmark) {
        this._current = bookmark;
    }});
})();
dorado.util.KeyedArray = $class({$className:"dorado.util.KeyedArray", constructor:function (getKeyFunction) {
    this.items = [];
    this._keyMap = {};
    this._getKeyFunction = getKeyFunction;
}, size:0, _getKey:function (data) {
    var key = this._getKeyFunction ? this._getKeyFunction(data) : null;
    return key + "";
}, insert:function (data, insertMode, refData) {
    var ctx;
    if (this.beforeInsert) {
        ctx = this.beforeInsert(data);
    }
    if (!isFinite(insertMode) && insertMode) {
        switch (insertMode) {
          case "begin":
            insertMode = 0;
            break;
          case "before":
            insertMode = this.items.indexOf(refData);
            if (insertMode < 0) {
                insertMode = 0;
            }
            break;
          case "after":
            insertMode = this.items.indexOf(refData) + 1;
            if (insertMode >= this.items.length) {
                insertMode = null;
            }
            break;
          default:
            insertMode = null;
            break;
        }
    }
    if (insertMode != null && isFinite(insertMode) && insertMode >= 0) {
        this.items.insert(data, insertMode);
    } else {
        this.items.push(data);
    }
    this.size++;
    var key = this._getKey(data);
    if (key) {
        this._keyMap[key] = data;
    }
    if (this.afterInsert) {
        this.afterInsert(data, ctx);
    }
}, append:function (data) {
    this.insert(data);
}, remove:function (data) {
    var ctx;
    if (this.beforeRemove) {
        ctx = this.beforeRemove(data);
    }
    var i = this.items.remove(data);
    if (i >= 0) {
        this.size--;
        var key = this._getKey(data);
        if (key) {
            delete this._keyMap[key];
        }
    }
    if (this.afterRemove) {
        this.afterRemove(data, ctx);
    }
    return i;
}, removeAt:function (i) {
    if (i >= 0 && i < this.size) {
        var data = this.items[i], ctx;
        if (data) {
            if (this.beforeRemove) {
                ctx = this.beforeRemove(data);
            }
            var key = this._getKey(data);
            if (key) {
                delete this._keyMap[key];
            }
        }
        this.items.removeAt(i);
        this.size--;
        if (data && this.afterRemove) {
            this.afterRemove(data, ctx);
        }
        return data;
    }
    return null;
}, indexOf:function (data) {
    return this.items.indexOf(data);
}, replace:function (oldData, newData) {
    var i = this.indexOf(oldData);
    if (i >= 0) {
        this.removeAt(i);
        this.insert(newData, i);
    }
    return i;
}, get:function (k) {
    return (k >= 0) ? this.items[k] : this._keyMap[k];
}, clear:function () {
    for (var i = this.size - 1; i >= 0; i--) {
        this.removeAt(i);
    }
}, iterator:function (from) {
    var start = this.items.indexOf(from);
    if (start < 0) {
        start = 0;
    }
    return new dorado.util.ArrayIterator(this.items, start);
}, each:function (fn, scope) {
    var array = this.items;
    for (var i = 0; i < array.length; i++) {
        if (fn.call(scope || array[i], array[i], i) === false) {
            return i;
        }
    }
}, toArray:function () {
    return this.items.slice(0);
}, clone:function () {
    return dorado.Core.clone(this);
}, deepClone:function () {
    var cloned = new dorado.util.KeyedArray(this._getKeyFunction);
    for (var i = 0; i < this.items.length; i++) {
        cloned.append(dorado.Core.clone(this.items[i]));
    }
    return cloned;
}});
dorado.util.KeyedList = $class({$className:"dorado.util.KeyedList", constructor:function (getKeyFunction) {
    this._keyMap = {};
    this._getKeyFunction = getKeyFunction;
}, size:0, _getKey:function (data) {
    return this._getKeyFunction ? this._getKeyFunction(data) : null;
}, _registerEntry:function (entry) {
    var key = this._getKey(entry.data);
    if (key != null) {
        this._keyMap[key] = entry;
    }
}, _unregisterEntry:function (entry) {
    var key = this._getKey(entry.data);
    if (key != null) {
        delete this._keyMap[key];
    }
}, _unregisterAllEntries:function () {
    this._keyMap = {};
}, insertEntry:function (entry, insertMode, refEntry) {
    var e1, e2;
    switch (insertMode) {
      case "begin":
        e1 = null;
        e2 = this.first;
        break;
      case "before":
        e1 = (refEntry) ? refEntry.previous : null;
        e2 = refEntry;
        break;
      case "after":
        e1 = refEntry;
        e2 = (refEntry) ? refEntry.next : null;
        break;
      default:
        e1 = this.last;
        e2 = null;
        break;
    }
    entry.previous = e1;
    entry.next = e2;
    if (e1) {
        e1.next = entry;
    } else {
        this.first = entry;
    }
    if (e2) {
        e2.previous = entry;
    } else {
        this.last = entry;
    }
    this._registerEntry(entry);
    this.size++;
}, removeEntry:function (entry) {
    var e1, e2;
    e1 = entry.previous;
    e2 = entry.next;
    if (e1) {
        e1.next = e2;
    } else {
        this.first = e2;
    }
    if (e2) {
        e2.previous = e1;
    } else {
        this.last = e1;
    }
    this._unregisterEntry(entry);
    this.size--;
}, findEntry:function (data) {
    if (data == null) {
        return null;
    }
    var key = this._getKey(data);
    if (key != null) {
        return this._keyMap[key];
    } else {
        var entry = this.first;
        while (entry) {
            if (entry.data === data) {
                return entry;
            }
            entry = entry.next;
        }
    }
    return null;
}, insert:function (data, insertMode, refData) {
    var refEntry = null;
    if (refData != null) {
        refEntry = this.findEntry(refData);
    }
    var entry = {data:data};
    this.insertEntry(entry, insertMode, refEntry);
}, append:function (data) {
    this.insert(data);
}, remove:function (data) {
    var entry = this.findEntry(data);
    if (entry != null) {
        this.removeEntry(entry);
    }
    return (entry != null);
}, removeKey:function (key) {
    var entry = this._keyMap[key];
    if (entry) {
        this.removeEntry(entry);
        return entry.data;
    }
    return null;
}, get:function (key) {
    var entry = this._keyMap[key];
    if (entry) {
        return entry.data;
    }
    return null;
}, clear:function () {
    var entry = this.first;
    while (entry) {
        if (entry.data) {
            delete element.data;
        }
        entry = entry.next;
    }
    this._unregisterAllEntries();
    this.first = null;
    this.last = null;
    this.size = 0;
}, iterator:function (from) {
    return new dorado.util.KeyedListIterator(this, from);
}, each:function (fn, scope) {
    var entry = this.first, i = 0;
    while (entry != null) {
        if (fn.call(scope || entry.data, entry.data, i++) === false) {
            break;
        }
        entry = entry.next;
    }
}, toArray:function () {
    var v = [], entry = this.first;
    while (entry != null) {
        v.push(entry.data);
        entry = entry.next;
    }
    return v;
}, getFirst:function () {
    return this.first ? this.first.data : null;
}, getLast:function () {
    return this.last ? this.last.data : null;
}, clone:function () {
    var cloned = new dorado.util.KeyedList(this._getKeyFunction);
    var entry = this.first;
    while (entry != null) {
        cloned.append(entry.data);
        entry = entry.next;
    }
    return cloned;
}, deepClone:function () {
    var cloned = new dorado.util.KeyedList(this._getKeyFunction);
    var entry = this.first;
    while (entry != null) {
        cloned.append(dorado.Core.clone(entry.data));
        entry = entry.next;
    }
    return cloned;
}});
dorado.util.KeyedListIterator = $extend(dorado.util.Iterator, {$className:"dorado.util.KeyedListIterator", constructor:function (list, from) {
    this._list = list;
    this.current = null;
    if (from) {
        this.current = list.findEntry(from);
    }
    this.isFirst = (this.current == null);
    this.isLast = false;
}, first:function () {
    this.isFirst = true;
    this.isLast = false;
    this.current = null;
}, last:function () {
    this.isFirst = false;
    this.isLast = true;
    this.current = null;
}, hasNext:function () {
    if (this.isFirst) {
        return (this._list.first != null);
    } else {
        if (this.current != null) {
            return (this.current.next != null);
        } else {
            return false;
        }
    }
}, hasPrevious:function () {
    if (this.isLast) {
        return (this._list.last != null);
    } else {
        if (this.current != null) {
            return (this.current.previous != null);
        } else {
            return false;
        }
    }
}, next:function () {
    var current = this.current;
    if (this.isFirst) {
        current = this._list.first;
    } else {
        if (current != null) {
            current = current.next;
        } else {
            current = null;
        }
    }
    this.current = current;
    this.isFirst = false;
    if (current != null) {
        this.isLast = false;
        return current.data;
    } else {
        this.isLast = true;
        return null;
    }
}, previous:function () {
    var current = this.current;
    if (this.isLast) {
        current = this._list.last;
    } else {
        if (current != null) {
            current = current.previous;
        } else {
            current = null;
        }
    }
    this.current = current;
    this.isLast = false;
    if (current != null) {
        this.isFirst = false;
        return current.data;
    } else {
        this.isFirst = true;
        return null;
    }
}, current:function () {
    return (this.current) ? this.current.data : null;
}, createBookmark:function () {
    return {isFirst:this.isFirst, isLast:this.isLast, current:this.current};
}, restoreBookmark:function (bookmark) {
    this.isFirst = bookmark.isFirst;
    this.isLast = bookmark.isLast;
    this.current = bookmark.current;
}});
dorado.util.ObjectPool = $class({$className:"dorado.util.ObjectPool", constructor:function (factory) {
    dorado.util.ObjectPool.OBJECT_POOLS.push(this);
    this._factory = factory;
    this._idlePool = [];
    this._activePool = [];
}, borrowObject:function () {
    var object = null;
    var factory = this._factory;
    if (this._idlePool.length > 0) {
        object = this._idlePool.pop();
    } else {
        object = factory.makeObject();
    }
    if (object != null) {
        this._activePool.push(object);
        if (factory.activateObject) {
            factory.activateObject(object);
        }
    }
    return object;
}, returnObject:function (object) {
    if (object != null) {
        var factory = this._factory;
        var i = this._activePool.indexOf(object);
        if (i < 0) {
            return;
        }
        if (factory.passivateObject) {
            factory.passivateObject(object);
        }
        this._activePool.removeAt(i);
        this._idlePool.push(object);
    }
}, getNumActive:function () {
    return this._activePool.length;
}, getNumIdle:function () {
    return this._idlePool.length;
}, destroy:function () {
    if (!!this._destroyed) {
        return;
    }
    var factory = this._factory;
    function returnObject(object) {
        if (factory.passivateObject) {
            factory.passivateObject(object);
        }
    }
    function destroyObject(object) {
        if (factory.destroyObject) {
            factory.destroyObject(object);
        }
    }
    var activePool = this._activePool;
    for (var i = 0; i < activePool.length; i++) {
        var object = activePool[i];
        returnObject(object);
        destroyObject(object);
    }
    var idlePool = this._idlePool;
    for (var i = 0; i < idlePool.length; i++) {
        var object = idlePool[i];
        destroyObject(object);
    }
    this._factory = null;
    this._destroyed = true;
}});
dorado.util.ObjectPool.OBJECT_POOLS = [];
jQuery(window).unload(function () {
    var pools = dorado.util.ObjectPool.OBJECT_POOLS;
    for (var i = 0; i < pools.length; i++) {
        pools[i].destroy();
    }
});
(function () {
    function f(n) {
        return n < 10 ? "0" + n : n;
    }
    Date.prototype.toJSON = function (key) {
        return this.getFullYear() + "-" + f(this.getMonth() + 1) + "-" + f(this.getDate()) + "T" + f(this.getHours()) + ":" + f(this.getMinutes()) + ":" + f(this.getSeconds()) + "Z";
    };
    dorado.JSON = {parse:function (text, untrusty) {
        return text ? ((untrusty) ? JSON.parse(text) : eval("(" + text + ")")) : null;
    }, stringify:function (value, options) {
        if (value != null) {
            if (value instanceof dorado.Entity || value instanceof dorado.EntityList) {
                value = value.toJSON(options);
            }
        }
        return JSON.stringify(value, (options != null) ? options.replacer : null);
    }, evaluate:function (template) {
        function toJSON(obj) {
            if (obj instanceof Function) {
                obj = obj.call(dorado.$this || this);
            }
            var json;
            if (obj instanceof dorado.Entity || obj instanceof dorado.EntityList) {
                json = obj.toJSON({generateDataType:true});
            } else {
                if (obj instanceof Object && !(obj instanceof Date)) {
                    json = {};
                    for (var p in obj) {
                        if (obj.hasOwnProperty(p)) {
                            v = obj[p];
                            if (v === undefined) {
                                continue;
                            }
                            if (v != null) {
                                v = toJSON.call(obj, v);
                            }
                            json[p] = v;
                        }
                    }
                } else {
                    json = obj;
                }
            }
            return json;
        }
        return toJSON(template);
    }};
})();
dorado.util.AjaxConnectionPool = new dorado.util.ObjectPool({activeX:["MSXML2.XMLHTTP.6.0", "MSXML2.XMLHTTP.5.0", "MSXML2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"], _createXMLHttpRequest:function () {
    try {
        return new XMLHttpRequest();
    }
    catch (e) {
        for (var i = 0; i < this.activeX.length; ++i) {
            try {
                return new ActiveXObject(this.activeX[i]);
            }
            catch (e) {
            }
        }
    }
}, makeObject:function () {
    return {conn:this._createXMLHttpRequest()};
}, passivateObject:function (connObj) {
    delete connObj.url;
    delete connObj.method;
    delete connObj.options;
    var conn = connObj.conn;
    conn.onreadystatechange = dorado._NULL_FUNCTION;
    conn.abort();
}});
dorado.util.AjaxEngine = $extend([dorado.AttributeSupport, dorado.EventSupport], {$className:"dorado.util.AjaxEngine", constructor:function () {
    $invokeSuper.call(this, arguments);
    this._requests = [];
    this._connectionPool = dorado.util.AjaxConnectionPool;
}, ATTRIBUTES:{defaultOptions:{writeOnce:true}, autoBatchEnabled:{setter:function (attr, value) {
    if (value && !(this._defaultOptions && this._defaultOptions.url)) {
        throw new dorado.ResourceException("dorado.core.BatchUrlUndefined");
    }
    this["_" + attr] = value;
}}, minConnectInterval:{defaultValue:50}, maxBatchSize:{defaultValue:20}}, EVENTS:{beforeRequest:{}, onRequest:{}, beforeConnect:{}, onConnect:{}}, request:function (options, callback) {
    if (typeof options == "string") {
        options = {url:options};
    }
    var useBatch = this._autoBatchEnabled;
    if (this._autoBatchEnabled) {
        if (options) {
            if (options.url && options.url != this._defaultOptions.url || options.method && options.method != "POST") {
                useBatch = false;
            }
            if (useBatch && options.headers) {
                for (var prop in options.headers) {
                    if (options.headers.hasOwnProperty(prop)) {
                        useBatch = false;
                        break;
                    }
                }
            }
        }
    }
    if (useBatch) {
        var requests = this._requests;
        if (requests.length == 0) {
            this._batchTimerId = $setTimeout(this, function () {
                this._requestBatch();
            }, this._minConnectInterval);
        }
        this.fireEvent("beforeRequest", this, {async:true, options:options});
        var message = options.message, taskId;
        if (message) {
            taskId = dorado.util.TaskIndicator.showTaskIndicator(message);
        }
        requests.push({options:options, callback:callback, taskId:taskId});
        if (requests.length >= this._maxBatchSize) {
            this._requestBatch();
        }
    } else {
        this.requestAsync(options, callback);
    }
}, _requestBatch:function () {
    if (this._batchTimerId) {
        clearTimeout(this._batchTimerId);
        this._batchTimerId = 0;
    }
    var requests = this._requests;
    if (requests.length == 0) {
        return;
    }
    this._requests = [];
    var batchCallback = {scope:this, callback:function (success, batchResult) {
        function createAjaxResult(options) {
            var result = new dorado.util.AjaxResult(options);
            result._init(batchResult._connObj);
            return result;
        }
        if (success) {
            var xmlDoc = jQuery(batchResult.getXmlDocument());
            var i = 0;
            xmlDoc.find("result>request").each($scopify(this, function (index, elem) {
                var request = requests[i];
                if (request.taskId) {
                    dorado.util.TaskIndicator.hideTaskIndicator(request.taskId);
                }
                var result = createAjaxResult(request.options);
                var el = jQuery(elem);
                var errorEl = el.children("error");
                var success = (errorEl.size() == 0);
                if (success) {
                    var responseEl = el.children("response");
                    result.text = unescape(responseEl.text());
                } else {
                    result.text = unescape(errorEl.text());
                    result._setError(result._parseError(result.text, batchResult._connObj));
                }
                $callback(request.callback, success, result);
                this.fireEvent("onRequest", this, {async:true, result:result});
                i++;
            }));
        } else {
            for (var i = 0; i < requests.length; i++) {
                var request = requests[i];
                if (request.taskId) {
                    dorado.util.TaskIndicator.hideTaskIndicator(request.taskId);
                }
                var result = createAjaxResult(request.options);
                result._setError(batchResult.error);
                $callback(request.callback, false, result);
                this.fireEvent("onRequest", this, {async:true, result:result});
            }
        }
    }};
    var sendData = ["<batch>\n"];
    for (var i = 0; i < requests.length; i++) {
        var request = requests[i];
        var options = request.options;
        var type = "";
        if (options) {
            if (options.xmlData) {
                type = "xml";
            } else {
                if (options.jsonData) {
                    type = "json";
                }
            }
        }
        sendData.push("<request type=\"" + type + "\"><![CDATA[");
        sendData.push(this._getSendData(options));
        sendData.push("]]></request>\n");
    }
    sendData.push("</batch>");
    var batchOptions = {isBatch:true, xmlData:sendData.join("")};
    this.requestAsync(batchOptions, batchCallback);
}, requestAsync:function (options, callback) {
    var connObj = this._connectionPool.borrowObject();
    this._init(connObj, options, true);
    var eventArg = {async:true, options:options};
    if (options == null || !options.isBatch) {
        this.fireEvent("beforeRequest", this, eventArg);
    }
    this.fireEvent("beforeConnect", this, eventArg);
    var conn = connObj.conn;
    var message = options.message, taskId;
    if (message) {
        taskId = dorado.util.TaskIndicator.showTaskIndicator(message);
    }
    if (callback && options && options.timeout) {
        connObj.timeoutTimerId = $setTimeout(this, function () {
            try {
                if (taskId) {
                    dorado.util.TaskIndicator.hideTaskIndicator(taskId);
                }
                var result = dorado.util.AjaxResult(options);
                result._init(connObj);
                result._setError(new dorado.util.AjaxException("Async Request Timeout.", null, connObj));
                $callback(callback, false, result, {scope:this});
                var eventArg = {async:true, result:result};
                this.fireEvent("onConnect", this, eventArg);
                if (options == null || !options.isBatch) {
                    this.fireEvent("onRequest", this, eventArg);
                }
            }
            finally {
                this._connectionPool.returnObject(connObj);
            }
        }, options.timeout);
    }
    conn.onreadystatechange = $scopify(this, function () {
        if (conn.readyState == 4) {
            try {
                if (taskId) {
                    dorado.util.TaskIndicator.hideTaskIndicator(taskId);
                }
                if (callback && options && options.timeout) {
                    clearTimeout(connObj.timeoutTimerId);
                }
                var result = new dorado.util.AjaxResult(options, connObj);
                var eventArg = {async:true, result:result};
                this.fireEvent("onConnect", this, eventArg);
                $callback(callback, result.success, result, {scope:this});
                if (options == null || !options.isBatch) {
                    this.fireEvent("onRequest", this, eventArg);
                }
            }
            finally {
                this._connectionPool.returnObject(connObj);
            }
        }
    });
    conn.send(this._getSendData(options));
}, _setHeader:function (connObj, options) {
    function setHeaders(conn, headers) {
        if (!headers) {
            return;
        }
        for (var prop in headers) {
            if (headers.hasOwnProperty(prop)) {
                var value = headers[prop];
                if (value != null) {
                    conn.setRequestHeader(prop, value);
                }
            }
        }
    }
    if (this._defaultOptions) {
        setHeaders(connObj.conn, this._defaultOptions.headers);
    }
    if (options) {
        setHeaders(connObj.conn, options.headers);
    }
}, _init:function (connObj, options, async) {
    function urlAppend(url, s) {
        if (s) {
            return url + (url.indexOf("?") === -1 ? "?" : "&") + encodeURI(s);
        }
        return url;
    }
    var url, method;
    if (options) {
        url = options.url;
        method = options.method;
        if (!options.headers) {
            options.headers = {};
        }
        if (options.xmlData) {
            options.headers["Content-Type"] = "text/xml";
            method = "POST";
        } else {
            if (options.jsonData) {
                options.headers["Content-Type"] = "text/javascript";
                method = "POST";
            }
        }
    }
    var defaultOptions = (this._defaultOptions) ? this._defaultOptions : {};
    url = url || defaultOptions.url;
    method = method || defaultOptions.method || "GET";
    var parameter = options.parameter;
    if (parameter && (method == "GET" || options.xmlData || options.jsonData)) {
        for (var p in parameter) {
            if (parameter.hasOwnProperty(p)) {
                url = urlAppend(url, parameter[p]);
            }
        }
    }
    connObj.url = url = $url(url);
    connObj.method = method;
    connObj.options = options;
    connObj.conn.open(method, url, async);
    this._setHeader(connObj, options);
}, _getSendData:function (options) {
    if (!options) {
        return null;
    }
    var data = null;
    if (options.xmlData) {
        data = options.xmlData;
    } else {
        if (options.jsonData) {
            data = dorado.JSON.stringify(options.jsonData, {replacer:function (key, value) {
                return (value instanceof Function) ? value.call(this) : value;
            }});
        } else {
            if (options.parameter) {
                var parameter = options.parameter;
                data = "";
                for (var p in parameter) {
                    if (parameter.hasOwnProperty(p)) {
                        data += (i > 0 ? "&" : "") + p + "=" + encodeURI(parameters[p]);
                    }
                }
            }
        }
    }
    return data;
}, requestSync:function (options, alwaysReturn) {
    if (typeof options == "string") {
        options = {url:options};
    }
    var connObj = this._connectionPool.borrowObject();
    try {
        var eventArg = {async:false, options:options};
        this.fireEvent("beforeRequest", this, eventArg);
        this.fireEvent("beforeConnect", this, eventArg);
        var error = null;
        try {
            this._init(connObj, options, false);
            connObj.conn.send(this._getSendData(options));
        }
        catch (e) {
            error = e;
        }
        var result = new dorado.util.AjaxResult(options);
        if (error != null) {
            result._init(connObj);
            result._setError(error);
        } else {
            result._init(connObj, true);
        }
        eventArg = {async:true, result:result};
        this.fireEvent("onConnect", this, eventArg);
        this.fireEvent("onRequest", this, eventArg);
        if (!alwaysReturn && error != null) {
            throw error;
        }
        return result;
    }
    finally {
        this._connectionPool.returnObject(connObj);
    }
}});
dorado.util.AjaxEngine._parseXml = function (xml) {
    var xmlDoc = null;
    try {
        if (dorado.Browser.msie) {
            var activeX = ["MSXML2.DOMDocument", "MSXML.DOMDocument"];
            for (var i = 0; i < activeX.length; ++i) {
                try {
                    xmlDoc = new ActiveXObject(activeX[i]);
                    break;
                }
                catch (e) {
                }
            }
            xmlDoc.loadXML(xml);
        } else {
            var parser = new DOMParser();
            xmlDoc = parser.parseFromString(xml, "text/xml");
        }
    }
    finally {
        return xmlDoc;
    }
};
dorado.util.AjaxException = $extend(Error, {$className:"dorado.util.AjaxException", constuctor:function (message, description, connObj) {
    this.message = message || "Unknown Error.";
    this.description = description;
    if (connObj == null) {
        return;
    }
    this.url = connObj.url;
    this.method = connObj.method;
    this.status = connObj.conn.status;
    this.statusText = connObj.conn.statusText;
}, toString:function () {
    var text = this.message;
    if (this.url) {
        text += "\nURL: " + this.url;
    }
    if (this.status) {
        text += "\nStatus: " + this.statusText + "(" + this.status + ")";
    }
    if (this.description) {
        text += "\n" + this.description;
    }
    return text;
}});
dorado.util.AjaxResult = $class({$className:"dorado.util.AjaxResult", constructor:function (options, connObj) {
    this.options = options;
    if (connObj != null) {
        this._init(connObj, true);
    }
}, success:true, _init:function (connObj, parseResponse) {
    this._connObj = connObj;
    this.url = connObj.url;
    this.method = connObj.method;
    var conn = connObj.conn;
    this.status = conn.status;
    this.statusText = conn.statusText;
    this.allResponseHeaders = conn.getAllResponseHeaders();
    if (parseResponse) {
        this.text = conn.responseText;
        if (conn.status < 200 || conn.status >= 400) {
            var error;
            if (conn.status == 12021) {
                error = this._parseError(conn.responseText, connObj);
            } else {
                error = new dorado.util.AjaxException("HTTP " + conn.status + " " + conn.statusText, null, connObj);
            }
            this._setError(error);
        }
    }
}, _setError:function (error) {
    this.success = false;
    this.error = error;
}, _parseError:function (text) {
    var json = dorado.JSON.parse(text, true);
    return new dorado.RemoteException(json.message, json.exceptionType, json.stackTrace);
}, getResponseHeaders:function () {
    var responseHeaders = this._responseHeaders;
    if (responseHeaders === undefined) {
        responseHeaders = {};
        this._responseHeaders = responseHeaders;
        try {
            var headerStr = this.allResponseHeaders;
            var headers = headerStr.split("\n");
            for (var i = 0; i < headers.length; i++) {
                var header = headers[i];
                var delimitPos = header.indexOf(":");
                if (delimitPos != -1) {
                    responseHeaders[header.substring(0, delimitPos)] = header.substring(delimitPos + 2);
                }
            }
        }
        catch (e) {
        }
    }
    return responseHeaders;
}, getXmlDocument:function () {
    var responseXML = this._responseXML;
    if (responseXML === undefined) {
        responseXML = dorado.util.AjaxEngine._parseXml(this.text);
        this._responseXML = responseXML;
    }
    return responseXML;
}, getJsonData:function (untrusty) {
    var jsonData = this._jsonData;
    if (jsonData === undefined) {
        this._jsonData = jsonData = dorado.JSON.parse(this.text, untrusty);
    }
    return jsonData;
}});
window.$ajax = new dorado.util.AjaxEngine();
(function () {
    var maxZIndex = 9999;
    window.$DomUtils = dorado.util.Dom = {getInvisibleContainer:function () {
        var id = "_dorado_invisible_div";
        var div = document.getElementById(id);
        if (!div) {
            div = this.xCreateElement({tagName:"DIV", id:id, style:{position:"absolute", width:100, height:100, left:-200, top:-200, overflow:"hidden"}});
            document.body.appendChild(div);
        }
        return div;
    }, getUndisplayContainer:function () {
        var id = "_dorado_undisplay_div";
        var div = document.getElementById(id);
        if (!div) {
            div = this.xCreateElement({tagName:"DIV", id:id, style:{visibility:"hidden", display:"none"}});
            document.body.appendChild(div);
        }
        return div;
    }, getOwnerWindow:function (node) {
        return dorado.Browser.msie ? node.ownerDocument.parentWindow : node.ownerDocument.defaultView;
    }, isOwnerOf:function (node, owner) {
        while (true) {
            node = node.parentNode;
            if (node == null) {
                return false;
            }
            if (node == owner) {
                return true;
            }
        }
    }, findParent:function (node, fn, includeSelf) {
        if (includeSelf !== false) {
            if (fn(node)) {
                return node;
            }
        }
        while (true) {
            node = node.parentNode;
            if (!node) {
                break;
            }
            if (fn(node)) {
                return node;
            }
        }
        return null;
    }, xCreateElement:function (template, arg, context) {
        function setAttrs(el, attrs, jqEl) {
            for (var attrName in attrs) {
                var attrValue = attrs[attrName];
                switch (attrName) {
                  case "style":
                    if (attrValue.constructor == String) {
                        $fly(el).attr("style", attrValue);
                    } else {
                        for (var styleName in attrValue) {
                            var v = attrValue[styleName];
                            if (styleName.match(/^width$|^height$|^top$|^left$|^right$|^bottom$/)) {
                                if (isFinite(v)) {
                                    v += "px";
                                }
                            }
                            el.style[styleName] = v;
                        }
                    }
                    break;
                  case "outerWidth":
                    jqEl.outerWidth(attrValue);
                    break;
                  case "outerHeight":
                    jqEl.outerHeight(attrValue);
                    break;
                  case "tagName":
                  case "content":
                    continue;
                  case "contextKey":
                    if (context instanceof Object && attrValue && typeof attrValue == "string") {
                        context[attrValue] = el;
                    }
                    continue;
                  default:
                    if (attrName.substr(0, 2) == "on") {
                        var event = attrName.substr(2);
                        if (!(attrValue instanceof Function)) {
                            attrValue = new Function(attrValue);
                        }
                        jqEl.bind(event, attrValue);
                    } else {
                        el[attrName] = attrValue;
                    }
                }
            }
            return el;
        }
        function setText(el, content, jqEl) {
            var isHtml = /(<\S[^><]*>)|(&.+;)/g;
            if (content.match(isHtml) != null && el.tagName.toUpperCase() != "TEXTAREA") {
                el.innerHTML = content;
            } else {
                el.appendChild(document.createTextNode(content));
            }
            return el;
        }
        function appendChild(parentEl, el) {
            if (parentEl.nodeName.toUpperCase() == "TABLE" && el.nodeName.toUpperCase() == "TR") {
                var tbody;
                if (parentEl && parentEl.tBodies[0]) {
                    tbody = parentEl.tBodies[0];
                } else {
                    tbody = parentEl.appendChild(document.createElement("tbody"));
                }
                parentEl = tbody;
            }
            parentEl.appendChild(el);
        }
        if (template instanceof Function) {
            template = template(arg || window);
        }
        if (template instanceof Array) {
            var elements = [];
            for (var i = 0; i < template.length; i++) {
                elements.push(this.xCreateElement(template[i], arg, context));
            }
            return elements;
        }
        var tagName = template.tagName || "DIV";
        tagName = tagName.toUpperCase();
        var content = template.content;
        var el;
        if (dorado.Core.msie && tagName == "INPUT" && template.type) {
            el = document.createElement("<" + tagName + " type=\"" + template.type + "\"/>");
        } else {
            el = document.createElement(tagName);
        }
        var jqEl = jQuery(el);
        el = setAttrs(el, template, jqEl);
        if (content != null) {
            if (content.constructor == String) {
                if (content.charAt(0) == "^") {
                    appendChild(el, document.createElement(content.substring(1)));
                } else {
                    el = setText(el, content, jqEl);
                }
            } else {
                if (content instanceof Array) {
                    for (var i = 0; i < content.length; i++) {
                        var c = content[i];
                        if (c.constructor == String) {
                            if (c.charAt(0) == "^") {
                                appendChild(el, document.createElement(c.substring(1)));
                            } else {
                                appendChild(el, document.createTextNode(c));
                            }
                        } else {
                            appendChild(el, this.xCreateElement(c, arg, context));
                        }
                    }
                } else {
                    if (content.nodeType) {
                        appendChild(el, content);
                    } else {
                        appendChild(el, this.xCreateElement(content, arg, context));
                    }
                }
            }
        }
        return el;
    }, BLANK_IMG:dorado.Setting["common.contextPath"] + "dorado/resources/blank.gif", setImgSrc:function (img, src) {
        src = $url(src) || BLANK_IMG;
        if (img.src != src) {
            img.src = src;
        }
    }, setBackgroundImage:function (el, url) {
        if (url) {
            var reg = /url\(.*\)/i, m = url.match(reg);
            if (m) {
                m = m[0];
                var realUrl = jQuery.trim(m.substring(4, m.length - 1));
                realUrl = $url(realUrl);
                el.style.background = url.replace(reg, "url(" + realUrl + ")");
                return;
            }
            url = $url(url);
            url = "url(" + url + ")";
        } else {
            url = "none";
        }
        if (el.style.backgroundImage != url) {
            el.style.backgroundImage = url;
        }
    }, placeCenterElement:function (element, container) {
        var offset = $fly(container).offset();
        element.style.left = (offset.left + (container.offsetWidth - element.offsetWidth) / 2) + "px";
        element.style.top = (offset.top + (container.offsetHeight - element.offsetHeight) / 2) + "px";
    }, getOrCreateChild:function (parentNode, index, tagName, fn) {
        var child, refChild;
        if (index < parentNode.childNodes.length) {
            child = refChild = parentNode.childNodes[index];
            if (fn && fn(child) === false) {
                child = null;
            }
        }
        if (!child) {
            child = (tagName instanceof Function) ? tagName(index) : ((tagName.constructor == String) ? document.createElement(tagName) : this.xCreateElement(tagName));
            (refChild) ? parentNode.insertBefore(child, refChild) : parentNode.appendChild(child);
        }
        return child;
    }, removeChildrenFrom:function (parentNode, from, fn) {
        var toRemove = [];
        for (var i = parentNode.childNodes.length - 1; i >= from; i--) {
            var child = parentNode.childNodes[i];
            if (fn && fn(child) === false) {
                continue;
            }
            toRemove.push(child);
        }
        $fly(toRemove).remove();
    }, fixMsieYScrollBar:function (container) {
        var content = findValidContent(container);
        if (!content) {
            return;
        }
        container.style.overflowX = "auto";
        container.style.overflowY = (dorado.Browser.version < "7") ? "scroll" : "";
        var offsetWidth = content.offsetWidth;
        if (offsetWidth > container.clientWidth) {
            if (offsetWidth == $fly(container).width()) {
                $fly(content).outerWidth(container.clientWidth);
                if (content.offsetWidth > container.clientWidth) {
                    container.style.overflowX = "scroll";
                } else {
                    container.style.overflowX = "hidden";
                    if (content.offsetHeight > container.clientHeight) {
                        container.style.overflowY = "scroll";
                    }
                }
                content.style.width = "";
            }
        }
    }, fixMsieXScrollBar:function (container) {
        var content = findValidContent(container);
        if (!content) {
            return;
        }
        var $container = $fly(container);
        $container.height(content.offsetHeight + ((content.offsetWidth > container.clientWidth) ? ($container.height() - container.clientHeight) : 0));
    }, isDragging:function () {
        var currentDraggable = jQuery.ui.ddmanager.current;
        return (currentDraggable && currentDraggable._mouseStarted);
    }, getCellPosition:function (event) {
        var element = event.srcElement || event.target, row = -1, column = -1;
        while (element && element != element.ownerDocument.body) {
            var tagName = element.tagName.toLowerCase();
            if (tagName == "td") {
                row = element.parentNode.rowIndex;
                column = element.cellIndex;
                break;
            }
            element = element.parentNode;
        }
        if (element != element.ownerDocument.body) {
            return {"row":row, "column":column, "element":element};
        }
        return null;
    }, dockAround:function (element, fixedElement, options) {
        options = options || {};
        var align = options.align || "innerleft", vAlign = options.vAlign || "innertop", positionOffset = options.positionOffset, offsetLeft = positionOffset ? positionOffset[0] : 0, offsetTop = positionOffset ? positionOffset[1] : 0, autoAdjustPosition = options.autoAdjustPosition, handleOverflow = options.handleOverflow, docEl = jQuery(document), docWidth = docEl.width(), docHeight = docEl.height(), overflowTrigger = false, bodyOffset = $fly(element.ownerDocument.body).offset(), overflowHeight, adjustLeft, adjustTop;
        docWidth += bodyOffset.left;
        docHeight += bodyOffset.top;
        var rect, offset;
        var position = jQuery(fixedElement == window ? document.body : fixedElement).offset(), left = position.left, top = position.top, newAlign, vAlignPrefix;
        if (fixedElement) {
            offset = jQuery(fixedElement).offset();
            rect = getRect(fixedElement);
            if (align) {
                left = getLeft(rect, element, align);
                if (!(autoAdjustPosition === false)) {
                    if ((left + element.offsetWidth > docWidth) || (left < 0)) {
                        if (align != "center") {
                            if (align.indexOf("left") != -1) {
                                newAlign = align.replace("left", "right");
                            } else {
                                if (align.indexOf("right") != -1) {
                                    newAlign = align.replace("right", "left");
                                }
                            }
                            adjustLeft = getLeft(rect, element, newAlign);
                            if ((adjustLeft + element.offsetWidth > docWidth) || (adjustLeft < 0)) {
                                left = 0;
                            } else {
                                left = adjustLeft;
                                align = newAlign;
                            }
                        } else {
                            if (align == "center") {
                                if (left < 0) {
                                    overflowTrigger = true;
                                    left = 0;
                                }
                            }
                        }
                    }
                }
            }
            if (vAlign) {
                top = getTop(rect, element, vAlign);
                if (!(autoAdjustPosition === false)) {
                    if ((top + element.offsetHeight > docHeight) || (top < 0)) {
                        if (vAlign != "center") {
                            if (vAlign.indexOf("top") != -1) {
                                vAlign = vAlign.replace("top", "bottom");
                                vAlignPrefix = vAlign.replace("top", "");
                            } else {
                                if (vAlign.indexOf("bottom") != -1) {
                                    vAlign = vAlign.replace("bottom", "top");
                                    vAlignPrefix = vAlign.replace("bottom", "");
                                }
                            }
                            adjustTop = getTop(rect, element, vAlign);
                            if (adjustTop + element.offsetHeight > docHeight) {
                                overflowTrigger = true;
                                if (adjustTop < (docHeight / 2)) {
                                    top = adjustTop;
                                    overflowHeight = docHeight - top;
                                    vAlign = vAlignPrefix + "bottom";
                                } else {
                                    overflowHeight = element.offsetHeight + top;
                                    vAlign = vAlignPrefix + "top";
                                }
                            } else {
                                if (adjustTop < 0) {
                                    overflowTrigger = true;
                                    if (top > (docHeight / 2)) {
                                        top = 0;
                                        overflowHeight = element.offsetHeight + adjustTop;
                                        vAlign = vAlignPrefix + "top";
                                    } else {
                                        overflowHeight = docHeight - top;
                                        vAlign = vAlignPrefix + "bottom";
                                    }
                                } else {
                                    top = adjustTop;
                                }
                            }
                        } else {
                            if (vAlign == "center") {
                                if (top < 0) {
                                    overflowTrigger = true;
                                    top = 0;
                                    overflowHeight = docHeight;
                                }
                            }
                        }
                    }
                }
            }
        }
        options.align = align;
        options.vAlign = vAlign;
        if (!(handleOverflow === false) && overflowTrigger) {
            if (options.overflowHandler && typeof options.overflowHandler == "function") {
                options.overflowHandler.call(null, overflowHeight);
            }
        }
        var finalLeft = left + offsetLeft, finalTop = top + offsetTop;
        $fly(element).left(finalLeft).top(finalTop);
        return {left:finalLeft, top:finalTop, 0:finalLeft, 1:finalTop};
    }, locateIn:function (element, options) {
        options = options || {};
        var positionOffset = options.positionOffset, offsetLeft = positionOffset ? positionOffset[0] : 0, offsetTop = positionOffset ? positionOffset[1] : 0, handleOverflow = options.handleOverflow, parent = options.parent, docEl = jQuery(document), docWidth = docEl.width(), docHeight = docEl.height(), adjustLeft, adjustTop, overflowTrigger = false, overflowHeight, position = options.position, left = position ? position.left : 0, top = position ? position.top : 0, autoAdjustPosition = options.autoAdjustPosition;
        if (parent) {
            var parentPos = $fly(parent).offset();
            left += parentPos.left;
            top += parentPos.top;
        }
        if (!(autoAdjustPosition === false)) {
            if (top < 0) {
                top = 0;
            }
            if (left < 0) {
                left = 0;
            }
            if (left + element.offsetWidth > docWidth) {
                adjustLeft = left - element.offsetWidth;
                left = adjustLeft > 0 ? adjustLeft : 0;
            }
            if (top + element.offsetHeight >= docHeight) {
                adjustTop = top - element.offsetHeight;
                if (adjustTop < 0) {
                    overflowTrigger = true;
                    top = 0;
                    overflowHeight = docHeight;
                } else {
                    top = adjustTop;
                }
            }
        }
        if (handleOverflow !== false && overflowTrigger) {
            if (options.overflowHandler && typeof options.overflowHandler == "function") {
                options.overflowHandler.call(null, overflowHeight);
            }
        }
        var finalLeft = left + offsetLeft, finalTop = top + offsetTop;
        $fly(element).left(finalLeft).top(finalTop);
        return {left:finalLeft, top:finalTop, 0:finalLeft, 1:finalTop};
    }, disableUserSelection:function (element) {
        if (dorado.Browser.msie) {
            $fly(element).bind("selectstart", onSelectStart);
        } else {
            element.style.MozUserSelect = "none";
            element.style.KhtmlUserSelect = "none";
            element.style.webkitUserSelect = "none";
            element.style.OUserSelect = "none";
            element.unselectable = "on";
        }
    }, enableUserSelection:function (element) {
        if (dorado.Browser.msie) {
            $fly(element).unbind("selectstart", onSelectStart);
        } else {
            element.style.MozUserSelect = "";
            element.style.KhtmlUserSelect = "";
            element.style.webkitUserSelect = "";
            element.style.OUserSelect = "";
            element.unselectable = "";
        }
    }, bringToFront:function (dom) {
        if (dorado.Browser.msie) {
            maxZIndex += 2;
        } else {
            maxZIndex += 1;
        }
        if (dom) {
            dom.style.zIndex = maxZIndex;
        }
        return maxZIndex;
    }};
    function onSelectStart() {
        return false;
    }
    function getRect(element) {
        if (element) {
            var width, height;
            if (element == window) {
                var $win = $fly(window), left = $win.scrollLeft(), top = $win.scrollTop();
                width = $win.width();
                height = $win.height();
                return {left:left, top:top, right:left + width, bottom:top + height};
            }
            var offset = $fly(element).offset();
            if (element == document.body) {
                width = $fly(window).width();
                height = $fly(window).height();
            } else {
                width = $fly(element).outerWidth();
                height = $fly(element).outerHeight();
            }
            return {left:offset.left, top:offset.top, right:offset.left + width, bottom:offset.top + height};
        }
        return null;
    }
    function getLeft(rect, dom, align) {
        switch (align.toLowerCase()) {
          case "left":
            return rect.left - dom.offsetWidth;
          case "innerleft":
            return rect.left;
          case "center":
            return (rect.left + rect.right - dom.offsetWidth) / 2;
          case "innerright":
            return rect.right - dom.offsetWidth;
          case "right":
          default:
            return rect.right;
        }
    }
    function getTop(rect, dom, vAlign) {
        switch (vAlign.toLowerCase()) {
          case "top":
            return rect.top - dom.offsetHeight;
          case "innertop":
            return rect.top;
          case "center":
            return (rect.top + rect.bottom - dom.offsetHeight) / 2;
          case "innerbottom":
            return rect.bottom - dom.offsetHeight;
          case "bottom":
          default:
            return rect.bottom;
        }
    }
    function findValidContent(container) {
        var childNodes = container.childNodes;
        for (var i = 0, j = childNodes.length; i < j; i++) {
            var child = childNodes[i];
            with (child.style) {
                if (display != "none" && (position == "" || position == "static")) {
                    return child;
                }
            }
        }
        return null;
    }
})();
dorado.util.ShadowPool = new dorado.util.ObjectPool({makeObject:function () {
    return $fly(document.body).prepend("<div class=\"d-shadow-ie\"></div>")[0].firstChild;
}});
dorado.util.Shadow = $class({$className:"dorado.util.Shadow", constructor:function (config) {
    dorado.Object.apply(this, config);
    if (!this.mode) {
        this.mode = this.defaultMode;
    }
    var offset = this.offset, adjusts = {h:0}, rad = Math.floor(this.offset / 2);
    switch (this.mode.toLowerCase()) {
      case "drop":
        adjusts.w = 0;
        adjusts.l = adjusts.t = offset;
        adjusts.t -= 1;
        if (dorado.Browser.msie) {
            adjusts.l -= this.offset + rad;
            adjusts.t -= this.offset + rad;
            adjusts.w -= rad;
            adjusts.h -= rad;
            adjusts.t += 1;
        }
        break;
      case "sides":
        adjusts.w = (offset * 2);
        adjusts.l = -offset;
        adjusts.t = offset - 1;
        if (dorado.Browser.msie) {
            adjusts.l -= (this.offset - rad);
            adjusts.t -= this.offset + rad;
            adjusts.l += 1;
            adjusts.w -= (this.offset - rad) * 2;
            adjusts.w -= rad + 1;
            adjusts.h -= 1;
        }
        break;
      case "frame":
        adjusts.w = adjusts.h = (offset * 2);
        adjusts.l = adjusts.t = -offset;
        adjusts.t += 1;
        adjusts.h -= 2;
        if (dorado.Browser.msie) {
            adjusts.l -= (this.offset - rad);
            adjusts.t -= (this.offset - rad);
            adjusts.l += 1;
            adjusts.w -= (this.offset + rad + 1);
            adjusts.h -= (this.offset + rad);
            adjusts.h += 1;
        }
        break;
    }
    this.adjusts = adjusts;
    this.target = config.target;
}, offset:(dorado.Browser.msie && dorado.Browser.version >= 9) ? 8 : 4, defaultMode:"drop", show:function () {
    var target = this.target, targetEl = jQuery(target), self = this;
    if (!self.dom) {
        self.dom = dorado.util.ShadowPool.borrowObject();
        if (self.dom.nextSibling != target) {
            targetEl.before(self.dom);
        }
    }
    if (targetEl.css("display") == "none") {
        self.dom.style.display = "none";
    } else {
        self.dom.style.zIndex = (parseInt(target.style.zIndex) || 0) - 1;
        var filter = "progid:DXImageTransform.Microsoft.alpha(opacity=50) progid:DXImageTransform.Microsoft.Blur(pixelradius=" + (this.offset) + ")";
        self.dom.style.filter = filter;
        var position = targetEl.offset();
        self.realign(position.left, position.top, targetEl.outerWidth(), targetEl.outerHeight());
        self.dom.style.display = "block";
    }
}, realign:function (left, top, width, height) {
    if (!this.dom) {
        return;
    }
    var adjusts = this.adjusts, dom = this.dom, style = dom.style;
    style.left = (left + adjusts.l) + "px";
    style.top = (top + adjusts.t) + "px";
    var sw = (width + adjusts.w), sh = (height + adjusts.h), sws = sw + "px", shs = sh + "px";
    if (style.width != sws || style.height != shs) {
        style.width = sws;
        style.height = shs;
    }
}, hide:function () {
    if (this.dom) {
        this.dom.style.display = "none";
        dorado.util.ShadowPool.returnObject(this.dom);
        delete this.dom;
    }
}, disable:function () {
    this.disabled = true;
    if (this.dom) {
        this.dom.style.display = "none";
    }
}, enable:function () {
    this.disabled = false;
    if (this.dom) {
        this.show();
    }
}});
jQuery.fn.shadow = function (options) {
    var element;
    if (this.length == 1) {
        element = this[0];
        if (dorado.Browser.msie) {
            if (element.offsetWidth) {
                var shadow = jQuery.data(element, "blockShadow");
                if (!shadow) {
                    options.target = element;
                    shadow = new dorado.util.Shadow(options);
                    jQuery.data(element, "blockShadow", shadow);
                    element.onresize = function () {
                        var disabled = jQuery.data(element, "blockShadowDisabled");
                        if (!disabled) {
                            setTimeout(function () {
                                shadow.show();
                            }, 0);
                        }
                    };
                    element.onpropertychange = function () {
                        var disabled = jQuery.data(element, "blockShadowDisabled");
                        if (!disabled) {
                            var property = event.propertyName;
                            if (property == "style.visibility" || property == "style.display") {
                                shadow.dom.style.visibility = element.style.visibility;
                                shadow.dom.style.display = element.style.display == "none" ? "none" : "block";
                            } else {
                                if (property == "style.left" || property == "style.top") {
                                    shadow.show();
                                }
                            }
                        }
                    };
                }
                shadow.show();
            }
        } else {
            options = options || {};
            var mode = options.mode || "drop";
            switch (mode.toLowerCase()) {
              case "drop":
                this.addClass("d-shadow-drop");
                break;
              case "sides":
                this.addClass("d-shadow-sides");
                break;
              case "frame":
                this.addClass("d-shadow-frame");
                break;
            }
        }
    }
    return this;
};
jQuery.fn.unshadow = function () {
    var element;
    if (this.length == 1) {
        element = this[0];
        if (dorado.Browser.msie) {
            var shadow = jQuery.data(element, "blockShadow");
            if (shadow != null) {
                element.onresize = null;
                element.onpropertychange = null;
                shadow.hide();
                jQuery.data(element, "blockShadow", null);
            }
        }
    }
    return this;
};
jQuery.fn.disableShadow = function () {
    var element;
    if (this.length == 1) {
        element = this[0];
        var blockShadow = jQuery.data(element, "blockShadow");
        if (blockShadow) {
            blockShadow.disable();
        }
        jQuery.data(element, "blockShadowDisabled", true);
    }
    return this;
};
jQuery.fn.enableShadow = function () {
    var element;
    if (this.length == 1) {
        element = this[0];
        var blockShadow = jQuery.data(element, "blockShadow");
        if (blockShadow) {
            blockShadow.enable();
        }
        jQuery.data(element, "blockShadowDisabled", false);
    }
    return this;
};
(function ($) {
    function num(el, prop) {
        return parseInt($.curCSS(el.jquery ? el[0] : el, prop, true)) || 0;
    }
    $.fn.bringToFront = function () {
        return this.css("zIndex", $DomUtils.bringToFront());
    };
    $.each(["left", "top", "right", "bottom"], function (i, name) {
        $.fn[name] = function (val) {
            return this.css(name, val);
        };
    });
    var oldPosition = $.fn.position;
    $.fn.position = function (left, top) {
        if (arguments.length) {
            this.css("left", left).css("top", top);
            return this;
        } else {
            return oldPosition.call(this);
        }
    };
    $.each(["Height", "Width"], function (i, name) {
        var tl = i ? "Left" : "Top";
        var br = i ? "Right" : "Bottom";
        var fn = $.fn["outer" + name];
        $.fn["outer" + name] = function (arg) {
            if (arg != null && arg.constructor != Boolean) {
                if (arg.constructor == String && arg.match("%")) {
                    this[name.toLowerCase()](arg);
                } else {
                    var n = parseInt(arg);
                    n = n - num(this, "padding" + tl) - num(this, "padding" + br) - num(this, "border" + tl + "Width") - num(this, "border" + br + "Width");
                    this[name.toLowerCase()](n);
                }
                return this;
            }
            return fn.call(this, arg);
        };
    });
    $.fn.addClassOnHover = function (cls, clsOwner, fn) {
        var clsOwner = clsOwner || this;
        this.hover(function () {
            if ($DomUtils.isDragging()) {
                return;
            }
            if (fn instanceof Function && !fn.call(this)) {
                return;
            }
            clsOwner.addClass(cls);
        }, function () {
            clsOwner.removeClass(cls);
        });
        return this;
    };
    $.fn.addClassOnFocus = function (cls, clsOwner, fn) {
        var clsOwner = clsOwner || this;
        this.focus(function () {
            if (fn instanceof Function && !fn.call(this)) {
                return;
            }
            clsOwner.addClass(cls);
        });
        this.blur(function () {
            clsOwner.removeClass(cls);
        });
        return this;
    };
    $.fn.addClassOnClick = function (cls, clsOwner, fn) {
        var clsOwner = clsOwner || this;
        this.mousedown(function () {
            if (fn instanceof Function && !fn.call(this)) {
                return;
            }
            clsOwner.addClass(cls);
            $(document).one("mouseup", function () {
                clsOwner.removeClass(cls);
            });
        });
        return this;
    };
    $.fn.repeatOnClick = function (fn, interval) {
        this.mousedown(function () {
            var timer;
            if (fn instanceof Function) {
                fn.apply(null, []);
                timer = setInterval(fn, interval || 100);
            }
            $(document).one("mouseup", function () {
                if (timer) {
                    clearInterval(timer);
                    timer = null;
                }
            });
        });
        return this;
    };
})(jQuery);
jQuery.fn.xCreate = function (template, arg, options) {
    var element = $DomUtils.xCreateElement(template, arg);
    if (element) {
        var insertBef = false, returnNewElements = false, refNode = null;
        var parentEl = this[0];
        if (options instanceof Object) {
            insertBef = options.insertBefore;
            refNode = (options.refNode) ? options.refNode : parentEl.firstChild;
            returnNewElements = options.returnNewElements;
        }
        var elements = (element instanceof Array) ? element : [element];
        for (var i = 0; i < elements.length; i++) {
            if (insertBef && refNode) {
                parentEl.insertBefore(elements[i], refNode);
            } else {
                parentEl.appendChild(elements[i]);
            }
        }
    }
    return returnNewElements ? jQuery(elements) : this;
};
(function () {
    jQuery.extend({speed:function (speed, easing, fn) {
        var opt = speed && typeof speed === "object" ? speed : {complete:fn || !fn && easing || jQuery.isFunction(speed) && speed, duration:speed, easing:fn && easing || easing && !jQuery.isFunction(easing) && easing};
        opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ? opt.duration : jQuery.fx.speeds[opt.duration] || jQuery.fx.speeds._default;
        opt.old = opt.complete;
        opt.complete = function () {
            if (jQuery.isFunction(opt.old)) {
                opt.old.call(this);
            }
            if (opt.queue !== false) {
                jQuery(this).dequeue();
            }
        };
        return opt;
    }});
    jQuery.fn.region = function () {
        var self = this, element = self[0];
        if (self.length == 1) {
            var position = self.offset(), width = element.offsetWidth, height = element.offsetHeight;
            return {top:position.top, right:position.left + width, left:position.left, bottom:position.top + height, height:height, width:width};
        }
    };
    jQuery.fn.innerRegion = function () {
        var el = this, element = el[0];
        if (el.length == 1) {
            var position = el.offset(), width = el.width(), height = el.height(), borderTop = parseInt(el.css("border-left-width"), 10) || 0, borderLeft = parseInt(el.css("border-top-width"), 10) || 0, paddingLeft = parseInt(el.css("padding-left"), 10) || 0, paddingTop = parseInt(el.css("padding-top"), 10) || 0;
            return {top:position.top + borderLeft + paddingTop, right:position.left + borderTop + paddingLeft + width, left:position.left + borderTop + paddingLeft, bottom:position.top + borderLeft + paddingTop + height, height:height, width:width};
        }
    };
    var propertyMap = {normal:["position", "visibility", "left", "right", "top", "bottom", "width", "height", "zIndex"], safe:["overflow", "position", "width", "height"], child:["position", "left", "right", "top", "bottom", "width", "height"]}, DOCKABLE_STYLE_RESTORE = "dockStyleRestore", DOCK_DATA = "dockData";
    var backupStyle = function (element, type) {
        var props = propertyMap[type || "normal"], object = {};
        if (props) {
            for (var i = 0, j = props.length; i < j; i++) {
                var prop = props[i];
                object[prop] = element.style[prop];
            }
        }
        jQuery.data(element, DOCKABLE_STYLE_RESTORE, object);
    };
    var ratioMap = {top:1, bottom:-1, left:1, right:-1}, dockStyleMap = {top:{horizontal:"left", vertical:"top", style:{left:0, top:0, right:"auto", bottom:"auto"}}, bottom:{horizontal:"left", vertical:"bottom", style:{left:0, top:"auto", right:"auto", bottom:0}}, left:{horizontal:"left", vertical:"top", style:{left:0, top:0, right:"auto", bottom:"auto"}}, right:{horizontal:"right", vertical:"top", style:{left:"auto", top:0, right:0, bottom:"auto"}}};
    jQuery.fn.dockable = function (direction, safe, showMask) {
        var self = this;
        if (self.length == 1) {
            direction = direction || "bottom";
            var element = self[0], absolute = (self.css("position") == "absolute"), leftStart = absolute ? parseInt(self.css("left"), 10) || 0 : 0, topStart = absolute ? parseInt(self.css("top"), 10) || 0 : 0;
            backupStyle(element, safe ? "safe" : "normal");
            self.css({visibility:"hidden", display:"block"});
            var dockConfig = dockStyleMap[direction], hori = dockConfig.horizontal, vert = dockConfig.vertical, rect = {width:self.outerWidth(), height:self.outerHeight()}, wrap, mask;
            if (safe) {
                var horiRatio = ratioMap[hori], vertRatio = ratioMap[vert], parentRegion = self.innerRegion(), child = element.firstChild, region, childStyle = {}, childEl;
                while (child) {
                    childEl = jQuery(child);
                    backupStyle(child, "child");
                    region = childEl.region();
                    childStyle[hori] = horiRatio * (region[hori] - parentRegion[hori]);
                    childStyle[vert] = vertRatio * (region[vert] - parentRegion[vert]);
                    childEl.css(childStyle).outerWidth(child.offsetWidth).outerHeight(child.offsetHeight);
                    child = child.nextSibling;
                }
                if (absolute) {
                    self.outerWidth(rect.width).outerHeight(rect.height).css({overflow:"hidden", visibility:""}).find("> *").css("position", "absolute");
                } else {
                    self.css({overflow:"hidden", position:"relative", visibility:""}).find("> *").css("position", "absolute");
                }
            } else {
                wrap = document.createElement("div");
                var wrapEl = jQuery(wrap);
                if (absolute) {
                    wrap.style.position = "absolute";
                    wrap.style.left = self.css("left");
                    wrap.style.top = self.css("top");
                    wrapEl.bringToFront();
                } else {
                    wrap.style.position = "relative";
                    element.style.position = "absolute";
                }
                wrap.style.overflow = "hidden";
                wrapEl.insertBefore(element);
                wrap.appendChild(element);
                var style = dockConfig.style;
                style.visibility = "";
                self.css(style).outerWidth(rect.width).outerHeight(rect.height);
            }
            if (showMask !== false) {
                mask = document.createElement("div");
                var maskEl = jQuery(mask);
                maskEl.css({position:"absolute", left:0, top:0, backgroundColor:"white", opacity:0}).bringToFront().outerWidth(rect.width).outerHeight(rect.height);
                if (safe) {
                    element.appendChild(mask);
                } else {
                    wrap.appendChild(mask);
                }
            }
            jQuery.data(element, DOCK_DATA, {rect:rect, mask:mask, wrap:wrap, leftStart:leftStart, topStart:topStart});
        }
        return this;
    };
    jQuery.fn.undockable = function (safe) {
        var self = this;
        if (self.length == 1) {
            var element = self[0], dockData = jQuery.data(element, DOCK_DATA);
            if (dockData == null) {
                return;
            }
            if (safe) {
                self.css(jQuery.data(element, DOCKABLE_STYLE_RESTORE)).find("> *").each(function (index, child) {
                    var style = jQuery.data(child, DOCKABLE_STYLE_RESTORE);
                    if (style != null) {
                        jQuery(child).css(style);
                    }
                    jQuery.data(child, DOCKABLE_STYLE_RESTORE, null);
                });
                jQuery(dockData.mask).remove();
            } else {
                var wrap = dockData.wrap;
                if (wrap) {
                    self.css(jQuery.data(element, DOCKABLE_STYLE_RESTORE)).insertAfter(wrap);
                    jQuery(wrap).remove();
                }
            }
            jQuery.data(element, DOCK_DATA, null);
            jQuery.data(element, DOCKABLE_STYLE_RESTORE, null);
        }
        return this;
    };
    var slideInDockDirMap = {l2r:"right", r2l:"left", t2b:"bottom", b2t:"top"}, slideOutDockDirMap = {l2r:"left", r2l:"right", t2b:"top", b2t:"bottom"}, slideSizeMap = {l2r:"height", r2l:"height", t2b:"width", b2t:"width"};
    var getAnimateConfig = function (type, direction, element, safe) {
        var dockData = jQuery.data(element, DOCK_DATA), rect = dockData.rect, leftStart = dockData.leftStart, topStart = dockData.topStart;
        if (safe) {
            if (type == "out") {
                switch (direction) {
                  case "t2b":
                    return {top:[topStart, topStart + rect.height], height:[rect.height, 0]};
                  case "r2l":
                    return {width:[rect.width, 0]};
                  case "b2t":
                    return {height:[rect.height, 0]};
                  case "l2r":
                    return {left:[leftStart, leftStart + rect.width], width:[rect.width, 0]};
                }
            } else {
                switch (direction) {
                  case "t2b":
                    return {height:[0, rect.height]};
                  case "l2r":
                    return {width:[0, rect.width]};
                  case "b2t":
                    return {top:[topStart + rect.height, topStart], height:[0, rect.height]};
                  case "r2l":
                    return {left:[leftStart + rect.width, leftStart], width:[0, rect.width]};
                }
            }
        } else {
            var property = slideSizeMap[direction];
            jQuery(dockData.wrap).css(property, dockData.rect[property]);
            if (type == "in") {
                switch (direction) {
                  case "t2b":
                    return {height:[0, rect.height]};
                  case "l2r":
                    return {width:[0, rect.width]};
                  case "b2t":
                    return {top:[topStart + rect.height, topStart], height:[0, rect.height]};
                  case "r2l":
                    return {left:[leftStart + rect.width, leftStart], width:[0, rect.width]};
                }
            } else {
                if (type == "out") {
                    switch (direction) {
                      case "t2b":
                        return {top:[topStart, topStart + rect.height], height:[rect.height, 0]};
                      case "r2l":
                        return {width:[rect.width, 0]};
                      case "b2t":
                        return {height:[rect.height, 0]};
                      case "l2r":
                        return {left:[leftStart, leftStart + rect.width], width:[rect.width, 0]};
                    }
                }
            }
        }
    };
    var slide = function (type, element, options, safe) {
        options = typeof options == "string" ? {direction:options} : options || {};
        var direction = options.direction || "t2b", callback = options.complete, step = options.step, start = options.start, animConfig, animElement = element, animEl, delayFunc, inited = false;
        delayFunc = function (direction) {
            $fly(element).disableShadow().dockable(type == "in" ? slideInDockDirMap[direction] : slideOutDockDirMap[direction], safe);
            animConfig = getAnimateConfig(type, direction, element, safe);
            animEl = jQuery(safe ? animElement : jQuery.data(element, DOCK_DATA).wrap);
            for (var prop in animConfig) {
                var value = animConfig[prop];
                animEl.css(prop, value[0]);
            }
            if (start) {
                start.call(element);
            }
            inited = true;
        };
        options.step = function (now, animate) {
            if (!inited) {
                delayFunc(direction);
            }
            var timestamp = new Date().getTime(), time = timestamp - animate.startTime, defaultEasing = animate.options.easing || (jQuery.easing.swing ? "swing" : "linear"), pos = jQuery.easing[defaultEasing](animate.state, time, 0, 1, animate.options.duration);
            var nowStyle = {};
            for (var prop in animConfig) {
                var range = animConfig[prop];
                nowStyle[prop] = range[0] + (range[1] - range[0]) * pos;
            }
            animEl.css(nowStyle);
            if (step) {
                step.call(animate.elem, nowStyle, animate);
            }
        };
        options.complete = function () {
            $fly(element).undockable(safe);
            $fly(element).enableShadow().css("display", type == "out" ? "none" : "");
            if (typeof callback == "function") {
                callback.apply(null, []);
            }
        };
        options.duration = options.duration ? options.duration : 300;
        $fly(element).animate({dummy:1}, options);
    };
    jQuery.fn.slideIn = function (options) {
        var self = this;
        if (self.length == 1) {
            slide("in", self[0], options, false);
        }
        return this;
    };
    jQuery.fn.slideOut = function (options) {
        var self = this;
        if (self.length == 1) {
            slide("out", self[0], options, false);
        }
        return this;
    };
    jQuery.fn.safeSlideIn = function (options) {
        var self = this;
        if (self.length == 1) {
            slide("in", self[0], options, true);
        }
        return this;
    };
    jQuery.fn.safeSlideOut = function (options) {
        var self = this;
        if (self.length == 1) {
            slide("out", self[0], options, true);
        }
        return this;
    };
    var zoomCoverPool = new dorado.util.ObjectPool({makeObject:function () {
        var cover = document.createElement("div");
        cover.className = "d-animate-zoom-proxy";
        jQuery(document.body).prepend(cover);
        return cover;
    }});
    var zoom = function (type, element, options) {
        var position = options.position, animTarget = options.animateTarget, startLeft, startTop, endLeft, endTop, offset, isTypeIn = (type != "out"), elWidth, elHeight;
        if (typeof animTarget == "string") {
            animTarget = jQuery(animTarget)[0];
        } else {
            if (animTarget instanceof dorado.widget.Control) {
                animTarget = animTarget._dom;
            }
        }
        var elementEl = jQuery(element), animTargetEl = jQuery(animTarget);
        if (type == "in") {
            if (animTarget) {
                offset = animTargetEl.offset();
                startTop = offset.top;
                startLeft = offset.left;
                endTop = position.top;
                endLeft = position.left;
            } else {
                offset = elementEl.offset();
                elWidth = elementEl.outerWidth();
                elHeight = elementEl.outerHeight();
                startTop = offset.top + elHeight / 2;
                startLeft = offset.left + elWidth / 2;
                endTop = position.top;
                endLeft = position.left;
            }
        } else {
            if (animTarget) {
                offset = animTargetEl.offset();
                if (!position) {
                    position = elementEl.offset();
                }
                startTop = position.top;
                startLeft = position.left;
                endTop = offset.top;
                endLeft = offset.left;
            } else {
                offset = elementEl.offset();
                elWidth = elementEl.outerWidth();
                elHeight = elementEl.outerHeight();
                startTop = offset.top;
                startLeft = offset.left;
                endTop = offset.top + elHeight / 2;
                endLeft = offset.left + elWidth / 2;
            }
        }
        var cover = zoomCoverPool.borrowObject();
        jQuery(cover).css({display:"", top:startTop, left:startLeft, width:isTypeIn ? 0 : elementEl.width(), height:isTypeIn ? 0 : elementEl.height()}).bringToFront().animate({top:endTop, left:endLeft, width:isTypeIn ? elementEl.width() : 0, height:isTypeIn ? elementEl.height() : 0}, {duration:options.animateDuration || 300, easing:options.animateEasing, complete:function () {
            cover.style.display = "none";
            zoomCoverPool.returnObject(cover);
            options.complete.apply(null, []);
        }});
    };
    jQuery.fn.zoomIn = function (options) {
        var self = this;
        if (self.length == 1) {
            zoom("in", self[0], options);
        }
        return this;
    };
    jQuery.fn.zoomOut = function (options) {
        var self = this;
        if (self.length == 1) {
            zoom("out", self[0], options);
        }
        return this;
    };
    var getWin = function (elem) {
        return (elem && ("scrollTo" in elem) && elem["document"]) ? elem : elem && elem.nodeType === 9 ? elem.defaultView || elem.parentWindow : elem === undefined ? window : false;
    }, SCROLL_TO = "scrollTo", DOCUMENT = "document";
    jQuery.fn.scrollIntoView = function (container, top, hscroll) {
        var self = this, elem;
        if (self.length == 1) {
            elem = self[0];
        }
        container = typeof container == "string" ? jQuery(container)[0] : container;
        hscroll = hscroll === undefined ? true : !!hscroll;
        top = top === undefined ? true : !!top;
        if (!container || container === window) {
            return elem.scrollIntoView(top);
        }
        if (container && container.nodeType == 9) {
            container = getWin(container);
        }
        var isWin = container && (SCROLL_TO in container) && container[DOCUMENT], elemOffset = self.offset(), containerOffset = isWin ? {left:jQuery(container).scrollLeft(), top:jQuery(container).scrollTop()} : jQuery(container).offset(), diff = {left:elemOffset["left"] - containerOffset["left"], top:elemOffset["top"] - containerOffset["top"]}, ch = isWin ? jQuery(window).height() : container.clientHeight, cw = isWin ? jQuery(window).width() : container.clientWidth, cl = jQuery(container).scrollLeft(), ct = jQuery(container).scrollTop(), cr = cl + cw, cb = ct + ch, eh = elem.offsetHeight, ew = elem.offsetWidth, l = diff.left + cl - (parseInt(jQuery(container).css("borderLeftWidth")) || 0), t = diff.top + ct - (parseInt(jQuery(container).css("borderTopWidth")) || 0), r = l + ew, b = t + eh, t2, l2;
        if (eh > ch || t < ct || top) {
            t2 = t;
        } else {
            if (b > cb) {
                t2 = b - ch;
            }
        }
        if (hscroll) {
            if (ew > cw || l < cl || top) {
                l2 = l;
            } else {
                if (r > cr) {
                    l2 = r - cw;
                }
            }
        }
        if (isWin) {
            if (t2 !== undefined || l2 !== undefined) {
                container[SCROLL_TO](l2, t2);
            }
        } else {
            if (t2 !== undefined) {
                container["scrollTop"] = t2;
            }
            if (l2 !== undefined) {
                container["scrollLeft"] = l2;
            }
        }
    };
})();
(function ($) {
    var oldDraggable = $.fn.draggable;
    $.fn.draggable = function (options) {
        var draggingInfo, doradoDraggable;
        if (options) {
            draggingInfo = options.draggingInfo;
            doradoDraggable = options.doradoDraggable;
        }
        if (draggingInfo || doradoDraggable) {
            var originOptions = options;
            options = dorado.Object.apply({}, originOptions);
            options.createDraggingInfo = function (evt) {
                var draggingInfo = originOptions.draggingInfo;
                if (draggingInfo instanceof Function) {
                    draggingInfo = draggingInfo.call(this, this, options);
                }
                if (!draggingInfo) {
                    if (doradoDraggable) {
                        draggingInfo = doradoDraggable.createDraggingInfo(this, options);
                    }
                    if (!draggingInfo) {
                        draggingInfo = new dorado.DraggingInfo();
                    }
                }
                if (draggingInfo) {
                    draggingInfo.set("element", this);
                }
                return draggingInfo;
            };
            if (typeof originOptions.revert != "string") {
                options.revert = function (dropped) {
                    var revert = originOptions.revert;
                    if (revert == null) {
                        revert = !dropped;
                    } else {
                        if (revert instanceof Function) {
                            revert = revert.call(this, dropped);
                        }
                    }
                    return revert;
                };
            }
            if (typeof originOptions.helper != "string") {
                options.helper = function (evt) {
                    var helper;
                    if (originOptions.helper instanceof Function) {
                        helper = originOptions.helper.apply(this, arguments);
                    }
                    if (doradoDraggable) {
                        helper = doradoDraggable.onGetDraggingIndicator(helper, evt, this);
                    }
                    var draggingInfo = options.createDraggingInfo.call(this, evt);
                    $fly(this).data("draggable").draggingInfo = draggingInfo;
                    if (helper instanceof dorado.DraggingIndicator) {
                        draggingInfo.set("indicator", helper);
                        helper = helper.getDom();
                    }
                    return helper;
                };
            }
            options.start = function (evt, ui) {
                var b = true;
                if (originOptions.start) {
                    b = originOptions.start.apply(this, arguments);
                }
                if (b !== false) {
                    var draggingInfo = dorado.DraggingInfo.getFromElement(this);
                    if (draggingInfo) {
                        draggingInfo._targetDroppables = [];
                        if (doradoDraggable) {
                            b = doradoDraggable.onDragStart(draggingInfo, evt);
                            if (b !== false) {
                                doradoDraggable.initDraggingInfo(draggingInfo, evt);
                                var indicator = draggingInfo.get("indicator");
                                if (indicator) {
                                    doradoDraggable.initDraggingIndicator(indicator, draggingInfo, evt);
                                }
                            }
                        }
                    }
                }
                return b;
            };
            options.stop = function (evt, ui) {
                var b = true;
                if (originOptions.stop) {
                    b = originOptions.stop.apply(this, arguments);
                }
                if (b !== false) {
                    var draggingInfo = dorado.DraggingInfo.getFromElement(this);
                    if (draggingInfo) {
                        if (doradoDraggable) {
                            b = doradoDraggable.onDragStop(draggingInfo, evt);
                        }
                        if (b !== false) {
                            setTimeout(function () {
                                var targetDroppable = draggingInfo._targetDroppables.peek();
                                if (targetDroppable) {
                                    targetDroppable.onDraggingSourceOut(draggingInfo, evt);
                                }
                            }, 20);
                        }
                    }
                }
                return b;
            };
            options.drag = function (evt, ui) {
                if (originOptions.drag) {
                    originOptions.drag.apply(this, arguments);
                }
                var draggingInfo = dorado.DraggingInfo.getFromElement(this);
                if (draggingInfo) {
                    if (doradoDraggable) {
                        doradoDraggable.onDragMove(draggingInfo, evt);
                    }
                    var targetDroppable = draggingInfo._targetDroppables.peek();
                    if (targetDroppable) {
                        targetDroppable.onDraggingSourceMove(draggingInfo, evt);
                    }
                }
            };
        }
        return oldDraggable.call(this, options);
    };
    var oldDroppable = $.fn.droppable;
    $.fn.droppable = function (options) {
        var doradoDroppable = options ? options.doradoDroppable : null;
        if (doradoDroppable) {
            var originOptions = options;
            options = dorado.Object.apply({}, originOptions);
            options.over = function (evt, ui) {
                if (originOptions.over) {
                    originOptions.over.apply(this, arguments);
                }
                if (doradoDroppable) {
                    var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
                    if (draggingInfo) {
                        if (draggingInfo._targetDroppables.peek() != doradoDroppable) {
                            draggingInfo._targetDroppables.push(doradoDroppable);
                        }
                        doradoDroppable.onDraggingSourceOver(draggingInfo, evt);
                    }
                }
            };
            options.out = function (evt, ui) {
                if (originOptions.out) {
                    originOptions.out.apply(this, arguments);
                }
                if (doradoDroppable) {
                    var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
                    if (draggingInfo) {
                        doradoDroppable.onDraggingSourceOut(draggingInfo, evt);
                        if (draggingInfo._targetDroppables.peek() == doradoDroppable) {
                            draggingInfo._targetDroppables.pop();
                        }
                    }
                }
            };
            options.drop = function (evt, ui) {
                var draggable = jQuery(ui.draggable).data("draggable");
                if (!jQuery.ui.ddmanager.accept) {
                    if (draggable && draggable.options.revert == "invalid") {
                        draggable.options.revert = true;
                        draggable.options.forceRevert = true;
                    }
                    return false;
                } else {
                    if (draggable && draggable.options.forceRevert) {
                        draggable.options.revert = "invalid";
                        draggable.options.forceRevert = false;
                    }
                    var dropped = false;
                    if (originOptions.drop) {
                        dropped = originOptions.drop.apply(this, arguments);
                    }
                    if (!dropped && doradoDroppable) {
                        var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
                        if (draggingInfo) {
                            setTimeout(function () {
                                doradoDroppable.onDraggingSourceDrop(draggingInfo, evt);
                            }, 20);
                        }
                    }
                    return true;
                }
            };
            options.accept = function (draggable) {
                var accept = originOptions.accept;
                if (accept) {
                    if (accept instanceof Function) {
                        accept = accept.apply(this, arguments);
                    } else {
                        accept = draggable.is(accept);
                    }
                }
                return !!accept;
            };
        }
        return oldDroppable.call(this, options);
    };
    if (dorado.Browser.chrome || dorado.Browser.safari) {
        jQuery.ui.draggable.prototype.options.userSelectFix = true;
        $.ui.plugin.add("draggable", "userSelectFix", {start:function (evt, ui) {
            $DomUtils.disableUserSelection(document.body);
        }, stop:function (evt, ui) {
            $DomUtils.enableUserSelection(document.body);
        }});
    }
})(jQuery);
dorado.ModalManager = {_controlStack:[], getMask:function () {
    var manager = dorado.ModalManager, maskDom = manager._dom;
    if (!maskDom) {
        maskDom = manager._dom = document.createElement("div");
        $fly(maskDom).addClass("d-modal-mask");
        $fly(document.body).append(maskDom);
    }
    manager.resizeMask();
    return maskDom;
}, resizeMask:function () {
    var manager = dorado.ModalManager, maskDom = manager._dom;
    if (maskDom) {
        var doc = maskDom.ownerDocument, bodyHeight = $fly(doc).height(), bodyWidth;
        if (dorado.Browser.msie) {
            if (dorado.Browser.version == 6) {
                bodyWidth = $fly(doc).width() - parseInt($fly(doc.body).css("margin-left"), 10) - parseInt($fly(doc.body).css("margin-right"), 10);
                $fly(maskDom).width(bodyWidth - 2).height(bodyHeight - 4);
            } else {
                if (dorado.Browser.version == 7) {
                    $fly(maskDom).height(bodyHeight);
                } else {
                    if (dorado.Browser.version == 8) {
                        $fly(maskDom).height(bodyHeight - 4);
                    }
                }
            }
        } else {
            $fly(maskDom).height(bodyHeight - 4);
        }
    }
}, show:function (dom, maskClass) {
    var manager = dorado.ModalManager, stack = manager._controlStack, maskDom = manager.getMask();
    if (dom) {
        maskClass = maskClass || "d-modal-mask";
        $fly(maskDom).css({display:""}).attr("className", maskClass).bringToFront();
        stack.push({dom:dom, maskClass:maskClass, zIndex:maskDom.style.zIndex});
        $fly(dom).bringToFront();
    }
}, hide:function (dom) {
    var manager = dorado.ModalManager, stack = manager._controlStack, maskDom = manager.getMask();
    if (dom) {
        if (stack.length > 0) {
            var target = stack[stack.length - 1];
            if (target && target.dom == dom) {
                stack.pop();
            } else {
                for (var i = 0, j = stack.length; i < j; i++) {
                    if (dom == (stack[i] || {}).dom) {
                        stack.removeAt(i);
                        break;
                    }
                }
            }
            if (stack.length == 0) {
                $fly(maskDom).css("display", "none");
            } else {
                target = stack[stack.length - 1];
                $fly(maskDom).css({zIndex:target.zIndex}).attr("className", target.className);
            }
        }
    }
}};
$fly(window).bind("resize", function () {
    if (dorado.ModalManager.onResizeTimerId) {
        clearTimeout(dorado.ModalManager.onResizeTimerId);
        delete dorado.ModalManager.onResizeTimerId;
    }
    dorado.ModalManager.onResizeTimerId = setTimeout(function () {
        delete dorado.ModalManager.onResizeTimerId;
    }, 200);
});
dorado.util.TaskIndicator = {idseed:0, _taskGroups:{}, registerTaskGroup:function (groupName, options) {
    var indicator = this, taskGroups = indicator._taskGroups;
    if (taskGroups[groupName]) {
    } else {
        options = options || {};
        taskGroups[groupName] = options;
    }
}, showTaskIndicator:function (taskInfo, groupName) {
    var indicator = this, taskGroups = indicator._taskGroups, taskGroupConfig;
    groupName = groupName || "daemon";
    taskGroupConfig = taskGroups[groupName];
    if (taskGroupConfig) {
        var groupPanel = taskGroupConfig.groupPanel;
        if (!groupPanel) {
            groupPanel = taskGroupConfig.groupPanel = new dorado.util.TaskGroupPanel(taskGroupConfig);
        }
        var taskId = groupName + "@" + ++indicator.idseed;
        groupPanel.show();
        groupPanel.addTask(taskInfo, taskId);
        return taskId;
    } else {
        return null;
    }
}, hideTaskIndicator:function (taskId) {
    var indicator = this, taskGroups = indicator._taskGroups, taskGroupName, taskGroupConfig;
    taskGroupName = taskId.substring(0, taskId.indexOf("@"));
    taskGroupConfig = taskGroups[taskGroupName];
    if (taskGroupConfig) {
        var groupPanel = taskGroupConfig.groupPanel;
        if (groupPanel) {
            groupPanel.removeTask(taskId);
        }
    } else {
    }
}};
dorado.util.TaskIndicator.registerTaskGroup("main", {showOptions:{align:"center", vAlign:"center"}, modal:true, className:"main-task-indicator"});
dorado.util.TaskIndicator.registerTaskGroup("daemon", {showOptions:{align:"innerright", vAlign:"innertop", positionOffset:[-15, 15]}, className:"daemon-task-indicator"});
dorado.util.TaskGroupPanel = $extend(dorado.RenderableElement, {$className:"dorado.util.TaskGroupPanel", tasks:null, taskGroupConfig:null, _intervalId:null, ATTRIBUTES:{className:{defaultValue:"d-task-group-panel"}}, constructor:function (taskGroupConfig) {
    $invokeSuper.call(this, [{}]);
    var panel = this;
    if (!taskGroupConfig) {
        throw new Error("taskGrooupRequired");
    }
    panel.taskGroupConfig = taskGroupConfig;
    panel.tasks = new dorado.util.KeyedArray(function (object) {
        return object.taskId;
    });
}, createDom:function () {
    var panel = this, dom, doms = {}, taskGroupConfig = panel.taskGroupConfig;
    dom = $DomUtils.xCreateElement({tagName:"div", className:panel._className, content:[{tagName:"div", className:"panel-header-left", contextKey:"header", content:{tagName:"div", className:"panel-header-right"}}, {tagName:"div", className:"panel-body-left", contextKey:"body", content:{tagName:"div", className:"panel-body-right", content:[{tagName:"div", className:"panel-body", contextKey:"body", content:{tagName:"div", className:"content-panel", contextKey:"contentPanel", content:[{tagName:"div", className:"count-info", contextKey:"countInfo"}, {tagName:"ul", className:"task-list", contextKey:"taskList"}]}}]}}, {tagName:"div", className:"panel-footer-left", contextKey:"footer", content:{tagName:"div", className:"panel-footer-right"}}]}, null, doms);
    panel._doms = doms;
    $fly(dom).addClass(taskGroupConfig.className).shadow({mode:"sides"});
    taskGroupConfig.caption = taskGroupConfig.caption ? taskGroupConfig.caption : $resource("dorado.core.DefaultTaskCountInfo");
    taskGroupConfig.executeTimeCaption = taskGroupConfig.executeTimeCaption ? taskGroupConfig.executeTimeCaption : $resource("dorado.core.DefaultTaskExecuteTime");
    return dom;
}, addTask:function (taskInfo, taskId) {
    var panel = this, taskGroupConfig = panel.taskGroupConfig, listDom = panel._doms.taskList, li = $DomUtils.xCreateElement({tagName:"li", className:"task-item", content:[{tagName:"span", className:"interval-span", content:taskGroupConfig.executeTimeCaption.replace("${taskExecuteTime}", "0")}, {tagName:"span", className:"caption-span", content:taskInfo}]});
    listDom.appendChild(li);
    if (panel.tasks.size == 0) {
        panel._intervalId = setInterval(function () {
            panel.refreshInterval();
        }, 500);
    }
    panel.tasks.append({taskId:taskId, dom:li, startTime:new Date().getTime()});
    $fly(panel._doms.countInfo).text(taskGroupConfig.caption.replace("${taskNum}", panel.tasks.size));
}, removeTask:function (taskId) {
    var panel = this, target = panel.tasks.get(taskId), taskGroupConfig = panel.taskGroupConfig;
    if (target) {
        setTimeout(function () {
            $fly(target.dom).remove();
            panel.tasks.remove(target);
            if (panel.tasks.size == 0) {
                clearInterval(panel._intervalId);
                panel._intervalId = null;
                panel.hide();
            }
            $fly(panel._doms.countInfo).text(taskGroupConfig.caption.replace("${taskNum}", panel.tasks.size));
        }, 500);
    }
}, refreshInterval:function () {
    var panel = this, time = new Date().getTime();
    panel.tasks.each(function (task) {
        var el = task.dom, startTime = task.startTime;
        if (el && startTime) {
            var interval = parseInt((time - startTime) / 1000, 10);
            $fly(el).find(".interval-span").text(panel.taskGroupConfig.executeTimeCaption.replace("${taskExecuteTime}", interval));
        }
    });
}, show:function (options) {
    var panel = this, taskGroupConfig = panel.taskGroupConfig;
    options = options || taskGroupConfig.showOptions;
    if (panel._hideTimer) {
        clearTimeout(panel._hideTimer);
        panel._hideTimer = null;
        return;
    }
    if (!panel._rendered) {
        panel.render(document.body);
    } else {
        $fly(panel._dom).css("display", "");
    }
    if (taskGroupConfig.modal) {
        dorado.ModalManager.show(panel._dom);
    }
    $fly(panel._dom).bringToFront();
    $DomUtils.dockAround(panel._dom, document.body, options);
}, hide:function () {
    var panel = this;
    if (panel._rendered) {
        var taskGroupConfig = panel.taskGroupConfig;
        $fly(panel._dom).css("display", "none");
        if (taskGroupConfig.modal) {
            dorado.ModalManager.hide(panel._dom);
        }
    }
}});
dorado.DraggingIndicator = $extend(dorado.RenderableElement, {$className:"dorado.DraggingIndicator", ATTRIBUTES:{className:{defaultValue:"d-dragging-indicator"}, accept:{skipRefresh:true, setter:function (p, v) {
    if (this._accept != v) {
        this._accept = v;
        this.refresh();
    }
}}, icon:{}, iconClass:{}, contentOffsetLeft:{defaultValue:20}, contentOffsetTop:{defaultValue:20}, content:{writeOnly:true, setter:function (p, content) {
    if (content instanceof jQuery) {
        content = content[0];
    }
    this._content = content;
}}}, constructor:function (config) {
    $invokeSuper.call(this, arguments);
    if (config) {
        this.set(config);
    }
}, createDom:function () {
    var dom = $DomUtils.xCreateElement({tagName:"div", content:[{tagName:"div", className:"content-container"}, {tagName:"div"}]});
    this._contentContainer = dom.firstChild;
    this._iconDom = dom.lastChild;
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var contentContainer = this._contentContainer, $contentContainer = $fly(this._contentContainer), content = this._content;
    $contentContainer.toggleClass("default-content", (content == null)).left(this._contentOffsetLeft || 0).top(this._contentOffsetTop || 0);
    if (content) {
        if (content.parentNode != contentContainer) {
            $contentContainer.empty().append(content);
        }
    } else {
        $contentContainer.empty();
    }
    var w = contentContainer.offsetWidth + (this._contentOffsetLeft || 0);
    var h = contentContainer.offsetHeight + (this._contentOffsetTop || 0);
    $fly(dom).width(w).height(h);
    var iconDom = this._iconDom;
    $fly(iconDom).attr("class", "icon");
    var icon = this._icon, iconClass = this._iconClass;
    if (!icon && !iconClass) {
        iconClass = this._accept ? "accept-icon" : "denied-icon";
    }
    if (icon) {
        $DomUtils.setBackgroundImage(iconDom, icon);
    } else {
        if (iconClass) {
            $fly(iconDom).addClass(iconClass);
        }
    }
}});
dorado.DraggingIndicator.create = function () {
    return new dorado.DraggingIndicator();
};
(function () {
    dorado.DraggingInfo = $extend(dorado.AttributeSupport, {$className:"dorado.DraggingInfo", ATTRIBUTES:{object:{setter:function (p, object) {
        this._object = object;
        this._insertMode = null;
        this._refObject = null;
    }}, element:{}, tags:{}, sourceControl:{}, targetObject:{}, targetControl:{}, insertMode:{}, refObject:{}, accept:{setter:function (p, accept) {
        if (this._indicator) {
            this._indicator.set("accept", accept);
        }
        jQuery.ui.ddmanager.accept = accept;
    }}, indicator:{}, options:{}}, constructor:function (options) {
        if (options) {
            this.set(options);
        }
        if (!this._tags) {
            this._tags = [];
        }
    }, isDropAcceptable:function (droppableTags) {
        if (droppableTags && droppableTags.length && this._tags.length) {
            for (var i = 0; i < droppableTags.length; i++) {
                if (this._tags.indexOf(droppableTags[i]) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }});
    dorado.DraggingInfo.getFromJQueryUI = function (ui) {
        return $fly(ui.draggable[0]).data("draggable").draggingInfo;
    };
    dorado.DraggingInfo.getFromElement = function (element) {
        element = (element instanceof jQuery) ? element : $fly(element);
        return element.data("draggable").draggingInfo;
    };
    dorado.Draggable = $class({$className:"dorado.Draggable", defaultDraggableOptions:{distance:5, revert:"invalid", cursorAt:{left:8, top:8}}, ATTRIBUTES:{draggable:{}, dragTags:{skipRefresh:true, setter:function (p, v) {
        if (typeof v == "string") {
            v = v.split(",");
        }
        this._dragTags = v || [];
    }}}, EVENTS:{onGetDraggingIndicator:{}, onDragStart:{}, onDragStop:{}, onDragMove:{}}, getDraggableOptions:function (dom) {
        var options = dorado.Object.apply({doradoDraggable:this}, this.defaultDraggableOptions);
        return options;
    }, applyDraggable:function (dom, options) {
        if (dom._currentDraggable !== this._draggable) {
            if (this._draggable) {
                options = options || this.getDraggableOptions(dom);
                $fly(dom).draggable(options);
            } else {
                $fly(dom).draggable("destroy");
            }
            dom._currentDraggable = this._draggable;
        }
    }, createDraggingInfo:function (dom, options) {
        var info = new dorado.DraggingInfo({sourceControl:this, options:options, tags:this._dragTags});
        return info;
    }, initDraggingInfo:function (draggingInfo, evt) {
    }, initDraggingIndicator:function (indicator, draggingInfo, evt) {
    }, onGetDraggingIndicator:function (indicator, evt, draggableElement) {
        if (!indicator) {
            indicator = dorado.DraggingIndicator.create();
        }
        var eventArg = {indicator:indicator, event:evt, draggableElement:draggableElement};
        this.fireEvent("onGetDraggingIndicator", this, eventArg);
        indicator = eventArg.indicator;
        if (indicator instanceof dorado.DraggingIndicator) {
            if (!indicator.get("rendered")) {
                indicator.render();
            }
            var dom = indicator.getDom();
            $fly(dom).bringToFront();
        }
        return indicator;
    }, onDragStart:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt, processDefault:true};
        this.fireEvent("onDragStart", this, eventArg);
        return eventArg.processDefault;
    }, onDragStop:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt, processDefault:true};
        this.fireEvent("onDragStop", this, eventArg);
        return eventArg.processDefault;
    }, onDragMove:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt};
        this.fireEvent("onDragMove", this, eventArg);
    }});
    dorado.Droppable = $class({$className:"dorado.Droppable", defaultDroppableOptions:{accept:"*", tolerance:"pointer"}, ATTRIBUTES:{droppable:{}, droppableTags:{skipRefresh:true, setter:function (p, v) {
        if (typeof v == "string") {
            v = v.split(",");
        }
        this._droppableTags = v || [];
    }}}, EVENTS:{onDraggingSourceOver:{}, onDraggingSourceOut:{}, onDraggingSourceMove:{}, onDraggingSourceDrop:{}}, getDroppableOptions:function (dom) {
        var options = dorado.Object.apply({doradoDroppable:this}, this.defaultDroppableOptions);
        return options;
    }, applyDroppable:function (dom, options) {
        if (dom._currentDroppable !== this._droppable) {
            if (this._droppable) {
                options = options || this.getDroppableOptions(dom);
                $fly(dom).droppable(options);
            } else {
                $fly(dom).droppable("destroy");
            }
            dom._currentDroppable = this._droppable;
        }
    }, onDraggingSourceOver:function (draggingInfo, evt) {
        var accept = draggingInfo.isDropAcceptable(this._droppableTags);
        var eventArg = {draggingInfo:draggingInfo, event:evt, accept:accept};
        this.fireEvent("onDraggingSourceOver", this, eventArg);
        draggingInfo.set("accept", eventArg.accept);
        return eventArg.accept;
    }, onDraggingSourceOut:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt};
        this.fireEvent("onDraggingSourceOut", this, eventArg);
        draggingInfo.set({targetObject:null, insertMode:null, refObject:null, accept:false});
    }, onDraggingSourceMove:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt};
        this.fireEvent("onDraggingSourceMove", this, eventArg);
    }, onDraggingSourceDrop:function (draggingInfo, evt) {
        var eventArg = {draggingInfo:draggingInfo, event:evt};
        this.fireEvent("onDraggingSourceDrop", this, eventArg);
    }, getMousePosition:function (evt) {
        var offset = $fly(this.getDom()).offset();
        return {x:evt.pageX - offset.left, y:evt.pageY - offset.top};
    }});
})();

