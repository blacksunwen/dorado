(function() {
	var TAB_CLOSEABLE_CLASS = "-closeable", TAB_DISABLED_CLASS = "-disabled", ICON_CLASS = "icon";

	/**
	 * @namespace
	 */
	dorado.widget.tab = {};

	/**
	 * 创建Tab的close的dom。
	 * @param tab 要创建close的dom的tab。
	 * @param dom tab的dom。
	 */
	function createTabCloseDom(tab, dom, doms) {
		var closeEl = $DomUtils.xCreate({
			tagName: "span",
			className: "close",
			contextKey: "close"
		}, null, doms);

		doms.tabLeft.appendChild(closeEl);

		jQuery(closeEl).click(function(event) {
			 if (!tab._disabled) {
				 tab.close();
			 }
			 event.stopImmediatePropagation();
		}).addClassOnHover("close-hover", null, function() {
			return !tab._disabled;
		}).addClassOnClick("close-click", null, function() {
			return !tab._disabled;
		});

		$fly(dom).addClass(tab._className + TAB_CLOSEABLE_CLASS);
	}

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class TabBar以及TabControl中使用的标签页。
	 * @shortTypeName Default
	 * @extends dorado.RenderableElement
	 */
	dorado.widget.tab.Tab = $extend(dorado.RenderableElement, /** @scope dorado.widget.tab.Tab.prototype */ {
		$className: "dorado.widget.tab.Tab",

		ATTRIBUTES: /** @scope dorado.widget.tab.Tab.prototype */ {
			className: {
				defaultValue: "tab"
			},
			/**
			 * Tab的Name，用来唯一表示TabBar或者TabPage里面的一个Tab。
			 * 可以指定，也可以不指定，不指定则默认为空。
			 * @type String
			 * @attribute
			 */
			name: {},

			/**
			 * Tab上显示的文字。
			 * @type String
			 * @attribute
			 */
			caption: {},

			/**
			 * Tab是否可以被关闭，也就是是否显示右侧的关闭按钮。
			 * @type boolean
			 * @default false
			 * @attribute
			 */
			closeable: {},

			/**
			 * Tab上显示的图标所在的具体路径，图标大小建议是20*20或者16*16。
			 * @type String
			 * @attribute
			 */
			icon: {},

			/**
			 * icon使用的className。
			 * @type String
			 * @attribute
			 */
			iconClass: {},

			/**
			 * Tab是否被禁用，默认为false。
			 * @type boolean
			 * @default false
			 * @attribute
			 */
			disabled: {
				setter: function(value) {
					var tab = this, tabbar = tab._parent;
					if (tabbar) {
						if (value) {
							tabbar.disableTab(tab);
						} else {
							tabbar.enableTab(tab);
						}
					} else {
						tab._disabled = value;
					}
				}
			},

			/**
			 * Tab是否可见，默认值为true。
			 * @type boolean
			 * @default true
			 * @attribute
			 */
			visible: {
				defaultValue: true,
				skipRefresh: true,
				setter: function(value) {
					var tab = this, tabbar = tab._parent;
					if (tabbar && tab) {
						tabbar.doSetTabVisible(tab, value);
					}
					tab._visible = value;
				}
			},

			/**
			 * 该Tab所属的TabBar，由系统指定，擅自修改该属性会导致TabBar无法正常运行。
			 * @type dorado.widget.TabBar
			 * @attribute
			 */
			parent: {},

			/**
			 * 用户自定义数据。
			 * @type Object
			 * @attribute
			 */
			userData: {}
		},

		constructor: function(config) {
			$invokeSuper.call(this, arguments);

			if (config) {
				this.set(config);
			}
		},

		_createIconSpan: function() {
			var tab = this, doms = tab._doms;
			var iconEl = document.createElement("span");
			iconEl.className = "icon";

			doms.icon = iconEl;

			$fly(iconEl).prependTo(doms.tabLeft);

			$DomUtils.setBackgroundImage(iconEl, tab._icon);
		},

		refreshDom: function(dom) {
			var tab = this, closeable = tab._closeable, disabled = tab._disabled, visible = tab._visible, doms = tab._doms,
				captionEl = doms.caption, closeEl = doms.close, width = tab._width, tabbar = tab._parent, tabMinWidth;

			$DomUtils.disableUserSelection(dom);
			$fly(captionEl).text(tab._caption);

			if (closeable) {
				if (!closeEl) {
					createTabCloseDom(tab, dom, doms);
				}
			} else {
				if (closeEl) {
					$fly(closeEl).remove();
					$fly(dom).removeClass(tab._className + TAB_CLOSEABLE_CLASS);
				}
			}

			$fly(dom).css("display", visible ? "" : "none");

			if (disabled) {
				$fly(dom).addClass(tab._className + TAB_DISABLED_CLASS);
			} else {
				$fly(dom).removeClass(tab._className + TAB_DISABLED_CLASS);
			}

			jQuery(dom).addClassOnHover(tab._className + "-hover", null, function() {
				return !tab._disabled;
			});

			if (tabbar && !width) {
				tabMinWidth = tabbar._tabMinWidth;
				if (dom.offsetWidth < tabMinWidth) {
					width = tab._width = tabMinWidth;
				}
			}

			if (width) {
				$fly(dom).outerWidth(width);
				var leftEl = $fly(doms.tabLeft);
				leftEl.width(jQuery(dom).width() - (parseInt(leftEl.css("margin-left"), 10) || 0) - (parseInt(leftEl.css("margin-right"), 10) || 0)
					- (parseInt(leftEl.css("padding-left"), 10) || 0) - (parseInt(leftEl.css("padding-right"), 10) || 0));
			}

			var icon = tab._icon, iconCls = tab._iconClass;

			if (!icon && !iconCls && doms.icon) {
				$fly(doms.icon).css("display", "none");
			} else {
				if (doms.icon) {
					$fly(doms.icon).prop("className", ICON_CLASS).css("display", "");
				}
				if ((icon || iconCls) && !doms.icon) {
					tab._createIconSpan();
				}

				if (icon) {
					$DomUtils.setBackgroundImage(doms.icon, icon);
				} else if (doms.icon) {
					$fly(doms.icon).css("background-image", "none");
				}

				if (iconCls) {
					$fly(doms.icon).addClass(iconCls);
				}
			}
		},

		/**
		 * 关闭并移除该Tab。
		 */
		close: function() {
			var tab = this, tabbar = tab._parent;
			if (tabbar) {
				tabbar.removeTab(tab);
			}
		},

		/**
		 * 对Tab而言，createDom以后无需refreshDom，所以覆写了该方法。
		 * @protected
		 */
		getDom: function() {
			if (!this._dom) {
				this._dom = this.createDom();
			}
			return this._dom;
		},

		/**
		 * 因为Tab的render需要按照顺序来render，所以重写了render方法。
		 * @param {HtmlElement} ctEl 要附加到的Element。
		 * @param {int} [index] 要添加到到的Element的索引。
		 * @protected
		 */
		render: function(ctEl, index) {
			var dom = this.getDom();
			if (!dom) {
				return;
			}
			if (!ctEl) {
				ctEl = document.body;
			}
			if (dom.parentNode != ctEl) {
				if (index != null) {
					var refEl = ctEl.childNodes[index];
					if (!refEl) {
						ctEl.appendChild(dom);
					} else {
						ctEl.insertBefore(dom, refEl);
					}
				} else {
					ctEl.appendChild(dom);
				}
			}
			this._rendered = true;
		},

		createDom: function() {
			var tab = this, doms = {}, dom = $DomUtils.xCreate({
				tagName: "li",
				className: tab._className,
				content: [
					{
						tagName: "span",
						className: "tab-left",
						contextKey: "tabLeft",
						content: {
							tagName: "span",
							className: "caption",
							content: tab._caption,
							contextKey: "caption"
						}
					},
					{
						tagName: "span",
						className: "tab-right",
						contextKey: "tabRight"
					}
				]
			}, null, doms);

			tab._doms = doms;

			$fly(dom).click(function() {
				var tabbar = tab._parent, disabled = tab._disabled;
				if (tabbar) {
					if (!disabled) {
						tabbar.doChangeCurrentTab(tab);
					}
				}
			}).addClass(tab._exClassName ? tab._exClassName : "");

			if (tab._closeable) {
				createTabCloseDom(tab, dom, doms);
			}

			jQuery(dom).addClassOnHover(tab._className + "-hover", null, function() {
				return !tab._disabled;
			}).bind("contextmenu", function(event) {
				event = jQuery.event.fix(event || window.event);
				event.preventDefault();
				event.returnValue = false;
				var tabbar = tab._parent, arg = {
					tab: tab,
					event: event
				};
				tabbar._contextMenuTab = tab;
				tabbar.fireEvent("onTabContextMenu", tabbar, arg);

				return false;
			});

			if (tab._icon || tab._iconClass) {
				tab._createIconSpan();
			}

			return dom;
		},

        destroy: function() {
            var tab = this, dom = tab._dom;
            if (dom) $fly(dom).remove();
        },

		getControl: function() {
			return (this.doGetControl) ? this.doGetControl() : null;
		},

        getListenerScope: function() {
			if (this._parent && this._parent._view) {
				return this._parent._view;
			}
			return this;
		}
	});

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class 面板标签页。
	 * @shortTypeName Control
	 * @extends dorado.widget.tab.Tab
	 */
	dorado.widget.tab.ControlTab = $extend(dorado.widget.tab.Tab, /** @scope dorado.widget.tab.ControlTab.prototype */ {
		$className: "dorado.widget.tab.ControlTab",
		ATTRIBUTES: /** @scope dorado.widget.tab.ControlTab.prototype */ {
			/**
			 * 该Tab对应的Control。
			 * @type dorado.widget.Control
			 * @attribute
			 */
			control: {
				setter: function(value) {
					if (value && value.constructor == Object.prototype.constructor) {
						value = dorado.Toolkits.createInstance("widget", value);
					}
					this._control = value;
				}
			}
		},
		
		destroy: function() {
			if (this._control) {
				this._control && this._control.destroy();
			}
			$invokeSuper.call(this);
		},
		
		doGetControl: function() {
			var result = this._control;
			if (!result) {
				result = this._control = new dorado.widget.Control();
			}
			return result;
		}
	});

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class IFrame标签页。
	 * @shortTypeName IFrame
	 * @extends dorado.widget.tab.ControlTab
	 */
	dorado.widget.tab.IFrameTab = $extend(dorado.widget.tab.ControlTab, /** @scope dorado.widget.tab.IFrameTab.prototype */ {
		$className: "dorado.widget.tab.IFrameTab",
		ATTRIBUTES: /** @scope dorado.widget.tab.IFrameTab.prototype */ {
			/**
			 * 该Tab对应的path。
			 * @type String
			 * @attribute
			 */
			path: {
				path: "_control.path"
			}
		},
		doGetControl: function() {
			var tab = this, iframe = this._control;
			if (!iframe) {
				iframe = this._control = new dorado.widget.IFrame({
					path: tab._path
				});
			}
			return iframe;
		}
	});
})();
