dorado.util.ShadowPool = new dorado.util.ObjectPool({
	makeObject: function() {
		return $fly(document.body).prepend("<div class=\"i-shadow-ie d-shadow-ie\"></div>")[0].firstChild;
	}
});

dorado.util.Shadow = $class({
	$className: "dorado.util.Shadow",

	constructor: function(config) {
		dorado.Object.apply(this, config);
		if (!this.mode) this.mode = this.defaultMode;

		var offset = this.offset, adjusts = { h: 0 }, rad = Math.floor(this.offset / 2);
		switch (this.mode.toLowerCase()) {
			case "drop":
				adjusts.w = 0;
				adjusts.l = adjusts.t = offset;
				adjusts.t -= 1;
				if (dorado.Browser.msie) {
					adjusts.l -= this.offset + rad;
					adjusts.t -= this.offset + rad;
					adjusts.w -= rad;
					adjusts.h -= rad;
					adjusts.t += 1;
				}
				break;
			case "sides":
				adjusts.w = (offset * 2);
				adjusts.l = -offset;
				adjusts.t = offset - 1;
				if (dorado.Browser.msie) {
					adjusts.l -= (this.offset - rad);
					adjusts.t -= this.offset + rad;
					adjusts.l += 1;
					adjusts.w -= (this.offset - rad) * 2;
					adjusts.w -= rad + 1;
					adjusts.h -= 1;
				}
				break;
			case "frame":
				adjusts.w = adjusts.h = (offset * 2);
				adjusts.l = adjusts.t = -offset;
				adjusts.t += 1;
				adjusts.h -= 2;
				if (dorado.Browser.msie) {
					adjusts.l -= (this.offset - rad);
					adjusts.t -= (this.offset - rad);
					adjusts.l += 1;
					adjusts.w -= (this.offset + rad + 1);
					adjusts.h -= (this.offset + rad);
					adjusts.h += 1;
				}
				break;
		}

		this.adjusts = adjusts;
		this.target = config.target;
	},

	offset: (dorado.Browser.msie && dorado.Browser.version >= 9) ? 8 : 4,
	defaultMode: "drop",

	show: function() {
		var target = this.target, targetEl = jQuery(target), self = this;
		if (!self.dom) {
			self.dom = dorado.util.ShadowPool.borrowObject();
			if (self.dom.nextSibling != target) {
				targetEl.before(self.dom);
			}
		}

		if (targetEl.css("display") == "none") {
			self.dom.style.display = "none";
		} else {
			self.dom.style.zIndex = (parseInt(target.style.zIndex) || 0) - 1;
			var filter = "progid:DXImageTransform.Microsoft.alpha(opacity=50) progid:DXImageTransform.Microsoft.Blur(pixelradius=" + (this.offset) + ")";
			self.dom.style.filter = filter;

			var position = targetEl.offset();
			self.realign(position.left, position.top, targetEl.outerWidth(), targetEl.outerHeight());
			self.dom.style.display = "block";
		}
	},

	realign: function(left, top, width, height) {
		if (!this.dom) return;

		var adjusts = this.adjusts, dom = this.dom, style = dom.style;
		style.left = (left + adjusts.l) + "px";
		style.top = (top + adjusts.t) + "px";
		var sw = (width + adjusts.w), sh = (height + adjusts.h), sws = sw + "px", shs = sh + "px";
		if (style.width != sws || style.height != shs) {
			style.width = sws;
			style.height = shs;
		}
	},

	hide: function() {
		if (this.dom) {
			this.dom.style.display = "none";
			dorado.util.ShadowPool.returnObject(this.dom);
			delete this.dom;
		}
	},

	disable: function() {
		this.disabled = true;
		if (this.dom) {
			this.dom.style.display = "none";
		}
	},

	enable: function() {
		this.disabled = false;
		if (this.dom) {
			this.show();
		}
	}
});

