package com.kukuhAditya.newsApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.kukuhAditya.newsApi.Models.NewsApiResponse;
import com.kukuhAditya.newsApi.Models.NewsHeadlines;
import com.kukuhAditya.newsApi.adapter.Adapter;
import com.kukuhAditya.newsApi.adapter.impl.CategoryHolder;
import com.kukuhAditya.newsApi.adapter.impl.NewsAdapterHolder;
import com.kukuhAditya.newsApi.misc.SharedState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView newsView, catView;
    Adapter<NewsHeadlines, NewsAdapterHolder> newsAdapter;
    Adapter<String, CategoryHolder> catAdapter;
    ProgressDialog dialog;

    EditText searchField;
    TabLayout tab;

    private int oldTab;
    @Override
    protected void onResume() {
        super.onResume();
        tab.getTabAt(oldTab).select();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.searchBox);
        searchField.setOnKeyListener((v,k,e) -> {
            if ((e.getAction() == e.ACTION_DOWN) && (k == KeyEvent.KEYCODE_ENTER)) {
                boolean state = (boolean) SharedState.getInstance().getSetting("foo", false);

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception ex) {
                    // TODO: handle exception
                }

                return true;
            }
            return false;
            });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching news articles..");
        dialog.show();

        tab = findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 View about = findViewById(R.id.about);
                 View recent = findViewById(R.id.recent);

                 switch(tab.getPosition()){
                     case 0: // Recent
                     case 1 : // Highlight
                         oldTab = tab.getPosition();


                         about.setVisibility(View.INVISIBLE);


                         recent.setVisibility(View.VISIBLE);

                         break;

                     case 2 :
                         about.setVisibility(View.VISIBLE);
                         recent.setVisibility(View.INVISIBLE);
                 }
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         }
        );


        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadLines(listener, "general", null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            showNews(list);
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {

        }
    };

    private void showNews(List<NewsHeadlines> list) {
        newsView = findViewById(R.id.recycler_main);
        newsView.setHasFixedSize(true);
        newsView.setLayoutManager(new GridLayoutManager(this, 1));
        newsAdapter = new Adapter(this, R.layout.news_items, NewsAdapterHolder.class, list);
        newsView.setAdapter(newsAdapter);

        catView = findViewById(R.id.cat_list);
        catView.setHasFixedSize(true);
        catView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,  false));

        List<String> catList = List.of(
                "business",
                "entertainment",
                "general",
                "health",
                "science",
                "sports",
                "technology");

        catAdapter = new Adapter(this, R.layout.category_items, CategoryHolder.class, catList);
        catView.setAdapter(catAdapter);





        TabLayout tab = findViewById(R.id.tabLayout);
        int tabHeight = tab.getHeight();

        int pl = newsView.getPaddingLeft();
        int pr = newsView.getPaddingRight();
        int pu = newsView.getPaddingTop();

        newsView.setPadding(pl,pu,pr, tabHeight);

        Map<String, Boolean> prop = new HashMap<>();

        newsView.setOnTouchListener((v,e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    if(prop.get("REFRESH")){
                        dialog.show();

                        System.out.println("foo");
                        RequestManager manager = new RequestManager(MainActivity.this);
                        manager.getNewsHeadLines(listener, "general", null);

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

}