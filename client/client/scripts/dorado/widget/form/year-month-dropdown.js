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
	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class YearMonthPicker
	 * @extends dorado.widget.Control
	 */
	dorado.widget.YearMonthPicker = $extend(dorado.widget.Control, /** @scope dorado.widget.YearMonthPicker.prototype */ {
        $className: "dorado.widget.YearMonthPicker",
        _inherentClassName: "i-year-month-picker",
		_opened: false,
		focusable: true,
		ATTRIBUTES: /** @scope dorado.widget.YearMonthPicker.prototype */ {
			className: {
				defaultValue: "d-year-month-picker"
			},
			
			/**
			 * 选择的年份
			 * @attribute
			 * @default (new Date).getFullYear()
			 * @type int
			 */
			year: {
				defaultValue: (new Date).getFullYear()
			},

			/**
			 * 选择的月份
			 * @attribute
			 * @default 0
			 * @type int
			 */
			month: {
				defaultValue: 0
			}
		},

		EVENTS: /** @scope dorado.widget.YearMonthPicker.prototype */ {
			/**
			 * 当点击确定按钮之后触发的事件
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onPick: {},
			/**
			 * 当点击取消按钮之后触发的事件
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onCancel: {}
		},

		/**
		 * 刷新年份。
		 * @param {int} year 新的年份
		 * @private
		 */
		refreshYear: function(year) {
			var picker = this, oldYear = picker._year, startYear, doms = picker._doms, yearTable = doms.yearTable, remainder;
			
			year = year ? year : oldYear;
			remainder = year % 10;
			startYear = year - (remainder == 0 ? 10 : remainder) + 1;
			
			for (var i = 0; i < 5; i++) {
				for (var j = 0; j < 2; j++) {
					var cell = yearTable.rows[i].cells[j];
					cell.innerText = startYear + i * 2 + j;
				}
			}
			
			var lastSelectedCell = picker._lastSelectedYearCell, index = year - startYear, newSelectedCell = yearTable.rows[Math.floor(index / 2)].cells[index % 2];
			
			if (lastSelectedCell) {
				$fly(lastSelectedCell).removeClass("selected");
			}
			
			if (newSelectedCell) {
				$fly(newSelectedCell).addClass("selected");
			}
			
			picker._lastSelectedYearCell = newSelectedCell;
		},

		/**
		 * 刷新月份。
		 * @param {int} month 设置的月份。
		 * @private
		 */
		refreshMonth: function(month) {
			var picker = this, oldMonth = picker._month, doms = picker._doms, monthTable = doms.monthTable;
			
			if (isNaN(month)) {
				month = oldMonth ? oldMonth : 0;
			}
			
			var lastSelectedCell = picker._lastSelectedMonthCell, cell = monthTable.rows[Math.floor(month / 2)].cells[month % 2];
			
			if (lastSelectedCell) {
				$fly(lastSelectedCell).removeClass("selected");
			}
			
			if (cell) {
				$fly(cell).addClass("selected");
			}
			
			picker._lastSelectedMonthCell = cell;
		},

		/**
		 * 更新日期。
		 * @param date
		 * @param month
		 */
		updateDate: function(date, month) {
			var picker = this, year = date;
			
			if (date instanceof Date) {
				picker.set("year", date.getFullYear());
				picker.set("month", date.getMonth());
			} else {
				if (!isNaN(year) && !isNaN(month)) {
					picker.set("year", year);
					picker.set("month", month);
				}
			}
		},

		refreshDom: function() {
			var picker = this;
			picker.refreshYear(picker._year);
			picker.refreshMonth(picker._month);
			$invokeSuper.call(this, arguments);
		},

		createDom: function() {
			var monthLabel = $resource("dorado.baseWidget.AllMonths") || "", month_label = monthLabel.split(",");
			var picker = this, doms = {}, dom = $DomUtils.xCreate({
				tagName: "div",
				content: [{
					tagName: "table",
					className: "year-table",
					contextKey: "yearTable",
					content: [
						{ tagName: "tr", className: "number-row", content: [{ tagName: "td" }, { tagName: "td" }] },
						{ tagName: "tr", className: "number-row", content: [{ tagName: "td" }, { tagName: "td" }] },
						{ tagName: "tr", className: "number-row", content: [{ tagName: "td" }, { tagName: "td" }] },
						{ tagName: "tr", className: "number-row", content: [{ tagName: "td" }, { tagName: "td" }] },
						{ tagName: "tr", className: "number-row", content: [{ tagName: "td" }, { tagName: "td" }] },
						{ tagName: "tr", className: "btn-row", content: [
							{ tagName: "td", align: "center", contextKey: "preaYearCell" },
							{ tagName: "td", align: "center", contextKey: "nextYearCell" }
						]}
					]
				}, {
					tagName: "table",
					className: "month-table",
					contextKey: "monthTable",
					content: [{
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[0] },
							{ tagName: "td", content: month_label[1] }
						]
					}, {
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[2] },
							{ tagName: "td", content: month_label[3] }
						]
					}, {
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[4] },
							{ tagName: "td", content: month_label[5] }
						]
					}, {
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[6] },
							{ tagName: "td", content: month_label[7] }
						]
					}, {
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[8] },
							{ tagName: "td", content: month_label[9] }
						]
					}, {
						tagName: "tr",
						className: "number-row",
						content: [
							{ tagName: "td", content: month_label[10] },
							{ tagName: "td", content: month_label[11] }
						]
					}]
				}, {
					tagName: "div",
					className: "btns-pane",
					contextKey: "buttonPanel"
				}]
			}, null, doms);
			
			var monthLastOverCell, yearLastOverCell;
			
			picker._doms = doms;
			
			if (dorado.Browser.msie && dorado.Browser.version == 6) {
				$fly(doms.monthTable).mousemove(function(event) {
					var position = $DomUtils.getCellPosition(event);
					
					if (position && position.element && (position.element != monthLastOverCell || !monthLastOverCell)) {
						if (monthLastOverCell) {
							$fly(monthLastOverCell).removeClass("hover");
						}
						$fly(position.element).addClass("hover");
						monthLastOverCell = position.element;
					}
				}).mouseout(function() {
					if (monthLastOverCell) {
						$fly(monthLastOverCell).removeClass("hover");
					}
					monthLastOverCell = null;
				});
			}
			
			$fly(doms.monthTable).click(function(event) {
				var position = $DomUtils.getCellPosition(event);
				if (position && position.element) {
					picker.set("month", position.row * 2 + position.column);
				}
			});
			
			if (dorado.Browser.msie && dorado.Browser.version == 6) {
				$fly(doms.yearTable).mousemove(function(event) {
					var position = $DomUtils.getCellPosition(event);
					
					if (position && position.element && (position.element != yearLastOverCell || !yearLastOverCell)) {
						if (position.row == 5) {
							return;
						}
						if (yearLastOverCell) {
							$fly(yearLastOverCell).removeClass("hover");
						}
						$fly(position.element).addClass("hover");
						yearLastOverCell = position.element;
					}
				}).mouseout(function() {
					if (yearLastOverCell) {
						$fly(yearLastOverCell).removeClass("hover");
					}
					yearLastOverCell = null;
				});
			}
			
			$fly(doms.yearTable).click(function(event) {
				var position = $DomUtils.getCellPosition(event);
				if (position && position.element) {
					if (position.row == 5) {
						return;
					}
					picker.set("year", parseInt(position.element.innerHTML, 10));
				}
			});
			
			var preYearButton = new dorado.widget.SimpleIconButton({
				iconClass: "prev-year-button",
				listener: {
					onClick: function() {
						picker.set("year", picker._year - 10);
					}
				}
			});
			
			var nextYearButton = new dorado.widget.SimpleIconButton({
				iconClass: "next-year-button",
				listener: {
					onClick: function() {
						picker.set("year", picker._year + 10);
					}
				}
			});
			
			preYearButton.render(doms.preaYearCell);
			nextYearButton.render(doms.nextYearCell);
			
			picker.registerInnerControl(preYearButton);
			picker.registerInnerControl(nextYearButton);
			
			var okButton = new dorado.widget.Button({
				caption: $resource("dorado.baseWidget.YMPickerConfirm"),
				listener: {
					onClick: function() {
						picker.fireEvent("onPick", picker);
					}
				}
			});
			okButton.render(doms.buttonPanel);
			
			var cancelButton = new dorado.widget.Button({
				caption: $resource("dorado.baseWidget.YMPickerCancel"),
				listener: {
					onClick: function() {
						picker.fireEvent("onCancel", picker);
					}
				}
			});
			cancelButton.render(doms.buttonPanel);
			
			picker.registerInnerControl(okButton);
			picker.registerInnerControl(cancelButton);
			
			picker.refreshYear();
			picker.refreshMonth();
			
			return dom;
		},
		doOnKeyDown: function(event) {
			var picker = this, year = picker._year, month = picker._month;
			switch (event.keyCode) {
				case 37://left arrow
					if (event.ctrlKey) {
						picker.set("month", month == 0 ? 11 : month - 1);
					}
					break;
				case 38://up arrow
					if (event.ctrlKey) {
						picker.set("year", year - 1);
					}
					break;
				case 39://right arrow
					if (event.ctrlKey) {
						picker.set("month", month == 11 ? 0 : month + 1);
					}
					break;
				case 40://down arrow
					if (event.ctrlKey) {
						picker.set("year", year + 1);
					}
					break;
				case 33://page up
					picker.set("year", year - 10);
					break;
				case 34://page down
					picker.set("year", year + 10);
					break;
				case 13://enter
					picker.fireEvent("onPick", picker);
					return false;
			}
		}
	});
	
	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Trigger
	 * @class YearMonthDropDown
	 * @extends dorado.widget.DropDown
	 */
	dorado.widget.YearMonthDropDown = $extend(dorado.widget.DropDown, /** @scope dorado.widget.YearMonthDropDown.prototype */{
        $className: "dorado.widget.YearMonthDropDown",
		ATTRIBUTES: {
			width: {
				defaultValue: 260
			},
			height: {
				defaultValue: 190
			},
			iconClass: {
				defaultValue: "d-trigger-icon-date"
			}
		},

		createDropDownBox: function(editor) {
			var dropDown = this, box = $invokeSuper.call(this, arguments), picker = new dorado.widget.YearMonthPicker({
				listener: {
					onPick: function(picker) {
						var retval = new Date(picker._year, picker._month);
						retval.year = picker._year;
						retval.month = picker._month;
						dropDown.close(retval);
					},
					onCancel: function() {
						dropDown.close();
					}
				}
			});
			
			box.set("control", picker);
			return box;
		},

		doOnKeyPress: function(event) {
			var picker = this, retValue = true, ymPicker = picker.get("box.control");
			switch (event.keyCode) {
				case 27: // esc
					picker.close();
					retValue = false;
					break;
				case 13: // enter
					ymPicker.fireEvent("onPick", picker);
					retValue = false;
					break;
				default:
					retValue = ymPicker.onKeyDown(event);
			}
			return retValue;
		}
	});
	
	dorado.widget.View.registerDefaultComponent("defaultYearMonthDropDown", function() {
		return new dorado.widget.YearMonthDropDown();
	});
})();
