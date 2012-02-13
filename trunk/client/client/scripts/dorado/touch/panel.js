(function() {
    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
     * @class 抽象面板。
     * <p>
     * 该类是抽象类，是FieldSet、GroupBox、Panel的超类。
     * 继承该类之后，会拥有以下功能：
     * <ul>
     *   <li>标题的显示</li>
     *   <li>面板下方的Button的显示。</li>
     *	 <li>收缩与展开。</li>
     * </ul>
     * </p>
     * @abstract
     * @extends dorado.touch.Container
     */
    dorado.touch.AbstractPanel = $extend(dorado.widget.Container, /** @scope dorado.touch.AbstractPanel.prototype */ {
        ATTRIBUTES: /** @scope dorado.touch.AbstractPanel.prototype */ {
            /**
             * 显示的标题。
             * @attribute
             * @type String
             */
            caption: {
                skipRefresh: true,
                path: "_toolbar._caption"
            },

            /**
             * 显示在对话框下方的按钮。
             * @attribute
             * @type Array
             */
            buttons: {
            },

            /**
             * button的显示位置，可选值：center、left、right，默认值是center。
             * @attribute
             * @type String
             * @default center
             */
            buttonAlign: {
                defaultValue: "center",
                skipRefresh: true,
                setter: function(attr, value) {
                    var panel = this, doms = panel._doms, oldValue = panel._buttonAlign;
                    if (doms) {
                        if (oldValue && oldValue != "center") {
                            $fly(doms.buttonPanel).removeClass("button-panel-" + oldValue);
                        }
                        if (value && value != "center") {
                            $fly(doms.buttonPanel).addClass("button-panel-" + value);
                        }
                    }

                    panel._buttonAlign = value;
                }
            }

        },
        EVENTS: /** @scope dorado.touch.AbstractPanel.prototype */ {
        },

        createButtonPanel: function(dom) {
            var panel = this, doms = panel._doms, buttonPanel = document.createElement("div");
            buttonPanel.className = "button-panel";

            doms.buttonPanel = buttonPanel;
            if (doms.body) {
                doms.body.appendChild(buttonPanel);
            } else {
                dom.appendChild(buttonPanel);
            }
            return buttonPanel;
        },

        initButtons: function(dom) {
            var panel = this, doms = panel._doms;
            if (panel._buttons) {
                var buttons = panel._buttons, button, buttonPanel;
                buttonPanel = panel.createButtonPanel(dom);
                for (var i = 0, j = buttons.length; i < j; i++) {
                    button = buttons[i];
                    panel.registerInnerControl(button);
                    button.render(buttonPanel);
                }
                var buttonAlign = panel._buttonAlign;
                if (buttonAlign && buttonAlign != "center") {
                    $fly(doms.buttonPanel).addClass("button-panel-" + buttonAlign);
                }
            }
        }
    });

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
     * @component Base
     * @class 面板容器
     * <p>
     * 面板容器默认支持三种类型的边框：
     * <ul>
     *   <li>none：无边框</li>
     *   <li>normal：普通边框</li>
     *   <li>curve：圆角边框</li>
     * </ul>
     * </p>
     *
     * <p>
     * 面板容器可以支持显示标题栏，并且标题栏可以支持图标、工具按钮等。<br />
     * 面板容器可以支持收缩与展开的功能。<br />
     * </p>
     *
     * <p>
     * 注意：一般情况下，如果需要标题栏显示标题、在容器下方默认添加一些Button，则可以使用此组件，否则，请使用Container替代之，可以取得更高的性能。
     * </p>
     * @extends dorado.touch.AbstractPanel
     */
    dorado.touch.Panel = $extend(dorado.touch.AbstractPanel, /** @scope dorado.touch.Panel.prototype */ {
        ATTRIBUTES: /** @scope dorado.touch.Panel.prototype */ {
            className: {
                defaultValue: "d-panel"
            },

            width: {
                defaultValue: 300
            },

            /**
             * 面板的边框类型。
             * @attribute
             * @type String
             */
            border: {
                writeOnce: true,
                //none、normal、curve
                defaultValue: "curve"
            },

            /**
             * 面板的背景。
             * @attribute
             * @type String
             */
            background: {},

            /**
             * 是否显示CaptionBar。
             * @attribute
             * @type boolean
             */
            showCaptionBar: {
                defaultValue: true
            },

            /**
             * 显示的图标.
             * @attribute
             * @type String
             */
            icon: {
                skipRefresh: true,
                path: "_toolbar._icon"
            },

            /**
             * 显示的图标的ClassName.
             * @attribute
             * @type String
             */
            iconClass: {
                skipRefresh: true,
                path: "_toolbar._iconClass"
            },

            toolbarItems: {},

            /**
             * 是否显示关闭按钮.
             * @attribute
             * @type boolean
             */
            closeable: {
                //TODO fixit.
                defaultValue: false,
                setter: function(attr, value) {
                    var panel = this, toolbar = panel._toolbar, button;
                    panel._closeable = value;
                    if (toolbar) {
                        if (value) {
                            button = toolbar.getButton(panel._id + "_close");
                            if (button) {
                                $fly(button.dom).css("display", "");
                            } else {
                                panel.createCloseButton();
                            }
                        } else {
                            button = toolbar.getButton(panel._id + "_close");
                            if (button) {
                                $fly(button.dom).css("display", "none");
                            }
                        }
                    }
                }
            },

            /**
             * 当用户点击关闭以后要做的动作，默认是hide，可选值为hide和close。hide仅仅是隐藏该对话框，close会完全销毁该对话框，销毁以后该对话框不能再使用。
             * @attribute
             * @type String
             */
            closeAction: {
                defaultValue: "hide"
            },

            scrollbar: {
                defaultValue: false
            }
        },

        EVENTS: {
            close: {
                preventable: true,
                defaultFn: function() {
                    var panel = this;
                    panel.hide();
                    if (panel._closeAction == "close") {
                        panel.destroy();
                    }
                }
            }
        },

        createDom: function() {
            var panel = this, doms = {}, dom;
            dom = $DomUtils.xCreate({
                tagName: "div",
                className: panel._className,
                content: {
                    tagName: "div",
                    className: "panel-body",
                    contextKey: "body",
                    content: {
                        tagName: "div",
                        className: "content-panel",
                        contextKey: "contentPanel"
                    }
                }
            }, null, doms);

            panel._doms = doms;

            var caption = panel._caption;

            if (caption) {
                $fly(dom).addClass(panel._className + "-showcaptionbar");

                var toolbar = panel._toolbar = new dorado.touch.ToolBar({
                    caption: panel._caption,
                    icon: panel._icon,
                    items: panel._toolbarItems
                });

                if (doms.body) {
                    toolbar.render(doms.body.parentNode, doms.body);
                } else {
                    toolbar.render(dom, doms.contentPanel);
                }

                doms.toolbar = toolbar._dom;

                panel.registerInnerControl(toolbar);

                if (panel._collapseable) {
                    panel.createCollapseButton();
                    if (panel._collapsed) {
                        $fly(doms.body).css("display", "none");
                    }
                }
            }

            panel.initButtons(dom);

            if (panel._closeable) {
                panel.createCloseButton();
            }

            return dom;
        },

        createCloseButton: function() {
            var panel = this, toolbar = panel._toolbar;
            if (toolbar) {
                toolbar.addButton(new dorado.touch.SimpleButton({
                    id: panel._id + "_close",
                    onClick: function() {
                        panel.close();
                    },
                    className: "d-close-button"
                }));
            }
        },

        /**
         * 关闭面板。
         */
        close: function() {
            var panel = this;
            panel.fireEvent("onClose", panel);
        },

        onResize: function() {
            var panel = this, border = panel._border, dom = panel._dom, doms = panel._doms, height = panel._height;

            if (!panel._autoHeight/*!isNaN(height)*/) {
                if (panel._collapsed) {
                    if (border == "curve") {
                    }
                    $fly(dom).height("auto");
                } else {
                    var buttonPanelHeight = 0, toolbarHeight = 0;
                    if (doms.buttonPanel) {
                        buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                    }
                    if (doms.toolbar) {
                        toolbarHeight = $fly(doms.toolbar).outerHeight(true);
                    }
                    if (border == "curve") {
                        $fly(doms.body).outerHeight(jQuery(dom).height() - jQuery(doms.header).outerHeight() - jQuery(doms.footer).outerHeight() - toolbarHeight);
                        $fly(doms.contentPanel).outerHeight(jQuery(doms.body).height() - buttonPanelHeight);
                    } else {
                        $fly(doms.contentPanel).outerHeight(jQuery(dom).height() - toolbarHeight - buttonPanelHeight);
                    }
                }
            }

            $invokeSuper.call(this, arguments);
        },

        refreshDom: function(dom) {
            var panel = this, doms = panel._doms, border = panel._border;
            $invokeSuper.call(this, arguments);

            if (this.background) {
                doms.contentPanel.style.background = this.background;
            }

            panel.onResize();
        },

        doOnAttachToDocument: function() {
            $invokeSuper.call(this, arguments);
            if (this._scrollbar) {
                new iScroll(this.getContentContainer());
            }
        },

        getContentContainer: function() {
            var panel = this, doms = panel._doms;
            return doms.contentPanel;
        }
    });
})();