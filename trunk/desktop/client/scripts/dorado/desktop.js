/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.Shortcut
 * @extends dorado.RenderableElement
 * @extends dorado.EventSupport
 */
dorado.widget.desktop.Shortcut = $extend([dorado.RenderableElement, dorado.EventSupport], /** @scope dorado.widget.desktop.Shortcut.prototype */{
	$className: "dorado.widget.desktop.Shortcut",
	focusable: true,

    constructor: function(config) {
        $invokeSuper.call(this, arguments);
        if (config) {
            this.set(config);
        }
    },

    destroy: function() {
        var isClosed = (window.closed || dorado.windowClosed);

        var dom = this._dom;
        if (dom) {
            $dom = $fly(dom);
            if (!isClosed) $dom.remove();
        }
    },

    getListenerScope: function() {
        if (this._parent && this._parent._view) {
            return this._parent._view;
        }
        return this;
    },

	ATTRIBUTES: /** @scope dorado.widget.desktop.Shortcut.prototype */{
		className: {
			defaultValue: "shortcut"
		},

		/**
         * 图标路径。
		 * @attribute
		 * @type String
		 */
		icon: {},

		/**
         * 图标使用的className。
		 * @attribute
		 * @type String
		 */
		iconClass: {},

		/**
		 * 图标大小，可选值为small,medium,big，分别对应32*32，48*48，64*64大小的图标。
		 * @attribute
		 * @type String
         * @default "small"
		 */
		iconSize: {
			defaultValue: "small"
		},

		/**
         * 图标下方显示的文字。
		 * @attribute
		 * @type String
		 */
		caption: {},

		/**
         * Shortcut所在的列，暂不支持设定。
		 * @attribute
		 * @type int
		 */
		column: {},

		/**
         * Shortcut所在的行，暂不支持设定。
		 * @attribute
		 * @type int
		 */
		row: {},

		/**
         * 对应的App的id。
		 * @attribute
		 * @type String
		 */
		appId: {}
	},

	EVENTS: /** @scope dorado.widget.desktop.Shortcut.prototype */{
        /**
		 * 在Shortcut上点击触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {boolean} #arg.processDefault=false 是否要继续系统的默认操作，让系统上下文菜单显示出来。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
        onClick: {}
    },

	getShell: function() {
		return this._parent.findParent(dorado.widget.desktop.Shell);
	},

	onClick: function() {
		$invokeSuper.call(this, arguments);
		var cut = this, appId = cut._appId, shell = this.getShell();
		if (shell && appId) {
			shell.launchApp(appId);
		}
        cut.fireEvent("onClick", self);
	},

	createDom: function() {
		var cut = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: cut._className,
			content: [
				{
					tagName: "div",
					className: "icon",
					contextKey: "icon"
				},
				{
					tagName: "span",
					className: "caption",
					content: cut._caption,
					contextKey: "caption"
				}
			]
		}, null, doms);

		$fly(dom).addClass(cut._className + "-" + cut._iconSize + "-icon").click(function() {
            if ($fly(this).hasClass("ui-draggable-dragged")) {
                $fly(this).removeClass("ui-draggable-dragged");
                return;
            }
            cut.onClick();
        });

		cut._doms = doms;
		$DomUtils.setBackgroundImage(doms.icon, cut._icon);

		return dom;
	},

    refreshDom: function(dom) {
        $invokeSuper.call(this, arguments);
        var cut = this;
        if (!cut._parent) {
            return;
        }
        var shell = cut.getShell(), appId = cut._appId;
        if (appId && shell) {
            var appAttrs = shell._appAttrs[appId] || {}, doms = cut._doms, iconProperty;
            switch(cut._iconSize) {
                case "small":
                    iconProperty = "icon32";
                    break;
                case "medium":
                    iconProperty = "icon48";
                    break;
                case "big":
                    iconProperty = "icon64";
                    break;
            }
            var icon = cut._icon || appAttrs[iconProperty], caption = cut._caption || appAttrs.caption;
            $DomUtils.setBackgroundImage(doms.icon, icon);
            $fly(doms.caption).text(caption || "");
        }
    }
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class AbstractDesktop
 * @extends dorado.widget.Control
 */
dorado.widget.desktop.AbstractDesktop = $extend(dorado.widget.Control, /** @scope dorado.widget.desktop.AbstractDesktop.prototype */ {
    ATTRIBUTES: /** @scope dorado.widget.desktop.AbstractDesktop.prototype */{
        /**
         * 激活右键菜单的Shortcut，可能会被激活的右键菜单使用。
         * @type dorado.widget.desktop.Shortcut
         * @attribute
         */
        contextMenuShortcut: {
            readOnly: true
        }
    },
    EVENTS: /** @scope dorado.widget.desktop.AbstractDesktop.prototype */ {
        /**
		 * 在Shortcut上点击右键触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.desktop.Shortcut} arg.shortcut 触发该事件的Shortcut。
		 * @param {Event} arg.event DHTML中的事件event参数。
		 * @param {boolean} #arg.processDefault=false 是否要继续系统的默认操作，让系统上下文菜单显示出来。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onShortcutContextMenu: {}
    }
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.Desktop
 * @extends dorado.widget.Control
 */
dorado.widget.desktop.Desktop = $extend(dorado.widget.desktop.AbstractDesktop, /** @scope dorado.widget.desktop.Desktop.prototype */{
	$className: "dorado.widget.desktop.Desktop",
	focusable: true,
	ATTRIBUTES: /** @scope dorado.widget.desktop.Desktop.prototype */{
		className: {
			defaultValue: "d-desktop"
		},

		/**
		 * 图标大小，可选值为small,medium,big，分别对应32*32，48*48，64*64大小的图标。
		 * @attribute
		 * @type String
		 */
		iconSize: {
			defaultValue: "small"
		},

		/**
		 * 桌面上的快捷键的集合。
		 * @attribute
		 * @type dorado.widget.desktop.Shortcut[]
		 */
		items: {
			innerComponent: "desktop.Shortcut"
		},

		/**
		 * 列数
		 * @attribute readOnly
		 * @type int
		 */
		columnCount: {
			readOnly: true
		},

		/**
		 * 行数
		 * @attribute readOnly
		 * @type int
		 */
		rowCount: {
			readOnly: true
		}
	},

	/**
	 * 初始化快捷键。
	 * @param {dorado.widget.desktop.Shortcut} shortcut 要初始化的快捷键。
	 * @param {HtmlElement} dom desktop的dom。
	 * @private
	 */
	initShortcut: function(shortcut, dom) {
		var desktop = this, desktopXY, desktopSize, itemWidth, itemHeight, columnIndex, rowIndex;
		shortcut._iconSize = desktop._iconSize;
        shortcut._parent = desktop;
		shortcut.render(dom);
		$fly(shortcut._dom).bind("contextmenu", function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();
			var desktop = shortcut._parent, arg = {
				shortcut: shortcut,
				event: event
			};
			desktop._contextMenuShortcut = shortcut;
			desktop.fireEvent("onShortcutContextMenu", desktop, arg);
            if (desktop._parent instanceof dorado.widget.desktop.DesktopCarousel) {
                desktop._parent._contextMenuShortcut = shortcut;
			    desktop._parent.fireEvent("onShortcutContextMenu", desktop._parent, arg);
            }

			return false;
		}).css("position", "absolute").draggable({
			distance: 10,
			scope: "desktop",
			start: function(event, ui) {
				desktopXY = $fly(dom).offset();
				desktopSize = {
					width: $fly(dom).width(),
					height: $fly(dom).height()
				};

				var helper = ui.helper;
				itemWidth = helper.outerWidth(true);
				itemHeight = helper.outerHeight(true);

				helper.bringToFront();

				if (!desktop._shortcutHolder) {
					desktop._shortcutHolder = $DomUtils.xCreate({
						tagName: "div",
						className: "shortcut-place-holder shortcut-place-holder-" + (desktop._iconSize || "medium")
					});
					dom.appendChild(desktop._shortcutHolder);
				}
			},
			drag: function(event, ui) {
				var helper = ui.helper, position = helper.offset();

				var itemCenterLeft = position.left + itemWidth / 2, itemCenterTop = position.top + itemHeight / 2,
					tempColumnIndex = Math.floor((itemCenterLeft - desktop._leftStart - desktopXY.left) / itemWidth),
					tempRowIndex = Math.floor((itemCenterTop - desktop._topStart - desktopXY.top) / itemHeight);

				if (tempRowIndex >= 0 && tempRowIndex <= desktop._rowCount - 1 && tempColumnIndex >= 0 && tempColumnIndex <= desktop._columnCount - 1) {
					columnIndex = tempColumnIndex;
					rowIndex = tempRowIndex;
					$fly(desktop._shortcutHolder).css({
						left: desktop._leftStart + tempColumnIndex * itemWidth,
						top: desktop._topStart + tempRowIndex * itemHeight,
						display: "block"
					});
				}
			},
			stop: function() {
				function getNextShortcut(column, row) {
					var resultColumn = column, resultRow = row + 1;
					if (row == desktop._rowCount - 1) {
						resultColumn += 1;
						resultRow = 0;
					}
					return desktop.getShortcut(resultColumn, resultRow);
				}
				function placeLastShortcut(shortcut) {
					var column = shortcut._column, row = shortcut._row, resultColumn = column, resultRow = row + 1;
					if (row == desktop._rowCount - 1) {
						resultColumn += 1;
						resultRow = 0;
					}
					desktop.placeShortcut(shortcut, resultColumn, resultRow, true);
				}
				var target = desktop.getShortcut(columnIndex, rowIndex);
				if (target) {
					desktop.unplaceShortcut(shortcut);//unplace self first
					var nextShortcut;
					while (target) {
						nextShortcut = getNextShortcut(target._column, target._row);
						if (nextShortcut) {
							desktop.placeShortcut(target, nextShortcut._column, nextShortcut._row, true);
						} else {//last short cut
							placeLastShortcut(target);
						}
						target = nextShortcut;
					}
				}
				desktop.placeShortcut(shortcut, columnIndex, rowIndex);//place self
				$fly(desktop._shortcutHolder).css("display", "");
                $(shortcut._dom).addClass("ui-draggable-dragged");
			}
		});
	},

	/**
	 * 通过行列索引取得相应位置的快捷键。
	 * @param {int} column 列索引
	 * @param {int} row 行索引
	 */
	getShortcut: function(column, row) {
		var desktop = this, scCache = desktop._scCache;
		if (scCache) {
			var columnCache = scCache[column];
			if (columnCache) {
				return columnCache[row];
			}
		}
		return null;
	},

	/**
	 * 取消放置某个快捷键，该方法主要用来清空缓存。
	 * @param {dorado.widget.desktop.Shortcut} shortcut 要取消放置的快捷键。
	 * @protected
	 */
	unplaceShortcut: function(shortcut) {
		if (shortcut) {
			var column = shortcut._column, row = shortcut._row;
			var desktop = this, scCache = desktop._scCache;
			if (scCache) {
				var columnCache = scCache[column];
				if (columnCache && columnCache[row] == shortcut) {
					columnCache[row] = null;
					shortcut._column = undefined;
					shortcut._row = undefined;
				}
			}
		}
	},

    /**
     * 添加Shortcut，目前不支持Shortcut自身的column、row的设置，Desktop会自动找到一个合理的位置放置该Shortcut。
     * @param {dorado.widget.desktop.Shortcut} item 要添加的Shortcut。
     */
    addItem: function(item) {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) {
            desktop._items = items = [];
        }
        if (!(item instanceof dorado.widget.desktop.Shortcut)) {
            item = new dorado.widget.desktop.Shortcut(item);
        }
        items.push(item);
        if (rendered) {
            desktop.initShortcut(item, desktop._dom);
            desktop.placeNewShortcut(item);
        }
    },

    /**
     * 删除指定的Shortcut。
     * @param {dorado.widget.desktop.Shortcut} item 要删除的Shortcut。
     */
    removeItem: function(item) {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) return;
        desktop.unplaceShortcut(item);
        items.remove(item);
        item.destroy();
    },

    /**
     * 清除所有的Shortcut。
     */
    clearItems: function() {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) return;
        for (var i = 0, j = items.length; i < j; i++) {
            var item = items[i];
            desktop.unplaceShortcut(item);
            item.destroy();
        }
        desktop._items = [];
    },

    /**
     * @private
     */
    placeNewShortcut: function(shortcut) {
        var desktop = this, scCache = desktop._scCache;

        if (!scCache) {
            var dom = desktop._dom, items = desktop._items, item = items[0], width = $fly(dom).width(), height = $fly(dom).height();

            desktop._leftStart = parseInt($fly(dom).css("padding-left"), 10);
            desktop._topStart = parseInt($fly(dom).css("padding-top"), 10);

            if (item) {
                var itemWidth = $fly(item._dom).outerWidth(true);
                var itemHeight = $fly(item._dom).outerHeight(true);

                desktop._rowCount = Math.floor(height / itemHeight);
                desktop._columnCount = Math.floor(width / itemWidth);
            }
        }

        var rowCount = desktop._rowCount, columnCount = desktop._columnCount;
        for (var i = 0; i < columnCount; i++) {
            for (var j = 0; j < rowCount; j++) {
                if (desktop.getShortcut(i, j) == null) {
                    desktop.placeShortcut(shortcut, i, j);
                    return;
                }
            }
        }
        desktop.placeShortcut(shortcut, columnCount, rowCount);
    },

	/**
	 * 放置快捷键到指定位置上。
	 * @param {dorado.widget.desktop.Shortcut} shortcut 要放置的快捷键。
	 * @param {int} column 列索引
	 * @param {int} row 行索引
	 * @param {boolean} animate 是否需要动画效果。
	 */
	placeShortcut: function(shortcut, column, row, animate) {
		if (!shortcut || typeof column != "number" || typeof row != "number") {
			return;
		}
		var desktop = this, scCache = desktop._scCache, leftStart = desktop._leftStart, topStart = desktop._topStart;
		desktop.unplaceShortcut(shortcut);
		if (!scCache) {
			scCache = desktop._scCache = [];
		}
		var columnCache = scCache[column];
		if (!columnCache) {
			columnCache = scCache[column] = [];
		}
		columnCache[row] = shortcut;
		shortcut._column = column;
		shortcut._row = row;

		$fly(shortcut._dom)[animate ? "animate" : "css"]({
			left: leftStart + column * shortcut._dom.offsetWidth,
			top: topStart + row * shortcut._dom.offsetHeight
		});
	},

	createDom: function() {
		var desktop = this, dom = $invokeSuper.call(desktop, arguments), items = desktop._items || [];
		for (var i = 0, j = items.length; i < j; i++) {
			var item = items[i];
			desktop.initShortcut(item, dom);
		}

		return dom;
	},

	/**
	 * 移动不合理的快捷键。
	 * @param columnCount 列数
	 * @param rowCount 行数
	 */
	moveInvalidShortcut: function(columnCount, rowCount) {
		var desktop = this, items = desktop._items, scCache = desktop._scCache, allShortcuts = [], tempArray = [], i, j, item;
		for (i = 0; i < items.length; i++) {
			item = items[i];
			if (item) {
				if (item._column > columnCount - 1) {
					tempArray.push(item);
				} else {
					allShortcuts[item._column * desktop._rowCount + item._row] = item;
				}
			}
		}

		for (i = 0; i < tempArray.length; i++) {
			allShortcuts.push(tempArray[i]);
		}

		scCache = desktop._scCache = [];
		for (i = 0, j = allShortcuts.length; i < j; i++) {
			item = allShortcuts[i];
			if (item) {
				item._column = Math.floor(i / rowCount);
				item._row = i % rowCount;
				desktop.placeShortcut(item, item._column, item._row);
			}
		}
	},

	refreshDom: function(dom) {
		var desktop = this, items = desktop._items;
		$invokeSuper.call(desktop, arguments);

		if (items) {
			var scCache = desktop._scCache, width = $fly(dom).width(), height = $fly(dom).height(),
				itemWidth, itemHeight, rowCount, columnCount, item, i, j;

			if (!scCache) {
				item = items[0];

				desktop._leftStart = parseInt($fly(dom).css("padding-left"), 10);
				desktop._topStart = parseInt($fly(dom).css("padding-top"), 10);

				if (item) {
					itemWidth = $fly(item._dom).outerWidth(true);
					itemHeight = $fly(item._dom).outerHeight(true);

					desktop._rowCount = rowCount = Math.floor(height / itemHeight);
					desktop._columnCount = columnCount = Math.floor(width / itemWidth);
				}
				for (i = 0, j = items.length; i < j; i++) {
					item = items[i];
					desktop.placeShortcut(item, Math.floor(i / rowCount), i % rowCount);
				}
			} else {
				item = items[0];
				if (item) {
					itemWidth = $fly(item._dom).outerWidth(true);
					itemHeight = $fly(item._dom).outerHeight(true);

					rowCount = Math.floor(height / itemHeight);
					columnCount = Math.floor(width / itemWidth);

					if (desktop._rowCount != rowCount || desktop._columnCount > columnCount) {
						//if desktop._columnCount > columnCount move the hidden shortcut to the last
						//if desktop._rowCount != rowCount resort shortcut by current order
						desktop.moveInvalidShortcut(columnCount, rowCount);
					}
					desktop._columnCount = columnCount;
					desktop._rowCount = rowCount;
					scCache = desktop._scCache;
					for (i = 0; i < columnCount; i++) {
						var columnCache = scCache[i];
						if (columnCache) {
							for (j = 0; j < rowCount; j++) {
								var shortcut = columnCache[j];
								if (shortcut) {
									$fly(shortcut._dom).css({
										left: desktop._leftStart + i * shortcut._dom.offsetWidth,
										top: desktop._topStart + j * shortcut._dom.offsetHeight
									});
								}
							}
						}
					}
				}
			}
		}
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @class Flash
 * @extends dorado.widget.Control
 */
dorado.widget.Flash = $extend(dorado.widget.Control, /** @scope dorado.widget.Flash.prototype */{
    $className: "dorado.widget.Flash",
    ATTRIBUTES: /** @scope dorado.widget.Flash.prototype */{
        /**
         * Flash file path.
         * @attribute
         * @type String
         */
        path: {}
    },
    onAttachToDocument: function() {
        $invokeSuper.call(this, arguments);

        jQuery(this._dom).flash({
            swf: dorado.Toolkits.translateURL(this._path),
            wmode: "transparent",
            width: "100%",
            height: "100%"
        });
    }
});


/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class Widget
 * @extends dorado.widget.Control
 * @extends dorado.widget.FloatControl
 */
dorado.widget.desktop.Widget = $extend([dorado.widget.Control, dorado.widget.FloatControl], /** @scope dorado.widget.desktop.Widget.prototype */{
    $className: "dorado.widget.desktop.Widget",
	focusable: true,
	ATTRIBUTES: /** @scope dorado.widget.desktop.Widget.prototype */{
		className: {
			defaultValue: "d-widget"
		},
        /**
         * @attribute
         * @type dorado.widget.Control
         */
		control: {},
		visible: {
			defaultValue: false
		},
		shadowMode: {
			defaultValue: "none"
		},
		animateType: {
			defaultValue: "none"
		}
	},
	createDom: function() {
		var widget = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: widget._className,
			content: [{
				tagName: "div",
				className: "caption",
				contextKey: "caption",
				content: {
					tagName: "div",
					className: "close",
					contextKey: "close"
				}
			}]
		}, null, doms);
		widget._doms = doms;

		var control = widget._control;
		if (control) {
			control.render(dom);
			widget.registerInnerControl(control);
		}
		jQuery(dom).css("position", "absolute").draggable({
		    //handle: ".caption",
			addClasses: false,
			containment: "parent"
		}).addClassOnHover(this._className + "-hover");
		$fly(doms.close).click(function() {
			widget.fireEvent("onClose", widget);
		});
		return dom;
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class DesktopCarousel
 * @extends dorado.widget.desktop.AbstractDesktop
 */
dorado.widget.desktop.DesktopCarousel = $extend(dorado.widget.desktop.AbstractDesktop, /** @scope dorado.widget.desktop.DesktopCarousel.prototype */ {
	$className: "dorado.widget.desktop.DesktopCarousel",

	ATTRIBUTES: /** @scope dorado.widget.desktop.DesktopCarousel.prototype */ {
		className: {
			defaultValue: "d-desktop-carousel"
		},

		/**
		 * 当前活动的组件。
		 * @type dorado.widget.Control|int
		 * @attribute
		 */
		currentControl: {
			//skipRefresh: true,
			setter: function(value) {
				var cardbook = this, controls = cardbook._controls;
				if (value != null) {
					if (typeof value == "string" || typeof value == "number") {
						value = controls.get(value);
					}
				}
                var oldValue = cardbook._currentControl;
				var eventArg = {
					oldValue: oldValue
				};
                cardbook.fireEvent("beforeCurrentChange", this, eventArg);
				if (eventArg.processDefault === false) return;
				if (oldValue) {
					var oldDom = oldValue._dom;
					if (oldDom) {
						//oldDom.style.display = "none";
					}
				}
				cardbook._currentControl = value;
				var dom = cardbook._dom;
				if (dom && value) {
					value.set("width", $fly(dom).innerWidth());
					value.set("height", $fly(dom).innerHeight());

					if (!value._rendered) {
                        cardbook.registerInnerControl(value);
						value.render(dom);
					} else {
						$fly(value._dom).css("display", "block");
					}

					var currentIndex = controls.indexOf(value);
					if (currentIndex != -1){
						var width = $fly(cardbook._dom).outerWidth();
						$fly(cardbook._doms.itemsWrap).animate({
							"left": -1 * currentIndex * width
						});
					}
				}
                cardbook.fireEvent("onCurrentChange", this, value);
			}
		},

		/**
		 * Card中所有的Control。
		 * 该属性设置时使用Array类型的数据进行设置，取得时取得的数据类型为dorado.util.KeyedArray。
		 * @type dorado.util.KeyedArray
		 * @attribute
		 */
		controls: {
			writeOnce: true,
			innerComponent: "",
			setter: function(value) {
				if (value) {
					var controls = this._controls, currentFirstControl = (controls.size == 0);
					if (value instanceof Array) {
						for (var i = 0; i < value.length; i++) {
							controls.insert(value[i]);
							value[i]._parent = value[i]._focusParent = this;
							if (i == 0 && currentFirstControl) {
								this.set("currentControl", value[i]);
							}
						}
					} else if (value.constructor == Object.prototype.constructor) {
                        controls.insert(value);
						value._parent = value._focusParent = this;
                        this.set("currentControl", value);
                    }
				}
			}
		}
	},

    EVENTS: /** @scope dorado.widget.desktop.DesktopCarousel.prototype */ {
		/**
		 * 在currentControl改变之前触发。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
        beforeCurrentChange: {},

        /**
         * 在currentControl改变之后触发。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        onCurrentChange: {}
    },

	constructor: function() {
		this._controls = new dorado.util.KeyedArray(function(value) {
			return value._id;
		});
		$invokeSuper.call(this, arguments);
	},

	/**
	 * 插入子组件。
	 * @param {dorado.widget.Control} control 要插入的子组件
	 * @param {int} [index] 要插入的子组件的索引。
	 * @param {boolean} [current=false] 是否把插入的组件置为活动组件，默认为false。
	 * @return {dorado.widget.Control} 插入的组件。
	 */
	addControl: function(control, index, current) {
		if (!control) {
			throw new dorado.ResourceException("dorado.base.CardControlUndefined");
		}
		var card = this, doms = card._doms, controls = card._controls;
		controls.insert(control, index);
        if (card._rendered) {
            control.render(doms.itemsWrap, index);
            $fly(control._dom).addClass("carousel-item");
            card.doCreateSwitchButton(index);
        }
		card.registerInnerControl(control);
		if (current !== false) {
			card.set("currentControl", control);
		}
		return control;
	},

	/**
	 * 移除子组件。
	 * @param {String|int|dorado.widget.Control} control
	 * 可以为组件本身、组件的索引(在controls中的索引)、组件的id。
	 * @return {dorado.widget.Control} 移除的子组件
	 */
	removeControl: function(control) {
		var card = this, controls = card._controls;
		control = card.getControl(control);
		if (control) {
			control.destroy && control.destroy();
			controls.remove(control);
			return control;
		}
		return null;
	},

	/**
	 * 移除所有的组件。
	 */
	removeAllControls: function() {
		var card = this, controls = card._controls;
		for (var i = 0, j = controls.size; i < j; i++) {
			var item = controls.get(0);
			card.removeControl(item);
		}
	},

	/**
	 * 取得Card中的组件。
	 * @param {String|Number|dorado.widget.Control} id 组件的id、index或者组件本身。
	 * @return {dorado.widget.Control} 找到的子组件。
	 */
	getControl: function(id) {
		var card = this, controls = card._controls;
		if (controls) {
			if (typeof id == "number" || typeof id == "string") {
				return controls.get(id);
			} else {
				return id;
			}
		}
		return null;
	},

	createDom: function() {
		var card = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: this._className,
			content: [{
				tagName: "div",
				className: "items-wrap",
				contextKey: "itemsWrap"
			}, {
                tagName: "div",
                className: "switcher",
                contextKey: "switcher",
                content: {
                    tagName: "span",
                    className: "switcher-right",
                    content: {
                        tagName: "span",
                        className: "switcher-center",
                        contextKey: "switcherCenter"
                    }
                }
            }]
		}, null, doms);
		card._doms = doms;
		dom.className = this._className;

		var controls = card._controls;

		for (var i = 0, j = controls.size; i < j; i++) {
			var control = controls.get(i);
			control.render(doms.itemsWrap);
			$fly(control._dom).addClass("carousel-item");
			card.registerInnerControl(control);

            card.doCreateSwitchButton();
		}

		return dom;
	},

    doCreateSwitchButton: function(index) {
        var card = this, doms = card._doms, button = document.createElement("span");
        button.className = "switch-button";
        jQuery(button).addClassOnHover("switch-button-hover").click(function() {
            card.setCurrentButton(this);
        });
        if (index == undefined) {
            $fly(doms.switcherCenter).append(button);
        } else {
            var refNode = $fly(doms.switcherCenter).find(".switch-button")[index];
            if (refNode) {
                $fly(button).insertBefore(refNode);
            } else {
                $fly(button).appendTo(doms.switcherCenter);
            }
        }
    },

	onResize: function() {
		$invokeSuper.call(this, arguments);
		this.refresh();
	},

	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);

		var card = this, currentControl = card._currentControl, doms = card._doms;
		var width = $fly(dom).outerWidth(), controls = card._controls;

		for (var i = 0, j = controls.size; i < j; i++) {
			var control = controls.get(i);
			control.set("width", $fly(dom).width());
			control.set("height", $fly(dom).height());
			$fly(control._dom).css("left", i * width);
		}

		if (currentControl) {
			var currentIndex = controls.indexOf(currentControl);
			$fly(doms.itemsWrap).css({ "left": -1 * currentIndex * width });
		}

        $fly(doms.switcher).css("left", (dom.offsetWidth - doms.switcher.offsetWidth) / 2);

        var index = card._controls.indexOf(currentControl);
        if (index != -1) {
            $fly(dom).find(".switch-button").removeClass("switch-button-current");
            var button = $fly(dom).find(".switch-button")[index];
            $fly(button).addClass("switch-button-current");
        }
	},

    setCurrentButton: function(button) {
		var card = this, dom = card._dom;
		if (dom) {
			$fly(dom).find(".switch-button").removeClass("switch-button-current");
			var index = $fly(button).addClass("switch-button-current").index();
			if (card) {
				card.set("currentControl", index);
			}
		}
	}
});