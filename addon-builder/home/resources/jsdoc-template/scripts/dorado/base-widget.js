
(function () {
    dorado.widget.Action = $extend(dorado.widget.Component, {$className:"dorado.widget.Action", ATTRIBUTES:{caption:{}, icon:{}, iconClass:{}, tip:{}, disabled:{}, parameter:{}, returnValue:{readOnly:true}, async:{}, hotkey:{writeBeforeReady:true, setter:function (p, hotkey) {
        this._hotkey = hotkey;
        var self = this;
        if (hotkey) {
            jQuery(document).bind("keydown", hotkey, function () {
                self.execute();
            });
        }
    }}, confirmMessage:{}, executingMessage:{}, successMessage:{}}, EVENTS:{beforeExecute:{}, onExecute:{}, onSuccess:{}, onFailure:{}}, constructor:function () {
        this._bindingObjects = new dorado.ObjectGroup();
        $invokeSuper.call(this, arguments);
        this.addListener("onAttributeChange", function (self, arg) {
            dorado.Toolkits.setDelayedAction(self, "$actionStateChangeTimerId", function () {
                var attr = arg.attribute, value = arg.value;
                if ((attr == "icon") || (attr == "iconClass")) {
                    self._bindingObjects.set(attr, value, {skipUnknownAttribute:true});
                } else {
                    self._bindingObjects.invoke("onActionStateChange");
                }
            }, 20);
        });
    }, doExecuteSync:dorado._NULL_FUNCTION, doExecuteAsync:dorado._NULL_FUNCTION, execute:function (callback) {
        var self = this, retval = true;
        function realCall(callback) {
            var eventArg = {processDefault:true};
            if (self._async) {
                var taskId;
                if (self._executingMessage) {
                    taskId = dorado.util.TaskIndicator.showTaskIndicator(self._executingMessage);
                }
                self.doExecuteAsync({callback:function (success, result) {
                    if (taskId) {
                        dorado.util.TaskIndicator.hideTaskIndicator(taskId);
                    }
                    self._returnValue = result;
                    $callback(callback, success, result, {scope:self._view});
                    eventArg.success = success;
                    eventArg[success ? "result" : "error"] = result;
                    self.fireEvent("onExecute", eventArg);
                    self.fireEvent((success) ? "onSuccess" : "onFailure", self, eventArg);
                    if (success && eventArg.processDefault && self._successMessage) {
                        dorado.MessageBox.alert(self._successMessage);
                    }
                }});
            } else {
                var success = false, result;
                try {
                    self._returnValue = result = self.doExecuteSync();
                    success = true;
                    $callback(callback, true, result, {scope:self._view});
                }
                catch (e) {
                    self._returnValue = result = e;
                }
                eventArg.success = success;
                eventArg[success ? "result" : "error"] = result;
                self.fireEvent("onExecute", self, eventArg);
                self.fireEvent((success) ? "onSuccess" : "onFailure", self, eventArg);
                if (success && eventArg.processDefault && self._successMessage) {
                    dorado.MessageBox.alert(self._successMessage);
                }
                return result;
            }
        }
        var eventArg = {processDefault:true};
        self.fireEvent("beforeExecute", self, eventArg);
        if (eventArg.processDefault) {
            if (this._confirmMessage) {
                var self = this;
                dorado.MessageBox.confirm(this._confirmMessage, function () {
                    realCall.call(self, callback);
                });
            } else {
                retval = realCall.call(this, callback);
            }
        }
        return retval;
    }});
    var listenedAttrs = ["caption", "icon", "iconClass", "tip", "disabled"];
    dorado.widget.ActionSupport = $class({$className:"dorado.widget.ActionSupport", ATTRIBUTES:{action:{componentReference:true, setter:function (p, action) {
        if (this._action instanceof dorado.widget.Action) {
            this._action._bindingObjects.objects.remove(this);
        }
        if (action && !(action instanceof dorado.widget.Action)) {
            var ref = action;
            action = ref.view.id(ref.component);
        }
        this._action = action;
        if (action) {
            action._bindingObjects.objects.push(this);
        }
    }}}, onActionStateChange:function () {
        if (this.refresh) {
            this.refresh(true);
        }
    }, destroy:function () {
        if (this._destroyed) {
            return;
        }
        this.set("action", null);
        $invokeSuper.call(this, arguments);
    }});
})();
(function () {
    var VALIDATION_RESULT_CODE = {ok:0, invalid:1, executing:2};
    dorado.widget.AjaxAction = $extend(dorado.widget.Action, {$className:"dorado.widget.AjaxAction", ATTRIBUTES:{async:{defaultValue:true}, service:{}, supportsEntity:{defaultValue:true}}, getAjaxOptions:function () {
        var jsonData = {action:"remote-service", service:this._service, parameter:dorado.JSON.evaluate(this._parameter), context:(this._view ? this._view.get("context") : null)};
        if (this._supportsEntity) {
            jsonData.loadedDataTypes = this.get("dataTypeRepository").getLoadedDataTypes();
        }
        return dorado.Object.apply({jsonData:jsonData}, $setting["ajax.remoteServiceOptions"]);
    }, doExecuteSync:function () {
        var ajaxOptions = this.getAjaxOptions(), ajax = dorado.Toolkits.getAjax(ajaxOptions);
        var result = ajax.requestSync(ajaxOptions);
        if (result.success) {
            var result = result.getJsonData(), dataTypeRepository = this.get("dataTypeRepository");
            if (result && (result.$dataTypeDefinitions || result.$context)) {
                result = result.data;
                if (result.$dataTypeDefinitions) {
                    dataTypeRepository.parseJsonData(result.$dataTypeDefinitions);
                }
                if (result.$context && this._view) {
                    this._view.set("context", json.$context);
                }
            }
            if (result && this._supportsEntity) {
                result = dorado.DataUtil.convertIfNecessary(result, dataTypeRepository);
            }
            return result;
        } else {
            throw result.error;
        }
    }, doExecuteAsync:function (callback) {
        var ajaxOptions = this.getAjaxOptions(), ajax = dorado.Toolkits.getAjax(ajaxOptions);
        ajax.request(ajaxOptions, {scope:this, callback:function (success, result) {
            if (success) {
                result = result.getJsonData(), dataTypeRepository = this.get("dataTypeRepository");
                if (result && (result.$dataTypeDefinitions || result.$context)) {
                    result = result.data;
                    if (result.$dataTypeDefinitions) {
                        dataTypeRepository.parseJsonData(result.$dataTypeDefinitions);
                    }
                    if (result.$context && this._view) {
                        this._view.set("context", json.$context);
                    }
                }
                if (result && this._supportsEntity) {
                    result = dorado.DataUtil.convertIfNecessary(result, dataTypeRepository);
                }
                $callback(callback, true, result);
            } else {
                $callback(callback, false, result.error);
            }
        }});
    }});
    dorado.DataPath.registerInterceptor("CASCADE_DIRTY", function (data) {
        function isDirty(entity) {
            var dirty = (entity.state != dorado.Entity.STATE_NONE);
            if (!dirty) {
                var data = entity._data;
                for (var p in data) {
                    var v = data[p];
                    if (v instanceof dorado.Entity) {
                        dirty = isDirty(v);
                    } else {
                        if (v instanceof dorado.EntityList) {
                            var it = v.iterator(true);
                            while (it.hasNext()) {
                                dirty = isDirty(it.next());
                                if (dirty) {
                                    break;
                                }
                            }
                        }
                    }
                    if (dirty) {
                        break;
                    }
                }
            }
            return dirty;
        }
        if (data instanceof dorado.Entity) {
            if (!isDirty(data)) {
                data = null;
            }
        } else {
            if (data instanceof dorado.EntityList) {
                var it = data.iterator(true);
                var data = [];
                while (it.hasNext()) {
                    var e = it.next();
                    if (isDirty(e)) {
                        data.push(e);
                    }
                }
            } else {
                data = null;
            }
        }
        return data;
    }, function (dataType) {
        return dataType;
    });
    var CASCADE_NOT_DRITY_ENTITYS;
    dorado.DataPath.registerInterceptor("DIRTY_TREE", function (data) {
        function gothrough(entity) {
            var isDirty = entity.isDirty();
            var data = entity._data;
            for (var property in data) {
                if (!data.hasOwnProperty(property)) {
                    continue;
                }
                if (property.charAt(0) == "$") {
                    continue;
                }
                var propertyDef = (entity._propertyDefs) ? entity._propertyDefs.get(property) : null;
                if (!propertyDef || !propertyDef._submitable) {
                    continue;
                }
                var value = entity.get(property, "never");
                if (value instanceof dorado.EntityList) {
                    var it = value.iterator(true);
                    while (it.hasNext()) {
                        if (gothrough(it.next())) {
                            isDirty = true;
                        }
                    }
                }
            }
            if (!isDirty) {
                CASCADE_NOT_DRITY_ENTITYS[entity.entityId] = true;
            }
            return isDirty;
        }
        CASCADE_NOT_DRITY_ENTITYS = {};
        if (data instanceof dorado.Entity) {
            if (!gothrough(data)) {
                data = null;
            }
        } else {
            if (data instanceof dorado.EntityList) {
                var it = data.iterator(true);
                data = [];
                while (it.hasNext()) {
                    var entity = it.next();
                    if (gothrough(entity)) {
                        data.push(entity);
                    }
                }
            }
        }
        return data;
    }, function (dataType) {
        return dataType;
    });
    function filterCascadeDrityEntity(entity) {
        return !CASCADE_NOT_DRITY_ENTITYS[entity.entityId];
    }
    dorado.widget.UpdateAction = $extend(dorado.widget.Action, {$className:"dorado.widget.UpdateAction", ATTRIBUTES:{async:{defaultValue:true}, dataResolver:{setter:function (a, v) {
        this._dataResolver = (v && v.constructor === String) ? dorado.DataResolver.create(v) : v;
    }}, updateItems:{getter:function () {
        var updateItems = this._updateItems;
        if (updateItems) {
            var self = this;
            jQuery.each(updateItems, function (i, updateItem) {
                if (updateItem.autoResetEntityState == null) {
                    updateItem.autoResetEntityState = true;
                }
                if (updateItem.dataSet == null) {
                    return;
                }
                if (typeof updateItem.dataSet == "string") {
                    updateItem.dataSet = self.get("view").id(updateItem.dataSet);
                } else {
                    if (!(updateItem.dataSet instanceof dorado.widget.DataSet)) {
                        var ref = updateItem.dataSet;
                        updateItem.dataSet = ref.view.id(ref.component);
                    }
                }
            });
        }
        return updateItems;
    }}, alwaysExecute:{}}, EVENTS:{beforeExecute:{interceptor:function (superFire, self, arg) {
        var retval = superFire(self, arg);
        this._realConfirmMessage = this._confirmMessage;
        this._confirmMessage = null;
        return retval;
    }}, beforeUpdate:{}, onUpdate:{}}, constructor:function (id) {
        this._updateItems = [];
        $invokeSuper.call(this, arguments);
    }, getResolveContext:function () {
        function mergeValidateContext(context, contextForMerge) {
            if (!context) {
                return contextForMerge;
            }
            if (VALIDATION_RESULT_CODE[contextForMerge.result] > VALIDATION_RESULT_CODE[context.result]) {
                context.result = contextForMerge.result;
            }
            context.info = context.info.concat(contextForMerge.info);
            context.ok = context.info.concat(contextForMerge.ok);
            context.warn = context.info.concat(contextForMerge.warn);
            context.error = context.info.concat(contextForMerge.error);
            context.executing = context.info.concat(contextForMerge.executing);
            context.executingValidationNum += contextForMerge.executingValidationNum;
            return context;
        }
        var dataItems = [], updateInfos = [], aliasMap = {}, hasUpdateData = false, updateItems = this.get("updateItems");
        for (var i = 0; i < updateItems.length; i++) {
            var updateItem = updateItems[i];
            var dataSet = updateItem.dataSet;
            var options = updateItem.options;
            if (dataSet == null) {
                continue;
            }
            if (options instanceof Object) {
                if (options.loadMode !== false) {
                    options.loadMode = true;
                }
                if (options.includeUnsubmitableProperties !== true) {
                    options.includeUnsubmitableProperties = false;
                }
                if (options.generateDataType !== false) {
                    options.generateDataType = true;
                }
                if (options.generateState !== false) {
                    options.generateState = true;
                }
                if (options.generateEntityId !== false) {
                    options.generateEntityId = true;
                }
                if (options.generateOldData !== false) {
                    options.generateOldData = true;
                }
            } else {
                options = {loadMode:"never", includeUnsubmitableProperties:false, generateDataType:true, generateState:true, generateEntityId:true, generateOldData:true};
            }
            if (updateItem.firstResultOnly != null) {
                options.firstResultOnly = updateItem.firstResultOnly;
            }
            var alias = updateItem.alias || dataSet._id;
            aliasMap[alias] = dataSet;
            var dataPath = updateItem.dataPath || "!DIRTY_TREE";
            if (dataPath.indexOf("!DIRTY_TREE") >= 0) {
                options.entityFilter = filterCascadeDrityEntity;
                options.includeDeletedEntity = true;
                CASCADE_NOT_DRITY_ENTITYS = null;
            } else {
                if (updateItem.submitSimplePropertyOnly) {
                    options.entityFilter = filterCascadeDrityEntity;
                    CASCADE_NOT_DRITY_ENTITYS = {};
                }
            }
            var entityFilter = options.entityFilter;
            var data = dataSet.queryData(dataPath, options);
            if (data) {
                var validateContext, validateOptions = {validateSimplePropertyOnly:updateItem.submitSimplePropertyOnly};
                if (data instanceof Array) {
                    for (var j = 0; j < data.length; j++) {
                        var entity = data[j];
                        if (entity instanceof dorado.Entity) {
                            validateOptions.context = {};
                            entity.validate(validateOptions);
                            validateContext = mergeValidateContext(validateContext, validateOptions.context);
                        }
                    }
                } else {
                    if (data instanceof dorado.EntityList) {
                        for (var it = data.iterator(); it.hasNext(); ) {
                            var entity = it.next();
                            validateOptions.context = {};
                            entity.validate(validateOptions);
                            validateContext = mergeValidateContext(validateContext, validateOptions.context);
                        }
                    } else {
                        if (data instanceof dorado.Entity) {
                            validateOptions.context = {};
                            data.validate(validateOptions);
                            validateContext = mergeValidateContext(validateContext, validateOptions.context);
                        }
                    }
                }
            }
            dataItems.push({updateItem:updateItem, alias:alias, data:data, refreshMode:updateItem.refreshMode, autoResetEntityState:updateItem.autoResetEntityState});
        }
        if (validateContext) {
            if (validateContext.result == "invalid") {
                var errorMessage = $resource("dorado.baseWidget.SubmitInvalidData") + "\n";
                if (validateContext.error.length + validateContext.warn.length == 1) {
                    if (validateContext.error.length) {
                        errorMessage += validateContext.error[0].text;
                    } else {
                        errorMessage += validateContext.warn[0].text;
                    }
                } else {
                    errorMessage += $resource("dorado.baseWidget.SubmitValidationSummary", validateContext.error.length, validateContext.warn.length);
                }
                throw new dorado.Exception(errorMessage);
            } else {
                if (validateContext.result == "executing") {
                    throw new dorado.ResourceException("dorado.baseWidget.SubmitValidatingData", validateContext.executing.length);
                }
            }
        }
        for (var i = 0; i < dataItems.length; i++) {
            var dataItem = dataItems[i], updateItem = dataItem.updateItem, data = dataItem.data;
            delete dataItem.updateItem;
            var entities = [], context = {entities:[]};
            if (data) {
                if (data instanceof Array) {
                    var v = data, data = [];
                    hasUpdateData = hasUpdateData || (v.length > 0);
                    for (var j = 0; j < v.length; j++) {
                        var generateDataType = options.generateDataType, lastDataType;
                        var entity = v[j];
                        if (entity instanceof dorado.Entity) {
                            if (generateDataType && lastDataType != entity.dataType) {
                                lastDataType = entity.dataType;
                                options.generateDataType = true;
                            } else {
                                options.generateDataType = false;
                            }
                            if (updateItem.submitSimplePropertyOnly) {
                                CASCADE_NOT_DRITY_ENTITYS[entity.entityId] = true;
                            }
                            entities.push(entity);
                            data.push(entity.toJSON(options, context));
                        }
                    }
                } else {
                    if (data instanceof dorado.EntityList || data instanceof dorado.Entity) {
                        hasUpdateData = true;
                        if (updateItem.refreshMode == "cascade" || updateItem.submitSimplePropertyOnly) {
                            if (data instanceof dorado.Entity) {
                                if (updateItem.submitSimplePropertyOnly) {
                                    CASCADE_NOT_DRITY_ENTITYS[data.entityId] = true;
                                }
                                if (updateItem.refreshMode == "cascade") {
                                    entities.push(data);
                                }
                            } else {
                                for (var it = data.iterator(); it.hasNext(); ) {
                                    var entity = it.next();
                                    if (updateItem.submitSimplePropertyOnly) {
                                        CASCADE_NOT_DRITY_ENTITYS[entity.entityId] = true;
                                    }
                                    if (updateItem.refreshMode == "cascade") {
                                        entities.push(entity);
                                    }
                                }
                            }
                        }
                        data = data.toJSON(options, context);
                    }
                }
                dataItem.data = data;
            }
            updateInfos.push({alias:dataItem.alias, refreshMode:updateItem.refreshMode, entities:((updateItem.refreshMode == "cascade") ? entities : context.entities)});
        }
        return {aliasMap:aliasMap, updateInfos:updateInfos, dataResolverArg:{dataItems:dataItems, parameter:this._parameter, view:this._view}, hasUpdateData:hasUpdateData};
    }, doExecuteSync:function () {
        return this.doExecuteAsync();
    }, doExecuteAsync:function (callback) {
        function processEntityStates(entityStates, context) {
            function processEntity(entity, entityStates, refreshMode) {
                if (!entity.entityId) {
                    return;
                }
                var b;
                if (refreshMode != "cascade") {
                    var data = entity.getData();
                    for (var p in data) {
                        if (!data.hasOwnProperty(p)) {
                            continue;
                        }
                        var v = data[p];
                        if (v instanceof Object && v.entityId) {
                            b = processEntity(v, entityStates) || b;
                        }
                    }
                }
                var state = entityStates[entity.entityId] || 0;
                if (state.constructor == Number) {
                    if (entity.state == state) {
                        return b;
                    }
                    if (state == dorado.Entity.STATE_DELETED) {
                        entity.remove(true);
                    } else {
                        entity.setState(state);
                    }
                } else {
                    var s = state.$state || 0;
                    delete state.$state;
                    if (refreshMode == "cascade") {
                        entity.fromJSON(state);
                    } else {
                        for (var p in state) {
                            if (!state.hasOwnProperty(p)) {
                                continue;
                            }
                            entity.set(p, state[p]);
                        }
                    }
                    entity.setState(s);
                }
                return true;
            }
            function processUpdateInfo(updateInfo, entityStates) {
                if (updateInfo.refreshMode == "none") {
                    return false;
                }
                var b = false, entities = updateInfo.entities;
                if (updateInfo.refreshMode == "cascade") {
                    var map = {};
                    for (var i = 0; i < entities.length; i++) {
                        var entity = entities[i];
                        map[entity.entityId] = entity;
                    }
                    for (var entityId in entityStates) {
                        if (entityStates.hasOwnProperty(entityId)) {
                            var entity = map[entityId];
                            if (entity) {
                                b = processEntity(entity, entityStates, updateInfo.refreshMode) || b;
                            }
                        }
                    }
                } else {
                    for (var i = 0; i < entities.length; i++) {
                        var entity = entities[i];
                        b = processEntity(entity, entityStates, updateInfo.refreshMode) || b;
                    }
                }
                return b;
            }
            var updateInfos = context.updateInfos, changedDataSets = [];
            for (var i = 0; i < updateInfos.length; i++) {
                var alias = updateInfos[i].alias;
                var dataSet = context.aliasMap[alias];
                dataSet.disableObservers();
                try {
                    if (processUpdateInfo(updateInfos[i], entityStates)) {
                        changedDataSets.push(dataSet);
                    }
                }
                finally {
                    dataSet.enableObservers();
                }
            }
            jQuery.each(changedDataSets, function (i, dataSet) {
                dataSet.notifyObservers();
            });
        }
        function doUpdate(context, dataResolverArg) {
            var eventArg = {dataItems:dataResolverArg.dataItems, parameter:dataResolverArg.parameter, processDefault:true};
            this.fireEvent("beforeUpdate", this, eventArg);
            if (eventArg.processDefault && this._dataResolver) {
                var dataResolver = this._dataResolver;
                dataResolver.supportsEntity = this._supportsEntity;
                dataResolver.dataTypeRepository = this.get("dataTypeRepository");
                dataResolver.message = this._executingMessage ? "" : null;
                if (callback) {
                    return dataResolver.resolveAsync(dataResolverArg, {scope:this, callback:function (success, result) {
                        if (success) {
                            processEntityStates.call(this, result.entityStates, context);
                        }
                        $callback(callback, success, (success) ? result.returnValue : result);
                        this.fireEvent("onUpdate", this, eventArg);
                    }});
                } else {
                    var result = dataResolver.resolve(dataResolverArg);
                    processEntityStates.call(this, result.entityStates, context);
                    this.fireEvent("onUpdate", this, eventArg);
                    return result.returnValue;
                }
            }
        }
        var context = this.getResolveContext(), dataResolverArg = context.dataResolverArg;
        if (this._alwaysExecute || !this._updateItems.length || context.hasUpdateData) {
            if (this._realConfirmMessage) {
                var self = this;
                dorado.MessageBox.confirm(this._realConfirmMessage, function () {
                    doUpdate.call(self, context, dataResolverArg);
                });
                if (this._confirmMessage === null) {
                    this._confirmMessage = this._realConfirmMessage;
                    this._realConfirmMessage = null;
                }
            } else {
                doUpdate.call(this, context, dataResolverArg);
            }
        } else {
            if (callback) {
                $callback(callback, false);
            } else {
                return false;
            }
        }
    }});
})();
(function () {
    var form_prefix = "form_submit_action_", form_seed = 1;
    var dateToJSON = function (date) {
        function f(n) {
            return n < 10 ? "0" + n : n;
        }
        return date.getUTCFullYear() + "-" + f(date.getUTCMonth() + 1) + "-" + f(date.getUTCDate()) + "T" + f(date.getUTCHours()) + ":" + f(date.getUTCMinutes()) + ":" + f(date.getUTCSeconds()) + "Z";
    };
    dorado.widget.FormSubmitAction = $extend(dorado.widget.Action, {$className:"dorado.widget.FormSubmitAction", ATTRIBUTES:{action:{}, target:{defaultValue:"_self"}, method:{defaultValue:"post"}}, doSubmitData:function (data) {
        var action = this, form = document.createElement("form");
        form.name = form_prefix + form_seed++;
        form.style.display = "none";
        form.action = dorado.Toolkits.translateURL(action._action);
        form.target = action._target || "_self";
        form.method = action._method || "post";
        for (var param in data) {
            var input = document.createElement("input"), value = data[param], string = "";
            if (value !== undefined) {
                if (value instanceof Date) {
                    string = dateToJSON(value);
                } else {
                    if (value.toString) {
                        string = value.toString();
                    }
                }
            }
            input.type = "hidden";
            input.value = string;
            input.name = param;
            form.appendChild(input);
        }
        document.body.appendChild(form);
        form.submit();
        document.body.removeChild(form);
    }, doExecuteSync:function () {
        var action = this, parameter = dorado.JSON.evaluate(action._parameter), data = {};
        if (parameter && parameter instanceof dorado.Entity) {
            data = parameter.toJSON();
        } else {
            if (parameter) {
                data = parameter;
            }
        }
        action.doSubmitData(data);
    }, doExecuteAsync:function () {
        this.doExecuteSync();
    }});
})();
dorado.widget.AbstractButton = $extend([dorado.widget.Control, dorado.widget.ActionSupport], {selectable:false, ATTRIBUTES:{disabled:{}, toggleable:{}, menu:{componentReference:true}, toggleOnShowMenu:{defaultValue:true}, toggled:{skipRefresh:true, setter:function (attr, value) {
    var button = this;
    if (button._toggled != value) {
        button._toggled = value;
        button.fireEvent("onToggle", button);
        button.doSetToggle(value);
    }
}}}, EVENTS:{onToggle:{}}, onClick:function () {
    var button = this, action = button._action;
    if (action) {
        action.execute && action.execute();
    }
    if (button._toggleable) {
        button.set("toggled", !button._toggled);
    }
    return false;
}});
(function () {
    var BUTTON_CLICK_CLASS = "-click", BUTTON_HOVER_CLASS = "-hover", BUTTON_TOGGLED_CLASS = "-toggled", BUTTON_DISABLED_CLASS = "-disabled";
    dorado.widget.SimpleButton = $extend(dorado.widget.AbstractButton, {$className:"dorado.widget.SimpleButton", ATTRIBUTES:{mouseDownClassName:{}, hoverClassName:{}, toggledClassName:{}, disabledClassName:{}}, doSetToggle:function () {
        var button = this, dom = button._dom, cls = button._className, toggledClass = button._toggledClassName;
        if (dom) {
            if (button._toggled) {
                $fly(dom).addClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
            } else {
                $fly(dom).removeClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
            }
        }
    }, doShowMenu:function () {
        var button = this, menu = button._menu, dom = button._dom, cls = button._className, toggledClass = button._toggledClassName;
        if (menu) {
            menu.addListener("onShow", function () {
                if (button._toggleOnShowMenu) {
                    $fly(dom).addClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
                }
                menu.addListener("onHide", function () {
                    if (button._toggleOnShowMenu) {
                        $fly(dom).removeClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
                    }
                    dorado.widget.setFocusedControl(button);
                }, {once:true});
            }, {once:true});
            menu._focusParent = button;
            menu.show({anchorTarget:button, align:"innerleft", vAlign:"bottom"});
        }
    }, createDom:function () {
        var button = this, dom = document.createElement("div"), cls = button._className, hoverClass = button._hoverClassName, mouseDownClass = button._mouseDownClassName;
        dom.className = cls;
        jQuery(dom).addClassOnHover(hoverClass ? hoverClass : (cls + BUTTON_HOVER_CLASS), null, function () {
            return !button._disabled;
        }).addClassOnClick(mouseDownClass ? mouseDownClass : (cls + BUTTON_CLICK_CLASS), null, function () {
            return !button._disabled;
        });
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var button = this, cls = button._className, disabledClass = button._disabledClassName, toggledClass = button._toggledClassName;
        if (button._disabled) {
            $fly(dom).addClass(disabledClass ? disabledClass : (cls + BUTTON_DISABLED_CLASS));
        } else {
            $fly(dom).removeClass(disabledClass ? disabledClass : (cls + BUTTON_DISABLED_CLASS));
        }
        if (button._toggled) {
            $fly(dom).addClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
        } else {
            $fly(dom).removeClass(toggledClass ? toggledClass : (cls + BUTTON_TOGGLED_CLASS));
        }
    }, onClick:function () {
        $invokeSuper.call(this, arguments);
        if (this._menu) {
            this.doShowMenu();
        }
    }});
    dorado.widget.SimpleIconButton = $extend(dorado.widget.SimpleButton, {$className:"dorado.widget.SimpleIconButton", ATTRIBUTES:{className:{defaultValue:"d-icon-button"}, width:{independent:true}, height:{independent:true}, icon:{}, iconClass:{}, showTrigger:{writeBeforeReady:true}}, createDom:function () {
        this._className = this._showTrigger === true || (this._menu && (this._showTrigger !== false)) ? this._className + "-trigger" : this._className;
        var dom = $invokeSuper.call(this, arguments);
        $fly(dom).addClass(this._className);
        dom.appendChild($DomUtils.xCreateElement({tagName:"div", className:"icon"}));
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var button = this, cls = button._className, action = button._action || {}, icon = button._icon || action._icon, iconClass = button._iconClass || action._iconClass;
        var iconEl = dom.firstChild;
        if (icon) {
            $DomUtils.setBackgroundImage(iconEl, icon);
        } else {
            if (iconClass) {
                iconEl.className = "icon " + iconClass;
            }
        }
        $fly(dom).toggleClass(cls + "-disabled", !!(button._disabled || action._disabled));
    }});
})();
(function () {
    var BUTTON_HOVER_CLASS = "-hover", BUTTON_LEFT_HOVER_CLASS = "-left-hover", BUTTON_RIGHT_HOVER_CLASS = "-right-hover", BUTTON_CLICK_CLASS = "-click", BUTTON_LEFT_CLICK_CLASS = "-left-click", BUTTON_RIGHT_CLICK_CLASS = "-right-click", BUTTON_TOGGLED_CLASS = "-toggled", BUTTON_LEFT_TOGGLED_CLASS = "-left-toggled", BUTTON_TRIGGER_TOGGLED_CLASS = "-right-toggled", BUTTON_TRIGGER_CLASS = "-trigger", ICON_CLASS = "icon";
    function hasClickListener(button) {
        return !!(button._events && button._events["onClick"] && button._events["onClick"].length > 0);
    }
    function needSplit(button) {
        return button._showTrigger !== false && (button._splitButton === true || (button._splitButton !== false && !!(hasClickListener(button) && button._menu)));
    }
    dorado.widget.Button = $extend(dorado.widget.AbstractButton, {$className:"dorado.widget.Button", focusable:true, ATTRIBUTES:{className:{defaultValue:"d-button"}, caption:{}, icon:{}, iconClass:{}, triggerToggled:{}, showTrigger:{writeBeforeReady:true}, splitButton:{writeBeforeReady:true}, width:{independent:true}, height:{independent:true, readOnly:true}}, EVENTS:{onTriggerClick:{}}, doOnKeyDown:function (event) {
        var button = this;
        if (event.keyCode == 32) {
            button.fireEvent("onClick", button);
        }
        return true;
    }, doSetToggle:function () {
        var button = this, dom = button._dom, cls = button._className;
        if (dom) {
            if (button._toggled) {
                $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_LEFT_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS));
            } else {
                $fly(dom).removeClass(cls + BUTTON_TOGGLED_CLASS).removeClass(cls + BUTTON_LEFT_TOGGLED_CLASS);
            }
        }
    }, doShowMenu:function () {
        var button = this, menu = button._menu, dom = button._dom, cls = button._className;
        if (menu) {
            menu.addListener("onShow", function () {
                if (button._toggleOnShowMenu && !button._triggerToggled) {
                    $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_TRIGGER_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS));
                    button._triggerToggled = true;
                }
                menu.addListener("onHide", function () {
                    button.doAfterMenuHide();
                }, {once:true});
            }, {once:true});
            menu._focusParent = button;
            menu.show({anchorTarget:button, align:"innerleft", vAlign:"bottom"});
        }
    }, doAfterMenuHide:function () {
        var button = this, dom = button._dom;
        if (button._toggleOnShowMenu) {
            $fly(dom).removeClass(button._className + (needSplit(button) ? BUTTON_TRIGGER_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS));
            button._triggerToggled = false;
        }
    }, _createIconSpan:function (dom) {
        var button = this, doms = button._doms, action = button._action || {};
        dom = dom || button._dom;
        if (dom) {
            var icon = document.createElement("span");
            icon.className = ICON_CLASS;
            icon.innerHTML = "&nbsp;";
            $fly(icon).prependTo(doms.buttonLeft).addClass(button._iconClass || action._iconClass);
            $DomUtils.setBackgroundImage(icon, button._icon || action._icon);
            doms.icon = icon;
        }
    }, onClick:function () {
        var button = this, action = button._action || {}, disabled = button._disabled || action._disabled;
        if (!disabled) {
            $invokeSuper.call(this, arguments);
            if (this._menu && !hasClickListener(this)) {
                this.doShowMenu();
            }
        }
    }, onResize:function () {
        var button = this, dom = button._dom, width = button.getRealWidth();
        if (dom && width != null) {
            $fly(dom).width(width);
            var leftWidth = dom.offsetWidth - button._doms.buttonRight.offsetWidth - parseInt($fly(button._doms.buttonLeft).css("margin-left"), 10);
            if (leftWidth > 0) {
                $fly(button._doms.buttonLeft).outerWidth(leftWidth);
            }
        }
    }, createDom:function () {
        var button = this, cls = button._className, doms = {}, action = button._action || {};
        var dom = $DomUtils.xCreateElement({tagName:"span", className:cls, content:[{tagName:"span", className:"button-left", contextKey:"buttonLeft", content:{tagName:"span", className:"caption", content:button._caption || action._caption, contextKey:"caption"}}, {tagName:"span", className:"button-right", contextKey:"buttonRight"}]}, null, doms);
        button._doms = doms;
        $fly(doms.buttonLeft).hover(function () {
            var action = button._action || {}, disabled = button._disabled || action._disabled;
            if (!disabled && !button._toggled) {
                $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_LEFT_HOVER_CLASS : BUTTON_HOVER_CLASS));
            }
        }, function () {
            $fly(dom).removeClass(cls + BUTTON_HOVER_CLASS).removeClass(cls + BUTTON_LEFT_HOVER_CLASS);
        }).mousedown(function () {
            var action = button._action || {}, disabled = button._disabled || action._disabled;
            if (!disabled) {
                var clazz = cls + (needSplit(button) ? BUTTON_LEFT_CLICK_CLASS : BUTTON_CLICK_CLASS);
                $fly(dom).addClass(clazz);
                $fly(document).one("mouseup", function () {
                    $fly(dom).removeClass(clazz);
                });
            }
        });
        $fly(doms.buttonRight).hover(function () {
            var action = button._action || {}, disabled = button._disabled || action._disabled;
            if (!disabled) {
                $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_RIGHT_HOVER_CLASS : BUTTON_HOVER_CLASS));
            }
        }, function () {
            $fly(dom).removeClass(cls + BUTTON_HOVER_CLASS).removeClass(cls + BUTTON_RIGHT_HOVER_CLASS);
        }).mousedown(function () {
            var action = button._action || {}, disabled = button._disabled || action._disabled;
            if (!disabled) {
                var clazz = cls + (needSplit(button) ? BUTTON_RIGHT_CLICK_CLASS : BUTTON_CLICK_CLASS);
                $fly(dom).addClass(clazz);
                $fly(document).one("mouseup", function () {
                    $fly(dom).removeClass(clazz);
                });
            }
        }).click(function (event) {
            var action = button._action || {}, disabled = button._disabled || action._disabled;
            if (!disabled) {
                button.fireEvent("onTriggerClick", button);
                if (button._menu) {
                    button.doShowMenu();
                } else {
                    if (button.onClick(event) === false) {
                        return false;
                    }
                    button.fireEvent("onClick", button, {button:event.button, event:event});
                }
            }
            event.stopImmediatePropagation();
        });
        if (button._toggleable) {
            if (button._toggled) {
                $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_LEFT_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS));
            } else {
                $fly(dom).removeClass(cls + BUTTON_TOGGLED_CLASS).removeClass(cls + BUTTON_LEFT_TOGGLED_CLASS);
            }
        }
        $fly(dom).toggleClass(cls + BUTTON_TRIGGER_CLASS, button._showTrigger === true || (!!button._menu && button._showTrigger !== false)).toggleClass(cls + "-disabled", !!(button._disabled || action._disabled));
        var icon = button._icon || action._icon, iconCls = button._iconClass || action._iconClass;
        if (icon || iconCls) {
            button._createIconSpan(dom);
        }
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var button = this, cls = button._className, doms = button._doms, action = button._action || {}, disabled = button._disabled || action._disabled;
        $fly(button._doms.caption).html(button._caption || action._caption);
        button._dom.disabled = disabled;
        if (button._toggleable) {
            if (button._toggled) {
                $fly(dom).addClass(cls + (needSplit(button) ? BUTTON_LEFT_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS));
            } else {
                $fly(dom).removeClass(cls + BUTTON_TOGGLED_CLASS).removeClass(cls + BUTTON_LEFT_TOGGLED_CLASS);
            }
        }
        $fly(dom).toggleClass(cls + BUTTON_TRIGGER_CLASS, button._showTrigger === true || (!!button._menu && button._showTrigger !== false)).toggleClass(cls + (needSplit(button) ? BUTTON_TRIGGER_TOGGLED_CLASS : BUTTON_TOGGLED_CLASS), !!button._triggerToggled).toggleClass(cls + "-disabled", !!(button._disabled || action._disabled));
        var icon = button._icon || action._icon, iconCls = button._iconClass || action._iconClass;
        if (!icon && !iconCls && doms.icon) {
            $fly(doms.icon).css("display", "none");
        } else {
            $fly(doms.icon).attr("className", ICON_CLASS).css("display", "");
            if ((icon || iconCls) && !doms.icon) {
                button._createIconSpan();
            }
            if (icon) {
                $DomUtils.setBackgroundImage(doms.icon, icon);
            } else {
                if (doms.icon) {
                    $fly(doms.icon).css("background", "");
                }
            }
            if (iconCls) {
                $fly(doms.icon).addClass(iconCls);
            }
        }
        button.onResize();
    }});
})();
(function () {
    function centerCover(dom, doms) {
        var width = $fly(dom).width(), height = $fly(dom).height(), loadingCoverImg = doms.loadingCoverImg, imgWidth = $fly(loadingCoverImg).outerWidth(), imgHeight = $fly(loadingCoverImg).outerHeight();
        $fly(loadingCoverImg).left((width - imgWidth) / 2).top((height - imgHeight) / 2);
    }
    var BLANK_PATH = "about:blank";
    dorado.widget.IFrame = $extend(dorado.widget.Control, {$className:"dorado.widget.IFrame", ATTRIBUTES:{className:{defaultValue:"d-iframe"}, path:{skipRefresh:true, setter:function (attr, value) {
        var frame = this, dom = frame._dom, doms = frame._doms;
        if (dom) {
            $fly(doms.loadingCover).css("display", "block");
            $fly(doms.iframe).attr("src", value || BLANK_PATH).addClass("hidden");
            centerCover(dom, doms);
        }
        frame._path = value;
    }}, iFrameWindow:{readOnly:true, getter:function () {
        return this.getIFrameWindow();
    }}}, EVENTS:{onLoad:{}}, createDom:function () {
        var frame = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:frame._className, content:[{tagName:"iframe", contextKey:"iframe", frameBorder:0, className:"hidden"}, {tagName:"div", contextKey:"loadingCover", className:"frame-loading-cover", content:{tagName:"div", className:"frame-loading-image", contextKey:"loadingCoverImg"}}]}, null, doms);
        frame._doms = doms;
        var iframe = doms.iframe, loadingCoverImg = doms.loadingCoverImg;
        $fly(iframe).attr("src", $url(frame._path || BLANK_PATH)).load(function () {
            $fly(doms.loadingCover).css("display", "none");
            if (!(dorado.Browser.msie && dorado.Browser.version == 6)) {
                $fly(iframe).removeClass("hidden");
            }
            frame.fireEvent("onLoad", frame);
        });
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        centerCover(dom, this._doms);
    }, getIFrameWindow:function () {
        var frame = this, doms = frame._doms || {};
        if (doms.iframe) {
            return doms.iframe.contentWindow;
        }
        return null;
    }});
})();
(function () {
    var TAB_CLOSEABLE_CLASS = "-closeable", TAB_DISABLED_CLASS = "-disabled", ICON_CLASS = "icon";
    dorado.widget.tab = {};
    function createTabCloseDom(tab, dom, doms) {
        var closeEl = $DomUtils.xCreateElement({tagName:"span", className:"close", contextKey:"close"}, null, doms);
        doms.tabLeft.appendChild(closeEl);
        jQuery(closeEl).click(function (event) {
            if (!tab._disabled) {
                tab.close();
            }
            event.stopImmediatePropagation();
        }).addClassOnHover("close-hover", null, function () {
            return !tab._disabled;
        }).addClassOnClick("close-click", null, function () {
            return !tab._disabled;
        });
        $fly(dom).addClass(tab._className + TAB_CLOSEABLE_CLASS);
    }
    dorado.widget.tab.Tab = $extend(dorado.RenderableElement, {$className:"dorado.widget.tab.Tab", ATTRIBUTES:{className:{defaultValue:"tab"}, name:{}, caption:{}, closeable:{}, icon:{}, iconClass:{}, disabled:{setter:function (attr, value) {
        var tab = this, tabbar = tab._parent;
        if (tabbar) {
            if (value) {
                tabbar.disableTab(tab);
            } else {
                tabbar.enableTab(tab);
            }
        } else {
            tab._disabled = value;
        }
    }}, visible:{defaultValue:true, skipRefresh:true, setter:function (attr, value) {
        var tab = this, tabbar = tab._parent;
        if (tabbar && tab) {
            tabbar.doSetTabVisible(tab, value);
        }
        tab._visible = value;
    }}, parent:{}, userData:{}}, constructor:function (config) {
        $invokeSuper.call(this, arguments);
        if (config) {
            this.set(config);
        }
    }, _createIconSpan:function () {
        var tab = this, doms = tab._doms;
        var iconEl = document.createElement("span");
        iconEl.className = "icon";
        doms.icon = iconEl;
        $fly(iconEl).prependTo(doms.tabLeft);
        $DomUtils.setBackgroundImage(iconEl, tab._icon);
    }, refreshDom:function (dom) {
        var tab = this, closeable = tab._closeable, disabled = tab._disabled, visible = tab._visible, doms = tab._doms, captionEl = doms.caption, closeEl = doms.close, width = tab._width, tabbar = tab._parent, tabMinWidth;
        $DomUtils.disableUserSelection(dom);
        $fly(captionEl).text(tab._caption);
        if (closeable) {
            if (!closeEl) {
                createTabCloseDom(tab, dom, doms);
            }
        } else {
            if (closeEl) {
                $fly(closeEl).remove();
                $fly(dom).removeClass(tab._className + TAB_CLOSEABLE_CLASS);
            }
        }
        $fly(dom).css("display", visible ? "" : "none");
        if (disabled) {
            $fly(dom).addClass(tab._className + TAB_DISABLED_CLASS);
        } else {
            $fly(dom).removeClass(tab._className + TAB_DISABLED_CLASS);
        }
        jQuery(dom).addClassOnHover(tab._className + "-hover", null, function () {
            return !tab._disabled;
        });
        if (tabbar && !width) {
            tabMinWidth = tabbar._tabMinWidth;
            if (dom.offsetWidth < tabMinWidth) {
                width = tab._width = tabMinWidth;
            }
        }
        if (width) {
            $fly(dom).outerWidth(width);
            var leftEl = $fly(doms.tabLeft);
            leftEl.width(jQuery(dom).width() - (parseInt(leftEl.css("margin-left"), 10) || 0) - (parseInt(leftEl.css("margin-right"), 10) || 0) - (parseInt(leftEl.css("padding-left"), 10) || 0) - (parseInt(leftEl.css("padding-right"), 10) || 0));
        }
        var icon = tab._icon, iconCls = tab._iconClass;
        if (!icon && !iconCls && doms.icon) {
            $fly(doms.icon).css("display", "none");
        } else {
            $fly(doms.icon).attr("className", ICON_CLASS).css("display", "");
            if ((icon || iconCls) && !doms.icon) {
                tab._createIconSpan();
            }
            if (icon) {
                $DomUtils.setBackgroundImage(doms.icon, icon);
            } else {
                if (doms.icon) {
                    $fly(doms.icon).css("background-image", "none");
                }
            }
            if (iconCls) {
                $fly(doms.icon).addClass(iconCls);
            }
        }
    }, close:function () {
        var tab = this, tabbar = tab._parent;
        if (tabbar) {
            tabbar.removeTab(tab);
        }
    }, getDom:function () {
        if (!this._dom) {
            this._dom = this.createDom();
        }
        return this._dom;
    }, render:function (ctEl, index) {
        var dom = this.getDom();
        if (!dom) {
            return;
        }
        if (!ctEl) {
            ctEl = document.body;
        }
        if (dom.parentNode != ctEl) {
            if (index != null) {
                var refEl = ctEl.childNodes[index];
                if (!refEl) {
                    ctEl.appendChild(dom);
                } else {
                    ctEl.insertBefore(dom, refEl);
                }
            } else {
                ctEl.appendChild(dom);
            }
        }
        this._rendered = true;
    }, createDom:function () {
        var tab = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"li", className:tab._className, content:[{tagName:"span", className:"tab-left", contextKey:"tabLeft", content:{tagName:"span", className:"caption", content:tab._caption, contextKey:"caption"}}, {tagName:"span", className:"tab-right", contextKey:"tabRight"}]}, null, doms);
        tab._doms = doms;
        $fly(dom).click(function () {
            var tabbar = tab._parent, disabled = tab._disabled;
            if (tabbar) {
                if (!disabled) {
                    tabbar.doChangeCurrentTab(tab);
                }
            }
        });
        if (tab._closeable) {
            createTabCloseDom(tab, dom, doms);
        }
        jQuery(dom).addClassOnHover(tab._className + "-hover", null, function () {
            return !tab._disabled;
        }).bind("contextmenu", function (event) {
            event = jQuery.event.fix(event || window.event);
            event.preventDefault();
            event.returnValue = false;
            var tabbar = tab._parent, arg = {tab:tab, event:event};
            tabbar._contextMenuTab = tab;
            tabbar.fireEvent("onTabContextMenu", tabbar, arg);
            return false;
        });
        if (tab._icon || tab._iconClass) {
            tab._createIconSpan();
        }
        return dom;
    }, getControl:function () {
        return (this.doGetControl) ? this.doGetControl() : null;
    }});
    dorado.widget.tab.ControlTab = $extend(dorado.widget.tab.Tab, {$className:"dorado.widget.tab.ControlTab", ATTRIBUTES:{control:{setter:function (attr, value) {
        if (value && value.constructor == Object.prototype.constructor) {
            value = dorado.Toolkits.createInstance("widget", value);
        }
        this._control = value;
    }}}, doGetControl:function () {
        var result = this._control;
        if (!result) {
            result = this._control = new dorado.widget.Control();
        }
        return result;
    }});
    dorado.widget.tab.IFrameTab = $extend(dorado.widget.tab.ControlTab, {$className:"dorado.widget.tab.IFrameTab", ATTRIBUTES:{path:{path:"_control.path"}}, doGetControl:function () {
        var tab = this, iframe = this._control;
        if (!iframe) {
            iframe = this._control = new dorado.widget.IFrame({path:tab._path});
        }
        return iframe;
    }});
})();
(function () {
    var TABBAR_CLASS = "d-tabbar", TABS_CLASS = "tabs", TABS_WRAP_CLASS = "tabs-wrap", LEFT_BUTTON_CLASS = "left-button", RIGHT_BUTTON_CLASS = "right-button", MENU_BUTTON_CLASS = "menu-button";
    function createNavButtons(tabbar, dom) {
        if (!dom || !tabbar) {
            return;
        }
        var doms = tabbar._doms, tabbarDom = doms.tabbar, leftBtn, rightBtn;
        leftBtn = tabbar._leftButton = new dorado.widget.SimpleButton({className:LEFT_BUTTON_CLASS, listener:{onClick:function () {
            tabbar.doScrollLeft(true);
        }}});
        rightBtn = tabbar._rightButton = new dorado.widget.SimpleButton({className:RIGHT_BUTTON_CLASS, listener:{onClick:function () {
            tabbar.doScrollRight(true);
        }}});
        tabbar.registerInnerControl(leftBtn);
        tabbar.registerInnerControl(rightBtn);
        leftBtn.render(tabbarDom);
        tabbarDom.insertBefore(leftBtn._dom, tabbarDom.firstChild);
        rightBtn.render(tabbarDom);
        tabbarDom.insertBefore(rightBtn._dom, doms.tabsWrap);
        doms.leftButton = leftBtn._dom;
        doms.rightButton = rightBtn._dom;
        $fly(doms.leftButton).repeatOnClick(function () {
            tabbar.doScrollLeft(false, 12);
        }, 30);
        $fly(doms.rightButton).repeatOnClick(function () {
            tabbar.doScrollRight(false, 12);
        }, 30);
    }
    function insertMenuItem(tabbar, tab, navmenu, index) {
        navmenu.insertItem({caption:tab._caption, disabled:tab._disabled, visible:tab._visible, listener:{onClick:function () {
            tabbar.set("currentTab", tab);
        }}}, index);
    }
    function createMenuButton(tabbar, dom) {
        if (!dom) {
            return;
        }
        var wrapEl = dom.lastChild, doms = tabbar._doms, rightButtonEl = doms.rightButton, refEl = wrapEl;
        if (rightButtonEl) {
            refEl = rightButtonEl;
        }
        var navmenu = tabbar._navmenu = new dorado.widget.Menu({listener:{beforeShow:function (self, configs) {
            if (tabbar._tabPlacement == "top") {
                dorado.Object.apply(configs, {anchorTarget:menuBtn, align:"innerright", vAlign:"bottom"});
            } else {
                dorado.Object.apply(configs, {anchorTarget:menuBtn, align:"innerright", vAlign:"top"});
            }
        }}}), tabs = tabbar._tabs, tab;
        for (var i = 0, j = tabs.size; i < j; i++) {
            tab = tabs.get(i);
            insertMenuItem(tabbar, tab, navmenu, null);
        }
        var menuBtn = tabbar._menuButton = new dorado.widget.SimpleButton({className:MENU_BUTTON_CLASS, menu:navmenu});
        menuBtn.render(dom);
        dom.insertBefore(menuBtn._dom, refEl);
        doms.menuButton = menuBtn._dom;
    }
    dorado.widget.TabBar = $extend(dorado.widget.Control, {$className:"dorado.widget.TabBar", focusable:true, ATTRIBUTES:{className:{defaultValue:TABBAR_CLASS}, currentTab:{setter:function (attr, value) {
        var tabbar = this, tabs = tabbar._tabs, tab = value;
        if (typeof value == "number" || typeof value == "string") {
            tab = tabs.get(tab);
        }
        if (tab && !tab._disabled) {
            tabbar.doChangeCurrentTab(tab);
        }
    }}, tabs:{setter:function (attr, value) {
        var tabs = this._tabs;
        if (value && value instanceof Array) {
            for (var i = 0, j = value.length; i < j; i++) {
                var tab = value[i];
                if (!(tab instanceof dorado.widget.tab.Tab)) {
                    tab = dorado.Toolkits.createInstance("tab", tab);
                }
                tab._parent = this;
                tabs.insert(tab);
            }
        }
    }}, alwaysShowNavButtons:{skipRefresh:true, setter:function (attr, value) {
        var tabbar = this;
        tabbar._alwaysShowNavButtons = value;
        if (value) {
            tabbar.showNavButtons();
            tabbar.refreshButtons();
        } else {
            tabbar.hideNavButtons();
        }
    }}, showMenuButton:{skipRefresh:true, setter:function (attr, value) {
        var tabbar = this, dom = tabbar._dom, doms = tabbar._doms;
        if (dom) {
            if (value) {
                if (!doms.menuButton) {
                    createMenuButton(tabbar, dom);
                } else {
                    $fly(doms.menuButton).css("display", "");
                }
            } else {
                if (doms.menuButton) {
                    $fly(doms.menuButton).css("display", "none");
                }
            }
            tabbar.refreshButtons();
            tabbar.onToolButtonVisibleChange();
        }
        tabbar._showMenuButton = value;
    }}, tabMinWidth:{writeOnce:true}, tabPlacement:{skipRefresh:true, defaultValue:"top", setter:function (attr, value) {
        if (this._rendered) {
            this.doChangeTabPlacement(value);
        } else {
            this._tabPlacement = value;
        }
    }}, contextMenuTab:{readOnly:true}, height:{independent:true, readOnly:true}}, EVENTS:{beforeTabChange:{}, onTabChange:{}, onTabContextMenu:{}}, constructor:function () {
        this._tabs = new dorado.util.KeyedArray(function (value) {
            return value._name;
        });
        $invokeSuper.call(this, arguments);
    }, disableTab:function (tab) {
        var tabbar = this, tabDom, navmenu = tabbar._navmenu, tabs = tabbar._tabs, index;
        tab = tabbar.getTab(tab);
        index = tabs.indexOf(tab);
        tabDom = tab._dom;
        tab._disabled = true;
        if (tabDom) {
            $fly(tabDom).addClass("tab-disabled");
            if (tab == tabbar._currentTab) {
                var newCurrentTab = tabbar.getAvialableTab(tab);
                tabbar.doChangeCurrentTab(newCurrentTab);
            }
        }
        if (navmenu) {
            navmenu.getItem(index).set("disabled", true);
        }
    }, enableTab:function (tab) {
        var tabbar = this, tabDom, navmenu = tabbar._navmenu, tabs = tabbar._tabs, index;
        tab = tabbar.getTab(tab);
        index = tabs.indexOf(tab);
        tabDom = tab._dom;
        tab._disabled = false;
        if (tabDom) {
            $fly(tabDom).removeClass("tab-disabled");
        }
        if (navmenu) {
            navmenu.getItem(index).set("disabled", false);
        }
    }, doSetTabVisible:function (tab, visible) {
        var tabbar = this, tabDom, navmenu = tabbar._navmenu, tabs = tabbar._tabs, index;
        tab = tabbar.getTab(tab);
        index = tabs.indexOf(tab);
        tabDom = tab._dom;
        if (tab._visible == visible) {
            return;
        }
        if (tabDom) {
            if (visible) {
                tabDom.style.display = "";
            } else {
                tabDom.style.display = "none";
                if (tab == tabbar._currentTab) {
                    var newCurrentTab = tabbar.getAvialableTab(tab);
                    tabbar.doChangeCurrentTab(newCurrentTab);
                }
            }
            tabbar.refreshButtons();
        }
        if (navmenu) {
            navmenu.getItem(index).set("visible", visible);
        }
    }, getTab:function (tab) {
        var tabbar = this, tabs = tabbar._tabs;
        if (typeof tab == "number" || typeof tab == "string") {
            return tabs.get(tab);
        } else {
            return tab;
        }
    }, insertTab:function (tab, index, current) {
        if (!tab) {
            throw new dorado.ResourceException("dorado.base.TabUndefined");
        }
        var tabbar = this, tabs = tabbar._tabs;
        if (tabs) {
            if (!(tab instanceof dorado.widget.tab.Tab)) {
                tab = dorado.Toolkits.createInstance("tab", tab);
                tab._parent = tabbar;
            }
            tabbar.doInsertTab(tab, index, current);
            return tab;
        }
        return null;
    }, doInsertTab:function (tab, index, current) {
        var tabbar = this, tabs = tabbar._tabs, doms = tabbar._doms, navmenu = tabbar._navmenu;
        if (index != null) {
            tabs.insert(tab, index);
        } else {
            index = tabs.size;
            tabs.insert(tab);
        }
        if (navmenu) {
            insertMenuItem(tabbar, tab, navmenu, index);
        }
        if (tabbar._rendered) {
            tab.render(doms.tabs, index);
            tab.refreshDom(tab._dom);
        }
        tabbar.doRefreshGap();
        if (current !== false) {
            tabbar.doChangeCurrentTab(tab);
        }
    }, removeTab:function (tab) {
        var tabbar = this, tabs = tabbar._tabs, navmenu = tabbar._navmenu, index;
        if (tabs) {
            tab = tabbar.getTab(tab);
            if (navmenu) {
                index = tabs.indexOf(tab);
                navmenu.removeItem(index);
            }
            tabbar.doRemoveTab(tab);
        }
    }, doRemoveTab:function (tab) {
        var tabbar = this, tabs = tabbar._tabs;
        if (tab != tabbar._currentTab) {
            tabs.remove(tab);
            $fly(tab._dom).remove();
        } else {
            var avialableTab = tabbar.getAvialableTab(tab);
            tabs.removeAt(tabs.indexOf(tab));
            $fly(tab._dom).remove();
            tabbar.doChangeCurrentTab(avialableTab);
        }
        tabbar.doRefreshGap();
        tabbar.refreshButtons();
    }, getAvialableTab:function (tab) {
        var tabbar = this, tabs = tabbar._tabs, index, result, i, j;
        if (tabs) {
            if (!tab) {
                index = -1;
            } else {
                index = tabs.indexOf(tab);
            }
            for (i = index - 1; i >= 0; i--) {
                result = tabs.get(i);
                if (!result._disabled && result._visible) {
                    return result;
                }
            }
            for (i = index + 1, j = tabs.size; i < j; i++) {
                result = tabs.get(i);
                if (!result._disabled && result._visible) {
                    return result;
                }
            }
        }
        return null;
    }, removeAllTabs:function () {
        var tabbar = this, tabs = tabbar._tabs;
        for (var i = 0, j = tabs.size; i < j; i++) {
            tabbar.removeTab(tabs.get(0));
        }
    }, createDom:function () {
        var tabbar = this, tabs = tabbar._tabs, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:tabbar._className, contextKey:"tabbar", content:[{tagName:"div", className:TABS_WRAP_CLASS, contextKey:"tabsWrap", content:{tagName:"ul", className:TABS_CLASS, contextKey:"tabs"}}]}, null, doms), jDom = jQuery(dom);
        tabbar._doms = doms;
        jDom.addClass(tabbar._className + (tabbar._tabPlacement == "top" ? "-top" : "-bottom"));
        if (tabbar._alwaysShowNavButtons) {
            createNavButtons(tabbar, dom);
        }
        if (tabbar._showMenuButton) {
            createMenuButton(tabbar, dom);
        }
        var tabsEl = doms.tabs, currentTab;
        if (tabs) {
            for (var i = 0, j = tabs.size; i < j; i++) {
                var tab = tabs.get(i);
                if (tab._current) {
                    currentTab = tab;
                }
                tab.render(tabsEl);
            }
            if (!currentTab) {
                currentTab = tabbar.getAvialableTab();
            }
            if (currentTab) {
                tabbar.doChangeCurrentTab(currentTab);
            }
        }
        $fly(doms.tabsWrap).mousewheel(function (event, delta) {
            if (tabbar._overflowing) {
                if (delta < 0) {
                    tabbar.doScrollRight(false);
                } else {
                    if (delta > 0) {
                        tabbar.doScrollLeft(false);
                    }
                }
            }
        });
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var tabbar = this, tabs = tabbar._tabs;
        tabbar.doRefreshGap();
        if (tabs) {
            var currentTab = tabbar._currentTab, curIndex = -1;
            if (currentTab) {
                curIndex = tabs.indexOf(currentTab);
            }
            for (var i = 0, j = tabs.size; i < j; i++) {
                var tab = tabs.get(i);
                tab.refreshDom(tab._dom);
            }
        }
        tabbar.onToolButtonVisibleChange();
        tabbar.refreshButtons();
    }, doFilterTabs:function (tabs) {
        var result = new dorado.util.KeyedArray(function (value) {
            return value._name;
        });
        for (var i = 0, j = tabs.size; i < j; i++) {
            var tab = tabs.get(i);
            if (tab._visible) {
                result.append(tab);
            }
        }
        return result;
    }, doRefreshGap:function () {
        var tabbar = this, tabs = tabbar.doFilterTabs(tabbar._tabs);
        if (tabs) {
            var currentTab = tabbar._currentTab, curIndex = -1;
            if (currentTab) {
                curIndex = tabs.indexOf(currentTab);
            }
            for (var i = 0, j = tabs.size; i < j; i++) {
                var tab = tabs.get(i), refresh = false;
                if ($fly(tab._dom).hasClass("last-tab") || $fly(tab._dom).hasClass("first-tab")) {
                    refresh = true;
                }
                $fly(tab._dom).removeClass("first-tab").removeClass("last-tab").removeClass("current-prev-tab").removeClass("current-next-tab");
                if (i == 0) {
                    $fly(tab._dom).addClass("first-tab");
                    refresh = true;
                }
                if (i == j - 1) {
                    $fly(tab._dom).addClass("last-tab");
                    refresh = true;
                }
                if (refresh) {
                    tab.refresh();
                }
                if (curIndex != -1) {
                    if (i == curIndex - 1) {
                        $fly(tab._dom).addClass("current-prev-tab");
                    } else {
                        if (i == curIndex + 1) {
                            $fly(tab._dom).addClass("current-next-tab");
                        }
                    }
                }
            }
        }
    }, doScrollLeft:function (anim, length) {
        var tabbar = this, doms = tabbar._doms, tabsEl = doms.tabs, to = parseInt(tabsEl.style.left, 10) + (length > 0 ? length : 100);
        if (anim) {
            $fly(tabsEl).animate({left:to > 0 ? 0 : to}, 300, null, function () {
                tabbar.refreshButtons();
            });
        } else {
            $fly(tabsEl).left(to > 0 ? 0 : to);
            tabbar.refreshButtons();
        }
    }, doScrollRight:function (anim, length) {
        var tabbar = this, doms = tabbar._doms, tabs = tabbar._tabs, tabsEl = doms.tabs, currentLeft = parseInt(tabsEl.style.left, 10);
        length = length > 0 ? length : 100;
        if (tabs) {
            var tabsWidth = tabbar.getTabsWidth(), visibleWidth = $fly(doms.tabsWrap).innerWidth(), to = currentLeft - length, rightWidth = tabsWidth + currentLeft - visibleWidth;
            if (rightWidth <= 0) {
                return false;
            } else {
                if (rightWidth < length) {
                    to = currentLeft - rightWidth;
                }
            }
        }
        if (anim) {
            $fly(tabsEl).animate({left:to}, 300, null, function () {
                tabbar.refreshButtons();
            });
        } else {
            $fly(tabsEl).left(to);
            tabbar.refreshButtons();
        }
    }, doChangeCurrentTab:function (tab) {
        var tabbar = this, doms = tabbar._doms, currentTab = tabbar._currentTab;
        if (tab == currentTab) {
            return false;
        }
        var eventArg = {newTab:tab, currentTab:currentTab};
        tabbar.fireEvent("beforeTabChange", tabbar, eventArg);
        if (eventArg.processDefault === false) {
            return;
        }
        if (currentTab) {
            $fly(currentTab._dom).removeClass("tab-selected");
        }
        if (tab) {
            var tabDom = tab._dom, tabsEl = doms.tabs, offsetLeft = tabDom.offsetLeft, offsetWidth = tabDom.offsetWidth, left = (parseInt(tabsEl.style.left, 10) || 0) * -1, viewWidth = $fly(doms.tabsWrap).width();
            tabbar._currentTab = tab;
            $fly(tabDom).addClass("tab-selected");
            tabbar.doRefreshGap();
            if (left > offsetLeft) {
                $fly(tabsEl).animate({left:-1 * offsetLeft}, 300, null, function () {
                    tabbar.refreshButtons();
                });
            } else {
                if ((left + viewWidth) < (offsetLeft + offsetWidth)) {
                    $fly(tabsEl).animate({left:-1 * (offsetLeft + offsetWidth - viewWidth)}, 300, null, function () {
                        tabbar.refreshButtons();
                    });
                } else {
                    tabbar.refreshButtons();
                }
            }
        } else {
            tabbar._currentTab = null;
        }
        tabbar.fireEvent("onTabChange", tabbar, {tab:tab});
        return true;
    }, doChangeTabPlacement:function (value) {
        var tabbar = this, cls = tabbar._className, doms = tabbar._doms, tabbarDom = doms.tabbar;
        if (tabbarDom) {
            var oldValue = tabbar._tabPlacement;
            $fly(tabbarDom).addClass(cls + "-" + value);
            if (oldValue) {
                $fly(tabbarDom).removeClass(cls + "-" + oldValue);
            }
        }
        tabbar._tabPlacement = value;
        return true;
    }, getTabsWidth:function () {
        var tabbar = this, tabs = tabbar._tabs, lastTab, lastDom;
        if (tabs) {
            lastTab = tabs.get(tabs.size - 1);
            if (lastTab) {
                lastDom = lastTab._dom;
                if (lastDom) {
                    return lastDom.offsetLeft + $fly(lastDom).outerWidth();
                }
            }
        }
        return 0;
    }, refreshButtons:function () {
        var tabbar = this, dom = tabbar._dom, tabs = tabbar._tabs, doms = tabbar._doms;
        if (!dom || !tabs) {
            return;
        }
        var leftButton = tabbar._leftButton, rightButton = tabbar._rightButton;
        var tabsWidth = tabbar.getTabsWidth(), tabsEl = doms.tabs, currentLeft = parseInt(tabsEl.style.left, 10);
        var visibleWidth = $fly(doms.tabsWrap).width();
        if (tabsWidth > 0) {
            if (tabbar._alwaysShowNavButtons !== true && visibleWidth > tabsWidth) {
                tabbar._overflowing = false;
                if (rightButton) {
                    rightButton.set("disabled", true);
                    if (tabsWidth + currentLeft <= visibleWidth) {
                        $fly(tabsEl).left(0);
                        if (!tabbar._alwaysShowNavButtons) {
                            tabbar.hideNavButtons();
                        }
                    }
                }
            } else {
                tabbar._overflowing = true;
                if (!tabbar._alwaysShowNavButtons) {
                    tabbar.showNavButtons();
                    visibleWidth = $fly(doms.tabsWrap).innerWidth();
                    currentLeft = parseInt(tabsEl.style.left, 10);
                }
                if (!rightButton) {
                    rightButton = tabbar._rightButton;
                    leftButton = tabbar._leftButton;
                }
                if (tabsWidth + currentLeft > visibleWidth) {
                    rightButton.set("disabled", false);
                } else {
                    if (tabsWidth + currentLeft < visibleWidth) {
                        $fly(tabsEl).left(visibleWidth - tabsWidth);
                        rightButton.set("disabled", true);
                    } else {
                        if (tabsWidth + currentLeft == visibleWidth) {
                            rightButton.set("disabled", true);
                        }
                    }
                }
                if (parseInt(tabsEl.style.left, 10) < 0) {
                    leftButton.set("disabled", false);
                } else {
                    leftButton.set("disabled", true);
                }
                if (leftButton._disabled && rightButton._disabled) {
                    $fly(tabsEl).left(0);
                }
            }
        }
    }, showNavButtons:function () {
        var tabbar = this, dom = tabbar._dom, modifyLeft = true, doms = tabbar._doms;
        if (dom) {
            if (!doms.leftButton) {
                createNavButtons(tabbar, dom);
            } else {
                if ($fly(doms.leftButton).css("display") == "none") {
                    $fly([doms.leftButton, doms.rightButton]).css("display", "block");
                } else {
                    modifyLeft = false;
                }
            }
            if (modifyLeft) {
                var tabsEl = doms.tabs, left = parseInt(tabsEl.style.left, 10) || 0, leftButtonWidth = $fly(doms.leftButton).outerWidth(true), rightButtonWidth = $fly(doms.rightButton).outerWidth(true);
                $fly(tabsEl).left(left - leftButtonWidth - rightButtonWidth);
            }
            tabbar.onToolButtonVisibleChange();
        }
    }, hideNavButtons:function (force) {
        var tabbar = this, dom = tabbar._dom, doms = tabbar._doms;
        if (!dom) {
            return;
        }
        var leftButton = doms.leftButton, rightButton = doms.rightButton;
        if (leftButton && rightButton) {
            var tabsWidth = tabbar.getTabsWidth(), visibleWidth = $fly(doms.tabsWrap).innerWidth();
            if ((tabsWidth < visibleWidth) || force) {
                $fly(leftButton).css("display", "none");
                $fly(rightButton).css("display", "none");
                tabbar.onToolButtonVisibleChange();
            }
        }
    }, onToolButtonVisibleChange:function () {
        var tabbar = this, dom = tabbar._dom, doms = tabbar._doms;
        if (!dom) {
            return;
        }
        var leftButton = doms.leftButton, rightButton = doms.rightButton, menuButton = doms.menuButton;
        var leftWidth = 0, rightWidth = 0;
        if (leftButton && leftButton.style.display != "none") {
            leftWidth += $fly(leftButton).outerWidth(true);
        }
        if (rightButton && rightButton.style.display != "none") {
            rightWidth += $fly(rightButton).outerWidth(true);
        }
        if (menuButton) {
            var menuButtonVisible = menuButton.style.display != "none", menuButtonWidth = menuButtonVisible ? $fly(menuButton).outerWidth(true) : 0;
            if (rightWidth > 0 && menuButtonWidth > 0) {
                $fly(rightButton).css("right", menuButtonWidth);
            } else {
                if (rightButton) {
                    $fly(rightButton).css("right", "");
                }
            }
            rightWidth += menuButtonWidth;
        }
        $fly(doms.tabsWrap).css({"margin-left":leftWidth, "margin-right":rightWidth});
    }});
    dorado.Toolkits.registerPrototype("tab", {Default:dorado.widget.tab.Tab, Tab:dorado.widget.tab.Tab, IFrame:dorado.widget.tab.IFrameTab, Control:dorado.widget.tab.ControlTab});
})();
dorado.widget.AbstractPanel = $extend(dorado.widget.Container, {$className:"dorado.widget.AbstractPanel", ATTRIBUTES:{caption:{skipRefresh:true, path:"_captionBar.caption"}, buttons:{writeBeforeReady:true, innerComponent:"Button"}, buttonAlign:{defaultValue:"center", skipRefresh:true, setter:function (attr, value) {
    var panel = this, doms = panel._doms, oldValue = panel._buttonAlign;
    if (doms) {
        if (oldValue && oldValue != "center") {
            $fly(doms.buttonPanel).removeClass("button-panel-" + oldValue);
        }
        if (value && value != "center") {
            $fly(doms.buttonPanel).addClass("button-panel-" + value);
        }
    }
    panel._buttonAlign = value;
}}, collapseable:{}, visible_todelete:{setter:function (attr, value) {
    var oldValue = this._visible;
    this._visible = value;
    if (oldValue != value && value && this._rendered) {
        this.onResize();
    }
}}, collapsed:{getter:function (attr, value) {
    var panel = this;
    if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
        return panel._parent._collapsed;
    } else {
        return panel._collapsed;
    }
}, setter:function (attr, value) {
    var panel = this;
    if (panel._rendered) {
        panel.doSetCollapsed(value);
    } else {
        panel._collapsed = value;
    }
}}}, EVENTS:{beforeCollapse:{}, onCollapse:{}}, toggleCollapsed:function () {
    var panel = this, collapsed = panel.get("collapsed");
    panel.doSetCollapsed(!collapsed);
}, doSetCollapsed:function (collapsed) {
    var panel = this, dom = panel._dom, doms = panel._doms, collapseButton = panel._collapseButton, eventArg = {};
    panel.fireEvent("beforeCollapse", panel, eventArg);
    if (eventArg.processDefault === false) {
        return;
    }
    if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
        var direction = panel._parent._direction;
        if (collapseButton) {
            collapseButton.set("iconClass", collapsed ? ("expand-icon-" + direction) : ("collapse-icon-" + direction));
        }
        panel._parent.doSetCollapsed(collapsed, function () {
            panel.fireEvent("onCollapse", panel);
            panel.onResize(collapsed);
            panel.notifySizeChange();
        }, true);
    } else {
        panel._collapsed = collapsed;
        if (collapsed) {
            if (panel._rendered) {
                $fly(doms.body).safeSlideOut({direction:"b2t", start:function () {
                }, step:function () {
                }, complete:function () {
                    $fly(dom).addClass(panel._className + "-collapsed");
                    if (collapseButton) {
                        collapseButton.set("iconClass", "expand-icon");
                    }
                    panel.fireEvent("onCollapse", panel);
                    panel.onResize(collapsed);
                    panel.notifySizeChange();
                }});
            }
        } else {
            panel._collapsed = collapsed;
            if (panel._rendered) {
                $fly(doms.body).safeSlideIn({direction:"t2b", start:function () {
                    $fly(dom).removeClass(panel._className + "-collapsed");
                }, step:function () {
                }, complete:function () {
                    if (collapseButton) {
                        collapseButton.set("iconClass", "collapse-icon");
                    }
                    panel.fireEvent("onCollapse", panel);
                    panel.onResize(collapsed);
                    panel.notifySizeChange();
                }});
            }
        }
    }
}, _createButtonPanel:function (dom) {
    var panel = this, doms = panel._doms, buttonPanel = document.createElement("div");
    buttonPanel.className = "button-panel";
    doms.buttonPanel = buttonPanel;
    if (doms.body) {
        $fly(doms.body).append(buttonPanel);
    } else {
        $fly(dom).append(buttonPanel);
    }
    return buttonPanel;
}, onResize:function (collapsed) {
    this.doOnResize && this.doOnResize(collapsed);
    $invokeSuper.call(this, arguments);
}, initButtons:function (dom) {
    var panel = this, doms = panel._doms;
    if (panel._buttons) {
        var buttons = panel._buttons, button, buttonPanel;
        buttonPanel = panel._createButtonPanel(dom);
        for (var i = 0, j = buttons.length; i < j; i++) {
            button = buttons[i];
            panel.registerInnerControl(button);
            button.render(buttonPanel);
        }
        var buttonAlign = panel._buttonAlign;
        if (buttonAlign && buttonAlign != "center") {
            $fly(doms.buttonPanel).addClass("button-panel-" + buttonAlign);
        }
    }
}});
dorado.widget.Panel = $extend(dorado.widget.AbstractPanel, {$className:"dorado.widget.Panel", ATTRIBUTES:{className:{defaultValue:"d-panel"}, width:{defaultValue:300}, border:{writeOnce:true, defaultValue:"normal"}, background:{}, showCaptionBar:{writeBeforeReady:true}, icon:{skipRefresh:true, path:"_captionBar.icon"}, iconClass:{skipRefresh:true, path:"_captionBar.iconClass"}, tools:{innerComponent:"SimpleIconButton"}}, createDom:function () {
    var panel = this, doms = {}, border = panel._border, dom;
    if ((dorado.Browser.msie || !$setting["widget.panel.useCssCurveBorder"]) && border == "curve") {
        dom = $DomUtils.xCreateElement({tagName:"div", className:panel._className, content:[{tagName:"div", className:"panel-header-left", contextKey:"header", content:{tagName:"div", className:"panel-header-right"}}, {tagName:"div", className:"panel-body-left", contextKey:"body", content:{tagName:"div", className:"panel-body-right", content:[{tagName:"div", className:"panel-body", contextKey:"body", content:{tagName:"div", className:"content-panel", contextKey:"contentPanel"}}]}}, {tagName:"div", className:"panel-footer-left", contextKey:"footer", content:{tagName:"div", className:"panel-footer-right"}}]}, null, doms);
        panel._doms = doms;
    } else {
        dom = $DomUtils.xCreateElement({tagName:"div", className:panel._className, content:{tagName:"div", className:"panel-body", contextKey:"body", content:{tagName:"div", className:"content-panel", contextKey:"contentPanel"}}}, null, doms);
        panel._doms = doms;
    }
    var caption = panel._caption, showCaptionBar = panel._showCaptionBar;
    if (showCaptionBar !== false && (caption || showCaptionBar)) {
        $fly(dom).addClass(panel._className + "-showcaptionbar");
        var tools = panel._tools, toolButtons = [];
        if (tools instanceof Array) {
            for (var i = 0, j = tools.length; i < j; i++) {
                var tool = tools[i];
                if (tool) {
                    toolButtons.push(tool);
                }
            }
        }
        var captionBar = panel._captionBar = new dorado.widget.CaptionBar({caption:panel._caption, icon:panel._icon, buttons:toolButtons});
        if (doms.body) {
            captionBar.render(doms.body.parentNode, doms.body);
        } else {
            captionBar.render(dom, doms.contentPanel);
        }
        doms.captionBar = captionBar._dom;
        panel.registerInnerControl(captionBar);
        if (panel._collapseable) {
            panel._createCollapseButton();
            if (panel._collapsed) {
                $fly(doms.body).css("display", "none");
            }
        }
    }
    panel.initButtons(dom);
    $fly(dom).toggleClass(panel._className + "-normalborder", border == "normal").toggleClass(panel._className + "-curveborder", border == "curve");
    if (panel._collapsed) {
        $fly(dom).addClass(panel._className + "-collapsed");
    }
    return dom;
}, _createCollapseButton:function () {
    var panel = this;
    var collapseButton = panel._collapseButton = new dorado.widget.SimpleIconButton({iconClass:panel._collapsed ? "expand-icon" : "collapse-icon", onClick:function () {
        panel.toggleCollapsed();
    }});
    if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
        var direction = panel._parent._direction;
        collapseButton.set("iconClass", panel._collapsed ? "expand-icon-" + direction : "collapse-icon-" + direction);
    }
    panel._captionBar.addButton(collapseButton, 0);
}, doOnResize:function (collapsed) {
    var panel = this, border = panel._border, dom = panel._dom, doms = panel._doms, height = panel.getRealHeight();
    if (typeof height == "number" && height > 0) {
        if (collapsed == undefined) {
            collapsed = panel._collapsed;
        }
        if (collapsed) {
            if (border == "curve") {
                if (panel._parent instanceof dorado.widget.SplitPanel && panel._parent._sideControl == panel && panel._parent._collpaseable) {
                    $fly(doms.body).height("auto");
                }
            }
            $fly(dom).height("auto");
        } else {
            var buttonPanelHeight = 0, captionBarHeight = 0;
            if (doms.buttonPanel) {
                buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
            }
            if (doms.captionBar) {
                captionBarHeight = $fly(doms.captionBar).outerHeight(true);
            }
            if (border == "curve") {
                $fly(doms.body).outerHeight(jQuery(dom).height() - jQuery(doms.header).outerHeight() - jQuery(doms.footer).outerHeight() - captionBarHeight);
                $fly(doms.contentPanel).outerHeight(jQuery(doms.body).height() - buttonPanelHeight);
            } else {
                $fly(doms.contentPanel).outerHeight(jQuery(dom).height() - captionBarHeight - buttonPanelHeight);
            }
            $fly(dom).height("auto");
        }
    }
}, refreshDom:function (dom) {
    var panel = this, doms = panel._doms, border = panel._border;
    $invokeSuper.call(this, arguments);
    if (this._background) {
        doms.contentPanel.style.background = this._background;
    }
    $fly(dom).toggleClass(this._className + "-normalborder", border == "normal").toggleClass(this._className + "-curveborder", border == "curve");
}, getContentContainer:function () {
    var panel = this, doms = panel._doms;
    return doms.contentPanel;
}});
dorado.widget.FieldSet = $extend(dorado.widget.AbstractPanel, {ATTRIBUTES:{className:{defaultValue:"d-field-set"}}, createDom:function () {
    var fieldset = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"fieldset", className:fieldset._className, content:[{tagName:"legend", className:"field-set-legend", contextKey:"captionContainer", content:[{contextKey:"icon", tagName:"div", className:"field-set-button"}, {tagName:"span", contextKey:"caption", content:fieldset._caption}]}, {tagName:"div", className:"body", contextKey:"body", content:{contextKey:"contentPanel", tagName:"div", className:"content-panel"}}]}, null, doms);
    fieldset._doms = doms;
    jQuery(doms.icon).click(function () {
        fieldset.toggleCollapsed();
    }).addClassOnHover("field-set-button-hover").addClassOnClick("field-set-button-click");
    fieldset.initButtons(dom);
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var fieldset = this;
    $fly(dom)[fieldset._collapsed ? "addClass" : "removeClass"](fieldset._className + "-collapsed");
    $fly(fieldset._doms.caption).text(fieldset._caption);
}, doOnResize:function (collapsed) {
    var fieldset = this, dom = fieldset._dom, doms = fieldset._doms, height = fieldset._height;
    if (typeof height == "number" && height > 0) {
        if (collapsed == undefined) {
            collapsed = fieldset._collapsed;
        }
        if (collapsed) {
            $fly(dom).height("auto");
        } else {
            var buttonPanelHeight = 0, captionCtHeight = 0;
            if (doms.buttonPanel) {
                buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
            }
            if (doms.captionContainer) {
                captionCtHeight = $fly(doms.captionContainer).outerHeight(true);
            }
            $fly(doms.contentPanel).outerHeight(jQuery(dom).height() - captionCtHeight - buttonPanelHeight);
            $fly(dom).height("auto");
        }
    }
}, getContentContainer:function () {
    return this._doms.contentPanel;
}});
dorado.widget.GroupBox = $extend(dorado.widget.AbstractPanel, {ATTRIBUTES:{className:{defaultValue:"d-group-box"}}, createDom:function () {
    var groupBox = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:groupBox._className, content:[{tagName:"div", className:"group-box-bar", contextKey:"captionContainer", content:[{tagName:"div", className:"bar-right"}, {contextKey:"icon", tagName:"div", className:"group-box-button"}, {tagName:"div", className:"bar-caption", content:groupBox._caption, contextKey:"caption"}]}, {tagName:"div", className:"body", contextKey:"body", content:{contextKey:"contentPanel", tagName:"div", className:"content-panel"}}]}, null, doms);
    groupBox._doms = doms;
    jQuery(doms.icon).click(function () {
        groupBox.toggleCollapsed();
    }).addClassOnHover("group-box-button-hover").addClassOnClick("group-box-button-click");
    groupBox.initButtons(dom);
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var groupBox = this;
    $fly(dom)[groupBox._collapsed ? "addClass" : "removeClass"](groupBox._className + "-collapsed");
    $fly(groupBox._doms.caption).text(groupBox._caption);
}, doOnResize:function (collapsed) {
    var fieldset = this, dom = fieldset._dom, doms = fieldset._doms, height = fieldset._height;
    if (typeof height == "number" && height > 0) {
        if (collapsed == undefined) {
            collapsed = fieldset._collapsed;
        }
        if (collapsed) {
            $fly(dom).height("auto");
        } else {
            var buttonPanelHeight = 0, captionCtHeight = 0;
            if (doms.buttonPanel) {
                buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
            }
            if (doms.captionContainer) {
                captionCtHeight = $fly(doms.captionContainer).outerHeight(true);
            }
            $fly(doms.contentPanel).outerHeight(jQuery(dom).height() - captionCtHeight - buttonPanelHeight);
        }
    }
}, getContentContainer:function () {
    return this._doms.contentPanel;
}});
dorado.widget.CardBook = $extend(dorado.widget.Control, {$className:"dorado.widget.CardBook", ATTRIBUTES:{className:{defaultValue:"d-cardbook"}, currentControl:{skipRefresh:true, setter:function (attr, value) {
    var cardbook = this, controls = cardbook._controls;
    if (value != null) {
        if (typeof value == "string" || typeof value == "number") {
            value = controls.get(value);
        }
    }
    var oldValue = cardbook._currentControl;
    var eventArg = {oldValue:oldValue};
    cardbook.fireEvent("beforeCurrentChange", this, eventArg);
    if (eventArg.processDefault === false) {
        return;
    }
    if (oldValue) {
        var oldDom = oldValue._dom;
        if (oldDom) {
            oldDom.style.display = "none";
        }
    }
    cardbook._currentControl = value;
    var dom = cardbook._dom;
    if (dom && value) {
        value.set("width", $fly(dom).innerWidth());
        value.set("height", $fly(dom).innerHeight());
        if (!value._rendered) {
            value.render(dom);
            cardbook.registerInnerControl(value);
        } else {
            $fly(value._dom).css("display", "block");
        }
    }
    cardbook.fireEvent("onCurrentChange", this, value);
}}, controls:{writeOnce:true, innerComponent:"", setter:function (attr, value) {
    if (value) {
        var controls = this._controls, currentFirstControl = (controls.size == 0);
        if (value instanceof Array) {
            for (var i = 0; i < value.length; i++) {
                controls.insert(value[i]);
                value[i]._parent = value[i]._focusParent = this;
                if (i == 0 && currentFirstControl) {
                    this.set("currentControl", value[i]);
                }
            }
        } else {
            if (value.constructor == Object.prototype.constructor) {
                controls.insert(value);
                value._parent = value._focusParent = this;
                this.set("currentControl", value);
            }
        }
    }
}}}, EVENTS:{beforeCurrentChange:{}, onCurrentChange:{}}, constructor:function () {
    this._controls = new dorado.util.KeyedArray(function (value) {
        return value._id;
    });
    $invokeSuper.call(this, arguments);
}, addControl:function (control, index, current) {
    if (!control) {
        throw new dorado.ResourceException("dorado.base.CardControlUndefined");
    }
    var card = this, controls = card._controls;
    controls.insert(control, index);
    card.registerInnerControl(control);
    if (current !== false) {
        card.set("currentControl", control);
    }
    return control;
}, removeControl:function (control) {
    var card = this, controls = card._controls;
    control = card.getControl(control);
    if (control) {
        control.destroy && control.destroy();
        controls.remove(control);
        return control;
    }
    return null;
}, removeAllControls:function () {
    var card = this, controls = card._controls;
    for (var i = 0, j = controls.size; i < j; i++) {
        var item = controls.get(0);
        card.removeControl(item);
    }
}, getControl:function (id) {
    var card = this, controls = card._controls;
    if (controls) {
        if (typeof id == "number" || typeof id == "string") {
            return controls.get(id);
        } else {
            return id;
        }
    }
    return null;
}, createDom:function () {
    var dom = $invokeSuper.call(this, arguments);
    dom.className = this._className;
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var card = this, currentControl = card["_currentControl"];
    if (currentControl) {
        currentControl.set("width", $fly(dom).innerWidth());
        currentControl.set("height", $fly(dom).innerHeight());
        if (!currentControl._rendered) {
            currentControl.render(dom);
            card.registerInnerControl(currentControl);
        } else {
            $fly(currentControl._dom).css("display", "block");
        }
    }
}});
dorado.widget.TabControl = $extend(dorado.widget.TabBar, {$className:"dorado.widget.TabControl", ATTRIBUTES:{height:{defaultValue:200, independent:false, readOnly:false}}, constructor:function () {
    $invokeSuper.call(this, arguments);
    this._cardBook = new dorado.widget.CardBook();
    this.registerInnerControl(this._cardBook);
}, doChangeCurrentTab:function (tab) {
    var result = $invokeSuper.call(this, arguments);
    if (!result) {
        return false;
    }
    var tabpage = this, tabs = tabpage._tabs, index = typeof tab == "number" ? tab : tabs.indexOf(tab), card = tabpage._cardBook;
    if (card) {
        card.set("currentControl", index);
    }
    return true;
}, doChangeTabPlacement:function (value) {
    var result = $invokeSuper.call(this, arguments);
    if (!result) {
        return false;
    }
    var tabpage = this, dom = tabpage._dom;
    if (dom) {
        var tabbarDom = tabpage._tabbarDom, cardDom = tabpage._cardBook._dom;
        if (dorado.Browser.msie && dorado.Browser.version == 6) {
            if (value == "top") {
                dom.appendChild(cardDom);
            } else {
                dom.insertBefore(cardDom, tabbarDom);
            }
        } else {
            if (value == "top") {
                dom.insertBefore(tabbarDom, cardDom);
            } else {
                dom.appendChild(tabbarDom);
            }
        }
    }
    return true;
}, doRemoveTab:function (tab) {
    var tabpage = this, tabs = tabpage._tabs, index = typeof tab == "number" ? tab : tabs.indexOf(tab), card = tabpage._cardBook;
    if (card) {
        card.removeControl(index);
    }
    $invokeSuper.call(this, arguments);
}, doInsertTab:function (tab, index, current) {
    $invokeSuper.call(this, arguments);
    var tabpage = this, card = tabpage._cardBook, tabs = tabpage._tabs;
    if (index == null) {
        index = tabs.indexOf(tab);
    }
    if (card) {
        card.addControl(tab.getControl(), index, current);
    }
}, createDom:function () {
    var tabpage = this, card = tabpage._cardBook, dom = document.createElement("div"), tabbarDom = $invokeSuper.call(this, arguments), tabPlacement = tabpage._tabPlacement;
    if (tabPlacement == "top") {
        dom.appendChild(tabbarDom);
    }
    tabpage._tabbarDom = tabbarDom;
    var controls = [], tabs = tabpage._tabs;
    for (var i = 0, j = tabs.size; i < j; i++) {
        var tab = tabs.get(i);
        controls.push(tab.getControl());
    }
    card.set("controls", controls);
    card.render(dom);
    tabpage.registerInnerControl(card);
    if (tabPlacement == "bottom") {
        dom.appendChild(tabbarDom);
    }
    return dom;
}, refreshDom:function (dom) {
    var tabpage = this, card = tabpage._cardBook, tabbarDom = tabpage._tabbarDom, cardDom = tabpage._cardBook._dom;
    $invokeSuper.call(this, [tabbarDom]);
    $fly(tabbarDom).css("height", "auto");
    if (tabpage._height != null) {
        jQuery(card._dom).height(tabpage.getRealHeight() - $fly(tabbarDom).height()).width(tabpage.getRealWidth());
    }
    var tabs = tabpage._tabs, currentTab = tabpage._currentTab, currentTabIndex = tabs.indexOf(currentTab);
    if (currentTabIndex != -1) {
        card._currentControl = card._controls.get(currentTabIndex);
    }
    card.refreshDom(cardDom);
}, setFocus:function () {
    var dom = this._tabbarDom;
    if (dom) {
        setTimeout(function () {
            try {
                dom.focus();
            }
            catch (e) {
            }
        }, 0);
    }
}});
dorado.widget.CaptionBar = $extend(dorado.widget.Control, {$className:"dorado.widget.CaptionBar", ATTRIBUTES:{className:{defaultValue:"d-caption-bar"}, caption:{}, icon:{}, iconClass:{}, buttons:{innerComponent:"SimpleIconButton", setter:function (attr, value) {
    var bar = this, oldValue = bar._buttons;
    if (oldValue) {
        bar.clearButtons();
    }
    if (!bar._buttons) {
        bar._buttons = new dorado.util.KeyedArray(function (value) {
            return value._id;
        });
    }
    if (value instanceof Array) {
        for (var i = 0, j = value.length; i < j; i++) {
            bar._buttons.insert(value[i]);
        }
    } else {
        if (value instanceof dorado.util.KeyedArray) {
            for (var i = 0, j = value.size; i < j; i++) {
                bar._buttons.insert(value.get(i));
            }
        }
    }
}}, height:{independent:true, readOnly:true}}, addButton:function (button, index) {
    var bar = this, buttons = bar._buttons, doms = bar._doms, refBtn, dom = bar._dom;
    if (!bar._buttons) {
        buttons = bar._buttons = new dorado.util.KeyedArray(function (value) {
            return value._id;
        });
    }
    if (index == null) {
        buttons.insert(button);
    } else {
        if (typeof index == "number") {
            refBtn = buttons.get(index);
            buttons.insert(button, index);
        } else {
            if (typeof index == "string") {
                refBtn = buttons.get(index);
                if (!refBtn) {
                    buttons.insert(button);
                } else {
                    buttons.insert(button, "before", refBtn);
                }
            }
        }
    }
    bar.registerInnerControl(button);
    if (dom) {
        if (!doms.buttonGroup) {
            bar._createButtonGroup();
        }
        button.render(doms.buttonGroup, refBtn ? refBtn._dom : null);
    }
}, getButton:function (button) {
    var bar = this, buttons = bar._buttons;
    if (buttons && (typeof button == "number" || typeof button == "string")) {
        return buttons.get(button);
    } else {
        return button;
    }
}, removeButton:function (button) {
    var bar = this, buttons = bar._buttons;
    if (typeof button == "string" || typeof button == "number") {
        button = buttons.get(button);
    }
    if (button) {
        bar.unregisterInnerControl(button);
        button.destroy();
        buttons.remove(button);
    }
}, clearButtons:function () {
    var bar = this, buttons = bar._buttons;
    if (buttons) {
        for (var i = 0, j = buttons.size; i < j; i++) {
            var button = buttons.get(i);
            bar.unregisterInnerControl(button);
            button.destroy();
            buttons.removeAt(0);
        }
    }
}, _createIcon:function (dom) {
    var bar = this, doms = bar._doms;
    dom = dom ? dom : bar._dom;
    var icon = document.createElement("div");
    icon.className = "caption-bar-icon";
    $fly(icon).insertBefore(doms.caption);
    doms.icon = icon;
}, _createButtonGroup:function (dom) {
    var bar = this, doms = bar._doms;
    dom = dom ? dom : bar._dom;
    var buttonGroup = document.createElement("div");
    buttonGroup.className = "button-group";
    $fly(dom).prepend(buttonGroup);
    doms.buttonGroup = buttonGroup;
}, createDom:function () {
    var bar = this, buttons = bar._buttons, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:bar._className, content:[{tagName:"div", className:"caption", content:bar._caption, contextKey:"caption"}]}, null, doms);
    bar._doms = doms;
    if (buttons) {
        bar._createButtonGroup(dom);
        for (var i = 0, j = buttons.size; i < j; i++) {
            var button = buttons.get(i);
            bar.registerInnerControl(button);
            button.render(doms.buttonGroup);
        }
    }
    var icon = bar._icon, iconCls = bar._iconClass;
    if (icon || iconCls) {
        bar._createIcon(dom);
        $fly(doms.icon).addClass(iconCls);
        $DomUtils.setBackgroundImage(doms.icon, icon);
    }
    return dom;
}, refreshDom:function () {
    var bar = this, doms = bar._doms;
    $fly(doms.caption).text(bar._caption);
    var icon = bar._icon, iconCls = bar._iconClass;
    if (!icon && !iconCls && doms.icon) {
        $fly(doms.icon).css("display", "none");
    } else {
        $fly(doms.icon).attr("className", "caption-bar-icon").css("display", "");
        if ((icon || iconCls) && !doms.icon) {
            bar._createIcon();
        }
        if (icon) {
            $DomUtils.setBackgroundImage(doms.icon, icon);
        } else {
            if (doms.icon) {
                $fly(doms.icon).css("background-image", "none");
            }
        }
        if (iconCls) {
            $fly(doms.icon).addClass(iconCls);
        }
    }
}});
dorado.MessageBox = {_runStack:[], defaultTitle:"", minWidth:300, maxWidth:800, OK:["ok"], CANCEL:["cancel"], OKCANCEL:["ok", "cancel"], YESNO:["yes", "no"], YESNOCANCEL:["yes", "no", "cancel"], WARNING_ICON:"warning-icon", ERROR_ICON:"error-icon", INFO_ICON:"info-icon", QUESTION_ICON:"question-icon", SINGLE_EDITOR:null, buttonText:{ok:"dorado.baseWidget.MessageBoxButtonOK", cancel:"dorado.baseWidget.MessageBoxButtonCancel", yes:"dorado.baseWidget.MessageBoxButtonYes", no:"dorado.baseWidget.MessageBoxButtonNo"}, onButtonClick:function (buttonIndex) {
    var buttonId;
    if (dorado.MessageBox._runStack.length > 0) {
        var config = dorado.MessageBox._runStack[0];
        if (buttonIndex == "close") {
            if (config.closeAction) {
                buttonId = config.closeAction;
            } else {
                buttonIndex = config.buttons[config.buttons.length - 1];
            }
        }
        if (typeof config.detailCallback == "function" || typeof config.callback == "function") {
            if (!buttonId) {
                buttonId = config.buttons[buttonIndex];
            }
            var text = null;
            if (config.editor != "none") {
                if (buttonId != "ok") {
                    text = "";
                } else {
                    switch (config.editor) {
                      case "single":
                        text = dorado.MessageBox.SINGLE_EDITOR.get("value");
                        break;
                      case "multiple":
                        text = dorado.MessageBox.TEXTAREA.get("value");
                        break;
                    }
                }
            }
            if (typeof config.callback == "function" && (buttonId == "yes" || buttonId == "ok")) {
                config.callback.apply(null, [text]);
            }
            if (typeof config.detailCallback == "function") {
                config.detailCallback.apply(null, [buttonId, text]);
            }
        }
        dorado.MessageBox._runStack.splice(0, 1);
    }
    dorado.MessageBox._dialog.hide();
}, getDialog:function () {
    if (!dorado.MessageBox._dialog) {
        dorado.MessageBox.defaultTitle = $resource("dorado.baseWidget.MessageBoxDefaultTitle");
        dorado.MessageBox._dialog = new dorado.widget.Dialog({focusAfterShow:false, anchorTarget:window, align:"center", vAlign:"center", width:dorado.MessageBox.maxWidth, resizeable:false, exClassName:"d-message-box", modal:true, modalType:$setting["widget.messageBox.defaultModalType"] || "transparent", closeAction:"hide", buttons:[{width:60, listener:{onClick:function () {
            dorado.MessageBox.onButtonClick(0);
        }}}, {width:60, listener:{onClick:function () {
            dorado.MessageBox.onButtonClick(1);
        }}}, {width:60, listener:{onClick:function () {
            dorado.MessageBox.onButtonClick(2);
        }}}]});
        dorado.MessageBox._dialog.doOnAttachToDocument = function () {
            var dialog = this, dom = dialog.getContentContainer(), doms = dialog._doms;
            dorado.widget.Dialog.prototype.doOnAttachToDocument.apply(dialog, []);
            if (dom) {
                var lastIcon = dorado.MessageBox._lastIcon || "";
                dom.appendChild($DomUtils.xCreateElement({tagName:"div", className:"msg-content", contextKey:"msgContent", content:[{tagName:"span", className:"msg-icon " + lastIcon, contextKey:"msgIcon"}, {tagName:"span", className:"msg-text", contextKey:"msgText", content:dorado.MessageBox._lastText}]}, null, doms));
                var editorWrap = $DomUtils.xCreateElement({tagName:"div", className:"editor-wrap"});
                doms.editorWrap = editorWrap;
                var editor = new dorado.widget.TextEditor();
                editor.render(editorWrap);
                $fly(editor._dom).css("display", "none");
                dorado.MessageBox.SINGLE_EDITOR = editor;
                dialog.registerInnerControl(editor);
                dom.appendChild(editorWrap);
                var textareaWrap = $DomUtils.xCreateElement({tagName:"div", className:"textarea-wrap"});
                doms.textareaWrap = textareaWrap;
                var textarea = new dorado.widget.TextArea();
                textarea.render(textareaWrap);
                $fly(textarea._dom).css("display", "none");
                dorado.MessageBox.TEXTAREA = textarea;
                dialog.registerInnerControl(textarea);
                dom.appendChild(textareaWrap);
                dorado.MessageBox.updateText(dorado.MessageBox._lastText, dorado.MessageBox._lastIcon, dorado.MessageBox._lastEditor, dorado.MessageBox._lastValue);
            }
            dialog.addListener("beforeShow", function (dialog) {
                var dom = dialog._dom;
                $fly(dom).width(dorado.MessageBox.maxWidth);
                var doms = dialog._doms, contentWidth = $fly(doms.msgText).outerWidth(true) + $fly(doms.msgIcon).outerWidth(true);
                if (contentWidth < dorado.MessageBox.minWidth) {
                    contentWidth = dorado.MessageBox.minWidth;
                } else {
                    if (contentWidth > dorado.MessageBox.maxWidth) {
                        contentWidth = dorado.MessageBox.maxWidth;
                    }
                }
                var dialogWidth = $fly(dom).width(), panelWidth = $fly(doms.contentPanel).width();
                dialog._width = contentWidth + dialogWidth - panelWidth;
                $fly(dom).width(dialog._width);
                dialog._height = null;
                dialog.onResize();
            });
            dialog.addListener("onShow", function (dialog) {
                var buttons = dialog._buttons, button;
                if (buttons) {
                    button = buttons[0];
                    if (button && button._dom.display != "none") {
                        dorado.widget.setFocusedControl(button);
                    }
                }
            });
            dialog.addListener("beforeHide", function (self, arg) {
                if (dorado.MessageBox._runStack.length > 0) {
                    arg.processDefault = false;
                    dorado.MessageBox.doShow(dorado.MessageBox._runStack[0]);
                }
            });
            dialog.addListener("beforeClose", function (self, arg) {
                dorado.MessageBox.onButtonClick("close");
                arg.processDefault = false;
            });
        };
    }
    return dorado.MessageBox._dialog;
}, alert:function (msg, options) {
    if (typeof options == "function") {
        var callback = options;
        options = {callback:callback};
    } else {
        options = options || {};
    }
    options.message = msg;
    options.buttons = dorado.MessageBox.OK;
    options.closeAction = "ok";
    dorado.MessageBox.show(options);
}, confirm:function (msg, options) {
    if (typeof options == "function") {
        var callback = options;
        options = {callback:callback};
    } else {
        options = options || {};
    }
    options.message = msg;
    options.buttons = dorado.MessageBox.YESNO;
    options.closeAction = "no";
    dorado.MessageBox.show(options);
}, prompt:function (msg, options) {
    if (typeof options == "function") {
        var callback = options;
        options = {callback:callback};
    } else {
        options = options || {};
    }
    options.message = msg;
    options.buttons = dorado.MessageBox.OKCANCEL;
    options.closeAction = "cancel";
    options.editor = "single";
    dorado.MessageBox.show(options);
}, promptMultiline:function (msg, options) {
    if (typeof options == "function") {
        var callback = options;
        options = {callback:callback};
    } else {
        options = options || {};
    }
    options.message = msg;
    options.buttons = dorado.MessageBox.OKCANCEL;
    options.closeAction = "cancel";
    options.editor = "multiple";
    dorado.MessageBox.show(options);
}, resetEditorWidth:function (editor) {
    var dialog = dorado.MessageBox.getDialog(), doms = dialog._doms, width;
    if (editor == "multiple" && dorado.MessageBox.TEXTAREA) {
        width = $fly(doms.textareaWrap).outerWidth();
        dorado.MessageBox.TEXTAREA.set("width", width);
    } else {
        if (editor == "single" && dorado.MessageBox.SINGLE_EDITOR) {
            width = $fly(doms.editorWrap).outerWidth();
            dorado.MessageBox.SINGLE_EDITOR.set("width", width);
        }
    }
}, updateText:function (text, icon, editor, value) {
    var dialog = dorado.MessageBox.getDialog(), doms = dialog._doms;
    dorado.MessageBox._lastText = text;
    dorado.MessageBox._lastIcon = icon;
    dorado.MessageBox._lastEditor = editor;
    dorado.MessageBox._lastValue = value;
    if (!doms) {
        return;
    }
    text += "";
    if (text) {
        text = text.replace(/&/g, "&amp;").replace(/[<>\"\n]/g, function ($1) {
            switch ($1) {
              case "<":
                return "&lt;";
              case ">":
                return "&gt;";
              case "\n":
                return "<br/>";
              case "\"":
                return "&quot;";
            }
        });
    }
    $fly(doms.msgText).html(text || "&nbsp;");
    $fly(doms.msgIcon).attr("className", "msg-icon");
    if (icon) {
        $fly(doms.msgIcon).addClass(icon);
        $fly(doms.msgContent).addClass("msg-content-hasicon");
    } else {
        $fly(doms.msgContent).removeClass("msg-content-hasicon");
    }
    if (dorado.MessageBox.SINGLE_EDITOR) {
        switch (editor) {
          case "none":
            $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "none");
            $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "none");
            break;
          case "single":
            $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "");
            $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "none");
            dorado.MessageBox.SINGLE_EDITOR.set("value", value || "");
            break;
          case "multiple":
            $fly(dorado.MessageBox.SINGLE_EDITOR._dom).css("display", "none");
            $fly(dorado.MessageBox.TEXTAREA._dom).css("display", "");
            dorado.MessageBox.TEXTAREA.set("value", value || "");
            break;
        }
    }
}, show:function (options) {
    dorado.MessageBox._runStack.push(options);
    if (dorado.MessageBox._runStack.length > 1) {
        return;
    }
    dorado.MessageBox.doShow(options);
}, doShow:function (options) {
    options = options || {};
    dorado.widget.setFocusedControl(null);
    var dialog = dorado.MessageBox.getDialog(), msg = options.message, defaultText = options.defaultText, title = options.title || dorado.MessageBox.defaultTitle, icon = options.icon, buttons = options.buttons || [], buttonCount = buttons.length, editor = options.editor || "none", dlgButtons = dialog._buttons;
    dorado.MessageBox.updateText(msg, icon, editor, defaultText);
    dialog.set({caption:title});
    dialog.show({overflowHandler:function (overflowHeight) {
        dialog._height = overflowHeight;
        dialog.onResize();
    }});
    if (editor != "none") {
        dorado.MessageBox.resetEditorWidth(editor);
    }
    for (var i = 0; i < 3; i++) {
        var button = buttons[i];
        if (i >= buttonCount) {
            $fly(dlgButtons[i]._dom).css("display", "none");
        } else {
            var caption;
            if (dorado.MessageBox.buttonText[button]) {
                caption = $resource(dorado.MessageBox.buttonText[button]);
            } else {
                caption = button;
            }
            dlgButtons[i].set("caption", caption);
            $fly(dlgButtons[i]._dom).css("display", "");
        }
    }
}};
(function () {
    var directionReverse = {left:"right", right:"left", top:"bottom", bottom:"top"};
    var mouseEnterfunc = function (event) {
        var panel = event.data.panel;
        if (panel._hidePreviewTimer) {
            clearTimeout(panel._hidePreviewTimer);
        }
    };
    var mouseLeavefunc = function (event) {
        var panel = event.data.panel;
        panel._delayHidePreview();
    };
    var documentMouseDown = function (event) {
        $fly(document).unbind("click", documentMouseDown);
        var panel = event.data.panel;
        panel._closePreview();
    };
    dorado.widget.SplitPanel = $extend(dorado.widget.Control, {$className:"dorado.widget.SplitPanel", ATTRIBUTES:{className:{defaultValue:"d-split-panel"}, direction:{defaultValue:"left"}, maxPosition:{}, minPosition:{}, position:{defaultValue:100}, sideControl:{writeBeforeReady:true, writeOnce:true, innerComponent:""}, mainControl:{writeBeforeReady:true, writeOnce:true, innerComponent:""}, resizeable:{defaultValue:true}, collapsed:{}, collpaseable:{defaultValue:true}, previewable:{defaultValue:false}}, EVENTS:{}, _openPreview:function () {
        var panel = this, dom = panel._dom, doms = panel._doms, direction = panel._direction, sidePanelCss = {}, animConfig, width = $fly(dom).innerWidth(), height = $fly(dom).innerHeight(), collapseBarWidth = $fly(doms.collapseBar).outerWidth(), collapseBarHeight = $fly(doms.collapseBar).outerHeight();
        if (panel._previewOpened) {
            return;
        }
        panel._previewOpened = true;
        switch (direction) {
          case "left":
            animConfig = {left:collapseBarWidth};
            sidePanelCss.left = panel._position * -1;
            break;
          case "top":
            animConfig = {top:collapseBarHeight};
            sidePanelCss.top = panel._position * -1;
            break;
          case "right":
            animConfig = {left:width - panel._position - collapseBarWidth};
            sidePanelCss.left = width;
            break;
          case "bottom":
            animConfig = {top:height - panel._position - collapseBarHeight};
            sidePanelCss.top = height;
            break;
        }
        $fly(doms.sidePanel).css(sidePanelCss).bringToFront().animate(animConfig, {complete:function () {
        }});
        $fly(document).bind("click", {panel:panel}, documentMouseDown);
        $fly([doms.sidePanel, doms.collapseBar]).bind("mouseenter", {panel:panel}, mouseEnterfunc).bind("mouseleave", {panel:panel}, mouseLeavefunc);
    }, _closePreview:function () {
        var panel = this, dom = panel._dom, doms = panel._doms, direction = panel._direction, animConfig, width = $fly(dom).innerWidth(), height = $fly(dom).innerHeight(), collapseBarWidth = $fly(doms.collapseBar).outerWidth(), collapseBarHeight = $fly(doms.collapseBar).outerHeight();
        if (!panel._previewOpened) {
            return;
        }
        panel._previewOpened = false;
        switch (direction) {
          case "left":
            animConfig = {left:panel._position * -1 + collapseBarWidth};
            break;
          case "top":
            animConfig = {top:panel._position * -1 + collapseBarHeight};
            break;
          case "right":
            animConfig = {left:width - collapseBarWidth};
            break;
          case "bottom":
            animConfig = {top:height - collapseBarHeight};
            break;
        }
        $fly(doms.sidePanel).animate(animConfig, {complete:function () {
            $fly(doms.sidePanel).css("z-index", "");
        }});
        $fly([doms.sidePanel, doms.collapseBar]).unbind("mouseenter", mouseEnterfunc).unbind("mouseleave", mouseLeavefunc);
    }, _togglePreview:function () {
        var panel = this;
        if (panel._previewOpened) {
            panel._closePreview();
        } else {
            panel._openPreview();
        }
    }, _delayHidePreview:function () {
        var panel = this;
        if (panel._hidePreviewTimer) {
            clearTimeout(panel._hidePreviewTimer);
        }
        panel._hidePreviewTimer = setTimeout(function () {
            panel._hidePreviewTimer = null;
            panel._closePreview();
            $fly(document).unbind("click", documentMouseDown);
        }, 500);
    }, _createCollapseBar:function () {
        var panel = this, doms = panel._doms, bar = $DomUtils.xCreateElement({tagName:"div", className:"collapse-bar", contextKey:"collapseBar", content:{tagName:"div", className:"button", contextKey:"collapseBarButton"}}, null, doms);
        jQuery(doms.collapseBar).addClass("collapse-bar-" + panel._direction).addClassOnHover("collapse-bar-hover").click(function (event) {
            panel._togglePreview();
            event.stopImmediatePropagation();
        });
        jQuery(doms.collapseBarButton).click(function (event) {
            panel.doSetCollapsed(false);
            event.stopImmediatePropagation();
        }).addClassOnHover("button-hover");
        $fly(panel._dom).append(doms.collapseBar);
        return bar;
    }, createDom:function () {
        var panel = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:panel._className, content:[{tagName:"div", className:"side-panel", contextKey:"sidePanel"}, {tagName:"div", className:"splitter", content:{tagName:"div", className:"button", contextKey:"button"}, contextKey:"splitter"}, {tagName:"div", className:"main-panel", contextKey:"mainPanel"}]}, null, doms), direction = panel._direction, axis = (direction == "left" || direction == "right") ? "x" : "y";
        panel._doms = doms;
        $DomUtils.disableUserSelection(doms.splitter);
        $fly(doms.splitter).addClass("splitter-" + panel._direction).draggable({addClasses:false, containment:"parent", axis:axis, helper:"clone", iframeFix:true, start:function (event, ui) {
            var helper = ui.helper;
            if (helper) {
                helper.addClass("splitter-dragging").find("> .button").css("display", "none");
            }
        }, stop:function (event, ui) {
            var position = ui.position;
            switch (panel._direction) {
              case "left":
                panel.set("position", position.left);
                break;
              case "right":
                panel.set("position", $fly(dom).width() - position.left);
                break;
              case "top":
                panel.set("position", position.top);
                break;
              case "bottom":
                panel.set("position", $fly(dom).height() - position.top);
                break;
            }
        }});
        $fly(doms.button).click(function () {
            panel.doSetCollapsed(!panel._collapsed);
        });
        $fly(doms.sidePanel).click(function (event) {
            event.stopImmediatePropagation();
        });
        return dom;
    }, doSetCollapsed:function (collapsed, callback, slience) {
        var panel = this, dom = panel._dom, doms = panel._doms;
        if (dom) {
            var width = $fly(dom).width(), height = $fly(dom).height(), direction = panel._direction, left;
            var animConfig;
            if (collapsed) {
                switch (direction) {
                  case "left":
                    animConfig = {left:panel._position * -1};
                    break;
                  case "top":
                    animConfig = {top:panel._position * -1};
                    break;
                  case "right":
                    animConfig = {left:width};
                    break;
                  case "bottom":
                    animConfig = {top:height};
                    break;
                }
                $fly(doms.sidePanel).animate(animConfig, {complete:function () {
                    panel._collapsed = collapsed;
                    panel.refresh();
                    if (typeof callback == "function") {
                        callback.apply(null, []);
                    }
                }});
            } else {
                if (panel._previewOpened) {
                    $fly([doms.sidePanel, doms.collapseBar]).unbind("mouseenter", mouseEnterfunc).unbind("mouseleave", mouseLeavefunc);
                    $fly(document).unbind("click", documentMouseDown);
                    $fly(doms.sidePanel).css("z-index", "");
                    panel._previewOpened = false;
                    panel._collapsed = collapsed;
                    panel.refresh();
                } else {
                    var sidePanelCss = {};
                    switch (direction) {
                      case "left":
                        animConfig = {left:0};
                        sidePanelCss.left = panel._position * -1;
                        break;
                      case "top":
                        animConfig = {top:0};
                        sidePanelCss.top = panel._position * -1;
                        break;
                      case "right":
                        animConfig = {left:width - panel._position};
                        sidePanelCss.left = width;
                        break;
                      case "bottom":
                        animConfig = {top:height - panel._position};
                        sidePanelCss.top = height;
                        break;
                    }
                    $fly(doms.sidePanel).css(sidePanelCss).bringToFront().animate(animConfig, {complete:function () {
                        panel._collapsed = collapsed;
                        panel.refresh();
                        $fly(doms.sidePanel).css("z-index", "");
                        if (typeof callback == "function") {
                            callback.apply(null, []);
                        }
                    }});
                }
            }
        } else {
            panel._collapsed = collapsed;
            panel.refresh();
        }
        if (!slience) {
            if (panel._sideControl instanceof dorado.widget.AbstractPanel) {
                panel._sideControl.set("collapsed", collapsed);
            }
        }
    }, doOnAttachToDocument:function () {
        var panel = this, sideControl = panel._sideControl, mainControl = panel._mainControl, doms = panel._doms;
        if (sideControl) {
            panel.registerInnerControl(sideControl);
            sideControl.render(doms.sidePanel);
        }
        if (mainControl) {
            panel.registerInnerControl(mainControl);
            mainControl.render(doms.mainPanel);
        }
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var panel = this, doms = panel._doms, width = $fly(dom).width(), height = $fly(dom).height(), splitterWidth = $fly(doms.splitter).width(), splitterHeight = $fly(doms.splitter).height(), direction = panel._direction, previewable = panel._previewable && panel._collpaseable, vertical = direction == "top" || direction == "bottom";
        if (panel._collpaseable) {
            if (panel._collapsed) {
                $fly(dom).addClass(panel._className + "-collapsed");
                $fly(doms.splitter).draggable("disable");
            } else {
                $fly(dom).removeClass(panel._className + "-collapsed");
                $fly(doms.splitter).draggable("enable");
            }
        }
        if (panel._collpaseable) {
            $fly(doms.button).css("display", "");
        } else {
            $fly(doms.button).css("display", "none");
        }
        if (panel._resizeable) {
            $fly(doms.splitter).addClass(vertical ? "splitter-v-resizeable" : "splitter-h-resizeable");
        } else {
            $fly(doms.splitter).removeClass("splitter-h-resizeable").removeClass("splitter-v-resizeable");
        }
        var sidePanelStyle, splitterStyle, mainPanelStyle, mainControlStyle, sideControlStyle, collapseBarStyle, collapseBarWidth = 0, collapseBarHeight = 0;
        if (panel._collpaseable && panel._collapsed) {
            if (previewable) {
                if (!doms.collapseBar) {
                    panel._createCollapseBar();
                }
                $fly(doms.collapseBar).css("display", "");
                collapseBarWidth = $fly(doms.collapseBar).outerWidth();
                collapseBarHeight = $fly(doms.collapseBar).outerHeight();
            }
            switch (direction) {
              case "left":
                sidePanelStyle = {left:panel._position * -1, top:0, height:height};
                splitterStyle = {left:collapseBarWidth, top:0};
                mainPanelStyle = {left:splitterWidth + collapseBarWidth, top:0, width:width - splitterWidth - collapseBarWidth, height:height};
                mainControlStyle = {width:width - splitterWidth - collapseBarWidth, height:height};
                collapseBarStyle = {left:0, top:0, height:height};
                sideControlStyle = {width:panel._position, height:height};
                break;
              case "right":
                sidePanelStyle = {left:width, top:0, height:height};
                splitterStyle = {left:width - splitterWidth - collapseBarWidth, top:0};
                mainPanelStyle = {left:0, top:0, width:width - splitterWidth - collapseBarWidth, height:height};
                mainControlStyle = {width:width - splitterWidth - collapseBarWidth, height:height};
                collapseBarStyle = {left:width - collapseBarWidth, top:0, height:height};
                sideControlStyle = {width:panel._position, height:height};
                break;
              case "top":
                sidePanelStyle = {top:panel._position * -1, left:0, width:width};
                splitterStyle = {top:collapseBarHeight, left:0};
                mainPanelStyle = {top:splitterHeight + collapseBarHeight, left:0, width:width, height:height - splitterHeight - collapseBarHeight};
                mainControlStyle = {width:width, height:height - splitterHeight - collapseBarHeight};
                collapseBarStyle = {left:0, top:0, width:width};
                sideControlStyle = {width:width, height:panel._position};
                break;
              case "bottom":
                sidePanelStyle = {top:height, left:0, width:width};
                splitterStyle = {top:height - splitterHeight - collapseBarHeight, left:0};
                mainPanelStyle = {top:0, left:0, width:width, height:height - splitterHeight - collapseBarHeight};
                mainControlStyle = {width:width, height:height - splitterHeight - collapseBarHeight};
                collapseBarStyle = {top:height - collapseBarHeight, left:0, width:width};
                sideControlStyle = {width:width, height:panel._position};
                break;
            }
            if (previewable) {
                var barWidth = collapseBarStyle.width, barHeight = collapseBarStyle.height;
                if (width) {
                    $fly(doms.collapseBar).outerWidth(barWidth);
                    delete collapseBarStyle.width;
                }
                if (height) {
                    $fly(doms.collapseBar).outerHeight(barHeight);
                    delete collapseBarStyle.height;
                }
                $fly(doms.collapseBar).css(collapseBarStyle);
            }
            $fly(doms.sidePanel).css(sidePanelStyle);
            $fly(doms.splitter).css(splitterStyle).removeClass("splitter-" + direction).addClass("splitter-" + directionReverse[direction]);
            $fly(doms.mainPanel).css(mainPanelStyle);
            if (panel._sideControl) {
                panel._sideControl.set(sideControlStyle);
            }
            if (panel._mainControl) {
                panel._mainControl.set(mainControlStyle);
            }
        } else {
            if (previewable) {
                $fly(doms.collapseBar).css("display", "none");
            }
            switch (panel._direction) {
              case "left":
                sidePanelStyle = {left:0, top:0, width:panel._position, height:height};
                sideControlStyle = {width:panel._position, height:height};
                splitterStyle = {left:panel._position, top:0};
                mainPanelStyle = {left:panel._position + splitterWidth, top:0, width:width - panel._position - splitterWidth, height:height};
                mainControlStyle = {width:width - panel._position - splitterWidth, height:height};
                break;
              case "right":
                sidePanelStyle = {left:width - panel._position, top:0, width:panel._position, height:height};
                sideControlStyle = {width:panel._position, height:height};
                splitterStyle = {left:width - panel._position - splitterWidth, top:0};
                mainPanelStyle = {left:0, top:0, width:width - panel._position - splitterWidth, height:height};
                mainControlStyle = {width:width - panel._position - splitterWidth, height:height};
                break;
              case "top":
                sidePanelStyle = {top:0, left:0, width:width, height:panel._position};
                sideControlStyle = {width:width, height:panel._position};
                splitterStyle = {top:panel._position, left:0};
                mainPanelStyle = {top:panel._position + splitterHeight, left:0, width:width, height:height - panel._position - splitterHeight};
                mainControlStyle = {width:width, height:height - panel._position - splitterHeight};
                break;
              case "bottom":
                sidePanelStyle = {top:height - panel._position, left:0, width:width, height:panel._position};
                sideControlStyle = {width:width, height:panel._position};
                splitterStyle = {top:height - panel._position - splitterHeight, left:0};
                mainPanelStyle = {top:0, left:0, width:width, height:height - panel._position - splitterHeight};
                mainControlStyle = {width:width, height:height - panel._position - splitterHeight};
                break;
            }
            $fly(doms.sidePanel).css(sidePanelStyle);
            $fly(doms.mainPanel).css(mainPanelStyle);
            $fly(doms.splitter).css(splitterStyle).removeClass("splitter-" + directionReverse[direction]).addClass("splitter-" + direction);
            if (panel._sideControl) {
                panel._sideControl.set(sideControlStyle);
                panel._sideControl.refresh();
            }
            if (panel._mainControl) {
                panel._mainControl.set(mainControlStyle);
                panel._mainControl.refresh();
            }
        }
        if (panel._resizeable) {
            if (panel._maxPosition != null || panel._minPosition != null) {
                var offset = $fly(dom).offset(), min = panel._minPosition || 50, max, sideMin, sideMax, range;
                if (vertical) {
                    max = panel._maxPosition || height - 50;
                } else {
                    max = panel._maxPosition || width - 50;
                }
                if (panel._direction == "left") {
                    sideMin = offset.left + min;
                    sideMax = offset.left + max;
                } else {
                    if (panel._direction == "right") {
                        sideMin = offset.left + width - max;
                        sideMax = offset.left + width - min;
                    } else {
                        if (panel._direction == "top") {
                            sideMin = offset.top + min;
                            sideMax = offset.top + max;
                        } else {
                            if (panel._direction == "bottom") {
                                sideMin = offset.top + height - max;
                                sideMax = offset.top + height - min;
                            }
                        }
                    }
                }
                if (vertical) {
                    range = [0, sideMin, 0, sideMax];
                } else {
                    range = [sideMin, 0, sideMax, 0];
                }
                $fly(doms.splitter).draggable("option", "containment", range);
            }
        }
    }});
})();
dorado.widget.ProgressBar = $extend(dorado.widget.Control, {$className:"dorado.widget.ProgressBar", selectable:false, ATTRIBUTES:{className:{defaultValue:"d-progress-bar"}, height:{independent:true}, minValue:{defaultValue:0}, maxValue:{defaultValue:100}, showText:{defaultValue:true}, value:{}, textPattern:{defaultValue:"${percent}%"}}, createDom:function () {
    var bar = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:bar._className, content:[{tagName:"span", className:"bar", contextKey:"bar"}, {tagName:"span", className:"msg", contextKey:"msg"}]}, null, doms);
    bar._doms = doms;
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var bar = this, min = bar._minValue, max = bar._maxValue, value = bar._value || 0, doms = bar._doms, percent = value / (max - min), showText = bar._showText, pattern = bar._textPattern || "";
    if (percent >= 0 && percent <= 1) {
        $fly(doms.bar).css("width", percent * 100 + "%");
    }
    if (showText) {
        $fly(doms.msg).text(pattern.replace("${percent}", parseInt(percent * 100, 10)));
    }
}});
dorado.widget.Slider = $extend(dorado.widget.Control, {selectable:false, ATTRIBUTES:{className:{defaultValue:"d-slider"}, orientation:{defaultValue:"horizental"}, minValue:{defaultValue:0}, maxValue:{defaultValue:100}, value:{setter:function (attr, value) {
    var slider = this, result;
    value = slider.getValidValue(value);
    var eventArg = {value:slider._value};
    result = slider.fireEvent("beforeValueChange", slider, eventArg);
    if (eventArg.processDefault === false) {
        return;
    }
    if (!result) {
        slider.refresh();
        return false;
    }
    slider._value = value;
    slider.fireEvent("onValueChange", slider);
}}, precision:{defaultValue:0}, step:{}}, EVENTS:{beforeValueChange:{}, onValueChange:{}}, constructor:function () {
    $invokeSuper.call(this, arguments);
    this._value = this.getValidValue(this._value);
}, createDom:function () {
    var slider = this, dom, doms = {}, orientation = slider._orientation, className = slider._className;
    dom = $DomUtils.xCreateElement({tagName:"div", className:slider._className, content:[{tagName:"div", className:"slider-start", contextKey:"start"}, {tagName:"div", className:"slider-end", contextKey:"end"}, {tagName:"div", className:"slider-body", contextKey:"body"}, {tagName:"div", className:"slider-current", contextKey:"current"}, {tagName:"div", className:"slider-thumb", contextKey:"thumb", style:{position:"absolute"}}]}, null, doms);
    slider._doms = doms;
    var axis = "x";
    if (orientation == "vertical") {
        axis = "y";
        $fly(dom).addClass(className + "-vertical");
    } else {
        $fly(dom).addClass(className + "-horizental");
    }
    var tip = new dorado.widget.Tip({animateType:"none", showDelay:""});
    jQuery(doms.thumb).addClassOnHover("slider-thumb-hover").addClassOnClick("slider-thumb-click").draggable({addClasses:false, containment:"parent", axis:axis, stop:function (event, ui) {
        var helper = ui.helper[0], minValue = slider._minValue, maxValue = slider._maxValue, offset, size, thumbSize;
        if (orientation == "horizental") {
            thumbSize = $fly(doms.thumb).width();
            size = $fly(dom).width() - thumbSize;
            offset = parseInt($fly(helper).css("left"), 10);
        } else {
            thumbSize = $fly(doms.thumb).height();
            size = $fly(dom).height() - thumbSize;
            offset = parseInt($fly(helper).css("top"), 10);
        }
        slider.set("value", (maxValue - minValue) * offset / size);
        tip.hide();
    }, drag:function (event, ui) {
        var helper = ui.helper[0], minValue = slider._minValue, maxValue = slider._maxValue, offset, size, thumbSize;
        if (orientation == "horizental") {
            thumbSize = $fly(doms.thumb).width();
            size = $fly(dom).width() - thumbSize;
            offset = parseInt($fly(helper).css("left"), 10);
            $fly(doms.current).css("width", offset + thumbSize / 2);
        } else {
            thumbSize = $fly(doms.thumb).height();
            size = $fly(dom).height() - thumbSize;
            offset = parseInt($fly(helper).css("top"), 10);
            $fly(doms.current).css("height", offset + thumbSize / 2);
        }
        tip.set("text", slider.getValidValue((maxValue - minValue) * offset / size));
        tip.refresh();
        if (!tip._dom) {
            return;
        }
        if (orientation == "horizental") {
            $DomUtils.dockAround(tip._dom, slider._doms.thumb, {align:"center", vAlign:"top", positionOffset:[0, -10]});
        } else {
            $DomUtils.dockAround(tip._dom, slider._doms.thumb, {align:"right", vAlign:"center", positionOffset:[10, 0]});
        }
    }, start:function (event, ui) {
        tip.set("text", slider._value);
        tip.show({anchorTarget:ui.helper[0], align:"center", vAlign:"top", showDelay:0});
    }});
    return dom;
}, getValidValue:function (value) {
    function formatDecimal(value, precision) {
        if (precision == null) {
            precision = 0;
        }
        var result = value * Math.pow(10, precision);
        if (result - Math.floor(result) >= 0.5) {
            return (Math.floor(result) + 1) / Math.pow(10, precision);
        } else {
            return Math.floor(result) / Math.pow(10, precision);
        }
    }
    var slider = this, step = slider._step, result, minValue = slider._minValue, maxValue = slider._maxValue;
    if (value != undefined) {
        result = formatDecimal(value, slider._precision);
        if (step != null) {
            var total = (maxValue - minValue) / step, left = Math.floor((result - minValue) / step), right = left + 1;
            if (right > total) {
                result = maxValue;
            } else {
                if (Math.abs(minValue + step * right - result) > Math.abs(minValue + step * left - result)) {
                    result = minValue + step * left;
                } else {
                    result = minValue + step * right;
                }
            }
        }
        return result;
    }
    return null;
}, onClick:function (event) {
    var target = event.target || event.srcElement;
    if (target.className.indexOf("slider-thumb") != -1) {
        return;
    }
    var slider = this, dom = slider._dom, doms = slider._doms, pageX = event.pageX, pageY = event.pageY, position = $fly(dom).offset(), offset, size, thumbSize;
    if (slider._orientation == "horizental") {
        size = $fly(dom).innerWidth();
        thumbSize = $fly(doms.thumb).outerWidth();
        offset = pageX - position.left;
    } else {
        size = $fly(dom).innerHeight();
        thumbSize = $fly(doms.thumb).outerHeight();
        offset = pageY - position.top;
    }
    var percent = (offset - thumbSize / 2) / (size - thumbSize);
    if (percent < 0) {
        percent = 0;
    } else {
        if (percent > 1) {
            percent = 1;
        }
    }
    slider.set("value", (slider._maxValue - slider._minValue) * percent);
}, onResize:function () {
    this.refresh();
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var slider = this, doms = slider._doms, orientation = slider._orientation, maxValue = slider._maxValue, minValue = slider._minValue, value = slider._value, thumbSize, step = slider._step, handleIncrease = (step != null), stepCount;
    if (value == null) {
        value = minValue;
    }
    if (handleIncrease) {
        stepCount = (maxValue - minValue) / step;
        if (stepCount - Math.floor(stepCount) > 1e-11) {
            handleIncrease = false;
        }
    }
    if (orientation == "vertical") {
        thumbSize = $fly(doms.thumb).height();
        var height, startHeight, endHeight;
        height = $fly(dom).innerHeight();
        startHeight = $fly(doms.start).innerHeight();
        endHeight = $fly(doms.end).innerHeight();
        $fly(doms.body).height(height - startHeight - endHeight);
        $fly(doms.thumb).css("top", (height - thumbSize) * value / (maxValue - minValue));
        $fly(doms.current).css("height", (height - thumbSize) * value / (maxValue - minValue) + thumbSize / 2);
        if (handleIncrease) {
            $fly(doms.thumb).draggable("option", "grid", [0, (height - thumbSize) / stepCount]);
        }
    } else {
        thumbSize = $fly(doms.thumb).width();
        var width = $fly(dom).innerWidth();
        $fly(doms.thumb).css("left", (width - thumbSize) * value / (maxValue - minValue));
        $fly(doms.current).css("width", (width - thumbSize) * value / (maxValue - minValue) + thumbSize / 2);
        if (handleIncrease) {
            $fly(doms.thumb).draggable("option", "grid", [(width - thumbSize) / stepCount, 0]);
        }
    }
}});
(function () {
    var CONST_MOUSE_POS_ADJ_X = 5, CONST_MOUSE_POS_ADJ_Y = 15, ANCHOR_OFFSET_ADJ_HORIZENTAL = 5, ANCHOR_OFFSET_ADJ_VERTICAL = 5, CONST_ELEMENT_POS_ADJ = 3, TOOLTIP_KEY = "dorado.tooltip", DOMS_KEY = "dorado.tip.doms";
    dorado.widget.Tip = $extend([dorado.widget.Control, dorado.widget.FloatControl], {$className:"dorado.widget.Tip", focusable:true, ATTRIBUTES:{className:{defaultValue:"d-tip"}, visible:{defaultValue:false}, shadowMode:{defaultValue:"drop", skipRefresh:true}, animateType:{defaultValue:"fade", skipRefresh:true}, caption:{}, text:{}, closeable:{}, arrowDirection:{}, arrowAlign:{defaultValue:"center"}, arrowOffset:{}, showDuration:{}}, createDom:function () {
        var tip = this, dom, doms = {};
        if (dorado.Browser.msie) {
            dom = $DomUtils.xCreateElement({tagName:"div", className:tip._className, content:[{tagName:"div", className:"tip-tl", content:{tagName:"div", className:"tip-tr"}}, {tagName:"div", className:"tip-cl", content:{tagName:"div", className:"tip-cr", content:{tagName:"div", className:"tip-cm", contextKey:"tipCenter", content:{tagName:"div", contextKey:"tipContent"}}}}, {tagName:"div", className:"tip-bl", content:{tagName:"div", className:"tip-br"}}]}, null, doms);
        } else {
            dom = $DomUtils.xCreateElement({tagName:"div", className:tip._className, content:{tagName:"div", className:"tip-cm", contextKey:"tipCenter", content:{tagName:"div", contextKey:"tipContent"}}}, null, doms);
        }
        tip._doms = doms;
        $fly(dom).hover(function () {
            if (tip._showDurationTimer) {
                clearTimeout(tip._showDurationTimer);
                tip._showDurationTimer = null;
            }
        }, function () {
            if (tip._showDuration) {
                tip._showDurationTimer = setTimeout(function () {
                    tip.hide();
                    tip._showDurationTimer = null;
                }, tip._showDuration * 1000);
            }
        });
        return dom;
    }, doAfterShow:function () {
        var tip = this;
        $invokeSuper.call(tip, arguments);
        if (tip._showDuration) {
            tip._showDurationTimer = setTimeout(function () {
                tip.hide();
                tip._showDurationTimer = null;
            }, tip._showDuration * 1000);
        }
    }, doClose:function () {
        this.hide();
    }, getShowPosition:function (options) {
        var tip = this, arrowDirection = tip._arrowDirection, doms = tip._doms;
        if (arrowDirection && !options.positionOffset) {
            var arrowAlign = tip._arrowAlign;
            if (arrowAlign) {
                if (arrowDirection == "left") {
                    options.positionOffset = [doms.arrow.offsetWidth, 0];
                } else {
                    if (arrowDirection == "right") {
                        options.positionOffset = [-1 * doms.arrow.offsetWidth, 0];
                    } else {
                        if (arrowDirection == "top") {
                            options.positionOffset = [0, doms.arrow.offsetHeight];
                        } else {
                            options.positionOffset = [0, -1 * doms.arrow.offsetHeight];
                        }
                    }
                }
            }
        }
        return $invokeSuper.call(this, arguments);
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var tip = this, text = (tip._text == undefined) ? "&nbsp;&nbsp;" : tip._text, doms = tip._doms, arrowDirection = tip._arrowDirection, cls = tip._className;
        $fly(dom).attr("className", cls + " d-shadow-drop");
        $fly(doms.tipContent).html(text);
        if (arrowDirection && arrowDirection != "none") {
            if (doms.arrow == null) {
                var arrowEl = document.createElement("div");
                arrowEl.className = "arrow";
                $fly(dom).append(arrowEl);
                doms.arrow = arrowEl;
            }
            $fly(dom).addClass(cls + "-arrow-" + arrowDirection);
        } else {
            $fly(dom).removeClass(cls + "-arrow-top").removeClass(cls + "-arrow-bottom").removeClass(cls + "-arrow-left").removeClass(cls + "-arrow-right");
        }
        var captionDom = doms.caption;
        if (tip._caption) {
            if (captionDom == null) {
                doms.caption = captionDom = document.createElement("div");
                captionDom.className = "caption";
                $fly(doms.tipCenter).prepend(captionDom);
                $fly(captionDom).html(tip._caption);
            } else {
                $fly(captionDom).css("display", "").html(tip._caption);
            }
        } else {
            if (captionDom != null) {
                $fly(captionDom).css("display", "none");
            }
        }
        if (tip._closeable) {
            if (doms.close == null) {
                var closeEl = document.createElement("div");
                closeEl.className = "close";
                $fly(dom).append(closeEl);
                doms.close = closeEl;
                jQuery(closeEl).click(function () {
                    tip.doClose(this);
                }).addClassOnHover("close-hover").addClassOnClick("close-click");
            } else {
                $fly(doms.close).css("display", "");
            }
        } else {
            if (doms.close) {
                $fly(doms.close).css("display", "none");
            }
        }
        $fly(doms.arrow).css({left:"", top:""});
        if (arrowDirection && !tip._trackMouse) {
            var arrowAlign = tip._arrowAlign, arrowOffset = tip._arrowOffset || 0;
            if (arrowAlign) {
                if (arrowDirection == "left" || arrowDirection == "right") {
                    if (arrowAlign == "center") {
                        $fly(doms.arrow).css("top", (dom.offsetHeight - doms.arrow.offsetHeight) / 2 + arrowOffset);
                    } else {
                        if (arrowAlign == "top") {
                            $fly(doms.arrow).css("top", ANCHOR_OFFSET_ADJ_VERTICAL + arrowOffset);
                        } else {
                            if (arrowAlign == "bottom") {
                                $fly(doms.arrow).css("top", dom.offsetHeight - doms.arrow.offsetHeight - ANCHOR_OFFSET_ADJ_VERTICAL + arrowOffset);
                            }
                        }
                    }
                } else {
                    if (arrowAlign == "center") {
                        $fly(doms.arrow).css("left", (dom.offsetWidth - doms.arrow.offsetWidth) / 2 + arrowOffset);
                    } else {
                        if (arrowAlign == "left") {
                            $fly(doms.arrow).css("left", ANCHOR_OFFSET_ADJ_HORIZENTAL + arrowOffset);
                        } else {
                            if (arrowAlign == "right") {
                                $fly(doms.arrow).css("left", dom.offsetWidth - doms.arrow.offsetWidth - ANCHOR_OFFSET_ADJ_HORIZENTAL + arrowOffset);
                            }
                        }
                    }
                }
            }
        }
    }});
    dorado.widget.NotifyTip = $extend(dorado.widget.Tip, {$className:"dorado.widget.NotifyTip", ATTRIBUTES:{width:{defaultValue:300}, closeable:{defaultValue:true}, icon:{}, iconClass:{}, showDuration:{defaultValue:5}}, getShowPosition:function () {
        return dorado.widget.NotifyTipManager.getAvialablePosition(this);
    }, doAfterHide:function () {
        $invokeSuper.call(this, arguments);
        dorado.NotifyTipPool.returnObject(this);
    }});
    dorado.NotifyTipPool = new dorado.util.ObjectPool({makeObject:function () {
        return new dorado.widget.NotifyTip();
    }});
    var getRegionOffsets = function (region1, region2) {
        return {top:Math.max(region1["top"], region2["top"]), right:Math.min(region1["right"], region2["right"]), bottom:Math.min(region1["bottom"], region2["bottom"]), left:Math.max(region1["left"], region2["left"])};
    };
    var intersect = function (element1, element2) {
        var region1 = $fly(element1).region(), region2;
        if (element2.nodeType) {
            region2 = $fly(element2).region();
        } else {
            region2 = element2;
        }
        var offset = getRegionOffsets(region1, region2);
        return offset["bottom"] >= offset["top"] && offset["right"] >= offset["left"];
    };
    dorado.widget.NotifyTipManager = {offsetLeft:-10, offsetTop:10, padding:10, notifyWidth:300, notify:function (msg, options) {
        var tip = dorado.NotifyTipPool.borrowObject();
        tip._text = msg;
        tip._caption = "dorado 7";
        tip.show({anchorTarget:"body", align:"innerright", vAlign:"innertop", offsetLeft:-10, offsetTop:10});
    }, nextRegion:function (refTip, tip) {
        var left, top;
        left = parseInt($fly(refTip._dom).css("left"), 10);
        top = $fly(refTip._dom).outerHeight() + parseInt($fly(refTip._dom).css("top"), 10) + this.padding;
        var dom = tip._dom, width = dom.offsetWidth, height = dom.offsetHeight, docHeight = $fly(window).height();
        if (top + height > docHeight) {
            left = left - this.notifyWidth - this.padding;
            top = this.offsetTop;
        }
        return {left:left, top:top, bottom:top + height, right:left + width};
    }, avialable:function (tip, region) {
        var passed = true, activePool = dorado.NotifyTipPool._activePool;
        for (var k = 0, l = activePool.length; k < l; k++) {
            if (activePool[k] != tip) {
                var intersected = intersect(activePool[k]._dom, region);
                if (intersected) {
                    passed = false;
                }
            }
        }
        return passed;
    }, getAvialablePosition:function (tip) {
        var docWidth = $fly(window).width(), dom = tip._dom, width = dom.offsetWidth, height = dom.offsetHeight, left, top, region;
        left = docWidth + this.offsetLeft - width;
        top = this.offsetTop;
        region = {left:left, top:top, bottom:top + height, right:left + width};
        if (this.avialable(tip, region)) {
            dorado.NotifyTipPool._activePool.remove(tip);
            dorado.NotifyTipPool._activePool.unshift(tip);
            $fly(dom).css({left:left, top:top});
            return {left:left, top:top};
        }
        if (dorado.NotifyTipPool.getNumActive() > 1) {
            var activePool = dorado.NotifyTipPool._activePool;
            for (var i = 0, j = activePool.length; i < j; i++) {
                var curTip = activePool[i];
                if (curTip != tip) {
                    region = this.nextRegion(curTip, tip);
                    if (this.avialable(tip, region)) {
                        dorado.NotifyTipPool._activePool.remove(tip);
                        dorado.NotifyTipPool._activePool.insert(tip, dorado.NotifyTipPool._activePool.indexOf(curTip) + 1);
                        $fly(tip._dom).css({left:region.left, top:region.top});
                        return {left:region.left, top:region.top};
                    }
                }
            }
        }
    }};
    var elementMouseEnter = function (event) {
        var element = this, tip = dorado.TipManager.getTip(element);
        if (tip._text && !tip._visible) {
            dorado.TipManager.showTip(element, tip._showDelay || 0, event);
        }
        event.stopImmediatePropagation();
    };
    var elementMouseMove = function (event) {
        var element = this, tip = dorado.TipManager.getTip(element);
        if (tip) {
            if (tip._showTimer) {
                tip._latestEvent = event;
                event.stopImmediatePropagation();
            }
            if (tip._trackMouse && tip._dom && ($fly(tip._dom).css("display") != "none")) {
                tip._updatePosition(event);
            }
        }
    };
    var elementMouseLeave = function () {
        var element = this, tip = dorado.TipManager.getTip(element);
        if (tip) {
            if (tip._showTimer) {
                clearTimeout(tip._showTimer);
                tip._showTimer = null;
            }
            if (tip._autoHide) {
                dorado.TipManager.hideTip(tip, tip._hideDelay || 0);
            }
        }
    };
    var tipCanUsePool = [];
    dorado.widget.ToolTip = $extend(dorado.widget.Tip, {$className:"dorado.widget.ToolTip", ATTRIBUTES:{mouseOffset:{defaultValue:[CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y]}, anchorToTarget:{defaultValue:false, skipRefresh:true, setter:function (attr, value) {
        this._anchorToTarget = value;
        if (this._ready) {
            if (value === false) {
                this.unbindTarget();
            } else {
                this.bindTarget();
            }
        }
    }}, anchorTarget:{skipRefresh:true, setter:function (attr, value) {
        var oldValue = this._anchorTarget;
        if (oldValue && this._anchorTargetBinded) {
            this.unbindTarget();
        }
        this._anchorTarget = value;
        if (this._ready && value) {
            this.bindTarget();
        }
    }}, showDelay:{skipRefresh:true, defaultValue:500}, hideDelay:{skipRefresh:true, defaultValue:300}, autoHide:{defaultValue:true}, trackMouse:{}}, getShowPosition:function (options) {
        var tip = this, dom = tip.getDom(), event = tip._latestEvent || options.event;
        if (tip._anchorToTarget === true) {
            return $invokeSuper.call(this, arguments);
        } else {
            if (tip._anchorToTarget !== true && event) {
                var mouseOffset = tip._mouseOffset || [CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y];
                $DomUtils.locateIn(dom, {position:{left:event.pageX + mouseOffset[0], top:event.pageY + mouseOffset[1]}});
                tip._latestEvent = null;
            }
        }
        return {left:$fly(dom).left(), top:$fly(dom).top()};
    }, doClose:function (closeEl) {
        var target = jQuery.data(closeEl.parentNode, TOOLTIP_KEY);
        target.hide();
    }, getDom:function () {
        var dom = this._dom;
        if (!dom) {
            dom = tipCanUsePool.pop();
            if (dom) {
                this._doms = jQuery.data(dom, DOMS_KEY);
                this._dom = dom;
                if (this._visible) {
                    $fly(dom).css({display:"", visibility:"hidden", left:-99999, top:-99999});
                } else {
                    $fly(dom).css({display:"none"});
                }
                jQuery.data(dom, TOOLTIP_KEY, this);
                return dom;
            } else {
                dom = $invokeSuper.call(this, arguments);
                document.body.appendChild(dom);
                jQuery.data(dom, TOOLTIP_KEY, this);
                return dom;
            }
        } else {
            return dom;
        }
    }, _updatePosition:function (event) {
        var tip = this;
        if (event) {
            var mouseOffset = tip._mouseOffset || [CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y];
            $DomUtils.locateIn(tip._dom, {position:{left:event.pageX + mouseOffset[0], top:event.pageY + mouseOffset[1]}});
        }
    }, bindTarget:function () {
        var element = this._anchorTarget;
        if (element && !this._anchorTargetBinded) {
            $fly(element).hover(elementMouseEnter, elementMouseLeave).mousemove(elementMouseMove);
            this._anchorTargetBinded = true;
        }
    }, unbindTarget:function () {
        var element = this._anchorTarget;
        if (element && this._anchorTargetBinded) {
            $fly(element).unbind("mousemove", elementMouseMove).unbind("mouseenter", elementMouseEnter).unbind("mouseleave", elementMouseLeave);
            this._anchorTargetBinded = false;
        }
    }, doShow:function () {
        $invokeSuper.call(this, arguments);
    }, hide:function () {
        var tip = this;
        if (tip._showTimer) {
            clearTimeout(tip._showTimer);
            tip._showTimer = null;
            tip._visible = false;
            return;
        }
        $invokeSuper.call(this, arguments);
    }, show:function () {
        var tip = this;
        if (tip._hideTimer) {
            clearTimeout(tip._hideTimer);
            tip._hideTimer = null;
            tip._visible = true;
            return;
        }
        $invokeSuper.call(this, arguments);
    }, doAfterHide:function () {
        var tip = this;
        $invokeSuper.call(tip, arguments);
        tipCanUsePool.push(tip._dom);
        jQuery.data(tip._dom, DOMS_KEY, tip._doms);
        jQuery.data(tip._dom, TOOLTIP_KEY, null);
        tip._rendered = false;
        tip._dom = null;
        tip._doms = null;
    }});
    dorado.TipManager = {_previousTip:null, hasTip:function (element) {
        return !!dorado.TipManager.getTip(element);
    }, getTip:function (element) {
        var result;
        if (element) {
            result = jQuery.data(element, TOOLTIP_KEY);
        }
        return result;
    }, allocTip:function (element, options) {
        var result;
        options = options || {};
        options.anchorTarget = element;
        result = new dorado.widget.ToolTip(options);
        result.bindTarget();
        jQuery.data(element, TOOLTIP_KEY, result);
    }, initTip:function (element, options) {
        var manager = this;
        if (element) {
            if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
                element = element._dom;
                if (!element) {
                    return;
                }
            }
            if (!options) {
                manager.deleteTip(element);
            } else {
                if (manager.hasTip(element)) {
                    manager.updateTip(element, options);
                } else {
                    manager.allocTip(element, options);
                }
            }
        }
    }, updateTip:function (element, options) {
        if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
            element = element._dom;
            if (!element) {
                return;
            }
        }
        var tip = dorado.TipManager.getTip(element);
        tip.set(options, options);
    }, deleteTip:function (element) {
        if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
            element = element._dom;
            if (!element) {
                return;
            }
        }
        var tip = dorado.TipManager.getTip(element);
        if (tip) {
            tip.unbindTarget();
            jQuery.data(element, TOOLTIP_KEY, null);
        }
    }, showTip:function (element, delay, event) {
        var manager = this, tip = dorado.TipManager.getTip(element);
        if (tip._autoHide === false && !tip._visible) {
            if (delay) {
                tip._showTimer = setTimeout(function () {
                    tip.show({element:element, event:event});
                    tip._showTimer = null;
                }, delay);
            } else {
                tip.show({element:element, event:event});
            }
        } else {
            var oldPrevTip = manager._previousTip;
            if (oldPrevTip && oldPrevTip != tip) {
                oldPrevTip.hide();
            }
            if (delay) {
                tip._showTimer = setTimeout(function () {
                    tip.show({element:element, event:event});
                    tip._showTimer = null;
                }, delay);
            } else {
                tip.show({element:element, event:event});
            }
            manager._previousTip = tip;
        }
        return tip;
    }, hideTip:function (tip, delay) {
        var manager = this;
        if (tip) {
            if (manager._previousTip == tip) {
                manager._previousTip = null;
            }
            if (delay) {
                tip._hideTimer = setTimeout(function () {
                    tip.hide();
                    tip._hideTimer = null;
                }, delay);
            } else {
                tip.hide();
            }
        }
    }};
})();
dorado.widget.Section = $extend(dorado.widget.Control, {$className:"dorado.widget.Section", ATTRIBUTES:{className:{defaultValue:"d-section"}, caption:{skipRefresh:true, path:"_captionBar.caption"}, icon:{skipRefresh:true, path:"_captionBar.icon"}, iconClass:{skipRefresh:true, path:"_captionBar.iconClass"}, disabled:{setter:function (attr, value) {
    this._disabled = value;
    if (this._parent) {
        if (this._parent._activeSection == this) {
            this._parent.changeToAvialableSection();
            this._parent.refresh();
        }
    }
}}, visible:{setter:function (attr, value) {
    this._visible = value;
    if (this._rendered) {
        $fly(this._dom).css("display", value ? "" : "none");
    }
    if (this._parent) {
        if (this._parent._activeSection == this) {
            this._parent.changeToAvialableSection();
        }
        this._parent.refresh();
    }
}}, expandable:{defaultValue:true, setter:function (attr, value) {
    this._expandable = value;
    if (this._parent) {
        if (this._parent._activeSection == this) {
            this._parent.changeToAvialableSection();
            this._parent.refresh();
        }
    }
}}, control:{componentReference:true, innerComponent:"", setter:function (attr, value) {
    var oldValue = this._value;
    if (oldValue) {
        this.unregisterInnerControl(oldValue);
        oldValue.destroy();
    }
    this._control = value;
}}, userData:{}}, EVENTS:{onCaptionClick:{}}, createDom:function () {
    var section = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:"d-section", content:[{tagName:"div", className:"container", contextKey:"container"}]}, null, doms);
    section._doms = doms;
    jQuery(dom).addClassOnHover("hover-section");
    var captionBar = section._captionBar = new dorado.widget.CaptionBar({caption:section._caption, className:"d-section-caption-bar", icon:section._icon, iconClass:section._iconClass});
    captionBar.render(dom, doms.container);
    section.registerInnerControl(captionBar);
    doms.captionBar = captionBar._dom;
    return dom;
}, doRenderControl:function () {
    var section = this, doms = section._doms, control = section._control;
    if (control) {
        section.registerInnerControl(control);
        control.render(doms.container);
        control.set("visible", true);
    }
}});
dorado.widget.Accordion = $extend(dorado.widget.Control, {$className:"dorado.widget.Accordion", ATTRIBUTES:{className:{defaultValue:"d-accordion"}, sections:{innerComponent:"Section", setter:function (attr, value) {
    var accordion = this, oldValue = accordion._sections;
    if (oldValue) {
        accordion.clearSections();
    }
    if (typeof value == "object" && value.constructor == Array.prototype.constructor) {
        for (var i = 0, j = value.length; i < j; i++) {
            accordion.insertSection(value[i]);
        }
    }
}}, height:{defaultValue:400}, activeSection:{setter:function (attr, value) {
    this.doSetActiveSection(value);
}}}, EVENTS:{beforeActiveSectionChange:{}, onActiveSectionChange:{}}, doSetActiveSection:function (section, animate) {
    var accordion = this, lastActive = accordion._activeSection, newActive = section;
    if (lastActive == newActive) {
        return;
    }
    var eventArg = {activeSection:section, oldActiveSection:lastActive};
    accordion.fireEvent("beforeActiveSectionChange", accordion, eventArg);
    if (eventArg.processDefault === false) {
        return;
    }
    if (!accordion._rendered) {
        accordion._activeSection = newActive;
        return;
    }
    if (animate) {
        var lastActiveCt = lastActive._doms.container, oldHeight = $fly(lastActiveCt).height();
        $fly(lastActiveCt).dockable("bottom", true);
        accordion._sliding = true;
        $fly(newActive._doms.container).safeSlideIn({direction:"t2b", complete:function () {
            $fly(lastActiveCt).height(oldHeight).css("display", "").undockable(true);
            accordion._activeSection = newActive;
            accordion.refresh();
            accordion.fireEvent("onActiveSectionChange", accordion, newActive);
            accordion._sliding = false;
        }, step:function (now, animate) {
            $fly(lastActiveCt).height(dorado.Browser.chrome ? Math.ceil(oldHeight - now.height) : (oldHeight - now.height));
        }});
    } else {
        accordion._activeSection = newActive;
        accordion.refresh();
        accordion.fireEvent("onActiveSectionChange", accordion, newActive);
    }
}, changeToAvialableSection:function () {
    var accordion = this, sections = accordion._sections;
    if (sections) {
        var startIndex = -1, activeSection = accordion._activeSection, i, j, section;
        if (activeSection) {
            startIndex = sections.indexOf(activeSection);
        }
        for (i = startIndex + 1, j = sections.length; i < j; i++) {
            section = sections[i];
            if (section && section._visible && section._expandable && !section._disabled) {
                accordion.doSetActiveSection(section);
                return section;
            }
        }
        for (i = startIndex - 1; i >= 0; i--) {
            section = sections[i];
            if (section && section._visible && section._expandable && !section._disabled) {
                accordion.doSetActiveSection(section);
                return section;
            }
        }
    }
    return null;
}, getVisibleSectionCount:function () {
    var accordion = this, sections = accordion._sections, result = 0;
    if (sections) {
        var section;
        for (var i = 0, j = sections.length; i < j; i++) {
            section = sections[i];
            if (section && section._visible) {
                result++;
            }
        }
    }
    return result;
}, insertSection:function (section, index) {
    var accordion = this, sections = accordion._sections, refDom;
    if (!sections) {
        accordion._sections = sections = [];
    }
    if (typeof section == "object" && section.constructor == Object.prototype.constructor) {
        section = new dorado.widget.Section(section);
    }
    if (typeof index == "number") {
        refDom = sections[index]._dom;
        sections.insert(section, index);
    } else {
        sections.push(section);
    }
    if (accordion._rendered) {
        section.render(accordion._dom, refDom);
        accordion.registerInnerControl(section);
        accordion.bindAction(section);
        accordion.refresh();
    }
}, appendSection:function (section) {
    this.insertSection(section);
}, removeSection:function (section) {
    var accordion = this, sections = accordion._sections;
    if (sections) {
        if (typeof section == "number") {
            section = sections[section];
        }
        if (section instanceof dorado.widget.Section) {
            if (accordion._rendered) {
                accordion.unregisterInnerControl(section);
                section.destroy();
                if (section == accordion._activeSection) {
                    accordion.changeToAvialableSection();
                }
                sections.remove(section);
                accordion.refresh();
            } else {
                sections.remove(section);
            }
        }
    }
}, clearSections:function () {
    var accordion = this, sections = accordion._sections, section;
    if (sections) {
        for (var i = 0, j = sections.length; i < j; i++) {
            section = sections[i];
            accordion.unregisterInnerControl(section);
            section.destroy();
        }
        accordion._activeSection = null;
        accordion._sections = [];
    }
}, bindAction:function (section) {
    var accordion = this;
    section._captionBar.addListener("onClick", function () {
        if (accordion._sliding || section._disabled) {
            return;
        }
        section.fireEvent("onCaptionClick", section);
        if (section._expandable) {
            accordion.doSetActiveSection(section, true);
        }
    });
}, createDom:function () {
    var accordion = this, dom = document.createElement("div"), sections = accordion._sections, section;
    if (sections) {
        for (var i = 0, j = sections.length; i < j; i++) {
            section = sections[i];
            section.render(dom);
            accordion.registerInnerControl(section);
            accordion.bindAction(section);
        }
    }
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var accordion = this, sections = accordion._sections, activeSection = accordion._activeSection;
    if (sections && !activeSection) {
        activeSection = accordion.changeToAvialableSection();
    }
    if (activeSection) {
        $fly(activeSection._dom).addClass("active-section");
    }
    var sectionMinHeight, ctHeight, accordionHeight = $fly(dom).height(), visibleCount = accordion.getVisibleSectionCount();
    if (sections) {
        var section, control, sectionCt;
        for (var i = 0, j = sections.length; i < j; i++) {
            section = sections[i];
            sectionCt = section._doms.container;
            if (activeSection != section) {
                $fly(section._dom).removeClass("active-section");
            }
            if (typeof sectionMinHeight != "number") {
                sectionMinHeight = $fly(section._dom).outerHeight(true) - sectionCt.offsetHeight;
                ctHeight = accordionHeight - sectionMinHeight * visibleCount;
            }
            $fly(sectionCt).outerHeight(ctHeight, true);
            control = section._control;
            if (control) {
                control.set({width:$fly(dom).width(), height:ctHeight});
            }
        }
    }
    if (activeSection && activeSection._control && !activeSection._control._rendered) {
        activeSection.doRenderControl();
    }
}});
dorado.widget.toolbar = {};
dorado.widget.ToolBar = $extend(dorado.widget.Control, {$className:"dorado.widget.ToolBar", ATTRIBUTES:{className:{defaultValue:"d-toolbar"}, height:{independent:true, readOnly:true}, items:{setter:function (attr, value) {
    var toolbar = this, items = toolbar._items, rendered = toolbar._rendered, i, l, item;
    if (items) {
        toolbar.clearItems();
    }
    if (!items) {
        toolbar._items = items = new dorado.util.KeyedArray(function (value) {
            return value._id;
        });
    }
    if (value) {
        if (value.constructor == Array.prototype.constructor) {
            for (i = 0, l = value.length; i < l; i++) {
                item = toolbar.createItem(value[i]);
                items.insert(item);
            }
            if (rendered) {
                toolbar.doRenderItems();
            }
        } else {
            if (value instanceof dorado.util.KeyedArray) {
                for (i = 0, l = value.size; i < l; i++) {
                    item = toolbar.createItem(value.get(i));
                    items.append(item);
                }
                if (rendered) {
                    toolbar.doRenderItems();
                }
            }
        }
    }
}}, fixRight:{defaultValue:false}}, createDom:function () {
    var toolbar = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:toolbar._className, content:[{tagName:"div", className:"toolbar-left-wrap", contextKey:"toolbarLeftWrap", content:[{tagName:"div", className:"toolbar-left", contextKey:"toolbarLeft"}]}, {tagName:"div", className:"toolbar-right", contextKey:"toolbarRight"}]}, null, doms);
    toolbar._doms = doms;
    if (toolbar._fixRight) {
        $fly(dom).addClass(toolbar._className + "-fixright");
    }
    toolbar.doRenderItems();
    return dom;
}, createItem:function (config) {
    if (!config) {
        return null;
    }
    if (typeof config == "string" || config.constructor == Object.prototype.constructor) {
        var result = dorado.Toolkits.createInstance("toolbar,widget", config);
        result._parent = result._focusParent = this;
        return result;
    } else {
        config._parent = config._focusParent = this;
        return config;
    }
}, insertItem:function (item, index) {
    var toolbar = this, items = toolbar._items;
    if (!item) {
        return null;
    }
    if (!items) {
        items = toolbar._items = new dorado.util.KeyedArray(function (value) {
            return value._id;
        });
    }
    item = toolbar.createItem(item);
    if (toolbar._rendered) {
        var refDom = null, doms = toolbar._doms;
        if (typeof index == "number") {
            var refItem = items[index];
            refDom = refItem._dom;
        }
        items.insert(item, index);
        item.render(doms.toolbarLeft);
        toolbar.registerInnerControl(item);
        $fly(item._dom).addClass("d-toolbar-item");
    } else {
        items.insert(item, index);
    }
    return item;
}, removeItem:function (item) {
    var toolbar = this, items = toolbar._items;
    if (items && item !== undefined) {
        var realItem = item;
        if (typeof item == "number") {
            realItem = items.get(item);
            items.removeAt(item);
        } else {
            items.remove(item);
        }
        realItem.destroy();
        toolbar.unregisterInnerControl(realItem);
    }
}, clearItems:function () {
    var toolbar = this, items = toolbar._items, afterFill = false;
    for (var i = 0, j = items.size; i < j; i++) {
        var item = items.get(i);
        if (!(item instanceof dorado.widget.toolbar.Fill)) {
            toolbar.unregisterInnerControl(item);
            item.destroy();
        }
    }
    items.clear();
}, doRenderItems:function () {
    var toolbar = this, doms = toolbar._doms, items = toolbar._items || {}, afterFill = false;
    for (var i = 0, j = items.size; i < j; i++) {
        var item = items.get(i);
        if (item instanceof dorado.widget.toolbar.Fill) {
            afterFill = true;
        } else {
            toolbar.registerInnerControl(item);
            if (!afterFill) {
                item.render(doms.toolbarLeft);
            } else {
                item.render(doms.toolbarRight);
            }
            $fly(item._dom).addClass("d-toolbar-item");
        }
    }
}, convertItem:function (item, overflowMenu) {
    if (item instanceof dorado.widget.toolbar.Button) {
        overflowMenu.insertItem({caption:item._caption, submenu:item._menu, listener:{onClick:function () {
            item.fireEvent("onClick", item);
        }}});
    } else {
        if (item instanceof dorado.widget.toolbar.Separator) {
            overflowMenu.insertItem("-");
        }
    }
    item._visible = false;
    $fly(item._dom).css("visibility", "hidden");
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
}, onResize:function () {
    $invokeSuper.call(this, arguments);
    var toolbar = this, dom = toolbar._dom, doms = toolbar._doms, overflowMenu = toolbar._overflowMenu, overflowButton = toolbar._overflowButton, items = toolbar._items, lastChild = doms.toolbarLeft.lastChild, overflow = false;
    if (items && lastChild) {
        var leftRealWidth = lastChild.offsetWidth + lastChild.offsetLeft, leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
        overflow = leftRealWidth > leftVisibleWidth;
    }
    if (overflow) {
        $fly(dom).addClass(toolbar._className + "-overflow");
        if (!overflowMenu) {
            overflowMenu = toolbar._overflowMenu = new dorado.widget.Menu();
            overflowButton = toolbar._overflowButton = new dorado.widget.SimpleButton({className:"overflow-button", menu:overflowMenu});
            overflowButton.render(doms.toolbarRight);
            toolbar.registerInnerControl(overflowButton);
        } else {
            overflowMenu.clearItems();
        }
        var leftWidthSum = 0, startHideIndex = -1, item, i, j, afterFill;
        if (toolbar._fixRight) {
            leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
            for (i = 0, j = items.size; i < j; i++) {
                item = items.get(i);
                if (item instanceof dorado.widget.toolbar.Fill) {
                    break;
                }
                item._visible = true;
                $fly(item._dom).css("visibility", "");
                leftWidthSum += $fly(item._dom).outerWidth(true);
                if (leftWidthSum >= leftVisibleWidth) {
                    startHideIndex = i;
                    break;
                }
            }
            if (startHideIndex > -1) {
                for (i = startHideIndex, j = items.size; i < j; i++) {
                    item = items.get(i);
                    if (item instanceof dorado.widget.toolbar.Fill) {
                        break;
                    }
                    toolbar.convertItem(item, overflowMenu);
                }
            }
        } else {
            afterFill = false;
            for (i = 0, j = items.size; i < j; i++) {
                item = items.get(i);
                if (afterFill) {
                    if (item._dom && (item._dom.parentNode == doms.toolbarRight)) {
                        doms.toolbarLeft.appendChild(item._dom);
                    }
                }
                if (item instanceof dorado.widget.toolbar.Fill) {
                    afterFill = true;
                }
            }
            leftVisibleWidth = dom.offsetWidth - doms.toolbarRight.offsetWidth;
            for (i = 0, j = items.size; i < j; i++) {
                item = items.get(i);
                item._visible = true;
                $fly(item._dom).css("visibility", "");
                leftWidthSum += $fly(item._dom).outerWidth(true);
                if (leftWidthSum >= leftVisibleWidth) {
                    startHideIndex = i;
                    break;
                }
            }
            if (startHideIndex > -1) {
                for (i = startHideIndex, j = items.size; i < j; i++) {
                    item = items.get(i);
                    if (item instanceof dorado.widget.toolbar.Fill) {
                        break;
                    }
                    toolbar.convertItem(item, overflowMenu);
                }
            }
        }
    } else {
        $fly(dom).removeClass(toolbar._className + "-overflow");
        if (!items) {
            return;
        }
        if (!toolbar._fixRight) {
            afterFill = false;
            for (i = 0, j = items.size; i < j; i++) {
                item = items.get(i);
                if (afterFill) {
                    if (item._dom && (item._dom.parentNode == doms.toolbarLeft)) {
                        doms.toolbarRight.appendChild(item._dom);
                    }
                }
                if (item instanceof dorado.widget.toolbar.Fill) {
                    afterFill = true;
                }
            }
        }
        for (i = 0, j = items.size; i < j; i++) {
            item = items.get(i);
            item._visible = true;
            $fly(item._dom).css("visibility", "");
        }
    }
}});
dorado.widget.toolbar.Separator = $extend(dorado.widget.Control, {$className:"dorado.widget.toolbar.Separator", ATTRIBUTES:{className:{defaultValue:"d-toolbar-sep"}}, createDom:function () {
    var separator = this, dom;
    dom = document.createElement("span");
    dom.className = separator._className;
    return dom;
}});
dorado.widget.toolbar.Button = $extend(dorado.widget.Button, {$className:"dorado.widget.toolbar.Button", ATTRIBUTES:{className:{defaultValue:"d-toolbar-button"}, items:{getter:function () {
    if (this._menu) {
        return this._menu.get("items");
    }
    return null;
}, setter:function (attr, value) {
    if (value.constructor == Array.prototype.constructor) {
        this._menu = new dorado.widget.Menu({items:value});
    }
}}}, createDom:function () {
    var button = this, dom = $invokeSuper.call(button, arguments);
    $fly(dom).mouseenter(function () {
        var menu = button._menu, toolbar = button._parent;
        if (menu && toolbar) {
            var activeButton = toolbar._activeMenuButton;
            if (activeButton && activeButton != button) {
                toolbar._ignoreMenuHide = true;
                activeButton._menu.hide(true);
                button.doShowMenu();
            }
        }
    });
    return dom;
}, doShowMenu:function () {
    $invokeSuper.call(this, arguments);
    var button = this, menu = button._menu;
    if (menu) {
        var toolbar = button._parent;
        toolbar._activeMenuButton = button;
    }
}, doAfterMenuHide:function () {
    var button = this, toolbar = button._parent;
    if (toolbar && !toolbar._ignoreMenuHide) {
        toolbar._activeMenuButton = null;
    }
    $invokeSuper.call(this, arguments);
}});
dorado.widget.toolbar.Fill = $extend(dorado.widget.Control, {$className:"dorado.widget.toolbar.Fill"});
dorado.widget.toolbar.Label = $extend(dorado.widget.Control, {$className:"dorado.widget.toolbar.Label", ATTRIBUTES:{className:{defaultValue:"d-toolbar-label"}, text:{}}, createDom:function () {
    var label = this, dom = document.createElement("div");
    dom.className = label._className;
    $fly(dom).text(label._text);
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var label = this;
    $fly(dom).text(label._text);
}});
dorado.Toolkits.registerPrototype("toolbar", {Default:dorado.widget.toolbar.Button, ToolBarButton:dorado.widget.toolbar.Button, "->":dorado.widget.toolbar.Fill, Fill:dorado.widget.toolbar.Fill, ToolBarLabel:dorado.widget.toolbar.Label, "-":dorado.widget.toolbar.Separator, "|":dorado.widget.toolbar.Separator, Separator:dorado.widget.toolbar.Separator});
dorado.widget.FloatContainer = $extend([dorado.widget.Container, dorado.widget.FloatControl], {$className:"dorado.widget.FloatContainer", focusable:true, ATTRIBUTES:{visible:{defaultValue:false}}});
dorado.widget.FloatPanel = $extend([dorado.widget.Panel, dorado.widget.FloatControl], {$className:"dorado.widget.FloatPanel", focusable:true, ATTRIBUTES:{className:{defaultValue:"d-float-panel"}, closeable:{defaultValue:true, setter:function (attr, value) {
    var panel = this, captionBar = panel._captionBar, button;
    panel._closeable = value;
    if (captionBar) {
        if (value) {
            button = captionBar.getButton(panel._id + "_close");
            if (button) {
                $fly(button._dom).css("display", "");
            } else {
                panel._createCloseButton();
            }
        } else {
            button = captionBar.getButton(panel._id + "_close");
            if (button) {
                $fly(button._dom).css("display", "none");
            }
        }
    }
}}, closeAction:{defaultValue:"hide"}, visible:{defaultValue:false}}, EVENTS:{beforeClose:{}, onClose:{}}, createDom:function () {
    var panel = this, dom = $invokeSuper.call(this, arguments);
    if (panel._closeable) {
        panel._createCloseButton();
    }
    return dom;
}, _createCloseButton:function () {
    var panel = this, captionBar = panel._captionBar;
    if (captionBar) {
        captionBar.addButton(new dorado.widget.SimpleButton({id:panel._id + "_close", listener:{onClick:function () {
            panel.close();
        }}, className:"d-close-button"}));
    }
}, close:function () {
    var panel = this, eventArg = {};
    panel.fireEvent("beforeClose", panel, eventArg);
    if (eventArg.processDefault === false) {
        return;
    }
    panel.hide();
    panel.fireEvent("onClose", panel);
    if (panel._closeAction == "close") {
        panel.destroy();
    }
}, doShow:function () {
    var panel = this, doms = panel._doms;
    $fly([doms.contentPanel, doms.buttonPanel]).css("display", "");
    $invokeSuper.call(this, arguments);
}, doAfterShow:function () {
    var panel = this;
    panel._minimized = false;
    $invokeSuper.call(this, arguments);
}});
(function () {
    var handleConfigMap = {"dialog-header-left":{cursor:"nw-resize", horiStyle:"left width", vertStyle:"top height", widthRatio:-1, heightRatio:-1}, "dialog-header-right":{cursor:"ne-resize", horiStyle:"width", vertStyle:"top height", widthRatio:1, heightRatio:-1}, "dialog-header-center":{cursor:"n-resize", horiStyle:"", vertStyle:"top height", widthRatio:1, heightRatio:-1}, "dialog-body-left":{cursor:"w-resize", horiStyle:"left width", vertStyle:"", widthRatio:-1, heightRatio:1}, "dialog-body-right":{cursor:"e-resize", horiStyle:"width", vertStyle:"", widthRatio:1, heightRatio:1}, "dialog-footer-left":{cursor:"sw-resize", horiStyle:"left width", vertStyle:"height", widthRatio:-1, heightRatio:1}, "dialog-footer-right":{cursor:"se-resize", horiStyle:"width", vertStyle:"height", widthRatio:1, heightRatio:1}, "dialog-footer-center":{cursor:"s-resize", horiStyle:"", vertStyle:"height", widthRatio:1, heightRatio:1}};
    dorado.widget.DialogGroup = $class({$className:"dorado.widget.DialogGroup", constructor:function () {
        this._dialogCollection = new dorado.util.KeyedArray(function (value) {
            return value._id;
        });
        this._activeDialog = null;
    }, bringToFront:function (dialog) {
        this._activeDialog = dialog;
    }, sendToBack:function (dialog) {
        if (dialog == this._activeDialog) {
            this._activeDialog = null;
        }
    }, getActiveDialog:function () {
        return this._activeDialog;
    }, getDialog:function (index) {
        return this._dialogCollection.get(index);
    }, register:function (dialog) {
        this._dialogCollection.insert(dialog);
    }, unregister:function (dialog) {
        this._dialogCollection.remove(dialog);
    }, hideAll:function () {
        var dialogs = this._dialogCollection;
        for (var i = 0, j = dialogs.size; i < j; i++) {
            var dialog = dialogs.get(i);
            dialog.minimize();
        }
    }});
    dorado.widget.DialogManager = new dorado.widget.DialogGroup();
    dorado.widget.Dialog = $extend(dorado.widget.FloatPanel, {$className:"dorado.widget.Dialog", focusable:true, ATTRIBUTES:{className:{defaultValue:"d-dialog"}, minWidth:{defaultValue:200}, minHeight:{defaultValue:100}, draggable:{defaultValue:true}, resizeable:{defaultValue:true}, maximizeable:{defaultValue:false, setter:function (attr, value) {
        var dialog = this, captionBar = dialog._captionBar, button;
        dialog._maximizeable = value;
        if (captionBar) {
            if (value) {
                button = captionBar.getButton(dialog._id + "_maximize");
                if (button) {
                    $fly(button._dom).css("display", "");
                } else {
                    dialog._createMaximizeButton();
                }
            } else {
                button = captionBar.getButton(dialog._id + "_maximize");
                if (button) {
                    $fly(button._dom).css("display", "none");
                }
            }
        }
    }}, maximized:{}, maximizeTarget:{}, minimizeable:{defaultValue:false, setter:function (attr, value) {
        var dialog = this, captionBar = dialog._captionBar, button;
        dialog._minimizeable = value;
        if (captionBar) {
            if (value) {
                button = captionBar.getButton(dialog._id + "_minimize");
                if (button) {
                    $fly(button._dom).css("display", "");
                } else {
                    dialog._createMinimizeButton();
                }
            } else {
                button = captionBar.getButton(dialog._id + "_minimize");
                if (button) {
                    $fly(button._dom).css("display", "none");
                }
            }
        }
    }}, minimized:{}, shadowMode:{defaultValue:"frame", skipRefresh:true}}, EVENTS:{beforeMaximize:{}, onMaximize:{}, beforeMinimize:{}, onMinimize:{}}, setFocus:function () {
        var dialog = this;
        if (dialog._rendered) {
            dialog._dom.focus();
        }
    }, applyDraggable:function () {
    }, maximizeRestore:function () {
        var dialog = this, dom = dialog._dom, doms = dialog._doms;
        if (dom) {
            $fly(doms.contentPanel).css("display", "");
            if (dialog._maximized) {
                dialog._maximized = false;
                dialog._width = dialog._originalWidth;
                dialog._height = dialog._originalHeight;
                dialog._left = dialog._originalLeft;
                dialog._top = dialog._originalTop;
                dialog.resetDimension();
                dialog.refresh();
                var $dom = $fly(dom);
                if (dialog._resizeable) {
                    $dom.addClass("d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
                }
                if (dialog._draggable) {
                    $dom.addClass("d-dialog-draggable").draggable("enable");
                }
            }
        }
    }, maximize:function () {
        var dialog = this, dom = dialog._dom;
        if (dom) {
            var eventArg = {};
            dialog.fireEvent("beforeMaximize", dialog, eventArg);
            if (eventArg.processDefault === false) {
                return;
            }
            dialog._maximized = true;
            dialog._originalWidth = dialog._width;
            dialog._originalHeight = dialog._height;
            dialog._originalLeft = dialog._left;
            dialog._originalTop = dialog._top;
            var maximizeTarget = dialog._maximizeTarget;
            if (typeof maximizeTarget == "String") {
                maximizeTarget = jQuery(maximizeTarget)[0];
            } else {
                if (maximizeTarget && dorado.Object.isInstanceOf(maximizeTarget, dorado.RenderableElement)) {
                    maximizeTarget = maximizeTarget._dom;
                }
            }
            if (maximizeTarget) {
                dialog._width = $fly(maximizeTarget).outerWidth(true);
                dialog._height = $fly(maximizeTarget).outerHeight(true);
            } else {
                dialog._width = $fly(window).width();
                dialog._height = $fly(window).height();
            }
            dialog.resetDimension();
            var targetOffset = $fly(maximizeTarget).offset() || {left:0, top:0};
            dialog._left = targetOffset.left;
            dialog._top = targetOffset.top;
            var domEl = $fly(dom);
            domEl.css(targetOffset);
            if (dialog._resizeable) {
                domEl.removeClass("d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
            }
            if (dialog._draggable) {
                domEl.removeClass("d-dialog-draggable").draggable("disable");
            }
            dialog.fireEvent("onMaximize", dialog);
        }
    }, minimize:function () {
        var dialog = this, dom = dialog._dom;
        if (dom) {
            var eventArg = {};
            dialog.fireEvent("beforeMinimize", dialog, eventArg);
            if (eventArg.processDefault) {
                return;
            }
            dialog._minimized = true;
            dialog.hide();
            dialog.fireEvent("onMinimize", dialog);
        }
    }, doSetCollapsed:function (collapsed) {
        $invokeSuper.call(this, arguments);
        var dialog = this;
        if (dialog._resizeable) {
            if (collapsed) {
                $fly(dialog._dom).removeClass("d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
            } else {
                $fly(dialog._dom).addClass("d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
            }
        }
    }, doOnResize:function (collapsed) {
        var dialog = this, dom = dialog._dom, doms = dialog._doms, height = dialog.getRealHeight(), width = dialog.getRealWidth();
        if (typeof width == "string") {
            if (width.indexOf("%") == -1) {
                width = parseInt(width, 10);
            } else {
                width = jQuery(window).width() * parseInt(width.replace("%", ""), 10) / 100;
            }
        }
        if (typeof height == "string") {
            if (height.indexOf("%") == -1) {
                height = parseInt(height, 10);
            } else {
                height = jQuery(window).height() * parseInt(height.replace("%", ""), 10) / 100;
            }
        }
        if (typeof height == "number" && height > 0) {
            if (collapsed === undefined) {
                collapsed = dialog._collapsed;
            }
            if (collapsed) {
                $fly(dom).height("auto");
            } else {
                var domHeight, headerHeight = $fly(doms.header).outerHeight(true), bottomHeight = $fly(doms.footer).outerHeight(true), captionBarHeight = $fly(doms.captionBar).outerHeight(true), buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                domHeight = $fly(dom).height();
                $fly(doms.contentPanel).outerHeight(height - headerHeight - bottomHeight - captionBarHeight - buttonPanelHeight);
                if (dorado.Browser.msie && dorado.Browser.version == 6) {
                    $fly(doms.bodyWrap).css("zoom", 1);
                }
            }
        } else {
            $fly(doms.contentPanel).css("height", "");
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(doms.bodyWrap).css("zoom", 1);
            }
        }
        $fly(dom).css("height", "");
        if (typeof width == "number" && width > 0) {
            $fly(dom).outerWidth(width);
        }
    }, _createMaximizeButton:function () {
        var dialog = this, captionBar = dialog._captionBar;
        if (captionBar) {
            captionBar.addButton(new dorado.widget.SimpleButton({id:dialog._id + "_maximize", className:"d-maximize-button", listener:{onClick:function () {
                if (!dialog._maximized) {
                    dialog.maximize();
                } else {
                    dialog.maximizeRestore();
                }
            }}}), dialog._id + "_close");
        }
    }, _createMinimizeButton:function () {
        var dialog = this, captionBar = dialog._captionBar;
        if (captionBar) {
            captionBar.addButton(new dorado.widget.SimpleButton({id:dialog._id + "_minimize", className:"d-minimize-button", listener:{onClick:function () {
                if (!dialog._minimized) {
                    dialog.minimize();
                }
            }}}), 0);
        }
    }, createDom:function () {
        var dialog = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:dialog._className, style:{visibility:dialog._visible ? "visible" : "hidden"}, content:[{tagName:"div", className:"dialog-header", contextKey:"header", content:[{tagName:"div", className:"dialog-header-left dialog-resize-handle", contextKey:"headerLeft"}, {tagName:"div", className:"dialog-header-right dialog-resize-handle", contextKey:"headerRight"}, {tagName:"div", className:"dialog-header-center dialog-resize-handle", contextKey:"headerCenter"}]}, {tagName:"div", className:"dialog-body-wrap", contextKey:"bodyWrap", content:[{tagName:"div", className:"dialog-body-left dialog-resize-handle", contextKey:"bodyLeft"}, {tagName:"div", className:"dialog-body-right dialog-resize-handle", contextKey:"bodyRight"}, {tagName:"div", className:"dialog-body", contextKey:"body", content:{tagName:"div", className:"content-panel", contextKey:"contentPanel"}}]}, {tagName:"div", className:"dialog-footer", contextKey:"footer", content:[{tagName:"div", className:"dialog-footer-left dialog-resize-handle", contextKey:"footerLeft"}, {tagName:"div", className:"dialog-footer-right dialog-resize-handle", contextKey:"footerRight"}, {tagName:"div", className:"dialog-footer-center dialog-resize-handle", contextKey:"footerCenter"}]}]}, null, doms);
        dialog._doms = doms;
        var captionBar = dialog._captionBar = new dorado.widget.CaptionBar({className:"d-dialog-caption-bar", caption:dialog._caption, icon:dialog._icon});
        dialog.registerInnerControl(captionBar);
        captionBar.render(doms.body.parentNode, doms.body);
        doms.captionBar = captionBar._dom;
        $DomUtils.disableUserSelection(doms.captionBar);
        dialog.initButtons();
        if (dialog._minimizeable) {
            dialog._createMinimizeButton();
        }
        if (dialog._maximizeable) {
            dialog._createMaximizeButton();
        }
        if (dialog._closeable) {
            dialog._createCloseButton();
        }
        if (dialog._collapseable) {
            dialog._createCollapseButton();
        }
        if (dialog._draggable) {
            $fly(dom).addClass("d-dialog-draggable").css("position", "absolute").draggable({addClasses:false, handle:".d-dialog-caption-bar", cursor:"move", containment:"parent", start:function () {
                $fly(dom).addClass("d-dialog-dragging").disableShadow();
            }, stop:function () {
                $fly(dom).removeClass("d-dialog-dragging").enableShadow();
                dialog._left = parseInt($fly(dom).css("left"), 10);
                dialog._top = parseInt($fly(dom).css("top"), 10);
            }});
        }
        if (dialog._resizeable) {
            var dialogXY, dialogSize;
            $fly(dom).addClass("d-dialog-resizeable").find(".dialog-resize-handle").each(function (index, handle) {
                var className = handle.className.split(" ")[0], config = handleConfigMap[className];
                if (!config) {
                    return;
                }
                $fly(handle).draggable({cursor:config.cursor, addClasses:false, containment:"window", helper:function () {
                    var proxy = document.createElement("div");
                    proxy.className = "d-dialog-drag-proxy";
                    proxy.style.position = "absolute";
                    $fly(proxy).bringToFront();
                    document.body.appendChild(proxy);
                    return proxy;
                }, start:function (event, ui) {
                    var $dom = $fly(dom);
                    dialogXY = $dom.offset();
                    dialogSize = [$dom.width(), $dom.height()];
                    jQuery(ui.helper).outerWidth(dialogSize[0]).outerHeight(dialogSize[1]).css("cursor", config.cursor);
                }, drag:function (event, ui) {
                    var horiStyle = config.horiStyle, vertStyle = config.vertStyle, heightRatio = config.heightRatio, widthRatio = config.widthRatio, minWidth = dialog._minWidth || 200, minHeight = dialog._minHeight || 100;
                    ui.position = {left:$fly(dom).offset().left, top:$fly(dom).offset().top};
                    var inst = jQuery.data(this, "draggable"), horiChange = event.pageX - inst.originalPageX, vertChange = event.pageY - inst.originalPageY, width, height;
                    if (horiStyle.indexOf("width") != -1) {
                        width = dialogSize[0] + widthRatio * horiChange;
                        if (width >= minWidth) {
                            jQuery(ui.helper).width(width);
                        }
                    }
                    if (vertStyle.indexOf("height") != -1) {
                        height = dialogSize[1] + heightRatio * vertChange;
                        if (height >= minHeight) {
                            jQuery(ui.helper).height(height);
                        }
                    }
                    if (horiStyle.indexOf("left") != -1) {
                        if (width >= minWidth) {
                            ui.position.left = dialogXY.left + horiChange;
                        } else {
                            ui.position.left = jQuery(ui.helper).offset().left;
                        }
                    }
                    if (vertStyle.indexOf("top") != -1) {
                        if (height >= minHeight) {
                            ui.position.top = dialogXY.top + vertChange;
                        } else {
                            ui.position.top = jQuery(ui.helper).offset().top;
                        }
                    }
                }, stop:function (event, ui) {
                    var wrap = ui.helper, wrapEl = jQuery(wrap), offset = wrapEl.offset();
                    dialog._left = offset.left;
                    dialog._top = offset.top;
                    dialog._width = wrapEl.width();
                    dialog._height = wrapEl.height();
                    dialog.resetDimension();
                    $fly(dialog._dom).css(offset);
                }});
            });
        }
        return dom;
    }, getShowPosition:function (options) {
        var panel = this;
        if (panel._maximized) {
            return {left:0, top:0};
        } else {
            return $invokeSuper.call(panel, arguments);
        }
    }, refreshDom:function (dom) {
        var dialog = this;
        $invokeSuper.call(dialog, arguments);
        if (!dialog._maximized) {
            $fly(dom).css({left:dialog._left || 0, top:dialog._top || 0});
        }
    }});
})();
(function () {
    var MENU_ITEM_DISABLED_CLASS = "menu-item-disabled", CHECKED_ICON = "checked-icon", UN_CHECKED_ICON = "unchecked-icon", HAS_GROUP_CLASS = "has-subgroup", MENU_ITEM_OVER_CLASS = "menu-item-hover";
    dorado.widget.menu = {};
    dorado.widget.menu.AbstractMenuItem = $extend([dorado.RenderableElement, dorado.EventSupport], {$className:"dorado.widget.menu.AbstractMenuItem", ATTRIBUTES:{parent:{}, name:{}, visible:{defaultValue:true, setter:function (attr, value) {
        var item = this, dom = item._dom;
        item._visible = value;
        if (dom) {
            dom.style.display = value ? "" : "none";
        }
    }}, userData:{}}, constructor:function (config) {
        $invokeSuper.call(this, arguments);
        if (config) {
            this.set(config);
        }
    }, getListenerScope:function () {
        if (this._parent && this._parent._view) {
            return this._parent._view;
        }
        return this;
    }});
    dorado.widget.menu.Separator = $extend(dorado.widget.menu.AbstractMenuItem, {$className:"dorado.widget.menu.Separator", ATTRIBUTES:{className:{defaultValue:"menu-item-separator"}}, createDom:function () {
        var item = this, dom = document.createElement("li");
        dom.className = item._className;
        dom.style.display = item._visible ? "" : "none";
        return dom;
    }, refreshDom:function (dom) {
        var item = this;
        if (dom) {
            dom.style.display = item._visible ? "" : "none";
        }
    }});
    dorado.widget.menu.TextMenuItem = $extend([dorado.widget.menu.AbstractMenuItem, dorado.widget.ActionSupport], {$className:"dorado.widget.menu.TextMenuItem", ATTRIBUTES:{hideOnClick:{defaultValue:true}, className:{defaultValue:"menu-item"}, disabled:{}, caption:{}, icon:{}, iconClass:{}}, EVENTS:{onClick:{}}, refreshDom:function (dom) {
        var item = this, action = item._action || {}, disabled = item._disabled || action._disabled, icon = item._icon || action._icon, iconCls = item._iconClass || action._iconClass, doms = item._doms;
        $fly(dom)[disabled ? "addClass" : "removeClass"](MENU_ITEM_DISABLED_CLASS).css("display", item._visible ? "" : "none");
        $fly(doms.caption).text(item._caption || action._caption);
        if (icon) {
            $DomUtils.setBackgroundImage(doms.icon, icon);
        } else {
            $fly(doms.icon).css("background-image", "");
        }
        $fly(doms.icon).attr("className", "icon");
        if (iconCls) {
            $fly(doms.icon).addClass(iconCls);
        }
    }, createDom:function () {
        var item = this, action = item._action || {}, doms = {}, dom = $DomUtils.xCreateElement({tagName:"li", className:"menu-item", content:{tagName:"span", className:"menu-item-content", contextKey:"itemContent", content:[{tagName:"span", className:"icon", contextKey:"icon", content:"&nbsp;"}, {tagName:"span", className:"caption", content:item._caption || action._caption, contextKey:"caption"}]}}, null, doms), disabled = item._disabled || action._disabled, icon = item._icon || action._icon;
        item._doms = doms;
        $fly(dom).css("display", item._visible ? "" : "none").addClass(disabled ? MENU_ITEM_DISABLED_CLASS : "").hover(function () {
            item._parent.focusItem(item, true);
        }, dorado._NULL_FUNCTION);
        if (icon) {
            $DomUtils.setBackgroundImage(doms.icon, icon);
        } else {
            $fly(doms.icon).css("background-image", "");
        }
        return dom;
    }, hideTopMenu:function () {
        var item = this;
        if (item._parent) {
            item._parent.hideTopMenu();
        }
    }});
    dorado.widget.menu.ControlMenuItem = $extend(dorado.widget.menu.TextMenuItem, {$className:"dorado.widget.menu.ControlMenuItem", focusable:true, ATTRIBUTES:{control:{}}, createDom:function () {
        var item = this, dom = $invokeSuper.call(this, arguments);
        $fly(dom).click(function (event) {
            item.onClick(event);
        }).addClass(item._control ? HAS_GROUP_CLASS : "");
        return dom;
    }, hideControl:function () {
        var item = this;
        if (item._control) {
            item._control.hide();
        }
    }, onSelect:function () {
        var item = this;
        item.hideControl();
        item.hideTopMenu();
    }, onClick:function () {
    }, onFocus:function () {
        var item = this, dom = item._dom;
        $fly(dom).addClass(MENU_ITEM_OVER_CLASS);
        if (item._control) {
            item._control.show({anchorTarget:item, align:"right", vAlign:"innertop", delay:300, onHide:function (self) {
                self.opener = null;
            }});
            item._control._focusParent = item._parent;
            item._control.opener = item;
        }
    }, onBlur:function () {
        var item = this, dom = item._dom;
        $fly(dom).removeClass(MENU_ITEM_OVER_CLASS);
        if (item._control) {
            item._control.hide();
        }
    }});
    dorado.widget.menu.MenuItem = $extend(dorado.widget.menu.TextMenuItem, {$className:"dorado.widget.menu.MenuItem", focusable:true, ATTRIBUTES:{submenu:{setter:function (attr, value) {
        if (!value) {
            this._submenu = null;
        } else {
            if (value.constructor == Object.prototype.constructor) {
                this._submenu = new dorado.widget.Menu(value);
            } else {
                if (value instanceof dorado.widget.Menu) {
                    this._submenu = value;
                }
            }
        }
        var dom = this._dom;
        if (dom) {
            $fly(dom)[this._submenu ? "addClass" : "removeClass"](HAS_GROUP_CLASS);
        }
    }}, items:{getter:function () {
        if (this._submenu) {
            return this._submenu.get("items");
        }
        return null;
    }, setter:function (attr, value) {
        if (value.constructor == Array.prototype.constructor) {
            this._submenu = new dorado.widget.Menu({items:value});
        }
    }}}, hasSubmenu:function () {
        return !!this._submenu;
    }, insertItem:function (item, index) {
        var menuItem = this, submenu = menuItem._submenu;
        if (item) {
            if (!submenu) {
                this._submenu = submenu = new dorado.widget.Menu();
            }
            submenu.insertItem(item, index);
        }
    }, removeItem:function (item) {
        var menuItem = this, submenu = menuItem._submenu;
        if (item != null && submenu) {
            submenu.removeItem(item);
        }
    }, clearItems:function () {
        var menuItem = this, submenu = menuItem._submenu;
        if (submenu) {
            submenu.clearItems();
        }
    }, onFocus:function (showSubMenu) {
        var item = this, dom = item._dom;
        $fly(dom).addClass(MENU_ITEM_OVER_CLASS);
        if (item._submenu && showSubMenu) {
            item.showSubmenu();
        }
    }, onBlur:function () {
        var item = this, dom = item._dom;
        $fly(dom).removeClass(MENU_ITEM_OVER_CLASS);
        if (item._submenu) {
            item.hideSubmenu();
        }
    }, showSubmenu:function (focusfirst) {
        var item = this, submenu = item._submenu;
        if (submenu) {
            submenu.show({anchorTarget:item, align:"right", vAlign:"innertop", delay:300, focusFirst:focusfirst});
            submenu._focusParent = item._parent;
        }
    }, onClick:function (event) {
        var item = this, action = item._action || {}, disabled = item._disabled || action._disabled;
        if (!disabled) {
            if (item.hasSubmenu()) {
                action.execute && action.execute();
                item.fireEvent("onClick", item);
                event.stopImmediatePropagation();
            } else {
                action.execute && action.execute();
                item.fireEvent("onClick", item);
                if (item._hideOnClick) {
                    item._parent.hideTopMenu();
                }
            }
        }
    }, hideSubmenu:function () {
        var submenu = this._submenu;
        if (submenu) {
            submenu.hide();
        }
    }, createDom:function () {
        var item = this, dom = $invokeSuper.call(this, arguments);
        $fly(dom).click(function (event) {
            item.onClick(event);
        }).addClass(item.hasSubmenu() ? HAS_GROUP_CLASS : "");
        return dom;
    }});
    dorado.widget.menu.CheckGroupManager = {groups:{}, addCheckItem:function (groupName, item) {
        if (!groupName || !item) {
            return;
        }
        var manager = this, groups = manager.groups, group = groups[groupName];
        if (!group) {
            group = groups[groupName] = {current:null, items:[]};
        }
        group.items.push(item);
        if (item._checked) {
            dorado.widget.menu.CheckGroupManager.setGroupCurrent(groupName, item);
        }
    }, removeCheckItem:function (groupName, item) {
        if (!groupName || !item) {
            return;
        }
        var manager = this, groups = manager.groups, group = groups[groupName];
        if (group) {
            if (group.current == item) {
                group.current = null;
            }
            group.items.remove(item);
        }
    }, setGroupCurrent:function (groupName, item) {
        if (!groupName || !item) {
            return;
        }
        var manager = this, groups = manager.groups, group = groups[groupName];
        if (group) {
            var current = group.current;
            if (current == item) {
                return;
            }
            if (current instanceof dorado.widget.menu.CheckableMenuItem) {
                current.set("checked", false);
            }
            group.current = item;
        }
    }};
    dorado.widget.menu.CheckableMenuItem = $extend(dorado.widget.menu.MenuItem, {$className:"dorado.widget.menu.CheckableMenuItem", ATTRIBUTES:{checked:{setter:function (attr, value) {
        var item = this;
        if (value && item._group) {
            dorado.widget.menu.CheckGroupManager.setGroupCurrent(item._group, item);
        }
        item._checked = value;
    }}, group:{setter:function (attr, value) {
        var item = this, oldValue = item._group;
        if (oldValue) {
            dorado.widget.menu.CheckGroupManager.removeCheckItem(oldValue, item);
        }
        if (value) {
            dorado.widget.menu.CheckGroupManager.addCheckItem(value, item);
        }
        item._group = value;
    }}}, EVENTS:{onCheckedChange:{}}, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var item = this;
        if (item._dom) {
            if (item._checked) {
                $fly(item._doms.icon).removeClass(UN_CHECKED_ICON).addClass(CHECKED_ICON);
            } else {
                $fly(item._doms.icon).removeClass(CHECKED_ICON).addClass(UN_CHECKED_ICON);
            }
        }
    }, onClick:function () {
        var item = this, parent = item._parent, action = item._action || {}, disabled = item._disabled || action._disabled;
        if (!disabled) {
            if (item.hasSubmenu()) {
                item.fireEvent("onClick", item);
            } else {
                item.fireEvent("onClick", item);
                if (item._hideOnClick) {
                    parent.hideTopMenu();
                }
            }
            item.set("checked", !item._checked);
            item.fireEvent("onCheckedChange", item);
        }
    }, createDom:function () {
        var item = this, dom = $invokeSuper.call(this, arguments);
        $fly(item._doms.icon).addClass(item._checked ? CHECKED_ICON : UN_CHECKED_ICON);
        $fly(dom).hover(function () {
            item._parent.focusItem(item, true);
        }, dorado._NULL_FUNCTION);
        return dom;
    }});
    dorado.Toolkits.registerPrototype("menu", {"-":dorado.widget.menu.Separator, Separator:dorado.widget.menu.Separator, Checkable:dorado.widget.menu.CheckableMenuItem, Text:dorado.widget.menu.TextMenuItem, Control:dorado.widget.menu.ControlMenuItem, Default:dorado.widget.menu.MenuItem});
})();
(function () {
    var GROUP_CONTENT_CLASS = "group-content", GROUP_OVERFLOW_CLASS = "d-menu-item-group-overflow";
    dorado.widget.Menu = $extend([dorado.widget.Control, dorado.widget.FloatControl], {$className:"dorado.widget.Menu", focusable:true, selectable:false, ATTRIBUTES:{className:{defaultValue:"d-menu-item-group"}, floatingClassName:{defaultValue:"d-menu-floating"}, visible:{defaultValue:false}, animateType:{defaultValue:"slide", skipRefresh:true}, hideAnimateType:{defaultValue:"fade", skipRefresh:true}, iconPosition:{defaultValue:"left"}, iconSize:{defaultValue:"normal"}, items:{setter:function (attr, value) {
        var group = this, items = group._items, doms = group._doms, rendered = group._rendered, item, i, l;
        if (items) {
            group.clearItems();
        }
        if (value) {
            if (!items) {
                group._items = items = new dorado.util.KeyedArray(function (value) {
                    return value._name;
                });
            }
            if (value.constructor == Array.prototype.constructor) {
                for (i = 0, l = value.length; i < l; i++) {
                    item = group.createMenuItem(value[i]);
                    items.insert(item);
                    if (rendered) {
                        item.render(doms.groupContent);
                    }
                }
            } else {
                if (value instanceof dorado.util.KeyedArray) {
                    for (i = 0, l = value.size; i < l; i++) {
                        item = group.createMenuItem(value.get(i));
                        items.append(item);
                        if (rendered) {
                            item.render(doms.groupContent);
                        }
                    }
                }
            }
        }
    }}}, EVENTS:{onHideTopMenu:{}}, createMenuItem:function (config) {
        if (!config) {
            return null;
        }
        var group = this, item;
        if (config instanceof dorado.widget.menu.AbstractMenuItem) {
            config._parent = group;
            item = config;
        } else {
            item = dorado.Toolkits.createInstance("menu", config);
            item._parent = group;
        }
        return item;
    }, createOverflowarrow:function () {
        var group = this, dom = group._dom, doms = group._doms;
        if (dom) {
            var topArrow = $DomUtils.xCreateElement({tagName:"div", className:"overflow-top-arrow"});
            var bottomArrow = $DomUtils.xCreateElement({tagName:"div", className:"overflow-bottom-arrow"});
            $fly(dom).prepend(topArrow);
            dom.appendChild(bottomArrow);
            jQuery(topArrow).repeatOnClick(function () {
                group.doScrollUp();
            }, 120).addClassOnHover("overflow-top-arrow-hover");
            jQuery(bottomArrow).repeatOnClick(function () {
                group.doScrollDown();
            }, 120).addClassOnHover("overflow-bottom-arrow-hover");
            doms.overflowTopArrow = topArrow;
            doms.overflowBottomArrow = bottomArrow;
        }
    }, clearOverflow:function () {
        var group = this, dom = group._dom, doms = group._doms;
        if (dom) {
            group._overflowing = false;
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(dom).css("width", "");
            }
            $fly(dom).css("height", "").removeClass(GROUP_OVERFLOW_CLASS);
            $fly(doms.groupContent).scrollTop(0).css("height", "");
        }
    }, handleOverflow:function (overflowHeight) {
        var group = this, dom = group._dom, doms = group._doms;
        if (dom) {
            $fly(dom).addClass(GROUP_OVERFLOW_CLASS).outerHeight(overflowHeight);
            if (!doms.overflowTopArrow) {
                group.createOverflowarrow();
            }
            group._overflowing = true;
            var contentHeight = $fly(dom).innerHeight() - $fly(doms.overflowTopArrow).outerHeight() - $fly(doms.overflowBottomArrow).outerHeight(), contentWidth = $fly(dom).width();
            if (dorado.Browser.msie && dorado.Browser.version == 6) {
                $fly(dom).width(contentWidth);
                $fly(doms.groupContent).outerHeight(contentHeight);
            } else {
                $fly(doms.groupContent).outerHeight(contentHeight);
            }
            $fly([doms.overflowTopArrow, doms.overflowBottomArrow]).outerWidth(contentWidth);
        }
    }, clearFocusItem:function () {
        var group = this, focusItem = group._focusItem;
        if (focusItem && focusItem.focusable) {
            focusItem.onBlur();
        }
        group._focusItem = null;
    }, getFocusableItem:function (mode) {
        var group = this, items = group._items, focusItem = group._focusItem, focusIndex = -1, result = null, i, j, item;
        if (!items) {
            return null;
        }
        mode = mode || "next";
        if (focusItem) {
            focusIndex = items.indexOf(focusItem);
        }
        if (mode == "next") {
            for (i = focusIndex + 1, j = items.size; i < j; i++) {
                item = items.get(i);
                if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
                    result = item;
                    break;
                }
            }
            if (result == null) {
                mode = "first";
            } else {
                return result;
            }
        } else {
            if (mode == "prev") {
                for (i = focusIndex - 1; i >= 0; i--) {
                    item = items.get(i);
                    if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
                        result = item;
                        break;
                    }
                }
                if (result == null) {
                    mode = "last";
                } else {
                    return result;
                }
            }
        }
        if (mode == "first") {
            for (i = 0, j = items.size; i < j; i++) {
                item = items.get(i);
                if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
                    result = item;
                    break;
                }
            }
            return result;
        } else {
            if (mode == "last") {
                for (i = items.size - 1; i >= 0; i--) {
                    item = items.get(i);
                    if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
                        result = item;
                        break;
                    }
                }
                return result;
            }
        }
    }, focusItem:function (item, showSubmenu) {
        var group = this, focusItem = group._focusItem;
        if (item && focusItem !== item) {
            group._focusItem = item;
            if (group._overflowing) {
                var itemDom = item._dom, doms = group._doms, viewTop = $fly(doms.groupContent).attr("scrollTop"), contentTop = $fly(doms.groupContent).attr("offsetTop"), itemTop = itemDom.offsetTop, itemHeight = itemDom.offsetHeight, itemBottom = itemTop + itemHeight, contentHeight = $fly(doms.groupContent).innerHeight(), viewBottom = contentHeight + viewTop;
                if (!dorado.Browser.msie) {
                    itemTop = itemTop - contentTop;
                    itemBottom = itemTop + itemHeight;
                }
                if (itemTop < viewTop) {
                    $fly(doms.groupContent).attr("scrollTop", itemTop);
                } else {
                    if (itemBottom > viewBottom) {
                        $fly(doms.groupContent).attr("scrollTop", itemBottom - contentHeight);
                    }
                }
            }
            if (focusItem && focusItem.focusable) {
                focusItem.onBlur();
            }
            if (!item._disabled) {
                item.onFocus(showSubmenu);
            }
        }
    }, insertItem:function (item, index) {
        var group = this, items = group._items, doms = group._doms, refItem;
        items = group._items = items ? group._items : new dorado.util.KeyedArray(function (value) {
            return value._name;
        });
        if (item.constructor == Object.prototype.constructor || typeof item == "string") {
            item = group.createMenuItem(item);
        } else {
            item._parent = group;
        }
        if (typeof index == "number") {
            items.insert(item, index);
            refItem = items.get(index + 1);
        } else {
            if (index instanceof dorado.widget.menu.AbstractMenuItem) {
                refItem = items.indexOf(index);
                if (index != -1) {
                    items.insert(item, index);
                } else {
                    throw new dorado.ResourceException("dorado.base.ItemNotInGroup");
                }
            } else {
                items.insert(item);
            }
        }
        if (group._rendered) {
            item.render(doms.groupContent, refItem ? refItem._dom : null);
        }
        group.refresh();
    }, getItem:function (name) {
        var group = this, items = group._items;
        if (items) {
            if (typeof name == "number" || typeof name == "string") {
                return items.get(name);
            } else {
                return name;
            }
        }
        return null;
    }, findItem:function (path) {
        var group = this, items = group._items, item, itemgroup, i, j;
        if (!items) {
            return null;
        }
        if (typeof path == "string") {
            var index = path.indexOf("/"), mainpath, subpath;
            if (index != -1) {
                mainpath = path.substring(0, index);
                subpath = path.substring(index + 1);
            } else {
                mainpath = path;
            }
            if (subpath) {
                for (i = 0, j = items.size; i < j; i++) {
                    item = items.get(i);
                    if (item._name == mainpath) {
                        itemgroup = item._submenu;
                        if (itemgroup) {
                            return itemgroup.findItem(subpath);
                        }
                    }
                }
            } else {
                for (i = 0, j = items.size; i < j; i++) {
                    item = items.get(i);
                    if (item._name == mainpath) {
                        return item;
                    }
                }
                return null;
            }
        }
        return null;
    }, removeItem:function (item) {
        var group = this, items = group._items, dom;
        if (items) {
            if (typeof item == "number" || typeof item == "string") {
                item = items.get(item);
            }
            if (item) {
                items.remove(item);
                dom = item._dom;
                if (dom) {
                    $fly(dom).remove();
                }
                return item;
            }
        }
        group.refresh();
        return null;
    }, clearItems:function (deep) {
        var group = this, items = group._items, dom;
        if (items) {
            var innerItems = items.items;
            for (var i = 0, j = innerItems.length; i < j; i++) {
                var item = innerItems[0];
                dom = item._dom;
                if (deep && item instanceof dorado.widget.menu.MenuItem) {
                    var subGroup = item._submenu;
                    if (subGroup) {
                        subGroup.clearItems(deep);
                        subGroup.destroy();
                    }
                }
                if (dom) {
                    $fly(dom).remove();
                }
                items.removeAt(0);
            }
            group.refresh();
        }
    }, hideTopMenu:function () {
        var group = this, opener = group.opener, parent;
        if (group._floating) {
            group.hide();
            group.fireEvent("onHideTopMenu", group);
            if (opener) {
                parent = opener._parent;
                parent.hideTopMenu();
            }
        }
    }, doScrollUp:function () {
        var group = this, doms = group._doms, groupContent = doms.groupContent, st = $fly(groupContent).scrollTop(), target = st - 22;
        if (target >= 0) {
            $fly(groupContent).scrollTop(target);
        } else {
            $fly(groupContent).scrollTop(0);
        }
    }, doScrollDown:function () {
        var group = this, doms = group._doms, groupContent = doms.groupContent, st = $fly(groupContent).scrollTop(), target = st + 22, scrollHeight = $fly(groupContent).attr("scrollHeight");
        if (target <= scrollHeight) {
            $fly(groupContent).scrollTop(target);
        } else {
            $fly(groupContent).scrollTop(scrollHeight - $fly(groupContent).innerHeight());
        }
    }, doOnBlur:function () {
        if (this._floating) {
            this.hide();
        } else {
            this.clearFocusItem();
        }
    }, doOnKeyDown:function (event) {
        var group = this, opener, focusItem;
        switch (event.keyCode) {
          case 37:
            if (group) {
                opener = group.opener;
                if (opener) {
                    opener.hideSubmenu && opener.hideSubmenu();
                }
            }
            break;
          case 38:
            group.focusItem(group.getFocusableItem("prev"));
            break;
          case 39:
            if (group._focusItem) {
                group._focusItem.showSubmenu && group._focusItem.showSubmenu(true);
            }
            break;
          case 40:
            group.focusItem(group.getFocusableItem("next"));
            break;
          case 13:
            focusItem = group._focusItem;
            if (focusItem) {
                focusItem.onClick && focusItem.onClick();
            }
            return false;
          case 27:
            group.hideTopMenu();
            break;
        }
    }, createDom:function () {
        var group = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:group._className, content:{tagName:"ul", className:"group-content", contextKey:"groupContent"}}, null, doms), items = group._items;
        group._doms = doms;
        var groupContent = doms.groupContent;
        if (items) {
            items.each(function (item) {
                item.render(groupContent);
            });
        }
        $fly(dom).hover(dorado._NULL_FUNCTION, function () {
            var focusItem = group._focusItem;
            if (focusItem) {
                if (focusItem instanceof dorado.widget.menu.MenuItem) {
                    if (!focusItem._submenu) {
                        group.clearFocusItem();
                    }
                } else {
                    if (focusItem instanceof dorado.widget.menu.ControlMenuItem) {
                        if (!focusItem._control) {
                            group.clearFocusItem();
                        }
                    } else {
                        group.clearFocusItem();
                    }
                }
            }
        }).click(function (event) {
            event.stopImmediatePropagation();
        });
        $fly(groupContent).mousewheel(function (event, delta) {
            if (group._overflowing) {
                if (delta < 0) {
                    group.doScrollDown();
                } else {
                    if (delta > 0) {
                        group.doScrollUp();
                    }
                }
            }
        });
        if (group._iconPosition == "top") {
            $fly(dom).addClass(group._className + "-icon-top");
        }
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var group = this, doms = group._doms, menuContentHeight = $fly(doms.groupContent).outerHeight();
        if (menuContentHeight > dom.offsetHeight) {
            group.handleOverflow();
        } else {
        }
        var items = group._items || {}, visibleItemCount = 0;
        for (var i = 0, j = items.size; i < j; i++) {
            var item = items.get(i);
            if (item._visible === false) {
                continue;
            }
            visibleItemCount++;
        }
        if (visibleItemCount == 0) {
            if (!group._noContentEl) {
                group._noContentEl = document.createElement("div");
                group._noContentEl.className = "no-content-group";
                group._noContentEl.innerHTML = "&lt;empty panel&gt;";
                dom.appendChild(group._noContentEl);
            }
            $fly(dom).addClass(group._className + "-no-content");
        } else {
            if (group._noContentEl) {
                $fly(dom).removeClass(group._className + "-no-content");
            }
        }
    }, getShowPosition:function (options) {
        var menu = this, anchorTarget = options.anchorTarget, dom = menu._dom, fixedElement, result;
        options = options || {};
        options.overflowHandler = function (overflowHeight) {
            menu.handleOverflow(overflowHeight);
        };
        if (anchorTarget && anchorTarget instanceof dorado.widget.menu.MenuItem) {
            fixedElement = anchorTarget._dom;
            menu.opener = anchorTarget;
            menu._focusParent = anchorTarget._parent;
            result = $DomUtils.dockAround(dom, fixedElement, options);
            return result;
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, doAfterShow:function (options) {
        var menu = this, focusfirst = (options || {}).focusFirst;
        if (focusfirst) {
            var item = menu.getFocusableItem("first");
            if (item) {
                menu.focusItem(item);
            }
        }
        $invokeSuper.call(this, arguments);
    }, doAfterHide:function () {
        var menu = this, dom = menu._dom;
        if (!dom) {
            return;
        }
        menu.clearFocusItem();
        if (dom) {
            if (menu._overflowing) {
                menu.clearOverflow();
            }
        }
        menu.opener = null;
        $invokeSuper.call(this, arguments);
    }});
})();
(function () {
    var validItemCodes = ["|<", "<", ">", ">|", "goto", "+", "-", "x", "|"];
    var defaultShowTextItemCodes = ["goto", "+", "-", "x"];
    dorado.widget.DataPilot = $extend([dorado.widget.Control, dorado.widget.DataControl], {$className:"dorado.widget.DataPilot", ATTRIBUTES:{className:{defaultValue:"d-data-pilot"}, itemCodes:{defaultValue:"pages", setter:function (p, v) {
        if (this._itemCodes != v) {
            this._itemCodes = v;
            this.compileItemCodes(v);
        }
    }}, height:{independent:true, readOnly:true}}, EVENTS:{onSubControlRefresh:{}, onSubControlAction:{}}, filterDataSetMessage:function (messageCode, arg, data) {
        var b = true;
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
          case dorado.widget.DataSet.MESSAGE_DELETED:
          case dorado.widget.DataSet.MESSAGE_INSERTED:
            b = false;
            break;
        }
        return b;
    }, processDataSetMessage:function (messageCode, arg, data) {
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_REFRESH:
            this.refresh(true);
            break;
          case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
            this.refresh(true);
            break;
          case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
          case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
            this.refresh(true);
            break;
        }
    }, getBindingData:function (options) {
        var realOptions = {firstResultOnly:true, acceptAggregation:true};
        if (typeof options == "String") {
            realOptions.loadMode = options;
        } else {
            dorado.Object.apply(realOptions, options);
        }
        return $invokeSuper.call(this, [realOptions]);
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        if (this._currentItemCodeExpression === undefined) {
            this.compileItemCodes();
        }
        if (this._itemCodeExpression != this._currentItemCodeExpression) {
            this._currentItemCodeExpression = this._itemCodeExpression || null;
            var itemObjects = this._itemObjects, oldItemObjects = itemObjects;
            if (itemObjects) {
                for (var p in itemObjects) {
                    var item = itemObjects[p];
                    item.unrender();
                    this.unregisterInnerControl(item);
                }
            }
            this._itemObjects = itemObjects = {};
            var itemCodes = this._compiledItemCodes;
            if (itemCodes) {
                for (var i = 0; i < itemCodes.length; i++) {
                    var itemCode = itemCodes[i];
                    var item = oldItemObjects ? oldItemObjects[itemCode.key] : null;
                    if (!item) {
                        item = this.createItem(itemCode);
                    }
                    item.render(dom);
                    this.registerInnerControl(item);
                    itemObjects[itemCode.key] = item;
                }
            }
        }
        this.refreshItems();
    }, createItem:function (itemCode) {
        function fireOnActionEvent(code, control) {
            var eventArg = {code:code, control:control, processDefault:true};
            this.fireEvent("onSubControlAction", this, eventArg);
            return eventArg.processDefault;
        }
        var item, pilot = this;
        switch (itemCode.code) {
          case "|<":
            item = new PageButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) 0px -220px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotFirstPage") : null, onClick:function (self) {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.firstPage(dorado._NULL_FUNCTION);
                }
            }});
            break;
          case "<":
            item = new PageButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -40px -220px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotPrevousPage") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.previousPage(dorado._NULL_FUNCTION);
                }
            }});
            break;
          case ">":
            item = new PageButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -60px -220px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotNextPage") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.nextPage(dorado._NULL_FUNCTION);
                }
            }});
            break;
          case ">|":
            item = new PageButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -100px -220px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotLastPage") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.lastPage(dorado._NULL_FUNCTION);
                }
            }});
            break;
          case "goto":
            item = new GotoPageControl({onAction:function (self, arg) {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.gotoPage(arg.pageNo, dorado._NULL_FUNCTION);
                }
            }});
            break;
          case "+":
            item = new ToolBarButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -120px 0px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotInsert") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                var list = pilot.getBindingData();
                if (list instanceof dorado.EntityList) {
                    list.createChild();
                }
            }});
            break;
          case "-":
            item = new ToolBarButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -140px 0px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotDelete") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                dorado.MessageBox.confirm($resource("dorado.baseWidget.DataPilotDeleteConfirm"), function () {
                    var list = pilot.getBindingData();
                    if (list instanceof dorado.EntityList && list.current) {
                        list.current.remove();
                    }
                });
            }});
            break;
          case "x":
            item = new ToolBarButton({icon:itemCode.showIcon ? "url(>skin>common/icons.gif) -280px -220px" : null, caption:itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotCancel") : null, onClick:function () {
                if (!fireOnActionEvent.call(pilot, itemCode.code, self)) {
                    return;
                }
                dorado.MessageBox.confirm($resource("dorado.baseWidget.DataPilotCancelConfirm"), function () {
                    var list = pilot.getBindingData();
                    if (list instanceof dorado.EntityList && list.current) {
                        list.current.cancel();
                    }
                });
            }});
            break;
          case "|":
            item = new dorado.widget.toolbar.Separator();
            break;
        }
        if (item) {
            item.set("style", "float: left");
        }
        return item;
    }, compileItemCodes:function () {
        var itemCodes = this._itemCodes;
        if (itemCodes && itemCodes.indexOf("pages") >= 0) {
            itemCodes = itemCodes.replace("pages", "|<,<,goto,>,>|");
        }
        itemCodes = (itemCodes || "").split(",");
        var compiledItemCodes = this._compiledItemCodes = [], itemCodeExpression = "";
        for (var i = 0; i < itemCodes.length; i++) {
            var itemCode = itemCodes[i], index = itemCode.indexOf("/");
            var code, showIcon = true, showCaption, options = null;
            if (index > 0) {
                code = itemCode.substring(0, index);
                var options = itemCode.substring(index + 1);
                showIcon = (options.indexOf("i") >= 0);
                showCaption = (options.indexOf("c") >= 0);
            } else {
                code = itemCode;
                showCaption = (defaultShowTextItemCodes.indexOf(code) >= 0);
            }
            if (validItemCodes.indexOf(code) >= 0) {
                itemCode = {code:code, showIcon:showIcon, showCaption:showCaption, key:code + "/" + (showIcon ? "i" : "") + (showCaption ? "c" : "") + i};
                compiledItemCodes.push(itemCode);
                if (itemCodeExpression.length > 0) {
                    itemCodeExpression += ",";
                }
                itemCodeExpression += itemCode.key;
            }
        }
        this._itemCodeExpression = itemCodeExpression;
    }, refreshItems:function () {
        if (this._itemObjects) {
            var itemCodes = this._compiledItemCodes;
            if (itemCodes) {
                for (var i = 0; i < itemCodes.length; i++) {
                    this.refreshItem(itemCodes[i]);
                }
            }
        }
    }, refreshItem:function (itemCode) {
        var item = this._itemObjects[itemCode.key];
        if (!item) {
            return;
        }
        var eventArg = {code:itemCode.code, control:item, processDefault:true};
        this.fireEvent("onSubControlRefresh", this, eventArg);
        if (!eventArg.processDefault) {
            return;
        }
        var list = this.getBindingData();
        if (!(list instanceof dorado.EntityList)) {
            list = null;
        }
        var pageNo = list ? list.pageNo : 1, pageCount = list ? list.pageCount : 1;
        var current = list ? list.current : null;
        switch (itemCode.code) {
          case "|<":
            item.set("disabled", pageNo <= 1);
            break;
          case "<":
            item.set("disabled", pageNo <= 1);
            break;
          case ">":
            item.set("disabled", pageNo >= pageCount);
            break;
          case ">|":
            item.set("disabled", pageNo >= pageCount);
            break;
          case "goto":
            item.set({disabled:pageCount == 1, pageNo:pageNo, pageCount:pageCount});
            break;
          case "+":
            item.set("disabled", this._dataSet ? this._dataSet._readOnly : true);
            break;
          case "-":
            item.set("disabled", !current || this._dataSet._readOnly);
            break;
          case "x":
            item.set("disabled", !current || (current.state != dorado.Entity.STATE_MODIFIED && current.state != dorado.Entity.STATE_NEW) || this._dataSet._readOnly);
            break;
        }
    }});
    var PageButton = dorado.widget.SimpleIconButton;
    var ToolBarButton = dorado.widget.toolbar.Button;
    var GotoPageControl = $extend(dorado.widget.Control, {ATTRIBUTES:{className:{defaultValue:"d-goto-page"}, pageNo:{defaultValue:1}, pageCount:{defaultValue:1}, disabled:{}}, EVENTS:{onAction:{interceptor:function (superFire, self, arg) {
        this._pageNo = arg.pageNo;
        return superFire(self, arg);
    }}}, createDom:function (dom) {
        var dom = document.createElement("SPAN");
        var gotoPage = this;
        this._labelPrefix = $DomUtils.xCreateElement({tagName:"SPAN", className:"text", style:"float: left"});
        dom.appendChild(this._labelPrefix);
        var spinner = this._spinner = new dorado.widget.NumberSpinner({min:1, max:1, value:1, showSpinTrigger:false, trigger:{$type:"Trigger", icon:">skin>base/goto-page.gif", onExecute:function (self, arg) {
            spinner.post();
            gotoPage.fireEvent("onAction", gotoPage, {pageNo:spinner.get("value")});
        }}, onKeyDown:function (self, arg) {
            if (arg.keyCode == 13) {
                spinner.post();
                gotoPage.fireEvent("onAction", gotoPage, {pageNo:spinner.get("value")});
            }
            arg.returnValue = true;
        }, width:60, style:"float: left; margin-top: 1px"});
        spinner.render(dom);
        this.registerInnerControl(spinner);
        this._labelSuffix = $DomUtils.xCreateElement({tagName:"SPAN", className:"text", style:"float: left"});
        dom.appendChild(this._labelSuffix);
        return dom;
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var spinner = this._spinner, pageCountChanged = false;
        if (this._pageCount != this._currentPageCount) {
            $fly(this._labelPrefix).text($resource("dorado.baseWidget.DataPilotGotoPagePrefix", this._pageCount));
            $fly(this._labelSuffix).text($resource("dorado.baseWidget.DataPilotGotoPageSuffix", this._pageCount));
            this._currentPageCount = this._pageCount;
            spinner.set("max", this._pageCount);
            pageCountChanged = true;
        }
        spinner.set("value", this._pageNo);
        spinner.set("readOnly", this._disabled);
    }});
})();
dorado.widget.LabelRenderer = $extend(dorado.Renderer, {render:function (dom, arg) {
    dom.innerText = arg.text;
}});
dorado.widget.Label = $extend(dorado.widget.Control, {$className:"dorado.widget.Label", ATTRIBUTES:{className:{defaultValue:"d-label"}, text:{}, renderer:{}}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    (this._renderer || $singleton(dorado.widget.LabelRenderer)).render(dom, {text:this._text});
}});
dorado.widget.DataLabelRenderer = $extend(dorado.Renderer, {render:function (dom, arg) {
    var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : "";
    dom.innerText = text;
}});
dorado.widget.DataLabel = $extend([dorado.widget.Control, dorado.widget.PropertyDataControl], {$className:"dorado.widget.DataLabel", ATTRIBUTES:{className:{defaultValue:"d-data-label"}, width:{defaultValue:100}, renderer:{}}, processDataSetMessage:function (messageCode, arg, data) {
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
        this.refresh(true);
        break;
    }
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var entity = this.getBindingData(true), timestamp;
    if (entity) {
        timestamp = entity.timestamp;
    }
    if (timestamp != this._timestamp) {
        (this._renderer || $singleton(dorado.widget.DataLabelRenderer)).render(dom, {entity:entity, property:this._property});
        this._timestamp = timestamp;
    }
}});
dorado.widget.AbstractEditor = $extend(dorado.widget.Control, {$className:"dorado.widget.AbstractEditor", focusable:true, ATTRIBUTES:{value:{}, entity:{setter:function (p, v) {
    this._entity = v;
    this.refreshData();
}}, property:{setter:function (p, v) {
    this._property = v;
    this.refreshData();
}}, readOnly:{}, supportsDirtyFlag:{defaultValue:true}, modified:{readOnly:true}}, EVENTS:{beforePost:{}, onPost:{}, onPostFailed:{}}, post:function (allowSkip) {
    return this.doPost ? this.doPost() : false;
}, refreshData:function () {
    if (this._property && this._entity) {
        if (this.doRefreshData) {
            this.doRefreshData();
        }
    }
}, doRefreshData:function () {
    var p = this._property, e = this._entity;
    this.set("value", (e instanceof dorado.Entity) ? e.get(p) : e[p]);
    this.setDirty(false);
}, doPost:function () {
    var p = this._property, e = this._entity;
    if (p && e) {
        var v = this.get("value");
        if (e instanceof dorado.Entity) {
            e.set(p, v);
            this.setDirty(e.isDirty(p));
        } else {
            e[p] = v;
            this.setDirty(true);
        }
    }
    return true;
}, setDirty:function (dirty) {
    if (!this._supportsDirtyFlag) {
        return;
    }
    $fly(this.getDom()).toggleClass(this._className + "-dirty", !!dirty);
}});
dorado.widget.AbstractDataEditor = $extend([dorado.widget.AbstractEditor, dorado.widget.PropertyDataControl], {$className:"dorado.widget.AbstractDataEditor", filterDataSetMessage:function (messageCode, arg, data) {
    var b = true;
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        b = false;
        break;
      default:
        b = $invokeSuper.call(this, arguments);
    }
    return b;
}, processDataSetMessage:function (messageCode, arg, data) {
    switch (messageCode) {
      case dorado.widget.DataSet.MESSAGE_REFRESH:
      case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
      case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
      case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
        this.refresh(true);
        break;
    }
}, getBindingInfo:function () {
    if (this._bindingInfoCache && (new Date().getTime() - this._bindingInfoCache.cacheTimestamp) < 50) {
        return this._bindingInfoCache;
    }
    var info = {};
    var entityDataType;
    var entity = info.entity = this.getBindingData("auto");
    if (entity != null) {
        entityDataType = entity.dataType;
        info.timestamp = entity.timestamp;
    }
    if (!entityDataType) {
        entityDataType = this.getBindingDataType();
    }
    info.entityDataType = entityDataType;
    if (entityDataType) {
        info.propertyDef = entityDataType.getPropertyDef(this._property);
        info.dataType = info.propertyDef ? info.propertyDef.get("dataType") : null;
    }
    if (info.timestamp == null) {
        info.timestamp = 0;
    }
    info.cacheTimestamp = new Date().getTime();
    this._bindingInfoCache = info;
    return info;
}, doPost:function () {
    var b = false;
    if (this._property) {
        var entity = this.getBindingData(true);
        if (entity) {
            entity.set(this._property, this.get("value"));
            this.timestamp = entity.timestamp;
            b = true;
        }
    }
    return b;
}});
dorado.widget.Trigger = $extend(dorado.widget.Component, {$className:"dorado.widget.Trigger", focusable:true, ATTRIBUTES:{className:{writeBeforeReady:true}, icon:{writeBeforeReady:true}, iconClass:{writeBeforeReady:true, defaultValue:"d-trigger-icon-custom"}}, EVENTS:{onExecute:{}}, createTriggerButton:function (editor) {
    var trigger = this;
    var control = new dorado.widget.SimpleIconButton({exClassName:"d-trigger " + (trigger._className || ""), icon:trigger._icon, iconClass:trigger._iconClass, onClick:function () {
        editor.onTriggerClick(trigger);
    }});
    jQuery(control.getDom()).addClassOnClick("d-trigger-down", null, function () {
        return !editor.get("readOnly");
    });
    return control;
}, execute:function (editor) {
    this.fireEvent("onExecute", this, {editor:editor});
}});
dorado.widget.DropDown = $extend(dorado.widget.Trigger, {$className:"dorado.widget.DropDown", focusable:true, ATTRIBUTES:{iconClass:{defaultValue:"d-trigger-icon-drop"}, width:{}, minWidth:{defaultValue:20}, maxWidth:{}, height:{}, minHeight:{defaultValue:10}, maxHeight:{defaultValue:400}, autoOpen:{}, postValueOnSelect:{defaultValue:true}, opened:{readOnly:true, getter:function () {
    return !!this._box;
}}, editor:{readOnly:true}, box:{readOnly:true}}, EVENTS:{onOpen:{}, onClose:{}, onValueSelect:{}}, createDropDownBox:function (editor, dropDown) {
    return new dorado.widget.DropDownBox({editor:editor, dropDown:dropDown, hideAnimateType:"none"});
}, initDropDownBox:dorado._NULL_FUNCTION, onEditorFocus:function (editor) {
    if (this._autoOpen) {
        $setTimeout(this, function () {
            this.execute(editor);
        }, 50);
    }
}, onEditorKeyDown:function (editor, evt) {
    dorado.widget.disableKeyBubble = this._editor;
    try {
        return this.doOnEditorKeyPress ? this.doOnEditorKeyPress(evt) : true;
    }
    finally {
        dorado.widget.disableKeyBubble = null;
    }
}, doOnEditorKeyPress:function (evt) {
    var retValue = true;
    if (this.get("opened")) {
        switch (evt.keyCode) {
          case 27:
            this.close();
            retValue = false;
            break;
        }
    }
    return retValue;
}, execute:function (editor) {
    if (this._skipExecute) {
        return;
    }
    this._skipExecute = true;
    $setTimeout(this, function () {
        delete this._skipExecute;
    }, 300);
    (this.get("opened")) ? this.close() : this.open(editor);
    $invokeSuper.call(this, arguments);
}, open:function (editor) {
    function getBoxCache(win) {
        var boxCache;
        try {
            if (win.dorado) {
                boxCache = win.dorado._DROPDOWN_BOX_CACHE;
                if (!boxCache) {
                    win.dorado._DROPDOWN_BOX_CACHE = boxCache = {};
                }
            }
        }
        catch (e) {
        }
        return boxCache;
    }
    if (this._box) {
        this._box.hide();
    }
    this._editor = editor;
    this.fireEvent("onOpen", this, {editor:editor});
    var dropdown = this, editorDom = editor.getDom();
    var win = $DomUtils.getOwnerWindow(editorDom) || window;
    var boxCache = getBoxCache(win);
    var box = boxCache ? boxCache[dorado.id + "$" + this._id] : null;
    if (!box) {
        box = this.createDropDownBox(editor, dropdown);
        box.addListener("onDropDownBoxShow", function () {
            if (dropdown.onDropDownBoxShow) {
                dropdown.onDropDownBoxShow();
            }
        });
        box.render(win.document.body);
        if (boxCache) {
            boxCache[dorado.id + "$" + this._id] = box;
        }
    }
    this._box = box;
    box._onBlurListener = function () {
        dropdown.close();
    };
    editor.addListener("onBlur", box._onBlurListener, {once:true});
    box._focusParent = editor;
    var boxWidth = this._width || $fly(editorDom).outerWidth();
    if (boxWidth > this._maxWidth) {
        boxWidth = this._maxWidth;
    }
    if (boxWidth < this._minWidth) {
        boxWidth = this._minWidth;
    }
    var boxHeight = this._height || 0;
    if (boxHeight > this._maxHeight) {
        boxHeight = this._maxHeight;
    }
    if (boxHeight < this._minHeight) {
        boxHeight = this._minHeight;
    }
    var boxDom = box.getDom(), containerElement = box.getContainerElement();
    with (boxDom.style) {
        left = -screen.availWidth + "px";
        top = 0;
        width = boxWidth + "px";
        height = boxHeight + "px";
        visibility = "hidden";
        display = "";
    }
    this.initDropDownBox(box, editor);
    box.init(editor);
    if (!this._width) {
        boxWidth = (dorado.Browser.mozilla) ? containerElement.firstChild.offsetWidth : containerElement.scrollWidth;
        if (boxWidth > this._maxWidth) {
            boxWidth = this._maxWidth;
        }
        if (boxWidth < this._minWidth) {
            boxWidth = this._minWidth;
        }
    }
    if (!this._height) {
        boxHeight = containerElement.scrollHeight;
        if (boxHeight > this._maxHeight) {
            boxHeight = this._maxHeight;
        }
        if (boxHeight < this._minHeight) {
            boxHeight = this._minHeight;
        }
    }
    boxDom.style.width = boxWidth + "px";
    boxDom.style.height = boxHeight + "px";
    box.show({anchorTarget:editor, editor:editor, align:"innerright", vAlign:"bottom"});
}, close:function (selectedValue) {
    var editor = this._editor;
    var eventArg = {editor:editor, selectedValue:selectedValue, processDefault:true};
    this.fireEvent("onClose", this, eventArg);
    var box = this._box;
    if (!box) {
        return;
    }
    this._box = null;
    this._editor = null;
    var self = this;
    editor.removeListener("onBlur", box._onBlurListener);
    box.hide();
    if (eventArg.selectedValue !== undefined) {
        this.fireEvent("onValueSelect", this, eventArg);
        if (eventArg.processDefault && eventArg.selectedValue !== undefined) {
            var lastPost = editor._lastPost;
            try {
                selectedValue = eventArg.selectedValue;
                if (selectedValue instanceof dorado.Entity || selectedValue instanceof dorado.EntityList) {
                    selectedValue = dorado.Core.clone(selectedValue);
                }
                editor.set("value", selectedValue);
            }
            finally {
                editor._lastPost = lastPost;
            }
            if (this._postValueOnSelect) {
                editor.post();
            }
        }
    }
}});
dorado.widget.DropDown.findDropDown = function (control) {
    var box = control.findParent(dorado.widget.DropDownBox);
    return box ? box.get("dropDown") : null;
};
dorado.widget.DropDownBox = $extend([dorado.widget.Control, dorado.widget.FloatControl], {$className:"dorado.widget.DropDownBox", ATTRIBUTES:{className:{defaultValue:"d-drop-down-box"}, showAnimateType:{defaultValue:"safeSlide"}, hideAnimateType:{defaultValue:"safeSlide"}, visible:{defaultValue:false}, editor:{}, dropDown:{}, control:{writeOnce:true, setter:function (p, control) {
    if (this._control == control) {
        return;
    }
    this._control = control;
    if (control) {
        this.registerInnerControl(control);
    }
}}}, EVENTS:{onDropDownBoxShow:{}}, createDom:function () {
    return $DomUtils.xCreateElement({tagName:"DIV", style:{position:"absolute", display:"none"}, content:{tagName:"DIV", style:{overflow:"visible"}}});
}, init:function (editor) {
    if (this._control) {
        this._control.render(this.getContainerElement());
    }
}, getContainerElement:function () {
    return this.getDom().firstChild;
}, doBeforeShow:function (editor) {
    $invokeSuper.call(this, arguments);
    this.refresh();
}, doAfterShow:function (editor) {
    $invokeSuper.call(this, arguments);
    this.fireEvent("onDropDownBoxShow", this);
}, doBeforeHide:function () {
    $invokeSuper.call(this, arguments);
    this._closed = true;
}});
dorado.widget.View.registerDefaultComponent("triggerClear", function () {
    return new dorado.widget.Trigger({className:"d-trigger-clear", iconClass:"d-trigger-icon-clear", onExecute:function (self, arg) {
        arg.editor.set("text", "");
    }});
});
(function () {
    DropDownFilterTrigger = $extend(dorado.widget.Trigger, {$className:"dorado.widget.DropDownFilterTrigger", ATTRIBUTES:{iconClass:{defaultValue:"d-trigger-icon-filter"}}, execute:function (editor) {
        var dropDown = this._dropDown;
        if (dropDown) {
            dropDown.onFilterItems(editor.get("text"));
        }
    }});
    DropDownResetFilterTrigger = $extend(dorado.widget.Trigger, {$className:"dorado.widget.DropDownResetFilterTrigger", ATTRIBUTES:{iconClass:{defaultValue:"d-trigger-icon-reset"}}, execute:function (editor) {
        var dropDown = this._dropDown;
        if (dropDown) {
            dropDown.onFilterItems();
        }
    }});
    var globalFilterTrigger;
    function getDropDownFilterTrigger() {
        if (!globalFilterTrigger) {
            globalFilterTrigger = new DropDownFilterTrigger();
        }
        return globalFilterTrigger;
    }
    var globalResetFilterTrigger;
    function getDropDownResetFilterTrigger() {
        if (!globalResetFilterTrigger) {
            globalResetFilterTrigger = new DropDownResetFilterTrigger();
        }
        return globalResetFilterTrigger;
    }
    dorado.widget.RowListDropDown = $extend(dorado.widget.DropDown, {$className:"dorado.widget.RowListDropDown", ATTRIBUTES:{property:{}, displayProperty:{}, columns:{writeBeforeReady:true}, dynaFilter:{}, filterOnOpen:{}, filterOnTyping:{defaultValue:true}, minFilterInterval:{defaultValue:300}}, EVENTS:{onFilterItems:{}, onFilterItem:{}}, getSelectedValue:function (rowList) {
        var value = rowList.getCurrentItem();
        if (value && this._property) {
            if (value instanceof dorado.Entity) {
                value = value.get(this._property);
            } else {
                value = value[this._property];
            }
        }
        return value;
    }, createDropDownBox:function (editor, dropDown) {
        var box = $invokeSuper.call(this, arguments);
        box.set("style", {overflow:"hidden"});
        var dropDown = this, rowList;
        if (this._columns) {
            rowList = new dorado.widget.Grid({stretchColumnsMode:"stretchableColumns", columns:this._columns, readOnly:true, style:"border: none", onDataRowClick:function (rowList) {
                dropDown.close(dropDown.getSelectedValue(rowList));
            }});
        } else {
            rowList = new dorado.widget.ListBox({width:"100%", property:this._displayProperty || this._property, style:"border: none", onDataRowClick:function (rowList) {
                dropDown.close(dropDown.getSelectedValue(rowList));
            }});
        }
        box.set("control", rowList);
        return box;
    }, initDropDownData:function (box, editor) {
        var rowList = box.get("control");
        var items = this.getDropDownItems() || [];
        rowList.set("items", items);
        var value = editor.get("value"), currentIndex = -1;
        if (items && value) {
            if (items instanceof Array) {
                if (!this._property) {
                    currentIndex = items.indexOf(value);
                } else {
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        if (item instanceof dorado.Entity) {
                            if (item.get(this._property) == value) {
                                currentIndex = i;
                                break;
                            }
                        } else {
                            if (item[this._property] == value) {
                                currentIndex = i;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (currentIndex >= 0) {
            rowList.set("currentIndex", currentIndex);
        }
    }, initDropDownBox:function (box, editor) {
        $invokeSuper.call(this, arguments);
        if (this.initDropDownData) {
            this.initDropDownData(box, editor);
        }
        var rowList = box.get("control");
        if (!this._height) {
            var useMaxHeight = true, refreshed = false, itemCount = rowList._itemModel.getItemCount();
            if (this._maxHeight && (!itemCount || (this._maxHeight / (rowList._rowHeight + 2) > (itemCount + 1)))) {
                rowList.set({scrollMode:"simple"});
                rowList.refresh();
                refreshed = true;
                var height = $fly(rowList._dom).outerHeight();
                if (height < this._maxHeight) {
                    useMaxHeight = false;
                }
            }
            if (useMaxHeight) {
                rowList.set({height:this._maxHeight, scrollMode:"viewport"});
                rowList.refresh();
                refreshed = true;
            }
            if (!refreshed) {
                rowList.refresh();
            }
        } else {
            rowList.set({height:this._height, scrollMode:"viewport"});
            rowList.refresh();
        }
    }, onDropDownBoxShow:function () {
        var rowList = this.get("box.control");
        var editor = this._editor;
        if (this._dynaFilter && editor instanceof dorado.widget.AbstractTextEditor) {
            var dropDown = this;
            var filterFn = this._box._onTextEditedListener = function () {
                dorado.Toolkits.setDelayedAction(dropDown, "$filterTimeId", function () {
                    delete dropDown._filterTimeId;
                    dropDown.onFilterItems(editor.get("text"));
                }, dropDown._minFilterInterval);
            };
            var text = editor.get("text");
            if (text && text.length > 0 && this._filterOnOpen) {
                filterFn();
            }
            editor.addListener("onTextEdit", function () {
                if (dropDown._filterOnTyping) {
                    filterFn();
                }
            });
        }
    }, open:function (editor) {
        $invokeSuper.call(this, arguments);
        if (this._dynaFilter && !this._filterOnTyping) {
            var triggers = editor.get("trigger");
            if (!(triggers instanceof Array)) {
                triggers = [triggers];
            }
            var resetFilterTrigger = getDropDownResetFilterTrigger();
            var filterTrigger = getDropDownFilterTrigger();
            triggers.insert(resetFilterTrigger);
            triggers.insert(filterTrigger);
            resetFilterTrigger._dropDown = this;
            filterTrigger._dropDown = this;
            editor.set("trigger", triggers);
        }
    }, close:function (selectedValue) {
        var editor = this._editor;
        if (this._dynaFilter && !this._filterOnTyping) {
            var triggers = editor.get("trigger");
            if (!(triggers instanceof Array)) {
                triggers = [triggers];
            }
            var resetFilterTrigger = getDropDownResetFilterTrigger();
            var filterTrigger = getDropDownFilterTrigger();
            triggers.remove(filterTrigger);
            triggers.remove(resetFilterTrigger);
            filterTrigger._dropDown = null;
            resetFilterTrigger._dropDown = null;
            editor.set("trigger", triggers);
        }
        if (editor instanceof dorado.widget.AbstractTextEditor) {
            editor.removeListener("onTextEdit", this._box._onTextEditedListener);
        }
        $invokeSuper.call(this, arguments);
    }, onFilterItems:function (filterValue) {
        if (!this._items) {
            return;
        }
        var rowList = this.get("box.control");
        if (!rowList) {
            return;
        }
        var arg = {filterValue:filterValue, processDefault:true};
        this.fireEvent("onFilterItems", this, arg);
        if (!arg.processDefault) {
            rowList.set("items", this._items);
            return;
        }
        filterValue += "";
        var items;
        if (filterValue && filterValue.length > 0) {
            items = [];
            filterValue = filterValue.toUpperCase();
            var originItems = this._items, property = this._displayProperty || this._property, textLen = filterValue.length;
            var self = this;
            function filterEntity(entity) {
                var s;
                if (property) {
                    s = (entity instanceof dorado.Entity) ? entity.get(property) : entity[property];
                } else {
                    s = entity;
                }
                var accept = (s && (s + "").toUpperCase().substring(0, textLen) == filterValue);
                var arg = {item:entity, accept:accept};
                self.fireEvent("onFilterItem", self, arg);
                accept = arg.accept;
                if (accept) {
                    items.push(entity);
                }
            }
            if (originItems instanceof dorado.EntityList) {
                originItems.each(function (entity) {
                    filterEntity(entity);
                });
            } else {
                jQuery.each(originItems, function (i, entity) {
                    filterEntity(entity);
                });
            }
        } else {
            items = this._items;
        }
        rowList.set("items", items);
    }, doOnEditorKeyPress:function (evt) {
        var retValue = true;
        if (this.get("opened")) {
            var rowList = this.get("box.control");
            switch (evt.keyCode) {
              case 13:
                this.close(this.getSelectedValue(rowList));
                retValue = false;
                break;
              default:
                retValue = rowList.onKeyDown(evt);
            }
        }
        if (retValue) {
            retValue = $invokeSuper.call(this, arguments);
        }
        return retValue;
    }});
    dorado.widget.ListDropDown = $extend(dorado.widget.RowListDropDown, {$className:"dorado.widget.ListDropDown", ATTRIBUTES:{items:{}}, getDropDownItems:function () {
        return this._items;
    }});
    dorado.widget.AutoMappingDropDown = $extend(dorado.widget.RowListDropDown, {$className:"dorado.widget.AutoMappingDropDown", ATTRIBUTES:{useEmptyItem:{}, property:{defaultValue:"value"}}, getDropDownItems:function () {
        var items = this._items, editor = this._editor, pd = editor._propertyDef;
        if (!pd) {
            if (dorado.Object.isInstanceOf(editor, dorado.widget.PropertyDataControl)) {
                var dataType = editor.getBindingDataType();
                if (dataType) {
                    pd = dataType.getPropertyDef(editor.get("property"));
                }
            }
        }
        if (pd) {
            items = pd.get("mapping");
        } else {
            items = $invokeSuper.call(this, arguments);
        }
        if (this._useEmptyItem) {
            items = new dorado.EntityList(items);
            items.insert({key:null, value:null}, "begin");
        }
        return items;
    }});
    dorado.widget.View.registerDefaultComponent("autoMappingDropDown1", function () {
        return new dorado.widget.AutoMappingDropDown();
    });
    dorado.widget.View.registerDefaultComponent("autoMappingDropDown2", function () {
        return new dorado.widget.AutoMappingDropDown({useEmptyItem:true});
    });
})();
dorado.widget.DataSetDropDown = $extend(dorado.widget.RowListDropDown, {$className:"dorado.widget.DataSetDropDown", ATTRIBUTES:{height:{defaultValue:230}, dataSet:{componentReference:true}, dataPath:{}, useDataBinding:{defaultValue:true}, reloadDataOnOpen:{}, dynaFilter:{}, filterOnTyping:{defaultValue:false}}, EVENTS:{onSetFilterParameter:{}}, open:function (editor) {
    var self = this, superClass = $getSuperClass();
    var doOpen = function () {
        self._dataSet.getDataAsync(self._dataPath, function (data) {
            if (!self._useDataBinding) {
                self._items = data;
            }
            superClass.prototype.open.call(self, editor);
        }, "auto");
    };
    if (this._useDataBinding && (this._filterOnOpen || this._reloadDataOnOpen || this._lastFilterValue)) {
        var filterValue = (this._reloadDataOnOpen || this._lastFilterValue) ? undefined : editor.get("text");
        this.onFilterItems(filterValue, doOpen);
        this._lastFilterValue = filterValue;
    } else {
        doOpen();
    }
}, createDropDownBox:function (editor, dropDown) {
    if (this._useDataBinding) {
        var box = new dorado.widget.DropDownBox({editor:editor, dropDown:dropDown, style:{overflow:"hidden"}});
        var dropDown = this, rowList;
        if (this._columns) {
            rowList = new dorado.widget.DataGrid({dataSet:this._dataSet, dataPath:this._dataPath, stretchColumnsMode:"stretchableColumns", columns:this._columns, readOnly:true, style:"border: none", onDataRowClick:function (rowList) {
                dropDown.close(dropDown.getSelectedValue(rowList));
            }});
        } else {
            rowList = new dorado.widget.DataListBox({dataSet:this._dataSet, dataPath:this._dataPath, width:"100%", property:this._displayProperty || this._property, style:"border: none", onDataRowClick:function (rowList) {
                dropDown.close(dropDown.getSelectedValue(rowList));
            }});
        }
        box.set("control", rowList);
        return box;
    } else {
        return $invokeSuper.call(this, arguments);
    }
}, initDropDownData:function (filterValue) {
    if (!this._useDataBinding) {
        $invokeSuper.call(this, arguments);
    }
}, getDropDownItems:function () {
    return this._items;
}, onFilterItems:function (filterValue, callback) {
    if (this._useDataBinding) {
        var arg = {filterValue:filterValue, processDefault:true};
        this.fireEvent("onFilterItems", this, arg);
        if (arg.processDefault) {
            var dataSet = this._dataSet;
            arg = {dataSet:dataSet, filterValue:filterValue, processDefault:true};
            if (this.getListenerCount("onSetFilterParameter") > 0) {
                arg.processDefault = false;
                this.fireEvent("onSetFilterParameter", this, arg);
            }
            if (arg.processDefault) {
                dataSet.set("parameter", filterValue);
            }
            dataSet.clear();
            dataSet.loadAsync(callback);
            this._lastFilterValue = filterValue;
        }
    } else {
        $invokeSuper.call(this, arguments);
    }
}, onDropDownBoxShow:function () {
    var filterOnOpen = this._filterOnOpen;
    this._filterOnOpen = false;
    $invokeSuper.call(this, arguments);
    this._filterOnOpen = filterOnOpen;
}});
dorado.widget.CustomDropDown = $extend(dorado.widget.DropDown, {$className:"dorado.widget.CustomDropDown", ATTRIBUTES:{control:{writeBeforeReady:true, innerComponent:""}}, createDropDownBox:function (editor, dropDown) {
    var box = $invokeSuper.call(this, arguments);
    box.set("control", this._control);
    return box;
}});
(function () {
    function isInputOrTextArea(dom) {
        return ["input", "textarea"].indexOf(dom.tagName.toLowerCase()) >= 0;
    }
    dorado.widget.editor = {};
    dorado.widget.editor.PostException = $extend(dorado.Exception, {$className:"dorado.widget.editor.PostException", constructor:function (messages) {
        $invokeSuper.call(this, [dorado.Toolkits.getTopMessage(messages).text]);
        this.messages = messages;
    }});
    dorado.widget.AbstractTextBox = $extend(dorado.widget.AbstractDataEditor, {$className:"dorado.widget.AbstractTextBox", _triggerChanged:true, ATTRIBUTES:{className:{defaultValue:"d-text-box"}, width:{defaultValue:150, independent:true}, height:{independent:true, readOnly:true}, text:{skipRefresh:true, getter:function (a) {
        return this.doGetText();
    }, setter:function (a, text) {
        this.doSetText(text);
    }}, value:{skipRefresh:true, getter:function (a) {
        var text = this.doGetText();
        if (this._value !== undefined && text === this._valueText) {
            return this._value;
        } else {
            return text || null;
        }
    }, setter:function (a, value) {
        this._value = value;
        var text = this._lastPost = this._valueText = dorado.$String.toText(value);
        this.doSetText(text);
    }}, editable:{defaultValue:true}, readOnly:{}, modified:{getter:function () {
        return (this._focused) ? (this._lastPost == this.doGetText()) : false;
    }}, validationState:{readOnly:true, defaultValue:"none"}, validationMessages:{readOnly:true}, trigger:{componentReference:true, setter:function (p, v) {
        if (v instanceof Array && v.length == 0) {
            v = null;
        }
        this._trigger = v;
        this._triggerChanged = true;
    }}}, EVENTS:{onTextEdit:{}, onTriggerClick:{}, onValidationStateChange:{}}, createDom:function () {
        var textDom = this._textDom = this.createTextDom();
        textDom.style.width = "100%";
        textDom.style.height = "100%";
        var dom = document.createElement("DIV");
        with (dom.style) {
            position = "relative";
            whiteSpace = "nowrap";
            overflow = "hidden";
        }
        dom.appendChild(textDom);
        var self = this;
        jQuery(dom).addClassOnHover(this._className + "-hover", null, function () {
            return !self.get("readOnly");
        });
        if (this._text) {
            this.doSetText(this._text);
        }
        return dom;
    }, refreshTriggerDoms:function () {
        var triggerButtons = this._triggerButtons, triggerButton;
        if (triggerButtons) {
            for (var i = 0; i < triggerButtons.length; i++) {
                triggerButton = triggerButtons[i];
                triggerButton.unrender();
                this.unregisterInnerControl(triggerButton);
            }
        }
        var triggers = this.get("trigger");
        if (triggers) {
            if (!(triggers instanceof Array)) {
                triggers = [triggers];
            }
            var trigger;
            this._triggerButtons = triggerButtons = [];
            for (var i = triggers.length - 1; i >= 0; i--) {
                trigger = triggers[i];
                triggerButton = trigger.createTriggerButton(this);
                triggerButton.set("style", {position:"absolute", top:0, right:0});
                triggerButtons.push(triggerButton);
                this.registerInnerControl(triggerButton);
                triggerButton.render(this._dom);
            }
            this._triggersWidth = -1;
            this.doOnAttachToDocument = this.onResize = this.doOnResize;
            if (this._attached) {
                this.onResize();
            }
        } else {
            this._textDom.style.width = "100%";
            delete this.doOnAttachToDocument;
            delete this.onResize;
        }
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        if (this._dataSet) {
            var value, dirty, timestamp = 0, readOnly;
            this._entity = null;
            if (this._property) {
                readOnly = this._dataSet._readOnly;
                var bindingInfo = this.getBindingInfo(), propertyDef = bindingInfo.propertyDef, state;
                if (propertyDef) {
                    readOnly = (bindingInfo.entity == null) || readOnly || propertyDef._readOnly;
                    if (!this._readOnly2 != !readOnly) {
                        this._readOnly2 = readOnly;
                        this.resetReadOnly();
                    }
                    if (!this._displayFormat) {
                        this._displayFormat = propertyDef._displayFormat;
                    }
                    if (!this._inputFormat) {
                        this._inputFormat = propertyDef._inputFormat;
                    }
                    if (!propertyDef._mapping && !this._dataType) {
                        this._dataType = propertyDef._dataType;
                    }
                }
                timestamp = bindingInfo.timestamp;
                if (bindingInfo.entity instanceof dorado.Entity) {
                    var e = this._entity = bindingInfo.entity;
                    if (this._dataType) {
                        value = e.get(this._property);
                    } else {
                        value = e.getText(this._property);
                    }
                    dirty = e.isDirty(this._property);
                    state = e.getMessageState(this._property);
                }
                if (timestamp != this.timestamp) {
                    this.set("value", value);
                    this.timestamp = timestamp;
                }
                this.doSetValidationState(state);
                this.setDirty(dirty);
            } else {
                readOnly = true;
            }
            if (!this._readOnly2 != !readOnly) {
                this._readOnly2 = readOnly;
                this.resetReadOnly();
            }
        } else {
            var dom = this._dom, textDom = this._textDom, readOnly = this._readOnly || this._readOnly2;
            $fly(dom).toggleClass(this._className + "-readonly", !!readOnly);
        }
        if (this._triggerChanged) {
            this._triggerChanged = false;
            this.refreshTriggerDoms();
        }
    }, validate:function (text) {
        if (this._skipValidate) {
            return null;
        }
        if (this.doValidate) {
            return this.doValidate(text);
        }
    }, doSetValidationState:function (state, messages) {
        state = state || "none";
        if (this._validationState == state) {
            return;
        }
        this._validationState = state;
        this._validationMessages = dorado.Toolkits.trimMessages(messages, "error");
        if (this._rendered) {
            var dom = this._dom, warnCls = this._className + "-warn", errorCls = this._className + "-error";
            $fly(dom).toggleClass(warnCls, state == "warn").toggleClass(errorCls, state == "error");
        }
        this.fireEvent("onValidationStateChange", this);
    }, doOnResize:function () {
        if (this._attached) {
            if (this._triggersWidth < 0) {
                this._triggersWidth = 0;
                var _triggerButtons = this._triggerButtons;
                if (_triggerButtons) {
                    for (var i = 0; i < _triggerButtons.length; i++) {
                        var triggerDom = _triggerButtons[i].getDom();
                        triggerDom.style.right = this._triggersWidth + "px";
                        this._triggersWidth += triggerDom.offsetWidth;
                    }
                }
            }
            var w = this._dom.clientWidth - this._triggersWidth + 1;
            this._textDom.style.width = (w < 0 ? 0 : w) + "px";
        }
    }, setFocus:function () {
        if (this._textDom) {
            this._textDom.focus();
        }
    }, doOnFocus:function () {
        var readOnly = this._readOnly || this._readOnly2;
        if (readOnly || !this._editable) {
            this._textDom.readOnly = true;
        }
        if (readOnly) {
            return;
        }
        this._focusTime = new Date();
        this._lastPost = this._lastObserve = this.doGetText();
        this._editObserverId = $setInterval(this, function () {
            var text = this.get("text");
            if (this._lastObserve != text) {
                this._lastObserve = text;
                this.textEdited();
            }
        }, 50);
        var triggers = this.get("trigger");
        if (triggers) {
            if (!(triggers instanceof Array)) {
                triggers = [triggers];
            }
            var self = this;
            jQuery.each(triggers, function (i, trigger) {
                if (trigger.onEditorFocus) {
                    trigger.onEditorFocus(self);
                }
            });
        }
    }, doOnBlur:function () {
        if (this.get("readOnly")) {
            return;
        }
        clearInterval(this._editObserverId);
        delete this._editObserverId;
        this.post();
    }, resetReadOnly:function () {
        if (!this._rendered) {
            return;
        }
        var readOnly = !!(this._readOnly || this._readOnly2);
        if (this.get("focused") && isInputOrTextArea(this._textDom)) {
            this._textDom.readOnly = readOnly;
        }
        $fly(this.getDom()).toggleClass(this._className + "-readonly", readOnly);
    }, onTriggerClick:function (trigger) {
        $setTimeout(this, function () {
            this._textDom.focus();
        }, 0);
        if (this._readOnly) {
            return;
        }
        if (this.fireEvent("onTriggerClick", this)) {
            trigger.execute(this);
        }
    }, doOnKeyDown:function (evt) {
        var retValue = true;
        var firstTrigger = this.get("trigger");
        if (firstTrigger && firstTrigger instanceof Array) {
            firstTrigger = firstTrigger[0];
        }
        switch (evt.keyCode) {
          case 36:
          case 35:
          case 38:
          case 27:
            if (firstTrigger && firstTrigger.onEditorKeyDown) {
                retValue = firstTrigger.onEditorKeyDown(this, evt);
            }
            break;
          case 40:
            if (firstTrigger) {
                if (firstTrigger.onEditorKeyDown) {
                    retValue = firstTrigger.onEditorKeyDown(this, evt);
                }
                if (evt.altKey) {
                    this.onTriggerClick(firstTrigger);
                    retValue = false;
                }
            }
            break;
          case 13:
            if (firstTrigger) {
                if (firstTrigger.onEditorKeyDown) {
                    retValue = firstTrigger.onEditorKeyDown(this, evt);
                }
            } else {
                var b = this.post();
                retValue = (b === true || b == null);
            }
            break;
          case 113:
            if (firstTrigger) {
                this.onTriggerClick(firstTrigger);
                retValue = false;
            }
            break;
          default:
            if (firstTrigger && firstTrigger.onEditorKeyDown) {
                retValue = firstTrigger.onEditorKeyDown(this, evt);
            }
        }
        return retValue;
    }, textEdited:function () {
        this.fireEvent("onTextEdit", this);
        if (this._dataSet && this._entity && this._property && !this._entity.isDirty()) {
            this._entity.setState(dorado.Entity.STATE_MODIFIED);
        }
    }, setDirty:function (dirty) {
        if (!this._supportsDirtyFlag) {
            return;
        }
        var dirtyFlag = this._dirtyFlag;
        if (dirty) {
            if (!dirtyFlag) {
                this._dirtyFlag = dirtyFlag = $DomUtils.xCreateElement({tagName:"LABEL", className:"dirty-flag"});
                this._dom.appendChild(dirtyFlag);
            }
            dirtyFlag.style.display = "";
        } else {
            if (dirtyFlag) {
                dirtyFlag.style.display = "none";
            }
        }
    }, post:function (force) {
        var bindingMode = (this._dataSet && this._property);
        try {
            var text = this.get("text"), state, result, modified = (this._lastPost != text);
            if ((force || modified || (this._validationState == "none" && text == "") && (new Date() - (this._focusTime || 0)) > 300)) {
                this._lastPost = text;
                var eventArg = {processDefault:true};
                if (force || modified) {
                    this.fireEvent("beforePost", this, eventArg);
                }
                if (eventArg.processDefault === false) {
                    return false;
                }
                var validationResults = this.validate(text);
                if (validationResults) {
                    var topValidationResult = dorado.Toolkits.getTopMessage(validationResults);
                    if (topValidationResult) {
                        state = topValidationResult.state;
                        if (state == "error") {
                            throw new dorado.widget.editor.PostException(validationResults);
                        }
                    }
                }
                if (force || modified) {
                    this.doPost();
                    this.fireEvent("onPost", this);
                    result = true;
                }
            }
            if (result) {
                this.doSetValidationState(state, validationResults);
            }
            return result;
        }
        catch (e) {
            if (e instanceof dorado.widget.editor.PostException) {
                this.doSetValidationState("error", e.messages);
            }
            var eventArg = {exception:e, processDefault:true};
            this.fireEvent("onPostFailed", this, eventArg);
            if (eventArg.processDefault) {
                dorado.Exception.processException(e);
            } else {
                dorado.Exception.removeException(e);
            }
            return false;
        }
    }});
    dorado.widget.AbstractTextEditor = $extend(dorado.widget.AbstractTextBox, {$className:"dorado.widget.AbstractTextEditor", ATTRIBUTES:{value:{skipRefresh:true, getter:function () {
        var text = this.get("text");
        if (this._value !== undefined && text === this._valueText) {
            return this._value;
        } else {
            return text || null;
        }
    }, setter:function (a, value) {
        this._value = value;
        var text = dorado.$String.toText(value);
        this._skipValidateEmpty = true;
        this.validate(text);
        this._text = this._lastPost = this._valueText = text;
        this.doSetText(text);
        this.doSetValidationState(null);
    }}, text:{skipRefresh:true, setter:function (a, text) {
        this.validate(text);
        this._text = this._lastPost = text;
        this.doSetText(text);
        this.doSetValidationState(null);
    }}, blankText:{}, required:{}, minLength:{skipRefresh:true}, maxLength:{skipRefresh:true}, selectTextOnFocus:{defaultValue:true}, validators:{setter:function (attr, value) {
        var validators = [];
        for (var i = 0; i < value.length; i++) {
            var v = value[i];
            if (!(v instanceof dorado.validator.Validator)) {
                v = dorado.Toolkits.createInstance("validator", v, function (type) {
                    return dorado.util.Common.getClassType("dorado.validator." + type + "Validator", true);
                });
            }
            validators.push(v);
        }
        this._validators = validators;
    }}}, createDom:function () {
        var text = this._text, dom = $invokeSuper.call(this, arguments);
        if (!text) {
            this.doSetText("");
        }
        return dom;
    }, doGetText:function () {
        if (this._useBlankText) {
            return "";
        }
        return (this._textDom) ? this._textDom.value : this._text;
    }, doSetText:function (text) {
        this._useBlankText = (!this._focused && text == "" && this._blankText);
        if (this._textDom) {
            if (this._useBlankText) {
                text = this._blankText;
            }
            $fly(this._textDom).toggleClass("blank-text", !!this._useBlankText);
            this._textDom.value = text || "";
        } else {
            this._text = text;
        }
    }, doValidate:function (text) {
        var validationResults = [];
        var skipValidateEmpty = this._skipValidateEmpty;
        this._skipValidateEmpty = false;
        if (!skipValidateEmpty && this._required && text.length == 0) {
            validationResults.push({state:"error", text:$resource("dorado.data.ErrorContentRequired")});
        }
        if (text.length) {
            var validator = $singleton(dorado.validator.LengthValidator);
            validator.set({minLength:this._minLength, maxLength:this._maxLength});
            var results = validator.validate(text);
            if (results) {
                validationResults.concat(results);
            }
            if (this._validators) {
                jQuery.each(this._validators, function (i, validator) {
                    results = validator.validate(text);
                    if (results) {
                        validationResults.concat(results);
                    }
                });
            }
        }
        return validationResults;
    }, doPost:function () {
        var p = this._property, e = this._entity;
        if (!p || !e) {
            return false;
        }
        if (this._dataSet) {
            if (this._dataType) {
                e.set(p, this.get("value"));
            } else {
                e.setText(p, this.get("value"));
            }
            this.timestamp = this._entity.timestamp;
        } else {
            if (e instanceof dorado.Entity) {
                var v = this.get("value");
                if (v instanceof dorado.Entity) {
                    e.set(p, v);
                } else {
                    e.setText(p, this.get("text"));
                }
                this.setDirty(e.isDirty(p));
            } else {
                e[p] = this.get("value");
                this.setDirty(true);
            }
        }
        return true;
    }, doOnFocus:function () {
        $invokeSuper.call(this, arguments);
        if (this._selectTextOnFocus) {
            $setTimeout(this, function () {
                this._textDom.select();
            }, 0);
        }
    }});
    dorado.widget.TextEditor = $extend(dorado.widget.AbstractTextEditor, {$className:"dorado.widget.TextEditor", ATTRIBUTES:{value:{skipRefresh:true, getter:function () {
        var text = (this._editorFocused) ? this.doGetText() : this._text;
        if (this._value !== undefined && text === this._valueText) {
            return this._value;
        } else {
            var dataType = this.get("dataType"), text;
            if (dataType) {
                try {
                    var value = this._value = dataType.parse(text);
                    this._valueText = text;
                    return value;
                }
                catch (e) {
                    dorado.Exception.removeException(e);
                    return null;
                }
            } else {
                return text || null;
            }
        }
    }, setter:function (a, value) {
        this._value = value;
        var t, text;
        var dataType = this.get("dataType");
        if (dataType) {
            text = dataType.toText(value, this._editorFocused ? this._typeFormat : this._displayFormat);
            t = (this._editorFocused) ? text : dataType.toText(value, this._typeFormat);
        } else {
            t = text = dorado.$String.toText(value);
        }
        this._skipValidateEmpty = true;
        this.validate(t);
        this._text = this._lastPost = this._valueText = t;
        this.doSetText(text);
        this.doSetValidationState(null);
    }}, text:{skipRefresh:true, getter:function (a) {
        return ((!this.get("dataType") || this._editorFocused) ? this.doGetText() : this._text) || "";
    }, setter:function (a, text) {
        var t = text;
        var dataType = this.get("dataType");
        if (dataType) {
            try {
                if (!this._editorFocused) {
                    var value = dataType.parse(t, this._typeFormat);
                    t = dataType.toText(value, this._displayFormat);
                }
            }
            catch (e) {
            }
        }
        this.validate(t);
        this._text = this._lastPost = text;
        this.doSetText(t);
        this.doSetValidationState(null);
    }}, dataType:{getter:function () {
        var dataType;
        if (this._dataType) {
            dataType = dorado.LazyLoadDataType.dataTypeGetter.call(this);
        } else {
            if (this._property && this._dataSet) {
                var bindingInfo = this.getBindingInfo();
                if (bindingInfo && !bindingInfo.propertyDef._mapping) {
                    dataType = bindingInfo.dataType;
                }
            }
        }
        return dataType;
    }}, password:{}, displayFormat:{}, typeFormat:{skipRefresh:true}, modified:{getter:function () {
        return (this._editorFocused) ? (this._lastPost == this.doGetText()) : false;
    }}, trigger:{getter:function (p, v) {
        var trigger = this._trigger;
        if (!trigger && this._view) {
            if (this.get("dataType") instanceof dorado.datatype.DateDataType) {
                trigger = this._view.id("defaultDateDropDown");
            }
        }
        return trigger;
    }}}, createTextDom:function () {
        var textDom = document.createElement("INPUT");
        textDom.className = "editor";
        if (this._password) {
            textDom.type = "password";
        }
        if (!(dorado.Browser.msie && dorado.Browser.version < "7")) {
            textDom.style.padding = 0;
        }
        if (dorado.Browser.msie && dorado.Browser.version > "7") {
            textDom.style.top = 0;
            textDom.style.position = "absolute";
        }
        return textDom;
    }, doValidate:function (text) {
        var validationResults;
        try {
            var dataType = this.get("dataType");
            if (dataType) {
                dataType.parse(text, this._typeFormat);
            }
            validationResults = $invokeSuper.call(this, arguments);
        }
        catch (e) {
            dorado.Exception.removeException(e);
            validationResults = [{state:"error", text:dorado.Exception.getExceptionMessage(e)}];
        }
        return validationResults;
    }, doOnFocus:function () {
        var dataType = this.get("dataType");
        if (dataType && this._validationState != "error") {
            var text = dataType.toText(this.get("value"), this._typeFormat);
            this._editorFocused = true;
            this.doSetText(text);
        } else {
            this._editorFocused = true;
            if (this._useBlankText) {
                this.doSetText("");
            }
        }
        $invokeSuper.call(this, arguments);
    }, doOnBlur:function () {
        this._textDom.readOnly = false;
        if (this.get("readOnly")) {
            return;
        }
        this._text = this.doGetText();
        try {
            $invokeSuper.call(this, arguments);
        }
        finally {
            var text, dataType = this.get("dataType");
            if (dataType && this._validationState != "error") {
                text = dataType.toText(this.get("value"), this._displayFormat);
                this._editorFocused = false;
                this.doSetText(text);
            }
        }
    }, doOnKeyPress:function (evt) {
        var dataType = this.get("dataType");
        if (!dataType) {
            return true;
        }
        var b = true, k = (evt.keyCode || evt.which), $d = dorado.DataType;
        switch (dataType._code) {
          case $d.INTEGER:
          case $d.PRIMITIVE_INT:
            b = (k == 44 || k == 45 || (k >= 48 && k <= 57));
            break;
          case $d.FLOAT:
          case $d.PRIMITIVE_FLOAT:
            b = (k == 44 || k == 45 || k == 46 || (k >= 48 && k <= 57));
            break;
        }
        return b;
    }});
})();
(function () {
    dorado.widget.TextArea = $extend(dorado.widget.AbstractTextEditor, {$className:"dorado.widget.TextArea", ATTRIBUTES:{width:{independent:false, defaultValue:150}, height:{independent:false, defaultValue:60}, className:{defaultValue:"d-text-area"}}, createTextDom:function () {
        var textDom = document.createElement("TEXTAREA");
        textDom.className = "textarea";
        if (dorado.Browser.msie && dorado.Browser.version < "8") {
            this.doOnAttachToDocument = this.onResize = this.doOnResize;
        }
        return textDom;
    }, refreshTriggerDoms:function () {
        var triggerButtons = this._triggerButtons, triggerButton, triggerPanel = this._triggerPanel;
        if (triggerButtons && triggerPanel) {
            for (var i = 0; i < triggerButtons.length; i++) {
                triggerButton = triggerButtons[i];
                triggerButton.unrender();
                this.unregisterInnerControl(triggerButton);
            }
        }
        var triggers = this.get("trigger");
        if (triggers) {
            if (!triggerPanel) {
                triggerPanel = this._triggerPanel = $create("DIV");
                triggerPanel.className = "d-trigger-panel";
                this._dom.appendChild(triggerPanel);
            }
            if (!(triggers instanceof Array)) {
                triggers = [triggers];
            }
            var trigger;
            this._triggerButtons = triggerButtons = [];
            for (var i = triggers.length - 1; i >= 0; i--) {
                trigger = triggers[i];
                triggerButton = trigger.createTriggerButton(this);
                triggerButton.set("style", {position:"absolute"});
                triggerButtons.push(triggerButton);
                this.registerInnerControl(triggerButton);
                triggerButton.render(triggerPanel);
            }
            this._triggersArranged = false;
            this.doOnAttachToDocument = this.onResize = this.doOnResize;
        } else {
            if (this._triggerPanel) {
                this._triggerPanel.style.display = "none";
            }
            this._textDom.style.width = "100%";
            delete this.doOnAttachToDocument;
            delete this.onResize;
        }
    }, doOnResize:function () {
        if (this._attached) {
            if (!this._triggersArranged) {
                this._triggersArranged = true;
                var triggerButtons = this._triggerButtons;
                if (triggerButtons) {
                    var bottom = 0;
                    for (var i = 0; i < triggerButtons.length; i++) {
                        var triggerButton = triggerButtons[i], buttonDom = triggerButton.getDom();
                        buttonDom.style.bottom = bottom + "px";
                        bottom += buttonDom.offsetWidth;
                    }
                }
            }
            var w = this._dom.clientWidth;
            if (this._triggerPanel) {
                w -= this._triggerPanel.offsetWidth;
            }
            this._textDom.style.width = (w < 0 ? 0 : w) + "px";
        }
    }, doOnKeyDown:function (evt) {
        if (evt.ctrlKey) {
            return true;
        }
        if (evt.keyCode == 13) {
            return;
        }
        return $invokeSuper.call(this, arguments);
    }, doOnFocus:function () {
        if (this._useBlankText) {
            this.doSetText("");
        }
        $invokeSuper.call(this, arguments);
    }, doOnBlur:function () {
        if (this.get("readOnly")) {
            return;
        }
        try {
            $invokeSuper.call(this, arguments);
        }
        finally {
            this.doSetText(this.doGetText());
        }
    }});
})();
dorado.widget.CheckBox = $extend(dorado.widget.AbstractDataEditor, {$className:"dorado.widget.CheckBox", ATTRIBUTES:{className:{defaultValue:"d-checkbox"}, iconOnly:{writeBeforeReady:true}, onValue:{defaultValue:true}, offValue:{defaultValue:false}, mixedValue:{defaultValue:"mixed"}, caption:{}, value:{defaultValue:false, setter:function (p, v) {
    this._value = v;
    if (this._mixedValue == v) {
        this._checked = null;
    } else {
        this._checked = (this._onValue == v);
    }
}}, checked:{defaultValue:false, getter:function () {
    if (this._value == this._mixedValue) {
        return null;
    }
    return this._value == this._onValue;
}, setter:function (attr, value) {
    var result = value;
    if (value === true) {
        this._value = this._onValue;
    } else {
        if (value == null) {
            this._value = this._mixedValue;
        } else {
            this._value = this._offValue;
            result = false;
        }
    }
    this._checked = result;
}}, triState:{defaultValue:false}}, EVENTS:{onValueChange:{}}, onClick:function () {
    var checkBox = this;
    if (checkBox._readOnly || this._readOnly2) {
        return;
    }
    checkBox._lastPost = checkBox._value;
    if (checkBox._triState) {
        if (checkBox._checked === true) {
            checkBox._checked = null;
        } else {
            if (checkBox._checked === false) {
                checkBox._checked = true;
            } else {
                checkBox._checked = false;
            }
        }
    } else {
        checkBox._checked = !checkBox._checked;
    }
    checkBox._value = checkBox._checked ? checkBox._onValue : (checkBox._checked == null ? checkBox._mixedValue : checkBox._offValue);
    checkBox._dirty = true;
    try {
        checkBox.post();
    }
    catch (e) {
        checkBox._value = checkBox._lastPost;
        if (checkBox._value == checkBox._mixedValue) {
            checkBox._checked = null;
        } else {
            checkBox._checked = !checkBox._checked;
        }
        checkBox._dirty = false;
        throw e;
    }
    checkBox.refresh();
    checkBox.fireEvent("onValueChange", checkBox);
}, refreshDom:function (dom) {
    var checkBox = this, checked = checkBox._checked, caption = checkBox._caption || "";
    if (checkBox._dataSet) {
        var value, readOnly, dirty;
        if (checkBox._property) {
            var bindingInfo = checkBox.getBindingInfo();
            if (bindingInfo.entity instanceof dorado.Entity) {
                value = bindingInfo.entity.get(checkBox._property);
                dirty = bindingInfo.entity.isDirty(checkBox._property);
            }
            readOnly = (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
        } else {
            readOnly = true;
        }
        checkBox._checked = (checkBox == checkBox._onValue);
        checkBox._readOnly2 = readOnly;
        checkBox.setDirty(dirty);
    }
    $invokeSuper.call(this, arguments);
    if (!checkBox._iconOnly) {
        var iconEl = dom.firstChild, captionEl = iconEl.nextSibling;
        if (checked) {
            $fly(iconEl).removeClass("unchecked halfchecked").addClass("checked");
        } else {
            if (checked == null) {
                $fly(iconEl).removeClass("checked unchecked").addClass("halfchecked");
            } else {
                $fly(iconEl).removeClass("checked halfchecked").addClass("unchecked");
            }
        }
        captionEl.innerText = caption;
    } else {
        var hovering = false, cls, $dom = $fly(dom);
        $dom.removeClass("d-checkbox-checked d-checkbox-unchecked d-checkbox-halfchecked");
        if ($dom.hasClass("d-checkbox-unchecked-hover") || $dom.hasClass("d-checkbox-checked-hover") || $dom.hasClass("d-checkbox-halfchecked-hover")) {
            $dom.removeClass("d-checkbox-checked-hover d-checkbox-unchecked-hover d-checkbox-halfchecked-hover");
            hovering = true;
        }
        if (checked) {
            cls = hovering ? "d-checkbox-checked-hover" : "d-checkbox-checked";
            $dom.addClass(cls);
        } else {
            if (checked == null) {
                cls = hovering ? "d-checkbox-halfchecked-hover" : "d-checkbox-halfchecked";
                $dom.addClass(cls);
            } else {
                cls = hovering ? "d-checkbox-unchecked-hover" : "d-checkbox-unchecked";
                $dom.addClass(cls);
            }
        }
    }
}, createDom:function () {
    var checkBox = this, dom;
    checkBox._checked = (checkBox._value == checkBox._onValue);
    if (checkBox._iconOnly) {
        checkBox._className = checkBox._className + "-icononly";
        dom = $DomUtils.xCreateElement({tagName:"div", className:checkBox._className});
        $fly(dom).hover(function () {
            if (!checkBox._readOnly) {
                if (checkBox._checked) {
                    $fly(dom).removeClass("d-checkbox-checked").addClass("d-checkbox-checked-hover");
                } else {
                    if (checkBox._checked == null) {
                        $fly(dom).removeClass("d-checkbox-halfchecked").addClass("d-checkbox-halfchecked-hover");
                    } else {
                        $fly(dom).removeClass("d-checkbox-unchecked").addClass("d-checkbox-unchecked-hover");
                    }
                }
            }
        }, function () {
            $fly(dom).removeClass("d-checkbox-checked-hover d-checkbox-unchecked-hover d-checkbox-halfchecked-hover");
            if (checkBox._checked) {
                $fly(dom).addClass("d-checkbox-checked");
            } else {
                if (checkBox._checked == null) {
                    $fly(dom).addClass("d-checkbox-halfchecked");
                } else {
                    $fly(dom).addClass("d-checkbox-unchecked");
                }
            }
        });
    } else {
        dom = $DomUtils.xCreateElement({tagName:"div", className:checkBox._className, content:[{tagName:"div", className:"icon"}, {tagName:"div", className:"caption", content:checkBox._caption || ""}]});
        jQuery(dom).addClassOnHover(checkBox._className + "-hover", null, function () {
            return !checkBox._readOnly;
        }).addClassOnClick(checkBox._className + "-click", null, function () {
            return !checkBox._readOnly;
        });
    }
    return dom;
}, post:function () {
    try {
        if (!this._dirty) {
            return false;
        }
        var eventArg = {processDefault:true};
        this.fireEvent("beforePost", this, eventArg);
        if (eventArg.processDefault === false) {
            return false;
        }
        this.doPost();
        this._lastPost = this._checked;
        this._dirty = false;
        this.fireEvent("onPost", this);
        return true;
    }
    catch (e) {
        dorado.Exception.processException(e);
    }
}, doOnKeyDown:function (evt) {
    var retValue = true;
    switch (evt.keyCode) {
      case 32:
        this.onClick();
        retValue = false;
        break;
    }
    return retValue;
}});
dorado.widget.RadioButton = $extend(dorado.widget.Control, {$className:"dorado.widget.RadioButton", focusable:true, ATTRIBUTES:{className:{defaultValue:"d-radio"}, text:{}, value:{}, checked:{}, readOnly:{}}, onClick:function () {
    var radioButton = this;
    if (!radioButton._readOnly && !radioButton._readOnly2) {
        if (!radioButton._checked) {
            radioButton._checked = true;
            if (radioButton._radioGroup) {
                radioButton._radioGroup._valueChange(radioButton);
            }
        }
    }
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var radioButton = this, checked = radioButton._checked, text = radioButton._text, jDom;
    if (dom) {
        jDom = jQuery(dom);
        var iconEl = dom.firstChild, textEl = iconEl.nextSibling;
        if (checked) {
            $fly(iconEl).removeClass("unchecked").addClass("checked");
        } else {
            $fly(iconEl).removeClass("checked").addClass("unchecked");
        }
        $fly(textEl).text(text);
    }
}, createDom:function () {
    var radioButton = this, dom, checked = radioButton._checked;
    dom = $DomUtils.xCreateElement({tagName:"div", className:radioButton._className, content:[{tagName:"div", className:"icon"}, {tagName:"span", className:"text", content:radioButton._text}]});
    jQuery(dom).addClassOnHover(radioButton._className + "-hover", null, function () {
        return !radioButton._readOnly && !radioButton._readOnly2;
    }).addClassOnClick(radioButton._className + "-click", null, function () {
        return !radioButton._readOnly && !radioButton._readOnly2;
    });
    return dom;
}, isFocusable:function (deep) {
    return !this._readOnly && !this._readOnly2 && $invokeSuper.call(this, arguments);
}});
dorado.widget.RadioGroup = $extend(dorado.widget.AbstractDataEditor, {ATTRIBUTES:{className:{defaultValue:"d-radiogroup"}, layout:{defaultValue:"vertical"}, radioButtons:{setter:function (p, radioButtons) {
    var radioGroup = this, oldValue = this._radioButtons, dom = radioGroup._dom;
    if (oldValue) {
        radioGroup.clearRadioButtons();
    }
    radioGroup._radioButtons = radioButtons;
    if (radioButtons) {
        for (var i = 0; i < radioButtons.length; i++) {
            var radioButton = radioButtons[i];
            if (!(radioButton instanceof dorado.widget.RadioButton)) {
                radioButtons[i] = radioButton = new dorado.widget.RadioButton(radioButton);
            }
            if (dom) {
                radioButton._radioGroup = radioGroup;
                radioButton._readOnly = radioGroup._readOnly && radioGroup._readOnly2;
                if (radioButton._value == radioGroup._value) {
                    radioGroup.activeRadioButton = radioButton;
                    radioButton._checked = true;
                }
                radioButton.render(dom);
            }
            radioGroup.registerInnerControl(radioButton);
        }
    }
}}, value:{setter:function (attr, value) {
    var radioGroup = this, radioButtons = radioGroup._radioButtons;
    if (radioButtons) {
        var found = false;
        for (var i = 0, j = radioButtons.length; i < j; i++) {
            var radioButton = radioButtons[i];
            if ((value + "") == (radioButton._value + "")) {
                found = true;
                radioGroup._setValue(radioButton);
                break;
            }
        }
        if (!found) {
            radioGroup._setValue(null);
        }
    }
    radioGroup._value = value;
}}, readOnly:{setter:function (attr, value) {
    var radioGroup = this, radioButtons = radioGroup._radioButtons;
    if (radioGroup._readOnly == value) {
        return;
    }
    if (radioButtons) {
        for (var i = 0, j = radioButtons.length; i < j; i++) {
            var radioButton = radioButtons[i];
            radioButton.set("readOnly", value || radioGroup._readOnly2);
        }
    }
    radioGroup._readOnly = value;
}}}, EVENTS:{onValueChange:{}}, addRadioButton:function (radioButton, index) {
    var radioGroup = this, radioButtons = radioGroup._radioButtons, dom = radioGroup._dom, refDom;
    if (!radioButtons) {
        radioButtons = radioGroup._radioButtons = [];
    }
    if (!(radioButton instanceof dorado.widget.RadioButton)) {
        radioButton = new dorado.widget.RadioButton(radioButton);
    }
    if (typeof index == "number") {
        var refButton = radioButtons[index];
        if (refButton) {
            refDom = refButton._dom;
        }
        radioButtons.insert(radioButton, index);
    } else {
        radioButtons.push(radioButton);
    }
    if (dom) {
        radioButton._radioGroup = radioGroup;
        radioButton._readOnly = radioGroup._readOnly && radioGroup._readOnly2;
        if (radioButton._value == radioGroup._value) {
            radioGroup.activeRadioButton = radioButton;
            radioButton._checked = true;
        }
        radioButton.render(dom, refDom);
    }
    radioGroup.registerInnerControl(radioButton);
}, removeRadioButton:function (radioButton) {
    var radioGroup = this, radioButtons = radioGroup._radioButtons, index;
    if (!radioButtons) {
        return;
    }
    if (typeof radioButton == "number") {
        index = radioButton;
        radioButton = radioButtons[radioButton];
        radioGroup.unregisterInnerControl(radioButton);
        radioButton.destroy();
        radioButtons.removeAt(index);
        if (radioGroup.activeRadioButton == radioButton) {
            radioGroup.activeRadioButton = null;
        }
    } else {
        if (radioButton && radioButton.destroy) {
            radioGroup.unregisterInnerControl(radioButton);
            radioButton.destroy();
            if (radioGroup.activeRadioButton == radioButton) {
                radioGroup.activeRadioButton = null;
            }
        }
    }
}, clearRadioButtons:function () {
    var radioGroup = this, radioButtons = radioGroup._radioButtons || [], radioButton;
    for (var i = 0; i < radioButtons.length; i++) {
        radioButton = radioButtons[i];
        radioGroup.unregisterInnerControl(radioButton);
        radioButton.destroy();
    }
    radioGroup._radioButtons = null;
    radioGroup.activeRadioButton = null;
}, _setValue:function (radioButton) {
    var radioGroup = this, value = radioButton ? radioButton._value : null;
    if (radioGroup.activeRadioButton == radioButton) {
        return;
    }
    if (radioGroup.activeRadioButton) {
        radioGroup.activeRadioButton._checked = false;
        radioGroup.activeRadioButton.refresh();
    }
    if (radioButton) {
        radioButton._checked = true;
        radioButton.refresh();
    }
    radioGroup.activeRadioButton = radioButton;
}, _valueChange:function (radioButton) {
    var radioGroup = this, value = radioButton ? radioButton._value : null;
    if (radioGroup.activeRadioButton == radioButton || radioGroup._value == value) {
        return;
    }
    radioGroup._lastPost = radioGroup._value;
    radioGroup._value = value;
    radioGroup._dirty = true;
    try {
        radioGroup.post();
    }
    catch (e) {
        radioGroup._value = radioGroup._lastPost;
        radioGroup._dirty = false;
        if (radioButton) {
            radioButton._checked = false;
            radioButton.refresh();
        }
        throw e;
    }
    this._setValue(radioButton);
    radioGroup.fireEvent("onValueChange", radioGroup);
}, createDom:function () {
    var radioGroup = this, layout = radioGroup._layout, radioButtons = radioGroup._radioButtons;
    var dom = $DomUtils.xCreateElement({tagName:"div", className:radioGroup._className});
    if (radioButtons) {
        for (var i = 0, j = radioButtons.length; i < j; i++) {
            var radioButton = radioButtons[i];
            radioButton._radioGroup = radioGroup;
            radioButton._readOnly = radioGroup._readOnly && radioGroup._readOnly2;
            if (radioButton._value == radioGroup._value) {
                radioGroup.activeRadioButton = radioButton;
                radioButton._checked = true;
            }
            radioButton.render(dom);
        }
    }
    return dom;
}, refreshDom:function (dom) {
    var group = this, layout = group._layout;
    if (group._dataSet) {
        var value, readOnly, dirty;
        if (group._property) {
            var bindingInfo = group.getBindingInfo();
            if (bindingInfo.entity instanceof dorado.Entity) {
                value = bindingInfo.entity.get(group._property);
                dirty = bindingInfo.entity.isDirty(group._property);
            }
            readOnly = (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
            if (!group._radioButtons || !group._radioButtons.length) {
                var radioButtons = [], mapping = bindingInfo.propertyDef._mapping;
                if (mapping) {
                    for (var i = 0; i < mapping.length; i++) {
                        var item = mapping[i];
                        radioButtons.push({value:item.key, text:item.value});
                    }
                }
                if (radioButtons.length) {
                    group.set("radioButtons", radioButtons);
                }
            }
        } else {
            readOnly = true;
        }
        group._readOnlyw = readOnly;
        group.set("value", value);
        group.setDirty(dirty);
    }
    $invokeSuper.call(this, arguments);
    if (layout == "flow") {
        $fly(dom).addClass(group._className + "-flow");
    }
}, doOnKeyDown:function (event) {
    if (event.ctrlKey) {
        return true;
    }
    var group = this, radioButtons = group._radioButtons, activeRadioButton = group.activeRadioButton, activeButtonIndex = activeRadioButton ? radioButtons.indexOf(activeRadioButton) : -1, buttonCount = radioButtons.length, newIndex, newRadioButton, retValue = true;
    switch (event.keyCode) {
      case 38:
      case 37:
        if (activeButtonIndex == 0) {
            newIndex = buttonCount - 1;
        } else {
            if (activeButtonIndex > 0 && activeButtonIndex < buttonCount) {
                newIndex = activeButtonIndex - 1;
            } else {
                newIndex = 0;
            }
        }
        retValue = false;
        break;
      case 39:
      case 40:
        if (activeButtonIndex >= 0 && activeButtonIndex < buttonCount - 1) {
            newIndex = activeButtonIndex + 1;
        } else {
            newIndex = 0;
        }
        retValue = false;
        break;
    }
    newRadioButton = radioButtons[newIndex];
    if (newRadioButton) {
        group._valueChange(newRadioButton);
    }
    return retValue;
}, post:function () {
    if (!this._dirty) {
        return false;
    }
    var eventArg = {processDefault:true};
    this.fireEvent("beforePost", this, eventArg);
    if (eventArg.processDefault === false) {
        return false;
    }
    this.doPost();
    this._lastPost = this._value;
    this._dirty = false;
    this.fireEvent("onPost", this);
    return true;
}});
(function () {
    var DEFAULT_OK_MESSAGES = [{state:"ok"}];
    var DEFAULT_WAITING_MESSAGES = [{state:"waiting"}];
    dorado.widget.DataMessage = $extend([dorado.widget.Control, dorado.widget.PropertyDataControl], {$className:"dorado.widget.DataMessage", ATTRIBUTES:{className:{defaultValue:"d-data-message"}, showIconOnly:{writeBeforeReady:true}, showMultiMessage:{writeBeforeReady:true}, messages:{setter:function (p, messages) {
        this._messages = dorado.Toolkits.trimMessages(messages, "info");
    }}}, processDataSetMessage:function (messageCode, arg, data) {
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_REFRESH:
          case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
          case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
          case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
            this._messages = null;
            this.refresh(true);
            break;
        }
    }, createMessageDom:function () {
        return document.createElement("DIV");
    }, refreshSingleMessageDom:function (dom, message) {
        var state, text;
        if (message) {
            state = message.state;
            text = message.text;
        }
        dom.className = "d-message d-message-" + (state || "none");
        if (!this._showIconOnly) {
            dom.innerText = text || " ";
        } else {
            if (text) {
                dorado.TipManager.initTip(dom, {text:text});
            }
        }
    }, refreshDom:function (dom) {
        $invokeSuper.call(this, arguments);
        var entity, messages = this._messages;
        if (!messages && this._dataSet) {
            var entity = this.getBindingData();
            if (entity instanceof dorado.Entity) {
                messages = entity.getMessages(this._property);
            } else {
                entity = null;
            }
            if (entity && this._property) {
                var state = entity.getValidateState(this._property);
                if (state == "validating") {
                    messages = DEFAULT_WAITING_MESSAGES;
                } else {
                    if (!messages || messages.length == 0) {
                        if (state == "ok") {
                            messages = DEFAULT_OK_MESSAGES;
                        } else {
                            var propertyDef = this.getBindingPropertyDef();
                            if (propertyDef && propertyDef._description) {
                                messages = [{state:"info", text:propertyDef._description}];
                            }
                        }
                    }
                }
            }
        }
        if (!this._showMultiMessage) {
            var message = dorado.Toolkits.getTopMessage(messages);
            var messageDom = dom.firstChild;
            if (!messageDom) {
                messageDom = this.createMessageDom();
                dom.appendChild(messageDom);
            }
            this.refreshSingleMessageDom(messageDom, message);
        } else {
        }
    }});
})();
(function () {
    dorado.widget.YearMonthPicker = $extend(dorado.widget.Control, {_opened:false, focusable:true, ATTRIBUTES:{year:{defaultValue:(new Date).getFullYear()}, month:{defaultValue:0}}, EVENTS:{onPick:{}, onCancel:{}}, refreshYear:function (year) {
        var picker = this, oldYear = picker._year, startYear, doms = picker._doms, yearTable = doms.yearTable, remainder;
        year = year ? year : oldYear;
        remainder = year % 10;
        startYear = year - (remainder == 0 ? 10 : remainder) + 1;
        for (var i = 0; i < 5; i++) {
            for (var j = 0; j < 2; j++) {
                var cell = yearTable.rows[i].cells[j];
                cell.innerText = startYear + i * 2 + j;
            }
        }
        var lastSelectedCell = picker._lastSelectedYearCell, index = year - startYear, newSelectedCell = yearTable.rows[Math.floor(index / 2)].cells[index % 2];
        if (lastSelectedCell) {
            $fly(lastSelectedCell).removeClass("selected");
        }
        if (newSelectedCell) {
            $fly(newSelectedCell).addClass("selected");
        }
        picker._lastSelectedYearCell = newSelectedCell;
    }, refreshMonth:function (month) {
        var picker = this, oldMonth = picker._month, doms = picker._doms, monthTable = doms.monthTable;
        if (isNaN(month)) {
            month = oldMonth ? oldMonth : 0;
        }
        var lastSelectedCell = picker._lastSelectedMonthCell, cell = monthTable.rows[Math.floor(month / 2)].cells[month % 2];
        if (lastSelectedCell) {
            $fly(lastSelectedCell).removeClass("selected");
        }
        if (cell) {
            $fly(cell).addClass("selected");
        }
        picker._lastSelectedMonthCell = cell;
    }, updateDate:function (date, month) {
        var picker = this, year = date;
        if (date instanceof Date) {
            picker.set("year", date.getFullYear());
            picker.set("month", date.getMonth());
        } else {
            if (!isNaN(year) && !isNaN(month)) {
                picker.set("year", year);
                picker.set("month", month);
            }
        }
    }, refreshDom:function () {
        var picker = this;
        picker.refreshYear(picker._year);
        picker.refreshMonth(picker._month);
        $invokeSuper.call(this, arguments);
    }, createDom:function () {
        var monthLabel = $resource("dorado.baseWidget.AllMonths"), month_label = monthLabel.split(",");
        var picker = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:"d-year-month-picker", content:[{tagName:"table", className:"year-table", contextKey:"yearTable", content:[{tagName:"tr", className:"number-row", content:[{tagName:"td"}, {tagName:"td"}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td"}, {tagName:"td"}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td"}, {tagName:"td"}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td"}, {tagName:"td"}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td"}, {tagName:"td"}]}, {tagName:"tr", className:"btn-row", content:[{tagName:"td", align:"center", contextKey:"preaYearCell"}, {tagName:"td", align:"center", contextKey:"nextYearCell"}]}]}, {tagName:"table", className:"month-table", contextKey:"monthTable", content:[{tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[0]}, {tagName:"td", content:month_label[1]}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[2]}, {tagName:"td", content:month_label[3]}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[4]}, {tagName:"td", content:month_label[5]}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[6]}, {tagName:"td", content:month_label[7]}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[8]}, {tagName:"td", content:month_label[9]}]}, {tagName:"tr", className:"number-row", content:[{tagName:"td", content:month_label[10]}, {tagName:"td", content:month_label[11]}]}]}, {tagName:"div", className:"btns-pane", contextKey:"buttonPanel"}]}, null, doms);
        var monthLastOverCell, yearLastOverCell;
        picker._doms = doms;
        if (dorado.Browser.msie && dorado.Browser.version == 6) {
            $fly(doms.monthTable).mousemove(function (event) {
                var position = $DomUtils.getCellPosition(event);
                if (position && position.element && (position.element != monthLastOverCell || !monthLastOverCell)) {
                    if (monthLastOverCell) {
                        $fly(monthLastOverCell).removeClass("hover");
                    }
                    $fly(position.element).addClass("hover");
                    monthLastOverCell = position.element;
                }
            }).mouseout(function () {
                if (monthLastOverCell) {
                    $fly(monthLastOverCell).removeClass("hover");
                }
                monthLastOverCell = null;
            });
        }
        $fly(doms.monthTable).click(function (event) {
            var position = $DomUtils.getCellPosition(event);
            if (position && position.element) {
                picker.set("month", position.row * 2 + position.column);
            }
        });
        if (dorado.Browser.msie && dorado.Browser.version == 6) {
            $fly(doms.yearTable).mousemove(function (event) {
                var position = $DomUtils.getCellPosition(event);
                if (position && position.element && (position.element != yearLastOverCell || !yearLastOverCell)) {
                    if (position.row == 5) {
                        return;
                    }
                    if (yearLastOverCell) {
                        $fly(yearLastOverCell).removeClass("hover");
                    }
                    $fly(position.element).addClass("hover");
                    yearLastOverCell = position.element;
                }
            }).mouseout(function () {
                if (yearLastOverCell) {
                    $fly(yearLastOverCell).removeClass("hover");
                }
                yearLastOverCell = null;
            });
        }
        $fly(doms.yearTable).click(function (event) {
            var position = $DomUtils.getCellPosition(event);
            if (position && position.element) {
                if (position.row == 5) {
                    return;
                }
                picker.set("year", parseInt(position.element.innerHTML, 10));
            }
        });
        var preYearButton = new dorado.widget.SimpleIconButton({iconClass:"prev-year-button", listener:{onClick:function () {
            picker.set("year", picker._year - 10);
        }}});
        var nextYearButton = new dorado.widget.SimpleIconButton({iconClass:"next-year-button", listener:{onClick:function () {
            picker.set("year", picker._year + 10);
        }}});
        preYearButton.render(doms.preaYearCell);
        nextYearButton.render(doms.nextYearCell);
        picker.registerInnerControl(preYearButton);
        picker.registerInnerControl(nextYearButton);
        var okButton = new dorado.widget.Button({caption:$resource("dorado.baseWidget.YMPickerConfirm"), listener:{onClick:function () {
            picker.fireEvent("onPick", picker);
        }}});
        okButton.render(doms.buttonPanel);
        var cancelButton = new dorado.widget.Button({caption:$resource("dorado.baseWidget.YMPickerCancel"), listener:{onClick:function () {
            picker.fireEvent("onCancel", picker);
        }}});
        cancelButton.render(doms.buttonPanel);
        picker.registerInnerControl(okButton);
        picker.registerInnerControl(cancelButton);
        picker.refreshYear();
        picker.refreshMonth();
        return dom;
    }, doOnKeyDown:function (event) {
        var picker = this, year = picker._year, month = picker._month;
        switch (event.keyCode) {
          case 37:
            if (event.ctrlKey) {
                picker.set("month", month == 0 ? 11 : month - 1);
            }
            break;
          case 38:
            if (event.ctrlKey) {
                picker.set("year", year - 1);
            }
            break;
          case 39:
            if (event.ctrlKey) {
                picker.set("month", month == 11 ? 0 : month + 1);
            }
            break;
          case 40:
            if (event.ctrlKey) {
                picker.set("year", year + 1);
            }
            break;
          case 33:
            picker.set("year", year - 10);
            break;
          case 34:
            picker.set("year", year + 10);
            break;
          case 13:
            picker.fireEvent("onPick", picker);
            return false;
          case 27:
            break;
        }
    }});
    dorado.widget.YearMonthDropDown = $extend(dorado.widget.DropDown, {ATTRIBUTES:{width:{defaultValue:260}, iconClass:{defaultValue:"d-trigger-icon-date"}}, EVENTS:{onValueSelected:{interceptor:function (superFire, self, arg) {
        var value = arg.selectedValue;
        arg.selectedValue = [value.year, value.month];
        return superFire(self, arg);
    }}}, createDropDownBox:function (editor, dropDown) {
        var dropDown = this, box = $invokeSuper.call(this, arguments), picker = new dorado.widget.YearMonthPicker({listener:{onPick:function (picker) {
            dropDown.close({year:picker._year, month:picker._month});
        }, onCancel:function () {
            dropDown.close();
        }}});
        box.set("style", {overflow:"hidden", height:190});
        box.set("control", picker);
        return box;
    }, doOnKeyPress:function (event) {
        var picker = this, retValue = true, ymPicker = picker.get("box.control");
        switch (event.keyCode) {
          case 27:
            picker.close();
            retValue = false;
            break;
          case 13:
            ymPicker.fireEvent("onPick", picker);
            retValue = false;
            break;
          default:
            retValue = ymPicker.onKeyDown(event);
        }
        return retValue;
    }, initDropDownBox:function (box, editor) {
    }});
})();
(function () {
    var DateHelper = {getWeekCountOfYear:function (date) {
        var begin = new Date(date.getFullYear(), 0, 1);
        var day = begin.getDay();
        if (day == 0) {
            day = 7;
        }
        var duration = date.getTime() - begin.getTime() + (day - 1) * 3600 * 24 * 1000;
        return Math.ceil(duration / 3600 / 24 / 1000 / 7);
    }, getDayCountOfMonth:function (year, month) {
        if (month == 3 || month == 5 || month == 8 || month == 10) {
            return 30;
        }
        if (month == 1) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
        return 31;
    }, getFirstDayOfMonth:function (date) {
        var temp = new Date(date.getTime());
        temp.setDate(1);
        return temp.getDay();
    }, getDayPosition:function (date) {
        if (date instanceof Date) {
            var day = DateHelper.getFirstDayOfMonth(date), dayIndex = day + date.getDate(), columnIndex = dayIndex % 7 == 0 ? 7 : dayIndex % 7;
            return {"rowIndex":(columnIndex == 0 ? Math.floor(dayIndex / 7) : Math.floor(dayIndex / 7) + 1), "columnIndex":dayIndex % 7 == 0 ? 7 : dayIndex % 7};
        }
    }};
    dorado.widget.DatePicker = $extend(dorado.widget.Control, {focusable:true, ATTRIBUTES:{date:{defaultValue:new Date()}, showTimeSpinner:{defaultValue:false}}, EVENTS:{onPick:{}, onCancel:{}}, setYear:function (year, refresh, animate) {
        var picker = this, date = picker._date, oldDay = date.getDate(), oldMonth = date.getMonth(), oldYear = date.getFullYear();
        var source = new Date(date.getTime());
        if (year == "prev") {
            year = oldYear - 1;
        } else {
            if (year == "next") {
                year = oldYear + 1;
            }
        }
        if (oldMonth == 1 && oldDay == 29) {
            var newMonthDay = DateHelper.getDayCountOfMonth(year, date.getMonth());
            if (oldDay > newMonthDay) {
                date.setDate(newMonthDay);
            }
        }
        date.setFullYear(year);
        if (refresh) {
            if (animate === false) {
                picker.refresh();
            } else {
                picker.doMonthAnimate(source, date);
            }
        }
    }, getMonthAnimateType:function (source, target) {
        if (source && target) {
            var syear = source.getFullYear(), tyear = target.getFullYear(), smonth = source.getMonth(), tmonth = target.getMonth();
            if (syear == tyear && smonth == tmonth) {
                return null;
            }
            if (syear == tyear) {
                return smonth > tmonth ? "r2l" : "l2r";
            } else {
                return syear > tyear ? "t2b" : "b2t";
            }
        }
        return null;
    }, doMonthAnimate:function (source, target) {
        var picker = this, animateType = picker.getMonthAnimateType(source, target);
        if (picker._doMonthAnimating) {
            return;
        }
        if (animateType == null) {
            picker.refresh();
            return;
        }
        var dateTable = picker._doms.dateTable, dateBlock = picker._doms.dateBlock, dateTableWidth = dateBlock.offsetWidth, dateTableHeight = dateBlock.offsetHeight;
        var sourceRegion, targetRegion, style, animConfig;
        switch (animateType) {
          case "l2r":
            sourceRegion = 1;
            targetRegion = 2;
            style = {width:dateTableWidth * 2, left:0, top:0};
            animConfig = {left:-1 * dateTableWidth};
            break;
          case "r2l":
            sourceRegion = 2;
            targetRegion = 1;
            style = {width:dateTableWidth * 2, left:-1 * dateTableWidth, top:0};
            animConfig = {left:0};
            break;
          case "b2t":
            sourceRegion = 1;
            targetRegion = 3;
            style = {width:dateTableWidth * 2, left:0, top:0};
            animConfig = {top:-1 * dateTableHeight};
            break;
          case "t2b":
            sourceRegion = 3;
            targetRegion = 1;
            style = {width:dateTableWidth * 2, left:0, top:-1 * dateTableHeight};
            animConfig = {top:0};
            break;
        }
        picker.refreshDate(source, sourceRegion);
        picker.refreshDate(target, targetRegion);
        $fly(dateTable).css(style);
        picker._visibleDateRegion = targetRegion;
        picker._doMonthAnimating = true;
        $fly(dateTable).animate(animConfig, {complete:function () {
            picker._doMonthAnimating = false;
            picker.refreshYearMonth();
        }});
    }, setMonth:function (month, refresh, animate) {
        var picker = this, date = picker._date, oldDay = date.getDate(), oldYear = date.getFullYear(), oldMonth = date.getMonth();
        var source = new Date(date.getTime());
        if (month == "prev") {
            if (oldMonth != 0) {
                month = oldMonth - 1;
            } else {
                picker.setYear(oldYear - 1);
                month = 11;
            }
        } else {
            if (month == "next") {
                if (oldMonth != 11) {
                    month = oldMonth + 1;
                } else {
                    picker.setYear(oldYear + 1);
                    month = 0;
                }
            }
        }
        if (oldDay >= 29) {
            var newMonthDay = DateHelper.getDayCountOfMonth(oldYear, month);
            if (oldDay > newMonthDay) {
                date.setDate(newMonthDay);
            }
        }
        date.setMonth(month);
        if (refresh) {
            if (animate === false) {
                picker.refresh();
            } else {
                picker.doMonthAnimate(source, date);
            }
        }
    }, setDate:function (day, refresh) {
        var picker = this, date = picker._date;
        switch (day) {
          case "next":
            date.setDate(date.getDate() + 1);
            break;
          case "prev":
            date.setDate(date.getDate() - 1);
            break;
          case "nextweek":
            date.setDate(date.getDate() + 7);
            break;
          case "prevweek":
            date.setDate(date.getDate() - 7);
            break;
          default:
            if (!isNaN(day)) {
                date.setDate(day);
            }
            break;
        }
        if (refresh) {
            picker.refresh();
        }
    }, refreshDate:function (target, region) {
        var picker = this, doms = picker._doms, date = target || picker._date, count = 1, day = DateHelper.getFirstDayOfMonth(date), maxDay = DateHelper.getDayCountOfMonth(date.getFullYear(), date.getMonth()), dateTable = doms.dateTable, selectDay = date.getDate(), lastMonthDay = DateHelper.getDayCountOfMonth(date.getFullYear(), (date.getMonth() == 0 ? 11 : date.getMonth() - 1));
        day = (day == 0 ? 7 : day);
        var startI = 0, startJ = 0;
        region = region || picker._visibleDateRegion;
        switch (region) {
          case 2:
            startJ = 7;
            break;
          case 3:
            startI = 6;
            break;
          case 4:
            startI = 6;
            startJ = 7;
            break;
        }
        for (var i = startI; i < startI + 6; i++) {
            for (var j = startJ; j < startJ + 7; j++) {
                var cell = dateTable.rows[i].cells[j];
                if (i == startI) {
                    if (j - startJ >= day) {
                        if (count == selectDay) {
                            cell.className = "selected-date";
                        } else {
                            cell.className = null;
                        }
                        cell.innerHTML = count++;
                    } else {
                        cell.innerHTML = lastMonthDay - (day - j) + 1;
                        cell.className = "pre-month";
                    }
                } else {
                    if (count <= maxDay) {
                        if (count == selectDay) {
                            cell.className = "selected-date";
                        } else {
                            cell.className = null;
                        }
                        cell.innerHTML = count++;
                    } else {
                        cell.innerHTML = count++ - maxDay;
                        cell.className = "next-month";
                    }
                }
            }
        }
    }, refreshYearMonth:function () {
        var picker = this, doms = picker._doms, date = picker._date;
        doms.yearMonthLabel.innerHTML = (date.getMonth() + 1) + "&nbsp;&nbsp;" + date.getFullYear();
    }, refreshSpinner:function () {
        var picker = this, spinner = picker._timeSpinner, date = picker._date;
        if (picker._showTimeSpinner && spinner) {
            spinner.set({hours:date.getHours(), minutes:date.getMinutes(), seconds:date.getSeconds()});
        }
    }, refreshDom:function (dom) {
        var picker = this;
        picker.refreshDate();
        picker.refreshYearMonth();
        if (picker._showTimeSpinner) {
            picker.doShowTimeSpinner();
            picker.refreshSpinner();
        } else {
            picker.doHideTimeSpinner();
        }
        $invokeSuper.call(this, arguments);
    }, createDom:function () {
        var allWeeks = $resource("dorado.baseWidget.AllWeeks"), weeks = allWeeks.split(",");
        var dateRows = [];
        for (var i = 0; i < 12; i++) {
            dateRows.push({tagName:"tr", content:[{tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}, {tagName:"td"}]});
        }
        var picker = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:"d-date-picker", content:[{tagName:"div", className:"year-month-block", content:[{tagName:"div", className:"pre-button-div", contextKey:"prevButtonDiv"}, {tagName:"div", className:"next-button-div", contextKey:"nextButtonDiv"}, {tagName:"div", className:"year-month-label", contextKey:"yearMonthLabel"}]}, {tagName:"table", cellPadding:0, cellSpacing:0, border:0, className:"date-header", contextKey:"dateHeader", content:[{tagName:"tr", className:"header", content:[{tagName:"td", content:weeks[0]}, {tagName:"td", content:weeks[1]}, {tagName:"td", content:weeks[2]}, {tagName:"td", content:weeks[3]}, {tagName:"td", content:weeks[4]}, {tagName:"td", content:weeks[5]}, {tagName:"td", content:weeks[6]}]}]}, {tagName:"div", className:"date-block", contextKey:"dateBlock", content:{tagName:"table", cellPadding:0, cellSpacing:0, border:0, className:"date-table", contextKey:"dateTable", content:dateRows}}, {tagName:"div", contextKey:"buttonBlock", className:"button-block"}, {tagName:"div", contextKey:"spinnerBlock", className:"spinner-block"}]}, null, doms);
        picker._doms = doms;
        var todayButton = new dorado.widget.Button({caption:$resource("dorado.baseWidget.DatePickerToday"), listener:{onClick:function () {
            picker.set("date", new Date());
        }}});
        todayButton.render(doms.buttonBlock);
        var clearButton = new dorado.widget.Button({caption:$resource("dorado.baseWidget.DatePickerClear"), listener:{onClick:function () {
            picker.fireEvent("onPick", picker, null);
        }}});
        clearButton.render(doms.buttonBlock);
        var okButton = new dorado.widget.Button({caption:$resource("dorado.baseWidget.DatePickerConfirm"), listener:{onClick:function () {
            picker.fireEvent("onPick", picker, picker._date);
        }}});
        okButton.render(doms.buttonBlock);
        picker.registerInnerControl(okButton);
        picker.registerInnerControl(clearButton);
        picker.registerInnerControl(todayButton);
        var lastOverCell = null, dateTable = doms.dateTable;
        if (dorado.Browser.msie && dorado.Browser.version == 6) {
            $fly(dateTable).mousemove(function (event) {
                var position = $DomUtils.getCellPosition(event);
                if (position && position.element && (position.element != lastOverCell || !lastOverCell)) {
                    if (position.row <= 0) {
                        return;
                    }
                    if (lastOverCell) {
                        $fly(lastOverCell).removeClass("hover");
                    }
                    $fly(position.element).addClass("hover");
                    lastOverCell = position.element;
                }
            }).mouseout(function () {
                if (lastOverCell) {
                    $fly(lastOverCell).removeClass("hover");
                }
                lastOverCell = null;
            });
        }
        $fly(dateTable).click(function (event) {
            var position = $DomUtils.getCellPosition(event), element = position.element, date = picker._date;
            if (position && element) {
                var className = element.className;
                if (className.indexOf("next-month") != -1) {
                    picker.setMonth(date.getMonth() + 1);
                } else {
                    if (className.indexOf("pre-month") != -1) {
                        picker.setMonth(date.getMonth() - 1);
                    }
                }
                picker.setDate(parseInt(element.innerHTML, 10), true);
                picker.fireEvent("onPick", picker, date);
            }
        });
        var prevYearButton = new dorado.widget.SimpleIconButton({iconClass:"pre-year-button", listener:{onClick:function () {
            picker.setYear("prev", true);
        }}});
        var prevMonthButton = new dorado.widget.SimpleIconButton({iconClass:"pre-month-button", listener:{onClick:function () {
            picker.setMonth("prev", true);
        }}});
        prevYearButton.render(doms.prevButtonDiv);
        prevMonthButton.render(doms.prevButtonDiv);
        var nextMonthButton = new dorado.widget.SimpleIconButton({iconClass:"next-month-button", listener:{onClick:function () {
            picker.setMonth("next", true);
        }}});
        var nextYearButton = new dorado.widget.SimpleIconButton({iconClass:"next-year-button", listener:{onClick:function () {
            picker.setYear("next", true);
        }}});
        nextMonthButton.render(doms.nextButtonDiv);
        nextYearButton.render(doms.nextButtonDiv);
        picker.registerInnerControl(prevYearButton);
        picker.registerInnerControl(prevMonthButton);
        picker.registerInnerControl(nextMonthButton);
        picker.registerInnerControl(nextYearButton);
        $fly(doms.yearMonthLabel).click(function () {
            picker.showYMPicker();
        });
        picker._visibleDateRegion = 1;
        return dom;
    }, doShowTimeSpinner:function () {
        var picker = this, spinner = picker._timeSpinner;
        if (!spinner) {
            spinner = picker._timeSpinner = new dorado.widget.DateTimeSpinner({type:"time", width:100, listener:{onPost:function () {
                var date = picker._date;
                date.setHours(spinner.get("hours"));
                date.setMinutes(spinner.get("minutes"));
                date.setSeconds(spinner.get("seconds"));
            }}});
            spinner.render(picker._doms.spinnerBlock);
            picker.registerInnerControl(spinner);
        }
        $fly(spinner._dom).css("display", "");
    }, doHideTimeSpinner:function () {
        var picker = this, spinner = picker._timeSpinner;
        if (spinner) {
            $fly(spinner._dom).css("display", "none");
        }
    }, showYMPicker:function () {
        var picker = this, dom = picker._dom, ymPicker = picker._yearMonthPicker;
        if (!ymPicker && dom) {
            ymPicker = picker._yearMonthPicker = new dorado.widget.YearMonthPicker({style:{display:"none"}, listener:{onPick:function () {
                var ymPicker = picker._yearMonthPicker, year = ymPicker._year, month = ymPicker._month;
                picker.setYear(year, false, false);
                picker.setMonth(month, true, false);
                picker.hideYMPicker();
            }, onCancel:function () {
                picker.hideYMPicker();
            }}});
            ymPicker.render(dom);
            picker.registerInnerControl(ymPicker);
        }
        ymPicker.updateDate(picker._date);
        if (!ymPicker._rendered) {
            ymPicker.render(document.body);
        }
        ymPicker._opened = true;
        $fly(ymPicker._dom).css("display", "").slideIn("t2b");
    }, hideYMPicker:function (animate) {
        var picker = this, ymPicker = picker._yearMonthPicker;
        if (ymPicker) {
            if (animate === false) {
                $fly(ymPicker._dom).css("display", "none");
            } else {
                $fly(ymPicker._dom).slideOut("b2t");
            }
            ymPicker._opened = false;
            dorado.widget.setFocusedControl(picker);
        }
    }, doOnKeyDown:function (event) {
        var picker = this, date, ymPicker = picker._yearMonthPicker;
        if (ymPicker && ymPicker._opened) {
            ymPicker.onKeyDown(event);
        } else {
            date = picker._date;
            switch (event.keyCode) {
              case 89:
                if (event.ctrlKey) {
                    picker.showYMPicker();
                }
                break;
              case 37:
                if (!event.ctrlKey) {
                    picker.setDate("prev", true);
                } else {
                    picker.setMonth("prev", true);
                }
                break;
              case 38:
                if (!event.ctrlKey) {
                    picker.setDate("prevweek", true);
                } else {
                    picker.setYear("prev", true);
                }
                break;
              case 39:
                if (!event.ctrlKey) {
                    picker.setDate("next", true);
                } else {
                    picker.setMonth("next", true);
                }
                break;
              case 40:
                if (!event.ctrlKey) {
                    picker.setDate("nextweek", true);
                } else {
                    picker.setYear("next", true);
                }
                break;
              case 13:
                return true;
              case 27:
                if (ymPicker && ymPicker._opened) {
                    return false;
                }
                break;
            }
        }
    }});
    dorado.widget.DateDropDown = $extend(dorado.widget.DropDown, {ATTRIBUTES:{width:{defaultValue:260}, iconClass:{defaultValue:"d-trigger-icon-date"}}, createDropDownBox:function (editor, dropDown) {
        var dropDown = this, box = $invokeSuper.call(this, arguments), picker = new dorado.widget.DatePicker({listener:{onPick:function (picker, value) {
            dropDown.close(value);
        }, onCancel:function () {
            dropDown.close();
        }}});
        box.set("style", {overflow:"hidden", height:200});
        box.set("control", picker);
        return box;
    }, doOnKeyPress:function (evt) {
        var dropDown = this, retValue = true, datePicker = this.get("box.control"), ymPicker = datePicker._yearMonthPicker;
        switch (evt.keyCode) {
          case 27:
            dropDown.close();
            retValue = false;
            break;
          case 13:
            if (!ymPicker || !ymPicker._opened) {
                datePicker.fireEvent("onPick", datePicker, datePicker._date);
                retValue = false;
            }
            break;
          default:
            retValue = datePicker.onKeyDown(evt);
            break;
        }
        return retValue;
    }, initDropDownBox:function (box, editor) {
        var dropDown = this, datePicker = dropDown.get("box.control");
        if (datePicker) {
            var date = editor.get("value");
            if (date) {
                datePicker.set("date", date);
            }
            if (datePicker._yearMonthPicker && datePicker._yearMonthPicker._opened) {
                datePicker.hideYMPicker(false);
            }
        }
    }});
    dorado.widget.View.registerDefaultComponent("defaultDateDropDown", function () {
        return new dorado.widget.DateDropDown();
    });
})();
(function () {
    var specialFormConfigProps = ["width", "height", "className", "exClassName"];
    var DEFAULT_OK_MESSAGES = [{state:"ok"}];
    dorado.widget.FormConfig = $class({$className:"dorado.widget.FormConfig", ATTRIBUTES:{width:{}, height:{}, className:{}, exClassName:{}, type:{writeBeforeReady:true}, trigger:{}, entity:{}, labelSeparator:{defaultValue:":"}, showLabel:{defaultValue:true}, labelWidth:{defaultValue:80}, labelSpacing:{defaultValue:3}, labelPosition:{writeBeforeReady:true}, labelAlign:{writeBeforeReady:true}, editorWidth:{}, showHint:{writeBeforeReady:true, defaultValue:true}, hintWidth:{defaultValue:22}, hintSpacing:{defaultValue:3}, showHintMessage:{writeBeforeReady:true}, hintPosition:{writeBeforeReady:true}, readOnly:{}}});
    dorado.widget.FormProfile = $extend([dorado.widget.Component, dorado.widget.FormConfig], {$className:"dorado.widget.FormProfile", ATTRIBUTES:{entity:{defaultValue:function () {
        return {};
    }}, dataSet:{componentReference:true, writeBeforeReady:true}, dataPath:{writeBeforeReady:true}}, constructor:function () {
        this._bindingElements = new dorado.ObjectGroup();
        $invokeSuper.call(this, arguments);
        this.addListener("onAttributeChange", function (self, arg) {
            var attr = arg.attribute;
            if (!dorado.widget.Control.prototype.ATTRIBUTES[attr] && dorado.widget.FormConfig.prototype.ATTRIBUTES[attr]) {
                dorado.Toolkits.setDelayedAction(self, "$profileChangeTimerId", function () {
                    var value = arg.value;
                    self._bindingElements.invoke("onProfileChange");
                }, 20);
            }
        });
    }});
    dorado.widget.FormElement = $extend([dorado.widget.Control, dorado.widget.FormConfig], {$className:"dorado.widget.FormElement", ATTRIBUTES:{width:{defaultValue:260, writeBeforeReady:true, independent:true}, height:{writeBeforeReady:true}, className:{defaultValue:"d-form-element"}, formProfile:{componentReference:true, setter:function (p, formProfile) {
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
        }
    }}, dataSet:{setter:function (p, v) {
        this._dataSet = v;
        delete this._propertyDef;
    }}, dataPath:{setter:function (p, v) {
        this._dataPath = v;
        delete this._propertyDef;
    }}, property:{writeBeforeReady:true}, label:{}, hint:{setter:function (p, hint) {
        function trimSingleHint(hint) {
            if (!hint) {
                return null;
            }
            if (typeof hint == "string") {
                hint = [{level:"info", text:hint}];
            } else {
                hint.level = hint.level || "info";
                hint = [hint];
            }
            return hint;
        }
        function trimHints(hint) {
            if (!hint) {
                return null;
            }
            if (hint instanceof Array) {
                var array = [];
                for (var i = 0; i < hint.length; i++) {
                    var h = trimSingleHint(hint[i]);
                    if (!h) {
                        continue;
                    }
                    array.push(h);
                }
                hint = (array.length) ? array : null;
            } else {
                hint = trimSingleHint(hint);
            }
            return hint;
        }
        this._hint = trimHints(hint);
    }}, editor:{writeBeforeReady:true, innerComponent:"TextEditor"}, value:{path:"editor.value"}, readOnly:{setter:function (p, v) {
        this._readOnly = v;
        this.resetEditorReadOnly();
    }}}, render:function (containerElement, nextChildElement) {
        if (!this._ready) {
            if (!this._formProfile) {
                var view = this.get("view") || dorado.widget.View.TOP;
                this.set("formProfile", view.id("defaultFormProfile"));
            }
            this.onProfileChange();
        }
        $invokeSuper.call(this, arguments);
    }, destroy:function () {
        if (this._destroyed) {
            return;
        }
        this.set("formProfile", null);
        $invokeSuper.call(this, arguments);
    }, createDom:function () {
        var labelConfig, editorConfig = {tagName:"DIV", className:"form-editor"};
        if (this._labelPosition == "top") {
            labelConfig = {tagName:"DIV", className:"form-label form-label-top", style:{position:"absolute"}, };
            if (this._labelAlign == "right") {
                labelConfig.style.right = 0;
            } else {
                labelConfig.style.left = 0;
            }
            editorConfig.style = {width:"100%", overflow:"hidden"};
        } else {
            labelConfig = {tagName:"DIV", className:"form-label form-label-left" + ((this._labelAlign == "right") ? " form-label-align-right" : ""), style:{position:"absolute", top:0, width:this._labelWidth}};
            editorConfig.style = {paddingLeft:this._labelWidth + this._labelSpacing + "px", height:"100%", overflow:"hidden"};
        }
        if (!this._showLabel) {
            labelConfig.style.display = "none";
        }
        if (this._showHint) {
            var hintConfig = {tagName:"DIV", className:"form-hint"};
            editorConfig.content = [hintConfig];
            if (this._hintPosition == "bottom") {
                hintConfig.style = {width:"100%"};
            } else {
                hintConfig.style = {position:"absolute", right:0, top:0};
            }
        }
        var dom = $DomUtils.xCreateElement({tagName:"DIV", content:[labelConfig, editorConfig], style:{position:"relative", overflow:"hidden"}});
        this._labelEl = dom.firstChild;
        this._editorEl = dom.lastChild;
        this._hintEl = this._editorEl.firstChild;
        return dom;
    }, createEditor:function (type) {
        switch (type) {
          case "textArea":
            return new dorado.widget.TextArea();
          case "checkBox":
            return new dorado.widget.CheckBox();
          case "radioGroup":
            return new dorado.widget.RadioGroup({layout:"flow"});
          default:
            return new dorado.widget.TextEditor();
        }
    }, getEditor:function () {
        function getRadioButtons(propertyDef) {
            var radioButtons = [], mapping = propertyDef._mapping;
            if (mapping) {
                for (var i = 0; i < mapping.length; i++) {
                    var item = mapping[i];
                    radioButtons.push({value:item.key, text:item.value});
                }
            }
            return radioButtons;
        }
        var control = this._editor, shouldInitControl = true;
        if (!control) {
            var propertyDef = this.getBindingPropertyDef();
            if (propertyDef) {
                if (!this._type) {
                    var propertyDataType = propertyDef.get("dataType");
                    if (propertyDataType) {
                        if (propertyDataType._code == dorado.DataType.PRIMITIVE_BOOLEAN || propertyDataType._code == dorado.DataType.BOOLEAN) {
                            this._type = (!propertyDef._mapping) ? "checkBox" : "radioGroup";
                        }
                    }
                }
                if (propertyDef._mapping) {
                    if ((!this._type || this._type == "textEditor")) {
                        this._trigger = new dorado.widget.AutoMappingDropDown({items:propertyDef._mapping});
                    }
                }
            }
            this._editor = control = this.createEditor(this._type);
            if (control) {
                var config = {};
                this.initEditorConfig(config);
                control.set(config, {skipUnknownAttribute:true, tryNextOnError:true});
            }
            shouldInitControl = false;
        }
        if (control && !this._controlRegistered) {
            this._controlRegistered = true;
            if (this._showHint && control instanceof dorado.widget.AbstractEditor) {
                if (control instanceof dorado.widget.AbstractTextBox) {
                    control.addListener("onValidationStateChange", $scopify(this, this.onEditorStateChange));
                    control.addListener("onPost", $scopify(this, this.onEditorPost));
                }
                control.addListener("onPostFailed", $scopify(this, this.onEditorPostFailed));
            }
            if (shouldInitControl) {
                var config1 = {}, config2 = {}, attrs = control.ATTRIBUTES;
                this.initEditorConfig(config1);
                for (var attr in config1) {
                    if (!attrs[attr] || attrs[attr].writeOnly) {
                        continue;
                    }
                    if (config1[attr] != null) {
                        config2[attr] = config1[attr];
                    }
                }
                control.set(config2, {skipUnknownAttribute:true, tryNextOnError:true, preventOverwriting:true});
            }
            this.registerInnerControl(control);
        }
        return control;
    }, getHintControl:function () {
        var control = this._hintControl;
        if (!control) {
            this._hintControl = control = new dorado.widget.DataMessage({dataSet:this._dataSet, dataPath:this._dataPath, property:this._property, showIconOnly:!this._showHintMessage});
        }
        if (control && !this._hintControlRegistered) {
            this._hintControlRegistered = true;
            this.registerInnerControl(control);
        }
        return control;
    }, initEditorConfig:function (config) {
        if (this._type == "password") {
            config.password = true;
        }
        if (this._trigger) {
            config.trigger = this._trigger;
        }
        if (this._readOnly) {
            config.readOnly = this._readOnly || this._realReadOnly;
        }
        if (this._entity) {
            config.entity = this._entity;
        }
        if (this._dataSet) {
            config.dataSet = this._dataSet;
        }
        if (this._dataPath) {
            config.dataPath = this._dataPath;
        }
        if (this._property) {
            config.property = this._property;
        }
    }, resetEditorReadOnly:function () {
        if (this._editor && this._editor instanceof dorado.widget.AbstractEditor) {
            this._editor.set("readOnly", this._readOnly || this._realReadOnly);
        }
    }, onEditorStateChange:function (editor, arg) {
        var hintControl = this.getHintControl();
        if (hintControl) {
            hintControl.set("messages", editor.get("validationMessages"));
        }
    }, onEditorPost:function (editor, arg) {
        var hintControl = this.getHintControl();
        if (hintControl) {
            setTimeout(function () {
                messages = editor.get("validationMessages");
                hintControl.set("messages", messages || DEFAULT_OK_MESSAGES);
            }, 0);
        }
    }, onEditorPostFailed:function (editor, arg) {
        if (!this._dataSet) {
            var exception = arg.exception;
            if (exception instanceof dorado.widget.editor.PostException) {
                var hintControl = this.getHintControl();
                if (hintControl) {
                    hintControl.set("messages", exception.messages);
                }
            }
        }
        arg.processDefault = false;
    }, getBindingPropertyDef:function () {
        var p = this._propertyDef;
        if (!p) {
            var dataSet = this._dataSet, dataPath = dorado.DataPath.create(this._dataPath), dataType;
            if (dataSet) {
                dataType = dataPath.getDataType(dataSet.get("dataType"));
            }
            if (dataType) {
                this._propertyDef = p = dataType.getPropertyDef(this._property);
            }
        }
        return p;
    }, getLabel:function () {
        var label = this._label;
        if (!label && this._dataSet) {
            var p = this.getBindingPropertyDef();
            if (p) {
                label = p._label || p._name;
            }
        }
        return label || this._property;
    }, isRequired:function () {
        var p;
        if (this._dataSet) {
            p = this.getBindingPropertyDef();
        }
        var required = p ? p._required : false;
        if (!required) {
            var editor = this.getEditor();
            required = (editor && editor instanceof dorado.widget.TextEditor && editor.get("required"));
        }
        return required;
    }, onProfileChange:function () {
        var formProfile = this._formProfile;
        if (dorado.Object.isInstanceOf(formProfile, dorado.widget.FormProfile)) {
            var attrs = formProfile.ATTRIBUTES, config = {};
            for (var attr in attrs) {
                if (!attrs.hasOwnProperty(attr) || attrs[attr].writeOnly) {
                    continue;
                }
                if (specialFormConfigProps.indexOf(attr) >= 0 && formProfile instanceof dorado.widget.Control) {
                    continue;
                }
                var value = formProfile.get(attr);
                if (value != null) {
                    config[attr] = value;
                }
            }
            this.set(config, {skipUnknownAttribute:true, tryNextOnError:true, preventOverwriting:true});
        }
    }, refreshDom:function (dom) {
        var height = this._height || this._realHeight;
        $invokeSuper.call(this, arguments);
        var dom = this._dom, editorEl = this._editorEl, labelEl = this._labelEl, hintEl = this._hintEl, editor = this.getEditor();
        if (this._showLabel) {
            labelEl.style.display = "";
            labelEl.style.textAlign = this._labelAlign || "left";
            var label = this.getLabel();
            labelEl.innerText = (label || "") + ((this._labelSeparator && label) ? this._labelSeparator : "");
            $fly(labelEl).toggleClass("form-label-required", !!this.isRequired());
        } else {
            if (labelEl) {
                labelEl.style.display = "none";
            }
        }
        var labelWidth = 0, labelHeight = 0, editorWidth = 0, hintSize = 0;
        if (this._labelPosition == "top") {
            labelHeight = labelEl.offsetHeight + this._labelSpacing;
            editorEl.style.paddingTop = labelHeight + "px";
            editorWidth = dom.offsetWidth;
            if (height) {
                editorEl.style.height = (dom.offsetHeight - labelHeight) + "px";
            }
        } else {
            labelWidth = this._labelWidth + this._labelSpacing;
            editorWidth = dom.offsetWidth - labelWidth;
            if (editorWidth > 0) {
                editorEl.style.width = editorWidth + "px";
            }
        }
        if (editor) {
            var config = {};
            if (hintEl) {
                var autoHeight = !editor.ATTRIBUTES.height.independent;
                if (this._hintPosition == "bottom") {
                    config.width = dom.offsetWidth - labelWidth;
                    if (height && autoHeight) {
                        config.height = editorEl.offsetHeight - labelHeight - hintEl.offsetHeight;
                    }
                } else {
                    if (autoHeight) {
                        config.height = editorEl.offsetHeight - labelHeight;
                    }
                    if (this._editorWidth > 0) {
                        config.width = this._editorWidth;
                    } else {
                        hintSize = this._hintWidth + this._hintSpacing;
                        config.width = editorWidth - hintSize;
                    }
                }
            } else {
                config.width = dom.offsetWidth - labelWidth;
                config.height = editorEl.offsetHeight;
            }
            if (config.width) {
                editor._realWidth = config.width;
            }
            if (config.height) {
                editor._realHeight = config.height;
            }
            if (!editor.get("rendered")) {
                editor.render(editorEl, editorEl.firstChild);
            } else {
                editor.refresh();
            }
        }
        if (hintEl) {
            var hintControl = this.getHintControl();
            if (this._hintPosition != "bottom") {
                if (this._labelPosition == "top") {
                    hintEl.style.top = labelHeight + "px";
                }
                if (this._editorWidth > 0) {
                    hintEl.style.width = (editorEl.clientWidth - this._editorWidth - this._hintSpacing) + "px";
                } else {
                    hintEl.style.width = this._hintWidth + "px";
                }
                if (editorEl.clientHeight) {
                    hintEl.style.height = editorEl.clientHeight + "px";
                }
            }
            if (!hintControl.get("rendered")) {
                hintControl.render(hintEl);
            }
        }
    }});
    dorado.widget.View.registerDefaultComponent("defaultFormProfile", function () {
        return new dorado.widget.FormProfile();
    });
})();
dorado.widget.autoform = {};
dorado.widget.autoform.AutoFormElement = $extend(dorado.widget.FormElement, {$className:"dorado.widget.autoform.AutoFormElement", ATTRIBUTES:{width:{independent:false}, name:{writeOnce:true, setter:function (p, v) {
    this._name = v;
    if (!this.getAttributeWatcher().getWritingTimes("property")) {
        this._property = v;
    }
}}}});
dorado.widget.AutoForm = $extend([dorado.widget.Control, dorado.widget.FormProfile], {$className:"dorado.widget.AutoForm", ATTRIBUTES:{className:{defaultValue:"d-auto-form"}, formProfile:{componentReference:true, setter:function (p, formProfile) {
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
    }
}}, cols:{skipRefresh:true, setter:function (p, cols) {
    this._cols = cols;
    if (this._rendered) {
        this.refreshFormLayout();
    }
}}, rowHeight:{}, cellSpacing:{}, cellPadding:{}, stretchWidth:{}, padding:{}, dataType:{getter:dorado.LazyLoadDataType.dataTypeGetter}, autoCreateElements:{}, elements:{skipRefresh:true, setter:function (p, elements) {
    if (this._rendered) {
        var container = this._container, layout;
        if (container) {
            layout = container.get("layout");
            layout.disableRendering();
        }
        try {
            if (container) {
                this._elements.each(function (element) {
                    container.removeChild(element);
                });
            }
            this._elements.clear();
            if (!elements) {
                return;
            }
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
}}}, addElement:function (element) {
    var elements = this._elements;
    if (!(element instanceof dorado.widget.Control)) {
        if (element) {
            var constraint = element._layoutConstraint;
            var config = dorado.Object.apply({}, {layoutConstraint:constraint, entity:this._entity, dataSet:this._dataSet, dataPath:this._dataPath, formProfile:this}, function (p, v) {
                return (v !== undefined);
            });
            dorado.Object.apply(config, element);
            if (!config.name) {
                var name = config.property || "_unnamed";
                if (elements.get(name)) {
                    var j = 2;
                    while (!elements.get(name + "_" + j)) {
                        j++;
                    }
                    name = name + "_" + j;
                }
                config.name = name;
            }
            element = dorado.Toolkits.createInstance("widget", config, function (type) {
                if (!type) {
                    return dorado.widget.autoform.AutoFormElement;
                }
            });
        } else {
            element = new dorado.widget.Control(config);
        }
    }
    elements.append(element);
    if (this._container) {
        this._container.addChild(element);
    }
    return element;
}, removeElement:function (element) {
    this._elements.remove(element);
    if (this._container) {
        this._container.removeChild(element);
    }
}, getElement:function (name) {
    return this._elements.get(name);
}, constructor:function () {
    this._elements = new dorado.util.KeyedArray(function (element) {
        return (element instanceof dorado.widget.autoform.AutoFormElement) ? element._name : element._id;
    });
    this._container = new dorado.widget.Container({layout:"Form", contentOverflow:"visible"});
    this.registerInnerControl(this._container);
    this._bindingElements = new dorado.ObjectGroup();
    $invokeSuper.call(this, arguments);
    this.addListener("onAttributeChange", function (self, arg) {
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
        } else {
            if (!dorado.widget.Control.prototype.ATTRIBUTES[attr] && dorado.widget.FormConfig.prototype.ATTRIBUTES[attr]) {
                dorado.Toolkits.setDelayedAction(self, "$profileChangeTimerId", function () {
                    var value = arg.value;
                    self._bindingElements.invoke("onProfileChange");
                }, 20);
            }
        }
    });
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var container = this._container;
    if (!container._rendered) {
        if (this._elementConfigs) {
            var configs = this._elementConfigs;
            for (var i = 0; i < configs.length; i++) {
                this.addElement(configs[i]);
            }
            delete this._elementConfigs;
        }
        if (this._autoCreateElements && !this._defaultElementsGenerated) {
            this.generateDefaultElements();
        }
        this.initLayout(container.get("layout"));
        container.render(dom);
    }
}, onResize:function () {
    var dom = this.getDom(), container = this._container;
    container._realWidth = dom.offsetWidth;
    container._realHeight = dom.offsetHeight;
    container.resetDimension();
}, refreshFormLayout:function () {
    var container = this._container, layout = container.get("layout");
    container.refresh();
    this.initLayout(layout);
    layout.refresh();
}, initLayout:function (layout) {
    configs = {};
    if (this._cols) {
        configs.cols = this._cols;
    }
    if (this._rowHeight) {
        configs.rowHeight = this._rowHeight;
    }
    if (this._cellSpacing) {
        configs.cellSpacing = this._cellSpacing;
    }
    if (this._cellPadding) {
        configs.cellPadding = this._cellPadding;
    }
    if (this._stretchWidth) {
        configs.stretchWidth = this._stretchWidth;
    }
    if (this._padding) {
        configs.padding = this._padding;
    }
    layout.set(configs);
}, generateDefaultElements:function () {
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
        dataType.get("propertyDefs").each(function (propertyDef) {
            if (!propertyDef._visible) {
                return;
            }
            var name = propertyDef._name, element = elements.get(name);
            if (!element) {
                config = {name:name, property:name};
            } else {
                config = {property:name};
                self.removeElement(element);
                self.addElement(element);
            }
            var propertyDataType = propertyDef.get("dataType");
            if (propertyDataType instanceof dorado.EntityDataType || propertyDataType instanceof dorado.AggregationDataType) {
                return;
            }
            if (!element) {
                element = self.addElement(config);
            } else {
                element.set(config, {tryNextOnError:true, preventOverwriting:true});
            }
            if (element instanceof dorado.widget.FormElement && !this._dataSet && propertyDef._readOnly) {
                element._realReadOnly = true;
            }
        });
        if (container) {
            layout.enableRendering();
            container.refresh(true);
        }
    }
}, validate:function () {
    var result = true, elements = this._elements;
    this._elements.each(function (element) {
        if (element instanceof dorado.widget.FormElement) {
            var editor = element.get("editor");
            if (editor && editor instanceof dorado.widget.AbstractTextBox) {
                if (editor.get("validateState") == "none") {
                    editor.post();
                }
                if (result && editor.get("validateState") == "invalid") {
                    result = false;
                }
            }
        }
    });
    return result;
}});
(function () {
    dorado.widget.SpinnerTrigger = $extend(dorado.widget.Trigger, {$className:"dorado.widget.SpinnerTrigger", ATTRIBUTES:{className:{defaultValue:"d-spinner-trigger"}}, createTriggerButton:function (editor) {
        var trigger = this, control = new dorado.widget.HtmlContainer({content:{tagName:"div", className:trigger._className, content:[{tagName:"div", className:"up-button", contextKey:"upButton"}, {tagName:"div", className:"down-button", contextKey:"downButton"}]}});
        control.getDom();
        jQuery(control.getSubDom("upButton")).repeatOnClick(function () {
            editor.doStepUp();
        }, 150).addClassOnClick("up-button-click", null, function () {
            return !editor.get("readOnly");
        });
        jQuery(control.getSubDom("downButton")).repeatOnClick(function () {
            editor.doStepDown();
        }, 150).addClassOnClick("down-button-click", null, function () {
            return !editor.get("readOnly");
        });
        return control;
    }});
    dorado.widget.Spinner = $extend(dorado.widget.AbstractTextBox, {$className:"dorado.widget.Spinner", focusable:true, ATTRIBUTES:{trigger:{getter:function (p, v) {
        var triggers = this._trigger;
        if (triggers && !(triggers instanceof Array)) {
            triggers = [triggers];
        }
        var spinnerTriggers = this.getSpinnerTriggers();
        return triggers ? spinnerTriggers.concat(triggers) : spinnerTriggers;
    }}, showSpinTrigger:{defaultValue:true}, step:{defaultValue:1}}, constructor:function () {
        $invokeSuper.call(this, arguments);
        this._triggerChanged = true;
    }, getSpinnerTriggers:function () {
        if (!this._showSpinTrigger) {
            return [];
        }
        if (!this._spinTrigger) {
            var triggers = this._spinTrigger = [], self = this;
            triggers.push(new dorado.widget.SpinnerTrigger({}));
        }
        return this._spinTrigger;
    }, createDom:function () {
        var dom = $invokeSuper.call(this, arguments), self = this;
        jQuery(dom).addClassOnHover(this._className + "-hover", null, function () {
            return !self.get("readOnly");
        });
        return dom;
    }});
    dorado.widget.NumberSpinner = $extend(dorado.widget.Spinner, {ATTRIBUTES:{min:{defaultValue:-2147483648}, max:{defaultValue:2147483648}, text:{defaultValue:0}, value:{getter:function () {
        return parseInt(this.get("text"), 10);
    }}, selectTextOnFocus:{defaultValue:true}}, createTextDom:function () {
        var textDom = document.createElement("INPUT");
        textDom.className = "editor";
        if (!(dorado.Browser.msie && dorado.Browser.version < "7")) {
            textDom.style.padding = 0;
        }
        if (dorado.Browser.msie && dorado.Browser.version > "7") {
            textDom.style.top = 0;
            textDom.style.position = "absolute";
        }
        return textDom;
    }, doGetText:function () {
        return (this._textDom) ? this._textDom.value : this._text;
    }, doSetText:function (text) {
        if (this._textDom) {
            this._textDom.value = text;
        } else {
            this._text = text;
        }
    }, doStepUp:function () {
        var spinner = this;
        var value = parseInt(spinner.get("value"), 10);
        if (!isNaN(value)) {
            spinner.set("value", value + spinner._step);
        }
    }, doStepDown:function () {
        var spinner = this;
        var value = parseInt(spinner.get("value"), 10);
        if (!isNaN(value)) {
            spinner.set("value", value - spinner._step);
        }
    }, post:function (force) {
        var text = this.get("text"), value = parseInt(text) || 0;
        if (value > this._max) {
            value = this._max;
        } else {
            if (value < this._min) {
                value = this._min;
            }
        }
        this._value = value;
        this.doSetText(value + "");
        $invokeSuper.call(this, arguments);
    }, doOnFocus:function () {
        $invokeSuper.call(this, arguments);
        if (this._selectTextOnFocus) {
            $setTimeout(this, function () {
                this._textDom.select();
            }, 0);
        }
    }});
    dorado.widget.MultiSlotSpinner = $extend(dorado.widget.Spinner, {$className:"dorado.widget.MultiSlotSpinner", slotConfigs:[{}], defaultSlot:0, constructor:function () {
        if (this.slotConfigs) {
            this.initSlotConfigs();
        }
        this._currentSlotIndex = 0;
        $invokeSuper.call(this, arguments);
    }, initSlotConfigs:function () {
        var slotConfigs = this.slotConfigs, slotMap = this._slotMap = {}, values = this._values = [];
        for (var i = 0, j = slotConfigs.length; i < j; i++) {
            var config = slotConfigs[i], name = config.name;
            config.className = config.className || "slot";
            config.defaultValue = config.defaultValue || 0;
            config.range = config.range || [null, null];
            slotMap[name] = config;
            values[i] = config.defaultValue;
        }
    }, createTextDom:function () {
        var spinner = this, doms = {}, dom = $DomUtils.xCreateElement({tagName:"div", className:"editor slots-container", contextKey:"editor"}, null, doms);
        spinner._doms = doms;
        var slotConfigs = spinner.slotConfigs;
        for (var i = 0, j = slotConfigs.length; i < j; i++) {
            var config = slotConfigs[i], name = config.name;
            var label = document.createElement("span");
            label.className = config.className;
            label.slotIndex = i;
            doms["slot_" + i] = label;
            $fly(label).mousedown(function () {
                spinner.doChangeCurrentSlot(parseInt(this.slotIndex));
            });
            if (config.prefix) {
                var spEl = document.createElement("span");
                $fly(spEl).text(config.prefix).attr("class", "text");
                $fly(doms.editor).append(spEl);
            }
            $fly(doms.editor).append(label);
            if (config.suffix) {
                var spEl = document.createElement("span");
                $fly(spEl).text(config.suffix).attr("class", "text");
                $fly(doms.editor).append(spEl);
            }
        }
        return dom;
    }, doGetSlotRange:function (slotIndex) {
        if (typeof slotIndex == "string") {
            slotIndex = this.getSlotIndexByName(slotIndex);
        }
        return this.slotConfigs[slotIndex].range;
    }, getSlotIndexByName:function (name) {
        var config = this._slotMap[name];
        return config ? this.slotConfigs.indexOf(config) : -1;
    }, doGetSlotValue:function (slotIndex) {
        this._slotValueChanged = true;
        if (typeof slotIndex == "string") {
            slotIndex = this.getSlotIndexByName(slotIndex);
        }
        return this._values[slotIndex];
    }, doSetSlotValue:function (slotIndex, value) {
        var spinner = this;
        if (typeof slotIndex == "string") {
            slotIndex = spinner.getSlotIndexByName(slotIndex);
        }
        if (slotIndex < 0) {
            return;
        }
        var config = spinner.slotConfigs[slotIndex], range = config.range || [], minValue = range[0], maxValue = range[1];
        value = parseInt(value, 10);
        if (!isNaN(value)) {
            if (maxValue != null && value > maxValue) {
                value = maxValue;
            } else {
                if (minValue != null && value < minValue) {
                    value = minValue;
                }
            }
        } else {
            value = config.defaultValue || 0;
        }
        this._values[slotIndex] = value;
        dorado.Toolkits.setDelayedAction(spinner, "$refreshDelayTimerId", spinner.refresh, 50);
    }, doGetSlotText:function (slotIndex) {
        var spinner = this;
        if (typeof slotIndex == "string") {
            slotIndex = spinner.getSlotIndexByName(slotIndex);
        }
        if (slotIndex < 0) {
            return "";
        }
        var config = spinner.slotConfigs[slotIndex];
        var text = this.doGetSlotValue(slotIndex);
        if (text == null) {
            text = "&nbsp;";
        } else {
            var num = text, negative = (num < 0), text = Math.abs(num) + "";
            if (config.digit > 0 && text.length < config.digit) {
                for (var i = text.length; i <= config.digit - 1; i++) {
                    text = "0" + text;
                }
            }
            text = (negative ? "-" : "") + text;
        }
        return text;
    }, doStepUp:function () {
        var spinner = this, currentSlotIndex = spinner._currentSlotIndex;
        if (!currentSlotIndex) {
            currentSlotIndex = spinner.doChangeCurrentSlot();
        }
        var value = spinner.doGetSlotValue(currentSlotIndex) + spinner._step;
        var config = spinner.slotConfigs[currentSlotIndex], range = config.range || [], minValue = range[0], maxValue = range[1];
        if (maxValue != null && minValue != null && value > maxValue) {
            value = minValue;
        }
        spinner.doSetSlotValue(currentSlotIndex, value);
    }, doStepDown:function () {
        var spinner = this, currentSlotIndex = spinner._currentSlotIndex;
        if (!currentSlotIndex) {
            currentSlotIndex = spinner.doChangeCurrentSlot();
        }
        var value = spinner.doGetSlotValue(currentSlotIndex) - spinner._step;
        var config = spinner.slotConfigs[currentSlotIndex], range = config.range || [], minValue = range[0], maxValue = range[1];
        if (maxValue != null && minValue != null && value < minValue) {
            value = maxValue;
        }
        spinner.doSetSlotValue(currentSlotIndex, value);
    }, doOnKeyDown:function (event) {
        var spinner = this, retval = true;
        switch (event.keyCode) {
          case 38:
            spinner.doStepUp();
            retval = false;
            break;
          case 40:
            spinner.doStepDown();
            retval = false;
            break;
          case 8:
            if (spinner._currentSlotIndex >= 0) {
                var currentSlotIndex = spinner._currentSlotIndex, value, range = spinner.doGetSlotRange(currentSlotIndex);
                if (spinner._neverEdit) {
                    value = 0;
                } else {
                    var text = spinner.doGetSlotText(currentSlotIndex);
                    value = parseInt(text.substring(0, text.length - 1), 10);
                }
                spinner.doSetSlotValue(currentSlotIndex, value);
            }
            retval = false;
            break;
          case 9:
            if (event.ctrlKey || !event.shiftKey && spinner._currentSlotIndex == spinner.slotConfigs.length - 1 || event.shiftKey && spinner._currentSlotIndex == 0) {
                retval = true;
            } else {
                spinner.doChangeCurrentSlot(event.shiftKey ? "prev" : "next");
                retval = false;
            }
            break;
          case 187:
            if (spinner._currentSlotIndex >= 0) {
                var currentSlotIndex = spinner._currentSlotIndex, value = spinner.doGetSlotValue(currentSlotIndex) || 0;
                if (value) {
                    value = Math.abs(value);
                    spinner.doSetSlotValue(currentSlotIndex, value);
                }
                retval = false;
            }
            break;
          case 189:
            if (spinner._currentSlotIndex >= 0) {
                var currentSlotIndex = spinner._currentSlotIndex, value = spinner.doGetSlotValue(currentSlotIndex) || 0;
                if (value) {
                    value = 0 - Math.abs(value);
                    spinner.doSetSlotValue(currentSlotIndex, value);
                }
                retval = false;
            }
            break;
          default:
            if (spinner._currentSlotIndex >= 0) {
                if (event.keyCode >= 48 && event.keyCode <= 57) {
                    var number = event.keyCode - 48, currentSlotIndex = spinner._currentSlotIndex, range = spinner.doGetSlotRange(currentSlotIndex), maxValue = range[1];
                    var value = spinner._neverEdit ? 0 : (spinner.doGetSlotValue(currentSlotIndex) || 0), ignore = false;
                    var config = spinner.slotConfigs[currentSlotIndex], digit = config.digit;
                    if (!digit && maxValue != null) {
                        digit = (maxValue + "").length;
                    }
                    if (digit && (value + "").length == digit) {
                        ignore = true;
                    } else {
                        value = value * 10 + number;
                        ignore = (maxValue != null && value > maxValue);
                    }
                    if (!ignore) {
                        spinner.doSetSlotValue(currentSlotIndex, value);
                    }
                    retval = false;
                }
            }
            break;
        }
        if (retval === false) {
            spinner._neverEdit = false;
        }
        return retval;
    }, doChangeCurrentSlot:function (slotIndex) {
        var spinner = this;
        if (typeof slotIndex == "string") {
            if (slotIndex == "next") {
                slotIndex = (spinner._currentSlotIndex >= 0) ? spinner._currentSlotIndex + 1 : spinner.defaultSlot;
            } else {
                if (slotIndex == "prev") {
                    slotIndex = (spinner._currentSlotIndex >= 0) ? spinner._currentSlotIndex - 1 : spinner.defaultSlot;
                } else {
                    slotIndex = spinner.getSlotIndexByName(slotIndex);
                }
            }
        }
        slotIndex = slotIndex || 0;
        var oldSlotIndex = spinner._currentSlotIndex, doms = spinner._doms;
        var config = spinner.slotConfigs[slotIndex];
        if (config) {
            if (oldSlotIndex >= 0) {
                var oldSlotConfig = spinner.slotConfigs[oldSlotIndex];
                $fly(doms["slot_" + oldSlotIndex]).removeClass(oldSlotConfig.className + "-selected");
                spinner.doAfterSlotBlur(oldSlotIndex);
            }
            $fly(doms["slot_" + slotIndex]).addClass(config.className + "-selected");
            spinner._currentSlotIndex = slotIndex;
            return slotIndex;
        } else {
            return oldSlotIndex;
        }
    }, doAfterSlotBlur:function (slotIndex) {
        var spinner = this, value = spinner.doGetSlotValue(slotIndex);
        if (value == null) {
            spinner.doSetSlotValue(slotIndex, spinner.slotConfigs[slotIndex].defaultValue || 0);
        }
    }, doOnBlur:function () {
        var spinner = this, currentSlotIndex = spinner._currentSlotIndex, doms = spinner._doms;
        if (currentSlotIndex >= 0) {
            $fly(doms["slot_" + currentSlotIndex]).removeClass(spinner.slotConfigs[currentSlotIndex].className + "-selected");
            spinner.doAfterSlotBlur(currentSlotIndex);
        }
        this.post(true);
    }, doOnFocus:function () {
        var spinner = this, currentSlotIndex = spinner._currentSlotIndex, doms = spinner._doms;
        spinner._neverEdit = true;
        if (currentSlotIndex >= 0) {
            $fly(doms["slot_" + currentSlotIndex]).addClass(spinner.slotConfigs[currentSlotIndex].className + "-selected");
        }
    }, refreshDom:function () {
        $invokeSuper.call(this, arguments);
        var spinner = this, doms = spinner._doms;
        for (var i = 0; i < spinner.slotConfigs.length; i++) {
            $fly(doms["slot_" + i]).html(spinner.doGetSlotText(i));
        }
    }, doGetText:function () {
        var spinner = this, slotConfigs = spinner.slotConfigs, text = "";
        for (var i = 0; i < slotConfigs.length; i++) {
            var config = slotConfigs[i];
            text += config.prefix || "";
            text += spinner.doGetSlotText(i);
            text += config.suffix || "";
        }
        return text;
    }});
    function slotAttributeGetter(attr) {
        return this.doGetSlotValue(attr);
    }
    function slotAttributeSetter(attr, value) {
        this.doSetSlotValue(attr, value);
    }
    var now = new Date();
    dorado.widget.DateTimeSpinner = $extend(dorado.widget.MultiSlotSpinner, {$className:"dorado.widget.DateTimeSpinner", slotConfigTemplate:{year:{name:"year", range:[1, 9999], defaultValue:now.getFullYear()}, month:{name:"month", range:[1, 12], defaultValue:now.getMonth() + 1, digit:2, prefix:"-"}, date:{name:"date", range:[1, 31], defaultValue:now.getDate(), digit:2, prefix:"-"}, hours:{name:"hours", range:[0, 23], digit:2, prefix:" "}, leading_hours:{name:"hours", range:[0, 23], digit:2}, minutes:{name:"minutes", range:[0, 59], digit:2, prefix:":"}, seconds:{name:"seconds", range:[0, 59], digit:2, prefix:":"}}, ATTRIBUTES:{type:{writeOnce:true, writeBeforeReady:true, setter:function (p, type) {
        this._type = type = type || "time";
        this._typeChanged = true;
        var configs, template = this.slotConfigTemplate;
        switch (type) {
          case "date":
            configs = [template.year, template.month, template.date];
            break;
          case "time":
            configs = [template.leading_hours, template.minutes, template.seconds];
            break;
          case "dateTime":
            configs = [template.year, template.month, template.date, template.hours, template.minutes, template.seconds];
            break;
          case "hours":
            configs = [template.leading_hours];
            break;
          case "minutes":
            configs = [template.leading_hours, template.minutes];
            break;
          case "dateHours":
            configs = [template.year, template.month, template.date, template.hours];
            break;
          case "dateMinutes":
            configs = [template.year, template.month, template.date, template.hours, template.minutes];
            break;
        }
        this.slotConfigs = configs;
        this.initSlotConfigs();
    }}, year:{getter:slotAttributeGetter, setter:slotAttributeSetter}, month:{getter:slotAttributeGetter, setter:slotAttributeSetter}, date:{getter:slotAttributeGetter, setter:slotAttributeSetter}, hours:{getter:slotAttributeGetter, setter:slotAttributeSetter}, minutes:{getter:slotAttributeGetter, setter:slotAttributeSetter}, seconds:{getter:slotAttributeGetter, setter:slotAttributeSetter}, value:{getter:function (p) {
        var year = this.doGetSlotValue("year") || 1980;
        var month = (this.doGetSlotValue("month") - 1) || 0;
        var date = this.doGetSlotValue("date") || 1;
        var hours = this.doGetSlotValue("hours") || 0;
        var minutes = this.doGetSlotValue("minutes") || 0;
        var seconds = this.doGetSlotValue("seconds") || 0;
        return new Date(year, month, date, hours, minutes, seconds);
    }, setter:function (p, d) {
        var year = 0, month = 1, date = 1, hours = 0, minutes = 1, seconds = 1;
        if (d) {
            year = d.getFullYear();
            month = d.getMonth() + 1;
            date = d.getDate();
            hours = d.getHours();
            minutes = d.getMinutes();
            seconds = d.getSeconds();
        }
        this.doSetSlotValue("year", year);
        this.doSetSlotValue("month", month);
        this.doSetSlotValue("date", date);
        this.doSetSlotValue("hours", hours);
        this.doSetSlotValue("minutes", minutes);
        this.doSetSlotValue("seconds", seconds);
        this.doSetValidateState("none");
    }}}, constructor:function () {
        this.slotConfigs = [];
        $invokeSuper.call(this, arguments);
    }, createTextDom:function () {
        if (!this._typeChanged) {
            this.set("type", "time");
        }
        return $invokeSuper.call(this, arguments);
    }, doSetSlotValue:function (slotIndex, value) {
        var spinner = this, slotName;
        if (typeof slotIndex == "number") {
            slotName = spinner.slotConfigs[slotIndex].name;
        } else {
            slotName = slotIndex;
            slotIndex = spinner.getSlotIndexByName(slotIndex);
        }
        if (!spinner._slotMap[slotName]) {
            return;
        }
        if (!spinner._slotMap["date"]) {
            $invokeSuper.call(this, arguments);
            return;
        }
        var dateSlotIndex = spinner.getSlotIndexByName("date"), date = spinner._values[dateSlotIndex], newDate = 0;
        if (date >= 28) {
            var year = (slotIndex == 0) ? value : spinner._values[0];
            var month = (slotIndex == 1) ? value : spinner._values[1];
            var dayCount = new Date(year, month - 1).getDaysInMonth();
            if (date > dayCount) {
                newDate = dayCount;
            }
        }
        if (newDate) {
            if (slotName == "year" || slotName == "month") {
                spinner.doSetSlotValue("date", newDate);
                $invokeSuper.call(this, arguments);
            } else {
                $invokeSuper.call(this, [slotIndex, newDate]);
            }
        } else {
            $invokeSuper.call(this, arguments);
        }
    }, doGetSlotRange:function (slotIndex) {
        var spinner = this, slotName;
        if (typeof slotIndex == "number") {
            slotName = spinner.slotConfigs[slotIndex].name;
        } else {
            slotName = slotIndex;
        }
        if (slotName == "date" && spinner._slotMap["date"]) {
            var year = spinner._values[0], month = spinner._values[1], dayCount = new Date(year, month - 1).getDaysInMonth();
            return [1, dayCount];
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, doSetText:function (text) {
        var format;
        switch (this._type) {
          case "date":
            format = "Y-m-d";
            break;
          case "time":
            format = "h:i:s";
            break;
          case "dateTime":
            format = "Y-m-d h:i:s";
            break;
          case "hours":
            format = "h";
            break;
          case "minutes":
            format = "h:i";
            break;
          case "dateHours":
            format = "Y-m-d h";
            break;
          case "dateMinutes":
            format = "Y-m-d h:i";
            break;
        }
        this.set("value", format);
    }});
    dorado.widget.CustomSpinner = $extend(dorado.widget.MultiSlotSpinner, {$className:"dorado.widget.CustomSpinner", ATTRIBUTES:{pattern:{writeOnce:true, writeBeforeReady:true, setter:function (p, pattern) {
        this.parsePattern(pattern);
    }}, value:{getter:function () {
        return this._values;
    }, setter:function (p, v) {
        var v = v || [];
        for (var i = 0; i < this.slotConfigs.length; i++) {
            this.doSetSlotValue(i, v[i]);
        }
        this.doSetValidateState("none");
    }}}, parsePattern:function (pattern) {
        function parseSlotConfig(slotConfig) {
            var slot = {}, sections = slotConfig.split("|");
            if (sections[0] != "*") {
                var range = sections[0].replace(/\*/g, "null");
                slot.range = eval("[" + range + "]");
            }
            if (sections[1]) {
                slot.digit = parseInt(sections[1]);
            }
            return slot;
        }
        if (!pattern) {
            throw new dorado.ResourceException("dorado.core.AttributeValueRequired", "pattern");
        }
        this.slotConfigs = [];
        var i = 0, c, text = "", slotConfig = "", inSlot = false;
        while (i < pattern.length) {
            c = pattern.charAt(i);
            if (inSlot) {
                if (c == "]") {
                    var slot = parseSlotConfig(slotConfig);
                    if (text) {
                        slot.prefix = text;
                    }
                    this.slotConfigs.push(slot);
                    inSlot = false;
                    text = slotConfig = "";
                } else {
                    slotConfig += c;
                }
            } else {
                if (c == "\\") {
                    c = pattern.charAt(++i);
                    text += c;
                } else {
                    if (c == "[") {
                        inSlot = true;
                    } else {
                        text += c;
                    }
                }
            }
            i++;
        }
        if (!this.slotConfigs.length) {
            throw new dorado.ResourceException("dorado.baseWidget.InvalidSpinnerPattern", pattern);
        }
        if (text) {
            this.slotConfigs.peek().suffix = text;
        }
        this.initSlotConfigs();
    }, doSetText:dorado._UNSUPPORTED_FUNCTION()});
})();

