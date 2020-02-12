// 作一个时钟，实现分钟和小时的递增运转
// 构造类display，包括值递增和值返回的功能
// 将其分别实例化为小时和分钟两个对象

public class Display {
	int value = 0;
	int limit = 60;
	
	Display(int l){
		
		this.limit = l;
	}
	
	void increase() {
		value ++;
		if(value == limit) {
			value = 0;
		}
	}
	
	int getValue() {
		return value;
	}
	
	public static void main(String[] args) {
		Display min = new Display(60);
		
		while(true) {
			min.increase();
			System.out.println(min.getValue());
		}

	}
	
	

}
