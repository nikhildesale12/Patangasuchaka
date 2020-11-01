package com.gkvk.retrofit;

import com.gkvk.bean.AboutUsResponse;
import com.gkvk.bean.CommonResponse;
import com.gkvk.bean.FeedbackRequest;
import com.gkvk.bean.ForgotPassRequest;
import com.gkvk.bean.ForgotPassResponse;
import com.gkvk.bean.HistoryRequest;
import com.gkvk.bean.HistoryResponse;
import com.gkvk.bean.LoginGoogleRequest;
import com.gkvk.bean.LoginNewResponse;
import com.gkvk.bean.LoginRequest;
import com.gkvk.bean.ProfileRequest;
import com.gkvk.bean.ProfileResponse;
import com.gkvk.bean.RegisterRequest;
import com.gkvk.bean.RegisterResponse;
import com.gkvk.bean.UploadDataToWebRequest;
import com.gkvk.bean.UploadDataToWebResponse;
import com.gkvk.bean.UploadImageToAIResponse;
import com.gkvk.bean.UploadToWebResponse;
import com.gkvk.util.ApplicationConstant;

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
    Call<LoginNewResponse> loginService(@Body LoginRequest loginRequest);

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

    @POST(ApplicationConstant.LOGIN_GOOGLE_SERVICE_URL)
    Call<LoginNewResponse> loginGoogleService(@Body LoginGoogleRequest loginRequest);

}
