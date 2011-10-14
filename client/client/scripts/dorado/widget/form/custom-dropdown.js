/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Trigger
 * @class 自定义下拉框。
 * @extends dorado.widget.DropDown
 */
dorado.widget.CustomDropDown = $extend(dorado.widget.DropDown,/** @scope dorado.widget.CustomDropDown.prototype */ {
	$className: "dorado.widget.CustomDropDown",
	
	ATTRIBUTES: /** @scope dorado.widget.CustomDropDown.prototype */ {
	
		/**
		 * 要显示在自定义下拉框中的控件。
		 * @type dorado.widget.Control
		 * @attribute writeBeforeReady
		 */
		control: {
			writeBeforeReady: true,
			innerComponent: ""
		}
	},
	
	createDropDownBox: function(editor, dropDown) {
		var box = $invokeSuper.call(this, arguments);
		var control = this._control;
		box.set("control", control);
		box.addListener("beforeShow", function() {			
			var $box = jQuery(box.getDom().firstChild), boxWidth = $box.width(), boxHeight = $box.height();
			var $dom = jQuery(control.getDom()), realWidth = $dom.outerWidth(), realHeight = $dom.outerHeight(), shouldRefresh;
			if (realWidth < boxWidth) {
				control.set("width", boxWidth);
				shouldRefresh = true;
			}
			if (realHeight < boxHeight) {
				control.set("height", boxHeight);
				shouldRefresh = true;
			}
			if (shouldRefresh) control.refresh();
		});
		return box;
	}
});



