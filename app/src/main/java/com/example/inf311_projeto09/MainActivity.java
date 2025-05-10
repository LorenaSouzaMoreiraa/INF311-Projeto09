package com.example.inf311_projeto09;

import android.os.Bundle;

import androidx.activity.ComponentActivity;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyComposeLauncher.launch(this, ScreenType.CLASS_INFOS);
    }
}
