package com.bstek.dorado.sample;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.bstek.dorado.sample.dao.ExampleSourceDao;
import com.bstek.dorado.sample.entity.ExampleSource;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.resolver.AbstractWebFileResolver;
import com.bstek.dorado.web.resolver.HttpConstants;
import com.bstek.dorado.web.resolver.ResourceType;
import com.bstek.dorado.web.resolver.ResourcesWrapper;

@Component
public class DownloadSourceResolver extends AbstractWebFileResolver {
	@Resource
	private ExampleSourceDao exampleSourceDao;

	protected ResourcesWrapper createResourcesWrapper(
			HttpServletRequest request, DoradoContext context) throws Exception {
		String paramSourceId = request.getParameter("sourceId");
		if (StringUtils.isNotEmpty(paramSourceId)) {
			long sourceId = Long.parseLong(paramSourceId);
			ExampleSource exampleSource = exampleSourceDao.get(sourceId);
			if (exampleSource != null) {
				String path = exampleSource.getPath();
				if (StringUtils.isNotEmpty(path)) {
					com.bstek.dorado.core.io.Resource resource = context
							.getResource(path);
					String ext = StringUtils.substringAfterLast(path, ".");
					return new SourceResourcesWrapper(resource,
							getResourceTypeManager().getResourceType('.' + ext));
				}
			}
		}
		return null;
	}

	@Override
	protected OutputStream getOutputStream(HttpServletRequest request,
			HttpServletResponse response, ResourcesWrapper resourcesWrapper)
			throws IOException {
		response.addHeader(HttpConstants.CACHE_CONTROL, HttpConstants.NO_CACHE);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ ((SourceResourcesWrapper) resourcesWrapper).getFileName());

		return super.getOutputStream(request, response, resourcesWrapper);
	}
}

class SourceResourcesWrapper extends ResourcesWrapper {
	private String fileName;

	public SourceResourcesWrapper(com.bstek.dorado.core.io.Resource resource,
			ResourceType resourceType) {
		super(new com.bstek.dorado.core.io.Resource[] { resource },
				resourceType);
		fileName = resource.getFilename();
	}

	public String getFileName() {
		return fileName;
	}
}
