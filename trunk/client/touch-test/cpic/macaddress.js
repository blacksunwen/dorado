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
var MacAddress = function() {
};

MacAddress.prototype.get = function(successCallback, failureCallback) {
 return PhoneGap.exec(successCallback,    //Success callback from the plugin
      failureCallback,     //Error callback from the plugin
      'MacAddressPlugin',  //Tell PhoneGap to run "MacAddressPlugin"
      'get',              //Tell plugin, which action we want to perform
      []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("macAddress", new MacAddress());
});
