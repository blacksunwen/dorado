dorado.MessageBox = {
    _runStack: [],

    /**
     * 对话框的默认标题。
     */
    defaultTitle: "MessageBox",

    /**
     * 对话框的最小宽度。
     */
    minWidth: 300,

    /**
     * 对话框的最大宽度。
     */
    maxWidth: 800,

    OK: ["ok"],
    CANCEL: ["cancel"],
    OKCANCEL: ["ok", "cancel"],
    YESNO: ["yes", "no"],
    YESNOCANCEL: ["yes", "no", "cancel"],

    WARNING_ICON: "warning-icon",
    ERROR_ICON: "error-icon",
    INFO_ICON: "info-icon",
    QUESTION_ICON: "question-icon",

    SINGLE_EDITOR: null,

    buttonText: {
        ok: "dorado.baseWidget.MessageBoxButtonOK",
        cancel: "dorado.baseWidget.MessageBoxButtonCancel",
        yes: "dorado.baseWidget.MessageBoxButtonYes",
        no: "dorado.baseWidget.MessageBoxButtonNo"
    },

    onButtonTap: function(buttonIndex) {
        var buttonId;
        if (dorado.MessageBox._runStack.length > 0) {
            var config = dorado.MessageBox._runStack[0];

            if (buttonIndex == "close") {
                if (config.closeAction) {
                    buttonId = config.closeAction;
                } else {
                    buttonIndex = config.buttons[config.buttons.length - 1];
                }
            }

            if (typeof config.detailCallback == "function" || typeof config.callback == "function") {
                if (!buttonId) {
                    buttonId = config.buttons[buttonIndex];
                }
                var text = null;
                if (config.editor != "none") {
                    if (buttonId != "ok") {
                        text = "";
                    } else {
                        switch (config.editor) {
                            case "single":
                                text = dorado.MessageBox.SINGLE_EDITOR.get("value");
                                break;
                            case "multiple":
                                text = dorado.MessageBox.TEXTAREA.get("value");
                                break;
                        }
                    }
                }
                if (typeof config.callback == "function" && (buttonId == "yes" || buttonId == "ok")) {
                    config.callback.apply(null, [text]);
                }
                if (typeof config.detailCallback == "function") {
                    config.detailCallback.apply(null, [buttonId, text]);
                }
            }

            dorado.MessageBox._runStack.splice(0, 1);
        }
        dorado.MessageBox._dialog.hide();
    },

    getDialog: function() {
        if (!dorado.MessageBox._dialog) {

            var dialog = dorado.MessageBox._dialog = new dorado.touch.Dialog({
                floating: true,
                focusAfterShow: true,
	            animateType: "none",
                center: true,
                modal: true,
                width: dorado.MessageBox.maxWidth,
                autoHeight: true,
                exClassName: "d-message-box",
                visible: false,
                closeAction: "hide",
                buttons: [new dorado.touch.Button({
                    listener: {
                        onTap: function() {
                            dorado.MessageBox.onButtonTap(0);
                        }
                    }
                }), new dorado.touch.Button({
                    listener: {
                        onTap: function() {
                            dorado.MessageBox.onButtonTap(1);
                        }
                    }
                }), new dorado.touch.Button({
                    listener: {
                        onTap: function() {
                            dorado.MessageBox.onButtonTap(2);
                        }
                    }
                })]
            });

            dorado.MessageBox._dialog.doOnAttachToDocument = function() {
                var dialog = this, dom = dialog.getContentContainer(), doms = dialog._doms;
                if (!doms) {
                    doms = dialog._doms = {};
                }
                //dorado.touch.Panel.prototype.doOnAttachToDocument.apply(dialog, []);
                if (dom) {
                    var lastIcon = dorado.MessageBox._lastIcon || "";
                    dom.appendChild($DomUtils.xCreate({
                        tagName: "div",
                        className: "msg-content",
                        contextKey: "msgContent",
                        content: [ {
                            tagName: "span",
                            className: "msg-icon " + lastIcon,
                            contextKey: "msgIcon"
                        }, {
                            tagName: "span",
                            className: "msg-text",
                            contextKey: "msgText",
                            content: dorado.MessageBox._lastText
                        }]
                    }, null, doms));

                    var editorWrap = $DomUtils.xCreate({ tagName: "div", className: "editor-wrap" });

                    doms.editorWrap = editorWrap;

                    var editor = new dorado.touch.TextEditor();
                    editor.render(editorWrap);
                    $fly(editor._dom).css("display", "none");
                    dorado.MessageBox.SINGLE_EDITOR = editor;
                    dialog.registerInnerControl(editor);

                    dom.appendChild(editorWrap);

                    var textareaWrap = $DomUtils.xCreate({
                        tagName: "div",
                        className: "textarea-wrap"
                    });

                    doms.textareaWrap = textareaWrap;

                    var textarea = /*new dorado.touch.TextArea()*/ new dorado.touch.TextEditor();
                    textarea.render(textareaWrap);
                    $fly(textarea._dom).css("display", "none");
                    dorado.MessageBox.TEXTAREA = textarea;
                    dialog.registerInnerControl(textarea);

                    dom.appendChild(textareaWrap);

                    dorado.MessageBox.updateText(dorado.MessageBox._lastText, dorado.MessageBox._lastIcon, dorado.MessageBox._lastEditor, dorado.MessageBox._lastValue);
                }

                dialog.addListener("beforeShow", function(dialog) {
                    var dom = dialog._dom;
                    $fly(dom).width(dorado.MessageBox.maxWidth);

                    var doms = dialog._doms, contentWidth = $fly(doms.msgText).outerWidth(true) + $fly(doms.msgIcon).outerWidth(true);

                    if (contentWidth < dorado.MessageBox.minWidth) {
                        contentWidth = dorado.MessageBox.minWidth;
                    } else if (contentWidth > dorado.MessageBox.maxWidth) {
                        contentWidth = dorado.MessageBox.maxWidth;
                    }
                    var dialogWidth = $fly(dom).width(), panelWidth = $fly(doms.contentPanel).width();
                    dialog._width = contentWidth + dialogWidth - panelWidth;
                    $fly(dom).width(dialog._width);
                    dialog._height = null;
                    dialog.onResize();
                });

                dialog.addListener("onShow", function(dialog) {
                    var buttons = dialog._buttons, button;
                    if(buttons){
                        button = buttons[0];
                        if(button && button._dom.display != "none"){
                            //dorado.widget.setFocusedControl(button);
                        }
                    }
                });

                dialog.addListener("beforeHide", function(self, arg) {
                    if (dorado.MessageBox._runStack.length > 0) {
                        arg.processDefault = false; //通知系统不再执行默认的后续动作。
                        dorado.MessageBox.doShow(dorado.MessageBox._runStack[0]);
                    }
                });

                dialog.addListener("beforeClose", function(self, arg) {
                    dorado.MessageBox.onButtonTap("close");
                    arg.processDefault = false;
                });
            };

        }
        return dorado.MessageBox._dialog;
    },

    /**
     * 显示提示对话框。
     * @param {String} msg 要显示的提示信息。
     * @param {Object|Function} options 如果提供一个Function类型的参数，则会被当作callback，即用户点击yes、ok后执行的回调函数。
     * 									如果是json类型，则里面的配置选项与show方法一样。
     * @see dorado.MessageBox.show
     */
    alert: function(msg, options) {
        if (typeof options == "function") {
            var callback = options;
            options = {
                callback: callback
            };
        } else {
            options = options || {};
        }
        options.icon = dorado.MessageBox.INFO_ICON;
        options.message = msg;
        options.buttons = dorado.MessageBox.OK;
        options.closeAction = "ok";
        dorado.MessageBox.show(options);
    },

    /**
     * 显示确认对话框。
     * @param {String} msg 要显示的提示信息。
     * @param {Object|Function} options 如果提供一个Function类型的参数，则会被当作callback，即用户点击yes、ok后执行的回调函数。
     * 									如果是json类型，则里面的配置选项与show方法一样。
     * @see dorado.MessageBox.show
     */
    confirm: function(msg, options) {
        if (typeof options == "function") {
            var callback = options;
            options = {
                callback: callback
            };
        } else {
            options = options || {};
        }
        options.icon = dorado.MessageBox.QUESTION_ICON;
        options.message = msg;
        options.buttons = dorado.MessageBox.YESNO;
        options.closeAction = "no";
        dorado.MessageBox.show(options);
    },

    /**
     * 显示单行输入对话框。
     * @param {String} msg 要显示的提示信息。
     * @param {Object|Function} options 如果提供一个Function类型的参数，则会被当作callback，即用户点击yes、ok后执行的回调函数。
     * 									如果是json类型，则里面的配置选项与show方法一样。
     * @see dorado.MessageBox.show
     */
    prompt: function(msg, options) {
        if (typeof options == "function") {
            var callback = options;
            options = {
                callback: callback
            };
        } else {
            options = options || {};
        }
        options.message = msg;
        options.buttons = dorado.MessageBox.OKCANCEL;
        options.closeAction = "cancel";
        options.editor = "single";
        dorado.MessageBox.show(options);
    },

    /**
     * 显示多行输入对话框。
     * @param {String} msg 要显示的提示信息。
     * @param {Object|Function} options 如果提供一个Function类型的参数，则会被当作callback，即用户点击yes、ok后执行的回调函数。
     * 									如果是json类型，则里面的配置选项与show方法一样。
     * @see dorado.MessageBox.show
     */
    promptMultiline: function(msg, options) {
        if (typeof options == "function") {
            var callback = options;
            options = {
                callback: callback
            };
        } else {
            options = options || {};
        }
        options.message = msg;
        options.buttons = dorado.MessageBox.OKCANCEL;
        options.closeAction = "cancel";
        options.editor = "multiple";
        dorado.MessageBox.show(options);
    },

    resetEditorWidth: function(editor) {
        var dialog = dorado.MessageBox.getDialog(), doms = dialog._doms, width;
        if (editor == "multiple" && dorado.MessageBox.TEXTAREA) {
            width = $fly(doms.textareaWrap).outerWidth();
            dorado.MessageBox.TEXTAREA.set("width", width);
        } else if (editor == "single" && dorado.MessageBox.SINGLE_EDITOR) {
            width = $fly(doms.editorWrap).outerWidth();
            dorado.MessageBox.SINGLE_EDITOR.set("width", width);
        }
    },

    updateText: function(text, icon, editor, value) {
        var dialog = dorado.MessageBox.getDialog(), doms = dialog._doms;

        dorado.MessageBox._lastText = text;
        dorado.MessageBox._lastIcon = icon;
        dorado.MessageBox._lastEditor = editor;
        dorado.MessageBox._lastValue = value;

        if (!doms) return;

        text += '';
        if (text) {
            text = text.replace(/&/g, "&amp;").replace(/[<>\"\n]/g, function($1) {
                switch ($1) {
                    case "<":
                        return "&lt;";
                    case ">":
                        return "&gt;";
                    case "\n":
                        return "<br/>";
                    case "\"":
                        return "&quot;";
                }
            });
        }

        $fly(doms.msgText).html(text || "&nbsp;");
        $fly(doms.msgIcon).prop("className", "msg-icon");

        if (icon) {
            $fly(doms.msgIcon).addClass(icon).css("display", "");
            $fly(doms.msgContent).addClass("msg-content-hasicon");
        } else {
            $fly(doms.msgIcon).css("display", "none");
            $fly(doms.msgContent).removeClass("msg-content-hasicon");
        }
        if (dorado.MessageBox.SINGLE_EDITOR) {
            switch (editor) {
                case "none":
                    $fly(doms.editorWrap).css("display", "none");
                    $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "none");
                    $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "none");
                    break;
                case "single":
                    $fly(doms.editorWrap).css("display", "");
                    $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "");
                    $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "none");
                    dorado.MessageBox.SINGLE_EDITOR.set("value", value || "");
                    break;
                case "multiple":
                    $fly(doms.editorWrap).css("display", "");
                    $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "none");
                    $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "");
                    dorado.MessageBox.TEXTAREA.set("value", value || "");
                    break;
            }
        }
    },

    /**
     * 通过自定义参数显示对话框。
     * @param {Object} options 显示参数。
     * @param {String} options.title dialog显示的标题，如果不配置，取dorado.MessageBox.defaultTitle。
     * @param {String} options.message 要显示的提示信息。
     * @param {String} options.defaultText 如果显示单行或者多行文本框，为文本框的默认值。
     * @param {String} options.icon 目前可选值：dorado.MessageBox.WARNING_ICON、dorado.MessageBox.ERROR_ICON、dorado.MessageBox.INFO_ICON、dorado.MessageBox.QUESTION_ICON。
     *                              这个值会被作为icon的className添加到icon上，直接传入一个className也可以。
     * @param {String} options.editor 目前可选值:single、multiple、none。分别代表单行输入框、多行输入框、无输入框。
     * @param {String[]} options.buttons 要显示的按钮，数组形式。目前最多支持显示3个按钮，每个按钮使用不同的id来显示，目前支持ok(确定)、cancel(取消)、yes(是)、no(否)。
     * @param {String} options.closeAction 如果点击dialog的关闭按钮，对应的buttonId是什么，如果不传入这个值，默认取最后一个button。
     * @param {Function} options.detailCallback 当用户点击不同的按钮的时候执行的回调函数，会传入两个参数：buttonId与text。
     * @param {Function} options.callback 当用户点击yes、ok后执行的回调函数，仅传入一个参数：text。
     */
    show: function(options) {
        dorado.MessageBox._runStack.push(options);
        if (dorado.MessageBox._runStack.length > 1) {
            return;
        }
        dorado.MessageBox.doShow(options);
    },

    doShow: function(options) {
        options = options || {};
        //dorado.widget.setFocusedControl(null);

        var dialog = dorado.MessageBox.getDialog(), msg = options.message, defaultText = options.defaultText,
            title = options.title || dorado.MessageBox.defaultTitle, icon = options.icon, buttons = options.buttons || [],
            buttonCount = buttons.length, editor = options.editor || "none", dlgButtons = dialog._buttons;

        dorado.MessageBox.updateText(msg, icon, editor, defaultText);
        dialog.set({ caption: title });
        dialog.show({
            overflowHandler: function(options) {
                dialog._height = options.maxHeight;
                dialog.onResize();
            }
        });

        if (editor != "none") {
            dorado.MessageBox.resetEditorWidth(editor);
        }

        for (var i = 0; i < 3; i++) {
            var button = buttons[i];
            if (i >= buttonCount) {
                $fly(dlgButtons[i]._dom).css("display", "none");
            }
            else {
                var caption;
                //TODO remove false &&
                if (false && dorado.MessageBox.buttonText[button]) {
                    caption = $resource(dorado.MessageBox.buttonText[button]);
                } else {
                    caption = button;
                }
                dlgButtons[i].set("caption", caption.toUpperCase());
                $fly(dlgButtons[i]._dom).css("display", "");
            }
        }
    }
};