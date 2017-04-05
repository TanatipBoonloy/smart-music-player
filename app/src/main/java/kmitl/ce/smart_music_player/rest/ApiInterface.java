package kmitl.ce.smart_music_player.rest;

import java.util.List;

import kmitl.ce.smart_music_player.model.request.LoginWithFacebookRequest;
import kmitl.ce.smart_music_player.model.response.MusicResponse;
import kmitl.ce.smart_music_player.model.response.UserResponse;
import kmitl.ce.smart_music_player.model.response.base.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jo on 4/4/2017.
 */

public interface ApiInterface {

    @POST("api/v1/smp/login/facebook")
    Call<BaseResponse<UserResponse>> loginWithFacebook(@Body LoginWithFacebookRequest request);

    @GET("api/v1/smp/login/new")
    Call<BaseResponse<UserResponse>> localLogin();

    @GET("api/v1/smp/songs/random/{qty}")
    Call<BaseResponse<List<MusicResponse>>> getRandomSong(@Path("qty")int qty);
}
