package com.kukuhAditya.newsApi.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kukuhAditya.newsApi.R;

public class NewsView extends LinearLayout {
    public static enum NewsType {
        HIGHLIGHT, RECENT
    }

    private NewsType newsType;

    public NewsView(Context context) {
        super(context);
    }

    public NewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {

    }
}
