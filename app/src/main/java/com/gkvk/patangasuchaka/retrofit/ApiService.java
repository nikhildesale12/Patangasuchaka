package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.bean.AboutUsResponse;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.FeedbackRequest;
import com.gkvk.patangasuchaka.bean.HistoryRequest;
import com.gkvk.patangasuchaka.bean.HistoryResponse;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.bean.ProfileRequest;
import com.gkvk.patangasuchaka.bean.ProfileResponse;
import com.gkvk.patangasuchaka.bean.RegisterRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


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

    @POST(ApplicationConstant.HISTORY_SERVICE_URL)
    Call<HistoryResponse> historyService(@Body HistoryRequest historyRequest);

    @POST(ApplicationConstant.PROFILE_SERVICE_URL)
    Call<ProfileResponse> profileService(@Body ProfileRequest profileRequest);

    @GET(ApplicationConstant.ABOUT_US_URL)
    Call<AboutUsResponse> loadAboutData();
}
