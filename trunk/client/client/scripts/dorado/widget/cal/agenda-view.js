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
    var HOLD_EVENT_KEY = "cal_event", HOLD_AGENDAVIEW_KEY = "cal_agendaview", INTERVAL_A_DAY = 86400000;

    function intervalToDay(interval) {
        return interval / INTERVAL_A_DAY;
    }

    var fakeEvent;

    dorado.widget.AgendaEventPool = new dorado.util.ObjectPool({
        makeObject: function() {
            var dom = document.createElement("div"), title = document.createElement("div");
            dom.className = "event";
            title.className = "title";
            dom.appendChild(title);

            var topHandle = document.createElement("div"), bottomHandle = document.createElement("div");
            topHandle.className = "top-handle";
            bottomHandle.className = "bottom-handle";
            topHandle.style.position = "absolute";
            bottomHandle.style.position = "absolute";

            dom.appendChild(topHandle);
            dom.appendChild(bottomHandle);

            dom.style.position = "absolute";

            var startHeight, startTop, startLeft, startScrollTop, gridUnit, minHeight, widthPerSlot, toAllDay = false;

            var grid = function(value) {
                return Math.floor(value / gridUnit) * gridUnit;
            };

            var fakeOffset, alldayEvent;

            $fly(dom).draggable({
                addClasses: false,
                scope: "calendar",
                axis: "fake",
                helper: function() {
                    if (!fakeEvent) {
                        fakeEvent = document.createElement("div");
                        fakeEvent.className = "fake-event";
                    }

                    return fakeEvent;
                },
                start: function(evt) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                        $fly(fakeEvent).html(event.title);
                    }

                    var holderOffset = $fly(agendaview._doms.eventTableWrap).offset(), viewoffset = $fly(agendaview._dom).offset(),
                        currentLeft = parseInt($fly(dom).css("left"), 10), currentTop = parseInt($fly(dom).css("top"), 10);

                    widthPerSlot = Math.floor(($fly(agendaview._doms.eventTable).width() - 100) / agendaview._showDays);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).prop("scrollTop");
                    startHeight = $fly(dom).outerHeight(true);

                    fakeOffset = {
                        left: holderOffset.left + 100 - viewoffset.left,
                        top: holderOffset.top - viewoffset.top
                    };

                    agendaview._dom.appendChild(fakeEvent);
                    agendaview._dragoverAllDayTable = false;

                    $fly(fakeEvent).css({
                        width: widthPerSlot - 10,
                        height: startHeight,
                        left: currentLeft - currentLeft % widthPerSlot + fakeOffset.left,
                        top:  currentTop + fakeOffset.top - startScrollTop
                    }).bringToFront();

                    startTop = parseInt($fly(fakeEvent).css("top"), 10);
                    startLeft = parseInt($fly(fakeEvent).css("left"), 10);
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;
                    alldayEvent = $fly(dom).addClass("dragging-event").hasClass("allday-event");
                    toAllDay = alldayEvent;
                    //console.log("start top:" + startTop + "\tcurrentTop:" + currentTop + "\tstartScrollTop:" + startScrollTop);

                    $log("event dragstart handle:" + evt.target.className);
                },
                drag: function(event) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        scrollTop = $fly(agendaview._doms.eventTableWrap).prop("scrollTop"),
                        mouseChange = event.pageY - inst.originalPageY,
                        vertChange = mouseChange + scrollTop - startScrollTop;

                    if (agendaview._dragoverAllDayTable) {
                        var alldayTableTop = $fly(agendaview._doms.alldayEventTable).position().top;
                        $fly(fakeEvent).css({
                            top: alldayTableTop,
                            height: ""
                        });
                        toAllDay = true;
                    } else {
                        $fly(fakeEvent).css({
                            top: startTop + mouseChange + grid(vertChange) - vertChange,
                            height: startHeight
                        });
                        toAllDay = false;
                    }

                    //console.log("agendaview._dragoverColumn:" + agendaview._dragoverColumn);

                    if (agendaview._dragoverColumn != null) {
                        $fly(fakeEvent).css("left", agendaview._dragoverColumn * widthPerSlot + fakeOffset.left);
                    }
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        if (!event.allDay) {
                            if (toAllDay) {
                                var changeDays = Math.round((parseInt($fly(fakeEvent).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays));

                                event.startTime = event.startTime.clone().minimizeTime().addDays(changeDays);
                                event.endTime = event.endTime.clone().maximizeTime().addDays(changeDays);
                                event.allDay = true;

                                //console.log(event.startTime.toString("i"), event.endTime.toString("i"));

                                agendaview.refreshOnDateChange();
                            } else {
                                var scrollTop = $fly(agendaview._doms.eventTableWrap).prop("scrollTop");
                                var changeVUnit = (scrollTop - startScrollTop + parseInt($fly(fakeEvent).css("top"), 10) - startTop) / gridUnit;
                                var changeHUnit = (parseInt($fly(fakeEvent).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays);

                                var changeInterval = changeVUnit * 1800000 + changeHUnit * INTERVAL_A_DAY;

                                event.startTime = new XDate(event.startTime.getTime() + changeInterval);
                                event.endTime = new XDate(event.endTime.getTime() + changeInterval);

                                agendaview.refreshOnDateChange();
                            }
                        } else {
                            if (toAllDay) {
                                var changeDays = Math.round((parseInt($fly(fakeEvent).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays));

                                event.startTime = event.startTime.clone().minimizeTime().addDays(changeDays);
                                event.endTime = event.endTime.clone().maximizeTime().addDays(changeDays);

                                //console.log(event.startTime.toString("i"), event.endTime.toString("i"));

                                agendaview.refreshOnDateChange();
                            } else {
                                var time = ($fly(agendaview._doms.eventTableWrap).prop("scrollTop") + parseInt($fly(fakeEvent).css("top"), 10) - fakeOffset.top) / gridUnit;
                                var day = Math.round((parseInt($fly(fakeEvent).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays));

                                event.startTime = event.startTime.clone().addDays(day).minimizeTime().addMilliseconds(time * 1800000);
                                event.endTime = event.startTime.clone().addMilliseconds(1800000);

                                event.allDay = false;

                                agendaview.refreshOnDateChange();
                            }
                        }

                        $fly(dom).removeClass("dragging-event");
                    }
                }
            });

            $fly(topHandle).draggable({
                addClasses: false,
                axis: "fake",
                start: function(evt) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).prop("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;

                    $fly(dom).draggable("option", "disabled", true);
                },
                drag: function(event) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        vertChange = event.pageY - inst.originalPageY + ($fly(agendaview._doms.eventTableWrap).prop("scrollTop") - startScrollTop);

                    vertChange = grid(vertChange);

                    var targetHeight = startHeight - vertChange;
                    if (targetHeight < minHeight) {
                        vertChange = startHeight - (startHeight % minHeight + minHeight);
                    }

                    $fly(dom).css("top", startTop + vertChange).outerHeight(startHeight - vertChange);
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeUnit = (parseInt($fly(dom).css("top"), 10) - startTop) / gridUnit;
                        event.startTime = new XDate(event.startTime.getTime() + changeUnit * 1800000);
                        agendaview.refreshEvents();
                    }

                    $fly(dom).draggable("option", "disabled", false);
                }
            }).mousedown(function(event) {
                event.stopPropagation();
            });

            $fly(bottomHandle).draggable({
                addClasses: false,
                axis: "fake",
                start: function(evt) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).prop("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;

                    $fly(dom).draggable("option", "disabled", true);
                    $log("bottomHandle dragstart");
                },
                drag: function(event) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        vertChange = event.pageY - inst.originalPageY + ($fly(agendaview._doms.eventTableWrap).prop("scrollTop") - startScrollTop);

                    vertChange = grid(vertChange);

                    var targetHeight = startHeight + vertChange;
                    if (targetHeight < minHeight) {
                        vertChange = (startHeight % minHeight + minHeight) - startHeight;
                    }

                    $fly(dom).outerHeight(startHeight + vertChange);

                    $fly(dom).draggable("enable");
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeUnit = ($fly(dom).outerHeight() - startHeight) / gridUnit;
                        event.endTime = new XDate(event.endTime.getTime() + changeUnit * 1800000);
                        agendaview.refreshEvents();
                    }
                    $fly(dom).draggable("option", "disabled", false);
                }
            }).mousedown(function(event) {
                event.stopPropagation();
            });

            return dom;
        }
    });

    dorado.widget.AgendaView = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "d-agenda-view"
            },
            date: {
                setter: function(value) {
                    if (this._forceDateDay !== undefined && value) {
                        var day = value.getDay();
                        if (day != this._forceDateDay) {
                            this._date = new XDate(value.getFullYear(), value.getMonth(), value.getDate() - (day - this._forceDateDay));
                            if (this._rendered && this._visible) {
                                this.refreshOnDateChange();
                            }
                            return;
                        }
                    }

                    this._date = value;

                    if (this._rendered && this._visible) {
                        this.refreshOnDateChange();
                    }
                }
            },
            showDays: {
                defaultValue: 1
            },
            forceDateDay: {},
            axisMode: {
                defaultValue: "ical"
            },
            events: {}
        },
        doOnResize: function() {
            var view = this, doms = view._doms, height = view.getRealHeight(), width = view.getRealWidth();

            var columnRowHeight = $fly(doms.columnRow).outerHeight(true),
                alldayEventTableHeight = $fly(doms.alldayEventTable).outerHeight(true);

            $fly(doms.contentHeightCell).height(height - columnRowHeight);
            $fly(doms.eventTableWrap).height(height - columnRowHeight - alldayEventTableHeight);

            $fly(doms.bottomLine).width(width - 100);

            view.refreshEvents();
            view.refreshAllDayEvents();
        },
        createDom: function() {
            var view = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                content: [{
                    tagName: "table",
                    className: "agenda-columns",
                    content: [{
                        tagName: "thead",
                        content: [{
                            tagName: "tr",
                            contextKey: "columnRow",
                            content: [{
                                tagName: "th",
                                className: "time-axis-cell"
                            }]
                        }]
                    }, {
                        tagName: "tbody",
                        content: {
                            tagName: "tr",
                            contextKey: "contentRow",
                            content: [{
                                tagName: "td",
                                style: {
                                    width: "100px"
                                }
                            }, {
                                tagName: "td",
                                contextKey: "contentHeightCell"
                            }]
                        }
                    }]
                }, {
                   tagName: "div",
                   className: "events-wrap",
                   content: [
                       {
                           tagName: "table",
                           className: "allday-event-table",
                           contextKey: "alldayEventTable",
                           content: {
                               tagName: "tbody",
                               content: {
                                   tagName: "tr",
                                   contextKey: "alldayEventTableRow",
                                   content: [{
                                       tagName: "td",
                                       className: "time-axis-cell",
                                       content: "allday"
                                   }, {
                                       tagName: "td",
                                       className: "content-cell",
                                       contextKey: "alldayTableContentCell",
                                       content: {
                                           tagName: "div",
                                           className: "allday-events-holder",
                                           contextKey: "alldayEventsHolder",
                                           style: {
                                               height: "30px"
                                           }
                                       }
                                   }]
                               }
                           }
                       }, {
                           tagName: "div",
                           className: "event-table-wrap",
                           contextKey: "eventTableWrap",
                           content: {
                               tagName: "div",
                               className: "event-table-wrap-inner",
                               content: [{
                                   tagName: "table",
                                   className: "event-table",
                                   contextKey: "eventTable",
                                   content: {
                                       tagName: "tbody",
                                       contextKey: "eventTableBody"
                                   }
                               }, {
                                   tagName: "div",
                                   className: "events-holder",
                                   contextKey: "eventsHolder"
                               }]
                           }
                       }, {
                           tagName: "div",
                           className: "bottom-line",
                           contextKey: "bottomLine"
                       }
                   ]
                }]
            }, null, doms);

            view._doms = doms;

            var eventTableBody = doms.eventTableBody;

            var axisMode = dorado.Browser.msie && dorado.Browser.version == 6 ? "gcal" : view._axisMode;

            var headTR = document.createElement("tr"), headTD1 = document.createElement("td"), headTD2 = document.createElement("td");
            headTD1.className = "axis-cell";
            headTD2.className = "odd-slot-cell first-row-slot-cell";
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                headTD1.innerHTML = "&nbsp;";
                headTD2.innerHTML = "&nbsp;";
            }
            if (axisMode == "gcal") {
                headTD1.innerHTML = "0:00";
            }
            headTR.appendChild(headTD1);
            headTR.appendChild(headTD2);
            eventTableBody.appendChild(headTR);

            for (var i = 0; i < 23; i++) {
                var evenTR = document.createElement("tr"), oddTR = document.createElement("tr");
                var axisCell = document.createElement("td"), evenCell = document.createElement("td"), oddCell = document.createElement("td");
                axisCell.className = "axis-cell";
                if (axisMode != "gcal") {
                    axisCell.rowSpan = 2;
                    axisCell.innerHTML = (i + 1) + ":00";
                }

                evenCell.className = "even-slot-cell";
                oddCell.className = "odd-slot-cell";

                if (dorado.Browser.msie && dorado.Browser.version == 6) {
                    evenCell.innerHTML = "&nbsp;";
                    oddCell.innerHTML = "&nbsp;";
                }

                evenTR.appendChild(axisCell);
                evenTR.appendChild(evenCell);
                if (axisMode == "gcal") {
                    var oddAxisCell = document.createElement("td");
                    oddAxisCell.className = "axis-cell";
                    oddAxisCell.innerHTML = (i + 1) + ":00";
                    oddTR.appendChild(oddAxisCell);
                }
                oddTR.appendChild(oddCell);

                eventTableBody.appendChild(evenTR);
                eventTableBody.appendChild(oddTR);
            }

            var bottomTR = document.createElement("tr"), bottomTD1 = document.createElement("td"), bottomTD2 = document.createElement("td");
            bottomTD1.className = "axis-cell";
            bottomTD2.className = "even-slot-cell";
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                bottomTD1.innerHTML = "&nbsp;";
                bottomTD2.innerHTML = "&nbsp;";
            }
            bottomTR.appendChild(bottomTD1);
            bottomTR.appendChild(bottomTD2);

            eventTableBody.appendChild(bottomTR);

            var makeDropable = function(td) {
                if (dorado.Browser.msie && dorado.Browser.version == 6) {
                    td.innerHTML = "&nbsp;";
                }
                $fly(td).droppable({
                    hoverClass: 'event-drag-over',
                    scope: "calendar",
                    greedy: false,
                    addClasses: false,
                    tolerance: "pointer",
                    drop: function() {
                    },
                    over: function() {
                        view._dragoverColumn = td.cellIndex - 1;
                    },
                    out: function() {
                    }
                });
            };

            var columnRow = doms.columnRow, contentRow = doms.contentRow, showDays = view._showDays;
            for (var j = 0; j < showDays; j++) {
                var th = document.createElement("th"), td;
                th.className = "header-cell";
                columnRow.appendChild(th);

                if (j != 0) {
                    td = document.createElement("td");
                    contentRow.appendChild(td);

                    makeDropable(td);
                }
            }

            makeDropable(doms.contentHeightCell);

            $fly(doms.alldayTableContentCell).droppable({
                scope: "calendar",
                greedy: false,
                tolerance: "touch",
                drop: function() {
                },
                over: function() {
                    view._dragoverAllDayTable = true;
                },
                out: function() {
                    view._dragoverAllDayTable = false;
                }
            });

            return dom;
        },
        refreshDom: function() {
            $invokeSuper.call(this, arguments);
            if (!this._initScrollBarGutter) {
                var view = this, doms = view._doms, eventTableWrap = doms.eventTableWrap,
                    columnRow = doms.columnRow, alldayEventTableRow = doms.alldayEventTableRow;

                var barWidth = eventTableWrap.offsetWidth - eventTableWrap.clientWidth;

                var gutter1 = document.createElement("th"), gutter2 = document.createElement("td");
                gutter1.className = "agenda-gutter";
                gutter1.style.width = barWidth + "px";
                gutter2.className = "agenda-gutter";
                gutter2.style.width = barWidth + "px";

                alldayEventTableRow.appendChild(gutter2);
                columnRow.appendChild(gutter1);

                view._initScrollBarGutter = true;
            }
        },
        filterEvents: function(events, start, end) {
            if (!end) end = start;
            var array = events || [], result = [];
            for (var i = 0, j = array.length; i < j; i++) {
                var event = array[i];
                if (!(event.startTime >= end || event.endTime <= start)) {
                    result.push(event);
                }
            }
            return result;
        },
        refreshEvents: function() {
            var view = this, dateRange = view.getDateRange(),
                eventSource = view._parent._parent._eventSource, events, i, j, k;

            if (eventSource) {
                events = eventSource.getEventsByRange(dateRange[0], dateRange[1], "normal");
            } else {
                return;
            }

            if (view._currentAgendaEvents) {
                var cacheEvents = view._currentAgendaEvents;
                for (i = 0; i < cacheEvents.length; i++) {
                    var eventDom = cacheEvents[i];
                    $fly(eventDom).css({ left: "", top: "" });
                    dorado.widget.AgendaEventPool.returnObject(eventDom);
                }
                view._currentAgendaEvents = [];
            }
            var dayCount = dateRange[0].diffDays(dateRange[1]);

            for (i = 0; i < dayCount; i++) {
                var date = dateRange[0].clone().addDays(i), dayEvents = view.filterEvents(events, date, date.clone().addDays(1));

                var result = view.sortEventsOfSlot(dayEvents);

                for (j = 0, k = result.length; j < k; j++) {
                    var item = result[j];
                    if (item instanceof Array) {
                        view.refreshBlock(item, i);
                    } else {
                        view.refreshEvent(item, i);
                    }
                }
            }
        },
        sortEventsOfSlot: function(events) {
            var allEvents = events.concat().sort(function(a, b) {
                return a.startTime > b.startTime;
            });

            var result = [], block = [];

            function positionBlock(block) {
                var columns = [];
                function getColumn(index) {
                    if (!columns[index]) {
                        columns[index] = [];
                    }
                    return columns[index];
                }

                var i, k, j, l;

                for (i = 0, k = block.length; i < k; i++) {
                    var event = block[i], columnIndex = 0;
                    while (true) {
                        var columnInfo = getColumn(columnIndex), okay = true, lastEventInColumn = columnInfo[columnInfo.length - 1];
                        if (lastEventInColumn && lastEventInColumn.endTime > event.startTime)
                            okay = false;
                        if (okay) {
                            columnInfo.push(event);
                            break;
                        }
                        columnIndex++;
                    }
                }

                function fixEvent(event, index) {
                    event.colSpan = 1;
                    for (var i = index + 1, k = columns.length; i < k; i++) {
                        var okay = true, columnEvents = columns[i];
                        for (var j = 0, l = columnEvents.length; j < l; j++) {
                            var columnEvent = columnEvents[j];
                            if (!(columnEvent.startTime >= event.endTime || columnEvent.endTime <= event.startTime)) {
                                okay = false;
                                break;
                            }
                        }
                        if (okay) {
                            event.colSpan++;
                        } else {
                            break;
                        }
                    }
                }

                for (i = 0, k = columns.length; i < k; i++) {
                    var events = columns[i];
                    for (j = 0, l = events.length; j < l; j++) {
                        fixEvent(events[j], i);
                    }
                }

                return columns;
            }

            var blockMaxDate;

            for (var i = 0, j = allEvents.length; i < j; i++) {
                var event = allEvents[i], nextEvent = allEvents[i + 1];
                if (blockMaxDate == null) blockMaxDate = event.endTime;
                if (nextEvent && blockMaxDate > nextEvent.startTime) {
                    block.push(event);
                    if (event.endTime > blockMaxDate) {
                        blockMaxDate = event.endTime;
                    }
                } else if (block.length > 0) {
                    block.push(event);
                    result.push(positionBlock(block));
                    blockMaxDate = null;
                    block = [];
                } else {
                    result.push(event);
                }
            }

            return result;
        },
        refreshBlock: function(block, slotIndex) {
            var view = this, columntCount = block.length;
            for (var i = 0, k = block.length; i < k; i++) {
                var column = block[i];
                for (var j = 0, l = column.length; j < l; j++) {
                    var event = column[j];
                    view.refreshBlockEvent(event, i, columntCount, slotIndex);
                }
            }
        },
        refreshBlockEvent: function(event, columnIndex, columnCount, slotIndex) {
            var view = this, doms = view._doms, eventTable = doms.eventTable,
                eventTableWidth = $fly(eventTable).width() - 100, eventTableHeight = $fly(eventTable).height(),
                widthPerColumn = eventTableWidth / view._showDays / columnCount, heightPerMin = eventTableHeight / 24 / 60;

            function refreshEventPerSlot(event, startDate, endDate) {
                var dom = view.getEventDom(event);
                $fly(dom).css({
                    left: view._showDays == 1 ? widthPerColumn * columnIndex : eventTableWidth / view._showDays * (startDate.getDay()) + widthPerColumn * columnIndex,
                    width: widthPerColumn * event.colSpan,
                    top: (startDate.getHours() * 60 + startDate.getMinutes()) * heightPerMin
                }).outerHeight(Math.floor((endDate.getTime() - startDate.getTime()) / 1000 / 60 * heightPerMin));
            }

            var startDate = event.startTime, endDate = event.endTime, dateRange = view.getDateRange(),
                minDate = dateRange[0].clone().addDays(slotIndex), maxDate = minDate.clone().addDays(1);

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            refreshEventPerSlot(event, startDate, endDate);
        },
        refreshEvent: function(event, slotIndex) {
            var view = this, doms = view._doms, eventTable = doms.eventTable,
                eventTableWidth = $fly(eventTable).width() - 100, eventTableHeight = $fly(eventTable).height(),
                widthPerColumn = eventTableWidth / view._showDays - 10, heightPerMin = eventTableHeight / 24 / 60;

            function refreshEventPerSlot(event, startDate, endDate) {
                var dom = view.getEventDom(event);
                $fly(dom).css({
                    left: view._showDays == 1 ? 0 : (eventTableWidth - 0) / view._showDays * (startDate.getDay()),
                    top: (startDate.getHours() * 60 + startDate.getMinutes()) * heightPerMin,
                    width: widthPerColumn
                }).outerHeight(Math.floor((endDate.getTime() - startDate.getTime()) / 1000 / 60 * heightPerMin));
            }

            var startDate = event.startTime, endDate = event.endTime,
                dateRange = view.getDateRange(), minDate = dateRange[0].clone().addDays(slotIndex), maxDate = minDate.clone().addDays(1);

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            refreshEventPerSlot(event, startDate, endDate);
        },
        getEventDom: function(event) {
            var view = this, dom = dorado.widget.AgendaEventPool.borrowObject();
            $fly(dom).removeClass("allday-event").find(".title").html(event.title);
            jQuery.data(dom, HOLD_EVENT_KEY, event);
            jQuery.data(dom, HOLD_AGENDAVIEW_KEY, this);

            if (view._currentAgendaEvents == null) view._currentAgendaEvents = [];
            view._currentAgendaEvents.push(dom);
            view._doms.eventsHolder.appendChild(dom);

            return dom;
        },
        refreshAllDayEvents: function() {
            var view = this, dateRange = view.getDateRange(), eventSource = view._parent._parent._eventSource, events;

            if (eventSource) {
                events = eventSource.getEventsByRange(dateRange[0], dateRange[1], "allday")
            } else {
                return;
            }

            if (view._currentAgendaAllDayEvents) {
                var cacheEvents = view._currentAgendaAllDayEvents;
                for (var i = 0; i < cacheEvents.length; i++) {
                    var eventDom = cacheEvents[i];
                    if (eventDom.parentNode) {
                        eventDom.parentNode.removeChild(eventDom);
                    }
                    dorado.widget.AgendaEventPool.returnObject(eventDom);
                }
                view._currentAgendaAllDayEvents = [];
            }

            var table = view.sortAllDayEvents(events);

            view.changeAlldayEventsHolderHeight(table.length * 20 + 10);

            for (var j = 0; j < events.length; j++) {
                var event = events[j], dom = view.getAllDayEventDom(event);
                view.refreshAllDayEvent(dom, event);
            }
        },
        changeAlldayEventsHolderHeight: function(height) {
            var view = this, doms = view._doms, totalHeight = view.getRealHeight();
            $fly(doms.alldayEventsHolder).height(height);

            var columnRowHeight = $fly(doms.columnRow).outerHeight(true),
                alldayEventTableHeight = $fly(doms.alldayEventTable).outerHeight(true);

            $fly(doms.contentHeightCell).height(totalHeight - columnRowHeight);
            $fly(doms.eventTableWrap).height(totalHeight - columnRowHeight - alldayEventTableHeight);
        },
        sortAllDayEvents: function(events) {
            var view = this, resultTable = [];
            function getRow(row) {
                var rowArray = resultTable[row];
                if (!rowArray) {
                    rowArray = new Array(view._showDays);
                    resultTable[row] = rowArray;
                }
                return rowArray;
            }
            function positionEvent(event) {
                var startTime = event.startTime, endTime = event.endTime,
                    count = Math.ceil(intervalToDay(endTime - startTime)),
                    column = Math.ceil(intervalToDay(startTime - view._date)),
                    rowIndex = 0;

                while (true) {
                    var row = getRow(rowIndex), okay = true;
                    for (var i = 0; i < count; i++) {
                        if (row[column + i] != null) {
                            okay = false;
                        }
                    }
                    if (okay) {
                        for (var j = 0; j < count; j++) {
                            if (j == 0)
                                row[column + j] = event;
                            else
                                row[column + j] = true;
                        }
                        event.row = rowIndex;
                        return;
                    }
                    rowIndex++;
                }
            }

            for (var i = 0, j = events.length; i < j; i++) {
                var event = events[i];
                positionEvent(event);
            }

            return resultTable;
        },
        refreshAllDayEvent: function(dom, event) {
            var view = this, eventTableWidth = $fly(view._doms.eventTable).width() - 100,
                dateRange = view.getDateRange(), minDate = dateRange[0], maxDate = dateRange[1],
                startDate = event.startTime.clone().minimizeTime(), endDate = event.endTime.clone().maximizeTime();

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            $fly(dom).css({
                left: view._showDays == 1 ? 0 : eventTableWidth / view._showDays * startDate.getDay(),
                top: 20 * event.row,
                width: eventTableWidth / view._showDays * intervalToDay(endDate - startDate) - 10,
                height: 20
            });
        },
        getDateRange: function() {
            var date = this._date || new XDate(), startDate = date.clone().minimizeTime(),
                endDate = startDate.clone().addDays(this._showDays).minimizeTime();

            return [startDate, endDate];
        },
        getAllDayEventDom: function(event) {
            var view = this, dom = dorado.widget.AgendaEventPool.borrowObject();
            $fly(dom).addClass("allday-event").find(".title").html(event.title);
            jQuery.data(dom, HOLD_EVENT_KEY, event);
            jQuery.data(dom, HOLD_AGENDAVIEW_KEY, this);

            if (view._currentAgendaAllDayEvents == null) view._currentAgendaAllDayEvents = [];
            view._currentAgendaAllDayEvents.push(dom);
            view._doms.alldayEventsHolder.appendChild(dom);

            return dom;
        },
        refreshOnDateChange: function() {
            var view = this, date = view._date || new XDate(), doms = view._doms, columnRow = doms.columnRow;

            for (var i = 1; i <= view._showDays; i++) {
                var cell = columnRow.cells[i], newDate = new XDate(date.getFullYear(), date.getMonth(), date.getDate() + i - 1);
                cell.innerHTML = (newDate.getMonth() + 1) + "/" + newDate.getDate();
            }

            view.refreshEvents();
            view.refreshAllDayEvents();
        }
    });
})();
