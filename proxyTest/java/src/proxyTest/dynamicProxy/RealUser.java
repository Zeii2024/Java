package proxyTest.dynamicProxy;


// 目标类
public class RealUser implements IUser {
    @Override
    public int getUserId() {
        System.out.println("getUserId...");
        return 2562;
    }
}
