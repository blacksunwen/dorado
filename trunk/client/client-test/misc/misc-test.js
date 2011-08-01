function testFunctionInJSON() {
	var text = "{\n" + "	\"p1\": \"value1\",\n" +
	"	\"p2\": function() {\n" +
	"		return \"value2\";\n" +
	"	},\n" +
	"	\"p3\": (function() {\n" +
	"		return \"value3\";\n" +
	"	})()\n" +
	"}";
	
	var json = eval("(" + text + ")");
	assertEquals("value2", json.p2());
	assertEquals("value3", json.p3);
	
	var text = JSON.stringify(json, function(key, value) {
		return (value instanceof Function) ? value.call(this) : value;
	});
	
	json = eval("(" + text + ")");
	assertEquals("value2", json.p2);
	assertEquals("value3", json.p3);
}
