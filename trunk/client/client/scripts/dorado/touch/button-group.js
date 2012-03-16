dorado.touch.AbstractListModel = $extend(dorado.AttributeSupport, {
    constructor: function(options) {
        if (options) this.set(options);
    },
    addObserver: function(observer) {
        var model = this, observers = model.observers;
        if (!observers) {
            model.observers = observers = [];
        }
        if (observer) {
            observers.push(observer);
        }
    },
    removeObserver: function(observer) {
        var model = this, observers = model.observers;
        if (observers) {
            if (observer != undefined) {
                observers[typeof observer == "number" ? "removeAt" : "remove"](observer);
            }
        }
    },
    getSize: function() {},
    getElementAt: function() {},
    fireItemChanged: function(start, end) {
        var model = this, observers = model.observers;
        if (observers) {
            for (var i = 0, j = observers.length; i < j; i++) {
                var observer = observers[i];
                observer && observer.onItemChanged && observer.onItemChanged(start, end);
            }
        }
    },
    fireItemAdded: function(start, end) {
        var model = this, observers = model.observers;
        if (observers) {
            for (var i = 0, j = observers.length; i < j; i++) {
                var observer = observers[i];
                observer && observer.onItemAdded && observer.onItemAdded(start, end);
            }
        }
    },
    fireItemRemoved: function(start, end) {
        var model = this, observers = model.observers;
        if (observers) {
            for (var i = 0, j = observers.length; i < j; i++) {
                var observer = observers[i];
                observer && observer.onItemRemoved && observer.onItemRemoved(start, end);
            }
        }
    }
});

dorado.touch.DefaultListModel = $extend(dorado.touch.AbstractListModel, {
    ATTRIBUTES: {
        series: {}
    },
    getSize: function() {
        var model = this, elements = model._series;
        if (elements) {
            return elements.length;
        }
        return 0;
    },
    getElementAt: function(index) {
        var model = this, elements = model._series;
        if (elements) {
            return elements[index];
        }
        return null;
    },
    insertElement: function(element, index) {
        var model = this, elements = model._series;
        if (!elements) {
            model._series = elements = [];
        }
        if (element) {
            if (index != undefined) {
                elements.insert(element, index);
                model.fireItemAdded(index);
            } else {
                elements.push(element);
                model.fireItemAdded(elements.length - 1);
            }
        }
    },
    addElement: function(element) {
        this.insertElement(element);
    },
    setElementAt: function(index, element) {
        var model = this, elements = model._series;
        if (!elements) {
            model._series = elements = [];
        }
        if (element) {
            elements[index] = element;
            model.fireItemChanged(index);
        }
    },
    removeElement: function(element) {
        var model = this, elements = model._series, index = element;
        if (elements && element != undefined) {
            if (typeof element != "number") {
                index = elements.indexOf(element);
            }
            if (index > -1) {
                elements.removeAt(index);
                model.fireItemRemoved(index);
            }
        }
    },
    removeAllElements: function() {
        var model = this, elements = model._series || [], length = elements.length;
        elements = null;
        model.fireItemRemoved(0, length - 1);
    },
    indexOfElement: function(element) {
        var model = this, elements = model._series;
        if (elements && element) {
            return elements.indexOf(element);
        }
    }
});

