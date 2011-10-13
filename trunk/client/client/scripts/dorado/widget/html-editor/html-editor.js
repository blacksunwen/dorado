(function() {
    dorado.widget.htmleditor = {
        //,'Video','Map','GMap','Code'
        fullToolbars: [
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','FormatMatch','|',
             'BlockQuote','|',
             'PastePlain','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'DirectionalityLtr','DirectionalityRtl','|','Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Anchor','Image','Emoticon', '|',
             'Horizontal','Date','Time','Spechars','|',
             'InsertTable','DeleteTable','InsertParagraphBeforeTable','InsertRow','DeleteRow','InsertCol','DeleteCol','MergeCells','MergeRight','MergeDown','SplittoCells','SplittoRows','SplittoCols','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help']
        ],
        simpleToolbars: [
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Horizontal','Image','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help']
        ],
        defaultLabelMap: {
            'anchor':'锚点',
            'undo': '撤销',
            'redo': '重做',
            'bold': '加粗',
            'indent':'首行缩进',
            'outdent':'取消缩进',
            'italic': '斜体',
            'underline': '下划线',
            'strikethrough': '删除线',
            'subscript': '下标',
            'superscript': '上标',
            'formatmatch': '格式刷',
            'source': '源代码',
            'blockquote': '引用',
            'pasteplain': '纯文本粘贴模式',
            'selectall': '全选',
            'print': '打印',
            'preview': '预览',
            'horizontal': '分隔线',
            'removeformat': '清除格式',
            'time': '时间',
            'date': '日期',
            'unlink': '祛除链接',
            'insertrow': '前插入行',
            'insertcol': '前插入列',
            'mergeright': '右合并单元格',
            'mergedown': '下合并单元格',
            'deleterow': '删除行',
            'deletecol': '删除列',
            'splittorows': '拆分成行',
            'splittocols': '拆分成列',
            'splittocells': '完全拆分单元格',
            'mergecells': '合并多个单元格',
            'deletetable': '删除表格',
    //        'tablesuper': '表格高级设置',
            'insertparagraphbeforetable': '表格前插行',
            'cleardoc': '清空文档',
            'fontfamily': '字体',
            'fontsize': '字号',
            'paragraph': '格式',
            'image': '图片',
            'inserttable': '表格',
            'link': '超链接',
            'emoticon': '表情',
            'spechars': '特殊字符',
            'searchreplace': '查询替换',
            'map': 'Baidu地图',
            'gmap': 'Google地图',
            'video': '视频',
            'help': '帮助',
            'justifyleft':'居左对齐',
            'justifyright':'居右对齐',
            'justifycenter':'居中对齐',
            'justifyjustify':'两端对齐',
            'forecolor' : '字体颜色',
            'backcolor' : '背景色',
            'insertorderedlist' : '有序列表',
            'insertunorderedlist' : '无序列表',
            'fullscreen' : '全屏',
            'directionalityltr' : '从左向右输入',
            'directionalityrtl' : '从右向左输入',
            'rowspacing' : '行距',
            'code' : '插入代码'
        },
        defaultListMap: {
            'fontfamily': ['宋体', '楷体', '隶书', '黑体','andale mono','arial','arial black','comic sans ms','impact','times new roman'],
            'fontsize': [10, 11, 12, 14, 16, 18, 20, 24, 36],
            'underline':['none','overline','line-through','underline'],
            'paragraph': ['p:Paragraph', 'h1:Heading 1', 'h2:Heading 2', 'h3:Heading 3', 'h4:Heading 4', 'h5:Heading 5', 'h6:Heading 6'],
            'rowspacing' : ['1.0:0','1.5:15','2.0:20','2.5:25','3.0:30']
        },
        FONT_MAP: {
            '宋体': ['宋体', 'SimSun'],
            '楷体': ['楷体', '楷体_GB2312', 'SimKai'],
            '黑体': ['黑体', 'SimHei'],
            '隶书': ['隶书', 'SimLi'],
            'andale mono' : ['andale mono'],
            'arial' : ['arial','helvetica','sans-serif'],
            'arial black' : ['arial black','avant garde'],
            'comic sans ms' : ['comic sans ms'],
            'impact' : ['impact','chicago'],
            'times new roman' : ['times new roman']
        }
    };

    dorado.widget.htmleditor.ToolBar = $extend(dorado.widget.Control, {
        focusable: true,
        ATTRIBUTES: {
            className: {
                defaultValue: "d-htmleditor-toolbar"
            },
            items: {}
        },
        createItem: function(config) {
            if (!config) return null;
            if (typeof config == "string" || config.constructor == Object.prototype.constructor) {
                var result = dorado.Toolkits.createInstance("toolbar,widget", config);
                result._parent = result._focusParent = this;
                return result;
            } else {
                config._parent = config._focusParent = this;
                return config;
            }
        },
        addItem: function(item, index) {
            var toolbar = this, items = toolbar._items;
            if (!item) return null;
            if (!items) {
                items = toolbar._items = new dorado.util.KeyedArray(function(value) {
                    return value._id;
                });
            }
            item = toolbar.createItem(item);
            if (toolbar._rendered) {
                var refDom = null, dom = toolbar._dom;
                if (typeof index == "number") {
                    var refItem = items[index];
                    refDom = refItem._dom;
                }
                items.insert(item, index);
                item.render(dom);
                toolbar.registerInnerControl(item);
            } else {
                items.insert(item, index);
            }

            return item;
        },
        createDom: function() {
            var bar = this, dom = document.createElement("div"), items = bar._items || [];
            dom.className = bar._className;
            for (var i = 0, j = items.size; i < j; i++) {
                var item = items.get(i);
                bar.registerInnerControl(item);
                item.render(dom);
                if (item instanceof dorado.widget.TextEditor) {
                    $fly(item._dom).addClass("i-text-box");
                }
            }
            return dom;
        }
    });

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Advance
	 * @class 富文本编辑器。
	 * <p>
     *     用来编辑html的富文本编辑器。
	 * </p>
	 * @extends dorado.widget.Control
	 */
    dorado.widget.HtmlEditor = $extend(dorado.widget.AbstractDataEditor, /** @scope dorado.widget.HtmlEditor.prototype */{
        focusable: true,
        ATTRIBUTES: /** @scope dorado.widget.HtmlEditor.prototype */{
            className: {
                defaultValue: "d-html-editor"
            },

            /**
             * <p>富文本编辑器的模式，不同的模式会开启不同的插件，目前可选full,simple，区别在于工具栏上的功能的多少。</p>
             * <p>如果需要自定义，比如叫custom，可以为dorado.widget.htmleditor添加一属性名为customToolbars，内容可参考fullToolbars和simpleToolbars。</p>
             * @attribute
             * @type String
             * @default "full"
             */
            mode: {
                //full,simple
                defaultValue: "full"
            },

            /**
             * 富文本编辑器的内容。
             * @attribute
             * @type String
             */
            content: {
                getter: function() {
                    var editor = this._editor;
                    if (editor) {
                        return editor.getContent();
                    }
                    return "";
                },
                setter: function(value) {
                    var editor = this._editor;
                    if (editor) {
                        return editor.setContent(value || "");
                    }
                }
            }
        },

        /**
         * 显示工具栏。
         */
        showToolBar: function() {
            var editor = this, doms = editor._doms;
            if (doms) {
                var toolbar = doms.toolbar;
                $fly(toolbar).css("display", "");
                editor.doOnResize();
            }
        },

        /**
         * 隐藏工具栏。
         */
        hideToolBar: function() {
            var editor = this, doms = editor._doms;
            if (doms) {
                var toolbar = doms.toolbar;
                $fly(toolbar).css("display", "none");
                editor.doOnResize();
            }
        },
        doOnBlur: function() {
            var editor = this;
            editor._lastPostValue = editor._value;
            editor._value = editor.get("content");
            editor._dirty = true;
            try {
                editor.post();
            } catch (e) {
                editor._value = editor._lastPostValue;
                editor._dirty = false;
                throw e;
            }
            editor.refresh();
            //editor.fireEvent("onValueChange", editor);
        },
        doOnReadOnlyChange: function(readOnly) {
            var editor = this, riche = editor._editor;
            if (readOnly === undefined) {
                readOnly = editor._readOnly || editor._readOnly2;
            }
            if (!riche || !riche.document) return;
            if (readOnly) {
                if (dorado.Browser.msie) {
                    riche.document.body.contentEditable = false;
                } else {
                    riche.document.body.contentEditable = false;
                }
            } else {
                if (dorado.Browser.msie) {
                    riche.document.body.contentEditable = true;
                } else {
                    riche.document.body.contentEditable = true;
                }
            }
            editor.checkStatus();
        },
        post: function() {
            try {
                if(!this._dirty) {
                    return false;
                }
                var eventArg = {
                    processDefault : true
                };
                this.fireEvent("beforePost", this, eventArg);
                if(eventArg.processDefault === false)
                    return false;
                this.doPost();
                this._lastPostValue = this._value;
                this._dirty = false;
                this.fireEvent("onPost", this);
                return true;
            } catch (e) {
                dorado.Exception.processException(e);
            }
        },
        setFocus : function() {},
        doOnAttachToDocument: function() {
            var heditor = this;
            $invokeSuper.call(this, arguments);
            //editor的属性
            var option = {
                initialContent: heditor._value,//初始化编辑器的内容
                minFrameHeight: 100,
                iframeCssUrl: $url(">skin>/html-editor/iframe.css")//给iframe样式的路径
            };
            var editor = new baidu.editor.Editor(option);
            this._editor = editor;
            editor.addListener('selectionchange', function () {
                heditor.checkStatus();
            });
            editor.addListener("ready", function() {
                heditor.checkStatus();
                heditor.doOnResize();
            });
            var popup = new dorado.widget.FloatContainer({
                exClassName: "popup",
                animateType: "fade"
            });
            jQuery.extend(popup, {
                _onEditButtonClick: function () {
                    this.hide();
                    heditor.executePlugin("Link");
                },
                _onImgEditButtonClick: function () {
                    this.hide();
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    //edui remove.
                    if (img && img.className.indexOf("edui-faked-video") != -1) {
                        heditor.executePlugin("Video");
                    } else if (img && img.src.indexOf("http://api.map.baidu.com") != -1) {
                        heditor.executePlugin("Map");
                    } else if (img && img.src.indexOf("http://maps.google.com/maps/api/staticmap") != -1) {
                        heditor.executePlugin("GMap");
                    } else if (img && img.getAttribute("anchorname")) {
                        heditor.executePlugin("Anchor");
                    } else {
                        heditor.executePlugin("Image");
                    }

                },
                _onImgSetFloat: function(event, value) {
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    if (img) {
                        switch (value) {
                            case -2:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "";
                                }
                                break;
                            case -1:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "left";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "left";
                                }
                                break;
                            case 1:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "right";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "right";
                                }
                                break;
                            case 2:
                                if (!!window.ActiveXObject) {
                                    img.style.styleFloat = "";
                                    img.style.display = "block";
                                } else {
                                    img.style.cssFloat = "";
                                    img.style.display = "block";
                                }

                        }
                        //this.showAnchor(img);
                    }
                },
                _onRemoveButtonClick: function () {
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    if (img && img.getAttribute("anchorname")) {
                        editor.execCommand("anchor");
                    } else {
                        editor.execCommand('unlink');
                    }
                    this.hide();
                }
            });
            var popupId = heditor._uniqueId + "_imageLinkPopup";
            window[popupId] = popup;
            editor.addListener('sourcemodechanged', function() {
                popup.hide();
            });
            editor.addListener('selectionchange', function (t, evt) {
                dorado.widget.setFocusedControl(heditor);
                var html = '', img = editor.selection.getRange().getClosedNode(),
                    imglink = baidu.editor.dom.domUtils.findParentByTagName(img, "a", true);

                if (imglink != null) {
                    html += '<nobr>属性: <span class="unclickable">默认</span>&nbsp;&nbsp;<span class="unclickable">左浮动</span>&nbsp;&nbsp;<span class="unclickable">右浮动</span>&nbsp;&nbsp;' +
                            '<span class="unclickable">独占一行</span>' +
                            ' <span onclick="$$._onImgEditButtonClick(event, this);" class="clickable">修改</span></nobr>';
                } else if (img != null && img.tagName.toLowerCase() == 'img') {
                    if (img.getAttribute('anchorname')) {
                        //锚点处理
                        html += '<nobr>属性: <span onclick=$$._onImgEditButtonClick(event) class="clickable">修改</span>&nbsp;&nbsp;<span onclick=$$._onRemoveButtonClick(event) class="clickable">删除</span></nobr>';
                    } else {
                        html += '<nobr>属性: <span onclick=$$._onImgSetFloat(event,-2) class="clickable">默认</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat(event,-1) class="clickable">左浮动</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat(event,1) class="clickable">右浮动</span>&nbsp;&nbsp;' +
                                '<span onclick=$$._onImgSetFloat(event,2) class="clickable">独占一行</span>' +
                                ' <span onclick="$$._onImgEditButtonClick(event, this);" class="clickable">修改</span></nobr>';
                    }
                }
                var link;
                if (editor.selection.getRange().collapsed) {
                    link = editor.queryCommandValue("link");
                } else {
                    link = editor.selection.getStart();
                }
                link = baidu.editor.dom.domUtils.findParentByTagName(link, "a", true);
                var url;
                if (link != null && (url = link.getAttribute('href', 2)) != null) {
                    var txt = url;
                    if (url.length > 30) {
                        txt = url.substring(0, 20) + "...";
                    }
                    if (html) {
                        html += '<div style="height:5px;"></div>'
                    }
                    html += '<nobr>链接: <a target="_blank" href="' + url + '" title="' + url + '" >' + txt + '</a>' +
                            ' <span class="clickable" onclick="$$._onEditButtonClick(event, this);">修改</span>' +
                            ' <span class="clickable" onclick="$$._onRemoveButtonClick(event, this);"> 清除</span></nobr>';
                }
                if (html) {
                    popup.getDom().innerHTML = html.replace(/\$\$/g, popupId);
                    var anchorTarget = img || link;
                    var anchorPosition = $fly(anchorTarget).offset(), position = {};
                    var editorPosition = $fly(editor.iframe).position(), targetHeight = $fly(anchorTarget).height();
                    position.left = anchorPosition.left + editorPosition.left;
                    position.top = anchorPosition.top + editorPosition.top + targetHeight;
                    popup.show({ position: position, autoAdjustPosition: false });
                } else {
                    popup.hide();
                }
            });

            editor.render(this._doms.editorWrap);
        },
        createDom: function() {
            var editor = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                className: editor._className,
                content: [
                    {
                        tagName: "div",
                        className: "toolbar-wrap",
                        contextKey: "toolbar"
                    },
                    {
                        tagName: "div",
                        className: "editor-wrap",
                        contextKey: "editorWrap"
                    }
                ]
            }, null, doms);

            editor._doms = doms;

            editor.initPlugins();

            return dom;
        },
        executePlugin: function(name) {
            var plugin = this._plugins[name];
            if (plugin) {
                plugin.execute();
            }
        },
        initPlugins: function() {
            var editor = this, mode = editor._mode || "default";
            editor._plugins = {};
            var toolbars = dorado.widget.htmleditor[mode + "Toolbars"] || [];
            for (var i = 0, k = toolbars.length; i < k; i++) {
                var toolbarConfig = toolbars[i], toolbar = new dorado.widget.htmleditor.ToolBar();
                for (var j = 0, l = toolbarConfig.length; j < l; j++) {
                    var pluginName = toolbarConfig[j];
                    if (pluginName == "|") {
                        toolbar.addItem("-");
                    } else {
                        var pluginConfig = plugins[pluginName];
                        pluginConfig.htmlEditor = editor;

                        if (pluginConfig.iconClass == undefined && pluginConfig.command) {
                            pluginConfig.iconClass = "html-editor-icon " + pluginConfig.command;
                        }

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
                        plugin._name = pluginName;
                        plugin.initToolBar(toolbar);

                        editor._plugins[pluginName] = plugin;
                    }
                }
                editor.registerInnerControl(toolbar);
                toolbar.render(editor._doms.toolbar);
            }
        },
        doOnResize: function() {
            var htmleditor = this, dom = htmleditor._dom, doms = htmleditor._doms;
            if (dom) {
                var toolBarHeight = doms.toolbar.offsetHeight, height = dom.clientHeight;
                if (htmleditor._editor) {
                    htmleditor._editor.setHeight(height - toolBarHeight > 0 ? height - toolBarHeight : 0);
                }
            }
        },
        checkStatus: function() {
            var editor = this, plugins = editor._plugins;

            for (var name in plugins) {
                var plugin = plugins[name];
                if (plugin.checkStatus) {
                    plugin.checkStatus();
                }
            }
        },
        refreshDom: function() {
            $invokeSuper.call(this, arguments);
            var editor = this;
            if(editor._dataSet) {
                var value, dirty, readOnly = this._dataSet._readOnly;
                if(editor._property) {
                    var bindingInfo = editor._bindingInfo;
                    if(bindingInfo.entity instanceof dorado.Entity) {
                        value = bindingInfo.entity.get(editor._property);
                        dirty = bindingInfo.entity.isDirty(editor._property);
                    }
                    readOnly = readOnly || (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
                } else {
                    readOnly = true;
                }

                var oldReadOnly = editor._oldReadOnly;
                editor._oldReadOnly = !!readOnly;

                editor._value = value;
                if (editor._editor && editor._editor.getContent() != value) {
                    editor._editor.setContent(value || "");
                }
                editor._readOnly2 = readOnly;
                if (oldReadOnly === undefined || oldReadOnly !== readOnly) {
                    editor.doOnReadOnlyChange(!!readOnly);
                }
                editor.setDirty(dirty);
            }
        }
    });

    dorado.widget.htmleditor.HtmlEditorPlugIn = $extend(dorado.AttributeSupport, {
        constructor: function(options) {
            if (options) this.set(options);
        },
        ATTRIBUTES: {
            name: {},
            label: {},
            icon: {},
            iconClass: {},
            command: {},
            parameter: {},
            htmlEditor: {},
            statusToggleable: {},
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
                htmlEditor._editor.execCommand(plugin._command, plugin._parameter);
            }
        },
        execCommand: function(cmd, value) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                htmlEditor._editor.execCommand(cmd, value);
            }
        },
        insertHtml: function(html) {
            this.execCommand("inserthtml", html);
        },
        queryCommandValue: function(cmd) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                return htmlEditor._editor.queryCommandValue(cmd);
            }
        },
        queryCommandState: function(cmd) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                return htmlEditor._editor.queryCommandState(cmd);
            }
        },
        initToolBar: function(toolbar) {
            var plugin = this, labels = dorado.widget.htmleditor.defaultLabelMap;

            plugin.button = toolbar.addItem({
                $type: "SimpleIconButton",
                icon: plugin._icon,
                tip: labels[plugin._name.toLowerCase()],
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
                plugin.execute.apply(this, arguments);
            }
        },
        checkStatus: function() {
            var plugin = this, heditor = plugin._htmlEditor, editor = plugin._htmlEditor._editor, result;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            if (plugin._statusToggleable) {
                try {
                    result = editor.queryCommandState(plugin._command);
                    if (result === 1 || result === true) {
                        plugin.set("status", "on");
                    } else if (result === 0 || result === false) {
                        plugin.set("status", "enable");
                    } else if (result === -1) {
                        plugin.set("status", "disable");
                    }
                } catch(e) {
                }
            } else {
                try {
                    result = editor.queryCommandState(plugin._command);
                    if (result === -1) {
                        plugin.set("status", "disable");
                    } else {
                        plugin.set("status", "enable");
                    }
                } catch(e) {
                }
            }
        }
    });

    var pcheckStatus = function() {
        var plugin = this, heditor = plugin._htmlEditor, editor = plugin._htmlEditor._editor;
        try {
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            var status = editor.queryCommandState(plugin._command);
            if (status == -1) {
                plugin.set("status", "disable");

                return;
            }

            var value = editor.queryCommandValue(plugin._command);
            if (value === plugin._parameter) {
                plugin.set("status", "on");
            } else {
                plugin.set("status", "enable");
            }
        } catch(e) {
        }
    };

    var plugins = dorado.widget.htmleditor.plugins = {
        Source: {
            command: "source"
        },
        DirectionalityLtr: {
            iconClass: "html-editor-icon directionalityltr",
            command: "directionality",
            parameter: "ltr",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        DirectionalityRtl: {
            iconClass: "html-editor-icon directionalityrtl",
            command: "directionality",
            parameter: "rtl",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyCenter: {
            iconClass: "html-editor-icon justifycenter",
            command: "justify",
            parameter: "center",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyLeft: {
            iconClass: "html-editor-icon justifyleft",
            command: "justify",
            parameter: "left",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyRight: {
            iconClass: "html-editor-icon justifyright",
            command: "justify",
            parameter: "right",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyJustify: {
            iconClass: "html-editor-icon justifyjustify",
            command: "justify",
            parameter: "justify",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        FullScreen: {
            iconClass: "html-editor-icon fullscreen",
            statusToggleable: true,
            execute: function() {
                var editor = this._htmlEditor;
                if (!editor._maximized) {
                    editor._originalWidth = editor.getRealWidth();
                    editor._originalHeight = editor._height;
                    $fly(editor._dom).fullWindow({
                        modifySize: false,
                        callback: function(docSize) {
                            editor._maximized = true;
                            editor.set(docSize);
                            editor.resetDimension();
                            editor.refresh();
                        }
                    });
                } else {
                    editor._maximized = false;
                    $fly(editor._dom).unfullWindow({
                        callback: function() {
                            editor._maximized = false;
                            editor._width = editor._originalWidth;
                            editor._height = editor._originalHeight;
                            if (!editor._width) {
                                $fly(editor._dom).css("width", "");
                            }
                            editor.resetDimension();
                            editor.refresh();
                        }
                    });
                }
                this.checkStatus();
            },
            initToolBar: function(toolbar) {
                var plugin = this;
                plugin.button = toolbar.addItem({
                    $type: "SimpleIconButton",
                    exClassName: "fullscreen-button",
                    icon: plugin._icon,
                    iconClass: plugin._iconClass,
                    listener: {
                        onClick: function() {
                            plugin.onClick();
                        }
                    }
                });
            },
            checkStatus: function() {
                var editor = this._htmlEditor;
                if (editor._maximized) {
                    this.set("status", "on");
                } else {
                    this.set("status", "enable");
                }
            }
        }
    };

    var baseCmdMap = {
        Copy: true,
        Paste: true,
        Cut: true,
        Undo: true,
        Redo: true,
        Bold: true,
        Italic: true,
        Underline: true,
        StrikeThrough: true,
        Subscript: true,
        Superscript: true,
        BlockQuote: true,
        Indent: true,
        Outdent: true,
        InsertOrderedList: true,
        InsertUnorderedList: true,
        Unlink: true,
        SelectAll: false,
        RemoveFormat: false,
        Print: false,
        Preview: false,
        Date: false,
        Time: false,
        ClearDoc: false,
        Horizontal: false,
        DeleteTable: false,
        InsertParagraphBeforeTable: false,
        InsertRow: false,
        DeleteRow: false,
        InsertCol: false,
        DeleteCol: false,
        MergeCells: false,
        MergeRight: false,
        MergeDown: false,
        SplittoCells: false,
        SplittoRows: false,
        SplittoCols: false,
        FormatMatch: true,
        PastePlain: true
    };

    for (var prop in baseCmdMap) {
        var checkStatus = baseCmdMap[prop], object = {};
        object.command = prop.toLowerCase();
        if (checkStatus)
            object.statusToggleable = true;
        plugins[prop] = object;
    }

    plugins.Help = {
        iconClass: "html-editor-icon help",
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
})();