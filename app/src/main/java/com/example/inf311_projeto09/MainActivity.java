package com.example.inf311_projeto09;

import android.os.Bundle;

import androidx.activity.ComponentActivity;

import com.example.inf311_projeto09.ui.MyComposeLauncher;
import com.example.inf311_projeto09.ui.ScreenType;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyComposeLauncher.launch(this, ScreenType.WELCOME);
    }
}
