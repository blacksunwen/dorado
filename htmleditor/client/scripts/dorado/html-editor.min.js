var baidu = baidu || {};

/**
 * @namespace baidu.editor
 */
baidu.editor = baidu.editor || {};

/**
 * @class baidu.editor.commands
 */
baidu.editor.commands = {};
/**
 * @class baidu.editor.plugins
 */
baidu.editor.plugins = {};




///import editor.js
/**
 * @class baidu.editor.browser     判断浏览器
 */

baidu.editor.browser = function(){
    var agent = navigator.userAgent.toLowerCase(),
        opera = window.opera,
        browser = {
        /**
         * 检测浏览器是否为IE
         * @name baidu.editor.browser.ie
         * @property    检测浏览器是否为IE
         * @grammar     baidu.editor.browser.ie
         * @return     {Boolean}    返回是否为ie浏览器
         */
        ie		: !!window.ActiveXObject,

        /**
         * 检测浏览器是否为Opera
         * @name baidu.editor.browser.opera
         * @property    检测浏览器是否为Opera
         * @grammar     baidu.editor.browser.opera
         * @return     {Boolean}    返回是否为opera浏览器
         */
        opera	: ( !!opera && opera.version ),

        /**
         * 检测浏览器是否为WebKit内核
         * @name baidu.editor.browser.webkit
         * @property    检测浏览器是否为WebKit内核
         * @grammar     baidu.editor.browser.webkit
         * @return     {Boolean}    返回是否为WebKit内核
         */
        webkit	: ( agent.indexOf( ' applewebkit/' ) > -1 ),

        /**
         * 检测是否为Adobe AIR
         * @name baidu.editor.browser.air
         * @property    检测是否为Adobe AIR
         * @grammar     baidu.editor.browser.air
         * @return     {Boolean}    返回是否为Adobe AIR
         */
        air		: ( agent.indexOf( ' adobeair/' ) > -1 ),

        /**
         * 检查是否为Macintosh系统
         * @name baidu.editor.browser.mac
         * @property    检查是否为Macintosh系统
         * @grammar     baidu.editor.browser.mac
         * @return     {Boolean}    返回是否为Macintosh系统
         */
        mac	: ( agent.indexOf( 'macintosh' ) > -1 ),

        /**
         * 检查浏览器是否为quirks模式
         * @name baidu.editor.browser.quirks
         * @property    检查浏览器是否为quirks模式
         * @grammar     baidu.editor.browser.quirks
         * @return     {Boolean}    返回是否为quirks模式
         */
        quirks : ( document.compatMode == 'BackCompat' )
    };

    /**
     * 检测浏览器是否为Gecko内核，如Firefox
     * @name baidu.editor.browser.gecko
     * @property    检测浏览器是否为Gecko内核
     * @grammar     baidu.editor.browser.gecko
     * @return     {Boolean}    返回是否为Gecko内核
     */
    browser.gecko = ( navigator.product == 'Gecko' && !browser.webkit && !browser.opera );

    var version = 0;

    // Internet Explorer 6.0+
    if ( browser.ie )
    {
        version = parseFloat( agent.match( /msie (\d+)/ )[1] );

        /**
         * 检测浏览器是否为 IE8 浏览器
         * @name baidu.editor.browser.IE8
         * @property    检测浏览器是否为 IE8 浏览器
         * @grammar     baidu.editor.browser.IE8
         * @return     {Boolean}    返回是否为 IE8 浏览器
         */
        browser.ie8 = !!document.documentMode;

        /**
         * 检测浏览器是否为 IE8 模式
         * @name baidu.editor.browser.ie8Compat
         * @property    检测浏览器是否为 IE8 模式
         * @grammar     baidu.editor.browser.ie8Compat
         * @return     {Boolean}    返回是否为 IE8 模式
         */
        browser.ie8Compat = document.documentMode == 8;

        /**
         * 检测浏览器是否运行在 兼容IE7模式
         * @name baidu.editor.browser.ie7Compat
         * @property    检测浏览器是否为兼容IE7模式
         * @grammar     baidu.editor.browser.ie7Compat
         * @return     {Boolean}    返回是否为兼容IE7模式
         */
        browser.ie7Compat = ( ( version == 7 && !document.documentMode )
                || document.documentMode == 7 );

        /**
         * 检测浏览器是否IE6模式或怪异模式
         * @name baidu.editor.browser.ie6Compat
         * @property    检测浏览器是否IE6 模式或怪异模式
         * @grammar     baidu.editor.browser.ie6Compat
         * @return     {Boolean}    返回是否为IE6 模式或怪异模式
         */
        browser.ie6Compat = ( version < 7 || browser.quirks );

    }

    // Gecko.
    if ( browser.gecko )
    {
        var geckoRelease = agent.match( /rv:([\d\.]+)/ );
        if ( geckoRelease )
        {
            geckoRelease = geckoRelease[1].split( '.' );
            version = geckoRelease[0] * 10000 + ( geckoRelease[1] || 0 ) * 100 + ( geckoRelease[2] || 0 ) * 1;
        }
    }
    /**
     * 检测浏览器是否为chrome
     * @name baidu.editor.browser.chrome
     * @property    检测浏览器是否为chrome
     * @grammar     baidu.editor.browser.chrome
     * @return     {Boolean}    返回是否为chrome浏览器
     */
    if (/chrome\/(\d+\.\d)/i.test(agent)) {
        browser.chrome = + RegExp['\x241'];
    }
    /**
     * 检测浏览器是否为safari
     * @name baidu.editor.browser.safari
     * @property    检测浏览器是否为safari
     * @grammar     baidu.editor.browser.safari
     * @return     {Boolean}    返回是否为safari浏览器
     */
    if(/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(agent) && !/chrome/i.test(agent)){
    	browser.safari = + (RegExp['\x241'] || RegExp['\x242']);
    }


    // Opera 9.50+
    if ( browser.opera )
        version = parseFloat( opera.version() );

    // WebKit 522+ (Safari 3+)
    if ( browser.webkit )
        version = parseFloat( agent.match( / applewebkit\/(\d+)/ )[1] );

    /**
     * 浏览器版本
     *
     * gecko内核浏览器的版本会转换成这样(如 1.9.0.2 -> 10900).
     *
     * webkit内核浏览器版本号使用其build号 (如 522).
     * @name baidu.editor.browser.version
     * @grammar     baidu.editor.browser.version
     * @return     {Boolean}    返回浏览器版本号
     * @example
     * if ( baidu.editor.browser.ie && <b>baidu.editor.browser.version</b> <= 6 )
     *     alert( "Ouch!" );
     */
    browser.version = version;

    /**
     * 是否是兼容模式的浏览器
     * @name baidu.editor.browser.isCompatible
     * @grammar     baidu.editor.browser.isCompatible
     * @return     {Boolean}    返回是否是兼容模式的浏览器
     * @example
     * if ( baidu.editor.browser.isCompatible )
     *     alert( "Your browser is pretty cool!" );
     */
    browser.isCompatible =
        !browser.mobile && (
        ( browser.ie && version >= 6 ) ||
        ( browser.gecko && version >= 10801 ) ||
        ( browser.opera && version >= 9.5 ) ||
        ( browser.air && version >= 1 ) ||
        ( browser.webkit && version >= 522 ) ||
        false );
    return browser;
}();

