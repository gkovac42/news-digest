package com.example.goran.mvvm_demo.ui.sources;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.data.model.Source;
import com.example.goran.mvvm_demo.ui.BaseActivity;
import com.example.goran.mvvm_demo.ui.adapters.SourceAdapter;
import com.example.goran.mvvm_demo.ui.articles.ArticlesActivity;
import com.example.goran.mvvm_demo.util.Category;
import com.example.goran.mvvm_demo.util.ErrorCodes;

import java.util.List;

public class SourcesActivity extends BaseActivity {

    private SourceAdapter adapter;
    private SourcesViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        setActionBarColor(R.color.colorRed);
        setStatusBarColor(R.color.colorRedDark);

        progressBar = findViewById(R.id.progress_sources);

        initSpinner();

        initAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(SourcesViewModel.class);

        viewModel.getSourcesFromApi().observe(this, sources -> {
            updateAdapter(sources);
            hideProgressBar();
        });

        viewModel.getErrorCode().observe(this, errorCode -> {
            if (errorCode != null && errorCode == ErrorCodes.NETWORK_ERROR) {
                showNetworkError();
                hideProgressBar();
            }
        });
    }

    private void initSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Category.getCategories());

        Spinner spinner = findViewById(R.id.spinner_sources);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = adapter.getItem(i);
                viewModel.getSourcesByCategory(category)
                        .observe(SourcesActivity.this, SourcesActivity.this::updateAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initAdapter() {
        adapter = new SourceAdapter();
        adapter.setListener(this::navigateToMainActivity);
    }

    private void updateAdapter(List<Source> sources) {
        adapter.submitList(sources);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        RecyclerView recyclerView = findViewById(R.id.recycler_sources);
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

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
