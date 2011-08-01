(function($) {

	/**
	 * @name jQuery#draggable
	 * @function
	 * @description 此方法是对jQuery UI中draggable方法的增强，请首先参考jQuery UI中draggable方法的使用方法。
	 * @param {Object} options 选项。以下只列出dorado增加额外子参数。
	 * @param {Function|dorado.DraggingInfo} [options.draggingInfo] 拖拽信息或用于获取拖拽信息的Function。<br>
	 * 要使一个可拖拽对象可以被dorado的控件感知和接受，只需要定义了此参数即可。
	 * @param {dorado.Draggable} [options.doradoDraggable] 与此可拖拽元素对应的dorado可拖拽对象。
	 * 如果定义了此参数，那么我们就可以通过{@link dorado.Draggable}支持的各种方法和事件对拖拽操作进行监听和控制。
	 * @return {jQuery} 调用此方法的jQuery对象自身。
	 */
	var oldDraggable = $.fn.draggable;
	$.fn.draggable = function(options) {
		var draggingInfo, doradoDraggable;
		if (options) {
			draggingInfo = options.draggingInfo;
			doradoDraggable = options.doradoDraggable;
		}
		
		if (draggingInfo || doradoDraggable) {
			var originOptions = options;
			options = dorado.Object.apply({}, originOptions);
			
			options.createDraggingInfo = function(evt) {
				var draggingInfo = originOptions.draggingInfo;
				if (draggingInfo instanceof Function) draggingInfo = draggingInfo.call(this, this, options);
				if (!draggingInfo) {
					if (doradoDraggable) draggingInfo = doradoDraggable.createDraggingInfo(this, options);
					if (!draggingInfo) draggingInfo = new dorado.DraggingInfo();
				}
				if (draggingInfo) draggingInfo.set("element", this);
				return draggingInfo;
			};
			
			if (typeof originOptions.revert != "string") {
				options.revert = function(dropped) {
					var revert = originOptions.revert;
					if (revert == null) {
						revert = !dropped;
					} else if (revert instanceof Function) {
						revert = revert.call(this, dropped);
					}
					return revert;
				};
			}
			
			if (typeof originOptions.helper != "string") {
				options.helper = function(evt) {
					var helper;
					if (originOptions.helper instanceof Function) {
						helper = originOptions.helper.apply(this, arguments);
					}
					if (doradoDraggable) helper = doradoDraggable.onGetDraggingIndicator(helper, evt, this);
					
					var draggingInfo = options.createDraggingInfo.call(this, evt);
					$fly(this).data("draggable").draggingInfo = draggingInfo;
					
					if (helper instanceof dorado.DraggingIndicator) {
						draggingInfo.set("indicator", helper);
						helper = helper.getDom();
					}
					return helper;
				};
			}
			
			options.start = function(evt, ui) {
				var b = true;
				if (originOptions.start) b = originOptions.start.apply(this, arguments);
				
				if (b !== false) {
					var draggingInfo = dorado.DraggingInfo.getFromElement(this);
					if (draggingInfo) {
						draggingInfo._targetDroppables = [];
						if (doradoDraggable) {
							b = doradoDraggable.onDragStart(draggingInfo, evt);
							if (b !== false) {
								doradoDraggable.initDraggingInfo(draggingInfo, evt);
								var indicator = draggingInfo.get("indicator");
								if (indicator) doradoDraggable.initDraggingIndicator(indicator, draggingInfo, evt);
							}
						}
					}
				}
				return b;
			};
			
			options.stop = function(evt, ui) {
				var b = true;
				if (originOptions.stop) b = originOptions.stop.apply(this, arguments);
				if (b !== false) {
					var draggingInfo = dorado.DraggingInfo.getFromElement(this);
					if (draggingInfo) {
						if (doradoDraggable) b = doradoDraggable.onDragStop(draggingInfo, evt);
						if (b !== false) {
							setTimeout(function() {
								var targetDroppable = draggingInfo._targetDroppables.peek();
								if (targetDroppable) targetDroppable.onDraggingSourceOut(draggingInfo, evt);
							}, 20);
						}
					}
				}
				return b;
			};
			
			options.drag = function(evt, ui) {
				if (originOptions.drag) originOptions.drag.apply(this, arguments);
				var draggingInfo = dorado.DraggingInfo.getFromElement(this);
				if (draggingInfo) {
					if (doradoDraggable) doradoDraggable.onDragMove(draggingInfo, evt);
					var targetDroppable = draggingInfo._targetDroppables.peek();
					if (targetDroppable) targetDroppable.onDraggingSourceMove(draggingInfo, evt);
				}
			};
		}
		if (!options) {
			//options = { iframeFix: true };
		} else {
			//options.iframeFix = true;
		}
		return oldDraggable.call(this, options);
	};

	/**
	 * @name jQuery#droppable
	 * @function
	 * @description 此方法是对jQuery UI中droppable方法的增强，请首先参考jQuery UI中droppable方法的使用方法。
	 * @param {Object} options 选项。以下只列出dorado增加额外子参数。
	 * @param {dorado.Droppable} [options.doradoDroppable] 与此可拖拽元素对应的dorado可接受拖拽对象。
	 * 如果定义了此参数，那么我们就可以通过{@link dorado.Droppable}支持的各种方法和事件对拖拽操作进行监听和控制。
	 * @return {jQuery} 调用此方法的jQuery对象自身。
	 */
	var oldDroppable = $.fn.droppable;
	$.fn.droppable = function(options) {
		var doradoDroppable = options ? options.doradoDroppable : null;
		if (doradoDroppable) {
			var originOptions = options;
			options = dorado.Object.apply({}, originOptions);
			
			options.over = function(evt, ui) {
				if (originOptions.over) originOptions.over.apply(this, arguments);
				if (doradoDroppable) {
					var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
					if (draggingInfo) {
						if (draggingInfo._targetDroppables.peek() != doradoDroppable) {
							draggingInfo._targetDroppables.push(doradoDroppable);
						}
						doradoDroppable.onDraggingSourceOver(draggingInfo, evt);
					}
				}
			};
			
			options.out = function(evt, ui) {
				if (originOptions.out) originOptions.out.apply(this, arguments);
				if (doradoDroppable) {
					var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
					if (draggingInfo) {
						doradoDroppable.onDraggingSourceOut(draggingInfo, evt);
						if (draggingInfo._targetDroppables.peek() == doradoDroppable) {
							draggingInfo._targetDroppables.pop();
						}
					}
				}
			};
			
			options.drop = function(evt, ui) {
				var draggable = jQuery(ui.draggable).data("draggable");
				if (!jQuery.ui.ddmanager.accept) {
					if (draggable && draggable.options.revert == "invalid") {
						draggable.options.revert = true;
						draggable.options.forceRevert = true;
					}
					return false;
				} else {
					if (draggable && draggable.options.forceRevert) {
						draggable.options.revert = "invalid";
						draggable.options.forceRevert = false;
					}
					var dropped = false;
					if (originOptions.drop) dropped = originOptions.drop.apply(this, arguments);
					if (!dropped && doradoDroppable) {
						var draggingInfo = dorado.DraggingInfo.getFromJQueryUI(ui);
						if (draggingInfo) {
							setTimeout(function() {
								doradoDroppable.onDraggingSourceDrop(draggingInfo, evt);
							}, 20);
						}
					}
					return true;
				}
			};
			
			options.accept = function(draggable) {
				var accept = originOptions.accept;
				if (accept) {
					if (accept instanceof Function) {
						accept = accept.apply(this, arguments);
					} else {
						accept = draggable.is(accept);
					}
				}
				return !!accept;
			};
		}
		return oldDroppable.call(this, options);
	};
	
	if (dorado.Browser.chrome || dorado.Browser.safari) {
		jQuery.ui.draggable.prototype.options.userSelectFix = true;
		$.ui.plugin.add("draggable", "userSelectFix", {
			start: function(evt, ui) {
				$DomUtils.disableUserSelection(document.body);
			},
			stop: function(evt, ui) {
				$DomUtils.enableUserSelection(document.body);
			}
		});
	}

	var useShimDiv;

	$.ui.plugin.add("draggable", "useShim", {
		start: function(event, ui) {
			var options = $(this).data('draggable').options;
			if (options.useShim !== false) {
				if (!useShimDiv) {
					useShimDiv = document.createElement("div");
					useShimDiv.className = "ui-draggable-useShim";
					useShimDiv.style.background = "#fff";
					document.body.appendChild(useShimDiv);
				}
				$(useShimDiv).css({
					display: "",
					position: "absolute",
					opacity: "0.001",
					zIndex: 999,
					left: 0,
					top: 0
				});

				var doc = useShimDiv.ownerDocument, bodyHeight = $fly(doc).height(), bodyWidth;
				if (dorado.Browser.msie) {
					if (dorado.Browser.version == 6) {
						bodyWidth = $fly(doc).width() - parseInt($fly(doc.body).css("margin-left"), 10) -
									parseInt($fly(doc.body).css("margin-right"), 10);
						$fly(useShimDiv).width(bodyWidth - 2).height(bodyHeight - 4);
					} else if (dorado.Browser.version == 7) {
						$fly(useShimDiv).height(bodyHeight);
					} else if (dorado.Browser.version == 8) {
						$fly(useShimDiv).height(bodyHeight - 4);
					}
				} else {
					$fly(useShimDiv).width("100%").height(bodyHeight - 4);
				}
			}
		},
		stop: function(event, ui) {
			$(useShimDiv).css("display", "none");
		}
	});

	$.ui.draggable.prototype.options.useShim = true;

})(jQuery);
