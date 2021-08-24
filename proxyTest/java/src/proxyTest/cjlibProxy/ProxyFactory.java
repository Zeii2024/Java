package proxyTest.cjlibProxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyFactory implements MethodInterceptor { // 要实现 MethodIntercepter 接口
    // 维护一个对象
    private Object target;

    public ProxyFactory(Object target){
        this.target = target;
    }

    // 获取代理对象
    public Object getProxyInstance(){
        // 利用 Enhancer 来创建子类代理对象
        Enhancer enhancer = new Enhancer();
        // 设置父类，即需要代理的对象类
        enhancer.setSuperclass(target.getClass());
        // 设置代理人
        enhancer.setCallback(this); // this 可以换成 ProxyFactory 对象：new ProxyFactory(target)
        // 创建并返回代理对象
        return enhancer.create();
    }

    // 处理调用
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("proxy method invoke...");
        Object res = method.invoke(target,args);
        return null;
    }
}
