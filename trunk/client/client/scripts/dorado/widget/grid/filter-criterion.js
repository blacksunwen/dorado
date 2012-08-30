(function() {

	var operatorItemsInited = false;
	var operators = ["like", "like*", "*like", "=", "<>", ">", ">=", "<", "<="], operatorItems = [];
	
	function getOperatorItems() {
		if (!operatorItemsInited) {
			operatorItemsInited = true;
			var texts = $resource("dorado.grid.FilterExpressionOperators").split(',');
			for (var i = 0; i < operators.length; i++) {
				var operator = operators[i];
				operatorItems.push({
					key: operator,
					value: texts[i]
				});
			}
		}
		return operatorItems;
	}
	
	var numberTypeCodes = [dorado.DataType.INTEGER, dorado.DataType.PRIMITIVE_INT, dorado.DataType.FLOAT, dorado.DataType.PRIMITIVE_FLOAT, dorado.DataType.DATE, dorado.DataType.TIME, dorado.DataType.DATETIME];
	var numberOperatorDropDown, stringOperatorDropDown, booleanOperatorDropDow;
	
	function getOperatorDropDown(column) {
		var dataType = column.get("dataType"), dropDown, operatorItems = getOperatorItems();
		if (dataType && dataType._code) {
			if (numberTypeCodes.indexOf(dataType._code) >= 0) {
				if (!numberOperatorDropDown) {
					numberOperatorDropDown = new dorado.widget.ListDropDown({
						items: operatorItems.slice(3),
						property: "key",
						displayProperty: "value",
						autoOpen: true
					});
				}
				dropDown = numberOperatorDropDown;
			} else if ([dorado.DataType.PRIMITIVE_BOOLEAN, dorado.DataType.BOOLEAN].indexOf(dataType._code) >= 0) {
				if (!booleanOperatorDropDow) {
					booleanOperatorDropDow = new dorado.widget.ListDropDown({
						items: operatorItems.slice(3, 2),
						property: "key",
						displayProperty: "value",
						autoOpen: true
					});
				}
				dropDown = booleanOperatorDropDow;
			}
		}
		
		if (!dropDown) {
			stringOperatorDropDown = new dorado.widget.ListDropDown({
				items: operatorItems.slice(0, 5),
				property: "key",
				displayProperty: "value",
				autoOpen: true
			});
			dropDown = stringOperatorDropDown;
		}
		return dropDown;
	}
	
	var primitiveBooleanDropDown;
	function getPrimitiveBooleanDropDown() {
		if (!primitiveBooleanDropDown) {
			primitiveBooleanDropDown = new dorado.widget.ListDropDown({
				items: [{
					key: true,
					value: $resource("dorado.core.BooleanTrue")
				}, {
					key: false,
					value: $resource("dorado.core.BooleanFalse")
				}],
				editable: false,
				property: "key",
				displayProperty: "value",
				autoOpen: true
			});
		}
		return primitiveBooleanDropDown;
	}
	
	var booleanDropDown;
	function getBooleanDropDown() {
		if (!booleanDropDown) {
			booleanDropDown = new dorado.widget.ListDropDown({
				items: [{
					key: null,
					value: ""
				}, {
					key: true,
					value: $resource("dorado.core.BooleanTrue")
				}, {
					key: false,
					value: $resource("dorado.core.BooleanFalse")
				}],
				editable: false,
				property: "key",
				displayProperty: "value",
				autoOpen: true
			});
		}
		return booleanDropDown;
	}
	
	function splitCriterions(text, column) {
		var criterions = [], criterion = "", contentBegin = false, contentEnd = false, inQuote = false, escape = false;
		var c;
		for (var i = 0, len = text.length; i < len; i++, escape = false) {
			c = text.charAt(i);
			if (c === ' ') {
				if (contentBegin) {
					if (!inQuote) {
						if (operators.indexOf(criterion) < 0) {
							criterions.push(criterion);
							criterion = "";
							contentBegin = false;
							contentEnd = false;
							inQuote = false;
						}
						continue;
					}
				} else {
					continue;
				}
			} else if (c === '\\') {
				escape = true;
			} else if ((c === '\'' || c === '"') && !escape) {
				if (!inQuote) {
					inQuote = c;
					continue;
				} else if (c === inQuote) {
					contentEnd = true;
					continue;
				}
			} else if (contentEnd) {
				throw new dorado.ResourceException("dorado.grid.InvalidFilterExpression", text);
			}
			criterion += c;
			contentBegin = true;
		}
		
		if (criterion) {
			criterions.push(criterion);
		}
		return criterions;
	}
	
	function parseCriterion(criterionText, column) {
		var criterion = {};
		for (var i = operators.length - 1; i >= 0; i--) {
			var operator = operators[i];
			if (criterionText.startsWith(operator)) {
				criterion.operator = operator;
				criterion.value = criterionText.substring(operator.length);
				break;
			}
		}
		
		if (!criterion.operator) {
			var defaultOperator = dorado.widget.grid.DataColumn.getDefaultOperator(column), operator, len = criterionText.length;
			if (defaultOperator == "like" && len > 1) {
				var firstChar = criterionText.charAt(0), lastChar = criterionText.charAt(len - 1);
				if (len > 2 && criterionText.charAt(len - 2) == '\\') {
					lastChar = 0;
				}
				
				if (firstChar != '*' && firstChar != '%') {
					firstChar = 0;
				}
				if (lastChar != '*' && lastChar != '%') {
					lastChar = 0;
				}
				
				if (firstChar) {
					if (lastChar) {
						if (len > 2) {
							operator = "like";
							criterion.value = criterionText.substring(1, len - 1);
						} else {
							operator = "=";
							criterion.value = criterionText;
						}
					} else {
						operator = "*like";
						criterion.value = criterionText.substring(1);
					}
				} else if (lastChar) {
					operator = "like*";
					criterion.value = criterionText.substring(0, len - 1);
				} else {
					operator = defaultOperator;
					criterion.value = criterionText;
				}
			} else {
				operator = defaultOperator;
				criterion.value = criterionText;
			}
		}
		
		criterion.operator = operator;
		if (criterion.value && criterion.value.indexOf('\\') >= 0) {
			criterion.value = eval('"' + criterion.value + '"');
		}
		
		var pd = column._propertyDef;
		if (pd && pd._mapping) {
			criterion.value = pd.getMappedKey(criterion.value);
		} else {
			var dataType = column.get("dataType");
			if (dataType) {
				criterion.value = dataType.parse(criterion.value, column.get("displayFormat"));
			}
		}
		return criterion;
	}
	
	dorado.widget.grid.DataColumn.getDefaultOperator = function(column) {
		var dataType = column.get("dataType");
		if (dataType && dataType._code && numberTypeCodes.indexOf(dataType._code) >= 0) {
			return "=";
		} else {
			return "like";
		}
	};
	
	dorado.widget.grid.DataColumn.parseCriterions = function(text, column) {
		if (!text) return null;
		var criterions = [], criterionTexts = splitCriterions(text, column);
		for (var i = 0; i < criterionTexts.length; i++) {
			criterions.push(parseCriterion(criterionTexts[i], column));
		}
		return criterions;
	};
	
	dorado.widget.grid.DataColumn.criterionsToText = function(criterions, column) {
		var text = "", pd = column._propertyDef, dataType = column.get("dataType");
		for (var i = 0; i < criterions.length; i++) {
			if (text != "") text += " ";
			
			var criterion = criterions[i], operator = criterion.operator;
			if (operator && operator.indexOf("like") < 0) {
				text += operator;
			}
			
			var valueText;
			if (pd && pd._mapping) {
				valueText = pd.getMappedValue(criterion.value);
			} else {
				var dataType = column.get("dataType");
				if (dataType) {
					valueText = dataType.toText(criterion.value, column.get("displayFormat"));
				} else {
					valueText = criterion.value + '';
				}
			}
			
			if (operator) {
				if (operator.startsWith("like")) {
					valueText = valueText + '*';
				}
				if (operator.endsWith("like")) {
					valueText = '*' + valueText;
				}
			}
			
			if (valueText.indexOf(' ') > 0) {
				text += ('"' + valueText + '"');
			} else {
				text += valueText;
			}
		}
		return text;
	};
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Trigger
	 * @class 自定义下拉框。
	 * @extends dorado.widget.DropDown
	 */
	dorado.widget.grid.CriterionDropDown = $extend(dorado.widget.DropDown, /** @scope dorado.widget.grid.CriterionDropDown.prototype */ {
		$className: "dorado.widget.grid.CriterionDropDown",
		
		ATTRIBUTES: /** @scope dorado.widget.grid.CriterionDropDown.prototype */ {
			criterions: {
				defaultValue: [],
				setter: function(criterions) {
					criterions = criterions || [];
					var criterions = this._criterions = dorado.Core.clone(criterions, true);
					for (var i = 0; i < criterions.length; i++) {
						var criterion = criterions[i];
						if (!criterion.id) criterion.id = dorado.Core.newId();
					}
				}
			}
		},
		
		constructor: function() {
			$invokeSuper.call(this, arguments);
			
			this._criterionMap = {};
			this._criterionControlCache = [];
		},
		
		createDropDownBox: function(editor) {
			var dropdown = this, box = $invokeSuper.call(dropdown, arguments);
			
			var containerElement = box.getContainerElement(), doms = {};
			$fly(containerElement).xCreate({
				tagName: "DIV",
				className: "i-criterion-panel d-criterion-panel",
				content: [{
					tagName: "DIV",
					contextKey: "criterionsContainer"
				}, {
					tagName: "DIV",
					className: "i-buttons-container d-buttons-container",
					contextKey: "buttonsContainer"
				}]
			}, null, {
				context: doms
			});
			dropdown._criterionsContainer = doms.criterionsContainer;
			dropdown._buttonsContainer = doms.buttonsContainer;
			
			var addButton = new dorado.widget.Button({
				icon: "url(skin>common/icons.gif) -120px 0",
				style: "margin-right:2px",
				onClick: function() {
					dropdown.addCriterion(box);
				}
			});
			box.registerInnerControl(addButton);
			addButton.render(dropdown._buttonsContainer);
			
			var okButton = new dorado.widget.Button({
				caption: $resource("dorado.baseWidget.MessageBoxButtonOK"),
				onClick: function() {
					var column = editor._cellColumn, criterions = dropdown._criterions, grid = column._grid, text;
					if (criterions.length == 1 && criterions[0].value === undefined && criterions[0].operator === dorado.widget.grid.DataColumn.getDefaultOperator(column)) {
						text = "";
					} else {
						text = dorado.widget.grid.DataColumn.criterionsToText(criterions, column);
					}
					debugger;
					dropdown.close(text);
					grid.filter();
				}
			});
			box.registerInnerControl(okButton);
			okButton.render(dropdown._buttonsContainer);
			
			return box;
		},
		
		open: function(editor) {
			editor.post();
			var column = editor._cellColumn, grid = column._grid;
			var filterEntity = grid.get("filterEntity");
			this.set("criterions", filterEntity.get(column._property));
			
			return $invokeSuper.call(this, arguments);
		},
		
		initDropDownBox: function(box, editor) {
			$invokeSuper.call(this, arguments);
			
			var dropdown = this, criterions = dropdown._criterions, criterionsContainer = dropdown._criterionsContainer, i = 0;
			var column = editor._cellColumn;
			
			if (criterions.length == 0) {
				criterions.push({
					id: dorado.Core.newId(),
					operator: dorado.widget.grid.DataColumn.getDefaultOperator(column)
				});
			}
			
			criterions.each(function(criterion) {
				var criterionControl, criterionDom = criterionsContainer.childNodes[i];
				if (criterionDom) {
					criterionControl = dorado.widget.Control.findParentControl(criterionDom, CriterionControl);
				}
				if (criterionControl) {
					criterionControl.set({
						column: column,
						criterion: criterion
					});
				} else {
					criterionControl = dropdown.getCriterionControl(box, criterion);
					criterionControl.render(dropdown._criterionsContainer);
				}
				dropdown._criterionMap[criterion.id] = criterionControl;
				i++;
			});
			
			var criterionControls = criterionsContainer.childNodes, criterionControlsNum = criterionControls.length;
			for (; i < criterionControlsNum; i++) {
				var criterionControl = dorado.widget.Control.findParentControl(criterionsContainer.lastChild, CriterionControl);
				var criterion = criterionControl._criterion;
				delete dropdown._criterionMap[criterion.id];
				criterionControl.unrender();
				dropdown._criterionControlCache.push(criterionControl);
			}
		},
		
		getCriterionControl: function(box, criterion) {
			var criterionControl = this._criterionControlCache.pop();
			if (!criterionControl) {
				criterionControl = new CriterionControl(this, box);
			}
			
			var column = box._editor._cellColumn;
			criterionControl.set({
				column: column,
				criterion: criterion
			});
			return criterionControl;
		},
		
		addCriterion: function(box) {
			var dropdown = this, column = box._editor._cellColumn, criterion = {
				id: dorado.Core.newId(),
				operator: dorado.widget.grid.DataColumn.getDefaultOperator(column)
			};
			var criterionControl = dropdown.getCriterionControl(box, criterion);
			criterionControl.render(dropdown._criterionsContainer);
			dropdown._criterionMap[criterion.id] = criterionControl;
			dropdown._criterions.push(criterion);
			
			dropdown.locate();
		},
		
		removeCriterion: function(box, criterion) {
			var dropdown = this, criterionControl = dropdown._criterionMap[criterion.id];
			criterionControl.unrender();
			dropdown._criterionControlCache.push(criterionControl);
			delete dropdown._criterionMap[criterion.id];
			dropdown._criterions.remove(criterion);
			
			dropdown.locate();
			
			setTimeout(function() {
				if (dropdown._criterions.length == 0) {
					dropdown.addCriterion(box);
				}
			}, 100);
		}
	});
	
	var CriterionControl = $extend(dorado.widget.Control, {
		$className: "dorado.widget.grid.CriterionControl",
		
		ATTRIBUTES: {
			column: {},
			
			criterion: {}
		},
		
		constructor: function(dropDown, dropDownBox) {
			$invokeSuper.call(this);
			
			this._dropDown = dropDown;
			this._dropDownBox = dropDownBox;
			dropDownBox.registerInnerControl(this);
		},
		
		createDom: function() {
			var criterionControl = this, doms = {}, dom = $DomUtils.xCreate({
				tagName: "TABLE",
				className: "i-criterion d-criterion",
				content: {
					tagName: "TR",
					content: [{
						tagName: "TD",
						className: "operator-container",
						contextKey: "operatorContainer"
					}, {
						tagName: "TD",
						className: "value-container",
						contextKey: "valueContainer"
					}, {
						tagName: "TD",
						className: "button-container",
						contextKey: "buttonContainer"
					}]
				}
			}, null, doms);
			
			var dropDownBox = criterionControl._dropDownBox;
			var operatorEditor = criterionControl._operatorEditor = new dorado.widget.TextEditor({
				width: 100,
				mapping: getOperatorItems(),
				onPost: function(self) {
					criterionControl._criterion.operator = self.get("value");
				}
			});
			criterionControl.registerInnerControl(operatorEditor);
			operatorEditor.render(doms.operatorContainer);
			
			var valueEditor = criterionControl._valueEditor = new dorado.widget.TextEditor({
				width: 180,
				onPost: function(self) {
					criterionControl._criterion.value = self.get("value");
				}
			});
			criterionControl.registerInnerControl(valueEditor);
			valueEditor.render(doms.valueContainer);
			
			var delButton = criterionControl._delButton = new dorado.widget.SimpleButton({
				className: "delete-button",
				onClick: function() {
					var dropdown = criterionControl._dropDown, criterion = criterionControl._criterion;
					dropdown.removeCriterion(dropDownBox, criterion);
				}
			});
			criterionControl.registerInnerControl(delButton);
			delButton.render(doms.buttonContainer);
			
			this._doms = doms;
			return dom;
		},
		
		refreshDom: function(dom) {
			$invokeSuper.call(this, [dom]);
			
			var column = this._column, pd = column._propertyDef;
			var dataType = column.get("dataType"), dtCode = dataType ? dataType._code : -1;
			var trigger = column.get("trigger"), displayFormat = column.get("displayFormat"), typeFormat = column.get("typeFormat");
			if (!dtCode || (pd && pd._mapping)) dataType = undefined;
			
			var operatorEditor = this._operatorEditor, valueEditor = this._valueEditor, doms = this._doms;
			operatorEditor.set({
				trigger: getOperatorDropDown(column),
				value: this._criterion && this._criterion.operator
			});
			
			if (!trigger) {
				if (pd && pd._mapping) {
					trigger = new dorado.widget.AutoMappingDropDown({
						items: pd._mapping
					});
				} else if (dtCode == dorado.DataType.PRIMITIVE_BOOLEAN) {
					trigger = getPrimitiveBooleanDropDown();
				} else if (dtCode == dorado.DataType.BOOLEAN) {
					trigger = getBooleanDropDown();
				} else if (dtCode == dorado.DataType.DATE) {
					trigger = "defaultDateDropDown";
				} else if (dtCode == dorado.DataType.DATETIME) {
					trigger = "defaultDateTimeDropDown";
				}
			}
			
			valueEditor.set({
				dataType: dataType || null,
				displayFormat: displayFormat,
				typeFormat: typeFormat,
				trigger: trigger,
				editable: column._editable
			}, {
				skipUnknownAttribute: true,
				tryNextOnError: true
			});
			valueEditor.set("value", this._criterion && this._criterion.value);
		}
	});
	
	dorado.widget.View.registerDefaultComponent("defaultCriterionDropDown", function() {
		return new dorado.widget.grid.CriterionDropDown();
	});
	
})();





