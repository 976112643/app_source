package com.wq.businessdirectory.common.db.mode;

import com.wq.support.utils.DateUtil;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

import static com.wq.businessdirectory.common.db.DBHelper.generateId;

/**
 * Created by WQ on 2017/3/23.
 */

public class CollectCompanyBean extends RealmObject {
    @PrimaryKey
    public int id;
    public int company_id;
    public CompanyBean companyDetails;
    public long add_time ;

    public String getTime(){
        return DateUtil.getTimeSimpleText(add_time);
    }
    public static void addOrDelCollect(Realm mRealm, CompanyBean companyDetails) {
        mRealm.beginTransaction();
        CollectCompanyBean tmpBean= mRealm.where(CollectCompanyBean.class).equalTo("company_id", companyDetails.id).findFirst();
        if(tmpBean!=null){
            tmpBean.deleteFromRealm();
        }else {
            CollectCompanyBean collectCompanyBean=mRealm.createObject(CollectCompanyBean.class);
            collectCompanyBean.id=generateId();
            collectCompanyBean.company_id=companyDetails.id;
            collectCompanyBean.companyDetails=companyDetails;
            collectCompanyBean.add_time= System.currentTimeMillis();
//            mRealm.insertOrUpdate(collectCompanyBean);
            mRealm.copyToRealmOrUpdate(collectCompanyBean);

        }
        mRealm.commitTransaction();

    }
}
