
import java.util.Scanner;
// ��װһ���࣬�书�ܰ�����ʾһ������
// �Զ�Լ��Ϊ�����ʽ����ʵ����������������Ӻ����
// ���Է�����С����ʽ���ֵ

public class Fraction{ 
	private int a;
	private int b;

    public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Fraction f1 = new Fraction(in.nextInt(), in.nextInt());
		Fraction f2 = new Fraction(in.nextInt(),in.nextInt());
		f1.print();
		f2.print();
		System.out.println(f1.toDouble());
		f1.plus(f2).print();
		f1.multiply(f2).plus(new Fraction(5,6)).print();
		f1.print();
		f2.print();
		in.close();
	} 
    
    // ���캯����ֱ�ӽ��������Լ��Ϊ���
    Fraction(int a, int b){

        // �����Լ��
        int r = gcd(a, b);
        this.a = a / r;
        this.b = b / r;
        
    }

    // �����Լ��
    public int gcd(int a, int b){
        int r = 1;
        while( b != 0){
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    // ת����double
    public double toDouble(){
        return (double)this.a / (double)this.b;
    }

    // ����һ������l���
    Fraction plus(Fraction l){

        int rp = gcd(this.b, l.b);
        int fm = this.b * l.b / rp;
        int fz1 = this.a * (fm/this.b);
        int fz2 = l.a * (fm/l.b);
        int fz = fz1 + fz2;

        return new Fraction(fz, fm);  // return Fraction��ʱ��Ҫ��new
        
    }

    // ����һ��l���
    Fraction multiply(Fraction l){
        int fz = this.a * l.a;
        int fm = this.b * l.b;
        
        return new Fraction(fz, fm);  
    }

    void print(){
        if (b == 1){
            System.out.println(a);  // ��ĸΪ1ʱֱ���������
        }
        else if(b > 0){
            System.out.println(a+"/"+b);  // �ַ���Ҫ��˫����
        }
        else{
            System.out.println("Error");
        }
    }

}
