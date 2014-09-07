package co.smartpocket.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

import java.util.Random;
import java.util.UUID;

/**
 * Created by hugo on 9/6/14.
 */
public class UIUtils {

    /**
     * Phone Unique ID
     */
    public static String PHONE_UNIQUE_ID = "uniqueId";

    /**
     * Facebook User ID
     */
    public static String FACEBOOK_USER_ID ="facebookUserId";

    /**
     * Facebook Token
     */
    public static String FACEBOOK_TOKEN ="facebookToken";

    /**
     * Facebok User Name
     */
    public static String FACEBOOK_USER_NAME = "facebookUserName";

    /**
     * Facebook Login Ok
     */
    public static String IS_FACEBOOK_LOGGED = "facebookLogged";

    /**
     * Unregister User ID
     */
    public static String UNREGISTERED_USER_ID = "unRegisteredUserId";

    /**
     * Unregistered User Ok
     */
    public static String IS_UNREGISTERED_LOGGED = "unRegisteredLogged";

    /**
     * Facebook AppId
     */
    public static String FACEBOOK_APP_ID = "139737526177384";
    /**
     * Facebook App NameSpace
     */
    public static String FACEBOOK_APP_NAMESPACE = "yoamoloszapatos";

    /**
     * TAG VolleyPatterns Log or request
     */
    public static String TAG = "VolleyPatterns";

    // HelloShoes End-points
    // Using the same end-point for send applying filter
    protected static String BASE_URL ="http://dev3.ventas-yoamo.appspot.com/";
    protected static String BASE_FILTER_URL = "http://yoamo.shoelovers.co/";
    public static String URL_FILTER = BASE_FILTER_URL + "data/images/mock_filter_json.json";

    // Shoes TimeLine
    public static String URL_TIME_LINE = BASE_URL + "getTShoes/?user_id=";
    // Shoes Rate
    public static String URL_RATE = BASE_URL + "rateShoes/";
    // Facebook Login
//    public static String URL_FACEBOOK_LOGIN = BASE_URL + "mobile_registerHelloShoesApp/";
    public static String URL_FACEBOOK_LOGIN = BASE_URL + "saveUserInfoForHelloShoes/";
    // PushMessages
    public static String URL_PUSH_MESSAGES = BASE_URL + "getPushMessage/";

    // Facebook profile image
    public static final String URL_FACEBOOK_IMAGE = "https://graph.facebook.com/";
    public static String facebookPrefix = "/picture?type=normal";

    // Shoes Matches
    public static String URL_USER_MATCHES = BASE_URL + "getMatches/?user_id=";

    // Shoes Wish list
    public static String URL_USER_WISHLIST = BASE_URL + "getLiked/?user_id=";

    // Seen Matches
    public static String URL_SEEN_MATCHES = BASE_URL + "seenMatch/?";

    // Delete Match/Wish
    public static String URL_DELETE_MATCH_WISH = BASE_URL + "deleteMatch/?";

    // Fragment
    public static final int HOME_FRAGMENT = 0;
    public static final int STORE_FRAGMENT = 1;
    public static final int SETTINGS_FRAGMENT = 2;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity activity){
        int width = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            width = size.x;

            //int height = size.y;
        }else{
            Display displays = activity.getWindowManager().getDefaultDisplay();
            width = displays.getWidth();  // deprecated
            //int height = displays.getHeight();
        }
        return width;
    }

    public static String getCadenaAlfanumAleatoria (int longitud){
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i < longitud){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                cadenaAleatoria += c;
                i ++;
            }
        }
        return cadenaAleatoria;
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public static String getUniquePsuedoID(){

        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception e)
        {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


}
