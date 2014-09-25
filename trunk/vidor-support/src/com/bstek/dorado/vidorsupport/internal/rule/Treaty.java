package com.bstek.dorado.vidorsupport.internal.rule;

final class Treaty {
	public class PackageInfo {
		public static final String NAME          = "name";
		public static final String VERSION       = "version";
		public static final String ADDON_VERSION = "addonVersion";
	}
	
	public class RuleSet {
		public static final String VERSION       = "version";
		public static final String PACKAGE_INFOS = "packageInfos";
		public static final String RULES         = "rules";
		public static final String EDITOR_METAS  = "editorMetas";
		public static final String DEFINITIONS   = "definitions";
	}
	
	public class Rule {
		//--简单属性--
		public static final String LEVEL            = "level";
		public static final String CATEGORY         = "category";
		public static final String LABEL            = "label";
		public static final String LABEL_PROPERTIES = "labelProperties";
		public static final String ICON             = "icon";
		public static final String JS_PROTOTYPE     = "jsPrototype";
		public static final String JS_SHORTTYPE     = "jsShortType";
		public static final String LAYOUTABLE       = "layoutable";
		public static final String POSITIONED       = "positioned";
		public static final String DEPRECATED       = "deprecated";
		public static final String DEPENDS_PACKAGE  = "dependsPackage";
		public static final String CLIENT_TYPE = "clientTypes";
		//--复合属性--
		public static final String PROPERTIES = "properties";
		public static final String EVENTS     = "events";
		public static final String CHILDREN   = "children";
	}
	
	public class Property {
		//--简单属性--
		public static final String DEFAULT_VALUE = "defaultValue";
		public static final String EDITOR_TYPE   = "editorType";
		public static final String VISIBLE       = "visible";
		public static final String FIXED         = "fixed";
		public static final String DEPRECATED    = "deprecated";
		//--复合属性--
		public static final String PROPERTIES = "properties";
		public static final String NAMES = "names";
		public static final String DATA = "data";
	}
	
	public class Editor {
		public static final String BOOLEAN      = "boolean";
		public static final String STRING_ARRAY = "array[string]";
		public static final String POJO         = "pojo";
		public static final String REFERENCE    = "reference";
		public static final String ENUM         = "enum";
		public static final String TRADITION    = "tradition";
		public static final String CUSTOM       = "custom";
		public static final String ANY          = "any";
		public static final String MULTILINES   = "multilines";
		public static final String POJO_COLLECTION  = "collection[pojo]";
		public static final String ANY_COLLECTION   = "collection[any]";
		public static final String VALUE_COLLECTION = "collection[value]";
	}

}
