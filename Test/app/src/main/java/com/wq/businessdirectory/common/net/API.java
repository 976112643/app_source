package com.wq.businessdirectory.common.net;

import com.wq.businessdirectory.common.db.mode.CompanyBean;
import com.wq.businessdirectory.common.mode.BaseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by WQ on 2017/3/28.
 */

public interface API {

    String  COMPANYS="Home/Index/companys";
    String BASE_URL = "http://192.168.1.133/naoke/Api/";
    @GET(COMPANYS)
    Call<BaseBean<List<CompanyBean>>> companys(@Query("p") int page);
}
