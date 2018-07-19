package com.example.jeremychen.movieapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jeremychen.movieapp.Loader.MyLoader;
import com.example.jeremychen.movieapp.adapters.OnItemClickListener;
import com.example.jeremychen.movieapp.adapters.RecyclerViewAdapter;
import com.example.jeremychen.movieapp.adapters.RecyclerViewSearchAdapter;
import com.example.jeremychen.movieapp.adapters.SpacesItemDecoration;
import com.example.jeremychen.movieapp.model.Movie;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String DOWNLOAD_PATH = "http://www.imooc.com/api/movie";
    private static final String TYPE_INDICATOR = "请选择";
    private final static String PASS_FLAG = "PASS_ID";
    private final static String LOGGER_TAG = "TAG";
    private final static String CONTENT_PROGRESS = "加载中。。。。";

    @BindView(R.id.button_search)
    Button button_search;
    @BindView(R.id.edit_text_name)
    EditText editText;
    @BindView(R.id.text_view_return)
    TextView textView_return;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;


    private RecyclerViewAdapter adapter_home;
    private RecyclerViewSearchAdapter adapter_search;
    private ArrayAdapter<String> arrayAdapter;
    private String filterName;
    private String filterType;
    private ProgressDialog progressDialog;
    private LoaderManager loaderManager;
    //TODO: 2018/6/22  确定是否在查找页面
    private boolean SEARCH_TAG = false;

    private List<Movie> res_list = new ArrayList<>();
    private List<Movie> search_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(CONTENT_PROGRESS);
        progressDialog.show();

        // TODO: 2018/6/23 ButterKnife的使用
        ButterKnife.bind(this);
        // TODO: 2018/6/23 Logger 的初始化
        Logger.init(LOGGER_TAG);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null,this);
        final GridLayoutManager gridLayout = new GridLayoutManager(this,2);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(gridLayout);
        // TODO: 2018/6/22 同时创建两种不同格式的adapter以供后来使用
        adapter_home = new RecyclerViewAdapter(this, res_list);

        adapter_home.SetOnClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(PASS_FLAG,res_list.get(position).getTitle());
                startActivity(intent);
            }
        });

        adapter_search = new RecyclerViewSearchAdapter(MainActivity.this, search_list);
        adapter_search.SetOnClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(PASS_FLAG,search_list.get(position).getTitle());
                MainActivity.this.startActivity(intent);
            }
        });
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterName = editText.getText().toString();
                if(!TextUtils.isEmpty(filterName) || !filterType.equals(TYPE_INDICATOR))
                {
                    progressDialog.show();
                    textView_return.setVisibility(View.VISIBLE);
                    SEARCH_TAG = true;
                    loaderManager.restartLoader(0,null,MainActivity.this);
                    recyclerview.setAdapter(adapter_search);
                    recyclerview.setLayoutManager(linearLayoutManager);
                }
                else
                    return;
            }
        });

        textView_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_TAG = false;
                search_list.clear();
                textView_return.setVisibility(View.INVISIBLE);
                filterName = "";
                recyclerview.setAdapter(adapter_home);
                recyclerview.setLayoutManager(gridLayout);
            }
        });
        // TODO: 2018/6/22 表明此时并不是在查找页面
        if(SEARCH_TAG == false) {
            recyclerview.setAdapter(adapter_home);
        }
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        recyclerview.addItemDecoration(spacesItemDecoration);

        // TODO: 2018/6/21 初始化spinner
        List<String> spinner_data = new ArrayList<>();
        initData(spinner_data);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinner_data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = spinner.getSelectedItem().toString();
                //Logger.v(filterType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initData(List<String> spinner_data) {
        spinner_data.add(getString(R.string.PLEASE_CHOOSE));
        spinner_data.add(getString(R.string.TYPE_ACTION));
        spinner_data.add(getString(R.string.SCIENCE));
        spinner_data.add(getString(R.string.ADVANTURE));
        spinner_data.add(getString(R.string.STORY));
        spinner_data.add(getString(R.string.BIOGRAPHY));
        spinner_data.add(getString(R.string.CRIME));
        spinner_data.add(getString(R.string.SUSPENSE));
        spinner_data.add(getString(R.string.HUMOR));
        spinner_data.add(getString(R.string.LOVE));
        spinner_data.add(getString(R.string.CATOON));
        spinner_data.add(getString(R.string.MAGIC));
        spinner_data.add(getString(R.string.SPORT));
        spinner_data.add(getString(R.string.PANIC));
        spinner_data.add(getString(R.string.THRILLER));
        spinner_data.add(getString(R.string.OPERA));
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        String download_path = DOWNLOAD_PATH;
        boolean tag = false;
        if(!TextUtils.isEmpty(filterName)) {
            tag = true;
            download_path += "?title=" + filterName;
        }
        if(!TextUtils.isEmpty(filterType)) {
            if(!filterType.equals(TYPE_INDICATOR)) {
                if(tag == false) {
                    download_path += "?types=" + filterType;
                }
                else {
                    download_path += "&types=" + filterType;
                }
            }
        }
        return new MyLoader(this, download_path);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if(SEARCH_TAG == true) {
            search_list.clear();
            search_list.addAll(data);
            adapter_search.notifyDataSetChanged();
            progressDialog.dismiss();
        }
        else {
            if(res_list != null) {
                res_list.clear();
            }
            res_list.addAll(data);
            adapter_home.notifyDataSetChanged();
            progressDialog.dismiss();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        //重制loader
    }
}
