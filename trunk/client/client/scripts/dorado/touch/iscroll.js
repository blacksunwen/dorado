/*!
 * iScroll v4.1.9 ~ Copyright (c) 2011 Matteo Spinelli, http://cubiq.org
 * Released under MIT license, http://cubiq.org/license
 */
(function(){
    var m = Math,
        mround = function (r) { return r >> 0; },
        vendor = (/webkit/i).test(navigator.appVersion) ? 'webkit' :
            (/firefox/i).test(navigator.userAgent) ? 'Moz' :
                (/trident/i).test(navigator.userAgent) ? 'ms' :
                    'opera' in window ? 'O' : '',

    // Browser capabilities
        isAndroid = (/android/gi).test(navigator.appVersion),
        isIDevice = (/iphone|ipad/gi).test(navigator.appVersion),
        isPlaybook = (/playbook/gi).test(navigator.appVersion),
        isTouchPad = (/hp-tablet/gi).test(navigator.appVersion),

        has3d = 'WebKitCSSMatrix' in window && 'm11' in new WebKitCSSMatrix(),
        hasTouch = 'ontouchstart' in window && !isTouchPad,
        hasTransform = vendor + 'Transform' in document.documentElement.style,
        hasTransitionEnd = isIDevice || isPlaybook,

        nextFrame = (function() {
            return window.requestAnimationFrame
                || window.webkitRequestAnimationFrame
                || window.mozRequestAnimationFrame
                || window.oRequestAnimationFrame
                || window.msRequestAnimationFrame
                || function(callback) { return setTimeout(callback, 1); }
        })(),
        cancelFrame = (function () {
            return window.cancelRequestAnimationFrame
                || window.webkitCancelAnimationFrame
                || window.webkitCancelRequestAnimationFrame
                || window.mozCancelRequestAnimationFrame
                || window.oCancelRequestAnimationFrame
                || window.msCancelRequestAnimationFrame
                || clearTimeout
        })(),

    // Events
        RESIZE_EV = 'onorientationchange' in window ? 'orientationchange' : 'resize',
        START_EV = hasTouch ? 'touchstart' : 'mousedown',
        MOVE_EV = hasTouch ? 'touchmove' : 'mousemove',
        END_EV = hasTouch ? 'touchend' : 'mouseup',
        CANCEL_EV = hasTouch ? 'touchcancel' : 'mouseup',
        WHEEL_EV = vendor == 'Moz' ? 'DOMMouseScroll' : 'mousewheel',

    // Helpers
        trnOpen = 'translate' + (has3d ? '3d(' : '('),
        trnClose = has3d ? ',0)' : ')',
        TransformOrigin = "-" + vendor + "-transform-origin",
        Transform = "-" + vendor + "-transform",
        TransitionDuration = "-" + vendor + "-transition-duration",
        TransitionProperty = "-" + vendor + "-transition-property",
        TransitionTimingFunction = "-" + vendor + "-transition-timing-function",

    // Constructor
        iScroll = function (el, options) {
            var scroll = this, doc = document, i;

            scroll.dom = typeof el == 'object' ? el : doc.getElementById(el);
            scroll.dom.style.overflow = 'hidden';

            // Default options
            scroll.options = {
                hScroll: true,
                vScroll: true,
                x: 0,
                y: 0,
                bounce: true,
                bounceLock: false,
                momentum: true,
                lockDirection: true,
                useTransform: true,
                useScrollAttribute: false,
                useTransition: false,
                topOffset: 0,
                checkDOMChanges: false,		// Experimental

                // Scrollbar
                hScrollbar: true,
                vScrollbar: true,
                fixedScrollbar: isAndroid,
                hideScrollbar: isIDevice,
                fadeScrollbar: isIDevice && has3d,
                scrollbarClass: '',

                // Zoom
                zoom: false,
                zoomMin: 1,
                zoomMax: 4,
                doubleTapZoom: 2,
                wheelAction: 'scroll',

                // Snap
                snap: false,
                snapThreshold: 1,

                // Events
                onRefresh: null,
                onBeforeScrollStart: function (e) { e.preventDefault(); },
                onScrollStart: null,
                onBeforeScrollMove: null,
                onScrollMove: null,
                onBeforeScrollEnd: null,
                onScrollEnd: null,
                onTouchEnd: null,
                onDestroy: null,
                onZoomStart: null,
                onZoom: null,
                onZoomEnd: null
            };

            // User defined options
            for (i in options) scroll.options[i] = options[i];

            if (options.scrollSize) {
                scroll.scrollSize = options.scrollSize;
            }

            if (options.viewportSize) {
                scroll.viewportSize = options.viewportSize;
            }

            if (options.resumeHelper) {
                scroll.resumeHelper = options.resumeHelper;
            }

            scroll.childStyles = {};
            scroll.uuid = iScroll.uuid++;
            scroll.dom.className += " iScroll" + scroll.uuid;

            // Set starting position
            scroll.x = scroll.options.x;
            scroll.y = scroll.options.y;

            // Normalize options
            scroll.options.useTransform = hasTransform ? scroll.options.useTransform : false;
            scroll.options.hScrollbar = scroll.options.hScroll && scroll.options.hScrollbar;
            scroll.options.vScrollbar = scroll.options.vScroll && scroll.options.vScrollbar;
            scroll.options.zoom = scroll.options.useTransform && scroll.options.zoom;
            scroll.options.useTransition = hasTransitionEnd && scroll.options.useTransition;

            // Helpers FIX ANDROID BUG!
            // translate3d and scale doesn't work together!
            // Ignoring 3d ONLY WHEN YOU SET that.options.zoom
            if ( scroll.options.zoom && isAndroid ){
                trnOpen = 'translate(';
                trnClose = ')';
            }

            // Set some default styles
            var styles = {};
            styles[TransitionDuration] = '0';
            styles[TransformOrigin] = '0 0';
            styles[TransitionProperty] = scroll.options.useTransform ? '-' + vendor.toLowerCase() + '-transform' : 'top left';

            if (scroll.options.useTransition) styles[TransitionTimingFunction] = 'cubic-bezier(0.33,0.66,0.66,1)';

            if (scroll.options.useTransform)
                styles[Transform] = trnOpen + scroll.x + 'px,' + scroll.y + 'px' + trnClose;
            else if (scroll.options.useScrollAttribute) {
                scroll.dom.scrollLeft = scroll.x * -1;
                scroll.dom.scrollTop = scroll.y * -1;
            } else if (scroll.options.stillScroller) {
            } else {
                styles.position = "absolute";
                styles.top = scroll.y + "px";
                styles.left = scroll.x + "px";
            }

            if (scroll.options.useTransition) scroll.options.fixedScrollbar = true;

            scroll.updateChildrenStyle(styles);

            scroll.refresh();

            scroll._bind(RESIZE_EV, window);
            scroll._bind(START_EV);
            if (!hasTouch) {
                scroll._bind('mouseout', scroll.dom);
                if (scroll.options.wheelAction != 'none')
                    scroll._bind(WHEEL_EV);
            }

            if (scroll.options.checkDOMChanges) scroll.checkDOMTime = setInterval(function () {
                scroll._checkDOMChanges();
            }, 500);
        };

    iScroll.getStyleSheet = function() {
        if (!iScroll.stylesheet) {
            var style = document.createElement('style');
            document.getElementsByTagName('head')[0].appendChild(style);
            iScroll.stylesheet = document.styleSheets[document.styleSheets.length - 1];
        }

        return iScroll.stylesheet;
    };

    iScroll.uuid = 1;

    // Prototype
    iScroll.prototype = {
        enabled: true,
        x: 0,
        y: 0,
        steps: [],
        scale: 1,
        currPageX: 0, currPageY: 0,
        pagesX: [], pagesY: [],
        aniTime: null,
        wheelZoomCount: 0,

        updateChildrenStyle2: function(prop, value) {
            var childStyles = this.childStyles;
            if (value === undefined) {
                for (var style in prop) {
                    childStyles[style] = prop[style];
                }
            } else {
                childStyles[prop] = value;
            }

            var styleString = "";
            for (var prop in childStyles) {
                styleString += prop + ":" + childStyles[prop] + ";";
            }

            var stylesheet = iScroll.getStyleSheet();
            if (this.styleIndex) {
                if (vendor == "Moz") {
                    stylesheet.deleteRule(this.styleIndex);
                } else {
                    stylesheet.removeRule(this.styleIndex);
                }
            }

            var styleString = ".iScroll" + this.uuid + " > *:not(.scroll-bar) {" + styleString + "}";
            this.styleIndex = stylesheet.cssRules.length;
            stylesheet.insertRule(styleString, this.styleIndex);
        },

        updateChildrenStyle: function(prop, value) {
            var scroll = this, childStyles = scroll.childStyles;
            var children = scroll.dom.children;
            for (var i = 0, l = children.length; i < l; i++) {
                var child = children[i];
                if (child.nodeType == 3 || child.className.indexOf("scroll-bar") != -1) continue;
                if (value === undefined) {
                    for (var style in prop) {
                        child.style[style] = prop[style];
                    }
                } else {
                    child.style[prop] = value;
                }
            }
        },

        handleEvent: function (e) {
            var scroll = this;
            switch(e.type) {
                case START_EV:
                    if (!hasTouch && e.button !== 0) return;
                    scroll._start(e);
                    break;
                case MOVE_EV: scroll._move(e); break;
                case END_EV:
                case CANCEL_EV: scroll._end(e); break;
                case RESIZE_EV: scroll._resize(); break;
                case WHEEL_EV: scroll._wheel(e); break;
                case 'mouseout': scroll._mouseout(e); break;
                case 'webkitTransitionEnd': scroll._transitionEnd(e); break;
            }
        },

        _checkDOMChanges: function () {
            if (this.moved || this.zoomed || this.animating ||
                (this.scrollWidth == this.scrollSize("h") * this.scale && this.scrollHeight == this.scrollSize("v") * this.scale)) return;

            this.refresh();
        },

        _scrollbar: function (dir) {
            var scroll = this,
                doc = document,
                bar;

            if (!scroll[dir + 'Scrollbar']) {
                if (scroll[dir + 'ScrollbarWrapper']) {
                    if (hasTransform) scroll[dir + 'ScrollbarIndicator'].style[vendor + 'Transform'] = '';
                    scroll[dir + 'ScrollbarWrapper'].parentNode.removeChild(scroll[dir + 'ScrollbarWrapper']);
                    scroll[dir + 'ScrollbarWrapper'] = null;
                    scroll[dir + 'ScrollbarIndicator'] = null;
                }

                return;
            }

            if (!scroll[dir + 'ScrollbarWrapper']) {
                // Create the scrollbar dom
                bar = doc.createElement('div');

                if (scroll.options.scrollbarClass)
                    bar.className = scroll.options.scrollbarClass + dir.toUpperCase();
                else {
                    bar.style.cssText = 'position:absolute;z-index:100;' + (dir == 'h' ? 'height:7px;bottom:1px;left:2px;right:' + (scroll.vScrollbar ? '7' : '2') + 'px' : 'width:7px;bottom:' + (scroll.hScrollbar ? '7' : '2') + 'px;top:2px;right:1px');
                }

                bar.className += " scroll-bar";

                bar.style.cssText += ';pointer-events:none;-' + vendor + '-transition-property:opacity;-' + vendor + '-transition-duration:' + (scroll.options.fadeScrollbar ? '350ms' : '0') + ';overflow:hidden;opacity:' + (scroll.options.hideScrollbar ? '0' : '1');

                scroll.dom.appendChild(bar);
                scroll[dir + 'ScrollbarWrapper'] = bar;

                // Create the scrollbar indicator
                bar = doc.createElement('div');
                if (!scroll.options.scrollbarClass) {
                    bar.style.cssText = 'position:absolute;z-index:100;background:rgba(0,0,0,0.5);border:1px solid rgba(255,255,255,0.9);-' + vendor + '-background-clip:padding-box;-' + vendor + '-box-sizing:border-box;' + (dir == 'h' ? 'height:100%' : 'width:100%') + ';-' + vendor + '-border-radius:3px;border-radius:3px';
                }
                bar.style.cssText += ';pointer-events:none;-' + vendor + '-transition-property:-' + vendor + '-transform;-' + vendor + '-transition-timing-function:cubic-bezier(0.33,0.66,0.66,1);-' + vendor + '-transition-duration:0;-' + vendor + '-transform:' + trnOpen + '0,0' + trnClose;
                if (scroll.options.useTransition) bar.style.cssText += ';-' + vendor + '-transition-timing-function:cubic-bezier(0.33,0.66,0.66,1)';

                scroll[dir + 'ScrollbarWrapper'].appendChild(bar);
                scroll[dir + 'ScrollbarIndicator'] = bar;
            }

            if (dir == 'h') {
                scroll.hScrollbarSize = scroll.hScrollbarWrapper.clientWidth;
                scroll.hScrollbarIndicatorSize = m.max(mround(scroll.hScrollbarSize * scroll.hScrollbarSize / scroll.scrollWidth), 8);
                scroll.hScrollbarIndicator.style.width = scroll.hScrollbarIndicatorSize + 'px';
                scroll.hScrollbarMaxScroll = scroll.hScrollbarSize - scroll.hScrollbarIndicatorSize;
                scroll.hScrollbarProp = scroll.hScrollbarMaxScroll / scroll.maxScrollX;
            } else {
                scroll.vScrollbarSize = scroll.vScrollbarWrapper.clientHeight;
                scroll.vScrollbarIndicatorSize = m.max(mround(scroll.vScrollbarSize * scroll.vScrollbarSize / scroll.scrollHeight), 8);
                scroll.vScrollbarIndicator.style.height = scroll.vScrollbarIndicatorSize + 'px';
                scroll.vScrollbarMaxScroll = scroll.vScrollbarSize - scroll.vScrollbarIndicatorSize;
                scroll.vScrollbarProp = scroll.vScrollbarMaxScroll / scroll.maxScrollY;
            }

            // Reset position
            scroll._scrollbarPos(dir, true);
        },

        _resize: function () {
            var scroll = this;
            setTimeout(function () { scroll.refresh(); }, isAndroid ? 200 : 0);
        },

        _pos: function (x, y) {
            x = this.hScroll ? x : 0;
            y = this.vScroll ? y : 0;

            if (this.options.useTransform) {
                this.updateChildrenStyle(Transform, trnOpen + x + 'px,' + y + 'px' + trnClose + ' scale(' + this.scale + ')');
            } else if (this.options.useScrollAttribute) {
                x = mround(x);
                y = mround(y);
                this.dom.scrollTop = y * -1;
                this.dom.scrollLeft = x * -1;

            } else if (this.options.stillScroller) {
                x = mround(x);
                y = mround(y);
            } else {
                x = mround(x);
                y = mround(y);
                var styles = {
                    left: x + 'px',
                    top: y + 'px'
                };
                this.updateChildrenStyle(styles);
            }

            this.x = x;
            this.y = y;

            this._scrollbarPos('h');
            this._scrollbarPos('v');

            var scroll = this;
            if (scroll.options.stillScroller) {
                scroll['vScrollbarWrapper'].style["height"] = this.dom.clientHeight + "px";
                scroll['vScrollbarWrapper'].style["top"] = this.dom.scrollTop + "px";
                scroll['vScrollbarWrapper'].style["bottom"] = "";
            }

            if (this.options.onScrollMove) this.options.onScrollMove.call(this);
        },

        _scrollbarPos: function (dir, hidden) {
            var scroll = this,
                pos = dir == 'h' ? scroll.x : scroll.y,
                size;

            if (!scroll[dir + 'Scrollbar']) return;

            pos = scroll[dir + 'ScrollbarProp'] * pos;

            if (pos < 0) {
                if (!scroll.options.fixedScrollbar) {
                    size = scroll[dir + 'ScrollbarIndicatorSize'] + mround(pos * 3);
                    if (size < 8) size = 8;
                    scroll[dir + 'ScrollbarIndicator'].style[dir == 'h' ? 'width' : 'height'] = size + 'px';
                }
                pos = 0;
            } else if (pos > scroll[dir + 'ScrollbarMaxScroll']) {
                if (!scroll.options.fixedScrollbar) {
                    size = scroll[dir + 'ScrollbarIndicatorSize'] - mround((pos - scroll[dir + 'ScrollbarMaxScroll']) * 3);
                    if (size < 8) size = 8;
                    scroll[dir + 'ScrollbarIndicator'].style[dir == 'h' ? 'width' : 'height'] = size + 'px';
                    pos = scroll[dir + 'ScrollbarMaxScroll'] + (scroll[dir + 'ScrollbarIndicatorSize'] - size);
                } else {
                    pos = scroll[dir + 'ScrollbarMaxScroll'];
                }
            }

            scroll[dir + 'ScrollbarWrapper'].style[vendor + 'TransitionDelay'] = '0';
            scroll[dir + 'ScrollbarWrapper'].style.opacity = hidden && scroll.options.hideScrollbar ? '0' : '1';
            if (scroll.options.useScrollAttribute) {
                scroll[dir + 'ScrollbarWrapper'].style[vendor + 'Transform'] = trnOpen + (-1 * scroll.x + 'px,' + -1 * scroll.y + 'px') + trnClose;
            }
            scroll[dir + 'ScrollbarIndicator'].style[vendor + 'Transform'] = trnOpen + (dir == 'h' ? pos + 'px,0' : '0,' + pos + 'px') + trnClose;
        },

        scrollSize: function(dir) {
            return dir == "h" ? this.dom.scrollWidth : this.dom.scrollHeight;
        },

        viewportSize: function(dir) {
            return dir == "h" ? this.dom.clientWidth : this.dom.clientHeight;
        },

        _start: function (e) {
            var scroll = this,
                point = hasTouch ? e.touches[0] : e,
                matrix, x, y,
                c1, c2;

            if (!scroll.enabled) return;

            if (scroll.options.onBeforeScrollStart) scroll.options.onBeforeScrollStart.call(scroll, e);

            if (scroll.options.useTransition || scroll.options.zoom) scroll._transitionTime(0);

            scroll.moved = false;
            scroll.animating = false;
            scroll.zoomed = false;
            scroll.distX = 0;
            scroll.distY = 0;
            scroll.absDistX = 0;
            scroll.absDistY = 0;
            scroll.dirX = 0;
            scroll.dirY = 0;

            // Gesture start
            if (scroll.options.zoom && hasTouch && e.touches.length > 1) {
                c1 = m.abs(e.touches[0].pageX-e.touches[1].pageX);
                c2 = m.abs(e.touches[0].pageY-e.touches[1].pageY);
                scroll.touchesDistStart = m.sqrt(c1 * c1 + c2 * c2);

                scroll.originX = m.abs(e.touches[0].pageX + e.touches[1].pageX - scroll.domOffsetLeft * 2) / 2 - scroll.x;
                scroll.originY = m.abs(e.touches[0].pageY + e.touches[1].pageY - scroll.domOffsetTop * 2) / 2 - scroll.y;

                if (scroll.options.onZoomStart) scroll.options.onZoomStart.call(scroll, e);
            }

            var firstChild = scroll.dom.firstChild;

            while(firstChild && firstChild.nodeType != 1) {
                firstChild = firstChild.nextSibling;
            }

            if (scroll.options.momentum && firstChild) {
                if (scroll.options.useTransform) {
                    // Very lame general purpose alternative to CSSMatrix
                    matrix = getComputedStyle(firstChild, null)[vendor + 'Transform'].replace(/[^0-9-.,]/g, '').split(',');
                    x = matrix[4] * 1;
                    y = matrix[5] * 1;
                } else if (scroll.options.useScrollAttribute) {
                    x = scroll.dom.scrollLeft * -1;
                    y = scroll.dom.scrollTop * -1;
                } else if (scroll.options.stillScroller) {
                    if (scroll.resumeHelper) {
                        var pos = scroll.resumeHelper();
                        //console.log("x:" + pos.x + "\ty:" + pos.y);
                        x = pos.x;
                        y = pos.y;
                    }
                } else {
                    x = getComputedStyle(firstChild, null).left.replace(/[^0-9-]/g, '') * 1;
                    y = getComputedStyle(firstChild, null).top.replace(/[^0-9-]/g, '') * 1;
                }

                if (x != scroll.x || y != scroll.y) {
                    if (scroll.options.useTransition)
                        scroll._unbind('webkitTransitionEnd');
                    else
                        cancelFrame(scroll.aniTime);
                    scroll.steps = [];
                    scroll._pos(x, y);
                }
            }

            scroll.absStartX = scroll.x;	// Needed by snap threshold
            scroll.absStartY = scroll.y;

            scroll.startX = scroll.x;
            scroll.startY = scroll.y;
            scroll.pointX = point.pageX;
            scroll.pointY = point.pageY;

            scroll.startTime = e.timeStamp || Date.now();

            if (scroll.options.onScrollStart) scroll.options.onScrollStart.call(scroll, e);

            scroll._bind(MOVE_EV);
            scroll._bind(END_EV);
            scroll._bind(CANCEL_EV);
        },

        _move: function (e) {
            var scroll = this,
                point = hasTouch ? e.touches[0] : e,
                deltaX = point.pageX - scroll.pointX,
                deltaY = point.pageY - scroll.pointY,
                newX = scroll.x + deltaX,
                newY = scroll.y + deltaY,
                c1, c2, scale,
                timestamp = e.timeStamp || Date.now();

            if (scroll.options.onBeforeScrollMove) scroll.options.onBeforeScrollMove.call(scroll, e);

            // Zoom
            if (scroll.options.zoom && hasTouch && e.touches.length > 1) {
                c1 = m.abs(e.touches[0].pageX - e.touches[1].pageX);
                c2 = m.abs(e.touches[0].pageY - e.touches[1].pageY);
                scroll.touchesDist = m.sqrt(c1*c1+c2*c2);

                scroll.zoomed = true;

                scale = 1 / scroll.touchesDistStart * scroll.touchesDist * this.scale;

                if (scale < scroll.options.zoomMin)
                    scale = 0.5 * scroll.options.zoomMin * Math.pow(2.0, scale / scroll.options.zoomMin);
                else if (scale > scroll.options.zoomMax)
                    scale = 2.0 * scroll.options.zoomMax * Math.pow(0.5, scroll.options.zoomMax / scale);

                scroll.lastScale = scale / this.scale;

                newX = this.originX - this.originX * scroll.lastScale + this.x,
                    newY = this.originY - this.originY * scroll.lastScale + this.y;


                this.updateChildrenStyle(Transform, trnOpen + newX + 'px,' + newY + 'px' + trnClose + ' scale(' + scale + ')');

                if (scroll.options.onZoom) scroll.options.onZoom.call(scroll, e);
                return;
            }

            scroll.pointX = point.pageX;
            scroll.pointY = point.pageY;

            // Slow down if outside of the boundaries
            if (newX > 0 || newX < scroll.maxScrollX) {
                newX = scroll.options.bounce ? scroll.x + (deltaX / 2) : newX >= 0 || scroll.maxScrollX >= 0 ? 0 : scroll.maxScrollX;
            }
            if (newY > scroll.minScrollY || newY < scroll.maxScrollY) {
                newY = scroll.options.bounce ? scroll.y + (deltaY / 2) : newY >= scroll.minScrollY || scroll.maxScrollY >= 0 ? scroll.minScrollY : scroll.maxScrollY;
            }

            scroll.distX += deltaX;
            scroll.distY += deltaY;
            scroll.absDistX = m.abs(scroll.distX);
            scroll.absDistY = m.abs(scroll.distY);

            if (scroll.absDistX < 6 && scroll.absDistY < 6) {
                return;
            }

            // Lock direction
            if (scroll.options.lockDirection) {
                if (scroll.absDistX > scroll.absDistY + 5) {
                    newY = scroll.y;
                    deltaY = 0;
                } else if (scroll.absDistY > scroll.absDistX + 5) {
                    newX = scroll.x;
                    deltaX = 0;
                }
            }

            scroll.moved = true;
            scroll._pos(newX, newY);
            scroll.dirX = deltaX > 0 ? -1 : deltaX < 0 ? 1 : 0;
            scroll.dirY = deltaY > 0 ? -1 : deltaY < 0 ? 1 : 0;

            if (timestamp - scroll.startTime > 300) {
                scroll.startTime = timestamp;
                scroll.startX = scroll.x;
                scroll.startY = scroll.y;
            }
        },

        _end: function (e) {
            if (hasTouch && e.touches.length != 0) return;

            var scroll = this,
                point = hasTouch ? e.changedTouches[0] : e,
                target, ev,
                momentumX = { dist:0, time:0 },
                momentumY = { dist:0, time:0 },
                duration = (e.timeStamp || Date.now()) - scroll.startTime,
                newPosX = scroll.x,
                newPosY = scroll.y,
                distX, distY,
                newDuration,
                snap,
                scale;

            scroll._unbind(MOVE_EV);
            scroll._unbind(END_EV);
            scroll._unbind(CANCEL_EV);

            if (scroll.options.onBeforeScrollEnd) scroll.options.onBeforeScrollEnd.call(scroll, e);

            if (scroll.zoomed) {
                scale = scroll.scale * scroll.lastScale;
                scale = Math.max(scroll.options.zoomMin, scale);
                scale = Math.min(scroll.options.zoomMax, scale);
                scroll.lastScale = scale / scroll.scale;
                scroll.scale = scale;

                scroll.x = scroll.originX - scroll.originX * scroll.lastScale + scroll.x;
                scroll.y = scroll.originY - scroll.originY * scroll.lastScale + scroll.y;

                var styles = {};
                styles[TransitionDuration] = '200ms';
                styles[Transform] = trnOpen + scroll.x + 'px,' + scroll.y + 'px' + trnClose + ' scale(' + scroll.scale + ')';
                scroll.updateChildrenStyle(styles);

                scroll.zoomed = false;
                scroll.refresh();

                if (scroll.options.onZoomEnd) scroll.options.onZoomEnd.call(scroll, e);
                return;
            }

            if (!scroll.moved) {
                if (hasTouch) {
                    if (scroll.doubleTapTimer && scroll.options.zoom) {
                        // Double tapped
                        clearTimeout(scroll.doubleTapTimer);
                        scroll.doubleTapTimer = null;
                        if (scroll.options.onZoomStart) scroll.options.onZoomStart.call(scroll, e);
                        scroll.zoom(scroll.pointX, scroll.pointY, scroll.scale == 1 ? scroll.options.doubleTapZoom : 1);
                        if (scroll.options.onZoomEnd) {
                            setTimeout(function() {
                                scroll.options.onZoomEnd.call(scroll, e);
                            }, 200); // 200 is default zoom duration
                        }
                    } else {
                        scroll.doubleTapTimer = setTimeout(function () {
                            scroll.doubleTapTimer = null;

                            // Find the last touched element
                            target = point.target;
                            while (target.nodeType != 1) target = target.parentNode;

                            if (target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA') {
                                ev = document.createEvent('MouseEvents');
                                ev.initMouseEvent('click', true, true, e.view, 1,
                                    point.screenX, point.screenY, point.clientX, point.clientY,
                                    e.ctrlKey, e.altKey, e.shiftKey, e.metaKey,
                                    0, null);
                                ev._fake = true;
                                target.dispatchEvent(ev);
                            }
                        }, scroll.options.zoom ? 250 : 0);
                    }
                }

                scroll._resetPos(200);

                if (scroll.options.onTouchEnd) scroll.options.onTouchEnd.call(scroll, e);
                return;
            }

            if (duration < 300 && scroll.options.momentum) {
                momentumX = newPosX ? scroll._momentum(newPosX - scroll.startX, duration, -scroll.x, scroll.scrollWidth - scroll.viewWidth + scroll.x, scroll.options.bounce ? scroll.viewWidth : 0) : momentumX;
                momentumY = newPosY ? scroll._momentum(newPosY - scroll.startY, duration, -scroll.y, (scroll.maxScrollY < 0 ? scroll.scrollHeight - scroll.viewHeight + scroll.y - scroll.minScrollY : 0), scroll.options.bounce ? scroll.viewHeight : 0) : momentumY;

                newPosX = scroll.x + momentumX.dist;
                newPosY = scroll.y + momentumY.dist;

                if ((scroll.x > 0 && newPosX > 0) || (scroll.x < scroll.maxScrollX && newPosX < scroll.maxScrollX)) momentumX = { dist:0, time:0 };
                if ((scroll.y > scroll.minScrollY && newPosY > scroll.minScrollY) || (scroll.y < scroll.maxScrollY && newPosY < scroll.maxScrollY)) momentumY = { dist:0, time:0 };
            }

            if (momentumX.dist || momentumY.dist) {
                newDuration = m.max(m.max(momentumX.time, momentumY.time), 10);

                // Do we need to snap?
                if (scroll.options.snap) {
                    distX = newPosX - scroll.absStartX;
                    distY = newPosY - scroll.absStartY;
                    if (m.abs(distX) < scroll.options.snapThreshold && m.abs(distY) < scroll.options.snapThreshold) { scroll.scrollTo(scroll.absStartX, scroll.absStartY, 200); }
                    else {
                        snap = scroll._snap(newPosX, newPosY);
                        newPosX = snap.x;
                        newPosY = snap.y;
                        newDuration = m.max(snap.time, newDuration);
                    }
                }

                scroll.scrollTo(mround(newPosX), mround(newPosY), newDuration);

                if (scroll.options.onTouchEnd) scroll.options.onTouchEnd.call(scroll, e);
                return;
            }

            // Do we need to snap?
            if (scroll.options.snap) {
                distX = newPosX - scroll.absStartX;
                distY = newPosY - scroll.absStartY;
                if (m.abs(distX) < scroll.options.snapThreshold && m.abs(distY) < scroll.options.snapThreshold) scroll.scrollTo(scroll.absStartX, scroll.absStartY, 200);
                else {
                    snap = scroll._snap(scroll.x, scroll.y);
                    if (snap.x != scroll.x || snap.y != scroll.y) scroll.scrollTo(snap.x, snap.y, snap.time);
                }

                if (scroll.options.onTouchEnd) scroll.options.onTouchEnd.call(scroll, e);
                return;
            }

            scroll._resetPos(200);
            if (scroll.options.onTouchEnd) scroll.options.onTouchEnd.call(scroll, e);
        },

        _resetPos: function (time) {
            var scroll = this,
                resetX = scroll.x >= 0 ? 0 : scroll.x < scroll.maxScrollX ? scroll.maxScrollX : scroll.x,
                resetY = scroll.y >= scroll.minScrollY || scroll.maxScrollY > 0 ? scroll.minScrollY : scroll.y < scroll.maxScrollY ? scroll.maxScrollY : scroll.y;

            if (resetX == scroll.x && resetY == scroll.y) {
                if (scroll.moved) {
                    scroll.moved = false;
                    if (scroll.options.onScrollEnd) scroll.options.onScrollEnd.call(scroll);		// Execute custom code on scroll end
                }

                if (scroll.hScrollbar && scroll.options.hideScrollbar) {
                    if (vendor == 'webkit') scroll.hScrollbarWrapper.style[vendor + 'TransitionDelay'] = '300ms';
                    scroll.hScrollbarWrapper.style.opacity = '0';
                }
                if (scroll.vScrollbar && scroll.options.hideScrollbar) {
                    if (vendor == 'webkit') scroll.vScrollbarWrapper.style[vendor + 'TransitionDelay'] = '300ms';
                    scroll.vScrollbarWrapper.style.opacity = '0';
                }

                return;
            }

            scroll.scrollTo(resetX, resetY, time || 0);
        },

        _wheel: function (e) {
            var scroll = this,
                wheelDeltaX, wheelDeltaY,
                deltaX, deltaY,
                deltaScale;

            if ('wheelDeltaX' in e) {
                wheelDeltaX = e.wheelDeltaX / 12;
                wheelDeltaY = e.wheelDeltaY / 12;
            } else if('wheelDelta' in e) {
                wheelDeltaX = wheelDeltaY = e.wheelDelta / 12;
            } else if ('detail' in e) {
                wheelDeltaX = wheelDeltaY = -e.detail * 3;
            } else {
                return;
            }

            if (scroll.options.wheelAction == 'zoom') {
                deltaScale = scroll.scale * Math.pow(2, 1/3 * (wheelDeltaY ? wheelDeltaY / Math.abs(wheelDeltaY) : 0));
                if (deltaScale < scroll.options.zoomMin) deltaScale = scroll.options.zoomMin;
                if (deltaScale > scroll.options.zoomMax) deltaScale = scroll.options.zoomMax;

                if (deltaScale != scroll.scale) {
                    if (!scroll.wheelZoomCount && scroll.options.onZoomStart) scroll.options.onZoomStart.call(scroll, e);
                    scroll.wheelZoomCount++;

                    scroll.zoom(e.pageX, e.pageY, deltaScale, 400);

                    setTimeout(function() {
                        scroll.wheelZoomCount--;
                        if (!scroll.wheelZoomCount && scroll.options.onZoomEnd) scroll.options.onZoomEnd.call(scroll, e);
                    }, 400);
                }

                return;
            }

            deltaX = scroll.x + wheelDeltaX;
            deltaY = scroll.y + wheelDeltaY;

            if (deltaX > 0) deltaX = 0;
            else if (deltaX < scroll.maxScrollX) deltaX = scroll.maxScrollX;

            if (deltaY > scroll.minScrollY) deltaY = scroll.minScrollY;
            else if (deltaY < scroll.maxScrollY) deltaY = scroll.maxScrollY;

            scroll.scrollTo(deltaX, deltaY, 0);
        },

        _mouseout: function (e) {
            var t = e.relatedTarget;

            if (!t) {
                this._end(e);
                return;
            }

            while (t = t.parentNode) if (t == this.dom) return;

            this._end(e);
        },

        _transitionEnd: function (e) {
            var scroll = this;

            if (e.target != scroll.dom) return;

            scroll._unbind('webkitTransitionEnd');

            scroll._startAni();
        },


        /**
         *
         * Utilities
         *
         */
        _startAni: function () {
            var scroll = this,
                startX = scroll.x, startY = scroll.y,
                startTime = Date.now(),
                step, easeOut,
                animate;

            if (scroll.animating) return;

            if (!scroll.steps.length) {
                scroll._resetPos(400);
                return;
            }

            step = scroll.steps.shift();

            if (step.x == startX && step.y == startY) step.time = 0;

            scroll.animating = true;
            scroll.moved = true;

            if (scroll.options.useTransition) {
                scroll._transitionTime(step.time);
                scroll._pos(step.x, step.y);
                scroll.animating = false;
                if (step.time) scroll._bind('webkitTransitionEnd');
                else scroll._resetPos(0);
                return;
            }

            animate = function () {
                var now = Date.now(),
                    newX, newY;

                if (now >= startTime + step.time) {
                    scroll._pos(step.x, step.y);
                    scroll.animating = false;
                    if (scroll.options.onAnimationEnd) scroll.options.onAnimationEnd.call(scroll);			// Execute custom code on animation end
                    scroll._startAni();
                    return;
                }

                now = (now - startTime) / step.time - 1;
                easeOut = m.sqrt(1 - now * now);
                newX = (step.x - startX) * easeOut + startX;
                newY = (step.y - startY) * easeOut + startY;
                scroll._pos(newX, newY);
                if (scroll.animating) scroll.aniTime = nextFrame(animate);
            };

            animate();
        },

        _transitionTime: function (time) {
            time += 'ms';
            this.updateChildrenStyle(TransitionDuration, time);
            if (this.hScrollbar) this.hScrollbarIndicator.style[vendor + 'TransitionDuration'] = time;
            if (this.vScrollbar) this.vScrollbarIndicator.style[vendor + 'TransitionDuration'] = time;
        },

        _momentum: function (dist, time, maxDistUpper, maxDistLower, size) {
            var deceleration = 0.0006,
                speed = m.abs(dist) / time,
                newDist = (speed * speed) / (2 * deceleration),
                newTime = 0, outsideDist = 0;

            // Proportinally reduce speed if we are outside of the boundaries
            if (dist > 0 && newDist > maxDistUpper) {
                outsideDist = size / (6 / (newDist / speed * deceleration));
                maxDistUpper = maxDistUpper + outsideDist;
                speed = speed * maxDistUpper / newDist;
                newDist = maxDistUpper;
            } else if (dist < 0 && newDist > maxDistLower) {
                outsideDist = size / (6 / (newDist / speed * deceleration));
                maxDistLower = maxDistLower + outsideDist;
                speed = speed * maxDistLower / newDist;
                newDist = maxDistLower;
            }

            newDist = newDist * (dist < 0 ? -1 : 1);
            newTime = speed / deceleration;

            return { dist: newDist, time: mround(newTime) };
        },

        _offset: function (el) {
            var left = -el.offsetLeft,
                top = -el.offsetTop;

            while (el = el.offsetParent) {
                left -= el.offsetLeft;
                top -= el.offsetTop;
            }

            if (el != this.dom) {
                left *= this.scale;
                top *= this.scale;
            }

            return { left: left, top: top };
        },

        _snap: function (x, y) {
            var scroll = this, i, l, page, time, sizeX, sizeY;

            // Check page X
            page = scroll.pagesX.length - 1;
            for (i=0, l=scroll.pagesX.length; i<l; i++) {
                if (x >= scroll.pagesX[i]) {
                    page = i;
                    break;
                }
            }
            if (page == scroll.currPageX && page > 0 && scroll.dirX < 0) page--;
            x = scroll.pagesX[page];
            sizeX = m.abs(x - scroll.pagesX[scroll.currPageX]);
            sizeX = sizeX ? m.abs(scroll.x - x) / sizeX * 500 : 0;
            scroll.currPageX = page;

            // Check page Y
            page = scroll.pagesY.length-1;
            for (i=0; i<page; i++) {
                if (y >= scroll.pagesY[i]) {
                    page = i;
                    break;
                }
            }
            if (page == scroll.currPageY && page > 0 && scroll.dirY < 0) page--;
            y = scroll.pagesY[page];
            sizeY = m.abs(y - scroll.pagesY[scroll.currPageY]);
            sizeY = sizeY ? m.abs(scroll.y - y) / sizeY * 500 : 0;
            scroll.currPageY = page;

            // Snap with constant speed (proportional duration)
            time = mround(m.max(sizeX, sizeY)) || 200;

            return { x: x, y: y, time: time };
        },

        _bind: function (type, el, bubble) {
            (el || this.dom).addEventListener(type, this, !!bubble);
        },

        _unbind: function (type, el, bubble) {
            (el || this.dom).removeEventListener(type, this, !!bubble);
        },


        /**
         *
         * Public methods
         *
         */
        destroy: function () {
            var scroll = this;

            scroll.updateChildrenStyle(Transform, '');

            // Remove the scrollbars
            scroll.hScrollbar = false;
            scroll.vScrollbar = false;
            scroll._scrollbar('h');
            scroll._scrollbar('v');

            // Remove the event listeners
            scroll._unbind(RESIZE_EV, window);
            scroll._unbind(START_EV);
            scroll._unbind(MOVE_EV);
            scroll._unbind(END_EV);
            scroll._unbind(CANCEL_EV);

            if (!scroll.options.hasTouch) {
                scroll._unbind('mouseout', scroll.dom);
                scroll._unbind(WHEEL_EV);
            }

            if (scroll.options.useTransition) scroll._unbind('webkitTransitionEnd');

            if (scroll.options.checkDOMChanges) clearInterval(scroll.checkDOMTime);

            if (scroll.options.onDestroy) scroll.options.onDestroy.call(scroll);
        },

        refresh: function () {
            var scroll = this, offset, i, l, els, pos = 0, page = 0;

            if (scroll.scale < scroll.options.zoomMin) scroll.scale = scroll.options.zoomMin;
            scroll.viewWidth = scroll.viewportSize("h") || 1;
            scroll.viewHeight = scroll.viewportSize("v") || 1;

            scroll.minScrollY = -scroll.options.topOffset || 0;
            scroll.scrollWidth = mround(scroll.scrollSize("h") * scroll.scale);
            scroll.scrollHeight = mround((scroll.scrollSize("v") + scroll.minScrollY) * scroll.scale);
            scroll.maxScrollX = scroll.viewWidth - scroll.scrollWidth;
            scroll.maxScrollY = scroll.viewHeight - scroll.scrollHeight + scroll.minScrollY;
            scroll.dirX = 0;
            scroll.dirY = 0;

            if (scroll.options.onRefresh) scroll.options.onRefresh.call(scroll);

            scroll.hScroll = scroll.options.hScroll && scroll.maxScrollX < 0;
            scroll.vScroll = scroll.options.vScroll && (!scroll.options.bounceLock && !scroll.hScroll || scroll.scrollHeight > scroll.viewHeight);

            scroll.hScrollbar = scroll.hScroll && scroll.options.hScrollbar;
            scroll.vScrollbar = scroll.vScroll && scroll.options.vScrollbar && scroll.scrollHeight > scroll.viewHeight;

            offset = scroll._offset(scroll.dom);
            scroll.domOffsetLeft = -offset.left;
            scroll.domOffsetTop = -offset.top;

            // Prepare snap
            if (typeof scroll.options.snap == 'string') {
                scroll.pagesX = [];
                scroll.pagesY = [];
                els = scroll.dom.querySelectorAll(scroll.options.snap);
                for (i=0, l=els.length; i<l; i++) {
                    pos = scroll._offset(els[i]);
                    pos.left += scroll.domOffsetLeft;
                    pos.top += scroll.domOffsetTop;
                    scroll.pagesX[i] = pos.left < scroll.maxScrollX ? scroll.maxScrollX : pos.left * scroll.scale;
                    scroll.pagesY[i] = pos.top < scroll.maxScrollY ? scroll.maxScrollY : pos.top * scroll.scale;
                }
            } else if (scroll.options.snap) {
                scroll.pagesX = [];
                while (pos >= scroll.maxScrollX) {
                    scroll.pagesX[page] = pos;
                    pos = pos - scroll.viewWidth;
                    page++;
                }
                if (scroll.maxScrollX%scroll.viewWidth)
                    scroll.pagesX[scroll.pagesX.length] = scroll.maxScrollX - scroll.pagesX[scroll.pagesX.length-1] + scroll.pagesX[scroll.pagesX.length-1];

                pos = 0;
                page = 0;
                scroll.pagesY = [];
                while (pos >= scroll.maxScrollY) {
                    scroll.pagesY[page] = pos;
                    pos = pos - scroll.viewHeight;
                    page++;
                }
                if (scroll.maxScrollY%scroll.viewHeight)
                    scroll.pagesY[scroll.pagesY.length] = scroll.maxScrollY - scroll.pagesY[scroll.pagesY.length-1] + scroll.pagesY[scroll.pagesY.length-1];
            }

            // Prepare the scrollbars
            scroll._scrollbar('h');
            scroll._scrollbar('v');

            if (!scroll.zoomed) {
                scroll.updateChildrenStyle(TransitionDuration, '0');
                scroll._resetPos(200);
            }
        },

        scrollTo: function (x, y, time, relative) {
            var scroll = this, step = x, i, l;

            scroll.stop();

            if (!step.length) step = [{ x: x, y: y, time: time, relative: relative }];

            for (i=0, l=step.length; i<l; i++) {
                if (step[i].relative) { step[i].x = scroll.x - step[i].x; step[i].y = scroll.y - step[i].y; }
                scroll.steps.push({ x: step[i].x, y: step[i].y, time: step[i].time || 0 });
            }

            scroll._startAni();
        },

        scrollToElement: function (el, time) {
            var scroll = this, pos;
            el = el.nodeType ? el : scroll.dom.querySelector(el);
            if (!el) return;

            pos = scroll._offset(el);
            pos.left += scroll.domOffsetLeft;
            pos.top += scroll.domOffsetTop;

            pos.left = pos.left > 0 ? 0 : pos.left < scroll.maxScrollX ? scroll.maxScrollX : pos.left;
            pos.top = pos.top > scroll.minScrollY ? scroll.minScrollY : pos.top < scroll.maxScrollY ? scroll.maxScrollY : pos.top;
            time = time === undefined ? m.max(m.abs(pos.left)*2, m.abs(pos.top)*2) : time;

            scroll.scrollTo(pos.left, pos.top, time);
        },

        scrollToPage: function (pageX, pageY, time) {
            var scroll = this, x, y;

            time = time === undefined ? 400 : time;

            if (scroll.options.onScrollStart) scroll.options.onScrollStart.call(scroll);

            if (scroll.options.snap) {
                pageX = pageX == 'next' ? scroll.currPageX+1 : pageX == 'prev' ? scroll.currPageX-1 : pageX;
                pageY = pageY == 'next' ? scroll.currPageY+1 : pageY == 'prev' ? scroll.currPageY-1 : pageY;

                pageX = pageX < 0 ? 0 : pageX > scroll.pagesX.length-1 ? scroll.pagesX.length-1 : pageX;
                pageY = pageY < 0 ? 0 : pageY > scroll.pagesY.length-1 ? scroll.pagesY.length-1 : pageY;

                scroll.currPageX = pageX;
                scroll.currPageY = pageY;
                x = scroll.pagesX[pageX];
                y = scroll.pagesY[pageY];
            } else {
                x = -scroll.viewWidth * pageX;
                y = -scroll.viewHeight * pageY;
                if (x < scroll.maxScrollX) x = scroll.maxScrollX;
                if (y < scroll.maxScrollY) y = scroll.maxScrollY;
            }

            scroll.scrollTo(x, y, time);
        },

        disable: function () {
            this.stop();
            this._resetPos(0);
            this.enabled = false;

            // If disabled after touchstart we make sure that there are no left over events
            this._unbind(MOVE_EV);
            this._unbind(END_EV);
            this._unbind(CANCEL_EV);
        },

        enable: function () {
            this.enabled = true;
        },

        stop: function () {
            if (this.options.useTransition) this._unbind('webkitTransitionEnd');
            else cancelFrame(this.aniTime);
            this.steps = [];
            this.moved = false;
            this.animating = false;
        },

        zoom: function (x, y, scale, time) {
            var scroll = this,
                relScale = scale / scroll.scale;

            if (!scroll.options.useTransform) return;

            scroll.zoomed = true;
            time = time === undefined ? 200 : time;
            x = x - scroll.domOffsetLeft - scroll.x;
            y = y - scroll.domOffsetTop - scroll.y;
            scroll.x = x - x * relScale + scroll.x;
            scroll.y = y - y * relScale + scroll.y;

            scroll.scale = scale;
            scroll.refresh();

            scroll.x = scroll.x > 0 ? 0 : scroll.x < scroll.maxScrollX ? scroll.maxScrollX : scroll.x;
            scroll.y = scroll.y > scroll.minScrollY ? scroll.minScrollY : scroll.y < scroll.maxScrollY ? scroll.maxScrollY : scroll.y;
            var styles = {};
            styles[TransitionDuration] = time + 'ms';
            styles[Transform] = trnOpen + scroll.x + 'px,' + scroll.y + 'px' + trnClose + ' scale(' + scale + ')';
            scroll.updateChildrenStyle(styles);

            scroll.zoomed = false;
        },

        isReady: function () {
            return !this.moved && !this.zoomed && !this.animating;
        }
    };

    if (typeof exports !== 'undefined')
        exports.iScroll = iScroll;
    else
        window.iScroll = iScroll;

})();
