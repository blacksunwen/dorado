/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
function jsUnitTestSuite() {
    this.isjsUnitTestSuite = true;
    this.testPages = Array();
    this.pageIndex = 0;
}

jsUnitTestSuite.prototype.addTestPage = function (pageName)
{
    this.testPages[this.testPages.length] = pageName;
}

jsUnitTestSuite.prototype.addTestSuite = function (suite)
{
    for (var i = 0; i < suite.testPages.length; i++)
        this.addTestPage(suite.testPages[i]);
}

jsUnitTestSuite.prototype.containsTestPages = function ()
{
    return this.testPages.length > 0;
}

jsUnitTestSuite.prototype.nextPage = function ()
{
    return this.testPages[this.pageIndex++];
}

jsUnitTestSuite.prototype.hasMorePages = function ()
{
    return this.pageIndex < this.testPages.length;
}

jsUnitTestSuite.prototype.clone = function ()
{
    var clone = new jsUnitTestSuite();
    clone.testPages = this.testPages;
    return clone;
}

if (xbDEBUG.on)
{
    xbDebugTraceObject('window', 'jsUnitTestSuite');
}

