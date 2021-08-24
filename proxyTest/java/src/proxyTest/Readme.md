1. 所谓代理模式，就是想要访问对象 A 时，不直接调用 A，而是实现一个对象 B，把 A 放到 B 中通过 B 来调用 A。
比如:
```java
A中有个方法 say(), 想调用这个 say()方法

但不直接用 A.say();
 
而是实现一个对象 B，
        
在 B 中：

B{
    Object target;
    public B(target){
        this.target = target
    }  
    say(){
        target.say();
    }
}
```
于是可以通过
```java
a = new A();

b = new B(a);

用 b.say()来实现 a.say()的调用;
```
相当于在我们要调用的对象外面又加了一层对象，也就是加了个代理层，

B 就是 A 的代理对象，调用对象 B 的方法，就相当于调用 A 的方法。

以上基本就是静态代理的过程。

2. 知道了它的实现，就忍不住想问，为什么要这么做，为什么不直接调用 A.say(),而要在外面再套一层，多麻烦。

实际上，这么做是为了保护要调用的类 A，或者不能直接对 A 调用，
比如 A 在远程机器上。或者一些其他的原因，
比如对象创建开销很大，或者某些操作需要安全控制，或者需要进程外的访问等，
或者直接访问会给使用者或者系统结构带来很多麻烦，总之在有些场景下，代理模式是很有用的。

代理模式有哪些优点：代码解耦，可扩展性？？

3. 上面静态代理有明显的缺点。
   
在定义代理类 B 时，要包含 A 所有要被代理的方法，
   如果有很多个类要被代理，就要写很多个代理类，代码冗长，且不易扩展。

于是有了动态代理，动态代理就是不再手动实现代理类，而是利用 java 的反射机制，获得目标类的接口，
利用接口来创建代理对象，很显然这种方式更灵活，我们可以根据不同的目标对象动态的获取代理对象，
而不是每个目标对象都要实现一个代理对象。

**动态代理的核心是 java 反射：**

> 1. 接口和目标类的实现和静态代理没有区别
>
> 2. 在动态代理中，需要获取 target 的类加载器ClassLoader,target 实现的接口 Interface,
以及方法调用时的处理器 invocationHandler
> 
> 3. 然后利用 Proxy.newProxyInstance()方法获得代理对象
代理对象即可直接调用目标的方法

Handler需要实现 InvocationHandler 接口并重写 invoke 方法
```java
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
```
动态代理有一个条件，就是**目标函数必须实现一个接口**，因为反射要拿到目标对象的 Class 或 Interface
，然后才能创建对应的实例。

那为什么必须要实现接口的，不能继承一个类吗？
因为在反射过程中，目标函数会继承 Proxy 类，java中不能多继承，所以需要实现一个接口。

另外注意, JDK 动态代理中，在目标对象方法内部调用自己的另一个方法时，另一个方法在执行时
实际上是直接执行的目标对象中的方法，没有经过代理对象，因为内部方法中的另一个方法，它的
this 指针指向的是目标对象，不是代理对象。

4. Cglib(Code Generation Library)

cglib 是一个第三方代码生成库，运行时在内存中动态生成一个子类对象从而实现对目标对象功能的扩展。

cjlib 相对于 java 动态代理的区别：

   - 动态代理必须实现一个或多个接口，cjlib 不用
   - cjlib 通过继承获取代理对象，需要重写方法，所以不能代理 final 修饰的类
   - java 反射比较消耗性能，cglib 在生成代理对象时更加快速高效
```java
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
```

```java
package proxyTest.cjlibProxy;

public class Test {
    public static void main(String[] args) {
        RealUser target = new RealUser();
        // 输出下 target 的类
        System.out.println(target.getClass());

        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 获取代理实例对象
        RealUser proxy = (RealUser) proxyFactory.getProxyInstance();

        //输出下 proxy 的类
        System.out.println(proxy.getClass());
        proxy.getUserId();
    }
}
```

cglib 在使用时，应该是很多第三方库在使用时，都会用到字节码解析工具 asm，要在工程中
引入 asm 依赖，否则可能会报错：`Exception in thread "main" java.lang.NoClassDefFoundError: org/objectweb/asm/Type`

在 maven 中加入 asm 和 ant 依赖。
