package com.itcheima.test;

import java.util.Date;

/**
 * @Author: Lucky
 * @Date:2020-02-10 1:07
 */
public class Demo {

    public static void main(String[] args) {
        String date="2020/02/10";
        Date date1 = new Date(date);
    //    System.out.println(date1);

       // Integer a= 345612345678900;


        Integer i=10000000;
        Integer j=127;
        int k=i;
        System.out.println(k);
        System.out.println(i==j);
        long l=10000;
       // Long L=100000;

        if(i>2){
            test();
          //  return false;
            System.out.println(2);
        }
        System.out.println(11);
    }
    public static Boolean test(){
        System.out.println(1);
            return false;
    }
}
