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

(function () {
    var DateHelper = {
        getWeekCountOfYear:function (date) {
            var begin = new Date(date.getFullYear(), 0, 1);
            var day = begin.getDay();
            if (day == 0) {
                day = 7;
            }
            var duration = date.getTime() - begin.getTime() + (day - 1) * 3600 * 24 * 1000;
            return Math.ceil(duration / 3600 / 24 / 1000 / 7);
        },
        //取得某个月的天数
        getDayCountOfMonth:function (year, month) {
            if (month == 3 || month == 5 || month == 8 || month == 10) {
                return 30;
            }
            if (month == 1) {
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            }
            return 31;
        },
        getFirstDayOfMonth:function (date) {
            var temp = new Date(date.getTime());
            temp.setDate(1);
            return temp.getDay();
        }
    };

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
     * @class YearMonthPicker
     * @extends dorado.widget.Control
     */
    dorado.widget.YearMonthPicker = $extend(dorado.widget.Control, /** @scope dorado.widget.YearMonthPicker.prototype */ {
        $className:"dorado.widget.YearMonthPicker",
        _inherentClassName:"i-year-month-picker",
        _opened:false,
        focusable:true,
        ATTRIBUTES:/** @scope dorado.widget.YearMonthPicker.prototype */ {
            className:{
                defaultValue:"d-year-month-picker"
            },

            /**
             * 选择的年份
             * @attribute
             * @default (new Date).getFullYear()
             * @type int
             */
            year:{
                defaultValue:(new Date).getFullYear()
            },

            /**
             * 选择的月份
             * @attribute
             * @default 0
             * @type int
             */
            month:{
                defaultValue:0
            }
        },

        EVENTS:/** @scope dorado.widget.YearMonthPicker.prototype */ {
            /**
             * 当点击确定按钮之后触发的事件
             * @param {Object} self 事件的发起者，即组件本身。
             * @param {Object} arg 事件参数。
             * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
             * @event
             */
            onPick:{},
            /**
             * 当点击取消按钮之后触发的事件
             * @param {Object} self 事件的发起者，即组件本身。
             * @param {Object} arg 事件参数。
             * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
             * @event
             */
            onCancel:{}
        },

        /**
         * 刷新年份。
         * @param {int} year 新的年份
         * @private
         */
        refreshYear:function (year) {
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
        refreshMonth:function (month) {
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
        updateDate:function (date, month) {
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

        refreshDom:function () {
            var picker = this;
            picker.refreshYear(picker._year);
            picker.refreshMonth(picker._month);
            $invokeSuper.call(this, arguments);
        },

        createDom:function () {
            var monthLabel = "\u4e00\u6708,\u4e8c\u6708,\u4e09\u6708,\u56db\u6708,\u4e94\u6708,\u516d\u6708,\u4e03\u6708,\u516b\u6708,\u4e5d\u6708,\u5341\u6708,\u5341\u4e00\u6708,\u5341\u4e8c\u6708", month_label = monthLabel.split(",");
            var picker = this, doms = {}, dom = $DomUtils.xCreate({
                tagName:"div",
                content:[
                    {
                        tagName:"table",
                        className:"year-table",
                        contextKey:"yearTable",
                        content:[
                            { tagName:"tr", className:"number-row", content:[
                                { tagName:"td" },
                                { tagName:"td" }
                            ] },
                            { tagName:"tr", className:"number-row", content:[
                                { tagName:"td" },
                                { tagName:"td" }
                            ] },
                            { tagName:"tr", className:"number-row", content:[
                                { tagName:"td" },
                                { tagName:"td" }
                            ] },
                            { tagName:"tr", className:"number-row", content:[
                                { tagName:"td" },
                                { tagName:"td" }
                            ] },
                            { tagName:"tr", className:"number-row", content:[
                                { tagName:"td" },
                                { tagName:"td" }
                            ] },
                            { tagName:"tr", className:"btn-row", content:[
                                { tagName:"td", align:"center", contextKey:"preaYearCell" },
                                { tagName:"td", align:"center", contextKey:"nextYearCell" }
                            ]}
                        ]
                    },
                    {
                        tagName:"table",
                        className:"month-table",
                        contextKey:"monthTable",
                        content:[
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[0] },
                                    { tagName:"td", content:month_label[1] }
                                ]
                            },
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[2] },
                                    { tagName:"td", content:month_label[3] }
                                ]
                            },
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[4] },
                                    { tagName:"td", content:month_label[5] }
                                ]
                            },
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[6] },
                                    { tagName:"td", content:month_label[7] }
                                ]
                            },
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[8] },
                                    { tagName:"td", content:month_label[9] }
                                ]
                            },
                            {
                                tagName:"tr",
                                className:"number-row",
                                content:[
                                    { tagName:"td", content:month_label[10] },
                                    { tagName:"td", content:month_label[11] }
                                ]
                            }
                        ]
                    },
                    {
                        tagName:"div",
                        className:"btns-pane",
                        contextKey:"buttonPanel"
                    }
                ]
            }, null, doms);

            var monthLastOverCell, yearLastOverCell;

            picker._doms = doms;

            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(doms.monthTable).mousemove(
                    function (event) {
                        var position = $DomUtils.getCellPosition(event);

                        if (position && position.element && (position.element != monthLastOverCell || !monthLastOverCell)) {
                            if (monthLastOverCell) {
                                $fly(monthLastOverCell).removeClass("hover");
                            }
                            $fly(position.element).addClass("hover");
                            monthLastOverCell = position.element;
                        }
                    }).mouseout(function () {
                        if (monthLastOverCell) {
                            $fly(monthLastOverCell).removeClass("hover");
                        }
                        monthLastOverCell = null;
                    });
            }

            $fly(doms.monthTable).click(function (event) {
                var position = $DomUtils.getCellPosition(event);
                if (position && position.element) {
                    picker.set("month", position.row * 2 + position.column);
                }
            });

            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(doms.yearTable).mousemove(
                    function (event) {
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
                    }).mouseout(function () {
                        if (yearLastOverCell) {
                            $fly(yearLastOverCell).removeClass("hover");
                        }
                        yearLastOverCell = null;
                    });
            }

            $fly(doms.yearTable).click(function (event) {
                var position = $DomUtils.getCellPosition(event);
                if (position && position.element) {
                    if (position.row == 5) {
                        return;
                    }
                    picker.set("year", parseInt(position.element.innerHTML, 10));
                }
            });

            var preYearButton = new dorado.widget.SimpleIconButton({
                iconClass:"prev-year-button",
                listener:{
                    onClick:function () {
                        picker.set("year", picker._year - 10);
                    }
                }
            });

            var nextYearButton = new dorado.widget.SimpleIconButton({
                iconClass:"next-year-button",
                listener:{
                    onClick:function () {
                        picker.set("year", picker._year + 10);
                    }
                }
            });

            preYearButton.render(doms.preaYearCell);
            nextYearButton.render(doms.nextYearCell);

            picker.registerInnerControl(preYearButton);
            picker.registerInnerControl(nextYearButton);

            var okButton = new dorado.touch.Button({
                caption:"\u786e\u5b9a",
                listener:{
                    onClick:function () {
                        picker.fireEvent("onPick", picker);
                    }
                }
            });
            okButton.render(doms.buttonPanel);

            var cancelButton = new dorado.touch.Button({
                caption:"\u53d6\u6d88",
                listener:{
                    onClick:function () {
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
        doOnKeyDown:function (event) {
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
                case 27://esc
                    break;
            }
        }
    });

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
     * @class DatePicker
     * @extends dorado.widget.Control
     */
    dorado.widget.DatePicker = $extend(dorado.widget.Control, /** @scope dorado.widget.DatePicker.prototype */ {
        $className:"dorado.widget.DatePicker",
        focusable:true,
        _inherentClassName:"i-date-picker",

        ATTRIBUTES:/** @scope dorado.widget.DatePicker.prototype */ {
            className:{
                defaultValue:"d-date-picker"
            },

            /**
             * DataPicker对应的日期。
             * @attribute
             * @default new Date()
             * @type Date
             */
            date:{
                defaultValue:new Date()
            },

            /**
             * 是否显示TimeSpinner。
             * @attribute
             * @default false
             * @type boolean
             */
            showTimeSpinner:{
                defaultValue:false
            },

            /**
             * 是否显示今天按钮，仅在渲染前设置有效。
             * @attribute
             * @default true
             * @type boolean
             */
            showTodayButton:{
                defaultValue:true
            },

            /**
             * 是否显示清除按钮，仅在渲染前设置有效。
             * @attribute writeBeforeReady
             * @default true
             * @type boolean
             */
            showClearButton:{
                defaultValue:true
            },

            /**
             * 是否显示确定按钮，仅在渲染前设置有效。
             * @attribute
             * @default true
             * @type boolean
             */
            showConfirmButton:{
                defaultValue:true
            }
        },

        EVENTS:/** @scope dorado.widget.DatePicker.prototype */ {
            /**
             * 当用户点击日期表格上的日期，或者点击确定按钮时触发此事件。
             * @param {Object} self 事件的发起者，即组件本身。
             * @param {Object} arg 事件参数。
             * @param {Object} arg.date 用户选择的日期。
             * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
             * @event
             */
            onPick:{},

            /**
             * 当用户不选择日期，点击关闭按钮后触发此事件
             * @param {Object} self 事件的发起者，即组件本身。
             * @param {Object} arg 事件参数。
             * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
             * @event
             */
            onCancel:{},

            /**
             * 当日期单元格要刷新的时候会触发此事件。
             * @param {Object} self 事件的发起者，即组件本身。
             * @param {Object} arg 事件参数。
             * @param {HtmlElement} arg.cell 要刷新的cell的dom。
             * @param {Date} arg.date 要刷新的cell的日期。
             * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
             * @event
             */
            onRefreshDateCell:{}
        },

        /**
         * 设置Picker的年份。
         * @param {int|String} year 如果直接指定数字，则为要设置的年份，注意为完整年份。如果是字符串，目前只支持prev和next。
         * @param {boolean} refresh 是否要刷新，默认为false。
         * @private
         */
        setYear:function (year, refresh, animate) {
            var picker = this, date = picker._date, oldDay = date.getDate(), oldMonth = date.getMonth(), oldYear = date.getFullYear();

            var source = new Date(date.getTime());

            if (year == "prev") {
                year = oldYear - 1;
            } else {
                if (year == "next") {
                    year = oldYear + 1;
                }
            }

            if (oldMonth == 1 && oldDay == 29) {
                var newMonthDay = DateHelper.getDayCountOfMonth(year, date.getMonth());

                if (oldDay > newMonthDay) {
                    date.setDate(newMonthDay);
                }
            }

            date.setFullYear(year);

            if (refresh) {
                if (animate === false) {
                    picker.refresh();
                } else {
                    picker.doMonthAnimate(source, date);
                }
            }
        },

        /**
         * @param {Date} source
         * @param {Date} target
         * @return {String} the type of animate. l2r, r2l, t2b, b2t
         * @private
         */
        getMonthAnimateType:function (source, target) {
            if (source && target) {
                var syear = source.getFullYear(), tyear = target.getFullYear(), smonth = source.getMonth(), tmonth = target.getMonth();
                if (syear == tyear && smonth == tmonth) return null;
                if (syear == tyear) {
                    return smonth > tmonth ? "r2l" : "l2r";
                } else {
                    return syear > tyear ? "t2b" : "b2t";
                }
            }
            return null;
        },

        /**
         * @param {Date} source
         * @param {Date} target
         * @private
         */
        doMonthAnimate:function (source, target) {
            var picker = this, animateType = picker.getMonthAnimateType(source, target);
            if (picker._doMonthAnimating) {
                return;
            }
            if (animateType == null) {
                picker.refresh();
                return;
            }

            var dateTable = picker._doms.dateTable, dateBlock = picker._doms.dateBlock, dateTableWidth = dateBlock.offsetWidth, dateTableHeight = dateBlock.offsetHeight;
            var sourceRegion, targetRegion, style, animConfig;

            switch (animateType) {
                case "l2r":
                    sourceRegion = 1;
                    targetRegion = 2;
                    style = {
                        width:dateTableWidth * 2,
                        left:0,
                        top:0
                    };
                    animConfig = {
                        left:-1 * dateTableWidth
                    };
                    break;
                case "r2l":
                    sourceRegion = 2;
                    targetRegion = 1;
                    style = {
                        width:dateTableWidth * 2,
                        left:-1 * dateTableWidth,
                        top:0
                    };
                    animConfig = {
                        left:0
                    };
                    break;
                case "b2t":
                    sourceRegion = 1;
                    targetRegion = 3;
                    style = {
                        width:dateTableWidth * 2,
                        left:0,
                        top:0
                    };
                    animConfig = {
                        top:-1 * dateTableHeight
                    };
                    break;
                case "t2b":
                    sourceRegion = 3;
                    targetRegion = 1;
                    style = {
                        width:dateTableWidth * 2,
                        left:0,
                        top:-1 * dateTableHeight
                    };
                    animConfig = {
                        top:0
                    };
                    break;
            }

            picker.refreshDate(source, sourceRegion);
            picker.refreshDate(target, targetRegion);

            $fly(dateTable).css(style);

            picker._visibleDateRegion = targetRegion;

            picker._doMonthAnimating = true;
            $fly(dateTable).animate(animConfig, {
                complete:function () {
                    picker._doMonthAnimating = false;
                    picker.refreshYearMonth();
                }
            });
        },

        /**
         * 设置月份。
         * @param {int|String} month 如果是数字，则为要设置的月份，从0开始计算。如果是字符串，目前只支持prev和next。
         * @param {boolean} refresh 是否要刷新。
         * @private
         */
        setMonth:function (month, refresh, animate) {
            var picker = this, date = picker._date, oldDay = date.getDate(), oldYear = date.getFullYear(), oldMonth = date.getMonth();

            var source = new Date(date.getTime());

            if (month == "prev") {
                if (oldMonth != 0) {
                    month = oldMonth - 1;
                } else {
                    picker.setYear(oldYear - 1);
                    month = 11;
                }
            } else if (month == "next") {
                if (oldMonth != 11) {
                    month = oldMonth + 1;
                } else {
                    picker.setYear(oldYear + 1);
                    month = 0;
                }
            }

            if (oldDay >= 29) {
                var newMonthDay = DateHelper.getDayCountOfMonth(oldYear, month);

                if (oldDay > newMonthDay) {
                    date.setDate(newMonthDay);
                }
            }

            date.setMonth(month);

            if (refresh) {
                if (animate === false) {
                    picker.refresh();
                } else {
                    picker.doMonthAnimate(source, date);
                }
            }
        },

        /**
         * 设置日期。
         * @param {int|String} day 如果是数字，则为要设置的日期，从0开始。如果是数字，只支持prev和next。
         * @param {boolean} refresh 是否要刷新。
         */
        setDate:function (day, refresh) {
            var picker = this, date = picker._date;
            switch (day) {
                case "next":
                    date.setDate(date.getDate() + 1);
                    break;
                case "prev":
                    date.setDate(date.getDate() - 1);
                    break;
                case "nextweek":
                    date.setDate(date.getDate() + 7);
                    break;
                case "prevweek":
                    date.setDate(date.getDate() - 7);
                    break;
                default:
                    if (!isNaN(day)) {
                        date.setDate(day);
                    }
                    break;
            }
            if (refresh) {
                picker.refresh();
            }
        },

        refreshDate:function (target, region) {
            var picker = this, doms = picker._doms, date = target || picker._date, count = 1, day = DateHelper.getFirstDayOfMonth(date), maxDay = DateHelper.getDayCountOfMonth(date.getFullYear(), date.getMonth()), dateTable = doms.dateTable, selectDay = date.getDate(), lastMonthDay = DateHelper.getDayCountOfMonth(date.getFullYear(), (date.getMonth() == 0 ? 11 : date.getMonth() - 1));

            day = (day == 0 ? 7 : day);

            var startI = 0, startJ = 0;
            region = region || picker._visibleDateRegion;

            switch (region) {
                case 2:
                    startJ = 7;
                    break;
                case 3:
                    startI = 6;
                    break;
                case 4:
                    startI = 6;
                    startJ = 7;
                    break;
            }

            for (var i = startI; i < startI + 6; i++) {
                for (var j = startJ; j < startJ + 7; j++) {
                    var cell = dateTable.rows[i].cells[j];
                    if (i == startI) {
                        if (j - startJ >= day) {
                            if (count == selectDay) {
                                cell.className = "selected-date";
                            } else {
                                cell.className = null;
                            }
                            cell.innerHTML = count++;
                            picker.fireEvent("onRefreshDateCell", picker, {
                                date:new Date(date.getFullYear(), date.getMonth(), parseInt(cell.innerHTML, 10)),
                                cell:cell
                            });
                        } else {
                            cell.innerHTML = lastMonthDay - (day - j % 7) + 1;

                            cell.className = "pre-month";
                            picker.fireEvent("onRefreshDateCell", picker, {
                                date:new Date(date.getFullYear(), date.getMonth() - 1, parseInt(cell.innerHTML, 10)),
                                cell:cell
                            });
                        }
                    } else {
                        if (count <= maxDay) {
                            if (count == selectDay) {
                                cell.className = "selected-date";
                            } else {
                                cell.className = null;
                            }
                            cell.innerHTML = count++;
                            picker.fireEvent("onRefreshDateCell", picker, {
                                date:new Date(date.getFullYear(), date.getMonth(), parseInt(cell.innerHTML, 10)),
                                cell:cell
                            });
                        } else {
                            cell.innerHTML = count++ - maxDay;
                            cell.className = "next-month";
                            picker.fireEvent("onRefreshDateCell", picker, {
                                date:new Date(date.getFullYear(), date.getMonth() + 1, parseInt(cell.innerHTML, 10)),
                                cell:cell
                            });
                        }
                    }
                }
            }
        },

        /**
         * 刷新月份和年份。
         * @private
         */
        refreshYearMonth:function () {
            var picker = this, doms = picker._doms, date = picker._date;
            doms.yearMonthLabel.innerHTML = (date.getMonth() + 1) + "&nbsp;&nbsp;" + date.getFullYear();
        },

        refreshSpinner:function () {
            var picker = this, spinner = picker._timeSpinner, date = picker._date;
            if (picker._showTimeSpinner && spinner) {
                spinner.set({
                    hours:date.getHours(),
                    minutes:date.getMinutes(),
                    seconds:date.getSeconds()
                });
            }
        },

        refreshDom:function (dom) {
            var picker = this;

            picker.refreshDate();
            picker.refreshYearMonth();

            if (picker._showTimeSpinner) {
                picker.doShowTimeSpinner();
                picker.refreshSpinner();
            } else {
                picker.doHideTimeSpinner();
            }

            $invokeSuper.call(this, arguments);
        },

        createDom:function () {
            var allWeeks = "\u65e5,\u4e00,\u4e8c,\u4e09,\u56db,\u4e94,\u516d", weeks = allWeeks.split(",");

            var dateRows = [];

            for (var i = 0; i < 12; i++) {
                dateRows.push({
                    tagName:"tr",
                    content:[
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        },
                        {
                            tagName:"td"
                        }
                    ]
                });
            }

            var picker = this, doms = {}, dom = $DomUtils.xCreate({
                tagName:"div",
                content:[
                    {
                        tagName:"div",
                        className:"year-month-block",
                        content:[
                            {
                                tagName:"div",
                                className:"pre-button-div",
                                contextKey:"prevButtonDiv"
                            },
                            {
                                tagName:"div",
                                className:"next-button-div",
                                contextKey:"nextButtonDiv"
                            },
                            {
                                tagName:"div",
                                className:"year-month-label",
                                contextKey:"yearMonthLabel"
                            }
                        ]
                    },
                    {
                        tagName:"table",
                        cellPadding:0,
                        cellSpacing:0,
                        border:0,
                        className:"date-header",
                        contextKey:"dateHeader",
                        content:[
                            {
                                tagName:"tr",
                                className:"header",
                                content:[
                                    {
                                        tagName:"td",
                                        content:weeks[0]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[1]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[2]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[3]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[4]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[5]
                                    },
                                    {
                                        tagName:"td",
                                        content:weeks[6]
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        tagName:"div",
                        className:"date-block",
                        contextKey:"dateBlock",
                        content:{
                            tagName:"table",
                            cellPadding:0,
                            cellSpacing:0,
                            border:0,
                            className:"date-table",
                            contextKey:"dateTable",
                            content:dateRows
                        }
                    },
                    {
                        tagName:"div",
                        contextKey:"buttonBlock",
                        className:"button-block"
                    },
                    {
                        tagName:"div",
                        contextKey:"spinnerBlock",
                        className:"spinner-block"
                    }
                ]
            }, null, doms);

            picker._doms = doms;

            if (picker._showTodayButton) {
                var todayButton = new dorado.touch.Button({
                    caption:"\u4eca\u5929",
                    listener:{
                        onClick:function () {
                            var now = new Date(), date = picker._date;
                            date.setFullYear(now.getFullYear());
                            date.setMonth(now.getMonth());
                            date.setDate(now.getDate());
                            picker.set("date", picker._date);
                        }
                    }
                });
                todayButton.render(doms.buttonBlock);
                picker.registerInnerControl(todayButton);
            }

            if (false && picker._showClearButton) {
                var clearButton = new dorado.touch.Button({
                    caption:"\u6e05\u7a7a",
                    listener:{
                        onClick:function () {
                            picker.fireEvent("onPick", picker, {
                                date:null
                            });
                        }
                    }
                });
                clearButton.render(doms.buttonBlock);
                picker.registerInnerControl(clearButton);
            }

            if (picker._showConfirmButton) {
                var confirmButton = new dorado.touch.Button({
                    caption:"\u786e\u5b9a",
                    listener:{
                        onClick:function () {
                            picker.fireEvent("onPick", picker, {
                                date:new Date(picker._date.getTime())
                            });
                        }
                    }
                });
                confirmButton.render(doms.buttonBlock);
                picker.registerInnerControl(confirmButton);
            }

            var lastOverCell = null, dateTable = doms.dateTable;

            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(dateTable).mousemove(
                    function (event) {
                        var position = $DomUtils.getCellPosition(event);

                        if (position && position.element && (position.element != lastOverCell || !lastOverCell)) {
                            if (position.row <= 0) {
                                return;
                            }
                            if (lastOverCell) {
                                $fly(lastOverCell).removeClass("hover");
                            }
                            $fly(position.element).addClass("hover");
                            lastOverCell = position.element;
                        }
                    }).mouseout(function () {
                        if (lastOverCell) {
                            $fly(lastOverCell).removeClass("hover");
                        }
                        lastOverCell = null;
                    });
            }

            $fly(dateTable).click(function (event) {
                var position = $DomUtils.getCellPosition(event), element = position.element, date = picker._date;

                if (position && element) {
                    var className = element.className;
                    if (className.indexOf("next-month") != -1) {
                        picker.setMonth(date.getMonth() + 1);
                    } else {
                        if (className.indexOf("pre-month") != -1) {
                            picker.setMonth(date.getMonth() - 1);
                        }
                    }
                    picker.setDate(parseInt(element.innerHTML, 10), true);

                    picker.fireEvent("onPick", picker, {
                        date:new Date(picker._date.getTime())
                    });
                }
            });

            var prevYearButton = new dorado.widget.SimpleIconButton({
                iconClass:"pre-year-button",
                listener:{
                    onClick:function () {
                        picker.setYear("prev", true);
                    }
                }
            });

            var prevMonthButton = new dorado.widget.SimpleIconButton({
                iconClass:"pre-month-button",
                listener:{
                    onClick:function () {
                        picker.setMonth("prev", true);
                    }
                }
            });

            prevYearButton.render(doms.prevButtonDiv);
            prevMonthButton.render(doms.prevButtonDiv);

            var nextMonthButton = new dorado.widget.SimpleIconButton({
                iconClass:"next-month-button",
                listener:{
                    onClick:function () {
                        picker.setMonth("next", true);
                    }
                }
            });

            var nextYearButton = new dorado.widget.SimpleIconButton({
                iconClass:"next-year-button",
                listener:{
                    onClick:function () {
                        picker.setYear("next", true);
                    }
                }
            });

            nextMonthButton.render(doms.nextButtonDiv);
            nextYearButton.render(doms.nextButtonDiv);

            picker.registerInnerControl(prevYearButton);
            picker.registerInnerControl(prevMonthButton);
            picker.registerInnerControl(nextMonthButton);
            picker.registerInnerControl(nextYearButton);

            $fly(doms.yearMonthLabel).click(function () {
                picker.showYMPicker();
            });

            picker._visibleDateRegion = 1;

            return dom;
        },

        /**
         * @private
         */
        doShowTimeSpinner:function () {
            var picker = this, spinner = picker._timeSpinner;
            if (!spinner) {
                spinner = picker._timeSpinner = new dorado.widget.DateTimeSpinner({
                    type:"time",
                    width:100,
                    listener:{
                        onPost:function () {
                            var date = picker._date;
                            date.setHours(spinner.get("hours"));
                            date.setMinutes(spinner.get("minutes"));
                            date.setSeconds(spinner.get("seconds"));
                        }
                    }
                });
                spinner.render(picker._doms.spinnerBlock);
                picker.registerInnerControl(spinner);
            }
            $fly(spinner._dom).css("display", "");
        },

        /**
         * @private
         */
        doHideTimeSpinner:function () {
            var picker = this, spinner = picker._timeSpinner;
            if (spinner) {
                $fly(spinner._dom).css("display", "none");
            }
        },

        /**
         * 显示年份和月份选择器。
         * 每个日期选择器都会对应一个年份和月份选择器。
         * @private
         */
        showYMPicker:function () {
            var picker = this, dom = picker._dom, ymPicker = picker._yearMonthPicker;
            if (!ymPicker && dom) {
                ymPicker = picker._yearMonthPicker = new dorado.widget.YearMonthPicker({
                    style:{
                        display:"none"
                    },
                    listener:{
                        onPick:function () {
                            var ymPicker = picker._yearMonthPicker, year = ymPicker._year, month = ymPicker._month;
                            picker.setYear(year, false, false);
                            picker.setMonth(month, true, false);

                            picker.hideYMPicker();
                        },
                        onCancel:function () {
                            picker.hideYMPicker();
                        }
                    }
                });
                ymPicker.render(dom);
                picker.registerInnerControl(ymPicker);
            }

            ymPicker.updateDate(picker._date);

            if (!ymPicker._rendered) {
                ymPicker.render(document.body);
            }

            ymPicker._opened = true;
            $fly(ymPicker._dom).css("display", "").slideIn("t2b");
        },

        /**
         * 隐藏日期选择器。
         * @param {boolean} animate 是否使用动画效果。
         * @private
         */
        hideYMPicker:function (animate) {
            var picker = this, ymPicker = picker._yearMonthPicker;
            if (ymPicker) {
                if (animate === false) {
                    $fly(ymPicker._dom).css("display", "none");
                } else {
                    $fly(ymPicker._dom).slideOut("b2t");
                }

                ymPicker._opened = false;

                dorado.widget.setFocusedControl(picker);
            }
        },

        doOnKeyDown:function (event) {
            var picker = this, date, ymPicker = picker._yearMonthPicker;
            if (ymPicker && ymPicker._opened) {
                ymPicker.onKeyDown(event);
            } else {
                date = picker._date;
                switch (event.keyCode) {
                    case 89: //Y
                        if (event.ctrlKey) {
                            picker.showYMPicker();
                        }
                        break;
                    case 37://left arrow
                        if (!event.ctrlKey) {
                            picker.setDate("prev", true);
                        } else {
                            picker.setMonth("prev", true);
                        }
                        break;
                    case 38://up arrow
                        if (!event.ctrlKey) {
                            picker.setDate("prevweek", true);
                        } else {
                            picker.setYear("prev", true);
                        }
                        break;
                    case 39://right arrow
                        if (!event.ctrlKey) {
                            picker.setDate("next", true);
                        } else {
                            picker.setMonth("next", true);
                        }

                        break;
                    case 40://down arrow
                        if (!event.ctrlKey) {
                            picker.setDate("nextweek", true);
                        } else {
                            picker.setYear("next", true);
                        }
                        break;
                    case 13://enter
                        return true;
                    case 27://esc
                        if (ymPicker && ymPicker._opened) {
                            return false;
                        }
                        break;
                }
            }
        }
    });

})();