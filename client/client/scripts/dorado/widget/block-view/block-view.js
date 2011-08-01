/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Collection
 * @class 块状列表控件。
 * @extends dorado.widget.AbstractBlockView
 */
dorado.widget.BlockView = $extend(dorado.widget.AbstractBlockView, /** @scope dorado.widget.BlockView.prototype */ {
	$className: "dorado.widget.BlockView",
	
	ATTRIBUTES: /** @scope dorado.widget.BlockView.prototype */ {
		/**
		 * 当前子元素的行号，此行号是从0开始计算的。
		 * @type int
		 * @attribute
		 */
		currentIndex: {
			skipRefresh: true,
			setter: function(index) {
				var itemModel = this._itemModel;
				if (index >= itemModel.getItemCount()) index = -1;
				if (this._currentIndex == index) return;
				this._currentIndex = index;
				if (this._rendered) {
					var blockDom = this.getItemDomByItemIndex(index);
					this.setCurrentBlock(blockDom);
					this.scrollCurrentIntoView();
				}
			}
		},
		
		/**
		 * 列表中要显示的数据。
		 * @type Object[]|dorado.EntityList
		 * @attribute
		 */
		items: {
			setter: function(v) {
				this._itemModel.setItems(v);
			},
			getter: function() {
				return this._itemModel.getItems();
			}
		}
	},
	
	refreshDom: function(dom) {
		this.set("currentIndex", this._currentIndex);
		$invokeSuper.call(this, arguments);
	},
	
	getItemDomByItemIndex: function(index) {
		return this._itemDomMap[index];
	},
	
	/**
	 * 返回当前子元素所对应的数据。
	 * @return {Object|dorado.Entity} 当前子元素所对应的数据。
	 */
	getCurrentItem: function() {
		if (this._currentIndex >= 0) {
			return this._itemModel.getItemAt(this._currentIndex);
		}
	},
	
	getCurrentItemId: function() {
		return this._currentIndex;
	},
	
	getCurrentItemIdForRefresh: function() {
		return this.getCurrentItemId();
	},
	
	setCurrentItemDom: function(blockDom) {
		this.set("currentIndex", blockDom ? blockDom.itemIndex : -1);
		return true;
	},
	
	doOnKeyDown: function(evt) {
	
		function previous() {
			if (this._currentIndex > 0) this.set("currentIndex", this._currentIndex - 1);
		}
		function next() {
			if (this._currentIndex < this._itemModel.getItemCount() - 1) this.set("currentIndex", this._currentIndex + 1);
		}
		function previousLine() {
			var i = this._currentIndex - this._realLineSize;
			if (i < 0) i = 0;
			this.set("currentIndex", i);
		}
		function nextLine() {
			var i = this._currentIndex + this._realLineSize;
			if (i >= this._itemModel.getItemCount()) i = this._itemModel.getItemCount() - 1;
			this.set("currentIndex", i);
		}
		
		var retValue = true;
		switch (evt.keyCode || evt.which) {
			case 36:{ // home
				this.set("currentIndex", 0);
				break;
			}
			case 35:{ // end
				this.set("currentIndex", this._itemModel.getItemCount() - 1);
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
	 * @param {int} index 要高亮的子元素的序号。
	 * @param {Object} [options] 高亮选项。见jQuery ui相关文档中关于highlight方法的说明。
	 * @param {Object} [speed] 动画速度。
	 */
	highlightItem: function(index, options, speed) {
		var block = this.getItemDomByItemIndex(index);
		if (block) {
			$fly(block).effect("pulsate", {
				times: 3
			}, speed || 230);
		}
	}
});
