/**
 * @name dorado.widget.ofc.binding
 * @namespace OpenFlashChart的Element使用的数据绑定配置类的命名空间。
 */
dorado.widget.ofc.binding = {};

/**
 * @class ChartBindingConfig
 * @extends dorado.widget.ofc.JSONizable
 */
dorado.widget.ofc.binding.ChartBindingConfig = $extend(dorado.widget.ofc.JSONizable, {
	$className: "dorado.widget.ofc.binding.ChartBindingConfig",
	toJSON: function() {
		var object = this, result = {}, ATTRIBUTES = object.ATTRIBUTES;
		for (var attr in ATTRIBUTES) {
			var attrConfig = ATTRIBUTES[attr], jsonAttr = attrConfig.jsonProperty || attr;
			jsonAttr = jsonAttr.replace("Property", "");
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
});

/**
 * @class PieSliceBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.PieSliceBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.PieSliceBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.PieSliceBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.PieSliceBindingConfig.prototype */{
		valueProperty: {
			jsonable: true
		},
		labelProperty: {
			jsonable: true
		},
		labelColorProperty: {
			jsonable: true
		},
		textProperty: {
			jsonable: true
		},
		fontSizeProperty: {
			jsonable: true
		},
		toolTipProperty: {
			jsonable: true
		}
		//TODO animate
	}
});


/**
 * @class PointBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.PointBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.PointBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.PointBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.PointBindingConfig.prototype */{
		xProperty: {
			jsonable: true
		},
		yProperty: {
			jsonable: true
		}
	}
});

/**
 * @class StackBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.StackBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.StackBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.StackBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.StackBindingConfig.prototype */{
		valuesProperty: {
			jsonable: true
		},
		valueProperty: {
			jsonable: true,
			jsonProperty: "value"
		},
		colorProperty: {
			jsonable: true,
			jsonProperty: "color"
		}
	}
});

/**
 * @class TagBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.TagBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.TagBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.TagBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.TagBindingConfig.prototype */{
		xProperty: {
			jsonable: true
		},
		yProperty: {
			jsonable: true
		},
		backgroundColorProperty: {
			jsonable: true
		},
		axisProperty: {
			jsonable: true
		},
		colorProperty: {
			jsonable: true
		},
		alignXProperty: {
			jsonable: true
		},
		alignYProperty: {
			jsonable: true
		},
		padXProperty: {
			jsonable: true
		},
		padYProperty: {
			jsonable: true
		},
		fontProperty: {
			jsonable: true
		},
		boldProperty: {
			jsonable: true
		},
		rotateProperty: {
			jsonable: true
		},
		textProperty: {
			jsonable: true
		},
		fontSizeProperty: {
			jsonable: true
		},
		borderProperty: {
			jsonable: true
		},
		underlineProperty: {
			jsonable: true
		},
		alphaProperty: {
			jsonable: true
		}
	}
});

/**
 * @class LineDotBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.LineDotBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.LineDotBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.LineDotBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.LineDotBindingConfig.prototype */{
		typeProperty: {
			jsonable: true
		},
		colorProperty: {
			jsonable: true
		},
		haloSizeProperty: {
			jsonable: true
		},
		sizeProperty: {
			jsonable: true
		},
		toolTipProperty: {
			jsonable: true
		},
		valueProperty: {
			jsonable: true
		},
		xProperty: {
			jsonable: true
		},
		yProperty: {
			jsonable: true
		},
		sidesProperty: {
			jsonable: true
		},
		rotationProperty: {
			jsonable: true
		},
		hollowProperty: {
			jsonable: true
		}
	}
});

/**
 * @class ColumnBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.ColumnBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.ColumnBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.ColumnBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.ColumnBindingConfig.prototype */{
		valueProperty: {
			jsonable: true
		},
		topProperty: {
			jsonable: true
		},
		bottomProperty: {
			jsonable: true
		}
	}
});

/**
 * @class BarBindingConfig
 * @extends dorado.widget.ofc.binding.ChartBindingConfig
 */
dorado.widget.ofc.binding.BarBindingConfig = $extend(dorado.widget.ofc.binding.ChartBindingConfig, /** @scope dorado.widget.ofc.binding.BarBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.BarBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.BarBindingConfig.prototype */{
		valueProperty: {
			jsonable: true
		},
		leftProperty: {
			jsonable: true
		},
		rightProperty: {
			jsonable: true
		},
		colorProperty: {
			jsonable: true
		},
		toolTipProperty: {
			jsonable: true
		}
	}
});

/**
 * @class SketchColumnBindingConfig
 * @extends dorado.widget.ofc.binding.ColumnBindingConfig
 */
dorado.widget.ofc.binding.SketchColumnBindingConfig = $extend(dorado.widget.ofc.binding.ColumnBindingConfig, /** @scope dorado.widget.ofc.binding.SketchColumnBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.SketchColumnBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.SketchColumnBindingConfig.prototype */{
		offsetProperty: {
			jsonable: true
		},
		outlineColorProperty: {
			jsonable: true
		}
	}
});

/**
 * @class CandleBindingConfig
 * @extends dorado.widget.ofc.binding.ColumnBindingConfig
 */
dorado.widget.ofc.binding.CandleBindingConfig = $extend(dorado.widget.ofc.binding.ColumnBindingConfig, /** @scope dorado.widget.ofc.binding.CandleBindingConfig.prototype */{
	$className: "dorado.widget.ofc.binding.CandleBindingConfig",
	ATTRIBUTES: /** @scope dorado.widget.ofc.binding.CandleBindingConfig.prototype */{
		valueProperty: {
			jsonable: true
		},
		highProperty: {
			jsonable: true
		},
		lowProperty: {
			jsonable: true
		},
		negativeColorProperty: {
			jsonable: true
		}
	}
});
