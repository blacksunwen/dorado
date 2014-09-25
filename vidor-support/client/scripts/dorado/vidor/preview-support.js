/**
 * Created with JetBrains WebStorm.
 * User: Alex Tong(mailto:alex.tong@bstek.com)
 * Date: 13-12-19
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
(function () {
	window.$cloudo = window.parent.cloudo;
	window.ControlMap = {
		_ALL_Control: {},
		put: function (nid, control) {
			this._ALL_Control[nid] = control;
		},
		remove: function (nid) {
			delete this._ALL_Control[nid];
		},
		find: function (nid) {
			return this._ALL_Control[nid];
		},
		findParentControl: function (nid) {
			var control = this.find(nid);
			if (!control) {
				var nodeMeta = $cloudo.NodeHelper.meta(nid);
				if (nodeMeta && nodeMeta.parentMeta) {
					var parentMeta = nodeMeta.parentMeta;
					var parentNid = parentMeta.nid;
					control = this.find(parentNid);
				}
			}
			return   control;
		}
	};

	var oldFun = dorado.Toolkits.createInstance;

	dorado.Toolkits.createInstance = function (namespace, config, typeTranslator) {

		var cloudoId, _KEY = "$cloudoID";
		if (config) {
			cloudoId = config && config[_KEY];
			delete config[_KEY];
			if (config.listener) {
				var listener = config.listener;
				for (var k in listener) {
					if (listener.hasOwnProperty(k)) {
						var c, l = listener[k];
						if (l instanceof Object) {
							eval('c=' + l.fn);
							l.fn = c;
						} else {
							eval('c=' + l);
							if (typeof c == 'function') {
								listener[k] = c;
							}
						}
					}
				}
			}
		}
		var args = Array.prototype.slice.call(arguments);
		var o = oldFun.apply(dorado.Toolkits, args);
		if (cloudoId) {
			ControlMap.put(cloudoId, o);
			o[_KEY] = cloudoId;
			if (o && o.EVENTS) {
				var oType = o.constructor.className;
				var eventName = "onClick";
				if (o.EVENTS.onTabChange) {
					o.addListener("onReady", function (self) {
						function bind(tab) {
							var dom = tab.getDom();
							dom._doradoTab = tab;
							$fly(dom).click(function (event) {
								$CloudoHelper.pushCurrent(tab[_KEY]);
								return false;
							});

							$fly(dom).addClass("c-target");
						}

						var tabs = self.get("tabs").toArray();
						for (var i = 0; i < tabs.length; i++) {
							var tab = tabs[i];
							bind(tab);
						}
					});
					o.addListener(eventName, function (self, arg) {
						if (arg && arg.event) {
							$CloudoHelper.pushCurrent(self[_KEY]);
						}
						arg.returnValue = false;
					});
				}  else if (o.EVENTS.onClick) {
					o.addListener(eventName, function (self, arg) {
						if (arg && arg.event) {
							$CloudoHelper.pushCurrent(self[_KEY]);
							arg.returnValue = false;
						}
					});
				} else if (o.EVENTS.onFocus) {
					eventName = "onFocus";
					o.addListener(eventName, function (self, arg) {
						if (!arg.dom[_KEY]) {
							arg.dom[_KEY] = true;
							$fly(arg.dom).click(function (event) {
								$CloudoHelper.pushCurrent(self[_KEY]);
							});
						}
						arg.processDefault = true;
					});
				}
				if (o.EVENTS.onReady) {
					o.addListener("onReady", function (self, arg) {
						if (self.getDom) {
							var dom = $(self.getDom());
							dom.addClass("c-target");
							if (self instanceof dorado.widget.Container) {
								self.getContentContainer && $(self.getContentContainer()).bind("scroll", function (e) {

								});
							}
						}
					});
				}
			}
		}
		return o;
	};
})();