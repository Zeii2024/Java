// �����������clock�������ڲ�ʵ����hour��minute
// hour��minute��ͨ����clock�����

public class Clock {
	
	Display minute = new Display(60);
	Display hour = new Display(24);
	
	void start() {       // Ҫ�����ܱ�֤ģ��Ķ����ԣ�����ֱ�ӽ���
		while(true) {    // ����ģ���ͨ�ţ��ڵ�����clock�����
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
