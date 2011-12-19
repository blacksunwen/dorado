(function() {

	var ItemModel = $extend(dorado.widget.grid.ItemModel, {
		resetFilterEntityOnSetItem: false,
		
		getItemCount: function() {
			var items = this._items;
			if (!items) return 0;
			if (this.groups || this._items instanceof Array) {
				return $invokeSuper.call(this, arguments);
			} else {
				if (!(items.pageSize > 0)) {
					return items.entityCount;
				} else if (this.grid._supportsPaging || items.entityCount < items.pageSize) {
					return items.entityCount;
				} else {
					return items.pageSize;
				}
			}
		},
		
		iterator: function(nextIndex) {
			if (!this._items) return this.EMPTY_ITERATOR;
			if (this.groups || this._items instanceof Array) {
				return $invokeSuper.call(this, arguments);
			} else {
				return this._items.iterator({
					simulateUnloadPage: this.grid._supportsPaging,
					currentPage: !this.grid._supportsPaging,
					nextIndex: this._startIndex || nextIndex
				});
			}
		},
		
		getItemAt: function(index) {
			if (!this._items || !(index >= 0)) return null;
			if (this.groups || this._items instanceof Array) {
				return $invokeSuper.call(this, arguments);
			} else {
				return this._items.iterator({
					simulateUnloadPage: this.grid._supportsPaging,
					currentPage: !this.grid._supportsPaging,
					nextIndex: index
				}).next();
			}
		},
		
		getItemIndex: function(item) {
			if (!item || item.dummy) return -1;
			if (this.groups || this._items instanceof Array) {
				return $invokeSuper.call(this, arguments);
			} else {
				var entityList = this._items, itemId, page;
				if (item instanceof dorado.Entity) {
					itemId = item.entityId;
					page = item.page;
				} else {
					itemId = item;
					page = entityList.getById(itemId).page;
				}
				if (page.entityList != entityList) return -1;
				
				var index = 0, entry = page.first, found = false;
				while (entry != null) {
					if (entry.data.entityId == itemId) {
						found = true;
						break;
					}
					if (entry.data.state != dorado.Entity.STATE_DELETED) index++;
					entry = entry.next;
				}
				
				if (found) {
					if (this.grid._supportsPaging) {
						for (var i = page.pageNo - 1; i > 0; i--) {
							index += entityList.getPage(i, false).entityCount;
						}
					}
					return index;
				} else {
					return -1;
				}
			}
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Collection
	 * @class 数据表格控件。
	 * @extends dorado.widget.AbstractGrid
	 * @extends dorado.widget.DataControl
	 */
	dorado.widget.DataGrid = $extend([dorado.widget.AbstractGrid, dorado.widget.DataControl], /** @scope dorado.widget.DataGrid.prototype */ {
		$className: "dorado.widget.DataGrid",
		
		ATTRIBUTES: /** @scope dorado.widget.DataGrid.prototype */ {
		
			/**
			 * 是否自动根据绑定的EntityDataType自动创建其中的表格列。
			 * @type boolean
			 * @default true
			 * @attribute
			 */
			autoCreateColumns: {
				defaultValue: true
			},
			
			/**
			 * 是否支持分页显示及动态的分页数据下载。
			 * @type boolean
			 * @attribute
			 */
			supportsPaging: {},
			
			/**
			 * 当用户在最后一个单元格中按下回车键时是否要自动追加一条新的记录。
			 * @type boolean
			 * @attribute
			 */
			appendOnLastEnter: {},
			
			/**
			 * 数据过滤模式。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>clientSide - 在客户端执行过滤。</li>
			 * <li>serverSide - 在服务端执行过滤。<br>
			 * 在此种模式下，当DataGrid需要过滤其中的数据时会将过滤条件设置到绑定的DataSet的parameter属性中。<br>
			 * 用户输入的过滤条件以数组的形式放入parameter的criteria属性的criterions子属性中，
			 * 数组中的每一个元素是一个JSON对象，该对象中包含下列属性：
			 * 	<ul>
			 * 		<li>property	-	{String} 属性名。</li>
			 * 		<li>expression	-	{String} 用户输入的过滤表达式，例如：">6000"。</li>
			 * 	</ul>
			 * </li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute
			 * @default clientSide
			 */
			filterMode: {
				defaultValue: "clientSide"
			},
			
			/**
			 * 数据排序模式。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>clientSide - 在客户端执行排序。</li>
			 * <li>serverSide - 在服务端执行排序。<br>
			 * 在此种模式下，当DataGrid需要过滤其中的数据时会将排序条件设置到绑定的DataSet的parameter属性中。<br>
			 * 用户输入的过滤条件以数组的形式放入parameter的criteria属性的orders子属性中，
			 * 数组中的每一个元素是一个JSON对象，该对象中包含下列属性：
			 * 	<ul>
			 * 		<li>property	-	{String} 属性名。</li>
			 * 		<li>desc	-	{boolean} 是否反向排序。</li>
			 * 	</ul>
			 * </li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute
			 * @default clientSide
			 * @dorado.widget.View#attribute:context
			 */
			sortMode: {
				defaultValue: "clientSide"
			},
			
			selection: {
				setter: function(selection) {
					this.refresh();
					$invokeSuper.call(this, arguments);
				}
			}
		},
		
		createItemModel: function() {
			return new ItemModel(this);
		},
		
		createInnerGrid: function(fixed) {
			return new dorado.widget.grid.InnerDataGrid(this, fixed);
		},
		
		/**
		 * 设置某记录对应的数据实体对应的当前行。
		 * @param {dorado.Entity} entity 数据实体。
		 */
		setCurrentEntity: function(entity) {
			this._innerGrid.setCurrentEntity(entity);
			if (this._domMode == 2) this._fixedInnerGrid.setCurrentEntity(entity);
		},
		
		getCurrentEntity: function() {
			return this._innerGrid.getCurrentItem();
		},
		
		addColumn: function() {
			var column = $invokeSuper.call(this, arguments);
			if (this._autoCreateColumns && column instanceof dorado.widget.grid.DataColumn && column._property && column._property != "none") {
				var watcher = this.getAttributeWatcher();
				if (watcher.getWritingTimes("autoCreateColumns") == 0) {
					this._autoCreateColumns = false;
				}
			}
			return column;
		},
		
		initColumns: function(dataType) {
		
			function doInitColumns(cols, dataType) {
				for (var i = 0; i < cols.length; i++) {
					var col = cols[i];
					if (col instanceof dorado.widget.grid.ColumnGroup) {
						doInitColumns(col._columns.items, dataType);
					} else {
						col._propertyDef = (col._property) ? dataType.getPropertyDef(col._property) : null;
					}
				}
			}
			
			if (dataType && (this._dataType != dataType || !this._columnInited)) {
				this._columnInited = true;
				this._dataType = dataType;
				if (dataType) {
					/*generate default columns*/
					var columns = this._columns;
					if (this._autoCreateColumns && !this._defaultColumnsGenerated) {
						this._defaultColumnsGenerated = true;
						
						var self = this, columnsClear = false;
						dataType._propertyDefs.each(function(pd) {
							if (!pd._visible) return;
							
							var column = columns.get(pd._name), columnConfig = {};
							if (column) {
								columns.remove(column);
								columns.append(column);
							}
							
							var t = pd.getDataType(true);
							if (t && (!t._code || !(t instanceof dorado.DataType))) return;
							columnConfig.name = columnConfig.property = pd._name;
							
							if (column) {
								column.set(columnConfig, {
									tryNextOnError: true,
									preventOverwriting: true
								});
							} else {
								if (!columnsClear && columns.size == 1 && columns.get(0)._name == "empty") {
									columns.clear();
									columnsClear = true;
								}
								
								self.addColumn(new dorado.widget.grid.DataColumn(columnConfig));
							}
						});
					}
					doInitColumns(columns.items, dataType);
				}
			}
		},
		
		refreshDom: function(dom) {
			var columnsInited = false;
			if (this._dataSet) {
				var entityList = this.getBindingData({
					firstResultOnly: true,
					acceptAggregation: true
				});
				if (entityList && !(entityList instanceof dorado.EntityList)) {
					throw new dorado.ResourceException("dorado.grid.BindingTypeMismatch", this._id);
				}
				
				var dataType;
				if (entityList && entityList.dataType) dataType = entityList.dataType.getElementDataType("auto");
				if (!dataType) dataType = this.getBindingDataType("auto");
				if (dataType) {
					this.initColumns(dataType);
					columnsInited = true;
				} else if (this._autoCreateColumns && !this._listeningDataTypeRepository) {
					this._columnInited = false;
					this._listeningDataTypeRepository = true;
					var grid = this;
					this.get("dataTypeRepository").addListener("onDataTypeRegister", function(self, arg) {
						var dataType = grid.getBindingDataType("never");
						if (dataType) {
							self.removeListener("onDataTypeRegister", arguments.callee);
							grid._autoCreateColumns = true;
							grid._listeningDataTypeRepository = false;
							grid.initColumns(dataType);
							grid.refresh(true);
						}
					});
				}
				
				var oldItems = this._itemModel.getItems();
				if (oldItems != entityList) {
					this._itemModel.setItems(entityList);
					this.set("selection", null);
				}
			}
			
			if (!columnsInited) this.initColumns();
			$invokeSuper.call(this, arguments);
			
			if (!this._ready && this._dataSet && this._dataSet._loadingData) {
				this.showLoadingTip();
			}
		},
		
		/**
		 * 刷新某数据实体对应的表格行。
		 * @param {dorado.Entity} entity 数据实体
		 */
		refreshEntity: function(entity) {
			this._innerGrid.refreshEntity(entity);
			if (this._domMode == 2) this._fixedInnerGrid.refreshEntity(entity);
		},
		
		onEntityInserted: function(arg) {
			if (this._domMode == 2) this._fixedInnerGrid.onEntityInserted(arg);
			this._innerGrid.onEntityInserted(arg);
			this.updateScroller(this._innerGrid._container);
			this.fixSizeBugs();
		},
		
		onEntityDeleted: function(arg) {
			this._innerGrid.onEntityDeleted(arg);
			if (this._domMode == 2) this._fixedInnerGrid.onEntityDeleted(arg);
			this.updateScroller(this._innerGrid._container);
			this.fixSizeBugs();
		},
		
		shouldEditing: function(column) {
			var readOnly = false;
			if (this._dataSet) {
				readOnly = this._dataSet.get("readOnly");
			}
			return !readOnly && $invokeSuper.call(this, [column]);
		},
		
		_doOnKeyDown: function(evt) {
			var retValue = true;
			var items = this._itemModel.getItems();
			switch (evt.keyCode) {
				case 36:{ /* home */
					if (evt.ctrlKey) {
						items.first();
					} else {
						this.setCurrentColumn(this._columnsInfo.dataColumns[0]);
					}
					break;
				}
				case 35:{ /* end */
					if (evt.ctrlKey) {
						items.last();
					} else {
						var columns = this._columnsInfo.dataColumns;
						this.setCurrentColumn(columns[columns.length - 1]);
					}
					break;
				}
				case 38:{ /* up */
					items.previous();
					retValue = false;
					break;
				}
				case 40:{ /* down */
					items.next();
					retValue = false;
					break;
				}
				case 13:{ /* enter */
					retValue = false;
					var columns = this._columnsInfo.dataColumns, i;
					if (this._currentColumn) {
						i = columns.indexOf(this._currentColumn) || 0;
						if (evt.shiftKey) {
							if (i > 0) i--;
							else if (items.hasPrevious()) {
								items.previous();
								i = columns.length - 1;
							} else retValue = true;
						} else {
							if (i < columns.length - 1) i++;
							else if (items.hasNext()) {
								items.next();
								i = 0;
							} else if (this._appendOnLastEnter && items.current) {
								items.insert();
								i = 0;
							} else retValue = true;
						}
					} else {
						i = evt.shiftKey ? (columns.length - 1) : 0;
					}
					this.setCurrentColumn(columns[i]);
					break;
				}
				case 45:{ /* insert */
					items.insert();
					break;
				}
				case 46:{ /* delete */
					if (evt.ctrlKey) {
						items.remove();
					}
					break;
				}
			}
			return retValue;
		},
		
		filterDataSetMessage: function(messageCode, arg) {
			var itemModel = this._itemModel;
			var items = itemModel.getItems();
			switch (messageCode) {
				case dorado.widget.DataSet.MESSAGE_REFRESH:{
					return true;
				}
				
				case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:{
					return (arg.entityList == items || dorado.DataUtil.isOwnerOf(items, arg.entityList));
				}
				
				case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
				case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:{
					// 此处items._observer != this._dataSet判断的用意是什么？
					return (!items || items._observer != this._dataSet || arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
				}
				
				case dorado.widget.DataSet.MESSAGE_DELETED:{
					return (arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
				}
				
				case dorado.widget.DataSet.MESSAGE_INSERTED:{
					return (arg.entityList == items);
				}
				
				case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:{
					return (arg.entity.parent == items);
				}
				
				case dorado.widget.DataSet.MESSAGE_LOADING_START:
				case dorado.widget.DataSet.MESSAGE_LOADING_END:{
					if (arg.entityList) {
						return (items == arg.entityList || dorado.DataUtil.isOwnerOf(items, arg.entityList));
					} else if (arg.entity) {
						var asyncExecutionTimes = dorado.DataPipe.MONITOR.asyncExecutionTimes;
						this.getBindingData({
							firstResultOnly: true,
							acceptAggregation: true
						});
						return (dorado.DataPipe.MONITOR.asyncExecutionTimes > asyncExecutionTimes);
					} else {
						return true;
					}
				}
				
				default:
					{
						return false;
					}
			}
		},
		
		processDataSetMessage: function(messageCode, arg, data) {
			switch (messageCode) {
				case dorado.widget.DataSet.MESSAGE_REFRESH:{
					if (this._currentCellEditor) this._currentCellEditor.hide();
					if (this._itemModel.groups) this.setDirtyMode(true);
					else this.refresh(true);
					break;
				}
				case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:{
					if (this._currentCellEditor) this._currentCellEditor.hide();
					if (arg.entityList == this._itemModel.getItems()) {
						var oldCurrentEntity = this.getCurrentEntity();
						if (!this._supportsPaging && (!oldCurrentEntity || oldCurrentEntity.page.pageNo != arg.entityList.pageNo)) {
							this.refresh(true);
						} else {
							this.setCurrentEntity(arg.entityList.current);
						}
					} else {
						this.refresh(true);
					}
					break;
				}
				case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:{
					var items = this._itemModel.getItems();
					if (!items || items._observer != this._dataSet || dorado.DataUtil.isOwnerOf(items, arg.entity)) {
						this.refresh(true);
					} else {
						var entity = arg.entity;
						this.refreshEntity(entity);
						if (!entity.rowType) this.onEntityChanged(entity, arg.property);
						this.refreshSummary();
					}
					break;
				}
				case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:{
					var items = this._itemModel.getItems();
					if (!items || items._observer != this._dataSet) {
						this.refresh(true);
					} else {
						if (this._itemModel.groups) this.setDirtyMode(true);
						else this.refreshEntity(arg.entity);
					}
					break;
				}
				case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:{
					this.refreshEntity(arg.entity);
					break;
				}
				case dorado.widget.DataSet.MESSAGE_DELETED:{
					if (this._itemModel.groups) this.setDirtyMode(true);
					else {
						this.onEntityDeleted(arg);
						this.refreshSummary();
					}
					break;
				}
				case dorado.widget.DataSet.MESSAGE_INSERTED:{
					if (this._itemModel.groups) this.setDirtyMode(true);
					else {
						this.onEntityInserted(arg);
						this.refreshSummary();
					}
					break;
				}
				case dorado.widget.DataSet.MESSAGE_LOADING_START:{
					this.showLoadingTip();
					break;
				}
				case dorado.widget.DataSet.MESSAGE_LOADING_END:{
					this.hideLoadingTip();
					break;
				}
			}
		},
		
		_requirePage: function(pageNo, timeout) {
			var requiredPages = this._requiredPages;
			if (!requiredPages) this._requiredPages = requiredPages = [];
			
			var loadingPages = this._loadingPages;
			if (loadingPages && loadingPages.indexOf(pageNo) >= 0) return;
			
			if (this._loadPageTimerId) {
				clearTimeout(this._loadPageTimerId);
				delete this._loadPageTimerId;
			}
			
			if (requiredPages.indexOf(pageNo) < 0) requiredPages.push(pageNo);
			
			this._loadPageTimerId = $setTimeout(this, function() {
				this._loadingPages = requiredPages;
				delete this._requiredPages;
				
				var items = this._itemModel.getItems();
				for (var i = 0; i < requiredPages.length; i++) {
					items.getPage(requiredPages[i], true, dorado._NULL_FUNCTION);
				}
			}, timeout || 20);
		},
		
		filter: function(filterParams) {
			if (this._filterMode == "serverSide") {
				var dataSet = this._dataSet;
				if (!dataSet) return;
				
				if (filterParams === undefined) {
					var filterEntity = this._itemModel.filterEntity, criterions = [];
					var dataColumns = this._columnsInfo.dataColumns;
					for (var i = 0; i < dataColumns.length; i++) {
						var column = dataColumns[i];
						if (!column._property) continue;
						var text = filterEntity.get(column._property);
						if (text != null) criterions.push({
							property: column._property,
							expression: text
						});
					}
				}
				
				var parameter = dataSet.get("parameter"), criteria;
				if (criterions.length) {
					if (parameter != null && !(parameter instanceof dorado.util.Map)) {
						dataSet.set("parameter", null);
					}
					
					if (parameter) criteria = parameter.get("criteria");
					criteria = criteria || {};
					criteria.criterions = criterions;
					dataSet.set("parameter", $map({
						criteria: criteria
					}));
				} else if (parameter instanceof dorado.util.Map) {
					criteria = parameter.get("criteria");
					if (criteria && (!criteria.criterions || !criteria.orders)) {
						parameter.remove("criteria");
					}
				}
				
				dataSet.flushAsync();
			} else {
				return $invokeSuper.call(this, arguments);
			}
		},
		
		sort: function(column, desc) {
			var itemModel = this._itemModel;
			if (this._filterMode == "serverSide") {
				var dataSet = this._dataSet;
				if (!dataSet) return;
				
				var parameter = dataSet.get("parameter"), criteria, orders;
				if (column) {
					if (parameter != null && !(parameter instanceof dorado.util.Map)) {
						dataSet.set("parameter", null);
					}
					
					if (parameter) criteria = parameter.get("criteria");
					criteria = criteria || {};
					criteria.orders = orders = [{
						property: column.get("property"),
						desc: desc
					}];
					dataSet.set("parameter", $map({
						criteria: criteria
					}));
				} else if (parameter instanceof dorado.util.Map) {
					criteria = parameter.get("criteria");
					if (criteria && (!criteria.criterions || !criteria.orders)) {
						parameter.remove("criteria");
					}
				}
				
				var dataColumns = this._columnsInfo.dataColumns;
				dataSet.flushAsync(function() {
					var sortOrderMap = {};
					for (var i = 0; i < orders.length; i++) {
						var order = orders[i];
						if (order.property) sortOrderMap[order.property] = !!order.desc;
					}
					for (var i = 0; i < dataColumns.length; i++) {
						var column = dataColumns[i], desc = sortOrderMap[column._property];
						if (desc == null) {
							column.set("sortState", null);
						} else {
							column.set("sortState", desc ? "desc" : "asc");
						}
					}
				});
			} else {
				return $invokeSuper.call(this, arguments);
			}
		}
	});
	
	var DataListBoxProtoType = dorado.widget.DataListBox.prototype;
	
	dorado.widget.grid.InnerDataGrid = $extend(dorado.widget.grid.AbstractInnerGrid, {
		$className: "dorado.widget.grid.InnerDataGrid",
		
		getItemId: DataListBoxProtoType.getItemId,
		getCurrentItem: DataListBoxProtoType.getCurrentItem,
		getCurrentItemId: DataListBoxProtoType.getCurrentItemId,
		getCurrentItemIdForRefresh: DataListBoxProtoType.getCurrentItemIdForRefresh,
		setCurrentItemDom: DataListBoxProtoType.setCurrentItemDom,
		refreshEntity: DataListBoxProtoType.refreshEntity,
		refreshItemDom: DataListBoxProtoType.refreshItemDom,
		onEntityDeleted: DataListBoxProtoType.onEntityDeleted,
		onEntityInserted: DataListBoxProtoType.onEntityInserted,
		
		_adjustBeginBlankRow: DataListBoxProtoType._adjustBeginBlankRow,
		_adjustEndBlankRow: DataListBoxProtoType._adjustEndBlankRow,
		
		setCurrentRowByItemId: function(itemId) {
			if (!this._itemDomMap) return;
			var row = (itemId == null) ? null : this._itemDomMap[itemId];
			var item = row ? $fly(row).data("item") : null;
			this.setCurrentEntity(item);
			this._itemModel.getItems().setCurrent(item);
		},
		
		setCurrentEntity: function(entity) {
			if (entity) {
				if (entity.dummy) {
					this.grid._requirePage(entity.page.pageNo);
					return false;
				}
				if (entity.rowType) {
					return false;
				}
			}
			DataListBoxProtoType.setCurrentEntity.apply(this, arguments);
			this.grid.doInnerGridSetCurrentRow(this, entity ? entity.entityId : null);
			this.fireEvent("onCurrentChange", this);
			return true;
		},
		
		doRefreshItemDomData: function(row, item) {
			$invokeSuper.call(this, arguments);
			row.dummy = item.dummy;
			if (row.dummy) {
				row.pageNo = item.page.pageNo;
				if (this._requiredPages) this._requiredPages.push(row.pageNo);
			}
			$fly(row).toggleClass("dummy-row", !!row.dummy);
		},
		
		refreshViewPortContent: function(container) {
			this._requiredPages = [];
			$invokeSuper.call(this, arguments);
			for (var i = 0; i < this._requiredPages.length; i++) {
				this.grid._requirePage(this._requiredPages[i]);
			}
		}
		
	});
	
})();
