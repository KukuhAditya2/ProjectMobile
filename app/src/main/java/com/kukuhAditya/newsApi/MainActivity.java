package com.kukuhAditya.newsApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.kukuhAditya.newsApi.Models.NewsApiResponse;
import com.kukuhAditya.newsApi.Models.NewsHeadlines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching news articles..");
        dialog.show();

        tab = findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 switch(tab.getPosition()){
                     case 0: // Recent
                     case 1 : // Highlight
                         oldTab = tab.getPosition();
                         break;

                     case 2 :
                         Intent aboutV = new Intent(MainActivity.this, AboutActivity.class);
                         startActivity(aboutV);
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


        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tabLayout);
        int tabHeight = tab.getHeight();

        int pl = recyclerView.getPaddingLeft();
        int pr = recyclerView.getPaddingRight();
        int pu = recyclerView.getPaddingTop();

        recyclerView.setPadding(pl,pu,pr, tabHeight);

        Map<String, Boolean> prop = new HashMap<>();

        recyclerView.setOnTouchListener((v,e) -> {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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