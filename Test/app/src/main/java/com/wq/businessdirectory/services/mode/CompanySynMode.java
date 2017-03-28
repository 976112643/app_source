package com.wq.businessdirectory.services.mode;

import android.content.Intent;

import com.wq.businessdirectory.common.db.mode.CompanyBean;
import com.wq.businessdirectory.common.mode.BaseBean;
import com.wq.businessdirectory.common.net.APIManager;
import com.wq.support.utils.log.Logger;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by WQ on 2017/3/28.
 */

public class CompanySynMode implements ActionMode {
    Realm mRealm = Realm.getDefaultInstance();

    @Override
    public void execute(Intent intent) {
//        CompanyBean firstBean=mRealm.where(CompanyBean.class).findAllSorted("update_time", Sort.DESCENDING).first();

        int page= intent.getIntExtra("page",1);
        try {
            BaseBean<List<CompanyBean>> baseBean = APIManager.getAPI().companys(page).execute().body();
            List<CompanyBean> companyBeanList = baseBean.Data();
            if(companyBeanList!=null){
                for (CompanyBean companyBean : companyBeanList) {
                    CompanyBean.addCompany(mRealm,companyBean);
                }
            }
            Logger.D(companyBeanList+"");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        mRealm.close();
    }
}
