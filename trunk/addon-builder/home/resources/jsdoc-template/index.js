var packageTree = PACKAGE_TREE, componentTree = COMPONENT_TREE;
var appTitle = "Dorado 7.x Client Side API - %name% %revision%";

function showSymbolDocInTab(alias) {
	var mainSymbolInfo = symbolDictionary[alias], mainSymbolAlias = alias;
	var detailSymbolInfo = mainSymbolInfo;
	while (mainSymbolInfo != null && ["namespace", "category", "class", "object"].indexOf(mainSymbolInfo[0]) < 0) {
		mainSymbolAlias = mainSymbolInfo[4];
		mainSymbolInfo = symbolDictionary[mainSymbolAlias];
	}
	if (mainSymbolInfo == null) {
		new dorado.Exception("Invalid symbol [" + alias + "].");
	}

	var tabset = view.id("tabcontrolMain");
	var tab = tabset.getTab(mainSymbolAlias);
	if (!tab) {
		var name = mainSymbolInfo[1], path, icon;
		if (mainSymbolInfo != detailSymbolInfo) {
			path = "class.html?symbol=" + mainSymbolAlias + "&member=" + alias;
			icon = getSymbolIcon(mainSymbolAlias);
		} else {
			path = "class.html?symbol=" + alias;
			icon = getSymbolIcon(alias);
		}

		tabset.insertTab({
			$type: "IFrame",
			name: mainSymbolAlias,
			icon: icon,
			caption: ((name == "_global_") ? "<Global>" : name),
			closeable: true,
			path: path
		});
	} else {
		tabset.set("currentTab", tab);
		tab.get("control.iFrameWindow").scrollMemberIntoView(alias);
	}
}

function searchSymbols(text) {
	var tabset = view.id("tabcontrolMain"), tabName = "tabSearchResult";
	var tab = tabset.getTab(tabName), path = "search-result.html?text=" + escape(text), icon = "styles/search.gif";
	if (!tab) {
		tabset.insertTab({
			$type: "IFrame",
			name: tabName,
			icon: icon,
			caption: "Search",
			closeable: true,
			path: path
		});
	} else {
		tabset.set("currentTab", tab);
		tab.set("path", path);
	}
}

function symbolToNode(symbolNode) {
	var symbolInfo = symbolDictionary[symbolNode.alias];
	var type = symbolInfo[0], name = symbolInfo[1], label = name;
	if (type == "namespace") {
		label = symbolNode.alias;
	}

	var hasChild = (type == "namespace" || type == "category") && symbolNode.children && symbolNode.children.length;
	if (hasChild) {
		hasChild = false;
		var children = symbolNode.children;
		for (var i = 0; i < children.length; i++) {
			var child = children[i], childType = symbolDictionary[child.alias][0];
			if (["namespace", "category", "class", "object"].indexOf(childType) >= 0) {
				hasChild = true;
				break;
			}
		}
	}
	var node = {
		label: label,
		icon: getSymbolIcon(symbolNode.alias),
		hasChild: hasChild,
		userData: {
			type: type,
			name: name,
			alias: symbolNode.alias,
			isAbstract: symbolInfo[2],
			deprecated: symbolInfo[3],
			children: symbolNode.children
		}
	};
	return node;
}

function symbolsToNodes(symbolNodes) {
	if (!symbolNodes) return;
	var nodes = [];
	jQuery.each(symbolNodes, function(i, symbolNode) {
		var symbolInfo = symbolDictionary[symbolNode.alias], type = symbolInfo[0];
		if (["namespace", "category", "class", "object"].indexOf(type) >= 0) {
			nodes.push(symbolToNode(symbolNode));
		}
	});
	return nodes;
}

function prepareChildNodes(node) {
	var userData = node.get("userData");
	if (userData.childrenPrepared) return;
	userData.childrenPrepared = true;
	var children = userData.children;
	node.set("nodes", symbolsToNodes(children));
}

function arrangeTreeNodes() {

	function doArrange(nodes) {
		if (!nodes) return;
		var symbolType;
		for (var i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			if (node.alias) {
				symbolType = symbolDictionary[node.alias][0];
				if (symbolType == "namespace") {
					packageTree.push(node);
					nodes.removeAt(i);
					i--;

					doArrange(node.children);
				}
			}
		}
	}

	for (var i = 0, len = packageTree.length; i < len; i++) {
		doArrange(packageTree[i].children);
	}
}

arrangeTreeNodes();

