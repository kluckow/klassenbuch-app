package de.hhbk.app.main.activity;

import android.support.v7.app.AppCompatActivity;

import android.os.Process;

/**
 * Created by Markus on 03.11.2016.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
}
