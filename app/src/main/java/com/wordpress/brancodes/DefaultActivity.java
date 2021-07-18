package com.wordpress.brancodes;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public abstract class DefaultActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpNavBar();
	}

	@Override
	public void onWindowFocusChanged(final boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (MainActivity.API_VERSION >= Build.VERSION_CODES.KITKAT) {
			hideNavBar();
		}
	}

	private static final int DEFAULT_VIEW_FLAGS = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

	private void setUpNavBar() {
		if (MainActivity.API_VERSION >= Build.VERSION_CODES.KITKAT) {
			hideNavBar();
			getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					hideNavBar();
				}
			});
		}

	}

	private void hideNavBar() {
		getWindow().getDecorView().setSystemUiVisibility(DEFAULT_VIEW_FLAGS);
	}

}
