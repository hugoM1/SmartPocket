package co.smartpocket.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import co.smartpocket.R;
import co.smartpocket.http.VolleyClient;

public class PayActivity extends Activity {

    private TextView text;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        text = (TextView)findViewById(R.id.data);
        button = (Button)findViewById(R.id.next);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        VolleyClient client = new VolleyClient() {
            @Override
            public void response(Object response) {
                if(response instanceof String){

                    try{
                        String te = (String)response;
                        JSONObject ob = new JSONObject(te);
                        text.setText(String.valueOf(ob.getString("pay_message")));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error(VolleyError error) {

            }
        };
        client.sentData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
