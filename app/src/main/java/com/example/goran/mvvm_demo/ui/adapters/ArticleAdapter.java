package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private AdapterListener listener;

    public ArticleAdapter() {
        articles = new ArrayList<>();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View itemView = holder.itemView;
        Article article = articles.get(position);

        TextView txtTitle = itemView.findViewById(R.id.txt_item_title);
        txtTitle.setText(article.getTitle());

        TextView txtDescription = itemView.findViewById(R.id.txt_item_desc);
        txtDescription.setText(article.getDescription());

        ImageView imgThumb = itemView.findViewById(R.id.img_item_thumbnail);

        Glide.with(holder.itemView.getContext())
                .load(article.getUrlToImage())
                .error(R.drawable.ic_info_outline_black_24dp)
                .override(320, 180)
                .into(imgThumb);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.itemView.setOnClickListener(
                    view -> listener.onClick(articles.get(getAdapterPosition())));

            this.itemView.setOnLongClickListener(view -> {
                listener.onLongClick(articles.get(getAdapterPosition()));
                return true;
            });
        }
    }

    public interface AdapterListener {

        void onClick(Article article);

        void onLongClick(Article article);
    }
}
