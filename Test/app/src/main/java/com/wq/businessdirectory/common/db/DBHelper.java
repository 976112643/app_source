package com.wq.businessdirectory.common.db;

/**
 * Created by WQ on 2017/3/21.
 */

public class DBHelper {

    public static int generateId(){
        return String.valueOf(System.currentTimeMillis()).hashCode();
    }
}