dorado.touch.AbstractButtonGroup = $extend(dorado.widget.Control, {
    currentable: false,
    ATTRIBUTES: {
        buttons: {
            setter: function(value) {
                if (value instanceof Array) {
                    this._dataModel = new dorado.touch.DefaultListModel({ series: value });
                    this._dataModel.addObserver(this);
                }
            }
        },
        currentButton: {
            setter: function(value) {
                var group = this, buttons = group._buttons, button = value;
                if (typeof value == "number" || typeof value == "string") {
                    button = buttons.get(button);
                }
                if (button && !button._disabled) {
                    group.doChangeCurrentButton(button);
                }
            }
        },
        dataModel: {}
    },
    EVENTS: {
        onCurrentButtonChange: {}
    },
    removeItemDom: function(index) {
        var group = this, itemDom = group.itemDoms[index];
        if (itemDom) {
            group._dom.removeChild(itemDom);
            group.itemDoms.splice(index, 1);
        }
    },
    createDom: function() {
        var group = this, dom = document.createElement("span"), dataModel = group._dataModel;
        group.itemDoms = [];
        if (dataModel) {
            var size = dataModel.getSize();
            for (var i = 0; i < size; i++) {
                var element = dataModel.getElementAt(i);
                group.registerInnerControl(element);
                element.render(dom);
                group.itemDoms.push(element._dom);
            }
        }

        if (group.currentable) {
            if (!group._currentButton) {
                group.doChangeCurrentButton(group._dataModel.getElementAt(0));
            }
        }

        return dom;
    },
    doGetCurrentButtonClass: function() {
    },
    doChangeCurrentButton: function(button) {
        var group = this, currentButton = group._currentButton;

        if(button == currentButton) return false;

        var currentButtonCls = group.doGetCurrentButtonClass();

        if (currentButton) {
            $fly(currentButton._dom).removeClass(currentButtonCls);
        }
        if (button) {
            group._currentButton = button;
            $fly(button._dom).addClass(currentButtonCls);
        } else {
            group._currentButton = null;
        }

        group.fireEvent("onCurrentButtonChange", group, {});

        return true;
    },
    onItemChanged: function(start, end) {
        var group = this, dataModel = group._dataModel;
        if (!group._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        for (var i = start; i <= end; i++) {
            var oldItemDom = group.itemDoms[i], element = dataModel.getElementAt(i);
            if (element.rendered) {
                group.itemDoms[i] = element._dom;
            } else {
                element.render(group._dom, oldItemDom);
                group.itemDoms[i] = element._dom;
            }
            if (oldItemDom) {
                group._dom.removeChild(oldItemDom);
            }
        }
    },
    onItemAdded: function(start, end) {
        var group = this, dataModel = group._dataModel, itemDoms = group.itemDoms;
        if (!group._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        var dom = group._dom, beforeNode = itemDoms[start];
        for (var i = start; i <= end; i++) {
            var element = dataModel.getElementAt(i);
            element.render(dom, beforeNode);
            if (beforeNode) {
                group.itemDoms.splice(i, 0, element._dom);
            } else {
                group.itemDoms.push(element._dom);
            }
            beforeNode = element._dom;
        }
    },
    onItemRemoved: function(start, end) {
        var group = this;
        if (!group._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        for (var i = start; i <= end; i++) {
            group.removeItemDom(i);
        }
    }
});

dorado.touch.ButtonGroup = $extend(dorado.touch.AbstractButtonGroup, {
    ATTRIBUTES: {
        className: {
            defaultValue: "button-group"
        }
    },

    refreshDom: function() {
        $invokeSuper.call(this, arguments);
        var itemDoms = this.itemDoms;
        if (itemDoms.length > 0) {
            $fly(itemDoms[0]).addClass("button-first");
            $fly(itemDoms[itemDoms.length - 1]).addClass("button-last");
        }
    }
});

dorado.touch.RadioButton = $extend(dorado.touch.Button, {
    ATTRIBUTES: {
        value: {}
    },
    onTap: function() {
        var button = this, group = button._parent, disabled = button._disabled || group._disabled;
        if (group) {
            if (!disabled) {
                group.doChangeCurrentButton(button);
            }
        }
        $invokeSuper.call(this, arguments);
    }
});

dorado.touch.RadioButtonGroup = $extend(dorado.touch.AbstractButtonGroup, {
    currentable: true,
    ATTRIBUTES: {
        className: {
            defaultValue: "button-group"
        },
        disabled: {},
        readOnly: {
            path: "_disabled"
        },
        value: {
            getter: function() {
                var currentButton = this._currentButton;
                if (currentButton) {
                    return currentButton._value;
                }
                return null;
            }
        }
    },

    doGetCurrentButtonClass: function() {
        return "button-current";
    },

    refreshDom: function(dom) {
        $invokeSuper.call(this, arguments);
        var itemDoms = this.itemDoms;
        if (itemDoms.length > 0) {
            $fly(itemDoms[0]).addClass("button-first");
            $fly(itemDoms[itemDoms.length - 1]).addClass("button-last");
        }
        $fly(dom)[this._disabled ? "addClass" : "removeClass"](this._className + "-disabled");
    }
});