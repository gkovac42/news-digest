package com.example.goran.mvvm_demo.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.remote.model.Article;

import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private ArticleAdapter adapter;
    private ArticlesViewModel viewModel;
    private RecyclerView recyclerView;

    public static Intent newIntent(Context context) {
        return new Intent(context, ArchiveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new ArticleAdapter(this);
        adapter.setListener(new ArticleAdapter.AdapterListener() {
            @Override
            public void onClick(String articleUrl) {
                Intent intent = ArticleActivity.newIntent(ArchiveActivity.this, articleUrl);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Article article) {
                viewModel.delete(article);
                showDeleteSnackbar(article);
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        viewModel.getArticlesFromDb().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                adapter.setArticles(articles);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showDeleteSnackbar(final Article article) {
        Snackbar.make(recyclerView, "Article deleted.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.insert(article);
                    }
                })
                .show();
    }
}
