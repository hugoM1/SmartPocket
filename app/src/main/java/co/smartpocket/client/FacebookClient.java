package co.smartpocket.client;

/**
 * Created by hugo on 9/6/14.
 */
import android.app.Activity;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public abstract class FacebookClient implements OnLoginListener, OnLogoutListener{

    abstract public void loginSuccess();
    abstract public void loginFail();
    abstract public void logOut();

    @Override
    public void onThinking() {

    }

    @Override
    public void onException(Throwable throwable) {

    }

    @Override
    public void onFail(String reason) {
        loginFail();

    }

    @Override
    public void onLogout() {
        logOut();

    }

    @Override
    public void onLogin() {
        loginSuccess();

    }

    @Override
    public void onNotAcceptingPermissions(Type type) {
    }

    public void doLogin(SimpleFacebook simple, Activity activity){
        simple = SimpleFacebook.getInstance(activity);
        simple.login(this);
    }

    public void doLogout(SimpleFacebook simple, Activity activity){
        simple = SimpleFacebook.getInstance(activity);
        simple.logout(this);
    }

}

