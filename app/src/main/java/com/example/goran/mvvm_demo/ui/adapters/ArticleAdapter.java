package com.example.goran.mvvm_demo.ui.adapters;

import android.content.Context;
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
    private ClickListener listener;
    private Context context;

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public interface ClickListener {

        void onClick(String articleUrl);

        void onLongClick(Article article);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View itemView = holder.itemView;

        TextView txtTitle = itemView.findViewById(R.id.txt_item_title);
        txtTitle.setText(articles.get(position).getTitle());

        ImageView imgThumb = itemView.findViewById(R.id.img_item_thumbnail);

        Glide.with(context)
                .load(articles.get(position).getUrlToImage())
                .centerCrop()
                .into(imgThumb);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = articles.get(getAdapterPosition()).getUrl();
                    listener.onClick(url);
                }
            });

            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Article article = articles.get(getAdapterPosition());
                    listener.onLongClick(article);
                    return true;
                }
            });
        }
    }
}
