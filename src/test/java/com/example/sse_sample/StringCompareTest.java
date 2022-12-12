package com.example.sse_sample;

import org.junit.jupiter.api.Test;

public class StringCompareTest {

    @Test
    public void stringCompare(){
        String one = "1_" + System.currentTimeMillis();
        String two = "1_" + System.currentTimeMillis();

        int i = one.compareTo(two);

        System.out.println("i = " + i);
        int i1 = two.compareTo(one);

        System.out.println("i1 = " + i1);
    }
}
