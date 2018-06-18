package com.example.goran.mvvm_demo.ui.archive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.adapters.SimpleArticleAdapter;
import com.example.goran.mvvm_demo.ui.articles.ReaderActivity;

import java.util.List;

public class ArchiveActivity extends BaseActivity {

    private ArchiveViewModel viewModel;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;

    public static Intent newIntent(Context context) {
        return new Intent(context, ArchiveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setActionBarColor(R.color.colorBlue);
        setStatusBarColor(R.color.colorBlueDark);

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel.class);
        viewModel.getArticlesFromDb().observe(this, articles -> {
            ProgressBar progressBar = findViewById(R.id.progress_list);
            progressBar.setVisibility(View.GONE);
            updateAdapter(articles);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.archive, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.archive_menu_search);

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = searchView.getQuery().toString();

                if (query.length() > 2) {
                    performSearch(query);

                } else if (query.isEmpty()) {
                    viewModel.getArticlesFromDb()
                            .observe(ArchiveActivity.this, ArchiveActivity.this::updateAdapter);
                }
                return false;
            }
        });
        return true;
    }

    private void performSearch(String query) {
        viewModel.searchDbByTitle(query).observe(this, this::updateAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {
            showClearDialog();
        }
        return true;
    }

    private void initAdapter() {
        adapter = new SimpleArticleAdapter();
        adapter.setListener(new ArticleAdapter.AdapterListener() {
            @Override
            public void onClick(Article article) {
                navigateToArticle(article.getUrl());
            }

            @Override
            public void onLongClick(Article article) {
                viewModel.deleteFromDb(article);
                showDeleteSnackbar(article);
            }
        });
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

    private void updateAdapter(List<Article> articles) {
        adapter.submitList(articles);
    }

    private void navigateToArticle(String articleUrl) {
        Intent intent = ReaderActivity.newIntent(ArchiveActivity.this, articleUrl);
        startActivity(intent);
    }

    private void showDeleteSnackbar(final Article article) {
        Snackbar.make(recyclerView, R.string.msg_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> viewModel.insertIntoDb(article))
                .show();
    }

    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Caution!")
                .setMessage(R.string.msg_clear)
                .setPositiveButton(R.string.action_yes, (dialogInterface, i) -> viewModel.clearDb())
                .setNegativeButton(R.string.action_no, null)
                .create()
                .show();
    }
}
