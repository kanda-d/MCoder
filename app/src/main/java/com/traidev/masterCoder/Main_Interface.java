package com.traidev.masterCoder;

import com.traidev.masterCoder.Youtube.UpdateModal;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Main_Interface {



    @POST("TubeUpdates.php")
    Call<List<UpdateModal>> getVideos();

    @POST("versionCheck.php")
    Call<DefaultResponse> getUpdate(
            @Query("versionCode") String code
    );






}
