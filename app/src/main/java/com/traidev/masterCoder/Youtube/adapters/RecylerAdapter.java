package com.traidev.masterCoder.Youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.traidev.masterCoder.R;
import com.traidev.masterCoder.Youtube.DetailsActivity;
import com.traidev.masterCoder.Youtube.UpdateModal;

import java.util.List;

public class RecylerAdapter extends RecyclerView.Adapter <RecylerAdapter.MyViewHolder>{

    private List<UpdateModal> videos;
    private Context context;
    private int lchange;


    public RecylerAdapter(List<UpdateModal> videos, Context context, int lchange)
    {
        this.videos = videos;
        this.lchange = lchange;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(lchange==2)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_thumb,parent,false);
            return new MyViewHolder(view);
        }

        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_post_layout, parent, false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if(lchange !=2) {  holder.Title.setText(videos.get(position).getTitle());}

        holder.VideoId.setText(videos.get(position).getVideo());
        Glide.with(context).load("http://www.traidev.com/LIVE_APPS/Mcoder/thumb/"+videos.get(position).getThumbnil()).into(holder.Img);


        holder.Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.youtube.com/watch?v="+videos.get(position).getVideo()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

              /*  Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("VideoId", );
                i.putExtra("VideoTitle", videos.get(position).getTitle());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
            return videos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView Title,VideoId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Img = (ImageView) itemView.findViewById(R.id.ImageThumb);
            Title = (TextView) itemView.findViewById(R.id.textViewTitle);
            VideoId = (TextView) itemView.findViewById(R.id.txtId);
        }
    }

}
