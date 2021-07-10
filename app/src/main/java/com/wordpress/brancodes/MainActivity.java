package com.wordpress.brancodes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends DefaultActivity {

	public static final int API_VERSION = Build.VERSION.SDK_INT;

	private static final int defaultBackgroundColor = Color.rgb(63, 74, 78);

	public MainActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void launchGame(View v) {
		final Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("A", "k");
		startActivity(intent);
	}

}
