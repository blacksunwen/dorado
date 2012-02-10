function searchSymbols(text) {
	var results = [], text = text.toLowerCase();
	for (var p in symbolDictionary) {
		if (symbolDictionary.hasOwnProperty(p)) {
			var name = symbolDictionary[p][1];
			if (name.toLowerCase().indexOf(text) >= 0) results.push(p);
		}
	}
	return results;
}

$(document).ready(function() {
	var params = getRequestParameters();
	if (!params.text) return;
	
	$("#elSearchText").text("\"" + params.text + "\"");
	
	var results = searchSymbols(params.text);
	var elSearchResults = document.getElementById("elSearchResults");
	var ulElement = document.createElement("UL");
	$.each(results, function(i, alias) {
		var liElement = document.createElement("LI");
		var img = document.createElement("IMG");
		img.className = "search-result-icon";
		img.src = getSymbolIcon(alias);
		liElement.appendChild(img);
		liElement.appendChild(createSymbolLink(alias));
		ulElement.appendChild(liElement);
	});
	elSearchResults.appendChild(ulElement);
	$(elSearchResults).highlight(params.text);
});
