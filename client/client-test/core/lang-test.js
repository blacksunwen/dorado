function testArray() {
	var v = [ "A", "B", "C", "D", "E" ];
	assertEquals(5, v.length);

	v.push("F");
	v.push("G");
	assertEquals(7, v.length);

	assertEquals(2, v.indexOf("C"));
	assertEquals(4, v.indexOf("E"));
	assertEquals(6, v.indexOf("G"));
	assertEquals(-1, v.indexOf("Z"));

	v.removeAt(3);
	assertEquals(6, v.length);
	assertEquals(-1, v.indexOf("D"));

	v.removeAt(3);
	assertEquals(5, v.length);
	assertEquals(-1, v.indexOf("E"));
	assertEquals(4, v.indexOf("G"));

	v.insert("D", 3);
	assertEquals(6, v.length);
	assertEquals(3, v.indexOf("D"));
	assertEquals(5, v.indexOf("G"));

	v.insert("E", 4);
	assertEquals(7, v.length);
	assertEquals(3, v.indexOf("D"));
	assertEquals(4, v.indexOf("E"));
	assertEquals(6, v.indexOf("G"));
}
