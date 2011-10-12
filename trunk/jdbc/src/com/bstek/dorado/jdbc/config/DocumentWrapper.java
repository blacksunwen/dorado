package com.bstek.dorado.jdbc.config;

import org.w3c.dom.Document;

import com.bstek.dorado.core.io.Resource;

class DocumentWrapper {
	private Document document;
	private Resource resource;

	DocumentWrapper(Document document, Resource resource) {
		this.document = document;
		this.resource = resource;
	}

	Document getDocument() {
		return document;
	}

	Resource getResource() {
		return resource;
	}
}
