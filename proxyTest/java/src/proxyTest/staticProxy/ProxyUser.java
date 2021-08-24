package proxyTest.staticProxy;

public class ProxyUser implements IUser{  // 实现接口
    // 维护一个目标类
    private IUser target;
    // 构造函数中传入 target
    public ProxyUser(IUser target){
        this.target = target;
    }
    @Override
    public int getUserId() {
        System.out.println("getting UserId by proxy...");
        // 调用目标类中的方法
         int res = target.getUserId();
        return res;
    }
}
