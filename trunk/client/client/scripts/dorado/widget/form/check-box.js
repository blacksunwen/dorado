/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Form
 * @class 复选框组件
 * <p>
 * 复选框组件，并非对系统的checkbox进行的包装，因此支持三态。
 * </p>
 * <p>
 * 该组件有三种状态：on、off、mixed，对应的值分别为复选框的onValue、offValue、mixedValue属性的值，对应的checked的值为true、false、null。
 * </p>
 * <p>
 * triState属性主要影响的是用户操作层面，即使triState=false，通过设置 value=mixedValue或checked=null，仍可以使CheckBox进入第三态。但是在triState=false时用户不能通过 鼠标点击令CheckBox进入第三态；而当triState=true时点击CheckBox，其状态在on、off、mixed三种状态中循环变化。
 * </p>
 * @extends dorado.widget.AbstractDataEditor
 */
dorado.widget.CheckBox = $extend(dorado.widget.AbstractDataEditor, /** @scope dorado.widget.CheckBox.prototype */ {
	$className: "dorado.widget.CheckBox",
	_inherentClassName: "i-checkbox",
	
	ATTRIBUTES: /** @scope dorado.widget.CheckBox.prototype */ {
		className: {
			defaultValue: "d-checkbox"
		},
		
		height: {
			independent: true
		},
		
		/**
		 * 是否只显示图标、不显示文字。主要在Grid和Tree中作为编辑器的时候被使用。
		 * @attribute writeBeforeReady
		 * @default false
		 * @type boolean
		 */
		iconOnly: {
			writeBeforeReady: true,
			setter: function(value) {
				this.iconOnly = value;
				this._inherentClassName = (value ? "i-checkbox i-checkbox-icononly" : "i-checkbox");
			}
		},
		
		/**
		 * 当复选框被选中的时候的value值。
		 * @type int|boolean|Object
		 * @default true
		 * @attribute
		 */
		onValue: {
			defaultValue: true
		},
		
		/**
		 * 当复选框未被选中的时候的value值。
		 * @type int|boolean|Object
		 * @default false
		 * @attribute
		 */
		offValue: {
			defaultValue: false
		},
		
		/**
		 * 当复选框处于第三态时的value值。
		 * @type int|boolean|Object
		 * @attribute
		 */
		mixedValue: {},
		
		/**
		 * 复选框右侧显示的文本。
		 * @type String
		 * @attribute
		 */
		caption: {},
		
		/**
		 * 复选框的值。
		 * @type int|boolean|Object
		 * @attribute
		 */
		value: {
			defaultValue: false,
			getter: function() {
				return this._checked ? this._onValue : ((this._checked === null || this._checked === undefined) ? this._mixedValue : this._offValue);
			},
			setter: function(v) {
				if (this._onValue === v) {
					this._checked = true;
				} 
				else if (this._offValue === v) {
					this._checked = false;
				} else {
					this._checked = null;
				}
			}
		},
		
		/**
		 * 复选框是否被选中。
		 * @type Boolean
		 * @default false
		 * @attribute
		 */
		checked: {
			defaultValue: false,
			setter: function(value) {
				if (this._triState) {
					this._checked = value;
				} else {
					this._checked = !!value;
				}
			}
		},
		
		/**
		 * 是否3态复选框，即是否启用复选框的mixed状态。
		 * @type boolean
		 * @default false
		 * @attribute
		 */
		triState: {
			defaultValue: false
		}
	},
	
	EVENTS: /** @scope dorado.widget.CheckBox.prototype */ {
		/**
		 * 当复选框的value发生变化的时候触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onValueChange: {}
	},
	
	onClick: function() {
		var checkBox = this;
		
		if (checkBox._readOnly || this._readOnly2) {
			return;
		}
		
		checkBox._lastPostChecked = checkBox._checked;
		if (checkBox._triState) {
			if (checkBox._checked === null || checkBox._checked === undefined) {
				checkBox._checked = true;
			} else if (checkBox._checked === true) {
				checkBox._checked = false;
			} else {
				checkBox._checked = null;
			}
		} else {
			checkBox._checked = !checkBox._checked;
		}
		
		checkBox._dirty = true;
		try {
			checkBox.post();
		} 
		catch (e) {
			checkBox._checked = checkBox._lastPostChecked;
			checkBox._dirty = false;
			throw e;
		}
		checkBox.refresh();
		checkBox.fireEvent("onValueChange", checkBox);
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var checkBox = this, checked = checkBox._checked, caption = checkBox._caption || '';
		$fly(dom)[checkBox._readOnly || checkBox._readOnly2 ? "addClass" : "removeClass"](checkBox._className + "-readonly");
		if (checkBox._dataSet) {
			checked = undefined;
			var value, dirty, readOnly = this._dataSet._readOnly;
			if (checkBox._property) {
				var bindingInfo = checkBox._bindingInfo;
				var dt = bindingInfo.dataType;
				if (dt) {
					var config;
					switch (dt._code) {
						case dorado.DataType.BOOLEAN:{
							config = {
								triState: true
							};
							break;
						}
						case dorado.DataType.PRIMITIVE_INT:
						case dorado.DataType.PRIMITIVE_FLOAT:{
							config = {
								offValue: 0,
								onValue: 1
							};
							break;
						}
						case dorado.DataType.INTEGER:
						case dorado.DataType.FLOAT:{
							config = {
								offValue: 0,
								onValue: 1,
								triState: true
							};
							break;
						}
					}
					if (config) {
						this.set(config, {
							preventOverwriting: true
						});
					}
				}
				
				if (bindingInfo.entity instanceof dorado.Entity) {
					value = bindingInfo.entity.get(checkBox._property);
					dirty = bindingInfo.entity.isDirty(checkBox._property);
				}
				readOnly = readOnly || (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
			} else {
				readOnly = true;
			}
			
			value += '';
			if (value == (checkBox._onValue + '')) {
				checked = true;
			} else if (value == (checkBox._offValue + '')) {
				checked = false;
			}
			checkBox._checked = checked;
			checkBox._readOnly2 = readOnly;
			checkBox.setDirty(dirty);
		}
		
		if (!checkBox._iconOnly) {
			var iconEl = dom.firstChild, captionEl = iconEl.nextSibling;
			if (checked) {
				$fly(iconEl).removeClass("unchecked halfchecked").addClass("checked");
			} else if (checked == null && checkBox._triState) {
				$fly(iconEl).removeClass("checked unchecked").addClass("halfchecked");
			} else {
				$fly(iconEl).removeClass("checked halfchecked").addClass("unchecked");
			}
			captionEl.innerText = caption;
		} else {
			var hovering = false, cls, $dom = $fly(dom);
			$dom.removeClass("d-checkbox-checked d-checkbox-unchecked d-checkbox-halfchecked");
			if ($dom.hasClass("d-checkbox-unchecked-hover") || $dom.hasClass("d-checkbox-checked-hover") || $dom.hasClass("d-checkbox-halfchecked-hover")) {
				$dom.removeClass("d-checkbox-checked-hover d-checkbox-unchecked-hover d-checkbox-halfchecked-hover");
				hovering = true;
			}
			if (checked) {
				cls = hovering ? "d-checkbox-checked-hover" : "d-checkbox-checked";
				$dom.addClass(cls);
			} else if (checked == null) {
				cls = hovering ? "d-checkbox-halfchecked-hover" : "d-checkbox-halfchecked";
				$dom.addClass(cls);
			} else {
				cls = hovering ? "d-checkbox-unchecked-hover" : "d-checkbox-unchecked";
				$dom.addClass(cls);
			}
		}
	},
	
	createDom: function() {
		var checkBox = this, dom;
		if (checkBox._iconOnly) {
			checkBox._className = checkBox._className + "-icononly";
			dom = $DomUtils.xCreate({
				tagName: "SPAN",
				className: checkBox._className
			});
			$fly(dom).hover(function() {
				if (!checkBox._readOnly) {
					if (checkBox._checked) {
						$fly(dom).removeClass("d-checkbox-checked").addClass("d-checkbox-checked-hover");
					} else if (checkBox._checked == null) {
						$fly(dom).removeClass("d-checkbox-halfchecked").addClass("d-checkbox-halfchecked-hover");
					} else {
						$fly(dom).removeClass("d-checkbox-unchecked").addClass("d-checkbox-unchecked-hover");
					}
				}
			}, function() {
				$fly(dom).removeClass("d-checkbox-checked-hover d-checkbox-unchecked-hover d-checkbox-halfchecked-hover");
				if (checkBox._checked) {
					$fly(dom).addClass("d-checkbox-checked");
				} else if (checkBox._checked === null || checkBox._checked === undefined) {
					$fly(dom).addClass("d-checkbox-halfchecked");
				} else {
					$fly(dom).addClass("d-checkbox-unchecked");
				}
			});
		} else {
			dom = $DomUtils.xCreate({
				tagName: "SPAN",
				className: checkBox._className,
				content: [{
					tagName: "SPAN",
					className: "icon"
				}, {
					tagName: "SPAN",
					className: "caption",
					content: checkBox._caption || ''
				}]
			});
			
			jQuery(dom).addClassOnHover(checkBox._className + "-hover", null, function() {
				return !checkBox._readOnly;
			}).addClassOnClick(checkBox._className + "-click", null, function() {
				return !checkBox._readOnly;
			});
		}
		
		return dom;
	},
	
	post: function() {
		try {
			if (!this._dirty) {
				return false;
			}
			var eventArg = {
				processDefault: true
			};
			this.fireEvent("beforePost", this, eventArg);
			if (eventArg.processDefault === false) return false;
			this.doPost();
			this._lastPostChecked = this._checked;
			this._dirty = false;
			this.fireEvent("onPost", this);
			return true;
		} 
		catch (e) {
			dorado.Exception.processException(e);
		}
	},
	
	doOnKeyDown: function(evt) {
		var retValue = true;
		switch (evt.keyCode) {
			case 32:
				// space
				this.onClick();
				retValue = false;
				break;
		}
		return retValue;
	}
});
