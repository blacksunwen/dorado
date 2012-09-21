// @Bind view.onReady
!function(tipSearch) {
	setTimeout(function() {
		tipSearch.show({
			position : {
				left : 168,
				top : 14
			}
		});
	}, 500);
}

// @Bind @Schema.onCurrentChange
!function(dsSchemas, criteriaBuilder) {
	var criteria = null, schema = dsSchemas.getData("#"), criteriaString = schema
			.get("criteria");
	if (criteriaString) {
		criteria = dorado.JSON.parse(criteriaString);
	}
	criteriaBuilder.set("criteria", criteria);

	var name = schema.get("name");
	view.set({
		"^needSchema.disabled" : !name,
		"#editorSchema.text" : name
	});
}

// @Bind #buttonQuery.onClick
!function(dsExamples, criteriaBuilder) {
	dsExamples.set("parameter", criteriaBuilder.get("criteria")).flushAsync();
}

// @Bind #buttonSave.onClick
function save(dsSchemas, criteriaBuilder) {
	var name = dsSchemas.getData("#.name");
	if (!name) {
		saveAs(dsSchemas, criteriaBuilder)
	} else {
		dorado.MessageBox.confirm("确认要覆盖当前的查询方案[" + name + "]吗？", function() {
			var criteria = criteriaBuilder.get("criteria");
			dsSchemas.getData("#").set("criteria",
					dorado.JSON.stringify(criteria));
			activeSave.execute();
		});
	}
}

// @Bind #buttonSaveAs.onClick
function saveAs(dsSchemas, criteriaBuilder, activeSave) {
	dorado.MessageBox.prompt("请输入新的查询方案的名称", function(text) {
		if (text) {
			var save = true;
			dsSchemas.getData().each(function(schema) {
				if (text == schema.get("name")) {
					dorado.MessageBox.alert(("查询方案[" + text + "]已经存在！"));
					save = false;
					return false;
				}
			});

			if (save) {
				var criteria = criteriaBuilder.get("criteria");
				dsSchemas.insert({
					name : text,
					criteria : dorado.JSON.stringify(criteria)
				});
				activeSave.execute();
			}
		}
	});
}

// @Bind #buttonDelete.onClick
!function(dsSchemas, activeSave) {
	var schema = dsSchemas.getData("#"), name = schema.get("name");
	if (name) {
		dorado.MessageBox.confirm("确认要删除当前的查询方案[" + name + "]吗？", function() {
			schema.remove();
			activeSave.execute();
		});
	}
}

// @Bind #buttonClear.onClick
!function(criteriaBuilder) {
	criteriaBuilder.set("criteria", null);
}

// @Bind #editorSchema.onFocus
!function(tipSearch) {
	if (tipSearch.get("visible")) {
		tipSearch.hide();
	}
}