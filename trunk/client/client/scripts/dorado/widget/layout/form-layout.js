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

	var IGNORE_PROPERTIES = ["colSpan", "rowSpan", "align", "vAlign"];

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 表单式布局管理器。
	 * <p>
	 * 一般用于辅助表单的定义。
	 * 在使用时可以为表单式布局指定好列数，以及每一列的宽度。然后根据控件被添加进布局管理器的顺序依次进行排列。
	 * 同时，开发人员还可以在排列的过程中为特定的控件指定跨行或跨列的特殊规则。
	 * </p>
	 * <p>
	 * 下图就是一个比较典型的表单式布局。注意其中的Contrl4被设定为跨两行，Control6被设定为跨两列。
	 * 而Control8由于被设定为跨两列，而原位置的空间又不足，因此自动被安排到了下一个位置。
	 * </p>
	 * <img class="clip-image" src="images/form-layout-1.gif">
	 * <p>
	 * FormLayout的布局条件支持的具体子属性包括:
	 * <ul>
	 * <li>padding    -    {int} 布局区域内四周的留白的大小，像素值。</li>
	 * <li>colSpan    -    {int} 该区域的列跨度，即占据几列的宽度。默认值为1。</li>
	 * <li>rowSpan    -    {int}  该区域的行跨度，即占据几行的高度。默认值为1。</li>
	 * </ul>
	 * </p>
	 * @shortTypeName Form
	 * @extends dorado.widget.layout.Layout
	 */
	dorado.widget.layout.FormLayout = $extend(dorado.widget.layout.Layout, /** @scope dorado.widget.layout.FormLayout.prototype */ {
		$className: "dorado.widget.layout.FormLayout",
		_className: "d-form-layout",

		ATTRIBUTES: /** @scope dorado.widget.layout.FormLayout.prototype */ {

			regionClassName: {
				defaultValue: "d-form-layout-region"
			},

			/**
			 * 表单布局的分栏方式。
			 * <p>
			 * 定义分栏方式的基本格式为: 列1的宽度[列1的ClassName],列2的宽度[列2的ClassName],.....
			 * </p>
			 * <p>
			 * 其中列的宽度既可以使用像素值，也可以使用'*'表示中充满剩余宽度。<br>
			 * 列的ClassName不是必须给定的信息，可以省略。如果要省略某列的ClassName，请注意一同省略'['和']'。
			 * </p>
			 * @type String
			 * @attribute
			 * @default "*,*"
			 *
			 * @example
			 * // 下面的例子表示将FormLayout，设置为具有两个列。
			 * // 其中第一列的宽度为80，并且指定第一列的布局区域全部使用label-region作为CSS ClassName。
			 * // 第二列的占据剩余的宽度，并且指定第二列的布局区域全部使用input-region作为CSS ClassName。
			 * new dorado.widget.layout.FormLayout({
			 * 	cols: "80[label-region],*[input-region]"
			 * });
			 */
			cols: {
				defaultValue: "*,*"
			},

			/**
			 * 默认的行高。
			 * @type int
			 * @attribute
			 * @default 22
			 */
			rowHeight: {
				defaultValue: 22
			},

			/**
			 * 表单列之间的留白大小。像素值。
			 * @type int
			 * @attribute
			 * @default 6
			 */
			colPadding: {
				defaultValue: 6
			},

			/**
			 * 表单行之间的留白大小。像素值。
			 * @type int
			 * @attribute
			 * @default 6
			 */
			rowPadding: {
				defaultValue: 6
			},

			padding: {
				defaultValue: 8
			},

			/**
			 * 是否将表单的宽度自动扩展为撑满容器。
			 * @type boolean
			 * @attribute
			 */
			stretchWidth: {}
		},

		constructor: function(config) {
			this._useTable = true; //dorado.Browser.msie && dorado.Browser.version < 8;
			this._currentUseTable = false;
			this._useBlankRow = true; // !dorado.Browser.webkit;
			$invokeSuper.call(this, [config]);
		},

		createDom: function() {
			this._currentUseTable = this._useTable;
			if (this._useTable) {
				return $DomUtils.xCreate({
					tagName: "TABLE",
					className: this._className,
					cellSpacing: 0,
					cellPadding: 0,
					content: "^TBODY"
				});
			}
			else {
				return $DomUtils.xCreate({
					tagName: "DIV",
					className: this._className
				});
			}
		},

		refreshTableLayout: function(dom) {

			function isSameGrid(oldGrid, newGrid) {
				if (!oldGrid) {
					return false;
				}
				if (oldGrid.length != newGrid.length) {
					return false;
				}

				var same = true;
				for(var i = 0; i < newGrid.length && same; i++) {
					var oldRow = oldGrid[i], newRow = newGrid[i];
					if (oldRow == null || oldRow.length != newRow.length) {
						same = false;
						break;
					}

					for(var j = 0; j < newRow.length; j++) {
						var oldRegion = oldRow[j], newRegion = newRow[j];
						if (oldRegion == null && newRegion == null) {
							continue;
						}
						if (oldRegion == null || newRegion == null ||
							oldRegion.colSpan != newRegion.colSpan ||
							oldRegion.rowSpan != newRegion.rowSpan ||
							oldRegion.regionIndex != newRegion.regionIndex) {
							same = false;
							break;
						}
					}
				}
				return same;
			}

			var tbody;
			var grid = this.precalculateRegions();
			var structureChanged = !isSameGrid(this._grid, grid);
			if (structureChanged) {
				this._domCache = {};
				this._grid = grid;

				tbody = dom.tBodies[0];
				for(var i = 0, rowNum = tbody.childNodes.length, row; i < rowNum; i++) {
					row = tbody.childNodes[i];
					for(var j = 0, cellNum = row.childNodes.length; j < cellNum; j++) {
						var cell = row.childNodes[j];
						if (cell.firstChild) cell.removeChild(cell.firstChild);
					}
				}
				$fly(tbody).remove();

				tbody = document.createElement("TBODY");
				dom.appendChild(tbody);
			}
			else {
				tbody = dom.tBodies[0];
				grid = this._grid;
			}

			this.resizeTableAndCols();

			var realColWidths = this._realColWidths;
			if (this._useBlankRow) {
				if (structureChanged) {
					var tr = document.createElement("TR");
					tr.style.height = 0;
					for(var i = 0; i < realColWidths.length; i++) {
						var td = document.createElement("TD");
						td.style.width = realColWidths[i] + "px";
						tr.appendChild(td);
					}
					tbody.appendChild(tr);
				}
				else {
					var tr = tbody.childNodes[0];
					for(var i = 0; i < realColWidths.length; i++) {
						var td = tr.childNodes[i];
						td.style.width = realColWidths[i] + "px";
					}
				}
			}

			var rowIndexOffset = ((this._useBlankRow) ? 1 : 0), index = -1;
			for(var row = 0; row < grid.length; row++) {
				var tr;
				if (structureChanged) {
					tr = document.createElement("TR");
					if (row == 0) {
						tr.className = "d-form-layout-row first-row";
					}
					else {
						tr.className = "d-form-layout-row";
					}
					tbody.appendChild(tr);
				}
				else {
					tr = tbody.childNodes[row + rowIndexOffset];
				}

				if (!dorado.Browser.webkit) {
					tr.style.height = this._rowHeight + "px";
				}

				var cols = grid[row], colIndex = 0;
				for(var col = 0; col < cols.length; col++) {
					var region = grid[row][col];
					if (region && region.regionIndex <= index) {
						continue;
					}

					var td;
					if (structureChanged) {
						td = this.createTableRegionContainer(region);
						if (dorado.Browser.webkit) {
							td.style.height = this._rowHeight + "px";
						}
						tr.appendChild(td);
					}
					else {
						td = tr.childNodes[colIndex];
					}
					colIndex++;

					var cls = this._colClss[col];
					if (cls) $fly(td).addClass(cls);

					// real cell
					if (region) {
						index = region.regionIndex;

						region.autoWidthAdjust = region.autoHeightAdjust = 0;
						if ((col + region.colSpan) < cols.length) {
							region.autoWidthAdjust = 0 - this._colPadding;
						}
						if ((row + region.rowSpan) < grid.length) {
							region.autoHeightAdjust = 0 - this._rowPadding;
						}

						td.colSpan = region.colSpan;
						td.rowSpan = region.rowSpan;

						var w = 0;
						if (region.colSpan > 1) {
							var endIndex = region.colIndex + region.colSpan;
							for(var j = region.colIndex; j < endIndex; j++) {
								w += realColWidths[j];
							}
						}
						else {
							w = realColWidths[region.colIndex];
						}
						region.width = w;
						// td.style.width = w + "px";
						if (dorado.Browser.msie && dorado.Browser.version < 8) td.style.paddingTop = "0px";
						td.style.paddingBottom = (-region.autoHeightAdjust || 0) + "px";

						var useControlWidth = region.control.getAttributeWatcher().getWritingTimes("width") && region.control._width != "auto";
						this.renderControl(region, td, !useControlWidth, false);
					}
				}
			}
		},

		createTableRegionContainer: function(region) {
			var dom = this.getRegionDom(region);
			if (!dom) {
				dom = document.createElement("TD");
				if (region) this._domCache[region.id] = dom;
			}
			else if (dom.firstChild) {
				dom.removeChild(dom.firstChild);
			}
			dom.className = this._regionClassName;

			if (region) {
				var $dom = $fly(dom), constraint = region.constraint;
				if (constraint.className) $dom.addClass(constraint.className);
				if (region.colIndex == 0) $dom.addClass("first-cell");
				if (constraint.align) dom.align = constraint.align;
				if (constraint.vAlign) dom.vAlign = constraint.vAlign;
				var css = dorado.Object.apply({}, constraint, function(p, v) {
					if (IGNORE_PROPERTIES.indexOf(p) >= 0) return false;
				});
				$dom.css(css);
			}
			return dom;
		},

		initColInfos: function() {
			this._cols = this._cols || "*";
			var colWidths = this._colWidths = [];
			var colClss = this._colClss = [];
			var dynaColCount = 0, fixedWidth = 0;
			this._cols.split(',').each(function(col, i) {
				var w, cls, ind = col.indexOf('[');
				if (ind > 0) {
					w = col.substring(0, ind);
					cls = col.substring(ind + 1);
					if (cls.charAt(cls.length - 1) == ']') {
						cls = cls.substring(0, cls.length - 1);
					}
				}
				else {
					w = col;
				}

				colClss[i] = cls;
				if (w == '*') {
					colWidths.push(-1);
					dynaColCount++;
				}
				else {
					w = parseInt(w);
					colWidths.push(w);
					fixedWidth += (w || 0);
				}
			});
			this.colCount = colWidths.length;
			this.dynaColCount = dynaColCount;
			this.fixedWidth = fixedWidth;
		},

		precalculateRegions: function() {

			function precalculateRegion(grid, region) {

				function doTestRegion() {
					for(var row = rowIndex; row < rowIndex + rowSpan && row < grid.length; row++) {
						for(var col = colIndex; col < colIndex + colSpan; col++) {
							if (grid[row][col]) return false;
						}
					}
					return true;
				}

				var previousRegion = this.getPreviousRegion(region);
				var pRegionIndex = -1, pRowIndex = 0, pColIndex = -1, pColSpan = 1;
				if (previousRegion) {
					pRegionIndex = previousRegion.regionIndex;
					pRowIndex = previousRegion.rowIndex;
					pColIndex = previousRegion.colIndex;
				}

				var constraint = region.constraint;
				var rowIndex = pRowIndex, colIndex = pColIndex;
				var colSpan = ((constraint.colSpan > this.colCount) ? this.colCount : constraint.colSpan) || 1;
				var rowSpan = constraint.rowSpan || 1;
				do {
					colIndex++;
					if (colIndex + colSpan > this.colCount) {
						// go to next row
						rowIndex++;
						colIndex = 0;
					}
				}
				while(!doTestRegion());

				for(var row = 0; row < rowSpan; row++) {
					if ((rowIndex + row) >= grid.length) grid.push(new Array(this.colCount));
					for(var col = 0; col < colSpan; col++) {
						grid[rowIndex + row][colIndex + col] = region;
					}
				}

				region.regionIndex = pRegionIndex + 1;
				region.colIndex = colIndex;
				region.rowIndex = rowIndex;
				region.colSpan = colSpan;
				region.rowSpan = rowSpan;
			}

			this.initColInfos();

			var grid = [];
			var regions = this._regions.items, region, constraint;
			for (var i = 0, len = regions.length; i < len; i++) {
				region = regions[i];
				constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					region.regionIndex = -1;
					continue;
				}
				constraint.colSpan = parseInt(constraint.colSpan);
				constraint.rowSpan = parseInt(constraint.rowSpan);
				precalculateRegion.call(this, grid, region);
			}
			return grid;
		},

		resizeTableAndCols: function() {
			var realColWidths = this._realColWidths;
			if (!realColWidths) {
				this._realColWidths = realColWidths = [];
			}

			var table = this.getDom(), padding = parseInt(this._padding) || 0, colPadding = this._colPadding || 0;
			var containerWidth = (table.parentNode) ? (jQuery(table.parentNode).width() - padding * 2) : 0;
			if (!(containerWidth >= 0) || containerWidth > 10000) containerWidth = 0;
			if (!containerWidth) return;

			if (this._stretchWidth || this.dynaColCount > 0) {
				table.style.width = (containerWidth - $fly(table).edgeWidth()) + "px";
			}
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				table.style.margin = padding + "px";
			}
			else {
				table.style.padding = padding + "px";
			}

			containerWidth -= colPadding * (this._colWidths.length - 1);
			for(var i = 0; i < this._colWidths.length; i++) {
				var w = this._colWidths[i];
				if (this.dynaColCount > 0) {
					if (w == -1) {
						w = parseInt((containerWidth - this.fixedWidth) / this.dynaColCount);
					}
					w = (w < 0) ? 0 : w;
				}
				else if (this._stretchWidth) {
					w = parseInt(w * containerWidth / this.fixedWidth);
				}
				if (i < this._colWidths.length - 1) w += colPadding;
				realColWidths[i] = w;
			}
		},

		refreshDivLayout: function(dom) {
			
			function getOrCreateChild(parentNode, index, tagName) {
				var child, refChild;
				if (index < parentNode.childNodes.length) {
					child = refChild = parentNode.childNodes[index];
				}
				if (!child || child.tagName != tagName) {
					child = (typeof tagName == "function") ? tagName(index) : ((tagName.constructor == String) ? document.createElement(tagName) : this.xCreate(tagName));
					(refChild) ? parentNode.insertBefore(child, refChild) : parentNode.appendChild(child);
				}
				return child;
			}
			
			var grid = this.precalculateRegions();

			var realColWidths = this._realColWidths;
			if (!realColWidths) {
				this._realColWidths = realColWidths = [];
			}

			var padding = parseInt(this._padding) || 0, colPadding = this._colPadding || 0;
			var containerWidth = (dom.parentNode) ? (jQuery(dom.parentNode).width() - padding * 2) : 0;
			if (!(containerWidth >= 0) || containerWidth > 10000) containerWidth = 0;

			if (this._stretchWidth || this.dynaColCount > 0) {
				dom.style.width = containerWidth + "px";
			}
			dom.style.padding = padding + "px";

			containerWidth -= colPadding * (this._colWidths.length - 1);
			for(var i = 0; i < this._colWidths.length; i++) {
				var w = this._colWidths[i];
				if (this.dynaColCount > 0) {
					if (w == -1) {
						w = parseInt((containerWidth - this.fixedWidth) / this.dynaColCount);
					}
					w = (w < 0) ? 0 : w;
				}
				else if (this._stretchWidth) {
					w = parseInt(w * containerWidth / this.fixedWidth);
				}
				realColWidths[i] = w;
			}

			var index = -1, domIndex = 0;
			for(var row = 0; row < grid.length; row++) {
				var cols = grid[row];
				for(var col = 0; col < cols.length; col++) {
					var region = cols[col];
					if (!region) continue;
					if (region.regionIndex <= index) {
						if (region.regionIndex == -1) {
							var control = region.control;
							if (control._dom) $DomUtils.getUndisplayContainer().appendChild(control._dom);
						}
						continue;
					}
					index = region.regionIndex;

					var constraint = region.constraint;
					constraint.padding = parseInt(constraint.padding) || 0;
					constraint.colSpan = parseInt(constraint.colSpan) || 0;

					var div = getOrCreateChild(dom, domIndex++, "DIV"), control = region.control;
					div.className = this._regionClassName;
					div.style.display = "inline-block";
					div.style.verticalAlign = "middle";
					div.style.position = "relative";

					var cls = this._colClss[col];
					if (cls) $fly(div).addClass(cls);
					
					var width = 0;
					if (constraint.colSpan > 1) {
						for(var _col = col; _col < (col + constraint.colSpan) && _col < realColWidths.length; _col++) {
							width += realColWidths[_col];
							if (_col > col) width += this._colPadding;
						}
					}
					else {
						width = realColWidths[col];
					}
					div.style.padding = constraint.padding + "px";
					if (col > 0) {
						div.style.marginLeft = this._colPadding + "px";
					}

					if (row > 0) {
						var rowPaddingAdj = 4; // 尚未弄清为什么inline-block元素的垂直方向上总有4px的间隙
						div.style.marginTop = (this._rowPadding - rowPaddingAdj) + "px";
					}

					region.width = width - constraint.padding * 2;
					// region.height = this._rowHeight - constraint.padding * 2;
					div.style.width = region.width + "px";
					// div.style.height =region.height + "px";

					var useControlWidth = control.getAttributeWatcher().getWritingTimes("width") && control._width != "auto";
					if (div.firstChild !== control.getDom()) {
						this.renderControl(region, div, !useControlWidth, false);
					}
					else {
						this.resetControlDimension(region, div, !useControlWidth, false);
					}
				}
				getOrCreateChild(dom, domIndex++, "BR");
			}

			$DomUtils.removeChildrenFrom(dom, domIndex);
		},

		preprocessLayoutConstraint: function(layoutConstraint, control) {
			var layoutConstraint = $invokeSuper.call(this, arguments);
			if (layoutConstraint.rowSpan > 1) this._useTable = true;
			return layoutConstraint;
		},

		refreshDom: function(dom) {
			this.ensureControlsInited();
			
			var oldDom;
			if (this._useTable != this._currentUseTable) {
				oldDom = dom;
				if (!this._dom) this._dom = dom = this.createDom();
			}

			if (this._currentUseTable) {
				this.refreshTableLayout(dom);
			}
			else {
				this.refreshDivLayout(dom);
			}

			if (oldDom) {
				$fly(oldDom).remove();
			}
		}
	});

	var p = dorado.widget.layout.FormLayout.prototype;
	p.onAddControl = p.onRemoveControl = p.doRefreshRegion = function() {
		if (!this._attached || this._disableRendering) return;
		this.refresh();
	};
})();
