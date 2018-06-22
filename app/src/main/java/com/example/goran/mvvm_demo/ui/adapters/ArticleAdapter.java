package com.example.goran.mvvm_demo.ui.adapters;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;

public class ArticleAdapter extends ListAdapter<Article, ArticleAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemDeleteListener onItemDeleteListener;
    private OnItemInsertListener onItemInsertListener;

    public ArticleAdapter() {
        super(Article.DIFF_CALLBACK);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnItemInsertListener(OnItemInsertListener onItemInsertListener) {
        this.onItemInsertListener = onItemInsertListener;
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

        Glide.with(itemView.getContext()).load(article.getUrlToImage())
                .error(R.drawable.ic_info_outline_black_24dp)
                .override(360, 180)
                .into(imgThumb);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            setListeners(itemView);
        }

        private void setListeners(View itemView) {
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(getItem(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(view -> {
                if (onItemDeleteListener != null) {
                    animateView(itemView, android.R.anim.slide_out_right);
                    onItemDeleteListener.onLongClick(getItem(getAdapterPosition()));

                } else if (onItemInsertListener != null) {
                    onItemInsertListener.onLongClick(getItem(getAdapterPosition()));
                }

                return true;
            });
        }

        private void animateView(View view, @AnimRes int resId) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), resId);
            view.startAnimation(animation);
        }
    }

    public interface OnItemClickListener {

        void onClick(Article article);
    }

    public interface OnItemDeleteListener {

        void onLongClick(Article article);
    }

    public interface OnItemInsertListener {

        void onLongClick(Article article);
    }
}
