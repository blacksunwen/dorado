(function() {
    delete baidu.editor.plugins['contextmenu'];

    dorado.widget.htmleditor = {};

    var configs = {
        defaultToolbars: [
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','FormatMatch','|',
             'BlockQuote','|',
             'PastePlain','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'DirectionalityLtr','DirectionalityRtl','|','','Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Anchor','Image','MultiMenu','Video','Map','GMap','Code', '|',
             'Horizontal','Date','Time','Spechars','|',
             'InsertTable','DeleteTable','InsertParagraphBeforeTable','InsertRow','DeleteRow','InsertCol','DeleteCol','MergeCells','MergeRight','MergeDown','SplittoCells','SplittoRows','SplittoCols','|',
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

    var ToolBarItems = [
        ["Source", "|", "SearchReplace", "ClearDoc", "Copy", "Paste", "Cut", "|", "Undo", "Redo", "|","SelectAll","RemoveFormat","Print","|","Preview","Help", "FullScreen"],
        ["Bold","Italic","UnderLine",
            "StrikeThrough", "Subscript", "Superscript", "BlockQuote", "FormatMatch", "|", "PastePlain", "|",
            "Indent","Outdent","|","InsertOrderedList","InsertUnOrderedList","|","JustifyCenter","JustifyLeft","JustifyRight",
            "JustifyFull", "|", "CreateLink", "UnLink", "Anchor", "|",
            "DirectionalityLtr", "DirectionalityRtl", "|","Date", "Time",
            "Horizontal","Image","MultiMenu", "Spechars"],
        ["Format","FontName","FontSize","RowSpacing","|","ForeColor","BackColor"],
        ["InsertTable", 'DeleteTable','InsertParagraphBeforeTable','InsertRow','DeleteRow','InsertCol','DeleteCol',
            'MergeCells','MergeRight','MergeDown','SplittoCells','SplittoRows','SplittoCols']
    ];

    dorado.widget.HtmlEditor = $extend(dorado.widget.Control, {
        ATTRIBUTES: {
            className: {
                defaultValue: "d-html-editor"
            },
            status: {
                defaultValue: "visual"
            }
        },
        doOnAttachToDocument: function() {
            var heditor = this;
            $invokeSuper.call(this, arguments);
            //editor的属性
            var option = {
                initialContent: 'hello world',//初始化编辑器的内容
                minFrameHeight: 200,
                iframeCssUrl: $url(">skin>/advance/iframe.css")            //给iframe样式的路径
                //initialStyle: ''             //编辑器初始化样式
                //enterTag : 'p'              //输入回车时使用p标签
            };
            var editor = new baidu.editor.Editor(option);
            this._editor = editor;
            editor.addListener('selectionchange', function () {
                heditor.checkStatus();
            });
            editor.addListener("ready", function() {
                heditor.checkStatus();
            });
            var popup = new dorado.widget.FloatContainer({
                exClassName: "popup",
                animateType: "fade"
            });
            jQuery.extend(popup, {
                _onEditButtonClick: function () {
                    this.hide();
                    heditor.executePlugin("CreateLink");
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
                        this.showAnchor(img);
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
                },
                queryAutoHide: function (el) {
                    if (el && el.ownerDocument == editor.document) {
                        if (el.tagName.toLowerCase() == 'img' || baidu.editor.dom.domUtils.findParentByTagName(el, 'a', true)) {
                            return el !== popup.anchorEl;
                        }
                    }
                    return baidu.editor.ui.Popup.prototype.queryAutoHide.call(this, el);
                }
            });
            var popupId = heditor._uniqueId + "_imageLinkPopup";
            window[popupId] = popup;
            editor.addListener('selectionchange', function (t, evt) {
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
                    var anchorTarget = img || link, anchorPosition = $fly(anchorTarget).offset(), position = {};
                    var editorPosition = $fly(editor.iframe).position(), targetHeight = $fly(anchorTarget).height();
                    position.left = anchorPosition.left + editorPosition.left;
                    position.top = anchorPosition.top + editorPosition.top + targetHeight;

                    popup.show({ position: position });
                } else {
                    popup.hide();
                }
            });

            editor.render(this._doms.editorWrap);
            this.doOnResize();
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

                        plugin.initToolBar(toolbar);

                        editor._plugins[pluginName] = plugin;
                    }
                }
            }
        },
        doOnResize: function() {
            var editor = this, dom = editor._dom, doms = editor._doms;
            if (dom) {
                var toolBarHeight = doms.toolbar.offsetHeight, height = dom.clientHeight;
                if (editor._editor)
                    editor._editor.setHeight(height - toolBarHeight);
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
            var editor = this, plugins = editor._plugins;
            for (var name in plugins) {
                var plugin = plugins[name];
                if (plugin.checkStatus) {
                    plugin.checkStatus();
                }
            }
        },
        onChange: function() {}
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
            var plugin = this;
            plugin.button = toolbar.addItem({
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
                plugin.execute.apply(this, arguments);
                plugin._htmlEditor.checkStatus();
            }
        },
        checkStatus: function() {
            var plugin = this, editor = plugin._htmlEditor._editor, result;
            if (plugin._statusToggleable) {
                try {
                    result = editor.queryCommandState(plugin._command);
                    //console.log("command:" + plugin._command + "\tresult:" + result);
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
                    //console.log("command:" + plugin._command + "\tresult:" + result);
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
        var plugin = this, editor = plugin._htmlEditor._editor;
        try {
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

    var plugins = {
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
        JustifyFull: {
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
                var htmlEditor = this._htmlEditor;
                $fly(htmlEditor._dom).fullWindow();
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
        UnderLine: true,
        StrikeThrough: true,
        Subscript: true,
        Superscript: true,
        BlockQuote: true,
        Indent: true,
        Outdent: true,
        InsertOrderedList: true,
        InsertUnOrderedList: true,
        UnLink: true,
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
                    buttons: [
                        {
                            caption: "确定",
                            listener: {
                                onClick: function() {
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    ],
                    children: [
                        {
                            $type: "HtmlContainer",
                            content: "<div style='text-align:center;'>Dorado Html Editor</div>"
                        }
                    ]
                });
            }
            plugin.dialog.show();
        }
    };

    var urlObject = new dorado.Entity();

    plugins.CreateLink = {
        iconClass: "html-editor-icon link",
        command: "CreateLink",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "linkForm";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入超链接",
                    width: 480,
                    center: true,
                    children: [
                        {
                            id: formId,
                            $type: "AutoForm",
                            cols: "*",
                            entity: urlObject,
                            elements: [
                                { property: "url", label: "超链接", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            caption: "确定",
                            listener: {
                                onClick: function() {
                                    if (urlObject) {
                                        var url = urlObject.get("url");
                                        plugin.execCommand("link", {
                                            href: url,
                                            title: urlObject.get("title"),
                                            target: urlObject.get("target") === true ? "_blank" : "_self"
                                        });
                                        plugin.dialog.hide();
                                    }
                                }
                            }
                        },
                        {
                            caption: "取消",
                            listener: {
                                onClick: function() {
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    ]
                });
                editor.registerInnerControl(plugin.dialog);
            }
            var link = plugin.queryCommandValue("link") || {}, autoform = editor._view.id(formId);

            urlObject.set("url", link.href);
            urlObject.set("title", link.title);
            urlObject.set("target", link.target == "_blank");
            autoform.refreshData();

            plugin.dialog.show();
        }
    };

    plugins.InsertTable = {
        iconClass: "html-editor-icon table",
        command: "inserttable",
        execute: function() {
            var plugin = this, tableConfig = new dorado.Entity();

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog(
                    {
                        caption: "插入表格",
                        width: 480,
                        center: true,
                        children: [
                            {
                                $type: "AutoForm",
                                entity: tableConfig,
                                elements: [
                                    { property: "row", label: "行数", type: "text"},
                                    { property: "column", label: "列数", type: "text"},
                                    { property: "width", label: "宽度", type: "text"},
                                    { property: "height", label: "高度", type: "text"},
                                    { property: "border", label: "边框", type: "text"},
                                    { property: "cellborder", label: "单元格边框", type: "text" },
                                    { property: "cellpadding", label: "单元格边距", type: "text"},
                                    { property: "cellspacing", label: "单元格间距", type: "text"},
                                    { property: "alignment", label: "对齐方式", type: "text",
                                        editor: {
                                            $type: "TextEditor",
                                            trigger: "autoMappingDropDown1",
                                            mapping: [
                                                { key: "default", value: "无" },
                                                { key: "left", value: "左对齐" },
                                                { key: "center", value: "居中" },
                                                { key: "right", value: "右对齐" }
                                            ]
                                        }
                                    }
                                ]
                            }
                        ],
                        buttons: [
                            {
                                caption: "确定",
                                listener: {
                                    onClick: function() {
                                        if (tableConfig) {
                                            var border = tableConfig.border;
                                            var cellpadding = tableConfig.cellpadding;
                                            var cellspacing = tableConfig.cellspacing;
                                            var width = tableConfig.width;
                                            var row = tableConfig.row || 2, column = tableConfig.column || 2;

                                            var alignment = tableConfig.alignment, cellborder = tableConfig.cellborder;

                                            plugin.execCommand("inserttable", {
                                                numRows: row,
                                                numCols: column,
                                                border: border,
                                                cellborder: cellborder,
                                                cellpadding: cellpadding,
                                                cellspacing: cellspacing,
                                                width: width,
                                                align: alignment
                                            });

                                            plugin.dialog.hide();
                                        }
                                    }
                                }
                            },
                            {
                                caption: "取消",
                                listener: {
                                    onClick: function() {
                                        plugin.dialog.hide();
                                    }
                                }
                            }
                        ]
                    });
            }
            plugin.dialog.show();
        }
    };

    var imageObject = new dorado.Entity();

    function suo( img, max ) {
        var width = 0,height = 0,percent;
        img.sWidth = img.width;
        img.sHeight = img.height;
        if ( img.width > max || img.height > max ) {
            if ( img.width >= img.height ) {
                if ( width = img.width - max ) {
                    percent = (width / img.width).toFixed( 2 );
                    img.height = img.height - img.height * percent;
                    img.width = max;
                }
            } else {
                if ( height = img.height - max ) {
                    percent = (height / img.height).toFixed( 2 );
                    img.width = img.width - img.width * percent;
                    img.height = max;
                }
            }
        }
    }

    var alignImageMap = {
        left: "float: left;",
        right: "float: right;",
        block: "display: block;",
        "default": ""
    };

    plugins.Image = {
        iconClass: "html-editor-icon image",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;
            if (!plugin.dialog) {
                var imgInfoId = editor._uniqueId + "imageInfo", imgPreviewId = editor._uniqueId + "imagePreview", alignId = editor._uniqueId + "alignEditor";

                plugin.dialog = new dorado.widget.Dialog(
                {
                    caption: "插入图像",
                    width: 480,
                    cols: "*",
                    center: true,
                    children: [
                        {
                            $type: "AutoForm",
                            entity: imageObject,
                            cols: "*,100",
                            elements: [
                                { property: "url", label: "图片链接",
                                    layoutConstraint: { colSpan: 2 },
                                    editor: new dorado.widget.TextEditor({
                                        required: true,
                                        entity: imageObject,
                                        property: "url",
                                        listener: {
                                            onPost: function(self, arg) {
                                                debugger;
                                                var imgInfo = this.id(imgInfoId).getDom(), imgPreview = this.id(imgPreviewId);
                                                var url = self.get("text"), preImg = imgPreview.getContentContainer();
                                                preImg.style.height = "100px";
                                                if ( !/\.(png|gif|jpg|jpeg|bmp)$/ig.test( url ) && url.indexOf( "api.map.baidu.com" ) == -1 ) {
                                                    preImg.innerHTML = "";
                                                    return false;
                                                } else {
                                                    preImg.innerHTML = "图片正在加载。。。";
                                                    preImg.innerHTML = "<img src='" + url + "' />";
                                                    var pimg = preImg.firstChild;
                                                    //G( "urll" ).value = pimg.src;
                                                    pimg.onload = function() {
                                                        imgInfo.innerHTML = "原始宽：" + this.width + "px&nbsp;&nbsp;原始高：" + this.height + "px";
                                                        imgInfo.parentNode.parentNode.style.display = "";
                                                        suo( this, 100 );
                                                    };
                                                    pimg.onerror = function() {
                                                        preImg.innerHTML = "图片不存在";
                                                    }
                                                }
                                            }
                                        }
                                    })
                                },
                                { property: "width", label: "宽度", type: "text" },
                                {
                                    id: imgPreviewId,
                                    $type: "Container",
                                    layoutConstraint: { rowSpan: 4, vAlign: "top" },
                                    style: {
                                        border: "1px solid #ddd"
                                    },
                                    width: "100%",
                                    height: "100%"
                                },
                                { property: "height", label: "高度", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                {
                                    property: "align", label: "对齐方式",
                                    editor: new dorado.widget.TextEditor({
                                        id: alignId,
                                        entity: imageObject,
                                        property: "align",
                                        trigger: "autoMappingDropDown1",
                                        mapping: [
                                            { key: "default", value: "默认" },
                                            { key: "left", value: "左浮动" },
                                            { key: "right", value: "右浮动" },
                                            { key: "block", value: "独占一行" }
                                        ]
                                    })
                                },
                                {
                                    id: imgInfoId,
                                    $type: "HtmlContainer",
                                    style: {
                                        "text-align": "right"
                                    },
                                    layoutConstraint: { colSpan: 1 },
                                    content: "&nbsp;"
                                }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            caption: "确定",
                            listener: {
                                onClick: function() {
                                    var url = imageObject.get("url");
                                    if (url) {
                                        var width = imageObject.get("width"), height = imageObject.get("height"),
                                            align = this.id(alignId).get("value"), title = imageObject.get("title");

                                        var imgstr = "<img ";
                                        var myimg = this.id(imgPreviewId).getDom().firstChild;
                                        imgstr += " src=" + url;

                                        if ( !width ) {
                                            imgstr += " width=" + myimg.sWidth;
                                        }else if ( width && !/^[1-9]+[.]?\d*$/g.test( width ) ) {
                                            alert( "请输入正确的宽度" );
                                            return false;
                                        } else {
                                            myimg && myimg.setAttribute( "width", width );
                                            imgstr += " width=" + width;
                                        }
                                        if (!height) {
                                            imgstr += " height=" + myimg.sHeight;
                                        } else if ( height && !/^[1-9]+[.]?\d*$/g.test( height ) ) {
                                            alert( "请输入正确的高度" );
                                            return false;
                                        } else {
                                            myimg && myimg.setAttribute( "height", height );
                                            imgstr += " height=" + height;
                                        }

                                        if (title) {
                                            myimg && myimg.setAttribute( "title", title );
                                            imgstr += " title=" + title;
                                        }

                                        if (align) {
                                            var value = alignImageMap[align];
                                            if (value) {
                                                imgstr += " style='" + value + "'";
                                            }
                                        }

                                        plugin.insertHtml(imgstr + " />");
                                        plugin.dialog.hide();
                                    }
                                }
                            }
                        },
                        {
                            caption: "取消",
                            listener: {
                                onClick: function() {
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    ]
                });

                plugin._htmlEditor.registerInnerControl(plugin.dialog);
            }
            plugin.dialog.show();
        }
    };

    var searchEntity = new dorado.Entity(), replaceEntity = new dorado.Entity();

    plugins.SearchReplace = {
        iconClass: "html-editor-icon searchreplace",
        command: "searchreplace",
        execute: function() {
            var plugin = this;
            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog(
                {
                    caption: "查找/替换",
                    width: 380,
                    center: true,
                    children: [
                        {
                            $type: "TabControl",
                            height: 150,
                            tabMinWidth: 80,
                            tabs: [{
                                $type: "Control",
                                caption: "查找",
                                control: {
                                    $type: "Panel",
                                    children: [{
                                        $type: "AutoForm",
                                        entity: searchEntity,
                                        cols: "*",
                                        elements: [
                                            { property: "text", label: "查找", type: "text"},
                                            { property: "matchCase", label: "区分大小写", type: "checkBox" }
                                        ]
                                    }],
                                    buttons: [{
                                        caption: "上一个",
                                        onClick: function() {
                                            plugin.execCommand("searchreplace", {
                                                searchStr: searchEntity.get("text"),
                                                casesensitive: searchEntity.get("matchCase"),
                                                dir: -1
                                            });
                                        }
                                    }, {
                                        caption: "下一个",
                                        onClick: function() {
                                            plugin.execCommand("searchreplace", {
                                                searchStr: searchEntity.get("text"),
                                                casesensitive: searchEntity.get("matchCase"),
                                                dir: 1
                                            });
                                        }
                                    }]
                                }
                            }, {
                                $type: "Control",
                                caption: "替换",
                                control: {
                                    $type: "Panel",
                                    children: [{
                                        $type: "AutoForm",
                                        entity: replaceEntity,
                                        cols: "*",
                                        elements: [
                                            { property: "text", label: "查找", type: "text"},
                                            { property: "replaceText", label: "替换", type: "text"},
                                            { property: "matchCase", label: "区分大小写", type: "checkBox"}
                                        ]
                                    }],
                                    buttons: [{
                                        caption: "上一个",
                                        onClick: function() {
                                            plugin.execCommand("searchreplace", {
                                                searchStr: replaceEntity.get("text"),
                                                casesensitive: replaceEntity.get("matchCase"),
                                                dir: -1
                                            });
                                        }
                                    }, {
                                        caption: "下一个",
                                        onClick: function() {
                                            plugin.execCommand("searchreplace", {
                                                searchStr: replaceEntity.get("text"),
                                                casesensitive: replaceEntity.get("matchCase"),
                                                dir: 1
                                            });
                                        }
                                    }, {
                                        caption: "替换",
                                        onClick: function() {
                                            var searchStr = replaceEntity.get("text");
                                            if (searchStr == null || searchStr == "") {
                                                dorado.MessageBox.alert("Please input search Text");
                                                return;
                                            }
                                            plugin.execCommand("searchreplace", {
                                                searchStr: replaceEntity.get("text"),
                                                replaceStr: replaceEntity.get("replaceText") || null,
                                                all: false,
                                                casesensitive: replaceEntity.get("matchCase")
                                            });
                                        }
                                    }, {
                                        caption: "全部替换",
                                        onClick: function() {
                                            var searchStr = replaceEntity.get("text");
                                            if (searchStr == null || searchStr == "") {
                                                dorado.MessageBox.alert("Please input search Text");
                                                return;
                                            }
                                            plugin.execCommand("searchreplace", {
                                                searchStr: replaceEntity.get("text"),
                                                replaceStr: replaceEntity.get("replaceText") || null,
                                                all: true,
                                                casesensitive: replaceEntity.get("matchCase")
                                            });
                                        }
                                    }]
                                }
                            }]
                        }
                    ]
                });
            }
            plugin.dialog.show();
        }
    };

    plugins.MultiMenu = {
        iconClass: "html-editor-icon emoticon",
        command: "inserthtml",
        execute: function() {
            var plugin = this, facePicker = plugin.facePicker;

            if (!facePicker) {
                facePicker = plugin.facePicker = new dorado.widget.FacePicker();
            }

            function select(self, arg) {
                plugin.insertHtml("<img src='" + arg.image + "'/>");
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
        icon: null,
        command: "paragraph",
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

            var entity = new dorado.Entity();

            var formatEditor = new dorado.widget.TextEditor({
                width: 100,
                trigger: "autoMappingDropDown1",
                mapping: [
                    { key: "p", value: "段落" },
                    { key: "h1", value: "标题 1" },
                    { key: "h2", value: "标题 2" },
                    { key: "h3", value: "标题 3" },
                    { key: "h4", value: "标题 4" },
                    { key: "h5", value: "标题 5" },
                    { key: "h6", value: "标题 6" }
                ],
                entity: entity,
                property: "format",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        var value = self.get("value");
                        plugin.execCommand('paragraph', self.get("value"));
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
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("paragraph");
            plugin.formatEditor.set("value", value);
            var status = plugin.queryCommandState("paragraph");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    var fontMap = configs.FONT_MAP, fontMapString = {};
    for (var font in fontMap) {
        fontMapString[font] = fontMap[font].join(",");
    }

    plugins.FontName = {
        command: "fontfamily",
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
                items: ["宋体", "黑体", "隶书", "楷体", "Arial", "Impact", "Georgia", "Verdana", "Courier New", "Times New Roman"],
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
                        var text = self.get("text"), result = text;
                        if (fontMapString[text]) {
                            result = fontMapString[text];
                        }
                        plugin.execCommand("fontfamily", result);
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
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("fontfamily");
            plugin.fontEditor.set("text", value);
            var status = plugin.queryCommandState("fontfamily");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.RowSpacing = {
        command: "rowspacing",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem(
                {
                    $type: "Label",
                    text: "行距",
                    style: {
                        "margin-left": 3,
                        "margin-right": 5
                    }
                });

            var rowspacing = configs.defaultListMap.rowspacing, mappingArray = [];

            for (var i = 0, j = rowspacing.length; i < j; i++) {
                var temp = rowspacing[i].split(":");
                mappingArray.push({ key: temp[1], value: temp[0] });
            }

            var entity = new dorado.Entity();

            var rowSpacingEditor = new dorado.widget.TextEditor({
                width: 100,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "rowspacing",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("rowspacing", self.get("value"));
                        plugin.checkStatus();
                    }
                }
            });
            plugin.rowSpacingEditor = rowSpacingEditor;

            toolbar.addItem(rowSpacingEditor);
        },
        onStatusChange: function(status) {
            this.rowSpacingEditor.set("readOnly", status == "disable");
        },
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("rowspacing");
            plugin.rowSpacingEditor.set("value", value);
            var status = plugin.queryCommandState("rowspacing");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.FontSize = {
        command: "fontsize",
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

            var fontsizeArray = configs.defaultListMap.fontsize, mappingArray = [];

            for (var i = 0, j = fontsizeArray.length; i < j; i++) {
                var temp = fontsizeArray[i];
                mappingArray.push({
                    key: "" + temp,
                    value: temp + "pt"
                });
            }

            var entity = new dorado.Entity();

            var fontSizeEditor = new dorado.widget.TextEditor({
                width: 100,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "fontsize",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("fontsize", self.get("value") + "pt");
                        //console.log("size:" + self.get("text"));
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
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("fontsize");
            plugin.fontSizeEditor.set("text", value);

            var status = plugin.queryCommandState("fontsize");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.ForeColor = {
        iconClass: "html-editor-icon forecolor",
        command: "ForeColor",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

            if (!colorPicker) {
                colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
            }

            function select(self, arg) {
                plugin.execCommand('forecolor', arg.color);
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

    plugins.BackColor = {
        iconClass: "html-editor-icon backcolor",
        command: "backcolor",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

            if (!colorPicker) {
                colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
            }

            function select(self, arg) {
                plugin.execCommand('backcolor', arg.color);
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

    function A(sr){ return sr.split(","); }
    var character_common = [
        ["特殊符号", A("、,。,·,ˉ,ˇ,¨,〃,々,—,～,‖,…,‘,’,“,”,〔,〕,〈,〉,《,》,「,」,『,』,〖,〗,【,】,±,×,÷,∶,∧,∨,∑,∏,∪,∩,∈,∷,√,⊥,∥,∠,⌒,⊙,∫,∮,≡,≌,≈,∽,∝,≠,≮,≯,≤,≥,∞,∵,∴,♂,♀,°,′,″,℃,＄,¤,￠,￡,‰,§,№,☆,★,○,●,◎,◇,◆,□,■,△,▲,※,→,←,↑,↓,〓,〡,〢,〣,〤,〥,〦,〧,〨,〩,㊣,㎎,㎏,㎜,㎝,㎞,㎡,㏄,㏎,㏑,㏒,㏕,︰,￢,￤,,℡,ˊ,ˋ,˙,–,―,‥,‵,℅,℉,↖,↗,↘,↙,∕,∟,∣,≒,≦,≧,⊿,═,║,╒,╓,╔,╕,╖,╗,╘,╙,╚,╛,╜,╝,╞,╟,╠,╡,╢,╣,╤,╥,╦,╧,╨,╩,╪,╫,╬,╭,╮,╯,╰,╱,╲,╳,▁,▂,▃,▄,▅,▆,▇,�,█,▉,▊,▋,▌,▍,▎,▏,▓,▔,▕,▼,▽,◢,◣,◤,◥,☉,⊕,〒,〝,〞")],
        ["罗马数字", A("ⅰ,ⅱ,ⅲ,ⅳ,ⅴ,ⅵ,ⅶ,ⅷ,ⅸ,ⅹ,Ⅰ,Ⅱ,Ⅲ,Ⅳ,Ⅴ,Ⅵ,Ⅶ,Ⅷ,Ⅸ,Ⅹ,Ⅺ,Ⅻ")],
        ["数字符号", A("⒈,⒉,⒊,⒋,⒌,⒍,⒎,⒏,⒐,⒑,⒒,⒓,⒔,⒕,⒖,⒗,⒘,⒙,⒚,⒛,⑴,⑵,⑶,⑷,⑸,⑹,⑺,⑻,⑼,⑽,⑾,⑿,⒀,⒁,⒂,⒃,⒄,⒅,⒆,⒇,①,②,③,④,⑤,⑥,⑦,⑧,⑨,⑩,㈠,㈡,㈢,㈣,㈤,㈥,㈦,㈧,㈨,㈩")],
        ["日文符号", A("ぁ,あ,ぃ,い,ぅ,う,ぇ,え,ぉ,お,か,が,き,ぎ,く,ぐ,け,げ,こ,ご,さ,ざ,し,じ,す,ず,せ,ぜ,そ,ぞ,た,だ,ち,ぢ,っ,つ,づ,て,で,と,ど,な,に,ぬ,ね,の,は,ば,ぱ,ひ,び,ぴ,ふ,ぶ,ぷ,へ,べ,ぺ,ほ,ぼ,ぽ,ま,み,む,め,も,ゃ,や,ゅ,ゆ,ょ,よ,ら,り,る,れ,ろ,ゎ,わ,ゐ,ゑ,を,ん,ァ,ア,ィ,イ,ゥ,ウ,ェ,エ,ォ,オ,カ,ガ,キ,ギ,ク,グ,ケ,ゲ,コ,ゴ,サ,ザ,シ,ジ,ス,ズ,セ,ゼ,ソ,ゾ,タ,ダ,チ,ヂ,ッ,ツ,ヅ,テ,デ,ト,ド,ナ,ニ,ヌ,ネ,ノ,ハ,バ,パ,ヒ,ビ,ピ,フ,ブ,プ,ヘ,ベ,ペ,ホ,ボ,ポ,マ,ミ,ム,メ,モ,ャ,ヤ,ュ,ユ,ョ,ヨ,ラ,リ,ル,レ,ロ,ヮ,ワ,ヰ,ヱ,ヲ,ン,ヴ,ヵ,ヶ")],
        ["希腊字母", A("Α,Β,Γ,Δ,Ε,Ζ,Η,Θ,Ι,Κ,Λ,Μ,Ν,Ξ,Ο,Π,Ρ,Σ,Τ,Υ,Φ,Χ,Ψ,Ω,α,β,γ,δ,ε,ζ,η,θ,ι,κ,λ,μ,ν,ξ,ο,π,ρ,σ,τ,υ,φ,χ,ψ,ω")],
        ["俄文字母", A("А,Б,В,Г,Д,Е,Ё,Ж,З,И,Й,К,Л,М,Н,О,П,Р,С,Т,У,Ф,Х,Ц,Ч,Ш,Щ,Ъ,Ы,Ь,Э,Ю,Я,а,б,в,г,д,е,ё,ж,з,и,й,к,л,м,н,о,п,р,с,т,у,ф,х,ц,ч,ш,щ,ъ,ы,ь,э,ю,я")],
        ["拼音字母", A("ā,á,ǎ,à,ē,é,ě,è,ī,í,ǐ,ì,ō,ó,ǒ,ò,ū,ú,ǔ,ù,ǖ,ǘ,ǚ,ǜ,ü")],
        ["注音字符及其他", A("ㄅ,ㄆ,ㄇ,ㄈ,ㄉ,ㄊ,ㄋ,ㄌ,ㄍ,ㄎ,ㄏ,ㄐ,ㄑ,ㄒ,ㄓ,ㄔ,ㄕ,ㄖ,ㄗ,ㄘ,ㄙ,ㄚ,ㄛ,ㄜ,ㄝ,ㄞ,ㄟ,ㄠ,ㄡ,ㄢ,ㄣ,ㄤ,ㄥ,ㄦ,ㄧ,ㄨ")]
    ];

    plugins.Spechars = {
        iconClass: "html-editor-icon spechars",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                var tabs = [];

                for (var i = 0, k = character_common.length; i < k; i++) {
                    var tab = character_common[i], tabName = tab[0], chars = tab[1];
                    tabs.push({
                        $type: "Control",
                        caption: tabName,
                        control: {
                            $type: "GridPicker",
                            floating: false,
                            column: 15,
                            elements: chars,
                            listener: {
                                onSelect: function(self, arg) {
                                    plugin.execCommand("inserthtml", arg.element);
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    });
                }

                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 640,
                    height: 480,
                    center: true,
                    children: [
                        {
                            $type: "TabControl",
                            tabs: tabs
                        }
                    ]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            plugin.dialog.show();
        }
    };

    plugins.Video = {
        iconClass: "html-editor-icon spechars",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 480,
                    center: true,
                    children: [
                        {
                            $type: "AutoForm",
                            cols: "*",
                            entity: urlObject,
                            elements: [
                                { property: "url", label: "超链接", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                            ]
                        }
                    ],
                    buttons: []
                });
                editor.registerInnerControl(plugin.dialog);
            }

            plugin.dialog.show();
        }
    };

    var anchorObject = new dorado.Entity();

    plugins.Anchor = {
        iconClass: "html-editor-icon anchor",
        command: "anchor",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "anchorForm";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "锚点",
                    width: 480,
                    center: true,
                    children: [
                        {
                            id: formId,
                            $type: "AutoForm",
                            cols: "*",
                            entity: anchorObject,
                            elements: [
                                { property: "name", label: "锚点名字", type: "text" }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            caption: "确定",
                            listener: {
                                onClick: function() {
                                    if (anchorObject) {
                                        var name = anchorObject.get("name");
                                        if (name) {
                                            plugin.execCommand("anchor", name);
                                            plugin.dialog.hide();
                                        }
                                    }
                                }
                            }
                        },
                        {
                            caption: "取消",
                            listener: {
                                onClick: function() {
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    ]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            var anchor, img = editor._editor.selection.getRange().getClosedNode(), autoform = editor._view.id(formId);;
            if(img && /img/ig.test(img.tagName.toLowerCase()) && img.getAttribute('anchorname')){
                anchor = img.getAttribute('anchorname');
            }

            anchorObject.set("name", anchor || "");
            autoform.refreshData();

            plugin.dialog.show();
        }
    };

    var scriptOnload = document.createElement('script').readyState ? function(node, callback) {
	   var oldCallback = node.onreadystatechange;
	   node.onreadystatechange = function() {
		   var rs = node.readyState;
		   if (rs === 'loaded' || rs === 'complete') {
			   node.onreadystatechange = null;
			   oldCallback && oldCallback();
			   callback.call(this);
		   }
	   };
	} : function(node, callback) {
	   node.addEventListener('load', callback, false);
	};

    plugins.Map = {
        iconClass: "html-editor-icon map",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 480,
                    center: true,
                    children: [
                        {
                            $type: "AutoForm",
                            cols: "*",
                            entity: urlObject,
                            elements: [
                                { property: "url", label: "超链接", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                            ]
                        }
                    ],
                    buttons: []
                });
                editor.registerInnerControl(plugin.dialog);
                var script = document.createElement("script");
                script.src = "http://api.map.baidu.com/api?v=1.1&services=true";
                script.type = "text/javascript";
                scriptOnload(script, function() {
                    //var map = BMap.Map;
                });
                document.getElementsByTagName("head")[0].appendChild(script);
            }

            plugin.dialog.show();
        }
    };
})();