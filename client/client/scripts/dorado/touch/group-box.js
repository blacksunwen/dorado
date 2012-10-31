/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
dorado.touch.GroupBox = $extend(dorado.widget.Container, {
    ATTRIBUTES: {
        className: {
            defaultValue: "d-group-box"
        },
        caption: {}
    },
    createDom: function() {
        var groupBox = this, doms = {}, dom = $DomUtils.xCreate({
            tagName: "div",
            className: groupBox._className,
            content: [
                {
                    tagName: "div",
                    className: "group-box-bar",
                    contextKey: "captionContainer",
                    content: [
                        {
                            tagName: "div",
                            className: "bar-caption",
                            content: groupBox._caption,
                            contextKey: "caption"
                        }
                    ]
                },
                {
                    tagName: "div",
                    className: "body",
                    contextKey: "body",
                    content: {
                        contextKey: "contentPanel",
                        tagName: "div",
                        className: "content-panel"
                    }
                }
            ]
        }, null, doms);

        groupBox._doms = doms;

        return dom;
    },
    refreshDom: function(dom) {
        $invokeSuper.call(this, arguments);
        var groupbox = this;
        $fly(groupbox._doms.caption).html(groupbox._caption);
    },
    doOnResize: function(collapsed) {
        var groupbox = this, dom = groupbox._dom, doms = groupbox._doms, height = groupbox.getRealHeight();
        if (typeof height == "number" && height > 0) {
            if (collapsed == undefined) {
                collapsed = groupbox._collapsed;
            }
            if (groupbox._collapseable && collapsed) {
                $fly(dom).height("auto");
            } else {
                var buttonPanelHeight = 0, captionCtHeight = 0;
                if (doms.buttonPanel) {
                    buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                }
                if (doms.captionContainer) {
                    captionCtHeight = $fly(doms.captionContainer).outerHeight(true);
                }
                $fly(doms.contentPanel).outerHeight(height - captionCtHeight - buttonPanelHeight);
                $fly(dom).height("auto");
            }
        }

        $invokeSuper.call(this, arguments);
    },
    getContentContainer: function() {
        return this._doms.contentPanel;
    }
});
