package com.example.goran.mvvm_demo.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter;
import com.example.goran.mvvm_demo.ui.adapters.ArticleAdapter.ClickListener;
import com.example.goran.mvvm_demo.ui.archive.ArchiveActivity;
import com.example.goran.mvvm_demo.ui.article.ArticleActivity;

public class MainActivity extends AppCompatActivity {

    private ArticleAdapter adapter;
    private MainViewModel viewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new ArticleAdapter(this);
        adapter.setListener(new ClickListener() {
            @Override
            public void onClick(String articleUrl) {
                Intent intent = ArticleActivity.newIntent(MainActivity.this, articleUrl);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Article article) {
                try {
                    viewModel.insert(article);
                    showInsertSnackbar(article);
                } catch (SQLiteConstraintException e) {
                    showErrorToast();
                }
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getArticlesFromWeb().observe(this, articles -> {
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void showInsertSnackbar(final Article article) {
        Snackbar.make(recyclerView, "Article archived.", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> viewModel.delete(article))
                .show();
    }

    private void showErrorToast() {
        Toast.makeText(MainActivity.this, "Already in archive!", Toast.LENGTH_SHORT).show();
    }
}
