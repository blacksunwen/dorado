package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.JdbcParameterSource;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	implTypes = {"com.bstek.dorado.jdbc.model.autotable.*"},
	definitionType = "com.bstek.dorado.jdbc.model.autotable.MatchRuleDefinition"
)
public abstract class AbstractMatchRule {
	private boolean available = true;
	
	@ClientProperty(escapeValue = "true")
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public abstract String token(AutoTable autoTable, JdbcParameterSource parameterSource);
}
