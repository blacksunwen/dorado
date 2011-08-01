(function() {

	valueComparators = {};
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 列表数据模型。 
	 * <p>列表数据模型是用于辅助列表控件管理列表数据的对象。</p>
	 */
	dorado.widget.list.ItemModel = $class(/** @scope dorado.widget.list.ItemModel.prototype */{
		$className: "dorado.widget.list.ItemModel",
		
		EMPTY_ITERATOR: new dorado.util.ArrayIterator([]),
		
		_itemDomSize: 0,
		_viewPortSize: 0,
		_scrollSize: 0,
		_scrollPos: 0,
		_startIndex: 0,
		_reverse: false,
		
		/**
		 * 列表控件中每一个列表元素显示时的大小。
		 * @param {int} itemDomSize 列表元素的大小。
		 */
		setItemDomSize: function(itemDomSize) {
			this._itemDomSize = itemDomSize;
		},
		
		/**
		 * 返回迭代数据的起始位置。
		 * @return {int} 起始位置。
		 * @see dorado.widget.list.ItemModel#setStartIndex
		 */
		getStartIndex: function() {
			return this._startIndex;
		},
		
		/**
		 * 列表控件的可视区域中第一个可见列表项的位置序号，即迭代数据的起始位置。此序号是从0开始计算的。
		 * @param {int} startIndex 起始位置。
		 */
		setStartIndex: function(startIndex) {
			this._startIndex = startIndex;
		},
		
		/**
		 * 是否要以逆向对象数据进行迭代。
		 * @return {boolean} 是否逆向。
		 */
		isReverse: function() {
			return this._reverse;
		},
		
		/**
		 * 设置是否要以逆向对象数据进行迭代。
		 * @param {boolean} reverse 是否逆向。
		 */
		setReverse: function(reverse) {
			this._reverse = reverse;
		},
		
		/**
		 * 设置可视区域和滚动区域的大小。
		 * @param {int} viewPortSize 可视区域的大小。
		 * @param {int} scrollSize 滚动区域的大小。
		 */
		setScrollSize: function(viewPortSize, scrollSize) {
			this._viewPortSize = viewPortSize;
			this._scrollSize = scrollSize;
		},
		
		/**
		 * 设置可视区域当前的滚动位置。
		 * @param {int} scrollPos 滚动位置。
		 */
		setScrollPos: function(scrollPos) {
			var itemCount = this.getItemCount();
			if (itemCount > 0) {
				var itemDomSize = this._scrollSize / itemCount;
				if (scrollPos / (this._scrollSize - this._viewPortSize) > 0.5) {
					this._startIndex = itemCount - 1 - (Math.round((this._scrollSize - this._viewPortSize - scrollPos) / itemDomSize) || 0);
					if (this._startIndex > itemCount - 1) this._startIndex = itemCount - 1;
					this._reverse = true;
				} else {
					this._startIndex = Math.round(scrollPos / itemDomSize) || 0;
					this._reverse = false;
				}
			} else {
				this._startIndex = 0;
				this._reverse = false;
			}
		},
		
		/**
		 * 返回列表中要显示的数据。
		 * @return {Object[]|dorado.EntityList} data 数据。
		 */
		getItems: function() {
			return this._items;
		},
		
		/**
		 * 设置列表中要显示的数据。
		 * @param {Object[]|dorado.EntityList} items 数据。
		 */
		setItems: function(items) {
			this.filter();
			if (items instanceof dorado.EntityList) this._items = items;
			else if (items != null) this._items = (items instanceof Array) ? items : [items];
			else this._items = [];
		},
		
		/**
		 * 返回一个从startIndex位置开始的数据迭代器。
		 * @param {int} [startIndex] 起始的迭代位置。
		 * 如果不定义此参数将从ItemModel的startIndex属性指定的位置开始迭代。
		 * @return {dorado.util.Iterator} 数据迭代器。
		 * @see dorado.widget.list.ItemModel#startIndex
		 */
		iterator: function(startIndex) {
			var items = this._items;
			if (!items) return this.EMPTY_ITERATOR;
			var index = startIndex;
			if (index == null) index = this._startIndex || 0;
			if (items instanceof Array) {
				return new dorado.util.ArrayIterator(this._items, index);
			} else {
				return items.iterator({
					currentPage: true,
					nextIndex: index
				});
			}
		},
		
		/**
		 * 返回总的列表数据实体的个数。
		 * @return {int} 数据实体的个数。
		 */
		getItemCount: function() {
			var items = this._items;
			if (!items) return 0;
			return (items instanceof Array) ? items.length : ((items.pageSize > 0) ? items.getPageEntityCount() : items.entityCount);
		},
		
		/**
		 * 根据传入的序号返回相应的列表数据实体，此序号是从0开始计算的。
		 * @param {int} index 列表数据实体的序号。
		 * @return {Object|dorado.Entity} 数据实体。
		 */
		getItemAt: function(index) {
			if (!this._items || !(index >= 0)) return null;
			if (this._items instanceof Array) {
				return this._items[index];
			} else {
				var entityList = this._items;
				return entityList.iterator({
					nextIndex: index
				}).next();
			}
		},
		
		/**
		 * 根据传入的Id返回相应的列表数据实体。
		 * @param {String|int} itemId 数据实体的Id。
		 * @return {Object|dorado.Entity} 数据实体。
		 */
		getItemById: function(itemId) {
			if (this._items instanceof Array) {
				return this._items.indexOf(itemId);
			} else {
				return this._items.getById(itemId);
			}
		},
		
		/**
		 * 根据传入的列表数据实体返回其在整个列表中的位置序号，此序号是从0开始计算的。
		 * @param {Object|dorado.Entity} item 数据实体。
		 * @return {int} index 数据实体的序号。
		 */
		getItemIndex: function(item) {
			if (!this._items || !item) return -1;
			if (this._items instanceof Array) {
				return this._items.indexOf(item);
			} else {
				var entityList = this._items, itemId = item.entityId, page = item.page;
				if (page.entityList != entityList) return -1;
				
				var index = 0, entry = page.first;
				while (entry != null) {
					if (entry.data.entityId == itemId) return index;
					if (entry.data.state != dorado.Entity.STATE_DELETED) index++;
					entry = entry.next;
				}
				return -1;
			}
		},
		
		getItemId: function(item, index) {
			if (this._items instanceof Array || !(item instanceof dorado.Entity)) {
				return (index >= 0) ? index : this.getItemIndex(item);
			} else {
				return item.entityId;
			}
		},
		
		/**
		 * 对所有数据实体进行排序。详见{@link dorado.DataUtil.sort}。
		 * @param {Object|Object[]} sortParams 排序参数或排序参数的数组。
		 * @param {String} sortParams.property 要排序的属性名。
		 * @param {boolean} sortParams.desc 是否逆向排序。
		 * @param {Function} comparator 比较器。
		 * @see dorado.DataUtil.sort
		 */
		sort: function(sortParams, comparator) {
			if (!this.getItemCount()) return;
			
			var items = this.toArray();
			dorado.DataUtil.sort(items, sortParams, comparator);
			if (!(this._items instanceof Array)) this._items = items;
		},
		
		/**
		 * 对所有数据实体进行过滤。
		 * @param {Object[]} filterParams 过滤条件的数组。
		 * @param {String} filterParams.property 要过滤的属性名。
		 * @param {String} sortParams.operator 比较操作符。如"="、"link"、">"、"<="等。
		 * @param {Object} sortParams.value 过滤条件值。
		 */
		filter: function(filterParams) {
		
			function getValueComparator(op) {
				var comparator = valueComparators[escape(op)];
				if (!comparator) {
					valueComparators[escape(op)] = comparator = new Function("v1,v2", "return v1" +
					((op == '=') ? "==" : op) +
					"v2");
				}
				return comparator;
			}
			
			function filterEntity(entity, filterParam) {
				var value = (entity instanceof dorado.Entity) ? entity.get(filterParam.property) : entity[filterParam.property];
				var op = filterParam.operator;
				if (!op) {
					op = (typeof value == "string") ? "like" : '=';
				}
				if (op == "like") {
					return (value + '').indexOf(filterParam.value) >= 0;
				} else {
					return getValueComparator(op)(value, filterParam.value);
				}
			}
			
			if (filterParams && filterParams.length > 0) {
				if (this._originItems) this._items = this._originItems;
				else this._originItems = this._items;
				var filtered = [];
				for (var it = this.iterator(0); it.hasNext();) {
					var entity = it.next(), passed = true;
					for (var i = 0; i < filterParams.length; i++) {
						if (!filterEntity(entity, filterParams[i])) {
							passed = false;
							break;
						}
					}
					if (passed) filtered.push(entity);
				}
				this._items = filtered;
			} else if (this._originItems) {
				this._items = this._originItems;
				delete this._originItems;
			}
		},
		
		/**
		 * 将列表数据模型中的数据导出到一个数组中。
		 * @return {Object[]} 数据实体数组。
		 */
		toArray: function() {
			if (this._items instanceof Array) {
				return this._items;
			} else if (this._items instanceof dorado.EntityList) {
				return this._items.toArray();
			} else {
				var v = [];
				for (var it = this.iterator(0); it.hasNext();) 
					v.push(it.next());
				return v;
			}
		}
	});
	
})();
