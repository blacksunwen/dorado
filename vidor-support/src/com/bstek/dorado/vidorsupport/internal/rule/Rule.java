package com.bstek.dorado.vidorsupport.internal.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.vidorsupport.internal.IOriginalObjectHolder;
import com.bstek.dorado.vidorsupport.internal.output.AbstractField;
import com.bstek.dorado.vidorsupport.internal.output.BooleanField;
import com.bstek.dorado.vidorsupport.internal.output.IOutputFiledsable;
import com.bstek.dorado.vidorsupport.internal.output.IOutputable;
import com.bstek.dorado.vidorsupport.internal.output.IntegerField;
import com.bstek.dorado.vidorsupport.internal.output.OutputContext;
import com.bstek.dorado.vidorsupport.internal.output.StringArrayField;
import com.bstek.dorado.vidorsupport.internal.output.StringField;
import com.bstek.dorado.util.clazz.ClassUtils;
import com.bstek.dorado.view.annotation.Widget;

public class Rule implements IOutputable<OutputContext>, IOutputFiledsable,
	IOriginalObjectHolder<com.bstek.dorado.idesupport.model.Rule> {
	/**
	 * 全局ID
	 * 对于顶层"规则"，ID等于name；对于内部"规则"，则是从所属的顶层"规则"开始到自己的路径
	 */
	private String id;
	/**
	 * 序列化时候对应的XML的节点名称
	 */
	private String xmlNodeName;
	/**
	 * 是否是顶层"规则"
	 * 0表示顶层"规则"，大于0表示内部"规则"
	 */
	private IntegerField level = new IntegerField(Treaty.Rule.LEVEL, 0);
	/**
	 * 所属的分类名称
	 */
	private StringField category = new StringField(Treaty.Rule.CATEGORY);
	/**
	 * 标签，用于显示的名称
	 * 通常与name相等
	 */
	private StringField label = new StringField(Treaty.Rule.LABEL);
	/**
	 * 用于显示的属性名称
	 */
	private StringArrayField labelProperties = new StringArrayField(Treaty.Rule.LABEL_PROPERTIES, new String[]{"name", "id"});
	/**
	 * 图标
	 * 用于显示的图标路径
	 */
	private StringField icon = new StringField(Treaty.Rule.ICON);
	/**
	 * 对应客户端的JavaScript对象的完整类名
	 */
	private StringField jsPrototype = new StringField(Treaty.Rule.JS_PROTOTYPE);
	/**
	 * 对应客户端的JavaScript对象的简写类名
	 */
	private StringField jsShortType = new StringField(Treaty.Rule.JS_SHORTTYPE);
	/**
	 * 是否具有"布局"属性
	 */
	private BooleanField layoutable = new BooleanField(Treaty.Rule.LAYOUTABLE,false);
	/**
	 * 是否具有"位置"属性
	 */
	private BooleanField positioned = new BooleanField(Treaty.Rule.POSITIONED, false);
	/**
	 * 是否是不推荐的"规则"
	 */
	private BooleanField deprecated = new BooleanField(Treaty.Rule.DEPRECATED, false);

	private StringField dependsPackage = new StringField(Treaty.Rule.DEPENDS_PACKAGE);
	
	private IntegerField clientTypes = new IntegerField(Treaty.Rule.CLIENT_TYPE);
	/**
	 * "属性"集合
	 */
	private Property[] properties = null;
	/**
	 * "事件"集合
	 */
	private ClientEvent[] events = null;
	/**
	 * 存放子对象的"子规则"集合
	 */
	private Child[] children = null;
	
	private com.bstek.dorado.idesupport.model.Rule originalObject;
	
	public Rule() {
		super();
	}
	
	public Rule(com.bstek.dorado.idesupport.model.Rule rule, BuildContext context) {
		this();
		this.originalObject = rule;
		
		buildAttributes(rule);
		buildProperties(rule, context);
		buildClientEvents(rule);
	}
	
	@Override
	public com.bstek.dorado.idesupport.model.Rule getOriginalObject(){
		return this.originalObject;
	}
	
	public AbstractField<?>[] getOutputFilelds() {
		AbstractField<?>[] fields = new AbstractField<?>[]{
			level, category, label, labelProperties,
			icon, jsPrototype, jsShortType, layoutable,
			positioned, deprecated, dependsPackage,
			clientTypes
		};
		return fields;
	}
	
	@Override
	public void output(OutputContext context) {
		if (id == null || id.length() == 0)
			throw new IllegalArgumentException("the value of 'id' property cant be empty.");
		
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			//输出"基本信息"
			AbstractField<?>[] fields = this.getOutputFilelds();
			for (AbstractField<?> field: fields) {
				if (field.shouldOutput())
					field.output(context);
			}
			//输出"属性"列表
			if (properties.length > 0) {
				jsonGenerator.writeFieldName("properties");
				jsonGenerator.writeStartObject();
				{//properties.names
					jsonGenerator.writeFieldName("names");
					jsonGenerator.writeStartArray();
					for (Property property: properties) {
						jsonGenerator.writeString(property.getName());
					}
					jsonGenerator.writeEndArray();
				}
				{//properties.data
					jsonGenerator.writeFieldName("data");
					jsonGenerator.writeStartObject();
					for (Property property: properties) {
						property.output(context);
					}
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndObject();
			}
			//输出"事件"列表
			if (events.length > 0) {
				jsonGenerator.writeFieldName("events");
				jsonGenerator.writeStartObject();
				{//events.names
					jsonGenerator.writeFieldName("names");
					jsonGenerator.writeStartArray();
					for (ClientEvent event: events) {
						jsonGenerator.writeString(event.getName());
					}
					jsonGenerator.writeEndArray();
				}
				{//events.data
					jsonGenerator.writeFieldName("data");
					jsonGenerator.writeStartArray();
					for (ClientEvent event: events) {
						event.output(context);
					}
					jsonGenerator.writeEndArray();
				}
				jsonGenerator.writeEndObject();
			}
			//输出"子规则"列表
			if (children.length > 0) {
				jsonGenerator.writeFieldName("children");
				jsonGenerator.writeStartArray();
				for (Child child: children) {
					child.output(context);
				}
				jsonGenerator.writeEndArray();
			}
			jsonGenerator.writeEndObject();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildAttributes(com.bstek.dorado.idesupport.model.Rule rule) {
		this.xmlNodeName = rule.getNodeName();
		this.category.setValue(rule.getCategory());
		this.label.setValue(rule.getLabel());
		String labelProperty = rule.getLabelProperty();
		if (labelProperty != null && labelProperty.length() > 0) {
			this.labelProperties.setValue(StringUtils.split(labelProperty, ','));
		}
		this.icon.setValue(rule.getIcon());
		this.deprecated.setValue(rule.isDeprecated());
		this.clientTypes.setValue(rule.getClientTypes());
		
		String javaClassName = rule.getType();
		if (javaClassName != null && javaClassName.length() > 0) {
			try {
				Class<?> clazz = ClassUtils.forName(javaClassName);
				ClientObject clientObject = clazz.getAnnotation(ClientObject.class);
				if (clientObject != null) {
					this.jsPrototype.setValue(clientObject.prototype());
					this.jsShortType.setValue(clientObject.shortTypeName());
				}
				Widget widget = clazz.getAnnotation(Widget.class);
				if (widget != null) {
					String pkg = widget.dependsPackage();
					this.dependsPackage.setValue(pkg);
				}
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("[" + javaClassName + "]",e);
			}
		}
		
	}
	
	private void buildProperties(com.bstek.dorado.idesupport.model.Rule rule, BuildContext context) {
		Map<String, com.bstek.dorado.idesupport.model.Property> primitiveProperties = rule.getPrimitiveProperties();
		Map<String, com.bstek.dorado.idesupport.model.Property> properties = rule.getProperties();
		
		Map<String, Property> allD7Properties = new LinkedHashMap<String, Property>();
		for (com.bstek.dorado.idesupport.model.Property p: primitiveProperties.values()) {
			Property d7Property = new Property(p, context);
			d7Property.setPrimitive(true);
			allD7Properties.put(p.getName(), d7Property);
		}
		for (com.bstek.dorado.idesupport.model.Property p: properties.values()) {
			String name = p.getName();
			if (!allD7Properties.containsKey(name)) {
				Property d7Property = new Property(p, context);
				d7Property.setPrimitive(false);
				allD7Properties.put(p.getName(), d7Property);
			}
			
		}
		
		for (Property p: allD7Properties.values()) {
			String name  = p.getName();
			if ("layout".equals(name)) {
				this.layoutable.setValue(true);
				p.setVisible(false);
				continue;
			}
			if ("layoutConstraint".equals(name)) {
				this.positioned.setValue(true);
				p.setVisible(false);
				continue;
			}
		}
		this.setProperties(allD7Properties.values().toArray(new Property[0]));
	}
	
	private void buildClientEvents(com.bstek.dorado.idesupport.model.Rule rule) {
		Collection<com.bstek.dorado.idesupport.model.ClientEvent> events = rule.getClientEvents().values();
		ArrayList<ClientEvent> d7Events = new ArrayList<ClientEvent>(events.size());
		for (com.bstek.dorado.idesupport.model.ClientEvent e: events) {
			ClientEvent d7Event = new ClientEvent(e);
			d7Events.add(d7Event);
		}
		this.events = d7Events.toArray(new ClientEvent[0]);
	}
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLevel() {
		return level.getValue();
	}
	public void setLevel(int level) {
		this.level.setValue(level);
	}
	public String getCategory() {
		return category.getValue();
	}
	public void setCategory(String category) {
		this.category.setValue(category);
	}
	public String getLabel() {
		return label.getValue();
	}
	public void setLabel(String label) {
		this.label.setValue(label);
	}
	public String[] getLabelProperties() {
		return labelProperties.getValue();
	}
	public void setLabelProperties(String[] labelProperties) {
		this.labelProperties.setValue(labelProperties);
	}
	public String getIcon() {
		return icon.getValue();
	}
	public void setIcon(String icon) {
		this.icon.setValue(icon);
	}
	public String getXmlNodeName() {
		return xmlNodeName;
	}
	public void setXmlNodeName(String xmlNodeName) {
		this.xmlNodeName = xmlNodeName;
	}
	public String getJsPrototype() {
		return jsPrototype.getValue();
	}
	public void setJsPrototype(String jsPrototype) {
		this.jsPrototype.setValue(jsPrototype);
	}
	public String getJsShortType() {
		return jsShortType.getValue();
	}
	public void setJsShortType(String jsShortType) {
		this.jsShortType.setValue(jsShortType);
	}
	public boolean isLayoutable() {
		return layoutable.getValue();
	}
	public void setLayoutable(boolean layoutable) {
		this.layoutable.setValue(layoutable);
	}
	public boolean isPositioned() {
		return positioned.getValue();
	}
	public void setPositioned(boolean positioned) {
		this.positioned.setValue(positioned);
	}
	public boolean isDeprecated() {
		return deprecated.getValue();
	}
	public void setDeprecated(boolean deprecated) {
		this.deprecated.setValue(deprecated);
	}
	
	public String getDependsPackage() {
		return dependsPackage.getValue();
	}
	public void setDependsPackage(String dependsPackage) {
		this.dependsPackage.setValue(dependsPackage);
	}
	
	public int getClientTypes() {
		return clientTypes.getValue();
	}
	public void setClientTypes(int clientTypes) {
		this.clientTypes.setValue(clientTypes);
	}
	
	private Map<String, Property> _propertiesCache_ = null;
	private Property[] _fixedProperties_ = new Property[0];
	public Property[] getProperties() {
		return properties;
	}
	
	public void setProperties(Property[] properties) {
		ArrayList<Property> fixedProperties = new ArrayList<Property>(2);
		Map<String, Property> cache = new LinkedHashMap<String, Property>(properties.length);
		for (Property p: properties) {
			if (p.isFixed()) {
				fixedProperties.add(p);
			} else {
				String name = p.getName();
				cache.put(name, p);
			}
		}
		this._propertiesCache_ = cache;
		this.properties = (new ArrayList<Property>(cache.values())).toArray(new Property[0]);
		
		if (fixedProperties.size() > 0) {
			this._fixedProperties_ = fixedProperties.toArray(new Property[0]);
		}
	}
	
	public Property getProperty(String name) {
		return this._propertiesCache_.get(name);
	}
	
	public Property[] getFixedProperties() {
		return _fixedProperties_;
	}
	public Property getFixedProperty(String name) {
		for (Property property: _fixedProperties_) {
			String propertyName = property.getName();
			if (propertyName.equals(name))
				return property;
		}
		return null;
	}
	public boolean hasFixedProperty(String name) {
		return this.getFixedProperty(name) != null;
	}
	
	
	public ClientEvent[] getEvents() {
		return events;
	}
	public void setEvents(ClientEvent[] events) {
		this.events = events;
	}
	
	public Child[] getChildren() {
		return children;
	}
	public void setChildren(Child[] children) {
		this.children = children;
	}
}
