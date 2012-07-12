// @Controller

// @Bind #buttonCollapseAll.onClick
!function(treeGrid) {
	function collapse(nodes) {
		nodes.each(function(node) {
			collapse(node.get("nodes"));
			if (node.get("expanded"))
				node.collapse();
		});
	}

	collapse(treeGrid.get("nodes"));
}

// @Bind #buttonExpandAll.onClick
!function(treeGrid) {
	function expand(nodes) {
		nodes.each(function(node) {
			if (!node.get("expanded"))
				node.expand();
			expand(node.get("nodes"));
		});
	}

	expand(treeGrid.get("nodes"));
}

// @Bind #buttonSelectAll.onClick
!function(treeGrid) {
	function select(nodes) {
		nodes.each(function(node) {
			select(node.get("nodes"));
			node.set("checked", true);
		});
	}

	select(treeGrid.get("nodes"));
}

// @Bind #buttonUnselectAll.onClick
!function(treeGrid) {
	function unselect(nodes) {
		nodes.each(function(node) {
			unselect(node.get("nodes"));
			node.set("checked", false);
		});
	}

	unselect(treeGrid.get("nodes"));
}

// @Bind #buttonShowSelection.onClick
!function(treeGrid) {
	var nodes = treeGrid.getCheckedNodes(), message;
	if (nodes && nodes.length) {
		var names = [];
		nodes.each(function(data) {
			names.push(data.get("data.firstName"));
		});
		message = names.join("\n");
	} else {
		message = "未选中任何节点！";
	}
	dorado.MessageBox.alert(message);
}

// @Bind #buttonHighlightCurrent.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	treeGrid.highlightItem();
}

// @Bind #buttonAppendChild.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var currentEntity = currentNode.get("data");
	currentNode.expandAsync(function() {
		currentEntity.createChild("underlings", {
			firstName : "<First Name>",
			lastName : "<Last Name>",
		});
	});
}

// @Bind #buttonInsertChild.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var currentEntity = currentNode.get("data");
	currentNode.expandAsync(function() {
		currentEntity.get("underlings").insert({
			firstName : "<First Name>",
			lastName : "<Last Name>",
		}, "before");
	});
}

// @Bind #buttonAppendBrother.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var currentEntity = currentNode.get("data");
	currentEntity.createBrother({
		firstName : "<First Name>",
		lastName : "<Last Name>",
	});
}

// @Bind #buttonInsertBrother.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var currentEntity = currentNode.get("data");
	currentEntity.parent.insert({
		firstName : "<First Name>",
		lastName : "<Last Name>",
	}, "before", currentEntity);
}

// @Bind #buttonDelete.onClick
!function(treeGrid) {
	var currentEntity = treeGrid.get("currentEntity");
	if (!currentEntity) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	currentEntity.remove();
}

// @Bind #buttonMoveUp.onClick
!function(treeGrid) {
	var currentEntity = treeGrid.get("currentNode.data");
	if (!currentEntity) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var previous = currentEntity.getPrevious();
	if (!previous) {
		throw new dorado.Exception("当前节点已是父节点下的第一个树节点！");
	}
	currentEntity.parent.insert(currentEntity, "before", previous);
	treeGrid.set("currentEntity", currentEntity);
}

// @Bind #buttonMoveDown.onClick
!function(treeGrid) {
	var currentEntity = treeGrid.get("currentNode.data");
	if (!currentEntity) {
		throw new dorado.Exception("请先点中一个树节点！");
	}

	var next = currentEntity.getNext();
	if (!next) {
		throw new dorado.Exception("当前节点已是父节点下的最后一个树节点！");
	}
	currentEntity.parent.insert(currentEntity, "after", next);
	treeGrid.set("currentEntity", currentEntity);
}

// @Bind #buttonUpgrade.onClick
!function(treeGrid) {
	var currentNode = treeGrid.get("currentNode");
	if (!currentNode) {
		throw new dorado.Exception("请先点中一个树节点！");
	}
	if (currentNode.get("level") == 1) {
		throw new dorado.Exception("当前节点顶层树节点！");
	}

	var currentEntity = currentNode.get("data");
	var parentEntity = currentNode.get("parent.data");
	parentEntity.parent.insert(currentEntity, "after", parentEntity);
	treeGrid.set("currentEntity", currentEntity);
}