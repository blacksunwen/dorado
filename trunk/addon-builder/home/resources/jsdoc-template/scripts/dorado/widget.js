
(function () {
    dorado.widget = {};
    dorado.widget.Component = $extend([dorado.AttributeSupport, dorado.EventSupport], {$className:"dorado.widget.Component", ATTRIBUTES:{id:{readOnly:true}, ready:{readOnly:true}, destroyed:{readOnly:true}, parent:{readOnly:true}, view:{setter:function (attr, view) {
        if (this._view == view) {
            return;
        }
        if (this._view) {
            this._view.unregisterComponent(this._id);
        }
        if (view) {
            view.registerComponent(this._id, this);
        }
        this._view = view;
    }}, dataTypeRepository:{readOnly:true, getter:function () {
        var view = this.get("view") || dorado.widget.View.TOP;
        return view.get("dataTypeRepository");
    }}, userData:{skipRefresh:true}}, EVENTS:{onCreate:{}, onDestroy:{}, onReady:{}}, constructor:function (config) {
        var id;
        if (config && config.constructor == String) {
            id = config;
            config = null;
        } else {
            if (config) {
                id = config.id;
                delete config.id;
            }
        }
        this._uniqueId = dorado.Core.newId();
        dorado.widget.Component.ALL[this._uniqueId] = this;
        this._id = id ? id : this._uniqueId;
        $invokeSuper.call(this, arguments);
        if (config) {
            this.set(config);
        }
        if (dorado.widget.View.TOP) {
            dorado.widget.View.TOP.addChild(this);
        }
        this.fireEvent("onCreate", this);
    }, doSet:function (attr, value) {
        function getComponent(value, defaultType) {
            if (!value) {
                return value;
            }
            if (!(value instanceof dorado.widget.Component) && typeof value == "object") {
                value = dorado.Toolkits.createInstance("widget", value, function () {
                    return defaultType ? dorado.Toolkits.getPrototype("widget", defaultType) : null;
                });
            }
            return value;
        }
        var def = this.ATTRIBUTES[attr];
        if (def) {
            if (this._ready && def.writeBeforeReady) {
                throw new dorado.ResourceException("dorado.widget.AttributeWriteBeforeReady", attr);
            }
            if (def.componentReference) {
                var component = null, allPrepared = false;
                if (value) {
                    if (value instanceof Array) {
                        if (value.length > 0) {
                            component = [], allPrepared = true;
                            for (var i = 0; i < value.length; i++) {
                                component[i] = dorado.widget.Component.getComponentReference(this, attr, value[i]);
                                if (!(component[i] instanceof dorado.widget.Component)) {
                                    allPrepared = false;
                                }
                            }
                        }
                    } else {
                        component = dorado.widget.Component.getComponentReference(this, attr, value);
                        allPrepared = (component instanceof dorado.widget.Component);
                    }
                }
                $invokeSuper.call(this, [attr, (allPrepared ? component : null)]);
                return;
            } else {
                if (def.innerComponent != null) {
                    if (value) {
                        if (value instanceof Array) {
                            var components = [];
                            for (var i = 0; i < value.length; i++) {
                                components.push(getComponent(value[i], def.innerComponent));
                            }
                            value = components;
                        } else {
                            value = getComponent(value, def.innerComponent);
                        }
                    }
                }
            }
        }
        $invokeSuper.call(this, arguments);
    }, onReady:function () {
        if (this._ready) {
            return;
        }
        this._ready = true;
        this.fireEvent("onReady", this);
    }, destroy:function () {
        if (this._destroyed) {
            return;
        }
        this._destroyed = true;
        this.fireEvent("onDestroy", this);
        delete dorado.widget.Component.ALL[this._uniqueId];
    }, getListenerScope:function () {
        return this.get("view") || dorado.widget.View.TOP;
    }});
    dorado.widget.Component.getComponentReference = function (object, attr, value) {
        if (!value) {
            return value;
        }
        if (value instanceof dorado.widget.Component) {
            return value;
        } else {
            var component;
            if (typeof value == "string") {
                component = dorado.widget.View.TOP.id(value);
                if (component) {
                    return component;
                }
                value = {view:dorado.widget.View.TOP, component:value};
            } else {
                if (typeof value == "object" && value.$type) {
                    return dorado.Toolkits.createInstance("widget", value);
                }
            }
            var view = value.view, componentId = value.component;
            component = view.id(componentId);
            if (component) {
                return component;
            }
            var wantedComponents = view._wantedComponents;
            if (!wantedComponents) {
                view._wantedComponents = wantedComponents = {count:0};
                view.addListener("onComponentRegistered", viewOnComponentRegisteredListener);
            }
            var wanters = wantedComponents[componentId];
            if (!wanters) {
                wantedComponents[componentId] = wanters = [];
                wantedComponents.count++;
            }
            wanters.push({object:object, attribute:attr});
            var idProperty = "_" + attr + "_id";
            if (!object[idProperty]) {
                object[idProperty] = componentId;
            } else {
                var ids = object[idProperty];
                if (typeof ids == "string") {
                    object[idProperty] = ids = [object[idProperty]];
                }
                ids.push(componentId);
            }
            return componentId;
        }
    };
    function viewOnComponentRegisteredListener(view, arg) {
        var wantedComponents = view._wantedComponents;
        var wanters = wantedComponents[arg.component._id];
        if (wanters) {
            var component = arg.component;
            delete wantedComponents[component._id];
            wantedComponents.count--;
            if (wantedComponents.count == 0) {
                view.removeListener("onComponentRegistered", viewOnComponentRegisteredListener);
                delete view._wantedComponent;
            }
            for (var i = 0; i < wanters.length; i++) {
                var wanter = wanters[i], object = wanter.object, attribute = wanter.attribute;
                var ids = object["_" + attribute + "_id"];
                if (ids) {
                    if (typeof ids == "string") {
                        if (ids == component._id) {
                            object.set(attribute, component);
                        }
                    } else {
                        var index = ids.indexOf(component._id);
                        if (index >= 0) {
                            ids[index] = component;
                            var allComponentPrepared = true;
                            for (var j = 0; j < ids.length; j++) {
                                if (typeof ids[j] == "string") {
                                    allComponentPrepared = false;
                                    break;
                                }
                            }
                            if (allComponentPrepared) {
                                object.set(attribute, ids);
                            }
                        }
                    }
                }
            }
        }
    }
    dorado.widget.Component.ALL = {};
    dorado.Toolkits.registerTypeTranslator("widget", function (type) {
        return dorado.util.Common.getClassType("dorado.widget." + type, true);
    });
})();
(function () {
    dorado.widget.DataSet = $extend(dorado.widget.Component, {$className:"dorado.widget.DataSet", ATTRIBUTES:{loadMode:{writeBeforeReady:true}, dataType:{getter:function () {
        return this.getDataType();
    }}, data:{getter:function () {
        return this.getData();
    }, setter:function (a, data) {
        if (this._ready) {
            this.setData(data);
        } else {
            this._data = data;
        }
    }}, dataProvider:{setter:function (a, dp) {
        this["_" + a] = (dp && dp.constructor === String) ? dorado.DataProvider.create(dp) : dp;
    }}, parameter:{}, pageSize:{defaultValue:0}, pageNo:{defaultValue:1}, dataLoaded:{readOnly:true}, readOnly:{}}, EVENTS:{onDataLoad:{}}, _disableObserversCounter:0, constructor:function (id) {
        this._dataPathCache = {};
        this._observers = [];
        $invokeSuper.call(this, arguments);
    }, onReady:function () {
        $invokeSuper.call(this, arguments);
        if (this._observers.length > 0) {
            for (var i = 0; i < this._observers; i++) {
                this.retrievePreloadConfig(this._observers[i]);
            }
            if (this._data) {
                this.setData(this._data);
            } else {
                this.sendMessage(0);
            }
        }
        if (this._loadMode == "onReady") {
            if (this._view) {
                this._view._loadingDataSet.push(this);
            }
            this.getDataAsync();
        }
    }, setData:function (data) {
        var dataType = this.getDataType(null, true);
        if (dataType || this._getDataCalled) {
            if (data != null) {
                if (!(data instanceof dorado.EntityList || data instanceof dorado.Entity)) {
                    data = dorado.DataUtil.convert(data, this.getDataTypeRepository(), dataType);
                }
            }
            if (this._data && (this._data instanceof dorado.EntityList || this._data instanceof dorado.Entity)) {
                this._data._setObserver(null);
            }
            if (data) {
                if (data.dataType == null) {
                    data.dataType = dataType;
                } else {
                    if (dataType && dataType != data.dataType) {
                        var mismatch = true;
                        if (dataType instanceof dorado.EntityDataType && data.dataType) {
                            mismatch = (data.dataType.getElementDataType() != dataType);
                        }
                        if (mismatch) {
                            throw new dorado.ResourceException("dorado.widget.DataTypeNotAccording", this._id);
                        }
                    }
                }
            }
            this._data = data;
            this._dataLoaded = true;
        } else {
            this._data = data;
        }
        if (data && (data instanceof dorado.EntityList || data instanceof dorado.Entity)) {
            data._setObserver(this);
            this._dataPathCache = {};
            this.sendMessage(0);
            this.fireEvent("onDataLoad", this);
        }
    }, doLoad:function (callback) {
        var data = this._data;
        if (data === undefined) {
            if (this._dataProvider) {
                data = this.dataPipe;
                if (!data) {
                    data = this.dataPipe = new dorado.DataSetDataPipe(this);
                }
            }
        }
        if (data instanceof dorado.DataPipe) {
            var pipe = data;
            if (callback) {
                pipe.getAsync({scope:this, callback:function (success, result) {
                    if (success) {
                        this.setData(result);
                    }
                    $callback(callback, success);
                }});
                return;
            } else {
                if (pipe.runningProcNum > 0) {
                    throw new dorado.ResourceException("dorado.widget.GetDataDuringLoading", this._id);
                }
                this.setData(data = pipe.get());
            }
        } else {
            if (callback) {
                $callback(callback, true);
            }
        }
    }, load:function () {
        return this.doLoad();
    }, loadAsync:function (callback) {
        this.doLoad(callback || dorado._NULL_FUNCTION);
    }, doGetData:function (path, options, callback) {
        function evaluatePath(path, options, callback) {
            var data = this._data;
            if (data) {
                if (!(data instanceof dorado.EntityList || data instanceof dorado.Entity)) {
                    this.setData(data);
                    data = this._data;
                }
                var dataPath;
                if (path) {
                    dataPath = dorado.DataPath.create(path);
                }
                var key = (path || "$EMPTY") + "~" + optionsCode;
                var cachedData = this._dataPathCache[key];
                if (cachedData !== undefined) {
                    return cachedData;
                }
                if (data && dataPath) {
                    data = dataPath.evaluate(data, options);
                }
                this._dataPathCache[key] = data;
            }
            return data;
        }
        if (typeof options == "string") {
            options = {loadMode:options};
        } else {
            options = options || {};
        }
        if (options.flush) {
            this.clear();
        }
        var optionsCode, loadMode = options.loadMode || "always";
        optionsCode = loadMode;
        if (options.firstResultOnly) {
            optionsCode += "F";
        }
        if (options.acceptAggregation) {
            optionsCode += "A";
        }
        this._getDataCalled = true;
        if (this._data === undefined && loadMode != "never") {
            if (this._preloadConfigsMap) {
                var preloadConfigs = this._preloadConfigsMap[path || "#EMPTY"];
                if (preloadConfigs) {
                    dorado.DataProvider._SYS_PARAMETER = {preloadConfigs:preloadConfigs};
                }
            }
            if (callback) {
                this.doLoad({scope:this, callback:function (success, result) {
                    if (success) {
                        result = evaluatePath.call(this, path, options);
                    }
                    $callback(callback, success, result);
                }});
                return;
            } else {
                if (loadMode == "auto") {
                    this.doLoad(dorado._NULL_FUNCTION);
                    return;
                } else {
                    this.doLoad();
                }
            }
            dorado.DataProvider._SYS_PARAMETER = null;
        }
        var data = evaluatePath.call(this, path, options);
        if (callback) {
            $callback(callback, true, data);
        } else {
            return data;
        }
    }, getData:function (path, options) {
        options = options || {};
        if (options.firstResultOnly == null) {
            options.firstResultOnly = true;
        }
        if (options.acceptAggregation == null) {
            options.acceptAggregation = true;
        }
        return this.doGetData(path, options);
    }, getDataAsync:function (path, callback, options) {
        options = options || {};
        if (options.firstResultOnly == null) {
            options.firstResultOnly = true;
        }
        if (options.acceptAggregation == null) {
            options.acceptAggregation = true;
        }
        this.doGetData(path, options, callback || dorado._NULL_FUNCTION);
    }, queryData:function (path, options) {
        return this.doGetData(path, options);
    }, queryDataAsync:function (path, callback, options) {
        this.doGetData(path, options, callback || dorado._NULL_FUNCTION);
    }, flush:function () {
        this.getData(null, {flush:true});
    }, flushAsync:function () {
        this.getDataAsync(null, null, {flush:true});
    }, getDataTypeRepository:function () {
        return this._view ? this._view._dataTypeRepository : null;
    }, getDataType:function (path, options) {
        var loadMode;
        if (typeof options == "string") {
            loadMode = options;
        } else {
            loadMode = options ? options.loadMode : undefined;
        }
        var dataType = dorado.LazyLoadDataType.dataTypeGetter.call(this, loadMode);
        if (!dataType && this._data) {
            dataType = this._data.dataType;
        }
        if (dataType) {
            return dorado.DataPath.create(path).getDataType(dataType, options);
        } else {
            return null;
        }
    }, clear:function () {
        delete this._data;
    }, retrievePreloadConfig:function (observer) {
        if (dorado.widget.DataTree && dorado.Object.isInstanceOf(observer, dorado.widget.DataTree)) {
            var bindingConfigs = observer.get("bindingConfigs");
            if (bindingConfigs) {
                var preloadConfigsMap = this._preloadConfigsMap, dataPath = observer._dataPath || "#EMPTY";
                if (!preloadConfigsMap) {
                    this._preloadConfigsMap = preloadConfigsMap = {};
                }
                var preloadConfigs = preloadConfigsMap[dataPath] || [];
                for (var i = 0; i < bindingConfigs.length; i++) {
                    var configs = dorado.widget.DataTree.bindingConfigToPreloadConfig(bindingConfigs[i]);
                    if (configs) {
                        preloadConfigs = preloadConfigs.concat(configs);
                    }
                }
                if (preloadConfigs.length) {
                    preloadConfigsMap[dataPath] = preloadConfigs;
                }
            }
        }
    }, addObserver:function (observer) {
        this._observers.push(observer);
        if (this._ready) {
            this.retrievePreloadConfig(observer);
            observer.dataSetMessageReceived(this, DataSet.MESSAGE_REFRESH);
        }
    }, removeObserver:function (observer) {
        this._observers.remove(observer);
    }, entityMessageReceived:function (messageCode, args) {
        this._dataPathCache = {};
        if (this._ready) {
            this.sendMessage(messageCode, args);
        }
    }, disableObservers:dorado.Entity.prototype.disableObservers, enableObservers:dorado.Entity.prototype.enableObservers, notifyObservers:function () {
        this._dataPathCache = {};
        this.sendMessage(0);
    }, sendMessage:function (messageCode, args) {
        if (this._disableObserversCounter > 0) {
            return;
        }
        var observers = this._observers;
        for (var i = 0; i < observers.length; i++) {
            var observer = observers[i];
            observer.dataSetMessageReceived.call(observer, messageCode, args);
        }
    }});
    dorado.DataSetDataPipe = $extend(dorado.DataProviderPipe, {$className:"dorado.DataSetDataPipe", constructor:function (dataSet) {
        this.dataSet = dataSet;
        this.dataType = dataSet._dataType;
        this.dataTypeRepository = dataSet.get("dataTypeRepository");
        this.view = dataSet.get("view");
    }, getDataProviderArg:function () {
        var parameter = dorado.$this = this.dataSet._parameter;
        return {pageSize:this.dataSet._pageSize, pageNo:this.dataSet._pageNo, parameter:dorado.JSON.evaluate(parameter), dataType:this.dataType, view:this.view};
    }, getDataProvider:function () {
        return this.dataSet._dataProvider;
    }});
    var DataSet = dorado.widget.DataSet;
    DataSet.MESSAGE_REFRESH = 0;
    DataSet.MESSAGE_DATA_CHANGED = dorado.Entity._MESSAGE_DATA_CHANGED;
    DataSet.MESSAGE_ENTITY_STATE_CHANGED = dorado.Entity._MESSAGE_ENTITY_STATE_CHANGED;
    DataSet.MESSAGE_DELETED = dorado.EntityList._MESSAGE_DELETED;
    DataSet.MESSAGE_INSERTED = dorado.EntityList._MESSAGE_INSERTED;
    DataSet.MESSAGE_CURRENT_CHANGED = dorado.EntityList._MESSAGE_CURRENT_CHANGED;
    DataSet.MESSAGE_REFRESH_ENTITY = dorado.EntityList._MESSAGE_REFRESH_ENTITY;
    dorado.widget.DataSetObserver = $class({$className:"dorado.widget.DataSetObserver", dataSetMessageReceived:function (messageCode, arg) {
    }});
})();
(function () {
    var lastMouseDownTarget, lastMouseDownTimestamp = 0;
    dorado.widget.Control = $extend([dorado.widget.Component, dorado.RenderableElement, dorado.Draggable, dorado.Droppable], {$className:"dorado.widget.Control", _ignoreRefresh:1, renderUtilAttached:true, focusable:false, selectable:true, ATTRIBUTES:{width:{setter:function (p, v) {
        this._width = isFinite(v) ? parseInt(v) : v;
        delete this._realWidth;
        this._fixedWidth = !(typeof v == "string" && v.match("%"));
    }}, height:{setter:function (p, v) {
        this._height = isFinite(v) ? parseInt(v) : v;
        delete this._realHeight;
        this._fixedHeight = !(typeof v == "string" && v.match("%"));
    }}, renderTo:{writeOnce:true, writeBeforeReady:true}, renderOn:{writeOnce:true, writeBeforeReady:true}, visible:{defaultValue:true}, hideMode:{defaultValue:"visibility", writeBeforeReady:true}, attached:{readOnly:true}, focused:{readOnly:true}, focusParent:{skipRefresh:true, readOnly:true, getter:function () {
        return this._focusParent || this._parent;
    }}, tip:{}, layoutConstraint:{setter:function (p, v) {
        this._layoutConstraint = v;
        if (this._rendered && this._parent && this._parent._layout) {
            this._parent._layout.refreshControl(this);
        }
    }}, view:{skipRefresh:true, setter:function (attr, view) {
        if (this._view == view) {
            return;
        }
        $invokeSuper.call(this, arguments);
        if (this._innerControls) {
            jQuery.each(this._innerControls, function (i, control) {
                if (this._view) {
                    control.set("view", null);
                }
                control.set("view", view);
            });
        }
    }}}, EVENTS:{onCreateDom:{}, onClick:{}, onDoubleClick:{}, onMouseDown:{}, onMouseUp:{}, onContextMenu:{}, onFocus:{}, onBlur:{}, onKeyDown:{}, onKeyPress:{}}, constructor:function (config) {
        $invokeSuper.call(this, arguments);
        var renderTo = this._renderTo, renderOn = this._renderOn;
        if (renderTo || renderOn) {
            $setTimeout(this, function () {
                var container = renderTo || renderOn;
                if (typeof container == "string") {
                    container = jQuery(container)[0];
                }
                if (container && container.nodeType) {
                    (renderTo) ? this.render(container) : this.replace(container);
                }
            }, 0);
        }
    }, onReady:function () {
        if (this._innerControls) {
            jQuery.each(this._innerControls, function (i, control) {
                if (!(control instanceof dorado.widget.Control) && !control._ready) {
                    control.onReady();
                }
            });
        }
        $invokeSuper.call(this, arguments);
    }, destroy:function () {
        if (this._innerControls) {
            jQuery.each(this._innerControls.slice(0, -1), function (i, control) {
                if (!control._destroyed) {
                    control.destroy();
                }
            });
            delete this._innerControls;
        }
        var isClosed = (window.closed || dorado.windowClosed);
        if (!isClosed && this._parent) {
            if (this._isInnerControl) {
                this._parent.unregisterInnerControl(this);
            } else {
                this._parent.removeChild(this);
            }
        }
        var dom = this._dom;
        if (dom) {
            try {
                dom.doradoUniqueId = null;
            }
            catch (e) {
            }
            if (!isClosed) {
                $fly(dom).remove();
            }
        }
        $invokeSuper.call(this, arguments);
    }, doSet:function (attr, value) {
        $invokeSuper.call(this, arguments);
        if (!this._rendered) {
            return;
        }
        var def = this.ATTRIBUTES[attr];
        if ((this._visible || attr == "visible") && this._ignoreRefresh < 1 && def && !def.skipRefresh) {
            dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", this.refresh, 50);
        }
    }, assignDom:function (dom) {
        dom.style.display = "";
        this._dom = dom;
    }, refresh:function (delay) {
        if (this._duringRefreshDom || !this._rendered || (!this._attached && this.renderUtilAttached)) {
            return;
        }
        if (delay) {
            dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", function () {
                this._duringRefreshDom = true;
                try {
                    dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");
                    this.refreshDom(this.getDom());
                }
                finally {
                    this._duringRefreshDom = false;
                }
            }, 50);
        } else {
            this._duringRefreshDom = true;
            try {
                dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");
                this.refreshDom(this.getDom());
            }
            finally {
                this._duringRefreshDom = false;
            }
        }
    }, refreshDom:function (dom) {
        if (!this.selectable) {
            $DomUtils.disableUserSelection(dom);
        }
        try {
            dom.doradoUniqueId = this._uniqueId;
            if (this._currentVisible !== undefined) {
                if (this._currentVisible != this._visible) {
                    if (this._hideMode == "layout") {
                        if (this._visible) {
                            this.set("layoutConstraint", this._oldLayoutConstraint);
                        } else {
                            this._oldLayoutConstraint = this._layoutConstraint;
                            this.set("layoutConstraint", dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT);
                        }
                    } else {
                        dom.style.visibility = (this._visible) ? "" : "hidden";
                    }
                }
            } else {
                if (!this._visible) {
                    if (this._hideMode == "layout") {
                        this._oldLayoutConstraint = this._layoutConstraint;
                    } else {
                        dom.style.visibility = "hidden";
                    }
                }
            }
            if (this._tip) {
                this._currentTip = this._tip;
                dorado.TipManager.initTip(dom, {text:this._tip});
            } else {
                if (this._currentTip) {
                    dorado.TipManager.deleteTip(dom);
                }
            }
        }
        catch (e) {
        }
        this.applyDraggable(dom);
        this.applyDroppable(dom);
        $invokeSuper.call(this, arguments);
        this._currentVisible = !!this._visible;
    }, resetDimension:function () {
        if (this._skipResetDimension) {
            return;
        }
        var changed = $invokeSuper.call(this, arguments) || !this._fixedWidth || !this._fixedHeight;
        if ((changed || !this._currentVisible) && this.onResize && this._visible) {
            this._skipResetDimension = true;
            this.onResize();
            this._skipResetDimension = false;
        }
        return changed;
    }, notifySizeChange:function () {
        if (!this._parent || !this._rendered) {
            return;
        }
        var layout = this._parent._layout;
        if (layout && layout._attached && layout.onControlSizeChange) {
            dorado.Toolkits.setDelayedAction(this, "$notifySizeChangeTimerId", function () {
                layout.onControlSizeChange(this);
            }, 200);
        }
    }, getDom:function () {
        if (!this._dom) {
            var dom = this._dom = this.createDom();
            var self = this;
            $fly(dom).click(function (evt) {
                if (!self.processDefaultMouseListener()) {
                    return;
                }
                if (self.onClick) {
                    if (self.onClick(evt) === false) {
                        return false;
                    }
                }
                return self.fireEvent("onClick", self, {button:evt.button, event:evt});
            }).bind("dblclick", function (evt) {
                if (!self.processDefaultMouseListener()) {
                    return;
                }
                if (self.onDoubleClick) {
                    if (self.onDoubleClick(evt) === false) {
                        return false;
                    }
                }
                return self.fireEvent("onDoubleClick", self, {button:evt.button, event:evt});
            }).mousedown(function (evt) {
                if (evt.srcElement != lastMouseDownTarget || (new Date() - lastMouseDownTimestamp) > 500) {
                    dorado.widget.setFocusedControl(self);
                    lastMouseDownTarget = evt.srcElement;
                    lastMouseDownTimestamp = new Date();
                }
                if (!self.processDefaultMouseListener()) {
                    return;
                }
                if (self.onMouseDown) {
                    if (self.onMouseDown(evt) === false) {
                        return false;
                    }
                }
                return self.fireEvent("onMouseDown", self, {button:evt.button, event:evt});
            }).mouseup(function (evt) {
                if (!self.processDefaultMouseListener()) {
                    return;
                }
                if (self.onMouseUp) {
                    if (self.onMouseUp(evt) === false) {
                        return false;
                    }
                }
                return self.fireEvent("onMouseUp", self, {button:evt.button, event:evt});
            }).bind("contextmenu", function (evt) {
                evt = jQuery.event.fix(evt || window.event);
                var eventArg = {event:evt, processDefault:true};
                if (self.getListenerCount("onContextMenu")) {
                    eventArg.processDefault = false;
                    self.fireEvent("onContextMenu", self, eventArg);
                }
                if (!eventArg.processDefault) {
                    evt.preventDefault();
                    evt.returnValue = false;
                    return false;
                }
            });
            if (this.focusable) {
                dom.tabIndex = 1;
            }
            this.fireEvent("onCreateDom", this, {dom:dom});
        }
        return this._dom;
    }, processDefaultMouseListener:function () {
        return !this._disabled;
    }, doRenderToOrReplace:function (replace, element, nextChildElement) {
        var renderTarget = this._renderTo || this._renderOn;
        $invokeSuper.call(this, arguments);
        var dom = this._dom;
        if (!dom) {
            return;
        }
        var attached = false;
        if (!renderTarget && this._parent && this._parent != dorado.widget.View.TOP) {
            attached = this._parent._attached;
        } else {
            var body = dom.ownerDocument.body;
            var node = dom.parentNode;
            while (node) {
                if (node == body) {
                    attached = true;
                    break;
                }
                node = node.parentNode;
            }
        }
        if (attached) {
            this.onAttachToDocument();
        } else {
            if (this._attached) {
                this.onDetachFromDocument();
            }
        }
        if (!this._ready) {
            this.onReady();
        }
    }, onAttachToDocument:function () {
        if (this._rendered && !this._attached) {
            var dom = this.getDom();
            this._attached = true;
            this._ignoreRefresh--;
            if (this.renderUtilAttached) {
                this.refreshDom(dom);
            }
            if (this.doOnAttachToDocument) {
                this.doOnAttachToDocument();
            }
            if (this._innerControls) {
                jQuery.each(this._innerControls, function (i, control) {
                    control.onAttachToDocument();
                });
            }
        }
    }, onDetachFromDocument:function () {
        if (this._rendered && this._attached) {
            this._attached = false;
            this._ignoreRefresh++;
            if (this.doOnDetachFromDocument) {
                this.doOnDetachFromDocument();
            }
            if (this._innerControls) {
                jQuery.each(this._innerControls, function (i, control) {
                    control.onDetachFromDocument();
                });
            }
        }
    }, registerInnerControl:function (control) {
        if (!this._innerControls) {
            this._innerControls = [];
        }
        this._innerControls.push(control);
        if (this._attached) {
            control.onAttachToDocument();
        }
        control._isInnerControl = true;
        control._parent = control._focusParent = this;
        control.set("view", (this instanceof dorado.widget.View) ? this : this.get("view"));
    }, unregisterInnerControl:function (control) {
        if (!this._innerControls) {
            return;
        }
        control.onDetachFromDocument();
        this._innerControls.remove(control);
        control._parent = control._focusParent = null;
        control.set("view", null);
        delete control._isInnerControl;
    }, findParent:function (cls) {
        var parent = this._parent;
        while (parent) {
            if (parent instanceof cls) {
                return parent;
            }
            parent = parent._parent;
        }
        return null;
    }, setFocus:function () {
        var dom = this._dom;
        if (dom) {
            setTimeout(function () {
                try {
                    dom.focus();
                }
                catch (e) {
                }
            }, 0);
        }
    }, isFocusable:function (deep) {
        if (!this.focusable || !this._rendered || !this.getDom() || this._disabled) {
            return false;
        }
        if (deep) {
            var parent = this._parent;
            while (parent) {
                if (!parent._rendered) {
                    break;
                }
                if (parent.isChildrenFocusable && !parent.isChildrenFocusable(false)) {
                    return false;
                }
                parent = parent._parent;
            }
        }
        var dom = this.getDom();
        return !dom.disabled && dom.offsetWidth > 0;
    }, onFocus:function () {
        this._focused = true;
        if (this.doOnFocus) {
            this.doOnFocus();
        }
        if (this._className) {
            $fly(this.getDom()).addClass(this._className + "-focused");
        }
        this.fireEvent("onFocus", this);
    }, onBlur:function () {
        this._focused = false;
        if (this.doOnBlur) {
            this.doOnBlur();
        }
        $fly(this.getDom()).removeClass(this._className + "-focused");
        this.fireEvent("onBlur", this);
    }, onKeyDown:function (evt) {
        var b = true;
        if (this.getListenerCount("onKeyDown")) {
            var arg = {keyCode:evt.keyCode, shiftKey:evt.shiftKey, ctrlKey:evt.ctrlKey, altlKey:evt.altlKey, event:evt};
            this.fireEvent("onKeyDown", this, arg);
            b = arg.returnValue;
        }
        if (!b) {
            return b;
        }
        var b = this.doOnKeyDown ? this.doOnKeyDown(evt) : true;
        if (!b) {
            return b;
        }
        var fp = this.get("focusParent");
        if (fp && dorado.widget.disableKeyBubble != fp) {
            b = fp.onKeyDown(evt);
        }
        return b;
    }, onKeyPress:function (evt) {
        var b = true;
        if (this.getListenerCount("onKeyPress")) {
            var arg = {keyCode:evt.keyCode, shiftKey:evt.shiftKey, ctrlKey:evt.ctrlKey, altlKey:evt.altlKey, event:evt};
            this.fireEvent("onKeyPress", this, arg);
            b = arg.returnValue;
        }
        if (!b) {
            return b;
        }
        var b = this.doOnKeyPress ? this.doOnKeyPress(evt) : true;
        if (!b) {
            return b;
        }
        var fp = this.get("focusParent");
        if (fp && dorado.widget.disableKeyBubble != fp) {
            b = fp.onKeyDown(evt);
        }
        return b;
    }, initDraggingInfo:function (draggingInfo, evt) {
        $invokeSuper.call(this, arguments);
        draggingInfo.set({object:this, sourceControl:this});
    }, onDraggingSourceOver:function (draggingInfo, evt) {
        draggingInfo.set({targetObject:this, targetControl:this});
        return $invokeSuper.call(this, arguments);
    }, onDraggingSourceOut:function (draggingInfo, evt) {
        var retval = $invokeSuper.call(this, arguments);
        draggingInfo.set({targetObject:null, targetControl:null});
        return retval;
    }});
    dorado.widget.disableKeyBubble = false;
    dorado.widget.focusedControl = [];
    dorado.widget.onControlGainedFocus = function (control) {
        if (dorado.widget.focusedControl && dorado.widget.focusedControl.peek() == control) {
            return;
        }
        var ov = dorado.widget.focusedControl;
        var nv = [];
        if (control) {
            var c = control;
            while (c) {
                nv.push(c);
                var focusParent = c.get("focusParent");
                if (!focusParent) {
                    break;
                }
                c = focusParent;
            }
            nv = nv.reverse();
        }
        var i = ov.length - 1;
        for (; i >= 0; i--) {
            var o = ov[i];
            if (o == nv[i]) {
                break;
            }
            if (o.onBlur) {
                o.onBlur();
            }
        }
        dorado.widget.focusedControl = nv;
        i++;
        for (; i < nv.length; i++) {
            if (nv[i].onFocus) {
                nv[i].onFocus();
            }
        }
    };
    dorado.widget.onControlLostFocus = function (control) {
        if (!control.get("focused")) {
            return;
        }
        do {
            control = control.get("focusParent");
        } while (control && !control.isFocusable());
        dorado.widget.onControlGainedFocus(control);
    };
    dorado.widget.setFocusedControl = function (control) {
        if (dorado.widget.focusedControl && dorado.widget.focusedControl.peek() == control) {
            return;
        }
        while (control && !control.isFocusable()) {
            control = control.get("focusParent");
        }
        if (control) {
            control.setFocus();
            dorado.widget.onControlGainedFocus(control);
        } else {
            if (document.body) {
                document.body.focus();
            }
            dorado.widget.onControlGainedFocus(null);
        }
    };
    dorado.widget.getMainFocusedControl = function () {
        var v = dorado.widget.focusedControl;
        for (var i = v.length - 1; i >= 0; i--) {
            if (!v[i]._focusParent) {
                return v[i];
            }
        }
        return v[0];
    };
    dorado.widget.getFocusedControl = function () {
        var v = dorado.widget.focusedControl;
        return v.peek();
    };
    dorado.widget.findFocusableControlInElement = function (element) {
        function findInChildren(element) {
            var el = element.firstChild, control = null;
            while (el) {
                control = findInChildren(el);
                if (control) {
                    break;
                }
                if (el.doradoUniqueId) {
                    c = dorado.widget.Component.ALL[el.doradoUniqueId];
                    if (c && c.isFocusable()) {
                        control = c;
                        break;
                    }
                }
                el = el.nextSibling;
            }
            return control;
        }
        return findInChildren(element);
    };
    function findFirstChild(c) {
        var t = null;
        if (c instanceof dorado.widget.Container && c.isChildrenFocusable()) {
            var it = c._children.iterator();
            while (it.hasNext()) {
                t = it.next();
                if (t instanceof dorado.widget.Control && t.isFocusable()) {
                    t = findFirstChild(t);
                    if (t) {
                        break;
                    }
                }
            }
        }
        if (!t && c.isFocusable()) {
            t = c;
        }
        return t;
    }
    function findNext(c) {
        var p = c._parent;
        if (p && p._children) {
            var it = p._children.iterator(c);
            while (it.hasNext()) {
                var t = it.next();
                if (t instanceof dorado.widget.Control && t.isFocusable()) {
                    t = findFirstChild(t);
                    if (t) {
                        return t;
                    }
                }
            }
        }
        return null;
    }
    function findLastChild(c) {
        var t = null;
        if (c instanceof dorado.widget.Container && c.isChildrenFocusable()) {
            var it = c._children.iterator();
            it.last();
            while (it.hasPrevious()) {
                t = it.previous();
                if (t instanceof dorado.widget.Control && t.isFocusable()) {
                    t = findLastChild(t);
                    if (t) {
                        break;
                    }
                }
            }
        }
        if (!t && c.isFocusable()) {
            t = c;
        }
        return t;
    }
    function findPrevious(c) {
        var p = c._parent;
        if (p && p._children) {
            var it = p._children.iterator(c);
            while (it.hasPrevious()) {
                var t = it.previous();
                if (t instanceof dorado.widget.Control && t.isFocusable()) {
                    t = findLastChild(t);
                    if (t) {
                        return t;
                    }
                }
            }
        }
        return null;
    }
    dorado.widget.findNextFocusableControl = function (control) {
        var c = control || dorado.widget.getMainFocusedControl();
        while (c) {
            var t = findNext(c);
            if (t) {
                return t;
            }
            c = c._parent;
        }
        return findFirstChild(dorado.widget.View.TOP);
    };
    dorado.widget.findPreviousFocusableControl = function (control) {
        var c = control || dorado.widget.getMainFocusedControl();
        while (c) {
            var t = findPrevious(c);
            if (t) {
                return t;
            }
            c = c._parent;
        }
        return findLastChild(dorado.widget.View.TOP);
    };
})();
dorado._queueObject = {};
dorado.queue = function (namespace, fn) {
    if (!namespace) {
        return;
    }
    var queue = dorado._queueObject[namespace];
    if (!queue) {
        queue = dorado._queueObject[namespace] = [];
    }
    queue.push(fn);
    if (!queue.processing) {
        dorado.dequeue(namespace);
    }
};
dorado.dequeue = function (namespace) {
    if (!namespace) {
        return true;
    }
    var queue = dorado._queueObject[namespace];
    if (queue) {
        if (queue.length > 0) {
            queue.processing = true;
            var fn = queue.shift();
            fn.call(null, []);
        } else {
            queue.processing = false;
        }
    }
};
(function () {
    var SHOWHIDE_SUFFIX = "_SHOWHIDE";
    dorado.widget.FloatControl = $class({$className:"dorado.widget.FloatControl", ATTRIBUTES:{floating:{defaultValue:true, setter:function (attr, value) {
        var attributeWatcher = this.getAttributeWatcher();
        if (attributeWatcher.getWritingTimes("visible") == 0) {
            this._visible = !value;
        }
        this._floating = value;
    }}, floatingClassName:{defaultValue:"d-floating"}, visible:{defaultValue:false}, animateType:{defaultValue:"zoom", skipRefresh:true}, showAnimateType:{skipRefresh:true}, hideAnimateType:{skipRefresh:true}, animateTarget:{skipRefresh:true}, left:{}, top:{}, center:{skipRefresh:true}, anchorTarget:{skipRefresh:true}, positionOffset:{skipRefresh:true}, align:{skipRefresh:true}, vAlign:{skipRefresh:true}, autoAdjustPosition:{skipRefresh:true, defaultValue:true}, handleOverflow:{skipRefresh:true, defaultValue:true}, modal:{skipRefresh:true}, modalType:{skipRefresh:true, defaultValue:"dark"}, shadowMode:{defaultValue:"sides"}, focusAfterShow:{defaultValue:true}}, EVENTS:{beforeShow:{}, onShow:{}, beforeHide:{}, onHide:{}}, show:function (options) {
        options = options || {};
        var control = this;
        var attrs = ["autoAdjustPosition", "handleOverflow", "positionOffset", "align", "vAlign", "handleOverflow", "anchorTarget"];
        for (var i = 0; i < attrs.length; i++) {
            var attr = attrs[i], value = options[attr];
            if (value === undefined) {
                options[attr] = control["_" + attr];
            }
        }
        dorado.queue(control._id + SHOWHIDE_SUFFIX, function () {
            options = options || {};
            if (!control._rendered) {
                var renderTo = control._renderTo;
                if (renderTo) {
                    if (renderTo instanceof dorado.widget.Container) {
                        renderTo = renderTo.get("containerDom");
                    } else {
                        if (typeof renderTo == "string") {
                            renderTo = jQuery(document.body).find(renderTo)[0];
                        } else {
                            if (!renderTo.nodeName) {
                                renderTo = null;
                            }
                        }
                    }
                }
                var oldVisible = control._visible;
                control._visible = true;
                control.render(renderTo);
                control._visible = oldVisible;
            }
            if (control.doShow) {
                control.doShow.apply(control, [options]);
            }
        });
    }, doShow:function (options) {
        var control = this, dom = control.getDom(), anim = true, handleModal = true;
        $fly(dom).css({display:"", visibility:"hidden", left:-99999, top:-99999});
        var arg = {};
        control.fireEvent("beforeShow", control, arg);
        if (arg.processDefault === false) {
            dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
            return;
        }
        if (control._visible) {
            handleModal = false;
        }
        control._visible = true;
        var position = control.getShowPosition(options);
        options.position = position;
        options.animateTarget = control._animateTarget;
        if (handleModal && control._modal) {
            dorado.ModalManager.show(dom, dorado.widget.FloatControl.modalTypeClassName[control._modalType]);
        }
        var animateType = options.animateType || control._showAnimateType || control._animateType;
        if (anim && animateType != "none") {
            control.fireEvent("onShow", control);
            var behavior = dorado.widget.FloatControl.behaviors[animateType];
            if (typeof behavior.show == "function") {
                behavior.show.apply(control, [options]);
            }
        } else {
            $fly(dom).css(position);
            control.fireEvent("onShow", control);
            control.doAfterShow.apply(control, [options]);
        }
    }, doAfterShow:function () {
        var control = this, dom = control.getDom();
        if (dom) {
            $fly(dom).css({visibility:"", display:""}).bringToFront();
            if (control._shadowMode != "none") {
                $fly(dom).shadow({mode:control._shadowMode || "sides"});
            }
            if (control.isFocusable() && control._focusAfterShow) {
                dorado.widget.setFocusedControl(control);
            }
        }
        dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
    }, getShowPosition:function (options) {
        var control = this, anchorTarget = options.anchorTarget, position = options.position, dom = control.getDom(), event = options.event, fixedElement, result;
        if (anchorTarget) {
            if (anchorTarget instanceof dorado.widget.Control) {
                fixedElement = anchorTarget._dom;
                control._focusParent = anchorTarget;
            } else {
                if (dorado.Object.isInstanceOf(anchorTarget, dorado.RenderableElement)) {
                    fixedElement = anchorTarget._dom;
                } else {
                    if (typeof anchorTarget == "string") {
                        fixedElement = jQuery(anchorTarget)[0];
                    } else {
                        fixedElement = anchorTarget;
                    }
                }
            }
            result = $DomUtils.dockAround(dom, fixedElement, options);
        } else {
            if (position) {
                result = $DomUtils.locateIn(dom, options);
            } else {
                if (event) {
                    options.position = {left:event.pageX, top:event.pageY};
                    result = $DomUtils.locateIn(dom, options);
                } else {
                    if (control._center && control._left == undefined && control._top == undefined) {
                        var docSize = {width:$fly(window).width(), height:$fly(window).height()};
                        control._left = (docSize.width - $fly(dom).width()) / 2 + jQuery(window).scrollLeft();
                        control._top = (docSize.height - $fly(dom).height()) / 2 + jQuery(window).scrollTop();
                    }
                    options.position = {left:control._left || 0, top:control._top || 0};
                    result = $DomUtils.locateIn(dom, options);
                }
            }
        }
        return result;
    }, hide:function (options) {
        var control = this, args = arguments;
        if (!control._visible) {
            dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
            return;
        }
        dorado.queue(control._id + SHOWHIDE_SUFFIX, function () {
            var arg = {};
            control.fireEvent("beforeHide", control, arg);
            if (arg.processDefault === false) {
                dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
                return;
            }
            options = options || {};
            if (control.doHide) {
                control.doHide.apply(control, args);
            }
            if (control._focused) {
                dorado.widget.setFocusedControl(control._focusParent);
            }
        });
    }, doHide:function (options) {
        var control = this, dom = control._dom;
        if (dom) {
            options = options || {};
            if (control._modal) {
                dorado.ModalManager.hide(dom);
            }
            control._visible = false;
            var animateType = options.animateType || control._hideAnimateType || control._animateType;
            options.animateTarget = control._animateTarget;
            if (animateType != "none") {
                var behavior = dorado.widget.FloatControl.behaviors[animateType];
                if (typeof behavior.hide == "function") {
                    behavior.hide.apply(control, [options]);
                }
            } else {
                if (control._shadowMode != "none") {
                    $fly(control._dom);
                }
                control.doAfterHide();
            }
        }
    }, doAfterHide:function () {
        var control = this, dom = control._dom;
        control.fireEvent("onHide", control);
        $fly(dom).css({visibility:"hidden", display:"none"});
        dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
    }});
    var slideShow = function (options, safe) {
        var control = this, align = options.align, vAlign = options.vAlign, direction = options.direction, dom = control._dom;
        $fly(dom).css("visibility", "");
        if (!direction && (vAlign && align)) {
            if (vAlign.indexOf("inner") != -1) {
                direction = align.indexOf("right") != -1 ? "l2r" : "r2l";
            } else {
                direction = vAlign.indexOf("bottom") != -1 ? "t2b" : "b2t";
            }
        }
        direction = direction || "t2b";
        control._slideInDir = direction;
        var position = options.position || {};
        $fly(dom).css(position).bringToFront()[safe ? "safeSlideIn" : "slideIn"]({duration:options.animateDuration || 200, easing:options.animateEasing, direction:direction, complete:function () {
            control.doAfterShow.apply(control, [options]);
            dom.style.display = "";
        }});
    };
    var slideHide = function (options, safe) {
        var control = this, dom = control._dom, direction = control._slideInDir;
        switch (direction) {
          case "l2r":
            direction = "r2l";
            break;
          case "r2l":
            direction = "l2r";
            break;
          case "b2t":
            direction = "t2b";
            break;
          case "t2b":
            direction = "b2t";
            break;
        }
        control._slideInDir = null;
        $fly(dom)[safe ? "safeSlideOut" : "slideOut"]({direction:direction, duration:options.animateDuration || 200, easing:options.animateEasing, complete:function () {
            control.doAfterHide.apply(control, arguments);
        }});
    };
    dorado.widget.FloatControl.modalTypeClassName = {dark:"d-modal-mask", transparent:"d-modal-mask-transparent"};
    dorado.widget.FloatControl.behaviors = {zoom:{show:function (options) {
        var control = this, dom = control._dom;
        $fly(dom).zoomIn(jQuery.extend(options, {duration:options.animateDuration || 200, easing:options.animateEasing, complete:function () {
            control.doAfterShow.apply(control, [options]);
        }}));
    }, hide:function (options) {
        var control = this, dom = control._dom;
        $fly(dom).css("visibility", "hidden").zoomOut(jQuery.extend(options, {duration:options.animateDuration || 200, easing:options.animateEasing, complete:function () {
            control.doAfterHide.apply(control, arguments);
        }}));
    }}, slide:{show:function (options) {
        slideShow.apply(this, [options]);
    }, hide:function (options) {
        slideHide.apply(this, [options]);
    }}, safeSlide:{show:function (options) {
        slideShow.apply(this, [options, true]);
    }, hide:function (options) {
        slideHide.apply(this, [options, true]);
    }}, fade:{show:function (options) {
        var control = this, dom = control._dom;
        $fly(dom).disableShadow().bringToFront().css({visibility:"", opacity:0}).animate({opacity:1}, {duration:options.animateDuration || 200, easing:options.animateEasing, complete:function () {
            $fly(dom).css({opacity:""});
            control.doAfterShow.apply(control, [options]);
            $fly(dom).enableShadow();
        }});
    }, hide:function (options) {
        var control = this, dom = control._dom;
        $fly(dom).disableShadow().animate({opacity:0}, {duration:options.animateDuration || 200, easing:options.animateEasing, complete:function () {
            $fly(dom).css({opacity:""});
            control.doAfterHide.apply(control, arguments);
            $fly(dom).enableShadow();
        }});
    }}};
})();
(function () {
    var fireParentChanged = true;
    dorado.widget.Container = $extend(dorado.widget.Control, {$className:"dorado.widget.Container", ATTRIBUTES:{className:{defaultValue:"d-container"}, layout:{writeBeforeReady:true, setter:function (a, layout) {
        if (this._layout) {
            this._layout.set("container", null);
        }
        if (layout && !(layout instanceof dorado.widget.layout.Layout)) {
            layout = dorado.Toolkits.createInstance("layout", layout, function (type) {
                type = type || "Dock";
                return dorado.util.Common.getClassType("dorado.widget.layout." + type + "Layout", true);
            });
        }
        this._layout = layout;
        if (layout) {
            layout.set("container", this);
        }
    }, getter:function () {
        if (!this._rendered && this._layout === undefined) {
            this._ignoreRefresh++;
            try {
                this.createDefaultLayout();
            }
            finally {
                this._ignoreRefresh--;
            }
        }
        return this._layout;
    }}, children:{setter:function (p, children) {
        if (!children || children.length < 1) {
            return;
        }
        for (var i = 0; i < children.length; i++) {
            var child = children[i];
            if (child instanceof dorado.widget.Component) {
                this.addChild(child);
            } else {
                if (child.$type) {
                    this.addChild(dorado.Toolkits.createInstance("widget", child));
                }
            }
        }
    }}, contentOverflow:{skipRefresh:true, defaultValue:"auto", setter:function (attr, value) {
        var container = this;
        if (container._rendered) {
            var contentContainer = container.getContentContainer();
            if (contentContainer) {
                contentContainer.style.overflow = value;
            }
        }
        container._contentOverflow = value;
    }}, view:{setter:function (attr, view) {
        if (this._view == view) {
            return;
        }
        $invokeSuper.call(this, arguments);
        this._children.each(function (child) {
            if (this._view) {
                child.set("view", null);
            }
            child.set("view", view);
        }, this);
    }}, containerDom:{readOnly:true, getter:function () {
        return this._container;
    }}}, constructor:function (id) {
        this._children = new dorado.util.KeyedList(dorado._GET_ID);
        $invokeSuper.call(this, arguments);
    }, createDefaultLayout:function () {
        this.set("layout", new dorado.widget.layout.DockLayout());
    }, onReady:function () {
        this._children.each(function (child) {
            if (!(child instanceof dorado.widget.Control) && !child._ready) {
                child.onReady();
            }
            if (dorado.Object.isInstanceOf(child, dorado.widget.FloatControl) && !child._ready && child._floating && child._visible) {
                child.show();
            }
        });
        $invokeSuper.call(this, arguments);
    }, destroy:function () {
        jQuery.each(this._children.toArray(), function (i, child) {
            if (!child._destroyed) {
                child.destroy();
            }
        });
        $invokeSuper.call(this, arguments);
    }, doRenderToOrReplace:function (replace, element) {
        if (replace && this._children.size == 0 && element.childNodes.length > 0) {
            var children = [];
            for (var i = 0; i < element.childNodes.length; i++) {
                children.push(element.childNodes[i]);
            }
            var htmlContrainer = new dorado.widget.HtmlContainer({content:children});
            this.addChild(htmlContrainer);
        }
        $invokeSuper.call(this, arguments);
    }, addChild:function (component) {
        if (component._parent) {
            fireParentChanged = false;
            component._parent.removeChild(component);
            fireParentChanged = true;
        }
        this._children.insert(component);
        if (dorado.widget.View.TOP != this) {
            component._parent = this;
        }
        component.set("view", (this instanceof dorado.widget.View) ? this : this.get("view"));
        if (fireParentChanged && component.parentChanged) {
            component.parentChanged();
        }
        if (component instanceof dorado.widget.Control) {
            var layout = this.get("layout");
            if (layout) {
                if (!(dorado.Object.isInstanceOf(component, dorado.widget.FloatControl) && component._floating)) {
                    var shouldFireOnAttach = (this._attached && layout._regions.size == 0 && !layout._rendered);
                    if (!component._visible && component._hideMode == "layout") {
                        component._layoutConstraint = dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT;
                    }
                    layout.addControl(component);
                    if (shouldFireOnAttach) {
                        layout.onAttachToDocument(this.getContentContainer());
                    }
                }
            }
        }
        if (!(component instanceof dorado.widget.Control) && !component._ready && this._ready) {
            component.onReady.call(component);
        }
    }, removeChild:function (component) {
        this._children.remove(component);
        component.set("view", null);
        component._parent = null;
        dorado.widget.View.TOP.addChild(component);
        if (fireParentChanged && component.parentChanged) {
            component.parentChanged();
        }
        if (component instanceof dorado.widget.Control) {
            var layout = this.get("layout");
            if (layout) {
                layout.removeControl(component);
            }
        }
    }, removeAllChildren:function () {
        this._children.each(function (child) {
            this.removeChild(child);
        }, this);
    }, getContentContainer:function () {
        return this.getDom();
    }, doOnAttachToDocument:function () {
        var contentCt = this.getContentContainer();
        if (contentCt.nodeType && contentCt.nodeType == 1 && !contentCt.style.overflow) {
            contentCt.style.overflow = this._contentOverflow;
        }
        var layout = this.get("layout");
        if (layout && !(layout._regions.size == 0 && !layout._rendered)) {
            layout.onAttachToDocument(this.getContentContainer());
        }
    }, doOnDetachToDocument:function () {
        var layout = this.get("layout");
        if (layout) {
            layout.onDetachToDocument();
        }
    }, onResize:function () {
        var layout = this.get("layout");
        if (layout && layout._attached) {
            layout.onResize();
        }
    }, isChildrenFocusable:function (deep) {
        if (!this._rendered || !this.getDom()) {
            return false;
        }
        if (deep) {
            var parent = this._parent;
            while (parent) {
                if (!parent._rendered) {
                    break;
                }
                if (!parent.isChildrenFocusable(false)) {
                    return false;
                }
                parent = parent._parent;
            }
        }
        var dom = this.getDom();
        return !dom.disabled && dom.offsetWidth > 0;
    }});
})();
(function () {
    var ALL_VIEWS = [];
    dorado.widget.View = $extend(dorado.widget.Container, {$className:"dorado.widget.View", ATTRIBUTES:{dataTypeRepository:{getter:function (p) {
        return this["_" + p] || $dataTypeRepository;
    }}, className:{defaultValue:"d-view"}, width:{defaultValue:"100%"}, height:{defaultValue:"100%"}, name:{}, view:{setter:function (attr, view) {
        this._view = view;
    }}, context:{getter:function () {
        if (this._context == null) {
            this._context = {};
        }
        return this._context;
    }, setter:function (p, ctx) {
        if (ctx instanceof Object) {
            this._context = ctx;
        }
    }}}, EVENTS:{onDataLoaded:{}, onComponentRegistered:{}, onComponentUnregistered:{}}, constructor:function (id) {
        ALL_VIEWS.push(this);
        this._identifiedComponents = {};
        this._loadingDataSet = [];
        if (id == "$TOP_VIEW") {
            this._dataTypeRepository = dorado.DataTypeRepository.ROOT;
        } else {
            this._dataTypeRepository = new dorado.DataTypeRepository(dorado.DataTypeRepository.ROOT);
        }
        this._dataTypeRepository.view = this;
        $invokeSuper.call(this, arguments);
    }, destroy:function () {
        ALL_VIEWS.remove(this);
        $invokeSuper.call(this, arguments);
    }, createDefaultLayout:function () {
        if (this._id != "$TOP_VIEW") {
            $invokeSuper.call(this, arguments);
        }
    }, parentChanged:function () {
        if (this._parent) {
            do {
                var container = this._parent;
                if (container instanceof dorado.widget.View) {
                    this._dataTypeRepository.parent = container._dataTypeRepository;
                    break;
                }
                container = container._parent;
            } while (container != null);
        } else {
            this._dataTypeRepository.parent = dorado.DataTypeRepository.ROOT;
        }
    }, registerComponent:function (id, comp) {
        if (this._identifiedComponents[id]) {
            throw new dorado.ResourceException("dorado.widget.ComponentIdNotUnique", id, this._id);
        }
        this._identifiedComponents[id] = comp;
        this.fireEvent("onComponentRegistered", this, {component:comp});
    }, unregisterComponent:function (id) {
        var comp = this._identifiedComponents[id];
        delete this._identifiedComponents[id];
        this.fireEvent("onComponentUnregistered", this, {component:comp});
    }, getListenerScope:function () {
        return this;
    }, id:function (id) {
        var comp = this._identifiedComponents[id];
        if (!comp && dorado.widget.View.DEFAULT_COMPONENTS) {
            comp = dorado.widget.View.getDefaultComponent(this, id);
            if (comp) {
                this.registerComponent(id, comp);
            }
        }
        return comp;
    }, tag:function (tags) {
        var group = dorado.TagManager.find(tags), objects = group.objects;
        for (var i = 0; i < objects.length; i++) {
            var object = objects[i];
            if (object._view != this && object.view != this) {
                objects.remove(object);
            }
        }
        return group;
    }, getComponentReference:function (id) {
        var comp = this.id(id);
        return comp || {view:this, component:id};
    }, getDataType:function (name) {
        return this._dataTypeRepository.get(name);
    }, getDataTypeAsync:function (name, callback) {
        return this._dataTypeRepository.getAsync(id, callback);
    }, onReady:function () {
        $invokeSuper.call(this, arguments);
        $waitFor(this._loadingDataSet, $scopify(this, this.onDataLoaded));
        this._loadingDataSet = [];
    }, onDataLoaded:function () {
        this.fireEvent("onDataLoaded", this);
    }, render:function (containerElement) {
        var bodyWidth;
        if (containerElement == document.body) {
            bodyWidth = document.body.clientWidth;
        }
        $invokeSuper.call(this, arguments);
        if (bodyWidth && bodyWidth > document.body.clientWidth) {
            this.onResize();
        }
    }});
    dorado.widget.View.registerDefaultComponent = function (id, component) {
        var comps = this.DEFAULT_COMPONENTS = this.DEFAULT_COMPONENTS || {};
        comps[id] = component;
    };
    dorado.widget.View.getDefaultComponent = function (view, id) {
        var comps = this.DEFAULT_COMPONENTS;
        if (!comps || !comps[id]) {
            return;
        }
        var comp = comps[id];
        if (comp instanceof Function) {
            comp = comp(view);
        }
        return comp;
    };
    var topView = new dorado.widget.View("$TOP_VIEW");
    topView.isChildrenFocusable = function () {
        return true;
    };
    dorado.widget.View.TOP = topView;
    window.$view = topView;
    window.$id = function (id) {
        var components = [];
        for (var i = 0; ALL_VIEWS.length; i++) {
            var view = ALL_VIEWS[i];
            var component = view.id(id);
            if (component) {
                components.push(component);
            }
        }
        return new dorado.ObjectGroup(components);
    };
    window.$waitFor = dorado.widget.View.waitFor = function (tasks, callback) {
        if (!(tasks instanceof Array)) {
            tasks = [tasks];
        }
        var simTasks = [];
        jQuery.each(tasks, function (i, task) {
            if (task instanceof dorado.widget.DataSet) {
                simTasks.push({callback:dorado._NULL_FUNCTION, run:function (callback) {
                    task.loadAsync(callback);
                }});
            } else {
                if (task instanceof Function) {
                    simTasks.push({callback:dorado._NULL_FUNCTION, run:task});
                } else {
                    simTasks.push(task);
                }
            }
        });
        dorado.Callback.simultaneousCallbacks(simTasks, callback);
    };
    jQuery().ready(function () {
        function getControlByElement(el) {
            var node = $DomUtils.findParent(el, function (node) {
                return (!!node.doradoUniqueId);
            });
            var control = null;
            if (node) {
                control = dorado.widget.Component.ALL[node.doradoUniqueId];
            }
            return control;
        }
        $fly(document).mousedown(function (evt) {
            var control = getControlByElement(evt.target);
            if (control == null) {
                dorado.widget.setFocusedControl(null);
            }
        }).keydown(function (evt) {
            var b, c = dorado.widget.getFocusedControl();
            if (c) {
                b = c.onKeyDown(evt);
            }
            if (b === false) {
                evt.preventDefault();
                evt.cancelBubble = true;
                return false;
            } else {
                if (b === true) {
                    switch (evt.keyCode || evt.which) {
                      case 9:
                      case 13:
                        var c = (evt.shiftKey) ? dorado.widget.findPreviousFocusableControl() : dorado.widget.findNextFocusableControl();
                        if (c && c.isFocusable(true)) {
                            c.setFocus();
                        }
                    }
                }
                return true;
            }
        }).keypress(function (evt) {
            var b, c = dorado.widget.getFocusedControl();
            if (c) {
                b = c.onKeyPress(evt);
            }
            if (b === false) {
                evt.preventDefault();
                evt.cancelBubble = true;
                return false;
            } else {
                return true;
            }
        });
        var cls = "d-unknown-browser", b = dorado.Browser, v = b.version;
        if (b.msie) {
            cls = "d-ie";
        } else {
            if (b.mozilla) {
                cls = "d-mozilla";
            } else {
                if (b.chrome) {
                    cls = "d-chrome";
                } else {
                    if (b.safari) {
                        cls = "d-safari";
                    } else {
                        if (b.opera) {
                            cls = "d-opera";
                        }
                    }
                }
            }
        }
        if (v) {
            var i = v.indexOf(".");
            cls += " " + cls + ((i > 0) ? v.substring(0, i) : v);
        }
        $fly(document.body).addClass(cls).focusin(function (evt) {
            var control = getControlByElement(evt.target);
            if (control) {
                dorado.widget.onControlGainedFocus(control);
            }
        });
        setTimeout(function () {
            topView.onReady();
            $fly(window).unload(function () {
                dorado.windowClosed = true;
                if (!topView._destroyed) {
                    topView.destroy();
                }
            }).bind("resize", function () {
                if (topView.onResizeTimerId) {
                    clearTimeout(topView.onResizeTimerId);
                    delete topView.onResizeTimerId;
                }
                topView.onResizeTimerId = setTimeout(function () {
                    delete topView.onResizeTimerId;
                    topView._children.each(function (child) {
                        if (child.resetDimension) {
                            child.resetDimension();
                        }
                    });
                }, 200);
            });
        }, 30);
    });
})();
dorado.widget.SubViewHolder = $extend(dorado.widget.Control, {$className:"dorado.widget.SubViewHolder", ATTRIBUTES:{subView:{writeBeforeReady:true}}, createDom:function (dom) {
    var dom = document.createElement("DIV");
    var subView = this._subView;
    if (subView) {
        this.registerInnerControl(subView);
        subView.render(dom);
    }
    return dom;
}});
dorado.widget.HtmlContainer = $extend(dorado.widget.Container, {$className:"dorado.widget.HtmlContainer", ATTRIBUTES:{className:{defaultValue:"d-html-container"}, content:{}, containerExpression:{}}, createDom:function () {
    var dom;
    var content = this._content;
    if (content) {
        if (content.constructor == String) {
            var div = document.createElement("DIV");
            div.innerHTML = this._content;
            dom = div;
        } else {
            var doms = [];
            this._xCreateContext = {};
            this.pushHtmlElement(doms, this._content);
            if (doms.length == 1) {
                dom = doms[0];
            } else {
                if (doms.length > 1) {
                    var div = document.createElement("DIV");
                    for (var i = 0; i < doms.length; i++) {
                        div.appendChild(doms[i]);
                    }
                    dom = div;
                }
            }
        }
    }
    if (!dom) {
        dom = document.createElement("DIV");
    }
    var container = dom;
    if (this._containerExpression) {
        var jq = $fly(container).find(this._containerExpression);
        if (jq && jq.length > 0) {
            this._container = container = jq[0];
        }
    }
    return dom;
}, pushHtmlElement:function (doms, content) {
    function doPush(doms, content, context) {
        if (!content) {
            return;
        }
        if (content.constructor == String) {
            var div = this._DIV_FOR_INNERHTML;
            if (!div) {
                this._DIV_FOR_INNERHTML = div = document.createElement("DIV");
            }
            div.innerHTML = content;
            while (div.firstChild) {
                var node = div.firstChild;
                div.removeChild(node);
                if (dorado.Browser.msie && node.nodeType == 3) {
                    var span = document.createElement("SPAN");
                    span.appendChild(node);
                    node = span;
                }
                doms.push(node);
            }
        } else {
            if (content.nodeType) {
                doms.push(content);
            } else {
                doms.push($DomUtils.xCreateElement(content, null, context));
            }
        }
    }
    if (content instanceof Array) {
        for (var i = 0; i < content.length; i++) {
            doPush(doms, content[i], this._xCreateContext);
        }
    } else {
        doPush(doms, content, this._xCreateContext);
    }
}, getSubDom:function (contextKey) {
    this.getDom();
    return this._xCreateContext ? this._xCreateContext[contextKey] : null;
}});
dorado.widget.DataControl = $extend(dorado.widget.DataSetObserver, {$className:"dorado.widget.DataControl", ATTRIBUTES:{dataSet:{componentReference:true, setter:function (a, dataSet) {
    if (this._dataSet) {
        this._dataSet.removeObserver(this);
    }
    this._dataSet = dataSet;
    if (dataSet) {
        dataSet.addObserver(this);
    }
}}, dataPath:{}}, EVENTS:{onGetBindingData:{}, onGetBindingDataType:{}}, timestamp:0, _disableBindingCounter:0, disableBinding:function () {
    this._disableBindingCounter++;
}, enableBinding:function () {
    if (this._disableBindingCounter > 0) {
        this._disableBindingCounter--;
    }
}, getBindingData:function (options) {
    if (!options) {
        options = {};
    }
    if (options.loadMode == null) {
        options.loadMode = "auto";
    }
    var data = null;
    if (this.getListenerCount("onGetBindingData") > 0) {
        var eventArg = {options:options, data:data};
        if (this.fireEvent("onGetBindingData", this, eventArg)) {
            data = eventArg.data;
        }
    } else {
        if (this._dataSet) {
            data = this._dataSet.queryData(this._dataPath, options);
        }
    }
    return data;
}, getBindingDataType:function (options) {
    if (!options) {
        options = {};
    }
    if (options.loadMode == null) {
        options.loadMode = "auto";
    }
    var dataType = null;
    if (this.getListenerCount("onGetBindingDataType") > 0) {
        var eventArg = {options:options};
        if (this.fireEvent("onGetBindingDataType", this, eventArg)) {
            dataType = eventArg.dataType;
        }
    } else {
        if (this._dataSet) {
            dataType = this._dataSet.getDataType(this._dataPath, options);
        }
    }
    return dataType;
}, dataSetMessageReceived:function (messageCode, arg) {
    if (this._disableBindingCounter == 0 && this.filterDataSetMessage(messageCode, arg)) {
        this.processDataSetMessage(messageCode, arg);
    }
}, filterDataSetMessage:function (messageCode, arg) {
    return true;
}, processDataSetMessage:dorado._NULL_FUNCTION});
dorado.widget.PropertyDataControl = $extend(dorado.widget.DataControl, {$className:"dorado.widget.PropertyDataControl", ATTRIBUTES:{dataPath:{defaultValue:"#"}, property:{}}, filterDataSetMessage:function (messageCode, arg, data) {
    var b = true;
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
        b = (arg.property == this._property);
        break;
      case dorado.widget.DataSet.MESSAGE_DELETED:
      case dorado.widget.DataSet.MESSAGE_INSERTED:
        b = false;
        break;
    }
    return b;
}, getBindingData:function (options) {
    var realOptions = {firstResultOnly:true, acceptAggregation:false};
    if (typeof options == "String") {
        realOptions.loadMode = options;
    } else {
        dorado.Object.apply(realOptions, options);
    }
    return $invokeSuper.call(this, [realOptions]);
}, getBindingPropertyDef:function () {
    var entityDataType = this.getBindingDataType(), p;
    if (entityDataType) {
        p = entityDataType.getPropertyDef(this._property);
    }
    return p;
}, getBindingPropertyValue:function () {
    var entity = this.getBindingData(true);
    return (entity) ? entity.get(this._property) : null;
}, getBindingPropertyText:function () {
    var entity = this.getBindingData(true);
    return (entity) ? entity.getText(this._property) : "";
}});
dorado.widget.layout = {};
dorado.widget.layout.Layout = $extend(dorado.AttributeSupport, {$className:"dorado.widget.layout.Layout", ATTRIBUTES:{className:{}, regionClassName:{}, padding:{}, style:{writeOnly:true}, container:{setter:function (a, container) {
    if (this._container != container) {
        this._domCache = {};
        this._container = container;
    }
}}, rendered:{readOnly:true}, attached:{readOnly:true}}, constructor:function (config) {
    this._regions = new dorado.util.KeyedList(function (region) {
        return region.control._id;
    });
    $invokeSuper.call(this, arguments);
    if (config) {
        this.set(config);
    }
}, getDom:function () {
    if (!this._dom) {
        this._dom = this.createDom();
    }
    return this._dom;
}, getRegionDom:function (region) {
    if (region) {
        return this._domCache[region.id];
    }
}, refresh:function () {
    if (this._attached) {
        this.refreshDom(this.getDom());
    }
}, onAttachToDocument:function (containerElement) {
    if (!this._attached) {
        this._attached = true;
        if (this._regions.size == 0) {
            return;
        }
        var dom = this.getDom();
        if (dom.parentNode != containerElement) {
            containerElement.appendChild(dom);
        }
        this.refreshDom(dom);
        this._rendered = true;
    }
}, onDetachFromDocument:function () {
    if (this._attached) {
        this._attached = false;
        this._regions.each(function (region) {
            region.control.onDetachFromDocument();
        });
    }
}, getPreviousRegion:function (region) {
    var entry = this._regions.findEntry(region);
    while (entry) {
        entry = entry.previous;
        if (entry && entry.data.constraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
            return entry.data;
        }
    }
}, getNextRegion:function (region) {
    var entry = this._regions.findEntry(region);
    while (entry) {
        entry = entry.next;
        if (entry && entry.data.constraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
            return entry.data;
        }
    }
}, preprocessLayoutConstraint:function (layoutConstraint) {
    return layoutConstraint || {};
}, addControl:function (control) {
    var region = {id:dorado.Core.newId(), control:control, constraint:this.preprocessLayoutConstraint(control._layoutConstraint)};
    this._regions.insert(region);
    if (this.onAddControl) {
        this.onAddControl(control);
    }
}, removeControl:function (control) {
    if (this.onRemoveControl) {
        this.onRemoveControl(control);
    }
    this._regions.removeKey(control._id);
}, disableRendering:function () {
    this._disableRendering = true;
}, enableRendering:function () {
    this._disableRendering = false;
}, resetControlDimension:function (region, container, autoWidth, autoHeight) {
    var control = region.control;
    if (autoWidth && (!control.ATTRIBUTES.width.independent || control._fixedWidth === false)) {
        var clientWidth = container._realWidth || ((dorado.Browser.msie && dorado.Browser.version < "7") ? container.offsetWidth : container.clientWidth);
        control._realWidth = clientWidth - region.padding * 2 + (region.autoWidthAdjust || 0);
    }
    if (autoHeight && (!control.ATTRIBUTES.height.independent || control._fixedHeight === false)) {
        var clientHeight = container._realHeight || ((dorado.Browser.msie && dorado.Browser.version < "7") ? container.offsetHeight : container.clientHeight);
        control._realHeight = clientHeight - region.padding * 2 + (region.autoHeightAdjust || 0);
    }
    if (control._attached) {
        control.refresh(true);
    }
}, renderControl:function (region, container, autoWidth, autoHeight) {
    region.autoWidth = autoWidth;
    region.autoHeight = autoHeight;
    region.padding = parseInt(region.constraint.padding) || 0;
    var control = region.control;
    if (region.padding) {
        var style = control.get("style");
        if (!style) {
            style = {};
            control.set("style", style);
        }
        style.position = "relative";
        style.left = style.top = region.padding + "px";
    }
    if (!container.style.width && autoWidth && !control.ATTRIBUTES.width.independent && dorado.Browser.msie && dorado.Browser.version < "7") {
        container.style.width = "0px";
    }
    this.resetControlDimension.apply(this, arguments);
    if (control._rendered && control.getDom().parentNode == container) {
        return;
    }
    this._ignoreControlSizeChange = true;
    control.render(container);
    this._ignoreControlSizeChange = false;
}, getRegion:function (control) {
    return this._regions.get((control instanceof dorado.widget.Control) ? control._id : control);
}, refreshControl:function (control) {
    var region = this.getRegion(control);
    if (region) {
        region.constraint = this.preprocessLayoutConstraint(control._layoutConstraint);
        this._ignoreControlSizeChange = true;
        if (this.doRefreshRegion) {
            this.doRefreshRegion(region);
        }
        this._ignoreControlSizeChange = false;
    }
}, onResize:function () {
    if (this._ignoreControlSizeChange || !this.doOnResize) {
        return;
    }
    var container = this.getDom();
    if (container.offsetWidth == 0 || container.offsetHeight == 0) {
        return;
    }
    this.doOnResize();
}, onControlSizeChange:function (control) {
    if (this._ignoreControlSizeChange || !this.doOnControlSizeChange) {
        return;
    }
    this.doOnControlSizeChange(control);
}});
dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT = "none";
(function () {
    var IGNORE_PROPERTIES = ["left", "right", "width", "top", "bottom", "height", "anchorLeft", "anchorRight", "anchorTop", "anchorBottom", "padding"];
    dorado.widget.layout.AnchorLayout = $extend(dorado.widget.layout.Layout, {$className:"dorado.widget.layout.AnchorLayout", ATTRIBUTES:{className:{defaultValue:"d-anchor-layout"}, regionClassName:{defaultValue:"d-anchor-layout-region"}}, createDom:function () {
        var dom = document.createElement("DIV");
        dom.className = this._className;
        return dom;
    }, refreshDom:function (dom) {
        for (var it = this._regions.iterator(); it.hasNext(); ) {
            var region = it.next();
            var constraint = region.constraint;
            if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
                continue;
            }
            var regionDom = this.createRegionContainer(region);
            var realignArg = this.adjustRegion(region);
            dom.appendChild(regionDom);
            this.renderControl(region, regionDom, true, true);
            if (realignArg) {
                this.realignRegion(region, realignArg);
            }
        }
    }, createRegionContainer:function (region) {
        var dom = this.getRegionDom(region);
        if (!dom) {
            dom = this._domCache[region.id] = document.createElement("DIV");
            if (this.style) {
                $fly(dom).css(this._style);
            }
            dom.className = this._regionClassName;
            if (region.constraint) {
                dom.style.position = (region.constraint.left != null || region.constraint.top != null || region.constraint.right != null || region.constraint.bottom != null) ? "absolute" : "relative";
                $fly(dom).addClass(region.constraint.className);
            }
        }
        if (region.constraint) {
            var css = dorado.Object.apply({}, region.constraint, function (p, v) {
                if (IGNORE_PROPERTIES.indexOf(p) >= 0) {
                    return false;
                }
            });
            $fly(dom).css(css);
        }
        return dom;
    }, onAddControl:function (control) {
        if (!this._attached || this._disableRendering) {
            return;
        }
        var region = this._regions.get(control._id);
        if (region) {
            var realignArg = this.adjustRegion(region, true);
            if (realignArg) {
                this.realignRegion(region, realignArg);
            }
        }
    }, onRemoveControl:function (control) {
        if (!this._attached || this._disableRendering) {
            return;
        }
        var region = this._regions.get(control._id);
        if (region) {
            var dom = this.getRegionDom(region);
            if (dom) {
                var jq = jQuery(dom);
                jq.remove();
                delete this._domCache[region.id];
            }
            var nextRegion = this.getNextRegion(region);
            if (nextRegion) {
                this.calculateRegions(nextRegion);
            }
        }
    }, doOnControlSizeChange:function (control) {
        this.refreshControl(control);
    }, doRefreshRegion:function (region) {
        var w, h, hidden = (region.constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT);
        var dom = this.getRegionDom(region);
        dom.style.display = (hidden ? "none" : "");
        if (!hidden) {
            w = region.currentWidth;
            h = region.currentHeight;
            var realignArg = this.adjustRegion(region, true);
            if (realignArg) {
                this.realignRegion(region, realignArg);
            }
        }
        if (hidden || w != region.currentWidth || h != region.currentHeight) {
            var nextRegion = this.getNextRegion(region);
            if (nextRegion) {
                this.calculateRegions(nextRegion);
            }
        }
    }, doOnResize:function () {
        this.calculateRegions();
    }, calculateRegions:function (fromRegion) {
        var regions = this._regions;
        if (regions.size == 0) {
            return;
        }
        var found = !fromRegion;
        regions.each(function (region) {
            if (!found) {
                found = (fromRegion == region);
                if (!found) {
                    return;
                }
            }
            var realignArg = this.adjustRegion(region);
            if (realignArg) {
                this.realignRegion(region, realignArg);
            }
        }, this);
    }, adjustRegion:function (region, skipResizeBubble) {
        function getAnchorRegion(region, p) {
            var anchor = constraint[p];
            if (anchor) {
                if (anchor.constructor == String) {
                    if (anchor == "previous") {
                        return this.getPreviousRegion(region);
                    }
                } else {
                    if (anchor instanceof Function) {
                        anchor = anchor.call(this, region);
                    }
                }
            }
            return anchor;
        }
        var regionDom = this.getRegionDom(region);
        if (!regionDom) {
            return;
        }
        var constraint = region.constraint, realignArg;
        var container = this.getDom();
        var left, right, width, top, bottom, height;
        left = right = width = top = bottom = height = -1;
        var lp, rp, tp, bp, wp, hp;
        lp = rp = tp = bp = wp = hp = 0;
        var padding = parseInt(this._padding) || 0;
        var realContainerWidth = container.clientWidth - padding * 2;
        var realContainerHeight = container.clientHeight - padding * 2;
        if (constraint.left != null && constraint.anchorLeft != "none") {
            var l = constraint.left;
            if (l.constructor == String && l.match("%")) {
                var rate = lp = parseInt(l);
                if (!isNaN(rate)) {
                    left = rate * realContainerWidth / 100 + padding;
                }
            }
            if (left < 0) {
                var anchorRegion = getAnchorRegion.call(this, region, "anchorLeft");
                var anchorDom = this.getRegionDom(anchorRegion);
                if (anchorDom) {
                    left = anchorDom.offsetLeft + anchorDom.clientWidth + parseInt(l);
                } else {
                    left = parseInt(l);
                    left += padding;
                }
            }
        }
        if (constraint.right != null && constraint.anchorRight != "none") {
            var r = constraint.right;
            if (r.constructor == String && r.match("%")) {
                var rate = rp = parseInt(r);
                if (!isNaN(rate)) {
                    right = rate * realContainerWidth / 100 + padding;
                }
            }
            if (right < 0) {
                var anchorRegion = getAnchorRegion.call(this, region, "anchorRight");
                var anchorDom = this.getRegionDom(anchorRegion);
                if (anchorDom) {
                    right = container.clientWidth - anchorDom.offsetLeft + parseInt(r);
                } else {
                    right = parseInt(r);
                    right += padding;
                }
            }
        }
        if (constraint.top != null && constraint.anchorTop != "none") {
            var t = constraint.top;
            if (t.constructor == String && t.match("%")) {
                var rate = tp = parseInt(t);
                if (!isNaN(rate)) {
                    top = rate * realContainerWidth / 100 + padding;
                }
            }
            if (top < 0) {
                var anchorRegion = getAnchorRegion.call(this, region, "anchorTop");
                var anchorDom = this.getRegionDom(anchorRegion);
                if (anchorDom) {
                    top = anchorDom.offsetTop + anchorDom.clientHeight + parseInt(t);
                } else {
                    top = parseInt(t);
                    top += padding;
                }
            }
        }
        if (constraint.bottom != null && constraint.anchorBottom != "none") {
            var b = constraint.bottom;
            if (b.constructor == String && b.match("%")) {
                var rate = bp = parseInt(b);
                if (!isNaN(rate)) {
                    bottom = rate * realContainerWidth / 100 + padding;
                }
            }
            if (bottom < 0) {
                var anchorRegion = getAnchorRegion.call(this, region, "anchorBottom");
                var anchorDom = this.getRegionDom(anchorRegion);
                if (anchorDom) {
                    bottom = container.clientHeight - anchorDom.offsetTop + parseInt(b);
                } else {
                    bottom = parseInt(b);
                    bottom += padding;
                }
            }
        }
        var constraintWidth = constraint.width;
        if (constraintWidth == null && region.control.getAttributeWatcher().getWritingTimes("width")) {
            constraintWidth = region.control._width;
        }
        if (constraintWidth != null) {
            var w = constraintWidth;
            if (w.constructor == String && w.match("%")) {
                var rate = wp = parseInt(w);
                if (!isNaN(rate)) {
                    width = rate * realContainerWidth / 100;
                }
            } else {
                width = parseInt(w);
            }
        } else {
            if (left >= 0 && right >= 0) {
                if (lp && rp) {
                    width = container.clientWidth - left - right;
                    right = -1;
                    lp = rp = false;
                } else {
                    width = container.clientWidth;
                    if (lp) {
                        left = -1;
                        width -= right;
                    }
                    if (rp) {
                        right = -1;
                        width -= left;
                    }
                    if (!lp && !rp) {
                        width -= (left + right);
                        right = -1;
                    }
                }
            }
        }
        var constraintHeight = constraint.height;
        if (constraintHeight == null && region.control.getAttributeWatcher().getWritingTimes("height")) {
            constraintHeight = region.control._height;
        }
        if (constraintHeight != null) {
            var h = constraintHeight;
            if (h.constructor == String && h.match("%")) {
                var rate = hp = parseInt(h);
                if (!isNaN(rate)) {
                    height = rate * realContainerHeight / 100;
                }
            } else {
                height = parseInt(h);
            }
        } else {
            if (top >= 0 && bottom >= 0) {
                if (tp && bp) {
                    height = container.clientHeight - top - bottom;
                    right = -1;
                    tp = bp = false;
                } else {
                    height = container.clientHeight;
                    if (tp) {
                        top = -1;
                        height -= bottom;
                    }
                    if (bp) {
                        bottom = -1;
                        height -= top;
                    }
                    if (!tp && !rp) {
                        height -= (top + bottom);
                        bottom = -1;
                    }
                }
            }
        }
        if (lp || rp || tp || bp || wp || hp) {
            realignArg = {left:lp, right:rp, top:tp, bottom:bp, width:wp, height:hp};
        }
        var style = regionDom.style;
        if (left >= 0 || right >= 0 || top >= 0 || bottom >= 0) {
            if (padding > 0) {
                if ((left >= 0 || right >= 0) && top < 0 && bottom < 0) {
                    top = padding;
                }
                if ((top >= 0 || bottom >= 0) && left < 0 && right < 0) {
                    left = padding;
                }
            }
            if (left >= 0) {
                style.left = left + "px";
            }
            if (right >= 0) {
                style.right = right + "px";
            }
            if (top >= 0) {
                style.top = top + "px";
            }
            if (bottom >= 0) {
                style.bottom = bottom + "px";
            }
        } else {
            if (padding > 0) {
                style.position = "relative";
                style.left = style.top = padding + "px";
            }
        }
        if (width >= 0) {
            style.width = width + "px";
        }
        if (height >= 0) {
            style.height = height + "px";
        }
        if (regionDom.parentNode) {
            if (!skipResizeBubble) {
                this.bubbleOnResize(region);
            }
            region.currentWidth = regionDom.offsetWidth;
            region.currentHeight = regionDom.offsetHeight;
        }
        return realignArg;
    }, realignRegion:function (region, realignArg) {
        var regionDom = this.getRegionDom(region);
        if (!regionDom) {
            return;
        }
        var left, right, width, top, bottom, height;
        left = right = width = top = bottom = height = -1;
        var constraint = region.constraint, container = this.getDom();
        if (realignArg.left) {
            left = Math.round((container.clientWidth - regionDom.offsetWidth - (parseInt(regionDom.style.right) || 0)) * realignArg.left / 100);
        } else {
            if (realignArg.right) {
                right = Math.round((container.clientWidth - regionDom.offsetWidth - (parseInt(regionDom.style.left) || 0)) * realignArg.right / 100);
            }
        }
        if (realignArg.top) {
            top = Math.round((container.clientHeight - regionDom.offsetHeight - (parseInt(regionDom.style.bottom) || 0)) * realignArg.top / 100);
        } else {
            if (realignArg.bottom) {
                bottom = Math.round((container.clientHeight - regionDom.offsetHeight - (parseInt(regionDom.style.top) || 0)) * realignArg.bottom / 100);
            }
        }
        var style = regionDom.style;
        if (left >= 0) {
            style.left = left + "px";
        }
        if (right >= 0) {
            style.right = right + "px";
        }
        if (top >= 0) {
            style.top = top + "px";
        }
        if (bottom >= 0) {
            style.bottom = bottom + "px";
        }
    }, bubbleOnResize:function (region) {
        var control = region.control;
        if (control._attached) {
            this.resetControlDimension(region, this.getRegionDom(region), region.autoWidth, region.autoHeight);
        }
    }});
    dorado.Toolkits.registerPrototype("layout", "Default", dorado.widget.layout.AnchorLayout);
})();
(function () {
    var defaultRegionPadding = 0;
    var getLastRegionFuncs = {};
    jQuery.each(["left", "right", "top", "bottom"], function (i, type) {
        getLastRegionFuncs[type] = function (region) {
            while (region.previousRegion) {
                region = region.previousRegion;
                if (region.constraint.type == type) {
                    return region;
                }
            }
            return null;
        };
    });
    var defaultConstraintsCache = {};
    function getDefaultConstraints(regionPadding) {
        if (regionPadding == null) {
            regionPadding = defaultRegionPadding;
        }
        var constraints = defaultConstraintsCache[regionPadding + ""];
        if (!constraints) {
            constraints = {top:{type:"top", left:regionPadding, right:regionPadding, top:regionPadding, bottom:undefined, anchorLeft:getLastRegionFuncs.left, anchorRight:getLastRegionFuncs.right, anchorTop:getLastRegionFuncs.top}, bottom:{type:"bottom", left:regionPadding, right:regionPadding, top:undefined, bottom:regionPadding, anchorLeft:getLastRegionFuncs.left, anchorRight:getLastRegionFuncs.right, anchorBottom:getLastRegionFuncs.bottom}, left:{type:"left", left:regionPadding, right:undefined, top:regionPadding, bottom:regionPadding, anchorLeft:getLastRegionFuncs.left, anchorTop:getLastRegionFuncs.top, anchorBottom:getLastRegionFuncs.bottom}, right:{type:"right", left:undefined, right:regionPadding, top:regionPadding, bottom:regionPadding, anchorRight:getLastRegionFuncs.right, anchorTop:getLastRegionFuncs.top, anchorBottom:getLastRegionFuncs.bottom}, center:{type:"center", left:regionPadding, right:regionPadding, top:regionPadding, bottom:regionPadding, anchorLeft:getLastRegionFuncs.left, anchorRight:getLastRegionFuncs.right, anchorTop:getLastRegionFuncs.top, anchorBottom:getLastRegionFuncs.bottom, overFlow:"auto"}};
            defaultConstraintsCache[regionPadding + ""] = constraints;
        }
        return constraints;
    }
    function getDefaultConstraint(type, regionPadding) {
        var constraints = getDefaultConstraints(regionPadding);
        return constraints[type || "center"];
    }
    dorado.widget.layout.DockLayout = $extend(dorado.widget.layout.AnchorLayout, {$className:"dorado.widget.layout.DockLayout", ATTRIBUTES:{className:{defaultValue:"d-dock-layout"}, regionClassName:{defaultValue:"d-dock-layout-region"}, regionPadding:{defaultValue:defaultRegionPadding}}, preprocessLayoutConstraint:function (layoutConstraint) {
        if (layoutConstraint) {
            if (layoutConstraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
                if (layoutConstraint.constructor == String) {
                    layoutConstraint = getDefaultConstraint(layoutConstraint, this._regionPadding);
                } else {
                    dorado.Object.apply(layoutConstraint, getDefaultConstraint(layoutConstraint.type, this._regionPadding));
                }
            }
        } else {
            layoutConstraint = dorado.Object.apply({}, getDefaultConstraint(null, this._regionPadding));
        }
        return layoutConstraint;
    }, addControl:function (control) {
        var layoutConstraint = this.preprocessLayoutConstraint(control._layoutConstraint);
        var region = {id:dorado.Core.newId(), control:control, constraint:layoutConstraint};
        var regions = this._regions;
        var lastRegion = (regions.size > 0) ? regions.last.data : null;
        if (lastRegion && lastRegion.constraint.type == "center") {
            if (layoutConstraint.type == "center") {
                lastRegion.constraint = getDefaultConstraint("top", this._regionPadding);
                if (this._rendered) {
                    this.refreshControl(control);
                }
                region.previousRegion = lastRegion;
                regions.insert(region);
            } else {
                region.previousRegion = (regions.last.previous) ? regions.last.previous.data : null;
                lastRegion.previousRegion = region;
                regions.insert(region, "before", lastRegion);
            }
        } else {
            region.previousRegion = lastRegion;
            regions.insert(region);
        }
        if (this.onAddControl) {
            this.onAddControl(control);
        }
    }, renderControl:function (region, container, autoWidth, autoHeight) {
        switch (region.constraint.type) {
          case "top":
          case "bottom":
            autoWidth = true;
            break;
          case "left":
          case "right":
            autoHeight = true;
            break;
          default:
            autoWidth = true;
            autoHeight = true;
        }
        return $invokeSuper.call(this, [region, container, autoWidth, autoHeight]);
    }});
})();
(function () {
    var IGNORE_PROPERTIES = ["colSpan", "rowSpan", "align", "vAlign"];
    dorado.widget.layout.FormLayout = $extend(dorado.widget.layout.Layout, {$className:"dorado.widget.layout.FormLayout", ATTRIBUTES:{className:{defaultValue:"d-form-layout"}, regionClassName:{defaultValue:"d-form-layout-region"}, cols:{defaultValue:"*,*"}, rowHeight:{defaultValue:22}, colPadding:{defaultValue:16}, rowPadding:{defaultValue:6}, padding:{defaultValue:8}, stretchWidth:{}}, createDom:function () {
        var dom = document.createElement("TABLE");
        dom.cellSpacing = dom.cellPadding = 0;
        dom.className = this._className;
        var $dom = $fly(dom);
        if (this.style) {
            $dom.css(this._style);
        }
        $dom.xCreate([{tagName:"TBODY"}]);
        return dom;
    }, refreshDom:function (dom) {
        if (this.precalculateRegions) {
            this.precalculateRegions();
        }
        var tbody = dom.tBodies[0];
        dom.removeChild(tbody);
        while (tbody.firstChild) {
            tbody.removeChild(tbody.firstChild);
        }
        dom.appendChild(tbody);
        this.resizeTableAndCols();
        var grid = this._grid, index = -1, realColWidths = this._realColWidths;
        for (var row = 0; row < grid.length; row++) {
            var tr = document.createElement("TR");
            tr.style.height = this._rowHeight + "px";
            tbody.appendChild(tr);
            var cols = grid[row], cellForRenders = [];
            for (var col = 0; col < cols.length; col++) {
                var region = grid[row][col];
                if (region && region.regionIndex <= index) {
                    continue;
                }
                var td = this.createRegionContainer(region);
                var cls = this._colClss[col];
                if (cls) {
                    $fly(td).addClass(cls);
                }
                tr.appendChild(td);
                if (region) {
                    $fly(td).css(region.constraint);
                    region.autoWidthAdjust = region.autoHeightAdjust = 0;
                    if ((col + region.colSpan) < cols.length) {
                        region.autoWidthAdjust = 0 - this._colPadding;
                    }
                    if ((row + region.rowSpan) < grid.length) {
                        region.autoHeightAdjust = 0 - this._rowPadding;
                    }
                    td.colSpan = region.colSpan;
                    td.rowSpan = region.rowSpan;
                    var w = 0;
                    if (region.colSpan > 1) {
                        var endIndex = region.colIndex + region.colSpan;
                        for (var j = region.colIndex; j < endIndex; j++) {
                            w += realColWidths[j];
                        }
                    } else {
                        w = realColWidths[region.colIndex];
                    }
                    td._realWidth = w;
                    td.style.width = w + "px";
                    td.style.paddingBottom = (-region.autoHeightAdjust || 0) + "px";
                    cellForRenders.push({cell:td, region:region});
                }
            }
            for (var i = 0; i < cellForRenders.length; i++) {
                var cellInfo = cellForRenders[i], td = cellInfo.cell, region = cellInfo.region;
                index = region.regionIndex;
                this.renderControl(region, td, true, false);
            }
        }
        this._regions.each(function (region) {
            if (region.constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
                return;
            }
            if (region.control.onAttachToDocument) {
                region.control.onAttachToDocument();
            }
        });
    }, createRegionContainer:function (region) {
        var dom = this.getRegionDom(region);
        if (!dom) {
            dom = document.createElement("TD");
            if (region) {
                this._domCache[region.id] = dom;
            }
        }
        dom.className = this._regionClassName;
        if (region) {
            var $dom = $fly(dom), constraint = region.constraint;
            if (constraint.className) {
                $dom.addClass(constraint.className);
            }
            if (constraint.align) {
                dom.align = constraint.align;
            }
            if (constraint.vAlign) {
                dom.vAlign = constraint.vAlign;
            }
            var css = dorado.Object.apply({}, constraint, function (p, v) {
                if (IGNORE_PROPERTIES.indexOf(p) >= 0) {
                    return false;
                }
            });
            $dom.css(css);
        }
        return dom;
    }, initColInfos:function () {
        this._cols = this._cols || "*";
        var colWidths = this._colWidths = [];
        var colClss = this._colClss = [];
        var dynaColCount = 0, fixedWidth = 0;
        jQuery.each(this._cols.split(","), function (i, col) {
            var w, cls, ind = col.indexOf("[");
            if (ind > 0) {
                w = col.substring(0, ind);
                cls = col.substring(ind + 1, col.length - 1);
            } else {
                w = col;
            }
            colClss[i] = cls;
            if (w == "*") {
                colWidths.push(-1);
                dynaColCount++;
            } else {
                w = parseInt(w);
                colWidths.push(w);
                fixedWidth += (w || 0);
            }
        });
        this.colCount = colWidths.length;
        this.dynaColCount = dynaColCount;
        this.fixedWidth = fixedWidth;
    }, precalculateRegions:function () {
        function precalculateRegion(region) {
            function doTestRegion() {
                for (var row = rowIndex; row < rowIndex + rowSpan && row < grid.length; row++) {
                    for (var col = colIndex; col < colIndex + colSpan; col++) {
                        if (grid[row][col]) {
                            return false;
                        }
                    }
                }
                return true;
            }
            var previousRegion = this.getPreviousRegion(region);
            var pRegionIndex = -1, pRowIndex = 0, pColIndex = -1, pRowSpan = pColSpan = 1;
            if (previousRegion) {
                pRegionIndex = previousRegion.regionIndex;
                pRowIndex = previousRegion.rowIndex;
                pColIndex = previousRegion.colIndex;
                pRowSpan = previousRegion.rowSpan;
                pColSpan = previousRegion.colSpan;
            }
            var constraint = region.constraint;
            var rowIndex = pRowIndex, colIndex = pColIndex;
            var colSpan = ((constraint.colSpan > this.colCount) ? this.colCount : constraint.colSpan) || 1;
            var rowSpan = constraint.rowSpan || 1;
            do {
                colIndex++;
                if (colIndex + colSpan > this.colCount) {
                    rowIndex++;
                    colIndex = 0;
                }
            } while (!doTestRegion());
            for (var row = 0; row < rowSpan; row++) {
                if ((rowIndex + row) >= grid.length) {
                    grid.push(new Array(this.colCount));
                }
                for (var col = 0; col < colSpan; col++) {
                    grid[rowIndex + row][colIndex + col] = region;
                }
            }
            region.regionIndex = pRegionIndex + 1;
            region.colIndex = colIndex;
            region.rowIndex = rowIndex;
            region.colSpan = colSpan;
            region.rowSpan = rowSpan;
        }
        this.initColInfos();
        var grid = this._grid = [];
        var regions = this._regions;
        for (var it = regions.iterator(); it.hasNext(); ) {
            var region = it.next(), constraint = region.constraint;
            if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
                region.regionIndex = -1;
                continue;
            }
            constraint.colSpan = parseInt(constraint.colSpan);
            constraint.rowSpan = parseInt(constraint.rowSpan);
            precalculateRegion.call(this, region);
        }
    }, resizeTableAndCols:function () {
        var realColWidths = this._realColWidths;
        if (!realColWidths) {
            this._realColWidths = realColWidths = [];
        }
        var table = this.getDom(), padding = parseInt(this._padding) || 0, colPadding = this._colPadding || 0;
        var containerWidth = (table.parentNode) ? (jQuery(table.parentNode).width() - padding * 2) : 0;
        if (containerWidth < 0) {
            containerWidth = 0;
        }
        var adjust = 0;
        if (dorado.Browser.safari || dorado.Browser.chrome) {
            adjust = padding * 2;
        }
        if (this._stretchWidth || this.dynaColCount > 0) {
            table.style.width = (containerWidth + adjust) + "px";
        }
        table.style.padding = padding + "px";
        containerWidth -= colPadding * (this._colWidths.length - 1);
        var self = this, changedCols = [];
        for (var i = 0; i < this._colWidths.length; i++) {
            var w = this._colWidths[i];
            if (self.dynaColCount > 0) {
                if (w == -1) {
                    w = parseInt((containerWidth - self.fixedWidth) / self.dynaColCount);
                }
                w = (w < 0) ? 0 : w;
            } else {
                if (self._stretchWidth) {
                    w = parseInt(w * containerWidth / self.fixedWidth);
                }
            }
            if (i < this._colWidths.length - 1) {
                w += colPadding;
            }
            if (realColWidths[i] != w) {
                changedCols.push(i);
            }
            realColWidths[i] = w;
        }
        return changedCols;
    }, doOnResize:function () {
        if (this._disableRendering) {
            return;
        }
        this.initColInfos();
        var changedCols = this.resizeTableAndCols();
        if (changedCols.length == 0) {
            return;
        }
        if (this._regions.size == 0) {
            return;
        }
        var grid = this._grid, realColWidths = this._realColWidths;
        for (var row = 0; row < grid.length; row++) {
            var cols = grid[row];
            for (var col = 0; col < cols.length; col++) {
                var region = grid[row][col];
                if (region) {
                    var w = 0;
                    if (region.colSpan > 1) {
                        var endIndex = region.colIndex + region.colSpan;
                        for (var j = region.colIndex; j < endIndex; j++) {
                            w += realColWidths[j];
                        }
                    } else {
                        w = realColWidths[region.colIndex];
                    }
                    var td = this.getRegionDom(region);
                    td._realWidth = w;
                    td.style.width = w + "px";
                    var control = region.control;
                    if (control._attached) {
                        this.resetControlDimension(region, td, true, false);
                        control.resetDimension();
                    }
                }
            }
        }
    }, resetControlDimension:function (region, container, autoWidth, autoHeight) {
        var control = region.control;
        if (autoWidth) {
            autoWidth = (!control.ATTRIBUTES.width.independent && !control.getAttributeWatcher().getWritingTimes("width"));
        }
        if (autoHeight) {
            autoHeight = (!control.ATTRIBUTES.height.independent && !control.getAttributeWatcher().getWritingTimes("height"));
        }
        $invokeSuper.call(this, [region, container, autoWidth, autoHeight]);
    }, doRefreshRegion:function () {
        this.refresh();
    }});
    var p = dorado.widget.layout.FormLayout.prototype;
    p.onAddControl = p.onRemoveControl = p.doRefreshRegion = function () {
        if (!this._attached || this._disableRendering) {
            return;
        }
        this.refreshDom(this.getDom());
    };
})();

