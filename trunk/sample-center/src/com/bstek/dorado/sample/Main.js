// @Bind #treeExamples.onDataRowClick
!function(self, frameExample) {
	var nodeType = self.get("currentNode.bindingConfig.name"), url;
	if (nodeType == "Category") {
		url = "com.bstek.dorado.sample.CategorySummary.d?categoryId=";
	} else {
		url = "com.bstek.dorado.sample.ExampleSummary.d?exampleId=";
	}
	url += self.get("currentNode.data.id");
	frameExample.set("path", url);
}

// @Bind #treeExamples.onRenderNode
!function(arg) {
	var node = arg.node, data = node.get("data");
	arg.dom.style.fontWeight = (data.get("hot") ? "bold" : "");
	if (data.get("new")) {
		$(arg.dom).empty().xCreate([ {
			tagName : "SPAN",
			contentText : node.get("label")
		}, {
			tagName : "IMG",
			src : $url(">images/new.gif"),
			style : "position: relative; left: 4px; top: 4px"
		} ]);
	} else {
		arg.processDefault = true;
	}
}

// @Bind #treeExamples.onNodeAttached
!function(arg) {
	var node = arg.node, data = node.get("data");
	node.set("icon", data.get("icon"));
}

// @Bind #buttonAdmin.onClick
!function() {
	open($url(">com.bstek.dorado.sample.admin.ExampleMaintain.d"));
}