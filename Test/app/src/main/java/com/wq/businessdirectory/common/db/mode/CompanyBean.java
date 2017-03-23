package com.wq.businessdirectory.common.db.mode;

import com.wq.support.utils.DateUtil;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by WQ on 2017/3/16.
 */

public class CompanyBean extends RealmObject {
    @PrimaryKey
    public int id;
    public String company_name="c "+ DateUtil.getCurrentDate(DateUtil.dateFormatHMS);
    public String company_descript;
    public String company_phone="15171438797";
    public String company_phone_name="weiquan";
    public String data_source;
    public String company_introduction;
    public int is_delete;
    public long add_time=System.currentTimeMillis();
    public long update_time;


}
