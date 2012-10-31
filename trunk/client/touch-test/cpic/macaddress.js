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
