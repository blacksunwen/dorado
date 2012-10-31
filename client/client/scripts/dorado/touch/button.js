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
dorado.touch.Button = $extend(dorado.widget.Control, {
    $className: "dorado.touch.Button",
    _inherentClassName: "button",
    focusable: true,
    ATTRIBUTES: {
        className: {
            defaultValue: "button-plain"
        },

        caption: {},

        disabled: {},

        ui: {},

        icon: {},

        iconClass: {},

        //top bottom left right
        iconAlign: {
            defaultValue: "left"
        },

        badgeCls: {},

        badgeText: {}
    },

    EVENTS: {
        onTap: {}
    },

    onTap: function() {
        var button = this;
        //TODO 可能有更好的方法解决这个问题
        document.activeElement.blur();
        button.fireEvent("onTap", button);
    },

    createDom: function() {
        var button = this, doms = {}, dom = $DomUtils.xCreate({
            tagName: "button",
            className: this._className,
            content: {
                tagName: "span",
                className: "label",
                contextKey: "label",
                content: this._caption
            }
        }, null, doms);

        button._doms = doms;
        $fly(dom).bind("click", function(e) {
            button.onTap();
	        return false;
        });

        $fly(dom).addClass("button-iconalign-" + button._iconAlign).addClass(button._ui || "");
        jQuery(dom).addClassOnClick(button._className + "-touched");

        button.doCreateIcon(dom);
        button.doCreateBadge(dom);

        return dom;
    },

    refreshDom: function(dom) {
        $invokeSuper.call(this, arguments);
        var button = this, doms = button._doms, icon = button._icon, iconClass = button._iconClass;

        if (icon || iconClass) {
            if (!doms.icon) {
                button.doCreateIcon();
            } else {
                $fly(doms.icon).css("display", "");
            }
        } else if (doms.icon) {
            $fly(doms.icon).css("display", "none");
        }

        doms.label.innerHTML = button._caption || "";

        if (icon)
            doms.icon.src = icon;
        else if (doms.icon)
            doms.icon.src = "data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

        if (iconClass) {
            $fly(doms.icon).addClass(iconClass);
        }

        if (button._badgeText) {
            $fly(dom).addClass("button-hasbadge");
            if (!doms.badge) {
                button.doCreateBadge();
            } else {
                $fly(doms.badge).css("display", "");
            }
        } else if (doms.badge) {
            $fly(dom).removeClass("button-hasbadge");
            $fly(doms.badge).css("display", "none");
        }
    },

    doCreateBadge: function(dom) {
        var button = this, doms = button._doms, badgeText = button._badgeText;
        if (badgeText == null) {
            return;
        }
        dom = dom ? dom : button._dom;
        var badgeEl = document.createElement("span");
        badgeEl.className = "badge";
        badgeEl.innerHTML = badgeText;
        doms.badge = badgeEl;
        dom.appendChild(badgeEl);
    },

    doCreateIcon: function(dom) {
        var button = this, doms = button._doms, icon = button._icon, iconClass = button._iconClass;
        if (icon == null && iconClass == null) {
            return;
        }
        dom = dom ? dom : button._dom;
        var iconEl = document.createElement("img");
        iconEl.className = "icon";
        doms.icon = iconEl;
        dom.appendChild(iconEl);
    }
});
