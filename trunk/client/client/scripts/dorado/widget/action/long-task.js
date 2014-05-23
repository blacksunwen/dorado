dorado.widget.LongTask = $extend(dorado.widget.Action, /** @scope dorado.widget.LongTask.prototype */ {
	$className: "dorado.widget.LongTask",

	ATTRIBUTES: /** @scope dorado.widget.LongTask.prototype */
	{
		taskName: {
		},
		socket: {
		},
		appearence: {
			defaultValue: "daemonTask",
			writeBeforeReady: true
		},
		stateInfo: {},
		lastLog: {}
	},

	EVENTS: /** @scope dorado.widget.LongTask.prototype */
	{
		onTaskScheduled: {},

		onTaskEnd: {},

		onStateChange: {},

		onLog: {}
	},

	onReady: function () {
		$invokeSuper.call(this);
		this.connect();
	},

	onReceive: function (type, data) {
		var task = this;
		if (task._starting) {
			if (!task._pendingMessages) {
				task._pendingMessages = [];
			}
			task._pendingMessages.push({
				type: type,
				data: data
			});
		}
		else {
			switch (type) {
				case "state":
				{
					task.onStateChange(data);
					break;
				}
				case "log":
				{
					task.onLog(data);
					break;
				}
			}
		}
	},

	getStateDisplayInfo: function (stateInfo) {
		var task = this, text = stateInfo.text;
		if (!text) {
			var resKey = stateInfo.state.substring(0, 1).toUpperCase() + stateInfo.state.substring(1);
			text = (task._caption || $resource("dorado.baseWidget.LongTask")) + " [" + $resource("dorado.baseWidget.LongTask" + resKey) + "]";
		}
		return {
			text: text,
			startTime: (stateInfo.localStartTime) ? new Date(stateInfo.localStartTime) : new Date()
		};
	},

	onTaskScheduled: function (stateInfo) {
		var task = this;
		task._isAlive = true;

		var displayInfo = task.getStateDisplayInfo(stateInfo);
		switch (task._appearence) {
			case "daemonTask":
			{
				task._taskIndicatorId = dorado.util.TaskIndicator.showTaskIndicator(displayInfo.text, "daemon", displayInfo.startTime);
				break;
			}
			case "mainTask":
			{
				task._taskIndicatorId = dorado.util.TaskIndicator.showTaskIndicator(displayInfo.text, "main", displayInfo.startTime);
				break;
			}
		}
		task._sysDisabled = true;
		task.notifyBindingObjects();
		task.fireEvent("onTaskScheduled", task, stateInfo);
	},

	onTaskEnd: function (stateInfo) {
		var task = this, state = stateInfo.state;
		task._isAlive = false;
		task.fireEvent("onTaskEnd", task, stateInfo);

		var success = (state == "terminated"), eventArg = {
			success: success
		};
		
		var result = stateInfo.data;
		if (!eventArg.success) {
			result = new dorado.RemoteException(result.message, result.exceptionType, result.stackTrace);
		}
		else {
			task._returnValue = result;
		}
		
		eventArg[eventArg.success ? "result" : "error"] = result;
		task.fireEvent("onExecute", task, eventArg);
		eventArg.processDefault = true;
		task.fireEvent((success) ? "onSuccess" : "onFailure", task, eventArg);

		task._sysDisabled = false;
		task.notifyBindingObjects();
		
		if (!success && !eventArg.processDefault) {
			dorado.Exception.removeException(result);
		}

		switch (task._appearence) {
			case "daemonTask":
			case "mainTask":
			{
				dorado.util.TaskIndicator.hideTaskIndicator(task._taskIndicatorId);
				break;
			}
		}
	},

	onStateChange: function (stateInfo) {
		var task = this;

		if (stateInfo) {
			var remoteStartTime = (stateInfo.state == "waiting") ? stateInfo.waitingStartTime : stateInfo.runningStartTime; 
			var timeGap = stateInfo.transferTimestamp - (new Date()).getTime();
			stateInfo.localStartTime = remoteStartTime - timeGap;
		}
		
		task._stateInfo = stateInfo;
		task.fireEvent("onStateChange", task, stateInfo);

		if (stateInfo) {
			var state = stateInfo.state;
			if (["terminated", "error", "aborted"].indexOf(state) >= 0) {
				if (task._isAlive) {
					task.onTaskEnd(stateInfo);
				}
			}
			else if (!task._isAlive) {
				task.onTaskScheduled(stateInfo);
			}

			switch (task._appearence) {
				case "daemonTask":
				case "mainTask":
				{
					var displayInfo = task.getStateDisplayInfo(stateInfo);
					dorado.util.TaskIndicator.updateTaskIndicator(task._taskIndicatorId, displayInfo.text, displayInfo.startTime);
					break;
				}
			}
		}
	},

	onLog: function (log) {
		var task = this;
		task._lastLog = log;
		task.fireEvent("onLog", task, log);
		
		var stateInfo = task._stateInfo;
		if (stateInfo) {
			switch (task._appearence) {
				case "daemonTask":
				case "mainTask":
				{
					var displayInfo = task.getStateDisplayInfo(stateInfo);
					dorado.util.TaskIndicator.updateTaskIndicator(task._taskIndicatorId, displayInfo.text + "\n" + log.text);
					break;
				}
			}
		}
	},

	connect: function () {
		var task = this;
		var socket = task._socket = dorado.Socket.connect({
			service: "dorado.connectLongTask",
			parameter: task._taskName,
			onReceive: function (self, arg) {
				task.onReceive(arg.type, arg.data);
			}
		}, function (stateInfo) {
			if (stateInfo != null) {
				task.onStateChange(stateInfo);
			}
		});
	},

	getAjaxOptions: function (service) {
		var task = this, jsonData = {
			action: "remote-service",
			service: service,
			parameter: {
				taskName: task._taskName,
				socketId: task._socket._socketId,
				parameter: dorado.JSON.evaluate(task._parameter)
			},
			sysParameter: task._sysParameter ? task._sysParameter.toJSON() : undefined,
			context: (task._view ? task._view.get("context") : null)
		};
		if (this._supportsEntity) {
			jsonData.loadedDataTypes = task.get("dataTypeRepository").getLoadedDataTypes();
		}
		return dorado.Object.apply({
			jsonData: jsonData,
			batchable: true
		}, $setting["ajax.remoteServiceOptions"]);
	},

	doStart: function (callback) {
		var task = this;
		var ajaxOptions = task.getAjaxOptions("dorado.startLongTask"), ajax = dorado.util.AjaxEngine.getInstance(ajaxOptions);
		task._starting = true;
		ajax.request(ajaxOptions, {
			callback: function (success, result) {
				try {
					result = result.getJsonData();
					if (success) {
						task.bind("onExecute", function (self, arg) {
							task.unbind("onExecute", arguments.callee);
							$callback(callback, arg.success, arg.result);
						});
						
						task.onStateChange({
							state: "waiting"
						});
					} else {
						$callback(callback, false, result && result.exception);
					}
				}
				finally {
					task._starting = false;					
					if (task._pendingMessages) {
						var messages = task._pendingMessages;
						delete task._pendingMessages;
						setTimeout(function() {
							messages.each(function(message) {
								task.onReceive(message.type, message.data);
							});
						}, 0);
					}
				}
			}
		});
	},

	checkSocket: function(callback) {
		var task = this;
		if (!task._socket) {
			setTimeout(function() {
				task.checkSocket(callback);
			}, 30);
		}
		else {
			callback();
		}
	},

	execute: function (callback) {
		var task = this, eventArg = {
			processDefault: true
		};
		task.fireEvent("beforeExecute", task, eventArg);
		if (eventArg.processDefault) {
			if (task._confirmMessage) {
				dorado.MessageBox.confirm(task._confirmMessage, function () {
					task.checkSocket(function() {
						task.doStart(callback);
					});
				});
			} else {
				task.checkSocket(function() {
					task.doStart(callback);
				});
			}
		}
	},

	start: function(callback) {
		this.execute(callback);
	},

	abort: function(callback) {
		var task = this;
		task.checkSocket(function() {
			task.bind("onTaskEnd", function (self, arg) {
				task.unbind("onTaskEnd", arguments.callee);
				$callback(callback, (arg.state == "aborted"), arg.data);
			});
			task._socket.send("abort");
		});
	}
});