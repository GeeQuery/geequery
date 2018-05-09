package com.github.geequery.tools.maven;

import java.io.File;
import java.util.List;

import com.github.geequery.common.log.LogUtil;

import org.junit.Test;

/**
 * TODO pom.xml中存在特定环境依赖，故先ingore
 *
 */
public class TestMavenDependencyParser {

	@Test
	public void test() throws Exception {
		File pomFile=new File("pom.xml");
		MavenDependencyParser.debug=false;
		List<File> files=MavenDependencyParser.parseDependency(pomFile);
		LogUtil.show(files.size());
		LogUtil.show(files);
	}

}
