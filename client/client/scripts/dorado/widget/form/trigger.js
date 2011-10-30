/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Trigger
 * @class 与编辑框相关联的触发器。
 * @extends dorado.widget.Component
 */
dorado.widget.Trigger = $extend(dorado.widget.Component, /** @scope dorado.widget.Trigger.prototype */ {
	$className: "dorado.widget.Trigger",
	_inherentClassName: "i-trigger",
	
	ATTRIBUTES: /** @scope dorado.widget.Trigger.prototype */ {
	
		/**
		 * CSS类名。
		 * @type String
		 * @attribute writeBeforeReady
		 */
		className: {
			defaultValue: "d-trigger",
			writeBeforeReady: true
		},
		
		/**
		 * 图标。
		 * @type String
		 * @attribute
		 */
		icon: {
			writeBeforeReady: true
		},
		
		/**
		 * 图标元素的CSS Class。
		 * @type String
		 * @attribute
		 * @default "d-trigger-icon-custom"
		 */
		iconClass: {
			writeBeforeReady: true,
			defaultValue: "d-trigger-icon-custom"
		}
	},
	
	EVENTS: /** @scope dorado.widget.Trigger.prototype */ {
	
		/**
		 * 当触发器被触发时激活的事件。
		 * <p>
		 * 即当用户点击关联在编辑框上的触发器按钮时，本事件将被触发。
		 * </p>
		 * @param {Object} self 事件的发起者，即控件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.AbstractTextEditor} arg.editor 激活此触发器的编辑框。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onExecute: {}
	},
	
	/**
	 * 当绑定了此触发器的编辑框获得控制焦点时触发的事件。
	 * @dorado.widget.Trigger#onEditorFocus
	 * @function
	 * @abstract
	 * @param {dorado.widget.AbstractTextEditor} editor 获得焦点的编辑框。
	 * @protected
	 */
	/**
	 * 当绑定了此触发器的编辑框失去控制焦点时触发的事件。
	 * @dorado.widget.Trigger#onEditorBlur
	 * @function
	 * @abstract
	 * @param {dorado.widget.AbstractTextEditor} editor 失去焦点的编辑框。
	 * @protected
	 */
	/**
	 *当绑定了此触发器的编辑框在拥有控制焦点时并有按键被被按下时触发的事件。
	 * @dorado.widget.Trigger#onEditorKeyDown
	 * @function
	 * @abstract
	 * @param {dorado.widget.AbstractTextEditor} editor 激活此方法的编辑框。
	 * @param {Event} evt 系统事件中的Event对象。
	 * @protected
	 */
	// =====
	
	/**
	 * 创建用于显示在关联的编辑框中的触发按钮。
	 * @param {dorado.widget.AbstractTextEditor} editor 创建的触发按钮将被用于现在哪个编辑框中。
	 * @return {dorado.widget.Control} 作为触发按钮的控件。
	 */
	createTriggerButton: function(editor) {
		var trigger = this;
		var control = new dorado.widget.SimpleIconButton({
			exClassName:  (trigger._inherentClassName || '') + " " + (trigger._className || ''),
			icon: trigger._icon,
			iconClass: trigger._iconClass,
			onClick: function() {
				editor.onTriggerClick(trigger);
			}
		});
		jQuery(control.getDom()).addClassOnClick("i-trigger-down d-trigger-down", null, function() {
			return !editor.get("readOnly");
		});
		return control;
	},
	
	/**
	 * 执行该触发器。此方法一般应由系统内部调用。
	 * @param {dorado.widget.AbstractTextEditor} editor 激活此触发器的编辑框。
	 */
	execute: function(editor) {
		this.fireEvent("onExecute", this, {
			editor: editor
		});
	}
});
