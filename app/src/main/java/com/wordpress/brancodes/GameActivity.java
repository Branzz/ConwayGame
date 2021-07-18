package com.wordpress.brancodes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends DefaultActivity {

	private static final Random random = new Random();

	private Level[] levels;
	private int endLevelNumber;
	private int currentLevelNumber;
	private GameBoardFragment gameBoard;
	private boolean foundAnswer;

	public GameActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		final String[] levelData = getString(R.string.level_data).replaceAll("[\\s]", "").split(";");
		levels = new Level[levelData.length];
		for (int i = 0; i < levels.length; i++)
			levels[i] = new Level(levelData[i].split(","));
		endLevelNumber = levels.length - 1;
		currentLevelNumber = 3; // TODO store data
		foundAnswer = false;
		loadLevel(levels[currentLevelNumber]);
	}

	private void loadLevel(Level level) {
		System.out.printf("%d, %d, %s\n", level.getRows(), level.getColumns(), Arrays.toString(level.getTileData()));
		gameBoard = GameBoardFragment.newInstance(level.getRows(), level.getColumns(), level.getTileData());
		getSupportFragmentManager().beginTransaction()
								   .replace(R.id.fragment_game_board, gameBoard)
								   .commit();
		View frag = findViewById(R.id.fragment_game_board);
		System.out.println(frag.getClass().getName());
		ConstraintSet constraintSet = new ConstraintSet();
		// final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) frag.getLayoutParams();
		// cS.
		final ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
		constraintSet.clone(gameLayout);
		constraintSet.setDimensionRatio(R.id.fragment_game_board, String.valueOf((double) level.getColumns() / level.getRows()));
		constraintSet.applyTo(gameLayout);
	}

	public void buttonPanelClick(View v) {
		Button button = (Button) v;
		// int buttonId = button.getId();
		// for (int i = 0; i < buttonsWidth; i++)
		// 	for (int j = 0; j < buttonsHeight; j++)

	}

	public void runButtonClicked(View v) {
		if (foundAnswer) {
			Button runButton = (Button) v;
			runButton.setTextSize(24.0F);
			runButton.setText(R.string.run);
			runButton.setBackgroundColor(getResources().getColor(R.color.runColor));
			foundAnswer = false;
			gameBoard.stopStepAnimation();
			loadLevel(levels[++currentLevelNumber]);
		} else {
			if (currentLevelNumber > endLevelNumber)
				finish();
			if (gameBoard.checkSolution()) {
				Button runButton = (Button) v;
				runButton.setTextSize(20.0F);
				runButton.setText(R.string.continueButton);
				runButton.setBackgroundColor(Color.CYAN);
				gameBoard.beginStepAnimation();
				foundAnswer = true;
			}
		}
	}

	public void helpButtonClicked(View v) {

	}

}
