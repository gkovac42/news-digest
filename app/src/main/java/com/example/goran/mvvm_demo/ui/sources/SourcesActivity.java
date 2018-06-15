package com.example.goran.mvvm_demo.ui.sources;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Source;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.SourceAdapter;
import com.example.goran.mvvm_demo.ui.articles.ArticlesActivity;

import java.util.List;

public class SourcesActivity extends BaseActivity {

    private SourceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setActionBarColor(R.color.colorRed);
        setStatusBarColor(R.color.colorRedDark);

        initAdapter();

        initRecyclerView();

        SourcesViewModel viewModel = ViewModelProviders.of(this).get(SourcesViewModel.class);
        viewModel.getSourcesFromApi().observe(this, this::updateAdapter);
    }

    private void initAdapter() {
        adapter = new SourceAdapter();
        adapter.setListener(this::navigateToMainActivity);
    }

    private void updateAdapter(List<Source> sources) {
        ProgressBar progressBar = findViewById(R.id.progress_list);
        progressBar.setVisibility(View.GONE);

        adapter.setSources(sources);
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void navigateToMainActivity(Source source) {
        Intent intent = new Intent(this, ArticlesActivity.class);
        intent.putExtra("source_id", source.getId());
        intent.putExtra("source_name", source.getName());
        startActivity(intent);
    }
}
