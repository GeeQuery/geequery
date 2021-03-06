package com.github.geequery.tools.management;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.tools.reflect.ClassUtils;

/**
 * 信号量处理工具
 * @author Administrator
 *
 */
public class SignalUtils {
	private static TermHandler TERM=null;

	/**
	 * 获得终止信号量处理器
	 * @return
	 */
	public static synchronized TermHandler getTermHandler() {
		if(TERM==null){
			String clzName;
			if(ClassUtils.isPresent("sun.misc.Signal.Signal", null)){
				clzName="com.github.geequery.tools.management.SunJdkTERMHandler";
			}else{
				LogUtil.error("Can not operate signals: Unknown JDK"+System.getProperty("java.vm.vendor"));
				return null;
			}
			try {
				Class<?> clz=Class.forName(clzName);
				TERM=(TermHandler)clz.newInstance();
				TERM.activate();
			} catch (Exception e) {
				LogUtil.exception(e);
			}
		}
		return TERM;
	}
	
	
	
}
