package com.sky.test;



//使用静态内部类的单例模式
public class SingletonTest4 {
    public static void main(String[] args) {
        Dish dish1 = Dish.getDishInstance();
        Dish dish2 = Dish.getDishInstance();
        System.out.println(dish1 == dish2);
    }


//
public static class Dish{

    private Dish(){}

    //静态内部类， 默认不加载
    private static class dishInstance{
        private static final Dish Instance = new Dish();
    }
    //获取唯一可用对象
    public static Dish getDishInstance(){
        //返回之前一定会先加载内部类
        return dishInstance.Instance;
    }
}

//    //枚举  SingleObject.INSTANCE.show()
//    public enum SingleObject {
//        INSTANCE; //唯一的单例对象
//        public void show(){
//            System.out.println("this is method");
//        }
//    }




}