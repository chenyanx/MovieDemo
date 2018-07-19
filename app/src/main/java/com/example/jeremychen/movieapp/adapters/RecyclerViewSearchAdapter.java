package com.example.jeremychen.movieapp.adapters;

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
 * Created by jeremychen on 2018/6/22.
 */

public class RecyclerViewSearchAdapter extends RecyclerView.Adapter<RecyclerViewSearchAdapter.MainViewHolder>{

    private Context mcontext;
    private List<Movie> res_list;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewSearchAdapter(Context context, List<Movie> list)
    {
        this.mcontext = context;
        this.res_list = list;
    }
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.search_layout,parent,false);
        MainViewHolder holder = new MainViewHolder(view);
        return holder;
    }

    public void SetOnClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.textView_title.setText(res_list.get(position).getTitle());
        String value_director = " ";
        String value_casts = " ";
        for(String name : res_list.get(position).getActors())
        {
            value_casts += " " + name ;
        }
        holder.textView_casts.setText("演员:" + value_casts);
        for(String name : res_list.get(position).getDirectors())
        {
            value_director += " " + name;
        }
        holder.textView_director.setText("导演:" + value_director);
        holder.ratingBar.setRating(Float.parseFloat(res_list.get(position).getAverage_rating()));
        holder.textView_year.setText("上映：" + String.valueOf(res_list.get(position).getYear()));
        Glide.with(mcontext).load(res_list.get(position).getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return res_list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.text_view_title)
        TextView textView_title;
        @BindView(R.id.image_view_pic)
        ImageView imageView;
        @BindView(R.id.text_view_director)
        TextView textView_director;
        @BindView(R.id.text_view_casts_name)
        TextView textView_casts;
        @BindView(R.id.text_view_year)
        TextView textView_year;
        @BindView(R.id.search_rating_bar)
        RatingBar ratingBar;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


    }
}
