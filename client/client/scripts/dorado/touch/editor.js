(function(){

    var createTrigger = function(editor, dom) {
        if (!editor) return;
        var doms = editor._doms;
        if (doms && doms["editor"]) {
            var triggerDom = $DomUtils.xCreate({
                contextKey: "trigger",
                tagName: "div",
                className: "trigger"
            }, null, doms);
            $fly(triggerDom).prependTo(dom);
            $fly(dom).addClass("text-editor-holder");
            jQuery(triggerDom).addClassOnHover("trigger-hover").addClassOnClick("trigger-click");

            $fly(triggerDom).bind("tap", function() {
                var popup = editor.popup;
                if (popup.show) {
                    popup.show({
                        opener: editor,
                        align: "center",
                        valign: "bottom"
                    });
                }
            });
        }
    };


    var ARRAY = "array", BOOLEAN   = "boolean", DATE = "date", ERROR = "error", FUNCTION = "function",
        NUMBER = "number", NULL = "null", OBJECT = "object", REGEX = "regexp", STRING = "string", UNDEFINED = "undefined";

    var TYPES = {
        "undefined"         : UNDEFINED,
        "number"            : NUMBER,
        "boolean"           : BOOLEAN,
        "string"            : STRING,
        "[object Function]" : FUNCTION,
        "[object RegExp]"   : REGEX,
        "[object Array]"    : ARRAY,
        "[object Date]"     : DATE,
        "[object Error]"    : ERROR
    };

    var $type =  function (object) {
        return  TYPES[typeof object] || TYPES[Object.prototype.toString.call(object)] || (object ? OBJECT : NULL);
    };

    var isValue = function(object) {
        var type = $type(object);
        switch (type) {
            case NUMBER:
                return isFinite(object);
            case NULL:
            case UNDEFINED:
                return false;
            default:
                return !!(type);
        }
    }

    var createIcon = function(editor, dom) {
        if (!editor) return;
        var doms = editor._doms;
        if (doms && doms["editor"]) {
            var iconDom = $DomUtils.xCreate({
                contextKey: "icon",
                tagName: "div",
                className: "icon"
            }, null, doms);
            $fly(iconDom).insertBefore(doms["editorWrap"]);
            $fly(dom).addClass("text-editor-icon");
        }
    };

    dorado.touch.TextEditor = $extend(dorado.widget.AbstractEditor, {
        ATTRIBUTES: {
            icon: {},
            emptyText: {},
            popup: {},
            type: {
                defaultValue: "text"
            }
        },
        EVENTS: {
            change: {}
        },
        createDom: function() {
            var editor = this, doms = {}, dom, value = editor.get("value"), emptyText = editor.get("emptyText");
            dom = $DomUtils.xCreate({
                tagName: "div",
                className: "text-editor",
                content:{
                    contextKey: "editorWrap",
                    tagName: "div",
                    className: "editor-wrap",
                    content: {
                        contextKey: "editor",
                        tagName: "input",
                        className: "editor",
                        type: editor.type ? editor.type: "text",
                        value: value ? value : "",
                        placeholder: editor._emptyText ? editor._emptyText : ""
                    }
                }
            }, null, doms);

            editor._doms = doms;

            var popup = editor.get("popup"), icon = editor.get("icon");

            if (popup) {
                createTrigger(editor, dom);
            }

            if (icon) {
                createIcon(editor, dom);
                $fly(doms["icon"]).css("background-image", "url('" + icon + "')");
            }

            if (emptyText && !value) {
                $fly(dom).addClass("text-editor-empty");
            }

            $fly(doms["editor"]).bind("change", function() {
                var editorEl = this;
                editor.set("value", editorEl.value);
            });

            return dom;
        },
        refreshDom: function(){
            $invokeSuper.call(this, arguments);

            var editor = this, dom = editor._dom;
            if (editor.get("rendered")) {
                var doms = editor._doms, editorEl = doms["editor"], emptyText = editor.get("emptyText"), value = editor.get("value");
                editorEl.placeholder = emptyText || "";
                if(value){
                    editorEl.value = value;
                } else {
                    if (!isValue(emptyText)) {
                        editorEl.value = value || "";
                    }
                }
            }
        },
        doOnKeyDown: function(event){
            if(event.keyCode == 13){
                alert("Enter a!!");
            }
            return true;
        },
        onFocus: function(){
            var editor = this, dom = editor._dom, doms = editor._doms, editorEl = doms["editor"], emptyText = editor.get("emptyText");
            $fly(dom).addClass("text-editor-focused");
            if (isValue(emptyText)) {
                if (!editor.get("value")) {
                    editorEl.value = "";
                    $fly(dom).removeClass("text-editor-empty");
                }
            }
        },
        onBlur: function(){
            var editor = this, dom = editor._dom, doms = editor._doms, editorEl = doms["editor"], emptyText = editor.get("emptyText");
            $fly(dom).removeClass("text-editor-focused");
            editor.set("value", editorEl.value);
            if (isValue(emptyText)) {
                if (!editorEl.value) {
                    editorEl.value = emptyText;
                    $fly(dom).addClass("text-editor-empty");
                } else {
                    $fly(dom).removeClass("text-editor-empty");
                }
            }
        }
    });

    dorado.touch.CheckBox = $extend(dorado.widget.AbstractEditor, {
        ATTRIBUTES: {
            className: {
                defaultValue: "check-box"
            }
        },

        createDom: function() {
            var checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.className = this.className;

            return checkbox;
        }
    });

    dorado.touch.RadioButton = $extend(dorado.widget.AbstractEditor, {
        ATTRIBUTES: {
            className: {
                defaultValue: "radio-button"
            },

            group: {}
        },

        createDom: function() {
            var radio = document.createElement("input");
            radio.type = "radio";
            radio.name = this.group;
            radio.className = this.className;

            return radio;
        }
    });

    dorado.touch.Toggle = $extend(dorado.widget.AbstractEditor, {
        ATTRIBUTES: {
            className: {
                defaultValue: "toggle"
            }
        },

        createDom: function() {
            var toggle = this, dom = $DomUtils.xCreate({
                tagName: "div",
                className: this._className,
                content: {
                    tagName: "div",
                    className: "thumb"
                }
            });

            $fly(dom).bind("tap", function() {
                $fly(dom).toggleClass(toggle._className + "-on");
            });

            return dom;
        }
    });

    dorado.touch.Spinner = $extend(dorado.widget.AbstractEditor, {
        ATTRIBUTES: {
            className: {
                defaultValue: "spinner"
            },
            min: {},
            max: {},
            step: {
                defaultValue: 1
            },
            textPattern: {
                defaultValue: "{value}"
            }
        },

        createDom: function() {
            var spinner = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                className: this._className,
                content: [{
                    tagName: "div",
                    className: "down-button",
                    contextKey: "downButton",
                    content: {
                        tagName: "div",
                        className: "icon"
                    }
                }, {
                    tagName: "div",
                    className: "up-button",
                    contextKey: "upButton",
                    content: {
                        tagName: "div",
                        className: "icon"
                    }
                }, {
                    tagName: "div",
                    className: "editor-wrap",
                    content: {
                        tagName: "input",
                        type: "text",
                        readOnly: true,
                        className: "editor",
                        contextKey: "editor"
                    }
                }]
            }, null, doms);

            spinner._doms = doms;

            $fly(doms.downButton).bind("tap", function() {
                spinner.doDown();
            });

            $fly(doms.upButton).bind("tap", function() {
                spinner.doUp();
            });

            return dom;
        },

        doUp: function() {
            var spinner = this, value = spinner.get("value") || 0, step = spinner.get("step") || 1;
            spinner.set("value", value + step);
        },

        doDown: function() {
            var spinner = this, value = spinner.get("value") || 0, step = spinner.get("step") || 1;
            spinner.set("value", value - step);
        },

        formatValue: function(value) {
            var format = this._textPattern || "{value}";
            return format.replace("{value}", value);
        },

        refreshDom: function(){
            $invokeSuper.call(this, arguments);

            var spinner = this, dom = spinner._dom;
            if (spinner.get("rendered")) {
                var doms = spinner._doms, editorEl = doms["editor"], value = spinner.get("value");
                if(value){
                    editorEl.value = spinner.formatValue(value);
                } else {
                    editorEl.value = "";
                }
            }
        },

    });
})();