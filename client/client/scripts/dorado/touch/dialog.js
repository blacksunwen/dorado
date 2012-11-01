/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

dorado.touch.FloatPanel = $extend([dorado.touch.Panel, dorado.widget.FloatControl], /** @scope dorado.widget.FloatPanel.prototype */ {
    $className: "dorado.touch.FloatPanel",
    focusable: true,

    ATTRIBUTES: /** @scope dorado.widget.FloatPanel.prototype */ {
        visible: {
            defaultValue: false
        }
    },

    doShow: function() {
        var panel = this, doms = panel._doms;
        $fly([doms.contentPanel, doms.buttonPanel]).css("display", "");

        $invokeSuper.call(this, arguments);
    },

    doAfterShow: function() {
        var panel = this;
        panel._minimized = false;
        $invokeSuper.call(this, arguments);
    }
});

dorado.touch.Dialog = $extend(dorado.touch.FloatPanel, /** @scope dorado.widget.Dialog.prototype */ {
    $className: "dorado.touch.Dialog",
    _inherentClassName: "i-dialog",

    ATTRIBUTES: /** @scope dorado.widget.Dialog.prototype */ {
        className: {
            defaultValue: "d-dialog"
        },

		animateType: {
            defaultValue: "none"
		},

        /**
         * 对话框的最小宽度，建议不要小于200。
         * @attribute
         * @default 200
         * @type int
         */
        minWidth: {
            defaultValue: 200
        },

        /**
         * 对话框的最小高度，建议不要小于100。
         * @attribute
         * @default 100
         * @type int
         */
        minHeight: {
            defaultValue: 100
        },

        /**
         * 对话框是否可拖拽。
         * @attribute
         * @default true
         * @type boolean
         */
        draggable: {
            defaultValue: true
        },

        /**
         * 对话框是否可以拖出window。
         * @attribute writeBeforeReady
         * @type boolean
         */
        dragOutside: {
            defaultValue: false
        },

        /**
         * 对话框是否可以改变尺寸。
         * @attribute
         * @default true
         * @type boolean
         */
        resizeable: {
            defaultValue: true
        },

        /**
         * 默认为window对象，可以是dorado的组件、dom对象、dom对象的jQuery选择符。
         * @attribute
         * @type String|Object|dorado.RenderableElement
         */
        maximizeTarget: {},

        /**
         * 是否显示最小化按钮.
         * @attribute
         * @default false
         * @type boolean
         */
        minimizeable: {
            defaultValue: false,
            setter: function(value) {
                var dialog = this, captionBar = dialog._captionBar, button;
                dialog._minimizeable = value;
                if (captionBar) {
                    if (value) {
                        button = captionBar.getButton(dialog._id + "_minimize");
                        if (button) {
                            $fly(button._dom).css("display", "");
                        } else {
                            dialog._createMinimizeButton();
                        }
                    } else {
                        button = captionBar.getButton(dialog._id + "_minimize");
                        if (button) {
                            $fly(button._dom).css("display", "none");
                        }
                    }
                }
            }
        },
        /**
         * 对话框是否已经最小化。
         * @attribute
         * @default false
         * @type boolean
         */
        minimized: {},

        closeable: {
            defaultValue: false
        },

        shadowMode: {
            defaultValue: "frame",
            skipRefresh: true
        }
    },

    EVENTS: /** @scope dorado.widget.Dialog.prototype */ {
        /**
         * 在对话框最大化之前触发此事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        beforeMaximize: {},

        /**
         * 在对话框最大化之后触发此事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        onMaximize: {},

        /**
         * 在对话框最小化之前触发此事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        beforeMinimize: {},

        /**
         * 在对话框最小化之后触发此事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        onMinimize: {}
    },

    setFocus: function() {
        var dialog = this;
        if (dialog._rendered) {
            //下面这句话有可能会导致IE8下无法bringToFront
            dialog._dom.focus();
        }
    },

    applyDraggable: function() {
        // do nothing
    },

    maximizeRestore: function() {
        var dialog = this, dom = dialog._dom, doms = dialog._doms;
        if (dom) {
            $fly(doms.contentPanel).css("display", "");

            if (dialog._maximized) {
                dialog._maximized = false;
                dialog._width = dialog._originalWidth;
                dialog._height = dialog._originalHeight;
                dialog._left = dialog._originalLeft;
                dialog._top = dialog._originalTop;
                dialog.resetDimension();
                dialog.refresh();

                var captionBar = dialog._captionBar;
                if (captionBar) {
                    var button = captionBar.getButton(dialog._id + "_maximize");
                    if (button) {
                        $fly(button._dom).prop("className", "d-maximize-button");
                        button._className = "d-maximize-button";
                    }
                }

                var $dom = $fly(dom);
                if (dialog._resizeable) {
                    $dom.addClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
                }

                if (dialog._draggable) {
                    $dom.addClass("i-dialog-resizeable d-dialog-draggable").draggable("enable");
                }
            }
        }
    },

    maximize: function() {
        var dialog = this, dom = dialog._dom;
        if (dom) {
            var eventArg = {};
            dialog.fireEvent("beforeMaximize", dialog, eventArg);
            if (eventArg.processDefault === false) {
                return;
            }

            dialog._maximized = true;
            dialog._originalWidth = dialog._width;
            dialog._originalHeight = dialog._height;
            dialog._originalLeft = dialog._left;
            dialog._originalTop = dialog._top;

            var maximizeTarget = dialog._maximizeTarget;
            if (typeof maximizeTarget == "String") {
                maximizeTarget = jQuery(maximizeTarget)[0];
            } else if (maximizeTarget && dorado.Object.isInstanceOf(maximizeTarget, dorado.RenderableElement)) {
                maximizeTarget = maximizeTarget._dom;
            }
            if (maximizeTarget) {
                dialog._width = $fly(maximizeTarget).outerWidth(true);
                dialog._height = $fly(maximizeTarget).outerHeight(true);
            } else {
                dialog._width = $fly(window).width();
                dialog._height = $fly(window).height();
            }

            var captionBar = dialog._captionBar;
            if (captionBar) {
                var button = captionBar.getButton(dialog._id + "_maximize");
                if (button) {
                    $fly(button._dom).prop("className", "d-restore-button");
                    button._className = "d-restore-button";
                }
            }

            dialog.resetDimension();

            var targetOffset = $fly(maximizeTarget).offset() || {left: 0, top: 0};

            dialog._left = targetOffset.left;
            dialog._top = targetOffset.top;

            var domEl = $fly(dom);
            domEl.css(targetOffset);

            if (dialog._resizeable) {
                domEl.removeClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
            }

            if (dialog._draggable) {
                domEl.removeClass("i-dialog-resizeable d-dialog-draggable").draggable("disable");
            }

            dialog.fireEvent("onMaximize", dialog);
        }
    },

    minimize: function() {
        var dialog = this, dom = dialog._dom;
        if (dom) {
            var eventArg = {};
            dialog.fireEvent("beforeMinimize", dialog, eventArg);
            if (eventArg.processDefault) {
                return;
            }
            dialog._minimized = true;
            dialog.hide();
            dialog.fireEvent("onMinimize", dialog);
        }
    },

    doSetCollapsed: function(collapsed) {
        $invokeSuper.call(this, arguments);
        var dialog = this;
        if (dialog._resizeable) {
            if (collapsed) {
                $fly(dialog._dom).removeClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
            } else {
                $fly(dialog._dom).addClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
            }
        }
    },

    _doOnResize: function(collapsed) {
        var dialog = this, dom = dialog._dom, doms = dialog._doms, height = dialog.getRealHeight(), width = dialog.getRealWidth();

        if (typeof width == "string") {
            if (width.indexOf("%") == -1) {
                width = parseInt(width, 10);
            } else {
                width = jQuery(window).width() * parseInt(width.replace("%", ""), 10) / 100;
            }
        }

        if (typeof height == "string") {
            if (height.indexOf("%") == -1) {
                height = parseInt(height, 10);
            } else {
                height = jQuery(window).height() * parseInt(height.replace("%", ""), 10) / 100;
            }
        }

        if (typeof height == "number" && height > 0) {
            if (collapsed === undefined) {
                collapsed = dialog._collapsed;
            }
            if (collapsed) {
                $fly(dom).height("auto");
            } else {
                var headerHeight = $fly(doms.header).outerHeight(true), footerHeight = $fly(doms.footer).outerHeight(true),
                    captionBarHeight = 0, buttonPanelHeight = 0;

                if (doms.captionBar) {
                    captionBarHeight = $fly(doms.captionBar).outerHeight(true)
                }

                if (doms.buttonPanel) {
                    buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                }

                $fly(doms.contentPanel).outerHeight(height - headerHeight - footerHeight - captionBarHeight - buttonPanelHeight);
                //$fly(doms.bodyWrap).outerHeight(height - headerHeight - bottomHeight);
                if (dorado.Browser.msie && dorado.Browser.version == 6) {
                    $fly(doms.bodyWrap).css("zoom", 1);
                }
            }
        } else {
            $fly(doms.contentPanel).css("height", "");
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(doms.bodyWrap).css("zoom", 1);
            }
        }
        $fly(dom).css("height", "");

        if (typeof width == "number" && width > 0) {
            $fly(dom).outerWidth(width);
        }
    },

    _createMinimizeButton: function() {
        var dialog = this, captionBar = dialog._captionBar;
        if (captionBar) {
            captionBar.addButton(new dorado.widget.SimpleButton({
                id: dialog._id + "_minimize",
                className: "d-minimize-button",
                listener: {
                    onClick: function() {
                        if (!dialog._minimized) {
                            dialog.minimize();
                        }
                    }
                }
            }), 0);
        }
    },

    createDom: function() {
        var dialog = this, dom = $invokeSuper.call(this, arguments), doms = dialog._doms;

        dialog.initButtons();

        if (dialog._minimizeable) {
            dialog._createMinimizeButton();
        }

        if (dialog._maximizeable) {
            dialog._createMaximizeButton();
        }

        if (dialog._closeable) {
            dialog._createCloseButton();
        }

        if (dialog._collapseable) {
            dialog._createCollapseButton();
        }

        return dom;
    },

    getShowPosition: function(options) {
        var panel = this;
        if (panel._maximized) {
            var result = { left: 0, top: 0 };
            $fly(panel._dom).css(result);
            return result;
        } else {
            return $invokeSuper.call(panel, arguments);
        }
    },

    refreshDom: function(dom) {
        var dialog = this;

        $invokeSuper.call(dialog, arguments);
    }
});
