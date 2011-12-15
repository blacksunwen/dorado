(function(){
	function isPlainObject(o) {
		return o && Object.prototype.toString.call(o) === '[object Object]' && ('isPrototypeOf' in o);
	}

	/**
	 * @name dorado.widget.ofc.model
	 * @namespace OpenFlashChart的Element使用的数据类命名空间。
	 */
	dorado.widget.ofc.model = {};

	/**
	 * @class Column
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.Column = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.Column.prototype */{
		$className: "dorado.widget.ofc.model.Column",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Column.prototype */{
			/**
			 * Column的值，实际会被写到top属性。
			 * @attribute
			 * @type Number
			 */
			value: {
				path: "top"
			},

			/**
			 * Column的在Y坐标轴上的最高点的值。
			 * @attribute
			 * @type Number
			 */
			top: {
				jsonable: true
			},

			/**
			 * Column的在Y坐标轴上的最低点的值，如果不指定，则为Y轴的最低点。
			 * @attribute
			 * @type Number
			 */
			bottom: {
				jsonable: true
			},

			/**
			 * Column的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 提示信息，可以使用Magic Value。
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
			}
		}
	});

	/**
	 * @class FilledColumn
	 * @extends dorado.widget.ofc.model.Column
	 */
	dorado.widget.ofc.model.FilledColumn = $extend(dorado.widget.ofc.model.Column, /** @scope dorado.widget.ofc.model.FilledColumn.prototype */{
		$className: "dorado.widget.ofc.model.FilledColumn",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.FilledColumn.prototype */{
			/**
			 * 边框的颜色。
			 * @attribute
			 * @type String
			 */
			outlineColor: {
				jsonable: true,
				jsonProperty: "outline-colour"
			}
		}
	});

	/**
	 * @class SketchBarValue
	 * @extends dorado.widget.ofc.FilledColumn
	 */
	dorado.widget.ofc.model.SketchColumn = $extend(dorado.widget.ofc.model.FilledColumn, /** @scope dorado.widget.ofc.model.SketchColumn.prototype */{
		$className: "dorado.widget.ofc.model.SketchColumn",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.SketchColumn.prototype */{
			/**
			 * 该风格的Column会分成几部分渲染，假设分成6部分，则每个部分之间会随机进行倾斜，所以看上去像是涂鸦。
			 * 该值越大，每个部分的倾斜会越严重，看起来越凌乱。推荐取值范围1-10。
			 * @attribute
			 * @type Number
			 */
			offset: {
				jsonable: true
			}
		}
	});

	/**
	 * @class Candle
	 * @extends dorado.widget.ofc.model.Column
	 */
	dorado.widget.ofc.model.Candle = $extend(dorado.widget.ofc.model.Column, /** @scope dorado.widget.ofc.model.Candle.prototype */{
		$className: "dorado.widget.ofc.model.Candle",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Candle.prototype */{
			/**
			 * 最高点的值，与top属性有所区别。
			 * @attribute
			 * @type Number
			 */
			high: {
				jsonable: true
			},
			/**
			 * 最低点的值，与bottom属性有所区别。
			 * @attribute
			 * @type Number
			 */
			low: {
				jsonable: true
			},
			/**
			 * 如果bottom>top的时候显示的颜色。
			 * @attribute
			 * @type Number
			 */
			negativeColor: {
				jsonable: true,
				jsonProperty: "negative-colour"
			}
		}
	});

	/**
	 * @class Bar
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.Bar = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.Bar.prototype */{
		$className: "dorado.widget.ofc.model.Bar",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Bar.prototype */{
			/**
			 * Bar的值，实际会被写到right属性。
			 * @attribute
			 * @type Number
			 */
			value: {
				path: "right"
			},

			/**
			 * Bar在X轴最左边的值，如不指定，则为X轴的最小值。
			 * @attribute
			 * @type Number
			 */
			left: {
				jsonable: true
			},

			/**
			 * Bar在X轴最右边的值。
			 * @attribute
			 * @type Number
			 */
			right: {
				jsonable: true
			},

			/**
			 * Bar的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * 提示信息，可以使用Magic Value。
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
			}
		}
	});

	/**
	 * @class Point
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.Point = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.Point.prototype */{
		$className: "dorado.widget.ofc.model.Point",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Point.prototype */{
			/**
			 * Point的x坐标。
			 * @attribute
			 * @type Number
			 */
			x: {
				jsonable: true
			},

			/**
			 * Point的Y坐标。
			 * @attribute
			 * @type Number
			 */
			y: {
				jsonable: true
			}
		}
	});

	/**
	 * @class Stack
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.Stack = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.Stack.prototype */{
		$className: "dorado.widget.ofc.model.Stack",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Stack.prototype */{
			/**
			 * Stack的值，由StackElement的数组组成。
			 * @attribute
			 * @type StackElement[]
			 */
			values: {
				jsonable: true,
				setter: function(value) {
					if (value instanceof Array) {
						for (var i = 0, j = value.length; i < j; i++) {
							var v = value[i];
							if (isPlainObject(v)) {
								value[i] = new dorado.widget.ofc.model.StackElement(v);
							}
						}
					}
					this._values = value;
				}
			}
		},
		toJSON: function() {
			return this._values || [];
		}
	});

	/**
	 * @class StackElement
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.StackElement = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.StackElement.prototype */{
		$className: "dorado.widget.ofc.model.StackElement",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.StackElement.prototype */{
			/**
			 * StackElement对应的Column的颜色。
			 * @attribute
			 * @type String
			 */
			color: {
				jsonable: true,
				jsonProperty: "colour"
			},

			/**
			 * StackElement对应的Column的值。
			 * @attribute
			 * @type Number
			 */
			value: {
				jsonable: true,
				jsonProperty: "val"
			}
		}
	});

	/**
	 * @class PieSlice
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.PieSlice = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.PieSlice.prototype */{
		$className: "dorado.widget.ofc.model.PieSlice",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.PieSlice.prototype */{
			/**
			 * 图例上显示的文字。
			 * 注意如果text的值为空，可能导致虽然设置了显示图例，但是图例无法正常显示出来的情况。
			 * @attribute
			 * @type String
			 */
			text: {
				jsonable: true
			},

			/**
			 * 每个Slice旁边显示的文字。
			 * @attribute
			 * @type String
			 */
			label: {
				jsonable: true
			},

			/**
			 * 每个Slice旁边显示文字的颜色。
			 * @attribute
			 * @type String
			 */
			labelColor: {
				jsonable: true,
				jsonProperty: "label-colour"
			},

			/**
			 * Slice的值。
			 * @attribute
			 * @type Number
			 */
			value: {
				jsonable: true
			},

			/**
			 * 提示信息，可以使用Magic Value。
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
			}
		}
	});

	dorado.widget.ofc.dotTypeMap = {
		Anchor: "anchor",
		Bow: "bow",
		Dot: "dot",
		HollowDot: "hollow-dot",
		SolidDot: "solid-dot",
		StarDot: "star"
	};

	/**
	 * @class LineDot
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.LineDot = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.LineDot.prototype */{
		$className: "dorado.widget.ofc.model.LineDot",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.LineDot.prototype */{
			/**
			 * Dot的值，就是该Dot在Y轴的值。
			 * @attribute
			 * @type Number
			 */
			value: {
				jsonable: true
			},

			/**
			 * Dot对应X轴的值，一般情况无需指定。
			 * @attribute
			 * @type Number
			 */
			x: {
				jsonable: true
			},

			/**
			 * Dot对应Y轴的值，与Value一样。
			 * @attribute
			 * @type Number
			 */
			y: {
				jsonable: true
			},

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
			},

			/**
			 * 提示信息，可以使用Magic Value。
			 * @attribute
			 * @type String
			 */
			toolTip: {
				jsonable: true,
				jsonProperty: "tip"
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
	 * @class Tag
	 * @extends dorado.widget.ofc.JSONizable
	 */
	dorado.widget.ofc.model.Tag = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.model.Tag.prototype */{
		$className: "dorado.widget.ofc.model.Tag",
		ATTRIBUTES: /** @scope dorado.widget.ofc.model.Tag.prototype */{
			/**
			 * Tag的透明度，取值范围0到1(0为不可见，1为不透明)。
			 * @attribute
			 * @type Number
			 */
			alpha:{
				jsonable: true
			},

			/**
			 * Tag的背景色。
			 * @attribute
			 * @type String
			 */
			backgroundColor: {
				jsonable: true,
				jsonProperty: "background"
			},

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
			 * Tag上的文字。
			 * @attribute
			 * @type String
			 */
			text: {
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
			},

			/**
			 * Tag使用的坐标轴，可选值left、right。
			 * @attribute
			 * @type String
			 */
			axis: {
				jsonable: true
			},

			/**
			 * x轴坐标。
			 * @attribute
			 * @type Number
			 */
			x: {
				jsonable: true
			},

			/**
			 * y轴坐标。
			 * @attribute
			 * @type Number
			 */
			y: {
				jsonable: true
			}
		}
	});
})();
