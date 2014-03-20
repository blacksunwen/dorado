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

(function () {

	/**
	 * @name dorado.widget
	 * @namespace 包含dorado中各种界面组件的命名空间。
	 */
	dorado.widget = {};

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @class 组件的抽象类。
	 * @abstract
	 * @extends dorado.AttributeSupport
	 * @extends dorado.EventSupport
	 * @param {String|Object} [config] 配置信息。
	 * <p>
	 * 此参数具有多态性，当我们传入一个String类型的参数时，该String值表示组件的id。
	 * 当我们传入的参数是一个JSON对象时，系统会自动将该JSON对象中的属性设置到组件中。 <br>
	 * 如果没有在此步骤中没有为组件指定id，那么系统会自动为其分配一个id。
	 * </p>
	 */
	dorado.widget.Component = $extend([dorado.AttributeSupport, dorado.EventSupport], /** @scope dorado.widget.Component.prototype */ {
		$className: "dorado.widget.Component",

		ATTRIBUTES: /** @scope dorado.widget.Component.prototype */ {

			/**
			 * 组件的id。
			 * @type String
			 * @attribute writeBeforeReady
			 */
			id: {
				readOnly: true
			},

			/**
			 * 组件当前是否已激活。
			 * @type boolean
			 * @attribute readOnly
			 */
			ready: {
				readOnly: true
			},

			/**
			 * 组件当前是否已被销毁。
			 * @type boolean
			 * @attribute readOnly
			 */
			destroyed: {
				readOnly: true
			},

			/**
			 * 父组件，即组件所属的容器。
			 * @type dorado.widget.Container
			 * @attribute readOnly
			 */
			parent: {
				readOnly: true
			},

			/**
			 * 组件所属的视图。
			 * @type dorado.widget.View
			 * @attribute readOnly
			 */
			view: {
				setter: function (view) {
					if (this._view == view) return;
					if (this._view) this._view.unregisterComponent(this._id);
					if (view) view.registerComponent(this._id, this);
					this._view = view;
				}
			},

			/**
			 * 此视图对象使用的数据类型管理器。
			 * @type dorado.DataTypeRepository
			 * @attribute readOnly
			 */
			dataTypeRepository: {
				readOnly: true,
				getter: function () {
					var view = this.get("view") || $topView;
					return view.get("dataTypeRepository");
				}
			},

			/**
			 * 用户自定义数据。
			 * @type Object
			 * @attribute skipRefresh
			 */
			userData: {
				skipRefresh: true
			}
		},

		EVENTS: /** @scope dorado.widget.Component.prototype */ {
			/**
			 * 当组件被创建时触发的事件。
			 * <p>
			 * 与其他事件不同，onCreate事件中的this是个例外，它并不指向控件所属的View对象，而是指向$view（见{@link $view}）。
			 * 之所以这样是因此，该事件是在控件创建的过程中触发的，而在此时控件还没有被添加到具体的（最终的）View中。
			 * 因此在触发此事件系统无法得知此控件应该属于哪个View。<br>
			 * {@link dorado.widget.View}对象的onCreate事件又是这个例外规则中的例外。
			 * 因为View对象的onCreate事件中的this总是指向自身的，因此View对象的onCreate事件与其他事件中的this并没有什么不同。
			 * </p>
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onCreate: {},

			/**
			 * 当组件被销毁时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onDestroy: {},

			/**
			 * 当组件被激活（即真正可用时）时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onReady: {}
		},

		constructor: function (config) {
			var id;
			if (config && config.constructor == String) {
				id = config;
				config = null;
			} else if (config) {
				id = config.id;
				delete config.id;
			}
			this._uniqueId = dorado.Core.newId();
			dorado.widget.Component.ALL[this._uniqueId] = this;
			if (id) {
				id = id + '';
				if (!(/^[a-zA-Z_$][a-z0-9A-Z_$]*$/.exec(id))) {
					throw new dorado.ResourceException("dorado.widget.InvaliedComponentId", id);
				}
			}
			this._id = id ? id : this._uniqueId;

			$invokeSuper.call(this);
			if (config) this.set(config, { skipUnknownAttribute: true });

			if (AUTO_APPEND_TO_TOPVIEW && window.$topView) {
				$topView.addChild(this);
			}

			if (!(this._skipOnCreateListeners > 0) && this.getListenerCount("onCreate")) {
				this.fireEvent("onCreate", this);
			}
		},

		doSet: function (attr, value, skipUnknownAttribute, lockWritingTimes) {

			function getComponent(value, defaultType) {
				if (!value) return value;
				if (!(value instanceof dorado.widget.Component) && typeof value == "object") {
					value = dorado.Toolkits.createInstance("widget", value, function () {
						return defaultType ? dorado.Toolkits.getPrototype("widget", defaultType) : null;
					});
				}
				return value;
			}

			var def = this.ATTRIBUTES[attr];
			if (def) {
				if (this._ready && def.writeBeforeReady) {
					throw new dorado.AttributeException("dorado.widget.AttributeWriteBeforeReady", attr);
				}
				if (def.componentReference) {
					var component = null, allPrepared = false;
					if (value) {
						if (value instanceof Array) {
							if (value.length > 0) {
								component = [], allPrepared = true;
								for (var i = 0; i < value.length; i++) {
									component[i] = dorado.widget.Component.getComponentReference(this, attr, value[i]);
									if (!(component[i] instanceof dorado.widget.Component)) allPrepared = false;
								}
							}
						} else {
							component = dorado.widget.Component.getComponentReference(this, attr, value);
							allPrepared = (component instanceof dorado.widget.Component);
						}
					}
					$invokeSuper.call(this, [attr, (allPrepared ? component : null), skipUnknownAttribute, lockWritingTimes]);
					// dorado.AttributeSupport.prototype.doSet.call(this, attr, (allPrepared ? component : null), skipUnknownAttribute, lockWritingTimes);
					return;
				} else if (def.innerComponent != null) {
					if (value) {
						if (value instanceof Array) {
							var components = [];
							for (var i = 0; i < value.length; i++) {
								components.push(getComponent(value[i], def.innerComponent));
							}
							value = components;
						} else {
							value = getComponent(value, def.innerComponent);
						}
					}
				}
			}

			$invokeSuper.call(this, [attr, value, skipUnknownAttribute, lockWritingTimes]);
			// dorado.AttributeSupport.prototype.doSet.call(this, attr, value, skipUnknownAttribute, lockWritingTimes);
		},

		/**
		 * 当组件的状态已变为可用时被激活的方法。
		 * @protected
		 */
		onReady: function () {
			if (this._ready) return;
			this._ready = true;
			this.fireEvent("onReady", this);
		},

		/**
		 * 销毁组件。
		 */
		destroy: function () {
			if (this._destroyed) return;
			this._destroyed = true;
			this.fireEvent("onDestroy", this);
			if (!dorado.windowClosed) {
				delete dorado.widget.Component.ALL[this._uniqueId];
			}
		},

		getListenerScope: function () {
			return this.get("view") || $topView;
		},

		fireEvent: function () {
			var optimized = (AUTO_APPEND_TO_TOPVIEW === false);
			if (optimized) AUTO_APPEND_TO_TOPVIEW = true;
			var retVal = $invokeSuper.call(this, arguments);
			if (optimized) AUTO_APPEND_TO_TOPVIEW = false;
			return retVal;
		}
	});

	dorado.widget.Component.getComponentReference = function (object, attr, value) {
		if (!value) return value;
		if (value instanceof dorado.widget.Component) {
			return value;
		} else {
			var component, view;
			if (typeof value == "string") {
				if (object.getListenerScope) {
					view = object.getListenerScope();
				}
				else {
					view = $topView;
				}
				component = view.id(value);
				if (component) return component;
				value = {
					view: view,
					component: value
				};
			} else if (typeof value == "object" && value.$type) {
				return dorado.Toolkits.createInstance("widget", value);
			}

			view = value.view, componentId = value.component;
			component = view.id(componentId);
			if (component) return component;

			var wantedComponents = view._wantedComponents;
			if (!wantedComponents) {
				view._wantedComponents = wantedComponents = {
					count: 0
				};
				view.addListener("onComponentRegistered", viewOnComponentRegisteredListener);
			}
			var wanters = wantedComponents[componentId];
			if (!wanters) {
				wantedComponents[componentId] = wanters = [];
				wantedComponents.count++;
			}
			wanters.push({
				object: object,
				attribute: attr
			});

			var idProperty = '_' + attr + "_id";
			if (!object[idProperty]) {
				object[idProperty] = componentId;
			} else {
				var ids = object[idProperty];
				if (typeof ids == "string") object[idProperty] = ids = [object[idProperty]];
				ids.push(componentId);
			}
			return componentId;
		}
	}

	function viewOnComponentRegisteredListener(view, arg) {
		var wantedComponents = view._wantedComponents;
		var wanters = wantedComponents[arg.component._id];
		if (wanters) {
			var component = arg.component;
			delete wantedComponents[component._id];
			wantedComponents.count--;
			if (wantedComponents.count == 0) {
				view.removeListener("onComponentRegistered", viewOnComponentRegisteredListener);
				delete view._wantedComponents;
			}
			for (var i = 0; i < wanters.length; i++) {
				var wanter = wanters[i], object = wanter.object, attribute = wanter.attribute;
				var ids = object['_' + attribute + "_id"];
				if (ids) {
					if (typeof ids == "string") {
						if (ids == component._id) {
							object.set(attribute, component, {
								lockWritingTimes: true
							});
						}
					} else {
						var index = ids.indexOf(component._id);
						if (index >= 0) {
							ids[index] = component;
							var allComponentPrepared = true;
							for (var j = 0; j < ids.length; j++) {
								if (typeof ids[j] == "string") {
									allComponentPrepared = false;
									break;
								}
							}
							if (allComponentPrepared) {
								object.set(attribute, ids, {
									lockWritingTimes: true
								});
							}
						}
					}
				}
			}
		}
	}

	dorado.widget.Component.ALL = {};

	dorado.Toolkits.registerTypeTranslator("widget", function (type) {
		return dorado.util.Common.getClassType("dorado.widget." + type, true);
	});
})();
