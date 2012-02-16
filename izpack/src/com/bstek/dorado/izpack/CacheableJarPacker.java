/**
 * 
 */
package com.bstek.dorado.izpack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Pack200;

import org.eclipse.internal.provisional.equinox.p2.jarprocessor.JarProcessorExecutor;

import com.izforge.izpack.compiler.PackagerHelper;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-24
 */
public class CacheableJarPacker {
	private static final String CACHE_EXT = ".pack.gz";
	private static final String DEFAULT_PACK200_EXT = ".pack.gz";
	private static final long MAX_FILE_SIZE = 3 * 1024 * 1024;

	private JarProcessorExecutor jarProcessorExecutor;
	private String sourceRootPath;
	private String libRootPath;
	private String cacheRootPath;
	private Pack200.Packer packer;
	private Map<String, Integer> jarTypeCache = new HashMap<String, Integer>();

	public String getSourceRootPath() {
		return sourceRootPath;
	}

	public void setSourceRootPath(String sourceRootPath) throws IOException {
		this.sourceRootPath = sourceRootPath;
		libRootPath = (new File(sourceRootPath, "lib")).getCanonicalPath();
	}

	public String getCacheRootPath() {
		return cacheRootPath;
	}

	public void setCacheRootPath(String cacheRootPath) {
		this.cacheRootPath = cacheRootPath;
	}

	private boolean isSignedJar(File file) throws IOException {
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().startsWith("META-INF")
					&& entry.getName().endsWith(".SF")) {
				jar.close();
				return true;
			}
		}
		jar.close();
		return false;
	}

	private boolean isEclipseSignedJar(File file) throws IOException {
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().startsWith("META-INF")
					&& entry.getName().endsWith("ECLIPSEF.SF")) {
				jar.close();
				return true;
			}
		}
		jar.close();
		return false;
	}

	public int getJarType(File file) throws IOException {
		String cacheKey = file.getCanonicalPath();
		Integer jarType = jarTypeCache.get(cacheKey);
		if (jarType == null) {
			/*
			 * type = 1 普通jar，可以执行pack200 type = 2 eclipse中特殊的signed
			 * jar，需使用特殊的pack200 type = -1 不支持pack200
			 */
			String name = file.getName();
			String path = file.getCanonicalPath();
			int type = 1;

			if (isEclipseSignedJar(file)) {
				if (org.eclipse.equinox.internal.p2.jarprocessor.Utils
						.shouldSkipJar(file, false, true) || file.length() > MAX_FILE_SIZE) {
					type = -1;
				} else {
					type = 2;
				}
			} else {
				if (isSignedJar(file) 
						|| path.startsWith(libRootPath)
						|| name.contains(".doc.")
						|| name.contains(".doc_")
						|| name.contains("-doc.")
						|| name.contains("-doc_")
						|| name.contains("dorado.")
						|| name.contains(".bstek.")
						|| name.contains(".source.")
						|| name.contains(".source_")
						|| name.contains("-source.")
						|| name.contains("-source_")
						|| name.contains(".help.")
						|| name.contains(".help_")
						|| name.contains("-help.")
						|| name.contains("-help_")
						|| file.length() > MAX_FILE_SIZE) {
					type = -1;
				}
			}
			jarTypeCache.put(cacheKey, Integer.valueOf(type));
			return type;
		} else {
			return jarType.intValue();
		}
	}

	public void pack(File file, OutputStream out) throws Exception {
		int jarType = getJarType(file);
		assert (jarType > 0);

		String absolutePath = file.getCanonicalPath();
		if (absolutePath.startsWith(sourceRootPath)) {
			String cachePath = cacheRootPath
					+ absolutePath.substring(sourceRootPath.length())
					+ CACHE_EXT;

			File cacheFile = new File(cachePath);
			if (!cacheFile.exists()) {
				File cacheParentDir = cacheFile.getParentFile();
				if (!cacheParentDir.exists()) {
					cacheParentDir.mkdirs();
				}
				cacheFile.createNewFile();
				buildPackedCacheFile(jarType, file, cacheFile);
			} else if (cacheFile.lastModified() != file.lastModified()) {
				buildPackedCacheFile(jarType, file, cacheFile);
			}

			InputStream in = new FileInputStream(cacheFile);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			try {
				PackagerHelper.copyStream(in, bos);
				bos.flush();
			} finally {
				in.close();
			}
		} else {
			doPack(file, out);
		}
	}

	private void buildPackedCacheFile(int jarType, File file, File cacheFile)
			throws FileNotFoundException, IOException {
		if (jarType == 2) {
			doPackByJarProcessor(file, cacheFile);
		} else {
			FileOutputStream fos = new FileOutputStream(cacheFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			try {
				doPack(file, bos);
				bos.flush();
			} finally {
				fos.close();
			}
		}
		cacheFile.setLastModified(file.lastModified());
	}

	protected Pack200.Packer getPack200Packer() {
		if (packer == null) {
			packer = Pack200.newPacker();
			Map<String, String> m = packer.properties();
			m.put(Pack200.Packer.EFFORT, "9");
			m.put(Pack200.Packer.SEGMENT_LIMIT, "-1");
			m.put(Pack200.Packer.KEEP_FILE_ORDER, Pack200.Packer.FALSE);
			m.put(Pack200.Packer.DEFLATE_HINT, Pack200.Packer.FALSE);
			m.put(Pack200.Packer.MODIFICATION_TIME, Pack200.Packer.LATEST);
			m.put(Pack200.Packer.CODE_ATTRIBUTE_PFX + "LineNumberTable",
					Pack200.Packer.STRIP);
			m.put(Pack200.Packer.CODE_ATTRIBUTE_PFX + "LocalVariableTable",
					Pack200.Packer.STRIP);
			m.put(Pack200.Packer.CODE_ATTRIBUTE_PFX + "SourceFile",
					Pack200.Packer.STRIP);
		}
		return packer;
	}

	protected void doPack(File file, OutputStream out) throws IOException {
		System.out.println("Packing jar " + file.getAbsolutePath() + "...");
		JarFile jar = new JarFile(file);
		try {
			getPack200Packer().pack(jar, out);
		} finally {
			jar.close();
		}
	}

	private JarProcessorExecutor getJarProcessorExecutor() {
		if (jarProcessorExecutor == null) {
			jarProcessorExecutor = new JarProcessorExecutor();
		}
		return jarProcessorExecutor;
	}

	protected void doPackByJarProcessor(File file, File outputFile)
			throws IOException {
		System.out.println("Processing jar " + file.getAbsolutePath() + "...");

		File outputDir = outputFile.getParentFile();

		JarProcessorExecutor.Options options = new JarProcessorExecutor.Options();
		options.pack = true;
		options.input = file;
		options.outputDir = outputDir.getAbsolutePath();
		getJarProcessorExecutor().runJarProcessor(options);

		File defaultOuptutFile = new File(options.outputDir, file.getName()
				+ DEFAULT_PACK200_EXT);

		if (!defaultOuptutFile.equals(outputFile)) {
			defaultOuptutFile.renameTo(outputFile);
		}
	}
}
