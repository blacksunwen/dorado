/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Collection
 * @class 支持数据绑定的树状表格控件。
 * @extends dorado.widget.TreeGrid
 * @extends dorado.widget.DataTree
 */
dorado.widget.DataTreeGrid = $extend([dorado.widget.TreeGrid, dorado.widget.DataTree], /** @scope dorado.widget.DataTreeGrid.prototype */ {
	$className: "dorado.widget.DataTreeGrid",
	
	ATTRIBUTES: /** @scope dorado.widget.DataTreeGrid.prototype */ {
		/**
		 * 是否自动根据绑定的EntityDataType自动创建其中的表格列。
		 * @type boolean
		 * @default true
		 * @attribute
		 */
		autoCreateColumns: {
			defaultValue: true
		},
		
		selection: {
			setter: function(selection) {
				this.refresh();
				$invokeSuper.call(this, arguments);
			}
		}
	},
	
	onReady: dorado.widget.DataTree.prototype.onReady,
	
	createRootNode: function() {
		return new dorado.widget.tree.DataBindingNode({
			bindingConfig: {}
		});
	},
	
	onCellValueEdited: null,
	
	initColumns: dorado.widget.DataGrid.prototype.initColumns,
	
	refresh: function() {
		var columnsInited = false;
		if (this._dataSet) {
			var dataType, data = this.getBindingData({
				firstResultOnly: true,
				acceptAggregation: true
			});
			if (data && data.dataType) dataType = data.dataType.getElementDataType("auto");
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
			
			if (!this._root._childrenPrepared || this._data != data ||
				(this._data && this._data.pageNo != (this._pageNo || 0))) {
				this._data = data;
				this._pageNo = (data ? data.pageNo : 0);
				this._root._prepareChildren(dorado._NULL_FUNCTION);
			}
		}
		if (!columnsInited) this.initColumns();
		$invokeSuper.call(this, arguments);
	},
	
	processItemDrop: dorado.widget.DataTree.prototype.processItemDrop,
	filterDataSetMessage:  dorado.widget.DataTree.prototype.filterDataSetMessage
});
