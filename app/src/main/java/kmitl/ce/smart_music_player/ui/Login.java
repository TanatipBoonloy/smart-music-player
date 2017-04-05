package kmitl.ce.smart_music_player.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import kmitl.ce.smart_music_player.R;
import kmitl.ce.smart_music_player.model.request.LoginWithFacebookRequest;
import kmitl.ce.smart_music_player.model.response.UserResponse;
import kmitl.ce.smart_music_player.model.response.base.BaseResponse;
import kmitl.ce.smart_music_player.rest.ApiClient;
import kmitl.ce.smart_music_player.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import static android.app.PendingIntent.getActivity;

public class Login extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button continueLogin;
    private static final String PERMISSION = "publish_actions";
    private String LOGIN_TOKEN_PREFERENCE = "LOGIN_TOKEN_PREFERENCE";
    private String LOGIN_USER_ID_PREFERENCE = "LOGIN_USER_ID_PREFERENCE";
    private AccessToken accessToken;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String TYPE_ACTIVITY = "TYPE_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEditor = appSharedPrefs.edit();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        if (isLoggedIn() ) {
            LoginWithFacebookRequest request = new LoginWithFacebookRequest();
            request.setFacebookId(accessToken.getUserId());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<BaseResponse<UserResponse>> call = apiService.loginWithFacebook(request);
            call.enqueue(new Callback<BaseResponse<UserResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<UserResponse>> call, Response<BaseResponse<UserResponse>> response) {
                    if(response.isSuccessful()) {
                        prefsEditor.putString(LOGIN_USER_ID_PREFERENCE,response.body().getData().getUserId());
                        prefsEditor.apply();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UserResponse>> call, Throwable t) {
                    // ! do nothing
                    // ! implement soon!!!
                }
            });

        }

        // Callback registration -- First login with facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                String title = getString(R.string.error);
                String alertMessage = exception.getMessage();
                showResult(title, alertMessage);
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
            }
        });


        this.continueLogin = (Button) findViewById(R.id.local_login);
        this.continueLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<BaseResponse<UserResponse>> call = apiService.localLogin();
                call.enqueue(new Callback<BaseResponse<UserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<UserResponse>> call, Response<BaseResponse<UserResponse>> response) {
                        prefsEditor.putString(LOGIN_USER_ID_PREFERENCE,response.body().getData().getUserId());
                        prefsEditor.apply();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<UserResponse>> call, Throwable t) {
                        // do nothing now
                        // implement soon
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showResult(String title, String alertMessage) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    public boolean isLoggedIn() {
        this.accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
