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
            if (collapsed) {
                $fly(dom).height("auto");
            } else {
                var buttonPanelHeight = 0, captionCtHeight = 0;
                if (doms.buttonPanel) {
                    buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                }
                if (doms.captionContainer) {
                    captionCtHeight = $fly(doms.captionContainer).outerHeight(true);
                }
                var hheight = $fly(dom).height();
                $fly(doms.contentPanel).outerHeight(height - captionCtHeight - buttonPanelHeight);
            }
        }
    },
    getContentContainer: function() {
        return this._doms.contentPanel;
    }
});