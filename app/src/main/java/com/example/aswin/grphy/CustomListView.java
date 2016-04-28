package com.example.aswin.grphy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> url_tag;
    private final ArrayList<String> url_value;
    private final Integer [] image1,image2;

    public CustomListView(Activity context, ArrayList<String> url_tag, ArrayList<String> url_value,
                          Integer [] image1, Integer [] image2) {
        super(context, R.layout.custom_listview,url_tag);
        this.context = context;
        this.url_tag = url_tag;
        this.url_value = url_value;
        this.image1 = image1;
        this.image2 = image2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_listview, null, true);

        TextView textview1 = (TextView)rowView.findViewById(R.id.tv_url_tag);
        TextView textView2 = (TextView)rowView.findViewById(R.id.tv_url_value);
        ImageView imageView1 = (ImageView)rowView.findViewById(R.id.imageview_left);
        ImageView imageView2 = (ImageView)rowView.findViewById(R.id.imageview_right);


        textview1.setText(url_tag.get(position));
        textView2.setText(url_value.get(position));
        Random generator = new Random();
        int randomIndex = generator.nextInt(image1.length);
        imageView1.setImageResource(image1[randomIndex]);
        imageView2.setImageResource(image2[0]);

        return rowView;
    }
}
