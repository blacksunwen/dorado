
(function () {
    var LOG_LEVELS = {debug:10, info:9, warn:8, error:7, 10:"debug", 9:"info", 8:"warn", 7:"error"};
    var ENTITY_STATE_MAP = {0:"none", 1:"new", 2:"modified", 3:"deleted"};
    var format2HTML = function (text) {
        var reg1 = new RegExp("&", "g"), pattern = new RegExp("[<>\"\n\t]", "g"), map = {"<":"&lt;", ">":"&gt;", "\n":"<br/>", "\t":"&nbsp;&nbsp;", "\"":"&quot;"};
        return text.replace(reg1, "&amp;").replace(pattern, function (string) {
            return map[string];
        });
    };
    dorado.widget.Logger = $extend(dorado.widget.Control, {$className:"dorado.widget.Logger", ATTRIBUTES:{className:{defaultValue:"d-logger"}, lockScrollBar:{}, level:{defaultValue:"debug", setter:function (attr, value) {
        var logger = this, oldValue = logger._level, dom = logger._dom, i;
        value = value || "debug";
        if (oldValue && dom) {
            if (LOG_LEVELS[oldValue] > LOG_LEVELS[value]) {
                for (i = LOG_LEVELS[value] + 1; i <= LOG_LEVELS[oldValue]; i++) {
                    $fly(dom).find(".log-" + LOG_LEVELS[i]).css("display", "none");
                }
            } else {
                if (LOG_LEVELS[oldValue] < LOG_LEVELS[value]) {
                    for (i = LOG_LEVELS[oldValue] + 1; i <= LOG_LEVELS[value]; i++) {
                        $fly(dom).find(".log-" + LOG_LEVELS[i]).css("display", "");
                    }
                }
            }
            if (!logger._lockScrollBar) {
                logger.scrollToEnd();
            }
        }
        this._level = value;
    }}}, clear:function () {
        var logger = this, logs = logger._logs;
        if (logs) {
            logs = [];
            var dom = logger._dom;
            if (dom) {
                dom.innerHTML = "";
            }
        }
    }, log:function (msg, level) {
        var logger = this, logs = logger._logs;
        level = level in LOG_LEVELS ? level : "debug";
        msg = msg ? msg : "";
        if (!logs) {
            logs = logger._logs = [];
        }
        msg = format2HTML(msg);
        logs.push(level + ":" + msg);
        if (logger._rendered) {
            var dom = logger._dom, logDom = $DomUtils.xCreateElement({tagName:"div", className:"log log-" + level, style:{display:LOG_LEVELS[level] > LOG_LEVELS[logger._level] ? "none" : ""}, content:[{tagName:"div", className:"icon"}, {tagName:"div", className:"msg", content:msg}]});
            dom.appendChild(logDom);
            if (dom.style.display != "none" && !logger._lockScrollBar) {
                $fly(logDom).scrollIntoView(dom);
            }
            return logDom;
        }
    }, dir:function (msg, level) {
        var buffer = "{\n";
        for (var prop in msg) {
            buffer += "\t" + prop + ": " + msg[prop] + "\n";
        }
        buffer += "}\n";
        this.log(buffer, level);
    }, scrollToEnd:function () {
        var logger = this, dom = logger._dom;
        if (dom && dom.style.display != "none") {
            var scrollHeight = dom.scrollHeight, offsetHeight = dom.offsetHeight;
            dom.scrollTop = scrollHeight - offsetHeight;
        }
    }, createDom:function () {
        var logger = this, dom = $DomUtils.xCreateElement({tagName:"div"}), logs = logger._logs, logDom;
        if (logs) {
            for (var i = 0, j = logs.length; i < j; i++) {
                var log = logs[i], semicolonIndex = log.indexOf(":"), level = log.substring(0, semicolonIndex), msg = log.substr(semicolonIndex + 1);
                logDom = $DomUtils.xCreateElement({tagName:"div", className:"log log-" + level, style:{display:LOG_LEVELS[level] > LOG_LEVELS[logger._level] ? "none" : ""}, content:[{tagName:"div", className:"icon"}, {tagName:"div", className:"msg", content:msg}]});
                dom.appendChild(logDom);
            }
            if (!logger._lockScrollBar) {
                logger.scrollToEnd();
            }
        }
        return dom;
    }, refreshDom:function (dom) {
        var logger = this;
        $invokeSuper.call(this, arguments);
        if (!logger._lockScrollBar) {
            logger.scrollToEnd();
        }
    }});
    var DEBUGGER_CODE_AREA_ID = "dorado.Debugger.runCodeTextArea", DEBUGGER_LOGGER_ID = "dorado.Debugger.logger", DEBUGGER_SHOW_ON_VISIBLE_KEY = "dorado.Debugger.showOnVisible", DEBUGGER_TABCONTROL = "dorado.Debugger.tabControl", DEBUGGER_DEBUG_BUTTON = "dorado.Debugger.debugButton", DEBUGGER_INFO_BUTTON = "dorado.Debugger.infoButton", DEBUGGER_WARN_BUTTON = "dorado.Debugger.warnButton", DEBUGGER_ERROR_BUTTON = "dorado.Debugger.errorButton";
    function setCookie(name, value, expire) {
        var exp = new Date();
        exp.setTime(exp.getTime() + expire);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }
    function getCookie(name) {
        var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
        if (arr != null) {
            return unescape(arr[2]);
        }
        return null;
    }
    function delCookie(name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null) {
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
        }
    }
    dorado.Debugger = {inited:false, trapError:true, init:function () {
        function isCollection(object) {
            return object instanceof Array || object instanceof dorado.util.KeyedArray || object instanceof dorado.util.KeyedList || object instanceof dorado.EntityList || object instanceof dorado.Entity;
        }
        function isCollectionHasElement(object) {
            var result = false, firstObject;
            if (object instanceof Array) {
                firstObject = object[0];
            } else {
                if (object instanceof dorado.util.KeyedArray) {
                    firstObject = object.get(0);
                } else {
                    if (object instanceof dorado.util.KeyedList) {
                        firstObject = object.getFirst();
                    } else {
                        if (object instanceof dorado.EntityList) {
                            firstObject = object.first();
                        } else {
                            if (object instanceof dorado.Entity) {
                                var data = object.getData(), prop, value, hasChild = false;
                                for (prop in data) {
                                    try {
                                        value = object.get(prop);
                                    }
                                    catch (e) {
                                        dorado.Exception.removeException(e);
                                        value = data[prop];
                                    }
                                    if (value instanceof dorado.EntityList || value instanceof dorado.Entity) {
                                        hasChild = true;
                                    }
                                }
                                return hasChild;
                            }
                        }
                    }
                }
            }
            if (firstObject && (firstObject instanceof dorado.Entity || dorado.Object.isInstanceOf(firstObject, dorado.AttributeSupport))) {
                return true;
            }
            return result;
        }
        function isHasChild(object) {
            var prop, value;
            if (object && dorado.Object.isInstanceOf(object, dorado.AttributeSupport)) {
                for (prop in object.ATTRIBUTES) {
                    value = object["_" + prop];
                    if (isCollectionHasElement(value)) {
                        return true;
                    }
                }
            } else {
                for (prop in object) {
                    value = object[prop];
                    if (isCollectionHasElement(value)) {
                        return true;
                    }
                }
            }
            return false;
        }
        function getObjectChildren(object, onlyview) {
            var prop, value, nodes = [], temp = [];
            if (object && dorado.Object.isInstanceOf(object, dorado.AttributeSupport)) {
                for (prop in object.ATTRIBUTES) {
                    value = object["_" + prop];
                    if (isCollectionHasElement(value)) {
                        temp.push({label:prop, hasChild:true, data:{value:value}});
                    }
                }
                if (temp.length != 1) {
                    nodes = temp;
                } else {
                    return getCollectionChildren(temp[0].data.value, onlyview);
                }
            } else {
                for (prop in object) {
                    value = object[prop];
                    if (isCollectionHasElement(value)) {
                        nodes.push({label:prop, hasChild:true, data:{value:value}});
                    }
                }
            }
            return nodes;
        }
        function getCollectionChildren(object, onlyview) {
            var nodes = [];
            function doGetNodes(obj) {
                if (onlyview) {
                    if (obj._parent && obj._parent != $view) {
                        return;
                    }
                }
                var className = obj.constructor.className || "", $type = className.replace("dorado.widget.", "");
                nodes.push({label:$type || obj._id, hasChild:isHasChild(obj), data:{value:obj}});
            }
            var data, prop, value;
            if (object instanceof dorado.Entity) {
                data = object.getData();
                for (prop in data) {
                    try {
                        value = object.get(prop);
                    }
                    catch (e) {
                        dorado.Exception.removeException(e);
                        value = data[prop];
                    }
                    if (value instanceof dorado.EntityList || value instanceof dorado.Entity) {
                        nodes.push({label:prop, hasChild:true, data:{value:value}});
                    }
                }
            } else {
                if (object instanceof dorado.EntityList) {
                    object.each(function (entity) {
                        nodes.push({label:"Entity", hasChild:isCollectionHasElement(entity), data:{value:entity}});
                    });
                } else {
                    if (object && object.each) {
                        object.each(doGetNodes);
                    } else {
                        for (var i = 0; i < object.length; i++) {
                            doGetNodes(object[i]);
                        }
                    }
                }
            }
            return nodes;
        }
        function getTreeNodes(object, onlyview) {
            var nodes;
            if (isCollection(object)) {
                nodes = getCollectionChildren(object, onlyview);
            } else {
                nodes = getObjectChildren(object, onlyview);
            }
            return nodes;
        }
        function getTreeGridNodes(object) {
            var prop, value, hasChild, nodes = [];
            if (object instanceof dorado.Entity) {
                nodes.push({label:"State", hasChild:false, data:{label:ENTITY_STATE_MAP[object.state], value:ENTITY_STATE_MAP[object.state]}});
                var data = object.getData();
                if (data) {
                    for (prop in data) {
                        if (prop != "$dataType") {
                            value = data[prop];
                            nodes.push({label:prop, hasChild:false, data:{label:prop, value:value}});
                        }
                    }
                }
            } else {
                if (object && dorado.Object.isInstanceOf(object, dorado.AttributeSupport)) {
                    for (prop in object.ATTRIBUTES) {
                        if (prop in {"view":true, "parent":true}) {
                            continue;
                        }
                        value = object["_" + prop];
                        if (isCollection(value)) {
                            continue;
                        }
                        hasChild = value && typeof value == "object";
                        nodes.push({label:prop, hasChild:hasChild, data:{label:prop, value:value}});
                    }
                } else {
                    for (prop in object) {
                        if (prop in {"view":true, "parent":true}) {
                            continue;
                        }
                        value = object[prop];
                        if (typeof value == "function" || isCollection(value)) {
                            continue;
                        }
                        hasChild = value && typeof value == "object";
                        nodes.push({label:prop, hasChild:hasChild, data:{label:prop, value:value}});
                    }
                }
            }
            nodes.sort(function (a, b) {
                return a.label > b.label;
            });
            return nodes;
        }
        function getEventNodes(object) {
            var nodes = [], value, hasChild;
            if (object && object.EVENTS) {
                var events = object._events || {};
                for (var prop in object.EVENTS) {
                    value = events[prop];
                    hasChild = value && value.length > 0;
                    nodes.push({label:prop, hasChild:hasChild, data:{label:prop, value:value}});
                }
            } else {
                if (object && object instanceof Array) {
                    for (var i = 0; i < object.length; i++) {
                        value = object[i];
                        nodes.push({label:i, hasChild:false, data:{label:i, value:value}});
                    }
                }
            }
            return nodes;
        }
        var topView = $view, nodes = getTreeNodes(topView._children, true);
        var controlList = {$type:"Control", caption:"View", control:{$type:"SplitPanel", position:200, sideControl:{$type:"Panel", caption:"View", layoutConstraint:{type:"left", width:200, height:"100%"}, tools:[{$type:"SimpleIconButton", icon:"url(>skin>common/icons.gif) -60px -20px", listener:{onClick:function () {
            var controlTree = this.id("DEBUGGER_CONTROL_TREE"), current = controlTree.get("currentNode");
            if (!current) {
                dorado.MessageBox.alert($resource("dorado.baseWidget.DebuggerVariableNodeRequired"));
            } else {
                dorado.MessageBox.prompt($resource("dorado.baseWidget.DebuggerVariableExportInput"), function (text) {
                    if (text) {
                        window[text] = current.get("data.value");
                    } else {
                        dorado.MessageBox.alert($resource("dorado.baseWidget.DebuggerVariableNameRequired"));
                    }
                });
            }
        }}}, {$type:"SimpleIconButton", icon:"url(>skin>common/icons.gif) -40px -240px", listener:{onClick:function () {
            var topView = $view, nodes = getTreeNodes(topView._children, true);
            var controlTree = this.id("DEBUGGER_CONTROL_TREE");
            controlTree.disableAutoRefresh();
            controlTree._root.clearChildren();
            controlTree._root.addNodes(nodes);
            controlTree.enableAutoRefresh();
            controlTree.refresh();
        }}}], children:[{id:"DEBUGGER_CONTROL_TREE", expandingMode:"sync", $type:"Tree", nodes:nodes, style:"borderStyle:none", beforeExpand:function (self, arg) {
            var node = arg.node, value = node.get("data.value");
            if (node.get("userData") || !value) {
            } else {
                node.set("userData", true);
                node.addNodes(getTreeNodes(value, false));
            }
        }, onCurrentChange:function (self, arg) {
            var current = arg.newCurrent, attrGrid = this.id("DEBUGGER_ATTRIBUTES_GRID"), eventGrid = this.id("DEBUGGER_EVENTS_TREE");
            if (current) {
                attrGrid.disableAutoRefresh();
                attrGrid._root.clearChildren();
                attrGrid._root.addNodes(getTreeGridNodes(current.get("data.value")));
                attrGrid.enableAutoRefresh();
                attrGrid.refresh();
                eventGrid.disableAutoRefresh();
                eventGrid._root.clearChildren();
                eventGrid._root.addNodes(getEventNodes(current.get("data.value")));
                eventGrid.enableAutoRefresh();
                eventGrid.refresh();
            }
        }}]}, mainControl:{$type:"TabControl", tabs:[{$type:"Control", caption:"Attributes", control:{id:"DEBUGGER_ATTRIBUTES_GRID", $type:"TreeGrid", expandingMode:"sync", stretchColumnsMode:"lastColumn", columns:[{name:"name", property:"name", width:200}, {name:"value", property:"value"}], treeColumn:"name", beforeExpand:function (self, arg) {
            var node = arg.node, id = node.get("data.label"), value = node.get("data.value");
            if (node.get("userData") || !id) {
            } else {
                node.set("userData", true);
                node.addNodes(getTreeGridNodes(value));
            }
        }, onCellValueEdit:function (self, arg) {
            var entity = arg.entity;
            if (entity) {
                var name = entity.get("label"), value = entity.get("value");
                var tree = this.id("DEBUGGER_CONTROL_TREE"), current = tree.get("currentNode"), data = current.get("data.value");
                if (data && dorado.Object.isInstanceOf(data, dorado.AttributeSupport)) {
                    if (isFinite(value)) {
                        value = parseFloat(value);
                    } else {
                        if (["true", "false"].indexOf(value) >= 0) {
                            value = (value === "true") ? true : false;
                        }
                    }
                    data.set(name, value);
                }
            }
        }}}, {$type:"Control", caption:"Events", control:{$type:"Container", layout:{$type:"Dock", regionPadding:1}, style:"border:1px #B0B0B0 solid", children:[{id:"DEBUGGER_EVENTS_TREE", expandingMode:"sync", $type:"Tree", layoutConstraint:"right", width:200, beforeExpand:function (self, arg) {
            var node = arg.node, id = node.get("data.label"), value = node.get("data.value");
            if (node.get("userData") || !id) {
            } else {
                node.set("userData", true);
                node.addNodes(getEventNodes(value));
            }
        }, onCurrentChange:function (self, arg) {
            var current = arg.newCurrent, value, content = "", preview = this.id("DEBUGGER_EVENTS_PREVIEW");
            if (current) {
                value = current.get("data.value");
                if (value && typeof value.listener == "function") {
                    content = format2HTML(value.listener.toString());
                }
            }
            preview._dom.innerHTML = content;
        }}, {id:"DEBUGGER_EVENTS_PREVIEW", $type:"HtmlContainer"}]}}]}}};
        var logger = new dorado.widget.Logger({id:DEBUGGER_LOGGER_ID});
        var consolePanel = {$type:"Container", layout:{$type:"Dock", padding:0}, children:[{$type:"ToolBar", items:[{caption:"Clear", iconClass:"d-debugger-log-clear-icon", toolTip:"Clear log console", listener:{onClick:function () {
            logger.clear();
        }}}, {caption:"Lock", iconClass:"d-debugger-log-lock-icon", toolTip:"Lock log console", toggleable:true, toggled:logger._lockScrollBar, listener:{onToggle:function (self) {
            logger._lockScrollBar = self._toggled;
        }}}, "-", {id:DEBUGGER_DEBUG_BUTTON, $type:"SimpleIconButton", toggleable:true, iconClass:"d-debugger-log-debug-icon", toggled:true, toolTip:"Set log level to debug", listener:{onClick:function () {
            dorado.Debugger.setLogLevel("debug");
        }}}, {id:DEBUGGER_INFO_BUTTON, $type:"SimpleIconButton", iconClass:"d-debugger-log-info-icon", toggleable:true, toolTip:"Set log level to info", listener:{onClick:function () {
            dorado.Debugger.setLogLevel("info");
        }}}, {id:DEBUGGER_WARN_BUTTON, $type:"SimpleIconButton", iconClass:"d-debugger-log-warn-icon", toggleable:true, toolTip:"Set log level to warn", listener:{onClick:function () {
            dorado.Debugger.setLogLevel("warn");
        }}}, {id:DEBUGGER_ERROR_BUTTON, $type:"SimpleIconButton", iconClass:"d-debugger-log-error-icon", toggleable:true, toolTip:"Set log level to error", listener:{onClick:function () {
            dorado.Debugger.setLogLevel("error");
        }}}], layoutConstraint:"top"}, logger]};
        var debuggerPanel = new dorado.widget.Panel({border:"none", layout:new dorado.widget.layout.DockLayout({padding:0}), children:[{$type:"ToolBar", items:[{caption:"Run", iconClass:"d-debugger-script-run-icon", toolTip:"Run Code", listener:{onClick:function () {
            var codeTextArea = this.id(DEBUGGER_CODE_AREA_ID), code = codeTextArea.get("text");
            if (dorado.Debugger.trapError) {
                try {
                    eval(code);
                }
                catch (e) {
                    var errorMsg;
                    if (!dorado.Browser.msie) {
                        errorMsg = "name: " + e.name + "\nmessage: " + e.message + "\nstack: " + e.stack;
                    } else {
                        errorMsg = "name: " + e.name + "\nerrorNumber: " + (e.number & 65535) + "\nmessage: " + e.message;
                    }
                    this.id(DEBUGGER_TABCONTROL).set("currentTab", 0);
                    var logDom = dorado.Debugger.log(errorMsg, "error");
                    $fly(logDom).effect("highlight", {color:"#FFFF80"}, 1500);
                }
            } else {
                eval(code);
            }
        }}}, {caption:"Clear", toolTip:"Clear Code", iconClass:"d-debugger-script-clear-icon", listener:{onClick:function () {
            var codeTextArea = this.id(DEBUGGER_CODE_AREA_ID);
            codeTextArea.set("text", "");
        }}}, "->", {$type:"CheckBox", caption:"Trap Error", width:80, checked:dorado.Debugger.trapError, listener:{onValueChange:function (self) {
            dorado.Debugger.trapError = self._checked;
        }}}], layoutConstraint:"top"}, {id:DEBUGGER_CODE_AREA_ID, $type:"TextArea", width:"100%", layoutConstraint:"center"}]});
        var dialog = new dorado.widget.Dialog({layout:{$type:"Dock", padding:1}, caption:"Dorado Debugger", width:800, height:500, center:true, maximizeable:true, exClassName:"d-debugger", contentOverflow:"hidden", children:[{id:DEBUGGER_TABCONTROL, $type:"TabControl", tabs:[{$type:"Control", caption:"Console", control:consolePanel}, {$type:"Control", caption:"Script", control:debuggerPanel}, controlList]}, {$type:"Container", layout:{$type:"Dock", padding:4}, layoutConstraint:"bottom", height:30, children:[{$type:"CheckBox", caption:"Visible on load", checked:!!getCookie(DEBUGGER_SHOW_ON_VISIBLE_KEY), listener:{onValueChange:function (self) {
            if (self._value) {
                setCookie(DEBUGGER_SHOW_ON_VISIBLE_KEY, true, 3600 * 24 * 365 * 1000);
            } else {
                delCookie(DEBUGGER_SHOW_ON_VISIBLE_KEY);
            }
        }}}]}]});
        this.dialog = dialog;
        this.logger = logger;
        this.inited = true;
    }, setLogLevel:function (level) {
        if (level in LOG_LEVELS) {
            this.logger.set("level", level);
            if (this.logger._rendered) {
                var view = this.logger.get("view");
                if (view) {
                    view.id(DEBUGGER_DEBUG_BUTTON).set("toggled", false);
                    view.id(DEBUGGER_INFO_BUTTON).set("toggled", false);
                    view.id(DEBUGGER_WARN_BUTTON).set("toggled", false);
                    view.id(DEBUGGER_ERROR_BUTTON).set("toggled", false);
                    view.id("dorado.Debugger." + level + "Button").set("toggled", true);
                }
            }
        }
    }, log:function (msg, level) {
        var deb = dorado.Debugger;
        if (!deb.inited) {
            deb.init();
        }
        return deb.logger.log(msg, level);
    }, dir:function (msg, level) {
        var deb = dorado.Debugger;
        if (!deb.inited) {
            deb.init();
        }
        return deb.logger.dir(msg, level);
    }, show:function () {
        var deb = dorado.Debugger;
        if (!deb.inited) {
            deb.init();
        }
        deb.dialog.show();
    }};
    $(document).ready(function () {
        var showOnVisible = getCookie(DEBUGGER_SHOW_ON_VISIBLE_KEY);
        if (showOnVisible) {
            dorado.Debugger.show();
        }
        jQuery(document).bind("keydown", "f2", function () {
            dorado.Debugger.show();
        });
        jQuery(document).bind("keydown", "ctrl+f12", function () {
            dorado.Debugger.show();
        });
    });
    window.$log = dorado.Debugger.log;
})();

