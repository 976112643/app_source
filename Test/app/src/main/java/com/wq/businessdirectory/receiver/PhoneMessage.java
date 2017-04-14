package com.wq.businessdirectory.receiver;

import android.content.Context;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 */
public class PhoneMessage extends RealmObject {
    String phone;
    String content;
    String addtime;
    long add_time=System.currentTimeMillis();
    public static void addSMS(Realm realm,PhoneMessage phoneMessage){
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

}