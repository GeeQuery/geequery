package com.github.geequery;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.tools.JefConfiguration.Item;
import com.github.geequery.tools.StringUtils;

public class CoreTest {
	
	@Test
	public void test1(){
		LogUtil.show(System.getProperties());
		System.out.println("-----------------------");
		LogUtil.show(System.getenv());
	}
	
	@Test
	@Ignore
	public void testx() throws MalformedURLException{
		URL u=new URL("jar:file:/C:/Users/jiyi/.m2/repository/com/belerweb/pinyin4j/2.5.0/pinyin4j-2.5.0.jar!/META-INF/MANIFEST.MF");
		URL u2=new URL("file:/C:/Users/jiyi/.m2/repository/com/belerweb/pinyin4j/2.5.0/pinyin4j-2.5.0.jar!/META-INF/MANIFEST.MF");
		System.out.println(u.getFile());
		System.out.println(u.getPath());
		System.out.println(u2.getFile());
		System.out.println(u2.getPath());
		}
	
	
	@Test
	public void test2() throws MalformedURLException{
		URL u=new URL("jar:file:/C:/Users/jiyi/.m2/repository/com/belerweb/pinyin4j/2.5.0/pinyin4j-2.5.0.jar!/META-INF/MANIFEST.MF");
		String crc=u.getPath()+StringUtils.getCRC(u.toString());
		System.out.println(crc);
		System.out.println();
	}
	
	/**
	 * 523742
641
642
641

2724547
1603
963
962


	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception{
		Class<Item> c=Item.class;
		{
			long start=System.nanoTime();
//			Item i=Enums.getIfPresent(c, "HTTP_TIMEOUT").orNull();
			Item i=com.github.geequery.tools.reflect.Enums.valueOf(c, "HTTP_TIMEOUT", null);
			long end=System.nanoTime();
			System.out.println(end-start);
		}
		{
			long start=System.nanoTime();
			Item i=com.github.geequery.tools.reflect.Enums.valueOf(c, "HTTP_TIMEOUT", null);
			long end=System.nanoTime();
			System.out.println(end-start);
		}
		{
			long start=System.nanoTime();
			Item i=com.github.geequery.tools.reflect.Enums.valueOf(c, "HTTP_TIMEOUT", null);
			long end=System.nanoTime();
			System.out.println(end-start);
		}
		{
			long start=System.nanoTime();
			Item i=com.github.geequery.tools.reflect.Enums.valueOf(c, "HTTP_TIMEOUT", null);
			long end=System.nanoTime();
			System.out.println(end-start);
		}
	}
	
	
	@Test
	public void test4(){
		System.out.println(StringUtils.matches("dsds1233","*123",false));
	}
}
