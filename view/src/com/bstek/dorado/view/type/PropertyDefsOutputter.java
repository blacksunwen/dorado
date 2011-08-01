package com.bstek.dorado.view.type;

import java.io.Writer;
import java.util.Map;


import com.bstek.dorado.data.type.property.Lookup;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.type.property.Reference;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.ObjectPropertyOutputter;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 23, 2008
 */
public class PropertyDefsOutputter extends ObjectPropertyOutputter {

	private Outputter basePropertyDefOutputter;
	private Outputter referencePropertyDefOutputter;
	private Outputter lookupPropertyDefOutputter;

	public void setBasePropertyDefOutputter(Outputter basePropertyDefOutputter) {
		this.basePropertyDefOutputter = basePropertyDefOutputter;
	}

	public void setReferenceOutputter(Outputter referencePropertyDefOutputter) {
		this.referencePropertyDefOutputter = referencePropertyDefOutputter;
	}

	public void setLookupOutputter(Outputter lookupPropertyDefOutputter) {
		this.lookupPropertyDefOutputter = lookupPropertyDefOutputter;
	}

	public Outputter getBasePropertyDefOutputter() {
		return basePropertyDefOutputter;
	}

	public Outputter getReferenceOutputter() {
		return referencePropertyDefOutputter;
	}

	public Outputter getLookupOutputter() {
		return lookupPropertyDefOutputter;
	}

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		Writer writer = context.getWriter();

		Map<?, ?> propertyDefs = (Map<?, ?>) object;
		if (propertyDefs.isEmpty()) {
			json.beginValue();
			writer.append("null");
			json.endValue();
			return;
		}

		json.escapeableArray();
		for (Map.Entry<?, ?> entry : propertyDefs.entrySet()) {
			PropertyDef pd = (PropertyDef) entry.getValue();
			if (pd.isIgnored()) {
				continue;
			}

			json.beginValue();
			if (pd instanceof Reference) {
				referencePropertyDefOutputter.output(pd, context);
			}
			else if (pd instanceof Lookup) {
				lookupPropertyDefOutputter.output(pd, context);
			}
			else {
				basePropertyDefOutputter.output(pd, context);
			}
			json.endValue();
		}
		json.endArray();
	}
}
