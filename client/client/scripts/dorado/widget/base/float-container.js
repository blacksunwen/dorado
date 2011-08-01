/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Base
 * @class 浮动容器。
 * @extend dorado.widget.Container
 * @extend dorado.widget.FloatControl
 */
dorado.widget.FloatContainer = $extend([dorado.widget.Container, dorado.widget.FloatControl], {
	$className: "dorado.widget.FloatContainer",

	focusable: true,

	ATTRIBUTES: {
		visible: {
			defaultValue: false
		}
	}

});
