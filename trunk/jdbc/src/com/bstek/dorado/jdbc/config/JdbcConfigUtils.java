package com.bstek.dorado.jdbc.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;

public abstract class JdbcConfigUtils {

	private static Log logger = LogFactory.getLog(JdbcConfigUtils.class);
	
	private static final String XML_SUFFIX = ".xml";
	
	static DocumentWrapper[] getDocuments(Resource[] resources, XmlDocumentBuilder xmlDocumentBuilder) throws Exception {
		List<DocumentWrapper> documentList = new ArrayList<DocumentWrapper>();
		for (Resource resource : resources) {
			if (resource.exists()) {
				String filename = resource.getFilename().toLowerCase();
				if (filename.endsWith(XML_SUFFIX)) {
					Document document = xmlDocumentBuilder.loadDocument(resource);
					documentList.add(new DocumentWrapper(document, resource));
				} else {
					logger.warn("Unsupported jdbc configure - [" + resource + "]");
				}
			} else {
				logger.warn("File not exists - [" + resource + "]");
			}
		}
		return documentList.toArray(new DocumentWrapper[documentList.size()]);
	}
	
}
