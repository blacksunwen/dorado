/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

function testDate() {
	assertTrue(Date.parseDate("1976-04-03", "Y-m-d") instanceof Date);
	assertTrue(Date.parseDate("19760403", "Ymd") instanceof Date);
	assertTrue(Date.parseDate("19760403", "Y-m-d") == null);
	assertTrue(Date.parseDate("1976-04-03 06:20:45", "Y-m-d h:i:s") instanceof Date);
}
