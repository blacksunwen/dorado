var rootNodesLoaded = false;

// @Bind view.onReady
!function(treeExamples) {
	jQuery(window).hashchange(function() {
		if (rootNodesLoaded) {
			processHash();
		}
	});
}

function processHash() {
	var hash = window.location.hash;
	if (hash) {
		hash = hash.substring(1);
	}
	if (!hash) {
		return false;
	}

	var type = "example", id;
	if (hash.indexOf("category") == 0) {
		type = "category";
		id = parseInt(hash.substring(8));
	}
	else {
		id = parseInt(hash);
	}
	if (!(id > 0)) {
		return false;
	}
	
	if (view.get("#treeExamples.currentEntity.id") == id) {
		return;
	}

	view.get("#actionGetPath").set("parameter", {
		type : type,
		id : id
	}).execute(function(path) {
		var node = view.get("#treeExamples.root");
		path.each(function(categoryId) {
			node.expand();
			var child = findChild(node, categoryId);
			if (!child) {
				return false;
			}
			child.expand();
			node = child;
		});

		if (node) {
			var child = findChild(node, id);
			if (child) {
				view.set("#treeExamples.currentNode", child);
				openPageInFrame(child);
			}
		}
	});
}

function onRootNodesLoaded() {
	if (processHash() === false) {
		openPageInFrame(null);
	}
}

function findChild(node, id) {
	var result = null;
	node.get("nodes").each(function(child) {
		if (child.get("data.id") == id) {
			result = child;
			return false;
		}
	});
	return result;
}

function openPageInFrame(node) {
	var url;
	if (node) {
		var nodeType = node.get("bindingConfig.name"), hash = "#";
		if (nodeType == "Category") {
			url = "com.bstek.dorado.sample.CategorySummary.d?categoryId=";
			hash += "category";
		} else {
			url = "com.bstek.dorado.sample.ExampleSummary.d?exampleId=";
		}
		var id = node.get("data.id");
		url += id;
		window.location.hash = (hash + id);
	}
	else {
		url = "com.bstek.dorado.sample.Welcome.d";
		window.location.hash = "";
	}
	view.set("#frameExample.path", url);
}

// @Bind #treeExamples.onDataNodeCreate
!function(self, arg) {
	if (!rootNodesLoaded) {
		if (arg.node.get("parent") == self.get("root")) {
			rootNodesLoaded = true;
			setTimeout(onRootNodesLoaded, 0);
		}
	}
}

// @Bind #treeExamples.onDataRowClick
!function(self) {
	openPageInFrame(self.get("currentNode"));
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