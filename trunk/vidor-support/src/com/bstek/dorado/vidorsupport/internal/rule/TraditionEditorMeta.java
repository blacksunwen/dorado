package com.bstek.dorado.vidorsupport.internal.rule;

/**
 * 在规则文件中出现的编辑器：
 * 例如：{"pojo","any","collection[pojo]","multiLines","collection[any]"}
 * 
 * @author TD
 * 
 */
public class TraditionEditorMeta extends AbstractEditorMeta {

	public static final TraditionEditorMeta[] ALL = new TraditionEditorMeta[]{
		new PojoTraditionEditorMeta(),
		new AnyTraditionEditorMeta(),
		new MultiLinesTraditionEditorMeta(),
		new CollectionPojoTraditionEditorMeta(),
		new CollectionAnyTraditionEditorMeta(),
		new CollectionValueTraditionEditorMeta(),
		new ArrayStringTraditionEditorMeta()
	};
	
	protected TraditionEditorMeta(String name) {
		super(name, Treaty.Editor.TRADITION);
	}

	public static class PojoTraditionEditorMeta extends TraditionEditorMeta {
		public PojoTraditionEditorMeta() {
			super(Treaty.Editor.POJO);
		}
	}
	
	public static class AnyTraditionEditorMeta extends TraditionEditorMeta {
		public AnyTraditionEditorMeta() {
			super(Treaty.Editor.ANY);
		}
	}
	
	public static class MultiLinesTraditionEditorMeta extends TraditionEditorMeta {
		public MultiLinesTraditionEditorMeta() {
			super(Treaty.Editor.MULTILINES);
		}
	}
	
	public static class CollectionPojoTraditionEditorMeta extends TraditionEditorMeta {
		public CollectionPojoTraditionEditorMeta() {
			super(Treaty.Editor.POJO_COLLECTION);
		}
	}
	
	public static class CollectionAnyTraditionEditorMeta extends TraditionEditorMeta {
		public CollectionAnyTraditionEditorMeta() {
			super(Treaty.Editor.ANY_COLLECTION);
		}
	}
	
	public static class CollectionValueTraditionEditorMeta extends TraditionEditorMeta {
		public CollectionValueTraditionEditorMeta() {
			super(Treaty.Editor.VALUE_COLLECTION);
		}
	}
	
	public static class ArrayStringTraditionEditorMeta extends TraditionEditorMeta {
		public ArrayStringTraditionEditorMeta() {
			super(Treaty.Editor.STRING_ARRAY);
		}
	}
}
