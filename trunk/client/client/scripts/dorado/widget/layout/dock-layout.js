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

(function() {

	var defaultRegionPadding = 0;
	
	var getLastRegionFuncs = {};
	jQuery.each(["left", "right", "top", "bottom"], function(i, type) {
		getLastRegionFuncs[type] = function(region) {
			var regions = this._regions.items;
			var i = regions.indexOf(region);
			for (i--; i >= 0; i--) {
				region = regions[i];
				if (region.constraint.type == type) return region;
			}
			return null;
		};
	});
	
	var defaultConstraintsCache = {
		top: {
			type: "top",
			left: 0,
			right: 0,
			top: 0,
			bottom: undefined,
			anchorLeft: getLastRegionFuncs.left,
			anchorRight: getLastRegionFuncs.right,
			anchorTop: getLastRegionFuncs.top,
			anchorBottom: undefined
		},
		bottom: {
			type: "bottom",
			left: 0,
			right: 0,
			top: undefined,
			bottom: 0,
			anchorLeft: getLastRegionFuncs.left,
			anchorRight: getLastRegionFuncs.right,
			anchorTop: undefined,
			anchorBottom: getLastRegionFuncs.bottom
		},
		left: {
			type: "left",
			left: 0,
			right: undefined,
			top: 0,
			bottom: 0,
			anchorLeft: getLastRegionFuncs.left,
			anchorRight: undefined,
			anchorTop: getLastRegionFuncs.top,
			anchorBottom: getLastRegionFuncs.bottom
		},
		right: {
			type: "right",
			left: undefined,
			right: 0,
			top: 0,
			bottom: 0,
			anchorLeft: undefined,
			anchorRight: getLastRegionFuncs.right,
			anchorTop: getLastRegionFuncs.top,
			anchorBottom: getLastRegionFuncs.bottom
		},
		center: {
			type: "center",
			left: 0,
			right: 0,
			top: 0,
			bottom: 0,
			anchorLeft: getLastRegionFuncs.left,
			anchorRight: getLastRegionFuncs.right,
			anchorTop: getLastRegionFuncs.top,
			anchorBottom: getLastRegionFuncs.bottom
		}
	};
	
	function getDefaultConstraint(type) {
		return dorado.Object.apply({}, defaultConstraintsCache[type || "center"]);
	}
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 停靠式布局管理器。
	 * <p>
	 * 停靠式布局将整个容器的空间划分为五种，分别是left、top、right、bottom和center，对应五种停靠方式。<br>
	 * 当一个控件被分配了上述五种停靠方式中的一种时，它总是尽可能占据那个区域的所有空间，然后将剩余的空间留给后面的控件。见下图。
	 * </p>
	 * <img class="clip-image" src="images/dock-layout-1.gif">
	 * <p>
	 * DockLayout中允许除center之外的每一种停靠方式出现多次，具体空间被如何划分取决于它们被添加到布局管理器的顺序。<br>
	 * 而center停靠只允许出现一次，因为center停靠总是会完整的占据剩余的所有空间。因此一旦被出现了center停靠，整个布局空间将不会再有任何剩余的空间了。<br>
	 * 在DockLayout中center停靠并不需要总是在最后被定义，系统会在实际的处理过程中自动将center停靠方式移到最后处理。
	 * </p>
	 * <p>
	 * DockLayout的布局条件支持两种定义方式：
	 * <ul>
	 * <li>当布局条件为字符串时，该字符串应有如下5种取值，默认将按照center来进行处理：
	 * 	<ul>
	 * 	<li>left - 表示该区域自动停靠到容器的左边。</li>
	 * 	<li>top - 表示该区域自动停靠到容器的顶边。</li>
	 * 	<li>right - 表示该区域自动停靠到容器的右边。</li>
	 * 	<li>bottom - 表示该区域自动停靠到容器的底边。</li>
	 * 	<li>center - 表示该区域占据容器中部剩余的位置。</li>
	 * 	</ul>
	 * </li>
	 * <li>
	 * 当布局条件为JSON对象时，其中将支持下列子属性:
	 * 	<ul>
	 * 	<li>padding	-	{int} 布局区域内四周的留白的大小，像素值。</li>
	 * 	<li>type	-	{String} 锚定方式，表示该区域自动停靠的方式，有left、top、right、bottom、center这5中取值，默认将按照center来进行处理。</li>
	 * 	</ul>
	 * </li>
	 * </ul>
	 * </p>
	 * @shortTypeName Default ; Dock
	 * @extends dorado.widget.layout.AnchorLayout
	 */
	dorado.widget.layout.DockLayout = $extend(dorado.widget.layout.AnchorLayout, /** @scope dorado.widget.layout.DockLayout.prototype */ {
		$className: "dorado.widget.layout.DockLayout",
		_className: "d-dock-layout",
		
		ATTRIBUTES: /** @scope dorado.widget.layout.DockLayout.prototype */ {
			
			/**
			 * 布局区域之间空隙的大小。
			 * @type int
			 * @attribute
			 */
			regionPadding: {
				defaultValue: defaultRegionPadding
			}
		},
		
		preprocessLayoutConstraint: function(layoutConstraint, control) {
			if (!control._visible && control._hideMode == "display") {
				layoutConstraint = dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT;
			}
			if (layoutConstraint) {
				if (layoutConstraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					if (layoutConstraint.constructor == String) {
						layoutConstraint = getDefaultConstraint(layoutConstraint);
					} else {
						layoutConstraint = dorado.Object.apply(layoutConstraint, getDefaultConstraint(layoutConstraint.type));
					}
				}
			} else {
				layoutConstraint = dorado.Object.apply({}, getDefaultConstraint(null));
			}
			return layoutConstraint;
		},
		
		onAddControl: function(control) {
			var region = this.getRegion(control);
			if (region.constraint && region.constraint.type == "center") {
				var centerRegion = this._centerRegion;
				if (centerRegion) {
					var centerControl = centerRegion.control;
					if (!centerControl._layoutConstraint || typeof centerControl._layoutConstraint != "object") {
						centerControl._layoutConstraint = "top";
					} else {
						centerControl._layoutConstraint.type = "top";
					}
					dorado.Object.apply(centerRegion.constraint, getDefaultConstraint("top"));
				}
				this._centerRegion = region;
			}
			$invokeSuper.call(this, [control]);
		},
		
		onRemoveControl: function(control) {
			var region = this.getRegion(control);
			if (this._centerRegion === region && region) {
				delete this._centerRegion;
			}
			$invokeSuper.call(this, [control]);
		},
		
		refreshDom: function(dom) {
			var regions = this._regions;
			if (this._centerRegion && this._centerRegion !== regions.items[regions.size - 1]) {
				regions.remove(this._centerRegion);
				regions.append(this._centerRegion);
			}
			$invokeSuper.call(this, [dom]);
		},
		
		renderControl: function(region, regionDom, autoWidth, autoHeight) {
			switch (region.constraint.type) {
				case "top":
				case "bottom":{
					autoWidth = true;
					break;
				}
				case "left":
				case "right":{
					autoHeight = true;
					break;
				}
				default:
					{
						autoWidth = autoHeight = true;
					}
			}
			return $invokeSuper.call(this, [region, regionDom, autoWidth, autoHeight]);
		}
	});
	
	
	dorado.Toolkits.registerPrototype("layout", "Default", dorado.widget.layout.DockLayout);
	
})();
