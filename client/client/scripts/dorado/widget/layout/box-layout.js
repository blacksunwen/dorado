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

	var defaultRegionPadding = 2;

	var HBOX_ALIGNS = {
		top: "top",
		center: "middle",
		bottom: "bottom"
	}, HBOX_PACKS = {
		start: "left",
		center: "center",
		end: "right"
	};

	var VBOX_ALIGNS = {
		left: "left",
		center: "center",
		right: "right"
	}, VBOX_PACKS = {
		start: "top",
		center: "middle",
		end: "bottom"
	};

	/**
	 * @abstract
	 */
	dorado.widget.layout.AbstractBoxLayout = $extend(dorado.widget.layout.Layout, /** @scope dorado.widget.layout.AbstractBoxLayout.prototype */ {
		$className: "dorado.widget.layout.AbstractBoxLayout",

		ATTRIBUTES: /** @scope dorado.widget.layout.AbstractBoxLayout.prototype */ {

			/**
			 * start, center, end
			 */
			pack: {
				defaultValue: "start"
			},

			/**
			 *
			 */
			stretch: {
				defaultValue: true
			},

			padding: {
				defaultValue: 2
			},

			/**
			 * 布局区域之间空隙的大小。
			 * @type int
			 * @attribute
			 */
			regionPadding: {
				defaultValue: defaultRegionPadding
			}
		}
	});

	var p = dorado.widget.layout.AbstractBoxLayout.prototype;
	p.onAddControl = p.doRefreshRegion = function() {
		if (!this._attached || this._disableRendering) return;
		this.refresh();
	};
	p.onRemoveControl = function(control) {
		if (!this._attached || this._disableRendering) return;
		this.refresh();
	};

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 横向箱式布局管理器。
	 * @shortTypeName HBox
	 * @extends dorado.widget.layout.AbstractBoxLayout
	 */
	dorado.widget.layout.HBoxLayout = $extend(dorado.widget.layout.AbstractBoxLayout, /** @scope dorado.widget.layout.HBoxLayout.prototype */ {
		$className: "dorado.widget.layout.HBoxLayout",
		_className: " d-hbox-layout",

		ATTRIBUTES: /** @scope dorado.widget.layout.HBoxLayout.prototype */ {

			/**
			 * top、center、bottom
			 */
			align: {
				defaultValue: "center"
			}
		},

		createDom: function() {
			var context = {}, dom = $DomUtils.xCreate({
				tagName: "TABLE",
				className: this._className,
				cellSpacing: 0,
				cellPadding: 0,
				content: {
					tagName: "TBODY",
					content: {
						tagName: "TR",
						contextKey: "row"
					}
				}
			}, null, context);
			this._row = context.row;
			return dom;
		},

		refreshDom: function(dom) {
			this.ensureControlsInited();
			
			var table = dom, row = this._row, containerDom = table.parentNode;
			var domCache = this.domCache || {}, newDomCache = this.domCache = {};

			var padding = parseInt(this._padding) || 0, regionPadding = this._regionPadding || 0;
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				table.style.margin = padding + "px";
			}
			else {
				table.style.padding = padding + "px";
			}

			var clientSize = this._container.getContentContainerSize();
			var clientWidth = clientSize[0], clientHeight = clientSize[1];
			if (clientWidth > 10000) clientWidth = 0;
			if (clientHeight > 10000) clientHeight = 0;

			var realContainerWidth = clientWidth - padding * 2;
			var realContainerHeight = clientHeight - padding * 2;
			if (realContainerWidth < 0) realContainerWidth = 0;
			if (realContainerHeight < 0) realContainerHeight = 0;

			// 较新版的Chrome下似乎不用这么干了，见form-layout.js
			if (dorado.Browser.webkit) {
				realContainerHeight -= padding * 2; // 搞不懂为什么Webkit中一定要重复的减去一次padding后才刚好 2012/3/14
			}

			$fly(containerDom).css("text-align", HBOX_PACKS[this._pack]);
			row.style.verticalAlign = HBOX_ALIGNS[this._align];

			if (!dorado.Browser.webkit) {
				table.style.height = realContainerHeight + "px";
			}
			else {
				row.style.height = realContainerHeight + "px";
			}
			
			var regions = this._regions.items, region, cell, childNodeIndex = 0;
			for (var i = 0, len = regions.length; i < len; i++) {
				region = regions[i];
				
				cell = domCache[region.id];
				if (cell) cell.style.display = "none";
				
				var constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					var control = region.control;
					if (control._dom) $DomUtils.getUndisplayContainer().appendChild(control._dom);
				}
				else {
					var w, div, isNewCell = false;
					if (!cell) {
						cell = document.createElement("TD");
						isNewCell = true;
					}
					else {
						delete domCache[region.id];
					}
					newDomCache[region.id] = cell;
	
					var refCell = row.childNodes[childNodeIndex];
					if (refCell != cell) {
						if (cell.parentNode == row) {
							while(refCell && refCell != cell) {
								row.removeChild(refCell);
								refCell = refCell.nextSibling;
							}
						}
						else {
							(refCell) ? row.insertBefore(cell, refCell) : row.appendChild(cell);
						}
					}
					childNodeIndex ++;
					
					cell.style.display = "";
					if (constraint.align) cell.style.verticalAlign = (constraint.align == "center") ? "middle" : constraint.align;
	
					var w = region.control._width;
					if (w) {
						if (w.constructor == String && w.match('%')) {
							var rate = parseInt(w);
							if (!isNaN(rate)) {
								w = rate * realContainerWidth / 100;
							}
						}
						else {
							w = parseInt(w);
						}
					}
					else {
						w = undefined;
					}
					region.width = w;
	
					var h;
					if (!this._stretch || region.control.getAttributeWatcher().getWritingTimes("height")) {
						h = region.control._height;
						if (h) {
							if (h.constructor == String && h.match('%')) {
								var rate = parseInt(h);
								if (!isNaN(rate)) {
									h = rate * realContainerHeight / 100;
								}
							}
							else {
								h = parseInt(h);
							}
						}
						else {
							h = undefined;
						}
					}
					else {
						h = realContainerHeight;
					}
					region.height = h;
	
					if (i > 0) cell.style.paddingLeft = (region.constraint.padding || regionPadding) + "px";
					this.renderControl(region, cell, true, this._stretch);
				}
			}

			for(var regionId in domCache) {
				var cell = domCache[regionId];
				if (cell && cell.parentNode == row) row.removeChild(cell);
				delete domCache[regionId];
			}

			if (this._stretch) {
				var rowHeight = row.offsetHeight;
				if (rowHeight > realContainerHeight) {
					table.style.height = "";
					realContainerHeight += (rowHeight - realContainerHeight);
					
					for (var i = 0, len = regions.length; i < len; i++) {
						region = regions[i];
						var constraint = region.constraint;
						if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) continue;

						var h = region.control._height;
						if (h) {
							if (h.constructor == String && h.match('%')) {
								var rate = parseInt(h);
								if (!isNaN(rate)) {
									h = rate * realContainerHeight / 100;
								}
							}
						}
						if (h) {
							region.height = h;
							var cell = newDomCache[region.id];
							this.resetControlDimension(region, cell, true, true);
						}
					}
				}
			}
		}

	});

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 纵向箱式布局管理器。
	 * @shortTypeName VBox
	 * @extends dorado.widget.layout.AbstractBoxLayout
	 */
	dorado.widget.layout.VBoxLayout = $extend(dorado.widget.layout.AbstractBoxLayout, /** @scope dorado.widget.layout.VBoxLayout.prototype */ {
		$className: "dorado.widget.layout.VBoxLayout",
		_className: "d-vbox-layout",

		ATTRIBUTES: /** @scope dorado.widget.layout.VBoxLayout.prototype */ {
			
			lazyRenderChild: {},

			/**
			 * left、center、right
			 */
			align: {
				defaultValue: "left"
			}
		},

		createDom: function() {
			var context = {}, dom = $DomUtils.xCreate({
				tagName: "TABLE",
				className: this._className,
				cellSpacing: 0,
				cellPadding: 0,
				content: {
					tagName: "TBODY",
					contextKey: "tbody"
				}
			}, null, context);
			this._tbody = context.tbody;
			return dom;
		},

		preparePackTable: function(container, pack) {
			if (!this._packTable) {
				var context = {};
				this._packTable = $DomUtils.xCreate({
					tagName: "TABLE",
					cellSpacing: 0,
					cellPadding: 0,
					content: {
						tagName: "TR",
						content: {
							tagName: "TD",
							contextKey: "packCell",
							content: this.getDom()
						}
					},
					style: {
						width: "100%",
						height: "100%"
					}
				}, null, context);
				this._packCell = context.packCell;
				container.appendChild(this._packTable);
			}
			this._packCell.style.verticalAlign = VBOX_PACKS[pack];
			this._packCell.style.align = "center";
			this._packCell.align = "center";
			return this._packTable;
		},

		removePackTable: function(container) {
			if (this._packTable) {
				container.removeChild(this._packTable);
				container.appendChild(this.getDom());
				delete this._packTable;
				delete this._packCell;
			}
		},

		refreshDom: function(dom) {
			var containerDom = this._dom.parentNode;
			if (this._pack == "start") {
				this.removePackTable(containerDom);
			}
			else {
				this.preparePackTable(containerDom, this._pack);
			}

			var table = dom, tbody = this._tbody;
			var domCache = this.domCache || {}, newDomCache = this.domCache = {};

			var padding = parseInt(this._padding) || 0, regionPadding = this._regionPadding || 0;
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				table.style.margin = padding + "px";
			}
			else {
				table.style.padding = padding + "px";
			}

			var clientSize = this._container.getContentContainerSize();
			var clientWidth = clientSize[0], clientHeight = clientSize[1];
			if (clientWidth > 10000) clientWidth = 0;
			if (clientHeight > 10000) clientHeight = 0;

			var realContainerWidth = clientWidth - padding * 2;
			var realContainerHeight = clientHeight - padding * 2;
			if (realContainerWidth < 0) realContainerWidth = 0;
			if (realContainerHeight < 0) realContainerHeight = 0;

			table.style.width = realContainerWidth + "px";

			var regions = this._regions.items, region, control, fakeDom, row, cell;
			var childNodeIndex = 0, overflowed;
			for (var i = 0, len = regions.length; i < len; i++) {
				region = regions[i];

				row = domCache[region.id];
				
				var constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					if (row) row.style.display = "none";
					continue;
				}
				
				control = region.control;
				fakeDom = region.fakeDom;
				
				if (!control._rendered) {
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
						constraint = region.constraint;
						if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
							if (row) row.style.display = "none";
							continue;
						}
					}
				}

				var w, div, isNewRow = false;
				if (!row) {
					row = $DomUtils.xCreate({
						tagName: "TR",
						content: {
							tagName: "TD",
							content: (function() {
								if (dorado.Browser.msie && dorado.Browser.version < 8) {
									return undefined;
								}
								else {
									return {
										tagName: "DIV",
										className: "d-fix-text-align",
										// 用于防止TD.align=center的设置传染到内部子控件
										style: !dorado.Browser.webkit ? "zoom:1" : "display:table-cell"
									};
								}
							})()
						}
					});
					isNewRow = true;
				}
				else {
					delete domCache[region.id];
				}
				newDomCache[region.id] = row;
				row.style.display = "none";

				var refRow = tbody.childNodes[childNodeIndex];
				if (refRow != row) {
					if (row.parentNode == tbody) {
						while(refRow && refRow != row) {
							tbody.removeChild(refRow);
							refRow = refRow.nextSibling;
						}
					}
					else {
						(refRow) ? tbody.insertBefore(row, refRow) : tbody.appendChild(row);
					}
				}
				childNodeIndex ++;
				
				row.style.display = "";
				cell = row.firstChild;
				div = (dorado.Browser.msie && dorado.Browser.version < 8) ? cell : cell.firstChild;

				if (!this._stretch || control.getAttributeWatcher().getWritingTimes("width")) {
					w = control._width;
					if (w) {
						if (w.constructor == String && w.match('%')) {
							var rate = parseInt(w);
							if (!isNaN(rate)) {
								w = rate * realContainerWidth / 100;
							}
						}
						else {
							w = parseInt(w);
						}
					}
					else {
						w = undefined;
					}
				}
				else {
					w = realContainerWidth;
				}
				region.width = w;

				var h = control._height;
				if (h) {
					if (h.constructor == String && h.match('%')) {
						var rate = parseInt(h);
						if (!isNaN(rate)) {
							h = rate * realContainerHeight / 100;
						}
					}
					else {
						h = parseInt(h);
					}
				}
				else {
					h = undefined;
				}
				region.height = h;

				cell.align = constraint.align || VBOX_ALIGNS[this._align];
				if (i > 0) cell.style.paddingTop = (region.constraint.padding || regionPadding) + "px";
				
				if (!overflowed) {
					this.renderControl(region, div, true, true);
					if (dorado.Browser.msie && dorado.Browser.version < 9 && control._rendered) {
						if (dorado.Browser.version < 8) {
							$fly(control.getDom()).addClass("d-fix-text-align");
						}
						row.appendChild(cell);  // 修正ie8下有时td的内容溢出，但td却未被撑开的BUG
					}
				}
				else {
					div.appendChild(fakeDom);
				}
			}

			for(var regionId in domCache) {
				var row = domCache[regionId];
				if (row && row.parentNode == tbody) tbody.removeChild(row);
				delete domCache[regionId];
			}

			var tableWidth = realContainerWidth;
			if (this._stretch && (tableWidth - padding * 2) > realContainerWidth) {
				realContainerWidth += (tableWidth - padding * 2 - realContainerWidth);

				for (var i = 0, len = regions.length; i < len; i++) {
					region = regions[i];
					if (region.fakeDom || region.constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) continue;

					var w = region.control._width;
					if (w) {
						if (w.constructor == String && w.match('%')) {
							var rate = parseInt(w);
							if (!isNaN(rate)) {
								w = rate * realContainerWidth / 100;
							}
						}
					}
					if (w) {
						region.width = w;
						var div = newDomCache[region.id];
						this.resetControlDimension(region, div, true, true);
					}
				}
			}
		},
		
		getFakeDomOffsetTop: function(fakeDom) {
			var cell = (dorado.Browser.msie && dorado.Browser.version < 8) ? fakeDom.parentNode : fakeDom.parentNode.parentNode;
			return cell.offsetTop;
		}

	});

})();
