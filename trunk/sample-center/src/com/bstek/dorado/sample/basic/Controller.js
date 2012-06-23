/**
 * @View
 * @Global
 */
function showMessage(text) {
	dorado.widget.NotifyTipManager.notify(text);
}

/** @Bind view.onReady */
!function() {
	showMessage("\"View.onReady\"事件触发了，此监听器由Controller完成绑定。");
}

/**
 * @Bind #button1.onClick
 * @Bind #button2.onClick
 */
!function(self) {
	showMessage("您单击了\"" + self.get("caption") + "\"");
}

/** @Bind #button1.onClick */
!function(editor1) {
	editor1.set("text", "您单击了button1");
}

/** @Bind #button2.onClick */
!function(editor1) {
	editor1.set("text", "您单击了button2");
}

/** @Bind ^numberButton.onClick */
!function(self) {
	showMessage("您单击了\"" + self.get("caption") + "\"");
}