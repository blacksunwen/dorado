/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

/**
 * @name dorado.widget.layout
 * @namespace 包含dorado中布局管理器的命名空间。
 */
dorado.widget.layout = {};

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 布局管理器的抽象类。
 * @abstract
 * @extends dorado.AttributeSupport
 * @param {Object} [config] 包含对布局管理器的配置信息的JSON对象。
 */
dorado.widget.layout.Layout = $extend(dorado.AttributeSupport, /** @scope dorado.widget.layout.Layout.prototype */ {
	$className: "dorado.widget.layout.Layout",

	/**
	 * @name dorado.widget.layout.Layout#createDom
	 * @function
	 * @protected
	 * @abstract
	 * @return {HTMLElement} 布局管理器对应的DOM对象。
	 * @description 创建布局管理器对应的DOM对象。
	 */
	/**
	 * @name dorado.widget.layout.Layout#refreshDom
	 * @function
	 * @protected
	 * @abstract
	 * @description 根据布局管理器自身的属性设定来刷新DOM对象。
	 * <p>
	 * 此方法会在布局管理器每一次被刷新时调用，因此那些设置DOM对象的颜色、字体、尺寸的代码适合放置在此方法中。
	 * </p>
	 * @param {HTMLElement} dom 布局管理器对应的DOM对象。
	 */
	/**
	 * @name dorado.widget.layout.Layout#doOnControlSizeChange
	 * @function
	 * @protected
	 * @abstract
	 * @param {dorado.widget.Control} control 尺寸发生改变的控件
	 * @description 当布局区域内的控件尺寸发生改变时由外部激活的方法，供子类复写的方法。
	 * @see dorado.widget.layout.Layout#onControlSizeChange
	 */
	/**
	 * @name dorado.widget.layout.Layout#onAddControl
	 * @function
	 * @protected
	 * @abstract
	 * @description 当有新的控件被添加到布局管理器中时被激活的方法。
	 * @param {dorado.widget.Control} control 要添加的控件。
	 * @return {Object} 新增的布局区域描述对象。
	 * @see dorado.widget.layout.Layout#addControl
	 */
	/**
	 * @name dorado.widget.layout.Layout#onRemoveControl
	 * @function
	 * @protected
	 * @abstract
	 * @description 当将要从布局管理器中移除一个子控件时被激活的方法。
	 * @param {dorado.widget.Control} control 要移除的子控件。
	 * @see dorado.widget.layout.Layout#removeControl
	 */
	/**
	 * @name dorado.widget.layout.Layout#doRefreshRegion
	 * @function
	 * @protected
	 * @abstract
	 * @description 刷新指定的布局区域，供子类复写的方法。
	 * @param {Object} region 要刷新的布局区域。
	 * @see dorado.widget.layout.Layout#refreshControl
	 */
	// =====

	ATTRIBUTES: /** @scope dorado.widget.layout.Layout.prototype */ {

		/**
		 * 四周的留白大小。像素值。
		 * @type int
		 * @attribute
		 */
		padding: {},

		/**
		 * 此布局管理器所隶属的容器控件。
		 * @type dorado.widget.Container
		 * @attribute readOnly
		 */
		container: {
			setter: function(container) {
				if (this._container != container) {
					this._domCache = {};
					this._container = container;
				}
			}
		},

		/**
		 * 指示此控件是否已经渲染过。
		 * @type boolean
		 * @attribute readOnly
		 */
		rendered: {
			readOnly: true
		},

		/**
		 * 指示布局管理器所对应的HTML元素被真正的添加(相当于appendChild)到HTML的dom树中。
		 * @type boolean
		 * @attribute readOnly
		 */
		attached: {
			readOnly: true
		}
	},

	constructor: function(config) {
		this._regions = new dorado.util.KeyedArray(function(region) {
			return region.control._uniqueId;
		});
		$invokeSuper.call(this, [config]);
		if (config) this.set(config);
	},

	/**
	 * 返回布局管理器对应的DOM对象。
	 * @return {HTMLElement} 布局管理器对应的DOM对象。
	 */
	getDom: function() {
		if (!this._dom) this._dom = this.createDom();
		return this._dom;
	},

	getRegionDom: function(region) {
		if (region) return this._domCache[region.id];
	},
	
	ensureControlInited: function(control, region) {
		if (control._lazyInit) {
			control._lazyInit();
			region.constraint = this.preprocessLayoutConstraint(control._layoutConstraint, control);
		}
	},
	
	ensureControlsInited: function() {
		if (this._lazyInitControls) {
			var control;
			for (var i = 0, len = this._lazyInitControls.length; i < len; i++) {
				control = this._lazyInitControls[i];
				this.ensureControlInited(control, this.getRegion(control));
			}
			delete this._lazyInitControls;
		}
	},

	/**
	 * 刷新布局。
	 */
	refresh: function() {
		if (this._duringRefreshDom) return;
		this._duringRefreshDom = true;
		if (this._attached) {		
			var regions = this._regions.items, region, control;
			for (var i = 0, len = regions.length; i < len; i++) {
				region = regions[i];
				control = region.control;
				region.constraint = this.preprocessLayoutConstraint(control._layoutConstraint, control);
			}
			
			delete this.overflowedDoms;
			this.refreshDom(this.getDom());
			
			if (this._container) {
				if (this.overflowedDoms) {
					var layout = this;
					this._container.bind("onScroll.layout", function() {
						layout.onScroll();
					});
				}
				else {
					this._container.unbind("onScroll.layout");
				}
			}
			this._rendered = true;
		}
		this._duringRefreshDom = false;
	},

	/**
	 * 当布局管理器所属的容器控件被真正的添加(相当于appendChild)到HTML的dom树中时激活的方法。
	 * @param {HTMLElement} containerElement 控件容器的DOM对象。
	 * @see dorado.widget.Control.onAttachToDocument
	 */
	onAttachToDocument: function(containerElement) {
		if (!this._attached) {
			if (this._regions.size == 0) return;
			this._attached = true;
			var dom = this.getDom();
			if (dom.parentNode != containerElement) containerElement.appendChild(dom);
			
			// TODO: 导致container中的layout重新布局
			// this.refresh();
		}
	},

	/**
	 * 当布局管理器所属的容器控件被从(相当于removeChild)到HTML的dom树中分离时激活的方法。
	 * @see dorado.widget.Control.onDetachFromDocument
	 */
	onDetachFromDocument: function() {
		if (this._attached) {
			this._attached = false;
			var regions = this._regions.items;
			for (var i = 0, len = regions.length; i < len; i++) {
				regions[i].control.onDetachFromDocument();
			}
		}
	},

	/**
	 * 返回指定的布局区域的前一个布局区域。
	 * @protected
	 * @param {Object} region 指定的布局区域。
	 * @return {Object} 前一个布局区域。
	 */
	getPreviousRegion: function(region) {
		var regions = this._regions.items;
		var i = regions.indexOf(region);
		for (i--; i >= 0; i--) {
			region = regions[i];
			if (region.constraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) return region;
		}
		return null;
	},

	/**
	 * 返回指定的布局区域的下一个布局区域。
	 * @protected
	 * @param {Object} region 指定的布局区域。
	 * @return {Object} 下一个布局区域。
	 */
	getNextRegion: function(region) {
		var regions = this._regions.items;
		var i = regions.indexOf(region), len = regions.length;
		for (i++; i < len; i++) {
			region = regions[i];
			if (region.constraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) return region;
		}
		return null;
	},

	preprocessLayoutConstraint: function(layoutConstraint, control) {
		if (!control._visible && control._hideMode == "display") {
			layoutConstraint = dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT;
		}
		return layoutConstraint || {};
	},

	/**
	 * 向布局管理器中添加一个控件。
	 * @param {dorado.widget.Control} control 要添加的控件。
	 * @param {Object} [layoutConstraint] 布局条件。
	 * @return {Object} 新增的布局区域描述对象。
	 */
	addControl: function(control) {
		var region = {
			id: dorado.Core.newId(),
			control: control,
			constraint: this.preprocessLayoutConstraint(control._layoutConstraint, control)
		};
		
		if (control._lazyInit) {
			if (!this._lazyInitControls) this._lazyInitControls = [];
			this._lazyInitControls.push(control);
		}
		this._regions.insert(region);
		control._parentLayout = this;
		if (this.onAddControl) this.onAddControl(control);
	},

	/**
	 * 从布局管理器中移除一个子控件。
	 * @param {dorado.widget.Control} control 要移除的子控件。
	 */
	removeControl: function(control) {
		control._parentLayout = null;
		if (this.onRemoveControl) this.onRemoveControl(control);
		var region = this._regions.removeKey(control._uniqueId);
		if (region && region.fakeDom) $fly(region.fakeDom).remove();
	},

	/**
	 * 从布局管理器中移除所有子控件。
	 */
	removeAllControls: function() {
		var regions = this._regions.items;
		for (var i = regions.length - 1; i >= 0; i--) {
			this.removeControl(regions[i].control);
		}
	},

	/**
	 * 禁止布局管理器的自动重渲染。
	 */
	disableRendering: function() {
		this._disableRendering = true;
	},

	/**
	 * 允许布局管理器的自动重渲染。
	 */
	enableRendering: function() {
		this._disableRendering = false;
	},

	resetControlDimension: function(region, regionDom, autoWidth, autoHeight) {
		var control = region.control, attrWatcher = control.getAttributeWatcher();
		var oldWidth = control._currentWidth, oldHeight = control._currentHeight;
		if (autoWidth && region.width !== undefined && (!control.ATTRIBUTES.width.independent || control._fixedWidth)) {
			control._realWidth = region.width + (region.autoWidthAdjust || 0);
		}
		if (autoHeight && region.height !== undefined && (!control.ATTRIBUTES.height.independent || control._fixedHeight)) {
			control._realHeight = region.height + (region.autoHeightAdjust || 0);
		}
		if (control._attached && (oldWidth != control._realWidth || oldHeight != control._realHeight)) {
			control.refresh();
		}
	},

	renderControl: function(region, regionDom, autoWidth, autoHeight) {
		this.resetControlDimension(region, regionDom, autoWidth, autoHeight);
		var control = region.control;
		if (!control._dom || control._dom.parentNode != regionDom) {
			this._ignoreControlSizeChange = true;
			if (region.fakeDom) {
				control.replace(region.fakeDom);
				$fly(region.fakeDom).remove();
				delete region.fakeDom;
			}
			else {
				control.render(regionDom);
			}
			this._ignoreControlSizeChange = false;
		}
	},

	getRegion: function(control) {
		return this._regions.get(control._uniqueId);
	},

	/**
	 * 刷新指定的控件对应的布局。
	 * 此方法一般不应在子类中复写，如有需要子类应该复写本类中的doRefreshControl方法。
	 * @protected
	 * @param {doraod.widget.Control|String} control 子控件或子控件的id。
	 * @see dorado.widget.layout.Layout#doRefreshRegion
	 */
	refreshControl: function(control) {
		var region = this.getRegion(control);
		if (region) {
			region.constraint = this.preprocessLayoutConstraint(control._layoutConstraint, control);

			var container = this._container, dom = this._dom;
			if (!container || !dom) return;

			if (container.isActualVisible()) {
				// TODO: 临时屏蔽测试，如果出现布局问题应首先怀疑此处
				this._ignoreControlSizeChange = true;
				if (this.doRefreshRegion) {
					var currentWidth = dom.offsetWidth, currentHeight = dom.offsetHeight;
					this.doRefreshRegion(region);
					if (currentWidth != dom.offsetWidth || currentHeight != dom.offsetHeight) {
						container.onContentSizeChange();
					}
				}
				this._ignoreControlSizeChange = false;
			}
			else {
				container.refresh(); // 由于container目前不可见，因此本次刷新动作实际会被搁置到可见时再执行。
			}
		}
	},

	/**
	 * 当布局管理器的尺寸发生改变时激活的方法。
	 * 此方法一般不应在子类中复写，如有需要子类应该复写本类中的doOnResize方法。
	 * @protected
	 * @see dorado.widget.Control#onResize
	 */
	onResize: function() {
		if (!this._attached || this._ignoreControlSizeChange || !this.doOnResize) return;

		//var clientSize = this._container.getContentContainerSize();
		//var clientWidth = clientSize[0], clientHeight = clientSize[1];
		//if (clientWidth > 10000) clientWidth = 0;
		//if (clientHeight > 10000) clientHeight = 0;
		//if (clientWidth == 0 && clientHeight == 0) return;
		this.doOnResize();
	},

	/**
	 * 当布局管理器的尺寸发生改变时激活的方法，供子类复写的方法。
	 * @protected
	 * @see dorado.widget.layout.Layout#onResize
	 * @see dorado.widget.Control#onResize
	 */
	doOnResize: function() {
		if (!this._duringRefreshDom) {
			this.refresh();
		}
	},

	/**
	 * 当布局区域内的控件尺寸发生改变时由外部激活的方法。
	 * 此方法一般不应在子类中复写，如有需要子类应该复写本类中的doOnControlSizeChange方法。
	 * @protected
	 * @param {dorado.widget.Control} control 尺寸发生改变的控件
	 * @param {boolean} delay 尺寸发生改变的控件
	 * @param {boolean} force 是否强制通知父容器
	 */
	onControlSizeChange: function(control, delay, force) {
		if (this._ignoreControlSizeChange) return;
		dorado.Toolkits.cancelDelayedAction(this, "$notifySizeChangeTimerId");

		var fn = function() {
			var container = this._container, dom = this._dom;
			if (!container || !dom) return;

			var currentWidth, currentHeight;
			if (!force && this.doOnControlSizeChange) {
				currentWidth = dom.offsetWidth;
				currentHeight = dom.offsetHeight;
			}
			if (this.doOnControlSizeChange) this.doOnControlSizeChange(control);

			if (force || currentWidth != dom.offsetWidth || currentHeight != dom.offsetHeight) {
				container.onContentSizeChange();
			}
		}

		var region = this.getRegion(control);
		if (region) {
			region.constraint = this.preprocessLayoutConstraint(control._layoutConstraint, control);
			if (delay) {
				dorado.Toolkits.setDelayedAction(this, "$onControlSizeChangeTimerId", fn, 200);
			}
			else {
				fn.call(this);
			}
		}
	},
	
	getFakeDomOffsetTop: function(fakeDom) {
		return fakeDom.offsetTop;
	},
	
	onScroll: function() {
		if (!this._lazyRenderChild || !this.overflowedDoms) return;
		
		dorado.Toolkits.setDelayedAction(this, "$onScrollTimerId", function() {
			var containerDom = this._dom.parentNode, overflowedDoms = this.overflowedDoms;
			for(var i = 0; i < overflowedDoms.length; i++) {
				var fakeDom = overflowedDoms[i];
				if (fakeDom && this.getFakeDomOffsetTop(fakeDom) < (containerDom.scrollTop + containerDom.clientHeight)) {
					this.refresh();
					break;
				}
			}
		}, 200);
	}
});

