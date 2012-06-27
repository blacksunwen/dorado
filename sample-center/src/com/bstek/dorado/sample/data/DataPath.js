var selectedEntities;

function resultToString(result) {
	var s = "";
	if (result == null) {
		s = "<null>";
	} else if (result instanceof dorado.Entity) {
		s = "dorado.Entity [ " + result.get("label") + " ]";

		result.set("selected", true);
		selectedEntities.push(result);
	} else if (result instanceof Array) {
		s = "Array [\n";
		for ( var i = 0; i < result.length; i++) {
			s += "  " + resultToString(result[i]) + ",\n";
		}
		s += "]";
	} else {
		s = result;
	}
	return s;
}

// @Bind #actionExecute.onExecute
!function(dsExamples, inputDataPath, checkboxFirstResultOnly,
		checkboxAcceptAggregation, dataPathResult) {
	   if (selectedEntities) {
		selectedEntities.each(function(entity) {
			entity.set("selected", false);
		});
	}
	selectedEntities = [];

	var dataPath = inputDataPath.get("text");
	var firstResultOnly = checkboxFirstResultOnly.get("checked");
	var acceptAggregation = checkboxAcceptAggregation.get("checked");
	var result = dsExamples.getData(dataPath, {
		firstResultOnly : firstResultOnly,
		acceptAggregation : acceptAggregation
	});

	var resultType;
	if (result == null) {
		resultType = "<null>";
	} else if (result instanceof dorado.Entity) {
		resultType = "dorado.Entity";
	} else if (result instanceof dorado.EntityList) {
		resultType = "dorado.EntityList";
	} else if (result instanceof Array) {
		resultType = "Array";
	} else {
		resultType = typeof result;
	}

	var message = "DataPath: " + (dataPath ? dataPath : "<empty>");
	message += "\n - firstResultOnly: " + firstResultOnly;
	message += "\n - acceptAggregation: " + acceptAggregation;
	message += "\n";
	message += "\nResult Type: " + resultType;
	message += "\nResult: " + resultToString(result);

	dataPathResult.set("text", message);
}

// @Bind #dropDownDataPaths.onCreate
!function(self) {
	self
			.set(
					"items",
					[ "*", "#", "[#current]", "#.examples", "#.#examples",
							"categories(R)", ".categories(R)",
							"categories(R).label", ".examples",
							"categories(R).examples",
							"categories(R).examples.label",
							"#categories(R).examples",
							"#categories(R).#examples",
							"categories(R).examples[@.get(\"new\")]",
							"categories(R).examples[@.get(\"label\").length > 5 && !@.get(\"new\")]" ]);
}

// @Bind #radioGroupMode.onValueChange
!function(self, checkboxFirstResultOnly, checkboxAcceptAggregation) {
	var mode = self.get("value");
	this.set("^freeMode.readOnly", mode != "free");
	if (mode == "getData") {
		checkboxFirstResultOnly.set("checked", true);
		checkboxAcceptAggregation.set("checked", true);
	} else if (mode == "queryData") {
		checkboxFirstResultOnly.set("checked", false);
		checkboxAcceptAggregation.set("checked", false);
	}
}

// @Bind #treeExamples.onCurrentChange
!function(self) {
	var entity = self.get("currentNode.data");
	entity.setCurrent(true);
}

// @Bind #treeExamples.onRenderNode
!function(arg) {
	var selected = arg.node.get("data.selected");
	arg.dom.style.fontWeight = selected ? "bold" : "";
	arg.dom.style.background = selected ? "orange" : "";
	arg.dom.style.padding = selected ? "0 4px" : "";
	arg.dom.style.color = selected ? "white" : "";

	var node = arg.node, data = node.get("data");
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