
dorado.widget.Portlet = $extend(dorado.widget.Panel, {ATTRIBUTES:{showCaptionBar:{defaultValue:true}, border:{defaultValue:"curve"}, column:{defaultValue:0}}, createDom:function () {
    var portlet = this, dom = $invokeSuper.call(this, arguments);
    $DomUtils.disableUserSelection(portlet._doms.captionBar);
    $fly(dom).draggable({addClasses:false, handle:".d-caption-bar", scope:"portal", cursor:"move", distance:10, tolerance:"pointer", helper:"clone", start:function () {
        if (dorado.Browser.chrome) {
            $DomUtils.disableUserSelection(document.body);
        }
        portlet._parent._draggingPortlet = portlet;
        $fly(dom).css("display", "none");
    }, drag:function () {
        var portal = portlet._parent, columnIndex = portal._dropColumnIndex, columnDoms = portal._columnDoms;
        if (columnIndex >= 0) {
            var currentColumn = portal._columns.get(columnIndex), columnDom = columnDoms[columnIndex], currentColumnPortlets = portal._columnPortlets[columnIndex], minDistance = 100000, result = null, resultDom, placeholder = portal._placeholder, draggable = jQuery.data(this, "draggable");
            if (!currentColumnPortlets) {
                currentColumnPortlets = portal._columnPortlets[columnIndex] = [];
            }
            if (placeholder) {
                var outlineDrop = {offset:$fly(placeholder).offset(), proportions:{width:$fly(placeholder).width(), height:$fly(placeholder).height()}};
                if (jQuery.ui.intersect(draggable, outlineDrop, "touch") && placeholder.parentNode == columnDom) {
                    return;
                }
            }
            for (var i = 0, j = currentColumnPortlets.length; i < j; i++) {
                var temp = currentColumnPortlets[i];
                if (temp._dom != this) {
                    var droppable = {offset:$fly(temp._dom).offset(), proportions:{width:$fly(temp._dom).width(), height:$fly(temp._dom).height()}}, interects = jQuery.ui.intersect(draggable, droppable, "touch");
                    if (interects) {
                        var distance = Math.sqrt(Math.pow((draggable.positionAbs.left + draggable.helperProportions.width / 2) - (droppable.offset.left + droppable.proportions.width / 2), 2) + Math.pow((draggable.positionAbs.top + draggable.helperProportions.height / 2) - (droppable.offset.top + droppable.proportions.height / 2), 2));
                        if (distance < minDistance) {
                            result = i;
                            resultDom = temp._dom;
                            minDistance = distance;
                        }
                    }
                }
            }
            var width;
            if (result != null && result >= 0) {
                portal._setPlaceHolderPosition(columnIndex, result);
                width = $fly(resultDom).width();
                $fly(portal._placeholder).outerWidth(width);
                $fly(portal._placeholder).outerHeight(draggable.helperProportions.height);
            } else {
                portal._setPlaceHolderPosition(columnIndex);
                width = $fly(columnDom).width();
                $fly(portal._placeholder).outerWidth(width);
                $fly(portal._placeholder).outerHeight(draggable.helperProportions.height);
            }
        }
    }, stop:function () {
        if (dorado.Browser.chrome) {
            $DomUtils.enableUserSelection(document.body);
        }
        $fly(dom).css("display", "");
        var portal = portlet._parent;
        portal._draggingPortlet = null;
        if (portal._placeholder) {
            $fly(portal._placeholder).css("display", "none");
        }
    }});
    return dom;
}});
dorado.widget.PortalColumn = function (config) {
    config = config || {};
    this.name = config.name;
    this.width = config.width || "*";
    this.className = config.className;
};
dorado.widget.Portal = $extend(dorado.widget.Control, {ATTRIBUTES:{className:{defaultValue:"d-portal"}, portlets:{writeOnly:true, setter:function (attr, value) {
    if (value instanceof Array) {
        this._columnPortlets = [];
        for (var i = 0, j = value.length; i < j; i++) {
            var portlet = value[i];
            if (!(portlet instanceof dorado.widget.Portlet)) {
                portlet = new dorado.widget.Portlet(portlet);
            }
            var column = portlet._column || 0;
            var columnPortlets = this._columnPortlets[column];
            if (!columnPortlets) {
                columnPortlets = this._columnPortlets[column] = [];
            }
            columnPortlets.push(portlet);
        }
    }
}}, columnPortlets:{}, columns:{setter:function (attr, value) {
    var portal = this, oldValue = portal._columns, result;
    if (value instanceof Array) {
        result = new dorado.util.KeyedArray(function (value) {
            return value.name;
        });
        for (var i = 0, j = value.length; i < j; i++) {
            var column = value[i];
            if (!(column instanceof dorado.widget.PortalColumn)) {
                column = new dorado.widget.PortalColumn(column);
            }
            result.append(column);
        }
    }
    if (oldValue) {
        var oldColumnLength = oldValue.size, newColumnLength = result.size, rendered = portal._rendered;
        for (var i = newColumnLength; i < oldColumnLength; i++) {
            var columnPortlets = portal._columnPortlets[i];
            if (columnPortlets) {
                for (var j = 0, l = columnPortlets.length; j < l; j++) {
                    var portlet = columnPortlets[j];
                    portal._columnPortlets[0].push(portlet);
                    if (rendered) {
                        portal._columnDoms[0].appendChild(portlet._dom);
                    }
                }
                columnPortlets.removeAt(i);
            }
            if (rendered) {
                $fly(portal._columnDoms[i]).css("display", "none");
            }
        }
        var dom = portal._dom;
        for (var i = oldColumnLength; i < newColumnLength; i++) {
            if (rendered) {
                var columnDom = portal._columnDoms[i];
                if (!columnDom) {
                    columnDom = portal._columnDoms[i] = portal._createColumn(i);
                    dom.appendChild(columnDom);
                } else {
                    $fly(columnDom).css("display", "");
                }
            }
        }
    }
    if (value == null) {
    }
    portal._columns = result;
}}, portletPadding:{defaultValue:10}}, insertPortlet:function (portlet, column, index) {
    var portal = this, columns = portal._columns, columnIndex = column, columnDoms = portal._columnDoms;
    if (!portlet || !columns) {
        return;
    }
    var columnSetting = columns.get(column), columnDom = columnDoms[column];
    if (columns) {
        if (typeof column == "string") {
            columnIndex = columns.indexOf(columnSetting);
        }
        var columnPortlets = portal._columnPortlets[columnIndex];
        if (!columnPortlets) {
            columnPortlets = portal._columnPortlets[columnIndex] = [];
        }
        if (typeof index == "number" && index >= 0) {
            if (columnDom) {
                var refPortlet = columnPortlets[index];
                portlet.render(columnDom, (refPortlet || {})._dom);
                $fly(portlet._dom).css("margin-bottom", portal._portletPadding);
                portlet._column = columnIndex;
                portal.registerInnerControl(portlet);
            }
            columnPortlets.insert(portlet, index);
        } else {
            if (columnDom) {
                portlet.render(columnDom);
                $fly(portlet._dom).css("margin-bottom", portal._portletPadding);
                portlet._column = columnIndex;
                portal.registerInnerControl(portlet);
            }
            columnPortlets.push(portlet);
        }
    }
}, appendPortlet:function (portlet, column) {
    this.insertPortlet(portlet, column);
}, removePortlet:function (portlet, column) {
    var portal = this, columns = portal._columns, columnIndex = column;
    if (!portlet || !columns) {
        return;
    }
    var columnSetting = columns.get(column);
    if (columns) {
        if (typeof column == "string") {
            columnIndex = columns.indexOf(columnSetting);
        }
        portal.unregisterInnerControl(portlet);
        var columnPortlets = portal._columnPortlets[columnIndex];
        if (columnPortlets) {
            columnPortlets.remove(portlet);
            portlet.destroy();
        }
    }
}, clearPortlets:function () {
    var portal = this, portlets = portal._columnPortlets;
    for (var i = 0; i < portlets.length; i++) {
        var columnPortlets = portlets[i];
        if (!columnPortlets) {
            continue;
        }
        for (var j = 0; j < columnPortlets.length; j++) {
            var portlet = columnPortlets[j];
            portlet.destroy();
        }
    }
    portal._columnPortlets = [];
}, _setPlaceHolderPosition:function (column, row) {
    var portal = this, placeholder = portal._placeholder;
    if (!placeholder) {
        placeholder = portal._placeholder = document.createElement("div");
        placeholder.className = "place-holder";
        $fly(placeholder).css("margin-bottom", portal._portletPadding);
    }
    placeholder.style.display = "";
    if (column == portal._placeholderColumn && row == portal._placeholderRow) {
        return;
    }
    portal._placeholderColumn = column;
    portal._placeholderRow = row;
    var columnSetting = portal._columns.get(column), columnDom = portal._columnDoms[column];
    if (row == null || row == undefined) {
        columnDom.appendChild(placeholder);
        portal._placeholderColumn = column;
        portal._placeholderRow = row;
    } else {
        var columnPortlets = portal._columnPortlets[column];
        if (columnPortlets) {
            var temp = columnPortlets[row];
            if (temp._dom) {
                try {
                    columnDom.insertBefore(placeholder, temp._dom);
                }
                catch (e) {
                }
            } else {
                columnDom.appendChild(placeholder);
            }
        }
    }
}, _createColumn:function (columnIndex) {
    var portal = this, columnDoms = portal._columnDoms, columnDom = columnDoms[columnIndex] = $DomUtils.xCreateElement({tagName:"div", className:"portal-column"});
    columnDom.index = columnIndex;
    $fly(columnDom).droppable({hoverClass:"portal-column-hover", scope:"portal", greedy:false, drop:function (ev, ui) {
        var column = portal._placeholderColumn, row = portal._placeholderRow;
        if (column == null) {
            return;
        }
        var columnPortlets = portal._columnPortlets[column];
        if (!columnPortlets) {
            columnPortlets = portal._columnPortlets[column] = [];
        }
        if (row == null) {
            row = columnPortlets.length;
            this.appendChild(portal._draggingPortlet._dom);
        } else {
            this.insertBefore(portal._draggingPortlet._dom, columnPortlets[row]._dom);
        }
        columnPortlets.insert(portal._draggingPortlet, row);
        if (portal._columnPortlets[portal._draggingPortlet._column]) {
            portal._columnPortlets[portal._draggingPortlet._column].remove(portal._draggingPortlet);
        }
        portal._draggingPortlet.set("width", $fly(this).width());
        portal._draggingPortlet._column = column;
        if (dorado.Browser.msie) {
            portal._dom.appendChild(portal._placeholder);
        }
        portal._placeholder.style.display = "none";
    }, over:function (ev, ui) {
        portal._dropColumnIndex = this.index;
    }, out:function (ev, ui) {
    }});
    return columnDom;
}, createDom:function () {
    var portal = this, dom = $DomUtils.xCreateElement({tagName:"div", className:portal._className, style:{padding:portal._portletPadding + "px"}}), columnDoms;
    columnDoms = portal._columnDoms = [];
    var settings = portal._columns;
    if (settings) {
        for (var i = 0; i < settings.size; i++) {
            var columnDom = portal._createColumn(i);
            $fly(dom).append(columnDom);
        }
        var portlets = portal._columnPortlets;
        if (portlets) {
            for (var i = 0, k = portlets.length; i < k; i++) {
                var columnPortlets = portlets[i];
                if (!columnPortlets) {
                    continue;
                }
                var columnDom = columnDoms[i];
                for (var j = 0, l = columnPortlets.length; j < l; j++) {
                    var temp = columnPortlets[j];
                    temp.render(columnDom);
                    temp._column = i;
                    $fly(temp._dom).css("margin-bottom", portal._portletPadding);
                    portal.registerInnerControl(temp);
                }
            }
        }
    }
    return dom;
}, refreshDom:function (dom) {
    $invokeSuper.call(this, arguments);
    var portal = this, columns = portal._columns, columnDoms = portal._columnDoms;
    if (columns) {
        var portlets = portal._columnPortlets, width = $fly(dom).width(), height = $fly(dom).height(), columnCount = columns.size, viewWidth = width - (columnCount - 1) * portal._portletPadding;
        var starColumn = [], noneStartWidthTotal = 0, widthMap = [];
        for (var i = 0; i < columnCount; i++) {
            var columnSetting = columns.get(i), columnWidth = columnSetting.width || "*";
            if (columnWidth == "*") {
                starColumn.push(i);
            } else {
                var realWidth;
                if (String(columnWidth).indexOf("%") != -1) {
                    realWidth = viewWidth * parseInt(columnWidth, 10) / 100;
                } else {
                    realWidth = parseInt(columnWidth, 10);
                }
                noneStartWidthTotal += realWidth;
                widthMap[i] = realWidth;
            }
        }
        var leftWidth = viewWidth - noneStartWidthTotal, starColumnCount = starColumn.length, leftWidthAverage = leftWidth / starColumnCount;
        for (var i = 0; i < starColumnCount; i++) {
            widthMap[starColumn[i]] = leftWidthAverage;
        }
        for (var i = 0; i < columnCount; i++) {
            var columnDom = columnDoms[i], columnWidth = widthMap[i];
            $fly(columnDom).width(columnWidth).outerHeight(height).css("padding-right", i != columnCount - 1 ? portal._portletPadding : 0);
            var columnPortlets = portlets[i];
            if (!columnPortlets) {
                continue;
            }
            for (var k = 0, l = columnPortlets.length; k < l; k++) {
                var temp = columnPortlets[k];
                temp.set("width", columnWidth);
            }
        }
    }
}});

