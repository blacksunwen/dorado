/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
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
             * <iframe>的name属性，用于部分需要设置该属性的场景，该属性需要在IFrame ready之前设置。
             * @type String
             * @attribute[writeBeforeReady]
             */
            name: {
                writeBeforeReady: true
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

						frame.releaseCurrentPage();

                        $fly(doms.iframe).addClass("hidden");
                        if (oldPath != value) {
                            frame._loaded = false;
                        }
						frame.replaceUrl(value);
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

		releaseCurrentPage: function() {
			var frame = this, doms = frame._doms;
			if (doms) {
				try {
					if (frame.isSameDomain()) {
						if (doms.iframe.contentWindow.dorado) {
							doms.iframe.contentWindow.dorado.Exception.IGNORE_ALL_EXCEPTIONS = true;
						}
						doms.iframe.contentWindow.document.write('');
						if(dorado.Browser.msie){
							doms.iframe.contentWindow.close();
							CollectGarbage();
						} else {
							doms.iframe.contentWindow.close();
						}
					} else {
						frame.replaceUrl(null);
					}
				} catch(e) {
					//console.log(e);
				}
			}
		},

		destroy: function() {
			var frame = this, doms = frame._doms;
			frame.releaseCurrentPage();
			if (doms) {
				$fly(doms.iframe).remove();
			}
			$invokeSuper.call(frame);
		},

		createDom: function() {
			var frame = this, doms = {}, dom = $DomUtils.xCreate( {
				tagName: "div",
				className: "i-iframe " + frame._className,
				content: [ {
					tagName: "iframe",
					contextKey: "iframe",
		            scrolling: dorado.Browser.isTouch ? "no" : "auto",
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

            if (frame._name != undefined)
                doms.iframe.name = frame._name || "";

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
				if (!frame.isActualVisible()) {
					frame._notifyResizeOnVisible = true;
					frame.onActualVisibleChange();
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
			delete frame._notifyResizeOnVisible;
            if (frame.isSameDomain()) {
	            frame._replacedUrl = replacedUrl;
                if (frame.getIFrameWindow()) frame.getIFrameWindow().location.replace(replacedUrl);
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

		doOnResize: function() {
			if (dorado.Browser.isTouch) {
				$fly(this._doms.iframe).css({
					width: this._dom.clientWidth,
					height: this._dom.clientHeight
				});
			}
		},

        /**
         * 重新载入页面。
         */
        reload: function() {
            var frame = this;
	        frame.releaseCurrentPage();
	        frame.replaceUrl(null);
	        frame.replaceUrl(frame._path);
        },

		refreshDom: function(dom) {
			$invokeSuper.call(this, [dom]);
			centerCover(dom, this._doms);
		},
		
		onActualVisibleChange: function() {

			function resizeSubView(subView) {
				subView._children.each(function(child) {
					if (child.resetDimension && child._rendered && child._visible) child.resetDimension();
				});
			}

			$invokeSuper.call(this, arguments);

			var frame = this, window = frame.getIFrameWindow(), actualVisible = frame.isActualVisible();
			//FIX OpenFlashChart BUG: http://bsdn.org/projects/dorado7/issue/dorado7-240
			if (frame._ready && frame.isSameDomain()) {
				//if (dorado.Browser.mozilla && window && window.dorado && window.dorado.widget && window.dorado.widget.ofc) {
				if (window && window.dorado && window.dorado.widget) {
					// 在Chrome中，一旦通过UpdateAction的回调方法关闭一个含有iFrame的Dialog，
					// 会在下载触发主窗体事件时引发jQuery报compareDocumentPosition找不到的错误。
					// 通过下面的setTimeout可以避免，但原因不详。
					// 2012/11/26
					if (dorado.Browser.chrome) {
						setTimeout(function() {
							window.$topView.setActualVisible(actualVisible);
							if (frame._notifyResizeOnVisible && actualVisible) {
								resizeSubView(window.$topView);
							}
						}, 0);
					}
					else {
						window.$topView.setActualVisible(actualVisible);
						if (frame._notifyResizeOnVisible && actualVisible) {
								resizeSubView(window.$topView);
						}
					}
				}
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
