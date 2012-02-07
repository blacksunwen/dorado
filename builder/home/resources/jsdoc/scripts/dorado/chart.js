
(function () {
    dorado.widget.chart = {};
    var ofc_id_seed = 1;
    function findSWF(movieName) {
        if (navigator.appName.indexOf("Microsoft") != -1) {
            return window[movieName];
        } else {
            return document[movieName];
        }
    }
    function isPlainObject(o) {
        return o && Object.prototype.toString.call(o) === "[object Object]" && ("isPrototypeOf" in o);
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
    dorado.widget.chart.OpenFlashChart = $extend(dorado.widget.Control, {ATTRIBUTES:{backgroundColor:{jsonable:true, jsonProperty:"bg_colour"}, elements:{jsonable:true, innerComponent:"", setter:function (attr, value) {
        if (value) {
            for (var i = 0; i < value.length; i++) {
                var element = value[i];
                element.parent = this;
            }
        }
        this._elements = value;
    }}, legend:{jsonable:true, innerComponent:"chart.Legend"}, numDecimals:{}, radarAxis:{jsonable:true, jsonProperty:"radar_axis", innerComponent:"chart.axis.RadarAxis"}, title:{jsonable:true, innerComponent:"chart.Text"}, toolTip:{innerComponent:"chart.ToolTip", jsonable:true, jsonProperty:"tooltip"}, xAxis:{jsonable:true, jsonProperty:"x_axis", innerComponent:"chart.axis.XAxis"}, yAxis:{jsonable:true, jsonProperty:"y_axis", innerComponent:"chart.axis.YAxis"}, xLegend:{jsonable:true, jsonProperty:"x_legend", innerComponent:"chart.Legend"}, yLegend:{jsonable:true, jsonProperty:"y_legend", innerComponent:"chart.Legend"}, yAxisRight:{jsonable:true, jsonProperty:"y_axis_right", innerComponent:"chart.axis.YAxis"}, yLegendRight:{}, decimalSeparatorComma:{}, fixedNumDecimalsForced:{}, thousandSeparatorDisabled:{}}, createDom:function () {
        var chart = this, dom = document.createElement("object");
        dom.id = "dorado_ofc_" + ofc_id_seed++;
        return dom;
    }, reload:function () {
        var chart = this;
        if (chart._dom && chart._dom.id) {
            var swf = findSWF(chart._dom.id);
            if (swf) {
                if (swf.load) {
                    clearTimeout(chart._initSWFTimer);
                    chart._initSWFTimer = null;
                    if (chart._reloadTimer != null) {
                        clearTimeout(chart._reloadTimer);
                    }
                    chart._reloadTimer = setTimeout(function () {
                        console.log(JSON.stringify(chart));
                        swf.load(JSON.stringify(chart));
                        chart._reloadTimer = null;
                    }, 100);
                } else {
                    if (chart._initSWFTimer == null) {
                        chart._initSWFTimer = setTimeout(function () {
                            chart._initSWFTimer = null;
                            chart.reload();
                        }, 500);
                    }
                }
            }
        }
    }, onAttachToDocument:function () {
        $invokeSuper.call(this, arguments);
        swfobject.embedSWF(dorado.Setting["common.contextPath"] + "dorado/resources/open-flash-chart.swf", this._dom.id, "600", "400", "9.0.0", "expressInstall.swf", null, {allowScriptAccess:"always"});
    }, insertElement:function (element, index) {
        var elements = this._elements;
        if (!elements) {
            elements = this._elements = [];
        }
        if (typeof index == "number") {
            elements.insert(element, index);
        } else {
            elements.push(element);
        }
    }, addElement:function (element) {
        this.insertElement(element);
    }, addElements:function (elements) {
        if (elements) {
            for (var i = 0, j = elements.length; i < j; i++) {
                this.addElement(elements[i]);
            }
        }
    }, removeElement:function (index) {
        var elements = this._elements;
        if (elements) {
            if (typeof index == "number") {
                elements.removeAt(index);
            } else {
                elements.remove(index);
            }
        }
    }, toJSON:toJSON});
    dorado.widget.chart.JSONizable = $extend(dorado.AttributeSupport, {constructor:function (config) {
        $invokeSuper.call(this, arguments);
        if (config) {
            this.set(config);
        }
    }, toJSON:toJSON});
    dorado.widget.chart.Text = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{style:{jsonable:true}, text:{jsonable:true}}});
    dorado.widget.chart.Key = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, fontSize:{jsonable:true, jsonProperty:"font-size"}, text:{jsonable:true}}});
    dorado.widget.chart.ToolTip = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{backgroundColor:{jsonable:true, jsonProperty:"bg_colour"}, bodyStyle:{jsonable:true, jsonProperty:"body_style"}, color:{jsonable:true, jsonProperty:"colour"}, mouse:{jsonable:true}, shadow:{jsonable:true}, stroke:{jsonable:true}, titleStyle:{jsonable:true, jsonProperty:"title_style"}}});
    dorado.widget.chart.Legend = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{visible:{jsonable:true}, alpha:{jsonable:true}, backgroundColor:{jsonable:true, jsonProperty:"bg_colour"}, border:{jsonable:true}, borderColor:{jsonable:true}, margin:{jsonable:true}, padding:{jsonable:true}, position:{jsonable:true}, stroke:{jsonable:true}, shadow:{jsonable:true}}});
    dorado.widget.chart.ShowAnimation = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{type:{jsonable:true}, cascade:{jsonable:true}, delay:{jsonable:true}}});
    var element_seed = 1;
    dorado.widget.chart.Element = $extend([dorado.AttributeSupport, dorado.EventSupport, dorado.widget.DataControl], {constructor:function (config) {
        $invokeSuper.call(this, arguments);
        this._eventIndex = element_seed++;
        if (config) {
            this.set(config);
        }
    }, ATTRIBUTES:{animate:{defaultValue:false, jsonable:true}, showAnimation:{jsonable:true, jsonProperty:"on-show"}, colors:{jsonable:true, jsonProperty:"colours"}, values:{jsonable:true}, fontSize:{jsonable:true, jsonProperty:"font-size"}, text:{jsonable:true}, toolTip:{jsonable:true, jsonProperty:"tip"}, alpha:{jsonable:true}, bindingConfig:{setter:function (attr, value) {
        if (isPlainObject(value)) {
            value = this.createBindingConfig(value);
        }
        this._bindingConfig = value;
    }}}, EVENTS:{onClick:{}}, getValue:function (index) {
        var values = this._values;
        if (values) {
            return values[index];
        }
        return null;
    }, insertValue:function (value, index) {
        var values = this._values;
        if (!values) {
            this._values = values = [];
        }
        if (typeof index == "number") {
            values.insert(value, index);
        } else {
            values.push(value);
        }
    }, addValue:function (value) {
        this.insertValue(value);
    }, removeValue:function (index) {
        var values = this._values;
        if (values) {
            if (typeof index == "number") {
                values.removeAt(index);
            }
        }
    }, doExportEvents:function () {
        var element = this;
        window["dorado_ofc_onclick_" + element._eventIndex] = function () {
            element.fireEvent("onClick", element);
        };
    }, eventsToJSON:function (object) {
        if (!this._eventsExported) {
            this.doExportEvents();
            this._eventsExported = true;
        }
        object["on-click"] = "dorado_ofc_onclick_" + this._eventIndex;
    }, toJSON:function () {
        var json = toJSON.call(this, arguments);
        if (this.doGetType) {
            json.type = this.doGetType();
        } else {
            json.type = this.type;
        }
        json.values = this.getValues();
        this.eventsToJSON(json);
        return json;
    }, diggData:function (bindingConfig, data) {
        var chart = this, result = [];
        if (bindingConfig && data) {
            if (data instanceof dorado.Entity) {
                result.push(chart.diggEntity(data));
            } else {
                if (data instanceof dorado.EntityList) {
                    data.each(function (entity) {
                        result.push(chart.diggEntity(bindingConfig, entity));
                    });
                }
            }
        }
        return result;
    }, diggEntity:function (bindingConfig, entity) {
        var bindingMap = bindingConfig.toJSON(), result = {};
        for (var binding in bindingMap) {
            var property = bindingMap[binding];
            result[binding] = entity.get(property);
        }
        return this.createValue(result);
    }, getValues:function () {
        var chart = this, data = chart.getBindingData(), bindingConfig = chart._bindingConfig;
        if (bindingConfig && data) {
            chart._values = chart.diggData(bindingConfig, data);
        }
        return chart._values;
    }, processDataSetMessage:function (messageCode, arg, data) {
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_REFRESH:
            this.parent.reload();
            break;
          case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
          case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
          case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
          case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
          case dorado.widget.DataSet.MESSAGE_DELETED:
          case dorado.widget.DataSet.MESSAGE_INSERTED:
            break;
        }
        console.log("processDataSetMessage...");
    }});
})();
(function () {
    function isPlainObject(o) {
        return o && Object.prototype.toString.call(o) === "[object Object]" && ("isPrototypeOf" in o);
    }
    dorado.widget.chart.BarValue = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{value:{path:"top"}, top:{jsonable:true}, bottom:{jsonable:true}, color:{jsonable:true, jsonProperty:"colour"}, toolTip:{jsonable:true, jsonProperty:"tip"}}});
    dorado.widget.chart.FilledBarValue = $extend(dorado.widget.chart.BarValue, {ATTRIBUTES:{outlineColor:{jsonable:true, jsonProperty:"outline-colour"}}});
    dorado.widget.chart.SketchBarValue = $extend(dorado.widget.chart.FilledBarValue, {ATTRIBUTES:{offset:{jsonable:true}}});
    dorado.widget.chart.CandleValue = $extend(dorado.widget.chart.BarValue, {ATTRIBUTES:{high:{jsonable:true}, low:{jsonable:true}, negativeColor:{jsonable:true, jsonProperty:"negative-colour"}}});
    dorado.widget.chart.HorizontalBarValue = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{value:{path:"right"}, left:{jsonable:true}, right:{jsonable:true}, color:{jsonable:true, jsonProperty:"colour"}, toolTip:{jsonable:true, jsonProperty:"tip"}}});
    dorado.widget.chart.Point = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{x:{jsonable:true}, y:{jsonable:true}}});
    dorado.widget.chart.Stack = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{values:{jsonable:true, setter:function (attr, value) {
        if (value instanceof Array) {
            for (var i = 0, j = value.length; i < j; i++) {
                var v = value[i];
                if (isPlainObject(v)) {
                    value[i] = new dorado.widget.chart.StackValue(v);
                }
            }
        }
        this._values = value;
    }}}, toJSON:function () {
        return this._values || [];
    }});
    dorado.widget.chart.StackValue = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, value:{jsonable:true, jsonProperty:"val"}}});
    dorado.widget.chart.PieValue = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{label:{jsonable:true}, labelColor:{jsonable:true, jsonProperty:"label-colour"}, value:{jsonable:true}}});
    var dotTypeMap = {Anchor:"anchor", Bow:"bow", Dot:"dot", HollowDot:"hollow-dot", SolidDot:"solid-dot", StarDot:"star"};
    dorado.widget.chart.LineDot = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{type:{jsonable:true}, color:{jsonable:true, jsonPoperty:"colour"}, haloSize:{jsonable:true, jsonProperty:"halo-size"}, size:{jsonable:true}, toolTip:{jsonable:true, jsonProperty:"tip"}, value:{jsonable:true}, x:{jsonable:true}, y:{jsonable:true}, sides:{jsonable:true}, rotation:{jsonable:true}, hollow:{jsonable:true}, backgroundColor:{jsonable:true, jsonProperty:"background-colour"}, width:{jsonable:true}}, toJSON:function () {
        var json = $invokeSuper.call(this, arguments), type = this._type;
        if (type) {
            json.type = dotTypeMap[type];
        }
        return json;
    }});
    dorado.widget.chart.LineDotStyle = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{type:{jsonable:true}, axis:{jsonable:true}, alpha:{jsonable:true}, color:{jsonable:true, jsonPoperty:"colour"}, haloSize:{jsonable:true, jsonProperty:"halo-size"}, toolTip:{jsonable:true, jsonProperty:"tip"}, sides:{jsonable:true}, rotation:{jsonable:true}, hollow:{jsonable:true}, backgroundColor:{jsonable:true, jsonProperty:"background-colour"}, width:{jsonable:true}}, toJSON:function () {
        var json = $invokeSuper.call(this, arguments), type = this._type;
        if (type) {
            json.type = dotTypeMap[type];
        }
        return json;
    }});
    dorado.widget.chart.Tag = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{axis:{jsonable:true}, backgroundColor:{jsonable:true, jsonProperty:"background"}, color:{jsonable:true, jsonProperty:"colour"}, alignX:{jsonable:true, jsonProperty:"align-x"}, alignY:{jsonable:true, jsonProperty:"align-y"}, padX:{jsonable:true, jsonProperty:"pad-x"}, padY:{jsonable:true, jsonProperty:"pad-y"}, font:{jsonable:true}, bold:{jsonable:true}, rotate:{jsonable:true}, text:{jsonable:true}, fontSize:{jsonable:true, jsonProperty:"font-size"}, border:{jsonable:true}, underline:{jsonable:true}, alpha:{jsonable:true}, x:{jsonable:true}, y:{jsonable:true}}});
})();
dorado.widget.chart.ChartBindingConfig = $extend(dorado.widget.chart.JSONizable, {toJSON:function () {
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
}});
dorado.widget.chart.PieValueBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{valueProperty:{jsonable:true}, labelProperty:{jsonable:true}, labelColorProperty:{jsonable:true}, textProperty:{jsonable:true}, fontSizeProperty:{jsonable:true}, toolTipProperty:{jsonable:true}}});
dorado.widget.chart.PointBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{xProperty:{jsonable:true}, yProperty:{jsonable:true}}});
dorado.widget.chart.StackBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{stackValuesProperty:{jsonable:true}, stackValueValueProperty:{jsonable:true, jsonProperty:"value"}, stackValueColorProperty:{jsonable:true, jsonProperty:"color"}}});
dorado.widget.chart.TagBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{xProperty:{jsonable:true}, yProperty:{jsonable:true}, backgroundColorProperty:{jsonable:true}, axisProperty:{jsonable:true}, colorProperty:{jsonable:true}, alignXProperty:{jsonable:true}, alignYProperty:{jsonable:true}, padXProperty:{jsonable:true}, padYProperty:{jsonable:true}, fontProperty:{jsonable:true}, boldProperty:{jsonable:true}, rotateProperty:{jsonable:true}, textProperty:{jsonable:true}, fontSizeProperty:{jsonable:true}, borderProperty:{jsonable:true}, underlineProperty:{jsonable:true}, alphaProperty:{jsonable:true}}});
dorado.widget.chart.LineDotBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{typeProperty:{jsonable:true}, colorProperty:{jsonable:true}, haloSizeProperty:{jsonable:true}, sizeProperty:{jsonable:true}, toolTipProperty:{jsonable:true}, valueProperty:{jsonable:true}, xProperty:{jsonable:true}, yProperty:{jsonable:true}, sidesProperty:{jsonable:true}, rotationProperty:{jsonable:true}, hollowProperty:{jsonable:true}}});
dorado.widget.chart.BarValueBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{valueProperty:{jsonable:true}, topProperty:{jsonable:true}, bottomProperty:{jsonable:true}}});
dorado.widget.chart.HorizontalBarValueBindingConfig = $extend(dorado.widget.chart.ChartBindingConfig, {ATTRIBUTES:{valueProperty:{jsonable:true}, leftProperty:{jsonable:true}, rightProperty:{jsonable:true}, colorProperty:{jsonable:true}, toolTipProperty:{jsonable:true}}});
dorado.widget.chart.SketchBarValueBindingConfig = $extend(dorado.widget.chart.BarValueBindingConfig, {ATTRIBUTES:{offsetProperty:{jsonable:true}, outlineColorProperty:{jsonable:true}}});
dorado.widget.chart.CandleValueBindingConfig = $extend(dorado.widget.chart.BarValueBindingConfig, {ATTRIBUTES:{valueProperty:{jsonable:true}, highProperty:{jsonable:true}, lowProperty:{jsonable:true}, negativeColorProperty:{jsonable:true}}});
dorado.widget.chart.axis = {};
dorado.widget.chart.axis.AbstractAxis = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, gridColor:{jsonable:true, jsonProperty:"grid-colour"}, max:{jsonable:true}, min:{jsonable:true}, offset:{jsonable:true}, steps:{jsonable:true}, stroke:{jsonable:true}, zDepth3D:{jsonable:true, jsonProperty:"3d"}}});
dorado.widget.chart.axis.XAxis = $extend(dorado.widget.chart.axis.AbstractAxis, {ATTRIBUTES:{labels:{jsonable:true, innerComponent:"chart.XAxisLabels"}, tickHeight:{jsonable:true, jsonProperty:"tick-length"}}});
dorado.widget.chart.axis.AxisLabel = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, rotate:{jsonable:true}, size:{jsonable:true}, text:{jsonable:true}, visible:{jsonable:true}}});
dorado.widget.chart.axis.XAxisLabels = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, rotate:{jsonable:true}, size:{jsonable:true}, text:{jsonable:true}, visible:{jsonable:true}, steps:{jsonable:true}, labels:{jsonable:true}}});
dorado.widget.chart.axis.YAxis = $extend(dorado.widget.chart.axis.AbstractAxis, {ATTRIBUTES:{labels:{jsonable:true}, tickHeight:{jsonable:true, jsonProperty:"tick-length"}, logScale:{jsonable:true, jsonProperty:"log-scale"}}});
dorado.widget.chart.axis.YAxisLabels = $extend(dorado.widget.chart.axis.AxisLabel, {ATTRIBUTES:{steps:{jsonable:true}, labels:{jsonable:true}}});
dorado.widget.chart.axis.RadarAxis = $extend(dorado.widget.chart.axis.AbstractAxis, {ATTRIBUTES:{labels:{jsonable:true}, spokeLabels:{jsonable:true, jsonProperty:"spoke-labels"}, tickHeight:{jsonable:true, jsonProperty:"tick-length"}}});
(function () {
    function isPlainObject(o) {
        return o && Object.prototype.toString.call(o) === "[object Object]" && ("isPrototypeOf" in o);
    }
    dorado.widget.chart.PieChart = $extend(dorado.widget.chart.Element, {type:"pie", ATTRIBUTES:{alpha:{jsonable:true}, animation:{jsonable:true}, border:{jsonable:true}, gradientFill:{jsonable:true, jsonProperty:"gradient-fill"}, labelColor:{jsonable:true, jsonProperty:"label-colour"}, noLabels:{jsonable:true, jsonProperty:"no-labels"}, radius:{jsonable:true}, startAngle:{jsonable:true, jsonProperty:"start-angle"}}, createValue:function (options) {
        return new dorado.widget.chart.PieValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.PieValueBindingConfig(options);
    }});
    dorado.widget.chart.ScatterChart = $extend(dorado.widget.chart.Element, {ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, dotSize:{jsonable:true, jsonProperty:"dot-size"}, dotStyle:{jsonable:true, jsonProperty:"dot-style"}, type:{defaultValue:"point"}}, createValue:function (options) {
        return new dorado.widget.chart.LineDot(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.LineDotBindingConfig(options);
    }, doGetType:function () {
        return this._type == "line" ? "scatter_line" : "scatter";
    }});
    dorado.widget.chart.StackedBarChart = $extend(dorado.widget.chart.Element, {type:"bar_stack", ATTRIBUTES:{keys:{jsonable:true, setter:function (attr, value) {
        if (value instanceof Array) {
            for (var i = 0, j = value.length; i < j; i++) {
                var v = value[i];
                if (isPlainObject(v)) {
                    value[i] = new dorado.widget.chart.Key(v);
                }
            }
        }
        this._keys = value;
    }}}, createValue:function (options) {
        return new dorado.widget.chart.StackValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.StackBindingConfig(options);
    }, diggData:function (bindingConfig, data) {
        var chart = this, result = [];
        if (bindingConfig && data) {
            if (data instanceof dorado.EntityList) {
                data.each(function (entity) {
                    result.push(chart.diggStack(bindingConfig, entity));
                });
            }
        }
        return result;
    }, diggStack:function (bindingConfig, entity) {
        var chart = this, bindingMap = bindingConfig.toJSON(), result = [];
        var valuesProperty = bindingMap["stackValues"];
        if (valuesProperty) {
            var values = entity.get(valuesProperty);
            if (values) {
                values.each(function (value) {
                    result.push(chart.diggStackValue(bindingConfig, value));
                });
            }
        }
        return new dorado.widget.chart.Stack({values:result});
    }, diggStackValue:function (bindingConfig, entity) {
        var bindingMap = bindingConfig.toJSON(), result = {}, valueProperty = bindingMap["value"], colorProperty = bindingMap["color"];
        if (valueProperty) {
            result.value = entity.get(valueProperty);
        }
        if (colorProperty) {
            result.color = entity.get(colorProperty);
        }
        return result;
    }});
    dorado.widget.chart.Shape = $extend(dorado.widget.chart.Element, {type:"shape", ATTRIBUTES:{alpha:{jsonable:true}, color:{jsonable:true, jsonProperty:"colour"}}, createValue:function (options) {
        return new dorado.widget.chart.Point(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.PointBindingConfig(options);
    }});
    dorado.widget.chart.Arrow = $extend(dorado.widget.chart.Element, {type:"arrow", ATTRIBUTES:{start:{jsonable:true}, end:{jsonable:true}, color:{jsonable:true, jsonProperty:"colour"}, alpha:{jsonable:true}, barbLength:{jsonable:true, jsonProperty:"barb-length"}}, toJSON:function () {
        var json = $invokeSuper.call(this, arguments);
        if (json.start instanceof Array) {
            var start = json.start;
            json.start = {x:start[0], y:start[1]};
        }
        if (json.end instanceof Array) {
            var end = json.end;
            json.end = {x:end[0], y:end[1]};
        }
        return json;
    }});
    dorado.widget.chart.Tags = $extend(dorado.widget.chart.Element, {type:"tags", ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, alignX:{jsonable:true, jsonProperty:"align-x"}, alignY:{jsonable:true, jsonProperty:"align-y"}, padX:{jsonable:true, jsonProperty:"pad-x"}, padY:{jsonable:true, jsonProperty:"pad-y"}, font:{jsonable:true}, bold:{jsonable:true}, rotate:{jsonable:true}, fontSize:{jsonable:true, jsonProperty:"font-size"}, border:{jsonable:true}, underline:{jsonable:true}, alpha:{jsonable:true}}, createValue:function (options) {
        return new dorado.widget.chart.Tag(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.TagBindingConfig(options);
    }});
    dorado.widget.chart.BarChart = $extend(dorado.widget.chart.Element, {ATTRIBUTES:{barWidth:{jsonable:true, jsonProperty:"bar-width"}, color:{jsonable:true, jsonProperty:"colour"}, type:{defaultValue:"Bar"}}, createValue:function (options) {
        return new dorado.widget.chart.BarValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.BarValueBindingConfig(options);
    }, doGetType:function () {
        var typeMap = {Bar:"bar", Glass:"bar_glass", Cylinder:"bar_cylinder", CylinderOutline:"bar_cylinder_outline", Dome:"bar_dome", Round:"bar_round", RoundGlass:"bar_round_glass", Round3D:"bar_round3d", Bar3D:"bar_3d", Plastic:"bar_plastic", PlasticFlat:"bar_plastic_flat"};
        return typeMap[this._type] || "bar";
    }});
    dorado.widget.chart.HorizontalBarChart = $extend(dorado.widget.chart.Element, {ATTRIBUTES:{barWidth:{jsonable:true, jsonProperty:"bar-width"}, color:{jsonable:true, jsonProperty:"colour"}}, createValue:function (options) {
        return new dorado.widget.chart.HorizontalBarValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.HorizontalBarValueBindingConfig(options);
    }, doGetType:function () {
        return "hbar";
    }});
    dorado.widget.chart.FilledBarChart = $extend(dorado.widget.chart.BarChart, {ATTRIBUTES:{outlineColor:{jsonable:true, jsonProperty:"outline-colour"}}});
    dorado.widget.chart.SketchBarChart = $extend(dorado.widget.chart.FilledBarChart, {ATTRIBUTES:{offset:{jsonable:true}}, createValue:function (options) {
        return new dorado.widget.chart.SketchBarValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.SketchBarValueBindingConfig(options);
    }, doGetType:function () {
        return "bar_sketch";
    }});
    dorado.widget.chart.CandleChart = $extend(dorado.widget.chart.BarChart, {ATTRIBUTES:{negativeColor:{jsonable:true, jsonProperty:"negative-colour"}}, createValue:function (options) {
        return new dorado.widget.chart.CandleValue(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.CandleValueBindingConfig(options);
    }, doGetType:function () {
        return "candle";
    }});
    dorado.widget.chart.LineStyle = $extend(dorado.widget.chart.JSONizable, {ATTRIBUTES:{style:{jsonable:true}, on:{jsonable:true}, off:{jsonable:true}}});
    dorado.widget.chart.LineChart = $extend(dorado.widget.chart.Element, {type:"line", ATTRIBUTES:{color:{jsonable:true, jsonProperty:"colour"}, text:{jsonable:true}, width:{jsonable:true}, rightAxis:{jsonable:true}, dotStyle:{jsonable:true, jsonProperty:"dot-style", setter:function (attr, value) {
        if (value && !(value instanceof dorado.widget.chart.LineStyle)) {
            value = new dorado.widget.chart.LineDotStyle(value);
        }
        this._dotStyle = value;
    }}, lineStyle:{jsonable:true, jsonProperty:"line-style", setter:function (attr, value) {
        if (value && !(value instanceof dorado.widget.chart.LineStyle)) {
            value = new dorado.widget.chart.LineStyle(value);
        }
        this._lineStyle = value;
    }}}, createValue:function (options) {
        return new dorado.widget.chart.LineDot(options);
    }, createBindingConfig:function (options) {
        return new dorado.widget.chart.LineDotBindingConfig(options);
    }});
    dorado.widget.chart.AreaChart = $extend(dorado.widget.chart.LineChart, {type:"area", ATTRIBUTES:{fillAlpha:{jsonable:true, jsonProperty:"fill-alpha"}, fillColor:{jsonable:true, jsonProperty:"fill"}, loop:{jsonable:true}}});
})();

