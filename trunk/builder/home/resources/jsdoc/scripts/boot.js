var contextPath = "", skinRoot = "skins/", skin = "nature", locale = "zh_CN";

$setting = {
	"common.contextPath": contextPath,
	"widget.skinRoot": ">" + skinRoot,
	"widget.skin": skin
};

$packagesConfig = {
	"contextPath": contextPath,
	"defaultCharset": "UTF-8"
};

$packagesConfig.patterns = {
	"default": {
		url: ">${fileName}.js"
	},
	"css": {
		url: ">${fileName}.css",
		contentType: "text/css"
	},
	"lib": {
		charset: "UTF-8",
		url: ">scripts/${fileName}.js"
	},
	"skin": {
		charset: "UTF-8",
		contentType: "text/css",
		url: ">" + skinRoot + skin + "/${fileName}.min.css"
	}
};

$packagesConfig.packages = {
	"jquery": {
		fileName: "jquery/jquery-1.4.4",
		pattern: "lib"
	},
	"jquery-plugins": {
		fileName: ["jquery/aop", "jquery/jquery-template-1.0.0", "jquery/jquery.easing.1.3", "jquery/jquery.mousewheel", "jquery/jquery.hotkeys", "jquery/jquery-ui-1.8.7.custom", "jquery/jquery.highlight"],
		pattern: "lib",
		depends: "jquery"
	},
	"json2": {
		fileName: "json/json2",
		pattern: "lib"
	},

	"core": {
		fileName: "dorado/core,dorado/i18n/core." + locale,
		pattern: "lib",
		depends: "jquery,json2"
	},
	"data": {
		fileName: "dorado/data,dorado/i18n/data." + locale,
		pattern: "lib",
		depends: "core"
	},
	"widget.skin.support": {
		fileName: skinRoot + skin + "/support"
	},
	"widget.skin": {
		fileName: "common,widget",
		pattern: "skin",
		depends: "widget.skin.support"
	},
	"widget": {
		fileName: "dorado/widget,dorado/i18n/widget." + locale,
		pattern: "lib",
		depends: "jquery-plugins,data,widget.skin"
	},
	"base-widget.skin": {
		fileName: "base-widget",
		pattern: "skin"
	},
	"base-widget": {
		fileName: "dorado/base-widget,dorado/i18n/base-widget." + locale,
		pattern: "lib",
		depends: "widget,base-widget.skin"
	},
	"list.skin": {
		fileName: "list",
		pattern: "skin"
	},
	"list": {
		fileName: "dorado/list,dorado/i18n/list." + locale,
		pattern: "lib",
		depends: "base-widget,list.skin"
	},
	"grid.skin": {
		fileName: "grid",
		pattern: "skin"
	},
	"grid": {
		fileName: "dorado/grid,dorado/i18n/grid." + locale,
		pattern: "lib",
		depends: "list,grid.skin"
	},
	"tree.skin": {
		fileName: "tree",
		pattern: "skin"
	},
	"tree": {
		fileName: "dorado/tree,dorado/i18n/tree." + locale,
		pattern: "lib",
		depends: "list,tree.skin"
	},
	"block-view.skin": {
		fileName: "block-view",
		pattern: "skin"
	},
	"block-view": {
		fileName: "dorado/block-view",
		pattern: "lib",
		depends: "list,block-view.skin"
	},
	"tree-grid": {
		fileName: "dorado/tree-grid",
		pattern: "lib",
		depends: "grid,tree"
	},
	"advance.skin": {
		fileName: "advance",
		pattern: "skin"
	},
	"advance": {
		fileName: "dorado/advance",
		pattern: "lib",
		depends: "base-widget,advance.skin"
	},

	"syntax-highlighter.style": {
		fileName: "styles/shCore,styles/shThemeDefault",
		pattern: "css"
	},
	"syntax-highlighter": {
		fileName: ["jquery/jquery.beautyOfCode", "syntax-highlighter/shCore", "syntax-highlighter/shBrushJScript", "syntax-highlighter/shBrushXml"],
		pattern: "lib",
		depends: "jquery,syntax-highlighter.style"
	},
	"dictionary": {
		fileName: "data/symbol-dictionary"
	},
	"jsdoc.style": {
		fileName: "styles/jsdoc",
		pattern: "css"
	},
	"jsdoc": {
		fileName: "scripts/jsdoc",
		depends: ["widget", "dictionary", "jsdoc.style"]
	}
};

document.writeln("<script src='" + contextPath + "scripts/dorado/boot.min.js' charset='utf-8'></script>");
