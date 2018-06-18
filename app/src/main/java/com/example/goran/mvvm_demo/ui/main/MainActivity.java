package com.example.goran.mvvm_demo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.goran.mvvm_demo.R;
import com.example.goran.mvvm_demo.ui.archive.ArchiveActivity;
import com.example.goran.mvvm_demo.ui.search.SearchActivity;
import com.example.goran.mvvm_demo.ui.sources.SourcesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.card_news).setOnClickListener(
                view -> startActivity(new Intent(this, SourcesActivity.class)));

        findViewById(R.id.card_archive).setOnClickListener(
                view -> startActivity(new Intent(this, ArchiveActivity.class)));

        findViewById(R.id.card_search).setOnClickListener(
                view -> startActivity(new Intent(this, SearchActivity.class)));
    }
}
