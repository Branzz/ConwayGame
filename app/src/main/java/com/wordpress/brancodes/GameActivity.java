package com.wordpress.brancodes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class GameActivity extends DefaultActivity {

	private static final Random random = new Random();

	private Level[] levels;
	private Level currentLevel;
	private int currentLevelNumber;
	private int buttonsWidth;
	private int buttonsHeight;
	private Button[][] buttons;
	private int[][] buttonIds;
	private boolean[][] buttonStates = new boolean[buttonsWidth][buttonsHeight];

	public GameActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		buttons = new Button[buttonsHeight][buttonsWidth];
		for (int i = 0; i < buttonsWidth; i++)
			for (int j = 0; j < buttonsHeight; j++)
				buttons[i][j] = findViewById(buttonIds[i][j]);
		final String[] levelData = getString(R.string.level_data).replaceAll("[\\s]", "").split(";");
		levels = new Level[levelData.length];
		for (int i = 0; i < levels.length; i++)
			levels[i] = new Level(levelData[i].split(","));
	}

	public void buttonPanelClick(View v) {
		Button button = (Button) v;
		// int buttonId = button.getId();
		// for (int i = 0; i < buttonsWidth; i++)
		// 	for (int j = 0; j < buttonsHeight; j++)

	}

	public void runButtonClicked(View v) {
		Button button = (Button) v;

	}

}
