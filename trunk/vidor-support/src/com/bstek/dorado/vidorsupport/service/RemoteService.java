package com.bstek.dorado.vidorsupport.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.vidorsupport.iapi.IDataProviderWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IDataResolverWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IDataTypeWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IExposedServiceWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IJavaClassWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IViewReader;
import com.bstek.dorado.vidorsupport.iapi.IViewWriter;

public class RemoteService extends InitSupport {
	private IViewReader viewReader;
	private IViewWriter viewWriter;
	private IDataProviderWorkshop dataProviderWorkshop;
	private IDataResolverWorkshop dataResolverWorkshop;
	private IExposedServiceWorkshop exposedServiceWorkshop;
	private IJavaClassWorkshop javaClassWorkshop;

	public void setViewReader(IViewReader viewReader) {
		this.viewReader = viewReader;
	}

	public void setViewWriter(IViewWriter viewWriter) {
		this.viewWriter = viewWriter;
	}

	public void setDataProviderWorkshop(
			IDataProviderWorkshop dataProviderWorkshop) {
		this.dataProviderWorkshop = dataProviderWorkshop;
	}

	public void setDataResolverWorkshop(
			IDataResolverWorkshop dataResolverWorkshop) {
		this.dataResolverWorkshop = dataResolverWorkshop;
	}

	public void setDataTypeWorkshop(IDataTypeWorkshop dataTypeWorkshop) {
		this.dataTypeWorkshop = dataTypeWorkshop;
	}

	public void setExposedServiceWorkshop(
			IExposedServiceWorkshop exposedServiceWorkshop) {
		this.exposedServiceWorkshop = exposedServiceWorkshop;
	}

	public void setJavaClassWorkshop(IJavaClassWorkshop javaClassWorkshop) {
		this.javaClassWorkshop = javaClassWorkshop;
	}

	@Expose
	public void saveView(String json, String url) throws Exception {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(url));
			this.viewWriter.write(json, outputStream,
					Configure.getString(InitSupport.ENCODING));
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Expose
	public Collection<String> getModelNames() throws Exception {
		Map<String, Object> parameter = null;
		return this.dataTypeWorkshop.names(parameter, false);
	}

	@Expose
	public Collection<String> getBaseDataTypeNames() throws Exception {
		return this.dataTypeWorkshop.baseNames();
	}

	@Expose
	public Collection<String> getDataResolverNames() throws Exception {
		Map<String, Object> parameter = null;
		return this.dataResolverWorkshop.names(parameter);
	}

	@Expose
	public Collection<String> getDataProviderNames() throws Exception {
		Map<String, Object> parameter = null;
		return this.dataProviderWorkshop.names(parameter);
	}

	@Expose
	public Collection<String> getExposeNames() throws Exception {
		Map<String, Object> parameter = null;
		return this.exposedServiceWorkshop.names(parameter);
	}

	@Expose
	public Collection<String> getJavaClassNames(Map<String, Object> parameter)
			throws Exception {
		if (parameter == null)
			parameter = new HashMap<String, Object>();
		return this.javaClassWorkshop.names(parameter);
	}

	@Expose
	public String dataTypeReflection(String json) throws Exception {
		return this.dataTypeWorkshop.reflection(json);
	}

	@Expose
	public String dataTypeJSON(String name) throws Exception {
		return this.dataTypeWorkshop.json(name);
	}

	protected String parseFileName(String path) throws Exception {
		String[] paths = StringUtils.replace(path, "\\", "/").split("/");
		return paths[paths.length - 1];
	}

	@Expose
	public String dataTypeJSON(Map<String, String> jsonMap, String name)
			throws Exception {
		return this.dataTypeWorkshop.json(jsonMap, name);
	}
	@Expose
	public String jsonToXml(String json) throws Exception {
		String xml = this.viewWriter.toXML(json);
		return xml;
	}

	@Expose
	public Map<String, Object> initView(String path) throws Exception {
		String viewJson;
		FileInputStream inputStream = null;

		try {
			inputStream = new FileInputStream(new File(path));
			viewJson = viewReader.read(inputStream,
					Configure.getString(InitSupport.ENCODING));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		String viewName = this.parseFileName(path);
		return this.buildInitData(viewName, viewJson, path);
	}
}
