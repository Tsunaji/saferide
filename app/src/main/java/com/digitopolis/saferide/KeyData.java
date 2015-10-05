package com.digitopolis.saferide;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Bank on 8/6/2015.
 */
public class KeyData extends SlidingMenuNew {

    private EditText specEditText;
    private EditText sourceEditText;
    private EditText destinationEditText;
    private boolean haveSpec = true;
    private String strSpec = "";
    private CallbackManager callbackManager;
    private String strShow="";
    private LoginManager loginManager;
    private Bitmap bmpImage ;
    private Session session;
    private Typeface customTypeFace ;
    private Bitmap bitmapTmp;


    @Override
    protected void onResume() {
        if(session.getFinish().equals("1")){
            Log.e("EEE", "1");
            Intent sendIntent = new Intent(KeyData.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            KeyData.this.startActivity(sendIntent);
        }
        else{
            Log.e("EEE","0");
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.key_data);

        Bundle ex = getIntent().getExtras();

        initailSession();

        customTypeFace = Typeface.createFromAsset(getAssets(), "fonts/aaa 2005_iannnnnHBO.ttf");
        GPSTracker gps = new GPSTracker(getBaseContext());
        ll = gps.getLatitude()+","+gps.getLongitude();
        if(ll.equals("0.0,0.0")){
            ll = "13.754409,100.535549";
        }

        setOrientation();
        initialImage(ex);
        initialButton();
        initialEditText();
        initialTextView(ex);
        initailMenu();
    }

    private void initailSession(){
        session = new Session(getBaseContext());
        if(session.getFinish().equals("1")){
            Intent sendIntent = new Intent(KeyData.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            KeyData.this.startActivity(sendIntent);
        }
        else{
        }
    }

    private void initailMenu(){
        setBehindContentView(R.layout.manu_place);
        getSlidingMenu().setBehindOffset(30);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        getSlidingMenu().setMode(SlidingMenu.RIGHT);
        getSlidingMenu().setBackgroundResource(R.drawable.menu_page);
        startWorking();
    }

    private void initialImage(Bundle ex){
        ImageView imageTop = (ImageView)findViewById(R.id.image_top);
        if(ex.getString("haveImage").equals("Y")) {
            Bitmap bmp = ex.getParcelable("bitmapImage");
            bmpImage = bmp;
            imageTop.setImageBitmap(bmp);
            imageTop.setBackgroundResource(R.drawable.picture_farm);
        }
        else{
            int bitmap = 0;
            switch (ex.getString("type_selected")){

                case "01" :
                    bitmap = R.drawable.icon_01;
                    break;
                case "02" :
                    bitmap = R.drawable.icon_02;
                    break;
                case "03" :
                    bitmap = R.drawable.icon_03;
                    break;
                case "04" :
                    bitmap = R.drawable.icon_04;
                    break;
                case "05" :
                    bitmap = R.drawable.icon_05;
                    break;
                case "06" :
                    bitmap = R.drawable.icon_06;
                    break;

                case "11" :
                    bitmap = R.drawable.icon_07;
                    break;
                case "12" :
                    bitmap = R.drawable.icon_08;
                    break;
                case "13" :
                    bitmap = R.drawable.icon_09;
                    break;
                case "14" :
                    bitmap = R.drawable.icon_10;
                    break;
                case "15" :
                    bitmap = R.drawable.icon_11;
                    break;
                case "16" :
                    bitmap = R.drawable.icon_12;
                    break;

            }
//            bitmapTmp=bitmap;
            //Drawable ob = new BitmapDrawable(getResources(), bitmap);
//            imageTop.setImageBitmap(bitmapTmp);
            imageTop.setImageResource(bitmap);

            if(!ex.getString("haveImage").equals("Y")) {
                Random random = new Random();
                int xNum = random.nextInt(2) == 0 ? -1 : 1;
                int yNum = random.nextInt(2) == 0 ? -1 : 1;
                float xPos = 5.0f * xNum;
                float yPos = 3.0f * yNum;
                TranslateAnimation animation = new TranslateAnimation(0.0f, xPos, 0.0f, yPos);
                animation.setDuration(80);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(2);
                imageTop.startAnimation(animation);
            }
        }
    }

    private void initialTextView(Bundle ex){
        TextView specTextView = (TextView)findViewById(R.id.textView1);
        specTextView.setTypeface(customTypeFace);
        TextView sourceTextView = (TextView)findViewById(R.id.textView2);
        sourceTextView.setTypeface(customTypeFace);
        TextView destinationTextView = (TextView)findViewById(R.id.textView3);
        destinationTextView.setTypeface(customTypeFace);
        TextView shareTextView = (TextView)findViewById(R.id.textView4);
        shareTextView.setTypeface(customTypeFace);
        sourceTextView.setText("ต้นทาง");
        destinationTextView.setText("ปลายทาง");
        switch (ex.getString("type_selected")){

            case "01" : specTextView.setText("ทะเบียน"); strSpec = "ทะเบียน"; break;
            case "02" : specTextView.setText("สายรถเมล์"); strSpec = "สายรถเมล"; break;
            case "03" : specTextView.setText("เบอร์เสื้อวิน"); strSpec = "เบอร์เสื้อวิน"; break;
            case "04" : specTextView.setText("หมายเลขประจำรถ"); strSpec = "หมายเลขประจำรถ"; break;
            case "05" : specTextView.setText("หมายเลขประจำรถ"); strSpec = "หมายเลขประจำรถ"; break;
            case "06" : specTextView.setText("สายเรือ"); strSpec = "สายเรือ"; break;

            case "11" : specTextView.setVisibility(View.GONE); specEditText.setVisibility(View.GONE); haveSpec=false; break;
            case "12" : specTextView.setVisibility(View.GONE); specEditText.setVisibility(View.GONE); haveSpec=false; break;
            case "13" : specTextView.setText("เลขขบวนรถ"); strSpec = "เลขขบวนรถ"; break;
            case "14" : specTextView.setText("เลขทะเบียน"); strSpec = "เลขทะเบียน"; break;
            case "15" : specTextView.setText("สาย"); strSpec = "สาย"; break;
            case "16" : specTextView.setText("ขึ้นอะไร"); strSpec = "ขึ้นอะไร"; break;
        }
    }

    private void initialEditText(){
        specEditText = (EditText)findViewById(R.id.editText1);
        specEditText.setTypeface(customTypeFace);
        sourceEditText = (EditText)findViewById(R.id.editText2);
        sourceEditText.setTypeface(customTypeFace);
        destinationEditText = (EditText)findViewById(R.id.editText3);
        destinationEditText.setTypeface(customTypeFace);
    }

    private void initialButton(){
        Button facebookButton = (Button)findViewById(R.id.button1);
        Button twitterButton = (Button)findViewById(R.id.button2);
        ImageView search2 = (ImageView)findViewById(R.id.button8);
        ImageView search3 = (ImageView)findViewById(R.id.button9);
        Button back = (Button)findViewById(R.id.buttonBack);
        Button searchBg = (Button)findViewById(R.id.button_search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInFacebook();
            }
        });
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetInTwitter();
            }
        });

        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchId = 1;
                setSourceEditText(sourceEditText);
                getSlidingMenu().showMenu();

            }
        });

        search3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchId = 2;
                setDestinationEditText(destinationEditText);
                getSlidingMenu().showMenu();
            }
        });
    }

    protected void setOrientation() {
        int current = getRequestedOrientation();
        // only switch the orientation if not in portrait
        if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        }
    }

    private void tweetInTwitter(){
        String strTweet = "";
        if(haveSpec){
            strTweet+=strSpec+" "+specEditText.getText();
        }
        strTweet+=" ต้นทาง "+sourceEditText.getText();
        strTweet+=" ปลายทาง "+destinationEditText.getText();
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        String filename = null;
        try {
            filename = writeImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(filename!=null&&!filename.equals("")) {
            Uri phototUri = Uri.parse(filename);
            tweetIntent.setType("image/png");
            tweetIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
        }
        tweetIntent.putExtra(Intent.EXTRA_TEXT, strTweet);
        tweetIntent.setType("text/plain");
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);
        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivityForResult(tweetIntent, 2);
        }else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, strTweet);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=message&via=profileName"));
            startActivity(i);
            //Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String writeImage() throws IOException {
        if(bmpImage==null) return "";
        Date date = new Date();
        String name = date.getYear()+""+date.getMonth()+""+date.getDate()+""+date.getHours();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "/saferide_"+name+".jpg");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes.toByteArray());
        fileOutputStream.close();
        return file.getPath();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Intent sendIntent = new Intent(KeyData.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "2222");
            session.setFinish("1");
            KeyData.this.startActivity(sendIntent);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void shareInFacebook(){



        String strFacebook = "";
        if(haveSpec){
            strFacebook+=strSpec+" "+specEditText.getText()+" <br>";
        }
        strFacebook+=" ต้นทาง "+sourceEditText.getText()+"<br>";
        strFacebook+=" ปลายทาง "+destinationEditText.getText()+"<br>";

        strShow = strFacebook;

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Bitmap image = bmpImage;
                if (image == null) {
                    image = bitmapTmp;
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .setCaption(strShow)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    ShareApi.share(content, null);
                } else {
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .setCaption(strShow)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    ShareApi.share(content, null);
                }
                Log.e("hfadjl",""+loginResult.getAccessToken().getUserId());
                Intent sendIntent = new Intent(KeyData.this, CheckFinish.class);
                sendIntent.putExtra("idcheck", "1111");
                session.setFinish("1");
                startActivity(sendIntent);


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


}
