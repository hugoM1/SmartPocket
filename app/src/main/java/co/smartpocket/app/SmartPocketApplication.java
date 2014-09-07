package co.smartpocket.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import co.smartpocket.preferences.TinyDB;
import co.smartpocket.session.SessionManager;
import co.smartpocket.util.UIUtils;

/**
 * Created by hugo on 9/6/14.
 */
public class SmartPocketApplication extends Application {

    /**
     * SessionManager object
     */
    private static SessionManager sessionManager;

    /**
     * TinyDB Shared preferences object
     */
    private static TinyDB preferences;

    /**
     * Facebook Permissions
     */
    Permission[] permissions = new Permission[]{
            Permission.USER_PHOTOS,
            Permission.EMAIL,
            Permission.PUBLISH_ACTION,
            Permission.USER_BIRTHDAY
    };

    /**
     * Log or request TAG
     */
    public static final String VOLLEY_TAG = UIUtils.TAG;

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance for the application class for easy access in other
     */

    private static SmartPocketApplication sInstance;


    @Override
    public void onCreate() {

        super.onCreate();

        sInstance = this;
        preferences = new TinyDB(this);
        sessionManager = new SessionManager(this);

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId("139737526177384").setNamespace(UIUtils.FACEBOOK_APP_NAMESPACE)
                .setPermissions(permissions).build();
        SimpleFacebook.setConfiguration(configuration);
    }

    /**
     *
     * @return ApplicationController singleton instance
     */
    public static synchronized SmartPocketApplication getInstance(){
        return sInstance;
    }

    public static synchronized TinyDB getPreferencesInstance(){
        return preferences;
    }

    public static synchronized SessionManager getSessionManagerInstance(){
        return sessionManager;
    }

    /**
     *
     * @return The Volley Request queue, the queue will be crated if it is null
     */
    public RequestQueue getRequestQueue(){
        // Lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add the specified request to the global queue, if tag is specified then
     * it is used else Default VOLLEY_TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag){
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? VOLLEY_TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    /**
     * Add the specified request to the global queue using Default VOLLEY_TAG
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req){
        // set the default tag if tag is empty
        req.setTag(VOLLEY_TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending request by the specified TAG, it is important to
     * specify a TAG so that the pending?ongoing requests can be cancelled
     *
     * @param tag
     */
    public void cancePendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
