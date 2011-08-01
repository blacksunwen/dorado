package com.bstek.dorado.web.resolver;

import java.io.IOException;

import com.bstek.dorado.core.DoradoAbout;
import com.bstek.dorado.core.io.Resource;

public class ResourcesWrapper {
	private Resource[] resources;
	private ResourceType resourceType;
	private long lastModified;
	private long lastAccessed;
	private boolean reloadable;
	private int httpStatus;

	public ResourcesWrapper(Resource[] resources, ResourceType resourceType) {
		this.resources = resources;
		this.resourceType = resourceType;
		reloadable = true;
		updateLastModified();
		updateLastAccessed();
	}

	public ResourcesWrapper(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public Resource[] getResources() {
		return resources;
	}

	public long getLastModified() {
		return lastModified;
	}

	protected long updateLastModified() {
		long lastModified = 0;
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				long timestamp;
				try {
					timestamp = resource.getTimestamp();
				} catch (IOException ex) {
					timestamp = DoradoAbout.getInstantiationTime();
				}
				lastModified = (lastModified + timestamp) / (i + 1);
			}
		}
		this.lastModified = lastModified;
		return lastModified;
	}

	public long getLastAccessed() {
		return lastAccessed;
	}

	public long updateLastAccessed() {
		return lastAccessed = System.currentTimeMillis();
	}

	public boolean isReloadable() {
		return reloadable;
	}

	public void setReloadable(boolean reloadable) {
		this.reloadable = reloadable;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
