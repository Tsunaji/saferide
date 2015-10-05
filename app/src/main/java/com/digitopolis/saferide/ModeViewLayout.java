package com.digitopolis.saferide;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by Bank on 9/3/2015.
 */
public class ModeViewLayout extends BaseAdapter {

    private Context mContext;
    List<ModeView> modeViewList;
    int page = 0;
    TranslateAnimation animation ;
   // int positionNumber ;

    public ModeViewLayout(Context c,List<ModeView> modeViewList,int pageNumber) {
        mContext = c;
       this.modeViewList = modeViewList;
        this.page = pageNumber;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return modeViewList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //positionNumber = position;
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.mode_view, null);
            TextView textView = (TextView) grid.findViewById(R.id.textView);
            final ImageView imageView = (ImageView)grid.findViewById(R.id.imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                int pageNumber = 0;
                int positionNumber = 0;

                @Override
                public void onClick(View v) {
                    this.positionNumber = position;
                    this.pageNumber = page;
                    Intent sendIntent = new Intent(v.getContext(), SelectType.class);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra("type_selected", pageNumber + "" + (positionNumber + 1));
                    v.getContext().startActivity(sendIntent);
                }
            });

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        imageView.clearAnimation();
                        animateImage(imageView);
                    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP||
                            event.getAction() == MotionEvent.ACTION_CANCEL) {
                        imageView.clearAnimation();
                    }
                    return false;
                }
            });


            textView.setText(modeViewList.get(position).getName());
            imageView.setImageBitmap(modeViewList.get(position).getBitmap());
          // animateImage(imageView);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    public void animateImage(ImageView imageView){
        Random random = new Random();
        int xNum = random.nextInt(2)==0?-1:1;
        int yNum = random.nextInt(2)==0?-1:1;
        float xPos =  5.0f*xNum;
        float yPos =  3.0f*yNum;
        animation = new TranslateAnimation(0.0f, xPos,0.0f, yPos);
        animation.setDuration(30);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(2);
        imageView.startAnimation(animation);
    }
}
