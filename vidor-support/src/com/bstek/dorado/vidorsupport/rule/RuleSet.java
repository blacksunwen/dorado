package com.bstek.dorado.vidorsupport.rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.dom4j.Element;

import com.bstek.dorado.vidorsupport.exception.XmlException;
import com.bstek.dorado.vidorsupport.output.AbstractField;
import com.bstek.dorado.vidorsupport.output.IOutputFiledsable;
import com.bstek.dorado.vidorsupport.output.IOutputable;
import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.core.pkgs.PackageInfo;

public class RuleSet implements IOutputable<OutputContext>, IOriginalObjectHolder<com.bstek.dorado.idesupport.model.RuleSet> {
	private String version;
	private List<Map<String, String>> packageInfos = new ArrayList<Map<String, String>>();
	private Collection<Rule> rules;
	private Map<String, Rule> ruleCache;
	private Collection<String> auxiliaryRuleNames;
	private com.bstek.dorado.idesupport.model.RuleSet originalObject;
	private Map<String, AbstractEditorMeta> editorMetas = new LinkedHashMap<String, AbstractEditorMeta>();
	
	public RuleSet() {
		super();
	}
	
	public RuleSet(com.bstek.dorado.idesupport.model.RuleSet ruleSet) {
		this();
		this.originalObject = ruleSet;
		
		this.fillBaseInfo();
		RuleSetBuilder builder = RuleSetBuilder.create();
		Map<com.bstek.dorado.idesupport.model.Rule, Rule> ruleMap = builder.build(this.originalObject);
		this.fillRules(ruleMap);
		this.getEditorMetas().putAll(builder.editorMetas);
		this.fillAuxiliaryRules();
	}

	protected void fillRules(Map<com.bstek.dorado.idesupport.model.Rule, Rule> ruleMap) {
		rules = new ArrayList<Rule>(ruleMap.values());
		ruleCache = new HashMap<String, Rule>(rules.size());
		for (Rule rule: rules) {
			ruleCache.put(rule.getId(), rule);
		}
		
	}
	
	public com.bstek.dorado.idesupport.model.RuleSet getOriginalObject() {
		return originalObject;
	}
	public Map<String, AbstractEditorMeta> getEditorMetas() {
		return editorMetas;
	}
	public Collection<Rule> getRules() {
		return rules;
	}
	public Rule getRule(String id) {
		return ruleCache.get(id);
	}
	public Rule getRule(Class<?> clazz) {
		String name = clazz.getSimpleName();
		for (int tryCount = 0; true; tryCount++) {
			if (tryCount > 0) name += "_" + tryCount;
			Rule rule = this.getRule(name);
			if (rule != null) {
				com.bstek.dorado.idesupport.model.Rule drule = rule.getOriginalObject();
				if (drule != null) {
					if (clazz.getName().equals(drule.getType())) {
						return rule;
					}
				} else {
					return rule;
				}
			} else {
				return null;
			}
		}
	}
	
	public String getVersion() {
		return version;
	}
	
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartObject();
			//输出“基本信息”
			jsonGenerator.writeStringField(Treaty.RuleSet.VERSION, version);
			if (!this.packageInfos.isEmpty()) {
				jsonGenerator.writeFieldName(Treaty.RuleSet.PACKAGE_INFOS);
				jsonGenerator.writeStartArray();
				for (Map<String, String> pkgInfo: packageInfos) {
					jsonGenerator.writeStartObject();
					for (String key: pkgInfo.keySet()) {
						String value = pkgInfo.get(key);
						jsonGenerator.writeStringField(key, value);
					}
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndArray();
			}
			{//输出“规则”列表
				jsonGenerator.writeFieldName(Treaty.RuleSet.RULES);
				jsonGenerator.writeStartObject();
				{//rules.ids []
					jsonGenerator.writeFieldName("ids");
					jsonGenerator.writeStartArray();
					for (Rule rule: this.getRules()) {
						jsonGenerator.writeString(rule.getId());
					}
					jsonGenerator.writeEndArray();
				}
				{//rules.data {}
					jsonGenerator.writeFieldName("data");
					jsonGenerator.writeStartObject();
					for (Rule rule: this.getRules()) {
						jsonGenerator.writeFieldName(rule.getId());
						rule.output(context);
					}
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndObject();
			}
			{//输出“编辑器”列表
				jsonGenerator.writeFieldName(Treaty.RuleSet.EDITOR_METAS);
				jsonGenerator.writeStartObject();
				Map<String, AbstractEditorMeta> editorMetas = this.getEditorMetas();
				for (String editorName: editorMetas.keySet()) {
					AbstractEditorMeta editorMeta = editorMetas.get(editorName);
					jsonGenerator.writeFieldName(editorMeta.getName());
					editorMeta.output(context);
				}
				jsonGenerator.writeEndObject();
			}
			{//输出“结构定义”
				jsonGenerator.writeFieldName(Treaty.RuleSet.DEFINITIONS);
				jsonGenerator.writeStartObject();
				IOutputFiledsable[] ofs = new IOutputFiledsable[]{
						new Rule(), new Property(), new ClientEvent(), new Child()};
				for (IOutputFiledsable of: ofs) {
					jsonGenerator.writeFieldName(of.getClass().getSimpleName());
					jsonGenerator.writeStartObject();
					AbstractField<?>[] fields = of.getOutputFilelds();
					for (AbstractField<?> field: fields) {
						String name = field.getName();
						Object ignoredValue = field.getIgnoredValue();
						jsonGenerator.writeObjectField(name, ignoredValue);
					}
					jsonGenerator.writeEndObject();
				}
				jsonGenerator.writeEndObject();
			}
			jsonGenerator.writeEndObject();
			
			jsonGenerator.flush();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Rule findRule(Rule parentRule, Element element, int clientType) {
		Rule rule = null;
		
		Child[] children = parentRule.getChildren();
		
		for (Child child: children) {
			if (rule == null) {
				rule = child.findRule(this, element, clientType);
			} else {
				break;
			}
		}
		
		if (rule == null) {
			String tagName = element.getName();
			if (this.auxiliaryRuleNames.contains(tagName)) {
				rule = this.getRule(tagName);
			}
		}
		
		if (rule == null) 
			throw new XmlException(element, "no rule be found. [parentRule=" + parentRule.getId() + "]");
		return rule;
	}
	
	//--------------------------------------- private methods ---------------------------------------
	private void fillBaseInfo() {
		this.version = originalObject.getVersion();
		for (PackageInfo pkgInfo: originalObject.getPackageInfos()) {
			Map<String, String> pkg = new HashMap<String, String>(5);
			pkg.put(Treaty.PackageInfo.NAME, pkgInfo.getName());
			String version = pkgInfo.getVersion();
			if (version != null && version.length() > 0) {
				pkg.put(Treaty.PackageInfo.VERSION, version);
			}
			String addonVersion = pkgInfo.getAddonVersion();
			if (addonVersion != null && addonVersion.length() > 0) {
				pkg.put(Treaty.PackageInfo.ADDON_VERSION, addonVersion);
			}
			packageInfos.add(pkg);
		}
	}
	
	private void fillAuxiliaryRules() {
		LinkedHashSet<String> auxiliaryRuleNames = new LinkedHashSet<String>();
		Rule rule = this.getRule("Auxiliary");
		if (rule != null) {
			Child[] children = rule.getChildren();
			for (Child child: children) {
				String[] names = child.getMemberRuleIDs();
				for (String name: names) {
					auxiliaryRuleNames.add(name);
				}
			}
		}
		
		this.auxiliaryRuleNames = auxiliaryRuleNames;
	}
}
