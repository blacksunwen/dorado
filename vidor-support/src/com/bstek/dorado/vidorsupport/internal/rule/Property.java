package com.bstek.dorado.vidorsupport.internal.rule;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.internal.IOriginalObjectHolder;
import com.bstek.dorado.vidorsupport.internal.output.AbstractField;
import com.bstek.dorado.vidorsupport.internal.output.BooleanField;
import com.bstek.dorado.vidorsupport.internal.output.IOutputFiledsable;
import com.bstek.dorado.vidorsupport.internal.output.IOutputable;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.vidorsupport.internal.output.StringField;
import com.bstek.dorado.idesupport.model.Reference;
import com.bstek.dorado.util.clazz.ClassUtils;

public class Property implements IOutputable<OutputContext>, IOutputFiledsable,
	IOriginalObjectHolder<com.bstek.dorado.idesupport.model.Property>{
	private static final Log logger = LogFactory.getLog(Property.class);
	
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 对应的java类型，例如：java.lang.String
	 */
	private String javaClass;
	/**
	 * 默认值，类型是字符串
	 */
	private StringField defaultValue = new StringField(Treaty.Property.DEFAULT_VALUE);
	/**
	 * 编辑器类型
	 */
	private StringField editorType = new StringField(Treaty.Property.EDITOR_TYPE);
	/**
	 * 是否可以出现在编辑器中被看到
	 */
	private BooleanField visible = new BooleanField(Treaty.Property.VISIBLE, true);
	/**
	 * 在"规则文件"中是否被定义为primitive
	 */
	private BooleanField primitive = new BooleanField("primitive", false);
	/**
	 * 属性值是否被锁定，即不可修改
	 */
	private BooleanField fixed = new BooleanField(Treaty.Property.FIXED, false);
	/**
	 * 是否是不推荐的"属性"
	 */
	private BooleanField deprecated = new BooleanField(Treaty.Property.DEPRECATED, false);
	
	/**
	 * 子属性列表（嵌套属性）
	 */
	private Property[] properties;
	
	private com.bstek.dorado.idesupport.model.Property originalObject;
	
	public Property() {
		super();
	}
	
	public Property(com.bstek.dorado.idesupport.model.Property property, BuildContext context) {
		this();
		this.originalObject = property;
		
		this.name = property.getName();
		
		this.setDefaultValue((property.getDefaultValue() == null) ? null: property.getDefaultValue().toString());
		this.setVisible(property.isVisible());
		this.setFixed(property.isFixed());
		this.setDeprecated(property.isDeprecated());
		
		String javaClass = this.javaClass = property.getType();
		if ("boolean".equals(javaClass)) {
			this.setEditorType(Treaty.Editor.BOOLEAN);
			if (this.defaultValue.getValue() == null || this.defaultValue.getValue().length() == 0) {
				this.defaultValue.setValue("false");
			}
		} else if ("[Ljava.lang.String;".equals(javaClass)) {
			this.setEditorType(Treaty.Editor.STRING_ARRAY);
		}
		
		Reference reference = property.getReference();
		if (reference != null) {
			String propertyName = reference.getProperty();
			com.bstek.dorado.idesupport.model.Rule rule = reference.getRule();
			if (rule != null && propertyName != null && propertyName.length() > 0) {
				String[] enumValues = property.getEnumValues();
				String ruleName = rule.getName();
				ReferenceEditorMeta meta = new ReferenceEditorMeta(ruleName, propertyName, enumValues);
				this.editorType.setValue(meta.getName());
				context.addEditorMeta(meta);
			}
		}
		
		//枚举类
		if (this.editorType.getValue() == null) {
			String[] enumValues = property.getEnumValues();
			if (enumValues != null && enumValues.length > 0) {
				if (javaClass != null && javaClass.length() > 0) {
					try {
						Class<?> type = ClassUtils.forName(javaClass);
						if (type.isEnum()) {
							EnumEditorMeta meta = new EnumEditorMeta(javaClass, enumValues);
							context.addEditorMeta(meta);
							this.editorType.setValue(javaClass);
						}
					} catch (ClassNotFoundException e) {
						logger.error("[" + javaClass + "]", e);
					}
				}
				
				if (this.editorType.getValue() == null) {
					EnumEditorMeta meta = context.addEnumEditorMeta(enumValues);
					this.editorType.setValue(meta.getName());
				}
			} 
		}
		
		if (this.editorType.getValue() == null) {
			String editorType = property.getEditor();
			if (editorType != null && editorType.length() > 0) {
				editorType = editorType.toLowerCase();
				if (!context.hasEditorMeta(editorType)) {
					context.addEditorMeta(new CustomEditorMeta(editorType));
				}
				this.editorType.setValue(editorType);
			}
		}
		
		if (this.editorType.getValue() == null && "java.util.Map".equals(javaClass)) {
			this.editorType.setValue(Treaty.Editor.POJO);
		}
		
		{//处理子属性
			ArrayList<Property> properties = new ArrayList<Property>();
			for (com.bstek.dorado.idesupport.model.Property p: property.getProperties().values()) {
				Property d7Property = new Property(p, context);
				properties.add(d7Property);
			}
			properties.trimToSize();
			if (properties.size() > 0) {
				this.properties = properties.toArray(new Property[0]);
			}
		}
	}
	
	public AbstractField<?>[] getOutputFilelds() {
		AbstractField<?>[] fields = new AbstractField[]{
			defaultValue, editorType,
			visible, fixed, deprecated
		};
		return fields;
	}
	
	@Override
	public void output(OutputContext context) {
		AbstractField<?>[] fields = this.getOutputFilelds();
		
		boolean shoudOutput = false;
		for (AbstractField<?> field: fields) {
			if (!shoudOutput) {
				shoudOutput |= field.shouldOutput();
			}
		}
		
		shoudOutput |= (properties!= null && properties.length > 0);
		
		if (shoudOutput) {
			JsonGenerator jsonGenerator = context.getJsonGenerator();
			try {
				jsonGenerator.writeFieldName(name);
				jsonGenerator.writeStartObject();
				//输出"基本信息"
				for (AbstractField<?> field: fields) {
					if(field.shouldOutput()) {
						field.output(context);
					}
				}
				//输出"属性"列表
				if (properties!= null && properties.length > 0) {
					jsonGenerator.writeFieldName(Treaty.Property.PROPERTIES);
					jsonGenerator.writeStartObject();
					{//properties.names
						jsonGenerator.writeFieldName(Treaty.Property.NAMES);
						jsonGenerator.writeStartArray();
						for (Property property: properties) {
							jsonGenerator.writeString(property.getName());
						}
						jsonGenerator.writeEndArray();
					}
					{//properties.data
						jsonGenerator.writeFieldName(Treaty.Property.DATA);
						jsonGenerator.writeStartObject();
						for (Property property: properties) {
							property.output(context);
						}
						jsonGenerator.writeEndObject();
					}
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndObject();
			} catch (JsonGenerationException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue.getValue();
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue.setValue(defaultValue);
	}

	public String getEditorType() {
		return editorType.getValue();
	}
	public void setEditorType(String editorType) {
		this.editorType.setValue(editorType);
	}

	public boolean isVisible() {
		return visible.getValue();
	}
	public void setVisible(boolean visible) {
		this.visible.setValue(visible);
	}

	public boolean isPrimitive() {
		return primitive.getValue();
	}
	public void setPrimitive(boolean primitive) {
		this.primitive.setValue(primitive);
	}

	public boolean isFixed() {
		return fixed.getValue();
	}
	public void setFixed(boolean fixed) {
		this.fixed.setValue(fixed);
	}

	public boolean isDeprecated() {
		return deprecated.getValue();
	}
	public void setDeprecated(boolean deprecated) {
		this.deprecated.setValue(deprecated);
	}

	public String getJavaClass() {
		return javaClass;
	}
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	public Property[] getProperties() {
		return properties;
	}
	public void setProperties(Property[] properties) {
		this.properties = properties;
	}

	@Override
	public com.bstek.dorado.idesupport.model.Property getOriginalObject() {
		return this.originalObject;
	}

}
