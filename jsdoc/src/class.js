function getMemberType(alias) {
	return symbolDictionary[alias][0];
}

function getMemberId(name, memberType) {
	if (memberType == "attribute") name = "attribute:" + name;
	else if (memberType == "event") name = "event:" + name;
	return "elSymbolMember_" + name.replace(':', "__");
}

function getMemberIdByAlias(alias) {
	var symbolInfo = symbolDictionary[alias];
	return getMemberId(symbolInfo[1]);
}

function scrollMemberIntoView(alias) {
	var symbolInfo = symbolDictionary[alias];
	var tab = {
		attribute: "attributes",
		method: "methods",
		event: "events",
		property: "properties"
	}[symbolInfo[0]];
	
	tabsDetail.set("currentTab", tab);
	setTimeout(function() {
		var memberEl = document.getElementById(getMemberIdByAlias(alias));
		if (memberEl) {
			setTimeout(function() {
				var dom = memberEl.parentNode;
				dom.scrollIntoView();
				$(dom.parentNode).effect("highlight", {
					color: "#FF8000"
				}, 2000);
			}, 300);
		}
	}, 100);
}

function generateMemberOf(container, memberOf) {
	container.appendChild(document.createTextNode("from "));
	if (memberOf == mainSymbol.alias) {
		container.appendChild(document.createTextNode(memberOf.split('.').peek()));
	} else {
		container.appendChild(createSymbolLink(memberOf, memberOf.split('.').peek()));
	}
}

function generateAttributesDoc(dom, symbol, attributes) {
	if (attributes && attributes.length) {
		$.each(attributes, function(i, attribute) {
			generateAttributeDoc(dom, attribute);
		});
	}
}

function generateAttributeDoc(dom, member) {
	var memberType = getMemberType(member.alias);
	var symbolMember = new SymbolMember();
	symbolMember.getDom();
	var contentContainer = symbolMember.get("contentContainer");
	var $member = $(contentContainer);
	
	var subDoms = symbolMember._subDoms = {};
	var doms = $DomUtils.xCreateElement(function(member) {
		return [{
			id: getMemberId(member.name, memberType),
			tagName: "DIV",
			content: [{
				tagName: "SPAN",
				className: "member-visibility",
				contextKey: "memberVisibility",
				content: member.visibility + " ",
				style: {
					display: memberType == "property" ? "" : "none"
				}
			}, {
				tagName: "SPAN",
				className: "member-static",
				contextKey: "memberStatic",
				content: " static ",
				style: {
					display: member.isStatic ? "" : "none"
				}
			}, {
				tagName: "LABEL",
				className: "member-name  member-name-" + memberType + " member-name-" + member.visibility,
				contextKey: "memberName",
				content: member.name
			}, " : ", {
				tagName: "LABEL",
				className: "member-data-type member-data-type-" + memberType,
				contextKey: "memberDataType"
			}, " ", {
				tagName: "LABEL",
				className: "member-default",
				content: (member.defaultValue ? ("( = " + member.defaultValue + " )") : "")
			}, {
				tagName: "LABEL",
				className: "member-of member-of-" + memberType,
				contextKey: "memberOf",
				style: {
					position: "absolute",
					right: "20px"
				}
			}]
		}, {
			tagName: "DIV",
			className: "member-props",
			contextKey: "memberProps",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			className: "member-desc member-desc-" + memberType,
			contextKey: "memberDesc"
		}, {
			tagName: "P",
			className: "member-sees member-sees-" + memberType,
			contextKey: "memberSees",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			className: "member-examples member-examples-" + memberType,
			contextKey: "memberExamples",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			contextKey: "memberTail",
			style: {
				display: "none",
				overflow: "hidden",
				height: 8
			}
		}];
	}, member, subDoms);
	$.each(doms, function(i, dom) {
		contentContainer.appendChild(dom);
	});
	symbolMember.render(dom);
	
	if (memberType == "attribute") {
		var $memberProps = $fly(subDoms.memberProps);
		if (member.readOnly) $memberProps.append("<LABEL class=\"member-prop\">[ReadOnly]</LABEL>&nbsp;");
		if (member.writeOnly) $memberProps.append("<LABEL class=\"member-prop\">[WriteOnly]</LABEL>&nbsp;");
		if (member.writeOnce) $memberProps.append("<LABEL class=\"member-prop\">[WriteOnce]</LABEL>&nbsp;");
		if (member.skipAutoRefresh) $memberProps.append("<LABEL class=\"member-prop\">[skipAutoRefresh]</LABEL>&nbsp;");
		if (member.writeBeforeReady) $memberProps.append("<LABEL class=\"member-prop\">[WriteBeforeReady]</LABEL>&nbsp;");
	}
	
	symbolMember._member = member;
	$fly(subDoms.memberDesc).html(getDescriptionBrief(member.desc));
	$fly(subDoms.memberName).click(function() {
		toggleMemberDocExpand(symbolMember);
	});
	var memberDataType = (member.type) ? member.type : "void";
	$.each(createSymbolLinks(memberDataType), function(i, element) {
		subDoms.memberDataType.appendChild(element);
	});
	
	if (member.memberOf != "_global_") generateMemberOf(subDoms.memberOf, member.memberOf);
}

