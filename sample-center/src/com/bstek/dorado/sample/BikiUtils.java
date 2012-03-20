/**
 * 
 */
package com.bstek.dorado.sample;

import java.util.Properties;

import org.bsdn.wiki.AbstractConfiguration;
import org.bsdn.wiki.Configurable;
import org.bsdn.wiki.LinkBuilder;
import org.bsdn.wiki.WikiParser;
import org.bsdn.wiki.exception.ParserException;
import org.bsdn.wiki.parser.ParserContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-19
 */
public class BikiUtils {
	private static Configurable configuration = new AbstractConfiguration() {
		private Properties props = new Properties();

		{
			props.setProperty(PROP_PARSER_TOC_DEPTH, "5");
			props.setProperty(PROP_PARSER_MAX_INFINITE_LOOP_LIMIT, "5");
			props.setProperty(PROP_PARSER_MAX_ITERATIONS, "100");
			props.setProperty(PROP_PARSER_TOC_NUMERICAL, "false");
		}

		public void setValue(String key, String value) {
			props.setProperty(key, value);
		}

		public String getValue(String key) {
			return props.getProperty(key);
		}
	};

	private static LinkBuilder linkBuilder = new LinkBuilder() {
		public String buildTopicUrl(String space, String topic, String anchor) {
			return topic;
		}

		public String buildAttachmentUrl(String space, String topic,
				String attachment) {
			return attachment;
		}

		public String buildImageUrl(String space, String topic, String image) {
			return image;
		}

		public String buildUserUrl(String user) {
			return user;
		}
	};

	public static ParserContext createParserContext() {
		ParserContext parserContext = new ParserContext("$description",
				"sample-center");
		parserContext.setConfig(configuration);
		parserContext.setLinkBuilder(linkBuilder);
		return parserContext;
	}

	public static String render(String markup) throws ParserException {
		return WikiParser.parse(createParserContext(), markup);
	}
}
