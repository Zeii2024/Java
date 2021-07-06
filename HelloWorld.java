import java.util.Scanner;

public class HelloWorld {
    public static void main(String[] args) {

        // ----------HelloWorld---------
        // System.out.println("Hello World!");
        // Scanner in = new Scanner(System.in);
        // System.out.println("echo: " + in.nextLine());

        // ----------加号用来连接----------
        /*
        System.out.println("2+3 = " + 2 + 3); // 直接将2和3看作字符串连接起来
        System.out.println("2+3 = " + (2 + 3)); //先算括号内的加法
        System.out.println(2 + 3 + "2+3 = 5"); //加法在前面则先算加法
        */

        // ----------用户输入数据作计算----------
        // System.out.println("100 - 23 = " + (100 - in.nextInt()));

        // ----------变量赋值----------
        // nextInt 以及其他的next都是读取“下一个”，即如果连续用两次，则分别读取两个Int
        // 所以在多次用到nextInt中的某个数时,要先赋值给变量
        /*
        int price;
        price = in.nextInt(); // 用了两次price，因此不能直接用nextInt
        System.out.println("100 - " + price + " = " + (100 - price));
        */

        // ----------转换长度单位，英尺——>米---------
        
        int foot;
        int inch;
        System.out.println("输入英尺和英寸：");
        Scanner in = new Scanner(System.in);
        foot = in.nextInt();
        inch = in.nextInt();
        System.out.println("身高是："+((foot + inch/12.0)*0.3048)+"米");
        // 类型转换关键字要加括号,如(int)(27.5)
        System.out.println("身高是："+((int)((foot + inch/12.0)*0.3048))+"厘米"); 
        

    }
}