function generatorAttributeDocDetail(symbolMember) {
	var member = symbolMember._member, subDoms = symbolMember._subDoms;
	symbolMember._builded = true;
	if (member.see) {
		$fly(subDoms.memberSees).append("<DIV class=\"sees-title\">See:</DIV>");
		var ulElement = document.createElement("UL");
		$.each(member.see, function(i, see) {
			var liElement = document.createElement("LI");
			liElement.appendChild(createSymbolLink(see));
			ulElement.appendChild(liElement);
		});
		subDoms.memberSees.appendChild(ulElement);
	}
	if (member.example) {
		$.each(member.example, function(i, example) {
			if (i > 0) $fly(subDoms.memberExamples).append("&nbsp;");
			$fly(subDoms.memberExamples).append("<DIV class=\"example-title\">Example " + (i + 1) + ": <DIV>");
			var elExample = $DomUtils.xCreateElement({
				tagName: "PRE",
				className: "member-example code",
				content: {
					tagName: "CODE",
					className: "javascript"
				}
			});
			subDoms.memberExamples.appendChild(elExample);
			$fly(elExample.firstChild).html(example.desc);
			$fly(elExample).beautifyCode('javascript');
		});
	}
}

function generateMethodsDoc(dom, symbol, methods) {
	if (methods && methods.length) {
		$.each(methods, function(i, method) {
			generateMethodDoc(dom, method);
		});
	}
}

