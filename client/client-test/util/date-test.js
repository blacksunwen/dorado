function testDate() {
	assertTrue(Date.parseDate("1976-04-03", "Y-m-d") instanceof Date);
	assertTrue(Date.parseDate("19760403", "Ymd") instanceof Date);
	assertTrue(Date.parseDate("19760403", "Y-m-d") == null);
	assertTrue(Date.parseDate("1976-04-03 06:20:45", "Y-m-d h:i:s") instanceof Date);
}
