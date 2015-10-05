package com.digitopolis.saferide;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bank on 8/14/2015.
 */
public class CheckFinish extends Activity{
    ImageView starButton1 ;
    ImageView starButton2 ;
    ImageView starButton3 ;
    ImageView starButton4 ;
    ImageView starButton5 ;
    ImageView finishButton;
    Typeface customTypeFace;
    String code = "";
    Session session;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_finish);

        customTypeFace = Typeface.createFromAsset(getAssets(), "fonts/aaa 2005_iannnnnHBO.ttf");
        session = new Session(getBaseContext());
        Intent intent = getIntent();
        code = intent.getStringExtra("idcheck");

        setOrientation();
        initailButton();
        initailTextView();
        initailEditText();
    }
    protected void setOrientation() {
        int current = getRequestedOrientation();
        if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
            setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        }
    }

    private void initailTextView(){
        TextView scoreTv = (TextView)findViewById(R.id.textViewScore);
        scoreTv.setTypeface(customTypeFace);
    }

    private void initailEditText(){
        EditText commentEt = (EditText)findViewById(R.id.comment);
        commentEt.setTypeface(customTypeFace);
    }

    private void initailDefaultImage(){
        starButton1.setImageResource(R.drawable.star_01);
        starButton2.setImageResource(R.drawable.star_01);
        starButton3.setImageResource(R.drawable.star_01);
        starButton4.setImageResource(R.drawable.star_01);
        starButton5.setImageResource(R.drawable.star_01);
    }

    private void initailButton(){
        starButton1 = (ImageView)findViewById(R.id.button1);
        starButton2 = (ImageView)findViewById(R.id.button2);
        starButton3 = (ImageView)findViewById(R.id.button3);
        starButton4 = (ImageView)findViewById(R.id.button4);
        starButton5 = (ImageView)findViewById(R.id.button5);
        finishButton = (ImageView)findViewById(R.id.finishButton);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.equals("1111"))
                    shareInFacebook();
                else
                    tweetInTwitter();
            }
        });
        starButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initailDefaultImage();
                starButton1.setImageResource(R.drawable.star_02);
            }
        });
        starButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initailDefaultImage();
                starButton1.setImageResource(R.drawable.star_02);
                starButton2.setImageResource(R.drawable.star_02);
            }
        });
        starButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initailDefaultImage();
                starButton1.setImageResource(R.drawable.star_02);
                starButton2.setImageResource(R.drawable.star_02);
                starButton3.setImageResource(R.drawable.star_02);
            }
        });
        starButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initailDefaultImage();
                starButton1.setImageResource(R.drawable.star_02);
                starButton2.setImageResource(R.drawable.star_02);
                starButton3.setImageResource(R.drawable.star_02);
                starButton4.setImageResource(R.drawable.star_02);
            }
        });
        starButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initailDefaultImage();
                starButton1.setImageResource(R.drawable.star_02);
                starButton2.setImageResource(R.drawable.star_02);
                starButton3.setImageResource(R.drawable.star_02);
                starButton4.setImageResource(R.drawable.star_02);
                starButton5.setImageResource(R.drawable.star_02);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Intent sendIntent = new Intent(CheckFinish.this, MainActivity.class);
            session.setFinish("0");
            finish();
            CheckFinish.this.startActivity(sendIntent);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void tweetInTwitter(){
        String strTweet = "";

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        String filename = null;

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

    private void shareInFacebook(){

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {


                SharePhoto photo = new SharePhoto.Builder()
                            .setCaption("ถึงแล้วนะ!!")
                            .build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                ShareApi.share(content, null);
                Intent sendIntent = new Intent(CheckFinish.this, MainActivity.class);
                session.setFinish("0");
                finish();
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
