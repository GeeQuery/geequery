import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testxxx {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new MyWork(), "A");
		System.out.println(t.getName());
		t.start();
		System.out.println(t.getName());
		t.join();
		System.out.println(t.getName());
	}

	@Test
	public void bdsf() {
		float a = 10.222222225f;
		float b = 10.222222229f;
		System.out.println(a);
		System.out.println(b);
		System.out.println(a==b);
		System.out.println(a<b);
		System.out.println(a>b);
		
		if(a-b < 0.00000001){
			System.out.println("a<b");
		}

	}
}

class MyWork implements Runnable {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void run() {
		try {
		} catch (Throwable t) {
			log.error("Thread {} error.", Thread.currentThread().getName(), t);
		}
	}
}
