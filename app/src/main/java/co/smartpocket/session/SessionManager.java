package co.smartpocket.session;

import android.content.Context;
import android.util.Log;

import co.smartpocket.app.SmartPocketApplication;
import co.smartpocket.util.UIUtils;

/**
 * Created by hugo on 9/6/14.
 */
public class SessionManager {
    private Context context;
    public SessionManager(Context context){
        this.context = context;
    }

    public boolean isUserLoggedIn(){
        boolean loggedIn;
        if(isUserFacebookLoggedIn() == false && isUnregisteredUserLoggedIn() == false){
            loggedIn = false;
        }else{
            loggedIn = true;
        }
        return loggedIn;
    }

    public void setFacebookSession(
            String facebookUserId,
            String facebookUserName,
            String facebookToken){
        Log.wtf("SESSIONMANAGER", "OK");
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.PHONE_UNIQUE_ID, UIUtils.getUniquePsuedoID());
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.FACEBOOK_USER_ID, facebookUserId);
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.FACEBOOK_USER_NAME, facebookUserName);
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.FACEBOOK_TOKEN, facebookToken);
        SmartPocketApplication
                .getPreferencesInstance()
                .putBoolean(UIUtils.IS_FACEBOOK_LOGGED, true);
    }

    public boolean isUserFacebookLoggedIn(){
        return SmartPocketApplication
                .getPreferencesInstance()
                .getBoolean(UIUtils.IS_FACEBOOK_LOGGED);
    }

    public String getFacebookUserId(){
        return SmartPocketApplication
                .getPreferencesInstance()
                .getString(UIUtils.FACEBOOK_USER_ID);
    }

    public void setUregisterSession(){
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.PHONE_UNIQUE_ID, UIUtils.getUniquePsuedoID());
        SmartPocketApplication
                .getPreferencesInstance()
                .putString(UIUtils.UNREGISTERED_USER_ID, UIUtils.getUniquePsuedoID());
        SmartPocketApplication
                .getPreferencesInstance()
                .putBoolean(UIUtils.IS_UNREGISTERED_LOGGED, true);
    }

    public boolean isUnregisteredUserLoggedIn(){
        return SmartPocketApplication
                .getPreferencesInstance()
                .getBoolean(UIUtils.IS_UNREGISTERED_LOGGED);
    }

    public String getUnregisteredUserId(){
        return SmartPocketApplication
                .getPreferencesInstance()
                .getString(UIUtils.UNREGISTERED_USER_ID);
    }

    public String getUserId(){
        String user = null;
        if(isUserFacebookLoggedIn() == true){
            user = getFacebookUserId();
        }else{
            user = getUnregisteredUserId();
        }
        return user;
    }

    public String getUserName(){
        String userName = null;
        if(isUserFacebookLoggedIn() == true){
            userName = SmartPocketApplication
                    .getPreferencesInstance().getString(UIUtils.FACEBOOK_USER_NAME);
        }else{
            userName = "HelloShoes";
        }
        return userName;
    }
}
