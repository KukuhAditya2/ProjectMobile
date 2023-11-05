package com.kukuhAditya.newsApi.adapter.impl;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.kukuhAditya.newsApi.Models.NewsHeadlines;
import com.kukuhAditya.newsApi.R;
import com.kukuhAditya.newsApi.adapter.AbstractHolder;
import com.kukuhAditya.newsApi.misc.SharedState;

public class CategoryHolder extends AbstractHolder<String> {

    private final ToggleButton catNameButton;
    private String text;

    private final SharedState state = SharedState.getInstance();
    public CategoryHolder(@NonNull View itemView) {
        super(itemView);

        catNameButton = itemView.findViewById(R.id.cat_name);

        catNameButton.setOnClickListener(v -> {
            catNameButton.setText(text);
            state.putSetting(text, catNameButton.isChecked());
        });
    }

    @Override
    protected void onCreate(String entity) {
        catNameButton.setText(entity);
        text = entity;
    }

}
