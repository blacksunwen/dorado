function testGetJSON() {
	var data = Test.getJSON(Test.ROOT + "resource/hr-data.js");
	assertEquals(2, data.length);
}
