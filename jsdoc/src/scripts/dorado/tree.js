
dorado.widget.tree = {};
dorado.widget.tree.NodeList = $extend(dorado.util.KeyedArray, {$className:"dorado.widget.tree.NodeList", constructor:function (parent, getKeyFunction) {
    $invokeSuper.call(this, [getKeyFunction]);
    this.parent = parent;
}, beforeInsert:function (node) {
    var parentNode = node._parent = this.parent;
    var originHasChild = parentNode.get("hasChild");
    node._setTree(parentNode._tree);
    return {parentNode:parentNode, originHasChild:originHasChild};
}, afterInsert:function (node, ctx) {
    var parentNode = ctx.parentNode, originHasChild = ctx.originHasChild, tree = parentNode._tree;
    parentNode._changeVisibleChildNodeCount(1 + ((node._expanded) ? node._visibleChildNodeCount : 0));
    if (tree && (parentNode._expanded || !originHasChild) && tree._rendered && tree._attached && tree._autoRefreshLock < 1) {
        tree._nodeInserted(node);
    }
}, beforeRemove:function (node) {
    var tree = node._tree, parentNode = this.parent, index = this.indexOf(node);
    if (tree && node == tree._currentNode && parentNode) {
        var newCurrent;
        var size = parentNode._nodes.size;
        if (size == 1) {
            newCurrent = parentNode;
        } else {
            var i = 0;
            if (index < size - 1) {
                i = index + 1;
            } else {
                if (index > 0) {
                    i = index - 1;
                }
            }
            newCurrent = parentNode._nodes.get(i);
        }
        tree.set("currentNode", newCurrent);
    }
    node._setTree(null);
    delete node._parent;
    return {parentNode:parentNode, index:index};
}, afterRemove:function (node, ctx) {
    var parentNode = ctx.parentNode, index = ctx.index, tree = parentNode._tree;
    parentNode._changeVisibleChildNodeCount(-1 - ((node._expanded) ? node._visibleChildNodeCount : 0));
    if (tree && parentNode._expanded && tree._rendered && tree._attached && tree._autoRefreshLock < 1) {
        tree._nodeRemoved(node, parentNode, index);
    }
}});
dorado.widget.tree.Node = $extend([dorado.AttributeSupport, dorado.EventSupport], {$className:"dorado.widget.tree.Node", _visibleChildNodeCount:0, ATTRIBUTES:{tree:{readOnly:true}, parent:{readOnly:true}, nodes:{setter:function (p, nodes) {
    this.addNodes(nodes);
}}, firstNode:{readOnly:true, getter:function () {
    return this._expanded ? this._nodes.get(0) : null;
}}, label:{}, icon:{}, iconClass:{}, expandedIcon:{}, expandedIconClass:{}, checkable:{}, checked:{defaultValue:false, setter:function (p, checked) {
    if (this._checked === checked) {
        return;
    }
    var tree = this._tree, arg = {node:this, processDefault:true};
    tree.fireEvent("beforeNodeCheckedChange", tree, arg);
    if (!arg.processDefault) {
        return;
    }
    this._checked = checked;
    tree.fireEvent("onNodeCheckedChange", tree, arg);
    if (this.get("checkable")) {
        this._nodeCheckedChanged(this._checked, true, true);
    }
}}, autoCheckChildren:{defaultValue:true}, tip:{}, data:{}, hasChild:{getter:function () {
    return (this._nodes.size > 0) || this._hasChild;
}}, expanded:{}, expanding:{readOnly:true}, visible:{readOnly:true, getter:function () {
    if (!this._tree) {
        return false;
    }
    var parent = this._parent;
    while (parent) {
        if (!parent._expanded) {
            return false;
        }
        parent = parent._parent;
    }
    return true;
}}, userData:{}, level:{readOnly:true, getter:function () {
    var n = this, level = -1;
    while (n) {
        level++;
        n = n._parent;
    }
    return level;
}}}, constructor:function (config) {
    this._id = dorado.Core.newId();
    this._nodes = new dorado.widget.tree.NodeList(this, dorado._GET_ID);
    $invokeSuper.call(this, arguments);
    if (config) {
        if (config.constructor == String) {
            this._label = config;
        } else {
            this.set(config);
        }
    }
}, _setTree:function (tree) {
    if (this._tree != tree) {
        if (this._tree != null && this._tree.onNodeDetached) {
            this._tree.onNodeDetached(this);
        }
        this._tree = tree;
        if (tree != null && tree.onNodeAttached) {
            tree.onNodeAttached(this);
        }
        this._nodes.each(function (child) {
            child._setTree(tree);
        });
    }
}, _changeVisibleChildNodeCount:function (diff) {
    if (isNaN(diff)) {
        return;
    }
    this._visibleChildNodeCount += diff;
    if (this._expanded) {
        this._timestamp = dorado.Core.getTimestamp();
    }
    var n = this, p = n._parent;
    while (p && n._expanded) {
        p._visibleChildNodeCount += diff;
        n = p, p = p._parent;
    }
}, doSet:function (attr, value) {
    $invokeSuper.call(this, arguments);
    var def = this.ATTRIBUTES[attr];
    if (def && !def.skipRefresh) {
        this._timestamp = dorado.Core.getTimestamp();
        this.refresh();
    }
}, getTimestamp:function () {
    return this._timestamp;
}, _nodeCheckedChanged:function (checked, processChildren, processParent, ignoreThis) {
    var tree = this._tree;
    if (processChildren) {
        if (!tree._autoChecking) {
            tree._autoCheckingChildren = true;
        }
        if (tree._autoCheckingChildren && this._autoCheckChildren) {
            tree._autoCheckingParent = false;
            tree._autoChecking = true;
            this._nodes.each(function (child) {
                if (child.get("checkable")) {
                    child.set("checked", checked);
                }
            });
            tree._autoChecking = false;
        }
    }
    if (processParent) {
        var parent = this._parent;
        if (!tree._autoChecking) {
            tree._autoCheckingParent = true;
        }
        if (tree._autoCheckingParent && parent && parent.get("checkable") && parent._autoCheckChildren) {
            tree._autoCheckingChildren = false;
            var checkedCount = 0, checkableCount = 0, self = this;
            parent._nodes.each(function (child) {
                if (child == self && ignoreThis) {
                    return;
                }
                if (child.get("checkable")) {
                    checkableCount++;
                    if (child.get("checked") === true) {
                        checkedCount++;
                    }
                }
            });
            if (checkableCount) {
                tree._autoChecking = true;
                parent.set("checked", (checkedCount == 0) ? false : ((checkedCount == checkableCount) ? true : null));
                tree._autoChecking = false;
            }
        }
    }
}, refresh:function () {
    var tree = this._tree;
    if (tree && tree._rendered && tree._attached && tree._ignoreRefresh < 1 && tree._autoRefreshLock < 1) {
        dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", function () {
            tree.refreshNode(this);
        }, 50);
    }
}, createChildNode:function (config) {
    return new dorado.widget.tree.Node(config);
}, addNode:function (node, insertMode, refData) {
    if (node instanceof dorado.widget.tree.Node) {
        this._nodes.insert(node, insertMode, refData);
    } else {
        node = this.createChildNode(node);
        this._nodes.insert(node, insertMode, refData);
    }
    if (node.get("checkable")) {
        node._nodeCheckedChanged(node.get("checked"), false, true);
    }
    return node;
}, addNodes:function (nodeConfigs) {
    for (var i = 0; i < nodeConfigs.length; i++) {
        this.addNode(nodeConfigs[i]);
    }
}, remove:function () {
    if (this._parent) {
        if (this.get("checkable")) {
            this._nodeCheckedChanged(false, false, true, true);
        }
        this._parent._nodes.remove(this);
    }
}, clearChildren:function () {
    this._nodes.clear();
}, _expand:function (callback) {
    this._expanded = true;
    this._expanding = false;
    this._timestamp = dorado.Core.getTimestamp();
    if (this._parent) {
        this._parent._changeVisibleChildNodeCount(this._visibleChildNodeCount);
    }
    this._tree._nodeExpanded(this, callback);
    this._hasExpanded = true;
}, doExpand:function (callback) {
    this._expand(callback);
}, doExpandAsync:function (callback) {
    this._expand(callback);
}, expand:function (callback) {
    if (this._expanded || !this._tree) {
        $callback(callback);
        return;
    }
    var tree = this._tree;
    var eventArg = {async:false, node:this, processDefault:true};
    tree.fireEvent("beforeExpand", tree, eventArg);
    if (!eventArg.processDefault) {
        return;
    }
    if (this.doExpand) {
        this.doExpand(callback);
    }
    tree.fireEvent("onExpand", tree, eventArg);
}, expandAsync:function (callback) {
    if (this._expanded || !this._tree) {
        $callback(callback);
        return;
    }
    var tree = this._tree, self = this, called = false;
    var callDefault = function (success, result) {
        if (called) {
            return;
        }
        called = true;
        self._expanding = false;
        if (success === false) {
            $callback(callback, success, result);
        } else {
            self.doExpandAsync({callback:function (success, result) {
                $callback(callback, success, result);
                delete eventArg.callback;
                tree.fireEvent("onExpand", tree, eventArg);
            }});
        }
    };
    this._expanding = true;
    this._timestamp = dorado.Core.getTimestamp();
    tree.refreshNode(this);
    var eventArg = {async:true, node:this, callDefault:callDefault, processDefault:true};
    if (tree.getListenerCount("beforeExpand")) {
        tree.fireEvent("beforeExpand", tree, eventArg);
        if (!eventArg.processDefault) {
            this._expanding = false;
            this._timestamp = dorado.Core.getTimestamp();
            tree.refreshNode(this);
        }
    } else {
        callDefault();
    }
}, doCollapse:function () {
    this._expanded = false;
    this._timestamp = dorado.Core.getTimestamp();
    if (this._parent) {
        this._parent._changeVisibleChildNodeCount(-this._visibleChildNodeCount);
    }
    var self = this;
    this._tree._nodeCollapsed(this);
}, collapse:function () {
    if (!this._expanded) {
        return;
    }
    var tree = this._tree;
    var eventArg = {node:this, processDefault:true};
    if (tree) {
        tree.fireEvent("beforeCollapse", tree, eventArg);
    }
    if (!eventArg.processDefault) {
        return;
    }
    this.doCollapse();
    if (tree) {
        tree.fireEvent("onCollapse", tree, eventArg);
    }
}, highlight:function (options, speed) {
    if (this._tree) {
        this._tree.highlightItem(this, options, speed);
    }
}});
dorado.widget.tree.DataNode = $extend(dorado.widget.tree.Node, {$className:"dorado.widget.tree.DataNode", ATTRIBUTES:{data:{setter:function (p, data) {
    if (!(data instanceof dorado.Entity)) {
        data = new dorado.Entity(data);
    }
    this._data = data;
}}}, createChildNode:function (config) {
    return new dorado.widget.tree.DataNode(config);
}, getTimestamp:function () {
    return ((this._data) ? this._data.timestamp : 0) + this._timestamp;
}, _getEntityProperty:function (entity, property) {
    if (!entity || !property) {
        return null;
    }
    return (entity instanceof dorado.Entity) ? entity.get(property) : entity[property];
}});
dorado.widget.tree.TreeNodeIterator = $extend(dorado.util.Iterator, {$className:"dorado.widget.tree.TreeNodeIterator", constructor:function (root, options) {
    var nextIndex = (options && options.nextIndex > 0) ? options.nextIndex : 0;
    var includeInvisibleNodes = this.includeInvisibleNodes = options ? !!options.includeInvisibleNodes : false;
    this._iterators = [root._nodes.iterator()];
    if (nextIndex > 0) {
        var skiped = 0, it = this._iterators.peek();
        while (skiped < nextIndex) {
            var node = it.next(), processChildren = false;
            if (node) {
                var tmpSkiped = skiped + ((node._expanded || includeInvisibleNodes) ? (1 + node._visibleChildNodeCount) : 1);
                if (tmpSkiped < nextIndex) {
                    skiped = tmpSkiped;
                } else {
                    if (tmpSkiped == nextIndex) {
                        while ((node._expanded || includeInvisibleNodes) && node._nodes.size > 0) {
                            it = node._nodes.iterator();
                            it.last();
                            this._iterators.push(it);
                            node = node._nodes.get(node._nodes.size - 1);
                        }
                        break;
                    } else {
                        processChildren = true;
                        skiped++;
                    }
                }
            } else {
                processChildren = true;
            }
            if (processChildren) {
                var node = it.current();
                if (!node || (!node._expanded && !includeInvisibleNodes) || node._nodes.size == 0) {
                    break;
                }
                it = node._nodes.iterator();
                this._iterators.push(it);
            }
        }
    }
}, _findLastSubNode:function (node) {
    var its = this._iterators;
    while ((node._expanded || this.includeInvisibleNodes) && node._nodes.size > 0) {
        var it = node._nodes.iterator();
        it.last();
        its.push(it);
        node = it.previous();
    }
    return node;
}, first:function () {
    var its = this._iterators;
    its.splice(1, its.length - 1);
    its[0].first();
}, last:function () {
    var its = this._iterators;
    its.splice(1, its.length - 1);
    its[0].last();
    var node = its[0].previous();
    if (node) {
        this._findLastSubNode(node);
        its.peek().last();
    }
}, hasPrevious:function () {
    return (this._iterators.length > 1 || this._iterators[0].hasPrevious());
}, hasNext:function () {
    var its = this._iterators;
    var current = its.peek().current();
    if (current && current._nodes.size > 0 && (current._expanded || this.includeInvisibleNodes)) {
        return true;
    }
    for (var i = its.length - 1; i >= 0; i--) {
        if (its[i].hasNext()) {
            return true;
        }
    }
    return false;
}, previous:function () {
    var its = this._iterators;
    var node = its.peek().previous();
    if (node) {
        node = this._findLastSubNode(node);
    } else {
        if (its.length > 1) {
            its.pop();
            node = its.peek().current();
        } else {
            node = null;
        }
    }
    return node;
}, next:function () {
    var node, current, its = this._iterators;
    current = its.peek().current();
    if (current && current._nodes.size > 0 && (current._expanded || this.includeInvisibleNodes)) {
        its.push(current._nodes.iterator());
    }
    for (var i = its.length - 1; i >= 0; i--) {
        node = its[i].next();
        if (node) {
            break;
        }
        if (its.length > 1) {
            its.pop();
        }
    }
    return node;
}, createBookmark:function () {
    var subBookmarks = [];
    for (var i = 0; i < this._iterators.length; i++) {
        subBookmarks.push(this._iterators[i].createBookmark());
    }
    return {subIterators:this._iterators.slice(0), subBookmarks:subBookmarks};
}, restoreBookmark:function (bookmark) {
    this._iterators = bookmark.subIterators;
    for (var i = 0; i < this._iterators.length; i++) {
        this._iterators[i].restoreBookmark(bookmark.subBookmarks[i]);
    }
}});
dorado.widget.tree.TreeNodeIterator.getNodeIndex = function (node) {
    var index = -1, p = node._parent;
    while (p) {
        if (!p._expanded && !this.includeInvisibleNodes) {
            return -1;
        }
        var it = p._nodes.iterator();
        index++;
        while (it.hasNext()) {
            var n = it.next();
            if (n == node) {
                break;
            }
            index++;
            if (n._expanded || this.includeInvisibleNodes) {
                index += n._visibleChildNodeCount;
            }
        }
        node = p;
        p = node._parent;
    }
    return index;
};
dorado.widget.tree.ItemModel = $extend(dorado.widget.list.ItemModel, {$className:"dorado.widget.tree.ItemModel", constructor:function () {
    this._itemMap = {};
    $invokeSuper.call(this, arguments);
}, onNodeAttached:function (node) {
    this._itemMap[node._id] = node;
}, onNodeDetached:function (node) {
    delete this._itemMap[node._id];
}, iterator:function (startIndex) {
    var index = startIndex;
    if (index === undefined) {
        index = this._startIndex || 0;
    }
    var it = new dorado.widget.tree.TreeNodeIterator(this._root, {nextIndex:index});
    return it;
}, getItems:function () {
    return this._root;
}, setItems:function (root) {
    this._root = root;
}, getItemCount:function () {
    var root = this._root;
    return (root._expanded) ? root._visibleChildNodeCount : 0;
}, getItemAt:function (index) {
    return new dorado.widget.tree.TreeNodeIterator(this._root, {nextIndex:index}).next();
}, getItemIndex:dorado.widget.tree.TreeNodeIterator.getNodeIndex, getItemId:function (node) {
    return node._id;
}, getItemById:function (itemId) {
    return this._itemMap[itemId];
}});
dorado.widget.tree.TreeNodeRenderer = $extend(dorado.Renderer, {createIconDom:function (tree) {
    var icon = document.createElement("LABEL");
    icon.className = "icon";
    return icon;
}, getLabel:function (node) {
    return node.get("label");
}, renderLabel:function (labelDom, label, node) {
    var tree = node._tree, arg = {dom:labelDom, label:label, node:node, processDefault:(tree.getListenerCount("onRenderNode") == 0)};
    if (tree) {
        tree.fireEvent("onRenderNode", tree, arg);
    }
    if (arg.processDefault) {
        labelDom.innerText = label;
        labelDom.title = node.get("tip") || "";
    }
}, createCheckboxDom:function (tree) {
    return new dorado.widget.CheckBox({iconOnly:true, onValueChange:function (self, arg) {
        var row = tree.findItemDomByEvent(self.getDom());
        var node = $fly(row).data("item");
        node.set("checked", self.get("checked"));
        self.set("checked", node.get("checked"));
    }});
}, doRender:function (cell, node) {
    var tree = node._tree;
    cell.style.paddingLeft = ((node.get("level") - 1) * tree._indent) + "px";
    var cls = ["collapse-button", "expand-button"], buttonDom = cell.firstChild, $buttonDom = jQuery(buttonDom);
    if (node.get("hasChild")) {
        if (node._expanded) {
            cls.reverse();
        }
        $buttonDom.removeClass(cls[0]).addClass(cls[1]);
    } else {
        $buttonDom.removeClass(cls[0]).removeClass(cls[1]);
    }
    $buttonDom.toggleClass("button-expanding", !!node._expanding);
    if (!cell.lastChild) {
        eval("debugger");
    }
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
    } else {
        if (icon || iconClass) {
            cell.insertBefore(this.createIconDom(tree), cell.childNodes[1]);
            cell.doradoHasIcon = true;
        }
    }
    var iconDom = cell.childNodes[1];
    if (icon) {
        $DomUtils.setBackgroundImage(iconDom, icon);
    } else {
        if (iconClass) {
            $fly(iconDom).addClass(iconClass);
        }
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
    } else {
        if (checkable) {
            checkbox = this.createCheckboxDom(tree), checkboxIndex = cell.doradoHasIcon ? 2 : 1;
            checkbox.render(cell, cell.childNodes[checkboxIndex]);
            tree.registerInnerControl(checkbox);
            cell.subCheckboxId = checkbox._uniqueId;
        }
    }
    if (checkable && checkbox) {
        checkbox.set("checked", node.get("checked"));
        checkbox.refresh();
    }
}, render:function (row, node) {
    this.doRender(row.firstChild, node);
}});
dorado.widget.AbstractTree = $extend(dorado.widget.RowList, {$className:"dorado.widget.AbstractTree", selectable:false, ATTRIBUTES:{root:{readOnly:true}, nodes:{setter:function (p, nodes) {
    this._root.addNodes(nodes);
}, getter:function () {
    return this._root._nodes;
}}, currentNode:{skipRefresh:true, setter:function (p, node) {
    if (this._currentNode == node) {
        return;
    }
    if (node == this._root) {
        node = null;
    }
    var eventArg = {oldCurrent:this._currentNode, newCurrent:node, processDefault:true};
    this.fireEvent("beforeCurrentChange", this, eventArg);
    if (!eventArg.processDefault) {
        return;
    }
    this._currentNode = node;
    this.fireEvent("onCurrentChange", this, eventArg);
    if (this._rendered) {
        $setTimeout(this, function () {
            var row = node ? this._itemDomMap[node._id] : null;
            this.setCurrentRow(row);
            if (row) {
                this.scrollCurrentIntoView();
            }
        }, 50);
    }
}}, indent:{defaultValue:16}, expandingMode:{defaultValue:"async", skipRefresh:true}, dropMode:{defaultValue:"onItem", setter:function (p, v) {
    if (v == "insertItems") {
        v = "onOrInsertItems";
    }
    this._dropMode = v;
}}, expandingAnimated:{defaultValue:true, skipRefresh:true}, defaultIcon:{}, defaultIconClass:{}, defaultExpandedIcon:{}, defaultExpandedIconClass:{}, firstNode:{readOnly:true, getter:function () {
    return this._root.get("firstNode");
}}}, EVENTS:{beforeExpand:{disallowMultiListeners:true}, onExpand:{}, beforeCollapse:{}, onCollapse:{}, onNodeAttached:{}, onNodeDetached:{}, beforeCurrentChange:{}, onCurrentChange:{}, onRenderNode:{}, beforeNodeCheckedChange:{}, onNodeCheckedChange:{}}, constructor:function () {
    var root = this._root = this.createRootNode();
    root._setTree(this);
    root._expanded = true;
    this._autoRefreshLock = 0;
    $invokeSuper.call(this, arguments);
}, destroy:function () {
    if (this._scrollMode != "viewport") {
        this._root._setTree(null);
    }
    $invokeSuper.call(this, arguments);
}, createRootNode:function () {
    return new dorado.widget.tree.Node("<ROOT>");
}, createItemModel:function () {
    var im = new dorado.widget.tree.ItemModel();
    im.setItems(this._root);
    return im;
}, createItemDom:function (node) {
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
}, createItemDomDetail:function (row, node) {
    var cell = $DomUtils.xCreateElement({tagName:"TD", className:"d-tree-node", content:[{tagName:"LABEL", doradoType:"tree-button", className:"button"}, {tagName:"LABEL", className:"label", whiteSpace:"no-wrap"}]});
    var buttonDom = cell.firstChild, $buttonDom = jQuery(buttonDom), self = this;
    $buttonDom.mousedown(function () {
        return false;
    }).click(function () {
        var row = buttonDom.parentNode.parentNode;
        var node = $fly(row).data("item");
        if (node.get("hasChild")) {
            if (node._expanded) {
                node.collapse();
            } else {
                if (self._expandingMode == "sync") {
                    node.expand();
                } else {
                    node.expandAsync();
                }
            }
            $buttonDom.removeClass("expand-button-hover collapse-button-hover");
        }
        return false;
    }).hover(function () {
        if ($buttonDom.hasClass("expand-button")) {
            $buttonDom.addClass("expand-button-hover");
        }
        if ($buttonDom.hasClass("collapse-button")) {
            $buttonDom.addClass("collapse-button-hover");
        }
    }, function () {
        $buttonDom.removeClass("expand-button-hover collapse-button-hover");
    });
    row.appendChild(cell);
}, doRefreshItemDomData:function (row, node) {
    (this._renderer || $singleton(dorado.widget.tree.TreeNodeRenderer)).render(row, node);
}, getItemTimestamp:function (node) {
    return node.getTimestamp();
}, onNodeAttached:function (node) {
    if (this._itemModel) {
        this._itemModel.onNodeAttached(node);
        this.fireEvent("onNodeAttached", this, {node:node});
    }
}, onNodeDetached:function (node) {
    if (this._itemModel) {
        this._itemModel.onNodeDetached(node);
        this.fireEvent("onNodeDetached", this, {node:node});
    }
}, refreshItemDoms:function (tbody, reverse, fn) {
    if (this._duringAnimation) {
        return;
    }
    return $invokeSuper.call(this, arguments);
}, refreshNode:function (node) {
    if (node) {
        dorado.Toolkits.cancelDelayedAction(node, "$refreshDelayTimerId");
    }
    if (this._autoRefreshLock > 0 || !this._itemDomMap) {
        return;
    }
    var row = this._itemDomMap[node._id];
    if (row) {
        this.refreshItemDomData(row, node);
    }
}, disableAutoRefresh:function () {
    this._autoRefreshLock++;
}, enableAutoRefresh:function () {
    this._autoRefreshLock--;
    if (this._autoRefreshLock < 0) {
        this._autoRefreshLock = 0;
    }
}, getNodeByEvent:function (event) {
    var row = this.findItemDomByEvent(event);
    return (row) ? $fly(row).data("item") : null;
}, getCheckedNodes:function () {
    var it = new dorado.widget.tree.TreeNodeIterator(this._root, {includeInvisibleNodes:true}), nodes = [];
    while (it.hasNext()) {
        var node = it.next();
        if (node.get("checked")) {
            nodes.push(node);
        }
    }
    return nodes;
}, highlightItem:function (node, options, speed) {
    if (node._tree != this) {
        return;
    }
    var row = this._itemDomMap[node._id];
    if (row) {
        $fly(row.firstChild).effect("highlight", options || {color:"#FFFF80"}, speed || 1500);
    } else {
        if (!node._disableDelayHighlight) {
            var self = this;
            setTimeout(function () {
                node._disableDelayHighlight = true;
                self.highlightItem(node, options, speed);
                node._disableDelayHighlight = false;
            }, 100);
        }
    }
}, initDraggingIndicator:function (indicator, draggingInfo, evt) {
    if (this._dragMode != "control") {
        var itemDom = draggingInfo.get("element");
        if (itemDom) {
            var cell = itemDom.firstChild;
            var contentDom = $DomUtils.xCreateElement({tagName:"div", className:"d-list-dragging-item " + cell.className});
            var children = [];
            for (var i = 1; i < cell.childNodes.length; i++) {
                var child = cell.childNodes[i];
                children.push(child);
            }
            $fly(children).clone().appendTo(contentDom);
            indicator.set("content", contentDom);
        }
    }
}, doOnDraggingSourceMove:function (draggingInfo, evt, targetObject, insertMode, refObject, itemDom) {
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
}, showDraggingInsertIndicator:function (draggingInfo, insertMode, itemDom) {
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
        if (dom.clientWidth < width) {
            width = dom.clientWidth;
        }
        width -= left;
        var top = (insertMode == "before") ? itemDom.offsetTop : (itemDom.offsetTop + itemDom.offsetHeight);
        $fly(insertIndicator).width(width).height(2).left(left).top(top - 1).show();
        dom.appendChild(insertIndicator);
    } else {
        $invokeSuper.call(this, arguments);
    }
}, processItemDrop:function (draggingInfo, evt) {
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
}});
(function () {
    var originJQueryFxUpdate = jQuery.fx.prototype.update;
    jQuery.fx.prototype.update = function () {
        originJQueryFxUpdate.apply(this, arguments);
        if (this.elem.nodeName.toUpperCase() == "TR") {
            this.elem.style.display = "";
        }
    };
    dorado.widget.Tree = $extend(dorado.widget.AbstractTree, {$className:"dorado.widget.Tree", ATTRIBUTES:{className:{defaultValue:"d-tree"}, width:{defaultValue:200}, rowHeight:{defaultValue:22}}, getCurrentItem:function () {
        if (this._currentIndex >= 0) {
            return this._itemModel.getItemAt(this._currentIndex);
        }
    }, getCurrentItemId:function () {
        return (this._currentNode) ? this._currentNode._id : null;
    }, setCurrentItemDom:function (row) {
        this.set("currentNode", row ? $fly(row).data("item") : null);
    }, doRefreshItemDomData:function (row, node) {
        $invokeSuper.call(this, arguments);
        if (this._scrollMode != "viewport") {
            node._dom = row;
        }
    }, removeItemDom:function (row) {
        if (this._scrollMode != "viewport") {
            if (row.parentNode) {
                row.parentNode.removeChild(row);
            }
        } else {
            var item = $fly(row).data("item");
            if (item) {
                delete item._dom;
            }
            $invokeSuper.call(this, arguments);
        }
    }, createExpandingIndicator:function () {
        var row = this._expandingIndicator;
        if (row == null) {
            this._expandingIndicator = row = $DomUtils.xCreateElement({tagName:"TR", className:"d-tree-expanding-placeholder", content:"^TD"});
        }
        return row;
    }, _insertChildNodes:function (node, row, animated, callback) {
        var tbody = this._dataTBody, refRow = row ? row.nextSibling : null;
        var it = new dorado.widget.tree.TreeNodeIterator(node), count = 0, bottom = -1;
        while (it.hasNext()) {
            var child = it.next();
            var newRow = this.createItemDom(child);
            if (animated && !this._duringAnimation) {
                newRow.style.display = "none";
            }
            (refRow) ? tbody.insertBefore(newRow, refRow) : tbody.appendChild(newRow);
            if (count > 10 && !this._shouldSkipRender) {
                if (bottom < 0) {
                    bottom = this._container.scrollTop + this._container.clientHeight;
                }
                if (newRow.offsetTop + newRow.offsetHeight > bottom) {
                    this._shouldSkipRender = true;
                }
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
            $fly(indicator).animate({"height":"+=" + (self._rowHeight * count)}, 200, "swing", function () {
                $fly(indicator).remove();
                it = new dorado.widget.tree.TreeNodeIterator(node);
                while (it.hasNext()) {
                    var child = it.next(), childRow = self._itemDomMap[child._id];
                    if (childRow) {
                        childRow.style.display = "";
                    }
                }
                self.notifySizeChange();
                $callback(callback, true, node);
                self._duringAnimation = false;
            });
        } else {
            this.notifySizeChange();
            $callback(callback, true, node);
        }
    }, _removeChildNodes:function (node, animated, callback) {
        var it = new dorado.widget.tree.TreeNodeIterator(node);
        var rowsToRemove = [];
        while (it.hasNext()) {
            var child = it.next();
            if (child == this._currentNode) {
                this.set("currentNode", node);
            }
            var childRow = this._itemDomMap[child._id];
            if (childRow) {
                rowsToRemove.push(childRow);
            }
        }
        if (rowsToRemove.length) {
            if (animated && !this._duringAnimation) {
                var indicator = this.createExpandingIndicator();
                indicator.style.height = (this._rowHeight * rowsToRemove.length) + "px";
                this._dataTBody.insertBefore(indicator, rowsToRemove[0]);
            }
            for (var i = 0; i < rowsToRemove.length; i++) {
                var row = rowsToRemove[i];
                this.removeItemDom(row);
            }
            if (animated && !this._duringAnimation) {
                var self = this;
                this._duringAnimation = true;
                $fly(indicator).animate({"height":0}, 200, "swing", function () {
                    $fly(indicator).remove();
                    self.notifySizeChange();
                    if (callback) {
                        $callback(callback, true, node);
                    }
                    self._duringAnimation = false;
                });
            }
        } else {
            this.notifySizeChange();
            if (callback) {
                $callback(callback, true, node);
            }
        }
    }, _refreshAndScroll:function (node, mode, parentNode, nodeIndex) {
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
                        if (n._expanded) {
                            index += n._visibleChildNodeCount;
                        }
                    }
                } else {
                    index = itemModel.getItemIndex(node);
                }
                if (index >= 0) {
                    var startIndex = itemModel.getStartIndex();
                    switch (mode) {
                      case "insert":
                        if (index < startIndex) {
                            itemModel.setStartIndex(startIndex + node._visibleChildNodeCount + 1);
                            shouldScroll = true;
                        }
                        break;
                      case "remove":
                        if ((index + node._visibleChildNodeCount + 1) < startIndex) {
                            itemModel.setStartIndex(startIndex - node._visibleChildNodeCount - 1);
                            shouldScroll = true;
                        } else {
                            if (index < startIndex) {
                                itemModel.setStartIndex(index);
                                shouldScroll = true;
                            }
                        }
                        break;
                      case "expand":
                        if (index < startIndex) {
                            itemModel.setStartIndex(startIndex + node._visibleChildNodeCount);
                            shouldScroll = true;
                        }
                        break;
                      case "collapse":
                        if ((index + node._visibleChildNodeCount) < startIndex) {
                            itemModel.setStartIndex(startIndex - node._visibleChildNodeCount);
                            shouldScroll = true;
                        } else {
                            if (index < startIndex) {
                                itemModel.setStartIndex(index + 1);
                                shouldScroll = true;
                            }
                        }
                        break;
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
              case "insert":
                refreshParent = parentNode._nodes.size == 1;
                break;
              case "remove":
                refreshParent = !parentNode.get("hasChild");
                break;
            }
            if (refreshParent) {
                var parentRow = this._itemDomMap[parentNode._id];
                if (parentRow) {
                    this._ignoreItemTimestamp = true;
                    this.refreshItemDomData(parentRow, parentNode);
                }
            }
        }
    }, _getExpandingAnimated:function () {
        return this._expandingAnimated;
    }, _nodeExpanded:function (node, callback) {
        if (!this._rendered || !this._attached || this._autoRefreshLock > 0) {
            return;
        }
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
            if (callback) {
                $callback(callback, true, node);
            }
        }
    }, _nodeCollapsed:function (node, callback) {
        if (!this._rendered || !this._attached || this._autoRefreshLock > 0) {
            return;
        }
        if (this._scrollMode != "viewport") {
            this._removeChildNodes(node, this._getExpandingAnimated(), callback);
            var row = this._itemDomMap[node._id];
            if (row) {
                this.refreshItemDomData(row, node);
            }
        } else {
            this._refreshAndScroll(node, "collapse", node._parent);
            if (callback) {
                $callback(callback, true, node);
            }
        }
    }, _nodeInserted:function (node) {
        if (!this._rendered || !this._attached || this._autoRefreshLock > 0) {
            return;
        }
        if (this._scrollMode != "viewport") {
            var parentNode = node._parent;
            if (parentNode._expanded) {
                var nodeIndex = this._itemModel.getItemIndex(node);
                if (nodeIndex >= 0) {
                    var originExpanded = node._expanded;
                    node._expanded = false;
                    try {
                        var it = new dorado.widget.tree.TreeNodeIterator(this._itemModel.getItems(), {nextIndex:nodeIndex});
                        it.next();
                        var nextsibling = it.next(), tbody = this._dataTBody, refRow;
                    }
                    finally {
                        node._expanded = originExpanded;
                    }
                    if (nextsibling) {
                        refRow = this._itemDomMap[nextsibling._id];
                    }
                    var newRow = this.createItemDom(node);
                    (refRow) ? tbody.insertBefore(newRow, refRow) : tbody.appendChild(newRow);
                    this.refreshItemDom(tbody, node, newRow.sectionRowIndex);
                    if (originExpanded) {
                        this._insertChildNodes(node, newRow);
                    }
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
    }, _nodeRemoved:function (node, parentNode, index) {
        if (!this._rendered || !this._attached || this._autoRefreshLock > 0) {
            return;
        }
        if (this._scrollMode != "viewport") {
            if (parentNode) {
                if (parentNode._expanded) {
                    if (node._expanded) {
                        this._removeChildNodes(node);
                    }
                    var row = this._itemDomMap[node._id];
                    if (row) {
                        this.removeItemDom(row);
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
    }});
})();
dorado.widget.tree.DataBindingNode = $extend(dorado.widget.tree.DataNode, {$className:"dorado.widget.tree.DataBindingNode", ATTRIBUTES:{bindingConfig:{writeOnce:true}, childBindingConfigs:{writeOnce:true, setter:function (p, v) {
    this._bindingConfig.childBindingConfigs = v;
}, getter:function () {
    return this._bindingConfig.childBindingConfigs;
}}, hasChild:{getter:function () {
    function ifHasChild(entity, property) {
        if (!property) {
            return false;
        }
        var hasChild = false, value = entity._data[property];
        if (entity instanceof dorado.Entity) {
            if (value === undefined) {
                if (entity.dataType) {
                    var pd = entity.dataType.getPropertyDef(property);
                    hasChild = (pd.getDataPipe && pd.getDataPipe(entity) != null);
                }
            } else {
                if (value) {
                    if (value.isDataPipeWrapper || typeof value == "object" && !(value instanceof Date)) {
                        hasChild = true;
                    }
                }
            }
        } else {
            value = entity[property];
            if (value && typeof value == "object" && !(value instanceof Date)) {
                hasChild = true;
            }
        }
        return hasChild;
    }
    if (this._nodes.size > 0) {
        return true;
    }
    if (this._hasChild != undefined) {
        return this._hasChild;
    }
    if (this._bindingConfig._hasChildProperty != undefined) {
        return this._getEntityProperty(this._data, this._bindingConfig._hasChildProperty);
    }
    var hasChild = false;
    if (this._data && (this._bindingConfig.recursive || this._bindingConfig.childBindingConfigs)) {
        var entity = this._data, value;
        if (entity instanceof dorado.Entity) {
            hasChild = ifHasChild(entity, this._bindingConfig.childrenProperty);
            if (!hasChild) {
                var childBindingConfigs = this._bindingConfig.childBindingConfigs;
                if (childBindingConfigs) {
                    for (var i = 0; i < childBindingConfigs.length; i++) {
                        hasChild = ifHasChild(entity, childBindingConfigs[i].childrenProperty);
                        if (hasChild) {
                            break;
                        }
                    }
                }
            }
        }
    }
    return hasChild;
}}, label:{getter:function () {
    if (this._label) {
        return this._label;
    }
    return this._getEntityProperty(this._data, this._bindingConfig.labelProperty);
}}, icon:{getter:function () {
    if (this._icon) {
        return this._icon;
    }
    var bg = this._bindingConfig;
    if (bg.icon) {
        return bg.icon;
    }
    return this._getEntityProperty(this._data, bg.iconProperty);
}}, iconClass:{getter:function () {
    if (this._iconClass) {
        return this._iconClass;
    }
    var bg = this._bindingConfig;
    if (bg.iconClass) {
        return bg.iconClass;
    }
    return this._getEntityProperty(this._data, bg.iconClassProperty);
}}, expanededIcon:{getter:function () {
    if (this._expanededIcon) {
        return this._expanededIcon;
    }
    var bg = this._bindingConfig;
    if (bg.expanededIcon) {
        return bg.expanededIcon;
    }
    return this._getEntityProperty(this._data, bg.expanededIconProperty);
}}, expanededIconClass:{getter:function () {
    if (this._expanededIconClass) {
        return this._expanededIconClass;
    }
    var bg = this._bindingConfig;
    if (bg.expanededIconClass) {
        return bg.expanededIconClass;
    }
    return this._getEntityProperty(this._data, bg.expanededIconClassProperty);
}}, checkable:{getter:function () {
    return this._checkable || this._bindingConfig.checkable;
}}, checked:{getter:function () {
    var bg = this._bindingConfig;
    if (bg.checkedProperty && this._data) {
        this._checked = this._getEntityProperty(this._data, bg.checkedProperty);
    }
    if (this._checked != undefined) {
        return this._checked;
    }
    return bg.checked;
}, setter:function (p, checked) {
    var currentChecked = this._checked, bg = this._bindingConfig;
    if (currentChecked == undefined) {
        currentChecked = bg.checked;
    }
    if (currentChecked === checked) {
        return;
    }
    $invokeSuper.call(this, arguments);
    var entity = this._data, property = bg.checkedProperty;
    if (entity && property) {
        (entity instanceof dorado.Entity) ? entity.set(property, checked) : entity[property] = checked;
    }
}}, tip:{getter:function () {
    if (this._tip) {
        return this._tip;
    }
    return this._getEntityProperty(this._data, this._bindingConfig.tipProperty);
}}}, _prepareChildren:function (callback) {
    function processBindingConfig(bindingConfig, entity, startIndex, processDefaultExpand) {
        function addNode(entity) {
            var eventArg = {data:entity, processDefault:true};
            tree.fireEvent("beforeDataNodeCreate", tree, eventArg);
            if (!eventArg.processDefault) {
                return;
            }
            var node = null, oldNode = null;
            if (startIndex < nodes.size) {
                node = nodes.get(startIndex);
                if (node._data != entity) {
                    oldNode = node;
                    node = null;
                }
            }
            if (!node) {
                node = new dorado.widget.tree.DataBindingNode({bindingConfig:bindingConfig, data:entity, tags:bindingConfig.tags});
                if (oldNode) {
                    nodes.replace(oldNode, node);
                } else {
                    nodes.insert(node);
                }
            } else {
                node._bindingConfig = bindingConfig;
                node._parent._changeVisibleChildNodeCount(1);
            }
            eventArg.node = node;
            tree.fireEvent("onDataNodeCreate", tree, eventArg);
            var expanded = expandedNodes[entity.entityId];
            if (expanded === true || node._expanded) {
                node._expanded = false;
                node.expandAsync();
            } else {
                if (processDefaultExpand) {
                    if (!bindingConfig.recursive) {
                        if (!bindingConfig.recursive && bindingConfig.expandLevel) {
                            node.expandAsync();
                        }
                    } else {
                        if (bindingConfig.expandLevel) {
                            var parentNode = node._parent, i = 0;
                            while (parentNode && parentNode._bindingConfig == bindingConfig) {
                                parentNode = parentNode._parent;
                                i++;
                            }
                            if (i < bindingConfig.expandLevel) {
                                node.expandAsync();
                            }
                        }
                    }
                }
            }
        }
        var tree = this._tree, nodes = this._nodes, expandedNodes = {};
        for (var it = nodes.iterator(); it.hasNext(); ) {
            var node = it.next();
            if (node._data) {
                expandedNodes[node._data.entityId] = !!node._expanded;
            }
        }
        if (entity instanceof dorado.EntityList) {
            for (var it = entity.iterator(); it.hasNext(); ) {
                addNode(it.next());
                startIndex++;
            }
            return startIndex;
        } else {
            if (entity instanceof dorado.Entity) {
                addNode(entity);
                startIndex++;
            }
        }
        return startIndex;
    }
    this._childrenPrepared = true;
    var bindingConfig = this._bindingConfig, tree = this._tree;
    var isRoot = (this == tree._root);
    var data = this._data;
    if (isRoot && tree) {
        this._data = data = tree.getBindingData({firstResultOnly:true, acceptAggregation:true});
    }
    if (!data) {
        this._clearChildren();
        return;
    }
    var asyncTasks = [], self = this;
    if (callback && data instanceof dorado.Entity) {
        var processPreload = (this._parent == tree._root);
        if (bindingConfig.recursive && !isRoot) {
            asyncTasks.push(function (callback) {
                if (processPreload) {
                    var preloadConfigs = dorado.widget.DataTree.bindingConfigToPreloadConfig(bindingConfig, 1);
                    if (preloadConfigs) {
                        dorado.DataProvider._SYS_PARAMETER = {preloadConfigs:preloadConfigs};
                    }
                }
                data.getAsync(bindingConfig.childrenProperty, callback);
                dorado.DataProvider._SYS_PARAMETER = null;
            });
        }
        if (bindingConfig.childBindingConfigs) {
            for (var i = 0; i < bindingConfig.childBindingConfigs.length; i++) {
                var childBindingConfig = bindingConfig.childBindingConfigs[i];
                var childrenProperty = childBindingConfig.childrenProperty;
                asyncTasks.push(function (callback) {
                    if (processPreload) {
                        var preloadConfigs = dorado.widget.DataTree.bindingConfigToPreloadConfig(childBindingConfig, 1);
                        if (preloadConfigs) {
                            dorado.DataProvider._SYS_PARAMETER = {preloadConfigs:preloadConfigs};
                        }
                    }
                    data.getAsync(childrenProperty, callback);
                    dorado.DataProvider._SYS_PARAMETER = null;
                });
            }
        }
    }
    $waitFor(asyncTasks, {callback:function (success, result) {
        var nodesTimestamp = 0, infos = [];
        if (data instanceof dorado.Entity) {
            if (bindingConfig.recursive) {
                var e = (isRoot) ? data : data.get(bindingConfig.childrenProperty, callback ? "auto" : "always");
                if (e) {
                    nodesTimestamp += e.timestamp;
                    infos.push({bindingConfig:bindingConfig, data:e});
                }
            }
            if (bindingConfig.childBindingConfigs) {
                for (var i = 0; i < bindingConfig.childBindingConfigs.length; i++) {
                    var childBindingConfig = bindingConfig.childBindingConfigs[i];
                    var e = data.get(childBindingConfig.childrenProperty, callback ? "auto" : "always");
                    if (!e) {
                        continue;
                    }
                    nodesTimestamp += e.timestamp;
                    infos.push({bindingConfig:childBindingConfig, data:e});
                }
            }
        } else {
            if (isRoot) {
                var childBindingConfigs = bindingConfig.childBindingConfigs;
                if (childBindingConfigs && childBindingConfigs.length == 1) {
                    nodesTimestamp += data.timestamp;
                    infos.push({bindingConfig:childBindingConfigs[0], data:data});
                }
            }
        }
        if (self._nodesTimestamp != nodesTimestamp) {
            self._nodesTimestamp = nodesTimestamp;
            self._visibleChildNodeCount = 0;
            var startIndex = 0;
            if (tree) {
                tree.disableAutoRefresh();
            }
            try {
                for (var i = 0; i < infos.length; i++) {
                    var info = infos[i];
                    startIndex = processBindingConfig.call(self, info.bindingConfig, info.data, startIndex, !self._hasExpanded);
                }
                var nodes = self._nodes;
                for (var i = nodes.size - 1; i > startIndex; i--) {
                    nodes.removeAt(i);
                }
                if (startIndex == 0) {
                    self._hasChild = false;
                }
            }
            finally {
                if (tree) {
                    tree.enableAutoRefresh();
                }
            }
        }
        if (callback) {
            $callback(callback, success, result);
        }
    }});
}, _clearChildren:function () {
    delete this._nodesTimestamp;
    this._childrenPrepared = false;
}, doExpand:function () {
    if (!this._childrenPrepared) {
        this._prepareChildren();
    }
    $invokeSuper.call(this, arguments);
}, doExpandAsync:function (callback) {
    if (!this._childrenPrepared) {
        var self = this, superClass = $getSuperClass();
        this._prepareChildren({callback:function (success, result) {
            if (!self._expanded) {
                superClass.prototype.doExpandAsync.call(self, callback);
            }
        }});
    } else {
        $invokeSuper.call(this, arguments);
    }
}, doCollapse:function () {
    $invokeSuper.call(this, arguments);
    this._clearChildren();
}, rebuildChildNodes:function () {
    var tree = this._tree;
    tree.disableBinding();
    var oldExpandingAnimated = tree._expandingAnimated;
    tree._expandingAnimated = false;
    try {
        var expanded = this._expanded;
        if (expanded) {
            this.doCollapse();
        }
        this.clearChildren();
        if (expanded) {
            this.doExpandAsync($scopify(this, function () {
                if (tree._currentNode && tree._currentNode._tree != tree) {
                    tree.set("currentNode", this);
                }
            }));
        }
    }
    finally {
        tree._expandingAnimated = true;
    }
    tree.enableBinding();
}});
dorado.widget.DataTree = $extend([dorado.widget.Tree, dorado.widget.DataControl], {$className:"dorado.widget.DataTree", ATTRIBUTES:{bindingConfigs:{writeBeforeReady:true, setter:function (p, bindingConfigs) {
    this._root.set("childBindingConfigs", bindingConfigs);
}, getter:function () {
    return this._root.get("childBindingConfigs");
}}, dataPathInterceptor:{writeBeforeReady:true}, currentEntity:{setter:function (p, currentEntity) {
    var node = currentEntity ? this._entityMap[currentEntity.entityId] : null;
    this.set("currentNode", node);
}, getter:function () {
    return (this._currentNode) ? this._currentNode._data : null;
}}}, EVENTS:{beforeDataNodeCreate:{}, onDataNodeCreate:{}}, constructor:function () {
    this._entityMap = {};
    $invokeSuper.call(this, arguments);
}, onReady:function () {
    if (this._dataPathInterceptor) {
        var self = this;
        dorado.DataPath.registerInterceptor(this._dataPathInterceptor, function (data) {
            return self.get("currentNode.data");
        }, function (dataType) {
            var entity = self.get("currentNode.data");
            return entity ? entity.dataType : dataType;
        });
        this.addListener("onCurrentChange", function () {
            self.disableBinding();
            self.get("dataSet").notifyObservers();
            self.enableBinding();
        });
    }
    $invokeSuper.call(this, arguments);
}, createRootNode:function () {
    return new dorado.widget.tree.DataBindingNode({label:"<ROOT>", bindingConfig:{}});
}, syncCurrentEntity:function () {
    var path = [];
    var entity = this.get("currentEntity");
    while (entity && entity.parent) {
        path.push({entityList:entity.parent, entity:entity});
        entity = entity.parent.parent;
    }
    jQuery.each(path.reverse(), function (i, section) {
        if (section.entityList.current != section.entity) {
            section.entityList.setCurrent(section.entity);
        }
    });
}, onNodeAttached:function (node) {
    $invokeSuper.call(this, arguments);
    if (node._data) {
        this._entityMap[node._data.entityId] = node;
    }
}, onNodeDetached:function (node) {
    $invokeSuper.call(this, arguments);
    if (node._data) {
        delete this._entityMap[node._data.entityId];
    }
}, refresh:function () {
    if (this._dataSet) {
        var shouldPrepareChildren = !this._root._childrenPrepared;
        if (!shouldPrepareChildren) {
            var data = this.getBindingData({firstResultOnly:true, acceptAggregation:true});
            if (this._data != data) {
                shouldPrepareChildren = true;
                this._data = data;
            }
        }
        if (shouldPrepareChildren) {
            this._root._prepareChildren(dorado._NULL_FUNCTION);
        }
    }
    $invokeSuper.call(this, arguments);
}, filterDataSetMessage:function (messageCode, arg) {
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
      case dorado.widget.DataSet.MESSAGE_DELETED:
      case dorado.widget.DataSet.MESSAGE_INSERTED:
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        return true;
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
        if (this._data) {
            if (dorado.DataUtil.isOwnerOf(this._data, arg.entityList)) {
                return true;
            }
        }
        return false;
      default:
        return false;
    }
}, processDataSetMessage:function (messageCode, arg, data) {
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
        this.refresh(true);
        break;
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        this.refreshNodeByEntity(arg.entity);
        break;
      case dorado.widget.DataSet.MESSAGE_DELETED:
        var node = this._entityMap[arg.entity.entityId];
        if (node) {
            node.remove();
        }
        break;
      case dorado.widget.DataSet.MESSAGE_INSERTED:
        var parentEntity = arg.entity.parent, parentEntityList;
        if (parentEntity) {
            if (parentEntity instanceof dorado.EntityList) {
                parentEntityList = parentEntity;
                parentEntity = parentEntity.parent;
            }
        }
        var parentNode;
        if (parentEntity instanceof dorado.Entity) {
            parentNode = this._entityMap[parentEntity.entityId];
        } else {
            if (parentEntityList == this._root._data) {
                parentNode = this._root;
            }
        }
        if (parentNode && parentNode._expanded) {
            if (this._scrollMode == "simple") {
                parentNode.rebuildChildNodes();
            } else {
                this.disableAutoRefresh();
                parentNode._prepareChildren();
                this.enableAutoRefresh();
                this.refresh(true);
            }
        }
        break;
    }
}, refreshNodeByEntity:function (entity) {
    var node = this._entityMap[entity.entityId];
    if (!node) {
        return;
    }
    this.refreshNode(node);
}, rebuildNodes:function () {
    delete this._root._data;
    this._root.rebuildChildNodes();
}, processItemDrop:function (draggingInfo, evt) {
    var object = draggingInfo.get("object");
    var targetObject = draggingInfo.get("targetObject");
    var insertMode = draggingInfo.get("insertMode");
    var refObject = draggingInfo.get("refObject");
    if (object instanceof dorado.widget.tree.DataBindingNode && targetObject instanceof dorado.widget.tree.DataBindingNode) {
        var sourceNode = object, targetNode = targetObject;
        var sourceEntity = sourceNode.get("data"), targetEntity = targetNode.get("data");
        var refNode, refEntity;
        if (refObject instanceof dorado.widget.tree.DataBindingNode) {
            refNode = refObject;
            refEntity = refNode.get("data");
        }
        var sourceBindingConfig = sourceNode.get("bindingConfig");
        var bindingConfig = targetNode.get("bindingConfig");
        var childBindingConfigs = targetNode.get("childBindingConfigs") || [];
        var childBindingConfig;
        if (sourceBindingConfig == bindingConfig || bindingConfig.recursive && childBindingConfigs.indexOf(sourceBindingConfig) >= 0) {
            childBindingConfig = sourceBindingConfig;
        } else {
            if (childBindingConfigs.length == 1 && !bindingConfig.recursive) {
                childBindingConfig = childBindingConfigs[0];
            } else {
                if (childBindingConfigs.length == 0 && bindingConfig.recursive) {
                    childBindingConfig = bindingConfig;
                }
            }
        }
        if (childBindingConfig) {
            var entityList;
            if (targetEntity instanceof dorado.EntityList) {
                entityList = targetEntity;
            } else {
                entityList = targetEntity.get(childBindingConfig.childrenProperty, "always");
            }
            if (entityList instanceof dorado.EntityList) {
                sourceEntity.remove();
                sourceEntity = entityList.createChild(sourceEntity.toJSON(), true);
                entityList.insert(sourceEntity, insertMode, refEntity);
                return true;
            }
        }
    }
    return false;
}});
dorado.widget.DataTree.bindingConfigToPreloadConfig = function (bindingConfig, level) {
    function toPreloadConfig(bindingConfig, level) {
        var preloadConfig = {property:bindingConfig.childrenProperty, recursiveLevel:bindingConfig.recursive ? (bindingConfig.expandLevel - level - 1) : 0};
        var childConfigs = getChildPreloadConfigs(bindingConfig, level);
        if (childConfigs) {
            preloadConfig.childPreloadConfigs = childConfigs;
        }
        return preloadConfig;
    }
    function getChildPreloadConfigs(bindingConfig, level) {
        var preloadConfigs = [];
        if (bindingConfig.childBindingConfigs) {
            for (var i = 0; i < bindingConfig.childBindingConfigs.length; i++) {
                var config = toPreloadConfig(bindingConfig.childBindingConfigs[i], 0);
                preloadConfigs.push(config);
            }
        }
        return (preloadConfigs.length) ? preloadConfigs : null;
    }
    level = level || 0;
    var preloadConfigs = [];
    if (bindingConfig.recursive || bindingConfig.expandLevel > 0) {
        if (bindingConfig.recursive) {
            if (bindingConfig.expandLevel > level) {
                var config = toPreloadConfig(bindingConfig, level);
                preloadConfigs.push(config);
            }
        }
        var childConfigs = getChildPreloadConfigs(bindingConfig, level);
        if (childConfigs) {
            preloadConfigs = preloadConfigs.concat(childConfigs);
        }
    }
    return preloadConfigs;
};

