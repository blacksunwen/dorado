package com.bstek.dorado.data.type.property;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.keyvalue.MultiKey;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.proxy.ChildrenListSupport;

/**
 * 数据参照属性。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 22, 2007
 */
@XmlNode(parser = "spring:dorado.lookupParser")
@ClientObject(prototype = "dorado.Lookup", shortTypeName = "Lookup")
public class Lookup extends LazyPropertyDef {

	private class ConstraintList<E> extends ChildrenListSupport<E> {
		public ConstraintList() {
			super(new ArrayList<E>());
		}

		@Override
		protected void childAdded(E child) {
			cacheKey = null;
		}

		@Override
		protected void childRemoved(E child) {
			cacheKey = null;
		}
	}

	private DataProvider dataProvider;
	private List<LookupConstraint> constraints = new ConstraintList<LookupConstraint>();
	private String lookupProperty;
	private Object cacheKey;
	private int cacheTimeToLiveSeconds;
	private int cacheTimeToIdleSeconds;

	private String dataSet;

	public Lookup() {
		setActiveAtClient(false);
	}

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
		cacheKey = null;
	}

	@XmlProperty(ignored = true)
	@ClientProperty(outputter = "spring:dorado.dataProviderPropertyOutputter")
	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public LookupConstraint addConstraint(LookupConstraint constraint) {
		constraints.add(constraint);
		return constraint;
	}

	@XmlSubNode
	public List<LookupConstraint> getConstraints() {
		return constraints;
	}

	@XmlProperty(parser = "spring:dorado.staticDataPropertyParser")
	public String getLookupProperty() {
		return lookupProperty;
	}

	public void setLookupProperty(String lookupProperty) {
		this.lookupProperty = lookupProperty;
	}

	public boolean shouldIntercept() {
		return true;
	}

	@ClientProperty(ignored = true)
	public int getCacheTimeToIdleSeconds() {
		return cacheTimeToIdleSeconds;
	}

	public void setCacheTimeToIdleSeconds(int cacheTimeToIdleSeconds) {
		this.cacheTimeToIdleSeconds = cacheTimeToIdleSeconds;
	}

	/**
	 * 返回缓存过期前的存活时间（单位：秒）。
	 */
	@ClientProperty(ignored = true)
	public int getCacheTimeToLiveSeconds() {
		return cacheTimeToLiveSeconds;
	}

	public void setCacheTimeToLiveSeconds(int cacheTimeToLiveSeconds) {
		this.cacheTimeToLiveSeconds = cacheTimeToLiveSeconds;
	}

	public Object getCacheKey() {
		if (cacheKey == null) {
			Assert.notNull(dataProvider);
			Assert.notNull(constraints);
			String keyProperties = "";
			for (LookupConstraint constraint : constraints) {
				if (keyProperties.length() > 0)
					keyProperties += '^';
				keyProperties += constraint.getLookupKeyProperty();
			}
			cacheKey = new MultiKey(dataProvider.getName(), keyProperties);
		}
		return cacheKey;
	}

	public void setCacheKey(Object cacheKey) {
		this.cacheKey = cacheKey;
	}

	/**
	 * 返回用于为参照属性提供参照数据的DataSet的id。
	 */
	public String getDataSet() {
		return dataSet;
	}

	/**
	 * 设置用于为参照属性提供参照数据的DataSet的id。
	 * <p>
	 * 参照属性的dataProvider和dataSet这两个属性具有相似的作用，我们只需要定义其中的一个。
	 * 如果同时定义了这两个属性，那么dataProvider属性将被忽略。
	 * </p>
	 */
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
}
