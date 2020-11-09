package com.traidev.masterCoder.Editor;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.akshay_naik.texthighlighterapi.TextHighlighter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.traidev.masterCoder.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.testica.codeeditor.Editor;
import me.testica.codeeditor.SyntaxHighlightRule;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditorFragment extends Fragment implements View.OnClickListener {


    private static final int STORAGE_PERMISSION_CODE = 123;
    Dialog myDialog;
    private DrawerLayout drawerLayout;


    public EditorFragment() {
        // Required empty public constructor
    }
    private ImageButton RunCode,AddFile;
    private String FileName = null;
    static final int READ_BLOCK_SIZE = 100;
    Spinner fileTypes;

    SharedPreferences sharedPreferences;


    private FrameLayout parentFrame;

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,t27,t28,t29;
    Editor editor;
    TextView tg1,tg2,tg3,tg4,tg5,tg6,tg7,tg8,tg9,tg10,tg11,tg12,tg13,tg14,tg15,tg16,tg17;
    ImageView up;

    TextHighlighter highlighter=new TextHighlighter();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.frag_code_editor, container, false);

        myDialog = new Dialog(getContext());

        parentFrame = getActivity().findViewById(R.id.reg_frame);
        RunCode = root.findViewById(R.id.runCode);

        AddFile = root.findViewById(R.id.addFile);
        editor = (Editor) root.findViewById(R.id.editor);


        requestStoragePermission();
        sharedPreferences = getActivity().getSharedPreferences("Files", Context.MODE_PRIVATE);



        if(isFirstTimeStartApp())
        {
            TapTargetView.showFor(getActivity(), TapTarget.forView(root.findViewById(R.id.runCode), "Run your Code in Emulator", "Run -  Html,CSS,JS,Jquery PHP any type of Front End Codes! ")
                    .outerCircleColor(R.color.codeBackground).outerCircleAlpha(.91f).transparentTarget(false));
            setFirstTimeStartStatus(false);
        }




        initViews(root);
        setclick();
        EditorLoad();

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            FileName = bundle.getString("fileOpen");
            OpenFile(FileName);
        }
        else
        {
            String f1 = sharedPreferences.getString("file1",null);
            String f2 = sharedPreferences.getString("file2",null);
            String f3 = sharedPreferences.getString("file3",null);
            String f4 = sharedPreferences.getString("file4",null);
            String f5 = sharedPreferences.getString("file5",null);


            if(f1 != null){  FileName =  sharedPreferences.getString("file1",null); }
            if(f2 != null){  FileName =  sharedPreferences.getString("file2",null); }
            if(f3 != null){  FileName =  sharedPreferences.getString("file3",null); }
            if(f4 != null){  FileName =  sharedPreferences.getString("file4",null); }
            if(f5 != null){  FileName =  sharedPreferences.getString("file5",null); }

            if(FileName == null)
            {

            }
            else
            {
                OpenFile(FileName);
            }

        }

        TextView fileName = root.findViewById(R.id.file_name_change);

        fileName.setText(FileName);

        RunCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 saveFile();
            }
        });

        AddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().setText("");
                FileName = null;
                fileName.setText("New File");
            }
        });

        return root;


    }

    private boolean isFirstTimeStartApp()
    {
        SharedPreferences ref = getActivity().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("RunFirst",true);
    }

    private void setFirstTimeStartStatus(boolean stt)
    {

        SharedPreferences ref = getActivity().getSharedPreferences("IntroSliderApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("RunFirst",stt);
        editor.commit();

    }


    public void OpenFile(String fileDataName)
    {

        try {

            File sdCard = Environment.getExternalStorageDirectory();

            File file = new File(sdCard.getAbsolutePath()+"/MCoder");

            File fileData = new File(file,fileDataName);

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

            editor.getEditText().setText(s);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setclick()
    {
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t10.setOnClickListener(this);
        t11.setOnClickListener(this);
        t12.setOnClickListener(this);
        t13.setOnClickListener(this);
        t14.setOnClickListener(this);
        t15.setOnClickListener(this);
        t16.setOnClickListener(this);
        t17.setOnClickListener(this);
        t18.setOnClickListener(this);
        t19.setOnClickListener(this);
        t20.setOnClickListener(this);
        t21.setOnClickListener(this);
        t22.setOnClickListener(this);
        t23.setOnClickListener(this);
        t24.setOnClickListener(this);
        t25.setOnClickListener(this);
        t26.setOnClickListener(this);
        t27.setOnClickListener(this);
        t28.setOnClickListener(this);
        t29.setOnClickListener(this);
        up.setOnClickListener(this);



        tg1.setOnClickListener(this);
        tg2.setOnClickListener(this);
        tg3.setOnClickListener(this);
        tg4.setOnClickListener(this);
        tg5.setOnClickListener(this);
        tg6.setOnClickListener(this);
        tg7.setOnClickListener(this);
        tg8.setOnClickListener(this);
        tg9.setOnClickListener(this);
        tg10.setOnClickListener(this);
        tg11.setOnClickListener(this);
        tg12.setOnClickListener(this);
        tg13.setOnClickListener(this);
        tg14.setOnClickListener(this);
        tg15.setOnClickListener(this);
        tg16.setOnClickListener(this);

    }

    public void initViews(View root)
    {

        up = root.findViewById(R.id.upMenu);
        t1 = root.findViewById(R.id.g1);
        t2 = root.findViewById(R.id.g2);
        t3 = root.findViewById(R.id.s1);
        t4 = root.findViewById(R.id.s2);
        t5 = root.findViewById(R.id.un);
        t6 = root.findViewById(R.id.q1);
        t7 = root.findViewById(R.id.q2);
        t8 = root.findViewById(R.id.c1);
        t9 = root.findViewById(R.id.c2);
        t10 = root.findViewById(R.id.equal);
        t11 = root.findViewById(R.id.b1);
        t12 = root.findViewById(R.id.b2);
        t13 = root.findViewById(R.id.hash);
        t14 = root.findViewById(R.id.co);
        t15 = root.findViewById(R.id.ad);
        t16 = root.findViewById(R.id.qu);
        t17 = root.findViewById(R.id.and);
        t18 = root.findViewById(R.id.ex);
        t19 = root.findViewById(R.id.k1);
        t20 = root.findViewById(R.id.k2);
        t21 = root.findViewById(R.id.m1);
        t22 = root.findViewById(R.id.m2);
        t23 = root.findViewById(R.id.plus);
        t24 = root.findViewById(R.id.dash);
        t25 = root.findViewById(R.id.dol);
        t26 = root.findViewById(R.id.per);
        t27 = root.findViewById(R.id.co);
        t28 = root.findViewById(R.id.danda);
        t29 = root.findViewById(R.id.enter);


        tg1 = root.findViewById(R.id.pT);
        tg2 = root.findViewById(R.id.aT);
        tg3 = root.findViewById(R.id.dT);
        tg4 = root.findViewById(R.id.imgT);
        tg5 = root.findViewById(R.id.tbT);
        tg6 = root.findViewById(R.id.tdT);
        tg7 = root.findViewById(R.id.trT);
        tg8 = root.findViewById(R.id.inputT);
        tg9 = root.findViewById(R.id.spT);
        tg10 = root.findViewById(R.id.btT);
        tg11 = root.findViewById(R.id.liT);
        tg12 = root.findViewById(R.id.ulT);
        tg13 = root.findViewById(R.id.scT);
        tg14 = root.findViewById(R.id.stT);
        tg15 = root.findViewById(R.id.boT);
        tg16 = root.findViewById(R.id.itT);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void EditorLoad()
    {

       editor.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        // changing text color and background color to number lines view
        editor.getNumLinesView().setBackgroundColor(Color.BLACK);
        editor.setBackgroundResource(R.color.codeBackground);
        editor.getNumLinesView().setTextColor(Color.parseColor("#FFD1D1D1"));
        editor.getNumLinesView().setBackgroundResource(R.color.numcodeBackground);
        editor.getEditText().setTextColor(Color.parseColor("#ffffff"));

        // applying left padding to code view
        editor.getEditText().setPadding(10, 20, 0, 0);
        editor.getNumLinesView().setTextSize(12);
        editor.getNumLinesView().setPadding(5,23,10,0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editor.getEditText().getText().insert(editor.getText().length()," ");
            }
        },100);

        editor.getEditText().setSyntaxHighlightRules(
                new SyntaxHighlightRule("<[^>]*>", "#FF0560"),
                new SyntaxHighlightRule("\\\\s*(?i)href\\\\s*=\\\\s*(\\\"([^\\\"]*\\\")|'[^']*'|([^'\\\">\\\\s]+))", "#FF3BD542"),
                new SyntaxHighlightRule("[<>{}/]","#FFF8F8F2"),
                new SyntaxHighlightRule("\\w+\\s*=\\s*\"[\\^ [a-z]\"]*\"", "#FFB300"),
                new SyntaxHighlightRule("\\<!--(.*?)\\-->", "#8C8C8C"),
                new SyntaxHighlightRule("\\/\\/*(.*?)\\*\\/", "#8C8C8C"),
                new SyntaxHighlightRule("\\<style>(.*?)\\<\\/style>", "#45A8FF")
        );
    }

    public void saveFile()
    {
        if (FileName == null) {
            ShowPopup();
        }
        else {

            String codeData = editor.getEditText().getText().toString();
            try {

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File sdCard = Environment.getExternalStorageDirectory();

                    File file = new File(sdCard.getAbsolutePath() + "/MCoder");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File fileData = new File(file, FileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(fileData,false);

                    fileOutputStream.write(codeData.getBytes());

                    fileOutputStream.flush();
                    fileOutputStream.close();

                } else {
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Snackbar.make(parentFrame, "Changes Saved", 5000)
                    .setActionTextColor(Color.GREEN).setAction("Great!",new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            }).show();
            setFragment(new BrowserFragment());
        }
    }



    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ShowPopup() {

        Button Cancel,Save;
        final EditText fileEdit;
        myDialog.setContentView(R.layout.file_create);
        Cancel =  myDialog.findViewById(R.id.cancel);
        fileTypes = myDialog.findViewById(R.id.fileTypesSpinner);
        fileEdit = myDialog.findViewById(R.id.fileName);
        Save =  myDialog.findViewById(R.id.save);

        List<String> categories = new ArrayList<String>();
        categories.add("html");
        categories.add("css");
        categories.add("js");
        categories.add("xml");
        categories.add("json");
        categories.add("php");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        fileTypes.setAdapter(dataAdapter);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileName = fileEdit.getText().toString()+"."+String.valueOf(fileTypes.getSelectedItem());
                SharedPreferences.Editor sharedData = sharedPreferences.edit();

                String f1 = sharedPreferences.getString("file1",null);
                String f2 = sharedPreferences.getString("file2",null);
                String f3 = sharedPreferences.getString("file3",null);
                String f4 = sharedPreferences.getString("file4",null);
                String f5 = sharedPreferences.getString("file5",null);

                if(f1 == null){sharedData.putString("file1",FileName); }
                else if(f2 == null){sharedData.putString("file2",FileName);}
                else if(f3 == null){sharedData.putString("file3",FileName);}
                else if(f4 == null){sharedData.putString("file4",FileName);}
                else if(f5 == null){sharedData.putString("file5",FileName);}

                sharedData.apply();

                saveFile();

                myDialog.dismiss();

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.upMenu:
                codeStruct();
                break;
            case R.id.g1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<");
                break;
            case R.id.g2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),">");
                break;
            case R.id.k1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"[");
                break;
            case R.id.k2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"]");
                break;
            case R.id.m1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"{");
                break;
            case R.id.m2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"}");
                break;
            case R.id.c1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),":");
                break;
            case R.id.c2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),";");
                break;
            case R.id.b1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"(");
                break;
            case R.id.b2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),")");
                break;
            case R.id.q1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"'");
                break;
            case R.id.q2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"\"");
                break;
            case R.id.co:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),",");
                break;
            case R.id.dot:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),".");
                break;
            case R.id.danda:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"|");
                break;
            case R.id.s1:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"/");
                break;
            case R.id.s2:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"\\");
                break;
            case R.id.equal:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"=");
                break;
            case R.id.dash:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"-");
                break;
            case R.id.plus:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"+");
                break;
            case R.id.un:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"_");
                break;
            case R.id.enter:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"\n");
                break;
            case R.id.ex:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"!");
                break;
            case R.id.ad:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"@");
                break;
            case R.id.qu:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"?");
                break;
            case R.id.hash:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"#");
                break;
            case R.id.dol:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"$");
                break;
            case R.id.and:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"&");
                break;
