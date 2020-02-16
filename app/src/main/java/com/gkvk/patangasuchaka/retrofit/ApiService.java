package com.gkvk.patangasuchaka.retrofit;

import com.gkvk.patangasuchaka.bean.AboutUsResponse;
import com.gkvk.patangasuchaka.bean.CommonResponse;
import com.gkvk.patangasuchaka.bean.FeedbackRequest;
import com.gkvk.patangasuchaka.bean.ForgotPassRequest;
import com.gkvk.patangasuchaka.bean.ForgotPassResponse;
import com.gkvk.patangasuchaka.bean.HistoryRequest;
import com.gkvk.patangasuchaka.bean.HistoryResponse;
import com.gkvk.patangasuchaka.bean.LoginRequest;
import com.gkvk.patangasuchaka.bean.ProfileRequest;
import com.gkvk.patangasuchaka.bean.ProfileResponse;
import com.gkvk.patangasuchaka.bean.RegisterRequest;
import com.gkvk.patangasuchaka.bean.RegisterResponse;
import com.gkvk.patangasuchaka.bean.UploadDataToWebRequest;
import com.gkvk.patangasuchaka.bean.UploadDataToWebResponse;
import com.gkvk.patangasuchaka.bean.UploadImageToAIResponse;
import com.gkvk.patangasuchaka.bean.UploadToWebResponse;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by NN on 30/01/2017.
 */

public interface ApiService {

    @POST(ApplicationConstant.LOGIN_SERVICE_URL)
    Call<CommonResponse> loginService(@Body LoginRequest loginRequest);

    @POST(ApplicationConstant.FORGOTPASS_SERVICE_URL)
    Call<ForgotPassResponse> forgotPassService(@Body ForgotPassRequest forgotPassRequest);

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

    @POST(ApplicationConstant.UPLOAD_DATA_WEB_BUTTERFLY_URL)
    Call<UploadDataToWebResponse> uploadDataToWebServerButterFly(@Body UploadDataToWebRequest uploadDataToWebRequest);

    @POST(ApplicationConstant.UPLOAD_DATA_MOTH_URL)
    Call<UploadDataToWebResponse> uploadDataToWebServerMoth(@Body UploadDataToWebRequest uploadDataToWebRequest);

    @GET(ApplicationConstant.GET_DISTRIBUTION_DATA)
    Call<HistoryResponse> getDistributionData();

    @Multipart
    @POST(ApplicationConstant.UPLOAD_IMAGE_AI)
    Call<List<UploadImageToAIResponse>> uploadImageToAI(@Part MultipartBody.Part image);

    @Multipart
    @POST(ApplicationConstant.UPLOAD_IMAGE_WEB)
    Call<UploadToWebResponse> uploadImageToWeb(@Part MultipartBody.Part body);
}
