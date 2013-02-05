/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.loader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.bstek.dorado.web.ConsoleUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2013-1-22
 */
public class ConsoleStartedMessagesOutputter implements BeanFactoryAware {
	private BeanFactory beanFactory;
	private List<ConsoleStartedMessageOutputter> messageOutputters;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setMessageOutputters(
			List<ConsoleStartedMessageOutputter> messageOutputters) {
		this.messageOutputters = messageOutputters;
	}

	public void addMessageOutputter(
			ConsoleStartedMessageOutputter messageOutputter) {
		if (messageOutputters == null) {
			messageOutputters = new ArrayList<ConsoleStartedMessageOutputter>();
		}
		messageOutputters.add(messageOutputter);
	}

	public void output() throws Exception {
		Map<String, ConsoleStartedMessageOutputter> outputterMap = ((ListableBeanFactory) beanFactory)
				.getBeansOfType(ConsoleStartedMessageOutputter.class);

		boolean headerOutputted = false;
		Set<ConsoleStartedMessageOutputter> outputters = new TreeSet<ConsoleStartedMessageOutputter>(
				new Comparator<ConsoleStartedMessageOutputter>() {
					public int compare(ConsoleStartedMessageOutputter o1,
							ConsoleStartedMessageOutputter o2) {
						int gap = o1.getOrder() - o2.getOrder();
						if (gap != 0) {
							return gap;
						}
						return (o1 == o2) ? 0 : 1;
					}
				});

		outputters.addAll(outputterMap.values());
		if (messageOutputters != null) {
			outputters.addAll(messageOutputters);
		}

		for (ConsoleStartedMessageOutputter outputter : outputters) {
			StringWriter writer = new StringWriter();
			try {
				outputter.output(writer);
				String content = writer.toString();
				if (content.length() > 0) {
					if (!headerOutputted) {
						headerOutputted = true;
						ConsoleUtils.outputLoadingInfo();
						ConsoleUtils
								.outputLoadingInfo("========================");
						ConsoleUtils.outputLoadingInfo();
					} else {
						ConsoleUtils.outputLoadingInfo();
						ConsoleUtils
								.outputLoadingInfo("------------------------");
						ConsoleUtils.outputLoadingInfo();
					}

					StringReader sr = new StringReader(content);
					BufferedReader reader = new BufferedReader(sr);
					String line = reader.readLine();
					while (line != null) {
						ConsoleUtils.outputLoadingInfo(line);
						line = reader.readLine();
					}
				}
			} finally {
				writer.close();
			}
		}

		if (headerOutputted) {
			ConsoleUtils.outputLoadingInfo();
			ConsoleUtils.outputLoadingInfo("========================");
			ConsoleUtils.outputLoadingInfo();
		}
	}
}
