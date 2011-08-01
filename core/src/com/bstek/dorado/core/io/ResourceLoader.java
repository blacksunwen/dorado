package com.bstek.dorado.core.io;

import java.io.IOException;

/**
 * 资源装载的接口。其功能类似与资源描述对象的工厂。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 19, 2007
 */
public interface ResourceLoader {
	/**
	 * 根据资源路径获取相应的资源描述对象。
	 * @param resourceLocation 资源路径
	 * @return 资源描述对象
	 * @see com.bstek.dorado.core.io.Resource
	 * @see com.bstek.dorado.core.io.ResourceLoader
	 */
	Resource getResource(String resourceLocation);

	/**
	 * 根据资源路径获取相应的资源描述对象的数组。<br>
	 * 此处的资源路径可支持通配符，因此可以返回一个以上的资源描述对象。
	 * @param locationPattern 资源路径（可支持通配符）
	 * @return 资源描述对象的数组
	 * @throws IOException
	 * @see com.bstek.dorado.core.io.Resource
	 * @see com.bstek.dorado.core.io.ResourceLoader
	 */
	Resource[] getResources(String locationPattern) throws IOException;

	/**
	 * 获取内部使用的{@link com.bstek.dorado.core.io.ResourceLoader}在查找资源时使用的ClassLoader。
	 * @see java.lang.ClassLoader
	 */
	ClassLoader getClassLoader();
}
