package com.game.prezes.game;

/**
 * Created by micha on 18.02.2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] name;
    private final String[] score;
    private final String[] picture;
    public CustomList(Activity context,
                      String[] name, String[] score,String[] picture) {
        super(context, R.layout.result, name);
        this.context = context;
        this.name = name;
        this.score = score;
        this.picture=picture;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.result, null, true);
        TextView nameTitle = (TextView) rowView.findViewById(R.id.name);


        nameTitle.setText(name[position]);
        TextView scoreTitle = (TextView) rowView.findViewById(R.id.result);


        scoreTitle.setText(score[position]);
        ProfilePictureView profile=(ProfilePictureView)rowView.findViewById(R.id.fbpicture);
        profile.setProfileId(picture[position]);

        return rowView;
    }
}
