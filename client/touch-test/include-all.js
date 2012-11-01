/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

(function() {

	var skin = "default";

	var testLibsBefore = [
		"client/scripts/json/json2.js",

		"client/scripts/jquery/jquery-1.7.1.js",
		"client/scripts/jquery/jquery-template-1.0.0.js",
		"client/scripts/jquery/jquery.easing.1.3.js",
		"client/scripts/jquery/jquery.mousewheel.js",
		"client/scripts/jquery/aop.js",
		"client/scripts/jquery/jquery.hotkeys.js",
		"client/scripts/jquery/jquery-ui-1.8.17.custom.js",

        "client/scripts/touch/mustache.js",

		"client-test/lib/test-setting.js"
	];

	var doradoLibs = [
		"core/lang.js",
		"core/jquery-redefine.js",
		"core/core.js",
		"core/object.js",
		"core/setting.js",
		"core/exception.js",
		"core/attribute.js",
		"core/event.js",
		"core/tag-manager.js",
		"core/renderer.js",
		"core/renderable-element.js",
		"core/toolkits.js",

		"util/util.js",
		"util/date.js",
		"util/common.js",
		"util/resource.js",
		"util/iterator.js",
		"util/keyed-array.js",
		"util/keyed-list.js",
		"util/object-pool.js",
		"util/json.js",
		"util/ajax.js",
		"util/map.js",

		"data/data-pipe.js",
		"data/data-provider.js",
		"data/data-resolver.js",
		"data/data-type.js",
		"data/data-type-repository.js",
		"data/property-def.js",
		"data/entity.js",
		"data/entity-list.js",
		"data/validator.js",
		"data/data-path.js",
		"data/data-util.js",

		"dom/dom.js",
		"dom/shadow.js",
		"dom/modal-manager.js",
		"dom/jquery-dom.js",
		"dom/jquery-x-create.js",
        "dom/jquery-slide.js",
		"dom/jquery-dragdrop.js",
		"dom/jquery-gestures.js",
        "dom/modal-manager.js",
        "dom/task-indicator.js",

		"drag-drop/dragging-indicator.js",
		"drag-drop/drag-drop.js",

		"widget/component.js",
		"widget/control.js",
		"widget/float-control.js",

		"widget/layout/layout.js",
		"widget/layout/anchor-layout.js",
		"widget/layout/dock-layout.js",
		"widget/layout/box-layout.js",
		"widget/layout/form-layout.js",

		"widget/container.js",
		"widget/view.js",
		"widget/data-set.js",
		"widget/sub-view-holder.js",
		"widget/html-container.js",

		"widget/action/action.js",
        "widget/action/form-action.js",
		"widget/action/rpc-action.js",

        "widget/base/float-container.js",
        "widget/base/abstract-button.js",
        "widget/base/simple-button.js",
		"widget/base/button.js",
        "widget/base/iframe.js",
        "widget/base/card-book.js",
        "widget/base/tip.js",

		"widget/data-control/data-control.js",
		"widget/data-control/property-data-control.js",

		"widget/form/label.js",
		"widget/form/abstract-editor.js",
		"widget/form/trigger.js",
		"widget/form/dropdown.js",
		"widget/form/list-dropdown.js",
		"widget/form/dataset-dropdown.js",
		"widget/form/custom-dropdown.js",
		"widget/form/text-editor.js",
		"widget/form/text-area.js",
        "widget/form/check-box.js",
        "widget/form/radio-button.js",
		"widget/form/data-message.js",
        "widget/form/form-element.js",
        "widget/form/autoform.js",

        "widget/list/abstract-list.js",
        "widget/list/item-model.js",
        "widget/list/row-list.js",
        "widget/list/list-box.js",
        "widget/grid/column-model.js",
        "widget/grid/abstract-grid.js",
        "widget/grid/grid.js"
	];

    var touchLibs = [
        "touch/touch.js",
        "touch/loading.js",
        "touch/button.js",
        "touch/button-group.js",
        "touch/check-box.js",
        "touch/toolbar.js",
        "touch/panel.js",
        "touch/group-box.js",
        "touch/form-element.js",
        "touch/editor.js",
        "touch/dialog.js",
        "touch/date-picker.js",
        "touch/picker.js",
        "touch/iscroll.js",
        "touch/list.js",
        "touch/msgbox.js"
    ];
	
	var styleSheets = [
        "layout.css",
        "button.css",
        "button-group.css",
        "container.css",
        "editor.css",
        "list.css",
        "grid.css",
        "form-element.css",
        "picker.css",
        "date-picker.css",
        "icon.css",
        "d7-reset.css"
	];
	
	var cacheBuster = "";
	if ((location + "").substring(0, 4) == "http") {
		cacheBuster = "?cacheBuster=" + parseInt(new Date().getTime() / 10000);
	}

	var WORKSPACE_ROOT = "/client-test/workspace/";
	var CLIENT_ROOT = WORKSPACE_ROOT + "client/";

    WORKSPACE_ROOT = "/dorado7/";
    CLIENT_ROOT = "/dorado7/client/";

    writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/inherent/common.css");
    writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/inherent/widget.css");

    var d7is = [
        "common.css",
        "widget.css",
        "tab-bar.css",
        "iframe.css",
        "tip.css",
        "text-editor.css",
        "trigger.css",
        "checkbox.css",
        "radio-button.css",
        "form.css"
    ];

    for (var i = 0, j = d7is.length; i < j; i++) {
        writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/inherent/" + d7is[i]);
        writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/default/" + d7is[i]);
    }

    //writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/inherent/inherent.css");
    //writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/default/default.css");

    for (var i = 0; i < styleSheets.length; i++) {
		writeIncludeStyleSheet(CLIENT_ROOT + "client/skins/touch/" + styleSheets[i]);
	}

	for (var i = 0; i < testLibsBefore.length; i++) {
		writeIncludeScriptlet(CLIENT_ROOT + testLibsBefore[i]);
	}

	for (var i = 0; i < doradoLibs.length; i++) {
		writeIncludeScriptlet(CLIENT_ROOT + "client/scripts/dorado/" + doradoLibs[i]);
	}

    for (var i = 0; i < touchLibs.length; i++) {
        writeIncludeScriptlet(CLIENT_ROOT + "client/scripts/dorado/" + touchLibs[i]);
    }

	function writeIncludeScriptlet(file) {
		document.writeln("<script language=\"JavaScript\" type=\"text/javascript\" src=\"" + file /*+ cacheBuster*/ + "\" charset=\"utf-8\"></script>");
	}

	function writeIncludeStyleSheet(file) {
		document.writeln("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + file + cacheBuster + "\" />");
	}
})();
