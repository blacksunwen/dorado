/**
 * @namespace dorado.widget.toolbar
 */
dorado.widget.toolbar = {};

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Base
 * @class 工具栏
 * @extends dorado.widget.Control
 */
dorado.widget.ToolBar = $extend(dorado.widget.Control, /** @scope dorado.widget.ToolBar.prototype */ {
	$className: "dorado.widget.ToolBar",
	_inherentClassName: "i-toolbar",
	ATTRIBUTES: /** @scope dorado.widget.ToolBar.prototype */ {
		className: {
			defaultValue: "d-toolbar"
		},
		
		height: {
			independent: true,
			readOnly: true
		},
		
		/**
		 * 工具栏中的子组件集合。可以使用Array、dorado.util.KeyedArray类型的数据来设置，但是取得的值是dorado.util.KeyedArray类型的。
		 * @attribute
		 * @type dorado.util.KeyedArray
		 */
		items: {
			setter: function(value) {
				var toolbar = this, items = toolbar._items, rendered = toolbar._rendered, i, l, item;
				if (items) {
					toolbar.clearItems();
				}
				if (!items) {
					toolbar._items = items = new dorado.util.KeyedArray(function(value) {
						return value._id;
					});
				}
				if (value) {
					if (value.constructor == Array.prototype.constructor) {
						for (i = 0, l = value.length; i < l; i++) {
							item = toolbar.createItem(value[i]);
							items.insert(item);
						}
						if (rendered) {
							toolbar.doRenderItems();
						}
					} else if (value instanceof dorado.util.KeyedArray) {
						for (i = 0, l = value.size; i < l; i++) {
							item = toolbar.createItem(value.get(i));
							items.append(item);
						}
						if (rendered) {
							toolbar.doRenderItems();
						}
					}
				}
			}
		},
		
		/**
		 * 当内容超出以后，固定在右侧的内容是始终显示还是隐藏。
		 * @attribute
		 * @type boolean
		 */
		fixRight: {
			defaultValue: false
		}
	},
	
	createDom: function() {
		var toolbar = this, doms = {}, dom = $DomUtils.xCreateElement({
			tagName: "div",
			className: "i-toolbar " + toolbar._className,
			content: [{
				tagName: "div",
				className: "toolbar-left-wrap",
				contextKey: "toolbarLeftWrap",
				content: [{
					tagName: "div",
					className: "toolbar-left",
					contextKey: "toolbarLeft"
				}]
			}, {
				tagName: "div",
				className: "toolbar-right",
				contextKey: "toolbarRight"
			}]
		}, null, doms);
		
		toolbar._doms = doms;
		
		if (toolbar._fixRight) {
			$fly(dom).addClass("i-toolbar-fixright " + toolbar._className + "-fixright");
		}
		
		toolbar.doRenderItems();
		
		return dom;
	},
	
	/**
	 * 创建工具栏中的子组件。
	 * @param {Object} config 子组件的配置信息。
	 * @return {dorado.widget.Control} 创建好的Control。
	 * @protected
	 */
	createItem: function(config) {
		if (!config) return null;
		if (typeof config == "string" || config.constructor == Object.prototype.constructor) {
			var result = dorado.Toolkits.createInstance("toolbar,widget", config);
			result._parent = result._focusParent = this;
			return result;
		} else {
			config._parent = config._focusParent = this;
			return config;
		}
	},
	
	/**
	 * 为toolbar插入子组件。
	 * @param {Object|dorado.widget.Control} item 要插入的子组件。
	 * @param {int} [index] 要插入的子组件的index，如果不指定，默认会附加到最后。
	 * @return {dorado.widget.Control} 插入成功的Item，如果不成功，返回null。
	 */
	addItem: function(item, index) {
		var toolbar = this, items = toolbar._items;
		if (!item) return null;
		if (!items) {
			items = toolbar._items = new dorado.util.KeyedArray(function(value) {
				return value._id;
			});
		}
		item = toolbar.createItem(item);
		if (toolbar._rendered) {
			var refDom = null, doms = toolbar._doms;
			if (typeof index == "number") {
				var refItem = items[index];
				refDom = refItem._dom;
			}
			items.insert(item, index);
			item.render(doms.toolbarLeft);
			toolbar.registerInnerControl(item);
			$fly(item._dom).addClass("i-toolbar-item");
		} else {
			items.insert(item, index);
		}
		
		return item;
	},
	
	/**
	 * 移除工具栏中的子组件。
	 * @param {dorado.widget.Control|int} item 要移除的子组件或者子组件的索引。
	 */
	removeItem: function(item) {
		var toolbar = this, items = toolbar._items;
		if (items && item !== undefined) {
			var realItem = item;
			if (typeof item == "number") {
				realItem = items.get(item);
				items.removeAt(item);
			} else {
				items.remove(item);
			}
			realItem.destroy();
			toolbar.unregisterInnerControl(realItem);
		}
	},
	
	/**
	 * 清除工具栏中所有的子组件。
	 */
	clearItems: function() {
		var toolbar = this, items = toolbar._items, afterFill = false;
		for (var i = 0, j = items.size; i < j; i++) {
			var item = items.get(i);
			if (!(item instanceof dorado.widget.toolbar.Fill)) {
				toolbar.unregisterInnerControl(item);
				item.destroy();
			}
		}
		items.clear();
	},
	
	doRenderItems: function() {
		var toolbar = this, doms = toolbar._doms, items = toolbar._items || {}, afterFill = false;
		for (var i = 0, j = items.size; i < j; i++) {
			var item = items.get(i);
			if (item instanceof dorado.widget.toolbar.Fill) {
				afterFill = true;
			} else {
				toolbar.registerInnerControl(item);
				if (!afterFill) {
					item.render(doms.toolbarLeft);
				} else {
					item.render(doms.toolbarRight);
				}
				$fly(item._dom).addClass("i-toolbar-item");
			}
		}
	},
	
	convertItem: function(item, overflowMenu) {
		if (item instanceof dorado.widget.toolbar.Button) {
			overflowMenu.addItem({
				caption: item._caption,
				submenu: item._menu,
				listener: {
					onClick: function() {
						item.fireEvent("onClick", item);
					}
				}
			});
		} else if (item instanceof dorado.widget.toolbar.Separator) {
			overflowMenu.addItem("-");
		}
		item._visible = false;
		$fly(item._dom).css("visibility", "hidden");
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
	},
	
	doOnResize: function() {
		$invokeSuper.call(this, arguments);
		var toolbar = this, dom = toolbar._dom, doms = toolbar._doms, overflowMenu = toolbar._overflowMenu, overflowButton = toolbar._overflowButton, items = toolbar._items, lastChild = doms.toolbarLeft.lastChild, overflow = false;
		
		if (items && lastChild) {
			var leftRealWidth = lastChild.offsetWidth + lastChild.offsetLeft, leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
			
			overflow = leftRealWidth > leftVisibleWidth;
		}
		
		if (overflow) {
			$fly(dom).addClass("i-toolbar-overflow " + toolbar._className + "-overflow");
			
			if (!overflowMenu) {
				overflowMenu = toolbar._overflowMenu = new dorado.widget.Menu();
				
				overflowButton = toolbar._overflowButton = new dorado.widget.SimpleButton({
					className: "overflow-button",
					menu: overflowMenu
				});
				overflowButton.render(doms.toolbarRight);
				toolbar.registerInnerControl(overflowButton);
			} else {
				overflowMenu.clearItems();
			}
			
			var leftWidthSum = 0, startHideIndex = -1, item, i, j, afterFill;
			
			if (toolbar._fixRight) {
				leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
				for (i = 0, j = items.size; i < j; i++) {
					item = items.get(i);
					if (item instanceof dorado.widget.toolbar.Fill) {
						break;
					}
					item._visible = true;
					$fly(item._dom).css("visibility", "");
					
					leftWidthSum += $fly(item._dom).outerWidth(true);
					if (leftWidthSum >= leftVisibleWidth) {
						startHideIndex = i;
						break;
					}
				}
				
				if (startHideIndex > -1) {
					for (i = startHideIndex, j = items.size; i < j; i++) {
						item = items.get(i);
						if (item instanceof dorado.widget.toolbar.Fill) {
							break;
						}
						toolbar.convertItem(item, overflowMenu);
					}
				}
			} else {
				afterFill = false;
				for (i = 0, j = items.size; i < j; i++) {
					item = items.get(i);
					if (afterFill) {
						if (item._dom && (item._dom.parentNode == doms.toolbarRight)) {
							doms.toolbarLeft.appendChild(item._dom);
						}
					}
					if (item instanceof dorado.widget.toolbar.Fill) {
						afterFill = true;
					}
				}
				
				leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
				
				for (i = 0, j = items.size; i < j; i++) {
					item = items.get(i);
					item._visible = true;
					$fly(item._dom).css("visibility", "");
					
					leftWidthSum += $fly(item._dom).outerWidth(true);
					if (leftWidthSum >= leftVisibleWidth) {
						startHideIndex = i;
						break;
					}
				}
				
				if (startHideIndex > -1) {
					for (i = startHideIndex, j = items.size; i < j; i++) {
						item = items.get(i);
						if (item instanceof dorado.widget.toolbar.Fill) {
							break;
						}
						toolbar.convertItem(item, overflowMenu);
					}
				}
			}
		} else {
			$fly(dom).removeClass("i-toolbar-overflow " + toolbar._className + "-overflow");
			if (!items) return;
			if (!toolbar._fixRight) {
				afterFill = false;
				for (i = 0, j = items.size; i < j; i++) {
					item = items.get(i);
					if (afterFill) {
						if (item._dom && (item._dom.parentNode == doms.toolbarLeft)) {
							doms.toolbarRight.appendChild(item._dom);
						}
					}
					if (item instanceof dorado.widget.toolbar.Fill) {
						afterFill = true;
					}
				}
			}
			for (i = 0, j = items.size; i < j; i++) {
				item = items.get(i);
				item._visible = true;
				$fly(item._dom).css("visibility", "");
			}
		}
	}
	
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component ToolBar
 * @class 工具栏上的分割条。
 * @shortTypeName | ; - ; Separator
 * @extends dorado.widget.Control
 */
dorado.widget.toolbar.Separator = $extend(dorado.widget.Control, {
	$className: "dorado.widget.toolbar.Separator",
    _inherentClassName: "i-toolbar-sep",
	ATTRIBUTES: {
		className: {
			defaultValue: "d-toolbar-sep"
		}
	},
	
	createDom: function() {
		var separator = this, dom;
		dom = document.createElement("span");
		dom.className = separator._className;
		return dom;
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component ToolBar
 * @class 工具栏上的按钮。
 * @shortTypeName Default ; Button
 * @extends dorado.widget.Button
 */
dorado.widget.toolbar.Button = $extend(dorado.widget.Button, {
	$className: "dorado.widget.toolbar.Button",
	
	ATTRIBUTES: {
		className: {
			defaultValue: "d-toolbar-button"
		},
		
		/**
		 * 该属性为虚拟属性，指向MenuBarItem对应的Menu的items属性。
		 * @type dorado.util.KeyedArray
		 * @attribute
		 */
		items: {
			getter: function() {
				if (this._menu) {
					return this._menu.get("items");
				}
				return null;
			},
			setter: function(value) {
				if (value.constructor == Array.prototype.constructor) {
					this._menu = new dorado.widget.Menu({
						items: value
					});
				}
			}
		}
	},
	
	createDom: function() {
		var button = this, dom = $invokeSuper.call(button, arguments);
		$fly(dom).mouseenter(function() {
			var menu = button._menu, toolbar = button._parent;
			if (menu && toolbar) {
				var activeButton = toolbar._activeMenuButton;
				if (activeButton && activeButton != button) {
					toolbar._ignoreMenuHide = true;
					activeButton._menu.hide(true);
					button.doShowMenu();
				}
			}
		});
		
		return dom;
	},
	
	doShowMenu: function() {
		$invokeSuper.call(this, arguments);
		var button = this, menu = button._menu;
		if (menu) {
			var toolbar = button._parent;
			toolbar._activeMenuButton = button;
		}
	},
	
	doAfterMenuHide: function() {
		var button = this, toolbar = button._parent;
		if (toolbar && !toolbar._ignoreMenuHide) {
			toolbar._activeMenuButton = null;
		}
		$invokeSuper.call(this, arguments);
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component ToolBar
 * @class 工具栏上的填充。
 * <p>
 * 工具栏在渲染的时候，在该组件之前的组件，都在左边渲染，在该组件以后的组件，都在右边渲染。
 * </p>
 * @shortTypeName -> ; Fill
 * @extends dorado.widget.Control
 */
dorado.widget.toolbar.Fill = $extend(dorado.widget.Control, {
	$className: "dorado.widget.toolbar.Fill"
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component ToolBar
 * @class Label
 * @shortTypeName Label
 * @extends dorado.widget.Control
 */
dorado.widget.toolbar.Label = $extend(dorado.widget.Control, {
	$className: "dorado.widget.toolbar.Label",
    _inherentClassName: "i-toolbar-label",
	ATTRIBUTES: {
		className: {
			defaultValue: "d-toolbar-label"
		},
		text: {}
	},
	
	createDom: function() {
		var label = this, dom = document.createElement("div");
		dom.className = label._className;
		$fly(dom).text(label._text);
		return dom;
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		var label = this;
		$fly(dom).text(label._text);
	}
});

dorado.Toolkits.registerPrototype("toolbar", {
	Default: dorado.widget.toolbar.Button,
	ToolBarButton: dorado.widget.toolbar.Button,
	"->": dorado.widget.toolbar.Fill,
	Fill: dorado.widget.toolbar.Fill,
	ToolBarLabel: dorado.widget.toolbar.Label,
	"-": dorado.widget.toolbar.Separator,
	"|": dorado.widget.toolbar.Separator,
	Separator: dorado.widget.toolbar.Separator
});
