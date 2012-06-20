var dirtyEntityNum = 0;

function refreshActions() {
	var node = view.get("#treeExamples.currentNode");
	view.set({
		"^onCurrentNode.disabled" : node == null,
		"^onCurrentCategory.disabled" : (node == null || node
				.get("bindingConfig.name") != "Category"),
		"^onCurrentExample.disabled" : (node == null || node
				.get("bindingConfig.name") != "Example")
	});

	var disabled = true;
	if (node != null) {
		var entity = node.get("data");
		disabled = !entity.isDirty();
	}
	view.set({
		"^onDirtyNode.disabled" : disabled,
		"^onDirty.disabled" : dirtyEntityNum == 0
	});
}

// @Bind @CategoryNode.onInsert
// @Bind @ExampleNode.onInsert
// @Bind @ExampleSource.onInsert
!function(arg) {
	var entityList = arg.entityList;
	var maxSortFlag = 0;
	entityList.each(function(entity) {
		var sortFlag = entity.get("sortFlag");
		if (sortFlag <= maxSortFlag) {
			maxSortFlag++;
			entity.set("sortFlag", maxSortFlag);
		} else {
			maxSortFlag = sortFlag;
		}
	});
}

// @Bind @CategoryNode.onRemove
// @Bind @ExampleNode.onRemove
// @Bind @ExampleSource.onRemove
!function(arg) {
	arg.entity.set("sortFlag", 0);
}

// @Bind @CategoryNode.onStateChange
// @Bind @ExampleNode.onStateChange
// @Bind @ExampleSource.onStateChange
!function(arg) {
	if (arg.oldState == dorado.Entity.STATE_NONE) {
		dirtyEntityNum++;
	} else if (arg.newState == dorado.Entity.STATE_NONE) {
		dirtyEntityNum--;
	} else if (arg.oldState == dorado.Entity.STATE_NEW
			&& arg.newState == dorado.Entity.STATE_DELETED) {
		dirtyEntityNum--;
	}
	refreshActions();
}

// @Bind #actionAddCategory.onExecute
!function(dsCategories, treeExamples) {
	var parentCategory = dsCategories.getData("!CURRENT_CATEGORY");
	var data = {
		label : "<新分类>"
	};
	if (parentCategory) {
		parentCategory.createChild("categories", data);
	} else {
		dsCategories.getData().createChild(data);
	}

	var currentNode = treeExamples.get("currentNode");
	if (currentNode) {
		currentNode.expandAsync();
	}
}

// @Bind #actionAddExample.onExecute
!function(dsCategories, treeExamples) {
	var parentCategory = dsCategories.getData("!CURRENT_CATEGORY");
	var data = {
		label : "<新示例>"
	};
	if (parentCategory) {
		parentCategory.createChild("examples", data);

		var currentNode = treeExamples.get("currentNode");
		if (currentNode) {
			currentNode.expandAsync();
		}
	} else {
		var currentExample = dsCategories.getData("!CURRENT_EXAMPLE");
		if (currentExample) {
			currentExample.createBrother(data);
		}
	}
}

// @Bind #actionLinkExample.onExecute
!function(dialogSelectExample) {
	dialogSelectExample.show();
}

// @Bind #actionRemove.onExecute
!function(dsCategories) {
	var entity = dsCategories.getData("!CURRENT_NODE");
	if (entity) {
		entity.remove();
	}
}

// @Bind #actionCancel.onExecute
!function(dsCategories) {
	var entity = dsCategories.getData("!CURRENT_NODE");
	if (entity) {
		entity.cancel();
	}
}

// @Bind #actionSave.onSuccess
!function() {
	dirtyEntityNum = 0;
	refreshActions();
}

// @Bind #treeExamples.onReady
!function(self, CategoryNode, ExampleNode) {
	dorado.DataPath.registerInterceptor("CURRENT_CATEGORY", function(data) {
		var entity = self.get("currentNode.data");
		return (entity && entity.dataType == CategoryNode) ? entity : null;
	}, function(dataType) {
		return CategoryNode;
	});
	dorado.DataPath.registerInterceptor("CURRENT_EXAMPLE", function(data) {
		var entity = self.get("currentNode.data");
		return (entity && entity.dataType == ExampleNode) ? entity : null;
	}, function(dataType) {
		return ExampleNode;
	});
}

// @Bind #treeExamples.onCurrentChange
!function(self, cardbook) {
	refreshActions();
	var nodeType = self.get("currentNode.bindingConfig.name"), index = 0;
	if (nodeType)
		index = (nodeType == "Category") ? 1 : 2;
	cardbook.set("currentControl", index);
}

// @Bind #treeExamples.onRenderNode
!function(arg) {
	var entity = arg.node.get("data");
	if (entity.isDirty()) {
		var img = ">images/"
				+ ((entity.state == dorado.Entity.STATE_NEW) ? "new-node.gif"
						: "modified-node.gif");
		arg.dom.innerHTML = arg.node.get("label") + "&nbsp;<img src=\""
				+ $url(img) + "\">";
		arg.processDefault = false;
	} else {
		arg.processDefault = true;
	}
}

// @Bind #treeExamples.onDataNodeCreate
!function(self, arg) {
	var entity = arg.data;
	if (entity.state == dorado.Entity.STATE_NEW) {
		setTimeout(function() {
			self.set("currentNode", arg.node);
		}, 50);
	}
}

// @Bind #treeExamples.onContextMenu
!function(menuTree) {
	menuTree.show({
		position : {
			left : arg.event.pageX,
			top : arg.event.pageY
		}
	});
}

// @Bind #treeExamples.onDraggingSourceMove
!function(arg) {
	var draggingInfo = arg.draggingInfo;
	var targetNode = draggingInfo.get("targetObject");
	if (targetNode) {
		var accept = (targetNode.get("bindingConfig.name") == "Category");
		draggingInfo.set("accept", accept);
	}
}

// @Bind #buttonOk.onClick
!function(treeCategories, treeExamples, dialogSelectExample) {
	var category = treeCategories.get("currentNode.data");
	if (!category) {
		dorado.MessageBox.alert("请选择一个有效的分类。");
		return;
	}

	var currentCategoryNode = treeExamples.get("currentNode");
	if (currentCategoryNode.get("bindingConfig.name") == "Example")
		currentCategoryNode = currentCategoryNode.get("parent");
	if (category == currentCategoryNode.get("data")) {
		dorado.MessageBox.alert("请选择一个不同的分类。");
		return;
	}

	var example = treeExamples.get("currentNode.data");
	example = example.clone();
	example.set("sortFlag", 0);
	category.get("examples").insert(example);
	dialogSelectExample.hide();
}

// @Bind #buttonCancel.onClick
!function(dialogSelectExample) {
	dialogSelectExample.hide();
}
