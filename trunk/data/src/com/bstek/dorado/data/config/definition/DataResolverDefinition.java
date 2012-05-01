package com.bstek.dorado.data.config.definition;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.common.proxy.PatternMethodInterceptorFilter;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionManagerAware;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.DataModelObject;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverInterceptorInvoker;
import com.bstek.dorado.util.proxy.BaseMethodInterceptorDispatcher;
import com.bstek.dorado.util.proxy.MethodInterceptorFilter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public class DataResolverDefinition extends InterceptableDefinition implements
		DefinitionManagerAware<DataResolverDefinitionManager> {
	private DataResolverDefinitionManager dataResolverDefinitionManager;
	private String name;
	private String id;

	public DataResolverDefinition() {
		setCacheCreatedObject(true);
	}

	public DataResolverDefinition(String name) {
		this();
		setName(name);
	}

	public void setDefinitionManager(
			DataResolverDefinitionManager definitionManager) {
		dataResolverDefinitionManager = definitionManager;
	}

	/**
	 * 返回DataResolver的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置DataResolver的名称。
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
		if (StringUtils.isNotEmpty(id)) {
			setBeanId(Constants.SCOPE_DATA_RESOLVER_PREFIX + id);
		}
	}

	@Override
	protected MethodInterceptor[] getMethodInterceptors(
			CreationInfo creationInfo, CreationContext context)
			throws Exception {
		MethodInterceptor[] interceptors;
		MethodInterceptor[] customMethodInterceptors = dataResolverDefinitionManager
				.getDataResolverMethodInterceptors();
		MethodInterceptor[] superInterceptors = super.getMethodInterceptors(
				creationInfo, context);
		if (customMethodInterceptors != null) {
			MethodInterceptor rootCustomMethodInterceptor = new RootCustomDataProviderInterceptor(
					customMethodInterceptors);
			if (superInterceptors != null) {
				interceptors = new MethodInterceptor[superInterceptors.length + 1];
				interceptors[0] = rootCustomMethodInterceptor;
				System.arraycopy(superInterceptors, 0, interceptors, 1,
						superInterceptors.length);
			} else {
				interceptors = new MethodInterceptor[] { rootCustomMethodInterceptor };
			}
		} else {
			interceptors = superInterceptors;
		}
		return interceptors;
	}

	@Override
	protected MethodInterceptor getInterceptorInvoker(String interceptor) {
		return new DataResolverInterceptorInvoker(interceptor);
	}

	@Override
	protected void doInitObject(Object object, CreationInfo creationInfo,
			CreationContext context) throws Exception {
		((Namable) object).setName(name);
		if (StringUtils.isNotEmpty(id)) {
			((DataModelObject) object).setId(id);
		}
		super.doInitObject(object, creationInfo, context);
	}
}

class RootCustomDataResolverInterceptor extends BaseMethodInterceptorDispatcher {
	private static final String METHOD_NAME = "resolve";

	public RootCustomDataResolverInterceptor(
			MethodInterceptor[] subMethodInterceptors) {
		super(subMethodInterceptors);
	}

	@Override
	protected boolean filterMethod(Method method) {
		return method.getName().equals(METHOD_NAME);
	}

	@Override
	public MethodInterceptorFilter getMethodInterceptorFilter(Object object,
			Method method, Object[] args) {
		String name = ((DataResolver) object).getName();
		return new PatternMethodInterceptorFilter(name);
	}

};
