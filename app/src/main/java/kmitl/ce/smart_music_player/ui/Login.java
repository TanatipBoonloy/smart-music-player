package kmitl.ce.smart_music_player.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import kmitl.ce.smart_music_player.R;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {


    private TextView info;
        private LoginButton loginButton;
        private CallbackManager callbackManager;
        private   Animation anim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.login);





        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
//        loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();

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
//
                Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
                startActivity(i);

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }


            @Override
            public void onError(FacebookException exception) {

                info.setText("Login attempt failed.");
            }


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(data!=null ){
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            System.out.println("activity resulttttttttttttt");
        }
        System.out.println("activity not null");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
