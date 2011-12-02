(function(){
    var INTERVAL_A_DAY = 86400000;

    function intervalToDay(interval) {
        return interval / INTERVAL_A_DAY;
    }

    //TODO delete
    Date.prototype.toString = function() {
        return this.getFullYear() + "-" + (this.getMonth() + 1) + "-" + this.getDate() + " " + this.getHours() + ":" + this.getMinutes();
    };

    Array.prototype.select = function(fn) {
        var result = [];
        if (fn) {
            for (var i = 0, j = this.length; i < j; i++) {
                var el = this[i];
                if (fn(el, i)) {
                    result.push(el);
                }
            }
        }
        return result;
    };

    dorado.widget.Calendar = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "d-calendar"
            },
            date: {
                defaultValue: function() {
                    return new Date()
                },
                setter: function(value) {
                    this._date = value;
                    this.doOnDateChange();
                }
            },
            eventSource: {
                defaultValue: function() {
                    return new dorado.widget.EventSource()
                }
            }
        },
        doOnResize: function() {
            var cal = this, height = cal.getRealHeight(), doms = cal._doms;
            cal._cardbook.set("height", height - $fly(doms.navbar).outerHeight(true));
            cal._cardbook.resetDimension();
        },
        createDom: function() {
            var cal = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                content: [{
                    tagName: "div",
                    className: "navbar",
                    contextKey: "navbar",
                    content: [{
                        tagName: "span",
                        content: "",
                        contextKey: "currentDateCell"
                    }]
                }, {
                    tagName: "div",
                    className: "content-body",
                    contextKey: "contentBody"
                }]
            }, null, doms);

            cal._doms = doms;

            var cardbook = new dorado.widget.CardBook({
                controls: [
                    new dorado.widget.AgendaView(),
                    new dorado.widget.AgendaView({
                        showDays: 7,
                        forceDateDay: 0
                    }),
                    new dorado.widget.MonthView()
                ],
                onCurrentChange: function(self, arg) {
                    var newControl = arg.newControl;
                    newControl.set("date", cal._date);
                    //newControl.refreshOnDateChange();
                }
            });

            cal.registerInnerControl(cardbook);
            cardbook.render(doms.contentBody);

            var currentDateLabel = new dorado.widget.toolbar.Label({});

            var toolbar = new dorado.widget.ToolBar({
                items: [{
                    caption: "Prev",
                    onClick: function() {
                        cal.prev();
                    }
                }, {
                    caption: "Next",
                    onClick: function() {
                        cal.next();
                    }
                }, {
                    caption: "Today",
                    onClick: function() {
                        cal.set("date", new Date())
                    }
                }, currentDateLabel, "->", {
                    caption: "Day",
                    onClick: function() {
                        cardbook.set("currentControl", 0);
                    }
                }, {
                    caption: "Week",
                    onClick: function() {
                        cardbook.set("currentControl", 1);
                    }
                }, {
                    caption: "Month",
                    onClick: function() {
                        cardbook.set("currentControl", 2);
                    }
                }]
            });

            cal.registerInnerControl(toolbar);
            toolbar.render(doms.navbar);

            cal._cardbook = cardbook;
            cal._currentDateLabel = currentDateLabel;

            return dom;
        },
        prev: function() {
            var cal = this, cardbook = cal._cardbook, index = cardbook.getCurrentControlIndex(), date = cal._date;
            if (index == 0) {
                cal._date = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 1);
            } else if (index == 1) {
                cal._date = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 7);
            } else if (index == 2) {
                cal._date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
            }
            cal.doOnDateChange();
        },
        next: function() {
            var cal = this, cardbook = cal._cardbook, index = cardbook.getCurrentControlIndex(), date = cal._date;
            if (index == 0) {
                cal._date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1);
            } else if (index == 1) {
                cal._date = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 7);
            } else if (index == 2) {
                cal._date = new Date(date.getFullYear(), date.getMonth() + 1, date.getDate());
            }
            cal.doOnDateChange();
        },
        doOnDateChange: function() {
            var cal = this, date = cal._date, cardbook = cal._cardbook, currentControl = cardbook.get("currentControl");
            currentControl.set("date", date);
            cal._currentDateLabel.set("text", date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate());
        }
    });

    dorado.widget.EventSource = $class({
        constructor : function(options) {
            for (var prop in options) {
                this[prop] = options[prop];
            }
        },
        getEventsByRange: function(start, end, type) {
            if (!end) end = start;

            return [];
        }
    });

    dorado.widget.ArrayEventSource = $extend(dorado.widget.EventSource, {
        getEventsByRange: function(start, end, type) {
            if (!end) end = start;
            type = type || "all";
            function filter(event) {
                var allDay = event.allDay /*|| (event.endTime - event.startTime) > INTERVAL_A_DAY*/;
                return type == "all" || (type == "normal" && !allDay) || (type == "allday" && allDay);
            }
            var array = this.array || [], result = [];
            for (var i = 0, j = array.length; i < j; i++) {
                var event = array[i];
                if (event.title.indexOf("11-30") != -1) {
                }
                if (!(event.startTime >= end || event.endTime <= start)) {
                    if (filter(event)) {
                        result.push(event);
                    }
                }
            }
            return result;
        }
    });

    dorado.widget.CalEvent = function(options) {
        for (var prop in options) {
            this[prop] = options[prop];
        }
    };


    dorado.widget.CalEvent.prototype.toString = function() {
        return this.title + ":[" + this.startTime.toString() + "," + this.endTime.toString() + "]"
    }

})();
