package com.example.goran.mvvm_demo.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.goran.mvvm_demo.R;

public class BaseActivity extends AppCompatActivity {

    public void setActionBarColor(int colorRes) {

        ColorDrawable cd = new ColorDrawable(getResources().getColor(colorRes));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(cd);
        }
    }

    public void setStatusBarColor(int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(colorRes));
        }
    }

    public void showNetworkError() {
        Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show();
    }
}
