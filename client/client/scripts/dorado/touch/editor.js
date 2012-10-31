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
            $fly(triggerDom).appendTo(dom);
            $fly(dom).addClass("text-editor-holder");
            jQuery(triggerDom).addClassOnHover("trigger-hover").addClassOnClick("trigger-click");

	        function show(popup) {
		        if (popup && popup.show) {
                    popup._focusParent = editor;
			        popup.show({
				        anchorTarget: editor,
				        align: "innerleft",
				        vAlign: "bottom"
			        });
                    popup.sync(editor);
		        }
	        }

            $fly(dom).bind("click", function() {
                var popup = editor._popup;
                if (popup && editor._editable == false) {
	                show(popup);
                }
            });

            $fly(triggerDom).bind("click", function() {
                var popup = editor._popup;
                show(popup);
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
            $fly(iconDom).insertBefore(doms["editor"]);
            $fly(dom).addClass("text-editor-icon");
        }
    };

    dorado.touch.TextEditor = $extend(dorado.widget.AbstractEditor, {
        $className: "dorado.touch.TextEditor",
        ATTRIBUTES: {
            className: {
                defaultValue: "text-editor"
            },
            icon: {},
            emptyText: {},
            editable: {
                defaultValue: true
            },
            popup: {},
            text: {
                setter: function(text) {
                    var editor = this;

                    if (editor._t2vmapping && editor._t2vmapping[text]) {
                        editor._value = editor._t2vmapping[text];
                    }

                    if (editor._rendered) {
                        editor._doms.editor.value = text;
                    }

                    this._text = text;
                },
                getter: function() {
                    var editor = this, value = editor._value;
                    return editor.formatValue(value);
                }
            },
            mapping: {
                setter: function(mapping) {
                    if (mapping instanceof Array) {
                        var v2tmapping = {}, t2vmapping = {};
                        for (var i = 0, j = mapping.length; i < j; i++) {
                            var record = mapping[i], text = record.text, value = record.value;
                            v2tmapping[value] = text;
                            t2vmapping[text] = value;
                        }
                        this._v2tmapping = v2tmapping;
                        this._t2vmapping = t2vmapping;
                    }
                    this._mapping = mapping;
                }
            },
            value: {
                setter: function(value) {
                    var editor = this;
                    editor._text = editor.formatValue(value);
                    if (editor._rendered) {
                        editor._doms.editor.value = editor._text;
                    }
                    editor._value = value;
                    //console.log("before onValueChange value:" + editor._value);
                    editor.fireEvent("onValueChange", editor, value);
                    //console.log("after onValueChange value:" + editor._value);
                }
            },
            type: {
                defaultValue: "text"
            },
            format: {
                defaultValue: "Y-m-d H:i:s"
            }
        },
        EVENTS: {
            onValueChange: {}
        },
        createDom: function() {
            var editor = this, doms = {}, dom, value = editor.get("value"), emptyText = editor.get("emptyText");
            dom = $DomUtils.xCreate({
                tagName: "div",
                className: "text-editor",
                content: {
                    contextKey: "editor",
                    tagName: "input",
                    className: "editor",
                    type: editor._type ? editor._type: "text",
                    value: value ? value : "",
                    placeholder: editor._emptyText ? editor._emptyText : ""
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
        formatValue: function(value) {
            var editor = this, text;
            if (editor._v2tmapping && editor._v2tmapping[value]) {
                text = editor._v2tmapping[value];
            } else if (value instanceof Date) {
                text = value.formatDate(editor._format);
            } else {
                text = value;
            }

            return text;
        },
        refreshDom: function(){
            $invokeSuper.call(this, arguments);

            var editor = this, dom = editor._dom;
            if (editor.get("rendered")) {
                var doms = editor._doms, editorEl = doms["editor"], emptyText = editor.get("emptyText"), text = editor.get("text");
                editorEl.placeholder = emptyText || "";
                editorEl.readOnly = !editor._editable || editor._readOnly;
                $fly(dom)[editor._readOnly ? "addClass" : "removeClass"](editor._className + "-readonly");
                if(text){
                    editorEl.value = text;
                } else {
                    if (!isValue(emptyText)) {
                        editorEl.value = text || "";
                    }
                }
            }
        },
        doOnKeyDown: function(event){
            if(event.keyCode == 13){
            }
            return true;
        }
    });

    dorado.touch.CheckBox2 = $extend(dorado.widget.AbstractEditor, {
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

    dorado.touch.RadioButton2 = $extend(dorado.widget.AbstractEditor, {
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
        $className: "dorado.touch.Toggle",
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

            $fly(dom).bind("click", function() {
                $fly(dom).toggleClass(toggle._className + "-on");
            });

            return dom;
        }
    });

    dorado.touch.Spinner = $extend(dorado.widget.AbstractEditor, {
        $className: "dorado.touch.Spinner",
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

            $fly(doms.downButton).bind("click", function() {
                if (!(spinner._readOnly || spinner._readOnly2))
                    spinner.doDown();
            });

            $fly(doms.upButton).bind("click", function() {
                if (!(spinner._readOnly || spinner._readOnly2))
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
                $fly(dom)[spinner._readOnly ? "addClass" : "removeClass"](spinner._className + "-readonly");
                if(value){
                    editorEl.value = spinner.formatValue(value);
                } else {
                    editorEl.value = "";
                }
            }
        }

    });
})();
