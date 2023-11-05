package com.kukuhAditya.newsApi.layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kukuhAditya.newsApi.Models.NewsApiResponse;
import com.kukuhAditya.newsApi.Models.NewsHeadlines;
import com.kukuhAditya.newsApi.OnFetchDataListener;
import com.kukuhAditya.newsApi.R;
import com.kukuhAditya.newsApi.RequestManager;
import com.kukuhAditya.newsApi.adapter.Adapter;
import com.kukuhAditya.newsApi.adapter.impl.NewsAdapterHolder;
import com.kukuhAditya.newsApi.misc.SharedState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to control news table (RecyclerView)
 */
public class NewsView extends LinearLayout {
    public static enum NewsType {
        RECENT, HIGHLIGHT;
        public static final NewsType values[] = values();
    }

    private final SharedState sharedState = SharedState.getInstance();

    public NewsView(Context context) {
        super(context);
        inflate(context, R.layout.view_news, this);
    }

    public NewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_news, this);
    }

    // =========================================================

    RecyclerView newsView;

    ProgressDialog dialog;

    private void init(List<NewsHeadlines> list){
        newsView.setHasFixedSize(true);
        newsView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));


        Adapter<NewsHeadlines, NewsAdapterHolder> newsAdapter;
        newsAdapter = new Adapter(this.getContext(), R.layout.news_items, NewsAdapterHolder.class, list);

        newsView.setAdapter(newsAdapter);
    }

    /**
     * Control news here. Search query, category, type, etc should be visible here
     */
    public void refreshNews(){
        String   searchQuery = (String) sharedState.getSetting("SEARCH", "");
        NewsType type        = NewsType.values[(Integer) sharedState.getSetting("TYPE", 0)];


        newsView = findViewById(R.id.news_container);

        dialog = new ProgressDialog(this.getContext());
        dialog.setTitle("Fetching news articles..");
        dialog.show();

        RequestManager manager = new RequestManager(this.getContext());

        String[] cat = new String[]{"general", "sports"};

        manager.getNewsHeadLines(listener, cat[type.ordinal()], null);

        Map<String, Boolean> prop = new HashMap<>();

        newsView.setOnTouchListener((v,e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    if(prop.get("REFRESH")){
                        dialog.show();
                        manager.getNewsHeadLines(listener, cat[type.ordinal()], null);

                        return true;
                    }
            }

            return false;
        });


        newsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItemPosition == 0 && !recyclerView.canScrollVertically(-1) && dy < 0){
                    System.out.println(dy);
                    prop.put("REFRESH", true);
                    // The user has scrolled to the top
                    // You can now take appropriate actions.
                }else{
                    prop.put("REFRESH", false);
                }

                // Swipe to bottom
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && totalItemCount > 0) {


                }
            }
        });
    }

    private void captureGesture(){

    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            init(list);
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {

        }
    };
}
