package com.digitopolis.saferide;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SlidingMenuNew extends SlidingActivity {

    EditText textSearch;
    private String jsonResult;
    int searchId;
    ListView listView;
    private List <String> list ;
    List<String> urls;
    private List <String> listTmp ;
    List<Bitmap> bitmaps;
    HashMap<String,Bitmap> hashMapList;
    Typeface customTypeFace ;
    private String clientId = "ZZT2004BOSREUUENKHVCFTIOGH33RXWLQBZ0HNYY2PN0UVXJ";
    private String clientSecret = "H25JI4LKFWLSE35MXULRH2AK5Z5MD2UDFVHPA3BZBYBHGYJO";
    String ll = "13.754409,100.535549";
    private EditText sourceEditText;
    private EditText destinationEditText;

    public void startWorking(){
        urls = new ArrayList<>();
        list = new ArrayList<>();
        hashMapList = new HashMap<>();
        bitmaps = new ArrayList<>();
        ActorAsynkTask aat = new ActorAsynkTask();
        aat.execute();
    }

    public void setSourceEditText(EditText sourceEditText){
        this.sourceEditText = sourceEditText;
    }

    public void setDestinationEditText(EditText destinationEditText){
        this.destinationEditText = destinationEditText;
    }

    private void initailList() {
        ListViewRow adapter = new ListViewRow(getApplicationContext(), list, urls);
        listView = (ListView) this.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchId == 1) {
                    sourceEditText.setText(list.get((int) position));
                } else if (searchId == 2) {
                    destinationEditText.setText(list.get((int) position));
                } else {

                }

                getSlidingMenu().showContent(true);
                searchId = 0;
            }
        });
    }



    private void initailEventMenu(){
        textSearch = (EditText)getSlidingMenu().findViewById(R.id.editTextSearch);
        textSearch.setTypeface(customTypeFace);
        textSearch.setTextSize(18);
        textSearch.setTextColor(Color.parseColor("#3B2314"));
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

    private String getJsonResult(){
        try {
            String urlStr = "https://api.foursquare.com/v2/venues/search?client_id=" + clientId + "&client_secret=" + clientSecret + "&v=20150608&ll="+ll+"&radius=2000&limit=50";
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            jsonResult = response.toString();
            JSONObject object = new JSONObject(jsonResult);
            JSONObject tmp = new JSONObject(object.getString("meta"));
            if(tmp.getString("code").equals("200")) {
                JSONObject responseJson = new JSONObject(object.getString("response"));
                JSONArray venueArrayJson = new JSONArray(responseJson.getString("venues"));
                for (int i = 0; i < venueArrayJson.length(); i++) {
                    JSONObject trueObject = new JSONObject(venueArrayJson.getString(i));
                    String show =  trueObject.getString("name")+" ";
                    String urlImage = "";
                    if(!trueObject.getString("categories").equals("[]")){
                        JSONArray categoriesArray = new JSONArray(trueObject.getString("categories")+"");
                        JSONObject categorieJson = new JSONObject(categoriesArray.getString(0));
                        JSONObject iconJson = new JSONObject(categorieJson.getString("icon"));
                        urlImage = iconJson.getString("prefix");
                        urlImage.replace("ss3.4sqi.net","foursquare.com");
                        String urlTmp = urlImage.substring(20);
                        urlTmp = "https://foursquare.com"+urlTmp;
                        urlImage =urlTmp;
                        urlImage+="bg_32"+iconJson.getString("suffix");
                        urls.add(urlImage);
                    }
                    list.add(show);
                }
                listTmp = list;
            }
            in.close();

            urlConnection.disconnect();
        }
        catch (MalformedURLException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
        }
        catch (IOException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
        }
       catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonResult;
    }

    public class ActorAsynkTask extends AsyncTask<String ,Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return getJsonResult();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String Result) {
            try{
                initailList();
                initailEventMenu();
            }catch(Exception E){
                Toast.makeText(getApplicationContext(), "Error:"+E.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
