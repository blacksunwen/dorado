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

dorado.touch.List = $extend(dorado.widget.Control, {
    ATTRIBUTES: {
        data: {
            setter: function(value) {
                if (value instanceof Array) {
                    this._dataModel = new dorado.touch.DefaultListModel({series: value});
                    this._dataModel.addObserver(this);
                }
            }
        },
        dataModel: {},
        className: {
            defaultValue: "d-list"
        },
        grouped: {},
        offsetTop: {}
    },
    EVENTS: {
        onItemTap: {}
    },
    createGroupDom: function(group) {
        if (group) {
            var groupDom = document.createElement("div");
            groupDom.className = "group-title";
            groupDom.innerHTML = group;
            return groupDom;
        }
    },
    createItemDom: function(item, index) {
        var list = this, itemDom, itemText = item, leaf = true;
        if (item) {
            if (item instanceof Object) {
                itemText = item.text;
                if (item.items) {
                    leaf = false;
                }
            }

            itemDom = document.createElement("div");
            itemDom.className = "list-item";
            var labelDom = document.createElement("span");
            labelDom.className = "label";
            labelDom.innerHTML = itemText;
            var holderIcon = document.createElement("span");
            holderIcon.className = "holdericon";

            itemDom.appendChild(labelDom);
            itemDom.appendChild(holderIcon);

            if (!leaf) {
                $fly(itemDom).addClass("list-item-holder");
            }

            $fly(itemDom).bind("click", function() {
                list.fireEvent("onItemTap", list, {
                    item: item,
                    itemText: itemText
                });
                var oldCurrent = list.currentItem;
                if (oldCurrent) {
                    $fly(oldCurrent).removeClass("list-item-current");
                }
                $fly(itemDom).addClass("list-item-current");
                list.currentItem = itemDom;
            });

            list._itemDoms.splice(index, 0, itemDom);
        }
        return itemDom;
    },
    removeItemDom: function(index) {
        var list = this, itemDom = list._itemDoms[index];
        if (itemDom) {
            list._dom.removeChild(itemDom);
            list._itemDoms.splice(index, 1);
        }
    },
    createDom: function() {
        var list = this, doms = {}, dom = $DomUtils.xCreate({
            tagName: "div",
            content: [{
                tagName: "div",
                className: "list-items-wrap",
                contextKey: "itemsWrap"
            }]
        }, null, doms), dataModel = list._dataModel;

        list._doms = doms;

        list._itemDoms = [];
        var size, i, j, element;

        if (!list._grouped && dataModel) {
            size = dataModel.getSize();
            for (i = 0; i < size; i++) {
                var element = dataModel.getElementAt(i), itemDom = this.createItemDom(element, i);
                if (itemDom) {
                    doms.itemsWrap.appendChild(itemDom);
                }
            }
        } else if (list._grouped && dataModel) {
            size = dataModel.getSize();
            for (i = 0; i < size; i++) {
                var element = dataModel.getElementAt(i), group = element._group, groupData  = element._data || [];
                var groupDom = this.createGroupDom(group);
                if (groupDom) {
                    doms.itemsWrap.appendChild(groupDom);
                }
                for (j = 0; j < groupData.length; j++) {
                    var item = groupData[j];
                    itemDom = this.createItemDom(item, i * size + j);
                    if (itemDom) {
                        doms.itemsWrap.appendChild(itemDom);
                    }
                }
            }
        }

        return dom;
    },
    doOnAttachToDocument: function() {
        var list = this, dom = list._dom;
        setTimeout(function() {
            var scroller = new iScroll(dom, {
                fadeScrollbar: true
            });
            list._scroller = scroller;
        }, 0);
    },
    onItemChanged: function(start, end) {
        var list = this, dataModel = list._dataModel;
        if (!list._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        for (var i = start; i <= end; i++) {
            var itemDom = list._itemDoms[i], element = dataModel.getElementAt(i);
            $fly(itemDom).find(".label")[0].innerHTML = element;
        }
        this._scroller && this._scroller.refresh();
    },
    onItemAdded: function(start, end) {
        var list = this, dataModel = list._dataModel, itemDoms = list._itemDoms;
        if (!list._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        var dom = list._dom, beforeNode = itemDoms[start];
        for (var i = start; i <= end; i++) {
            var itemDom = list.createItemDom(dataModel.getElementAt(i), i);
            if (beforeNode) {
                dom.insertBefore(itemDom, beforeNode);
                beforeNode = itemDom;
            } else {
                dom.appendChild(itemDom);
                beforeNode = itemDom;
            }
        }
        this._scroller && this._scroller.refresh();
    },
    onItemRemoved: function(start, end) {
        var list = this;
        if (!list._dom) {
            return;
        }
        if (end == undefined) {
            end = start;
        }
        for (var i = start; i <= end; i++) {
            list.removeItemDom(i);
        }
        this._scroller && this._scroller.refresh();
    }
});
