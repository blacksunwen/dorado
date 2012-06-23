// @Bind ^draggingSource.onGetDraggingIndicator
!function(self, arg) {
	arg.indicator.set("content", $(self.getDom()).clone().css("background", "gray"));
}

// @Bind ^dropTarget.onDraggingSourceDrop
!function(self, arg) {
	var sum = parseInt(self.get("userData")) || 0;
	var draggingNum = parseInt(arg.draggingInfo.get("object.text"));
	var label = self.get("children").toArray()[0];
	sum += draggingNum;
	label.set("text", "合计: " + sum);
	self.set("userData", sum);
	$(label.getDom()).effect("highlight", { color: "#FF8000" });
}

// @Bind #panel3.onDraggingSourceMove
!function(self, arg) {
	var sum = parseInt(self.get("userData")) || 0;
	var draggingNum = parseInt(arg.draggingInfo.get("object.text"));
	arg.draggingInfo.set("accept", (sum + draggingNum) <= 10);
}