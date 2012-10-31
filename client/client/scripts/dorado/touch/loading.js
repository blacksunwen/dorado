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
dorado.touch.LoadingPanel = $extend(dorado.RenderableElement, { /** @scope dorado.touch.LoadingPanel.prototype */
    $className: "dorado.touch.LoadingPanel",

    ATTRIBUTES: {
        className: {
            defaultValue: "d-loading-panel"
        }
    },

    createDom: function() {
        var panel = this, dom, doms = {};
        dom = $DomUtils.xCreate({
            tagName: "div",
            className: panel._className,
            content: [{
                tagName: "div",
                className: "panel-header-left",
                contextKey: "header",
                content: {
                    tagName: "div",
                    className: "panel-header-right"
                }
            }, {
                tagName: "div",
                className: "panel-body-left",
                contextKey: "body",
                content: {
                    tagName: "div",
                    className: "panel-body-right",
                    content: [{
                        tagName: "div",
                        className: "panel-body",
                        contextKey: "body",
                        content: {
                            tagName: "div",
                            className: "content-panel",
                            contextKey: "contentPanel",
                            content: [{
                                tagName: "div",
                                className: "loading-image"
                            }, {
                                tagName: "div",
                                className: "loading-caption",
                                contextKey: "loadingCaption"
                            }]
                        }
                    }]
                }
            }, {
                tagName: "div",
                className: "panel-footer-left",
                contextKey: "footer",
                content: {
                    tagName: "div",
                    className: "panel-footer-right"
                }
            }]
        }, null, doms);

        panel._doms = doms;

        return dom;
    },

    /**
     * 显示任务面板
     * @param {Object} options 注册任务组的时候的配置信息中的showOptions选项。
     */
    show: function(options) {
        var panel = this;
        options = options || {};
        options.align = "center";
        options.vAlign = "center";
        if (panel._hideTimer) {
            clearTimeout(panel._hideTimer);
            panel._hideTimer = null;
            return;
        }
        if (!panel._rendered) {
            panel.render(document.body);
        } else {
            $fly(panel._dom).css("display", "");
        }
        dorado.ModalManager.show(panel._dom, "d-modal-mask-transparent");
        $fly(panel._dom).bringToFront();
        $fly(panel._doms.loadingCaption).html(options.caption || "");
        $DomUtils.dockAround(panel._dom, document.body, options);
    },

    /**
     * 隐藏任务面板
     */
    hide: function() {
        var panel = this;
        if (panel._rendered) {
            $fly(panel._dom).css("display", "none");
            dorado.ModalManager.hide(panel._dom);
        }
    }
});

dorado.touch.LoadingPanel.getInstance = function() {
    if (!this._instance) {
        this._instance = new this();
    }

    return this._instance;
};

dorado.touch.LoadingPanel.setInstance = function(instance) {
    this._instance = instance;

    return this;
};

dorado.touch.showLoadingPanel = function(caption) {
    var loadingPanel = dorado.touch.LoadingPanel.getInstance();
    loadingPanel.show({
        caption: caption
    });
};

dorado.touch.hideLoadingPanel = function() {
    var loadingPanel = dorado.touch.LoadingPanel.getInstance();
    loadingPanel.hide();
};
