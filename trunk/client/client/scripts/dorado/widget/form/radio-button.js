/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @class 单选框组件。
 * @extends dorado.widget.Control
 */
dorado.widget.RadioButton = $extend(dorado.widget.Control, /** @scope dorado.widget.RadioButton.prototype */ {
	$className: "dorado.widget.RadioButton",
	_inherentClassName: "i-radio",
	focusable: true,
	
	ATTRIBUTES: /** @scope dorado.widget.RadioButton.prototype */ {
		className: {
			defaultValue: "d-radio"
		},
		
		height:  {
			independent : true
		},
		
		/**
		 * 单选框显示的文本。
		 * @type String
		 * @attribute
		 */
		text: {},
		
		/**
		 * 单选框的值。
		 * @type String
		 * @attribute
		 */
		value: {},
		
		/**
		 * 单选框是否选中，默认值是false。
		 * @type boolean
		 * @default false
		 * @attribute
		 */
		checked: {},
		
		/**
		 * 单选框是否只读，默认值是false。
		 * @type boolean
		 * @default false
		 * @attribute
		 */
		readOnly: {}
	},
	
	_isReadOnly: function() {
		var radioButton = this, radioGroup = radioButton._radioGroup;
		return radioButton._readOnly || radioGroup._readOnly || radioGroup._readOnly2;
	},
	
	onClick: function() {
		var radioButton = this;
		if (!radioButton._isReadOnly()) {
			if (!radioButton._checked) {
				radioButton._checked = true;
				if (radioButton._radioGroup) {
					radioButton._radioGroup._valueChange(radioButton);
				}
			}
		}
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var radioButton = this, checked = radioButton._checked, text = radioButton._text, jDom;
		if (dom) {
			jDom = jQuery(dom);
			var iconEl = dom.firstChild, textEl = iconEl.nextSibling;
			if (checked) {
				$fly(iconEl).removeClass("unchecked").addClass("checked");
			} else {
				$fly(iconEl).removeClass("checked").addClass("unchecked");
			}
			$fly(textEl).text(text);
		}
	},
	
	createDom: function() {
		var radioButton = this, dom, checked = radioButton._checked;
		
		dom = $DomUtils.xCreate({
			tagName: "div",
			className: radioButton._className,
			content: [{
				tagName: "span",
				className: "icon"
			}, {
				tagName: "span",
				className: "text",
				content: radioButton._text
			}]
		});
		
		jQuery(dom).addClassOnHover(radioButton._className + "-hover", null, function() {
			return !radioButton._isReadOnly();
		}).addClassOnClick(radioButton._className + "-click", null, function() {
			return !radioButton._isReadOnly();
		});
		
		return dom;
	},
	
	isFocusable: function() {
		return !this._isReadOnly() && $invokeSuper.call(this);
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Form
 * @class 单选框组
 * @extends dorado.widget.AbstractDataEditor
 */
dorado.widget.RadioGroup = $extend(dorado.widget.AbstractDataEditor, /** @scope dorado.widget.RadioGroup.prototype */ {
    $className: "dorado.widget.RadioGroup",
    _inherentClassName: "i-radiogroup",
	
	ATTRIBUTES: /** @scope dorado.widget.RadioGroup.prototype */ {
		className: {
			defaultValue: "d-radiogroup"
		},
		
		/**
		 * 单选框的排列方式。目前具有以下几种取值:
		 * <ul>
		 * <li>vertical - 垂直布局。每个单选框各占一行。</li>
		 * <li>flow - 流式布局。</li>
		 * </ul>
		 * 默认的布局方式为vertical。
		 * @type String
		 * @default "flow"
		 * @attribute
		 */
		layout: /** @scope dorado.widget.RadioGroup.prototype */ {
			defaultValue: "flow"
		},
		
		/**
		 * RadioGroup中的radioButtons。
		 * @type dorado.widget.RadioButton[]
		 * @attribute
		 */
		radioButtons: {
			setter: function(radioButtons) {
				var radioGroup = this, oldValue = this._radioButtons, dom = radioGroup._dom;
				if (oldValue) {
					radioGroup.clearRadioButtons();
				}
				radioGroup._radioButtons = radioButtons;
				if (radioButtons) {
					for (var i = 0; i < radioButtons.length; i++) {
						var radioButton = radioButtons[i];
						if (!(radioButton instanceof dorado.widget.RadioButton)) {
							radioButtons[i] = radioButton = new dorado.widget.RadioButton(radioButton);
						}
						if (dom) {
							radioButton._radioGroup = radioGroup;							
							if (radioButton._value == radioGroup._value) {
								radioGroup.currentRadioButton = radioButton;
								radioButton._checked = true;
							}
							radioButton.render(dom);
						}
						radioGroup.registerInnerControl(radioButton);
					}
				}
			}
		},
		
		/**
		 * 单选框组的值。
		 * @type String|Number
		 * @attribute
		 */
		value: {
			setter: function(value) {
				this.setValue(value);
			}
		},
		
		/**
		 * 单选框组是否只读。
		 * @type boolean
		 * @attribute
		 */
		readOnly: {}
	},
	
	EVENTS: /** @scope dorado.widget.RadioGroup.prototype */ {
		/**
		 * 当单选框组的value发生变化的时候触发的事件。
		 * @param self 触发事件的单选框组。
		 * @event
		 */
		onValueChange: {}
	},
	
	setValue: function(value) {
		var radioGroup = this, radioButtons = radioGroup._radioButtons;
		if (radioButtons) {
			var found = false;
			for (var i = 0, j = radioButtons.length; i < j; i++) {
				var radioButton = radioButtons[i];
				if ((value + '') == (radioButton._value + '')) {
					found = true;
					radioGroup._setValue(radioButton);
					break;
				}
			}
			if (!found) radioGroup._setValue(null);
		}
		radioGroup._value = value;
	},
	
	addRadioButton: function(radioButton, index) {
		var radioGroup = this, radioButtons = radioGroup._radioButtons, dom = radioGroup._dom, refDom;
		if (!radioButtons) {
			radioButtons = radioGroup._radioButtons = [];
		}
		if (!(radioButton instanceof dorado.widget.RadioButton)) {
			radioButton = new dorado.widget.RadioButton(radioButton);
		}
		if (typeof index == "number") {
			var refButton = radioButtons[index];
			if (refButton) {
				refDom = refButton._dom;
			}
			radioButtons.insert(radioButton, index);
		} else {
			radioButtons.push(radioButton);
		}
		if (dom) {
			radioButton._radioGroup = radioGroup;
			if (radioButton._value == radioGroup._value) {
				radioGroup.currentRadioButton = radioButton;
				radioButton._checked = true;
			}
			radioButton.render(dom, refDom);
		}
		radioGroup.registerInnerControl(radioButton);
	},
	
	removeRadioButton: function(radioButton) {
		var radioGroup = this, radioButtons = radioGroup._radioButtons, index;
		if (!radioButtons) return;
		if (typeof radioButton == "number") {
			index = radioButton;
			radioButton = radioButtons[radioButton];
			radioGroup.unregisterInnerControl(radioButton);
			radioButton.destroy();
			radioButtons.removeAt(index);
			if (radioGroup.currentRadioButton == radioButton) {
				radioGroup.currentRadioButton = null;
			}
		} else if (radioButton && radioButton.destroy) {
			radioGroup.unregisterInnerControl(radioButton);
			radioButton.destroy();
			if (radioGroup.currentRadioButton == radioButton) {
				radioGroup.currentRadioButton = null;
			}
		}
	},
	
	clearRadioButtons: function() {
		var radioGroup = this, radioButtons = radioGroup._radioButtons || [], radioButton;
		for (var i = 0; i < radioButtons.length; i++) {
			radioButton = radioButtons[i];
			radioGroup.unregisterInnerControl(radioButton);
			radioButton.destroy();
		}
		radioGroup._radioButtons = null;
		radioGroup.currentRadioButton = null;
	},
	
	_setValue: function(radioButton) {
		var radioGroup = this, value = radioButton ? radioButton._value : null;
		
		if (radioGroup.currentRadioButton == radioButton) {
			return;
		}
		
		if (radioGroup.currentRadioButton) {
			radioGroup.currentRadioButton._checked = false;
			radioGroup.currentRadioButton.refresh();
		}
		if (radioButton) {
			radioButton._checked = true;
			radioButton.refresh();
		}
		radioGroup.currentRadioButton = radioButton;
	},
	
	_valueChange: function(radioButton) {
		var radioGroup = this, value = radioButton ? radioButton._value : null;
		
		if (radioGroup.currentRadioButton == radioButton || radioGroup._value == value) {
			return;
		}
		
		radioGroup._lastPost = radioGroup._value;
		radioGroup._value = value;
		radioGroup._dirty = true;
		try {
			radioGroup.post();
		} 
		catch (e) {
			radioGroup._value = radioGroup._lastPost;
			radioGroup._dirty = false;
			if (radioButton) {
				radioButton._checked = false;
				radioButton.refresh();
			}
			throw e;
		}
		
		this._setValue(radioButton);
		radioGroup.fireEvent("onValueChange", radioGroup, {});
	},
	
	createDom: function() {
		var radioGroup = this, layout = radioGroup._layout, radioButtons = radioGroup._radioButtons;
		
		var dom = $DomUtils.xCreate({
			tagName: "div",
			className: radioGroup._className
		});
		
		if (radioButtons) {
			for (var i = 0, j = radioButtons.length; i < j; i++) {
				var radioButton = radioButtons[i];
				radioButton._radioGroup = radioGroup;
				if (radioButton._value == radioGroup._value) {
					radioGroup.currentRadioButton = radioButton;
					radioButton._checked = true;
				}
				radioButton.render(dom);
			}
		}
		return dom;
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var group = this, layout = group._layout;
		if (group._dataSet) {
			var value, dirty, readOnly = this._dataSet._readOnly;
			if (group._property) {
				var bindingInfo = group._bindingInfo;
				if (bindingInfo.entity instanceof dorado.Entity) {
					value = bindingInfo.entity.get(group._property);
					dirty = bindingInfo.entity.isDirty(group._property);
				}
				readOnly = readOnly || (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
				
				if (!group._radioButtons || !group._radioButtons.length) {
					var radioButtons = [], mapping = bindingInfo.propertyDef._mapping;
					if (mapping) {
						for (var i = 0; i < mapping.length; i++) {
							var item = mapping[i];
							radioButtons.push({
								value: item.key,
								text: item.value
							});
						}
					}
					if (radioButtons.length) group.set("radioButtons", radioButtons);
				}
			} else {
				readOnly = true;
			}
			group._readOnly2 = readOnly;
			group.setValue(value);
			group.setDirty(dirty);
		}
		
		if (layout == "flow") {
			$fly(dom).addClass("i-radiogroup-flow " + group._className + "-flow");
		}
	},
	
	doOnKeyDown: function(event) {
		if (event.ctrlKey) {
			return true;
		}
		var group = this, radioButtons = group._radioButtons, currentRadioButton = group.currentRadioButton, currentButtonIndex = currentRadioButton ? radioButtons.indexOf(currentRadioButton) : -1, buttonCount = radioButtons.length, newIndex, newRadioButton, retValue = true;
		
		switch (event.keyCode) {
			case 38://up arrow
			case 37://left arrow
				if (currentButtonIndex == 0) {
					newIndex = buttonCount - 1;
				} else {
					if (currentButtonIndex > 0 && currentButtonIndex < buttonCount) {
						newIndex = currentButtonIndex - 1;
					} else {
						newIndex = 0;
					}
				}
				retValue = false;
				break;
				
			case 39://right arrow
			case 40://down arrow
				if (currentButtonIndex >= 0 && currentButtonIndex < buttonCount - 1) {
					newIndex = currentButtonIndex + 1;
				} else {
					newIndex = 0;
				}
				retValue = false;
				break;
		}
		newRadioButton = radioButtons[newIndex];
		if (newRadioButton) {
			group._valueChange(newRadioButton);
		}
		return retValue;
	},
	
	post: function() {
		if (!this._dirty) {
			return false;
		}
		var eventArg = {
			processDefault: true
		};
		this.fireEvent("beforePost", this, eventArg);
		if (eventArg.processDefault === false) return false;
		this.doPost();
		this._lastPost = this._value;
		this._dirty = false;
		this.fireEvent("onPost", this, eventArg); // 此事件已被声明为不能抛出异常
		return true;
	}
});