JSDOC.PluginManager.registerPlugin("JSDOC.attributeTag", {
	onSymbol: function(symbol) {
		var attributeTag = symbol.comment.getTag("attribute");
		if (attributeTag.length) attributeTag = attributeTag[0];
		else attributeTag = null;
		
		if (attributeTag && symbol.isa == "OBJECT") {
			if (symbol._name.indexOf("attribute:") < 0) {
				symbol._name = symbol._name.replace("#", "#attribute:");
				symbol.alias = symbol.alias.replace("#", "#attribute:");
			}
			
			var propsText = attributeTag.desc;
			if (propsText) {
				propsText = propsText.toLowerCase();
				if (propsText.indexOf("readonly") >= 0) symbol.readOnly = true;
				if (propsText.indexOf("writeonly") >= 0) symbol.writeOnly = true;
				if (propsText.indexOf("writeonce") >= 0) symbol.wirteOnce = true;
				if (propsText.indexOf("skipautorefresh") >= 0) symbol.skipAutoRefresh = true;
				if (propsText.indexOf("writebeforeready") >= 0) symbol.writeBeforeReady = true;
			}
		}
	}
});
