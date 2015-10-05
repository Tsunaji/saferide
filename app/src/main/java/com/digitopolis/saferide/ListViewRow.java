package com.digitopolis.saferide;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bank on 8/24/2015.
 */
public class ListViewRow extends BaseAdapter{

    Context mContext;
    List<String> strName;
    List<String> bitmapUrl;
    Typeface customTypeFace ;


    public ListViewRow(Context context, List<String> strName, List<String> bitmapUrl) {
        this.mContext= context;
        this.strName = strName;
        this.bitmapUrl = bitmapUrl;
        customTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/aaa 2005_iannnnnHBO.ttf");
    }

    public int getCount() {
        return strName.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.listview_row, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.textView1);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(strName.get(position));
        if (!holder.chk&&holder.imageView != null&&bitmapUrl.size() > position) {
            new ImageDownloaderTask(holder.imageView).execute(bitmapUrl.get(position));
            holder.chk = true;
        }
        return view;
    }

    static class ViewHolder {
        boolean chk = false;
        TextView name;
        ImageView imageView;
    }

}

