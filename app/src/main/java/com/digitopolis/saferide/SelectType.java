package com.digitopolis.saferide;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by Bank on 8/6/2015.
 */
public class SelectType extends Activity {

    private String selectString = "00";
    private static final int CAMERA_REQUEST = 1888;
    private Bitmap cameraImage = null;
    private String haveImage = "N";
    Typeface customTypeFace;
    private Session session;
    private Uri picUri;
    final int PIC_CROP = 2;

    @Override
    protected void onResume() {
        if(session.getFinish().equals("1")){
            Intent sendIntent = new Intent(SelectType.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            SelectType.this.startActivity(sendIntent);
        }
        else{
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_type);

        customTypeFace = Typeface.createFromAsset(getAssets(), "fonts/aaa 2005_iannnnnHBO.ttf");
        session = new Session(getBaseContext());
        if(session.getFinish().equals("1")){
            Intent sendIntent = new Intent(SelectType.this, CheckFinish.class);
            sendIntent.putExtra("idcheck", "1111");
            session.setFinish("1");
            SelectType.this.startActivity(sendIntent);
        }
        else{
        }

        getExtraData();
        initialButton();
        initailText();
        setOrientation();

    }

    private void initailText(){
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setTextSize(32);
        tv.setTypeface(customTypeFace);
    }

    public void getExtraData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectString = extras.getString("type_selected");
        }
    }

    public void performCrop(){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 235);
        cropIntent.putExtra("outputY", 235);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, PIC_CROP);
    }


    Uri mCapturedImageURI;
    public void initialButton(){
        ImageView cameraButton = (ImageView)findViewById(R.id.button1);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        ImageView keyDataButton = (ImageView)findViewById(R.id.button2);
        keyDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(SelectType.this, KeyData.class);
                sendIntent.putExtra("haveImage", "N");
                sendIntent.putExtra("type_selected", selectString);
                startActivity(sendIntent);
            }
        });

        Button backButton = (Button)findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(Activity.RESULT_OK);
                onBackPressed();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //data.getExtras().get("data");

            picUri = mCapturedImageURI;

           // Log.e("IMAGE",""+picUri);
            //Uri selectedImageUri = data.getData();
           /// selectedImagePath = getRealPathFromURI(selectedImageUri);

            try {
                performCrop();
            }
            catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }


        }
        if (requestCode == PIC_CROP) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            cameraImage = photo;
            Intent sendIntent = new Intent(SelectType.this, KeyData.class);
            sendIntent.putExtra("bitmapImage", cameraImage);
            sendIntent.putExtra("haveImage", "Y");
            sendIntent.putExtra("type_selected", selectString);
            startActivity(sendIntent);
        }
    }

}
