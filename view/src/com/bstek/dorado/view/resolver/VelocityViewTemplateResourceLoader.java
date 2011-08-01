package com.bstek.dorado.view.resolver;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;

/**
 * 用于桥接dorado的资源装载器的Velocity资源装载器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 18, 2008
 */
public class VelocityViewTemplateResourceLoader extends ResourceLoader {
	private static final Log logger = LogFactory
			.getLog(VelocityViewTemplateResourceLoader.class);

	protected com.bstek.dorado.core.io.Resource getDoradoResource(String path) {
		Context context = Context.getCurrent();
		com.bstek.dorado.core.io.Resource resource = context.getResource(path);
		return resource;
	}

	@Override
	public void init(ExtendedProperties properties) {
		setCachingOn(Configure.getBoolean("view.templateCachingOn", true));
		setModificationCheckInterval(Configure.getLong(
				"view.templateModificationCheckInterval", 5));
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		com.bstek.dorado.core.io.Resource r = getDoradoResource(resource
				.getName());
		try {
			return (r.getTimestamp() != resource.getLastModified());
		}
		catch (IOException e) {
			logger.error(e, e);
			return false;
		}
	}

	@Override
	public long getLastModified(Resource resource) {
		com.bstek.dorado.core.io.Resource r = getDoradoResource(resource
				.getName());
		try {
			return r.getTimestamp();
		}
		catch (IOException e) {
			logger.error(e, e);
			return 0;
		}
	}

	@Override
	public InputStream getResourceStream(String path)
			throws ResourceNotFoundException {
		com.bstek.dorado.core.io.Resource resource = getDoradoResource(path);
		if (!resource.exists()) {
			throw new ResourceNotFoundException(path);
		}
		try {
			return resource.getInputStream();
		}
		catch (IOException e) {
			logger.error(e, e);
			return null;
		}
	}

}
