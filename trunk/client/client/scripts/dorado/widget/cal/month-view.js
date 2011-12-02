(function() {
    var weekArray = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
    var DateHelper = {
        getDayCountOfMonth: function(year, month) {
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
        getFirstDayOfMonth: function(date) {
            var temp = new Date(date.getTime());
            temp.setDate(1);
            return temp.getDay();
        }
    };

    var refreshDate = function(date, dateTable) {
        var count = 1, day = DateHelper.getFirstDayOfMonth(date), selectDay = date.getDate(),
            maxDay = DateHelper.getDayCountOfMonth(date.getFullYear(), date.getMonth()),
            lastMonthDay = DateHelper.getDayCountOfMonth(date.getFullYear(), (date.getMonth() == 0 ? 11 : date.getMonth() - 1));

        day = (day == 0 ? 7 : day);

        var startI = 1, startJ = 0;

        for (var i = startI; i < startI + 6; i++) {
            for (var j = startJ; j < startJ + 7; j++) {
                var cell = dateTable.rows[i].cells[j].firstChild;
                if (i == startI) {
                    if (j - startJ >= day) {
                        if (count == selectDay) {
                            cell.className = "selected-date";
                        } else {
                            cell.className = null;
                        }
                        cell.innerHTML = count++;
                    } else {
                        cell.innerHTML = lastMonthDay - (day - j % 7) + 1;

                        cell.className = "premonth";
                    }
                } else {
                    if (count <= maxDay) {
                        if (count == selectDay) {
                            cell.className = "selected-date";
                        } else {
                            cell.className = null;
                        }
                        cell.innerHTML = count++;
                    } else {
                        cell.innerHTML = count++ - maxDay;
                        cell.className = "nextmonth";
                    }
                }
            }
        }
    };

    var HOLD_EVENT_KEY = "cal_event", HOLD_MONTHVIEW_KEY = "cal_monthview";

    var INTERVAL_A_DAY = 86400000;

    function intervalToDay(interval) {
        return interval / INTERVAL_A_DAY;
    }

    function minimizeDate(date) {
        var result = new Date(date.getFullYear(), date.getMonth(), date.getDate());
        result.setHours(0);
        result.setMinutes(0);
        result.setSeconds(0);

        return result;
    }

    function maxmizeDateIfPosibble(date) {
        var possible = !(date.getMinutes() == 0 && date.getHours() == 0 && date.getSeconds() == 0);
        var result = new Date(date.getFullYear(), date.getMonth(), date.getDate());

        if (possible) {
            result.setHours(23);
            result.setMinutes(59);
            result.setSeconds(59);
        }

        return result;
    }

    dorado.widget.MonthEventPool = new dorado.util.ObjectPool({
        makeObject: function() {
            var dom = document.createElement("div");
            dom.className = "event";
            dom.style.position = "absolute";

            $fly(dom).draggable({
                addClasses: false,
                helper: "clone",
                scope: "calendar",
                start: function() {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), monthview = jQuery.data(dom, HOLD_MONTHVIEW_KEY);
                    if (monthview && event) {
                        monthview._draggingevent = event;
                    }
                },
                stop: function() {
                    var monthview = jQuery.data(dom, HOLD_MONTHVIEW_KEY);
                    if (monthview) {
                        //monthview._draggingevent = null;
                    }
                    jQuery.data(dom, HOLD_EVENT_KEY, null);
                    jQuery.data(dom, HOLD_MONTHVIEW_KEY, null);
                }
            });

            return dom;
        }
    });

    function resetEventDate(event, startTime) {
        var oldStartTime = event.startTime;
        var newStartTime = new Date(startTime.getFullYear(), startTime.getMonth(), startTime.getDate(),
            oldStartTime.getHours(), oldStartTime.getMinutes(), oldStartTime.getSeconds());
        event.endTime = new Date(newStartTime.getTime() + (event.endTime - event.startTime));
        event.startTime = newStartTime;
    }

    dorado.widget.MonthView = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "month-view"
            },
            date: {
                setter: function(value) {
                    this._date = value;
                    if (this._rendered && this._visible) {
                        this.refreshOnDateChange();
                    }
                }
            }
        },
        doOnResize: function() {
            this.refreshEvents();
        },
        createDom: function() {
            var view = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                content: [{
                    tagName: "table",
                    className: "month-table",
                    contextKey: "monthTable"
                }, {
                    tagName: "div",
                    className: "events-holder",
                    contextKey: "eventsHolder"
                }]
            }, null, doms);

            view._doms = doms;

            var rowCount = 7, columnCount = 7;

            function makeDroppable(td) {
                $fly(td).droppable({
                    hoverClass: 'event-drag-over',
                    scope: "calendar",
                    greedy: false,
                    tolerance: "pointer",
                    drop: function(ev, ui) {
                        var column = td.cellIndex, row = td.parentNode._rowIndex - 1, date = view.getDateByPosition(row, column);
                        if (date) {
                            if (view._draggingevent) {
                                resetEventDate(view._draggingevent, date);
                                view.refreshEvents();
                            }
                        }
                    },
                    over: function(ev, ui) {
                    },
                    out: function(ev, ui) {
                    }
                });
            }

            for (var i = 0; i < rowCount; i++) {
                var tr = document.createElement("tr");
                tr._rowIndex = i;
                if (i == 0) tr.className = "header";
                for (var j = 0; j < columnCount; j++) {
                    var td = document.createElement("td");
                    makeDroppable(td);
                    if (i == 0) {
                        td.innerHTML = weekArray[j];
                    } else {
                        var div = document.createElement("div");
                        div.className = "date";
                        td.appendChild(div);
                    }
                    tr.appendChild(td);
                }
                doms.monthTable.appendChild(tr);
            }

            return dom;
        },
        refreshEvents: function() {
            //console.log(this._date.toString(), this.getDateRange()[0].toString(), this.getDateRange()[1].toString());
            var eventRange = this.getDateRange(), eventSource = this._parent._parent._eventSource,
                events;

            if (eventSource) {
                events = eventSource.getEventsByRange(eventRange[0], eventRange[1]);
            } else {
                return;
            }

            var view = this;

            if (this._currentMonthEvents) {
                var cacheEvents = this._currentMonthEvents;
                for (var i = 0; i < cacheEvents.length; i++) {
                    var edom = cacheEvents[i];
                    $fly(edom).css({ left: "", top: "" });
                    dorado.widget.MonthEventPool.returnObject(edom);
                }
                view._currentMonthEvents = [];
            }

            view.positionEvents(events);
        },
        positionEvents: function(events) {
            function isAllDay(event) {
                return event.allDay || (event.endTime - event.startTime) > INTERVAL_A_DAY;
            }

            var allDayEvents = events.select(isAllDay).sort(function(a, b) {
                return a.startTime > b.startTime;
            });

            var view = this, monthTable = view._doms.monthTable, tableOffset = $fly(monthTable).offset(),
                cellOffset, eventsHolder = view._doms.eventsHolder, cellWidth = $fly(monthTable).width() / 7;

            //console.log(allDayEvents);

            var i, j, k, l, event, position, viewMap = [];

            var fillCell = function(index, element) {
                var cell = viewMap[index];
                if (!cell) viewMap[index] = cell = [];
                cell.push(element);
            };

            for (i = 0, k = allDayEvents.length; i < k; i++) {
                event = allDayEvents[i];
                position = view.getPositionOfDate(event.startTime);

                var dayCount = Math.ceil(intervalToDay(event.endTime - event.startTime)),
                    startPosition = position.rowIndex * 7 + position.columnIndex;

                event.dayCount = dayCount;

                fillCell(startPosition, event);

                for (j = 1; j < dayCount && startPosition + j < 42; j++) {
                    if ((startPosition + j) % 7 == 0) {
                        fillCell(startPosition + j, event);
                    } else {
                        fillCell(startPosition + j, true);
                    }
                }
            }

            for (i = 0; i < events.length; i++) {
                event = events[i];
                if (!isAllDay(event)) {
                    event.dayCount = 1;
                    position = view.getPositionOfDate(event.startTime);
                    fillCell(position.rowIndex * 7 + position.columnIndex, event);
                }
            }

            var dateRange = view.getDateRange();

            //console.log("viewMap length:" + viewMap.length);
            //console.log(viewMap);

            var tempCell = monthTable.rows[1].cells[1];

            var cellHeight = $fly(tempCell).height(), showEventsCount = Math.floor((cellHeight - 20) / 20);

            for (i = 0, k = viewMap.length; i < k; i++) {
                var cellConfig = viewMap[i];
                if (cellConfig) {
                    for (j = 0, l = cellConfig.length; j < l && j < showEventsCount; j++) {
                        event = cellConfig[j];
                        if (event === true) continue;
                        var dom = view.getEventDom(event), cell = monthTable.rows[Math.floor(i / 7) + 1].cells[i % 7];

                        cellOffset = $fly(cell).offset();

                        var width = cellWidth;

                        if (event.dayCount > 1) {
                            var row = Math.floor(i / 7), firstColumnDate = new Date(dateRange[0].getTime() + row * 7 * INTERVAL_A_DAY),
                            lastColumnDate = new Date(dateRange[0].getTime() + (row + 1) * 7 * INTERVAL_A_DAY - 1000),
                            startDate = minimizeDate(event.startTime), endDate = maxmizeDateIfPosibble(event.endTime);

                            //console.log(firstColumnDate.toString(), lastColumnDate.toString());

                            if (event.endTime > lastColumnDate) {
                                endDate = lastColumnDate;
                            }
                            if (event.startTime < firstColumnDate) {
                                startDate = firstColumnDate;
                            }
                            width = intervalToDay(endDate - startDate) * cellWidth;
                            $fly(dom).addClass("long-event");
                        } else {
                            $fly(dom).removeClass("long-event");
                        }

                        $fly(dom).css({
                            width: width,
                            left: parseInt(cellOffset.left, 10) - parseInt(tableOffset.left, 10),
                            top: parseInt(cellOffset.top, 10) - parseInt(tableOffset.top, 10) + (j + 1) * 20
                        });

                        eventsHolder.appendChild(dom);
                    }
                    if (cellConfig.length < showEventsCount) {
                        //TODO let user see it.
                    }
                }
            }
        },
        getDateRange: function() {
            var date = this._date || new Date(), firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
            var startDate = new Date(firstDayOfMonth.getFullYear(), firstDayOfMonth.getMonth(), 1 - firstDayOfMonth.getDay(), 0, 0, 0),
                endDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + 41, 23, 59, 59);

            //console.log("startDate:" + startDate.toString());

            return [startDate, endDate];
        },
        getDateByPosition: function(row, column) {
            var dateRange = this.getDateRange(), minDate = dateRange[0];
            return new Date(minDate.getFullYear(), minDate.getMonth(), minDate.getDate() + row * 7 + column, 0, 0, 0);
        },
        getPositionOfDate: function(date) {
            var range = this.getDateRange(), startDate = range[0], dayCount = Math.floor(intervalToDay(date - startDate));

            //console.log(date.toString(), Math.floor(dayCount / 7), dayCount % 7, startDate.toString(), range[1].toString());

            return {
                rowIndex: Math.floor(dayCount / 7),
                columnIndex: dayCount % 7
            };
        },
        getEventDom: function(event) {
            var dom = dorado.widget.MonthEventPool.borrowObject();
            dom.innerHTML = event.title;

            jQuery.data(dom, HOLD_EVENT_KEY, event);
            jQuery.data(dom, HOLD_MONTHVIEW_KEY, this);

            if (!this._currentMonthEvents) this._currentMonthEvents = [];
            this._currentMonthEvents.push(dom);

            return dom;
        },
        refreshOnDateChange: function() {
            var view = this, date = view._date || new Date();

            refreshDate(date, view._doms.monthTable);

            view.refreshEvents();
        }
    });
})();