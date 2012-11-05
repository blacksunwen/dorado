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
	dorado.widget.IFrame = $extend(dorado.widget.Control, /** @scope dorado.widget.IFrame.prototype */ {
		$className: "dorado.widget.IFrame",
        _inherentClassName: "i-iframe",
		ATTRIBUTES: /** @scope dorado.widget.IFrame.prototype */
		{
			className: {
				defaultValue: "d-iframe"
			},

			/**
			 * IFrame对应的路径。
			 * 
			 * @type String
			 * @attribute
			 */
			path: {
				skipRefresh: true,
				setter: function(value) {
					var frame = this, oldPath = frame._path, dom = frame._dom, doms = frame._doms;

                    frame._path = value;

					if (dom) {
						$fly(doms.loadingCover).css("display", "block");
                        try {
							doms.iframe.contentWindow.dorado.Exception.IGNORE_ALL_EXCEPTIONS = true;
                            doms.iframe.contentWindow.document.write('');
                            if(dorado.Browser.msie){
                                CollectGarbage();
                            }
                        } catch(e) {}
                        $fly(doms.iframe).addClass("hidden");
                        if (oldPath != value) {
                            frame._loaded = false;
                        }
						this.replaceUrl(value);
						centerCover(dom, doms);
					}
				}
			},

			/**
			 * 该组件内置的IFrame的window对象。
			 * 
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
			 * 
			 * @param {Object}
			 *            self 事件的发起者，即组件本身。
			 * @param {Object}
			 *            arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onLoad: {}
		},

        getDomainInfo: function(domain) {
            var regex = /^(http[s]?):\/\/([\w.]+)(:([\d]+))?/ig, result = regex.exec(domain);
            if (result) {
                return {
                    protocol: result[1],
                    domain: result[2],
                    port: result[4]
                };
            } else {
                return {};
            }
        },

        isSameDomain: function() {
	        var iframeSrc = $url(this._path);
            if (/^(http[s]?):/ig.test(iframeSrc)) {
                var localDomain = this.getDomainInfo(location.href), frameDomain = this.getDomainInfo(iframeSrc);
                return localDomain.protocol == frameDomain.protocol && localDomain.domain == frameDomain.domain && localDomain.port == frameDomain.port;
            }
            return true;
        },

		destroy: function() {
			var frame = this, doms = frame._doms;
			if (doms) {
                try {
                    if (frame.isSameDomain()) {
                        if (doms.iframe.contentWindow.dorado)
                            doms.iframe.contentWindow.dorado.Exception.IGNORE_ALL_EXCEPTIONS = true;
                        doms.iframe.contentWindow.document.write('');
                        if(dorado.Browser.msie){
                            CollectGarbage();
                        } else {
                            doms.iframe.contentWindow.close();
                        }
                    } else {
						this.replaceUrl(null);
                    }
                } catch(e) {
                    //console.log(e);
                }
            }
			$invokeSuper.call(this);
		},

		createDom: function() {
			var frame = this, doms = {}, dom = $DomUtils.xCreate( {
				tagName: "div",
				className: "i-iframe " + frame._className,
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
	        $fly(iframe).load(function() {
		        $fly(doms.loadingCover).css("display", "none");
		        // fix ie 6 bug....
		        if (!(dorado.Browser.msie && dorado.Browser.version == 6)) {
			        $fly(iframe).removeClass("hidden");
		        }
		        frame.fireEvent("onLoad", frame);
		        if (frame.isSameDomain()) {
			        if (frame._replacedUrl && frame._replacedUrl != BLANK_PATH) {
				        frame._loaded = true;
			        }
		        } else if (iframe.src && iframe.src != BLANK_PATH) {
			        frame._loaded = true;
		        }
	        });
	        frame.doLoad();
        },
		
		replaceUrl: function(url) {
            var frame = this, doms = frame._doms, replacedUrl = $url(url || BLANK_PATH);
            if (frame.isSameDomain()) {
	            frame._replacedUrl = replacedUrl;
                frame.getIFrameWindow().location.replace(replacedUrl);
            } else {
                $fly(doms.iframe).prop("src", replacedUrl);
            }
		},

        doLoad: function() {
            var frame = this, doms = frame._doms;
            $fly(doms.loadingCover).css("display", "");
			this.replaceUrl(frame._path);
        },

        reloadIfNotLoaded: function() {
            var frame = this;
            if (!frame._loaded && frame._path) {
                frame.doLoad();
            }
        },

        cancelLoad: function() {
			this.replaceUrl(BLANK_PATH);
        },

        /**
         * 重新载入页面。
         */
        reload: function() {
            var frame = this;
			this.replaceUrl(null);
			this.replaceUrl(frame._path);
        },

		refreshDom: function(dom) {
			$invokeSuper.call(this, [dom]);
			centerCover(dom, this._doms);
		},
		
		onActualVisibleChange: function() {
			var window = this.getIFrameWindow(), actualVisible = this.isActualVisible();
			//FIX OpenFlashChart BUG: http://bsdn.org/projects/dorado7/issue/dorado7-240
			if (this._ready && this.isSameDomain()) {
				//if (dorado.Browser.mozilla && window && window.dorado && window.dorado.widget && window.dorado.widget.ofc) {
				if (window && window.dorado && window.dorado.widget)
					window.$topView.setActualVisible(actualVisible);
				//}
			}
		},
		
		/**
		 * 取得iframe中的window对象，如果iframe还没有创建，则返回null。
		 * 
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
