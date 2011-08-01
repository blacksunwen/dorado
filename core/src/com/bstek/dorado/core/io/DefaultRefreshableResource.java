package com.bstek.dorado.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.bstek.dorado.util.Assert;

/**
 * 默认的可重装载资源的实现类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 19, 2007
 */
public class DefaultRefreshableResource implements RefreshableResource {
	private static final int ONE_SECOND_MILLIS = 1000;
	private static final int DEFAULT_MIN_VALIDATE_SECONDS = 2;

	private Resource resource;
	private long minValidateSeconds = DEFAULT_MIN_VALIDATE_SECONDS;
	private long resourceTimpstamp;
	private long lastValidateMillis;

	/**
	 * @param resource 要包装的资源描述对象
	 */
	public DefaultRefreshableResource(Resource resource) {
		Assert.notNull(resource);
		this.resource = resource;
	}

	/**
	 * 执行真正的提取资源时间戳的操作。
	 */
	protected long getTimestampForValidate() {
		try {
			return resource.getTimestamp();
		}
		catch (IOException ex) {
			// do nothing
			return 0L;
		}
	}

	public long getMinValidateSeconds() {
		return minValidateSeconds;
	}

	public void setMinValidateSeconds(long minValidateSeconds) {
		this.minValidateSeconds = minValidateSeconds;
	}

	public boolean isValid() {
		long currentTimeMillis = System.currentTimeMillis();
		if ((currentTimeMillis - lastValidateMillis) > (minValidateSeconds * ONE_SECOND_MILLIS)) {
			lastValidateMillis = currentTimeMillis;
			return (resourceTimpstamp == getTimestampForValidate());
		}
		else {
			return true;
		}
	}

	public String getPath() {
		return resource.getPath();
	}

	public boolean exists() {
		return resource.exists();
	}

	public long getTimestamp() throws IOException {
		return getTimestampForValidate();
	}

	public InputStream getInputStream() throws IOException {
		return resource.getInputStream();
	}

	public URL getURL() throws IOException {
		return resource.getURL();
	}

	public File getFile() throws IOException {
		return resource.getFile();
	}

	public Resource createRelative(String relativePath) throws IOException {
		return resource.createRelative(relativePath);
	}

	public String getFilename() {
		return resource.getFilename();
	}

	public String getDescription() {
		return resource.getDescription();
	}

	@Override
	public String toString() {
		return resource.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj == this || (obj instanceof RefreshableResource && ((RefreshableResource) obj)
				.getDescription().equals(getDescription())));
	}

	@Override
	public int hashCode() {
		return resource.hashCode();
	}

}