function tree_beforeCurrentChange(tree, arg) {
	var node = arg.newCurrent;
	if (!node) return;
	arg.processDefault = (node.get("userData").type != "category");
}

function tree_beforeExpand(tree, arg) {
	if (arg.node._prepared) {
		if (arg.async) arg.callDefault();
	} else {
		prepareChildNodes(arg.node);
		arg.node._prepared = true;
		if (arg.async) setTimeout(function() {
			arg.callDefault();
		}, 200);
	}
}

function tree_onDataRowClick(tree, arg) {
	var node = tree.get("currentNode");
	if (node) {
		var userData = node.get("userData");
		showSymbolDocInTab(userData.alias);
	}
}

function tree_onRenderNode(tree, arg) {
	var dom = arg.dom, node = arg.node, userData = node.get("userData");
	var label = node.get("label");
	if (label == "_global_") {
		dom.innerText = "<Global>";
	} else if (userData.type == "namespace" || userData.type == "category") {
		label = "<LABEL class=\"tree-node-name\">" + label + "</LABEL>" +
		" <LABEL class=\"tree-node-info\">[" +
		(userData.children ? userData.children.length : 0) +
		"]</LABEL>";
		dom.innerHTML = label;
	} else {
		label = "<LABEL class=\"tree-node-name" + (userData.isAbstract ? " tree-node-name-abstract" : '') + (userData.deprecated ? " deprecated" : '') + "\">" + label + "</LABEL>" +
		" <LABEL class=\"tree-node-type\">&lt;" +
		userData.type +
		"&gt;</LABEL>";
		dom.innerHTML = label;
	}
}

var treePackage = new dorado.widget.Tree({
	id: "treePackage",
	scrollMode: "lazyRender",
	nodes: symbolsToNodes(packageTree),
	style: {
		border: "none"
	},
	beforeCurrentChange: tree_beforeCurrentChange,
	beforeExpand: tree_beforeExpand,
	onDataRowClick: tree_onDataRowClick,
	onRenderNode: tree_onRenderNode
});

var treeComponent = new dorado.widget.Tree({
	id: "treeComponent",
	scrollMode: "lazyRender",
	nodes: symbolsToNodes(componentTree),
	style: {
		border: "none"
	},
	beforeCurrentChange: tree_beforeCurrentChange,
	beforeExpand: tree_beforeExpand,
	onDataRowClick: tree_onDataRowClick,
	onRenderNode: tree_onRenderNode
});

var view = new dorado.widget.View({
	layout: {
		$type: "Dock",
		regionPadding: 1
	}
});
view.set("children", [{
	$type: "Panel",
	border: "curve",
	height: 30,
	background: "url(styles/title-bg.gif)",
	layout: "Anchor",
	layoutConstraint: {
		type: "top",
		height: 30
	},
	children: [{
		$type: "HtmlContainer",
		content: "<label>" + appTitle + "</label>",
		layoutConstraint: {
			left: 12,
			top: "50%"
		},
		style: {
			fontWeight: "bold",
			fontSize: "10pt",
			fontStyle: "italic"
		}
	}, {
		$type: "TextEditor",
		blankText: "输入搜索条件...",
		trigger: [view.getComponentReference("triggerSearch"), view.id("triggerClear")],
		width: 200,
		onKeyDown: function(self, arg) {
			if (arg.keyCode == 13) this.id("triggerSearch").execute(self);
		},
		layoutConstraint: {
			right: 12,
			top: "50%"
		}
	}, {
		$type: "Trigger",
		id: "triggerSearch",
		iconClass: "d-trigger-icon-search",
		onExecute: function(self, arg) {
			var text = arg.editor.get("text");
			if (text) searchSymbols(text);
		}
	}]
}, new dorado.widget.SplitPanel({
	position: 300,
	sideControl: new dorado.widget.Accordion({
		sections: [{
			caption: "Packages",
			control: treePackage
		}, {
			caption: "Components",
			control: treeComponent
		}]
	}),
	mainControl: new dorado.widget.Container({
		layout: {
			$type: "Dock"
		},
		style: {
			border: "1px solid #A1A1A1"
		},
		children: [new dorado.widget.TabControl({
			id: "tabcontrolMain",
			tabs: [{
				$type: "IFrame",
				caption: "Welcome",
				icon: "styles/welcome.gif",
				path: "welcome.html"
			}]
		})]
	}),
	direction: "left",
	previewable: true
})]);

document.title = appTitle;
$(document).ready(function() {
	view.render(document.body);
});
