package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;

public class ArticleAdapter extends ListAdapter<Article, ArticleAdapter.ViewHolder> {

    private AdapterListener listener;

    public ArticleAdapter() {
        super(Article.DIFF_CALLBACK);
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
        Article article = getItem(position);

        TextView txtTitle = itemView.findViewById(R.id.txt_item_title);
        txtTitle.setText(article.getTitle());

        TextView txtDescription = itemView.findViewById(R.id.txt_item_desc);
        txtDescription.setText(article.getDescription());

        ImageView imgThumb = itemView.findViewById(R.id.img_item_thumbnail);

        Glide.with(holder.itemView.getContext())
                .load(article.getUrlToImage())
                .error(R.drawable.ic_info_outline_black_24dp)
                .override(360, 180)
                .into(imgThumb);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(getItem(getAdapterPosition()));
                }
            });

            this.itemView.setOnLongClickListener(view -> {
                if (listener != null) {
                    listener.onLongClick(getItem(getAdapterPosition()));
                }
                return true;
            });
        }
    }

    public interface AdapterListener {

        void onClick(Article article);

        void onLongClick(Article article);
    }
}
