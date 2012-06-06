/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 下拉框控件的抽象类。一种特殊的编辑框触发器。
 * @extends dorado.widget.Trigger
 * @abstract
 */
dorado.widget.DropDown = $extend(dorado.widget.Trigger, /** @scope dorado.widget.DropDown.prototype */ {
	$className : "dorado.widget.DropDown",

	focusable : true,

	ATTRIBUTES : /** @scope dorado.widget.DropDown.prototype */ {

		/**
		 * 图标元素的CSS Class。
		 * 
		 * @type String
		 * @attribute
		 * @default "d-trigger-icon-drop"
		 */
		iconClass : {
			defaultValue : "d-trigger-icon-drop"
		},

		/**
		 * 下拉框的显示宽度。 如果不指定此属性则将由系统自动决定下拉框的显示宽度。
		 * 
		 * @type int
		 * @attribute
		 */
		width : {},

		/**
		 * 下拉框的最小显示宽度。 如果不指定此属性则表示不限制下拉框的最小显示宽度。
		 * 
		 * @type int
		 * @attribute
		 * @default 20
		 */
		minWidth : {
			defaultValue : 20
		},

		/**
		 * 下拉框的最大显示宽度。 如果不指定此属性则表示不限制下拉框的最大显示宽度。
		 * 
		 * @type int
		 * @attribute
		 */
		maxWidth : {},

		/**
		 * 下拉框的显示高度。 如果不指定此属性则将由系统自动决定下拉框的显示高度。
		 * 
		 * @type int
		 * @attribute
		 */
		height : {},

		/**
		 * 
		 * 下拉框的最小显示高度。 如果不指定此属性则表示不限制下拉框的最小显示高度。
		 * 
		 * @type int
		 * @attribute
		 * @default 10
		 */
		minHeight : {
			defaultValue : 10
		},

		/**
		 * 下拉框的最大显示高度。 如果不指定此属性则表示不限制下拉框的最大显示高度。
		 * 
		 * @type int
		 * @attribute
		 * @default 400
		 */
		maxHeight : {
			defaultValue : 400
		},

		/**
		 * 是否启用自动打开的功能。即当相应的编辑框获得输入焦点时自动打开此下拉框。
		 * 
		 * @type boolean
		 * @attribute
		 */
		autoOpen : {},

		/**
		 * 是否在用户选择某个下拉框中的条目之后自动确认相应编辑框中的内容。
		 * 
		 * @type boolean
		 * @attribute
		 * @default true
		 * @see dorado.widget.AbstractTextEditor#post
		 */
		postValueOnSelect : {
			defaultValue : true
		},

		/**
		 * 用于设定当用户选中下拉框中某个条目之后，系统应进行怎样的赋值操作。
		 * <p>
		 * 注意：此属性只在下拉框返回的数据是dorado.Entity或Object类型，并且对应的编辑框正与某个Entity绑定时有效。<br>
		 * </p>
		 * <p>
		 * 此属性的值应该是一个表达式，其基本格式为：
		 * 
		 * <pre>
		 * 被赋值属性名1 = 下拉项中的属性名1, 被赋值属性名2 = 下拉项中的属性名2,...
		 * </pre>
		 * 
		 * <br>
		 * 其代表的含义是将下拉选中项的某属性（即上述“下拉项中的属性名”）中的值赋值给目标Entity（即编辑框绑定的Entity）的某属性（即上述“被赋值属性名”），
		 * 如果用户定义了","分隔的多组赋值条件，系统将依次处理这些赋值条件。<br>
		 * 另外，此表达式中还支持一种简写。即当“被赋值属性名”与“下拉项中的属性名”一致时，我们可以直接省略“=下拉项中的属性名”部分。
		 * </p>
		 * <p>
		 * 注意：在定义“下拉项中的属性名”时可以使用
		 * 
		 * <pre>
		 * $this
		 * </pre>
		 * 
		 * 这样一个特殊的属性名，表示直接将选中的下拉项对象作为数值赋值给目标Entity的某属性。
		 * </p>
		 * <p>
		 * 注意：
		 * 
		 * <pre>
		 * $default
		 * </pre>
		 * 
		 * 可以作为一个特殊的段落，代表系统默认的处理下拉选中项的动作。
		 * </p>
		 * 
		 * @type String
		 * @attribute
		 * 
		 * @example //
		 *          此表达式表示将下拉选中项的id和name属性中的值分别赋值给目标Entity的propertyId和propertyName属性。
		 *          dropdown.set("assignmentMap",
		 *          "propertyId=id,propertyName=name");
		 * 
		 * @example // 此表达式表示将下拉选中项的id和name属性中的值分别赋值给目标Entity的id和name属性。
		 *          dropdown.set("assignmentMap", "id,name");
		 *          assignmentMap: {},
		 */
		assignmentMap: {},
		
		/**
		 * 下拉框此刻是否处于打开状态。
		 * 
		 * @type boolean
		 * @attribute readOnly
		 */
		opened : {
			readOnly : true,
			getter : function() {
				return !!this._box;
			}
		},

		/**
		 * 返回下拉框当前对应的编辑框。
		 * <p>
		 * 此属性只在此下拉框正处于打开状态时有效，返回的值即是当前打开的下拉框对应的编辑框。
		 * </p>
		 * 
		 * @type dorado.widget.AbstractTextBox
		 * @attribute readOnly
		 */
		editor : {
			readOnly : true
		},

		/**
		 * 返回下拉框当前对应的容器控件。
		 * 
		 * @type dorado.widget.DropDownBox
		 * @attribute readOnly
		 */
		box : {
			readOnly : true
		}
	},

	EVENTS : /** @scope dorado.widget.DropDown.prototype */
	{

		/**
		 * 当下拉框被打开时触发的事件。
		 * 
		 * @param {Object}
		 *            self 事件的发起者，即控件本身。
		 * @param {Object}
		 *            arg 事件参数。
		 * @param {dorado.widget.AbstractEditor}
		 *            arg.editor 下拉框此刻关联的编辑器。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onOpen : {},

		/**
		 * 当下拉框关闭时触发的事件。
		 * 
		 * @param {Object}
		 *            self 事件的发起者，即控件本身。
		 * @param {Object}
		 *            arg 事件参数。
		 * @param {dorado.widget.AbstractEditor}
		 *            arg.editor 下拉框此刻关联的编辑器。
		 * @param {Object}
		 *            #arg.selectedValue 被选中的数值。
		 *            如果要在此事件中定义或修改下拉框选中的值，请将新的值写入arg参数的selectedValue属性中。
		 *            如果将arg.selectedValue的值设置为undefined，则表示本次关闭下拉框时不向编辑框中传递任何值。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onClose : {},

		/**
		 * 当下拉框中选中的数值将要被传递给编辑框时触发的事件。
		 * <p>
		 * onValueSelect事件与onClose事件的区别在于，onClose事件会在每次下拉框关闭时触发；
		 * 而onValueSelect事件仅在确实有值被选中并且该值将要被传递给编辑框时触发。
		 * </p>
		 * 
		 * @param {Object}
		 *            self 事件的发起者，即控件本身。
		 * @param {Object}
		 *            arg 事件参数。
		 * @param {dorado.widget.AbstractEditor}
		 *            arg.editor 下拉框此刻关联的编辑器。
		 * @param {Object}
		 *            #arg.selectedValue 被选中的数值。
		 *            如果要在此事件中定义或修改下拉框选中的值，请将新的值写入arg参数的selectedValue属性中。
		 *            如果将arg.selectedValue的值设置为undefined，则表示本次关闭下拉框时不向编辑框中传递任何值。
		 * @param {boolean}
		 *            #arg.processDefault=true 是否在事件结束后继续执行系统默认的赋值动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 * @see dorado.widget.DropDown#event:onClose
		 */
		onValueSelect : {}
	},

	createDropDownBox : function(editor) {
		var config = {
			editor : editor,
			dropDown : this,
			hideAnimateType : "none"
		};
		if (dorado.Browser.msie && dorado.Browser.version < 9) {
			config.showAnimateType =  "none";
		}
		return new dorado.widget.DropDownBox(config);
	},

	initDropDownBox : dorado._NULL_FUNCTION,
	
	onEditorMouseDown: function(editor) {
		if (this._autoOpen && !this.get("opened")) {
			this._skipEditorOnFocusProcedure= true;
			this.execute(editor);
		}
	},

	onEditorFocus: function(editor) {
		if (this._autoOpen && !this._skipEditorOnFocusProcedure) {
			$setTimeout(this, function() {
				this.execute(editor);
			}, 50);
		}
		delete this._skipEditorOnFocusProcedure;
	},

	onEditorKeyDown: function(editor, evt) {
		dorado.widget.disableKeyBubble = this._editor;
		try {
			return this.doOnEditorKeyDown ? this.doOnEditorKeyDown(editor, evt) : true;
		} finally {
			dorado.widget.disableKeyBubble = null;
		}
	},

	doOnEditorKeyDown : function(editor, evt) {
		var retValue = true;
		if (this.get("opened")) {
			switch (evt.keyCode) {
			case 27: // esc
				this.close();
				retValue = false;
				break;
			}
		}
		return retValue;
	},

	execute : function(editor) {
		if (this._skipExecute) return;
		this._skipExecute = true;
		$setTimeout(this, function() {
			delete this._skipExecute;
		}, 300);
		if (this.get("opened")) {
			this.close();
		} else {
			var arg = {
				editor: editor,
				processDefault: true
			}
			this.fireEvent("beforeExecute", this, arg);
			if (!arg.processDefault) return;
			this.open(editor);
		}
		$invokeSuper.call(this, arguments);
	},

	open : function(editor) {
		function getBoxCache(win) {
			var boxCache;
			try {
				if (win.dorado) {
					boxCache = win.dorado._DROPDOWN_BOX_CACHE;
					if (!boxCache)
						win.dorado._DROPDOWN_BOX_CACHE = boxCache = {};
				}
			} catch (e) {
				// do nothing
			}
			return boxCache;
		}

		if (this._box)
			this._box.hide();

		this._editor = editor;
		this.fireEvent("onOpen", this, {
			editor : editor
		});

		var dropdown = this, editorDom = editor.getDom();
		var win = $DomUtils.getOwnerWindow(editorDom) || window;
		var boxCache = getBoxCache(win);
		var box = boxCache ? boxCache[dorado.id + '$' + this._id] : null;
		if (!box) {
			box = this.createDropDownBox(editor);
			box.addListener("onDropDownBoxShow", function() {
				if (dropdown.onDropDownBoxShow) dropdown.onDropDownBoxShow();
			});
			(this._view || $topView).registerInnerControl(box);
			
			var viewElement;
			if (!(dorado.Browser.msie && dorado.Browser.version < 9)) {
				var view = editor._view;
				while (view) {
					if (view._rendered) viewElement = view.getContentContainer();
					view = view._view;
				}
			}
			box.render(viewElement || win.document.body);
			if (boxCache) boxCache[dorado.id + '$' + this._id] = box;
		}
		this._box = box;

		box._onBlurListener = function() {
			dropdown.close();
		};
		editor.addListener("onBlur", box._onBlurListener, {
			once : true
		});

		box._focusParent = editor;
		
		var realMaxHeight, targetElement = editor.getDom(), boxContainer = box.getDom().parentNode;
		var boxContainerHeight = boxContainer.clientHeight;
		realMaxHeight = boxContainerHeight;
		
		var offsetTargetTop = $fly(targetElement).offset().top;
		var offsetTargetBottom = boxContainerHeight - offsetTargetTop - targetElement.offsetHeight;
		var vAlign = "bottom";
		if (offsetTargetTop > offsetTargetBottom) {
			vAlign = "top";
			realMaxHeight = offsetTargetTop;
		}
		else {
			realMaxHeight = offsetTargetBottom;
		}
		this._realMaxWidth = this._maxWidth;
		this._realMaxHeight = (realMaxHeight < this._maxHeight) ? realMaxHeight : this._maxHeight;

		var boxWidth = this._width || $fly(editorDom).outerWidth();
		if (boxWidth > this._realMaxWidth) boxWidth = this._realMaxWidth;
		if (boxWidth < this._minWidth) boxWidth = this._minWidth;
		var boxHeight = this._height || 0;
		if (boxHeight > this._realMaxHeight) boxHeight = this._realMaxHeight;
		if (boxHeight < this._minHeight) boxHeight = this._minHeight;

		var boxDom = box.getDom(), containerElement = box.getContainerElement();
		with (boxDom.style) {
			left = -screen.availWidth + "px";
			top = 0;
			width = boxWidth + "px";
			height = boxHeight + "px";
			visibility = "hidden";
			display = '';
		}

		this.initDropDownBox(box, editor);
		box.init(editor);

		if (!this._width) {
			boxWidth = (dorado.Browser.mozilla) ? containerElement.firstChild.offsetWidth
					: containerElement.scrollWidth;
			if (boxWidth > this._realMaxWidth) boxWidth = this._realMaxWidth;
			if (boxWidth < this._minWidth) boxWidth = this._minWidth;
		}
		if (!this._height) {
			boxHeight = (dorado.Browser.mozilla) ? containerElement.firstChild.offsetHeight
					: containerElement.scrollHeight;
			if (boxHeight > this._realMaxHeight) boxHeight = this._realMaxHeight;
			if (boxHeight < this._minHeight) boxHeight = this._minHeight;
		}
		if (vAlign == "top" && boxHeight < offsetTargetBottom) {
			vAlign = "bottom";
		}
		
		var widthOverflow = boxDom.offsetParent.clientWidth - ($fly(targetElement).offset().left + boxWidth);
		if (widthOverflow > 0) widthOverflow = 0;
		
		boxDom.style.width = boxWidth + "px";
		boxDom.style.height = boxHeight + "px";
		box.show({
			anchorTarget : editor,
			editor : editor,
			align : "innerleft",
			offsetLeft: widthOverflow,
			vAlign : vAlign,
			autoAdjustPosition: false
		});
	},

	/**
	 * 关闭下拉框。
	 * @param {Object} selectedValue 下拉框返回给编辑框的选中值。<br>
	 * 如果设置此参数的值为undefined，那么相当于只是简单的关闭下拉框而不会向编辑框传递任何数值。
	 */
	close : function(selectedValue) {
		var editor = this._editor;
		var eventArg = {
			editor : editor,
			selectedValue : selectedValue,
			processDefault : true
		};
		this.fireEvent("onClose", this, eventArg);

		var box = this._box;
		if (!box) return;
					
		var entityForAssignment;
		if (this.getEntityForAssignment) {
			entityForAssignment = this.getEntityForAssignment();
		}
		
		this._box = null;
		this._editor = null;

		var self = this;
		editor.removeListener("onBlur", box._onBlurListener);
		box.hide();

		if (eventArg.selectedValue !== undefined) {
			this.fireEvent("onValueSelect", this, eventArg);
			if (eventArg.processDefault && eventArg.selectedValue !== undefined) {
				var lastPost = editor._lastPost;
				try {
					selectedValue = eventArg.selectedValue;
					entityForAssignment = entityForAssignment || selectedValue;

					var targetEntity = (editor._entity || editor._cellEditor && editor._cellEditor.data);
					if (this._assignmentMap && entityForAssignment && entityForAssignment instanceof Object && targetEntity && targetEntity instanceof Object) {
						var assignmentMap = this._assignmentMap, maps = [];
						assignmentMap = assignmentMap.replace(/,/g, ";").split(';');
						for ( var i = 0; i < assignmentMap.length; i++) {
							var map = assignmentMap[i], index = map.indexOf('=');
							if (index >= 0) {
								maps.push({
									writeProperty : map.substring(0, index),
									readProperty : map.substring(index + 1)
								});
							} else {
								maps.push({
									writeProperty : map,
									readProperty : map
								});
							}
						}

						for ( var i = 0; i < maps.length; i++) {
							var map = maps[i], value;
							if (map.readProperty == "$this") {
								value = entityForAssignment;
							} else {
								value = (entityForAssignment instanceof dorado.Entity) ? entityForAssignment.get(map.readProperty) : entityForAssignment[map.readProperty];
							}

							if (value instanceof dorado.Entity || value instanceof dorado.EntityList) {
								value = dorado.Core.clone(value);
							}

							if (targetEntity instanceof dorado.Entity)
								targetEntity.set(map.writeProperty, value);
							else
								targetEntity[map.writeProperty] = value;
						}
					} else {
						if (selectedValue instanceof dorado.Entity || selectedValue instanceof dorado.EntityList) {
							selectedValue = dorado.Core.clone(selectedValue);
						}
						editor.set("value", selectedValue);
					}
				} finally {
					editor._lastPost = lastPost;
				}
				if (this._postValueOnSelect) editor.post();
			}
		}
	}

});

