package co.smartpocket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnProfileListener;

import co.smartpocket.R;
import co.smartpocket.app.SmartPocketApplication;
import co.smartpocket.client.FacebookClient;

public class WelcomeActivity extends Activity {

    private Button facebookLoginButton;
    private SimpleFacebook simpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setUpFacebookButton();
    }

    public void setUpFacebookButton(){
        facebookLoginButton = (Button)findViewById(R.id.facebookLogin_button);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpLoginFacebook();
            }
        });
    }

    public void setUpLoginFacebook(){
        FacebookClient facebookClient = new FacebookClient() {
            @Override
            public void loginSuccess() {
                 Log.wtf("LOGIN", "OK");

                OnProfileListener profileListener = new OnProfileListener() {
                    @Override
                    public void onComplete(Profile response) {
                        Log.wtf("PROFILE", "OK");
                        if(response != null){
                            SmartPocketApplication.getSessionManagerInstance().setFacebookSession(
                                    response.getId(),
                                    response.getName(),
                                    String.valueOf(simpleFacebook.getSession().getAccessToken()));

//                            registerUser(
//                                    response.getId(),
//                                    String.valueOf(simpleFacebook.getSession().getAccessToken()),
//                                    String.valueOf(
//                                            ParseInstallation
//                                                    .getCurrentInstallation().getInstallationId()),
//                                    profile
//                            );

                            goHome();
                        }
                        super.onComplete(response);
                    }
                };
                simpleFacebook.getProfile(profileListener);
            }
            @Override
            public void loginFail() {
                Log.e("HELLO-SHOES", "Login error");
            }

            @Override
            public void logOut() {

            }
        };
        facebookClient.doLogin(simpleFacebook, this);
    }

    public void goHome(){
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        simpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
