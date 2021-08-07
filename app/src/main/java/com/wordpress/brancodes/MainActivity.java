package com.wordpress.brancodes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends DefaultActivity {

	public static final int API_VERSION = Build.VERSION.SDK_INT;

	public MainActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tile.generateColors(getBaseContext());
		setContentView(R.layout.activity_main);
	}

	public void launchGame(View v) {
		System.out.println("launchGame");
		final Intent intent = new Intent(this, LevelSelectActivity.class);
		startActivity(intent);
	}

	public void launchTutorial(View v) {
		System.out.println("launchTutorial");
		final Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("tutorialMode", true);
		intent.putExtra("coloredCellsClickable", false);
		startActivity(intent);
	}

	public void launchRandomLevelsGame(View v) {
		final Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("randomLevels", true);
		startActivity(intent);
	}

	public void settingsButtonClicked(View view) {
		final Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

}
