package com.bstek.dorado.vidorsupport.internal;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.vidorsupport.Vidor;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetFactory;
import com.bstek.dorado.vidorsupport.iapi.IViewReader;
import com.bstek.dorado.vidorsupport.output.OutputContext;
import com.bstek.dorado.vidorsupport.vidor.XmlNode;
import com.bstek.dorado.vidorsupport.vidor.XmlReader;

public class ViewReader implements IViewReader {
	private IRuleSetFactory ruleSetFactory;

	public IRuleSetFactory getRuleSetFactory() {
		return ruleSetFactory;
	}

	public void setRuleSetFactory(IRuleSetFactory ruleSetFactory) {
		this.ruleSetFactory = ruleSetFactory;
	}

	public String read(InputStream input, String encoding) throws Exception {
		return this.read(input, encoding, null);
	}

	protected String toJSON(XmlNode xmlNode) {
		OutputContext context = new OutputContext();
		xmlNode.output(context);
		String json =  context.getWriter().toString();
		Log log = Vidor.LOG.getLog("viewReader");
		if (log.isDebugEnabled()) {
			log.debug("View JSON ->\n" + json);
		}
		return json;
	}

	@Override
	public String read(InputStream input, String encoding, RuleSet ruleSet)
			throws Exception {
		// TODO Auto-generated method stub
		com.bstek.dorado.vidorsupport.rule.RuleSet ruleSET;
		if (ruleSet == null) {
			ruleSET = this.getRuleSetFactory().get();
		} else {
			ruleSET = new com.bstek.dorado.vidorsupport.rule.RuleSet(ruleSet);
		}
		XMLReader xmlReader = new XMLReader();

		XmlNode xmlNode = xmlReader.parse(input, encoding, ruleSET);
		return toJSON(xmlNode);
	}
}

class XMLReader extends XmlReader {
	public XmlNode parse(InputStream input, String encoding, com.bstek.dorado.vidorsupport.rule.RuleSet ruleSet)
			throws Exception {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding(encoding);
		Document document = saxReader.read(input);
		XmlReader.ParseParameter parseParameter = new XmlReader.ParseParameter();
		parseParameter.setRuleSet(ruleSet);
		XmlNode result = this.parse(document, parseParameter);

		return result;
	}

}
