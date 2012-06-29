(function(){
	var handleConfigMap = {
		"dialog-header-left": { cursor: "nw-resize", horiStyle: "left width", vertStyle: "top height", widthRatio: -1, heightRatio: -1},
		"dialog-header-right": { cursor: "ne-resize", horiStyle: "width", vertStyle: "top height", widthRatio: 1, heightRatio: -1 },
		"dialog-header-center": { cursor: "n-resize", horiStyle: "", vertStyle: "top height", widthRatio: 1, heightRatio: -1},
		"dialog-body-left": { cursor: "w-resize", horiStyle: "left width", vertStyle: "", widthRatio: -1, heightRatio: 1 },
		"dialog-body-right": { cursor: "e-resize", horiStyle: "width", vertStyle: "", widthRatio: 1, heightRatio: 1 },
		"dialog-footer-left": { cursor: "sw-resize", horiStyle: "left width", vertStyle: "height", widthRatio: -1, heightRatio: 1 },
		"dialog-footer-right": { cursor: "se-resize", horiStyle: "width", vertStyle: "height", widthRatio: 1, heightRatio: 1 },
		"dialog-footer-center": { cursor: "s-resize", horiStyle: "", vertStyle: "height", widthRatio: 1, heightRatio: 1 }
	};

	var fakeDialog;

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Base
	 * @class 对话框
	 * <p>
	 * 默认提供了最大化、最小化、标题拖动、拖拽大小的功能，以及有自己的皮肤。<br />
	 * 如果不需要这些功能，可以考虑使用FloatPanel实现。
	 * </p>
	 * @extends dorado.widget.FloatPanel
	 */
	dorado.widget.Dialog = $extend(dorado.widget.FloatPanel, /** @scope dorado.widget.Dialog.prototype */ {
		$className: "dorado.widget.Dialog",
        _inherentClassName: "i-dialog",

		ATTRIBUTES: /** @scope dorado.widget.Dialog.prototype */ {
			className: {
				defaultValue: "d-dialog"
			},

			/**
			 * 对话框的最小宽度，建议不要小于200。
			 * @attribute
			 * @default 200
			 * @type int
			 */
			minWidth: {
				defaultValue: 200
			},

			/**
			 * 对话框的最小高度，建议不要小于100。
			 * @attribute
			 * @default 100
			 * @type int
			 */
			minHeight: {
				defaultValue: 100
			},

			/**
			 * 对话框是否可拖拽。
			 * @attribute
			 * @default true
			 * @type boolean
			 */
			draggable: {
				defaultValue: true
			},

			/**
			 * 对话框是否可以拖出window。
			 * @attribute writeBeforeReady
			 * @type boolean
			 */
			dragOutside: {
				defaultValue: false
			},

			/**
			 * 对话框是否可以改变尺寸。
			 * @attribute
			 * @default true
			 * @type boolean
			 */
			resizeable: {
				defaultValue: true
			},

			/**
			 * 默认为window对象，可以是dorado的组件、dom对象、dom对象的jQuery选择符。
			 * @attribute
			 * @type String|Object|dorado.RenderableElement
			 */
			maximizeTarget: {},

			/**
			 * 是否显示最小化按钮.
			 * @attribute
			 * @default false
			 * @type boolean
			 */
			minimizeable: {
				defaultValue: false,
				setter: function(value) {
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
				}
			},

			/**
			 * 对话框是否已经最小化。
			 * @attribute
			 * @default false
			 * @type boolean
			 */
			minimized: {},

            closeable: {
			    defaultValue: true
            },

			shadowMode: {
				defaultValue: "frame",
				skipRefresh: true
			},

            animateType: {
                defaultValue: dorado.Browser.msie ? "none" : "zoom"
            }
		},

		EVENTS: /** @scope dorado.widget.Dialog.prototype */ {
			/**
			 * 在对话框最大化之前触发此事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeMaximize: {},

			/**
			 * 在对话框最大化之后触发此事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onMaximize: {},

			/**
			 * 在对话框最小化之前触发此事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeMinimize: {},

			/**
			 * 在对话框最小化之后触发此事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onMinimize: {}
		},

		setFocus: function() {
			var dialog = this;
			if (dialog._rendered) {
				//下面这句话有可能会导致IE8下无法bringToFront
				dialog._dom.focus();
			}
		},
		
		applyDraggable: function() {
			// do nothing
		},

        doOnAttachToDocument: function() {
            //TODO 暂时没考虑清楚。
//            if (this._minimizeable && this._minimized) {
//                this.minimize();
//            }
            $invokeSuper.call(this, arguments);
        },

		maximizeRestore: function() {
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

					var captionBar = dialog._captionBar;
					if (captionBar) {
						var button = captionBar.getButton(dialog._id + "_maximize");
						if (button) {
							$fly(button._dom).prop("className", "d-maximize-button");
							button._className = "d-maximize-button";
						}
					}

					var $dom = jQuery(dom);
					if (dialog._resizeable) {
						$dom.addClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
					}

					if (dialog._draggable) {
						$dom.addClass("i-dialog-resizeable d-dialog-draggable").draggable("enable");
					}
				}
			}
		},

		maximize: function() {
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
				} else if (maximizeTarget && dorado.Object.isInstanceOf(maximizeTarget, dorado.RenderableElement)) {
					maximizeTarget = maximizeTarget._dom;
				}
				if (maximizeTarget) {
					dialog._width = $fly(maximizeTarget).outerWidth(true);
					dialog._height = $fly(maximizeTarget).outerHeight(true);
				} else {
					dialog._width = $fly(window).width();
					dialog._height = $fly(window).height();
				}

				var captionBar = dialog._captionBar;
				if (captionBar) {
					var button = captionBar.getButton(dialog._id + "_maximize");
					if (button) {
						$fly(button._dom).prop("className", "d-restore-button");
						button._className = "d-restore-button";
					}
				}

				dialog.resetDimension();

				var targetOffset = $fly(maximizeTarget).offset() || {left: 0, top: 0};

				dialog._left = targetOffset.left;
				dialog._top = targetOffset.top;

				var domEl = jQuery(dom);
				domEl.css(targetOffset);

				if (dialog._resizeable) {
					domEl.removeClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
				}

				if (dialog._draggable) {
					domEl.removeClass("i-dialog-resizeable d-dialog-draggable").draggable("disable");
				}

				dialog.fireEvent("onMaximize", dialog);
			}
		},

		minimize: function() {
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
		},

		doSetCollapsed: function(collapsed) {
			$invokeSuper.call(this, arguments);
			var dialog = this;
			if (dialog._resizeable) {
				if (collapsed) {
					jQuery(dialog._dom).removeClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("disable");
				} else {
					jQuery(dialog._dom).addClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").draggable("enable");
				}
			}
		},

		_doOnResize: function(collapsed) {
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
					height = $fly(window).height() * parseInt(height.replace("%", ""), 10) / 100;
				}
			}

			if (typeof height == "number" && height > 0) {
				if (collapsed === undefined) {
					collapsed = dialog._collapsed;
				}
				if (collapsed) {
					$fly(dom).height("auto");
				} else {
					var headerHeight = $fly(doms.header).outerHeight(true), footerHeight = $fly(doms.footer).outerHeight(true),
						captionBarHeight = 0, buttonPanelHeight = 0;

                    if (doms.captionBar) {
                        captionBarHeight = $fly(doms.captionBar).outerHeight(true)
                    }

                    if (doms.buttonPanel) {
                        buttonPanelHeight = $fly(doms.buttonPanel).outerHeight(true);
                    }

					$fly(doms.contentPanel).outerHeight(height - headerHeight - footerHeight - captionBarHeight - buttonPanelHeight);
					//$fly(doms.bodyWrap).outerHeight(height - headerHeight - bottomHeight);
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
		},

		_createMinimizeButton: function() {
			var dialog = this, captionBar = dialog._captionBar;
			if (captionBar) {
				captionBar.addButton(new dorado.widget.SimpleButton({
					id: dialog._id + "_minimize",
					className: "d-minimize-button",
					listener: {
						onClick: function() {
							if (!dialog._minimized) {
								dialog.minimize();
							}
						}
					}
				}), 0);
			}
		},

		createDom: function() {
			var dialog = this, doms = {}, dom = $DomUtils.xCreate({
				tagName: "div",
				className: dialog._className,
				style: {
					visibility: dialog._visible ? "visible" : "hidden"
				},
				content: [
					{
						tagName: "div",
						className: "dialog-header",
						contextKey: "header",
						content: [
							{
								tagName: "div",
								className: "dialog-header-left dialog-resize-handle",
								contextKey: "headerLeft"
							},
							{
								tagName: "div",
								className: "dialog-header-right dialog-resize-handle",
								contextKey: "headerRight"
							},
							{
								tagName: "div",
								className: "dialog-header-center dialog-resize-handle",
								contextKey: "headerCenter"
							}
						]
					},
					{
						tagName: "div",
						className: "dialog-body-wrap",
						contextKey: "bodyWrap",
						content: [
							{
								tagName: "div",
								className: "dialog-body-left dialog-resize-handle",
								contextKey: "bodyLeft"
							},
							{
								tagName: "div",
								className: "dialog-body-right dialog-resize-handle",
								contextKey: "bodyRight"
							},
							{
								tagName: "div",
								className: "dialog-body",
								contextKey: "body",
								content: {
									tagName: "div",
									className: "content-panel",
									contextKey: "contentPanel"
								}
							}
						]
					},
					{
						tagName: "div",
						className: "dialog-footer",
						contextKey: "footer",
						content: [
							{
								tagName: "div",
								className: "dialog-footer-left dialog-resize-handle",
								contextKey: "footerLeft"
							},
							{
								tagName: "div",
								className: "dialog-footer-right dialog-resize-handle",
								contextKey: "footerRight"
							},
							{
								tagName: "div",
								className: "dialog-footer-center dialog-resize-handle",
								contextKey: "footerCenter"
							}
						]
					}
				]
			}, null, doms);

			dialog._doms = doms;

			var showCaptionBar = dialog._showCaptionBar;

			if (showCaptionBar !== false) {
				var captionBar = dialog._captionBar = new dorado.widget.CaptionBar({
					className: "d-dialog-caption-bar",
					caption: dialog.get("caption") || dialog._caption,
					icon: dialog._icon
				});
				dialog.registerInnerControl(captionBar);
				captionBar.render(doms.body.parentNode, doms.body);

				doms.captionBar = captionBar._dom;
				$DomUtils.disableUserSelection(doms.captionBar);
			}

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

			if (dialog._draggable && showCaptionBar !== false) {
				jQuery(dom).addClass("d-dialog-draggable").css("position", "absolute").draggable({
					iframeFix: true,
					addClasses: false,
					handle: ".d-dialog-caption-bar",
					cursor: "move",
                    distance: 10,
					containment: dialog._dragOutside ? null : "parent",
					helper: function() {
						if (!fakeDialog) {
							fakeDialog = new dorado.widget.Dialog({ exClassName: "i-dialog-helper d-dialog-helper", visible: true, animateType: "none", shadowMode: "none" });
							fakeDialog.render(document.body);
						}

						return fakeDialog._dom;
					},
					start: function(event, ui) {
                        var height = dialog.getRealHeight();
                        if (height == null) {
                            height = $fly(dom).height();
                        }
						fakeDialog.set({
							width: dom.offsetWidth,
							height: height,
							caption: dialog.get("caption"),
							icon: dialog._icon,
							iconClass: dialog._iconClass,
							minimizeable: dialog._minimizeable,
							maximizeable: dialog._maximizeable,
							closeable: dialog._closeable,
							collapseable: dialog._collapseable
						});

						fakeDialog.refresh();
						var helper = ui.helper;
						helper.css({ display: "", visibility: "" }).bringToFront();
						$fly(dom).addClass("d-dialog-dragging").css("visibility", "hidden").disableShadow();
					},
					stop: function(event, ui) {
						var helper = ui.helper, left = parseInt(helper.css("left"), 10), top = parseInt(helper.css("top"), 10);
						$fly(dom).removeClass("d-dialog-dragging").css({
							visibility: "",
							left: left,
							top: top
						}).enableShadow();
						dialog._left = left;
						dialog._top = top;
					}
				});
			}

			if (dialog._resizeable) {
				var dialogXY, dialogSize;
				jQuery(dom).addClass("i-dialog-resizeable d-dialog-resizeable").find(".dialog-resize-handle").each(function(index, handle) {
					var className = handle.className.split(" ")[0], config = handleConfigMap[className];
					if (!config) return;
					jQuery(handle).draggable({
						iframeFix: true,
						cursor: config.cursor,
						addClasses: false,
						containment: "window",
						helper: function() {
							var proxy = document.createElement("div");
							proxy.className = "i-dialog-drag-proxy d-dialog-drag-proxy";
							proxy.style.position = "absolute";
							$fly(proxy).bringToFront();
							document.body.appendChild(proxy);

							return proxy;
						},

						start: function(event, ui) {
							var $dom = $fly(dom);
							dialogXY = $dom.offset();
							dialogSize = [$dom.width(), $dom.height()];
							jQuery(ui.helper).outerWidth(dialogSize[0]).outerHeight(dialogSize[1]).css("cursor", config.cursor);
						},

						drag: function(event, ui) {
							var horiStyle = config.horiStyle, vertStyle = config.vertStyle, heightRatio = config.heightRatio, widthRatio = config.widthRatio,
								minWidth = dialog._minWidth || 200, minHeight = dialog._minHeight || 100;

							ui.position = {
								left: $fly(dom).offset().left,
								top: $fly(dom).offset().top
							};

							var inst = jQuery.data(this, "draggable"), horiChange = event.pageX - inst.originalPageX,
								vertChange = event.pageY - inst.originalPageY, width, height;

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
						},

						stop: function(event, ui) {
							var wrap = ui.helper, wrapEl = jQuery(wrap), offset = wrapEl.offset();
							dialog._left = offset.left;
							dialog._top = offset.top;
							dialog._width = wrapEl.outerWidth();
							dialog._height = wrapEl.outerHeight();
							dialog.resetDimension();
							$fly(dialog._dom).css(offset);
						}
					});
				});
			}

			return dom;
		},

		getShowPosition: function(options) {
			var panel = this;
			if (panel._maximized) {
				var result = { left: 0, top: 0 };
				$fly(panel._dom).css(result);
				return result;
			} else {
				return $invokeSuper.call(panel, arguments);
			}
		},

		refreshDom: function(dom) {
			var dialog = this;
			
			$invokeSuper.call(dialog, arguments);

			if (!dialog._maximized) {
                if (dialog._left !== undefined && dialog._top !== undefined) {
                    $fly(dom).css({ left: dialog._left, top: dialog._top });
                }
			}
		}
	});

})();
