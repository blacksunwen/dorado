package com.bstek.dorado.vidorsupport.resolver;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.dorado.vidorsupport.iapi.IDataTypeWorkshop;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

public class BaseDataTypesJsonResolver extends AbstractTextualResolver {
	private Collection<String> baseDataTypes;
	protected IDataTypeWorkshop dataTypeWorkshop;

	public BaseDataTypesJsonResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_JAVASCRIPT);
		setCacheControl(HttpConstants.NO_CACHE);
	}

	public void setDataTypeWorkshop(IDataTypeWorkshop dataTypeWorkshop) {
		this.dataTypeWorkshop = dataTypeWorkshop;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter writer = getWriter(request, response);
		if (baseDataTypes == null) {
			baseDataTypes = dataTypeWorkshop.baseNames();
		}
		try {
			JsonBuilder jsonBuilder = new JsonBuilder(writer);
			jsonBuilder.escapeableArray();
			Iterator<String> iterator = baseDataTypes.iterator();
			while (iterator.hasNext()) {
				String type = iterator.next();
				jsonBuilder.value(type);

			}
			jsonBuilder.endArray();

		} finally {
			writer.flush();
			writer.close();
		}
	}
}
