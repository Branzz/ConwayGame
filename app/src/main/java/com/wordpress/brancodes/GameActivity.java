package com.wordpress.brancodes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;
import java.util.Random;

public class GameActivity extends DefaultActivity implements SharedPrefAccess {

	private static final Random RANDOM = new Random();

	private static boolean levelsGenerated = false;
	private static Level[] levels;
	private static Level[] tutorialLevels;
	private static int maxLevelAmount;

	private Level[] currentLevels;
	private boolean tutorialMode;
	private boolean randomLevelMode;
	private int endLevelNumber;
	private int currentLevelNumber;
	private GameBoardFragment gameBoard;
	private boolean foundAnswer = false;
	private boolean helpMenuVisible = false;
	private boolean coloredCellsClickable;
	private SharedPreferences sharedPrefs;
	private String origination;

	public GameActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		MobileAds.initialize(this, System.out::println);
		AdView bottomBannerAd = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		bottomBannerAd.loadAd(adRequest);

		sharedPrefs = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		if (!levelsGenerated)
			generateLevels();

		tutorialMode = getIntent().getBooleanExtra("tutorialMode", false);
		if (!tutorialMode)
			randomLevelMode = getIntent().getBooleanExtra("randomLevels", false);
		coloredCellsClickable = getIntent().getBooleanExtra("coloredCellsClickable", true);

		origination = getIntent().getStringExtra("origination");
		final Button returnButton = findViewById(R.id.returnButton);
		if (Objects.equals(origination, "levelActivity")) {
			returnButton.setBackgroundResource(R.drawable.return_button);
		} else {
			returnButton.setBackgroundResource(R.drawable.menu_select_button);
		}
		findViewById(R.id.helpButton).setBackgroundResource(R.drawable.help_button);

		// findViewById(R.id.helpMenu).setClipToOutline(true);
		currentLevels = tutorialMode ? tutorialLevels : levels;
		endLevelNumber = currentLevels.length - 1;

		int sentLevelNum = getIntent().getIntExtra("levelNumber", -1);
		if (sentLevelNum != -1)
			currentLevelNumber = sentLevelNum;
		else if (tutorialMode)
			currentLevelNumber = 0;
		else
			currentLevelNumber = getSavedCurrentLevel();

