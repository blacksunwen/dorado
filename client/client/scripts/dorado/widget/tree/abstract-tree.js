/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 树节点的渲染器。
 * @extends dorado.Renderer
 */
dorado.widget.tree.TreeNodeRenderer = $extend(dorado.Renderer, {
	createIconDom: function(tree) {
		var icon = document.createElement("LABEL");
		icon.className = "icon";
		return icon;
	},

	/**
	 * 返回树节点的文本标签值。
	 * @protected
	 * @param {dorado.widget.tree.Node} node 树节点。
	 * @return {String} 文本标签值。
	 */
	getLabel: function(node) {
		return node.get("label");
	},

	/**
	 * 渲染树节点中的文本标签部分。
	 * @protected
	 * @param {HTMLElement} labelDom 对应的DOM对象。
	 * @param {String} label 树节点的文本标签值。
	 * @param {dorado.widget.tree.Node} node 树节点。
	 */
	renderLabel: function(labelDom, label, node) {
		var tree = node._tree, arg = {
			dom: labelDom,
			label: label,
			node: node,
			processDefault: (tree.getListenerCount("onRenderNode") == 0)
		};
		if (tree) tree.fireEvent("onRenderNode", tree, arg);
		if (arg.processDefault) {
			labelDom.innerText = label;
			labelDom.title = node.get("tip") || '';
		}
	},

	createCheckboxDom: function(tree) {
		return new dorado.widget.CheckBox({
			iconOnly: true,
			triState: true,
			onValueChange: function(self, arg) {
				var row = tree.findItemDomByEvent(self.getDom());
				var node = $fly(row).data("item");
				var checked = self.get("checked");
				node.set("checked", (checked == null || checked)); // 由于是triState CheckBox，所以逻辑有点怪
				self.set("checked", node.get("checked"));
			}
		});
	},

	/**
	 * @name dorado.widget.tree.TreeNodeRenderer#doRender
	 * @protected
	 * @param {HTMLElement} dom 对应的DOM对象。
	 * @param {dorado.widget.tree.Node} node 树节点。
	 * 内部的渲染方法，供复写。
	 */
	doRender: function(cell, node) {
		var tree = node._tree;

		cell.style.paddingLeft = ((node.get("level") - 1) * tree._indent) + "px";

		var cls = ["collapse-button", "expand-button"], buttonDom = cell.firstChild, $buttonDom = jQuery(buttonDom);
		if (node.get("hasChild")) {
			if (node._expanded) cls.reverse();
			$buttonDom.removeClass(cls[0]).addClass(cls[1]);
		} else {
			$buttonDom.removeClass(cls[0]).removeClass(cls[1]);
		}
		$buttonDom.toggleClass("button-expanding", !!node._expanding);
		this.renderLabel(cell.lastChild, this.getLabel(node), node);

		var icon, iconClass;
		if (node._expanded) {
			icon = node.get("expandedIcon") || tree._defaultExpandedIcon;
			iconClass = node.get("expandedIconClass") || tree._defaultExpandedIconClass;
		}
		icon = icon || node.get("icon") || tree._defaultIcon;
		iconClass = iconClass || node.get("iconClass") || tree._defaultIconClass;

		if (cell.doradoHasIcon) {
			if (!icon) {
				cell.removeChild(cell.childNodes[1]);
				cell.doradoHasIcon = false;
			}
		} else if (icon || iconClass) {
			cell.insertBefore(this.createIconDom(tree), cell.childNodes[1]);
			cell.doradoHasIcon = true;
		}

		var iconDom = cell.childNodes[1];
		if (icon) {
			$DomUtils.setBackgroundImage(iconDom, icon);
		}
		else if (iconClass) {
			$fly(iconDom).addClass(iconClass);
		}

		var checkable = node.get("checkable"), checkbox;
		if (cell.subCheckboxId) {
			checkbox = dorado.widget.Component.ALL[cell.subCheckboxId];
			if (!checkable) {
				checkbox.unrender();
				tree.unregisterInnerControl(checkbox);
				checkbox.destroy();
				cell.subCheckboxId = null;
			}
		} else if (checkable) {
			checkbox = this.createCheckboxDom(tree), checkboxIndex = cell.doradoHasIcon ? 2 : 1;
			checkbox.render(cell, cell.childNodes[checkboxIndex]);
			tree.registerInnerControl(checkbox);
			cell.subCheckboxId = checkbox._uniqueId;
		}
		if (checkable && checkbox) {
			checkbox.set("checked", node.get("checked"));
			checkbox.refresh();
		}
	},

	/**
	 * 渲染。
	 * <p><b>如有需要应在子类中复写doRender方法，而不是此方法。</b></p>
	 * @param {HTMLElement} dom 表格行对应的DOM对象。
	 * @param {HTMLElement} dom 对应的DOM对象。
	 * @param {dorado.widget.tree.Node} node 树节点。
	 * @see dorado.widget.tree.TreeNodeRenderer#doRender
	 */
	render: function(row, node) {
		this.doRender(row.firstChild, node);
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 树状列表。
 * @abstract
 * @extends dorado.widget.RowList
 */
dorado.widget.AbstractTree = $extend(dorado.widget.RowList, /** @scope dorado.widget.AbstractTree.prototype */ {
	$className: "dorado.widget.AbstractTree",

	selectable: false,

	ATTRIBUTES: /** @scope dorado.widget.AbstractTree.prototype */ {
		/**
		 * 根节点。此节点时树状列表内部的顶层节点，其不可显示。
		 * @type dorado.widget.tree.Node
		 * @attribuate readOnly
		 */
		root: {
			readOnly: true
		},

		/**
		 * 子节点集合。
		 * <p>
		 * 此属性在读写时的意义不完全相同。
		 * <ul>
		 * <li>当读取时树状列表中的子节点集合，类型为{@link dorado.util.KeyedList}。</li>
		 * <li>
		 * 当写入时用于向树状列表中添加子节点。<br>
		 * 此处数组中既可以放入子节点的实例，又可以放入JSON对象。
		 * 具体请参考{@link dorado.widget.tree.Node#addNodes}。
		 * </li>
		 * </ul>
		 * </p>
		 * @type dorado.util.KeyedList|[Object]|[dorado.widget.tree.Node]
		 * @see dorado.widget.tree.Node#addNodes
		 * @attribute
		 */
		nodes: {
			setter: function(nodes) {
				this._root.addNodes(nodes);
			},
			getter: function() {
				return this._root._nodes;
			}
		},

		/**
		 * 当前节点。
		 * @type dorado.widget.tree.Node
		 * @attribute skipRefresh
		 */
		currentNode: {
			skipRefresh: true,
			setter: function(node) {
				if (this._currentNode == node) return;
				if (node == this._root) node = null;
				var eventArg = {
					oldCurrent: this._currentNode,
					newCurrent: node,
					processDefault: true
				};
				this.fireEvent("beforeCurrentChange", this, eventArg);
				if (!eventArg.processDefault) return;
				this._currentNode = node;
				this.fireEvent("onCurrentChange", this, eventArg);
				if (this._rendered) {
					$setTimeout(this, function() {
						var row = node ? this._itemDomMap[node._id] :null;
						this.setCurrentRow(row);
						if (row) this.scrollCurrentIntoView();
					}, 50);
				}
			}
		},

		/**
		 * 每一层子节点相对于父节点的缩进距离。
		 * @type int
		 * @attribute
		 * @default 16
		 */
		indent: {
			defaultValue: 16
		},

		/**
		 * 默认的节点展开模式。
		 * <p>目前支持下列两种取值：
		 * <ul>
		 * <li>async	-	异步模式。</li>
		 * <li>sync	-	同步模式。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute skipRefresh
		 * @default "async"
		 */
		expandingMode: {
			defaultValue: "async",
			skipRefresh: true
		},

		/**
		 * 拖放模式。
		 * <p>
		 * 此属性具有如下几种取值：
		 * <ul>
		 * <li>control - 只能将目标拖放到整个控件上。</li>
		 * <li>onItem - 只能将目标拖放到某个树节点上。</li>
		 * <li>insertItems - 此项对于树而言没有实际意义，系统会自动将此项按照onOrInsertItems来处理。</li>
		 * <li>onOrInsertItems - 可以将目标拖放到某个某个树节点上或以顺序敏感的方式插入到树节点中。</li>
		 * <li>onAnyWhere - 可以将目标拖放以上的所有位置。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute writeBeforeReady
		 * @default "onItem"
		 */
		dropMode: {
			defaultValue: "onItem",
			setter: function(v) {
				if (v == "insertItems") v = "onOrInsertItems";
				this._dropMode = v;
			}
		},

		/**
		 * 是否启用节点展开收缩时的动画效果。
		 * <p>注意：此动画效果对于scrollMode为viewport的树形列表控件时无效的。</p>
		 * @type boolean
		 * @attribute skipRefresh
		 * @default true
		 */
		expandingAnimated: {
			defaultValue: true,
			skipRefresh: true
		},

		/**
		 * 默认的节点图标URL。
		 * @type String
		 * @attribute
		 */
		defaultIcon: {},

		/**
		 * 默认的节点图标元素的CSS Class。
		 * @type String
		 * @attribute
		 */
		defaultIconClass: {},

		/**
		 * 默认的节点展开后的图标URL。
		 * 如果此属性未定义则直接使用icon属性中定义的图标作为展开后的图标。
		 * @type String
		 * @attribute
		 */
		defaultExpandedIcon: {},

		/**
		 * 默认的节点展开后图标元素的CSS Class。
		 * @type String
		 * @attribute
		 */
		defaultExpandedIconClass: {},

		/**
		 * 返回树中的第一个有效节点。
		 * @type dorado.widget.tree.Node
		 * @attribute readOnly
		 */
		firstNode: {
			readOnly: true,
			getter: function() {
				return this._root.get("firstNode");
			}
		}
	},

	EVENTS: /** @scope dorado.widget.AbstractTree.prototype */ {

		/**
		 * 当一个节点将被展开之前触发的事件。
		 * <p>
		 * 此事件比较常见的使用场景是用于在节点展开之前为该节点创建下级子节点，以达到动态构造树结构的目的。<br>
		 * 不过，由于节点的展开动作有同步和异步两种执行方式（见{@link dorado.widget.tree.Node#expand}、{@link dorado.widget.tree.Node#expandAsync}）。
		 * 因此如果要让自己的事件代码支持异步的执行方式，就必须在代码中提供一些特别的异步操作支持。
		 * 主要是必须在异步过程结束之后主动的激活系统提供arg.callback回调对象，见示例代码。<br>
		 * <b>需要特别注意的是，不论自己的异步过程的执行成功与否你都应该激活系统提供的arg.callback回调对象，否则系统会一直认为该节点的展开过程没有结束。</b>
		 * </p>
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {boolean} arg.async 是否正以异步方式执行展开操作。
		 * @param {dorado.widget.tree.Node} arg.node 要展开的节点。
		 * @param {Function} arg.callDefault 直接调用此事件的系统后续处理逻辑，在同步方式的执行过程中此参数无效。<br>
		 * 此处的arg.callDefault方法还支持两个传入参数：
		 * @param {boolean} [arg.callDefault.success=true] 用于通知系统本次展开节点的操作是否成功。
		 * @param {Object|Error|dorado.Exception} [arg.callDefault.exception] 如果展开节点的操作是失败的，那么可以使用此参数向系统传递具体的错误信息。
		 * @param {boolean} #arg.processDefault=true 用于通知系统是否要继续完成节点展开的后续动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @see dorado.widget.AbstractTree#attribute:expandingMode
		 * @see dorado.widget.tree.Node#expand
		 * @see dorado.widget.tree.Node#expandAsync
		 * @event
		 *
		 * @example
		 * tree.addListener("beforeExpand", function(self, arg) {
		 * 	var ajaxOptions = {
		 * 		url: "/get-nodes.do",
		 * 		method: "POST",
		 * 		jsonData: arg.node.get("userData.id")
		 * 	};
		 * 	if (arg.async) {
		 * 		// 以下代码用以支持异步的展开模式
		 * 		$ajax.request(ajaxOptions, function(result) {
		 * 			if (result.success) {
		 * 				arg.node.set("nodes", result.getJsonData());
		 * 				arg.callDefault(true);	// 通知系统展开过程已成功完成
		 * 			}
		 * 			else {
		 * 				arg.callDefault(false, result.error);	// 通知系统展开过程失败，同时传入异常信息。
		 * 			}
		 * 		});
		 * 	}
		 * 	else {
		 * 		// 以下代码用以支持同步的展开模式
		 * 		var result = $ajax.requestSync(ajaxOptions);
		 * 		arg.node.set("nodes", result.getJsonData());
		 * 	}
		 * });
		 */
		beforeExpand: {
			disallowMultiListeners: true
		},

		/**
		 * 当一个节点将被展开之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 展开的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onExpand: {},

		/**
		 * 当一个节点将被收缩之前触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相关的节点。
		 * @param {boolean} #arg.processDefault=true 用于通知系统是否要继续完成节点收起的后续动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeCollapse: {},

		/**
		 * 当一个节点将被收缩之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相关的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCollapse: {},

		/**
		 * 当一个节点被附着到树状列表之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相关的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onNodeAttached: {},

		/**
		 * 当一个节点离开树状列表（即失去附着状态）之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相关的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onNodeDetached: {},

		/**
		 * 当前节点改变之前触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.oldCurrent 目前的当前节点。
		 * @param {dorado.widget.tree.Node} arg.newCurrent 新的的当前节点，将要被设置为当前节点的节点。
		 * @param {boolean} #arg.processDefault=true 用于通知系统是否要继续完成设置当前节点的后续动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeCurrentChange: {},

		/**
		 * 当前节点改变之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.oldCurrent 目前的当前节点。
		 * @param {dorado.widget.tree.Node} arg.newCurrent 新的的当前节点，将要被设置为当前节点的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCurrentChange: {},

		/**
		 * 当树状列表渲染树节点时触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {HTMLElement} arg.dom 树节点对应的DOM对象。
		 * @param {String} arg.label 节点标题文本。
		 * @param {dorado.widget.tree.Node} arg.node 渲染的节点。
		 * @param {boolean} #arg.processDefault 是否在事件结束后继续使用系统默认的渲染逻辑。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onRenderNode: {},

		/**
		 * 当某个树节点的勾选状态将要被改变时触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相应的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeNodeCheckedChange: {},

		/**
		 * 当某个树节点的勾选状态将要被改变后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.tree.Node} arg.node 相应的节点。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onNodeCheckedChange: {}
	},

	constructor: function() {
		var root = this._root = this.createRootNode();
		root._setTree(this);
		root._expanded = true;

		this._autoRefreshLock = 0;
		$invokeSuper.call(this, arguments);
	},

	destroy: function() {
		if (this._scrollMode != "viewport") this._root._setTree(null);
		$invokeSuper.call(this, arguments);
	},

	createRootNode: function() {
		return new dorado.widget.tree.Node("<ROOT>");
	},
	
	/**
	 * 清除树中的所有树节点。
	 */
	clearNodes: function() {
		this._root.clearChildren();
	},

	createItemModel: function() {
		var im = new dorado.widget.tree.ItemModel();
		im.setItems(this._root);
		return im;
	},

	createItemDom: function(node) {
		var row = node._dom;
		if (!row || row.parentNode != null) {
			row = document.createElement("TR");
			row.className = "row";
			row.style.height = this._rowHeight + "px";

			if (this._scrollMode == "lazyRender" && this._shouldSkipRender) {
				row._lazyRender = true;
			} else {
				this.createItemDomDetail(row, node);
			}
		}
		return row;
	},

	createItemDomDetail: function(row, node) {
		var cell = $DomUtils.xCreateElement({
			tagName: "TD",
			className: "i-tree-node d-tree-node",
			content: [{
				tagName: "LABEL",
				doradoType: "tree-button",
				className: "button"
			}, {
				tagName: "LABEL",
				className: "label",
				whiteSpace: "no-wrap"
			}]
		});

		var buttonDom = cell.firstChild, $buttonDom = jQuery(buttonDom), self = this;
		$buttonDom.mousedown(function() {
			return false;
		}).click(function() {
			var row = buttonDom.parentNode.parentNode;
			var node = $fly(row).data("item");
			if (node.get("hasChild")) {
				if (node._expanded) node.collapse();
				else if (self._expandingMode == "sync") node.expand();
				else node.expandAsync();
				$buttonDom.removeClass("expand-button-hover collapse-button-hover");
			}
			return false;
		}).hover(function() {
			if ($buttonDom.hasClass("expand-button")) $buttonDom.addClass("expand-button-hover");
			if ($buttonDom.hasClass("collapse-button")) $buttonDom.addClass("collapse-button-hover");
		}, function() {
			$buttonDom.removeClass("expand-button-hover collapse-button-hover");
		});
		row.appendChild(cell);
	},

	doRefreshItemDomData: function(row, node) {
		(this._renderer || $singleton(dorado.widget.tree.TreeNodeRenderer)).render(row, node);
	},

	getItemTimestamp: function(node) {
		return node.getTimestamp();
	},

	onNodeAttached: function(node) {
		if (this._itemModel) {
			this._itemModel.onNodeAttached(node);
			this.fireEvent("onNodeAttached", this, {
				node: node
			});
		}
	},

	onNodeDetached: function(node) {
		if (this._itemModel) {
			this._itemModel.onNodeDetached(node);
			this.fireEvent("onNodeDetached", this, {
				node: node
			});
		}
	},
	
	refreshItemDoms: function(tbody, reverse, fn) {
		if (this._duringAnimation) return;
		return $invokeSuper.call(this, arguments);
	},

	/**
	 * 刷新某树节点的显示。
	 * @param {dorado.widget.tree.Node} node 要刷新的节点。
	 */
	refreshNode: function(node) {
		if (node) dorado.Toolkits.cancelDelayedAction(node, "$refreshDelayTimerId");
		if (this._autoRefreshLock > 0 || !this._itemDomMap) return;
		var row = this._itemDomMap[node._id];
		if (row) this.refreshItemDomData(row, node);
	},

	/**
	 * 禁止树状列表自动根据作用在树节点上的自动刷新显示。
	 */
	disableAutoRefresh: function() {
		this._autoRefreshLock++;
	},

	/**
	 * 允许树状列表自动根据作用在树节点上的自动刷新显示。
	 */
	enableAutoRefresh: function() {
		this._autoRefreshLock--;
		if (this._autoRefreshLock < 0) this._autoRefreshLock = 0;
	},

	/**
	 * 根据传入的DHTML Event返回相应的树节点。
	 * <p>此方法一般用于onMouseDown、onClick等事件，用于获得此时鼠标实际操作的树节点。</p>
	 * @param {Event} event DHTML中的Event对象。
	 * @return {dorado.widget.tree.Node} 相应的树节点。
	 */
	getNodeByEvent: function(event) {
		var row = this.findItemDomByEvent(event);
		return (row) ? $fly(row).data("item") : null;
	},

	/**
	 * 返回树中所有被勾选选中的节点的数组。
	 * @return {dorado.widget.tree.Node[]} 节点数组。
	 */
	getCheckedNodes: function() {
		var it = new dorado.widget.tree.TreeNodeIterator(this._root, {
			includeInvisibleNodes: true
		}), nodes = [];
		while (it.hasNext()) {
			var node = it.next();
			if (node.get("checked")) nodes.push(node);
		}
		return nodes;
	},

	/**
	 * 高亮指定的树节点。
	 * @param {dorado.widget.tree.Node} node 要高亮的树节点。
	 * @param {Object} [options] 高亮选项。见jQuery ui相关文档中关于highlight方法的说明。
	 * @param {Object} [speed] 动画速度。
	 */
	highlightItem: function(node, options, speed) {
		if (node._tree != this) return;
		var row = this._itemDomMap[node._id];
		if (row) {
			$fly(row.firstChild).effect("highlight", options|| {
					color: "#FFFF80"
				}, speed || 1500);
		}
		else if (!node._disableDelayHighlight) {
			var self = this;
			setTimeout(function() {
				node._disableDelayHighlight = true;
				self.highlightItem(node, options, speed);
				node._disableDelayHighlight = false;
			}, 100);
		}
	},

	initDraggingIndicator: function(indicator, draggingInfo, evt) {
		if (this._dragMode != "control") {
			var itemDom = draggingInfo.get("element");
			if (itemDom) {
				var cell = itemDom.firstChild;
				var contentDom = $DomUtils.xCreateElement({
					tagName: "div",
					className: "d-list-dragging-item " + cell.className
				});
				var children = [];
				for (var i = 1; i < cell.childNodes.length; i++) {
					var child = cell.childNodes[i];
					children.push(child);
				}
				$fly(children).clone().appendTo(contentDom);
				indicator.set("content", contentDom);
			}
		}
	},

	doOnDraggingSourceMove: function(draggingInfo, evt, targetObject, insertMode, refObject, itemDom) {
		var oldInsertMode = insertMode;

		if (itemDom) {
			if (insertMode) {
				var dom = this._dom, node = $fly(itemDom).data("item");
				if (insertMode == "after" && node._expanded && node._nodes.size) {
					targetObject = node;
					insertMode = "before";
					refObject = node.get("firstNode");
				} else {
					targetObject = node._parent;
				}
			}

			if (draggingInfo.get("sourceControl") == this) {
				var node = targetObject;
				while (node) {
					if (node == draggingInfo.get("object")) {
						targetObject = null;
						itemDom = null;
						break;
					}
					node = node._parent;
				}
			}
		}
		return $invokeSuper.call(this, [draggingInfo, evt, targetObject, oldInsertMode, refObject, itemDom]);
	},

	showDraggingInsertIndicator: function(draggingInfo, insertMode, itemDom) {

		function getNodeContentOffsetLeft(nodeDom) {
			return nodeDom.firstChild.childNodes[1].offsetLeft;
		}

		if (insertMode) {
			var insertIndicator = dorado.widget.RowList.getDraggingInsertIndicator();
			var dom = this._dom, node = $fly(itemDom).data("item");

			var left = getNodeContentOffsetLeft(itemDom);
			if (draggingInfo.get("targetObject") == $fly(itemDom).data("item")) {
				left += this._indent;
			}
			var width = dom.offsetWidth;
			if (dom.clientWidth < width) width = dom.clientWidth;
			width -= left;

			var top = (insertMode == "before") ? itemDom.offsetTop : (itemDom.offsetTop + itemDom.offsetHeight);
			$fly(insertIndicator).width(width).height(2).left(left).top(top - 1).show();
			dom.appendChild(insertIndicator);
		} else {
			$invokeSuper.call(this, arguments);
		}
	},

	processItemDrop: function(draggingInfo, evt) {
		var object = draggingInfo.get("object");
		var targetObject = draggingInfo.get("targetObject");
		var insertMode = draggingInfo.get("insertMode");
		var refObject = draggingInfo.get("refObject");
		if (object instanceof dorado.widget.tree.Node && targetObject instanceof dorado.widget.tree.Node) {
			object.remove();
			targetObject.addNode(object, insertMode, refObject);
			this.highlightItem(object);
			return true;
		}
		return false;
	}
});