// HTML TAGS
            case R.id.pT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<p></p>");
                break;
            case R.id.aT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<a href=\"\">Link</a>");
                break;
            case R.id.dT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<div></div>");
                break;
            case R.id.imgT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<img src=\"\">");
                break;
            case R.id.tbT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<table>\n" +
                        "\t<tr>\n" +
                        "\t\t<th></th>\n" +
                        "\t\t<th></th>\n" +
                        "\t\t<th></th>\n" +
                        "\t</tr>\n" +
                        "\t<tr>\n" +
                        "\t\t<td></td>\n" +
                        "\t\t<td></td>\n" +
                        "\t\t<td></td>\n" +
                        "\t</tr>\n" +
                        "</table>");
                break;
            case R.id.tdT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<td></td>");
                break;
            case R.id.trT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<br>");
                break;
            case R.id.inputT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<input type=\"\" placeholder=\"\" name=\"\">");
                break;
            case R.id.spT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<span></span>");
                break;
            case R.id.btT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<button>Click Me</button>");
                break;
            case R.id.liT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<li></li>");
                break;
            case R.id.ulT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<ul>\n" +
                        "\t<li> </li>\n" +
                        "\t<li> </li>\n" +
                        "</ul>");
                break;
            case R.id.scT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<script type=\"text/javascript\"></script>");
                break;
            case R.id.stT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<style type=\"text/css\"></style>");
                break;
            case R.id.boT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<b></b>");
                break;
            case R.id.itT:
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),"<i></i>");
                break;
            default:
                break;
        }
    }

    public void codeStruct()
    {
        BottomSheetDialog codeAdd = new BottomSheetDialog(getContext());
        codeAdd.setContentView(R.layout.codestruct);
        codeAdd.setCanceledOnTouchOutside(false);

        TextView html,php,css,js,jquery,boot;

        html = codeAdd.findViewById(R.id.htmlAdd);
        php = codeAdd.findViewById(R.id.phpAdd);
        js = codeAdd.findViewById(R.id.jsAdd);
        css = codeAdd.findViewById(R.id.cssAdd);
        jquery = codeAdd.findViewById(R.id.jqAdd);
        boot = codeAdd.findViewById(R.id.bootAdd);

        html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "\t<title></title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>");

                codeAdd.dismiss();

            }
        });

        css.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<style type=\"text/css\">\n" +
                                "#id\n" +
                                "{\n" +
                                "\n" +
                                "}\n" +
                                "</style>");
                codeAdd.dismiss();
            }
        });


        js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<script type=\"text/javascript\">\n" +
                                "\n" +
                                "function showAlert()\n" +
                                "{\n" +
                                "\talert('hello');\n" +
                                "}\n" +
                                "\t\n" +
                                "</script>");
                codeAdd.dismiss();
            }
        });

        jquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n" +
                                "<script>\n" +
                                "$(document).ready(function(){\n" +
                                "  $(\"button\").click(function(){\n" +
                                "    $(\"p\").hide();\n" +
                                "  });\n" +
                                "});\n" +
                                "</script>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "<h2>This is a heading</h2>\n" +
                                "\n" +
                                "<p>This is a paragraph.</p>\n" +
                                "<p>This is another paragraph.</p>\n" +
                                "\n" +
                                "<button>Click me</button>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>\n");
                codeAdd.dismiss();
            }
        });

        boot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<!DOCTYPE html>\n" +
                                "<html lang=\"en\">\n" +
                                "<head>\n" +
                                "  <title>Bootstrap Example</title>\n" +
                                "  <meta charset=\"utf-8\">\n" +
                                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\">\n" +
                                "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n" +
                                "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js\"></script>\n" +
                                "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js\"></script>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "<div class=\"jumbotron text-center\">\n" +
                                "  <h1>My First Bootstrap Page</h1>\n" +
                                "  <p>Resize this responsive page to see the effect!</p> \n" +
                                "</div>\n" +
                                "  \n" +
                                "<div class=\"container\">\n" +
                                "  <div class=\"row\">\n" +
                                "    <div class=\"col-sm-4\">\n" +
                                "      <h3>Column 1</h3>\n" +
                                "      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>\n" +
                                "      <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris...</p>\n" +
                                "    </div>\n" +
                                "    <div class=\"col-sm-4\">\n" +
                                "      <h3>Column 2</h3>\n" +
                                "      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>\n" +
                                "      <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris...</p>\n" +
                                "    </div>\n" +
                                "    <div class=\"col-sm-4\">\n" +
                                "      <h3>Column 3</h3>        \n" +
                                "      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>\n" +
                                "      <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris...</p>\n" +
                                "    </div>\n" +
                                "  </div>\n" +
                                "</div>\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>\n");
                codeAdd.dismiss();
            }
        });


        php.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.getEditText().getText().insert(editor.getEditText().getSelectionStart(),
                        "\n<?php \n" +
                                "\n" +
                                "echo /* enter your code here */;\n" +
                                "\n" +
                                "?>");
                codeAdd.dismiss();
            }
        });

        codeAdd.show();

    }

    private void setFragment(Fragment fragment)
    {
        Bundle bundle = new Bundle();
        bundle.putString("fileName", FileName);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrame.getId(),fragment);
        fragmentTransaction.commit();
    }



}
