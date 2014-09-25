package com.bstek.dorado.vidorsupport.rule;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;
import com.bstek.dorado.vidorsupport.internal.rule.Child;
import com.bstek.dorado.vidorsupport.internal.rule.Property;
import com.bstek.dorado.vidorsupport.internal.rule.Rule;
import com.bstek.dorado.vidorsupport.internal.rule.RuleSet;

public class OutputTest extends AbstractCloudoTestCase {

	@Override
	protected String[] getAddonsConfigLocation() {
		String[] superLocations = super.getAddonsConfigLocation();
		List<String> locations = new ArrayList<String>();
		for (String l: superLocations) {
			locations.add(l);
		}
		locations.add("classpath:com/bstek/dorado/tageditor/components-context.xml");
		return locations.toArray(new String[0]);
	}
	
	public void test_Error() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		long l1 = System.currentTimeMillis();
		String actual = CloudoTestHelper.toString(rs);
		long l2 = System.currentTimeMillis();
		System.out.println(actual);
		System.out.println("* "+ (l2 - l1) + " ms");
	}
	
	public void test_Container() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Container";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child = children[0];
		Assert.assertEquals("Children", child.getName());
		Assert.assertEquals("children", child.getProperty());
		Assert.assertEquals(false, child.isWrappered());
		Assert.assertEquals(false, child.isFixed());
		Assert.assertEquals(true, child.isMemberAggregated());
		Assert.assertEquals(false, child.isAggregated());
	}
	
	public void test_FloatContainer() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "FloatContainer";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child = children[0];
		Assert.assertEquals("Children", child.getName());
		Assert.assertEquals("children", child.getProperty());
		Assert.assertEquals(false, child.isWrappered());
		Assert.assertEquals(false, child.isFixed());
		Assert.assertEquals(true, child.isMemberAggregated());
		Assert.assertEquals(false, child.isAggregated());
	}
	
	public void test_HtmlContainer() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "HtmlContainer";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child = children[0];
		Assert.assertEquals("Children", child.getName());
		Assert.assertEquals("children", child.getProperty());
		Assert.assertEquals(false, child.isWrappered());
		Assert.assertEquals(false, child.isFixed());
		Assert.assertEquals(true, child.isMemberAggregated());
		Assert.assertEquals(false, child.isAggregated());
	}
	
	public void test_FieldSet() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "FieldSet";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(2, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Buttons", child1.getName());
		Assert.assertEquals("buttons", child1.getProperty());
		Assert.assertEquals(true, child1.isWrappered());
		Assert.assertEquals(true, child1.isFixed());
		Assert.assertEquals(true, child1.isMemberAggregated());
		Assert.assertEquals(false, child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Children", child2.getName());
		Assert.assertEquals("children", child2.getProperty());
		Assert.assertEquals(true, child2.isWrappered());
		Assert.assertEquals(true, child2.isFixed());
		Assert.assertEquals(true, child2.isMemberAggregated());
		Assert.assertEquals(false, child2.isAggregated());
	}
	
	public void test_GroupBox() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "GroupBox";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(2, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Buttons", child1.getName());
		Assert.assertEquals("buttons", child1.getProperty());
		Assert.assertEquals(true, child1.isWrappered());
		Assert.assertEquals(true, child1.isFixed());
		Assert.assertEquals(true, child1.isMemberAggregated());
		Assert.assertEquals(false, child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Children", child2.getName());
		Assert.assertEquals("children", child2.getProperty());
		Assert.assertEquals(true, child2.isWrappered());
		Assert.assertEquals(true, child2.isFixed());
		Assert.assertEquals(true, child2.isMemberAggregated());
		Assert.assertEquals(false, child2.isAggregated());
	}
	
	public void test_Panel() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Panel";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(3, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Buttons", child1.getName());
		Assert.assertEquals("buttons", child1.getProperty());
		Assert.assertEquals(true,      child1.isWrappered());
		Assert.assertEquals(true,      child1.isFixed());
		Assert.assertEquals(true,      child1.isMemberAggregated());
		Assert.assertEquals(false,     child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Children", child2.getName());
		Assert.assertEquals("children", child2.getProperty());
		Assert.assertEquals(true,       child2.isWrappered());
		Assert.assertEquals(true,       child2.isFixed());
		Assert.assertEquals(true,       child2.isMemberAggregated());
		Assert.assertEquals(false,      child2.isAggregated());
		
		Child child3 = children[2];
		Assert.assertEquals("Tools", child3.getName());
		Assert.assertEquals("tools", child3.getProperty());
		Assert.assertEquals(true,    child3.isWrappered());
		Assert.assertEquals(true,    child3.isFixed());
		Assert.assertEquals(true,    child3.isMemberAggregated());
		Assert.assertEquals(false,   child3.isAggregated());
	}
	
	public void test_FloatPanel() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "FloatPanel";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(3, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Buttons", child1.getName());
		Assert.assertEquals("buttons", child1.getProperty());
		Assert.assertEquals(true,      child1.isWrappered());
		Assert.assertEquals(true,      child1.isFixed());
		Assert.assertEquals(true,      child1.isMemberAggregated());
		Assert.assertEquals(false,     child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Children", child2.getName());
		Assert.assertEquals("children", child2.getProperty());
		Assert.assertEquals(true,       child2.isWrappered());
		Assert.assertEquals(true,       child2.isFixed());
		Assert.assertEquals(true,       child2.isMemberAggregated());
		Assert.assertEquals(false,      child2.isAggregated());
		
		Child child3 = children[2];
		Assert.assertEquals("Tools", child3.getName());
		Assert.assertEquals("tools", child3.getProperty());
		Assert.assertEquals(true,    child3.isWrappered());
		Assert.assertEquals(true,    child3.isFixed());
		Assert.assertEquals(true,    child3.isMemberAggregated());
		Assert.assertEquals(false,   child3.isAggregated());
	} 
	
	public void test_Dialog() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Dialog";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(3, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Buttons", child1.getName());
		Assert.assertEquals("buttons", child1.getProperty());
		Assert.assertEquals(true,      child1.isWrappered());
		Assert.assertEquals(true,      child1.isFixed());
		Assert.assertEquals(true,      child1.isMemberAggregated());
		Assert.assertEquals(false,     child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Children", child2.getName());
		Assert.assertEquals("children", child2.getProperty());
		Assert.assertEquals(true,       child2.isWrappered());
		Assert.assertEquals(true,       child2.isFixed());
		Assert.assertEquals(true,       child2.isMemberAggregated());
		Assert.assertEquals(false,      child2.isAggregated());
		
		Child child3 = children[2];
		Assert.assertEquals("Tools", child3.getName());
		Assert.assertEquals("tools", child3.getProperty());
		Assert.assertEquals(true,    child3.isWrappered());
		Assert.assertEquals(true,    child3.isFixed());
		Assert.assertEquals(true,    child3.isMemberAggregated());
		Assert.assertEquals(false,   child3.isAggregated());
	} 
	
	public void test_TabControl() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "TabControl";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("tabs", child1.getName());
		Assert.assertEquals("tabs", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_VerticalTabControl() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "VerticalTabControl";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("tabs", child1.getName());
		Assert.assertEquals("tabs", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_ControlTab() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "ControlTab";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Control", child1.getName());
		Assert.assertEquals("control", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(false,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_TabBar() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "TabBar";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals("tabs", child1.getName());
		Assert.assertEquals("tabs", child1.getProperty());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_TabColumn() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "TabColumn";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("tabs", child1.getName());
		Assert.assertEquals("tabs", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_SplitPanel() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "SplitPanel";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(2, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("MainControl", child1.getName());
		Assert.assertEquals("mainControl", child1.getProperty());
		Assert.assertEquals(true,  child1.isFixed());
		Assert.assertEquals(false, child1.isMemberAggregated());
		Assert.assertEquals(true,  child1.isWrappered());
		Assert.assertEquals(false, child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("SideControl", child2.getName());
		Assert.assertEquals("sideControl", child2.getProperty());
		Assert.assertEquals(true,  child2.isWrappered());
		Assert.assertEquals(true,  child2.isFixed());
		Assert.assertEquals(false, child2.isMemberAggregated());
		Assert.assertEquals(false, child2.isAggregated());
	}
	
	public void test_Accordion() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Accordion";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Sections", child1.getName());
		Assert.assertEquals("sections", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_Section() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Section";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Control", child1.getName());
		Assert.assertEquals("control", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(false,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_UpdateAction() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "UpdateAction";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("UpdateItems", child1.getName());
		Assert.assertEquals("updateItems", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_Menu() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Menu";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Items", child1.getName());
		Assert.assertEquals("items", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}

	public void test_MenuItem() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "MenuItem";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Items", child1.getName());
		Assert.assertEquals("items", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}

	public void test_CheckableMenuItem() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "CheckableMenuItem";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Items", child1.getName());
		Assert.assertEquals("items", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}

	public void test_ControlMenuItem() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "ControlMenuItem";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Control", child1.getName());
		Assert.assertEquals("control", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(false,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_RadioGroup() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "RadioGroup";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("RadioButtons", child1.getName());
		Assert.assertEquals("radioButtons", child1.getProperty());
		Assert.assertEquals(false,  child1.isWrappered());
		Assert.assertEquals(false,  child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_FormElement() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "FormElement";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Editor", child1.getName());
		Assert.assertEquals("editor", child1.getProperty());
		Assert.assertEquals(true,   child1.isWrappered());
		Assert.assertEquals(true,   child1.isFixed());
		Assert.assertEquals(false,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_AutoForm() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "AutoForm";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Elements", child1.getName());
		Assert.assertEquals("elements", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_ListDropDown() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "ListDropDown";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Columns", child1.getName());
		Assert.assertEquals("columns", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,    child1.isMemberAggregated());
		Assert.assertEquals(false,   child1.isAggregated());
	}
	
	public void test_DataGrid() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "DataGrid";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Columns", child1.getName());
		Assert.assertEquals("columns", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,    child1.isMemberAggregated());
		Assert.assertEquals(false,   child1.isAggregated());
	}
	
	public void test_DataColumn() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "DataColumn";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Editor", child1.getName());
		Assert.assertEquals("editor", child1.getProperty());
		Assert.assertEquals(true,   child1.isWrappered());
		Assert.assertEquals(true,   child1.isFixed());
		Assert.assertEquals(false,  child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_ColumnGroup() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "ColumnGroup";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Columns", child1.getName());
		Assert.assertEquals("columns", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,    child1.isMemberAggregated());
		Assert.assertEquals(false,   child1.isAggregated());
	}
	
	public void test_Tree() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Tree";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Nodes", child1.getName());
		Assert.assertEquals("nodes", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,    child1.isMemberAggregated());
		Assert.assertEquals(false,   child1.isAggregated());
	}
	
	public void test_Node() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "Node";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Nodes", child1.getName());
		Assert.assertEquals("nodes", child1.getProperty());
		Assert.assertEquals(false,   child1.isWrappered());
		Assert.assertEquals(false,   child1.isFixed());
		Assert.assertEquals(true,    child1.isMemberAggregated());
		Assert.assertEquals(false,   child1.isAggregated());
	}
	
	//DataTree
	public void test_DataTree() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "DataTree";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("BindingConfigs", child1.getName());
		Assert.assertEquals("bindingConfigs", child1.getProperty());
		Assert.assertEquals(true,   child1.isWrappered());
		Assert.assertEquals(true,   child1.isFixed());
		Assert.assertEquals(true,   child1.isMemberAggregated());
		Assert.assertEquals(false,  child1.isAggregated());
	}
	
	public void test_BindingConfig() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "BindingConfig";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(1, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("ChildBindingConfigs", child1.getName());
		Assert.assertEquals("childBindingConfigs", child1.getProperty());
		Assert.assertEquals(false, child1.isWrappered());
		Assert.assertEquals(false, child1.isFixed());
		Assert.assertEquals(true,  child1.isMemberAggregated());
		Assert.assertEquals(false, child1.isAggregated());
	}
	
	public void test_TreeGrid() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "TreeGrid";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(2, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("Columns", child1.getName());
		Assert.assertEquals("columns", child1.getProperty());
		Assert.assertEquals(true, child1.isWrappered());
		Assert.assertEquals(true, child1.isFixed());
		Assert.assertEquals(true, child1.isMemberAggregated());
		Assert.assertEquals(false, child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Nodes", child2.getName());
		Assert.assertEquals("nodes", child2.getProperty());
		Assert.assertEquals(true, child2.isWrappered());
		Assert.assertEquals(true, child2.isFixed());
		Assert.assertEquals(true, child2.isMemberAggregated());
		Assert.assertEquals(false, child2.isAggregated());
	}
	
	public void test_DataTreeGrid() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "DataTreeGrid";
		Rule rule = rs.getRule(ruleId);
		
		Child[] children = rule.getChildren();
		Assert.assertEquals(2, children.length);
		
		Child child1 = children[0];
		Assert.assertEquals("BindingConfigs", child1.getName());
		Assert.assertEquals("bindingConfigs", child1.getProperty());
		Assert.assertEquals(true, child1.isWrappered());
		Assert.assertEquals(true, child1.isFixed());
		Assert.assertEquals(true, child1.isMemberAggregated());
		Assert.assertEquals(false,child1.isAggregated());
		
		Child child2 = children[1];
		Assert.assertEquals("Columns", child2.getName());
		Assert.assertEquals("columns", child2.getProperty());
		Assert.assertEquals(true, child2.isWrappered());
		Assert.assertEquals(true, child2.isFixed());
		Assert.assertEquals(true, child2.isMemberAggregated());
		Assert.assertEquals(false, child2.isAggregated());
	}
	
	public void test_StringArray() throws Exception {
		RuleSet rs = CloudoTestHelper.getRuleSet();
		
		String ruleId = "TagEditor";
		Rule rule = rs.getRule(ruleId);
		Property property = rule.getProperty("availableTags");
		Assert.assertEquals("array[string]", property.getEditorType());
	}
}
