package com.example.goran.mvvm_demo.ui.articles;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.BaseActivity;

public class SearchActivity extends BaseActivity {

    private ArticlesViewModel viewModel;
    private ArticleAdapter adapter;
    private EditText txtSearchQuery;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setActionBarColor(R.color.colorGreen);
        setStatusBarColor(R.color.colorGreenDark);

        initSearchComponents();

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
    }

    private void initSearchComponents() {
        txtSearchQuery = findViewById(R.id.txt_search_query);

        ImageButton btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(view -> {
            String query = txtSearchQuery.getText().toString();
            if (query.length() > 2) {
                updateAdapter(query);
                hideSoftKeyboard();
            } else {
                Toast.makeText(this, R.string.msg_query_short, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initAdapter() {
        adapter = new ArticleAdapter();
        adapter.setListener(new ArticleAdapter.AdapterListener() {
            @Override
            public void onClick(Article article) {
                navigateToArticle(article.getUrl());
            }

            @Override
            public void onLongClick(Article article) {
                viewModel.insertIntoDb(article);
                showInsertSnackbar(article);
            }
        });
    }

    private void navigateToArticle(String articleUrl) {
        Intent intent = ReaderActivity.newIntent(SearchActivity.this, articleUrl);
        startActivity(intent);
    }

    private void showInsertSnackbar(final Article article) {
        Snackbar.make(recyclerView, R.string.msg_archived, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> viewModel.deleteFromDb(article))
                .show();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void updateAdapter(String query) {
        viewModel.searchApiForArticles(query)
                .observe(this, articles -> {
                    adapter.setArticles(articles);
                    adapter.notifyDataSetChanged();
                });
    }
}