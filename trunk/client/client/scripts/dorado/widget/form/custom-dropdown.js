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
		box.set("control", this._control);
		return box;
	}
});



