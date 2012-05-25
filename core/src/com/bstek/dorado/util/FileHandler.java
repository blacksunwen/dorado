package com.bstek.dorado.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileHandler {
	private File file;
	private OutputStream outputStream;
	private Writer writer;

	public FileHandler(File file) throws FileNotFoundException {
		this.file = file;
		outputStream = new FileOutputStream(file);
		writer = new OutputStreamWriter(outputStream);
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public Writer getWriter() {
		return writer;
	}

	public InputStream createInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();
			writer = null;
		}
		if (outputStream != null) {
			outputStream.flush();
			outputStream.close();
			outputStream = null;
		}
	}

	public void delete() throws IOException {
		close();
		if (!file.delete()) {
			throw new IOException("Can not delete file \""
					+ file.getCanonicalPath() + "\".");
		}
	}

	public String getPath() {
		return file.getAbsolutePath();
	}
}