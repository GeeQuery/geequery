package com.github.geequery.tools;

import java.io.IOException;

import org.junit.Test;

import com.github.geequery.accelerator.bean.BeanAccessor;
import com.github.geequery.accelerator.bean.FastBeanWrapperImpl;
import com.github.geequery.common.log.LogUtil;
import com.github.geequery.tools.reflect.BeanUtils;
import com.github.geequery.tools.reflect.BeanWrapper;
import com.github.geequery.tools.reflect.FieldEx;
import com.github.geequery.tools.reflect.MethodEx;
import com.github.geequery.tools.reflect.Property;

public class BeanWrapperTest {
	@Test
	public void test1() {
		BeanForTest t = new BeanForTest();
		BeanWrapper wr = BeanWrapper.wrap(t);
		System.out.println(wr.getPropertyNames());
		System.out.println(wr.isProperty("cSessionId"));
		System.out.println(wr.isProperty("tt1"));
		if(wr.isProperty("tt1")){
			LogUtil.show(wr.getPropertyRawType("tt1"));
			LogUtil.show(wr.getPropertyType("tt1"));	
		}
		
		for (String s : wr.getPropertyNames()) {
			Property p = wr.getProperty(s);
			System.out.println("=================");
			System.out.println(p.getName());
			System.out.println(p.getGenericType());
			System.out.println(p.getType());
		}

	}
	
	@Test
	public void testAsdsad() throws IOException, ClassNotFoundException{
		checkField(BeanForTest.class,"cSessionId");
		checkField(BeanForTest.class,"i");
		checkField(BeanForTest.class,"iB");
		checkField(BeanForTest.class,"iC");
		checkField(BeanForTest.class,"isBoolean");
		checkField(BeanForTest.class,"iSBoolean");
		
		
//	    String m = "2、95开头靓号，呼出显号，对方可以打回，即使不在线亦可设置呼转手机，不错过任何重要电话，不暴露真实身份。\r\n3、应用内完全免费发送文字消息、语音对讲。\r\n4、建议WIFI 或 3G 环境下使用以获得最佳通话体验";
//	    JSONObject json = new JSONObject();
//	    json.put("介绍", m);
//	    String content = json.toJSONString();
//	    System.out.println(content);
		
		
//		TypeUtils.compatibleWithJavaBean = true;
//		Person t = new Person();
//		t.setcSessionId(123);
//		System.out.println(JsonUtil.toJson(t));

	}

	private void checkField(Class<BeanForTest> class1, String field) {
		FieldEx ex=BeanUtils.getField(BeanForTest.class, field);
		MethodEx m1=BeanUtils.getGetter(ex);
		MethodEx m2=BeanUtils.getSetter(ex);
		Assert.notNull(m1);
		Assert.notNull(m2);
	}

	@Test
	public void test2() {
		BeanAccessor ba=FastBeanWrapperImpl.getAccessorFor(Foo.class);
		System.out.println(ba.getClass().getSuperclass());
		System.out.println(ba);
		System.out.println(ba.newInstance());
		System.out.println(ba.getType());
		
		BeanWrapper bw=BeanWrapper.wrap(new Foo());
		bw.setPropertyValue("name", null);
		System.out.println(bw.getPropertyValue("name"));
		
		bw=BeanWrapper.wrap(new BeanForTest());
		bw.setPropertyValue("id", null);
		System.out.println(bw.getPropertyValue("id"));
	}
	
	@Test
	public void sdfdsf(){
		System.out.println("ab1".hashCode());
		System.out.println("aaP".hashCode());
	}
	

}
