function open_flash_chart_data() {
	return JSON.stringify({"bg_colour": "#ffffff", elements: [{"type": "pie"}]});
}

(function() {
	/**
	 * @name dorado.widget.ofc
	 * @namespace OpenFlashChart的命名空间。
	 */
	dorado.widget.ofc = {};

	var ofc_id_seed = 1;

	function isPlainObject(o) {
		return o && Object.prototype.toString.call(o) === '[object Object]' && ('isPrototypeOf' in o);
	}

	function toJSON() {
		var object = this, result = {}, ATTRIBUTES = object.ATTRIBUTES;
		for (var attr in ATTRIBUTES) {
			var attrConfig = ATTRIBUTES[attr], jsonAttr = attrConfig.jsonProperty || attr;
			if (attrConfig.jsonable) {
				var value = object["_" + attr];
				if (value !== undefined) {
					if (dorado.Object.isInstanceOf(value, dorado.AttributeSupport)) {
						result[jsonAttr] = value.toJSON();
					} else {
						result[jsonAttr] = value;
					}
				}
			}
		}

		return result;
	}

	/**
	 * @class dorado.widget.ofc.JSONizable
	 * @extends dorado.AttributeSupport
	 */
	dorado.widget.ofc.JSONizable = $extend(dorado.AttributeSupport, {
		$className: "dorado.widget.ofc.JSONizable",
		constructor: function(config) {
			$invokeSuper.call(this, arguments);
			if (config) this.set(config);
		},
		toJSON: toJSON
	});

	/**
	 * @class Text
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.Text = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.Text.prototype */{
		$className: "dorado.widget.ofc.Text",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Text.prototype */{
			/**
			 * 文字的样式。可以使用标注的css，例如：
			 * {font-size:18px; padding: 5px; text-align: left; font-family: Century Gothic; font-weight: bold;}
			 * @attribute
			 * @type String
			 */
			style: {
				jsonable: true,
				defaultValue: ""
			},
			/**
			 * 要显示的文字。
			 * @attribute
			 * @type String
			 */
			text: {
				jsonable: true
			}
		}
	});

	/**
	 * @class Key
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.Key = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.Key.prototype */{
		$className: "dorado.widget.ofc.Key",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Key.prototype */{
			/**
			 * 字体颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},
			/**
			 * 字体大小。
			 * @attribute
			 * @type Number
			 */
			fontSize: {
				jsonable: true,
				jsonProperty: "font-size"
			},
			/**
			 * 文字。
			 * @attribute
			 * @type String
			 */
			text: {
				jsonable: true
			}
		}
	});

	var mouseMap = {Closest: 0,Proximity: 1,Normal: 2};

	/**
	 * @class ToolTip
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.ToolTip = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.ToolTip.prototype */{
		$className: "dorado.widget.ofc.ToolTip",
		ATTRIBUTES: /** @scope dorado.widget.ofc.ToolTip.prototype */ {
			/**
			 * 背景色。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "bg_colour"
			},

			/**
			 * 字体颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 标题的样式，可以使用标准的css。
			 * @attribute
			 * @type String
			 */
			titleStyle: {
				jsonable: true,
				jsonProperty: "title_style"
			},

			/**
			 * 内容的样式，可以使用标准的css。
			 * @attribute
			 * @type String
			 */
			bodyStyle: {
				jsonable: true,
				jsonProperty: "body_style"
			},

			/**
			 * 显示在鼠标的什么位置，可选值Closest,Proximity,Normal。
			 * @attribute
			 * @type Number
			 */
			mouse: {
				jsonable: true
			},

			/**
			 * 是否显示阴影。
			 * @attribute
			 * @type boolean
			 */
			shadow: {
				jsonable: true
			},

			/**
			 * 显示的边框的宽度。
			 * @attribute
			 * @type Number
			 */
			stroke: {
				jsonable: true
			},

			/**
			 * 显示的圆角边框的弯曲大小。
			 * @attribute
			 * @type Number
			 */
			rounded: {
				jsonable: true
			}
		},
		toJSON: function() {
			var json = toJSON.call(this, arguments);
			if (this._mouse) {
				json.mouse = mouseMap[this._mouse];
			}
			return json;
		}
	});

	/**
	 * @class Legend
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.Legend = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.Legend.prototype */{
		$className: "dorado.widget.ofc.Legend",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Legend.prototype */ {
			/**
			 * 图例的透明度，取值范围0到1(0为不可见，1为不透明)，只对position为right的图例起作用。
			 * @attribute
			 * @type Number
			 */
			alpha: {
				jsonable: true
			},

			/**
			 * 图例的背景色，只对position为right的图例起作用。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "bg_colour"
			},

			/**
			 * 是否显示图例的边框，只对position为right的图例起作用。
			 * @attribute
			 * @type boolean
			 */
			border: {
				jsonable: true
			},

			/**
			 * 图例边框的颜色，只对position为right的图例起作用。
			 * @attribute
			 * @type String
			 */
			borderColor: {
				jsonable: true
			},

			/**
			 * 图例边框的宽度，只对position为right的图例起作用。
			 * @attribute
			 * @type Number
			 */
			stroke: {
				jsonable: true
			},

			/**
			 * 图例的外边距，以像素为单位，只对position为right的图例起作用。
			 * @attribute
			 * @type Number
			 */
			margin: {
				jsonable: true
			},

			/**
			 * 图例的内填充，以像素为单位，只对position为right的图例起作用。
			 * @attribute
			 * @type Number
			 */
			padding: {
				jsonable: true
			},

			/**
			 * 图例显示的位置，目前只有top、right可选。
			 * @attribute
			 * @type String
			 */
			position: {
				jsonable: true
			},

			/**
			 * 是否显示图例。
			 * @attribute
			 * @type boolean
			 */
			visible: {
				jsonable: true
			},

			/**
			 * 图例是否显示阴影，只对position为right的图例起作用。
			 * @attribute
			 * @type boolean
			 */
			shadow: {
				jsonable: true
			}
		}
	});

	dorado.widget.ofc.showAnimateTypeMap = {
		popUp: "pop-up",
		explode: "",
		midSlide: "mid-slide",
		drop: "drop",
		fadeIn: "fade-in",
		shrinkIn : "shrink-in"
	};

	/**
	 * @class ShowAnimation
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.ShowAnimation = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.ShowAnimation.prototype */ {
		$className: "dorado.widget.ofc.ShowAnimation",
		ATTRIBUTES: /** @scope dorado.widget.ofc.ShowAnimation.prototype */ {
			/**
			 * 动画类型，可选值popUp、explode、midSlide、drop、fadeIn shrinkIn。
			 * @attribute
			 * @type String
			 */
			type: {
				jsonable: true
			},

			/**
			 * 动画执行时间，单位是秒。
			 * @attribute
			 * @type Number
			 */
			cascade: {
				jsonable: true
			},

			/**
			 * 动画执行延迟。
			 * @attribute
			 * @type Number
			 */
			delay: {
				jsonable: true
			}
		},
		toJSON: function() {
			var json = $invokeSuper.call(this, arguments), type = this._type;
			if (type) {
				json.type = dorado.widget.ofc.showAnimateTypeMap[type];
			}
			return json;
		}
	});

	/**
	 * @class OpenFlashChart
	 * @extends dorado.widget.Control
	 */
	dorado.widget.ofc.OpenFlashChart = $extend(dorado.widget.Control, /** @scope dorado.widget.ofc.OpenFlashChart.prototype */{
		$className: "dorado.widget.ofc.OpenFlashChart",
		
		ATTRIBUTES: /** @scope dorado.widget.ofc.OpenFlashChart.prototype */{
			hideMode: {
				defaultValue: "visibility"
			},

			width: {
				defaultValue: 600
			},

			height: {
				defaultValue: 400
			},

			/**
			 * 背景色。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "bg_colour"
			},

			/**
			 * 每个OpenFlashChart可以包含多个Element，每个Element会绑定不同的数据，按照配置展现成不同的形式。
			 * 每个Element可以指定自己的图例说明文字和图例颜色。如果Chart的图例为显示的话，就会看到这些信息在图例中显示。
			 * 这些Element的数据可能会使用到XAxis，YAxis，YAxisRight。
			 * @attribute
			 * @type Array
			 */
			elements: {
				jsonable: true,
				innerComponent: "",
				setter: function(value) {
					if (value) {
						for (var i = 0; i < value.length; i++) {
							var element = value[i];
							element.parent = this;
						}
					}
					this._elements = value;
				}
			},

			/**
			 * 图例。
			 * @attribute
			 * @type dorado.widget.ofc.Legend
			 */
			legend: {
				jsonable: true,
				innerComponent: "ofc.Legend"
			},

			/**
			 * 雷达坐标轴。
			 * @attribute
			 * @type dorado.widget.ofc.axis.RadarAxis
			 */
			radarAxis: {
				jsonable: true,
				jsonProperty: "radar_axis",
				innerComponent: "ofc.axis.RadarAxis"
			},

			/**
			 * Chart标题。主要用来说明该Chart是用来展示什么数据。
			 * @attribute
			 * @type dorado.widget.ofc.Text
			 */
			title: {
				jsonable: true,
				innerComponent: "ofc.Text"
			},

			/**
			 * 提示信息。
			 * @attribute
			 * @type dorado.widget.ofc.ToolTip
			 */
			toolTip: {
				innerComponent: "ofc.ToolTip",
				jsonable: true,
				jsonProperty: "tooltip"
			},

			/**
			 * X轴的图例。在X轴下方显示一段文字，用来说明X轴的含义。
			 * @attribute
			 * @type dorado.widget.ofc.Text
			 */
			xLegend: {
				jsonable: true,
				jsonProperty: "x_legend",
				innerComponent: "ofc.Text"
			},

			/**
			 * Y轴的图例。在Y轴左侧显示一段文字，用来说明Y轴的含义。
			 * @attribute
			 * @type dorado.widget.ofc.Text
			 */
			yLegend: {
				jsonable: true,
				jsonProperty: "y_legend",
				innerComponent: "ofc.Text"
			},

			/**
			 * 右Y轴的图例。在右Y轴右侧显示一段文字，用来说明右Y轴的含义。
			 * @attribute
			 * @type dorado.widget.ofc.Text
			 */
			yLegendRight: {
				jsonable: true,
				jsonProperty: "y2_legend",
				innerComponent: "ofc.Text"
			},

			/**
			 * X轴。主要用来展示X轴的分类标签。
			 * @attribute
			 * @type dorado.widget.ofc.axis.XAxis
			 */
			xAxis: {
				jsonable: true,
				jsonProperty: "x_axis",
				innerComponent: "ofc.axis.XAxis"
			},

			/**
			 * Y轴。主要用来展示对应X轴分类的Y轴的值。
			 * @attribute
			 * @type dorado.widget.ofc.axis.YAxis
			 */
			yAxis: {
				jsonable: true,
				jsonProperty: "y_axis",
				innerComponent: "ofc.axis.YAxis"
			},

			/**
			 * 右Y轴。有时候不同的Element会使用不同的Y轴，比如Bar Element使用左边的Y轴，Line Element使用右边的Y轴。
			 * @attribute
			 * @type dorado.widget.ofc.axis.YAxis
			 */
			yAxisRight: {
				jsonable: true,
				jsonProperty: "y_axis_right",
				innerComponent: "ofc.axis.YAxis"
			},

			/**
			 * 是否使用小数分隔符。
			 * @attribute
			 * @type boolean
			 */
			decimalSeparatorComma: {},

			/**
			 * 是否强制格式化小数。
			 * @attribute
			 * @type boolean
			 */
			fixedNumDecimalsForced: {},

			/**
			 * 是否使用千位分隔符。
			 * @attribute
			 * @type boolean
			 */
			thousandSeparatorDisabled: {},

			/**
			 * 格式化小数位数。
			 * @attribute
			 * @type Number
			 */
			numDecimals: {}
		},

		createDom: function() {
			var chart = this, dom = document.createElement("div");
			dom.className = "d-ofc";
			dom.style.overflow = "hidden";
			dom.id = "dorado_ofc_" + ofc_id_seed++;
			return dom;
		},

		reload: function(str) {
			var chart = this;
			if (chart._dom && chart._dom.id) {
				var swf = chart._dom.firstChild;
				if (swf) {
					if (swf.load) {
						clearTimeout(chart._initSWFTimer);
						chart._initSWFTimer = null;

						if (chart._reloadTimer != null) {
							clearTimeout(chart._reloadTimer);
						}

						chart._reloadTimer = setTimeout(function() {
							//console.log(JSON.stringify(chart));
							//console.log("swf reload invoked.");
							try{
								swf.load(str || JSON.stringify(chart));
							} catch(e) {
								//console.log(e);
							}
							chart._reloadTimer = null;
						}, 100);
					} else if (chart._initSWFTimer == null) {
						chart._initSWFTimer = setTimeout(function() {
							chart._initSWFTimer = null;
							chart.reload(str);
						}, 500);
					}
				}
			}
		},

		onAttachToDocument: function() {
			$invokeSuper.call(this, arguments);

			jQuery(this._dom).flash({
				swf: dorado.Setting["common.contextPath"] + "dorado/client/resources/open-flash-chart.swf",
				width: "100%",
				height: "100%"
			});

			this.reload();
		},

		insertElement: function(element, index) {
			var elements = this._elements;
			if (!elements) {
				elements = this._elements = [];
			}
			if (typeof index == "number") {
				elements.insert(element, index);
			} else {
				elements.push(element);
			}
		},

		addElement: function(element) {
			this.insertElement(element);
		},

		addElements: function(elements) {
			if (elements) {
				for (var i = 0, j = elements.length; i < j; i++) {
					this.addElement(elements[i]);
				}
			}
		},

		removeElement: function(index) {
			var elements = this._elements;
			if (elements) {
				if (typeof index == "number") {
				    elements.removeAt(index);
				} else {
				    elements.remove(index);
				}
			}
		},

		toJSON: toJSON
	});
	
	var element_seed = 1;

	/**
	 * @class dorado.widget.ofc.Element
	 * @extends dorado.AttributeSupport
	 * @extends dorado.EventSupport
	 * @extends dorado.widget.DataControl
	 */
	dorado.widget.ofc.Element = $extend([dorado.widget.Component, dorado.widget.DataControl], /** @scope dorado.widget.ofc.Element.prototype */{
		$className: "dorado.widget.ofc.Element",
		constructor: function(config) {
			this._eventIndex = element_seed++;
			$invokeSuper.call(this, arguments);
		},
		ATTRIBUTES: /** @scope dorado.widget.ofc.Element.prototype */{
			/**
			 * Element的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			alpha: {
				jsonable: true
			},

			/**
			 * Element在图例上显示的文字。
			 * @attribute
			 * @type String
			 */
			text: {
				jsonable: true
			},

			/**
			 * Element在图例上显示的颜色，除了这个含义，不同的种类Element会使用该属性作为其他方式使用，请参考具体的Element。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true
			},

			/**
			 * Element在图例上显示的文字的大小。
			 * @attribute
			 * @type Number
			 */
			fontSize: {
				jsonable: true,
				jsonProperty: "font-size"
			},

			/**
			 * 该Element使用的颜色集合，这些颜色集合对于不同的Element有不同的含义，可直接参考具体Element的说明。
			 * @attribute
			 * @type String[]
			 */
			colors: {
				jsonable: true,
				jsonProperty: "colours"
			},

			/**
			 * 对于Element，values就是该Element要显示的数据。
			 * 一般情况下，推荐直接使用数据绑定来实现Element数据的配置。
			 * 如果不使用数据绑定，则可以使用json，在服务器端配置亦可使用字符串。
			 * @attribute
			 * @type Array|String
			 */
			values: {
				jsonable: true,
				setter: function(value) {
					if (typeof value == "string") {
						eval("value = " + value);
					}
					this._values = value;
				}
			},

			/**
			 * 该Element是用的提示信息的模版，需要使用Magic Value。
			 * 每种Element使用的MagicValue不同，可以参考开发手册。
			 *
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
			},

			/**
			 * Element的数据绑定信息。目前不是所有的Element都支持数据绑定。
			 * @attribute
			 * @type dorado.widget.ofc.ChartBindingConfig
			 */
			bindingConfig: {
				setter: function(value) {
					if (isPlainObject(value)) {
						value = this.createBindingConfig(value);
					}
					this._bindingConfig = value;
				}
			}
		},
		EVENTS: /** @scope dorado.widget.ofc.Element.prototype */{
			/**
			 * @event
			 */
			onClick: {}
		},
		getValue: function(index) {
			var values = this._values;
			if (values) {
				return values[index];
			}
			return null;
		},
		insertValue: function(value, index) {
			var values = this._values;
			if (!values) {
				this._values = values = [];
			}
			if (typeof index == "number") {
				values.insert(value, index);
			} else {
				values.push(value);
			}
		},
		addValue: function(value) {
			this.insertValue(value);
		},
		removeValue: function(index) {
			var values = this._values;
			if (values) {
				if (typeof index == "number") {
					values.removeAt(index);
				}
			}
		},
		doExportEvents: function() {
			var element = this;
			window["dorado_ofc_onclick_" + element._eventIndex] = function() {
				element.fireEvent("onClick", element);
			};
		},
		eventsToJSON: function(object) {
			if (!this._eventsExported) {
				this.doExportEvents();
				this._eventsExported = true;
			}
			object["on-click"] = "dorado_ofc_onclick_" + this._eventIndex;
		},
		toJSON: function() {
			var json = toJSON.call(this, arguments);
			if (this.doGetType) {
				json.type = this.doGetType();
			} else {
				json.type = this.type;
			}
			json.values = this.getValues();
			this.eventsToJSON(json);
			return json;
		},
		diggData: function(bindingConfig, data) {
			var chart = this, result = [];
			if (bindingConfig && data) {
				if (data instanceof dorado.Entity) {
					result.push(chart.diggEntity(data));
				} else if (data instanceof dorado.EntityList) {
					data.each(function(entity) {
						result.push(chart.diggEntity(bindingConfig, entity));
					});
				}
			}
			return result;
		},
		diggEntity: function(bindingConfig, entity) {
			var bindingMap = bindingConfig.toJSON(), result = {};
			for (var binding in bindingMap) {
				var property = bindingMap[binding];
				result[binding] = entity.get(property);
			}
			return this.createValue(result);
		},
		getValues: function() {
			var chart = this, data = chart.getBindingData(), bindingConfig = chart._bindingConfig;
			if (bindingConfig && data) {
				chart._values = chart.diggData(bindingConfig, data);
			}
			return chart._values;
		},
		processDataSetMessage: function(messageCode, arg, data) {
			switch (messageCode) {
				case dorado.widget.DataSet.MESSAGE_REFRESH:
					this.parent.reload();
					break;

				case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
				case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
				case dorado.widget.DataSet.MESSAGE_DELETED:
				case dorado.widget.DataSet.MESSAGE_INSERTED:
					this.parent.reload();
					break;
			}
			//console.log("processDataSetMessage...");
		}
	});
})();