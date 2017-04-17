package com.wq.businessdirectory.common.db;

import android.content.Intent;

import com.wq.businessdirectory.common.db.mode.RealmSerializable;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * 数据库帮助类
 *  Created by WQ on 2017/3/21.
 */

public class DBHelper {
    public static  final String DEF_ID_NAME="_id";
    public static int generateId(){
        return String.valueOf(System.currentTimeMillis()).hashCode();
    }
    public static <T extends RealmModel & RealmSerializable> void put2Intent(Intent intent,T entity){
        intent.putExtra(entity.getClass().getName(),entity.getID());//存放id
    }
    public static <T extends RealmModel >  T getMode(Intent intent,Class<? extends RealmModel> clazz){
        int _ID=intent.getIntExtra(clazz.getName(),-1);
        RealmModel result=null;
        if(_ID!=-1){
            result=   Realm.getDefaultInstance().where(clazz).equalTo(DEF_ID_NAME,_ID).findFirst();
        }
        return (T) result;
    }
}
