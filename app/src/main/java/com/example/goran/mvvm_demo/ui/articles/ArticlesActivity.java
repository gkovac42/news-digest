package com.example.goran.mvvm_demo.ui.articles;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter.AdapterListener;

public class ArticlesActivity extends BaseActivity {

    private static final String EXTRA_SOURCE_ID = "source_id";
    private static final String EXTRA_SOURCE_NAME = "source_name";

    private ArticlesViewModel viewModel;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private String sourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        setStatusBarColor(R.color.colorRedDark);
        setActionBarColor(R.color.colorRed);

        String sourceName = getIntent().getStringExtra(EXTRA_SOURCE_NAME);
        getSupportActionBar().setTitle(sourceName);

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        sourceId = getIntent().getStringExtra(EXTRA_SOURCE_ID);

        getDataFromViewModel(sourceId);

        initSwipeRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.articles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_archive) {
            startActivity(ArchiveActivity.newIntent(this));
        }
        return false;
    }

    private void initAdapter() {
        adapter = new ArticleAdapter();
        adapter.setListener(new AdapterListener() {
            @Override
            public void onClick(Article article) {
                Intent intent = ReaderActivity.newIntent(ArticlesActivity.this, article.getUrl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(Article article) {
                try {
                    viewModel.insertIntoDb(article);
                    showInsertSnackbar(article);

                } catch (SQLiteConstraintException e) {
                    showErrorToast();
                }
            }
        });
    }

    private void showInsertSnackbar(final Article article) {
        Snackbar.make(recyclerView, R.string.msg_archived, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> viewModel.deleteFromDb(article))
                .show();
    }

    private void showErrorToast() {
        Toast.makeText(ArticlesActivity.this, R.string.msg_already_archived, Toast.LENGTH_SHORT)
                .show();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_articles);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getDataFromViewModel(String sourceId) {
        viewModel.getArticlesFromApi(sourceId).observe(this, articles -> {
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
            stopRefreshing();
        });
    }

    private void initSwipeRefresh() {
        swipeLayout = findViewById(R.id.container_swipe_refresh);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeLayout.setOnRefreshListener(() -> getDataFromViewModel(sourceId));
        swipeLayout.setRefreshing(true);
    }

    private void stopRefreshing() {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
