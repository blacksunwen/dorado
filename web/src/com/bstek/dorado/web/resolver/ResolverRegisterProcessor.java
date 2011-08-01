package com.bstek.dorado.web.resolver;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-15
 */
public class ResolverRegisterProcessor extends ApplicationObjectSupport {
	private UrlResolverMapping urlResolverMapping;

	public void setUrlResolverMapping(UrlResolverMapping urlResolverMapping) {
		this.urlResolverMapping = urlResolverMapping;
	}

	@Override
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();

		Set<ResolverRegister> treeSet = new TreeSet<ResolverRegister>(
				new Comparator<ResolverRegister>() {
					public int compare(ResolverRegister o1, ResolverRegister o2) {
						int result = o1.getOrder() - o2.getOrder();
						if (result == 0) {
							result = o1.hashCode() - o2.hashCode();
							if (result == 0)
								result = -1;
						}
						return result;
					}
				});

		Map<String, ResolverRegister> resolverRegisters = getApplicationContext()
				.getBeansOfType(ResolverRegister.class);
		treeSet.addAll(resolverRegisters.values());
		for (ResolverRegister resolverRegister : treeSet) {
			urlResolverMapping.registerHandler(resolverRegister.getUrl(),
					resolverRegister.getResolver());
		}
	}
}
