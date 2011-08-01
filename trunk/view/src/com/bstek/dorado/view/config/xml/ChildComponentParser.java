/**
 * 
 */
package com.bstek.dorado.view.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionInitOperation;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.view.config.definition.ComponentDefinition;
import com.bstek.dorado.view.config.definition.ContainerDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-1
 */
public class ChildComponentParser extends ConfigurableDispatchableXmlParser {

	private static class AddComponentOperation implements
			DefinitionInitOperation {
		private ComponentDefinition component;

		public AddComponentOperation(ComponentDefinition component) {
			this.component = component;
		}

		public void execute(Object object, CreationContext context)
				throws Exception {
			ContainerDefinition container = (ContainerDefinition) object;
			container.appendChild(component);
		}
	}

	private XmlParser componentParser;

	public void setComponentParser(XmlParser componentParser) {
		this.componentParser = componentParser;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		ComponentDefinition component = (ComponentDefinition) componentParser
				.parse(node, context);
		return new AddComponentOperation(component);
	}
}
