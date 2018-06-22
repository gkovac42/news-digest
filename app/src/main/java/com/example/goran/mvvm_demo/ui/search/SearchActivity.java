package com.example.goran.mvvm_demo.ui.search;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.articles.ReaderActivity;
import com.example.goran.mvvm_demo.util.Code;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private SearchViewModel viewModel;
    private ArticleAdapter adapter;
    private EditText txtSearchQuery;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progress_search);

        initSearchComponents();

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        viewModel.getArticlesLiveData().observe(this, articles -> {
            updateAdapter(articles);
            hideProgressBar();
        });

        viewModel.getErrorCodeLiveData().observe(this, errorCode -> {
            if (errorCode != null && errorCode == Code.NETWORK_ERROR) {
                showNetworkError();
                hideProgressBar();
            }
        });
    }

    private void initSearchComponents() {
        txtSearchQuery = findViewById(R.id.txt_search_query);
        txtSearchQuery.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String query = txtSearchQuery.getText().toString();

                if (query.length() > 2) {
                    viewModel.searchApiForArticles(query);
                    showProgressBar();
                    hideSoftKeyboard();

                } else {
                    Toast.makeText(SearchActivity.this,
                            R.string.msg_query_short,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
            return false;
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initAdapter() {
        adapter = new ArticleAdapter();
        adapter.setOnItemClickListener(this::readArticle);
        adapter.setOnItemInsertListener(article -> {
            viewModel.insertIntoDb(article);
            showInsertSnackbar(article);
        });
    }

    private void readArticle(Article article) {
        String url = article.getUrl();
        Intent intent = ReaderActivity.newIntent(this, url);
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

    private void updateAdapter(List<Article> articles) {
        adapter.submitList(articles);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        txtSearchQuery.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        txtSearchQuery.setVisibility(View.VISIBLE);
    }
}