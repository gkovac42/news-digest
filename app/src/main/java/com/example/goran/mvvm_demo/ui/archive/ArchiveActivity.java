package com.example.goran.mvvm_demo.ui.archive;

import android.arch.lifecycle.ViewModelProviders;
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

public class ArchiveActivity extends BaseActivity {

    public static final int MIN_QUERY_LENGTH = 3;

    private ArchiveViewModel viewModel;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel.class);
        viewModel.getArticlesFromDb().observe(this, articles -> {
            ProgressBar progressBar = findViewById(R.id.progress_list);
            progressBar.setVisibility(View.GONE);
            adapter.submitList(articles);
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
                processQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = searchView.getQuery().toString();
                processQuery(query);
                return false;
            }
        });
        return true;
    }

    private void processQuery(String query) {
        if (query.length() >= MIN_QUERY_LENGTH) {
            viewModel.searchDbByTitle(query).observe(this,
                    articles -> adapter.submitList(articles));

        } else if (query.isEmpty()) {
            viewModel.getArticlesFromDb()
                    .observe(ArchiveActivity.this,
                            articles -> adapter.submitList(articles));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                showClearDialog();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initAdapter() {
        adapter = new SimpleArticleAdapter();
        adapter.setOnItemClickListener(this::readArticle);
        adapter.setOnItemDeleteListener(article -> {
            viewModel.deleteFromDb(article);
            showDeleteSnackbar(article);
        });
    }

    private void readArticle(Article article) {
        String url = article.getUrl();
        Intent intent = ReaderActivity.newIntent(this, url);
        startActivity(intent);
    }

    private void showDeleteSnackbar(final Article article) {
        Snackbar.make(recyclerView, R.string.msg_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> viewModel.insertIntoDb(article))
                .show();
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

    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.msg_clear)
                .setPositiveButton(R.string.action_yes, (dialogInterface, i) -> viewModel.clearDb())
                .setNegativeButton(R.string.action_no, null)
                .create()
                .show();
    }
}