(function (){
    var noop = new Function();
    var utils = baidu.editor.utils = {
        /**
         * 以obj为原型创建实例
         * @param obj
         */
        makeInstance : function ( obj ) {
            noop.prototype = obj;
            obj = new noop;
            noop.prototype = null;
            return obj;
        },
        /**
         * 判断是否为数组
         * @param array
         */
        isArray : function (array){
            return array && array.constructor === Array;
        },
        isString : function(str){
            return typeof str == 'string' || str.constructor == String;
        },
        /**
         * 遍历元素执行迭代器
         * @param {Array|Object} eachable
         * @param {Function} iterator
         * @param {Object} this_
         */
        each: function (eachable, iterator, this_){
            if (utils.isArray(eachable)) {
                for (var i=0; i<eachable.length; i++) {
                    iterator.call(this_, eachable[i], i, eachable);
                }
            } else {
                for (var k in eachable) {
                    iterator.call(this_, eachable[k], k, eachable);
                }
            }
        },
        /**
         * 继承
         * @param {Object} subClass
         * @param {Object} superClass
         */
        inherits : function ( subClass, superClass ) {
            var oldP = subClass.prototype;
            var newP = utils.makeInstance( superClass.prototype );
            utils.extend( newP, oldP, true );
            subClass.prototype = newP;
            return ( newP.constructor = subClass );
        },

        /**
         * @param {Function} fn
         * @param {Object} this_
         */
        bind : function ( fn, this_ ) {
            return function () {
                return fn.apply( this_, arguments );
            };
        },

        /**
         * 创建延迟执行的函数
         * @param {Function} fn
         * @param {Number} delay
         * @param {Boolean?} exclusion 是否互斥执行
         */
        defer : function ( fn, delay, exclusion ){
            var timerID;
            return function () {
                if ( exclusion ) {
                    clearTimeout( timerID );
                }
                timerID = setTimeout( fn, delay );
            };
        },

        /**
         * 将s对象中的属性扩展到t对象上
         * @param {Object} t
         * @param {Object} s
         * @param {Boolean} b 是否保留已有属性
         * @returns {Object}
         */
        extend : function ( t, s, b ) {
            if (s) {
                for ( var k in s ) {
                    if (!b || !t.hasOwnProperty(k)) {
                        t[k] = s[k];
                    }
                }
            }
            return t;
        },

        /**
         * 查找元素在数组中的索引, 若找不到返回-1
         * @param {Array} array
         * @param {*} item
         * @param {Number?} at
         * @returns {Number}
         */
        indexOf : function ( array, item, at ) {
            at = at || 0;
            while ( at < array.length ) {
                if ( array[at] === item ) {
                    return at;
                }
                at ++;
            }
            return -1;
        },

        /**
         * 移除数组中的元素
         * @param {Array} array
         * @param {*} item
         */
        removeItem : function ( array, item ) {
            var k = array.length;
            if ( k ) while ( k -- ) {
                if ( array[k] === item ) {
                    array.splice(k, 1);
                    break;
                }
            }
        },

        /**
         * 删除字符串首尾空格
         * @param {String} str
         * @return {String} str
         */
        trim : function () {
            // "non-breaking spaces" 就是&nbsp;不能被捕获，所以不用\s
            var trimRegex = /(^[ \t\n\r]+)|([ \t\n\r]+$)/g;
            return function ( str ) {
                return str.replace( trimRegex, '' ) ;
            };
        }(),

        /**
         * 将字符串转换成hashmap, key由逗号分开
         * @param {String} list
         * @returns {Object}
         */
        listToMap : function ( list ) {
            if ( !list ) {
                return {};
            }
            var array = list.split( /,/g ),
                k = array.length,
                map = {};
            if ( k ) while ( k -- ) {
                map[array[k]] = 1;
            }
            return map;
        },

        /**
         * 将str中的html符号转义以用于放入html中
         * @param {String} str
         * @returns {String}
         */
        unhtml: function () {
            var map = { '<': '&lt;', '&': '&amp', '"': '&quot;', '>': '&gt;' };
            function rep( m ){ return map[m]; }
            return function ( str ) {
                return str ? str.replace( /[&<">]/g, rep ) : '';
            };
        }(),

        cssStyleToDomStyle : function(){
			var test = document.createElement( 'div' ).style,
				cssFloat =  test.cssFloat != undefined  ? 'cssFloat'
				: test.styleFloat != undefined ? 'styleFloat'
				: 'float',
                cache = { 'float': cssFloat };
            function replacer( match ){ return match.charAt( 1 ).toUpperCase(); }
			return function( cssName ) {
                return cache[cssName] || (cache[cssName] = cssName.toLowerCase().replace( /-./g, replacer ) );
			};
		}()
    };
})();

(function () {
    baidu.editor.EventBase = EventBase;

    var utils = baidu.editor.utils;

    /**
     * 事件基础类
     * @public
     * @class
     * @name baidu.editor.EventBase
     */
    function EventBase() {

    }

    EventBase.prototype = /**@lends baidu.editor.EventBase.prototype*/{
        /**
         * 注册事件监听器
         * @public
         * @function
         * @param {String} type 事件名
         * @param {Function} listener 监听器
         */
        addListener : function ( type, listener ) {
            getListener( this, type, true ).push( listener );
        },
        /**
         * 移除事件监听器
         * @public
         * @function
         * @param {String} type 事件名
         * @param {Function} listener 监听器
         */
        removeListener : function ( type, listener ) {
            var listeners = getListener( this, type );
            listeners && utils.removeItem( listeners, listener );
        },
        /**
         * 触发事件
         * @public
         * @function
         * @param {String} type 事件名
         */
        fireEvent : function ( type ) {
            var listeners = getListener( this, type ),
                r, t, k;
            if ( listeners ) {

                k = listeners.length;
                while ( k -- ) {

                    t = listeners[k].apply( this, arguments );
                    if ( t !== undefined ) {
                        r = t;
                    }

                }
                
            }
            if ( t = this['on' + type.toLowerCase()] ) {
                r = t.apply( this, arguments );
            }
            return r;
        }
    };

    function getListener( obj, type, force ) {
        var allListeners;
        type = type.toLowerCase();
        return ( ( allListeners = ( obj.__allListeners || force && ( obj.__allListeners = {} ) ) )
            && ( allListeners[type] || force && ( allListeners[type] = [] ) ) );
    }
})();

//注册命名空间
baidu.editor.dom = baidu.editor.dom || {};
/**
 * dtd html语义化的体现类
 * @constructor
 * @namespace dtd
 */
baidu.editor.dom.dtd = (function() {
    function _( s ) {
        for (var k in s) {
            s[k.toUpperCase()] = s[k];
        }
        return s;
    }
    function X( t ) {
        var a = arguments;
        for ( var i=1; i<a.length; i++ ) {
            var x = a[i];
            for ( var k in x ) {
                if (!t.hasOwnProperty(k)) {
                    t[k] = x[k];
                }
            }
        }
        return t;
    }
    var A = _({isindex:1,fieldset:1}),
        B = _({input:1,button:1,select:1,textarea:1,label:1}),
        C = X( _({a:1}), B ),
        D = X( {iframe:1}, C ),
        E = _({hr:1,ul:1,menu:1,div:1,blockquote:1,noscript:1,table:1,center:1,address:1,dir:1,pre:1,h5:1,dl:1,h4:1,noframes:1,h6:1,ol:1,h1:1,h3:1,h2:1}),
        F = _({ins:1,del:1,script:1,style:1}),
        G = X( _({b:1,acronym:1,bdo:1,'var':1,'#':1,abbr:1,code:1,br:1,i:1,cite:1,kbd:1,u:1,strike:1,s:1,tt:1,strong:1,q:1,samp:1,em:1,dfn:1,span:1}), F ),
        H = X( _({sub:1,img:1,embed:1,object:1,sup:1,basefont:1,map:1,applet:1,font:1,big:1,small:1}), G ),
        I = X( _({p:1}), H ),
        J = X( _({iframe:1}), H, B ),
        K = _({img:1,embed:1,noscript:1,br:1,kbd:1,center:1,button:1,basefont:1,h5:1,h4:1,samp:1,h6:1,ol:1,h1:1,h3:1,h2:1,form:1,font:1,'#':1,select:1,menu:1,ins:1,abbr:1,label:1,code:1,table:1,script:1,cite:1,input:1,iframe:1,strong:1,textarea:1,noframes:1,big:1,small:1,span:1,hr:1,sub:1,bdo:1,'var':1,div:1,object:1,sup:1,strike:1,dir:1,map:1,dl:1,applet:1,del:1,isindex:1,fieldset:1,ul:1,b:1,acronym:1,a:1,blockquote:1,i:1,u:1,s:1,tt:1,address:1,q:1,pre:1,p:1,em:1,dfn:1}),

        L = X( _({a:0}), J ),//a不能被切开，所以把他
        M = _({tr:1}),
        N = _({'#':1}),
        O = X( _({param:1}), K ),
        P = X( _({form:1}), A, D, E, I ),
        Q = _({li:1}),
        R = _({style:1,script:1}),
        S = _({base:1,link:1,meta:1,title:1}),
        T = X( S, R ),
        U = _({head:1,body:1}),
        V = _({html:1});

    var block = _({address:1,blockquote:1,center:1,dir:1,div:1,dl:1,fieldset:1,form:1,h1:1,h2:1,h3:1,h4:1,h5:1,h6:1,hr:1,isindex:1,menu:1,noframes:1,ol:1,p:1,pre:1,table:1,ul:1});

    return  _({

        // $ 表示自定的属性

        // body外的元素列表.
        $nonBodyContent: X( V, U, S ),

        //块结构元素列表
        $block : block,

        //内联元素列表
        $inline : L,

        $body : X( _({script:1,style:1}), block ),

        $cdata : _({script:1,style:1}),

        //自闭和元素
        $empty : _({area:1,base:1,br:1,col:1,hr:1,img:1,embed:1,input:1,link:1,meta:1,param:1}),

        //列表元素列表
        $listItem : _({dd:1,dt:1,li:1}),

        //列表根元素列表
        $list: _({ul:1,ol:1,dl:1}),

        //如果没有子节点就可以删除的元素列表，像span,a
        $removeEmpty : _({a:1,abbr:1,acronym:1,address:1,b:1,bdo:1,big:1,cite:1,code:1,del:1,dfn:1,em:1,font:1,i:1,ins:1,label:1,kbd:1,q:1,s:1,samp:1,small:1,span:1,strike:1,strong:1,sub:1,sup:1,tt:1,u:1,'var':1}),

        //在table元素里的元素列表
        $tableContent : _({caption:1,col:1,colgroup:1,tbody:1,td:1,tfoot:1,th:1,thead:1,tr:1,table:1}),

        html: U,
        head: T,
        style: N,
        script: N,
        body: P,
        base: {},
        link: {},
        meta: {},
        title: N,
        col : {},
        tr : _({td:1,th:1}),
        img : {},
        embed: {},
        colgroup : _({col:1}),
        noscript : P,
        td : P,
        br : {},
        th : P,
        center : P,
        kbd : L,
        button : X( I, E ),
        basefont : {},
        h5 : L,
        h4 : L,
        samp : L,
        h6 : L,
        ol : Q,
        h1 : L,
        h3 : L,
        option : N,
        h2 : L,
        form : X( A, D, E, I ),
        select : _({optgroup:1,option:1}),
        font : L,
        ins : L,
        menu : Q,
        abbr : L,
        label : L,
        table : _({thead:1,col:1,tbody:1,tr:1,colgroup:1,caption:1,tfoot:1}),
        code : L,
        tfoot : M,
        cite : L,
        li : P,
        input : {},
        iframe : P,
        strong : L,
        textarea : N,
        noframes : P,
        big : L,
        small : L,
        span :{'#':1},
        hr : L,
        dt : L,
        sub : L,
        optgroup : _({option:1}),
        param : {},
        bdo : L,
        'var' : L,
        div : P,
        object : O,
        sup : L,
        dd : P,
        strike : L,
        area : {},
        dir : Q,
        map : X( _({area:1,form:1,p:1}), A, F, E ),
        applet : O,
        dl : _({dt:1,dd:1}),
        del : L,
        isindex : {},
        fieldset : X( _({legend:1}), K ),
        thead : M,
        ul : Q,
        acronym : L,
        b : L,
        a : X( _({a:1}), J ),
        blockquote :X(_({td:1,tr:1,tbody:1,li:1}),P),
        caption : L,
        i : L,
        u : L,
        tbody : M,
        s : L,
        address : X( D, I ),
        tt : L,
        legend : L,
        q : L,
        pre : X( G, C ),
        p : X(_({'a':1}),L),
        em :L,
        dfn : L
    });
})();

(function() {
    var editor = baidu.editor,
        browser = editor.browser,
        dtd = editor.dom.dtd,
        utils = editor.utils,
        // for domUtils.remove
        orphanDiv;

    //for getNextDomNode getPreviousDomNode
    function getDomNode( node, start, ltr, startFromChild, fn, guard ) {
        var tmpNode = startFromChild && node[start],
            parent;

        !tmpNode && (tmpNode = node[ltr]);

        while ( !tmpNode && (parent = (parent || node).parentNode) ) {
            if ( parent.tagName == 'BODY' )
                return null;
            if ( guard && !guard( parent ) )
                return null;
            tmpNode = parent[ltr];
        }

        if ( tmpNode && fn && !fn( tmpNode ) ) {
            return  getDomNode( tmpNode, start, ltr, false, fn )
        }
        return tmpNode;
    }


    var domUtils = baidu.editor.dom.domUtils = {
        //节点常量
        NODE_ELEMENT : 1,
        NODE_DOCUMENT : 9,
        NODE_TEXT : 3,
        NODE_COMMENT : 8,
        NODE_DOCUMENT_FRAGMENT : 11,

        //位置关系
        POSITION_IDENTICAL : 0,
        POSITION_DISCONNECTED : 1,
        POSITION_FOLLOWING : 2,
        POSITION_PRECEDING : 4,
        POSITION_IS_CONTAINED : 8,
        POSITION_CONTAINS : 16,
        //ie6使用其他的会有一段空白出现
        fillChar : browser.ie && browser.version == '6' ? '\ufeff' : '\u200B',
        //-------------------------Node部分--------------------------------

        keys : {
            /*Backspace*/ 8:1, /*Delete*/ 46:1,
            /*Shift*/ 16:1, /*Ctrl*/ 17:1, /*Alt*/ 18:1,
            37:1, 38:1, 39:1, 40:1,
            13:1 /*enter*/
        },
        /**
         * 获取两个节点的位置关系
         * @param {Node} nodeA
         * @param {Node} nodeB
         * @returns {Number}
         */
        getPosition : function ( nodeA, nodeB ) {
            // 如果两个节点是同一个节点
            if ( nodeA === nodeB ) {
                // domUtils.POSITION_IDENTICAL
                return 0;
            }
            //chrome在nodeA,nodeB都不在树上时，会有问题
            if ( browser.gecko ) {
                return nodeA.compareDocumentPosition( nodeB );
            }

            var node,
                parentsA = [nodeA],
                parentsB = [nodeB];


            node = nodeA;
            while ( node = node.parentNode ) {
                // 如果nodeB是nodeA的祖先节点
                if ( node === nodeB ) {
                    // domUtils.POSITION_IS_CONTAINED + domUtils.POSITION_FOLLOWING
                    return 10;
                }
                parentsA.push( node );

            }


            node = nodeB;
            while ( node = node.parentNode ) {
                // 如果nodeA是nodeB的祖先节点
                if ( node === nodeA ) {
                    // domUtils.POSITION_CONTAINS + domUtils.POSITION_PRECEDING
                    return 20;
                }
                parentsB.push( node );

            }

            parentsA.reverse();
            parentsB.reverse();

            if ( parentsA[0] !== parentsB[0] )
            // domUtils.POSITION_DISCONNECTED
                return 1;

            var i = -1;
            while ( i++,parentsA[i] === parentsB[i] ) ;
            nodeA = parentsA[i];
            nodeB = parentsB[i];

            while ( nodeA = nodeA.nextSibling ) {
                if ( nodeA === nodeB ) {
                    // domUtils.POSITION_PRECEDING
                    return 4
                }
            }
            // domUtils.POSITION_FOLLOWING
            return  2;
        },

        /**
         * 返回节点索引，zero-based
         * @param {Node} node
         * @returns {Number}
         */
        getNodeIndex : function ( node ) {
            var childNodes = node.parentNode.childNodes,
                i = 0;
            while ( childNodes[i] !== node ) i++;
            return i;
        },

//        /**
//         * 判断节点是否在树上
//         * @param node
//         */
//        inDoc: function (node, doc){
//            while (node = node.parentNode) {
//                if (node === doc) {
//                    return true;
//                }
//            }
//            return false;
//        },

        /**
         * 查找祖先节点
         * @param {Function} tester
         * @param {Boolean} includeSelf 包含自己
         * @returns {Node}
         */
        findParent : function ( node, tester,includeSelf) {
            if(!this.isBody(node)){
                node =  includeSelf ? node : node.parentNode;
                while ( node ) {

                    if ( !tester || tester( node ) || this.isBody(node)) {

                        return tester && !tester(node) && this.isBody(node) ? null : node;
                    }
                    node = node.parentNode;

                }
            }

            return null;
        },

        findParentByTagName : function( node, tagName, includeSelf ) {
            if(node && node.nodeType && !this.isBody(node)  && (node.nodeType == 1 || node.nodeType) ){
                tagName = !utils.isArray( tagName ) ? [tagName] : tagName;
                node = node.nodeType == 3 || !includeSelf ? node.parentNode : node;
                while ( node && node.tagName && node.nodeType != 9 ) {
                   
                    if ( utils.indexOf( tagName, node.tagName.toLowerCase() ) > -1 )
                        return node;
                    node = node.parentNode;
                }
            }

            return null;
        },
        /**
         * 查找祖先节点集合
         * @param {Node} node
         * @param {Function} tester
         * @param {Boolean} includeSelf
         * @param {Boolean} closerFirst
         * @returns {Array}
         */
        findParents: function ( node, includeSelf, tester, closerFirst ) {
            var parents = includeSelf && ( tester && tester(node) || !tester ) ? [node] : [];
            while ( node = domUtils.findParent( node, tester ) ) {
                parents.push( node );
            }
            if ( !closerFirst ) {
                parents.reverse();
            }
            return parents;
        },

        /**
         * 往后插入节点
         * @param node
         * @param nodeToInsert
         */
        insertAfter : function ( node, nodeToInsert ) {
            return node.parentNode.insertBefore( nodeToInsert, node.nextSibling );
        },

        /**
         * 删除该节点
         * @param {Node} node
         * @param {Boolean} keepChildren 是否包含子节点
         * @return {Node} node
         */
        remove :  function ( node, keepChildren ) {
            var parent = node.parentNode,
                child;
            if ( parent ) {
                if ( keepChildren && node.hasChildNodes() ) {
                    while ( child = node.firstChild ) {
                        parent.insertBefore( child, node );
                    }
                }
//                if ( browser.ie ) {
//                    if ( orphanDiv == null ) {
//                        orphanDiv = node.ownerDocument.createElement( 'div' );
//                    }
//                    orphanDiv.appendChild( node );
//                    orphanDiv.innerHTML = '';
//                } else {
//                    parent.removeChild( node );
//                }
                parent.removeChild( node );
            }
            return node;
        },

        /**
         * 取得node节点在dom树上的下一个节点
         * @param {Node} node
         * @param {Boolean} startFromChild 为true从子节点开始找
         * @param {Function} fn 找到fn为真的节点
         * @return {Node} node
         */
        getNextDomNode : function( node, startFromChild, filter, guard ) {
            return getDomNode( node, 'firstChild', 'nextSibling', startFromChild, filter, guard );

        },

        /**
         * 取得node节点在dom树上的上一个节点
         * @param {Node} node
         * @param {Boolean} startFromChild 为true从子节点开始找
         * @param {Function} fn 找到fn为真的节点
         * @return {Node} node
         */
        getPreviousDomNode : function( node, startFromChild, fn ) {
            return getDomNode( node, 'lastChild', 'previousSibling', startFromChild, fn );

        },
        /**
         * 是bookmark节点
         * @param {Node} node
         * @return {Boolean} true
         */
        isBookmarkNode : function( node ) {
            return node.nodeType == 1 && node.id && /^_baidu_bookmark_/i.test(node.id);
        },
        /**
         * 获取节点所在window对象
         * @param {Node} node
         */
        getWindow : function ( node ) {
            var doc = node.ownerDocument || node;
            return doc.defaultView || doc.parentWindow;
        },
        /**
         * 得到公共的祖先节点
         * @return {Node} 祖先节点
         */
        getCommonAncestor : function( nodeA, nodeB ) {
            if ( nodeA === nodeB )
                return nodeA;
            var parentsA = [nodeA] ,parentsB = [nodeB], parent = nodeA,
                i = -1;


            while ( parent = parent.parentNode ) {

                if ( parent === nodeB )
                    return parent;
                parentsA.push( parent )
            }
            parent = nodeB;
            while ( parent = parent.parentNode ) {
                if ( parent === nodeA )
                    return parent;
                parentsB.push( parent )
            }

            parentsA.reverse();
            parentsB.reverse();
            while ( i++,parentsA[i] === parentsB[i] );
            return i == 0 ? null : parentsA[i - 1];

        },
        /**
         * 清除该节点左右空的inline节点
         * @exmaple <b></b><i></i>xxxx<b>bb</b> --> xxxx<b>bb</b>
         */
        clearEmptySibling : function( node, ingoreNext, ingorePre ) {
            function clear( next, dir ) {
                var tmpNode;
                if ( next && (!domUtils.isBookmarkNode( next ) && domUtils.isEmptyInlineElement( next ) || domUtils.isWhitespace(next) )) {
                    tmpNode = next[dir];
                    domUtils.remove( next );
                    tmpNode && clear( tmpNode, dir );
                }
            }

            !ingoreNext && clear( node.nextSibling, 'nextSibling' );
            !ingorePre && clear( node.previousSibling, 'previousSibling' );
        },

        //---------------------------Text----------------------------------

        /**
         * 将一个文本节点拆分成两个文本节点
         * @param {TextNode} node
         * @param {Integer} offset 拆分的
         * @return {TextNode} 拆分后的后一个文本节
         */
        split: function ( node, offset ) {
            var doc = node.ownerDocument;
            if ( browser.ie && offset == node.nodeValue.length ) {
                var next = doc.createTextNode( '' );
                return domUtils.insertAfter( node, next );
            }

            var retval = node.splitText( offset );


            //ie8下splitText不会跟新childNodes,我们手动触发他的更新

            if ( browser.ie8 ) {
                var tmpNode = doc.createTextNode( '' );
                domUtils.insertAfter( retval, tmpNode );
                domUtils.remove( tmpNode );

            }

            return retval;
        },

        /**
         * 是空白节点否
         * @param {TextNode}
            * @return {Boolean}
         */
        isWhitespace : function( node ) {
            var reg = new RegExp('[^ \t\n\r'+domUtils.fillChar+']');
            return !reg.test( node.nodeValue );
        },

        //------------------------------Element-------------------------------------------
        /**
         * 获取元素相对于viewport的像素坐标
         * @param {Element} element
         * @returns {Object}
         */
        getXY : function ( element ) {
            var box = element.getBoundingClientRect();
            return {
                x: Math.round( box.left ),
                y: Math.round( box.top )
            };
        },
        /**
         * 绑原生DOM事件
         * @param {Element|Window|Document} target
         * @param {Array|String} type
         * @param {Function} handler
         */
        on : function ( obj, type, handler ) {
            var types = type instanceof Array ? type : [type],
                k = types.length;
            if ( k ) while ( k -- ) {
                type = types[k];
                if ( obj.addEventListener ) {
                    obj.addEventListener( type, handler, false );
                } else {
                    //绑定obj 为this
                    obj.attachEvent( 'on' + type, function(evt){ handler.call(obj,evt)} );
                }
            }
        },

        /**
         * 解除原生DOM事件绑定
         * @param {Element|Window|Document} obj
         * @param {Array|String} type
         * @param {Function} handler
         */
        un : function ( obj, type, handler ) {
            var types = type instanceof Array ? type : [type],
                k = types.length;
            if ( k ) while ( k -- ) {
                type = types[k];
                if ( obj.removeEventListener ) {
                    obj.removeEventListener( type, handler, false );
                } else {
                    obj.detachEvent( 'on' + type, handler );
                }
            }
        },

        /**
         * 比较两个节点是否tagName相同且有相同的属性和属性值
         * @param {Element}   nodeA
         * @param {Element}   nodeB
         * @return {Boolean} true 相同
         * @example
         * &lt;span  style="font-size:12px"&gt;ssss&lt;/span&gt;和&lt;span style="font-size:12px"&gt;bbbbb&lt;/span&gt; 相等
         *  &lt;span  style="font-size:13px"&gt;ssss&lt;/span&gt;和&lt;span style="font-size:12px"&gt;bbbbb&lt;/span&gt; 不相等
         */
        isSameElement : function( nodeA, nodeB ) {

            if ( nodeA.tagName != nodeB.tagName )
                return false;

            var thisAttribs = nodeA.attributes,
                otherAttribs = nodeB.attributes;


            if ( !browser.ie && thisAttribs.length != otherAttribs.length )
                return false;

            var k = thisAttribs.length,
                specLen = 0;
            if ( k ) while ( k -- ) {
                var thisAttr = thisAttribs[k];
                if ( !browser.ie || thisAttr.specified ) {
                    specLen ++;
                    if ( thisAttr.nodeName == 'style' ) continue;
                    // ie6 下必须用getAttribute("className")才能取到class属性
//                    if ( nodeB.getAttribute( thisAttr.nodeName ) != thisAttr.nodeValue ) {
                    var attr = nodeB.attributes[thisAttr.nodeName];
                    var attrValue = attr && attr.nodeValue || null;
                    if (attrValue != thisAttr.nodeValue ) {
                        return false;
                    }
                }
            }

            if ( !domUtils.isSameStyle( nodeA, nodeB ) ) {
                return false;
            }

            // 如果是IE，不能通过attributes.length判断属性是否一样多，需要单独判断
            if ( browser.ie ) {
                k = otherAttribs.length;
                if ( k ) while ( k -- ) {
                    if ( otherAttribs[k].specified ) {
                        specLen --;
                    }
                }
                return !specLen;
            }

            return true;
        },
        isRedundantSpan : function( node ) {
            if ( node.nodeType == 3 || node.tagName.toLowerCase() != 'span' )
                return 0;
            if ( browser.ie ) {
                //ie 下判断实效，所以只能简单用style来判断
                return node.style.cssText == '' ? 1 : 0;
//                var attrs = node.attributes;
//                if ( attrs.length ) {
//                    for ( var i = 0,l = attrs.length; i<l; i++ ) {
//                        if ( attrs[i].specified ) {
//                            return 0;
//                        }
//                    }
//                    return 1;
//                }
            }
            return !node.attributes.length
        },
        /**
         * 判断两个元素的style是不是style属性一致
         * @param elementA
         * @param elementB
         */
        isSameStyle : function ( elementA, elementB ) {
            var styleA = elementA.style.cssText,
                styleB = elementB.style.cssText;
//            if ( browser.ie && browser.version <= 8 ) {
//                styleA = styleA.toLowerCase();
//                styleB = styleB.toLowerCase();
//            }
            if ( !styleA && !styleB ) {
                return true;
            } else if ( !styleA || !styleB ) {
                return false;
            }
            var styleNameMap = {},
                record = [],
                exit = {};
            styleA.replace( /[\w-]+\s*(?=:)/g, function ( name ) {
                styleNameMap[name] = record.push( name );
            } );
            try {
                styleB.replace( /[\w-]+\s*(?=:)/g, function ( name ) {
                    var index = styleNameMap[name];
                    if ( index ) {
//                        var valA, valB;
                        name = utils.cssStyleToDomStyle( name );
//                        if ( browser.ie ) {
//                            valA = elementA.style.getAttribute( name );
//                            valB = elementB.style.getAttribute( name );
//                        } else {
//                            valA = elementA.style[name];
//                            valB = elementB.style[name];
//                        }
                        if ( elementA.style[name] !== elementB.style[name] ) {
                            throw exit;
                        }
                        record[index - 1] = '';
                    } else {
                        throw exit;
                    }
                } );
            } catch( ex ) {
                if ( ex === exit ) {
                    return false;
                }
            }
            return !record.join( '' );
        },

        /**
         * 检查是否为块元素
         * @param {Element} node
         * @param {String} customNodeNames 自定义的块元素的tagName
         * @return {Boolean} true 是块元素
         */
        isBlockElm : function () {
            var blockBoundaryDisplayMatch = ['block' ,'list-item' ,'table' ,'table-row-group' ,'table-header-group','table-footer-group' ,'table-row' ,'table-column-group' ,'table-column' ,'table-cell' ,'table-caption'],
                blockBoundaryNodeNameMatch = { hr : 1 };
            return function( node, customNodeNames ) {
                return node.nodeType == 1 && (utils.indexOf( blockBoundaryDisplayMatch, domUtils.getComputedStyle( node, 'display' ) ) != -1 ||
                    utils.extend( blockBoundaryNodeNameMatch, customNodeNames || {} )[ node.tagName.toLocaleLowerCase() ]);
            }
        }(),

        /**
         * 是body
         * @param {Node}
            * @param {Boolean}
            */
        isBody : function( node ) {
            return  node && node.nodeType == 1 && node.tagName.toLowerCase() == 'body';
        },
        /**
         * 以node节点为中心，将该节点的父节点拆分成2块
         * @param {Element} node
         * @param {Element} parent 要被拆分的父节点
         * @example <div>xxxx<b>xxx</b>xxx</div> ==> <div>xxx</div><b>xx</b><div>xxx</div>
         */
        breakParent : function( node, parent ) {
            var tmpNode, parentClone = node, clone = node, leftNodes, rightNodes;
            do {
                parentClone = parentClone.parentNode;

                if ( leftNodes ) {
                    tmpNode = parentClone.cloneNode( false );
                    tmpNode.appendChild( leftNodes );
                    leftNodes = tmpNode;

                    tmpNode = parentClone.cloneNode( false );
                    tmpNode.appendChild( rightNodes );
                    rightNodes = tmpNode;

                } else {
                    leftNodes = parentClone.cloneNode( false );
                    rightNodes = leftNodes.cloneNode( false );
                }


                while ( tmpNode = clone.previousSibling ) {
                    leftNodes.insertBefore( tmpNode, leftNodes.firstChild );
                }

                while ( tmpNode = clone.nextSibling ) {
                    rightNodes.appendChild( tmpNode );
                }

                clone = parentClone;
            } while ( parent !== parentClone );

            tmpNode = parent.parentNode;
            tmpNode.insertBefore( leftNodes, parent );
            tmpNode.insertBefore( rightNodes, parent );
            tmpNode.insertBefore( node, rightNodes );
            domUtils.remove( parent );
            return node;
        },

        /**
         * 检查是否是inline的套用的空节点
         * @return {Boolean} 1/0
         * @example
         * &lt;b&gt;&lt;i&gt;&lt;/i&gt;&lt;/b&gt; //true
         * <b><i></i><u></u></b> true
         * &lt;b&gt;&lt;/b&gt; true  &lt;b&gt;xx&lt;i&gt;&lt;/i&gt;&lt;/b&gt; //false
         */
        isEmptyInlineElement : function( node ) {

            if ( node.nodeType != 1 || !dtd.$removeEmpty[ node.tagName ] )
                return 0;

            node = node.firstChild;
            while ( node ) {
                //如果是创建的bookmark就跳过
                if ( domUtils.isBookmarkNode( node ) )
                    return 0;
                if ( node.nodeType == 1 && !domUtils.isEmptyInlineElement( node ) ||
                    node.nodeType == 3 && !domUtils.isWhitespace( node )
                    ) {
                    return 0;
                }
                node = node.nextSibling;
            }
            return 1;

        },

        /**
         * 删除节点下的左右的空白节点
         * @param {Element} node
         */
        trimWhiteTextNode : function( node ) {

            function remove( dir ) {
                var child;
                while ( (child = node[dir]) && child.nodeType == 3 && domUtils.isWhitespace( child ) )
                    node.removeChild( child )

            }

            remove( 'firstChild' );
            remove( 'lastChild' );

        },

        /**
         * 合并子节点
         * @example &lt;span style="font-size:12px;"&gt;xx&lt;span style="font-size:12px;"&gt;aa&lt;/span&gt;xx&lt;/span  使用后
         * &lt;span style="font-size:12px;"&gt;xxaaxx&lt;/span
         */
        mergChild : function( node,tagName,attrs ) {
            
            var list = domUtils.getElementsByTagName( node, node.tagName.toLowerCase() );
            for ( var i = 0,ci; ci = list[i++]; ) {

                if ( !ci.parentNode || domUtils.isBookmarkNode( ci ) ) continue;
                //span单独处理
                if ( ci.tagName.toLowerCase() == 'span' ) {
                    if ( node === ci.parentNode ) {
                        domUtils.trimWhiteTextNode( node );
                        if(node.childNodes.length == 1){
                            node.style.cssText = ci.style.cssText + ";"+node.style.cssText;
                            domUtils.remove(ci,true);
                            continue;
                        }
                    }
                    ci.style.cssText  =  node.style.cssText + ';' + ci.style.cssText;
                    if(attrs){
                        var style = attrs.style;
                        if(style){
                            style = style.split(';');
                            for(var j=0,s;s=style[j++];){
                               ci.style[utils.cssStyleToDomStyle(s.split(':')[0])] = s.split(':')[1];
                            }
                        }
                    }
                    if(domUtils.isSameStyle(ci,node)){

                        domUtils.remove( ci, true )
                    }
                    continue;
                }
                if ( domUtils.isSameElement( node, ci ) ) {
                    domUtils.remove( ci, true );
                }
            }
            
            if(tagName == 'span'){
                var as = domUtils.getElementsByTagName( node,'a');
                for(var i=0,ai;ai=as[i++];){

                    ai.style.cssText = ';' + node.style.cssText;

                    ai.style.textDecoration = 'underline';

                }
            }
        },

        /**
         * 封装一下getElemensByTagName
         * @param node
         * @return {Array}
         */
        getElementsByTagName : function( node, name ) {
            var list = node.getElementsByTagName( name ),arr = [];
            for ( var i = 0,ci; ci = list[i++]; ) {
                arr.push( ci )
            }
            return arr;
        },
        /**
         * 合并子节点和父节点
         * @param {Element} node
         * @example &lt;span style="color:#ff"&gt;&lt;span style="font-size:12px"&gt;xxx&lt;/span&gt;&lt;/span&gt; ==&gt; &lt;span style="color:#ff;font-size:12px"&gt;xxx&lt;/span&gt;
         */
        mergToParent : function( node ) {
            var parent = node.parentNode;
            
            while ( parent && dtd.$removeEmpty[parent.tagName] ) {
                if ( parent.tagName == node.tagName || parent.tagName == 'A') {//针对a标签单独处理
                    domUtils.trimWhiteTextNode( parent );
                    //span需要特殊处理  不处理这样的情况 <span stlye="color:#fff">xxx<span style="color:#ccc">xxx</span>xxx</span>
                    if ( parent.tagName.toLowerCase() == 'span' && !domUtils.isSameStyle(parent, node) || (parent.tagName == 'A' && node.tagName == 'SPAN') ) {
                        if ( parent.childNodes.length > 1 || parent !== node.parentNode) {
                            node.style.cssText = parent.style.cssText + ";" + node.style.cssText;
                            parent = parent.parentNode;
                            continue;
                        } else {
                           
                            parent.style.cssText += ";" + node.style.cssText;
                            //trace:952 a标签要保持下划线
                            if(parent.tagName == 'A'){
                                parent.style.textDecoration = 'underline';
                            }

                        }
                    }
                    parent.tagName != 'A' && domUtils.remove( node, true );

                }
                parent = parent.parentNode;
            }

        },
        /**
         * 合并左右兄弟节点
         * @example &lt;b&gt;xxxx&lt;/b&gt;&lt;b&gt;xxx&lt;/b&gt;&lt;b&gt;xxxx&lt;/b&gt; ==> &lt;b&gt;xxxxxxxxxxx&lt;/b&gt;
         */
        mergSibling : function( node,ingorePre,ingoreNext ) {
            function merg( rtl, start, node ) {
                var next;
                if ( (next = node[rtl]) && !domUtils.isBookmarkNode( next ) && next.nodeType == 1 && domUtils.isSameElement( node, next ) ) {
                    while ( next.firstChild ) {
                        if ( start == 'firstChild' ) {
                            node.insertBefore( next.lastChild, node.firstChild );
                        } else {
                            node.appendChild( next.firstChild )
                        }

                    }
                    domUtils.remove( next );
                }
            }

           !ingorePre &&  merg( 'previousSibling', 'firstChild', node );
           !ingoreNext && merg( 'nextSibling', 'lastChild', node );
        },

        /**
         * 使得元素和他的子节点不可编辑器
         */
        unselectable :
            browser.gecko ?
                function( node ) {
                    node.style.MozUserSelect = 'none';
                }
                : browser.webkit ?
                function( node ) {
                    node.style.KhtmlUserSelect = 'none';
                }
                :
                function( node ) {
                    node.unselectable = 'on';
                    for ( var i = 0,ci; ci = node.all[i++]; ) {
                        switch ( ci.tagName.toLowerCase() ) {
                            case 'iframe' :
                            case 'textarea' :
                            case 'input' :
                            case 'select' :

                                break;
                            default :
                                ci.unselectable = 'on';
                        }
                    }
                },
        //todo yuxiang
        /**
         * 删除元素上的属性，可以删除多个
         * @param {Element} element
         * @param {Array} attrNames
         */
        removeAttributes : function ( element, attrNames ) {
            var k = attrNames.length;
            if ( k ) while ( k -- ) {
                element.removeAttribute( attrNames[k] );
            }
        },
        setAttributes : function( node, attrs ) {
            for ( var name in attrs ) {
                switch ( name ) {
                    case 'class' :
                        node.className = attrs[name];
                        break;
                    case 'style' :
                        node.style.cssText = attrs[name];
                        break;
                    default:
                        node.setAttribute( name, attrs[name] );
                }
            }

            return node;
        },

        /**
         * 获取元素的样式
         * @param {Element} element
         * @param {String} styleName
         */
        getComputedStyle : function () {
            function fixUnit(key, val){
                if (key == 'font-size' && /px$/.test(val)) {
                    val = Math.round(parseFloat(val) * 0.75) + 'pt';
                }
                return val;
            }
            if ( window.getComputedStyle ) {
                return function ( element, styleName ) {
                    return fixUnit(styleName,
                        domUtils.getStyle(element, styleName) ||
                            domUtils.getWindow( element ).getComputedStyle( element, '' ).getPropertyValue( styleName ));
                };
            }

            return function ( element, styleName ) {
                return fixUnit(styleName,
                        domUtils.getStyle(element, styleName) ||
                            ( element.currentStyle || element.style )[utils.cssStyleToDomStyle( styleName )]);
            };
        }(),

        /**
         * 删除cssClass，可以支持删除多个class，需以空格分隔
         * @param {Element} element
         * @param {Array} classNames
         */
        removeClasses : function ( element, classNames ) {
            element.className = element.className.replace(
                new RegExp( '(?:\\s*\\b)(?:' + classNames.join( '|' ) + ')(?:\\b)', 'g' ), '' );
        },

        removeStyle : function( node, name ) {
            node.style[utils.cssStyleToDomStyle( name )] = '';
            if ( node.style.removeAttribute )
                node.style.removeAttribute( utils.cssStyleToDomStyle( name ) );

            if ( !node.style.cssText )
                node.removeAttribute( 'style' );
        },
        /**
         * 判断元素是否包含某cssClass
         * @param {Element} element
         * @param {String} className
         * @returns {Boolean}
         */
        hasClass : function ( element, className ) {
            return ( ' ' + element.className + ' ' ).indexOf( ' ' + className + ' ' ) > -1;
        },

        /**
         * 阻止事件默认行为
         * @param {Event} evt
         */
        preventDefault : function ( evt ){
            if ( evt.preventDefault ) {
                evt.preventDefault();
            } else {
                evt.returnValue = false;
            }
        },
        
        getStyle : function( element, name ) {
            var value = element.style[ utils.cssStyleToDomStyle( name ) ];
            if ( /color/i.test( name ) && value.indexOf( "rgb(" ) != -1 ) {
                var array = value.split( "," );

                value = "#";
                for ( var i = 0, color; color = array[i]; i++ ) {
                    color = parseInt( color.replace( /[^\d]/gi, '' ), 10 ).toString( 16 );
                    value += color.length == 1 ? "0" + color : color;
                }

                value = value.toUpperCase();
            }
            return  value;
        },
        removeDirtyAttr : function( node ) {
            for ( var i = 0,ci,nodes = node.getElementsByTagName( '*' ); ci = nodes[i++]; ) {
                ci.removeAttribute( '_moz_dirty' )
            }
            node.removeAttribute( '_moz_dirty' )
        },
        getChildCount : function (node,fn){
            var count = 0,first = node.firstChild;
            fn = fn || function(){return 1};
            while(first){
                if(fn(first))
                    count++;
                first = first.nextSibling;
            }
            return count;
        },
        /**
         * 清除冗余的inline标签
         * @param node node下的冗余节点
         * @param tags 清除的节点的列表
         * @example <div><b><i></i></b></div> ==> <div></div>
         */
        clearReduent : function(node,tags){

            var nodes,
                reg = new RegExp( domUtils.fillChar, 'g' ),
                _parent;
            for(var t=0,ti;ti=tags[t++];){
                   nodes = node.getElementsByTagName(ti);
                  
                   for(var i=0,ci;ci=nodes[i++];){
                      if( ci.parentNode && ci[browser.ie?'innerText':'textContent'].replace(reg,'').length == 0 && ci.children.length == 0 ){

                          _parent = ci.parentNode;

                       domUtils.remove(ci);
                       while(_parent.childNodes.length == 0 && new RegExp(tags.join('|'),'i').test(_parent.tagName)){
                           ci = _parent;
                           _parent = _parent.parentNode;
                             domUtils.remove(ci)

                       }

                      }
                   }
                }

        },
        isEmptyNode : function(node){
            var first = node.firstChild;
            return !first || domUtils.getChildCount(node,function(node){
                return  !domUtils.isBr(node) &&  !domUtils.isBookmarkNode(node) && !domUtils.isWhitespace( node )
            } ) == 0
        },
        clearSelectedArr : function(nodes){
            var node;
            while(node = nodes.pop()){
                node.className = ''
            }
        },
        scrollToView : function(node,win,alignTop){
            var 
                getViewPaneSize = function(){
                    var doc = win.document,
                        mode = doc.compatMode == 'CSS1Compat';

                    return {
                        width : ( mode ? doc.documentElement.clientWidth : doc.body.clientWidth ) || 0,
                        height : ( mode ? doc.documentElement.clientHeight : doc.body.clientHeight ) || 0
                    };

                },
                getScrollPosition = function(win){

                    if ( 'pageXOffset' in win )
                    {
                        return {
                            x : win.pageXOffset || 0,
                            y : win.pageYOffset || 0
                        };
                    }
                    else
                    {
                        var doc = win.document;
                        return {
                            x : doc.documentElement.scrollLeft || doc.body.scrollLeft || 0,
                            y : doc.documentElement.scrollTop || doc.body.scrollTop || 0
                        };
                    }
                };


			var	winHeight = getViewPaneSize().height,offset = winHeight * -1;


            offset += (node.offsetHeight || 0) ;

			var elementPosition = domUtils.getXY(node);
			offset += elementPosition.y;

			var currentScroll = getScrollPosition(win).y;
			if ( offset > currentScroll || offset < currentScroll - winHeight )
				win.scrollTo( 0, offset );
        },
        isBr : function(node){
            return node.nodeType == 1 && node.tagName == 'BR';
        }

    };
})();

/**
 * @description Range类实现
 * @author zhanyi
 */
(function() {
    var editor = baidu.editor,
        browser = editor.browser,
        domUtils = editor.dom.domUtils,
        dtd = editor.dom.dtd,
        utils = editor.utils,
        guid = 0,
        fillChar = domUtils.fillChar;


    //更新collapse
    var updateCollapse = function( range ) {
        range.collapsed =
            range.startContainer && range.endContainer &&
                range.startContainer === range.endContainer &&
                range.startOffset == range.endOffset;
    },
        setEndPoint = function( toStart, node, offset, range ) {
            //如果node是自闭合标签要处理
            if ( node.nodeType == 1 && dtd.$empty[node.tagName.toLowerCase()] ) {
                offset = domUtils.getNodeIndex( node ) + (toStart ? 0 : 1);
                node = node.parentNode;
            }
            if ( toStart ) {
                range.startContainer = node;
                range.startOffset = offset;
                if ( !range.endContainer ) {
                    range.collapse( true );
                }
            } else {
                range.endContainer = node;
                range.endOffset = offset;
                if ( !range.startContainer ) {
                    range.collapse( false );
                }
            }
            updateCollapse( range );
            return range;
        },
        execContentsAction = function( range, action ) {
            //调整边界
            //range.includeBookmark();

            var start = range.startContainer,
                end = range.endContainer,
                startOffset = range.startOffset,
                endOffset = range.endOffset,
                doc = range.document,
                frag = doc.createDocumentFragment(),
                tmpStart,tmpEnd;

            if ( start.nodeType == 1 ) {
                start = start.childNodes[startOffset] || (tmpStart = start.appendChild( doc.createTextNode( '' ) ));
            }
            if ( end.nodeType == 1 ) {
                end = end.childNodes[endOffset] || (tmpEnd = end.appendChild( doc.createTextNode( '' ) ));
            }

            if ( start === end && start.nodeType == 3 ) {

                frag.appendChild( doc.createTextNode( start.substringData( startOffset, endOffset - startOffset ) ) );
                //is not clone
                if ( action ) {
                    start.deleteData( startOffset, endOffset - startOffset );
                    range.collapse( true );
                }

                return frag;
            }


            var current,currentLevel,clone = frag,
                startParents = domUtils.findParents( start, true ),endParents = domUtils.findParents( end, true );
            for ( var i = 0; startParents[i] == endParents[i]; i++ );


            for ( var j = i,si; si = startParents[j]; j++ ) {
                current = si.nextSibling;
                if ( si == start ) {
                    if ( !tmpStart ) {
                        if ( range.startContainer.nodeType == 3 ) {
                            clone.appendChild( doc.createTextNode( start.nodeValue.slice( startOffset ) ) );
                            //is not clone
                            if ( action ) {
                                start.deleteData( startOffset, start.nodeValue.length - startOffset );

                            }
                        } else {
                            clone.appendChild( !action ? start.cloneNode( true ) : start );
                        }
                    }

                } else {
                    currentLevel = si.cloneNode( false );
                    clone.appendChild( currentLevel );
                }


                while ( current ) {
                    if ( current === end || current === endParents[j] )break;
                    si = current.nextSibling;
                    clone.appendChild( !action ? current.cloneNode( true ) : current );


                    current = si;
                }
                clone = currentLevel;

            }


            clone = frag;

            if ( !startParents[i] ) {
                clone.appendChild( startParents[i - 1].cloneNode( false ) );
                clone = clone.firstChild;
            }
            for ( var j = i,ei; ei = endParents[j]; j++ ) {
                current = ei.previousSibling;
                if ( ei == end ) {
                    if ( !tmpEnd && range.endContainer.nodeType == 3 ) {
                        clone.appendChild( doc.createTextNode( end.substringData( 0, endOffset ) ) );
                        //is not clone
                        if ( action ) {
                            end.deleteData( 0, endOffset );

                        }
                    }


                } else {
                    currentLevel = ei.cloneNode( false );
                    clone.appendChild( currentLevel );
                }
                //如果两端同级，右边第一次已经被开始做了
                if ( j != i || !startParents[i] ) {
                    while ( current ) {
                        if ( current === start )break;
                        ei = current.previousSibling;
                        clone.insertBefore( !action ? current.cloneNode( true ) : current, clone.firstChild );


                        current = ei;
                    }

                }
                clone = currentLevel;
            }


            if ( action ) {
                range.setStartBefore( !endParents[i] ? endParents[i - 1] : !startParents[i] ? startParents[i - 1] : endParents[i] ).collapse( true )
            }
            tmpStart && domUtils.remove( tmpStart );
            tmpEnd && domUtils.remove( tmpEnd );
            return frag;
        };


    /**
     * Range类
     * @constructor
     * @namespace range对象
     * @param {} document 编辑器页面document对象
     */
    var Range = baidu.editor.dom.Range = function( document ) {
        var me = this;
        me.startContainer =
            me.startOffset =
                me.endContainer =
                    me.endOffset = null;
        me.document = document;
        me.collapsed = true;
    };
    function removeFillDataWithEmptyParentNode(node){
         var parent = node.parentNode,
            tmpNode;
            domUtils.remove( node );
            while(parent && dtd.$removeEmpty[parent.tagName] && parent.childNodes.length == 0){
                tmpNode = parent;
                domUtils.remove(parent);
                parent = tmpNode.parentNode;
            }
    }
    Range.prototype = {
        /**
         * 克隆选中的内容到一个fragment里
         * @return {}
         */
        cloneContents : function() {

            return this.collapsed ? null : execContentsAction( this, 0 );
        },
        deleteContents : function() {
            if ( !this.collapsed )
                execContentsAction( this, 1 );
            return this;
        },
        extractContents : function() {
            return this.collapsed ? null : execContentsAction( this, 2 );
        },
        setStart : function( node, offset ) {
            return setEndPoint( true, node, offset, this );
        },
        setEnd : function( node, offset ) {
            return setEndPoint( false, node, offset, this );
        },
        setStartAfter : function( node ) {
            return this.setStart( node.parentNode, domUtils.getNodeIndex( node ) + 1 );
        },
        setStartBefore : function( node ) {
            return this.setStart( node.parentNode, domUtils.getNodeIndex( node ) );
        },
        setEndAfter : function( node ) {
            return this.setEnd( node.parentNode, domUtils.getNodeIndex( node ) + 1 );
        },
        setEndBefore : function( node ) {
            return this.setEnd( node.parentNode, domUtils.getNodeIndex( node ) );
        },
        selectNode : function( node ) {
            return this.setStartBefore( node ).setEndAfter( node );
        },
        /**
         * 选中node下的所有节点
         * @param {Element} node 要设置的节点
         */
        selectNodeContents : function( node ) {
            return this.setStart( node, 0 ).setEnd( node, node.nodeType == 3 ? node.nodeValue.length : node.childNodes.length );
        },

        /**
         * 克隆range
         * @return {Range} 克隆的range对象
         */
        cloneRange : function() {
            var me = this,range = new Range( me.document );
            return range.setStart( me.startContainer, me.startOffset ).setEnd( me.endContainer, me.endOffset );

        },

        /**
         * 让选区闭合
         * @param {Boolean} toStart 是否在选区开始位置闭合选区
         */
        collapse : function( toStart ) {
            var me = this;
            if ( toStart ) {
                me.endContainer = me.startContainer;
                me.endOffset = me.startOffset;
            }
            else {
                me.startContainer = me.endContainer;
                me.startOffset = me.endOffset;
            }

            me.collapsed = true;
            return me;
        },
        /**
         * 调整range的边界，缩进到合适的位置
         */
        shrinkBoundary : function( ignoreEnd ) {
            var me = this,child,
                collapsed = me.collapsed;
            while ( me.startContainer.nodeType == 1 //是element
                && (child = me.startContainer.childNodes[me.startOffset]) //子节点也是element
                && child.nodeType == 1  && !domUtils.isBookmarkNode(child)
                && !dtd.$empty[child.tagName] ) {
                me.setStart( child, 0 );
            }
            if ( collapsed )
                return me.collapse( true );
            if ( !ignoreEnd ) {
                while ( me.endContainer.nodeType == 1//是element
                    && me.endOffset > 0 //如果是空元素就退出 endOffset=0那么endOffst-1为负值，childNodes[endOffset]报错
                    && (child = me.endContainer.childNodes[me.endOffset - 1]) //子节点也是element
                    && child.nodeType == 1 && !domUtils.isBookmarkNode(child)
                    && !dtd.$empty[child.tagName] ) {
                    me.setEnd( child, child.childNodes.length );
                }
            }

            return me;
        },
        /**
         * 找到祖先节点
         * @param includeSelf
         * @param {Boolean} ignoreTextNode 是否忽略文本节点
         */
        getCommonAncestor : function( includeSelf, ignoreTextNode ) {
            var start = this.startContainer,
                end = this.endContainer;
            if ( start === end ) {
                if ( includeSelf && start.nodeType == 1 && this.startOffset == this.endOffset - 1 ) {
                    return start.childNodes[this.startOffset];
                }
                //只有在上来就相等的情况下才会出现是文本的情况
                return ignoreTextNode && start.nodeType == 3 ? start.parentNode : start;
            }
            return domUtils.getCommonAncestor( start, end );

        },
        /**
         * 切割文本节点，将边界扩大到element
         * @param {Boolean}  为真就不处理结束边界
         * @example <b>|xxx</b>
         * startContainer = xxx
         * startOffset = 0
         * 执行后
         * startContainer = <b>
         * startOffset = 0
         * @example <b>xx|x</b>
         * startContainer = xxx
         * startOffset = 2
         * 执行后
         * startContainer = <b>
         * startOffset = 1  因为将xxx切割成2个节点了
         */
        trimBoundary : function( ignoreEnd ) {
            this.txtToElmBoundary();
            var start = this.startContainer,
                offset = this.startOffset,
                collapsed = this.collapsed,
                end = this.endContainer;
            if ( start.nodeType == 3 ) {
                if ( offset == 0 ) {
                    this.setStartBefore( start )
                } else {
                    if ( offset >= start.nodeValue.length ) {
                        this.setStartAfter( start );
                    } else {
                        var textNode = domUtils.split( start, offset );
                        //跟新结束边界
                        if ( start === end )
                            this.setEnd( textNode, this.endOffset - offset );
                        else if ( start.parentNode === end )
                            this.endOffset += 1;
                        this.setStartBefore( textNode );
                    }
                }
                if ( collapsed ) {
                    return this.collapse( true );
                }
            }
            if ( !ignoreEnd ) {
                offset = this.endOffset;
                end = this.endContainer;
                if ( end.nodeType == 3 ) {
                    if ( offset == 0 ) {
                        this.setEndBefore( end );
                    } else {
                        if ( offset >= end.nodeValue.length ) {
                            this.setEndAfter( end );
                        } else {
                            domUtils.split( end, offset );
                            this.setEndAfter( end );
                        }
                    }

                }
            }
            return this;
        },
        /**
         * 如果边界在文本的边上，就提升边界到元素上
         * @example <b> |xxx</b>
         * startContainer = xxx
         * startOffset = 0
         * 执行后
         * startContainer = <b>
         * startOffset = 0
         * @example <b> xxx| </b>
         * startContainer = xxx
         * startOffset = 3
         * 执行后
         * startContainer = <b>
         * startOffset = 1
         */
        txtToElmBoundary : function() {
            function adjust( r, c ) {
                var container = r[c + 'Container'],
                    offset = r[c + 'Offset'];
                if ( container.nodeType == 3 ) {
                    if ( !offset ) {
                        r['set' + c.replace( /(\w)/, function( a ) {
                            return a.toUpperCase()
                        } ) + 'Before']( container )
                    } else if ( offset >= container.nodeValue.length ) {
                        r['set' + c.replace( /(\w)/, function( a ) {
                            return a.toUpperCase()
                        } ) + 'After' ]( container )
                    }
                }
            }

            if ( !this.collapsed ) {
                adjust( this, 'start' );
                adjust( this, 'end' );
            }

            return this;
        },

        /**
         * 在range开始插入一个节点或者一个fragment
         * @param {Node/DocumentFragment}
            */
        insertNode : function( node ) {
            var first = node,length = 1;
            if ( node.nodeType == 11 ) {
                first = node.firstChild;
                length = node.childNodes.length;
            }


            this.trimBoundary( true );

            var start = this.startContainer,
                offset = this.startOffset;

            var nextNode = start.childNodes[ offset ];

            if ( nextNode ) {
                start.insertBefore( node, nextNode );

            }
            else {
                start.appendChild( node );
            }


            if ( first.parentNode === this.endContainer ) {
                this.endOffset = this.endOffset + length;
            }


            return this.setStartBefore( first );
        },

        setCursor : function( toEnd ) {
            return this.collapse( toEnd ? false : true ).select();
        },
        /**
         * 创建标签
         * @params {Boolean}
         * @returns {Object} bookmark对象
         */
        createBookmark : function( serialize, same ) {
            var endNode,
                startNode = this.document.createElement( 'span' );
            startNode.style.cssText = 'display:none;line-height:0px;';
            startNode.appendChild( this.document.createTextNode( '\uFEFF' ) );
            startNode.id = '_baidu_bookmark_start_' + (same ? '' : guid++);

            if ( !this.collapsed ) {
                endNode = startNode.cloneNode( true );
                endNode.id = '_baidu_bookmark_end_' + (same ? '' : guid++);
            }
            this.insertNode( startNode );

            if ( endNode ) {
                this.collapse( false ).insertNode( endNode );
                this.setEndBefore( endNode )
            }
            this.setStartAfter( startNode );

            return {
                start : serialize ? startNode.id : startNode,
                end : endNode ? serialize ? endNode.id : endNode : null,
                id : serialize
            }
        },
        /**
         *  移动边界到bookmark
         *  @params {Object} bookmark对象
         *  @returns {Range}
         */
        moveToBookmark : function( bookmark ) {
            var start = bookmark.id ? this.document.getElementById( bookmark.start ) : bookmark.start,
                end = bookmark.end && bookmark.id ? this.document.getElementById( bookmark.end ) : bookmark.end;
            this.setStartBefore( start );
            domUtils.remove( start );
            if ( end ) {
                this.setEndBefore( end );
                domUtils.remove( end )
            } else {
                this.collapse( true );
            }

            return this;
        },
        /**
         * 调整边界到一个block元素上，或者移动到最大的位置
         * @params {Boolean} 扩展到block元素
         * @params {Function} 停止函数
         */
        enlarge : function( toBlock, stopFn ) {
            var isBody = domUtils.isBody,
                pre,node,tmp = this.document.createTextNode( '' );
            if ( toBlock ) {
                node = this.startContainer;
                if ( node.nodeType == 1 ) {
                    if ( node.childNodes[this.startOffset] ) {
                        pre = node = node.childNodes[this.startOffset]
                    } else {
                        node.appendChild( tmp );
                        pre = node = tmp;
                    }
                } else {
                    pre = node;
                }

                while ( 1 ) {
                    if ( domUtils.isBlockElm( node ) ) {
                        node = pre;
                        while ( (pre = node.previousSibling) && !domUtils.isBlockElm( pre ) ) {
                            node = pre;
                        }
                        this.setStartBefore( node );

                        break;
                    }
                    pre = node;
                    node = node.parentNode;
                }
                node = this.endContainer;
                if ( node.nodeType == 1 ) {
                    if(pre = node.childNodes[this.endOffset]) {
                        node.insertBefore( tmp, pre );
                    }else{
                        node.appendChild(tmp)
                    }

                    pre = node = tmp;
                } else {
                    pre = node;
                }

                while ( 1 ) {
                    if ( domUtils.isBlockElm( node ) ) {
                        node = pre;
                        while ( (pre = node.nextSibling) && !domUtils.isBlockElm( pre ) ) {
                            node = pre;
                        }
                        this.setEndAfter( node );

                        break;
                    }
                    pre = node;
                    node = node.parentNode;
                }
                if ( tmp.parentNode === this.endContainer ) {
                    this.endOffset--;
                }
                domUtils.remove( tmp )
            }

            // 扩展边界到最大
            if ( !this.collapsed ) {
                while ( this.startOffset == 0 ) {
                    if ( stopFn && stopFn( this.startContainer ) )
                        break;
                    if ( isBody( this.startContainer ) )break;
                    this.setStartBefore( this.startContainer );
                }
                while ( this.endOffset == (this.endContainer.nodeType == 1 ? this.endContainer.childNodes.length : this.endContainer.nodeValue.length) ) {
                    if ( stopFn && stopFn( this.endContainer ) )
                        break;
                    if ( isBody( this.endContainer ) )break;

                    this.setEndAfter( this.endContainer )
                }
            }

            return this;
        },
        /**
         * 调整边界
         * @example
         * <b>xx[</b>xxxxx] ==> <b>xx</b>[xxxxx]
         * <b>[xx</b><i>]xxx</i> ==> <b>[xx</b>]<i>xxx</i>
         *
         */
        adjustmentBoundary : function() {
            if(!this.collapsed){
                while ( !domUtils.isBody( this.startContainer ) &&
                    this.startOffset == this.startContainer[this.startContainer.nodeType == 3 ? 'nodeValue' : 'childNodes'].length
                ) {
                    this.setStartAfter( this.startContainer );
                }
                while ( !domUtils.isBody( this.endContainer ) && !this.endOffset ) {
                    this.setEndBefore( this.endContainer );
                }
            }
            return this;
        },
        /**
         * 给选区中的内容加上inline样式
         * @param {String} tagName 标签名称
         * @param {Object} attrObj 属性

         */
        applyInlineStyle : function( tagName, attrs ,list) {
            
            if(this.collapsed)return;
            this.trimBoundary().enlarge( false,
                function( node ) {
                    return node.nodeType == 1 && domUtils.isBlockElm( node )
                } ).adjustmentBoundary();


            var bookmark = this.createBookmark(),
                end = bookmark.end,
                filterFn = function( node ) {
                    return node.nodeType == 1 ? node.tagName.toLowerCase() != 'br' : !domUtils.isWhitespace( node )
                },
                current = domUtils.getNextDomNode( bookmark.start, false, filterFn ),
                node,
                pre,
                range = this.cloneRange();

            while ( current && (domUtils.getPosition( current, end ) & domUtils.POSITION_PRECEDING) ) {


                if ( current.nodeType == 3 || dtd[tagName][current.tagName] ) {
                    range.setStartBefore( current );
                    node = current;
                    while ( node && (node.nodeType == 3 || dtd[tagName][node.tagName]) && node !== end ) {

                        pre = node;
                        node = domUtils.getNextDomNode( node, node.nodeType == 1, null, function( parent ) {
                            return dtd[tagName][parent.tagName]
                        } )
                    }

                    var frag = range.setEndAfter( pre ).extractContents(),elm;
                    if(list && list.length > 0){
                        var level,top;
                        top = level = list[0].cloneNode(false);
                        for(var i=1,ci;ci=list[i++];){

                            level.appendChild(ci.cloneNode(false));
                            level = level.firstChild;

                        }
                        elm = level;

                    }else{
                        elm = range.document.createElement( tagName )
                    }
                    
                    if ( attrs ) {
                        domUtils.setAttributes( elm, attrs )
                    }
                    elm.appendChild( frag );
                    //去除子节点相同的
                    domUtils.mergChild( elm, tagName,attrs );
                    range.insertNode( list ?  top : elm );
                    domUtils.mergSibling( elm );
                    domUtils.clearEmptySibling( elm );
                    current = domUtils.getNextDomNode( elm, false, filterFn );
                    domUtils.mergToParent( elm );
                    if ( node === end )break;
                } else {
                    current = domUtils.getNextDomNode( current, true, filterFn )
                }
            }

            return this.moveToBookmark( bookmark );
        },
        /**
         * 去掉inline样式
         * @params {String/Array} 要去掉的标签名
         */
        removeInlineStyle : function( tagName ) {
            if(this.collapsed)return;
            tagName = utils.isArray( tagName ) ? tagName : [tagName];

            this.shrinkBoundary().adjustmentBoundary();

            var start = this.startContainer,end = this.endContainer;

            while ( 1 ) {

                if ( start.nodeType == 1 ) {
                    if ( utils.indexOf( tagName, start.tagName.toLowerCase() ) > -1 ) {
                        break;
                    }
                    if ( start.tagName.toLowerCase() == 'body' ) {
                        start = null;
                        break;
                    }


                }
                start = start.parentNode;

            }

            while ( 1 ) {
                if ( end.nodeType == 1 ) {
                    if ( utils.indexOf( tagName, end.tagName.toLowerCase() ) > -1 ) {
                        break;
                    }
                    if ( end.tagName.toLowerCase() == 'body' ) {
                        end = null;
                        break;
                    }

                }
                end = end.parentNode;
            }


            var bookmark = this.createBookmark(),
                frag,
                tmpRange;
            if ( start ) {
                tmpRange = this.cloneRange().setEndBefore( bookmark.start ).setStartBefore( start );
                frag = tmpRange.extractContents();
                tmpRange.insertNode( frag );
                domUtils.clearEmptySibling( start, true );
                start.parentNode.insertBefore( bookmark.start, start );

            }

            if ( end ) {
                tmpRange = this.cloneRange().setStartAfter( bookmark.end ).setEndAfter( end );
                frag = tmpRange.extractContents();
                tmpRange.insertNode( frag );
                domUtils.clearEmptySibling( end, false, true );
                end.parentNode.insertBefore( bookmark.end, end.nextSibling );


            }

            var current = domUtils.getNextDomNode( bookmark.start, false, function( node ) {
                return node.nodeType == 1
            } ),next;

            while ( current && current !== bookmark.end ) {

                next = domUtils.getNextDomNode( current, true, function( node ) {
                    return node.nodeType == 1
                } );
                if ( utils.indexOf( tagName, current.tagName.toLowerCase() ) > -1 ) {

                    domUtils.remove( current, true );


                }
                current = next;
            }



            return this.moveToBookmark( bookmark );
        },
        /**
         * 得到一个字闭合的节点
         */
        getClosedNode : function() {

            var node;
            if ( !this.collapsed ) {
                var range = this.cloneRange().adjustmentBoundary().shrinkBoundary();
                if ( range.startContainer.nodeType == 1 && range.startContainer === range.endContainer && range.endOffset - range.startOffset == 1 ) {
                    var child = range.startContainer.childNodes[range.startOffset];
                    if ( child && child.nodeType == 1 && dtd.$empty[child.tagName] ) {
                        node = child;
                    }
                }
            }
            return node;
        },
        /**
         * 根据range选中元素
         */
        select : browser.ie ? function( notInsertFillData ) {

            var collapsed = this.collapsed,
                nativeRange;

            if ( !collapsed )
                this.shrinkBoundary();
            var node = this.getClosedNode();
            if ( node ) {
                try {
                    nativeRange = this.document.body.createControlRange();
                    nativeRange.addElement( node );
                    nativeRange.select();
                } catch( e ) {
                }
                return this;
            }

            var bookmark = this.createBookmark(),
                start = bookmark.start,
                end;

            nativeRange = this.document.body.createTextRange();
            nativeRange.moveToElementText( start );
            nativeRange.moveStart( 'character', 1 );
            if ( !collapsed ) {
                var nativeRangeEnd = this.document.body.createTextRange();
                end = bookmark.end;
                nativeRangeEnd.moveToElementText( end );
                nativeRange.setEndPoint( 'EndToEnd', nativeRangeEnd );

            } else {
                if ( !notInsertFillData && this.startContainer.nodeType != 3 ) {

                    //使用<span>|x<span>固定住光标
                    var fillData = editor.fillData,
                        tmpText,
                        tmp = this.document.createElement( 'span' );

                    try {
                        if ( fillData && fillData.parentNode && !fillData.nodeValue.replace( new RegExp( domUtils.fillChar, 'g' ), '' ).length) {


                            removeFillDataWithEmptyParentNode(fillData)

                        }

                    } catch( e ) {
                    }

                    tmpText = editor.fillData = this.document.createTextNode( fillChar );
                    tmp.appendChild( this.document.createTextNode( fillChar) );
                    start.parentNode.insertBefore( tmp, start );
                    start.parentNode.insertBefore( tmpText, start );

                    nativeRange.moveStart( 'character', -1 );
                    nativeRange.collapse( true );

                }
            }

            this.moveToBookmark( bookmark );
            tmp && domUtils.remove( tmp );
            nativeRange.select();
            return this;

        } : function( notInsertFillData ) {

            var win = domUtils.getWindow( this.document ),
                sel = win.getSelection(),
                txtNode,child;
           
            browser.gecko ?  this.document.body.focus() : win.focus();
            function mergSibling(node){
                if(node && node.nodeType == 3 && !node.nodeValue.replace( new RegExp( domUtils.fillChar, 'g' ), '' ).length){
                    domUtils.remove(node);
                }
            }
            if ( sel ) {
                sel.removeAllRanges();
              
                // trace:870 chrome/safari后边是br对于闭合得range不能定位 所以去掉了判断
                // this.startContainer.nodeType != 3 &&! ((child = this.startContainer.childNodes[this.startOffset]) && child.nodeType == 1 && child.tagName == 'BR'
                if ( this.collapsed && !notInsertFillData  ){

                    var fillData = editor.fillData;

                    txtNode =  this.document.createTextNode( fillChar );
                    editor.fillData = txtNode;
                    //跟着前边走
                    this.insertNode( txtNode );
                    //todo fillData有时会失效，不能关联到上一次的文本节点
                    if ( fillData &&  fillData.parentNode  ) {

                        if(!fillData.nodeValue.replace( new RegExp( domUtils.fillChar, 'g' ), '' ).length)
                            removeFillDataWithEmptyParentNode(fillData)
                        else
                            fillData.nodeValue = fillData.nodeValue.replace( new RegExp( domUtils.fillChar, 'g' ), '' )

                    }
                    mergSibling(txtNode.previousSibling);
                    mergSibling(txtNode.nextSibling);
                    this.setStart( txtNode, browser.webkit ? 1 : 0 ).collapse( true );

                }
                var nativeRange = this.document.createRange();
                nativeRange.setStart( this.startContainer, this.startOffset );
                nativeRange.setEnd( this.endContainer, this.endOffset );

                sel.addRange( nativeRange );

            }
            return this;
        },


        scrollToView : function(win,offset){
            
            win = win ? window : domUtils.getWindow(this.document);

            var span = this.document.createElement('span');
            span.appendChild(this.document.createTextNode(domUtils.fillChar));
            var tmpRange = this.cloneRange();
            tmpRange.insertNode(span);
            domUtils.scrollToView(span,win);

            domUtils.remove(span);
            return this;
        }

    };
})();
(function () {
    baidu.editor.dom.Selection = Selection;

    var domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd,
        ie = baidu.editor.browser.ie;

    function getBoundaryInformation( range, start ) {

        var getIndex = domUtils.getNodeIndex;

        range = range.duplicate();
        range.collapse( start );


        var parent = range.parentElement();

        //如果节点里没有子节点，直接退出
        if ( !parent.hasChildNodes() ) {
            return  {container:parent,offset:0};
        }

        var siblings = parent.children,
            child,
            testRange = range.duplicate(),
            startIndex = 0,endIndex = siblings.length - 1,index = -1,
            distance;

        while ( startIndex <= endIndex ) {
            index = Math.floor( (startIndex + endIndex) / 2 );
            child = siblings[index];
            testRange.moveToElementText( child );
            var position = testRange.compareEndPoints( 'StartToStart', range );


            if ( position > 0 ) {

                endIndex = index - 1;
            } else if ( position < 0 ) {

                startIndex = index + 1;
            } else {
                //trace:1043
                return  {container:parent,offset:getIndex( child )};
//                return  dtd.$empty[child.tagName.toLowerCase()] ?
//                {container:parent,offset:getIndex( child )} :
//                {container:child,offset:0}

            }
        }

        if ( index == -1 ) {
            testRange.moveToElementText( parent );
            testRange.setEndPoint( 'StartToStart', range );
            distance = testRange.text.replace( /(\r\n|\r)/g, '\n' ).length;
            siblings = parent.childNodes;
            if ( !distance ) {
                child = siblings[siblings.length - 1];
                return  {container:child,offset:child.nodeValue.length};
            }

            var i = siblings.length;
            while ( distance > 0 )
                distance -= siblings[ --i ].nodeValue.length;

            return {container:siblings[i],offset:-distance}
        }

        testRange.collapse( position > 0 );
        testRange.setEndPoint( position > 0 ? 'StartToStart' : 'EndToStart', range );
        distance = testRange.text.replace( /(\r\n|\r)/g, '\n' ).length;
        if ( !distance ) {
            return  dtd.$empty[child.tagName] ?

            {container : parent,offset:getIndex( child ) + (position > 0 ? 0 : 1)} :
            {container : child,offset: position > 0 ? 0 : child.childNodes.length}
        }

        while ( distance > 0 ) {
            try{
                var pre = child;
                child = child[position > 0 ? 'previousSibling' : 'nextSibling'];
                distance -= child.nodeValue.length;
            }catch(e){
                return {container:parent,offset:getIndex(pre)};
            }

        }
        return  {container:child,offset:position > 0 ? -distance : child.nodeValue.length + distance}
    }

    function transformIERangeToRange( ieRange, range ) {
        if ( ieRange.item ) {
            range.selectNode( ieRange.item( 0 ) );
        } else {
            var bi = getBoundaryInformation( ieRange, true );
            range.setStart( bi.container, bi.offset );
            if ( ieRange.compareEndPoints( 'StartToEnd',ieRange ) != 0 ) {
                bi = getBoundaryInformation( ieRange, false );
                range.setEnd( bi.container, bi.offset );
            }
        }
        return range;
    }

    function _getIERange(sel){
        var ieRange = sel.getNative().createRange();
        var el = ieRange.item ? ieRange.item( 0 ) : ieRange.parentElement();
        if ( ( el.ownerDocument || el ) === sel.document ) {
            return ieRange;
        }
    }
    function Selection( doc ) {
        var me = this, iframe;
        me.document = doc;

        if ( ie ) {
            iframe = domUtils.getWindow(doc).frameElement;
            domUtils.on( iframe, 'beforedeactivate', function () {

                me._bakIERange = me.getIERange();
            } );
            domUtils.on( iframe, 'activate', function () {
                try {
                    if ( !_getIERange(me) && me._bakIERange ) {
                        me._bakIERange.select();
                    }
                } catch ( ex ) {
                }
                me._bakIERange = null;
            } );
        }
        iframe = doc = null;
    }

    Selection.prototype = {
        /**
         * 获取原生seleciton对象
         * @returns {Selection}
         */
        getNative : function () {
            if ( ie ) {
                return this.document.selection;
            } else {
                return domUtils.getWindow( this.document ).getSelection();
            }
        },



       getIERange : function () {

            var ieRange = _getIERange(this);
            if ( !ieRange ) {
                if ( this._bakIERange ) {
                    return this._bakIERange;
                }
            }
            return ieRange;
        },

        /**
         * 缓存当前选区的range和startElement
         */
        cache : function () {
            this.clear();
            this._cachedRange = this.getRange();
            this._cachedStartElement = this.getStart();
        },

        /**
         * 清空缓存
         */
        clear : function () {
            this._cachedRange = this._cachedStartElement = null;
        },

        /**
         * 获取选区对应的Range
         * @returns {baidu.editor.dom.Range}
         */
        getRange : function () {
            var me = this;
            
            function optimze(range){
                var child = me.document.body.firstChild,
                    collapsed = range.collapsed;
                while(child && child.firstChild){
                    range.setStart(child,0);
                    child = child.firstChild;
                }
                if(!range.startContainer){
                    range.setStart(me.document.body,0)
                }
                if(collapsed){
                    range.collapse(true);
                }
            }
            if ( me._cachedRange != null ) {
                return this._cachedRange;
            }
            var range = new baidu.editor.dom.Range( me.document );

            if ( ie ) {
                var nativeRange = me.getIERange();
                if(nativeRange){
                    transformIERangeToRange( nativeRange, range );
                }else{
                    optimze(range)
                }

            } else {
                var sel = me.getNative();
                if ( sel && sel.rangeCount ) {
                    var firstRange = sel.getRangeAt( 0 );
                    var lastRange = sel.getRangeAt( sel.rangeCount - 1 );
                    range.setStart( firstRange.startContainer, firstRange.startOffset ).setEnd( lastRange.endContainer, lastRange.endOffset );
                    if(range.collapsed && domUtils.isBody(range.startContainer) && !range.startOffset){
                        optimze(range)
                    }
                } else {
                    
                    optimze(range)
                        
                }
                
            }

            return range;
        },

        /**
         * 获取开始元素，用于状态反射
         * @returns {Element}
         */
        getStart : function () {
            if ( this._cachedStartElement ) {
                return this._cachedStartElement;
            }
            var range = ie ? this.getIERange()  : this.getRange(),
                tmpRange,
                start,tmp,parent;

            if (ie) {
                if(!range){
                    //todo 给第一个值可能会有问题
                   return this.document.body.firstChild;
                }
                //control元素
                if (range.item)
                    return range.item(0);

                tmpRange = range.duplicate();
                //修正ie下<b>x</b>[xx] 闭合后 <b>x|</b>xx
                tmpRange.text.length > 0 && tmpRange.moveStart('character',1);
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

                if (start.nodeType == 1 && start.hasChildNodes())
                    start = start.childNodes[Math.min(start.childNodes.length - 1, range.startOffset)];

                if (start.nodeType == 3)
                    return start.parentNode;


            }
            return start;

        },
         //得到选区的文本
        getText : function(){
            var nativeSel = this.getNative(),
                nativeRange = baidu.editor.browser.ie ? nativeSel.createRange() : nativeSel.getRangeAt(0);

            return nativeRange.text || nativeRange.toString();
        }
    };


})();
///import editor.js
///import core/utils.js
///import core/EventBase.js
///import core/browser.js
///import core/dom/domUtils.js
///import core/dom/Selection.js
///import core/dom/dtd.js
(function () {
    baidu.editor.Editor = Editor;

    var editor = baidu.editor,
        utils = editor.utils,
        EventBase = editor.EventBase,
        domUtils = editor.dom.domUtils,
        Selection = editor.dom.Selection,
        ie = editor.browser.ie,
        uid = 0,
        browser = editor.browser,
        dtd = editor.dom.dtd,
        textarea;

    /**
     * 编辑器类
     * @public
     * @class
     * @extends baidu.editor.EventBase
     * @name baidu.editor.Editor
     * @param {Object} options
     * @config {String}         id     将编辑器渲染到容器的id
     * @config {String}         initialStyle     编辑器内部样式
     * @config {String}         initialContent   初始化编辑器的内容
     * @config {String}         iframeCssUrl   要引入css的url
     * @config {String}         removeFormatTags   配置格式刷删除的标签
     * @config {String}         removeFormatAttributes   配置格式刷删除的属性
     * @config {String}         enterTag   编辑器回车标签。p或br
     * @config {Number}         maxUndoCount   最多可以回退的次数
     * @config {Number}         maxInputCount   当输入的字符数超过该值时，保存一次现场
     * @config {String}         selectedTdClass   设定选中td的样式名称
     * @config {Boolean}         pasteplain   是否纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
     * @config {String}         textarea   提交表单时，服务器端接收编辑器内容的名字
     * @config {Boolean}         focus   初始化时，是否让编辑器获得焦点true或false
     * @config {String}         indentValue   初始化时，首行缩进距离
     */
    function Editor( options ) {
        var me = this;
        me.uid = uid ++;
        EventBase.call( me );
        me.commands = {};
        me.options = utils.extend( options || {}, Editor.DEFAULT_OPTIONS, true );
        me.initPlugins();
    }

    /**
     * @event
     * @name baidu.editor.Editor#ready
     */
    /**
     * @event
     * @name baidu.editor.Editor#beforeSelectionChange
     */
    /**
     * @event
     * @name baidu.editor.Editor#selectionChange
     */

    Editor.DEFAULT_OPTIONS = /**@lends baidu.editor.Editor.prototype*/{
        initialStyle: '',
        initialContent: '',
        iframeCssUrl: '',
        removeFormatTags : 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var',
        removeFormatAttributes : 'class,style,lang,width,height,align,hspace,valign',
        enterTag : 'p',
        maxUndoCount : 20,
        maxInputCount : 20,
        selectedTdClass : 'selectTdClass',
        pasteplain : 0,
        textarea : 'editorValue',
        focus : false,
        indentValue : '2em'

    };

    Editor.prototype = /**@lends baidu.editor.Editor.prototype*/{

        /**
         * 渲染编辑器的DOM到指定容器，必须且只能调用一次
         * @public
         * @function
         * @param {Element|String} container
         */
        render : function ( container ) {
            if (container.constructor === String) {
                container = document.getElementById(container);
            }
            var iframeId = 'baidu_editor_' + this.uid;
            container.innerHTML = '<iframe id="' + iframeId + '"' +
                'width="100%" height="100%" scroll="no" frameborder="0"></iframe>';
            // firefox4 getComputedStyle不能在domReady之前使用
//            if (domUtils.getComputedStyle(container, 'position') != 'absolute') {
//                container.style.position = 'relative';
//            }
            container.style.overflow = 'hidden';
            var iframe = document.getElementById( iframeId ),
                doc = iframe.contentWindow.document;
            this._setup( doc );
        },

        _setup: function ( doc ) {
            var options = this.options,
                me = this;
            doc.open();
            var html = ( ie ? '' : '<!DOCTYPE html>' ) +
                '<html xmlns="http://www.w3.org/1999/xhtml"><head><title></title><style type="text/css">' +
                ( ie && browser.version < 9 ? 'body' : 'html' ) + '{ word-wrap: break-word;word-break: break-all;cursor: text; height: 100%; }' + ( options.initialStyle ) + '</style>' +
                ( options.iframeCssUrl ? '<link rel="stylesheet" type="text/css" href="' + utils.unhtml( options.iframeCssUrl ) + '"/>' : '' ) + '</head><body>' +
                '</body></html>';
            doc.write( html );
            doc.close();
            if ( ie ) {
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
            this.selection = new Selection( doc );
            this._initEvents();
            this.options.initialContent && this.setContent(this.options.initialContent);
            //为form提交提供一个隐藏的textarea
            var form = domUtils.findParentByTagName(this.iframe,'form');
            
            if(form){
                domUtils.on(form,'submit',function(){

                    if(!textarea){
                        textarea = document.createElement('textarea');
                        textarea.setAttribute('name',me.options.textarea);
                        textarea.style.display = 'none';
                        this.appendChild(textarea);
                    }
                    textarea.value = me.getContent();
                    
                })
            }
            //编辑器不能为空内容
            
            if(domUtils.getChildCount(this.body,function(node){return !domUtils.isBr(node)}) == 0){
                this.body.innerHTML = '<p>'+(browser.ie?'':'<br/>')+'</p>';
            }
            //如果要求focus, 就把光标定位到内容的最后
            if(me.options.focus){

                var range = this.selection.getRange(),
                    last = this.body.lastChild;
               
                

                while(last.lastChild){
                    last = last.lastChild;
                }
             
                range[domUtils.isBr(last) ? 'setStartBefore' : 'setStartAfter'](last).setCursor()


            }
            this.fireEvent( 'ready' );


        },
        /**
         * 手动触发创建textarea,同步编辑的内容到textarea,为后台等到内容做准备
         * @public
         * @function
         */

        sync : function(){
            var form = domUtils.findParentByTagName(this.iframe,'form');
            if(form){
                if(!textarea){
                    textarea = document.createElement('textarea');
                    textarea.setAttribute('name',this.options.textarea);
                    textarea.style.display = 'none';
                    form.appendChild(textarea);
                }
                textarea.value = this.getContent();
            }
        },
        
        setHeight: function (height){
            this.iframe.parentNode.style.height = height + 'px';
        },

        /**
         * 获取编辑内容
         * @public
         * @function
         * @returns {String}
         */
        getContent : function () {
            this.fireEvent( 'beforegetcontent' );
            var reg = new RegExp( domUtils.fillChar, 'g' ),
                html = this.document.body.innerHTML.replace(reg,'');
            this.fireEvent( 'aftergetcontent' );
            if (this.serialize) {
                var node = this.serialize.parseHTML(html);
                node = this.serialize.transformOutput(node);
                html = this.serialize.toHTML(node);
            }
            return html;
        },

        /**
         * 获取编辑器内容的文本模式
         * @public
         * @function
         * @returns {String}
         */
        getContentTxt : function(){
            var reg = new RegExp( domUtils.fillChar, 'g' );
            return this.document.body[browser.ie ? 'innerText':'textContent'].replace(reg,'')
        },
        /**
         * 设置编辑内容
         * @public
         * @function
         * @param {String} html
         */
        setContent : function ( html ) {
            var me = this;
            me.fireEvent( 'beforesetcontent' );
            var serialize = this.serialize;
            if (serialize) {
                var node = serialize.parseHTML(html);
                node = serialize.transformInput(node);
                node = serialize.filter(node);
                html = serialize.toHTML(node);
            }
            this.document.body.innerHTML = html;
            //给文本或者inline节点套p标签
            if(me.options.enterTag == 'p'){
                var child = this.body.firstChild,
                    p = me.document.createElement('p'),
                    tmpNode;

                while(child){
                    if(child.nodeType ==3 || child.nodeType == 1 && dtd.p[child.tagName]){
                        tmpNode = child.nextSibling;

                        p.appendChild(child);
                        child = tmpNode;
                        if(!child){
                            me.body.appendChild(p)
                        }
                    }else{
                        if(p.firstChild){
                            me.body.insertBefore(p,child);
                            p = me.document.createElement('p')

                            
                        }
                        child = child.nextSibling
                    }


                }   
            }
            me.adjustTable && me.adjustTable(me.body);
            me.fireEvent( 'aftersetcontent' );
        },

        /**
         * 让编辑器获得焦点
         * @public
         * @function
         */
        focus : function () {
            domUtils.getWindow( this.document ).focus();
        },

        /**
         * 加载插件
         * @private
         * @function
         * @param {Array} plugins
         */
        initPlugins : function ( plugins ) {
            var fn,originals = baidu.editor.plugins;
            if ( plugins ) {
                for ( var i = 0,pi; pi = plugins[i++]; ) {
                    if ( utils.indexOf( this.options.plugins, pi ) == -1 && (fn = baidu.editor.plugins[pi]) ) {
                        this.options.plugins.push( pi );
                        fn.call( this )
                    }
                }
            } else {

                plugins = this.options.plugins;

                if ( plugins ) {
                    for ( i = 0; pi = originals[plugins[i++]]; ) {
                        pi.call( this )
                    }
                } else {
                    this.options.plugins = [];
                    for ( pi in originals ) {
                        this.options.plugins.push( pi );
                        originals[pi].call( this )
                    }
                }
            }


        },
         /**
         * 初始化事件绑定selectionchange
         * @private
         * @function
         */
        _initEvents : function () {
            var me = this,
                doc = this.document,
                win = domUtils.getWindow( doc );

            var _selectionChange = utils.defer( utils.bind( me._selectionChange, me ), 250, true );
            me._proxyDomEvent = utils.bind( me._proxyDomEvent, me );
            domUtils.on( doc, ['click', 'contextmenu', 'mousedown','keydown', 'keyup','keypress', 'mouseup', 'mouseover', 'mouseout', 'selectstart'], me._proxyDomEvent );

            domUtils.on( win, ['focus', 'blur'], me._proxyDomEvent );

            domUtils.on( doc, ['mouseup','keydown'], function(evt){
                var keyCode = evt.keyCode || evt.which;

                //特殊键不触发selectionchange
                if(evt.type == 'keydown' && (evt.ctrlKey || evt.metaKey || evt.shiftKey || evt.altKey)){
                    return;
                }

                _selectionChange()
            });

             //处理拖拽
            //ie ff不能从外边拖入
            //chrome只针对从外边拖入的内容过滤
            var innerDrag = 0,source = browser.ie ? me.body : me.document,dragoverHandler;

            domUtils.on(source,'dragstart',function(){
                innerDrag = 1;
            });

            domUtils.on(source,browser.webkit ? 'dragover' : 'drop',function(){
                return browser.webkit ?
                    function(){
                        clearTimeout( dragoverHandler );
                        dragoverHandler = setTimeout( function(){
                            if(!innerDrag){
                                var sel = me.selection,
                                    range = sel.getRange();
                                if(range){
                                    var common = range.getCommonAncestor();
                                    if(common && me.serialize){
                                        var f = me.serialize,
                                            node =
                                                f.filter(
                                                    f.transformInput(
                                                        f.parseHTML(
                                                            f.word(common.innerHTML)
                                                        )
                                                    )
                                                )
                                        common.innerHTML = f.toHTML(node)
                                    }

                                }
                            }
                            innerDrag = 0;
                        }, 200 );
                    } :
                    function(e){

                        if(!innerDrag){
                            e.preventDefault ? e.preventDefault() :(e.returnValue = false) ;

                        }
                        innerDrag = 0;
                    }

            }());

        },
        _proxyDomEvent: function ( evt ) {
            return this.fireEvent( evt.type.replace( /^on/, '' ), evt );
        },

        _selectionChange : function () {
            var me = this;
            //需要捕获ie回报错误
            try {
                //注释掉，因为在chrome下输入中文过慢时，会把第一个字母丢掉
//                var fillData = editor.fillData,
//                    reg = new RegExp( domUtils.fillChar, 'g' ),
//                    parent = fillData.parentNode;
//
//                if ( fillData && parent ) {
//
//                    if ( fillData.nodeValue.replace( reg, '' ).length ) {
//                        fillData.nodeValue = fillData.nodeValue.replace( reg, '' );
//                        editor.fillData = null;
//                    }
//
//                }
            } catch( e ) {
            }
           

            me.selection.cache();
            if ( me.selection._cachedRange && me.selection._cachedStartElement ) {
                me.fireEvent( 'beforeselectionchange' );
                me.fireEvent( 'selectionchange' );
                me.selection.clear();
            }

        },

        _callCmdFn: function ( fnName, args ) {
            var cmdName = args[0].toLowerCase(),
                cmd, cmdFn;
            cmdFn = ( cmd = this.commands[cmdName] ) && cmd[fnName] ||
                ( cmd = baidu.editor.commands[cmdName]) && cmd[fnName];
            if ( cmd && !cmdFn && fnName == 'queryCommandState' ) {
                return false;
            } else if ( cmdFn ) {
                return cmdFn.apply( this, args );
            }
        },

        /**
         * 执行编辑命令
         * @public
         * @function
         * @param {String} cmdName
         * @see docs/Commands.html
         */
        execCommand : function ( cmdName ) {

            cmdName = cmdName.toLowerCase();
            var me = this,
                result,
                cmd = me.commands[cmdName] || baidu.editor.commands[cmdName];
            if ( !cmd || !cmd.execCommand ) {
                return;
            }

            if ( !cmd.notNeedUndo && !me.__hasEnterExecCommand ) {
                me.__hasEnterExecCommand = true;
                me.fireEvent( 'beforeexeccommand', cmdName );
                result = this._callCmdFn( 'execCommand', arguments );
                me.fireEvent( 'afterexeccommand', cmdName );
                me.__hasEnterExecCommand = false;
            } else {
                result = this._callCmdFn( 'execCommand', arguments );
            }
            me._selectionChange();
            return result;
        },

        /**
         * 查询命令的状态
         * @public
         * @function
         * @param {String} cmdName
         * @returns {Number|*} -1 : disabled, false : normal, true : enabled.
         * @see docs/Commands.html
         */
        queryCommandState : function ( cmdName ) {
            return this._callCmdFn( 'queryCommandState', arguments );
        },

        /**
         * 查询命令的值
         * @public
         * @function
         * @param {String} cmdName
         * @returns {*}
         * @see docs/Commands.html
         */
        queryCommandValue : function ( cmdName ) {
            return this._callCmdFn( 'queryCommandValue', arguments );
        },
        /**
         * 检查编辑区域中是否有内容
         * @public
         * @function
         * @returns {Boolean} true 有,false 没有
         */
        hasContents : function(){
            var cont = this.body[browser.ie ? 'innerText' : 'textContent'],
                reg = new RegExp('[ \t\n\r'+domUtils.fillChar+']','g');

            return !!cont.replace(reg,'').length
        }

    };
    utils.inherits( Editor, EventBase );
})();

/**
 * b u i等基础功能实现
 * @function
 * @name baidu.editor.commands.basestyle
 * @author zhanyi
*/
(function() {
    var basestyles = {
            'bold':['strong','b'],
            'italic':['em','i'],
            //'underline':['u'],
            //'strikethrough':['strike'],
            'subscript':['sub'],
            'superscript':['sup']
        },
        domUtils = baidu.editor.dom.domUtils,
        getObj = function(editor,tagNames){
            var start = editor.selection.getStart();
            return  domUtils.findParentByTagName( start, tagNames, true )
        },
        flag = 0;
    for ( var style in basestyles ) {
        (function( cmd, tagNames ) {
            baidu.editor.commands[cmd] = {
                execCommand : function( cmdName ) {

                    var range = new baidu.editor.dom.Range(this.document),obj = '',me = this;

                    //执行了上述代码可能产生冗余的html代码，所以要注册 beforecontent去掉这些冗余的代码
                    if(!flag){
                        this.addListener('beforegetcontent',function(){
                            domUtils.clearReduent(me.document,['strong','u','em','sup','sub','strike'])
                        });
                        flag = 1;
                    }
                    //table的处理
                    if(me.currentSelectedArr && me.currentSelectedArr.length > 0){
                        for(var i=0,ci;ci=me.currentSelectedArr[i++];){
                            if(ci.style.display != 'none'){
                                range.selectNodeContents(ci).select();
                                //trace:943
                                !obj && (obj = getObj(this,tagNames));
                                if(cmdName == 'superscript' || cmdName == 'subscript'){
                                    
                                    if(!obj || obj.tagName.toLowerCase() != cmdName)
                                        range.removeInlineStyle(['sub','sup'])

                                }
                                obj ? range.removeInlineStyle( tagNames ) : range.applyInlineStyle( tagNames[0] )
                            }

                        }
                        range.selectNodeContents(me.currentSelectedArr[0]).select();
                    }else{
                        range = me.selection.getRange();
                        obj = getObj(this,tagNames);

                        if ( range.collapsed ) {
                            if ( obj ) {
                                var tmpText =  me.document.createTextNode('');
                                range.insertNode( tmpText ).removeInlineStyle( tagNames );

                                range.setStartBefore(tmpText);
                                domUtils.remove(tmpText);
                            } else {
                                
                                var tmpNode = range.document.createElement( tagNames[0] );
                                if(cmdName == 'superscript' || cmdName == 'subscript'){
                                    tmpText = me.document.createTextNode('');
                                    range.insertNode(tmpText)
                                        .removeInlineStyle(['sub','sup'])
                                        .setStartBefore(tmpText)
                                        .collapse(true);

                                }
                                range.insertNode( tmpNode ).setStart( tmpNode, 0 );
                                


                            }
                            range.collapse( true )

                        } else {
                            if(cmdName == 'superscript' || cmdName == 'subscript'){
                                if(!obj || obj.tagName.toLowerCase() != cmdName)
                                    range.removeInlineStyle(['sub','sup'])

                            }
                            obj ? range.removeInlineStyle( tagNames ) : range.applyInlineStyle( tagNames[0] )
                        }

                        range.select();
                        
                    }

                    return true;
                },
                queryCommandState : function() {
                   return getObj(this,tagNames) ? 1 : 0;
                }
            }
        })( style, basestyles[style] );

    }
//    baidu.editor.contextMenuItems.push({
//        group : '基本样式',
//        subMenu : [
//            {
//                label: '加粗',
//                cmdName : 'bold'
//            },
//            {
//                label: '加斜',
//                cmdName : 'italic'
//            },
//            {
//                label: '上标',
//                cmdName : 'superscript'
//            },
//            {
//                label: '下标',
//                cmdName : 'subscript'
//            }]
//    })
})();


/**
 * @description 插入内容
 * @author zhanyi
    */
(function(){
    var domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd,
        utils = baidu.editor.utils;
    baidu.editor.commands['inserthtml'] = {
        execCommand: function (command,html){
            var editor = this,
                range,deletedElms, i,ci,
                div,
                tds = editor.currentSelectedArr;

            range = editor.selection.getRange();

            div = range.document.createElement( 'div' );
            div.style.display = 'inline';
            div.innerHTML = utils.trim( html );

            editor.adjustTable && editor.adjustTable(div);

            if(tds && tds.length){
                for(var i=0,ti;ti=tds[i++];){
                    ti.className = ''
                }
                tds[0].innerHTML = '';
                range.setStart(tds[0],0).collapse(true);
                editor.currentSelectedArr = [];
            }

            if ( !range.collapsed ) {

                range.deleteContents();
                if(range.startContainer.nodeType == 1){
                    var child = range.startContainer.childNodes[range.startOffset],pre;
                    if(child && domUtils.isBlockElm(child) && (pre = child.previousSibling) && domUtils.isBlockElm(pre)){
                        range.setEnd(pre,pre.childNodes.length).collapse();
                        while(child.firstChild){
                            pre.appendChild(child.firstChild);

                        }
                        domUtils.remove(child);
                    }
                }

            }


            var child,parent,pre,tmp,hadBreak = 0;
            while ( child = div.firstChild ) {
                range.insertNode( child );
                if ( !hadBreak && child.nodeType == domUtils.NODE_ELEMENT && domUtils.isBlockElm( child ) ){

                    parent = domUtils.findParent( child,function ( node ){ return domUtils.isBlockElm( node ); } );
                    if ( parent && parent.tagName.toLowerCase != 'body' && !(dtd[parent.tagName][child.nodeName] && child.parentNode === parent)){
                        if(!dtd[parent.tagName][child.nodeName]){
                            pre = parent;
                        }else{
                            tmp = child.parentNode;
                            while (tmp !== parent){
                                pre = tmp;
                                tmp = tmp.parentNode;
    
                            }    
                        }
                        
                       
                        domUtils.breakParent( child, pre || tmp );
                        //去掉break后前一个多余的节点  <p>|<[p> ==> <p></p><div></div><p>|</p>
                        var pre = child.previousSibling;
                        domUtils.trimWhiteTextNode(pre);
                        if(!pre.childNodes.length){
                            domUtils.remove(pre);
                        }
                        hadBreak = 1;
                    }
                }
                var next = child.nextSibling;
                if(!div.firstChild && next && domUtils.isBlockElm(next)){

                    range.setStart(next,0).collapse(true);
                    break;
                }
                range.setEndAfter( child ).collapse();

            }
//            if(!range.startContainer.childNodes[range.startOffset] && domUtils.isBlockElm(range.startContainer)){
//                next = editor.document.createElement('br');
//                range.insertNode(next);
//                range.collapse(true);
//            }
            //block为空无法定位光标

            child = range.startContainer;
            //用chrome可能有空白展位符
            if(domUtils.isBlockElm(child) && domUtils.isEmptyNode(child)){
                child.innerHTML = baidu.editor.browser.ie ? '' : '<br/>'
            }
            //加上true因为在删除表情等时会删两次，第一次是删的fillData
            range.select(true);
            //range.scrollToView(editor.autoHeightEnabled);
            
        }
    };
}());

/**
 * @description 居左右中
 * @author zhanyi
 */
(function(){
    var domUtils = baidu.editor.dom.domUtils,
        block = domUtils.isBlockElm,
        defaultValue = {
            left : 1,
            right : 1,
            center : 1,
            justify : 1
        },
        utils = baidu.editor.utils,
        doJustify = function(range,style){
            var bookmark = range.createBookmark(),
                filterFn = function( node ) {
                    return node.nodeType == 1 ? node.tagName.toLowerCase() != 'br' &&  !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace( node )
                };

            range.enlarge(true);
            var bookmark2 = range.createBookmark(),
                current = domUtils.getNextDomNode(bookmark2.start,false,filterFn),
                tmpRange = range.cloneRange(),
                tmpNode;
            while(current &&  !(domUtils.getPosition(current,bookmark2.end)&domUtils.POSITION_FOLLOWING)){
                if(current.nodeType == 3 || !block(current)){
                    tmpRange.setStartBefore(current);
                    while(current && current!==bookmark2.end &&  !block(current)){
                        tmpNode = current;
                        current = domUtils.getNextDomNode(current,false,null,function(node){
                            return !block(node)
                        });
                    }
                    tmpRange.setEndAfter(tmpNode);
                    var common = tmpRange.getCommonAncestor();
                    if( !domUtils.isBody(common) && block(common)){
                        if(!utils.isString(style)){
                            for(var pro in style){
                                common.style[pro] = style[pro];
                            }
                        }else{
                            common.style.textAlign = style;
                        }

                        current = common;
                    }else{
                        var p = range.document.createElement('p');
                        if(!utils.isString(style)){
                            for(var pro in style){
                                p.style[pro] = style[pro];
                            }
                        }else{
                            p.style.textAlign = style;
                        }
                       
                        var frag = tmpRange.extractContents();
                        p.appendChild(frag);
                        tmpRange.insertNode(p);
                        current = p;
                    }
                    current = domUtils.getNextDomNode(current,false,filterFn);
                }else{
                    current = domUtils.getNextDomNode(current,true,filterFn);
                }
            }
            return range.moveToBookmark(bookmark2).moveToBookmark(bookmark)
        };
    baidu.editor.commands['justify'] =  {
        execCommand : function( cmdName,align ) {
            
            var range = new baidu.editor.dom.Range(this.document);
            if(this.currentSelectedArr && this.currentSelectedArr.length > 0){
                for(var i=0,ti;ti=this.currentSelectedArr[i++];){
                    doJustify(range.selectNode(ti),align);
                }
                range.selectNode(this.currentSelectedArr[0]).select()
            }else{
                range = this.selection.getRange();
                //闭合时单独处理
                if(range.collapsed){
                    var txt = this.document.createTextNode('p');
                    range.insertNode(txt);
                }
                doJustify(range,align);
                if(txt){
                    range.setStartBefore(txt).collapse(true);
                    domUtils.remove(txt);
                }

                range.select();

            }
            return true;
        },
        queryCommandValue : function() {
            var startNode = this.selection.getStart(),
                value = domUtils.getComputedStyle(startNode,'text-align');
            return defaultValue[value] ? value : 'left';
        }
    }

//    baidu.editor.contextMenuItems.push({
//        group : '段落格式',
//        subMenu : [
//            {
//                label: '居左',
//                cmdName : 'justify',
//                value : 'left'
//            },
//           {
//                label: '居右',
//                cmdName : 'justify',
//                value : 'right'
//            },{
//                label: '居中',
//                cmdName : 'justify',
//                value : 'center'
//            },{
//                label: '两端对齐',
//                cmdName : 'justify',
//                value : 'justify'
//            }
//        ]
//    });

})();

/**
 * @description 字体
 * @author zhanyi
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        fonts = {
            'forecolor':'color',
            'backcolor':'background-color',
            'fontsize':'font-size',
            'fontfamily':'font-family',
           
            'underline':'text-decoration',
            'strikethrough':'text-decoration'
        },
        reg = new RegExp(domUtils.fillChar,'g'),
        browser = baidu.editor.browser,
        flag = 0;

    for ( var p in fonts ) {
        (function( cmd, style ) {
            baidu.editor.commands[cmd] = {
                execCommand : function( cmdName, value ) {
                    value = value || (this.queryCommandState(cmdName) ? 'none' : cmdName == 'underline' ? 'underline' : 'line-through');
                    var me = this,
                        range = this.selection.getRange(),
                        text;
                    //执行了上述代码可能产生冗余的html代码，所以要注册 beforecontent去掉这些冗余的代码
                    if(!flag){
                        me.addListener('beforegetcontent',function(){
                            domUtils.clearReduent(me.document,['span'])
                        });
                        flag = 1;
                    }
                    if ( value == 'default' ) {

                        if(range.collapsed){
                            text = me.document.createTextNode('font');
                            range.insertNode(text).select()

                        }
                        me.execCommand( 'removeFormat', 'span,a', style);
                        if(text){
                            range.setStartBefore(text).setCursor();
                            domUtils.remove(text)
                        }


                    } else {
                        if(me.currentSelectedArr && me.currentSelectedArr.length > 0){
                            for(var i=0,ci;ci=me.currentSelectedArr[i++];){
                                range.selectNodeContents(ci);
                                range.applyInlineStyle( 'span', {'style':style + ':' + value} );

                            }
                            range.selectNodeContents(this.currentSelectedArr[0]).select();
                        }else{
                            if ( !range.collapsed ) {
                                if((cmd == 'underline'||cmd=='strikethrough') && me.queryCommandValue(cmd)){
                                     me.execCommand( 'removeFormat', 'span,a', style );
                                }
                                range = me.selection.getRange();
                                range.applyInlineStyle( 'span', {'style':style + ':' + value} ).select();
                            } else {
                                
                                var span = domUtils.findParentByTagName(range.startContainer,'span',true);
                                text = me.document.createTextNode('font');
                                if(span && !span.children.length && !span[browser.ie ? 'innerText':'textContent'].replace(reg,'').length){
                                    //for ie hack when enter
                                    range.insertNode(text);
                                     if(cmd == 'underline'||cmd=='strikethrough'){
                                         range.selectNode(text).select();
                                         me.execCommand( 'removeFormat','span,a', style, null );

                                         span = domUtils.findParentByTagName(text,'span',true);
                                         range.setStartBefore(text)

                                    }
                                    span.style.cssText = span.style.cssText +  ';' + style + ':' + value;
                                    range.collapse(true).select();


                                }else{


                                    range.insertNode(text);
                                    range.selectNode(text).select();
                                        span = range.document.createElement( 'span' );
                                     if(cmd == 'underline'||cmd=='strikethrough'){
                                         me.execCommand( 'removeFormat','span,a', style );
                                    }

                                    span.style.cssText = style + ':' + value;


                                    text.parentNode.insertBefore(span,text);

                                    range.setStart(span,0).setCursor();
                                    //trace:981
                                    //domUtils.mergToParent(span)


                                }
                                domUtils.remove(text)
                            }
                        }

                    }
                    return true;
                },
                queryCommandValue : function (cmdName) {
                    var startNode = this.selection.getStart();
                    //trace:946
                    if(cmdName == 'underline'||cmdName=='strikethrough' ){
                        var tmpNode = startNode,value;
                        while(tmpNode && !domUtils.isBlockElm(tmpNode) && !domUtils.isBody(tmpNode)){
                            value = domUtils.getComputedStyle( tmpNode, style );

                            if(value != 'none'){
                                return value;
                            }
                            tmpNode = tmpNode.parentNode;
                        }
                        return 'none'
                    }
                    return  domUtils.getComputedStyle( startNode, style );
                },
                queryCommandState : function(cmdName){
                    if(!(cmdName == 'underline'||cmdName=='strikethrough'))
                        return 0;
                    return this.queryCommandValue(cmdName) == (cmdName == 'underline' ? 'underline' : 'line-through')
                }
            }
        })( p, fonts[p] );
    }
//    baidu.editor.contextMenuItems.push({
//        group : '字体样式',
//        subMenu : [
//            {
//                label: '下划线',
//                cmdName : 'underline'
//            },
//            {
//                label: '删除线',
//                cmdName : 'strikethrough'
//            }]
//    })
})();
/**
 * @description link
 * @author zhanyi
 */
(function() {
    var dom = baidu.editor.dom,
        domUtils = dom.domUtils,
        browser = baidu.editor.browser;

    function optimize( range ) {
        var start = range.startContainer,end = range.endContainer;

        if ( start = domUtils.findParentByTagName( start, 'a', true ) ) {
            range.setStartBefore( start )
        }
        if ( end = domUtils.findParentByTagName( end, 'a', true ) ) {
            range.setEndAfter( end )
        }
    }

    baidu.editor.commands['unlink'] = {
        execCommand : function() {
            var as,
                range = new baidu.editor.dom.Range(this.document),
                tds = this.currentSelectedArr,
                bookmark;
            if(tds && tds.length >0){
                for(var i=0,ti;ti=tds[i++];){
                    as = domUtils.getElementsByTagName(ti,'a');
                    for(var j=0,aj;aj=as[j++];){
                        domUtils.remove(aj,true);
                    }
                }
                if(domUtils.isEmptyNode(tds[0])){
                    range.setStart(tds[0],0).setCursor();
                }else{
                    range.selectNodeContents(tds[0]).select()
                }
            }else{
                range = this.selection.getRange();
                if(range.collapsed && !domUtils.findParentByTagName( range.startContainer, 'a', true )){
                    return;
                }
                bookmark = range.createBookmark();
                optimize( range );
                range.removeInlineStyle( 'a' ).moveToBookmark( bookmark ).select();
            }
        },
        queryCommandState : function(){
            return this.queryCommandValue('link') ?  0 : -1;
        }

    };
    function doLink(range,opt){

        optimize( range = range.adjustmentBoundary() );
        var start = range.startContainer;
        if(start.nodeType == 1){
            start = start.childNodes[range.startOffset];
            if(start && start.nodeType == 1 && start.tagName == 'A' && /^(?:https?|ftp|file)\s*:\s*\/\//.test(start[browser.ie?'innerText':'textContent'])){
                start.innerHTML = opt.href;
            }
        }
        range.removeInlineStyle( 'a' );
        if ( range.collapsed ) {
            var a = range.document.createElement( 'a' );
            domUtils.setAttributes( a, opt );
            a.innerHTML = opt.href;
            range.insertNode( a ).selectNode( a );
        } else {
            range.applyInlineStyle( 'a', opt )
        }
    }
    baidu.editor.commands['link'] = {
        execCommand : function( cmdName, opt ) {
            var range = new baidu.editor.dom.Range(this.document),
                tds = this.currentSelectedArr;
            
            if(tds && tds.length){
                for(var i=0,ti;ti=tds[i++];){
                    if(domUtils.isEmptyNode(ti)){
                        ti.innerHTML = opt.href
                    }
                    doLink(range.selectNodeContents(ti),opt)
                }
                if(domUtils.isEmptyNode(tds[0])){
                    range.setStart(tds[0],0).setCursor();
                }else{
                    range.selectNodeContents(tds[0]).select()
                }

               
            }else{
                doLink(range=this.selection.getRange(),opt);

                range.collapse().select();

            }
        },
        queryCommandValue : function() {
            
            var range = new baidu.editor.dom.Range(this.document),
                tds = this.currentSelectedArr,
                as,
                node;
            if(tds && tds.length){
                for(var i=0,ti;ti=tds[i++];){
                    as = ti.getElementsByTagName('a');
                    if(as[0])
                        return as[0]
                }
            }else{
                range = this.selection.getRange();



                if ( range.collapsed ) {
                    node = this.selection.getStart();
                    if ( node && (node = domUtils.findParentByTagName( node, 'a', true )) ) {
                        return node;
                    }
                } else {
                    var start = range.startContainer.nodeType  == 3 || !range.startContainer.childNodes[range.startOffset] ? range.startContainer : range.startContainer.childNodes[range.startOffset],
                        end =  range.endContainer.nodeType == 3 || range.endOffset == 0 ? range.endContainer : range.endContainer.childNodes[range.endOffset-1],

                        common = range.getCommonAncestor();


                    node = domUtils.findParentByTagName( common, 'a', true );
                    if ( !node && common.nodeType == 1){

                        var as = common.getElementsByTagName( 'a' ),
                            ps,pe;

                        for ( var i = 0,ci; ci = as[i++]; ) {
                            ps = domUtils.getPosition( ci, start ),pe = domUtils.getPosition( ci,end);
                            if ( (ps & domUtils.POSITION_FOLLOWING || ps & domUtils.POSITION_CONTAINS)
                                &&
                                (pe & domUtils.POSITION_PRECEDING || pe & domUtils.POSITION_CONTAINS)
                                ) {
                                node = ci;
                                break;
                            }
                        }
                    }

                    return node;
                }
            }


        }
    };

//    baidu.editor.contextMenuItems.push({
//        label : '删除链接',
//        cmdName : 'unlink'
//    })
})();

/**
 * @description 清除样式
 * @author zhanyi
 */
(function() {

    var domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd;
    baidu.editor.commands['removeformat'] = {
        execCommand : function( cmdName, tags, style, attrs,notIncludeA ) {
            var tagReg = new RegExp( '^(?:' + (tags || this.options.removeFormatTags).replace( /,/g, '|' ) + ')$', 'i' ) ,
                removeFormatAttributes = style ? [] : (attrs || this.options.removeFormatAttributes).split( ',' ),
                range = new baidu.editor.dom.Range( this.document ),
                bookmark,node,parent,
                filter = function( node ) {
                    return node.nodeType == 1;
                };

            function doRemove( range ) {
                
                var bookmark1 = range.createBookmark();
                if ( range.collapsed ) {
                    range.enlarge( true );
                }

             //不能把a标签切了
                if(!notIncludeA){
                    var aNode = domUtils.findParentByTagName(range.startContainer,'a',true);
                    if(aNode){
                        range.setStartBefore(aNode)
                    }

                    aNode = domUtils.findParentByTagName(range.endContainer,'a',true);
                    if(aNode){
                        range.setEndAfter(aNode)
                    }

                }
              
                
                bookmark = range.createBookmark();

                node = bookmark.start;
                //切开始
                while ( (parent = node.parentNode) && !domUtils.isBlockElm( parent ) ) {
                    domUtils.breakParent( node, parent );
                    domUtils.clearEmptySibling( node );
                }
                if ( bookmark.end ) {
                    //切结束
                    node = bookmark.end;
                    while ( (parent = node.parentNode) && !domUtils.isBlockElm( parent ) ) {
                        domUtils.breakParent( node, parent );
                        domUtils.clearEmptySibling( node );
                    }

                    //开始去除样式
                    var current = domUtils.getNextDomNode( bookmark.start, false, filter ),
                        next;
                    while ( current ) {
                        if ( current == bookmark.end ) {
                            break;
                        }

                        next = domUtils.getNextDomNode( current, true, filter );

                        if ( !dtd.$empty[current.tagName.toLowerCase()] && !domUtils.isBookmarkNode( current ) ) {
                            if ( tagReg.test( current.tagName ) ) {
                                if ( style ) {
                                    domUtils.removeStyle( current, style );
                                    if ( domUtils.isRedundantSpan( current ) && style != 'text-decoration')
                                        domUtils.remove( current, true );
                                } else {
                                    current.tagName != 'A' && domUtils.remove( current, true )
                                }
                            } else {

                                if(!dtd.$tableContent[current.tagName]){
                                    domUtils.removeAttributes( current, removeFormatAttributes );
                                    if ( domUtils.isRedundantSpan( current ) )
                                        domUtils.remove( current, true );
                                }else{

                                }


                            }
                        }
                        current = next;
                    }
                }
                //trace:1035
                if(domUtils.isBlockElm(bookmark.start.parentNode)){
                    domUtils.removeAttributes(  bookmark.start.parentNode,removeFormatAttributes );
                }
                if(bookmark.end && domUtils.isBlockElm(bookmark.end.parentNode)){
                    domUtils.removeAttributes(  bookmark.end.parentNode,removeFormatAttributes );
                }
                range.moveToBookmark( bookmark ).moveToBookmark(bookmark1);
                //清除冗余的代码 <b><bookmark></b>
                var node = range.startContainer,
                    tmp,
                    collapsed = range.collapsed;
                while(node.nodeType == 1 && node.childNodes.length == 0 && dtd.$removeEmpty[node.tagName]){
                    tmp = node.parentNode;
                    range.setStartBefore(node);
                    //trace:937
                    //更新结束边界
                    if(range.startContainer === range.endContainer){
                        range.endOffset--;
                    }
                    domUtils.remove(node);
                    node = tmp;
                }
             
                if(!collapsed){
                    node = range.endContainer;
                    while(node.nodeType == 1 && node.childNodes.length == 0 && dtd.$removeEmpty[node.tagName]){
                        tmp = node.parentNode;
                        range.setEndBefore(node);
                        domUtils.remove(node);

                        node = tmp;
                    }


                }
            }

            if ( this.currentSelectedArr && this.currentSelectedArr.length ) {
                for ( var i = 0,ci; ci = this.currentSelectedArr[i++]; ) {
                    range.selectNodeContents( ci );
                    doRemove( range );
                }
                range.selectNodeContents( this.currentSelectedArr[0] ).select();
            } else {
                
                range = this.selection.getRange();
                doRemove( range );
                range.select();
            }
        }

    }
//    baidu.editor.contextMenuItems.push({
//        label : '清除样式',
//        cmdName : 'removeformat'
//    })
})();
/**
 * 
 * 引用模块实现
 * @function
 * @name baidu.editor.commands.blockquate
 * @author zhanyi
 */
(function() {

    var domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd,
        getObj = function(editor){
            var startNode = editor.selection.getStart();
            return domUtils.findParentByTagName( startNode, 'blockquote', true )
        };

    baidu.editor.commands['blockquote'] = {
        execCommand : function( cmdName, attrs ) {
          
            var range = this.selection.getRange(),
                obj = getObj(this),
                blockquote = dtd.blockquote,
                bookmark = range.createBookmark(),
                tds = this.currentSelectedArr;


            if ( obj ) {
                if(tds && tds.length){
                    domUtils.remove(obj,true)
                }else{
                    var start = range.startContainer,
                        startBlock = domUtils.isBlockElm(start) ? start : domUtils.findParent(start,function(node){return domUtils.isBlockElm(node)}),

                        end = range.endContainer,
                        endBlock = domUtils.isBlockElm(end) ? end :  domUtils.findParent(end,function(node){return domUtils.isBlockElm(node)});


                    if(startBlock.tagName == 'LI' || startBlock.tagName == 'TD' || startBlock === obj){
                        domUtils.remove(obj,true)
                    }else{
                        domUtils.breakParent(startBlock,obj);
                    }

                    if(startBlock !== endBlock){
                        obj = domUtils.findParentByTagName(endBlock,'blockquote');
                        if(obj){
                            if(endBlock.tagName == 'LI' || endBlock.tagName == 'TD'){
                                domUtils.remove(obj,true)
                            }else{
                                 domUtils.breakParent(endBlock,obj);
                            }
    
                        }
                    }

                    var blockquotes = domUtils.getElementsByTagName(this.document,'blockquote');
                    for(var i=0,bi;bi=blockquotes[i++];){
                        if(!bi.childNodes.length){
                            domUtils.remove(bi)
                        }else if(domUtils.getPosition(bi,startBlock)&domUtils.POSITION_FOLLOWING && domUtils.getPosition(bi,endBlock)&domUtils.POSITION_PRECEDING){
                            domUtils.remove(bi,true)
                        }
                    }
                }



            } else {

                var tmpRange = range.cloneRange(),
                    node = tmpRange.startContainer.nodeType == 1 ? tmpRange.startContainer : tmpRange.startContainer.parentNode,
                    preNode = node,
                    doEnd = 1;

                //调整开始
                while ( 1 ) {
                    if ( domUtils.isBody(node) ) {
                        if ( preNode !== node ) {
                            if ( range.collapsed ) {
                                tmpRange.selectNode( preNode );
                                doEnd = 0;
                            } else {
                                tmpRange.setStartBefore( preNode );
                            }
                        }else{
                            tmpRange.setStart(node,0)
                        }

                        break;
                    }
                    if ( !blockquote[node.tagName] ) {
                        if ( range.collapsed ) {
                            tmpRange.selectNode( preNode )
                        } else
                            tmpRange.setStartBefore( preNode);
                        break;
                    }

                    preNode = node;
                    node = node.parentNode;
                }
                
                //调整结束
               if ( doEnd ) {
                    preNode = node =  node = tmpRange.endContainer.nodeType == 1 ? tmpRange.endContainer : tmpRange.endContainer.parentNode;
                    while ( 1 ) {

                        if ( domUtils.isBody( node ) ) {
                            if ( preNode !== node ) {

                                    tmpRange.setEndAfter( preNode );
                                
                            } else {
                                tmpRange.setEnd( node, node.childNodes.length )
                            }

                            break;
                        }
                        if ( !blockquote[node.tagName] ) {
                            tmpRange.setEndAfter( preNode );
                            break;
                        }

                        preNode = node;
                        node = node.parentNode;
                    }

                }


                node = range.document.createElement( 'blockquote' );
                domUtils.setAttributes( node, attrs );
                node.appendChild( tmpRange.extractContents() );
                tmpRange.insertNode( node );
                //去除重复的
                var childs = domUtils.getElementsByTagName(node,'blockquote');
                for(var i=0,ci;ci=childs[i++];){
                    if(ci.parentNode){
                        domUtils.remove(ci,true)
                    }
                }

            }
            range.moveToBookmark( bookmark ).select()
        },
        queryCommandState : function() {
            return getObj(this) ? 1 : 0;
        }
    };
//    baidu.editor.contextMenuItems.push({
//        label : '引用',
//        cmdName : 'blockquote'
//    })

})();


/**
 * @description 列表
 * @author zhanyi
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        browser = baidu.editor.browser,
        webkit = browser.webkit,
        gecko = browser.gecko;
    baidu.editor.commands['insertorderedlist'] = baidu.editor.commands['insertunorderedlist'] = {
        execCommand : function( command, style ) {

            var me = this,
                parent = domUtils.findParentByTagName( this.selection.getStart(), command.toLowerCase() == 'insertorderedlist' ? 'ol' : 'ul', true ),
                doc = this.document,
                range,flag=1;
            if(browser.ie){
                var start = me.selection.getStart(),
                    blockquote = domUtils.findParent(start,function(node){return node.tagName == 'BLOCKQUOTE'}),
                    hasBlockquote = 0;
                if(blockquote)
                    hasBlockquote = 1;
            }

            style = style && style.toLowerCase() || (command == 'insertorderedlist' ? 'decimal' : 'disc');

            range = me.selection.getRange();

            if(parent && !range.collapsed){
                var eParent = domUtils.findParentByTagName(range.endContainer,command.toLowerCase() == 'insertorderedlist' ? 'ol' : 'ul',true);
                if(!eParent){
                    flag = 0
                }
            }
            

            doc.execCommand( command, false, null );

            if( parent && domUtils.getStyle( parent, 'list-style-type' ) != style && flag){

                doc.execCommand( command, false, null );
            }
            parent = domUtils.findParentByTagName( this.selection.getStart(), command.toLowerCase() == 'insertorderedlist' ? 'ol' : 'ul', true );
            if ( parent ) {
                
               
                if ( webkit ) {
                    var lis = parent.getElementsByTagName( 'li' );
//                    for ( var i = 0,ci; ci = lis[i++]; ) {
//                        ci = ci.lastChild;
//                        if ( ci.nodeType == 1 && ci.tagName.toLowerCase() == 'br' )
//                            domUtils.remove( ci );
//                    }

                    if ( parent.parentNode.tagName.toLowerCase() == 'p' ) {
                        range = this.selection.getRange();
                        var bookmark = range.createBookmark();
                        domUtils.remove( parent.parentNode, true );
                        range.moveToBookmark(bookmark).select()

                    }
                }
                var pre = parent.previousSibling;

                if(pre && pre.nodeType == 1 && pre.tagName == parent.tagName &&
                    style == domUtils.getStyle( pre, 'list-style-type' )){
                    range = me.selection.getRange();
                    var bookmark = range.createBookmark();
                    while(parent.firstChild){
                        pre.appendChild(parent.firstChild)
                    }
                    domUtils.remove(parent);
                    range.moveToBookmark(bookmark).select();
                    return 1;
                }

                if ( gecko ) {
                    parent.removeAttribute( '_moz_dirty' );
                    var nodes = parent.getElementsByTagName( '*' );
                    for ( var i = 0,ci; ci = nodes[i++]; ) {
                        ci.removeAttribute( '_moz_dirty' );
                    }
                }
                parent.style.listStyleType = style;
                if(browser.ie && hasBlockquote && !domUtils.findParent(parent,function(node){return node.tagName == 'BLOCKQUOTE'})){
                    var pp = domUtils.findParent(parent,function(node){return node.tagName == command.toLowerCase() == 'insertorderedlist' ? 'OL' : 'UL'});
                    if(pp){
                        blockquote.innerHTML = '';
                        while(pp.firstChild){
                            blockquote.appendChild(pp.firstChild)
                        }
                        pp.parentNode.insertBefore(blockquote,pp);
                        domUtils.remove(pp)
                    }
                }
                //修正chrome下h1套ol/ul导致标号看不到
                if(browser.webkit){
                    var h = domUtils.findParentByTagName(parent,[ 'h1', 'h2', 'h3', 'h4', 'h5', 'h6']);
                    if(h){
                        range = me.selection.getRange();
                        var bk = range.createBookmark(),
                            lis = domUtils.getElementsByTagName(parent,'li');
                        for(var i=0,li;li=lis[i++];){
                            var tmp = h.cloneNode(false);
                            while(li.firstChild){
                                tmp.appendChild(li.firstChild);
                            }
                            li.appendChild(tmp)
                        }
                        domUtils.remove(h,true);
                        range.moveToBookmark(bk).select()
                    }
                }
            }


        },
        queryCommandState : function( command ) {

            var startNode = this.selection.getStart();
           
            return domUtils.findParentByTagName( startNode, command.toLowerCase() == 'insertorderedlist' ? 'ol' : 'ul', true ) ? 1 : 0;
        },
        queryCommandValue : function( command ) {

            var startNode = this.selection.getStart(),
                node = domUtils.findParentByTagName( startNode, command.toLowerCase() == 'insertorderedlist' ? 'ol' : 'ul', true );
          
            return node ? domUtils.getStyle( node, 'list-style-type' ) : null;
        }
    }
//    baidu.editor.contextMenuItems.push({
//        group : '列表',
//        subMenu : [
//            {
//                label: '有序列表',
//                cmdName : 'insertorderedlist',
//                value : 'decimal'
//            },
//            {
//                label: '无序列表',
//                cmdName : 'insertunorderedlist',
//                value : 'disc'
//            }
//        ]
//    });

})();

/**
 * 首行缩进
 * @function
 * @name baidu.editor.commands.indent
 * @author zhanyi
 */
(function (){
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands['outdent'] = baidu.editor.commands['indent'] = {
        execCommand : function(cmd) {
             var me = this,
                 value = cmd.toLowerCase() == 'outdent' ? '0em' : (me.options.indentValue || '2em');
             me.execCommand('Paragraph','p',{'textIndent':value});
        }

    };
//    baidu.editor.contextMenuItems.push('-',{
//        label : '首行缩进',
//        cmdName : 'indent'
//    },{
//        label : '取消缩进',
//        cmdName : 'outdent'
//    },'-')
})();

/**
 * @description 打印
 * @author zhanyi
 */
(function() {
    baidu.editor.commands['print'] = {
        execCommand : function(){
            this.window.print();
        },
        notNeedUndo : 1
    }
//    baidu.editor.contextMenuItems.push({
//        label : '打印',
//        cmdName : 'print'
//    })
})();


baidu.editor.commands['preview'] = {
    execCommand : function(){
        var me = this;
        var w = window.open('', '_blank', "");
        var d = w.document;
        d.open();
        d.write('<html><head><title></title></head><body>' +
            me.getContent() +
            '</body></html>');
        d.close();
    },
    notNeedUndo : 1
};
//baidu.editor.contextMenuItems.push({
//    label : '预览',
//    cmdName : 'preview'
//});

/**
 * 选中所有
 * @function
 * @name execCommand
 * @author zhanyi
*/
(function() {
    var browser = baidu.editor.browser;
    baidu.editor.commands['selectall'] = {
        execCommand : function(){
            this.document.execCommand('selectAll',false,null);
            //trace 1081
            !browser.ie && this.focus();
        },
        notNeedUndo : 1
    }
//    baidu.editor.contextMenuItems.push({
//        label : '全选',
//        cmdName : 'selectall'
//    })
})();


/**
 * @description 段落样式
 * @author zhanyi
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        block = domUtils.isBlockElm,
        notExchange = ['TD','LI','PRE'],
        utils = baidu.editor.utils,
        browser = baidu.editor.browser,
        
        doParagraph = function(range,style,attrs){
            var bookmark = range.createBookmark(),
                filterFn = function( node ) {
                    return   node.nodeType == 1 ? node.tagName.toLowerCase() != 'br' &&  !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace( node )
                },
                para;

            range.enlarge( true );
            var bookmark2 = range.createBookmark(),
                current = domUtils.getNextDomNode( bookmark2.start, false, filterFn ),
                tmpRange = range.cloneRange(),
                tmpNode;
            while ( current && !(domUtils.getPosition( current, bookmark2.end ) & domUtils.POSITION_FOLLOWING) ) {
                if ( current.nodeType == 3 || !block( current ) ) {
                    tmpRange.setStartBefore( current );
                    while ( current && current !== bookmark2.end && !block( current ) ) {
                        tmpNode = current;
                        current = domUtils.getNextDomNode( current, false, null, function( node ) {
                            return !block( node )
                        } );
                    }
                    tmpRange.setEndAfter( tmpNode );
                    
                    para = range.document.createElement( style );
                    if(attrs){
                        for(var pro in attrs){
                            para.style[pro] = attrs[pro];
                        }
                    }
                    para.appendChild( tmpRange.extractContents() );

                    tmpRange.insertNode( para );
                    var parent = para.parentNode;
                    //如果para上一级是一个block元素且不是body,td就删除它
                    if ( block( parent ) && !domUtils.isBody( para.parentNode ) && utils.indexOf(notExchange,parent.tagName)==-1) {
                        //存储dir,style

                        parent.getAttribute('dir') && para.setAttribute('dir',parent.getAttribute('dir'));
                        //trace:1070
                        parent.style.cssText && (para.style.cssText = parent.style.cssText + ';' + para.style.cssText);
                        //trace:1030
                        parent.style.textAlign && !para.style.textAlign && (para.style.textAlign = parent.style.textAlign);
                        parent.style.textIndent && !para.style.textIndent && (para.style.textIndent = parent.style.textIndent);
                        parent.style.padding && !para.style.padding && (para.style.padding = parent.style.padding);
                        if(attrs && /h\d/i.test(parent.tagName)){
                           for(var pro in attrs){
                                parent.style[pro] = attrs[pro];
                           }
                            domUtils.remove(para,true);
                            para = parent;
                        }else
                            domUtils.remove( para.parentNode, true );

                    }
                    if(  utils.indexOf(notExchange,parent.tagName)!=-1){
                        current = parent;
                    }else{
                       current = para;
                    }


                    current = domUtils.getNextDomNode( current, false, filterFn );
                } else {
                    current = domUtils.getNextDomNode( current, true, filterFn );
                }
            }
            return range.moveToBookmark( bookmark2 ).moveToBookmark( bookmark );
        };

    baidu.editor.commands['paragraph'] = {
        execCommand : function( cmdName, style,attrs ) {
            var range = new baidu.editor.dom.Range(this.document);
            if(this.currentSelectedArr && this.currentSelectedArr.length > 0){
                for(var i=0,ti;ti=this.currentSelectedArr[i++];){
                    doParagraph(range.selectNode(ti),style,attrs);
                }
                range.selectNode(this.currentSelectedArr[0]).select()
            }else{
                range = this.selection.getRange();
                 //闭合时单独处理
                if(range.collapsed){
                    var txt = this.document.createTextNode('p');
                    range.insertNode(txt);
                }
                range = doParagraph(range,style,attrs)
                if(txt){
                    range.setStartBefore(txt).collapse(true);
                    domUtils.remove(txt);
                }

                if(browser.gecko && range.collapsed && range.startContainer.nodeType == 1){
                    var child = range.startContainer.childNodes[range.startOffset];
                    if(child && child.nodeType == 1 && child.tagName.toLowerCase() == style){
                        range.setStart(child,0).collapse(true)
                    }
                }
                range.select(true)

            }
            return true;
        },
        queryCommandValue : function() {
            var startNode = this.selection.getStart(),
                parent =  domUtils.findParentByTagName( startNode, ['p','h1','h2','h3','h4','h5','h6'], true );

            return  parent && parent.tagName.toLowerCase();
        }
    }


})();

/**
 * 输入的方向
 * @function
 * @name baidu.editor.commands.execCommand
 * @author zhanyi
 */
(function() {

    var domUtils = baidu.editor.dom.domUtils,
        block = domUtils.isBlockElm ,
        getObj = function(editor){
            var startNode = editor.selection.getStart(),
                parents;
            if ( startNode ) {
                //查找所有的是block的父亲节点
                parents = domUtils.findParents( startNode, true, block, true );
                for ( var i = 0,ci; ci = parents[i++]; ) {
                    if ( ci.getAttribute( 'dir' ) ) {
                        return ci;
                    }

                }
            }
        },
        doDirectionality = function(range,editor,forward){
            
            var bookmark,
                filterFn = function( node ) {
                    return   node.nodeType == 1 ? !domUtils.isBookmarkNode(node) : !domUtils.isWhitespace(node)
                },

                obj = getObj( editor );

            if ( obj && range.collapsed ) {
                obj.setAttribute( 'dir', forward );
                return range;
            }
            bookmark = range.createBookmark();
            range.enlarge( true );
            var bookmark2 = range.createBookmark(),
                current = domUtils.getNextDomNode( bookmark2.start, false, filterFn ),
                tmpRange = range.cloneRange(),
                tmpNode;
            while ( current &&  !(domUtils.getPosition( current, bookmark2.end ) & domUtils.POSITION_FOLLOWING) ) {
                if ( current.nodeType == 3 || !block( current ) ) {
                    tmpRange.setStartBefore( current );
                    while ( current && current !== bookmark2.end && !block( current ) ) {
                        tmpNode = current;
                        current = domUtils.getNextDomNode( current, false, null, function( node ) {
                            return !block( node )
                        } );
                    }
                    tmpRange.setEndAfter( tmpNode );
                    var common = tmpRange.getCommonAncestor();
                    if ( !domUtils.isBody( common ) && block( common ) ) {
                        //遍历到了block节点
                        common.setAttribute( 'dir', forward );
                        current = common;
                    } else {
                        //没有遍历到，添加一个block节点
                        var p = range.document.createElement( 'p' );
                        p.setAttribute( 'dir', forward );
                        var frag = tmpRange.extractContents();
                        p.appendChild( frag );
                        tmpRange.insertNode( p );
                        current = p;
                    }

                    current = domUtils.getNextDomNode( current, false, filterFn );
                } else {
                    current = domUtils.getNextDomNode( current, true, filterFn );
                }
            }
            return range.moveToBookmark( bookmark2 ).moveToBookmark( bookmark );
        };


    baidu.editor.commands['directionality'] = {
        execCommand : function( cmdName,forward ) {
            var range = new baidu.editor.dom.Range(this.document);
            if(this.currentSelectedArr && this.currentSelectedArr.length > 0){
                for(var i=0,ti;ti=this.currentSelectedArr[i++];){
                    if(ti.style.display != 'none')
                        doDirectionality(range.selectNode(ti),this,forward);
                }
                range.selectNode(this.currentSelectedArr[0]).select()
            }else{
                range = this.selection.getRange();
                //闭合时单独处理
                if(range.collapsed){
                    var txt = this.document.createTextNode('d');
                    range.insertNode(txt);
                }
                doDirectionality(range,this,forward);
                if(txt){
                    range.setStartBefore(txt).collapse(true);
                    domUtils.remove(txt);
                }

                range.select();
                

            }
            return true;
        },
        queryCommandValue : function() {
            var node = getObj(this);
            return node ? node.getAttribute('dir') : 'ltr'
        }
    }
})();


/*
 ** @description 分割线
 * @author zhanyi
 */
(function (){
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands['horizontal'] = {
        execCommand : function( cmdName ) {
            if(this.queryCommandState(cmdName)!==-1){
                this.execCommand('insertHtml','<hr>');
                var range = this.selection.getRange(),
                    start = range.startContainer;
                if(start.nodeType == 1 && !start.childNodes[range.startOffset] ){

                    var tmp;
                    if(tmp = start.childNodes[range.startOffset - 1]){
                        if(tmp.nodeType == 1 && tmp.tagName == 'HR'){
                            if(this.options.enterTag == 'p'){
                                tmp = this.document.createElement('p');
                                range.insertNode(tmp);
                                range.setStart(tmp,0).setCursor();
        
                            }else{
                                tmp = this.document.createElement('br');
                                range.insertNode(tmp);
                                range.setStartBefore(tmp).setCursor();
                            }
                        }
                    }

                }
                return true;
            }

        },
        //边界在table里不能加分隔线
        queryCommandState : function() {
            var range = this.selection.getRange();
            return domUtils.findParentByTagName(range.startContainer,'table') ||
                domUtils.findParentByTagName(range.endContainer,'table') ? -1 : 0;
        }
    };
//    baidu.editor.contextMenuItems.push({
//        label : '插入分割线',
//        cmdName : 'horizontal'
//    })
})();

/**
 * Created by .
 * User: zhuwenxuan
 * Date: 11-5-30
 * Time: 上午11:02
 */


baidu.editor.commands['time'] = {
    execCommand : function() {
        var date = new Date,
            min = date.getMinutes(),
            sec = date.getSeconds(),
            arr = [date.getHours(),min<10 ? "0"+min : min,sec<10 ? "0"+sec : sec];
        this.execCommand('insertHtml',arr.join(":"));
        return true;
    }
};
baidu.editor.commands['date'] = {
    execCommand : function() {
        var date = new Date,
            month = date.getMonth()+1,
            day = date.getDate(),
            arr = [date.getFullYear(),month<10 ? "0"+month : month,day<10?"0"+day:day];
        this.execCommand('insertHtml',arr.join("-"));
        return true;
    }
};
//baidu.editor.contextMenuItems.push({
//    group : '插入时间',
//    subMenu : [
//        {
//            label: '插入日期',
//            cmdName : 'date'
//        },
//        {
//            label: '插入时间',
//            cmdName : 'time'
//        }]
//});




/**
 * @description 查找替换
 * @author zhanyi
 */
(function(){


    baidu.editor.commands['searchreplace'] = {
            execCommand : function(cmdName,opt){
               	var editor = this,
                    sel = editor.selection,
                    range,
                    nativeRange,
                    num = 0,
                    currentRange = editor.currentRangeForSR,
                    first = editor.firstForSR;
                opt = baidu.editor.utils.extend(opt,{
                    replaceStr : null,
                    all : false,
                    casesensitive : false,
                    dir : 1
                },true);


                if(baidu.editor.browser.ie){
                    while(1){
                        var tmpRange;
                        nativeRange = editor.document.selection.createRange();
                        tmpRange = nativeRange.duplicate();
                        tmpRange.moveToElementText(editor.document.body);
                        if(opt.all){
                            first = 0;
                            opt.dir = 1;
                            if(currentRange){
                                tmpRange.setEndPoint(opt.dir == -1 ? 'EndToStart' : 'StartToEnd',currentRange)
                            }
                        }else{
                            tmpRange.setEndPoint(opt.dir == -1 ? 'EndToStart' : 'StartToEnd',nativeRange);
                            if(opt.replaceStr){
                                tmpRange.setEndPoint(opt.dir == -1 ? 'StartToEnd' : 'EndToStart',nativeRange);
                            }
                        }
                        nativeRange = tmpRange.duplicate();



                        if(!tmpRange.findText(opt.searchStr,opt.dir,opt.casesensitive ? 4 : 0)){

                            tmpRange = editor.document.selection.createRange();
                            tmpRange.scrollIntoView();
                            return num;
                        }
                        tmpRange.select();
                        //替换
                        if(opt.replaceStr){
                            range = sel.getRange();
                            range.deleteContents().insertNode(range.document.createTextNode(opt.replaceStr)).select();
                            currentRange = sel.getNative().createRange();

                        }
                        num++;
                        if(!opt.all)break;
                    }
                }else{
                    var w = editor.window,nativeSel = sel.getNative(),tmpRange;
                    while(1){
                        if(opt.all){
                            if(currentRange){
                                currentRange.collapse(false);
                                nativeRange = currentRange;
                                
                            }else{
                                nativeRange  = editor.document.createRange();
                                nativeRange.setStart(editor.document.body,0);

                            }
                            nativeSel.removeAllRanges();
                            nativeSel.addRange( nativeRange );
                            first = 0;
                            opt.dir = 1;
                        }else{
                            nativeRange = w.getSelection().getRangeAt(0);
                           
                            if(opt.replaceStr){
                                nativeRange.collapse(opt.dir == 1 ? true : false);
                            }
                        }

                        //如果是第一次并且海选中了内容那就要清除，为find做准备
                       
                        if(!first){
                            nativeRange.collapse( opt.dir <0 ? true : false);
                            nativeSel.removeAllRanges();
                            nativeSel.addRange( nativeRange );
                        }else{
                            nativeSel.removeAllRanges();
                        }

                        if(!w.find(opt.searchStr,opt.casesensitive,opt.dir < 0 ? true : false) ) {
                            nativeSel.removeAllRanges();

                            return num;
                        }
                        first = 0;
                        range = w.getSelection().getRangeAt(0);
                        if(!range.collapsed){
                            if(opt.replaceStr){
                                range.deleteContents();
                                var text = w.document.createTextNode(opt.replaceStr);
                                range.insertNode(text);
                                range.selectNode(text);
                                nativeSel.addRange(range);
                                currentRange = range.cloneRange();
                            }
                        }
                        num++;
                        if(!opt.all)break;
                    }

                }
                return true;
            }
    }

})();
/**
 * @description 插入内容
 * @author zhanyi
    */
(function(){
    var domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd,
        utils = baidu.editor.utils;
    baidu.editor.commands['inserthtml'] = {
        execCommand: function (command,html){
            var editor = this,
                range,deletedElms, i,ci,
                div,
                tds = editor.currentSelectedArr;

            range = editor.selection.getRange();

            div = range.document.createElement( 'div' );
            div.style.display = 'inline';
            div.innerHTML = utils.trim( html );

            editor.adjustTable && editor.adjustTable(div);

            if(tds && tds.length){
                for(var i=0,ti;ti=tds[i++];){
                    ti.className = ''
                }
                tds[0].innerHTML = '';
                range.setStart(tds[0],0).collapse(true);
                editor.currentSelectedArr = [];
            }

            if ( !range.collapsed ) {

                range.deleteContents();
                if(range.startContainer.nodeType == 1){
                    var child = range.startContainer.childNodes[range.startOffset],pre;
                    if(child && domUtils.isBlockElm(child) && (pre = child.previousSibling) && domUtils.isBlockElm(pre)){
                        range.setEnd(pre,pre.childNodes.length).collapse();
                        while(child.firstChild){
                            pre.appendChild(child.firstChild);

                        }
                        domUtils.remove(child);
                    }
                }

            }


            var child,parent,pre,tmp,hadBreak = 0;
            while ( child = div.firstChild ) {
                range.insertNode( child );
                if ( !hadBreak && child.nodeType == domUtils.NODE_ELEMENT && domUtils.isBlockElm( child ) ){

                    parent = domUtils.findParent( child,function ( node ){ return domUtils.isBlockElm( node ); } );
                    if ( parent && parent.tagName.toLowerCase != 'body' && !(dtd[parent.tagName][child.nodeName] && child.parentNode === parent)){
                        if(!dtd[parent.tagName][child.nodeName]){
                            pre = parent;
                        }else{
                            tmp = child.parentNode;
                            while (tmp !== parent){
                                pre = tmp;
                                tmp = tmp.parentNode;
    
                            }    
                        }
                        
                       
                        domUtils.breakParent( child, pre || tmp );
                        //去掉break后前一个多余的节点  <p>|<[p> ==> <p></p><div></div><p>|</p>
                        var pre = child.previousSibling;
                        domUtils.trimWhiteTextNode(pre);
                        if(!pre.childNodes.length){
                            domUtils.remove(pre);
                        }
                        hadBreak = 1;
                    }
                }
                var next = child.nextSibling;
                if(!div.firstChild && next && domUtils.isBlockElm(next)){

                    range.setStart(next,0).collapse(true);
                    break;
                }
                range.setEndAfter( child ).collapse();

            }
//            if(!range.startContainer.childNodes[range.startOffset] && domUtils.isBlockElm(range.startContainer)){
//                next = editor.document.createElement('br');
//                range.insertNode(next);
//                range.collapse(true);
//            }
            //block为空无法定位光标

            child = range.startContainer;
            //用chrome可能有空白展位符
            if(domUtils.isBlockElm(child) && domUtils.isEmptyNode(child)){
                child.innerHTML = baidu.editor.browser.ie ? '' : '<br/>'
            }
            //加上true因为在删除表情等时会删两次，第一次是删的fillData
            range.select(true);
            //range.scrollToView(editor.autoHeightEnabled);
            
        }
    };
}());

/**
 * 选区路径
 * @function
 * @name baidu.editor.commands.elementpath
 * @author zhanyi
 */
(function() {

    var domUtils = baidu.editor.dom.domUtils,
        currentLevel,
        tagNames,
        dtd = baidu.editor.dom.dtd;


    baidu.editor.commands['elementpath'] = {
        execCommand : function( cmdName, level ) {
            var me = this,
                start = tagNames[level],
                range = me.selection.getRange();
            me.currentSelectedArr && domUtils.clearSelectedArr(me.currentSelectedArr);
           
            currentLevel = level*1;
            if(dtd.$tableContent[start.tagName]){
                switch (start.tagName){
                    case 'TD':me.currentSelectedArr = [start];
                            start.className = me.options.selectedTdClass;
                            break;
                    case 'TR':
                        var cells = start.cells;
                        for(var i=0,ti;ti=cells[i++];){
                            me.currentSelectedArr.push(ti);
                            ti.className = me.options.selectedTdClass;
                        }
                        break;
                    case 'TABLE':
                    case 'TBODY':

                        var rows = start.rows;
                        for(var i=0,ri;ri=rows[i++];){
                            cells = ri.cells;
                            for(var j=0,tj;tj=cells[j++];){
                                 me.currentSelectedArr.push(tj);
                                tj.className = me.options.selectedTdClass;
                            }
                        }

                }
                start = me.currentSelectedArr[0];
                if(domUtils.isEmptyNode(start)){
                    range.setStart(start,0).setCursor()
                }else{
                   range.selectNodeContents(start).select()
                }
            }else{
                range.selectNode(start).select()

            }
        },
        queryCommandValue : function() {
            var start = this.selection.getStart(),
                parents = domUtils.findParents(start, true),

                names = [];
            tagNames = parents;
            for(var i=0,ci;ci=parents[i];i++){
                var name = ci.tagName.toLowerCase();
                if(name == 'img' && ci.getAttribute('anchorname')){
                    name = 'anchor'
                }
                names[i] = name;
                if(currentLevel == i){
                   currentLevel = -1;
                    break;
                }
            }
            return names;
        }
    }


})();


/**
 * 格式刷，只格式inline的
 * @function
 * @name baidu.editor.commands.formatmatch
 * @author zhanyi
 */
(function() {

    var domUtils = baidu.editor.dom.domUtils,
        list = [],img,
        flag = 0,
        browser = baidu.editor.browser;

    baidu.editor.commands['formatmatch'] = {
        execCommand : function( cmdName ) {
            var me = this;
            if(flag){
                flag = 0;
                list = [];
                 me.removeListener('mouseup',addList);
                return;
            }

            function addList(type,evt){
                if(browser.webkit){
                    var target = evt.target.tagName == 'IMG' ? evt.target : null;
                }
               
                function addFormat(range){
                    
                    if(text && (!me.currentSelectedArr || !me.currentSelectedArr.length)){
                        range.selectNode(text);
                    }
                    return range.applyInlineStyle(list[list.length-1].tagName,null,list);
                    
                }

                me.undoManger && me.undoManger.save();

                var range = me.selection.getRange(),
                    imgT = target || range.getClosedNode();
                if(img && imgT && imgT.tagName == 'IMG'){
                    //trace:964
                    
                    imgT.style.cssText += ';float:' + (img.style.cssFloat || img.style.styleFloat ||'none') + ';display:' + (img.style.display||'inline');

                    img = null;
                }else{
                    if(!img){
                        var collapsed = range.collapsed;
                        if(collapsed){
                            var text = me.document.createTextNode('match');
                            range.insertNode(text).select();


                        }
                        me.__hasEnterExecCommand = true;
                        me.execCommand('removeformat',null,null,null,collapsed);
                        me.__hasEnterExecCommand = false;
                        //trace:969
                        range = me.selection.getRange();
                        if(list.length == 0){

                            if(me.currentSelectedArr && me.currentSelectedArr.length > 0){
                                range.selectNodeContents(me.currentSelectedArr[0]).select();
                            }
                        }else{
                            if(me.currentSelectedArr && me.currentSelectedArr.length > 0){

                                for(var i=0,ci;ci=me.currentSelectedArr[i++];){
                                    range.selectNodeContents(ci);
                                    addFormat(range);

                                }
                                range.selectNodeContents(me.currentSelectedArr[0]).select();
                            }else{


                                addFormat(range)

                            }
                        }
                        if(!me.currentSelectedArr || !me.currentSelectedArr.length){
                            if(text){
                                range.setStartBefore(text).collapse(true);

                            }

                            range.select()
                        }
                        text && domUtils.remove(text);
                    }

                }


               

                me.undoManger && me.undoManger.save();
                me.removeListener('mouseup',addList);
                flag = 0;
            }

              
            var range = me.selection.getRange();
            img = range.getClosedNode();
            if(!img || img.tagName != 'IMG'){
               range.collapse(true).shrinkBoundary();
               var start = range.startContainer;
               list = domUtils.findParents(start,true,function(node){
                   return !domUtils.isBlockElm(node) && node.nodeType == 1
               });
               //a不能加入格式刷, 并且克隆节点
               for(var i=0,ci;ci=list[i];i++){
                   if(ci.tagName == 'A'){
                       list.splice(i,1);
                       break;
                   }
               }

            }

            me.addListener('mouseup',addList);
            flag = 1;


        },
        queryCommandState : function() {
            return flag;
        },
        notNeedUndo : 1
    }
//    baidu.editor.contextMenuItems.push({
//        label : '格式刷',
//        cmdName : 'formatmatch'
//    })
})();


/**
 * @description 设置行距
 * @author zhanyi
 */
(function(){
    
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands['rowspacing'] =  {
        execCommand : function( cmdName,value ) {
            this.execCommand('paragraph','p',{'padding' : value + 'px 0'});
            return true;
        },
        queryCommandValue : function() {
            var startNode = this.selection.getStart(),
                pN = domUtils.findParent(startNode,function(node){return domUtils.isBlockElm(node)},true),
                value;
            //trace:1026
            if(pN){
                value = domUtils.getComputedStyle(pN,'padding-top').replace(/[^\d]/g,'');
                return value*1 <= 10 ? 0 : value;
            }
            return 0;
             
        }
    }

})();

/**
 * Created by .
 * User: zhuwenxuan
 * Date: 11-6-13
 * Time: 下午3:29
 * To change this template use File | Settings | File Templates.
 */
(function(){
    function setRange(range,node){
        range.setStart(node,0).setCursor();
    }
    baidu.editor.commands['cleardoc'] = {
        execCommand : function( cmdName) {
            var me = this,
                enterTag = me.options.enterTag,
                browser = baidu.editor.browser,
                range = this.selection.getRange();
            if(enterTag == "br"){
                this.body.innerHTML = "<br/>";
                setRange(range,this.body);
            }else{
                //不用&nbsp;也能定位，所以去掉，chrom也可以不要br,ff不行再想定位回去不行了，需要br
                this.body.innerHTML = "<p>"+(browser.ie ? "" : "<br/>")+"</p>";
                me.focus();
                setRange(range,me.body.firstChild);
            }
        }
    };
//    baidu.editor.contextMenuItems.push({
//        label : '清除文档',
//        cmdName : 'cleardoc'
//    })
})();

/**
 * 锚点
 * @function
 * @name baidu.editor.commands.anchor
 * @author zhanyi
 */
(function (){
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands['anchor'] = {
        execCommand : function (cmd, name){
            var range = this.selection.getRange();
            var img = range.getClosedNode();
            if(img && img.getAttribute('anchorname')){
                if(name){
                    img.setAttribute('anchorname',name);
                }else{
                    range.setStartBefore(img).setCursor();
                    domUtils.remove(img);
                }
            }else{
                if(name){
                    //只在选区的开始插入
                    var anchor = this.document.createElement('img');
                    range.collapse(true);
                    anchor.setAttribute('anchorname',name);
                    anchor.className = 'anchorclass';

                    range.insertNode(anchor).setStartAfter(anchor).collapse(true).select(true);
                    baidu.editor.browser.gecko && anchor.parentNode.insertBefore(this.document.createElement('br'),anchor.nextSibling)
                }
            }
        }
    };
})();

/**
 * 锚点
 * @function
 * @name baidu.editor.commands.delete
 * @author zhanyi
 */
(function (){
    var domUtils = baidu.editor.dom.domUtils,
        browser = baidu.editor.browser;
    baidu.editor.commands['delete'] = {
        execCommand : function (cmd, name){
            
            var range = this.selection.getRange(),
            
                mStart = 0,
                mEnd = 0,
                me = this;
            while(!range.startOffset && !domUtils.isBody(range.startContainer) && !domUtils.isBlockElm(range.startContainer)){
                mStart = 1;
                range.setStartBefore(range.startContainer);
            }
            while(!domUtils.isBody(range.endContainer) &&  !domUtils.isBlockElm(range.endContainer) && range.endOffset == (range.endContainer.nodeType == 1 ? range.endContainer.childNodes.length : range.endContainer.nodeValue.length)){
                mEnd = 1;
                range.setEndAfter(range.endContainer);
                if(browser.webkit){
                    var child = range.endContainer.childNodes[range.endOffset];
                    if(child && child.nodeType == 1 && child.tagName == 'BR' && range.endContainer.lastChild === child){
                        range.setEndAfter(child);
                    }
                }

            }
            if(mStart){
                var start = me.document.createElement('span');
                start.innerHTML = 'start';
                start.id = '_baidu_cut_start';
                range.insertNode(start).setStartBefore(start)
            }
            if(mEnd){
                var end = me.document.createElement('span');
                end.innerHTML = 'end';
                end.id = '_baidu_cut_end';
                range.cloneRange().collapse(false).insertNode(end);
                range.setEndAfter(end)

            }
            range.deleteContents().select()
        },
        queryCommandState : function(){
            return this.selection.getRange().collapsed ? -1 : 0;
        }
    };
})();

/**
 * @description 回退
 * @author zhanyi
 */
(function() {

    var fillchar = new RegExp(baidu.editor.dom.domUtils.fillChar + '|<\/hr>','gi'),// ie会产生多余的</hr>
        browser = baidu.editor.browser;
    baidu.editor.plugins['undo'] = function() {
        var me = this,
            maxUndoCount = me.options.maxUndoCount,
            maxInputCount = me.options.maxInputCount,
            //在比较时，需要过滤掉这些属性
            specialAttr = /\b(?:href|src|name)="[^"]*?"/gi;

        function UndoManager() {

            this.list = [];
            this.index = 0;
            this.hasUndo = false;
            this.hasRedo = false;
            this.undo = function() {

                if ( this.hasUndo ) {
                    var currentScene = this.getScene(),
                        lastScene = this.list[this.index];
                    if ( lastScene.content.replace(specialAttr,'') != currentScene.content.replace(specialAttr,'') ) {
                        this.save();
                    }
                                        if(!this.list[this.index - 1] && this.list.length == 1){
                        this.reset();
                        return;
                    }
                    while ( this.list[this.index].content == this.list[this.index - 1].content ) {
                        this.index--;
                        if ( this.index == 0 ) {
                            return this.restore( 0 )
                        }
                    }
                    this.restore( --this.index );
                }
            };
            this.redo = function() {
                if ( this.hasRedo ) {
                    while ( this.list[this.index].content == this.list[this.index + 1].content ) {
                        this.index++;
                        if ( this.index == this.list.length - 1 ) {
                            return this.restore( this.index )
                        }
                    }
                    this.restore( ++this.index );
                }
            };

            this.restore = function() {
              
                var scene = this.list[this.index];
                //trace:873
                //去掉展位符
                me.document.body.innerHTML = scene.bookcontent.replace(fillchar,'');
                var range = new baidu.editor.dom.Range( me.document );
                range.moveToBookmark( {
                    start : '_baidu_bookmark_start_',
                    end : '_baidu_bookmark_end_',
                    id : true
                //去掉true 是为了<b>|</b>，回退后还能在b里
                //todo safari里输入中文时，会因为改变了dom而导致丢字
                } ).select(!browser.gecko).scrollToView(me.autoHeightEnabled);

                this.update();
                //table的单独处理
                if(me.currentSelectedArr){
                    me.currentSelectedArr = [];
                    var tds = me.document.getElementsByTagName('td');
                    for(var i=0,td;td=tds[i++];){
                        if(td.className == me.options.selectedTdClass){
                             me.currentSelectedArr.push(td);
                        }
                    }
                }
                 this.clearKey();
            };

            this.getScene = function() {
                var range = me.selection.getRange(),
                    cont = me.body.innerHTML.replace(fillchar,'');
                baidu.editor.browser.ie && (cont = cont.replace(/>&nbsp;</g,'><').replace(/\s*</g,'').replace(/>\s*/g,'>'));
                var bookmark = range.createBookmark( true, true ),
                    bookCont = me.body.innerHTML.replace(fillchar,'');
                
                range.moveToBookmark( bookmark ).select( true );
                return {
                    bookcontent : bookCont,
                    content : cont
                }
            };
            this.save = function() {

                var currentScene = this.getScene(),
                    lastScene = this.list[this.index];
                //内容相同位置相同不存
                if ( lastScene && lastScene.content == currentScene.content &&
                        lastScene.bookcontent == currentScene.bookcontent
                ) {
                    return;
                }

                this.list = this.list.slice( 0, this.index + 1 );
                this.list.push( currentScene );
                //如果大于最大数量了，就把最前的剔除
                if ( this.list.length > maxUndoCount ) {
                    this.list.shift();
                }
                this.index = this.list.length - 1;
                this.clearKey();
                //跟新undo/redo状态
                this.update()
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
            this.clearKey = function(){
                 keycont = 0;
                lastKeyCode = null;
            }
        }

        me.undoManger = new UndoManager();
        function saveScene() {

            this.undoManger.save()
        }

        me.addListener( 'beforeexeccommand', saveScene );
        me.addListener( 'afterexeccommand', saveScene );

        // me.addListener('ready',saveScene);
        

        me.commands['redo'] = me.commands['undo'] = {
            execCommand : function( cmdName ) {
                me.undoManger[cmdName]();
            },
            queryCommandState : function( cmdName ) {

                return me.undoManger['has' + (cmdName.toLowerCase() == 'undo' ? 'Undo' : 'Redo')] ? 0 : -1;
            },
            notNeedUndo : 1
        };

        var keys = {
             //  /*Backspace*/ 8:1, /*Delete*/ 46:1,
                /*Shift*/ 16:1, /*Ctrl*/ 17:1, /*Alt*/ 18:1,
                37:1, 38:1, 39:1, 40:1,
                13:1 /*enter*/
            },
            keycont = 0,
            lastKeyCode;

        me.addListener( 'keydown', function( type, evt ) {
            var keyCode = evt.keyCode || evt.which;

            if ( !keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey ) {

                if ( me.undoManger.list.length == 0 || ((keyCode == 8 ||keyCode == 46) && lastKeyCode != keyCode) ) {

                    me.undoManger.save();
                    lastKeyCode = keyCode;
                    return

                }
                //trace:856
                //修正第一次输入后，回退，再输入要到keycont>maxInputCount才能在回退的问题
                if(me.undoManger.list.length == 2 && me.undoManger.index == 0 && keycont == 0){
                    me.undoManger.list.splice(1,1);
                    me.undoManger.update();
                }
                lastKeyCode = keyCode;
                keycont++;
                if ( keycont > maxInputCount ) {

                    setTimeout( function() {
                        me.undoManger.save();
                    }, 0 );

                }
            }
        } )
    };
})();

/*
 ** @description 粘贴
 * @author zhanyi
 */
(function() {

	var domUtils = baidu.editor.dom.domUtils,
        browser = baidu.editor.browser;

    function getClipboardData( callback ) {

        var doc = this.document;

        if ( doc.getElementById( 'baidu_pastebin' ) ) {
            return;
        }

        var range = this.selection.getRange(),
            bk = range.createBookmark(),
            //创建剪贴的容器div
            pastebin = doc.createElement( 'div' );

        pastebin.id = 'baidu_pastebin';

        // Safari 要求div必须有内容，才能粘贴内容进来
        browser.webkit && pastebin.appendChild( doc.createTextNode( domUtils.fillChar + domUtils.fillChar ) );
        doc.body.appendChild( pastebin );
        pastebin.style.cssText = "position:absolute;width:1px;height:1px;overflow:hidden;left:-1000px;white-space:nowrap;top:" +
            //要在现在光标平行的位置加入，否则会出现跳动的问题
            domUtils.getXY( bk.start ).y + 'px';

        range.selectNodeContents( pastebin ).select( true );

        setTimeout( function() {
           
			pastebin.parentNode.removeChild(pastebin);
            range.moveToBookmark( bk ).select(true);
            callback( pastebin );
        }, 0 );


    }

    baidu.editor.plugins['paste'] = function() {
        var me = this;

        var pasteplain = me.options.pasteplain;

        me.commands['pasteplain'] = {
            queryCommandState: function (){
                return pasteplain;
            },
            execCommand: function (){
                pasteplain = !pasteplain|0;
            },
            notNeedUndo : 1
        };
        
        function filter(div){
            var html;
            if ( div.firstChild ) {
                    //去掉cut中添加的边界值
                    var nodes = domUtils.getElementsByTagName(div,'span');
                    for(var i=0,ni;ni=nodes[i++];){
                        if(ni.id == '_baidu_cut_start' || ni.id == '_baidu_cut_end'){
                            domUtils.remove(ni)
                        }
                    }


                    if(browser.webkit){
                        var divs = div.querySelectorAll('div #baidu_pastebin'),p;
                        for(var i=0,di;di=divs[i++];){
                            p = me.document.createElement('p');
                            while(di.firstChild){
                                p.appendChild(di.firstChild)
                            }
                            di.parentNode.insertBefore(p,di);
                            domUtils.remove(di,true)
                        }
                        var spans = div.querySelectorAll('span.Apple-style-span');
                        for(var i=0,ci;ci=spans[i++];){
                            domUtils.remove(ci,true);
                        };
                        var metas = div.querySelectorAll('meta');
                        for(var i=0,ci;ci=metas[i++];){
                            domUtils.remove(ci);
                        };
                        //<div><br></div>会造成多余的空行
                        var brs = div.querySelectorAll('div br');
                        for(var i=0,bi;bi=brs[i++];){
                            var pN = bi.parentNode;
                            if(pN.tagName == 'DIV' && pN.childNodes.length ==1){
                                domUtils.remove(pN)
                            }
                        }

                    }
                    if(browser.gecko){
                        var dirtyNodes = div.querySelectorAll('[_moz_dirty]')
                        for(i=0;ci=dirtyNodes[i++];){
                            ci.removeAttribute( '_moz_dirty' )
                        }
                    }

                    //if(!pasteplain){
                        html = div.innerHTML;

                        var f = me.serialize;
                        if(f){
                            var node =  f.transformInput(
                                        f.parseHTML(
                                            f.word(html), true
                                        )
                                    );
                            //trace:924
                            //纯文本模式也要保留段落
                            node = f.filter(node,pasteplain ? {
                                whiteList: {
                                    'p': {$:{}}
                                },
                                blackList: {
                                    'style':1,
                                    'script':1,
                                    'object':1
                                }
                            } :  null);

                            if(browser.webkit){
                                var length = node.children.length,
                                    child;
                                while((child = node.children[length-1]) && child.tag == 'br'){
                                    node.children.splice(length-1,1);
                                    length = node.children.length;
                                }
                            }
                            html = f.toHTML(node)

                        }
//                        }else{
//                            html = div[browser.ie ? 'innerText':'textContent'];
//                        }
                    //自定义的处理
                    me.fireEvent('beforepaste',html);
                    me.execCommand( 'insertHtml',html);

                }
        }
        
        me.addListener('ready',function(){
            domUtils.on(me.body,'cut',function(){

                var range = me.selection.getRange();
                if(!range.collapsed && me.undoManger){
                    me.undoManger.save()
                }
                //修正剪切不能把整个元素剪切出来
                range = me.selection.getRange();
                if( !range.collapsed){
                    var mStart = 0,
                        mEnd = 0;
                    while(!range.startOffset && !domUtils.isBody(range.startContainer)){
                        mStart = 1;
                        range.setStartBefore(range.startContainer);
                    }
                    while(!domUtils.isBody(range.endContainer) && range.endOffset == (range.endContainer.nodeType == 1 ? range.endContainer.childNodes.length : range.endContainer.nodeValue.length)){
                        mEnd = 1;
                        range.setEndAfter(range.endContainer);
                        if(browser.webkit){
                            var child = range.endContainer.childNodes[range.endOffset];
                            if(child && child.nodeType == 1 && child.tagName == 'BR' && range.endContainer.lastChild === child){
                                range.setEndAfter(child);
                            }
                        }
                        
                    }
                    if(mStart){
                        var start = me.document.createElement('span');
                        start.innerHTML = 'start';
                        start.id = '_baidu_cut_start';
                        range.insertNode(start).setStartBefore(start)
                    }
                    if(mEnd){
                        var end = me.document.createElement('span');
                        end.innerHTML = 'end';
                        end.id = '_baidu_cut_end';
                        range.cloneRange().collapse(false).insertNode(end);
                        range.setEndAfter(end)

                    }
                    range.select();
                    if(browser.ie){
                        setTimeout(function(){
                            var node = me.document.getElementById('_baidu_cut_end');
                            node && domUtils.remove(node)
                        },50)
                    }

                }
            });
            domUtils.on(me.body, browser.ie ? 'beforepaste' : 'paste',function(e){
                 getClipboardData.call( me, function( div ) {
                        filter(div);

                    } );
               

            })
        });

    }

})();


(function (){
    var browser = baidu.editor.browser,
        domUtils = baidu.editor.dom.domUtils,
        dtd = baidu.editor.dom.dtd;

    function SourceFormater(config){
        config = config || {};
        this.indentChar = config.indentChar || '  ';
        this.breakChar = config.breakChar || '\n';
        this.selfClosingEnd = config.selfClosingEnd || ' />';
    }
    var unhtml1 = function (){
        var map = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
        function rep( m ){ return map[m]; }
        return function ( str ) {
            return str ? str.replace( /[<>"']/g, rep ) : '';
        };
    }();
    function printAttrs(attrs){
        var buff = [];
        for (var k in attrs) {
            buff.push(k + '="' + unhtml1(attrs[k]) + '"');
        }
        return buff.join(' ');
    }
    SourceFormater.prototype = {
        format: function (html){
            var node = baidu.editor.serialize.parseHTML(html);
            this.buff = [];
            this.indents = '';
            this.indenting = 1;
            this.visitNode(node);
            return this.buff.join('');
        },
        visitNode: function (node){
            if (node.type == 'fragment') {
                this.visitChildren(node.children);
            } else if (node.type == 'element') {
                var selfClosing = dtd.$empty[node.tag];
                this.visitTag(node.tag, node.attributes, selfClosing);
                this.visitChildren(node.children);
                if (!selfClosing) {
                    this.visitEndTag(node.tag);
                }
            } else if (node.type == 'comment') {
                this.visitComment(node.data);
            } else {
                this.visitText(node.data);
            }
        },
        visitChildren: function (children){
            for (var i=0; i<children.length; i++) {
                this.visitNode(children[i]);
            }
        },
        visitTag: function (tag, attrs, selfClosing){
            if (this.indenting) {
                this.indent();
            } else if (!dtd.$inline[tag] && tag != 'a') { // todo: 去掉a, 因为dtd的inline里面没有a
                this.newline();
                this.indent();
            }
            this.buff.push('<', tag);
            var attrPart = printAttrs(attrs);
            if (attrPart) {
                this.buff.push(' ', attrPart);
            }
            if (selfClosing) {
                this.buff.push(this.selfClosingEnd);
                if (tag == 'br') {
                    this.newline();
                }
            } else {
                this.buff.push('>');
                this.indents += this.indentChar;
            }
            if (!dtd.$inline[tag]) {
                this.newline();
            }
        },
        indent: function (){
            this.buff.push(this.indents);
            this.indenting = 0;
        },
        newline: function (){
            this.buff.push(this.breakChar);
            this.indenting = 1;
        },
        visitEndTag: function (tag){
            this.indents = this.indents.slice(0, -this.indentChar.length);
            if (this.indenting) {
                this.indent();
            } else if (!dtd.$inline[tag] && !(dtd[tag] && dtd[tag]['#'])) {
                this.newline();
                this.indent();
            }
            this.buff.push('</', tag, '>');
        },
        visitText: function (text){
            if (this.indenting) {
                this.indent();
            }
            this.buff.push(text);
        },
        visitComment: function (text){
            if (this.indenting) {
                this.indent();
            }
            this.buff.push('<!--', text, '-->');
        }
    };

    function selectTextarea(textarea){
        var range;
        if (browser.ie) {
            range = textarea.createTextRange();
            range.collapse(true);
            range.select();
        } else {
            //todo: chrome下无法设置焦点
            textarea.setSelectionRange(0, 0);
            textarea.focus();
        }
    }
    function createTextarea(container){
        var textarea = document.createElement('textarea');
        textarea.style.cssText = 'display:none;resize:none;width:100%;height:100%;border:0;padding:0;margin:0;';
        container.appendChild(textarea);
        return textarea;
    }

    baidu.editor.plugins['source'] = function (){
        var editor = this;
        editor.initPlugins(['serialize']);

        var formatter = new SourceFormater(editor.options.source);
        var sourceMode = false;
        var textarea;

        editor.addListener('ready', function (){
            var container = editor.iframe.parentNode;
            textarea = createTextarea(container);
            if (browser.ie && browser.version < 8) {
                container.onresize = function (){
                    textarea.style.width = this.offsetWidth + 'px';
                    textarea.style.height = this.offsetHeight + 'px';
                };
                textarea.style.width = container.offsetWidth + 'px';
                textarea.style.height = container.offsetHeight + 'px';
            }
            container = null;
        });

        var bakCssText;
        editor.commands['source'] = {
            execCommand: function (){
                sourceMode = !sourceMode;
                if (sourceMode) {
                    editor.undoManger && editor.undoManger.save();
                    this.currentSelectedArr && domUtils.clearSelectedArr(this.currentSelectedArr);
                    bakCssText = editor.iframe.style.cssText;
                    editor.iframe.style.cssText += 'position:absolute;left:-32768px;top:-32768px;';
                    if (browser.webkit) {
                        textarea = createTextarea(editor.iframe.parentNode);
                    }
                    textarea.value = formatter.format(editor.getContent());
                    textarea.style.display = '';
                    setTimeout(function (){
                        selectTextarea(textarea);
                    });
                } else {
                    textarea.style.display = 'none';
                    editor.iframe.style.cssText = bakCssText;
                    editor.setContent(textarea.value);
                    //要在ifm为显示时ff才能取到selection,否则报错
                    editor.undoManger && editor.undoManger.save();
                    if(browser.gecko){
                        var range = editor.selection.getRange();
                        range.setCursor();
                    }


                }
                this.fireEvent('sourcemodechanged', sourceMode);
            },
            queryCommandState: function (){
                return sourceMode|0;
            }
        };

        var oldQueryCommandState = editor.queryCommandState;
        editor.queryCommandState = function (cmdName){
            cmdName = cmdName.toLowerCase();
            if (sourceMode) {
                return cmdName == 'source' ? 1 : -1;
            }
            return oldQueryCommandState.apply(this, arguments);
        };
    };

})();
//配置快捷键
baidu.editor.plugins['shortcutkeys'] = function(){
    var editor = this,
        shortcutkeys =  baidu.editor.utils.extend({
    		 "ctrl+66" : "Bold" //^B
        	,"ctrl+90" : "Undo" //undo
        	,"ctrl+89" : "Redo" //redo
       		,"ctrl+73" : "Italic" //^I
       		,"ctrl+85" : "Underline:Underline" //^U
        	,"ctrl+shift+67" : "removeformat" //清除格式
        	,"ctrl+shift+76" : "justify:left" //居左
        	,"ctrl+shift+82" : "justify:right" //居右
        	,"ctrl+65" : "selectAll"
//        	,"9"	   : "indent" //tab
    	},editor.options.shortcutkeys);
    editor.addListener('keydown',function(type,e){

        var keyCode = e.keyCode || e.which,value;
		for ( var i in shortcutkeys ) {
		    if ( /^(ctrl)(\+shift)?\+(\d+)$/.test( i.toLowerCase() ) || /^(\d+)$/.test( i ) ) {
		        if ( ( (RegExp.$1 == 'ctrl' ? (e.ctrlKey||e.metaKey) : 0)
                        && (RegExp.$2 != "" ? e[RegExp.$2.slice(1) + "Key"] : 1)
                        && keyCode == RegExp.$3
                    ) ||
                     keyCode == RegExp.$1
                ){

                    value = shortcutkeys[i].split(':');
                    editor.execCommand( value[0],value[1]);
                    e.preventDefault ? e.preventDefault() : (e.returnValue = false);

		        }
		    }
		}
    });

};
/**
 * @description 处理回车
 * @author zhanyi
 */
(function() {

    var browser = baidu.editor.browser,
        domUtils = baidu.editor.dom.domUtils,
        hTag ;
    baidu.editor.plugins['enterkey'] = function() {
        var me = this,
            tag = me.options.enterTag,
            flag = 0,
            inlineParents;
        me.addListener( 'keyup', function( type, evt ) {

            var keyCode = evt.keyCode || evt.which;
            if ( keyCode == 13 ) {
                var range = me.selection.getRange(),
                    start = range.startContainer,
                    doSave;
                
                //修正在h1-h6里边回车后不能嵌套p的问题
                if(!browser.ie){
                     if(/h\d/i.test(hTag) ){
                        if(browser.gecko){
                            var h = domUtils.findParentByTagName(start,[ 'h1', 'h2', 'h3', 'h4', 'h5', 'h6','blockquote'],true);
                            if(!h){
                                me.document.execCommand( 'formatBlock', false, '<p>' );
                                doSave = 1;
                            }
                        }else{
                            //chrome remove div
                            if(start.nodeType == 1 ){
                                var tmp = me.document.createTextNode(''),div;
                                range.insertNode(tmp);
                                div = domUtils.findParentByTagName(tmp,'div');
                                if(div){
                                    var p = me.document.createElement('p');
                                    while(div.firstChild){
                                        p.appendChild(div.firstChild);
                                    }
                                    div.parentNode.insertBefore(p,div);
                                    domUtils.remove(div);
                                    range.setStartBefore(tmp).setCursor();
                                    doSave = 1;
                                }
                                domUtils.remove(tmp);
                                
                            }
                        }




                    }
                }

                //修正回车不能把inline样式绑定下来的问题
                if( me.options.enterTag == 'p' && inlineParents && inlineParents.length>0){


                     
                    if(!range.startOffset && start.nodeType == 1 &&  domUtils.isBlockElm(start)){
                        if(domUtils.isEmptyNode(start)){

                            var frag = me.document.createDocumentFragment(),
                            level = frag,
                            node;
                            while((node = inlineParents.pop()) && node.nodeType == 1 ){

                                level.appendChild(node.cloneNode(false));
                                level = level.firstChild;
                            }
                            if(frag.firstChild){
                                start.innerHTML = '';
                                range.insertNode(frag).setStart(level,0).setCursor();
                                doSave = 1;
                            }
                        }

                    }



                }
                if ( me.undoManger && doSave ) {
                    me.undoManger.save()
                }
                
            }
        } );

        me.addListener( 'keypress', function( type, evt ) {
            var keyCode = evt.keyCode || evt.which;
            if ( keyCode == 13 ) {//回车
                hTag = '';
                //chrome 在回车时，保存现场会有问题
                if ( !browser.webkit && me.undoManger ) {
                    me.undoManger.save()
                }
                var range = me.selection.getRange();
                inlineParents = [];
                range.shrinkBoundary();

                //修正ff不能把内联样式放到新的换行里的问题,先记录有那些节点
                if( range.collapsed){
                     inlineParents = domUtils.findParents(range.startContainer,true,function(node){
                        return node.nodeType == 1 && !domUtils.isBlockElm(node) && node.tagName != 'A' //a不加入到回车中
                    },true)
                }

                //li不处理
                if ( domUtils.findParentByTagName( range.startContainer, ['ol','ul'], true ) ) {
                    return;
                }
                if ( !range.collapsed ) {
                    //跨td不能删
                    var start = range.startContainer,
                        end = range.endContainer,
                        startTd = domUtils.findParentByTagName( start, 'td', true ),
                        endTd = domUtils.findParentByTagName( end, 'td', true );
                    if ( startTd && endTd && startTd !== endTd || !startTd && endTd || startTd && !endTd ) {
                        evt.preventDefault ? evt.preventDefault() : ( evt.returnValue = false);
                        return;
                    }
                }
               me.currentSelectedArr && domUtils.clearSelectedArr(me.currentSelectedArr);

                if ( tag == 'p' ) {
                  

                    if ( !browser.ie ) {
                        
                        start = domUtils.findParentByTagName( range.startContainer, ['p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6','blockquote'], true );


                        if ( !start ) {
                            me.document.execCommand( 'formatBlock', false, '<p>' );
                            if ( browser.gecko ) {
                                range = me.selection.getRange();
                                start = domUtils.findParentByTagName( range.startContainer, 'p', true );
                                start && domUtils.removeDirtyAttr( start );
                            }

                        } else {
                            hTag = start.tagName;
                            start.tagName.toLowerCase() == 'p' && browser.gecko && domUtils.removeDirtyAttr( start );
                        }

                    }

                } else {
                    evt.preventDefault ? evt.preventDefault() : ( evt.returnValue = false);
                    if ( !range.collapsed ) {
                        range.deleteContents();
                        start = range.startContainer;
                        if ( start.nodeType == 1 && (start = start.childNodes[range.startOffset]) ) {
                            while ( start.nodeType == 1 ) {
                                if ( baidu.editor.dom.dtd.$empty[start.tagName] ) {
                                    range.setStartBefore( start ).setCursor();
                                    if ( me.undoManger ) {
                                        me.undoManger.save()
                                    }
                                    return false;
                                }
                                if ( !start.firstChild ) {
                                    var br = range.document.createElement( 'br' );
                                    start.appendChild( br );
                                    range.setStart( start, 0 ).setCursor();
                                    if ( me.undoManger ) {
                                        me.undoManger.save()
                                    }
                                    return false;
                                }
                                start = start.firstChild
                            }
                            if ( start === range.startContainer.childNodes[range.startOffset] ) {
                                br = range.document.createElement( 'br' );
                                range.insertNode( br ).setCursor();

                            } else {
                                range.setStart( start, 0 ).setCursor();
                            }


                        } else {
                            br = range.document.createElement( 'br' );
                            range.insertNode( br ).setStartAfter( br ).setCursor();
                        }


                    } else {
                        br = range.document.createElement( 'br' );
                        range.insertNode( br );
                        var parent = br.parentNode;
                        if ( parent.lastChild === br ) {
                            br.parentNode.insertBefore( br.cloneNode( true ), br );
                            range.setStartBefore( br )
                        } else {
                            range.setStartAfter( br )
                        }
                        range.setCursor();

                    }

                }
                if (  !browser.webkit && me.undoManger ) {
                    me.undoManger.save()
                }
            }
        } );
    }

})();

/*
 *   处理特殊键的兼容性问题
 */
(function() {
    var domUtils = baidu.editor.dom.domUtils,
        browser = baidu.editor.browser,
        dtd = baidu.editor.dom.dtd,
        utils = baidu.editor.utils,
        flag = 0,
        keys = domUtils.keys,
        trans = {
            'B' : 'strong',
            'I' : 'em',
            'FONT' : 'span'
        },
        sizeMap = [0, 10, 12, 16, 18, 24, 32, 48];

    baidu.editor.plugins['keystrokes'] = function() {
        var me = this;
        me.addListener( 'keydown', function( type, evt ) {
            var keyCode = evt.keyCode || evt.which;



            //处理space/del
            if ( keyCode == 8 || keyCode == 46) {


                var range = me.selection.getRange(),
                    tmpRange,
                    start,end;

                if(range.collapsed && range.startContainer.nodeType == 3 && range.startContainer.nodeValue.replace(new RegExp( domUtils.fillChar, 'g' ),'').length == 0){
                    range.setStartBefore(range.startContainer).collapse(true)
                }
                //解决选中control元素不能删除的问题
                if(start = range.getClosedNode()){
                    me.undoManger && me.undoManger.save();
                    range.setStartBefore(start);
                    domUtils.remove(start);
                    range.setCursor();
                    me.undoManger && me.undoManger.save();
                    evt.preventDefault? evt.preventDefault() : (evt.returnValue = false);
                    return;
                }
                //阻止在table上的删除
                if(!browser.ie){

                    start= domUtils.findParentByTagName(range.startContainer,'table',true);
                    end = domUtils.findParentByTagName(range.endContainer,'table',true);
                    if(start && !end  || !start && end || start!==end ){
                        evt.preventDefault();
                        return;
                    }
                    if(browser.webkit && range.collapsed && start){
                        tmpRange = range.cloneRange().txtToElmBoundary();
                        start = tmpRange.startContainer;

                        if(domUtils.isBlockElm(start) && start.nodeType == 1 && !dtd.$tableContent[start.tagName] && !domUtils.getChildCount(start,function(node){
                            return node.nodeType == 1 ? node.tagName !=='BR' : 1;
                        }) ){

                            tmpRange.setStartBefore(start).setCursor();
                            domUtils.remove(start,true);
                            evt.preventDefault();
                            return;
                        }
                    }
                }
                //修中ie中li下的问题
                if( range.collapsed && !range.startOffset){

                    var li = domUtils.findParentByTagName(range.startContainer,'li',true),pre;
                    tmpRange = range.cloneRange().trimBoundary();

                    //要在li的最左边，才能处理
                    if(!tmpRange.startOffset){
                        if(browser.ie){
                            if(li && (pre = li.previousSibling)){
                                if(keyCode == 46 && li.childNodes.length)
                                    return;
                                me.undoManger && me.undoManger.save();
                                range.setEnd(pre,pre.childNodes.length).collapse();
                                while(li.firstChild){
                                    pre.appendChild(li.firstChild)
                                }
                                domUtils.remove(li);
                                range.select();
                                me.undoManger && me.undoManger.save();
                                evt.returnValue = false;
                                return;

                            }


                            if( keyCode == 8 && (li && pre || li && domUtils.getChildCount(li,function(node){
                                return !domUtils.isBr(node) && !domUtils.isWhitespace(node);
                            })) ){
                                evt.returnValue = false;
                                return;
                            }
                        }
                        //trace:980

                        if(li && !li.nextSibling && !li.previousSibling && domUtils.getChildCount(li,function(node){return !domUtils.isWhitespace(node) && !domUtils.isBr(node)}) == 0){
                            var p = me.document.createElement('p');
                            li.parentNode.parentNode.insertBefore(p,li.parentNode);
                            p.innerHTML = browser.ie ? '' : '<br/>';
                            range.setStart(p,0).setCursor();
                            domUtils.remove(li.parentNode);
                            me.undoManger && me.undoManger.save();
                            evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false)
                        }



                    }


                }


                if ( me.undoManger ) {

                    if ( !range.collapsed ) {
                        me.undoManger.save();
                        flag = 1;
                    }
                }

            }
        } );
        me.addListener( 'keyup', function( type, evt ) {

            var keyCode = evt.keyCode || evt.which;
            //修复ie/chrome <strong><em>x|</em></strong> 当点退格后在输入文字后会出现  <b><i>x</i></b>
            if ( !browser.gecko && !keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey ){
                range = me.selection.getRange();
                if(range.collapsed){
                    var start = range.startContainer,
                        lastNode,
                        isFixed = 0;

                    while(!domUtils.isBlockElm(start)){
                        if(start.nodeType == 1 && utils.indexOf(['FONT','B','I'],start.tagName)!=-1){

                            var tmpNode = me.document.createElement(trans[start.tagName]);
                            if(start.tagName == 'FONT'){
                                //chrome only remember color property
                                tmpNode.style.cssText = (start.getAttribute('size') ? 'font-size:' + (sizeMap[start.getAttribute('size')] || 12) + 'px' : '')
                                    + ';' + (start.getAttribute('color') ? 'color:'+ start.getAttribute('color') : '')
                                    + ';' + (start.getAttribute('face') ? 'font-family:'+ start.getAttribute('face') : '')
                                    + ';' + start.style.cssText;
                            }
                            while(start.firstChild){
                                tmpNode.appendChild(start.firstChild)
                            }
                            start.parentNode.insertBefore(tmpNode,start);
                            domUtils.remove(start);
                            if(!isFixed){
                                range.setEnd(tmpNode,tmpNode.childNodes.length).collapse(true)

                            };
                            start = tmpNode;
                            isFixed = 1;
                        }
                        start = start.parentNode;

                    }

                   isFixed &&  range.select()

                }
            }

            if ( keyCode == 8 || keyCode == 46  ) {
                
                var range,body,start,parent,
                    tds = this.currentSelectedArr;
                if(tds && tds.length > 0){
                    for(var i=0,ti;ti=tds[i++];){
                        ti.innerHTML = browser.ie ? ( browser.version < 9 ? '&#65279' : '' ) : '<br/>';

                    }
                    range = new baidu.editor.dom.Range(this.document);
                    range.setStart(tds[0],0).setCursor();
                    if(flag){
                        me.undoManger.save();
                        flag = 0;
                    }
                    //阻止chrome执行默认的动作
                    if(browser.webkit){
                        evt.preventDefault();
                    }
                    return;
                }

                range = me.selection.getRange();
                //ctrl+a 后全部删除做处理
                if ( domUtils.isBody( range.startContainer ) && !range.startOffset ) {
                    body = me.document.body;
                    if ( body.childNodes.length == 1 && body.firstChild.nodeType == 1 && body.firstChild.tagName.toLowerCase() == 'br' && me.options.enterTag == 'p' || me.body.innerHTML == '') {
                        me.document.execCommand( 'formatBlock', false, '<p>' );
                        if ( browser.gecko ) {
                            range = me.selection.getRange();

                            start = domUtils.findParentByTagName( range.startContainer, 'p', true );
                            start && domUtils.removeDirtyAttr( start );
                            //trace:1005
                            range.setStart(start,0).setCursor();
                        }
                        me.undoManger.save();
                        return;

                    }
                }

                //处理删除不干净的问题
                start = range.startContainer;

                while ( start.nodeType == 1 && dtd.$removeEmpty[start.tagName] ) {
                    if ( domUtils.getChildCount( start, function( n ) {
                        return n.nodeType != 1 || n.tagName.toLowerCase() != 'br'
                    } ) > 0 ) {
                        break;
                    }
                    parent = start.parentNode;
                    domUtils.remove( start );
                    start = parent;
                }
                if ( start.nodeType == 1 && start.childNodes.length == 0 ) {

                    //ie下的问题，虽然没有了相应的节点但一旦你输入文字还是会自动把删除的节点加上，
                    if ( browser.ie ) {

                        var span = range.document.createElement('span');
                        start.appendChild(span);
                        range.setStart(span,0).setCursor();

                    }else{
                        var br = range.document.createElement('br');
                        start.appendChild(br);
                        range.setStart(start,0).setCursor();
                    }

                    setTimeout( function() {
                        if(browser.ie){
                            domUtils.remove(span);
                        }
                        //range.setStart( start, 0 ).setCursor();
                        if(flag){
                            me.undoManger.save();
                            flag = 0;
                        }
                    }, 0)
                } else {

                    if(flag){
                        me.undoManger.save();
                        flag = 0;
                    }

                }
            }
        } )
    }
})();
//修复chrome下图片不能点击的问题
//todo 可以改大小
baidu.editor.plugins['fiximgclick'] = function() {
    var me = this,
        browser = baidu.editor.browser;
    if ( browser.webkit ) {
        me.addListener( 'click', function( type, e ) {
            if ( e.target.tagName == 'IMG' ) {
                var range = new baidu.editor.dom.Range( me.document );
                range.selectNode( e.target ).select();

            }
        } )
    }
};
/**
 * @description 为非ie浏览器自动添加a标签
 * @author zhanyi
 */
(function() {

    var editor = baidu.editor,
        browser = editor.browser,
        domUtils = editor.dom.domUtils,
        cont = 0;

    baidu.editor.plugins['autolink'] = function() {
        if (browser.ie) {
            return;
        }
        var me = this;
        me.addListener('keydown', function(type, evt) {
            var keyCode = evt.keyCode || evt.which;

            if (keyCode == 32 || keyCode == 13) {

                var sel = me.selection.getNative(),
                    range = sel.getRangeAt(0).cloneRange(),
                    offset,
                    charCode;

                var start = range.startContainer;
                while (start.nodeType == 1 && range.startOffset > 0) {
                    start = range.startContainer.childNodes[range.startOffset - 1];
                    if (!start)
                        break;

                    range.setStart(start, start.nodeType == 1 ? start.childNodes.length : start.nodeValue.length);
                    range.collapse(true);
                    start = range.startContainer;
                }

                do{
                    if (range.startOffset == 0) {
                        start = range.startContainer.previousSibling;

                        while (start && start.nodeType == 1) {
                            start = start.lastChild;
                        }
                        if (!start)
                            break;
                        offset = start.nodeValue.length;
                    } else {
                        start = range.startContainer;
                        offset = range.startOffset;
                    }
                    range.setStart(start, offset - 1);
                    charCode = range.toString().charCodeAt(0);
                } while (charCode != 160 && charCode != 32);

                if (range.toString().replace(new RegExp(domUtils.fillChar, 'g'), '').match(/^(\s*)(?:https?:\/\/|ssh:\/\/|ftp:\/\/|file:\/|www\.)/i)) {

                    var a = me.document.createElement('a'),text = me.document.createTextNode(' ');
                    //去掉开头的空格
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
                    sel.addRange(range)
                }
            }


        })
    }

})();

/**
 * @description 自动伸展
 * @author zhanyi
 */
(function() {

    var browser = baidu.editor.browser;

    baidu.editor.plugins['autoheight'] = function() {
        var editor = this;
        //提供开关，就算加载也可以关闭
        editor.autoHeightEnabled = this.options.autoHeightEnabled;
        
        var timer;
        var bakScroll;
        var bakOverflow;
        editor.enableAutoHeight = function (){
            var iframe = editor.iframe,
                doc = editor.document,
                minHeight = editor.options.minFrameHeight;

            function updateHeight(){
                editor.setHeight(Math.max( browser.ie ? doc.body.scrollHeight :
                        doc.body.offsetHeight + 20, minHeight ));
            }
            this.autoHeightEnabled = true;
            bakScroll = iframe.scroll;
            bakOverflow = doc.body.style.overflowY;
            iframe.scroll = 'no';
            doc.body.style.overflowY = 'hidden';
            timer = setTimeout(function (){
                updateHeight();
                timer = setTimeout(arguments.callee);
            });
            editor.fireEvent('autoheightchanged', this.autoHeightEnabled);
        };
        editor.disableAutoHeight = function (){
            var iframe = editor.iframe,
                doc = editor.document;
            iframe.scroll = bakScroll;
            doc.body.style.overflowY = bakOverflow;
            clearTimeout(timer);
            this.autoHeightEnabled = false;
            editor.fireEvent('autoheightchanged', this.autoHeightEnabled);
        };
        editor.addListener( 'ready', function() {
            if(this.autoHeightEnabled){
                editor.enableAutoHeight();
            }

        });
    }

})();

(function (){

var dtd = baidu.editor.dom.dtd;
var EMPTY_TAG = dtd.$empty;
var browser = baidu.editor.browser;

var parseHTML = function (){

    var RE_PART = /<(?:(?:\/([^>]+)>\s*)|(?:!--([\S|\s]*?)-->)|(?:([^\s\/>]+)\s*((?:(?:"[^"]*")|(?:'[^']*')|[^"'<>])*)\/?>\s*))/g;
    var RE_ATTR = /([\w\-:.]+)(?:(?:\s*=\s*(?:(?:"([^"]*)")|(?:'([^']*)')|([^\s>]+)))|(?=\s|$))/g;

    var EMPTY_ATTR = {checked:1,compact:1,declare:1,defer:1,disabled:1,ismap:1,multiple:1,nohref:1,noresize:1,noshade:1,nowrap:1,readonly:1,selected:1};
    var CDATA_TAG = {script:1,style: 1};
    var NEED_PARENT_TAG = {
        "li": { "$": 'ul', "ul": 1, "ol": 1 },
        "dd": { "$": "dl", "dl": 1 },
        "dt": { "$": "dl", "dl": 1 },
        "option": { "$": "select", "select": 1 },
        "td": { "$": "tr", "tr": 1 },
        "tr": { "$": "tbody", "tbody": 1, "thead": 1, "tfoot": 1, "table": 1 },
        "tbody": { "$": "table", "table": 1 },
        "thead": { "$": "table", "table": 1 },
        "tfoot": { "$": "table", "table": 1 },
        "col": { "$": "colgroup" }
    };
    var NEED_CHILD_TAG = {
        "table": "td", "tbody": "td", "thead": "td", "tfoot": "td", "tr": "td",
        "colgroup": "col",
        "ul": "li", "ol": "li",
        "dl": "dd",
        "select": "option"
    };

    function parse(html, callbacks){

        var match,
            nextIndex = 0,
            tagName,
            cdata;
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
                    callbacks.onCDATA(cdata.join(''));
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
                var attrPart = match[4],
                    attrMatch,
                    attrMap = {},
                    selfClosing = attrPart && attrPart.slice(-1) == '/';
                if (attrPart) {
                    RE_ATTR.exec("");
                    while ((attrMatch = RE_ATTR.exec(attrPart))) {
                        var attrName = attrMatch[1].toLowerCase(),
                            attrValue = attrMatch[2] || attrMatch[3] || attrMatch[4] || '';
                        if (!attrValue && EMPTY_ATTR[attrName]) {
                            attrValue = attrName;
                        }
                        if (attrName == 'style') {
                            if (browser.ie && browser.version <= 6) {
                                attrValue = attrValue.replace(/(?!;)\s*([\w-]+):/g, function (m, p1){
                                    return p1.toLowerCase() + ':';
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
    return function (html, forceDtd){
        var fragment = {
            type: 'fragment',
            parent: null,
            children: []
        };
        var currentNode = fragment;
        function addChild(node){
            node.parent = currentNode;
            currentNode.children.push(node);
        }
        function addElement(element, open){
            var node = element;
            // 遇到结构化标签的时候
            if (NEED_PARENT_TAG[node.tag]) {
                // 考虑这种情况的时候, 结束之前的标签
                // e.g. <table><tr><td>12312`<tr>`4566
                while (NEED_PARENT_TAG[currentNode.tag] && NEED_PARENT_TAG[currentNode.tag][node.tag]){
                    currentNode = currentNode.parent;
                }
                // 如果前一个标签和这个标签是同一级, 结束之前的标签
                // e.g. <ul><li>123<li>
                if (currentNode.tag == node.tag) {
                    currentNode = currentNode.parent;
                }
                // 向上补齐父标签
                while (NEED_PARENT_TAG[node.tag]) {
                    if (NEED_PARENT_TAG[node.tag][currentNode.tag]) break;
                    node = node.parent = {
                        type: 'element',
                        tag: NEED_PARENT_TAG[node.tag]['$'],
                        attributes: {},
                        children: [node]
                    };
                }
            }
            if (forceDtd) {
                
                // 如果遇到这个标签不能放在前一个标签内部，则结束前一个标签,span单独处理
                while (dtd[node.tag] && !(currentNode.tag == 'span' ? baidu.editor.utils.extend(dtd['strong'],{'a':1,'A':1}) : (dtd[currentNode.tag] || dtd['div']))[node.tag]) {
                    if (tagEnd(currentNode)) continue;
                    if (!currentNode.parent) break;
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
        // 结束一个标签的时候，需要判断一下它是否缺少子标签
        // e.g. <table></table>
        function tagEnd(node){
            var needTag;
            if (!node.children.length && (needTag = NEED_CHILD_TAG[node.tag])) {
                addElement({
                    type: 'element',
                    tag: needTag,
                    attributes: {},
                    children: []
                }, true);
                return true;
            }
            return false;
        }
        parse(html, {
            onText: function (text){
                while (!(dtd[currentNode.tag] || dtd['div'])['#']) {
                    if (tagEnd(currentNode)) continue;
                    currentNode = currentNode.parent;
                }
                // TODO: 注意这里会去掉空白节点
                if (/\S/.test(text)) {
                    addChild({
                        type: 'text',
                        data: text
                    });
                }
            },
            onComment: function (text){
                addChild({
                    type: 'comment',
                    data: text
                });
            },
            onCDATA: function (text){
                while (!(dtd[currentNode.tag] || dtd['div'])['#']) {
                    if (tagEnd(currentNode)) continue;
                    currentNode = currentNode.parent;
                }
                addChild({
                    type: 'cdata',
                    data: text
                });
            },
            onTagOpen: function (tag, attrs, closed){
                closed = closed || EMPTY_TAG[tag];
                addElement({
                    type: 'element',
                    tag: tag,
                    attributes: attrs,
                    closed: closed,
                    children: []
                }, !closed);
            },
            onTagClose: function (tag){
                var node = currentNode;
                // 向上找匹配的标签, 这里不考虑dtd的情况是因为tagOpen的时候已经处理过了, 这里不会遇到
                while (node && tag != node.tag) {
                    node = node.parent;
                }
                if (node) {
                    // 关闭中间的标签
                    for (var tnode=currentNode; tnode!==node.parent; tnode=tnode.parent) {
                        tagEnd(tnode);
                    }
                    currentNode = node.parent;
                } else {
                    // 如果没有找到开始标签, 则创建新标签
                    // eg. </div> => <div></div>
                    node = {
                        type: 'element',
                        tag: tag,
                        attributes: {},
                        children: []
                    };
                    addElement(node, true);
                    tagEnd(node);
                    currentNode = node.parent;
                }
            }
        });
        // 处理这种情况, 只有开始标签没有结束标签的情况, 需要关闭开始标签
        // eg. <table>
        while (currentNode !== fragment) {
            tagEnd(currentNode);
            currentNode = currentNode.parent;
        }
        
        return fragment;
    };
}();
var unhtml1 = function (){
    var map = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' };
    function rep( m ){ return map[m]; }
    return function ( str ) {
        return str ? str.replace( /[<>"']/g, rep ) : '';
    };
}();
var toHTML = function (){
    function printChildren(node){
        var children = node.children;
       
        var buff = [];
        for (var i=0,ci; ci = children[i]; i++) {

            buff.push(toHTML(ci));
        }
        return buff.join('');
    }
    function printAttrs(attrs){
        var buff = [];
        for (var k in attrs) {
            buff.push(k + '="' + unhtml1(attrs[k]) + '"');
        }
        return buff.join(' ');
    }
    function printData(node){ return unhtml1(node.data); }
    function printElement(node){
        var tag = node.tag;
        var attrs = printAttrs(node.attributes);
        var html = '<' + tag + (attrs ? ' ' + attrs : '') + (EMPTY_TAG[tag] ? ' />' : '>');
        if (!EMPTY_TAG[tag]) {
            html += printChildren(node);
            html += '</' + tag + '>';
        }
        return html;
    }

    return function (node){
        if (node.type == 'fragment') {
            return printChildren(node);
        } else if (node.type == 'element') {
            return printElement(node);
        } else if (node.type == 'text' || node.type == 'cdata') {
            return printData(node);
        } else if (node.type == 'comment') {
            return '<!--' + node.data + '-->';
        }
        return '';
    };
}();

/////////////////
// WORD /////////
/////////////////
var transformWordHtml = function (){

    function isWordDocument( strValue ) {
        var re = new RegExp( /(class="?Mso|style="[^"]*\bmso\-|w:WordDocument)/ig );
        return re.test( strValue );
    }

    function ensureUnits( v ) {
        v = v.replace(/([\d.]+)([\w]+)?/g, function (m, p1, p2){
            return (Math.round(parseFloat(p1)) || 1) + (p2 || 'px');
        });
        return v;
    }

    function filterPasteWord( str ) {
        str = str.replace( /<!--\s*EndFragment\s*-->[\s\S]*$/, '' );
        //remove link break
        str = str.replace( /\r\n|\n|\r/ig, "" );
        //remove &nbsp; entities at the start of contents
        str = str.replace( /^\s*(&nbsp;)+/ig, "" );
        //remove &nbsp; entities at the end of contents
        str = str.replace( /(&nbsp;|<br[^>]*>)+\s*$/ig, "" );
        // Word comments like conditional comments etc
        str = str.replace( /<!--[\s\S]*?-->/ig, "" );
        // Remove comments, scripts (e.g., msoShowComment), XML tag, VML content, MS Office namespaced tags, and a few other tags
        str = str.replace( /<(!|script[^>]*>.*?<\/script(?=[>\s])|\/?(\?xml(:\w+)?|xml|img|meta|link|style|\w+:\w+)(?=[\s\/>]))[^>]*>/gi, "" );

        //convert word headers to strong
        str = str.replace( /<p [^>]*class="?MsoHeading"?[^>]*>(.*?)<\/p>/gi, "<p><strong>$1</strong></p>" );
        //remove lang attribute
        str = str.replace( /(lang)\s*=\s*([\'\"]?)[\w-]+\2/ig, "" );
        // Examine all styles: delete junk, transform some, and keep the rest
        str = str.replace( /(<[a-z][^>]*)\sstyle="([^"]*)"/gi, function( str, tag, style ) {
            var n = [],
                    i = 0,
                    s = style.replace( /^\s+|\s+$/, '' ).replace( /&quot;/gi, "'" ).split( /;\s*/g );

            // Examine each style definition within the tag's style attribute
            for ( var i = 0; i < s.length; i++ ) {
                var v = s[i];
                var name, value,
                        parts = v.split( ":" );

                if ( parts.length == 2 ) {
                    name = parts[0].toLowerCase();
                    value = parts[1].toLowerCase();
                    // Translate certain MS Office styles into their CSS equivalents
                    switch ( name ) {
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
    //                        case "mso-border-alt":
    //                        case "mso-border-top-alt":
    //                        case "mso-border-right-alt":
    //                        case "mso-border-bottom-alt":
    //                        case "mso-border-left-alt":
                        case "mso-table-layout-alt":
                        case "mso-height":
                        case "mso-width":
                        case "mso-vertical-align-alt":
                            n[i++] = name.replace( /^mso-|-alt$/g, "" ) + ":" + ensureUnits( value );
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
                            n[i++] = "min-height:" + ensureUnits( value );
                            continue;

                        case "mso-default-width":
                            n[i++] = "min-width:" + ensureUnits( value );
                            continue;

                        case "mso-padding-between-alt":
                            n[i++] = "border-collapse:separate;border-spacing:" + ensureUnits( value );
                            continue;

                        case "text-line-through":
                            if ( (value == "single") || (value == "double") ) {
                                n[i++] = "text-decoration:line-through";
                            }
                            continue;

                        case "mso-zero-height":
                            if ( value == "yes" ) {
                                n[i++] = "display:none";
                            }
                            continue;
                    }

                    if ( /^(mso|column|font-emph|lang|layout|line-break|list-image|nav|panose|punct|row|ruby|sep|size|src|tab-|table-border|text-(?:align|decor|indent|trans)|top-bar|version|vnd|word-break)/.test( name ) ) {
                            if(!/mso-list/.test(name))
                            continue;
                    }
                    // If it reached this point, it must be a valid CSS style
                    n[i] = name + ":" + parts[1];        // Lower-case name, but keep value case
                }
            }
            // If style attribute contained any valid styles the re-write it; otherwise delete style attribute.
            if ( i > 0 ) {
                return tag + ' style="' + n.join( ';' ) + '"';
            } else {
                return tag;
            }
        } );
        str = str.replace( /([ ]+)<\/span>/ig, function ( m, p ) {
            return new Array( p.length + 1 ).join( '&nbsp;' ) + '</span>';
        } );

        return str;
    }
    return function (html){

        //过了word,才能转p->li
        first = null;
        parentTag = '',liStyle = '',firstTag = '';
        if (isWordDocument(html)) {
            html = filterPasteWord(html);
        }
        return html.replace(/>\s*</g,'><');
    };
}();
var NODE_NAME_MAP = {
    'text': '#text',
    'comment': '#comment',
    'cdata': '#cdata-section',
    'fragment': '#document-fragment'
};

function _likeLi(node){
    var a;
    if (node && node.tag == 'p') {
    //office 2011下有效
        if(node.attributes['class'] == 'MsoListParagraph' || /mso-list/.test(node.attributes.style)) {
            a = 1;
        } else {
            var firstChild = node.children[0];
            if (firstChild && firstChild.tag == 'span' && /Wingdings/i.test(firstChild.attributes.style)) {
                a = 1;
            }
        }
    }
    return a;
}
//为p==>li 做个标志
var first,
    orderStyle = {
        'decimal' : /\d+/,
        'lower-roman': /^m{0,4}(cm|cd|d?c{0,3})(xc|xl|l?x{0,3})(ix|iv|v?i{0,3})$/,
        'upper-roman': /^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/,
        'lower-alpha' : /^\(?[a-z]+\)?$/,
        'upper-alpha': /^\(?[A-Z]+\)?$/
    },
    unorderStyle = { 'disc' : /^[l\u00B7\u2002]/, 'circle' : /^[\u006F\u00D8]/,'square' : /^[\u006E\u25C6]/},
    parentTag = '',liStyle = '',firstTag;

    
//b|i|font ==> strong|em|span 放在toHTML节省在遍历的时间
function transNode(node){
    if(node.type == 'element' && !node.children.length && dtd.$removeEmpty[node.tag] && node.tag != 'a'){// 锚点保留
        return {
            type : 'fragment',
            children:[]
        }
    }
    var sizeMap = [0, 10, 12, 16, 18, 24, 32, 48],
        attr,
        indexOf = baidu.editor.utils.indexOf;
    
    switch(node.tag){
        case 'a'://锚点，a==>img
            if(node.attributes['name']){
                node.tag = 'img';
                node.attributes = {
                    'class' : 'anchorclass',
                    'anchorname':node.attributes['name']
                }
                node.closed = 1;
            }
            break;
        case 'b':
            node.tag = node.name = 'strong';
            break;
        case 'i':
            node.tag = node.name = 'em';
            break;
        case 'u':
            node.tag = node.name = 'span';
            node.attributes.style = (node.attributes.style || '') + ';text-decoration:underline;';
            break;
        case 's':
        case 'del':
            node.tag = node.name = 'span';
            node.attributes.style = (node.attributes.style || '') + ';text-decoration:line-through;';
            break;
        case 'span':
            if(/mso-list/.test(node.attributes.style)){
                //判断了两次就不在判断了
               if(firstTag != 'end'){

                    var ci = node.children[0],p;
                    while(ci.type == 'element'){
                        ci = ci.children[0];
                    }
                    for(p in unorderStyle){
                        if(unorderStyle[p].test(ci.data)){
                           // ci.data = ci.data.replace(unorderStyle[p],'');
                            parentTag = 'ul';
                            liStyle = p;
                            break;
                        }
                    }


                    if(!parentTag){
                       for( p in orderStyle){
                            if(orderStyle[p].test(ci.data.replace(/\.$/,''))){
                              //   ci.data = ci.data.replace(orderStyle[p],'');
                                parentTag = 'ol';
                                liStyle = p;
                                break;
                            }
                        }
                    }
                    if(firstTag){
                        if(ci.data == firstTag){
                            if(parentTag != 'ul'){
                                liStyle = '';
                            }
                            parentTag = 'ul'
                        }else{
                             if(parentTag != 'ol'){
                                liStyle = '';
                            }
                            parentTag = 'ol'
                        }
                        firstTag = 'end'
                    }else{
                        firstTag = ci.data
                    }
                    if(parentTag){
                        var tmpNode = node;
                        while(tmpNode.tag != 'ul' && tmpNode.tag != 'ol'){
                            tmpNode = tmpNode.parent;
                        }
                        tmpNode.tag = parentTag;
                        tmpNode.attributes.style = 'list-style-type:' + liStyle;


                    }

               }

                node = {
                    type : 'fragment',
                    children : []
                }
                break;



            }
            var style = node.attributes.style;
            if(style){
                style = style.replace(/background(?!-)/g, 'background-color');
                style = style.match(/(?:\b(?:color|font-size|background-color|font-size|font-family|text-decoration)\b\s*:\s*(&[^;]+;|[^;])+(?=;)?)/gi);
                if(style){
                    node.attributes.style = style.join(';');
                    if(!node.attributes.style){
                        delete node.attributes.style;
                    }
                }
            }
            break;
        case 'font':
            node.tag = node.name = 'span';
            attr = node.attributes;
            node.attributes = {
                'style': (attr.size ? 'font-size:' + (sizeMap[attr.size] || 12) + 'px' : '')
                + ';' + (attr.color ? 'color:'+ attr.color : '')
                + ';' + (attr.face ? 'font-family:'+ attr.face : '')
                + ';' + (attr.style||'')
            };
            break;
        case 'p':
            if (node.attributes.align) {
                node.attributes.style = (node.attributes.style || '') + ';text-align:' +
                    node.attributes.align + ';';
                delete node.attributes.align;
            }
            if(_likeLi(node)){

                if(!first){

                    var ulNode  = {
                        type: 'element',
                        tag: 'ul',
                        attributes: {},
                        children: []
                    },
                    index = indexOf(node.parent.children,node);
                    node.parent.children[index] = ulNode;
                    ulNode.parent = node.parent;
                    ulNode.children[0] = node;
                    node.parent = ulNode;

                    while(1){
                        node = ulNode.parent.children[index+1];
                        if( _likeLi(node)){
                            ulNode.children[ulNode.children.length] = node;
                            node.parent = ulNode;
                            ulNode.parent.children.splice(index+1,1)
                        }else{
                            break;
                        }
                    }

                    return ulNode;
                }
                node.tag = node.name = 'li';

                delete node.attributes['class'];
                delete node.attributes.style
            }
    }
    return node;
}
function transOutNode(node){

    switch (node.tag){
        case 'td':
            if (/display\s*:\s*none/i.test(node.attributes.style)) {
                return {
                    type: 'fragment',
                    children: []
                };
            }
            if(browser.ie && !node.children.length ){
                 var txtNode  = {
                        type: 'text',
                        data:'&nbsp;',
                        parent : node
                    };
                    node.children[0] = txtNode;
            }
            break;
        case 'img'://锚点，img==>a
            if(node.attributes.anchorname){
                node.tag = 'a';
                node.attributes = {
                    name : node.attributes.anchorname
                }
                node.closed = null;
            }
       

    }

    return node;
}
function childrenAccept(node, visit, ctx){
    if (!node.children || !node.children.length) {
        return node;
    }
    var children = node.children;
    for (var i=0; i<children.length; i++) {
        var newNode = visit(children[i], ctx);
        if (newNode.type == 'fragment') {
            var args = [i, 1];
            args.push.apply(args, newNode.children);
            children.splice.apply(children, args);
            //节点为空的就干掉，不然后边的补全操作会添加多余的节点
            if(!children.length){
                node = {
                    type: 'fragment',
                    children: []
                }
            }
            i --;
        } else {
            children[i] = newNode;
        }
    }
    return node;
}
function Serialize(rules){
    this.rules = rules;
}
Serialize.prototype = {
    // NOTE: selector目前只支持tagName
    rules: null,
    // NOTE: node必须是fragment
    filter: function (node, rules){
        rules = rules || this.rules;
        var whiteList = rules && rules.whiteList;
        var blackList = rules && rules.blackList;
        function visitNode(node, parent){
            node.name = node.type == 'element' ?
                    node.tag : NODE_NAME_MAP[node.type];
            if (parent == null) {
                return childrenAccept(node, visitNode, node);
            }
            if (blackList && blackList[node.name]) {
                return {
                    type: 'fragment',
                    children: []
                };
            }
            if (whiteList) {
                if (node.type == 'element') {
                    if (parent.type == 'fragment' ? whiteList[node.name] : whiteList[node.name] && whiteList[parent.name][node.name]) {
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
                        node.type = 'fragment';
                        // NOTE: 这里算是一个hack
                        node.name = parent.name;
                    }
                } else {
                    // NOTE: 文本默认允许
                }
            }
            if (blackList || whiteList) {
                childrenAccept(node, visitNode, node);
            }
            return node;
        }
        return visitNode(node, null);
    },
    transformInput: function (node, wrapInline){
        function visitNode(node){
            node = transNode(node);
            if(node.tag == 'ol' || node.tag == 'ul'){
                first = 1;
            }
            node = childrenAccept(node, visitNode, node);
            if(node.tag == 'ol' || node.tag == 'ul'){
                first = 0;
                parentTag = '',liStyle = '',firstTag = '';
            }
            return node;
        }
        return visitNode(node);
    },
    transformOutput: function (node){
        function visitNode(node){
            node = transOutNode(node);
            if(node.tag == 'ol' || node.tag == 'ul'){
                first = 1;
            }
            node = childrenAccept(node, visitNode, node);
            if(node.tag == 'ol' || node.tag == 'ul'){
                first = 0;
            }
            return node;
        }
        return visitNode(node);
    },
    toHTML: toHTML,
    parseHTML: parseHTML,
    word: transformWordHtml
};
baidu.editor.serialize = new Serialize({});

baidu.editor.plugins['serialize'] = function () {
    var editor = this;
    editor.serialize = new Serialize(editor.options.serialize);
};

})();

(function (){
    baidu.editor.plugins['video'] = function (){
        var editor = this;
        var fakedMap = {};
        var fakedPairs = [];
        var lastFakedId = 0;
        function fake(url, width, height,style){
            var fakedId = 'edui_faked_video_' + (lastFakedId ++);
            var fakedHtml = '<img isfakedvideo id="'+ fakedId +'" width="'+ width +'" height="' + height + '" _url="'+url+'" class="edui-faked-video"' +
                ' src="http://hi.baidu.com/fc/editor/images/spacer.gif"' +
                ' style="background:url(http://hi.baidu.com/ui/neweditor/lib/fck/images/fck_videologo.gif) no-repeat center center; border:1px solid gray;'+ style +';" />';
            fakedMap[fakedId] = '<embed isfakedvideo' +
                ' type="application/x-shockwave-flash"' +
                ' pluginspage="http://www.macromedia.com/go/getflashplayer"' +
                ' src="' + url + '"' +
                ' width="' + width + '"' +
                ' height="' + height + '"' +
                ' wmode="transparent"' +
                ' play="true"' +
                ' loop="false"' +
                ' menu="false"' +
                ' allowscriptaccess="never"' +
                '></embed>';
            return fakedHtml;
        }
        editor.commands['insertvideo'] = {
            execCommand: function (cmd, options){
                var url = options.url;
                var width = options.width || 320;
                var height = options.height || 240;
                var style = options.style ? options.style : "";
                editor.execCommand('inserthtml', fake(url, width, height,style));
            }
        };
        //获得style里的某个样式对应的值
        function getPars(str,par){
            var reg = new RegExp(par+":\\s*((\\w)*)","ig");
            var arr = reg.exec(str);
            return arr ? arr[1] : "";
        }

        editor.addListener('beforegetcontent', function (){
            var tempDiv = editor.document.createElement('div');
            var newFakedMap = {};
            for (var fakedId in fakedMap) {
                var fakedImg;
                while ((fakedImg = editor.document.getElementById(fakedId))) {
                    tempDiv.innerHTML = fakedMap[fakedId];
                    var temp = tempDiv.firstChild;
                    temp.width = fakedImg.width;
                    temp.height = fakedImg.height;
                    var strcss = fakedImg.style.cssText;
                    if(/float/ig.test(strcss)){
                        if(!!window.ActiveXObject){
                            temp.style.styleFloat = getPars(strcss,"float");
                        }else{
                            temp.style.cssFloat = getPars(strcss,"float");
                        }
                    }else if(/display/ig.test(strcss)){
                        temp.style.display = getPars(strcss,"display");
                    }
                    fakedImg.parentNode.replaceChild(temp, fakedImg);
                    fakedPairs.push([fakedImg, temp]);
                    newFakedMap[fakedId] = fakedMap[fakedId];
                }
            }
            fakedMap = newFakedMap;
        });

        editor.addListener('aftersetcontent', function (){
            var tempDiv = editor.document.createElement('div');
            fakedMap = {};
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
                var style = getPars(strcss,"display") ? "display:"+getPars(strcss,"display") : "float:"+getPars(strcss,"float");
                tempDiv.innerHTML = fake(url, width, height,style);
                embeds[k].parentNode.replaceChild(tempDiv.firstChild, embeds[k]);
            }
        });
        editor.addListener('aftergetcontent', function (){
            for (var i=0; i<fakedPairs.length; i++) {
                var fakedPair = fakedPairs[i];
                fakedPair[1].parentNode.replaceChild(fakedPair[0], fakedPair[1]);
            }
            fakedPairs = [];
        });

    };
})();

/**
 * Created by .
 * User: taoqili
 * Date: 11-5-5
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */

(function(){
    var editor = baidu.editor,
        browser = editor.browser,
        domUtils = editor.dom.domUtils,
        keys= domUtils.keys,
        clearSelectedTd = domUtils.clearSelectedArr;
    //框选时用到的几个全局变量
    var anchorTd,
        tableOpt,
        tdStyle,
        tableStyle,
        _isEmpty = domUtils.isEmptyNode;
    function getIndex(cell){
        var cells = cell.parentNode.cells;
        for(var i=0,ci;ci=cells[i];i++){
            if(ci===cell){
                return i;
            }
        }
    }

    /**
     * 判断当前单元格是否处于隐藏状态
     * @param cell 待判断的单元格
     * @return {Boolean} 隐藏时返回true，否则返回false
     */
    function _isHide(cell) {
        return cell.style.display == "none";
    }

    function getCount(arr){
        var count = 0;
        for(var i=0,ti;ti=arr[i++];){
            if(!_isHide(ti)){
                count++
            }

        }
        return count;
    }
    /**
     * table操作插件
     */
    baidu.editor.plugins['table'] = function() {
        var me = this;

        me.currentSelectedArr =  [];
        me.addListener('mousedown', _mouseDownEvent);
        me.addListener('keydown', function(type,evt){
            var keyCode = evt.keyCode || evt.which;
            if( !keys[keyCode] && !evt.ctrlKey && !evt.metaKey && !evt.shiftKey && !evt.altKey){
                clearSelectedTd(me.currentSelectedArr)
            }
        });
        me.addListener('mouseup', function(type,evt) {
            
            anchorTd = null;
            me.removeListener('mouseover', _mouseDownEvent);
            var td = me.currentSelectedArr[0];
            if(td){
                me.document.body.style.webkitUserSelect = '';
                var range = new baidu.editor.dom.Range(me.document);
                if(_isEmpty(td)){
                    range.setStart(me.currentSelectedArr[0],0).setCursor()
                }else{
                   range.selectNodeContents(me.currentSelectedArr[0]).select()
                }
               
            }else{
                
                //浏览器能从table外边选到里边导致currentSelectedArr为空，清掉当前选区回到选区的最开始

                    var range = me.selection.getRange().shrinkBoundary();

                    if(!range.collapsed){
                        var start = domUtils.findParentByTagName(range.startContainer,'td',true),
                            end = domUtils.findParentByTagName(range.endContainer,'td',true);
                        //在table里边的不能清除
                        if(start && !end || !start && end || start && end && start !== end){
                            range.collapse(true).select(true)
                        }
                    }


                

            }

        });

        function reset (){
            me.currentSelectedArr = [];
            anchorTd = null;
           
        }
        
        /**
         * 插入表格
         * @param numRows 行数
         * @param numCols 列数
         */
       me.commands['inserttable'] = {
            queryCommandState: function () {
                var range = this.selection.getRange();

                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                        || domUtils.findParentByTagName(range.endContainer, 'table', true)
                        || me.currentSelectedArr.length > 0 ? -1 : 0;
            },
            execCommand: function (cmdName, tableobj) {
                tableOpt = tableobj;
                var arr = [];
                
                arr.push("cellpadding='"+(tableobj.cellpadding||0)+"'");
                arr.push("cellspacing='"+(tableobj.cellspacing||0)+"'");
              
                tableobj.width ? arr.push("width='"+tableobj.width+"'") : arr.push("width='500'");
                tableobj.height ? arr.push("height='"+tableobj.height+"'") : arr.push("height='100'");

                tdStyle = 'vertical-align:top;padding:2px;border:'+(tableobj.cellborder||1) +'px solid #000;width:'+Math.floor((tableobj.width||500)/tableobj.numCols)+'px;height:20px;text-align: '+(tableobj.align||'left')+';';
                var html,rows = [],j = tableobj.numRows;
                if (j) while (j --) {
                    var cols = [];
                    var k = tableobj.numCols;
                    while (k --) {

                        cols[k] = '<td style="'+tdStyle+'">' +
                                (browser.ie ? '' : '<br/>') + '</td>';
                    }
                    rows.push('<tr>' + cols.join('') + '</tr>');
                }
                
                html = '<table _baidu_table="true" '+arr.join(" ")+' style="margin-bottom:10px;'+(tableobj.cellpadding||tableobj.cellspacing ? '':'border-collapse:collapse;')+'border:'+(tableobj.border||1)+'px solid #000;">' + rows.join('') + '</table>';
                this.execCommand('insertHtml',html);
               
                reset();
            }
        };

        /**
         * 删除表格
         */
        me.commands['deletetable'] = {
            queryCommandState:function() {
                var range = this.selection.getRange();
                return (domUtils.findParentByTagName(range.startContainer, 'table', true)
                        && domUtils.findParentByTagName(range.endContainer, 'table', true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
            },
            execCommand:function() {
                var range = this.selection.getRange(),
                    table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, 'table',true);
                 var p = table.ownerDocument.createElement('p');
                    p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                    table.parentNode.insertBefore(p,table)
                    domUtils.remove(table);
                    range.setStart(p,0).setCursor();
                    domUtils.remove(table);

                reset();
            }
        };


        /**
         * 表格样式设置
         */
        me.commands['settablestyle'] = {
            queryCommandState:function() {
                var range = this.selection.getRange();
                return (domUtils.findParentByTagName(range.startContainer, 'table', true)
                        && domUtils.findParentByTagName(range.endContainer, 'table', true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
            },

            //opt:json{key:value}
            execCommand:function(cmdName,getOpt) {


                var range = this.selection.getRange(),
                    table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, 'table', true),
                    css = table.style.cssText;
                
                //opt={border:"5px solid red",background:"blue"};
                var opt = typeof(getOpt)=='function'? getOpt():getOpt;
                for (var i in opt ) {
                    if(i=="border-spacing"){
                        table['cellSpacing'] = opt[i];
                        continue;
                    }
                    if(i=="width"){
                        table['width'] = opt[i];
                        continue;
                    }
                    if(i=="background"){
                        table['bgColor'] = opt[i];
                        continue;
                    }
                    css += i + ":"+ opt[i] +";"
                }
                table.style.cssText = css;

            }
        };

        /**
         * 添加表格标题
         */
        me.commands['addcaption'] = {
            queryCommandState:function() {
                var range = this.selection.getRange();
                return (domUtils.findParentByTagName(range.startContainer, 'table', true)
                        && domUtils.findParentByTagName(range.endContainer, 'table', true)) || me.currentSelectedArr.length > 0 ? 0 : -1;
            },
            execCommand:function(cmdName,opt) {

                var range = this.selection.getRange(),
                    table = domUtils.findParentByTagName(me.currentSelectedArr.length > 0 ? me.currentSelectedArr[0] : range.startContainer, 'table', true);

                if(opt=="on"){
                    var c = table.createCaption();
                    c.innerHTML = "请在此输入表格标题";
                }else{
                    table.removeChild(table.caption);
                }



            }
        };



        /**
         * 向右合并单元格
         */
        me.commands['mergeright'] = {
            queryCommandState : function() {
                var range = this.selection.getRange(),
                     start = range.startContainer,
                     td = domUtils.findParentByTagName(start, ['td','th'], true);


                if( !td || this.currentSelectedArr.length > 1 )return -1;

                var  tr = td.parentNode;

               //最右边行不能向右合并
                var rightCellIndex = getIndex(td) + td.colSpan;
                if (rightCellIndex >= tr.cells.length) {
                    return -1;
                }
                //单元格不在同一行不能向右合并
                var rightCell = tr.cells[rightCellIndex];
                if (_isHide(rightCell)) {
                    return -1;
                }
                return td.rowSpan == rightCell.rowSpan ? 0 : -1;
            },
            execCommand : function() {
                
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0],
                    tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                //找到当前单元格右边的未隐藏单元格
                var rightCellRowIndex = tr.rowIndex,
                    rightCellCellIndex = getIndex(td) + td.colSpan,
                    rightCell = rows[rightCellRowIndex].cells[rightCellCellIndex];

                //在隐藏的原生td对象上增加两个属性，分别表示当前td对应的真实td坐标
                for(var i = rightCellRowIndex; i < rightCellRowIndex + rightCell.rowSpan; i++){
                    for(var j = rightCellCellIndex; j < rightCellCellIndex + rightCell.colSpan; j++){
                        var tmpCell = rows[i].cells[j];
                        tmpCell.setAttribute('rootRowIndex',tr.rowIndex);
                        tmpCell.setAttribute('rootCellIndex',getIndex(td));

                    }
                }
                //合并单元格
                td.colSpan += rightCell.colSpan || 1;
                //合并内容
                _moveContent(td, rightCell);
                //删除被合并的单元格，此处用隐藏方式实现来提升性能
                rightCell.style.display = "none";
                //重新让单元格获取焦点
                range.setStart(td, 0).setCursor();
            }
        };

        /**
         * 向下合并单元格
         */
        me.commands['mergedown'] = {
            queryCommandState : function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true);

                if( !td || getCount(me.currentSelectedArr) > 1)return -1;



                var tr = td.parentNode,
                    table = tr.parentNode.parentNode,
                    rows = table.rows;

                //已经是最底行,不能向下合并
                var downCellRowIndex = tr.rowIndex + td.rowSpan;
                if (downCellRowIndex >= rows.length) {
                    return -1;
                }

                //如果下一个单元格是隐藏的，表明他是由左边span过来的，不能向下合并
                var downCell = rows[downCellRowIndex].cells[getIndex(td)];
                if (_isHide(downCell)) {
                    return -1;
                }

                //只有列span都相等时才能合并
                return td.colSpan == downCell.colSpan ? 0 : -1;
            },
            execCommand : function() {
                
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true)|| me.currentSelectedArr[0];

                

                var tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                var downCellRowIndex = tr.rowIndex + td.rowSpan,
                    downCellCellIndex = getIndex(td),
                    downCell = rows[downCellRowIndex].cells[downCellCellIndex];

                //找到当前列的下一个未被隐藏的单元格
                for (var i = downCellRowIndex; i < downCellRowIndex + downCell.rowSpan; i++) {
                    for(var j = downCellCellIndex; j< downCellCellIndex + downCell.colSpan; j++) {
                        var tmpCell = rows[i].cells[j];


                        tmpCell.setAttribute('rootRowIndex',tr.rowIndex);
                        tmpCell.setAttribute('rootCellIndex',getIndex(td));
                    }
                }
                //合并单元格
                td.rowSpan += downCell.rowSpan || 1;
                //合并内容
                _moveContent(td, downCell);
                //删除被合并的单元格，此处用隐藏方式实现来提升性能
                downCell.style.display = "none";
                //重新让单元格获取焦点
                range.setStart(td, 0).setCursor();
            }
        };

        /**
         * 删除行
         */
        me.commands['deleterow'] = {
            queryCommandState : function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                if(!td && me.currentSelectedArr.length == 0 )return -1;

            },
            execCommand : function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true),
                    tr,
                    table,
                    cells,
                    rows ,
                    rowIndex ,
                    cellIndex;
                
                if(td && me.currentSelectedArr.length == 0){
                    var count = (td.rowSpan || 1) - 1;

                    me.currentSelectedArr.push(td);
                    tr = td.parentNode,
                    table = tr.parentNode.parentNode;

                    rows = table.rows,
                    rowIndex = tr.rowIndex + 1,
                    cellIndex = getIndex(td);

                    while(count){

                        me.currentSelectedArr.push(rows[rowIndex].cells[cellIndex]);
                        count--;
                        rowIndex++
                    }
                }

               while(td = me.currentSelectedArr.pop()){

                    if(!domUtils.findParentByTagName(td,'table')  ){//|| _isHide(td)
                       
                        continue;
                    }
                    tr = td.parentNode,
                    table = tr.parentNode.parentNode;
                    cells = tr.cells,
                    rows = table.rows,
                    rowIndex = tr.rowIndex,
                    cellIndex = getIndex(td);
                    /*
                     * 从最左边开始扫描并隐藏当前行的所有单元格
                     * 若当前单元格的display为none,往上找到它所在的真正单元格，获取colSpan和rowSpan，
                     *  将rowspan减一，并跳转到cellIndex+colSpan列继续处理
                     * 若当前单元格的display不为none,分两种情况：
                     *  1、rowspan == 1 ，直接设置display为none，跳转到cellIndex+colSpan列继续处理
                     *  2、rowspan > 1  , 修改当前单元格的下一个单元格的display为"",
                     *    并将当前单元格的rowspan-1赋给下一个单元格的rowspan，当前单元格的colspan赋给下一个单元格的colspan，
                     *    然后隐藏当前单元格，跳转到cellIndex+colSpan列继续处理
                     */
                    for (var currentCellIndex = 0; currentCellIndex < cells.length;) {
                        var currentNode = cells[currentCellIndex];
                        if (_isHide(currentNode)) {
                            var topNode = rows[currentNode.getAttribute('rootRowIndex')].cells[currentNode.getAttribute('rootCellIndex')];
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
                    //完成更新后再删除外层包裹的tr
                    domUtils.remove(tr);

                    //重新定位焦点
                    var topRowTd, focusTd, downRowTd;

                    if (rowIndex == rows.length) { //如果被删除的行是最后一行,这里之所以没有-1是因为已经删除了一行
                        //如果删除的行也是第一行，那么表格总共只有一行，删除整个表格
                        if (rowIndex == 0) {
                            var p = table.ownerDocument.createElement('p');
                            p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                            table.parentNode.insertBefore(p,table)
                            domUtils.remove(table);
                            range.setStart(p,0).setCursor();
                           
                            return;
                        }
                        //如果上一单元格未隐藏，则直接定位，否则定位到最近的上一个非隐藏单元格
                        var preRowIndex = rowIndex - 1;
                        topRowTd = rows[preRowIndex].cells[ cellIndex];
                        focusTd = _isHide(topRowTd) ? rows[topRowTd.getAttribute('rootRowIndex')].cells[topRowTd.getAttribute('rootCellIndex')]: topRowTd;

                    } else { //如果被删除的不是最后一行，则光标定位到下一行,此处未加1是因为已经删除了一行

                        downRowTd = rows[rowIndex].cells[cellIndex];
                        focusTd = _isHide(downRowTd) ?  rows[downRowTd.getAttribute('rootRowIndex')].cells[downRowTd.getAttribute('rootCellIndex')] : downRowTd;
                    }
                }


                range.setStart(focusTd, 0).setCursor();
                update(table)
            }
        };

        /**
         * 删除列
         */
        me.commands['deletecol'] = {
            queryCommandState:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                 if(!td && me.currentSelectedArr.length == 0 )return -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);


                if(td && me.currentSelectedArr.length == 0){
                    
                    var count = (td.colSpan || 1)-1;

                    me.currentSelectedArr.push(td);
                    while(count){
                        do{
                            td = td.nextSibling
                        }while(td.nodeType == 3);
                        me.currentSelectedArr.push(td);
                        count--;
                    }
                }

                while(td = me.currentSelectedArr.pop()){
                     if(!domUtils.findParentByTagName(td,'table')  ){ //|| _isHide(td)
                        continue;
                     }
                    
                     var tr = td.parentNode,
                        table = tr.parentNode.parentNode,
                        cellIndex = getIndex(td),
                        rows = table.rows,
                        cells = tr.cells,
                        rowIndex = tr.rowIndex;
                    /*
                     * 从第一行开始扫描并隐藏当前列的所有单元格
                     * 若当前单元格的display为none，表明它是由左边Span过来的，
                     *  将左边第一个非none单元格的colSpan减去1并删去对应的单元格后跳转到rowIndex + rowspan行继续处理；
                     * 若当前单元格的display不为none，分两种情况，
                     *  1、当前单元格的colspan == 1 ， 则直接删除该节点，跳转到rowIndex + rowspan行继续处理
                     *  2、当前单元格的colsapn >  1, 修改当前单元格右边单元格的display为"",
                     *      并将当前单元格的colspan-1赋给它的colspan，当前单元格的rolspan赋给它的rolspan，
                     *      然后删除当前单元格，跳转到rowIndex+rowSpan行继续处理
                     */
                    var rowSpan;
                    for (var currentRowIndex = 0; currentRowIndex < rows.length;) {
                        var currentNode = rows[currentRowIndex].cells[cellIndex];
                        if (_isHide(currentNode)) {
                            var leftNode = rows[currentNode.getAttribute('rootRowIndex')].cells[currentNode.getAttribute('rootCellIndex')];
                            //依次删除对应的单元格
                            rowSpan = leftNode.rowSpan;
                            for (var i = 0; i < leftNode.rowSpan; i++) {
                                var delNode = rows[currentRowIndex + i].cells[cellIndex];
                                domUtils.remove(delNode);
                            }
                            //修正被删后的单元格信息
                            leftNode.colSpan--;
                            currentRowIndex += rowSpan;
                        } else {
                            if (currentNode.colSpan == 1) {
                                rowSpan = currentNode.rowSpan;
                                for(var i=currentRowIndex,l = currentRowIndex+currentNode.rowSpan;i<l;i++){
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

                    //重新定位焦点
                    var preColTd, focusTd, nextColTd;
                    if (cellIndex == cells.length) { //如果当前列是最后一列，光标定位到当前列的前一列,同样，这里没有减去1是因为已经被删除了一列
                        //如果当前列也是第一列，则删除整个表格
                        if (cellIndex == 0) {
                            var p = table.ownerDocument.createElement('p');
                            p.innerHTML = browser.ie ? '&nbsp;' : '<br/>';
                            table.parentNode.insertBefore(p,table)
                            domUtils.remove(table);
                            range.setStart(p,0).setCursor();
                            return;
                        }
                        //找到当前单元格前一列中和本单元格最近的一个未隐藏单元格
                        var preCellIndex = cellIndex - 1;
                        preColTd = rows[rowIndex].cells[preCellIndex];
                        focusTd = _isHide(preColTd) ? rows[preColTd.getAttribute('rootRowIndex')].cells[preColTd.getAttribute('rootCellIndex')] : preColTd;

                    } else { //如果当前列不是最后一列，则光标定位到当前列的后一列

                        nextColTd = rows[rowIndex].cells[cellIndex];
                        focusTd = _isHide(nextColTd) ? rows[nextColTd.getAttribute('rootRowIndex')].cells[nextColTd.getAttribute('rootCellIndex')] : nextColTd;
                    }
               }

                range.setStart(focusTd, 0).setCursor();
                update(table)
            }
        };

        /**
         * 完全拆分单元格
         */
        me.commands['splittocells'] = {
            queryCommandState:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true);
                return td && ( td.rowSpan > 1 || td.colSpan > 1 ) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) ==1)? 0 : -1;
            },
            execCommand:function() {
                
                var range = this.selection.getRange(),
                        start = range.startContainer,
                        td = domUtils.findParentByTagName(start, ['td','th'], true),
                        tr = td.parentNode,
                        table = tr.parentNode.parentNode;
                var rowIndex = tr.rowIndex,
                    cellIndex = getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;


                for (var i = 0; i < rowSpan; i++) {
                    for (var j = 0; j < colSpan; j++) {
                        var cell = table.rows[rowIndex + i].cells[cellIndex + j];
                        cell.rowSpan = 1;
                        cell.colSpan = 1;
                        if (_isHide(cell)) {
                            cell.style.display = "";
                            cell.innerHTML = browser.ie ? '' : "<br/>";
                        }
                    }
                }
            }
        };


        /**
         * 将单元格拆分成行
         */
        me.commands['splittorows'] = {
            queryCommandState:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true)||me.currentSelectedArr[0];
                return td && ( td.rowSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) ==1)? 0 : -1;
            },
            execCommand:function() {
              
                var range = this.selection.getRange(),
                        start = range.startContainer,
                        td = domUtils.findParentByTagName(start, 'td', true)||me.currentSelectedArr[0],
                        tr = td.parentNode,
                        rows = tr.parentNode.parentNode.rows;

                var rowIndex = tr.rowIndex,
                    cellIndex =  getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;

                for (var i = 0; i < rowSpan; i++) {
                    var cells = rows[rowIndex + i],
                        cell = cells.cells[cellIndex];
                    cell.rowSpan = 1;
                    cell.colSpan = colSpan;
                    if (_isHide(cell)) {
                        cell.style.display = "";
                        //原有的内容要清除掉
                        cell.innerHTML = browser.ie ? '' : '<br/>'
                    }
                    //修正被隐藏单元格中存储的rootRowIndex和rootCellIndex信息
                    for(var j = cellIndex + 1; j < cellIndex + colSpan; j++) {
                        cell = cells.cells[j];
                     
                        cell.setAttribute('rootRowIndex',rowIndex + i)
                    }

                }
                clearSelectedTd(me.currentSelectedArr);
                this.selection.getRange().setStart(td,0).setCursor();
            }
        };



        /**
         * 在表格前插入行
         */
        me.commands['insertparagraphbeforetable'] = {
            queryCommandState:function() {
                
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, 'td', true)||me.currentSelectedArr[0];
                return  td && domUtils.findParentByTagName(td,'table')? 0 : -1;
            },
            execCommand:function() {

                var range = this.selection.getRange(),
                    start = range.startContainer,
                    table = domUtils.findParentByTagName(start, 'table', true);

                start = me.document.createElement(me.options.enterTag);
                table.parentNode.insertBefore(start,table);
                clearSelectedTd(me.currentSelectedArr);
                if(start.tagName == 'P'){
                    //trace:868 
                    start.innerHTML = browser.ie ? '' : '<br/>';
                    range.setStart(start,0)
                }else{
                    range.setStartBefore(start)
                }
                range.setCursor();

            }
        };

        /**
         * 将单元格拆分成列
         */
        me.commands['splittocols'] = {
            queryCommandState:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true)||me.currentSelectedArr[0];
                return td && ( td.colSpan > 1) && (!me.currentSelectedArr.length || getCount(me.currentSelectedArr) ==1) ? 0 : -1;
            },
            execCommand:function() {
                
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true)||me.currentSelectedArr[0],
                    tr = td.parentNode,
                    rows = tr.parentNode.parentNode.rows;

                var rowIndex = tr.rowIndex,
                    cellIndex =  getIndex(td),
                    rowSpan = td.rowSpan,
                    colSpan = td.colSpan;

                for (var i = 0; i < colSpan; i++) {
                    var cell = rows[rowIndex].cells[cellIndex + i];
                    cell.rowSpan = rowSpan;
                    cell.colSpan = 1;
                    if (_isHide(cell)) {
                        cell.style.display = "";
                        cell.innerHTML = browser.ie ? '' : '<br/>'
                    }

                    for(var j = rowIndex + 1; j < rowIndex + rowSpan; j++ ) {
                       var tmpCell = rows[j].cells[cellIndex + i];
                       tmpCell.setAttribute('rootCellIndex',cellIndex + i);
                    }

                }
                 clearSelectedTd(me.currentSelectedArr);
                this.selection.getRange().setStart(td,0).setCursor();
            }
        };


        /**
         * 插入行
         */
        me.commands['insertrow'] = {
            queryCommandState:function() {
                var range = this.selection.getRange();
                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                        || domUtils.findParentByTagName(range.endContainer, 'table', true) || me.currentSelectedArr.length != 0 ? 0 : -1;
            },
            execCommand:function() {
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    tr = domUtils.findParentByTagName(start, 'tr', true) || me.currentSelectedArr[0].parentNode,
                    table = tr.parentNode.parentNode,
                    rows = table.rows;

                //记录插入位置原来所有的单元格
                var rowIndex = tr.rowIndex,
                    cells = rows[rowIndex].cells;

                //插入新的一行
                var newRow = table.insertRow(rowIndex);

                var newCell;
                //遍历表格中待插入位置中的所有单元格，检查其状态，并据此修正新插入行的单元格状态
                for (var cellIndex = 0; cellIndex < cells.length;) {

                    var tmpCell = cells[cellIndex];

                    if (_isHide(tmpCell)) { //如果当前单元格是隐藏的，表明当前单元格由其上部span过来，找到其上部单元格

                        //找到被隐藏单元格真正所属的单元格
                        var topCell = rows[tmpCell.getAttribute('rootRowIndex')].cells[tmpCell.getAttribute('rootCellIndex')];
                        //增加一行，并将所有新插入的单元格隐藏起来
                        topCell.rowSpan++;
                        for (var i = 0; i < topCell.colSpan; i++) {
                             newCell = tmpCell.cloneNode(false);
                             newCell.rowSpan = newCell.colSpan = 1;
                             newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';

                            if(newRow.children[cellIndex+i]){
                                   newRow.insertBefore(newCell,newRow.children[cellIndex+i]);
                            }else{
                                newRow.appendChild(newCell)
                            }

                            newCell.style.display = "none";
                        }
                        cellIndex += topCell.colSpan;

                    } else {//若当前单元格未隐藏，则在其上行插入colspan个单元格

                        for (var j = 0; j < tmpCell.colSpan; j++) {

                            newCell = tmpCell.cloneNode(false);
                             newCell.rowSpan = newCell.colSpan = 1;
                             newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                             if(newRow.children[cellIndex+j]){
                                   newRow.insertBefore(newCell,newRow.children[cellIndex+j]);
                            }else{
                                newRow.appendChild(newCell)
                            }
                          

                          

                           
                        }
                        cellIndex += tmpCell.colSpan;
                    }
                }
                update(table);
                range.setStart(newRow.cells[0],0).setCursor();

                clearSelectedTd(me.currentSelectedArr);
            }
        };

        /**
         * 插入列
         */
        me.commands['insertcol'] = {
            queryCommandState:function() {
                var range = this.selection.getRange();
                return domUtils.findParentByTagName(range.startContainer, 'table', true)
                        || domUtils.findParentByTagName(range.endContainer, 'table', true) || me.currentSelectedArr.length != 0  ? 0 : -1;
            },
            execCommand:function() {
                
                var range = this.selection.getRange(),
                    start = range.startContainer,
                    td = domUtils.findParentByTagName(start, ['td','th'], true) || me.currentSelectedArr[0],
                    table = domUtils.findParentByTagName(td,'table'),
                    rows =table.rows;

                var cellIndex = getIndex(td),
                    newCell;

                //遍历当前列中的所有单元格，检查其状态，并据此修正新插入列的单元格状态
                for (var rowIndex = 0; rowIndex < rows.length;) {

                    var tmpCell = rows[rowIndex].cells[cellIndex],tr;

                    if (_isHide(tmpCell)) {//如果当前单元格是隐藏的，表明当前单元格由其左边span过来，找到其左边单元格

                        var leftCell = rows[tmpCell.getAttribute('rootRowIndex')].cells[tmpCell.getAttribute('rootCellIndex')];
                        leftCell.colSpan++;
                        for (var i = 0; i < leftCell.rowSpan; i++) {
                            newCell = td.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                             newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                            tr = rows[rowIndex + i];
                            if(tr.children[cellIndex]){
                                 tr.insertBefore(newCell,tr.children[cellIndex]);
                            }else{
                                tr.appendChild(newCell)
                            }

                            newCell.style.display = "none";
                        }
                        rowIndex += leftCell.rowSpan;

                    } else { //若当前单元格未隐藏，则在其左边插入rowspan个单元格

                        for (var j = 0; j < tmpCell.rowSpan; j++) {
                           newCell = td.cloneNode(false);
                            newCell.rowSpan = newCell.colSpan = 1;
                             newCell.innerHTML = browser.ie ? '' : "<br/>";
                            newCell.className = '';
                            tr = rows[rowIndex+j];
                            if(tr.children[cellIndex]){
                                 tr.insertBefore(newCell,tr.children[cellIndex]);
                            }else{
                                tr.appendChild(newCell)
                            }
                            
                            newCell.innerHTML = browser.ie ? '' : "<br/>";
                            
                        }
                        rowIndex += tmpCell.rowSpan;
                    }
                }
                update(table);
                range.setStart(rows[0].cells[cellIndex],0).setCursor();
                clearSelectedTd(me.currentSelectedArr);
            }
        };

        /**
         * 合并多个单元格，通过两个cell将当前包含的所有横纵单元格进行合并
         */
        me.commands['mergecells'] = {
            queryCommandState:function() {
                var count = 0;
                for(var i=0,ti;ti=this.currentSelectedArr[i++];){
                    if(!_isHide(ti))
                        count++;
                }
                return count>1?0:-1;
            },
            execCommand:function() {
                
                var start = me.currentSelectedArr[0],
                    end = me.currentSelectedArr[me.currentSelectedArr.length -1],
                    table = domUtils.findParentByTagName(start,'table'),
                    rows = table.rows,
                    cellsRange = {
                        beginRowIndex:start.parentNode.rowIndex,
                        beginCellIndex:getIndex(start),
                        endRowIndex:end.parentNode.rowIndex,
                        endCellIndex:getIndex(end)
                    },

                    beginRowIndex = cellsRange.beginRowIndex,
                    beginCellIndex = cellsRange.beginCellIndex,
                    rowsLength = cellsRange.endRowIndex - cellsRange.beginRowIndex + 1,
                    cellLength = cellsRange.endCellIndex - cellsRange.beginCellIndex + 1,

                    tmp = rows[beginRowIndex].cells[beginCellIndex];
                
                for (var i = 0, ri; (ri = rows[beginRowIndex + i++]) && i <= rowsLength;) {
                    for (var j = 0, ci; (ci = ri.cells[beginCellIndex + j++]) && j <= cellLength;) {
                        if (i == 1 && j == 1) {
                            ci.style.display = "";
                            ci.rowSpan = rowsLength;
                            ci.colSpan = cellLength;
                        } else {
                            ci.style.display = "none";
                            ci.rowSpan = 1;
                            ci.colSpan = 1;
                            ci.setAttribute('rootRowIndex',beginRowIndex);
                            ci.setAttribute('rootCellIndex',beginCellIndex);
                         
                            //传递内容
                           _moveContent(tmp,ci);
                        }
                    }
                }
                this.selection.getRange().setStart(tmp,0).setCursor();
                clearSelectedTd(me.currentSelectedArr);
            }
        };


       

        /**
         * 将cellFrom单元格中的内容移动到cellTo中
         * @param cellTo  目标单元格
         * @param cellFrom  源单元格
         */
        function _moveContent(cellTo,cellFrom) {

            

            if( _isEmpty(cellFrom) ) return;

            if(_isEmpty(cellTo)){
                cellTo.innerHTML = cellFrom.innerHTML;
                return;
            }
            var child = cellTo.lastChild;
            if(child.nodeType !=1  || child.tagName != 'BR'){
                cellTo.appendChild(cellTo.ownerDocument.createElement('br'))
            }

            //依次移动内容
            while (child = cellFrom.firstChild) {
                cellTo.appendChild(child);
            }
        }


        /**
         * 根据两个单元格来获取中间包含的所有单元格集合选区
         * @param cellA
         * @param cellB
         * @return {Object} 选区的左上和右下坐标
         */
        function _getCellsRange(cellA, cellB) {

            var trA = cellA.parentNode,
                trB = cellB.parentNode,
                aRowIndex = trA.rowIndex,
                bRowIndex = trB.rowIndex,
                rows = trA.parentNode.parentNode.rows,
                rowsNum = rows.length,
                cellsNum = rows[0].cells.length,
                cellAIndex = getIndex(cellA),
                cellBIndex = getIndex(cellB);

            if (cellA == cellB) {
                return {
                    beginRowIndex: aRowIndex,
                    beginCellIndex: cellAIndex,
                    endRowIndex: aRowIndex + cellA.rowSpan - 1,
                    endCellIndex: cellBIndex + cellA.colSpan - 1
                }
            }

            var
                beginRowIndex = Math.min(aRowIndex, bRowIndex),
                beginCellIndex = Math.min(cellAIndex, cellBIndex),
                endRowIndex = Math.max(aRowIndex + cellA.rowSpan - 1, bRowIndex + cellB.rowSpan - 1),
                endCellIndex = Math.max(cellAIndex + cellA.colSpan - 1, cellBIndex + cellB.colSpan - 1);

            while (1) {

                var tmpBeginRowIndex = beginRowIndex,
                    tmpBeginCellIndex = beginCellIndex,
                    tmpEndRowIndex = endRowIndex,
                    tmpEndCellIndex = endCellIndex;
                // 检查是否有超出TableRange上边界的情况
                if (beginRowIndex > 0) {
                    for (cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentTopTd = rows[beginRowIndex].cells[cellIndex];
                        if (_isHide(currentTopTd)) {

                            //overflowRowIndex = beginRowIndex == currentTopTd.rootRowIndex ? 1:0;
                            beginRowIndex = currentTopTd.getAttribute('rootRowIndex');
                            currentTopTd = rows[currentTopTd.getAttribute('rootRowIndex')].cells[currentTopTd.getAttribute('rootCellIndex')];
                        }

                        cellIndex = getIndex (currentTopTd) + (currentTopTd.colSpan || 1);
                    }
                }

                //检查是否有超出左边界的情况
                if (beginCellIndex > 0) {
                    for (var rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentLeftTd = rows[rowIndex].cells[beginCellIndex];
                        if (_isHide(currentLeftTd)) {
                            // overflowCellIndex = beginCellIndex== currentLeftTd.rootCellIndex ? 1:0;
                            beginCellIndex = currentLeftTd.getAttribute('rootCellIndex');
                            currentLeftTd = rows[currentLeftTd.getAttribute('rootRowIndex')].cells[currentLeftTd.getAttribute('rootCellIndex')];
                        }
                        rowIndex = currentLeftTd.parentNode.rowIndex + (currentLeftTd.rowSpan || 1);
                    }
                }

                // 检查是否有超出TableRange下边界的情况
                if (endRowIndex < rowsNum) {
                    for (var cellIndex = beginCellIndex; cellIndex <= endCellIndex;) {
                        var currentDownTd = rows[endRowIndex].cells[cellIndex];
                        if (_isHide(currentDownTd)) {
                            currentDownTd = rows[currentDownTd.getAttribute('rootRowIndex')].cells[currentDownTd.getAttribute('rootCellIndex')];
                        }
                        endRowIndex = currentDownTd.parentNode.rowIndex + currentDownTd.rowSpan - 1;
                        cellIndex = getIndex(currentDownTd) + (currentDownTd.colSpan || 1);
                    }
                }

                //检查是否有超出右边界的情况
                if (endCellIndex < cellsNum) {
                    for (rowIndex = beginRowIndex; rowIndex <= endRowIndex;) {
                        var currentRightTd = rows[rowIndex].cells[endCellIndex];
                        if (_isHide(currentRightTd)) {
                            currentRightTd = rows[currentRightTd.getAttribute('rootRowIndex')].cells[currentRightTd.getAttribute('rootCellIndex')];
                        }
                        endCellIndex = getIndex(currentRightTd) + currentRightTd.colSpan - 1;
                        rowIndex = currentRightTd.parentNode.rowIndex + (currentRightTd.rowSpan || 1);
                    }
                }

                if (tmpBeginCellIndex == beginCellIndex && tmpEndCellIndex == endCellIndex && tmpEndRowIndex == endRowIndex && tmpBeginRowIndex == beginRowIndex) {
                    break;
                }
            }

            //返回选区的起始和结束坐标
            return {
                beginRowIndex:  beginRowIndex,
                beginCellIndex: beginCellIndex,
                endRowIndex:    endRowIndex,
                endCellIndex:   endCellIndex
            }
        }


        /**
         * 鼠标按下事件
         * @param type
         * @param evt
         */
        function _mouseDownEvent(type, evt) {
            
            if(evt.button ==2)return;
            me.document.body.style.webkitUserSelect = '';
            anchorTd = evt.target || evt.srcElement;

            clearSelectedTd(me.currentSelectedArr);
            domUtils.clearSelectedArr(me.currentSelectedArr);
            //在td里边点击，anchorTd不是td
            if(anchorTd.tagName !== 'TD'){
                anchorTd = domUtils.findParentByTagName(anchorTd,'td') || anchorTd;
            }

            if (anchorTd.tagName == 'TD') {


                me.addListener('mouseover', function(type,evt) {
                   var tmpTd = evt.target || evt.srcElement;
                   _mouseOverEvent.call(me,tmpTd );
                   evt.preventDefault ? evt.preventDefault() : (evt.returnValue = false);
                });

            }else{

              
                reset();
            }
          
        }

        /**
         * 鼠标移动事件
         * @param tmpTd
         */
        function _mouseOverEvent(tmpTd) {

            if (anchorTd && tmpTd.tagName == "TD") {

                me.document.body.style.webkitUserSelect = 'none';
                var table = tmpTd.parentNode.parentNode.parentNode;
                me.selection.getNative()[browser.ie ? 'empty' : 'removeAllRanges']();
                var range =  _getCellsRange(anchorTd, tmpTd);
                _toggleSelect(table, range);
              

            }
        }

        /**
         * 切换选区状态
         * @param table
         * @param cellsRange
         */
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
        //更新rootRowIndxe,rootCellIndex
        function update(table){
            var tds = table.getElementsByTagName('td'),
                rowIndex,cellIndex,rows = table.rows;
            for(var j=0,tj;tj=tds[j++];){
                if(!_isHide(tj)){
                    rowIndex = tj.parentNode.rowIndex;
                    cellIndex = getIndex(tj);
                    for(var r=0;r<tj.rowSpan;r++){
                        var c = r== 0 ? 1 : 0;
                        for(;c<tj.colSpan;c++){
                            var tmp = rows[rowIndex+r].children[cellIndex+c];



                                tmp.setAttribute('rootRowIndex',rowIndex);
                                tmp.setAttribute('rootCellIndex',cellIndex);

                        }
                    }
                }
            }
        }
        me.adjustTable = function(cont){
            var table = cont.getElementsByTagName('table');
            for(var i=0,ti;ti=table[i++];){
                var createTable = ti.getAttribute('_baidu_table');
               
                !createTable && (ti.style.cssText = ti.style.cssText + ';margin-bottom:10px;border-collapse:collapse;border:1px solid #000;');
                var tds = domUtils.getElementsByTagName(ti,'td'),
                    td,tmpTd;
                
                for(var j=0,tj;tj=tds[j++];){

                   !createTable && (tj.style.cssText = tj.style.cssText +  ';vertical-align:top;padding:2px;border:1px solid #000;');
                
                      var index = getIndex(tj),
                        rowIndex = tj.parentNode.rowIndex,
                        rows = domUtils.findParentByTagName(tj,'table').rows;
                    
                    for(var r=0;r<tj.rowSpan;r++){
                        var c = r== 0 ? 1 : 0;
                        for(;c<tj.colSpan;c++){

                            if(!td){
                                td = tj.cloneNode(false);
                                
                                td.rowSpan = td.colSpan = 1;
                                td.style.display = 'none';
                                td.innerHTML = browser.ie ? '' :'<br/>';


                           }else{
                              td = td.cloneNode(true)
                            }

                             td.setAttribute('rootRowIndex',tj.parentNode.rowIndex);
                                td.setAttribute('rootCellIndex',index);
                            if(r==0){
                                if(tj.nextSibling){
                                 tj.parentNode.insertBefore(td,tj.nextSibling);
                                }else{
                                    tj.parentNode.appendChild(td)
                                }
                            }else{
                                tmpTd = rows[rowIndex+r].children[index];
                                if(tmpTd){
                                    tmpTd.parentNode.insertBefore(td,tmpTd)
                                }else{
                                    //trace:1032
                                    rows[rowIndex+r].appendChild(td)
                                }
                            }




                        }
                    }


                    
                }

            }
        }
    };



})();
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
		var picker = this, doms = {}, dom = $DomUtils.xCreateElement({
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
		var picker = this, doms = {}, dom = $DomUtils.xCreateElement({
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
		var picker = this, doms = {}, dom = $DomUtils.xCreateElement({
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
    dorado.widget.htmleditor = {
        //,'Video','Map','GMap','Code'
        fullToolbars: [
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','FormatMatch','|',
             'BlockQuote','|',
             'PastePlain','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'DirectionalityLtr','DirectionalityRtl','|','Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Anchor','Image','Emoticon', '|',
             'Horizontal','Date','Time','Spechars','|',
             'InsertTable','DeleteTable','InsertParagraphBeforeTable','InsertRow','DeleteRow','InsertCol','DeleteCol','MergeCells','MergeRight','MergeDown','SplittoCells','SplittoRows','SplittoCols','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help']
        ],
        simpleToolbars: [
            ['FullScreen','Source','|','Undo','Redo','|',
             'Bold','Italic','Underline','StrikeThrough','Superscript','Subscript','RemoveFormat','|',
             'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
             'Paragraph','RowSpacing','FontFamily','FontSize','|',
             'Indent','Outdent','|',
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyJustify','|',
             'Link','Unlink','Horizontal','Image','|',
             'SelectAll','ClearDoc','SearchReplace','Print','Preview','Help']
        ],
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

    dorado.widget.htmleditor.ToolBar = $extend(dorado.widget.Control, {
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
             * <p>如果需要自定义，比如叫custom，可以为dorado.widget.htmleditor添加一属性名为customToolbars，内容可参考fullToolbars和simpleToolbars。</p>
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
            var editor = this, riche = editor._editor;
            if (readOnly === undefined) {
                readOnly = editor._readOnly || editor._readOnly2;
            }
            if (!riche || !riche.document) return;
            if (readOnly) {
                if (dorado.Browser.msie) {
                    riche.document.body.contentEditable = false;
                } else {
                    riche.document.body.contentEditable = false;
                }
            } else {
                if (dorado.Browser.msie) {
                    riche.document.body.contentEditable = true;
                } else {
                    riche.document.body.contentEditable = true;
                }
            }
            editor.checkStatus();
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
            var heditor = this;
            $invokeSuper.call(this, arguments);
            //editor的属性
            var option = {
                initialContent: heditor._value,//初始化编辑器的内容
                minFrameHeight: 100,
                iframeCssUrl: $url(">skin>/html-editor/iframe.css")//给iframe样式的路径
            };
            var editor = new baidu.editor.Editor(option);
            this._editor = editor;
            editor.addListener('selectionchange', function () {
                heditor.checkStatus();
            });
            editor.addListener("ready", function() {
                heditor.checkStatus();
                heditor.doOnResize();
            });
            var popup = new dorado.widget.FloatContainer({
                exClassName: "popup",
                animateType: "fade"
            });
            jQuery.extend(popup, {
                _onEditButtonClick: function () {
                    this.hide();
                    heditor.executePlugin("Link");
                },
                _onImgEditButtonClick: function () {
                    this.hide();
                    var nodeStart = editor.selection.getRange().getClosedNode();
                    var img = baidu.editor.dom.domUtils.findParentByTagName(nodeStart, "img", true);
                    //edui remove.
                    if (img && img.className.indexOf("edui-faked-video") != -1) {
                        heditor.executePlugin("Video");
                    } else if (img && img.src.indexOf("http://api.map.baidu.com") != -1) {
                        heditor.executePlugin("Map");
                    } else if (img && img.src.indexOf("http://maps.google.com/maps/api/staticmap") != -1) {
                        heditor.executePlugin("GMap");
                    } else if (img && img.getAttribute("anchorname")) {
                        heditor.executePlugin("Anchor");
                    } else {
                        heditor.executePlugin("Image");
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
                        //this.showAnchor(img);
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
            var popupId = heditor._uniqueId + "_imageLinkPopup";
            window[popupId] = popup;
            editor.addListener('sourcemodechanged', function() {
                popup.hide();
            });
            editor.addListener('selectionchange', function (t, evt) {
                dorado.widget.setFocusedControl(heditor);
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
                    var anchorTarget = img || link;
                    var anchorPosition = $fly(anchorTarget).offset(), position = {};
                    var editorPosition = $fly(editor.iframe).position(), targetHeight = $fly(anchorTarget).height();
                    position.left = anchorPosition.left + editorPosition.left;
                    position.top = anchorPosition.top + editorPosition.top + targetHeight;
                    popup.show({ position: position, autoAdjustPosition: false });
                } else {
                    popup.hide();
                }
            });

            editor.render(this._doms.editorWrap);
        },
        createDom: function() {
            var editor = this, doms = {}, dom = $DomUtils.xCreateElement({
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
            var editor = this, mode = editor._mode || "default";
            editor._plugins = {};
            var toolbars = dorado.widget.htmleditor[mode + "Toolbars"] || [];
            for (var i = 0, k = toolbars.length; i < k; i++) {
                var toolbarConfig = toolbars[i], toolbar = new dorado.widget.htmleditor.ToolBar();
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

                        var plugin = new dorado.widget.htmleditor.HtmlEditorPlugIn(pluginConfig);

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
            }
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
                    plugin.checkStatus();
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

                editor._value = value;
                if (editor._editor && editor._editor.getContent() != value) {
                    editor._editor.setContent(value || "");
                }
                editor._readOnly2 = readOnly;
                if (oldReadOnly === undefined || oldReadOnly !== readOnly) {
                    editor.doOnReadOnlyChange(!!readOnly);
                }
                editor.setDirty(dirty);
            }
        }
    });

    dorado.widget.htmleditor.HtmlEditorPlugIn = $extend(dorado.AttributeSupport, {
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
            var plugin = this, labels = dorado.widget.htmleditor.defaultLabelMap;

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

    var plugins = dorado.widget.htmleditor.plugins = {
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
                            editor.set(docSize);
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
(function(plugins) {
    var htmleditor = dorado.widget.htmleditor;

    plugins.Link = {
        iconClass: "html-editor-icon link",
        command: "link",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor, formId = editor._uniqueId + "linkForm",
                urlObject = plugin.urlObject;

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
                        id: formId,
                        $type: "AutoForm",
                        cols: "*",
                        entity: urlObject,
                        elements: [
                            { property: "url", label: "超链接", type: "text" },
                            { property: "title", label: "标题", type: "text" },
                            { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                        ]
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
                                    var border = tableConfig.border;
                                    var cellpadding = tableConfig.cellpadding;
                                    var cellspacing = tableConfig.cellspacing;
                                    var width = tableConfig.width;
                                    var row = tableConfig.row || 2, column = tableConfig.column || 2;

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

    function suo(img, max) {
        var width = 0,height = 0,percent;
        img.sWidth = img.width;
        img.sHeight = img.height;
        if ( img.width > max || img.height > max ) {
            if ( img.width >= img.height ) {
                if ( width = img.width - max ) {
                    percent = (width / img.width).toFixed( 2 );
                    img.height = img.height - img.height * percent;
                    img.width = max;
                }
            } else {
                if ( height = img.height - max ) {
                    percent = (height / img.height).toFixed( 2 );
                    img.width = img.width - img.width * percent;
                    img.height = max;
                }
            }
        }
    }

    window.reloadHtmlEditorImageFn = null;

    window.reloadHtmlEditorImage = function(path) {
        if (window.reloadHtmlEditorImageFn) {
            window.reloadHtmlEditorImageFn($url(path));
            window.reloadHtmlEditorImageFn = null;
        }
    };

    var alignImageMap = {
        left: "float: left;",
        right: "float: right;",
        block: "display: block;",
        "default": ""
    };

    plugins.Image = {
        iconClass: "html-editor-icon image",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;
            var imageObject = plugin.imageObject, imageObjectLocal = plugin.imageObjectLocal;

            if (!imageObject) {
                imageObject = plugin.imageObject = new dorado.Entity();
                imageObjectLocal = plugin.imageObjectLocal = new dorado.Entity();
            }

            if (!plugin.dialog) {
                var imgInfoId = editor._uniqueId + "imageInfo", imgPreviewId = editor._uniqueId + "imagePreview",
                    alignId = editor._uniqueId + "alignEditor", imgInfoLocalId = editor._uniqueId + "imageInfoLocal",
                    imgPreviewLocalId = editor._uniqueId + "imagePreviewLocal", alignLocalId = editor._uniqueId + "alignEditorLocal";

                var imagePath = editor._uniqueId + "imagePath";

                plugin.dialog = new dorado.widget.Dialog({
                    caption: "插入图像",
                    width: 480,
                    height: 280,
                    cols: "*",
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [{
                        $type: "TabControl",
                        tabs: [{
                            $type: "Control",
                            caption: "Remote",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: imageObject,
                                    cols: "*,100",
                                    elements: [{
                                        property: "url", label: "图片链接",
                                        layoutConstraint: { colSpan: 2 },
                                        editor: new dorado.widget.TextEditor({
                                            required: true,
                                            entity: imageObject,
                                            property: "url",
                                            listener: {
                                                onPost: function(self, arg) {
                                                    var imgInfo = this.id(imgInfoId).getDom(), imgPreview = this.id(imgPreviewId);
                                                    var url = self.get("text"), preImg = imgPreview.getContentContainer();
                                                    preImg.style.height = "100px";
                                                    if ( !/\.(png|gif|jpg|jpeg|bmp)$/ig.test( url ) && url.indexOf( "api.map.baidu.com" ) == -1 ) {
                                                        preImg.innerHTML = "";
                                                        return false;
                                                    } else {
                                                        preImg.innerHTML = "图片正在加载。。。";
                                                        preImg.innerHTML = "<img src='" + url + "' />";
                                                        var pimg = preImg.firstChild;
                                                        //G( "urll" ).value = pimg.src;
                                                        pimg.onload = function() {
                                                            imgInfo.innerHTML = "原始宽：" + this.width + "px&nbsp;&nbsp;原始高：" + this.height + "px";
                                                            imgInfo.parentNode.parentNode.style.display = "";
                                                            suo( this, 100 );
                                                        };
                                                        pimg.onerror = function() {
                                                            preImg.innerHTML = "图片不存在";
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    },
                                    { property: "width", label: "宽度", type: "text" },
                                    {
                                        id: imgPreviewId,
                                        $type: "Container",
                                        layoutConstraint: { rowSpan: 4, vAlign: "top" },
                                        style: {
                                            border: "1px solid #ddd"
                                        },
                                        width: "100%",
                                        height: "100%"
                                    },
                                    { property: "height", label: "高度", type: "text" },
                                    { property: "title", label: "标题", type: "text" },
                                    {
                                        property: "align", label: "对齐方式",
                                        editor: new dorado.widget.TextEditor({
                                            id: alignId,
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
                                        layoutConstraint: { colSpan: 1 },
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
                                                    align = this.id(alignId).get("value"), title = imageObject.get("title");

                                                var imgstr = "<img ";
                                                var myimg = this.id(imgPreviewId).getDom().firstChild;
                                                imgstr += " src=" + url;

                                                if ( !width ) {
                                                    imgstr += " width=" + myimg.sWidth;
                                                }else if ( width && !/^[1-9]+[.]?\d*$/g.test( width ) ) {
                                                    alert( "请输入正确的宽度" );
                                                    return false;
                                                } else {
                                                    myimg && myimg.setAttribute( "width", width );
                                                    imgstr += " width=" + width;
                                                }
                                                if (!height) {
                                                    imgstr += " height=" + myimg.sHeight;
                                                } else if ( height && !/^[1-9]+[.]?\d*$/g.test( height ) ) {
                                                    alert( "请输入正确的高度" );
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
                            caption: "Local",
                            control: {
                                $type: "Panel",
                                children: [{
                                    $type: "AutoForm",
                                    entity: imageObjectLocal,
                                    cols: "*,100",
                                    elements: [{
                                        property: "url", label: "图片链接",
                                        layoutConstraint: { colSpan: 1 },
                                        editor: new dorado.widget.TextEditor({
                                            id: imagePath,
                                            required: true,
                                            entity: imageObjectLocal,
                                            property: "url",
                                            readOnly: true
                                        })
                                    },
                                    {
                                        $type: "Container",
                                        exClassName: "browse-button-wrap",
                                        children: [{
                                            $type: "Button",
                                            caption: "浏览..."
                                        }],
                                        onReady: function(self, arg) {
                                            if (self._inited) {
                                                return;
                                            }
                                            self._inited = true;
                                            var dom = self._dom, hiddenFile, pathEditor = this.id(imagePath);
                                            if (dorado.Browser.msie) {
                                                hiddenFile = document.createElement("<input type='file' name='filename' class='hidden-file'/>");
                                            } else {
                                                hiddenFile = document.createElement("input");
                                                hiddenFile.type = "file";
                                                hiddenFile.name = "filename";
                                                hiddenFile.className = "hidden-file";
                                            }

                                            var iframe, iframeName = editor._uniqueId + "uploadIframe";
                                            if (dorado.Browser.msie) {
                                                iframe = document.createElement("<iframe name='" + iframeName + "'></iframe>")
                                            } else {
                                                iframe = document.createElement("iframe");
                                                iframe.name = iframeName;
                                            }
                                            iframe.style.display = "none";

                                            var form, action = $url(">dorado/htmleditor/imageupload");
                                            if (dorado.Browser.msie) {
                                                form = document.createElement("<form name ='imgForm' action='" + action + "' enctype='multipart/form-data' method='post' target='" + iframeName + "'></form>");
                                            } else {
                                                form = document.createElement("form");
                                                form.action = action;
                                                form.method = "post";
                                                form.target = iframeName;
                                                form.enctype = "multipart/form-data";
                                            }

                                            form.appendChild(hiddenFile);
                                            var view = this;
                                            hiddenFile.onchange = function() {
                                                pathEditor.set("text", this.value);
                                                form.submit();
                                                window.reloadHtmlEditorImageFn = function(url) {
                                                    var imgInfo = view.id(imgInfoLocalId).getDom(), imgPreview = view.id(imgPreviewLocalId);
                                                    var preImg = imgPreview.getContentContainer();
                                                    preImg.style.height = "100px";
                                                    if ( !/\.(png|gif|jpg|jpeg|bmp)$/ig.test( url ) && url.indexOf( "api.map.baidu.com" ) == -1 ) {
                                                        preImg.innerHTML = "";
                                                        return false;
                                                    } else {
                                                        preImg.innerHTML = "图片正在加载。。。";
                                                        preImg.innerHTML = "<img src='" + url + "' />";
                                                        var pimg = preImg.firstChild;
                                                        pimg.onload = function() {
                                                            imgInfo.innerHTML = "原始宽：" + this.width + "px&nbsp;&nbsp;原始高：" + this.height + "px";
                                                            imgInfo.parentNode.parentNode.style.display = "";
                                                            suo( this, 100 );
                                                        };
                                                        pimg.onerror = function() {
                                                            preImg.innerHTML = "图片不存在";
                                                        }
                                                    }
                                                    imageObjectLocal.set("url", url);
                                                }
                                            };
                                            dom.firstChild.appendChild(form);
                                            dom.firstChild.appendChild(iframe);
                                        }
                                    },
                                    { property: "width", label: "宽度", type: "text" },
                                    {
                                        id: imgPreviewLocalId,
                                        $type: "Container",
                                        layoutConstraint: { rowSpan: 4, vAlign: "top" },
                                        style: {
                                            border: "1px solid #ddd"
                                        },
                                        width: "100%",
                                        height: "100%"
                                    },
                                    { property: "height", label: "高度", type: "text" },
                                    { property: "title", label: "标题", type: "text" },
                                    {
                                        property: "align", label: "对齐方式",
                                        editor: new dorado.widget.TextEditor({
                                            id: alignLocalId,
                                            entity: imageObjectLocal,
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
                                        id: imgInfoLocalId,
                                        $type: "HtmlContainer",
                                        style: {
                                            "text-align": "right"
                                        },
                                        layoutConstraint: { colSpan: 1 },
                                        content: "&nbsp;"
                                    }]
                                }],
                                buttons: [{
                                    caption: "确定",
                                    listener: {
                                        onClick: function() {
                                            var url = imageObjectLocal.get("url");
                                            if (url) {
                                                var width = imageObjectLocal.get("width"), height = imageObjectLocal.get("height"),
                                                    align = this.id(alignLocalId).get("value"), title = imageObjectLocal.get("title");

                                                var imgstr = "<img ";
                                                var myimg = this.id(imgPreviewLocalId).getDom().firstChild;
                                                imgstr += " src=" + url;

                                                if ( !width ) {
                                                    imgstr += " width=" + myimg.sWidth;
                                                }else if ( width && !/^[1-9]+[.]?\d*$/g.test( width ) ) {
                                                    alert( "请输入正确的宽度" );
                                                    return false;
                                                } else {
                                                    myimg && myimg.setAttribute( "width", width );
                                                    imgstr += " width=" + width;
                                                }
                                                if (!height) {
                                                    imgstr += " height=" + myimg.sHeight;
                                                } else if ( height && !/^[1-9]+[.]?\d*$/g.test( height ) ) {
                                                    alert( "请输入正确的高度" );
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
                            }
                        }]
                    }]
                });

                plugin._htmlEditor.registerInnerControl(plugin.dialog);
            }
            plugin.dialog.show();
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
            plugin.fontEditor.set("text", value);
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
                    value: temp + "pt"
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
                        plugin.execCommand("fontsize", self.get("value") + "pt");
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

    plugins.Video = {
        iconClass: "html-editor-icon spechars",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            $type: "AutoForm",
                            cols: "*",
                            entity: urlObject,
                            elements: [
                                { property: "url", label: "超链接", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                            ]
                        }
                    ],
                    buttons: []
                });
                editor.registerInnerControl(plugin.dialog);
            }

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

            var anchor, img = editor._editor.selection.getRange().getClosedNode(), autoform = editor._view.id(formId);;
            if(img && /img/ig.test(img.tagName.toLowerCase()) && img.getAttribute('anchorname')){
                anchor = img.getAttribute('anchorname');
            }

            anchorObject.set("name", anchor || "");
            autoform.refreshData();

            plugin.dialog.show();
        }
    };

    var scriptOnload = document.createElement('script').readyState ? function(node, callback) {
	   var oldCallback = node.onreadystatechange;
	   node.onreadystatechange = function() {
		   var rs = node.readyState;
		   if (rs === 'loaded' || rs === 'complete') {
			   node.onreadystatechange = null;
			   oldCallback && oldCallback();
			   callback.call(this);
		   }
	   };
	} : function(node, callback) {
	   node.addEventListener('load', callback, false);
	};

    plugins.Map = {
        iconClass: "html-editor-icon map",
        command: "inserthtml",
        execute: function() {
            var plugin = this, editor = plugin._htmlEditor;

            if (!plugin.dialog) {
                plugin.dialog = new dorado.widget.Dialog({
                    caption: "特殊字符",
                    width: 480,
                    center: true,
                    modal: true,
                    modalType: "transparent",
                    children: [
                        {
                            $type: "AutoForm",
                            cols: "*",
                            entity: urlObject,
                            elements: [
                                { property: "url", label: "超链接", type: "text" },
                                { property: "title", label: "标题", type: "text" },
                                { property: "target", label: "是否在新窗口打开", type: "checkBox" }
                            ]
                        }
                    ],
                    buttons: []
                });
                editor.registerInnerControl(plugin.dialog);
                var script = document.createElement("script");
                script.src = "http://api.map.baidu.com/api?v=1.1&services=true";
                script.type = "text/javascript";
                scriptOnload(script, function() {
                    //var map = BMap.Map;
                });
                document.getElementsByTagName("head")[0].appendChild(script);
            }

            plugin.dialog.show();
        }
    };
})(dorado.widget.htmleditor.plugins);
