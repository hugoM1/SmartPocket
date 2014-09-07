package co.smartpocket.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.smartpocket.R;
import co.smartpocket.adapter.DrawerAdapter;
import co.smartpocket.adapter.DrawerMatchesAdapter;
import co.smartpocket.app.SmartPocketApplication;
import co.smartpocket.fragment.FragmentScanner;
import co.smartpocket.http.VolleyClient;
import co.smartpocket.model.DrawerItem;
import co.smartpocket.model.Product;
import co.smartpocket.model.Products;
import co.smartpocket.session.SessionManager;
import co.smartpocket.util.UIUtils;
import co.smartpocket.view.RoundedTransformation;


public class HomeActivity extends Activity{

    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] menuItems;
    private ListView mDrawerList, mDrawerMatchList;
    private CharSequence mTitle;
    private ActionBar actionBar;
    private FrameLayout rightDrawer, leftDrawer;
    private Button logOutButton;
    private DrawerMatchesAdapter matchesAdapter;
    private ArrayList<Product> matches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getActionBar().setIcon(R.drawable.logo_menu);
        getActionBar().setTitle("");
        actionBar = getActionBar();
        matchesAdapter = new DrawerMatchesAdapter(HomeActivity.this, 0, matches);
        leftDrawer = (FrameLayout) findViewById(R.id.leftDrawer);
        rightDrawer = (FrameLayout) findViewById(R.id.rightDrawer);
        mDrawerMatchList = (ListView) findViewById(R.id.right_drawer);
        mDrawerMatchList.setClickable(true);

        if(SmartPocketApplication.getSessionManagerInstance().isUserLoggedIn() == false){
            goToRegister();
        }else{
            setUpView();
            setUpDrawerLayout();
        }

        VolleyClient client = new VolleyClient() {
            @Override
            public void response(Object response) {
                JSONObject jsonObject = (JSONObject)response;
                try{
                    SmartPocketApplication.getPreferencesInstance().putString("amount", jsonObject.getString("amount"));
                    SmartPocketApplication.getPreferencesInstance().putString("customerId", jsonObject.getString("customerId"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void error(VolleyError error) {

            }
        };
        client.getPayData();
    }

    /**
     * User is not logged yet
     */
    private void goToRegister(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void restoreActionBar() {
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setUpView(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.content_frame, FragmentScanner.getInstance(),
                    "CURRENT_FRAGMENT");
            ft.commit();
        }
    }

    private void setUpDrawerLayout(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mAdapter = new DrawerAdapter(this);

        menuItems = getResources().getStringArray(R.array.ns_menu_items);
        String[] menuItemsIcon = getResources().getStringArray(
                R.array.ns_menu_items_icon);

        int res = 0;
        for (String item : menuItems) {

            int id_title = getResources().getIdentifier(item, "string",
                    this.getPackageName());
            int id_icon = getResources().getIdentifier(menuItemsIcon[res],
                    "drawable", this.getPackageName());

            DrawerItem mItem = new DrawerItem(id_title, id_icon);
            mAdapter.addItem(mItem);
            res++;
        }

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();

        // Add header news title
        ViewGroup header_news
                = (ViewGroup)inflater.inflate(R.layout.drawer_left_header, mDrawerList, false);
        mDrawerList.addHeaderView(header_news, null, false);

        ImageView ima = (ImageView) findViewById(R.id.imageProfile);

        Picasso.with(this)
                .load(UIUtils.URL_FACEBOOK_IMAGE + SmartPocketApplication.getSessionManagerInstance().getUserId() + UIUtils.facebookPrefix)
                .transform(new RoundedTransformation())
                .into(ima);

        TextView userName = (TextView) findViewById(R.id.textProfile);
//        userName.setTypeface(UIUtils.getLightTypeFace(this));
        userName.setText(String.valueOf(SmartPocketApplication.getSessionManagerInstance().getUserName()));

        if (mDrawerList != null)
            mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        logOutButton = (Button) findViewById(R.id.logoutButton);
        /*logOutButton.setTypeface(UIUtils.getLightTypeFace(this));
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Logout", "Ok");
                FacebookClient facebookClient = new FacebookClient() {
                    @Override
                    public void loginSuccess() {

                    }

                    @Override
                    public void loginFail() {

                    }

                    @Override
                    public void logOut() {
                        HelloShoesApplication.getPreferencesInstance().clear();
                        goToRegister();
                    }
                };
                facebookClient.doLogout(SimpleFacebook.getInstance(), HomeActivity.this);
                mDrawerLayout.closeDrawers();

            }
        });*/


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private class DrawerItemClickListener implements
            AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            switch (position) {
                case 1:
                    mDrawerLayout.closeDrawers();

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:
                    // Facebook Login
                    mDrawerLayout.closeDrawers();
//                    if(!tinyDB.getBoolean("looged")){
//                        doLogin();
//                    }else{
//                        doLogout();
//                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void fragmentSwitcher(Fragment which) {
        if (getFragmentManager().findFragmentByTag("CURRENT_FRAGMENT")
                .equals(which)) {
            mDrawerLayout.closeDrawer(mDrawerList);
            //Toast.makeText(this, "Same fragment", Toast.LENGTH_SHORT).show();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, which, "CURRENT_FRAGMENT")
                    .commit();
            mDrawerLayout.closeDrawer(leftDrawer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mDrawerLayout.isDrawerOpen(rightDrawer)){
            mDrawerLayout.closeDrawer(rightDrawer);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        //restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            mDrawerLayout.closeDrawer(rightDrawer);
            return true;
        }
        if(item.getItemId() == R.id.action_matches){
            if(mDrawerLayout.isDrawerOpen(leftDrawer)){
                mDrawerLayout.closeDrawer(leftDrawer);
                mDrawerLayout.openDrawer(rightDrawer);
            }else{
//				setUpMatches(tinyDB.getString("userId"));
//				adapter.notifyDataSetChanged();
                mDrawerLayout.openDrawer(rightDrawer);
            }

            if(mDrawerLayout.isDrawerOpen(rightDrawer)){
                mDrawerLayout.closeDrawer(rightDrawer);
            }
        }else if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this, PayActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 10) if (resultCode == 10) {
                VolleyClient client = new VolleyClient() {
                    @Override
                    public void response(Object response) {
                        if(response instanceof Products){
                            Products products = (Products)response;
                            Log.w("PRO", products.getProducts().get(1).getProductName());
                            setUpCart(products.getProducts());
                        }
                    }

                    @Override
                    public void error(VolleyError error) {

                    }
                };
                client.getCart();
            }
        }

    public void setUpCart(ArrayList<Product> products){
        matchesAdapter = new DrawerMatchesAdapter(HomeActivity.this, 0, products);
        mDrawerMatchList.setAdapter(matchesAdapter);
        matchesAdapter.notifyDataSetChanged();
    }
}
