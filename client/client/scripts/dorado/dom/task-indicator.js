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

/**
 * @class 任务指示器。
 * <p>
 * 任务提示器要正确执行就要先定义任务组，任务组的目的是为了将相似任务的信息合并到同一个任务提示框中。
 * </p>
 * <p>
 * 任务提示器已经预先定义好下列两个任务组:
 * 1. daemon:取义幽灵线程，用于显示大部分的Ajax后台操作。
 * 2. main:取义主线程，用于显示数据保存等操作，此类操作大都在执行时需要屏蔽用户的操作。
 * </p>
 * @static
 */
dorado.util.TaskIndicator = {

	idseed: 0,
	
	_taskGroups: {},
	
	/**
	 * 注册一个任务提示组。
	 * @param {String} groupName 任务组的名称。
	 * @param {Object} [options] 任务组显示选项。
	 * @param {String} options.className 该组的任务提示面板使用的className。
	 * @param {Object} options.showOptions 该组的任务提示面板使用的显示选项，参考$DomUtils.dockAround方法的的options参数。
	 * @param {boolean} options.modal 任务提示组是否模态，默认非模态。
	 * @param {String} options.caption 任务提示框上的文字，默认为"正在执行${taskNum}个后台任务..."，${taskNum}为被替换值。
	 * @param {String} options.executeTimeCaption 任务执行时间的提示文字，默认为"${taskExecuteTime}秒“，${taskExecuteTime}是被替换值。
	 * <p>
	 * 	此处的提示文字中可用特殊的占位符表示下列显示元素：${taskNum}	- 任务数。
	 * 	caption的示例：正在执行${taskNum}个后台任务...，显示结果可能为：正在执行2个后台任务...
	 * </p>
	 * @param {int} options.maxLines 该组的任务提示面板使用的className。
	 */
	registerTaskGroup: function(groupName, options) {
		var indicator = this, taskGroups = indicator._taskGroups;
		if (taskGroups[groupName]) {
			//task has registered already.
		} else {
			options = options || {};
			taskGroups[groupName] = options;
		}
	},
	
	/**
	 * 显示任务提示框。
	 * @example
	 * var taskId = dorado.util.TaskIndicator.showTaskIndicator("保存所有数据");
	 *  command.execute(function() {
	 * 	dorado.util.TaskIndicator.hideTaskIndicator(taskId);
	 * });
	 * @param {String} [taskInfo] 任务描述信息。如果不定义则显示一个默认的任务名称。
	 * @param {String} [groupName] 任务组的名称。如果不定义此参数，默认值为daemon。
	 * @return {String} taskId 任务id。
	 */
	showTaskIndicator: function(taskInfo, groupName) {
		var indicator = this, taskGroups = indicator._taskGroups, taskGroupConfig;
		groupName = groupName || "daemon";
		taskGroupConfig = taskGroups[groupName];
		
		if (taskGroupConfig) {
			var groupPanel = taskGroupConfig.groupPanel;
			
			if (!groupPanel) {
				groupPanel = taskGroupConfig.groupPanel = new dorado.util.TaskGroupPanel(taskGroupConfig);
			}
			
			var taskId = groupName + "@" + ++indicator.idseed;
			groupPanel.show();
			groupPanel.addTask(taskInfo, taskId);
			
			return taskId;
		} else {
			//no register.
			return null;
		}
	},
	
	/**
	 * 隐藏任务提示框。
	 * @param {String} taskId 任务id。
	 */
	hideTaskIndicator: function(taskId) {
		var indicator = this, taskGroups = indicator._taskGroups, taskGroupName, taskGroupConfig;
		
		taskGroupName = taskId.substring(0, taskId.indexOf("@"));
		taskGroupConfig = taskGroups[taskGroupName];
		
		if (taskGroupConfig) {
			var groupPanel = taskGroupConfig.groupPanel;
			if (groupPanel) {
				groupPanel.removeTask(taskId);
			}
		} else {
			//no register.
		}
	}
	
};

