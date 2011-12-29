(function() {

	var originJQueryFxUpdate = jQuery.fx.prototype.update;
	jQuery.fx.prototype.update = function() {
		originJQueryFxUpdate.apply(this, arguments);
		if (this.elem.nodeName.toUpperCase() == "TR") this.elem.style.display = "";
	};
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Collection
	 * @class 树状列表。
	 * @extends dorado.widget.AbstractTree
	 */
	dorado.widget.Tree = $extend(dorado.widget.AbstractTree, /** @scope dorado.widget.Tree.prototype */ {
		$className: "dorado.widget.Tree",
		_inherentClassName: "i-tree",
		
		ATTRIBUTES: /** @scope dorado.widget.Tree.prototype */ {
			className: {
				defaultValue: "d-tree"
			},
			
			width: {
				defaultValue: 200
			},
			
			rowHeight: {
				defaultValue: 24
			}
		},
		
		getCurrentItem: function() {
			if (this._currentIndex >= 0) {
				return this._itemModel.getItemAt(this._currentIndex);
			}
		},
		
		getCurrentItemId: function() {
			return (this._currentNode) ? this._currentNode._id : null;
		},
		
		setCurrentItemDom: function(row) {
			this.set("currentNode", row ? $fly(row).data("item") : null);
		},
		
		doRefreshItemDomData: function(row, node) {
			$invokeSuper.call(this, arguments);
			if (this._scrollMode != "viewport") node._dom = row;
		},
		
		removeItemDom: function(row) {
			if (this._scrollMode != "viewport") {
				if (row.parentNode) row.parentNode.removeChild(row);
			} else {
				var item = $fly(row).data("item");
				if (item) delete item._dom;
				$invokeSuper.call(this, arguments);
			}
		},
		
		createExpandingIndicator: function() {
			var row = this._expandingIndicator;
			if (row == null) {
				this._expandingIndicator = row = $DomUtils.xCreate({
					tagName: "TR",
					className: "d-tree-expanding-placeholder",
					content: "^TD"
				});
			}
			return row;
		},
		
		_refreshRearRows: function(fromRow) {
			if (fromRow && this._forceRefreshRearRows !== false) {
				var nextRow = fromRow, tbody = this._dataTBody;
				while (nextRow) {
					var item = $fly(nextRow).data("item");
					this.refreshItemDom(tbody, item, nextRow.sectionRowIndex);
					nextRow = nextRow.nextSibling;
				}
			}
		},
		
		_insertChildNodes: function(node, row, animated, callback) {
			
			function invokdCallback(refRow, callback, node) {
				this._refreshRearRows(refRow);
				if (callback) $callback(callback, true, node);
			}
			
			var tbody = this._dataTBody, refRow = row ? row.nextSibling : null;
			var it = new dorado.widget.tree.TreeNodeIterator(node), count = 0, bottom = -1;
			while (it.hasNext()) {
				var child = it.next();
				var newRow = this.createItemDom(child);
				if (animated && !this._duringAnimation) newRow.style.display = "none";
				(refRow) ? tbody.insertBefore(newRow, refRow) : tbody.appendChild(newRow);
				if (count > 10 && !this._shouldSkipRender) {
					if (bottom < 0) bottom = this._container.scrollTop + this._container.clientHeight;
					if (newRow.offsetTop + newRow.offsetHeight > bottom) this._shouldSkipRender = true;
				}
				this.refreshItemDom(tbody, child, newRow.sectionRowIndex);
				count++;
			}
			this._shouldSkipRender = false;
			
			if (animated && !this._duringAnimation) {
				var indicator = this.createExpandingIndicator(), self = this;
				indicator.style.height = 0;
				(refRow) ? tbody.insertBefore(indicator, refRow) : tbody.appendChild(indicator);
				
				this._duringAnimation = true;
				$fly(indicator).animate({
					"height": "+=" + (self._rowHeight * count)
				}, 200, "swing", function() {
					$fly(indicator).remove();
					it = new dorado.widget.tree.TreeNodeIterator(node);
					while (it.hasNext()) {
						var child = it.next(), childRow = self._itemDomMap[child._id];
						if (childRow) childRow.style.display = "";
					}
					self.notifySizeChange();
					invokdCallback.call(self, refRow, callback, node);
					self._duringAnimation = false;
				});
			} else {
				this.notifySizeChange();
				invokdCallback.call(this, refRow, callback, node);
			}
		},
		
		_removeChildNodes: function(node, animated, callback) {
			
			function invokdCallback(node, callback, node) {
				if (this._forceRefreshRearRows !== false) {
					var row = this._itemDomMap[node._id];
					if (row) this._refreshRearRows(row);
				}
				if (callback) $callback(callback, true, node);
			}
			
			var it = new dorado.widget.tree.TreeNodeIterator(node);
			var rowsToRemove = [];
			while (it.hasNext()) {
				var child = it.next();
				if (child == this._currentNode) {
					this.set("currentNode", node);
				}
				var childRow = this._itemDomMap[child._id];
				if (childRow) rowsToRemove.push(childRow);
			}
			
			if (rowsToRemove.length) {
				if (animated && !this._duringAnimation) {
					var indicator = this.createExpandingIndicator();
					indicator.style.height = (this._rowHeight * rowsToRemove.length) + "px";
					this._dataTBody.insertBefore(indicator, rowsToRemove[0]);
				}
				
				for (var i = 0; i < rowsToRemove.length; i++) {
					var childRow = rowsToRemove[i];
					this.removeItemDom(childRow);
				}
				
				if (animated && !this._duringAnimation) {
					var self = this;
					this._duringAnimation = true;
					$fly(indicator).animate({
						"height": 0
					}, 200, "swing", function() {
						$fly(indicator).remove();
						self.notifySizeChange();
						invokdCallback.call(self, node, callback, node);
						self._duringAnimation = false;
					});
				}
			} else {
				this.notifySizeChange();
				invokdCallback.call(this, node, callback, node);
			}
		},
		
		_refreshAndScroll: function(node, mode, parentNode, nodeIndex) {
			var shouldScroll = false, itemModel = this._itemModel;
			if (parentNode._expanded) {
				var row = this._itemDomMap[node._id];
				if (!row) {
					var index;
					if (mode == "remove") {
						index = itemModel.getItemIndex(parentNode) + 1;
						var it = parentNode._nodes.iterator();
						var i = 0;
						while (it.hasNext() && i < nodeIndex) {
							var n = it.next();
							index++, i++;
							if (n._expanded) index += n._visibleChildNodeCount;
						}
					} else {
						index = itemModel.getItemIndex(node);
					}
					
					if (index >= 0) {
						var startIndex = itemModel.getStartIndex();
						switch (mode) {
							case "insert":{
								if (index < startIndex) {
									itemModel.setStartIndex(startIndex + node._visibleChildNodeCount + 1);
									shouldScroll = true;
								}
								break;
							}
							case "remove":{
								if ((index + node._visibleChildNodeCount + 1) < startIndex) {
									itemModel.setStartIndex(startIndex - node._visibleChildNodeCount - 1);
									shouldScroll = true;
								} else if (index < startIndex) {
									itemModel.setStartIndex(index);
									shouldScroll = true;
								}
								break;
							}
							case "expand":{
								if (index < startIndex) {
									itemModel.setStartIndex(startIndex + node._visibleChildNodeCount);
									shouldScroll = true;
								}
								break;
							}
							case "collapse":{
								if ((index + node._visibleChildNodeCount) < startIndex) {
									itemModel.setStartIndex(startIndex - node._visibleChildNodeCount);
									shouldScroll = true;
								} else if (index < startIndex) {
									itemModel.setStartIndex(index + 1);
									shouldScroll = true;
								}
								break;
							}
						}
					}
				}
				this.refreshDom(this._dom);
				if (shouldScroll) {
					this._container.scrollTop = this._scrollTop = this._dataTBody.offsetTop;
				}
			} else {
				var refreshParent = false;
				switch (mode) {
					case "insert":{
						refreshParent = parentNode._nodes.size == 1;
						break;
					}
					case "remove":{
						refreshParent = !parentNode.get("hasChild");
						break;
					}
				}
				if (refreshParent) {
					var parentRow = this._itemDomMap[parentNode._id];
					if (parentRow) {
						this._ignoreItemTimestamp = true;
						this.refreshItemDomData(parentRow, parentNode);
					}
				}
			}
		},
		
		_getExpandingAnimated: function() {
			return this._expandingAnimated;
		},
		
		_nodeExpanded: function(node, callback) {
			if (!this._rendered || !this._attached || this._autoRefreshLock > 0) return;
			if (this._scrollMode != "viewport") {
				if (node == this._root) {
					this._insertChildNodes(node, null, this._getExpandingAnimated(), callback);
					this.notifySizeChange();
				} else {
					var row = this._itemDomMap[node._id];
					if (row) {
						this._insertChildNodes(node, row, this._getExpandingAnimated(), callback);
						this.refreshItemDomData(row, node);
					}
				}
			} else {
				this._refreshAndScroll(node, "expand", node._parent);
				if (callback) $callback(callback, true, node);
			}
		},
		
		_nodeCollapsed: function(node, callback) {
			if (!this._rendered || !this._attached || this._autoRefreshLock > 0) return;
			if (this._scrollMode != "viewport") {
				this._removeChildNodes(node, this._getExpandingAnimated(), callback);
				var row = this._itemDomMap[node._id];
				if (row) this.refreshItemDomData(row, node);
			} else {
				this._refreshAndScroll(node, "collapse", node._parent);
				if (callback) $callback(callback, true, node);
			}
		},
		
		_nodeInserted: function(node) {
			if (!this._rendered || !this._attached || this._autoRefreshLock > 0) return;
			if (this._scrollMode != "viewport") {
				var parentNode = node._parent;
				if (parentNode._expanded) {
					var nodeIndex = this._itemModel.getItemIndex(node);
					if (nodeIndex >= 0) {
						var originExpanded = node._expanded;
						node._expanded = false;
						try {
							var it = new dorado.widget.tree.TreeNodeIterator(this._itemModel.getItems(), {
								nextIndex: nodeIndex
							});
							it.next();
							var nextsibling = it.next(), tbody = this._dataTBody, refRow;
						}
						finally {
							node._expanded = originExpanded;
						}
						if (nextsibling) refRow = this._itemDomMap[nextsibling._id];
						var newRow = this.createItemDom(node);
						(refRow) ? tbody.insertBefore(newRow, refRow) : tbody.appendChild(newRow);
						this.refreshItemDom(tbody, node, newRow.sectionRowIndex);
						if (originExpanded) this._insertChildNodes(node, newRow);
						this.notifySizeChange();
					}
				}
				
				if (parentNode && parentNode._nodes.size == 1) {
					var parentRow = this._itemDomMap[parentNode._id];
					if (parentRow) {
						this._ignoreItemTimestamp = true;
						this.refreshItemDomData(parentRow, parentNode);
					}
				}
			} else {
				this._refreshAndScroll(node, "insert", node._parent);
			}
		},
		
		_nodeRemoved: function(node, parentNode, index) {
			if (!this._rendered || !this._attached || this._autoRefreshLock > 0) return;
			if (this._scrollMode != "viewport") {
				if (parentNode) {
					if (parentNode._expanded) {
						this._forceRefreshRearRows = false;
						if (node._expanded) this._removeChildNodes(node);
						this._forceRefreshRearRows = true;
						var row = this._itemDomMap[node._id];
						if (row) {
							var nextRow = row.nextSibling;
							this.removeItemDom(row);
							if (nextRow) this._refreshRearRows(nextRow);
						}
					}
					
					if (!parentNode.get("hasChild")) {
						var parentRow = this._itemDomMap[parentNode._id];
						if (parentRow) {
							this._ignoreItemTimestamp = true;
							this.refreshItemDomData(parentRow, parentNode);
						}
					}
					this.notifySizeChange();
				}
			} else {
				this._refreshAndScroll(node, "remove", parentNode, index);
			}
		}
	});
	
})();
