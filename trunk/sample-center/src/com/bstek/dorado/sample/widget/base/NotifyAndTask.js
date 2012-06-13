var taskNo = 0, notifyNo = 0;

// @Bind #buttonModelessIndicator.onClick
!function() {
	taskNo++;
	var taskId = dorado.util.TaskIndicator.showTaskIndicator("测试后台任务" + taskNo);
	setTimeout(function() {
		dorado.util.TaskIndicator.hideTaskIndicator(taskId);
	}, Math.random() * 3000 + 3000);
}

// @Bind #buttonModelIndicator.onClick
!function(self) {
	taskNo++;
	self.set("disabled", true);
	var taskId = dorado.util.TaskIndicator.showTaskIndicator("测试主任务" + taskNo,
			"main");
	setTimeout(function() {
		dorado.util.TaskIndicator.hideTaskIndicator(taskId);
		self.set("disabled", false);
	}, Math.random() * 500 + 500);
}

// @Bind #buttonNotify.onClick
!function() {
	notifyNo++;
	dorado.widget.NotifyTipManager.notify("通知信息" + notifyNo);
}

// @Bind #buttonCustomNotify.onClick
!function(self) {
	
	function getMessage(num) {
		return "您有" + num + "条带处理的消息...";
	}

	self.set("disabled", true);

	var i = 1;
	var tip = dorado.widget.NotifyTipManager.notify({
		text : getMessage(i),
		caption : "<重要通知>",
		icon : "INFO",
		autoHide : false,
		listener : {
			onHide : {
				fn : function() {
					tip = null;
					self.set("disabled", false);
				}
			}
		}
	});

	var timerId = setInterval(function() {
		if (!tip) {
			clearInterval(timerId);
			return;
		}
		tip.set("text", getMessage(++i));
	}, 750);

}