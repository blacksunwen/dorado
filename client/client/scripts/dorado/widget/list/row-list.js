(function() {

	var TABLE_HEIGHT_ADJUST = (dorado.Browser.msie) ? -1 : 0;
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 具有行集特征的列表控件的抽象类。
	 * @abstract
	 * @extends dorado.widget.ViewPortList
	 */
	dorado.widget.RowList = $extend(dorado.widget.ViewPortList, /** @scope dorado.widget.RowList.prototype */ {
		$className: "dorado.widget.RowList",
		
		ATTRIBUTES: /** @scope dorado.widget.RowList.prototype */ {
			/**
			 * 默认的行高。
			 * @type int
			 * @attribute
			 */
			rowHeight: {
				defaultValue: 18
			},
			
			selection: {
				setter: function(v) {
					if (v == null && "multiRows" == this._selectionMode) v = [];
					this.replaceSelection(this.get("selection"), v);
				}
			}
		},
		
		EVENTS: /** @scope dorado.widget.RowList.prototype */ {
		
			/**
			 * 当数据行被点击时触发的事件。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onDataRowClick: {}
		},
		
		constructor: function() {
			$invokeSuper.call(this, arguments);
			if (this._itemModel) this._itemModel.setItemDomSize(this._rowHeight);
		},
		
		getIndexByItemId: function(itemId) {
			if (typeof itemId == "number") return itemId;
			else {
				var itemModel = this._itemModel;
				return itemModel.getItemIndex(itemModel.getItemById(itemId));
			}
		},
		
		getCurrentItemDom: function() {
			return this._currentRow;
		},
		
		getDraggableOptions: function(dom) {
			var options = $invokeSuper.call(this, arguments);
			if (dom == this._dom) {
				options.handle = ":first-child";
			}
			return options;
		},
		
		createDataTable: function() {
			var table = this._dataTable = $DomUtils.xCreateElement({
				tagName: "TABLE",
				cellSpacing: 0,
				cellPadding: 0,
				className: "data-table",
				content: {
					tagName: "TBODY"
				}
			});
			var tbody = this._dataTBody = table.tBodies[0];
			
			var self = this;
			$fly(table).mouseover(function(evt) {
				if ($DomUtils.isDragging()) return;
				dorado.Toolkits.cancelDelayedAction(self, "$hoverOutTimerId");
				self.setHoverRow(self.findItemDomByEvent(evt));
			}).mouseout(function(evt) {
				dorado.Toolkits.setDelayedAction(self, "$hoverOutTimerId", function() {
					self.setHoverRow(null);
				}, 50);
			});
			return table;
		},
		
		findItemDomByEvent: function(evt) {
			var target = evt.srcElement || evt.target;
			var target = target || evt, tbody = this._dataTBody;
			return $DomUtils.findParent(target, function(parentNode) {
				return parentNode.parentNode == tbody;
			});
		},
		
		onMouseDown: function(evt) {
			var row = this.findItemDomByEvent(evt);
			if (row || this._allowNoCurrent) {
				var oldCurrentItem = this.getCurrentItem();
				if (this.setCurrentItemDom(row)) {
					var clickedItem = (row ? $fly(row).data("item") : null), selection = this.get("selection");
					if (this._selectionMode == "singleRow") {
						if (evt.ctrlKey || evt.shiftKey) this.replaceSelection(null, clickedItem);
					} else if (this._selectionMode == "multiRows") {
						var removed = [], added = [];
						if (evt.ctrlKey) {
							this.addOrRemoveSelection(selection, clickedItem, removed, added);
						} else if (evt.shiftKey) {
							if (oldCurrentItem) {
								var itemModel = this._itemModel;
								var si = itemModel.getItemIndex(oldCurrentItem);
								var ei = itemModel.getItemIndex(clickedItem);
								if (si > ei) {
									var i = si;
									si = ei, ei = i;
								}
								
								var c = ei - si + 1, i = 0;
								var it = itemModel.iterator(si);
								while (it.hasNext() && i < c) {
									var item = it.next();
									if (item == oldCurrentItem) {
										if (selection.indexOf(item) < 0) added.push(item);
									} else {
										this.addOrRemoveSelection(selection, item, removed, added);
									}
									i++;
								}
							} else {
								this.addOrRemoveSelection(selection, clickedItem, removed, added);
							}
						} else {
							removed = selection;
							added.push(clickedItem);
						}
						this.replaceSelection(removed, added);
					}
				}
				
				if (dorado.Browser.msie) {
					var tbody = this._dataTBody;
					try {
						var cell = $DomUtils.findParent(evt.target, function(parentNode) {
							return parentNode.parentNode.parentNode == tbody;
						}, true);
						if (cell) ((cell.firstChild && cell.firstChild.nodeType == 1) ? cell.firstChild : cell).focus();
					} 
					catch (e) {
						evt.target.focus();
					}
				}
			}
			return false;
		},
		
		setSelection: function(selection) {
			this._selection = selection;
		},
		
		replaceSelection: function(removed, added, silence) {
			if (removed == added && removed != null) return;
			var selection = this.get("selection"), selectionMode = this._selectionMode;
			switch (selectionMode) {
				case "singleRow":{
					removed = selection;
					if (removed) this.toggleItemSelection(removed, false);
					if (added) this.toggleItemSelection(added, true);
					this.setSelection(added);
					break;
				}
				case "multiRows":{
					if (removed && selection) {
						if (removed == selection) {
							removed = selection.slice(0);
							for (var i = 0; i < selection.length; i++) {
								this.toggleItemSelection(selection[i], false);
							}
							selection = null;
						} else {
							for (var i = 0; i < removed.length; i++) {
								selection.remove(removed[i]);
								this.toggleItemSelection(removed[i], false);
							}
						}
					}
					if (selection == null) this.setSelection(selection = []);
					if (added) {
						for (var i = 0; i < added.length; i++) {
							selection.push(added[i]);
							this.toggleItemSelection(added[i], true);
						}
					}
					break;
				}
			}
			if (!silence) this.fireEvent("onSelectionChange", this, {
				removed: removed,
				added: added
			});
		},
		
		addOrRemoveSelection: function(selection, clickedObj, removed, added) {
			if (selection.indexOf(clickedObj) < 0) added.push(clickedObj);
			else removed.push(clickedObj);
		},
		
		toggleItemSelection: function(item, selected) {
			if (!this._itemDomMap) return;
			var row = this._itemDomMap[this._itemModel.getItemId(item)];
			if (row) $fly(row).toggleClass("selected-row", selected);
		},
		
		onClick: function(evt) {
			if (this.findItemDomByEvent(evt)) {
				this.fireEvent("onDataRowClick", this, {
					event: evt
				});
			}
		},
		
		setHoverRow: function(row) {
			if (row) {
				if (this._draggable && this._dragMode != "control") {
					this.applyDraggable(row);
				}
				$fly(row).addClass("hover-row");
			}
			if (this._hoverRow == row) return;
			if (this._hoverRow) $fly(this._hoverRow).removeClass("hover-row");
			this._hoverRow = row;
		},
		
		setCurrentRow: function(row) {
			if (this._currentRow == row) return;
			this.setHoverRow(null);
			if (this._currentRow) $fly(this._currentRow).removeClass("current-row");
			this._currentRow = row;
			if (row && this._highlightCurrentRow) $fly(row).addClass("current-row");
		},
		
		getItemTimestamp: function(item) {
			return (item instanceof dorado.Entity) ? item.timestamp : -1;
		},
		
		refreshItemDoms: function(tbody, reverse, fn) {
			if (this._scrollMode == "viewport") this.setCurrentRow(null);
			this._duringRefreshAll = true;
			this._selectionCache = this.get("selection");
			try {
				return $invokeSuper.call(this, arguments);
			}
			finally {
				delete this._selectionCache;
				this._duringRefreshAll = false;
			}
		},
		
		getCurrentItemIdForRefresh: function() {
			return this.getCurrentItemId();
		},
		
		refreshItemDom: function(tbody, item, index, prepend) {
			var row;
			if (index >= 0 && index < tbody.childNodes.length) {
				var i = index;
				if (prepend) i = tbody.childNodes.length - i - 1;
				row = tbody.childNodes[i];
				if (this._itemDomMap[row.itemId] == row) delete this._itemDomMap[row.itemId];
			} else {
				row = this.createItemDom(item);
				prepend ? tbody.insertBefore(row, tbody.firstChild) : tbody.appendChild(row);
			}
			
			var flag = prepend ? -1 : 1;
			if (index < 0) flag = -flag;
			index = this._itemModel.getStartIndex() + index * flag;
			var itemId = this._itemModel.getItemId(item, index);
			this._itemDomMap[itemId] = row;
			row.itemIndex = index;
			row.itemId = itemId;
			
			var $row = $fly(row);
			$row.data("item", item);
			
			$row.toggleClass("odd-row", (!this._itemModel.groups && (index % 2) == 1));
			if (itemId == this.getCurrentItemIdForRefresh()) {
				this.setCurrentRow(row);
			}
			if (this._selectionMode != "none") {
				var selection = this._selectionCache || this.get("selection");
				switch (this._selectionMode) {
					case "singleRow":{
						$row.toggleClass("selected-row", selection == item);
						break;
					}
					case "multiRows":{
						$row.toggleClass("selected-row", selection.indexOf(item) >= 0);
						break;
					}
				}
			}
			this.refreshItemDomData(row, item);
			return row;
		},
		
		refreshItemDomData: function(row, item) {
			if (row._lazyRender) return;
			var timestamp = this.getItemTimestamp(item);
			if (this._ignoreItemTimestamp || timestamp <= 0 || row.timestamp != timestamp) {
				this.doRefreshItemDomData(row, item);
				row.timestamp = timestamp;
			}
		},
		
		refreshContent: function(container) {
			if (!this._dataTable) {
				var table = this.createDataTable();
				container.appendChild(table);
			}
			if (this._scrollMode == "viewport") {
				var beginBlankRow = this._beginBlankRow;
				var endBlankRow = this._endBlankRow;
				if (beginBlankRow) beginBlankRow.parentNode.style.display = "none";
				if (endBlankRow) endBlankRow.parentNode.style.display = "none";
				this._itemModel.setScrollPos(0);
			}
			
			var fn;
			if (this._scrollMode == "lazyRender" && container.clientHeight > 0) {
				var count = parseInt(container.clientHeight / this._rowHeight), i = 0;
				fn = function(row) {
					i++;
					return i <= count;
				};
			}
			this.refreshItemDoms(this._dataTBody, false, fn);
		},
		
		refreshViewPortContent: function(container) {
			var beginBlankRow = this._beginBlankRow;
			var endBlankRow = this._endBlankRow;
			
			if (!this._dataTable) container.appendChild(this.createDataTable());
			if (!beginBlankRow) {
				this._beginBlankRow = beginBlankRow = $DomUtils.xCreateElement({
					tagName: "TR",
					className: "preparing-area",
					content: "^TD"
				});
				var thead = document.createElement("THEAD");
				thead.appendChild(beginBlankRow);
				container.firstChild.appendChild(thead);
			}
			if (!endBlankRow) {
				this._endBlankRow = endBlankRow = $DomUtils.xCreateElement({
					tagName: "TR",
					className: "preparing-area",
					content: "^TD"
				});
				var tfoot = document.createElement("TFOOT");
				tfoot.appendChild(endBlankRow);
				container.firstChild.appendChild(tfoot);
			}
			
			var tbody = this._dataTBody;
			var itemModel = this._itemModel, itemCount = itemModel.getItemCount();
			var clientHeight = (container.scrollWidth > container.clientWidth) ? container.offsetHeight : container.clientHeight;
			var viewPortHeight, itemDomCount, self = this;
			
			if (clientHeight) {
				viewPortHeight = TABLE_HEIGHT_ADJUST;
				itemDomCount = this.refreshItemDoms(tbody, itemModel.isReverse(), function(row) {
					viewPortHeight += row.offsetHeight;
					if (dorado.Browser.safari) viewPortHeight -= 2;
					return viewPortHeight <= (clientHeight + 0/*self._rowHeight*/);
				});
			} else {
				itemDomCount = viewPortHeight = 0;
			}
			this._itemDomCount = itemDomCount;
			
			if (!this._skipProcessBlankRows) {
				var startIndex = this.startIndex;
				var cols = this._cols || 1;
				var rowHeightAverage = (itemDomCount > 0) ? viewPortHeight / itemDomCount : this._rowHeight;
				with (beginBlankRow) {
					if (startIndex > 0) {
						firstChild.colSpan = cols;
						firstChild.style.height = Math.round(startIndex * rowHeightAverage) + "px";
						parentNode.style.display = "";
					} else {
						parentNode.style.display = "none";
						firstChild.style.height = "0px";
					}
				}
				with (endBlankRow) {
					if ((itemDomCount + startIndex) < itemCount) {
						firstChild.colSpan = cols;
						firstChild.style.height = Math.round((itemCount - itemDomCount - startIndex) * rowHeightAverage) +
						"px";
						parentNode.style.display = "";
					} else {
						parentNode.style.display = "none";
						firstChild.style.height = "0px";
					}
				}
				
				var st;
				if (this.startIndex >= itemModel.getStartIndex()) {
					st = this._dataTBody.offsetTop;
				} else {
					st = this._dataTBody.offsetTop + this._dataTBody.offsetHeight - container.clientHeight;
				}
				container.scrollTop = this._scrollTop = st;
				
				var scrollHeight = container.scrollHeight;
				itemModel.setScrollSize(container.clientHeight, scrollHeight);
				this._rowHeightAverage = rowHeightAverage;
			}
		},
		
		_watchScroll: function() {
			delete this._watchScrollTimerId;
			if (this._scrollMode == "simple") return;
			
			var container = this._container;
			if (container.scrollLeft == 0 && container.scrollTop == 0 && container.offsetWidth > 0) {
				container.scrollLeft = this._scrollLeft || 0;
				container.scrollTop = this._scrollTop || 0;
			}
			if (this._scrollTop) {
				this._watchScrollTimerId = $setTimeout(this, this._watchScroll, 300);
			}
		},
		
		onScroll: function() {
			if (this._scrollMode == "simple") return;
			
			var container = this._container;
			if ((this._scrollLeft || 0) != container.scrollLeft) {
				if (this.onXScroll) this.onXScroll();
			}
			if ((this._scrollTop || 0) != container.scrollTop) {
				this.onYScroll();
			}
			
			if (this._watchScrollTimerId) {
				clearTimeout(this._watchScrollTimerId);
				delete this._watchScrollTimerId;
			}
			if (container.scrollTop) {
				this._watchScrollTimerId = $setTimeout(this, this._watchScroll, 300);
			}
			this._scrollLeft = container.scrollLeft;
			this._scrollTop = container.scrollTop;
		},
		
		onYScroll: function() {
			var container = this._container;
			if (container.scrollTop == (container._scrollTop || 0)) return;
			
			if (this._scrollMode == "viewport") {
				if (dorado.Toolkits.cancelDelayedAction(container, "$scrollTimerId")) {
					if (Math.abs(container.scrollTop - container._scrollTop) > (container.clientHeight / 2)) {
						var itemCount = this._itemModel.getItemCount();
						var rowHeight = container.scrollHeight / itemCount;
						this.setScrollingIndicator((Math.round(container.scrollTop / rowHeight) + 1) + "/" + itemCount);
					}
				} else {
					container._scrollTop = container.scrollTop;
				}
				var self = this;
				dorado.Toolkits.setDelayedAction(container, "$scrollTimerId", function() {
					self.doOnYScroll();
				}, 300);
			} else {
				container._scrollTop = container.scrollTop;
				this.doOnYScroll();
			}
		},
		
		doOnYScroll: function() {
			var container = this._container;
			if (this._scrollMode == "viewport") {
				dorado.Toolkits.cancelDelayedAction(container, "$scrollTimerId");
				this._itemModel.setScrollPos(container.scrollTop);
				this.setHoverRow(null);
				this.refresh();
				this.hideScrollingIndicator();
			} else if (this._scrollMode == "lazyRender") {
				var rows = this._dataTBody.rows;
				var i = parseInt(container.scrollTop / this._rowHeight) || 0;
				if (i >= rows.length) i = rows.length - 1;
				var row = rows[i];
				if (!row) return;
				while (row.offsetTop > container.scrollTop) {
					i--;
					row = rows[i];
				}
				var bottom = container.scrollTop + container.clientHeight;
				while (row && row.offsetTop < bottom) {
					if (row._lazyRender) {
						var item = $fly(row).data("item");
						this.createItemDomDetail(row, item);
						delete row._lazyRender;
						this.refreshItemDomData(row, item);
					}
					i++;
					row = rows[i];
				}
			}
		},
		
		createDom: function() {
			var dom = $invokeSuper.call(this, arguments);
			if (dorado.Browser.msie && dorado.Browser.version >= '8') dom.hideFocus = true;
			$fly(dom).bind("scroll", $scopify(this, this.onScroll));
			return dom;
		},
		
		refreshDom: function(dom) {
			var dynaHeight = !dom.style.height || !dom.style.width, oldHeight;
			if (dynaHeight) {
				oldHeight = dom.offsetHeight;
			}
			
			$invokeSuper.call(this, arguments);
			
			if (this._scrollMode == "viewport") {
				this.refreshViewPortContent(dom);
			} else {
				this.refreshContent(dom);
			}
			
			if (this._currentScrollMode && this._currentScrollMode != this._scrollMode && !this.getCurrentItemId()) {
				this.onYScroll();
			}
			this._currentScrollMode = this._scrollMode;
			
			if (this._currentRow) this.scrollItemDomIntoView(this._currentRow);
			if (dorado.Browser.msie) {
				$DomUtils.fixMsieYScrollBar(dom);
				if (!this._itemModelFixed && this._scrollMode == "viewport") {
					this._itemModel.setScrollSize(dom.clientHeight, dom.scrollHeight);
				}
			}
			
			if (dynaHeight && oldHeight != dom.offsetHeight) {
				this.notifySizeChange();
			}
			
			delete this._ignoreItemTimestamp;
		},
		
		notifySizeChange: function() {
			if (!this._dom.style.height || !this._dom.style.width) $invokeSuper.call(this, arguments);
		},
		
		scrollItemDomIntoView: function(row) {
			with (this._container) {
				var st = -1;
				if ((row.offsetTop + row.offsetHeight) > (scrollTop + clientHeight)) {
					st = row.offsetTop + row.offsetHeight - clientHeight;
				} else if (row.offsetTop < scrollTop) {
					st = row.offsetTop;
				}
				if (st >= 0) {
					if (this._scrollMode != "lazyRender") this._scrollTop = st;
					scrollTop = st;
				}
			}
		},
		
		scrollCurrentIntoView: function() {
			var currentItemId = this.getCurrentItemId();
			if (currentItemId != null) {
				var row = this._currentRow;
				if (row) {
					this.scrollItemDomIntoView(row);
				} else if (this._scrollMode == "viewport") {
					var itemModel = this._itemModel;
					var index = this.getIndexByItemId(currentItemId);
					if (index < 0) index = 0;
					itemModel.setStartIndex(index);
					var oldReverse = itemModel.isReverse();
					itemModel.setReverse(index >= this.startIndex);
					this.refresh();
					itemModel.setReverse(oldReverse);
				}
			}
		},
		
		findItemDomByPosition: function(pos) {
			var dom = this._dom, y = pos.y + dom.scrollTop, row = null;
			var rows = this._dataTBody.rows, rowHeight = this._rowHeightAverage || this._rowHeight, i;
			if (this._scrollMode == "viewport") {
				var relativeY = y;
				if (this._beginBlankRow) relativeY -= this._beginBlankRow.offsetHeight;
				i = parseInt(relativeY / rowHeight);
			} else {
				i = parseInt(y / rowHeight);
			}
			
			if (i < 0) i = 0;
			else if (i >= rows.length) i = rows.length - 1;
			row = rows[i];
			while (row) {
				if (row.offsetTop > y) {
					row = row.previousSibling;
				} else if (row.offsetTop + row.offsetHeight < y) {
					if (row.nextSibling) {
						row = row.nextSibling;
					} else {
						row._dropY = y - row.offsetTop;
						break;
					}
				} else {
					row._dropY = y - row.offsetTop;
					break;
				}
			}
			return row;
		},
		
		setDraggingOverItemDom: function(itemDom) {
			if (this._draggingOverItemDom == itemDom) return;
			if (this._draggingOverItemDom) $fly(this._draggingOverItemDom).removeClass("drag-over-row");
			this._draggingOverItemDom = itemDom;
			if (itemDom) $fly(itemDom).addClass("drag-over-row");
		},
		
		showDraggingInsertIndicator: function(draggingInfo, insertMode, itemDom) {
			var insertIndicator = dorado.widget.RowList.getDraggingInsertIndicator();
			var $insertIndicator = $fly(insertIndicator);
			if (insertMode) {
				var dom = this._dom;
				var width = dom.offsetWidth;
				var top = (insertMode == "before") ? itemDom.offsetTop : (itemDom.offsetTop + itemDom.offsetHeight);
				if (dom.clientWidth < width) width = dom.clientWidth;
				$insertIndicator.width(width).height(2).left(0).top(top - 1).show().appendTo(dom);
			} else {
				$insertIndicator.hide();
			}
		},
		
		doOnDraggingSourceMove: function(draggingInfo, evt, targetObject, insertMode, refObject, itemDom) {
			var accept = (draggingInfo.isDropAcceptable(this._droppableTags) && !(this._dropMode == "onItem" && targetObject == null));
			draggingInfo.set({
				targetObject: targetObject,
				insertMode: insertMode,
				refObject: refObject,
				accept: accept
			});
			
			var eventArg = {
				draggingInfo: draggingInfo,
				event: evt,
				processDefault: true
			};
			this.fireEvent("onDraggingSourceMove", this, eventArg);
			
			if (accept && eventArg.processDefault) {
				this.showDraggingInsertIndicator(draggingInfo, insertMode, itemDom);
				this.setDraggingOverItemDom((accept && !insertMode) ? itemDom : null);
			}
			return eventArg.processDefault;
		},
		
		onDraggingSourceMove: function(draggingInfo, evt) {
			var dropMode = this._dropMode;
			var targetObject = draggingInfo.get("targetObject");
			var insertMode, refObject, itemDom;
			if (dropMode != "onControl") {
				var pos = this.getMousePosition(evt);
				itemDom = this.findItemDomByPosition(pos);
				if (itemDom && $fly(itemDom).data("item") == draggingInfo.get("object")) {
					itemDom = null;
				}
				
				if (itemDom) {
					if (dropMode == "insertItems") {
						var dropY = itemDom._dropY;
						var halfHeight = itemDom.offsetHeight / 2;
						insertMode = (dropY < halfHeight) ? "before" : "after";
					} else if (dropMode == "onOrInsertItems") {
						var dropY = itemDom._dropY;
						if (dropY <= 4) insertMode = "before";
						else if ((itemDom.offsetHeight - dropY) <= 4) insertMode = "after";
					}
				}
				
				refObject = itemDom ? $fly(itemDom).data("item") : null;
				if (!refObject) {
					targetObject = (dropMode == "onAnyWhere") ? this : null;
				} else {
					targetObject = refObject;
				}
			}
			return this.doOnDraggingSourceMove(draggingInfo, evt, targetObject, insertMode, refObject, itemDom);
		},
		
		onDraggingSourceOut: function(draggingInfo, evt) {
			$invokeSuper.call(this, arguments);
			this.setDraggingOverItemDom();
			this.showDraggingInsertIndicator();
		},
		
		processItemDrop: function(draggingInfo, evt) {
		
			function getItemList(control, entity) {
				var list;
				if (entity instanceof dorado.Entity) {
					list = entity.parent;
				} else if (control.ATTRIBUTES.itemModel) {
					var itemModel = control.get("itemModel");
					if (itemModel) list = itemModel.getItems();
				}
				return list;
			}
			
			var object = draggingInfo.get("object");
			var insertMode = draggingInfo.get("insertMode");
			var refObject = draggingInfo.get("refObject");
			var sourceControl = draggingInfo.get("sourceControl");
			if (insertMode && refObject || this._dropMode == "insertItems" || this._dropMode == "onOrInsertItems" || this._dropMode == "onAnyWhere") {
				var sourceList = getItemList(sourceControl, object);
				if (sourceList) {
					sourceList.remove(object, true);
					if (!dorado.Object.isInstanceOf(sourceControl, dorado.widget.DataControl)) {
						sourceControl.refresh();
					}
				}
				if (object instanceof dorado.Entity) object.setState(dorado.Entity.STATE_MOVED);
				
				var targetList = this.get("itemModel").getItems(), highlight;
				if (!targetList && !dorado.Object.isInstanceOf(this, dorado.widget.DataControl) && this.ATTRIBUTES.items) {
					targetList = [];
					this.set("items", targetList);
				}
				
				if (targetList) {
					if (targetList instanceof dorado.EntityList) {
						targetList.insert(object, insertMode, refObject);
						highlight = object;
					} else {
						var i = refObject ? targetList.indexOf(refObject) : -1;
						if (i < 0) {
							targetList.push(object);
							highlight = targetList.length - 1;
						} else {
							if (insertMode == "after") i++;
							targetList.insert(object, i);
							highlight = i;
						}
					}
					if (!dorado.Object.isInstanceOf(this, dorado.widget.DataControl)) {
						this.refresh();
					}
					if (highlight != null) this.highlightItem(highlight);
					return true;
				}
			}
			return false;
		},
		
		onDraggingSourceDrop: function(draggingInfo, evt) {
			var dropped = $invokeSuper.call(this, arguments);
			if (!dropped) dropped = this.processItemDrop(draggingInfo, evt);
			return dropped;
		}
	});
	
	dorado.widget.RowList.getDraggingInsertIndicator = function() {
		var indicator = this._draggingInsertIndicator;
		if (indicator == null) {
			indicator = $DomUtils.xCreateElement({
				tagName: "div",
				className: "d-list-dragging-insert-indicator"
			});
			this._draggingInsertIndicator = indicator;
		}
		return indicator;
	};
	
})();
