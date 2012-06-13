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

/** @Bind #button1.onClick */
!function(button2) {
	button2.set("toggled", !button2.get("toggled"));
}

/** @Bind ^numberButton.onClick */
!function(self) {
	showMessage("您单击了\"" + self.get("caption") + "\"");
}