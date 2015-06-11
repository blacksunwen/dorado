package com.bstek.dorado.vidorsupport.internal;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.bstek.dorado.common.ClientType;
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

	public String read(InputStream input, String encoding, RuleSet ruleSet)
			throws Exception {
		return this.read(input, encoding, ruleSet,null);
	}

	@Override
	public String read(InputStream input, String encoding, RuleSet ruleSet,
			String clientType) throws Exception {
		
		if (clientType == null){
			clientType=ClientType.DESKTOP_NAME;
		}
				
		com.bstek.dorado.vidorsupport.rule.RuleSet ruleSET;
		if (ruleSet == null) {
			ruleSET = this.getRuleSetFactory().get();
		} else {
			ruleSET = new com.bstek.dorado.vidorsupport.rule.RuleSet(ruleSet);
		}
		XMLReader xmlReader = new XMLReader();

		XmlNode xmlNode = xmlReader.parse(input, encoding, ruleSET,clientType);
		return toJSON(xmlNode);
	}
	
	
	protected String toJSON(XmlNode xmlNode) {
		OutputContext context = new OutputContext();
		xmlNode.output(context);
		String json = context.getWriter().toString();
		Log log = Vidor.LOG.getLog("viewReader");
		if (log.isDebugEnabled()) {
			log.debug("View JSON ->\n" + json);
		}
		return json;
	}
	
}

class XMLReader extends XmlReader {
	public XmlNode parse(InputStream input, String encoding,
			com.bstek.dorado.vidorsupport.rule.RuleSet ruleSet)
			throws Exception {
		return this.parse(input, encoding, ruleSet, ClientType.DESKTOP_NAME);
	}

	public XmlNode parse(InputStream input, String encoding,
			com.bstek.dorado.vidorsupport.rule.RuleSet ruleSet,
			String clientType) throws Exception {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding(encoding);
		Document document = saxReader.read(input);
		XmlReader.ParseParameter parseParameter = new XmlReader.ParseParameter();
		parseParameter.setClientType(clientType);
		parseParameter.setRuleSet(ruleSet);
		XmlNode result = this.parse(document, parseParameter);

		return result;
	}
}
