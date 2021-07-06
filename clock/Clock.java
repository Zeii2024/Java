// 构造第三个类clock，在其内部实例化hour和minute
// hour与minute的通信由clock来完成

public class Clock {
	
	Display minute = new Display(60);
	Display hour = new Display(24);
	
	void start() {       // 要尽可能保证模块的独立性，避免直接交互
		while(true) {    // 两个模块的通信，在第三方clock中完成
			minute.increase();
			if(minute.getValue() == 0) {
				hour.increase();
			}
			System.out.printf("%02d : %02d\n", hour.getValue(),minute.getValue());
			
		}
	}
	
	public static void main(String[] args) {
		Clock clock = new Clock();
		clock.start();
	}
}
