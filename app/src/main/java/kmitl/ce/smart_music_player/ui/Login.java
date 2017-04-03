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
import android.widget.TextView;
import android.widget.Toast;

import kmitl.ce.smart_music_player.R;

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
import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Login extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Animation anim;
    private Button continueLogin;
    private Button register;
    private static final String PERMISSION = "publish_actions";
    private String LOGIN_TOKEN_PREFERENCE = "LOGIN_TOKEN_PREFERENCE";
    private String LOGIN_USER_ID_PREFERENCE = "LOGIN_USER_ID_PREFERENCE";
    private AccessToken accessToken;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Gson gson;
    private String TYPE_ACTIVITY = "TYPE_ACTIVITY";
    private String LOCAL = "LOCAL";
    private String FB_LOCAL = "FB_LOCAL";
    private String FB = "FB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEditor = appSharedPrefs.edit();
        gson = new Gson();
        String type= appSharedPrefs.getString(TYPE_ACTIVITY,"");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
//        loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();

        if (isLoggedIn() ) {

            Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
            startActivity(i);
        }
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                info.setText("successssss");
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );

                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                accessToken = AccessToken.getCurrentAccessToken();
//
//                prefsEditor.putString(TYPE_ACTIVITY,"FB");
//                prefsEditor.commit();

                Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
                startActivity(i);
//                finish();

            }

            @Override
            public void onCancel() {
//                info.setText("Login attempt canceled.");
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

//                LoginLocalFragment df= new LoginLocalFragment();
//                df.show(getSupportFragmentManager(), "LocalLogin");
                saveLocalToken("tokenExample", "userIDExample");

                Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
                startActivity(i);
            }
        });

//        this.register = (Button) findViewById(R.id.register);
//        this.register.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                RegisterFragment df= new RegisterFragment();
//                df.show(getSupportFragmentManager(), "register");
//            }
//        });

        String GetJson = appSharedPrefs.getString(TYPE_ACTIVITY, "");

        if (GetJson != "" ) {
            this.continueLogin.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data != null) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
//            System.out.println("activity resulttttttttttttt");
        }
//        System.out.println("activity not null");
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

    private void saveLocalToken(String token, String userID) {

//        String GetJson = appSharedPrefs.getString(LOGIN_TOKEN_PREFERENCE, "");
        prefsEditor.putString(LOGIN_TOKEN_PREFERENCE, token);
        prefsEditor.putString(LOGIN_USER_ID_PREFERENCE, userID);
        prefsEditor.putString(TYPE_ACTIVITY,"LOCAL");
        prefsEditor.commit();
    }

//    public void initSession(){ //init token form Preferences
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        token_login = prefs.getString(LOGIN_TOKEN_PREFERENCE,"");
//        time_token = prefs.getString(TIME_TOKEN_PREFERENCE,"");
//    }
//    public void setTokenSharedPreferences(String loginToken,String timeToken){// set token before login
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(LOGIN_TOKEN_PREFERENCE, loginToken);
//        editor.putString(TIME_TOKEN_PREFERENCE, timeToken);
//        editor.commit();
//    }
}
