package kmitl.ce.smart_music_player.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jo on 4/4/2017.
 */

public class ApiClient {
    public static final String BASE_URL = "http://ec2-52-221-237-100.ap-southeast-1.compute.amazonaws.com:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
