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
	 * <li>padding	-	{int} 布局区域内四周的留白的大小，像素值。</li>
	 * <li>colSpan	-	{int} 该区域的列跨度，即占据几列的宽度。默认值为1。</li>
	 * <li>rowSpan	-	{int}  该区域的行跨度，即占据几行的高度。默认值为1。</li>
	 * </ul>
	 * </p>
	 * @shortTypeName Form
	 * @extends dorado.widget.layout.Layout
	 */
	dorado.widget.layout.FormLayout = $extend(dorado.widget.layout.Layout, /** @scope dorado.widget.layout.FormLayout.prototype */ {
		$className: "dorado.widget.layout.FormLayout",
		
		ATTRIBUTES: /** @scope dorado.widget.layout.FormLayout.prototype */ {
		
			className: {
				defaultValue: "d-form-layout"
			},
			
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
			 *
			 * @type int
			 * @attribute
			 * @default 6
			 */
			colPadding: {
				defaultValue: 16
			},
			
			/**
			 *
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
		
		constructor: function() {
			this._useBlankRow = !(dorado.Browser.safari || dorado.Browser.chrome);
			$invokeSuper.call(this, arguments);
		},
		
		createDom: function() {
			return $DomUtils.xCreateElement({
				tagName: "TABLE",
				cellSpacing: 0,
				cellPadding: 0,
				content: "^TBODY"
			});
		},
		
		refreshDom: function(dom) {
			if (this.precalculateRegions) this.precalculateRegions();
			
			var tbody = dom.tBodies[0];
			this._domCache = {};
			dom.removeChild(tbody);
			while (tbody.firstChild) {
				tbody.removeChild(tbody.firstChild);
			}
			dom.appendChild(tbody);
			
			this.resizeTableAndCols();
			
			var grid = this._grid, index = -1, realColWidths = this._realColWidths;
			if (this._useBlankRow) {
				var tr = document.createElement("TR");
				tr.style.height = 0;
				for (var i = 0; i < realColWidths.length; i++) {
					var td = document.createElement("TD");
					td.style.width = realColWidths[i] + "px";
					tr.appendChild(td);
				}
				tbody.appendChild(tr);
			}
			
			var realignRegions = [];
			for (var row = 0; row < grid.length; row++) {
				var tr = document.createElement("TR");
				tr.style.height = this._rowHeight + "px";
				tbody.appendChild(tr);
				
				var cols = grid[row], cellForRenders = [];
				for (var col = 0; col < cols.length; col++) {
					var region = grid[row][col];
					if (region && region.regionIndex <= index) continue;
					var td = this.createRegionContainer(region);
					var cls = this._colClss[col];
					if (cls) $fly(td).addClass(cls);
					tr.appendChild(td); // real cell
					if (region) {
						$fly(td).css(region.constraint);
						
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
							for (var j = region.colIndex; j < endIndex; j++) {
								w += realColWidths[j];
							}
						} else {
							w = realColWidths[region.colIndex];
						}
						region.width = w;
						td.style.width = w + "px";
						if (dorado.Browser.msie && dorado.Browser.version < '8') td.style.paddingTop = "0px";
						td.style.paddingBottom = (-region.autoHeightAdjust || 0) + "px";
						
						cellForRenders.push({
							cell: td,
							region: region
						});
					}
				}
				
				for (var i = 0; i < cellForRenders.length; i++) {
					var cellInfo = cellForRenders[i], td = cellInfo.cell, region = cellInfo.region;
					index = region.regionIndex;
					if (region.control._fixedHeight === false) {
						var controlDom = region.control.getDom();
						region.display = controlDom.style.display;
						controlDom.style.display = "none";
						realignRegions.push(region);
					} else {
						this.renderControl(region, td, true, false);
					}
				}
			}
			
			for (var i = 0; i < realignRegions.length; i++) {
				var region = realignRegions[i], td = this.createRegionContainer(region);
				region.height = td.clientHeight;
				controlDom.style.display = region.display;
				this.renderControl(region, td, true, true);
			}
		},
		
		createRegionContainer: function(region) {
			var dom = this.getRegionDom(region);
			if (!dom) {
				dom = document.createElement("TD");
				if (region) this._domCache[region.id] = dom;
			} else if (dom.firstChild) {
				dom.removeChild(dom.firstChild);
			}
			dom.className = this._regionClassName;
			
			if (region) {
				var $dom = $fly(dom), constraint = region.constraint;
				if (constraint.className) $dom.addClass(constraint.className);
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
			jQuery.each(this._cols.split(','), function(i, col) {
				var w, cls, ind = col.indexOf('[');
				if (ind > 0) {
					w = col.substring(0, ind);
					cls = col.substring(ind + 1);
				} else {
					w = col;
				}
				
				colClss[i] = cls;
				if (w == '*') {
					colWidths.push(-1);
					dynaColCount++;
				} else {
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
		
			function precalculateRegion(region) {
			
				function doTestRegion() {
					for (var row = rowIndex; row < rowIndex + rowSpan && row < grid.length; row++) {
						for (var col = colIndex; col < colIndex + colSpan; col++) {
							if (grid[row][col]) return false;
						}
					}
					return true;
				}
				
				var previousRegion = this.getPreviousRegion(region);
				var pRegionIndex = -1, pRowIndex = 0, pColIndex = -1, pRowSpan = pColSpan = 1;
				if (previousRegion) {
					pRegionIndex = previousRegion.regionIndex;
					pRowIndex = previousRegion.rowIndex;
					pColIndex = previousRegion.colIndex;
					pRowSpan = previousRegion.rowSpan;
					pColSpan = previousRegion.colSpan;
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
				while (!doTestRegion());
				
				for (var row = 0; row < rowSpan; row++) {
					if ((rowIndex + row) >= grid.length) grid.push(new Array(this.colCount));
					for (var col = 0; col < colSpan; col++) {
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
			
			var grid = this._grid = [];
			var regions = this._regions;
			for (var it = regions.iterator(); it.hasNext();) {
				var region = it.next(), constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					region.regionIndex = -1;
					continue;
				}
				constraint.colSpan = parseInt(constraint.colSpan);
				constraint.rowSpan = parseInt(constraint.rowSpan);
				precalculateRegion.call(this, region);
			}
		},
		
		resizeTableAndCols: function() {
			var realColWidths = this._realColWidths;
			if (!realColWidths) {
				this._realColWidths = realColWidths = [];
			}
			
			var table = this.getDom(), padding = parseInt(this._padding) || 0, colPadding = this._colPadding || 0;
			var containerWidth = (table.parentNode) ? (jQuery(table.parentNode).width() - padding * 2) : 0;
			if (containerWidth < 0) containerWidth = 0;
			
			var adjust = 0;
			if (dorado.Browser.safari || dorado.Browser.chrome) {
				adjust = padding * 2;
			}
			if (this._stretchWidth || this.dynaColCount > 0) {
				table.style.width = (containerWidth + adjust) + "px";
			}
			if (dorado.Browser.msie && dorado.Browser.version < '8') {
				table.style.margin = padding + "px";
			} else {
				table.style.padding = padding + "px";
			}
			
			containerWidth -= colPadding * (this._colWidths.length - 1);
			var self = this, changedCols = [];
			for (var i = 0; i < this._colWidths.length; i++) {
				var w = this._colWidths[i];
				if (self.dynaColCount > 0) {
					if (w == -1) {
						w = parseInt((containerWidth - self.fixedWidth) / self.dynaColCount);
					}
					w = (w < 0) ? 0 : w;
				} else if (self._stretchWidth) {
					w = parseInt(w * containerWidth / self.fixedWidth);
				}
				if (i < this._colWidths.length - 1) w += colPadding;
				
				if (realColWidths[i] != w) changedCols.push(i);
				realColWidths[i] = w;
			}
			return changedCols;
		}
		
	});
	
	var p = dorado.widget.layout.FormLayout.prototype;
	p.onAddControl = p.onRemoveControl = p.doRefreshRegion = function() {
		if (!this._attached || this._disableRendering) return;
		this.refreshDom(this.getDom());
	};
	
})();
