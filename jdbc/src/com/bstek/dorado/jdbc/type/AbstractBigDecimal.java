package com.bstek.dorado.jdbc.type;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class AbstractBigDecimal extends AbstractJdbcType {

	public AbstractBigDecimal(String jdbcName) {
		super(jdbcName, "BigDecimal");
	}

}
