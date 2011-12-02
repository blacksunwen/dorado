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
	_inherentClassName: "i-cardbook",
	
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
			setter: function(control) {
				var cardbook = this, controls = cardbook._controls;
				if (control != null) {
					if (typeof control == "string" || typeof control == "number") {
						control = controls.get(control);
					}
				}
				if (cardbook._currentControl == control) return;
				if (!cardbook._ready) { // 延时触发currentChange事件，以确保第一次触发时CardBook已被渲染
					cardbook._currentControl = control;
					return;
				}
				
				var oldControl = cardbook._currentControl;
				var eventArg = {
					oldControl: oldControl,
					newControl: control
				};
				cardbook.fireEvent("beforeCurrentChange", this, eventArg);
				if (eventArg.processDefault === false) return;
				if (oldControl) {
					var oldDom = oldControl._dom;
					if (oldDom) {
						oldDom.style.display = "none";
					}
					oldControl.setActualVisible(false);
				}
				cardbook._currentControl = control;
				var dom = cardbook._dom;
				if (dom && control) {
					if (!control._rendered) {
						cardbook.registerInnerControl(control);
						control.render(dom);
					} else {
						$fly(control._dom).css("display", "block");
						control.setActualVisible(true);
					}
                    control.set("width", $fly(dom).innerWidth());
					control.set("height", $fly(dom).innerHeight());

                    control.resetDimension();
				}
				cardbook.fireEvent("onCurrentChange", this, eventArg);
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
					var controls = this._controls;
					if (value instanceof Array) {
						for (var i = 0; i < value.length; i++) {
							controls.insert(value[i]);
						}
					} else if (value.constructor == Object.prototype.constructor) {
						controls.insert(value);
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
		 * @param {dorado.widget.Control} arg.newControl 要切换到的控件。
		 * @param {dorado.widget.Control} arg.oldControl 当前控件。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeCurrentChange: {},
		
		/**
		 * 在currentControl改变之后触发。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.Control} arg.newControl 要切换到的控件。
		 * @param {dorado.widget.Control} arg.oldControl 切换之前的控件。
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
	
	onReady: function() {
		var currentControl = this._currentControl;
		this._currentControl = null;
		
		$invokeSuper.call(this);
		
		if (!currentControl) {
			currentControl = this._controls.get(0);
		}
		if (currentControl) {
			this.set("currentControl", currentControl);
		}
	},
	
	/**
	 * 插入子组件。
	 * @param {dorado.widget.Control} control 要插入的子组件
	 * @param {int} [index] 要插入的子组件的索引。
	 * @param {boolean} [current=false] 是否把插入的组件置为活动组件，默认为false。
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

    /**
     * 取得当前组件的索引。
     * @return {int} 当前组件的索引，如果没有当前组件，返回-1.
     */
    getCurrentControlIndex: function() {
        var card = this, controls = card._controls, currentControl = card._currentControl;
        if (controls && currentControl) {
            return controls.indexOf(currentControl);
        }
        return -1;
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
	},
	
	getFocusableSubControls: function() {
		return [this._currentControl];
	}
});
