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
	dorado.widget.NumberGridPicker = $extend(dorado.widget.Control, {
		$className: "dorado.widget.NumberGridPicker",
		focusable: true,

		ATTRIBUTES: {
			className: {},

			formatter: {},

			columnCount: {
				writeBeforeReady: true
			},

			rowCount: {
				writeBeforeReady: true
			},

			cellClassName: {
				writeBeforeReady: true
			},

            selectedCellClassName: {
                writeBeforeReady: true
            },

            rowClassName: {
                writeBeforeReady: true
            },

			tableClassName: {
				writeBeforeReady: true
			},

			min: {},

			max: {},

			step: {},

			value: {}
		},

		EVENTS: {
			onValueChange: {}
		},

		createDom: function() {
			var picker = this, min = picker._min === undefined ? 1 : picker._min, max = picker._max, step = picker._step || 1,
				columnCount = picker._columnCount || 1, rowCount = picker._rowCount || 1, doms = {};

			var dom = $DomUtils.xCreate({
				tagName: "table",
				className: picker._tableClassName || "",
				content: {
					tagName: "tbody",
					contextKey: "body"
				}
			}, null, doms);

			picker._doms = doms;

            var formatter = picker._formatter;

			for (var i = 0; i < rowCount; i++) {
				var tr = document.createElement("tr");
				for (var j = 0; j < columnCount; j++) {
					var td = document.createElement("td"), value = min + step * (i * columnCount + j);
					td.className = picker._cellClassName || "";

                    if (typeof formatter == "function") {
                        td.innerText = formatter(value);
                    } else {
                        td.innerText = value;
                    }
					tr.appendChild(td);
				}
                tr.className = picker._rowClassName || "";
				doms.body.appendChild(tr);
			}

			var lastOverCell;

			if (dorado.Browser.msie && dorado.Browser.version == 6) {
				$fly(dom).mousemove(function(event) {
					var position = $DomUtils.getCellPosition(event);
                    if (position.row > picker._rowCount) return;
					if (position && position.element && (position.element != lastOverCell || !lastOverCell)) {
						if (lastOverCell) {
							$fly(lastOverCell).removeClass("hover");
						}
						$fly(position.element).addClass("hover");
						lastOverCell = position.element;
					}
				}).mouseout(function(event) {
                    var position = $DomUtils.getCellPosition(event);
                    if (position.row > picker._rowCount) return;
					if (lastOverCell) {
						$fly(lastOverCell).removeClass("hover");
					}
					lastOverCell = null;
				});
			}

			$fly(dom).click(function(event) {
				var position = $DomUtils.getCellPosition(event), step = picker._step || 1;
				if (position && position.element) {
                    if (position.row >= picker._rowCount) return;
                    var min = picker._min === undefined ? 1 : picker._min, step = step = picker._step || 1,
                        value = min + step * (position.row * columnCount + position.column);

                    picker._value = value;

					picker.fireEvent("onValueChange", picker, {
						value: value
					});

                    picker.refreshValue();
				}
			});

			return dom;
		},

        refreshTable: function() {
            var picker = this, dom = picker._doms.body, formatter = picker._formatter;

            var step = picker._step || 1, min = picker._min === undefined ? 1 : picker._min, columnCount = picker._columnCount,
                rowCount = picker._rowCount, row = Math.floor((value - min) / columnCount), column = (value - min) % columnCount;

            for (var i = 0; i < rowCount; i++) {
                var rows = dom.rows[i];
                for (var j = 0; j < columnCount; j++) {
                    var cell = rows.cells[j], value = min + step * (i * columnCount + j);
                    cell.className = picker._cellClassName || "";

                    if (typeof formatter == "function") {
                        cell.innerText = formatter(value);
                    } else {
                        cell.innerText = value;
                    }
                }
            }
        },

        refreshValue: function() {
            var picker = this, dom = picker._doms.body, lastSelectedCell = picker._lastSelectedCell, value = picker._value;

            if (isNaN(picker._value)) return;

            var step = picker._step || 1, min = picker._min === undefined ? 1 : picker._min, columnCount = picker._columnCount,
                rowCount = picker._rowCount, row = Math.floor((value - min) / columnCount), column = (value - min) % columnCount,
                cell;

            if (dom.rows[row]) {
                cell = dom.rows[row].cells[column];
            } else {
                return;
            }

            if (lastSelectedCell) {
                $fly(lastSelectedCell).removeClass(picker._selectedCellClassName || "selected");
            }

            if (cell) {
                $fly(cell).addClass(picker._selectedCellClassName || "selected");
            }

            picker._lastSelectedCell = cell;
        },

		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);

            this.refreshTable();
            this.refreshValue();
		}
	});

    dorado.widget.YearPicker = $extend(dorado.widget.NumberGridPicker, {
        ATTRIBUTES: {
            rowCount: {
                defaultValue: 5
            },
            columnCount: {
                defaultValue: 2
            },
            tableClassName: {
                defaultValue: "year-table"
            },
            rowClassName: {
                defaultValue: "number-row"
            },
            value: {
                setter: function(value) {
                    var picker = this, oldValue = picker._value, startYear, remainder;
                    remainder = value % 10;
                    picker._min = value - (remainder == 0 ? 10 : remainder) + 1;

                    picker._value = value;
                }
            }
        },

        createDom: function() {
            var picker = this, dom = $invokeSuper.call(picker, arguments);

            var preYearButton = new dorado.widget.SimpleIconButton({
                iconClass: "prev-year-button",
                listener: {
                    onClick: function() {
                        picker.set("value", picker._value - 10);
                    }
                }
            });

            var nextYearButton = new dorado.widget.SimpleIconButton({
                iconClass: "next-year-button",
                listener: {
                    onClick: function() {
                        picker.set("value", picker._value + 10);
                    }
                }
            });

            var buttonRow = document.createElement("tr"), prevYearCell = document.createElement("td"), nextYearCell = document.createElement("td");
            buttonRow.className = "btn-row";
            prevYearCell.align = "center";
            nextYearCell.align = "center";

            buttonRow.appendChild(prevYearCell);
            buttonRow.appendChild(nextYearCell);

            picker._doms.body.appendChild(buttonRow);

            preYearButton.render(prevYearCell);
            nextYearButton.render(nextYearCell);

            picker.registerInnerControl(preYearButton);
            picker.registerInnerControl(nextYearButton);

            return dom;
        }
    });

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
				defaultValue: (new Date).getFullYear(),
                path: "_yearTablePicker.value"
			},

			/**
			 * 选择的月份
			 * @attribute
			 * @default 0
			 * @type int
			 */
			month: {
				defaultValue: 0,
                path: "_monthTablePicker.value"
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
         * @private
		 * 更新日期。
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

		createDom: function() {
			var monthLabel = $resource("dorado.baseWidget.AllMonths") || "", monthLabels = monthLabel.split(",");
			var picker = this, doms = {}, dom = $DomUtils.xCreate({
				tagName: "div",
				content: [{
					tagName: "table",
					className: "year-table",
					contextKey: "yearTable"
				}, {
					tagName: "table",
					className: "month-table",
					contextKey: "monthTable"
				}, {
					tagName: "div",
					className: "btns-pane",
					contextKey: "buttonPanel"
				}]
			}, null, doms);
			
			var monthLastOverCell, yearLastOverCell;
			
			picker._doms = doms;

            var monthTablePicker = new dorado.widget.NumberGridPicker({
                rowCount: 6,
                columnCount: 2,
                tableClassName: "month-table",
                rowClassName: "number-row",
                min: 0,
                max: 11,
                renderOn: doms.monthTable,
                formatter: function(value) {
                    return monthLabels[value];
                },
                value: picker._month || 0
            });

            monthTablePicker.render(doms.monthTable);

            doms.monthTable = monthTablePicker._dom;

            picker._monthTablePicker = monthTablePicker;
            picker.registerInnerControl(monthTablePicker);

            var yearTablePicker = new dorado.widget.YearPicker({
                renderOn: doms.yearTable,
                value: picker._year
            });

            yearTablePicker.render(doms.yearTable);
            doms.yearTable = yearTablePicker._dom;
            picker._yearTablePicker = yearTablePicker;
            picker.registerInnerControl(yearTablePicker);
			
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
			
			return dom;
		},

		doOnKeyDown: function(event) {
			var picker = this, year = picker.get("year"), month = picker.get("month");
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
					onPick: function(self) {
						var retval = new Date(self.get("year"), self.get("month"));
						retval.year = self.get("year");
						retval.month = self.get("month");
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