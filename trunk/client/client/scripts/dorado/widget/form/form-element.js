(function() {

	var specialFormConfigProps = ["width", "height", "className", "exClassName"];
	
	var DEFAULT_OK_MESSAGES = [{
		state: "ok"
	}];
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 包含表单项中主要配置信息的抽象类。
	 * @abstract
	 */
	dorado.widget.FormConfig = $class(/** @scope dorado.widget.FormConfig.prototype */{
		$className: "dorado.widget.FormConfig",
		
		ATTRIBUTES: /** @scope dorado.widget.FormConfig.prototype */ {
		
			/**
			 * 宽度。
			 * @type int
			 * @attribute
			 */
			width: {},
			
			/**
			 * 高度。
			 * @type int
			 * @attribute
			 */
			height: {},
			
			/**
			 * CSS类名。
			 * @type String
			 * @attribute
			 */
			className: {},
			
			/**
			 * 扩展CSS类名。
			 * @type String
			 * @attribute
			 */
			exClassName: {},
			
			/**
			 * 表单项类型。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>text - 文本编辑框。</li>
			 * <li>password - 口令编辑框。</li>
			 * <li>textArea - 多行文本编辑框。</li>
			 * <li>checkBox - 单选框。</li>
			 * <li>radioGroup - 多选框组合。
			 * 此选项在FormElement不与某个DataSet绑定的情况下意义不大，因为基本上radioGroup都需要做一些特别的设置之后开可以开始使用。
			 * 而在于DataSet绑定时，内部创建的DataRadioGroup则可以根据相应PropertyDef中的mapping属性自动的初始化其中的单选框。</li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute writeBeforeReady
			 */
			type: {
				writeBeforeReady: true
			},
			
			/**
			 * 关联的编辑框触发器。下拉框也是一种特殊的编辑框触发器。
			 * @type dorado.widget.EditorTrigger
			 * @attribute
			 */
			trigger: {},
			
			/**
			 * 绑定的数据实体。
			 * @type Object|dorado.Entity
			 * @attribute
			 * @see dorado.widget.AbstractEditor#attribute:entity
			 */
			entity: {},
			
			/**
			 * 文本标签与编辑器之间的分隔字符。
			 * @type String
			 * @attribute
			 * @default ":"
			 */
			labelSeparator: {
				defaultValue: ":"
			},
			
			/**
			 * 文本标签是否可见。
			 * @type boolean
			 * @attribute
			 * @default true
			 */
			showLabel: {
				defaultValue: true
			},
			
			/**
			 * 文本标签的宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 * @default 80
			 */
			labelWidth: {
				defaultValue: 80,
				writeBeforeReady: true
			},
			
			/**
			 * 文本标签与编辑框之间空隙的宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 * @default 3
			 */
			labelSpacing: {
				defaultValue: 3,
				writeBeforeReady: true
			},
			
			/**
			 * 文本标签的显示位置。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>left - 左侧。</li>
			 * <li>top - 上方。</li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute writeBeforeReady
			 */
			labelPosition: {
				writeBeforeReady: true
			},
			
			/**
			 * 文本标签的水平对齐方式。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>left - 左对齐。</li>
			 * <li>right - 右对齐。</li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute writeBeforeReady
			 */
			labelAlign: {
				writeBeforeReady: true
			},
			
			/**
			 * 编辑器的宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 */
			editorWidth: {
				writeBeforeReady: true
			},
			
			/**
			 * 是否显示提示信息区。
			 * @type boolean
			 * @attribute writeBeforeReady
			 * @default true
			 */
			showHint: {
				writeBeforeReady: true,
				defaultValue: true
			},
			
			
			/**
			 * 提示信息区的宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 * @default 22
			 */
			hintWidth: {
				defaultValue: 22,
				writeBeforeReady: true
			},
			
			/**
			 * 提示信息区与编辑框之间的空隙的宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 * @default 3
			 */
			hintSpacing: {
				defaultValue: 3,
				writeBeforeReady: true
			},
			
			/**
			 * 是否将提示信息的文本消息直接显示在界面中。
			 * <p>
			 * 否则见只在提示信息区显示图标形式的表单项状态。
			 * </p>
			 * @type int
			 * @attribute writeBeforeReady
			 */
			showHintMessage: {
				writeBeforeReady: true
			},
			
			/**
			 * 提示信息区的显示位置。
			 * <p>
			 * 目前支持以下几种取值：
			 * <ul>
			 * <li>right - 右侧。</li>
			 * <li>bottom - 下方。</li>
			 * </ul>
			 * </p>
			 * @type String
			 * @attribute writeBeforeReady
			 */
			hintPosition: {
				writeBeforeReady: true
			},
			
			/**
			 * 内部使用的信息提示控件。
			 * @type dorado.widget.dorado.widget.DataMessage
			 * @attribute readOnly
			 */
			hintControl: {
				readOnly: true
			},
			
			/**
			 * 是否只读。
			 * @type boolean
			 * @attribute
			 */
			readOnly: {}
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Form
	 * @class 表单配置。
	 * <p>
	 * 此控件并不用于显示，其目的是为了方便为一批表单元素统一的定义表单属性。
	 * 例如：我们可以在View中添加一个FormProfile控件，并在其中定义好labelWidth=120。 然后在后面定义具体的{@link dorado.widget.FormElement}的过程中，我们只要将其与这个FormProfile控件进行绑定，那么该FormElement就会自动的获得labelWidth=120的设置。
	 * </p>
	 * <p>
	 * 另外，为了进一步的简化操作。{@link dorado.widget.FormElement}还会在创建的过程中自动的在View中寻找是否存在id为"defaultFormProfile"的FormProfile控件。
	 * 如果找到则将自动建立与其的绑定关系。
	 * 因此，在很多情况下，我们只要声明一个id为"defaultFormProfile"的FormProfile控件，并且将需要的表单属性设置在其中。
	 * 然后，这个View中所有的FormElement在默认情况下，都将自动从中读取配置信息。
	 * </p>
	 * @extends dorado.widget.Component
	 * @extends dorado.widget.FormConfig
	 * @see dorado.widget.FormElement
	 */
	dorado.widget.FormProfile = $extend([dorado.widget.Component, dorado.widget.FormConfig], /** @scope dorado.widget.FormConfig.prototype */ {
		$className: "dorado.widget.FormProfile",
		
		ATTRIBUTES: /** @scope dorado.widget.FormProfile.prototype */ {
			/**
			 * 绑定的数据实体。
			 * @type Object|dorado.Entity
			 * @attribute
			 * @see dorado.widget.AbstractEditor#attribute:entity
			 */
			entity: {
				defaultValue: function() {
					return new dorado.widget.FormProfile.DefaultEntity();
				}
			},
			
			/**
			 * 绑定的数据集。
			 * @type dorado.widget.DataSet
			 * @attribute writeBeforeReady
			 */
			dataSet: {
				componentReference: true,
				writeBeforeReady: true
			},
			
			/**
			 * 数据路径，用于指定数据控件与数据集中的哪些数据节点进行关联。
			 * @type String
			 * @attribute writeBeforeReady
			 * @see dorado.DataPath
			 */
			dataPath: {
				writeBeforeReady: true
			}
		},
		
		constructor: function() {
			this._bindingElements = new dorado.ObjectGroup();
			$invokeSuper.call(this, arguments);
			this.addListener("onAttributeChange", function(self, arg) {
				var attr = arg.attribute;
				if (!dorado.widget.Control.prototype.ATTRIBUTES[attr] &&
				dorado.widget.FormConfig.prototype.ATTRIBUTES[attr]) {
					dorado.Toolkits.setDelayedAction(self, "$profileChangeTimerId", function() {
						var value = arg.value;
						self._bindingElements.invoke("onProfileChange");
					}, 20);
				}
			});
		}
	});

	dorado.widget.FormProfile.DefaultEntity = $class({});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 表单元素。
	 * <p>
	 * 表单元素是一种组合式的控件，提供此组件的目的是为了使表单的定义变得更加简单。 一般而言，一个表单元素包含文本标签、编辑器、提示信息三个部分。
	 * 我们在实际应用中所见的大部分输入域都是由这三个部分组成的。
	 * </p>
	 * <p>
	 * 由于表单元素的属性较多，并且往往一组表单元素都拥有相似的属性设置。
	 * 为了使用户能够更加方便的对表单元素的属性进行批量的设置，dorado特别提供了FormProfile组件来对表单元素进行增强。 其具体用法请参考{@link dorado.widget.FormProfile}的说明。
	 * </p>
	 * @extends dorado.widget.Control
	 * @extends dorado.widget.FormConfig
	 * @see dorado.widget.FormProfile
	 */
	dorado.widget.FormElement = $extend([dorado.widget.Control, dorado.widget.FormConfig], /** @scope dorado.widget.FormElement.prototype */ {
		$className: "dorado.widget.FormElement",
		
		ATTRIBUTES: /** @scope dorado.widget.FormElement.prototype */ {
		
			/**
			 * 宽度。
			 * @type int
			 * @attribute writeBeforeReady
			 * @default 260
			 */
			width: {
				defaultValue: 260,
				writeBeforeReady: true,
				independent: true
			},
			
			/**
			 * 高度。
			 * @type int
			 * @attribute writeBeforeReady
			 */
			height: {
				writeBeforeReady: true
			},
			
			className: {
				defaultValue: "d-form-element"
			},
			
			/**
			 * 绑定的表单配置。
			 * @type dorado.widget.FormPorfile
			 * @attribute
			 */
			formProfile: {
				componentReference: true,
				setter: function(formProfile) {
					if (dorado.Object.isInstanceOf(this._formProfile, dorado.widget.FormProfile)) {
						this._formProfile._bindingElements.objects.remove(this);
					}
					if (formProfile && !dorado.Object.isInstanceOf(formProfile, dorado.widget.FormProfile)) {
						var ref = formProfile;
						formProfile = ref.view.id(ref.component);
					}
					this._formProfile = formProfile;
					if (formProfile) {
						formProfile._bindingElements.objects.push(this);
					}
				}
			},
			
			/**
			 * 数据控件绑定的数据集。
			 * @type dorado.widget.DataSet
			 * @attribute
			 */
			dataSet: {
				setter: function(v) {
					this._dataSet = v;
					delete this._propertyDef;
				}
			},
			
			/**
			 * 数据路径，用于指定数据控件与数据集中的哪些数据节点进行关联。
			 * @type String
			 * @attribute
			 * @default "#"
			 * @see dorado.DataPath
			 */
			dataPath: {
				setter: function(v) {
					this._dataPath = v;
					delete this._propertyDef;
				}
			},
			
			/**
			 * 绑定的属性名。
			 * @type String
			 * @attribute writeBeforeReady
			 * @see dorado.widget.AbstractEditor#attribute:property
			 */
			property: {
				writeBeforeReady: true
			},
			
			/**
			 * 文本标签的内容。
			 * @type String
			 * @attribute
			 */
			label: {},
			
			/**
			 * 提示信息的内容。
			 * <p>
			 * 此属性在写入时可以传入单独的消息文本、消息对象或消息数组等多种形式的参数。
			 * 但在读取时系统会统一的返回消息数组的形式。
			 * 该数组中的每一项是一个JSON对象，包含下列两个子属性：
			 * <ul>
			 * <li>level	-	{String} 信息级别。取值范围包括：info、warn、error。默认值为error。</li>
			 * <li>text	-	{String} 信息内容。</li>
			 * </ul>
			 * </p>
			 * @type String|Object|[String]|[Object]
			 * @attribute
			 */
			hint: {
				setter: function(hint) {
				
					function trimSingleHint(hint) {
						if (!hint) return null;
						if (typeof hint == "string") {
							hint = [{
								level: "info",
								text: hint
							}];
						} else {
							hint.level = hint.level || "info";
							hint = [hint];
						}
						return hint;
					}
					
					function trimHints(hint) {
						if (!hint) return null;
						if (hint instanceof Array) {
							var array = [];
							for (var i = 0; i < hint.length; i++) {
								var h = trimSingleHint(hint[i]);
								if (!h) continue;
								array.push(h);
							}
							hint = (array.length) ? array : null;
						} else {
							hint = trimSingleHint(hint);
						}
						return hint;
					}
					
					this._hint = trimHints(hint);
				}
			},
			
			/**
			 * 内部使用的编辑器。
			 * @type dorado.widget.Control
			 * @attribute writeBeforeReady
			 */
			editor: {
				writeBeforeReady: true,
				innerComponent: "TextEditor"
			},
			
			/**
			 * 内部使用的编辑器的值。 此属性相当于一个访问内部使用的编辑器中值的快捷方式。
			 * @type Object
			 * @attribute
			 */
			value: {
				path: "editor.value"
			},
			
			readOnly: {
				setter: function(v) {
					this._readOnly = v;
					this.resetEditorReadOnly();
				}
			}
		},
		
		destroy: function() {
			if (this._destroyed) return;
			this.set("formProfile", null);
			$invokeSuper.call(this, arguments);
		},
		
		createDom: function() {
			if (!this._formProfile) {
				var view = this.get("view") || dorado.widget.View.TOP;
				this.set("formProfile", view.id("defaultFormProfile"));
			}
			this.onProfileChange();
			
			var labelConfig, editorConfig = {
				tagName: "DIV",
				className: "form-editor"
			};
			if (this._labelPosition == "top") {
				labelConfig = {
					tagName: "DIV",
					className: "form-label form-label-top",
					style: {
						position: "absolute"
					}
				};
				if (this._labelAlign == "right") {
					labelConfig.style.right = 0;
				} else {
					labelConfig.style.left = 0;
				}
				editorConfig.style = {
					width: "100%",
					overflow: "hidden"
				};
			} else {
				labelConfig = {
					tagName: "DIV",
					className: "form-label form-label-left" + ((this._labelAlign == "right") ? " form-label-align-right" : ""),
					style: {
						position: "absolute",
						top: 0,
						width: this._labelWidth
					}
				};
				editorConfig.style = {
					paddingLeft: this._labelWidth + this._labelSpacing +
					"px",
					height: "100%",
					overflow: "hidden"
				};
			}
			if (!this._showLabel) labelConfig.style.display = "none";
			
			if (this._showHint) {
				var hintConfig = {
					tagName: "DIV",
					className: "form-hint"
				};
				editorConfig.content = [hintConfig];
				if (this._hintPosition == "bottom") {
					hintConfig.style = {
						width: "100%"
					};
				} else {
					hintConfig.style = {
						position: "absolute",
						right: 0,
						top: 0
					};
				}
			}
			
			var dom = $DomUtils.xCreateElement({
				tagName: "DIV",
				content: [labelConfig, editorConfig],
				style: {
					position: "relative",
					overflow: "hidden"
				}
			});
			this._labelEl = dom.firstChild;
			this._editorEl = dom.lastChild;
			this._hintEl = this._editorEl.firstChild;
			return dom;
		},
		
		createEditor: function(type) {
			switch (type) {
				case "textArea":{
					return new dorado.widget.TextArea();
				}
				case "checkBox":{
					return new dorado.widget.CheckBox();
				}
				case "radioGroup":{
					return new dorado.widget.RadioGroup({
						layout: "flow"
					});
				}
				default:
					{
						return new dorado.widget.TextEditor();
					}
			}
		},
		
		getEditor: function() {
		
			function getRadioButtons(propertyDef) {
				var radioButtons = [], mapping = propertyDef._mapping;
				if (mapping) {
					for (var i = 0; i < mapping.length; i++) {
						var item = mapping[i];
						radioButtons.push({
							value: item.key,
							text: item.value
						});
					}
				}
				return radioButtons;
			}
			
			var control = this._editor, shouldInitControl = true;
			if (!control) {
				var propertyDef = this.getBindingPropertyDef();
				if (propertyDef) {
					if (!this._type) {
						var propertyDataType = propertyDef.get("dataType");
						if (propertyDataType) {
							if (propertyDataType._code == dorado.DataType.PRIMITIVE_BOOLEAN || propertyDataType._code == dorado.DataType.BOOLEAN) {
								this._type = (!propertyDef._mapping) ? "checkBox" : "radioGroup";
							}
						}
					}
					
					if (propertyDef._mapping) {
						if ((!this._type || this._type == "textEditor")) {
							this._trigger = new dorado.widget.AutoMappingDropDown({
								items: propertyDef._mapping
							});
						}
					}
				}
				
				this._editor = control = this.createEditor(this._type);
				if (control) {
					var config = {};
					this.initEditorConfig(config);
					control.set(config, {
						skipUnknownAttribute: true,
						tryNextOnError: true
					});
				}
				shouldInitControl = false;
			}
			
			if (control && !this._controlRegistered) {
				this._controlRegistered = true;
				
				if (this._showHint && control instanceof dorado.widget.AbstractEditor) {
					if (control instanceof dorado.widget.AbstractTextBox) {
						control.addListener("onValidationStateChange", $scopify(this, this.onEditorStateChange));
						control.addListener("onPost", $scopify(this, this.onEditorPost));
					}
					control.addListener("onPostFailed", $scopify(this, this.onEditorPostFailed));
				}
				
				if (shouldInitControl) {
					var config1 = {}, config2 = {}, attrs = control.ATTRIBUTES;
					this.initEditorConfig(config1);
					for (var attr in config1) {
						if (!attrs[attr] || attrs[attr].writeOnly) continue;
						if (config1[attr] != null) config2[attr] = config1[attr];
					}
					control.set(config2, {
						skipUnknownAttribute: true,
						tryNextOnError: true,
						preventOverwriting: true
					});
				}
				
				this.registerInnerControl(control);
			}
			return control;
		},
		
		getHintControl: function() {
			var control = this._hintControl;
			if (!control) {
				var config = {
					showIconOnly: !this._showHintMessage
				};
				if (this._dataPath) config.dataPath = this._dataPath;
				if (this._dataSet && this._property) {
					config.dataSet = this._dataSet;
					config.property = this._property;
				}
				
				this._hintControl = control = new dorado.widget.DataMessage(config);
			}
			
			if (control && !this._hintControlRegistered) {
				this._hintControlRegistered = true;
				this.registerInnerControl(control);
			}
			return control;
		},
		
		initEditorConfig: function(config) {
			if (this._type == "password") config.password = true;
			if (this._trigger) config.trigger = this._trigger;
			if (this._readOnly) config.readOnly = this._readOnly || this._realReadOnly;
			if (this._entity) config.entity = this._entity;
			if (this._dataPath) config.dataPath = this._dataPath;
			if (this._dataSet && this._property) {
				config.dataSet = this._dataSet;
			}
			config.property = this._property;
		},
		
		resetEditorReadOnly: function() {
			if (this._editor && this._editor instanceof dorado.widget.AbstractEditor) {
				this._editor.set("readOnly", this._readOnly || this._realReadOnly);
			}
		},
		
		onEditorStateChange: function(editor, arg) {
			var hintControl = this.getHintControl();
			if (hintControl) hintControl.set("messages", editor.get("validationMessages"));
		},
		
		onEditorPost: function(editor, arg) {
			var hintControl = this.getHintControl();
			if (hintControl) {
				messages = editor.get("validationMessages");
				hintControl.set("messages", messages || DEFAULT_OK_MESSAGES);
			}
		},
		
		onEditorPostFailed: function(editor, arg) {
			if (!this._dataSet && !this._property) {
				var exception = arg.exception;
				if (exception instanceof dorado.widget.editor.PostException) {
					var hintControl = this.getHintControl();
					if (hintControl) hintControl.set("messages", exception.messages);
				}
			}
			arg.processDefault = false;
			
		},
		
		getBindingPropertyDef: function() {
			var p = this._propertyDef;
			if (!p) {
				var dataSet = this._dataSet, dataPath = dorado.DataPath.create(this._dataPath), dataType;
				if (dataSet) dataType = dataPath.getDataType(dataSet.get("dataType"));
				if (dataType) this._propertyDef = p = dataType.getPropertyDef(this._property);
			}
			return p;
		},
		
		getLabel: function() {
			var label = this._label;
			if (!label && this._dataSet && this._property) {
				var p = this.getBindingPropertyDef();
				if (p) label = p._label || p._name;
			}
			return label || this._property;
		},
		
		isRequired: function() {
			var p;
			if (this._dataSet && this._property) p = this.getBindingPropertyDef();
			var required = p ? p._required : false;
			if (!required) {
				var editor = this.getEditor();
				required = (editor &&
				editor instanceof dorado.widget.TextEditor &&
				editor.get("required"));
			}
			return required;
		},
		
		onProfileChange: function() {
			var formProfile = this._formProfile;
			if (dorado.Object.isInstanceOf(formProfile, dorado.widget.FormProfile)) {
				var attrs = formProfile.ATTRIBUTES, config = {};
				for (var attr in attrs) {
					if (!attrs.hasOwnProperty(attr) || attrs[attr].writeOnly) continue;
					if (specialFormConfigProps.indexOf(attr) >= 0 && formProfile instanceof dorado.widget.Control) {
						continue;
					}
					
					var value = formProfile.get(attr);
					if (value != null) config[attr] = value;
				}
				this.set(config, {
					skipUnknownAttribute: true,
					tryNextOnError: true,
					preventOverwriting: true
				});
			}
		},
		
		refreshDom: function(dom) {
			var height = this._height || this._realHeight;
			$invokeSuper.call(this, arguments);
			
			var dom = this._dom, editorEl = this._editorEl, labelEl = this._labelEl, hintEl = this._hintEl, editor = this.getEditor();
			if (this._showLabel) {
				labelEl.style.display = '';
				labelEl.style.textAlign = this._labelAlign || "left";
				var label = this.getLabel();
				labelEl.innerText = (label || '') + ((this._labelSeparator && label) ? this._labelSeparator : '');
				$fly(labelEl).toggleClass("form-label-required", !!this.isRequired());
			} else if (labelEl) {
				labelEl.style.display = "none";
			}
			
			var labelWidth = 0, labelHeight = 0, editorWidth = 0, hintSize = 0;
			if (this._labelPosition == "top") {
				labelHeight = labelEl.offsetHeight + this._labelSpacing;
				editorEl.style.paddingTop = labelHeight + "px";
				editorWidth = dom.offsetWidth;
				if (height) editorEl.style.height = (dom.offsetHeight - labelHeight) +
				"px";
			} else {
				labelWidth = this._labelWidth + this._labelSpacing;
				editorWidth = dom.offsetWidth - labelWidth;
				if (editorWidth > 0) editorEl.style.width = editorWidth + "px";
			}
			
			if (editor) {
				var config = {};
				if (hintEl) {
					var autoHeight = !editor.ATTRIBUTES.height.independent;
					if (this._hintPosition == "bottom") {
						config.width = dom.offsetWidth - labelWidth;
						if (height && autoHeight) {
							config.height = editorEl.offsetHeight - labelHeight - hintEl.offsetHeight;
						}
					} else {
						if (autoHeight) config.height = editorEl.offsetHeight - labelHeight;
						if (this._editorWidth > 0) {
							config.width = this._editorWidth;
						} else {
							hintSize = this._hintWidth + this._hintSpacing;
							config.width = editorWidth - hintSize;
						}
					}
				} else {
					config.width = dom.offsetWidth - labelWidth;
					config.height = editorEl.offsetHeight;
				}
				if (config.width) editor._realWidth = config.width;
				if (config.height) editor._realHeight = config.height;
				
				if (!editor.get("rendered")) editor.render(editorEl, editorEl.firstChild);
				else editor.refresh();
			}
			
			if (hintEl) {
				var hintControl = this.getHintControl();
				if (this._hintPosition != "bottom") {
					if (this._labelPosition == "top") hintEl.style.top = labelHeight + "px";
					if (this._editorWidth > 0) {
						hintEl.style.width = (editorWidth - this._editorWidth - this._hintSpacing) + "px";
					} else {
						hintEl.style.width = this._hintWidth + "px";
					}
					if (editorEl.clientHeight) hintEl.style.height = editorEl.clientHeight +
					"px";
				}
				if (!hintControl.get("rendered")) hintControl.render(hintEl);
			}
		}
	});
	
	dorado.widget.View.registerDefaultComponent("defaultFormProfile", function() {
		return new dorado.widget.FormProfile();
	});
	
})();
