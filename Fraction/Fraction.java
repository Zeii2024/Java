
import java.util.Scanner;
// 封装一个类，其功能包括表示一个分数
// 自动约分为最简形式，能实现与其他分数的相加和相乘
// 能以分数和小数形式输出值

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
    
    // 构造函数，直接将传入的数约分为最简
    Fraction(int a, int b){

        // 求最大公约数
        int r = gcd(a, b);
        this.a = a / r;
        this.b = b / r;
        
    }

    // 求最大公约数
    public int gcd(int a, int b){
        int r = 1;
        while( b != 0){
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    // 转换成double
    public double toDouble(){
        return (double)this.a / (double)this.b;
    }

    // 与另一个分数l相加
    Fraction plus(Fraction l){

        int rp = gcd(this.b, l.b);
        int fm = this.b * l.b / rp;
        int fz1 = this.a * (fm/this.b);
        int fz2 = l.a * (fm/l.b);
        int fz = fz1 + fz2;

        return new Fraction(fz, fm);  // return Fraction的时候要先new
        
    }

    // 与另一个l相乘
    Fraction multiply(Fraction l){
        int fz = this.a * l.a;
        int fm = this.b * l.b;
        
        return new Fraction(fz, fm);  
    }

    void print(){
        if (b == 1){
            System.out.println(a);  // 分母为1时直接输出分子
        }
        else if(b > 0){
            System.out.println(a+"/"+b);  // 字符串要用双引号
        }
        else{
            System.out.println("Error");
        }
    }

}
