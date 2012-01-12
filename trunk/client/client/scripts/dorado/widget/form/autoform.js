/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @name dorado.widget.autoform
 * @namespace 自动表单所使用的一些相关类的命名空间。
 */
dorado.widget.autoform = {};

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 自动表单中的表单元素。
 * <p>此控件一般不应被单独使用，而应该与AutoForm控件配合使用。</p>
 * @extends dorado.widget.FormElement
 * @see dorado.widget.AutoForm
 */
dorado.widget.autoform.AutoFormElement = $extend(dorado.widget.FormElement, /** @scope dorado.widget.autoform.AutoFormElement.prototype */ {
	$className: "dorado.widget.autoform.AutoFormElement",
	
	ATTRIBUTES: /** @scope dorado.widget.autoform.AutoFormElement.prototype */ {
	
		width: {
			independent: false
		},
		
		/**
		 * 表单元素的名称。
		 * @type String
		 * @attribute writeOnce
		 */
		name: {
			writeOnce: true,
			setter: function(v) {
				this._name = v;
				if (!this.getAttributeWatcher().getWritingTimes("property")) this._property = v;
			}
		}
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 自动表单。
 * @component Form
 * <p>
 * AutoForm的get方法在{@link dorado.AttributeSupport#get}的基础上做了增强。
 * 除了原有的读取属性值的功能之外，此方法还另外提供了下面的用法。
 * <ul>
 * 	<li>当传入一个以&开头的字符串时，@后面的内容将被识别成AutoFormElement的名称，表示根据名称获取AutoFormElement。参考{@link dorado.widget.AutoForm#getElement}。</li>
 * </ul>
 * </p>
 * @extends dorado.widget.Control
 * @extends dorado.widget.FormProfile
 */
dorado.widget.AutoForm = $extend([dorado.widget.Control, dorado.widget.FormProfile], /** @scope dorado.widget.AutoForm.prototype */ {
	$className: "dorado.widget.AutoForm",
	
	ATTRIBUTES: /** @scope dorado.widget.AutoForm.prototype */ {
	
		className: {
			defaultValue: "d-auto-form"
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
					this.onProfileChange();
				}
			}
		},
		
		/**
		 * 表单布局的分栏方式。
		 * @type String
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:cols
		 */
		cols: {
			skipRefresh: true,
			setter: function(cols) {
				this._cols = cols;
				if (this._rendered) this.refreshFormLayout();
			}
		},
		
		/**
		 * 默认的行高。
		 * @type int
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:rowHeight
		 */
		rowHeight: {},
		
		/**
		 * 表单区域之间的留白大小。像素值。
		 * @type int
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:cellSpacing
		 */
		cellSpacing: {},
		
		/**
		 * 表单区域之间的留白大小。像素值。
		 * @type int
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:cellPadding
		 */
		cellPadding: {},
		
		/**
		 * 是否将表单的宽度自动扩展为撑满容器。
		 * @type boolean
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:stretchWidth
		 */
		stretchWidth: {},
		
		/**
		 * 表单四周的留白大小。像素值。
		 * @type int
		 * @attribute
		 * @see dorado.widget.layout.FormLayout#attribute:padding
		 */
		padding: {},
		
		/**
		 * 此属性只影响自动创建表单元素的功能，即我们可以指定一个实体数据类型让表单根据此数据类型自动创建其中的表单元素。
		 * @type dorado.data.EntityDataType
		 * @attribute
		 * @see dorado.widget.AutoForm#attribute:autoCreateElements
		 */
		dataType: {
			getter: dorado.LazyLoadDataType.dataTypeGetter
		},
		
		/**
		 * 是否自动根据绑定的EntityDataType自动创建其中的表单元素。
		 * @type boolean
		 * @attribute
		 */
		autoCreateElements: {},
		
		/**
		 * 表单元素的集合。
		 * <p>
		 * 此属性在读取和写入时的数据类型是不同的。
		 * <ul>
		 * <li>在写入时应该传入一个表单元素数组或包含表单元素配置信息的数组。</li>
		 * <li>在读取时此方法返回一个包含所有表单元素的{@link dorado.util.KeyedArray}集合。</li>
		 * <li></li>
		 * </ul>
		 * </p>
		 * @type Control[]|dorado.util.KeyedArray
		 * @attribute skipRefresh
		 */
		elements: {
			skipRefresh: true,
			setter: function(elements) {
				if (this._rendered) {
					var container = this._container, layout;
					if (container) {
						layout = container.get("layout");
						layout.disableRendering();
					}
					try {
						if (container) {
							this._elements.each(function(element) {
								container.removeChild(element);
							});
						}
						this._elements.clear();
						
						if (!elements) return;
						for (var i = 0; i < elements.length; i++) {
							this.addElement(elements[i]);
						}
					}
					finally {
						if (container) {
							layout.enableRendering();
							container.refresh(true);
						}
					}
				} else {
					this._elementConfigs = elements;
				}
			}
		},
		
		/**
		 * 是否要自动创建一个私有的Entity，否则AutoForm将尝试使用关联的FormProfile中的Entity。
		 * @boolean
		 * @attribute
		 * @default value
		 */
		createOwnEntity: {
			defaultValue: true
		}
	},
	
	constructor: function() {
		this._elements = new dorado.util.KeyedArray(function(element) {
			return (element instanceof dorado.widget.autoform.AutoFormElement) ? element._name : element._id;
		});
		this._container = new dorado.widget.Container({
			layout: "Form",
			contentOverflow: "visible"
		});
		this.registerInnerControl(this._container);
		this._bindingElements = new dorado.ObjectGroup();
		
		this._skipOnCreateListeners = (this._skipOnCreateListeners || 0) + 1;
		$invokeSuper.call(this, arguments);
		this._skipOnCreateListeners --;

		if (this._createOwnEntity && this.getAttributeWatcher().getWritingTimes("entity") == 0) {
			this.set("entity", new dorado.widget.FormProfile.DefaultEntity());
		}
		
		if (this._elementConfigs) {
			var configs = this._elementConfigs;
			for (var i = 0; i < configs.length; i++) {
				this.addElement(configs[i]);
			}
			delete this._elementConfigs;
		}
		
		this.addListener("onAttributeChange", function(self, arg) {
			var attr = arg.attribute;
			if (attr == "readOnly") {
				var readOnly = self._readOnly, objects = self._bindingElements.objects;
				for (var i = 0; i < objects.length; i++) {
					var object = objects[i];
					if (object instanceof dorado.widget.FormElement) {
						object._realReadOnly = readOnly;
						object.resetEditorReadOnly();
					}
				}
			} else if (!dorado.widget.Control.prototype.ATTRIBUTES[attr] && dorado.widget.FormConfig.prototype.ATTRIBUTES[attr]) {
				dorado.Toolkits.setDelayedAction(self, "$profileChangeTimerId", function() {
					self._bindingElements.invoke("onProfileChange");
				}, 20);
			}
		});
		
		if (!(this._skipOnCreateListeners > 0) && this.getListenerCount("onCreate")) {
			this.fireEvent("onCreate", this);
		}
	},
	
	doGet: function(attr) {
		var c = attr.charAt(0);
		if (c == '&') {
			var elementName = attr.substring(1);
			return this.getElement(elementName);
		} else {
			return $invokeSuper.call(this, [attr]);
		}
	},
	
	/**
	 * 添加一个表单元素。
	 * @param {Object|dorado.widget.Control} element 表单元素或可用于创建表单元素的JSON对象。
	 * @return {dorado.widget.Control} 新添加的表单元素。
	 */
	addElement: function(element) {
		var elements = this._elements, config = {}, constraint;
		if (!config.name) {
			var name = config.property || "_unnamed";
			if (elements.get(name)) {
				var j = 2;
				while (elements.get(name + '_' + j)) {
					j++;
				}
				name = name + '_' + j;
			}
			config.name = name;
		}
		
		if (!(element instanceof dorado.widget.Control)) {
			dorado.Object.apply(config, element);
			if (element) {
				constraint = element._layoutConstraint;
				element = dorado.Toolkits.createInstance("widget", config, function(type) {
					if (!type) return dorado.widget.autoform.AutoFormElement;
				});
			} else {
				element = new dorado.widget.Control(config);
			}
		}
		element.set("formProfile", this, {
			skipUnknownAttribute: true,
			tryNextOnError: true,
			preventOverwriting: true,
			lockWritingTimes: true
		});
		elements.append(element);
		
		if (this._container) this._container.addChild(element);
		return element;
	},
	
	/**
	 * 移除一个的表单元素。
	 * @param {dorado.widget.Control} element 移除的表单元素。
	 */
	removeElement: function(element) {
		this._elements.remove(element);
		if (this._container) this._container.removeChild(element);
	},
	
	/**
	 * 根据名称或序号获得一个的表单元素。
	 * @param {String|int} name 表单元素的名称或序号（自0开始）。
	 * @return {dorado.widget.Control} 表单元素。
	 */
	getElement: function(name) {
		return this._elements.get(name);
	},
	
	createDom: function() {
		if (!this._formProfile) {
			var view = this.get("view") || dorado.widget.View.TOP;
			this.set("formProfile", view.id("defaultFormProfile"));
		}
		return $invokeSuper.call(this, arguments);
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		var container = this._container;
		if (!container._rendered) {
			if (this._autoCreateElements && !this._defaultElementsGenerated) {
				this.generateDefaultElements();
			}
			this.initLayout(container.get("layout"));
			container.render(dom);
		}
	},
	
	doOnResize: function() {
		var dom = this.getDom(), container = this._container;
		// container._realWidth = dom.offsetWidth;
		// container._realHeight = dom.offsetHeight;
		container.resetDimension();
	},
	
	refreshFormLayout: function() {
		var container = this._container, layout = container.get("layout");
		container.refresh();
		this.initLayout(layout);
		layout.refresh();
	},
	
	initLayout: function(layout) {
		configs = {};
		if (this._cols) configs.cols = this._cols;
		if (this._rowHeight) configs.rowHeight = this._rowHeight;
		if (this._cellSpacing) configs.cellSpacing = this._cellSpacing;
		if (this._cellPadding) configs.cellPadding = this._cellPadding;
		if (this._stretchWidth) configs.stretchWidth = this._stretchWidth;
		if (this._padding) configs.padding = this._padding;
		layout.set(configs);
	},
	
	generateDefaultElements: function() {
		var dataType = this.get("dataType");
		if (!dataType && this._dataSet) {
			var dataPath = dorado.DataPath.create(this._dataPath);
			dataType = dataPath.getDataType(this._dataSet.get("dataType"));
		}
		if (!dataType && this._entity) {
			dataType = this._entity.dataType;
		}
		
		if (dataType && dataType instanceof dorado.EntityDataType) {
			this._defaultElementsGenerated = true;
			
			var container = this._container, layout;
			if (container) {
				layout = container.get("layout");
				layout.disableRendering();
			}
			var self = this, elements = self._elements, config;
			dataType.get("propertyDefs").each(function(propertyDef) {
				if (!propertyDef._visible) return;
				
				var name = propertyDef._name, element = elements.get(name);
				if (!element) {
					config = {
						name: name,
						property: name
					};
				} else {
					config = {
						property: name
					};
					self.removeElement(element);
					self.addElement(element);
				}
				
				var propertyDataType = propertyDef.get("dataType");
				if (propertyDataType instanceof dorado.EntityDataType || propertyDataType instanceof dorado.AggregationDataType) {
					return;
				}
				
				if (!element) element = self.addElement(config);
				else element.set(config, {
					skipUnknownAttribute: true,
					tryNextOnError: true,
					preventOverwriting: true,
					lockWritingTimes: true
				});
				
				if (element instanceof dorado.widget.FormElement && !this._dataSet && propertyDef._readOnly) {
					element._realReadOnly = true;
				}
			});
			if (container) {
				layout.enableRendering();
				container.refresh(true);
			}
		}
	},
	
	/**
	 * 验证表单中所有编辑器中的数据。
	 * @return {boolean} 返回本次验证结果是否全部正确。
	 */
	validate: function() {
		var result = true, elements = this._elements;
		this._elements.each(function(element) {
			if (element instanceof dorado.widget.FormElement) {
				var editor = element.get("editor");
				if (editor && editor instanceof dorado.widget.AbstractTextBox) {
					if (editor.get("validateState") == "none") {
						editor.post();
					}
					if (result && editor.get("validateState") == "invalid") result = false;
				}
			}
		});
		return result;
	},
	
	/**
	 * 刷新其中编辑器中的数据。
	 * <p>
	 * 该方法通常只对那么未通过DataSet建立数据绑定的使用场景有效。
	 * 例如我们将一个AutoForm与一个数据实体进行了数据关联，当数据实体中的属性值发生变化时AutoForm并不会自动刷新。
	 * 此时我们需要调用refreshData()方法，手工的通知AutoForm进行数据刷新。
	 * </p>
	 */
	refreshData: function() {
		this._elements.each(function(element) {
			if (element instanceof dorado.widget.FormElement) {
				element.refreshData();
			}
		});
	},
	
	getFocusableSubControls: function() {
		return [this._container];
	}
});
