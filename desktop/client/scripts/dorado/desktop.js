/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.ShortCut
 * @extends dorado.RenderableElement
 * @extends dorado.EventSupport
 */
dorado.widget.desktop.ShortCut = $extend([dorado.RenderableElement, dorado.EventSupport], /** @scope dorado.widget.desktop.ShortCut.prototype */{
	$className: "dorado.widget.desktop.ShortCut",
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

	ATTRIBUTES: /** @scope dorado.widget.desktop.ShortCut.prototype */{
		className: {
			defaultValue: "shortcut"
		},

		/**
		 * @attribute
		 * @type String
		 */
		icon: {},

		/**
		 * @attribute
		 * @type String
		 */
		iconClass: {},

		/**
		 * small medium big
		 * @attribute
		 * @type String
		 */
		iconSize: {
			defaultValue: "small"
		},

		/**
		 * @attribute
		 * @type String
		 */
		caption: {},

		/**
		 * @attribute
		 * @type int
		 */
		column: {},

		/**
		 * @attribute
		 * @type int
		 */
		row: {},

		/**
		 * @attribute
		 * @type String
		 */
		appId: {}
	},

	EVENTS: {},

	getShell: function() {
		return this._parent.findParent(dorado.widget.desktop.Shell);
	},

	onClick: function() {
		$invokeSuper.call(this, arguments);
		var cut = this, appId = cut._appId, shell = this.getShell();
		if (shell && appId) {
			shell.launchApp(appId);
		}
	},

	createDom: function() {
		var cut = this, doms = {}, dom = $DomUtils.xCreateElement({
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
dorado.widget.desktop.AbstractDesktop = $extend(dorado.widget.Control, {});

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
		 * small medium big
		 * @attribute
		 * @type String
		 */
		iconSize: {
			defaultValue: "small"
		},

		/**
		 * 桌面上的快捷键的集合。
		 * @attribute
		 * @type dorado.widget.desktop.ShortCut[]
		 */
		items: {
			innerComponent: "desktop.ShortCut"
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
	 * @param {dorado.widget.desktop.ShortCut} shortcut 要初始化的快捷键。
	 * @param {HtmlElement} dom desktop的dom。
	 * @private
	 */
	initShortCut: function(shortcut, dom) {
		var desktop = this, desktopXY, desktopSize, itemWidth, itemHeight, columnIndex, rowIndex;
		shortcut._iconSize = desktop._iconSize;
        shortcut._parent = desktop;
		shortcut.render(dom);
		$fly(shortcut._dom).css("position", "absolute").draggable({
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
					desktop._shortcutHolder = $DomUtils.xCreateElement({
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
				function getNextShortCut(column, row) {
					var resultColumn = column, resultRow = row + 1;
					if (row == desktop._rowCount - 1) {
						resultColumn += 1;
						resultRow = 0;
					}
					return desktop.getShortCut(resultColumn, resultRow);
				}
				function placeLastShortcut(shortcut) {
					var column = shortcut._column, row = shortcut._row, resultColumn = column, resultRow = row + 1;
					if (row == desktop._rowCount - 1) {
						resultColumn += 1;
						resultRow = 0;
					}
					desktop.placeShortCut(shortcut, resultColumn, resultRow, true);
				}
				var target = desktop.getShortCut(columnIndex, rowIndex);
				if (target) {
					desktop.unplaceShortCut(shortcut);//unplace self first
					var nextShortCut;
					while (target) {
						nextShortCut = getNextShortCut(target._column, target._row);
						if (nextShortCut) {
							desktop.placeShortCut(target, nextShortCut._column, nextShortCut._row, true);
						} else {//last short cut
							placeLastShortcut(target);
						}
						target = nextShortCut;
					}
				}
				desktop.placeShortCut(shortcut, columnIndex, rowIndex);//place self
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
	getShortCut: function(column, row) {
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
	 * @param {dorado.widget.desktop.ShortCut} shortcut 要取消放置的快捷键。
	 * @protected
	 */
	unplaceShortCut: function(shortcut) {
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
     * 添加ShortCut，目前不支持ShortCut自身的column、row的设置，Desktop会自动找到一个合理的位置放置该ShortCut。
     * @param {dorado.widget.desktop.ShortCut} item 要添加的ShortCut。
     */
    addItem: function(item) {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) {
            items = desktop._items || [];
        }
        if (!(item instanceof dorado.widget.desktop.ShortCut)) {
            item = new dorado.widget.desktop.ShortCut(item);
        }
        if (rendered) {
            desktop.initShortCut(item, desktop._dom);
            desktop.placeNewShortCut(item);
        }
        items.push(item);
    },

    /**
     * 删除指定的ShortCut。
     * @param {dorado.widget.desktop.ShortCut} item 要删除的ShortCut。
     */
    removeItem: function(item) {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) return;
        desktop.unplaceShortCut(item);
        items.remove(item);
        item.destroy();
    },

    /**
     * 清除所有的ShortCut。
     */
    clearItems: function() {
        var desktop = this, items = desktop._items, rendered = desktop._rendered;
        if (!items) return;
        for (var i = 0, j = items.length; i < j; i++) {
            var item = items[i];
            desktop.unplaceShortCut(item);
            item.destroy();
        }
        desktop._items = [];
    },

    /**
     * @private
     */
    placeNewShortCut: function(shortcut) {
        var desktop = this, rowCount = desktop._rowCount, columnCount = desktop._columnCount;
        for (var i = 0; i < columnCount; i++) {
            for (var j = 0; j < rowCount; j++) {
                if (desktop.getShortCut(i, j) == null) {
                    desktop.placeShortCut(shortcut, i, j);
                    return;
                }
            }
        }
        desktop.placeShortCut(shortcut, columnCount, rowCount);
    },

	/**
	 * 放置快捷键到指定位置上。
	 * @param {dorado.widget.desktop.ShortCut} shortcut 要放置的快捷键。
	 * @param {int} column 列索引
	 * @param {int} row 行索引
	 * @param {boolean} animate 是否需要动画效果。
	 */
	placeShortCut: function(shortcut, column, row, animate) {
		if (!shortcut || typeof column != "number" || typeof row != "number") {
			return;
		}
		var desktop = this, scCache = desktop._scCache, leftStart = desktop._leftStart, topStart = desktop._topStart;
		desktop.unplaceShortCut(shortcut);
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
		var desktop = this, dom = $invokeSuper.call(desktop, arguments), items = desktop._items;
		for (var i = 0, j = items.length; i < j; i++) {
			var item = items[i];
			desktop.initShortCut(item, dom);
		}

		return dom;
	},

	/**
	 * 移动不合理的快捷键。
	 * @param columnCount 列数
	 * @param rowCount 行数
	 */
	moveInvalidShortCut: function(columnCount, rowCount) {
		var desktop = this, items = desktop._items, scCache = desktop._scCache, allShortCuts = [], tempArray = [], i, j, item;
		for (i = 0; i < items.length; i++) {
			item = items[i];
			if (item) {
				if (item._column > columnCount - 1) {
					tempArray.push(item);
				} else {
					allShortCuts[item._column * desktop._rowCount + item._row] = item;
				}
			}
		}

		for (i = 0; i < tempArray.length; i++) {
			allShortCuts.push(tempArray[i]);
		}

		scCache = desktop._scCache = [];
		for (i = 0, j = allShortCuts.length; i < j; i++) {
			item = allShortCuts[i];
			if (item) {
				item._column = Math.floor(i / rowCount);
				item._row = i % rowCount;
				desktop.placeShortCut(item, item._column, item._row);
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
					desktop.placeShortCut(item, Math.floor(i / rowCount), i % rowCount);
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
						desktop.moveInvalidShortCut(columnCount, rowCount);
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
		var widget = this, doms = {}, dom = $DomUtils.xCreateElement({
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
		var card = this, controls = card._controls;
		controls.insert(control, index);
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
		var card = this, doms = {}, dom = $DomUtils.xCreateElement({
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
                    tagName: "div",
                    className: "switcher-right",
                    content: {
                        tagName: "div",
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

            var button = document.createElement("div");
			button.className = "switch-button";
			jQuery(button).addClassOnHover("switch-button-hover").click(function() {
				card.setCurrentButton(this);
			});
			$fly(doms.switcherCenter).append(button);
		}

		return dom;
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
			control.set("width", $fly(dom).innerWidth());
			control.set("height", $fly(dom).innerHeight());
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