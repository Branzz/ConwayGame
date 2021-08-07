package com.wordpress.brancodes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.gridlayout.widget.GridLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LevelSelectActivity extends DefaultActivity implements SharedPrefAccess {

	final static int COLUMN_WIDTH = 5;

	private final Map<Button, Integer> buttonMap = new HashMap<>(); // to track which button was clicked
	private SharedPreferences sharedPrefs;
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_select);
		sharedPrefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		String[] levelData = getString(R.string.level_data).replaceAll("[\\s]", "").split(";");
		currentLevel = getSavedCurrentLevel();
		// Level[] levels = new Level[levelData.length];
		// for (int i = 0; i < levels.length; i++)
		// 	levels[i] = new Level(levelData[i].split(","));
		GridLayout grid = findViewById(R.id.levelSelectGrid);
		grid.removeAllViews();
		grid.removeAllViewsInLayout();

		ConstraintSet constraintSet = new ConstraintSet();
		// final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) frag.getLayoutParams();
		final ConstraintLayout levelSelectConstraint = findViewById(R.id.levelSelectConstraintLayout);
		constraintSet.clone(levelSelectConstraint);
		constraintSet.setDimensionRatio(R.id.levelSelectGrid, String.valueOf((double) COLUMN_WIDTH / (1 + levelData.length / COLUMN_WIDTH)));
		constraintSet.applyTo(levelSelectConstraint);

		for (int i = 0; i < levelData.length; i++) {
			final Button button = (Button) LayoutInflater.from(getBaseContext()).inflate(R.layout.button_template, null);
			grid.addView(button);
			buttonMap.put(button, i);
			final GridLayout.LayoutParams layoutParams = ((GridLayout.LayoutParams) button.getLayoutParams());
			layoutParams.height = layoutParams.width = 0;
			layoutParams.rowSpec    = GridLayout.spec(i / COLUMN_WIDTH, GridLayout.FILL, 1.0F);
			layoutParams.columnSpec = GridLayout.spec(i % COLUMN_WIDTH, GridLayout.FILL, 1.0F);
			button.setLayoutParams(layoutParams);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0F);
			button.setText(String.valueOf(i + 1));
			// button.setId(2_100_000 + i);
			button.setVisibility(View.VISIBLE);
		}
		reColorTiles();
		if (currentLevel == 0) // Enter first level if none were cleared yet
			buttonPanelClick(0);
	}

	private void reColorTiles() {
		int currentBestLevel = getSavedCurrentLevel();
		for (final Map.Entry<Button, Integer> buttonEntry : buttonMap.entrySet()) {
			final Button button = buttonEntry.getKey();
			final int buttonIndex = buttonEntry.getValue();
			final Tile tileType = buttonIndex == currentBestLevel ? Tile.TO_EMPTY_EMPTY : buttonIndex < currentBestLevel ? Tile.EMPTY : Tile.FILLED;
			button.setBackgroundColor(tileType.color());
			button.setTextColor(tileType.textColor());
			if (buttonIndex <= currentBestLevel) {
				button.setOnClickListener(v -> buttonPanelClick(buttonMap.get(v)));
				button.setClickable(true);
			} else {
				button.setClickable(false);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		if (currentLevel != getSavedCurrentLevel())
			reColorTiles();
	}

	public void buttonPanelClick(int buttonNumber) {
		// if (buttonNumber <= currentLevel) {
			final Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra("levelNumber", buttonNumber);
			startActivity(intent);
		// }
	}

	public int getSavedCurrentLevel() {
		return sharedPrefs.getInt("currentLevel", 0);
	}

	public void setCurrentLevel(int currentLevel) {
		sharedPrefs.edit()
				   .putInt("currentLevel", currentLevel)
				   .apply();
	}

}
