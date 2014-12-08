/**
 * Created with JetBrains WebStorm.
 * User: Alex Tong(mailto:alex.tong@bstek.com)
 * Date: 13-12-19
 * Time: 下午12:22
 * To change this template use File | Settings | File Templates.
 */
(function () {
	window.$cloudo = window.parent.cloudo;
	var DomFinder = {
		_ALL: {
			"SplitPanel/Wrapper.SideControl": function (parent) {
				return parent._doms && parent._doms.sidePanel;
			},
			"SplitPanel/Wrapper.MainControl": function (parent) {
				return parent._doms && parent._doms.mainPanel;
			},
			"Panel/Wrapper.Buttons": function (parent) {
				return   parent._doms && parent._doms.buttonPanel;
			},
			"Panel/Wrapper.Children": function (parent) {
				return parent.getContentContainer();
			},
			"Panel/Wrapper.Tools": function (parent) {
				var captionBar = parent._captionBar;
				if (captionBar) {
					return captionBar.getDom();
				}
			},
			"Dialog/Wrapper.Buttons": function (parent) {
				return   parent._doms && parent._doms.buttonPanel;
			},
			"Dialog/Wrapper.Children": function (parent) {
				return parent.getContentContainer();
			},
			"Dialog/Wrapper.Tools": function (parent) {
				var captionBar = parent._captionBar;
				if (captionBar) {
					return captionBar.getDom();
				}
			},
			"GroupBox/Wrapper.Buttons": function (parent) {
				return   parent._doms && parent._doms.buttonPanel;
			},
			"GroupBox/Wrapper.Children": function (parent) {
				return parent.getContentContainer();
			},
			"FieldSet/Wrapper.Buttons": function (parent) {
				return   parent._doms && parent._doms.buttonPanel;
			},
			"FieldSet/Wrapper.Children": function (parent) {
				return parent.getContentContainer();
			},
			"TouchSplitView/Wrapper.SideControl": function (parent) {
				return  parent._doms && parent._doms.sidePanel;
			},
			"TouchSplitView/Wrapper.MainControl": function (parent) {
				return parent._doms && parent._doms.mainPanel;
			}, "TouchPanel/Wrapper.Buttons": function (parent) {
				return   parent._doms && parent._doms.buttonPanel;
			},
			"TouchPanel/Wrapper.Children": function (parent) {
				return parent.getContentContainer();
			},
			"TouchPanel/Wrapper.Tools": function (parent) {
				return parent._captionBar;
			}
		},
		register: function (rid, fun) {
			this._ALL[rid] = fun;
		},
		getDom: function (parentControl, rid) {
			var finder = this._ALL[rid], dom;
			if (finder) {
				dom = finder(parentControl);
			}
			return dom || parentControl.getDom();
		}
	};
	DomFinder.register("SplitPanel/Wrapper.SideControl", function (parentControl) {

	});
	var
		CH = {
			removeNode: function (id) {
				$cloudo.viewConfig.removeNode(id);
			},
			pushCurrent: function (ids) {
				$cloudo.selections.add(ids, {merge: false});
			},
			setPosition: function (nid, position) {
				delete position["relativePosition"];
				$cloudo.setPosition(nid, position);
			},
			setPositionProperty: function (nid, name, value) {
				$cloudo.setPositionProperty(nid, name, value);
			},
			KEY_PROPERTY: "$cloudoID"
		},

		DH = {
			getDom: function (nid) {
				var dom, parentControl = ControlMap.findParentControl(nid);

				if (parentControl && parentControl[CH.KEY_PROPERTY] != nid) {
					var nodeMeta = $cloudo.NodeHelper.meta(nid);
					if (nodeMeta && nodeMeta.parentMeta) {
						//TODO 往下为对固定子节点DOM的特殊处理(以后可在此处添加特殊路径)
						var className = parentControl.constructor.className;

						dom = DomFinder.getDom(parentControl, nodeMeta.rid);
					}
				} else if (parentControl && parentControl.getDom) {
					dom = parentControl.getDom();
				}

				return  dom;
			},
			getPreviousDom: function (control, parentControl) {
				var dom, layout = parentControl.get("layout"), region = layout.getRegion(control);
				if (region) {
					var previousRegion = layout.getPreviousRegion(region);
					if (previousRegion) {
						dom = previousRegion.control.getDom();
					}
				}
				return dom;
			},
			getLastRegionDom: function (control) {
				var dom, layout = control.get("layout"), _regions = layout._regions, lastRegion;
				if (_regions.getLast) {
					lastRegion = _regions.getLast();
				} else {
					if (_regions.size > 0) {
						lastRegion = _regions.items[_regions.size - 1];
					}
				}
				if (lastRegion) {
					var control = lastRegion.control;
					dom = control.getDom();
				}
				return dom;
			}
		};


	var DrawPad = {
		_INDICATOR: {
			width: 24, height: 24,
			borderWidth: 2
		},
		_getDomDimension: function (dom) {
			if (!dom) return;

			var $dom = dom instanceof jQuery ? dom : $(dom),
				offset = $dom.offset();

			return   {
				top: offset.top,
				left: offset.left,
				width: $dom.outerWidth(),
				height: $dom.outerHeight()
			};
		},

		_isIn: function (x, y, region) {
			var width = region.width, height = region.height,
				top = region.top, left = region.left;
			return top <= y && y <= top + height && left <= x && x <= left + width;
		},


		_setCurrent: function (nid) {
			var settings = $cloudo.Settings;
			this._current = {
				nid: nid,
				control: ControlMap.findParentControl(nid)
			};
			if (settings.runMode === settings.RUN_MODE_EDIT) {
				this._repositionTargetIndicator(DH.getDom(nid));
			} else {
				this.clearTraces();
				var indicator = this._targetIndicator;
				$(indicator).remove();
				delete this["_targetIndicator"];
				if (indicator && indicator._actualVisible) {
					$(indicator).addClass("c-invisible");
					indicator._actualVisible = false;
				}
			}
		},

		_makeSelector: function () {
			this._selectable = true;
			var context = {}, layerDom = $DomUtils.xCreate([
				{
					tagName: "DIV", className: "c-selector",
					content: [
						{
							tagName: "DIV", className: "content"
						}
					]
				}
			], null, context);

			return layerDom;
		},

		_makeTargetIndicator: function () {
			var context = {}, indicatorDom = $DomUtils.xCreate(
				{
					tagName: "DIV", className: "c-indicator",
					content: [
						{
							tagName: "DIV", className: "content",
							content: [
								{
									tagName: "DIV", className: "selector c-invisible"
								}
							]
						}
					]
				}, null, context), $indicatorDom = $(indicatorDom);
			indicatorDom.hide = function () {
				$(indicatorDom).addClass("c-invisible");
			};
			indicatorDom.show = function () {
				$(indicatorDom).removeClass("c-invisible");
			};
			this._selectable = true;
			this._targetIndicator = indicatorDom;

			$("body").append(indicatorDom);
			$indicatorDom.click(function (event) {
				function doClick() {
					var control = DrawPad._findControl(event.pageX, event.pageY), nodeId;
					if (control) {
						var className = control.constructor.className;

						if (className === "dorado.widget.DataGrid") {
							var dom = control.getDom();
							var $headerDoms = $("thead .header-table .header", dom);
							$headerDoms.each(function (i, headerDom) {
								var colId = headerDom.colId,
									column = control._columnsInfo.idMap[colId],
									nid = column[CH.KEY_PROPERTY];
								if (DrawPad._isIn(event.pageX, event.pageY, DrawPad._getDomDimension(headerDom))) {
									nodeId = nid;
									return false;
								}
							});
						}


						nodeId = nodeId || control[CH.KEY_PROPERTY];

						CH.pushCurrent(nodeId);
					}
				}

				if (this._executable) {
					doClick();
				}
				//阻止事件冒泡
				return false;
			});

			$indicatorDom.mousedown(function (event) {
				if (DrawPad._selectable) {
					this._startLocation = {
						x: event.pageX,
						y: event.pageY
					};
				}
			});
			$indicatorDom.mousemove(function (event) {
				var startLocation = this._startLocation;
				if (startLocation && DrawPad._selectable) {
					var x1 = startLocation.x, y1 = startLocation.y,
						x2 = event.pageX, y2 = event.pageY,
						region = DrawPad._getDomDimension(this);
					var left, top, width, height;
					if (x1 > x2) {
						width = x1 - x2;
						left = x2;
					} else {
						width = x2 - x1;
						left = x1;
					}
					if (y1 > y2) {
						height = y1 - y2;
						top = y2;
					} else {
						height = y2 - y1;
						top = y1;
					}
					$(".selector", this).css({
						left: left - region.left,
						top: top - region.top,
						width: width, height: height
					});

					$(".selector", this).toggleClass("c-invisible", false);
				}

			});

			$indicatorDom.mouseup(function (event) {
				if (DrawPad._selectable && this._startLocation) {
					var startLocation = this._startLocation,
						sX = startLocation.x, sY = startLocation.y,
						eX = event.pageX, eY = event.pageY;

					if (sX > eX) {
						var tX = eX;
						eX = sX;
						sX = tX;
					}

					if (sY > eY) {
						var tY = eY;
						eY = sY;
						sY = tY;
					}

					var controls = $(".c-target"), nodeIdList = [];
					for (var i = controls.length - 1; i >= 0; i--) {
						var e = controls.eq(i),
							region = DrawPad._getDomDimension(e),
							x11 = region.left, y11 = region.top,
							x22 = region.left + region.width,
							y22 = region.top + region.height;

						if (sX < x11 && eX > x22 && sY < y11 && eY > y22) {

							//    var targetControl = dorado.widget.Component.ALL[e[0].doradoUniqueId] || e[0]._doradoTab,
							var targetControl = dorado.widget.findParentControl(e[0]),
								nodeId = targetControl[CH.KEY_PROPERTY];
							nodeId && nodeIdList.push(nodeId);
						}
					}
					delete this["_startLocation"];
					if (nodeIdList.length > 0) {
						CH.pushCurrent(nodeIdList);
						this._executable = false;
					} else {
						this._executable = true;
					}

					$(".selector", this).toggleClass("c-invisible", true);
					return false;
				}
			});
		},
		_findControl: function (x, y) {
			var controls = $(".c-target");
			for (var i = controls.length - 1; i >= 0; i--) {
				var e = controls.eq(i), region = DrawPad._getDomDimension(e);
				if (DrawPad._isIn(x, y, region)) {
					//var targetControl = dorado.widget.Component.ALL[e[0].doradoUniqueId];
					var targetControl = dorado.widget.findParentControl(e[0])
					if (!targetControl) {
						targetControl = e[0]._doradoTab;
					}
					return targetControl;
				}
			}
		},

		_makeAnchorLocator: function () {
			var context = {}, anchorLocator = $DomUtils.xCreate({
				tagName: "DIV", className: "c-anchor-locator",
				content: [
					{
						tagName: "DIV", className: "indicator"
					},
					{
						tagName: "DIV", className: "relative-indicator"
					},
					{
						tagName: "DIV", className: "line horizontal line-left",
						content: [
							{
								tagName: "DIV", className: "value value-left"
							}
						]
					},
					{
						tagName: "DIV", className: "line horizontal line-right",
						content: [
							{
								tagName: "DIV", className: "value value-right"
							}
						]
					},
					{
						tagName: "DIV", className: "line vertical line-bottom",
						content: [
							{
								tagName: "DIV", className: "value value-bottom"
							}
						]
					},
					{
						tagName: "DIV", className: "line vertical line-top",
						content: [
							{
								tagName: "DIV", className: "value value-top"
							}
						]
					},
					{
						tagName: "DIV", className: "relative",
						content: [
							{
								tagName: "DIV", className: "value value-relative-top"
							},
							{
								tagName: "DIV", className: "value value-relative-left"
							},
							{
								tagName: "DIV", className: "value value-relative-bottom"
							},
							{
								tagName: "DIV", className: "value value-relative-right"
							}
						]
					}
				]}, null, context);

			$(".content", this._targetIndicator).append(anchorLocator);

			this._anchorLocator = anchorLocator;
		},
		setView: function (view) {
			this._view = view;
		},
		//获得目标锚定位置
		getAnchorPosition: function (mouseX, mouseY, jsPrototype) {
			var indicator = this._targetIndicator;
			if (!indicator._actualVisible) {
				return false;
			}

			var parentControl = this._current.control, proxyControl = parentControl._tempControl;
			if (!proxyControl) {

				proxyControl = new (eval(jsPrototype))();

				parentControl._tempControl = proxyControl;
			}
			if (proxyControl instanceof dorado.widget.Control) {
				var layout = parentControl.get("layout"),
					constraint = layout.preprocessLayoutConstraint(proxyControl._layoutConstraint, proxyControl);
				if (constraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
					return false;
				}
				var padding, relativeDom = DH.getLastRegionDom(parentControl),
					nid = this._current.nid,
					currentNodeMeta = $cloudo.NodeHelper.meta(nid),
					rid = currentNodeMeta.rid;

				if (/\/Wrapper/ig.exec(rid)) {
					padding = $cloudo.NodeHelper.parentPadding(nid)
				} else {
					padding = $cloudo.NodeHelper.padding(nid);
				}

				return this._repositionAnchorLocator({mouse: {x: mouseX, y: mouseY}}, relativeDom, padding);
			} else {
				return false;
			}
		},
		_hideIndicator: function () {
			var indicator = this._targetIndicator;
			indicator && $(indicator).addClass("c-invisible");
		},
		clearTraces: function () {
			//清除目标区域控件
			var regionControl = this._targetRegionControl;
			if (regionControl) {
				try {
					var parentControl = regionControl.get("parent");
					if (parentControl && parentControl instanceof dorado.widget.Container) {
						parentControl.removeChild(regionControl);
					}
					regionControl.destroy();
				} catch (e) {
				}
				delete  this["_targetRegionControl"];
			}
			var targetIndicator = this._targetIndicator;
			if (targetIndicator) {
				targetIndicator.hide();
			}
			//  this._hideIndicator();
			//清除锚定布局辅助
			var anchorLocator = this._anchorLocator;
			if (anchorLocator) {
				$(anchorLocator).remove();
				delete this["_anchorLocator"];
			}

			$(".c-selector").remove();

		},
		_refreshTargetIndicator: function () {
			var targetIndicator = this._targetIndicator;
			if (targetIndicator && targetIndicator._targetDom && targetIndicator._actualVisible) {
				setTimeout(function () {
					DrawPad._repositionTargetIndicator(targetIndicator._targetDom);
				}, 200)
			}


		},
		_repositionTargetIndicator: function (targetDom) {

			if (!this._targetIndicator) {
				this._makeTargetIndicator();
			}
			var actualVisible = true, targetIndicator = this._targetIndicator,
				$targetIndicator = $fly(targetIndicator), currentControl = this._current && this._current.control;

			if (targetDom) {
				targetIndicator._targetDom = targetDom;
				if (currentControl && currentControl.getDom && targetDom == currentControl.getDom()) {
					actualVisible = $(targetDom).css("display") != "none";
				}
			} else {
				actualVisible = false;
			}

			if (actualVisible) {
				var region = DrawPad._getDomDimension(targetDom);
				region.left = region.left - 2;
				region.top = region.top - 2;
				$targetIndicator.css(region);
			}
			$targetIndicator.toggleClass("c-invisible", !actualVisible);
			targetIndicator._actualVisible = actualVisible;
		},

		_repositionAnchorLocator: function (target, relativeDom, padding) {
			if (!this._anchorLocator) {
				this._makeAnchorLocator();
			}
			padding = parseInt(padding);

			var targetIndicator = this._targetIndicator;

			function createValueDom(value) {
				return '<STRONG>' + value + '</STRONG>';
			}

			var $targetIndicator = $(targetIndicator), indicator = this._INDICATOR,
				$indicatorDom = $targetIndicator.find(".indicator");
			var helperRegion = this._getDomDimension($targetIndicator), helperBorderWidth = 2,
				targetRegion , left, top, right, bottom,
				indicatorWidth = indicator.width, indicatorHeight = indicator.height;
			if (target.dom) {
				if (target.dom == $indicatorDom[0]) {
					targetRegion = this._getDomDimension(target.dom);
					left = targetRegion.left - (helperRegion.left + helperBorderWidth) + indicator.borderWidth;
					top = targetRegion.top - (helperRegion.top + helperBorderWidth) + indicator.borderWidth;
					right = helperRegion.width - (left + targetRegion.width);
					bottom = helperRegion.height - (top + targetRegion.height);
					indicatorWidth = targetRegion.width - helperBorderWidth * 2;
					indicatorHeight = targetRegion.height - helperBorderWidth * 2;
				} else {
					targetRegion = this._getDomDimension(target.dom);
					left = targetRegion.left - helperRegion.left - helperBorderWidth;
					top = targetRegion.top - helperRegion.top - helperBorderWidth;
					right = helperRegion.width - (left + targetRegion.width + helperBorderWidth * 2);
					bottom = helperRegion.height - (top + targetRegion.height + helperBorderWidth * 2);
					indicatorWidth = targetRegion.width;
					indicatorHeight = targetRegion.height;
				}
			} else {
				var mouse = target.mouse;
				var left = mouse.x - helperRegion.left - helperBorderWidth - indicator.width - indicator.borderWidth ,
					top = mouse.y - helperRegion.top - helperBorderWidth - indicator.height - indicator.borderWidth;
				var right = helperRegion.width - (left + indicator.width + helperBorderWidth * 2);
				bottom = helperRegion.height - (top + indicator.height + helperBorderWidth * 2);
			}

			left = left - padding;
			right = right - padding;
			top = top - padding;
			bottom = bottom - padding;
			$(this._anchorLocator).css({left: padding,
				right: padding,
				top: padding,
				bottom: padding});

			$indicatorDom.css({
				left: left - helperBorderWidth, top: top - helperBorderWidth,
				width: indicatorWidth, height: indicatorHeight
			});

			//线位置确定
			var $lineLeft = $(".line-left", $targetIndicator), $lineTop = $(".line-top", $targetIndicator),
				$lineRight = $(".line-right", $targetIndicator), $lineBottom = $(".line-bottom", $targetIndicator);

			$lineLeft.css({
				width: left, top: top + indicatorHeight / 2
			});
			$lineTop.css({
				height: top, left: left + indicatorWidth / 2
			});
			$lineRight.css({
				right: 0, width: right - indicator.borderWidth,
				top: top + indicatorHeight / 2
			});
			$lineBottom.css({
				bottom: 0, height: bottom - indicator.borderWidth,
				left: left + indicatorWidth / 2
			});
			//值位置确定
			var $leftValue = $(".value-left", $targetIndicator), $topValue = $(".value-top", $targetIndicator),
				$rightValue = $(".value-right", $targetIndicator), $bottomValue = $(".value-bottom", $targetIndicator),
				$relativeTopValue = $(".value-relative-top", $targetIndicator), $relativeLeftValue = $(".value-relative-left", $targetIndicator),
				$relativeBottomValue = $(".value-relative-bottom", $targetIndicator), $relativeRightValue = $(".value-relative-right", $targetIndicator);


			$([$leftValue, $topValue, $rightValue, $bottomValue,
				$relativeTopValue, $relativeLeftValue, $relativeBottomValue, $relativeRightValue]).empty();
			var critical = 20, offset = -22;
			$leftValue.css({left: left / 2 > critical ? left / 2 : offset });
			$leftValue.html(createValueDom(left));

			$topValue.css({ top: top / 2 > critical ? top / 2 : offset});
			$topValue.html(createValueDom(top));

			$rightValue.css({ right: right / 2 > critical ? right / 2 : offset});
			$rightValue.html(createValueDom(right));

			$bottomValue.css({ bottom: bottom / 2 > critical ? bottom / 2 : offset});
			$bottomValue.html(createValueDom(bottom));

			$lineLeft.toggleClass("dotted", left >= right);
			$lineTop.toggleClass("dotted", top >= bottom);

			$lineRight.toggleClass("dotted", !(left >= right));
			$lineBottom.toggleClass("dotted", !(top >= bottom));


			$(".relative", targetIndicator).toggleClass("c-invisible", !relativeDom);
			$(".relative-indicator", targetIndicator).toggleClass("c-invisible", !relativeDom);
			var relativeLeft, relativeTop, relativeBottom, relativeRight;
			if (relativeDom) {
				var width, height,
					relativeDimension = this._getDomDimension(relativeDom),
					$relative = $(".relative", targetIndicator);

				var mouseX = left + indicatorWidth + indicator.borderWidth + helperRegion.left,
					mouseY = top + indicatorHeight + indicator.borderWidth + helperRegion.top;

				if (relativeDimension.left + relativeDimension.width / 2 < mouseX) {
					width = left - (relativeDimension.left + relativeDimension.width - helperRegion.left - helperBorderWidth - padding);
					$relative.css({
						right: right + indicatorWidth,
						left: "initial"
					});
					relativeLeft = width;
				} else {
					width = (relativeDimension.left - (helperRegion.left + helperBorderWidth) - padding) - (left + indicatorWidth);
					$relative.css({
						left: left + indicatorWidth,
						right: "initial"
					});
					relativeRight = width;
				}
				if (relativeDimension.top + relativeDimension.height / 2 < mouseY) {
					height = top - (relativeDimension.top + relativeDimension.height - helperRegion.top - helperBorderWidth - padding);
					$relative.css({
						bottom: bottom + indicatorHeight,
						top: "initial"
					});
					relativeTop = height;
				} else {
					height = (relativeDimension.top - helperRegion.top - helperBorderWidth - padding) - (top + indicatorHeight  );
					$relative.css({
						bottom: "initial",
						top: top + indicatorHeight
					});
					relativeBottom = height;
				}

				var relativeLeftUndefined = typeof relativeLeft == "undefined",
					relativeTopUndefined = typeof relativeTop == "undefined",
					relativeRightUndefined = typeof relativeRight == "undefined",
					relativeBottomUndefined = typeof relativeBottom == "undefined";
				$relativeBottomValue.toggleClass("value-show-right", relativeLeftUndefined);
				$relativeTopValue.toggleClass("value-show-right", relativeLeftUndefined);

				$relativeRightValue.toggleClass("value-show-bottom", relativeTopUndefined);
				$relativeLeftValue.toggleClass("value-show-bottom", relativeTopUndefined);

				$relativeBottomValue.toggleClass("c-invisible", relativeBottomUndefined || relativeBottom <= 0);
				$relativeTopValue.toggleClass("c-invisible", relativeTopUndefined || relativeTop <= 0);
				$relativeRightValue.toggleClass("c-invisible", relativeRightUndefined || relativeRight <= 0);
				$relativeLeftValue.toggleClass("c-invisible", relativeLeftUndefined || relativeLeft <= 0);

				if (relativeTopUndefined) {
					if (relativeBottom > 0) $relativeBottomValue.html(createValueDom(relativeBottom));
					$relativeBottomValue.css({ top: height / 2 + offset / 2});
				} else {
					if (relativeTop > 0)  $relativeTopValue.html(createValueDom(relativeTop));
					$relativeTopValue.css({ top: height / 2 + offset / 2  });
				}


				if (relativeLeftUndefined) {
					if (relativeRight > 0) $relativeRightValue.html(createValueDom(relativeRight));
					$relativeRightValue.css({left: width / 2});
				} else {
					if (relativeLeft > 0)  $relativeLeftValue.html(createValueDom(relativeLeft));
					$relativeLeftValue.css({left: width / 2 });
				}

				$relative.css({
					"border-bottom-width": height > 0 ? 1 : 0,
					"border-left-width": width > 0 ? 1 : 0,
					width: width, height: height
				});
				$(".relative-indicator", targetIndicator).css(relativeDimension).css({
					left: relativeDimension.left - helperRegion.left - 4 - padding,
					top: relativeDimension.top - helperRegion.top - 4 - padding
				});
			}
			this._anchorLocator._targetPosition = {
				left: left, right: right,
				top: top, bottom: bottom,
				relativePosition: {
					left: relativeLeft, right: relativeRight,
					top: relativeTop, bottom: relativeBottom
				}
			}
			return   this._anchorLocator._targetPosition;
		},
		isValidHoverTarget: function (mouseX, mouseY) {
			var targetIndicator = this._targetIndicator;
			if (!targetIndicator._actualVisible) {
				return false;
			}
			var dimension = this._getDomDimension(targetIndicator);
			return this._isIn(mouseX, mouseY, dimension);
		},
		drawTargetRegion: function () {
			if (!this._targetRegionControl) {
				var currentNodeMeta = $cloudo.NodeHelper.meta(this._current.nid);
				var currentNodeRule = currentNodeMeta.rid;
				var currentControl = this._current.control;

				var regionControl, targetContainer = currentControl;
				if (targetContainer instanceof dorado.widget.AutoForm || targetContainer instanceof dorado.widget.Container) {
					if (/\/Wrapper/ig.exec(currentNodeRule) && !/.*Children/.exec(currentNodeRule)) {
						if (/.*Buttons/.exec(currentNodeRule)) {
							regionControl = new dorado.widget.Button({
								icon: "url(>skin>common/icons.gif) -280px -280px"
							});
							var buttons = targetContainer.get("buttons") || [];
							buttons.push(regionControl);
							targetContainer.set("buttons", buttons);
						}

						if (/.*Tools/.exec(currentNodeRule)) {
							regionControl = new dorado.widget.SimpleButton({
								icon: "url(>skin>common/icons.gif) -280px -280px"
							});
							if (targetContainer) {
								var tools = targetContainer.get("tools");
								if (tools && tools instanceof dorado.util.KeyedArray) {
									tools = tools.toArray();
								}
								tools = tools || [];
								tools.push(regionControl);
								targetContainer.set("tools", tools);
							}
						}
					} else {
						var layout = $cloudo.NodeHelper.layoutRuleId(currentNodeMeta.nid);
						if (targetContainer instanceof dorado.widget.AutoForm || layout !== "DockLayout") {
							regionControl = new dorado.widget.Control();
							regionControl.set("style", {
								border: "1px solid #69bcff"
							});
							if (targetContainer instanceof dorado.widget.AutoForm || layout == "FormLayout" || layout == "VboxLayout") {
								regionControl.set("height", 20);
							} else if (layout == "HboxLayout") {
								regionControl.set("width", 20);
							} else if (layout == "NativeLayout") {
								regionControl.set({width: "20",
									height: 20});
							}

							if (targetContainer instanceof dorado.widget.AutoForm) {
								targetContainer.addElement(regionControl);
							} else {
								targetContainer.addChild(regionControl);
							}
						} else {
							regionControl = new dorado.widget.Container({
								children: [
									{
										$type: "Control", height: "20%", userData: "top",
										exClassName: "c-sub-region",
										layoutConstraint: { type: "top"}
									},
									{
										$type: "Control", width: "20%", userData: "left",
										exClassName: "c-sub-region",
										layoutConstraint: {  type: "left"}
									},
									{
										$type: "Control", userData: "center",
										exClassName: "c-sub-region",
										layoutConstraint: { type: "center" }
									},
									{
										$type: "Control", width: "20%", userData: "right",
										exClassName: "c-sub-region",
										layoutConstraint: { type: "right" }
									},
									{
										$type: "Control", height: "20%", userData: "bottom",
										exClassName: "c-sub-region",
										layoutConstraint: {  type: "bottom"  }
									}
								],
								exClassName: "c-region",
								layout: {
									$type: "Dock", regionPadding: "1"
								}
							});

							targetContainer.addChild(regionControl);
						}
					}
					this._targetRegionControl = regionControl;
				}
			}
		},
		//获得目标Dock布局类型
		getDockType: function (mouseX, mouseY) {
			var regionControl = this._targetRegionControl, dockType = "left";
			if (regionControl) {
				var children = regionControl.get("children"),
					it = children.iterator();
				while (it.hasNext()) {
					var control = it.next(), dom = control.getDom(),
						dimension = DrawPad._getDomDimension(dom),
						isIn = DrawPad._isIn(mouseX, mouseY, dimension);
					if (isIn) {
						dockType = control.get("userData");
					}
					$(dom).toggleClass("c-region-highlight", isIn);
				}
			}
			return dockType;
		},
		refreshSelections: function (selections) {
			delete this["_current"];
			var settings = $cloudo.Settings;
			this.clearTraces();

			if (settings.runMode === settings.RUN_MODE_EDIT) {
				if (selections.length > 1) {

					for (var i = 0; selections.length > 1 && i < selections.length; i++) {
						var nid = selections[i],
							dom = DH.getDom(nid);
						if (dom) {
							var selector = this._makeSelector(),
								dimension = this._getDomDimension(dom);
							dimension.left -= 2;
							dimension.top -= 2;
							$("body").append(selector);
							$(selector).css(dimension);
						}
					}
				}

				if (selections.length == 1) {
					this._setCurrent(selections[0]);
				}
			}
		},
		setProperty: function (nid, name, value) {
			var control = ControlMap.find(nid), returnValue = false;
			if (!(value instanceof Object) && control) {
				try {
					var attr = control.ATTRIBUTES[name];
					if (!attr.writeBeforeReady && !attr.readOnly) {
						control.set(name, value);
						this._refreshTargetIndicator();
						returnValue = true;
					}
				} catch (e) {

				}
			}
			return returnValue;
		},

		showPositionEditor: function (nid) {
			var targetControl, parentControl, anchorLocator, parentNid,
				targetDom, relativeDom, parentPadding, nodeMeta = $cloudo.NodeHelper.meta(nid);

			targetControl = ControlMap.findParentControl(nid);
			parentNid = nodeMeta.parentMeta.nid;
			parentControl = ControlMap.findParentControl(parentNid);
			relativeDom = DH.getPreviousDom(targetControl, parentControl);
			parentPadding = $cloudo.NodeHelper.parentPadding(nid);

			//将指示器指向控件容器
			this._repositionTargetIndicator(DH.getDom(parentNid));
			//将指示器调整为不可选择模式
			this._selectable = false;

			if (targetControl.getDom) {
				targetDom = targetControl.getDom();
				this._repositionAnchorLocator({dom: targetDom}, relativeDom, parentPadding);
				anchorLocator = this._anchorLocator;
				$(".indicator", anchorLocator).draggable({
					containment: $(".content", this._targetIndicator),
					grid: [4, 4],
					drag: function (event, ui) {
						DrawPad._repositionAnchorLocator({dom: ui.helper[0]}, relativeDom, parentPadding);
					},
					stop: function (event, ui) {
						var position = DrawPad._repositionAnchorLocator({
								dom: ui.helper[0]
							},
							relativeDom, parentPadding);
						CH.setPosition(nid, position)
					}
				});
			}


			var position = $cloudo.NodeHelper.position(nid), context = {};

			function firstToUpperCase(str) {
				var word = str.toLowerCase();
				return   word.substring(0, 1).toUpperCase() + word.substring(1);
			}

			function buildCheckBox(selector, value) {
				var checkBox = new dorado.widget.CheckBox({
					userData: {
						value: value, type: selector
					},
					onClick: function (self, arg) {
						//阻止事件冒泡
						arg.returnValue = false;
					},
					onValueChange: function (self) {
						var userData = self.get("userData"),
							type = userData.type, value = userData.value,
							k = type.replace(/.value-/g, "").replace(/relative-/g, "");
						if (self.get("value")) {
							position[k] = value;
							if (/\-relative/ig.exec(type)) {
								position["anchor" + firstToUpperCase(k)] = "previous";
							} else {
								delete position["anchor" + firstToUpperCase(k)];
							}
						} else {
							delete position["anchor" + firstToUpperCase(k)];
							delete position[k];
						}
						position.operation = "set";
						CH.setPosition(nid, position)


					}
				});

				context[selector] = checkBox;
				var $lineDom = $(selector, anchorLocator);
				$lineDom.css({width: $lineDom.find("strong").width() + 18});
				checkBox.render($lineDom[0]);
			}


			var targetPosition = anchorLocator._targetPosition;
			jQuery.each(targetPosition, function (key) {
				if (key != "relativePosition") {
					buildCheckBox(".value-" + key, targetPosition[key]);
				}
			})

			var relativePosition = targetPosition.relativePosition;
			relativePosition && jQuery.each(relativePosition, function (key, value) {
				if (typeof value !== "undefined") {
					buildCheckBox(".value-relative-" + key, value);
				}
			});

			var constraint = ["left", "right", "top", "bottom"];
			constraint.each(function (type) {
				var anchor = "anchor" + firstToUpperCase(type);
				if (position[anchor] && position[anchor] == "previous") {
					context[".value-relative-" + type].set("value", true);
				} else if (typeof position[type] !== "undefined") {
					context[".value-" + type].set("value", true);
				}
			});
		},
		repositionControl: function (nid) {
			var position = $cloudo.NodeHelper.position(nid),
				control = ControlMap.findParentControl(nid);

			if (control && control[CH.KEY_PROPERTY] == nid) {
				control.set({layoutConstraint: position});
			}

			control.get("parent").refresh();
			setTimeout(function () {
				DrawPad.showPositionEditor(nid);
			}, 200);
		},
		pushGridProfile: function (nid) {
			var buildDataGrid = function (grid) {
				var list = new Array(), columns = grid.get("columns");
				var iterator = columns.iterator()
				while (iterator.hasNext()) {
					var column = iterator.next();
					if (column instanceof dorado.widget.grid.DataColumn) {
						list.push(buildDataColumnEntity(column));
					} else if (column instanceof dorado.widget.grid.ColumnGroup) {
						list.push(buildGroupColumnEntity(column));
					}
				}
				return  {
					fixedColumnCount: grid.get("fixedColumnCount"),
					columns: list
				};
			}, buildDataColumnEntity = function (column) {
				var entity = {
					name: column.get("name"),
					caption: column.get("caption"),
					visible: column.get("visible")
				};
				if (column instanceof dorado.widget.grid.IndicatorColumn) {
					entity.type = "IndicatorColumn";
				} else if (column instanceof dorado.widget.grid.RowNumColumn) {
					entity.type = "RowNumColumn";
				} else if (column instanceof dorado.widget.grid.RowSelectorColumn) {
					entity.type = "RowSelectorColumn";
				} else {
					entity.type = "DataColumn";
					entity.width = column._realWidth;
				}
				return entity;
			}, buildGroupColumnEntity = function (columnGroup) {
				var columns = columnGroup.get("columns");
				var list = [];
				var iterator = columns.iterator();
				while (iterator.hasNext()) {
					var column = iterator.next();
					if (column instanceof dorado.widget.grid.ColumnGroup) {
						list.push(buildGroupColumnEntity(column));
					} else {
						list.push(buildDataColumnEntity(column));
					}
				}
				return {
					name: columnGroup.get("name"),
					type: "ColumnGroup",
					caption: columnGroup.get("caption"),
					visible: columnGroup.get("visible"),
					width: columnGroup._realWidth,
					columns: list
				};
			};

			var grid = ControlMap.findParentControl(nid);
			return buildDataGrid(grid);
		}
	}
	$(document).keydown(function (event) {
		var processDefault = true;
		//Delete键操作绑定
		if (event.keyCode === 8 && DrawPad._current) {
			var nodeId = DrawPad._current.nid;
			nodeId && CH.removeNode(nodeId);
			return false;
		}

		if (processDefault && event.ctrlKey) {
			switch (event.keyCode) {
				case 83:  // Ctrl+S
					$cloudo.portal.save();
					processDefault = false;
					break;
				case 90:    // Ctrl+Z
					$cloudo.commandExecutor.undo();
					processDefault = false;
					break;
				case 89:     // Ctrl+Y
					$cloudo.commandExecutor.redo();
					processDefault = false;
					break;
				default :
					processDefault = true;
					break;
			}
		}
		return processDefault;
	});

	window.$CloudoHelper = CH;
	window.$DrawPad = DrawPad;

})
();