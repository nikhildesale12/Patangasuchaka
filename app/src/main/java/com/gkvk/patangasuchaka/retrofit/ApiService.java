package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.util.ApplicationConstant;
import com.ibin.plantplacepic.bean.CommentResponse;
import com.ibin.plantplacepic.bean.InformationResponseBean;
import com.ibin.plantplacepic.bean.LoginResponse;
import com.ibin.plantplacepic.bean.UserDetailResponseBean;
import com.ibin.plantplacepic.utility.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by NN on 30/01/2017.
 */

public interface ApiService {

    @GET(ApplicationConstant.LOGIN_SERVICE_URL)
    Call<LoginResponse> loginService(
            @Query("USER") String USER,
            @Query("PASS") String PASS,
            @Query("GOOG_ID") String GOOG_ID
    );
}
