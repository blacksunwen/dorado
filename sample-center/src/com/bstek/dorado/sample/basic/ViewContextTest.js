function refreshFormViewContext() {
	view.set("#formViewContext.entity", {
		attr1: view.get("context.attr1"),
		attr2: view.get("context.attr2"),
		attr3: view.get("context.attr3")
	});
}

// @Bind view.onReady
!function() {
	view.set("context.attr2", "some text");
	refreshFormViewContext();
}

// @Bind #actionAjax.onSuccess
!function() {
	refreshFormViewContext();
}