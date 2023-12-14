package com.kukuhAditya.newsApi;

import android.content.Context;
import android.widget.Toast;

import com.kukuhAditya.newsApi.Models.NewsApiResponse;
import com.kukuhAditya.newsApi.layout.NewsView;
import com.kukuhAditya.newsApi.misc.SharedState;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    SharedState sharedState = SharedState.getInstance();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadLines(NewsView.NewsListener listener, String query) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);

        Call<NewsApiResponse> call = null;

        if((int)sharedState.getSetting("TYPE", 0) == 0){
            if(query == null || query.isEmpty() || query.isBlank()) {
                query = "*";
            }

            listener.reset();
            call  = callNewsApi.callEverything( query, context.getString(R.string.api_key));

            if(call == null){
                return;
            }

            try {
                call.enqueue(new Callback<NewsApiResponse>() {
                    @Override
                    public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                        listener.onFetchData(response.body().getArticles(), response.message());
                        listener.refresh();
                    }

                    @Override
                    public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                        listener.onError("Request Failed!");
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }else{


            List<String> catList = List.of(
                    "business",
                    "entertainment",
                    "health",
                    "science",
                    "sports",
                    "technology");



            List<String> q = new LinkedList<>();

            catList.forEach(v -> {
                if((boolean)sharedState.getSetting(v, false)){
                    q.add(v);
                }
            });

            if(q.isEmpty()){
                q.add("general");
            }

            int i = -1;
            for(String qr : q){
                if(++i == 0){
                    listener.reset();
                }

                call  = callNewsApi.callHeadlines("us", qr, query, context.getString(R.string.api_key));

                if(call == null){
                    return;
                }

                try {
                    call.enqueue(new Callback<NewsApiResponse>() {
                        @Override
                        public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                            listener.onFetchData(response.body().getArticles(), response.message());
                            listener.refresh();
                        }

                        @Override
                        public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                            listener.onError("Request Failed!");
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public RequestManager(Context context) {
        this.context = context;
    }

    public interface CallNewsApi {
        @GET("everything")
        Call<NewsApiResponse> callEverything(
                @Query("q") String query,
                @Query("apiKey") String api_key
        );
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_key
        );


    }
}
