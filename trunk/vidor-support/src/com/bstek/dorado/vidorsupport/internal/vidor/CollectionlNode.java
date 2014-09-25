package com.bstek.dorado.vidorsupport.internal.vidor;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.internal.output.OutputContext;

public class CollectionlNode extends XmlNode {

	static final String NODE_NAME = "Collection";
	
	public CollectionlNode() {
		super(NODE_NAME);
	}

	@Override
	public void output(OutputContext context) {
		JsonGenerator jsonGenerator = context.getJsonGenerator();
		try {
			jsonGenerator.writeStartArray();
			List<XmlNode> nodes = this.getNodes();
			for (XmlNode node: nodes) {
				node.output(context);
			}
			jsonGenerator.writeEndArray();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
