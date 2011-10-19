(function(plugins) {
    var htmleditor = dorado.widget.htmleditor;

    plugins.Link = {
        iconClass: "html-editor-icon link",
        command: "link",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "linkForm",
                urlObject = plugin.urlObject;

            if (!urlObject) {
                urlObject = plugin.urlObject = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入超链接",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        id: formId,
                        $type: "AutoForm",
                        cols: "*",
                        entity: urlObject,
                        elements: [
                            { property: "url", label: "超链接", type: "text" },
                            { property: "title", label: "标题", type: "text" },
                            { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                        ]
                    }],
                    buttons: [{
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
                    }, {
                        caption: "取消",
                        listener: {
                            onClick: function() {
                                plugin.dialog.hide();
                            }
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            }
            var link = plugin.queryCommandValue("link") || {}, autoform = editor._view.id(formId);

            urlObject.set("url", link.href);
            urlObject.set("title", link.title || "");
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
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入表格",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
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
                    }],
                    buttons: [{
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

    function suo(img, max) {
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

    window.reloadHtmlEditorImageFn = null;

    window.reloadHtmlEditorImage = function(path) {
        if (window.reloadHtmlEditorImageFn) {
            window.reloadHtmlEditorImageFn($url(path));
            window.reloadHtmlEditorImageFn = null;
        }
    };

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
            var imageObject = plugin.imageObject, imageObjectLocal = plugin.imageObjectLocal;

            if (!imageObject) {
                imageObject = plugin.imageObject = new dorado.Entity();
                imageObjectLocal = plugin.imageObjectLocal = new dorado.Entity();
            }

            if (!plugin.dialog) {
                var imgInfoId = editor._uniqueId + "imageInfo", imgPreviewId = editor._uniqueId + "imagePreview",
                    alignId = editor._uniqueId + "alignEditor", imgInfoLocalId = editor._uniqueId + "imageInfoLocal",
                    imgPreviewLocalId = editor._uniqueId + "imagePreviewLocal", alignLocalId = editor._uniqueId + "alignEditorLocal";

                var imagePath = editor._uniqueId + "imagePath";

                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入图像",
                    width: 480,
                    height: 280,
                    cols: "*",
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "TabControl",
                        tabs: [{
                            $type: "Control",
                            caption: "Remote",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: imageObject,
                                    cols: "*,100",
                                    elements: [{
                                        property: "url", label: "图片链接",
                                        layoutConstraint: { colSpan: 2 },
                                        editor: new dorado.widget.TextEditor({
                                            required: true,
                                            entity: imageObject,
                                            property: "url",
                                            listener: {
                                                onPost: function(self, arg) {
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
                                    }]
                                }],
                                buttons: [{
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
                                }, {
                                    caption: "取消",
                                    listener: {
                                        onClick: function() {
                                            plugin.dialog.hide();
                                        }
                                    }
                                }
                            ]
                            }
                        }, {
                            $type: "Control",
                            caption: "Local",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: imageObjectLocal,
                                    cols: "*,100",
                                    elements: [{
                                        property: "url", label: "图片链接",
                                        layoutConstraint: { colSpan: 1 },
                                        editor: new dorado.widget.TextEditor({
                                            id: imagePath,
                                            required: true,
                                            entity: imageObjectLocal,
                                            property: "url",
                                            readOnly: true
                                        })
                                    },
                                    {
                                        $type: "Container",
                                        exClassName: "browse-button-wrap",
                                        children: [{
                                            $type: "Button",
                                            caption: "浏览..."
                                        }],
                                        onReady: function(self, arg) {
                                            if (self._inited) {
                                                return;
                                            }
                                            self._inited = true;
                                            var dom = self._dom, hiddenFile, pathEditor = this.id(imagePath);
                                            if (dorado.Browser.msie) {
                                                hiddenFile = document.createElement("<input type='file' name='filename' class='hidden-file'/>");
                                            } else {
                                                hiddenFile = document.createElement("input");
                                                hiddenFile.type = "file";
                                                hiddenFile.name = "filename";
                                                hiddenFile.className = "hidden-file";
                                            }

                                            var iframe, iframeName = editor._uniqueId + "uploadIframe";
                                            if (dorado.Browser.msie) {
                                                iframe = document.createElement("<iframe name='" + iframeName + "'></iframe>")
                                            } else {
                                                iframe = document.createElement("iframe");
                                                iframe.name = iframeName;
                                            }
                                            iframe.style.display = "none";

                                            var form, action = $url(">dorado/htmleditor/imageupload");
                                            if (dorado.Browser.msie) {
                                                form = document.createElement("<form name ='imgForm' action='" + action + "' enctype='multipart/form-data' method='post' target='" + iframeName + "'></form>");
                                            } else {
                                                form = document.createElement("form");
                                                form.action = action;
                                                form.method = "post";
                                                form.target = iframeName;
                                                form.enctype = "multipart/form-data";
                                            }

                                            form.appendChild(hiddenFile);
                                            var view = this;
                                            hiddenFile.onchange = function() {
                                                pathEditor.set("text", this.value);
                                                form.submit();
                                                window.reloadHtmlEditorImageFn = function(url) {
                                                    var imgInfo = view.id(imgInfoLocalId).getDom(), imgPreview = view.id(imgPreviewLocalId);
                                                    var preImg = imgPreview.getContentContainer();
                                                    preImg.style.height = "100px";
                                                    if ( !/\.(png|gif|jpg|jpeg|bmp)$/ig.test( url ) && url.indexOf( "api.map.baidu.com" ) == -1 ) {
                                                        preImg.innerHTML = "";
                                                        return false;
                                                    } else {
                                                        preImg.innerHTML = "图片正在加载。。。";
                                                        preImg.innerHTML = "<img src='" + url + "' />";
                                                        var pimg = preImg.firstChild;
                                                        pimg.onload = function() {
                                                            imgInfo.innerHTML = "原始宽：" + this.width + "px&nbsp;&nbsp;原始高：" + this.height + "px";
                                                            imgInfo.parentNode.parentNode.style.display = "";
                                                            suo( this, 100 );
                                                        };
                                                        pimg.onerror = function() {
                                                            preImg.innerHTML = "图片不存在";
                                                        }
                                                    }
                                                    imageObjectLocal.set("url", url);
                                                }
                                            };
                                            dom.firstChild.appendChild(form);
                                            dom.firstChild.appendChild(iframe);
                                        }
                                    },
                                    { property: "width", label: "宽度", type: "text" },
                                    {
                                        id: imgPreviewLocalId,
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
                                            id: alignLocalId,
                                            entity: imageObjectLocal,
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
                                        id: imgInfoLocalId,
                                        $type: "HtmlContainer",
                                        style: {
                                            "text-align": "right"
                                        },
                                        layoutConstraint: { colSpan: 1 },
                                        content: "&nbsp;"
                                    }]
                                }],
                                buttons: [{
                                    caption: "确定",
                                    listener: {
                                        onClick: function() {
                                            var url = imageObjectLocal.get("url");
                                            if (url) {
                                                var width = imageObjectLocal.get("width"), height = imageObjectLocal.get("height"),
                                                    align = this.id(alignLocalId).get("value"), title = imageObjectLocal.get("title");

                                                var imgstr = "<img ";
                                                var myimg = this.id(imgPreviewLocalId).getDom().firstChild;
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
                            }
                        }]
                    }]
                });

                plugin._htmlEditor.registerInnerControl(plugin.dialog);
            }
            plugin.dialog.show();
        }
    };

    plugins.SearchReplace = {
        iconClass: "html-editor-icon searchreplace",
        command: "searchreplace",
        execute: function() {
            var plugin = this, searchEntity = plugin.searchEntity, replaceEntity = plugin.replaceEntity;

            if (!searchEntity) {
                searchEntity = plugin.searchEntity = new dorado.Entity();
                replaceEntity = plugin.replaceEntity = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "查找/替换",
                    width: 380,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
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
                                        var searchStr = replaceEntity.get("text"), replaceStr = replaceEntity.get("replaceText");
                                        if (!searchStr || !replaceStr) {
                                            dorado.MessageBox.alert("Please input search Text");
                                            return;
                                        }
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchStr,
                                            replaceStr: replaceStr,
                                            all: false,
                                            casesensitive: replaceEntity.get("matchCase")
                                        });
                                    }
                                }, {
                                    caption: "全部替换",
                                    onClick: function() {
                                        var searchStr = replaceEntity.get("text"), replaceStr = replaceEntity.get("replaceText");
                                        if (!searchStr || !replaceStr) {
                                            dorado.MessageBox.alert("Please input search Text");
                                            return;
                                        }
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchStr,
                                            replaceStr: replaceStr,
                                            all: true,
                                            casesensitive: replaceEntity.get("matchCase")
                                        });
                                    }
                                }]
                            }
                        }]
                    }]
                });
            }
            plugin.dialog.show();
        }
    };

    plugins.Emoticon = {
        iconClass: "html-editor-icon emoticon",
        command: "inserthtml",
        execute: function() {
            var plugin = this, emoticonPicker = plugin.emoticonPicker;

            if (!emoticonPicker) {
                emoticonPicker = plugin.emoticonPicker = new dorado.widget.EmoticonPicker();
            }

            function select(self, arg) {
                plugin.insertHtml("<img src='" + arg.image + "'/>");
            }

            emoticonPicker.addListener("beforeShow", function() {
                emoticonPicker.addListener("onSelect", select);
            }, { once: true });

            emoticonPicker.addListener("onHide", function() {
                emoticonPicker.removeListener("onSelect", select);
            });

            plugin.emoticonPicker.show({
                anchorTarget: plugin.button,
                vAlign: "bottom"
            });
        }
    };

    plugins.Paragraph = {
        icon: null,
        command: "paragraph",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "格式",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var entity = new dorado.Entity();

            var formatEditor = new dorado.widget.TextEditor({
                width: 70,
                editable: false,
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
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            plugin.formatEditor.set("value", value);
            var status = plugin.queryCommandState("paragraph");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    var fontMap = htmleditor.FONT_MAP, fontMapString = {};
    for (var font in fontMap) {
        fontMapString[font] = fontMap[font].join(",");
    }

    plugins.FontFamily = {
        command: "fontfamily",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "字体",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
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
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
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

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "行距",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var rowspacing = htmleditor.defaultListMap.rowspacing, mappingArray = [];

            for (var i = 0, j = rowspacing.length; i < j; i++) {
                var temp = rowspacing[i].split(":");
                mappingArray.push({ key: temp[1], value: temp[0] });
            }

            var entity = new dorado.Entity();

            var rowSpacingEditor = new dorado.widget.TextEditor({
                width: 50,
                editable: false,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "rowspacing",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("rowspacing", self.get("value"));
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
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
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

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "大小",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var fontsizeArray = htmleditor.defaultListMap.fontsize, mappingArray = [];

            for (var i = 0, j = fontsizeArray.length; i < j; i++) {
                var temp = fontsizeArray[i];
                mappingArray.push({
                    key: "" + temp,
                    value: temp + "px"
                });
            }

            var entity = new dorado.Entity();

            var fontSizeEditor = new dorado.widget.TextEditor({
                width: 50,
                editable: false,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "fontsize",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("fontsize", self.get("value") + "px");
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
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
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
        command: "forecolor",
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
                    modal: true,
                    modalType: "transparent",
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
                    modal: true,
                    modalType: "transparent",
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

    plugins.Anchor = {
        iconClass: "html-editor-icon anchor",
        command: "anchor",
        execute: function() {
            var plugin = this, anchorObject = plugin.anchorObject, editor = plugin._htmlEditor, formId = editor._uniqueId + "anchorForm";

            if (!anchorObject) {
                anchorObject = plugin.anchorObject = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "锚点",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
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
            var plugin = this, editor = plugin._htmlEditor, iframeId = editor._id + "_map_iframe";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "Baidu Map",
                    width: 600,
                    height: 400,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            id: iframeId,
                            $type: "IFrame",
                            path: dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_map.html?" + (new Date()).getTime(),
                            onCreateDom: function(self) {
                                self._doms.iframe.editor = editor._editor;
                            }
                        }
                    ],
                    buttons: [{
                        caption: "OK",
                        onClick: function(self, arg) {
                            var iframe = this.id(iframeId), contentWindow = iframe.getIFrameWindow();
                            contentWindow.dialog_onok();

                            plugin.dialog.hide();
                        }
                    }, {
                        caption: "Cancel",
                        onClick: function(self, arg) {
                            plugin.dialog.hide();
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            } else {
                var iframe = editor.get("view").id(iframeId);
                iframe.set("path", dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_map.html?" + (new Date()).getTime());
            }

            plugin.dialog.show();
        }
    };

    plugins.GMap = {
        iconClass: "html-editor-icon gmap",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, iframeId = editor._id + "_gmap_iframe";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "Google Map",
                    width: 600,
                    height: 400,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            id: iframeId,
                            $type: "IFrame",
                            path: dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_gmap.html?" + (new Date()).getTime(),
                            onCreateDom: function(self) {
                                self._doms.iframe.editor = editor._editor;
                            }
                        }
                    ],
                    buttons: [{
                        caption: "OK",
                        onClick: function(self, arg) {
                            var iframe = this.id(iframeId), contentWindow = iframe.getIFrameWindow();
                            contentWindow.dialog_onok();

                            plugin.dialog.hide();
                        }
                    }, {
                        caption: "Cancel",
                        onClick: function(self, arg) {
                            plugin.dialog.hide();
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            } else {
                var iframe = editor.get("view").id(iframeId);
                iframe.set("path", dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_gmap.html?" + (new Date()).getTime());
            }

            plugin.dialog.show();
        }
    };
})(dorado.widget.htmleditor.plugins);