function generateMethodDoc(dom, member) {
	var memberType = getMemberType(member.alias);
	var symbolMember = new SymbolMember();
	symbolMember.getDom();
	var contentContainer = symbolMember.get("contentContainer");
	var $member = $(contentContainer);
	
	var subDoms = symbolMember._subDoms = {};
	var doms = $DomUtils.xCreateElement(function(member) {
		return [{
			id: getMemberId(member.name, memberType),
			tagName: "DIV",
			content: [{
				tagName: "SPAN",
				className: "member-visibility",
				contextKey: "memberVisibility",
				content: member.visibility + " ",
				style: {
					display: memberType == "method" ? "" : "none"
				}
			}, {
				tagName: "SPAN",
				className: "member-static",
				contextKey: "memberStatic",
				content: " static ",
				style: {
					display: member.isStatic ? "" : "none"
				}
			}, {
				tagName: "LABEL",
				className: "member-name  member-name-" + memberType + " member-name-" + member.visibility + (member.deprecated ? " deprecated" : ''),
				contextKey: "memberName",
				content: member.name
			}, " ( ", {
				tagName: "LABEL",
				className: "member-param-def member-param-def-" + memberType,
				contextKey: "memberParamsDef"
			}, " ) : ", {
				tagName: "LABEL",
				className: "member-data-type member-data-type-" + memberType,
				contextKey: "memberDataType"
			}, {
				tagName: "LABEL",
				className: "member-of member-of-" + memberType,
				contextKey: "memberOf",
				style: {
					position: "absolute",
					right: "20px"
				}
			}]
		}, {
			tagName: "DIV",
			className: "member-desc member-desc-" + memberType,
			contextKey: "memberDesc"
		}, {
			tagName: "P",
			className: "member-sees member-sees-" + memberType,
			contextKey: "memberSees",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			className: "member-params member-params-" + memberType,
			contextKey: "memberParams",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			className: "member-returns member-returns-" + memberType,
			contextKey: "memberReturns",
			style: {
				display: "none"
			}
		}, {
			tagName: "P",
			className: "member-exceptions member-exceptions-" + memberType,
			contextKey: "memberExceptions",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			className: "member-examples member-examples-" + memberType,
			contextKey: "memberExamples",
			style: {
				display: "none"
			}
		}, {
			tagName: "DIV",
			contextKey: "memberTail",
			style: {
				display: "none",
				overflow: "hidden",
				height: 8
			}
		}];
	}, member, subDoms);
	$.each(doms, function(i, dom) {
		contentContainer.appendChild(dom);
	});
	symbolMember.render(dom);
	
	symbolMember._member = member;
	$fly(subDoms.memberDesc).html(getDescriptionBrief(member.desc));
	$fly(subDoms.memberName).click(function() {
		toggleMemberDocExpand(symbolMember);
	});
	
	if (member.memberOf != "_global_") generateMemberOf(subDoms.memberOf, member.memberOf);
	
	if (member.params) {
		$.each(member.params, function(i, param) {
			if (param.name.indexOf('.') >= 0) return;
			if (i > 0) subDoms.memberParamsDef.appendChild(document.createTextNode(" , "));
			$.each(createSymbolLinks(param.type), function(j, element) {
				subDoms.memberParamsDef.appendChild(element);
			});
			subDoms.memberParamsDef.appendChild(document.createTextNode(" "));
			var name = param.isOptional ? ('[' + param.name + ']') : param.name;
			$fly(subDoms.memberParamsDef).append("<LABEL class=\"member-param-name " + (param.isOptional ? "member-param-name-optional" : "") + "\">" + name + "</LABEL>");
		});
	}
	
	var returnType = (member.returns) ? member.returns.type : "void";
	$.each(createSymbolLinks(returnType), function(i, element) {
		subDoms.memberDataType.appendChild(element);
	});
	
	if (memberType == "constructor") toggleMemberDocExpand(symbolMember, true);
}

