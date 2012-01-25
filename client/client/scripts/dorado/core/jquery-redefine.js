/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @name jQuery
 * @class 针对jQuery的扩展。
 *        <p>
 *        <b>注意：这里给出的并不是jQuery的使用文档，而是dorado7对jQuery所做了一系列扩展方法的文档。</b>
 *        </p>
 * @static
 */
(function($) {

	// 在jQuery 1.2.6、jQuery 1.3.2中Safari下的ready事件总是在document.readyState
	// 变为complete之前触发，这导致在此事件中进行渲染的DOM对象无法正确的提取offsetHeight等属性。
	// 1.4.x下此现象似乎又转移到了Chrome中，但只在个别情况下出现，如访问一个已经被缓存的页面时。
	var superReady = $.prototype.ready;
	$.prototype.ready = function(fn) {
		if (jQuery.browser.webkit) {
			var self = this;
			function waitForReady() {
				if (document.readyState !== "complete") {
					setTimeout(waitForReady, 10);
				} else {
					superReady.call(self, fn);
				}
			}
			waitForReady();
		} else {
			superReady.call(this, fn);
		}
	};
	
	var flyableElem = $();
	flyableElem.length = 1;
	var flyableArray = $();
	
	/**
	 * @name $fly
	 * @function
	 * @description 根据传入的DOM元素返回一个jQuery的对象。
	 *              <p>
	 *              注意：与jQuery()或$()不同，$fly()为了提高效率并不总是返回新的jQuery对象的实例。
	 *              因此，保留$fly()返回的实例变量不能保证我们能够一直操作同样的DOM元素。
	 *              </p>
	 * @param {HTMLElement|HTMLElement[]}
	 *            elems 要包装的DOM元素或DOM元素的数组。
	 * @return {jQuery} jQuery对象的实例。
	 *
	 * @example var elem1 = $fly("div1"); elem1.text("Text A"); //
	 *          将div1的内容设置为"Text A"。
	 *
	 * var elem2 = $fly("div2"); // 此处返回的elem2很可能和elem1是同一个jQuery实例。
	 * elem2.text("Text B"); // 将div2的内容设置为"Text B"。
	 *
	 * elem1.text("Text C"); // 很可能改写的不是div1的内容，而是div2的内容。
	 */
	$fly = function(elems) {
		if (elems instanceof Array) {
			if (dorado.Browser.mozilla && dorado.Browser.version >= 2 ||
			dorado.Browser.msie && dorado.Browser.version < 9) {
				for (var i = flyableArray.length - 1; i >= 0; i--) {
					delete flyableArray[i];
				}
			}
			Array.prototype.splice.call(flyableArray, 0, flyableArray.length);
			Array.prototype.push.apply(flyableArray, elems);
			return flyableArray;
		} else {
			flyableElem[0] = elems;
			return flyableElem;
		}
	};
	
})(jQuery);
