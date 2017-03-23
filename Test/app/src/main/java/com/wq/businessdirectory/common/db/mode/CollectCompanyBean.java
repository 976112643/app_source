package com.wq.businessdirectory.common.db.mode;

import io.realm.RealmObject;
import io.realm.annotations.RealmModule;

/**
 * Created by WQ on 2017/3/23.
 */

public class CollectCompanyBean extends RealmObject {
    public int id;
    public int company_id;
    public CompanyBean companyDetails;
    public long add_time=System.currentTimeMillis();

}
