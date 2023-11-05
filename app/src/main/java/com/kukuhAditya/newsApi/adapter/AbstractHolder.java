package com.kukuhAditya.newsApi.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractHolder<T> extends RecyclerView.ViewHolder{

    private T entity;
    public AbstractHolder(@NonNull View itemView) {
        super(itemView);
    }

    public T getEntity(){ return entity;}
    void setEntity(T entity){
        this.entity = entity;
    }

    protected abstract void onCreate(T entity);
}
