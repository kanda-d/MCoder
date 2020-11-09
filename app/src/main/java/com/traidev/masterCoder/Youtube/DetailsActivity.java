package com.traidev.masterCoder.Youtube;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.traidev.masterCoder.R;

public class DetailsActivity extends AppCompatActivity {
    public static String VIDEO_ID = "";
    public static String VIDEO_TITLE = "";

    private TextView title;
    TextView txt_help_gest;
    ImageView expand,fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                VIDEO_ID= null;
            } else {
                VIDEO_ID= extras.getString("VideoId");
                VIDEO_TITLE = extras.getString("VideoTitle");


            }
        } else {
            VIDEO_ID= (String) savedInstanceState.getSerializable("VideoId");
            VIDEO_TITLE= (String) savedInstanceState.getSerializable("VideoTitle");

        }

        title = (TextView)findViewById(R.id.textViewTitle);
        title.setText(VIDEO_TITLE);


        txt_help_gest = (TextView) findViewById(R.id.txt_help_gest);
        expand = (ImageView) findViewById(R.id.expand);
        fav = (ImageView) findViewById(R.id.fav);
        // hide until its title is clicked
        txt_help_gest.setVisibility(View.VISIBLE);


    }

    public void share_btn_pressed(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String link = ("https://www.youtube.com/watch?v=" + VIDEO_ID);
        // this is the text that will be shared
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, VIDEO_TITLE
                + "Share");

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "share"));


    }



    private ProgressDialog pDialog;


    public void toggle_contents(View view) {


        if(txt_help_gest.isShown())
        {
            expand.setImageResource(R.drawable.down);
            txt_help_gest.setVisibility(View.GONE);
        }
        else
        {
            expand.setImageResource(R.drawable.up);
            txt_help_gest.setVisibility(View.VISIBLE);
        }

    }

    public void like_press(View view) {
        fav.setImageResource(R.drawable.favroit);

        startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.youtube.com/watch?v="+VIDEO_ID)));

    }

    public void downloadNow(View view) {
        Toast.makeText(this,"Offline feature Coming Soon!",Toast.LENGTH_LONG).show();
    }
}



