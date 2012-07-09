(function() {
    var axisToJSON = function() {
        var json = $invokeSuper.call(this, arguments), labelsShortcut = this._labelsShortcut;
        if (labelsShortcut) {
            var labels = this._labels;
            if (labels) {
                json.labels.labels = labelsShortcut.split(",") ;
            } else {
                json.labels = { labels: labelsShortcut.split(",") };
            }
        }
        return json;
    };

    var yAxisToJSON = function() {
        var json = $invokeSuper.call(this, arguments), labelsShortcut = this._labelsShortcut;
        if (labelsShortcut) {
            json.labels = labelsShortcut.split(",") ;
        }
        return json;
    };

    /**
     * @name dorado.widget.ofc.axis
     * @namespace Axis NameSpace
     */
    dorado.widget.ofc.axis = {};

    /**
     * @class AbstractAxis
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.AbstractAxis = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.AbstractAxis.prototype */{
        $className: "dorado.widget.ofc.axis.AbstractAxis",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.AbstractAxis.prototype */{
            /**
             * 轴线和刻度线的颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 表格线的颜色。
             * @attribute
             * @type String
             */
            gridColor: {
                jsonable: true,
                jsonProperty: "grid-colour"
            },

            /**
             * 轴线的宽度或高度，对于X轴来讲，是高度。对于Y轴讲，是宽度。
             * @attribute
             * @type Number
             */
            stroke: {
                jsonable: true
            },

            /**
             * 轴的最大值。
             * @attribute
             * @type Number
             */
            max: {
                jsonable: true
            },

            /**
             * 轴的最小值。
             * @attribute
             * @type Number
             */
            min: {
                jsonable: true
            },

            /**
             * 每个label间的差值。
             * 如果min为1，max为5，steps为1，则坐标轴显示的label为1，2，3，4，5
             * 如果min为1，max为5，steps为2，则坐标轴显示的label为1，3，5
             * @attribute
             * @type Number
             */
            steps: {
                jsonable: true
            }
        }
    });

    /**
     * @class AxisLabel
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.AxisLabel = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.AxisLabel.prototype */{
        $className: "dorado.widget.ofc.axis.AxisLabel",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.AxisLabel.prototype */{
            /**
             * 标签上文字的颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 标签是否旋转，默认为0，表示不旋转，取值范围-180到180。
             * @attribute
             * @type Number
             */
            rotate: {
                jsonable: true
            },

            /**
             * 标签上文字的字体大小，最小为6。
             * @attribute
             * @type Number
             */
            size: {
                jsonable: true
            },

            /**
             * 标签上显示的文字。
             * @attribute
             * @type String
             */
            text: {
                jsonable: true
            },

            /**
             * 该属性只有当label被旋转的时候才会有意义，可选值为auto、center，说明如下：
             * 1. auto 旋转标签被移动，这样的标签的最高点是指向刻度线。
             * 2. center 旋转标签被移动，这样的标签在刻度线下居中。
             *
             * @attribute
             * @type String
             */
            align: {
                jsonable: true
            },

            /**
             * 指定如何对齐多行文本，可选值为left、center、right。
             * 注意指定的是如何对齐多行文本，如果只有一行，则标签还是会居中。
             * @attribute
             * @type String
             */
            justify: {
                jsonable: true
            },

            /**
             * 是否显示，默认为true。
             * @attribute
             * @type boolean
             */
            visible: {
                defaultValue: true,
                jsonable: true
            }
        }
    });

    /**
     * @class XAxis
     * @extends dorado.widget.ofc.axis.AbstractAxis
     */
    dorado.widget.ofc.axis.XAxis = $extend(dorado.widget.ofc.axis.AbstractAxis, /** @scope dorado.widget.ofc.axis.XAxis.prototype */{
        $className: "dorado.widget.ofc.axis.XAxis",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.XAxis.prototype */{
            /**
             * 指定3d渲染的深度，默认为0，表示不使用3d渲染。
             * @attribute
             * @type Number
             */
            zDepth3D: {
                jsonable: true,
                jsonProperty: "3d"
            },

            /**
             * 指定是否应该在显示最小值刻度之前有一个小的间隔，对X轴和Y轴都起作用。
             * 如果为true，则坐标轴的刻度和文字的起始位置不为给坐标轴的起始点。
             * 如果为false，则坐标轴的刻度和标签的起始位置为坐标轴的起始点。
             * @attribute
             * @type boolean
             */
            offset: {
                jsonable: true
            },

            /**
             * 刻度线的高度。
             * @attribute
             * @type Number
             */
            tickHeight: {
                jsonable: true,
                jsonProperty: "tick-height"
            },

            /**
             * X坐标轴的标签对象。
             * @attribute
             * @type dorado.widget.ofc.XAxisLabels
             */
            labels: {
                jsonable: true,
                innerComponent: "ofc.XAxisLabels"
            },

            /**
             * 如果labels很简单，只是简单的文字，则可以用逗号隔开，通过该属性赋值。
             * @attribute
             * @type String
             */
            labelsShortcut: {
                setter: function(value) {
                    if (value) {
                        this._labelsShortcut = value;
                    }
                }
            }
        },
        toJSON: axisToJSON
    });

    /**
     * @class XAxisLabels
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.XAxisLabels = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.XAxisLabels.prototype */{
        $className: "dorado.widget.ofc.axis.XAxisLabels",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.XAxisLabels.prototype */{
            /**
             * 标签文字的颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 标签是否旋转，默认为0，表示不旋转，取值范围-180到180。
             * @attribute
             * @type Number
             */
            rotate: {
                jsonable: true
            },

            /**
             * 标签上文字的字体大小，最小为6。
             * @attribute
             * @type Number
             */
            size: {
                jsonable: true
            },

            /**
             * 指定默认的X轴标签标签文字​，文字应该包括魔法值(Magic Value)，否则所有的文字会相同。
             * @attribute
             * @type String
             */
            text: {
                jsonable: true
            },

            /**
             * 是否可见。
             * @attribute
             * @type boolean
             */
            visible: {
                jsonable: true
            },

            /**
             * 定义每个标签之间的差值。
             * @attribute
             * @type Number
             */
            steps: {
                jsonable: true
            },

            /**
             * 该属性只有当label被旋转的时候才会有意义，可选值为auto、center，说明如下：
             * 1. auto 旋转标签被移动，这样的标签的最高点是指向刻度线。
             * 2. center 旋转标签被移动，这样的标签在刻度线下居中。
             *
             * @attribute
             * @type String
             */
            align: {
                jsonable: true
            },

            /**
             * 指定如何对齐多行文本，可选值为left、center、right。
             * 注意指定的是如何对齐多行文本，如果只有一行，则标签还是会居中。
             * @attribute
             * @type String
             */
            justify: {
                jsonable: true
            },

            /**
             * 如果没有标签的定义，则标签将根据X轴的值和steps自动生成。
             * 每个标签对象的定义可以覆盖上述任何属性。
             * @attribute
             * @type AxisLabel[]
             */
            labels: {
                jsonable: true
            }
        }
    });

    /**
     * @class YAxis
     * @extends dorado.widget.ofc.axis.AbstractAxis
     */
    dorado.widget.ofc.axis.YAxis = $extend(dorado.widget.ofc.axis.AbstractAxis, /** @scope dorado.widget.ofc.axis.YAxis.prototype */{
        $className: "dorado.widget.ofc.axis.YAxis",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.YAxis.prototype */{
            /**
             * 指定3d渲染的深度，默认为0，表示不使用3d渲染。
             * @attribute
             * @type Number
             */
            zDepth3D: {
                jsonable: true,
                jsonProperty: "3d"
            },

            /**
             * 指定是否应该在显示最小值刻度之前有一个小的间隔，对X轴和Y轴都起作用。
             * 如果为true，则坐标轴的刻度和文字的起始位置不为给坐标轴的起始点。
             * 如果为false，则坐标轴的刻度和标签的起始位置为坐标轴的起始点。
             * @attribute
             * @type boolean
             */
            offset: {
                jsonable: true
            },

            /**
             * 刻度线的长度。
             * @attribute
             * @type Number
             */
            tickLength: {
                jsonable: true,
                jsonProperty: "tick-length"
            },

            /**
             *
             * @attribute
             * @type dorado.widget.ofc.axis.YAxisLabels
             */
            labels: {
                jsonable: true
            },

            /**
             * @attribute
             */
            logScale: {
                jsonable: true,
                jsonProperty: "log-scale"
            },

            /**
             * 如果labels很简单，只是简单的文字，则可以用逗号隔开，通过该属性赋值。
             * @attribute
             * @type String
             */
            labelsShortcut: {
                setter: function(value) {
                    if (value) {
                        this._labelsShortcut = value;
                    }
                }
            }
        },
        toJSON: yAxisToJSON
    });

    /**
     * @class YAxisLabels
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.YAxisLabels = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.YAxisLabels.prototype */{
        $className: "dorado.widget.ofc.axis.YAxisLabels",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.YAxisLabels.prototype */{
            /**
             * 标签文字的颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 标签是否旋转，默认为0，表示不旋转，取值范围-180到180。
             * @attribute
             * @type Number
             */
            rotate: {
                jsonable: true
            },

            /**
             * 标签上文字的字体大小，最小为6。
             * @attribute
             * @type Number
             */
            size: {
                jsonable: true
            },

            /**
             * 指定默认的X轴标签标签文字​，文字应该包括魔法值(Magic Value)，否则所有的文字会相同。
             * @attribute
             * @type String
             */
            text: {
                jsonable: true
            },

            /**
             * 是否可见。
             * @attribute
             * @type boolean
             */
            visible: {
                jsonable: true,
                jsonProperty: "show_labels"
            },

            /**
             * 定义每个标签之间的差值。
             * @attribute
             * @type Number
             */
            steps: {
                jsonable: true
            },

            /**
             * 如果没有标签的定义，则标签将根据X轴的值和steps自动生成。
             * 每个标签对象的定义可以覆盖上述任何属性。
             * @attribute
             * @type AxisLabel[]
             */
            labels: {
                jsonable: true
            }
        }
    });

    /**
     * @class RadarAxisLabels
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.RadarAxisLabels = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.RadarAxisLabels.prototype */{
        $className: "dorado.widget.ofc.axis.RadarAxisLabels",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.RadarAxisLabels.prototype */{
            /**
             * 每个标签之间的差值。
             * @attribute
             * @type Number
             */
            steps: {
                jsonable: true
            },

            /**
             * 标签的字体颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 标签的配置数组。
             * @attribute
             * @type dorado.widget.ofc.axis.AxisLabel[]
             */
            labels: {
                jsonable: true
            }
        }
    });

    /**
     * @class RadarAxisSpokeLabels
     * @extends dorado.widget.ofc.JSONizable
     */
    dorado.widget.ofc.axis.RadarAxisSpokeLabels = $extend(dorado.widget.ofc.JSONizable, /** @scope dorado.widget.ofc.axis.RadarAxisSpokeLabels.prototype */{
        $className: "dorado.widget.ofc.axis.RadarAxisSpokeLabels",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.RadarAxisSpokeLabels.prototype */{
            /**
             * 标签的字体颜色。
             * @attribute
             * @type String
             */
            color: {
                jsonable: true,
                jsonProperty: "colour"
            },

            /**
             * 标签的配置数组。
             * @attribute
             * @type dorado.widget.ofc.axis.AxisLabel[]
             */
            labels: {
                jsonable: true
            }
        }
    });

    /**
     * @class RadarAxis
     * @extends dorado.widget.ofc.axis.AbstractAxis
     */
    dorado.widget.ofc.axis.RadarAxis = $extend(dorado.widget.ofc.axis.AbstractAxis, /** @scope dorado.widget.ofc.axis.RadarAxis.prototype */{
        $className: "dorado.widget.ofc.axis.RadarAxis",
        ATTRIBUTES: /** @scope dorado.widget.ofc.axis.RadarAxis.prototype */{
            /**
             * 如果labels很简单，只是简单的文字，则可以用逗号隔开，通过该属性赋值。
             * @attribute
             * @type String
             */
            labelsShortcut: {
                setter: function(value) {
                    if (value) {
                        this._labelsShortcut = value;
                    }
                }
            },

            /**
             * 如果spokeLabels很简单，只是简单的文字，则可以用逗号隔开，通过该属性赋值。
             * @attribute
             * @type String
             */
            spokeLabelsShortcut: {
                setter: function(value) {
                    if (value) {
                        this._spokeLabels = { labels: value.split(",") };
                    }
                }
            },

            /**
             * 对应于直角坐标系的Y轴。
             * @attribute
             * @type dorado.widget.ofc.axis.RadarAxisLabels
             */
            labels: {
                jsonable: true
            },

            /**
             * 对应于直角坐标系的X轴。
             * @attribute
             * @type dorado.widget.ofc.axis.RadarAxisSpokeLabels
             */
            spokeLabels: {
                jsonable: true,
                jsonProperty: "spoke-labels"
            }

        },
        toJSON: axisToJSON
    });
})();