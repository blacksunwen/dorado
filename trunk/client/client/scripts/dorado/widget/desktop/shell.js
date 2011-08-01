/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.Shell
 * @extends dorado.widget.Control
 */
dorado.widget.desktop.Shell = $extend(dorado.widget.Control, /** @scope dorado.widget.desktop.Shell.prototype */{
    $className: "dorado.widget.desktop.Shell",

	constructor: function() {
		this._instances = {};
		$invokeSuper.call(this, arguments);
	},
	ATTRIBUTES: /** @scope dorado.widget.desktop.Shell.prototype */ {
		className: {
			defaultValue: "d-shell"
		},

        /**
         * Shell对应的桌面。
         * @attribute
         * @type dorado.widget.desktop.Desktop
         */
		desktop: {
            innerComponent: ""
        },

        /**
         * Shell对应的Taskbar。
         * @attribute
         * @type dorado.widget.desktop.Taskbar
         */
		taskbar: {
            innerComponent: ""
        },

        /**
         * Shell中的apps，注意初始化以后不要再设置该属性。
         * @attribute
         * @type Array
         */
        apps: {
            setter: function(value) {
                if (value instanceof Array) {
                    for (var i = 0, j = value.length; i < j; i++) {
                        var app = value[i];
                        this.registerApp(app);
                    }
                }
            }
        },

		/**
		 * 桌面的墙纸位置
		 * @attribute
		 * @type String
		 */
		wallpaper: {
			skipRefresh: true,
			setter: function(value) {
				if (this._rendered) {
                    $DomUtils.setBackgroundImage(this._dom, value || "");
				}
				this._wallpaper = value;
			}
		}
	},

    /**
     * 取得当前桌面。
     */
	getCurrentDesktop: function() {
		var shell = this, desktop = shell._desktop;
		if (desktop instanceof dorado.widget.desktop.DesktopCarousel) {
			return desktop._currentControl;
		} else if (desktop instanceof dorado.widget.desktop.Desktop) {
			return desktop;
		}
	},

    /**
     * 注册指定的app的实例。
     * @param {String} appId 要注册的app的实例的id。
     * @param {Object} instance app的实例。
     * @private
     */
	registerInstance: function(appId, instance) {
		var instanceArray = this._instances[appId];
		if (!instanceArray) {
			this._instances[appId] = instanceArray = [];
		}
		instanceArray.push(instance);
	},

    /**
     * 注销指定的app的实例，一般不需要手动注销，当app的某个实例close的时候，会主动调用该方法。
     * @param {String} appId 要注销的app的实例的id。
     * @param {Object} instance app的实例。
     * @private
     */
	unregisterInstance: function(appId, instance) {
		if (!appId || !instance) return;
		var instanceArray = this._instances[appId];
		if (instanceArray) {
			for (var i = 0, j = instanceArray.length; i < j; i++) {
				instanceArray.remove(instance);
			}
		}
	},

    /**
     * 取得某个单例app的实例。
     * @param {String} appId 要取得实例的app的id。
     * @private
     */
	getInstance: function(appId) {
		var instanceArray = this._instances[appId];
		if (!instanceArray) {
			return null;
		}
		return instanceArray[0];
	},

    /**
     * 判断某个app是否已经实例。
     * @param {String} appId 要判断是否有实例的app的id。
     * @private
     */
	hasInstance: function(appId) {
		var instanceArray = this._instances[appId];
		if (!instanceArray) {
			return false;
		}
		return instanceArray.length > 0;
	},

    /**
     * 注册一个App。
     * @param {Object} options App的json配置信息，不可为app的实例，id为必填，App的属性直接在配置信息里面配置即可。其中要指定App的$type，可以为Control、IFrame、Widget。
     * @param {boolean=true} singleton 是否单例，默认为true。
     * @return {Function} App的构造函数。
     */
	registerApp: function(options, singleton) {
		options = options || {};
		var appId = options.id, appClazz, attrs;
		if (!appId) {
			throw new Error("id is required for createApp");
		}

        var type = options.$type || "Default", clazz = appTypeMap[type];
        if (options.attrs) {
            attrs = options.attrs;
            delete options.attrs;
        }

        if (attrs == null) {
            attrs = {};
        }

        var allAttrs = clazz.prototype.ATTRIBUTES;
        for (var prop in options) {
            if (prop in allAttrs) {
                attrs[prop] = options[prop];
                delete options[prop];
            }
        }

        appClazz = $extend(clazz, options);

		if (!this._apps) {
			this._apps = {};
			this._appAttrs = {};
			this._singletons = {};
		}

		this._apps[appId] = appClazz;
		this._appAttrs[appId] = attrs;
		this._singletons[appId] = singleton;

		return appClazz;
	},

    /**
     * 启动指定id的app。
     * @param {String} appId 要启动的app的id。
     */
	launchApp: function(appId) {
		var app = this._apps[appId], options = this._appAttrs[appId];
		if (app) {
			var instance;

			if (this._singletons[appId] !== false) {
				if (!this.hasInstance(appId)) {
					instance = new app(options);
					this.registerInstance(appId, instance);
					instance._shell = this;
					instance.launch();
				} else {
					instance = this.getInstance(appId);
					instance.launch();
				}
			} else {
				instance = new app(options);
				this.registerInstance(appId, instance);
				instance._shell = this;
				instance.launch();
			}
		}
	},

    /**
     * 关闭指定的app的实例。
     * @param {String} appId 要关闭的app的id。
     * @param {Object} appInstance 要关闭的实例。
     */
	closeApp: function(appId, appInstance) {
		var app = this._apps[appId];
		if (app && appInstance) {
			this.unregisterInstance(appId, appInstance);
		}
	},

	createDom: function() {
		var dom = $invokeSuper.call(this, arguments);
		var desktop = this._desktop, taskbar = this._taskbar;
		if (desktop) {
            this.registerInnerControl(desktop);
			desktop.render(dom);
		}
		if (taskbar) {
            this.registerInnerControl(taskbar);
			taskbar.render(dom);
		}

        $DomUtils.setBackgroundImage(dom, this._wallpaper || "");

		return dom;
	},

	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		var width = $fly(dom).width(), height = $fly(dom).height(), desktop = this._desktop, taskbar = this._taskbar, tabbarHeight = 0;
		if (taskbar) {
			taskbar.set("width", width);
			taskbar.refresh();
			tabbarHeight = $fly(taskbar._dom).outerHeight();
		}
		if (desktop) {
			desktop.set({
				width: width,
				height: height - tabbarHeight
			});
			desktop.refresh();
		}
	}
});