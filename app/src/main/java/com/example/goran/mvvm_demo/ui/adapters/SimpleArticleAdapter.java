package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;

public class SimpleArticleAdapter extends ArticleAdapter {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article_simple, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View itemView = holder.itemView;
        Article article = getItem(position);

        TextView txtTitle = itemView.findViewById(R.id.txt_item_title_s);
        txtTitle.setText(article.getTitle());

        ImageView imgThumb = itemView.findViewById(R.id.img_item_thumbnail_s);

        Glide.with(holder.itemView.getContext())
                .load(article.getUrlToImage())
                .error(R.drawable.ic_info_outline_black_24dp)
                .override(120,120)
                .into(imgThumb);
    }
}
