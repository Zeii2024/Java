// ��һ��ʱ�ӣ�ʵ�ַ��Ӻ�Сʱ�ĵ�����ת
// ������display������ֵ������ֵ���صĹ���
// ����ֱ�ʵ����ΪСʱ�ͷ�����������

public class Display {
	private int value = 0;
	private int limit = 60;
	
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
