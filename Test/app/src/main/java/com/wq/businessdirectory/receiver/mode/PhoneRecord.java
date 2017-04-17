package com.wq.businessdirectory.receiver.mode;

import com.wq.businessdirectory.common.db.DBHelper;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 通话记录实体类
 * Created by WQ on 2017/4/14.
 */

public class PhoneRecord extends RealmObject {
    String phone;
    String type;
    String record_time;
    @PrimaryKey
    int _id = DBHelper.generateId();
    long add_time = System.currentTimeMillis();

    public static void addPhoneRecord(Realm realm, PhoneRecord phoneRecord) {
        realm.beginTransaction();
        realm.copyToRealm(phoneRecord);
        realm.commitTransaction();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }
}
