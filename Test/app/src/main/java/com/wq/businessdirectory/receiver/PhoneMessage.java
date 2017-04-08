package com.wq.businessdirectory.receiver;

import java.io.Serializable;

/**
 */
public class PhoneMessage implements Serializable {
    String phone;
    String content;
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

    String addtime;

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

    public  static class  Contacts implements Serializable{
        String phone="1213";
        String name="4567";

        @Override
        public String toString() {
            return "Contacts{" +
                    "phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}