/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Base
 * @class 卡片组件。
 * <p>
 * 该组件可以添加多个子组件，但是每次只能显示一个子组件，显示的组件称为活动组件。<br />
 * 目前该组件被TabControl使用。
 * </p>
 * @extends dorado.widget.Control
 */
dorado.widget.CardBook = $extend(dorado.widget.Control, /** @scope dorado.widget.CardBook.prototype */ {
	$className: "dorado.widget.CardBook",

	ATTRIBUTES: /** @scope dorado.widget.CardBook.prototype */ {
		className: {
			defaultValue: "d-cardbook"
		},

		/**
		 * 当前活动的组件。
		 * @type dorado.widget.Control|int
		 * @attribute
		 */
		currentControl: {
			skipRefresh: true,
			setter: function(value) {
				var cardbook = this, controls = cardbook._controls;
				if (value != null) {
					if (typeof value == "string" || typeof value == "number") {
						value = controls.get(value);
					}
				}
                var oldValue = cardbook._currentControl;
				var eventArg = {
					oldValue: oldValue
				};
                cardbook.fireEvent("beforeCurrentChange", this, eventArg);
				if (eventArg.processDefault === false) return;
				if (oldValue) {
					var oldDom = oldValue._dom;
					if (oldDom) {
						oldDom.style.display = "none";
					}
                    oldValue.setActualVisible(false);
				}
				cardbook._currentControl = value;
				var dom = cardbook._dom;
				if (dom && value) {
					value.set("width", $fly(dom).innerWidth());
					value.set("height", $fly(dom).innerHeight());

					if (!value._rendered) {
						cardbook.registerInnerControl(value);
						value.render(dom);
					} else {
						$fly(value._dom).css("display", "block");
                        value.setActualVisible(true);
					}
				}
                cardbook.fireEvent("onCurrentChange", this, value);
			}
		},

		/**
		 * Card中所有的Control。
		 * 该属性设置时使用Array类型的数据进行设置，取得时取得的数据类型为dorado.util.KeyedArray。
		 * @type dorado.util.KeyedArray
		 * @attribute
		 */
		controls: {
			writeOnce: true,
			innerComponent: "",
			setter: function(value) {
				if (value) {
					var controls = this._controls, currentFirstControl = (controls.size == 0);
					if (value instanceof Array) {
						for (var i = 0; i < value.length; i++) {
							controls.insert(value[i]);
							value[i]._parent = value[i]._focusParent = this;
							if (i == 0 && currentFirstControl) {
								this.set("currentControl", value[i]);
							}
						}
					} else if (value.constructor == Object.prototype.constructor) {
                        controls.insert(value);
						value._parent = value._focusParent = this;
                        this.set("currentControl", value);
                    }
				}
			}
		}
	},

    EVENTS: /** @scope dorado.widget.CardBook.prototype */ {
		/**
		 * 在currentControl改变之前触发。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
        beforeCurrentChange: {},
        /**
         * 在currentControl改变之后触发。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
        onCurrentChange: {}
    },

	constructor: function() {
		this._controls = new dorado.util.KeyedArray(function(value) {
			return value._id;
		});
		$invokeSuper.call(this, arguments);
	},

	/**
	 * 插入子组件。
	 * @param {dorado.widget.Control} control 要插入的子组件
	 * @param {int} index {optional} 要插入的子组件的索引。
	 * @param {boolean} current {optional} 是否把插入的组件置为活动组件，默认为false。
	 * @return {dorado.widget.Control} 插入的组件。
	 */
	addControl: function(control, index, current) {
		if (!control) {
			throw new dorado.ResourceException("dorado.base.CardControlUndefined");
		}
		var card = this, controls = card._controls;
		card.registerInnerControl(control);
		controls.insert(control, index);
		if (current !== false) {
			card.set("currentControl", control);
		}
		return control;
	},

	/**
	 * 移除子组件。
	 * @param {String|int|dorado.widget.Control} control
	 * 可以为组件本身、组件的索引(在controls中的索引)、组件的id。
	 * @return {dorado.widget.Control} 移除的子组件
	 */
	removeControl: function(control) {
		var card = this, controls = card._controls;
		control = card.getControl(control);
		if (control) {
			control.destroy && control.destroy();
			controls.remove(control);
			return control;
		}
		return null;
	},

	/**
	 * 移除所有的组件。
	 */
	removeAllControls: function() {
		var card = this, controls = card._controls;
		for (var i = 0, j = controls.size; i < j; i++) {
			var item = controls.get(0);
			card.removeControl(item);
		}
	},

	/**
	 * 取得Card中的组件。
	 * @param {String|Number|dorado.widget.Control} id 组件的id、index或者组件本身。
	 * @return {dorado.widget.Control} 找到的子组件。
	 */
	getControl: function(id) {
		var card = this, controls = card._controls;
		if (controls) {
			if (typeof id == "number" || typeof id == "string") {
				return controls.get(id);
			} else {
				return id;
			}
		}
		return null;
	},

	createDom: function() {
		var dom = $invokeSuper.call(this, arguments);
		dom.className = this._className;

		return dom;
	},

	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);

		var card = this, currentControl = card["_currentControl"];
		/*
		 * Commented by benny 此处应当允许currentControl为Null。
		 * 否则当TabControl中的某个Tab的getControl()不能返回一个有效的控件，CardBook会指向错误的Control。
		 if (!currentControl && controls) {
		 currentControl = card["_currentControl"] = controls.get(0);
		 }
		 */

		if (currentControl) {
			currentControl.set("width", $fly(dom).innerWidth());
			currentControl.set("height", $fly(dom).innerHeight());

			if (!currentControl._rendered) {
				card.registerInnerControl(currentControl);
				currentControl.render(dom);
			} else {
				$fly(currentControl._dom).css("display", "block");
                currentControl.setActualVisible(true);
			}
		}
	}
});
