/**
 * @name dorado.widget.list
 * @namespace 列表型控件所使用的一些相关类的命名空间。
 */
dorado.widget.list = {};

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 列表型控件的抽象类。
 * @abstract
 * @extends dorado.widget.Control
 */
dorado.widget.AbstractList = $extend(dorado.widget.Control, /** @scope dorado.widget.AbstractList.prototype */ {
	$className: "dorado.widget.AbstractList",
	
	focusable: true,
	
	ATTRIBUTES: /** @scope dorado.widget.AbstractList.prototype */ {
		/**
		 * 列表数据模型。
		 * @type dorado.widget.list.ItemModel
		 * @attribute readOnly
		 */
		itemModel: {},
		
		/**
		 * 是否允许用户通过界面操作使列表中没有任何选中项。
		 * <p>
		 * 此属性只在当列表中存在列表项是有意义。
		 * 例如对于列表控件，当此属性为true时，如果用户点击了列表中空白区域后，表格的当前行将被设置为空。
		 * </p>
		 * <p>
		 * 此属性对于那些支持数据绑定的列表型控件可能无效。
		 * </p>
		 * @type boolean
		 * @attribute skipRefresh
		 */
		allowNoCurrent: {
			skipRefresh: true
		},
		
		/**
		 * 选择模式。
		 * <p>
		 * 此属性具有如下几种取值：
		 * <ul>
		 * <li>none - 不支持选择模式。</li>
		 * <li>singleRow - 单行选择。</li>
		 * <li>multiRows - 多行选择。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute skipRefresh
		 * @default "none"
		 */
		selectionMode: {
			defaultValue: "none",
			skipRefresh: true,
			setter: function(v) {
				this.replaceSelection(this.get("selection"), null);
				this._selectionMode = v;
			}
		},
		
		/**
		 * 表格中的当前选中项。
		 * @type Object|Object[]
		 * @attribute skipRefresh
		 */
		selection: {
			skipRefresh: true,			
			getter: function() {
				var selection = this._selection;
				if (selection instanceof dorado.Entity) {
					if (selection.state == dorado.Entity.STATE_DELETED) {
						this._selection = selection = null;
					}
				}
				else if (selection instanceof Array) {
					for (var i = selection.length; i >= 0; i--) {
						var s = selection[i];
						if (s instanceof dorado.Entity && s.state == dorado.Entity.STATE_DELETED) {
							selection.removeAt(i);
						}
					}
				}
				if (this._selectionMode == "multiRows" && !selection) selection = [];
				return selection;
			},
			setter: function(v) {
				if (v == null && "multiRows" == this._selectionMode) v = [];
				this.replaceSelection(this.get("selection"), v);
			}
		},
		
		/**
		 * 拖拽模式。
		 * <p>
		 * 此属性具有如下几种取值：
		 * <ul>
		 * <li>item - 只能拖动列表项。</li>
		 * <li>control - 只能拖动整个控件。</li>
		 * <li>itemOrControl - 根据实际的操作可以拖动列表项或整个控件。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute writeBeforeReady
		 * @default "item"
		 */
		dragMode: {
			writeBeforeReady: true,
			defaultValue: "item"
		},
		
		/**
		 * 拖放模式。
		 * <p>
		 * 此属性具有如下几种取值：
		 * <ul>
		 * <li>onControl - 只能将目标拖放到整个控件上。</li>
		 * <li>onItem - 只能将目标拖放到某个列表项上。</li>
		 * <li>insertItems - 只能将目标以顺序敏感的方式插入到列表中。</li>
		 * <li>onOrInsertItems - 可以将目标拖放到某个列表项上或以顺序敏感的方式插入到列表中。</li>
		 * <li>onAnyWhere - 可以将目标拖放以上的所有位置。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute writeBeforeReady
		 * @default "insertItems"
		 */
		dropMode: {
			writeBeforeReady: true,
			defaultValue: "insertItems"
		}
	},
	
	EVENTS: /** @scope dorado.widget.AbstractList.prototype */ {
	
		/**
		 * 当当前行改变时触发的事件。
		 * @param {Object} self 事件的发起者，即控件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCurrentChange: {},
		
		/**
		 * 当列表中的选择发生改变时触发的事件。
		 * @param {Object} self 事件的发起者，即控件本身。
		 * @param {Object} arg 事件参数。
		 * @param {Object[]} arg.added 在本次改变中新增的被选中项。
		 * @param {Object[]} arg.removed 在本次改变中被移除的选中项。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 * @see dorado.widget.AbstractList#selection
		 */
		onSelectionChange: {}
	},
	
	/**
	 * @name dorado.widget.AbstractList#getCurrentItem
	 * @function
	 * @return {Object|dorado.Entity} 当前列表项。
	 * @description 返回当前列表项。
	 */
	/**
	 * @name dorado.widget.AbstractList#setCurrentItemDom
	 * @function
	 * @protected
	 * @param dom {HTMLElement} 当前列表项DOM元素。
	 * @description 将列表控件中的某列表项DOM元素设置为当前元素。
	 */
	/**
	 * @name dorado.widget.AbstractList#getCurrentItemDom
	 * @function
	 * @protected
	 * @return {HTMLElement} 当前列表项DOM元素。
	 * @description 将列表控件中的某列表项DOM元素设置为当前元素。
	 */
	/**
	 * @name dorado.widget.AbstractList#findItemDomByEvent
	 * @function
	 * @protected
	 * @param evt {Event} 鼠标事件对象。
	 * @return {HTMLElement} 当前DOM元素。
	 * @description 根据鼠标事件对象查找相应的列表项元素。
	 */
	// =====
	
	constructor: function() {
		this._itemModel = this.createItemModel();
		$invokeSuper.call(this, arguments);
	},
	
	createItemModel: function() {
		return new dorado.widget.list.ItemModel();
	},
	
	createDom: function() {
		var dom = $invokeSuper.call(this, arguments);
		if (!dorado.Browser.msie) {
			dom.style.overflow = "auto";
		}
		return dom;
	},
	
	applyDraggable: function(dom, options) {
		if (this._droppable && dom == this._dom && this._dragMode == "item") return;
		$invokeSuper.call(this, arguments);
	},
	
	initDraggingInfo: function(draggingInfo, evt) {
		$invokeSuper.call(this, arguments);
		if (this._dragMode != "control") {
			var itemDom = this.findItemDomByEvent(evt);
			draggingInfo.set({
				object: itemDom ? $fly(itemDom).data("item") : null,
				insertMode: null,
				refObject: null
			});
		}
	},
	
	initDraggingIndicator: function(indicator, draggingInfo, evt) {
		if (this._dragMode != "control") {
			var itemDom = draggingInfo.get("element");
			if (itemDom) {
				var contentDom = $DomUtils.xCreate({
					tagName: "div",
					className: "d-list-dragging-item"
				});
				$fly(itemDom).find(">*").clone().appendTo(contentDom);
				indicator.set("content", contentDom);
			}
		}
	},
	
	showLoadingTip: function() {
	
		function getLoadingTipDom() {
			var tipDom = this._loadingTipDom;
			if (!tipDom) {
				this._loadingTipDom = tipDom = $DomUtils.xCreate({
					tagName: "TABLE",
					className: "i-list-loading d-list-loading",
					cellPadding: 0,
					cellSpacing: 0,
					style: {
						position: "absolute",
						left: 0,
						top: 0,
						width: "100%",
						height: "100%",
						zIndex: 9999
					},
					content: {
						tagName: "TR",
						content: {
							tagName: "TD",
							align: "center",
							content: [{
								tagName: "DIV",
								className: "mask",
								style: {
									zIndex: 1,
									position: "absolute",
									left: 0,
									top: 0,
									width: "100%",
									height: "100%"
								}
							}, {
								tagName: "DIV",
								className: "tip",
								content: [{
									tagName: "DIV",
									className: "icon"
								}, {
									tagName: "DIV",
									className: "label",
									content: $resource("dorado.list.LoadingData")
								}],
								style: {
									zIndex: 2,
									position: "relative"
								}
							}]
						}
					}
				});
				this._dom.appendChild(tipDom);
			}
			return tipDom;
		}
		
		dorado.Toolkits.cancelDelayedAction(this, "$hideLoadingTip");
		dorado.Toolkits.setDelayedAction(this, "$showLoadingTip", function() {
			var tipDom = getLoadingTipDom.call(this);
			$fly(tipDom).show();
		}, 100);
	},
	
	hideLoadingTip: function() {
		dorado.Toolkits.cancelDelayedAction(this, "$showLoadingTip");
		if (this._loadingTipDom) {
			dorado.Toolkits.setDelayedAction(this, "$hideLoadingTip", function() {
				$fly(this._loadingTipDom).hide();
			}, 200);
		}
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 支持"视口"功能的列表型控件的抽象类。
 * <p>
 * 列表型控件的"视口"功能是一种用于提高渲染效率的功能，即只根据显示的需要创建真实可见的那一部分列表元素，
 * 并且根据用户的操作（如操作滚动栏）动态的创建其它列表元素。 利用此功能可以使列表型控件支持较大数据量的展示，且具备较高的渲染效率。
 * </p>
 * @abstract
 * @extends dorado.widget.AbstractList
 */
dorado.widget.ViewPortList = $extend(dorado.widget.AbstractList, /** @scope dorado.widget.ViewPortList.prototype */ {

	$className: "dorado.widget.ViewPortList",
	
	ATTRIBUTES: /** @scope dorado.widget.ViewPortList.prototype */ {
		/**
		 * 滚动模式。
		 * <p>
		 * 此属性具有如下几种取值：
		 * <ul>
		 * <li>simple - 表示使用简单的滚动模式。在此种滚动模式下"视口"功能将不会被启用。</li>
		 * <li>lazyRender - 懒渲染模式，类似于简单的滚动模式。
		 * 但是对于那些暂时不可见的列表项将只会创建一个空的占位对象，而不会进行具体的渲染，直到该列表项真正变为可见。<br>
		 *  懒渲染模式只在较微小的程度上影响列表控件的操作体验。
		 * </li>
		 * <li>viewport - 表示使用简单的滚动模式。在此种滚动模式下"视口"功能将被启用。<br>
		 * "视口"功能是指不创建在列表中不可见的列表项，以便于最大程度的优化列表控件初始渲染的效率。
		 * "视口"会在一定程度上影响列表控件的操作体验。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute
		 * @default "lazyRender"
		 */
		scrollMode: {
			defaultValue: "lazyRender"
		}
	},
	
	/**
	 * @name dorado.widget.ViewPortList#scrollItemDomIntoView
	 * @function
	 * @protected
	 * @param {HTMLElement} dom 列表控件中的某列表DOM元素。
	 * @description 滚动列表控件的滚动栏以便于传入的某列表DOM元素处于可见范围内。
	 */
	// =====
	
	createDom: function() {
		return this._container = $invokeSuper.call(this, arguments);
	},
	
	refreshItemDoms: function(itemDomContainer, reverse, fn, keepItemsOutOfViewPort) {
		if (!this._itemDomMap) this._itemDomMap = {};
		
		var startIndex = this._itemModel.getStartIndex();
		if (!keepItemsOutOfViewPort && this._scrollMode == "viewport" && this.startIndex >= 0) {
			var _startIndex = reverse ? this.startIndex + this.itemDomCount : this.startIndex;
			if (Math.abs(startIndex - _startIndex) < (this.itemDomCount) / 2) {
				for (var i = Math.abs(startIndex - _startIndex); i > 0; i--) {
					if (startIndex > _startIndex) { // 较近的向下移动
						itemDomContainer.appendChild(itemDomContainer.firstChild);
					} else { // 较近的向上移动
						itemDomContainer.insertBefore(itemDomContainer.lastChild, itemDomContainer.firstChild);
					}
				}
			}
		}
		
		var itemDomCount = 0;
		var it = this._itemModel.iterator();
		
		var bookmark = it.createBookmark(), viewPortFilled = false, reverseFlag = true;
		if (reverse) {
			it.next();
			reverseFlag = it.hasNext();
			reverseFlag ? it.next() : it.last();
		}
		this._shouldSkipRender = false;
		while (reverse ? it.hasPrevious() : it.hasNext()) {
			var item = reverse ? it.previous() : it.next();
			var dom = this.refreshItemDom(itemDomContainer, item, itemDomCount, reverse);
			itemDomCount++;
			if (fn && !fn(dom)) {
				if (this._scrollMode == "viewport") {
					viewPortFilled = true;
					break;
				} else {
					this._shouldSkipRender = true;
					fn = null;
				}
			}
		}
		this._shouldSkipRender = false;
		
		var fillCount = 0;
		if (!viewPortFilled && this._scrollMode == "viewport") {
			it.restoreBookmark(bookmark);
			if (reverseFlag) {
				reverse ? it.previous() : it.next();
			}
			while (reverse ? it.hasNext() : it.hasPrevious()) {
				var item = reverse ? it.next() : it.previous();
				var dom = this.refreshItemDom(itemDomContainer, item, --fillCount, !reverse);
				itemDomCount++;
				if (fn && !fn(dom)) break;
			}
		}
		
		if (!keepItemsOutOfViewPort) {
			for (var i = itemDomContainer.childNodes.length - 1; i >= itemDomCount; i--) {
				this.removeItemDom(reverse ? itemDomContainer.firstChild : itemDomContainer.lastChild);
			}
		}
		this.startIndex = reverse ? startIndex - fillCount - itemDomCount + 1 : startIndex + fillCount;
		// this._itemModel.setStartIndex(this.startIndex = reverse ? startIndex - fillCount - itemDomCount + 1 : startIndex + fillCount);
		
		this.itemCount = this._itemModel.getItemCount();
		this.itemDomCount = itemDomCount;
		return itemDomCount;
	},
	
	removeItemDom: function(dom) {
		if (this._itemDomMap[dom.itemId] == dom) delete this._itemDomMap[dom.itemId];
		$fly(dom).remove();
	},
	
	getScrollingIndicator: function() {
		var indicator = dorado.widget.ViewPortList._indicator;
		if (!indicator) {
			indicator = document.createElement("DIV");
			dorado.widget.ViewPortList._indicator = indicator;
			$fly(indicator).addClass("d-list-scrolling-indicator").hide();
			document.body.appendChild(indicator);
		}
		$fly(indicator).bringToFront();
		return indicator;
	},
	
	setScrollingIndicator: function(text) {
		var indicator = this.getScrollingIndicator();
		$fly(indicator).text(text).show();
		$DomUtils.placeCenterElement(indicator, this.getDom());
	},
	
	hideScrollingIndicator: function() {
		$fly(this.getScrollingIndicator()).hide();
	},
	
	doOnResize: function() {
		this.refresh(true);
	}
});
