package kmitl.ce.smart_music_player.network;

import java.util.List;

import kmitl.ce.smart_music_player.models.Music;
import kmitl.ce.smart_music_player.models.rest.request.LoginWithFacebookRequest;
import kmitl.ce.smart_music_player.models.rest.response.UserResponse;
import kmitl.ce.smart_music_player.models.rest.response.base.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jo on 4/4/2017.
 */

public interface ApiInterface {

    @POST("api/v1/smp/activity_login/facebook")
    Call<BaseResponse<UserResponse>> loginWithFacebook(@Body LoginWithFacebookRequest request);

    @GET("api/v1/smp/activity_login/new")
    Call<BaseResponse<UserResponse>> localLogin();

    @GET("api/v1/smp/songs/random/{qty}")
    Call<BaseResponse<List<Music>>> getRandomSong(@Path("qty")int qty);
}
