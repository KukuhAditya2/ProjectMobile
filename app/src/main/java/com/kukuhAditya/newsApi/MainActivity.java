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
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.kukuhAditya.newsApi.Models.NewsApiResponse;
import com.kukuhAditya.newsApi.Models.NewsHeadlines;
import com.kukuhAditya.newsApi.adapter.Adapter;
import com.kukuhAditya.newsApi.adapter.impl.CategoryHolder;
import com.kukuhAditya.newsApi.adapter.impl.NewsAdapterHolder;
import com.kukuhAditya.newsApi.layout.AboutView;
import com.kukuhAditya.newsApi.layout.NewsView;
import com.kukuhAditya.newsApi.misc.SharedState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView catView;

    Adapter<String, CategoryHolder> catAdapter;

    EditText searchField;
    TabLayout tab;

    private final SharedState sharedState = SharedState.getInstance();

    private int oldTab = 0;
    @Override
    protected void onResume() {
        super.onResume();
        //tab.getTabAt(oldTab).select();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsView news = findViewById(R.id.newsTable);
        news.refreshNews();

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

        int pl = news.getPaddingLeft();
        int pr = news.getPaddingRight();
        int pu = news.getPaddingTop();

        news.setPadding(pl,pu,pr, tabHeight);











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

        tab = findViewById(R.id.tabLayout);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    AboutView aboutLayout = findViewById(R.id.aboutView);
                    LinearLayout newsLayout = findViewById(R.id.newsView);

                    switch(tab.getPosition()){
                        case 0: // Recent
                        case 1 : // Highlight
                            oldTab = tab.getPosition();

                            sharedState.putSetting("TYPE", oldTab);
                            aboutLayout.setVisibility(View.INVISIBLE);
                            newsLayout.setVisibility(View.VISIBLE);

                            news.refreshNews();
                            break;

                        case 2 :
                            aboutLayout.setVisibility(View.VISIBLE);
                            newsLayout.setVisibility(View.INVISIBLE);
                    }
                }
                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
            }
        );


    }

}