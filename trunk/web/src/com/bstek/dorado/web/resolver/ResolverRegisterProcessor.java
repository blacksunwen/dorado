package com.bstek.dorado.web.resolver;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.bstek.dorado.core.EngineStartupListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-15
 */
public class ResolverRegisterProcessor extends EngineStartupListener {
	private Map<String, ResolverRegister> resolverRegisters = new HashMap<String, ResolverRegister>();
	private UriResolverMapping urlResolverMapping;

	public void setUrlResolverMapping(UriResolverMapping urlResolverMapping) {
		this.urlResolverMapping = urlResolverMapping;
	}

	public void addResolverRegister(ResolverRegister resolverRegister) {
		resolverRegisters.put(resolverRegister.getUrl(), resolverRegister);
	}

	@Override
	public void onStartup() throws Exception {
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

		treeSet.addAll(resolverRegisters.values());
		for (ResolverRegister resolverRegister : treeSet) {
			urlResolverMapping.registerHandler(resolverRegister.getUrl(),
					resolverRegister.getResolver());
		}
	}
}
