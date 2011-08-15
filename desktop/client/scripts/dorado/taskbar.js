/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @description Taskbar上用的按钮。
 * @class dorado.widget.desktop.TaskButton
 * @extends dorado.widget.AbstractButton
 */
dorado.widget.desktop.TaskButton = $extend(dorado.widget.AbstractButton, /** @scope dorado.widget.desktop.TaskButton.prototype */ {
	$className: "dorado.widget.desktop.TaskButton",
	ATTRIBUTES: /** @scope dorado.widget.desktop.TaskButton.prototype */ {
		className: {
			defaultValue: "task-button"
		},
		
		width: {
			defaultValue: 150
		},
		
		/**
		 * 图标所在路径。
		 * @type String
		 * @attribute
		 */
		icon: {},
		
		/**
		 * 按钮的标题。
		 * @type String
		 * @attribute
		 */
		caption: {},
		
		/**
		 * 图标使用的className。
		 * @type String
		 * @attribute
		 */
		iconClass: {
			defaultValue: "default-icon"
		},
		
		/**
		 * 是否处于活动状态。
		 * @attribute
		 * @type boolean
		 */
		current: {
			defaultValue: false
		},

        /**
         * ControlApp生成的TaskButton，会包含该属性。
         * @attribute readOnly
         * @type dorado.widget.Control
         */
		control: {},

        /**
         * ControlApp生成的TaskButton，会包含该属性。
         * @attribute readOnly
         * @type dorado.widget.desktop.App
         */
        app: {}
	},
	
	getShell: function() {
		return this._parent.findParent(dorado.widget.desktop.Shell);
	},
	
	onClick: function() {
		$invokeSuper.call(this, arguments);
		var button = this, taskbar = button._parent, control = this._control;
		if (control) {
			if (!control._visible) {
				control.show();
			} else {
				if (control._blurTime) {
					var delta = (new Date()).getTime() - control._blurTime;
					
					control._blurTime = null;
					
					if (delta < 200) {
						control.hide();
						taskbar.setCurrentButton(null);
						
						return;
					}
				}
				if (button._current) {
					control.hide();
					taskbar.setCurrentButton(null);
				} else {
					dorado.widget.setFocusedControl(control);
				}
				
			}
		}
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var button = this, cls = button._className, doms = button._doms, action = button._action || {};
		
		$fly(button._doms.caption).html(button._caption || action._caption);
		
		var icon = button._icon || action._icon, iconCls = button._iconClass || action._iconClass;
		
		if (!icon && !iconCls && doms.icon) {
			$fly(doms.icon).css("display", "none");
		} else {
			if (doms.icon) {
				$fly(doms.icon).attr("className", "icon").css("display", "");
			}
			if ((icon || iconCls) && !doms.icon) {
				button._createIconSpan();
			}
			
			if (icon) {
				$DomUtils.setBackgroundImage(doms.icon, icon);
			} else if (doms.icon) {
				$fly(doms.icon).css("background-image", "");
			}
			
			if (iconCls) {
				$fly(doms.icon).addClass(iconCls);
			}
		}
		$fly(dom)[button._current ? "addClass" : "removeClass"](cls + "-current");
	},
	
	onResize: function() {
		var button = this, dom = button._dom, width = button.getRealWidth();
		if (dom && width != null) {
			$fly(dom).width(width);
			var leftWidth = dom.offsetWidth - button._doms.buttonRight.offsetWidth -
			parseInt($fly(button._doms.buttonLeft).css("margin-left"), 10);
			
			if (leftWidth > 0) {
				$fly(button._doms.buttonLeft).outerWidth(leftWidth);
			}
		}
	},
	
	_createIconSpan: function(dom) {
		var button = this, doms = button._doms, action = button._action || {};
		dom = dom || button._dom;
		if (dom) {
			var icon = document.createElement("span");
			icon.className = "icon";
			icon.innerHTML = "&nbsp;";
			
			$fly(icon).prependTo(doms.buttonLeft).addClass(button._iconClass || action._iconClass);
			
			$DomUtils.setBackgroundImage(icon, button._icon || action._icon);
			
			doms.icon = icon;
		}
	},
	
	createDom: function() {
		var button = this, cls = button._className, doms = {}, action = button._action || {};
		
		var dom = $DomUtils.xCreateElement({
			tagName: "div",
			className: cls,
			content: [{
				tagName: "div",
				className: "button-left",
				contextKey: "buttonLeft",
				content: {
					tagName: "div",
					className: "caption",
					content: button._caption || action._caption,
					contextKey: "caption"
				}
			}, {
				tagName: "div",
				className: "button-right",
				contextKey: "buttonRight"
			}]
		}, null, doms);
		
		button._doms = doms;
		
		$fly(dom).bind("contextmenu", function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();
			var taskbar = button._parent, arg = {
				taskButton: button,
				event: event
			};
			taskbar._contextMenuTaskButton = button;
			taskbar.fireEvent("onTaskButtonContextMenu", taskbar, arg);
			
			return false;
		});
		
		var icon = button._icon || action._icon, iconCls = button._iconClass || action._iconClass;
		
		if (icon || iconCls) {
			button._createIconSpan(dom);
		}
		
		return dom;
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class dorado.widget.desktop.TimeLabel
 * @extends dorado.widget.Control
 */
dorado.widget.desktop.TimeLabel = $extend(dorado.widget.Control, /** @scope dorado.widget.desktop.TimeLabel.prototype */ {
	$className: "dorado.widget.desktop.TimeLabel",
	ATTRIBUTES: /** @scope dorado.widget.desktop.TimeLabel.prototype */ {
		className: {
			defaultValue: "d-time-label"
		},
		
		/**
		 * 时间显示的格式
		 * @type String
		 * @attribute
		 */
		format: {
			defaultValue: "H:i"
		}
	},
	createDom: function() {
		var label = this, dom = document.createElement("span"), now = new Date();
		dom.className = label._className;
		
		$fly(dom).text(now.formatDate(label._format));
		
		label._refreshTimer = setInterval(function() {
			now = new Date();
			$fly(dom).text(now.formatDate(label._format));
		}, 40000);
		
		return dom;
	},

	destroy: function() {
		var label = this;
		$invokeSuper.call(label, arguments);
		clearInterval(label._refreshTimer);
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class QuickButton
 * @extends dorado.widget.SimpleIconButton
 */
dorado.widget.desktop.QuickButton = $extend(dorado.widget.SimpleIconButton, /** @scope dorado.widget.desktop.QuickButton.prototype */ {
	$className: "dorado.widget.SimpleIconButton",
	
	ATTRIBUTES: /** @scope dorado.widget.QuickButton.prototype */ {
		appId: {}
	},
	
	onClick: function() {
		$invokeSuper.call(this, arguments);
		var button = this, appId = button._appId, shell = this.getShell();
		if (shell && appId) {
			shell.launchApp(appId);
		}
	},
	
	getShell: function() {
		return this._parent.findParent(dorado.widget.desktop.Shell);
	},
	
	refreshDom: function(dom) {
		var button = this, cls = button._className, action = button._action || {}, icon = button._icon || action._icon, iconClass = button._iconClass || action._iconClass;
		
		var iconEl = dom.firstChild;
		if (icon) {
			$DomUtils.setBackgroundImage(iconEl, icon);
		} else if (iconClass) {
			iconEl.className = "icon " + iconClass;
		}
		$fly(dom).toggleClass(cls + "-disabled", !!(button._disabled || action._disabled));
		
		if (!button._parent) {
			return;
		}
		var shell = button.getShell(), appId = button._appId;
		if (appId && shell) {
			var appAttrs = shell._appAttrs[appId] || {}, iconProperty = "icon16";
			$DomUtils.setBackgroundImage(iconEl, button._icon || appAttrs[iconProperty]);
		}
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Desktop
 * @class 任务栏
 * @extends dorado.widget.Control
 */
dorado.widget.desktop.Taskbar = $extend(dorado.widget.Control, /** @scope dorado.widget.Taskbar.prototype */ {
	$className: "dorado.widget.desktop.Taskbar",
	ATTRIBUTES: /** @scope dorado.widget.Taskbar.prototype */ {
		className: {
			defaultValue: "d-task-bar"
		},
		
		/**
		 * 任务按钮。
		 * @attribute
		 * @type dorado.widget.desktop.TaskButton[]
		 */
		taskButtons: {
			innerComponent: "TaskButton",
			setter: function(value) {
				var taskbar = this, taskButtons = taskbar._taskButtons, rendered = taskbar._rendered;
				if (taskButtons) {
					taskbar.clearTaskButtons();
				}
				this._taskButtons = value;
				if (value && rendered) {
					toolbar.doRenderTaskButtons();
				}
			}
		},
		
		/**
		 * 快速启动按钮。
		 * @attribute
		 * @type dorado.widget.SimpleIconButton[]
		 */
		quickButtons: {
			innerComponent: "desktop.QuickButton",
			setter: function(value) {
				var taskbar = this, quickButtons = taskbar._quickButtons, rendered = taskbar._rendered;
				if (quickButtons) {
					taskbar.clearQuickButtons();
				}
				this._quickButtons = value;
				if (value && rendered) {
					toolbar.doRenderQuickButtons();
				}
			}
		},
		
		/**
		 * 托盘按钮。
		 * @attribute
		 * @type dorado.widget.SimpleButton[]
		 */
		trayButtons: {
			innerComponent: "SimpleButton",
			setter: function(value) {
				var taskbar = this, trayButtons = taskbar._trayButtons, rendered = taskbar._rendered;
				if (trayButtons) {
					taskbar.clearTrayButtons();
				}
				this._trayButtons = value;
				if (value && rendered) {
					toolbar.doRenderTrayButtons();
				}
			}
		},
		
		/**
		 * 是否显示时间标签。
		 * @attribute writeBeforeReady
		 * @default true
		 */
		showTimeLabel: {
			writeBeforeReady: true,
			defaultValue: true
		},

        /**
         * 激活右键菜单的TaskButton，可能会被激活的右键菜单使用。
         * @type dorado.widget.desktop.TaskButton
         * @attribute
         */
        contextMenuTaskButton: {
            readOnly: true
        },

		/**
		 *
		 * @attribute
		 * @type dorado.widget.Control
		 */
		startButton: {
			writeBeforeReady: true,
			innerComponent: ""
		}
	},
	
	EVENTS: /** @scope dorado.widget.Taskbar.prototype */ {
		/**
		 * 在TaskButton上点击右键触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.desktop.TaskButton} arg.taskButton 触发该事件的taskButton。
		 * @param {Event} arg.event DHTML中的事件event参数。
		 * @param {boolean} #arg.processDefault=false 是否要继续系统的默认操作，让系统上下文菜单显示出来。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onTaskButtonContextMenu: {}
	},
	
	createDom: function() {
		var taskbar = this, doms = {}, dom = $DomUtils.xCreateElement({
			tagName: "div",
			className: taskbar._className,
			content: [{
				tagName: "div",
				className: "right-wrap",
				contextKey: "rightWrap",
				content: [{
					tagName: "div",
					className: "tray-buttons",
					contextKey: "trayButtons"
				}]
			}, {
				tagName: "div",
				className: "left-wrap",
				contextKey: "leftWrap",
				content: [{
					tagName: "div",
					className: "quick-buttons",
					contextKey: "quickButtons"
				}, {
					tagName: "div",
					className: "task-buttons-wrap",
					contextKey: "taskButtonsWrap",
					content: [{
						tagName: "div",
						className: "task-buttons",
						contextKey: "taskButtons"
					}, {
						tagName: "div",
						className: "spinner",
						contextKey: "spinner",
						style: {
							display: "none"
						},
						content: [{
							tagName: "div",
							className: "up-button",
							contextKey: "upButton"
						}, {
							tagName: "div",
							className: "down-button",
							contextKey: "downButton"
						}]
					}]
				}]
			}]
		}, null, doms);
		
		taskbar._doms = doms;
		
		jQuery(doms.upButton).click(function() {
			var $taskButtons = $fly(doms.taskButtons), scrollTop = $taskButtons.attr("scrollTop");
			$taskButtons.attr("scrollTop", scrollTop - $taskButtons.outerHeight(true));
		}).addClassOnHover("up-button-hover").addClassOnClick("up-button-click");
		
		jQuery(doms.downButton).click(function() {
			var $taskButtons = $fly(doms.taskButtons), scrollTop = $taskButtons.attr("scrollTop");
			$taskButtons.attr("scrollTop", scrollTop + $taskButtons.outerHeight(true));
		}).addClassOnHover("down-button-hover").addClassOnClick("down-button-click");
		
		$DomUtils.disableUserSelection(doms.spinner);
		
		var startButton = taskbar._startButton;
		
		if (startButton) {
			startButton.render(dom, doms.leftWrap);
			doms.startButton = startButton._dom;
			taskbar.registerInnerControl(startButton);
		}
		
		if (taskbar._showTimeLabel) {
			var timeLabel = new dorado.widget.desktop.TimeLabel();
			timeLabel.render(doms.rightWrap);
			taskbar.registerInnerControl(timeLabel);
			doms.timeLabel = timeLabel._dom;
		}
		
		this.doRenderTaskButtons();
		this.doRenderQuickButtons();
		this.doRenderTrayButtons();
		
		$fly(doms.quickButtons).droppable({
			scope: "desktop",
			over: function(ev, ui) {
				//console.log("....");
			},
			drop: function() {
				//console.log("drop!!");
			}
		});
		
		return dom;
	},
	
	doRenderTaskButtons: function() {
		var taskbar = this, taskButtons = taskbar._taskButtons || [], doms = taskbar._doms;
		for (var i = 0, j = taskButtons.length; i < j; i++) {
			var taskButton = taskButtons[i];
			taskButton.render(doms.taskButtons);
			this.registerInnerControl(taskButton);
		}
	},
	
	doRenderQuickButtons: function() {
		var taskbar = this, quickButtons = taskbar._quickButtons || [], doms = taskbar._doms;
		for (var i = 0, j = quickButtons.length; i < j; i++) {
			var quickButton = quickButtons[i];
			taskbar.registerInnerControl(quickButton);
			quickButton.render(doms.quickButtons);
		}
	},
	
	doRenderTrayButtons: function() {
		var taskbar = this, trayButtons = taskbar._trayButtons || [], doms = taskbar._doms;
		for (var i = 0, j = trayButtons.length; i < j; i++) {
			var trayButton = trayButtons[i];
			trayButton.render(doms.trayButtons);
			this.registerInnerControl(trayButton);
		}
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		var taskbar = this, doms = taskbar._doms, startButtonWidth = $fly(doms.startButton).outerWidth(true);
		$fly(doms.leftWrap).css("margin-left", startButtonWidth);
	},
	
	onResize: function() {
		$invokeSuper.call(this, arguments);
		
		var taskbar = this, dom = taskbar._dom, doms = taskbar._doms, domWidth = $fly(dom).width(), rightWidth = $fly(doms.rightWrap).outerWidth(true), startButtonWidth = $fly(doms.startButton).outerWidth(true);
		
		var quickButtonsWidth = $fly(doms.quickButtons).outerWidth(true);
		
		$fly(doms.taskButtonsWrap).outerWidth(domWidth - rightWidth - startButtonWidth - quickButtonsWidth);
		
		taskbar.refreshTrigger();
	},
	
	/**
	 * 设置某个按钮为活动状态。
	 * @param  {dorado.widget.desktop.TaskButton} button 要设置的按钮。
	 */
	setCurrentButton: function(button) {
		var taskbar = this, taskButtons = taskbar._taskButtons;
		if (taskButtons) {
			for (var i = 0, j = taskButtons.length; i < j; i++) {
				var taskButton = taskButtons[i];
				taskButton.set("current", false);
			}
			if (button) {
				button.set("current", true);
				taskbar.scrollIntoView(button);
			}
		}
	},
	
	/**
	 * 刷新上下滚动按钮。
	 */
	refreshTrigger: function() {
		var taskbar = this, dom = taskbar._dom, doms = taskbar._doms, taskButtons = taskbar._taskButtons || [], button = taskButtons[taskButtons.length - 1];
		
		if (dom && button) {
			var buttonDom = button._dom, scrollTop = buttonDom.offsetTop, parentTop = doms.taskButtons.offsetTop;
			$fly(doms.spinner).css("display", scrollTop > parentTop ? "" : "none");
		}
	},
	
	/**
	 * 滚动指定的按钮到可视范围。
	 * @param {dorado.widget.desktop.TaskButton} button 要显示的按钮。
	 */
	scrollIntoView: function(button) {
		var taskbar = this, dom = taskbar._dom;
		if (button && dom) {
			var buttonDom = button._dom, scrollTop = buttonDom.offsetTop, taskButtonsDom = taskbar._doms.taskButtons, parentTop = taskButtonsDom.offsetTop;
			
			if (taskButtonsDom.scrollTop != scrollTop - parentTop) {
				$fly(taskButtonsDom).animate({
					scrollTop: scrollTop - parentTop
				});
			}
		}
	},
	
	/**
	 * 创建一个任务按钮。
	 * @param {dorado.widget.desktop.TaskButton|Object} button 任务按钮或者任务按钮的配置信息。
	 * @return {dorado.widget.desktop.TaskButton} 创建好的任务按钮。
	 * @private
	 */
	createTaskButton: function(button) {
		if (!button) return null;
		if (button.constructor == Object.prototype.constructor) {
			return dorado.Toolkits.createInstance("widget", button);
		} else {
			return button;
		}
	},
	
	/**
	 * 插入一个任务按钮。
	 * @param {dorado.widget.desktop.TaskButton|Object} button 要添加的按钮或者按钮的配置信息。
	 * @param {dorado.widget.desktop.TaskButton|int} index 要插入的索引，也可以是Taskbar中的某个按钮，会在该按钮之前插入。
	 */
	addTaskButton: function(button, index) {
		var taskbar = this, taskButtons = taskbar._taskButtons;
		if (!taskButtons) {
			taskButtons = taskbar._taskButtons = [];
		}
		button = taskbar.createTaskButton(button);
		var refButton, rendered = taskbar._rendered, refDom;
		if (typeof index == "number") {
			refButton = taskButtons[index];
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		} else if (index instanceof dorado.widget.desktop.TaskButton) {
			refButton = index;
			index = taskButtons.indexOf(refButton);
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		}
		if (typeof index == "number") {
			taskButtons.insert(button, index);
		} else {
			taskButtons.push(button);
		}
		if (rendered) {
			button.render(taskbar._doms.taskButtons, refDom);
		}
		taskbar.registerInnerControl(button);
		taskbar.refreshTrigger();
	},
	
	/**
	 * 删除指定的任务按钮。
	 * @param {dorado.widget.desktop.TaskButton|Number} button 要删除的任务按钮或者其索引。
	 */
	removeTaskButton: function(button) {
		var taskbar = this, taskButtons = taskbar._taskButtons, realButton = button;
		if (taskButtons) {
			if (typeof button == "number") {
				realButton = taskButtons[button];
			}
			if (realButton) {
				taskbar.unregisterInnerControl(realButton);
				taskButtons.remove(realButton);
				realButton.destroy();
			}
			taskbar.refreshTrigger();
		}
	},
	
	/**
	 * 删除所有的TaskButton。
	 */
	clearTaskButtons: function() {
		var taskbar = this, taskButtons = taskbar._taskButtons || [];
		for (var i = 0, j = taskButtons.length; i < j; i++) {
			var item = taskButtons[i];
			taskbar.unregisterInnerControl(item);
			item.destroy();
		}
		taskbar._taskButtons = null;
	},
	
	createQuickButton: function(button) {
		if (!button) return null;
		if (button.constructor == Object.prototype.constructor) {
			return new dorado.widget.desktop.QuickButton(button);
		} else {
			return button;
		}
	},
	
	/**
	 * 插入一个快速启动按钮。
	 * @param {dorado.widget.SimpleIconButton|Object} button 要添加的快速启动按钮或者快速启动按钮的配置信息。
	 * @param {dorado.widget.SimpleIconButton|int} index 要插入的索引，也可以是Taskbar中的某个启动按钮，会在该按钮之前插入。
	 */
	addQuickButton: function(button, index) {
		var taskbar = this, quickButtons = taskbar._quickButtons;
		if (!quickButtons) {
			quickButtons = taskbar._quickButtons = [];
		}
		button = taskbar.createQuickButton(button);
		var refButton, rendered = taskbar._rendered, refDom;
		if (typeof index == "number") {
			refButton = quickButtons[index];
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		} else if (index instanceof dorado.widget.AbstractButton) {
			refButton = index;
			index = quickButtons.indexOf(refButton);
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		}
		if (typeof index == "number") {
			quickButtons.insert(button, index);
		} else {
			quickButtons.push(button);
		}
		taskbar.registerInnerControl(button);
		if (rendered) {
			button.render(taskbar._doms.quickButtons, refDom);
		}
		taskbar.resetDimension();
		taskbar.refreshTrigger();
	},
	
	/**
	 * 删除指定的启动按钮。
	 * @param {dorado.widget.SimpleIconButton|Number} button 要删除的启动按钮或者其索引。
	 */
	removeQuickButton: function(button) {
		var taskbar = this, quickButtons = taskbar._quickButtons, realButton = button;
		if (quickButtons) {
			if (typeof button == "number") {
				realButton = quickButtons[button];
			}
			if (realButton) {
				taskbar.unregisterInnerControl(realButton);
				quickButtons.remove(realButton);
				realButton.destroy();
			}
			taskbar.resetDimension();
			taskbar.refreshTrigger();
		}
	},
	
	/**
	 * 删除所有的QuickButton。
	 */
	clearQuickButtons: function() {
		var taskbar = this, quickButtons = taskbar._quickButtons || [];
		for (var i = 0, j = quickButtons.length; i < j; i++) {
			var item = quickButtons[i];
			taskbar.unregisterInnerControl(item);
			item.destroy();
		}
		taskbar._quickButtons = null;
	},
	
	createTrayButton: function(button) {
		if (!button) return null;
		if (button.constructor == Object.prototype.constructor) {
			return new dorado.widget.SimpleIconButton(button);
		} else {
			return button;
		}
	},
	
	/**
	 * 插入一个托盘按钮。
	 * @param {dorado.widget.SimpleIconButton|Object} button 要添加的托盘按钮或者托盘按钮的配置信息。
	 * @param {dorado.widget.SimpleIconButton|int} index 要插入的索引，也可以是Taskbar中的某个托盘按钮，会在该按钮之前插入。
	 */
	addTrayButton: function(button, index) {
		var taskbar = this, trayButtons = taskbar._trayButtons;
		if (!trayButtons) {
			trayButtons = taskbar._trayButtons = [];
		}
		button = taskbar.createTrayButton(button);
		var refButton, rendered = taskbar._rendered, refDom;
		if (typeof index == "number") {
			refButton = trayButtons[index];
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		} else if (index instanceof dorado.widget.AbstractButton) {
			refButton = index;
			index = trayButtons.indexOf(refButton);
			if (rendered && refButton) {
				refDom = refButton._dom;
			}
		}
		if (typeof index == "number") {
			trayButtons.insert(button, index);
		} else {
			trayButtons.push(button);
		}
		if (rendered) {
			button.render(taskbar._doms.trayButtons, refDom);
		}
		taskbar.registerInnerControl(button);
		taskbar.resetDimension();
		taskbar.refreshTrigger();
	},
	
	/**
	 * 删除指定的托盘按钮。
	 * @param {dorado.widget.SimpleIconButton|Number} button 要删除的托盘按钮或者其索引。
	 */
	removeTrayButton: function(button) {
		var taskbar = this, trayButtons = taskbar._trayButtons, realButton = button;
		if (trayButtons) {
			if (typeof button == "number") {
				realButton = trayButtons[button];
			}
			if (realButton) {
				taskbar.unregisterInnerControl(realButton);
				trayButtons.remove(realButton);
				realButton.destroy();
			}
			taskbar.resetDimension();
			taskbar.refreshTrigger();
		}
	},
	
	/**
	 * 删除所有的QuickButton。
	 */
	clearTrayButtons: function() {
		var taskbar = this, trayButtons = taskbar._trayButtons || [];
		for (var i = 0, j = trayButtons.length; i < j; i++) {
			var item = trayButtons[i];
			taskbar.unregisterInnerControl(item);
			item.destroy();
		}
		taskbar._trayButtons = null;
	}
});
