package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.bean.AboutUsResponse;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.FeedbackRequest;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.bean.RegisterRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;



/**
 * Created by NN on 30/01/2017.
 */

public interface ApiService {

    @POST(ApplicationConstant.LOGIN_SERVICE_URL)
    Call<CommonResponse> loginService(@Body LoginRequest loginRequest);

    @POST(ApplicationConstant.SIGNUP_SERVICE_URL)
    Call<RegisterResponse> signUpService(@Body RegisterRequest registerRequest);

    @POST(ApplicationConstant.FEEDBACK_SERVICE_URL)
    Call<RegisterResponse> feedbackService(@Body FeedbackRequest feedbackRequest);

    @GET(ApplicationConstant.ABOUT_US_URL)
    Call<AboutUsResponse> loadAboutData();
}
