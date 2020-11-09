package com.traidev.masterCoder.Youtube.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.traidev.masterCoder.Main_Interface;
import com.traidev.masterCoder.R;
import com.traidev.masterCoder.RetrofitClient;
import com.traidev.masterCoder.Youtube.UpdateModal;
import com.traidev.masterCoder.Youtube.adapters.RecylerAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 */
public class ChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<UpdateModal> videos;
    private RecylerAdapter adapter;
    private Main_Interface main_interface;

    private SwipeRefreshLayout swipeRefreshLayout;

    public ChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.mList_videos);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout  = view.findViewById(R.id.swipe_videos);
        swipeRefreshLayout.setOnRefreshListener(this);

        fetchInfo();

        return view;
    }



    public void fetchInfo()
    {

        swipeRefreshLayout.setRefreshing(true);

        main_interface = RetrofitClient.getApiClient().create(Main_Interface.class);

        Call<List<UpdateModal>> call = main_interface.getVideos();

        call.enqueue(new Callback<List<UpdateModal>>() {
            @Override
            public void onResponse(Call<List<UpdateModal>> call, Response<List<UpdateModal>> response) {

                videos = response.body();
                adapter = new RecylerAdapter(videos,getActivity(),1);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<UpdateModal>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
            fetchInfo();
    }
}

