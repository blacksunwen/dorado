package com.bstek.dorado.vidorsupport.vidor;

import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;
import com.bstek.dorado.vidorsupport.internal.vidor.CollectionlNode;
import com.bstek.dorado.vidorsupport.internal.vidor.EntityNode;
import com.bstek.dorado.vidorsupport.internal.vidor.EventNode;
import com.bstek.dorado.vidorsupport.internal.vidor.LayoutAttribute;
import com.bstek.dorado.vidorsupport.internal.vidor.PositionAttribute;
import com.bstek.dorado.vidorsupport.internal.vidor.PropertyNode;
import com.bstek.dorado.vidorsupport.internal.vidor.ValueNode;
import com.bstek.dorado.vidorsupport.internal.vidor.XmlNode;

public class XmlReaderTest extends AbstractCloudoTestCase {
	private XmlNode parse(URL url) throws Exception {
		XmlNode actual = CloudoTestHelper.parseXmlNode(url);
		return actual;
	}
	
	@Test
	public void test_01() throws Exception {
		URL url = this.getClass().getResource("01.Empty.view.xml");
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_02() throws Exception {
		URL url = this.getClass().getResource("02.Property1.view.xml");
		
		XmlNode expected = new XmlNode("ViewConfig");
		{
			expected.getAttributes().put("listener", "lis");
			
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_03() throws Exception {
		URL url = this.getClass().getResource("03.Property2.view.xml");
		
		XmlNode expected = new XmlNode("ViewConfig");
		{
			expected.getAttributes().put("listener", "lis");
			
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			View.getAttributes().put("contentOverflow", "scroll");
			
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_04() throws Exception {
		URL url = this.getClass().getResource("04.Property3.view.xml");
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			XmlNode metaData = new PropertyNode();
			metaData.getAttributes().put("name", "metaData");
			metaData.getAttributeNodes().put("key1", new PropertyNode("key1", "value1"));
			metaData.getAttributeNodes().put("key2", new PropertyNode("key2", "value2"));
			
			View.getAttributeNodes().put("metaData", metaData);
			
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_05() throws Exception {
		URL url = this.getClass().getResource("05.Property4.view.xml");
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				XmlNode DataSet = new XmlNode("DataSet");
				DataSet.getAttributes().put("id", "dataSet1");
				DataSet.getAttributes().put("userData", "sdfdf");
				XmlNode Entity = new EntityNode();
				Entity.getAttributes().put("key1", "value1");
				Entity.getAttributes().put("key2", "value2");
				Entity.getAttributes().put("key3", "value3");
				DataSet.getAttributeNodes().put("parameter", Entity);
				View.getNodes().add(DataSet);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_06() throws Exception {
		URL url = this.getClass().getResource("06.Property5.view.xml");
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				XmlNode DataSet = new XmlNode("DataSet");
				DataSet.getAttributes().put("id", "dataSet1");
				XmlNode Collection = new CollectionlNode();
				XmlNode Entity = new EntityNode();
				Entity.getAttributes().put("key1", "value1");
				Entity.getAttributes().put("key2", "value2");
				Collection.getNodes().add(Entity);
				XmlNode Value = new ValueNode();
				Value.setText("sss");
				Collection.getNodes().add(Value);
				
				DataSet.getAttributeNodes().put("userData", Collection);
				View.getNodes().add(DataSet);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_07() throws Exception {
		URL url = this.getClass().getResource("07.Mapping.view.xml");
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				XmlNode DataType = new XmlNode("DataType");
				DataType.getAttributes().put("name", "dataType1");
				
				XmlNode PropertyDef = new XmlNode("BasePropertyDef");
				PropertyDef.getAttributes().put("name", "propertyDef1");
				PropertyNode mapping = new PropertyNode();
				mapping.getAttributes().put("name", "mapping");
				mapping.getAttributeNodes().put("keyProperty", new PropertyNode("keyProperty", "key"));
				CollectionlNode Collection = new CollectionlNode();
				EntityNode entity1 = new EntityNode();
				entity1.getAttributes().put("key", "key1");
				entity1.getAttributes().put("value", "value1");
				Collection.getNodes().add(entity1);
				EntityNode entity2 = new EntityNode();
				entity2.getAttributes().put("key", "key2");
				entity2.getAttributes().put("value", "value2");
				Collection.getNodes().add(entity2);
				mapping.getAttributeNodes().put("mapValues", Collection);
				mapping.getAttributeNodes().put("valueProperty", new PropertyNode("valueProperty", "value"));
				PropertyDef.getAttributeNodes().put("mapping", mapping);
				
				DataType.getNodes().add(PropertyDef);
				Model.getNodes().add(DataType);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_08() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				XmlNode AutoForm = new XmlNode("AutoForm");
				XmlNode AutoFormElement = new XmlNode("AutoFormElement");
				XmlNode Wrapper_Editor = new XmlNode("AutoFormElement/Wrapper.Editor");
				XmlNode TextArea = new XmlNode("TextArea");
				
				Wrapper_Editor.getNodes().add(TextArea);
				AutoFormElement.getNodes().add(Wrapper_Editor);
				AutoForm.getNodes().add(AutoFormElement);
				
				View.getNodes().add(AutoForm);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("08.Wrapper.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_09() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				EventNode e1 = new EventNode();
				e1.getAttributes().put("name", "onReady");
				e1.setText("if (1<2) {\n\tconsole.log(123);\t\n}\n");
				View.getEventNodes().add(e1);
				
				EventNode e2 = new EventNode();
				e2.getAttributes().put("name", "onCreate");
				e2.getAttributes().put("signature", "self,arg,HHH");
				e2.setText("console.log(\"HHH\");\n");
				View.getEventNodes().add(e2);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("09.Event.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_10() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("padding", "padding");
				layout.getAttributeMap().put("regionPadding", "regionPadding");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				PositionAttribute position = new PositionAttribute();
				position.setType("left");
				position.getAttributeMap().put("className", "className");
				position.getAttributeMap().put("padding", "padding");
				Button.setPosition(position);
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("10.DockLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_11() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.setType("anchor");
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("padding", "padding");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				PositionAttribute position = new PositionAttribute();
				position.getAttributeMap().put("anchorBottom", "auto");
				position.getAttributeMap().put("anchorLeft", "none");
				position.getAttributeMap().put("anchorRight", "container");
				position.getAttributeMap().put("anchorTop", "previous");
				position.getAttributeMap().put("bottom", "botton");
				position.getAttributeMap().put("className", "className");
				position.getAttributeMap().put("heightOffset", "heightOffset");
				position.getAttributeMap().put("left", "left");
				position.getAttributeMap().put("leftOffset", "leftOffset");
				position.getAttributeMap().put("padding", "padding");
				position.getAttributeMap().put("right", "right");
				position.getAttributeMap().put("top", "top");
				position.getAttributeMap().put("topOffset", "topOffset");
				position.getAttributeMap().put("widthOffset", "widthOffset");
				Button.setPosition(position);
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("11.AnchorLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_12() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.setType("hbox");
				layout.getAttributeMap().put("align", "top");
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("pack","start");
				layout.getAttributeMap().put("padding","padding");
				layout.getAttributeMap().put("regionPadding","regionPadding");
				layout.getAttributeMap().put("stretch","true");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				PositionAttribute position = new PositionAttribute();
				position.getAttributeMap().put("align", "center");
				position.getAttributeMap().put("className", "className");
				position.getAttributeMap().put("padding", "padding");
				Button.setPosition(position);
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}

		URL url = this.getClass().getResource("12.HBoxLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_13() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.setType("vbox");
				layout.getAttributeMap().put("align", "left");
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("pack","end");
				layout.getAttributeMap().put("padding","padding");
				layout.getAttributeMap().put("regionPadding","regionPadding");
				layout.getAttributeMap().put("stretch","true");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				PositionAttribute position = new PositionAttribute();
				position.getAttributeMap().put("align", "center");
				position.getAttributeMap().put("className", "className");
				position.getAttributeMap().put("padding", "padding");
				Button.setPosition(position);
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("13.VBoxLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_14() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.setType("form");
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("colPadding", "colPadding");
				layout.getAttributeMap().put("cols","cols");
				layout.getAttributeMap().put("padding","padding");
				layout.getAttributeMap().put("rowHeight","rowHeight");
				layout.getAttributeMap().put("rowPadding","rowPadding");
				layout.getAttributeMap().put("stretchWidth","true");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				PositionAttribute position = new PositionAttribute();
				position.getAttributeMap().put("align", "left");
				position.getAttributeMap().put("className", "className");
				position.getAttributeMap().put("colSpan", "colSpan");
				position.getAttributeMap().put("padding", "padding");
				position.getAttributeMap().put("rowSpan", "rowSpan");
				position.getAttributeMap().put("vAlign", "top");
				Button.setPosition(position);
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("14.FormLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_15() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				LayoutAttribute layout = new LayoutAttribute();
				layout.setType("native");
				layout.getAttributeMap().put("className", "className");
				layout.getAttributeMap().put("padding","padding");
				layout.getAttributeMap().put("style","style");
				View.setLayout(layout);
				
				XmlNode Button = new XmlNode("Button");
				Button.getAttributes().put("caption", "标题");
				View.getNodes().add(Button);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("15.NativeLayout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_16() throws Exception {
		XmlNode expected = new XmlNode("ViewConfig");
		{
			XmlNode Arguments = new XmlNode("ViewConfig/Arguments");
			XmlNode Context = new XmlNode("ViewConfig/Context");
			XmlNode Model = new XmlNode("Model");
			XmlNode View = new XmlNode("DefaultView");
			{
				XmlNode Container = new XmlNode("Container");
				LayoutAttribute containerLayout = new LayoutAttribute();
				containerLayout.setType("anchor");
				Container.setLayout(containerLayout);
				
				XmlNode Button = new XmlNode("Button");
				Button.getAttributes().put("id", "button1");
				Button.getAttributes().put("caption", "后台读取");
				PropertyNode style = new PropertyNode();
				style.getAttributes().put("name", "style");
				style.getAttributeNodes().put("ss", new PropertyNode("ss", "sss"));
				style.getAttributeNodes().put("key", new PropertyNode("key", "ss"));
				Button.getAttributeNodes().put("style", style);
				
				EventNode eventNode = new EventNode();
				eventNode.getAttributes().put("name", "onClick");
				eventNode.setText("alert('hello');\n");
				Button.getEventNodes().add(eventNode);
				Container.getNodes().add(Button);
				
				View.getNodes().add(Container);
			}
			expected.getNodes().add(Arguments);
			expected.getNodes().add(Context);
			expected.getNodes().add(Model);
			expected.getNodes().add(View);
		}
		
		URL url = this.getClass().getResource("16.Layout.view.xml");
		XmlNode actual = this.parse(url);
		Assert.assertEquals(expected, actual);
	}
}
