(function() {
    var queue = [];
    jQuery(function() {
        document.onclick = function() {
            //console.log("-------------!!!document click capture....." + queue.length);
            if (queue.length > 0) {
                queue.forEach(function(fn) {
                    setTimeout(function() {
                        fn && fn();
                    }, 400);
                });
                queue.splice(0, queue.length);
            }
        };
    });

    dorado.doOnBodyClick = function(fn) {
        //console.log("------------- push function");
        queue.push(fn);
    };
})();

dorado.touch.Picker = $extend(dorado.touch.FloatPanel, {
    $className: "dorado.touch.Picker",
    ATTRIBUTES: {
        className: {
            defaultValue: "picker"
        },
        floating: {
            defaultValue: true
        },
        animateType: {
            defaultValue: "none"
        },
        contentOverflow: {
            defaultValue: "hidden"
        }
    },
    EVENTS: {
        onPick: {}
    },
    sync: function(){},
    getModalDom: function() {
        var picker = this;
        if (!picker._modalDom) {
            var dom = document.createElement("div");
            var width = $fly(document).width(), height = $fly(document).height();
            $fly(dom).css({
                position: "absolute",
                left: 0,
                top: 0,
                width: width,
                height: height,
                opacity: 0.5,
                background: "transparent",
                zIndex: 1000,
                display: "none"
            });

            document.body.appendChild(dom);

            picker._modalDom = dom;
        }

        dorado.doOnBodyClick(function() {
            //console.log("=================show modalDom");
            picker._modalDom.style.display = "";
        });

        return picker._modalDom;
    },
    show: function() {
        $invokeSuper.call(this, arguments);

        this.getModalDom();
    },
    hide: function() {
        var picker = this;
        dorado.doOnBodyClick(function() {
            //console.log("=================hide modalDom");
            picker._modalDom.style.display = "none";
        });

        $invokeSuper.call(this, arguments);
    },
	getShowPosition: function(options){
		var picker = this;

		options = options || {};
		options.overflowHandler = function(options) {
			picker.handleOverflow(options.maxHeight);
		};

		return $invokeSuper.call(this, arguments);
	},
    createDom: function() {
        var dom = $invokeSuper.call(this, arguments);
        return dom;
    },
	onBlur: function() {
        console.log("Picker onBlur fired");
		if (this._visible) this.hide();
	}
});

dorado.touch.DatePicker = $extend(dorado.touch.Picker, {
    $className: "dorado.touch.DatePicker",
    ATTRIBUTES: {
        width: {
            defaultValue: 380
        },
        height: {
            defaultValue: 350
        },
        autoHeight: {
            defaultValue: true
        }
    },
    handleOverflow: function() {},
    sync: function(editor) {
        var picker = this, dropdown = picker._dropdown;
        if (dropdown) {
            var date = editor.get("value");
            if (date && date instanceof Date) dropdown.set("date", new Date(date.getTime()));
            if (dropdown._yearMonthPicker && dropdown._yearMonthPicker._opened) {
                dropdown.hideYMPicker(false);
            }
        }
    },
	createDom: function() {
		var picker = this, dropdown = new dorado.widget.DatePicker({
            onPick: function(pick, arg) {
                picker.fireEvent("onPick", arg);
                if (picker._focusParent) {
                    var date = arg.date ? new Date(arg.date.getTime()) : null;
                    picker._focusParent.set("value", date);
                }
                picker.hide();
            }
		});
		picker.addChild(dropdown);
		picker._dropdown = dropdown;

		return $invokeSuper.call(this, arguments);
	}
});

dorado.touch.defaultDatePicker = new dorado.touch.DatePicker();

dorado.touch.ListPicker = $extend(dorado.touch.Picker, {
    $className: "dorado.touch.ListPicker",
    ATTRIBUTES: {
        data: {},

        autoHeight: {
            defaultValue: true
        }
    },
    handleOverflow: function(overflowHeight) {
        var picker = this;
        picker._height = overflowHeight;
        picker.resetDimension();
        this._list._scroller && this._list._scroller.refresh();
    },
    createDom: function() {
        var picker = this, list = new dorado.touch.List({
            data: this._data,
            listener: {
                onItemTap: function(list, arg) {
                    picker.fireEvent("onPick", arg);
                    if (picker._focusParent) {
                        //TODO delete
                        picker._focusParent.set("text", arg.item);
                    }
                    picker.hide();
                }
            }
        });
        //picker._layout = new dorado.widget.layout.AnchorLayout();
	    picker.addChild(list);
	    picker._list = list;

        var dom = $invokeSuper.call(this, arguments), anchor = document.createElement("div");
        anchor.className = "anchor";
        //dom.appendChild(anchor);

        return dom;
    }
});