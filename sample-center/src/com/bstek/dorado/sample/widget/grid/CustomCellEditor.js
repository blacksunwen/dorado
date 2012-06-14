function parseParam(param) {
	if (!param)
		return null;

	var pairs = [];
	param.split(';').each(function(pair) {
		var i = pair.indexOf('='), key, value;
		if (i >= 0 && i < (pair.length - 1)) {
			pairs.push({
				key : pair.substring(0, i),
				value : pair.substring(i + 1)
			});
		} else {
			pairs.push({
				value : pair
			});
		}
	});
	return pairs;
}

function getEditor(entity) {
	var editorType = entity.get("editorType"), param = entity.get("param");
	if (!editorType) {
		return null;
	}
	
	var editor = eval("new dorado.widget." + editorType + "()");
	if (editor instanceof dorado.widget.TextEditor) {
		if (param) {
			var trigger;
			if (param == "date") {
				trigger = "defaultDateDropDown";
			} else if (param == "dateTime") {
				trigger = "defaultDateTimeDropDown";
			} else {
				trigger = new dorado.widget.ListDropDown({
					items : parseParam(param),
					property : "value"
				});
			}
			editor.set("trigger", trigger);
		}
	} else if (editor instanceof dorado.widget.CheckBox) {
		editor.set("exClassName", "d-checkbox-center")
	} else if (editor instanceof dorado.widget.RadioGroup) {
		editor.set("layout", "flow");
		if (param) {
			var radioButtons = [];
			parseParam(param).each(function(pair) {
				radioButtons.push({
					value : pair.key,
					text : pair.value
				});
			});
			editor.set("radioButtons", radioButtons);
		}
	} else if (editor instanceof dorado.widget.DateTimeSpinner) {
		editor.set({
			dataType : "DateTime",
			type : param
		});
	} else if (editor instanceof dorado.widget.CustomSpinner) {
		editor.set("pattern", param);
	}
	return editor;
}

// @Bind #dsRows1.onCreate
// @Bind #dsRows2.onCreate
!function(self) {
	self.setData([ {
		editorType : "TextEditor"
	}, {
		editorType : "TextEditor",
		param : "new=新增;modified=修改;deleted=删除;none=未改变"
	}, {
		editorType : "TextEditor",
		param : "date"
	}, {
		editorType : "TextEditor",
		param : "dateTime"
	}, {
		editorType : "CheckBox"
	}, {
		editorType : "RadioGroup",
		param : "male=男;female=女"
	}, {
		editorType : "NumberSpinner",
		value : 10
	}, {
		editorType : "DateTimeSpinner",
		param : "dateTime",
		value : new Date()
	}, {
		editorType : "CustomSpinner",
		param : "IP: [0,255].[0,255].[0,255].[0,255]",
		value : [ 192, 168, 1, 100 ]
	} ]);
}

// @Bind #grid1.onGetCellEditor
!function(arg) {
	var entity = arg.data, editor = getEditor(entity);
	arg.cellEditor.setEditorControl(editor);
}

// @Bind #grid1.&value.onRenderCell
!function(arg) {
	arg.dom.style.background = "#d5e4fc";
	arg.processDefault = true;
}

// @Bind #buttonAdd1.onClick
!function(dsRows1) {
	dsRows1.getData().insert();
}

// @Bind #buttonDel1.onClick
!function(dsRows1) {
	dsRows1.getData().remove();
}

var CellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer, {
	createSubControl : function(arg) {
		var entity = arg.data;
		var editor = getEditor(entity);
		if (editor) {
			editor.set("width", "100%");
		}
		return editor;
	},
	refreshSubControl : function(editor, arg) {
		if (editor)
			editor.set("value", arg.data.get("value"));
	}
});

// @Bind #grid2.onReady
!function(self) {
	self.set("&value.renderer", new CellRenderer());
}