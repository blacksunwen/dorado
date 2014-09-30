package com.bstek.dorado.vidorsupport.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.vidorsupport.Vidor;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.iapi.IViewWriter;
import com.bstek.dorado.vidorsupport.vidor.JsonReader;

public class ViewWriter implements IViewWriter {
	private IRuleSetFactory ruleSetFactory;

	public IRuleSetFactory getRuleSetFactory() {
		return ruleSetFactory;
	}

	public void setRuleSetFactory(IRuleSetFactory ruleSetFactory) {
		this.ruleSetFactory = ruleSetFactory;
	}

	public void write(String json, OutputStream output, String encoding)
			throws Exception {
		this.write(json, output, encoding, null);
	}

	@Override
	public void write(String json, OutputStream output, String encoding,
			RuleSet ruleSet) throws Exception {
		OutputStreamWriter writer = new OutputStreamWriter(output, encoding);
		String xml = this.toXML(json, ruleSet);
		try {
			writer.write(xml);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {

			}
		}
	};

	public String toXML(String json, RuleSet ruleSet) throws Exception {
		com.bstek.dorado.vidorsupport.rule.RuleSet ruleSET;
		if (ruleSet == null) {
			ruleSET = this.getRuleSetFactory().get();
		} else {
			ruleSET = new com.bstek.dorado.vidorsupport.rule.RuleSet(ruleSet);
		}
		JsonReader reader = new JsonReader();
		Document document = reader.parse(json, ruleSET);

		OutputFormat format = new OutputFormat();
		format.setIndentSize(2);
		format.setNewlines(true);
		format.setTrimText(false);
		format.setPadText(false);

		StringWriter writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		try {
			xmlWriter.write(document);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		String xml = writer.toString();
		Log log = Vidor.LOG.getLog("viewWriter");
		if (log.isDebugEnabled()) {
			log.debug("View XML ->\n" + xml);
		}
		return xml;
	}

	/* (non-Javadoc)
	 * @see com.bstek.dorado.vidorsupport.iapi.IViewWriter#toXML(java.lang.String)
	 */
	@Override
	public String toXML(String json) throws Exception {
		return this.toXML(json, null);
	}
	
	
}
