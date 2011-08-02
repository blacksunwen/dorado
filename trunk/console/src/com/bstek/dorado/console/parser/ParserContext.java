package com.bstek.dorado.console.parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.config.xml.CollectionToPropertyParser;
import com.bstek.dorado.config.xml.DispatchableXmlParser;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;

class ParserContext {
	private Context doradoContext;
	private Set<Parser> doneParsers;

	private static Log logger = LogFactory.getLog(ParserContext.class);

	ParserContext() {
		doradoContext = Context.getCurrent();
		doneParsers = new HashSet<Parser>();
	}

	ParserContext createSubContext() {
		ParserContext subCtx = new ParserContext();
		subCtx.doneParsers.addAll(this.doneParsers);
		return subCtx;
	}

	Context getDoradoContext() {
		return doradoContext;
	}

	void properties(Node node) throws Exception {
		if (node.parser == null)
			return;

		Parser parser = node.parser;
		this.init(parser);

		if (parser instanceof DispatchableXmlParser) {
			Map<String, XmlParser> parsers = ((DispatchableXmlParser) parser)
					.getPropertyParsers();
			if (parsers.isEmpty())
				return;

			Iterator<Entry<String, XmlParser>> entryItr = parsers.entrySet()
					.iterator();
			while (entryItr.hasNext()) {
				Entry<String, XmlParser> entry = entryItr.next();
				String key = entry.getKey();
				XmlParser p = entry.getValue();
				node.addProperty(key, p.getClass().getName());
			}
		}
	}

	void children(Node parentNode) throws Exception {
		if (parentNode.parser == null)
			return;

		Parser parentParser = parentNode.parser;
		this.init(parentParser);

		if (parentParser instanceof DispatchableXmlParser) {
			Map<String, XmlParser> parsers = ((DispatchableXmlParser) parentParser)
					.getSubParsers();
			if (parsers.isEmpty())
				return;

			Iterator<Entry<String, XmlParser>> entryItr = parsers.entrySet()
					.iterator();
			while (entryItr.hasNext()) {
				Entry<String, XmlParser> entry = entryItr.next();
				String name = entry.getKey();
				XmlParser parser = entry.getValue();

				Node node = new Node(parser, name);
				parentNode.getChildren().add(node);
				properties(node);

				if (this.accept(parser)) {
					this.addDoneParser(name, parser);
					this.createSubContext().children(node);
				}
			}
		}
	}

	private void init(Parser parser) {
		try {
			if (parser instanceof ObjectParser) {
				((ObjectParser) parser).init();
			}
			if (parser instanceof CollectionToPropertyParser) {
				((CollectionToPropertyParser) parser).init();
			}
		} catch (Exception e) {
			logger.error("Parser init error.", e);
		}
	}

	void resetDoneParsers() {
		doneParsers.clear();
	}

	boolean accept(Parser parser) {
		if (doneParsers.contains(parser)) {
			return false;
		}
		return true;
	}

	void addDoneParser(String tag, Parser parser) {
		doneParsers.add(parser);
	}
}
