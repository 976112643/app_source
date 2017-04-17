package com.wq.businessdirectory;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public void genericityTest1() {

    }

    public void upperBound(List<? extends CharSequence> list, CharSequence date) {
        CharSequence now = list.get(0);
        System.out.println("now==>" + now);
//        list.add(date); //这句话无法编译
//        list.add(System.currentTimeMillis()+""); //这句话无法编译
        list.add(null);//这句可以编译，因为null没有类型信息
    }

    public void lowerBound(List<? super CharSequence> list, CharSequence date) {
        String now = System.currentTimeMillis() + "";
        list.add(now);
        list.add(date);
//        CharSequence time = list.get(0); //不能编译
    }

    //    interface Payable<T>{
//    }
//    class Employee implements Payable<Employee>{
//    }
//    class Hourly extends Employee implements Payable<Hourly>{
//    }
    class SelfBounded<T extends SelfBounded<T>> {

    }
    public class SelfBoundedImpl extends SelfBounded<SelfBoundedImpl> {

    }


    public class Generic<T, S extends T> {
    }

    public class GenericImpl extends Generic<CharSequence, String> {

    }


}