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
		this.refreshDom(this.getDom());
	};
	p.removeControl = function(control) {
		this._regions.removeKey(control._id);
		if (!this._attached || this._disableRendering) return;
		this.refreshDom(this.getDom());
	},
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 横向箱式布局管理器。
	 * @shortTypeName HBox
	 * @extends dorado.widget.layout.AbstractBoxLayout
	 */
	dorado.widget.layout.HBoxLayout = $extend(dorado.widget.layout.AbstractBoxLayout, /** @scope dorado.widget.layout.HBoxLayout.prototype */ {
		$className: "dorado.widget.layout.HBoxLayout",
		
		ATTRIBUTES: /** @scope dorado.widget.layout.HBoxLayout.prototype */ {
		
			className: {
				defaultValue: "d-hbox-layout"
			},
			
			regionClassName: {
				defaultValue: "d-hbox-layout-region"
			},
			
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
			var table = dom, row = this._row, parentDom = table.parentNode;
			var domCache = this.domCache || {}, newDomCache = this.domCache = {};
			
			var padding = parseInt(this._padding) || 0, regionPadding = this._regionPadding || 0;
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				table.style.margin = padding + "px";
			} else {
				table.style.padding = padding + "px";
			}
			
			var realContainerWidth = parentDom.clientWidth - padding * 2;
			var realContainerHeight = parentDom.clientHeight - padding * 2;
			if (realContainerWidth < 0) realContainerWidth = 0;
			if (realContainerHeight < 0) realContainerHeight = 0;
			
			if (dorado.Browser.webkit) {
				realContainerHeight -= padding * 2; // 搞不懂为什么Webkit中一定要重复的减去一次padding后才刚好 2012/3/14
			}
			
			$fly(parentDom).css("text-align", HBOX_PACKS[this._pack]);
			row.style.verticalAlign = HBOX_ALIGNS[this._align];
			
			if (!dorado.Browser.webkit) {
				table.style.height = realContainerHeight + "px";
			} else {
				row.style.height = realContainerHeight + "px";
			}
			
			for (var it = this._regions.iterator(); it.hasNext();) {
				var region = it.next(), cell = domCache[region.id];
				if (cell) {
					cell.style.display = "none";
					delete domCache[region.id];
					newDomCache[region.id] = cell;
				}
			}
			
			for (var id in domCache) {
				var cell = domCache[id];
				if (cell) row.removeChild(cell);
			}
			domCache = newDomCache;
			
			var i = 0;
			for (var it = this._regions.iterator(); it.hasNext();) {
				var region = it.next();
				var constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) continue;
				
				var w, cell = domCache[region.id], cell, div, isNewCell = false;
				if (!cell) {
					cell = document.createElement("TD");
					newDomCache[region.id] = cell;
					isNewCell = true;
				}
				var refCell = row[i];
				if (refCell != cell) {
					(refCell) ? tbody.insertBefore(cell, refCell) : row.appendChild(cell);
				}
				cell.style.display = "";
				if (constraint.align) cell.style.verticalAlign = constraint.align;
				
				var w = region.control._width;
				if (w) {
					if (w.constructor == String && w.match('%')) {
						var rate = parseInt(w);
						if (!isNaN(rate)) {
							w = rate * realContainerWidth / 100;
						}
					} else {
						w = parseInt(w);
					}
				} else {
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
						} else {
							h = parseInt(h);
						}
					} else {
						h = undefined;
					}
				} else {
					h = realContainerHeight;
				}
				region.height = h;
				
				if (i > 0) cell.style.paddingLeft = regionPadding + "px";
				if (isNewCell) this.renderControl(region, cell, true, this._stretch);
				else this.resetControlDimension(region, cell, true, this._stretch);
				i++;
			}
			
			var rowHeight = row.offsetHeight;
			if (this._stretch && rowHeight > realContainerHeight) {
				table.style.height = "";
				realContainerHeight += (rowHeight - realContainerHeight);
				for (var it = this._regions.iterator(); it.hasNext();) {
					var region = it.next();
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
		
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 纵向箱式布局管理器。
	 * @shortTypeName VBox
	 * @extends dorado.widget.layout.AbstractBoxLayout
	 */
	dorado.widget.layout.VBoxLayout = $extend(dorado.widget.layout.AbstractBoxLayout, /** @scope dorado.widget.layout.VBoxLayout.prototype */ {
		$className: "dorado.widget.layout.HBoxLayout",
		
		ATTRIBUTES: /** @scope dorado.widget.layout.VBoxLayout.prototype */ {
		
			className: {
				defaultValue: "d-vbox-layout"
			},
			
			regionClassName: {
				defaultValue: "d-vbox-layout-region"
			},
			
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
			var parentDom = this._dom.parentNode;
			if (this._pack == "start") {
				this.removePackTable(parentDom);
			} else {
				this.preparePackTable(parentDom, this._pack);
			}
			
			var table = dom, tbody = this._tbody;
			var domCache = this.domCache || {}, newDomCache = this.domCache = {};
			
			var padding = parseInt(this._padding) || 0, regionPadding = this._regionPadding || 0;
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				table.style.margin = padding + "px";
			} else {
				table.style.padding = padding + "px";
			}
			
			var realContainerWidth = parentDom.clientWidth - padding * 2;
			var realContainerHeight = parentDom.clientHeight - padding * 2;
			if (realContainerWidth < 0) realContainerWidth = 0;
			if (realContainerHeight < 0) realContainerHeight = 0;
			
			table.style.width = realContainerWidth + "px";
			for (var it = this._regions.iterator(); it.hasNext();) {
				var region = it.next(), row = domCache[region.id];
				if (row) {
					row.style.display = "none";
					delete domCache[region.id];
					newDomCache[region.id] = row;
				}
			}
			
			for (var id in domCache) {
				var row = domCache[id];
				if (row) tbody.removeChild(row);
			}
			domCache = newDomCache;
			
			var i = 0;
			for (var it = this._regions.iterator(); it.hasNext();) {
				var region = it.next();
				var constraint = region.constraint;
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) continue;
				
				var w, row = domCache[region.id], cell, div, isNewRow = false;
				if (!row) {
					row = $DomUtils.xCreate({
						tagName: "TR",
						content: {
							tagName: "TD",
							content: (function() {
								if (dorado.Browser.msie && dorado.Browser.version < 8) {
									return undefined;
								} else {
									return {
										tagName: "DIV",
										className: "i-fix-text-align",
										// 用于防止TD.align=center的设置传染到内部子控件
										style: "display:inline-block;zoom:1"
									};
								}
							})()
						}
					});
					newDomCache[region.id] = row;
					isNewRow = true;
				}
				var refRow = tbody[i];
				if (refRow != row) {
					(refRow) ? tbody.insertBefore(row, refRow) : tbody.appendChild(row);
				}
				row.style.display = "";
				cell = row.firstChild;
				div = (dorado.Browser.msie && dorado.Browser.version < 8) ? cell : cell.firstChild;
				
				if (!this._stretch || region.control.getAttributeWatcher().getWritingTimes("width")) {
					w = region.control._width;
					if (w) {
						if (w.constructor == String && w.match('%')) {
							var rate = parseInt(w);
							if (!isNaN(rate)) {
								w = rate * realContainerWidth / 100;
							}
						} else {
							w = parseInt(w);
						}
					} else {
						w = undefined;
					}
				} else {
					w = realContainerWidth;
				}
				region.width = w;
				
				var h = region.control._height;
				if (h) {
					if (h.constructor == String && h.match('%')) {
						var rate = parseInt(h);
						if (!isNaN(rate)) {
							h = rate * realContainerHeight / 100;
						}
					} else {
						h = parseInt(h);
					}
				} else {
					h = undefined;
				}
				region.height = h;
				
				cell.align = constraint.align || VBOX_ALIGNS[this._align];
				if (i > 0) cell.style.paddingTop = regionPadding + "px";
				
				if (isNewRow) {
					this.renderControl(region, div, true, true);
					if (dorado.Browser.msie && dorado.Browser.version < 8 && region.control && region.control._rendered) {
						$fly(region.control.getDom()).addClass("i-fix-text-align");
					}
				} else {
					this.resetControlDimension(region, div, true, true);
				}
				i++;
			}
			
			var tableWidth = table.offsetWidth;
			if (this._stretch && (tableWidth - padding * 2) > realContainerWidth) {
				realContainerWidth += (tableWidth - padding * 2 - realContainerWidth);
				for (var it = this._regions.iterator(); it.hasNext();) {
					var region = it.next();
					var constraint = region.constraint;
					if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) continue;
					
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
		}
		
	});
	
})();
