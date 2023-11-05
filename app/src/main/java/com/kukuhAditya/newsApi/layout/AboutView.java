package com.kukuhAditya.newsApi.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kukuhAditya.newsApi.R;

public class AboutView extends ConstraintLayout {
    public AboutView(Context context) {
        super(context);
        init(context);
    }

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_about, this);

        String[] members = {
                "41521010118 - Kukuh Aditya",
                "41520110069 - Malik Syah",
                "41519120044 - Reza Arrofi",
                "41521120048 - Ricky Miftahuddin",
                "41521110012 - Septian Pramana R",
                "41519120073 - Syauqii Fayyadh Hilal Z"
        };

        ListView listView = findViewById(R.id.listViewMembers);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, members);
        listView.setAdapter(adapter);
    }
}
