package com.sky.test;

/**
 * 1.单例模式：确保某个类只有一个实例对象
 *
 * 2. 区分饿汉式和懒汉式
 *      饿汉式： 坏处： 对象加载时间过长
 *              好处： 饿汉式是线程安全
 *      懒汉式： 好处：延迟对象创建
 *              坏处： 懒汉式是线程不安全的
 */




public class SingletonTest1 {
    public static void main(String[] args) {

        Bank bank1 = Bank.getInstance();
        Bank bank2 = Bank.getInstance();
        System.out.println(bank1 == bank2);  //true  表明是同一个对象


    }
}

//饿汉式
class Bank{
    //1.私有化类的构造器
    private Bank(){

    }
    //2. 内部创建类的对象
    //4. 要求此对象也必须声明为静态的
    private static Bank instance = new Bank();

    //3. 提供公共的方法，返回类的对象
    public static Bank getInstance(){
        return instance;
    }
}