/**
 * @name jQuery#shadow
 * @function
 * @description 为HTML元素添加阴影效果。
 * <p>
 * 注意：在默认情况下，IE和其他浏览器中的所使用的阴影的实现方式是不同的。<br>
 * 在其他浏览器中，我们将使用纯CSS的方法来显示对象阴影。这样可以提高阴影的显示效率，但是在这种情况下options中的offset选项将是无效的。阴影的大小由CSS中的定义决定。
 * </p>
 * <p>
 * 使用该方法添加的阴影效果，如果不是需要移除，则无需手动移除，可以调用unshadow与shadow方法来临时禁用或启用阴影效果。
 * 在非IE浏览器下，浏览器会控制阴影的显示隐藏与大小。
 * 在IE浏览器下，使用了属性监听事件来控制阴影的显示隐藏与大小。
 * </p>
 * @param {Object} [options] 选项。
 * @param {String} [options.mode="drop"] 阴影类型，目前有drop、sides、frame这三种类型供选择。
 * @param {int} [options.offset=4] 阴影大小。
 * @return {jQuery} 调用此方法的jQuery对象自身。
 * @see jQuery#unshadow
 */
jQuery.fn.shadow = function(options) {
	var element;
	if (this.length == 1) {
		element = this[0];
		if (dorado.Browser.msie) {
			if (element.offsetWidth) {
				var shadow = jQuery.data(element, "blockShadow");
				if (!shadow) {
					options.target = element;
					shadow = new dorado.util.Shadow(options);
					jQuery.data(element, "blockShadow", shadow);
					element.onresize = function() {
						//ie9 will get left == 0 && top == 0 in this event
						var disabled = jQuery.data(element, "blockShadowDisabled");
						if (!disabled) {
							setTimeout(function() {
								shadow.show();
							}, 0);
						}
					};
					element.onpropertychange = function() {
						var disabled = jQuery.data(element, "blockShadowDisabled");
						if (!disabled) {
							var property = event.propertyName;
							if (property == "style.visibility" || property == "style.display") {
								shadow.dom.style.visibility = element.style.visibility;
								shadow.dom.style.display = element.style.display == "none" ? "none" : "block";
							} else if (property == "style.left" || property == "style.top") {
								shadow.show();
							}
						}
					};
				}
				shadow.show();
			}
		} else {
			options = options || {};
			var mode = options.mode || "drop";
			switch (mode.toLowerCase()) {
				case "drop":
					this.addClass("i-shadow-drop d-shadow-drop");
					break;
				case "sides":
					this.addClass("i-shadow-sides d-shadow-sides");
					break;
				case "frame":
					this.addClass("i-shadow-frame d-shadow-frame");
					break;
			}
		}
	}
	return this;
};

/**
 * @name jQuery#unshadow
 * @function
 * @description 移除HTML元素上的阴影效果。
 * @return {jQuery} 调用此方法的jQuery对象自身。
 * @see jQuery#shadow
 */
jQuery.fn.unshadow = function() {
	var element;
	if (this.length == 1) {
		element = this[0];
		if (dorado.Browser.msie) {
			var shadow = jQuery.data(element, "blockShadow");
			if (shadow != null) {
				element.onresize = null;
				element.onpropertychange = null;
				shadow.hide();
				jQuery.data(element, "blockShadow", null);
			}
		}
	}
	return this;
};

/**
 * @name jQuery#unshadow
 * @function
 * @description 禁用shadow效果，目前仅对非css阴影有效。如果发现在某些情况下，在IE浏览器下的阴影效果不理想(比如动画过程中)，可以考虑临时禁用阴影。
 * @return {jQuery} 调用此方法的jQuery对象自身。
 * @see jQuery#shadow
 */
jQuery.fn.disableShadow = function() {
	var element;
	if (this.length == 1) {
		element = this[0];
		var blockShadow = jQuery.data(element, "blockShadow");
		if (blockShadow) {
			blockShadow.disable();
		}
		jQuery.data(element, "blockShadowDisabled", true);
	}
	return this;
};

/**
 * @name jQuery#unshadow
 * @function
 * @description 启用shadow效果，目前仅对非css阴影有效。
 * @return {jQuery} 调用此方法的jQuery对象自身。
 * @see jQuery#shadow
 */
jQuery.fn.enableShadow = function() {
	var element;
	if (this.length == 1) {
		element = this[0];
		var blockShadow = jQuery.data(element, "blockShadow");
		if (blockShadow) {
			blockShadow.enable();
		}
		jQuery.data(element, "blockShadowDisabled", false);
	}
	return this;
};
