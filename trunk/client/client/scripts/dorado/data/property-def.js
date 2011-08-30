(function() {

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 实体类型中的属性声明的抽象类。
	 * @abstract
	 * @shortTypeName Default
	 * @extends dorado.AttributeSupport
	 * @extends dorado.EventSupport
	 * @see dorado.EntityDataType
	 * @param {String} name 属性名。
	 * @param {dorado.DataType|dorado.LazyLoadDataType} [dataType] 属性的数据类型。
	 */
	dorado.PropertyDef = $extend([dorado.AttributeSupport, dorado.EventSupport], /** @scope dorado.PropertyDef.prototype */ {
		$className: "dorado.PropertyDef",
		
		ATTRIBUTES: /** @scope dorado.PropertyDef.prototype */ {
		
			/**
			 * 属性名。
			 * @type String
			 * @attribute readOnly
			 */
			name: {
				readOnly: true
			},
			
			/**
			 * 此属性所隶属的实体类型。
			 * @type dorado.EntityDataType
			 * @attribute readOnly
			 */
			parent: {
				readOnly: true
			},
			
			/**
			 * 返回所属的视图。
			 * @type dorado.widget.View
			 * @attribute readOnly
			 */
			view: {
				path: "parent.view"
			},
			
			/**
			 * 数据类型。
			 * @return {dorado.DataType}
			 * @attribute writeOnce
			 */
			dataType: {
				getter: dorado.LazyLoadDataType.dataTypeGetter,
				writeOnce: true
			},
			
			/**
			 * 属性的标签，即用于显示的属性名。
			 * @type String
			 * @attribute
			 */
			label: {
				getter: function() {
					var label = this._label;
					if (label == null) label = this._name;
					return label;
				}
			},
			
			/**
			 * 属性的描述信息。
			 * @type String
			 * @attribute
			 */
			description: {},
			
			/**
			 * 属性是否只读。
			 * @type boolean
			 * @attribute
			 */
			readOnly: {},
			
			/**
			 * 属性是否默认可见。
			 * @type boolean
			 * @attribute
			 * @default true
			 */
			visible: {
				defaultValue: true
			},
			
			/**
			 * 属性的输入格式。
			 * 具体用法请参考{@link dorado.PropertyDef#attribute:displayFormat}
			 * @type String
			 * @attribute
			 */
			typeFormat: {},
			
			/**
			 * 属性的显示格式。
			 * <p>
			 * 此属性对于不同的DataType其定义方法有不同的解释，具体的实现取决对相应DataType的toText方法。
			 * </p>
			 * <p>
			 * 例如：
			 * <li>对于数值型DataType而言，displayFormat的定义与{@link dorado.util.Common.formatFloat}方法的format参数一致。<li>
			 * <li>对于日期时间型DataType而言，displayFormat的定义与{@link Date.formatDate}方法的format参数一致。<li>
			 * </p>
			 * <p>
			 * 当用户传入的displayFormat字符串以call:开头时，dorado会将call:后面的部分识别为一个全局Function的名字，
			 * 如果系统中确实存在一个这样的Function，那么dorado会直接调用该Function并以其返回值作为转换后的格式。
			 * </p>
			 * @type String
			 * @attribute
			 */
			displayFormat: {},
			
			/**
			 * 一组用于定义改变属性显示方式的"代码"/"名称"键值对。
			 * @type Object[]
			 * @attribute
			 *
			 * @example
			 * // 例如对于一个以逻辑型表示性别的属性，我们可能希望在显示属性值时将true显示为"男"、将false显示为"女"。
			 * propertyDef.set("mapping", [
			 * 	{
			 * 		key : "true",
			 * 		value : "男"
			 * 	},
			 * 	{
			 * 		key : "false",
			 * 		value : "女"
			 * 	}
			 * ]);
			 */
			mapping: {
				setter: function(mapping) {
					this._mapping = mapping;
					if (mapping && mapping.length > 0) {
						var index = this._mappingIndex = {};
						for (var i = 0; i < mapping.length; i++) {
							var key = mapping[i].key;
							if (key == null) key = "${null}";
							else if (key === '') key = "${empty}";
							index[key + ''] = mapping[i].value;
						}
					} else {
						delete this._mappingIndex;
					}
					delete this._mappingRevIndex;
				}
			},
			
			/**
			 * 是否允许向此属性设置mapping的键值中未声明的值。默认为true，即不允许设置mapping的键值中未声明的值。
			 * <p>
			 * 注意：此属性只在mapping属性确实有值时才有效。
			 * </p>
			 * @type boolean
			 * @attribute
			 * @default true
			 */
			acceptUnknownMapKey: {
				defaultValue: true
			},
			
			/**
			 * 该属性中的内容默认情况是否需要向服务端提交。
			 * @type boolean
			 * @attribute
			 */
			submittable: {
				defaultValue: true
			},
			
			/**
			 * 是否非空。
			 * @type boolean
			 * @attribute
			 */
			required: {},
			
			/**
			 * 默认值。
			 * @type Object
			 * @attribute
			 */
			defaultValue: {},
			
			/**
			 * 数据校验器的数组。
			 * <p>
			 * 此处数组中可放置两种类型的校验器定义：
			 * 	<ul>
			 * 	<li>直接放入一个校验器的实例对象。</li>
			 * 	<li>放入含校验器信息的JSON对象。<br>
			 * 此时可以使用子控件类型名称中"dorado."和"Validator"之间的部分作为$type的简写。
			 * 	</li>
			 * 	<li>直接放入一个字符串代表$type的简写。</li>
			 * 	</ul>
			 * </p>
			 * @type dorado.validator.Validator[]|Object[]|String[]
			 * @attribute
			 * @see dorado.validator.Validator
			 * @see dorado.Toolkits.createInstance
			 */
			validators: {
				setter: function(value) {
					var validators = [];
					for (var i = 0; i < value.length; i++) {
						var v = value[i];
						if (!(v instanceof dorado.validator.Validator)) {
							v = dorado.Toolkits.createInstance("validator", v);
						}
						if (v._propertyDef) {
							throw new dorado.Exception("Validator alreay belongs to another PropertyDef \"" + v._propertyDef._name + "\"."); 
						}
						v._propertyDef = this;
						validators.push(v);
					}
					this._validators = validators;
				}
			},
			
			/**
			 * 隶属的数据类型的管理器。
			 * @type dorado.DataTypeRepository
			 * @attribute readOnly
			 */
			dataTypeRepository: {
				getter: function(attr) {
					var parent = this.get("parent");
					return (parent) ? parent.get(attr) : null;
				},
				readOnly: true
			},
			
			/**
			 * 是否缓存数据提供者返回的数据。
			 * @type boolean
			 * @attribute
			 */
			cacheable: {}
		},
		
		EVENTS: /** @scope dorado.PropertyDef.prototype */ {
			/**
			 * 当外界尝试从某数据实体中读取此属性的值时触发的事件。
			 * @param {Object} self 事件的发起者，即本属性声明对象。
			 * @param {Object} arg 事件参数。
			 * @param {dorado.Entity} arg.entity 当前读取的数据实体。
			 * @param {Object} #arg.value 默认将要返回给外界的属性值。
			 * 同时如果您希望改变此实体属性提供给外界的值也可以直接修改此子属性。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @see dorado.Entity#get
			 * @event
			 */
			onGet: {},
			
			/**
			 * 当外界尝试从某数据实体中读取此属性的文本值时触发的事件。
			 * @param {Object} self 事件的发起者，即本属性声明对象。
			 * @param {Object} arg 事件参数。
			 * @param {dorado.Entity} arg.entity 当前读取的数据实体。
			 * @param {String} #arg.text 默认将要返回给外界的属性文本值。
			 * 同时如果您希望改变此实体属性提供给外界的文本值也可以直接修改此子属性。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @see dorado.Entity#getText
			 * @event
			 */
			onGetText: {},
			
			/**
			 * 当外界尝试向某数据实体的此属性中写入一个值时触发的事件。
			 * @param {Object} self 事件的发起者，即本属性声明对象。
			 * @param {Object} arg 事件参数。
			 * @param {dorado.Entity} arg.entity 当前设置的数据实体。
			 * @param {Object} #arg.value 默认将要写入该属性的值。
			 * 同时如果您希望改变实际写入值也可以直接修改此子属性。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @see dorado.Entity#set
			 * @event
			 */
			onSet: {}
		},
		
		constructor: function(name, dataType) {
			$invokeSuper.call(this, arguments);
			if (name) {
				if (name.constructor == String) {
					this._name = name;
					this._dataType = dataType;
				} else {
					this._name = name.name;
					delete name.name;
					this.set(name);
				}
			}
		},
		
		getListenerScope: function() {
			return this.get("view");
		},
		
		getDataType: function(loadMode) {
			return dorado.LazyLoadDataType.dataTypeGetter.call(this, loadMode);
		},
		
		/**
		 * 将给定的数值翻译成显示值。
		 * @param {String} key 要翻译的键值。
		 * @return {Object} 显示值。
		 * @see dorado.PropertyDef#attribute:mapping
		 */
		getMappedValue: function(key) {
			if (key == null) key = "${null}";
			else if (key === '') key = "${empty}";
			return this._mappingIndex ? this._mappingIndex[key + ''] : undefined;
		},
		
		/**
		 * 根据给定的显示值返回与其匹配的键值。
		 * @param {Object} value 要翻译的显示值。
		 * @return {String} 键值。
		 * @see dorado.PropertyDef#attribute:mapping
		 */
		getMappedKey: function(value) {
			if (!this._mappingRevIndex) {
				var index = this._mappingRevIndex = {}, mapping = this._mapping;
				for (var i = 0; i < mapping.length; i++) {
					var v = mapping[i].value;
					if (v == null) v = "${null}";
					else if (v === '') v = "${empty}";
					index[v + ''] = mapping[i].key;
				}
			}
			if (value == null) value = "${null}";
			else if (value === '') value = "${empty}";
			return this._mappingRevIndex[value + ''];
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 实体类型中的普通属性声明。
	 * @shortTypeName PropertyDef
	 * @extends dorado.PropertyDef
	 * @see dorado.EntityDataType
	 */
	dorado.BasePropertyDef = $extend(dorado.PropertyDef, /** @scope dorado.BasePropertyDef.prototype */ {
		$className: "dorado.BasePropertyDef",
		
		ATTRIBUTES: /** @scope dorado.BasePropertyDef.prototype */ {
			/**
			 * 是否键值属性。
			 * @type boolean
			 * @attribute
			 */
			key: {}
		}
	});
	
	dorado.ReferenceDataPipe = $extend(dorado.DataProviderPipe, {
		$className: "dorado.ReferenceDataPipe",
		
		constructor: function(propertyDef, entity) {
			this.propertyDef = propertyDef;
			this.entity = entity;
			this.dataType = propertyDef._dataType;
			var parent = propertyDef.get("parent");
			this.dataTypeRepository = (parent ? parent.get("dataTypeRepository") : null) || $dataTypeRepository;
			this.view = this.dataTypeRepository ? this.dataTypeRepository.view : null;
		},
		
		getDataProviderArg: function() {
			dorado.$this = this.entity;
			return {
				pageSize: this.propertyDef._pageSize,
				parameter: dorado.JSON.evaluate(this.propertyDef._parameter),
				dataType: this.dataType,
				view: this.view
			};
		},
		
		getDataProvider: function() {
			return this.propertyDef._dataProvider;
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 实体类型中的数据关联属性。数据关联属性常常用来实现较大量数据的懒装载。
	 * @shortTypeName Refernce
	 * @extends dorado.PropertyDef
	 * @see dorado.EntityDataType
	 */
	dorado.Reference = $extend(dorado.PropertyDef, /** @scope dorado.Reference.prototype */ {
		$className: "dorado.Reference",
		
		ATTRIBUTES: /** @scope dorado.Reference.prototype */ {
		
			/**
			 * 用于为数据关联属性提供数据的数据提供者。
			 * @type dorado.DataProvider
			 * @attribute
			 */
			dataProvider: {},
			
			/**
			 * 提取数据时使用的参数。 即调用{@link dorado.DataProvider}时使用的参数。此处允许使用JSON数据模板。
			 * @type Object
			 * @attribute
			 * @see dorado.JSON
			 */
			parameter: {
				setter: function(parameter) {
					if (this._parameter instanceof dorado.util.Map && parameter instanceof dorado.util.Map) {
						this._parameter.put(parameter);
					} else {
						this._parameter = parameter;
					}
				}
			},
			
			/**
			 * 提取数据时使用的分页大小。即调用{@link dorado.DataProvider}时使用的分页大小。
			 * @type int
			 * @attribute
			 */
			pageSize: {},
			
			/**
			 * 是否缓存数据提供者返回的数据。
			 * @type boolean
			 * @attribute
			 * @default true
			 */
			cacheable: {
				defaultValue: true
			},
			
			/**
			 * 对于新增的数据实体是否有效，及是否为新增的数据实体装载此属性中的子数据。
			 * @type boolean
			 * @attribute
			 */
			activeOnNewEntity: {}
		},
		
		EVENTS: /** @scope dorado.Reference.prototype */ {
			/**
			 * 当系统尝试为某数据实体的此属性装载数据之前触发的事件。
			 * @param {Object} self 事件的发起者，即本属性声明对象。
			 * @param {Object} arg 事件参数。
			 * @param {dorado.Entity} arg.entity 当前处理的数据实体。
			 * @param {Object} #arg.data 如果您为此子属性设置了一个非undefined的值，那么系统将跳过默认的数据装载动作并直接将此处的值视作装载到的数据。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeLoadData: {},
			
			/**
			 * 当系统为某数据实体的此属性装载数据之后触发的事件。
			 * @param {Object} self 事件的发起者，即本属性声明对象。
			 * @param {Object} arg 事件参数。
			 * @param {dorado.Entity} arg.entity 当前处理的数据实体。
			 * @param {Object} #arg.data 装载到的数据。如果您为此子属性设置了值，那么系统会将此处的值视作装载到的数据。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onLoadData: {}
		},
		
		/**
		 * 获取为数据关联属性提供数据的数据管道。
		 * <p>
		 * 该数据管道事实上是对{@link dorado.DataProvider}的包装，目的是为了简化和统一数据获取的方法。
		 * </p>
		 * @param entity {dorado.Entity} 需要使用该数据管道的数据实体。
		 * @return {dorado.DataPipe} 数据管道。
		 */
		getDataPipe: function(entity) {
			return new dorado.ReferenceDataPipe(this, entity);
		}
	});
	
	var lookupIndexs = {};
	
	dorado.LookupDataPipe = $extend(dorado.DataPipe, {
		$className: "dorado.LookupDataPipe",
		
		constructor: function(propertyDef, entity) {
			this.propertyDef = propertyDef;
			this.entity = entity;
			this.dataType = propertyDef._dataType;
			var parent = propertyDef.get("parent");
			this.dataTypeRepository = (parent ? parent.get("dataTypeRepository") : null) || $dataTypeRepository;
		},
		
		getLookupData: function() {
			var data = this.propertyDef.lookupData;
			if (!data) {
				var propDef = this.propertyDef;
				if (propDef._dataSet) {
					data = propDef._dataSet.getData();
				} else if (propDef._dataProvider) {
					data = propDef._dataProvider.getResult();
				}
			}
			return this.propertyDef.lookupData = data;
		},
		
		getLookupDataAsync: function(callback) {
			if (!this.propertyDef.lookupData) {
				var propDef = this.propertyDef;
				var callbackObj = {
					scope: this,
					callback: function(success, result) {
						if (success) this.propertyDef.lookupData = result;
						$callback(callback, success, result);
					}
				};
				
				if (propDef._dataSet) {
					propDef._dataSet.getDataAsync(callbackObj);
				} else if (propDef._dataProvider) {
					propDef._dataProvider.getResultAsync(callbackObj);
				}
			} else {
				$callback(callback, true, this.propertyDef.lookupData);
			}
		},
		
		getIndex: function() {
		
			function getKey(entity) {
				var constraints = propDef._constraints;
				var keys = [];
				for (var i = 0; i < constraints.length; i++) {
					var constraint = constraints[i];
					var path = dorado.DataPath.create(constraint.lookupKeyProperty);
					keys.push(path.evaluate(entity));
				}
				return keys.join("^^");
			}
			
			var propDef = this.propertyDef;
			if (!propDef.cacheKey) {
				propDef.cacheKey = (propDef._dataSet) ? propDef._dataSet.id : (propDef._dataProvider) ? ("dp:" + propDef._dataProvider.name) : null;
				if (propDef.cacheKey) {
					propDef.cacheKey += '|' + propDef._lookupProperty + '|';
					var constraints = propDef._constraints;
					for (var i = 0; i < constraints.length; i++) {
						var constraint = constraints[i];
						propDef.cacheKey += ',' + constraint.lookupKeyProperty;
					}
				}
			}
			var index = lookupIndexs[propDef.cacheKey];
			
			if (!index) {
				lookupIndexs[propDef.cacheKey] = index = {};
				
				var data = this.propertyDef.lookupData;
				if (data) {
					var path = (propDef._lookupProperty) ? dorado.DataPath.create(propDef._lookupProperty) : null;
					if (data instanceof Array) {
						for (var i = 0; i < data.length; i++) {
							var entity = data[i];
							index[getKey.call(this, entity)] = (path) ? path.evaluate(entity, true) : entity;
						}
					} else if (data instanceof dorado.EntityList) {
						for (var it = data.iterator(); it.hasNext();) {
							var entity = it.next();
							index[getKey.call(this, entity)] = (path) ? path.evaluate(entity, true) : entity;
						}
					} else {
						index[getKey.call(this, data)] = (path) ? path.evaluate(data, true) : data;
					}
				}
			}
			return index;
		},
		
		getKey: function(entity) {
			dorado.$this = entity;
			var constraints = this.propertyDef._constraints;
			var keys = [];
			for (var i = 0; i < constraints.length; i++) {
				var constraint = constraints[i];
				if (constraint.keyProperty) {
					/*
					 * keys.push((entity instanceof dorado.Entity) ? entity.get(constraint.keyProperty) : entity[constraint.keyProperty]);
					 */
					keys.push(entity.get(constraint.keyProperty));
				} else {
					keys.push(dorado.JSON.evaluate(constraint.keyValue));
				}
			}
			return keys.join("^^");
		},
		
		doGet: function() {
			this.getLookupData();
			var key = this.getKey(this.entity);
			return this.getIndex()[key ? key : ''];
		},
		
		doGetAsync: function(callback) {
			this.getLookupDataAsync({
				scope: this,
				callback: function(success, result) {
					if (success) {
						var key = this.getKey(this.entity);
						var result = this.getIndex()[key ? key : ''];
					}
					$callback(callback, success, result);
				}
			});
		}
	});
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 实体类型中的数据参照属性。
	 * <p>
	 * 数据参照属性可根据一定的匹配规则，从一组给定的参照数据对象中查找到第一个匹配对象， 并从该对象中提取出所需的数据作为属性值。
	 * </p>
	 * <p>
	 * 例如我们设置的参照数据(通过dataSet属性指定)如下：
	 * <pre class="symbol-example code">
	 * <code class="javascript">
	 * [ {
	 * 	id : "01",
	 * 	name : "Dept1"
	 * }, {
	 * 	id : "02",
	 * 	name : "Dept2"
	 * }, {
	 * 	id : "03",
	 * 	name : "Dept3"
	 * }, {
	 * 	id : "04",
	 * 	name : "Dept4"
	 * } ]
	 * </code>
	 * </pre>
	 *
	 * 假设我们设置的参照的约束条件(即constraints属性)为:
	 * <pre class="symbol-example code">
	 * <code class="javascript">
	 * [ {
	 * 	keyProperty : "id",
	 * 	value : "03"
	 * } ]
	 * </code>
	 * </pre>
	 *
	 * 那么参照规则将定位到
	 * <pre class="symbol-example code">
	 * <code class="javascript">
	 * {
	 * 	id : "03",
	 * 	name : "Dept3"
	 * }
	 * </code>
	 * </pre>
	 *
	 * 对象。 如果没有设定lookupProperty属性，那么上述对象就将被作为参照属性的值；
	 * 如果我们设定了lookupProperty属性为"name"，那么参照属性的值将是"Dept3"。
	 * </p>
	 * @shortTypeName Lookup
	 * @extends dorado.PropertyDef
	 * @see dorado.EntityDataType
	 */
	dorado.Lookup = $extend(dorado.PropertyDef, /** @scope dorado.Lookup.prototype */ {
		$className: "dorado.Lookup",
		
		ATTRIBUTES: /** @scope dorado.Lookup.prototype */ {
		
			/**
			 * 用于提供参照数据的数据提供器。
			 * @type dorado.DataProvider
			 * @attribute
			 */
			dataProvider: {},
			
			/**
			 * 用于管理参照数据的数据集。
			 * <p>
			 * 参照属性的dataProvider和dataSet这两个属性具有相似的作用，我们只需要定义其中的一个。
			 * 如果同时定义了这两个属性，那么dataProvider属性将被忽略。
			 * </p>
			 * @type dorado.widget.DataSet
			 * @attribute
			 */
			dataSet: {},
			
			/**
			 * 表示提取被参照数据对象中的哪个属性的值作为此参照属性的值。
			 * @type String
			 * @attribute
			 */
			lookupProperty: {},
			
			/**
			 * 数据参照的约束条件数组。
			 * <p>
			 * 该数组中的每一个元素都是一个约束条件对象，该对象包含以下两个子属性：
			 * <ul>
			 * <li>lookupKeyProperty - {String} 用于进行匹配运算的参照数据对象中的属性名。</li>
			 * <li>keyProperty - {String} 用户提取匹配值的，本数据对象中属性名。</li>
			 * <li>value - {Object} 匹配值，此处允许使用JSON数据模板。
			 * 此属性与property只能选择其一定义，如果同时定义了这两个属性，value属性将被忽略。</li>
			 * </ul>
			 * </p>
			 * @type Object[]
			 * @attribute
			 * @see dorado.JSON
			 */
			constraints: {}
		},
		
		constructor: function(name, dataType) {
			this._constraints = [];
			$invokeSuper.call(this, arguments);
		},
		
		/**
		 * 获取为数据参照属性提供数据的数据管道。
		 * @param entity {dorado.Entity} 需要使用该数据管道的数据实体。
		 * @return {dorado.DataPipe} 数据管道。
		 */
		getDataPipe: function(entity) {
			return new dorado.LookupDataPipe(this, entity);
		},
		
		/**
		 * 清除dorado内部为了提高参照效率而建立的缓存。
		 */
		clearCache: function() {
			delete this.cacheKey;
		}
	});
	
	dorado.Toolkits.registerPrototype("propertydef", {
		"Default": dorado.BasePropertyDef,
		"Reference": dorado.Reference,
		"Lookup": dorado.Lookup
	});
	
})();
