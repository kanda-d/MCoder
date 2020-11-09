package com.traidev.masterCoder.Youtube;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.traidev.masterCoder.BuildConfig;
import com.traidev.masterCoder.Editor.CoderActivity;
import com.traidev.masterCoder.Editor.EditorFragment;
import com.traidev.masterCoder.HomActivity;
import com.traidev.masterCoder.Main_Interface;
import com.traidev.masterCoder.R;
import com.traidev.masterCoder.RetrofitClient;
import com.traidev.masterCoder.Youtube.adapters.RecylerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosPlaylist extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<UpdateModal> videos;
    private RecylerAdapter adapter;
    private Main_Interface main_interface;
    private static String ID,Title = "";
    private ProgressBar progressBar;
    DrawerLayout drawer;
    TextView  Editor,Yt,Setting,Rating,Share,Tut;

    private FrameLayout FramChange;

    LinearLayout layout;
    static int CODE_FOR_RESULT=981;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_playlist);


        layout =  findViewById(R.id.shareLinear);

        recyclerView = (RecyclerView) findViewById(R.id.mList_VideoPlaylist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        progressBar = (ProgressBar) findViewById(R.id.progressBar6);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Files", Context.MODE_PRIVATE);


        Editor = findViewById(R.id.edit_nav);
        Yt = findViewById(R.id.yt_nav);
        Setting = findViewById(R.id.setting_nav);
        Rating = findViewById(R.id.rating_nav);
        Share = findViewById(R.id.share_nav);
        Tut = findViewById(R.id.codeTutorials);

        Editor.setOnClickListener(this);
        Yt.setOnClickListener(this);
        Setting.setOnClickListener(this);
        Rating.setOnClickListener(this);
        Share.setOnClickListener(this);
        Tut.setOnClickListener(this);

        FramChange = findViewById(R.id.reg_frame);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ID= null;
            } else {
                ID= extras.getString("PlaylistId");
                Title= extras.getString("Title");
            }
        }
        fetchInfo();
    }



    public void fetchInfo()
    {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> data = new HashMap<>();
        data.put("playlistId", ID);


        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<UpdateModal>> call = main_interface.getVideos();

        call.enqueue(new Callback<List<UpdateModal>>() {
            @Override
            public void onResponse(Call<List<UpdateModal>> call, Response<List<UpdateModal>> response) {

                videos = response.body();
                adapter = new RecylerAdapter(videos,getApplicationContext(),12);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<List<UpdateModal>> call, Throwable t) {

            }
        });
    }

    public void OpenBox(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_nav:
                startActivity(new Intent(this, CoderActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.yt_nav:
                startActivity(new Intent(this, VideosPlaylist.class));
                break;
            case R.id.code_nav:
                Snackbar.make(drawer, "Coming Soon", 5000)
                        .setActionTextColor(Color.RED).setAction("Any Query",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Mail to k.developer.x@gmail.com",Toast.LENGTH_LONG).show();
                    }
                }).show();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.share_nav:
                OnClickShare(v,layout,"all");
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.setting_nav:
                Snackbar.make(drawer, "Coming Soon", 5000)
                        .setActionTextColor(Color.RED).setAction("Any Query",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Mail to k.developer.x@gmail.com",Toast.LENGTH_LONG).show();
                    }
                }).show();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.codeTutorials:
                startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.youtube.com/watch?v=UhyuDctUrYs")));
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.rating_nav:
                RateUs();
                break;
            default:
                break;
        }

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(FramChange.getId(),fragment);
        fragmentTransaction.commit();
    }

    public void OnClickShare(View view , LinearLayout linear, String social){

        Bitmap bitmap =getBitmapFromView(linear);
        try {
            File file = new File(getExternalCacheDir(),"logicchip.png");
            // File file = new File(this.getCacheDir(),File.separator+ "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(getApplication(), BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            if(social != "all")
            {
                intent.setPackage(social);
            }
            intent.putExtra(Intent.EXTRA_TEXT,
                    "Master Coder - Code on Bed \uD83C\uDFA7 \n" +
                            "\nA text and source code editor for android tablets and phones with many Features.\n" +
                            "\nCheckout Master Coder App at: https://play.google.com/store/apps/details?id="+getPackageName());
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpeg");

            //startActivity(Intent.createChooser(intent, "Share image via"));
            startActivityForResult(Intent.createChooser(intent, "Share Post via"),CODE_FOR_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    private void RateUs() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }



}
