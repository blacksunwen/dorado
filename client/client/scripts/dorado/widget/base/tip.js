(function() {
	var CONST_MOUSE_POS_ADJ_X = 5, CONST_MOUSE_POS_ADJ_Y = 15,
		ANCHOR_OFFSET_ADJ_HORIZENTAL = 5, ANCHOR_OFFSET_ADJ_VERTICAL = 5, CONST_ELEMENT_POS_ADJ = 3,
		TOOLTIP_KEY = "dorado.tooltip", DOMS_KEY = "dorado.tip.doms";

	var icons = {
		WARNING_ICON: "warning-icon",
		ERROR_ICON: "error-icon",
		INFO_ICON: "info-icon",
		QUESTION_ICON: "question-icon"
	};

	/**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Base
	 * @class 提示信息组件。<br />
	 * 该组件主要用来展示与用户的鼠标动作无关的提示信息，通过指定位置或者锚定对象来显示。<br />
	 * 如果要使用与用户鼠标动作有关的提示信息，请使用dorado.widget.ToolTip替换。
	 *
	 * @extends dorado.widget.Control
	 * @extends dorado.widget.FloatControl
	 */
	dorado.widget.Tip = $extend([dorado.widget.Control, dorado.widget.FloatControl], /** @scope  dorado.widget.Tip.prototype */ {
		$className: "dorado.widget.Tip",
		
        _inherentClassName: "i-tip",
		ATTRIBUTES: /** @scope dorado.widget.Tip.prototype */ {
			className: {
				defaultValue: "d-tip"
			},

			visible: {
				defaultValue: false
			},

			shadowMode: {
				defaultValue: "drop",
				skipRefresh: true
			},

			animateType: {
				defaultValue: "fade",
				skipRefresh: true
			},

			/**
             * 显示的标题。
			 * @attribute
			 * @type String
			 */
			caption: {},

			/**
             * Tip显示的文本，可以为Html。
			 * @attribute
			 * @type String
			 */
			text: {},

			/**
			 *
			 * @attribute
			 * @type String
			 */
			icon: {},

			/**
             * Tip是否可以关闭，默认不可以关闭。
			 * @attribute
			 * @default false
			 * @type boolean
			 */
			closeable: {},

			/**
			 * Tip显示指向箭头的位置，可以为left、right、top、bottom、none，默认为none，即不显示指向箭头。
			 * <p>该属性指的是tip的箭头的相对于自身Box的位置，而不是Tip的显示位置。</p>
			 * @attribute
			 * @type String
			 * @default "none"
			 */
			arrowDirection: {},

			/**
			 * 箭头的横向或者纵向排列位置。<br />
			 * 当arrowDirection为left、right的时候，可选值为left、right、center。<br />
			 * 当arrowDirection为top、bottom的时候，可选值为top、bottom、center。
			 * @attribute
			 * @type String
			 * @default "center"
			 */
			arrowAlign: {
				defaultValue: "center"
			},

			/**
             * 显示的箭头的偏移量，这个偏移量是想对于计算出来的箭头的位置而言。
			 * @attribute
			 * @type int
			 */
			arrowOffset: {},

			/**
			 * 提示信息自动隐藏时间，单位为毫秒，默认为空，则不会自动隐藏。
			 * @attribute
			 * @type int
			 */
			showDuration: {}
		},

		createDom: function() {
			var tip = this, dom, doms = {};
            if (dorado.Browser.msie) {
                dom = $DomUtils.xCreate({
                    tagName: "div",
                    className: tip._className,
                    content: [
                        {
                            tagName: "div",
                            className: "tip-tl",
                            content: {
                                tagName: "div",
                                className: "tip-tr"
                            }
                        },
                        {
                            tagName: "div",
                            className: "tip-cl",
                            content: {
                                tagName: "div",
                                className: "tip-cr",
                                content: {
                                    tagName: "div",
                                    className: "tip-cm",
	                                contextKey: "tipCenter",
	                                content: {
		                                tagName: "div",
		                                contextKey: "tipContent",
										className: "tip-content",
										content: [ {
											tagName: "span",
											className: "tip-icon",
											contextKey: "tipIcon"
										}, {
											tagName: "span",
											className: "tip-text",
											contextKey: "tipText"
										}]
	                                }
                                }
                            }
                        },
                        {
                            tagName: "div",
                            className: "tip-bl",
                            content: {
                                tagName: "div",
                                className: "tip-br"
                            }
                        }
                    ]
                }, null, doms);
            } else {
                dom = $DomUtils.xCreate({
                    tagName: "div",
                    className: tip._className,
                    content: {
                        tagName: "div",
                        className: "tip-cm",
                        contextKey: "tipCenter",
						content: {
							tagName: "div",
							contextKey: "tipContent",
							className: "tip-content",
							content: [ {
								tagName: "span",
								className: "tip-icon",
								contextKey: "tipIcon"
							}, {
								tagName: "span",
								className: "tip-text",
								contextKey: "tipText"
							}]
						}
                    }
                }, null, doms);
            }

			tip._doms = doms;

			$fly(dom).hover(function() {
				if (tip._showDurationTimer) {
					clearTimeout(tip._showDurationTimer);
					tip._showDurationTimer = null;
				}
			}, function() {
				if (tip._showDuration) {
					tip._showDurationTimer = setTimeout(function() {
						tip.hide();
						tip._showDurationTimer = null;
					}, tip._showDuration * 1000);
				}
			});

			return dom;
		},

		doAfterShow: function() {
			var tip = this;
			$invokeSuper.call(tip, arguments);
			if (tip._showDuration) {
				tip._showDurationTimer = setTimeout(function() {
					tip.hide();
					tip._showDurationTimer = null;
				}, tip._showDuration * 1000);
			}
		},
		
		/**
		 * 点击Tip上的关闭按钮会触发的方法。
		 * @protected
		 */
		doClose: function() {
			this.hide();
		},

		getShowPosition: function(options) {
			var tip = this, arrowDirection = tip._arrowDirection, doms = tip._doms;

			if (arrowDirection && (options.offsetLeft == null && options.offsetTop == null)) {
				var arrowAlign = tip._arrowAlign;
				if (arrowAlign) {
					if (arrowDirection == "left") {
                        options.offsetLeft = doms.arrow.offsetWidth;
					} else if (arrowDirection == "right") {
                        options.offsetLeft = -1 * doms.arrow.offsetWidth;
					} else if (arrowDirection == "top") {
                        options.offsetTop = doms.arrow.offsetHeight;
					} else {
                        options.offsetHeight = -1 * doms.arrow.offsetHeight;
					}
				}
			}

			return $invokeSuper.call(this, arguments);
		},

		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);

			var tip = this, text = (tip._text == undefined) ? "&nbsp;&nbsp;" : tip._text,
				doms = tip._doms, arrowDirection = tip._arrowDirection, cls = tip._className;

			$fly(dom).attr("className", "i-tip " + cls + " d-shadow-drop");
			$fly(doms.tipText).html(text);

			if (arrowDirection && arrowDirection != "none") {
				if (doms.arrow == null) {
					var arrowEl = document.createElement("div");
					arrowEl.className = "arrow";
					$fly(dom).append(arrowEl);
					doms.arrow = arrowEl;
				}

				$fly(dom).addClass(cls + "-arrow-" + arrowDirection);
			} else {
                $fly(dom).removeClass(cls + "-arrow-top").removeClass(cls + "-arrow-bottom")
	                .removeClass(cls + "-arrow-left").removeClass(cls + "-arrow-right");
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
			} else if (captionDom != null) {
				$fly(captionDom).css("display", "none");
			}

			if (tip._closeable) {
				if (doms.close == null) {
					var closeEl = document.createElement("div");
					closeEl.className = "close";
					$fly(dom).append(closeEl);

					doms.close = closeEl;

					jQuery(closeEl).click(function() {
						tip.doClose(this);
					}).addClassOnHover("close-hover").addClassOnClick("close-click");
				} else {
                    $fly(doms.close).css("display", "");
                }
			} else if (doms.close) {
				$fly(doms.close).css("display", "none");
			}

			var icon = tip._icon;
			$fly(doms.tipIcon).attr("className", "tip-icon");
			if (icon) {
				$fly(doms.tipIcon).addClass(icon);
				$fly(doms.tipContent).addClass("tip-content-hasicon");
			} else {
				$fly(doms.tipContent).removeClass("tip-content-hasicon");
			}

			$fly(doms.arrow).css({left: "", top: ""});
			if (arrowDirection && !tip._trackMouse) {
				var arrowAlign = tip._arrowAlign, arrowOffset = tip._arrowOffset || 0;
				if (arrowAlign) {
					if (arrowDirection == "left" || arrowDirection == "right") {
						if (arrowAlign == "center") {
							$fly(doms.arrow).css("top", (dom.offsetHeight  - doms.arrow.offsetHeight) / 2 + arrowOffset);
						} else if (arrowAlign == "top") {
							$fly(doms.arrow).css("top", ANCHOR_OFFSET_ADJ_VERTICAL + arrowOffset);
						} else if(arrowAlign == "bottom") {
							$fly(doms.arrow).css("top", dom.offsetHeight - doms.arrow.offsetHeight - ANCHOR_OFFSET_ADJ_VERTICAL + arrowOffset);
						}
					} else {
						if (arrowAlign == "center") {
							$fly(doms.arrow).css("left", (dom.offsetWidth - doms.arrow.offsetWidth) / 2 + arrowOffset);
						} else if (arrowAlign == "left" ) {
							$fly(doms.arrow).css("left", ANCHOR_OFFSET_ADJ_HORIZENTAL + arrowOffset);
						} else if (arrowAlign == "right" ) {
							$fly(doms.arrow).css("left", dom.offsetWidth - doms.arrow.offsetWidth - ANCHOR_OFFSET_ADJ_HORIZENTAL + arrowOffset);
						}
					}
				}
			}
		}

	});

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Base
	 * @class NotifyTip
	 * <p>
	 * 提醒提示信息，主要用来通知用户某个动作已经完成。
	 * 当动作完成不是那么重要，只需要给用户一个不屏蔽用户操作的提醒的时候，可以考虑使用该组件。
	 * 该组件默认在右上角显示，如果显示不开，会按照从上倒下，从右到左的优先级来依次排列。
	 * </p>
	 * @extends dorado.widget.Tip
	 */
	dorado.widget.NotifyTip = $extend(dorado.widget.Tip, {
		$className: "dorado.widget.NotifyTip",

		ATTRIBUTES: {
			width: {
				defaultValue: 300
			},

			closeable: {
				defaultValue: true
			},

			/**
			 * 图标路径。
			 * @attribute
			 * @type String
			 */
			icon: {},

			/**
			 * 图标使用的className。
			 * @attribute
			 * @type String
			 */
			iconClass: {},

			/**
			 * 显示时间，默认为5s，5s后自动隐藏。
			 * @attribute
			 * @type int
			 * @default 5
			 */
			showDuration: {
				defaultValue: 5
			}
		},

		getShowPosition: function(){
			return dorado.widget.NotifyTipManager.getAvialablePosition(this);
		},

		doAfterHide: function() {
			$invokeSuper.call(this, arguments);
			dorado.NotifyTipPool.returnObject(this);
		}
	});

	dorado.NotifyTipPool = new dorado.util.ObjectPool({
		makeObject: function() {
			return new dorado.widget.NotifyTip();
		}
	});

	var getRegionOffsets = function(region1, region2) {
        return {
            top : Math.max(region1['top'], region2['top']),
            right : Math.min(region1['right'], region2['right']),
            bottom: Math.min(region1['bottom'], region2['bottom']),
            left : Math.max(region1['left'], region2['left'])
        };
    };

	var intersect = function(element1, element2) {
        var region1 = $fly(element1).region(), region2;
		if (element2.nodeType) {
			region2 = $fly(element2).region();
		} else {
			region2 = element2;
		}
        var offset = getRegionOffsets(region1, region2);
		return offset['bottom'] >= offset['top'] && offset['right'] >= offset['left'];
    };

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class 提醒信息管理器。
	 * @static
	 */
	dorado.widget.NotifyTipManager = {
		offsetLeft: -10,
		offsetTop: 10,
		padding: 10,
		notifyWidth: 300,
		//tl,tr,bl,br
		position: "br",
		alignPriority: "vertical",

		/**
		 * 在屏幕上显示一个提示信息。
		 * @param {String} msg 提醒信息。
		 * @param {Object} options 选项。
		 */
		notify: function(msg, options) {
			options = options || {};
			var tip = dorado.NotifyTipPool.borrowObject();
			tip._text = msg;
			tip._caption = options.caption || $resource("dorado.baseWidget.NotifyTipDefaultCaption") || "dorado 7";
			tip._icon = options.icon;
			tip.show();
		},

		/**
		 * 为要定位的tip根据现有显示的其他tip找到一个可用的region。就是引用tip的
		 * @param refTip 参考tip，拿到的是该tip的下一个tip。
		 * @param tip 要取得可能region的tip，也就是要定位的tip
		 * @private
		 */
		nextRegion: function(refTip, tip) {
			var left, top, dom = tip._dom, width = dom.offsetWidth, height = dom.offsetHeight, position = this.position;
			var docWidth = $fly(window).width(), docHeight = $fly(window).height();

			if (this.alignPriority == "vertical") {
				if (position == "tr") {
					left = parseInt($fly(refTip._dom).css("left"), 10);
					top = $fly(refTip._dom).outerHeight() + parseInt($fly(refTip._dom).css("top"), 10) + this.padding;

					if (top + height > docHeight) {
						left = left - this.notifyWidth - this.padding;
						top = this.padding;
					}
				} else if (position == "br") {
					left = parseInt($fly(refTip._dom).css("left"), 10);
					top = parseInt($fly(refTip._dom).css("top"), 10) - $fly(refTip._dom).outerHeight() - this.padding;

					if (top < 0) {
						left = left - this.notifyWidth - this.padding;
						top = docHeight - height - this.padding;
					}
				} else if (position == "tl") {
					left = parseInt($fly(refTip._dom).css("left"), 10);
					top = parseInt($fly(refTip._dom).css("top"), 10) + $fly(refTip._dom).outerHeight() + this.padding;

					if (top + height > docHeight) {
						left = left + this.notifyWidth + this.padding;
						top = this.padding;
					}
				} else if (position == "bl") {
					left = parseInt($fly(refTip._dom).css("left"), 10);
					top = parseInt($fly(refTip._dom).css("top"), 10) - $fly(refTip._dom).outerHeight() - this.padding;

					if (top < 0) {
						left = left + this.notifyWidth + this.padding;
						top = docHeight - height - this.padding;
					}
				}
			} else {
				if (position == "tr") {
					left = parseInt($fly(refTip._dom).css("left"), 10) - this.notifyWidth - this.padding;
					top = parseInt($fly(refTip._dom).css("top"), 10);

					if (left < 0) {
						left = docWidth - this.padding - this.notifyWidth;
						top = top + $fly(refTip._dom).outerHeight() + this.padding;
					}
				} else if (position == "br") {
					left = parseInt($fly(refTip._dom).css("left"), 10) - this.notifyWidth - this.padding;
					top = parseInt($fly(refTip._dom).css("top"), 10);

					if (left < 0) {
						left = docWidth - this.padding - this.notifyWidth;
						top = top - $fly(refTip._dom).outerHeight() - this.padding;
					}
				} else if (position == "tl") {
					left = parseInt($fly(refTip._dom).css("left"), 10) + $fly(refTip._dom).outerWidth() + this.padding;
					top = parseInt($fly(refTip._dom).css("top"), 10);

					if (left + width > docWidth) {
						left = this.padding;
						top = top + $fly(refTip._dom).outerHeight() + this.padding;
					}
				} else if (position == "bl") {
					left = parseInt($fly(refTip._dom).css("left"), 10) + $fly(refTip._dom).outerWidth() + this.padding;
					top = parseInt($fly(refTip._dom).css("top"), 10);

					if (left + width > docWidth) {
						left = this.padding;
						top = top - $fly(refTip._dom).outerHeight() - this.padding;
					}
				}
			}

			return {
				left: left,
				top: top,
				bottom: top + height,
				right: left + width
			};
		},

		/**
		 * 判断一个tip的region是否会与其他region重合
		 * @param tip 要判断是否重合的tip
		 * @param region 要判断的tip的假设region
		 * @private
		 */
		avialable: function(tip, region) {
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
		},

		/**
		 * 为tip取得一个可行的位置。
		 * @param tip 要取得位置的tip。
		 * @private
		 */
		getAvialablePosition: function(tip) {
			var docWidth = $fly(window).width(), dom = tip._dom, width = dom.offsetWidth, height = dom.offsetHeight,
				left, top, region, position = this.position;

			if (position == "tr") {
				left = docWidth - this.padding - width;
				top = this.padding;
			} else if (position == "br") {
				left = docWidth - this.padding - width;
				top = $fly(window).height() - height - this.padding;
			} else if (position == "tl") {
				left = this.padding;
				top = this.padding;
			} else if (position == "bl") {
				left = this.padding;
				top = $fly(window).height() - height - this.padding;
			}

			region = {
				left: left,
				top: top,
				bottom: top + height,
				right: left + width
			};

			if (this.avialable(tip, region)) {
				dorado.NotifyTipPool._activePool.remove(tip);
				dorado.NotifyTipPool._activePool.unshift(tip);
				$fly(dom).css({left: left, top: top});
				return {
					left: left,
					top: top
				};
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
							$fly(tip._dom).css({left: region.left, top: region.top});
							return {
								left: region.left,
								top: region.top
							};
						}
					}
				}
			}
		}
	};

	var elementMouseEnter = function(event) {
		var element = this, tip = dorado.TipManager.getTip(element);

		//$log("tip._text:" + tip._text + "\ttip._visible:" + tip._visible);
		if (tip._text && !tip._visible) {
			dorado.TipManager.showTip(element, tip._showDelay || 0, event);
		}

		event.stopImmediatePropagation();
	};

	var elementMouseMove = function(event) {
		var element = this, tip = dorado.TipManager.getTip(element);
		if (tip) {
			if (tip._showTimer) {
				tip._latestEvent = event;
				event.stopImmediatePropagation();
			}
			if(tip._trackMouse && tip._dom && ($fly(tip._dom).css("display") != "none")){
				tip._updatePosition(event);
			}
		}
	};

	var elementMouseLeave = function() {
		var element = this, tip = dorado.TipManager.getTip(element);
		if (tip) {
			//$log("element mouse leave; tip._showTimer:" + tip._showTimer);
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

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Base
	 * @class 系统提示信息组件。<br />
	 * 显示为一个长方形的小弹出面板，该面板在用户将指针悬停在一个控件上时显示有关该控件用途的简短说明。<br />
	 * 该组件与dorado.widget.Tip的区别主要在于该组件的显示依赖于用户的鼠标移动操作，而且会被dorado.widget.ToolTipManager管理。
	 * 如果要为某一组件显示一个与用户的鼠标动作无关的提示信息，请使用dorado.widget.Tip。<br />
	 * 该组件被dorado.widget.ToolTipManager管理，不建议直接创建，那样会造成不可预知的问题。
	 *
	 * @extends dorado.widget.Tip
	 */
	dorado.widget.ToolTip = $extend(dorado.widget.Tip,  /** @scope dorado.widget.ToolTip.prototype */{
		$className: "dorado.widget.ToolTip",

		ATTRIBUTES: /** @scope  dorado.widget.ToolTip.prototype */ {

			/**
			 * 鼠标的偏移值。<br />
			 * ToolTip不是直接显示在鼠标的位置，而是有一定的偏移，默认值为横向为5像素，纵向为15像素。
			 * @attribute
			 * @type Array
			 * @default [5, 15]
			 */
			mouseOffset: {
				defaultValue: [CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y]
			},

			/**
			 * 定位是根据anchorTarget定位，还是根据鼠标定位，默认为false，使用鼠标定位。
			 * @attribute
			 * @type boolean
			 * @default true
			 */
			anchorToTarget: {
				defaultValue: false,
				skipRefresh: true,
				setter: function(value) {
					this._anchorToTarget = value;
					if (this._ready) {
						if (value === false) {
							this.unbindTarget();
						} else {
							this.bindTarget();
						}
					}
				}
			},

			anchorTarget: {
				skipRefresh: true,
				setter: function(value) {
					var oldValue = this._anchorTarget;
					if (oldValue && this._anchorTargetBinded) {
						this.unbindTarget();
					}
					this._anchorTarget = value;
					if (this._ready && value) {
						this.bindTarget();
					}
				}
			},

			/**
             * ToolTip显示延时的毫秒数，默认为500。
			 * @attribute
			 * @default 500
			 * @type int
			 */
			showDelay: {
				skipRefresh: true,
				defaultValue: 500
			},

			/**
             * ToolTip隐藏延时的毫秒数，默认为300。
			 * @attribute
			 * @default 300
			 * @type int
			 */
			hideDelay: {
				skipRefresh: true,
				defaultValue: 300
			},

			/**
             * ToolTip是否在鼠标移开anchorTarget后自动隐藏，默认为自动隐藏。
			 * @attribute
			 * @default true
			 * @type boolean
			 */
			autoHide: {
				defaultValue: true
			},

			/**
             * ToolTip是否对鼠标的移动进行跟踪，默认不进行跟踪。
			 * @attribute
			 * @default false
			 * @type boolean
			 */
			trackMouse: {}
		},

		getShowPosition: function(options){
			var tip = this, dom = tip.getDom(), event = tip._latestEvent || options.event;

			if (tip._anchorToTarget === true) {
				return $invokeSuper.call(this, arguments);
			} else if (tip._anchorToTarget !== true && event) {
				var mouseOffset = tip._mouseOffset || [CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y];
				$DomUtils.locateIn(dom, {
					position: {
						left: event.pageX + mouseOffset[0],
						top: event.pageY + mouseOffset[1]
					}
				});
				tip._latestEvent = null;
			}

			return {
				left: $fly(dom).left(),
				top: $fly(dom).top()
			};
		},

		doClose: function(closeEl) {
			var target = jQuery.data(closeEl.parentNode, TOOLTIP_KEY);
			target.hide();
		},

		getDom: function() {
			var dom = this._dom;
			if (!dom) {
				dom = tipCanUsePool.pop();

				if (dom) {
					this._doms = jQuery.data(dom, DOMS_KEY);
					this._dom = dom;
					//sync visible.
					if (this._visible) {
						$fly(dom).css({ display: "", visibility: "hidden", left: -99999, top: -99999 });
					} else {
						$fly(dom).css({ display: "none" });
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
		},

		/**
		 * 更新Tip的位置。
		 * @param {Event} event dom event.
		 * @protected
		 */
		_updatePosition: function(event) {
			var tip = this;
			if (event) {
				var mouseOffset = tip._mouseOffset || [CONST_MOUSE_POS_ADJ_X, CONST_MOUSE_POS_ADJ_Y];
				$DomUtils.locateIn(tip._dom, {
					position: {
						left: event.pageX + mouseOffset[0],
						top: event.pageY + mouseOffset[1]
					}
				});
			}
		},
		
		/**
		 * 当anchorToTarget设置为false的时候，绑定用户指定的anchorTarget。
		 * @protected
		 */
		bindTarget: function() {
			var element = this._anchorTarget;
			if (element && !this._anchorTargetBinded) {
				$fly(element).hover(elementMouseEnter, elementMouseLeave).mousemove(elementMouseMove);
				this._anchorTargetBinded = true;
			}

		},
		
		/**
		 * 当anchorToTarget设置为false的时候，取消绑定用户指定的anchorTarget。
		 * @protected
		 */
		unbindTarget: function() {
			var element = this._anchorTarget;
			if (element && this._anchorTargetBinded) {
				$fly(element).unbind("mousemove", elementMouseMove).unbind("mouseenter", elementMouseEnter).unbind("mouseleave", elementMouseLeave);
				this._anchorTargetBinded = false;
			}
		},

		doShow: function() {
			$invokeSuper.call(this, arguments);
			//this.refresh();
		},

		hide: function() {
			var tip = this;
			if (tip._showTimer) {
				clearTimeout(tip._showTimer);
				tip._showTimer = null;
				tip._visible = false;

				return;
			}

			$invokeSuper.call(this, arguments);
		},

		show: function() {
			var tip = this;
			if (tip._hideTimer) {
				clearTimeout(tip._hideTimer);
				tip._hideTimer = null;
				tip._visible = true;

				return;
			}

			$invokeSuper.call(this, arguments);
		},

		/**
		 * 隐藏ToolTip。
		 * @protected
		 */
		doAfterHide: function(){
			var tip = this;

			$invokeSuper.call(tip, arguments);

			tipCanUsePool.push(tip._dom);
			jQuery.data(tip._dom, DOMS_KEY, tip._doms);
			jQuery.data(tip._dom, TOOLTIP_KEY, null);

			tip._rendered = false;
			tip._dom = null;
			tip._doms = null;
		}
	});

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
     * @class 提示信息管理器。
     * @static
     */
	dorado.TipManager = {
		_previousTip: null,

		/**
		 * 判断某个Html元素是否绑定了ToolTip。
		 * @param {dorado.RenderableElement|HtmlElement} element 要判断是否有ToolTip的HtmlElement。
		 * @return {boolean} 该Html元素是否绑定了ToolTip。
		 */
		hasTip: function(element) {
			return !!dorado.TipManager.getTip(element);
		},

		/**
		 * 根据给定的Html元素取得该元素的ToolTip。
		 * @param {dorado.RenderableElement|HtmlElement} element 要取得ToolTip的HtmlElement。
		 * @return {dorado.widget.ToolTip} 取得的ToolTip。
		 */
		getTip: function(element) {
			var result;
			if (element) {
				result = jQuery.data(element, TOOLTIP_KEY);
			}
			return result;
		},

		/**
		 * @private
		 */
		allocTip: function(element, options) {
			var result;
			options = options || {};
			options.anchorTarget = element;
			result = new dorado.widget.ToolTip(options);
			result.bindTarget();
			jQuery.data(element, TOOLTIP_KEY, result);
		},

        /**
         * 初始化指定的Html元素的ToolTip的配置信息，如果已经存在过配置信息，则会调用updateTip;如果options为空，则会删除该Html元素绑定的ToolTip。
         * @param {dorado.RenderableElement|HtmlElement} element 要初始化Tip的HtmlElement。如果类型为dorado.RenderableElement，则会取得其dom属性。
         * @param {Object} options tip的配置信息。
         */
		initTip: function(element, options) {
			var manager = this;
			if (element) {
				if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
					element = element._dom;
					if (!element) return;
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
		},

        /**
         * 更新指定的HtmlElement的tip的配置信息。
         * @param {dorado.RenderableElement|HtmlElement} element 要初始化Tip的HtmlElement。如果类型为dorado.RenderableElement，则会取得其dom属性。
         * @param {Object} options tip的配置信息。
         */
		updateTip: function(element, options) {
			if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
				element = element._dom;
				if (!element) return;
			}
			var tip = dorado.TipManager.getTip(element);
			tip.set(options, options);
		},

        /**
         * 删除指定的HtmlElement的tip。
         * @param {dorado.RenderableElement|HtmlElement} element 要删除ToolTip的HtmlElement。
         *          如果类型为dorado.RenderableElement，则会取得其dom属性。
         */
		deleteTip: function(element) {
			if (dorado.Object.isInstanceOf(element, dorado.RenderableElement)) {
				element = element._dom;
				if (!element) return;
			}
			var tip = dorado.TipManager.getTip(element);
			if (tip) {
				dorado.TipManager.hideTip(tip, false);
				tip.unbindTarget();
				jQuery.data(element, TOOLTIP_KEY, null);
			}
		},

        /**
         * 显示指定的HtmlElement的tip。
         * @param {HtmlElement} element 要显示系统提示信息的HtmlElement。
         * @param {int} delay 显示延时，以毫秒作单位。
         * @param {Event} event 浏览器的event。
         */
		showTip: function(element, delay, event) {
			var manager = this, tip = dorado.TipManager.getTip(element);

			if (tip._autoHide === false && !tip._visible) {
				if (delay) {
					tip._showTimer = setTimeout(function(){
						tip.show({ element: element, event: event });
						tip._showTimer = null;
					}, delay);
				} else {
					tip.show({ element: element, event: event });
				}
			} else {
				var oldPrevTip = manager._previousTip;
				if (oldPrevTip && oldPrevTip != tip) {
					oldPrevTip.hide();
				}
				if (delay) {
					tip._showTimer = setTimeout(function(){
						tip.show({ element: element, event: event });
						tip._showTimer = null;
					}, delay);
				} else {
					tip.show({ element: element, event: event });
				}
				manager._previousTip = tip;
			}

			return tip;
		},

        /**
         * 隐藏指定的tip。
         * @param {dorado.widget.ToolTip} tip 要隐藏的tip。
         * @param {int} delay 隐藏的延时，用毫秒作单位。
         */
		hideTip: function(tip, delay) {
			var manager = this;
			if (tip) {
				if (manager._previousTip == tip) {
					manager._previousTip = null;
				}
				if (delay) {
					tip._hideTimer = setTimeout(function(){
						tip.hide();
						tip._hideTimer = null;
					}, delay);
				} else {
					tip.hide();
				}
			}
		}
	};
})();