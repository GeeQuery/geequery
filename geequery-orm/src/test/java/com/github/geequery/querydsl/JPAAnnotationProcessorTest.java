package com.github.geequery.querydsl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Test;
import org.springframework.util.Assert;

import com.github.geequery.tools.StringUtils;
import com.querydsl.core.util.FileUtils;

public class JPAAnnotationProcessorTest {

	@Test
	public void testCreateQObjects() {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		Assert.notNull(compiler);

		Set<File> files = getFiles(new File(System.getProperty("user.dir"), "src/main/java"));
		if (files.isEmpty()) {
			return;
		}

		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(files);

		String compileClassPath = buildCompileClasspath();

		String processor = "com.querydsl.apt.jpa.JPAAnnotationProcessor";

		String outputDirectory = new File(System.getProperty("user.dir"), "target/generated-sources").getPath();
		File tempDirectory = null;

		List<String> compilerOptions = buildCompilerOptions(processor, compileClassPath, outputDirectory);

		Writer out = null;
		if (logOnlyOnError) {
			out = new StringWriter();
		}
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			CompilationTask task = compiler.getTask(out, fileManager, null, compilerOptions, null, compilationUnits1);
			Future<Boolean> future = executor.submit(task);
			Boolean rv = future.get();

			if (Boolean.FALSE.equals(rv) && logOnlyOnError) {
				getLog().error(out.toString());
			}
		} finally {
			executor.shutdown();
			if (tempDirectory != null) {
				FileSync.syncFiles(tempDirectory, getOutputDirectory());
				FileUtils.deleteDirectory(tempDirectory);
			}
		}

		buildContext.refresh(getOutputDirectory());
	}

	private Set<File> getFiles(File sourceDir) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> buildCompilerOptions(String processor, String compileClassPath, String outputDirectory) throws IOException {
		Map<String, String> compilerOpts = new LinkedHashMap<String, String>();

		// Default options
		compilerOpts.put("cp", compileClassPath);

//		if (sourceEncoding != null) {
//			compilerOpts.put("encoding", sourceEncoding);
//		}

		compilerOpts.put("proc:only", null);
		compilerOpts.put("processor", processor);

//		if (options != null) {
//			for (Map.Entry<String, String> entry : options.entrySet()) {
//				if (entry.getValue() != null) {
//					compilerOpts.put("A" + entry.getKey() + "=" + entry.getValue(), null);
//				} else {
//					compilerOpts.put("A" + entry.getKey() + "=", null);
//				}
//
//			}
//		}

		if (outputDirectory != null) {
			compilerOpts.put("s", outputDirectory);
		}


		StringBuilder builder = new StringBuilder();
		for (File file : getSourceDirectories()) {
			if (builder.length() > 0) {
				builder.append(";");
			}
			builder.append(file.getCanonicalPath());
		}
		compilerOpts.put("sourcepath", builder.toString());

		// User options override default options
		if (compilerOptions != null) {
			compilerOpts.putAll(compilerOptions);
		}

		List<String> opts = new ArrayList<String>(compilerOpts.size() * 2);

		for (Map.Entry<String, String> compilerOption : compilerOpts.entrySet()) {
			opts.add("-" + compilerOption.getKey());
			String value = compilerOption.getValue();
			if (StringUtils.isNotBlank(value)) {
				opts.add(value);
			}
		}
		return opts;
	}
}
