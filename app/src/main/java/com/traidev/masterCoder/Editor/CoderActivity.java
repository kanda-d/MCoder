package com.traidev.masterCoder.Editor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.traidev.masterCoder.BuildConfig;
import com.traidev.masterCoder.DefaultResponse;
import com.traidev.masterCoder.HomActivity;
import com.traidev.masterCoder.IntroSlider.WelcomeActivity;
import com.traidev.masterCoder.R;
import com.traidev.masterCoder.RetrofitClient;
import com.traidev.masterCoder.Youtube.VideosPlaylist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoderActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    TextView fl1,fl2,fl3,fl4,fl5, Editor,Yt,Setting,Rating,Share,Tut;
    String f1,f2,f3,f4,f5;
    LinearLayout layout;
    Dialog myDialog;

    static int CODE_FOR_RESULT=981;

    private FrameLayout FramChange;
    public static boolean onResetFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isFirstTimeStartApp())
        {
            startMainActivity();
            finish();
        }
        else
        {
            CheckVersion();
        }


        myDialog = new Dialog(CoderActivity.this);




        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Files", Context.MODE_PRIVATE);

        layout =  findViewById(R.id.shareLinear);
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

        fl1 = findViewById(R.id.f1);
        fl2 = findViewById(R.id.f2);
        fl3 = findViewById(R.id.f3);
        fl4 = findViewById(R.id.f4);
        fl5 = findViewById(R.id.f5);

         f1 = sharedPreferences.getString("file1",null);
         f2 = sharedPreferences.getString("file2",null);
         f3 = sharedPreferences.getString("file3",null);
         f4 = sharedPreferences.getString("file4",null);
         f5 = sharedPreferences.getString("file5",null);

        if(f1 != null){fl1.setVisibility(View.VISIBLE);fl1.setText("⦿ "+f1); }
        if(f2 != null){fl2.setVisibility(View.VISIBLE);fl2.setText("⦿ "+f2); }
        if(f3 != null){fl3.setVisibility(View.VISIBLE);fl3.setText("⦿ "+f3); }
        if(f4 != null){fl4.setVisibility(View.VISIBLE);fl4.setText("⦿ "+f4); }
        if(f5 != null){fl5.setVisibility(View.VISIBLE);fl5.setText("⦿ "+f5); }


        fl1.setOnClickListener(this);
        fl2.setOnClickListener(this);
        fl3.setOnClickListener(this);
        fl4.setOnClickListener(this);
        fl5.setOnClickListener(this);



        setDefaultFragment(new EditorFragment());

         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }

    private void startMainActivity()
    {
        setFirstTimeStartStatus(false);
        startActivity(new Intent(CoderActivity.this, WelcomeActivity.class));
        finish();

    }


    private boolean isFirstTimeStartApp()
    {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag",true);
    }

    private void setFirstTimeStartStatus(boolean stt)
    {
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag",stt);
        editor.commit();
    }


    private void CheckVersion() {
        String versionName = null;

        try {
             versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().getUpdate(versionName);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if(response.code() == 201)
                {

                    String data = dr.getMessage();
                    String[] dif = data.split("#");

                    Button Update;
                    TextView ver,updates;
                    myDialog.setContentView(R.layout.update_dialog);
                    Update =  myDialog.findViewById(R.id.updateButton);
                    updates = myDialog.findViewById(R.id.textUpdate);
                    ver = myDialog.findViewById(R.id.versionCode);

                    ver.setText(dif[0]);
                    updates.setText(dif[1]);

                    Update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String appName = getPackageName();

                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                            }

                            myDialog.dismiss();
                        }
                    });
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                }
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hom, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(onResetFragment == true){
                onResetFragment = false;
                setFragment(new EditorFragment());
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }

    private void setDefaultFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(FramChange.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(FramChange.getId(),fragment);
        fragmentTransaction.commit();
    }

    public void openDrawer(View view) {
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
            case R.id.share_nav:
                OnClickShare(v, layout,"all");
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
            case R.id.rating_nav:
                RateUs();
                break;
            case R.id.f1:
                openFile(f1);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.f2:
                openFile(f2);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.f3:
                openFile(f3);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.f4:
                openFile(f4);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.f5:
                openFile(f5);
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.codeTutorials:
                startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.youtube.com/watch?v=UhyuDctUrYs")));
                drawer.closeDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }

    }
    private void openFile(String file)
    {
        Fragment fragment = new EditorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fileOpen", file);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
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
            Uri photoURI = FileProvider.getUriForFile(getApplication(),BuildConfig.APPLICATION_ID + ".provider", file);
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
