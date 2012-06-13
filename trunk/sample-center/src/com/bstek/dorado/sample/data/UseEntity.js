// @Bind #actionExecute.onExecute
!function(dsExamples, inputScript, treeExamples) {
	try {
		var script = inputScript.get("text");
		if (!script) {
			throw new dorado.Exception("请先输入或选择一段要测试的脚本！");
		}

		var currentEntity = treeExamples.get("currentEntity");
		if (!currentEntity) {
			throw new dorado.Exception("请先选中一个树节点！");
		}

		var list = dsExamples.get("data");
		eval(script);
	} catch (e) {
		dorado.Exception.processException(e);
	}
}

// @Bind #listboxScripts.onReady
!function(self) {
	$ajax.request(
			">dorado/res/com/bstek/dorado/sample/data/UseEntityScripts.js",
			function(result) {
				self.set("items", result.getJsonData());
			});
}

// @Bind #listboxScripts.onDataRowClick
!function(self, inputScript) {
	inputScript.set("text", self.get("currentItem.script"));
	self.highlightItem();
}

// @Bind #listboxScripts.onRenderRow
!function(arg) {
	var script = arg.data.script || "";
	if (script.length > 80)
		script = script.substring(0, 80) + "...";

	$(arg.dom).empty().xCreate([ {
		tagName : "DIV",
		contentText : arg.data.description,
		style : "font-weight: bold; color: navy"
	}, {
		tagName : "SPAN",
		contentText : script,
		style : "color: gray"
	} ]);
}

// @Bind #buttonReset.onClick
!function(dsExamples) {
	dsExamples.flushAsync();
}

// @Bind #inputScript.onReady
!function(self) {
	$(self.getDom()).find(".textarea").css("font-size", "10pt");
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

//@Bind #treeExamples.onNodeAttached
!function(arg) {
	var node = arg.node, data = node.get("data");
	node.set("icon", data.get("icon"));
}