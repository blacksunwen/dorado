function testSort1() {
	var v = [6, 8, 1, 5, 3, 2, 9, 7, 4, 0];
	dorado.DataUtil.sort(v);
	assertEquals(v.toString(), "0,1,2,3,4,5,6,7,8,9");
}