dorado.util.TaskIndicator.registerTaskGroup("main", {
	showOptions: {
		align: "center",
		vAlign: "center"
	},
	modal: true,
	className: "i-main-task-indicator d-main-task-indicator"
});

dorado.util.TaskIndicator.registerTaskGroup("daemon", {
	showOptions: {
		align: "innerright",
		vAlign: "innertop",
		offsetLeft: -15,
		offsetTop: 15
	},
	className: "i-daemon-task-indicator d-daemon-task-indicator"
});

/**
 * @class TaskGroupPanel
 * <p>
 * 一般情况下，不需要单独使用该类。该类被TaskIndicator中的每个Group使用，每个Group对应一个TaskGroupPanel.
 * </p>
 */
dorado.util.TaskGroupPanel = $extend(dorado.RenderableElement, { /** @scope dorado.util.TaskGroupPanel.prototype */
	$className: "dorado.util.TaskGroupPanel",
	
	_inherentClassName: "i-task-group-panel",
	tasks: null,
	taskGroupConfig: null,
	_intervalId: null,
	
	ATTRIBUTES: /** @scope dorado.util.TaskGroupPanel.prototype */ {
		className: {
			defaultValue: "d-task-group-panel"
		}
	},
	
	constructor: function(taskGroupConfig) {
		$invokeSuper.call(this);
		var panel = this;
		if (!taskGroupConfig) {
			throw new dorado.Exception("taskGrooupRequired");
		}
		panel.taskGroupConfig = taskGroupConfig;
		
		panel.tasks = new dorado.util.KeyedArray(function(object) {
			return object.taskId;
		});
	},
	
	createDom: function() {
		var panel = this, dom, doms = {}, taskGroupConfig = panel.taskGroupConfig;
		dom = $DomUtils.xCreate({
			tagName: "div",
			className: panel._className,
			content: [{
				tagName: "div",
				className: "panel-header-left",
				contextKey: "header",
				content: {
					tagName: "div",
					className: "panel-header-right"
				}
			}, {
				tagName: "div",
				className: "panel-body-left",
				contextKey: "body",
				content: {
					tagName: "div",
					className: "panel-body-right",
					content: [{
						tagName: "div",
						className: "panel-body",
						contextKey: "body",
						content: {
							tagName: "div",
							className: "content-panel",
							contextKey: "contentPanel",
							content: [{
								tagName: "div",
								className: "count-info",
								contextKey: "countInfo"
							}, {
								tagName: "ul",
								className: "task-list",
								contextKey: "taskList",
								content: {
									tagName: "li",
									className: "more",
									content: "... ... ...",
									contextKey: "more",
									style: "display: none"
								}
							}]
						}
					}]
				}
			}, {
				tagName: "div",
				className: "panel-footer-left",
				contextKey: "footer",
				content: {
					tagName: "div",
					className: "panel-footer-right"
				}
			}]
		}, null, doms);
		
		panel._doms = doms;
		
		var $dom = $fly(dom);
		$dom.addClass(taskGroupConfig.className);
		if (!dorado.Browser.msie || dorado.Browser.version >= 9) {
			$dom.shadow({
				mode: "sides"
			});
		}
		
		taskGroupConfig.caption = taskGroupConfig.caption ? taskGroupConfig.caption : $resource("dorado.core.DefaultTaskCountInfo");
		taskGroupConfig.executeTimeCaption = taskGroupConfig.executeTimeCaption ? taskGroupConfig.executeTimeCaption : $resource("dorado.core.DefaultTaskExecuteTime");
		
		return dom;
	},
	
	/**
	 * 添加任务
	 * @param {String} taskInfo 任务的提示信息
	 * @param {int} taskId 任务Id。
	 */
	addTask: function(taskInfo, taskId) {
		var panel = this, taskGroupConfig = panel.taskGroupConfig, listDom = panel._doms.taskList, li = $DomUtils.xCreate({
			tagName: "li",
			className: "task-item",
			content: [{
				tagName: "span",
				className: "interval-span",
				content: taskGroupConfig.executeTimeCaption.replace("${taskExecuteTime}", "0")
			}, {
				tagName: "span",
				className: "caption-span",
				content: taskInfo
			}]
		});
		
		if (panel.tasks.size >= (panel.taskGroupConfig.showOptions.maxLines || 3)) {
			li.style.display = "none";
			panel._doms.more.style.display = "";
		}
		listDom.insertBefore(li, panel._doms.more);
		
		if (panel.tasks.size == 0) {
			panel._intervalId = setInterval(function() {
				panel.refreshInterval();
			}, 500);
		}
		
		panel.tasks.append({
			taskId: taskId,
			dom: li,
			startTime: new Date().getTime()
		});
		
		$fly(panel._doms.countInfo).text(taskGroupConfig.caption.replace("${taskNum}", panel.tasks.size));
	},
	
	/**
	 * 移除任务。
	 * @param {int} taskId 分配给该任务的id。
	 */
	removeTask: function(taskId) {
		var panel = this, target = panel.tasks.get(taskId), taskGroupConfig = panel.taskGroupConfig;
		if (target) {
			setTimeout(function() {
				$fly(target.dom).remove();
				panel.tasks.remove(target);
				
				var maxLines = panel.taskGroupConfig.showOptions.maxLines || 3;
				if (panel.tasks.size > maxLines) {
					var i = 0;
					panel.tasks.each(function(task) {
						task.dom.style.display = "";
						if (++i == maxLines) return false;
					});
				}
				else {
					panel._doms.more.style.display = "none";
					if (panel.tasks.size == 0) {
						clearInterval(panel._intervalId);
						panel._intervalId = null;
						panel.hide();
					}
					else {
						panel.tasks.each(function(task) {
							task.dom.style.display = "";
						});
					}
				}
				$fly(panel._doms.countInfo).text(taskGroupConfig.caption.replace("${taskNum}", panel.tasks.size));
			}, 500);
		}
	},
	
	/**
	 * 刷新所有正在执行任务的执行时间。
	 * @protected
	 */
	refreshInterval: function() {
		var panel = this, time = new Date().getTime();
		panel.tasks.each(function(task) {
			var el = task.dom, startTime = task.startTime;
			if (el && startTime) {
				var interval = parseInt((time - startTime) / 1000, 10);
				$fly(el).find(".interval-span").text(panel.taskGroupConfig.executeTimeCaption.replace("${taskExecuteTime}", interval));
			}
		});
	},
	
	/**
	 * 显示任务面板
	 * @param {Object} options 注册任务组的时候的配置信息中的showOptions选项。
	 */
	show: function(options) {
		var panel = this, taskGroupConfig = panel.taskGroupConfig;
		options = options || taskGroupConfig.showOptions;
		if (panel._hideTimer) {
			clearTimeout(panel._hideTimer);
			panel._hideTimer = null;
			return;
		}
		if (!panel._rendered) {
			panel.render(document.body);
		} else {
			$fly(panel._dom).css("display", "").css("visibility", "");
		}
		if (panel.tasks.size == 0 && taskGroupConfig.modal) {
			dorado.ModalManager.show(panel._dom);
		}
		$fly(panel._dom).bringToFront();
		$DomUtils.dockAround(panel._dom, document.body, options);
	},
	
	/**
	 * 隐藏任务面板
	 */
	hide: function() {
		var panel = this;
		if (panel._rendered) {
			var taskGroupConfig = panel.taskGroupConfig;
			jQuery(panel._dom).css("display", "none").css("visibility", "hidden");
			if (taskGroupConfig.modal) {
				dorado.ModalManager.hide(panel._dom);
			}
		}
	}
});
