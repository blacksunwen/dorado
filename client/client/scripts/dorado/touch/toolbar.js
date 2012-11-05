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

dorado.touch.ToolBar = $extend(dorado.widget.Control, {
    $className: "dorado.touch.ToolBar",
    ATTRIBUTES: {
        className: {
            defaultValue: "toolbar"
        },
        caption: {},
        items: {
            setter: function(value) {
                var toolbar = this, items = toolbar._items, rendered = toolbar._rendered, i, l, item;
                if (items) {
                    toolbar.clearItems();
                }
                if (!items) {
                    toolbar._items = items = new dorado.util.KeyedArray(function(value) {
                        return value._id;
                    });
                }
                if (value) {
                    if (value.constructor == Array.prototype.constructor) {
                        for (i = 0,l = value.length; i < l; i++) {
                            item = toolbar.createItem(value[i]);
                            items.insert(item);
                        }
                        if (rendered) {
                            toolbar.doRenderItems();
                        }
                    } else if (value instanceof dorado.util.KeyedArray) {
                        for (i = 0,l = value.size; i < l; i++) {
                            item = toolbar.createItem(value.get(i));
                            items.append(item);
                        }
                        if (rendered) {
                            toolbar.doRenderItems();
                        }
                    }
                }
            }
        }
    },

    createDom: function() {
        var toolbar = this, doms = {}, dom = $DomUtils.xCreate({
            tagName: "div",
            className: toolbar._className,
            content: [{
                tagName: "div",
                className: "caption",
                contextKey: "caption"
            }, {
                tagName: "div",
                contextKey: "wrap",
                className: "toolbar-wrap"
            }]
        }, null, doms);

        toolbar._doms = doms;
        toolbar.doRenderItems(dom);

        return dom;
    },

    onResize: function() {
        $invokeSuper.call(this, arguments);
        this.refresh();
    },

    refreshDom: function() {
        $invokeSuper.call(this, arguments);
        var toolbar = this, doms = toolbar._doms;
        doms.caption.innerHTML = toolbar._caption || "";
        toolbar.applyLayout();
    },

    applyLayout: function() {
        var layout = this, ctDom = this.getContentContainer(), items = this.get("items") || [];
        var containerSize = { width: $fly(ctDom).width(), height: $fly(ctDom).height() };

        //TODO 为了touch的性能优化删掉的，我记不清为啥当初要加上这段代码了。
        $fly(ctDom).addClass("box-layout").css({
            "-webkit-box-orient": "horizontal",
            "-webkit-box-direction": "normal",
            "-webkit-box-pack": "center",
            "-webkit-box-align": "start"
        });

        var flexItems = [], flexItemValues = [], flexSum = 0, noneFlexSum = 0;

        items.each(function(item) {
            var layoutConstraint = item._layoutConstraint, dom = item._dom;

            if (layoutConstraint) {
                //handle flex
                var flex = layoutConstraint.flex;
                if (flex != undefined) {
                    flexItems.push(item);
                    flexItemValues.push(flex);
                    flexSum += flex;

                    return;
                }
            }
            noneFlexSum += $fly(dom).outerWidth(true);
        });

        var flexSize = containerSize.width - noneFlexSum;

        jQuery.each(flexItems, function(index, flexItem) {
            var flex = flexItemValues[index];
            $fly(flexItem._dom).width(flex / flexSum * flexSize);
        });
    },

    getContentContainer: function() {
        return this._doms.wrap;
    },

    createItem: function(config) {
        if (!config) return null;
        if (typeof config == "string" || config.constructor == Object.prototype.constructor) {
            var result = dorado.touch.create(config);
            result._parent = result._focusParent = this;
            return result;
        } else {
            config._parent = config._focusParent = this;
            return config;
        }
    },

    addItem: function(item, index) {
        var toolbar = this, items = toolbar._items, doms = toolbar._doms;
        if (!item) return null;
        if (!items) {
            items = toolbar._items = new dorado.util.KeyedArray(function(value) {
                return value._id;
            });
        }
        item = toolbar.createItem(item);
        if (toolbar._rendered) {
            var refDom = null;
            if (typeof index == "number") {
                var refItem = items.get(index);
                refDom = refItem._dom;
            }
            items.insert(item, index);
            item.render(doms.wrap);
            $fly(item._dom).addClass("box-layout-item");
            toolbar.registerInnerControl(item);
        } else {
            items.insert(item, index);
        }

        return item;
    },

    removeItem: function(item) {
        var toolbar = this, items = toolbar._items;
        if (items && item !== undefined) {
            var realItem = item;
            if (typeof item == "number") {
                realItem = items.get(item);
                items.removeAt(item);
            } else {
                items.remove(item);
            }
            realItem.destroy();
            toolbar.unregisterInnerControl(realItem);
        }
    },

    /**
     * 清除工具栏中所有的子组件。
     */
    clearItems: function() {
        var toolbar = this, items = toolbar._items;
        for (var i = 0, j = items.size; i < j; i++) {
            var item = items.get(i);
            toolbar.unregisterInnerControl(item);
            item.destroy();
        }
        items.clear();
    },

    doRenderItems: function(dom) {
        var toolbar = this, items = toolbar._items || {}, doms = toolbar._doms;
        dom = dom ? dom : toolbar._dom;
        for (var i = 0, j = items.size; i < j; i++) {
            var item = items.get(i);
            item.render(doms.wrap);
            $fly(item._dom).addClass("box-layout-item");
            toolbar.registerInnerControl(item);
        }
    }
});

dorado.touch.toolbar = {};
dorado.touch.Spacer = $extend(dorado.widget.Control, {
    ATTRIBUTES: {
        layoutConstraint: {
            defaultValue: function() {
                return { flex: 1 };
            }
        }
    }
});
