
(function () {
    dorado.widget.grid = {};
    dorado.widget.grid.ColumnList = $extend(dorado.util.KeyedArray, {$className:"dorado.widget.grid.ColumnList", constructor:function (parentColumn) {
        $invokeSuper.call(this, [dorado._GET_NAME]);
        this.parentColumn = parentColumn;
    }, beforeInsert:function (column) {
        column._parent = this.parentColumn;
    }, beforeRemove:function (column) {
        delete column._parent;
    }});
    dorado.widget.grid.ColumnModel = $extend(dorado.AttributeSupport, {$className:"dorado.widget.grid.ColumnModel", ATTRIBUTES:{columns:{setter:function (a, v) {
        this.addColumns(v);
    }}}, addColumn:function (columnConfig, insertMode, refcolumn) {
        var column;
        if (columnConfig instanceof dorado.widget.grid.Column) {
            column = columnConfig;
        } else {
            if (!columnConfig.name && columnConfig.property) {
                var name = columnConfig.property;
                if (this.getColumn(name)) {
                    var j = 2;
                    while (!this.getColumn(name + "_" + j)) {
                        j++;
                    }
                    name = name + "_" + j;
                }
                columnConfig.name = name;
            }
            column = dorado.Toolkits.createInstance("gridcolumn", columnConfig, function (type) {
                if (type) {
                    return dorado.util.Common.getClassType("dorado.widget.grid." + type + "Column", true);
                }
                return (columnConfig.columns && columnConfig.columns.length) ? dorado.widget.grid.ColumnGroup : dorado.widget.grid.DataColumn;
            });
        }
        this._columns.insert(column, insertMode, refcolumn);
        column.set("grid", this._grid);
        return column;
    }, addColumns:function (columnConfigs) {
        for (var i = 0; i < columnConfigs.length; i++) {
            this.addColumn(columnConfigs[i]);
        }
    }, getColumn:function (name) {
        return this._columns.get(name);
    }, findColumns:function (name) {
        function doFindColumns(column, name, result) {
            var cols = column._columns.items;
            for (var i = 0; i < cols.length; i++) {
                var col = cols[i];
                if (col._name == name) {
                    result.push(col);
                }
                if (col instanceof dorado.widget.grid.ColumnGroup) {
                    doFindColumns(col, name, result);
                }
            }
        }
        var result = [];
        doFindColumns(this, name, result);
        return result;
    }, getColumnsInfo:function (fixedColumnCount) {
        function getStructure(structure, cols, row) {
            if (structure.length <= row) {
                structure.push([]);
                if (row >= maxRowCount) {
                    maxRowCount = row + 1;
                }
            }
            var cells = structure[row];
            for (var i = 0; i < cols.length; i++) {
                var col = cols[i];
                if (!col._visible) {
                    continue;
                }
                idMap[col._id] = col;
                var cell = {column:col, row:row, colSpan:1, rowSpan:0, topColIndex:topColIndex};
                if (col instanceof dorado.widget.grid.ColumnGroup) {
                    var oldDataCellCount = dataColCount;
                    getStructure(structure, col._columns.items, row + 1);
                    cell.colSpan = dataColCount - oldDataCellCount;
                    cell.rowSpan = 1;
                } else {
                    dataColCount++;
                    dataColumnInfos.push(cell);
                }
                if (row == 0) {
                    topColIndex++;
                }
                cells.push(cell);
            }
        }
        function extractStructure(structure, start, end) {
            var subStruct = [];
            if (end == undefined) {
                end = Number.MAX_VALUE;
            }
            for (var i = 0; i < structure.length; i++) {
                var row = structure[i], subRow = [];
                for (var j = 0; j < row.length; j++) {
                    var col = row[j];
                    if (col.topColIndex >= start && col.topColIndex <= end) {
                        subRow.push(col);
                    }
                }
                subStruct.push(subRow);
            }
            return subStruct;
        }
        function extractDataColumns(dataColumnInfos, start, end) {
            var dataCols = [];
            if (end == undefined) {
                end = Number.MAX_VALUE;
            }
            for (var i = 0; i < dataColumnInfos.length; i++) {
                var col = dataColumnInfos[i];
                if (col.topColIndex >= start && col.topColIndex <= end) {
                    dataCols.push(col.column);
                }
            }
            return dataCols;
        }
        var cols = this._columns.items, topColIndex = 0, dataColCount = 0, maxRowCount = 0;
        var idMap = {}, fixedColumns, mainColumns = {}, dataColumnInfos = [];
        var tempStruct = [];
        getStructure(tempStruct, cols, 0);
        fixedColumnCount = fixedColumnCount || 0;
        if (fixedColumnCount > 0) {
            fixedColumns = {};
            fixedColumns.structure = extractStructure(tempStruct, 0, fixedColumnCount - 1);
            fixedColumns.dataColumns = extractDataColumns(dataColumnInfos, 0, fixedColumnCount - 1);
        }
        mainColumns.structure = extractStructure(tempStruct, fixedColumnCount);
        mainColumns.dataColumns = extractDataColumns(dataColumnInfos, fixedColumnCount);
        return {idMap:idMap, fixed:fixedColumns, main:mainColumns, dataColumns:extractDataColumns(dataColumnInfos, 0)};
    }});
    dorado.widget.grid.DefaultCellHeaderRenderer = $extend(dorado.Renderer, {render:function (dom, arg) {
        var column = arg.column, cell = dom.parentNode, label;
        if (dom.childNodes.length == 1) {
            label = dom.firstChild;
        } else {
            $fly(dom).empty();
            label = $DomUtils.xCreateElement({tagName:"LABEL", className:"caption"});
            dom.appendChild(label);
        }
        label.innerText = column.get("caption");
        if (column instanceof dorado.widget.grid.DataColumn) {
            $fly(label).toggleClass("caption-required", !!column.get("required"));
            var sortState = column.get("sortState"), sortIndicator;
            if (sortState) {
                sortIndicator = $DomUtils.xCreateElement({tagName:"LABEL", className:"sort-state sort-state-" + sortState});
            }
            if (sortIndicator) {
                dom.appendChild(sortIndicator);
            }
        }
    }});
    dorado.widget.grid.Column = $extend([dorado.AttributeSupport, dorado.EventSupport], {$className:"dorado.widget.grid.Column", ATTRIBUTES:{grid:{}, parent:{readOnly:true}, name:{writeOnce:true}, caption:{getter:function () {
        var caption = this._caption;
        if (caption == null) {
            caption = this._name;
        }
        return caption;
    }}, headerRenderer:{}, visible:{defaultValue:true}, supportsOptionMenu:{skipRefresh:true, defaultValue:true}, userData:{skipRefresh:true}}, EVENTS:{onRenderHeaderCell:{}, onHeaderClick:{}}, constructor:function (config) {
        $invokeSuper.call(this, arguments);
        this._id = dorado.Core.newId();
        if (config) {
            this.set(config);
        }
        if (!this._name) {
            this._name = this._id;
        }
    }, doSet:function (attr, value) {
        $invokeSuper.call(this, arguments);
        var grid = this._grid;
        if (!grid || !grid._rendered) {
            return;
        }
        var def = this.ATTRIBUTES[attr];
        if ((this._visible || attr == "visible") && grid._ignoreRefresh < 1 && def && !def.skipRefresh) {
            grid._ignoreItemTimestamp = true;
            dorado.Toolkits.setDelayedAction(grid, "$refreshDelayTimerId", grid.refresh, 50);
        }
    }});
    dorado.widget.grid.RowRenderer = $extend(dorado.Renderer, {rebuildRow:function (grid, innerGrid, row, rowType) {
        var len = innerGrid._columnsInfo.dataColumns.length, oldRowType = row.rowType, $row = $fly(row);
        if (oldRowType == "header") {
            $row.empty();
        }
        $row.toggleClass("group-header-row", rowType == "header").toggleClass("group-footer-row", rowType == "footer");
        if (rowType == "header") {
            $row.empty();
            var cell = innerGrid.createCell();
            cell.colSpan = len;
            row.appendChild(cell);
        } else {
            for (var i = 0; i < len; i++) {
                $DomUtils.getOrCreateChild(row, i, innerGrid.createCell);
            }
            $DomUtils.removeChildrenFrom(row, len);
        }
        if (rowType) {
            row.rowType = rowType;
        } else {
            row.removeAttribute("rowType");
        }
    }, render:function (row, arg) {
        var grid = arg.grid, innerGrid = arg.innerGrid, entity = arg.data, dataColumns = innerGrid._columnsInfo.dataColumns;
        if (row.rowType != entity.rowType || (entity.rowType != "header" && row.cells.length != dataColumns.length) || (entity.rowType == "header" && row.firstChild.colSpan != dataColumns.length)) {
            this.rebuildRow(grid, innerGrid, row, entity.rowType);
        }
        this.doRender(row, arg);
    }});
    dorado.widget.grid.DefaultRowRenderer = $extend(dorado.widget.grid.RowRenderer, {renderCell:function (cellRenderer, dom, arg) {
        var grid = arg.grid, column = arg.column, entity = arg.data, processDefault = true, eventArg = {dom:dom, data:entity, column:column, rowType:entity.rowType, cellRenderer:cellRenderer, processDefault:false};
        if (grid.getListenerCount("onRenderCell")) {
            grid.fireEvent("onRenderCell", grid, eventArg);
            processDefault = eventArg.processDefault;
        }
        if (processDefault) {
            cellRenderer = eventArg.cellRenderer;
            if (column.getListenerCount("onRenderCell")) {
                eventArg.processDefault = false;
                column.fireEvent("onRenderCell", column, eventArg);
                processDefault = eventArg.processDefault;
            }
            if (processDefault) {
                dorado.Renderer.render(cellRenderer, dom, arg);
            }
        }
    }, doRender:function (row, arg) {
        if (row._lazyRender) {
            return;
        }
        var grid = arg.grid, innerGrid = arg.innerGrid, entity = arg.data, dataColumns = innerGrid._columnsInfo.dataColumns;
        var oldHeight;
        if (grid._dynaRowHeight) {
            if (dorado.Browser.chrome || dorado.Browser.safari) {
                oldHeight = row.firstChild.clientHeight;
            } else {
                oldHeight = row.clientHeight;
            }
            if (dorado.Browser.msie && dorado.Browser.version >= "8") {
                row.style.height = "";
                $fly(row).addClass("fix-valign-bug");
            } else {
                if (dorado.Browser.chrome || dorado.Browser.safari) {
                    row.firstChild.style.height = "";
                } else {
                    row.style.height = "";
                }
            }
        }
        for (var i = 0; i < dataColumns.length; i++) {
            var col = dataColumns[i];
            var cell = row.cells[i];
            var label = cell.firstChild;
            if (grid._dynaRowHeight) {
                label.style.overflowY = "visible";
                cell.style.height = grid._rowHeight + "px";
            } else {
                cell.style.height = "";
                label.style.height = grid._rowHeight + "px";
            }
            if (col instanceof dorado.widget.grid.DataColumn) {
                label.style.width = col._realWidth + "px";
            }
            var align = "", renderer = col._renderer || grid._cellRenderer;
            if (!renderer) {
                var dt = col.get("dataType");
                var dtCode = dt ? dt._code : -1;
                if (dtCode == dorado.DataType.PRIMITIVE_BOOLEAN || dtCode == dorado.DataType.BOOLEAN) {
                    var pd = col._propertyDef;
                    if (pd && pd._mapping) {
                        renderer = $singleton(dorado.widget.grid.DefaultCellRenderer);
                    } else {
                        renderer = $singleton(dorado.widget.grid.CheckBoxCellRenderer);
                        align = "center";
                    }
                } else {
                    renderer = $singleton(dorado.widget.grid.DefaultCellRenderer);
                }
            }
            cell.align = col._align || align;
            this.renderCell(renderer, label, {grid:grid, innerGrid:arg.innerGrid, data:entity, column:col});
            cell.colId = col._id;
        }
        if (grid._dynaRowHeight) {
            var h;
            if (dorado.Browser.chrome || dorado.Browser.safari) {
                h = row.firstChild.clientHeight;
            } else {
                h = row.clientHeight;
            }
            if (oldHeight != h) {
                if (!grid.xScroll || !grid.yScroll) {
                    grid.notifySizeChange();
                }
                if (grid._fixedColumnCount) {
                    var rowHeightInfos = grid._rowHeightInfos, itemId = row.itemId;
                    if (innerGrid.fixed) {
                        rowHeightInfos.rows[itemId] = h;
                        rowHeightInfos.unfound[itemId] = true;
                    } else {
                        delete rowHeightInfos.unfound[itemId];
                        var fh = rowHeightInfos.rows[itemId];
                        if (h > fh) {
                            rowHeightInfos.rows[itemId] = h;
                            rowHeightInfos.unmatched.push(itemId);
                            if (!innerGrid._duringRefreshDom) {
                                grid._fixedInnerGrid.syncroRowHeight(itemId);
                            }
                        } else {
                            if (fh > 0) {
                                if (dorado.Browser.msie && dorado.Browser.version >= "8") {
                                    row.style.height = fh + "px";
                                    $fly(row).toggleClass("fix-valign-bug");
                                } else {
                                    if (dorado.Browser.chrome || dorado.Browser.safari) {
                                        row.firstChild.style.height = fh + "px";
                                    } else {
                                        row.style.height = fh + "px";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }});
    dorado.widget.grid.CellRenderer = $extend(dorado.Renderer, {getText:function (entity, column) {
        var text = "";
        if (entity) {
            if (column._property) {
                var dataType = column.get("dataType"), displayFormat = column.get("displayFormat");
                if (dataType && displayFormat) {
                    var value = (entity instanceof dorado.Entity) ? entity.get(column._property) : entity[column._property];
                    text = dataType.toText(value, displayFormat);
                } else {
                    text = (entity instanceof dorado.Entity) ? entity.getText(column._property) : entity[column._property];
                }
            } else {
                text = "";
            }
        }
        if (text && text.replace && !column._wrappable) {
            text = text.replace(/\n/g, " ");
        }
        return text;
    }, beforeCellValueEdit:function (entity, column, value) {
        column._grid.beforeCellValueEdit(entity, column, value);
    }, onCellValueEdit:function (entity, column) {
        column._grid.onCellValueEdit(entity, column);
    }, renderDirtyFlag:function (dom, arg) {
        var entity = arg.data, property = arg.column._property;
        if (!entity.rowType && entity instanceof dorado.Entity && property) {
            $fly(dom).toggleClass("dirty-cell", entity.isDirty(property));
        }
    }, render:function () {
        this.doRender.apply(this, arguments);
    }});
    dorado.widget.grid.DefaultCellRenderer = $extend(dorado.widget.grid.CellRenderer, {doRender:function (dom, arg) {
        dom.innerText = this.getText(arg.data, arg.column);
        this.renderDirtyFlag(dom, arg);
    }});
    dorado.widget.grid.DefaultCellFooterRenderer = $extend(dorado.widget.grid.CellRenderer, {doRender:function (dom, arg) {
        var entity = arg.data, expired = !!entity.get("$expired");
        dom.innerText = expired ? (arg.column._summaryType ? "..." : "") : this.getText(entity, arg.column);
    }});
    dorado.widget.grid.SubControlCellRenderer = $extend(dorado.widget.grid.CellRenderer, {doRender:function (dom, arg) {
        var subControl;
        if (dom.subControlId && dom.parentNode && dom.parentNode.colId == arg.column._id) {
            subControl = dorado.widget.Component.ALL[dom.subControlId];
        }
        var attach;
        if (!subControl) {
            subControl = this.createSubControl(arg);
            attach = true;
        }
        if (!subControl) {
            $fly(dom).empty();
            return;
        }
        this.refreshSubControl(subControl, arg);
        if (attach) {
            var controlEl = subControl.getDom();
            if (controlEl.parentNode == dom) {
                subControl.refresh();
            } else {
                $fly(dom).empty();
                subControl.render(dom);
                dom.subControlId = subControl._uniqueId;
            }
            jQuery(controlEl).bind("remove", function () {
                var sc = dorado.widget.Component.ALL[this.doradoUniqueId];
                if (sc) {
                    sc.destroy();
                }
            });
            arg.innerGrid.registerInnerControl(subControl);
        }
        this.renderDirtyFlag(dom, arg);
    }});
    dorado.widget.grid.CheckBoxCellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {createSubControl:function (arg) {
        var self = this;
        return new dorado.widget.CheckBox({readOnly:arg.grid.get("readOnly"), iconOnly:true, beforePost:function (control, arg) {
            arg.processDefault = self.beforeCellValueEdit(control._cellEntity, control._cellColumn, control.get("value"));
        }, onPost:function (control) {
            var entity = control._cellEntity, property = control._cellColumn._property, value = control.get("value");
            (entity instanceof dorado.Entity) ? entity.set(property, value) : entity[property] = value;
            self.onCellValueEdit(entity, control._cellColumn);
        }});
    }, refreshSubControl:function (checkbox, arg) {
        var entity = arg.data, property = arg.column._property;
        var value = (entity instanceof dorado.Entity) ? entity.get(property) : entity[property];
        checkbox._cellEntity = entity;
        checkbox._cellColumn = arg.column;
        checkbox.disableListeners();
        checkbox.set("value", !!value);
        checkbox.refresh();
        checkbox.enableListeners();
    }});
    dorado.widget.grid.RadioGroupCellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {getRadioButtons:function (arg) {
        var radioButtons = [];
        var pd = arg.column._propertyDef;
        if (pd && pd._mapping) {
            for (var i = 0; i < pd._mapping.length; i++) {
                var item = pd._mapping[i];
                radioButtons.push({value:item.key, text:item.value});
            }
        }
        return radioButtons;
    }, createSubControl:function (arg) {
        var self = this;
        return new dorado.widget.RadioGroup({readOnly:arg.grid.get("readOnly"), layout:"flow", width:"100%", radioButtons:this.getRadioButtons(arg), beforePost:function (control, arg) {
            arg.processDefault = self.beforeCellValueEdit(control._cellEntity, control._cellColumn, control.get("value"));
        }, onPost:function (control) {
            var entity = control._cellEntity, property = control._cellColumn._property, value = control.get("value");
            (entity instanceof dorado.Entity) ? entity.set(property, value) : entity[property] = value;
            self.onCellValueEdit(entity, control._cellColumn);
        }});
    }, refreshSubControl:function (radioGroup, arg) {
        var entity = arg.data, property = arg.column._property;
        var value = (entity instanceof dorado.Entity) ? entity.get(property) : entity[property];
        radioGroup._cellEntity = entity;
        radioGroup._cellColumn = arg.column;
        radioGroup.disableListeners();
        radioGroup.set("value", value);
        radioGroup.refresh();
        radioGroup.enableListeners();
    }});
    dorado.widget.grid.GroupHeaderRenderer = $extend(dorado.widget.grid.RowRenderer, {doRender:function (dom, arg) {
        if (dom._lazyRender) {
            return;
        }
        var grid = arg.grid, entity = arg.data, processDefault = true;
        if (grid.getListenerCount("onRenderCell")) {
            var arg = {dom:dom, data:entity, rowType:entity.rowType, processDefault:false};
            grid.fireEvent("onRenderCell", grid, arg);
            processDefault = arg.processDefault;
        }
        if (processDefault) {
            dom.firstChild.firstChild.innerText = entity.getText("$groupValue") + " (" + entity.get("$count") + ")";
        }
    }});
    dorado.widget.grid.GroupFooterRenderer = $extend(dorado.widget.grid.DefaultRowRenderer, {renderCell:function (cellRenderer, dom, arg) {
        var grid = arg.grid, entity = arg.data, processDefault = true;
        if (grid.getListenerCount("onRenderCell")) {
            var arg = {dom:dom, data:entity, column:arg.column, rowType:entity.rowType, processDefault:false};
            grid.fireEvent("onRenderCell", grid, arg);
            processDefault = arg.processDefault;
        }
        if (processDefault) {
            if (!!entity.get("$expired")) {
                dom.innerText = arg.column._summaryType ? "..." : "";
            } else {
                dorado.Renderer.render(cellRenderer, dom, arg);
            }
        }
    }});
    dorado.widget.grid.CellEditor = $class({$className:"dorado.widget.grid.CellEditor", cachable:true, hideCellContent:true, bindColumn:function (column) {
        this.grid = column._grid;
        this.column = column;
    }, createDom:function () {
        return $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-cell-editor" + (this.showBorder ? " d-grid-cell-editor-border" : ""), style:{position:"absolute"}});
    }, getDom:function () {
        if (!this._dom) {
            this._dom = this.createDom();
            var fn = function () {
                return false;
            };
            $fly(this._dom).mousewheel(fn);
            this.grid.getDom().appendChild(this._dom);
        }
        return this._dom;
    }, resize:function () {
        var dom = this.getDom(), cell = this.cell;
        var offset = $fly(cell).offset();
        var l = offset.left, t = offset.top, w = cell.offsetWidth, h = cell.offsetHeight;
        if (this.minWidth && this.minWidth > w) {
            w = this.minWidth;
        }
        if (this.minHeight && this.minHeight > h) {
            h = this.minHeight;
        }
        $fly(dom).css({left:l, top:t}).outerWidth(w).outerHeight(h).bringToFront();
    }, shouldShow:function () {
        return this.column && this.column._property;
    }, show:function (parent, cell) {
        this.cell = cell;
        var dom = this.getDom();
        document.body.appendChild(dom);
        if (!this.inited) {
            this.initDom(dom);
            this.inited = true;
        }
        this.refresh();
        var self = this;
        if (!dorado.Browser.mozilla) {
            self.resize();
        } else {
            setTimeout(function () {
                self.resize();
            }, 0);
        }
        $fly(window).one("resize", function () {
            self.hide();
        });
        if (this.hideCellContent) {
            cell.firstChild.style.visibility = "hidden";
        }
        this.visible = true;
    }, hide:function (post) {
        var grid = this.grid;
        if (post !== false && this.post) {
            this.post();
        }
        $DomUtils.getUndisplayContainer().appendChild(this.getDom());
        delete this.data;
        if (grid._currentCellEditor == this) {
            delete grid._currentCellEditor;
        }
        this.visible = false;
        if (this.cell) {
            if (this.hideCellContent) {
                this.cell.firstChild.style.visibility = "";
            }
            this.cell = null;
        }
    }, getEditorValue:function () {
        return null;
    }, beforePost:function (arg) {
        arg.processDefault = this.grid.beforeCellValueEdit(this.data, this.column, this.getEditorValue());
    }, onPost:function (arg) {
        this.grid.onCellValueEdit(this.data, this.column);
    }});
    dorado.widget.grid.ControlCellEditor = $extend(dorado.widget.grid.CellEditor, {getEditorControl:function () {
        return this._editorControl;
    }, getContainerElement:function (dom) {
        return dom;
    }, initDom:function (dom) {
        var control = this._editorControl = this.createEditorControl();
        this.grid.registerInnerControl(control);
        if (!control.get("rendered")) {
            control.render(this.getContainerElement(dom));
        }
    }, resize:function () {
        var dom = this.getDom(), control = this.getEditorControl();
        var ie6 = (dorado.Browser.msie && dorado.Browser.version < "7");
        if (control) {
            if (ie6) {
                control.getDom().style.display = "none";
            }
        }
        $invokeSuper.call(this, arguments);
        if (control) {
            var w = dom.clientWidth, h = dom.clientHeight;
            if (ie6) {
                control.getDom().style.display = "";
            }
            control.set("width", w);
            if (!control.ATTRIBUTES.height.independent) {
                control.set("height", h);
            }
            control.refresh();
        }
    }, show:function (parent, cell) {
        $invokeSuper.call(this, arguments);
        var control = this.getEditorControl();
        control._focusParent = parent;
        setTimeout(function () {
            try {
                control.setFocus();
            }
            catch (e) {
            }
        }, 50);
        if (dorado.Browser.msie) {
            setTimeout(function () {
                try {
                    control.setFocus();
                }
                catch (e) {
                }
            }, 50);
        }
    }, hide:function (post) {
        var control = this.getEditorControl();
        if (control) {
            delete control._focusParent;
        }
        $invokeSuper.call(this, arguments);
    }});
    dorado.widget.grid.SimpleCellEditor = $extend(dorado.widget.grid.ControlCellEditor, {refresh:function () {
        var entity = this.data, column = this.column, editor = this.getEditorControl(), value;
        if (entity) {
            if (entity instanceof dorado.Entity) {
                if (editor instanceof dorado.widget.AbstractTextEditor) {
                    value = entity.getText(column._property);
                    editor.set("text", value);
                } else {
                    value = entity.get(column._property);
                    editor.set("value", value);
                }
            } else {
                value = entity[column._property];
                editor.set("value", value);
            }
        } else {
            editor.set("value", null);
        }
    }, getEditorValue:function () {
        var editor = this.getEditorControl();
        return editor ? editor.get("value") : null;
    }, post:function () {
        var editor = this.getEditorControl();
        return (editor) ? editor.post() : false;
    }, onPost:function (arg) {
        var entity = this.data;
        if (!entity) {
            return null;
        }
        var column = this.column, editor = this.getEditorControl(), value;
        if (entity instanceof dorado.Entity) {
            if (editor instanceof dorado.widget.AbstractTextEditor) {
                value = editor.get("value");
                if (value instanceof dorado.Entity) {
                    entity.set(column._property, value);
                } else {
                    entity.setText(column._property, editor.get("text"));
                }
            } else {
                value = editor.get("value");
                entity.set(column._property, value);
            }
        } else {
            value = editor.get("value");
            entity[column._property] = value;
        }
        $invokeSuper.call(this, arguments);
    }});
    dorado.widget.grid.DefaultCellEditor = $extend(dorado.widget.grid.SimpleCellEditor, {createEditorControl:function () {
        var column = this.column, editor = this.getEditorControl(), grid = column._grid;
        if (!editor) {
            var dt = column.get("dataType"), dtCode = dt ? dt._code : -1;
            var trigger = column.get("trigger"), displayFormat = column.get("displayFormat"), typeFormat = column.get("typeFormat");
            var pd = column._propertyDef;
            if (!trigger) {
                if (pd && pd._mapping) {
                    trigger = new dorado.widget.AutoMappingDropDown({items:pd._mapping});
                } else {
                    if (dtCode == dorado.DataType.PRIMITIVE_BOOLEAN || dtCode == dorado.DataType.BOOLEAN) {
                        editor = new dorado.widget.CheckBox({onValue:true, offValue:false});
                        $fly(editor.getDom()).addClass("d-checkbox-center");
                    }
                }
            }
            if (!editor) {
                if (column._wrappable) {
                    editor = new dorado.widget.TextArea({trigger:trigger});
                    this.minWidth = 120;
                    this.minHeight = 40;
                } else {
                    if (!dtCode || (pd && pd._mapping)) {
                        dt = undefined;
                    }
                    editor = new dorado.widget.TextEditor({dataType:dt, displayFormat:displayFormat, typeFormat:typeFormat, trigger:trigger});
                }
            }
        }
        var cellEditor = this;
        editor.addListener("beforePost", function (self, arg) {
            cellEditor.beforePost(arg);
        }).addListener("onPost", function (self, arg) {
            cellEditor.onPost(arg);
        }).addListener("onBlur", function (self) {
            if ((new Date() - cellEditor._showTimestamp) > 300) {
                cellEditor.hide();
            }
        });
        editor._cellEditor = cellEditor;
        editor._propertyDef = column._propertyDef;
        return editor;
    }, show:function (parent, cell) {
        this._showTimestamp = new Date();
        var editor = this.getEditorControl();
        var sameEditor = (dorado.widget.getMainFocusedControl() == editor);
        if (sameEditor && editor) {
            editor.onBlur();
        }
        $invokeSuper.call(this, arguments);
        if (sameEditor && editor) {
            editor.onFocus();
        }
    }});
    dorado.widget.grid.DataColumn = $extend(dorado.widget.grid.Column, {$className:"dorado.widget.grid.DataColumn", ATTRIBUTES:{width:{defaultValue:80}, caption:{getter:function () {
        var caption = this._caption;
        if (caption == null && this._propertyDef) {
            caption = this._propertyDef.get("label");
        }
        if (caption == null) {
            caption = (this._name.charAt(0) == "_" ? this._property : this._name);
        }
        return caption;
    }}, name:{setter:function (p, v) {
        this._name = v;
        if (!this.getAttributeWatcher().getWritingTimes("property")) {
            this._property = v;
        }
    }}, property:{writeOnce:true, setter:function (p, v) {
        this._property = v;
        if (!this.getAttributeWatcher().getWritingTimes("name")) {
            this._name = v;
        }
    }}, align:{}, headerAlign:{}, footerAlign:{}, dataType:{getter:function () {
        var dt = dorado.LazyLoadDataType.dataTypeGetter.apply(this, arguments);
        if (!dt && this._propertyDef) {
            dt = this._propertyDef.get("dataType");
        }
        return dt;
    }}, dataTypeRepository:{getter:function () {
        if (this._grid) {
            var view = this._grid.get("view");
            if (view) {
                return view.get("dataTypeRepository");
            }
        }
        return null;
    }, readOnly:true}, readOnly:{skipRefresh:true, getter:function () {
        var readOnly = this._readOnly;
        if (!readOnly && this._propertyDef) {
            readOnly = this._propertyDef.get("readOnly");
        }
        return readOnly;
    }}, required:{getter:function () {
        var required = this._required;
        if (!required && this._propertyDef) {
            required = this._propertyDef.get("required");
        }
        return required;
    }}, typeFormat:{skipRefresh:true, getter:function () {
        var typeFormat = this._typeFormat;
        if (!typeFormat && this._propertyDef) {
            typeFormat = this._propertyDef.get("typeFormat");
        }
        return typeFormat;
    }}, displayFormat:{getter:function () {
        var displayFormat = this._displayFormat;
        if (!displayFormat && this._propertyDef) {
            displayFormat = this._propertyDef.get("displayFormat");
        }
        return displayFormat;
    }}, trigger:{skipRefresh:true}, renderer:{}, footerRenderer:{}, summaryType:{writeOnce:true}, summaryRenderer:{}, editor:{skipRefresh:true}, sortState:{}, wrappable:{}, propertyDef:{readOnly:true}, filterable:{defaultValue:true}}, EVENTS:{onRenderCell:{}, onRenderFooterCell:{}, onGetCellEditor:{}}});
    dorado.widget.grid.ColumnGroup = $extend([dorado.widget.grid.Column, dorado.widget.grid.ColumnModel], {$className:"dorado.widget.grid.ColumnGroup", ATTRIBUTES:{grid:{setter:function (p, v) {
        this._grid = v;
        this._columns.each(function (column) {
            column.set(p, v);
        });
    }}}, constructor:function (config) {
        this._columns = new dorado.widget.grid.ColumnList(this);
        $invokeSuper.call(this, arguments);
    }});
    dorado.widget.grid.IndicatorColumn = $extend(dorado.widget.grid.DataColumn, {ATTRIBUTES:{width:{defaultValue:16}, caption:{defaultValue:"Indicator"}, supportsOptionMenu:{defaultValue:false}, headerRenderer:{neverEvalDefaultValue:true, defaultValue:function (dom, arg) {
        $fly(dom).empty();
        $fly(dom.parentNode).addClass("indicator");
    }}, renderer:{neverEvalDefaultValue:true, defaultValue:function (dom, arg) {
        if (arg.data.rowType) {
            return;
        }
        var className = "indicator-none";
        if (arg.data instanceof dorado.Entity) {
            switch (arg.data.state) {
              case dorado.Entity.STATE_NEW:
                className = "indicator-new";
                break;
              case dorado.Entity.STATE_MODIFIED:
                className = "indicator-modified";
                break;
            }
        }
        dom.innerHTML = "";
        dom.className = "cell " + className;
    }}}});
    dorado.widget.grid.RowNumColumn = $extend(dorado.widget.grid.DataColumn, {ATTRIBUTES:{width:{defaultValue:16}, caption:{defaultValue:"RowNum"}, align:{defaultValue:"center"}, supportsOptionMenu:{defaultValue:false}, headerRenderer:{neverEvalDefaultValue:true, defaultValue:function (dom, arg) {
        $fly(dom).empty();
        $fly(dom.parentNode).addClass("row-num");
    }}, renderer:{neverEvalDefaultValue:true, defaultValue:function (dom, arg) {
        var row = dom.parentNode.parentNode;
        dom.innerHTML = arg.grid._groupProperty ? "" : row.itemIndex + 1;
    }}}});
    RowSelectorCellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {checkboxMap:{}, cellMouseDownListener:function () {
        return false;
    }, gridOnSelectionChangedListener:function (grid, arg) {
        var itemModel = grid._itemModel;
        var selection = grid.get("selection"), selectionMode = grid._selectionMode, removed = arg.removed, added = arg.added, checkbox;
        this.selection = selection;
        if (selectionMode == "multiRows") {
            if (removed) {
                for (var i = 0; i < removed.length; i++) {
                    checkbox = this.checkboxMap[itemModel.getItemId(removed[i])];
                    if (checkbox) {
                        checkbox.set("checked", false);
                    }
                }
            }
            if (added) {
                for (var i = 0; i < added.length; i++) {
                    checkbox = this.checkboxMap[itemModel.getItemId(added[i])];
                    if (checkbox) {
                        checkbox.set("checked", true);
                    }
                }
            }
        } else {
            if (selectionMode == "singleRow") {
                checkbox = this.checkboxMap[itemModel.getItemId(removed)];
                if (checkbox) {
                    checkbox.set("checked", false);
                }
                checkbox = this.checkboxMap[itemModel.getItemId(added)];
                if (checkbox) {
                    checkbox.set("checked", true);
                }
            }
        }
    }, createSubControl:function (arg) {
        if (arg.data.rowType) {
            return null;
        }
        var self = this;
        if (!this._listenerBinded) {
            this._listenerBinded = true;
            arg.grid.addListener("onSelectionChange", $scopify(this, this.gridOnSelectionChangedListener));
        }
        var checkbox = new dorado.widget.CheckBox({onValueChange:function (checkbox) {
            var grid = arg.grid, innerGrid = grid._innerGrid;
            var entity = grid.get("itemModel").getItemById(checkbox._selectionEntityId), checked = checkbox.get("checked");
            innerGrid.replaceSelection.apply(innerGrid, checked ? [null, [entity]] : [[entity], null]);
        }});
        $fly(checkbox.getDom()).mousedown(this.cellMouseDownListener);
        return checkbox;
    }, refreshSubControl:function (checkbox, arg) {
        if (arg.data.rowType) {
            checkbox.destroy();
            return;
        }
        var grid = arg.grid, entity = arg.data, selection = this.selection, selectionMode = grid._selectionMode, config = {};
        if (selectionMode == "multiRows") {
            config.checked = (selection && selection.indexOf(entity) >= 0);
            config.readOnly = false;
        } else {
            if (selectionMode == "singleRow") {
                config.checked = (arg.data == selection);
                config.readOnly = false;
            } else {
                config.checked = false;
                config.readOnly = true;
            }
        }
        var oldId = checkbox._selectionEntityId;
        if (oldId != null) {
            delete this.checkboxMap[oldId];
        }
        checkbox.set(config);
        checkbox.refresh();
        checkbox._selectionEntityId = grid._itemModel.getItemId(entity);
        this.checkboxMap[checkbox._selectionEntityId] = checkbox;
    }});
    dorado.widget.grid.RowSelectorColumn = $extend(dorado.widget.grid.DataColumn, {ATTRIBUTES:{width:{defaultValue:16}, align:{defaultValue:"center"}, caption:{defaultValue:"RowSelector"}, supportsOptionMenu:{defaultValue:false}, headerRenderer:{neverEvalDefaultValue:true, defaultValue:function (dom, arg) {
        function getMenu(column) {
            var menu = column._rowSelectorMenu;
            if (!menu) {
                menu = column._rowSelectorMenu = new dorado.widget.menu.Menu({items:[{name:"select-all", caption:$resource("dorado.grid.SelectAll"), onClick:function (self) {
                    grid.selectAll();
                }}, {name:"unselect-all", caption:$resource("dorado.grid.UnselectAll"), onClick:function (self) {
                    grid.unselectAll();
                }}, {name:"select-invert", caption:$resource("dorado.grid.SelectInvert"), onClick:function (self) {
                    grid.selectInvert();
                }}]});
                grid.registerInnerControl(menu);
            }
            return menu;
        }
        var grid = arg.grid, column = arg.column, cell = dom.parentNode;
        $fly(dom).empty();
        $fly(cell).addClass("row-selector").click(function () {
            var menu = getMenu(column);
            menu.show({anchorTarget:cell, align:"innerright", vAlign:"bottom"});
            return false;
        });
    }}, renderer:{defaultValue:function () {
        return new RowSelectorCellRenderer();
    }}}});
    dorado.widget.grid.FilterBarCellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {createSubControl:function (arg) {
        var column = arg.column;
        if (column._property && column._filterable) {
            var self = this, grid = arg.grid;
            var textEditor = new dorado.widget.TextEditor({width:"100%", property:column._property, onPost:function (textEditor) {
                var text = textEditor.get("text"), operator, value;
                if (text) {
                    var v = dorado.Toolkits.parseFilterValue(text);
                    operator = v[0];
                    value = v[1];
                    var pd = column._propertyDef;
                    if (pd && pd._mapping) {
                        value = operator + pd.getMappedKey(value);
                    }
                    var dataType = column.get("dataType");
                    if (dataType) {
                        value = operator + dataType.parse(value, column.get("typeFormat"));
                    }
                } else {
                    operator = value = "";
                }
                var filterEntity = grid.get("filterEntity");
                filterEntity.disableObservers();
                filterEntity.set(column._property, value);
                filterEntity.enableObservers();
            }, onKeyDown:function (textEditor, arg) {
                if (arg.keyCode == 13) {
                    textEditor.post();
                    grid.filter();
                }
            }});
            var trigger = column.get("trigger"), pd = column._propertyDef;
            if (!trigger) {
                if (pd && pd._mapping) {
                    trigger = new dorado.widget.AutoMappingDropDown({items:pd._mapping});
                }
            }
            var dataType = column.get("dataType");
            if (!trigger && dataType && (dataType._code == dorado.DataType.PRIMITIVE_BOOLEAN || dataType._code == dorado.DataType.BOOLEAN)) {
                trigger = $singleton(function () {
                    return new dorado.widget.ListDropDown({property:"value", displayProperty:"label", items:[{value:false, label:$resource("dorado.core.BooleanFalse")}, {value:true, label:$resource("dorado.core.BooleanTrue")}]});
                });
                trigger._isBooeanDropDown = true;
            }
            if (trigger) {
                textEditor.set("trigger", trigger);
            }
            return textEditor;
        } else {
            return null;
        }
    }, refreshSubControl:function (textEditor, arg) {
        var entity = arg.data, column = arg.column, property = column._property, pd = column._propertyDef;
        var text = entity.getText(property);
        if (text) {
            var v = dorado.Toolkits.parseFilterValue(text), operator = v[0], value = v[1];
            if (pd && pd._mapping) {
                value = operator + pd.getMappedValue(value);
            }
            var dataType = column.get("dataType");
            if (dataType) {
                value = operator + dataType.toText(value, column.get("typeFormat"));
            }
        }
        textEditor._cellColumn = arg.column;
        textEditor.disableListeners();
        textEditor.set("value", value);
        textEditor.refresh();
        textEditor.enableListeners();
    }});
    dorado.Toolkits.registerPrototype("gridcolumn", {"Group":dorado.widget.grid.ColumnGroup, "*":dorado.widget.grid.IndicatorColumn, "#":dorado.widget.grid.RowNumColumn, "[]":dorado.widget.grid.RowSelectorColumn});
})();
(function () {
    var MIN_COL_WIDTH = 8;
    GroupedItemIterator = $extend(dorado.util.Iterator, {constructor:function (groups, showFooter, nextIndex) {
        this.groups = groups;
        this.showFooter = showFooter;
        if (nextIndex > 0) {
            nextIndex--;
            var g;
            for (var i = 0; i < groups.length; i++) {
                g = groups[i], gs = g.entities.length + 1 + (showFooter ? 1 : 0);
                if (gs <= nextIndex) {
                    nextIndex -= gs;
                } else {
                    this.groupIndex = i;
                    this.entityIndex = nextIndex - 1;
                    this.isFirst = this.isLast = false;
                    this.currentGroup = g;
                    break;
                }
            }
        } else {
            this.first();
        }
    }, first:function () {
        this.groupIndex = 0;
        this.entityIndex = -2;
        this.isFirst = true;
        this.isLast = (this.groups.length == 0);
        this.currentGroup = this.groups[this.groupIndex];
    }, last:function () {
        this.groupIndex = this.groups.length - 1;
        this.entityIndex = this.currentGroup.length + (this.showFooter ? 1 : 0);
        this.isFirst = (this.groups.length == 0);
        this.isLast = true;
        this.currentGroup = this.groups[this.groupIndex];
    }, hasPrevious:function () {
        if (this.isFirst || this.groups.length == 0) {
            return false;
        }
        if (this.groupIndex <= 0 && this.entityIndex <= -1) {
            return false;
        }
        return true;
    }, hasNext:function () {
        if (this.isLast || this.groups.length == 0) {
            return false;
        }
        var maxEntityIndex = this.currentGroup.entities.length + (this.showFooter ? 0 : -1);
        if (this.groupIndex >= this.groups.length - 1 && this.entityIndex >= maxEntityIndex) {
            return false;
        }
        return true;
    }, current:function () {
        if (this.entityIndex == -1) {
            return this.currentGroup.headerEntity;
        } else {
            if (this.entityIndex >= this.currentGroup.entities.length) {
                return this.currentGroup.footerEntity;
            } else {
                return this.currentGroup.entities[this.entityIndex];
            }
        }
    }, previous:function () {
        if (this.entityIndex >= 0) {
            this.entityIndex--;
        } else {
            if (this.groupIndex > 0) {
                this.currentGroup = this.groups[--this.groupIndex];
                this.entityIndex = this.currentGroup.entities.length + (this.showFooter ? 0 : -1);
            } else {
                this.isFirst = true;
                this.entityIndex = -1;
            }
        }
        return (this.isFirst) ? null : this.current();
    }, next:function () {
        var maxEntityIndex = this.currentGroup.entities.length + (this.showFooter ? 0 : -1);
        if (this.entityIndex < maxEntityIndex) {
            this.entityIndex++;
        } else {
            if (this.groupIndex < this.groups.length - 1) {
                this.currentGroup = this.groups[++this.groupIndex];
                this.entityIndex = -1;
            } else {
                this.isLast = true;
                this.entityIndex = maxEntityIndex + 1;
            }
        }
        return (this.isLast) ? null : this.current();
    }, createBookmark:function () {
        return {groupIndex:this.groupIndex, entityIndex:this.entityIndex, currentEntity:this.currentEntity, isFirst:this.isFirst, isLast:this.isLast};
    }, restoreBookmark:function (bookmark) {
        this.groupIndex = bookmark.groupIndex;
        this.entityIndex = bookmark.entityIndex;
        this.currentEntity = bookmark.currentEntity;
        this.isFirst = bookmark.isFirst;
        this.isLast = bookmark.isLast;
    }});
    dorado.widget.grid.ItemModel = $extend(dorado.widget.list.ItemModel, {constructor:function (grid) {
        this.grid = grid;
        var items = this._items, footerData = {$expired:true};
        var footerEntity = this.footerEntity = (items instanceof dorado.EntityList) ? items.createChild(footerData, true) : new dorado.Entity(footerData);
        footerEntity.acceptUnknownProperty = true;
        footerEntity.disableEvents = true;
        footerEntity._setObserver({grid:grid, entityMessageReceived:function (messageCode, arg) {
            if (messageCode == 0 || messageCode == dorado.Entity._MESSAGE_DATA_CHANGED || messageCode == dorado.Entity._MESSAGE_REFRESH_ENTITY) {
                var grid = this.grid;
                if (!grid._innerGrid) {
                    return;
                }
                if (grid._domMode == 2) {
                    grid._fixedInnerGrid.refreshFrameFooter();
                }
                grid._innerGrid.refreshFrameFooter();
            }
        }});
        var filterEntity = this.filterEntity = new dorado.Entity();
        filterEntity.acceptUnknownProperty = true;
        filterEntity.disableEvents = true;
        filterEntity._setObserver({grid:grid, entityMessageReceived:function (messageCode, arg) {
            if (messageCode == 0 || messageCode == dorado.Entity._MESSAGE_DATA_CHANGED || messageCode == dorado.Entity._MESSAGE_REFRESH_ENTITY) {
                var grid = this.grid;
                if (!grid._innerGrid) {
                    return;
                }
                if (grid._domMode == 2) {
                    grid._fixedInnerGrid.refreshFilterBar();
                }
                grid._innerGrid.refreshFilterBar();
            }
        }});
        $invokeSuper.call(this, arguments);
    }, getItems:function () {
        return this._originItems || this._items;
    }, setItems:function (items) {
        if ((this._originItems || this._items) == items) {
            return;
        }
        var filterEntity = this.filterEntity;
        filterEntity.disableObservers();
        filterEntity.clearData();
        filterEntity.enableObservers();
        $invokeSuper.call(this, arguments);
        this.refreshItems();
    }, clearSortFlags:function () {
        var grid = this.grid;
        if (grid._columnsInfo) {
            var columns = grid._columnsInfo.dataColumns;
            for (var i = 0; i < columns.length; i++) {
                columns[i].set("sortState", null);
            }
        }
    }, refreshItems:function () {
        var grid = this.grid;
        if (grid._rendered) {
            this.clearSortFlags();
            grid.setDirtyMode(false);
            if (grid._groupProperty) {
                this.group();
            } else {
                this.refreshSummary();
            }
        }
    }, extractSummaryColumns:function (dataColumns) {
        var columns = [];
        for (var i = 0; i < dataColumns.length; i++) {
            var column = dataColumns[i];
            if (!column._summaryType) {
                continue;
            }
            var cal = dorado.SummaryCalculators[column._summaryType];
            if (cal) {
                columns.push({name:column._name, property:column._property, calculator:cal});
            }
        }
        return columns.length ? columns : null;
    }, initSummary:function (summary) {
        var columns = this._summaryColumns;
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i], cal = col.calculator;
            summary[col.property] = (cal instanceof Function) ? 0 : cal.getInitialValue();
        }
    }, accumulate:function (entity, summary) {
        var columns = this._summaryColumns;
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i], cal = col.calculator;
            summary[col.property] = ((cal instanceof Function) ? cal : cal.accumulate)(summary[col.property], entity, col.property);
        }
    }, finishSummary:function (summary) {
        var columns = this._summaryColumns;
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i], cal = col.calculator;
            if (!(cal instanceof Function)) {
                summary[col.property] = cal.getFinalValue(summary[col.property]);
            }
        }
        delete summary.$expired;
    }, group:function () {
        function getGroupSysEntityObserver(grid) {
            if (!grid._groupSysEntityObserver) {
                grid._groupSysEntityObserver = {grid:grid, entityMessageReceived:function (messageCode, arg) {
                    if (messageCode == 0 || messageCode == dorado.Entity._MESSAGE_DATA_CHANGED || messageCode == dorado.Entity._MESSAGE_REFRESH_ENTITY) {
                        if (!this.grid._rendered) {
                            return;
                        }
                        this.grid.refreshEntity(arg.entity);
                    }
                }};
            }
            return grid._groupSysEntityObserver;
        }
        this.filter();
        var items = this._items, entities, isArray = items instanceof Array;
        if (!isArray) {
            for (var i = 1; i <= items.pageCount; i++) {
                if (!items.isPageLoaded(i)) {
                    throw ResourceException("dorado.grid.GroupOnUnloadPage");
                }
            }
        }
        var grid = this.grid, groupProperty = grid._groupProperty;
        entities = isArray ? items.slice(0) : items.toArray();
        if (grid._groupOnSort) {
            dorado.DataUtil.sort(entities, {property:groupProperty});
        }
        this.entityCount = entities.length;
        var columns = this._summaryColumns;
        var groups = this.groups = [], groupMap = this.groupMap = {}, entityMap = this.entityMap = {};
        var entity, groupValue, curGroupValue, curGroup, groupEntities, headerEntity, footerEntity, summary, totalSummary = this.footerEntity._data;
        if (columns) {
            this.initSummary(totalSummary);
        }
        for (var i = 0; i < entities.length; i++) {
            entity = entities[i];
            groupValue = ((entity instanceof dorado.Entity) ? entity.getText(groupProperty) : (entity[groupProperty] + "")) + "";
            if (curGroupValue != groupValue) {
                if (curGroup) {
                    headerEntity.set("$count", groupEntities.length);
                    footerEntity.set("$count", groupEntities.length);
                    if (columns) {
                        this.finishSummary(summary);
                    }
                }
                curGroupValue = groupValue;
                headerEntity = isArray ? new dorado.Entity() : items.createChild(null, true);
                headerEntity.rowType = "header";
                headerEntity.acceptUnknownProperty = true;
                headerEntity.disableEvents = true;
                headerEntity.set("$groupValue", groupValue);
                headerEntity._setObserver(getGroupSysEntityObserver(grid));
                footerEntity = isArray ? new dorado.Entity() : items.createChild(null, true);
                footerEntity.rowType = "footer";
                footerEntity.acceptUnknownProperty = true;
                footerEntity.disableEvents = true;
                footerEntity.set("$groupValue", groupValue);
                footerEntity._setObserver(getGroupSysEntityObserver(grid));
                groupEntities = [], summary = footerEntity._data;
                curGroup = {expanded:true, entities:groupEntities, headerEntity:headerEntity, footerEntity:footerEntity};
                if (columns) {
                    this.initSummary(summary);
                }
                groups.push(curGroup);
                groupMap[groupValue] = curGroup;
            }
            if (columns) {
                this.accumulate(entity, summary);
                this.accumulate(entity, totalSummary);
            }
            groupEntities.push(entity);
            if (entity instanceof dorado.Entity) {
                entityMap[entity.entityId] = groupValue;
            }
        }
        if (curGroup) {
            headerEntity.set("$count", groupEntities.length);
            footerEntity.set("$count", groupEntities.length);
            if (columns) {
                this.finishSummary(summary);
            }
        }
        if (columns) {
            this.finishSummary(totalSummary);
        }
        this.footerEntity.timestamp = dorado.Core.getTimestamp();
        this.clearSortFlags();
    }, ungroup:function () {
        delete this.groups;
        delete this.groupMap;
        delete this.entityMap;
        this.clearSortFlags();
    }, filter:function (filterParams) {
        var hasParam = (filterParams && filterParams.length > 0);
        if (hasParam) {
            this.ungroup();
        }
        $invokeSuper.call(this, arguments);
        if (hasParam) {
            this.refreshSummary();
        }
    }, refreshSummary:function () {
        if (!this._summaryColumns) {
            return;
        }
        var totalSummary = this.footerEntity._data;
        if (this.groups) {
            var groups = this.groups, columns = this._summaryColumns, summary, entity;
            this.initSummary(totalSummary);
            for (var i = 0; i < groups.length; i++) {
                var group = groups[i], entities = group.entities, headerEntity = group.headerEntity, footerEntity = group.footerEntity;
                summary = (footerEntity.get("$expired")) ? footerEntity._data : null;
                if (summary) {
                    this.initSummary(summary);
                }
                for (var j = 0; j < entities.length; j++) {
                    entity = entities[j];
                    if (summary) {
                        this.accumulate(entity, summary);
                    }
                    this.accumulate(entity, totalSummary);
                }
                if (summary) {
                    this.finishSummary(summary);
                    headerEntity.set("$count", entities.length);
                    footerEntity.set("$count", entities.length);
                    headerEntity.set("$expired", false);
                    footerEntity.set("$expired", false);
                }
            }
            this.finishSummary(totalSummary);
        } else {
            this.initSummary(totalSummary);
            if (this._items) {
                var self = this;
                if (this._items instanceof Array) {
                    jQuery.each(this._items, function (i, entity) {
                        self.accumulate(entity, totalSummary);
                    });
                } else {
                    this._items.each(function (entity) {
                        self.accumulate(entity, totalSummary);
                    });
                }
            }
            this.finishSummary(totalSummary);
        }
        this.footerEntity.timestamp = dorado.Core.getTimestamp();
        this.footerEntity.sendMessage(0);
    }, iterator:function () {
        if (this.groups) {
            return new GroupedItemIterator(this.groups, this.grid._showGroupFooter, this._startIndex || 0);
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, getItemCount:function () {
        if (this.groups) {
            return this.entityCount + this.groups.length * (this.grid._showGroupFooter ? 2 : 1);
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, getItemAt:function (index) {
        if (this.groups) {
            var grid = this.grid, groupProperty = grid.groupProperty, groups = this.groups, showFooter = grid._showGroupFooter, g;
            for (var i = 0; i < groups.length; i++) {
                g = groups[i], gs = g.entities.length + 1 + (showFooter ? 1 : 0);
                if (gs <= index) {
                    index -= gs;
                } else {
                    if (index == 0) {
                        return g.headerEntity;
                    }
                    if (index <= g.entities.length) {
                        return g.entities[index - 1];
                    }
                    return g.footerEntity;
                }
            }
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, getItemIndex:function (item) {
        if (this.groups) {
            var grid = this.grid, groupProperty = grid._groupProperty, groups = this.groups, showFooter = grid._showGroupFooter;
            var groupValue;
            if (item.rowType) {
                groupValue = item.get("$groupValue");
            } else {
                groupValue = ((item instanceof dorado.Entity) ? this.entityMap[item.entityId] : item[groupProperty]) + "";
            }
            var group = this.groupMap[groupValue], index = 0;
            for (var i = 0; i < groups.length; i++) {
                var g = groups[i];
                if (g == group) {
                    break;
                }
                index += g.entities.length + 1;
                if (showFooter) {
                    index++;
                }
            }
            var i = group.entities.indexOf(item);
            if (i < 0) {
                i = (item.rowType == "header" ? -1 : group.entities.length);
            }
            index += i + 1;
            return index;
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, sort:function (sortParams, comparator) {
        if (!this.getItemCount()) {
            return;
        }
        if (!(sortParams instanceof Array)) {
            sortParams = [sortParams];
        }
        var grid = this.grid, columns = grid._columnsInfo.dataColumns, sortParamMap = {};
        for (var i = 0; i < sortParams.length; i++) {
            var sortParam = sortParams[i];
            if (sortParam.property) {
                sortParamMap[sortParam.property] = !!sortParam.desc;
            }
        }
        for (var i = 0; i < columns.length; i++) {
            var column = columns[i], desc = sortParamMap[column._property];
            if (desc == null) {
                column.set("sortState", null);
            } else {
                column.set("sortState", desc ? "desc" : "asc");
            }
        }
        if (this.groups) {
            var groups = this.groups;
            for (var i = 0; i < groups.length; i++) {
                var group = groups[i];
                dorado.DataUtil.sort(group.entities, sortParams, comparator);
            }
        } else {
            var items = this._items;
            if (items instanceof Array) {
                $invokeSuper.call(this, arguments);
            } else {
                var changed = false;
                for (var i = 1; i <= items.pageCount; i++) {
                    if (!items.isPageLoaded(i)) {
                        continue;
                    }
                    var page = items.getPage(i);
                    var array = page.toArray();
                    dorado.DataUtil.sort(array, sortParams, comparator);
                    var entry = page.first, j = 0;
                    while (entry != null) {
                        entry.data = array[j];
                        page._registerEntry(entry);
                        entry = entry.next;
                        j++;
                    }
                    changed = true;
                }
                if (changed) {
                    items.timestamp = dorado.Core.getTimestamp();
                    items.sendMessage(0);
                }
            }
        }
    }, getAllDataEntities:function () {
        var v = [];
        for (var it = this.iterator(); it.hasNext(); ) {
            var entity = it.next();
            if (!entity.rowType) {
                v.push(entity);
            }
        }
        return v;
    }});
    var overrides = {constructor:function (itemModel) {
        this._itemModel = itemModel;
    }};
    var dp = ["setStartIndex", "setItemDomSize", "setScrollPos", "setItems"];
    dorado.Object.eachProperty(dorado.widget.list.ItemModel.prototype, function (p, v) {
        if (v instanceof Function && p != "constructor") {
            if (dp.indexOf(p) >= 0) {
                overrides[p] = dorado._NULL_FUNCTION;
            } else {
                overrides[p] = new Function("return this._itemModel." + p + ".apply(this._itemModel, arguments)");
            }
        }
    });
    var PassiveItemModel = $extend(dorado.widget.list.ItemModel, overrides);
    function getCellOffsetTop(cell, rowHeight) {
        return (dorado.Browser.safari || dorado.Browser.chrome) ? (cell.parentNode.sectionRowIndex * (rowHeight + 1)) : cell.offsetTop;
    }
    dorado.widget.AbstractGrid = $extend([dorado.widget.AbstractList, dorado.widget.grid.ColumnModel], {$className:"dorado.widget.AbstractGrid", ATTRIBUTES:{className:{defaultValue:"d-grid"}, rowHeight:{defaultValue:18}, headerRowHeight:{defaultValue:20}, footerRowHeight:{defaultValue:22}, scrollMode:{defaultValue:"lazyRender"}, fixedColumnCount:{defaultValue:0}, showHeader:{defaultValue:true}, showFooter:{}, readOnly:{}, dynaRowHeight:{writeBeforeReady:true}, cellRenderer:{}, headerRenderer:{}, footerRenderer:{}, rowRenderer:{}, groupHeaderRenderer:{}, groupFooterRenderer:{}, currentColumn:{skipRefresh:true, setter:function (p, v) {
        this.setCurrentColumn(v);
    }}, dataColumns:{getter:function () {
        return this._columnsInfo ? this._columnsInfo.dataColumns : [];
    }, readOnly:true}, editing:{defaultValue:true, readOnly:true, skipRefresh:true, getter:function (p, v) {
        return this._editing;
    }}, allowNoCurrent:{skipRefresh:true, setter:function (p, v) {
        this._allowNoCurrent = v;
        if (this._fixedInnerGrid) {
            this._fixedInnerGrid.set("allowNoCurrent", v);
        }
        if (this._innerGrid) {
            this._innerGrid.set("allowNoCurrent", v);
        }
    }}, selectionMode:{defaultValue:"none", skipRefresh:true, setter:function (p, v) {
        this._selectionMode = v;
        if (this._innerGrid) {
            this._innerGrid.set("selectionMode", v);
        }
    }}, selection:{getter:function (p, v) {
        if (this._innerGrid) {
            return this._innerGrid.get("selection");
        } else {
            return ("multiRows" == this._selectionMode) ? [] : null;
        }
    }, setter:function (p, v) {
        if (v == null && ["multiRows", "multiCells"].indexOf(this._selectionMode) >= 0) {
            v = [];
        }
        if (this._innerGrid) {
            this._innerGrid.set("selection", v);
        }
    }}, groupProperty:{skipRefresh:true, setter:function (p, v) {
        if (this._groupProperty == v) {
            return;
        }
        this._groupProperty = v;
        if (v != null) {
            this._itemModel.group();
        } else {
            this._itemModel.ungroup();
        }
    }}, groupOnSort:{defaultValue:true, skipRefresh:true}, showGroupFooter:{}, footerEntity:{readOnly:true, getter:function (p) {
        return this._itemModel.footerEntity;
    }}, showFilterBar:{}, filterEntity:{readOnly:true, getter:function (p) {
        return this._itemModel.filterEntity;
    }}, stretchColumnsMode:{defaultValue:"off"}}, EVENTS:{onGetCellEditor:{}, onDataRowClick:{}, onGetEntityComparator:{}, onRenderRow:{}, onRenderCell:{}, onRenderHeaderCell:{}, onRenderFooterCell:{}, onHeaderClick:{}, beforeCellValueEdit:{}, onCellValueEdit:{}}, constructor:function () {
        this._columns = new dorado.widget.grid.ColumnList(this, dorado._GET_NAME);
        this._grid = this;
        $invokeSuper.call(this, arguments);
    }, createItemModel:function () {
        return new dorado.widget.grid.ItemModel(this);
    }, getCurrentItem:function () {
        return this._innerGrid.getCurrentItem();
    }, notifySizeChange:function () {
        if (!this.xScroll || !this.yScroll) {
            $invokeSuper.call(this, arguments);
        }
    }, createDom:function () {
        var dom = $invokeSuper.call(this, arguments);
        dom.style.position = "relative";
        $fly(dom).mousewheel($scopify(this, function (evt, delta) {
            if (this._divScroll) {
                this._divScroll.scrollTop -= delta * this._rowHeight * 2;
            }
            if (this._currentCellEditor) {
                this._currentCellEditor.hide();
            }
            return false;
        }));
        return dom;
    }, refreshDom:function (dom) {
        function getDivScroll() {
            if (this._divScroll) {
                return this._divScroll;
            }
            var div = this._divScroll = $DomUtils.xCreateElement({tagName:"DIV", style:{width:"100%", height:"100%", overflowX:(xScroll && domMode == 0) ? "scroll" : "hidden", overflowY:yScroll ? "scroll" : "hidden"}, content:"^DIV"});
            $fly(div).bind("scroll", $scopify(this, this.onScroll));
            this._divViewPort = div.firstChild;
            dom.appendChild(div);
            return div;
        }
        function registerInnerControl(innerGrid) {
            var grid = this;
            innerGrid.addListener("onDataRowClick", function (self, arg) {
                if (grid.getListenerCount("onDataRowClick")) {
                    var row = $DomUtils.findParent(arg.event.target, function (parentNode) {
                        return (parentNode.parentNode == innerGrid._dataTBody);
                    });
                    if (row) {
                        var cell = $DomUtils.findParent(arg.event.target, function (parentNode) {
                            return parentNode.parentNode == row;
                        }, true);
                        arg.column = grid._columnsInfo.idMap[cell.colId];
                    }
                    grid.fireEvent("onDataRowClick", grid, arg);
                }
            });
            this.registerInnerControl(innerGrid);
        }
        function getFixedInnerGrid() {
            if (this._fixedInnerGrid) {
                return this._fixedInnerGrid;
            }
            var innerGrid = this._fixedInnerGrid = this.createInnerGrid(true);
            innerGrid.set({allowNoCurrent:this._allowNoCurrent});
            registerInnerControl.call(this, innerGrid);
            return innerGrid;
        }
        function getInnerGrid() {
            if (this._innerGrid) {
                return this._innerGrid;
            }
            var innerGrid = this._innerGrid = this.createInnerGrid(), self = this;
            innerGrid.set({allowNoCurrent:this._allowNoCurrent, selectionMode:this._selectionMode, onSelectionChange:function (innerGrid, arg) {
                self.fireEvent("onSelectionChange", self, arg);
            }});
            registerInnerControl.call(this, innerGrid);
            this._innerGridDom = innerGrid.getDom();
            return innerGrid;
        }
        function getFixedInnerGridWrapper() {
            var wrapper = this._fixedInnerGridWrapper;
            if (!wrapper) {
                var wrapper = this._fixedInnerGridWrapper = document.createElement("DIV");
                with (wrapper.style) {
                    overflowX = "visible";
                    overflowY = "hidden";
                    position = "absolute";
                    left = top = 0;
                }
                dom.appendChild(wrapper);
            }
            return wrapper;
        }
        function getInnerGridWrapper() {
            var wrapper = this._innerGridWrapper;
            if (!wrapper) {
                var wrapper = this._innerGridWrapper = document.createElement("DIV");
                with (wrapper.style) {
                    overflow = "hidden";
                    position = "absolute";
                    left = top = 0;
                }
                dom.appendChild(wrapper);
            }
            return wrapper;
        }
        $invokeSuper.call(this, arguments);
        if (!this._columns.size) {
            this.addColumns([{name:"empty", caption:""}]);
        }
        var ignoreItemTimestamp = (this._ignoreItemTimestamp === undefined) ? true : this._ignoreItemTimestamp;
        delete this._ignoreItemTimestamp;
        var fixedColumnCount;
        if (!this.getRealWidth() || !this.getRealHeight() || this._groupProperty) {
            fixedColumnCount = 0;
        } else {
            fixedColumnCount = this._fixedColumnCount;
            if (fixedColumnCount > this._columns.size) {
                fixedColumnCount = this._columns.size;
            }
        }
        this._realStretchColumnsMode = (fixedColumnCount > 0) ? "off" : this._stretchColumnsMode;
        var columnsInfo = this._columnsInfo = this.getColumnsInfo(fixedColumnCount);
        if (columnsInfo) {
            var cols = columnsInfo.dataColumns;
            this._forceRefreshRearRows = false;
            for (var i = 0; i < cols.length; i++) {
                var col = cols[i];
                col._realWidth = parseInt(col._realWidth || col._width) || 80;
                if (col instanceof dorado.widget.grid.RowNumColumn) {
                    this._forceRefreshRearRows = true;
                }
            }
        }
        var itemModel = this._itemModel;
        itemModel._summaryColumns = itemModel.extractSummaryColumns(columnsInfo.dataColumns);
        if (this._currentCell) {
            $fly(this._currentCell).removeClass("current-cell");
        }
        var menuColumn = this._headerMenuOpenColumn || this._headerHoverColumn;
        if (menuColumn) {
            this.hideHeaderOptionButton(menuColumn);
        }
        this._headerMenuOpenColumn = this._headerHoverColumn = null;
        var divScroll = this._divScroll, fixedInnerGrid = this._fixedInnerGrid, fixedInnerGridWrapper = this._fixedInnerGridWrapper;
        var innerGrid = getInnerGrid.call(this), innerGridWrapper = this._innerGridWrapper;
        var xScroll = this.xScroll = !!(this._width || this._realWidth);
        var yScroll = this.yScroll = !!(this._height || this._realHeight);
        var domMode;
        if (fixedColumnCount > 0) {
            domMode = xScroll ? 2 : 0;
        } else {
            domMode = yScroll ? 1 : 0;
        }
        var oldHeight;
        if (!this.xScroll || !this.yScroll) {
            oldHeight = dom.offsetHeight;
        }
        if (this._domMode != domMode) {
            this._domMode = domMode;
            switch (domMode) {
              case 0:
                with (dom.style) {
                    overflowX = overflowY = xScroll ? "auto" : "visible";
                }
                if (divScroll) {
                    $fly(divScroll).hide();
                }
                if (fixedInnerGridWrapper) {
                    $fly(fixedInnerGridWrapper).hide();
                }
                with (this._innerGridDom.style) {
                    position = top = left = width = height = "";
                }
                innerGrid.render(dom);
                break;
              case 1:
                with (dom.style) {
                    overflowX = overflowY = xScroll ? "hidden" : "visible";
                }
                divScroll = getDivScroll.call(this);
                $fly(divScroll).show();
                if (fixedInnerGridWrapper) {
                    $fly(fixedInnerGridWrapper).hide();
                }
                var innerGridWrapper = getInnerGridWrapper.call(this);
                with (this._innerGridDom.style) {
                    position = top = left = width = "";
                }
                innerGrid.render(innerGridWrapper);
                break;
              case 2:
                with (dom.style) {
                    overflowX = "hidden";
                    overflowY = yScroll ? "hidden" : "visible";
                }
                divScroll = getDivScroll.call(this);
                $fly(divScroll).show();
                fixedInnerGridWrapper = getFixedInnerGridWrapper.call(this);
                $fly(fixedInnerGridWrapper).show();
                fixedInnerGrid = getFixedInnerGrid.call(this);
                fixedInnerGrid.render(fixedInnerGridWrapper);
                innerGridWrapper = getInnerGridWrapper.call(this);
                with (this._innerGridDom.style) {
                    position = top = left = width = "";
                }
                innerGrid.render(innerGridWrapper);
                break;
            }
        }
        if (this._currentScrollMode != this._scrollMode && this._scrollMode != "viewport") {
            itemModel.setScrollPos(0);
        }
        if (!this._height) {
            this._scrollMode = "simple";
        }
        this._currentScrollMode = this._scrollMode;
        if (domMode == 2) {
            with (fixedInnerGridWrapper.style) {
                overflowX = width = "";
                height = divScroll.clientHeight + "px";
            }
            fixedInnerGrid._scrollMode = this._scrollMode;
            fixedInnerGrid._rowHeight = this._rowHeight;
            fixedInnerGrid._highlightCurrentRow = this._highlightCurrentRow;
            fixedInnerGrid._selectionMode = this._selectionMode;
            fixedInnerGrid._columnsInfo = columnsInfo.fixed;
            fixedInnerGrid._forceRefreshRearRows = this._forceRefreshRearRows;
            fixedInnerGrid._ignoreItemTimestamp = ignoreItemTimestamp;
            fixedInnerGrid.refreshDom(innerGrid.getDom());
            var scrollLeft = ((dorado.Browser.msie && dorado.Browser.version < "7") ? fixedInnerGridWrapper.firstChild : fixedInnerGridWrapper).offsetWidth;
            if (scrollLeft >= divScroll.clientWidth) {
                with (fixedInnerGridWrapper.style) {
                    overflowX = "hidden";
                    width = divScroll.clientWidth + "px";
                }
                innerGridWrapper.style.width = 0;
            } else {
                with (innerGridWrapper.style) {
                    width = (divScroll.clientWidth - scrollLeft) + "px";
                    height = divScroll.clientHeight + "px";
                }
                innerGridWrapper.style.left = scrollLeft + "px";
            }
        } else {
            if (innerGridWrapper) {
                with (innerGridWrapper.style) {
                    left = 0;
                    width = divScroll.clientWidth + "px";
                    height = divScroll.clientHeight + "px";
                }
            }
        }
        if (domMode != 2) {
            this.stretchColumnsToFit();
            if (innerGrid._itemModel instanceof PassiveItemModel) {
                innerGrid._itemModel = itemModel;
            }
        } else {
            if (!(innerGrid._itemModel instanceof PassiveItemModel)) {
                innerGrid._itemModel = new PassiveItemModel(itemModel);
            }
        }
        innerGrid._scrollMode = this._scrollMode;
        innerGrid._rowHeight = this._rowHeight;
        innerGrid._highlightCurrentRow = this._highlightCurrentRow;
        innerGrid._columnsInfo = columnsInfo.main;
        innerGrid._forceRefreshRearRows = this._forceRefreshRearRows;
        innerGrid._ignoreItemTimestamp = ignoreItemTimestamp;
        innerGrid.refreshDom(innerGrid.getDom());
        this.fixSizeBugs();
        if (!this._groupProperty && itemModel.footerEntity && itemModel.footerEntity.get("$expired")) {
            this.refreshSummary();
        }
        if ((!this.xScroll || !this.yScroll) && oldHeight != dom.offsetHeight) {
            this.notifySizeChange();
        }
    }, stretchColumnsToFit:function () {
        var WIDTH_ADJUST = 6;
        var stretchColumnsMode = this._realStretchColumnsMode;
        if (stretchColumnsMode == "off") {
            return;
        }
        var columns = this._columnsInfo.dataColumns;
        if (!columns.length) {
            return;
        }
        var clientWidth = (this._domMode == 0) ? this._dom.clientWidth : this._divScroll.clientWidth;
        if (!clientWidth) {
            return;
        }
        var totalWidth = 0, column;
        switch (stretchColumnsMode) {
          case "stretchableColumns":
            var stretchableColumns = [];
            for (var i = 0; i < columns.length; i++) {
                column = columns[i];
                if (column._width == "*") {
                    stretchableColumns.push(column);
                } else {
                    totalWidth += (columns[i]._realWidth || 80) + WIDTH_ADJUST;
                }
            }
            for (var i = 0; i < stretchableColumns.length; i++) {
                column = stretchableColumns[i];
                var w = Math.round((clientWidth - totalWidth) / (stretchableColumns.length - i)) - WIDTH_ADJUST;
                if (w < MIN_COL_WIDTH) {
                    w = MIN_COL_WIDTH;
                }
                column._realWidth = w;
                totalWidth += (w + WIDTH_ADJUST);
            }
            break;
          case "lastColumn":
            for (var i = 0; i < columns.length; i++) {
                column = columns[i];
                if (i == columns.length - 1) {
                    column._realWidth = clientWidth - totalWidth - WIDTH_ADJUST;
                    if (column._realWidth < MIN_COL_WIDTH) {
                        column._realWidth = MIN_COL_WIDTH;
                    }
                } else {
                    totalWidth += (column._realWidth + WIDTH_ADJUST);
                }
            }
            break;
          case "allColumns":
            var totalWeight = 0;
            for (var i = 0; i < columns.length; i++) {
                totalWeight += (columns[i]._realWidth || 80) + WIDTH_ADJUST;
            }
            var assignedWidth = 0;
            for (var i = 0; i < columns.length; i++) {
                var column = columns[i], weight = (parseInt(column._realWidth) || 80) + WIDTH_ADJUST;
                if (i == columns.length - 1) {
                    column._realWidth = clientWidth - assignedWidth - WIDTH_ADJUST;
                } else {
                    var w = Math.round(clientWidth * weight / totalWeight) - WIDTH_ADJUST;
                    if (w < MIN_COL_WIDTH) {
                        w = MIN_COL_WIDTH;
                    }
                    column._realWidth = w;
                    assignedWidth += (w + WIDTH_ADJUST);
                }
            }
            break;
        }
    }, syncroRowHeights:function (scrollInfo) {
        if (this._domMode == 2) {
            this._fixedInnerGrid.syncroRowHeights(scrollInfo);
        }
    }, fixSizeBugs:function () {
        var dom = this.getDom(), domMode = this._domMode;
        var xScroll = this.xScroll = !!(this._width || this._realWidth);
        var yScroll = this.yScroll = !!(this._height || this._realHeight);
        if (!xScroll) {
            $fly(dom).width(dom.firstChild.offsetWidth);
        } else {
            if (domMode != 0) {
                var divScroll = this._divScroll;
                var fixedInnerGrid = this._fixedInnerGrid;
                var fixedInnerGridWrapper = this._fixedInnerGridWrapper;
                var innerGrid = this._innerGrid;
                var innerGridWrapper = this._innerGridWrapper;
                var useOffsetWidth = !this._hScrollBarFixed && (dorado.Browser.safari || dorado.Browser.chrome);
                var ofx = (divScroll.scrollWidth > (useOffsetWidth ? divScroll.offsetWidth : divScroll.clientWidth)) ? "scroll" : "hidden";
                this._hScrollBarFixed = true;
                if (divScroll.style.overflowX != ofx) {
                    divScroll.style.overflowX = ofx;
                    if (ofx == "scroll") {
                        if (fixedInnerGridWrapper) {
                            fixedInnerGridWrapper.style.height = divScroll.clientHeight + "px";
                            fixedInnerGrid.updateContainerHeight(fixedInnerGrid._container);
                        }
                        innerGridWrapper.style.height = divScroll.clientHeight + "px";
                        innerGrid.updateContainerHeight(innerGrid._container);
                    } else {
                        this._ignoreItemTimestamp = true;
                        this.refreshDom(dom);
                        return;
                    }
                }
            }
        }
        if (domMode == 0 && dorado.Browser.msie && !yScroll) {
            $DomUtils.fixMsieXScrollBar(dom);
        }
    }, updateScroller:function (info) {
        if (this._divScroll) {
            var divScroll = this._divScroll, divViewPort = this._divViewPort;
            var ratio = info.clientHeight ? divScroll.clientHeight / info.clientHeight : 0;
            divViewPort.style.height = Math.round(info.scrollHeight * ratio) + "px";
            divScroll.scrollTop = this._scrollTop = Math.round(info.scrollTop * ratio);
            if (this._innerGridWrapper) {
                var innerGridWrapper = this._innerGridWrapper;
                if (innerGridWrapper.offsetLeft <= divScroll.clientWidth) {
                    var ratio = divScroll.clientWidth / (innerGridWrapper.clientWidth || 1);
                    var viewPortWidth = Math.round(innerGridWrapper.scrollWidth * ratio);
                    divViewPort.style.width = viewPortWidth + "px";
                    divScroll.scrollLeft = this._scrollLeft = Math.round(innerGridWrapper.scrollLeft * ratio);
                } else {
                    divViewPort.style.width = divScroll.scrollLeft = 0;
                }
            }
        }
    }, onResize:function () {
        if (this._domMode != 0) {
            this.refresh(true);
        }
    }, _watchScroll:function () {
        delete this._watchScrollTimerId;
        if (this._scrollMode == "simple") {
            return;
        }
        var divScroll = this._divScroll;
        if (divScroll.scrollLeft == 0 && divScroll.scrollTop == 0 && divScroll.offsetWidth > 0) {
            divScroll.scrollLeft = this._scrollLeft || 0;
            divScroll.scrollTop = this._scrollTop || 0;
            var innerGrid = this._innerGrid;
            var container = innerGrid._container;
            container.scrollTop = innerGrid._scrollTop;
            container.scrollLeft = innerGrid._scrollLeft;
            if (this._domMode == 2) {
                innerGrid = this._fixedInnerGrid;
                container = innerGrid._container;
                container.scrollTop = innerGrid._scrollTop;
                container.scrollLeft = innerGrid._scrollLeft;
            }
        }
        if (this._scrollTop) {
            this._watchScrollTimerId = $setTimeout(this, this._watchScroll, 300);
        }
    }, onScroll:function () {
        if (this._currentCellEditor) {
            this._currentCellEditor.resize();
        }
        if (this._currentCell) {
            $fly(this._currentCell).removeClass("current-cell");
        }
        var divScroll = this._divScroll;
        if ((this._scrollLeft || 0) != divScroll.scrollLeft) {
            if (this.onXScroll) {
                this.onXScroll();
            }
        }
        if ((this._scrollTop || 0) != divScroll.scrollTop) {
            if (this.onYScroll) {
                this.onYScroll();
            }
        }
        if (this._watchScrollTimerId) {
            clearTimeout(this._watchScrollTimerId);
            delete this._watchScrollTimerId;
        }
        if (divScroll.scrollTop && this._scrollMode != "simple") {
            this._watchScrollTimerId = $setTimeout(this, this._watchScroll, 300);
        }
        this._scrollLeft = divScroll.scrollLeft;
        this._scrollTop = divScroll.scrollTop;
    }, onXScroll:function () {
        if (this._innerGridWrapper) {
            var divScroll = this._divScroll;
            var innerGridWrapper = this._innerGridWrapper;
            var ratio = divScroll.clientWidth / (innerGridWrapper.clientWidth || 1);
            innerGridWrapper.scrollLeft = Math.round(divScroll.scrollLeft / ratio);
        }
    }, onYScroll:function () {
        var ratio = this._divScroll.scrollTop / this._divScroll.scrollHeight, innerContainer = this._innerGrid._container;
        if (this._scrollMode == "lazyRender") {
            innerContainer.scrollTop = Math.round(innerContainer.scrollHeight * ratio);
        } else {
            this._innerGrid.setYScrollPos(ratio);
        }
        if (this._domMode == 2) {
            this._fixedInnerGrid._container.scrollTop = innerContainer.scrollTop;
        }
        if (this._scrollMode == "lazyRender") {
            if (this._domMode == 2) {
                this._fixedInnerGrid.doOnYScroll();
            }
            var innerGrid = this._innerGrid;
            innerGrid.doOnYScroll();
            if (this._rowHeightInfos) {
                this.syncroRowHeights(innerGrid._container);
            }
            this.updateScroller(innerGrid._container);
        } else {
            if (this._scrollMode == "viewport") {
                dorado.Toolkits.setDelayedAction(this, "$scrollTimerId", function () {
                    if (this._domMode == 2) {
                        this._fixedInnerGrid.doOnYScroll();
                    }
                    this._innerGrid.doOnYScroll();
                }, 300);
            }
        }
    }, doOnKeyDown:function (evt) {
        var retValue = true;
        switch (evt.keyCode) {
          case 37:
            if (evt.ctrlKey && this._currentColumn) {
                var columns = this._columnsInfo.dataColumns;
                var i = columns.indexOf(this._currentColumn);
                if (i > 0) {
                    this.setCurrentColumn(columns[i - 1]);
                }
            }
            break;
          case 39:
            if (evt.ctrlKey && this._currentColumn) {
                var columns = this._columnsInfo.dataColumns;
                var i = columns.indexOf(this._currentColumn);
                if (i >= 0 && i < columns.length - 1) {
                    this.setCurrentColumn(columns[i + 1]);
                }
            }
            break;
          case 27:
            if (this._currentCellEditor) {
                this._editing = false;
                this._currentCellEditor.hide(false);
                dorado.widget.onControlGainedFocus(this);
            }
            break;
          default:
            retValue = this._doOnKeyDown(evt);
            break;
        }
        if (this._editing && !this._currentCellEditor) {
            this.showColumnEditor(this._currentColumn);
        }
        return retValue;
    }, doInnerGridSetCurrentRow:function (innerGrid, itemId) {
        if (this._processingCurrentRow) {
            return;
        }
        if (this._currentCellEditor) {
            this._currentCellEditor.hide();
        }
        this._processingCurrentRow = true, ig = this._innerGrid;
        if (this._domMode == 2) {
            (innerGrid == ig ? this._fixedInnerGrid : ig).setCurrentRowByItemId(itemId);
        }
        if (this._divScroll) {
            var st = Math.round(ig._container.scrollTop / ig._container.scrollHeight * this._divScroll.scrollHeight);
            if (this._scrollMode != "lazyRender") {
                this._scrollTop = st;
            }
            this._divScroll.scrollTop = st;
        }
        this._processingCurrentRow = false;
    }, onMouseUp:function (evt) {
        var tbody1 = this._innerGrid._dataTBody, tbody2 = (this._domMode == 2) ? this._fixedInnerGrid._dataTBody : null;
        var self = this, innerGrid;
        var row = $DomUtils.findParent(evt.target, function (parentNode) {
            var p = parentNode.parentNode;
            if (p == tbody1) {
                innerGrid = self._innerGrid;
                return true;
            } else {
                if (tbody2 && p == tbody2) {
                    innerGrid = self._fixedInnerGrid;
                    return true;
                }
            }
        });
        if (row) {
            this._editing = true;
            var column = null;
            if (innerGrid.getCurrentItemDom() == row) {
                var cell = $DomUtils.findParent(evt.target, function (parentNode) {
                    return parentNode.parentNode == row;
                }, true);
                column = this._columnsInfo.idMap[cell.colId];
                if (this._currentColumn == column) {
                    this.showColumnEditor(column);
                } else {
                    this.setCurrentColumn(column);
                }
            }
        } else {
            this._editing = false;
            this.setCurrentColumn(null);
        }
    }, _getCellByEvent:function (event) {
        var tbody1 = this._innerGrid._dataTBody, tbody2 = (this._domMode == 2) ? this._fixedInnerGrid._dataTBody : null;
        return $DomUtils.findParent(evt.target, function (parentNode) {
            var p = parentNode.parentNode;
            if (!p) {
                return;
            }
            p = p.parentNode;
            return (p == tbody1 || tbody2 && p == tbody2);
        });
    }, getEntityByEvent:function (event) {
        var cell = this._getCellByEvent(event);
        return (cell) ? $fly(cell.parentNode).data("item") : null;
    }, getColumnByEvent:function (event) {
        var cell = this._getCellByEvent(event);
        return (cell) ? this._columnsInfo.idMap[cell.colId] : null;
    }, doOnFocus:function () {
        if (this._currentColumn) {
            this.showColumnEditor(this._currentColumn);
        }
    }, doOnBlur:function () {
        if (this._currentCell) {
            $fly(this._currentCell).removeClass("current-cell");
        }
        if (this._currentCellEditor) {
            this._currentCellEditor.hide();
        }
    }, shouldEditing:function (column) {
        return this._editing && this._focused && column && !column.get("readOnly") && !this.get("readOnly") && column._property && column._property != this._groupProperty;
    }, setCurrentColumn:function (column) {
        if (this._currentColumn != column) {
            if (this._currentCell) {
                $fly(this._currentCell).removeClass("current-cell");
            }
            if (this._currentCellEditor) {
                this._currentCellEditor.hide();
            }
            this._currentColumn = column;
            if (column) {
                this.showColumnEditor(column);
            }
        }
    }, showColumnEditor:function (column) {
        if (this.shouldEditing(column)) {
            if (this._domMode == 2) {
                this._fixedInnerGrid.showColumnEditor(column);
            }
            this._innerGrid.showColumnEditor(column);
        }
    }, getCellEditor:function (column, entity) {
        var cellEditorCache = this._cellEditorCache;
        if (!cellEditorCache) {
            this._cellEditorCache = cellEditorCache = {};
        }
        var cellEditor = cellEditorCache[column._id] || column._editor;
        var eventArg = {data:entity, column:column, cellEditor:cellEditor};
        if (column.fireEvent("onGetCellEditor", column, eventArg) && this.fireEvent("onGetCellEditor", this, eventArg)) {
            cellEditor = eventArg.cellEditor;
            if (cellEditor === undefined) {
                var dt = column.get("dataType"), dtCode = dt ? dt._code : -1, trigger = column.get("trigger");
                var pd = column._propertyDef;
                if (trigger || (dtCode != dorado.DataType.PRIMITIVE_BOOLEAN && dtCode != dorado.DataType.BOOLEAN) || (pd && pd._mapping)) {
                    cellEditor = new dorado.widget.grid.DefaultCellEditor();
                    cellEditor.bindColumn(column);
                }
            } else {
                if (cellEditor) {
                    if (cellEditor.column) {
                        if (cellEditor.column != column) {
                            throw new ResourceException("dorado.grid.CellEditorShareError");
                        }
                    } else {
                        cellEditor.bindColumn(column);
                    }
                }
            }
            if (cellEditor && cellEditor.cachable) {
                cellEditorCache[column._id] = cellEditor;
            }
        }
        if (cellEditor) {
            cellEditor.data = entity;
        }
        return cellEditor;
    }, sort:function (column, desc) {
        var sortParams = column ? [{property:column.get("property"), desc:desc}] : [];
        var eventArg = {sortParams:sortParams, comparator:null};
        if (this.fireEvent("onGetEntityComparator", column, eventArg)) {
            comparator = eventArg.comparator;
        }
        this._itemModel.sort(sortParams, eventArg.comparator);
    }, filter:function (filterParams) {
        if (filterParams === undefined) {
            var filterEntity = this._itemModel.filterEntity, filterParams = [];
            var dataColumns = this._columnsInfo.dataColumns;
            for (var i = 0; i < dataColumns.length; i++) {
                var column = dataColumns[i];
                if (!column._property) {
                    continue;
                }
                var text = filterEntity.get(column._property);
                if (text == null) {
                    continue;
                }
                var v = dorado.Toolkits.parseFilterValue(text), operator = v[0], value = v[1];
                var dataType = column.get("dataType");
                if (!operator && (!dataType || dataType._code == dorado.DataType.STRING)) {
                    operator = "like";
                }
                if (dataType) {
                    value = dataType.parse(value, column.get("typeFormat"));
                }
                filterParams.push({property:column._property, operator:operator, value:value});
            }
        }
        this._itemModel.filter(filterParams);
        this.refresh();
    }, highlightItem:function (entity, options, speed) {
        function highlight(row) {
            if (!row) {
                return;
            }
            $fly(row).addClass("highlighting-row").effect("highlight", options || {color:"#FFFF80"}, speed || 1500, function () {
                $fly(row).removeClass("highlighting-row");
            });
        }
        if (!entity) {
            return;
        }
        var itemId = this._itemModel.getItemId(entity), innerGrid, row1, row2;
        if (this._domMode == 2) {
            innerGrid = this._fixedInnerGrid;
            row1 = innerGrid._itemDomMap[itemId];
        }
        innerGrid = this._innerGrid;
        row2 = innerGrid._itemDomMap[itemId];
        if (row2) {
            highlight(row1);
            highlight(row2);
        } else {
            if (!entity._disableDelayHighlight) {
                var self = this;
                setTimeout(function () {
                    entity._disableDelayHighlight = true;
                    self.highlightItem(entity, options, speed);
                    entity._disableDelayHighlight = false;
                }, 100);
            }
        }
    }, setHoverHeaderColumn:function (column) {
        if (this._headerHoverColumn == column) {
            return;
        }
        var oldColumn = this._headerHoverColumn;
        if (oldColumn) {
            $fly(oldColumn.headerCell).removeClass("hover-header");
            if (this._headerMenuOpenColumn != oldColumn) {
                this.hideHeaderOptionButton(oldColumn);
            }
        }
        this._headerHoverColumn = column;
        if (column) {
            hideColumnResizeHandler();
            var $cell = $fly(column.headerCell);
            $cell.addClass("hover-header");
            if (!$cell.data("draggable")) {
                var grid = this;
                var options = dorado.Object.apply({appendTo:"body", helper:function (evt) {
                    return getColumnDragHelper(evt, column.headerCell);
                }, draggingInfo:function () {
                    return new dorado.DraggingInfo({object:column, sourceControl:grid, options:options, tags:["grid-column"]});
                }, start:function () {
                    grid.hideHeaderOptionButton(column);
                }}, this.defaultDraggableOptions);
                $cell.draggable(options);
            }
            this.showHeaderOptionButton(column);
        }
    }, showHeaderOptionButton:function (column) {
        if (!column || !column._supportsOptionMenu) {
            return;
        }
        var cell = column.headerCell, $cell = jQuery(cell);
        $cell.addClass("menu-open-header");
        var button = this.getHeaderOptionButton(column);
        button.style.display = "";
        var offset = $cell.offset(), offsetParent = $fly(cell.offsetParent).offset();
        var l = offset.left - offsetParent.left + cell.offsetWidth - button.offsetWidth, t = offset.top - offsetParent.top + 1;
        $fly(button).css({left:l, top:t}).outerHeight(cell.offsetHeight - 2);
    }, hideHeaderOptionButton:function (column) {
        $fly(column.headerCell).removeClass("menu-open-header");
        var button = this.getHeaderOptionButton(column);
        if (button) {
            button.style.display = "none";
        }
    }, getHeaderOptionButton:function (column) {
        var cell = column.headerCell, button = cell.lastChild;
        if ((!button || button.className != "d-grid-header-option-button") && cell) {
            button = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-header-option-button", style:{display:"none", position:"absolute"}});
            $DomUtils.disableUserSelection(button);
            var self = this;
            $fly(button).mousedown(function (evt) {
                return false;
            }).click(function () {
                var menu = self.getHeaderOptionMenu(true);
                if (menu.get("visible")) {
                    menu.hide();
                } else {
                    var column = $fly(button).data("gridColumn");
                    self.initHeaderOptionMenu(menu, column);
                    menu._gridColumn = column;
                    menu.addListener("onHide", function () {
                        var col = self._headerMenuOpenColumn;
                        if (col && col != self._headerHoverColumn) {
                            self.hideHeaderOptionButton(col);
                            self._headerMenuOpenColumn = null;
                        }
                    }, {once:true, delay:0});
                    menu.show({anchorTarget:button, align:"innerright", vAlign:"bottom"});
                    self._headerMenuOpenColumn = column;
                }
                return false;
            });
        }
        if (cell && button.parentNode != cell) {
            cell.appendChild(button);
        }
        $fly(button).data("gridColumn", column);
        return button;
    }, getHeaderOptionMenu:function (create) {
        var menu = this._headerOptionMenu, grid = this;
        if (!menu && create) {
            this._headerOptionMenu = menu = new dorado.widget.Menu({items:[{name:"sortAsc", caption:$resource("dorado.grid.OptionMenuSortAscending"), icon:"url(>skin>common/icons.gif) -280px -181px", onClick:function (self) {
                if (menu._gridColumn instanceof dorado.widget.grid.DataColumn) {
                    grid.sort(menu._gridColumn);
                }
            }}, {name:"sortDesc", caption:$resource("dorado.grid.OptionMenuSortDescending"), icon:"url(>skin>common/icons.gif) -300px -181px", onClick:function (self) {
                if (menu._gridColumn instanceof dorado.widget.grid.DataColumn) {
                    grid.sort(menu._gridColumn, true);
                }
            }}, new dorado.widget.menu.Separator({name:"sortSeprator"}), {name:"fix", caption:$resource("dorado.grid.OptionMenuFix"), icon:"url(>skin>common/icons.gif) -60px -141px", onClick:function (self) {
                grid.set("fixedColumnCount", menu._columnIndex + 1);
            }}, {name:"unfix", caption:$resource("dorado.grid.OptionMenuUnfix"), onClick:function (self) {
                grid.set("fixedColumnCount", 0);
            }}, new dorado.widget.menu.Separator({name:"fixSeprator"}), {name:"group", caption:$resource("dorado.grid.OptionMenuGroup"), icon:"url(>skin>common/icons.gif) -80px -181px", onClick:function (self) {
                var column = menu._gridColumn, grid = column._grid;
                grid.set("groupProperty", column.get("property"));
                grid.refresh();
            }}, {name:"ungroup", caption:$resource("dorado.grid.OptionMenuUngroup"), onClick:function (self) {
                var column = menu._gridColumn, grid = column._grid;
                grid.set("groupProperty", null);
                grid.refresh();
            }}, new dorado.widget.menu.Separator({name:"groupSeprator"}), {$type:"Checkable", name:"toggleFilterBar", caption:$resource("dorado.grid.OptionMenuToggleFilterBar"), checked:!!grid.get("showFilterBar"), onClick:function (self) {
                grid.set("showFilterBar", !grid.get("showFilterBar"));
            }}, new dorado.widget.menu.Separator({name:"filterSeprator"}), {name:"groupColumn", caption:$resource("dorado.grid.OptionMenuGroupColumn"), onClick:function (self) {
                dorado.MessageBox.prompt($resource("dorado.grid.InputNewGroupName"), function (text) {
                    var column = menu._gridColumn, parentColumn = column._parent, grid = column._grid;
                    var i = parentColumn.get("columns").remove(column);
                    if (i >= 0) {
                        parentColumn.addColumn(new dorado.widget.grid.ColumnGroup({caption:text, columns:[column]}), i);
                        grid.refresh();
                    }
                });
            }}, {name:"ungroupColumns", caption:$resource("dorado.grid.OptionMenuUngroupColumns"), onClick:function (self) {
                var column = menu._gridColumn, parentColumn = column._parent, grid = column._grid;
                var i = parentColumn.get("columns").remove(column);
                if (i >= 0) {
                    column.get("columns").each(function (subColumn) {
                        parentColumn.addColumn(subColumn, i);
                        i++;
                    });
                    grid.refresh();
                }
            }}, new dorado.widget.menu.Separator({name:"groupColumnSeprator"}), {name:"columns", caption:$resource("dorado.grid.OptionMenuColumns"), icon:"url(>skin>common/icons.gif) -120px -181px", items:[]}]});
            this.registerInnerControl(menu);
        }
        return menu;
    }, initHeaderOptionMenu:function (menu, column) {
        function crreateColumnItems(columnsItem, columns) {
            columns.each(function (column) {
                var item = new dorado.widget.menu.CheckableMenuItem({$type:"Checkable", caption:column.get("caption") || column.get("name"), checked:column.get("visible"), hideOnClick:false, onCheckedChange:function (self) {
                    var col = self._column;
                    col.set("visible", !col.get("visible"));
                    col._grid.refresh();
                }});
                item._column = column;
                if (column instanceof dorado.widget.grid.ColumnGroup) {
                    crreateColumnItems(item, column.get("columns"));
                }
                columnsItem.insertItem(item);
            });
        }
        var isDataColumn = column instanceof dorado.widget.grid.DataColumn;
        var sortState = isDataColumn ? column.get("sortState") : null;
        menu.findItem("sortAsc").set("disabled", !isDataColumn);
        menu.findItem("sortDesc").set("disabled", !isDataColumn);
        var columns = this.get("columns");
        var isTopColumn = (column.get("parent") == this), fixed = false;
        if (isTopColumn) {
            menu._columnIndex = columns.indexOf(column);
            fixed = (this._fixedColumnCount > 0 && (menu._columnIndex + 1) == this._fixedColumnCount);
        }
        menu.findItem("fix").set("disabled", !isTopColumn || fixed || this._groupProperty);
        menu.findItem("unfix").set("disabled", this._fixedColumnCount == 0 || this._groupProperty);
        menu.findItem("toggleFilterBar").set("checked", this._showFilterBar);
        menu.findItem("ungroupColumns").set("disabled", isDataColumn);
        menu.findItem("group").set("disabled", !isDataColumn);
        menu.findItem("ungroup").set("disabled", !this._groupProperty);
        var columnsItem = menu.findItem("columns");
        columnsItem.clearItems();
        crreateColumnItems(columnsItem, columns);
    }, selectAll:function () {
        if (this._selectionMode != "multiRows") {
            return;
        }
        this._innerGrid.replaceSelection(this.get("selection"), this._itemModel.getAllDataEntities());
    }, unselectAll:function () {
        this._innerGrid.replaceSelection(this.get("selection"), null);
    }, selectInvert:function () {
        if (this._selectionMode != "multiRows") {
            return;
        }
        var selection = this.get("selection"), removed = [], added = [];
        jQuery.each(this._itemModel.getAllDataEntities(), function (i, item) {
            if (selection.indexOf(item) >= 0) {
                removed.push(item);
            } else {
                added.push(item);
            }
        });
        this._innerGrid.replaceSelection(removed, added);
    }, refreshSummary:function () {
        this._itemModel.footerEntity.set("$expired", true);
        dorado.Toolkits.setDelayedAction(this, "$refreshSummaryTimerId", function () {
            this._itemModel.refreshSummary();
        }, 300);
    }, onEntityChanged:function (entity, property) {
        var itemModel = this._itemModel;
        if (itemModel.groups) {
            var groupProperty = this._groupProperty;
            var groupValue = ((entity instanceof dorado.Entity) ? itemModel.entityMap[entity.entityId] : entity[groupProperty]) + "";
            if (property == groupProperty && entity instanceof dorado.Entity && entity.getText(groupProperty) != groupValue) {
                this.setDirtyMode(true);
            }
            var group = itemModel.groupMap[groupValue];
            if (group) {
                group.headerEntity.set("$expired", true);
                group.footerEntity.set("$expired", true);
            }
        }
    }, getFloatRefreshPanel:function () {
        var floatRefreshPanel = this._floatRefreshPanel;
        if (!floatRefreshPanel) {
            this._floatRefreshPanel = floatRefreshPanel = $DomUtils.xCreateElement({tagName:"DIV", className:"float-refresh-panel"});
            var self = this;
            $fly(floatRefreshPanel).mouseenter(function () {
                self.maximizeFloatRefreshPanel();
            }).mouseleave(function () {
                if (self._dirtyMode) {
                    dorado.Toolkits.setDelayedAction(self, "$refreshPanelTimerId", self.minimizeFloatRefreshPanel, 500);
                }
            });
            var button = new dorado.widget.SimpleButton({className:"button", onClick:function () {
                self._itemModel.refreshItems();
                self.refresh();
            }});
            this.registerInnerControl(button);
            button.render(floatRefreshPanel);
            this.getDom().appendChild(floatRefreshPanel);
            floatRefreshPanel._minTop = parseInt($fly(floatRefreshPanel).css("top"));
        }
        return floatRefreshPanel;
    }, setDirtyMode:function (dirtyMode) {
        if (!!this._dirtyMode == dirtyMode) {
            return;
        }
        this._dirtyMode = dirtyMode;
        var floatButton = this.getFloatRefreshPanel();
        floatButton.style.display = dirtyMode ? "" : "none";
        if (dirtyMode) {
            this.maximizeFloatRefreshPanel();
            dorado.Toolkits.setDelayedAction(this, "$refreshPanelTimerId", this.minimizeFloatRefreshPanel, 3000);
        } else {
            $fly(floatButton).top(floatButton._minTop);
        }
    }, maximizeFloatRefreshPanel:function () {
        if (!dorado.Toolkits.cancelDelayedAction(this, "$refreshPanelTimerId")) {
            var floatButton = this.getFloatRefreshPanel();
            $fly(floatButton).animate({"top":"+=" + Math.abs(floatButton._minTop)}, {duration:"slow", specialEasing:{top:"easeOutBack"}});
        }
    }, minimizeFloatRefreshPanel:function ($floatButton) {
        dorado.Toolkits.cancelDelayedAction(this, "$refreshPanelTimerId");
        var floatButton = this.getFloatRefreshPanel();
        $fly(floatButton).animate({"top":"-=" + Math.abs(floatButton._minTop)}, {duration:"slow", specialEasing:{top:"easeInOutBack"}});
    }, getFloatFilterPanel:function () {
        var floatFilterPanel = this._floatFilterPanel;
        if (!floatFilterPanel) {
            this._floatFilterPanel = floatFilterPanel = $DomUtils.xCreateElement({tagName:"DIV", className:"float-filter-panel"});
            var self = this;
            $fly(floatFilterPanel).mouseenter(function () {
                self.showFilterPanel();
            }).mouseleave(function () {
                dorado.Toolkits.setDelayedAction(self, "$filterPanelTimerId", self.hideFilterPanel, 500);
            });
            var button;
            button = new dorado.widget.SimpleIconButton({className:"filter-button", onClick:function () {
                self.filter();
            }});
            this.registerInnerControl(button);
            button.render(floatFilterPanel);
            button = new dorado.widget.SimpleIconButton({className:"reset-button", onClick:function () {
                self.get("filterEntity").clearData();
                self.filter();
            }});
            this.registerInnerControl(button);
            button.render(floatFilterPanel);
            button = new dorado.widget.SimpleIconButton({className:"hide-button", onClick:function () {
                dorado.Toolkits.cancelDelayedAction(self, "$filterPanelTimerId");
                self.getFloatFilterPanel().style.display = "none";
                self.set("showFilterBar", false);
            }});
            this.registerInnerControl(button);
            button.render(floatFilterPanel);
            this.getDom().appendChild(floatFilterPanel);
        }
        return floatFilterPanel;
    }, showFilterPanel:function () {
        if (!dorado.Toolkits.cancelDelayedAction(this, "$filterPanelTimerId")) {
            var panel = this.getFloatFilterPanel(), filterBar = this._innerGrid._filterBarRow;
            $fly(panel).hide().top(filterBar.offsetTop + filterBar.offsetHeight - 1).slideDown("fast");
        }
    }, hideFilterPanel:function () {
        dorado.Toolkits.cancelDelayedAction(this, "$filterPanelTimerId");
        var panel = this.getFloatFilterPanel(), filterBar = this._innerGrid._filterBarRow;
        $fly(panel).slideUp("slow");
    }, getDraggableOptions:function (dom) {
        var options = $invokeSuper.call(this, arguments);
        if (dom == this._dom) {
            options.handle = ":first-child";
        }
        return options;
    }, findItemDomByEvent:function (evt) {
        var target = evt.srcElement || evt.target;
        var target = target || evt;
        var innerTbody = this._innerGrid._dataTBody, fixedInnerTBody;
        if (this._domMode == 2) {
            fixedInnerTBody = this._fixedInnerGrid._dataTBody;
        }
        return $DomUtils.findParent(target, function (parentNode) {
            return parentNode.parentNode == innerTbody || (fixedInnerTBody && parentNode.parentNode == fixedInnerTBody);
        });
    }, getDraggingInsertIndicator:dorado.widget.RowList.prototype.getDraggingInsertIndicator, onDragStart:function () {
        $invokeSuper.call(this, arguments);
        if (this._currentCellEditor) {
            this._currentCellEditor.hide();
        }
    }, findItemDomByPosition:function (pos) {
        pos.y -= this._innerGrid._frameTBody.offsetTop - this._innerGrid._container.scrollTop;
        return this._innerGrid.findItemDomByPosition.call(this._innerGrid, pos);
    }, showDraggingInsertIndicator:function (draggingInfo, insertMode, itemDom) {
        var insertIndicator = dorado.widget.RowList.getDraggingInsertIndicator();
        if (insertMode) {
            var dom = this._dom;
            var width = dom.firstChild.offsetWidth;
            var top = this._innerGrid._frameTBody.offsetTop - this._innerGrid._container.scrollTop + ((insertMode == "before") ? itemDom.offsetTop : (itemDom.offsetTop + itemDom.offsetHeight));
            if (dom.firstChild.clientWidth < width) {
                width = dom.firstChild.clientWidth;
            }
            $fly(insertIndicator).width(width).height(2).left(0).top(top - 1).show();
            dom.appendChild(insertIndicator);
        } else {
            $fly(insertIndicator).hide();
        }
    }, setDraggingOverItemDom:function (itemDom) {
        this._innerGrid.setDraggingOverItemDom(itemDom);
        if (this._fixedInnerGrid) {
            if (itemDom) {
                itemDom = this._fixedInnerGrid._itemDomMap[itemDom.itemId];
            }
            this._fixedInnerGrid.setDraggingOverItemDom(itemDom);
        }
    }, onHeaderDragMove:function (draggingInfo, evt) {
        function findDropPosition(columns) {
            for (var i = 0; i < columns.length; i++) {
                var column = columns[i];
                var cell = column.headerCell;
                if (offsetParent != cell.offsetParent) {
                    offsetParent = cell.offsetParent;
                    parentOffset = $fly(offsetParent).offset();
                }
                var left = parentOffset.left + cell.offsetLeft;
                if (left <= evt.pageX && left + cell.offsetWidth >= evt.pageX) {
                    if (column instanceof dorado.widget.grid.ColumnGroup) {
                        var top = parentOffset.top + getCellOffsetTop(cell, this._headerRowHeight);
                        if (evt.pageY > top + cell.offsetHeight) {
                            return findDropPosition.call(this, column._columns.items);
                        }
                    }
                    return {before:evt.pageX < left + cell.offsetWidth / 2, column:column};
                }
            }
            return null;
        }
        var column = draggingInfo.get("object");
        if (column) {
            var offsetParent, parentOffset, dropPosition = findDropPosition.call(this, this._columns.items);
            if (dropPosition != null) {
                if (dropPosition.column == column) {
                    dropPosition = null;
                } else {
                    var oldColumns = column._parent._columns;
                    if (dropPosition.column == oldColumns.items[oldColumns.indexOf(column) + (dropPosition.before ? 1 : -1)]) {
                        dropPosition = null;
                    }
                }
            }
            showColumnDropIndicator(this, dropPosition);
            draggingInfo.dropPosition = dropPosition;
            draggingInfo.set("accept", dropPosition != null);
        }
    }, onDraggingSourceMove:function (draggingInfo, evt) {
        var pos = this.getMousePosition(evt);
        if (pos.y < this._innerGrid._frameTBody.offsetTop) {
            if (draggingInfo.isDropAcceptable(["grid-column"])) {
                this.showDraggingInsertIndicator();
                this.onHeaderDragMove(draggingInfo, evt);
            } else {
                draggingInfo.set("accept", false);
            }
        } else {
            hideColumnDropIndicator();
            return dorado.widget.RowList.prototype.onDraggingSourceMove.apply(this, arguments);
        }
    }, doOnDraggingSourceMove:dorado.widget.RowList.prototype.doOnDraggingSourceMove, onDraggingSourceOut:function (draggingInfo, evt) {
        hideColumnDropIndicator();
        return dorado.widget.RowList.prototype.onDraggingSourceOut.apply(this, arguments);
    }, onHeaderDragDrop:function (draggingInfo, evt) {
        var dropPosition = draggingInfo.dropPosition;
        if (dropPosition) {
            var ind = window._colDropIndicator;
            if (ind) {
                var column = draggingInfo.get("object");
                var refColumn = dropPosition.column;
                hideColumnDropIndicator();
                var oldColumns = column._parent._columns;
                var columns = refColumn._parent._columns;
                if (columns != oldColumns && oldColumns.size <= 1) {
                    setTimeout(function () {
                        throw new dorado.ResourceException("dorado.grid.RemoveTheOnlyColumn", grid._id);
                    }, 100);
                    return;
                }
                var oldGrid = column.get("grid");
                oldColumns.remove(column);
                columns.insert(column, columns.indexOf(refColumn) + (dropPosition.before ? 0 : 1));
                this._ignoreItemTimestamp = true;
                this.refresh();
                if (oldGrid != this) {
                    oldGrid._ignoreItemTimestamp = true;
                    oldGrid.refresh();
                }
            }
        } else {
            hideColumnDropIndicator();
        }
        return true;
    }, onDraggingSourceDrop:function (draggingInfo, evt) {
        var pos = this.getMousePosition(evt);
        if (pos.y < this._innerGrid._frameTBody.offsetTop) {
            return this.onHeaderDragDrop(draggingInfo, evt);
        } else {
            return dorado.widget.RowList.prototype.onDraggingSourceDrop.apply(this, arguments);
        }
    }, processItemDrop:dorado.widget.RowList.prototype.processItemDrop, initDraggingIndicator:function () {
    }, beforeCellValueEdit:function (entity, column, value) {
        var arg = {entity:entity, column:column, value:value, processDefault:true};
        this.fireEvent("beforeCellValueEdit", this, arg);
        return arg.processDefault;
    }, onCellValueEdit:function (entity, column) {
        this.fireEvent("onCellValueEdit", this, {entity:entity, column:column});
    }});
    dorado.widget.grid.AbstractInnerGrid = $extend(dorado.widget.RowList, {$className:"dorado.widget.grid.AbstractInnerGrid", focusable:false, ATTRIBUTES:{selection:{getter:function (p, v) {
        if (this.fixed) {
            return this.grid.get(p);
        } else {
            return this._selection;
        }
    }}}, constructor:function (grid, fixed) {
        this.grid = grid;
        this.fixed = fixed;
        $invokeSuper.call(this, []);
        this._itemModel = grid._itemModel;
        if (fixed) {
            this._className = "fixed-inner-grid";
            this._skipProcessBlankRows = true;
            this.setScrollingIndicator = dorado._NULL_FUNCTION;
        } else {
            this._className = "inner-grid";
        }
    }, createItemModel:dorado._NULL_FUNCTION, createDom:function () {
        this._container = $DomUtils.xCreateElement({tagName:"DIV", style:{overflow:"hidden", height:"100%"}});
        var tableFrame = $DomUtils.xCreateElement({tagName:"TABLE", className:"frame-table", cellSpacing:0, cellPadding:0, style:{position:"relative"}, content:["^THEAD", {tagName:"TR", content:{tagName:"TD", vAlign:"top", content:this._container}}, "^TFOOT"]});
        this._frameTHead = tableFrame.tHead;
        this._frameTBody = tableFrame.tBodies[0];
        this._frameTFoot = tableFrame.tFoot;
        return tableFrame;
    }, refreshDom:function (dom) {
        if (!this._columnsInfo) {
            return;
        }
        dorado.widget.Control.prototype.refreshDom.apply(this, arguments);
        this.refreshFrameHeader();
        this.refreshFrameFooter();
        this.updateContainerHeight(this._container);
        this.refreshFrameBody(this._container);
        this._scrollMode = this._scrollMode;
        var grid = this.grid;
        if (!this.fixed) {
            if (this._currentRow) {
                this.scrollItemDomIntoView(this._currentRow);
            }
            if (grid._rowHeightInfos) {
                with (grid._rowHeightInfos) {
                    var p = (dorado.Browser.mozilla || dorado.Browser.opera) ? "offsetHeight" : "clientHeight";
                    if (this._beginBlankRow) {
                        rows["beginBlankRow"] = (this._beginBlankRow.parentNode.style.display == "none") ? 0 : this._beginBlankRow.firstChild[p];
                    }
                    if (this._endBlankRow) {
                        rows["endBlankRow"] = (this._endBlankRow.parentNode.style.display == "none") ? 0 : this._endBlankRow.firstChild[p];
                    }
                    rowHeightAverage = this._rowHeightAverage;
                    startIndex = this.startIndex;
                }
            }
            if (grid._rowHeightInfos) {
                grid.syncroRowHeights(this._container);
            }
            grid.updateScroller(this._container);
        }
    }, refreshFrameHeader:function () {
        var grid = this.grid, tableFrameHeader = this._frameTHead;
        var $tableFrameHeader = jQuery(tableFrameHeader);
        if (grid._showHeader) {
            var headerTable = this._headerTable;
            var headerTBody = this._headerTBody;
            if (!headerTable) {
                headerTable = this._headerTable = $DomUtils.xCreateElement({tagName:"TABLE", className:"header-table", cellSpacing:0, cellPadding:0, content:"^TBODY"});
                tableFrameHeader.appendChild($DomUtils.xCreateElement({tagName:"TR", style:{height:"1px"}, content:{tagName:"TD", content:headerTable}}));
                headerTBody = this._headerTBody = headerTable.tBodies[0];
                var self = this;
                $fly(headerTBody).mousemove(function () {
                    return self.onHeaderMouseMove.apply(self, arguments);
                }).mouseleave(function () {
                    return self.onHeaderMouseLeave.apply(self, arguments);
                });
                var options = dorado.Object.apply({doradoDroppable:grid}, grid.defaultDroppableOptions);
                $fly(headerTable).droppable(options);
            }
            var structure = this._columnsInfo.structure;
            for (var i = 0; i < structure.length; i++) {
                var cellInfos = structure[i];
                var row = $DomUtils.getOrCreateChild(headerTBody, i, function () {
                    var row = document.createElement("TR");
                    $DomUtils.disableUserSelection(row);
                    row.style.height = grid._headerRowHeight + ((dorado.Browser.msie && cellInfos.length == 0) ? 1 : 0) + "px";
                    return row;
                });
                var self = this;
                for (var j = 0; j < cellInfos.length; j++) {
                    var cellInfo = cellInfos[j];
                    var col = cellInfo.column;
                    var cell = col.headerCell = $DomUtils.getOrCreateChild(row, j, function () {
                        var cell = self.createCell();
                        $fly(cell).click(function () {
                            var column = grid._columnsInfo.idMap[cell.colId];
                            if (column) {
                                if (grid.fireEvent("onHeaderClick", grid, {column:column})) {
                                    column.fireEvent("onHeaderClick", column);
                                }
                            }
                        });
                        return cell;
                    });
                    cell.className = "header";
                    cell.colSpan = cellInfo.colSpan;
                    cell.rowSpan = cellInfo.rowSpan || (structure.length - i);
                    var label = cell.firstChild;
                    if (col instanceof dorado.widget.grid.DataColumn) {
                        cell.align = "left";
                        if (col.get("sortState")) {
                            $fly(cell).addClass("sorted-header");
                        }
                        label.style.width = col._realWidth + "px";
                    } else {
                        cell.align = "center";
                        var w = 0;
                        col._columns.each(function (subCol) {
                            if (subCol._visible) {
                                w += (subCol._realWidth || 0);
                            }
                        });
                        if (w) {
                            label.style.width = w + "px";
                        }
                    }
                    var processDefault = true, arg = {dom:cell, column:col, processDefault:false};
                    if (grid.getListenerCount("onRenderHeaderCell")) {
                        grid.fireEvent("onRenderHeaderCell", this, arg);
                        processDefault = arg.processDefault;
                    }
                    if (processDefault) {
                        if (col.getListenerCount("onRenderHeaderCell")) {
                            arg.processDefault = false;
                            col.fireEvent("onRenderHeaderCell", col, arg);
                            processDefault = arg.processDefault;
                        }
                        if (processDefault) {
                            dorado.Renderer.render(col._headerRenderer || grid._headerRenderer || $singleton(dorado.widget.grid.DefaultCellHeaderRenderer), label, {grid:grid, innerGrid:this, column:col});
                        }
                    }
                    cell.colId = col._id;
                    if (grid._headerMenuOpenColumn == col) {
                        grid.showHeaderOptionButton(col);
                    }
                }
                $DomUtils.removeChildrenFrom(row, cellInfos.length);
            }
            $DomUtils.removeChildrenFrom(headerTBody, structure.length);
            var filterBarRow = this._filterBarRow, filterBarHeight = 0;
            var tFoot = headerTable.tFoot;
            if (grid._showFilterBar) {
                if (!filterBarRow) {
                    tFoot = document.createElement("TFOOT");
                    this._filterBarRow = filterBarRow = document.createElement("TR");
                    filterBarRow.className = "filter-bar";
                    $fly(filterBarRow).mouseenter(function () {
                        grid.showFilterPanel();
                    }).mouseleave(function () {
                        dorado.Toolkits.setDelayedAction(grid, "$filterPanelTimerId", grid.hideFilterPanel, 500);
                    });
                    tFoot.appendChild(filterBarRow);
                } else {
                    tFoot = filterBarRow.parentNode;
                }
                if (tFoot.parentNode != headerTable) {
                    headerTable.appendChild(tFoot);
                }
                this.refreshFilterBar();
                filterBarHeight = tFoot.offsetHeight;
            } else {
                if (tFoot) {
                    headerTable.removeChild(tFoot);
                }
            }
            if (!(dorado.Browser.mozilla || dorado.Browser.opera)) {
                headerTable.style.height = ((grid._headerRowHeight + (dorado.Browser.msie ? 2 : 1)) * structure.length + filterBarHeight + 1) + "px";
            }
            $tableFrameHeader.show();
        } else {
            $tableFrameHeader.hide();
        }
    }, refreshFilterBar:function () {
        var grid = this.grid, filterBarRow = this._filterBarRow, filterEntity = grid._itemModel.filterEntity;
        var renderer = $singleton(dorado.widget.grid.FilterBarCellRenderer);
        var dataColumns = this._columnsInfo.dataColumns;
        for (var i = 0; i < dataColumns.length; i++) {
            var column = dataColumns[i];
            var cell = $DomUtils.getOrCreateChild(filterBarRow, i, this.createCell), label = cell.firstChild;
            cell.className = "filter-bar-cell";
            label.style.width = column._realWidth + "px";
            dorado.Renderer.render(renderer, label, {grid:grid, innerGrid:this, data:filterEntity, column:column});
            cell.colId = column._id;
        }
        $DomUtils.removeChildrenFrom(filterBarRow, dataColumns.length);
    }, refreshFrameFooter:function () {
        var grid = this.grid, tableFrameFooter = this._frameTFoot;
        var $tableFrameFooter = jQuery(tableFrameFooter);
        if (grid._showFooter) {
            var footerTable = this._footerTable;
            var footerRow = this._footerRow;
            if (!footerTable) {
                footerTable = this._footerTable = $DomUtils.xCreateElement({tagName:"TABLE", className:"footer-table", cellSpacing:0, cellPadding:0, content:"^TR"});
                tableFrameFooter.appendChild($DomUtils.xCreateElement({tagName:"TR", style:{height:"1px"}, content:{tagName:"TD", content:footerTable}}));
                footerRow = this._footerRow = footerTable.tBodies[0].childNodes[0];
            }
            footerRow.style.height = grid._footerRowHeight + "px";
            var dataColumns = this._columnsInfo.dataColumns;
            for (var i = 0; i < dataColumns.length; i++) {
                var col = dataColumns[i];
                var cell = $DomUtils.getOrCreateChild(footerRow, i, this.createCell);
                cell.className = "footer";
                cell.align = col._align || "";
                var label = cell.firstChild;
                if (col instanceof dorado.widget.grid.DataColumn) {
                    label.style.width = col._realWidth + "px";
                }
                var processDefault = true, arg = {dom:label, column:col, processDefault:false};
                if (grid.getListenerCount("onRenderFooterCell")) {
                    grid.fireEvent("onRenderFooterCell", this, arg);
                    processDefault = arg.processDefault;
                }
                if (processDefault) {
                    if (col.getListenerCount("onRenderFooterCell")) {
                        arg.processDefault = false;
                        col.fireEvent("onRenderFooterCell", col, arg);
                        processDefault = arg.processDefault;
                    }
                    if (processDefault) {
                        dorado.Renderer.render(col._footerRenderer || grid._footerRenderer || $singleton(dorado.widget.grid.DefaultCellFooterRenderer), label, {grid:grid, innerGrid:this, column:col, data:grid._itemModel.footerEntity});
                    }
                }
                cell.colId = col._id;
            }
            $DomUtils.removeChildrenFrom(footerRow, dataColumns.length);
            $tableFrameFooter.show();
        } else {
            $tableFrameFooter.hide();
        }
    }, refreshFrameBody:function (container) {
        this._cols = this._columnsInfo.dataColumns.length;
        if (this._scrollMode == "viewport") {
            this.refreshViewPortContent(container);
        } else {
            this.refreshContent(container);
        }
        if (this._scrollMode && this._scrollMode != this._scrollMode && !this.getCurrentItemId()) {
            this.onYScroll();
        }
    }, updateContainerHeight:function (container) {
        if (this.grid._height || this.grid._realHeight) {
            var tableFrame = this.getDom();
            container.style.height = (tableFrame.parentNode.offsetHeight - (this._headerTable ? this._headerTable.offsetHeight : 0) - (this._footerTable ? this._footerTable.offsetHeight : 0)) + "px";
        }
    }, doRefreshItemDomData:function (row, entity) {
        var grid = this.grid, processDefault = true;
        if (grid.getListenerCount("onRenderRow")) {
            var arg = {dom:row, data:entity, rowType:entity.rowType, processDefault:true};
            grid.fireEvent("onRenderRow", grid, arg);
            processDefault = arg.processDefault;
        }
        if (processDefault) {
            var renderer;
            if (entity.rowType == "header") {
                renderer = grid._groupHeaderRenderer || $singleton(dorado.widget.grid.GroupHeaderRenderer);
            } else {
                if (entity.rowType == "footer") {
                    renderer = grid._groupFooterRenderer || $singleton(dorado.widget.grid.GroupFooterRenderer);
                } else {
                    renderer = grid._rowRenderer || $singleton(dorado.widget.grid.DefaultRowRenderer);
                }
            }
            dorado.Renderer.render(renderer, row, {grid:grid, innerGrid:this, data:entity});
        }
    }, createCell:function () {
        var label = document.createElement("DIV");
        label.className = "cell";
        label.style.overflow = "hidden";
        var cell = document.createElement("TD");
        cell.appendChild(label);
        return cell;
    }, createItemDom:function (item) {
        var grid = this.grid;
        var row = document.createElement("TR");
        row.className = "row";
        if (this._scrollMode == "lazyRender" && this._shouldSkipRender) {
            row._lazyRender = true;
            row.style.height = grid._rowHeight + "px";
        }
        return row;
    }, createItemDomDetail:function (row, item) {
        row.style.height = "";
    }, refreshItemDoms:function (tbody, reverse, fn) {
        var grid = this.grid;
        if (this.fixed) {
            grid._rowHeightInfos = {rows:{}, unmatched:[], unfound:{}};
        }
        if (grid._domMode == 2) {
            var rows;
            if (this.fixed) {
                rows = $invokeSuper.call(this, arguments);
            } else {
                var i = 0;
                var visibleRows = grid._rowHeightInfos ? grid._rowHeightInfos.visibleRows : Number.MAX_VALUE;
                rows = $invokeSuper.call(this, [tbody, reverse, (function (row) {
                    var b = fn ? fn(row) : true;
                    return b && ((++i) < visibleRows);
                })]);
            }
            if (grid._rowHeightInfos) {
                grid._rowHeightInfos.visibleRows = rows;
            }
            return rows;
        } else {
            return $invokeSuper.call(this, arguments);
        }
    }, setFocus:dorado._NULL_FUNCTION, onScroll:dorado._NULL_FUNCTION, doOnKeyDown:function () {
        return true;
    }, syncroRowHeights:function (scrollInfo) {
        with (this.grid._rowHeightInfos) {
            if (this.grid._dynaRowHeight) {
                for (var i = 0; i < unmatched.length; i++) {
                    var row = this._itemDomMap[unmatched[i]];
                    if (row) {
                        var h = rows[unmatched[i]];
                        if (dorado.Browser.msie && dorado.Browser.version >= "8") {
                            row.style.height = h + "px";
                            $fly(row).toggleClass("fix-row-bug");
                        } else {
                            if (dorado.Browser.chrome || dorado.Browser.safari) {
                                row.firstChild.style.height = h + "px";
                            } else {
                                row.style.height = h + "px";
                            }
                        }
                    }
                }
                unmatched = [];
                if (this._itemDomCount > visibleRows) {
                    for (var itemId in unfound) {
                        if (unfound.hasOwnProperty(itemId)) {
                            var row = this._itemDomMap[itemId];
                            if (row) {
                                this.removeItemDom(row);
                            }
                        }
                    }
                    unfound = {};
                }
            }
            if (this._beginBlankRow) {
                with (this._beginBlankRow) {
                    var beginBlankRow = rows["beginBlankRow"];
                    if (beginBlankRow) {
                        firstChild.colSpan = this._cols;
                        firstChild.style.height = beginBlankRow + "px";
                        parentNode.style.display = "";
                    } else {
                        parentNode.style.display = "none";
                    }
                }
            }
            if (this._endBlankRow) {
                with (this._endBlankRow) {
                    var endBlankRow = rows["endBlankRow"];
                    if (endBlankRow) {
                        firstChild.colSpan = this._cols;
                        firstChild.style.height = endBlankRow + "px";
                        parentNode.style.display = "";
                    } else {
                        parentNode.style.display = "none";
                    }
                }
            }
            this._itemDomCount = visibleRows;
            this._rowHeightAverage = rowHeightAverage;
            this.startIndex = startIndex;
            this._container.scrollTop = this._scrollTop = scrollInfo.scrollTop;
        }
    }, syncroRowHeight:function (itemId) {
        var row = this._itemDomMap[itemId];
        if (!row) {
            return;
        }
        var h = this.grid._rowHeightInfos.rows[itemId];
        if (dorado.Browser.msie && dorado.Browser.version >= "8") {
            row.style.height = h + "px";
            $fly(row).toggleClass("fix-row-bug");
        } else {
            if (dorado.Browser.chrome || dorado.Browser.safari) {
                row.firstChild.style.height = h + "px";
            } else {
                row.style.height = h + "px";
            }
        }
    }, setYScrollPos:function (ratio) {
        var container = this._container, scrollTop = Math.round(container.scrollHeight * ratio);
        if (scrollTop != container.scrollTop) {
            container.scrollTop = scrollTop;
            this.onYScroll();
            dorado.Toolkits.cancelDelayedAction(this._container, "$scrollTimerId");
            this._container.$scrollTimerId = 1;
        }
    }, setScrollingIndicator:function (text) {
        var indicator = this.getScrollingIndicator();
        $fly(indicator).text(text).show();
        $DomUtils.placeCenterElement(indicator, this.grid.getDom());
    }, setHoverRow:function (row) {
        if (row && row.rowType) {
            row = null;
        }
        row = (row == null) ? null : ((row && row.nodeType) ? row : this._itemDomMap[row]);
        $invokeSuper.call(this, arguments);
        var grid = this.grid;
        if (row && grid._draggable && grid._dragMode != "control") {
            grid.applyDraggable(row);
        }
        if (grid._domMode != 2 || grid._processingSetHoverRow) {
            return;
        }
        grid._processingSetHoverRow = true;
        (this == grid._innerGrid ? grid._fixedInnerGrid : grid._innerGrid).setHoverRow(row ? row.itemId : null);
        grid._processingSetHoverRow = false;
    }, showColumnEditor:function (column) {
        var grid = this.grid;
        var row = this._currentRow;
        if (!row) {
            return;
        }
        if (grid._currentCell) {
            $fly(grid._currentCell).removeClass("current-cell");
        }
        for (var i = 0; i < row.cells.length; i++) {
            var cell = row.cells[i];
            if (cell.colId == column._id) {
                var gridDom = grid.getDom();
                if (gridDom.scrollWidth > gridDom.clientWidth) {
                    var offset1 = $fly(gridDom).offset(), offset2 = $fly(cell).offset();
                    var l = offset2.left - offset1.left;
                    with (gridDom) {
                        if ((l + cell.offsetWidth) > clientWidth) {
                            scrollLeft -= clientWidth - (l + cell.offsetWidth);
                        } else {
                            if (l < 0) {
                                scrollLeft += l;
                            }
                        }
                    }
                } else {
                    var container = this.getDom().parentNode;
                    if (grid._divScroll && container.scrollWidth > container.clientWidth) {
                        var scrollPos = -1, ratio;
                        with (container) {
                            if ((cell.offsetLeft + cell.offsetWidth) > (scrollLeft + clientWidth)) {
                                scrollPos = cell.offsetLeft + cell.offsetWidth - clientWidth;
                            } else {
                                if (cell.offsetLeft < scrollLeft) {
                                    scrollPos = cell.offsetLeft;
                                }
                            }
                            ratio = scrollPos / scrollWidth;
                        }
                        if (scrollPos >= 0) {
                            var divScroll = grid._divScroll;
                            divScroll.scrollLeft = ratio * divScroll.scrollWidth;
                        }
                    }
                }
                grid._currentCell = cell;
                if (!grid.get("readOnly")) {
                    $fly(cell).addClass("current-cell");
                }
                var currentItem = this.getCurrentItem(), cellEditor;
                if (currentItem) {
                    cellEditor = grid._currentCellEditor = grid.getCellEditor(column, currentItem);
                }
                if (cellEditor) {
                    if (cellEditor.shouldShow()) {
                        cellEditor.show(this, cell);
                    }
                } else {
                    var fc = dorado.widget.findFocusableControlInElement(cell);
                    if (fc) {
                        if (!fc.get("focused")) {
                            fc.setFocus();
                        }
                    } else {
                        if (!grid.get("focused")) {
                            grid.setFocus();
                        }
                    }
                }
                break;
            }
        }
    }, onHeaderMouseMove:function (evt) {
        if ($DomUtils.isDragging()) {
            return;
        }
        var grid = this.grid, headerTable = this._headerTable;
        var offset = $fly(headerTable).offset(), action;
        var dataColumns = this._columnsInfo.dataColumns;
        for (var i = 0; i < dataColumns.length; i++) {
            var col = dataColumns[i];
            var headerCell = col.headerCell;
            if (Math.abs((headerCell.offsetLeft + headerCell.offsetWidth) - (evt.pageX - offset.left)) < 2 && (evt.pageY - offset.top) > getCellOffsetTop(headerCell, grid._headerRowHeight)) {
                action = "resize";
                showColumnResizeHandler(col);
                break;
            }
        }
        if (!action) {
            var headerCell = $DomUtils.findParent(evt.target, function (node) {
                return node.nodeName == "TD" && node.parentNode.parentNode.parentNode == headerTable;
            }, true);
            if (headerCell) {
                var column = grid._columnsInfo.idMap[headerCell.colId];
                if (column) {
                    grid.setHoverHeaderColumn(column);
                }
            }
        }
        return !action;
    }, onHeaderMouseLeave:function () {
        if ($DomUtils.isDragging()) {
            return;
        }
        var grid = this.grid;
        grid.setHoverHeaderColumn(null);
        return true;
    }, setSelection:function (selection) {
        if (this.fixed) {
            this.grid._innerGrid._selection = selection;
        } else {
            this._selection = selection;
        }
    }, toggleItemSelection:function (item, selected) {
        $invokeSuper.call(this, arguments);
        var grid = this.grid;
        if (grid._domMode != 2 || grid._processingToggleItemSelection) {
            return;
        }
        grid._processingToggleItemSelection = true;
        ((this == grid._fixedInnerGrid) ? grid._innerGrid : grid._fixedInnerGrid).toggleItemSelection(item, selected);
        grid._processingToggleItemSelection = false;
    }});
    function getColumnDragHelper(evt, draggableElement) {
        var cell = draggableElement;
        var ind = window._dragColIndicator;
        if (!ind) {
            window._dragColIndicator = ind = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-col-drag-helper", style:{position:"absolute"}, content:{tagName:"TABLE", style:{width:"100%", height:"100%"}, content:{tagName:"TR", content:"^TD"}}});
            document.body.appendChild(ind);
        }
        if (cell) {
            $fly(ind).outerWidth(cell.offsetWidth).outerHeight(cell.offsetHeight).show().bringToFront();
            $fly(ind).find(">TABLE>TBODY>TR>TD").empty().attr("align", cell.align).append(cell.firstChild.cloneNode(true));
        }
        return ind;
    }
    function showColumnDropIndicator(grid, dropPosition) {
        if (!dropPosition) {
            hideColumnDropIndicator();
            return;
        }
        var ind = window._colDropIndicator;
        if (!ind) {
            var ind1 = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-col-drag-top", style:{position:"absolute"}});
            var ind2 = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-col-drag-bottom", style:{position:"absolute"}});
            var ind3 = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-col-drop-indicator", style:{width:1, position:"absolute"}});
            document.body.appendChild(ind1);
            document.body.appendChild(ind2);
            document.body.appendChild(ind3);
            window._colDropIndicator = ind = [ind1, ind2, ind3];
        }
        var cacheId = dropPosition.column._id + dropPosition.before;
        if (cacheId != window._colDropPosition) {
            window._colDropPosition = cacheId;
            var cell = dropPosition.column.headerCell;
            var offset = $fly(cell).offset();
            var cellOffsetTop = getCellOffsetTop(cell, grid._headerRowHeight);
            offset.top = $fly(cell.offsetParent).offset().top + cellOffsetTop;
            var arrowHeight = $setting["GridColDropIndicatorSize"] || 9;
            $fly(ind[0]).top(offset.top - arrowHeight);
            var top2 = $fly(cell.offsetParent).offset().top + cell.offsetParent.offsetHeight;
            $fly(ind[1]).top(top2);
            var widthAdj = dropPosition.before ? 0 : cell.offsetWidth;
            $fly([ind[0], ind[1]]).left(offset.left + widthAdj - parseInt(arrowHeight / 2));
            $fly(ind[2]).position(offset.left + widthAdj - parseInt(ind[2].offsetWidth / 2), offset.top).height(top2 - offset.top);
            if (ind[0].parentNode != document.body) {
                document.body.appendChild(ind[0]);
                document.body.appendChild(ind[1]);
                document.body.appendChild(ind[2]);
            }
            $fly(ind).show().bringToFront();
        }
    }
    function hideColumnDropIndicator() {
        window._colDropPosition = undefined;
        var ind = window._colDropIndicator;
        if (ind) {
            $fly(ind).hide();
        }
    }
    function showColumnResizeHandler(column) {
        var handler = window._colResizingHanlder, minSize = 5;
        if (!handler) {
            window._colResizingHanlder = handler = $DomUtils.xCreateElement({tagName:"DIV", className:"d-grid-col-resize-handler", style:{position:"absolute", width:6}, onmouseleave:function () {
                $fly(handler).hide();
            }});
            document.body.appendChild(handler);
            $DomUtils.disableUserSelection(handler);
            $fly(handler).draggable({distence:3, helper:getColumnResizeHelper, axis:"x", start:function (evt, ui) {
                var column = $fly(handler).data("column");
                var grid = column.get("grid");
                if (grid._currentCellEditor) {
                    grid._currentCellEditor.hide();
                    dorado.widget.onControlGainedFocus(grid);
                }
                var cell = column.headerCell;
                var tableOffset = $fly(cell.offsetParent).offset();
                var cellOffsetTop = getCellOffsetTop(cell, grid._headerRowHeight);
                var cellOffset = $fly(cell).offset();
                cellOffset.top = tableOffset.top + cellOffsetTop;
                this._originLeft = cellOffset.left;
                this._originWidth = evt.pageX - cellOffset.left;
                var height = ((grid._domMode == 0) ? (grid.getDom()) : (grid._divScroll)).clientHeight - cellOffsetTop;
                ui.helper.show().bringToFront().position(this._originLeft, cellOffset.top).height(height);
            }, drag:function (evt, ui) {
                ui.position.left = this._originLeft;
                if (evt.pageX - this._originLeft > minSize) {
                    ui.helper.width(evt.pageX - this._originLeft);
                }
            }, stop:function (evt, ui) {
                var width = evt.pageX - this._originLeft;
                if (width < minSize) {
                    width = minSize;
                }
                if (width != this._originWidth) {
                    var column = $fly(handler).data("column");
                    var grid = column.get("grid");
                    column._realWidth = column._width = column._realWidth + (width - this._originWidth);
                    grid._ignoreItemTimestamp = true;
                    grid.stretchColumnsToFit();
                    grid.refresh();
                }
                return true;
            }});
        }
        var columnCell = column.headerCell, $columnCell = $fly(columnCell);
        var offset = $columnCell.offset();
        $fly(handler).data("column", column).top(offset.top).left(offset.left + columnCell.offsetWidth - 3).height(columnCell.offsetHeight).show();
        return handler;
    }
    function hideColumnResizeHandler() {
        var handler = window._colResizingHanlder;
        if (handler) {
            $fly(handler).hide();
        }
    }
    function getColumnResizeHelper() {
        if (!window._colResizeHelper) {
            window._colResizeHelper = $fly(document.body).xCreate({tagName:"DIV", style:{position:"absolute"}, content:{tagName:"DIV", className:"d-grid-col-resize-helper", style:{height:"100%"}}}, null, {returnNewElements:true});
        }
        return window._colResizeHelper;
    }
})();
(function () {
    dorado.widget.Grid = $extend(dorado.widget.AbstractGrid, {$className:"dorado.widget.Grid", ATTRIBUTES:{currentIndex:{skipRefresh:true, getter:function (p) {
        return (this._domMode == 2 ? this._fixedInnerGrid : this._innerGrid).get(p);
    }, setter:function (p, index) {
        if (!this._ready) {
            return;
        }
        if (index >= this._itemModel.getItemCount()) {
            index = -1;
        }
        (this._domMode == 2 ? this._fixedInnerGrid : this._innerGrid).set(p, index);
    }}, currentEntity:{readOnly:true, getter:function () {
        return this._innerGrid.getCurrentItem(0);
    }}, items:{setter:function (p, items) {
        this.set("selection", null);
        this._itemModel.setItems(items);
    }, getter:function () {
        return this._itemModel.getItems();
    }}, groupProperty:{setter:function (p, v) {
        if (this._groupProperty == v) {
            return;
        }
        this.set("currentIndex", -1);
        $invokeSuper.call(this, arguments);
    }}}, createInnerGrid:function (fixed) {
        return new dorado.widget.grid.InnerGrid(this, fixed);
    }, refreshDom:function (dom) {
        this.set("currentIndex", this._currentIndex);
        $invokeSuper.call(this, arguments);
    }, _doOnKeyDown:function (evt) {
        var retValue = true;
        var items = this._itemModel.getItems();
        switch (evt.keyCode) {
          case 36:
            if (evt.ctrlKey) {
                this.set("currentIndex", 0);
            } else {
                this.setCurrentColumn(this._columnsInfo.dataColumns[0]);
            }
            break;
          case 35:
            if (evt.ctrlKey) {
                this.set("currentIndex", this._itemModel.getItemCount() - 1);
            } else {
                var columns = this._columnsInfo.dataColumns;
                this.setCurrentColumn(columns[columns.length - 1]);
            }
            break;
          case 38:
            var index = this.get("currentIndex");
            if (index > 0) {
                this.set("currentIndex", index - 1);
            }
            retValue = false;
            break;
          case 40:
            var index = this.get("currentIndex");
            if (index < this._itemModel.getItemCount() - 1) {
                this.set("currentIndex", index + 1);
            }
            retValue = false;
            break;
          case 13:
            retValue = false;
            var columns = this._columnsInfo.dataColumns, i;
            if (this._currentColumn) {
                i = columns.indexOf(this._currentColumn) || 0;
                if (evt.shiftKey) {
                    if (i > 0) {
                        i--;
                    } else {
                        var index = this.get("currentIndex");
                        if (index > 0) {
                            this.set("currentIndex", index - 1);
                            i = columns.length - 1;
                        } else {
                            retValue = true;
                        }
                    }
                } else {
                    if (i < columns.length - 1) {
                        i++;
                    } else {
                        var index = this.get("currentIndex");
                        if (index < this._itemModel.getItemCount() - 1) {
                            this.set("currentIndex", index + 1);
                            i = 0;
                        } else {
                            retValue = true;
                        }
                    }
                }
            } else {
                i = evt.shiftKey ? (columns.length - 1) : 0;
            }
            this.setCurrentColumn(columns[i]);
            break;
        }
        return retValue;
    }, refreshEntity:function (entity) {
        var itemId = this._itemModel.getItemId(entity), row, innerGrid;
        if (this._domMode == 2) {
            innerGrid = this._fixedInnerGrid;
            row = innerGrid._itemDomMap[itemId];
            if (row) {
                innerGrid.refreshItemDomData(row, entity);
            }
        }
        innerGrid = this._innerGrid;
        row = innerGrid._itemDomMap[itemId];
        if (row) {
            innerGrid.refreshItemDomData(row, entity);
        }
    }, onCellValueEdit:function (entity, column) {
        this.refreshEntity(entity);
        if (!entity.rowType) {
            this.onEntityChanged(entity, column._property);
        }
        this.refreshSummary();
        $invokeSuper.call(this, arguments);
    }, sort:function (column, desc) {
        $invokeSuper.call(this, arguments);
        this.refresh();
    }, highlightItem:function (entity, options, speed) {
        if (typeof entity == "number") {
            entity = this._itemModel.getItemAt(entity);
        }
        $invokeSuper.call(this, [entity, options, speed]);
    }});
    var ListBoxPrototype = dorado.widget.ListBox.prototype;
    dorado.widget.grid.InnerGrid = $extend(dorado.widget.grid.AbstractInnerGrid, {$className:"dorado.widget.grid.InnerGrid", ATTRIBUTES:{currentIndex:{skipRefresh:true, setter:function (p, index) {
        if (this._currentIndex == index) {
            return;
        }
        if (this._rendered && this._itemDomMap) {
            var itemModel = this._itemModel, item = itemModel.getItemAt(index);
            if (item && item.rowType) {
                return;
            }
        }
        ListBoxPrototype.ATTRIBUTES.currentIndex.setter.apply(this, arguments);
        this.grid.doInnerGridSetCurrentRow(this, index);
    }}}, getCurrentItem:ListBoxPrototype.getCurrentItem, setCurrentItemDom:ListBoxPrototype.setCurrentItemDom, getCurrentItemId:ListBoxPrototype.getCurrentItemId, refreshItemDom:ListBoxPrototype.refreshItemDom, getItemDomByItemIndex:ListBoxPrototype.getItemDomByItemIndex, setCurrentRowByItemId:function (itemId) {
        if (this._currentIndex != itemId) {
            this.set("currentIndex", itemId);
        }
    }});
})();
(function () {
    var ItemModel = $extend(dorado.widget.grid.ItemModel, {getItemCount:function () {
        var items = this._items;
        if (!items) {
            return 0;
        }
        if (this.groups || this._items instanceof Array) {
            return $invokeSuper.call(this, arguments);
        } else {
            if (!(items.pageSize > 0)) {
                return items.entityCount;
            } else {
                if (this.grid._supportsPaging || items.entityCount < items.pageSize) {
                    return items.entityCount;
                } else {
                    return items.pageSize;
                }
            }
        }
    }, iterator:function () {
        if (!this._items) {
            return this.EMPTY_ITERATOR;
        }
        if (this.groups || this._items instanceof Array) {
            return $invokeSuper.call(this, arguments);
        } else {
            return this._items.iterator({simulateUnloadPage:this.grid._supportsPaging, currentPage:!this.grid._supportsPaging, nextIndex:this._startIndex || 0});
        }
    }, getItemAt:function (index) {
        if (!this._items || !(index >= 0)) {
            return null;
        }
        if (this.groups || this._items instanceof Array) {
            return $invokeSuper.call(this, arguments);
        } else {
            return this._items.iterator({simulateUnloadPage:this.grid._supportsPaging, currentPage:!this.grid._supportsPaging, nextIndex:index}).next();
        }
    }, getItemIndex:function (item) {
        if (!item || item.dummy) {
            return -1;
        }
        if (this.groups || this._items instanceof Array) {
            return $invokeSuper.call(this, arguments);
        } else {
            var entityList = this._items, itemId, page;
            if (item instanceof dorado.Entity) {
                itemId = item.entityId;
                page = item.page;
            } else {
                itemId = item;
                page = entityList.getById(itemId).page;
            }
            if (page.entityList != entityList) {
                return -1;
            }
            var index = 0, entry = page.first, found = false;
            while (entry != null) {
                if (entry.data.entityId == itemId) {
                    found = true;
                    break;
                }
                if (entry.data.state != dorado.Entity.STATE_DELETED) {
                    index++;
                }
                entry = entry.next;
            }
            if (found) {
                if (this.grid._supportsPaging) {
                    for (var i = page.pageNo - 1; i > 0; i--) {
                        index += entityList.getPage(i, false).entityCount;
                    }
                }
                return index;
            } else {
                return -1;
            }
        }
    }});
    dorado.widget.DataGrid = $extend([dorado.widget.AbstractGrid, dorado.widget.DataControl], {$className:"dorado.widget.DataGrid", ATTRIBUTES:{autoCreateColumns:{defaultValue:true}, supportsPaging:{}, appendOnLastEnter:{}, filterMode:{defaultValue:"clientSide"}, sortMode:{defaultValue:"clientSide"}}, createItemModel:function () {
        return new ItemModel(this);
    }, createInnerGrid:function (fixed) {
        return new dorado.widget.grid.InnerDataGrid(this, fixed);
    }, setCurrentEntity:function (entity) {
        this._innerGrid.setCurrentEntity(entity);
        if (this._domMode == 2) {
            this._fixedInnerGrid.setCurrentEntity(entity);
        }
    }, getCurrentEntity:function () {
        return this._innerGrid.getCurrentItem();
    }, addColumn:function () {
        var column = $invokeSuper.call(this, arguments);
        if (this._autoCreateColumns && column instanceof dorado.widget.grid.DataColumn && column._property) {
            var watcher = this.getAttributeWatcher();
            if (watcher.getWritingTimes("autoCreateColumns") == 0) {
                this._autoCreateColumns = false;
            }
        }
        return column;
    }, initColumns:function (dataType) {
        function doInitColumns(cols, dataType) {
            for (var i = 0; i < cols.length; i++) {
                var col = cols[i];
                if (col instanceof dorado.widget.grid.ColumnGroup) {
                    doInitColumns(col._columns.items, dataType);
                } else {
                    col._propertyDef = (col._property) ? dataType.getPropertyDef(col._property) : null;
                }
            }
        }
        if (dataType && (this._dataType != dataType || !this._columnInited)) {
            this._columnInited = true;
            this._dataType = dataType;
            if (dataType) {
                var columns = this._columns;
                if (this._autoCreateColumns && !this._defaultColumnsGenerated) {
                    this._defaultColumnsGenerated = true;
                    var self = this, columnsClear = false;
                    dataType._propertyDefs.each(function (pd) {
                        if (!pd._visible) {
                            return;
                        }
                        var column = columns.get(pd._name), columnConfig = {};
                        if (column) {
                            columns.remove(column);
                            columns.append(column);
                        }
                        var t = pd.getDataType(true);
                        if (t && (!t._code || !(t instanceof dorado.DataType))) {
                            return;
                        }
                        columnConfig.name = columnConfig.property = pd._name;
                        if (column) {
                            column.set(columnConfig, {tryNextOnError:true, preventOverwriting:true});
                        } else {
                            if (!columnsClear && columns.size == 1 && columns.get(0)._name == "empty") {
                                columns.clear();
                                columnsClear = true;
                            }
                            columnConfig.width = "*";
                            self.addColumn(new dorado.widget.grid.DataColumn(columnConfig));
                        }
                    });
                }
                doInitColumns(columns.items, dataType);
            }
        }
    }, refreshDom:function (dom) {
        var columnsInited = false;
        if (this._dataSet) {
            var entityList = this.getBindingData({firstResultOnly:true, acceptAggregation:true});
            if (entityList && !(entityList instanceof dorado.EntityList)) {
                throw new dorado.ResourceException("dorado.grid.BindingTypeMismatch", this._id);
            }
            var dataType;
            if (entityList && entityList.dataType) {
                dataType = entityList.dataType.getElementDataType("auto");
            }
            if (!dataType) {
                dataType = this.getBindingDataType("auto");
            }
            if (dataType) {
                this.initColumns(dataType);
                columnsInited = true;
            } else {
                if (this._autoCreateColumns && !this._listeningDataTypeRepository) {
                    this._columnInited = false;
                    this._listeningDataTypeRepository = true;
                    var grid = this;
                    this.get("dataTypeRepository").addListener("onDataTypeRegister", function (self, arg) {
                        var dataType = grid.getBindingDataType("never");
                        if (dataType) {
                            self.removeListener("onDataTypeRegister", arguments.callee);
                            grid._autoCreateColumns = true;
                            grid._listeningDataTypeRepository = false;
                            grid.initColumns(dataType);
                            grid.refresh(true);
                        }
                    });
                }
            }
            var oldItems = this._itemModel.getItems();
            if (oldItems != entityList) {
                this._itemModel.setItems(entityList);
                this.set("selection", null);
            }
        }
        if (!columnsInited) {
            this.initColumns();
        }
        $invokeSuper.call(this, arguments);
    }, refreshEntity:function (entity) {
        this._innerGrid.refreshEntity(entity);
        if (this._domMode == 2) {
            this._fixedInnerGrid.refreshEntity(entity);
        }
    }, onEntityInserted:function (arg) {
        if (this._domMode == 2) {
            this._fixedInnerGrid.onEntityInserted(arg);
        }
        this._innerGrid.onEntityInserted(arg);
        this.fixSizeBugs();
    }, onEntityDeleted:function (arg) {
        this._innerGrid.onEntityDeleted(arg);
        if (this._domMode == 2) {
            this._fixedInnerGrid.onEntityDeleted(arg);
        }
        this.fixSizeBugs();
    }, _doOnKeyDown:function (evt) {
        var retValue = true;
        var items = this._itemModel.getItems();
        switch (evt.keyCode) {
          case 36:
            if (evt.ctrlKey) {
                items.first();
            } else {
                this.setCurrentColumn(this._columnsInfo.dataColumns[0]);
            }
            break;
          case 35:
            if (evt.ctrlKey) {
                items.last();
            } else {
                var columns = this._columnsInfo.dataColumns;
                this.setCurrentColumn(columns[columns.length - 1]);
            }
            break;
          case 38:
            items.previous();
            retValue = false;
            break;
          case 40:
            items.next();
            retValue = false;
            break;
          case 13:
            retValue = false;
            var columns = this._columnsInfo.dataColumns, i;
            if (this._currentColumn) {
                i = columns.indexOf(this._currentColumn) || 0;
                if (evt.shiftKey) {
                    if (i > 0) {
                        i--;
                    } else {
                        if (items.hasPrevious()) {
                            items.previous();
                            i = columns.length - 1;
                        } else {
                            retValue = true;
                        }
                    }
                } else {
                    if (i < columns.length - 1) {
                        i++;
                    } else {
                        if (items.hasNext()) {
                            items.next();
                            i = 0;
                        } else {
                            if (this._appendOnLastEnter && items.current) {
                                items.insert();
                                i = 0;
                            } else {
                                retValue = true;
                            }
                        }
                    }
                }
            } else {
                i = evt.shiftKey ? (columns.length - 1) : 0;
            }
            this.setCurrentColumn(columns[i]);
            break;
          case 45:
            items.insert();
            break;
          case 46:
            if (evt.ctrlKey) {
                items.remove();
            }
            break;
        }
        return retValue;
    }, filterDataSetMessage:function (messageCode, arg) {
        var itemModel = this._itemModel;
        var items = itemModel.getItems();
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_REFRESH:
            return true;
          case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
            return (arg.entityList == items || dorado.DataUtil.isOwnerOf(items, arg.entityList));
          case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
          case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
            return (!items || items._observer != this._dataSet || arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
          case dorado.widget.DataSet.MESSAGE_DELETED:
            return (arg.entity.parent == items || dorado.DataUtil.isOwnerOf(items, arg.entity));
          case dorado.widget.DataSet.MESSAGE_INSERTED:
            return (arg.entityList == items);
          case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
            return (arg.entity.parent == items);
          default:
            return false;
        }
    }, processDataSetMessage:function (messageCode, arg, data) {
        switch (messageCode) {
          case dorado.widget.DataSet.MESSAGE_REFRESH:
            if (this._currentCellEditor) {
                this._currentCellEditor.hide();
            }
            if (this._itemModel.groups) {
                this.setDirtyMode(true);
            } else {
                this.refresh(true);
            }
            break;
          case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
            if (this._currentCellEditor) {
                this._currentCellEditor.hide();
            }
            if (arg.entityList == this._itemModel.getItems()) {
                var oldCurrentEntity = this.getCurrentEntity();
                if (!this._supportsPaging && (!oldCurrentEntity || oldCurrentEntity.page.pageNo != arg.entityList.pageNo)) {
                    this.refresh(true);
                } else {
                    this.setCurrentEntity(arg.entityList.current);
                }
            } else {
                this.refresh(true);
            }
            break;
          case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
            var items = this._itemModel.getItems();
            if (!items || items._observer != this._dataSet) {
                this.refresh(true);
            } else {
                var entity = arg.entity;
                this.refreshEntity(entity);
                if (!entity.rowType) {
                    this.onEntityChanged(entity, arg.property);
                }
                this.refreshSummary();
            }
            break;
          case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
            var items = this._itemModel.getItems();
            if (!items || items._observer != this._dataSet) {
                this.refresh(true);
            } else {
                if (this._itemModel.groups) {
                    this.setDirtyMode(true);
                } else {
                    this.refreshEntity(arg.entity);
                }
            }
            break;
          case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
            this.refreshEntity(arg.entity);
            break;
          case dorado.widget.DataSet.MESSAGE_DELETED:
            if (this._itemModel.groups) {
                this.setDirtyMode(true);
            } else {
                this.onEntityDeleted(arg);
                this.refreshSummary();
            }
            break;
          case dorado.widget.DataSet.MESSAGE_INSERTED:
            if (this._itemModel.groups) {
                this.setDirtyMode(true);
            } else {
                this.onEntityInserted(arg);
                this.refreshSummary();
            }
            break;
        }
    }, _requirePage:function (pageNo, timeout) {
        var requiredPages = this._requiredPages;
        if (!requiredPages) {
            this._requiredPages = requiredPages = [];
        }
        var loadingPages = this._loadingPages;
        if (loadingPages && loadingPages.indexOf(pageNo) >= 0) {
            return;
        }
        if (this._loadPageTimerId) {
            clearTimeout(this._loadPageTimerId);
            delete this._loadPageTimerId;
        }
        if (requiredPages.indexOf(pageNo) < 0) {
            requiredPages.push(pageNo);
        }
        this._loadPageTimerId = $setTimeout(this, function () {
            this._loadingPages = requiredPages;
            delete this._requiredPages;
            var items = this._itemModel.getItems();
            for (var i = 0; i < requiredPages.length; i++) {
                items.getPage(requiredPages[i], true, dorado._NULL_FUNCTION);
            }
        }, timeout || 20);
    }, filter:function (filterParams) {
        if (this._filterMode == "serverSide") {
            var dataSet = this._dataSet;
            if (!dataSet) {
                return;
            }
            var parameter = dataSet._parameter;
            if (parameter != null && (typeof parameter != "object" || parameter instanceof Date)) {
                return;
            }
            if (!parameter) {
                parameter = {};
            }
            if (filterParams === undefined) {
                var filterEntity = this._itemModel.filterEntity, criterions = [];
                var dataColumns = this._columnsInfo.dataColumns;
                for (var i = 0; i < dataColumns.length; i++) {
                    var column = dataColumns[i];
                    if (!column._property) {
                        continue;
                    }
                    var text = filterEntity.get(column._property);
                    if (text != null) {
                        criterions.push({property:column._property, expression:text});
                    }
                }
                parameter.$criterions = criterions;
            } else {
                delete parameter.$criterions;
            }
            dataSet.set("parameter", parameter);
            dataSet.flushAsync();
        } else {
            return $invokeSuper.apply(this, arguments);
        }
    }, sort:function (column, desc) {
        var itemModel = this._itemModel;
        if (this._filterMode == "serverSide") {
            var dataSet = this._dataSet;
            if (!dataSet) {
                return;
            }
            var parameter = dataSet._parameter;
            if (parameter != null && (typeof parameter != "object" || parameter instanceof Date)) {
                return;
            }
            if (!parameter) {
                parameter = {};
            }
            var sortParams = null;
            if (column) {
                parameter.$orders = sortParams = [{property:column.get("property"), desc:desc}];
            } else {
                delete parameter.$orders;
            }
            var grid = this.grid, columns = grid._columnsInfo.dataColumns, sortParamMap = {};
            dataSet.set("parameter", parameter);
            dataSet.flushAsync(function () {
                for (var i = 0; i < sortParams.length; i++) {
                    var sortParam = sortParams[i];
                    if (sortParam.property) {
                        sortParamMap[sortParam.property] = !!sortParam.desc;
                    }
                }
                for (var i = 0; i < columns.length; i++) {
                    var column = columns[i], desc = sortParamMap[column._property];
                    if (desc == null) {
                        column.set("sortState", null);
                    } else {
                        column.set("sortState", desc ? "desc" : "asc");
                    }
                }
            });
        } else {
            return $invokeSuper.apply(this, arguments);
        }
    }});
    var DataListBoxProtoType = dorado.widget.DataListBox.prototype;
    dorado.widget.grid.InnerDataGrid = $extend(dorado.widget.grid.AbstractInnerGrid, {$className:"dorado.widget.grid.InnerDataGrid", getItemId:DataListBoxProtoType.getItemId, getCurrentItem:DataListBoxProtoType.getCurrentItem, getCurrentItemId:DataListBoxProtoType.getCurrentItemId, setCurrentItemDom:DataListBoxProtoType.setCurrentItemDom, refreshEntity:DataListBoxProtoType.refreshEntity, refreshItemDom:DataListBoxProtoType.refreshItemDom, onEntityDeleted:DataListBoxProtoType.onEntityDeleted, onEntityInserted:DataListBoxProtoType.onEntityInserted, _adjustBeginBlankRow:DataListBoxProtoType._adjustBeginBlankRow, _adjustEndBlankRow:DataListBoxProtoType._adjustEndBlankRow, setCurrentRowByItemId:function (itemId) {
        if (!this._itemDomMap) {
            return;
        }
        var row = (itemId == null) ? null : this._itemDomMap[itemId];
        var item = row ? $fly(row).data("item") : null;
        this.setCurrentEntity(item);
        this._itemModel.getItems().setCurrent(item);
    }, setCurrentEntity:function (entity) {
        if (entity) {
            if (entity.dummy) {
                this.grid._requirePage(entity.page.pageNo);
                return false;
            }
            if (entity.rowType) {
                return false;
            }
        }
        DataListBoxProtoType.setCurrentEntity.apply(this, arguments);
        this.grid.doInnerGridSetCurrentRow(this, entity ? entity.entityId : null);
        return true;
    }, doRefreshItemDomData:function (row, item) {
        $invokeSuper.call(this, arguments);
        row.dummy = item.dummy;
        if (row.dummy) {
            row.pageNo = item.page.pageNo;
            if (this._requiredPages) {
                this._requiredPages.push(row.pageNo);
            }
        }
        $fly(row).toggleClass("dummy-row", !!row.dummy);
    }, refreshViewPortContent:function (container) {
        this._requiredPages = [];
        $invokeSuper.call(this, arguments);
        for (var i = 0; i < this._requiredPages.length; i++) {
            this.grid._requirePage(this._requiredPages[i]);
        }
    }});
})();

