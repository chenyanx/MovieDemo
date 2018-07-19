package com.example.jeremychen.movieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jeremychen.movieapp.Loader.MyLoader;
import com.example.jeremychen.movieapp.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private final static String PASS_FLAG = "PASS_ID";
    private final static String CONTENT_PROGRESS = "加载中。。。。";
    @BindView(R.id.image_view_detail)
    ImageView imageView;
    @BindView(R.id.text_view_director)
    TextView text_view_director;
    @BindView(R.id.text_view_casts)
    TextView text_view_casts;
    @BindView(R.id.text_view_type)
    TextView text_view_type;
    @BindView(R.id.text_view_year)
    TextView text_view_year;
    @BindView(R.id.text_view_description)
    TextView text_view_description;

    private ProgressDialog progressDialog;


    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        title = intent.getExtras().getString(PASS_FLAG);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(CONTENT_PROGRESS);
        // TODO: 2018/6/23 当点击屏幕外区域，进度条不消失
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0,null,this);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        String download_path = "http://www.imooc.com/api/movie?title=" + title;
        return new MyLoader(this,download_path);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, final List<Movie> data) {
        Glide.with(this).load(data.get(0).getImageUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                // TODO: 2018/6/23 使得进度条消失
                progressDialog.dismiss();
                return false;
            }
        }).into(imageView);
        String directors = "导演: ";
        for(String director : data.get(0).getDirectors())
        {
            directors += director + " ";
        }
        text_view_director.setText(directors);
        String casts = "演员: ";
        for(String cast : data.get(0).getActors())
        {
            casts += cast + " ";
        }
        text_view_casts.setText(casts);
        String types = "类型: ";
        for(String type : data.get(0).getType())
        {
            types += type + " ";
        }
        text_view_type.setText(types);
        text_view_year.setText("上映年份: " + data.get(0).getYear() + "");
        text_view_description.setText("故事情节" + "\n" + data.get(0).getDescription());

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
