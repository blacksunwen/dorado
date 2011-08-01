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
		className: {
			defaultValue: "d-float-panel"
		},
		
		/**
		 * 是否显示关闭按钮.
		 * @attribute
		 * @type boolean
		 */
		closeable: {
			defaultValue: true,
			setter: function(value) {
				var panel = this, captionBar = panel._captionBar, button;
				panel._closeable = value;
				if (captionBar) {
					if (value) {
						button = captionBar.getButton(panel._id + "_close");
						if (button) {
							$fly(button._dom).css("display", "");
						} else {
							panel._createCloseButton();
						}
					} else {
						button = captionBar.getButton(panel._id + "_close");
						if (button) {
							$fly(button._dom).css("display", "none");
						}
					}
				}
			}
		},
		
		/**
		 * 当用户点击关闭以后要做的动作，默认是hide，可选值为hide和close。hide仅仅是隐藏该对话框，close会完全销毁该对话框，销毁以后该对话框不能再使用。
		 * @attribute
		 * @type String
		 */
		closeAction: {
			defaultValue: "hide"
		},
		
		visible: {
			defaultValue: false
		}
	},
	
	EVENTS: /** @scope dorado.widget.FloatPanel.prototype */ {},
	
	createDom: function() {
		var panel = this, dom = $invokeSuper.call(this, arguments);
		if (panel._closeable) {
			panel._createCloseButton();
		}
		return dom;
	},
	
	_createCloseButton: function() {
		var panel = this, captionBar = panel._captionBar;
		if (captionBar) {
			captionBar.addButton(new dorado.widget.SimpleButton({
				id: panel._id + "_close",
				listener: {
					onClick: function() {
						panel.close();
					}
				},
				className: "d-close-button"
			}));
		}
	},
	
	/**
	 * 关闭面板。
	 */
	close: function() {
		var panel = this, eventArg = {};
		panel.fireEvent("beforeClose", panel, eventArg);
		if (eventArg.processDefault === false) return;
		panel.hide();
		panel.fireEvent("onClose", panel);
		if (panel._closeAction == "close") {
			panel.destroy();
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
