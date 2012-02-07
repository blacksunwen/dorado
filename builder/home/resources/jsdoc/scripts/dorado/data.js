
dorado.DataPipe = $class({$className:"dorado.DataPipe", runningProcNum:0, convertIfNecessary:function (data, dataTypeRepository, dataType) {
    return dorado.DataUtil.convertIfNecessary(data, dataTypeRepository, dataType);
}, get:function () {
    return this.convertIfNecessary(this.doGet(), this.dataTypeRepository, this.dataType);
}, getAsync:function (callback) {
    callback = callback || dorado._NULL_FUNCTION;
    var callbacks = this._waitingCallbacks;
    if (callbacks) {
        callbacks.push(callback);
    } else {
        this._waitingCallbacks = callbacks = [callback];
        this.runningProcNum++;
        this.doGetAsync({scope:this, callback:function (success, result) {
            delete this._waitingCallbacks;
            this.runningProcNum = 0;
            if (success) {
                result = this.convertIfNecessary(result, this.dataTypeRepository, this.dataType);
            }
            var errors;
            for (var i = 0; i < callbacks.length; i++) {
                try {
                    $callback(callbacks[i], success, result);
                }
                catch (e) {
                    if (errors === undefined) {
                        errors = [];
                    }
                    errors.push(e);
                }
            }
            if (errors) {
                throw ((errors.length > 1) ? errors : errors[0]);
            }
        }});
    }
}});
(function () {
    dorado.DataProvider = $class({$className:"dorado.DataProvider", supportsEntity:true, constructor:function (id) {
        this.id = id;
        this.name = dorado.DataUtil.extractNameFromId(id);
    }, getAjaxOptions:function (arg) {
        var jsonData = {action:"load-data", dataProvider:this.id, supportsEntity:this.supportsEntity};
        if (arg) {
            jsonData.parameter = dorado.JSON.evaluate(arg.parameter);
            if (arg.dataType) {
                var dataType = arg.dataType;
                if (dataType instanceof dorado.DataType) {
                    dataType = dataType.get("id");
                } else {
                    if (typeof dataType == "string") {
                        dataType = dataType;
                    } else {
                        dataType = dataType.id;
                    }
                }
                jsonData.resultDataType = dataType;
            }
            jsonData.pageSize = arg.pageSize;
            jsonData.pageNo = arg.pageNo;
            jsonData.context = arg.view ? arg.view.get("context") : null;
        }
        if (dorado.DataProvider._SYS_PARAMETER) {
            jsonData.sysParameter = dorado.DataProvider._SYS_PARAMETER;
            dorado.DataProvider._SYS_PARAMETER = null;
        }
        if (this.supportsEntity && this.dataTypeRepository) {
            jsonData.loadedDataTypes = this.dataTypeRepository.getLoadedDataTypes();
        }
        return dorado.Object.apply({jsonData:jsonData}, $setting["ajax.dataProviderOptions"]);
    }, convertEntity:function (data, dataTypeRepository, dataType, ajaxOptions) {
        if (data == null) {
            return data;
        }
        data = dorado.DataUtil.convertIfNecessary(data, dataTypeRepository, dataType);
        if (data instanceof dorado.EntityList) {
            data.dataProvider = this;
            data.parameter = ajaxOptions.jsonData.parameter;
            data.pageSize = ajaxOptions.jsonData.pageSize;
        } else {
            if (data instanceof dorado.Entity) {
                data.dataProvider = this;
                data.parameter = ajaxOptions.jsonData.parameter;
            }
        }
        return data;
    }, getResult:function (arg) {
        var ajaxOptions = this.getAjaxOptions(arg), ajax = dorado.Toolkits.getAjax(ajaxOptions);
        var result = ajax.requestSync(ajaxOptions);
        if (result.success) {
            var json = result.getJsonData(), data;
            if (json && (json.$dataTypeDefinitions || json.$context)) {
                data = json.data;
                if (json.$dataTypeDefinitions) {
                    this.dataTypeRepository.parseJsonData(json.$dataTypeDefinitions);
                }
                if (json.$context && arg && arg.view) {
                    arg.view.set("context", json.$context);
                }
            } else {
                data = json;
            }
            if (data && this.supportsEntity) {
                data = this.convertEntity(data, this.dataTypeRepository, this.dataType, ajaxOptions);
            }
            return data;
        } else {
            throw result.error;
        }
    }, getResultAsync:function (arg, callback) {
        var ajaxOptions = this.getAjaxOptions(arg), ajax = dorado.Toolkits.getAjax(ajaxOptions);
        var dataType = this.dataType, supportsEntity = this.supportsEntity, dataTypeRepository = this.dataTypeRepository;
        var message = this.message;
        if (message == null) {
            message = ajaxOptions.message;
        }
        if (message === undefined) {
            message = $resource("dorado.data.DataProviderTaskIndicator");
        }
        if (message) {
            ajaxOptions.message = message;
        }
        ajax.request(ajaxOptions, {scope:this, callback:function (success, result) {
            if (success) {
                var json = result.getJsonData(), data;
                if (json && (json.$dataTypeDefinitions || json.$context)) {
                    data = json.data;
                    if (json.$dataTypeDefinitions) {
                        this.dataTypeRepository.parseJsonData(json.$dataTypeDefinitions);
                    }
                    if (json.$context && arg && arg.view) {
                        arg.view.set("context", json.$context);
                    }
                } else {
                    data = json;
                }
                if (data && supportsEntity) {
                    data = this.convertEntity(data, dataTypeRepository, dataType, ajaxOptions);
                }
                $callback(callback, true, data, {scope:this});
            } else {
                $callback(callback, false, result.error, {scope:this});
            }
        }});
    }});
    dorado.AjaxDataProvider = $extend(dorado.DataProvider, {$className:"dorado.AjaxDataProvider", constructor:function (options) {
        if (typeof options == "string") {
            options = {url:options};
        }
        this._baseOptions = options || {};
    }, getAjaxOptions:function (arg) {
        var options = dorado.Object.apply({}, this._baseOptions), jsonData = options.jsonData = {};
        if (this._baseOptions.jsonData) {
            dorado.Object.apply(jsonData, this._baseOptions.jsonData);
        }
        if (arg) {
            jsonData.parameter = dorado.JSON.evaluate(arg.parameter);
            jsonData.pageSize = arg.pageSize;
            jsonData.pageNo = arg.pageNo;
        }
        if (dorado.DataProvider._SYS_PARAMETER) {
            jsonData.sysParameter = dorado.DataProvider._SYS_PARAMETER;
            dorado.DataProvider._SYS_PARAMETER = null;
        }
        return options;
    }});
    var dataProviders = {};
    dorado.DataProvider.create = function (id) {
        var provider = dataProviders[id];
        if (provider === undefined) {
            dataProviders[id] = provider = new dorado.DataProvider(id);
        }
        return provider;
    };
})();
dorado.DataProviderPipe = $extend(dorado.DataPipe, {$className:"dorado.DataProviderPipe", getDataProvider:function () {
    return this.dataProvider;
}, doGet:function () {
    return this.doGetAsync();
}, doGetAsync:function (callback) {
    var dataProviderArg = this.getDataProviderArg(), dataProvider = this.getDataProvider();
    dataProvider.dataTypeRepository = this.dataTypeRepository;
    dataProvider.dataType = this.dataType;
    if (callback) {
        dataProvider.getResultAsync(dataProviderArg, callback);
    } else {
        return dataProvider.getResult(dataProviderArg);
    }
}});
(function () {
    dorado.DataResolver = $class({$className:"dorado.DataResolver", supportsEntity:true, constructor:function (id) {
        this.id = id;
        this.name = dorado.DataUtil.extractNameFromId(id);
    }, getAjaxOptions:function (arg) {
        var jsonData = {action:"resolve-data", dataResolver:this.id, supportsEntity:this.supportsEntity};
        if (arg) {
            jsonData.dataItems = arg.dataItems;
            jsonData.parameter = dorado.JSON.evaluate(arg.parameter);
            jsonData.context = arg.view ? arg.view.get("context") : null;
        }
        if (dorado.DataProvider._SYS_PARAMETER) {
            jsonData.sysParameter = dorado.DataProvider._SYS_PARAMETER;
            dorado.DataProvider._SYS_PARAMETER = null;
        }
        if (this.supportsEntity && this.dataTypeRepository) {
            jsonData.loadedDataTypes = this.dataTypeRepository.getLoadedDataTypes();
        }
        return dorado.Object.apply({jsonData:jsonData}, $setting["ajax.dataResolverOptions"]);
    }, resolve:function (arg) {
        var ajaxOptions = this.getAjaxOptions(arg), ajax = dorado.Toolkits.getAjax(ajaxOptions);
        var result = ajax.requestSync(ajaxOptions);
        if (result.success) {
            var result = result.getJsonData();
            if (result && (result.$dataTypeDefinitions || result.$context)) {
                result = result.data;
                if (result.$dataTypeDefinitions) {
                    this.dataTypeRepository.parseJsonData(result.$dataTypeDefinitions);
                }
                if (json.$context && arg && arg.view) {
                    arg.view.set("context", json.$context);
                }
            }
            if (result && this.supportsEntity) {
                result.returnValue = dorado.DataUtil.convertIfNecessary(result.returnValue, this.dataTypeRepository);
            }
            return result;
        } else {
            throw result.error;
        }
    }, resolveAsync:function (arg, callback) {
        var ajaxOptions = this.getAjaxOptions(arg), supportsEntity = this.supportsEntity, ajax = dorado.Toolkits.getAjax(ajaxOptions);
        var message = this.message;
        if (message == null) {
            message = ajaxOptions.message;
        }
        if (message === undefined) {
            message = $resource("dorado.data.DataResolverTaskIndicator");
        }
        if (message) {
            ajaxOptions.message = message;
        }
        ajax.request(ajaxOptions, {scope:this, callback:function (success, result) {
            if (success) {
                var result = result.getJsonData();
                if (result && (result.$dataTypeDefinitions || result.$context)) {
                    result = result.data;
                    if (result.$dataTypeDefinitions) {
                        this.dataTypeRepository.parseJsonData(result.$dataTypeDefinitions);
                    }
                    if (json.$context && arg && arg.view) {
                        arg.view.set("context", json.$context);
                    }
                }
                if (result && supportsEntity) {
                    result.returnValue = dorado.DataUtil.convertIfNecessary(result.returnValue, this.dataTypeRepository);
                }
                $callback(callback, true, result, {scope:this});
            } else {
                $callback(callback, false, result.error, {scope:this});
            }
        }});
    }});
    dorado.AjaxDataResolver = $extend(dorado.DataResolver, {$className:"dorado.AjaxDataResolver", constructor:function (options) {
        if (typeof options == "string") {
            options = {url:options};
        }
        this._baseOptions = options || {};
    }, getAjaxOptions:function (arg) {
        var options = dorado.Object.apply({}, this._baseOptions), jsonData = options.jsonData = {};
        if (this._baseOptions.jsonData) {
            dorado.Object.apply(jsonData, this._baseOptions.jsonData);
        }
        jsonData.action = "resolve-data";
        jsonData.dataResolver = this.name;
        if (arg) {
            jsonData.dataItems = arg.dataItems;
            jsonData.parameter = dorado.JSON.evaluate(arg.parameter);
        }
        if (dorado.DataProvider._SYS_PARAMETER) {
            jsonData.sysParameter = dorado.DataProvider._SYS_PARAMETER;
            dorado.DataProvider._SYS_PARAMETER = null;
        }
        return options;
    }});
    var dataResolvers = {};
    dorado.DataResolver.create = function (id) {
        var resolver = dataResolvers[id];
        if (resolver === undefined) {
            dataResolvers[id] = resolver = new dorado.DataResolver(id);
        }
        return resolver;
    };
})();
dorado.DataType = $extend(dorado.AttributeSupport, {$className:"dorado.DataType", ATTRIBUTES:{name:{readOnly:true}, id:{writeOnce:true}, dataTypeRepository:{readOnly:true}}, constructor:function (config) {
    $invokeSuper.call(this, arguments);
    var name;
    if (config && config.constructor == String) {
        name = config;
        config = null;
    } else {
        if (config) {
            name = config.name;
            delete config.name;
            this.set(config);
        }
    }
    this._name = name ? name : dorado.Core.newId();
    if (!this._id) {
        this._id = this._name;
    }
    if (this.id) {
        if (window[this.id] === undefined) {
            window[this.id] = this;
        } else {
            var v = window[this.id];
            if (v instanceof Array) {
                v.push(this);
            } else {
                window[this.id] = [v, this];
            }
        }
    }
}, parse:function (data, argument) {
    return data;
}, getListenerScope:function () {
    return (this._dataTypeRepository) ? this._dataTypeRepository.view : null;
}, toText:function (data, argument) {
    if (data === null || data === undefined || (typeof data !== "object" && isNaN(data))) {
        return "";
    } else {
        return data + "";
    }
}});
dorado.DataType.getSubName = function (name) {
    var complexDataTypeNameRegex = /^[\w.$:@#]*\[[\w\[\].$:@#]*\]$/;
    return (name.match(complexDataTypeNameRegex)) ? name.substring(name.indexOf("[") + 1, name.length - 1) : null;
};
dorado.AggregationDataType = $extend(dorado.DataType, {$className:"dorado.AggregationDataType", ATTRIBUTES:{elementDataType:{getter:function () {
    return this.getElementDataType("always");
}, writeOnce:true}, pageSize:{defaultValue:0}}, constructor:function (config, elementDataType) {
    $invokeSuper.call(this, arguments);
    if (elementDataType) {
        this._elementDataType = elementDataType;
    }
}, getElementDataType:function (loadMode) {
    var dataType = this._elementDataType;
    if (dataType != null) {
        dataType = dorado.LazyLoadDataType.dataTypeTranslator.call(this, dataType, loadMode);
        if (dataType instanceof dorado.DataType) {
            this._elementDataType = dataType;
        }
    }
    return dataType;
}, parse:function (data) {
    if (data != null) {
        return (data instanceof dorado.EntityList) ? data : new dorado.EntityList(data, this._dataTypeRepository, this);
    } else {
        return null;
    }
}});
dorado.EntityDataType = $extend([dorado.DataType, dorado.EventSupport], {$className:"dorado.EntityDataType", ATTRIBUTES:{acceptUnknownProperty:{}, defaultDisplayProperty:{}, acceptValidationState:{defaultValue:"ok"}, propertyDefs:{setter:function (attr, value) {
    if (value) {
        for (var i = 0; i < value.length; i++) {
            this.addPropertyDef(value[i]);
        }
    }
}}}, EVENTS:{beforeCurrentChange:{}, onCurrentChange:{}, beforeInsert:{}, onInsert:{}, beforeRemove:{}, onRemove:{}, beforeDataChange:{}, onDataChange:{}, beforeStateChange:{}, onStateChange:{}, onEntityToText:{}}, constructor:function (config) {
    this._propertyDefs = new dorado.util.KeyedArray(function (propertyDef) {
        return propertyDef._name;
    });
    $invokeSuper.call(this, arguments);
}, addPropertyDef:function (propertyDef) {
    if (propertyDef instanceof dorado.PropertyDef) {
        if (propertyDef._parent) {
            var parent = propertyDef._parent;
            if (parent.getPropertyDef(propertyDef._name) == propertyDef) {
                parent._propertyDefs.remove(propertyDef);
            }
        }
    } else {
        propertyDef = dorado.Toolkits.createInstance("propertydef", propertyDef);
    }
    propertyDef._parent = this;
    this._propertyDefs.append(propertyDef);
    return propertyDef;
}, getPropertyDef:function (name) {
    return this._propertyDefs.get(name);
}, parse:function (data) {
    if (data != null) {
        return (data instanceof dorado.Entity) ? data : new dorado.Entity(data, this._dataTypeRepository, this);
    } else {
        return null;
    }
}, extend:function (config) {
    if (typeof config == "string") {
        config = {name:config};
    } else {
        config = config || {};
    }
    var self = this;
    jQuery(["acceptUnknownProperty", "tag"]).each(function (i, p) {
        if (config[p] === undefined) {
            config[p] = self.get(p);
        }
    });
    var newDataType = new this.constructor(config);
    newDataType._events = dorado.Core.clone(this._events);
    this._propertyDefs.each(function (pd) {
        newDataType.addPropertyDef(dorado.Core.clone(pd));
    });
    return newDataType;
}});
(function () {
    dorado.datatype = {};
    var DataType = dorado.DataType;
    DataType.STRING = 1;
    DataType.PRIMITIVE_INT = 2;
    DataType.INTEGER = 3;
    DataType.PRIMITIVE_FLOAT = 4;
    DataType.FLOAT = 5;
    DataType.PRIMITIVE_BOOLEAN = 6;
    DataType.BOOLEAN = 7;
    DataType.DATE = 8;
    dorado.datatype.StringDataType = $extend(DataType, {$className:"dorado.datatype.StringDataType", _code:DataType.STRING, parse:function (data, argument) {
        return (data == null) ? null : (data + "");
    }, toText:function (data, argument) {
        return (data == null) ? "" : data + "";
    }});
    dorado.$String = new dorado.datatype.StringDataType("String");
    $parseFloat = dorado.util.Common.parseFloat;
    $parseInt = function (s) {
        return Math.round($parseFloat(s));
    };
    $formatFloat = dorado.util.Common.formatFloat;
    dorado.datatype.PrimitiveIntDataType = $extend(DataType, {$className:"dorado.datatype.PrimitiveIntDataType", _code:DataType.PRIMITIVE_INT, parse:function (data, argument) {
        var n = $parseInt(data);
        return (isNaN(n)) ? 0 : n;
    }, toText:$formatFloat});
    dorado.$int = new dorado.datatype.PrimitiveIntDataType("int");
    dorado.datatype.IntegerDataType = $extend(DataType, {$className:"dorado.datatype.IntegerDataType", _code:DataType.INTEGER, parse:function (data, argument) {
        var n = $parseInt(data);
        return (isNaN(n)) ? null : n;
    }, toText:$formatFloat});
    dorado.$Integer = new dorado.datatype.IntegerDataType("Integer");
    dorado.datatype.PrimitiveFloatDataType = $extend(DataType, {$className:"dorado.datatype.PrimitiveFloatDataType", _code:DataType.PRIMITIVE_FLOAT, parse:function (data, argument) {
        var n = $parseFloat(data);
        return (isNaN(n)) ? 0 : n;
    }, toText:$formatFloat});
    dorado.$float = new dorado.datatype.PrimitiveFloatDataType("float");
    dorado.datatype.FloatDataType = $extend(DataType, {$className:"dorado.datatype.FloatDataType", _code:DataType.FLOAT, parse:function (data, argument) {
        var n = $parseFloat(data);
        return (isNaN(n)) ? null : n;
    }, toText:$formatFloat});
    dorado.$Float = new dorado.datatype.FloatDataType("Float");
    function parseBoolean(data, argument) {
        if (argument == null) {
            if (data == null) {
                return false;
            }
            if (data.constructor == String) {
                return (data.toLowerCase() == "true");
            } else {
                return !!data;
            }
        } else {
            return (data === argument);
        }
    }
    dorado.datatype.PrimitiveBooleanDataType = $extend(DataType, {$className:"dorado.datatype.PrimitiveBooleanDataType", _code:DataType.PRIMITIVE_BOOLEAN, parse:parseBoolean});
    dorado.$boolean = new dorado.datatype.PrimitiveBooleanDataType("boolean");
    dorado.datatype.BooleanDataType = $extend(DataType, {$className:"dorado.datatype.BooleanDataType", _code:DataType.BOOLEAN, parse:function (data, argument) {
        if (data == null) {
            return null;
        }
        return parseBoolean(data, argument);
    }});
    dorado.$Boolean = new dorado.datatype.BooleanDataType("Boolean");
    dorado.datatype.DateDataType = $extend(DataType, {$className:"dorado.datatype.DateDataType", _code:DataType.DATE, parse:function (data, argument) {
        if (data == null || data == "") {
            return null;
        }
        if (data instanceof Date) {
            return data;
        }
        if (isFinite(data)) {
            return new Date(data);
        }
        var date = Date.parseDate(data, argument || $setting["common.defaultDateFormat"]);
        if (date == null) {
            var format = $setting["common.defaultTimeFormat"];
            if (format) {
                date = Date.parseDate(data, format);
                if (date == null) {
                    data = new Date(data);
                }
            }
        }
        if (date == null) {
            throw new dorado.ResourceException("dorado.data.BadDateFormat", data);
        }
        return date;
    }, toText:function (data, argument) {
        return (data != null && data instanceof Date) ? data.dateFormat(argument || $setting["common.defaultDateFormat"]) : "";
    }});
    dorado.$Date = new dorado.datatype.DateDataType("Date");
})();
(function () {
    var hasRespositoryListener = false;
    function newAggDataType(name, subId) {
        var dataType = new AggregationDataType(name, dorado.LazyLoadDataType.create(this, subId));
        this.register(dataType);
        return dataType;
    }
    dorado.LazyLoadDataType = $class({$className:"dorado.LazyLoadDataType", constructor:function (dataTypeRepository, id) {
        this.dataTypeRepository = dataTypeRepository;
        this.id = id;
    }, get:function (loadMode) {
        return this.dataTypeRepository.get(this.id, loadMode);
    }, getAsync:function (loadMode, callback) {
        this.dataTypeRepository.getAsync(this.id, callback, loadMode);
    }, toString:function () {
        return dorado.defaultToString(this);
    }});
    dorado.LazyLoadDataType.create = function (dataTypeRepository, id) {
        var name = dorado.DataUtil.extractNameFromId(id);
        var origin = dataTypeRepository._get(name);
        if (origin instanceof dorado.DataType) {
            return origin;
        } else {
            if (origin && origin != DataTypeRepository.UNLOAD_DATATYPE) {
                return dataTypeRepository.get(name);
            } else {
                var subId = dorado.DataType.getSubName(id);
                if (subId) {
                    var aggDataType = newAggDataType.call(dataTypeRepository, name, subId);
                    aggDataType.set("id", id);
                    return aggDataType;
                } else {
                    dataTypeRepository.register(name);
                    return new dorado.LazyLoadDataType(dataTypeRepository, id);
                }
            }
        }
    };
    dorado.LazyLoadDataType.dataTypeTranslator = function (dataType, loadMode) {
        if (dataType.constructor == String) {
            var repository;
            if (this.getDataTypeRepository) {
                repository = this.getDataTypeRepository();
            } else {
                if (this.ATTRIBUTES && this.ATTRIBUTES.dataTypeRepository) {
                    repository = this.get("dataTypeRepository");
                }
            }
            if (!repository) {
                repository = dorado.DataTypeRepository.ROOT;
            }
            if (repository) {
                dataType = dorado.LazyLoadDataType.create(repository, dataType);
            } else {
                throw new dorado.ResourceException("dorado.data.RepositoryUndefined");
            }
        }
        loadMode = loadMode || "always";
        if (loadMode == "always") {
            if (dataType instanceof dorado.LazyLoadDataType) {
                dataType = dataType.get();
            }
        } else {
            if (loadMode == "auto") {
                if (dataType instanceof dorado.LazyLoadDataType) {
                    dataType.getAsync();
                }
            }
        }
        if (!(dataType instanceof dorado.DataType)) {
            dataType = null;
        }
        return dataType;
    };
    dorado.LazyLoadDataType.dataTypeGetter = function (loadMode) {
        var dataType = this._dataType;
        if (dataType != null) {
            dataType = dorado.LazyLoadDataType.dataTypeTranslator.call(this, dataType, loadMode);
            if (dataType instanceof dorado.DataType) {
                this._dataType = dataType;
            }
        }
        return dataType;
    };
    dorado.DataTypePipe = $extend(dorado.DataPipe, {constructor:function (dataTypeRepository, id) {
        this.dataTypeRepository = dataTypeRepository || $dataTypeRepository;
        this.loadOptions = dataTypeRepository.loadOptions;
        this.id = id;
        this.name = dorado.DataUtil.extractNameFromId(id);
    }, getAjaxOptions:function () {
        var dataTypeRepository = this.dataTypeRepository;
        return dorado.Object.apply({jsonData:{action:"load-datatype", dataType:[this.id], context:(dataTypeRepository.view ? dataTypeRepository.view.get("context") : null)}}, this.loadOptions);
    }, doGet:function () {
        return this.doGetAsync();
    }, doGetAsync:function (callback) {
        var ajax = dorado.Toolkits.getAjax(this.loadOptions), dataTypeRepository = this.dataTypeRepository;
        if (callback) {
            dataTypeRepository.register(this.name, this);
            ajax.request(this.getAjaxOptions(), {scope:this, callback:function (success, result) {
                if (success) {
                    var json = result.getJsonData(), dataTypeJson, context;
                    if (json.$context) {
                        dataTypeJson = json.data;
                        context = json.$context;
                    } else {
                        dataTypeJson = json;
                    }
                    if (dataTypeRepository.parseJsonData(dataTypeJson) > 0) {
                        var dataType = dataTypeRepository._dataTypeMap[this.name];
                        $callback(callback, true, dataType, {scope:this});
                    }
                    if (context && dataTypeRepository.view) {
                        dataTypeRepository.view.set("context", context);
                    }
                } else {
                    $callback(callback, false, result.error, {scope:this});
                }
            }});
        } else {
            dataTypeRepository.unregister(this.name);
            var result = ajax.requestSync(this.getAjaxOptions());
            var dataType;
            if (dataTypeRepository.parseJsonData(result.getJsonData()) > 0) {
                dataType = dataTypeRepository._dataTypeMap[this.name];
            }
            if (!dataType) {
                throw new dorado.ResourceException("dorado.data.DataTypeLoadFailed", this.name);
            }
            return dataType;
        }
    }});
    dorado.DataTypeRepository = DataTypeRepository = $extend(dorado.EventSupport, {$className:"dorado.DataTypeRepository", EVENTS:{onDataTypeRegister:{interceptor:function (superFire, self, arg) {
        var retval = superFire(self, arg);
        if (retval !== false) {
            for (var i = 0; i < this.children.length; i++) {
                this.children[i].fireEvent(self, arg);
            }
        }
        return retval;
    }}}, constructor:function (parent) {
        this._dataTypeMap = {};
        this.parent = parent;
        if (parent) {
            parent.children.push(this);
        }
        this.children = [];
        this.loadOptions = dorado.Object.apply({}, $setting["ajax.dataTypeRepositoryOptions"]);
    }, destroy:function () {
        if (this.parent) {
            this.parent.children.remove(this);
        }
    }, addListener:function () {
        hasRespositoryListener = true;
        return $invokeSuper.call(this, arguments);
    }, parseSingleDataType:function (jsonData) {
        var dataType, name = jsonData.name, type = jsonData.$type;
        delete jsonData.name;
        delete jsonData.$type;
        if (type == "Aggregation") {
            dataType = new dorado.AggregationDataType(name);
        } else {
            dataType = new dorado.EntityDataType(name);
        }
        if (dataType) {
            dataType.loadFromServer = true;
            dataType._dataTypeRepository = this;
            dataType.set(jsonData);
        }
        return dataType;
    }, parseJsonData:function (jsonData) {
        var n = 0, dataTypeMap = this._dataTypeMap, dataType;
        if (jsonData instanceof Array) {
            n = jsonData.length;
            for (var i = 0; i < n; i++) {
                this.register(this.parseSingleDataType(jsonData[i]));
            }
        } else {
            this.register(this.parseSingleDataType(jsonData));
            n++;
        }
        return n;
    }, register:function (name, dataType) {
        if (name.constructor == String) {
            dataType = dataType || DataTypeRepository.UNLOAD_DATATYPE;
        } else {
            dataType = name;
            name = name._name;
        }
        if (this._dataTypeMap[name] instanceof dorado.DataType) {
            return;
        }
        this._dataTypeMap[name] = dataType;
        if (dataType instanceof dorado.DataType) {
            dataType._dataTypeRepository = this;
            if (hasRespositoryListener) {
                this.fireEvent("onDataTypeRegister", this, {dataType:dataType});
            }
        }
    }, unregister:function (name) {
        delete this._dataTypeMap[name];
    }, _get:function (name) {
        var dataType = this._dataTypeMap[name];
        if (!dataType && this.parent) {
            dataType = this.parent._get(name);
        }
        return dataType;
    }, get:function (name, loadMode) {
        var id = name, name = dorado.DataUtil.extractNameFromId(id);
        var dataType = this._get(name);
        if (dataType == DataTypeRepository.UNLOAD_DATATYPE) {
            var subId = dorado.DataType.getSubName(id);
            if (subId) {
                dataType = newAggDataType.call(this, name, subId);
                dataType.set("id", id);
            } else {
                loadMode = loadMode || "always";
                if (loadMode == "always") {
                    var pipe = new dorado.DataTypePipe(this, id);
                    dataType = pipe.get();
                } else {
                    if (loadMode == "auto") {
                        this.getAsync(id);
                    }
                    dataType = null;
                }
            }
        } else {
            if (dataType instanceof dorado.DataTypePipe) {
                var pipe = dataType;
                if (loadMode == "always") {
                    dataType = pipe.get(callback);
                } else {
                    dataType = null;
                }
            } else {
                if (!dataType) {
                    var subId = dorado.DataType.getSubName(id);
                    if (subId) {
                        dataType = newAggDataType.call(this, name, subId);
                        dataType.set("id", id);
                    }
                }
            }
        }
        return dataType;
    }, getAsync:function (name, callback, loadMode) {
        var id = name, name = dorado.DataUtil.extractNameFromId(id);
        var dataType = this._get(name);
        if (dataType == DataTypeRepository.UNLOAD_DATATYPE) {
            var subId = dorado.DataType.getSubName(id);
            if (subId) {
                dataType = newAggDataType.call(this, name, subId);
                dataType.set("id", id);
            } else {
                loadMode = loadMode || "always";
                if (loadMode != "never") {
                    var pipe = new dorado.DataTypePipe(this, id);
                    pipe.getAsync(callback);
                    return;
                }
            }
        } else {
            if (dataType instanceof dorado.DataTypePipe) {
                var pipe = dataType;
                if (loadMode != "never") {
                    pipe.getAsync(callback);
                    return;
                }
            } else {
                if (!dataType) {
                    var subId = dorado.DataType.getSubName(id);
                    if (subId) {
                        dataType = newAggDataType.call(this, name, subId);
                        dataType.set("id", id);
                    }
                }
            }
        }
        $callback(callback, true, dataType);
    }, getLoadedDataTypes:function () {
        function collect(dataTypeRepository, nameMap) {
            var map = dataTypeRepository._dataTypeMap;
            for (var name in map) {
                var dt = map[name];
                if (dt.loadFromServer && !(dt instanceof dorado.AggregationDataType)) {
                    nameMap[name] = true;
                }
            }
            if (dataTypeRepository.parent) {
                collect(dataTypeRepository.parent, nameMap);
            }
        }
        var nameMap = {}, result = [];
        collect(this, nameMap);
        for (var name in nameMap) {
            result.push(name);
        }
        return result;
    }});
    var DataType = dorado.DataType;
    var root = new DataTypeRepository();
    DataTypeRepository.ROOT = root;
    DataTypeRepository.UNLOAD_DATATYPE = {};
    window.$dataTypeRepository = DataTypeRepository.ROOT;
    var dataType = dorado.$String;
    root.register(dataType);
    dataType = dorado.$int;
    root.register("byte", dataType);
    root.register("char", dataType);
    root.register("short", dataType);
    root.register("int", dataType);
    root.register("long", dataType);
    dataType = dorado.$Integer;
    root.register("Byte", dataType);
    root.register("Character", dataType);
    root.register("Short", dataType);
    root.register("Integer", dataType);
    root.register("Long", dataType);
    dataType = dorado.$float;
    root.register("float", dataType);
    root.register("double", dataType);
    dataType = dorado.$Float;
    root.register("Float", dataType);
    root.register("Double", dataType);
    root.register("BigDecimal", dataType);
    dataType = dorado.$boolean;
    root.register(dataType);
    dataType = dorado.$Boolean;
    root.register(dataType);
    dataType = dorado.$Date;
    root.register("Date", dataType);
    root.register("Calendar", dataType);
    var AggregationDataType = dorado.AggregationDataType;
    root.register(new AggregationDataType("List"));
    root.register(new AggregationDataType("Set"));
    root.register(new AggregationDataType("Array"));
    var EntityDataType = dorado.EntityDataType;
    root.register(new EntityDataType("Bean"));
    root.register(new EntityDataType("Map"));
    root.register(new EntityDataType("Entity"));
})();
(function () {
    dorado.PropertyDef = $extend(dorado.AttributeSupport, {$className:"dorado.PropertyDef", ATTRIBUTES:{name:{readOnly:true}, parent:{readOnly:true}, dataType:{getter:dorado.LazyLoadDataType.dataTypeGetter, writeOnce:true}, label:{getter:function () {
        var label = this._label;
        if (label == null) {
            label = this._name;
        }
        return label;
    }}, description:{}, readOnly:{}, visible:{defaultValue:true}, key:{}, typeFormat:{}, displayFormat:{}, mapping:{setter:function (attr, mapping) {
        this._mapping = mapping;
        if (mapping && mapping.length > 0) {
            var index = this._mappingIndex = {};
            for (var i = 0; i < mapping.length; i++) {
                var key = mapping[i].key;
                if (key == null) {
                    key = "${null}";
                } else {
                    if (key === "") {
                        key = "${empty}";
                    }
                }
                index[key + ""] = mapping[i].value;
            }
        } else {
            delete this._mappingIndex;
        }
        delete this._mappingRevIndex;
    }}, acceptUnknownMapKey:{defaultValue:true}, submitable:{defaultValue:true}, required:{}, defaultValue:{}, validators:{setter:function (attr, value) {
        var validators = [];
        for (var i = 0; i < value.length; i++) {
            var v = value[i];
            if (!(v instanceof dorado.validator.Validator)) {
                v = dorado.Toolkits.createInstance("validator", v);
            }
            validators.push(v);
        }
        this._validators = validators;
    }}, dataTypeRepository:{getter:function (attr) {
        var parent = this.get("parent");
        return (parent) ? parent.get(attr) : null;
    }, readOnly:true}, cacheable:{}}, constructor:function (name, dataType) {
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
    }, getDataType:function (loadMode) {
        return dorado.LazyLoadDataType.dataTypeGetter.call(this, loadMode);
    }, getMappedValue:function (key) {
        if (key == null) {
            key = "${null}";
        } else {
            if (key === "") {
                key = "${empty}";
            }
        }
        return this._mappingIndex ? this._mappingIndex[key + ""] : undefined;
    }, getMappedKey:function (value) {
        if (!this._mappingRevIndex) {
            var index = this._mappingRevIndex = {}, mapping = this._mapping;
            for (var i = 0; i < mapping.length; i++) {
                var v = mapping[i].value;
                if (v == null) {
                    v = "${null}";
                } else {
                    if (v === "") {
                        v = "${empty}";
                    }
                }
                index[v + ""] = mapping[i].key;
            }
        }
        if (value == null) {
            value = "${null}";
        } else {
            if (value === "") {
                value = "${empty}";
            }
        }
        return this._mappingRevIndex[value + ""];
    }});
    dorado.BasePropertyDef = $extend(dorado.PropertyDef, {$className:"dorado.BasePropertyDef", ATTRIBUTES:{key:{}}});
    dorado.ReferenceDataPipe = $extend(dorado.DataProviderPipe, {$className:"dorado.ReferenceDataPipe", constructor:function (propertyDef, entity) {
        this.propertyDef = propertyDef;
        this.entity = entity;
        this.dataType = propertyDef._dataType;
        var parent = propertyDef.get("parent");
        this.dataTypeRepository = (parent ? parent.get("dataTypeRepository") : null) || $dataTypeRepository;
        this.view = this.dataTypeRepository ? this.dataTypeRepository.view : null;
    }, getDataProviderArg:function () {
        dorado.$this = this.entity;
        return {pageSize:this.propertyDef._pageSize, parameter:dorado.JSON.evaluate(this.propertyDef._parameter), dataType:this.dataType, view:this.view};
    }, getDataProvider:function () {
        return this.propertyDef._dataProvider;
    }});
    dorado.Reference = $extend(dorado.PropertyDef, {$className:"dorado.Reference", ATTRIBUTES:{dataProvider:{}, parameter:{}, pageSize:{}, cacheable:{defaultValue:true}}, getDataPipe:function (entity) {
        return new dorado.ReferenceDataPipe(this, entity);
    }});
    var lookupIndexs = {};
    dorado.LookupDataPipe = $extend(dorado.DataPipe, {$className:"dorado.LookupDataPipe", constructor:function (propertyDef, entity) {
        this.propertyDef = propertyDef;
        this.entity = entity;
        this.dataType = propertyDef._dataType;
        var parent = propertyDef.get("parent");
        this.dataTypeRepository = (parent ? parent.get("dataTypeRepository") : null) || $dataTypeRepository;
    }, getLookupData:function () {
        var data = this.propertyDef.lookupData;
        if (!data) {
            var propDef = this.propertyDef;
            if (propDef._dataSet) {
                data = propDef._dataSet.getData();
            } else {
                if (propDef._dataProvider) {
                    data = propDef._dataProvider.getResult();
                }
            }
        }
        return this.propertyDef.lookupData = data;
    }, getLookupDataAsync:function (callback) {
        if (!this.propertyDef.lookupData) {
            var propDef = this.propertyDef;
            var callbackObj = {scope:this, callback:function (success, result) {
                if (success) {
                    this.propertyDef.lookupData = result;
                }
                $callback(callback, success, result);
            }};
            if (propDef._dataSet) {
                propDef._dataSet.getDataAsync(callbackObj);
            } else {
                if (propDef._dataProvider) {
                    propDef._dataProvider.getResultAsync(callbackObj);
                }
            }
        } else {
            $callback(callback, true, this.propertyDef.lookupData);
        }
    }, getIndex:function () {
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
                propDef.cacheKey += "|" + propDef._lookupProperty + "|";
                var constraints = propDef._constraints;
                for (var i = 0; i < constraints.length; i++) {
                    var constraint = constraints[i];
                    propDef.cacheKey += "," + constraint.lookupKeyProperty;
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
                } else {
                    if (data instanceof dorado.EntityList) {
                        for (var it = data.iterator(); it.hasNext(); ) {
                            var entity = it.next();
                            index[getKey.call(this, entity)] = (path) ? path.evaluate(entity, true) : entity;
                        }
                    } else {
                        index[getKey.call(this, data)] = (path) ? path.evaluate(data, true) : data;
                    }
                }
            }
        }
        return index;
    }, getKey:function (entity) {
        dorado.$this = entity;
        var constraints = this.propertyDef._constraints;
        var keys = [];
        for (var i = 0; i < constraints.length; i++) {
            var constraint = constraints[i];
            if (constraint.keyProperty) {
                keys.push(entity.get(constraint.keyProperty));
            } else {
                keys.push(dorado.JSON.evaluate(constraint.keyValue));
            }
        }
        return keys.join("^^");
    }, doGet:function () {
        this.getLookupData();
        var key = this.getKey(this.entity);
        return this.getIndex()[key ? key : ""];
    }, doGetAsync:function (callback) {
        this.getLookupDataAsync({scope:this, callback:function (success, result) {
            if (success) {
                var key = this.getKey(this.entity);
                var result = this.getIndex()[key ? key : ""];
            }
            $callback(callback, success, result);
        }});
    }});
    dorado.Lookup = $extend(dorado.PropertyDef, {$className:"dorado.Lookup", ATTRIBUTES:{dataProvider:{}, dataSet:{}, lookupProperty:{}, constraints:{}}, constructor:function (name, dataType) {
        this._constraints = [];
        $invokeSuper.call(this, arguments);
    }, getDataPipe:function (entity) {
        return new dorado.LookupDataPipe(this, entity);
    }, clearCache:function () {
        delete this.cacheKey;
    }});
    dorado.Toolkits.registerPrototype("propertydef", {"Default":dorado.BasePropertyDef, "Reference":dorado.Reference, "Lookup":dorado.Lookup});
})();
(function () {
    var DEFAULT_VALIDATION_RESULT_STATE = "error";
    var STATE_CODE = dorado.Toolkits.STATE_CODE;
    var VALIDATION_RESULT_CODE = {ok:0, invalid:1, executing:2};
    dorado.Entity = function (data, dataTypeRepository, dataType) {
        this.entityId = dorado.Core.getTimestamp() + "";
        this.timestamp = dorado.Core.getTimestamp();
        this.dataTypeRepository = dataTypeRepository;
        this._propertyInfoMap = {};
        if (data) {
            this._data = data;
            if (dataType == null) {
                if (dataTypeRepository && data.$dataType) {
                    dataType = dataTypeRepository.get(data.$dataType);
                }
            } else {
                data.$dataType = dataType._id;
            }
        } else {
            this._data = data = {};
            if (dataType) {
                this._data.$dataType = dataType._id;
            }
        }
        this.dataType = dataType;
        if (dataType) {
            this._propertyDefs = dataType._propertyDefs;
            this._propertyDefs.each(function (pd) {
                if (pd._defaultValue != undefined && data[pd._name] == undefined) {
                    data[pd._name] = (pd._defaultValue instanceof Function) ? pd._defaultValue.call(this) : pd._defaultValue;
                }
                if (data[pd._name] == null) {
                    var dataType = pd.get("dataType");
                    if (dataType) {
                        switch (dataType._code) {
                          case dorado.DataType.PRIMITIVE_INT:
                          case dorado.DataType.PRIMITIVE_FLOAT:
                            data[pd._name] = 0;
                            break;
                          case dorado.DataType.PRIMITIVE_BOOLEAN:
                            data[pd._name] = false;
                            break;
                        }
                    }
                }
            });
        } else {
            this._propertyDefs = null;
        }
        if (this.acceptUnknownProperty == null) {
            this.acceptUnknownProperty = (dataType) ? dataType._acceptUnknownProperty : true;
        }
    };
    dorado.Entity.STATE_NONE = 0;
    dorado.Entity.STATE_NEW = 1;
    dorado.Entity.STATE_MODIFIED = 2;
    dorado.Entity.STATE_DELETED = 3;
    dorado.Entity._MESSAGE_DATA_CHANGED = 3;
    dorado.Entity._MESSAGE_ENTITY_STATE_CHANGED = 4;
    dorado.Entity._MESSAGE_REFRESH_ENTITY = 5;
    $class({$className:"dorado.Entity", constructor:dorado.Entity, state:dorado.Entity.STATE_NONE, _observer:null, _disableObserversCounter:0, _messages:null, _setObserver:function (observer) {
        this._observer = observer;
        var data = this._data;
        for (p in data) {
            if (data.hasOwnProperty(p)) {
                var v = data[p];
                if (v == null) {
                    continue;
                }
                if (v instanceof dorado.Entity || v instanceof dorado.EntityList) {
                    v._setObserver(observer);
                }
            }
        }
    }, disableObservers:function () {
        this._disableObserversCounter++;
    }, enableObservers:function () {
        if (this._disableObserversCounter > 0) {
            this._disableObserversCounter--;
        }
    }, notifyObservers:function () {
        this.sendMessage(0);
    }, sendMessage:function (messageCode, arg) {
        if (this._disableObserversCounter == 0 && this._observer) {
            this._observer.entityMessageReceived(messageCode, arg);
        }
    }, setState:function (state) {
        if (this.state == state) {
            return;
        }
        var eventArg = {entity:this, oldState:this.state, newState:state, processDefault:true};
        var dataType = this.dataType;
        if (dataType && !this.disableEvents) {
            dataType.fireEvent("beforeStateChange", dataType, eventArg);
        }
        if (!eventArg.processDefault) {
            return;
        }
        if (this.state == dorado.Entity.STATE_NONE && state == dorado.Entity.STATE_MODIFIED) {
            this.storeOldData();
        }
        this.state = state;
        this.timestamp = dorado.Core.getTimestamp();
        if (dataType && !this.disableEvents) {
            dataType.fireEvent("onStateChange", dataType, eventArg);
        }
        this.sendMessage(dorado.Entity._MESSAGE_ENTITY_STATE_CHANGED, eventArg);
    }, _get:function (property, propertyDef, callback, loadMode) {
        function transferAndReplaceIf(propertyDef, value) {
            if (value && typeof value == "object" && value.parent == this) {
                return value;
            }
            var dataType = propertyDef.get("dataType");
            if (dataType == null) {
                return value;
            }
            var replaceValue = (!(value instanceof dorado.EntityList) && dataType instanceof dorado.AggregationDataType) || (!(value instanceof dorado.Entity) && dataType instanceof dorado.EntityDataType);
            value = dataType.parse(value, propertyDef.get("typeFormat"));
            if ((value instanceof dorado.Entity || value instanceof dorado.EntityList) && value.parent != this) {
                value.parent = this;
                value._setObserver(this._observer);
            }
            if (replaceValue) {
                this._data[propertyDef._name] = value;
            }
            return value;
        }
        var value = this._data[property];
        if (value === undefined) {
            if (propertyDef) {
                var dataPipeWrapper = null;
                if (loadMode != "never" && propertyDef.getDataPipe) {
                    var pipe = propertyDef.getDataPipe(this);
                    if (pipe) {
                        if (callback || loadMode == "auto") {
                            pipe.getAsync({scope:this, callback:function (success, result) {
                                if (success) {
                                    if (propertyDef.get("cacheable")) {
                                        var oldValue = this._data[property];
                                        if (oldValue && oldValue.isDataPipeWrapper) {
                                            oldValue = oldValue.value;
                                        }
                                        if (oldValue instanceof dorado.Entity || oldValue instanceof dorado.EntityList) {
                                            oldValue.parent = null;
                                            oldValue._setObserver(null);
                                        }
                                        this._data[property] = result;
                                        this.sendMessage(dorado.Entity._MESSAGE_DATA_CHANGED, {entity:this, property:property, newValue:result});
                                    }
                                }
                                if (callback) {
                                    $callback(callback, success, result);
                                }
                            }});
                            this._data[property] = dataPipeWrapper = {isDataPipeWrapper:true, pipe:pipe};
                            if (callback) {
                                return;
                            }
                        } else {
                            value = pipe.get();
                            if (propertyDef._cacheable) {
                                this._data[property] = value;
                            }
                        }
                    }
                }
                if (dorado.Entity.ALWAYS_RETURN_VALID_ENTITY_LIST) {
                    var aggregationDataType = propertyDef.get("dataType");
                    if (value === undefined && aggregationDataType instanceof dorado.AggregationDataType) {
                        value = aggregationDataType.parse([], propertyDef.get("typeFormat"));
                        value.parent = this;
                        value._setObserver(this._observer);
                        if (dataPipeWrapper) {
                            dataPipeWrapper.value = value;
                        } else {
                            if (loadMode != "never") {
                                this._data[property] = value;
                            }
                        }
                    }
                }
            }
        } else {
            if (value != null && value.isDataPipeWrapper) {
                var pipe = value.pipe;
                if (loadMode != "never") {
                    if (callback || loadMode == "auto") {
                        pipe.getAsync(callback);
                        if (callback) {
                            return;
                        }
                    } else {
                        value = pipe.get();
                    }
                }
            } else {
                if (propertyDef) {
                    value = transferAndReplaceIf.call(this, propertyDef, value);
                }
            }
        }
        if (callback) {
            $callback(callback, true, value);
        }
        return value;
    }, _getPropertyDef:function (property) {
        var propertyDef = null;
        if (!this.acceptUnknownProperty && this._propertyDefs) {
            propertyDef = this._propertyDefs.get(property);
            if (!propertyDef) {
                throw new dorado.ResourceException("dorado.data.UnknownProperty", property);
            }
        }
        return propertyDef;
    }, get:function (property, loadMode) {
        loadMode = loadMode || "always";
        var properties = property.split("."), result;
        for (var i = 0; i < properties.length; i++) {
            property = properties[i];
            if (i == 0) {
                var propertyDef = this._getPropertyDef(property);
                result = this._get(property, propertyDef, null, loadMode);
            } else {
                if (!result) {
                    break;
                }
                result = (result instanceof dorado.Entity) ? result.get(property) : result[property];
            }
        }
        return result;
    }, getAsync:function (property, callback, loadMode) {
        loadMode = loadMode || "always";
        var properties = property.split("."), index = 0, result;
        var property = properties[index];
        var propertyDef = this._getPropertyDef(property);
        if (properties.length == 1) {
            this._get(property, propertyDef, callback || dorado._NULL_FUNCTION, loadMode);
        } else {
            this._get(property, propertyDef, {callback:function (success, result) {
                if (success) {
                    var subProperty = properties[++index];
                    if (result instanceof dorado.Entity) {
                        result.getAsync(subProperty, callback, loadMode);
                        return;
                    } else {
                        result = result[subProperty];
                    }
                    return;
                }
                $callback(callback, success, result);
            }}, loadMode);
        }
    }, doGetText:function (property, callback, loadMode) {
        function toText(value, propertyDef) {
            var text;
            if (propertyDef) {
                var dataType = propertyDef.get("dataType");
                if (dataType) {
                    text = dataType.toText(value, propertyDef._displayFormat);
                } else {
                    text = dorado.$String.toText(value);
                }
                if (text && propertyDef._mapping) {
                    text = propertyDef.getMappedValue(text);
                }
            } else {
                text = dorado.$String.toText(value);
            }
            return text;
        }
        var propertyDef = this._getPropertyDef(property);
        if (callback) {
            this.get(property, function (value) {
                $callback(callback, true, toText(value, propertyDef));
            }, loadMode);
        } else {
            var value = this.get(property, loadMode);
            return toText(value, propertyDef);
        }
    }, getText:function (property, loadMode) {
        return this.doGetText(property, null, loadMode);
    }, getTextAsync:function (property, callback, loadMode) {
        this.doGetText(property, callback || dorado._NULL_FUNCTION, loadMode);
    }, storeOldData:function () {
        if (this._oldData) {
            return;
        }
        var data = this._data, oldData = this._oldData = {};
        for (var p in data) {
            if (data.hasOwnProperty(p)) {
                oldData[p] = data[p];
            }
        }
    }, _set:function (property, value, propertyDef) {
        var oldValue = this._data[property];
        var eventArg = {entity:this, property:property, oldValue:oldValue, newValue:value, processDefault:true};
        var dataType = this.dataType;
        if (dataType && !this.disableEvents && dataType.fireEvent("beforeDataChange", dataType, eventArg)) {
            value = eventArg.newValue;
        }
        if (!eventArg.processDefault) {
            return;
        }
        if (this.state == dorado.Entity.STATE_NONE) {
            this.storeOldData();
        }
        if (oldValue && oldValue.isDataPipeWrapper) {
            oldValue = oldValue.value;
        }
        if (oldValue instanceof dorado.Entity || oldValue instanceof dorado.EntityList) {
            oldValue.parent = null;
            oldValue._setObserver(null);
        }
        if (value instanceof dorado.Entity || value instanceof dorado.EntityList) {
            if (value.parent != null) {
                throw new dorado.ResourceException("dorado.data.ValueNotFree", ((value instanceof dorado.Entity) ? "Entity" : "EntityList"));
            }
            value.parent = this;
            value._setObserver(this._observer);
        }
        this._data[property] = value;
        this.timestamp = dorado.Core.getTimestamp();
        var messages = [];
        if (propertyDef) {
            if (propertyDef._required && (!value && value !== false)) {
                messages.push({state:"error", text:$resource("dorado.data.ErrorContentRequired")});
            } else {
                if (propertyDef._mapping && value != null) {
                    var mappedValue = propertyDef.getMappedValue(value);
                    if (propertyDef._acceptUnknownMapKey && mappedValue === undefined) {
                        messages.push({state:"error", text:$resource("dorado.data.ErrorContentRequired")});
                    }
                } else {
                    var propertyInfoMap = this._propertyInfoMap, propertyInfo = propertyInfoMap[property];
                    if (!propertyInfo) {
                        propertyInfoMap[property] = propertyInfo = {};
                    }
                    if (propertyDef._validators) {
                        var entity = this, currentTimestamp = this.timestamp, asyncValidateActions = [];
                        jQuery.each(propertyDef._validators, function (i, validator) {
                            if (validator instanceof dorado.validator.RemoteValidator && validator._async) {
                                asyncValidateActions.push(function (callback) {
                                    validator.validate(value, {callback:function (success, result) {
                                        if (success) {
                                            if (entity.timestamp != currentTimestamp) {
                                                return;
                                            }
                                            entity.setMessages(property, result);
                                        }
                                        $callback(callback, success, result);
                                    }});
                                });
                            } else {
                                messages = validator.validate(value);
                            }
                        });
                        if (asyncValidateActions.length) {
                            propertyInfo.validating = asyncValidateActions.length;
                            $waitFor(asyncValidateActions, function () {
                                if (entity.timestamp != currentTimestamp) {
                                    return;
                                }
                                propertyInfo.validating = 0;
                                propertyInfo.validated = true;
                            });
                        } else {
                            propertyInfo.validated = true;
                        }
                    } else {
                        propertyInfo.validated = true;
                    }
                }
            }
        }
        this.doSetMessages(property, (messages && messages.length) ? messages : null);
        if (this.state == dorado.Entity.STATE_NONE) {
            this.setState(dorado.Entity.STATE_MODIFIED);
        }
        if (dataType && !this.disableEvents) {
            dataType.fireEvent("onDataChange", dataType, eventArg);
        }
        this.sendMessage(dorado.Entity._MESSAGE_DATA_CHANGED, eventArg);
    }, set:function (property, value) {
        var propertyDef = this._getPropertyDef(property);
        if (propertyDef) {
            var dataType = propertyDef.get("dataType");
            if (dataType) {
                value = dataType.parse(value, propertyDef._typeFormat);
            }
        }
        this._set(property, value, propertyDef);
    }, setText:function (property, text) {
        var propertyDef = this._getPropertyDef(property), value = text;
        if (propertyDef) {
            if (propertyDef._mapping && text != null) {
                value = propertyDef.getMappedKey(text);
                if (value === undefined) {
                    value = text;
                }
            }
            var dataType = propertyDef.get("dataType");
            if (dataType) {
                value = dataType.parse(value, propertyDef._displayFormat);
            }
        }
        this._set(property, value, propertyDef);
    }, cancel:function () {
        if (this.state == dorado.Entity.STATE_NEW) {
            this.remove();
        } else {
            if (this.state != dorado.Entity.STATE_NONE) {
                var data = this._data, oldData = this._oldData;
                if (oldData) {
                    for (var p in data) {
                        if (data.hasOwnProperty(p)) {
                            delete data[p];
                        }
                    }
                    for (var p in oldData) {
                        if (oldData.hasOwnProperty(p)) {
                            data[p] = oldData[p];
                        }
                    }
                }
                this.reset();
                this.sendMessage(0);
            }
        }
    }, reset:function () {
        this._propertyInfoMap = {};
        delete this._messages;
        delete this._messageState;
        this.setState(dorado.Entity.STATE_NONE);
        delete this._oldData;
    }, createBrother:function (data, detached) {
        var brother = new dorado.Entity(data, this.dataTypeRepository, this.dataType);
        if (!detached && this.parent instanceof dorado.EntityList) {
            this.parent.insert(brother);
        }
        return brother;
    }, createChild:function (property, data, detached) {
        var child = null;
        if (this.dataType) {
            var propertyDef = this._getPropertyDef(property);
            if (!propertyDef) {
                throw new dorado.ResourceException("dorado.data.UnknownProperty", property);
            }
            var elementDataType = propertyDef.get("dataType"), aggregationDataType;
            if (elementDataType && elementDataType instanceof dorado.AggregationDataType) {
                aggregationDataType = elementDataType;
                elementDataType = elementDataType.getElementDataType();
            }
            if (elementDataType && !(elementDataType instanceof dorado.EntityDataType)) {
                throw new ResourceException("dorado.data.EntityPropertyExpected", property);
            }
            child = new dorado.Entity(data, this.dataTypeRepository, elementDataType);
            if (!detached) {
                if (aggregationDataType) {
                    var list = this._get(property, propertyDef);
                    list.insert(child);
                } else {
                    this._set(property, child, propertyDef);
                }
            }
        } else {
            child = new dorado.Entity(data);
            if (!detached) {
                var oldChild = this.get(property);
                if (oldChild instanceof dorado.EntityList) {
                    oldChild.insert(child);
                } else {
                    if (oldChild instanceof Array) {
                        oldChild.push(child);
                    } else {
                        this.set(property, child);
                    }
                }
            }
        }
        return child;
    }, setCurrent:function () {
        if (this.parent instanceof dorado.EntityList) {
            this.parent.setCurrent(this);
        }
    }, clearData:function () {
        var data = this._data;
        for (var property in data) {
            if (!data.hasOwnProperty(property)) {
                continue;
            }
            delete data[property];
        }
        this.sendMessage(0);
    }, fromJSON:function (json) {
        if (this.dataType) {
            json.$dataType = this.dataType._id;
        }
        this._data = json;
        delete this._oldData;
        this.state = dorado.Entity.STATE_NONE;
        this.timestamp = dorado.Core.getTimestamp();
        this.sendMessage(0);
    }, toJSON:function (options, context) {
        var result = {};
        var includeUnsubmitableProperties = includeReferenceProperties = includeLookupProperties = true, generateDataType = generateState = generateEntityId = generateOldData = false, properties = null, entityFilter = null;
        if (options != null) {
            if (options.includeUnsubmitableProperties === false) {
                includeUnsubmitableProperties = false;
            }
            if (options.includeReferenceProperties === false) {
                includeReferenceProperties = false;
            }
            if (options.includeLookupProperties === false) {
                includeLookupProperties = false;
            }
            generateDataType = options.generateDataType;
            generateState = options.generateState;
            generateEntityId = options.generateEntityId;
            generateOldData = options.generateOldData && this._oldData;
            options.generateDataType = false;
            properties = options.properties;
            entityFilter = options.entityFilter;
            if (properties != null && properties.length == 0) {
                properties = null;
            }
        }
        var data = this._data, oldData = this._oldData, oldDataHolder;
        for (var property in data) {
            if (!data.hasOwnProperty(property)) {
                continue;
            }
            if (property.charAt(0) == "$") {
                continue;
            }
            if (properties && properties.indexOf(property) < 0) {
                continue;
            }
            var propertyDef = (this._propertyDefs) ? this._propertyDefs.get(property) : null;
            if (!includeUnsubmitableProperties && propertyDef && !propertyDef._submitable) {
                continue;
            }
            if (propertyDef instanceof dorado.Reference) {
                if (!includeReferenceProperties) {
                    continue;
                }
            } else {
                if (propertyDef instanceof dorado.Lookup) {
                    if (!includeLookupProperties) {
                        continue;
                    }
                }
            }
            var value = this._get(property, propertyDef);
            if (value != null) {
                if (value instanceof dorado.Entity) {
                    if (!entityFilter || entityFilter(value)) {
                        value = value.toJSON(options, context);
                    } else {
                        value = null;
                    }
                } else {
                    if (value instanceof dorado.EntityList) {
                        value = value.toJSON(options, context);
                    }
                }
            }
            if (generateOldData && propertyDef && propertyDef._key && oldData[property] != null) {
                if (!oldDataHolder) {
                    oldDataHolder = {};
                }
                oldDataHolder[property] = oldData[property];
            }
            result[property] = value;
        }
        if (generateDataType && data.$dataType) {
            result.$dataType = data.$dataType;
        }
        if (generateState && this.state != dorado.Entity.STATE_NONE) {
            result.$state = this.state;
        }
        if (generateEntityId) {
            result.$entityId = this.entityId;
        }
        if (oldDataHolder) {
            result.$oldData = oldDataHolder;
        }
        if (options) {
            options.generateDataType = generateDataType;
        }
        if (context && context.entities) {
            context.entities.push(this);
        }
        return result;
    }, getData:function () {
        return this._data;
    }, getOldData:function () {
        return this._oldData;
    }, getMessages:function (property) {
        var results;
        if (property) {
            var obj = this._propertyInfoMap[property];
            results = ((obj) ? obj.messages : null);
        } else {
            results = this._messages;
        }
        return results;
    }, doSetMessages:function (property, messages) {
        function getMessageState(entity) {
            var state = null, stateCode = -1;
            if (entity._messages) {
                state = dorado.Toolkits.getTopMessageState(entity._messages);
                if (state) {
                    stateCode = STATE_CODE[state];
                }
            }
            var map = entity._propertyInfoMap;
            for (var p in map) {
                var obj = map[p];
                var code = STATE_CODE[obj.state];
                if (code > stateCode) {
                    stateCode = code;
                    state = obj.state;
                }
            }
            return state;
        }
        if (messages === undefined) {
            throw new dorado.ResourceException("dorado.core.OperationNotSupported", "dorado.Entity.doSetMessages");
            messages = property;
            messages = dorado.Toolkits.trimMessages(messages, DEFAULT_VALIDATION_RESULT_STATE);
            if (this._messages == messages) {
                return false;
            }
            this._messages = messages;
            if (dorado.Toolkits.getTopMessageState(messages) != this._messageState) {
                this._messageState = getMessageState(this);
            }
        } else {
            var map = this._propertyInfoMap;
            messages = dorado.Toolkits.trimMessages(messages, DEFAULT_VALIDATION_RESULT_STATE);
            var obj = map[property];
            if (obj && obj.messages == messages) {
                return false;
            }
            var state = dorado.Toolkits.getTopMessageState(messages);
            if (!obj) {
                map[property] = obj = {};
            }
            obj.state = state;
            obj.messages = messages;
            if (state != this._messageState || state != (obj ? obj.state : null)) {
                this._messageState = getMessageState(this);
            }
        }
        return true;
    }, setMessages:function (property, messages) {
        var retval = this.doSetMessages(property, messages);
        if (retval) {
            this.timestamp = dorado.Core.getTimestamp();
            if (property) {
                this.sendMessage(dorado.Entity._MESSAGE_DATA_CHANGED, {entity:this, property:property});
            } else {
                this.sendMessage(0);
            }
        }
        return retval;
    }, getMessageState:function (property) {
        if (property) {
            var map = this._propertyInfoMap;
            return map[property] ? map[property].state : null;
        } else {
            return this._messageState;
        }
    }, getValidateState:function (property) {
        var state = "unvalidate", map = this._propertyInfoMap;
        if (map) {
            var propertyInfo = map[property];
            if (propertyInfo) {
                if (propertyInfo.validating) {
                    state = "validating";
                } else {
                    if (propertyInfo.validated) {
                        state = this.getMessageState(property);
                        if (!state || state == "info") {
                            state = "ok";
                        }
                    }
                }
            }
        }
        return state;
    }, validate:function (options) {
        function addMessage2Context(context, entity, property, message) {
            var state = message.state || "error";
            context[state].push({entity:entity, property:property, state:message.state, text:message.text});
        }
        var simplePropertyOnly = options ? options.validateSimplePropertyOnly : false;
        var context = options ? options.context : null;
        var result, topResult, resultCode, topResultCode = -1;
        if (context) {
            context.info = [];
            context.ok = [];
            context.warn = [];
            context.error = [];
            context.executing = [];
            context.executingValidationNum = 0;
        }
        var hasExecutingValidator = false, message;
        var dataType = this.dataType, propertyInfoMap = this._propertyInfoMap;
        if (dataType) {
            this._propertyDefs = dataType._propertyDefs;
            var entity = this;
            this._propertyDefs.each(function (pd) {
                var property = pd._name, propertyInfo = propertyInfoMap[property];
                if (propertyInfo) {
                    if (propertyInfo.validating) {
                        hasExecutingValidator = true;
                        if (context) {
                            context.executingValidationNum = (context.executingValidationNum || 0) + propertyInfo.validating;
                            var executing = context.executing = context.executing || [];
                            executing.push({entity:this, property:property, num:propertyInfo.validating});
                        }
                    } else {
                        if (context && propertyInfo.messages) {
                            for (var i = 0; i < propertyInfo.messages.length; i++) {
                                addMessage2Context(context, this, property, propertyInfo.messages[i]);
                            }
                        }
                    }
                }
                if (pd._required && (!propertyInfo || !propertyInfo.validated)) {
                    if (!propertyInfo) {
                        propertyInfoMap[property] = propertyInfo = {};
                    }
                    propertyInfo.validated = true;
                    var value = entity._data[property];
                    if (!value && value !== false) {
                        message = {state:"error", text:$resource("dorado.data.ErrorContentRequired")};
                        entity.setMessages(property, [message]);
                        if (context) {
                            addMessage2Context(context, this, property, message);
                        }
                    }
                }
            });
        }
        if (simplePropertyOnly) {
            var data = this._data;
            for (var p in data) {
                if (!data.hasOwnProperty(p)) {
                    continue;
                }
                var value = data[p];
                if (value instanceof dorado.Entity) {
                    result = value.validate(options);
                    resultCode = VALIDATION_RESULT_CODE[result];
                    if (resultCode > topResultCode) {
                        topResultCode = resultCode;
                        topResult = result;
                    }
                } else {
                    if (value instanceof dorado.EntityList) {
                        var it = value.iterator();
                        while (it.hasNext()) {
                            result = it.next().validate(options);
                            resultCode = VALIDATION_RESULT_CODE[result];
                            if (resultCode > topResultCode) {
                                topResultCode = resultCode;
                                topResult = result;
                            }
                        }
                    }
                }
            }
        }
        state = this.getMessageState();
        var acceptState = this.dataType ? this.dataType.get("acceptValidationState") : null;
        if (STATE_CODE[state || "info"] <= STATE_CODE[acceptState || "ok"]) {
            result = hasExecutingValidator ? "executing" : "ok";
        } else {
            result = "invalid";
        }
        resultCode = VALIDATION_RESULT_CODE[result];
        if (resultCode > topResultCode) {
            topResultCode = resultCode;
            topResult = result;
        }
        if (context) {
            context.result = topResult;
        }
        return topResult;
    }, isDirty:function (property) {
        if (this.state == dorado.Entity.STATE_NONE) {
            return false;
        }
        if (property) {
            var dirty = false, oldData = this._oldData;
            if (oldData) {
                dirty = (oldData[property] != this._data[property]);
            } else {
                dirty = (this._data[property] != null);
            }
            return dirty;
        } else {
            return this.state != dorado.Entity.STATE_NONE;
        }
    }, flush:function (callback) {
        function checkResult(result) {
            if (result instanceof Array && result.length > 1) {
                throw new dorado.ResourceException("dorado.data.TooMoreResult");
            }
        }
        if (!this.dataType || !this.dataProvider) {
            throw new dorado.ResourceException("dorado.data.DataProviderUndefined");
        }
        var arg = {parameter:this.parameter};
        this.dataProvider.supportsEntity = false;
        if (callback) {
            this.dataProvider.getResultAsync(arg, {scope:this, callback:function (success, result) {
                if (success) {
                    this.fromJSON(result);
                }
                $callback(callback, success, ((success) ? this : result));
            }});
        } else {
            var result = this.dataProvider.getResult(arg);
            this.fromJSON(result);
        }
    }, flushAsync:function (callback) {
        this.flush(callback || dorado._NULL_FUNCTION);
    }, remove:function (detach) {
        if (this.parent) {
            this.parent.remove(this, detach);
        }
    }, toString:function () {
        var text;
        if (this.dataType) {
            var dataType = this.dataType;
            var eventArg = {entity:this, processDefault:true};
            if (!this.disableEvents && dataType.getListenerCount("onEntityToText")) {
                eventArg.processDefault = false;
                dataType.fireEvent("onEntityToText", dataType, eventArg);
            }
            if (eventArg.processDefault) {
                if (dataType._defaultDisplayProperty) {
                    text = this.getText(dataType._defaultDisplayProperty, "never");
                }
                if (text === undefined) {
                    text = "[Entity]";
                }
            }
        } else {
            text = "[Entity]";
        }
        return text;
    }, clone:function () {
        var data = dorado.Core.clone(this._data);
        return new dorado.Entity(data, this.dataTypeRepository, this.dataType);
    }});
    dorado.Entity.ALWAYS_RETURN_VALID_ENTITY_LIST = true;
    var dummyEntityMap = {};
    dorado.Entity.getDummyEntity = function (pageNo) {
        var entity = dummyEntityMap[pageNo];
        if (!entity) {
            dummyEntityMap[pageNo] = entity = new dorado.Entity();
            entity.get = entity.set = entity.getState = dorado._NULL_FUNCTION;
            entity.dummy = true;
            entity.page = {pageNo:pageNo};
        }
        return entity;
    };
})();
(function () {
    dorado.EntityList = function (data, dataTypeRepository, dataType) {
        this.timestamp = dorado.Core.getTimestamp();
        this.dataTypeRepository = dataTypeRepository;
        if (data) {
            if (dataType == null) {
                if (dataTypeRepository && data.$dataType) {
                    dataType = dataTypeRepository.get(data.$dataType);
                }
            } else {
                data.$dataType = dataType._id;
            }
        }
        this.dataType = dataType;
        this.elementDataType = (dataType) ? dataType.getElementDataType() : null;
        this.pageSize = (dataType) ? dataType._pageSize : 0;
        this.pageNo = 1;
        this.pageCount = 0;
        this.entityCount = 0;
        this._pages = [];
        this._keyMap = {};
        if (data != null) {
            this.fromJSON(data);
        }
    };
    dorado.EntityList._MESSAGE_CURRENT_CHANGED = 10;
    dorado.EntityList._MESSAGE_DELETED = 11;
    dorado.EntityList._MESSAGE_INSERTED = 12;
    $class({$className:"dorado.EntityList", constructor:dorado.EntityList, _disableObserversCounter:0, _setObserver:function (observer) {
        this._observer = observer;
        this.each(function (v) {
            if (v instanceof dorado.Entity) {
                v._setObserver(observer);
            }
        });
    }, disableObservers:dorado.Entity.prototype.disableObservers, enableObservers:dorado.Entity.prototype.enableObservers, notifyObservers:dorado.Entity.prototype.notifyObservers, sendMessage:function (messageCode, arg) {
        if (this._disableObserversCounter == 0 && this._observer) {
            this._observer.entityMessageReceived(messageCode, arg);
        }
    }, _findPreviousEntry:function (entry, loadPage, pageNo) {
        if (pageNo == null) {
            pageNo = this.pageNo;
        }
        var previous = (entry) ? entry.previous : null;
        while (!(previous && previous.data.state != dorado.Entity.STATE_DELETED)) {
            if (!previous) {
                if (pageNo > 1) {
                    pageNo--;
                    if (this.isPageLoaded(pageNo)) {
                        previous = this.getPage(pageNo, loadPage).last;
                    }
                }
            } else {
                previous = previous.previous;
            }
            if (!previous) {
                break;
            }
        }
        return previous;
    }, _findNextEntry:function (entry, loadPage, pageNo) {
        if (pageNo == null) {
            pageNo = this.pageNo;
        }
        var next = (entry) ? entry.next : null;
        while (!(next && next.data.state != dorado.Entity.STATE_DELETED)) {
            if (!next) {
                if (pageNo < this.pageCount) {
                    pageNo++;
                    if (this.isPageLoaded(pageNo)) {
                        next = this.getPage(pageNo, loadPage).first;
                    }
                }
            } else {
                next = next.next;
            }
            if (!next) {
                break;
            }
        }
        return next;
    }, _throwInvalidEntity:function (entity) {
        throw new dorado.ResourceException("dorado.data.InvalidEntityToList");
    }, _throwNoCurrent:function () {
        throw new dorado.ResourceException("dorado.data.NoCurrent");
    }, isPageLoaded:function (pageNo) {
        var page = this._pages[pageNo];
        return (page && page.loaded);
    }, getPage:function (pageNo, loadPage, callback) {
        if (pageNo > 0 && pageNo <= this.pageCount) {
            var page = this._pages[pageNo];
            if (!page) {
                page = new dorado.EntityList.Page(this, pageNo);
                this._pages[pageNo] = page;
            }
            if (page.loaded) {
                if (callback) {
                    $callback(callback, true, page);
                    return;
                }
            } else {
                if (loadPage) {
                    if (this.dataProvider) {
                        var dataType = this.dataType, view;
                        if (dataType) {
                            var dataTypeRepository = dataType.get("dataTypeRepository");
                            view = dataTypeRepository ? dataTypeRepository.view : null;
                        }
                        var arg = {parameter:this.parameter, pageSize:this.pageSize, pageNo:pageNo, dataType:dataType, view:view};
                        this.dataProvider.supportsEntity = false;
                        if (callback) {
                            this.dataProvider.getResultAsync(arg, {scope:this, callback:function (success, result) {
                                if (success) {
                                    this._fillPage(page, result);
                                }
                                $callback(callback, success, ((success) ? page : result));
                            }});
                            return;
                        } else {
                            var result = this.dataProvider.getResult(arg);
                            this._fillPage(page, result);
                        }
                    } else {
                        page.loaded = true;
                    }
                }
            }
            return page;
        } else {
            throw new dorado.ResourceException("dorado.data.InvalidPage", pageNo);
        }
    }, setCurrent:function (current) {
        if (this.current == current) {
            return;
        }
        if (current && (!current.page || current.page.entityList != this)) {
            this._throwInvalidEntity(current);
        }
        if (current && current.state == dorado.Entity.STATE_DELETED) {
            throw new dorado.ResourceException("dorado.data.EntityDeleted");
        }
        var eventArg = {entityList:this, oldCurrent:this.current, newCurrent:current, processDefault:true};
        var dataType = this.dataType, elementDataType;
        if (dataType) {
            elementDataType = dataType.getElementDataType();
        }
        if (elementDataType) {
            elementDataType.fireEvent("beforeCurrentChange", elementDataType, eventArg);
        }
        if (!eventArg.processDefault) {
            return;
        }
        this.current = current;
        this.pageNo = (current) ? current.page.pageNo : 1;
        this.timestamp = dorado.Core.getTimestamp();
        if (elementDataType) {
            elementDataType.fireEvent("onCurrentChange", elementDataType, eventArg);
        }
        this.sendMessage(dorado.EntityList._MESSAGE_CURRENT_CHANGED, eventArg);
    }, hasPrevious:function () {
        if (this.current) {
            var page = this.current.page;
            var entry = page.findEntry(this.current);
            entry = this._findPreviousEntry(entry, true);
            return entry != null;
        } else {
            if (this.entityCount > 0) {
                this._throwNoCurrent();
            }
        }
    }, hasNext:function () {
        if (this.current) {
            var page = this.current.page;
            var entry = page.findEntry(this.current);
            entry = this._findNextEntry(entry, true);
            return entry != null;
        } else {
            if (this.entityCount > 0) {
                this._throwNoCurrent();
            }
        }
    }, first:function () {
        var entry = this._findNextEntry(null, true, 0);
        var entity = (entry) ? entry.data : null;
        this.setCurrent(entity);
        return entity;
    }, previous:function () {
        if (this.current) {
            var page = this.current.page;
            var entry = page.findEntry(this.current);
            entry = this._findPreviousEntry(entry, true);
            if (entry) {
                this.setCurrent(entry.data);
                return entry.data;
            }
            return null;
        } else {
            if (this.entityCount > 0) {
                this._throwNoCurrent();
            }
        }
    }, next:function () {
        if (this.current) {
            var page = this.current.page;
            var entry = page.findEntry(this.current);
            entry = this._findNextEntry(entry, true);
            if (entry) {
                this.setCurrent(entry.data);
                return entry.data;
            }
            return null;
        } else {
            if (this.entityCount > 0) {
                this._throwNoCurrent();
            }
        }
    }, last:function () {
        var entry = this._findPreviousEntry(null, true, this.pageCount + 1);
        var entity = (entry) ? entry.data : null;
        this.setCurrent(entity);
        return entity;
    }, move:function (offset) {
        var page = this.current.page;
        var entry = page.findEntry(this.current);
        if (offset > 0) {
            for (var i = 0; i < offset; i++) {
                entry = this._findNextEntry(entry, true);
                if (!entry && this.entityCount > 0) {
                    this._throwNoCurrent();
                }
            }
        } else {
            if (offset < 0) {
                for (var i = 0; i > offset; i--) {
                    entry = this._findPreviousEntry(entry, true);
                    if (!entry && this.entityCount > 0) {
                        this._throwNoCurrent();
                    }
                }
            }
        }
        this.setCurrent(entry.data);
        return entry.data;
    }, gotoPage:function (pageNo, callback) {
        if (callback) {
            var self = this;
            this.getPage(pageNo, true, {callback:function (success, result) {
                if (success) {
                    var entry = result.first;
                    while (entry && entry.data.state == dorado.Entity.STATE_DELETED) {
                        entry = entry.next;
                    }
                    var entity = (entry) ? entry.data : null;
                    if (entity) {
                        self.setCurrent(entity);
                    }
                }
                $callback(callback, success, result);
            }});
        } else {
            var entry = this.getPage(pageNo, true).first;
            while (entry && entry.data.state == dorado.Entity.STATE_DELETED) {
                entry = entry.next;
            }
            var entity = (entry) ? entry.data : null;
            if (entity) {
                this.setCurrent(entity);
            }
            return entity;
        }
    }, firstPage:function (callback) {
        this.gotoPage(1, callback);
    }, previousPage:function (callback) {
        if (this.pageNo <= 1) {
            return;
        }
        this.gotoPage(this.pageNo - 1, callback);
    }, nextPage:function (callback) {
        if (this.pageNo >= this.pageCount) {
            return;
        }
        this.gotoPage(this.pageNo + 1, callback);
    }, lastPage:function (callback) {
        this.gotoPage(this.pageCount, callback);
    }, insert:function (entity, insertMode, refEntity) {
        if (entity == null) {
            entity = new dorado.Entity(null, this.dataTypeRepository, this.elementDataType);
        } else {
            if (entity instanceof dorado.Entity) {
                if (entity.page) {
                    var entityList = entity.page.entityList;
                    if (entityList) {
                        entityList.remove(entity, true);
                    }
                }
            } else {
                entity = new dorado.Entity(entity, this.dataTypeRepository, this.elementDataType);
            }
        }
        if (insertMode == "before" || insertMode == "after") {
            refEntity = refEntity || this.current;
            if (!refEntity) {
                insertMode = (insertMode == "before") ? "begin" : "after";
            }
        }
        var eventArg = {entityList:this, entity:entity, insertMode:insertMode, refEntity:refEntity, processDefault:true};
        var dataType = entity.dataType;
        if (dataType) {
            dataType.fireEvent("beforeInsert", dataType, eventArg);
        }
        if (!eventArg.processDefault) {
            return;
        }
        if (this.pageCount == 0 && this.pageNo == 1) {
            this.pageCount = 1;
        }
        var page = this.getPage(this.pageNo, true);
        page.insert(entity, insertMode, refEntity);
        entity.setState(dorado.Entity.STATE_NEW);
        page.entityCount++;
        this.entityCount++;
        this.timestamp = dorado.Core.getTimestamp();
        if (dataType) {
            dataType.fireEvent("onInsert", dataType, eventArg);
        }
        this.sendMessage(dorado.EntityList._MESSAGE_INSERTED, eventArg);
        this.setCurrent(entity);
        return entity;
    }, remove:function (entity, detach) {
        if (!entity) {
            if (!this.current) {
                this._throwNoCurrent();
            }
            entity = this.current;
        }
        if (entity.parent != this) {
            this._throwInvalidEntity();
        }
        var eventArg = {entity:entity, entityList:this, processDefault:true};
        var dataType = entity.dataType, simpleDetach = (entity.state == dorado.Entity.STATE_DELETED);
        if (!simpleDetach) {
            if (dataType) {
                dataType.fireEvent("beforeRemove", dataType, eventArg);
            }
            if (!eventArg.processDefault) {
                return;
            }
        }
        var isCurrent = (this.current == entity);
        var newCurrent = null;
        if (isCurrent) {
            var entry = entity.page.findEntry(this.current);
            var newCurrentEntry = this._findNextEntry(entry);
            if (!newCurrentEntry) {
                newCurrentEntry = this._findPreviousEntry(entry);
            }
            if (newCurrentEntry) {
                newCurrent = newCurrentEntry.data;
            }
        }
        var page = entity.page;
        if (simpleDetach) {
            detach = true;
        } else {
            page.entityCount--;
            this.entityCount--;
            detach = detach || entity.state == dorado.Entity.STATE_NEW;
            entity.setState(dorado.Entity.STATE_DELETED);
        }
        this.timestamp = dorado.Core.getTimestamp();
        if (!simpleDetach) {
            if (dataType) {
                dataType.fireEvent("onRemove", dataType, eventArg);
            }
            this.sendMessage(dorado.EntityList._MESSAGE_DELETED, eventArg);
        }
        if (detach) {
            page.remove(entity);
        }
        if (isCurrent) {
            this.setCurrent(newCurrent);
        }
    }, createChild:function (data, detached) {
        var elementDataType = (this.dataType) ? this.dataType.getElementDataType() : null;
        if (elementDataType && !(elementDataType instanceof dorado.EntityDataType)) {
            throw new ResourceException("dorado.data.EntityPropertyExpected", property);
        }
        var child = new dorado.Entity(data, this.dataTypeRepository, elementDataType);
        if (!detached) {
            this.insert(child);
        }
        return child;
    }, getById:function (id) {
        return this._keyMap[id];
    }, _fillPage:function (page, jsonArray, changeCurrent) {
        page.entityCount = 0;
        if (jsonArray == null) {
            return;
        }
        if (!(jsonArray instanceof Array)) {
            jsonArray = [jsonArray];
        }
        var entity, firstEntity;
        var dataType = this.dataType;
        if (dataType) {
            dataType._disableObserversCounter++;
        }
        this._disableObserversCounter++;
        try {
            var elementDataType = this.elementDataType;
            for (var i = 0; i < jsonArray.length; i++) {
                if (elementDataType != null) {
                    entity = elementDataType.parse(jsonArray[i]);
                } else {
                    entity = new dorado.Entity(jsonArray[i], (this.dataType) ? this.dataType.get("dataTypeRepository") : null);
                }
                if (i == 0) {
                    firstEntity = entity;
                }
                page.insert(entity);
                if (entity.state != dorado.Entity.STATE_DELETED) {
                    page.entityCount++;
                    this.entityCount++;
                }
            }
            if (jsonArray.entityCount) {
                this.entityCount = jsonArray.entityCount;
            }
            if (jsonArray.pageCount) {
                this.pageCount = jsonArray.pageCount;
            }
            if (changeCurrent && firstEntity) {
                this.setCurrent(firstEntity);
            }
        }
        finally {
            this._disableObserversCounter--;
            if (dataType) {
                dataType._disableObserversCounter--;
            }
        }
        if (firstEntity) {
            this.timestamp = dorado.Core.getTimestamp();
            this.sendMessage(0);
        }
    }, cancel:function () {
        var it = this.iterator(this), entity;
        while (it.hasNext()) {
            entity = it.next();
            entity.disableObservers();
            entity.cancel();
            entity.enableObservers();
        }
        if (entity) {
            this.timestamp = dorado.Core.getTimestamp();
            this.sendMessage(0);
        }
    }, clear:function () {
        this._pages = [];
        this._keyMap = {};
        this.entityCount = 0;
        this.current = null;
        this.sendMessage(0);
    }, flush:function (callback) {
        if (!this.dataProvider) {
            throw new dorado.ResourceException("dorado.data.NoDataPipe");
        }
        this._disableObserversCounter++;
        try {
            this.clear();
        }
        finally {
            this._disableObserversCounter--;
        }
        if (callback) {
            var self = this;
            this.getPage(this.pageNo, true, function (success, page) {
                self._disableObserversCounter++;
                try {
                    if (success) {
                        var entity = (page.first) ? page.first.data : null;
                        self.setCurrent(entity);
                        $callback(callback, true, null);
                    }
                }
                finally {
                    self._disableObserversCounter--;
                    self.sendMessage(0);
                }
            });
        } else {
            var entry = this.getPage(this.pageNo, true).first;
            var entity = (entry) ? entry.data : null;
            this._disableObserversCounter++;
            try {
                this.setCurrent(entity);
            }
            finally {
                this._disableObserversCounter--;
                this.sendMessage(0);
            }
        }
    }, flushAsync:function (callback) {
        this.flush(callback);
    }, fromJSON:function (jsonArray) {
        if (jsonArray.pageNo) {
            this.pageNo = jsonArray.pageNo;
        }
        if (this.pageCount == 0) {
            if (jsonArray.pageCount) {
                this.pageCount = jsonArray.pageCount;
            } else {
                if (this.pageNo == 1) {
                    this.pageCount = 1;
                }
            }
        }
        var page = this.getPage(this.pageNo, true);
        this._fillPage(page, jsonArray, true);
    }, toJSON:function (options, context) {
        var result = [];
        var generateDataType = (options) ? options.generateDataType : false;
        var entityFilter = (options) ? options.entityFilter : null;
        var it = this.iterator(options);
        while (it.hasNext()) {
            var entity = it.next();
            if (entity) {
                if (!entityFilter || entityFilter(entity)) {
                    result.push(entity.toJSON(options, context));
                }
            } else {
                result.push(null);
            }
            if (generateDataType) {
                options.generateDataType = false;
            }
        }
        if (generateDataType) {
            options.generateDataType = true;
        }
        if (result.length == 0 && entityFilter) {
            result = null;
        }
        return result;
    }, toArray:function () {
        var result = [];
        this.each(function (entity) {
            result.push(entity);
        });
        return result;
    }, each:function (fn, scope) {
        for (var i = 1; i <= this.pageCount; i++) {
            if (this.isPageLoaded(i)) {
                this._pages[i].each(fn, scope);
            }
        }
    }, iterator:function (options) {
        return new dorado.EntityList.EntityListIterator(this, options);
    }, toText:function () {
        return this.toString();
    }, toString:function () {
        return "[EntityList(" + this.entityCount + ")]";
    }, clone:function () {
        var cloned = new dorado.EntityList(null, this.dataTypeRepository, this.dataType);
        for (var it = this.iterator(); it.hasNext(); ) {
            cloned.insert(it.next());
        }
        return cloned;
    }});
    var Page = dorado.EntityList.Page = $extend(dorado.util.KeyedList, {$className:"dorado.EntityList.Page", constructor:function (entityList, pageNo) {
        $invokeSuper.call(this, [(function (entity) {
            return entity.entityId;
        })]);
        this.entityList = entityList;
        this.pageNo = pageNo;
        this.entityCount = entityList.pageSize;
    }, insert:function (data, insertMode, refData) {
        $invokeSuper.call(this, arguments);
        data.page = this;
        data.parent = this.entityList;
        data._setObserver(this.entityList._observer);
        this.entityList._keyMap[data.entityId] = data;
        this.loaded = true;
    }, remove:function (data) {
        $invokeSuper.call(this, arguments);
        data.page = null;
        data.parent = null;
        data._setObserver(null);
        delete this.entityList._keyMap[data.entityId];
    }});
    dorado.EntityList.EntityListIterator = $extend(dorado.util.Iterator, {$className:"dorado.EntityList.EntityListIterator", constructor:function (entityList, options) {
        this._entityList = entityList;
        if (options === true) {
            this._includeDeletedEntity = true;
        } else {
            if (options instanceof Object) {
                this._includeDeletedEntity = options.includeDeletedEntity;
                this._includeUnloadPage = options.includeUnloadPage;
                this._nextIndex = options.nextIndex;
                this._fixedPageNo = options.pageNo;
                if (!this._fixedPageNo && options.currentPage) {
                    this._fixedPageNo = entityList.pageNo;
                }
                this._simulateUnloadPage = options.simulateUnloadPage;
                if (this._simulateUnloadPage) {
                    this._includeUnloadPage = true;
                }
            }
        }
        this.firstOrLast();
    }, firstOrLast:function (reverse) {
        var entityList = this._entityList;
        var pageCount = entityList.pageCount;
        var pageNo = this._pageNo = (this._fixedPageNo || (reverse ? pageCount : 1));
        this._previous = this._current = this._next = null;
        this._previousPage = this._currentPage = this._nextPage = pageNo;
        if (this._nextIndex) {
            var skiped = 0;
            if (!this._fixedPageNo) {
                while (pageNo > 0 && pageNo <= pageCount) {
                    var page = entityList.getPage(pageNo, false);
                    skiped += page.entityCount;
                    if (skiped > this._nextIndex) {
                        skiped -= page.entityCount;
                        break;
                    }
                    reverse ? pageNo-- : pageNo++;
                    if (skiped >= this._nextIndex) {
                        break;
                    }
                }
            }
            this._next = this._findFromPage(pageNo);
            if (this._next) {
                this._nextPage = this._next.pageNo;
                this._current = this._findNeighbor(this._next, this._nextPage, true);
                if (this._current) {
                    this._currentPage = this._current.pageNo;
                    this._previous = this._findNeighbor(this._current, this._currentPage, true);
                    if (this._previous) {
                        this._previousPage = this._previous.pageNo;
                    }
                }
                for (var i = skiped; i < this._nextIndex; i++) {
                    if (this.hasNext()) {
                        this.next();
                    } else {
                        break;
                    }
                }
            }
        } else {
            var result = this._findFromPage(pageNo, reverse);
            if (reverse) {
                this._previous = result;
                if (this._previous) {
                    this._previousPage = this._previous.pageNo;
                }
            } else {
                this._next = result;
                if (this._next) {
                    this._nextPage = this._next.pageNo;
                }
            }
        }
    }, _findFromPage:function (pageNo, reverse) {
        var result = null, entityList = this._entityList, pageCount = entityList.pageCount, page;
        if (this._includeUnloadPage) {
            page = entityList.getPage(pageNo, !this._simulateUnloadPage);
        } else {
            while (pageNo > 0 && pageNo <= pageCount) {
                var p = entityList.getPage(pageNo, false);
                if (p.loaded) {
                    page = p;
                    break;
                }
                if (this._fixedPageNo) {
                    break;
                }
                pageNo += (reverse ? -1 : 1);
            }
        }
        if (page) {
            if (page.loaded) {
                result = reverse ? page.last : page.first;
                while (result && !this._includeDeletedEntity && result.data.state == dorado.Entity.STATE_DELETED) {
                    result = reverse ? result.previous : result.next;
                }
            } else {
                result = {data:dorado.Entity.getDummyEntity(pageNo)};
                this._simulatePageSize = (pageNo == pageCount) ? (entityList.entityCount % entityList.pageSize) : entityList.pageSize;
                this._simulateIndex = (reverse) ? this._simulatePageSize : 0;
            }
        }
        return result;
    }, _findNeighbor:function (entry, pageNo, reverse) {
        if (!entry) {
            return null;
        }
        var inc = reverse ? -1 : 1;
        if (entry.data && !entry.data.dummy) {
            do {
                entry = reverse ? entry.previous : entry.next;
            } while (entry && !this._includeDeletedEntity && entry.data.state == dorado.Entity.STATE_DELETED);
            if (entry) {
                entry.pageNo = pageNo;
            }
        } else {
            this._simulateIndex += inc;
            if (this._simulateIndex < 0 || this._simulateIndex >= this._simulatePageSize) {
                this._simulateIndex -= inc;
                entry = null;
            }
        }
        if (entry == null && !this._fixedPageNo) {
            pageNo += inc;
            if (pageNo > 0 && pageNo <= this._entityList.pageCount) {
                entry = this._findFromPage(pageNo, reverse);
            }
        }
        return entry;
    }, _find:function (reverse) {
        var result = this._findNeighbor((reverse ? this._previous : this._next), (reverse ? this._previousPage : this._nextPage), reverse);
        if (reverse) {
            this._next = this._current;
            this._nextPage = this._currentPage;
            this._current = this._previous;
            this._currentPage = this._previousPage;
            this._previous = result;
            this._previousPage = result ? result.data.page.pageNo : 1;
        } else {
            this._previous = this._current;
            this._previousPage = this._currentPage;
            this._current = this._next;
            this._currentPage = this._nextPage;
            this._next = result;
            this._nextPage = result ? result.data.page.pageNo : this._entityList.pageCount;
        }
    }, first:function () {
        this.firstOrLast();
    }, last:function () {
        this.firstOrLast(true);
    }, hasPrevious:function () {
        return !!this._previous;
    }, hasNext:function () {
        return !!this._next;
    }, previous:function () {
        if (!this._previous) {
            this._next = this._current;
            this._nextPage = this._currentPage;
            this._current = this._previous = null;
            this._currentPage = this._previousPage = 1;
            return null;
        }
        var data = this._previous.data;
        this._find(true);
        return data;
    }, next:function () {
        if (!this._next) {
            this._previous = this._current;
            this._previousPage = this._currentPage;
            this._current = this._next = null;
            this._currentPage = this._nextPage = this._entityList.pageCount;
            return null;
        }
        var data = this._next.data;
        this._find(false);
        return data;
    }, current:function () {
        return (this._current) ? this._current.data : null;
    }, createBookmark:function () {
        return {previous:this._previous, current:this._current, next:this._next, previousPage:this._previousPage, currentPage:this._currentPage, nextPage:this._nextPage, simulateIndex:this._simulateIndex, simulatePageSize:this._simulatePageSize};
    }, restoreBookmark:function (bookmark) {
        this._previous = bookmark.previous;
        this._current = bookmark.current;
        this._next = bookmark.next;
        this._previousPage = bookmark.previousPage;
        this._currentPage = bookmark.currentPage;
        this._nextPage = bookmark.nextPage;
        this._simulateIndex = bookmark.simulateIndex;
        this._simulatePageSize = bookmark.simulatePageSize;
    }});
}());
dorado.validator = {};
dorado.Toolkits.registerTypeTranslator("validator", function (type) {
    return dorado.util.Common.getClassType("dorado.validator." + type + "Validator", true);
});
dorado.validator.Validator = $extend(dorado.AttributeSupport, {className:"dorado.Validator", ATTRIBUTES:{defaultResultMessage:{}, defaultResultState:{defaultValue:"error"}}, constructor:function (config) {
    $invokeSuper.call(this, arguments);
    if (config) {
        this.set(config);
    }
}, validate:function (data) {
    var result = this.doValidate(data);
    return dorado.Toolkits.trimMessages(result, this._defaultResultState);
}});
dorado.validator.RemoteValidator = $extend(dorado.validator.Validator, {className:"dorado.validator.RemoteValidator", ATTRIBUTES:{async:{defaultValue:true}, executingMessage:{}}, validate:function (data, callback) {
    if (this._async) {
        this.doValidate(data, {scope:this, callback:function (success, result) {
            if (success) {
                result = dorado.Toolkits.trimMessages(result, this._defaultResultState);
            }
            $callback(success, result);
        }});
    } else {
        var result = $invokeSuper.call(this, arguments);
        if (callback) {
            $callback(callback, true, result);
        }
        return result;
    }
}});
dorado.validator.LengthValidator = $extend(dorado.validator.Validator, {className:"dorado.validator.LengthValidator", ATTRIBUTES:{minLength:{defaultValue:-1}, maxLength:{defaultValue:-1}}, doValidate:function (data) {
    if (typeof data != "string") {
        return;
    }
    var invalid, message = "", len = data.length;
    if (this._minLength > 0 && len < this._minLength) {
        invalid = true;
        message += $resource("dorado.data.ErrorContentTooShort", this._minLength);
    }
    if (this._maxLength > 0 && len > this._maxLength) {
        invalid = true;
        if (message) {
            message += "\n";
        }
        message += $resource("dorado.data.ErrorContentTooLong", this._maxLength);
    }
    return message;
}});
dorado.validator.RangeValidator = $extend(dorado.validator.Validator, {className:"dorado.validator.RangeValidator", ATTRIBUTES:{minValue:{}, minValueValidateMode:{defaultValue:"ignore"}, maxValue:{}, maxValueValidateMode:{defaultValue:"ignore"}}, doValidate:function (data) {
    var invalid, message = "", subMessage = "", data = (typeof data == "number") ? data : parseFloat(data);
    if (this._minValueValidateMode != "ignore") {
        if (data == this._minValue && this._minValueValidateMode != "allowEquals") {
            invalid = true;
            subMessage = $resource("dorado.data.ErrorOrEqualTo");
        }
        if (data < this._minValue) {
            invalid = true;
        }
        if (invalid) {
            message += $resource("dorado.data.ErrorNumberTooLess", subMessage, this._minValue);
        }
    }
    if (this._maxValueValidateMode != "ignore") {
        if (data == this._maxValue && this._maxValueValidateMode != "allowEquals") {
            invalid = true;
            subMessage = $resource("dorado.data.ErrorOrEqualTo");
        }
        if (data > this._maxValue) {
            invalid = true;
        }
        if (invalid) {
            if (message) {
                message += "\n";
            }
            message += $resource("dorado.data.ErrorNumberTooGreat", subMessage, this._maxValue);
        }
    }
    if (invalid) {
        return message;
    }
}});
dorado.validator.EnumValidator = $extend(dorado.validator.Validator, {className:"dorado.validator.EnumValidator", ATTRIBUTES:{enumValus:{}}, doValidate:function (data) {
    if (this._enumValues instanceof Array && this._enumValues.indexOf(data) < 0) {
        return $resource("dorado.data.ErrorValueOutOfEnumRange");
    }
}});
dorado.validator.RegExpValidator = $extend(dorado.validator.Validator, {className:"dorado.validator.RegExpValidator", ATTRIBUTES:{whiteRegExp:{}, blackRegExp:{}, validateMode:{defaultValue:"whiteBlack"}}, doValidate:function (data) {
    function toRegExp(text) {
        var regexp = null;
        if (text) {
            regexp = (text.charAt(0) == "/") ? eval(text) : new RegExp(text);
        }
        return regexp;
    }
    if (typeof data != "string") {
        return;
    }
    var whiteRegExp = toRegExp(this._whiteRegExp), blackRegExp = toRegExp(this._blackRegExp);
    var valid;
    if (this._validateMode == "whiteBlack") {
        valid = blackRegExp && data.match(blackRegExp) || whiteRegExp && !data.match(whiteRegExp);
    } else {
        valid = whiteRegExp && !data.match(whiteRegExp) || blackRegExp && data.match(blackRegExp);
    }
    if (!valid) {
        return $resource("dorado.data.ErrorBadFormat", data);
    }
}});
dorado.validator.AjaxValidator = $extend(dorado.validator.RemoteValidator, {className:"dorado.validator.AjaxValidator", ATTRIBUTES:{service:{}}, constructor:function (config) {
    if (!dorado.widget || !dorado.widget.AjaxAction) {
        this._disabled = true;
        alert("'dorado.validator.AjaxValidator' is disabled because the 'dorado.widget.AjaxAction' is not available.");
    }
    $invokeSuper.call(this, arguments);
}, doValidate:function (data, callback) {
    var ajaxAction = this._ajaxAction;
    if (!ajaxAction) {
        this._ajaxAction = ajaxAction = new dorado.widget.AjaxAction();
    }
    ajaxAction.set({async:this._async, executingMessage:this._executingMessage, service:this._service, parameter:data});
    var retval = ajaxAction.execute(this._async ? callback : null);
    if (retval && !this._async) {
        return ajaxAction.get("returnValue");
    }
}});
(function () {
    var BREAK_ALL = {};
    var BREAK_LEVEL = {};
    var ENTITY_PATH_CACHE = {};
    dorado.DataPath = $class({$className:"dorado.DataPath", _ALL:[{visibility:1}], _CURRENT:[{visibility:2}], _REPEAT_ALL:[{visibility:1, repeat:true}], constructor:function (path) {
        this.path = (path != null) ? $.trim(path) : path;
    }, _throw:function (message, position) {
        var text = "DataPath syntax error";
        if (message) {
            text += (":\n" + message + "in:\n");
        } else {
            text += " in:\n";
        }
        var path = this.path;
        text += path;
        if (isFinite(position)) {
            position = parseInt(position);
            text += "\nat char " + position;
        }
        throw new SyntaxError(text);
    }, compile:function () {
        function isUnsignedInteger(s) {
            return (s.search(/^[0-9]+$/) == 0);
        }
        var path = this.path;
        if (path == null || path == "" || path == "*") {
            this._compiledPath = this._ALL;
            return;
        }
        if (path == "#" || path == "[#current]") {
            this._compiledPath = this._CURRENT;
            this._compiledPath.singleResult = true;
            return;
        }
        var _path = path.toLowerCase();
        if (_path == "(repeat)" || _path == "(r)") {
            this._compiledPath = this._REPEAT_ALL;
            return;
        }
        var compiledPath = [];
        var property = "";
        var args = null;
        var arg;
        var conditions = null;
        var condition;
        var quotation = null;
        var inArgs = false;
        var afterArgs = false;
        var inCondition = false;
        var afterCondition = false;
        var escapeNext = false;
        for (var i = 0; i < path.length; i++) {
            var c = path.charAt(i);
            if (escapeNext) {
                property += c;
                escapeNext = false;
                continue;
            }
            if (afterArgs && afterCondition && c != ".") {
                this._throw(null, i);
            }
            switch (c) {
              case ".":
                if (!quotation && !inArgs && !inCondition) {
                    compiledPath.push({property:property, args:args, conditions:conditions});
                    property = "";
                    args = null;
                    arg = "";
                    conditions = null;
                    condition = "";
                    c = null;
                    quotation = null;
                    inArgs = false;
                    afterArgs = false;
                    inCondition = false;
                    afterCondition = false;
                }
                break;
              case ",":
                if (!inArgs && !inCondition) {
                    this._throw(null, i);
                }
                if (!quotation) {
                    if (inArgs) {
                        args.push(arg);
                        arg = "";
                    } else {
                        if (inCondition) {
                            conditions.push(condition);
                            condition = "";
                        }
                    }
                    c = null;
                }
                break;
              case "'":
              case "\"":
                if (!inArgs && !inCondition) {
                    this._throw(null, i);
                }
                if (!quotation) {
                    quotation = c;
                } else {
                    if (quotation == c) {
                        quotation = null;
                    }
                }
                break;
              case "[":
                if (inArgs || afterCondition) {
                    this._throw(null, i);
                }
                if (!inCondition) {
                    inCondition = true;
                    conditions = [];
                    condition = "";
                    c = null;
                }
                break;
              case "]":
                if (inCondition) {
                    if (condition.length > 0) {
                        conditions.push(condition);
                    }
                    inCondition = false;
                    afterCondition = true;
                    c = null;
                } else {
                    this._throw(null, i);
                }
                break;
              case "(":
                if (!inCondition) {
                    if (inArgs || afterArgs) {
                        this._throw(null, i);
                    }
                    inArgs = true;
                    args = [];
                    arg = "";
                    c = null;
                }
                break;
              case ")":
                if (!inCondition && afterArgs) {
                    this._throw(null, i);
                }
                if (inArgs) {
                    if (arg.length > 0) {
                        args.push(arg);
                    }
                    inArgs = false;
                    afterArgs = true;
                    c = null;
                }
                break;
              case "@":
                c = "$this";
                break;
              default:
                escapeNext = (c == "\\");
            }
            if (!escapeNext && c != null) {
                if (inCondition) {
                    condition += c;
                } else {
                    if (inArgs) {
                        arg += c;
                    } else {
                        property += c;
                    }
                }
            }
        }
        if (property.length > 0 || (args && args.length > 0) || (conditions && conditions.length > 0)) {
            compiledPath.push({property:property, args:args, conditions:conditions});
        }
        var singleResult = (compiledPath.length > 0);
        for (var i = 0; i < compiledPath.length; i++) {
            var section = compiledPath[i];
            if ((!section.property || section.property == "*") && !section.args && !section.conditions) {
                section = this._ALL;
                compiledPath[i] = section;
                singleResult = false;
            } else {
                var property = section.property;
                if (property) {
                    if (property.charAt(0) == "#") {
                        section.visibility = 2;
                        section.property = property = property.substring(1);
                    }
                    if (property.charAt(0) == "!") {
                        section.interceptor = property.substring(1);
                    }
                }
                var args = section.args;
                if (args) {
                    for (var j = 0; j < args.length; j++) {
                        var arg = args[j].toLowerCase();
                        if (arg == "r" || arg == "repeat") {
                            section.repeat = true;
                        } else {
                            if (arg == "l" || arg == "leaf") {
                                section.repeat = true;
                                section.leaf = true;
                            } else {
                                if (isUnsignedInteger(arg)) {
                                    section.max = parseInt(arg);
                                }
                            }
                        }
                    }
                }
                var conditions = section.conditions;
                if (conditions) {
                    for (var j = conditions.length - 1; j >= 0; j--) {
                        var condition = conditions[j];
                        if (condition && condition.charAt(0) == "#" && !(section.visibility > 0)) {
                            if (condition == "#dirty") {
                                section.visibility = 3;
                            } else {
                                if (condition == "#new") {
                                    section.visibility = 4;
                                } else {
                                    if (condition == "#modified") {
                                        section.visibility = 5;
                                    } else {
                                        if (condition == "#deleted") {
                                            section.visibility = 6;
                                        } else {
                                            if (condition == "#current") {
                                                section.visibility = 2;
                                            } else {
                                                this._throw("Unknown token \"" + condition + "\".");
                                            }
                                        }
                                    }
                                }
                            }
                            conditions.removeAt(j);
                        }
                    }
                }
                singleResult = (section.visibility == 2 && (section.leaf || !section.repeat));
            }
        }
        compiledPath.singleResult = singleResult;
        this._compiledPath = compiledPath;
    }, _selectEntityIf:function (context, entity, isLeaf) {
        var section = context.section;
        if (!section.leaf || isLeaf) {
            var sections = context.sections;
            if (section == sections[sections.length - 1]) {
                context.addResult(entity);
            } else {
                this._evaluateSectionOnEntity(context, entity, true);
            }
        }
    }, _evaluateSectionOnEntity:function (context, entity, nextSection) {
        var oldLevel = context.level;
        if (nextSection) {
            if (context.level >= (context.sections.length - 1)) {
                reutrn;
            }
            context.setCurrentLevel(context.level + 1);
        }
        var oldLastSection = context.lastSection;
        var section = context.section;
        context.lastSection = section;
        try {
            var result;
            if (section.interceptor) {
                var interceptors = dorado.DataPath.interceptors[section.interceptor];
                if (interceptors && interceptors.dataInterceptor) {
                    result = interceptors.dataInterceptor.call(this, entity, section.interceptor);
                } else {
                    throw new dorado.Exception("DataPath interceptor \"" + section.interceptor + "\" not found.");
                }
            } else {
                if (section.property) {
                    if (entity instanceof dorado.Entity) {
                        dorado.Entity.ALWAYS_RETURN_VALID_ENTITY_LIST = !section.leaf;
                        try {
                            result = entity.get(section.property, context.loadMode);
                        }
                        finally {
                            dorado.Entity.ALWAYS_RETURN_VALID_ENTITY_LIST = true;
                        }
                    } else {
                        result = entity[section.property];
                    }
                    if (result == null && section.leaf && section == oldLastSection) {
                        this._selectEntityIf(context, entity, true);
                    }
                } else {
                    result = entity;
                }
            }
            if (result != null) {
                if (result instanceof dorado.EntityList || result instanceof Array) {
                    this._evaluateSectionOnAggregation(context, result);
                } else {
                    this._selectEntityIf(context, result);
                    if (section.repeat) {
                        this._evaluateSectionOnEntity(context, entity);
                    }
                }
            }
        }
        finally {
            context.lastSection = oldLastSection;
            context.setCurrentLevel(oldLevel);
        }
    }, _evaluateSectionOnAggregation:function (context, entities, isRoot) {
        function selectEntityIf(entity) {
            var b = true;
            switch (section.visibility) {
              case 3:
                b = entity.state != dorado.Entity.STATE_NONE;
                break;
              case 4:
                b = entity.state == dorado.Entity.STATE_NEW;
                break;
              case 5:
                b = entity.state == dorado.Entity.STATE_MODIFIED;
                break;
              case 6:
                b = entity.state == dorado.Entity.STATE_DELETED;
                break;
            }
            if (b) {
                var conditions = section.conditions;
                if (conditions) {
                    var $this = entity;
                    for (var i = 0; i < conditions.length; i++) {
                        b = eval(conditions[i]);
                        if (!b) {
                            break;
                        }
                    }
                }
            }
            if (b) {
                this._selectEntityIf(context, entity);
            }
            if (section.repeat) {
                this._evaluateSectionOnEntity(context, entity);
            }
        }
        try {
            var section = context.section;
            if (section.interceptor) {
                var interceptors = dorado.DataPath.interceptors[section.interceptor];
                if (interceptors && interceptors.dataInterceptor) {
                    entities = interceptors.dataInterceptor.call(this, entities, section.interceptor);
                    if (!(entities instanceof dorado.EntityList || entities instanceof Array)) {
                        if (entities != null) {
                            entities = [entities];
                        } else {
                            return;
                        }
                    }
                } else {
                    throw new dorado.Exception("DataPath interceptor \"" + section.interceptor + "\" not found.");
                }
            }
            if (!isRoot && context.acceptAggregation && (section.visibility || 1) == 1) {
                var sections = context.sections;
                if (section == sections[sections.length - 1]) {
                    this._selectEntityIf(context, entities, true);
                }
            }
            if (entities instanceof dorado.EntityList) {
                if (section.visibility == 2) {
                    if (entities.current) {
                        selectEntityIf.call(this, entities.current);
                    }
                } else {
                    var it = entities.iterator(section.visibility == 3 || section.visibility == 6);
                    while (it.hasNext()) {
                        selectEntityIf.call(this, it.next());
                    }
                }
            } else {
                for (var i = 0; i < entities.length; i++) {
                    selectEntityIf.call(this, entities[i]);
                }
            }
        }
        catch (e) {
            if (e != BREAK_LEVEL) {
                throw e;
            }
        }
    }, evaluate:function (data, options) {
        var firstResultOnly, acceptAggregation = false, loadMode;
        if (options === true) {
            firstResultOnly = options;
        } else {
            if (options instanceof Object) {
                firstResultOnly = options.firstResultOnly;
                acceptAggregation = options.acceptAggregation;
                loadMode = options.loadMode;
            }
        }
        loadMode = loadMode || "always";
        if (this._compiledPath === undefined) {
            this.compile();
        }
        firstResultOnly = firstResultOnly || this._compiledPath.singleResult;
        var context = new dorado.DataPathContext(this._compiledPath, firstResultOnly);
        context.acceptAggregation = acceptAggregation;
        context.loadMode = loadMode;
        try {
            if (data != null) {
                if (data instanceof dorado.EntityList || data instanceof Array) {
                    this._evaluateSectionOnAggregation(context, data, true);
                } else {
                    this._evaluateSectionOnEntity(context, data);
                }
            }
            return context.results;
        }
        catch (e) {
            if (e == BREAK_ALL) {
                return (firstResultOnly) ? context.result : context.results;
            } else {
                throw e;
            }
        }
    }, getDataType:function (dataType, options) {
        if (!dataType) {
            return null;
        }
        var acceptAggregationDataType, loadMode;
        if (options === true) {
            acceptAggregationDataType = options;
        } else {
            if (options instanceof Object) {
                acceptAggregationDataType = options.acceptAggregationDataType;
                loadMode = options.loadMode;
            }
        }
        loadMode = loadMode || "always";
        var cache = dataType._subDataTypeCache;
        if (cache) {
            var dt = cache[this.path];
            if (dt !== undefined) {
                if (!acceptAggregationDataType && dt instanceof dorado.AggregationDataType) {
                    dt = dt.getElementDataType(loadMode);
                }
                if (dt instanceof dorado.DataType) {
                    return dt;
                }
            }
        } else {
            dataType._subDataTypeCache = cache = {};
        }
        if (dataType instanceof dorado.LazyLoadDataType) {
            dataType = dataType.get(loadMode);
        }
        if (this._compiledPath === undefined) {
            this.compile();
        }
        var compiledPath = this._compiledPath;
        for (var i = 0; i < compiledPath.length; i++) {
            var section = compiledPath[i];
            if (section.interceptor) {
                var interceptors = dorado.DataPath.interceptors[section.interceptor];
                if (interceptors && interceptors.dataTypeInterceptor) {
                    dataType = interceptors.dataTypeInterceptor.call(this, dataType, section.interceptor);
                } else {
                    dataType = null;
                }
            } else {
                if (section.property) {
                    if (dataType instanceof dorado.AggregationDataType) {
                        dataType = dataType.getElementDataType(loadMode);
                    }
                    var p = dataType.getPropertyDef(section.property);
                    dataType = (p) ? p.get("dataType") : null;
                }
            }
            if (!dataType) {
                break;
            }
        }
        cache[this.path] = dataType;
        if (!acceptAggregationDataType && dataType instanceof dorado.AggregationDataType) {
            dataType = dataType.getElementDataType(loadMode);
        }
        return dataType;
    }, _section2Path:function (section) {
        var path = (section.visibility == 2) ? "#" : "";
        path += (section.property) ? section.property : "";
        var args = section.args;
        if (args && args.length > 0) {
            path += "(" + args.join(",") + ")";
        }
        var conditions = section.conditions;
        if (conditions && conditions.length > 0) {
            path += "[" + conditions.join(",") + "]";
        }
        return (path) ? path : "*";
    }, _compiledPath2Path:function () {
        var compiledPath = this._compiledPath;
        var sections = [];
        for (var i = 0; i < compiledPath.length; i++) {
            sections.push(this._section2Path(compiledPath[i]));
        }
        return sections.join(".");
    }, toString:function () {
        this.compile();
        return this._compiledPath2Path();
    }});
    dorado.DataPath.create = function (path) {
        var key = path || "$EMPTY";
        var dataPath = ENTITY_PATH_CACHE[key];
        if (dataPath == null) {
            ENTITY_PATH_CACHE[key] = dataPath = new dorado.DataPath(path);
        }
        return dataPath;
    };
    dorado.DataPath.interceptors = {};
    dorado.DataPath.registerInterceptor = function (section, dataInterceptor, dataTypeInterceptor) {
        dorado.DataPath.interceptors[section] = {dataInterceptor:dataInterceptor, dataTypeInterceptor:dataTypeInterceptor};
    };
    dorado.DataPathContext = $class({$className:"dorado.DataPathContext", constructor:function (sections, firstResultOnly) {
        this.sections = sections;
        this.firstResultOnly = firstResultOnly;
        this.level = -1;
        this.levelInfos = [];
        if (firstResultOnly) {
            this.result = null;
        } else {
            this.results = [];
        }
        this.lastSection = sections[sections.length - 1];
        this.setCurrentLevel(0);
    }, setCurrentLevel:function (level) {
        if (level > this.level) {
            this.levelInfos[level] = this.levelInfo = {count:0};
        } else {
            this.levelInfo = this.levelInfos[level];
        }
        this.level = level;
        this.section = this.sections[level];
    }, addResult:function (result) {
        if (this.firstResultOnly) {
            this.result = result;
            throw BREAK_ALL;
        } else {
            var section = this.section;
            if (section.max > 0 && this.levelInfo.count >= section.max) {
                throw BREAK_LEVEL;
            }
            this.results.push(result);
            this.levelInfo.count++;
        }
    }});
})();
(function () {
    dorado.DataUtil = {extractNameFromId:function (id) {
        function extractName(id) {
            if (id.indexOf("v:") == 0) {
                var i = id.indexOf("$");
                if (i > 0) {
                    return id.substring(i + 1);
                }
            }
            return id;
        }
        var name = id;
        var subId = dorado.DataType.getSubName(id);
        if (subId) {
            var subName = this.extractNameFromId(subId);
            if (subName != subId) {
                name = name.replace(subId, subName);
            }
        }
        return extractName(name);
    }, convertIfNecessary:function (data, dataTypeRepository, dataType) {
        if (data == null) {
            return data;
        }
        if (dataType) {
            if (dataType instanceof dorado.LazyLoadDataType) {
                dataType = dataType.get();
            } else {
                if (typeof dataType == "string" && dataTypeRepository) {
                    dataType = dataTypeRepository.get(dataType);
                }
            }
        }
        if (data instanceof dorado.Entity || data instanceof dorado.EntityList) {
            if (!dataType || data.dataType == dataType) {
                return data;
            }
            if (data.dataType instanceof dorado.AggregationDataType && data.dataType.get("elementDataType") == dataType) {
                return data;
            }
            data = data.toJSON();
        }
        if (data.$dataType && !dataType && dataTypeRepository) {
            dataType = dataTypeRepository.get(data.$dataType);
        }
        if (dataType) {
            if (dataType instanceof dorado.EntityDataType && data instanceof Array) {
                dataType = new dorado.AggregationDataType({elementDataType:dataType});
            }
            if (dataType instanceof dorado.DataType) {
                data = dataType.parse(data);
            }
        }
        return data;
    }, convert:function (data, dataTypeRepository, dataType) {
        if (data == null) {
            return data;
        }
        var result = this.convertIfNecessary(data, dataTypeRepository, dataType);
        if (result == data) {
            if (data instanceof Array) {
                result = new dorado.EntityList(data, dataTypeRepository, dataType);
            } else {
                if (data instanceof Object) {
                    result = new dorado.Entity(data, dataTypeRepository, dataType);
                } else {
                    throw new dorado.ResourceException("dorado.data.DataTypeUnsupported");
                }
            }
        }
        return result;
    }, isOwnerOf:function (data, owner) {
        if (data == null) {
            return false;
        }
        while (true) {
            data = data.parent;
            if (data == null) {
                return false;
            }
            if (data == owner) {
                return true;
            }
        }
    }, DEFAULT_SORT_PARAMS:[{desc:false}], sort:function (array, sortParams, comparator) {
        function quickSort(low, high) {
            function compareEntity(entity, midEntity, comparator) {
                if (comparator) {
                    return comparator(entity, midEntity, sortParams);
                }
                var result1, result2;
                if (!(sortParams instanceof Array)) {
                    sortParams = [sortParams];
                }
                for (var i = 0; i < sortParams.length; i++) {
                    var sortParam = sortParams[i], property = sortParam.property;
                    var value, midValue;
                    if (property) {
                        value = (entity instanceof dorado.Entity) ? entity.get(property) : entity[property];
                        midValue = (entity instanceof dorado.Entity) ? midEntity.get(property) : midEntity[property];
                    } else {
                        value = entity;
                        midValue = midEntity;
                    }
                    if (value > midValue) {
                        return (sortParam.desc) ? -1 : 1;
                    } else {
                        if (value < midValue) {
                            return (sortParam.desc) ? 1 : -1;
                        }
                    }
                }
                return 0;
            }
            var paramLow = low, paramHigh = high;
            var mid = parseInt((low + high) / 2);
            var midEntity = array[mid];
            do {
                while (compareEntity(array[low], midEntity, comparator) < 0) {
                    low++;
                }
                while (compareEntity(array[high], midEntity, comparator) > 0) {
                    high--;
                }
                if (low <= high) {
                    var swap = array[low];
                    array[low] = array[high];
                    array[high] = swap;
                    low++;
                    high--;
                }
            } while (low <= high);
            if (high > paramLow) {
                quickSort(paramLow, high);
            }
            if (paramHigh > low) {
                quickSort(low, paramHigh);
            }
        }
        if (sortParams == null && !comparator) {
            sortParams = this.DEFAULT_SORT_PARAMS;
        }
        quickSort(0, array.length - 1);
    }};
    function getValueForSummary(entity, property) {
        return parseFloat((entity instanceof dorado.Entity) ? entity.get(property) : entity[property]) || 0;
    }
    dorado.SummaryCalculators = {count:function (value, entity, property) {
        return value + 1;
    }, sum:function (value, entity, property) {
        return value + getValueForSummary(entity, property);
    }, average:{getInitialValue:function () {
        return {sum:0, count:0};
    }, accumulate:function (value, entity, property) {
        value.sum += getValueForSummary(entity, property);
        value.count++;
        return value;
    }, getFinalValue:function (value) {
        return value.count ? value.sum / value.count : 0;
    }}, max:{getInitialValue:function () {
        return null;
    }, accumulate:function (value, entity, property) {
        var v = getValueForSummary(entity, property);
        return (v < value) ? value : v;
    }}, min:{getInitialValue:function () {
        return null;
    }, accumulate:function (value, entity, property) {
        var v = getValueForSummary(entity, property);
        return (v > value) ? value : v;
    }}};
})();

