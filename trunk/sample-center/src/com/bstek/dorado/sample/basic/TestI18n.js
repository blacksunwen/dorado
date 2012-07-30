/*
 * @Bind #buttonTestParam.onClick
 */
!function() {
	dorado.MessageBox.alert("${res.get('paramString', util.getDate())}");
}