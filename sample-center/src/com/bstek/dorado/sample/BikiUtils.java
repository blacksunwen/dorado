/**
 * 
 */
package com.bstek.dorado.sample;

import java.util.Properties;

import org.bsdn.biki.AbstractConfiguration;
import org.bsdn.biki.BikiParser;
import org.bsdn.biki.Configurable;
import org.bsdn.biki.LinkBuilder;
import org.bsdn.biki.exception.ParserException;
import org.bsdn.biki.parser.ParserContext;

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
		public String buildTopicLink(String space, String topic, String anchor) {
			return topic;
		}

		public String buildAttachmentLink(String space, String topic,
				String attachment) {
			return attachment;
		}

		public String buildImageLink(String space, String topic, String image) {
			return image;
		}

		public String buildUserLink(String user) {
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
		return BikiParser.parse(createParserContext(), markup);
	}
}
