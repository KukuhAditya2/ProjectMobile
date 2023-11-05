package com.kukuhAditya.newsApi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for RecyclerView
 * @param <T> Entity Data
 * @param <V> Holder
 */
public final class Adapter<T, V extends AbstractHolder<T>> extends RecyclerView.Adapter<V>{

    private final List<T> entries;
    private final Class<V> viewHolder;
    private final int ui;
    private final Context context;

    public Adapter(Context context,  @LayoutRes int xmlUI, Class<V> viewHolder, List<T> entries) {
        this.ui = xmlUI;
        this.entries = entries;
        this.viewHolder = viewHolder;

        this.context = context;
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(context).inflate(ui, parent, false);
            V holder = (V)this.viewHolder.getDeclaredConstructor(View.class).newInstance(view);

            return holder;
        } catch (NoSuchMethodException | InvocationTargetException | RuntimeException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        T t = entries.get(position);
        holder.setEntity(t);
        holder.onCreate(t);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

}
