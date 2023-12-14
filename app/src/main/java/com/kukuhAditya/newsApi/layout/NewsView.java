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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
    NewsListener listener;
    RequestManager manager;
    public void initialize(){
        dialog = new ProgressDialog(this.getContext());
        dialog.setTitle("Fetching news articles..");

        newsView = findViewById(R.id.news_container);

        manager = new RequestManager(this.getContext());
        listener = new NewsListener(dialog, newsView, this.getContext());

        Map<String, Boolean> prop = new HashMap<>();
        newsView.setOnTouchListener((v,e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    if(prop.get("REFRESH")){
                        manager.getNewsHeadLines(listener, null);

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

    /**
     * Control news here. Search query, category, type, etc should be visible here
     */
    public void refreshNews(){

        String   searchQuery = (String) sharedState.getSetting("SEARCH", "");
        NewsType type        = NewsType.values[(Integer) sharedState.getSetting("TYPE", 0)];

        dialog.show();


        String q = (String)sharedState.getSetting("SEARCHQ", "");

        manager.getNewsHeadLines(listener,  q);
    }

    private void captureGesture(){

    }

    public static class NewsListener implements OnFetchDataListener<NewsApiResponse> {

        private final ProgressDialog dialog;
        private final RecyclerView newsView;
        private final Context context;
        NewsListener(ProgressDialog dialog, RecyclerView newsView, Context context) {
            this.dialog = dialog;
            this.newsView = newsView;
            this.context = context;
        }

        private List<NewsHeadlines> news = new LinkedList<>();
        public void reset(){
            news.clear();
        }
        public synchronized void refresh(){

            Collections.sort(news, (a,b) -> b.getPublishedAt().compareTo(a.getPublishedAt()));

            newsView.setHasFixedSize(true);
            newsView.setLayoutManager(new GridLayoutManager(context, 1));

            Adapter<NewsHeadlines, NewsAdapterHolder> newsAdapter;
            newsAdapter = new Adapter(context, R.layout.news_items, NewsAdapterHolder.class, news);

            newsView.setAdapter(newsAdapter);

            dialog.dismiss();
        }

        @Override
        public synchronized void onFetchData(List<NewsHeadlines> list, String message) {
            synchronized (news) {
                list.forEach(v -> news.add(v));
            }
        }

        @Override
        public void onError(String message) {

        }
    }
}
