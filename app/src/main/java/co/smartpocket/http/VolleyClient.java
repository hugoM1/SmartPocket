package co.smartpocket.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.smartpocket.app.SmartPocketApplication;
import co.smartpocket.model.Product;
import co.smartpocket.parser.JSONParser;

/**
 * Created by hugo on 9/7/14.
 */
public abstract class VolleyClient implements Response.ErrorListener {

    private JsonObjectRequest jsonObject;
    private StringRequest stringRequest;
    private JsonArrayRequest jsonArrayRequest;

    abstract public void response(Object response);
    abstract public void error(VolleyError error);

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        error(volleyError);
    }

    public void registerUser(){
        stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }
        };
    }

    public void getProductDetail(String barCode){
        jsonObject = new JsonObjectRequest(Request.Method.GET, "http://smartpocket.me/load_database/" + barCode, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONParser parser = new JSONParser();
                response(parser.productParser(jsonObject));
            }
        }, this);
        SmartPocketApplication.getInstance().addToRequestQueue(jsonObject, "productDetail");
    }
    //http://smartpocket.me/addProduct/?image=loquesea&name=aasdfasd&price=123
    /*"http://smartpocket.me/addProduct/"
            + "?name=" + product.getProductName()
            + "&image=" + product.getProductImage()
            + "&price=" + product.getProductPrice()*/
    public void sendProduct(Product product){
        stringRequest = new StringRequest(Request.Method.GET,"http://smartpocket.me/addProduct/?key=" + product.getProductKey() , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.w("ADD_RESPONSE", s);
                response(s);
            }
        }, this);
        SmartPocketApplication.getInstance().addToRequestQueue(stringRequest, "addProduct");
    }

    public void getCart(){
        jsonArrayRequest = new JsonArrayRequest("http://smartpocket.me/getCart/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONParser jsonParser = new JSONParser();
                response(jsonParser.productListParser(jsonArray));
            }
        }, this);
        SmartPocketApplication.getInstance().addToRequestQueue(jsonArrayRequest, "getCart");
    }

    public void getPayData(){
        jsonObject = new JsonObjectRequest(Request.Method.GET,"http://smartpocket.me/getPaymentData/",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                response(jsonObject);
            }
        }, this);
        SmartPocketApplication.getInstance().addToRequestQueue(jsonObject, "getPayData");
    }

    public void sentData(){
        stringRequest = new StringRequest(Request.Method.GET,"http://smartpocket.me/payget/?"
                + "amount="+SmartPocketApplication.getPreferencesInstance().getString("amount")
                + "&customerId="+SmartPocketApplication.getPreferencesInstance().getString("customerId"), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                response(s);
            }
        }, this);
        SmartPocketApplication.getInstance().addToRequestQueue(stringRequest, "sendData");
    }

}
