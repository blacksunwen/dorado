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
 * @extends dorado.widget.Container
 */
dorado.widget.AbstractPanel = $extend(dorado.widget.Container, /** @scope dorado.widget.AbstractPanel.prototype */ {
	$className: "dorado.widget.AbstractPanel",
	
	ATTRIBUTES: /** @scope dorado.widget.AbstractPanel.prototype */ {
	
		/**
		 * 显示的标题。
		 * @attribute
		 * @type String
		 */
		caption: {
			skipRefresh: true,
			path: "_captionBar.caption"
		},
		
		/**
		 * 显示在对话框下方的按钮。
		 * @attribute writeBeforeReady
		 * @type dorado.widget.Button[]|Object[]
		 */
		buttons: {
			writeBeforeReady: true,
			innerComponent: "Button"
		},
		
		/**
		 * button的显示位置，可选值：center、left、right，默认值是center。
		 * @attribute
		 * @type String
		 * @default "center"
		 */
		buttonAlign: {
			defaultValue: "center",
			skipRefresh: true,
			setter: function(value) {
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
		},
		
		/**
		 * 是否可折叠
		 * @type boolean
		 * @default false
		 * @attribute
		 */
		collapseable: {
			setter: function(value) {
				var panel = this, captionBar = panel._captionBar, button;
				panel._collapseable = value;
				if (captionBar) {
					if (value) {
						button = captionBar.getButton(panel._id + "_collapse");
						if (button) {
							$fly(button._dom).css("display", "");
						} else {
							panel._createCollapseButton();
						}
					} else {
						button = captionBar.getButton(panel._id + "_collapse");
						if (button) {
							$fly(button._dom).css("display", "none");
						}
					}
				}
			}
		},
		
		/**
		 * 是否处于已折叠的状态。
		 * @type boolean
		 * @default false
		 * @attribute
		 */
		collapsed: {
			getter: function(attr, value) {
				var panel = this;
				if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
					return panel._parent._collapsed;
				} else {
					return panel._collapsed;
				}
			},
			setter: function(value) {
				var panel = this;
				if (panel._rendered) {
					panel.doSetCollapsed(value);
				} else {
					panel._collapsed = value;
				}
			}
		}
	},
	
	EVENTS: /** @scope dorado.widget.AbstractPanel.prototype */ {
		/**
		 * 在容器折叠或者展开之前触发，只有当collapseable为true的时候才会触发该事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeCollapsedChange: {},
		/**
		 * 在容器折叠或者展开之后触发，只有当collapseable为true的时候才会触发该事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCollapsedChange: {}
	},
	
	/**
	 * @private
	 */
	toggleCollapsed: function() {
		var panel = this, collapsed = panel.get("collapsed");
		panel.doSetCollapsed(!collapsed);
	},

	/**
	 * @private
	 */
	doSetCollapsed: function(collapsed) {
	
		function onCollapsedChange(panel, collapsed) {
			panel._children.each(function(child) {
				if (child instanceof dorado.widget.Control) {
					child.setActualVisible(!collapsed);
				}
			});
            var buttons = panel._buttons;
            if (buttons) {
                for (var i = 0, j = buttons.length; i < j; i++) {
                    var button = buttons[i];
                    button.setActualVisible(!collapsed);
                }
            }
			panel.fireEvent("onCollapsedChange", panel);
			panel._doOnResize(collapsed);
			panel.notifySizeChange();
		}
		
		var panel = this, dom = panel._dom, doms = panel._doms, collapseButton = panel._collapseButton, eventArg = {};
		panel.fireEvent("beforeCollapsedChange", panel, eventArg);
		if (eventArg.processDefault === false) return;
		if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
			var direction = panel._parent._direction;
			if (collapseButton) {
				collapseButton.set("iconClass", collapsed ? ("expand-icon-" + direction) : ("collapse-icon-" + direction));
			}
			panel._parent.doSetCollapsed(collapsed, function() {
				onCollapsedChange(panel, collapsed);
			}, true);
		} else {
			panel._collapsed = collapsed;
            var orginalZIndex;
			if (panel._rendered) {
				if (collapsed) {
					$fly(doms.body).safeSlideOut({
						direction: "b2t",
						start: function() {
                            orginalZIndex = dom.style.zIndex;
							$fly(dom).bringToFront();
						},
						step: function() {
						},
						complete: function() {
							$fly(dom).addClass(panel._className + "-collapsed");
							if (collapseButton) {
								collapseButton.set("iconClass", "expand-icon");
							}
							onCollapsedChange(panel, collapsed);
                            dom.style.zIndex = orginalZIndex || "";
						}
					});
				} else {
					$fly(doms.body).safeSlideIn({
						direction: "t2b",
						start: function() {
                            orginalZIndex = dom.style.zIndex;
							$fly(dom).bringToFront().removeClass(panel._className + "-collapsed");
						},
						step: function() {
						},
						complete: function() {
                            orginalZIndex = null;
							if (collapseButton) {
								collapseButton.set("iconClass", "collapse-icon");
							}
							onCollapsedChange(panel, collapsed);
                            dom.style.zIndex = orginalZIndex || "";
						}
					});
				}
			}
		}
	},

	_createButtonPanel: function(dom) {
		var panel = this, doms = panel._doms, buttonPanel = document.createElement("div");
		buttonPanel.className = "button-panel";
		
		doms.buttonPanel = buttonPanel;
		if (doms.body) {
			$fly(doms.body).append(buttonPanel);
		} else {
			$fly(dom).append(buttonPanel);
		}
		return buttonPanel;
	},

	doOnResize: function(collapsed) {
		this._doOnResize && this._doOnResize(collapsed);
		$invokeSuper.call(this, arguments);
	},

	initButtons: function(dom) {
		var panel = this, doms = panel._doms;
		if (panel._buttons) {
			var buttons = panel._buttons, button, buttonPanel;
			buttonPanel = panel._createButtonPanel(dom);
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
 * @extends dorado.widget.AbstractPanel
 */
dorado.widget.Panel = $extend(dorado.widget.AbstractPanel, /** @scope dorado.widget.Panel.prototype */ {
	$className: "dorado.widget.Panel",
	
	ATTRIBUTES: /** @scope dorado.widget.Panel.prototype */ {
		className: {
			defaultValue: "d-panel"
		},
		
		width: {
			defaultValue: 300
		},
		
		/**
		 * 面板的边框类型。
		 * none、normal、curve
		 * @attribute
		 * @default "normal"
		 * @type String
		 */
		border: {
			writeOnce: true,
			defaultValue: "normal"
		},
		
		/**
		 * 面板的背景。
		 * @attribute
		 * @type String
		 */
		background: {},
		
		/**
		 * 是否显示标题栏。<br />
		 * 该属性没有默认值，当设定了caption属性以后，则该属性为true，否则为false。<br />
		 * 当设定了caption属性，又不想显示标题栏，设置该属性为false即可。
		 * @attribute writeBeforeReady
		 * @type boolean
		 */
		showCaptionBar: {
			writeBeforeReady: true
		},
		
		/**
		 * 显示的图标.
		 * @attribute
		 * @type String
		 */
		icon: {
			skipRefresh: true,
			path: "_captionBar.icon"
		},
		
		/**
		 * 显示的图标的ClassName.
		 * @attribute
		 * @type String
		 */
		iconClass: {
			skipRefresh: true,
			path: "_captionBar.iconClass"
		},
		
		/**
		 * 显示在标题栏右侧的工具按钮。
		 * @attribute
		 * @type dorado.util.KeyedArray
		 */
		tools: {
			innerComponent: "SimpleIconButton"
		},

		/**
		 * 是否显示最大化按钮。
		 * @attribute
		 * @default true
		 * @type boolean
		 */
		maximizeable: {
			setter: function(value) {
				var panel = this, captionBar = panel._captionBar, button;
				panel._maximizeable = value;
				if (captionBar) {
					if (value) {
						button = captionBar.getButton(panel._id + "_maximize");
						if (button) {
							$fly(button._dom).css("display", "");
						} else {
							panel._createMaximizeButton();
						}
					} else {
						button = captionBar.getButton(panel._id + "_maximize");
						if (button) {
							$fly(button._dom).css("display", "none");
						}
					}
				}
			}
		},

		/**
		 * 是否已经最大化。
		 * @attribute
		 * @default false
		 * @type boolean
		 */
		maximized: {},

        /**
		 * 是否显示关闭按钮.
		 * @attribute
		 * @type boolean
		 */
		closeable: {
			setter: function(value) {
				var panel = this, captionBar = panel._captionBar, button;
				panel._closeable = value;
				if (captionBar) {
					if (value) {
						button = captionBar.getButton(panel._id + "_close");
						if (button) {
							$fly(button._dom).css("display", "");
						} else {
							panel._createCloseButton();
						}
					} else {
						button = captionBar.getButton(panel._id + "_close");
						if (button) {
							$fly(button._dom).css("display", "none");
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
		}
	},

	EVENTS: {
		/**
		 * 在组件最大化之前触发此事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeMaximize: {},

		/**
		 * 在组件最大化之后触发此事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onMaximize: {},

        /**
         * 在组件关闭之前触发。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        beforeClose: {},

        /**
         * 在组件关闭之后触发。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        onClose: {}
	},

	createDom: function() {
		var panel = this, doms = {}, border = panel._border, dom;
		if ((dorado.Browser.msie || !$setting["widget.panel.useCssCurveBorder"]) && border == "curve") {
			dom = $DomUtils.xCreateElement({
				tagName: "div",
				className: panel._className,
				content: [{
					tagName: "div",
					className: "panel-header-left",
					contextKey: "header",
					content: {
						tagName: "div",
						className: "panel-header-right"
					}
				}, {
					tagName: "div",
					className: "panel-body-left",
					contextKey: "body",
					content: {
						tagName: "div",
						className: "panel-body-right",
						content: [{
							tagName: "div",
							className: "panel-body",
							contextKey: "body",
							content: {
								tagName: "div",
								className: "content-panel",
								contextKey: "contentPanel"
							}
						}]
					}
				}, {
					tagName: "div",
					className: "panel-footer-left",
					contextKey: "footer",
					content: {
						tagName: "div",
						className: "panel-footer-right"
					}
				}]
			}, null, doms);
			
			panel._doms = doms;
		} else {
			dom = $DomUtils.xCreateElement({
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
		}
		
		var caption = panel._caption, showCaptionBar = panel._showCaptionBar;
		
		if (showCaptionBar !== false && (caption || showCaptionBar)) {
			$fly(dom).addClass(panel._className + "-showcaptionbar");
			var tools = panel._tools, toolButtons = [];
			
			if (tools instanceof Array) {
				for (var i = 0, j = tools.length; i < j; i++) {
					var tool = tools[i];
					if (tool) {
						toolButtons.push(tool);
					}
				}
			}
			
			var captionBar = panel._captionBar = new dorado.widget.CaptionBar({
				caption: panel._caption,
				icon: panel._icon,
				buttons: toolButtons
			});
			
			if (doms.body) {
				captionBar.render(doms.body.parentNode, doms.body);
			} else {
				captionBar.render(dom, doms.contentPanel);
			}
			
			doms.captionBar = captionBar._dom;
			
			panel.registerInnerControl(captionBar);
			
			if (panel._collapseable) {
				panel._createCollapseButton();
				if (panel._collapsed) {
                    panel._children.each(function(child) {
                        if (child instanceof dorado.widget.Control) {
                            child.setActualVisible(false);
                        }
                        var buttons = panel._buttons;
                        if (buttons) {
                            for (var i = 0, j = buttons.length; i < j; i++) {
                                var button = buttons[i];
                                button.setActualVisible(false);
                            }
                        }
                    });
                    $fly(doms.body).css("display", "none");
				}
			}
		}
		
		panel.initButtons(dom);
		
		$fly(dom).toggleClass(panel._className + "-normalborder", border == "normal").toggleClass(panel._className + "-curveborder", border == "curve");
		
		if (panel._collapsed) {
			$fly(dom).addClass(panel._className + "-collapsed");
		}

        if (panel._closeable) {
			panel._createCloseButton();
		}

		if (panel._maximizeable) {
			panel._createMaximizeButton();
		}

		return dom;
	},
	
	_createCollapseButton: function() {
		var panel = this;
		if (!panel._captionBar) {
			return;
		}
		
		var collapseButton = panel._collapseButton = new dorado.widget.SimpleIconButton({
			id: panel._id + "_collapse",
			iconClass: panel._collapsed ? "expand-icon" : "collapse-icon",
			onClick: function() {
				panel.toggleCollapsed();
			}
		});
		
		if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
			var direction = panel._parent._direction;
			collapseButton.set("iconClass", panel._collapsed ? "expand-icon-" + direction : "collapse-icon-" + direction);
		}
		
		panel._captionBar.addButton(collapseButton, 0);
	},

    _createCloseButton: function() {
		var panel = this, captionBar = panel._captionBar;
		if (captionBar) {
			captionBar.addButton(new dorado.widget.SimpleButton({
				id: panel._id + "_close",
				listener: {
					onClick: function() {
						panel.close();
					}
				},
				className: "d-close-button"
			}));
		}
	},

	/**
	 * 关闭面板。
	 */
	close: function() {
		var panel = this, eventArg = {};
		panel.fireEvent("beforeClose", panel, eventArg);
		if (eventArg.processDefault === false) return;
		panel.hide && panel.hide();
		panel.fireEvent("onClose", panel);
		if (panel._closeAction == "close") {
			panel.destroy();
		}
	},

	_doOnResize: function(collapsed) {
		var panel = this, border = panel._border, dom = panel._dom, doms = panel._doms, height = panel.getRealHeight();
		if (typeof height == "number" && height > 0) {
			if (collapsed == undefined) {
				collapsed = panel._collapsed;
			}
			if (collapsed) {
				if (border == "curve") {
					if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
						$fly(doms.body).height("auto");
					}
				}
				$fly(dom).height("auto");
			} else {
				var buttonPanelHeight = 0, captionBarHeight = 0;
				if (doms.buttonPanel) {
					buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
				}
				if (doms.captionBar) {
					captionBarHeight = $fly(doms.captionBar).outerHeight(true);
				}
				if (border == "curve") {
                    var bodyHeight = height - jQuery(doms.header).outerHeight() - jQuery(doms.footer).outerHeight() - captionBarHeight;
					$fly(doms.body).outerHeight(bodyHeight);
					$fly(doms.contentPanel).outerHeight(bodyHeight - buttonPanelHeight);
				} else {
					$fly(doms.contentPanel).outerHeight(height - captionBarHeight - buttonPanelHeight);
				}
				$fly(dom).height("auto");
			}
		}
	},
	
	refreshDom: function(dom) {
		var panel = this, doms = panel._doms, border = panel._border;
		
		$invokeSuper.call(this, arguments);
		
		if (this._background) {
			doms.contentPanel.style.background = this._background;
		}
		
		$fly(dom).toggleClass(this._className + "-normalborder", border == "normal").toggleClass(this._className + "-curveborder", border == "curve");
	},
	
	getContentContainer: function() {
		var panel = this, doms = panel._doms;
		return doms.contentPanel;
	},

	maximizeRestore: function() {
		var panel = this, dom = panel._dom, doms = panel._doms;
		if (dom) {
			$fly(doms.contentPanel).css("display", "");

			if (panel._maximized) {

				$fly(dom).unfullWindow({
					callback: function() {
						panel._maximized = false;
						panel._width = panel._originalWidth;
						panel._height = panel._originalHeight;
						panel._left = panel._originalLeft;
						panel._top = panel._originalTop;
						panel.resetDimension();
						panel.refresh();
					}
				});

				$fly(dom).draggable("enable");

				var captionBar = panel._captionBar;
				if (captionBar) {
					var button = captionBar.getButton(panel._id + "_maximize");
					if (button) {
						$fly(button._dom).attr("className", "d-maximize-button");
						button._className = "d-maximize-button";
					}
				}
			}
		}
	},

	maximize: function() {
		var panel = this, dom = panel._dom;
		if (dom) {
			var eventArg = {};
			panel.fireEvent("beforeMaximize", panel, eventArg);
			if (eventArg.processDefault === false) {
				return;
			}

			panel._originalWidth = panel._width;
			panel._originalHeight = panel._height;
			panel._originalLeft = panel._left;
			panel._originalTop = panel._top;

			var captionBar = panel._captionBar;
			if (captionBar) {
				var button = captionBar.getButton(panel._id + "_maximize");
				if (button) {
					$fly(button._dom).attr("className", "d-restore-button");
					button._className = "d-restore-button";
				}
			}

			$fly(dom).fullWindow({
				modifySize: false,
				callback: function(docSize) {
					panel._maximized = true;
					panel.set(docSize);
					panel.refresh();
				}
			});

			$fly(dom).draggable("disable");

			panel.fireEvent("onMaximize", panel);
		}
	},

	_createMaximizeButton: function() {
		var panel = this, captionBar = panel._captionBar;
		if (captionBar) {
			captionBar.addButton(new dorado.widget.SimpleButton({
				id: panel._id + "_maximize",
				className: "d-maximize-button",
				listener: {
					onClick: function() {
						if (!panel._maximized) {
							panel.maximize();
						} else {
							panel.maximizeRestore();
						}
					}
				}
			}), panel._id + "_close");
		}
	}
});
