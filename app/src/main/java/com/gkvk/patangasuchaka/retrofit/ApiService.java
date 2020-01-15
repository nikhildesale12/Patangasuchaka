package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;



/**
 * Created by NN on 30/01/2017.
 */

public interface ApiService {

    @POST(ApplicationConstant.LOGIN_SERVICE_URL)
    Call<CommonResponse> loginService(
            @Body LoginRequest loginRequest
    );

    @POST(ApplicationConstant.SIGNUP_SERVICE_URL)
    Call<CommonResponse> signUpService(
            @Query("email") String email,
            @Query("password") String password,
            @Query("full_name") String full_name,
            @Query("username") String username
    );

    @POST(ApplicationConstant.SIGNUP_SERVICE_URL)
    Call<CommonResponse> feedbackService(
            @Query("full_name") String full_name,
            @Query("email") String email,
            @Query("contact") String contact,
            @Query("feedback") String feedback
    );

}
