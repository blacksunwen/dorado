/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 *
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 *
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html)
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 *
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
(function($) {

	var SCROLLER_SIZE = 3, ACTIVE_SCROLLER_SIZE = 11, SCROLLER_PADDING = 0, MIN_SLIDER_SIZE = ACTIVE_SCROLLER_SIZE, MIN_SPILLAGE = 2;
	
	function insertAfter(element, refElement) {
		var parent = refElement.parentNode;
		if (parent.lastChild == refElement) {
			parent.appendChild(element);
		} else {
			parent.insertBefore(element, refElement.nextSibling);
		}
	}
	
	var Scroller = $class({
		// dom, doms, container, direction
		
		constructor: function(container, direction, options) {
			this.container = container;
			this.direction = direction;
			if (options) dorado.Object.apply(this, options);
		},
		
		createDom: function() {
			var scroller = this, doms = {}, dom = scroller.dom = $DomUtils.xCreate({
				tagName: "DIV",
				className: "i-modern-scroller d-modern-scroller",
				style: "position: absolute",
				content: [{
					tagName: "DIV",
					contextKey: "track",
					className: "track",
					style: {
						width: "100%",
						height: "100%"
					}
				}, {
					tagName: "DIV",
					contextKey: "slider",
					className: "slider",
					style: "position: absolute"
				}]
			}, null, doms);
			
			var $dom = scroller.$dom = $(dom), slider = doms.slider, $slider = $(slider), track = doms.track, $track = $(track);
			
			var draggableOptions = {
				containment: "parent",
				start: function() {
					scroller.dragging = true;
				},
				stop: function() {
					scroller.dragging = false;
					(scroller.hover) ? scroller.doMouseEnter() : scroller.doMouseLeave();
				},
				drag: function() {
					var container = scroller.container;
					if (scroller.direction == "h") {
						container.scrollLeft = Math.round(slider.offsetLeft * scroller.positionRatio);
					} else {
						container.scrollTop = Math.round(slider.offsetTop * scroller.positionRatio);
					}
				}
			}
			
			if (scroller.direction == "h") {
				dom.style.height = SCROLLER_SIZE + "px";
				
				slider.style.height = "100%";
				slider.style.top = "0px";
				
				draggableOptions.axis = "x";
			} else {
				dom.style.width = SCROLLER_SIZE + "px";
				
				slider.style.width = "100%";
				slider.style.left = "0px";
				
				draggableOptions.axis = "y";
			}
			$slider.draggable(draggableOptions);
			
			$dom.hover(function() {
				scroller.update();
				scroller.doMouseEnter();
			}, function() {
				scroller.doMouseLeave();
			});
			
			$track.click(function(evt) {
				var container = scroller.container;
				if (scroller.direction == "h") {
					if (evt.offsetX > slider.offsetLeft) {
						container.scrollLeft += container.clientWidth;
					} else {
						container.scrollLeft -= container.clientWidth;
					}
				} else {
					if (evt.offsetY > slider.offsetTop) {
						container.scrollTop += container.clientHeight;
					} else {
						container.scrollTop -= container.clientHeight;
					}
				}
			});
			
			$DomUtils.disableUserSelection(dom);
			$DomUtils.disableUserSelection(doms.track);
			
			scroller.doms = doms;
			return dom;
		},
		
		doMouseEnter: function() {
			var scroller = this;
			scroller.hover = true;
			if (scroller.dragging) return;
			
			scroller.$dom.addClass("d-modern-scroller-hover");
			scroller.expand();
		},
		
		doMouseLeave: function() {
			var scroller = this;
			scroller.hover = false;
			if (scroller.dragging) return;
			
			scroller.$dom.removeClass("d-modern-scroller-hover");
			scroller.unexpand();
		},
		
		expand: function() {
			var scroller = this;
			dorado.Toolkits.cancelDelayedAction(scroller, "$expandTimerId");
			if (scroller.expanded) return;
			
			var animOptions;
			if (scroller.direction == "h") {
				animOptions = {
					height: ACTIVE_SCROLLER_SIZE
				};
			} else {
				animOptions = {
					width: ACTIVE_SCROLLER_SIZE
				};
			}
			
			scroller.expanded = true;
			scroller.$dom.addClass("d-modern-scroller-expand");
			if (dorado.Browser.msie && dorado.Browser.version < 7) {
				scroller.$dom.css(animOptions);
			} else {
				scroller.duringAnimation = true;
				scroller.$dom.animate(animOptions, "fast", function() {
					scroller.duringAnimation = false;
				});
			}
		},
		
		unexpand: function() {
			var scroller = this;
			dorado.Toolkits.setDelayedAction(scroller, "$expandTimerId", function() {
				var animOptions, container = scroller.container;
				if (scroller.direction == "h") {
					animOptions = {
						height: SCROLLER_SIZE
					};
				} else {
					animOptions = {
						width: SCROLLER_SIZE
					};
				}
				
				if (dorado.Browser.msie && dorado.Browser.version < 7) {
					scroller.$dom.css(animOptions);
					scroller.expanded = false;
				} else {
					scroller.duringAnimation = true;
					scroller.$dom.animate(animOptions, "fast", function() {
						scroller.expanded = false;
						scroller.duringAnimation = false;
						scroller.$dom.removeClass("d-modern-scroller-expand");
					});
				}
			}, 700);
		},
		
		update: function() {
			if (this.dragging) return;
			
			var scroller = this, dom = scroller.dom, doms = scroller.doms;
			var container = scroller.container, $container = $(container), scrollerSize = scroller.expanded ? ACTIVE_SCROLLER_SIZE : SCROLLER_SIZE;
			
			if (scroller.direction == "h") {
				if (container.scrollWidth > (container.clientWidth + MIN_SPILLAGE) && container.clientWidth > 0) {
					if (!dom) {
						dom = scroller.createDom();
						doms = scroller.doms;
						
						dom.style.zIndex = 9999;
						dom.style.bottom = 0;
						dom.style.left = 0;
						if (!dorado.Browser.msie || dorado.Browser.version != 6) {
							dom.style.width = "100%";
						}
						container.parentNode.appendChild(dom);
					} else {
						dom.style.display = "";
					}
					
					if (dorado.Browser.msie && dorado.Browser.version == 6) {
						dom.style.width = container.offsetWidth + "px";
					}
					
					var trackSize = container.offsetWidth - SCROLLER_PADDING * 2;
					var slider = doms.slider;
					var sliderSize = (trackSize * container.clientWidth / container.scrollWidth), sliderAdj = 0;
					if (sliderSize < MIN_SLIDER_SIZE) {
						trackSize -= (MIN_SLIDER_SIZE - sliderSize);
						sliderSize = MIN_SLIDER_SIZE;
					}
					
					scroller.positionRatio = container.scrollWidth / trackSize;
					slider.style.left = Math.round(container.scrollLeft / scroller.positionRatio) + "px";
					slider.style.width = Math.round(sliderSize) + "px";
				} else {
					if (dorado.Browser.msie && dorado.Browser.version == 9 && container.offsetWidth > 0) {
						// IE9下有时无法在初始化是取到正确的clientWidth
						setTimeout(function() {
							scroller.update();
						}, 0);						
					}
					if (dom) {
						dom.style.display = "none";
					}
				}
			} else {
				if (container.scrollHeight > (container.clientHeight + MIN_SPILLAGE) && container.clientHeight > 0) {
					if (!dom) {
						dom = scroller.createDom();
						doms = scroller.doms;
						
						dom.style.zIndex = 9999;
						dom.style.top = 0;
						dom.style.right = 0;
						if (!dorado.Browser.msie || dorado.Browser.version != 6) {
							dom.style.height = "100%";
						}
						container.parentNode.appendChild(dom);
					} else {
						dom.style.display = "";
					}
					
					if (dorado.Browser.msie && dorado.Browser.version == 6) {
						dom.style.height = container.offsetHeight + "px";
					}
					
					var trackSize = container.offsetHeight - SCROLLER_PADDING * 2;
					var slider = doms.slider;
					var sliderSize = (trackSize * container.clientHeight / container.scrollHeight), sliderAdj = 0;
					if (sliderSize < MIN_SLIDER_SIZE) {
						trackSize -= (MIN_SLIDER_SIZE - sliderSize);
						sliderSize = MIN_SLIDER_SIZE;
					}
					
					scroller.positionRatio = container.scrollHeight / trackSize;
					slider.style.top = Math.round(container.scrollTop / scroller.positionRatio) + "px";
					slider.style.height = Math.round(sliderSize) + "px";
				} else {
					if (dorado.Browser.msie && dorado.Browser.version == 9 && container.offsetHeight > 0) {
						// IE9下有时无法在初始化是取到正确的clientWidth
						setTimeout(function() {
							scroller.update();
						}, 0);						
					}
					if (dom) {
						dom.style.display = "none";
					}
				}
			}
		},
		
		destroy: function() {
			var dom = this.dom;
			if (dom && dom.parentNode) dom.parentNode.removeChild(dom);
			delete this.dom;
			delete this.doms;
			delete this.container;
		}
	});
	
	var ModernScrolled = $class({
		constructor: function(container, options) {
			this.id = dorado.Core.newId();
			this.container = container;	
			this.options = options || {};		
			var $container = $(container), options = this.options;
			
			if (options.listenSize || options.listenContainerSize || options.listenContentSize) {
				addListenModernScrolled(this);
			}
		},
		
		destroy: function() {
			var options = this.options;
			if (options.listenSize || options.listenContainerSize || options.listenContentSize) {
				removeListenModernScrolled(this);
			}
		}
	});
	
	var DesktopModernScrolled = $extend(ModernScrolled, {
		// container, xScroller, yScroller, originOverflowX, originOverflowY
		
		constructor: function(container, options) {
			$invokeSuper.call(this, arguments);
			
			var options= this.options; $container = $(container),
				parentDom = container.parentNode, $parentDom = $(parentDom);
			
			var overflowX = $container.css("overflowX"), overflowY = $container.css("overflowY");
			var width = $container.css("width"), height = $container.css("height");
			var xScroller, yScroller;
			
			if (!(overflowX == "hidden" || overflowX != "scroll" && (width == "" || width == "auto"))) {
				$container.css("overflowX", "hidden");
				xScroller = new Scroller(container, "h", options);
			}
			if (!(overflowY == "hidden" || overflowY != "scroll" && (height == "" || height == "auto"))) {
				$container.css("overflowY", "hidden");
				yScroller = new Scroller(container, "v", options);
			}
			
			if (!xScroller && !yScroller) throw new dorado.AbortException();
			
			this.xScroller = xScroller;
			this.yScroller = yScroller;
			this.originOverflowX = overflowX;
			this.originOverflowY = overflowY;
			
			var position = $parentDom.css("position");
			if (position != "relative" && position != "absolute") {
				$parentDom.css("position", "relative");
			}
			
			position = $container.css("position");
			if (position != "relative" && position != "absolute") {
				$container.css("position", "relative");
			}
			
			this.update();
			
			var modernScrolled = this;
			$container.mousewheel(function(evt, delta) {
				if (container.scrollHeight > container.clientHeight) {
					var scrollTop = container.scrollTop - delta * 25;
					if (scrollTop <= 0) {
						scrollTop = 0;
					} else if (scrollTop + container.clientHeight > container.scrollHeight) {
						scrollTop = container.scrollHeight - container.clientHeight;
					}
					var gap = container.scrollTop - scrollTop
					if (gap) {
						container.scrollTop = scrollTop;
						if (Math.abs(gap) > MIN_SPILLAGE) return false;
					}
				}

				if (container.scrollWidth > container.clientWidth) {
					var scrollLeft = container.scrollLeft - delta * 25;
					if (scrollLeft <= 0) {
						scrollLeft = 0;
					} else if (scrollLeft + container.clientWidth > container.scrollWidth) {
						scrollLeft = container.scrollWidth - container.clientWidth;
					}
					var gap = container.scrollLeft - scrollLeft
					if (gap) {
						container.scrollLeft = scrollLeft;
						if (Math.abs(gap) > MIN_SPILLAGE) return false;
					}
				}
			}).bind("scroll", function(evt) {
				modernScrolled.update();
			}).resize(function(evt) {
				modernScrolled.update();
			});
		},
		
		update: function() {
			if (this.xScroller) this.xScroller.update();
			if (this.yScroller) this.yScroller.update();
			
			var container = this.container;
			this.currentClientWidth = container.clientWidth;
			this.currentClientHeight = container.clientHeight;
			this.currentScrollWidth = container.scrollWidth;
			this.currentScrollHeight = container.scrollHeight;
		},
		
		scrollToElement: function(dom) {
			// TODO
		},
		
		destroy: function() {
			$invokeSuper.call(this, arguments);			
			if (this.xScroller) this.xScroller.destroy();
			if (this.yScroller) this.yScroller.destroy();
		}
	});
	
	var TouchModernScrolled = $extend(ModernScrolled, {
		// iscroll
		
		constructor: function(container, options) {
			var $container = $(container);
			var overflowX = $container.css("overflowX"), overflowY = $container.css("overflowY");
			var width = $container.css("width"), height = $container.css("height");
			
			/*
			if ((overflowX == "hidden" || overflowX != "scroll" && (width == "" || width == "auto")) &&
				(overflowY == "hidden" || overflowY != "scroll" && (height == "" || height == "auto"))) {
				throw new dorado.AbortException();
			}
			*/		
			
			var modernScrolled = this, options = modernScrolled.options = dorado.Object.apply({
				scrollbarClass: "iscroll",
				hideScrollbar: true
			}, options, false);
			
			$container.css("overflowX", "hidden").css("overflowY", "hidden");
			setTimeout(function() {
				modernScrolled.iscroll = new iScroll(container, modernScrolled.options);
				if (options.autoDisable && container.scrollHeight <= (container.clientHeight + 2) && (container.scrollWidth <= container.clientWidth + 2)) {
					modernScrolled.iscroll.disable();
				}
			}, 0);
			
			$invokeSuper.call(modernScrolled, [container, modernScrolled.options]);
			
			var $container = $(container);
			$container.bind("scroll", function(evt) {
				modernScrolled.update();
			}).resize(function(evt) {
				modernScrolled.update();
			});
		},
		
		update: function() {
			if (!this.iscroll) return;
			
			if (this.options.autoDisable) {
				var container = this.container;
				if (container.scrollHeight > (container.clientHeight + 2) || container.scrollWidth > (container.clientWidth + 2)) {
					this.iscroll.enable();
					this.iscroll.refresh();
				}
				else {
					this.iscroll.disable();
				}
			}
			else {
				this.iscroll.refresh();
			}
		},
		
		scrollToElement: function(dom) {
			if (this.iscroll) this.iscroll.scrollToElement(dom);
		}
	});
	
	var listenModernScrolleds = new dorado.util.KeyedList(dorado._GET_ID), listenTimerId;
	
	function addListenModernScrolled(modernScrolled) {
		listenModernScrolleds.insert(modernScrolled);
		if (listenModernScrolleds.size == 1) {
			listenTimerId = setInterval(function() {
				listenModernScrolleds.each(function(modernScrolled) {
					var container = modernScrolled.container, shouldUpdate = false;
					if (modernScrolled.options.listenSize || modernScrolled.options.listenContainerSize) {
						if (modernScrolled.currentClientWidth != container.clientWidth ||
							modernScrolled.currentClientHeight != container.clientHeight) {
							shouldUpdate = true;
						}
					}
					if (modernScrolled.options.listenSize || modernScrolled.options.listenContentSize) {
						if (modernScrolled.currentScrollWidth != container.scrollWidth ||
							modernScrolled.currentScrollHeight != container.scrollHeight) {
							shouldUpdate = true;
						}
					}
					if (shouldUpdate) {
						modernScrolled.update();
					}
				});
			}, 300);
		}
	}
	
	function removeListenModernScrolled(modernScrolled) {
		listenModernScrolleds.remove(modernScrolled);
		if (listenModernScrolleds.size == 0 && listenTimerId) {
			clearInterval(listenTimerId);
			listenTimerId = 0;
		}
	}
	
	/**
	 * @param {Object} container
	 * @param {Object} [options]
	 * @param {boolean} [options.listenSize]
	 * @param {boolean} [options.listenContainerSize]
	 * @param {boolean} [options.listenContentSize]
	 */
	dorado.util.Dom.modernScroll = function(container, options) {
		var $container = $(container);
		if ($container.data("modernScrolled")) return;
		
		try {
			var modernScrolled;
			if (dorado.Browser.isTouch || $setting["common.simulateTouch"]) {
				modernScrolled = new TouchModernScrolled(container, options);;
			}
			else {			
				var parentDom = container.parentNode;
				if (parentDom) {
					modernScrolled = new DesktopModernScrolled(container, options);
				}
			}
			
			if (modernScrolled) $container.data("modernScrolled", modernScrolled);
		} 
		catch (e) {
			dorado.Exception.processException(e);
		}
		return modernScrolled;
	}
	
	dorado.util.Dom.destroyModernScroll = function(container, options) {
		var modernScrolled = $(container).data("modernScrolled");
		if (modernScrolled) modernScrolled.destroy();
	}
	
})(jQuery);
