JSDOC.PluginManager.registerPlugin("JSDOC.componentTag", {
	onSymbol: function(symbol) {
		var componentTag = symbol.comment.getTag("component");
		if (componentTag.length) componentTag = componentTag[0];
		else componentTag = null;
		
		if (componentTag && symbol.isa == "CONSTRUCTOR") {
			symbol.isComponent = true;
			symbol.componentCategory = componentTag.desc;
		}
	}
});
