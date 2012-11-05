/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

(function() {
    var weekArray = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];

    var HOLD_EVENT_KEY = "cal_event", HOLD_MONTHVIEW_KEY = "cal_monthview";

    var INTERVAL_A_DAY = 86400000;

    function intervalToDay(interval) {
        return interval / INTERVAL_A_DAY;
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
        var newStartTime = new XDate(startTime.getFullYear(), startTime.getMonth(), startTime.getDate(),
            oldStartTime.getHours(), oldStartTime.getMinutes(), oldStartTime.getSeconds());

        event.endTime = new XDate(newStartTime.getTime() + (event.endTime - event.startTime));
        event.startTime = newStartTime;
    }

    dorado.widget.MonthView = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "d-month-view"
            },
            date: {
                defaultValue: function() {
                    return new XDate()
                },
                setter: function(value) {
                    this._date = value;
                    if (this._rendered && this._visible) {
                        this.refreshOnDateChange();
                    }
                }
            },
            eventSource: {}
        },
        EVENTS: {
            onDateChange: {}
        },
        doOnResize: function() {
            this.refreshEvents();
        },
        prev: function() {
            var view = this, date = view._date;
            date.addMonths(-1, true);
            view.refreshOnDateChange();
        },
        next: function() {
            var view = this, date = view._date;
            date.addMonths(1, true);
            view.refreshOnDateChange();
        },
        createDom: function() {
            var view = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                content: [{
                    tagName: "table",
                    className: "month-table",
                    contextKey: "monthTable",
                    content: [{
                        tagName: "thead",
                        contextKey: "monthTableHead"
                    }, {
                        tagName: "tbody",
                        contextKey: "monthTableBody"
                    }]
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
                    drop: function() {
                        var column = td.cellIndex, row = td.parentNode._rowIndex - 1, date = view.getDateByPosition(row, column);
                        if (date) {
                            if (view._draggingevent) {
                                resetEventDate(view._draggingevent, date);
                                view.refreshEvents();
                            }
                        }
                    },
                    over: function() {
                    },
                    out: function() {
                    }
                });
            }

            for (var i = 0; i < rowCount; i++) {
                var tr = document.createElement("tr");
                tr._rowIndex = i;
                if (i == 0) tr.className = "header";
                for (var j = 0; j < columnCount; j++) {
                    var td = document.createElement(i == 0 ? "th" : "td");

                    if (i == 0) {
                        td.innerHTML = weekArray[j];
                    } else {
                        makeDroppable(td);
                        var div = document.createElement("div");
                        div.className = "date";
                        td.appendChild(div);
                    }
                    tr.appendChild(td);
                }
                if (i == 0) {
                    doms.monthTableHead.appendChild(tr);
                } else {
                    doms.monthTableBody.appendChild(tr);
                }
            }

            return dom;
        },
        refreshDom: function(dom) {
            $invokeSuper.call(this, [dom]);
            this.refreshOnDateChange();
        },
        getEventSource: function() {
            if (this._eventSource) return this._eventSource;
            if (this._parent && this._parent._parent && this._parent._parent instanceof dorado.widget.Calendar) {
                return this._parent._parent._eventSource;
            }
        },
        refreshEvents: function() {
            var view = this, eventRange = view.getDateRange(), eventSource = view.getEventSource(), events;

            if (eventSource) {
                events = eventSource.getEventsByRange(eventRange[0], eventRange[1]);
            } else {
                return;
            }

            if (view._currentMonthEvents) {
                var cacheEvents = view._currentMonthEvents;
                for (var i = 0; i < cacheEvents.length; i++) {
                    var eventDom = cacheEvents[i];
                    $fly(eventDom).css({ left: "", top: "" });
                    dorado.widget.MonthEventPool.returnObject(eventDom);
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

            var view = this, doms = view._doms, monthTable = doms.monthTable, monthTableBody = doms.monthTableBody,
                eventsHolder = doms.eventsHolder, tableOffset = $fly(monthTable).offset(), cellWidth = $fly(monthTable).width() / 7;

            var i, j, k, l, cellOffset, eventPosition, viewMap = [];

            var fillCell = function(index, element, eventIndex) {
                var cell = viewMap[index];
                if (!cell) viewMap[index] = cell = [];
                if (element === true && eventIndex !== undefined) {
                    //fill for long event cell
                    cell[eventIndex] = true;
                } else {
                    if (element === true) console.log("true but eventIndex === undefined");
                    var i = 0;
                    while (true) {
                        if (cell[i] === undefined) {
                            cell[i] = element;
                            return i;
                        }
                        i++;
                    }
                    //cell.push(element);
                }
            };

            for (i = 0, k = allDayEvents.length; i < k; i++) {
                var alldayEvent = allDayEvents[i];
                eventPosition = view.getPositionOfDate(alldayEvent.startTime);

                var dayCount = Math.ceil(intervalToDay(alldayEvent.endTime - alldayEvent.startTime)),
                    startPosition = eventPosition.rowIndex * 7 + eventPosition.columnIndex;

                alldayEvent.dayCount = dayCount;

                var eventIndex = fillCell(startPosition, alldayEvent);

                //console.log("event.title:" + alldayEvent.title, eventPosition.rowIndex, eventPosition.columnIndex, dayCount);

                for (j = 1; j < dayCount && startPosition + j < 42; j++) {
                    if ((startPosition + j) % 7 == 0) {
                        eventIndex = fillCell(startPosition + j, alldayEvent);
                    } else {
                        fillCell(startPosition + j, true, eventIndex);
                    }
                }
            }

            for (i = 0; i < events.length; i++) {
                var event = events[i];
                if (!isAllDay(event)) {
                    event.dayCount = 1;
                    eventPosition = view.getPositionOfDate(event.startTime);
                    //console.log("event.title:" + event.title, eventPosition.rowIndex, eventPosition.columnIndex);
                    fillCell(eventPosition.rowIndex * 7 + eventPosition.columnIndex, event);
                }
            }

            //console.log(viewMap);

            var dateRange = view.getDateRange(), refCell = monthTableBody.rows[1].cells[1],
                cellHeight = $fly(refCell).height(), showEventsCount = Math.floor((cellHeight - 20) / 20);

            for (i = 0, k = viewMap.length; i < k; i++) {
                var cellConfig = viewMap[i];
                if (cellConfig) {
                    for (j = 0, l = cellConfig.length; j < l && j < showEventsCount; j++) {
                        alldayEvent = cellConfig[j];
                        if (alldayEvent === undefined || alldayEvent === true) continue;

                        var dom = view.getEventDom(alldayEvent), eventWidth = cellWidth,
                            cell = monthTableBody.rows[Math.floor(i / 7)].cells[i % 7];

                        if (alldayEvent.dayCount > 1) {
                            var row = Math.floor(i / 7), firstColumnDate = dateRange[0].clone().addDays(row * 7),
                            lastColumnDate = dateRange[0].clone().addDays((row + 1) * 7).minimizeTime(),
                            startDate = alldayEvent.startTime.clone().minimizeTime(), endDate = alldayEvent.endTime.clone().maximizeTime();

                            //console.log(firstColumnDate.toString(), lastColumnDate.toString());

                            if (alldayEvent.endTime > lastColumnDate) {
                                endDate = lastColumnDate;
                            }
                            if (alldayEvent.startTime < firstColumnDate) {
                                startDate = firstColumnDate;
                            }
                            eventWidth = startDate.diffDays(endDate) * cellWidth;
                            $fly(dom).addClass("long-event");
                        } else {
                            $fly(dom).removeClass("long-event");
                        }

                        cellOffset = $fly(cell).offset();

                        $fly(dom).css({
                            width: eventWidth,
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
            var date = this._date || new XDate(), firstDayOfMonth = date.clone().setDate(1),
                startDate = firstDayOfMonth.clone().addDays(-firstDayOfMonth.getDay()).minimizeTime(),
                endDate = startDate.clone().addDays(41).maximizeTime(true);

            //console.log("date range:[" + startDate.toString() + "," + endDate.toString() + "]");

            return [startDate, endDate];
        },
        getDateByPosition: function(row, column) {
            var dateRange = this.getDateRange(), minDate = dateRange[0];
            return new XDate(minDate.getFullYear(), minDate.getMonth(), minDate.getDate() + row * 7 + column, 0, 0, 0);
        },
        getPositionOfDate: function(date) {
            var range = this.getDateRange(), startDate = range[0], dayCount = Math.round(startDate.diffDays(date.clone().minimizeTime()));

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
            var view = this, date = view._date || new XDate();
            view.fireEvent("onDateChange", view, {
                date: date
            });
            view.refreshDate(date);
            view.refreshEvents();
        },
        refreshDate: function(date) {
            var dateTableBody = this._doms.monthTableBody, count = 1,
                day = date.clone().setDate(1).getDay(), selectDay = date.getDate(),
                maxDay = XDate.getDaysInMonth(date.getFullYear(), date.getMonth()),
                lastMonthDay = XDate.getDaysInMonth(date.getFullYear(), (date.getMonth() == 0 ? 11 : date.getMonth() - 1));

            day = (day == 0 ? 7 : day);

            var startI = 0, startJ = 0;

            for (var i = startI; i < startI + 6; i++) {
                for (var j = startJ; j < startJ + 7; j++) {
                    var cell = dateTableBody.rows[i].cells[j].firstChild;
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
        }
    });
})();
