package com.wq.businessdirectory.common.db.mode;

import com.google.gson.annotations.SerializedName;
import com.wq.support.utils.DateUtil;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by WQ on 2017/3/16.
 */

public class CompanyBean extends RealmObject {
    @PrimaryKey
    public int id;
    @SerializedName("title")
    public String company_name;
    public String company_descript;
    @SerializedName("phone")
    public String company_phone="";
    public String company_phone_name="";
    public String data_source;
    public String company_introduction;
    public int is_delete;
    public long add_time=System.currentTimeMillis();
    public long update_time;


    public static void addCompany(Realm mRealm, CompanyBean companyBean){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(companyBean);
        mRealm.commitTransaction();
    }

}
