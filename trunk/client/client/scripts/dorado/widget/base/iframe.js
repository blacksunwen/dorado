( function() {
	function centerCover(dom, doms) {
		var width = $fly(dom).width(), height = $fly(dom).height(), loadingCoverImg = doms.loadingCoverImg, imgWidth = $fly(
				loadingCoverImg).outerWidth(), imgHeight = $fly(loadingCoverImg).outerHeight();
		$fly(loadingCoverImg).left((width - imgWidth) / 2).top((height - imgHeight) / 2);
	}

	var BLANK_PATH = "about:blank";

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Base
	 * @class IFrame组件。
	 * @extends dorado.widget.Control
	 */
	dorado.widget.IFrame = $extend(dorado.widget.Control, /** @scope dorado.widget.IFrame.prototype */
	{
		$className: "dorado.widget.IFrame",

		ATTRIBUTES: /** @scope dorado.widget.IFrame.prototype */
		{
			className: {
				defaultValue: "d-iframe"
			},

			/**
			 * IFrame对应的路径。
			 * @type String
			 * @attribute
			 */
			path: {
				skipRefresh: true,
				setter: function(value) {
					var frame = this, dom = frame._dom, doms = frame._doms;

					if (dom) {
						$fly(doms.loadingCover).css("display", "block");
						$fly(doms.iframe).attr("src", value || BLANK_PATH).addClass("hidden");

						centerCover(dom, doms);
					}
					frame._path = value;
				}
			},

			/**
			 * 该组件内置的IFrame的window对象。
			 * @attribute readOnly
			 * @type Window
			 */
			iFrameWindow: {
				readOnly: true,
				getter: function() {
					return this.getIFrameWindow();
				}
			}
		},

		EVENTS: /** @scope dorado.widget.IFrame.prototype */
		{
			/**
			 * 当iframe中的页面加载完毕后触发。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onLoad: {}
		},

		createDom: function() {
			var frame = this, doms = {}, dom = $DomUtils.xCreateElement( {
				tagName: "div",
				className: frame._className,
				content: [ {
					tagName: "iframe",
					contextKey: "iframe",
					frameBorder: 0,
					className: "hidden"
				}, {
					tagName: "div",
					contextKey: "loadingCover",
					className: "frame-loading-cover",
					content: {
						tagName: "div",
						className: "frame-loading-image",
						contextKey: "loadingCoverImg"
					}
				} ]
			}, null, doms);

			frame._doms = doms;

			return dom;
		},

        doOnAttachToDocument: function() {
            var frame = this, doms = frame._doms, iframe = doms.iframe;
			$fly(iframe).attr("src", $url(frame._path || BLANK_PATH)).load( function() {
				$fly(doms.loadingCover).css("display", "none");
				//fix ie 6 bug....
				if (!(dorado.Browser.msie && dorado.Browser.version == 6)) {
					$fly(iframe).removeClass("hidden");
				}
				frame.fireEvent("onLoad", frame);
			});
        },

		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);
			centerCover(dom, this._doms);
		},

		/**
		 * 取得iframe中的window对象，如果iframe还没有创建，则返回null。
		 * @return {window} iframe的contentWindow对象
		 */
		getIFrameWindow: function() {
			var frame = this, doms = frame._doms || {};
			if (doms.iframe) {
				return doms.iframe.contentWindow;
			}
			return null;
		}
	});
})();
