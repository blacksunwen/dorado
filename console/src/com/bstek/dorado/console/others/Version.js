// @Bind view.onReady
!function(self, arg) {
	userData = view.get("userData");
	view.get('#core_version').set("text", userData.core_version);
	view.get('#console_version').set("text", userData.console_version);
}
