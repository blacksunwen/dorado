/** Called automatically by JsDoc Toolkit. */
function publish(symbolSet) {

	publish.conf = { // trailing slash expected for dirs
		ext: ".html",
		outDir: JSDOC.opt.d || SYS.pwd + "../out/jsdoc/data",
		templatesDir: JSDOC.opt.t || SYS.pwd + "../templates/jsdoc/",
		symbolsDir: "symbols/",
		srcDir: "symbols/src/"
	};
	
	// some ustility filters
	function hasNoParent($) {
		return ($.memberOf == "")
	}
	
	function isaFile($) {
		return ($.is("FILE"))
	}
	
	function isaClass($) {
		return ($.is("CONSTRUCTOR") || $.isNamespace)
	}
	
	function getSymbolType(symbol) {
		var type = symbol.isa, name = symbol._name;
		if ("_global_" == symbol.alias) {
			type = "object";
		} else {
			if (symbol.isNamespace) {
				type = (type == "CONSTRUCTOR") ? "object" : "namespace";
			} else if (type == "CONSTRUCTOR") type = "class";
			else if (type == "OBJECT") type = (name.indexOf("attribute:") >= 0) ? "attribute" : "property";
			else if (type == "FUNCTION") type = (symbol.isEvent) ? "event" : ((name == "constructor") ? "constructor" : "method");
		}
		return type;
	}
	
	function getVisibility(symbol) {
		var visibility = "public";
		if (symbol.isPrivate) visibility = "private";
		else if (symbol.comment.getTag("protected").length) visibility = "protected";
		return visibility;
	}
	
	function copyProperties(target, source, properties) {
		for (var i = 0; i < properties.length; i++) {
			var property = properties[i], v = source[property];
			if (typeof v != "undefined") {
				if (v instanceof Array && v.length == 0) continue;
				if (v === false) continue;
				if (v === '') continue;
				target[property] = v;
			}
		}
	}
	
	function populateSymbolBase(store, symbol) {
		store.name = symbol._name;
		store.type = symbol.$type;
		store.desc = convertLinks(symbol.desc);
		if (symbol.comment.getTag("deprecated").length) store.deprecated = true;
		copyProperties(store, symbol, ["alias", "author", "version", "example", "see", "addOn"]);
	}
	
	function populateSymbol(store, symbol) {
		if (symbol.$type == "class" || symbol.$type == "object") populateClass(store, symbol);
		else if (symbol.$type == "namespace") populateNamespace(store, symbol);
	}
	
	function populateNamespace(store, symbol) {
		populateSymbolBase(store, symbol);
		store.classDesc = convertLinks(symbol.classDesc);
		copyProperties(store, symbol, ["children"]);
	}
	
	function populateClass(store, symbol) {
		populateSymbolBase(store, symbol);
		if (symbol.comment.getTag("abstract").length) store.isAbstract = true;
		store.classDesc = convertLinks(symbol.classDesc);
		copyProperties(store, symbol, ["package", "shortTypeName", "inheritsFrom", "inherits", "memberOf"]);
		
		var properties = symbol.properties;
		var propertiesStore = store.properties = [], attributesStore = store.attributes = [];
		for (var i = 0; i < properties.length; i++) {
			var propertySymbol = symbolSet.getSymbol(properties[i].alias), propertyStore = {};
			if (!propertySymbol || propertySymbol.isPrivate) continue;
			
			if (propertySymbol.comment.getTag("attribute").length) {
				populateAttribute(propertyStore, propertySymbol);
				attributesStore.push(propertyStore);
			} else {
				populateProperty(propertyStore, propertySymbol);
				propertiesStore.push(propertyStore);
			}
		}
		
		var methods = symbol.methods;
		var methodsStore = store.methods = [], constructorsStore = store.constructors = [], eventsStore = store.events = [];
		for (var i = 0; i < methods.length; i++) {
			var methodSymbol = symbolSet.getSymbol(methods[i].alias), methodStore = {};
			if (!methodSymbol || methodSymbol.isPrivate) continue;
			
			if (methodSymbol.comment.getTag("event").length) {
				populateEvent(methodStore, methodSymbol);
				eventsStore.push(methodStore);
			} else {
				populateMethod(methodStore, methodSymbol);
				methodsStore.push(methodStore);
			}
		}
		
		if (symbol._params && symbol.params.length) {
			var constuctor = {
				name: "constuctor",
				type: "constuctor",
				memberOf: symbol.alias
			};
			var params = symbol._params, paramsStore = [];
			for (var i = 0; i < params.length; i++) {
				var param = params[i], paramStore = {};
				populateParam(paramStore, param);
				paramsStore.push(paramStore);
			}
			constuctor.params = paramsStore;
			constructorsStore.push(constuctor);
		}
	}
	
	function populateProperty(store, symbol) {
		populateSymbolBase(store, symbol);
		store.visibility = getVisibility(symbol);
		copyProperties(store, symbol, ["type", "defaultValue", "isStatic", "isConstant", "memberOf", "package"]);
	}
	
	function populateAttribute(store, symbol) {
		populateProperty(store, symbol);
		store.name = symbol._name.substring(10);
		copyProperties(store, symbol, ["package", "defaultValue", "readOnly", "writeOnly", "writeOnce", "skipAutoRefresh", "writeBeforeReady"]);
	}
	
	function populateParam(store, param) {
		store.desc = convertLinks(param.desc);
		if (param.name.charAt(0) == '#') {
			param.name = param.name.substring(1);
			param.writeable = true;
		}
		copyProperties(store, param, ["type", "name", "isOptional", "defaultValue", "writeable"]);
	}
	
	function populateMethod(store, symbol) {
		populateSymbolBase(store, symbol);
		store.visibility = getVisibility(symbol);
		
		var params = symbol._params, paramsStore = [];
		for (var i = 0; i < params.length; i++) {
			var param = params[i], paramStore = {};
			populateParam(paramStore, param);
			paramsStore.push(paramStore);
		}
		if (paramsStore.length) store.params = paramsStore;
		
		if (symbol.returns.length) {
			store.returns = {};
			populateParam(store.returns, symbol.returns[0]);
		}
		
		if (symbol.exceptions.length) {
			store.exceptions = [];
			for (var i = 0; i < symbol.exceptions.length; i++) {
				store.exceptions.push(symbol.exceptions[i].type);
			}
		}
		copyProperties(store, symbol, ["package", "isStatic", "memberOf"]);
	}
	
	function populateConstructor(store, symbol) {
		populateMethod(store, symbol)
	}
	
	function populateEvent(store, symbol) {
		populateMethod(store, symbol)
		store.name = symbol._name.substring(6);
	}
	
	load(publish.conf.templatesDir + "json2.js");
	// IO.saveFile(publish.conf.outDir, "json.js", "var json = " + JSON.stringify(symbolSet));
	
	IO.makeDir(publish.conf.outDir);
	IO.makeDir(publish.conf.outDir + "/symbols");
	
	var symbols = symbolSet.toArray().slice(0).sort(makeSortby("alias"));
	var symbolDictionary = {};
	var symbolTree = [], symbolTreeNodeMap = {}, componentCategoryMap = {};
	
	for (var i = 0; i < symbols.length; i++) {
		var symbol = symbols[i], alias = symbol.alias, isObject = isaClass(symbol);
		symbol.$type = getSymbolType(symbol);
		if (symbol.srcFile) symbol["package"] = parsePackageFromSrcFile(symbol.srcFile);
		
		if (symbol.inheritsFrom && symbol.inheritsFrom.length > 0) {
			var inheritsFrom = [];
			for (var j = 0; j < symbol.inheritsFrom.length; j++) {
				var from = symbol.inheritsFrom[j];
				if (inheritsFrom.indexOf(from) < 0) inheritsFrom.push(from);
			}
			symbol.inheritsFrom = inheritsFrom;
		}
		symbolDictionary[alias] = [symbol.$type, symbol.name, (symbol.comment.getTag("abstract").length > 0), (symbol.comment.getTag("deprecated").length > 0), symbol.memberOf];
		if (!isObject) continue;
		symbolTreeNodeMap[alias] = {
			alias: alias
		};
	}
	
	for (var i = 0; i < symbols.length; i++) {
		var symbol = symbols[i], alias = symbol.alias, isObject = isaClass(symbol);
		if (!isObject) continue;
		
		var treeNode = symbolTreeNodeMap[alias];
		if (symbol.inheritsFrom) {
			for (var j = 0; j < symbol.inheritsFrom.length; j++) {
				var superSymbol = symbolSet.getSymbol(symbol.inheritsFrom[j]);
				if (superSymbol) {
					if (!superSymbol.inherits) superSymbol.inherits = [];
					if (superSymbol.inherits.indexOf(alias) < 0) superSymbol.inherits.push(alias);
				}
			}
		}
		
		if (symbol.memberOf) {
			var parentTreeNode = symbolTreeNodeMap[symbol.memberOf];
			if (!parentTreeNode) print("Error: Invalid symbol.memberOf [" + symbol.memberOf + "].");
			if (!parentTreeNode.children) parentTreeNode.children = [];
			parentTreeNode.children.push(treeNode);
			
			var parentSymbol = symbolSet.getSymbol(symbol.memberOf);
			if (parentSymbol.$type == "namespace") {
				if (!parentSymbol.children) parentSymbol.children = [];
				parentSymbol.children.push({
					alias: symbol.alias,
					descBrief: getDescriptionBrief(symbol.classDesc)
				});
			}
		} else {
			symbolTree.push(treeNode);
		}
		
		if (symbol.$type == "class" && symbol.isComponent) {
			if (symbol.alias.match("^dorado.widget.") == "dorado.widget.") {
				symbol.shortTypeName = symbol.alias.substring(14);
			}
			
			var category = symbol.componentCategory || "Others";
			var components = componentCategoryMap[category];
			if (!components) {
				components = [];
				componentCategoryMap[category] = components;
			}
			components.push(symbolTreeNodeMap[symbol.alias]);
		}
	}
	
	for (var i = 0; i < symbolTree.length; i++) {
		if (symbolTree[i].alias == "dorado") {
			var node = symbolTree[i];
			symbolTree.splice(i, 1);
			symbolTree.push(node);
			break;
		}
	}
	
	var fileNameMap = {};
	for (var i = 0; i < symbols.length; i++) {
		var symbol = symbols[i], alias = symbol.alias, isObject = isaClass(symbol);
		if (!isObject) continue;
		
		var store = {};
		populateSymbol(store, symbol);
		
		var tryCount = 0, fileName;
		do {
			fileName = symbol.alias + ((tryCount > 0) ? tryCount : '');
			tryCount++;
		}
		while (fileNameMap[fileName.toLowerCase()]);
		
		print("Save file [" + publish.conf.outDir + "symbols/" + fileName + ".js].");
		IO.saveFile(publish.conf.outDir + "symbols/", fileName + ".js", "SYMBOLS[\"" + symbol.alias + "\"]=" + JSON.stringify(store));
		fileNameMap[fileName.toLowerCase()] = true;
		
		if (tryCount > 1) {
			symbolDictionary[symbol.alias][5] = fileName;
		}
	}
	
	print("Save file [" + publish.conf.outDir + "package-tree.js].");
	IO.saveFile(publish.conf.outDir, "package-tree.js", "var PACKAGE_TREE=" + JSON.stringify(symbolTree));
	
	var componentTree = [];
	for (var p in componentCategoryMap) {
		var components = componentCategoryMap[p];
		symbolDictionary["componentCategory:" + p] = ["category", p, false, false];
		componentTree.push({
			alias: "componentCategory:" + p,
			children: components.sort(makeSortby("alias"))
		});
	}
	componentTree = componentTree.sort(makeSortby("alias"));
	
	print("Save file [" + publish.conf.outDir + "component-tree.js].");
	IO.saveFile(publish.conf.outDir, "component-tree.js", "var COMPONENT_TREE=" + JSON.stringify(componentTree));
	
	print("Save file [" + publish.conf.outDir + "symbol-dictionary.js].");
	IO.saveFile(publish.conf.outDir, "symbol-dictionary.js", "var SYMBOL_DICTIONARY=" + JSON.stringify(symbolDictionary));
}

