package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;



/**
 * Created by NN on 30/01/2017.
 */

public interface ApiService {
    @POST(ApplicationConstant.LOGIN_SERVICE_URL)
    Call<CommonResponse> loginService(
            @Query("email") String email,
            @Query("password") String password
    );
}
