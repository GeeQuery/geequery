package com.github.geequery.concurrent;

import java.util.Date;

import org.junit.Test;

import com.github.geequery.concurrent.timer.Job;
import com.github.geequery.tools.ThreadUtils;

public class JobTest {
	
	@Test
	public void test1() throws Exception{
		MyJob job=new MyJob();
		job.startTimer();
		ThreadUtils.doSleep(2000);
		job.stopTimer();
	}
	
	
	static class MyJob extends Job{
		private int times=0;
		@Override
		protected long getDelay() {
			return 0;
		}

		@Override
		protected long getPeriod() {
			return 500;
		}

		@Override
		protected void execute() {
			times++;
			Date date=new Date();
			System.out.println("第"+times+"次执行，本次开始:" + date);
			System.out.println("上次执行时间段：" + super.getLastFire());
		}
		
	}
}
