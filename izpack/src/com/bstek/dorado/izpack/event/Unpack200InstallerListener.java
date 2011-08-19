/**
 * 
 */
package com.bstek.dorado.izpack.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;

import com.izforge.izpack.PackFile;
import com.izforge.izpack.event.SimpleInstallerListener;

/**
 * @author mailto:benny.bao@bstek.com
 * @since 2011-3-23
 */
@SuppressWarnings("rawtypes")
public class Unpack200InstallerListener extends SimpleInstallerListener {
	private static final String DEFAULT_PACK200_EXT = ".pack.gz";
	private static final String ECLIPSE_JAR_PROCESSOR = "lib/org.eclipse.equinox.p2.jarprocessor_1.0.200.v20100503a.jar";

	private static Class jarProcessorExecutorClass;
	private static Class jarProcessorExecutorOptionsClass;
	private static Method jarProcessorExecutorRunMethod;

	private Unpacker unpacker = Pack200.newUnpacker();
	private Object jarProcessorExecutor;

	private Object getJarProcessorExecutor() throws Exception {
		if (jarProcessorExecutor == null) {
			if (jarProcessorExecutorClass == null) {
				InputStream in = getClass().getClassLoader()
						.getResourceAsStream(ECLIPSE_JAR_PROCESSOR);

				File jarFile = File.createTempFile("eclipse.jarprocessor.",
						".jar");
				// File jarFile = new File("d:/temp/eclipse.jarprocessor.jar");
				jarFile.createNewFile();
				jarFile.deleteOnExit();

				FileOutputStream fos = new FileOutputStream(jarFile);
				try {
					copyStream(in, fos);
					fos.flush();
				} finally {
					fos.close();
					in.close();
				}

				URL url = new URL("file:" + jarFile.getAbsolutePath());
				URLClassLoader urlClassLoader = new URLClassLoader(
						new URL[] { url }, getClass().getClassLoader());

				jarProcessorExecutorClass = urlClassLoader
						.loadClass("org.eclipse.internal.provisional.equinox.p2.jarprocessor.JarProcessorExecutor");
				jarProcessorExecutorOptionsClass = urlClassLoader
						.loadClass("org.eclipse.internal.provisional.equinox.p2.jarprocessor.JarProcessorExecutor$Options");

				jarProcessorExecutorRunMethod = jarProcessorExecutorClass
						.getMethod(
								"runJarProcessor",
								new Class[] { jarProcessorExecutorOptionsClass });
			}
			jarProcessorExecutor = jarProcessorExecutorClass.newInstance();
		}
		return jarProcessorExecutor;
	}

	private long copyStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[5120];
		long bytesCopied = 0;
		int bytesInBuffer;
		while ((bytesInBuffer = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesInBuffer);
			bytesCopied += bytesInBuffer;
		}
		return bytesCopied;
	}

	@Override
	public boolean isFileListener() {
		return true;
	}

	@Override
	public void afterFile(File file, PackFile packFile) throws Exception {
		if (!packFile.isPack200Jar()) {
			return;
		}

		File targetFile = file;
		File tmpFile = new File(file.getAbsoluteFile() + DEFAULT_PACK200_EXT);
		file.renameTo(tmpFile);

		if (packFile.isEclipsePack200Jar()) {
			System.out.println("Unpacking eclipse signed jar "
					+ targetFile.getAbsolutePath() + "...");

			Object jarProcessorExecutor = getJarProcessorExecutor();
			Object options = jarProcessorExecutorOptionsClass.newInstance();
			jarProcessorExecutorOptionsClass.getField("unpack").set(options,
					true);
			jarProcessorExecutorOptionsClass.getField("input").set(options,
					tmpFile);
			jarProcessorExecutorOptionsClass.getField("outputDir").set(options,
					targetFile.getParentFile().getAbsolutePath());
			jarProcessorExecutorRunMethod.invoke(jarProcessorExecutor,
					new Object[] { options });
		} else {
			System.out.println("Unpacking " + targetFile.getAbsolutePath()
					+ "...");
			FileOutputStream fos = new FileOutputStream(targetFile);
			JarOutputStream jos = new JarOutputStream(fos);
			try {
				unpacker.unpack(tmpFile, jos);
			} finally {
				jos.close();
			}
		}
		tmpFile.delete();
	}
}
