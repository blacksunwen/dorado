//@Bind view.onReady
!function(self, arg) {
	view.get('#loginDialog').show();
}

// @Bind #loginAction.beforeExecute
!function(self, arg) {
	var password = view.get('#user_password').get("value");
	var name = view.get('#user_name').get('value');
	self.set('parameter', {
		name : name,
		password : password
	});

}
// @Bind #loginAction.onSuccess
!function(self, arg) {
	if (true === arg.result) {
		window.location.reload();
	} else {
		dorado.widget.NotifyTipManager.notify("${res.failed_login}");
	}
}
