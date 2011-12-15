(function(){
	function isPlainObject(o) {
		return o && Object.prototype.toString.call(o) === '[object Object]' && ('isPrototypeOf' in o);
	}

	/**
	 * @class Pie
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Pie = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Pie.prototype */{
		$className: "dorado.widget.ofc.Pie",
		type: "pie",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Pie.prototype */{
			/**
			 * 元素的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			alpha: {
				jsonable: true
			},

			/**
			 * 是否是用动画。
			 * @attribute
			 * @type boolean
			 */
			animate: {
				defaultValue: false,
				jsonable: true
			},

			/**
			 * 边框的宽度。
			 * @attribute
			 * @type Number
			 */
			borderWidth: {
				jsonable: true,
				jsonProperty: "border"
			},

			/**
			 * 是否渐变填充。
			 * @attribute
			 * @type boolean
			 */
			gradientFill: {
				jsonable: true,
				jsonProperty: "gradient-fill"
			},

			/**
			 * 标签上文字的颜色。
			 * @attribute
			 * @type String
			 */
			labelColor: {
				jsonable: true,
				jsonProperty: "label-colour"
			},

			/**
			 * 是否显示标签。
			 * @attribute
			 * @type boolean
			 */
			noLabels: {
				jsonable: true,
				jsonProperty: "no-labels"
			},

			/**
			 * 饼图的半径。
			 * @attribute
			 * @type Number
			 */
			radius: {
				jsonable: true
			},

			/**
			 * 饼图的开始角度。
			 * @attribute
			 * @type Number
			 */
			startAngle: {
				jsonable: true,
				jsonProperty: "start-angle"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.PieSlice(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.PieSliceBindingConfig(options);
		}
	});

	/**
	 * @class Scatter
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Scatter = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Scatter.prototype */{
		$className: "dorado.widget.ofc.Scatter",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Scatter.prototype */{
			/**
			 * 对于Scatter而言，除了是图例上的颜色外，也是每个点的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 使用Y轴还是右Y轴，可选值left，right。
			 *
			 * @attribute
			 * @type String
			 */
			axis: {
				jsonable: true
			},

			/**
			 * Scatter上点使用的样式。
			 * @attribute
			 * @type dorado.widget.ofc.LineDotStyle
			 */
			dotStyle: {
				jsonable: true,
				jsonProperty: "dot-style",
				setter: function(value) {
					if (value && !(value instanceof dorado.widget.ofc.LineDotStyle)) {
						value = new dorado.widget.ofc.LineDotStyle(value);
					}
					this._dotStyle = value;
				}
			},

			/**
			 * 如果type为line，则使用该属性作为线的宽度。
			 * @attribute
			 * @type Number
			 */
			width: {
				jsonable: true
			},

			/**
			 * Scatter的类型，只能有两种：line、point。
			 * 如果为point，则不会使用线连接每个点。
			 * @attribute
			 * @type String
			 */
			type: {
				defaultValue: "point"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.LineDot(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.LineDotBindingConfig(options);
		},
		doGetType: function() {
			return this._type == "line" ? "scatter_line" : "scatter";
		}
	});

	/**
	 * @class StackedColumn
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.StackedColumn = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.StackedColumn.prototype */{
		$className: "dorado.widget.ofc.StackedColumn",
		type: "bar_stack",
		ATTRIBUTES: /** @scope dorado.widget.ofc.StackedColumn.prototype */{
			/**
			 * 图例中使用的配置。
			 * @attribute
			 * @type dorado.widget.ofc.Key[]
			 */
			keys: {
				jsonable: true,
				setter: function(value) {
					if (value instanceof Array) {
						for (var i = 0, j = value.length; i < j; i++) {
							var v = value[i];
							if (!(v instanceof dorado.widget.ofc.Key)) {
						        value[i] = new dorado.widget.ofc.Key(v);
							}
						}
					}
					this._keys = value;
				}
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.StackElement(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.StackBindingConfig(options);
		},
		diggData: function(bindingConfig, data) {
			var chart = this, result = [];
			if (bindingConfig && data) {
				if (data instanceof dorado.EntityList) {
					data.each(function(entity) {
						result.push(chart.diggStack(bindingConfig, entity));
					});
				}
			}
			return result;
		},
		diggStack: function(bindingConfig, entity) {
			var chart = this, bindingMap = bindingConfig.toJSON(), result = [];
			var valuesProperty = bindingMap["values"];
			if (valuesProperty) {
				var values = entity.get(valuesProperty);
				if (values) {
					values.each(function(value) {
						result.push(chart.diggStackElement(bindingConfig, value));
					});
				}
			}

			return new dorado.widget.ofc.model.Stack({values: result});
		},
		diggStackElement: function(bindingConfig, entity) {
			var bindingMap = bindingConfig.toJSON(), result = {}, valueProperty = bindingMap["value"], colorProperty = bindingMap["color"];
			if (valueProperty) {
				result.value = entity.get(valueProperty);
			}
			if (colorProperty) {
				result.color = entity.get(colorProperty);
			}
			return result;
		}
	});

	/**
	 * @class Shape
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Shape = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Shape.prototype */{
		$className: "dorado.widget.ofc.Shape",
		type: "shape",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Shape.prototype */{
			/**
			 * 线的宽度。
			 * @attribute
			 * @type Number
			 */
			width: {
				jsonable: true
			},

			/**
			 * 线的颜色。
			 * @attribute
			 * @type String
			 */
			lineColor: {
				jsonable: true,
				jsonProperty: "line-colour"
			},

			/**
			 * 线的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			lineAlpha: {
				jsonable: true,
				jsonProperty: "line-alpha"
			},

			/**
			 * 对于Shape而言，除了可以为图例的颜色，也为区域内填充的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.Point(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.PointBindingConfig(options);
		}
	});

	dorado.widget.ofc.Arrow = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Arrow.prototype */{
		$className: "dorado.widget.ofc.Arrow",
		type: "arrow",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Arrow.prototype */{
			/**
			 * 起始点。
			 * @attribute
			 * @type dorado.widget.ofc.model.Point
			 */
			start: {
				jsonable: true
			},

			/**
			 * 结束点。
			 * @attribute
			 * @type dorado.widget.ofc.model.Point
			 */
			end: {
				jsonable: true
			},

			/**
			 * 除了为图例的颜色，也为箭头本身的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 箭头部分的长度。
			 * @attribute
			 * @type Number
			 */
			barbLength: {
				jsonable: true,
				jsonProperty: "barb-length"
			}
		},
		toJSON: function() {
			var json = $invokeSuper.call(this, arguments);
			if (json.start instanceof Array) {
				var start = json.start;
				json.start = {x: start[0], y: start[1]};
			}
			if (json.end instanceof Array) {
				var end = json.end;
				json.end = {x: end[0], y: end[1]};
			}

			return json;
		}
	});

	/**
	 * @class Tags
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Tags = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Tags.prototype */{
		$className: "dorado.widget.ofc.Tags",
		type: "tags",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Tags.prototype */{
			/**
			 * Tag的字体颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 背景色。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "background"
			},

			/**
			 * 在水平方向的如何定位，可选值center、left、right。
			 * @attribute
			 * @type String
			 */
			alignX: {
				jsonable: true,
				jsonProperty: "align-x"
			},

			/**
			 * 在垂直方向的如何定位，可选值above、below、center。
			 * @attribute
			 * @type String
			 */
			alignY: {
				jsonable: true,
				jsonProperty: "align-y"
			},

			/**
			 * 定位以后相对于X轴的偏移。
			 * @attribute
			 * @type Number
			 */
			padX: {
				jsonable: true,
				jsonProperty: "pad-x"
			},

			/**
			 * 定位以后相对于Y轴的偏移。
			 * @attribute
			 * @type Number
			 */
			padY: {
				jsonable: true,
				jsonProperty: "pad-y"
			},

			/**
			 * 字体。
			 * @attribute
			 * @type String
			 */
			font: {
				jsonable: true
			},

			/**
			 * 文字是否粗体。
			 * @attribute
			 * @type boolean
			 */
			bold: {
				jsonable: true
			},

			/**
			 * Tag的旋转角度，取值范围-180到180。
			 * @attribute
			 * @type Number
			 */
			rotate: {
				jsonable: true
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
			 * 是否显示边框。
			 * @attribute
			 * @type boolean
			 */
			border: {
				jsonable: true
			},

			/**
			 * 文字是否有下划线。
			 * @attribute
			 * @type boolean
			 */
			underline: {
				jsonable: true
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.Tag(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.TagBindingConfig(options);
		}
	});

	/**
	 * @class Column
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Column = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Column.prototype */{
		$className: "dorado.widget.ofc.Column",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Column.prototype */{
			/**
			 * 显示使用的动画。
			 * @attribute
			 * @type dorado.widget.ofc.ShowAnimation
			 */
			showAnimation: {
				jsonable: true,
				jsonProperty: "on-show"
			},

			/**
			 * 每个Column显示的宽度所占每个X轴单元的比例，合理的取值范围是0到1，但是如果你设置1以上的数值OpenFlashChart有可能也可以正常显示。
			 * @attribute
			 * @type Number
			 */
			barWidth: {
				jsonable: true,
				jsonProperty: "bar-width"
			},

			/**
			 * Column显示所用的颜色，也是图例上使用的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 渲染类型，取值范围Column、Glass、Cylinder、Cylinder、CylinderOutline、Dome、Round、RoundGlass、Round3D、Column3D、Plastic、PlasticFlat。
			 *
			 * @attribute
			 * @type String
			 */
			type: {
				defaultValue: "Bar"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.Column(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.ColumnBindingConfig(options);
		},
		doGetType: function() {
			var typeMap = {
				Column: "bar",
				Glass: "bar_glass",

				Cylinder: "bar_cylinder",
				CylinderOutline: "bar_cylinder_outline",
				Dome: "bar_dome",
				Round: "bar_round",
				RoundGlass: "bar_round_glass",
				Round3D: "bar_round3d",

				//fade has bug. remove this type.
				//Fade: "bar_fade",

				Column3D: "bar_3d",
				Plastic: "bar_plastic",
				PlasticFlat: "bar_plastic_flat"
			};

			return typeMap[this._type] || "bar";
		}
	});

	/**
	 * @class Bar
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Bar = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Bar.prototype */{
		$className: "dorado.widget.ofc.Bar",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Bar.prototype */{
			/**
			 * 每个Bar显示的宽度所占每个Y轴单元的比例，合理的取值范围是0到1，但是如果你设置1以上的数值OpenFlashChart有可能也可以正常显示。
			 * @attribute
			 * @type Number
			 */
			barWidth: {
				jsonable: true,
				jsonProperty: "bar-width"
			},

			/**
			 * Bar使用的颜色，也是图例的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.Bar(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.BarBindingConfig(options);
		},
		doGetType: function() {
			return "hbar";
		}
	});

	/**
	 * @class FilledColumn
	 * @extends dorado.widget.ofc.Column
	 */
	dorado.widget.ofc.FilledColumn = $extend(dorado.widget.ofc.Column, /** @scope dorado.widget.ofc.FilledColumn.prototype */{
		$className: "dorado.widget.ofc.FilledColumn",
		ATTRIBUTES: /** @scope dorado.widget.ofc.FilledColumn.prototype */{
			/**
			 * 每个Bar的外边框的颜色。
			 * @attribute
			 * @type String
			 */
			outlineColor: {
				jsonable: true,
				jsonProperty: "outline-colour"
			}
		},
		doGetType: function() {
			return "bar_filled";
		}
	});

	/**
	 * @class SketchColumn
	 * @extends dorado.widget.ofc.FilledBarChart
	 */
	dorado.widget.ofc.SketchColumn = $extend(dorado.widget.ofc.FilledColumn, /** @scope dorado.widget.ofc.SketchColumn.prototype */{
		$className: "dorado.widget.ofc.SketchColumn",
		ATTRIBUTES: /** @scope dorado.widget.ofc.SketchColumn.prototype */{
			/**
			 * 该风格的Column会分成几部分渲染，假设分成6部分，则每个部分之间会随机进行倾斜，所以看上去像是涂鸦。
			 * 该值越大，每个部分的倾斜会越严重，看起来越凌乱。推荐取值范围1-10。
			 * @attribute
			 * @type Number
			 */
			offset: {
				jsonable: true
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.SketchColumn(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.SketchColumnBindingConfig(options);
		},
		doGetType: function() {
			return "bar_sketch";
		}
	});

	/**
	 * @class Candle
	 * @extends dorado.widget.ofc.Column
	 */
	dorado.widget.ofc.Candle = $extend(dorado.widget.ofc.Column, /** @scope dorado.widget.ofc.Candle.prototype */{
		$className: "dorado.widget.ofc.Candle",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Candle.prototype */{
			/**
			 * 如果bottom>top的时候显示的颜色。
			 * @attribute
			 * @type String
			 */
			negativeColor: {
				jsonable: true,
				jsonProperty: "negative-colour"
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.Candle(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.CandleBindingConfig(options);
		},
		doGetType: function() {
			return "candle";
		}
	});

	/**
	 * @class LineStyle
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.LineStyle = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.LineStyle.prototype */{
		$className: "dorado.widget.ofc.LineStyle",
		ATTRIBUTES: /** @scope dorado.widget.ofc.LineStyle.prototype */{
			/**
			 * 线条的样式，两种solid，dash，默认为solid。
			 *
			 * @attribute
			 * @type String
			 */
			style: {
				jsonable: true
			},

			/**
			 * 仅当线条样式为dash的时候有效，该值为虚线中每个小线条的长度。
			 * @attribute
			 * @type Number
			 */
			on: {
				jsonable: true
			},

			/**
			 * 仅当线条样式为dash的时候有效，该值为虚线中每个小线条中的空隙的长度。
			 * @attribute
			 * @type Number
			 */
			off: {
				jsonable: true
			}
		}
	});

	/**
	 * @class LineDotStyle
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.LineDotStyle = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.LineDotStyle.prototype */{
		$className: "dorado.widget.ofc.LineDotStyle",
		ATTRIBUTES: /** @scope dorado.widget.ofc.LineDotStyle.prototype */{
			/**
			 * Dot的类型，默认值为Dot，可选值Anchor, Bow, Dot, StarDot, HollowDot, SolidDot。
			 * @attribute
			 * @type String
			 * @default "Dot"
			 */
			type: {
				jsonable: true
			},

			/**
			 * 使用哪边的Y轴，可选值left、right。
			 * @attribute
			 * @type String
			 */
			axis: {
				jsonable: true
			},

			/**
			 * Dot的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			alpha: {
				jsonable: true
			},

			/**
			 * Dot的颜色，如果为空心，则为边框的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 如果类型为Dot、SolidDot、HollowDot，则为Dot的半径。
			 * 其他类型的Dot应该理解为在这个半径的圆内，该类型Dot的大小。
			 * @attribute
			 * @type Number
			 */
			dotSize: {
				jsonable: true,
				jsonProperty: "dot-size"
			},

			/**
			 * Dot的光晕的大小，从视觉效果上来看，就是每个Dot与线的间隔。
			 * 默认为1，该值越大，Dot与线之间的间隔越大。
			 * @attribute
			 * @type Number
			 */
			haloSize: {
				jsonable: true,
				jsonProperty: "halo-size"
			},

			/**
			 * Dot的边框的宽度，仅在Dot为空心的情况下才起作用。
			 * 默认情况下，除了Dot、SolidDot，其他类型的Dot默认都是空心。
			 * @attribute
			 * @type Number
			 */
			width: {
				jsonable: true
			},

			/**
			 * 提示信息。
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
			},

			/**
			 * 仅在Dot的类型为Anchor的情况下起作用，为多边形的边的个数。
			 * @attribute
			 * @default 3
			 */
			sides: {
				jsonable: true
			},

			/**
			 * 仅在Dot的类型为Anchor、Bow、Start的情况下起作用，为图形的旋转度数，取值范围-180到180。
			 * @attribute
			 * @type Number
			 */
			rotation: {
				jsonable: true
			},

			/**
			 * 仅在Dot的类型为Anchor、Bow、Start的情况下起作用，默认值为true，表示Dot是否空心。
			 * @attribute
			 * @type boolean
			 * @default true
			 */
			hollow: {
				jsonable: true
			},

			/**
			 * 仅在Dot的类型为Anchor、Bow、Start并且hollow为true的情况下起作用，表示空心区域的颜色。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "background-colour"
			},

			/**
			 * 仅在Dot的类型为Anchor、Bow、Start并且hollow为true的情况下起作用，表示空心区域的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			backgroundAlpha: {
				jsonable: true,
				jsonProperty: "background-colour"
			}
		},
		toJSON: function() {
			var json = $invokeSuper.call(this, arguments), type = this._type;
			if (type) {
				json.type = dorado.widget.ofc.dotTypeMap[type];
			}
			return json;
		}
	});

	/**
	 * @class Line
	 * @extends dorado.widget.ofc.Element
	 */
	dorado.widget.ofc.Line = $extend(dorado.widget.ofc.Element, /** @scope dorado.widget.ofc.Line.prototype */{
		$className: "dorado.widget.ofc.Line",
		type: "line",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Line.prototype */{
			/**
			 * 显示使用的动画。
			 * @attribute
			 * @type dorado.widget.ofc.ShowAnimation
			 */
			showAnimation: {
				jsonable: true,
				jsonProperty: "on-show",
				setter: function(value) {
					if (isPlainObject(value)) {
						value = new dorado.widget.ofc.ShowAnimation(value);
					}
					this._showAnimation = value;
				}
			},

			/**
			 * 线条的颜色，也是图例的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 线条的宽度。
			 * @attribute
			 * @type Number
			 */
			width: {
				jsonable: true
			},

			/**
			 * 使用Y轴还是右Y轴，可选值left，right。
			 *
			 * @attribute
			 * @type String
			 */
			axis: {
				jsonable: true
			},

			/**
			 * 线条上每个数据点的样式。
			 * @attribute
			 * @type dorado.widget.ofc.LineDotStyle
			 */
			dotStyle: {
				jsonable: true,
				jsonProperty: "dot-style",
				setter: function(value) {
					if (value && !(value instanceof dorado.widget.ofc.LineStyle)) {
						value = new dorado.widget.ofc.LineDotStyle(value);
					}
					this._dotStyle = value;
				}
			},

			/**
			 * 线条的样式定义，目前仅支持实线与虚线，如果使用实线，则无需设置此属性。
			 * @attribute
			 * @type dorado.widget.ofc.LineStyle
			 */
			lineStyle: {
				jsonable: true,
				jsonProperty: "line-style",
				setter: function(value) {
					if (value && !(value instanceof dorado.widget.ofc.LineStyle)) {
						value = new dorado.widget.ofc.LineStyle(value);
					}
					this._lineStyle = value;
				}
			}
		},
		createValue: function(options) {
			return new dorado.widget.ofc.model.LineDot(options);
		},
		createBindingConfig: function(options) {
			return new dorado.widget.ofc.binding.LineDotBindingConfig(options);
		}
	});

	/**
	 * @class Area
	 * @extends dorado.widget.ofc.LineChart
	 */
	dorado.widget.ofc.Area = $extend(dorado.widget.ofc.Line, /** @scope dorado.widget.ofc.Area.prototype */{
		$className: "dorado.widget.ofc.Area",
		type: "area",
		ATTRIBUTES: /** @scope dorado.widget.ofc.Area.prototype */{
			/**
			 * 填充区域使用的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			fillAlpha: {
				jsonable: true,
				jsonProperty: "fill-alpha"
			},

			/**
			 * 填充区域使用的颜色。
			 * @attribute
			 * @type String
			 */
			fillColor: {
				jsonable: true,
				jsonProperty: "fill"
			},

			/**
			 * 是否连接起点和终点，如果连接，则loop为true。
			 * @attribute
			 * @type boolean
			 */
			loop: {
				jsonable: true
			}
		}
	});
})();
