package com.example.jeremychen.movieapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.jeremychen.movieapp.R;
import com.example.jeremychen.movieapp.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeremychen on 2018/6/21.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder> {

    private Context context;
    private List<Movie> m_list;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, List<Movie> list) {
        this.context = context;
        this.m_list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.homelayout,parent,false);
        MainViewHolder holder = new MainViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        String tag = m_list.get(position).getImageUrl();
        if(null == holder || null == tag || tag.equals(""))
        {
            return;
        }
        holder.textView.setText(m_list.get(position).getTitle());
        holder.ratingBar.setRating((float) Double.parseDouble(m_list.get(position).getAverage_rating()));
        Glide.with(context).load(tag).placeholder(R.mipmap.ic_launcher).dontAnimate().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public void onViewRecycled(MainViewHolder holder) {
        if(holder != null)
        {
            Glide.clear(holder.imageView);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return m_list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image_view_pic)
        ImageView imageView;
        @BindView(R.id.text_view_title)
        TextView textView;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

}
