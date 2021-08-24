import java.util.Random;
import java.util.Scanner;

public class ChapterTwotoFour{
    public static void main(String[] args){
        // SellTaket();
        // Recycle();
        // Average();
        // GuessNumber();
        // ReverseNumber();
        // Fact();
        // Prime();
        // Prime100();
        // Label();
        EuclidGCD();
    }


    // --------------------售票机-------------------
    public static void SellTaket(){
        
        int cash;
        int change;
        int taket = 10;

        System.out.println("****************");
        System.out.println("* Java城际快车 *");
        System.out.println("*    256路     *");
        System.out.println("* 2020年2月9日 *");
        System.out.println("*   票价10元   *");
        System.out.println("****************");

        System.out.println("请投币: ");
        Scanner in = new Scanner(System.in);

        cash = in.nextInt();
        if(cash < taket){
            System.out.println("钱不够！穷鬼");
        }
        else{
            change = cash - taket;
            System.out.println("找零：" + change);
        }
    }

    // --------------循环-输出整数的位数-------------
    public static void Recycle(){
        int number;
        int count = 0;

        System.out.println("输入number: ");
        Scanner in = new Scanner(System.in);
        number = in.nextInt();

        while(number >= 0){
            number = number/10;
            count += 1;
        }
        System.out.println("位数为: " + count);
    }

    // ---------------计算平均数---------------------
    public static void Average(){
        int number;
        int score = 0;
        float ave = 0;
        int count = 0;

        System.out.println("输入一系列数(-1结束)：");
        Scanner in = new Scanner(System.in);
        number = in.nextInt();

        while(number != -1){
            score = score + number;
            number = in.nextInt();
            count += 1;
        }

        ave = score / count;
        System.out.println("平均分 = " + ave);
    }

    // -----------------猜数小游戏-------------------
    public static void GuessNumber(){
        // 两种随机数生成方式：
        // 1.Math.random(); 生成[0.0, 1.0)的随机数;
        // 2.Random r = new Random(seed); new一个随机数生成器
        //   num = r.nextInt(10);  用num取随机数，10是随机数上限，即[0,10)
        int number;
        int guess;
        
        Random r = new Random();  // Random(seed)种子随机
        number = r.nextInt(100);  // 100是随机数的上限[0, 100)

        System.out.println("猜一个数字(1-100): ");
        Scanner in = new Scanner(System.in);
        guess = in.nextInt();

        while(guess != number){
            if(guess > number){
                System.out.println("太大了");
            }
            else{
                System.out.println("太小了");  
            }
            System.out.println("再猜：");
            guess = in.nextInt();
        }
        if(guess == number){
            System.out.println("猜对了！");
        }
    }

    // -----------------整数拆解---------------------
    public static void ReverseNumber(){
        int number;
        int num;
        int count = 0;
        int result = 0;

        System.out.println("输入一个多位数：");
        Scanner in = new Scanner(System.in);
        number = in.nextInt();

        while(number >= 0){
            num = number % 10;
            number = number / 10;
            result = result * 10 + num;
            count += 1;
        }
        System.out.println(result);
        System.out.println("count = "+count);
    }

    // ---------------for循环求阶乘------------------
    public static void Fact(){
        int n;
        int fact = 1;

        System.out.println("输入n(大于等于0):");
        Scanner in = new Scanner(System.in);
        n = in.nextInt();

        for(int i=1; i<=n; i++){
            fact = fact * i;
        }
        System.out.println(n + "的阶乘 = " + fact);
    }

    // ------------------prime----------------------
    public static int Prime(int n){
        int flag = 1;
        
        // System.out.println("输入n: ");
        // Scanner in = new Scanner(System.in);
        // n = in.nextInt();

        for (int i=2; i<=n/2; i++){
            if(n % i == 0){
                flag = 0;
                break;
            }
        }
        return flag;
        /*
        if(flag == 1){
            // System.out.println(n+"是素数");
        }
        else
            System.out.println(n+"不是素数");
        */
    }

    // ----------------prime 100以内----------------
    public static void Prime100(){
        int n = 2;
        int flag = 0;

        for(n=2; n<=100; n++){
            flag = Prime(n);
            if(flag == 1){
                System.out.print(n+" ");
            }
        }
    }

    // ----------------标号label--------------------
    public static void Label(){
        // 多重f循环时，满足条件时跳出指定的for循环
        // 给需要指定的for循环起个label名字，break时加上名字;
        // 适用于各种循环，break 和 continue都可以
        System.out.println("输入n: ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        OUT:   // 标号名字可自行起
        for(int one=0; one <= n; ++one){
            for(int five=0; five <= n/5; ++five){
                for(int ten=0; ten <= n/10; ++ten){
                    if(one + five*5 + ten*10 == n){
                        System.out.println("1毛"+one+"张，"+"5毛"+five+"张，"+"1块"+ten+"张。");
                        break OUT;
                    }
                }
            }
        }
    }

    // ------------欧几里得法求最大公约数---------------
    public static void EuclidGCD(){
        int a;
        int b;

        System.out.println("输入a和b: ");
        Scanner in = new Scanner(System.in);
        a = in.nextInt();
        b = in.nextInt();
        int oa = a;
        int ob = b;
        int r = 0;  // 余数

        while(b != 0){
            r = a % b;
            a = b;
            b = r;
        }
        System.out.println(oa+"和"+ob+"的最大公约数= "+a);
    }
    
}