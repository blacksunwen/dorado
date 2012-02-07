
dorado.widget.list = {};
dorado.widget.AbstractList = $extend(dorado.widget.Control, {$className:"dorado.widget.AbstractList", focusable:true, ATTRIBUTES:{itemModel:{}, highlightCurrentRow:{defaultValue:true}, allowNoCurrent:{skipRefresh:true}, selectionMode:{defaultValue:"none", skipRefresh:true, setter:function (p, v) {
    this.replaceSelection(this.get("selection"), null, true);
    this._selectionMode = v;
    this.replaceSelection(this.get("selection"), null);
}}, selection:{}, dragMode:{writeBeforeReady:true, defaultValue:"item"}, dropMode:{writeBeforeReady:true, defaultValue:"insertItems"}}, EVENTS:{onCurrentChange:{}, onSelectionChange:{}}, constructor:function () {
    this._itemModel = this.createItemModel();
    $invokeSuper.call(this, arguments);
}, createItemModel:function () {
    return new dorado.widget.list.ItemModel();
}, createDom:function () {
    var dom = $invokeSuper.call(this, arguments);
    if (!dorado.Browser.msie) {
        dom.style.overflow = "auto";
    }
    return dom;
}, applyDraggable:function (dom, options) {
    if (this._droppable && dom == this._dom && this._dragMode == "item") {
        return;
    }
    $invokeSuper.call(this, arguments);
}, initDraggingInfo:function (draggingInfo, evt) {
    $invokeSuper.call(this, arguments);
    if (this._dragMode != "control") {
        var itemDom = this.findItemDomByEvent(evt);
        draggingInfo.set({object:itemDom ? $fly(itemDom).data("item") : null, insertMode:null, refObject:null});
    }
}, initDraggingIndicator:function (indicator, draggingInfo, evt) {
    if (this._dragMode != "control") {
        var itemDom = draggingInfo.get("element");
        if (itemDom) {
            var contentDom = $DomUtils.xCreateElement({tagName:"div", className:"d-list-dragging-item"});
            $fly(itemDom).find(">*").clone().appendTo(contentDom);
            indicator.set("content", contentDom);
        }
    }
}});
dorado.widget.ViewPortList = $extend(dorado.widget.AbstractList, {$className:"dorado.widget.ViewPortList", ATTRIBUTES:{scrollMode:{defaultValue:"lazyRender"}}, createDom:function () {
    return this._container = $invokeSuper.call(this, arguments);
}, refreshItemDoms:function (itemDomContainer, reverse, fn, keepItemsOutOfViewPort) {
    if (!this._itemDomMap) {
        this._itemDomMap = {};
    }
    var startIndex = this._itemModel.getStartIndex();
    if (!keepItemsOutOfViewPort && this._scrollMode == "viewport" && this.startIndex >= 0) {
        var _startIndex = reverse ? this.startIndex + this.itemDomCount : this.startIndex;
        if (Math.abs(startIndex - _startIndex) < (this.itemDomCount) / 2) {
            for (var i = Math.abs(startIndex - _startIndex); i > 0; i--) {
                if (startIndex > _startIndex) {
                    itemDomContainer.appendChild(itemDomContainer.firstChild);
                } else {
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
        if (reverseFlag) {
            it.next();
        }
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
            if (fn && !fn(dom)) {
                break;
            }
        }
    }
    if (!keepItemsOutOfViewPort) {
        for (var i = itemDomContainer.childNodes.length - 1; i >= itemDomCount; i--) {
            this.removeItemDom(reverse ? itemDomContainer.firstChild : itemDomContainer.lastChild);
        }
    }
    this.startIndex = reverse ? startIndex - fillCount - itemDomCount + 1 : startIndex + fillCount;
    this.itemCount = this._itemModel.getItemCount();
    this.itemDomCount = itemDomCount;
    return itemDomCount;
}, removeItemDom:function (dom) {
    if (this._itemDomMap[dom.itemId] == dom) {
        delete this._itemDomMap[dom.itemId];
    }
    jQuery(dom).remove();
}, getScrollingIndicator:function () {
    var indicator = dorado.widget.ViewPortList._indicator;
    if (!indicator) {
        indicator = document.createElement("DIV");
        dorado.widget.ViewPortList._indicator = indicator;
        $fly(indicator).addClass("d-list-scrolling-indicator").hide();
        document.body.appendChild(indicator);
    }
    $fly(indicator).bringToFront();
    return indicator;
}, setScrollingIndicator:function (text) {
    var indicator = this.getScrollingIndicator();
    $fly(indicator).text(text).show();
    $DomUtils.placeCenterElement(indicator, this.getDom());
}, hideScrollingIndicator:function () {
    $fly(this.getScrollingIndicator()).hide();
}, onResize:function () {
    this.refresh(true);
}});
(function () {
    valueComparators = {};
    dorado.widget.list.ItemModel = $class({$className:"dorado.widget.list.ItemModel", EMPTY_ITERATOR:new dorado.util.ArrayIterator([]), _itemDomSize:0, _viewPortSize:0, _scrollSize:0, _scrollPos:0, _startIndex:0, _reverse:false, setItemDomSize:function (itemDomSize) {
        this._itemDomSize = itemDomSize;
    }, getStartIndex:function () {
        return this._startIndex;
    }, setStartIndex:function (startIndex) {
        this._startIndex = startIndex;
    }, isReverse:function () {
        return this._reverse;
    }, setReverse:function (reverse) {
        this._reverse = reverse;
    }, setScrollSize:function (viewPortSize, scrollSize) {
        this._viewPortSize = viewPortSize;
        this._scrollSize = scrollSize;
    }, setScrollPos:function (scrollPos) {
        var itemCount = this.getItemCount();
        if (itemCount > 0) {
            var itemDomSize = this._scrollSize / itemCount;
            if (scrollPos / (this._scrollSize - this._viewPortSize) > 0.5) {
                this._startIndex = itemCount - 1 - (Math.round((this._scrollSize - this._viewPortSize - scrollPos) / itemDomSize) || 0);
                if (this._startIndex > itemCount - 1) {
                    this._startIndex = itemCount - 1;
                }
                this._reverse = true;
            } else {
                this._startIndex = Math.round(scrollPos / itemDomSize) || 0;
                this._reverse = false;
            }
        } else {
            this._startIndex = 0;
            this._reverse = false;
        }
    }, getItems:function () {
        return this._items;
    }, setItems:function (items) {
        this.filter();
        if (items instanceof dorado.EntityList) {
            this._items = items;
        } else {
            if (items != null) {
                this._items = (items instanceof Array) ? items : [items];
            } else {
                this._items = [];
            }
        }
    }, iterator:function (startIndex) {
        var items = this._items;
        if (!items) {
            return this.EMPTY_ITERATOR;
        }
        var index = startIndex;
        if (index == null) {
            index = this._startIndex || 0;
        }
        if (items instanceof Array) {
            return new dorado.util.ArrayIterator(this._items, index);
        } else {
            return items.iterator({nextIndex:index});
        }
    }, getItemCount:function () {
        var items = this._items;
        if (!items) {
            return 0;
        }
        return (items instanceof Array) ? items.length : ((items.pageSize > 0 && items.entityCount > items.pageSize) ? items.pageSize : items.entityCount);
    }, getItemAt:function (index) {
        if (!this._items || !(index >= 0)) {
            return null;
        }
        if (this._items instanceof Array) {
            return this._items[index];
        } else {
            var entityList = this._items;
            return entityList.iterator({nextIndex:index}).next();
        }
    }, getItemById:function (itemId) {
        if (this._items instanceof Array) {
            return this._items.indexOf(itemId);
        } else {
            return this._items.getById(itemId);
        }
    }, getItemIndex:function (item) {
        if (!this._items || !item) {
            return -1;
        }
        if (this._items instanceof Array) {
            return this._items.indexOf(item);
        } else {
            var entityList = this._items, itemId = item.entityId, page = item.page;
            if (page.entityList != entityList) {
                return -1;
            }
            var index = 0, entry = page.first;
            while (entry != null) {
                if (entry.data.entityId == itemId) {
                    return index;
                }
                if (entry.data.state != dorado.Entity.STATE_DELETED) {
                    index++;
                }
                entry = entry.next;
            }
            return -1;
        }
    }, getItemId:function (item, index) {
        if (this._items instanceof Array || !(item instanceof dorado.Entity)) {
            return (index >= 0) ? index : this.getItemIndex(item);
        } else {
            return item.entityId;
        }
    }, sort:function (sortParams, comparator) {
        if (!this.getItemCount()) {
            return;
        }
        var items = this.toArray();
        dorado.DataUtil.sort(items, sortParams, comparator);
        if (!(this._items instanceof Array)) {
            this._items = items;
        }
    }, filter:function (filterParams) {
        function getValueComparator(op) {
            var comparator = valueComparators[escape(op)];
            if (!comparator) {
                valueComparators[escape(op)] = comparator = new Function("v1,v2", "return v1" + ((op == "=") ? "==" : op) + "v2");
            }
            return comparator;
        }
        function filterEntity(entity, filterParam) {
            var value = (entity instanceof dorado.Entity) ? entity.get(filterParam.property) : entity[filterParam.property];
            var op = filterParam.operator;
            if (!op) {
                op = (typeof value == "string") ? "like" : "=";
            }
            if (op == "like") {
                return (value + "").indexOf(filterParam.value) >= 0;
            } else {
                return getValueComparator(op)(value, filterParam.value);
            }
        }
        if (filterParams && filterParams.length > 0) {
            if (this._originItems) {
                this._items = this._originItems;
            } else {
                this._originItems = this._items;
            }
            var filtered = [];
            for (var it = this.iterator(0); it.hasNext(); ) {
                var entity = it.next(), passed = true;
                for (var i = 0; i < filterParams.length; i++) {
                    if (!filterEntity(entity, filterParams[i])) {
                        passed = false;
                        break;
                    }
                }
                if (passed) {
                    filtered.push(entity);
                }
            }
            this._items = filtered;
        } else {
            if (this._originItems) {
                this._items = this._originItems;
                delete this._originItems;
            }
        }
    }, toArray:function () {
        if (this._items instanceof Array) {
            return this._items;
        } else {
            if (this._items instanceof dorado.EntityList) {
                return this._items.toArray();
            } else {
                var v = [];
                for (var it = this.iterator(0); it.hasNext(); ) {
                    v.push(it.next());
                }
                return v;
            }
        }
    }});
})();
(function () {
    var TABLE_HEIGHT_ADJUST = (dorado.Browser.msie) ? -1 : 0;
    dorado.widget.RowList = $extend(dorado.widget.ViewPortList, {$className:"dorado.widget.RowList", ATTRIBUTES:{rowHeight:{defaultValue:18}, selection:{setter:function (p, v) {
        if (v == null && "multiRows" == this._selectionMode) {
            v = [];
        }
        this.replaceSelection(this.get("selection"), v);
    }}}, EVENTS:{onDataRowClick:{}}, constructor:function () {
        $invokeSuper.call(this, arguments);
        if (this._itemModel) {
            this._itemModel.setItemDomSize(this._rowHeight);
        }
    }, getIndexByItemId:function (itemId) {
        if (typeof itemId == "number") {
            return itemId;
        } else {
            var itemModel = this._itemModel;
            return itemModel.getItemIndex(itemModel.getItemById(itemId));
        }
    }, getCurrentItemDom:function () {
        return this._currentRow;
    }, getDraggableOptions:function (dom) {
        var options = $invokeSuper.call(this, arguments);
        if (dom == this._dom) {
            options.handle = ":first-child";
        }
        return options;
    }, createDataTable:function () {
        var table = this._dataTable = $DomUtils.xCreateElement({tagName:"TABLE", cellSpacing:0, cellPadding:0, className:"data-table", content:{tagName:"TBODY"}});
        var tbody = this._dataTBody = table.tBodies[0];
        var self = this;
        $fly(table).mouseover(function (evt) {
            if ($DomUtils.isDragging()) {
                return;
            }
            dorado.Toolkits.cancelDelayedAction(self, "$hoverOutTimerId");
            self.setHoverRow(self.findItemDomByEvent(evt));
        }).mouseout(function (evt) {
            dorado.Toolkits.setDelayedAction(self, "$hoverOutTimerId", function () {
                self.setHoverRow(null);
            }, 50);
        });
        return table;
    }, findItemDomByEvent:function (evt) {
        var target = evt.srcElement || evt.target;
        var target = target || evt, tbody = this._dataTBody;
        return $DomUtils.findParent(target, function (parentNode) {
            return parentNode.parentNode == tbody;
        });
    }, onMouseDown:function (evt) {
        var row = this.findItemDomByEvent(evt);
        if (row || this._allowNoCurrent) {
            var oldCurrentItem = this.getCurrentItem();
            if (this.setCurrentItemDom(row)) {
                var clickedItem = (row ? $fly(row).data("item") : null), selection = this.get("selection");
                if (this._selectionMode == "singleRow") {
                    this.replaceSelection(null, clickedItem);
                } else {
                    if (this._selectionMode == "multiRows") {
                        var removed = [], added = [];
                        if (evt.ctrlKey) {
                            this.addOrRemoveSelection(selection, clickedItem, removed, added);
                        } else {
                            if (evt.shiftKey) {
                                if (oldCurrentItem) {
                                    var itemModel = this._itemModel;
                                    var si = itemModel.getItemIndex(oldCurrentItem);
                                    var ei = itemModel.getItemIndex(clickedItem);
                                    if (si > ei) {
                                        var i = si;
                                        si = ei, ei = i;
                                    }
                                    var c = ei - si + 1, i = 0;
                                    itemModel.setStartIndex(si);
                                    var it = itemModel.iterator(si);
                                    while (it.hasNext() && i < c) {
                                        var item = it.next();
                                        if (item != oldCurrentItem) {
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
                        }
                        this.replaceSelection(removed, added);
                    }
                }
            }
            if (dorado.Browser.msie) {
                var tbody = this._dataTBody;
                try {
                    var cell = $DomUtils.findParent(evt.target, function (parentNode) {
                        return parentNode.parentNode.parentNode == tbody;
                    }, true);
                    if (cell) {
                        ((cell.firstChild && cell.firstChild.nodeType == 1) ? cell.firstChild : cell).focus();
                    }
                }
                catch (e) {
                    evt.target.focus();
                }
            }
        }
        return false;
    }, setSelection:function (selection) {
        this._selection = selection;
    }, replaceSelection:function (removed, added, silence) {
        if (removed == added && removed != null) {
            return;
        }
        var selection = this.get("selection"), selectionMode = this._selectionMode;
        switch (selectionMode) {
          case "singleRow":
            removed = selection;
            if (removed) {
                this.toggleItemSelection(removed, false);
            }
            if (added) {
                this.toggleItemSelection(added, true);
            }
            this.setSelection(added);
            break;
          case "multiRows":
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
            if (selection == null) {
                this.setSelection(selection = []);
            }
            if (added) {
                for (var i = 0; i < added.length; i++) {
                    selection.push(added[i]);
                    this.toggleItemSelection(added[i], true);
                }
            }
            break;
        }
        if (!silence) {
            this.fireEvent("onSelectionChange", this, {removed:removed, added:added});
        }
    }, addOrRemoveSelection:function (selection, clickedObj, removed, added) {
        if (selection.indexOf(clickedObj) < 0) {
            added.push(clickedObj);
        } else {
            removed.push(clickedObj);
        }
    }, toggleItemSelection:function (item, selected) {
        if (!this._itemDomMap) {
            return;
        }
        var row = this._itemDomMap[this._itemModel.getItemId(item)];
        if (row) {
            $fly(row).toggleClass("selected-row", selected);
        }
    }, onClick:function (evt) {
        if (this.findItemDomByEvent(evt)) {
            this.fireEvent("onDataRowClick", this, {event:evt});
        }
    }, setHoverRow:function (row) {
        if (row) {
            if (this._draggable && this._dragMode != "control") {
                this.applyDraggable(row);
            }
            $fly(row).addClass("hover-row");
        }
        if (this._hoverRow == row) {
            return;
        }
        if (this._hoverRow) {
            $fly(this._hoverRow).removeClass("hover-row");
        }
        this._hoverRow = row;
    }, setCurrentRow:function (row) {
        if (this._currentRow == row) {
            return;
        }
        this.setHoverRow(null);
        if (this._currentRow) {
            $fly(this._currentRow).removeClass("current-row");
        }
        this._currentRow = row;
        if (row && this._highlightCurrentRow) {
            $fly(row).addClass("current-row");
        }
    }, getItemTimestamp:function (item) {
        return (item instanceof dorado.Entity) ? item.timestamp : -1;
    }, refreshItemDoms:function (tbody, reverse, fn) {
        if (this._scrollMode == "viewport") {
            this.setCurrentRow(null);
        }
        this._duringRefreshAll = true;
        this._selectionCache = this.get("selection");
        try {
            return $invokeSuper.call(this, arguments);
        }
        finally {
            delete this._selectionCache;
            this._duringRefreshAll = false;
        }
    }, refreshItemDom:function (tbody, item, index, prepend) {
        var row;
        if (index >= 0 && index < tbody.childNodes.length) {
            var i = index;
            if (prepend) {
                i = tbody.childNodes.length - i - 1;
            }
            row = tbody.childNodes[i];
            if (this._itemDomMap[row.itemId] == row) {
                delete this._itemDomMap[row.itemId];
            }
        } else {
            row = this.createItemDom(item);
            prepend ? tbody.insertBefore(row, tbody.firstChild) : tbody.appendChild(row);
        }
        var flag = prepend ? -1 : 1;
        if (index < 0) {
            flag = -flag;
        }
        index = this._itemModel.getStartIndex() + index * flag;
        var itemId = this._itemModel.getItemId(item, index);
        this._itemDomMap[itemId] = row;
        row.itemIndex = index;
        row.itemId = itemId;
        var $row = $fly(row);
        $row.data("item", item);
        $row.toggleClass("odd-row", (!this._itemModel.groups && (index % 2) == 1));
        if (itemId == this.getCurrentItemId()) {
            this.setCurrentRow(row);
        }
        if (this._selectionMode != "none") {
            var selection = this._selectionCache || this.get("selection");
            switch (this._selectionMode) {
              case "singleRow":
                $row.toggleClass("selected-row", selection == item);
                break;
              case "multiRows":
                $row.toggleClass("selected-row", selection.indexOf(item) >= 0);
                break;
            }
        }
        this.refreshItemDomData(row, item);
        return row;
    }, refreshItemDomData:function (row, item) {
        if (row._lazyRender) {
            return;
        }
        var timestamp = this.getItemTimestamp(item);
        if (this._ignoreItemTimestamp || timestamp <= 0 || row.timestamp != timestamp) {
            this.doRefreshItemDomData(row, item);
            row.timestamp = timestamp;
        }
    }, refreshContent:function (container) {
        if (!this._dataTable) {
            var table = this.createDataTable();
            container.appendChild(table);
        }
        if (this._scrollMode == "viewport") {
            var beginBlankRow = this._beginBlankRow;
            var endBlankRow = this._endBlankRow;
            if (beginBlankRow) {
                beginBlankRow.parentNode.style.display = "none";
            }
            if (endBlankRow) {
                endBlankRow.parentNode.style.display = "none";
            }
            this._itemModel.setScrollPos(0);
        }
        var fn;
        if (this._scrollMode == "lazyRender" && container.clientHeight > 0) {
            var count = parseInt(container.clientHeight / this._rowHeight), i = 0;
            fn = function (row) {
                i++;
                return i <= count;
            };
        }
        this.refreshItemDoms(this._dataTBody, false, fn);
    }, refreshViewPortContent:function (container) {
        var beginBlankRow = this._beginBlankRow;
        var endBlankRow = this._endBlankRow;
        if (!this._dataTable) {
            container.appendChild(this.createDataTable());
        }
        if (!beginBlankRow) {
            this._beginBlankRow = beginBlankRow = $DomUtils.xCreateElement({tagName:"TR", className:"preparing-area", content:"^TD"});
            var thead = document.createElement("THEAD");
            thead.appendChild(beginBlankRow);
            container.firstChild.appendChild(thead);
        }
        if (!endBlankRow) {
            this._endBlankRow = endBlankRow = $DomUtils.xCreateElement({tagName:"TR", className:"preparing-area", content:"^TD"});
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
            itemDomCount = this.refreshItemDoms(tbody, itemModel.isReverse(), function (row) {
                viewPortHeight += row.offsetHeight;
                if (dorado.Browser.safari) {
                    viewPortHeight -= 2;
                }
                return viewPortHeight <= (clientHeight + 0);
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
                    firstChild.style.height = Math.round((itemCount - itemDomCount - startIndex) * rowHeightAverage) + "px";
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
    }, _watchScroll:function () {
        delete this._watchScrollTimerId;
        if (this._scrollMode == "simple") {
            return;
        }
        var container = this._container;
        if (container.scrollLeft == 0 && container.scrollTop == 0 && container.offsetWidth > 0) {
            container.scrollLeft = this._scrollLeft || 0;
            container.scrollTop = this._scrollTop || 0;
        }
        if (this._scrollTop) {
            this._watchScrollTimerId = $setTimeout(this, this._watchScroll, 300);
        }
    }, onScroll:function () {
        if (this._scrollMode == "simple") {
            return;
        }
        var container = this._container;
        if ((this._scrollLeft || 0) != container.scrollLeft) {
            if (this.onXScroll) {
                this.onXScroll();
            }
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
    }, onYScroll:function () {
        var container = this._container;
        if (container.scrollTop == (container._scrollTop || 0)) {
            return;
        }
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
            dorado.Toolkits.setDelayedAction(container, "$scrollTimerId", function () {
                self.doOnYScroll();
            }, 300);
        } else {
            container._scrollTop = container.scrollTop;
            this.doOnYScroll();
        }
    }, doOnYScroll:function () {
        var container = this._container;
        if (this._scrollMode == "viewport") {
            dorado.Toolkits.cancelDelayedAction(container, "$scrollTimerId");
            this._itemModel.setScrollPos(container.scrollTop);
            this.setHoverRow(null);
            this.refresh();
            this.hideScrollingIndicator();
        } else {
            if (this._scrollMode == "lazyRender") {
                var rows = this._dataTBody.rows;
                var i = parseInt(container.scrollTop / this._rowHeight) || 0;
                if (i >= rows.length) {
                    i = rows.length - 1;
                }
                var row = rows[i];
                if (!row) {
                    return;
                }
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
        }
    }, createDom:function () {
        var dom = $invokeSuper.call(this, arguments);
        if (dorado.Browser.msie && dorado.Browser.version >= "8") {
            dom.hideFocus = true;
        }
        $fly(dom).bind("scroll", $scopify(this, this.onScroll));
        return dom;
    }, refreshDom:function (dom) {
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
        if (this._currentRow) {
            this.scrollItemDomIntoView(this._currentRow);
        }
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
    }, notifySizeChange:function () {
        if (!this._dom.style.height || !this._dom.style.width) {
            $invokeSuper.call(this, arguments);
        }
    }, scrollItemDomIntoView:function (row) {
        with (this._container) {
            var st = -1;
            if ((row.offsetTop + row.offsetHeight) > (scrollTop + clientHeight)) {
                st = row.offsetTop + row.offsetHeight - clientHeight;
            } else {
                if (row.offsetTop < scrollTop) {
                    st = row.offsetTop;
                }
            }
            if (st >= 0) {
                if (this._scrollMode != "lazyRender") {
                    this._scrollTop = st;
                }
                scrollTop = st;
            }
        }
    }, scrollCurrentIntoView:function () {
        var currentItemId = this.getCurrentItemId();
        if (currentItemId != null) {
            var row = this._currentRow;
            if (row) {
                this.scrollItemDomIntoView(row);
            } else {
                if (this._scrollMode == "viewport") {
                    var itemModel = this._itemModel;
                    var index = this.getIndexByItemId(currentItemId);
                    if (index < 0) {
                        index = 0;
                    }
                    itemModel.setStartIndex(index);
                    var oldReverse = itemModel.isReverse();
                    itemModel.setReverse(index >= this.startIndex);
                    this.refresh();
                    itemModel.setReverse(oldReverse);
                }
            }
        }
    }, findItemDomByPosition:function (pos) {
        var dom = this._dom, y = pos.y + dom.scrollTop, row = null;
        var rows = this._dataTBody.rows, rowHeight = this._rowHeightAverage || this._rowHeight, i;
        if (this._scrollMode == "viewport") {
            var relativeY = y;
            if (this._beginBlankRow) {
                relativeY -= this._beginBlankRow.offsetHeight;
            }
            i = parseInt(relativeY / rowHeight);
        } else {
            i = parseInt(y / rowHeight);
        }
        if (i < 0) {
            i = 0;
        } else {
            if (i >= rows.length) {
                i = rows.length - 1;
            }
        }
        row = rows[i];
        while (row) {
            if (row.offsetTop > y) {
                row = row.previousSibling;
            } else {
                if (row.offsetTop + row.offsetHeight < y) {
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
        }
        return row;
    }, setDraggingOverItemDom:function (itemDom) {
        if (this._draggingOverItemDom == itemDom) {
            return;
        }
        if (this._draggingOverItemDom) {
            $fly(this._draggingOverItemDom).removeClass("drag-over-row");
        }
        this._draggingOverItemDom = itemDom;
        if (itemDom) {
            $fly(itemDom).addClass("drag-over-row");
        }
    }, showDraggingInsertIndicator:function (draggingInfo, insertMode, itemDom) {
        var insertIndicator = dorado.widget.RowList.getDraggingInsertIndicator();
        var $insertIndicator = $fly(insertIndicator);
        if (insertMode) {
            var dom = this._dom;
            var width = dom.offsetWidth;
            var top = (insertMode == "before") ? itemDom.offsetTop : (itemDom.offsetTop + itemDom.offsetHeight);
            if (dom.clientWidth < width) {
                width = dom.clientWidth;
            }
            $insertIndicator.width(width).height(2).left(0).top(top - 1).show().appendTo(dom);
        } else {
            $insertIndicator.hide();
        }
    }, doOnDraggingSourceMove:function (draggingInfo, evt, targetObject, insertMode, refObject, itemDom) {
        var accept = (draggingInfo.isDropAcceptable(this._droppableTags) && !(this._dropMode == "onItem" && targetObject == null));
        draggingInfo.set({targetObject:targetObject, insertMode:insertMode, refObject:refObject, accept:accept});
        var eventArg = {draggingInfo:draggingInfo, event:evt, processDefault:true};
        this.fireEvent("onDraggingSourceMove", this, eventArg);
        if (accept && eventArg.processDefault) {
            this.showDraggingInsertIndicator(draggingInfo, insertMode, itemDom);
            this.setDraggingOverItemDom((accept && !insertMode) ? itemDom : null);
        }
        return eventArg.processDefault;
    }, onDraggingSourceMove:function (draggingInfo, evt) {
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
                } else {
                    if (dropMode == "onOrInsertItems") {
                        var dropY = itemDom._dropY;
                        if (dropY <= 4) {
                            insertMode = "before";
                        } else {
                            if ((itemDom.offsetHeight - dropY) <= 4) {
                                insertMode = "after";
                            }
                        }
                    }
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
    }, onDraggingSourceOut:function (draggingInfo, evt) {
        $invokeSuper.call(this, arguments);
        this.setDraggingOverItemDom();
        this.showDraggingInsertIndicator();
    }, processItemDrop:function (draggingInfo, evt) {
        function getItemList(control, entity) {
            var list;
            if (entity instanceof dorado.Entity) {
                list = entity.parent;
            } else {
                if (control.ATTRIBUTES.itemModel) {
                    var itemModel = control.get("itemModel");
                    if (itemModel) {
                        list = itemModel.getItems();
                    }
                }
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
                sourceList.remove(object);
                if (!dorado.Object.isInstanceOf(sourceControl, dorado.widget.DataControl)) {
                    sourceControl.refresh();
                }
            }
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
                        if (insertMode == "after") {
                            i++;
                        }
                        targetList.insert(object, i);
                        highlight = i;
                    }
                }
                if (!dorado.Object.isInstanceOf(this, dorado.widget.DataControl)) {
                    this.refresh();
                }
                if (highlight != null) {
                    this.highlightItem(highlight);
                }
                return true;
            }
        }
        return false;
    }, onDraggingSourceDrop:function (draggingInfo, evt) {
        var dropped = $invokeSuper.call(this, arguments);
        if (!dropped) {
            dropped = this.processItemDrop(draggingInfo, evt);
        }
        return dropped;
    }});
    dorado.widget.RowList.getDraggingInsertIndicator = function () {
        var indicator = this._draggingInsertIndicator;
        if (indicator == null) {
            indicator = $DomUtils.xCreateElement({tagName:"div", className:"d-list-dragging-insert-indicator"});
            this._draggingInsertIndicator = indicator;
        }
        return indicator;
    };
})();
dorado.widget.list.ListBoxRowRenderer = $extend(dorado.Renderer, {$className:"dorado.ListBoxRowRenderer", render:function (dom, arg) {
    var item = arg.data, text = "";
    if (item) {
        if (arg.property) {
            if (item instanceof dorado.Entity) {
                text = item.getText(arg.property);
            } else {
                text = item[arg.property];
            }
        } else {
            text = item;
        }
    }
    dom.innerText = text;
}});
dorado.widget.AbstractListBox = $extend(dorado.widget.RowList, {$className:"dorado.widget.AbstractListBox", ATTRIBUTES:{className:{defaultValue:"d-list-box"}, width:{defaultValue:200}, property:{}, renderer:{}}, EVENTS:{onRenderRow:{}}, doRefreshItemDomData:function (row, item) {
    var processDefault = true, arg = {dom:row.firstChild, data:item, property:this._property, processDefault:false};
    if (this.getListenerCount("onRenderRow")) {
        this.fireEvent("onRenderRow", this, arg);
        processDefault = arg.processDefault;
    }
    if (processDefault) {
        (this._renderer || $singleton(dorado.widget.list.ListBoxRowRenderer)).render(row.firstChild, arg);
    }
}, createItemDom:function (item) {
    var row = document.createElement("TR");
    row.className = "row";
    row.style.height = this._rowHeight + "px";
    if (this._scrollMode == "lazyRender" && this._shouldSkipRender) {
        row._lazyRender = true;
        row.style.height = this._rowHeight + "px";
    } else {
        this.createItemDomDetail(row, item);
    }
    return row;
}, createItemDomDetail:function (dom, item) {
    var cell = document.createElement("TD");
    dom.appendChild(cell);
}, getItemByEvent:function (event) {
    var row = this.findItemDomByEvent(event);
    return (row) ? $fly(row).data("item") : null;
}});
dorado.widget.ListBox = $extend(dorado.widget.AbstractListBox, {$className:"dorado.widget.ListBox", ATTRIBUTES:{currentIndex:{skipRefresh:true, defaultValue:-1, setter:function (p, index) {
    if (index >= this._itemModel.getItemCount()) {
        index = -1;
    }
    if (this._currentIndex == index) {
        return;
    }
    this._currentIndex = index;
    var row = this.getItemDomByItemIndex(index);
    this.setCurrentRow(row);
    if (row) {
        this.scrollCurrentIntoView();
    }
    this.fireEvent("onCurrentChange", this);
}}, currentItem:{readOnly:true, getter:function () {
    return this.getCurrentItem();
}}, items:{setter:function (p, v) {
    this._currentIndex = -1;
    this._itemModel.setItems(v);
    this.set("selection", null);
}, getter:function () {
    return this._itemModel.getItems();
}}}, refreshDom:function (dom) {
    this.set("currentIndex", this._currentIndex);
    $invokeSuper.call(this, arguments);
}, getItemDomByItemIndex:function (index) {
    var itemModel = this._itemModel, row;
    if (index >= itemModel.getItemCount()) {
        index = -1;
    }
    var item = index >= 0 ? itemModel.getItemAt(index) : null;
    if (this._rendered && this._itemDomMap && index >= 0) {
        if (this._rowCache && $fly(this._rowCache).data("item") == item) {
            row = this._rowCache;
            delete this._rowCache;
        } else {
            row = this._itemDomMap[itemModel.getItemId(item)];
        }
    }
    return row;
}, getCurrentItem:function () {
    if (this._currentIndex >= 0) {
        return this._itemModel.getItemAt(this._currentIndex);
    }
}, getCurrentItemId:function () {
    return this._currentIndex;
}, doOnKeyDown:function (evt) {
    var retValue = true;
    switch (evt.keyCode || evt.which) {
      case 36:
        this.set("currentIndex", 0);
        break;
      case 35:
        this.set("currentIndex", this._itemModel.getItemCount() - 1);
        break;
      case 38:
        if (this._currentIndex > 0) {
            this.set("currentIndex", this._currentIndex - 1);
        }
        retValue = false;
        break;
      case 40:
        if (this._currentIndex < this._itemModel.getItemCount() - 1) {
            this.set("currentIndex", this._currentIndex + 1);
        }
        retValue = false;
        break;
    }
    return retValue;
}, setCurrentItemDom:function (row) {
    this._rowCache = row;
    this.set("currentIndex", row ? this._itemModel.getItemIndex($fly(row).data("item")) : -1);
    return true;
}, highlightItem:function (index, options, speed) {
    var row = this.getItemDomByItemIndex(index);
    if (row) {
        $fly(row).addClass("highlighting-row").effect("highlight", options || {color:"#FF8000"}, speed || 1500, function () {
            $fly(row).removeClass("highlighting-row");
        });
    }
}});
dorado.widget.DataListBox = $extend([dorado.widget.AbstractListBox, dorado.widget.DataControl], {$className:"dorado.widget.DataListBox", getCurrentItem:function () {
    if (this._duringRefreshAll) {
        return this._itemModel.getItems().current;
    } else {
        return (this._currentRow) ? $fly(this._currentRow).data("item") : null;
    }
}, getCurrentItemId:function (item, index) {
    var current = this.getCurrentItem();
    return current ? current.entityId : null;
}, setCurrentItemDom:function (row) {
    var item = (row ? $fly(row).data("item") : null);
    if (item && this.setCurrentEntity(item)) {
        this._itemModel.getItems().setCurrent(item);
        return true;
    }
    return false;
}, refreshEntity:function (entity) {
    var row = this._itemDomMap[entity.entityId];
    if (row) {
        this.refreshItemDomData(row, entity);
    }
}, refreshDom:function (dom) {
    var entityList = this.getBindingData({firstResultOnly:true, acceptAggregation:true});
    if (entityList && !(entityList instanceof dorado.EntityList)) {
        throw new dorado.ResourceException(dorado.list.BindingTypeMismatch);
    }
    var oldItems = this._itemModel.getItems();
    if (oldItems != entityList) {
        this._itemModel.setItems(entityList);
        this.set("selection", null);
    }
    $invokeSuper.call(this, arguments);
}, setCurrentEntity:function (entity) {
    var itemId = entity ? entity.entityId : null;
    var row = this._itemDomMap[itemId];
    this.setCurrentRow(row);
    this.scrollCurrentIntoView();
    this.fireEvent("onCurrentChange", this);
    return true;
}, doOnKeyDown:function (evt) {
    var retValue = true;
    var items = this._itemModel.getItems();
    switch (evt.keyCode) {
      case 36:
        items.first();
        break;
      case 35:
        items.last();
        break;
      case 38:
        items.previous();
        retValue = false;
        break;
      case 40:
        items.next();
        retValue = false;
        break;
    }
    return retValue;
}, _adjustBeginBlankRow:function () {
    this._ignoreOnScroll++;
    var itemModel = this._itemModel;
    var container = this.getDom();
    var beginBlankRow = this._beginBlankRow;
    var adj = container.scrollTop - beginBlankRow.offsetHeight;
    beginBlankRow.firstChild.style.height = this.startIndex * this._rowHeightAverage + "px";
    container.scrollTop = beginBlankRow.offsetHeight + adj;
    itemModel.setScrollSize(container.clientHeight, container.scrollHeight);
    $setTimeout(this, function () {
        this._ignoreOnScroll--;
    }, 0);
}, _adjustEndBlankRow:function () {
    var itemModel = this._itemModel;
    var container = this.getDom();
    var endBlankRow = this._endBlankRow;
    endBlankRow.firstChild.style.height = (itemModel.getItemCount() - this.startIndex - this.itemDomCount) * this._rowHeightAverage + "px";
    itemModel.setScrollSize(container.clientHeight, container.scrollHeight);
}, onEntityDeleted:function (arg) {
    var entity = arg.entity;
    var row = this._itemDomMap[entity.entityId], tbody = this._dataTBody;
    if (this._scrollMode != "viewport") {
        if (row) {
            var nextRow = row.nextSibling;
            this.removeItemDom(row);
            if (this._forceRefreshRearRows) {
                this._ignoreItemTimestamp = true;
                while (nextRow) {
                    this.refreshItemDom(tbody, $fly(nextRow).data("item"), nextRow.sectionRowIndex);
                    nextRow = nextRow.nextSibling;
                }
            }
            this.notifySizeChange();
        }
    } else {
        var itemModel = this._itemModel;
        if (this._forceRefreshRearRows) {
            if (this._forceRefreshRearRows) {
                this._ignoreItemTimestamp = true;
            }
            this.refresh(true);
        } else {
            if (row) {
                if (row == tbody.firstChild) {
                    itemModel.setStartIndex(itemModel.getStartIndex() - 1);
                    if (itemModel.getStartIndex() < 0) {
                        itemModel.setStartIndex(0);
                    }
                } else {
                    this.removeItemDom(row);
                }
                this.refresh(true);
            } else {
                var i = itemModel.getItemIndex(entity);
                if (i >= 0) {
                    if (i < itemModel.getStartIndex()) {
                        this.startIndex--;
                        itemModel.setStartIndex(itemModel.getStartIndex() - 1);
                        this._adjustBeginBlankRow();
                    } else {
                        this._adjustEndBlankRow();
                    }
                }
            }
        }
    }
}, onEntityInserted:function (arg) {
    var entity = arg.entity;
    var mode = arg.insertMode;
    var refEntity = arg.refEntity;
    var tbody = this._dataTBody;
    if (this._scrollMode != "viewport") {
        var row = this.createItemDom(entity);
        switch (mode) {
          case "begin":
            tbody.insertBefore(row, tbody.firstChild);
            break;
          case "before":
            var refRow = this._itemDomMap[refEntity.entityId];
            tbody.insertBefore(row, refRow);
            break;
          case "after":
            var refRow = this._itemDomMap[refEntity.entityId];
            if (refRow.nextSibling) {
                tbody.insertBefore(row, refRow.nextSibling);
                break;
            }
          default:
            tbody.appendChild(row);
        }
        this.refreshItemDom(tbody, entity, row.sectionRowIndex);
        if (this._forceRefreshRearRows) {
            this._ignoreItemTimestamp = true;
            row = row.nextSibling;
            while (row) {
                this.refreshItemDom(tbody, $fly(row).data("item"), row.sectionRowIndex);
                row = row.nextSibling;
            }
        }
        this.notifySizeChange();
    } else {
        var itemModel = this._itemModel;
        var i = itemModel.getItemIndex(entity);
        if (i >= 0) {
            if (this._forceRefreshRearRows) {
                if (this._forceRefreshRearRows) {
                    this._ignoreItemTimestamp = true;
                }
                this.refresh(true);
            } else {
                if (i < this.startIndex) {
                    this.startIndex++;
                    itemModel.setStartIndex(itemModel.getStartIndex() + 1);
                    this._adjustBeginBlankRow();
                } else {
                    if (i >= this.startIndex) {
                        if (i < this.startIndex + this.itemDomCount) {
                            var row = tbody.lastChild;
                            var nextRow = tbody.childNodes[i - this.startIndex];
                            tbody.insertBefore(row, nextRow);
                            this.refresh(true);
                        } else {
                            this._adjustEndBlankRow();
                        }
                    }
                }
            }
        }
    }
}, filterDataSetMessage:function (messageCode, arg) {
    var itemModel = this._itemModel;
    var items = itemModel.getItems();
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
        return true;
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
        return (arg.entityList == items || dorado.DataUtil.isOwnerOf(items, arg.entityList));
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
        return (!items || items._observer != this._dataSet || arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
      case dorado.widget.DataSet.MESSAGE_DELETED:
        return (arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
      case dorado.widget.DataSet.MESSAGE_INSERTED:
        return (arg.entityList == items);
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        return (arg.entity.parent == items);
      default:
        return false;
    }
}, processDataSetMessage:function (messageCode, arg, data) {
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
        this.refresh(true);
        break;
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
        if (arg.entityList == this._itemModel.getItems()) {
            var oldCurrentEntity = this.getCurrentItem();
            if (!oldCurrentEntity || oldCurrentEntity.page.pageNo != arg.entityList.pageNo) {
                this.refresh(true);
            } else {
                this.setCurrentEntity(arg.entityList.current);
            }
        } else {
            this.refresh(true);
        }
        break;
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
        var items = this._itemModel.getItems();
        if (!items || items._observer != this._dataSet) {
            this.refresh(true);
        } else {
            this.refreshEntity(arg.entity);
        }
        break;
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        this.refreshEntity(arg.entity);
        break;
      case dorado.widget.DataSet.MESSAGE_DELETED:
        this.onEntityDeleted(arg);
        break;
      case dorado.widget.DataSet.MESSAGE_INSERTED:
        this.onEntityInserted(arg);
        break;
    }
}, highlightItem:function (entity, options, speed) {
    var row = this._itemDomMap[entity.entityId];
    if (row) {
        $fly(row).addClass("highlighting-row").effect("highlight", options || {color:"#FF8000"}, speed || 1500, function () {
            $fly(row).removeClass("highlighting-row");
        });
    }
}});

