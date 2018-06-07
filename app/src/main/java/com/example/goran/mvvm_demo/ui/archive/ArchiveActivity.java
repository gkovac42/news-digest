package com.example.goran.mvvm_demo.ui.archive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.article.ArticleActivity;

public class ArchiveActivity extends AppCompatActivity {

    private ArticleAdapter adapter;
    private ArchiveViewModel viewModel;
    private RecyclerView recyclerView;

    public static Intent newIntent(Context context) {
        return new Intent(context, ArchiveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new ArticleAdapter(this);
        adapter.setListener(new ArticleAdapter.ClickListener() {
            @Override
            public void onClick(String articleUrl) {
                navigateToArticle(articleUrl);
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

        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel.class);

        viewModel.getArticlesFromDb().observe(this, articles -> {
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        });
    }

    private void navigateToArticle(String articleUrl) {
        Intent intent = ArticleActivity.newIntent(ArchiveActivity.this, articleUrl);
        startActivity(intent);
    }

    private void showDeleteSnackbar(final Article article) {
        Snackbar.make(recyclerView, "Article deleted.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> viewModel.insert(article))
                .show();
    }
}
