(function() {
	dorado.Browser.supportTouch = "ontouchstart" in window;
	dorado.gesture = {};

	var touch = {}, singleTapTimeout;

	function parentIfText(node){
		return 'tagName' in node ? node : node.parentNode;
	}

	function swipeDirection(x1, x2, y1, y2){
		var xDelta = Math.abs(x1 - x2), yDelta = Math.abs(y1 - y2);
		if (xDelta >= yDelta) {
			return (x1 - x2 > 0 ? 'Left' : 'Right');
		} else {
			return (y1 - y2 > 0 ? 'Up' : 'Down');
		}
	}

	dorado.GestureManager = {
		eventStarted: false,

		init: function() {
			var manager = this, startEvent, endEvent, moveEvent;

			if (dorado.Browser.supportTouch) {
				startEvent = "touchstart";
				endEvent = "touchend";
				moveEvent = "touchmove";
			} else {
				startEvent = "mousedown";
				endEvent = "mouseup";
				moveEvent = "mousemove";
				return;
			}

			jQuery(document.body).bind(startEvent, function(e){
				//window.ss = new Date();
				if (e.originalEvent) e = e.originalEvent;
				manager.onTouchStart.apply(manager, [e]);
			}).bind(moveEvent, function(e){
				//window.mms = new Date();
				if (e.originalEvent) e = e.originalEvent;
				manager.onTouchMove.apply(manager, [e]);
			}).bind(endEvent, function(e){
				//window.es = new Date();
				//console.log("es - ms:" + (window.es - window.mms) + "\tes - ss:" + (window.es - window.ss));
				if (e.originalEvent) e = e.originalEvent;
				manager.onTouchEnd.apply(manager, [e]);
			});
		},

		onTouchStart: function(e) {
			var eventObject;
			if (dorado.Browser.supportTouch) {
				eventObject = e.touches[0];
			} else {
				eventObject = e;
				if (e.button == 2) {
					return;
				}
			}

			this.currentTargets = [];

			touch.target = parentIfText(eventObject.target);
			var target = touch.target;
			while(target && target != document.body) {
				var gestures = jQuery.data(target, 'dorado-gestures');
				if (gestures) this.currentTargets.push(target);
				target = target.parentNode;
			}

			touch.x1 = eventObject.pageX;
			touch.y1 = eventObject.pageY;

			this.targetGestures(function(gesture) {
				gesture.onTouchStart(e);
			});

			this.eventStarted = true;
		},

		onTouchMove: function(e) {
			if (!this.eventStarted) {
				return;
			}

			var eventObject;
			if (dorado.Browser.supportTouch) {
				eventObject = e.touches[0];
			} else {
				eventObject = e;
			}
			touch.x2 = eventObject.pageX;
			touch.y2 = eventObject.pageY;

			this.targetGestures(function(gesture) {
				gesture.onTouchMove(e);
			});
		},

		onTouchEnd: function(e) {
			var manager = this;

			this.targetGestures(function(gesture) {
				gesture.onTouchEnd(e);
			});

			this.eventStarted = false;
		},

		targetGestures: function(fn) {
			var manager = this, targets = manager.currentTargets || [];
			//TODO
			for (var i = 0, j = targets.length; i < j; i++) {
				var target = targets[i], gestures = jQuery.data(target, 'dorado-gestures');
				for (var type in gestures) {
					var gesture = gestures[type], result = fn(gesture);
					//console.log("fn result:" + result);
					if (result === false) return;
				}
			}
		},

		bind: function(dom, eventName, func) {
			var gesture = this.getGesture(dom, eventName, true);
			//console.log("gesture");
			gesture.addListener(eventName, func);
		},

		getGesture: function(dom, eventName, createIfNull) {
			var gestures = jQuery.data(dom, 'dorado-gestures');
			if (!gestures) {
				gestures = {};
				jQuery.data(dom, 'dorado-gestures', gestures);
			}
			var type = this.gestureEventsMap[eventName];
			if (gestures[type]) {
				return gestures[type];
			} else if (createIfNull) {
				var result = this.createGesture(eventName, {});
				//console.log("create an empty gesture:" + type);
				gestures[type] = result;
				return result;
			}
		},

		fireEvent: function(dom, eventName, arg) {
			var gesture = this.getGesture(dom, eventName);
			if (gesture) {
				gesture.fireEvent(eventName, arg);
			}
		},

		fireEvents: function(eventName, arg) {
			var manager = this, targets = manager.currentTargets || [];
			for (var i = 0, j = targets.length; i < j; i++) {
				var target = targets[i];
				manager.fireEvent(target, eventName, arg);
			}
		},

		unbind: function(dom, eventName, func) {
			var gesture = this.getGesture(dom, eventName);
			if (gesture) gesture.removeListener(eventName, func);
		},

		gestureTypes: [],
		gestureEventsMap: {},

		registerGesture: function(type, cls) {
			this.gestureTypes[type] = cls;
			var events = cls.prototype.events || [], bindFn = function() {
				dorado.GestureManager.bind.apply(dorado.GestureManager, arguments);
			}, unbindFn = function() {
				dorado.GestureManager.unbind.apply(dorado.GestureManager, arguments);
			};
			for (var i = 0, j = events.length; i < j; i++) {
				var event = events[i];
				this.gestureEventsMap[event] = type;
			}
		},

		createGesture: function(eventName, options) {
			var type = this.gestureEventsMap[eventName], cls = this.gestureTypes[type];
			if (cls) {
				return new cls(options);
			}
		}
	};

	jQuery(function() {
		dorado.GestureManager.init();
	});

	dorado.gesture.Gesture = $extend([dorado.AttributeSupport, dorado.EventSupport], {
		EVENTS: {},
		constructor: function(options) {
			var events = this.events || [];
			for (var i = 0; i < events.length; i++) {
				this.EVENTS[events[i]] = {};
			}
			$invokeSuper.call(this, arguments);
			this.set(options);
		},
		onTouchStart: function(e) {},
		onTouchMove: function(e) {},
		onTouchEnd: function(e) {},
		fireEvent: function() {
			$invokeSuper.call(this, arguments);
			//console.log("gesture event " + arguments[0] + " fired.");
		}
	});

	dorado.gesture.Touch = $extend(dorado.gesture.Gesture, {
		events: ['touchstart', 'touchmove', 'touchend', 'touchdown', 'tapstart', 'tapcancel', 'tap', 'doubletap', 'taphold', 'singletap', 'swipe'],
		onTouchStart: function(e) {
			var now = Date.now(), delta = now - (touch.last || now);
			singleTapTimeout && clearTimeout(singleTapTimeout);
			if (delta > 0 && delta <= 250) touch.isDoubleTap = true;
			touch.last = now;

			var gesture = this;

			if (gesture.touchInterval) {
				clearInterval(gesture.touchInterval);
				gesture.touchInterval = null;
			}

			gesture.fireEvent("touchstart", e);
			gesture.fireEvent("tapstart", e);

			gesture.touchInterval = setInterval(function() {
				gesture.fireEvent("touchdown", e);
				var now = Date.now(), delta = now - (touch.last || now);
				if (delta >= 1000 && !touch.tapholdFired) {
					gesture.fireEvent("taphold", e);
					touch.tapholdFired = true;
				}
			}, 500);
		},
		onTouchMove: function(e) {
			var gesture = this;
			gesture.fireEvent("touchmove", e);
			if (!gesture.tapcancelled && (Math.abs(touch.x2 - touch.x1) > 5 || Math.abs(touch.y2 - touch.y1) > 5)) {
				gesture.fireEvent("tapcancel", e);

				gesture.tapcancelled = true;
			}
		},
		onTouchEnd: function(e) {
			var gesture = this;
			gesture.fireEvent("touchend", e);

			//console.log("touchend:" + (new Date() - window.es) + "\tlast in touch:" + ('last' in touch));

			if ((Math.abs(touch.x1 - touch.x2) > 0 || Math.abs(touch.y1 - touch.y2) > 0) && (touch.x2 > 0 || touch.y2 > 0)) {
				(Math.abs(touch.x1 - touch.x2) > 30 || Math.abs(touch.y1 - touch.y2) > 30)  &&
					gesture.fireEvent('swipe', e) && false && /** delete this. */
				gesture.fireEvent('swipe' + (swipeDirection(touch.x1, touch.x2, touch.y1, touch.y2)), e);
				//console.log("swipe...");
				touch.x1 = touch.x2 = touch.y1 = touch.y2 = touch.last = 0;
			} else if (('last' in touch)) {
				//console.log("beforetap:" + (new Date() - window.es));
				gesture.fireEvent("tap", e);
				if (touch.isDoubleTap) {
					gesture.fireEvent("doubletap", e);

					touch = {};
				} else {
					singleTapTimeout = setTimeout(function(){
						singleTapTimeout = null;

						gesture.fireEvent("singletap", e);

						this.currentTargets = null;
						touch = {};
					}, 250);
				}
			}
			clearInterval(gesture.touchInterval);
			gesture.touchInterval = null;
			gesture.tapcancelled = false;
		}
	});

	dorado.gesture.Drag = $extend(dorado.gesture.Gesture, {
		events: ['dragstart', 'drag', 'dragend'],
		onTouchStart: function(e) {

		},
		onTouchMove: function(e) {
			var gesture = this;
			if (!gesture.dragstarted && (Math.abs(touch.x2 - touch.x1) > 5 || Math.abs(touch.y2 - touch.y1) > 5)) {
				gesture.fireEvent("dragstart", e);

				gesture.dragstarted = true;
			} else if (gesture.dragstarted) {
				gesture.fireEvent("drag", e);
			}
		},
		onTouchEnd: function(e) {
			var gesture = this;
			if (gesture.dragstarted) {
				gesture.fireEvent("dragend", e);
			}
			gesture.dragstarted = false;
		}
	});

	dorado.gesture.Pinch = $extend(dorado.gesture.Gesture, {
		events: ['pinchstart', 'pinch', 'pinchend'],
		onTouchStart: function(e) {
		},
		onTouchMove: function(e) {
		},
		onTouchEnd: function(e) {
		}
	});

	dorado.GestureManager.registerGesture('touch', dorado.gesture.Touch);
	dorado.GestureManager.registerGesture('drag', dorado.gesture.Drag);
	dorado.GestureManager.registerGesture('pinch', dorado.gesture.Pinch);
})();
(function() {
	if (!("ontouchstart" in window)) return;

	jQuery.event.special["mouseover"] = {
		setup: function() {}
	};

	jQuery.event.special["mouseout"] = {
		setup: function() {}
	};

	jQuery.event.special["click"] = {
		setup: function() {
			var elem = this;
			$(elem).each(function() {
				dorado.GestureManager.bind(elem, "tap", function(event) {
                    var touch = event.targetTouches[0] || event.touches[0];
                    console.log("tap event:" + event + "\ttouch:" + touch);
					return jQuery.event.trigger( { type: "click", target: touch.target }, null, elem );
				});
			});
		}
	};

	jQuery.event.special["mousedown"] = {
		setup: function() {
			var elem = this;
			dorado.GestureManager.bind(elem, "touchstart", function(event) {
				var touch = event.targetTouches[0];
				$(elem).trigger({ type: "mousedown", target: touch.target });
			});
		}
	};

	jQuery.event.special["mouseup"] = {
		setup: function() {
			var elem = this;
			dorado.GestureManager.bind(elem, "touchend", function(event) {
				$(elem).trigger({ type: "mouseup" });
			});
		}
	};

	jQuery.fn.hover = function() {
        return this;
    };

	$.fn.addClassOnClick = function(cls, clsOwner, fn) {
		clsOwner = clsOwner || this;
		this.mousedown(function() {
			if (fn instanceof Function && !fn.call(this)) return;
			clsOwner.addClass(cls);
			//console.log("addClassOnClick mousedown");
		});
		this.mouseup(function() {
			//console.log("addClassOnClick mouseup:" + (new Date - window.s));
			clsOwner.removeClass(cls);
		});
		return this;
	};
})();
