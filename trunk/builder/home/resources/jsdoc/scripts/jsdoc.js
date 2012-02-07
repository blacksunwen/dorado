var symbolDictionary = SYMBOL_DICTIONARY;
var SYMBOLS = {};

function getMainFrame() {
	var p = window;
	try {
		while (p) {
			if (p.showSymbolDocInTab) return p;
			if (p == p.parent) break;
			p = p.parent;
		}
	} 
	catch (e) {
		// do nothing
	}
	return null;
}

function openSymbolDoc(alias) {
	var frame = getMainFrame();
	if (frame) frame.showSymbolDocInTab(alias);
	else {
		if (dorado.Browser.chrome && window.location.protocol == "file:") {
			dorado.MessageBox.show({
				message: "由于Chrome浏览器在使用file协议时禁用了某些关键操作，这导致此处的超链功能无法正常使用。\n请尝试访问此文档的在线版本或换其他浏览器试试。",
				icon: dorado.MessageBox.WARNING_ICON,
				buttons: dorado.MessageBox.OK
			});
		} else new dorado.Exception("Main frame not found.");
	}
}

function createSymbolLink(alias, text) {
	var dom;
	if (symbolDictionary[alias]) {
		dom = document.createElement("A");
		dom.href = "javascript:openSymbolDoc(\"" + alias + "\")";
		$(dom).addClass("symbol-link").text(text || alias);
	} else {
		dom = document.createTextNode(text || alias);
	}
	return dom;
}

function createSymbolLinks(text) {
	var doms = [];
	if (text) {
		$.each(text.split('|'), function(i, section) {
			var index = section.indexOf("[]"), alias = section, isArray;
			if (index > 0 && index == section.length - 2) {
				isArray = true;
				alias = section.substring(0, section.length - 2);
			}
			if (i > 0) doms.push(document.createTextNode("|"));
			doms.push(createSymbolLink(alias, section));
		});
	}
	return doms;
}

function getDescriptionBrief(description) {
	if (!description) return "";
	var i1 = description.indexOf('\r'), i2 = description.indexOf('\n'), i = -1, ellipsis = '';
	if (i1 < 0) i = i2;
	else if (i1 < i2) i = i1;
	else if (i2 >= 0) i = i2;
	else i = i1;
	if (i < 0) i = description.length;
	if (i > 60) {
		i = 60;
		ellipsis = "...";
	}
	var brief = description.substring(0, i);
	return brief + ellipsis;
}

function getRequestParameters() {
	var params = {}, url = location.href;
	var index = url.indexOf('?');
	if (index > 0) {
		var paramsText = url.substring(index + 1);
		jQuery.each(paramsText.split('&'), function(i, paramText) {
			var index = paramText.indexOf('=');
			if (index > 0) {
				params[paramText.substring(0, index)] = unescape(paramText.substring(index + 1));
			}
		});
	}
	return params;
}

function loadScript(url, callback) {
	var element = document.createElement("script");
	if (dorado.Browser.msie) {
		element.onreadystatechange = function() {
			if (/loaded|complete/.test(this.readyState)) {
				if (callback) callback(url);
			}
		};
	} else {
		element.onload = function() {
			if (callback) callback(url);
		};
	}
	element.language = "JavaScript";
	element.type = "text/javascript";
	element.charset = "utf-8";
	element.src = url;
	var head = document.getElementsByTagName("head")[0] || document.documentElement;
	head.insertBefore(element, head.firstChild);
}

function getSymbolIcon(alias) {
	var symbolInfo = symbolDictionary[alias];
	var type = symbolInfo[0];
	var icon;
	if (alias == "_global_") {
		icon = "styles/global.gif";
	} else if (alias == "jQuery") {
		icon = "styles/jquery.gif";
	} else {
		switch (type) {
			case "namespace", "category":{
				icon = "styles/namespace.gif";
				break;
			}
			case "class":{
				icon = (symbolInfo[2] ? "styles/class-abstract.gif" : "styles/class.gif");
				break;
			}
			case "object":{
				icon = "styles/object.gif";
				break;
			}
			case "attribute":{
				icon = "styles/attribute.gif";
				break;
			}
			case "event":{
				icon = "styles/event.gif";
				break;
			}
			case "method":{
				icon = "styles/method.gif";
				break;
			}
			case "property":{
				icon = "styles/property.gif";
				break;
			}
			default:
				{
					icon = "styles/namespace.gif";
					break;
				}
		}
	}
	return icon;
}

SymbolMember = $extend(dorado.widget.Control, {
	ATTRIBUTES: /** @scope dorado.widget.Button.prototype */ {
		className: {
			defaultValue: "symbol-member"
		},
		
		contentContainer: {
			readOnly: true
		}
	},
	
	createDom: function() {
		var dom = $DomUtils.xCreateElement({
			tagName: "DIV",
			className: "symbol-member",
			style: {
				overflow: "hidden"
			},
			content: "^DIV"
		});
		this._contentContainer = dom.firstChild;
		return dom;
	},
	
	fitSize: function(noAnimate) {
		if (!this._rendered) return;
		var dom = this.getDom();
		var height = this._contentContainer.offsetHeight;
		if (noAnimate) {
			$fly(dom).height(height);
		} else {
			$fly(dom).animate({
				height: height
			}, "fast");
		}
	},
	
	changeContent: function(fn, noAnimate) {
		if (!this._rendered) {
			fn();
		} else {
			var dom = this.getDom();
			if (!dom.style.height) {
				dom.style.height = this._contentContainer.offsetHeight + "px";
			}
			fn();
			this.fitSize(noAnimate);
		}
	}
	
});
