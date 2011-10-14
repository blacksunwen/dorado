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
