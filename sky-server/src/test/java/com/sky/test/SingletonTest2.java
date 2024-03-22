package com.sky.test;


//单例模式的懒汉式实现

public class SingletonTest2 {
    public static void main(String[] args) {
        Order order1 = Order.getInstance();
        Order order2 = Order.getInstance();
        System.out.println(order2 == order1);  //输出true， 同一个对象
    }

}
class Order{
    //1. 私有化类的构造器哦
    private Order(){

    }

    //2. 声明当前类的对象， 没有初始化
    //4. 此对象也必须声明为static
    private static Order instance = null;

    //3. 声明public、static的返回当前类对象的方法
    public static Order getInstance(){
        if(instance == null){
            instance = new Order();
        }
        return instance;
    }
}