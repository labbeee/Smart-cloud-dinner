package com.sky.test;


//使用同步机制将单例模式中的懒汉式改为线程安全的:  效率差
public class SingletonTest3 {
    public static void main(String[] args) {
        Employee emp1 = Employee.getInstance();
        Employee emp2 = Employee.getInstance();
        System.out.println(emp1 == emp2);
    }

}

class Employee{

    private Employee(){

    }
    private static Employee instance = null;

    //方式1. 同步方法
//    public static synchronized Employee getInstance(){
//        if(instance == null){
//            instance = new Employee();
//        }
//        return instance;
//    }

    //同步代码块
//    public static synchronized Employee getInstance(){
//        synchronized (Employee.class) {
//            if(instance == null){
//                instance = new Employee();
//            }
//            return instance;
//        }
//    }


    //方式2  双重检查 效率高
    public static synchronized Employee getInstance(){
        if (instance == null) {
            synchronized (Employee.class) {
                if(instance == null){
                    instance = new Employee();
                }
            }
        }
        return instance;
    }


}