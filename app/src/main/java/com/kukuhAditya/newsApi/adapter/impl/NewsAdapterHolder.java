package com.kukuhAditya.newsApi.adapter.impl;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.kukuhAditya.newsApi.Models.NewsHeadlines;
import com.kukuhAditya.newsApi.R;
import com.kukuhAditya.newsApi.ViewActivity;
import com.kukuhAditya.newsApi.adapter.AbstractHolder;
import com.squareup.picasso.Picasso;

/**
 * Used to hold individual news data
 */
public class NewsAdapterHolder extends AbstractHolder<NewsHeadlines> {
    private final TextView text_title, text_source;
    private final ImageView img_headline;
    private final CardView cardView;
    private String url;

    public NewsAdapterHolder(@NonNull View itemView) {
        super(itemView);

        text_title   = itemView.findViewById(R.id.text_title);
        text_source  = itemView.findViewById(R.id.text_source);
        img_headline = itemView.findViewById(R.id.img_headline);
        cardView     = itemView.findViewById(R.id.main_container);
    }

    @Override
    protected void onCreate(NewsHeadlines entity) {
        url = getEntity().getUrl();

        text_title.setText(getEntity().getTitle());
        text_source.setText(getEntity().getSource().getName());

        if (getEntity().getUrlToImage() != null){
            Picasso.get().load(getEntity().getUrlToImage()).into(img_headline);
        }

        cardView.setOnClickListener(v -> {
                Intent detailView = new Intent(v.getContext(), ViewActivity.class);
                detailView.putExtra("link", url);

                v.getContext().startActivity(detailView);
            }
        );
    }
}