		if (randomLevelMode)
			loadRandomLevel();
		else
			loadLevel(currentLevels[currentLevelNumber]);
	}

	private <T> T def(T value, T default0) {
		return value == null ? default0 : value;
	}

	private void generateLevels() {
		levels = getLevels(R.string.level_data);
		tutorialLevels = getLevels(R.string.tutorial_level_data);
		maxLevelAmount = levels.length;
		levelsGenerated = true;
	}

	private Level[] getLevels(int levelDataStringID) {
		String[] levelData = getString(levelDataStringID).replaceAll("[\\s]", "").split(";");
		Level[] levels = new Level[levelData.length];
		for (int i = 0; i < levels.length; i++)
			levels[i] = new Level(levelData[i].split(","));
		return levels;
	}

	private void loadRandomLevel() {
		int size = RANDOM.nextInt(5) + 4;
		// loadLevel(new Level(size + RANDOM.nextInt(2) - 1, size + RANDOM.nextInt(2) - 1, coloredCellsClickable));
		loadLevel(new Level(4, 4, coloredCellsClickable));
	}

	private void loadLevel(Level level) {
		// System.out.printf("%d, %d, %s\n", level.getRows(), level.getColumns(), Arrays.toString(level.getTileData()));
		gameBoard = GameBoardFragment.newInstance(level.getRows(), level.getColumns(), level.getTileData(), coloredCellsClickable);
		getSupportFragmentManager().beginTransaction()
								   .replace(R.id.fragment_game_board, gameBoard)
								   .commit();
		// View frag = findViewById(R.id.fragment_game_board);
		// System.out.println(frag.getClass().getName());
		ConstraintSet constraintSet = new ConstraintSet();
		// final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) frag.getLayoutParams();
		final ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
		constraintSet.clone(gameLayout);
		constraintSet.setDimensionRatio(R.id.fragment_game_board, String.valueOf((double) level.getColumns() / level.getRows()));
		constraintSet.applyTo(gameLayout);

		if (!tutorialMode)
			((TextView) findViewById(R.id.levelNumberText)).setText(String.valueOf(currentLevelNumber + 1));

		// ======= level testing: ======= TODO

		System.out.println(level);

	}

	public int getSavedCurrentLevel() {
		return sharedPrefs.getInt("currentLevel", 0);
	}

	public void setCurrentLevel(int currentLevel) {
		sharedPrefs.edit()
				   .putInt("currentLevel", currentLevel)
				   .apply();
	}

	// public void buttonPanelClick(View v) {
	// 	Button button = (Button) v;
	// 	// int buttonId = button.getId();
	// 	// for (int i = 0; i < buttonsWidth; i++)
	// 	// 	for (int j = 0; j < buttonsHeight; j++)
	//
	// }

	public void runButtonClicked(View v) {
		Button runButton = (Button) v;
		if (foundAnswer) {
			runButton.setTextSize(40.0F);
			runButton.setText(R.string.run);
			runButton.setBackgroundColor(getResources().getColor(R.color.green));
			foundAnswer = false;
			gameBoard.stopStepAnimation();
			if (randomLevelMode)
				loadRandomLevel();
			else {
				currentLevelNumber++;
				if (currentLevelNumber == maxLevelAmount)
					endGame();
				else {
					if (currentLevelNumber - 1 >= getSavedCurrentLevel())
						setCurrentLevel(currentLevelNumber);
					loadLevel(currentLevels[currentLevelNumber]);
				}
			}
		} else {
			if (currentLevelNumber > endLevelNumber)
				finish();
			if (gameBoard.checkSolution()) {
				runButton.setTextSize(26.0F);
				runButton.setText(R.string.continueButton);
				runButton.setBackgroundColor(getResources().getColor(R.color.blueBell));
				gameBoard.beginStepAnimation();
				foundAnswer = true;
			}
		}
	}

	private void endGame() {
		setCurrentLevel(0);
		finish();
	}

	public void helpButtonClicked(View v) {
		// PopupMenu helpMenu = new PopupMenu(this, v);
		// helpMenu.setOnMenuItemClickListener();
		// helpMenu.inflate(R.menu.help_menu);
		// helpMenu.show();
		// System.out.println("help button clicked");
		// HelpMenuFragment helpMenuFragment = HelpMenuFragment.newInstance(5);
		// getSupportFragmentManager().beginTransaction()
		// 						   .replace(R.id.fragment_help_popup, helpMenuFragment)
		// 						   .commit();
		// final ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
		// gameLayout.setClickable(true);
		// gameLayout.setOnClickListener(l -> {
		// 	l.setClickable(false);
		// 	getSupportFragmentManager().beginTransaction()
		// 							   .remove(helpMenuFragment)
		// 							   .commit();
		//
		// });
		final ConstraintLayout helpMenu = findViewById(R.id.helpMenu);
		// helpMenu.bringToFront();
		// final ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
		// gameLayout.bringChildToFront(helpMenu);
		// helpMenu.setConstraintSet(ConstraintSet.);
		if (helpMenuVisible) {
			helpMenu.setVisibility(View.INVISIBLE);
			gameBoard.resetClickableButtons();
			findViewById(R.id.runButton).setClickable(true);
			// gameLayout.setClickable(false);
			helpMenuVisible = false;
		} else {
			helpMenu.setVisibility(View.VISIBLE);
			gameBoard.deClickableButtons();
			findViewById(R.id.runButton).setClickable(false);
			// gameLayout.setClickable(true);
			// gameLayout.setOnClickListener(this::helpButtonClicked);
			helpMenuVisible = true;
		}
	}

	public void returnButtonClicked(View v) {
		finish();
	}

	// public void closeHelpMenu(View v) {
	//
	// }

}