function generatorMethodDocDetail(symbolMember) {
	var member = symbolMember._member, subDoms = symbolMember._subDoms;
	symbolMember._builded = true;
	if (member.see) {
		$fly(subDoms.memberSees).append("<DIV class=\"sees-title\">See:</DIV>");
		var ulElement = document.createElement("UL");
		$.each(member.see, function(i, see) {
			var liElement = document.createElement("LI");
			liElement.appendChild(createSymbolLink(see));
			ulElement.appendChild(liElement);
		});
		subDoms.memberSees.appendChild(ulElement);
	}
	if (member.params) {
		$fly(subDoms.memberParams).append("<DIV class=\"params-title\">Parameters:</DIV>");
		var ulElements = [document.createElement("UL")], currentDeepth = 0, lastLiElement;
		$.each(member.params, function(i, param) {
			var r = param.name.match(/\./g), deepth = r ? r.length : 0;
			if (deepth > currentDeepth) {
				var ulElement = document.createElement("UL");
				if (lastLiElement) lastLiElement.appendChild(ulElement);
				ulElements.push(ulElement);
				currentDeepth++;
			} else if (deepth < currentDeepth) {
				currentDeepth = deepth;
			}
			
			var name = param.name;
			var i = name.lastIndexOf('.');
			if (i >= 0) name = name.substring(i);
			
			var doms = {};
			lastLiElement = $DomUtils.xCreateElement(function(param) {
				return {
					tagName: "LI",
					className: "member-param",
					content: [{
						tagName: "LABEL",
						className: "member-param-name",
						contextKey: "name",
						content: name
					}, " : ", {
						tagName: "LABEL",
						className: "member-param-type",
						contextKey: "type"
					}, {
						tagName: "LABEL",
						className: "member-param-default",
						content: (param.defaultValue ? (" ( = " + param.defaultValue + " )") : "")
					}, {
						tagName: "LABEL",
						className: "member-param-writeable",
						content: (param.isOptional ? " [optional]" : "") + (param.writeable ? " [writeable]" : "")
					}, {
						tagName: "DIV",
						className: "member-param-desc",
						contextKey: "desc"
					}]
				};
			}, param, doms);
			if (param.isOptional) $fly(doms.name).addClass("member-param-name-optional");
			$.each(createSymbolLinks(param.type), function(i, element) {
				doms.type.appendChild(element);
			});
			$fly(doms.desc).html(param.desc);
			ulElements[currentDeepth].appendChild(lastLiElement);
		});
		subDoms.memberParams.appendChild(ulElements[0]);
	}
	if (member.returns) {
		$fly(subDoms.memberReturns).append("<DIV class=\"returns-title\">Returns:</DIV>");
		var doms = {};
		subDoms.memberReturns.appendChild($DomUtils.xCreateElement({
			tagName: "UL",
			content: {
				tagName: "LI",
				content: [{
					tagName: "LABEL",
					className: "member-return-type",
					contextKey: "type"
				}, {
					tagName: "DIV",
					className: "member-return-desc",
					contextKey: "desc"
				}]
			}
		}, null, doms));
		$.each(createSymbolLinks(member.returns.type), function(i, element) {
			doms.type.appendChild(element);
		});
		$fly(doms.desc).html(member.returns.desc);
	}
	if (member.exceptions) {
		$fly(subDoms.memberExceptions).append("<DIV class=\"exceptions-title\">Exceptions:</DIV>");
		var ulElement = document.createElement("UL");
		var liElement = document.createElement("LI");
		$.each(member.exceptions, function(i, exception) {
			if (i > 0) liElement.appendChild(document.createTextNode(" , "));
			liElement.appendChild(createSymbolLink(exception));
		});
		ulElement.appendChild(liElement);
		subDoms.memberExceptions.appendChild(ulElement);
	}
	if (member.example) {
		$.each(member.example, function(i, example) {
			$fly(subDoms.memberExamples).append("<DIV class=\"example-title\">Example " + (i + 1) + ": <DIV>");
			var elExample = $DomUtils.xCreateElement({
				tagName: "PRE",
				className: "member-example code",
				content: {
					tagName: "CODE",
					className: "javascript"
				}
			});
			subDoms.memberExamples.appendChild(elExample);
			$fly(elExample.firstChild).html(example.desc);
			$fly(elExample).beautifyCode('javascript');
		});
	}
}

function toggleMemberDocExpand(symbolMember, noAnimate) {
	symbolMember.changeContent(function() {
		var subDoms = symbolMember._subDoms;
		var $member = $(symbolMember.get("contentContainer"));
		var member = symbolMember._member, builded = symbolMember._builded, expanded = symbolMember._expanded;
		var memberType = getMemberType(member.alias), isAttributeLike = ["attribute", "property"].indexOf(memberType) >= 0;
		if (!builded) {
			if (isAttributeLike) generatorAttributeDocDetail(symbolMember);
			else generatorMethodDocDetail(symbolMember);
		}
		if (expanded) {
			$fly(subDoms.memberDesc).html(getDescriptionBrief(member.desc));
			if (isAttributeLike) $fly([subDoms.memberSees, subDoms.memberProps, subDoms.memberExamples]).hide();
			else $fly([subDoms.memberSees, subDoms.memberParams, subDoms.memberReturns, subDoms.memberExceptions, subDoms.memberExamples]).hide();
			$fly(subDoms.memberTail).hide();
		} else {
			$fly(subDoms.memberDesc).html(member.desc);
			$fly(subDoms.memberExamples).show();
			if (member.see) $fly(subDoms.memberSees).show();
			if (memberType == "attribute") {
				$fly(subDoms.memberProps).show();
			} else {
				if (member.params) $fly(subDoms.memberParams).show();
				if (member.returns) $fly(subDoms.memberReturns).show();
				if (member.exceptions) $fly(subDoms.memberExceptions).show();
			}
			$fly(subDoms.memberTail).show();
		}
		expanded = !expanded;
		symbolMember._expanded = expanded;
		$fly(symbolMember.getDom()).toggleClass("symbol-member-expanded", expanded);
	}, noAnimate);
}

