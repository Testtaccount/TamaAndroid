package com.tama.chat.ui.adapters.mapBlink;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
//chka
public class SimpleImageArrayAdapter extends ArrayAdapter<Integer> {
    private Integer[] images;
    private int height, padding;

    public SimpleImageArrayAdapter(Context context, Integer[] images) {
        super(context, android.R.layout.simple_spinner_item, images);
        height = convertDpToPixel(40,context);
        padding = height/8;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(images[position]);
        imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        imageView.setPadding(padding,padding,padding,padding);
        return imageView;
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}
