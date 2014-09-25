// @Bind view.onCreate
!function(self, arg) {
	var userData = self.get('userData'),initData;
	initData = userData.initData;
	cloudo.run(self,initData);
}