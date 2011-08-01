function setUpPage() {
	Test.preloadAsyncTests([AsyncActionChanged]);
}

var AsyncActionChanged = {
	load: function() {
		Test.asyncProcBegin("AsyncActionChanged");
		TestActionButton = $extend([dorado.widget.Control, dorado.widget.ActionSupport], {
			refreshCounter: 0,
			
			onActionStateChanged: function() {
				this.refreshCounter++;
				Test.asyncProcEnd("AsyncActionChanged");
			}
		});
		
		var action = this.action = new dorado.widget.Action();
		
		this.button = new TestActionButton({
			action: action
		});
		action.set("label", "label1");
		action.set("icon", "icon1");
	},
	
	run: function() {
		assertEquals(1, this.button.refreshCounter);
	}
};

function testAsyncActionChanged() {
	AsyncActionChanged.run();
}
