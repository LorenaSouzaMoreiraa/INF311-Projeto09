package com.example.inf311_projeto09;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.ComponentActivity;

import com.example.inf311_projeto09.ui.MyComposeLauncher;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyComposeLauncher.launch(this);

        final View decorView = this.getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                if (!insets.isVisible(WindowInsets.Type.ime())) {
                    this.hideSystemUI();
                }
                return v.onApplyWindowInsets(insets);
            });
        } else {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                final Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                final int screenHeight = decorView.getRootView().getHeight();
                final int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight < screenHeight * 0.15) {
                    this.hideSystemUI();
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.hideSystemUI();
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = this.getWindow().getInsetsController();
            if (insetsController != null) {
                this.getWindow().getInsetsController().hide(WindowInsets.Type.systemBars());
                this.getWindow().getInsetsController().setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            final int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }
}