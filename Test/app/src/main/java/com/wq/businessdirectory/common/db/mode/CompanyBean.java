package com.wq.businessdirectory.common.db.mode;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by WQ on 2017/3/16.
 */

public class CompanyBean extends RealmObject {
    @PrimaryKey
    public int id= (int) (System.currentTimeMillis()/1000);
    public String companyName="companyName";
    public String companyPhone=""+(long)(15171438700l+Math.random()*100);
    public Date addTime=new Date();
    
}
