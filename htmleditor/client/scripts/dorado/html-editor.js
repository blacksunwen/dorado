/**
 * Copyright (c) 1982, 1986, 1990, 1991, 1993
 *      The Regents of the University of California.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *      This product includes software developed by the University of
 *      California, Berkeley and its contributors.
 * 4. Neither the name of the University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
UEDITOR_CONFIG = window.UEDITOR_CONFIG || {};
var baidu = baidu || {};
baidu.editor = baidu.editor || {};
baidu.editor.commands = {};
baidu.editor.plugins = {};
baidu.editor.browser = function() {
    var agent = navigator.userAgent.toLowerCase(),opera = window.opera,browser = {ie:!!window.ActiveXObject,opera:(!!opera && opera.version),webkit:(agent.indexOf(" applewebkit/") > -1),air:(agent.indexOf(" adobeair/") > -1),mac:(agent.indexOf("macintosh") > -1),quirks:(document.compatMode == "BackCompat")};
    browser.gecko = (navigator.product == "Gecko" && !browser.webkit && !browser.opera);
    var version = 0;
    if (browser.ie) {
        version = parseFloat(agent.match(/msie (\d+)/)[1]);
        browser.ie8 = !!document.documentMode;
        browser.ie8Compat = document.documentMode == 8;
        browser.ie7Compat = ((version == 7 && !document.documentMode) || document.documentMode == 7);
        browser.ie6Compat = (version < 7 || browser.quirks);
    }
    if (browser.gecko) {
        var geckoRelease = agent.match(/rv:([\d\.]+)/);
        if (geckoRelease) {
            geckoRelease = geckoRelease[1].split(".");
            version = geckoRelease[0] * 10000 + (geckoRelease[1] || 0) * 100 + (geckoRelease[2] || 0) * 1;
        }
    }
    if (/chrome\/(\d+\.\d)/i.test(agent)) {
        browser.chrome = +RegExp["$1"];
    }
    if (/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(agent) && !/chrome/i.test(agent)) {
        browser.safari = +(RegExp["$1"] || RegExp["$2"]);
    }
    if (browser.opera) {
        version = parseFloat(opera.version());
    }
    if (browser.webkit) {
        version = parseFloat(agent.match(/ applewebkit\/(\d+)/)[1]);
    }
    browser.version = version;
    browser.isCompatible = !browser.mobile && ((browser.ie && version >= 6) || (browser.gecko && version >= 10801) || (browser.opera && version >= 9.5) || (browser.air && version >= 1) || (browser.webkit && version >= 522) || false);
    return browser;
}();
(function() {
    baidu.editor.utils = {};
    var noop = new Function();
    var utils = baidu.editor.utils = {makeInstance:function(obj) {
        noop.prototype = obj;
        obj = new noop;
        noop.prototype = null;
        return obj;
    },isArray:function(array) {
        return array && array.constructor === Array;
    },isString:function(str) {
        return typeof str == "string" || str.constructor == String;
    },each:function(eachable, iterator, this_) {
        if (utils.isArray(eachable)) {
            for (var i = 0; i < eachable.length; i++) {
                iterator.call(this_, eachable[i], i, eachable);
            }
        } else {
            for (var k in eachable) {
                iterator.call(this_, eachable[k], k, eachable);
            }
        }
    },inherits:function(subClass, superClass) {
        var oldP = subClass.prototype;
        var newP = utils.makeInstance(superClass.prototype);
        utils.extend(newP, oldP, true);
        subClass.prototype = newP;
        return (newP.constructor = subClass);
    },bind:function(fn, this_) {
        return function() {
            return fn.apply(this_, arguments);
        };
    },defer:function(fn, delay, exclusion) {
        var timerID;
        return function() {
            if (exclusion) {
                clearTimeout(timerID);
            }
            timerID = setTimeout(fn, delay);
        };
    },extend:function(t, s, b) {
        if (s) {
            for (var k in s) {
                if (!b || !t.hasOwnProperty(k)) {
                    t[k] = s[k];
                }
            }
        }
        return t;
    },indexOf:function(array, item, at) {
        at = at || 0;
        while (at < array.length) {
            if (array[at] === item) {
                return at;
            }
            at++;
        }
        return -1;
    },removeItem:function(array, item) {
        var k = array.length;
        if (k) {
            while (k--) {
                if (array[k] === item) {
                    array.splice(k, 1);
                    break;
                }
            }
        }
    },trim:function() {
        var trimRegex = /(^[ \t\n\r]+)|([ \t\n\r]+$)/g;
        return function(str) {
            return str.replace(trimRegex, "");
        };
    }(),listToMap:function(list) {
        if (!list) {
            return {};
        }
        var array = list.split(/,/g),k = array.length,map = {};
        if (k) {
            while (k--) {
                map[array[k]] = 1;
            }
        }
        return map;
    },unhtml:function() {
        var map = {"<":"&lt;","&":"&amp","\"":"&quot;",">":"&gt;"};

        function rep(m) {
            return map[m];
        }

        return function(str) {
            return str ? str.replace(/[&<">]/g, rep) : "";
        };
    }(),cssStyleToDomStyle:function() {
        var test = document.createElement("div").style,cssFloat = test.cssFloat != undefined ? "cssFloat" : test.styleFloat != undefined ? "styleFloat" : "float",cache = {"float":cssFloat};

        function replacer(match) {
            return match.charAt(1).toUpperCase();
        }

        return function(cssName) {
            return cache[cssName] || (cache[cssName] = cssName.toLowerCase().replace(/-./g, replacer));
        };
    }()};
})();
(function() {
    baidu.editor.EventBase = EventBase;
    var utils = baidu.editor.utils;

    function EventBase() {
    }

    EventBase.prototype = {addListener:function(type, listener) {
        getListener(this, type, true).push(listener);
    },removeListener:function(type, listener) {
        var listeners = getListener(this, type);
        listeners && utils.removeItem(listeners, listener);
    },fireEvent:function(type) {
        var listeners = getListener(this, type),r,t,k;
        if (listeners) {
            k = listeners.length;
            while (k--) {
                t = listeners[k].apply(this, arguments);
                if (t !== undefined) {
                    r = t;
                }
            }
        }
        if (t = this["on" + type.toLowerCase()]) {
            r = t.apply(this, arguments);
        }
        return r;
    }};
    function getListener(obj, type, force) {
        var allListeners;
        type = type.toLowerCase();
        return ((allListeners = (obj.__allListeners || force && (obj.__allListeners = {}))) && (allListeners[type] || force && (allListeners[type] = [])));
    }
})();
baidu.editor.dom = baidu.editor.dom || {};
baidu.editor.dom.dtd = (function() {
    function _(s) {
        for (var k in s) {
            s[k.toUpperCase()] = s[k];
        }
        return s;
    }

    function X(t) {
        var a = arguments;
        for (var i = 1; i < a.length; i++) {
            var x = a[i];
            for (var k in x) {
                if (!t.hasOwnProperty(k)) {
                    t[k] = x[k];
                }
            }
        }
        return t;
    }

    var A = _({isindex:1,fieldset:1}),B = _({input:1,button:1,select:1,textarea:1,label:1}),C = X(_({a:1}), B),D = X({iframe:1}, C),E = _({hr:1,ul:1,menu:1,div:1,blockquote:1,noscript:1,table:1,center:1,address:1,dir:1,pre:1,h5:1,dl:1,h4:1,noframes:1,h6:1,ol:1,h1:1,h3:1,h2:1}),F = _({ins:1,del:1,script:1,style:1}),G = X(_({b:1,acronym:1,bdo:1,"var":1,"#":1,abbr:1,code:1,br:1,i:1,cite:1,kbd:1,u:1,strike:1,s:1,tt:1,strong:1,q:1,samp:1,em:1,dfn:1,span:1}), F),H = X(_({sub:1,img:1,embed:1,object:1,sup:1,basefont:1,map:1,applet:1,font:1,big:1,small:1}), G),I = X(_({p:1}), H),J = X(_({iframe:1}), H, B),K = _({img:1,embed:1,noscript:1,br:1,kbd:1,center:1,button:1,basefont:1,h5:1,h4:1,samp:1,h6:1,ol:1,h1:1,h3:1,h2:1,form:1,font:1,"#":1,select:1,menu:1,ins:1,abbr:1,label:1,code:1,table:1,script:1,cite:1,input:1,iframe:1,strong:1,textarea:1,noframes:1,big:1,small:1,span:1,hr:1,sub:1,bdo:1,"var":1,div:1,object:1,sup:1,strike:1,dir:1,map:1,dl:1,applet:1,del:1,isindex:1,fieldset:1,ul:1,b:1,acronym:1,a:1,blockquote:1,i:1,u:1,s:1,tt:1,address:1,q:1,pre:1,p:1,em:1,dfn:1}),L = X(_({a:0}), J),M = _({tr:1}),N = _({"#":1}),O = X(_({param:1}), K),P = X(_({form:1}), A, D, E, I),Q = _({li:1}),R = _({style:1,script:1}),S = _({base:1,link:1,meta:1,title:1}),T = X(S, R),U = _({head:1,body:1}),V = _({html:1});
    var block = _({address:1,blockquote:1,center:1,dir:1,div:1,dl:1,fieldset:1,form:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1,hr:1,isindex:1,menu:1,noframes:1,ol:1,p:1,pre:1,table:1,ul:1}),empty = _({area:1,base:1,br:1,col:1,hr:1,img:1,embed:1,input:1,link:1,meta:1,param:1});
    return _({$nonBodyContent:X(V, U, S),$block:block,$inline:L,$body:X(_({script:1,style:1}), block),$cdata:_({script:1,style:1}),$empty:empty,$nonChild:_({iframe:1}),$listItem:_({dd:1,dt:1,li:1}),$list:_({ul:1,ol:1,dl:1}),$isNotEmpty:_({table:1,ul:1,ol:1,dl:1,iframe:1,area:1,base:1,col:1,hr:1,img:1,embed:1,input:1,link:1,meta:1,param:1}),$removeEmpty:_({a:1,abbr:1,acronym:1,address:1,b:1,bdo:1,big:1,cite:1,code:1,del:1,dfn:1,em:1,font:1,i:1,ins:1,label:1,kbd:1,q:1,s:1,samp:1,small:1,span:1,strike:1,strong:1,sub:1,sup:1,tt:1,u:1,"var":1}),$tableContent:_({caption:1,col:1,colgroup:1,tbody:1,td:1,tfoot:1,th:1,thead:1,tr:1,table:1}),html:U,head:T,style:N,script:N,body:P,base:{},link:{},meta:{},title:N,col:{},tr:_({td:1,th:1}),img:{},embed:{},colgroup:_({thead:1,col:1,tbody:1,tr:1,tfoot:1}),noscript:P,td:P,br:{},th:P,center:P,kbd:L,button:X(I, E),basefont:{},h5:L,h4:L,samp:L,h6:L,ol:Q,h1:L,h3:L,option:N,h2:L,form:X(A, D, E, I),select:_({optgroup:1,option:1}),font:L,ins:L,menu:Q,abbr:L,label:L,table:_({thead:1,col:1,tbody:1,tr:1,colgroup:1,caption:1,tfoot:1}),code:L,tfoot:M,cite:L,li:P,input:{},iframe:P,strong:L,textarea:N,noframes:P,big:L,small:L,span:{"#":1},hr:L,dt:L,sub:L,optgroup:_({option:1}),param:{},bdo:L,"var":L,div:P,object:O,sup:L,dd:P,strike:L,area:{},dir:Q,map:X(_({area:1,form:1,p:1}), A, F, E),applet:O,dl:_({dt:1,dd:1}),del:L,isindex:{},fieldset:X(_({legend:1}), K),thead:M,ul:Q,acronym:L,b:L,a:X(_({a:1}), J),blockquote:X(_({td:1,tr:1,tbody:1,li:1}), P),caption:L,i:L,u:L,tbody:M,s:L,address:X(D, I),tt:L,legend:L,q:L,pre:X(G, C),p:X(_({"a":1}), L),em:L,dfn:L});
})();
baidu.editor.dom.domUtils = baidu.editor.dom.domUtils || {};
(function() {
    var editor = baidu.editor,browser = editor.browser,dtd = editor.dom.dtd,utils = editor.utils,orphanDiv;

    function fixColor(name, value) {
        if (/color/i.test(name) && /rgba?/.test(value)) {
            var array = value.split(",");
            if (array.length > 3) {
                return "";
            }
            value = "#";
            for (var i = 0,color; color = array[i++];) {
                color = parseInt(color.replace(/[^\d]/gi, ""), 10).toString(16);
                value += color.length == 1 ? "0" + color : color;
            }
            value = value.toUpperCase();
        }
        return value;
    }

    function getDomNode(node, start, ltr, startFromChild, fn, guard) {
        var tmpNode = startFromChild && node[start],parent;
        !tmpNode && (tmpNode = node[ltr]);
        while (!tmpNode && (parent = (parent || node).parentNode)) {
            if (parent.tagName == "BODY") {
                return null;
            }
            if (guard && !guard(parent)) {
                return null;
            }
            tmpNode = parent[ltr];
        }
        if (tmpNode && fn && !fn(tmpNode)) {
            return getDomNode(tmpNode, start, ltr, false, fn);
        }
        return tmpNode;
    }

    var attrFix = browser.ie && browser.version < 9 ? {tabindex:"tabIndex",readonly:"readOnly","for":"htmlFor","class":"className",maxlength:"maxLength",cellspacing:"cellSpacing",cellpadding:"cellPadding",rowspan:"rowSpan",colspan:"colSpan",usemap:"useMap",frameborder:"frameBorder"} : {tabindex:"tabIndex",readonly:"readOnly"};
    var domUtils = baidu.editor.dom.domUtils = {NODE_ELEMENT:1,NODE_DOCUMENT:9,NODE_TEXT:3,NODE_COMMENT:8,NODE_DOCUMENT_FRAGMENT:11,POSITION_IDENTICAL:0,POSITION_DISCONNECTED:1,POSITION_FOLLOWING:2,POSITION_PRECEDING:4,POSITION_IS_CONTAINED:8,POSITION_CONTAINS:16,fillChar:browser.ie && browser.version == "6" ? "\ufeff" : "\u200b",keys:{8:1,46:1,16:1,17:1,18:1,37:1,38:1,39:1,40:1,13:1},getPosition:function(nodeA, nodeB) {
        if (nodeA === nodeB) {
            return 0;
        }
        var node,parentsA = [nodeA],parentsB = [nodeB];
        node = nodeA;
        while (node = node.parentNode) {
            if (node === nodeB) {
                return 10;
            }
            parentsA.push(node);
        }
        node = nodeB;
        while (node = node.parentNode) {
            if (node === nodeA) {
                return 20;
            }
            parentsB.push(node);
        }
        parentsA.reverse();
        parentsB.reverse();
        if (parentsA[0] !== parentsB[0]) {
            return 1;
        }
        var i = -1;
        while (i++,parentsA[i] === parentsB[i]) {
        }
        nodeA = parentsA[i];
        nodeB = parentsB[i];
        while (nodeA = nodeA.nextSibling) {
            if (nodeA === nodeB) {
                return 4;
            }
        }
        return 2;
    },getNodeIndex:function(node) {
        var childNodes = node.parentNode.childNodes,i = 0;
        while (childNodes[i] !== node) {
            i++;
        }
        return i;
    },findParent:function(node, tester, includeSelf) {
        if (!this.isBody(node)) {
            node = includeSelf ? node : node.parentNode;
            while (node) {
                if (!tester || tester(node) || this.isBody(node)) {
                    return tester && !tester(node) && this.isBody(node) ? null : node;
                }
                node = node.parentNode;
            }
        }
        return null;
    },findParentByTagName:function(node, tagName, includeSelf) {
        if (node && node.nodeType && !this.isBody(node) && (node.nodeType == 1 || node.nodeType)) {
            tagName = !utils.isArray(tagName) ? [tagName] : tagName;
            node = node.nodeType == 3 || !includeSelf ? node.parentNode : node;
            while (node && node.tagName && node.nodeType != 9) {
                if (utils.indexOf(tagName, node.tagName.toLowerCase()) > -1) {
                    return node;
                }
                node = node.parentNode;
            }
        }
        return null;
    },findParents:function(node, includeSelf, tester, closerFirst) {
        var parents = includeSelf && (tester && tester(node) || !tester) ? [node] : [];
        while (node = domUtils.findParent(node, tester)) {
            parents.push(node);
        }
        if (!closerFirst) {
            parents.reverse();
        }
        return parents;
    },insertAfter:function(node, nodeToInsert) {
        return node.parentNode.insertBefore(nodeToInsert, node.nextSibling);
    },remove:function(node, keepChildren) {
        var parent = node.parentNode,child;
        if (parent) {
            if (keepChildren && node.hasChildNodes()) {
                while (child = node.firstChild) {
                    parent.insertBefore(child, node);
                }
            }
            parent.removeChild(node);
        }
        return node;
    },getNextDomNode:function(node, startFromChild, filter, guard) {
        return getDomNode(node, "firstChild", "nextSibling", startFromChild, filter, guard);
    },getPreviousDomNode:function(node, startFromChild, fn) {
        return getDomNode(node, "lastChild", "previousSibling", startFromChild, fn);
    },isBookmarkNode:function(node) {
        return node.nodeType == 1 && node.id && /^_baidu_bookmark_/i.test(node.id);
    },getWindow:function(node) {
        var doc = node.ownerDocument || node;
        return doc.defaultView || doc.parentWindow;
    },getCommonAncestor:function(nodeA, nodeB) {
        if (nodeA === nodeB) {
            return nodeA;
        }
        var parentsA = [nodeA],parentsB = [nodeB],parent = nodeA,i = -1;
        while (parent = parent.parentNode) {
            if (parent === nodeB) {
                return parent;
            }
            parentsA.push(parent);
        }
        parent = nodeB;
        while (parent = parent.parentNode) {
            if (parent === nodeA) {
                return parent;
            }
            parentsB.push(parent);
        }
        parentsA.reverse();
        parentsB.reverse();
        while (i++,parentsA[i] === parentsB[i]) {
        }
        return i == 0 ? null : parentsA[i - 1];
    },clearEmptySibling:function(node, ingoreNext, ingorePre) {
        function clear(next, dir) {
            var tmpNode;
            if (next && (!domUtils.isBookmarkNode(next) && domUtils.isEmptyInlineElement(next) || domUtils.isWhitespace(next))) {
                tmpNode = next[dir];
                domUtils.remove(next);
                tmpNode && clear(tmpNode, dir);
            }
        }

        !ingoreNext && clear(node.nextSibling, "nextSibling");
        !ingorePre && clear(node.previousSibling, "previousSibling");
    },split:function(node, offset) {
        var doc = node.ownerDocument;
        if (browser.ie && offset == node.nodeValue.length) {
            var next = doc.createTextNode("");
            return domUtils.insertAfter(node, next);
        }
        var retval = node.splitText(offset);
        if (browser.ie8) {
            var tmpNode = doc.createTextNode("");
            domUtils.insertAfter(retval, tmpNode);
            domUtils.remove(tmpNode);
        }
        return retval;
    },isWhitespace:function(node) {
        var reg = new RegExp("[^ \t\n\r" + domUtils.fillChar + "]");
        return !reg.test(node.nodeValue);
    },getXY:function(element) {
        var x = 0,y = 0;
        while (element.offsetParent) {
            y += element.offsetTop;
            x += element.offsetLeft;
            element = element.offsetParent;
        }
        return {"x":x,"y":y};
    },on:function(obj, type, handler) {
        var types = type instanceof Array ? type : [type],k = types.length;
        var d;
        if (!obj.addEventListener) {
            d = function(evt) {
                evt = evt || window.event;
                var el = evt.srcElement;
                return handler.call(el, evt);
            };
            handler._d = d;
        }
        if (k) {
            while (k--) {
                type = types[k];
                if (obj.addEventListener) {
                    obj.addEventListener(type, handler, false);
                } else {
                    obj.attachEvent("on" + type, d);
                }
            }
        }
        obj = null;
    },un:function(obj, type, handler) {
        var types = type instanceof Array ? type : [type],k = types.length;
        if (k) {
            while (k--) {
                type = types[k];
                if (obj.removeEventListener) {
                    obj.removeEventListener(type, handler, false);
                } else {
                    obj.detachEvent("on" + type, handler._d || handler);
                }
            }
        }
    },isSameElement:function(nodeA, nodeB) {
        if (nodeA.tagName != nodeB.tagName) {
            return false;
        }
        var thisAttribs = nodeA.attributes,otherAttribs = nodeB.attributes;
        if (!browser.ie && thisAttribs.length != otherAttribs.length) {
            return false;
        }
        var k = thisAttribs.length,specLen = 0;
        if (k) {
            while (k--) {
                var thisAttr = thisAttribs[k];
                if (!browser.ie || thisAttr.specified) {
                    specLen++;
                    if (thisAttr.nodeName == "style") {
                        continue;
                    }
                    var attr = nodeB.attributes[thisAttr.nodeName];
                    var attrValue = attr && attr.nodeValue || null;
                    if (attrValue != thisAttr.nodeValue) {
                        return false;
                    }
                }
            }
        }
        if (!domUtils.isSameStyle(nodeA, nodeB)) {
            return false;
        }
        if (browser.ie) {
            k = otherAttribs.length;
            if (k) {
                while (k--) {
                    if (otherAttribs[k].specified) {
                        specLen--;
                    }
                }
            }
            return !specLen;
        }
        return true;
    },isRedundantSpan:function(node) {
        if (node.nodeType == 3 || node.tagName.toLowerCase() != "span") {
            return 0;
        }
        if (browser.ie) {
            return node.style.cssText == "" ? 1 : 0;
        }
        return !node.attributes.length;
    },isSameStyle:function(elementA, elementB) {
        var styleA = elementA.style.cssText,styleB = elementB.style.cssText;
        if (!styleA && !styleB) {
            return true;
        } else {
            if (!styleA || !styleB) {
                return false;
            }
        }
        var styleNameMap = {},record = [],exit = {};
        styleA.replace(/[\w-]+\s*(?=:)/g, function(name) {
            styleNameMap[name] = record.push(name);
        });
        try {
            styleB.replace(/[\w-]+\s*(?=:)/g, function(name) {
                var index = styleNameMap[name];
                if (index) {
                    name = utils.cssStyleToDomStyle(name);
                    if (elementA.style[name] !== elementB.style[name]) {
                        throw exit;
                    }
                    record[index - 1] = "";
                } else {
                    throw exit;
                }
            });
        }
        catch(ex) {
            if (ex === exit) {
                return false;
            }
        }
        return !record.join("");
    },isBlockElm:function() {
        var blockBoundaryDisplayMatch = ["block","list-item","table","table-row-group","table-header-group","table-footer-group","table-row","table-column-group","table-column","table-cell","table-caption"],blockBoundaryNodeNameMatch = {hr:1};
        return function(node, customNodeNames) {
            return node.nodeType == 1 && (utils.indexOf(blockBoundaryDisplayMatch, domUtils.getComputedStyle(node, "display")) != -1 || utils.extend(blockBoundaryNodeNameMatch, customNodeNames || {})[node.tagName.toLocaleLowerCase()]);
        };
    }(),isBody:function(node) {
        return node && node.nodeType == 1 && node.tagName.toLowerCase() == "body";
    },breakParent:function(node, parent) {
        var tmpNode,parentClone = node,clone = node,leftNodes,rightNodes;
        do {
            parentClone = parentClone.parentNode;
            if (leftNodes) {
                tmpNode = parentClone.cloneNode(false);
                tmpNode.appendChild(leftNodes);
                leftNodes = tmpNode;
                tmpNode = parentClone.cloneNode(false);
                tmpNode.appendChild(rightNodes);
                rightNodes = tmpNode;
            } else {
                leftNodes = parentClone.cloneNode(false);
                rightNodes = leftNodes.cloneNode(false);
            }
            while (tmpNode = clone.previousSibling) {
                leftNodes.insertBefore(tmpNode, leftNodes.firstChild);
            }
            while (tmpNode = clone.nextSibling) {
                rightNodes.appendChild(tmpNode);
            }
            clone = parentClone;
        } while (parent !== parentClone);
        tmpNode = parent.parentNode;
        tmpNode.insertBefore(leftNodes, parent);
        tmpNode.insertBefore(rightNodes, parent);
        tmpNode.insertBefore(node, rightNodes);
        domUtils.remove(parent);
        return node;
    },isEmptyInlineElement:function(node) {
        if (node.nodeType != 1 || !dtd.$removeEmpty[node.tagName]) {
            return 0;
        }
        node = node.firstChild;
        while (node) {
            if (domUtils.isBookmarkNode(node)) {
                return 0;
            }
            if (node.nodeType == 1 && !domUtils.isEmptyInlineElement(node) || node.nodeType == 3 && !domUtils.isWhitespace(node)) {
                return 0;
            }
            node = node.nextSibling;
        }
        return 1;
    },trimWhiteTextNode:function(node) {
        function remove(dir) {
            var child;
            while ((child = node[dir]) && child.nodeType == 3 && domUtils.isWhitespace(child)) {
                node.removeChild(child);
            }
        }

        remove("firstChild");
        remove("lastChild");
    },mergChild:function(node, tagName, attrs) {
        var list = domUtils.getElementsByTagName(node, node.tagName.toLowerCase());
        for (var i = 0,ci; ci = list[i++];) {
            if (!ci.parentNode || domUtils.isBookmarkNode(ci)) {
                continue;
            }
            if (ci.tagName.toLowerCase() == "span") {
                if (node === ci.parentNode) {
                    domUtils.trimWhiteTextNode(node);
                    if (node.childNodes.length == 1) {
                        node.style.cssText = ci.style.cssText + ";" + node.style.cssText;
                        domUtils.remove(ci, true);
                        continue;
                    }
                }
                ci.style.cssText = node.style.cssText + ";" + ci.style.cssText;
                if (attrs) {
                    var style = attrs.style;
                    if (style) {
                        style = style.split(";");
                        for (var j = 0,s; s = style[j++];) {
                            ci.style[utils.cssStyleToDomStyle(s.split(":")[0])] = s.split(":")[1];
                        }
                    }
                }
                if (domUtils.isSameStyle(ci, node)) {
                    domUtils.remove(ci, true);
                }
                continue;
            }
            if (domUtils.isSameElement(node, ci)) {
                domUtils.remove(ci, true);
            }
        }
        if (tagName == "span") {
            var as = domUtils.getElementsByTagName(node, "a");
            for (var i = 0,ai; ai = as[i++];) {
                ai.style.cssText = ";" + node.style.cssText;
                ai.style.textDecoration = "underline";
            }
        }
    },getElementsByTagName:function(node, name) {
        var list = node.getElementsByTagName(name),arr = [];
        for (var i = 0,ci; ci = list[i++];) {
            arr.push(ci);
        }
        return arr;
    },mergToParent:function(node) {
        var parent = node.parentNode;
        while (parent && dtd.$removeEmpty[parent.tagName]) {
            if (parent.tagName == node.tagName || parent.tagName == "A") {
                domUtils.trimWhiteTextNode(parent);
                if (parent.tagName.toLowerCase() == "span" && !domUtils.isSameStyle(parent, node) || (parent.tagName == "A" && node.tagName == "SPAN")) {
                    if (parent.childNodes.length > 1 || parent !== node.parentNode) {
                        node.style.cssText = parent.style.cssText + ";" + node.style.cssText;
                        parent = parent.parentNode;
                        continue;
                    } else {
                        parent.style.cssText += ";" + node.style.cssText;
                        if (parent.tagName == "A") {
                            parent.style.textDecoration = "underline";
                        }
                    }
                }
                parent.tagName != "A" && domUtils.remove(node, true);
            }
            parent = parent.parentNode;
        }
    },mergSibling:function(node, ingorePre, ingoreNext) {
        function merg(rtl, start, node) {
            var next;
            if ((next = node[rtl]) && !domUtils.isBookmarkNode(next) && next.nodeType == 1 && domUtils.isSameElement(node, next)) {
                while (next.firstChild) {
                    if (start == "firstChild") {
                        node.insertBefore(next.lastChild, node.firstChild);
                    } else {
                        node.appendChild(next.firstChild);
                    }
                }
                domUtils.remove(next);
            }
        }

        !ingorePre && merg("previousSibling", "firstChild", node);
        !ingoreNext && merg("nextSibling", "lastChild", node);
    },unselectable:browser.gecko ? function(node) {
        node.style.MozUserSelect = "none";
    } : browser.webkit ? function(node) {
        node.style.KhtmlUserSelect = "none";
    } : function(node) {
        node.unselectable = "on";
        for (var i = 0,ci; ci = node.all[i++];) {
            switch (ci.tagName.toLowerCase()) {
                case "iframe":
                case "textarea":
                case "input":
                case "select":
                    break;
                default:
                    ci.unselectable = "on";
            }
        }
    },removeAttributes:function(element, attrNames) {
        var k = attrNames.length;
        if (k) {
            while (k--) {
                var attr = attrNames[k];
                attr = attrFix[attr] || attr;
                element.removeAttribute(attr);
            }
        }
    },setAttributes:function(node, attrs) {
        for (var name in attrs) {
            switch (name.toLowerCase()) {
                case "class":
                    node.className = attrs[name];
                    break;
                case "style":
                    node.style.cssText = attrs[name];
                    break;
                default:
                    node.setAttribute(name.toLowerCase(), attrs[name]);
            }
        }
        return node;
    },getComputedStyle:function(element, styleName) {
        function fixUnit(key, val) {
            if (key == "font-size" && /pt$/.test(val)) {
                val = Math.round(parseFloat(val) / 0.75) + "px";
            }
            return val;
        }

        if (element.nodeType == 3) {
            element = element.parentNode;
        }
        if (browser.ie && styleName == "font-size" && !dtd.$empty[element.tagName] && !dtd.$nonChild[element.tagName]) {
            var span = element.ownerDocument.createElement("span");
            span.style.cssText = "padding:0;border:0;";
            span.innerHTML = ".";
            element.appendChild(span);
            var result = span.offsetHeight;
            element.removeChild(span);
            span = null;
            return result + "px";
        }
        try {
            var value = domUtils.getStyle(element, styleName) || (window.getComputedStyle ? domUtils.getWindow(element).getComputedStyle(element, "").getPropertyValue(styleName) : (element.currentStyle || element.style)[utils.cssStyleToDomStyle(styleName)]);
        }
        catch(e) {
            return null;
        }
        return fixUnit(styleName, fixColor(styleName, value));
    },removeClasses:function(element, classNames) {
        element.className = (" " + element.className + " ").replace(new RegExp("(?:\\s+(?:" + classNames.join("|") + "))+\\s+", "g"), " ");
    },removeStyle:function(node, name) {
        node.style[utils.cssStyleToDomStyle(name)] = "";
        if (node.style.removeAttribute) {
            node.style.removeAttribute(utils.cssStyleToDomStyle(name));
        }
        if (!node.style.cssText) {
            node.removeAttribute("style");
        }
    },hasClass:function(element, className) {
        return (" " + element.className + " ").indexOf(" " + className + " ") > -1;
    },preventDefault:function(evt) {
        if (evt.preventDefault) {
            evt.preventDefault();
        } else {
            evt.returnValue = false;
        }
    },getStyle:function(element, name) {
        var value = element.style[utils.cssStyleToDomStyle(name)];
        return fixColor(name, value);
    },setStyle:function(element, name, value) {
        element.style[utils.cssStyleToDomStyle(name)] = value;
    },setStyles:function(element, styles) {
        for (var name in styles) {
            if (styles.hasOwnProperty(name)) {
                domUtils.setStyle(element, name, styles[name]);
            }
        }
    },removeDirtyAttr:function(node) {
        for (var i = 0,ci,nodes = node.getElementsByTagName("*"); ci = nodes[i++];) {
            ci.removeAttribute("_moz_dirty");
        }
        node.removeAttribute("_moz_dirty");
    },getChildCount:function(node, fn) {
        var count = 0,first = node.firstChild;
        fn = fn || function() {
            return 1;
        };
        while (first) {
            if (fn(first)) {
                count++;
            }
            first = first.nextSibling;
        }
        return count;
    },clearReduent:function(node, tags) {
        var nodes,reg = new RegExp(domUtils.fillChar, "g"),_parent;
        for (var t = 0,ti; ti = tags[t++];) {
            nodes = node.getElementsByTagName(ti);
            for (var i = 0,ci; ci = nodes[i++];) {
                if (ci.parentNode && ci[browser.ie ? "innerText" : "textContent"].replace(reg, "").length == 0 && ci.children.length == 0) {
                    _parent = ci.parentNode;
                    domUtils.remove(ci);
                    while (_parent.childNodes.length == 0 && new RegExp(tags.join("|"), "i").test(_parent.tagName)) {
                        ci = _parent;
                        _parent = _parent.parentNode;
                        domUtils.remove(ci);
                    }
                }
            }
        }
    },isEmptyNode:function(node) {
        var first = node.firstChild;
        return !first || domUtils.getChildCount(node, function(node) {
            return !domUtils.isBr(node) && !domUtils.isBookmarkNode(node) && !domUtils.isWhitespace(node);
        }) == 0;
    },clearSelectedArr:function(nodes) {
        var node;
        while (node = nodes.pop()) {
            node.className = "";
        }
    },scrollToView:function(node, win, offsetTop) {
        var getViewPaneSize = function() {
            var doc = win.document,mode = doc.compatMode == "CSS1Compat";
            return {width:(mode ? doc.documentElement.clientWidth : doc.body.clientWidth) || 0,height:(mode ? doc.documentElement.clientHeight : doc.body.clientHeight) || 0};
        },getScrollPosition = function(win) {
            if ("pageXOffset" in win) {
                return {x:win.pageXOffset || 0,y:win.pageYOffset || 0};
            } else {
                var doc = win.document;
                return {x:doc.documentElement.scrollLeft || doc.body.scrollLeft || 0,y:doc.documentElement.scrollTop || doc.body.scrollTop || 0};
            }
        };
        var winHeight = getViewPaneSize().height,offset = winHeight * -1 + offsetTop;
        offset += (node.offsetHeight || 0);
        var elementPosition = domUtils.getXY(node);
        offset += elementPosition.y;
        var currentScroll = getScrollPosition(win).y;
        if (offset > currentScroll || offset < currentScroll - winHeight) {
            win.scrollTo(0, offset + (offset < 0 ? -20 : 20));
        }
    },isBr:function(node) {
        return node.nodeType == 1 && node.tagName == "BR";
    },findTagNamesInSelection:function(range, tags) {
        var as,ps,pe,node,start,end,common;
        if (range.collapsed) {
            node = range.startContainer;
            if (node && (node = domUtils.findParentByTagName(node, tags, true))) {
                return node;
            }
        } else {
            range.shrinkBoundary();
            start = range.startContainer.nodeType == 3 || !range.startContainer.childNodes[range.startOffset] ? range.startContainer : range.startContainer.childNodes[range.startOffset];
            end = range.endContainer.nodeType == 3 || range.endOffset == 0 ? range.endContainer : range.endContainer.childNodes[range.endOffset - 1];
            common = domUtils.getCommonAncestor(start, end);
            for (var j = 0,t; t = tags[j++];) {
                node = domUtils.findParentByTagName(common, t, true);
                if (!node && common.nodeType == 1) {
                    as = common.getElementsByTagName(t);
                    for (var i = 0,ci; ci = as[i++];) {
                        if (start == ci || end == ci) {
                            node = ci;
                            break;
                        }
                        ps = domUtils.getPosition(ci, start),pe = domUtils.getPosition(ci, end);
                        if ((ps & domUtils.POSITION_FOLLOWING || ps & domUtils.POSITION_CONTAINS) && (pe & domUtils.POSITION_PRECEDING || pe & domUtils.POSITION_CONTAINS)) {
                            node = ci;
                            break;
                        }
                    }
                }
                if (node) {
                    return node;
                }
            }
        }
        return null;
    },isFillChar:function(node) {
        var reg = new RegExp(domUtils.fillChar);
        return node.nodeType == 3 && !node.nodeValue.replace(reg, "").length;
    },isStartInblock:function(range) {
        var tmpRange = range.cloneRange(),flag = 0,start = tmpRange.startContainer;
        if (domUtils.isFillChar(start)) {
            tmpRange.setStartBefore(start);
        }
        while (!tmpRange.startOffset) {
            start = tmpRange.startContainer;
            if (domUtils.isBlockElm(start) || domUtils.isBody(start)) {
                flag = 1;
                break;
            }
            var pre = tmpRange.startContainer.previousSibling,tmpNode;
            while (pre && domUtils.isFillChar(pre)) {
                tmpNode = pre;
                pre = pre.previousSibling;
            }
            if (tmpNode) {
                tmpRange.setStartBefore(tmpNode);
            } else {
                tmpRange.setStartBefore(tmpRange.startContainer);
            }
        }
        return flag && !domUtils.isBody(tmpRange.startContainer) ? 1 : 0;
    },isEmptyBlock:function(node) {
        var reg = new RegExp("[ \t\r\n" + domUtils.fillChar + "]", "g");
        if (node[browser.ie ? "innerText" : "textContent"].replace(reg, "").length > 0) {
            return 0;
        }
        for (var n in dtd.$isNotEmpty) {
            if (node.getElementsByTagName(n).length) {
                return 0;
            }
        }
        return 1;
    },findStartElement:function(range, tester) {
        var node = range.startContainer;
        if (node.hasChildNodes()) {
            node = node.childNodes[range.startOffset] || node;
        } else {
            if (node.nodeType == 3) {
                if (range.startOffset == 0) {
                    node = node.previousSibling || node.parentNode;
                } else {
                    if (range.startOffset >= node.nodeValue.length) {
                        node = node.nextSibling || node.parentNode;
                    }
                }
            }
        }
        if (node.nodeType != 1) {
            node = node.parentNode;
        }
        while (node != null) {
            if (matchSelector(node)) {
                return node;
            }
            node = node.parentNode;
        }
        return null;
        function matchSelector(node) {
            if (typeof tester == "string") {
                return node.nodeName == tester;
            } else {
                if (typeof tester == "function") {
                    return tester(node);
                } else {
                    return tester.test(node.nodeName);
                }
            }
        }
    }};
})();
baidu.editor.dom.Range = baidu.editor.dom.Range || {};
(function() {
    var editor = baidu.editor,browser = editor.browser,domUtils = editor.dom.domUtils,dtd = editor.dom.dtd,utils = editor.utils,guid = 0,fillChar = domUtils.fillChar;
    var updateCollapse = function(range) {
        range.collapsed = range.startContainer && range.endContainer && range.startContainer === range.endContainer && range.startOffset == range.endOffset;
    },setEndPoint = function(toStart, node, offset, range) {
        if (node.nodeType == 1 && (dtd.$empty[node.tagName] || dtd.$nonChild[node.tagName])) {
            offset = domUtils.getNodeIndex(node) + (toStart ? 0 : 1);
            node = node.parentNode;
        }
        if (toStart) {
            range.startContainer = node;
            range.startOffset = offset;
            if (!range.endContainer) {
                range.collapse(true);
            }
        } else {
            range.endContainer = node;
            range.endOffset = offset;
            if (!range.startContainer) {
                range.collapse(false);
            }
        }
        updateCollapse(range);
        return range;
    },execContentsAction = function(range, action) {
        var start = range.startContainer,end = range.endContainer,startOffset = range.startOffset,endOffset = range.endOffset,doc = range.document,frag = doc.createDocumentFragment(),tmpStart,tmpEnd;
        if (start.nodeType == 1) {
            start = start.childNodes[startOffset] || (tmpStart = start.appendChild(doc.createTextNode("")));
        }
        if (end.nodeType == 1) {
            end = end.childNodes[endOffset] || (tmpEnd = end.appendChild(doc.createTextNode("")));
        }
        if (start === end && start.nodeType == 3) {
            frag.appendChild(doc.createTextNode(start.substringData(startOffset, endOffset - startOffset)));
            if (action) {
                start.deleteData(startOffset, endOffset - startOffset);
                range.collapse(true);
            }
            return frag;
        }
        var current,currentLevel,clone = frag,startParents = domUtils.findParents(start, true),endParents = domUtils.findParents(end, true);
        for (var i = 0; startParents[i] == endParents[i]; i++) {
        }
        for (var j = i,si; si = startParents[j]; j++) {
            current = si.nextSibling;
            if (si == start) {
                if (!tmpStart) {
                    if (range.startContainer.nodeType == 3) {
                        clone.appendChild(doc.createTextNode(start.nodeValue.slice(startOffset)));
                        if (action) {
                            start.deleteData(startOffset, start.nodeValue.length - startOffset);
                        }
                    } else {
                        clone.appendChild(!action ? start.cloneNode(true) : start);
                    }
                }
            } else {
                currentLevel = si.cloneNode(false);
                clone.appendChild(currentLevel);
            }
            while (current) {
                if (current === end || current === endParents[j]) {
                    break;
                }
                si = current.nextSibling;
                clone.appendChild(!action ? current.cloneNode(true) : current);
                current = si;
            }
            clone = currentLevel;
        }
        clone = frag;
        if (!startParents[i]) {
            clone.appendChild(startParents[i - 1].cloneNode(false));
            clone = clone.firstChild;
        }
        for (var j = i,ei; ei = endParents[j]; j++) {
            current = ei.previousSibling;
            if (ei == end) {
                if (!tmpEnd && range.endContainer.nodeType == 3) {
                    clone.appendChild(doc.createTextNode(end.substringData(0, endOffset)));
                    if (action) {
                        end.deleteData(0, endOffset);
                    }
                }
            } else {
                currentLevel = ei.cloneNode(false);
                clone.appendChild(currentLevel);
            }
            if (j != i || !startParents[i]) {
                while (current) {
                    if (current === start) {
                        break;
                    }
                    ei = current.previousSibling;
                    clone.insertBefore(!action ? current.cloneNode(true) : current, clone.firstChild);
                    current = ei;
                }
            }
            clone = currentLevel;
        }
        if (action) {
            range.setStartBefore(!endParents[i] ? endParents[i - 1] : !startParents[i] ? startParents[i - 1] : endParents[i]).collapse(true);
        }
        tmpStart && domUtils.remove(tmpStart);
        tmpEnd && domUtils.remove(tmpEnd);
        return frag;
    };
    var Range = baidu.editor.dom.Range = function(document) {
        var me = this;
        me.startContainer = me.startOffset = me.endContainer = me.endOffset = null;
        me.document = document;
        me.collapsed = true;
    };

    function removeFillDataWithEmptyParentNode(node) {
        var parent = node.parentNode,tmpNode;
        domUtils.remove(node);
        while (parent && dtd.$removeEmpty[parent.tagName] && parent.childNodes.length == 0) {
            tmpNode = parent;
            domUtils.remove(parent);
            parent = tmpNode.parentNode;
        }
    }

    Range.prototype = {cloneContents:function() {
        return this.collapsed ? null : execContentsAction(this, 0);
    },deleteContents:function() {
        if (!this.collapsed) {
            execContentsAction(this, 1);
        }
        if (browser.webkit) {
            var txt = this.startContainer;
            if (txt.nodeType == 3 && !txt.nodeValue.length) {
                this.setStartBefore(txt).collapse(true);
                domUtils.remove(txt);
            }
        }
        return this;
    },extractContents:function() {
        return this.collapsed ? null : execContentsAction(this, 2);
    },setStart:function(node, offset) {
        return setEndPoint(true, node, offset, this);
    },setEnd:function(node, offset) {
        return setEndPoint(false, node, offset, this);
    },setStartAfter:function(node) {
        return this.setStart(node.parentNode, domUtils.getNodeIndex(node) + 1);
    },setStartBefore:function(node) {
        return this.setStart(node.parentNode, domUtils.getNodeIndex(node));
    },setEndAfter:function(node) {
        return this.setEnd(node.parentNode, domUtils.getNodeIndex(node) + 1);
    },setEndBefore:function(node) {
        return this.setEnd(node.parentNode, domUtils.getNodeIndex(node));
    },selectNode:function(node) {
        return this.setStartBefore(node).setEndAfter(node);
    },selectNodeContents:function(node) {
        return this.setStart(node, 0).setEnd(node, node.nodeType == 3 ? node.nodeValue.length : node.childNodes.length);
    },cloneRange:function() {
        var me = this,range = new Range(me.document);
        return range.setStart(me.startContainer, me.startOffset).setEnd(me.endContainer, me.endOffset);
    },collapse:function(toStart) {
        var me = this;
        if (toStart) {
            me.endContainer = me.startContainer;
            me.endOffset = me.startOffset;
        } else {
            me.startContainer = me.endContainer;
            me.startOffset = me.endOffset;
        }
        me.collapsed = true;
        return me;
    },shrinkBoundary:function(ignoreEnd) {
        var me = this,child,collapsed = me.collapsed;
        while (me.startContainer.nodeType == 1 && (child = me.startContainer.childNodes[me.startOffset]) && child.nodeType == 1 && !domUtils.isBookmarkNode(child) && !dtd.$empty[child.tagName] && !dtd.$nonChild[child.tagName]) {
            me.setStart(child, 0);
        }
        if (collapsed) {
            return me.collapse(true);
        }
        if (!ignoreEnd) {
            while (me.endContainer.nodeType == 1 && me.endOffset > 0 && (child = me.endContainer.childNodes[me.endOffset - 1]) && child.nodeType == 1 && !domUtils.isBookmarkNode(child) && !dtd.$empty[child.tagName] && !dtd.$nonChild[child.tagName]) {
                me.setEnd(child, child.childNodes.length);
            }
        }
        return me;
    },getCommonAncestor:function(includeSelf, ignoreTextNode) {
        var start = this.startContainer,end = this.endContainer;
        if (start === end) {
            if (includeSelf && start.nodeType == 1 && this.startOffset == this.endOffset - 1) {
                return start.childNodes[this.startOffset];
            }
            return ignoreTextNode && start.nodeType == 3 ? start.parentNode : start;
        }
        return domUtils.getCommonAncestor(start, end);
    },trimBoundary:function(ignoreEnd) {
        this.txtToElmBoundary();
        var start = this.startContainer,offset = this.startOffset,collapsed = this.collapsed,end = this.endContainer;
        if (start.nodeType == 3) {
            if (offset == 0) {
                this.setStartBefore(start);
            } else {
                if (offset >= start.nodeValue.length) {
                    this.setStartAfter(start);
                } else {
                    var textNode = domUtils.split(start, offset);
                    if (start === end) {
                        this.setEnd(textNode, this.endOffset - offset);
                    } else {
                        if (start.parentNode === end) {
                            this.endOffset += 1;
                        }
                    }
                    this.setStartBefore(textNode);
                }
            }
            if (collapsed) {
                return this.collapse(true);
            }
        }
        if (!ignoreEnd) {
            offset = this.endOffset;
            end = this.endContainer;
            if (end.nodeType == 3) {
                if (offset == 0) {
                    this.setEndBefore(end);
                } else {
                    if (offset >= end.nodeValue.length) {
                        this.setEndAfter(end);
                    } else {
                        domUtils.split(end, offset);
                        this.setEndAfter(end);
                    }
                }
            }
        }
        return this;
    },txtToElmBoundary:function() {
        function adjust(r, c) {
            var container = r[c + "Container"],offset = r[c + "Offset"];
            if (container.nodeType == 3) {
                if (!offset) {
                    r["set" + c.replace(/(\w)/, function(a) {
                        return a.toUpperCase();
                    }) + "Before"](container);
                } else {
                    if (offset >= container.nodeValue.length) {
                        r["set" + c.replace(/(\w)/, function(a) {
                            return a.toUpperCase();
                        }) + "After"](container);
                    }
                }
            }
        }

        if (!this.collapsed) {
            adjust(this, "start");
            adjust(this, "end");
        }
        return this;
    },insertNode:function(node) {
        var first = node,length = 1;
        if (node.nodeType == 11) {
            first = node.firstChild;
            length = node.childNodes.length;
        }
        this.trimBoundary(true);
        var start = this.startContainer,offset = this.startOffset;
        var nextNode = start.childNodes[offset];
        if (nextNode) {
            start.insertBefore(node, nextNode);
        } else {
            start.appendChild(node);
        }
        if (first.parentNode === this.endContainer) {
            this.endOffset = this.endOffset + length;
        }
        return this.setStartBefore(first);
    },setCursor:function(toEnd, notFillData) {
        return this.collapse(toEnd ? false : true).select(notFillData);
    },createBookmark:function(serialize, same) {
        var endNode,startNode = this.document.createElement("span");
        startNode.style.cssText = "display:none;line-height:0px;";
        startNode.appendChild(this.document.createTextNode("\ufeff"));
        startNode.id = "_baidu_bookmark_start_" + (same ? "" : guid++);
        if (!this.collapsed) {
            endNode = startNode.cloneNode(true);
            endNode.id = "_baidu_bookmark_end_" + (same ? "" : guid++);
        }
        this.insertNode(startNode);
        if (endNode) {
            this.collapse(false).insertNode(endNode);
            this.setEndBefore(endNode);
        }
        this.setStartAfter(startNode);
        return {start:serialize ? startNode.id : startNode,end:endNode ? serialize ? endNode.id : endNode : null,id:serialize};
    },moveToBookmark:function(bookmark) {
        var start = bookmark.id ? this.document.getElementById(bookmark.start) : bookmark.start,end = bookmark.end && bookmark.id ? this.document.getElementById(bookmark.end) : bookmark.end;
        this.setStartBefore(start);
        domUtils.remove(start);
        if (end) {
            this.setEndBefore(end);
            domUtils.remove(end);
        } else {
            this.collapse(true);
        }
        return this;
    },enlarge:function(toBlock, stopFn) {
        var isBody = domUtils.isBody,pre,node,tmp = this.document.createTextNode("");
        if (toBlock) {
            node = this.startContainer;
            if (node.nodeType == 1) {
                if (node.childNodes[this.startOffset]) {
                    pre = node = node.childNodes[this.startOffset];
                } else {
                    node.appendChild(tmp);
                    pre = node = tmp;
                }
            } else {
                pre = node;
            }
            while (1) {
                if (domUtils.isBlockElm(node)) {
                    node = pre;
                    while ((pre = node.previousSibling) && !domUtils.isBlockElm(pre)) {
                        node = pre;
                    }
                    this.setStartBefore(node);
                    break;
                }
                pre = node;
                node = node.parentNode;
            }
            node = this.endContainer;
            if (node.nodeType == 1) {
                if (pre = node.childNodes[this.endOffset]) {
                    node.insertBefore(tmp, pre);
                } else {
                    node.appendChild(tmp);
                }
                pre = node = tmp;
            } else {
                pre = node;
            }
            while (1) {
                if (domUtils.isBlockElm(node)) {
                    node = pre;
                    while ((pre = node.nextSibling) && !domUtils.isBlockElm(pre)) {
                        node = pre;
                    }
                    this.setEndAfter(node);
                    break;
                }
                pre = node;
                node = node.parentNode;
            }
            if (tmp.parentNode === this.endContainer) {
                this.endOffset--;
            }
            domUtils.remove(tmp);
        }
        if (!this.collapsed) {
            while (this.startOffset == 0) {
                if (stopFn && stopFn(this.startContainer)) {
                    break;
                }
                if (isBody(this.startContainer)) {
                    break;
                }
                this.setStartBefore(this.startContainer);
            }
            while (this.endOffset == (this.endContainer.nodeType == 1 ? this.endContainer.childNodes.length : this.endContainer.nodeValue.length)) {
                if (stopFn && stopFn(this.endContainer)) {
                    break;
                }
                if (isBody(this.endContainer)) {
                    break;
                }
                this.setEndAfter(this.endContainer);
            }
        }
        return this;
    },adjustmentBoundary:function() {
        if (!this.collapsed) {
            while (!domUtils.isBody(this.startContainer) && this.startOffset == this.startContainer[this.startContainer.nodeType == 3 ? "nodeValue" : "childNodes"].length) {
                this.setStartAfter(this.startContainer);
            }
            while (!domUtils.isBody(this.endContainer) && !this.endOffset) {
                this.setEndBefore(this.endContainer);
            }
        }
        return this;
    },applyInlineStyle:function(tagName, attrs, list) {
        if (this.collapsed) {
            return this;
        }
        this.trimBoundary().enlarge(false,
            function(node) {
                return node.nodeType == 1 && domUtils.isBlockElm(node);
            }).adjustmentBoundary();
        var bookmark = this.createBookmark(),end = bookmark.end,filterFn = function(node) {
            return node.nodeType == 1 ? node.tagName.toLowerCase() != "br" : !domUtils.isWhitespace(node);
        },current = domUtils.getNextDomNode(bookmark.start, false, filterFn),node,pre,range = this.cloneRange();
        while (current && (domUtils.getPosition(current, end) & domUtils.POSITION_PRECEDING)) {
            if (current.nodeType == 3 || dtd[tagName][current.tagName]) {
                range.setStartBefore(current);
                node = current;
                while (node && (node.nodeType == 3 || dtd[tagName][node.tagName]) && node !== end) {
                    pre = node;
                    node = domUtils.getNextDomNode(node, node.nodeType == 1, null, function(parent) {
                        return dtd[tagName][parent.tagName];
                    });
                }
                var frag = range.setEndAfter(pre).extractContents(),elm;
                if (list && list.length > 0) {
                    var level,top;
                    top = level = list[0].cloneNode(false);
                    for (var i = 1,ci; ci = list[i++];) {
                        level.appendChild(ci.cloneNode(false));
                        level = level.firstChild;
                    }
                    elm = level;
                } else {
                    elm = range.document.createElement(tagName);
                }
                if (attrs) {
                    domUtils.setAttributes(elm, attrs);
                }
                elm.appendChild(frag);
                domUtils.mergChild(elm, tagName, attrs);
                range.insertNode(list ? top : elm);
                var aNode;
                if (tagName == "span" && attrs.style && /text\-decoration/.test(attrs.style) && (aNode = domUtils.findParentByTagName(elm, "a", true))) {
                    domUtils.setAttributes(aNode, attrs);
                    domUtils.remove(elm, true);
                    elm = aNode;
                } else {
                    domUtils.mergSibling(elm);
                    domUtils.clearEmptySibling(elm);
                }
                current = domUtils.getNextDomNode(elm, false, filterFn);
                domUtils.mergToParent(elm);
                if (node === end) {
                    break;
                }
            } else {
                current = domUtils.getNextDomNode(current, true, filterFn);
            }
        }
        return this.moveToBookmark(bookmark);
    },removeInlineStyle:function(tagName) {
        if (this.collapsed) {
            return this;
        }
        tagName = utils.isArray(tagName) ? tagName : [tagName];
        this.shrinkBoundary().adjustmentBoundary();
        var start = this.startContainer,end = this.endContainer;
        while (1) {
            if (start.nodeType == 1) {
                if (utils.indexOf(tagName, start.tagName.toLowerCase()) > -1) {
                    break;
                }
                if (start.tagName.toLowerCase() == "body") {
                    start = null;
                    break;
                }
            }
            start = start.parentNode;
        }
        while (1) {
            if (end.nodeType == 1) {
                if (utils.indexOf(tagName, end.tagName.toLowerCase()) > -1) {
                    break;
                }
                if (end.tagName.toLowerCase() == "body") {
                    end = null;
                    break;
                }
            }
            end = end.parentNode;
        }
        var bookmark = this.createBookmark(),frag,tmpRange;
        if (start) {
            tmpRange = this.cloneRange().setEndBefore(bookmark.start).setStartBefore(start);
            frag = tmpRange.extractContents();
            tmpRange.insertNode(frag);
            domUtils.clearEmptySibling(start, true);
            start.parentNode.insertBefore(bookmark.start, start);
        }
        if (end) {
            tmpRange = this.cloneRange().setStartAfter(bookmark.end).setEndAfter(end);
            frag = tmpRange.extractContents();
            tmpRange.insertNode(frag);
            domUtils.clearEmptySibling(end, false, true);
            end.parentNode.insertBefore(bookmark.end, end.nextSibling);
        }
        var current = domUtils.getNextDomNode(bookmark.start, false, function(node) {
            return node.nodeType == 1;
        }),next;
        while (current && current !== bookmark.end) {
            next = domUtils.getNextDomNode(current, true, function(node) {
                return node.nodeType == 1;
            });
            if (utils.indexOf(tagName, current.tagName.toLowerCase()) > -1) {
                domUtils.remove(current, true);
            }
            current = next;
        }
        return this.moveToBookmark(bookmark);
    },getClosedNode:function() {
        var node;
        if (!this.collapsed) {
            var range = this.cloneRange().adjustmentBoundary().shrinkBoundary();
            if (range.startContainer.nodeType == 1 && range.startContainer === range.endContainer && range.endOffset - range.startOffset == 1) {
                var child = range.startContainer.childNodes[range.startOffset];
                if (child && child.nodeType == 1 && dtd.$empty[child.tagName]) {
                    node = child;
                }
            }
        }
        return node;
    },select:browser.ie ? function(notInsertFillData) {
        var collapsed = this.collapsed,nativeRange;
        if (!collapsed) {
            this.shrinkBoundary();
        }
        var node = this.getClosedNode();
        if (node) {
            try {
                nativeRange = this.document.body.createControlRange();
                nativeRange.addElement(node);
                nativeRange.select();
            }
            catch(e) {
            }
            return this;
        }
        var bookmark = this.createBookmark(),start = bookmark.start,end;
        nativeRange = this.document.body.createTextRange();
        nativeRange.moveToElementText(start);
        nativeRange.moveStart("character", 1);
        if (!collapsed) {
            var nativeRangeEnd = this.document.body.createTextRange();
            end = bookmark.end;
            nativeRangeEnd.moveToElementText(end);
            nativeRange.setEndPoint("EndToEnd", nativeRangeEnd);
        } else {
            if (!notInsertFillData && this.startContainer.nodeType != 3) {
                var fillData = editor.fillData,tmpText,tmp = this.document.createElement("span");
                try {
                    if (fillData && fillData.parentNode && !fillData.nodeValue.replace(new RegExp(domUtils.fillChar, "g"), "").length) {
                        removeFillDataWithEmptyParentNode(fillData);
                    }
                }
                catch(e) {
                }
                tmpText = editor.fillData = this.document.createTextNode(fillChar);
                tmp.appendChild(this.document.createTextNode(fillChar));
                start.parentNode.insertBefore(tmp, start);
                start.parentNode.insertBefore(tmpText, start);
                nativeRange.moveStart("character", -1);
                nativeRange.collapse(true);
            }
        }
        this.moveToBookmark(bookmark);
        tmp && domUtils.remove(tmp);
        nativeRange.select();
        return this;
    } : function(notInsertFillData) {
        var win = domUtils.getWindow(this.document),sel = win.getSelection(),txtNode,child;
        browser.gecko ? this.document.body.focus() : win.focus();
        function mergSibling(node) {
            if (node && node.nodeType == 3 && !node.nodeValue.replace(new RegExp(domUtils.fillChar, "g"), "").length) {
                domUtils.remove(node);
            }
        }

        if (sel) {
            sel.removeAllRanges();
            if (this.collapsed && !notInsertFillData) {
                var fillData = editor.fillData;
                txtNode = this.document.createTextNode(fillChar);
                editor.fillData = txtNode;
                this.insertNode(txtNode);
                if (fillData && fillData.parentNode) {
                    if (!fillData.nodeValue.replace(new RegExp(domUtils.fillChar, "g"), "").length) {
                        removeFillDataWithEmptyParentNode(fillData);
                    } else {
                        fillData.nodeValue = fillData.nodeValue.replace(new RegExp(domUtils.fillChar, "g"), "");
                    }
                }
                mergSibling(txtNode.previousSibling);
                mergSibling(txtNode.nextSibling);
                this.setStart(txtNode, browser.webkit ? 1 : 0).collapse(true);
            }
            var nativeRange = this.document.createRange();
            nativeRange.setStart(this.startContainer, this.startOffset);
            nativeRange.setEnd(this.endContainer, this.endOffset);
            sel.addRange(nativeRange);
        }
        return this;
    },scrollToView:function(win, offset) {
        win = win ? window : domUtils.getWindow(this.document);
        var span = this.document.createElement("span");
        span.innerHTML = "&nbsp;";
        var tmpRange = this.cloneRange();
        tmpRange.insertNode(span);
        domUtils.scrollToView(span, win, offset);
        domUtils.remove(span);
        return this;
    }};
})();
baidu.editor.dom.Selection = baidu.editor.dom.Selection || {};
(function() {
    baidu.editor.dom.Selection = Selection;
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd,ie = baidu.editor.browser.ie;

    function getBoundaryInformation(range, start) {
        var getIndex = domUtils.getNodeIndex;
        range = range.duplicate();
        range.collapse(start);
        var parent = range.parentElement();
        if (!parent.hasChildNodes()) {
            return {container:parent,offset:0};
        }
        var siblings = parent.children,child,testRange = range.duplicate(),startIndex = 0,endIndex = siblings.length - 1,index = -1,distance;
        while (startIndex <= endIndex) {
            index = Math.floor((startIndex + endIndex) / 2);
            child = siblings[index];
            testRange.moveToElementText(child);
            var position = testRange.compareEndPoints("StartToStart", range);
            if (position > 0) {
                endIndex = index - 1;
            } else {
                if (position < 0) {
                    startIndex = index + 1;
                } else {
                    return {container:parent,offset:getIndex(child)};
                }
            }
        }
        if (index == -1) {
            testRange.moveToElementText(parent);
            testRange.setEndPoint("StartToStart", range);
            distance = testRange.text.replace(/(\r\n|\r)/g, "\n").length;
            siblings = parent.childNodes;
            if (!distance) {
                child = siblings[siblings.length - 1];
                return {container:child,offset:child.nodeValue.length};
            }
            var i = siblings.length;
            while (distance > 0) {
                distance -= siblings[--i].nodeValue.length;
            }
            return {container:siblings[i],offset:-distance};
        }
        testRange.collapse(position > 0);
        testRange.setEndPoint(position > 0 ? "StartToStart" : "EndToStart", range);
        distance = testRange.text.replace(/(\r\n|\r)/g, "\n").length;
        if (!distance) {
            return dtd.$empty[child.tagName] || dtd.$nonChild[child.tagName] ? {container:parent,offset:getIndex(child) + (position > 0 ? 0 : 1)} : {container:child,offset:position > 0 ? 0 : child.childNodes.length};
        }
        while (distance > 0) {
            try {
                var pre = child;
                child = child[position > 0 ? "previousSibling" : "nextSibling"];
                distance -= child.nodeValue.length;
            }
            catch(e) {
                return {container:parent,offset:getIndex(pre)};
            }
        }
        return {container:child,offset:position > 0 ? -distance : child.nodeValue.length + distance};
    }

    function transformIERangeToRange(ieRange, range) {
        if (ieRange.item) {
            range.selectNode(ieRange.item(0));
        } else {
            var bi = getBoundaryInformation(ieRange, true);
            range.setStart(bi.container, bi.offset);
            if (ieRange.compareEndPoints("StartToEnd", ieRange) != 0) {
                bi = getBoundaryInformation(ieRange, false);
                range.setEnd(bi.container, bi.offset);
            }
        }
        return range;
    }

    function _getIERange(sel) {
        var ieRange = sel.getNative().createRange();
        var el = ieRange.item ? ieRange.item(0) : ieRange.parentElement();
        if ((el.ownerDocument || el) === sel.document) {
            return ieRange;
        }
    }

    function Selection(doc) {
        var me = this,iframe;
        me.document = doc;
        if (ie) {
            iframe = domUtils.getWindow(doc).frameElement;
            domUtils.on(iframe, "beforedeactivate", function() {
                me._bakIERange = me.getIERange();
            });
            domUtils.on(iframe, "activate", function() {
                try {
                    if (!_getIERange(me) && me._bakIERange) {
                        me._bakIERange.select();
                    }
                }
                catch(ex) {
                }
                me._bakIERange = null;
            });
        }
        iframe = doc = null;
    }

    Selection.prototype = {getNative:function() {
        if (ie) {
            return this.document.selection;
        } else {
            return domUtils.getWindow(this.document).getSelection();
        }
    },getIERange:function() {
        var ieRange = _getIERange(this);
        if (!ieRange) {
            if (this._bakIERange) {
                return this._bakIERange;
            }
        }
        return ieRange;
    },cache:function() {
        this.clear();
        this._cachedRange = this.getRange();
        this._cachedStartElement = this.getStart();
    },clear:function() {
        this._cachedRange = this._cachedStartElement = null;
    },getRange:function() {
        var me = this;

        function optimze(range) {
            var child = me.document.body.firstChild,collapsed = range.collapsed;
            while (child && child.firstChild) {
                range.setStart(child, 0);
                child = child.firstChild;
            }
            if (!range.startContainer) {
                range.setStart(me.document.body, 0);
            }
            if (collapsed) {
                range.collapse(true);
            }
        }

        if (me._cachedRange != null) {
            return this._cachedRange;
        }
        var range = new baidu.editor.dom.Range(me.document);
        if (ie) {
            var nativeRange = me.getIERange();
            if (nativeRange) {
                transformIERangeToRange(nativeRange, range);
            } else {
                optimze(range);
            }
        } else {
            var sel = me.getNative();
            if (sel && sel.rangeCount) {
                var firstRange = sel.getRangeAt(0);
                var lastRange = sel.getRangeAt(sel.rangeCount - 1);
                range.setStart(firstRange.startContainer, firstRange.startOffset).setEnd(lastRange.endContainer, lastRange.endOffset);
                if (range.collapsed && domUtils.isBody(range.startContainer) && !range.startOffset) {
                    optimze(range);
                }
            } else {
                optimze(range);
            }
        }
        return range;
    },getStart:function() {
        if (this._cachedStartElement) {
            return this._cachedStartElement;
        }
        var range = ie ? this.getIERange() : this.getRange(),tmpRange,start,tmp,parent;
        if (ie) {
            if (!range) {
                return this.document.body.firstChild;
            }
            if (range.item) {
                return range.item(0);
            }
            tmpRange = range.duplicate();
            tmpRange.text.length > 0 && tmpRange.moveStart("character", 1);
            tmpRange.collapse(1);
            start = tmpRange.parentElement();
            parent = tmp = range.parentElement();
            while (tmp = tmp.parentNode) {
                if (tmp == start) {
                    start = parent;
                    break;
                }
            }
        } else {
            range.shrinkBoundary();
            start = range.startContainer;
            if (start.nodeType == 1 && start.hasChildNodes()) {
                start = start.childNodes[Math.min(start.childNodes.length - 1, range.startOffset)];
            }
            if (start.nodeType == 3) {
                return start.parentNode;
            }
        }
        return start;
    },getText:function() {
        var nativeSel = this.getNative(),nativeRange = baidu.editor.browser.ie ? nativeSel.createRange() : nativeSel.getRangeAt(0);
        return nativeRange.text || nativeRange.toString();
    }};
})();
(function() {
    baidu.editor.Editor = Editor;
    var editor = baidu.editor,utils = editor.utils,EventBase = editor.EventBase,domUtils = editor.dom.domUtils,Selection = editor.dom.Selection,ie = editor.browser.ie,uid = 0,browser = editor.browser,dtd = editor.dom.dtd;

    function Editor(options) {
        var me = this;
        me.uid = uid++;
        EventBase.call(me);
        me.commands = {};
        me.options = utils.extend(options || {}, UEDITOR_CONFIG, true);
        me.initPlugins();
    }

    Editor.prototype = {render:function(container) {
        if (container.constructor === String) {
            container = document.getElementById(container);
        }
        var iframeId = "baidu_editor_" + this.uid;
        container.innerHTML = "<iframe id=\"" + iframeId + "\"" + "width=\"100%\" height=\"100%\" scroll=\"no\" frameborder=\"0\"></iframe>";
        container.style.overflow = "hidden";
        var iframe = document.getElementById(iframeId),doc = iframe.contentWindow.document;
        this._setup(doc);
    },_setup:function(doc) {
        var options = this.options,me = this;
        doc.open();
        var html = (ie ? "" : "<!DOCTYPE html>") + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title>" + (options.iframeCssUrl ? "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + utils.unhtml(options.iframeCssUrl) + "\"/>" : "") + "<style type=\"text/css\">" + (ie && browser.version < 9 ? "body" : "html") + "{ word-wrap: break-word;word-break: break-all;cursor: text; height: 100%; font-size:" + this.options.defaultFontSize + ";font-family:" + this.options.defaultFontFamily + ";}\n" + (options.initialStyle || " ") + "</style></head><body></body></html>";
        doc.write(html);
        doc.close();
        if (ie) {
            doc.body.disabled = true;
            doc.body.contentEditable = true;
            doc.body.disabled = false;
        } else {
            doc.body.contentEditable = true;
            doc.body.spellcheck = false;
        }
        this.document = doc;
        this.window = doc.defaultView || doc.parentWindow;
        this.iframe = this.window.frameElement;
        if (this.options.minFrameHeight) {
            this.setHeight(this.options.minFrameHeight);
        }
        this.body = doc.body;
        this.selection = new Selection(doc);
        this._initEvents();
        if (this.options.initialContent) {
            if (me.options.autoClearinitialContent) {
                var oldExecCommand = me.execCommand;
                me.execCommand = function() {
                    me.fireEvent("firstBeforeExecCommand");
                    oldExecCommand.apply(me, arguments);
                };
                this.setDefaultContent(this.options.initialContent);
            } else {
                this.setContent(this.options.initialContent);
            }
        }
        for (var form = this.iframe.parentNode; !domUtils.isBody(form); form = form.parentNode) {
            if (form.tagName == "FORM") {
                domUtils.on(form, "submit", function() {
                    var textarea = document.getElementById("ueditor_textarea_" + me.options.textarea);
                    if (!textarea) {
                        textarea = document.createElement("textarea");
                        textarea.setAttribute("name", me.options.textarea);
                        textarea.id = "ueditor_textarea_" + me.options.textarea;
                        textarea.style.display = "none";
                        this.appendChild(textarea);
                    }
                    textarea.value = me.getContent();
                });
                break;
            }
        }
        if (domUtils.getChildCount(this.body, function(node) {
            return !domUtils.isBr(node);
        }) == 0) {
            this.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
        }
        if (me.options.focus) {
            var range = this.selection.getRange(),first = this.body.firstChild;
            setTimeout(function() {
                range.setStartBefore(first).setCursor(false, true);
                me._selectionChange();
            });
        }
        this.fireEvent("ready");
    },sync:function() {
        for (var form = this.iframe.parentNode; !domUtils.isBody(form); form = form.parentNode) {
            if (form.tagName == "FORM") {
                var textarea = document.getElementById("ueditor_textarea_" + this.options.textarea);
                if (!textarea) {
                    textarea = document.createElement("textarea");
                    textarea.setAttribute("name", this.options.textarea);
                    textarea.id = "ueditor_textarea_" + this.options.textarea;
                    textarea.style.display = "none";
                    form.appendChild(textarea);
                }
                textarea.value = this.getContent();
                break;
            }
        }
    },setHeight:function(height) {
        if (height !== parseInt(this.iframe.parentNode.style.height)) {
            this.iframe.parentNode.style.height = height + "px";
        }
    },getContent:function() {
        this.fireEvent("beforegetcontent");
        var reg = new RegExp(domUtils.fillChar, "g"),html = this.document.body.innerHTML.replace(reg, "");
        this.fireEvent("aftergetcontent");
        if (this.serialize) {
            var node = this.serialize.parseHTML(html);
            node = this.serialize.transformOutput(node);
            html = this.serialize.toHTML(node);
        }
        return html;
    },getContentTxt:function() {
        var reg,space_str;
        space_str = browser.ie ? "\r\t\n" : "&#nbsp;\r\t\n";
        reg = new RegExp("[" + space_str + domUtils.fillChar + "]", "g");
        return this.document.body[browser.ie ? "innerText" : "textContent"].replace(reg, "");
    },setContent:function(html) {
        var me = this;
        me.fireEvent("beforesetcontent");
        var serialize = this.serialize;
        if (serialize) {
            var node = serialize.parseHTML(html);
            node = serialize.transformInput(node);
            node = serialize.filter(node);
            html = serialize.toHTML(node);
        }
        this.document.body.innerHTML = html;
        if (me.options.enterTag == "p") {
            var child = this.body.firstChild,p = me.document.createElement("p"),tmpNode;
            while (child) {
                if (child.nodeType == 3 || child.nodeType == 1 && dtd.p[child.tagName]) {
                    tmpNode = child.nextSibling;
                    p.appendChild(child);
                    child = tmpNode;
                    if (!child) {
                        me.body.appendChild(p);
                    }
                } else {
                    if (p.firstChild) {
                        me.body.insertBefore(p, child);
                        p = me.document.createElement("p");
                    }
                    child = child.nextSibling;
                }
            }
        }
        me.adjustTable && me.adjustTable(me.body);
        me.fireEvent("aftersetcontent");
        me._selectionChange();
    },focus:function() {
        domUtils.getWindow(this.document).focus();
    },initPlugins:function(plugins) {
        var fn,originals = baidu.editor.plugins;
        if (plugins) {
            for (var i = 0,pi; pi = plugins[i++];) {
                if (utils.indexOf(this.options.plugins, pi) == -1 && (fn = baidu.editor.plugins[pi])) {
                    this.options.plugins.push(pi);
                    fn.call(this);
                }
            }
        } else {
            plugins = this.options.plugins;
            if (plugins) {
                for (i = 0; pi = originals[plugins[i++]];) {
                    pi.call(this);
                }
            } else {
                this.options.plugins = [];
                for (pi in originals) {
                    this.options.plugins.push(pi);
                    originals[pi].call(this);
                }
            }
        }
    },_initEvents:function() {
        var me = this,doc = this.document,win = domUtils.getWindow(doc);
        me._proxyDomEvent = utils.bind(me._proxyDomEvent, me);
        domUtils.on(doc, ["click","contextmenu","mousedown","keydown","keyup","keypress","mouseup","mouseover","mouseout","selectstart"], me._proxyDomEvent);
        domUtils.on(win, ["focus","blur"], me._proxyDomEvent);
        domUtils.on(doc, ["mouseup","keydown"], function(evt) {
            if (evt.type == "keydown" && (evt.ctrlKey || evt.metaKey || evt.shiftKey || evt.altKey)) {
                return;
            }
            if (evt.button == 2) {
                return;
            }
            me._selectionChange(250, true);
        });
        var innerDrag = 0,source = browser.ie ? me.body : me.document,dragoverHandler;
        domUtils.on(source, "dragstart", function() {
            innerDrag = 1;
        });
        domUtils.on(source, browser.webkit ? "dragover" : "drop", function() {
            return browser.webkit ? function() {
                clearTimeout(dragoverHandler);
                dragoverHandler = setTimeout(function() {
                    if (!innerDrag) {
                        var sel = me.selection,range = sel.getRange();
                        if (range) {
                            var common = range.getCommonAncestor();
                            if (common && me.serialize) {
                                var f = me.serialize,node = f.filter(f.transformInput(f.parseHTML(f.word(common.innerHTML))));
                                common.innerHTML = f.toHTML(node);
                            }
                        }
                    }
                    innerDrag = 0;
                }, 200);
            } : function(e) {
                if (!innerDrag) {
                    e.preventDefault ? e.preventDefault() : (e.returnValue = false);
                }
                innerDrag = 0;
            };
        }());
    },_proxyDomEvent:function(evt) {
        return this.fireEvent(evt.type.replace(/^on/, ""), evt);
    },_selectionChange:function(delay, byUser) {
        var me = this;
        clearTimeout(this._selectionChangeTimer);
        this._selectionChangeTimer = setTimeout(function() {
            me.selection.cache();
            if (me.selection._cachedRange && me.selection._cachedStartElement) {
                me.fireEvent("beforeselectionchange");
                me.fireEvent("selectionchange", byUser);
                me.selection.clear();
            }
        }, delay || 50);
    },_callCmdFn:function(fnName, args) {
        var cmdName = args[0].toLowerCase(),cmd,cmdFn;
        cmdFn = (cmd = this.commands[cmdName]) && cmd[fnName] || (cmd = baidu.editor.commands[cmdName]) && cmd[fnName];
        if (cmd && !cmdFn && fnName == "queryCommandState") {
            return false;
        } else {
            if (cmdFn) {
                return cmdFn.apply(this, args);
            }
        }
    },execCommand:function(cmdName) {
        cmdName = cmdName.toLowerCase();
        var me = this,result,cmd = me.commands[cmdName] || baidu.editor.commands[cmdName];
        if (!cmd || !cmd.execCommand) {
            return;
        }
        if (!cmd.notNeedUndo && !me.__hasEnterExecCommand) {
            me.__hasEnterExecCommand = true;
            me.fireEvent("beforeexeccommand", cmdName);
            result = this._callCmdFn("execCommand", arguments);
            me.fireEvent("afterexeccommand", cmdName);
            me.__hasEnterExecCommand = false;
        } else {
            result = this._callCmdFn("execCommand", arguments);
        }
        me._selectionChange();
        return result;
    },queryCommandState:function(cmdName) {
        return this._callCmdFn("queryCommandState", arguments);
    },queryCommandValue:function(cmdName) {
        return this._callCmdFn("queryCommandValue", arguments);
    },hasContents:function() {
        var cont = this.body[browser.ie ? "innerText" : "textContent"],reg = new RegExp("[ \t\n\r" + domUtils.fillChar + "]", "g");
        return !!cont.replace(reg, "").length;
    },reset:function() {
        this.fireEvent("reset");
    },setDefaultContent:function() {
        function clear(type) {
            var me = this;
            if (me.document.getElementById("initContent")) {
                me.document.body.innerHTML = "<p>" + (baidu.editor.browser.ie ? "" : "<br/>") + "</p>";
                var range = me.selection.getRange();
                me.removeListener("firstBeforeExecCommand", clear);
                me.removeListener("focus", clear);
                setTimeout(function() {
                    range.setStart(me.document.body.firstChild, 0).collapse(true).select(true);
                    me._selectionChange();
                });
            }
        }

        return function(cont) {
            var me = this;
            me.document.body.innerHTML = "<p id=\"initContent\">" + cont + "</p>";
            me.addListener("firstBeforeExecCommand", clear);
            me.addListener("mousedown", clear);
        };
    }()};
    utils.inherits(Editor, EventBase);
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd,utils = baidu.editor.utils,browser = baidu.editor.browser;
    baidu.editor.commands["inserthtml"] = {execCommand:function(command, html) {
        var editor = this,range,deletedElms,i,ci,div,tds = editor.currentSelectedArr;
        range = editor.selection.getRange();
        div = range.document.createElement("div");
        div.style.display = "inline";
        div.innerHTML = utils.trim(html);
        try {
            editor.adjustTable && editor.adjustTable(div);
        }
        catch(e) {
        }
        if (tds && tds.length) {
            for (var i = 0,ti; ti = tds[i++];) {
                ti.className = "";
            }
            tds[0].innerHTML = "";
            range.setStart(tds[0], 0).collapse(true);
            editor.currentSelectedArr = [];
        }
        if (!range.collapsed) {
            range.deleteContents();
            if (range.startContainer.nodeType == 1) {
                var child = range.startContainer.childNodes[range.startOffset],pre;
                if (child && domUtils.isBlockElm(child) && (pre = child.previousSibling) && domUtils.isBlockElm(pre)) {
                    range.setEnd(pre, pre.childNodes.length).collapse();
                    while (child.firstChild) {
                        pre.appendChild(child.firstChild);
                    }
                    domUtils.remove(child);
                }
            }
        }
        var child,parent,pre,tmp,hadBreak = 0;
        while (child = div.firstChild) {
            range.insertNode(child);
            if (!hadBreak && child.nodeType == domUtils.NODE_ELEMENT && domUtils.isBlockElm(child)) {
                parent = domUtils.findParent(child, function(node) {
                    return domUtils.isBlockElm(node);
                });
                if (parent && parent.tagName.toLowerCase != "body" && !(dtd[parent.tagName][child.nodeName] && child.parentNode === parent)) {
                    if (!dtd[parent.tagName][child.nodeName]) {
                        pre = parent;
                    } else {
                        tmp = child.parentNode;
                        while (tmp !== parent) {
                            pre = tmp;
                            tmp = tmp.parentNode;
                        }
                    }
                    domUtils.breakParent(child, pre || tmp);
                    var pre = child.previousSibling;
                    domUtils.trimWhiteTextNode(pre);
                    if (!pre.childNodes.length) {
                        domUtils.remove(pre);
                    }
                    hadBreak = 1;
                }
            }
            var next = child.nextSibling;
            if (!div.firstChild && next && domUtils.isBlockElm(next)) {
                range.setStart(next, 0).collapse(true);
                break;
            }
            range.setEndAfter(child).collapse();
        }
        child = range.startContainer;
        if (domUtils.isBlockElm(child) && domUtils.isEmptyNode(child)) {
            child.innerHTML = baidu.editor.browser.ie ? "" : "<br/>";
        }
        range.select(true);
        setTimeout(function() {
            range.scrollToView(editor.autoHeightEnabled, editor.autoHeightEnabled ? domUtils.getXY(editor.iframe).y : 0);
        }, 200);
    }};
}());
(function() {
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands["insertimage"] = {execCommand:function(cmd, opt) {
        var range = this.selection.getRange(),img = range.getClosedNode();
        if (img && /img/ig.test(img.tagName)) {
            if (img.className != "edui-faked-video") {
                domUtils.setAttributes(img, opt);
            }
        } else {
            var str = "<img ",o;
            for (o in opt) {
                str += o + "='" + opt[o] + "' ";
            }
            str += "/>";
            this.execCommand("inserthtml", str);
        }
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,block = domUtils.isBlockElm,defaultValue = {left:1,right:1,center:1,justify:1},utils = baidu.editor.utils,doJustify = function(range, style) {
        var bookmark = range.createBookmark(),filterFn = function(node) {
            return node.nodeType == 1 ? node.tagName.toLowerCase() != "br" && !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace(node);
        };
        range.enlarge(true);
        var bookmark2 = range.createBookmark(),current = domUtils.getNextDomNode(bookmark2.start, false, filterFn),tmpRange = range.cloneRange(),tmpNode;
        while (current && !(domUtils.getPosition(current, bookmark2.end) & domUtils.POSITION_FOLLOWING)) {
            if (current.nodeType == 3 || !block(current)) {
                tmpRange.setStartBefore(current);
                while (current && current !== bookmark2.end && !block(current)) {
                    tmpNode = current;
                    current = domUtils.getNextDomNode(current, false, null, function(node) {
                        return !block(node);
                    });
                }
                tmpRange.setEndAfter(tmpNode);
                var common = tmpRange.getCommonAncestor();
                if (!domUtils.isBody(common) && block(common)) {
                    domUtils.setStyles(common, utils.isString(style) ? {"text-align":style} : style);
                    current = common;
                } else {
                    var p = range.document.createElement("p");
                    domUtils.setStyles(p, utils.isString(style) ? {"text-align":style} : style);
                    var frag = tmpRange.extractContents();
                    p.appendChild(frag);
                    tmpRange.insertNode(p);
                    current = p;
                }
                current = domUtils.getNextDomNode(current, false, filterFn);
            } else {
                current = domUtils.getNextDomNode(current, true, filterFn);
            }
        }
        return range.moveToBookmark(bookmark2).moveToBookmark(bookmark);
    };
    baidu.editor.commands["justify"] = {execCommand:function(cmdName, align) {
        var range = new baidu.editor.dom.Range(this.document);
        if (this.currentSelectedArr && this.currentSelectedArr.length > 0) {
            for (var i = 0,ti; ti = this.currentSelectedArr[i++];) {
                doJustify(range.selectNode(ti), align);
            }
            range.selectNode(this.currentSelectedArr[0]).select();
        } else {
            range = this.selection.getRange();
            if (range.collapsed) {
                var txt = this.document.createTextNode("p");
                range.insertNode(txt);
            }
            doJustify(range, align);
            if (txt) {
                range.setStartBefore(txt).collapse(true);
                domUtils.remove(txt);
            }
            range.select();
        }
        return true;
    },queryCommandValue:function() {
        var startNode = this.selection.getStart(),value = domUtils.getComputedStyle(startNode, "text-align");
        return defaultValue[value] ? value : "left";
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,fonts = {"forecolor":"color","backcolor":"background-color","fontsize":"font-size","fontfamily":"font-family","underline":"text-decoration","strikethrough":"text-decoration"},reg = new RegExp(domUtils.fillChar, "g"),browser = baidu.editor.browser,flag = 0;
    for (var p in fonts) {
        (function(cmd, style) {
            baidu.editor.commands[cmd] = {execCommand:function(cmdName, value) {
                value = value || (this.queryCommandState(cmdName) ? "none" : cmdName == "underline" ? "underline" : "line-through");
                var me = this,range = this.selection.getRange(),text;
                if (!flag) {
                    me.addListener("beforegetcontent", function() {
                        domUtils.clearReduent(me.document, ["span"]);
                    });
                    flag = 1;
                }
                if (value == "default") {
                    if (range.collapsed) {
                        text = me.document.createTextNode("font");
                        range.insertNode(text).select();
                    }
                    me.execCommand("removeFormat", "span,a", style);
                    if (text) {
                        range.setStartBefore(text).setCursor();
                        domUtils.remove(text);
                    }
                } else {
                    if (me.currentSelectedArr && me.currentSelectedArr.length > 0) {
                        for (var i = 0,ci; ci = me.currentSelectedArr[i++];) {
                            range.selectNodeContents(ci);
                            range.applyInlineStyle("span", {"style":style + ":" + value});
                        }
                        range.selectNodeContents(this.currentSelectedArr[0]).select();
                    } else {
                        if (!range.collapsed) {
                            if ((cmd == "underline" || cmd == "strikethrough") && me.queryCommandValue(cmd)) {
                                me.execCommand("removeFormat", "span,a", style);
                            }
                            range = me.selection.getRange();
                            range.applyInlineStyle("span", {"style":style + ":" + value}).select();
                        } else {
                            var span = domUtils.findParentByTagName(range.startContainer, "span", true);
                            text = me.document.createTextNode("font");
                            if (span && !span.children.length && !span[browser.ie ? "innerText" : "textContent"].replace(reg, "").length) {
                                range.insertNode(text);
                                if (cmd == "underline" || cmd == "strikethrough") {
                                    range.selectNode(text).select();
                                    me.execCommand("removeFormat", "span,a", style, null);
                                    span = domUtils.findParentByTagName(text, "span", true);
                                    range.setStartBefore(text);
                                }
                                span.style.cssText = span.style.cssText + ";" + style + ":" + value;
                                range.collapse(true).select();
                            } else {
                                range.insertNode(text);
                                range.selectNode(text).select();
                                span = range.document.createElement("span");
                                if (cmd == "underline" || cmd == "strikethrough") {
                                    if (domUtils.findParentByTagName(text, "a", true)) {
                                        range.setStartBefore(text).setCursor();
                                        domUtils.remove(text);
                                        return;
                                    }
                                    me.execCommand("removeFormat", "span,a", style);
                                }
                                span.style.cssText = style + ":" + value;
                                text.parentNode.insertBefore(span, text);
                                if (!browser.ie) {
                                    var spanParent = span.parentNode;
                                    while (!domUtils.isBlockElm(spanParent)) {
                                        if (spanParent.tagName == "SPAN") {
                                            span.style.cssText = spanParent.style.cssText + span.style.cssText;
                                        }
                                        spanParent = spanParent.parentNode;
                                    }
                                }
                                range.setStart(span, 0).setCursor();
                            }
                            domUtils.remove(text);
                        }
                    }
                }
                return true;
            },queryCommandValue:function(cmdName) {
                var startNode = this.selection.getStart();
                if (cmdName == "underline" || cmdName == "strikethrough") {
                    var tmpNode = startNode,value;
                    while (tmpNode && !domUtils.isBlockElm(tmpNode) && !domUtils.isBody(tmpNode)) {
                        if (tmpNode.nodeType == 1) {
                            value = domUtils.getComputedStyle(tmpNode, style);
                            if (value != "none") {
                                return value;
                            }
                        }
                        tmpNode = tmpNode.parentNode;
                    }
                    return "none";
                }
                return domUtils.getComputedStyle(startNode, style);
            },queryCommandState:function(cmdName) {
                if (!(cmdName == "underline" || cmdName == "strikethrough")) {
                    return 0;
                }
                return this.queryCommandValue(cmdName) == (cmdName == "underline" ? "underline" : "line-through");
            }};
        })(p, fonts[p]);
    }
})();
(function() {
    var dom = baidu.editor.dom,domUtils = dom.domUtils,browser = baidu.editor.browser;

    function optimize(range) {
        var start = range.startContainer,end = range.endContainer;
        if (start = domUtils.findParentByTagName(start, "a", true)) {
            range.setStartBefore(start);
        }
        if (end = domUtils.findParentByTagName(end, "a", true)) {
            range.setEndAfter(end);
        }
    }

    baidu.editor.commands["unlink"] = {execCommand:function() {
        var as,range = new baidu.editor.dom.Range(this.document),tds = this.currentSelectedArr,bookmark;
        if (tds && tds.length > 0) {
            for (var i = 0,ti; ti = tds[i++];) {
                as = domUtils.getElementsByTagName(ti, "a");
                for (var j = 0,aj; aj = as[j++];) {
                    domUtils.remove(aj, true);
                }
            }
            if (domUtils.isEmptyNode(tds[0])) {
                range.setStart(tds[0], 0).setCursor();
            } else {
                range.selectNodeContents(tds[0]).select();
            }
        } else {
            range = this.selection.getRange();
            if (range.collapsed && !domUtils.findParentByTagName(range.startContainer, "a", true)) {
                return;
            }
            bookmark = range.createBookmark();
            optimize(range);
            range.removeInlineStyle("a").moveToBookmark(bookmark).select();
        }
    },queryCommandState:function() {
        return this.queryCommandValue("link") ? 0 : -1;
    }};
    function doLink(range, opt) {
        optimize(range = range.adjustmentBoundary());
        var start = range.startContainer;
        if (start.nodeType == 1) {
            start = start.childNodes[range.startOffset];
            if (start && start.nodeType == 1 && start.tagName == "A" && /^(?:https?|ftp|file)\s*:\s*\/\//.test(start[browser.ie ? "innerText" : "textContent"])) {
                start.innerHTML = opt.href;
            }
        }
        range.removeInlineStyle("a");
        if (range.collapsed) {
            var a = range.document.createElement("a");
            domUtils.setAttributes(a, opt);
            a.innerHTML = opt.href;
            range.insertNode(a).selectNode(a);
        } else {
            range.applyInlineStyle("a", opt);
        }
    }

    baidu.editor.commands["link"] = {execCommand:function(cmdName, opt) {
        var range = new baidu.editor.dom.Range(this.document),tds = this.currentSelectedArr;
        if (tds && tds.length) {
            for (var i = 0,ti; ti = tds[i++];) {
                if (domUtils.isEmptyNode(ti)) {
                    ti.innerHTML = opt.href;
                }
                doLink(range.selectNodeContents(ti), opt);
            }
            range.selectNodeContents(tds[0]).select();
        } else {
            doLink(range = this.selection.getRange(), opt);
            range.collapse().select(browser.gecko ? true : false);
        }
    },queryCommandValue:function() {
        var range = new baidu.editor.dom.Range(this.document),tds = this.currentSelectedArr,as,node;
        if (tds && tds.length) {
            for (var i = 0,ti; ti = tds[i++];) {
                as = ti.getElementsByTagName("a");
                if (as[0]) {
                    return as[0];
                }
            }
        } else {
            range = this.selection.getRange();
            if (range.collapsed) {
                node = this.selection.getStart();
                if (node && (node = domUtils.findParentByTagName(node, "a", true))) {
                    return node;
                }
            } else {
                range.shrinkBoundary();
                var start = range.startContainer.nodeType == 3 || !range.startContainer.childNodes[range.startOffset] ? range.startContainer : range.startContainer.childNodes[range.startOffset],end = range.endContainer.nodeType == 3 || range.endOffset == 0 ? range.endContainer : range.endContainer.childNodes[range.endOffset - 1],common = range.getCommonAncestor();
                node = domUtils.findParentByTagName(common, "a", true);
                if (!node && common.nodeType == 1) {
                    var as = common.getElementsByTagName("a"),ps,pe;
                    for (var i = 0,ci; ci = as[i++];) {
                        ps = domUtils.getPosition(ci, start),pe = domUtils.getPosition(ci, end);
                        if ((ps & domUtils.POSITION_FOLLOWING || ps & domUtils.POSITION_CONTAINS) && (pe & domUtils.POSITION_PRECEDING || pe & domUtils.POSITION_CONTAINS)) {
                            node = ci;
                            break;
                        }
                    }
                }
                return node;
            }
        }
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd;
    baidu.editor.commands["removeformat"] = {execCommand:function(cmdName, tags, style, attrs, notIncludeA) {
        var tagReg = new RegExp("^(?:" + (tags || this.options.removeFormatTags).replace(/,/g, "|") + ")$", "i"),removeFormatAttributes = style ? [] : (attrs || this.options.removeFormatAttributes).split(","),range = new baidu.editor.dom.Range(this.document),bookmark,node,parent,filter = function(node) {
            return node.nodeType == 1;
        };

        function doRemove(range) {
            var bookmark1 = range.createBookmark();
            if (range.collapsed) {
                range.enlarge(true);
            }
            if (!notIncludeA) {
                var aNode = domUtils.findParentByTagName(range.startContainer, "a", true);
                if (aNode) {
                    range.setStartBefore(aNode);
                }
                aNode = domUtils.findParentByTagName(range.endContainer, "a", true);
                if (aNode) {
                    range.setEndAfter(aNode);
                }
            }
            bookmark = range.createBookmark();
            node = bookmark.start;
            while ((parent = node.parentNode) && !domUtils.isBlockElm(parent)) {
                domUtils.breakParent(node, parent);
                domUtils.clearEmptySibling(node);
            }
            if (bookmark.end) {
                node = bookmark.end;
                while ((parent = node.parentNode) && !domUtils.isBlockElm(parent)) {
                    domUtils.breakParent(node, parent);
                    domUtils.clearEmptySibling(node);
                }
                var current = domUtils.getNextDomNode(bookmark.start, false, filter),next;
                while (current) {
                    if (current == bookmark.end) {
                        break;
                    }
                    next = domUtils.getNextDomNode(current, true, filter);
                    if (!dtd.$empty[current.tagName.toLowerCase()] && !domUtils.isBookmarkNode(current)) {
                        if (tagReg.test(current.tagName)) {
                            if (style) {
                                domUtils.removeStyle(current, style);
                                if (domUtils.isRedundantSpan(current) && style != "text-decoration") {
                                    domUtils.remove(current, true);
                                }
                            } else {
                                domUtils.remove(current, true);
                            }
                        } else {
                            if (!dtd.$tableContent[current.tagName] && !dtd.$list[current.tagName]) {
                                domUtils.removeAttributes(current, removeFormatAttributes);
                                if (domUtils.isRedundantSpan(current)) {
                                    domUtils.remove(current, true);
                                }
                            }
                        }
                    }
                    current = next;
                }
            }
            var pN = bookmark.start.parentNode;
            if (domUtils.isBlockElm(pN) && !dtd.$tableContent[pN.tagName] && !dtd.$list[pN.tagName]) {
                domUtils.removeAttributes(pN, removeFormatAttributes);
            }
            pN = bookmark.end.parentNode;
            if (bookmark.end && domUtils.isBlockElm(pN) && !dtd.$tableContent[pN.tagName] && !dtd.$list[pN.tagName]) {
                domUtils.removeAttributes(pN, removeFormatAttributes);
            }
            range.moveToBookmark(bookmark).moveToBookmark(bookmark1);
            var node = range.startContainer,tmp,collapsed = range.collapsed;
            while (node.nodeType == 1 && domUtils.isEmptyNode(node) && dtd.$removeEmpty[node.tagName]) {
                tmp = node.parentNode;
                range.setStartBefore(node);
                if (range.startContainer === range.endContainer) {
                    range.endOffset--;
                }
                domUtils.remove(node);
                node = tmp;
            }
            if (!collapsed) {
                node = range.endContainer;
                while (node.nodeType == 1 && domUtils.isEmptyNode(node) && dtd.$removeEmpty[node.tagName]) {
                    tmp = node.parentNode;
                    range.setEndBefore(node);
                    domUtils.remove(node);
                    node = tmp;
                }
            }
        }

        if (this.currentSelectedArr && this.currentSelectedArr.length) {
            for (var i = 0,ci; ci = this.currentSelectedArr[i++];) {
                range.selectNodeContents(ci);
                doRemove(range);
            }
            range.selectNodeContents(this.currentSelectedArr[0]).select();
        } else {
            range = this.selection.getRange();
            doRemove(range);
            range.select();
        }
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd,getObj = function(editor) {
        var startNode = editor.selection.getStart();
        return domUtils.findParentByTagName(startNode, "blockquote", true);
    };
    baidu.editor.commands["blockquote"] = {execCommand:function(cmdName, attrs) {
        var range = this.selection.getRange(),obj = getObj(this),blockquote = dtd.blockquote,bookmark = range.createBookmark(),tds = this.currentSelectedArr;
        if (obj) {
            if (tds && tds.length) {
                domUtils.remove(obj, true);
            } else {
                var start = range.startContainer,startBlock = domUtils.isBlockElm(start) ? start : domUtils.findParent(start, function(node) {
                    return domUtils.isBlockElm(node);
                }),end = range.endContainer,endBlock = domUtils.isBlockElm(end) ? end : domUtils.findParent(end, function(node) {
                    return domUtils.isBlockElm(node);
                });
                startBlock = domUtils.findParentByTagName(startBlock, "li", true) || startBlock;
                endBlock = domUtils.findParentByTagName(endBlock, "li", true) || endBlock;
                if (startBlock.tagName == "LI" || startBlock.tagName == "TD" || startBlock === obj) {
                    domUtils.remove(obj, true);
                } else {
                    domUtils.breakParent(startBlock, obj);
                }
                if (startBlock !== endBlock) {
                    obj = domUtils.findParentByTagName(endBlock, "blockquote");
                    if (obj) {
                        if (endBlock.tagName == "LI" || endBlock.tagName == "TD") {
                            domUtils.remove(obj, true);
                        } else {
                            domUtils.breakParent(endBlock, obj);
                        }
                    }
                }
                var blockquotes = domUtils.getElementsByTagName(this.document, "blockquote");
                for (var i = 0,bi; bi = blockquotes[i++];) {
                    if (!bi.childNodes.length) {
                        domUtils.remove(bi);
                    } else {
                        if (domUtils.getPosition(bi, startBlock) & domUtils.POSITION_FOLLOWING && domUtils.getPosition(bi, endBlock) & domUtils.POSITION_PRECEDING) {
                            domUtils.remove(bi, true);
                        }
                    }
                }
            }
        } else {
            var tmpRange = range.cloneRange(),node = tmpRange.startContainer.nodeType == 1 ? tmpRange.startContainer : tmpRange.startContainer.parentNode,preNode = node,doEnd = 1;
            while (1) {
                if (domUtils.isBody(node)) {
                    if (preNode !== node) {
                        if (range.collapsed) {
                            tmpRange.selectNode(preNode);
                            doEnd = 0;
                        } else {
                            tmpRange.setStartBefore(preNode);
                        }
                    } else {
                        tmpRange.setStart(node, 0);
                    }
                    break;
                }
                if (!blockquote[node.tagName]) {
                    if (range.collapsed) {
                        tmpRange.selectNode(preNode);
                    } else {
                        tmpRange.setStartBefore(preNode);
                    }
                    break;
                }
                preNode = node;
                node = node.parentNode;
            }
            if (doEnd) {
                preNode = node = node = tmpRange.endContainer.nodeType == 1 ? tmpRange.endContainer : tmpRange.endContainer.parentNode;
                while (1) {
                    if (domUtils.isBody(node)) {
                        if (preNode !== node) {
                            tmpRange.setEndAfter(preNode);
                        } else {
                            tmpRange.setEnd(node, node.childNodes.length);
                        }
                        break;
                    }
                    if (!blockquote[node.tagName]) {
                        tmpRange.setEndAfter(preNode);
                        break;
                    }
                    preNode = node;
                    node = node.parentNode;
                }
            }
            node = range.document.createElement("blockquote");
            domUtils.setAttributes(node, attrs);
            node.appendChild(tmpRange.extractContents());
            tmpRange.insertNode(node);
            var childs = domUtils.getElementsByTagName(node, "blockquote");
            for (var i = 0,ci; ci = childs[i++];) {
                if (ci.parentNode) {
                    domUtils.remove(ci, true);
                }
            }
        }
        range.moveToBookmark(bookmark).select();
    },queryCommandState:function() {
        return getObj(this) ? 1 : 0;
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands["outdent"] = baidu.editor.commands["indent"] = {execCommand:function(cmd) {
        var me = this,value = cmd.toLowerCase() == "outdent" ? "0em" : (me.options.indentValue || "2em");
        me.execCommand("Paragraph", "p", {"textIndent":value});
    }};
})();
(function() {
    baidu.editor.commands["print"] = {execCommand:function() {
        this.window.print();
    },notNeedUndo:1};
})();
baidu.editor.commands["preview"] = {execCommand:function() {
    var me = this;
    var w = window.open("", "_blank", "");
    var d = w.document,utils = baidu.editor.utils;
    d.open();
    d.write("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"" + utils.unhtml(this.options.iframeCssUrl) + "\"/><title></title></head><body>" + me.getContent() + "</body></html>");
    d.close();
},notNeedUndo:1};
(function() {
    var browser = baidu.editor.browser;
    baidu.editor.commands["selectall"] = {execCommand:function() {
        this.document.execCommand("selectAll", false, null);
        !browser.ie && this.focus();
    },notNeedUndo:1};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,block = domUtils.isBlockElm,notExchange = ["TD","LI","PRE"],utils = baidu.editor.utils,browser = baidu.editor.browser,doParagraph = function(range, style, attrs) {
        var bookmark = range.createBookmark(),filterFn = function(node) {
            return node.nodeType == 1 ? node.tagName.toLowerCase() != "br" && !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace(node);
        },para;
        range.enlarge(true);
        var bookmark2 = range.createBookmark(),current = domUtils.getNextDomNode(bookmark2.start, false, filterFn),tmpRange = range.cloneRange(),tmpNode;
        while (current && !(domUtils.getPosition(current, bookmark2.end) & domUtils.POSITION_FOLLOWING)) {
            if (current.nodeType == 3 || !block(current)) {
                tmpRange.setStartBefore(current);
                while (current && current !== bookmark2.end && !block(current)) {
                    tmpNode = current;
                    current = domUtils.getNextDomNode(current, false, null, function(node) {
                        return !block(node);
                    });
                }
                tmpRange.setEndAfter(tmpNode);
                para = range.document.createElement(style);
                if (attrs) {
                    for (var pro in attrs) {
                        para.style[pro] = attrs[pro];
                    }
                }
                para.appendChild(tmpRange.extractContents());
                tmpRange.insertNode(para);
                var parent = para.parentNode;
                if (block(parent) && !domUtils.isBody(para.parentNode) && utils.indexOf(notExchange, parent.tagName) == -1) {
                    parent.getAttribute("dir") && para.setAttribute("dir", parent.getAttribute("dir"));
                    parent.style.cssText && (para.style.cssText = parent.style.cssText + ";" + para.style.cssText);
                    parent.style.textAlign && !para.style.textAlign && (para.style.textAlign = parent.style.textAlign);
                    parent.style.textIndent && !para.style.textIndent && (para.style.textIndent = parent.style.textIndent);
                    parent.style.padding && !para.style.padding && (para.style.padding = parent.style.padding);
                    if (attrs && /h\d/i.test(parent.tagName)) {
                        for (var pro in attrs) {
                            parent.style[pro] = attrs[pro];
                        }
                        domUtils.remove(para, true);
                        para = parent;
                    } else {
                        domUtils.remove(para.parentNode, true);
                    }
                }
                if (utils.indexOf(notExchange, parent.tagName) != -1) {
                    current = parent;
                } else {
                    current = para;
                }
                current = domUtils.getNextDomNode(current, false, filterFn);
            } else {
                current = domUtils.getNextDomNode(current, true, filterFn);
            }
        }
        return range.moveToBookmark(bookmark2).moveToBookmark(bookmark);
    };
    baidu.editor.commands["paragraph"] = {execCommand:function(cmdName, style, attrs) {
        var range = new baidu.editor.dom.Range(this.document);
        if (this.currentSelectedArr && this.currentSelectedArr.length > 0) {
            for (var i = 0,ti; ti = this.currentSelectedArr[i++];) {
                if (ti.style.display == "none") {
                    continue;
                }
                if (!domUtils.getChildCount(ti, function(node) {
                    return !domUtils.isBr(node) && !domUtils.isWhitespace(node);
                })) {
                    var tmpTxt = this.document.createTextNode("paragraph");
                    ti.insertBefore(tmpTxt, ti.firstChild);
                }
                doParagraph(range.selectNodeContents(ti), style, attrs);
                if (tmpTxt) {
                    var pN = tmpTxt.parentNode;
                    domUtils.remove(tmpTxt);
                    if (browser.ie && !pN.firstChild) {
                        pN.innerHTML = "&nbsp;";
                    }
                }
            }
            range.selectNode(this.currentSelectedArr[0]).select();
        } else {
            range = this.selection.getRange();
            if (range.collapsed) {
                var txt = this.document.createTextNode("p");
                range.insertNode(txt);
            }
            range = doParagraph(range, style, attrs);
            if (txt) {
                range.setStartBefore(txt).collapse(true);
                domUtils.remove(txt);
            }
            if (browser.gecko && range.collapsed && range.startContainer.nodeType == 1) {
                var child = range.startContainer.childNodes[range.startOffset];
                if (child && child.nodeType == 1 && child.tagName.toLowerCase() == style) {
                    range.setStart(child, 0).collapse(true);
                }
            }
            range.select();
        }
        return true;
    },queryCommandValue:function() {
        var startNode = this.selection.getStart(),parent = domUtils.findParentByTagName(startNode, ["p","h1","h2","h3","h4","h5","h6"], true);
        return parent && parent.tagName.toLowerCase();
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,block = domUtils.isBlockElm,getObj = function(editor) {
        var startNode = editor.selection.getStart(),parents;
        if (startNode) {
            parents = domUtils.findParents(startNode, true, block, true);
            for (var i = 0,ci; ci = parents[i++];) {
                if (ci.getAttribute("dir")) {
                    return ci;
                }
            }
        }
    },doDirectionality = function(range, editor, forward) {
        var bookmark,filterFn = function(node) {
            return node.nodeType == 1 ? !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace(node);
        },obj = getObj(editor);
        if (obj && range.collapsed) {
            obj.setAttribute("dir", forward);
            return range;
        }
        bookmark = range.createBookmark();
        range.enlarge(true);
        var bookmark2 = range.createBookmark(),current = domUtils.getNextDomNode(bookmark2.start, false, filterFn),tmpRange = range.cloneRange(),tmpNode;
        while (current && !(domUtils.getPosition(current, bookmark2.end) & domUtils.POSITION_FOLLOWING)) {
            if (current.nodeType == 3 || !block(current)) {
                tmpRange.setStartBefore(current);
                while (current && current !== bookmark2.end && !block(current)) {
                    tmpNode = current;
                    current = domUtils.getNextDomNode(current, false, null, function(node) {
                        return !block(node);
                    });
                }
                tmpRange.setEndAfter(tmpNode);
                var common = tmpRange.getCommonAncestor();
                if (!domUtils.isBody(common) && block(common)) {
                    common.setAttribute("dir", forward);
                    current = common;
                } else {
                    var p = range.document.createElement("p");
                    p.setAttribute("dir", forward);
                    var frag = tmpRange.extractContents();
                    p.appendChild(frag);
                    tmpRange.insertNode(p);
                    current = p;
                }
                current = domUtils.getNextDomNode(current, false, filterFn);
            } else {
                current = domUtils.getNextDomNode(current, true, filterFn);
            }
        }
        return range.moveToBookmark(bookmark2).moveToBookmark(bookmark);
    };
    baidu.editor.commands["directionality"] = {execCommand:function(cmdName, forward) {
        var range = new baidu.editor.dom.Range(this.document);
        if (this.currentSelectedArr && this.currentSelectedArr.length > 0) {
            for (var i = 0,ti; ti = this.currentSelectedArr[i++];) {
                if (ti.style.display != "none") {
                    doDirectionality(range.selectNode(ti), this, forward);
                }
            }
            range.selectNode(this.currentSelectedArr[0]).select();
        } else {
            range = this.selection.getRange();
            if (range.collapsed) {
                var txt = this.document.createTextNode("d");
                range.insertNode(txt);
            }
            doDirectionality(range, this, forward);
            if (txt) {
                range.setStartBefore(txt).collapse(true);
                domUtils.remove(txt);
            }
            range.select();
        }
        return true;
    },queryCommandValue:function() {
        var node = getObj(this);
        return node ? node.getAttribute("dir") : "ltr";
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands["horizontal"] = {execCommand:function(cmdName) {
        if (this.queryCommandState(cmdName) !== -1) {
            this.execCommand("insertHtml", "<hr>");
            var range = this.selection.getRange(),start = range.startContainer;
            if (start.nodeType == 1 && !start.childNodes[range.startOffset]) {
                var tmp;
                if (tmp = start.childNodes[range.startOffset - 1]) {
                    if (tmp.nodeType == 1 && tmp.tagName == "HR") {
                        if (this.options.enterTag == "p") {
                            tmp = this.document.createElement("p");
                            range.insertNode(tmp);
                            range.setStart(tmp, 0).setCursor();
                        } else {
                            tmp = this.document.createElement("br");
                            range.insertNode(tmp);
                            range.setStartBefore(tmp).setCursor();
                        }
                    }
                }
            }
            return true;
        }
    },queryCommandState:function() {
        var range = this.selection.getRange();
        return domUtils.findParentByTagName(range.startContainer, "table") || domUtils.findParentByTagName(range.endContainer, "table") ? -1 : 0;
    }};
})();
baidu.editor.commands["time"] = {execCommand:function() {
    var date = new Date,min = date.getMinutes(),sec = date.getSeconds(),arr = [date.getHours(),min < 10 ? "0" + min : min,sec < 10 ? "0" + sec : sec];
    this.execCommand("insertHtml", arr.join(":"));
    return true;
}};
baidu.editor.commands["date"] = {execCommand:function() {
    var date = new Date,month = date.getMonth() + 1,day = date.getDate(),arr = [date.getFullYear(),month < 10 ? "0" + month : month,day < 10 ? "0" + day : day];
    this.execCommand("insertHtml", arr.join("-"));
    return true;
}};
(function() {
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd,utils = baidu.editor.utils,browser = baidu.editor.browser;
    baidu.editor.commands["inserthtml"] = {execCommand:function(command, html) {
        var editor = this,range,deletedElms,i,ci,div,tds = editor.currentSelectedArr;
        range = editor.selection.getRange();
        div = range.document.createElement("div");
        div.style.display = "inline";
        div.innerHTML = utils.trim(html);
        try {
            editor.adjustTable && editor.adjustTable(div);
        }
        catch(e) {
        }
        if (tds && tds.length) {
            for (var i = 0,ti; ti = tds[i++];) {
                ti.className = "";
            }
            tds[0].innerHTML = "";
            range.setStart(tds[0], 0).collapse(true);
            editor.currentSelectedArr = [];
        }
        if (!range.collapsed) {
            range.deleteContents();
            if (range.startContainer.nodeType == 1) {
                var child = range.startContainer.childNodes[range.startOffset],pre;
                if (child && domUtils.isBlockElm(child) && (pre = child.previousSibling) && domUtils.isBlockElm(pre)) {
                    range.setEnd(pre, pre.childNodes.length).collapse();
                    while (child.firstChild) {
                        pre.appendChild(child.firstChild);
                    }
                    domUtils.remove(child);
                }
            }
        }
        var child,parent,pre,tmp,hadBreak = 0;
        while (child = div.firstChild) {
            range.insertNode(child);
            if (!hadBreak && child.nodeType == domUtils.NODE_ELEMENT && domUtils.isBlockElm(child)) {
                parent = domUtils.findParent(child, function(node) {
                    return domUtils.isBlockElm(node);
                });
                if (parent && parent.tagName.toLowerCase != "body" && !(dtd[parent.tagName][child.nodeName] && child.parentNode === parent)) {
                    if (!dtd[parent.tagName][child.nodeName]) {
                        pre = parent;
                    } else {
                        tmp = child.parentNode;
                        while (tmp !== parent) {
                            pre = tmp;
                            tmp = tmp.parentNode;
                        }
                    }
                    domUtils.breakParent(child, pre || tmp);
                    var pre = child.previousSibling;
                    domUtils.trimWhiteTextNode(pre);
                    if (!pre.childNodes.length) {
                        domUtils.remove(pre);
                    }
                    hadBreak = 1;
                }
            }
            var next = child.nextSibling;
            if (!div.firstChild && next && domUtils.isBlockElm(next)) {
                range.setStart(next, 0).collapse(true);
                break;
            }
            range.setEndAfter(child).collapse();
        }
        child = range.startContainer;
        if (domUtils.isBlockElm(child) && domUtils.isEmptyNode(child)) {
            child.innerHTML = baidu.editor.browser.ie ? "" : "<br/>";
        }
        range.select(true);
        setTimeout(function() {
            range.scrollToView(editor.autoHeightEnabled, editor.autoHeightEnabled ? domUtils.getXY(editor.iframe).y : 0);
        }, 200);
    }};
}());
(function() {
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands["rowspacing"] = {execCommand:function(cmdName, value) {
        this.execCommand("paragraph", "p", {"padding":value + "px 0"});
        return true;
    },queryCommandValue:function() {
        var startNode = this.selection.getStart(),pN = domUtils.findParent(startNode, function(node) {
            return domUtils.isBlockElm(node);
        }, true),value;
        if (pN) {
            value = domUtils.getComputedStyle(pN, "padding-top").replace(/[^\d]/g, "");
            return value * 1 <= 10 ? 0 : value;
        }
        return 0;
    }};
})();
(function() {
    function setRange(range, node) {
        range.setStart(node, 0).setCursor();
    }

    baidu.editor.commands["cleardoc"] = {execCommand:function(cmdName) {
        var me = this,enterTag = me.options.enterTag,browser = baidu.editor.browser,range = this.selection.getRange();
        if (enterTag == "br") {
            this.body.innerHTML = "<br/>";
            setRange(range, this.body);
        } else {
            this.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
            me.focus();
            setRange(range, me.body.firstChild);
        }
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands["anchor"] = {execCommand:function(cmd, name) {
        var range = this.selection.getRange();
        var img = range.getClosedNode();
        if (img && img.getAttribute("anchorname")) {
            if (name) {
                img.setAttribute("anchorname", name);
            } else {
                range.setStartBefore(img).setCursor();
                domUtils.remove(img);
            }
        } else {
            if (name) {
                var anchor = this.document.createElement("img");
                range.collapse(true);
                anchor.setAttribute("anchorname", name);
                anchor.className = "anchorclass";
                range.insertNode(anchor).setStartAfter(anchor).collapse(true).select(true);
            }
        }
    }};
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,browser = baidu.editor.browser;
    baidu.editor.commands["delete"] = {execCommand:function() {
        var range = this.selection.getRange(),mStart = 0,mEnd = 0,me = this;
        if (range.collapsed) {
            return;
        }
        while (!range.startOffset && !domUtils.isBody(range.startContainer)) {
            mStart = 1;
            range.setStartBefore(range.startContainer);
        }
        while (!domUtils.isBody(range.endContainer)) {
            var child,endContainer = range.endContainer,endOffset = range.endOffset;
            if (endContainer.nodeType == 3 && endOffset == endContainer.nodeValue.length) {
                range.setEndAfter(endContainer);
                continue;
            }
            child = endContainer.childNodes[endOffset];
            if (!child || domUtils.isBr(child) && endContainer.lastChild === child) {
                range.setEndAfter(endContainer);
                continue;
            }
            break;
        }
        if (mStart) {
            var start = me.document.createElement("span");
            start.innerHTML = "start";
            start.id = "_baidu_cut_start";
            range.insertNode(start).setStartBefore(start);
        }
        if (mEnd) {
            var end = me.document.createElement("span");
            end.innerHTML = "end";
            end.id = "_baidu_cut_end";
            range.cloneRange().collapse(false).insertNode(end);
            range.setEndAfter(end);
        }
        range.deleteContents();
        if (domUtils.isBody(range.startContainer) && domUtils.isEmptyNode(me.body)) {
            me.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
            range.setStart(me.body.firstChild, 0).collapse(true);
        }
        range.select(true);
    },queryCommandState:function() {
        return this.selection.getRange().collapsed ? -1 : 0;
    }};
})();
(function() {
    var editor = baidu.editor,domUtils = editor.dom.domUtils,notBreakTags = ["td"];
    baidu.editor.plugins["pagebreak"] = function() {
        var me = this;
        me.commands["pagebreak"] = {execCommand:function() {
            var range = me.selection.getRange();
            var div = me.document.createElement("div");
            div.className = "pagebreak";
            domUtils.unselectable(div);
            var node = domUtils.findParentByTagName(range.startContainer, notBreakTags, true),parents = [],pN;
            if (node) {
                switch (node.tagName) {
                    case "TD":
                        pN = node.parentNode;
                        if (!pN.previousSibling) {
                            var table = domUtils.findParentByTagName(pN, "table");
                            table.parentNode.insertBefore(div, table);
                            parents = domUtils.findParents(div, true);
                        } else {
                            pN.parentNode.insertBefore(div, pN);
                            parents = domUtils.findParents(div);
                        }
                        pN = parents[1];
                        if (div !== pN) {
                            domUtils.breakParent(div, pN);
                        }
                        range.moveToBookmark(bk).select();
                        domUtils.clearSelectedArr(me.currentSelectedArr);
                }
            } else {
                if (!range.collapsed) {
                    range.deleteContents();
                    var start = range.startContainer;
                    while (domUtils.isBlockElm(start) && domUtils.isEmptyNode(start)) {
                        range.setStartBefore(start).collapse(true);
                        domUtils.remove(start);
                        start = range.startContainer;
                    }
                }
                parents = domUtils.findParents(range.startContainer, true);
                pN = parents[1];
                range.insertNode(div);
                pN && domUtils.breakParent(div, pN);
                range.setEndAfter(div).setCursor(true, true);
            }
        }};
    };
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,fillchar = new RegExp(baidu.editor.dom.domUtils.fillChar + "|</hr>", "gi"),browser = baidu.editor.browser;
    baidu.editor.plugins["undo"] = function() {
        var me = this,maxUndoCount = me.options.maxUndoCount,maxInputCount = me.options.maxInputCount,specialAttr = /\b(?:href|src|name)="[^"]*?"/gi;

        function UndoManager() {
            this.list = [];
            this.index = 0;
            this.hasUndo = false;
            this.hasRedo = false;
            this.undo = function() {
                if (this.hasUndo) {
                    var currentScene = this.getScene(),lastScene = this.list[this.index];
                    if (lastScene.content.replace(specialAttr, "") != currentScene.content.replace(specialAttr, "")) {
                        this.save();
                    }
                    if (!this.list[this.index - 1] && this.list.length == 1) {
                        this.reset();
                        return;
                    }
                    while (this.list[this.index].content == this.list[this.index - 1].content) {
                        this.index--;
                        if (this.index == 0) {
                            return this.restore(0);
                        }
                    }
                    this.restore(--this.index);
                }
            };
            this.redo = function() {
                if (this.hasRedo) {
                    while (this.list[this.index].content == this.list[this.index + 1].content) {
                        this.index++;
                        if (this.index == this.list.length - 1) {
                            return this.restore(this.index);
                        }
                    }
                    this.restore(++this.index);
                }
            };
            this.restore = function() {
                var scene = this.list[this.index];
                me.document.body.innerHTML = scene.bookcontent.replace(fillchar, "");
                var range = new baidu.editor.dom.Range(me.document);
                range.moveToBookmark({start:"_baidu_bookmark_start_",end:"_baidu_bookmark_end_",id:true}).select(!browser.gecko);
                setTimeout(function() {
                    range.scrollToView(me.autoHeightEnabled, me.autoHeightEnabled ? domUtils.getXY(me.iframe).y : 0);
                }, 200);
                this.update();
                if (me.currentSelectedArr) {
                    me.currentSelectedArr = [];
                    var tds = me.document.getElementsByTagName("td");
                    for (var i = 0,td; td = tds[i++];) {
                        if (td.className == me.options.selectedTdClass) {
                            me.currentSelectedArr.push(td);
                        }
                    }
                }
                this.clearKey();
            };
            this.getScene = function() {
                var range = me.selection.getRange(),cont = me.body.innerHTML.replace(fillchar, "");
                baidu.editor.browser.ie && (cont = cont.replace(/>&nbsp;</g, "><").replace(/\s*</g, "").replace(/>\s*/g, ">"));
                var bookmark = range.createBookmark(true, true),bookCont = me.body.innerHTML.replace(fillchar, "");
                range.moveToBookmark(bookmark).select(true);
                return {bookcontent:bookCont,content:cont};
            };
            this.save = function() {
                var currentScene = this.getScene(),lastScene = this.list[this.index];
                if (lastScene && lastScene.content == currentScene.content && lastScene.bookcontent == currentScene.bookcontent) {
                    return;
                }
                this.list = this.list.slice(0, this.index + 1);
                this.list.push(currentScene);
                if (this.list.length > maxUndoCount) {
                    this.list.shift();
                }
                this.index = this.list.length - 1;
                this.clearKey();
                this.update();
            };
            this.update = function() {
                this.hasRedo = this.list[this.index + 1] ? true : false;
                this.hasUndo = this.list[this.index - 1] || this.list.length == 1 ? true : false;
            };
            this.reset = function() {
                this.list = [];
                this.index = 0;
                this.hasUndo = false;
                this.hasRedo = false;
                this.clearKey();
            };
            this.clearKey = function() {
                keycont = 0;
                lastKeyCode = null;
            };
        }

        me.undoManger = new UndoManager();
        function saveScene() {
            this.undoManger.save();
        }

        me.addListener("beforeexeccommand", saveScene);
        me.addListener("afterexeccommand", saveScene);
        me.addListener("reset", function() {
            me.undoManger.reset();
        });
        me.commands["redo"] = me.commands["undo"] = {execCommand:function(cmdName) {
            me.undoManger[cmdName]();
        },queryCommandState:function(cmdName) {
            return me.undoManger["has" + (cmdName.toLowerCase() == "undo" ? "Undo" : "Redo")] ? 0 : -1;
        },notNeedUndo:1};
        var keys = {16:1,17:1,18:1,37:1,38:1,39:1,40:1,13:1},keycont = 0,lastKeyCode;
        me.addListener("keydown", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (!keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
                if (me.undoManger.list.length == 0 || ((keyCode == 8 || keyCode == 46) && lastKeyCode != keyCode)) {
                    me.undoManger.save();
                    lastKeyCode = keyCode;
                    return;
                }
                if (me.undoManger.list.length == 2 && me.undoManger.index == 0 && keycont == 0) {
                    me.undoManger.list.splice(1, 1);
                    me.undoManger.update();
                }
                lastKeyCode = keyCode;
                keycont++;
                if (keycont > maxInputCount) {
                    setTimeout(function() {
                        me.undoManger.save();
                    }, 0);
                }
            }
        });
    };
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,browser = baidu.editor.browser;

    function getClipboardData(callback) {
        var doc = this.document;
        if (doc.getElementById("baidu_pastebin")) {
            return;
        }
        var range = this.selection.getRange(),bk = range.createBookmark(),pastebin = doc.createElement("div");
        pastebin.id = "baidu_pastebin";
        browser.webkit && pastebin.appendChild(doc.createTextNode(domUtils.fillChar + domUtils.fillChar));
        doc.body.appendChild(pastebin);
        bk.start.style.display = "";
        pastebin.style.cssText = "position:absolute;width:1px;height:1px;overflow:hidden;left:-1000px;white-space:nowrap;top:" + domUtils.getXY(bk.start).y + "px";
        range.selectNodeContents(pastebin).select(true);
        setTimeout(function() {
            if (browser.webkit) {
                var pastebins = doc.querySelectorAll("#baidu_pastebin");
                var lastPastebin = pastebins[pastebins.length - 1];
                if (pastebin !== lastPastebin) {
                    pastebin.parentNode.removeChild(pastebin);
                    pastebin = lastPastebin;
                }
            }
            pastebin.parentNode.removeChild(pastebin);
            range.moveToBookmark(bk).select(true);
            callback(pastebin);
        }, 0);
    }

    baidu.editor.plugins["paste"] = function() {
        var me = this;
        var pasteplain = me.options.pasteplain;
        var modify_num = {flag:""};
        me.commands["pasteplain"] = {queryCommandState:function() {
            return pasteplain;
        },execCommand:function() {
            pasteplain = !pasteplain | 0;
        },notNeedUndo:1};
        function filter(div) {
            var html;
            if (div.firstChild) {
                var nodes = domUtils.getElementsByTagName(div, "span");
                for (var i = 0,ni; ni = nodes[i++];) {
                    if (ni.id == "_baidu_cut_start" || ni.id == "_baidu_cut_end") {
                        domUtils.remove(ni);
                    }
                }
                if (browser.webkit) {
                    var divs = div.querySelectorAll("div #baidu_pastebin"),p;
                    for (var i = 0,di; di = divs[i++];) {
                        p = me.document.createElement("p");
                        while (di.firstChild) {
                            p.appendChild(di.firstChild);
                        }
                        di.parentNode.insertBefore(p, di);
                        domUtils.remove(di, true);
                    }
                    var spans = div.querySelectorAll("span.Apple-style-span");
                    for (var i = 0,ci; ci = spans[i++];) {
                        domUtils.remove(ci, true);
                    }
                    var metas = div.querySelectorAll("meta");
                    for (var i = 0,ci; ci = metas[i++];) {
                        domUtils.remove(ci);
                    }
                    var brs = div.querySelectorAll("div br");
                    for (var i = 0,bi; bi = brs[i++];) {
                        var pN = bi.parentNode;
                        if (pN.tagName == "DIV" && pN.childNodes.length == 1) {
                            domUtils.remove(pN);
                        }
                    }
                }
                if (browser.gecko) {
                    var dirtyNodes = div.querySelectorAll("[_moz_dirty]");
                    for (i = 0; ci = dirtyNodes[i++];) {
                        ci.removeAttribute("_moz_dirty");
                    }
                }
                html = div.innerHTML;
                var f = me.serialize;
                if (f) {
                    try {
                        var node = f.transformInput(f.parseHTML(f.word(html), true));
                        node = f.filter(node, pasteplain ? {whiteList:{"p":{$:{}}},blackList:{"style":1,"script":1,"object":1}} : null, modify_num);
                        if (browser.webkit) {
                            var length = node.children.length,child;
                            while ((child = node.children[length - 1]) && child.tag == "br") {
                                node.children.splice(length - 1, 1);
                                length = node.children.length;
                            }
                        }
                        html = f.toHTML(node);
                    }
                    catch(e) {
                    }
                }
                me.fireEvent("beforepaste", html);
                me.execCommand("insertHtml", html);
            }
        }

        me.addListener("ready", function() {
            domUtils.on(me.body, "cut", function() {
                var range = me.selection.getRange();
                if (!range.collapsed && me.undoManger) {
                    me.undoManger.save();
                }
            });
            domUtils.on(me.body, browser.ie ? "keydown" : "paste", function(e) {
                if (browser.ie && (!e.ctrlKey || e.keyCode != "86")) {
                    return;
                }
                getClipboardData.call(me, function(div) {
                    filter(div);
                    function hideMsg() {
                        me.ui.hideToolbarMsg();
                        me.removeListener("selectionchange", hideMsg);
                    }

                    if (modify_num.flag && me.ui) {
                        me.ui.showToolbarMsg(me.options.messages.pasteMsg);
                        modify_num.flag = "";
                        setTimeout(function() {
                            me.addListener("selectionchange", hideMsg);
                        }, 100);
                    }
                });
            });
        });
    };
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd,notExchange = {"TD":1,"PRE":1,"BLOCKQUOTE":1},browser = baidu.editor.browser;
    baidu.editor.plugins["list"] = function() {
        var me = this;
        me.addListener("keydown", function(type, evt) {
            function preventAndSave() {
                evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                me.undoManger && me.undoManger.save();
            }

            var keyCode = evt.keyCode || evt.which;
            if (keyCode == 13) {
                var range = me.selection.getRange(),start = domUtils.findParentByTagName(range.startContainer, ["ol","ul"], true),end = domUtils.findParentByTagName(range.endContainer, ["ol","ul"], true);
                if (start && end && start === end) {
                    if (!range.collapsed) {
                        start = domUtils.findParentByTagName(range.startContainer, "li", true);
                        end = domUtils.findParentByTagName(range.endContainer, "li", true);
                        if (start && end && start === end) {
                            range.deleteContents();
                            li = domUtils.findParentByTagName(range.startContainer, "li", true);
                            if (li && domUtils.isEmptyBlock(li)) {
                                pre = li.previousSibling;
                                next = li.nextSibling;
                                p = me.document.createElement("p");
                                p.innerHTML = browser.ie ? "" : "<br/>";
                                parentList = li.parentNode;
                                if (pre && next) {
                                    range.setStart(next, 0).collapse(true).select(true);
                                    domUtils.remove(li);
                                } else {
                                    if (!pre && !next || !pre) {
                                        parentList.parentNode.insertBefore(p, parentList);
                                    } else {
                                        li.parentNode.parentNode.insertBefore(p, parentList.nextSibling);
                                    }
                                    domUtils.remove(li);
                                    if (!parentList.firstChild) {
                                        domUtils.remove(parentList);
                                    }
                                    range.setStart(p, 0).setCursor();
                                }
                                preventAndSave();
                                return;
                            }
                        } else {
                            var tmpRange = range.cloneRange(),bk = tmpRange.collapse(false).createBookmark();
                            range.deleteContents();
                            tmpRange.moveToBookmark(bk);
                            var li = domUtils.findParentByTagName(tmpRange.startContainer, "li", true),pre = li.previousSibling,next = li.nextSibling;
                            if (pre) {
                                li = pre;
                                if (pre.firstChild && domUtils.isBlockElm(pre.firstChild)) {
                                    pre = pre.firstChild;
                                }
                                if (domUtils.isEmptyNode(pre)) {
                                    domUtils.remove(li);
                                }
                            }
                            if (next) {
                                li = next;
                                if (next.firstChild && domUtils.isBlockElm(next.firstChild)) {
                                    next = next.firstChild;
                                }
                                if (domUtils.isEmptyNode(next)) {
                                    domUtils.remove(li);
                                }
                            }
                            tmpRange.select();
                            preventAndSave();
                            return;
                        }
                    }
                    li = domUtils.findParentByTagName(range.startContainer, "li", true);
                    if (li) {
                        if (domUtils.isEmptyBlock(li)) {
                            bk = range.createBookmark();
                            var parentList = li.parentNode;
                            if (li !== parentList.lastChild) {
                                domUtils.breakParent(li, parentList);
                            } else {
                                parentList.parentNode.insertBefore(li, parentList.nextSibling);
                            }
                            if (!dtd.$list[li.parentNode.tagName]) {
                                if (!domUtils.isBlockElm(li.firstChild)) {
                                    p = me.document.createElement("p");
                                    li.parentNode.insertBefore(p, li);
                                    while (li.firstChild) {
                                        p.appendChild(li.firstChild);
                                    }
                                    domUtils.remove(li);
                                } else {
                                    domUtils.remove(li, true);
                                }
                            }
                            range.moveToBookmark(bk).select();
                        } else {
                            var first = li.firstChild;
                            if (!first || !domUtils.isBlockElm(first)) {
                                var p = me.document.createElement("p");
                                p.innerHTML = browser.ie || li.firstChild ? "" : "<br/>";
                                while (li.firstChild) {
                                    p.appendChild(li.firstChild);
                                }
                                li.appendChild(p);
                                first = p;
                            }
                            var span = me.document.createElement("span");
                            range.insertNode(span);
                            domUtils.breakParent(span, li);
                            var nextLi = span.nextSibling;
                            first = nextLi.firstChild;
                            if (!first) {
                                p = me.document.createElement("p");
                                p.innerHTML = browser.ie ? "" : "<br/>";
                                nextLi.appendChild(p);
                                first = p;
                            }
                            if (domUtils.isEmptyNode(first)) {
                                first.innerHTML = browser.ie ? "" : "<br/>";
                            }
                            range.setStart(first, 0).collapse(true).shrinkBoundary().select();
                            domUtils.remove(span);
                            pre = nextLi.previousSibling;
                            if (pre && domUtils.isEmptyBlock(pre)) {
                                pre.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
                            }
                        }
                        preventAndSave();
                    }
                }
            }
        });
        baidu.editor.commands["insertorderedlist"] = baidu.editor.commands["insertunorderedlist"] = {execCommand:function(command, style) {
            if (!style) {
                style = command.toLowerCase() == "insertorderedlist" ? "decimal" : "disc";
            }
            var me = this,range = this.selection.getRange(),filterFn = function(node) {
                return node.nodeType == 1 ? node.tagName.toLowerCase() != "br" : !domUtils.isWhitespace(node);
            },tag = command.toLowerCase() == "insertorderedlist" ? "ol" : "ul",frag = me.document.createDocumentFragment();
            range.shrinkBoundary().adjustmentBoundary();
            var bko = range.createBookmark(true),start = domUtils.findParentByTagName(me.document.getElementById(bko.start), "li"),modifyStart = 0,end = domUtils.findParentByTagName(me.document.getElementById(bko.end), "li"),modifyEnd = 0,startParent,endParent,list,tmp;
            if (start || end) {
                start && (startParent = start.parentNode);
                if (!bko.end) {
                    end = start;
                }
                end && (endParent = end.parentNode);
                if (startParent === endParent) {
                    while (start !== end) {
                        tmp = start;
                        start = start.nextSibling;
                        if (!domUtils.isBlockElm(tmp.firstChild)) {
                            var p = me.document.createElement("p");
                            while (tmp.firstChild) {
                                p.appendChild(tmp.firstChild);
                            }
                            tmp.appendChild(p);
                        }
                        frag.appendChild(tmp);
                    }
                    tmp = me.document.createElement("span");
                    startParent.insertBefore(tmp, end);
                    if (!domUtils.isBlockElm(end.firstChild)) {
                        p = me.document.createElement("p");
                        while (end.firstChild) {
                            p.appendChild(end.firstChild);
                        }
                        end.appendChild(p);
                    }
                    frag.appendChild(end);
                    domUtils.breakParent(tmp, startParent);
                    if (domUtils.isEmptyNode(tmp.previousSibling)) {
                        domUtils.remove(tmp.previousSibling);
                    }
                    if (domUtils.isEmptyNode(tmp.nextSibling)) {
                        domUtils.remove(tmp.nextSibling);
                    }
                    if (startParent.tagName.toLowerCase() == tag && domUtils.getComputedStyle(startParent, "list-style-type") == style) {
                        for (var i = 0,ci; ci = frag.childNodes[i++];) {
                            domUtils.remove(ci, true);
                        }
                        tmp.parentNode.insertBefore(frag, tmp);
                    } else {
                        list = me.document.createElement(tag);
                        domUtils.setStyle(list, "list-style-type", style);
                        list.appendChild(frag);
                        tmp.parentNode.insertBefore(list, tmp);
                    }
                    domUtils.remove(tmp);
                    range.moveToBookmark(bko).select();
                    return;
                }
                if (start) {
                    while (start) {
                        tmp = start.nextSibling;
                        var tmpfrag = me.document.createDocumentFragment(),hasBlock = 0;
                        while (start.firstChild) {
                            if (domUtils.isBlockElm(start.firstChild)) {
                                hasBlock = 1;
                            }
                            tmpfrag.appendChild(start.firstChild);
                        }
                        if (!hasBlock) {
                            var tmpP = me.document.createElement("p");
                            tmpP.appendChild(tmpfrag);
                            frag.appendChild(tmpP);
                        } else {
                            frag.appendChild(tmpfrag);
                        }
                        domUtils.remove(start);
                        start = tmp;
                    }
                    startParent.parentNode.insertBefore(frag, startParent.nextSibling);
                    if (domUtils.isEmptyNode(startParent)) {
                        range.setStartBefore(startParent);
                        domUtils.remove(startParent);
                    } else {
                        range.setStartAfter(startParent);
                    }
                    modifyStart = 1;
                }
                if (end) {
                    start = endParent.firstChild;
                    while (start !== end) {
                        tmp = start.nextSibling;
                        tmpfrag = me.document.createDocumentFragment(),hasBlock = 0;
                        while (start.firstChild) {
                            if (domUtils.isBlockElm(start.firstChild)) {
                                hasBlock = 1;
                            }
                            tmpfrag.appendChild(start.firstChild);
                        }
                        if (!hasBlock) {
                            tmpP = me.document.createElement("p");
                            tmpP.appendChild(tmpfrag);
                            frag.appendChild(tmpP);
                        } else {
                            frag.appendChild(tmpfrag);
                        }
                        domUtils.remove(start);
                        start = tmp;
                    }
                    frag.appendChild(end.firstChild);
                    domUtils.remove(end);
                    endParent.parentNode.insertBefore(frag, endParent);
                    range.setEndBefore(endParent);
                    if (domUtils.isEmptyNode(endParent)) {
                        domUtils.remove(endParent);
                    }
                    modifyEnd = 1;
                }
            }
            if (!modifyStart) {
                range.setStartBefore(me.document.getElementById(bko.start));
            }
            if (bko.end && !modifyEnd) {
                range.setEndAfter(me.document.getElementById(bko.end));
            }
            range.enlarge(true, function(node) {
                return notExchange[node.tagName];
            });
            frag = me.document.createDocumentFragment();
            var bk = range.createBookmark(),current = domUtils.getNextDomNode(bk.start, false, filterFn),tmpRange = range.cloneRange(),tmpNode,block = domUtils.isBlockElm;
            while (current && current !== bk.end && (domUtils.getPosition(current, bk.end) & domUtils.POSITION_PRECEDING)) {
                if (current.nodeType == 3 || dtd.li[current.tagName]) {
                    if (current.nodeType == 1 && dtd.$list[current.tagName]) {
                        while (current.firstChild) {
                            frag.appendChild(current.firstChild);
                        }
                        tmpNode = domUtils.getNextDomNode(current, false, filterFn);
                        domUtils.remove(current);
                        current = tmpNode;
                        continue;
                    }
                    tmpNode = current;
                    tmpRange.setStartBefore(current);
                    while (current && current !== bk.end && (!block(current) || domUtils.isBookmarkNode(current))) {
                        tmpNode = current;
                        current = domUtils.getNextDomNode(current, false, null, function(node) {
                            return !notExchange[node.tagName];
                        });
                    }
                    if (current && block(current)) {
                        tmp = domUtils.getNextDomNode(tmpNode, false, filterFn);
                        if (tmp && domUtils.isBookmarkNode(tmp)) {
                            current = domUtils.getNextDomNode(tmp, false, filterFn);
                            tmpNode = tmp;
                        }
                    }
                    tmpRange.setEndAfter(tmpNode);
                    current = domUtils.getNextDomNode(tmpNode, false, filterFn);
                    var li = range.document.createElement("li");
                    li.appendChild(tmpRange.extractContents());
                    frag.appendChild(li);
                } else {
                    current = domUtils.getNextDomNode(current, true, filterFn);
                }
            }
            range.moveToBookmark(bk).collapse(true);
            list = me.document.createElement(tag);
            domUtils.setStyle(list, "list-style-type", style);
            list.appendChild(frag);
            range.insertNode(list).moveToBookmark(bko).select();
        },queryCommandState:function(command) {
            var startNode = this.selection.getStart();
            return domUtils.findParentByTagName(startNode, command.toLowerCase() == "insertorderedlist" ? "ol" : "ul", true) ? 1 : 0;
        },queryCommandValue:function(command) {
            var startNode = this.selection.getStart(),node = domUtils.findParentByTagName(startNode, command.toLowerCase() == "insertorderedlist" ? "ol" : "ul", true);
            return node ? domUtils.getComputedStyle(node, "list-style-type") : null;
        }};
    };
})();
(function() {
    var browser = baidu.editor.browser,domUtils = baidu.editor.dom.domUtils,dtd = baidu.editor.dom.dtd;

    function SourceFormater(config) {
        config = config || {};
        this.indentChar = config.indentChar || "  ";
        this.breakChar = config.breakChar || "\n";
        this.selfClosingEnd = config.selfClosingEnd || " />";
    }

    var unhtml1 = function() {
        var map = {"<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"};

        function rep(m) {
            return map[m];
        }

        return function(str) {
            str = str + "";
            return str ? str.replace(/[<>"']/g, rep) : "";
        };
    }();

    function printAttrs(attrs) {
        var buff = [];
        for (var k in attrs) {
            buff.push(k + "=\"" + unhtml1(attrs[k]) + "\"");
        }
        return buff.join(" ");
    }

    SourceFormater.prototype = {format:function(html) {
        var node = baidu.editor.serialize.parseHTML(html);
        this.buff = [];
        this.indents = "";
        this.indenting = 1;
        this.visitNode(node);
        return this.buff.join("");
    },visitNode:function(node) {
        if (node.type == "fragment") {
            this.visitChildren(node.children);
        } else {
            if (node.type == "element") {
                var selfClosing = dtd.$empty[node.tag];
                this.visitTag(node.tag, node.attributes, selfClosing);
                this.visitChildren(node.children);
                if (!selfClosing) {
                    this.visitEndTag(node.tag);
                }
            } else {
                if (node.type == "comment") {
                    this.visitComment(node.data);
                } else {
                    this.visitText(node.data);
                }
            }
        }
    },visitChildren:function(children) {
        for (var i = 0; i < children.length; i++) {
            this.visitNode(children[i]);
        }
    },visitTag:function(tag, attrs, selfClosing) {
        if (this.indenting) {
            this.indent();
        } else {
            if (!dtd.$inline[tag] && tag != "a") {
                this.newline();
                this.indent();
            }
        }
        this.buff.push("<", tag);
        var attrPart = printAttrs(attrs);
        if (attrPart) {
            this.buff.push(" ", attrPart);
        }
        if (selfClosing) {
            this.buff.push(this.selfClosingEnd);
            if (tag == "br") {
                this.newline();
            }
        } else {
            this.buff.push(">");
            this.indents += this.indentChar;
        }
        if (!dtd.$inline[tag]) {
            this.newline();
        }
    },indent:function() {
        this.buff.push(this.indents);
        this.indenting = 0;
    },newline:function() {
        this.buff.push(this.breakChar);
        this.indenting = 1;
    },visitEndTag:function(tag) {
        this.indents = this.indents.slice(0, -this.indentChar.length);
        if (this.indenting) {
            this.indent();
        } else {
            if (!dtd.$inline[tag] && !(dtd[tag] && dtd[tag]["#"])) {
                this.newline();
                this.indent();
            }
        }
        this.buff.push("</", tag, ">");
    },visitText:function(text) {
        if (this.indenting) {
            this.indent();
        }
        this.buff.push(text);
    },visitComment:function(text) {
        if (this.indenting) {
            this.indent();
        }
        this.buff.push("<!--", text, "-->");
    }};
    function selectTextarea(textarea) {
        var range;
        if (browser.ie) {
            range = textarea.createTextRange();
            range.collapse(true);
            range.select();
        } else {
            textarea.setSelectionRange(0, 0);
            textarea.focus();
        }
    }

    function createTextarea(container) {
        var textarea = document.createElement("textarea");
        textarea.style.cssText = "resize:none;width:100%;height:100%;border:0;padding:0;margin:0;";
        container.appendChild(textarea);
        return textarea;
    }

    baidu.editor.plugins["source"] = function() {
        var editor = this;
        editor.initPlugins(["serialize"]);
        var formatter = new SourceFormater(editor.options.source);
        var sourceMode = false;
        var textarea;
        editor.addListener("ready", function() {
            var container = editor.iframe.parentNode;
            if (browser.ie && browser.version < 8) {
                container.onresize = function() {
                    if (textarea) {
                        textarea.style.width = this.offsetWidth + "px";
                        textarea.style.height = this.offsetHeight + "px";
                    }
                };
            }
            container = null;
        });
        var bakCssText;
        editor.commands["source"] = {execCommand:function() {
            sourceMode = !sourceMode;
            if (sourceMode) {
                editor.undoManger && editor.undoManger.save();
                this.currentSelectedArr && domUtils.clearSelectedArr(this.currentSelectedArr);
                if (browser.gecko) {
                    editor.body.contentEditable = false;
                }
                bakCssText = editor.iframe.style.cssText;
                editor.iframe.style.cssText += "position:absolute;left:-32768px;top:-32768px;";
                var content = formatter.format(editor.getContent());
                textarea = createTextarea(editor.iframe.parentNode);
                textarea.value = content;
                if (browser.ie && browser.version < 8) {
                    textarea.style.height = editor.iframe.parentNode.offsetHeight + "px";
                    textarea.style.width = editor.iframe.parentNode.offsetWidth + "px";
                }
                setTimeout(function() {
                    selectTextarea(textarea);
                });
            } else {
                editor.iframe.style.cssText = bakCssText;
                editor.setContent(textarea.value || "<p><br/></p>");
                domUtils.remove(textarea);
                textarea = null;
                setTimeout(function() {
                    var first = editor.body.firstChild;
                    if (!first) {
                        editor.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
                        first = editor.body.firstChild;
                    }
                    editor.undoManger && editor.undoManger.save();
                    while (first && first.firstChild) {
                        first = first.firstChild;
                    }
                    var range = editor.selection.getRange();
                    if (first.nodeType == 3 || baidu.editor.dom.dtd.$empty[first.tagName]) {
                        range.setStartBefore(first);
                    } else {
                        range.setStart(first, 0);
                    }
                    if (baidu.editor.browser.gecko) {
                        var input = document.createElement("input");
                        document.body.appendChild(input);
                        editor.body.contentEditable = false;
                        setTimeout(function() {
                            input.focus();
                            setTimeout(function() {
                                editor.body.contentEditable = true;
                                range.setCursor(false, true);
                                baidu.editor.dom.domUtils.remove(input);
                            });
                        });
                    } else {
                        range.setCursor(false, true);
                    }
                });
            }
            this.fireEvent("sourcemodechanged", sourceMode);
        },queryCommandState:function() {
            return sourceMode | 0;
        }};
        if (browser.ie) {
            editor.addListener("fullscreenchanged", function(type, fullscreen) {
                if (fullscreen && textarea) {
                    textarea.style.height = editor.iframe.parentNode.offsetHeight + "px";
                    textarea.style.width = editor.iframe.parentNode.offsetWidth + "px";
                }
            });
        }
        var oldQueryCommandState = editor.queryCommandState;
        editor.queryCommandState = function(cmdName) {
            cmdName = cmdName.toLowerCase();
            if (sourceMode) {
                return cmdName == "source" ? 1 : -1;
            }
            return oldQueryCommandState.apply(this, arguments);
        };
        var oldGetContent = editor.getContent;
        editor.getContent = function() {
            if (sourceMode && textarea) {
                var html = textarea.value;
                if (this.serialize) {
                    var node = this.serialize.parseHTML(html);
                    node = this.serialize.filter(node);
                    html = this.serialize.toHTML(node);
                }
                return html;
            } else {
                return oldGetContent.apply(this, arguments);
            }
        };
    };
})();
baidu.editor.plugins["shortcutkeys"] = function() {
    var editor = this,shortcutkeys = baidu.editor.utils.extend({"ctrl+66":"Bold","ctrl+90":"Undo","ctrl+89":"Redo","ctrl+73":"Italic","ctrl+85":"Underline:Underline","ctrl+shift+67":"removeformat","ctrl+shift+76":"justify:left","ctrl+shift+82":"justify:right","ctrl+65":"selectAll"}, editor.options.shortcutkeys);
    editor.addListener("keydown", function(type, e) {
        var keyCode = e.keyCode || e.which,value;
        for (var i in shortcutkeys) {
            if (/^(ctrl)(\+shift)?\+(\d+)$/.test(i.toLowerCase()) || /^(\d+)$/.test(i)) {
                if (((RegExp.$1 == "ctrl" ? (e.ctrlKey || e.metaKey) : 0) && (RegExp.$2 != "" ? e[RegExp.$2.slice(1) + "Key"] : 1) && keyCode == RegExp.$3) || keyCode == RegExp.$1) {
                    value = shortcutkeys[i].split(":");
                    editor.execCommand(value[0], value[1]);
                    e.preventDefault ? e.preventDefault() : (e.returnValue = false);
                }
            }
        }
    });
};
(function() {
    var browser = baidu.editor.browser,domUtils = baidu.editor.dom.domUtils,hTag;
    baidu.editor.plugins["enterkey"] = function() {
        var me = this,tag = me.options.enterTag;
        me.addListener("keyup", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (keyCode == 13) {
                var range = me.selection.getRange(),start = range.startContainer,doSave;
                if (!browser.ie) {
                    if (/h\d/i.test(hTag)) {
                        if (browser.gecko) {
                            var h = domUtils.findParentByTagName(start, ["h1","h2","h3","h4","h5","h6","blockquote"], true);
                            if (!h) {
                                me.document.execCommand("formatBlock", false, "<p>");
                                doSave = 1;
                            }
                        } else {
                            if (start.nodeType == 1) {
                                var tmp = me.document.createTextNode(""),div;
                                range.insertNode(tmp);
                                div = domUtils.findParentByTagName(tmp, "div", true);
                                if (div) {
                                    var p = me.document.createElement("p");
                                    while (div.firstChild) {
                                        p.appendChild(div.firstChild);
                                    }
                                    div.parentNode.insertBefore(p, div);
                                    domUtils.remove(div);
                                    range.setStartBefore(tmp).setCursor();
                                    doSave = 1;
                                }
                                domUtils.remove(tmp);
                            }
                        }
                        if (me.undoManger && doSave) {
                            me.undoManger.save();
                        }
                    }
                }
                range = me.selection.getRange();
                setTimeout(function() {
                    range.scrollToView(me.autoHeightEnabled, me.autoHeightEnabled ? domUtils.getXY(me.iframe).y : 0);
                }, 50);
            }
        });
        me.addListener("keydown", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (keyCode == 13) {
                if (me.undoManger) {
                    me.undoManger.save();
                }
                hTag = "";
                var range = me.selection.getRange();
                if (!range.collapsed) {
                    var start = range.startContainer,end = range.endContainer,startTd = domUtils.findParentByTagName(start, "td", true),endTd = domUtils.findParentByTagName(end, "td", true);
                    if (startTd && endTd && startTd !== endTd || !startTd && endTd || startTd && !endTd) {
                        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                        return;
                    }
                }
                me.currentSelectedArr && domUtils.clearSelectedArr(me.currentSelectedArr);
                if (tag == "p") {
                    if (!browser.ie) {
                        start = domUtils.findParentByTagName(range.startContainer, ["ol","ul","p","h1","h2","h3","h4","h5","h6","blockquote"], true);
                        if (!start) {
                            me.document.execCommand("formatBlock", false, "<p>");
                            if (browser.gecko) {
                                range = me.selection.getRange();
                                start = domUtils.findParentByTagName(range.startContainer, "p", true);
                                start && domUtils.removeDirtyAttr(start);
                            }
                        } else {
                            hTag = start.tagName;
                            start.tagName.toLowerCase() == "p" && browser.gecko && domUtils.removeDirtyAttr(start);
                        }
                    }
                } else {
                    evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                    if (!range.collapsed) {
                        range.deleteContents();
                        start = range.startContainer;
                        if (start.nodeType == 1 && (start = start.childNodes[range.startOffset])) {
                            while (start.nodeType == 1) {
                                if (baidu.editor.dom.dtd.$empty[start.tagName]) {
                                    range.setStartBefore(start).setCursor();
                                    if (me.undoManger) {
                                        me.undoManger.save();
                                    }
                                    return false;
                                }
                                if (!start.firstChild) {
                                    var br = range.document.createElement("br");
                                    start.appendChild(br);
                                    range.setStart(start, 0).setCursor();
                                    if (me.undoManger) {
                                        me.undoManger.save();
                                    }
                                    return false;
                                }
                                start = start.firstChild;
                            }
                            if (start === range.startContainer.childNodes[range.startOffset]) {
                                br = range.document.createElement("br");
                                range.insertNode(br).setCursor();
                            } else {
                                range.setStart(start, 0).setCursor();
                            }
                        } else {
                            br = range.document.createElement("br");
                            range.insertNode(br).setStartAfter(br).setCursor();
                        }
                    } else {
                        br = range.document.createElement("br");
                        range.insertNode(br);
                        var parent = br.parentNode;
                        if (parent.lastChild === br) {
                            br.parentNode.insertBefore(br.cloneNode(true), br);
                            range.setStartBefore(br);
                        } else {
                            range.setStartAfter(br);
                        }
                        range.setCursor();
                    }
                }
            }
        });
    };
})();
(function() {
    var domUtils = baidu.editor.dom.domUtils,browser = baidu.editor.browser,dtd = baidu.editor.dom.dtd,utils = baidu.editor.utils,flag = 0,keys = domUtils.keys,trans = {"B":"strong","I":"em","FONT":"span"},sizeMap = [0,10,12,16,18,24,32,48],listStyle = {"OL":["decimal","lower-alpha","lower-roman","upper-alpha","upper-roman"],"UL":["circle","disc","square"]};
    baidu.editor.plugins["keystrokes"] = function() {
        var me = this;
        me.addListener("keydown", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (keyCode == 8 || keyCode == 46) {
                var range = me.selection.getRange(),tmpRange,start,end;
                if (range.collapsed) {
                    start = range.startContainer;
                    if (domUtils.isWhitespace(start)) {
                        start = start.parentNode;
                    }
                    if (domUtils.isEmptyNode(start) && start === me.body.firstChild) {
                        if (start.tagName != "P") {
                            p = me.document.createElement("p");
                            me.body.insertBefore(p, start);
                            p.innerHTML = browser.ie ? "" : "<br/>";
                            range.setStart(p, 0).setCursor();
                        }
                        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                        return;
                    }
                }
                if (range.collapsed && range.startContainer.nodeType == 3 && range.startContainer.nodeValue.replace(new RegExp(domUtils.fillChar, "g"), "").length == 0) {
                    range.setStartBefore(range.startContainer).collapse(true);
                }
                if (start = range.getClosedNode()) {
                    me.undoManger && me.undoManger.save();
                    range.setStartBefore(start);
                    domUtils.remove(start);
                    range.setCursor();
                    me.undoManger && me.undoManger.save();
                    evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                    return;
                }
                if (!browser.ie) {
                    start = domUtils.findParentByTagName(range.startContainer, "table", true);
                    end = domUtils.findParentByTagName(range.endContainer, "table", true);
                    if (start && !end || !start && end || start !== end) {
                        evt.preventDefault();
                        return;
                    }
                    if (browser.webkit && range.collapsed && start) {
                        tmpRange = range.cloneRange().txtToElmBoundary();
                        start = tmpRange.startContainer;
                        if (domUtils.isBlockElm(start) && start.nodeType == 1 && !dtd.$tableContent[start.tagName] && !domUtils.getChildCount(start, function(node) {
                            return node.nodeType == 1 ? node.tagName !== "BR" : 1;
                        })) {
                            tmpRange.setStartBefore(start).setCursor();
                            domUtils.remove(start, true);
                            evt.preventDefault();
                            return;
                        }
                    }
                }
                if (range.collapsed && !range.startOffset) {
                    tmpRange = range.cloneRange().trimBoundary();
                    var li = domUtils.findParentByTagName(range.startContainer, "li", true),pre;
                    if (li && !tmpRange.startOffset) {
                        if (li && (pre = li.previousSibling)) {
                            if (keyCode == 46 && li.childNodes.length) {
                                return;
                            }
                            me.undoManger && me.undoManger.save();
                            var first = li.firstChild;
                            if (domUtils.isBlockElm(first)) {
                                if (domUtils.isEmptyNode(first)) {
                                    range.setEnd(pre, pre.childNodes.length).shrinkBoundary().collapse().select(true);
                                } else {
                                    span = me.document.createElement("span");
                                    range.insertNode(span);
                                    if (domUtils.isBlockElm(pre.firstChild)) {
                                        pre.firstChild.appendChild(span);
                                        while (first.firstChild) {
                                            pre.firstChild.appendChild(first.firstChild);
                                        }
                                    } else {
                                        while (first.firstChild) {
                                            pre.appendChild(first.firstChild);
                                        }
                                    }
                                    range.setStartBefore(span).collapse(true).select(true);
                                    domUtils.remove(span);
                                }
                            } else {
                                if (domUtils.isEmptyNode(li)) {
                                    range.setEnd(pre, pre.childNodes.length).shrinkBoundary().collapse().select(true);
                                } else {
                                    range.setEnd(pre, pre.childNodes.length).collapse().select(true);
                                    while (li.firstChild) {
                                        pre.appendChild(li.firstChild);
                                    }
                                }
                            }
                            domUtils.remove(li);
                            me.undoManger && me.undoManger.save();
                            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                            return;
                        }
                        if (li && !li.previousSibling) {
                            first = li.firstChild;
                            if (!first || domUtils.isEmptyNode(domUtils.isBlockElm(first) ? first : li)) {
                                var p = me.document.createElement("p");
                                li.parentNode.parentNode.insertBefore(p, li.parentNode);
                                p.innerHTML = browser.ie ? "" : "<br/>";
                                range.setStart(p, 0).setCursor();
                                domUtils.remove(!li.nextSibling ? li.parentNode : li);
                                me.undoManger && me.undoManger.save();
                                evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                                return;
                            }
                        }
                    }
                }
                if (me.undoManger) {
                    if (!range.collapsed) {
                        me.undoManger.save();
                        flag = 1;
                    }
                }
            }
            if (keyCode == 9) {
                range = me.selection.getRange();
                me.undoManger && me.undoManger.save();
                for (var i = 0,txt = ""; i < me.options.tabSize; i++) {
                    txt += me.options.tabNode;
                }
                var span = me.document.createElement("span");
                span.innerHTML = txt;
                if (range.collapsed) {
                    li = domUtils.findParentByTagName(range.startContainer, "li", true);
                    if (li && domUtils.isStartInblock(range)) {
                        bk = range.createBookmark();
                        var parentLi = li.parentNode,list = me.document.createElement(parentLi.tagName);
                        var index = utils.indexOf(listStyle[list.tagName], domUtils.getComputedStyle(parentLi, "list-style-type"));
                        index = index + 1 == listStyle[list.tagName].length ? 0 : index + 1;
                        domUtils.setStyle(list, "list-style-type", listStyle[list.tagName][index]);
                        parentLi.insertBefore(list, li);
                        list.appendChild(li);
                        range.moveToBookmark(bk).select();
                    } else {
                        range.insertNode(span.cloneNode(true).firstChild).setCursor(true);
                    }
                } else {
                    start = domUtils.findParentByTagName(range.startContainer, "table", true);
                    end = domUtils.findParentByTagName(range.endContainer, "table", true);
                    if (start || end) {
                        evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                        return;
                    }
                    start = domUtils.findParentByTagName(range.startContainer, ["ol","ul"], true);
                    end = domUtils.findParentByTagName(range.endContainer, ["ol","ul"], true);
                    if (start && end && start === end) {
                        var bk = range.createBookmark();
                        start = domUtils.findParentByTagName(range.startContainer, "li", true);
                        end = domUtils.findParentByTagName(range.endContainer, "li", true);
                        if (start === start.parentNode.firstChild) {
                            var parentList = me.document.createElement(start.parentNode.tagName);
                            start.parentNode.parentNode.insertBefore(parentList, start.parentNode);
                            parentList.appendChild(start.parentNode);
                        } else {
                            parentLi = start.parentNode,list = me.document.createElement(parentLi.tagName);
                            index = utils.indexOf(listStyle[list.tagName], domUtils.getComputedStyle(parentLi, "list-style-type"));
                            index = index + 1 == listStyle[list.tagName].length ? 0 : index + 1;
                            domUtils.setStyle(list, "list-style-type", listStyle[list.tagName][index]);
                            start.parentNode.insertBefore(list, start);
                            var nextLi;
                            while (start !== end) {
                                nextLi = start.nextSibling;
                                list.appendChild(start);
                                start = nextLi;
                            }
                            list.appendChild(end);
                        }
                        range.moveToBookmark(bk).select();
                    } else {
                        if (start || end) {
                            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                            return;
                        }
                        start = domUtils.findParent(range.startContainer, filterFn);
                        end = domUtils.findParent(range.endContainer, filterFn);
                        if (start && end && start === end) {
                            range.deleteContents();
                            range.insertNode(span.cloneNode(true).firstChild).setCursor(true);
                        } else {
                            var bookmark = range.createBookmark(),filterFn = function(node) {
                                return domUtils.isBlockElm(node);
                            };
                            range.enlarge(true);
                            var bookmark2 = range.createBookmark(),current = domUtils.getNextDomNode(bookmark2.start, false, filterFn);
                            while (current && !(domUtils.getPosition(current, bookmark2.end) & domUtils.POSITION_FOLLOWING)) {
                                current.insertBefore(span.cloneNode(true).firstChild, current.firstChild);
                                current = domUtils.getNextDomNode(current, false, filterFn);
                            }
                            range.moveToBookmark(bookmark2).moveToBookmark(bookmark).select();
                        }
                    }
                }
                me.undoManger && me.undoManger.save();
                evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
            }
        });
        me.addListener("keyup", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (!browser.gecko && !keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
                range = me.selection.getRange();
                if (range.collapsed) {
                    var start = range.startContainer,lastNode,isFixed = 0;
                    while (!domUtils.isBlockElm(start)) {
                        if (start.nodeType == 1 && utils.indexOf(["FONT","B","I"], start.tagName) != -1) {
                            var tmpNode = me.document.createElement(trans[start.tagName]);
                            if (start.tagName == "FONT") {
                                tmpNode.style.cssText = (start.getAttribute("size") ? "font-size:" + (sizeMap[start.getAttribute("size")] || 12) + "px" : "") + ";" + (start.getAttribute("color") ? "color:" + start.getAttribute("color") : "") + ";" + (start.getAttribute("face") ? "font-family:" + start.getAttribute("face") : "") + ";" + start.style.cssText;
                            }
                            while (start.firstChild) {
                                tmpNode.appendChild(start.firstChild);
                            }
                            start.parentNode.insertBefore(tmpNode, start);
                            domUtils.remove(start);
                            if (!isFixed) {
                                range.setEnd(tmpNode, tmpNode.childNodes.length).collapse(true);
                            }
                            start = tmpNode;
                            isFixed = 1;
                        }
                        start = start.parentNode;
                    }
                    isFixed && range.select();
                }
            }
            if (keyCode == 8 || keyCode == 46) {
                var range,body,start,parent,tds = this.currentSelectedArr;
                if (tds && tds.length > 0) {
                    for (var i = 0,ti; ti = tds[i++];) {
                        ti.innerHTML = browser.ie ? (browser.version < 9 ? "&#65279" : "") : "<br/>";
                    }
                    range = new baidu.editor.dom.Range(this.document);
                    range.setStart(tds[0], 0).setCursor();
                    if (flag) {
                        me.undoManger.save();
                        flag = 0;
                    }
                    if (browser.webkit) {
                        evt.preventDefault();
                    }
                    return;
                }
                range = me.selection.getRange();
                if (domUtils.isEmptyBlock(me.document.body) && !range.startOffset) {
                    me.document.body.innerHTML = "<p>" + (browser.ie ? "" : "<br/>") + "</p>";
                    range.setStart(me.document.body.firstChild, 0).setCursor();
                    me.undoManger && me.undoManger.save();
                    return;
                }
                start = range.startContainer;
                if (domUtils.isWhitespace(start)) {
                    start = start.parentNode;
                }
                while (start.nodeType == 1 && domUtils.isEmptyNode(start) && dtd.$removeEmpty[start.tagName]) {
                    parent = start.parentNode;
                    domUtils.remove(start);
                    start = parent;
                }
                if (start.nodeType == 1 && domUtils.isEmptyNode(start)) {
                    if (browser.ie) {
                        var span = range.document.createElement("span");
                        start.appendChild(span);
                        range.setStart(span, 0).setCursor();
                    } else {
                        start.innerHTML = "<br/>";
                        range.setStart(start, 0).setCursor(false, true);
                    }
                    setTimeout(function() {
                        if (browser.ie) {
                            domUtils.remove(span);
                        }
                        if (flag) {
                            me.undoManger.save();
                            flag = 0;
                        }
                    }, 0);
                } else {
                    if (flag) {
                        me.undoManger.save();
                        flag = 0;
                    }
                }
            }
        });
    };
})();
baidu.editor.plugins["fiximgclick"] = function() {
    var me = this,browser = baidu.editor.browser;
    if (browser.webkit) {
        me.addListener("click", function(type, e) {
            if (e.target.tagName == "IMG") {
                var range = new baidu.editor.dom.Range(me.document);
                range.selectNode(e.target).select();
            }
        });
    }
};
(function() {
    var editor = baidu.editor,browser = editor.browser,domUtils = editor.dom.domUtils;
    baidu.editor.plugins["autolink"] = function() {
        var cont = 0;
        if (browser.ie) {
            return;
        }
        var me = this;
        me.addListener("reset", function() {
            cont = 0;
        });
        me.addListener("keydown", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (keyCode == 32 || keyCode == 13) {
                var sel = me.selection.getNative(),range = sel.getRangeAt(0).cloneRange(),offset,charCode;
                var start = range.startContainer;
                while (start.nodeType == 1 && range.startOffset > 0) {
                    start = range.startContainer.childNodes[range.startOffset - 1];
                    if (!start) {
                        break;
                    }
                    range.setStart(start, start.nodeType == 1 ? start.childNodes.length : start.nodeValue.length);
                    range.collapse(true);
                    start = range.startContainer;
                }
                do {
                    if (range.startOffset == 0) {
                        start = range.startContainer.previousSibling;
                        while (start && start.nodeType == 1) {
                            start = start.lastChild;
                        }
                        if (!start || domUtils.isFillChar(start)) {
                            break;
                        }
                        offset = start.nodeValue.length;
                    } else {
                        start = range.startContainer;
                        offset = range.startOffset;
                    }
                    range.setStart(start, offset - 1);
                    charCode = range.toString().charCodeAt(0);
                } while (charCode != 160 && charCode != 32);
                if (range.toString().replace(new RegExp(domUtils.fillChar, "g"), "").match(/^(\s*)(?:https?:\/\/|ssh:\/\/|ftp:\/\/|file:\/|www\.)/i)) {
                    var a = me.document.createElement("a"),text = me.document.createTextNode(" ");
                    if (RegExp.$1.length) {
                        range.setStart(range.startContainer, range.startOffset + RegExp.$1.length);
                    }
                    a.appendChild(range.extractContents());
                    a.href = a.innerHTML;
                    range.insertNode(a);
                    a.parentNode.insertBefore(text, a.nextSibling);
                    range.setStart(text, 0);
                    range.collapse(true);
                    sel.removeAllRanges();
                    sel.addRange(range);
                }
            }
        });
    };
})();
(function() {
    var browser = baidu.editor.browser;
    baidu.editor.plugins["autoheight"] = function() {
        var editor = this;
        editor.autoHeightEnabled = this.options.autoHeightEnabled;
        var timer;
        var bakScroll;
        var bakOverflow;
        editor.enableAutoHeight = function() {
            var iframe = editor.iframe,doc = editor.document,minHeight = editor.options.minFrameHeight;

            function updateHeight() {
                editor.setHeight(Math.max(browser.ie ? doc.body.scrollHeight : doc.body.offsetHeight + 20, minHeight));
            }

            this.autoHeightEnabled = true;
            bakScroll = iframe.scroll;
            bakOverflow = doc.body.style.overflowY;
            iframe.scroll = "no";
            doc.body.style.overflowY = "hidden";
            timer = setTimeout(function() {
                if (editor.queryCommandState("source") != 1) {
                    updateHeight();
                }
                timer = setTimeout(arguments.callee);
            });
            editor.fireEvent("autoheightchanged", this.autoHeightEnabled);
        };
        editor.disableAutoHeight = function() {
            var iframe = editor.iframe,doc = editor.document;
            iframe.scroll = bakScroll;
            doc.body.style.overflowY = bakOverflow;
            clearTimeout(timer);
            this.autoHeightEnabled = false;
            editor.fireEvent("autoheightchanged", this.autoHeightEnabled);
        };
        editor.addListener("ready", function() {
            if (this.autoHeightEnabled) {
                editor.enableAutoHeight();
            }
        });
    };
})();
(function() {
    var browser = baidu.editor.browser,domUtils = baidu.editor.dom.domUtils,uiUtils,LteIE6 = browser.ie && browser.version <= 6;
    baidu.editor.plugins["autofloat"] = function() {
        var optsAutoFloatEnabled = this.options.autoFloatEnabled;
        if (!optsAutoFloatEnabled) {
            return;
        }
        var editor = this,floating = false,MIN_HEIGHT = 0,bakCssText,placeHolder = document.createElement("div");

        function setFloating(delta) {
            var toolbarBox = editor.ui.getDom("toolbarbox"),toobarBoxPos = domUtils.getXY(toolbarBox),origalFloat = window.getComputedStyle ? document.defaultView.getComputedStyle(toolbarBox, null).position : toolbarBox.currentStyle.position,origalLeft = window.getComputedStyle ? document.defaultView.getComputedStyle(toolbarBox, null).left : toolbarBox.currentStyle.left;
            placeHolder.style.height = toolbarBox.offsetHeight + "px";
            bakCssText = toolbarBox.style.cssText;
            toolbarBox.style.width = toolbarBox.offsetWidth + "px";
            toolbarBox.parentNode.insertBefore(placeHolder, toolbarBox);
            if (LteIE6) {
                toolbarBox.style.position = "absolute";
                toolbarBox.style.setExpression("top", "eval(\"((document.documentElement||document.body).scrollTop-" + delta + ")+'px'\")");
                toolbarBox.style.zIndex = "1";
            } else {
                toolbarBox.style.position = "fixed";
                toolbarBox.style.zIndex = "1";
                toolbarBox.style.top = "0";
                ((origalFloat == "absolute" || origalFloat == "relative") && parseFloat(origalLeft)) && (toolbarBox.style.left = toobarBoxPos.x + "px");
            }
            floating = true;
        }

        function unsetFloating() {
            var toolbarBox = editor.ui.getDom("toolbarbox");
            placeHolder.parentNode.removeChild(placeHolder);
            if (LteIE6) {
                toolbarBox.style.removeExpression("top");
            }
            toolbarBox.style.cssText = bakCssText;
            floating = false;
        }

        function updateFloating() {
            var rect = uiUtils.getClientRect(editor.ui.getDom("toolbarbox"));
            var rect2 = uiUtils.getClientRect(editor.ui.getDom("iframeholder"));
            if (!floating) {
                if (rect.top < 0 && rect2.bottom > rect.height + MIN_HEIGHT) {
                    var delta = (document.documentElement.scrollTop || document.body.scrollTop) + rect.top;
                    setFloating(delta);
                }
            } else {
                var rect1 = uiUtils.getClientRect(placeHolder);
                if (rect.top < rect1.top || rect.bottom + MIN_HEIGHT > rect2.bottom) {
                    unsetFloating();
                }
            }
        }

        editor.addListener("ready", function() {
            if (checkHasUI()) {
                if (LteIE6) {
                    fixIE6FixedPos();
                }
                editor.addListener("autoheightchanged", function(t, enabled) {
                    if (enabled) {
                        domUtils.on(window, "scroll", updateFloating);
                        domUtils.on(window, "resize", updateFloating);
                        editor.addListener("keydown", updateFloating);
                    } else {
                        domUtils.un(window, "scroll", updateFloating);
                        domUtils.un(window, "resize", updateFloating);
                        editor.removeListener("keydown", updateFloating);
                    }
                });
                editor.addListener("beforefullscreenchange", function(t, enabled) {
                    if (enabled) {
                        if (floating) {
                            unsetFloating();
                        }
                    }
                });
                editor.addListener("fullscreenchanged", function(t, enabled) {
                    if (!enabled) {
                        updateFloating();
                    }
                });
            }
        });
    };
    function checkHasUI() {
        try {
            uiUtils = baidu.editor.ui.uiUtils;
        }
        catch(ex) {
            alert("autofloat\u63d2\u4ef6\u529f\u80fd\u4f9d\u8d56\u4e8eUEditor UI\nautofloat\u5b9a\u4e49\u4f4d\u7f6e: _src/plugins/autofloat/autofloat.js");
            throw ({name:"\u672a\u5305\u542bUI\u6587\u4ef6",message:"autofloat\u529f\u80fd\u4f9d\u8d56\u4e8eUEditor UI\u3002autofloat\u5b9a\u4e49\u4f4d\u7f6e: _src/plugins/autofloat/autofloat.js"});
        }
        return 1;
    }

    function fixIE6FixedPos() {
        var docStyle = document.body.style;
        docStyle.backgroundImage = "url(\"about:blank\")";
        docStyle.backgroundAttachment = "fixed";
    }
})();
(function() {
    Array.prototype.contains = function(key, isCase) {
        for (var i = 0; i < this.length; i++) {
            if (isCase && this[i] == key) {
                return this[i];
            } else {
                if (this[i].toLowerCase() == key.toLowerCase()) {
                    return this[i].toLowerCase();
                }
            }
        }
        return false;
    };
    function addSpace(linenum) {
        if (linenum < 10) {
            return "&nbsp;&nbsp;";
        } else {
            if (linenum >= 10 && linenum < 100) {
                return "&nbsp;";
            } else {
                if (linenum >= 100 && linenum < 1000) {
                    return "";
                }
            }
        }
    }

    function CLASS_HIGHLIGHT(code, syntax) {
        this._codetxt = code;
        switch (syntax && syntax.toLowerCase()) {
            case "sql":
                this._caseSensitive = false;
                this._keywords = "COMMIT,DELETE,INSERT,LOCK,ROLLBACK,SELECT,TRANSACTION,READ,ONLY,WRITE,USE,ROLLBACK,SEGMENT,ROLE,EXCEPT,NONE,UPDATE,DUAL,WORK,COMMENT,FORCE,FROM,WHERE,INTO,VALUES,ROW,SHARE,MODE,EXCLUSIVE,UPDATE,ROW,NOWAIT,TO,SAVEPOINT,UNION,UNION,ALL,INTERSECT,MINUS,START,WITH,CONNECT,BY,GROUP,HAVING,ORDER,UPDATE,NOWAIT,IDENTIFIED,SET,DROP,PACKAGE,CREATE,REPLACE,PROCEDURE,FUNCTION,TABLE,RETURN,AS,BEGIN,DECLARE,END,IF,THEN,ELSIF,ELSE,WHILE,CURSOR,EXCEPTION,WHEN,OTHERS,NO_DATA_FOUND,TOO_MANY_ROWS,CURSOR_ALREADY_OPENED,FOR,LOOP,IN,OUT,TYPE,OF,INDEX,BINARY_INTEGER,RAISE,ROWTYPE,VARCHAR2,NUMBER,LONG,DATE,RAW,LONG RAW,CHAR,INTEGER,MLSLABEL,CURRENT,OF,DEFAULT,CURRVAL,NEXTVAL,LEVEL,ROWID,ROWNUM,DISTINCT,ALL,LIKE,IS,NOT,NULL,BETWEEN,ANY,AND,OR,EXISTS,ASC,DESC,ABS,CEIL,COS,COSH,EXP,FLOOR,LN,LOG,MOD,POWER,ROUND,SIGN,SIN,SINH,SQRT,TAN,TANH,TRUNC,CHR,CONCAT,INITCAP,LOWER,LPAD,LTRIM,NLS_INITCAP,NLS_LOWER,NLS_UPPER,REPLACE,RPAD,RTRIM,SOUNDEX,SUBSTR,SUBSTRB,TRANSLATE,UPPER,ASCII,INSTR,INSTRB,LENGTH,LENGTHB,NLSSORT,ADD_MONTHS,LAST_DAY,MONTHS_BETWEEN,NEW_TIME,NEXT_DAY,ROUND,SYSDATE,TRUNC,CHARTOROWID,CONVERT,HEXTORAW,RAWTOHEX,ROWIDTOCHAR,TO_CHAR,TO_DATE,TO_LABEL,TO_MULTI_BYTE,TO_NUMBER,TO_SINGLE_BYTE,DUMP,GREATEST,GREATEST_LB,LEAST,LEAST_UB,NVL,UID,USER,USERENV,VSIZE,AVG,COUNT,GLB,LUB,MAX,MIN,STDDEV,SUM,VARIANCE".split(",");
                this._commonObjects = [""];
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.?!;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["'"];
                this._lineComment = "--";
                this._escape = "";
                this._commentOn = "/*";
                this._commentOff = "*/";
                this._ignore = "";
                this._dealTag = false;
                break;
            case "c#":
                this._caseSensitive = true;
                this._keywords = "abstract,as,base,bool,break,byte,case,catch,char,checked,class,const,continue,decimal,default,delegate,do,double,else,enum,event,explicit,extern,false,finally,fixed,float,for,foreach,get,goto,if,implicit,in,int,interface,internal,is,lock,long,namespace,new,null,object,operator,out,override,params,private,protected,public,readonly,ref,return,sbyte,sealed,short,sizeof,stackalloc,static,set,string,struct,switch,this,throw,true,try,typeof,uint,ulong,unchecked,unsafe,ushort,using,value,virtual,void,volatile,while".split(",");
                this._commonObjects = "String,Boolean,DateTime,Int32,Int64,Exception,DataTable,DataReader".split(",");
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.?!;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["\""];
                this._lineComment = "//";
                this._escape = "\\";
                this._commentOn = "/*";
                this._commentOff = "*/";
                this._ignore = "";
                this._dealTag = false;
                break;
            case "java":
                this._caseSensitive = true;
                this._keywords = "abstract,boolean,break,byte,case,catch,char,class,const,continue,default,do,double,else,extends,final,finally,float,for,goto,if,implements,import,instanceof,int,interface,long,native,new,package,private,protected,public,return,short,static,strictfp,super,switch,synchronized,this,throw,throws,transient,try,void,volatile,while".split(",");
                this._commonObjects = "String,Boolean,DateTime,Int32,Int64,Exception,DataTable,DataReader".split(",");
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.?!;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["\""];
                this._lineComment = "//";
                this._escape = "\\";
                this._commentOn = "/*";
                this._commentOff = "*/";
                this._ignore = "";
                this._dealTag = false;
                break;
            case "vbs":
            case "vb":
                this._caseSensitive = false;
                this._keywords = "And,ByRef,ByVal,Call,Case,Class,Const,Dim,Do,Each,Else,ElseIf,Empty,End,Eqv,Erase,Error,Exit,Explicit,False,For,Function,Get,If,Imp,In,Is,Let,Loop,Mod,Next,Not,Nothing,Null,On,Option,Or,Private,Property,Public,Randomize,ReDim,Resume,Select,Set,Step,Sub,Then,To,True,Until,Wend,While,Xor,Anchor,Array,Asc,Atn,CBool,CByte,CCur,CDate,CDbl,Chr,CInt,CLng,Cos,CreateObject,CSng,CStr,Date,DateAdd,DateDiff,DatePart,DateSerial,DateValue,Day,Dictionary,Document,Element,Err,Exp,FileSystemObject,Filter,Fix,Int,Form,FormatCurrency,FormatDateTime,FormatNumber,FormatPercent,GetObject,Hex,Hour,InputBox,InStr,InstrRev,IsArray,IsDate,IsEmpty,IsNull,IsNumeric,IsObject,Join,LBound,LCase,Left,Len,Link,LoadPicture,Location,Log,LTrim,RTrim,Trim,Mid,Minute,Month,MonthName,MsgBox,Navigator,Now,Oct,Replace,Right,Rnd,Round,ScriptEngine,ScriptEngineBuildVersion,ScriptEngineMajorVersion,ScriptEngineMinorVersion,Second,Sgn,Sin,Space,Split,Sqr,StrComp,String,StrReverse,Tan,Time,TextStream,TimeSerial,TimeValue,TypeName,UBound,UCase,VarType,Weekday,WeekDayName,Year".split(",");
                this._commonObjects = "String,Number,Boolean,Date,Integert,Long,Double,Single".split(",");
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.?!;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["\""];
                this._lineComment = "'";
                this._escape = "";
                this._commentOn = "";
                this._commentOff = "";
                this._ignore = "<!--";
                this._dealTag = false;
                break;
            case "javascript":
                this._caseSensitive = true;
                this._keywords = "function,void,this,boolean,while,if,return,new,true,false,try,catch,throw,null,else,int,long,do,var".split(",");
                this._commonObjects = "String,Number,Boolean,RegExp,Error,Math,Date".split(",");
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.?!;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["\"","'"];
                this._lineComment = "//";
                this._escape = "\\";
                this._commentOn = "/*";
                this._commentOff = "*/";
                this._ignore = "<!--";
                break;
            case "css":
            case "html":
                this._caseSensitive = true;
                this._keywords = "function,void,this,boolean,while,if,return,new,true,false,try,catch,throw,null,else,int,long,do,var".split(",");
                this._commonObjects = "String,Number,Boolean,RegExp,Error,Math,Date".split(",");
                this._tags = "html,head,body,title,style,script,language,input,select,div,span,button,img,iframe,frame,frameset,table,tr,td,caption,form,font,meta,textarea".split(",");
                this._wordDelimiters = "\u3000 ,.?!;:\\/>(){}[]\"'\r\n\t+-|*%@#$^&";
                this._quotation = ["\"","'"];
                this._lineComment = "//";
                this._escape = "\\";
                this._commentOn = "/*";
                this._commentOff = "*/";
                this._ignore = "<!--";
                this._dealTag = true;
                break;
            case "xml":
            default:
                this._caseSensitive = true;
                this._keywords = "!DOCTYPE,?xml,script,version,encoding".split(",");
                this._commonObjects = [""];
                this._tags = [""];
                this._wordDelimiters = "\u3000 ,.;:\\/<>(){}[]\"'\r\n\t=+-|*%@#$^&";
                this._quotation = ["\"","'"];
                this._lineComment = "";
                this._escape = "\\";
                this._commentOn = "<!--";
                this._commentOff = "-->";
                this._ignore = "<!--";
                this._dealTag = true;
                break;
        }
        this.highlight = function() {
            var codeArr = new Array();
            var word_index = 0;
            var htmlTxt = new Array();
            for (var i = 0; i < this._codetxt.length; i++) {
                if (this._wordDelimiters.indexOf(this._codetxt.charAt(i)) == -1) {
                    if (!codeArr[word_index]) {
                        codeArr[word_index] = "";
                    }
                    codeArr[word_index] += this._codetxt.charAt(i);
                } else {
                    if (codeArr[word_index]) {
                        word_index++;
                    }
                    codeArr[word_index++] = this._codetxt.charAt(i);
                }
            }
            var quote_opened = false;
            var slash_star_comment_opened = false;
            var slash_slash_comment_opened = false;
            var line_num = 1;
            var quote_char = "";
            var tag_opened = false;
            htmlTxt[htmlTxt.length] = "<span style=\" text-align: right;padding:2px 10px  0;border-right:5px solid #ccc;margin:-2px 10px 0 0;color:#000;\">" + line_num + "." + addSpace(line_num) + "</span>";
            for (var i = 0; i <= word_index; i++) {
                if (typeof (codeArr[i]) == "undefined" || codeArr[i].length == 0) {
                    continue;
                }
                if (codeArr[i] == " ") {
                    htmlTxt[htmlTxt.length] = ("&nbsp;");
                } else {
                    if (!slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened && this.isKeyword(codeArr[i])) {
                        htmlTxt[htmlTxt.length] = ("<span style='color:#0000FF;'>" + codeArr[i] + "</span>");
                    } else {
                        if (!slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened && this.isCommonObject(codeArr[i])) {
                            htmlTxt[htmlTxt.length] = ("<span style='color:#808000;'>" + codeArr[i] + "</span>");
                        } else {
                            if (!slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened && tag_opened && this.isTag(codeArr[i])) {
                                htmlTxt[htmlTxt.length] = ("<span style='color:#0000FF;'>" + codeArr[i] + "</span>");
                            } else {
                                if (codeArr[i] == "\n") {
                                    if (slash_slash_comment_opened) {
                                        htmlTxt[htmlTxt.length] = ("</span>");
                                        slash_slash_comment_opened = false;
                                    }
                                    line_num++;
                                    htmlTxt[htmlTxt.length] = ("<br/><span style=\"text-align: right;padding:4px 10px  0;border-right:5px solid #ccc;margin:-5px 10px 0 0;color:#000;\">" + line_num + "." + addSpace(line_num) + "</span>");
                                } else {
                                    if (this._quotation.contains(codeArr[i]) && !slash_star_comment_opened && !slash_slash_comment_opened) {
                                        if (quote_opened) {
                                            if (quote_char == codeArr[i]) {
                                                if (tag_opened) {
                                                    htmlTxt[htmlTxt.length] = (codeArr[i] + "</span><span style='color:#808000;'>");
                                                } else {
                                                    htmlTxt[htmlTxt.length] = (codeArr[i] + "</span>");
                                                }
                                                quote_opened = false;
                                                quote_char = "";
                                            } else {
                                                htmlTxt[htmlTxt.length] = codeArr[i].replace(/\</g, "&lt;");
                                            }
                                        } else {
                                            if (tag_opened) {
                                                htmlTxt[htmlTxt.length] = ("</span><span style='color:#FF00FF;'>" + codeArr[i]);
                                            } else {
                                                htmlTxt[htmlTxt.length] = ("<span style='color:#FF00FF;'>" + codeArr[i]);
                                            }
                                            quote_opened = true;
                                            quote_char = codeArr[i];
                                        }
                                    } else {
                                        if (codeArr[i] == this._escape) {
                                            htmlTxt[htmlTxt.length] = (codeArr[i]);
                                            if (i < word_index - 1) {
                                                if (codeArr[i + 1].charCodeAt(0) >= 32 && codeArr[i + 1].charCodeAt(0) <= 127) {
                                                    htmlTxt[htmlTxt.length] = codeArr[i + 1].substr(0, 1).replace("&", "&amp;").replace(/\</g, "&lt;");
                                                    codeArr[i + 1] = codeArr[i + 1].substr(1);
                                                }
                                            }
                                        } else {
                                            if (codeArr[i] == "\t") {
                                                htmlTxt[htmlTxt.length] = ("&nbsp;&nbsp;&nbsp;&nbsp;");
                                            } else {
                                                if (this.isStartWith(this._commentOn, codeArr, i) && !slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened) {
                                                    slash_star_comment_opened = true;
                                                    htmlTxt[htmlTxt.length] = ("<span style='color:#008000;'>" + this._commentOn.replace(/\</g, "&lt;"));
                                                    i = i + this._commentOn.length - 1;
                                                } else {
                                                    if (this.isStartWith(this._lineComment, codeArr, i) && !slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened) {
                                                        slash_slash_comment_opened = true;
                                                        htmlTxt[htmlTxt.length] = ("<span style='color:#008000;'>" + this._lineComment);
                                                        i = i + this._lineComment.length - 1;
                                                    } else {
                                                        if (this.isStartWith(this._ignore, codeArr, i) && !slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened) {
                                                            slash_slash_comment_opened = true;
                                                            htmlTxt[htmlTxt.length] = ("<span style='color:#008000;'>" + this._ignore.replace(/\</g, "&lt;"));
                                                            i = i + this._ignore.length - 1;
                                                        } else {
                                                            if (this.isStartWith(this._commentOff, codeArr, i) && !quote_opened && !slash_slash_comment_opened) {
                                                                if (slash_star_comment_opened) {
                                                                    slash_star_comment_opened = false;
                                                                    htmlTxt[htmlTxt.length] = (this._commentOff + "</span>");
                                                                    i = i + this._commentOff.length - 1;
                                                                }
                                                            } else {
                                                                if (this._dealTag && !slash_slash_comment_opened && !slash_star_comment_opened && !quote_opened && codeArr[i] == "<") {
                                                                    htmlTxt[htmlTxt.length] = "&lt;<span style='color:#808000;'>";
                                                                    tag_opened = true;
                                                                } else {
                                                                    if (this._dealTag && tag_opened && codeArr[i] == ">") {
                                                                        htmlTxt[htmlTxt.length] = "</span>&gt;";
                                                                        tag_opened = false;
                                                                    } else {
                                                                        if (codeArr[i] == "&") {
                                                                            htmlTxt[htmlTxt.length] = "&amp;";
                                                                        } else {
                                                                            htmlTxt[htmlTxt.length] = codeArr[i].replace(/</g, "&lt;");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            htmlTxt[htmlTxt.length] = ("");
            this._codetxt = htmlTxt.join("");
        };
        this.isStartWith = function(str, code, index) {
            if (str) {
                for (var i = 0; i < str.length; i++) {
                    if (this._caseSensitive) {
                        if (str.charAt(i) != code[index + i] || (index + i >= code.length)) {
                            return false;
                        }
                    } else {
                        if (str.charAt(i).toLowerCase() != code[index + i].toLowerCase() || (index + i >= code.length)) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        };
        this.isKeyword = function(val) {
            return this._keywords.contains(val, this._caseSensitive);
        };
        this.isCommonObject = function(val) {
            return this._commonObjects.contains(val, this._caseSensitive);
        };
        this.isTag = function(val) {
            return this._tags.contains(val);
        };
        this.transform = function() {
            this._codetxt = this._codetxt.replace(/&nbsp;/ig, " ").replace(/<br\/>|<br>/ig, "\n").replace(/<[^>]*>/ig, "").replace(/&lt;/ig, "<").replace(/&gt;/ig, ">").replace(/&amp;/ig, "&").replace(/([0-9]+\.\s*)/ig, function($1) {
                var arr = $1.split(".");
                if (arr[0] < 10) {
                    return arr[1].replace(/\s{2}/, "");
                } else {
                    if (arr[0] < 100) {
                        return arr[1].replace(/\s{1}/, "");
                    } else {
                        return arr[1];
                    }
                }
            });
        };
    }

    baidu.editor.plugins["highlight"] = function() {
        var me = this,domUtils = baidu.editor.dom.domUtils;
        me.commands["highlightcode"] = {execCommand:function(cmdName, code, syntax) {
            if (code && syntax) {
                var highlight = new CLASS_HIGHLIGHT(code, syntax);
                highlight.highlight();
                me.execCommand("inserthtml", "<pre _syntax='" + syntax + "'>" + highlight._codetxt + "</pre>");
                for (var i = 0,pr,pres = domUtils.getElementsByTagName(me.document, "pre"); pr = pres[i++];) {
                    pr.style.overflowX = "auto";
                }
            } else {
                var range = this.selection.getRange(),start = domUtils.findParentByTagName(range.startContainer, "pre", true),end = domUtils.findParentByTagName(range.endContainer, "pre", true);
                if (start && end && start === end) {
                    if (domUtils.isBody(start.parentNode)) {
                        var p = me.document.createElement("p");
                        p.innerHTML = baidu.editor.browser.ie ? "" : "<br/>";
                        me.body.insertBefore(p, start);
                        range.setStart(p, 0);
                    } else {
                        range.setStartBefore(start);
                    }
                    range.setCursor();
                    domUtils.remove(start);
                }
            }
        },queryCommandState:function() {
            var range = this.selection.getRange(),start = domUtils.findParentByTagName(range.startContainer, "pre", true),end = domUtils.findParentByTagName(range.endContainer, "pre", true);
            return start && end && start === end ? 0 : -1;
        }};
        me.addListener("beforegetcontent", function() {
            for (var i = 0,pr,pres = domUtils.getElementsByTagName(me.document, "pre"); pr = pres[i++];) {
                var highlight = new CLASS_HIGHLIGHT(pr.innerHTML.replace(/\r\n/ig, ""));
                highlight.transform();
                pr.innerHTML = "";
                pr.appendChild(me.document.createTextNode(highlight._codetxt));
            }
        });
        me.addListener("aftersetcontent", function() {
            for (var i = 0,pr,pres = domUtils.getElementsByTagName(me.document, "pre"); pr = pres[i++];) {
                var highlight = new CLASS_HIGHLIGHT(pr.innerHTML, pr.getAttribute("_syntax"));
                highlight.transform();
                highlight.highlight();
                pr.innerHTML = highlight._codetxt;
                pr.style.overflowX = "auto";
            }
        });
    };
})();
(function() {
    baidu.editor.plugins["serialize"] = function() {
        var editor = this;
        var dtd = baidu.editor.dom.dtd;
        var EMPTY_TAG = dtd.$empty;
        var browser = baidu.editor.browser;
        var parseHTML = function() {
            var RE_PART = /<(?:(?:\/([^>]+)>[ \t\r\n]*)|(?:!--([\S|\s]*?)-->)|(?:([^\s\/>]+)\s*((?:(?:"[^"]*")|(?:'[^']*')|[^"'<>])*)\/?>[ \t\r\n]*))/g;
            var RE_ATTR = /([\w\-:.]+)(?:(?:\s*=\s*(?:(?:"([^"]*)")|(?:'([^']*)')|([^\s>]+)))|(?=\s|$))/g;
            var EMPTY_ATTR = {checked:1,compact:1,declare:1,defer:1,disabled:1,ismap:1,multiple:1,nohref:1,noresize:1,noshade:1,nowrap:1,readonly:1,selected:1};
            var CDATA_TAG = {script:1,style:1};
            var NEED_PARENT_TAG = {"li":{"$":"ul","ul":1,"ol":1},"dd":{"$":"dl","dl":1},"dt":{"$":"dl","dl":1},"option":{"$":"select","select":1},"td":{"$":"tr","tr":1},"tr":{"$":"tbody","tbody":1,"thead":1,"tfoot":1,"table":1},"tbody":{"$":"table","table":1,"colgroup":1},"thead":{"$":"table","table":1},"tfoot":{"$":"table","table":1},"col":{"$":"colgroup","colgroup":1}};
            var NEED_CHILD_TAG = {"table":"td","tbody":"td","thead":"td","tfoot":"td","tr":"td","colgroup":"col","ul":"li","ol":"li","dl":"dd","select":"option"};

            function parse(html, callbacks) {
                var match,nextIndex = 0,tagName,cdata;
                RE_PART.exec("");
                while ((match = RE_PART.exec(html))) {
                    var tagIndex = match.index;
                    if (tagIndex > nextIndex) {
                        var text = html.slice(nextIndex, tagIndex);
                        if (cdata) {
                            cdata.push(text);
                        } else {
                            callbacks.onText(text);
                        }
                    }
                    nextIndex = RE_PART.lastIndex;
                    if ((tagName = match[1])) {
                        tagName = tagName.toLowerCase();
                        if (cdata && tagName == cdata._tag_name) {
                            callbacks.onCDATA(cdata.join(""));
                            cdata = null;
                        }
                        if (!cdata) {
                            callbacks.onTagClose(tagName);
                            continue;
                        }
                    }
                    if (cdata) {
                        cdata.push(match[0]);
                        continue;
                    }
                    if ((tagName = match[3])) {
                        if (/="/.test(tagName)) {
                            continue;
                        }
                        tagName = tagName.toLowerCase();
                        var attrPart = match[4],attrMatch,attrMap = {},selfClosing = attrPart && attrPart.slice(-1) == "/";
                        if (attrPart) {
                            RE_ATTR.exec("");
                            while ((attrMatch = RE_ATTR.exec(attrPart))) {
                                var attrName = attrMatch[1].toLowerCase(),attrValue = attrMatch[2] || attrMatch[3] || attrMatch[4] || "";
                                if (!attrValue && EMPTY_ATTR[attrName]) {
                                    attrValue = attrName;
                                }
                                if (attrName == "style") {
                                    if (browser.ie && browser.version <= 6) {
                                        attrValue = attrValue.replace(/(?!;)\s*([\w-]+):/g, function(m, p1) {
                                            return p1.toLowerCase() + ":";
                                        });
                                    }
                                }
                                attrMap[attrName] = attrValue;
                            }
                        }
                        callbacks.onTagOpen(tagName, attrMap, selfClosing);
                        if (!cdata && CDATA_TAG[tagName]) {
                            cdata = [];
                            cdata._tag_name = tagName;
                        }
                        continue;
                    }
                    if ((tagName = match[2])) {
                        callbacks.onComment(tagName);
                    }
                }
                if (html.length > nextIndex) {
                    callbacks.onText(html.slice(nextIndex, html.length));
                }
            }

            return function(html, forceDtd) {
                var fragment = {type:"fragment",parent:null,children:[]};
                var currentNode = fragment;

                function addChild(node) {
                    node.parent = currentNode;
                    currentNode.children.push(node);
                }

                function addElement(element, open) {
                    var node = element;
                    if (NEED_PARENT_TAG[node.tag]) {
                        while (NEED_PARENT_TAG[currentNode.tag] && NEED_PARENT_TAG[currentNode.tag][node.tag]) {
                            currentNode = currentNode.parent;
                        }
                        if (currentNode.tag == node.tag) {
                            currentNode = currentNode.parent;
                        }
                        while (NEED_PARENT_TAG[node.tag]) {
                            if (NEED_PARENT_TAG[node.tag][currentNode.tag]) {
                                break;
                            }
                            node = node.parent = {type:"element",tag:NEED_PARENT_TAG[node.tag]["$"],attributes:{},children:[node]};
                        }
                    }
                    if (forceDtd) {
                        while (dtd[node.tag] && !(currentNode.tag == "span" ? baidu.editor.utils.extend(dtd["strong"], {"a":1,"A":1}) : (dtd[currentNode.tag] || dtd["div"]))[node.tag]) {
                            if (tagEnd(currentNode)) {
                                continue;
                            }
                            if (!currentNode.parent) {
                                break;
                            }
                            currentNode = currentNode.parent;
                        }
                    }
                    node.parent = currentNode;
                    currentNode.children.push(node);
                    if (open) {
                        currentNode = element;
                    }
                    return element;
                }

                function tagEnd(node) {
                    var needTag;
                    if (!node.children.length && (needTag = NEED_CHILD_TAG[node.tag])) {
                        addElement({type:"element",tag:needTag,attributes:{},children:[]}, true);
                        return true;
                    }
                    return false;
                }

                parse(html, {onText:function(text) {
                    while (!(dtd[currentNode.tag] || dtd["div"])["#"]) {
                        if (tagEnd(currentNode)) {
                            continue;
                        }
                        currentNode = currentNode.parent;
                    }
                    if (/[^ \t\r\n]/.test(text)) {
                        addChild({type:"text",data:text});
                    }
                },onComment:function(text) {
                    addChild({type:"comment",data:text});
                },onCDATA:function(text) {
                    while (!(dtd[currentNode.tag] || dtd["div"])["#"]) {
                        if (tagEnd(currentNode)) {
                            continue;
                        }
                        currentNode = currentNode.parent;
                    }
                    addChild({type:"cdata",data:text});
                },onTagOpen:function(tag, attrs, closed) {
                    closed = closed || EMPTY_TAG[tag];
                    addElement({type:"element",tag:tag,attributes:attrs,closed:closed,children:[]}, !closed);
                },onTagClose:function(tag) {
                    var node = currentNode;
                    while (node && tag != node.tag) {
                        node = node.parent;
                    }
                    if (node) {
                        for (var tnode = currentNode; tnode !== node.parent; tnode = tnode.parent) {
                            tagEnd(tnode);
                        }
                        if (!node.children.length && dtd.$removeEmpty[node.tag]) {
                            node.parent.children.pop();
                        }
                        currentNode = node.parent;
                    } else {
                        if (!dtd.$removeEmpty[tag]) {
                            node = {type:"element",tag:tag,attributes:{},children:[]};
                            addElement(node, true);
                            tagEnd(node);
                            currentNode = node.parent;
                        }
                    }
                }});
                while (currentNode !== fragment) {
                    tagEnd(currentNode);
                    currentNode = currentNode.parent;
                }
                return fragment;
            };
        }();
        var unhtml1 = function() {
            var map = {"<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;"};

            function rep(m) {
                return map[m];
            }

            return function(str) {
                str = str + "";
                return str ? str.replace(/[<>"']/g, rep) : "";
            };
        }();
        var toHTML = function() {
            function printChildren(node) {
                var children = node.children;
                var buff = [];
                for (var i = 0,ci; ci = children[i]; i++) {
                    buff.push(toHTML(ci));
                }
                return buff.join("");
            }

            function printAttrs(attrs) {
                var buff = [];
                for (var k in attrs) {
                    buff.push(k + "=\"" + unhtml1(attrs[k]) + "\"");
                }
                return buff.join(" ");
            }

            function printData(node) {
                return unhtml1(node.data);
            }

            function printElement(node) {
                var tag = node.tag;
                var attrs = printAttrs(node.attributes);
                var html = "<" + tag + (attrs ? " " + attrs : "") + (EMPTY_TAG[tag] ? " />" : ">");
                if (!EMPTY_TAG[tag]) {
                    html += printChildren(node);
                    html += "</" + tag + ">";
                }
                return html;
            }

            return function(node) {
                if (node.type == "fragment") {
                    return printChildren(node);
                } else {
                    if (node.type == "element") {
                        return printElement(node);
                    } else {
                        if (node.type == "text" || node.type == "cdata") {
                            return printData(node);
                        } else {
                            if (node.type == "comment") {
                                return "<!--" + node.data + "-->";
                            }
                        }
                    }
                }
                return "";
            };
        }();
        var transformWordHtml = function() {
            function isWordDocument(strValue) {
                var re = new RegExp(/(class="?Mso|style="[^"]*\bmso\-|w:WordDocument)/ig);
                return re.test(strValue);
            }

            function ensureUnits(v) {
                v = v.replace(/([\d.]+)([\w]+)?/g, function(m, p1, p2) {
                    return (Math.round(parseFloat(p1)) || 1) + (p2 || "px");
                });
                return v;
            }

            function filterPasteWord(str) {
                str = str.replace(/<!--\s*EndFragment\s*-->[\s\S]*$/, "");
                str = str.replace(/\r\n|\n|\r/ig, "");
                str = str.replace(/^\s*(&nbsp;)+/ig, "");
                str = str.replace(/(&nbsp;|<br[^>]*>)+\s*$/ig, "");
                str = str.replace(/<!--[\s\S]*?-->/ig, "");
                str = str.replace(/<(!|script[^>]*>.*?<\/script(?=[>\s])|\/?(\?xml(:\w+)?|xml|meta|link|style|\w+:\w+)(?=[\s\/>]))[^>]*>/gi, "");
                str = str.replace(/<p [^>]*class="?MsoHeading"?[^>]*>(.*?)<\/p>/gi, "<p><strong>$1</strong></p>");
                str = str.replace(/(lang)\s*=\s*([\'\"]?)[\w-]+\2/ig, "");
                str = str.replace(/(<[a-z][^>]*)\sstyle="([^"]*)"/gi, function(str, tag, style) {
                    var n = [],i = 0,s = style.replace(/^\s+|\s+$/, "").replace(/&quot;/gi, "'").split(/;\s*/g);
                    for (var i = 0; i < s.length; i++) {
                        var v = s[i];
                        var name,value,parts = v.split(":");
                        if (parts.length == 2) {
                            name = parts[0].toLowerCase();
                            value = parts[1].toLowerCase();
                            switch (name) {
                                case "mso-padding-alt":
                                case "mso-padding-top-alt":
                                case "mso-padding-right-alt":
                                case "mso-padding-bottom-alt":
                                case "mso-padding-left-alt":
                                case "mso-margin-alt":
                                case "mso-margin-top-alt":
                                case "mso-margin-right-alt":
                                case "mso-margin-bottom-alt":
                                case "mso-margin-left-alt":
                                case "mso-table-layout-alt":
                                case "mso-height":
                                case "mso-width":
                                case "mso-vertical-align-alt":
                                    n[i++] = name.replace(/^mso-|-alt$/g, "") + ":" + ensureUnits(value);
                                    continue;
                                case "horiz-align":
                                    n[i++] = "text-align:" + value;
                                    continue;
                                case "vert-align":
                                    n[i++] = "vertical-align:" + value;
                                    continue;
                                case "font-color":
                                case "mso-foreground":
                                    n[i++] = "color:" + value;
                                    continue;
                                case "mso-background":
                                case "mso-highlight":
                                    n[i++] = "background:" + value;
                                    continue;
                                case "mso-default-height":
                                    n[i++] = "min-height:" + ensureUnits(value);
                                    continue;
                                case "mso-default-width":
                                    n[i++] = "min-width:" + ensureUnits(value);
                                    continue;
                                case "mso-padding-between-alt":
                                    n[i++] = "border-collapse:separate;border-spacing:" + ensureUnits(value);
                                    continue;
                                case "text-line-through":
                                    if ((value == "single") || (value == "double")) {
                                        n[i++] = "text-decoration:line-through";
                                    }
                                    continue;
                                case "mso-zero-height":
                                    if (value == "yes") {
                                        n[i++] = "display:none";
                                    }
                                    continue;
                            }
                            if (/^(mso|column|font-emph|lang|layout|line-break|list-image|nav|panose|punct|row|ruby|sep|size|src|tab-|table-border|text-(?:align|decor|indent|trans)|top-bar|version|vnd|word-break)/.test(name)) {
                                if (!/mso\-list/.test(name)) {
                                    continue;
                                }
                            }
                            n[i] = name + ":" + parts[1];
                        }
                    }
                    if (i > 0) {
                        return tag + " style=\"" + n.join(";") + "\"";
                    } else {
                        return tag;
                    }
                });
                str = str.replace(/([ ]+)<\/span>/ig, function(m, p) {
                    return new Array(p.length + 1).join("&nbsp;") + "</span>";
                });
                return str;
            }

            return function(html) {
                first = null;
                parentTag = "",liStyle = "",firstTag = "";
                if (isWordDocument(html)) {
                    html = filterPasteWord(html);
                }
                return html.replace(/>[ \t\r\n]*</g, "><");
            };
        }();
        var NODE_NAME_MAP = {"text":"#text","comment":"#comment","cdata":"#cdata-section","fragment":"#document-fragment"};

        function _likeLi(node) {
            var a;
            if (node && node.tag == "p") {
                if (node.attributes["class"] == "MsoListParagraph" || /mso-list/.test(node.attributes.style)) {
                    a = 1;
                } else {
                    var firstChild = node.children[0];
                    if (firstChild && firstChild.tag == "span" && /Wingdings/i.test(firstChild.attributes.style)) {
                        a = 1;
                    }
                }
            }
            return a;
        }

        var first,orderStyle = {"decimal":/\d+/,"lower-roman":/^m{0,4}(cm|cd|d?c{0,3})(xc|xl|l?x{0,3})(ix|iv|v?i{0,3})$/,"upper-roman":/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/,"lower-alpha":/^\(?[a-z]+\)?$/,"upper-alpha":/^\(?[A-Z]+\)?$/},unorderStyle = {"disc":/^[l\u00B7\u2002]/,"circle":/^[\u006F\u00D8]/,"square":/^[\u006E\u25C6]/},parentTag = "",liStyle = "",firstTag,tdStyle,tableStyle,preStyle;

        function transNode(node) {
            if (node.type == "element" && !node.children.length && dtd.$removeEmpty[node.tag] && node.tag != "a") {
                return {type:"fragment",children:[]};
            }
            var sizeMap = [0,10,12,16,18,24,32,48],attr,indexOf = baidu.editor.utils.indexOf;
            switch (node.tag) {
                case "li":
                    var child = node.children[0];
                    if (!child || child.type != "element" || child.tag != "p") {
                        var pNode = {type:"element",tag:"p",attributes:{},children:node.children};
                        node.children = [pNode];
                    }
                    break;
                case "table":
                case "td":
                    optStyle(node);
                    break;
                case "a":
                    if (node.attributes["anchorname"]) {
                        node.tag = "img";
                        node.attributes = {"class":"anchorclass","anchorname":node.attributes["name"]};
                        node.closed = 1;
                    }
                    break;
                case "b":
                    node.tag = node.name = "strong";
                    break;
                case "i":
                    node.tag = node.name = "em";
                    break;
                case "u":
                    node.tag = node.name = "span";
                    node.attributes.style = (node.attributes.style || "") + ";text-decoration:underline;";
                    break;
                case "s":
                case "del":
                    node.tag = node.name = "span";
                    node.attributes.style = (node.attributes.style || "") + ";text-decoration:line-through;";
                    if (node.children.length == 1) {
                        child = node.children[0];
                        if (child.tag == node.tag) {
                            node.attributes.style += ";" + child.attributes.style;
                            node.children = child.children;
                        }
                    }
                    break;
                case "span":
                    if (/mso-list/.test(node.attributes.style)) {
                        if (firstTag != "end") {
                            var ci = node.children[0],p;
                            while (ci.type == "element") {
                                ci = ci.children[0];
                            }
                            for (p in unorderStyle) {
                                if (unorderStyle[p].test(ci.data)) {
                                    parentTag = "ul";
                                    liStyle = p;
                                    break;
                                }
                            }
                            if (!parentTag) {
                                for (p in orderStyle) {
                                    if (orderStyle[p].test(ci.data.replace(/\.$/, ""))) {
                                        parentTag = "ol";
                                        liStyle = p;
                                        break;
                                    }
                                }
                            }
                            if (firstTag) {
                                if (ci.data == firstTag) {
                                    if (parentTag != "ul") {
                                        liStyle = "";
                                    }
                                    parentTag = "ul";
                                } else {
                                    if (parentTag != "ol") {
                                        liStyle = "";
                                    }
                                    parentTag = "ol";
                                }
                                firstTag = "end";
                            } else {
                                firstTag = ci.data;
                            }
                            if (parentTag) {
                                var tmpNode = node;
                                while (tmpNode.tag != "ul" && tmpNode.tag != "ol") {
                                    tmpNode = tmpNode.parent;
                                }
                                tmpNode.tag = parentTag;
                                tmpNode.attributes.style = "list-style-type:" + liStyle;
                            }
                        }
                        node = {type:"fragment",children:[]};
                        break;
                    }
                    var style = node.attributes.style;
                    if (style) {
                        style = style.replace(/background(?!-)/g, "background-color");
                        style = style.match(/(?:\b(?:color|font-size|background-color|font-size|font-family|text-decoration)\b\s*:\s*(&[^;]+;|[^;])+(?=;)?)/gi);
                        if (style) {
                            node.attributes.style = style.join(";");
                            if (!node.attributes.style) {
                                delete node.attributes.style;
                            }
                        }
                    }
                    break;
                case "font":
                    node.tag = node.name = "span";
                    attr = node.attributes;
                    node.attributes = {"style":(attr.size ? "font-size:" + (sizeMap[attr.size] || 12) + "px" : "") + ";" + (attr.color ? "color:" + attr.color : "") + ";" + (attr.face ? "font-family:" + attr.face : "") + ";" + (attr.style || "")};
                    while (node.parent.tag == node.tag && node.parent.children.length == 1) {
                        node.attributes.style && (node.parent.attributes.style ? (node.parent.attributes.style += ";" + node.attributes.style) : (node.parent.attributes.style = node.attributes.style));
                        node.parent.children = node.children;
                        node = node.parent;
                    }
                    break;
                case "p":
                    if (node.attributes.align) {
                        node.attributes.style = (node.attributes.style || "") + ";text-align:" + node.attributes.align + ";";
                        delete node.attributes.align;
                    }
                    if (_likeLi(node)) {
                        if (!first) {
                            var ulNode = {type:"element",tag:"ul",attributes:{},children:[]},index = indexOf(node.parent.children, node);
                            node.parent.children[index] = ulNode;
                            ulNode.parent = node.parent;
                            ulNode.children[0] = node;
                            node.parent = ulNode;
                            while (1) {
                                node = ulNode.parent.children[index + 1];
                                if (_likeLi(node)) {
                                    ulNode.children[ulNode.children.length] = node;
                                    node.parent = ulNode;
                                    ulNode.parent.children.splice(index + 1, 1);
                                } else {
                                    break;
                                }
                            }
                            return ulNode;
                        }
                        node.tag = node.name = "li";
                        var span = node.children[0];
                        while (span && span.type == "element") {
                            span = span.children[0];
                        }
                        span.parent.attributes.style = (span.parent.attributes.style || "") + "mso-list:10";
                        delete node.attributes["class"];
                        delete node.attributes.style;
                        var pNode = {type:"element",tag:"p",attributes:{},children:node.children};
                        node.children = [pNode];
                    }
            }
            return node;
        }

        function optStyle(node) {
            if (browser.ie && node.attributes.style) {
                var border = node.attributes.style.match(/border[^:]*:([^;]*)/i);
                if (border) {
                    border = border[1];
                    if (border) {
                        node.attributes.style = node.attributes.style.replace(/border[^;]*?(;|$)/ig, "").replace(/^\s*|\s*$/, "");
                        if (!/^\s*#\w+\s*$/.test(border)) {
                            node.attributes.style = (/;$/.test(node.attributes.style) || node.attributes.style.length == 0 ? "" : ";") + "border:" + border;
                        }
                    }
                }
                node.attributes.style = node.attributes.style.replace(/^\s*|\s*$/, "");
            }
        }

        function transOutNode(node) {
            if (node.type == "text") {
                node.data = node.data.replace(/ /g, "&nbsp;");
            }
            switch (node.tag) {
                case "table":
                    !node.attributes.style && delete node.attributes.style;
                    if (browser.ie && node.attributes.style) {
                        optStyle(node);
                    }
                    break;
                case "td":
                    if (/display\s*:\s*none/i.test(node.attributes.style)) {
                        return {type:"fragment",children:[]};
                    }
                    if (browser.ie && !node.children.length) {
                        var txtNode = {type:"text",data:"&nbsp;",parent:node};
                        node.children[0] = txtNode;
                    }
                    if (browser.ie && node.attributes.style) {
                        optStyle(node);
                    }
                    break;
                case "img":
                    if (node.attributes.anchorname) {
                        node.tag = "a";
                        node.attributes = {name:node.attributes.anchorname,anchorname:1};
                        node.closed = null;
                    }
            }
            return node;
        }

        function childrenAccept(node, visit, ctx) {
            if (!node.children || !node.children.length) {
                return node;
            }
            var children = node.children;
            for (var i = 0; i < children.length; i++) {
                var newNode = visit(children[i], ctx);
                if (newNode.type == "fragment") {
                    var args = [i,1];
                    args.push.apply(args, newNode.children);
                    children.splice.apply(children, args);
                    if (!children.length) {
                        node = {type:"fragment",children:[]};
                    }
                    i--;
                } else {
                    children[i] = newNode;
                }
            }
            return node;
        }

        function Serialize(rules) {
            this.rules = rules;
        }

        Serialize.prototype = {rules:null,filter:function(node, rules, modify) {
            rules = rules || this.rules;
            var whiteList = rules && rules.whiteList;
            var blackList = rules && rules.blackList;

            function visitNode(node, parent) {
                node.name = node.type == "element" ? node.tag : NODE_NAME_MAP[node.type];
                if (parent == null) {
                    return childrenAccept(node, visitNode, node);
                }
                if (blackList && blackList[node.name]) {
                    modify && (modify.flag = 1);
                    return {type:"fragment",children:[]};
                }
                if (whiteList) {
                    if (node.type == "element") {
                        if (parent.type == "fragment" ? whiteList[node.name] : whiteList[node.name] && whiteList[parent.name][node.name]) {
                            var props;
                            if ((props = whiteList[node.name].$)) {
                                var oldAttrs = node.attributes;
                                var newAttrs = {};
                                for (var k in props) {
                                    if (oldAttrs[k]) {
                                        newAttrs[k] = oldAttrs[k];
                                    }
                                }
                                node.attributes = newAttrs;
                            }
                        } else {
                            modify && (modify.flag = 1);
                            node.type = "fragment";
                            node.name = parent.name;
                        }
                    } else {
                    }
                }
                if (blackList || whiteList) {
                    childrenAccept(node, visitNode, node);
                }
                return node;
            }

            return visitNode(node, null);
        },transformInput:function(node, wrapInline) {
            function visitNode(node) {
                node = transNode(node);
                if (node.tag == "ol" || node.tag == "ul") {
                    first = 1;
                }
                node = childrenAccept(node, visitNode, node);
                if (node.tag == "ol" || node.tag == "ul") {
                    first = 0;
                    parentTag = "",liStyle = "",firstTag = "";
                }
                if (node.type == "text" && node.data.replace(/\s/g, "") == editor.options.pageBreakTag) {
                    node.type = "element";
                    node.name = node.tag = "div";
                    delete node.data;
                    node.attributes = {"class":"pagebreak","unselectable":"on","style":"moz-user-select:none;-khtml-user-select: none;"};
                    node.children = [];
                }
                return node;
            }

            return visitNode(node);
        },transformOutput:function(node) {
            function visitNode(node) {
                if (node.tag == "div" && node.attributes["class"] == "pagebreak") {
                    delete node.tag;
                    node.type = "text";
                    node.data = editor.options.pageBreakTag;
                    delete node.children;
                }
                node = transOutNode(node);
                if (node.tag == "ol" || node.tag == "ul") {
                    first = 1;
                }
                node = childrenAccept(node, visitNode, node);
                if (node.tag == "ol" || node.tag == "ul") {
                    first = 0;
                }
                return node;
            }

            return visitNode(node);
        },toHTML:toHTML,parseHTML:parseHTML,word:transformWordHtml};
        editor.serialize = new Serialize(editor.options.serialize);
        baidu.editor.serialize = new Serialize({});
    };
})();
(function() {
    baidu.editor.plugins["table"] = function() {
        var editor = baidu.editor,browser = editor.browser,domUtils = editor.dom.domUtils,keys = domUtils.keys,clearSelectedTd = domUtils.clearSelectedArr;
        var anchorTd,tableOpt,_isEmpty = domUtils.isEmptyNode;

        function getIndex(cell) {
            var cells = cell.parentNode.cells;
            for (var i = 0,ci; ci = cells[i]; i++) {
                if (ci === cell) {
                    return i;
                }
            }
        }

        function _isHide(cell) {
            return cell.style.display == "none";
        }

        function getCount(arr) {
            var count = 0;
            for (var i = 0,ti; ti = arr[i++];) {
                if (!_isHide(ti)) {
                    count++;
                }
            }
            return count;
        }

        var me = this;
        me.currentSelectedArr = [];
        me.addListener("mousedown", _mouseDownEvent);
        me.addListener("keydown", function(type, evt) {
            var keyCode = evt.keyCode || evt.which;
            if (!keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey) {
                clearSelectedTd(me.currentSelectedArr);
            }
        });
        me.addListener("mouseup", function(type, evt) {
            anchorTd = null;
            me.removeListener("mouseover", _mouseDownEvent);
            var td = me.currentSelectedArr[0];
            if (td) {
                me.document.body.style.webkitUserSelect = "";
                var range = new baidu.editor.dom.Range(me.document);
                if (_isEmpty(td)) {
                    range.setStart(me.currentSelectedArr[0], 0).setCursor();
                } else {
                    range.selectNodeContents(me.currentSelectedArr[0]).select();
                }
            } else {
                var range = me.selection.getRange().shrinkBoundary();
                if (!range.collapsed) {
                    var start = domUtils.findParentByTagName(range.startContainer, "td", true),end = domUtils.findParentByTagName(range.endContainer, "td", true);
                    if (start && !end || !start && end || start && end && start !== end) {
                        range.collapse(true).select(true);
                    }
                }
            }
        });
        function reset() {
            me.currentSelectedArr = [];
            anchorTd = null;
        }

        me.commands["inserttable"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return domUtils.findParentByTagName(range.startContainer, "table", true) || domUtils.findParentByTagName(range.endContainer, "table", true) || me.currentSelectedArr.length > 0 ? -1 : 0;
        },execCommand:function(cmdName, tableobj) {
            tableOpt = tableobj;
            var arr = [];
            arr.push("cellpadding='" + (tableobj.cellpadding || 0) + "'");
            arr.push("cellspacing='" + (tableobj.cellspacing || 0) + "'");
            tableobj.width ? arr.push("width='" + tableobj.width + "'") : arr.push("width='500'");
            tableobj.height ? arr.push("height='" + tableobj.height + "'") : arr.push("height='100'");
            arr.push("borderColor='" + (tableobj.bordercolor || "#000") + "'");
            arr.push("border='" + (tableobj.border || 1) + "'");
            var html,rows = [],j = tableobj.numRows;
            if (j) {
                while (j--) {
                    var cols = [];
                    var k = tableobj.numCols;
                    while (k--) {
                        cols[k] = "<td width=" + Math.floor((tableobj.width || 500) / tableobj.numCols) + " >" + (browser.ie ? domUtils.fillChar : "<br/>") + "</td>";
                    }
                    rows.push("<tr " + (tableobj.align ? "style=text-align:" + tableobj.align + "" : "") + ">" + cols.join("") + "</tr>");
                }
            }
            html = "<table  " + arr.join(" ") + (tableobj.backgroundcolor ? " style=\"background-color:" + tableobj.backgroundcolor + ";\"" : "") + ">" + rows.join("") + "</table>";
            this.execCommand("insertHtml", html);
            reset();
        }};
        me.commands["edittable"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return domUtils.findParentByTagName(range.startContainer, "table", true) || me.currentSelectedArr.length > 0 ? 0 : -1;
        },execCommand:function(cmdName, tableobj) {
            var start = this.selection.getStart();
            var table = domUtils.findParentByTagName(start, "table", true);
            if (table) {
                table.setAttribute("cellpadding", tableobj.cellpadding);
                table.setAttribute("cellspacing", tableobj.cellspacing);
                table.setAttribute("width", tableobj.width);
                table.setAttribute("height", tableobj.height);
                table.setAttribute("border", tableobj.border);
                table.setAttribute("borderColor", tableobj.bordercolor);
                domUtils.setStyle(table, "background-color", tableobj.backgroundcolor);
                for (var r = 0,row; row = table.rows[r++];) {
                    domUtils.setStyle(row, "text-align", tableobj.align);
                }
            }
        }};
        me.commands["deletetable"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return (domUtils.findParentByTagName(range.startContainer, "table", true) && domUtils.findParentByTagName(range.endContainer, "table", true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, "table", true);
            var p = table.ownerDocument.createElement("p");
            p.innerHTML = browser.ie ? "&nbsp;" : "<br/>";
            table.parentNode.insertBefore(p, table);
            domUtils.remove(table);
            range.setStart(p, 0).setCursor();
            domUtils.remove(table);
            reset();
        }};
        me.commands["addcaption"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return (domUtils.findParentByTagName(range.startContainer, "table", true) && domUtils.findParentByTagName(range.endContainer, "table", true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
        },execCommand:function(cmdName, opt) {
            var range = this.selection.getRange(),table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, "table", true);
            if (opt == "on") {
                var c = table.createCaption();
                c.innerHTML = "\u8bf7\u5728\u6b64\u8f93\u5165\u8868\u683c\u6807\u9898";
            } else {
                table.removeChild(table.caption);
            }
        }};
        me.commands["mergeright"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true);
            if (!td || this.currentSelectedArr.length > 1) {
                return -1;
            }
            var tr = td.parentNode;
            var rightCellIndex = getIndex(td) + td.colSpan;
            if (rightCellIndex >= tr.cells.length) {
                return -1;
            }
            var rightCell = tr.cells[rightCellIndex];
            if (_isHide(rightCell)) {
                return -1;
            }
            return td.rowSpan == rightCell.rowSpan ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true) || me.currentSelectedArr[0],tr = td.parentNode,rows = tr.parentNode.parentNode.rows;
            var rightCellRowIndex = tr.rowIndex,rightCellCellIndex = getIndex(td) + td.colSpan,rightCell = rows[rightCellRowIndex].cells[rightCellCellIndex];
            for (var i = rightCellRowIndex; i < rightCellRowIndex + rightCell.rowSpan; i++) {
                for (var j = rightCellCellIndex; j < rightCellCellIndex + rightCell.colSpan; j++) {
                    var tmpCell = rows[i].cells[j];
                    tmpCell.setAttribute("rootRowIndex", tr.rowIndex);
                    tmpCell.setAttribute("rootCellIndex", getIndex(td));
                }
            }
            td.colSpan += rightCell.colSpan || 1;
            _moveContent(td, rightCell);
            rightCell.style.display = "none";
            range.setStart(td, 0).setCursor(true, true);
        }};
        me.commands["mergedown"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, "td", true);
            if (!td || getCount(me.currentSelectedArr) > 1) {
                return -1;
            }
            var tr = td.parentNode,table = tr.parentNode.parentNode,rows = table.rows;
            var downCellRowIndex = tr.rowIndex + td.rowSpan;
            if (downCellRowIndex >= rows.length) {
                return -1;
            }
            var downCell = rows[downCellRowIndex].cells[getIndex(td)];
            if (_isHide(downCell)) {
                return -1;
            }
            return td.colSpan == downCell.colSpan ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true) || me.currentSelectedArr[0];
            var tr = td.parentNode,rows = tr.parentNode.parentNode.rows;
            var downCellRowIndex = tr.rowIndex + td.rowSpan,downCellCellIndex = getIndex(td),downCell = rows[downCellRowIndex].cells[downCellCellIndex];
            for (var i = downCellRowIndex; i < downCellRowIndex + downCell.rowSpan; i++) {
                for (var j = downCellCellIndex; j < downCellCellIndex + downCell.colSpan; j++) {
                    var tmpCell = rows[i].cells[j];
                    tmpCell.setAttribute("rootRowIndex", tr.rowIndex);
                    tmpCell.setAttribute("rootCellIndex", getIndex(td));
                }
            }
            td.rowSpan += downCell.rowSpan || 1;
            _moveContent(td, downCell);
            downCell.style.display = "none";
            range.setStart(td, 0).setCursor();
        }};
        me.commands["deleterow"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true);
            if (!td && me.currentSelectedArr.length == 0) {
                return -1;
            }
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true),tr,table,cells,rows,rowIndex,cellIndex;
            if (td && me.currentSelectedArr.length == 0) {
                var count = (td.rowSpan || 1) - 1;
                me.currentSelectedArr.push(td);
                tr = td.parentNode,table = tr.parentNode.parentNode;
                rows = table.rows,rowIndex = tr.rowIndex + 1,cellIndex = getIndex(td);
                while (count) {
                    me.currentSelectedArr.push(rows[rowIndex].cells[cellIndex]);
                    count--;
                    rowIndex++;
                }
            }
            while (td = me.currentSelectedArr.pop()) {
                if (!domUtils.findParentByTagName(td, "table")) {
                    continue;
                }
                tr = td.parentNode,table = tr.parentNode.parentNode;
                cells = tr.cells,rows = table.rows,rowIndex = tr.rowIndex,cellIndex = getIndex(td);
                for (var currentCellIndex = 0; currentCellIndex < cells.length;) {
                    var currentNode = cells[currentCellIndex];
                    if (_isHide(currentNode)) {
                        var topNode = rows[currentNode.getAttribute("rootRowIndex")].cells[currentNode.getAttribute("rootCellIndex")];
                        topNode.rowSpan--;
                        currentCellIndex += topNode.colSpan;
                    } else {
                        if (currentNode.rowSpan == 1) {
                            currentCellIndex += currentNode.colSpan;
                        } else {
                            var downNode = rows[rowIndex + 1].cells[currentCellIndex];
                            downNode.style.display = "";
                            downNode.rowSpan = currentNode.rowSpan - 1;
                            downNode.colSpan = currentNode.colSpan;
                            currentCellIndex += currentNode.colSpan;
                        }
                    }
                }
                domUtils.remove(tr);
                var topRowTd,focusTd,downRowTd;
                if (rowIndex == rows.length) {
                    if (rowIndex == 0) {
                        var p = table.ownerDocument.createElement("p");
                        p.innerHTML = browser.ie ? "&nbsp;" : "<br/>";
                        table.parentNode.insertBefore(p, table);
                        domUtils.remove(table);
                        range.setStart(p, 0).setCursor();
                        return;
                    }
                    var preRowIndex = rowIndex - 1;
                    topRowTd = rows[preRowIndex].cells[cellIndex];
                    focusTd = _isHide(topRowTd) ? rows[topRowTd.getAttribute("rootRowIndex")].cells[topRowTd.getAttribute("rootCellIndex")] : topRowTd;
                } else {
                    downRowTd = rows[rowIndex].cells[cellIndex];
                    focusTd = _isHide(downRowTd) ? rows[downRowTd.getAttribute("rootRowIndex")].cells[downRowTd.getAttribute("rootCellIndex")] : downRowTd;
                }
            }
            range.setStart(focusTd, 0).setCursor();
            update(table);
        }};
        me.commands["deletecol"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true);
            if (!td && me.currentSelectedArr.length == 0) {
                return -1;
            }
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true);
            if (td && me.currentSelectedArr.length == 0) {
                var count = (td.colSpan || 1) - 1;
                me.currentSelectedArr.push(td);
                while (count) {
                    do {
                        td = td.nextSibling;
                    } while (td.nodeType == 3);
                    me.currentSelectedArr.push(td);
                    count--;
                }
            }
            while (td = me.currentSelectedArr.pop()) {
                if (!domUtils.findParentByTagName(td, "table")) {
                    continue;
                }
                var tr = td.parentNode,table = tr.parentNode.parentNode,cellIndex = getIndex(td),rows = table.rows,cells = tr.cells,rowIndex = tr.rowIndex;
                var rowSpan;
                for (var currentRowIndex = 0; currentRowIndex < rows.length;) {
                    var currentNode = rows[currentRowIndex].cells[cellIndex];
                    if (_isHide(currentNode)) {
                        var leftNode = rows[currentNode.getAttribute("rootRowIndex")].cells[currentNode.getAttribute("rootCellIndex")];
                        rowSpan = leftNode.rowSpan;
                        for (var i = 0; i < leftNode.rowSpan; i++) {
                            var delNode = rows[currentRowIndex + i].cells[cellIndex];
                            domUtils.remove(delNode);
                        }
                        leftNode.colSpan--;
                        currentRowIndex += rowSpan;
                    } else {
                        if (currentNode.colSpan == 1) {
                            rowSpan = currentNode.rowSpan;
                            for (var i = currentRowIndex,l = currentRowIndex + currentNode.rowSpan; i < l; i++) {
                                domUtils.remove(rows[i].cells[cellIndex]);
                            }
                            currentRowIndex += rowSpan;
                        } else {
                            var rightNode = rows[currentRowIndex].cells[cellIndex + 1];
                            rightNode.style.display = "";
                            rightNode.rowSpan = currentNode.rowSpan;
                            rightNode.colSpan = currentNode.colSpan - 1;
                            currentRowIndex += currentNode.rowSpan;
                            domUtils.remove(currentNode);
                        }
                    }
                }
                var preColTd,focusTd,nextColTd;
                if (cellIndex == cells.length) {
                    if (cellIndex == 0) {
                        var p = table.ownerDocument.createElement("p");
                        p.innerHTML = browser.ie ? "&nbsp;" : "<br/>";
                        table.parentNode.insertBefore(p, table);
                        domUtils.remove(table);
                        range.setStart(p, 0).setCursor();
                        return;
                    }
                    var preCellIndex = cellIndex - 1;
                    preColTd = rows[rowIndex].cells[preCellIndex];
                    focusTd = _isHide(preColTd) ? rows[preColTd.getAttribute("rootRowIndex")].cells[preColTd.getAttribute("rootCellIndex")] : preColTd;
                } else {
                    nextColTd = rows[rowIndex].cells[cellIndex];
                    focusTd = _isHide(nextColTd) ? rows[nextColTd.getAttribute("rootRowIndex")].cells[nextColTd.getAttribute("rootCellIndex")] : nextColTd;
                }
            }
            range.setStart(focusTd, 0).setCursor();
            update(table);
        }};
        me.commands["splittocells"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true);
            return td && (td.rowSpan > 1 || td.colSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true),tr = td.parentNode,table = tr.parentNode.parentNode;
            var rowIndex = tr.rowIndex,cellIndex = getIndex(td),rowSpan = td.rowSpan,colSpan = td.colSpan;
            for (var i = 0; i < rowSpan; i++) {
                for (var j = 0; j < colSpan; j++) {
                    var cell = table.rows[rowIndex + i].cells[cellIndex + j];
                    cell.rowSpan = 1;
                    cell.colSpan = 1;
                    if (_isHide(cell)) {
                        cell.style.display = "";
                        cell.innerHTML = browser.ie ? "" : "<br/>";
                    }
                }
            }
        }};
        me.commands["splittorows"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, "td", true) || me.currentSelectedArr[0];
            return td && (td.rowSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, "td", true) || me.currentSelectedArr[0],tr = td.parentNode,rows = tr.parentNode.parentNode.rows;
            var rowIndex = tr.rowIndex,cellIndex = getIndex(td),rowSpan = td.rowSpan,colSpan = td.colSpan;
            for (var i = 0; i < rowSpan; i++) {
                var cells = rows[rowIndex + i],cell = cells.cells[cellIndex];
                cell.rowSpan = 1;
                cell.colSpan = colSpan;
                if (_isHide(cell)) {
                    cell.style.display = "";
                    cell.innerHTML = browser.ie ? "" : "<br/>";
                }
                for (var j = cellIndex + 1; j < cellIndex + colSpan; j++) {
                    cell = cells.cells[j];
                    cell.setAttribute("rootRowIndex", rowIndex + i);
                }
            }
            clearSelectedTd(me.currentSelectedArr);
            this.selection.getRange().setStart(td, 0).setCursor();
        }};
        me.commands["insertparagraphbeforetable"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, "td", true) || me.currentSelectedArr[0];
            return td && domUtils.findParentByTagName(td, "table") ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,table = domUtils.findParentByTagName(start, "table", true);
            start = me.document.createElement(me.options.enterTag);
            table.parentNode.insertBefore(start, table);
            clearSelectedTd(me.currentSelectedArr);
            if (start.tagName == "P") {
                start.innerHTML = browser.ie ? "" : "<br/>";
                range.setStart(start, 0);
            } else {
                range.setStartBefore(start);
            }
            range.setCursor();
        }};
        me.commands["splittocols"] = {queryCommandState:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true) || me.currentSelectedArr[0];
            return td && (td.colSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) == 1) ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true) || me.currentSelectedArr[0],tr = td.parentNode,rows = tr.parentNode.parentNode.rows;
            var rowIndex = tr.rowIndex,cellIndex = getIndex(td),rowSpan = td.rowSpan,colSpan = td.colSpan;
            for (var i = 0; i < colSpan; i++) {
                var cell = rows[rowIndex].cells[cellIndex + i];
                cell.rowSpan = rowSpan;
                cell.colSpan = 1;
                if (_isHide(cell)) {
                    cell.style.display = "";
                    cell.innerHTML = browser.ie ? "" : "<br/>";
                }
                for (var j = rowIndex + 1; j < rowIndex + rowSpan; j++) {
                    var tmpCell = rows[j].cells[cellIndex + i];
                    tmpCell.setAttribute("rootCellIndex", cellIndex + i);
                }
            }
            clearSelectedTd(me.currentSelectedArr);
            this.selection.getRange().setStart(td, 0).setCursor();
        }};
        me.commands["insertrow"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return domUtils.findParentByTagName(range.startContainer, "table", true) || domUtils.findParentByTagName(range.endContainer, "table", true) || me.currentSelectedArr.length != 0 ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,tr = domUtils.findParentByTagName(start, "tr", true) || me.currentSelectedArr[0].parentNode,table = tr.parentNode.parentNode,rows = table.rows;
            var rowIndex = tr.rowIndex,cells = rows[rowIndex].cells;
            var newRow = table.insertRow(rowIndex);
            var newCell;
            for (var cellIndex = 0; cellIndex < cells.length;) {
                var tmpCell = cells[cellIndex];
                if (_isHide(tmpCell)) {
                    var topCell = rows[tmpCell.getAttribute("rootRowIndex")].cells[tmpCell.getAttribute("rootCellIndex")];
                    topCell.rowSpan++;
                    for (var i = 0; i < topCell.colSpan; i++) {
                        newCell = tmpCell.cloneNode(false);
                        newCell.rowSpan = newCell.colSpan = 1;
                        newCell.innerHTML = browser.ie ? "" : "<br/>";
                        newCell.className = "";
                        if (newRow.children[cellIndex + i]) {
                            newRow.insertBefore(newCell, newRow.children[cellIndex + i]);
                        } else {
                            newRow.appendChild(newCell);
                        }
                        newCell.style.display = "none";
                    }
                    cellIndex += topCell.colSpan;
                } else {
                    for (var j = 0; j < tmpCell.colSpan; j++) {
                        newCell = tmpCell.cloneNode(false);
                        newCell.rowSpan = newCell.colSpan = 1;
                        newCell.innerHTML = browser.ie ? "" : "<br/>";
                        newCell.className = "";
                        if (newRow.children[cellIndex + j]) {
                            newRow.insertBefore(newCell, newRow.children[cellIndex + j]);
                        } else {
                            newRow.appendChild(newCell);
                        }
                    }
                    cellIndex += tmpCell.colSpan;
                }
            }
            update(table);
            range.setStart(newRow.cells[0], 0).setCursor();
            clearSelectedTd(me.currentSelectedArr);
        }};
        me.commands["insertcol"] = {queryCommandState:function() {
            var range = this.selection.getRange();
            return domUtils.findParentByTagName(range.startContainer, "table", true) || domUtils.findParentByTagName(range.endContainer, "table", true) || me.currentSelectedArr.length != 0 ? 0 : -1;
        },execCommand:function() {
            var range = this.selection.getRange(),start = range.startContainer,td = domUtils.findParentByTagName(start, ["td","th"], true) || me.currentSelectedArr[0],table = domUtils.findParentByTagName(td, "table"),rows = table.rows;
            var cellIndex = getIndex(td),newCell;
            for (var rowIndex = 0; rowIndex < rows.length;) {
                var tmpCell = rows[rowIndex].cells[cellIndex],tr;
                if (_isHide(tmpCell)) {
                    var leftCell = rows[tmpCell.getAttribute("rootRowIndex")].cells[tmpCell.getAttribute("rootCellIndex")];
                    leftCell.colSpan++;
                    for (var i = 0; i < leftCell.rowSpan; i++) {
                        newCell = td.cloneNode(false);
                        newCell.rowSpan = newCell.colSpan = 1;
                        newCell.innerHTML = browser.ie ? "" : "<br/>";
                        newCell.className = "";
                        tr = rows[rowIndex + i];
                        if (tr.children[cellIndex]) {
                            tr.insertBefore(newCell, tr.children[cellIndex]);
                        } else {
                            tr.appendChild(newCell);
                        }
                        newCell.style.display = "none";
                    }
                    rowIndex += leftCell.rowSpan;
                } else {
                    for (var j = 0; j < tmpCell.rowSpan; j++) {
                        newCell = td.cloneNode(false);
                        newCell.rowSpan = newCell.colSpan = 1;
                        newCell.innerHTML = browser.ie ? "" : "<br/>";
                        newCell.className = "";
                        tr = rows[rowIndex + j];
                        if (tr.children[cellIndex]) {
                            tr.insertBefore(newCell, tr.children[cellIndex]);
                        } else {
                            tr.appendChild(newCell);
                        }
                        newCell.innerHTML = browser.ie ? "" : "<br/>";
                    }
                    rowIndex += tmpCell.rowSpan;
                }
            }
            update(table);
            range.setStart(rows[0].cells[cellIndex], 0).setCursor();
            clearSelectedTd(me.currentSelectedArr);
        }};
        me.commands["mergecells"] = {queryCommandState:function() {
            var count = 0;
            for (var i = 0,ti; ti = this.currentSelectedArr[i++];) {
                if (!_isHide(ti)) {
                    count++;
                }
            }
            return count > 1 ? 0 : -1;
        },execCommand:function() {
            var start = me.currentSelectedArr[0],end = me.currentSelectedArr[me.currentSelectedArr.length - 1],table = domUtils.findParentByTagName(start, "table"),rows = table.rows,cellsRange = {beginRowIndex:start.parentNode.rowIndex,beginCellIndex:getIndex(start),endRowIndex:end.parentNode.rowIndex,endCellIndex:getIndex(end)},beginRowIndex = cellsRange.beginRowIndex,beginCellIndex = cellsRange.beginCellIndex,rowsLength = cellsRange.endRowIndex - cellsRange.beginRowIndex + 1,cellLength = cellsRange.endCellIndex - cellsRange.beginCellIndex + 1,tmp = rows[beginRowIndex].cells[beginCellIndex];
            for (var i = 0,ri; (ri = rows[beginRowIndex + i++]) && i <= rowsLength;) {
                for (var j = 0,ci; (ci = ri.cells[beginCellIndex + j++]) && j <= cellLength;) {
                    if (i == 1 && j == 1) {
                        ci.style.display = "";
                        ci.rowSpan = rowsLength;
                        ci.colSpan = cellLength;
                    } else {
                        ci.style.display = "none";
                        ci.rowSpan = 1;
                        ci.colSpan = 1;
                        ci.setAttribute("rootRowIndex", beginRowIndex);
                        ci.setAttribute("rootCellIndex", beginCellIndex);
                        _moveContent(tmp, ci);
                    }
                }
            }
            this.selection.getRange().setStart(tmp, 0).setCursor();
            clearSelectedTd(me.currentSelectedArr);
        }};
        function _moveContent(cellTo, cellFrom) {
            if (_isEmpty(cellFrom)) {
                return;
            }
            if (_isEmpty(cellTo)) {
                cellTo.innerHTML = cellFrom.innerHTML;
                return;
            }
            var child = cellTo.lastChild;
            if (child.nodeType != 1 || child.tagName != "BR") {
                cellTo.appendChild(cellTo.ownerDocument.createElement("br"));
            }
            while (child = cellFrom.firstChild) {
                cellTo.appendChild(child);
            }
        }

        function _getCellsRange(cellA, cellB) {
            var trA = cellA.parentNode,trB = cellB.parentNode,aRowIndex = trA.rowIndex,bRowIndex = trB.rowIndex,rows = trA.parentNode.parentNode.rows,rowsNum = rows.length,cellsNum = rows[0].cells.length,cellAIndex = getIndex(cellA),cellBIndex = getIndex(cellB);
            if (cellA == cellB) {
                return {beginRowIndex:aRowIndex,beginCellIndex:cellAIndex,endRowIndex:aRowIndex + cellA.rowSpan - 1,endCellIndex:cellBIndex + cellA.colSpan - 1};
            }
            var beginRowIndex = Math.min(aRowIndex, bRowIndex),beginCellIndex = Math.min(cellAIndex, cellBIndex),endRowIndex = Math.max(aRowIndex + cellA.rowSpan - 1, bRowIndex + cellB.rowSpan - 1),endCellIndex = Math.max(cellAIndex + cellA.colSpan - 1, cellBIndex + cellB.colSpan - 1);
            while (1) {
                var tmpBeginRowIndex = beginRowIndex,tmpBeginCellIndex = beginCellIndex,tmpEndRowIndex = endRowIndex,tmpEndCellIndex = endCellIndex;
                if (beginRowIndex > 0) {
                    for (cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentTopTd = rows[beginRowIndex].cells[cellIndex];
                        if (_isHide(currentTopTd)) {
                            beginRowIndex = currentTopTd.getAttribute("rootRowIndex");
                            currentTopTd = rows[currentTopTd.getAttribute("rootRowIndex")].cells[currentTopTd.getAttribute("rootCellIndex")];
                        }
                        cellIndex = getIndex(currentTopTd) + (currentTopTd.colSpan || 1);
                    }
                }
                if (beginCellIndex > 0) {
                    for (var rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentLeftTd = rows[rowIndex].cells[beginCellIndex];
                        if (_isHide(currentLeftTd)) {
                            beginCellIndex = currentLeftTd.getAttribute("rootCellIndex");
                            currentLeftTd = rows[currentLeftTd.getAttribute("rootRowIndex")].cells[currentLeftTd.getAttribute("rootCellIndex")];
                        }
                        rowIndex = currentLeftTd.parentNode.rowIndex + (currentLeftTd.rowSpan || 1);
                    }
                }
                if (endRowIndex < rowsNum) {
                    for (var cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentDownTd = rows[endRowIndex].cells[cellIndex];
                        if (_isHide(currentDownTd)) {
                            currentDownTd = rows[currentDownTd.getAttribute("rootRowIndex")].cells[currentDownTd.getAttribute("rootCellIndex")];
                        }
                        endRowIndex = currentDownTd.parentNode.rowIndex + currentDownTd.rowSpan - 1;
                        cellIndex = getIndex(currentDownTd) + (currentDownTd.colSpan || 1);
                    }
                }
                if (endCellIndex < cellsNum) {
                    for (rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentRightTd = rows[rowIndex].cells[endCellIndex];
                        if (_isHide(currentRightTd)) {
                            currentRightTd = rows[currentRightTd.getAttribute("rootRowIndex")].cells[currentRightTd.getAttribute("rootCellIndex")];
                        }
                        endCellIndex = getIndex(currentRightTd) + currentRightTd.colSpan - 1;
                        rowIndex = currentRightTd.parentNode.rowIndex + (currentRightTd.rowSpan || 1);
                    }
                }
                if (tmpBeginCellIndex == beginCellIndex && tmpEndCellIndex == endCellIndex && tmpEndRowIndex == endRowIndex && tmpBeginRowIndex == beginRowIndex) {
                    break;
                }
            }
            return {beginRowIndex:beginRowIndex,beginCellIndex:beginCellIndex,endRowIndex:endRowIndex,endCellIndex:endCellIndex};
        }

        function _mouseDownEvent(type, evt) {
            if (evt.button == 2) {
                return;
            }
            me.document.body.style.webkitUserSelect = "";
            anchorTd = evt.target || evt.srcElement;
            clearSelectedTd(me.currentSelectedArr);
            domUtils.clearSelectedArr(me.currentSelectedArr);
            if (anchorTd.tagName !== "TD") {
                anchorTd = domUtils.findParentByTagName(anchorTd, "td") || anchorTd;
            }
            if (anchorTd.tagName == "TD") {
                me.addListener("mouseover", function(type, evt) {
                    var tmpTd = evt.target || evt.srcElement;
                    _mouseOverEvent.call(me, tmpTd);
                    evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                });
            } else {
                reset();
            }
        }

        function _mouseOverEvent(tmpTd) {
            if (anchorTd && tmpTd.tagName == "TD") {
                me.document.body.style.webkitUserSelect = "none";
                var table = tmpTd.parentNode.parentNode.parentNode;
                me.selection.getNative()[browser.ie ? "empty" : "removeAllRanges"]();
                var range = _getCellsRange(anchorTd, tmpTd);
                _toggleSelect(table, range);
            }
        }

        function _toggleSelect(table, cellsRange) {
            var rows = table.rows;
            clearSelectedTd(me.currentSelectedArr);
            for (var i = cellsRange.beginRowIndex; i <= cellsRange.endRowIndex; i++) {
                for (var j = cellsRange.beginCellIndex; j <= cellsRange.endCellIndex; j++) {
                    var td = rows[i].cells[j];
                    td.className = me.options.selectedTdClass;
                    me.currentSelectedArr.push(td);
                }
            }
        }

        function update(table) {
            var tds = table.getElementsByTagName("td"),rowIndex,cellIndex,rows = table.rows;
            for (var j = 0,tj; tj = tds[j++];) {
                if (!_isHide(tj)) {
                    rowIndex = tj.parentNode.rowIndex;
                    cellIndex = getIndex(tj);
                    for (var r = 0; r < tj.rowSpan; r++) {
                        var c = r == 0 ? 1 : 0;
                        for (; c < tj.colSpan; c++) {
                            var tmp = rows[rowIndex + r].children[cellIndex + c];
                            tmp.setAttribute("rootRowIndex", rowIndex);
                            tmp.setAttribute("rootCellIndex", cellIndex);
                        }
                    }
                }
            }
        }

        me.adjustTable = function(cont) {
            var table = cont.getElementsByTagName("table");
            for (var i = 0,ti; ti = table[i++];) {
                if (ti.getAttribute("border") == "0" || !ti.getAttribute("border")) {
                    ti.setAttribute("border", 1);
                }
                if (domUtils.getComputedStyle(ti, "border-color") == "#ffffff") {
                    ti.setAttribute("borderColor", "#000");
                }
                var tds = domUtils.getElementsByTagName(ti, "td"),td,tmpTd;
                for (var j = 0,tj; tj = tds[j++];) {
                    var index = getIndex(tj),rowIndex = tj.parentNode.rowIndex,rows = domUtils.findParentByTagName(tj, "table").rows;
                    for (var r = 0; r < tj.rowSpan; r++) {
                        var c = r == 0 ? 1 : 0;
                        for (; c < tj.colSpan; c++) {
                            if (!td) {
                                td = tj.cloneNode(false);
                                td.rowSpan = td.colSpan = 1;
                                td.style.display = "none";
                                td.innerHTML = browser.ie ? "" : "<br/>";
                            } else {
                                td = td.cloneNode(true);
                            }
                            td.setAttribute("rootRowIndex", tj.parentNode.rowIndex);
                            td.setAttribute("rootCellIndex", index);
                            if (r == 0) {
                                if (tj.nextSibling) {
                                    tj.parentNode.insertBefore(td, tj.nextSibling);
                                } else {
                                    tj.parentNode.appendChild(td);
                                }
                            } else {
                                tmpTd = rows[rowIndex + r].children[index];
                                if (tmpTd) {
                                    tmpTd.parentNode.insertBefore(td, tmpTd);
                                } else {
                                    rows[rowIndex + r].appendChild(td);
                                }
                            }
                        }
                    }
                }
            }
        };
    };
})();
(function() {
    var editor = baidu.editor,domUtils = editor.dom.domUtils,notBreakTags = ["td"];
    baidu.editor.plugins["pagebreak"] = function() {
        var me = this;
        me.commands["pagebreak"] = {execCommand:function() {
            var range = me.selection.getRange();
            var div = me.document.createElement("div");
            div.className = "pagebreak";
            domUtils.unselectable(div);
            var node = domUtils.findParentByTagName(range.startContainer, notBreakTags, true),parents = [],pN;
            if (node) {
                switch (node.tagName) {
                    case "TD":
                        pN = node.parentNode;
                        if (!pN.previousSibling) {
                            var table = domUtils.findParentByTagName(pN, "table");
                            table.parentNode.insertBefore(div, table);
                            parents = domUtils.findParents(div, true);
                        } else {
                            pN.parentNode.insertBefore(div, pN);
                            parents = domUtils.findParents(div);
                        }
                        pN = parents[1];
                        if (div !== pN) {
                            domUtils.breakParent(div, pN);
                        }
                        range.moveToBookmark(bk).select();
                        domUtils.clearSelectedArr(me.currentSelectedArr);
                }
            } else {
                if (!range.collapsed) {
                    range.deleteContents();
                    var start = range.startContainer;
                    while (domUtils.isBlockElm(start) && domUtils.isEmptyNode(start)) {
                        range.setStartBefore(start).collapse(true);
                        domUtils.remove(start);
                        start = range.startContainer;
                    }
                }
                parents = domUtils.findParents(range.startContainer, true);
                pN = parents[1];
                range.insertNode(div);
                pN && domUtils.breakParent(div, pN);
                range.setEndAfter(div).setCursor(true, true);
            }
        }};
    };
})();
baidu.editor.plugins["basestyle"] = function() {
    var basestyles = {"bold":["strong","b"],"italic":["em","i"],"subscript":["sub"],"superscript":["sup"]},domUtils = baidu.editor.dom.domUtils,getObj = function(editor, tagNames) {
        var start = editor.selection.getStart();
        return domUtils.findParentByTagName(start, tagNames, true);
    },flag = 0;
    for (var style in basestyles) {
        (function(cmd, tagNames) {
            baidu.editor.commands[cmd] = {execCommand:function(cmdName) {
                var range = new baidu.editor.dom.Range(this.document),obj = "",me = this;
                if (!flag) {
                    this.addListener("beforegetcontent", function() {
                        domUtils.clearReduent(me.document, ["strong","u","em","sup","sub","strike"]);
                    });
                    flag = 1;
                }
                if (me.currentSelectedArr && me.currentSelectedArr.length > 0) {
                    for (var i = 0,ci; ci = me.currentSelectedArr[i++];) {
                        if (ci.style.display != "none") {
                            range.selectNodeContents(ci).select();
                            !obj && (obj = getObj(this, tagNames));
                            if (cmdName == "superscript" || cmdName == "subscript") {
                                if (!obj || obj.tagName.toLowerCase() != cmdName) {
                                    range.removeInlineStyle(["sub","sup"]);
                                }
                            }
                            obj ? range.removeInlineStyle(tagNames) : range.applyInlineStyle(tagNames[0]);
                        }
                    }
                    range.selectNodeContents(me.currentSelectedArr[0]).select();
                } else {
                    range = me.selection.getRange();
                    obj = getObj(this, tagNames);
                    if (range.collapsed) {
                        if (obj) {
                            var tmpText = me.document.createTextNode("");
                            range.insertNode(tmpText).removeInlineStyle(tagNames);
                            range.setStartBefore(tmpText);
                            domUtils.remove(tmpText);
                        } else {
                            var tmpNode = range.document.createElement(tagNames[0]);
                            if (cmdName == "superscript" || cmdName == "subscript") {
                                tmpText = me.document.createTextNode("");
                                range.insertNode(tmpText).removeInlineStyle(["sub","sup"]).setStartBefore(tmpText).collapse(true);
                            }
                            range.insertNode(tmpNode).setStart(tmpNode, 0);
                        }
                        range.collapse(true);
                    } else {
                        if (cmdName == "superscript" || cmdName == "subscript") {
                            if (!obj || obj.tagName.toLowerCase() != cmdName) {
                                range.removeInlineStyle(["sub","sup"]);
                            }
                        }
                        obj ? range.removeInlineStyle(tagNames) : range.applyInlineStyle(tagNames[0]);
                    }
                    range.select();
                }
                return true;
            },queryCommandState:function() {
                return getObj(this, tagNames) ? 1 : 0;
            }};
        })(style, basestyles[style]);
    }
};
baidu.editor.plugins["elementpath"] = function() {
    var domUtils = baidu.editor.dom.domUtils,currentLevel,tagNames,dtd = baidu.editor.dom.dtd;
    baidu.editor.commands["elementpath"] = {execCommand:function(cmdName, level) {
        var me = this,start = tagNames[level],range = me.selection.getRange();
        me.currentSelectedArr && domUtils.clearSelectedArr(me.currentSelectedArr);
        currentLevel = level * 1;
        if (dtd.$tableContent[start.tagName]) {
            switch (start.tagName) {
                case "TD":
                    me.currentSelectedArr = [start];
                    start.className = me.options.selectedTdClass;
                    break;
                case "TR":
                    var cells = start.cells;
                    for (var i = 0,ti; ti = cells[i++];) {
                        me.currentSelectedArr.push(ti);
                        ti.className = me.options.selectedTdClass;
                    }
                    break;
                case "TABLE":
                case "TBODY":
                    var rows = start.rows;
                    for (var i = 0,ri; ri = rows[i++];) {
                        cells = ri.cells;
                        for (var j = 0,tj; tj = cells[j++];) {
                            me.currentSelectedArr.push(tj);
                            tj.className = me.options.selectedTdClass;
                        }
                    }
            }
            start = me.currentSelectedArr[0];
            if (domUtils.isEmptyNode(start)) {
                range.setStart(start, 0).setCursor();
            } else {
                range.selectNodeContents(start).select();
            }
        } else {
            range.selectNode(start).select();
        }
    },queryCommandValue:function() {
        var start = this.selection.getStart(),parents = domUtils.findParents(start, true),names = [];
        tagNames = parents;
        for (var i = 0,ci; ci = parents[i]; i++) {
            if (ci.nodeType == 3) {
                continue;
            }
            var name = ci.tagName.toLowerCase();
            if (name == "img" && ci.getAttribute("anchorname")) {
                name = "anchor";
            }
            names[i] = name;
            if (currentLevel == i) {
                currentLevel = -1;
                break;
            }
        }
        return names;
    }};
};
baidu.editor.plugins["formatmatch"] = function() {
    var me = this,domUtils = baidu.editor.dom.domUtils,list = [],img,flag = 0,browser = baidu.editor.browser;
    this.addListener("reset", function() {
        list = [];
        flag = 0;
    });
    function addList(type, evt) {
        if (browser.webkit) {
            var target = evt.target.tagName == "IMG" ? evt.target : null;
        }
        function addFormat(range) {
            if (text && (!me.currentSelectedArr || !me.currentSelectedArr.length)) {
                range.selectNode(text);
            }
            return range.applyInlineStyle(list[list.length - 1].tagName, null, list);
        }

        me.undoManger && me.undoManger.save();
        var range = me.selection.getRange(),imgT = target || range.getClosedNode();
        if (img && imgT && imgT.tagName == "IMG") {
            imgT.style.cssText += ";float:" + (img.style.cssFloat || img.style.styleFloat || "none") + ";display:" + (img.style.display || "inline");
            img = null;
        } else {
            if (!img) {
                var collapsed = range.collapsed;
                if (collapsed) {
                    var text = me.document.createTextNode("match");
                    range.insertNode(text).select();
                }
                me.__hasEnterExecCommand = true;
                me.execCommand("removeformat");
                me.__hasEnterExecCommand = false;
                range = me.selection.getRange();
                if (list.length == 0) {
                    if (me.currentSelectedArr && me.currentSelectedArr.length > 0) {
                        range.selectNodeContents(me.currentSelectedArr[0]).select();
                    }
                } else {
                    if (me.currentSelectedArr && me.currentSelectedArr.length > 0) {
                        for (var i = 0,ci; ci = me.currentSelectedArr[i++];) {
                            range.selectNodeContents(ci);
                            addFormat(range);
                        }
                        range.selectNodeContents(me.currentSelectedArr[0]).select();
                    } else {
                        addFormat(range);
                    }
                }
                if (!me.currentSelectedArr || !me.currentSelectedArr.length) {
                    if (text) {
                        range.setStartBefore(text).collapse(true);
                    }
                    range.select();
                }
                text && domUtils.remove(text);
            }
        }
        me.undoManger && me.undoManger.save();
        me.removeListener("mouseup", addList);
        flag = 0;
    }

    baidu.editor.commands["formatmatch"] = {execCommand:function(cmdName) {
        if (flag) {
            flag = 0;
            list = [];
            me.removeListener("mouseup", addList);
            return;
        }
        var range = me.selection.getRange();
        img = range.getClosedNode();
        if (!img || img.tagName != "IMG") {
            range.collapse(true).shrinkBoundary();
            var start = range.startContainer;
            list = domUtils.findParents(start, true, function(node) {
                return !domUtils.isBlockElm(node) && node.nodeType == 1;
            });
            for (var i = 0,ci; ci = list[i]; i++) {
                if (ci.tagName == "A") {
                    list.splice(i, 1);
                    break;
                }
            }
        }
        me.addListener("mouseup", addList);
        flag = 1;
    },queryCommandState:function() {
        return flag;
    },notNeedUndo:1};
};
baidu.editor.plugins["searchreplace"] = function() {
    var currentRange,first;
    this.addListener("reset", function() {
        currentRange = null;
        first = null;
    });
    baidu.editor.commands["searchreplace"] = {execCommand:function(cmdName, opt) {
        var editor = this,sel = editor.selection,range,nativeRange,num = 0,opt = baidu.editor.utils.extend(opt, {replaceStr:null,all:false,casesensitive:false,dir:1}, true);
        if (baidu.editor.browser.ie) {
            while (1) {
                var tmpRange;
                nativeRange = editor.document.selection.createRange();
                tmpRange = nativeRange.duplicate();
                tmpRange.moveToElementText(editor.document.body);
                if (opt.all) {
                    first = 0;
                    opt.dir = 1;
                    if (currentRange) {
                        tmpRange.setEndPoint(opt.dir == -1 ? "EndToStart" : "StartToEnd", currentRange);
                    }
                } else {
                    tmpRange.setEndPoint(opt.dir == -1 ? "EndToStart" : "StartToEnd", nativeRange);
                    if (opt.replaceStr) {
                        tmpRange.setEndPoint(opt.dir == -1 ? "StartToEnd" : "EndToStart", nativeRange);
                    }
                }
                nativeRange = tmpRange.duplicate();
                if (!tmpRange.findText(opt.searchStr, opt.dir, opt.casesensitive ? 4 : 0)) {
                    tmpRange = editor.document.selection.createRange();
                    tmpRange.scrollIntoView();
                    return num;
                }
                tmpRange.select();
                if (opt.replaceStr) {
                    range = sel.getRange();
                    range.deleteContents().insertNode(range.document.createTextNode(opt.replaceStr)).select();
                    currentRange = sel.getNative().createRange();
                }
                num++;
                if (!opt.all) {
                    break;
                }
            }
        } else {
            var w = editor.window,nativeSel = sel.getNative(),tmpRange;
            while (1) {
                if (opt.all) {
                    if (currentRange) {
                        currentRange.collapse(false);
                        nativeRange = currentRange;
                    } else {
                        nativeRange = editor.document.createRange();
                        nativeRange.setStart(editor.document.body, 0);
                    }
                    nativeSel.removeAllRanges();
                    nativeSel.addRange(nativeRange);
                    first = 0;
                    opt.dir = 1;
                } else {
                    nativeRange = w.getSelection().getRangeAt(0);
                    if (opt.replaceStr) {
                        nativeRange.collapse(opt.dir == 1 ? true : false);
                    }
                }
                if (!first) {
                    nativeRange.collapse(opt.dir < 0 ? true : false);
                    nativeSel.removeAllRanges();
                    nativeSel.addRange(nativeRange);
                } else {
                    nativeSel.removeAllRanges();
                }
                if (!w.find(opt.searchStr, opt.casesensitive, opt.dir < 0 ? true : false)) {
                    nativeSel.removeAllRanges();
                    return num;
                }
                first = 0;
                range = w.getSelection().getRangeAt(0);
                if (!range.collapsed) {
                    if (opt.replaceStr) {
                        range.deleteContents();
                        var text = w.document.createTextNode(opt.replaceStr);
                        range.insertNode(text);
                        range.selectNode(text);
                        nativeSel.addRange(range);
                        currentRange = range.cloneRange();
                    }
                }
                num++;
                if (!opt.all) {
                    break;
                }
            }
        }
        return true;
    }};
};
/**
 * @author frank.zhang
 */
dorado.widget.ColorPicker = $extend([dorado.widget.Control, dorado.widget.FloatControl], {
	focusable: true,
	ATTRIBUTES: {
		className: {
			defaultValue: "d-color-picker"
		},
		animateType: {
			defaultValue: "slide"
		},
		focusAfterShow: {
			defaultValue: true
		},
		mode: {
			defaultValue: "simple",
			setter: function(value) {
				var picker = this, dom = picker._dom;
				if (dom) {
					$fly(dom)[value == "more" ? "addClass" : "removeClass"](picker._className + "-more");
				}
				picker._mode = value;
			}
		},
		hideAfterSelect: {
			defaultValue: true
		},
		hideAfterClear: {
			defaultValue: true
		}
	},
	EVENTS: {
		onSelect: {},
		onClear: {}
	},
	createDom: function() {
		var picker = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: picker._className,
			content: [{
				tagName: "div",
				className: "clear-color",
				contextKey: "clearColor",
				content:  "去除颜色"
			}, {
				tagName: "div",
				className: "color-preview",
				contextKey: "colorPreview"
			}, {
				tagName: "table",
				className: "simple-color-table",
				contextKey: "simpleColorTable",
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "simpleColorTableBody"
				}]
			}, {
				tagName: "div",
				className: "more-color",
				contextKey: "moreColor",
				content: "更多颜色..."
			}, {
				tagName: "table",
				className: "more-color-table",
				contextKey: "moreColorTable",
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "moreColorTableBody"
				}]
			}]
		}, null, doms);

		jQuery(doms.clearColor).click(function() {
			picker.fireEvent("onClear", picker, {});
			if (picker._hideAfterClear) {
				picker.hide();
			}
		}).addClassOnHover("clear-color-over");

		jQuery(doms.moreColor).click(function() {
			picker.set("mode", "more");
		}).addClassOnHover("more-color-over");

		var simpleColor = [
			["#000000","#993300","#333300","#003300","#003366","#000080","#333399","#333333"],
			["#800000","#FF6600","#808000","#008000","#008080","#0000FF","#666699","#808080"],
			["#FF0000","#FF9900","#99CC00","#339966","#33CCCC","#3366FF","#800080","#999999"],
			["#FF00FF","#FFCC00","#FFFF00","#00FF00","#00FFFF","#00CCFF","#993366","#C0C0C0"],
			["#FF99CC","#FFCC99","#FFFF99","#CCFFCC","#CCFFFF","#99CCFF","#CC99FF","#FFFFFF"]
		], i, j, tr, td;

		var simpleColorTableBody = doms.simpleColorTableBody;
		for(i = 0;i < 5;i++){
			tr = document.createElement("tr");
			for(j = 0;j < 8;j++){
				td = document.createElement("td");
				td.bgColor = simpleColor[i][j];
				td.innerHTML = "&nbsp;";
				tr.appendChild(td);
			}
			simpleColorTableBody.appendChild(tr);
		}

		function getColor(row,column){
			var colorArray = ["00","33","66","99","CC","FF"];
			var result = "#";
			//red color
			result += colorArray[row > 5 ? Math.floor(column / 6) + 3 : Math.floor(column / 6)];
			//green color
			result += colorArray[column % 6 == 6 ? 0 : column % 6];
			//blue color
			result += colorArray[row <= 5 ? row : row - 6];
			return result;
		}

		var moreColorTableBody = doms.moreColorTableBody;
		for(i = 0; i < 12; i++){
			tr = document.createElement("tr");
			for(j = 0; j < 18; j++){
				td = document.createElement("td");
				td.bgColor = getColor(i,j);
				td.innerHTML = "&nbsp;";
				tr.appendChild(td);
			}
			moreColorTableBody.appendChild(tr);
		}

		var colorTableMouseOver = function(event){
			var position = $DomUtils.getCellPosition(event) || {}, row = position.row,
				column = position.column, element = position.element;
			if (row != -1 && column != -1 && element) {
				doms.colorPreview.style.backgroundColor = element.bgColor;
			}
		};

		var colorTableClick = function(event){
			var position = $DomUtils.getCellPosition(event) || {}, row = position.row,
				column = position.column, element = position.element;

			if (row != -1 && column != -1 && element) {
				picker.fireEvent("onSelect", picker, { color: element.bgColor });
				if (picker._hideAfterSelect) {
					picker.hide();
				}
			}
		};

		$fly(doms.simpleColorTable).mouseover(colorTableMouseOver).click(colorTableClick);
		$fly(doms.moreColorTable).mouseover(colorTableMouseOver).click(colorTableClick);

		$fly(dom)[picker._mode == "more" ? "addClass" : "removeClass"](picker._className + "-more");

		return dom;
	},
	onBlur: function() {
		if (this._visible) {
			this.hide();
		}
	}
});
/**
 * @author frank.zhang
 */
dorado.widget.EmoticonPicker = $extend([dorado.widget.Control, dorado.widget.FloatControl], {
	focusable: true,
	ATTRIBUTES: {
		className: {
			defaultValue: "d-emoticon-picker"
		},
		animateType: {
			defaultValue: "slide"
		},
		hideAfterSelect: {
			defaultValue: true
		}
	},
	EVENTS: {
		onSelect: {}
	},
	createDom: function() {
		var picker = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: picker._className,
			content: [{
				tagName: "table",
				contextKey: "emoticonTable",
				border: 0,
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "emoticonTableBody"
				}]
			}, {
				tagName: "div",
				className: "emoticon-preview",
				contextKey: "emoticonPreview",
				style: {
					display: "none"
				},
				content: [{
					tagName: "img"
				}]
			}]
		}, null, doms);

		picker._doms = doms;

		var emoticonTableBody = doms.emoticonTableBody;
		for(var i = 0; i < 7; i++){
			var tr = document.createElement("tr");
			for(var j = 0; j < 15; j++){
				var index = i * 15 + j, source = ">skin>/html-editor/emotion/" + index + ".gif";
				var td = document.createElement("td");
				td.className = "emoticon-cell";
				td.source = $url(source);

				tr.appendChild(td);
			}
			emoticonTableBody.appendChild(tr);
		}

		$fly(doms.emoticonTable).mouseover(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;

			if (element) {
				if (picker.lastElement) {
					$fly(picker.lastElement).removeClass("cur-emoticon-cell");
				}
				$fly(element).addClass("cur-emoticon-cell");
				picker.lastElement = element;
				doms.emoticonPreview.style.display = "";
				doms.emoticonPreview.firstChild.src = element.source;
			}
		}).mouseout(function() {
			doms.emoticonPreview.style.display = "none";
		}).click(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;
			var source = element.source;
			if (source) {
				picker.fireEvent("onSelect", picker, {image: source});
				if (picker._hideAfterSelect) {
					picker.hide();
				}
			}
		});

		$fly(doms.emoticonPreview).mouseover(function(){
			if (this.style.right == "auto") {
				this.style.right = 0;
				this.style.left = "auto";
			} else {
				this.style.left = 0;
				this.style.right = "auto";
			}
		});

		return dom;
	},
	onBlur: function() {
		if (this._visible) {
			this.hide();
		}
	}
});

dorado.widget.GridPicker = $extend(dorado.widget.Control, {
	focusable: true,
	ATTRIBUTES: {
		className: {
			defaultValue: "d-grid-picker"
		},
		animateType: {
			defaultValue: "slide"
		},
		hideAfterSelect: {
			defaultValue: true
		},
        column: {
            defaultValue: 10
        },
        elements: {}
	},
	EVENTS: {
		onSelect: {}
	},
	createDom: function() {
		var picker = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: picker._className,
			content: [{
				tagName: "table",
				contextKey: "gridTable",
				border: 0,
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "gridTableBody"
				}]
			}, {
				tagName: "div",
				className: "element-preview",
				contextKey: "elementPreview",
				style: {
					display: "none"
				},
				content: [{
                    tagName: "div"
				}]
			}]
		}, null, doms);

		picker._doms = doms;

		var gridTableBody = doms.gridTableBody, elements = picker._elements || [],
            elementsCount = elements.length, column = picker._column || 10, row = Math.ceil(elementsCount / column);

		for(var i = 0; i < row; i++){
			var tr = document.createElement("tr");
			for(var j = 0; j < column; j++){
				var index = i * column + j;
				var td = document.createElement("td");
				td.className = "element-cell";
                if (index < elementsCount) {
                    $fly(td).html(elements[index]);
                } else {
                    break;
                }
				tr.appendChild(td);
			}
			gridTableBody.appendChild(tr);
		}

		$fly(doms.gridTable).mouseover(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;

			if (element) {
				if (picker.lastElement) {
					$fly(picker.lastElement).removeClass("cur-element-cell");
				}
				$fly(element).addClass("cur-element-cell");
				picker.lastElement = element;
				doms.elementPreview.style.display = "";
				doms.elementPreview.firstChild.innerHTML = element.innerHTML;
			}
		}).mouseout(function() {
			doms.elementPreview.style.display = "none";
		}).click(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;
			var source = element.innerHTML;
			if (source) {
				picker.fireEvent("onSelect", picker, { element: source});
			}
		});

		$fly(doms.elementPreview).mouseover(function(){
			if (this.style.right == "auto") {
				this.style.right = 0;
				this.style.left = "auto";
			} else {
				this.style.left = 0;
				this.style.right = "auto";
			}
		});

		return dom;
	},
	onBlur: function() {
		if (this._visible) {
		}
	}
});
(function() {
    dorado.htmleditor = {
        //'Code'
        fullMode: ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','FormatMatch','|',
             'BlockQuote','|',
             'PastePlain','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'DirectionalityLtr','DirectionalityRtl','|','Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Anchor','Image','Emoticon', 'Flash', '|',
             'Horizontal','Date','Time','Spechars','Map','GMap','|',
             'InsertTable','DeleteTable','InsertParagraphBeforeTable','InsertRow','DeleteRow','InsertCol','DeleteCol','MergeCells','MergeRight','MergeDown','SplittoCells','SplittoRows','SplittoCols','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help'],
        simpleMode:
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Horizontal','Image','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help'],

        registerMode: function(name, config) {
            if (name && config) {
                dorado.htmleditor[name + "Mode"] = config;
            }
        },

        defaultLabelMap: {
            'anchor':'锚点',
            'undo': '撤销',
            'redo': '重做',
            'bold': '加粗',
            'indent':'首行缩进',
            'outdent':'取消缩进',
            'italic': '斜体',
            'underline': '下划线',
            'strikethrough': '删除线',
            'subscript': '下标',
            'superscript': '上标',
            'formatmatch': '格式刷',
            'source': '源代码',
            'blockquote': '引用',
            'pasteplain': '纯文本粘贴模式',
            'selectall': '全选',
            'print': '打印',
            'preview': '预览',
            'horizontal': '分隔线',
            'removeformat': '清除格式',
            'time': '时间',
            'date': '日期',
            'unlink': '祛除链接',
            'insertrow': '前插入行',
            'insertcol': '前插入列',
            'mergeright': '右合并单元格',
            'mergedown': '下合并单元格',
            'deleterow': '删除行',
            'deletecol': '删除列',
            'splittorows': '拆分成行',
            'splittocols': '拆分成列',
            'splittocells': '完全拆分单元格',
            'mergecells': '合并多个单元格',
            'deletetable': '删除表格',
    //        'tablesuper': '表格高级设置',
            'insertparagraphbeforetable': '表格前插行',
            'cleardoc': '清空文档',
            'fontfamily': '字体',
            'fontsize': '字号',
            'paragraph': '格式',
            'image': '图片',
            'inserttable': '表格',
            'link': '超链接',
            'emoticon': '表情',
            'spechars': '特殊字符',
            'searchreplace': '查询替换',
            'map': 'Baidu地图',
            'gmap': 'Google地图',
            'video': '视频',
            'help': '帮助',
            'justifyleft':'居左对齐',
            'justifyright':'居右对齐',
            'justifycenter':'居中对齐',
            'justifyjustify':'两端对齐',
            'forecolor' : '字体颜色',
            'backcolor' : '背景色',
            'insertorderedlist' : '有序列表',
            'insertunorderedlist' : '无序列表',
            'fullscreen' : '全屏',
            'directionalityltr' : '从左向右输入',
            'directionalityrtl' : '从右向左输入',
            'rowspacing' : '行距',
            'code' : '插入代码'
        },
        defaultListMap: {
            'fontfamily': ['宋体', '楷体', '隶书', '黑体','andale mono','arial','arial black','comic sans ms','impact','times new roman'],
            'fontsize': [10, 11, 12, 14, 16, 18, 20, 24, 36],
            'underline':['none','overline','line-through','underline'],
            'paragraph': ['p:Paragraph', 'h1:Heading 1', 'h2:Heading 2', 'h3:Heading 3', 'h4:Heading 4', 'h5:Heading 5', 'h6:Heading 6'],
            'rowspacing' : ['1.0:0','1.5:15','2.0:20','2.5:25','3.0:30']
        },
        FONT_MAP: {
            '宋体': ['宋体', 'SimSun'],
            '楷体': ['楷体', '楷体_GB2312', 'SimKai'],
            '黑体': ['黑体', 'SimHei'],
            '隶书': ['隶书', 'SimLi'],
            'andale mono' : ['andale mono'],
            'arial' : ['arial','helvetica','sans-serif'],
            'arial black' : ['arial black','avant garde'],
            'comic sans ms' : ['comic sans ms'],
            'impact' : ['impact','chicago'],
            'times new roman' : ['times new roman']
        }
    };

    dorado.htmleditor.ToolBar = $extend(dorado.widget.Control, {
        focusable: true,
        ATTRIBUTES: {
            className: {
                defaultValue: "d-htmleditor-toolbar"
            },
            items: {}
        },
        createItem: function(config) {
            if (!config) return null;
            if (typeof config == "string" || config.constructor == Object.prototype.constructor) {
                var result = dorado.Toolkits.createInstance("toolbar,widget", config);
                result._parent = result._focusParent = this;
                return result;
            } else {
                config._parent = config._focusParent = this;
                return config;
            }
        },
        addItem: function(item, index) {
            var toolbar = this, items = toolbar._items;
            if (!item) return null;
            if (!items) {
                items = toolbar._items = new dorado.util.KeyedArray(function(value) {
                    return value._id;
                });
            }
            item = toolbar.createItem(item);
            if (toolbar._rendered) {
                var refDom = null, dom = toolbar._dom;
                if (typeof index == "number") {
                    var refItem = items[index];
                    refDom = refItem._dom;
                }
                items.insert(item, index);
                item.render(dom);
                toolbar.registerInnerControl(item);
            } else {
                items.insert(item, index);
            }

            return item;
        },
        createDom: function() {
            var bar = this, dom = document.createElement("div"), items = bar._items || [];
            dom.className = bar._className;
            for (var i = 0, j = items.size; i < j; i++) {
                var item = items.get(i);
                bar.registerInnerControl(item);
                item.render(dom);
                if (item instanceof dorado.widget.TextEditor) {
                    $fly(item._dom).addClass("i-text-box");
                }
            }
            return dom;
        }
    });

    /**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @component Advance
	 * @class 富文本编辑器。
	 * <p>
     *     用来编辑html的富文本编辑器。
	 * </p>
	 * @extends dorado.widget.Control
	 */
    dorado.widget.HtmlEditor = $extend(dorado.widget.AbstractDataEditor, /** @scope dorado.widget.HtmlEditor.prototype */{
        focusable: true,
        ATTRIBUTES: /** @scope dorado.widget.HtmlEditor.prototype */{
            className: {
                defaultValue: "d-html-editor"
            },

            /**
             * <p>富文本编辑器的模式，不同的模式会开启不同的插件，目前可选full,simple，区别在于工具栏上的功能的多少。</p>
             * <p>如果需要自定义，比如叫custom，可以为dorado.htmleditor添加一属性名为customMode，内容可参考fullMode和simpleMode。</p>
             * @attribute
             * @type String
             * @default "full"
             */
            mode: {
                //full,simple
                defaultValue: "full"
            },

            /**
             * 富文本编辑器的内容。
             * @attribute
             * @type String
             */
            content: {
                getter: function() {
                    var editor = this._editor;
                    if (editor) {
                        return editor.getContent();
                    }
                    return "";
                },
                setter: function(value) {
                    var editor = this._editor;
                    if (editor) {
                        return editor.setContent(value || "");
                    }
                }
            },

            readOnly: {
                setter: function(value) {
                    this._readOnly = value;
                    this.doOnReadOnlyChange(value);
                }
            },

            /**
             * 默认字号。
             * @attribute
             * @type String
             * @default "16px"
             */
            defaultFontSize: {
                defaultValue: "16px"
            },

            /**
             * 默认字体。
             * @attribute
             * @type String
             * @default "宋体"
             */
            defaultFontFamily: {
                defaultValue: "宋体"
            },

            /**
             * 文件上传路径。
             * @attribute
             * @type String
             * @default ">dorado/htmleditor/fileupload"
             */
            fileUploadPath: {
                defaultValue: ">dorado/htmleditor/fileupload"
            },

            /**
             * Flash上传路径。
             * @attribute
             * @type String
             * @default ">dorado/htmleditor/flashupload"
             */
            flashUploadPath: {
                defaultValue: ">dorado/htmleditor/flashupload"
            },

            /**
             * 图片上传路径。
             * @attribute
             * @type String
             * @default ">dorado/htmleditor/imageupload"
             */
            imageUploadPath: {
                defaultValue: ">dorado/htmleditor/imageupload"
            }
        },

        /**
         * 显示工具栏。
         */
        showToolBar: function() {
            var editor = this, doms = editor._doms;
            if (doms) {
                var toolbar = doms.toolbar;
                $fly(toolbar).css("display", "");
                editor.doOnResize();
            }
        },

        /**
         * 隐藏工具栏。
         */
        hideToolBar: function() {
            var editor = this, doms = editor._doms;
            if (doms) {
                var toolbar = doms.toolbar;
                $fly(toolbar).css("display", "none");
                editor.doOnResize();
            }
        },
        doOnBlur: function() {
            var editor = this;
            editor._lastPostValue = editor._value;
            editor._value = editor.get("content");
            editor._dirty = true;
            try {
                editor.post();
            } catch (e) {
                editor._value = editor._lastPostValue;
                editor._dirty = false;
                throw e;
            }
            editor.refresh();
            //editor.fireEvent("onValueChange", editor);
        },

        doOnReadOnlyChange: function(readOnly) {
            var htmleditor = this, editor = htmleditor._editor;
            if (readOnly === undefined) {
                readOnly = htmleditor._readOnly || htmleditor._readOnly2;
            }
            if (!editor || !editor.document) return;
            if (readOnly) {
                if (dorado.Browser.msie) {
                    editor.document.body.contentEditable = false;
                } else {
                    editor.document.body.contentEditable = false;
                }
            } else {
                if (dorado.Browser.msie) {
                    editor.document.body.contentEditable = true;
                } else {
                    editor.document.body.contentEditable = true;
                }
            }
            htmleditor.checkStatus();
        },

        post: function() {
            try {
                if(!this._dirty) {
                    return false;
                }
                var eventArg = {
                    processDefault : true
                };
                this.fireEvent("beforePost", this, eventArg);
                if(eventArg.processDefault === false)
                    return false;
                this.doPost();
                this._lastPostValue = this._value;
                this._dirty = false;
                this.fireEvent("onPost", this);
                return true;
            } catch (e) {
                dorado.Exception.processException(e);
            }
        },

        setFocus : function() {},

        doOnAttachToDocument: function() {
            var htmleditor = this;
            $invokeSuper.call(this, arguments);
            //editor的属性
            var option = {
                initialContent: htmleditor._value,//初始化编辑器的内容
                minFrameHeight: 100,
                defaultFontFamily: htmleditor._defaultFontFamily,
                defaultFontSize: htmleditor._defaultFontSize,
                iframeCssUrl: $url(">skin>/html-editor/iframe.css"),//给iframe样式的路径,
                selectedTdClass : 'selectTdClass',
                autoHeightEnabled: false,
                removeFormatTags : 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var',    //清除格式删除的标签
                removeFormatAttributes : 'class,style,lang,width,height,align,hspace,valign',        //清除格式删除的属性
                enterTag : 'p',//编辑器回车标签。p或br
                pasteplain : 0
            };
            var editor = new baidu.editor.Editor(option);
            this._editor = editor;
            editor.addListener('selectionchange', function () {
                htmleditor.checkStatus();
            });
            editor.addListener("ready", function() {
                htmleditor.checkStatus();
                htmleditor.doOnResize();
            });
            var popup = new dorado.widget.FloatContainer({
                exClassName: "popup",
                animateType: "none"
            });
            popup.focusable = false;

            htmleditor._popup = popup;
            jQuery.extend(popup, {
                _onEditButtonClick: function () {
                    this.hide();
                    htmleditor.executePlugin("Link");
                },
                _onImgEditButtonClick: function () {
                    this.hide();
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    //edui remove.
                    if (img && img.className.indexOf("edui-faked-video") != -1) {
                        htmleditor.executePlugin("Flash");
                    } else if (img && img.src.indexOf("http://api.map.baidu.com") != -1) {
                        htmleditor.executePlugin("Map");
                    } else if (img && img.src.indexOf("http://maps.google.com/maps/api/staticmap") != -1) {
                        htmleditor.executePlugin("GMap");
                    } else if (img && img.getAttribute("anchorname")) {
                        htmleditor.executePlugin("Anchor");
                    } else {
                        htmleditor.executePlugin("Image");
                    }
                },
                _onImgSetFloat: function(event, value) {
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    if (img) {
                        switch (value) {
                            case -2:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "";
                                }
                                break;
                            case -1:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "left";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "left";
                                }
                                break;
                            case 1:
                                if (!!window.ActiveXObject) {
                                    img.style.removeAttribute("display");
                                    img.style.styleFloat = "right";
                                } else {
                                    img.style.removeProperty("display");
                                    img.style.cssFloat = "right";
                                }
                                break;
                            case 2:
                                if (!!window.ActiveXObject) {
                                    img.style.styleFloat = "";
                                    img.style.display = "block";
                                } else {
                                    img.style.cssFloat = "";
                                    img.style.display = "block";
                                }

                        }
                        htmleditor.updatePopupPosition(img);
                    }
                },
                _onRemoveButtonClick: function () {
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    if (img && img.getAttribute("anchorname")) {
                        editor.execCommand("anchor");
                    } else {
                        editor.execCommand('unlink');
                    }
                    this.hide();
                }
            });
            var popupId = htmleditor._uniqueId + "_imageLinkPopup";
            window[popupId] = popup;
            editor.addListener('sourcemodechanged', function() {
                popup.hide();
            });
            editor.addListener('selectionchange', function (t, evt) {
                dorado.widget.setFocusedControl(htmleditor);

                var html = '', img = editor.selection.getRange().getClosedNode(),
                    imglink = baidu.editor.dom.domUtils.findParentByTagName(img, "a", true);

                if (imglink != null) {
                    html += '<nobr>属性: <span class="unclickable">默认</span>&nbsp;&nbsp;<span class="unclickable">左浮动</span>&nbsp;&nbsp;<span class="unclickable">右浮动</span>&nbsp;&nbsp;' +
                            '<span class="unclickable">独占一行</span>' +
                            ' <span onclick="$$._onImgEditButtonClick(event, this);" class="clickable">修改</span></nobr>';
                } else if (img != null && img.tagName.toLowerCase() == 'img') {
                    if (img.getAttribute('anchorname')) {
                        //锚点处理
                        html += '<nobr>属性: <span onclick=$$._onImgEditButtonClick(event) class="clickable">修改</span>&nbsp;&nbsp;<span onclick=$$._onRemoveButtonClick(event) class="clickable">删除</span></nobr>';
                    } else {
                        html += '<nobr>属性: <span onclick=$$._onImgSetFloat(event,-2) class="clickable">默认</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat(event,-1) class="clickable">左浮动</span>&nbsp;&nbsp;<span onclick=$$._onImgSetFloat(event,1) class="clickable">右浮动</span>&nbsp;&nbsp;' +
                                '<span onclick=$$._onImgSetFloat(event,2) class="clickable">独占一行</span>' +
                                ' <span onclick="$$._onImgEditButtonClick(event, this);" class="clickable">修改</span></nobr>';
                    }
                }
                var link;
                if (editor.selection.getRange().collapsed) {
                    link = editor.queryCommandValue("link");
                } else {
                    link = editor.selection.getStart();
                }
                link = baidu.editor.dom.domUtils.findParentByTagName(link, "a", true);
                var url;
                if (link != null && (url = link.getAttribute('href', 2)) != null) {
                    var txt = url;
                    if (url.length > 30) {
                        txt = url.substring(0, 20) + "...";
                    }
                    if (html) {
                        html += '<div style="height:5px;"></div>'
                    }
                    html += '<nobr>链接: <a target="_blank" href="' + url + '" title="' + url + '" >' + txt + '</a>' +
                            ' <span class="clickable" onclick="$$._onEditButtonClick(event, this);">修改</span>' +
                            ' <span class="clickable" onclick="$$._onRemoveButtonClick(event, this);"> 清除</span></nobr>';
                }
                if (html) {
                    popup.getDom().innerHTML = html.replace(/\$\$/g, popupId);

                    htmleditor.updatePopupPosition(img || link);
                } else {
                    popup.hide();
                }
            });

            editor.render(this._doms.editorWrap);

            $fly(editor.iframe.contentWindow.document).bind("scroll", function() {
                //TODO: performance
                if (popup._visible) {
                    htmleditor.updatePopupPosition();
                }
            });
        },
        updatePopupPosition: function(anchorTarget) {
            var htmleditor = this, editor = htmleditor._editor, iframe = editor.iframe;
            if (!anchorTarget) {
                anchorTarget = htmleditor._lastAnchorTarget;
            }
            htmleditor._lastAnchorTarget = anchorTarget;

            var editorPosition = $fly(iframe).offset(), editorWidth = iframe.offsetWidth, editorHeight = iframe.offsetHeight;
            var targetPosition = $fly(anchorTarget).position(), targetWidth = $fly(anchorTarget).width(),
                targetHeight = $fly(anchorTarget).height(), scrollTop = $fly(iframe.contentWindow.document.body).scrollTop();

            var editorRect = {
                left: editorPosition.left,
                top: editorPosition.top,
                right: editorPosition.left + editorWidth,
                bottom: editorPosition.top + editorHeight,
                width: editorWidth,
                height: editorHeight
            };

            var targetRect = {
                left: editorPosition.left + targetPosition.left,
                top: editorPosition.top + targetPosition.top - scrollTop,
                right: editorPosition.left + targetPosition.left + targetWidth,
                bottom: editorPosition.top + targetPosition.top - scrollTop + targetHeight,
                width: targetWidth,
                height: targetHeight
            };

            var targetVisible = true;

            if (targetRect.top > editorRect.bottom || targetRect.bottom < editorRect.top) {
                targetVisible = false;
            }

            if (targetVisible) {
                var position = {};

                if (targetRect.bottom + 5 > editorRect.bottom) {
                    position.left = targetRect.left;
                    position.top = editorRect.bottom - htmleditor._popup._dom.offsetHeight;
                } else {
                    position.left = targetRect.left;
                    position.top = targetRect.top + targetHeight;
                }

                htmleditor._popup.show({ position: position, autoAdjustPosition: false });
            } else {
                htmleditor._popup.hide();
            }
        },
        createDom: function() {
            var editor = this, doms = {}, dom = $DomUtils.xCreate({
                tagName: "div",
                className: editor._className,
                content: [
                    {
                        tagName: "div",
                        className: "toolbar-wrap",
                        contextKey: "toolbar"
                    },
                    {
                        tagName: "div",
                        className: "editor-wrap",
                        contextKey: "editorWrap"
                    }
                ]
            }, null, doms);

            editor._doms = doms;

            editor.initPlugins();

            return dom;
        },
        executePlugin: function(name) {
            var plugin = this._plugins[name];
            if (plugin) {
                plugin.execute();
            }
        },
        initPlugins: function() {
            var editor = this, mode = editor._mode || "default", toolbarConfig = dorado.htmleditor[mode + "Mode"] || [], toolbar = new dorado.htmleditor.ToolBar();
            editor._plugins = {};

            for (var j = 0, l = toolbarConfig.length; j < l; j++) {
                var pluginName = toolbarConfig[j];
                if (pluginName == "|") {
                    toolbar.addItem("-");
                } else {
                    var pluginConfig = plugins[pluginName];
                    pluginConfig.htmlEditor = editor;

                    if (pluginConfig.iconClass == undefined && pluginConfig.command) {
                        pluginConfig.iconClass = "html-editor-icon " + pluginConfig.command;
                    }

                    var plugin = new dorado.htmleditor.HtmlEditorPlugIn(pluginConfig);

                    if (pluginConfig.execute) {
                        plugin.execute = pluginConfig.execute;
                    }

                    if (pluginConfig.initToolBar) {
                        plugin.initToolBar = pluginConfig.initToolBar;
                    }

                    if (pluginConfig.checkStatus) {
                        plugin.checkStatus = pluginConfig.checkStatus;
                    }

                    if (pluginConfig.onStatusChange) {
                        plugin.onStatusChange = pluginConfig.onStatusChange;
                    }

                    plugin._name = pluginName;
                    plugin.initToolBar(toolbar);

                    editor._plugins[pluginName] = plugin;
                }
            }
            editor.registerInnerControl(toolbar);
            toolbar.render(editor._doms.toolbar);
        },
        doOnResize: function() {
            var htmleditor = this, dom = htmleditor._dom, doms = htmleditor._doms;
            if (dom) {
                var toolBarHeight = doms.toolbar.offsetHeight, height = dom.clientHeight;
                if (htmleditor._editor) {
                    htmleditor._editor.setHeight(height - toolBarHeight > 0 ? height - toolBarHeight : 0);
                }
            }
        },
        checkStatus: function() {
            var editor = this, plugins = editor._plugins;

            for (var name in plugins) {
                var plugin = plugins[name];
                if (plugin.checkStatus) {
                    try {
                        plugin.checkStatus();
                    } catch(e) {//fix a bug for ie.
                        if (console.log) console.log(e);
                    }
                }
            }
        },
        refreshDom: function() {
            $invokeSuper.call(this, arguments);
            var editor = this;
            if(editor._dataSet) {
                var value, dirty, readOnly = this._dataSet._readOnly;
                if(editor._property) {
                    var bindingInfo = editor._bindingInfo;
                    if(bindingInfo.entity instanceof dorado.Entity) {
                        value = bindingInfo.entity.get(editor._property);
                        dirty = bindingInfo.entity.isDirty(editor._property);
                    }
                    readOnly = readOnly || (bindingInfo.entity == null) || bindingInfo.propertyDef.get("readOnly");
                } else {
                    readOnly = true;
                }

                var oldReadOnly = editor._oldReadOnly;
                editor._oldReadOnly = !!readOnly;

                if (editor._editor && editor._value != value) {
                    editor._editor.setContent(value || "");
                }

                editor._value = value;
                editor._readOnly2 = readOnly;
                if (oldReadOnly === undefined || oldReadOnly !== readOnly) {
                    editor.doOnReadOnlyChange(!!readOnly);
                }
                editor.setDirty(dirty);
            }
        }
    });

    dorado.htmleditor.HtmlEditorPlugIn = $extend(dorado.AttributeSupport, {
        constructor: function(options) {
            if (options) this.set(options);
        },
        ATTRIBUTES: {
            name: {},
            label: {},
            icon: {},
            iconClass: {},
            command: {},
            parameter: {},
            htmlEditor: {},
            statusToggleable: {},
            status: {
                setter: function(value) {
                    this.onStatusChange && this.onStatusChange(value);
                    this._status = value;
                }
            }
        },
        onStatusChange: function(status) {
            var plugin = this, button = plugin.button;
            if (button) {
                button.set("disabled", status == "disable");
                if (status == "on") {
                    button.set("toggled", true);
                } else {
                    button.set("toggled", false);
                }
            }
        },
        execute: function() {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (plugin._command && htmlEditor) {
                htmlEditor._editor.execCommand(plugin._command, plugin._parameter);
            }
        },
        execCommand: function(cmd, value) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                htmlEditor._editor.execCommand(cmd, value);
            }
        },
        insertHtml: function(html) {
            this.execCommand("inserthtml", html);
        },
        queryCommandValue: function(cmd) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                return htmlEditor._editor.queryCommandValue(cmd);
            }
        },
        queryCommandState: function(cmd) {
            var plugin = this, htmlEditor = plugin._htmlEditor;
            if (htmlEditor && htmlEditor._editor) {
                return htmlEditor._editor.queryCommandState(cmd);
            }
        },
        initToolBar: function(toolbar) {
            var plugin = this, labels = dorado.htmleditor.defaultLabelMap;

            plugin.button = toolbar.addItem({
                $type: "SimpleIconButton",
                icon: plugin._icon,
                tip: labels[plugin._name.toLowerCase()],
                iconClass: plugin._iconClass,
                listener: {
                    onClick: function() {
                        plugin.onClick();
                    }
                }
            });
        },
        onClick: function() {
            var plugin = this;
            if (plugin._status != "disable") {
                plugin.execute.apply(this, arguments);
            }
        },
        checkStatus: function() {
            var plugin = this, heditor = plugin._htmlEditor, editor = plugin._htmlEditor._editor, result;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            } else if (!plugin._command) {
                plugin.set("status", "enable");
                return;
            }
            if (plugin._statusToggleable) {
                try {
                    result = editor.queryCommandState(plugin._command);
                    if (result === 1 || result === true) {
                        plugin.set("status", "on");
                    } else if (result === 0 || result === false) {
                        plugin.set("status", "enable");
                    } else if (result === -1) {
                        plugin.set("status", "disable");
                    }
                } catch(e) {
                }
            } else {
                try {
                    result = editor.queryCommandState(plugin._command);
                    if (result === -1) {
                        plugin.set("status", "disable");
                    } else {
                        plugin.set("status", "enable");
                    }
                } catch(e) {
                }
            }
        }
    });

    var pcheckStatus = function() {
        var plugin = this, heditor = plugin._htmlEditor, editor = plugin._htmlEditor._editor;
        try {
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            var status = editor.queryCommandState(plugin._command);
            if (status == -1) {
                plugin.set("status", "disable");

                return;
            }

            var value = editor.queryCommandValue(plugin._command);
            if (value === plugin._parameter) {
                plugin.set("status", "on");
            } else {
                plugin.set("status", "enable");
            }
        } catch(e) {
        }
    };

    var plugins = dorado.htmleditor.plugins = {
        Source: {
            command: "source"
        },
        DirectionalityLtr: {
            iconClass: "html-editor-icon directionalityltr",
            command: "directionality",
            parameter: "ltr",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        DirectionalityRtl: {
            iconClass: "html-editor-icon directionalityrtl",
            command: "directionality",
            parameter: "rtl",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyCenter: {
            iconClass: "html-editor-icon justifycenter",
            command: "justify",
            parameter: "center",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyLeft: {
            iconClass: "html-editor-icon justifyleft",
            command: "justify",
            parameter: "left",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyRight: {
            iconClass: "html-editor-icon justifyright",
            command: "justify",
            parameter: "right",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        JustifyJustify: {
            iconClass: "html-editor-icon justifyjustify",
            command: "justify",
            parameter: "justify",
            statusToggleable: true,
            checkStatus: pcheckStatus
        },
        FullScreen: {
            iconClass: "html-editor-icon fullscreen",
            statusToggleable: true,
            execute: function() {
                var editor = this._htmlEditor;
                if (!editor._maximized) {
                    editor._originalWidth = editor.getRealWidth();
                    editor._originalHeight = editor._height;
                    $fly(editor._dom).fullWindow({
                        modifySize: false,
                        callback: function(docSize) {
                            editor._maximized = true;
                            editor._dirty = true;
                            editor._value = editor._editor.getContent();
                            editor.post();
                            editor.set(docSize);
                            editor.resetDimension();
                            editor.refresh();
                        }
                    });
                } else {
                    editor._maximized = false;
                    $fly(editor._dom).unfullWindow({
                        callback: function() {
                            editor._maximized = false;
                            editor._width = editor._originalWidth;
                            editor._height = editor._originalHeight;
                            editor._dirty = true;
                            editor._value = editor._editor.getContent();
                            editor.post();
                            if (!editor._width) {
                                $fly(editor._dom).css("width", "");
                            }
                            editor.resetDimension();
                            editor.refresh();
                        }
                    });
                }
                this.checkStatus();
            },
            initToolBar: function(toolbar) {
                var plugin = this;
                plugin.button = toolbar.addItem({
                    $type: "SimpleIconButton",
                    exClassName: "fullscreen-button",
                    icon: plugin._icon,
                    iconClass: plugin._iconClass,
                    listener: {
                        onClick: function() {
                            plugin.onClick();
                        }
                    }
                });
            },
            checkStatus: function() {
                var editor = this._htmlEditor;
                if (editor._maximized) {
                    this.set("status", "on");
                } else {
                    this.set("status", "enable");
                }
            }
        }
    };

    var baseCmdMap = {
        Copy: true,
        Paste: true,
        Cut: true,
        Undo: true,
        Redo: true,
        Bold: true,
        Italic: true,
        Underline: true,
        StrikeThrough: true,
        Subscript: true,
        Superscript: true,
        BlockQuote: true,
        Indent: true,
        Outdent: true,
        InsertOrderedList: true,
        InsertUnorderedList: true,
        Unlink: true,
        SelectAll: false,
        RemoveFormat: false,
        Print: false,
        Preview: false,
        Date: false,
        Time: false,
        ClearDoc: false,
        Horizontal: false,
        DeleteTable: false,
        InsertParagraphBeforeTable: false,
        InsertRow: false,
        DeleteRow: false,
        InsertCol: false,
        DeleteCol: false,
        MergeCells: false,
        MergeRight: false,
        MergeDown: false,
        SplittoCells: false,
        SplittoRows: false,
        SplittoCols: false,
        FormatMatch: true,
        PastePlain: true
    };

    for (var prop in baseCmdMap) {
        var checkStatus = baseCmdMap[prop], object = {};
        object.command = prop.toLowerCase();
        if (checkStatus)
            object.statusToggleable = true;
        plugins[prop] = object;
    }

    plugins.Help = {
        iconClass: "html-editor-icon help",
        command: null,
        execute: function() {
            var plugin = this;
            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "关于",
                    width: 300,
                    height: 200,
                    center: true,
                    buttons: [{
                        caption: "确定",
                        listener: {
                            onClick: function() {
                                plugin.dialog.hide();
                            }
                        }
                    }],
                    children: [{
                        $type: "HtmlContainer",
                        content: "<div style='text-align:center;'>Dorado Html Editor</div>"
                    }]
                });
            }
            plugin.dialog.show();
        }
    };
})();

(function (){
    baidu.editor.plugins['flash'] = function (){
        var editor = this, lastFakedId = 0;

        function real(url, width, height, style) {
            return '<embed isfakedvideo' +
            ' type="application/x-shockwave-flash"' +
            ' pluginspage="http://www.macromedia.com/go/getflashplayer"' +
            ' src="' + url + '"' +
            ' width="' + width + '"' +
            ' height="' + height + '"' +
            ' style="' + (style || "") + '"' +
            ' wmode="transparent"' +
            ' play="true"' +
            ' loop="false"' +
            ' menu="false"' +
            ' allowscriptaccess="never"' +
            '/>';
        }

        function fake(url, width, height, style){
            var fakedId = 'edui_faked_video_' + (lastFakedId ++);

            return '<img isfakedvideo id="'+ fakedId +'" width="'+ width +'" height="' + height + '" _url="'+url+'" class="edui-faked-video"' +
                ' src="' + $DomUtils.BLANK_IMG + '"' +
                ' style="background:url(' + $url('>skin>/html-editor/fck_videologo.gif') + ') no-repeat center center; border:1px solid gray;'+ style +';" />';
        }

        editor.commands['insertflash'] = {
            execCommand: function (cmd, options){
                var url = options.url;
                var width = options.width || 320;
                var height = options.height || 240;
                var style = options.style ? options.style : "";
                editor.execCommand('inserthtml', fake(url, width, height, style));
            }
        };

        //获得style里的某个样式对应的值
        function getPars(str,par){
            var reg = new RegExp(par+":\\s*((\\w)*)","ig");
            var arr = reg.exec(str);
            return arr ? arr[1] : "";
        }

        editor.addListener('aftersetcontent', function (){
            var tempDiv = editor.document.createElement('div');
            var embedNodeList = editor.document.getElementsByTagName('embed');
            var embeds = [];
            var k = embedNodeList.length;
            while (k --) {
                embeds[k] = embedNodeList[k];
            }
            k = embeds.length;
            while (k --) {
                var url = embeds[k].src;
                var width = embeds[k].width || 320;
                var height = embeds[k].height || 240;
                var strcss = embeds[k].style.cssText;
                var style = getPars(strcss,"display") ? "display:" + getPars(strcss, "display") : "float:" + getPars(strcss,"float");
                tempDiv.innerHTML = fake(url, width, height,style);
                embeds[k].parentNode.replaceChild(tempDiv.firstChild, embeds[k]);
            }
        });

        var oldGetContent = editor.getContent;
        editor.getContent = function() {
            var content = oldGetContent.apply(this, []), imgReg = /<img.*?(edui_faked_video_\d+)['"\s].*?>/ig;
            return content.replace(imgReg, function(word) {
                var fakeId = RegExp.$1, img = editor.document.getElementById(fakeId);
                if (img) {
                    var width = img.width || 320, height = img.height || 240, strcss = img.style.cssText,
                        url = img.getAttribute("_url"), style = getPars(strcss,"display") ? "display:" + getPars(strcss, "display") : "float:" + getPars(strcss,"float");

                    return real(url, width, height, style);
                }
                return word;
            });
        }
    };
})();

(function () {
    var contextMenu = [
        {
            caption: '删除',
            name: 'delete'
        },
        {
            caption: '全选',
            name: 'selectall'
        },
        {
            caption: '删除代码',
            name: 'highlightcode'
        },
        {
            caption: '清空文档',
            name: 'cleardoc',
            exec: function() {
                if (confirm('确定清空文档吗？')) {
                    this.execCommand('cleardoc');
                }
            }
        },
        {
            caption: '取消链接',
            name: 'unlink'
        },
        {
            caption: '段落格式',
            icon: 'justifyjustify',
            name: "justify",
            submenu: [
                {
                    caption: '居左对齐',
                    name: 'justify',
                    value: 'left'
                },
                {
                    caption: '居右对齐',
                    name: 'justify',
                    value: 'right'
                },
                {
                    caption: '居中对齐',
                    name: 'justify',
                    value: 'center'
                },
                {
                    caption: '两端对齐',
                    name: 'justify',
                    value: 'justify'
                }
            ]
        },
        //TODO
        /**
        {
            caption: '表格属性',
            name: 'edittable',
            onClick: function() {
                this.tableDialog.open();
            }
        },
        **/
        {
            caption: '表格',
            icon: 'table',
            name: "edittable",
            submenu: [
                {
                    caption: '删除表格',
                    name: 'deletetable'
                },
                {
                    caption: '表格前插行',
                    name: 'insertparagraphbeforetable'
                },
                {
                    caption: '删除行',
                    name: 'deleterow'
                },
                {
                    caption: '删除列',
                    name: 'deletecol'
                },
                {
                    caption: '前插入行',
                    name: 'insertrow'
                },
                {
                    caption: '前插入列',
                    name: 'insertcol'
                },
                {
                    caption: '右合并单元格',
                    name: 'mergeright'
                },
                {
                    caption: '下合并单元格',
                    name: 'mergedown'
                },
                {
                    caption: '拆分成行',
                    name: 'splittorows'
                },
                {
                    caption: '拆分成列',
                    name: 'splittocols'
                },
                {
                    caption: '合并多个单元格',
                    name: 'mergecells'
                },
                {
                    caption: '完全拆分单元格',
                    name: 'splittocells'
                }
            ]
        }
    ];

    baidu.editor.plugins['contextmenu'] = function () {
        var me = this, menu, items = contextMenu;

        var getWindow = function (node) {
            var doc = node.ownerDocument || node;
            return doc.defaultView || doc.parentWindow;
        };

        me.addListener('contextmenu', function(type, evt) {
            var element = evt.target || evt.srcElement, iframe = getWindow(element).frameElement;
            var frameOffset = $fly(iframe).offset(), iframeBody = iframe.contentWindow.document.body;
            var offset = {
                left: evt.pageX + frameOffset.left - iframeBody.scrollLeft,
                top: evt.pageY + frameOffset.top - iframeBody.scrollTop
            };

            if (!menu) {
                for (var i = 0, ti, contextItems = []; ti = items[i]; i++) {
                    (function(item) {
                        if (item.submenu) {
                            for (var j = 0, submenuItem, submenu = []; submenuItem = item.submenu[j]; j++) {
                                (function(subItem) {
                                    submenu.push({
                                        caption: subItem.caption,
                                        name: subItem.name,
                                        iconClass: 'html-editor-icon ' + subItem.name + (subItem.value || ''),
                                        onClick : subItem.exec ? function() {
                                            subItem.exec.call(me)
                                        } : function() {
                                            me.execCommand(subItem.name, subItem.value)
                                        }
                                    });
                                })(submenuItem);
                            }
                            if (submenu.length) {
                                contextItems.push({
                                    caption : item.caption,
                                    name: item.name,
                                    iconClass: 'html-editor-icon ' + item.icon,
                                    submenu: {
                                        items: submenu
                                    }
                                })
                            }
                        } else {
                            contextItems.push({
                                caption: item.caption,
                                name: item.name,
                                iconClass: 'html-editor-icon ' + item.name + (item.value || ''),
                                onClick : item.exec ? function() {
                                    item.exec.call(me)
                                } : function() {
                                    me.execCommand(item.name, item.value)
                                }
                            });
                        }
                    })(ti);
                }

                menu = new dorado.widget.Menu({
                    items: contextItems,
                    showAnimateType: "none",
                    hideAnimateType: "none"
                });
            }

            function filterMenuItem(item) {
                if (item._name) {
                    var visible = me.queryCommandState(item._name) != -1;
                    item.set("visible", visible);
                    if (visible) {
                        if (item.hasSubmenu()) {
                            var subitems = item.get("items");
                            subitems.each(function(subitem) {
                                filterMenuItem(subitem);
                            });
                        }
                    }
                }
            }

            var menuItems = menu.get("items");

            menuItems.each(function(item) {
                filterMenuItem(item);
            });

            menu.show({ position: offset });

            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false)
        });
    };
})();
(function(plugins) {
    var htmleditor = dorado.htmleditor;

    plugins.Link = {
        iconClass: "html-editor-icon link",
        command: "link",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "_linkForm",
                urlObject = plugin.urlObject, filePath = editor._uniqueId + "_linkFilePath", tabControlId = editor._uniqueId + "_linkTabControl";

            if (!urlObject) {
                urlObject = plugin.urlObject = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入超链接",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "TabControl",
                        id: tabControlId,
                        height: 150,
                        tabs: [{
                            $type: "Control",
                            caption: "超链接",
                            control: {
                                id: formId,
                                $type: "AutoForm",
                                cols: "*",
                                entity: urlObject,
                                elements: [
                                    { property: "url", label: "超链接", type: "text" },
                                    { property: "title", label: "标题", type: "text" },
                                    { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                                ]
                            }
                        }, {
                            $type: "Control",
                            caption: "上传",
                            control: {
                                $type: "AutoForm",
                                cols: "*,100",
                                elements: [{
                                    property: "url", label: "文件",
                                    editor: new dorado.widget.TextEditor({
                                        id: filePath,
                                        required: true,
                                        readOnly: true
                                    })
                                },
                                {
                                    $type: "Button",
                                    caption: "浏览...",
                                    onReady: function(self, arg) {
                                        if (self._inited) {
                                            return;
                                        }
                                        self._inited = true;
                                        var view = this, pathEditor = this.id(filePath), callback = function(url) {
                                            urlObject.set("url", url);
                                            var autoform = view.id(formId);
                                            if (autoform) autoform.refreshData();
                                            var tabControl = view.id(tabControlId);
                                            tabControl.set("currentTab", 0);
                                        };
                                        initBrowserButton(self, editor._uniqueId + "link", $url(editor._fileUploadPath), pathEditor, callback);
                                    }
                                }]
                            }
                        }]
                    }],
                    buttons: [{
                        caption: "确定",
                        listener: {
                            onClick: function() {
                                if (urlObject) {
                                    var url = urlObject.get("url");
                                    plugin.execCommand("link", {
                                        href: url,
                                        title: urlObject.get("title"),
                                        target: urlObject.get("target") === true ? "_blank" : "_self"
                                    });
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    }, {
                        caption: "取消",
                        listener: {
                            onClick: function() {
                                plugin.dialog.hide();
                            }
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            var link = plugin.queryCommandValue("link") || {}, autoform = editor._view.id(formId);

            urlObject.set("url", link.href);
            urlObject.set("title", link.title || "");
            urlObject.set("target", link.target == "_blank");
            if (autoform)
                autoform.refreshData();

            plugin.dialog.show();
        }
    };

    plugins.InsertTable = {
        iconClass: "html-editor-icon table",
        command: "inserttable",
        execute: function() {
            var plugin = this, tableConfig = new dorado.Entity();

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入表格",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "AutoForm",
                        entity: tableConfig,
                        elements: [
                            { property: "row", label: "行数", type: "text"},
                            { property: "column", label: "列数", type: "text"},
                            { property: "width", label: "宽度", type: "text"},
                            { property: "height", label: "高度", type: "text"},
                            { property: "border", label: "边框", type: "text"},
                            { property: "cellborder", label: "单元格边框", type: "text" },
                            { property: "cellpadding", label: "单元格边距", type: "text"},
                            { property: "cellspacing", label: "单元格间距", type: "text"},
                            { property: "alignment", label: "对齐方式", type: "text",
                                editor: {
                                    $type: "TextEditor",
                                    trigger: "autoMappingDropDown1",
                                    mapping: [
                                        { key: "default", value: "无" },
                                        { key: "left", value: "左对齐" },
                                        { key: "center", value: "居中" },
                                        { key: "right", value: "右对齐" }
                                    ]
                                }
                            }
                        ]
                    }],
                    buttons: [{
                        caption: "确定",
                        listener: {
                            onClick: function() {
                                if (tableConfig) {
                                    var border = tableConfig.get("border");
                                    var cellpadding = tableConfig.get("cellpadding");
                                    var cellspacing = tableConfig.get("cellspacing");
                                    var width = tableConfig.get("width");
                                    var row = tableConfig.get("row") || 2, column = tableConfig.get("column") || 2;

                                    var alignment = tableConfig.alignment, cellborder = tableConfig.cellborder;

                                    plugin.execCommand("inserttable", {
                                        numRows: row,
                                        numCols: column,
                                        border: border,
                                        cellborder: cellborder,
                                        cellpadding: cellpadding,
                                        cellspacing: cellspacing,
                                        width: width,
                                        align: alignment
                                    });

                                    plugin.dialog.hide();
                                }
                            }
                        }
                    }, {
                        caption: "取消",
                        listener: {
                            onClick: function() {
                                plugin.dialog.hide();
                            }
                        }
                    }]
                });
            }
            plugin.dialog.show();
        }
    };

    window.uploadCallbackForHtmlEditorFn = null;

    window.uploadCallbackForHtmlEditor = function(path) {
        if (window.uploadCallbackForHtmlEditorFn) {
            if (path && /Exception:/.test(path)) {
                alert(path.replace("Exception:", ""));
            } else {
                window.uploadCallbackForHtmlEditorFn($url(path));
            }
            window.uploadCallbackForHtmlEditorFn = null;
        }
    };

    var alignImageMap = {
        left: "float: left;",
        right: "float: right;",
        block: "display: block;",
        "default": ""
    };

    function initBrowserButton(button, id, action, pathEditor, callback) {
        var dom = button._dom, parentNode = dom.parentNode;
        $fly(parentNode).addClass("browse-button-wrap");
        if (dorado.Browser.msie && parseInt(dorado.Browser.version, 10) < 9) {
            hiddenFile = document.createElement("<input type='file' name='filename' class='hidden-file'/>");
        } else {
            hiddenFile = document.createElement("input");
            hiddenFile.type = "file";
            hiddenFile.name = "filename";
            hiddenFile.className = "hidden-file";
        }

        var form, hiddenFile, iframe, iframeName = id + "UploadIframe";
        if (dorado.Browser.msie && parseInt(dorado.Browser.version, 10) < 9) {
            iframe = document.createElement("<iframe name='" + iframeName + "'></iframe>")
        } else {
            iframe = document.createElement("iframe");
            iframe.name = iframeName;
        }
        iframe.style.display = "none";

        if (dorado.Browser.msie && parseInt(dorado.Browser.version, 10) < 9) {
            form = document.createElement("<form name ='" + id + "UploadForm' action='" + action + "' enctype='multipart/form-data' method='post' target='" + iframeName + "'></form>");
        } else {
            form = document.createElement("form");
            form.action = action;
            form.method = "post";
            form.target = iframeName;
            form.enctype = "multipart/form-data";
        }

        hiddenFile.onchange = function() {
            pathEditor.set("text", this.value);
            form.submit();
            window.uploadCallbackForHtmlEditorFn = callback;
        };

        form.appendChild(hiddenFile);
        dom.appendChild(form);
        parentNode.appendChild(iframe);
    }

    plugins.Image = {
        iconClass: "html-editor-icon image",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, imageObject = plugin.imageObject,
                autoformId = editor._uniqueId + "_imageAutoform";

            if (!imageObject) {
                imageObject = plugin.imageObject = new dorado.Entity();
                imageObject.set("lockRatio", true);
            }

            var imgInfoId = editor._uniqueId + "_imageInfo", imgPreviewId = editor._uniqueId + "_imagePreview",
                imageAlignEditorId = editor._uniqueId + "_imageAlignEditor", imgWidthEditorId = editor._uniqueId + "_imageWidthEditor",
                imgHeightEditorId = editor._uniqueId + "_imageHeightEditor", imagePathEditorId = editor._uniqueId + "_imagePathEditor",
                imageUrlEditorId = editor._uniqueId + "_imageUrlEditor", tabControlId = editor._uniqueId + "_imageTabControl";

            var resizeImage = function(view) {
                var previewImg = plugin.previewImg;
                if (previewImg) {
                    var width = view.id(imgWidthEditorId).get("text"), height = view.id(imgHeightEditorId).get("text");
                    if (width)
                        previewImg.width = width;
                    else
                        previewImg.removeAttribute("width");

                    if (height)
                        previewImg.height = height;
                    else
                        previewImg.removeAttribute("height");
                }
            };

            var refreshImage = function(view, url, width, height) {
                var imgInfo = view.id(imgInfoId).getDom(), imgPreview = view.id(imgPreviewId), preImg = imgPreview.getContentContainer();
                plugin.imageRatio = null;

                if (!/\.(png|gif|jpg|jpeg|bmp)$/ig.test(url) && url.indexOf("api.map.baidu.com") == -1) {
                    preImg.innerHTML = "";
                    imgInfo.innerHTML = "";
                    return false;
                } else {
                    preImg.innerHTML = "";
                    var image = new Image();
                    plugin.previewImg = image;
                    image.onload = function() {
                        imgInfo.innerHTML = "原始宽：" + this.width + "px&nbsp;&nbsp;原始高：" + this.height + "px";
                        imgInfo.parentNode.parentNode.style.display = "";

                        this.sWidth = this.width;
                        this.sHeight = this.height;

                        plugin.imageRatio = this.width / this.height;

                        if (width == undefined && height == undefined) {
                            var widthEditor = view.id(imgWidthEditorId), heightEditor = view.id(imgHeightEditorId);
                            widthEditor.set("value", this.width);
                            heightEditor.set("value", this.height);
                            widthEditor.post(true);
                            heightEditor.post(true);
                        } else {
                            this.width = width;
                            this.height = height;
                        }

                        view.id(tabControlId).set("currentTab", 0);
                    };
                    image.onerror = function() {
                        preImg.innerHTML = "图片不存在";
                    };
                    preImg.appendChild(image);
                    image.src = url;
                }
            };

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入图像",
                    width: 520,
                    height: 395,
                    cols: "*",
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        id: tabControlId,
                        $type: "TabControl",
                        tabs: [{
                            $type: "Control",
                            caption: "图像",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    id: autoformId,
                                    entity: imageObject,
                                    cols: "100,*",
                                    elements: [{
                                        property: "url", label: "图片链接",
                                        layoutConstraint: { colSpan: 2 },
                                        editor: new dorado.widget.TextEditor({
                                            id: imageUrlEditorId,
                                            required: true,
                                            entity: imageObject,
                                            property: "url",
                                            listener: {
                                                onPost: function(self) {
                                                    var url = self.get("text"), view = this;
                                                    refreshImage(view, url);
                                                }
                                            }
                                        })
                                    },
                                    {
                                        property: "width", label: "宽度", type: "text", labelPosition: "top",
                                        editor: new dorado.widget.TextEditor({
                                            id: imgWidthEditorId,
                                            entity: imageObject,
                                            property: "width",
                                            onPost: function() {
                                                resizeImage(this);
                                            },
                                            onTextEdit: function(self) {
                                                var heightEditor = this.id(imgHeightEditorId), width = parseInt(self.get("text"), 10) || 0, height;
                                                if (imageObject.get("lockRatio") && plugin.imageRatio) {
                                                    if (width == 0) {
                                                        height = "";
                                                    } else {
                                                        height = parseInt(width / plugin.imageRatio, 10);
                                                    }
                                                    heightEditor.set("value", height);
                                                    heightEditor.post(true);
                                                }
                                                resizeImage(this);
                                            }
                                        })
                                    },
                                    {
                                        id: imgPreviewId,
                                        $type: "Container",
                                        layoutConstraint: { rowSpan: 5, vAlign: "top" },
                                        style: {
                                            border: "1px solid #ddd"
                                        },
                                        height: "100%"
                                    },
                                    {
                                        property: "height", label: "高度", type: "text", labelPosition: "top",
                                        editor: new dorado.widget.TextEditor({
                                            id: imgHeightEditorId,
                                            entity: imageObject,
                                            property: "height",
                                            onPost: function() {
                                                resizeImage(this);
                                            },
                                            onTextEdit: function(self) {
                                                var widthEditor = this.id(imgWidthEditorId), height = parseInt(self.get("text"), 10) || 0, width;
                                                if (imageObject.get("lockRatio") && plugin.imageRatio) {
                                                    if (height == 0) {
                                                        width = "";
                                                    } else {
                                                        width = parseInt(height * plugin.imageRatio, 10);
                                                    }
                                                    widthEditor.set("value", width);
                                                    widthEditor.post(true);
                                                }
                                                resizeImage(this);
                                            }
                                        })
                                    },
                                    {
                                        property: "lockRadio", type: "checkBox", labelPosition: "top", showLabel: false,
                                        editor: new dorado.widget.CheckBox({
                                            entity: imageObject,
                                            property: "lockRatio",
                                            caption: "锁定比例"
                                        })
                                    },
                                    { property: "title", label: "标题", type: "text", labelPosition: "top" },
                                    {
                                        property: "align", label: "对齐方式", labelPosition: "top",
                                        editor: new dorado.widget.TextEditor({
                                            id: imageAlignEditorId,
                                            entity: imageObject,
                                            property: "align",
                                            trigger: "autoMappingDropDown1",
                                            mapping: [
                                                { key: "default", value: "默认" },
                                                { key: "left", value: "左浮动" },
                                                { key: "right", value: "右浮动" },
                                                { key: "block", value: "独占一行" }
                                            ]
                                        })
                                    },
                                    {
                                        id: imgInfoId,
                                        $type: "HtmlContainer",
                                        style: {
                                            "text-align": "right"
                                        },
                                        layoutConstraint: { colSpan: 2 },
                                        content: "&nbsp;"
                                    }]
                                }],
                                buttons: [{
                                    caption: "确定",
                                    listener: {
                                        onClick: function() {
                                            var url = imageObject.get("url");
                                            if (url) {
                                                var width = imageObject.get("width"), height = imageObject.get("height"),
                                                    align = this.id(imageAlignEditorId).get("value"), title = imageObject.get("title");

                                                var imgstr = "<img ";
                                                var myimg = this.id(imgPreviewId).getDom().firstChild;
                                                imgstr += " src=" + url;

                                                if ( !width ) {
                                                    imgstr += " width=" + myimg.sWidth;
                                                }else if ( width && !/^[1-9]+[.]?\d*$/g.test( width ) ) {
                                                    alert("请输入正确的宽度");
                                                    return false;
                                                } else {
                                                    myimg && myimg.setAttribute( "width", width );
                                                    imgstr += " width=" + width;
                                                }
                                                if (!height) {
                                                    imgstr += " height=" + myimg.sHeight;
                                                } else if ( height && !/^[1-9]+[.]?\d*$/g.test( height ) ) {
                                                    alert("请输入正确的高度");
                                                    return false;
                                                } else {
                                                    myimg && myimg.setAttribute( "height", height );
                                                    imgstr += " height=" + height;
                                                }

                                                if (title) {
                                                    myimg && myimg.setAttribute( "title", title );
                                                    imgstr += " title=" + title;
                                                }

                                                if (align) {
                                                    var value = alignImageMap[align];
                                                    if (value) {
                                                        imgstr += " style='" + value + "'";
                                                    }
                                                }

                                                plugin.insertHtml(imgstr + " />");
                                                plugin.dialog.hide();
                                            }
                                        }
                                    }
                                }, {
                                    caption: "取消",
                                    listener: {
                                        onClick: function() {
                                            plugin.dialog.hide();
                                        }
                                    }
                                }
                            ]
                            }
                        }, {
                            $type: "Control",
                            caption: "上传",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    cols: "*,100",
                                    elements: [{
                                        property: "linkurl", label: "链接",
                                        layoutConstraint: { colSpan: 1 },
                                        editor: new dorado.widget.TextEditor({
                                            id: imagePathEditorId,
                                            required: true,
                                            entity: imageObject,
                                            property: "url",
                                            readOnly: true
                                        })
                                    },
                                    {
                                        $type: "Button",
                                        caption: "浏览...",
                                        onReady: function(self) {
                                            if (self._inited) {
                                                return;
                                            }
                                            self._inited = true;

                                            var view = this, pathEditor = this.id(imagePathEditorId), callback = function(url) {
                                                var urlEditor = view.id(imageUrlEditorId);
                                                urlEditor.set("value", url);
                                                urlEditor.post(true);
                                            };
                                            initBrowserButton(self, editor._uniqueId + "image", $url(editor._imageUploadPath), pathEditor, callback);
                                        }
                                    }]
                                }]
                            }
                        }]
                    }]
                });

                plugin._htmlEditor.registerInnerControl(plugin.dialog);
            }

            plugin.dialog.show();

            editor.get("view").id(imageAlignEditorId).set("readOnly", false);

            var img = editor._editor.selection.getRange().getClosedNode(), image = {};
            if ( img && /img/ig.test( img.tagName ) && img.className != "edui-faked-video" ) {
                image = img;
                //判断图片是否在连接内部
                var link = baidu.editor.dom.domUtils.findParentByTagName( img, "a", true );
                if ( link != null ) {
                    editor.get("view").id(imageAlignEditorId).set("readOnly", true);
                }

                //获得style里的某个样式对应的值
                function getPars(str, par) {
                    var reg = new RegExp(par + ":\\s*((\\w)*)", "ig");
                    var arr = reg.exec(str);
                    return arr ? arr[1] : "";
                }

                var style = image.style.cssText;
                var reg = "";
                if ( /float/ig.test( style ) ) {
                    reg = getPars( style, "float" );
                } else if ( /display/ig.test( style ) ) {
                    reg = getPars( style, "display" );
                }
                switch ( reg ) {
                    case "left":
                    case "right" :
                    case "block" :
                        imageObject.set("align", reg);
                        break;
                }

                imageObject.set("width", image.width);
                imageObject.set("height", image.height);
                imageObject.set("title", image.title);
                imageObject.set("url", image.src);

                try {
                    if (image.src) {
                        refreshImage(editor.get("view"), image.src, image.width, image.height);
                    }
                    editor.get("view").id(autoformId).refreshData();
                } catch(e) {
                    alert(e);
                }
            } else {
                imageObject.clearData();
                imageObject.set("lockRatio", true);
                refreshImage(editor.get("view"), "");
                editor.get("view").id(autoformId).refreshData();
            }
        }
    };

    plugins.SearchReplace = {
        iconClass: "html-editor-icon searchreplace",
        command: "searchreplace",
        execute: function() {
            var plugin = this, searchEntity = plugin.searchEntity, replaceEntity = plugin.replaceEntity;

            if (!searchEntity) {
                searchEntity = plugin.searchEntity = new dorado.Entity();
                replaceEntity = plugin.replaceEntity = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "查找/替换",
                    width: 380,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "TabControl",
                        height: 150,
                        tabMinWidth: 80,
                        tabs: [{
                            $type: "Control",
                            caption: "查找",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: searchEntity,
                                    cols: "*",
                                    elements: [
                                        { property: "text", label: "查找", type: "text"},
                                        { property: "matchCase", label: "区分大小写", type: "checkBox" }
                                    ]
                                }],
                                buttons: [{
                                    caption: "上一个",
                                    onClick: function() {
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchEntity.get("text"),
                                            casesensitive: searchEntity.get("matchCase"),
                                            dir: -1
                                        });
                                    }
                                }, {
                                    caption: "下一个",
                                    onClick: function() {
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchEntity.get("text"),
                                            casesensitive: searchEntity.get("matchCase"),
                                            dir: 1
                                        });
                                    }
                                }]
                            }
                        }, {
                            $type: "Control",
                            caption: "替换",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: replaceEntity,
                                    cols: "*",
                                    elements: [
                                        { property: "text", label: "查找", type: "text"},
                                        { property: "replaceText", label: "替换", type: "text"},
                                        { property: "matchCase", label: "区分大小写", type: "checkBox"}
                                    ]
                                }],
                                buttons: [{
                                    caption: "上一个",
                                    onClick: function() {
                                        plugin.execCommand("searchreplace", {
                                            searchStr: replaceEntity.get("text"),
                                            casesensitive: replaceEntity.get("matchCase"),
                                            dir: -1
                                        });
                                    }
                                }, {
                                    caption: "下一个",
                                    onClick: function() {
                                        plugin.execCommand("searchreplace", {
                                            searchStr: replaceEntity.get("text"),
                                            casesensitive: replaceEntity.get("matchCase"),
                                            dir: 1
                                        });
                                    }
                                }, {
                                    caption: "替换",
                                    onClick: function() {
                                        var searchStr = replaceEntity.get("text"), replaceStr = replaceEntity.get("replaceText");
                                        if (!searchStr || !replaceStr) {
                                            dorado.MessageBox.alert("Please input search Text");
                                            return;
                                        }
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchStr,
                                            replaceStr: replaceStr,
                                            all: false,
                                            casesensitive: replaceEntity.get("matchCase")
                                        });
                                    }
                                }, {
                                    caption: "全部替换",
                                    onClick: function() {
                                        var searchStr = replaceEntity.get("text"), replaceStr = replaceEntity.get("replaceText");
                                        if (!searchStr || !replaceStr) {
                                            dorado.MessageBox.alert("Please input search Text");
                                            return;
                                        }
                                        plugin.execCommand("searchreplace", {
                                            searchStr: searchStr,
                                            replaceStr: replaceStr,
                                            all: true,
                                            casesensitive: replaceEntity.get("matchCase")
                                        });
                                    }
                                }]
                            }
                        }]
                    }]
                });
            }
            plugin.dialog.show();
        }
    };

    plugins.Emoticon = {
        iconClass: "html-editor-icon emoticon",
        command: "inserthtml",
        execute: function() {
            var plugin = this, emoticonPicker = plugin.emoticonPicker;

            if (!emoticonPicker) {
                emoticonPicker = plugin.emoticonPicker = new dorado.widget.EmoticonPicker();
            }

            function select(self, arg) {
                plugin.insertHtml("<img src='" + arg.image + "'/>");
            }

            emoticonPicker.addListener("beforeShow", function() {
                emoticonPicker.addListener("onSelect", select);
            }, { once: true });

            emoticonPicker.addListener("onHide", function() {
                emoticonPicker.removeListener("onSelect", select);
            });

            plugin.emoticonPicker.show({
                anchorTarget: plugin.button,
                vAlign: "bottom"
            });
        }
    };

    plugins.Paragraph = {
        icon: null,
        command: "paragraph",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "格式",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var entity = new dorado.Entity();

            var formatEditor = new dorado.widget.TextEditor({
                width: 70,
                editable: false,
                trigger: "autoMappingDropDown1",
                mapping: [
                    { key: "p", value: "段落" },
                    { key: "h1", value: "标题 1" },
                    { key: "h2", value: "标题 2" },
                    { key: "h3", value: "标题 3" },
                    { key: "h4", value: "标题 4" },
                    { key: "h5", value: "标题 5" },
                    { key: "h6", value: "标题 6" }
                ],
                entity: entity,
                property: "format",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        var value = self.get("value");
                        plugin.execCommand('paragraph', self.get("value"));
                    }
                }
            });
            plugin.formatEditor = formatEditor;

            toolbar.addItem(formatEditor);
        },
        onStatusChange: function(status) {
            this.formatEditor.set("readOnly", status == "disable");
        },
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("paragraph");
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            plugin.formatEditor.set("value", value);
            var status = plugin.queryCommandState("paragraph");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    var fontMap = htmleditor.FONT_MAP, fontMapString = {};
    for (var font in fontMap) {
        fontMapString[font] = fontMap[font].join(",");
    }

    plugins.FontFamily = {
        command: "fontfamily",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "字体",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var dropdown = new dorado.widget.ListDropDown({
                items: ["宋体", "黑体", "隶书", "楷体", "Arial", "Impact", "Georgia", "Verdana", "Courier New", "Times New Roman"],
                listener: {
                    onOpen: function(self) {
                        setTimeout(function() {
                            var rowList = self._box.get("control");
                            rowList.addListener("onRenderRow", function(self, arg) {
                                arg.dom.style.fontFamily = arg.data;
                            });
                        }, 0);
                    }
                }
            });

            var entity = new dorado.Entity();

            var fontEditor = new dorado.widget.TextEditor({
                width: 100,
                trigger: dropdown,
                entity: entity,
                property: "fontname",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self, arg) {
                        var text = self.get("text"), result = text;
                        if (fontMapString[text]) {
                            result = fontMapString[text];
                        }
                        plugin.execCommand("fontfamily", result);
                    }
                }
            });
            plugin.fontEditor = fontEditor;
            toolbar.addItem(fontEditor);
        },
        onStatusChange: function(status) {
            this.fontEditor.set("readOnly", status == "disable");
        },
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("fontfamily");
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            plugin.fontEditor.set("text", value == "undefined" ? "" : value);
            var status = plugin.queryCommandState("fontfamily");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.RowSpacing = {
        command: "rowspacing",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "行距",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var rowspacing = htmleditor.defaultListMap.rowspacing, mappingArray = [];

            for (var i = 0, j = rowspacing.length; i < j; i++) {
                var temp = rowspacing[i].split(":");
                mappingArray.push({ key: temp[1], value: temp[0] });
            }

            var entity = new dorado.Entity();

            var rowSpacingEditor = new dorado.widget.TextEditor({
                width: 50,
                editable: false,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "rowspacing",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("rowspacing", self.get("value"));
                    }
                }
            });
            plugin.rowSpacingEditor = rowSpacingEditor;

            toolbar.addItem(rowSpacingEditor);
        },
        onStatusChange: function(status) {
            this.rowSpacingEditor.set("readOnly", status == "disable");
        },
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("rowspacing");
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            plugin.rowSpacingEditor.set("value", value);
            var status = plugin.queryCommandState("rowspacing");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.FontSize = {
        command: "fontsize",
        initToolBar: function(toolbar) {
            var plugin = this;

            toolbar.addItem({
                $type: "ToolBarLabel",
                text: "大小",
                style: {
                    "padding-left": 3,
                    "padding-right": 5
                }
            });

            var fontsizeArray = htmleditor.defaultListMap.fontsize, mappingArray = [];

            for (var i = 0, j = fontsizeArray.length; i < j; i++) {
                var temp = fontsizeArray[i];
                mappingArray.push({
                    key: "" + temp,
                    value: temp + "px"
                });
            }

            var entity = new dorado.Entity();

            var fontSizeEditor = new dorado.widget.TextEditor({
                width: 50,
                editable: false,
                trigger: "autoMappingDropDown1",
                mapping: mappingArray,
                entity: entity,
                property: "fontsize",
                supportsDirtyFlag: false,
                listener: {
                    onPost: function(self) {
                        plugin.execCommand("fontsize", self.get("value") + "px");
                    }
                }
            });
            plugin.fontSizeEditor = fontSizeEditor;

            toolbar.addItem(fontSizeEditor);
        },
        onStatusChange: function(status) {
            this.fontSizeEditor.set("readOnly", status == "disable");
        },
        statusToggleable: true,
        checkStatus: function() {
            var plugin = this, value = plugin.queryCommandValue("fontsize");
            var heditor = plugin._htmlEditor;
            if (heditor._readOnly || heditor._readOnly2) {
                plugin.set("status", "disable");
                return;
            }
            plugin.fontSizeEditor.set("text", value);

            var status = plugin.queryCommandState("fontsize");
            if (status == -1) {
                plugin.set("status", "disable");
            } else {
                plugin.set("status", "enable");
            }
        }
    };

    plugins.ForeColor = {
        iconClass: "html-editor-icon forecolor",
        command: "forecolor",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

            if (!colorPicker) {
                colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
            }

            function select(self, arg) {
                plugin.execCommand('forecolor', arg.color);
            }

            function clearColor(self) {
                select(self, { color: "#000" });
            }

            colorPicker.addListener("beforeShow", function() {
                colorPicker.addListener("onSelect", select);
                colorPicker.addListener("onClear", clearColor);
            }, { once: true });

            colorPicker.addListener("onHide", function() {
                colorPicker.removeListener("onSelect", select);
                colorPicker.removeListener("onClear", clearColor);
            });

            colorPicker.show({
                anchorTarget: plugin.button,
                vAlign: "bottom"
            });
        }
    };

    plugins.BackColor = {
        iconClass: "html-editor-icon backcolor",
        command: "backcolor",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, colorPicker = editor.colorPicker;

            if (!colorPicker) {
                colorPicker = editor.colorPicker = new dorado.widget.ColorPicker();
            }

            function select(self, arg) {
                plugin.execCommand('backcolor', arg.color);
            }

            function clearColor(self) {
                select(self, "#FFF");
            }

            colorPicker.addListener("beforeShow", function() {
                colorPicker.addListener("onSelect", select);
                colorPicker.addListener("onClear", clearColor);
            }, { once: true });

            colorPicker.addListener("onHide", function() {
                colorPicker.removeListener("onSelect", select);
                colorPicker.removeListener("onClear", clearColor);
            });

            editor.colorPicker.show({
                anchorTarget: plugin.button,
                vAlign: "bottom"
            });
        }
    };

    function A(sr){ return sr.split(","); }
    var character_common = [
        ["特殊符号", A("、,。,·,ˉ,ˇ,¨,〃,々,—,～,‖,…,‘,’,“,”,〔,〕,〈,〉,《,》,「,」,『,』,〖,〗,【,】,±,×,÷,∶,∧,∨,∑,∏,∪,∩,∈,∷,√,⊥,∥,∠,⌒,⊙,∫,∮,≡,≌,≈,∽,∝,≠,≮,≯,≤,≥,∞,∵,∴,♂,♀,°,′,″,℃,＄,¤,￠,￡,‰,§,№,☆,★,○,●,◎,◇,◆,□,■,△,▲,※,→,←,↑,↓,〓,〡,〢,〣,〤,〥,〦,〧,〨,〩,㊣,㎎,㎏,㎜,㎝,㎞,㎡,㏄,㏎,㏑,㏒,㏕,︰,￢,￤,,℡,ˊ,ˋ,˙,–,―,‥,‵,℅,℉,↖,↗,↘,↙,∕,∟,∣,≒,≦,≧,⊿,═,║,╒,╓,╔,╕,╖,╗,╘,╙,╚,╛,╜,╝,╞,╟,╠,╡,╢,╣,╤,╥,╦,╧,╨,╩,╪,╫,╬,╭,╮,╯,╰,╱,╲,╳,▁,▂,▃,▄,▅,▆,▇,�,█,▉,▊,▋,▌,▍,▎,▏,▓,▔,▕,▼,▽,◢,◣,◤,◥,☉,⊕,〒,〝,〞")],
        ["罗马数字", A("ⅰ,ⅱ,ⅲ,ⅳ,ⅴ,ⅵ,ⅶ,ⅷ,ⅸ,ⅹ,Ⅰ,Ⅱ,Ⅲ,Ⅳ,Ⅴ,Ⅵ,Ⅶ,Ⅷ,Ⅸ,Ⅹ,Ⅺ,Ⅻ")],
        ["数字符号", A("⒈,⒉,⒊,⒋,⒌,⒍,⒎,⒏,⒐,⒑,⒒,⒓,⒔,⒕,⒖,⒗,⒘,⒙,⒚,⒛,⑴,⑵,⑶,⑷,⑸,⑹,⑺,⑻,⑼,⑽,⑾,⑿,⒀,⒁,⒂,⒃,⒄,⒅,⒆,⒇,①,②,③,④,⑤,⑥,⑦,⑧,⑨,⑩,㈠,㈡,㈢,㈣,㈤,㈥,㈦,㈧,㈨,㈩")],
        ["日文符号", A("ぁ,あ,ぃ,い,ぅ,う,ぇ,え,ぉ,お,か,が,き,ぎ,く,ぐ,け,げ,こ,ご,さ,ざ,し,じ,す,ず,せ,ぜ,そ,ぞ,た,だ,ち,ぢ,っ,つ,づ,て,で,と,ど,な,に,ぬ,ね,の,は,ば,ぱ,ひ,び,ぴ,ふ,ぶ,ぷ,へ,べ,ぺ,ほ,ぼ,ぽ,ま,み,む,め,も,ゃ,や,ゅ,ゆ,ょ,よ,ら,り,る,れ,ろ,ゎ,わ,ゐ,ゑ,を,ん,ァ,ア,ィ,イ,ゥ,ウ,ェ,エ,ォ,オ,カ,ガ,キ,ギ,ク,グ,ケ,ゲ,コ,ゴ,サ,ザ,シ,ジ,ス,ズ,セ,ゼ,ソ,ゾ,タ,ダ,チ,ヂ,ッ,ツ,ヅ,テ,デ,ト,ド,ナ,ニ,ヌ,ネ,ノ,ハ,バ,パ,ヒ,ビ,ピ,フ,ブ,プ,ヘ,ベ,ペ,ホ,ボ,ポ,マ,ミ,ム,メ,モ,ャ,ヤ,ュ,ユ,ョ,ヨ,ラ,リ,ル,レ,ロ,ヮ,ワ,ヰ,ヱ,ヲ,ン,ヴ,ヵ,ヶ")],
        ["希腊字母", A("Α,Β,Γ,Δ,Ε,Ζ,Η,Θ,Ι,Κ,Λ,Μ,Ν,Ξ,Ο,Π,Ρ,Σ,Τ,Υ,Φ,Χ,Ψ,Ω,α,β,γ,δ,ε,ζ,η,θ,ι,κ,λ,μ,ν,ξ,ο,π,ρ,σ,τ,υ,φ,χ,ψ,ω")],
        ["俄文字母", A("А,Б,В,Г,Д,Е,Ё,Ж,З,И,Й,К,Л,М,Н,О,П,Р,С,Т,У,Ф,Х,Ц,Ч,Ш,Щ,Ъ,Ы,Ь,Э,Ю,Я,а,б,в,г,д,е,ё,ж,з,и,й,к,л,м,н,о,п,р,с,т,у,ф,х,ц,ч,ш,щ,ъ,ы,ь,э,ю,я")],
        ["拼音字母", A("ā,á,ǎ,à,ē,é,ě,è,ī,í,ǐ,ì,ō,ó,ǒ,ò,ū,ú,ǔ,ù,ǖ,ǘ,ǚ,ǜ,ü")],
        ["注音字符及其他", A("ㄅ,ㄆ,ㄇ,ㄈ,ㄉ,ㄊ,ㄋ,ㄌ,ㄍ,ㄎ,ㄏ,ㄐ,ㄑ,ㄒ,ㄓ,ㄔ,ㄕ,ㄖ,ㄗ,ㄘ,ㄙ,ㄚ,ㄛ,ㄜ,ㄝ,ㄞ,ㄟ,ㄠ,ㄡ,ㄢ,ㄣ,ㄤ,ㄥ,ㄦ,ㄧ,ㄨ")]
    ];

    plugins.Spechars = {
        iconClass: "html-editor-icon spechars",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                var tabs = [];

                for (var i = 0, k = character_common.length; i < k; i++) {
                    var tab = character_common[i], tabName = tab[0], chars = tab[1];
                    tabs.push({
                        $type: "Control",
                        caption: tabName,
                        control: {
                            $type: "GridPicker",
                            floating: false,
                            column: 15,
                            elements: chars,
                            listener: {
                                onSelect: function(self, arg) {
                                    plugin.execCommand("inserthtml", arg.element);
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    });
                }

                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 640,
                    height: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            $type: "TabControl",
                            tabs: tabs
                        }
                    ]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            plugin.dialog.show();
        }
    };

    plugins.Flash = {
        iconClass: "html-editor-icon video",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "_flashForm",
                flashObject = plugin.flashObject, filePath = editor._uniqueId + "_flashPath",
                flashAlignEditorId = editor._uniqueId + "_flashAlignEditor", tabControlId = editor._uniqueId + "_flashTabControl";

            if (!flashObject) {
                flashObject = plugin.flashObject = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入Flash",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "TabControl",
                        id: tabControlId,
                        height: 150,
                        tabs: [{
                            $type: "Control",
                            caption: "Flash",
                            control: {
                                id: formId,
                                $type: "AutoForm",
                                cols: "*",
                                entity: flashObject,
                                elements: [
                                    { property: "url", label: "地址", type: "text" },
                                    { property: "width", label: "宽度", type: "text" },
                                    { property: "height", label: "高度", type: "text" },
                                    { property: "align", label: "对齐方式", type: "text",
                                        editor: new dorado.widget.TextEditor({
                                            id: flashAlignEditorId,
                                            entity: flashObject,
                                            property: "align",
                                            trigger: "autoMappingDropDown1",
                                            mapping: [
                                                { key: "default", value: "默认" },
                                                { key: "left", value: "左浮动" },
                                                { key: "right", value: "右浮动" },
                                                { key: "block", value: "独占一行" }
                                            ]
                                        })
                                    }
                                ]
                            }
                        }, {
                            $type: "Control",
                            caption: "上传",
                            control: {
                                $type: "AutoForm",
                                cols: "*,100",
                                elements: [{
                                    property: "url", label: "链接",
                                    layoutConstraint: { colSpan: 1 },
                                    editor: new dorado.widget.TextEditor({
                                        id: filePath,
                                        required: true,
                                        readOnly: true
                                    })
                                },
                                {
                                    $type: "Button",
                                    caption: "浏览...",
                                    onReady: function(self, arg) {
                                        if (self._inited) {
                                            return;
                                        }
                                        self._inited = true;

                                        var view = this, pathEditor = this.id(filePath), callback = function(url) {
                                            flashObject.set("url", url);
                                            var autoform = view.id(formId), tabControl = view.id(tabControlId);
                                            if (autoform) autoform.refreshData();
                                            if (tabControl) tabControl.set("currentTab", 0);
                                        };
                                        initBrowserButton(self, editor._uniqueId + "flash", $url(editor._flashUploadPath), pathEditor, callback);
                                    }
                                }]
                            }
                        }]
                    }],
                    buttons: [{
                        caption: "确定",
                        listener: {
                            onClick: function() {
                                if (flashObject) {
                                    var url = flashObject.get("url");
                                    plugin.execCommand("insertflash", {
                                        url: url,
                                        width: flashObject.get("width"),
                                        height: flashObject.get("height"),
                                        style: alignImageMap[flashObject.get("align")]
                                    });
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    }, {
                        caption: "取消",
                        listener: {
                            onClick: function() {
                                plugin.dialog.hide();
                            }
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            var autoform = editor._view.id(formId);

            var img = editor._editor.selection.getRange().getClosedNode();
            if ( img && /img/ig.test( img.tagName ) && img.className == "edui-faked-video" ) {
                //获得style里的某个样式对应的值
                function getPars(str, par) {
                    var reg = new RegExp(par + ":\\s*((\\w)*)", "ig");
                    var arr = reg.exec(str);
                    return arr ? arr[1] : "";
                }

                var style = img.style.cssText;
                var reg = "";
                if ( /float/ig.test( style ) ) {
                    reg = getPars( style, "float" );
                } else if ( /display/ig.test( style ) ) {
                    reg = getPars( style, "display" );
                }
                switch ( reg ) {
                    case "left":
                    case "right" :
                    case "block" :
                        flashObject.set("align", reg);
                        break;
                }

                flashObject.set("width", img.width);
                flashObject.set("height", img.height);
                flashObject.set("url", img.getAttribute("_url"));
            } else {
                flashObject.clearData();
            }

            if (autoform)
                autoform.refreshData();

            plugin.dialog.show();
        }
    };

    plugins.Anchor = {
        iconClass: "html-editor-icon anchor",
        command: "anchor",
        execute: function() {
            var plugin = this, anchorObject = plugin.anchorObject, editor = plugin._htmlEditor, formId = editor._uniqueId + "anchorForm";

            if (!anchorObject) {
                anchorObject = plugin.anchorObject = new dorado.Entity();
            }

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "锚点",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            id: formId,
                            $type: "AutoForm",
                            cols: "*",
                            entity: anchorObject,
                            elements: [
                                { property: "name", label: "锚点名字", type: "text" }
                            ]
                        }
                    ],
                    buttons: [
                        {
                            caption: "确定",
                            listener: {
                                onClick: function() {
                                    if (anchorObject) {
                                        var name = anchorObject.get("name");
                                        if (name) {
                                            plugin.execCommand("anchor", name);
                                            plugin.dialog.hide();
                                        }
                                    }
                                }
                            }
                        },
                        {
                            caption: "取消",
                            listener: {
                                onClick: function() {
                                    plugin.dialog.hide();
                                }
                            }
                        }
                    ]
                });
                editor.registerInnerControl(plugin.dialog);
            }

            var anchor, img = editor._editor.selection.getRange().getClosedNode(), autoform = editor._view.id(formId);
            if(img && /img/ig.test(img.tagName.toLowerCase()) && img.getAttribute('anchorname')){
                anchor = img.getAttribute('anchorname');
            }

            anchorObject.set("name", anchor || "");
            autoform.refreshData();

            plugin.dialog.show();
        }
    };

    plugins.Map = {
        iconClass: "html-editor-icon map",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, iframeId = editor._uniqueId + "_map_iframe";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "Baidu Map",
                    width: 580,
                    height: 490,
                    resizeable: false,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            id: iframeId,
                            $type: "IFrame",
                            path: dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_map.html?" + (new Date()).getTime(),
                            onCreateDom: function(self) {
                                self._doms.iframe.editor = editor._editor;
                            }
                        }
                    ],
                    buttons: [{
                        caption: "OK",
                        onClick: function(self, arg) {
                            var iframe = this.id(iframeId), contentWindow = iframe.getIFrameWindow();
                            contentWindow.dialog_onok();

                            plugin.dialog.hide();
                        }
                    }, {
                        caption: "Cancel",
                        onClick: function(self, arg) {
                            plugin.dialog.hide();
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            } else {
                var iframe = editor.get("view").id(iframeId);
                iframe.set("path", dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_map.html?" + (new Date()).getTime());
            }

            plugin.dialog.show();
        }
    };

    plugins.GMap = {
        iconClass: "html-editor-icon gmap",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, iframeId = editor._uniqueId + "_gmap_iframe";

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "Google Map",
                    width: 580,
                    height: 490,
                    center: true,
                    modal: true,
                    resizeable: false,
                    modalType: "transparent",
                    children: [
                        {
                            id: iframeId,
                            $type: "IFrame",
                            path: dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_gmap.html?" + (new Date()).getTime(),
                            onCreateDom: function(self) {
                                self._doms.iframe.editor = editor._editor;
                            }
                        }
                    ],
                    buttons: [{
                        caption: "OK",
                        onClick: function(self, arg) {
                            var iframe = this.id(iframeId), contentWindow = iframe.getIFrameWindow();
                            contentWindow.dialog_onok();

                            plugin.dialog.hide();
                        }
                    }, {
                        caption: "Cancel",
                        onClick: function(self, arg) {
                            plugin.dialog.hide();
                        }
                    }]
                });
                editor.registerInnerControl(plugin.dialog);
            } else {
                var iframe = editor.get("view").id(iframeId);
                iframe.set("path", dorado.Setting["common.contextPath"] + "dorado/client/resources/htmleditor_gmap.html?" + (new Date()).getTime());
            }

            plugin.dialog.show();
        }
    };
})(dorado.htmleditor.plugins);
