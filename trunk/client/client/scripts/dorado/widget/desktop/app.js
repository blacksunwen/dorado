/**
 * @name dorado.widget.desktop
 * @namespace 桌面相关的一些组件。
 */
dorado.widget.desktop = {};

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.App
 * @extends dorado.AttributeSupport
 */
dorado.widget.desktop.App = $extend([dorado.AttributeSupport, dorado.EventSupport], /** @scope dorado.widget.desktop.App.prototype */{
    $className: "dorado.widget.desktop.App",

	constructor: function(options) {
		$invokeSuper.call(this, arguments);
		if (options) this.set(options);
	},

	ATTRIBUTES: /** @scope dorado.widget.desktop.App.prototype */{
		shell: {},

        /**
         * App的标题。
         * @attribute
         * @type String
         */
		caption: {},

        /**
         * App使用的16*16的图标，可能会被用在QuickButton、TaskButton、Dialog的icon属性上。
         * @attribute
         * @type String
         */
        icon16: {},

        /**
         * App使用的32*32图标，可能会被用在Desktop的Shortcut的icon属性上。
         * @attribute
         * @type String
         */
        icon32: {},

        /**
         * App使用的48*48图标，可能会被用在Desktop的Shortcut的icon属性上。
         * @attribute
         * @type String
         */
        icon48: {},

        /**
         * App使用的64*64图标，可能会被用在Desktop的Shortcut的icon属性上。
         * @attribute
         * @type String
         */
        icon64: {}
	},

	EVENTS: /** @scope dorado.widget.desktop.App.prototype */{
        /**
         * 启动App的时候触发的事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
		onLaunch: {},

        /**
         * 关闭App的时候触发的事件。
         * @param {Object} self 事件的发起者，即组件本身。
         * @param {Object} arg 事件参数。
         * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
         * @event
         */
		onClose: {}
	},

    /**
     * 启动App。该方法应该为final，如果要覆写，请覆写doOnLaunch方法。
     */
	launch: function() {
		if (this.doOnLaunch) {
			this.doOnLaunch();
		}
		this.fireEvent("onLaunch", this);
	},

    /**
     * 关闭该App。该方法应该为final，如果要覆写，请覆写doOnClose方法。
     */
	close: function() {
		var shell = this._shell;
        if (this.doOnClose) {
			this.doOnClose();
		}
		if (shell) {
			shell.closeApp(this.id, this);
		}
		this.fireEvent("onClose", this);
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.AbstractControlApp
 * @extends dorado.widget.desktop.App
 */
dorado.widget.desktop.AbstractControlApp = $extend(dorado.widget.desktop.App, /** @scope dorado.widget.desktop.AbstractControlApp.prototype */{
    $className: "dorado.widget.desktop.AbstractControlApp",
	ATTRIBUTES: {},
	doOnLaunch: function() {
		var app = this, control = app.getControl();
		if (!(control instanceof dorado.widget.Control)) {
			control = app._control = app.createControl();
			if (!control) {
				throw new Error("createControl must return a float control");
			}
		}
		control.show();
	},
	createControl: function() {
		var app = this, control = this.doCreateControl();
		if (!control) {
			throw new Error("doCreateControl must return a control");
		}
		app._control = control;
		app.doBindEvent();
		return control;
	},
	doBindEvent: function() {
		var app = this, control = this.getControl();
		control.addListener("beforeShow", function() {
			app.onControlShow();
		});
		control.addListener("onClose", function() {
			app.onControlClose();
		}, { once: true });
		control.addListener("onFocus", function() {
			app.onControlFocus();
		});
		control.addListener("onBlur", function() {
			app.onControlBlur();
		});
	},
	getControl: function() {},
	onControlFocus: function() {
		var control = this.getControl();
		if (control) {
			$fly(control._dom).bringToFront();
		}
	},
	onControlBlur: function() {},
	onControlShow: function() {},
	onControlClose: function() {
		this.close();
	},
	doCreateControl: function() {
        var app = this, control = app._control;
        if (!(control instanceof dorado.widget.Control)) {
            app._control = control = dorado.Toolkits.createInstance("widget", control);
        }
        return control;
    }
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.WidgetApp
 * @extends dorado.widget.desktop.App
 */
dorado.widget.desktop.WidgetApp = $extend(dorado.widget.desktop.AbstractControlApp, /** @scope dorado.widget.desktop.WidgetApp.prototype */{
    $className: "dorado.widget.desktop.WidgetApp",
	ATTRIBUTES: /** @scope dorado.widget.desktop.WidgetApp.prototype */{
        /**
         * dorado.widget.desktop.Widget中使用的Control。
         * @attribute
         * @type dorado.widget.Control
         */
		control: {}
	},
	getControl: function() {
		return this._widget;
	},
	createControl: function() {
		var app = this, shell = app._shell, control = app.doCreateControl();

		var widget = new dorado.widget.desktop.Widget({
			control: control,
			center: true
		});

		app._widget = widget;

		widget._maximizeTarget = shell._desktop;
		widget._renderTo = shell.getCurrentDesktop();

		app.doBindEvent();

		return widget;
	},
	doOnClose: function() {
		var app = this, widget = app._widget;
		if (widget) {
			widget.destroy();
			app._widget = null;
			app._control = null;
		}
	}
});


/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.ControlApp
 * @extends dorado.widget.desktop.AbstractControlApp
 */
dorado.widget.desktop.ControlApp = $extend(dorado.widget.desktop.AbstractControlApp, /** @scope dorado.widget.desktop.ControlApp.prototype */{
    $className: "dorado.widget.desktop.ControlApp",
	ATTRIBUTES: /** @scope dorado.widget.desktop.ControlApp.prototype */{
        /**
         * ControlApp对应的Control，Control应该为FloatControl类型。
         * @attribute
         * @type dorado.widget.Control
         */
		control: {},

        /**
         * 是否显示TaskButton。
         * @attribute
         * @type boolean
         */
		showTaskButton: {
            defaultValue: true
        }
	},
	getControl: function() {
		return this._control;
	},
	createControl: function() {
		var app = this, shell = app._shell, control = $invokeSuper.call(this, arguments);

		this._control = control;

		control._maximizeTarget = shell._desktop;
		control._renderTo = shell;

		return control;
	},
	onControlShow: function() {
		var app = this, shell = app._shell, taskbar = shell._taskbar, showTaskButton = app._showTaskButton,
            control = app._control, taskButton = app._taskButton;
		if (showTaskButton && !taskButton) {
			taskButton = new dorado.widget.desktop.TaskButton({
				caption: app.getTaskbarCaption(),
                icon: app._icon16 || null,
				control: control
			});
			taskbar.addTaskButton(taskButton);
			control._animateTarget = taskButton;
			app._taskButton = taskButton;
		}
	},
	onControlFocus: function() {
		$invokeSuper.call(this, arguments);
		var app = this, shell = app._shell, taskbar = shell._taskbar, taskButton = app._taskButton;
		if (taskButton) {
            taskbar.setCurrentButton(taskButton);
        }
	},
    onControlBlur: function() {
		$invokeSuper.call(this, arguments);
		var app = this, shell = app._shell, taskbar = shell._taskbar, taskButton = app._taskButton;
		if (taskButton) {
            app._control._blurTime = (new Date()).getTime();
            taskbar.setCurrentButton(null);
        }
	},
	getTaskbarCaption: function(control) {
		if (!control) {
			control = this._control;
		}
		if (control) {
			return control._caption;
		}
		return "";
	},
	doOnClose: function() {
		var app = this, control = app._control, shell = app._shell, taskbar = shell._taskbar;
		if (app._taskButton) {
			taskbar.removeTaskButton(app._taskButton);
			app._taskButton = null;
		}
		if (control) {
			control.destroy();
			app._control = null;
		}
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class IFrameApp
 * @extends dorado.widget.desktop.ControlApp
 */
dorado.widget.desktop.IFrameApp = $extend(dorado.widget.desktop.ControlApp, /** @scope dorado.widget.desktop.IFrameApp.prototype */{
    $className: "dorado.widget.desktop.IFrameApp",
	ATTRIBUTES: /** @scope dorado.widget.desktop.IFrameApp.prototype */{
        /**
         * IFrame的路径。
         * @attribute
         * @type String
         */
		path: {},

        /**
         * Dialog的宽度。
         * @attribute
         * @type String
         */
		width: {},

        /**
         * Dialog的高度。
         * @attribute
         * @type String
         */
		height: {},

        /**
         * Dialog的left。
         * @attribute
         * @type int
         */
        left: {},

        /**
         * Dialog的top。
         * @attribute
         * @type int
         */
        top: {},

        /**
         * Dialog是否居中显示。
         * @attribute
         * @type boolean
         * @default true
         */
		center: { defaultValue: true },

        /**
         * Dialog是否有最小化功能。
         * @attribute
         * @type boolean
         */
		minimizeable: { defaultValue: true },

        /**
         * Dialog是否有最大化功能。
         * @attribute
         * @type boolean
         */
		maximizeable: { defaultValue: true }
	},
	doCreateControl: function() {
		var app = this;
		return new dorado.widget.Dialog({
            icon: app._icon16 || null,
			caption: app._caption,
			center: app._center,
			width: app._width || 640,
			height: app._height || 480,
            left: app._left,
            top: app._top,
			layout: {
				$type: "Dock"
			},
			children: [{
				$type: "IFrame",
				path: this._path
			}],
			minimizeable: app._minimizeable,
			maximizeable: app._maximizeable
		});
	}
});

var appTypeMap = {
	Default: dorado.widget.desktop.ControlApp,
	Control: dorado.widget.desktop.ControlApp,
	Widget: dorado.widget.desktop.WidgetApp,
	IFrame: dorado.widget.desktop.IFrameApp
};

dorado.Toolkits.registerPrototype("app", appTypeMap);