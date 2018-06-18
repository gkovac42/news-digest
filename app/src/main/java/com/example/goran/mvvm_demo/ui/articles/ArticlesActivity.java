package com.example.goran.mvvm_demo.ui.articles;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter.AdapterListener;
import com.example.goran.mvvm_demo.util.ErrorCodes;

import java.util.List;

public class ArticlesActivity extends BaseActivity {

    private static final String EXTRA_SOURCE_ID = "source_id";
    private static final String EXTRA_SOURCE_NAME = "source_name";

    private ArticlesViewModel viewModel;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setStatusBarColor(R.color.colorRedDark);
        setActionBarColor(R.color.colorRed);

        progressBar = findViewById(R.id.progress_list);

        String sourceName = getIntent().getStringExtra(EXTRA_SOURCE_NAME);
        getSupportActionBar().setTitle(sourceName);

        initAdapter();

        initRecyclerView();

        String sourceId = getIntent().getStringExtra(EXTRA_SOURCE_ID);

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        viewModel.getArticlesFromApi(sourceId).observe(this, articles -> {
            updateAdapter(articles);
            hideProgressBar();
        });

        viewModel.getErrorCodeLiveData().observe(this, errorCode -> {
            if (errorCode != null && errorCode == ErrorCodes.NETWORK_ERROR) {
                showNetworkError();
                hideProgressBar();
            }
        });
    }

    private void initAdapter() {
        adapter = new ArticleAdapter();
        adapter.setListener(new AdapterListener() {
            @Override
            public void onClick(Article article) {
                readArticle(article);
            }

            @Override
            public void onLongClick(Article article) {
                viewModel.insertIntoDb(article);
                showInsertSnackbar(article);
            }
        });
    }

    private void readArticle(Article article) {
        Intent intent = ReaderActivity.newIntent(this, article.getUrl());
        startActivity(intent);
    }

    private void showInsertSnackbar(final Article article) {
        Snackbar.make(recyclerView, R.string.msg_archived, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> viewModel.deleteFromDb(article))
                .show();
    }

    private void updateAdapter(List<Article> articles) {
        adapter.submitList(articles);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
