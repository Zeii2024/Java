package proxyTest.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 定义处理器来处理调用事件，要实现 InvocationHandler 接口
public class UserInvocationHandle implements InvocationHandler {
    public IUser target;

    public UserInvocationHandle(IUser target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("InvocationHandler invoke");
        // 通过反射调用目标对象方法
        return method.invoke(target, args);
    }
}
