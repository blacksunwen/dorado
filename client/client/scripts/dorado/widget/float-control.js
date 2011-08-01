dorado._queueObject = {};

dorado.queue = function(namespace, fn) {
	if (!namespace) return;
	var queue = dorado._queueObject[namespace];
	if (!queue) {
		queue = dorado._queueObject[namespace] = [];
	}
	queue.push(fn);
	if (!queue.processing) {
		dorado.dequeue(namespace);
	}
};

dorado.dequeue = function(namespace) {
	if (!namespace) return true;
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

(function() {
	var SHOWHIDE_SUFFIX = "_SHOWHIDE";

	/**
	 * @class 浮动控件的接口。
	 * <p>
	 * 浮动控件是一种比较特殊的可视化控件。例如：菜单、下拉框、对话框等控件。 初始情况下这些控件是不可见的，直到其render()方法被调用。
	 * 一般而言，浮动控件在渲染时不受布局管理器的约束。除非我们显式的为其指定布局条件。
	 * </p>
	 * @abstract
	 */
	dorado.widget.FloatControl = $class(/** @scope dorado.widget.FloatControl.prototype */{
		$className: "dorado.widget.FloatControl",

		ATTRIBUTES: /** @scope dorado.widget.FloatControl.prototype */ {

            /**
             * 是否浮动，默认为true。
             * @attribute
             * @type boolean
             * @default true
             */
			floating: {
				defaultValue: true,
				setter: function(value) {
					var attributeWatcher = this.getAttributeWatcher();
					if (attributeWatcher.getWritingTimes("visible") == 0) {
						this._visible = !value;
					}
					this._floating = value;
				}
			},

            /**
             * 如果浮动，则会为组件的dom添加上该className。
             * @attribute
             * @type String
             */
			floatingClassName: {
				defaultValue: "d-floating"
			},

			visible: {
				defaultValue: false
			},

			/**
			 * 当组件显示或者隐藏的时候要使用的动画类型。
			 * <p>
			 * 一般情况下组件的显示或者隐藏是同一类型的，则无需另外设置showAnimateType或者hideAnimateType。反之，则设置。
			 * 需要说明的是，在显示的时候，showAnimateType的优先级会高于animateType，在隐藏的时候，hideAnimateType的优先级会高于animateType。
			 * </p>
			 * <p>
			 * 目前可选值：zoom、slide、safeSlide、fade、none。
			 * 对几个动画进行一个简单的说明。
			 * <ol>
			 *  <li>none：不使用动画，直接显示或者隐藏。</li>
			 *  <li>zoom：使用一个灰色的html element的放大或者缩小来展示组件的显示或者隐藏。目前被Dialog使用。</li>
			 *  <li>slide：通过四个方向(从上到下，从下到上，从左到右，从右到左)四个方向展示组件的显示与隐藏，目前被Menu的showAnimateType使用。</li>
			 *  <li>safeSlide：在非IE浏览器下，如果组件内部包含IFrame组件时，使用slide类型的动画，会导致IFrame重新加载，在这种情况下，应该使用safeSlide类型的动画替换之。
			 *  另外，如果想保留组件中某些组件的滚动条，也需要使用safeSlide替换slide类型的动画。</li>
			 *  <li>fade：通过改变组件的html element的透明度来展示组件的显示或者隐藏，目前被Tip使用。</li>
			 * </ol>
			 * </p>
			 * @attribute skipAutoRefresh
			 * @default "zoom"
			 * @type String
			 */
			animateType: {
				defaultValue: "zoom",
				skipRefresh: true
			},

			/**
			 * 默认值为空，当显示的时候，取显示要使用的动画，优先级会高于animateType。<br/>
			 *
			 * 可选值：zoom、slide、safeSlide、fade、none。
			 * 关于动画的说明，参考animateType的说明。
			 * @attribute skipAutoRefresh
			 * @type String
			 */
			showAnimateType: {
				skipRefresh: true
			},

			/**
			 * 默认值为空，当隐藏的时候，取隐藏要使用的动画，优先级会高于animateType。 <br />
			 *
			 * 可选值：zoom、slide、safeSlide、fade、none。
			 * 关于动画的说明，参考animateType的说明。
			 * @attribute skipAutoRefresh
			 * @type String
			 */
			hideAnimateType: {
				skipRefresh: true
			},

			/**
			 * 动画开始时的目标来源，目前仅在animateType为zoom的时候会使用，用来确定动画开始的位置。
			 *
			 * @attribute skipAutoRefresh
			 * @type dorado.widget.Control|String|HTMLElement
			 */
			animateTarget: {
				skipRefresh: true
			},

			/**
			 * 组件在屏幕上的左偏移。
			 * @attribute
			 * @default 0
			 * @type int
			 */
			left: {},

			/**
			 * 组件在屏幕上的上偏移。
			 * @attribute
			 * @default 0
			 * @type int
			 */
			top: {},

			/**
			 * 是否居中显示组件。
			 * @attribute skipAutoRefresh
			 * @default false
			 * @type boolean
			 */
			center: {
				skipRefresh: true
			},

			/**
			 * 锚定对象，如果是window，则表示该要停靠的DOM元素相对于当前可视范围进行停靠。<br />
			 * 如果是组件或者HTMLElement，则是相对于组件的dom元素或HTMLElement进行停靠。
			 * @attribute
			 * @type {HtmlElement|window|dorado.RenderableElement}
			 */
			anchorTarget: {
				skipRefresh: true
			},

			/**
			 * 使用align、vAlign计算出组件的位置或者指定的位置的水平偏移量，可以为正，可以为负。
			 * @attribute
			 * @type int
			 */
			offsetLeft: {
				skipRefresh: true
			},

            /**
             * 使用align、vAlign计算出组件的位置或者指定的位置的垂直偏移量，可以为正，可以为负。
             * @attribute
             * @type int
             */
            offsetTop: {
                skipRefresh: true
            },

			/**
			 * 在水平方向上，组件停靠在anchorTarget的位置。可选值为left、innerleft、center、innerright、top。
			 * @attribute
			 * @type String
			 */
			align: {
				skipRefresh: true
			},

			/**
			 * 在垂直方向上，停靠的DOM对象停靠在固定位置的DOM对象的位置。可选值为top、innertop、center、innerbottom、bottom。
			 * @attribute
			 * @type String
			 */
			vAlign: {
				skipRefresh: true
			},

			/**
			 * 当使用默认的align、vAlign计算的位置超出屏幕可见范围以后，是否要对停靠DOM对象的位置进行调整，默认为true，即进行调整。
			 * @attribute
			 * @type boolean
			 * @default true
			 */
			autoAdjustPosition: {
				skipRefresh: true,
				defaultValue: true
			},

			/**
			 * 当自动调整以后也无法使得组件显示在屏幕范围以内以后，就认为停靠的DOM对象的超出触发了，该属性用来标示是否对这种情况进行处理，默认会对这种情况进行处理。
			 * @attribute
			 * @type boolean
			 * @default true
			 */
			handleOverflow: {
				skipRefresh: true,
				defaultValue: true
			},

			/**
			 * 是否模态显示组件。
			 * @attribute skipAutoRefresh
			 * @default false
			 * @type boolean
			 */
			modal: {
				skipRefresh: true
			},

			/**
			 * 模态以后显示的模态背景的类型。<br />
			 * 可选值: dark、transparent。<br />
			 * 每一种类型对应一个className，如果要扩展，可以扩展dorado.widget.FloatControl.modalTypeClassName对象。<br />
			 * @attribute
			 * @type String
			 */
			modalType: {
				skipRefresh: true,
				defaultValue: "dark"
			},

			/**
			 * 组件的阴影的阴影使用的模式。<br />
			 *
			 * 可选值：drop、sides、frame、none。
			 * @attribute
			 * @default "sides"
			 * @type String
			 */
			shadowMode: {
				defaultValue: "sides"
			},

			/**
			 * 是否在显示之后自动获得焦点，默认值为true。
			 * @attribute
			 * @default true
			 * @type boolean
			 */
			focusAfterShow: {
				defaultValue: true
			}
		},

		EVENTS: /** @scope dorado.widget.FloatControl.prototype */ {
			/**
			 * 在显示之前触发。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeShow: {},

			/**
			 * 在显示之后触发。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @event
			 */
			onShow: {},

			/**
			 * 在隐藏之前触发。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeHide: {},

			/**
			 * 在隐藏之后触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onHide: {},

			/**
			 * 在组件关闭之前触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeClose: {},

			/**
			 * 在组件关闭之后触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onClose: {}
		},

		/**
		 * 显示浮动控件。
		 * <p>
		 * 如无特殊需要请不要复写此方法，而应该复写doShow方法。
		 * </p>
		 * <p>
		 * 目前支持三种方式来决定位置：
		 * <ol>
		 * 	<li>指定anchorTarget</li>
		 *  <li>指定position</li>
		 * 	<li>指定left、top</li>
		 * </ol>
		 * 指定anchorTarget以后，anchorTarget会作为fixedElement传入dockAround方法，除了dockArounc方法用到的参数，其他参数会被忽略。<br />
		 * 指定position以后，position会作为参数传入locateIn方法，除了locateIn方法用到的参数，其他参数会被忽略。<br />
		 * 若不指定anchorTarget，也不指定position，则会判断用户是不是设置了center、left、top属性，根据这几个属性计算出组件显示的位置。
		 * </p>
		 *
		 * @param {Object|Function} options 传入的Json参数，下面的参数不会被设置为组件的显示属性。<br />
         *        如果组件每次显示使用的都是不同的参数，不建议使用组件属性里面的显示参数，建议每次在调用show方法的时候动态指定Json参数。<br />
         *        如果options类型为Function，则会被当做options.callback使用，与onShow事件调用的时间相同。
		 * @param {Object} options.position 组件显示的位置，该参数应该为Object，用left、top属性表示绝对值。
		 * @param {HTMLElement|dorado.widget.Control} options.anchorTarget anchorTarget 锚定对象，如果是window，则表示该要停靠的DOM元素相对于当前可视范围进行停靠。<br />
		 *              如果是组件或者HTMLElement，则是相对于组件的dom元素或HTMLElement进行停靠。
		 * @param {String} options.align 在水平方向上，组件停靠在anchorTarget的位置。可选值为left、innerleft、center、innerright、top。
		 * @param {String} options.vAlign 在垂直方向上，组件停靠在anchorTarget的位置。可选值为top、innertop、center、innerbottom、bottom。
		 * @param {Array} options.positionOffset 使用align、vAlign计算出组件的位置的偏移量，索引0为水平偏移量，索引1为垂直偏移量，两个值都是可以为正，可以为负。
		 * @param {boolean} options.autoAdjustPosition 当使用默认的align、vAlign计算的位置超出屏幕可见范围以后，是否要对停靠DOM对象的位置进行调整，默认为true，即进行调整。
		 * @param {boolean} options.handleOverflow 当自动调整以后也无法使得组件显示在屏幕范围以内以后，认为组件的超出触发了，该属性用来标示是否对这种情况进行处理，默认会对这种情况进行处理。
		 * @param {Function} options.overflowHandler 当组件的超出触发以后，要触发的函数，如果不指定，会去判断组件是否存在doHandleOverflow方法，如果存在，则会触发改方法。
		 * @param {String} options.animateType 动画类型。
         * @param {Function} options.callback 在FloatControl显示完成之后，调用的回调函数，与onShow事件调用的时间相同。
		 */
		show: function(options) {
			//$log(this._id + "\tshow..");
            if (typeof options == "function") {
                var callback = options;
                options = {
                    callback: callback
                };
            } else {
                options = options || {};
            }
			var control = this;

			var attrs = ["center", "autoAdjustPosition", "handleOverflow", "offsetLeft", "offsetTop", "align", "vAlign", "handleOverflow", "anchorTarget"];
			for (var i = 0; i < attrs.length; i++) {
				var attr = attrs[i], value = options[attr];
				if (value === undefined) {
					options[attr] = control["_" + attr];
				}
			}

            console.dir(options);

			//$log("control._hideTimer:" + control._hideTimer);
			dorado.queue(control._id + SHOWHIDE_SUFFIX, function() {
				options = options || {};
				if (!control._rendered) {
					var renderTo = control._renderTo;
					if (renderTo) {
						if (renderTo instanceof dorado.widget.Container) renderTo = renderTo.get("containerDom");
						else if(renderTo instanceof dorado.widget.Control) renderTo = renderTo.getDom();
						else if (typeof renderTo == "string") renderTo = jQuery(document.body).find(renderTo)[0];
						else if (!renderTo.nodeName) renderTo = null;
					}
					var oldVisible = control._visible;
					control._visible = true;
					control.render(renderTo);
					control._visible = oldVisible;
				}
				if (control.doShow) control.doShow.apply(control, [options]);
			});
		},

		/**
		 * 该方法在动画播放之前调用，会确定组件显示的位置，在位置确定以后，会按照组件属性的设置执行动画。
		 * 如果需要在动画播放之前执行一些动作，请覆写该方法。
		 *
		 * @param {Object} options Json类型的参数，同show方法。
		 * @protected
		 */
		doShow: function(options){
			var control = this, dom = control.getDom(), anim = true, handleModal = true;

			//left:0是为了解决IE下的一个bug：如果Control在右侧，dom的offsetWidth取得错误，dom实际不超出，导致宽度取值失败
			$fly(dom).css({ display: "", visibility: "hidden", left: -99999, top: -99999 });

			var arg = {};
			control.fireEvent("beforeShow", control, arg);
			if (arg.processDefault === false) {
				dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
				return;
			}

			if (control._visible) {
				//anim = false;
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
                if (options.callback) {
                    options.callback.apply(control.get("view"), [control]);
                }
				var behavior = dorado.widget.FloatControl.behaviors[animateType];
				if (typeof behavior.show == "function") {
					behavior.show.apply(control, [options]);
				}
			} else {
				$fly(dom).css(position);
				control.fireEvent("onShow", control);
                if (options.callback) {
                    options.callback.apply(control.get("view"), [control]);
                }
				control.doAfterShow.apply(control, [options]);
			}
		},

		/**
		 * 在动画完成之后会调用此方法。
		 * 该方法会根据组件的设置，为组件添加阴影、移到最前、获得焦点等。
		 * 在该方法的最后，会触发onShow方法。
		 * @protected
		 */
		doAfterShow: function(){
			var control = this, dom = control.getDom();
			if (dom) {
				$fly(dom).css({
					visibility: "",
					display: ""
				}).bringToFront();

				if(control._shadowMode != "none"){
					$fly(dom).shadow({
						mode: control._shadowMode || "sides"
					});
				}
				if(control.isFocusable() && control._focusAfterShow){
					dorado.widget.setFocusedControl(control);
				}
			}
			dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
			//log.debug("dorado.dequeue after show：" + control._id);
			
			this.setActualVisible(true);
		},

		/**
		 * 取得组件显示的位置。
		 * @param {Object} options Json类型的参数，同show方法。
		 * @protected
		 */
		getShowPosition: function(options) {
			var control = this, anchorTarget = options.anchorTarget, position = options.position,
				dom = control.getDom(), event = options.event, fixedElement, result;

			if (anchorTarget) {
				if (anchorTarget instanceof dorado.widget.Control) {
					fixedElement = anchorTarget._dom;

					control._focusParent = anchorTarget;
				} else if (dorado.Object.isInstanceOf(anchorTarget, dorado.RenderableElement)) {
					fixedElement = anchorTarget._dom;
				} else if (typeof anchorTarget == "string") {
					fixedElement = jQuery(anchorTarget)[0];
				} else {
					fixedElement = anchorTarget;
				}

				result = $DomUtils.dockAround(dom, fixedElement, options);
			} else if (position) {
				result = $DomUtils.locateIn(dom, options);
			} else if (event) {
				options.position = {
					left: event.pageX,
					top: event.pageY
				};

				result = $DomUtils.locateIn(dom, options);
			} else {
				if(options.center && control._left == undefined && control._top == undefined){
					var docSize = {
						width: $fly(window).width(),
						height: $fly(window).height()
					};

					control._left = (docSize.width - $fly(dom).width()) / 2 + jQuery(window).scrollLeft();
					control._top = (docSize.height - $fly(dom).height()) / 2 + jQuery(window).scrollTop();
				}

				options.position = { left: control._left || 0, top: control._top || 0 };

				result = $DomUtils.locateIn(dom, options);
			}

			return result;
		},

		/**
		 * 隐藏浮动控件。
		 * <p>
		 * 如无特殊需要请不要复写此方法，而应该复写doHide方法。
		 * </p>
		 * @param {Object} options Json类型的参数
		 * @param {boolean} options.atOnce 是否立即关闭，忽略hideDelay参数。
		 */
		hide: function(options) {
			//$log(this._id + "\thide..");
			var control = this, args = arguments;
			if (!control._visible) {
				dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
				return;
			}
			
			this.setActualVisible(false);
			dorado.queue(control._id + SHOWHIDE_SUFFIX, function() {
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
		},

		/**
		 * 该方法在动画播放之前调用，会按照组件属性的设置执行动画。
		 * 如果需要在动画播放之前执行一些动作，请覆写该方法。
		 * @param {Object} options Json类型的参数，同hide方法。
		 * @protected
		 */
		doHide: function(options) {
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
					if(control._shadowMode != "none"){
						$fly(control._dom);
					}
					control.doAfterHide();
				}
			}
		},

		/**
		 * 在动画完成之后会调用此方法。
		 * 在该方法的最后，会触发onHide方法。
		 * @protected
		 */
		doAfterHide: function(){
			var control = this, dom = control._dom;
			control.fireEvent("onHide", control);
			$fly(dom).css({
				visibility: "hidden",
				display: "none"
			});
			dorado.dequeue(control._id + SHOWHIDE_SUFFIX);
			//log.debug("dorado.dequeue after hide：" + control._id);
		}
	});

	var slideShow = function(options, safe) {
		var control = this, align = options.align,  vAlign = options.vAlign, direction = options.direction, dom = control._dom;
		$fly(dom).css("visibility", "");

		if (!direction && (vAlign && align)) {
			if (vAlign.indexOf("inner") != -1) { //说明是submenu
				direction = align.indexOf("right") != -1 ? "l2r" : "r2l";
			} else { //说明是button
				direction = vAlign.indexOf("bottom") != -1 ? "t2b" : "b2t";
			}
		}

		direction = direction || "t2b";

		control._slideInDir = direction;

		var position = options.position || {};

		$fly(dom).css(position).bringToFront()[safe ? "safeSlideIn" : "slideIn"]({
			duration: options.animateDuration || 200,
			easing: options.animateEasing,
			direction: direction,
			complete: function() {
				control.doAfterShow.apply(control, [options]);

				dom.style.display = "";
			}
		});
	};

	var slideHide = function(options, safe) {
		var control = this, dom = control._dom, direction = control._slideInDir;
		switch(direction){
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

		$fly(dom)[safe ? "safeSlideOut" : "slideOut"]({
			direction: direction,
			duration: options.animateDuration || 200,
			easing: options.animateEasing,
			complete: function() {
				control.doAfterHide.apply(control, arguments);
			}
		});
	};

	dorado.widget.FloatControl.modalTypeClassName = {
		dark: "d-modal-mask",
		transparent: "d-modal-mask-transparent"
	};

	dorado.widget.FloatControl.behaviors = {
		zoom: {
			show: function(options){
				var control = this, dom = control._dom;
				$fly(dom).zoomIn(jQuery.extend(options, {
					duration: options.animateDuration || 200,
					easing: options.animateEasing,
					complete: function(){
						control.doAfterShow.apply(control, [options]);
					}
				}));
			},
			hide: function(options){
				var control = this, dom = control._dom;
				$fly(dom).css("visibility", "hidden").zoomOut(jQuery.extend(options, {
					duration: options.animateDuration || 200,
					easing: options.animateEasing,
					complete: function(){
						control.doAfterHide.apply(control, arguments);
					}
				}));
			}
		},

		slide: {
			show: function(options){
				slideShow.apply(this, [options]);
			},
			hide: function(options){
				slideHide.apply(this, [options]);
			}
		},

		safeSlide: {
			show: function(options){
				slideShow.apply(this, [options, true]);
			},
			hide: function(options){
				slideHide.apply(this, [options, true]);
			}
		},

		fade: {
			show: function(options){
				var control = this, dom = control._dom;
				$fly(dom).disableShadow().bringToFront().css({
					visibility: "",
					opacity: 0
				}).animate({
					opacity: 1
				}, {
					duration: options.animateDuration || 200,
					easing: options.animateEasing,
					complete: function(){
						$fly(dom).css({
							opacity: ""
						});
						control.doAfterShow.apply(control, [options]);
						$fly(dom).enableShadow();
					}
				});
			},
			hide: function(options){
				var control = this, dom = control._dom;
				$fly(dom).disableShadow().animate({
					opacity: 0
				}, {
					duration: options.animateDuration || 200,
					easing: options.animateEasing,
					complete: function(){
						$fly(dom).css({ opacity: "" });
						control.doAfterHide.apply(control, arguments);
						$fly(dom).enableShadow();
					}
				});
			}
		}
	};
})();