var mainSymbol;
$(document).ready(function() {
	var params = getRequestParameters();
	var fileName = symbolDictionary[params.symbol][5] || params.symbol;
	loadScript("data/symbols/" + fileName + ".js", function() {
		mainSymbol = SYMBOLS[params.symbol];
		var name = mainSymbol.name, alias = mainSymbol.alias;
		if (name != "_global_") {
			var symbolTypeText;
			if (mainSymbol.type == "object") symbolTypeText = "singleton object";
			else if (mainSymbol.type == "class" && mainSymbol.isAbstract) {
				symbolTypeText = "class abstract";
				$("#elSybmolName").addClass("symbol-name-abstract");
			} else symbolTypeText = mainSymbol.type;
			$("#elSymbolType").text(symbolTypeText);
		}
		$("#elPackageName").text(alias.substring(0, alias.length - name.length));
		$("#elSybmolName").text((name == "_global_") ? "<Global>" : name).toggleClass("deprecated", !!mainSymbol.deprecated);
		
		if (mainSymbol.type == "namespace") {
			$("#elDoradoPackageRow,elShortTypeNameRow,#elSybmolProps,#elSybmolParams,#elSybmolSees,#elSybmolExamples,#elSybmolDetail").hide();
			
			if (mainSymbol.children) {
				var elSybmolChildren = document.getElementById("elSybmolChildren");
				var ulElement = document.createElement("UL");
				$.each(mainSymbol.children, function(i, child) {
					var liElement = document.createElement("LI");
					liElement.appendChild(createSymbolLink(child.alias));
					liElement.appendChild(document.createTextNode(" - "));
					var $desc = $("<LABEL class=\"child-desc\"></LABEL>");
					$desc.html(child.descBrief);
					liElement.appendChild($desc[0]);
					ulElement.appendChild(liElement);
				});
				elSybmolChildren.appendChild(ulElement);
			}
		} else {
			if (mainSymbol.author) $("#elAuthor").text(mainSymbol.author);
			else $("#elAuthorRow").hide();
			
			if (mainSymbol["package"]) $("#elDoradoPackage").text(mainSymbol["package"]);
			else $("#elDoradoPackageRow").hide();
			
			if (mainSymbol.shortTypeName) $("#elShortTypeName").text(mainSymbol.shortTypeName);
			else $("#elShortTypeNameRow").hide();
			
			var inheritsFrom = mainSymbol.inheritsFrom;
			if (inheritsFrom) {
				$elExtends = $("#elExtends");
				$.each(inheritsFrom, function(i, superClass) {
					if (i > 0) $elExtends[0].appendChild(document.createTextNode(" , "));
					$elExtends[0].appendChild(createSymbolLink(superClass));
				});
			} else {
				$("#elExtendsRow").hide();
			}
			
			var inherits = mainSymbol.inherits;
			if (inherits) {
				var $elSubclasses = $("#elSubclasses");
				$.each(inherits, function(i, subClass) {
					if (i > 0) $elSubclasses[0].appendChild(document.createTextNode(" , "));
					$elSubclasses[0].appendChild(createSymbolLink(subClass));
				});
			} else {
				$("#elSubclassesRow").hide();
			}
			
			if (mainSymbol.constructors && mainSymbol.constructors.length > 0) {
				var constructor = mainSymbol.constructors[0];
				var $elSybmolParams = $("#elSybmolParams");
				
				$elSybmolParams.append("<DIV class=\"params-title\">Constructor Parameters:</DIV>");
				var ulElement = document.createElement("UL");
				$.each(constructor.params, function(i, param) {
					var doms = {};
					var elparam = $DomUtils.xCreateElement(function(param) {
						return {
							tagName: "LI",
							className: "member-param",
							content: [{
								tagName: "LABEL",
								className: "member-param-name",
								contextKey: "name",
								content: (param.isOptional ? ('[' + param.name + ']') : param.name)
							}, " : ", {
								tagName: "LABEL",
								className: "member-param-type",
								contextKey: "type"
							}, " ", {
								tagName: "LABEL",
								className: "member-param-default",
								content: (param.defaultValue ? ("( = " + param.defaultValue + " )") : "")
							}, {
								tagName: "DIV",
								className: "member-param-desc",
								contextKey: "desc"
							}]
						};
					}, param, doms);
					if (param.isOptional) $fly(doms.name).addClass("member-param-name-optional");
					$.each(createSymbolLinks(param.type), function(i, element) {
						doms.type.appendChild(element);
					});
					$fly(doms.desc).html(param.desc);
					ulElement.appendChild(elparam);
				});
				$elSybmolParams[0].appendChild(ulElement);
			}
			
			if (mainSymbol.see) {
				var elSybmolSees = document.getElementById("elSybmolSees");
				$fly(elSybmolSees).append("<DIV class=\"sees-title\">See:</DIV>");
				var ulElement = document.createElement("UL");
				$.each(mainSymbol.see, function(i, see) {
					var liElement = document.createElement("LI");
					liElement.appendChild(createSymbolLink(see));
					ulElement.appendChild(liElement);
				});
				elSybmolSees.appendChild(ulElement);
			}
			
			if (mainSymbol.example) {
				var $symbolExamples = $("#elSybmolExamples");
				$.each(mainSymbol.example, function(i, example) {
					$symbolExamples.append("<DIV class=\"example-title\">Example " + (i + 1) + ": <DIV>");
					var elExample = $DomUtils.xCreateElement({
						tagName: "PRE",
						className: "member-example code",
						content: {
							tagName: "CODE",
							className: "javascript"
						}
					});
					$symbolExamples.append(elExample);
					$fly(elExample.firstChild).html(example.desc);
				});
			}
			
			var tabs = [];
			if (mainSymbol.attributes && mainSymbol.attributes.length > 0) {
				tabs.push({
					$type: "Control",
					name: "attributes",
					icon: "styles/attribute.gif",
					caption: "Attributes",
					control: new dorado.widget.Control()
				});
			}
			if (mainSymbol.events && mainSymbol.events.length > 0) {
				tabs.push({
					$type: "Control",
					name: "events",
					icon: "styles/event.gif",
					caption: "Events",
					control: new dorado.widget.Control()
				});
			}
			if (mainSymbol.methods && mainSymbol.methods.length > 0) {
				tabs.push({
					$type: "Control",
					name: "methods",
					icon: "styles/method.gif",
					caption: "Methods",
					control: new dorado.widget.Control()
				});
			}
			if (mainSymbol.properties && mainSymbol.properties.length > 0) {
				tabs.push({
					$type: "Control",
					name: "properties",
					icon: "styles/property.gif",
					caption: "Properties",
					control: new dorado.widget.Control()
				});
			}
			
			if (tabs.length) {
				var tabsDetail = window.tabsDetail = new dorado.widget.TabControl({
					tabs: tabs,
					beforeTabChange: function(self, arg) {
						var newTab = arg.newTab;
						if (newTab.docGenerated) return;
						newTab.docGenerated = true;
						
						var name = newTab.get("name");
						var dom = newTab.get("control").getDom();
						switch (name) {
							case "methods":{
								generateMethodsDoc(dom, mainSymbol, mainSymbol.methods);
								break;
							}
							case "events":{
								generateMethodsDoc(dom, mainSymbol, mainSymbol.events);
								break;
							}
							case "attributes":{
								generateAttributesDoc(dom, mainSymbol, mainSymbol.attributes);
								break;
							}
							case "properties":{
								generateAttributesDoc(dom, mainSymbol, mainSymbol.properties);
								break;
							}
						}
					}
				});
				tabsDetail.render(document.getElementById("elSybmolDetail"));
			}
		}
		
		if (mainSymbol.classDesc || mainSymbol.desc) $("#elSybmolDesc").html(mainSymbol.classDesc || mainSymbol.desc);
		
		$.beautyOfCode.init({
			autoLoad: false,
			baseUrl: "./",
			scripts: "scripts/",
			styles: "styles/",
			brushes: ["Xml", "JScript"],
			ready: function() {
				setTimeout(function() {
					$(".code").beautifyCode('javascript');
				}, 200);
			}
		});
		
		if (params.member) scrollMemberIntoView(params.member);
	});
});
