package com.wq.businessdirectory.receiver.mode;

import com.wq.businessdirectory.common.db.DBHelper;
import com.wq.businessdirectory.common.db.mode.RealmSerializable;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 短消息实体类
 */
public class PhoneMessage extends RealmObject implements RealmSerializable {
    String phone;
    String content;
    String addtime;
    @PrimaryKey
    int _id = DBHelper.generateId();
    long add_time = System.currentTimeMillis();

    public static void addSMS(Realm realm, PhoneMessage phoneMessage) {
        realm.beginTransaction();
        realm.copyToRealm(phoneMessage);
        realm.commitTransaction();
    }

    @Override
    public String toString() {
        return "PhoneMessage{" +
                "phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getID() {
        return _id;
    }
}