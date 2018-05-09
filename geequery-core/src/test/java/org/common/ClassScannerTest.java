package org.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.tools.ClassScanner;
import com.github.geequery.tools.resource.IResource;

public class ClassScannerTest {
	@Test
	public void testMyListClass() {
		IResource[] names=ClassScanner.listClassNameInPackage(null, new String[] { "org.slf4j" }, false);
//		LogUtil.show(names);
		System.out.println(names.length);
		Assert.assertTrue(names.length>0);
	}	
		
	@Test
	@Ignore
	public void testMyListClass2() throws MalformedURLException {
		IResource[] names=ClassScanner.listClassNameInPackage(new URL("file://d:/asia/target/perftest.jar"), new String[] {"com.ailk.easyframe.sdl.service"},  false);
		LogUtil.show(names);
		System.out.println(names.length);
		Assert.assertTrue(names.length>0);
	}
	
	@Test
	@Ignore
	public void testMyListClass3() {
		IResource[] names=ClassScanner.listClassNameInPackage(null, new String[] { "com.ailk.easyframe.sdl.service"}, false);
		LogUtil.show(names);
		System.out.println(names.length);
		Assert.assertTrue(names.length>0);
	}
}
