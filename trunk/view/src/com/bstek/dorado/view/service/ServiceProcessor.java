package com.bstek.dorado.view.service;

import java.io.Writer;

import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.web.DoradoContext;

/**
 * 用于为客户端提供Ajax服务的处理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Nov 7, 2008
 */
public interface ServiceProcessor {
	/**
	 * 执行服务处理。
	 * 
	 * @param writer
	 *            面向客户端Response输出流的输出器。
	 * @param jsonNode
	 *            客户端提交的信息。
	 * @param context
	 *            Dorado上下文对象。
	 * @throws Exception
	 */
	void execute(Writer writer, ObjectNode objectNode, DoradoContext context)
			throws Exception;
}
