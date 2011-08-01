(function() {
	dorado.widget.htmleditor = {};

	/**
	 * @author frank.zhang
	 */
	var HtmlEditorUtils = dorado.widget.htmleditor.HtmlEditorUtils = {};

	HtmlEditorUtils.execCommand = function(htmlEditor, cmd, showGui, arg) {
		var contentFrame = htmlEditor._doms.contentFrame, contentFrameDoc = contentFrame.contentWindow.document;
		if (dorado.Browser.msie) {
			var lastRange = contentFrame.lastRange;
			contentFrame.contentWindow.focus();
			if (lastRange) {
				lastRange.select();
				contentFrameDoc.execCommand(cmd, showGui, arg);
			}
		} else {
			contentFrameDoc.execCommand(cmd, showGui, arg);
		}
	};

	HtmlEditorUtils.formatFontSize = function(size) {
		var sizeArray = ["最小","较小","小","正常","大","较大","最大"];
		if (!size) {
			return "";
		} else {
			return sizeArray[size - 1];
		}
	};

	HtmlEditorUtils.formatFormatBlock = function(formatBlock) {
		if (!formatBlock || formatBlock == "x") {
			return "";
		}
		if (!dorado.Browser.msie) {
			if (/^h\d$/.test(formatBlock)) {
				return formatBlock.replace("h", "标题 ");
			} else {
				return "";
			}
		} else {
			return formatBlock;
		}
	};

	HtmlEditorUtils.queryCommandValue = function(htmlEditor, cmd) {
		try {
			return htmlEditor._doms.contentFrame.contentWindow.document.queryCommandValue(cmd);
		}
		catch(e) {
		}
	};

	HtmlEditorUtils.queryCommandState = function(htmlEditor, cmd) {
		try {
			return htmlEditor._doms.contentFrame.contentWindow.document.queryCommandState(cmd);
		}
		catch(e) {
		}
	};

	HtmlEditorUtils.queryCommandEnabled = function(htmlEditor, cmd) {
		try {
			if (!htmlEditor._doms.contentFrame.contentWindow.document.queryCommandEnabled(cmd)) {
				return "disable";
			} else {
				return "enable";
			}
		}
		catch (e) {
			return "disable";
		}
	};

	HtmlEditorUtils.getContent = function(htmlEditor) {
		try {
			if (htmlEditor._status == "visual") {
				var contentFrame = htmlEditor._doms.contentFrame;
				return contentFrame.contentWindow.document.body.innerHTML;
			} else {
				var sourceEditor = htmlEditor._doms.sourceEditor;
				return sourceEditor.value;
			}
		}
		catch(e) {
		}
	};

	HtmlEditorUtils.setContent = function(htmlEditor, content) {
		try {
			if (htmlEditor._status == "visual") {
				var contentFrame = htmlEditor._doms.contentFrame;
				contentFrame.contentWindow.document.body.innerHTML = content;
			} else {
				var sourceEditor = htmlEditor._doms.sourceEditor;
				sourceEditor.value = content;
			}
		}
		catch(e) {
		}
	};

	HtmlEditorUtils.insertHtml = function(htmlEditor, html) {
		var lastRange = htmlEditor._doms.contentFrame.lastRange;
		if (dorado.Browser.msie) {
			if (lastRange) {
				try {
					htmlEditor._doms.contentFrame.contentWindow.focus();
					lastRange.collapse(false);
					lastRange.pasteHTML(html);
					lastRange.select();
				}
				catch(e) {
				}
			}
		} else {
			HtmlEditorUtils.execCommand(htmlEditor, 'insertHTML', false, html);
		}
	};

	HtmlEditorUtils.print = function(htmlEditor) {
		if (dorado.Browser.msie) {
			htmlEditor._doms.contentFrame.contentWindow.document.execCommand('print');
		} else {
			htmlEditor._doms.contentFrame.contentWindow.print();
		}
	};

	HtmlEditorUtils.view = function(htmlEditor) {
		var viewWin = window.open('', "_blank", '');
		viewWin.document.open('text/html', 'replace');
		viewWin.opener = null;
		viewWin.document.writeln(htmlEditor._doms.contentFrame.contentWindow.document.body.innerHTML);
		viewWin.document.close();
	};

	HtmlEditorUtils.initEditable = function(editor) {
		try {
			var doms = editor._doms, contentFrameEl = doms.contentFrame;
			contentFrameEl.contentWindow.document.designMode = "on";

			var contentFrameDoc = contentFrameEl.contentWindow.document;

			/**
			 * 直接设置innerHTML导致在IE下运行错误。
			 */
			contentFrameDoc.open();
			contentFrameDoc.write("<html><head></head><body style=\"font-size: 9pt;\"><p></p></body></html>");
			contentFrameDoc.close();

			/**
			 * 因为IE才放在这里。
			 */
			$fly(contentFrameDoc).mouseup(function() {
				editor.checkStatus();
			});

			if (dorado.Browser.msie) {
				$fly(contentFrameDoc.body).bind("beforedeactivate", function() {
					contentFrameEl.lastRange = contentFrameDoc.selection.createRange();
					editor.onChange();
				});

				$fly(contentFrameDoc).bind("selectionchange", function() {
				   editor.checkStatus();
				}).bind("keydown", function() {
					//Ctrl + Z | Ctrl + Y
					if (event.ctrlKey) {
						var keyCode = event.keyCode;
						if (keyCode == 89) {
							editor.undoManager.redo();
						} else if (keyCode == 90) {
							editor.undoManager.undo();
						}
						editor.checkStatus();
					}
				});

				$fly(contentFrameEl).bind("focus", function() {
					dorado.widget.setFocusedControl(editor);
				});

			} else {
				$fly(contentFrameDoc).bind("keydown", function() {
					editor.checkStatus();
				});
				if (dorado.Browser.webkit) {
					$fly(contentFrameDoc.body).bind("blur", function() {
						editor.onChange();
					}).bind("focus", function() {
						dorado.widget.setFocusedControl(editor);
					});
				} else {
					$fly(contentFrameDoc).bind("blur", function() {
						editor.onChange();
					}).bind("focus", function() {
						dorado.widget.setFocusedControl(editor);
					});
				}
			}

			editor.checkStatus();
		}
		catch (e) {
			setTimeout(function() {
				HtmlEditorUtils.initEditable(editor);
			}, 250);
		}
	};

	dorado.widget.htmleditor.UndoManager = $class({
		constructor: function(htmlEditor) {
			this.index = -1;
			this.data = new Array();
			this.typing = false;
			this.maxLevels = 25;
			this.htmlEditor = htmlEditor;
		},
		saveUndoStep: function() {
			if (this.htmlEditor._status != "visual")
				return;

			var content = HtmlEditorUtils.getContent(this.htmlEditor);

			if (this.index >= 0 && content == this.data[this.index][0])
				return;

			if (this.index + 1 >= this.maxLevels) {
				this.data.shift();
			} else {
				this.index++;
			}

			var bookMark;
			if (this.htmlEditor._doms.contentFrame.document.selection.type == 'Text') {
				bookMark = this.htmlEditor._doms.contentFrame.document.selection.createRange().getBookmark();
			}

			this.data[this.index] = [content, bookMark];
		},
		hasUndo: function() {
			return this.index > 0 || this.typing;
		},
		hasRedo: function() {
			return this.index < this.data.length - 1;
		},
		apply: function(level) {
			var self = this, data = self.data[level];
			if (!data) {
				return;
			}

			HtmlEditorUtils.setContent(self.htmlEditor, data[0]);

			if (data[1]) {
				var range = self.htmlEditor._doms.contentFrame.document.selection.createRange();
				range.moveToBookmark(data[1]);
				range.select();
			}

			self.typing = false;
		},
		undo: function() {
			if (this.hasUndo()) {
				if (this.index == (this.data.length - 1)) {
					this.saveUndoStep();
				}
				this.apply(--this.index);
			}
		},
		redo: function() {
			if (this.hasRedo()) {
				this.apply(++this.index);
				this.saveUndoStep();
			}
		}
	});

	var ToolBarItems = [
		["Source", "|", "New", "Copy", "Paste", "Cut", "|", "Undo", "Redo", "|","SelectAll","ClearFormat","Print","|","View","Help"],
		["Bold","Italic","UnderLine","|","Indent","Outdent","|","OrderedList","UnOrderedList","|","JustifyCenter","JustifyLeft","JustifyRight",
			"JustifyFull","|","CreateLink","UnLink","|","HR","Table","Image","Face"],
		["Format","FontName","FontSize","|","FontColor","BGColor"]
	];

	var CheckStatusItems = [
		"Copy","Paste","Cut","Undo","Redo",
		"Bold","Italic","UnderLine","OrderedList","UnOrderedList","JustifyCenter","JustifyLeft","JustifyRight","JustifyFull",
		"Format","FontName","FontSize"
	];

	var plugins = {};

	dorado.widget.HtmlEditor = $extend(dorado.widget.Control, {
		ATTRIBUTES: {
			className: {
				defaultValue: "d-html-editor"
			},
			status: {
				defaultValue: "visual"
			}
		},
		createDom: function() {
			var editor = this, doms = {}, dom = $DomUtils.xCreateElement({
				tagName: "div",
				className: editor._className,
				content: [
					{
						tagName: "div",
						className: "toolbar-wrap",
						contextKey: "toolbar"
					},
					{
						tagName: "iframe",
						className: "content-frame",
						tabIndex: -1,
						frameBorder: 0,
						contextKey: "contentFrame"
					},
					{
						tagName: "textarea",
						className: "source-editor",
						contextKey: "sourceEditor"
					}
				]
			}, null, doms);

			editor._doms = doms;

			HtmlEditorUtils.initEditable(editor);
			editor.undoManager = new dorado.widget.htmleditor.UndoManager(editor);

			editor.initPlugins();

			return dom;
		},
		initPlugins: function() {
			var editor = this;
			editor._plugins = {};
			for (var i = 0, k = ToolBarItems.length; i < k; i++) {
				var toolbarConfig = ToolBarItems[i], toolbar = new dorado.widget.ToolBar();
				editor.registerInnerControl(toolbar);
				toolbar.render(editor._doms.toolbar);
				for (var j = 0, l = toolbarConfig.length; j < l; j++) {
					var pluginName = toolbarConfig[j];
					if (pluginName == "|") {
						toolbar.addItem("-");
					} else {
						var pluginConfig = plugins[pluginName];
						pluginConfig.htmlEditor = editor;
						var plugin = new dorado.widget.htmleditor.HtmlEditorPlugIn(pluginConfig);

						if (pluginConfig.execute) {
							plugin.execute = pluginConfig.execute;
						}

						if (pluginConfig.initToolBar) {
							plugin.initToolBar = pluginConfig.initToolBar;
						}

						if (pluginConfig.checkStatus) {
							plugin.checkStatus = pluginConfig.checkStatus;
						}

						if (pluginConfig.onStatusChange) {
							plugin.onStatusChange = pluginConfig.onStatusChange;
						}

						plugin.initToolBar(toolbar);

						editor._plugins[pluginConfig.name] = plugin;
					}
				}
			}
		},
		doOnResize: function() {
			var editor = this, dom = editor._dom, doms = editor._doms;
			if (dom) {
				var toolBarHeight = doms.toolbar.offsetHeight, height = dom.clientHeight;
				doms.contentFrame.style.height = height - toolBarHeight + "px";
				doms.sourceEditor.style.height = height - toolBarHeight + "px";
			}
		},
		switchMode: function() {
			var editor = this, dom = editor._dom, doms = editor._doms, plugins, name, plugin;
			if (editor._status == "visual") {
				$fly(dom).addClass(editor._className + "-source-mode");
				doms.sourceEditor.value = HtmlEditorUtils.getContent(editor);
				doms.sourceEditor.focus();

				plugins = editor._plugins;
				for (name in plugins) {
					plugin = plugins[name];
					if (plugin && plugin._name != "Source") {
						plugin.set("status", "disable");
					}
				}

				editor._status = "source";
			} else {
				$fly(dom).removeClass(editor._className + "-source-mode");
				var contentFrame = doms.contentFrame;
				contentFrame.contentWindow.document.body.innerHTML = doms.sourceEditor.value;

				plugins = editor._plugins;
				for (name in plugins) {
					plugin = plugins[name];
					if (plugin && plugin._name != "Source") {
						plugin.set("status", "enable");
					}
				}

				editor.checkStatus();
				editor._status = "visual";
			}
		},
		checkStatus: function() {
			var editor = this;
			for (var i = 0 , j = CheckStatusItems.length; i < j; i++) {
				if (editor._plugins[CheckStatusItems[i]].checkStatus) {
					editor._plugins[CheckStatusItems[i]].checkStatus();
				}
			}
		},
		onChange: function() {
		}
	});

	dorado.widget.htmleditor.HtmlEditorPlugIn = $extend(dorado.widget.SimpleIconButton, {
		ATTRIBUTES: {
			name: {},
			label: {},
			icon: {},
			iconClass: {},
			command: {},
			htmlEditor: {},
			statusCheckable: {},
			status: {
				setter: function(value) {
					this.onStatusChange && this.onStatusChange(value);
					this._status = value;
				}
			}
		},
		onStatusChange: function(status) {
			var plugin = this, button = plugin.button;
			if (button) {
				button.set("disabled", status == "disable");
				if (status == "on") {
					button.set("toggled", true);
				} else {
					button.set("toggled", false);
				}
			}
		},
		execute: function() {
			var plugin = this, htmlEditor = plugin._htmlEditor;
			if (plugin._command && htmlEditor) {
				HtmlEditorUtils.execCommand(htmlEditor, plugin._command, false, null);
			}
		},
		initToolBar: function(toolbar) {
			var plugin = this;
			plugin.button = toolbar.addItem(
			{
				$type: "SimpleIconButton",
				icon: plugin._icon,
				iconClass: plugin._iconClass,
				listener: {
					onClick: function() {
						plugin.onClick();
					}
				}
			});
		},
		onClick: function() {
			var plugin = this;
			if (plugin._status != "disable") {
				if (plugin._command != "redo" && plugin._command != "undo") {
					if (dorado.Browser.msie) {
						plugin.htmlEditor.undoManager.saveUndoStep();
					}
				}
				plugin.execute.apply(this, arguments);
				plugin._htmlEditor.checkStatus();
			}
		},
		checkStatus: function() {
			var plugin = this, contentFrameDoc = plugin._htmlEditor._doms.contentFrame.contentWindow.document;
			if (plugin._statusCheckable && plugin._status != "disable") {
				try {
					var result = contentFrameDoc.queryCommandState(plugin._command);
					if (result) {
						plugin.set("status", "on");
					} else {
						plugin.set("status", "enable");
					}
				} catch(e) {
				}
			}
		}
	});

	plugins.Source = {
		name: "Source",
		label: "源文件",
		iconClass: "source",
		command: null,
		execute: function() {
			this._htmlEditor.switchMode();
		}
	};

	plugins.New = {
		name: "New",
		label: "新建",
		iconClass: "new",
		command: null,
		execute: function() {
			if (this._htmlEditor._doms.contentFrame.contentWindow.document.body) {
				this._htmlEditor._doms.contentFrame.contentWindow.document.body.innerHTML = "<p>&nbsp;</p>";
			}
		}
	};

	plugins.Copy = {
		name: "Copy",
		label: "复制",
		iconClass: "copy",
		command: "Copy",
		statusCheckable: true,
		checkStatus: function() {
			if (!dorado.Browser.msie) {
				this.set("status", "disable");
			}
		}
	};

	plugins.Paste = {
		name: "Paste",
		label: "粘贴",
		iconClass: "paste",
		command: "Paste",
		statusCheckable: true,
		checkStatus: function() {
			if (!dorado.Browser.msie) {
				this.set("status", "disable");
			}
		}
	};

	plugins.Cut = {
		name: "Cut",
		label: "剪切",
		iconClass: "cut",
		command: "Cut",
		statusCheckable: true,
		checkStatus: function() {
			if (!dorado.Browser.msie) {
				this.set("status", "disable");
			}
		}
	};

	plugins.Undo = {
		name: "Undo",
		label: "撤销",
		iconClass: "undo",
		command: "undo",
		statusCheckable: true,
		execute: function() {
			var htmlEditor = this._htmlEditor;
			if (dorado.Browser.msie) {
				htmlEditor.undoManager.undo();
			} else {
				HtmlEditorUtils.execCommand(htmlEditor, 'undo', false, null);
			}
			htmlEditor.checkStatus();
		},
		checkStatus: function() {
			if (dorado.Browser.msie) {
				var undoManager = this._htmlEditor.undoManager;
				if (!undoManager.hasUndo()) {
					this.set("status", "disable");
				} else {
					this.set("status", "enable");
				}
			} else {
				var htmlEditor = this._htmlEditor;
				var value = HtmlEditorUtils.queryCommandEnabled(htmlEditor, "Undo");
				if (!value) {
					this.set("status", "disable");
				} else {
					this.set("status", "enable");
				}
			}
		}
	};

	plugins.Redo = {
		name: "Redo",
		label: "重做",
		iconClass: "redo",
		command: "redo",
		statusCheckable: true,
		execute: function() {
			var htmlEditor = this._htmlEditor;
			if (dorado.Browser.msie) {
				htmlEditor.undoManager.redo();
			} else {
				HtmlEditorUtils.execCommand(htmlEditor, 'Redo', false, null);
			}
			htmlEditor.checkStatus();
		},
		checkStatus: function() {
			if (dorado.Browser.msie) {
				var undoManager = this._htmlEditor.undoManager;
				if (!undoManager.hasRedo()) {
					this.set("status", "disable");
				} else {
					this.set("status", "enable");
				}
			} else {
				var htmlEditor = this._htmlEditor;
				var value = HtmlEditorUtils.queryCommandEnabled(htmlEditor, "Redo");
				if (value == "disable") {
					this.set("status", "disable");
				} else {
					this.set("status", "enable");
				}
			}
		}
	};

	plugins.SelectAll = {
		name: "SelectAll",
		label: "全选",
		iconClass: "select-all",
		command: "SelectAll"
	};

	plugins.ClearFormat = {
		name: "ClearFormat",
		label: "清除格式",
		iconClass: "clear-format",
		command: "RemoveFormat"
	};

	plugins.Print = {
		name: "Print",
		label: "打印",
		iconClass: "print",
		command: null,
		execute: function() {
			HtmlEditorUtils.print(this._htmlEditor);
		}
	};

	plugins.View = {
		name: "View",
		label: "预览",
		iconClass: "document-preview",
		command: null,
		execute: function() {
			HtmlEditorUtils.view(this._htmlEditor);
		}
	};

	plugins.Help = {
		name: "Help",
		label: "关于",
		iconClass: "help",
		command: null,
		execute: function() {
			var plugin = this;
			if (!plugin.dialog) {
				plugin.dialog = new dorado.widget.Dialog({
					caption: "关于",
					width: 300,
					height: 200,
					center: true,
					buttons: [{
						caption: "确定",
						listener: {
							onClick: function() {
								plugin.dialog.hide();
							}
						}
					}],
					children: [{
						$type: "HtmlContainer",
						content: "<div style='text-align:center;'>Dorado Html Editor</div>"
					}]
				});
			}
			plugin.dialog.show();
		}
	};

	plugins.Bold = {
		name: "Bold",
		label: "粗体",
		iconClass: "text-bold",
		command: "Bold",
		statusCheckable: true
	};

	plugins.Italic = {
		name: "Italic",
		label: "斜体",
		iconClass: "text-italic",
		command: "Italic",
		statusCheckable: true
	};

	plugins.UnderLine = {
		name: "UnderLine",
		label: "下划线",
		iconClass: "text-underline",
		command: "UnderLine",
		statusCheckable: true
	};


	plugins.Indent = {
		name: "Indent",
		label: "增大缩进量",
		iconClass: "indent",
		command: "indent",
		statusCheckable: true
	};

	plugins.Outdent = {
		name: "Outdent",
		label: "减少缩进量",
		iconClass: "outdent",
		command: "outdent",
		statusCheckable: true
	};

	plugins.OrderedList = {
		name: "OrderedList",
		label: "有序列表",
		iconClass: "ordered-list",
		command: "insertOrderedList",
		statusCheckable: true
	};

	plugins.UnOrderedList = {
		name: "UnOrderedList",
		label: "无序列表",
		iconClass: "unordered-list",
		command: "insertUnOrderedList",
		statusCheckable: true
	};

	plugins.JustifyCenter = {
		name: "JustifyCenter",
		label: "中间对齐",
		iconClass: "justify-center",
		command: "justifyCenter",
		statusCheckable: true
	};

	plugins.JustifyLeft = {
		name: "JustifyLeft",
		label: "向左对齐",
		iconClass: "justify-left",
		command: "justifyLeft",
		statusCheckable: true
	};

	plugins.JustifyRight = {
		name: "JustifyRight",
		label: "向右对齐",
		iconClass: "justify-right",
		command: "justifyRight",
		statusCheckable: true
	};

	plugins.JustifyFull = {
		name: "JustifyFull",
		label: "两端对齐",
		iconClass: "justify-full",
		command: "justifyFull",
		statusCheckable: true
	};

	plugins.CreateLink = {
		name: "CreateLink",
		label: "超链接",
		iconClass: "add-link",
		command: "CreateLink",
		execute: function() {
			var plugin = this, htmlEditor = plugin._htmlEditor, urlObject = {};

			if (!plugin.dialog) {
				plugin.dialog = new dorado.widget.Dialog(
				{
					caption: "插入超链接",
					width: 340,
					center: true,
					children: [{
						$type: "AutoForm",
						entity: urlObject,
						style: "margin-bottom:12px",
						elements: [
							{ property: "url", label: "超链接", type: "text", colSpan: 2 }
						]
					}],
					buttons: [{
						caption: "确定",
						listener: {
							onClick: function() {
								if (urlObject) {
									var url = urlObject.url;
									HtmlEditorUtils.execCommand(htmlEditor, "CreateLink", false, url);
									plugin.dialog.hide();
								}
							}
						}
					}, {
						caption: "取消",
						listener: {
							onClick: function() {
								plugin.dialog.hide();
							}
						}
					}]
				});
			}
			plugin.dialog.show();
		}
	};

	plugins.UnLink = {
		name: "UnLink",
		label: "取消超链接",
		iconClass: "break-link",
		command: "Unlink",
		statusCheckable: true
	};

	plugins.HR = {
		name: "HR",
		label: "插入分割线",
		iconClass: "hr",
		command: "inserthorizontalrule"
	};

	function StringBuffer(){
		this._string_=new Array;
	}

	StringBuffer.prototype.append = function(str){
		this._string_.push(str);
	};

	StringBuffer.prototype.toString = function(){
		return this._string_.join("");
	};

	plugins.Table = {
		name: "Table",
		label: "插入表格",
		iconClass: "table",
		command: null,
		execute: function() {
			var plugin = this, tableConfig = {};

			if (!plugin.dialog) {
				plugin.dialog = new dorado.widget.Dialog(
				{
					caption: "插入表格",
					width: 480,
					center: true,
					children: [{
						$type: "FieldSet",
						caption: "基本信息",
						children: [{
							$type: "AutoForm",
							entity: tableConfig,
							style: "margin-bottom:12px",
							elements: [
								{ property: "row", label: "行数", type: "text"},
								{ property: "column", label: "列数", type: "text"},
								{ property: "width", label: "宽度", type: "text"}
							]
						}]
					}, {
						$type: "FieldSet",
						caption: "其他信息",
						children: [{
							$type: "AutoForm",
							entity: tableConfig,
							style: "margin-bottom: 12px",
							elements: [
								{ property: "border", label: "行数", type: "text"},
								{ property: "cellpadding", label: "单元格边距", type: "text"},
								{ property: "cellspacing", label: "单元格间距", type: "text"}
							]
						}]
					}],
					buttons: [{
						caption: "确定",
						listener: {
							onClick: function() {
								if (tableConfig) {
									var buffer = new StringBuffer();
									buffer.append("<table");

									var border = tableConfig.border;
									if (border == undefined) {
										border = 1;
									}

									buffer.append(" border=" + border);

									var cellpadding = tableConfig.cellpadding;
									if (cellpadding == undefined) {
										cellpadding = 1;
									}

									buffer.append(" cellpadding=" + cellpadding);

									var cellspacing = tableConfig.cellspacing;
									if (cellspacing == undefined) {
										cellspacing = 1;
									}

									buffer.append(" cellspacing=" + cellspacing);

									var width = tableConfig.width;
									if (!width) {
										width = "100%";
									}
									buffer.append(" width=" + width);

									buffer.append(">");

									var row = tableConfig.row;
									var column = tableConfig.column;

									for (var i = 0; i < row; i++) {
										buffer.append("<tr>");
										for (var j = 0; j < column; j++) {
											buffer.append("<td>");
											buffer.append("</td>");
										}
										buffer.append("</tr>");
									}

									buffer.append("</table>");

									HtmlEditorUtils.insertHtml(plugin._htmlEditor, buffer.toString());

									plugin.dialog.hide();
								}
							}
						}
					}, {
						caption: "取消",
						listener: {
							onClick: function() {
								plugin.dialog.hide();
							}
						}
					}]
				});
			}
			plugin.dialog.show();
		}
	};

	plugins.Image = {
		name: "Image",
		label: "插入图片",
		iconClass: "img",
		command: null,
		execute: function() {
			var plugin = this, imgObject = {};
			if (!plugin.dialog) {
				plugin.dialog = new dorado.widget.Dialog(
				{
					caption: "插入图像",
					width: 340,
					center: true,
					children: [{
						$type: "AutoForm",
						entity: imgObject,
						style: "margin-bottom:12px",
						elements: [
							{ property: "url", label: "图片链接", type: "text", colSpan: 2 }
						]
					}],
					buttons: [{
						caption: "确定",
						listener: {
							onClick: function() {
						        var value = imgObject.url;
								if (value) {
									HtmlEditorUtils.insertHtml(plugin._htmlEditor, "<img src='" + value + "'/>");
									plugin.dialog.hide();
								}
							}
						}
					}, {
						caption: "取消",
						listener: {
							onClick: function() {
								plugin.dialog.hide();
							}
						}
					}]
				});
			}
			plugin.dialog.show();
		}
	};

	plugins.Face = {
		name: "Face",
		label: "插入表情",
		iconClass: "face",
		command: null,
		execute: function() {
			var plugin = this, facePicker = plugin.facePicker;

			if (!facePicker) {
				facePicker = plugin.facePicker = new dorado.widget.FacePicker();
			}

			function select(self, arg) {
				HtmlEditorUtils.insertHtml(plugin._htmlEditor, "<img src='" + arg.image + "'/>");
			}

			facePicker.addListener("beforeShow", function() {
				facePicker.addListener("onSelect", select);
			}, { once: true });

			facePicker.addListener("onHide", function() {
				facePicker.removeListener("onSelect", select);
			});

			plugin.facePicker.show({
				anchorTarget: plugin.button,
				vAlign: "bottom"
			});
		}
	};

	plugins.Format = {
		name: "Format",
		label: "格式",
		icon: null,
		command: null,
		initToolBar: function(toolbar) {
			var plugin = this;

			toolbar.addItem({
				$type: "Label",
				text: "格式",
				style: {
					"margin-left": 3,
					"margin-right": 5
				}
			});

			var format = new dorado.EntityDataType({
				name: "dataTypeFormat",
				propertyDefs: [
					{
						name: "format",
						label: "格式",
						mapping: [
							{ key: "H1", value: "标题 1" },
							{ key: "H2", value: "标题 2" },
							{ key: "H3", value: "标题 3" },
							{ key: "H4", value: "标题 4" },
							{ key: "H5", value: "标题 5" },
							{ key: "H6", value: "标题 6" }
						]
					}
				]
			}), formats = new dorado.AggregationDataType("formats", format);

			var dropdown = new dorado.widget.ListDropDown({
				property: "format",
				items: new dorado.EntityList(
				[
					{format: "H1"},
					{format: "H2"},
					{format: "H3"},
					{format: "H4"},
					{format: "H5"},
					{format: "H6"}
				], $dataTypeRepository, formats)
			});

			var entity = new dorado.Entity();

			var formatEditor = new dorado.widget.TextEditor({
				width: 100,
				trigger: dropdown,
				entity: entity,
				property: "format",
				supportsDirtyFlag: false,
				listener: {
					onPost: function(self) {
						HtmlEditorUtils.execCommand(plugin._htmlEditor, 'formatblock', false, "<" + self.get("text") + ">");
						plugin.checkStatus();
					}
				}
			});
			plugin.formatEditor = formatEditor;
			toolbar.addItem(formatEditor);
		},
		onStatusChange: function(status) {
			this.formatEditor.set("readOnly", status == "disable");
		},
		statusCheckable: true,
		checkStatus: function() {
			var plugin = this, htmlEditor = plugin._htmlEditor, value = HtmlEditorUtils.queryCommandValue(htmlEditor, "formatblock");
			plugin.formatEditor.set("text", HtmlEditorUtils.formatFormatBlock(value));
		}
	};

	plugins.FontName = {
		name: "FontName",
		label: "字体",
		command: null,
		initToolBar: function(toolbar) {
			var plugin = this;

			toolbar.addItem(
			{
				$type: "Label",
				text: "字体",
				style: {
					"margin-left": 3,
					"margin-right": 5
				}
			});

			var dropdown = new dorado.widget.ListDropDown({
				items: ["宋体", "黑体", "隶书", "楷体", "幼圆", "Arial", "Impact", "Georgia", "Verdana", "Courier New", "Times New Roman"],
				listener: {
					onOpen: function(self) {
						setTimeout(function() {
							var rowList = self._box.get("control");
							rowList.addListener("onRenderRow", function(self, arg) {
								arg.dom.style.fontFamily = arg.data;
							});
						}, 0);
					}
				}
			});

			var entity = new dorado.Entity();

			var fontEditor = new dorado.widget.TextEditor({
				width: 100,
				trigger: dropdown,
				entity: entity,
				property: "fontname",
				supportsDirtyFlag: false,
				listener: {
					onPost: function(self, arg) {
						HtmlEditorUtils.execCommand(plugin._htmlEditor, "fontname", false, self.get("text"));
						plugin.checkStatus();
					}
				}
			});
			plugin.fontEditor = fontEditor;
			toolbar.addItem(fontEditor);
		},
		onStatusChange: function(status) {
			this.fontEditor.set("readOnly", status == "disable");
		},
		statusCheckable: true,
		checkStatus: function() {
			var plugin = this, htmlEditor = this._htmlEditor, value = HtmlEditorUtils.queryCommandValue(htmlEditor, "FontName");
			plugin.fontEditor.set("text", value);
		}
	};

	plugins.FontSize = {
		name: "FontSize",
		label: "大小",
		command: null,
		initToolBar: function(toolbar) {
			var plugin = this;

			toolbar.addItem(
			{
				$type: "Label",
				text: "大小",
				style: {
					"margin-left": 3,
					"margin-right": 5
				}
			});

			var fontsize = new dorado.EntityDataType({
				name: "dataTypeFormat",
				propertyDefs: [
					{
						name: "fontsize",
						label: "格式",
						mapping: [
							{ key: "1", value: "最小" },
							{ key: "2", value: "较小" },
							{ key: "3", value: "小" },
							{ key: "4", value: "正常" },
							{ key: "5", value: "大" },
							{ key: "6", value: "较大" },
							{ key: "7", value: "最大" }
						]
					}
				]
			}), fontsizes = new dorado.AggregationDataType("fontsizes", fontsize);

			var dropdown = new dorado.widget.ListDropDown({
				property: "fontsize",
				items: new dorado.EntityList(
				[
					{fontsize: "1"},
					{fontsize: "2"},
					{fontsize: "3"},
					{fontsize: "4"},
					{fontsize: "5"},
					{fontsize: "6"},
					{fontsize: "7"}
				], $dataTypeRepository, fontsizes)
			});

			var entity = new dorado.Entity();

			var fontSizeEditor = new dorado.widget.TextEditor({
				width: 100,
				trigger: dropdown,
				entity: entity,
				property: "fontsize",
				supportsDirtyFlag: false,
				listener: {
					onPost: function(self) {
						HtmlEditorUtils.execCommand(plugin._htmlEditor, "fontsize", false, self.get("text"));
						plugin.checkStatus();
					}
				}
			});
			plugin.fontSizeEditor = fontSizeEditor;
			toolbar.addItem(fontSizeEditor);
		},
		onStatusChange: function(status) {
			this.fontSizeEditor.set("readOnly", status == "disable");
		},
		statusCheckable: true,
		checkStatus: function() {
			var plugin = this, htmlEditor = plugin._htmlEditor, value = HtmlEditorUtils.queryCommandValue(htmlEditor, "FontSize");
			plugin.fontSizeEditor.set("text", HtmlEditorUtils.formatFontSize(value));
		}
	};

	plugins.FontColor = {
		name: "FontColor",
		label: "前景色",
		iconClass: "font-color",
		command: null,
		execute: function() {
			var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

			if (!colorPicker) {
				colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
			}

			function select(self, arg) {
				HtmlEditorUtils.execCommand(plugin._htmlEditor, 'forecolor', false, arg.color);
			}

			function clearColor(self) {
				select(self, { color: "#000" });
			}

			colorPicker.addListener("beforeShow", function() {
				colorPicker.addListener("onSelect", select);
				colorPicker.addListener("onClear", clearColor);
			}, { once: true });

			colorPicker.addListener("onHide", function() {
				colorPicker.removeListener("onSelect", select);
				colorPicker.removeListener("onClear", clearColor);
			});

			colorPicker.show({
				anchorTarget: plugin.button,
				vAlign: "bottom"
			});
		}
	};

	plugins.BGColor = {
		name: "BGColor",
		label: "背景色",
		iconClass: "bg-color",
		command: null,
		execute: function() {
			var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

			if (!colorPicker) {
				colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
			}

			function select(self, arg) {
				if (dorado.Browser.msie) {
					HtmlEditorUtils.execCommand(plugin._htmlEditor, 'backColor', false, arg.color);
				} else {
					HtmlEditorUtils.execCommand(plugin._htmlEditor, 'hilitecolor', false, arg.color);
				}
			}

			function clearColor(self) {
				select(self, "#FFF");
			}

			colorPicker.addListener("beforeShow", function() {
				colorPicker.addListener("onSelect", select);
				colorPicker.addListener("onClear", clearColor);
			}, { once: true });

			colorPicker.addListener("onHide", function() {
				colorPicker.removeListener("onSelect", select);
				colorPicker.removeListener("onClear", clearColor);
			});

			editor.colorPicker.show({
				anchorTarget: plugin.button,
				vAlign: "bottom"
			});
		}
	};
})();