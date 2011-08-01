var Resource = dorado.util.Resource;

Resource.append( {
	property1: "string1",
	property2: "string2"
});

Resource.append("test.ns1", {
	property1: "ns1-string1",
	property2: "ns1-string2"
});

Resource.append("test.ns2", {
	property1: "ns2-string1",
	property2: "ns2-string2"
});

Resource.append("test.ns2", {
	property2: "ns2-string2-overwrited",
	property3: "ns2-string3",
	property4: "ns2-string4"
});

Resource.append("test.ns3.ns4", {
	property1: "My name is {0}, I'm {1} years old."
});

function test() {
	assertEquals("string1", Resource.get("property1"));
	assertEquals("string2", $resource("property2"));

	assertEquals("ns1-string1", Resource.get("test.ns1.property1"));
	assertEquals("ns1-string2", $resource("test.ns1.property2"));

	assertEquals("ns2-string1", Resource.get("test.ns2.property1"));
	assertEquals("ns2-string2-overwrited", $resource("test.ns2.property2"));
	assertEquals("ns2-string3", Resource.get("test.ns2.property3"));
	assertEquals("ns2-string4", $resource("test.ns2.property4"));

	assertEquals("My name is John, I'm 5 years old.", $resource("test.ns3.ns4.property1", "John", 5));
}