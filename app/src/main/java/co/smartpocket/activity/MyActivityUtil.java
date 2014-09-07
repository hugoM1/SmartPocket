package co.smartpocket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import co.smartpocket.R;
import co.smartpocket.callback.ImageLoadedCallback;
import co.smartpocket.http.VolleyClient;
import co.smartpocket.model.Product;
import co.smartpocket.util.UIUtils;

public class MyActivityUtil extends Activity {

    private TextView pName, pPrice;
    private ImageView pImage;
    private ProgressBar proView;
    private Button button;
    private Product pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity_util);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        pName = (TextView)findViewById(R.id.pName);
        pImage = (ImageView)findViewById(R.id.pImage);
        proView = (ProgressBar)findViewById(R.id.pBar);
        button = (Button)findViewById(R.id.button);

        VolleyClient volleyClient = new VolleyClient() {
            @Override
            public void response(Object response) {
                if(response instanceof Product){
                    Product product = (Product)response;
                    pro = product;
                    pName.setText(String.valueOf(product.getProductName()).substring(0, 20));
                    //Picasso.with(getParent()).load(String.valueOf(product.getProductImage())).into(pImage);

                    Picasso.with(getParent())
                        .load(String.valueOf(product.getProductImage()))
    //                                .placeholder(R.drawable.place_holder)
                        .skipMemoryCache()
                                //.into(m_image);
                        .into(pImage, new ImageLoadedCallback(proView) {
                            @Override
                            public void onSuccess() {
                                if (proView != null) {
                                    proView.setVisibility(View.GONE);
                                }
                            }
                        });

                }
            }

            @Override
            public void error(VolleyError error) {

            }
        };
        volleyClient.getProductDetail("");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyClient volley = new VolleyClient() {

                    @Override
                    public void response(Object response) {
                        setResult(10);
                        finish();
                    }

                    @Override
                    public void error(VolleyError error) {
                        finish();
                    }
                };
                volley.sendProduct(pro);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
