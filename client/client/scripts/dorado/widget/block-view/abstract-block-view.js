﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
(function() {

	/**
	 * @name dorado.widget.blockview
	 * @namespace 块状列表控件所使用的一些相关类的命名空间。
	 */
	dorado.widget.blockview = {};
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 块状列表控件的数据模型。
	 *        <p>
	 *        用于辅助块状列表控件管理数据的对象。
	 *        </p>
	 * @extends dorado.widget.list.ItemModel
	 */
	dorado.widget.blockview.BlockItemModel = $extend(dorado.widget.list.ItemModel, /** @scope dorado.widget.blockview.BlockItemModel.prototype */ {
		_lineSize: 1,
		
		/**
		 * 设置每一行或列中包含的块的个数。
		 *
		 * @param {Object}
		 *            lineSize 块的个数。
		 */
		setLineSize: function(lineSize) {
			this._lineSize = lineSize;
		},
		
		/**
		 * 返回总的行数或列数。 return {int} 行数或列数。
		 */
		getLineCount: function() {
			return parseInt((this.getItemCount() - 1) / this._lineSize + 1);
		},
		
		setScrollPos: function(scrollPos) {
			var itemCount = this.getItemCount(), lineCount = this.getLineCount();
			if (lineCount > 0) {
				this._startIndex = parseInt(((scrollPos / this._scrollSize) || 0) * lineCount) * this._lineSize;
			} else {
				this._startIndex = 0;
			}
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 默认的块渲染器。
	 * @extends dorado.Renderer
	 */
	dorado.widget.blockview.DefaultBlockRenderer = $extend(dorado.Renderer, /** @scope dorado.widget.blockview.DefaultBlockRenderer.prototype */ {
	
		/**
		 * 渲染。
		 *
		 * @param {HTMLElement}
		 *            dom 块对应的DOM对象。
		 * @param {Object}
		 *            arg 渲染参数。
		 * @param {Object|dorado.Entity}
		 *            arg.data 对应的数据实体。
		 */
		render: function(dom, arg) {
			var data = arg.data;
			dom.innerText = dom.itemIndex + ": " + data;
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 图片块的渲染器。
	 * @extends dorado.widget.blockview.DefaultBlockRenderer
	 * @param {Object}
	 *            [options] 包含配置信息的JSON对象。<br>
	 *            该对象中的子属性将在初始化的过程中被复制到渲染器中，此处支持的具体的属性见本对象文旦的Properties段落。
	 */
	dorado.widget.blockview.ImageBlockRenderer = $extend(dorado.widget.blockview.DefaultBlockRenderer, /** @scope dorado.widget.blockview.ImageBlockRenderer.prototype */ {
	
		/**
		 * 用于从数据实体中读取图片文字标签的属性名。
		 *
		 * @type String
		 * @default "caption"
		 */
		captionProperty: "caption",
		
		/**
		 * 用于从数据实体中读取图片URL的属性名。
		 *
		 * @type String
		 * @default "image"
		 */
		imageProperty: "image",
		
		/**
		 * 用于从数据实体中读取提示信息的属性名。
		 *
		 * @type String
		 */
		tipProperty: null,
		
		/**
		 * 当数据实体中没有具体的图片可显示时使用的默认图片的URL。
		 *
		 * @type String
		 * @default ">skin>block/blank-image.png"
		 */
		blankImage: ">skin>block/blank-image.png",
		
		/**
		 * 块内部的留白的大小，像素值。
		 *
		 * @type int
		 * @default 2
		 */
		padding: 2,
		
		/**
		 * 图片与图片文字标签之间的留白的大小，像素值。
		 *
		 * @type int
		 * @default 2
		 */
		spacing: 2,
		
		/**
		 * 文字标签的高度。 如果此高度设置为0则表示不显示文字标签。
		 *
		 * @type int
		 * @default 17
		 */
		labelHeight: 17,
		
		/**
		 * 是否拉伸图片以填充块的空间。
		 *
		 * @type boolean
		 */
		stretchImage: false,
		
		constructor: function(options) {
			dorado.Object.apply(this, options);
		},
		
		getImageDom: function(dom) {
			var img = dom.firstChild;
			if (img == null) {
				img = $DomUtils.xCreate({
					tagName: "IMG",
					style: {
						position: "absolute"
					}
				});
				dom.appendChild(img);
			}
			return img;
		},
		
		getLabelDom: function(dom) {
			var label = dom.lastChild;
			if (label == null || label.nodeName != "CENTER") {
				label = $DomUtils.xCreate({
					tagName: "CENTER",
					style: {
						position: "absolute"
					},
					content: "^LABEL"
				});
				dom.appendChild(label);
			}
			return label;
		},
		
		render: function(dom, arg) {
			var imageDom = this.getImageDom(dom), entity = arg.data;
			
			var labelHeight = 0;
			if (this.captionProperty && this.labelHeight > 0) {
				labelHeight = this.labelHeight;
				var labelDom = this.getLabelDom(dom);
				var label = (entity instanceof dorado.Entity) ? entity.get(this.captionProperty) : entity[this.captionProperty];
				$fly(labelDom).css({
					bottom: this.padding,
					width: dom.clientWidth - this.padding * 2,
					height: labelHeight
				});
				labelDom.firstChild.innerText = label;
			}
			
			if (this.tipProperty) {
				var tip = (entity instanceof dorado.Entity) ? entity.get(this.tipProperty) : entity[this.tipProperty];
				if (tip) {
					dorado.TipManager.initTip(dom, {
						text: tip
					});
				} else {
					dorado.TipManager.deleteTip(dom);
				}
			}
			
			var self = this;
			var maxWidth = dom.clientWidth - this.padding * 2;
			var maxHeight = dom.clientHeight - labelHeight - this.spacing - this.padding * 2;
			if (this.stretchImage) {
				$fly(imageDom).css({
					left: this.padding,
					top: this.padding,
					width: maxWidth,
					height: maxHeight
				});
			} else {
				$fly(imageDom).bind("load", function() {
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
					$fly(imageDom).css({
						left: left,
						top: top,
						width: width,
						height: height
					});
				});
			}
			var image = (entity instanceof dorado.Entity) ? entity.get(this.imageProperty) : entity[this.imageProperty];
			$DomUtils.setImgSrc(imageDom, image || this.blankImage);
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 块状列表控件的抽象类。
	 * @abstract
	 * @extends dorado.widget.ViewPortList
	 */
	dorado.widget.AbstractBlockView = $extend(dorado.widget.ViewPortList, /** @scope dorado.widget.AbstractBlockView.prototype */ {
		$className: "dorado.widget.AbstractBlockView",
		
		ATTRIBUTES: /** @scope dorado.widget.AbstractBlockView.prototype */ {
			className: {
				defaultValue: "d-block-view"
			},
			
			/**
			 * 块的布局方式。 目前支持两种定义方式：
			 * <ul>
			 * <li>vertical - 垂直方向布局，由上向下。</li>
			 * <li>horizontal - 水平方向布局，由左向右。</li>
			 * </ul>
			 *
			 * @type String
			 * @attribute
			 * @default "vertical"
			 */
			blockLayout: {
				defaultValue: "vertical"
			},
			
			/**
			 * 每行或列显示的块的个数。
			 * <p>
			 * 如果不设置此属性或设置为0表示有系统自动判断每行或列显示的块个数。
			 * </p>
			 *
			 * @type int
			 * @attribute
			 */
			lineSize: {},
			
			/**
			 * 默认的数据块的宽度。
			 *
			 * @type int
			 * @attribute
			 * @default 80
			 */
			blockWidth: {
				defaultValue: 80
			},
			
			/**
			 * 默认的数据块的高度。
			 *
			 * @type int
			 * @attribute
			 * @default 80
			 */
			blockHeight: {
				defaultValue: 80
			},
			
			/**
			 * 是否自动调整块的宽度或高度以充满整个行或列。
			 *
			 * @type boolean
			 * @attribute
			 */
			fillLine: {},
			
			/**
			 * 块渲染器。
			 *
			 * @type dorado.Renderer
			 * @attribute
			 */
			renderer: {
				setter: function(value) {
					if (typeof value == "string") value = eval("new " + value + "()");
					this._renderer = value;
				}
			},
			
			/**
			 * 水平方向上块与块之间空隙的大小。
			 *
			 * @type int
			 * @attribute
			 * @default 8
			 */
			horiSpacing: {
				defaultValue: 8
			},
			
			/**
			 * 垂直方向上块与块之间空隙的大小。
			 *
			 * @type int
			 * @attribute
			 * @default 8
			 */
			vertSpacing: {
				defaultValue: 8
			},
			
			/**
			 * 块状列表边界与块之间水平空隙的大小。
			 *
			 * @type int
			 * @attribute
			 * @default 8
			 */
			horiPadding: {
				defaultValue: 8
			},
			
			/**
			 * 块状列表边界与块之间垂直空隙的大小。
			 *
			 * @type int
			 * @attribute
			 * @default 8
			 */
			vertPadding: {
				defaultValue: 8
			},
			
			/**
			 * 块装饰器的大小。
			 * <p>
			 * 块装饰器是用于显示当前块或鼠标选定等特殊状态是使用的装饰对象。
			 * </p>
			 * <p>
			 * 此值是一个相对值，表示装饰器在每个方向上比块的尺寸大出的部分的大小。<br>
			 * 例如块的大小是80*80，而blockDecoratorSize=4，那么实际的块装饰器的大小将是88*88。
			 * </p>
			 *
			 * @type int
			 * @attribute
			 * @default 4
			 */
			blockDecoratorSize: {
				defaultValue: 4
			}
		},
		
		EVENTS: /** @scope dorado.widget.AbstractBlockView.prototype */ {
		
			/**
			 * 当块状列表渲染块时触发的事件。
			 *
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @param {HTMLElement}
			 *            arg.dom 块对应的DOM对象。
			 * @param {Object|dorado.Entity}
			 *            arg.data 块对应的数据实体。
			 * @param {boolean}
			 *            #arg.processDefault 是否在事件结束后继续使用系统默认的渲染逻辑。
			 * @return {boolean}
			 *         是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onRenderBlock: {},
			
			/**
			 * 当鼠标在某个块中按下时触发的事件。
			 *
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @param {Object|dorado.Entity}
			 *            arg.data 块对应的数据实体。
			 * @return {boolean}
			 *         是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onBlockMouseDown: {},
			
			/**
			 * 当鼠标在某个块中抬起时触发的事件。
			 *
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @param {Object|dorado.Entity}
			 *            arg.data 块对应的数据实体。
			 * @return {boolean}
			 *         是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onBlockMouseUp: {},
			
			/**
			 * 当某个块被点击时触发的事件。
			 *
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @param {Object|dorado.Entity}
			 *            arg.data 块对应的数据实体。
			 * @return {boolean}
			 *         是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onBlockClick: {},
			
			/**
			 * 当某个块被双击时触发的事件。
			 *
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @param {Object|dorado.Entity}
			 *            arg.data 块对应的数据实体。
			 * @return {boolean}
			 *         是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onBlockDoubleClick: {}
		},
		
		createItemModel: function() {
			return new dorado.widget.blockview.BlockItemModel();
		},
		
		createDom: function() {
			var dom = $invokeSuper.call(this, arguments);
			var container = this._container = $DomUtils.xCreate({
				tagName: "DIV",
				style: {
					position: "relative",
					overflow: "hidden"
				}
			});
			dom.appendChild(container);
			
			$fly(dom).bind("scroll", $scopify(this, this.onScroll)).mouseover($scopify(this, function(evt) {
				if ($DomUtils.isDragging()) return;
				var blockDom = $DomUtils.findParent(evt.target, function(node) {
					return node.parentNode == container &&
					$fly(node).hasClass("block");
				});
				this.setHoverBlock(blockDom);
			})).mouseleave($scopify(this, function(evt) {
				this.setHoverBlock(null);
			}));
			return dom;
		},
		
		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);
			
			var lineSize = this._lineSize, blockWidth = this._blockWidth, blockHeight = this._blockHeight;
			if (this._blockLayout == "vertical") {
				if (!(lineSize > 0)) {
					lineSize = (dom.clientWidth - this._horiPadding * 2) / (this._blockWidth + this._horiSpacing);
				}
			} else {
				if (!(lineSize > 0)) {
					lineSize = (dom.clientHeight - this._vertPadding * 2) / (this._blockHeight + this._vertSpacing);
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
				if (blockDom._isBlock) this.removeItemDom(blockDom);
				blockDom = next;
			}
			
			if (this._scrollMode == "viewport") {
				this.refreshViewPortContent(this._container);
			} else {
				this.refreshContent(this._container);
			}
		},
		
		_getVisibleBlockRange: function() {
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
		},
		
		refreshContent: function(container) {
			var fn;
			if (this._scrollMode == "lazyRender") {
				var count = this._getVisibleBlockRange()[1], i = 0;
				fn = function(row) {
					i++;
					return i < count;
				};
			}
			this.refreshItemDoms(container, false, fn);
			this._itemDomCount = this._itemModel.getItemCount();
		},
		
		refreshViewPortContent: function(container) {
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
				itemDomCount = this.refreshItemDoms(container, itemModel.isReverse(), function(itemDom) {
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
		},
		
		refreshItemDoms: function(itemDomContainer) {
			var currentDecorator = this._currentDecorator, hoverDecorator = this._hoverDecorator;
			if (currentDecorator) itemDomContainer.removeChild(currentDecorator.getDom());
			if (hoverDecorator) itemDomContainer.removeChild(hoverDecorator.getDom());
			
			$invokeSuper.call(this, arguments);
			
			if (hoverDecorator) itemDomContainer.insertBefore(hoverDecorator.getDom(), itemDomContainer.firstChild);
			if (currentDecorator) itemDomContainer.insertBefore(currentDecorator.getDom(), itemDomContainer.firstChild);
			
			var currentItemId = this.getCurrentItemIdForRefresh();
			if (currentItemId) {
				var itemDom = this._itemDomMap[currentItemId];
				if (itemDom) this.setCurrentBlock(itemDom);
			}
		},
		
		_getBlockPos: function(index) {
			var lineIndex = parseInt(index / this._realLineSize);
			var subIndex = index % this._realLineSize;
			var left, top;
			if (this._blockLayout == "vertical") {
				left = this._horiPadding +
				(this._realBlockWidth + this._horiSpacing) *
				subIndex;
				top = this._vertPadding +
				(this._realBlockHeight + this._vertSpacing) *
				lineIndex;
			} else {
				left = this._horiPadding +
				(this._realBlockWidth + this._horiSpacing) *
				lineIndex;
				top = this._vertPadding +
				(this._realBlockHeight + this._vertSpacing) *
				subIndex;
			}
			return [left, top, lineIndex, subIndex];
		},
		
		removeItemDom: function(blockDom) {
			$invokeSuper.call(this, arguments);
			this._itemDomCount--;
			this._lineCount = parseInt(this._itemDomCount /
			this._realLineSize);
		},
		
		refreshItemDom: function(itemDomContainer, item, index, prepend) {
			var flag = prepend ? -1 : 1;
			if (index < 0) flag = -flag;
			index = (this._itemModel.getStartIndex() || 0) +
			index *
			flag;
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
			$fly(itemDom).data("item", item).css({
				left: pos[0],
				top: pos[1]
			}).outerWidth(this._realBlockWidth).outerHeight(this._realBlockHeight);
			this._itemDomCount++;
			this._lineCount = parseInt(this._itemDomCount /
			this._realLineSize);
			
			this.refreshItemDomData(itemDom, item);
			return itemDom;
		},
		
		createItemDom: function(item) {
			var blockDom = $DomUtils.xCreate({
				tagName: "DIV",
				className: "block",
				style: {
					position: "absolute",
					overflow: "hidden"
				}
			});
			blockDom._isBlock = true;
			if (this._scrollMode == "lazyRender" &&
			this._shouldSkipRender) {
				blockDom._lazyRender = true;
			}
			return blockDom;
		},
		
		createItemDomDetail: dorado._NULL_FUNCTION,
		
		refreshItemDomData: function(blockDom, item) {
			if (blockDom._lazyRender) return;
			
			var processDefault = true;
			var eventArg = {
				dom: blockDom,
				data: item,
				processDefault: false
			};
			
			if (this.getListenerCount("onRenderBlock") &&
			this.fireEvent("onRenderBlock", this, eventArg)) {
				processDefault = eventArg.processDefault;
			}
			if (processDefault) {
				var timestamp = (item instanceof dorado.Entity) ? item.timestamp : -1;
				if (this._ignoreItemTimestamp || timestamp <= 0 ||
				blockDom.timestamp != timestamp) {
					(this._renderer || $singleton(dorado.widget.blockview.DefaultBlockRenderer)).render(blockDom, {
						blockView: this,
						data: item
					});
					blockDom.timestamp = timestamp;
				}
			}
		},
		
		getHoverBlockDecorator: function() {
			var decorator = this._hoverDecorator;
			if (!decorator) {
				this._hoverDecorator = decorator = new dorado.widget.Panel({
					className: "block-decorator",
					border: "curve"
				});
				var refDom;
				if (this._currentDecorator) refDom = this._currentDecorator.getDom();
				this.registerInnerControl(decorator);
				decorator.render(this._container, refDom);
			}
			return decorator;
		},
		
		setHoverBlock: function(itemDom) {
			if (itemDom) {
				if (this._draggable && this._dragMode != "control") {
					this.applyDraggable(itemDom);
				}
				$fly(itemDom).addClass("block-hover");
			}
			if (this._hoverBlock == itemDom) return;
			if (this._hoverBlock) $fly(this._hoverBlock).removeClass("block-hover");
			this._hoverBlock = itemDom;
			
			var decorator = this.getHoverBlockDecorator();
			if (itemDom && this._currentBlock != itemDom) {
				decorator.set({
					width: itemDom.offsetWidth +
					this._blockDecoratorSize * 2,
					height: itemDom.offsetHeight +
					this._blockDecoratorSize * 2,
					style: {
						left: itemDom.offsetLeft -
						this._blockDecoratorSize,
						top: itemDom.offsetTop -
						this._blockDecoratorSize,
						display: ''
					}
				});
			} else {
				decorator.set("style", {
					display: "none"
				});
			}
			decorator.refresh();
		},
		
		getCurrentBlockDecorator: function() {
			var decorator = this._currentDecorator;
			if (!decorator) {
				this._currentDecorator = decorator = new dorado.widget.Panel({
					className: "block-decorator",
					border: "curve"
				});
				$fly(decorator.getDom()).addClass("current-decorator");
				
				decorator.render(this._container);
				this.registerInnerControl(decorator);
			}
			return decorator;
		},
		
		setCurrentBlock: function(itemDom) {
			if (this._currentBlock == itemDom) return;
			if (this._currentBlock) $fly(this._currentBlock).removeClass("block-current");
			this._currentBlock = itemDom;
			if (itemDom) $fly(itemDom).addClass("block-current");
			
			var decorator = this.getCurrentBlockDecorator();
			if (itemDom) {
				decorator.set({
					width: itemDom.offsetWidth +
					this._blockDecoratorSize * 2,
					height: itemDom.offsetHeight +
					this._blockDecoratorSize * 2,
					style: {
						left: itemDom.offsetLeft -
						this._blockDecoratorSize,
						top: itemDom.offsetTop -
						this._blockDecoratorSize,
						display: ''
					}
				});
			} else {
				decorator.set("style", {
					display: "none"
				});
			}
			decorator.refresh();
		},
		
		onScroll: function() {
		
			function process(p1, p2) {
				if (scroller[p1] == (scroller[p2] || 0)) return;
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
			} else if (this._scrollMode == "lazyRender") {
				var range = this._getVisibleBlockRange(), childNodes = this._container.childNodes;
				for (var i = range[0]; i <= range[1] &&
				i < childNodes.length; i++) {
					var blockDom = childNodes[i];
					if (blockDom._lazyRender) {
						var item = $fly(blockDom).data("item");
						this.createItemDomDetail(blockDom, item);
						delete blockDom._lazyRender;
						this.refreshItemDomData(blockDom, item);
					}
				}
			}
		},
		
		doOnScroll: function() {
			var scroller = this._dom;
			if (scroller._scrollTimerId) {
				clearTimeout(scroller._scrollTimerId);
				scroller._scrollTimerId = undefined;
			}
			this._itemModel.setScrollPos((this._blockLayout == "vertical") ? scroller.scrollTop : scroller.scrollLeft);
			this.refreshViewPortContent(this._container);
		},
		
		_findBlockDom: function(evt) {
			var container = this._container;
			return $DomUtils.findParent(evt.target, function(parentNode) {
				return parentNode.parentNode == container;
			});
		},
		
		onMouseDown: function(evt) {
			var blockDom = this._findBlockDom(evt);
			if (blockDom || this._allowNoCurrent) {
				var data = blockDom ? $fly(blockDom).data("item") : null;
				this.fireEvent("onBlockMouseDown", this, {
					data: data
				});
				this.setCurrentItemDom(blockDom);
			}
		},
		
		onMouseUp: function(evt) {
			var blockDom = this._findBlockDom(evt);
			if (blockDom) {
				var data = $fly(blockDom).data("item");
				if (data) {
					this.fireEvent("onBlockMouseUp", this, {
						data: data
					});
				}
			}
		},
		
		onClick: function(evt) {
			var blockDom = this._findBlockDom(evt);
			if (blockDom) {
				var data = $fly(blockDom).data("item");
				if (data) {
					this.fireEvent("onBlockClick", this, {
						data: data
					});
				}
			}
		},
		
		onDoubleClick: function(evt) {
			var blockDom = this._findBlockDom(evt);
			if (blockDom) {
				var data = $fly(blockDom).data("item");
				if (data) {
					this.fireEvent("onBlockDoubleClick", this, {
						data: data
					});
				}
			}
		},
		
		scrollCurrentIntoView: function() {
			var currentItemId = this.getCurrentItemId();
			var itemDom = this._itemDomMap[currentItemId], dom = this._dom, itemIndex;
			if (itemDom) {
				itemIndex = itemDom.itemIndex;
				if (itemIndex >= this.startIndex &&
				itemIndex <= (this.startIndex + this.itemDomCount)) {
					if (this._blockLayout == "vertical") {
						if (itemDom.offsetTop < dom.scrollTop) {
							dom.scrollTop = this._scrollTop = itemDom.offsetTop -
							this._vertSpacing;
						} else if ((itemDom.offsetTop + itemDom.offsetHeight) > (dom.scrollTop + dom.clientHeight)) {
							dom.scrollTop = this._scrollTop = itemDom.offsetTop +
							itemDom.offsetHeight -
							dom.clientHeight +
							this._vertSpacing;
						}
					} else {
						if (itemDom.offsetLeft < dom.scrollLeft) {
							dom.scrollLeft = this._scrollLeft = itemDom.offsetLeft -
							this._horiSpacing;
						} else if ((itemDom.offsetLeft + itemDom.offsetWidth) > (dom.scrollLeft + dom.clientWidth)) {
							dom.scrollLeft = this._scrollLeft = itemDom.offsetLeft +
							itemDom.offsetWidth -
							dom.clientWidth +
							this._horiSpacing;
						}
					}
					return;
				}
			} else {
				var item = this.getCurrentItem();
				if (item) itemIndex = this._itemModel.getItemIndex(item);
			}
			var lineIndex = parseInt(itemIndex / this._realLineSize);
			if (itemIndex < this.startIndex) {
				if (this._blockLayout == "vertical") {
					dom.scrollTop = this._scrollTop = lineIndex *
					(this._blockHeight + this._vertSpacing) +
					this._vertPadding -
					this._vertSpacing;
				} else {
					dom.scrollLeft = this._scrollLeft = lineIndex *
					(this._blockWidth + this._horiSpacing) +
					this._horiPadding -
					this._horiSpacing;
				}
			} else if (itemIndex > (this.startIndex + this.itemDomCount - 1)) {
				if (this._blockLayout == "vertical") {
					dom.scrollTop = this._scrollTop = (lineIndex + 1) *
					(this._blockHeight + this._vertSpacing) +
					this._vertPadding -
					dom.clientHeight;
				} else {
					dom.scrollLeft = this._scrollLeft = (lineIndex + 1) *
					(this._blockWidth + this._horiSpacing) +
					this._horiPadding -
					dom.clientWidth;
				}
			}
			this.doOnScroll();
		},
		
		_getContainerSize: function() {
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
		},
		
		_arrangeBlockDoms: function(from, move) {
			var container = this._container;
			var blockDom = container.firstChild;
			while (blockDom) {
				if (blockDom.itemIndex >= from) {
					blockDom.itemIndex = blockDom.itemIndex + move;
					var pos = this._getBlockPos(blockDom.itemIndex);
					$fly(blockDom).css({
						left: pos[0],
						top: pos[1]
					});
				}
				blockDom = blockDom.nextSibling;
			}
		},
		
		doOnResize: function() {
			if (!this._ready) return;
			
			if (this._lineSize && this._fillLine) {
				this.refresh();
			} else {
				this._arrangeBlockDoms();
			}
		},
		
		findItemDomByEvent: function(evt) {
			var target = evt.srcElement || evt.target;
			var target = target || evt, container = this._container;
			return $DomUtils.findParent(target, function(parentNode) {
				return parentNode.parentNode == container;
			});
		},
		
		findItemDomByPosition: function(pos) {
			var dom = this._dom, x = pos.x + dom.scrollLeft, y = pos.y +
			dom.scrollTop;
			var xIndex = parseInt((x - this._horiPadding - this._horiSpacing / 2) /
			(this._realBlockWidth + this._horiSpacing));
			var yIndex = parseInt((y - this._vertPadding - this._vertSpacing / 2) /
			(this._realBlockHeight + this._vertSpacing));
			var index = -1;
			if (this._blockLayout == "vertical") {
				if (xIndex > this._realLineSize - 1) xIndex = this._realLineSize - 1;
				index = this._realLineSize * yIndex + xIndex;
			} else {
				if (yIndex > this._realLineSize - 1) yIndex = this._realLineSize - 1;
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
		},
		
		initDraggingIndicator: function(indicator, draggingInfo, evt) {
			if (this._dragMode != "control") {
				var itemDom = draggingInfo.get("element");
				if (itemDom) {
					var contentDom = $DomUtils.xCreate({
						tagName: "div",
						className: "d-list-dragging-item"
					});
					$fly(itemDom).clone().css({
						left: 0,
						top: 0,
						position: "relative"
					}).appendTo(contentDom);
					indicator.set("content", contentDom);
				}
			}
		},
		
		setDraggingOverBlockDom: function(blockDom) {
			if (this._draggingOverBlockDom == blockDom) return;
			if (this._draggingOverBlockDom) $fly(this._draggingOverBlockDom).removeClass("block-drag-over");
			this._draggingOverBlockDom = blockDom;
			if (blockDom) $fly(blockDom).addClass("block-drag-over");
		},
		
		onDraggingSourceMove: function(draggingInfo, evt) {
			var dropMode = this._dropMode;
			var targetObject = draggingInfo.get("targetObject");
			var insertMode, refObject, itemDom;
			if (dropMode != "onControl") {
				var pos = this.getMousePosition(evt);
				blockItem = this.findItemDomByPosition(pos);
				if (blockItem &&
				$fly(blockItem).data("item") ==
				draggingInfo.get("object")) {
					blockItem = null;
				}
				
				if (blockItem) {
					if (dropMode == "insertItems") {
						if (this._blockLayout == "vertical") insertMode = (blockItem._dropX < (this._realBlockWidth / 2)) ? "before" : "after";
						else insertMode = (blockItem._dropY < (this._realBlockHeight / 2)) ? "before" : "after";
					} else if (dropMode == "onOrInsertItems") {
						if (this._blockLayout == "vertical") {
							if (blockItem._dropX < 4) insertMode = "before";
							else if (blockItem._dropX > (this._realBlockWidth - 4)) insertMode = "after";
						} else {
							if (blockItem._dropY < 4) insertMode = "before";
							else if (blockItem._dropY > (this._realBlockHeight - 4)) insertMode = "after";
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
			
			var accept = (draggingInfo.isDropAcceptable(this._droppableTags) &&
			!(dropMode == "onItem" && targetObject == null));
			draggingInfo.set({
				targetObject: targetObject,
				insertMode: insertMode,
				refObject: refObject,
				accept: accept
			});
			
			var eventArg = {
				draggingInfo: draggingInfo,
				event: evt,
				processDefault: true
			};
			this.fireEvent("onDraggingSourceMove", this, eventArg);
			
			if (accept && eventArg.processDefault) {
				this.setDraggingOverBlockDom(blockItem);
				this.showDraggingInsertIndicator(draggingInfo, insertMode, blockItem);
			}
			return eventArg.processDefault;
		},
		
		onDraggingSourceOut: function(draggingInfo, evt) {
			$invokeSuper.call(this, arguments);
			this.setDraggingOverBlockDom();
			this.showDraggingInsertIndicator();
		},
		
		showDraggingInsertIndicator: function(draggingInfo, insertMode, blockDom) {
			var insertIndicator = dorado.widget.blockview.getDraggingInsertIndicator(this._blockLayout);
			var $insertIndicator = $fly(insertIndicator);
			if (insertMode) {
				var container = this._container;
				if (this._blockLayout == "vertical") {
					var left;
					if (insertMode == "before") {
						left = blockDom.offsetLeft -
						parseInt(this._horiSpacing / 2);
					} else {
						left = blockDom.offsetLeft +
						blockDom.offsetWidth +
						parseInt(this._horiSpacing / 2);
					}
					$insertIndicator.height(blockDom.offsetHeight).left(left - 1).top(blockDom.offsetTop);
				} else {
					var top;
					if (insertMode == "before") {
						top = blockDom.offsetTop -
						parseInt(this._vertSpacing / 2);
					} else {
						top = blockDom.offsetTop +
						blockDom.offsetHeight +
						parseInt(this._vertSpacing / 2);
					}
					$insertIndicator.width(blockDom.offsetWidth).top(top - 1).left(blockDom.offsetLeft);
				}
				$insertIndicator.show().appendTo(container);
			} else {
				$insertIndicator.hide().appendTo($DomUtils.getInvisibleContainer());
			}
		},
		
		onDraggingSourceDrop: function(draggingInfo, evt) {
			this.showDraggingInsertIndicator();
			dorado.widget.RowList.prototype.onDraggingSourceDrop.apply(this, arguments);
		},
		
		processItemDrop: dorado.widget.RowList.prototype.processItemDrop
	});
	
	dorado.widget.blockview.getDraggingInsertIndicator = function(direction) {
		var code = (direction == "horizontal") ? 'h' : 'v';
		var indicator = this["_draggingInsertIndicator-" + code];
		if (indicator == null) {
			indicator = $DomUtils.xCreate({
				tagName: "div",
				className: "i-block-dragging-insert-indicator-" + code +
				" d-block-dragging-insert-indicator-" +
				code
			});
			this["_draggingInsertIndicator-" + code] = indicator;
		}
		return indicator;
	};
	
})();
