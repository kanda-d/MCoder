package com.traidev.masterCoder.Youtube;


import com.google.gson.annotations.SerializedName;

public class UpdateModal {

    @SerializedName("title")
    private String Title;

    @SerializedName("thumb")
    private String Thumbnil;

    @SerializedName("vId")
    private String Video;


    @SerializedName("about")
    private String About;



    public String getTitle() {
        return Title;
    }

    public String getThumbnil() {
        return Thumbnil;
    }

    public String getVideo() {
        return Video;
    }

    public String getAbout() {
        return About;
    }
}


