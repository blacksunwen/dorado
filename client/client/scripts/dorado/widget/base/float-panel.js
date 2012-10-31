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
/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Base
 * @class 浮动面板
 * @extends dorado.widget.Panel
 * @extends dorado.widget.FloatControl
 */
dorado.widget.FloatPanel = $extend([dorado.widget.Panel, dorado.widget.FloatControl], /** @scope dorado.widget.FloatPanel.prototype */ {
	$className: "dorado.widget.FloatPanel",
	focusable: true,
	
	ATTRIBUTES: /** @scope dorado.widget.FloatPanel.prototype */ {
		visible: {
			defaultValue: false
		}
	},
	
	doShow: function() {
		var panel = this, doms = panel._doms;
		$fly([doms.contentPanel, doms.buttonPanel]).css("display", "");
		
		$invokeSuper.call(this, arguments);
	},
	
	doAfterShow: function() {
		var panel = this;
		panel._minimized = false;
		$invokeSuper.call(this, arguments);
	}
});
