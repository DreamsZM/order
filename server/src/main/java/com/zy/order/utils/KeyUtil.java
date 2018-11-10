package com.zy.order.utils;

import java.util.Random;

public class KeyUtil {

    public static synchronized String getUniqueKey(){
        Random random = new Random();
        int number = random.nextInt(9000000) + 1000000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
