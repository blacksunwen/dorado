/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Collection
 * @class 支持数据绑定的列表控件。
 * @extends dorado.widget.AbstractBlockView
 * @extends dorado.widget.DataControl
 */
dorado.widget.DataBlockView = $extend([dorado.widget.AbstractBlockView, dorado.widget.DataControl], /** @scope dorado.widget.DataBlockView.prototype */ {
	$className: "dorado.widget.DataBlockView",
	
	ATTRIBUTES: /** @scope dorado.widget.DataBlockView.prototype */ {
		selection: {
			setter: function(selection) {
				this.refresh();
				$invokeSuper.call(this, arguments);
			}
		}
	},
	
	getCurrentItem: function() {
		return (this._currentBlock) ? $fly(this._currentBlock).data("item") : null;
	},
	
	getCurrentItemId: function(item, index) {
		var currentItem = this.getCurrentItem();
		return currentItem ? currentItem.entityId : null;
	},
	
	getCurrentItemIdForRefresh: function() {
		var current = this._itemModel.getItems().current;
		return current ? current.entityId : null;
	},
	
	setCurrentItemDom: function(blockDom) {
		var item = $fly(blockDom).data("item"), b = this.setCurrentEntity(item);
		if (b) {
			this._itemModel.getItems().setCurrent(item);
		}
		return b;
	},
	
	/**
	 * 刷新某数据实体对应的块。
	 * @param {dorado.Entity} entity 数据实体
	 */
	refreshEntity: function(entity) {
		var blockDom = this._itemDomMap[entity.entityId];
		if (blockDom) this.refreshItemDomData(blockDom, entity);
	},
	
	refreshDom: function(dom) {
		var entityList = this.getBindingData({
			firstResultOnly: true,
			acceptAggregation: true
		});
		if (entityList && !(entityList instanceof dorado.EntityList)) {
			throw new dorado.ResourceException(dorado.list.BindingTypeMismatch);
		}
		this._itemModel.setItems(entityList);
		$invokeSuper.call(this, arguments);
	},
	
	/**
	 * 设置某记录对应的数据实体对应的行为当前行。
	 * @param {dorado.Entity} entity 数据实体。
	 */
	setCurrentEntity: function(entity) {
		var itemId = entity ? entity.entityId : null;
		var blockDom = this._itemDomMap[itemId];
		this.setCurrentBlock(blockDom);
		this.scrollCurrentIntoView();
		return true;
	},
	
	onEntityDeleted: function(arg) {
		var entity = arg.entity;
		if (this._lineCount != this._itemModel.getLineCount()) {
			var pos = this._getContainerSize();
			if (pos[2]) {
				this.refresh(true);
				return;
			} else {
				var $container = $fly(this._container);
				if (this._blockLayout == "vertical") {
					$container.height(pos[1]);
				} else {
					$container.width(pos[0]);
				}
			}
		}
		
		var blockDom = this._itemDomMap[entity.entityId];
		if (blockDom) {
			var from = blockDom.itemIndex;
			this.removeItemDom(blockDom);
			this._arrangeBlockDoms(from + 1, -1);
		}
	},
	
	onEntityInserted: function(arg) {
		var entity = arg.entity;
		if (this._lineCount != this._itemModel.getLineCount()) {
			var pos = this._getContainerSize();
			if (pos[2]) {
				this.refresh(true);
				return;
			} else {
				var $container = $fly(this._container);
				if (this._blockLayout == "vertical") {
					$container.height(pos[1]);
				} else {
					$container.width(pos[0]);
				}
			}
		}
		
		var mode = arg.insertMode;
		var refEntity = arg.refEntity;
		var container = this._container;
		switch (mode) {
			case "begin":
				this.refreshItemDom(container, entity, 0);
				this._arrangeBlockDoms(1, 1);
				break;
			case "before":
				var refBlockDom = this._itemDomMap[refEntity.entityId], index = refBlockDom.itemIndex;
				this._arrangeBlockDoms(index, 1);
				this.refreshItemDom(container, entity, index);
				break;
			case "after":
				var refBlockDom = this._itemDomMap[refEntity.entityId], index = refBlockDom.itemIndex;
				this._arrangeBlockDoms(index + 1, 1);
				this.refreshItemDom(container, entity, index + 1);
				break;
			default:
				this.refreshItemDom(container, entity, this._itemModel.getItemCount() - 1);
		}
	},
	
	filterDataSetMessage: function(messageCode, arg) {
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
	},
	
	processDataSetMessage: function(messageCode, arg, data) {
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
	},
	
	doOnKeyDown: function(evt) {
	
		function previous() {
			items.previous();
		}
		function next() {
			items.next();
		}
		function previousLine() {
			try {
				items.move(-this._realLineSize);
			} 
			catch (e) {
				dorado.Exception.removeException(e);
			}
		}
		function nextLine() {
			try {
				items.move(this._realLineSize);
			} 
			catch (e) {
				dorado.Exception.removeException(e);
			}
		}
		
		var retValue = true;
		var items = this._itemModel.getItems();
		switch (evt.keyCode) {
			case 36:{ // home
				items.first();
				break;
			}
			case 35:{ // end
				items.last();
				break;
			}
			case 37:{ // left
				((this._blockLayout == "vertical") ? previous : previousLine).call(this);
				retValue = false;
				break;
			}
			case 39:{ // right
				((this._blockLayout == "vertical") ? next : nextLine).call(this);
				retValue = false;
				break;
			}
			case 38:{ // up
				((this._blockLayout == "vertical") ? previousLine : previous).call(this);
				retValue = false;
				break;
			}
			case 40:{ // down
				((this._blockLayout == "vertical") ? nextLine : next).call(this);
				retValue = false;
				break;
			}
		}
		return retValue;
	},
	
	/**
	 * 高亮指定的子元素。
	 * @param {int} index 要高亮的子元素的数据实体。
	 * @param {Object} [options] 高亮选项。见jQuery ui相关文档中关于highlight方法的说明。
	 * @param {Object} [speed] 动画速度。
	 */
	highlightItem: function(entity, options, speed) {
		var block = this._itemDomMap[entity.entityId];
		if (block) {
			$fly(block).effect("pulsate", {
				times: 3
			}, speed || 300);
		}
	}
});
