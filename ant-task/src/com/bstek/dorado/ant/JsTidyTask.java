package com.bstek.dorado.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;

public class JsTidyTask extends Task {
	private List<FileSet> fileSets = new ArrayList<FileSet>();

	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
	}

	@Override
	public void execute() throws BuildException {
		try {
			Context ctx = Context.enter();
			try {
				ctx.initStandardObjects();

				for (FileSet fs : fileSets) {
					DirectoryScanner ds = fs.getDirectoryScanner(getProject());
					File dir = ds.getBasedir();
					String[] srcs = ds.getIncludedFiles();
					for (String src : srcs) {
						File file = new File(dir, src);
						if (file.exists()) {
							formatFile(ctx, file);
						}
					}
				}
			}
			finally {
				Context.exit();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		}
	}

	public void formatFile(Context ctx, File file) throws Exception {
		System.out.println("Formatting " + file.getAbsolutePath() + "...");

		StringBuffer sb = new StringBuffer();
		InputStream in = new FileInputStream(file);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					sb.append(line).append('\n');
				}
			}
		}
		finally {
			in.close();
		}

		FileOutputStream out = new FileOutputStream(file);
		try {
			OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
			String code = sb.toString();

			Script script = ctx.compileString(code, file.getAbsolutePath(), 1,
					null);
			code = ctx.decompileScript(script, 0);

			writer.append(code);
			writer.flush();
			writer.close();
		}
		finally {
			out.close();
		}
	}
}
