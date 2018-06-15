package com.example.goran.mvvm_demo.ui.articles;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.adapters.SimpleArticleAdapter;
import com.example.goran.mvvm_demo.ui.BaseActivity;

public class ArchiveActivity extends BaseActivity {

    private ArticlesViewModel viewModel;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;

    public static Intent newIntent(Context context) {
        return new Intent(context, ArchiveActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        setActionBarColor(R.color.colorBlue);
        setStatusBarColor(R.color.colorBlueDark);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.container_swipe_refresh);
        swipeRefreshLayout.setEnabled(false);

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        getArticlesFromDb();
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
                    getArticlesFromDb();
                }
                return false;
            }
        });
        return true;
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

        recyclerView = findViewById(R.id.recycler_articles);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getArticlesFromDb() {
        viewModel.getArticlesFromDb().observe(this, articles -> {
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        });
    }

    private void performSearch(String query) {
        viewModel.searchDbByTitle(query)
                .observe(ArchiveActivity.this, articles -> {
                    adapter.setArticles(articles);
                    adapter.notifyDataSetChanged();
                });
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
