package com.digitopolis.saferide;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphResponse;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.GraphRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends SlidingMenuImp {

    ViewPager tab;
    TabPagerAdapter tabAdapter;
    ActionBar actionBar;
    ListView listView;
    ArrayAdapter< String > dataAdapter ;
    private List <String> list ;
    private List <String> listTmp ;
    EditText textSearch;
    private Button buttonReport;
    private Session session;
    private CallbackManager callbackManager;
    private String strShow="";
    private LoginManager loginManager;

    @Override
    protected void onResume() {
        if(session.getFinish().equals("1")){
            Intent sendIntent = new Intent(MainActivity.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            MainActivity.this.startActivity(sendIntent);
        }
        else{
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void Test(){

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);
        permissionNeeds = Arrays.asList("user_friends");
        loginManager.logInWithReadPermissions(this, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newGraphPathRequest(loginResult.getAccessToken(),
                        "/me/friends",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {

                                try {
                                    JSONObject friendListData = response.getJSONObject();
                                    JSONArray friendList = friendListData.getJSONArray("data");

                                    for (int i = 0; i < friendList.length(); i++) {
                                        JSONObject tmpJson = new JSONObject(friendList.get(i).toString());
                                        if (!list.contains(tmpJson.getString("name")))
                                            list.add(tmpJson.getString("name"));
                                        Log.e("REPONSE", friendList.get(i).toString());
                                    }

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                initailList();
                                initailEventMenu();

                            }
                        });

                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        setOrientation();
        listView = ( ListView ) this.findViewById(R.id.listView);
        list = new ArrayList<String>( );
        listTmp = new ArrayList<String>( );

        session = new Session(getBaseContext());
        if(session.getFinish().equals("1")){
            Intent sendIntent = new Intent(MainActivity.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            MainActivity.this.startActivity(sendIntent);
        }
        else{
        }


        setTabAdapter();
        initailMenu();
        initialButton();
        Test();
        listTmp = list;
        initailList();
        initailEventMenu();


//        checkScreen();
    }

    public void checkScreen(){
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    protected void setOrientation() {
        int current = getRequestedOrientation();
        if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        }
    }

    private void initailList(){
        dataAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, list );
        listView = (ListView) this.findViewById ( R.id.listView );
        listView.setAdapter(dataAdapter);
    }

    private void initailEventMenu(){
        textSearch = (EditText)getSlidingMenu().findViewById(R.id.editTextSearch);
        ImageView search_bt = (ImageView)getSlidingMenu().findViewById(R.id.button_search);
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listClone = new ArrayList<String>();
                for (String string : listTmp) {
                    if (string.contains(textSearch.getText().toString())) {
                        listClone.add(string);
                    }
                }
                if (listClone.size() == 0) {
                    list = listTmp;
                } else {
                    list = listClone;
                }
                initailList();

            }
        });
    }

    private void initialButton(){
        Button b = (Button)findViewById(R.id.button);
        buttonReport = b;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReport.setBackgroundResource(R.drawable.but_report);
                getSlidingMenu().showMenu();
            }
        });
    }

    private void initailMenu(){
        setBehindContentView(R.layout.menu_report);
        getSlidingMenu().setBehindOffset(100);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        getSlidingMenu().setMode(SlidingMenu.RIGHT);
        getSlidingMenu().setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                buttonReport.setBackgroundResource(R.drawable.but_report);
            }
        });
    }

    public void setTabAdapter(){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new FirstMainMenu());
        fragmentList.add(new SecondMainMenu());
        this.getFragmentManager();
            tabAdapter = new TabPagerAdapter(getSupportFragmentManager(),fragmentList);

        tab = (ViewPager)findViewById(R.id.pager);
        tab.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                    }
                });
        //tab.setA
        tab.setAdapter(tabAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