/**
 * 用于表示某控件不参与布局管理的特殊布局条件，即布局管理器将在渲染时忽略对该控件的处理。
 * @type String
 * @constant
 * @default "none"
 */
dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT = "none";

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 系统原生布局管理器。
 * @extends dorado.widget.layout.Layout
 * @param {Object} [config] 包含对布局管理器的配置信息的JSON对象。
 */
dorado.widget.layout.NativeLayout = $extend(dorado.widget.layout.Layout, /** @scope dorado.widget.layout.NativeLayout.prototype */ {
	$className: "dorado.widget.layout.NativeLayout",
	_className: "d-native-layout",

	ATTRIBUTES: /** @scope dorado.widget.layout.NativeLayout.prototype */ {
		
		lazyRenderChild: {},
		
		container: {
			setter: function(container) {
				if (this._container == container) return;
				this._domCache = {};
				this._container = container;
			}
		},

		/**
		 * 用于简化DOM元素style属性设置过程的虚拟属性。
		 * 此处用于赋值给style属性的对象是style文本值或一个结构与HTMLElement的style相似的JavaScript对象。
		 * @type Object|String
		 * @attribute
		 * @example
		 *
		 * // 当我们需要为DOM元素指定背景色和字体颜色时可以使用这样的style
		 * layout.set("style", {
		 * 	color : "yellow",
		 * 	backgroundColor : "blue"
		 * });
		 *
		 * @example
		 * layout.set("style", "color: yellow; background-color: blue");
		 */
		style: {
			setter: function(v) {
				if (typeof v == "string" || !this._style) {
					this._style = v;
				}
				else if (v) {
					dorado.Object.apply(this._style, v);
				}
			}
		}
	},

	createDom: function() {
		var dom = document.createElement("DIV");
		dom.className = this._className;
		return dom;
	},

	refreshDom: function(dom) {
		$fly(dom).css("padding", this._padding);

		if (this._style) {
			var style = this._style;
			if (typeof this._style == "string") {
				// 此段处理不能用jQuery.attr("style", style)替代，原因是该方法会覆盖DOM原有的inliine style设置。
				var map = {};
				jQuery.each(style.split(';'), function(i, section) {
					var v = section.split(':');
					map[jQuery.trim(v[0])] = jQuery.trim(v[1]);
				});
				style = map;
			}
			$fly(dom).css(style);
			delete this._style;
		}
		
		var containerDom = this._dom.parentNode;
		var regions = this._regions.items, region, control, overflowed;
		for (var i = 0, len = regions.length; i < len; i++) {
			region = regions[i];			

			control = region.control;
			fakeDom = region.fakeDom;
			if (region.constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
				if (control._dom && control._dom.parentNode != dom) {
					dom.appendChild(control._dom);
				}
				else if (!fakeDom) {
					region.fakeDom = fakeDom = document.createElement("DIV");
					fakeDom.style.display = "none";
					dom.appendChild(fakeDom);
				}
			}
			else if (!control._rendered) {
				if (!fakeDom) {
					if (containerDom && this._lazyRenderChild &&
							(overflowed || containerDom.scrollHeight - (containerDom.scrollTop + containerDom.clientHeight) > 5)) {
						region.fakeDom = fakeDom = document.createElement("DIV");

						var properHeight = control.getRealHeight();
						if (!properHeight) properHeight = FAKE_PROPER_HEIGHT;
						$fly(fakeDom).css({
							width: control.getRealWidth(),
							height: properHeight
						});
						
						dom.appendChild(fakeDom);
						
						if (!this.overflowedDoms) this.overflowedDoms = [];
						this.overflowedDoms.push(fakeDom);
						overflowed = true;
					}
				}
				else if (containerDom && this._lazyRenderChild &&
						(overflowed || this.getFakeDomOffsetTop(fakeDom) > (containerDom.scrollTop + containerDom.clientHeight))) {
					var properHeight = control.getRealHeight();
					if (!properHeight) properHeight = FAKE_PROPER_HEIGHT;
					$fly(fakeDom).css({
						width: control.getRealWidth(),
						height: properHeight
					});
					
					if (!this.overflowedDoms) this.overflowedDoms = [];
					this.overflowedDoms.push(fakeDom);
					overflowed = true;
				}
				
				if (!overflowed) {
					this.ensureControlInited(control, region);
				}
			}
			
			if (!overflowed) {
				this.renderControl(region, dom, false, false);
			}
		}
	},

	onAddControl: function(control) {
		if (!this._attached || this._disableRendering) return;
		this.refresh();
	},

	onRemoveControl: function(control) {
		if (!this._attached) return;
		control.unrender();
	},

	doRefreshRegion: function(region) {
		region.control.refresh();
	}
});

var FAKE_PROPER_HEIGHT = 120;
