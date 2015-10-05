package com.digitopolis.saferide;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.share.model.AppInviteContent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper;

public class SlidingMenuImp extends AppCompatActivity implements SlidingActivityBase {
    private SlidingActivityHelper mHelper;

    public SlidingMenuImp() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHelper = new SlidingActivityHelper(this);
        this.mHelper.onCreate(savedInstanceState);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mHelper.onPostCreate(savedInstanceState);
    }

    public View findViewById(int id) {
        View v = super.findViewById(id);
        return v != null?v:this.mHelper.findViewById(id);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mHelper.onSaveInstanceState(outState);
    }

    public void setContentView(int id) {
        this.setContentView(this.getLayoutInflater().inflate(id, (ViewGroup)null));
    }

    public void setContentView(View v) {
        this.setContentView(v, new LayoutParams(-1, -1));
    }

    public void setContentView(View v, LayoutParams params) {
        super.setContentView(v, params);
        this.mHelper.registerAboveContentView(v, params);
    }

    public void setBehindContentView(int id) {
        this.setBehindContentView(this.getLayoutInflater().inflate(id, (ViewGroup)null));
    }

    public void setBehindContentView(View v) {
        this.setBehindContentView(v, new LayoutParams(-1, -1));
    }

    public void setBehindContentView(View v, LayoutParams params) {
        this.mHelper.setBehindContentView(v, params);
    }

    public SlidingMenu getSlidingMenu() {
        return this.mHelper.getSlidingMenu();
    }

    public void toggle() {
        this.mHelper.toggle();
    }

    public void showContent() {
        this.mHelper.showContent();
    }

    public void showMenu() {
        this.mHelper.showMenu();
    }

    public void showSecondaryMenu() {
        this.mHelper.showSecondaryMenu();
    }

    public void setSlidingActionBarEnabled(boolean b) {
        this.mHelper.setSlidingActionBarEnabled(b);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean b = this.mHelper.onKeyUp(keyCode, event);
        return b?b:super.onKeyUp(keyCode, event);
    }
}
