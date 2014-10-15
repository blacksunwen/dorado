package com.bstek.dorado.vidorsupport.internal;

import org.apache.commons.logging.Log;

import com.bstek.dorado.vidorsupport.Vidor;
import com.bstek.dorado.vidorsupport.rule.RuleSet;

public class GlobalRuleSetOutputter extends AbstractRuleSetOutputter {

	private String cacheStr = null;
	
	
	public String output() throws Exception {
		String out = this.doGet();
		if (out == null) {
			synchronized (this.getClass()) {
				out = this.doGet(); 
				if (out == null) {
					out = this.doCreate();
					this.doCapture(out);
				}
			}
		}
		return out;
	}

	protected String doCreate() throws Exception {
		RuleSet rs = this.getFactory().get();
		String output = this.doCreate(rs);
		return output;
	}
	
	protected String doGet() {
		return cacheStr;
	}

	protected void doCapture(String str) {
		this.cacheStr = str;
		Log log = Vidor.LOG.getLog("ruleSet");
		if (log.isDebugEnabled()) {
			log.debug("RuleSet -> \n" + str);
		}
	}

}