function parsePackageFromSrcFile(srcFile) {
	var i = srcFile.length - 1;
	for (; i >= 0; i--) {
		var c = srcFile.charAt(i);
		if (c == '\\' || c == '/') {
			break;
		}
	}
	return srcFile.substring(i + 1, srcFile.length - 3);
}

/** Make a symbol sorter by some attribute. */
function makeSortby(attribute) {
	return function(a, b) {
		if (a[attribute] != undefined && b[attribute] != undefined) {
			a = a[attribute].toLowerCase();
			b = b[attribute].toLowerCase();
			if (a < b) return -1;
			if (a > b) return 1;
			return 0;
		}
	}
}

function getDescriptionBrief(description) {
	var i1 = description.indexOf('\r'), i2 = description.indexOf('\n'), i = -1, ellipsis = '';
	if (i1 < 0) i = i2;
	else if (i1 < i2) i = i1;
	else if (i2 >= 0) i = i2;
	else i = i1;
	if (i < 0) i = description.length;
	if (i > 40) {
		i = 40;
		ellipsis = "...";
	}
	var brief = description.substring(0, i);
	return brief + ellipsis;
}

function convertLinks(text) {
	if (!text) return null;
	
	var result = '';
	while (true) {
		var i = text.indexOf("{@link ");
		if (i < 0) break;
		if (i > 0 && text.charAt(i - 1) == '\\') continue;
		
		result += text.substring(0, i);
		i += 6;
		var len = text.length, c, alias = '';
		while (i < len) {
			i++;
			c = text.charAt(i);
			if (c == '}') break;
			if (c == ' ') continue;
			alias += c;
		}
		result += "<A class=\"symbol-link\" href=\"javascript:openSymbolDoc('" + alias + "')\">" + alias + "</A>";
		text = text.substring(i + 1);
	}
	result += text;
	return result;
}
