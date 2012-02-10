
(function () {
    dorado.widget.blockview = {};
    dorado.widget.blockview.BlockItemModel = $extend(dorado.widget.list.ItemModel, {_lineSize:1, setLineSize:function (lineSize) {
        this._lineSize = lineSize;
    }, getLineCount:function () {
        return parseInt((this.getItemCount() - 1) / this._lineSize + 1);
    }, setScrollPos:function (scrollPos) {
        var itemCount = this.getItemCount(), lineCount = this.getLineCount();
        if (lineCount > 0) {
            this._startIndex = parseInt(((scrollPos / this._scrollSize) || 0) * lineCount) * this._lineSize;
        } else {
            this._startIndex = 0;
        }
    }});
    dorado.widget.blockview.DefaultBlockRenderer = $extend(dorado.Renderer, {render:function (dom, arg) {
        var data = arg.data;
        dom.innerText = dom.itemIndex + ": " + data;
    }});
    dorado.widget.blockview.ImageBlockRenderer = $extend(dorado.widget.blockview.DefaultBlockRenderer, {captionProperty:"caption", imageProperty:"image", tipProperty:"tip", blankImage:">skin>block/blank-image.png", padding:2, spacing:2, labelHeight:17, stretchImage:false, constructor:function (options) {
        dorado.Object.apply(this, options);
    }, getImageDom:function (dom) {
        var img = dom.firstChild;
        if (img == null) {
            img = $DomUtils.xCreateElement({tagName:"IMG", style:{position:"absolute"}});
            dom.appendChild(img);
        }
        return img;
    }, getLabelDom:function (dom) {
        var label = dom.lastChild;
        if (label == null || label.nodeName != "CENTER") {
            label = $DomUtils.xCreateElement({tagName:"CENTER", style:{position:"absolute"}, content:"^LABEL"});
            dom.appendChild(label);
        }
        return label;
    }, render:function (dom, arg) {
        var imageDom = this.getImageDom(dom), entity = arg.data;
        var labelHeight = 0;
        if (this.captionProperty && this.labelHeight > 0) {
            labelHeight = this.labelHeight;
            var labelDom = this.getLabelDom(dom);
            var label = (entity instanceof dorado.Entity) ? entity.get(this.captionProperty) : entity[this.captionProperty];
            $fly(labelDom).css({bottom:this.padding, width:dom.clientWidth - this.padding * 2, height:labelHeight});
            labelDom.firstChild.innerText = label;
        }
        if (this.tipProperty) {
            var tip = (entity instanceof dorado.Entity) ? entity.get(this.tipProperty) : entity[this.tipProperty];
            if (tip) {
                dorado.TipManager.initTip(dom, {text:tip});
            } else {
                dorado.TipManager.deleteTip(dom);
            }
        }
        var self = this;
        var maxWidth = dom.clientWidth - this.padding * 2;
        var maxHeight = dom.clientHeight - labelHeight - this.spacing - this.padding * 2;
        if (this.stretchImage) {
            $fly(imageDom).css({left:this.padding, top:this.padding, width:maxWidth, height:maxHeight});
        } else {
            $fly(imageDom).bind("load", function () {
                var left, top, width = imageDom.offsetWidth, height = imageDom.offsetHeight;
                if (width > maxWidth) {
                    height = Math.round(maxWidth * height / width);
                    width = maxWidth;
                }
                if (height > maxHeight) {
                    width = parseInt(maxHeight * width / height);
                    height = maxHeight;
                }
                left = Math.round((dom.clientWidth - width) / 2);
                top = Math.round((dom.clientHeight - labelHeight - self.spacing - height) / 2);
                $fly(imageDom).css({left:left, top:top, width:width, height:height});
            });
        }
        var image = (entity instanceof dorado.Entity) ? entity.get(this.imageProperty) : entity[this.imageProperty];
        $DomUtils.setImgSrc(imageDom, image || this.blankImage);
    }});
    dorado.widget.AbstractBlockView = $extend(dorado.widget.ViewPortList, {$className:"dorado.widget.AbstractBlockView", ATTRIBUTES:{className:{defaultValue:"d-block-view"}, blockLayout:{defaultValue:"vertical"}, lineSize:{}, blockWidth:{defaultValue:80}, blockHeight:{defaultValue:80}, fillLine:{}, renderer:{}, horiSpacing:{defaultValue:8}, vertSpacing:{defaultValue:8}, horiPadding:{defaultValue:8}, vertPadding:{defaultValue:8}, blockDecoratorSize:{defaultValue:4}}, EVENTS:{onRenderBlock:{}, onBlockMouseDown:{}, onBlockMouseUp:{}, onBlockClick:{}, onBlockDoubleClick:{}}, createItemModel:function () {
        return new dorado.widget.blockview.BlockItemModel();
    }, createDom:function () {
        var dom = $invokeSuper.call(this, arguments);
        var container = this._container = $DomUtils.xCreateElement({tagName:"DIV", style:{position:"relative", overflow:"hidden"}});
        dom.appendChild(container);
        $fly(dom).bind("scroll", $scopify(this, this.onScroll)).mouseover($scopify(this, function (evt) {
            if ($DomUtils.isDragging()) {
                return;
            }
            var blockDom = $DomUtils.findParent(evt.target, function (node) {
                return node.parentNode == container && $fly(node).hasClass("block");
            });
            this.setHoverBlock(blockDom);
        })).mouseleave($scopify(this, function (evt) {
            this.setHoverBlock(null);
        }));
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var lineSize = this._lineSize, blockWidth = this._blockWidth, blockHeight = this._blockHeight;
        if (this._blockLayout == "vertical") {
            if (!(lineSize > 0)) {
                lineSize = (dom.clientWidth - this._horiPadding * 2) / (this._blockWidth + this._horiSpacing / 2);
            }
        } else {
            if (!(lineSize > 0)) {
                lineSize = (dom.clientHeight - this._vertPadding * 2) / (this._blockHeight + this._vertSpacing / 2);
            }
        }
        this._realLineSize = lineSize = (lineSize < 1) ? 1 : parseInt(lineSize);
        this._itemModel.setLineSize(lineSize);
        this._lineCount = this._itemModel.getLineCount();
        var width, height, $container = jQuery(this._container);
        if (this._blockLayout == "vertical") {
            dom.style.overflowX = "hidden";
            dom.style.overflowY = "auto";
        } else {
            dom.style.overflowX = "auto";
            dom.style.overflowY = "hidden";
        }
        var pos = this._getContainerSize();
        if (this._blockLayout == "vertical") {
            $container.height(pos[1]).width(dom.clientWidth);
            if (this._lineSize > 0 && this._fillLine) {
                blockWidth = (dom.clientWidth - this._horiPadding * 2 - this._horiSpacing * (lineSize - 1)) / lineSize;
            }
        } else {
            $container.width(pos[0]).height(dom.clientHeight);
            if (this._lineSize > 0 && this._fillLine) {
                blockHeight = (dom.clientHeight - this._vertPadding * 2 - this._vertSpacing * (lineSize - 1)) / lineSize;
            }
        }
        this._realBlockWidth = blockWidth = (blockWidth < 1) ? 1 : parseInt(blockWidth);
        this._realBlockHeight = blockHeight = (blockHeight < 1) ? 1 : parseInt(blockHeight);
        var blockDom = this._container.firstChild;
        while (blockDom) {
            var next = blockDom.nextSibling;
            if (blockDom._isBlock) {
                this.removeItemDom(blockDom);
            }
            blockDom = next;
        }
        if (this._scrollMode == "viewport") {
            this.refreshViewPortContent(this._container);
        } else {
            this.refreshContent(this._container);
        }
    }, _getVisibleBlockRange:function () {
        var scroller = this._dom, blockSize, end;
        if (this._blockLayout == "vertical") {
            blockSize = this._blockHeight + this._vertSpacing;
            start = parseInt((scroller.scrollTop - this._vertPadding) / blockSize);
            end = start + Math.round((scroller.clientHeight - this._vertPadding) / blockSize + 0.5);
        } else {
            blockSize = this._blockWidth + this._horiSpacing;
            start = parseInt((scroller.scrollLeft - this._horiPadding) / blockSize);
            end = start + Math.round((scroller.clientWidth - this._horiPadding) / blockSize + 0.5);
        }
        return [start * this._realLineSize, end * this._realLineSize];
    }, refreshContent:function (container) {
        var fn;
        if (this._scrollMode == "lazyRender") {
            var count = this._getVisibleBlockRange()[1], i = 0;
            fn = function (row) {
                i++;
                return i < count;
            };
        }
        this.refreshItemDoms(container, false, fn);
        this._itemDomCount = this._itemModel.getItemCount();
    }, refreshViewPortContent:function (container) {
        var itemModel = this._itemModel, itemCount = itemModel.getItemCount(), scroller = this._dom;
        var topOrLeft, bottomOrRight;
        if (this._blockLayout == "vertical") {
            topOrLeft = scroller.scrollTop;
            bottomOrRight = scroller.scrollTop + scroller.clientHeight;
        } else {
            topOrLeft = scroller.scrollLeft;
            bottomOrRight = scroller.scrollLeft + scroller.clientWidth;
        }
        var itemDomCount, self = this;
        if (bottomOrRight > topOrLeft) {
            itemDomCount = this.refreshItemDoms(container, itemModel.isReverse(), function (itemDom) {
                if (itemDom.subIndex == self._realLineSize - 1) {
                    return ((self._blockLayout == "vertical") ? (itemDom.offsetTop + itemDom.offsetHeight + self._vertSpacing) : (itemDom.offsetLeft + itemDom.offsetWidth + self._horiSpacing)) < bottomOrRight;
                }
                return true;
            }, true);
        } else {
            itemDomCount = viewPortHeight = 0;
        }
        this._itemDomCount = itemDomCount;
        if (this._blockLayout == "vertical") {
            itemModel.setScrollSize(this._dom.clientHeight, container.clientHeight);
        } else {
            itemModel.setScrollSize(this._dom.clientWidth, container.clientWidth);
        }
    }, refreshItemDoms:function (itemDomContainer) {
        var currentDecorator = this._currentDecorator, hoverDecorator = this._hoverDecorator;
        if (currentDecorator) {
            itemDomContainer.removeChild(currentDecorator.getDom());
        }
        if (hoverDecorator) {
            itemDomContainer.removeChild(hoverDecorator.getDom());
        }
        $invokeSuper.call(this, arguments);
        if (currentDecorator) {
            itemDomContainer.insertBefore(currentDecorator.getDom(), itemDomContainer.firstChild);
        }
        if (hoverDecorator) {
            itemDomContainer.insertBefore(hoverDecorator.getDom(), itemDomContainer.firstChild);
        }
        var currentItemId = this.getCurrentItemId();
        if (currentItemId) {
            var itemDom = this._itemDomMap[currentItemId];
            if (itemDom) {
                this.setCurrentBlock(itemDom);
            }
        }
    }, _getBlockPos:function (index) {
        var lineIndex = parseInt(index / this._realLineSize);
        var subIndex = index % this._realLineSize;
        var left, top;
        if (this._blockLayout == "vertical") {
            left = this._horiPadding + (this._realBlockWidth + this._horiSpacing) * subIndex;
            top = this._vertPadding + (this._realBlockHeight + this._vertSpacing) * lineIndex;
        } else {
            left = this._horiPadding + (this._realBlockWidth + this._horiSpacing) * lineIndex;
            top = this._vertPadding + (this._realBlockHeight + this._vertSpacing) * subIndex;
        }
        return [left, top, lineIndex, subIndex];
    }, removeItemDom:function (blockDom) {
        $invokeSuper.call(this, arguments);
        this._itemDomCount--;
        this._lineCount = parseInt(this._itemDomCount / this._realLineSize);
    }, refreshItemDom:function (itemDomContainer, item, index, prepend) {
        var flag = prepend ? -1 : 1;
        if (index < 0) {
            flag = -flag;
        }
        index = (this._itemModel.getStartIndex() || 0) + index * flag;
        var itemId = this._itemModel.getItemId(item, index);
        var itemDom = this._itemDomMap[itemId];
        if (!itemDom) {
            itemDom = this.createItemDom(item);
            itemDomContainer.appendChild(itemDom);
            this._itemDomMap[itemId] = itemDom;
            itemDom.itemId = itemId;
        }
        itemDom.itemIndex = index;
        var pos = this._getBlockPos(index);
        itemDom.lineIndex = pos[2];
        itemDom.subIndex = pos[3];
        $fly(itemDom).data("item", item).css({left:pos[0], top:pos[1]}).outerWidth(this._realBlockWidth).outerHeight(this._realBlockHeight);
        this._itemDomCount++;
        this._lineCount = parseInt(this._itemDomCount / this._realLineSize);
        var processDefault = true;
        var eventArg = {dom:itemDom, data:item, processDefault:false};
        if (this.getListenerCount("onRenderBlock") && this.fireEvent("onRenderBlock", this, eventArg)) {
            processDefault = eventArg.processDefault;
        }
        if (processDefault) {
            this.refreshItemDomData(itemDom, item);
        }
        return itemDom;
    }, createItemDom:function (item) {
        var blockDom = $DomUtils.xCreateElement({tagName:"DIV", className:"block", style:{position:"absolute", overflow:"hidden"}});
        blockDom._isBlock = true;
        if (this._scrollMode == "lazyRender" && this._shouldSkipRender) {
            blockDom._lazyRender = true;
        }
        return blockDom;
    }, createItemDomDetail:dorado._NULL_FUNCTION, refreshItemDomData:function (blockDom, item) {
        if (blockDom._lazyRender) {
            return;
        }
        var timestamp = (item instanceof dorado.Entity) ? item.timestamp : -1;
        if (this._ignoreItemTimestamp || timestamp <= 0 || blockDom.timestamp != timestamp) {
            (this._renderer || $singleton(dorado.widget.blockview.DefaultBlockRenderer)).render(blockDom, {blockView:this, data:item});
            blockDom.timestamp = timestamp;
        }
    }, getHoverBlockDecorator:function () {
        var decorator = this._hoverDecorator;
        if (!decorator) {
            this._hoverDecorator = decorator = new dorado.widget.Panel({className:"block-decorator", border:"curve"});
            decorator.render(this._container);
            this.registerInnerControl(decorator);
        }
        return decorator;
    }, setHoverBlock:function (itemDom) {
        if (itemDom) {
            if (this._draggable && this._dragMode != "control") {
                this.applyDraggable(itemDom);
            }
            $fly(itemDom).addClass("block-hover");
        }
        if (this._hoverBlock == itemDom) {
            return;
        }
        if (this._hoverBlock) {
            $fly(this._hoverBlock).removeClass("block-hover");
        }
        this._hoverBlock = itemDom;
        var decorator = this.getHoverBlockDecorator();
        if (itemDom && this._currentBlock != itemDom) {
            decorator.set({width:itemDom.offsetWidth + this._blockDecoratorSize * 2, height:itemDom.offsetHeight + this._blockDecoratorSize * 2, style:{left:itemDom.offsetLeft - this._blockDecoratorSize, top:itemDom.offsetTop - this._blockDecoratorSize, display:""}});
        } else {
            decorator.set("style", {display:"none"});
        }
        decorator.refresh();
    }, getCurrentBlockDecorator:function () {
        var decorator = this._currentDecorator;
        if (!decorator) {
            this._currentDecorator = decorator = new dorado.widget.Panel({className:"block-decorator", border:"curve"});
            $fly(decorator.getDom()).addClass("current-decorator");
            decorator.render(this._container);
            this.registerInnerControl(decorator);
        }
        return decorator;
    }, setCurrentBlock:function (itemDom) {
        if (this._currentBlock == itemDom) {
            return;
        }
        if (this._currentBlock) {
            $fly(this._currentBlock).removeClass("block-current");
        }
        this._currentBlock = itemDom;
        if (itemDom) {
            $fly(itemDom).addClass("block-current");
        }
        var decorator = this.getCurrentBlockDecorator();
        if (itemDom) {
            decorator.set({width:itemDom.offsetWidth + this._blockDecoratorSize * 2, height:itemDom.offsetHeight + this._blockDecoratorSize * 2, style:{left:itemDom.offsetLeft - this._blockDecoratorSize, top:itemDom.offsetTop - this._blockDecoratorSize, display:""}});
        } else {
            decorator.set("style", {display:"none"});
        }
        decorator.refresh();
    }, onScroll:function () {
        function process(p1, p2) {
            if (scroller[p1] == (scroller[p2] || 0)) {
                return;
            }
            if (scroller._scrollTimerId) {
                clearTimeout(scroller._scrollTimerId);
                scroller._scrollTimerId = undefined;
            } else {
                scroller[p2] = scroller[p1];
            }
            scroller._scrollTimerId = $setTimeout(this, this.doOnScroll, 300);
        }
        if (this._scrollMode == "viewport") {
            var scroller = this._dom;
            if (this._blockLayout == "vertical") {
                if ((this._scrollTop || 0) != scroller.scrollTop) {
                    process.call(this, "scrollTop", "_scrollTop");
                }
            } else {
                if ((this._scrollLeft || 0) != scroller.scrollLeft) {
                    process.call(this, "scrollLeft", "_scrollLeft");
                }
            }
            this._scrollLeft = scroller.scrollLeft;
            this._scrollTop = scroller.scrollTop;
        } else {
            if (this._scrollMode == "lazyRender") {
                var range = this._getVisibleBlockRange(), childNodes = this._container.childNodes;
                for (var i = range[0]; i <= range[1] && i < childNodes.length; i++) {
                    var blockDom = childNodes[i];
                    if (blockDom._lazyRender) {
                        var item = $fly(blockDom).data("item");
                        this.createItemDomDetail(blockDom, item);
                        delete blockDom._lazyRender;
                        this.refreshItemDomData(blockDom, item);
                    }
                }
            }
        }
    }, doOnScroll:function () {
        var scroller = this._dom;
        if (scroller._scrollTimerId) {
            clearTimeout(scroller._scrollTimerId);
            scroller._scrollTimerId = undefined;
        }
        this._itemModel.setScrollPos((this._blockLayout == "vertical") ? scroller.scrollTop : scroller.scrollLeft);
        this.refreshViewPortContent(this._container);
    }, _findBlockDom:function (evt) {
        var container = this._container;
        return $DomUtils.findParent(evt.target, function (parentNode) {
            return parentNode.parentNode == container;
        });
    }, onMouseDown:function (evt) {
        var blockDom = this._findBlockDom(evt);
        if (blockDom || this._allowNoCurrent) {
            var data = blockDom ? $fly(blockDom).data("item") : null;
            this.fireEvent("onBlockMouseDown", this, {data:data});
            this.setCurrentItemDom(blockDom);
        }
    }, onMouseUp:function (evt) {
        var blockDom = this._findBlockDom(evt);
        if (blockDom) {
            var data = $fly(blockDom).data("item");
            if (data) {
                this.fireEvent("onBlockMouseUp", this, {data:data});
            }
        }
    }, onClick:function (evt) {
        var blockDom = this._findBlockDom(evt);
        if (blockDom) {
            var data = $fly(blockDom).data("item");
            if (data) {
                this.fireEvent("onBlockClick", this, {data:data});
            }
        }
    }, onDoubleClick:function (evt) {
        var blockDom = this._findBlockDom(evt);
        if (blockDom) {
            var data = $fly(blockDom).data("item");
            if (data) {
                this.fireEvent("onBlockDoubleClick", this, {data:data});
            }
        }
    }, scrollCurrentIntoView:function () {
        var currentItemId = this.getCurrentItemId();
        var itemDom = this._itemDomMap[currentItemId], dom = this._dom, itemIndex;
        if (itemDom) {
            itemIndex = itemDom.itemIndex;
            if (itemIndex >= this.startIndex && itemIndex <= (this.startIndex + this.itemDomCount)) {
                if (this._blockLayout == "vertical") {
                    if (itemDom.offsetTop < dom.scrollTop) {
                        dom.scrollTop = this._scrollTop = itemDom.offsetTop - this._vertSpacing;
                    } else {
                        if ((itemDom.offsetTop + itemDom.offsetHeight) > (dom.scrollTop + dom.clientHeight)) {
                            dom.scrollTop = this._scrollTop = itemDom.offsetTop + itemDom.offsetHeight - dom.clientHeight + this._vertSpacing;
                        }
                    }
                } else {
                    if (itemDom.offsetLeft < dom.scrollLeft) {
                        dom.scrollLeft = this._scrollLeft = itemDom.offsetLeft - this._horiSpacing;
                    } else {
                        if ((itemDom.offsetLeft + itemDom.offsetWidth) > (dom.scrollLeft + dom.clientWidth)) {
                            dom.scrollLeft = this._scrollLeft = itemDom.offsetLeft + itemDom.offsetWidth - dom.clientWidth + this._horiSpacing;
                        }
                    }
                }
                return;
            }
        } else {
            var item = this.getCurrentItem();
            if (item) {
                itemIndex = this._itemModel.getItemIndex(item);
            }
        }
        var lineIndex = parseInt(itemIndex / this._realLineSize);
        if (itemIndex < this.startIndex) {
            if (this._blockLayout == "vertical") {
                dom.scrollTop = this._scrollTop = lineIndex * (this._blockHeight + this._vertSpacing) + this._vertPadding - this._vertSpacing;
            } else {
                dom.scrollLeft = this._scrollLeft = lineIndex * (this._blockWidth + this._horiSpacing) + this._horiPadding - this._horiSpacing;
            }
        } else {
            if (itemIndex > (this.startIndex + this.itemDomCount - 1)) {
                if (this._blockLayout == "vertical") {
                    dom.scrollTop = this._scrollTop = (lineIndex + 1) * (this._blockHeight + this._vertSpacing) + this._vertPadding - dom.clientHeight;
                } else {
                    dom.scrollLeft = this._scrollLeft = (lineIndex + 1) * (this._blockWidth + this._horiSpacing) + this._horiPadding - dom.clientWidth;
                }
            }
        }
        this.doOnScroll();
    }, _getContainerSize:function () {
        var width = -1, height = -1, result;
        var lineCount = this._itemModel.getLineCount();
        var dom = this._dom, contrainer = this._container, hasScroller;
        var blockWidth = this._blockWidth, blockHeight = this._blockHeight;
        if (this._blockLayout == "vertical") {
            hasScroller = dom.scrollHeight > dom.clientHeight;
            height = this._vertPadding * 2 + (blockHeight + this._vertSpacing) * lineCount - this._vertSpacing;
            result = (height > dom.clientHeight) ^ hasScroller;
        } else {
            hasScroller = dom.scrollWidth > dom.clientWidth;
            width = this._horiPadding * 2 + (blockWidth + this._horiSpacing) * lineCount - this._horiSpacing;
            result = (width > dom.clientWidth) ^ hasScroller;
        }
        return [width, height, result];
    }, _arrangeBlockDoms:function (from, move) {
        var container = this._container;
        var blockDom = container.firstChild;
        while (blockDom) {
            if (blockDom.itemIndex >= from) {
                blockDom.itemIndex = blockDom.itemIndex + move;
                var pos = this._getBlockPos(blockDom.itemIndex);
                $fly(blockDom).css({left:pos[0], top:pos[1]});
            }
            blockDom = blockDom.nextSibling;
        }
    }, onResize:function () {
        if (this._lineSize && this._fillLine) {
            this.refresh();
        } else {
            this._arrangeBlockDoms();
        }
    }, findItemDomByEvent:function (evt) {
        var target = evt.srcElement || evt.target;
        var target = target || evt, container = this._container;
        return $DomUtils.findParent(target, function (parentNode) {
            return parentNode.parentNode == container;
        });
    }, findItemDomByPosition:function (pos) {
        var dom = this._dom, x = pos.x + dom.scrollLeft, y = pos.y + dom.scrollTop;
        var xIndex = parseInt((x - this._horiPadding - this._horiSpacing / 2) / (this._realBlockWidth + this._horiSpacing));
        var yIndex = parseInt((y - this._vertPadding - this._vertSpacing / 2) / (this._realBlockHeight + this._vertSpacing));
        var index = -1;
        if (this._blockLayout == "vertical") {
            if (xIndex > this._realLineSize - 1) {
                xIndex = this._realLineSize - 1;
            }
            index = this._realLineSize * yIndex + xIndex;
        } else {
            if (yIndex > this._realLineSize - 1) {
                yIndex = this._realLineSize - 1;
            }
            index = this._realLineSize * xIndex + yIndex;
        }
        if (index >= 0 && index < this._itemModel.getItemCount()) {
            var itemModel = this._itemModel, item = itemModel.getItemAt(index);
            var blockDom = this._itemDomMap[itemModel.getItemId(item)];
            if (blockDom) {
                blockDom._dropX = x - blockDom.offsetLeft;
                blockDom._dropY = y - blockDom.offsetTop;
            }
            return blockDom;
        } else {
            return null;
        }
    }, initDraggingIndicator:function (indicator, draggingInfo, evt) {
        if (this._dragMode != "control") {
            var itemDom = draggingInfo.get("element");
            if (itemDom) {
                var contentDom = $DomUtils.xCreateElement({tagName:"div", className:"d-list-dragging-item"});
                $fly(itemDom).clone().css({left:0, top:0, position:"relative"}).appendTo(contentDom);
                indicator.set("content", contentDom);
            }
        }
    }, setDraggingOverBlockDom:function (blockDom) {
        if (this._draggingOverBlockDom == blockDom) {
            return;
        }
        if (this._draggingOverBlockDom) {
            $fly(this._draggingOverBlockDom).removeClass("block-drag-over");
        }
        this._draggingOverBlockDom = blockDom;
        if (blockDom) {
            $fly(blockDom).addClass("block-drag-over");
        }
    }, onDraggingSourceMove:function (draggingInfo, evt) {
        var dropMode = this._dropMode;
        var targetObject = draggingInfo.get("targetObject");
        var insertMode, refObject, itemDom;
        if (dropMode != "onControl") {
            var pos = this.getMousePosition(evt);
            blockItem = this.findItemDomByPosition(pos);
            if (blockItem && $fly(blockItem).data("item") == draggingInfo.get("object")) {
                blockItem = null;
            }
            if (blockItem) {
                if (dropMode == "insertItems") {
                    if (this._blockLayout == "vertical") {
                        insertMode = (blockItem._dropX < (this._realBlockWidth / 2)) ? "before" : "after";
                    } else {
                        insertMode = (blockItem._dropY < (this._realBlockHeight / 2)) ? "before" : "after";
                    }
                } else {
                    if (dropMode == "onOrInsertItems") {
                        if (this._blockLayout == "vertical") {
                            if (blockItem._dropX < 4) {
                                insertMode = "before";
                            } else {
                                if (blockItem._dropX > (this._realBlockWidth - 4)) {
                                    insertMode = "after";
                                }
                            }
                        } else {
                            if (blockItem._dropY < 4) {
                                insertMode = "before";
                            } else {
                                if (blockItem._dropY > (this._realBlockHeight - 4)) {
                                    insertMode = "after";
                                }
                            }
                        }
                    }
                }
            }
            refObject = blockItem ? $fly(blockItem).data("item") : null;
            if (!refObject) {
                targetObject = (dropMode == "onAnyWhere") ? this : null;
            } else {
                targetObject = refObject;
            }
        }
        var accept = (draggingInfo.isDropAcceptable(this._droppableTags) && !(dropMode == "onItem" && targetObject == null));
        draggingInfo.set({targetObject:targetObject, insertMode:insertMode, refObject:refObject, accept:accept});
        var eventArg = {draggingInfo:draggingInfo, event:evt, processDefault:true};
        this.fireEvent("onDraggingSourceMove", this, eventArg);
        if (accept && eventArg.processDefault) {
            this.setDraggingOverBlockDom(blockItem);
            this.showDraggingInsertIndicator(draggingInfo, insertMode, blockItem);
        }
        return eventArg.processDefault;
    }, onDraggingSourceOut:function (draggingInfo, evt) {
        $invokeSuper.call(this, arguments);
        this.setDraggingOverBlockDom();
        this.showDraggingInsertIndicator();
    }, showDraggingInsertIndicator:function (draggingInfo, insertMode, blockDom) {
        var insertIndicator = dorado.widget.blockview.getDraggingInsertIndicator(this._blockLayout);
        var $insertIndicator = $fly(insertIndicator);
        if (insertMode) {
            var container = this._container;
            if (this._blockLayout == "vertical") {
                var left;
                if (insertMode == "before") {
                    left = blockDom.offsetLeft - parseInt(this._horiSpacing / 2);
                } else {
                    left = blockDom.offsetLeft + blockDom.offsetWidth + parseInt(this._horiSpacing / 2);
                }
                $insertIndicator.height(blockDom.offsetHeight).left(left - 1).top(blockDom.offsetTop);
            } else {
                var top;
                if (insertMode == "before") {
                    top = blockDom.offsetTop - parseInt(this._vertSpacing / 2);
                } else {
                    top = blockDom.offsetTop + blockDom.offsetHeight + parseInt(this._vertSpacing / 2);
                }
                $insertIndicator.width(blockDom.offsetWidth).top(top - 1).left(blockDom.offsetLeft);
            }
            $insertIndicator.show().appendTo(container);
        } else {
            $insertIndicator.hide().appendTo($DomUtils.getInvisibleContainer());
        }
    }, onDraggingSourceDrop:function (draggingInfo, evt) {
        this.showDraggingInsertIndicator();
        return dorado.widget.RowList.prototype.onDraggingSourceDrop.apply(this, arguments);
    }, processItemDrop:dorado.widget.RowList.prototype.processItemDrop});
    dorado.widget.blockview.getDraggingInsertIndicator = function (direction) {
        var code = (direction == "horizontal") ? "h" : "v";
        var indicator = this["_draggingInsertIndicator-" + code];
        if (indicator == null) {
            indicator = $DomUtils.xCreateElement({tagName:"div", className:"d-block-dragging-insert-indicator-" + code});
            this["_draggingInsertIndicator-" + code] = indicator;
        }
        return indicator;
    };
})();
dorado.widget.BlockView = $extend(dorado.widget.AbstractBlockView, {$className:"dorado.widget.BlockView", ATTRIBUTES:{currentIndex:{skipRefresh:true, setter:function (p, index) {
    var itemModel = this._itemModel;
    if (index >= itemModel.getItemCount()) {
        index = -1;
    }
    if (this._currentIndex == index) {
        return;
    }
    this._currentIndex = index;
    if (this._rendered) {
        var blockDom = this.getItemDomByItemIndex(index);
        this.setCurrentBlock(blockDom);
        this.scrollCurrentIntoView();
    }
}}, items:{setter:function (p, v) {
    this._itemModel.setItems(v);
}, getter:function () {
    return this._itemModel.getItems();
}}}, refreshDom:function (dom) {
    this.set("currentIndex", this._currentIndex);
    $invokeSuper.call(this, arguments);
}, getItemDomByItemIndex:function (index) {
    return this._itemDomMap[index];
}, getCurrentItem:function () {
    if (this._currentIndex >= 0) {
        return this._itemModel.getItemAt(this._currentIndex);
    }
}, getCurrentItemId:function () {
    return this._currentIndex;
}, setCurrentItemDom:function (blockDom) {
    this.set("currentIndex", blockDom ? blockDom.itemIndex : -1);
    return true;
}, doOnKeyDown:function (evt) {
    function previous() {
        if (this._currentIndex > 0) {
            this.set("currentIndex", this._currentIndex - 1);
        }
    }
    function next() {
        if (this._currentIndex < this._itemModel.getItemCount() - 1) {
            this.set("currentIndex", this._currentIndex + 1);
        }
    }
    function previousLine() {
        var i = this._currentIndex - this._realLineSize;
        if (i < 0) {
            i = 0;
        }
        this.set("currentIndex", i);
    }
    function nextLine() {
        var i = this._currentIndex + this._realLineSize;
        if (i >= this._itemModel.getItemCount()) {
            i = this._itemModel.getItemCount() - 1;
        }
        this.set("currentIndex", i);
    }
    var retValue = true;
    switch (evt.keyCode || evt.which) {
      case 36:
        this.set("currentIndex", 0);
        break;
      case 35:
        this.set("currentIndex", this._itemModel.getItemCount() - 1);
        break;
      case 37:
        ((this._blockLayout == "vertical") ? previous : previousLine).call(this);
        retValue = false;
        break;
      case 39:
        ((this._blockLayout == "vertical") ? next : nextLine).call(this);
        retValue = false;
        break;
      case 38:
        ((this._blockLayout == "vertical") ? previousLine : previous).call(this);
        retValue = false;
        break;
      case 40:
        ((this._blockLayout == "vertical") ? nextLine : next).call(this);
        retValue = false;
        break;
    }
    return retValue;
}, highlightItem:function (index, options, speed) {
    var block = this.getItemDomByItemIndex(index);
    if (block) {
        $fly(block).effect("pulsate", {times:3}, speed || 230);
    }
}});
dorado.widget.DataBlockView = $extend([dorado.widget.AbstractBlockView, dorado.widget.DataControl], {$className:"dorado.widget.DataBlockView", getCurrentItem:function () {
    return this._itemModel.getItems().current;
}, getCurrentItem:function () {
    return this._itemModel.getItems().current;
}, getCurrentItemId:function (item, index) {
    var current = this._itemModel.getItems().current;
    return current ? current.entityId : null;
}, setCurrentItemDom:function (blockDom) {
    var item = $fly(blockDom).data("item"), b = this.setCurrentEntity(item);
    if (b) {
        this._itemModel.getItems().setCurrent(item);
    }
    return b;
}, refreshEntity:function (entity) {
    var blockDom = this._itemDomMap[entity.entityId];
    if (blockDom) {
        this.refreshItemDomData(blockDom, entity);
    }
}, refreshDom:function (dom) {
    var entityList = this.getBindingData({firstResultOnly:true, acceptAggregation:true});
    if (entityList && !(entityList instanceof dorado.EntityList)) {
        throw new dorado.ResourceException(dorado.list.BindingTypeMismatch);
    }
    this._itemModel.setItems(entityList);
    $invokeSuper.call(this, arguments);
}, setCurrentEntity:function (entity) {
    var itemId = entity ? entity.entityId : null;
    var blockDom = this._itemDomMap[itemId];
    this.setCurrentBlock(blockDom);
    this.scrollCurrentIntoView();
    return true;
}, onEntityDeleted:function (arg) {
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
}, onEntityInserted:function (arg) {
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
        this.refreshItemDom(container, entity, this.itemCount);
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
}, doOnKeyDown:function (evt) {
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
      case 36:
        items.first();
        break;
      case 35:
        items.last();
        break;
      case 37:
        ((this._blockLayout == "vertical") ? previous : previousLine).call(this);
        retValue = false;
        break;
      case 39:
        ((this._blockLayout == "vertical") ? next : nextLine).call(this);
        retValue = false;
        break;
      case 38:
        ((this._blockLayout == "vertical") ? previousLine : previous).call(this);
        retValue = false;
        break;
      case 40:
        ((this._blockLayout == "vertical") ? nextLine : next).call(this);
        retValue = false;
        break;
    }
    return retValue;
}, highlightItem:function (entity, options, speed) {
    var block = this._itemDomMap[entity.entityId];
    if (block) {
        $fly(block).effect("pulsate", {times:3}, speed || 300);
    }
}});

