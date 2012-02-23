/**
 * 
 */
package com.bstek.dorado.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-23
 */
public final class TempFileUtils {

	private static class FileHandler {
		private File file;
		private FileChannel fileChannel;
		private FileLock fileLock;

		public FileHandler(File file, FileChannel fileChannel, FileLock fileLock) {
			this.file = file;
			this.fileChannel = fileChannel;
			this.fileLock = fileLock;
		}

		public File getFile() {
			return file;
		}

		public FileChannel getFileChannel() {
			return fileChannel;
		}

		public FileLock getFileLock() {
			return fileLock;
		}
	}

	private static Map<String, FileHandler> fileHandleMap = new Hashtable<String, FileHandler>();

	private TempFileUtils() {
	}

	public static File createTempFile(String id, String fileNamePrefix,
			String fileNamesuffix) throws IOException {
		Assert.notEmpty(id);

//		deleteTempFile(id);

		File file = File.createTempFile(fileNamePrefix, fileNamesuffix);
		file.deleteOnExit();

//		FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
//		FileLock lock = channel.tryLock();
//		if (lock == null) {
//			throw new IOException("Lock file \"" + file.getCanonicalPath()
//					+ "\" failed.");
//		}
//
//		FileHandler handler = new FileHandler(file, channel, lock);
//		fileHandleMap.put(id, handler);
		return file;
	}

	public static boolean deleteTempFile(String id) throws IOException {
		Assert.notEmpty(id);

		boolean deleted = false;
		FileHandler handler = fileHandleMap.get(id);
		if (handler != null) {
			handler.getFileLock().release();
			handler.getFileChannel().close();
			deleted = handler.getFile().delete();
		}
		return deleted;
	}
}
