(function(){

	var fakePortlet;

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Advance
	 * @class 门户组件。
	 * @extends dorado.widget.Panel
	 */
	dorado.widget.Portlet = $extend(dorado.widget.Panel, /** @scope dorado.widget.Portlet.prototype */{
		$className: "dorado.widget.Portlet",
		ATTRIBUTES: /** @scope dorado.widget.Portlet.prototype */{
			showCaptionBar: {
				defaultValue: true
			},

			border: {
				defaultValue: "curve"
			},

			/**
			 * Portlet所在的PortletColumn的索引。<br />
			 * 该属性的设置只在渲染完成之前起作用，请勿在渲染完成之后设置该属性，以免引起Portal组件的工作不正常。
			 * @attribute
			 * @type int
			 */
			column: {
				defaultValue: 0
			},

            closeable: {
			    defaultValue: true
            },

            closeAction: {
                defaultValue: "close"
            },
            
            closeable: {
            	defaultValue: true
            }
		},

		createDom: function() {
			var portlet = this, dom = $invokeSuper.call(this, arguments);
			$DomUtils.disableUserSelection(portlet._doms.captionBar);

			$fly(dom).css("position", "absolute").draggable({
				addClasses: false,
				handle: ".d-caption-bar",
				scope: "portal",
				appendTo: "body",
				cursor: "move",
				distance: 10,
				tolerance: "pointer",
				helper: function() {
					if (!fakePortlet) {
						fakePortlet  = new dorado.widget.Portlet({ exClassName: "d-portlet-helper" });
						fakePortlet.render(document.body);
					}
					return fakePortlet._dom;
				},
				start: function(event, ui) {
					if (dorado.Browser.chrome) $DomUtils.disableUserSelection(document.body);

					fakePortlet.set({
						caption: portlet._caption,
						width: dom.offsetWidth,
						height: dom.offsetHeight
					});
					fakePortlet.refresh();

					var helper = ui.helper;
					helper.css({ width: dom.offsetWidth, height: dom.offsetHeight }).bringToFront();

					$fly(dom).css("visibility", "hidden");

					portlet._parent._draggingPortlet = portlet;
					portlet._parent.showColumns();
				},
				drag: function() {
					var portal = portlet._parent, columnIndex = portal._dropColumnIndex;
					if (columnIndex >= 0) {
						var currentColumnPortlets = portal._columnPortlets[columnIndex], minDistance = 100000000, result = null, resultDom,
							placeholder = portal._placeholder, draggable = jQuery.data(this, "draggable");

						if (!currentColumnPortlets) {
							currentColumnPortlets = portal._columnPortlets[columnIndex] = [];
						}

						if (placeholder) {
							var outlineDrop = {
								offset: $fly(placeholder).offset(),
								proportions: {
									width: $fly(placeholder).width(),
									height: $fly(placeholder).height()
								}
							};

							if (jQuery.ui.intersect(draggable, outlineDrop, "touch") && portal._placeholderColumn == columnIndex) {
								return;
							}
						}

						for (var i = 0, j = currentColumnPortlets.length; i < j; i++) {
							var temp = currentColumnPortlets[i];
							if (temp._dom != this) {
								var droppable = {
									offset: $fly(temp._dom).offset(),
									proportions: {
										width: $fly(temp._dom).width(),
										height: $fly(temp._dom).height()
									}
								}, interects = jQuery.ui.intersect(draggable, droppable, "touch");

								if (interects) {
									var distance = Math.sqrt(
											Math.pow((draggable.positionAbs.left + draggable.helperProportions.width / 2) -
												(droppable.offset.left + droppable.proportions.width / 2), 2) +
											Math.pow((draggable.positionAbs.top + draggable.helperProportions.height / 2) -
												(droppable.offset.top + droppable.proportions.height / 2), 2));

									if (distance < minDistance) {
										result = i;
										resultDom = temp._dom;
										minDistance = distance;
									}
								}
							}
						}

						portal._setPlaceHolderPosition(columnIndex, result);
					}
				},
				stop: function(event, ui) {
					if (dorado.Browser.chrome) $DomUtils.enableUserSelection(document.body);
					$fly(dom).css("visibility", "");

					var portal = portlet._parent;

					if (portal._placeholder) {
						$fly(portal._placeholder).css("display", "none");
					}

					portal._draggingPortlet = null;
				}
			});

			return dom;
		},

        close: function() {
            var portal = this._parent;
            if (portal) {
                portal.doOnPortletClose(this);
            }

            $invokeSuper.call(this, arguments);
        }
	});

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class dorado.widget.PortalColumn
	 * width：
	 * 	可以使用具体像素，比如500。
	 * 	可以使用百分比，比如30%，建议使用百分比。
	 * 	可以使用*，代表使用剩余空间，如果有多个*，则平均分。
	 */
	dorado.widget.PortalColumn = function(config) {
		config = config || {};
		this.width = config.width || "*";
		this.className = config.className;
	};

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Advance
	 * @class Portal
	 * @extends dorado.widget.Control
	 */
	dorado.widget.Portal = $extend(dorado.widget.Control, /** @scope dorado.widget.Portal.prototype */{
		$className: "dorado.widget.Portal",
        _inherentClassName: "i-portal",
		ATTRIBUTES: /** @scope dorado.widget.Portal.prototype */{
			className: {
				defaultValue: "d-portal"
			},

            defaultPortletHeight: {
                defaultValue: 200
            },

			/**
			 * Portal组件包含的Portlet的集合，只建议初始化的时候设置。
			 * @attribute
			 * @type dorado.widget.Portlet[]|Object[]
			 */
			portlets: {
				setter: function(value) {
					var oldValue = this._portlets;
					if (oldValue) {
						this.clearPortlets();
					}
					if (value instanceof Array) {
						this._columnPortlets = [];
						for (var i = 0, j = value.length; i < j;i++) {
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
				},
				getter: function() {
					var columnPortlets = this._columnPortlets;
					if (columnPortlets) {
						var result = [];
						for (var i = 0; i < columnPortlets.length; i++) {
							var portlets = columnPortlets[i];
							for (var j = 0; j < portlets.length; j++) {
								result.push(portlets[j]);
							}
						}
						return result;
					}
				}
			},

			columnPortlets: {},

			/**
			 * Portal的列的配置信息，列的配置信息包括width和className。
			 * 可以在渲染后动态的设置该属性，该属性会自动的处理冗余列中的Portlet。
			 * @attribute
			 * @type Object[]|dorado.util.KeyedArray
			 */
			columns: {
				setter: function(value) {
					var portal = this, oldValue = portal._columns, result, i, j, l;
					if (value instanceof Array) {
						result = new dorado.util.KeyedArray();
						for (i = 0, j = value.length; i < j; i++) {
							var column = value[i];
							if (!(column instanceof dorado.widget.PortalColumn)) {
								column = new dorado.widget.PortalColumn(column);
							}
							result.append(column);
						}
					}
					if (oldValue) {
						var oldColumnLength = oldValue.size,  newColumnLength = result.size, rendered = portal._rendered;
						//if old length > new length, relayout
						for (i = newColumnLength; i < oldColumnLength; i++) {
							var columnPortlets = portal._columnPortlets[i];
							if (columnPortlets) {
								for (j = 0, l = columnPortlets.length; j < l; j++) {
									var portlet = columnPortlets[j];
									portlet._column = 0;
									portal._columnPortlets[0].push(portlet);
								}
								portal._columnPortlets.removeAt(i);
							}
							if (rendered) {
								$fly(portal._columnDoms[i]).css("display", "none");
							}
						}
						//if new length > old length init new column
						var dom = portal._dom;
						for (i = oldColumnLength; i < newColumnLength; i++) {
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
						//do something. clear all portlets
					}

					portal._columns = result;

					if (rendered) {
						portal.calcColumnsWidth();
						portal.refresh();
					}
				}
			},

			/**
			 * Portal中的Portlet的padding。
			 * @attribute
			 * @type int
			 * @default 10
			 */
			portletPadding: {
				defaultValue: 10
			}
		},

        EVENTS: /** @scope dorado.widget.Portal.prototype */{
            /**
             * 当调用addPortlet成功后触发的事件，此事件在Portal初始化的时候不会被触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
             * @param {Object} arg.portlet 添加成功的portlet。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
            onPortletAdd: {},

            /**
             * 当调用removePortlet成功后(包括手动关闭)触发的事件，此事件在Portal初始化的时候不会被触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
             * @param {Object} arg.portlet 移除成功的portlet。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
            onPortletRemove: {},

            /**
             * 当Portlet被移动后触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
             * @param {Object} arg.portlet 移动成功的portlet。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
            onPortletMove: {}
        },

		/**
		 * 添加指定的Portlet到指定的位置，column的指定请直接设置Portlet的column属性。
		 * @param {dorado.widget.Portlet} portlet 要添加的Portlet。
		 * @param {int} [index] 要插入到的Portlet所在Column的索引，可以不提供，不提供则直接添加到最后。
		 */
		addPortlet: function(portlet, index) {
			var portal = this, columns = portal._columns, rendered = portal._rendered;
			if (!portlet || !columns) {
				return;
			}
            if (!portlet.render) {
                portlet = new dorado.widget.Portlet(portlet);
            }
			var column = portlet._column || 0, dom = portal._dom;
			if (columns) {
				var columnPortlets = portal._columnPortlets[column];
				if (!columnPortlets) {
					columnPortlets = portal._columnPortlets[column] = [];
				}

                if (portlet._height == undefined) {
                    portlet._height = portal._defaultPortletHeight;
                }

				if (rendered) {
					portlet.render(dom);
					portal.registerInnerControl(portlet);
				}

				if (typeof index == "number" && index >= 0) {
					columnPortlets.insert(portlet, index);
				} else {
					columnPortlets.push(portlet);
				}

                portal.fireEvent("onPortletAdd", portal, {
                    portlet: portlet
                });

				portal.refresh();
			}

		},

		/**
		 * 移除指定的Portlet。
		 * @param {dorado.widget.Portlet} portlet 要移除的Portlet。
		 */
		removePortlet: function(portlet) {
			var portal = this, columns = portal._columns;
			if (!portlet || !columns) {
				return;
			}
			var column = portlet._column || 0;
			if (columns) {
				portal.unregisterInnerControl(portlet);
				var columnPortlets = portal._columnPortlets[column];
				if (columnPortlets) {
					columnPortlets.remove(portlet);
					portlet.destroy();
				}

                portal.fireEvent("onPortletRemove", portal, {
                    portlet: portlet
                });

				portal.refresh();
			}
		},

        doOnPortletClose: function(portlet) {
            this.removePortlet(portlet);
        },

		/**
		 * 清除所有的Portlet。
		 */
		clearPortlets: function() {
			var portal = this, portlets = portal._columnPortlets;
			for (var i = 0;i < portlets.length;i++){
				var columnPortlets = portlets[i];
				if (!columnPortlets) {
					continue;
				}
				for (var j = 0; j < columnPortlets.length; j++) {
					var portlet = columnPortlets[j];
                    portal.unregisterInnerControl(portlet);
                    portlet.destroy();
                    portal.fireEvent("onPortletRemove", portal, {
                        portlet: portlet
                    });
				}
			}
			portal._columnPortlets = [];
		},

		_setPlaceHolderPosition: function(column, row) {
			//console.log("column:" + column + "\trow:" + row);

			var portal = this, placeholder = portal._placeholder;
			if (!placeholder) {
				placeholder = portal._placeholder = document.createElement("div");
				placeholder.className = "place-holder";
				portal._dom.appendChild(placeholder);
			}

			var portlets = portal._columnPortlets;
			if (typeof column =="number" && row == undefined) {
				row = portlets[column].length;
			}

			placeholder.style.display = "";

			if (column == portal._placeholderColumn && row == portal._placeholderRow) {
				return;
			}

			portal._placeholderColumn = column;
			portal._placeholderRow = row;

			portal.positionPortlets();

			var columnPortlets = portlets[column], left = portal._columnLeftMap[column], draggingPortlet = portal._draggingPortlet;
			$fly(placeholder).outerWidth(portal._columnWidthMap[column]).outerHeight(draggingPortlet._dom.offsetHeight);

			//console.log("draggingPortlet:" + draggingPortlet._caption);

			if (columnPortlets) {
				var holderTop = portal._portletPadding;
				for (var j = 0; j < row; j++) {
					var temp = columnPortlets[j], tempDom;
					if (temp) {
						if (temp == draggingPortlet) {
							continue;
						}
						tempDom = temp._dom;
						holderTop += tempDom.offsetHeight + portal._portletPadding;
					} else {
						holderTop += portal._portletPadding;
					}
				}
				$fly(placeholder).css({ left: left, top: holderTop });
			}
		},

		_createColumn: function(columnIndex) {
			var portal = this, columnDoms = portal._columnDoms,
				columnDom = columnDoms[columnIndex] = $DomUtils.xCreateElement({
					tagName: "div",
					className: "portal-column"
				});

			columnDom.index = columnIndex;
			$fly(columnDom).droppable({
				hoverClass: 'portal-column-hover',
				scope: "portal",
				greedy: false,
				drop: function(ev, ui) {
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
					}
					if (portal._columnPortlets[portal._draggingPortlet._column]) {
						portal._columnPortlets[portal._draggingPortlet._column].remove(portal._draggingPortlet);
					}
					columnPortlets.insert(portal._draggingPortlet, row);
					portal._draggingPortlet._column = column;
                    var dom = portal._draggingPortlet._dom;
                    $fly(dom).css({
                        left: portal._placeholder.style.left,
                        top: portal._placeholder.style.top
                    });

                    portal.fireEvent("onPortletMove", portal, {
                        portlet: portal._draggingPortlet
                    });

					portal._placeholder.style.display = "none";
					portal._draggingPortlet = null;
					portal._placeholderColumn = null;
					portal._placeholderRow = null;
					portal._dropColumnIndex = null;

					portal.hideColumns();

                    setTimeout(function() { portal.refresh(); }, 0);
				},
				over: function(ev, ui) {
					portal._dropColumnIndex = this.index;
				},
				out: function(ev, ui) {
				}
			});
			return columnDom;
		},

		createDom: function() {
			var portal = this, dom = $DomUtils.xCreateElement({
				tagName: "div",
				className: portal._className,
				style: {
					//padding: portal._portletPadding + "px"
				}
			}), columnDoms;

			columnDoms = portal._columnDoms = [];

			var settings = portal._columns, i, j, k, l;
			if (settings) {
				for (i = 0; i < settings.size; i++) {
					var tempDom = portal._createColumn(i);
					$fly(dom).append(tempDom);
				}

				var portlets = portal._columnPortlets;
				if (portlets) {
					for (i = 0, k = portlets.length; i < k; i++) {
						var columnPortlets = portlets[i];
						if (!columnPortlets) {
							continue;
						}
						for (j = 0, l = columnPortlets.length; j < l; j++) {
							var temp = columnPortlets[j];
                            if (temp._height == undefined) {
                                temp._height = portal._defaultPortletHeight;
                            }
							temp.render(dom);
							temp._column = i;
							$fly(temp._dom).css("margin-bottom", portal._portletPadding);
							portal.registerInnerControl(temp);
						}
					}
				}
			}

			return dom;
		},

		calcColumnsWidth: function() {
			var portal = this, columns = portal._columns, columnDoms = portal._columnDoms, dom = portal._dom;
			if (columns) {
				var width = dom.clientWidth, columnCount = columns.size, viewWidth;

				viewWidth = width - (columnCount + 1) * portal._portletPadding;

				//init width
				var starColumn = [], noneStarWidthTotal = 0, widthMap = [], leftMap = [];
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
						noneStarWidthTotal += realWidth;
						widthMap[i] = realWidth;
					}
				}

				var leftWidth = viewWidth - noneStarWidthTotal, starColumnCount = starColumn.length, leftWidthAverage = leftWidth / starColumnCount;
				for (var i = 0; i < starColumnCount; i++) {
					widthMap[starColumn[i]] = leftWidthAverage;
				}

				var columnLeft = 0;
				for (var i = 0; i < columnCount; i++) {
					columnLeft += portal._portletPadding;
					leftMap.push(columnLeft);
					$fly(columnDoms[i]).css({ left: columnLeft, top: portal._portletPadding });
					columnLeft += widthMap[i];
				}

				this._columnLeftMap = leftMap;
				this._columnWidthMap = widthMap;

				return widthMap;
			}

		},

		ifScrollChange: function() {
			var portal = this, dom = portal._dom, hasScroll = jQuery.ui.hasScroll(dom, "top");

			//console.log("hasScroll:" + hasScroll + "\tportal._hasScroll:" + portal._hasScroll);

			if (hasScroll == portal._hasScroll) {
				return;
			}

			portal._hasScroll = hasScroll;
			portal.onScrollChange();
		},

		onScrollChange: function() {
			var portal = this;
			portal.calcColumnsWidth();
			portal.positionPortlets();
		},

		/**
		 * 当拖拽的时候显示ColumnDoms，显示蓝色背景，以及充当droppable的容器。
		 * @private
		 */
		showColumns: function() {
			var portal = this, columns = portal._columns, dom = portal._dom, columnDoms = portal._columnDoms,
				widthMap = portal._columnWidthMap, height = $fly(dom).attr("scrollHeight") - portal._portletPadding;

			for (var i = 0; i < columns.size; i++) {
				var columnDom = columnDoms[i], columnWidth = widthMap[i];
				$fly(columnDom).width(columnWidth).outerHeight(height);
			}
		},

		/**
		 * 拖拽完成以后隐藏ColumnDoms。
		 * @private
		 */
		hideColumns: function() {
			var portal = this, columns = portal._columns, columnDoms = portal._columnDoms;
			for (var i = 0; i < columns.size; i++) {
				var columnDom = columnDoms[i];
				$fly(columnDom).height(0);
			}
		},

        doOnResize: function() {
            this.refresh();
        },

		/**
		 * 定位所有的Portlets。
		 * @private
		 */
		positionPortlets: function() {
			var portal = this, columns = portal._columns, dom = portal._dom, portlets = portal._columnPortlets || [],
				width = $fly(dom).width(), columnCount = columns.size,
				widthMap = portal._columnWidthMap, leftMap = portal._columnLeftMap;

			if (!widthMap) {
				widthMap = portal.calcColumnsWidth();
				leftMap = portal._columnLeftMap;
			}

			var draggingPortlet = portal._draggingPortlet, dragging = (draggingPortlet != null);

			for (var j = 0; j < columnCount; j++) {
				var columnWidth = widthMap[j], paddingTop = portal._portletPadding, columnPortlets = portlets[j];
				if (columnPortlets) {
					var portletTop = 0;
					for (var k = 0, l = columnPortlets.length; k < l; k++) {
						var temp = columnPortlets[k], tempDom = temp._dom;
						if (dragging) {
							if (temp == draggingPortlet) {
								continue;
							}
							if (portal._placeholderColumn === j && portal._placeholderRow === k) {
								portletTop += $fly(draggingPortlet._dom).outerHeight() + portal._portletPadding;
							}
						}

						portletTop += paddingTop;

						temp.set("width", columnWidth);
						temp.resetDimension();
						temp.refresh();

						tempDom.style.left = leftMap[j] + "px";
						tempDom.style.top = portletTop + "px";

						portletTop += $fly(tempDom).outerHeight();
					}
				}
			}
		},

		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);

			var portal = this, columns = portal._columns;
			if (columns) {
                portal.calcColumnsWidth();
                portal.positionPortlets();
			}
		}
	});
})();