/**
 * 根据传入的控件查找其所属的下拉框。
 * <p>
 * 此功能一般用于{@link dorado.widget.CustomDropDown}。
 * 例如：我们在自定义下拉框中放置了一个关闭按钮，该关闭按钮需要知道自己关闭的哪个下拉框，此时即可使用此方法查找其所属的下拉框。
 * </p>
 * 
 * @param {dorado.widget.Control}
 *            control 任意的控件。
 * @return {dorado.widget.DropDown} 所属的下拉框。
 * 
 * @example // 例如自定义下拉框中的关闭按钮可以这样定义 new dorado.widget.Button({ caption: "Close",
 *          onClick: function(self, arg) {
 *          dorado.widget.DropDown.findDropDown(self).close(); } });
 */
dorado.widget.DropDown.findDropDown = function(control) {
	var box = control.findParent(dorado.widget.DropDownBox);
	return box ? box.get("dropDown") : null;
};

dorado.widget.DropDownBox = $extend([ dorado.widget.Control, dorado.widget.FloatControl ], /** @scope dorado.widget.DropDownBox.prototype */
{
	$className : "dorado.widget.DropDownBox",
	_inherentClassName : "i-drop-down-box",

	ATTRIBUTES : /** @scope dorado.widget.DropDownBox.prototype */
	{
		className : {
			defaultValue : "d-drop-down-box"
		},

		showAnimateType : {
			defaultValue : "safeSlide"
		},

		hideAnimateType : {
			defaultValue : "safeSlide"
		},

		visible : {
			defaultValue : false
		},

		editor : {},

		dropDown : {},

		control : {
			writeOnce : true,
			setter : function(control) {
				if (this._control == control) return;
				this._control = control;
				if (control) {
					this.registerInnerControl(control);
				}
			}
		}
	},

	EVENTS : /** @scope dorado.widget.DropDownBox.prototype */
	{
		onDropDownBoxShow : {}
	},

	createDom : function() {
		var json = {
			tagName : "DIV",
			style : {
				position : "absolute",
				display : "none"
			},
			content : {
				tagName : "DIV",
				style : {
					overflow : "visible",
					height: "100%"
				}
			}
		};
		if (dorado.Browser.msie && dorado.Browser.version < 7) {
			json.content.style.width = "100%";
			json.content.style.height = "100%";
		}
		return $DomUtils.xCreate(json);
	},

	init : function(editor) {
		if (this._control)
			this._control.render(this.getContainerElement());
	},

	getContainerElement : function() {
		return this.getDom().firstChild;
	},

	doBeforeShow : function(editor) {
		$invokeSuper.call(this, arguments);
		this.refresh();
	},

	doAfterShow : function(editor) {
		$invokeSuper.call(this, arguments);
		this.fireEvent("onDropDownBoxShow", this);
	},

	doBeforeHide : function() {
		$invokeSuper.call(this, arguments);
		this._closed = true;
	}
});

dorado.widget.View.registerDefaultComponent("triggerClear", function() {
	return new dorado.widget.Trigger({
		exClassName : "d-trigger-clear",
		iconClass : "d-trigger-icon-clear",
		onExecute : function(self, arg) {
			arg.editor.set("text", "");
		}
	});
});
