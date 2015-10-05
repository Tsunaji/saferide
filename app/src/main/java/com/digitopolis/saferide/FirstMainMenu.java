package com.digitopolis.saferide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bank on 8/6/2015.
 */
public class FirstMainMenu extends Fragment{

    GridView gridView;
    List<ModeView> modeViewList ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View firstPage = inflater.inflate(R.layout.page_frist,container,false);

        modeViewList = new ArrayList<>();

        setupImage();
        setupGrid(firstPage);

        return firstPage;
    }

    private void setupGrid(View firstPage){
        ModeViewLayout adapter = new ModeViewLayout(firstPage.getContext(), modeViewList,0);
        gridView=(GridView)firstPage.findViewById(R.id.gridview1);
        gridView.setAdapter(adapter);
    }

    private void setupImage(){
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_01);
        ModeView modeView1 = new ModeView();
        modeView1.setBitmap(icon1);
        modeView1.setName("แท็กซี่");
        modeViewList.add(modeView1);

        Bitmap icon2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_02);
        ModeView modeView2 = new ModeView();
        modeView2.setBitmap(icon2);
        modeView2.setName("รถเมล์");
        modeViewList.add(modeView2);

        Bitmap icon3 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_03);
        ModeView modeView3 = new ModeView();
        modeView3.setBitmap(icon3);
        modeView3.setName("มอเตอร์ไซด์");
        modeViewList.add(modeView3);

        Bitmap icon4 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_04);
        ModeView modeView4 = new ModeView();
        modeView4.setBitmap(icon4);
        modeView4.setName("รถตู้");
        modeViewList.add(modeView4);

        Bitmap icon5 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_05);
        ModeView modeView5 = new ModeView();
        modeView5.setBitmap(icon5);
        modeView5.setName("ตุ๊กตุ๊ก");
        modeViewList.add(modeView5);

        Bitmap icon6 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_06);
        ModeView modeView6 = new ModeView();
        modeView6.setBitmap(icon6);
        modeView6.setName("เรื่อโดยสาร");
        modeViewList.add(modeView6);

    }
}
