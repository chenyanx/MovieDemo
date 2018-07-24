package com.example.jeremychen.movieapp.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.example.jeremychen.movieapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremychen on 2018/6/21.
 */

public class MyLoader extends AsyncTaskLoader<List<Movie>> {

    private String download_url;


    public MyLoader(Context context, String url) {
        super(context);
        this.download_url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(isStarted())
        {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        // TODO: 2018/6/21  使用httpURLConnection下载数据
        InputStream inputStream = null;
        try {
            URL url = new URL(download_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(60000);
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String content = null;
                while((content = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(content);
                }
                String data = stringBuilder.toString();
                return DataResolver(data);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private List<Movie> DataResolver(String data)
    {
        List<Movie> result_list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("movies");
            for(int index = 0; index < jsonArray.length(); index++)
            {
                JSONObject object = (JSONObject) jsonArray.get(index);
                Movie movie = new Movie();
                int id = object.getInt("id");
                // TODO: 2018/6/21 电影评分
                JSONObject rating = object.getJSONObject("rating");
                String average_rating = rating.getString("average");
                // TODO: 2018/6/21 电影名称
                String title = object.getString("title");
                // TODO: 2018/6/21 电影详情
                String description = object.getString("description");
                // TODO: 2018/6/21 电影类型列表
                List<String> types_list = new ArrayList<>();
                for(int i = 0; i < object.getJSONArray("types").length(); i++)
                {
                    types_list.add((String) object.getJSONArray("types").get(i));
                }
                // TODO: 2018/6/21 演员列表
                List<String> actors_list = new ArrayList<>();
                JSONArray actors = object.getJSONArray("casts");
                for(int i = 0; i < actors.length(); i++)
                {
                    JSONObject actor = (JSONObject) actors.get(i);
                    String actor_name = actor.getString("name");
                    actors_list.add(actor_name);
                }
                // TODO: 2018/6/21 导演列表
                List<String> directors_list = new ArrayList<>();
                JSONArray directors = object.getJSONArray("directors");
                for(int i = 0; i < directors.length(); i++)
                {
                    JSONObject director = (JSONObject) actors.get(i);
                    String director_name = director.getString("name");
                    directors_list.add(director_name);
                }
                int year = object.getInt("year");
                String imageurl = object.getString("imageUrl");
                movie.setId(id);
                movie.setActors(actors_list);
                movie.setDirectors(directors_list);
                movie.setAverage_rating(average_rating);
                movie.setImageUrl(imageurl);
                movie.setTitle(title);
                movie.setYear(year);
                movie.setDescription(description);
                movie.setType(types_list);
                result_list.add(movie);
            }
            return result_list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
