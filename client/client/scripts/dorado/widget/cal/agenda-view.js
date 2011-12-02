(function() {
    var HOLD_EVENT_KEY = "cal_event";

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

            var startHeight, startTop, startLeft, startScrollTop, gridUnit, minHeight;

            var grid = function(value) {
                return Math.floor(value / gridUnit) * gridUnit;
            };

            $fly(dom).draggable({
                addClasses: false,
                scope: "calendar",
                axis: "fake",
                helper: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY),
                        width = Math.floor(($fly(agendaview._doms.eventTable).width() - 100) / agendaview._showDays),
                        currentLeft = parseInt($fly(dom).css("left"), 10);

                    $fly(dom).css({ width: width - 10, left: currentLeft - currentLeft % width }).bringToFront();

                    return dom;
                },
                start: function(ui, ev) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startLeft = parseInt($fly(dom).css("left"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).attr("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;
                },
                drag: function(ui, ev) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        vertChange = event.pageY - inst.originalPageY + ($fly(agendaview._doms.eventTableWrap).attr("scrollTop") - startScrollTop);

                    vertChange = grid(vertChange);

                    $fly(dom).css("top", startTop + vertChange);

                    //console.log("agendaview._dragoverColumn:" + agendaview._dragoverColumn);

                    if (agendaview._dragoverColumn != null) {
                        $fly(dom).css("left", agendaview._dragoverColumn * (agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays);
                    }
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeVUnit = (parseInt($fly(dom).css("top"), 10) - startTop) / gridUnit;
                        var changeHUnit = (parseInt($fly(dom).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays);

                        var changeInterval = changeVUnit * 1800000 + changeHUnit * INTERVAL_A_DAY;

                        //console.log("changeHUnit:" + changeHUnit + "\tchangeVUnit:" + changeVUnit +
                        //"\tchangeInterval:" + changeInterval + "\tstartLeft:" + startLeft + "\tendLeft:" + parseInt($fly(dom).css("left"), 10));

                        event.startTime = new Date(event.startTime.getTime() + changeInterval);
                        event.endTime = new Date(event.endTime.getTime() + changeInterval);

                        //console.log(event.title + ":\t" + event.startTime.toString() + "\t" + event.endTime.toString());

                        agendaview.refreshEvents();
                    }
                }
            });

            $fly(topHandle).draggable({
                addClasses: false,
                axis: "fake",
                start: function(ui, ev) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).attr("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;
                },
                drag: function(ui, ev) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        vertChange = event.pageY - inst.originalPageY + ($fly(agendaview._doms.eventTableWrap).attr("scrollTop") - startScrollTop);

                    vertChange = grid(vertChange);

                    var targetHeight = startHeight - vertChange;
                    if (targetHeight < minHeight) {
                        vertChange = startHeight - (startHeight % minHeight + minHeight);
                    }

                    $fly(dom).css("top", startTop + vertChange);
                    $fly(dom).outerHeight(startHeight - vertChange);
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeUnit = (parseInt($fly(dom).css("top"), 10) - startTop) / gridUnit;
                        event.startTime = new Date(event.startTime.getTime() + changeUnit * 1800000);
                        agendaview.refreshEvents();
                    }
                }
            });

            $fly(bottomHandle).draggable({
                addClasses: false,
                axis: "fake",
                start: function(ui, ev) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).attr("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;
                },
                drag: function(ui, ev) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable"),
                        vertChange = event.pageY - inst.originalPageY + ($fly(agendaview._doms.eventTableWrap).attr("scrollTop") - startScrollTop);

                    vertChange = grid(vertChange);

                    var targetHeight = startHeight + vertChange;
                    if (targetHeight < minHeight) {
                        vertChange = (startHeight % minHeight + minHeight) - startHeight;
                    }

                    $fly(dom).outerHeight(startHeight + vertChange);
                },
                stop: function(ui, ev) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeUnit = ($fly(dom).outerHeight() - startHeight) / gridUnit;
                        event.endTime = new Date(event.endTime.getTime() + changeUnit * 1800000);
                        agendaview.refreshEvents();
                    }
                }
            });

            return dom;
        }
    });

    dorado.widget.AgendaAllDayEventPool = new dorado.util.ObjectPool({
        makeObject: function() {
            var dom = document.createElement("div");
            dom.className = "event";

            dom.style.position = "absolute";

            var startHeight, startTop, startLeft, startScrollTop, gridUnit, minHeight;

            var grid = function(value) {
                return Math.floor(value / gridUnit) * gridUnit;
            };

            $fly(dom).draggable({
                addClasses: false,
                scope: "calendar",
                axis: "fake",
                helper: function() {
                    $fly(dom).bringToFront();

                    return dom;
                },
                start: function(ui, ev) {
                    var event = jQuery.data(dom, HOLD_EVENT_KEY), agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY);
                    if (agendaview && event) {
                        agendaview._draggingevent = event;
                    }
                    startHeight = $fly(dom).outerHeight(true);
                    startTop = parseInt($fly(dom).css("top"), 10);
                    startLeft = parseInt($fly(dom).css("left"), 10);
                    startScrollTop = $fly(agendaview._doms.eventTableWrap).attr("scrollTop");
                    gridUnit = Math.floor($fly(agendaview._doms.eventTable).outerHeight() / 48);
                    minHeight = gridUnit;
                },
                drag: function(ui, ev) {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), inst = jQuery.data(this, "draggable");

                    //console.log("agendaview._dragoverColumn:" + agendaview._dragoverColumn);

                    if (agendaview._dragoverColumn != null) {
                        $fly(dom).css("left", agendaview._dragoverColumn * (agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays);
                    }
                },
                stop: function() {
                    var agendaview = jQuery.data(dom, HOLD_AGENDAVIEW_KEY), event = jQuery.data(dom, HOLD_EVENT_KEY);
                    if (agendaview) {
                        var changeHUnit = Math.ceil(parseInt($fly(dom).css("left"), 10) - startLeft) / ((agendaview._doms.eventTable.offsetWidth - 100) / agendaview._showDays);
                        var changeInterval = changeHUnit * INTERVAL_A_DAY;

                        event.startTime = minimizeDate(new Date(event.startTime.getTime() + changeInterval));
                        event.endTime = minimizeDate(new Date(event.endTime.getTime() + changeInterval));

                        //console.log(event.title + ":\t" + event.startTime.toString() + "\t" + event.endTime.toString());

                        agendaview.refreshAllDayEvents();
                    }
                }
            });

            return dom;
        }
    });

    var HOLD_AGENDAVIEW_KEY = "cal_agendaview";

    dorado.widget.AgendaView = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "agenda-view"
            },
            date: {
                setter: function(value) {
                    if (this._forceDateDay !== undefined && value) {
                        var day = value.getDay();
                        if (day != this._forceDateDay) {
                            this._date = new Date(value.getFullYear(), value.getMonth(), value.getDate() - (day - this._forceDateDay));
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
                    tagName: "div",
                    className: "wrap",
                    content: {
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
                        }]
                    }
                }, {
                    tagName: "table",
                    className: "allday-event-table",
                    contextKey: "alldayEventTable",
                    content: {
                        tagName: "tr",
                        content: [{
                            tagName: "td",
                            className: "time-axis-cell",
                            content: "allday"
                        }, {
                            tagName: "td",
                            className: "content-cell",
                            content: {
                                tagName: "div",
                                className: "allday-events-wrap",
                                contextKey: "alldayEventsHolder",
                                style: {
                                    height: "30px"
                                }
                            }
                        }]
                    }
                }, {
                    tagName: "div",
                    className: "event-table-wrap",
                    contextKey: "eventTableWrap",
                    content: [{
                        tagName: "table",
                        className: "event-table",
                        contextKey: "eventTable"
                    }, {
                        tagName: "div",
                        className: "events-holder",
                        contextKey: "eventsHolder"
                    }]
                }, {
                    tagName: "div",
                    className: "bottom-line",
                    contextKey: "bottomLine"
                }]
            }, null, doms);

            view._doms = doms;

            var eventTable = doms.eventTable;

            var headTR = document.createElement("tr"), headTD1 = document.createElement("td"), headTD2 = document.createElement("td");
            headTD1.className = "axis-cell";
            headTD2.className = "odd-slot-cell first-row-slot-cell";
            headTR.appendChild(headTD1);
            headTR.appendChild(headTD2);
            eventTable.appendChild(headTR);

            for (var i = 0; i < 23; i++) {
                var evenTR = document.createElement("tr"), oddTR = document.createElement("tr");
                var axisCell = document.createElement("td"), evenCell = document.createElement("td"), oddCell = document.createElement("td");
                axisCell.className = "axis-cell";
                axisCell.innerHTML = (i + 1) + ":00";
                axisCell.rowSpan = 2;

                evenCell.className = "even-slot-cell";
                oddCell.className = "odd-slot-cell";

                evenTR.appendChild(axisCell);
                evenTR.appendChild(evenCell);
                oddTR.appendChild(oddCell);

                eventTable.appendChild(evenTR);
                eventTable.appendChild(oddTR);
            }

            var bottomTR = document.createElement("tr"), bottomTD1 = document.createElement("td"), bottomTD2 = document.createElement("td");
            bottomTD1.className = "axis-cell";
            bottomTD2.className = "even-slot-cell";
            bottomTR.appendChild(bottomTD1);
            bottomTR.appendChild(bottomTD2);

            eventTable.appendChild(bottomTR);

            var makeDropable = function(td) {
                $fly(td).droppable({
                    hoverClass: 'event-drag-over',
                    scope: "calendar",
                    greedy: false,
                    tolerance: "pointer",
                    drop: function(ev, ui) {
                    },
                    over: function(ev, ui) {
                        view._dragoverColumn = td.cellIndex - 1;
                    },
                    out: function(ev, ui) {
                    }
                });
            };

            var columnRow = doms.columnRow, contentRow = doms.contentRow, showDays = view._showDays;
            for (var i = 0; i < showDays; i++) {
                var th = document.createElement("th"), td;
                th.className = "header-cell";
                columnRow.appendChild(th);

                if (i != 0) {
                    td = document.createElement("td");
                    contentRow.appendChild(td);

                    makeDropable(td);
                }
            }

            makeDropable(doms.contentHeightCell);

            return dom;
        },
        refreshEvents: function() {
            var dateRange = this.getDateRange(), eventSource = this._parent._parent._eventSource,
                events;

            if (eventSource) {
                events = eventSource.getEventsByRange(dateRange[0], dateRange[1], "normal");
            } else {
                return;
            }

//            console.log("===events start===");
//            events.forEach(function(event) {
//                console.log(event.toString());
//            });
//            console.log("===events end===");

            var view = this;

            if (view._currentAgendaEvents) {
                var cacheEvents = view._currentAgendaEvents;
                for (var i = 0; i < cacheEvents.length; i++) {
                    var edom = cacheEvents[i];
                    $fly(edom).css({ left: "", top: "" });
                    dorado.widget.AgendaEventPool.returnObject(edom);
                }
                view._currentAgendaEvents = [];
            }

            var result = view.sortEvents(events);

            for (var i = 0; i < result.length; i++) {
                var item = result[i];
                if (item instanceof Array) {
                    view.refreshBlock(item);
                } else {
                    view.refreshEvent(item);
                }
            }
        },
        sortEvents: function(events) {
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
                for (var i = 0, k = block.length; i < k; i++) {
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

                return columns;
            }

            for (var i = 0, j = allEvents.length; i < j; i++) {
                var event = allEvents[i], nextEvent = allEvents[i + 1];
                if (nextEvent && event.endTime > nextEvent.startTime) {
                    block.push(event);
                } else if (block.length > 0) {
                    block.push(event);
                    result.push(positionBlock(block));
                    block = [];
                } else {
                    result.push(event);
                }
            }

            return result;
        },
        refreshBlock: function(block) {
            var view = this, columntCount = block.length;
            for (var i = 0, k = block.length; i < k; i++) {
                var column = block[i];
                for (var j = 0, l = column.length; j < l; j++) {
                    var event = column[j];
                    view.refreshBlockEvent(event, i, columntCount);
                }
            }
        },
        refreshBlockEvent: function(event, columnIndex, columnCount) {
            var startDate = event.startTime, endDate = event.endTime;
            var view = this, doms = view._doms, eventTable = doms.eventTable, eventTableHeight = $fly(eventTable).height();
            var heightPerMin = eventTableHeight / 24 / 60, eventTableWidth = $fly(eventTable).width() - 100,
                widthPerColumn = eventTableWidth / view._showDays / columnCount;


            function refreshEventPerSlot(event, startDate, endDate) {
                var dom = view.getEventDom(event);
                //console.log("event title:" + event.title + "\tstartDate:" + startDate.toString() + "\tendDate:" + endDate.toString());
                $fly(dom).css({
                    left: view._showDays == 1 ? widthPerColumn * columnIndex : eventTableWidth / view._showDays * (startDate.getDay()) + widthPerColumn * columnIndex,
                    width: widthPerColumn,
                    top: (startDate.getHours() * 60 + startDate.getMinutes()) * heightPerMin
                }).outerHeight(Math.floor((endDate.getTime() - startDate.getTime()) / 1000 / 60 * heightPerMin));
            }

            var dateRange = view.getDateRange(), minDate = dateRange[0], maxDate = dateRange[1];

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            if (startDate.getMonth() === endDate.getMonth() && startDate.getDate() === endDate.getDate()
                && startDate.getFullYear() === endDate.getFullYear()) {
                refreshEventPerSlot(event, startDate, endDate);
            } else {
                var count = Math.round((maxmizeDateIfPosibble(endDate) - minimizeDate(startDate)) / INTERVAL_A_DAY);
                for (var i = 0; i < count; i++) {
                    if (i == 0) {
                        refreshEventPerSlot(event, startDate, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(), 23, 59, 59));
                    } else if (i == count - 1) {
                        refreshEventPerSlot(event, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 0, 0, 0), endDate);
                    } else {
                        refreshEventPerSlot(event, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 0, 0, 0),
                            new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 23, 59, 59));
                    }
                }
            }
        },
        refreshEvent: function(event) {
            var view = this, doms = view._doms, eventTable = doms.eventTable, eventTableHeight = $fly(eventTable).height();
            var heightPerMin = eventTableHeight / 24 / 60, eventTableWidth = $fly(eventTable).width() - 100,
                widthPerColumn = eventTableWidth / view._showDays - 10;

            var startDate = event.startTime, endDate = event.endTime;

            function refreshEventPerSlot(event, startDate, endDate) {
                var dom = view.getEventDom(event);
                $fly(dom).css({
                    left: view._showDays == 1 ? 0 : (eventTableWidth - 0) / view._showDays * (startDate.getDay()),
                    top: (startDate.getHours() * 60 + startDate.getMinutes()) * heightPerMin,
                    width: widthPerColumn
                }).outerHeight(Math.floor((endDate.getTime() - startDate.getTime()) / 1000 / 60 * heightPerMin));
            }

            var dateRange = view.getDateRange(), minDate = dateRange[0], maxDate = dateRange[1];

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            if (startDate.getMonth() === endDate.getMonth() && startDate.getDate() === endDate.getDate()
                && startDate.getFullYear() === endDate.getFullYear()) {
                refreshEventPerSlot(event, startDate, endDate);
            } else {
                var count = Math.round((maxmizeDateIfPosibble(endDate) - minimizeDate(startDate)) / INTERVAL_A_DAY);
                for (var i = 0; i < count; i++) {
                    if (i == 0) {
                        refreshEventPerSlot(event, startDate, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(), 23, 59, 59));
                    } else if (i == count - 1) {
                        refreshEventPerSlot(event, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 0, 0, 0), endDate);
                    } else {
                        refreshEventPerSlot(event, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 0, 0, 0),
                            new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i, 23, 59, 59));
                    }
                }
            }
        },
        getEventDom: function(event) {
            var dom = dorado.widget.AgendaEventPool.borrowObject();
            $fly(dom).find(".title").html(event.title);

            jQuery.data(dom, HOLD_EVENT_KEY, event);
            jQuery.data(dom, HOLD_AGENDAVIEW_KEY, this);

            if (this._currentAgendaEvents == null) this._currentAgendaEvents = [];
            this._currentAgendaEvents.push(dom);
            this._doms.eventsHolder.appendChild(dom);

            return dom;
        },
        refreshAllDayEvents: function() {
            var dateRange = this.getDateRange(), eventSource = this._parent._parent._eventSource,
                events;

            if (eventSource) {
                events = eventSource.getEventsByRange(dateRange[0], dateRange[1], "allday")
            } else {
                return;
            }

            var view = this;

            if (view._currentAgendaAllDayEvents) {
                var cacheEvents = view._currentAgendaAllDayEvents;
                for (var i = 0; i < cacheEvents.length; i++) {
                    var edom = cacheEvents[i];
                    if (edom.parentNode) {
                        edom.parentNode.removeChild(edom);
                    }
                    dorado.widget.AgendaAllDayEventPool.returnObject(edom);
                }
                view._currentAgendaAllDayEvents = [];
            }

            var table = view.sortAllDayEvents(events);

            view.changeAlldayEventsHolderHeight(table.length * 20 + 10);

            for (var i = 0; i < events.length; i++) {
                var event = events[i], dom = view.getAllDayEventDom(event);
                view.refreshAllDayEvent(dom, event);
            }
        },
        changeAlldayEventsHolderHeight: function(height) {
            var view = this, doms = view._doms, allheight = view.getRealHeight();
            $fly(doms.alldayEventsHolder).height(height);
            var columnRowHeight = $fly(doms.columnRow).outerHeight(true),
                alldayEventTableHeight = $fly(doms.alldayEventTable).outerHeight(true);

            $fly(doms.contentHeightCell).height(allheight - columnRowHeight);
            $fly(doms.eventTableWrap).height(allheight - columnRowHeight - alldayEventTableHeight);
        },
        sortAllDayEvents: function(events) {
            var view = this, resultTable = [];
            function getRow(row) {
                var rowArray = resultTable[row];
                if (!rowArray) {
                    rowArray = new Array(this._showDays);
                    resultTable[row] = rowArray;
                }
                return rowArray;
            }
            function positionEvent(event) {
                var startTime = event.startTime, endTime = event.endTime,
                    count = Math.ceil(intervalToDay(endTime - startTime)),
                    column = Math.ceil(intervalToDay(startTime - view._date));

                var rowIndex = 0;
                while (true) {
                    var row = getRow(rowIndex), okay = true;
                    for (var i = 0; i < count; i++) {
                        if (row[column + i] != null) {
                            okay = false;
                        }
                    }
                    if (okay) {
                        for (var i = 0; i < count; i++) {
                            if (i == 0)
                                row[column + i] = event;
                            else
                                row[column + i] = true;
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
            var view = this, doms = view._doms, eventTable = doms.eventTable;
            var eventTableWidth = $fly(view._dom).width() - 100;

            var width, dateRange = view.getDateRange();

            var minDate = dateRange[0], maxDate = dateRange[1],
                startDate = minimizeDate(event.startTime),
                endDate = maxmizeDateIfPosibble(event.endTime);

            if (startDate < minDate) {
                startDate = minDate;
            }

            if (endDate > maxDate) {
                endDate = maxDate;
            }

            width = eventTableWidth / view._showDays * intervalToDay(endDate - startDate) - 10;

            $fly(dom).css({
                left: view._showDays == 1 ? 0 : eventTableWidth / view._showDays * startDate.getDay(),
                top: 20 * event.row,
                width: width,
                height: 20
            });
        },
        getDateRange: function() {
            var date = this._date || new Date(), startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0),
                endDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + this._showDays - 1, 23, 59, 59);

            //console.log("dateRange:[" + startDate.toString() + "," + endDate.toString() + "]");

            return [startDate, endDate];
        },
        getAllDayEventDom: function(event) {
            var dom = dorado.widget.AgendaAllDayEventPool.borrowObject();
            $fly(dom).html(event.title);

            jQuery.data(dom, HOLD_EVENT_KEY, event);
            jQuery.data(dom, HOLD_AGENDAVIEW_KEY, this);

            if (this._currentAgendaAllDayEvents == null) this._currentAgendaAllDayEvents = [];
            this._currentAgendaAllDayEvents.push(dom);
            this._doms.alldayEventsHolder.appendChild(dom);

            return dom;
        },
        refreshOnDateChange: function() {
            var view = this, date = view._date || new Date(), doms = view._doms, columnRow = doms.columnRow;

            for (var i = 1; i <= view._showDays; i++) {
                var cell = columnRow.cells[i];
                var newDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() + i - 1);
                cell.innerHTML = (newDate.getMonth() + 1) + "/" + newDate.getDate();
            }

            view.refreshEvents();
            view.refreshAllDayEvents();
        }
    });
})();