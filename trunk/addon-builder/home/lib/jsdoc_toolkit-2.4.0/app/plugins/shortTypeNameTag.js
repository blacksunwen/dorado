JSDOC.PluginManager.registerPlugin("JSDOC.shortTypeNameTag", {
	onSymbol: function(symbol) {
		var shortTypeNameTag = symbol.comment.getTag("shortTypeName");
		if (shortTypeNameTag.length) shortTypeNameTag = shortTypeNameTag[0];
		else shortTypeNameTag = null;
		
		if (shortTypeNameTag && symbol.isa == "CONSTRUCTOR") {
			symbol.shortTypeName = shortTypeNameTag.desc;
		}
	}
});
