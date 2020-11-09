package com.traidev.masterCoder.Editor;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.traidev.masterCoder.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrowserFragment extends Fragment {

    WebView webView;
    ProgressBar pbar;
    private ImageButton RunCode,BackCode;
    static final int READ_BLOCK_SIZE = 100;
    private FrameLayout parentFrame;
    private String FileName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_web_browser, container, false);

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        RunCode = root.findViewById(R.id.refreshCode);
        BackCode = root.findViewById(R.id.backCode);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
             FileName = bundle.getString("fileName");
        }

        pbar = (ProgressBar)root.findViewById(R.id.progressBar1);
        webView = (WebView)root.findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        fetchData();
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                webView.loadUrl("file:///android_asset/error.html");
            }
            public  void  onPageFinished(WebView view, String url){

                //Hide the SwipeReefreshLayout
                pbar.setVisibility(View.GONE);
            }

        });

        BackCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
            }
        });

        RunCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
                Toast.makeText(getContext(),"Refreshed!",Toast.LENGTH_SHORT).show();
            }
        });

        return root;

    }

    private void setFragment()
    {

        Fragment fragment = new EditorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fileOpen", FileName);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_form_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.commit();
    }


    public void fetchData()
    {
        try {

            File sdCard = Environment.getExternalStorageDirectory();

            File file = new File(sdCard.getAbsolutePath()+"/MCoder");

            File fileData = new File(file,FileName);

            FileInputStream inputStream = new FileInputStream(fileData);

            InputStreamReader inputStreamReader  = new InputStreamReader(inputStream);

            char[] iChar = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;
            while((charRead = inputStreamReader.read(iChar)) > 0)
            {
                String readString = String.copyValueOf(iChar,0,charRead);
                s+=readString;
                iChar = new char[READ_BLOCK_SIZE];
            }

            webView.loadUrl("file:///"+sdCard.getAbsolutePath()+"/MCoder/"+FileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
