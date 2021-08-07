package com.wordpress.brancodes;

import android.os.*;
import android.util.TypedValue;
import android.view.*;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Use the {@link GameBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameBoardFragment extends Fragment {

	private static final String ROWS = "rows";
	private static final String COLUMNS = "columns";
	private static final String BOARD_DATA = "boardData";
	private static final String ALL_CLICKABLE = "coloredCellsClickable";

	public static final long FRAME_LENGTH = 1_100; // milliseconds
	private HandlerThread handlerThread;
	private Looper looper;
	private Handler handler;
	private Runnable stepNextBoard;

	private final boolean showNeighborCount = true;

	private int rows;
	private int columns;
	private int[] boardData;
	private boolean coloredCellsClickable;
	private GameButton[][] gameButtons;
	private final Map<View, GameButton> buttonMap = new HashMap<>(); // to track which button was clicked
	private boolean stepAnimation;

	public GameBoardFragment() {
		// Required empty public constructor
	}

	public static GameBoardFragment newInstance(int rows, int columns, int[] boardData, boolean coloredCellsClickable) {
		GameBoardFragment fragment = new GameBoardFragment();
		Bundle args = new Bundle();
		args.putInt(ROWS, rows);
		args.putInt(COLUMNS, columns);
		args.putIntArray(BOARD_DATA, boardData);
		args.putBoolean(ALL_CLICKABLE, coloredCellsClickable);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			rows = getArguments().getInt(ROWS);
			columns = getArguments().getInt(COLUMNS);
			boardData = getArguments().getIntArray(BOARD_DATA);
			coloredCellsClickable = getArguments().getBoolean(ALL_CLICKABLE);
			gameButtons = new GameButton[rows][columns];
			stepAnimation = false;
			handlerThread = new HandlerThread("gameBoardStepAnimation", 3);
			handlerThread.start();
			looper = handlerThread.getLooper();
			stepNextBoard = () -> {
				while (stepAnimation) {
					System.out.println("animating...");
					setBoard(getNextBoard());
					for (int r = 0; r < rows; r++)
						for (int c = 0; c < columns; c++)
							gameButtons[r][c].refreshColor();
					try {
						Thread.sleep(FRAME_LENGTH);
					} catch (InterruptedException e) {
						break;
					}
				}
			};

			handler = new Handler(looper);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_board, container, false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final GridLayout grid = view.findViewById(R.id.grid);
		grid.removeAllViews();
		grid.removeAllViewsInLayout();
		// grid.setRowCount(rows);
		// grid.setColumnCount(columns);
		final Tile[] tileValues = Tile.values();
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				final int minLength = Math.min(rows, columns);
				final Button button = (Button) LayoutInflater.from(getContext()).inflate(minLength >= 6 ? R.layout.button_small_template : R.layout.button_template, null);
				grid.addView(button);
				final GridLayout.LayoutParams layoutParams = ((GridLayout.LayoutParams) button.getLayoutParams());
				layoutParams.height = layoutParams.width = 0;
				layoutParams.rowSpec    = GridLayout.spec(r, GridLayout.FILL, 1.0F);
				layoutParams.columnSpec = GridLayout.spec(c, GridLayout.FILL, 1.0F);
				// layoutParams.setGravity(Gravity.FILL);
				button.setLayoutParams(layoutParams);
				// WindowInsets buttonInsets = new WindowInsets()
				// button.getRootWindowInsets().inset(0,0,0,0); // TODO insets
				// final int inset = (int) Math.pow(2, (2.8) - minLength / 3.0);
				// setInsets(button, inset);
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) Math.pow(2, (67.0 / 12.0) - minLength / 4.25));
				// System.out.println((float) Math.pow(2, 67.0 / 12.0 - (rows + columns) / 2.0 / 3.0));
				final Tile tile = tileValues[boardData[r * columns + c]];
				final GameButton gameButton = new GameButton(button, tile, r, c);
				gameButtons[r][c] = gameButton;
				buttonMap.put(button, gameButton);
				// button.setId(2_000_000 + r * columns + c);
			}
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (gameButtons[r][c].getTileType().isFilled())
					changeNeighborCount(r, c, true);
		if (showNeighborCount)
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < columns; c++) {
					gameButtons[r][c].updateText();
				}
		resetClickableButtons();
	}

	// private void setInsets(View v, int inset) {
	// 	try {
	// 		Insets k = v.getOpticalInsets();
	// 		Field insetField = View.class.getDeclaredField("mLayoutInsets");
	// 		insetField.setAccessible(true);
	// 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
	// 			insetField.set(v, Insets.of(new Rect(inset, inset, inset, inset)));
	// 		}
	// 	} catch (NoSuchFieldException | IllegalAccessException e) {
	// 		e.printStackTrace();
	// 	}
	// }

	public void buttonPanelClick(GameButton gameButton) {
		gameButton.click();
		changeNeighborCount(gameButton.getX(), gameButton.getY(), gameButton.getTileType().isFilled());
	}

	private void changeNeighborCount(int row, int column, boolean increase) {
		for (int r = row - 1; r <= row + 1 && r < rows; r++)
			for (int c = column - 1; c <= column + 1 && c < columns; c++) {
				if (r >= 0 && c >= 0 && !(r == row && c == column)) {
					gameButtons[r][c].changeNeighborCount(increase);
					if (!stepAnimation && showNeighborCount)
						gameButtons[r][c].updateText();
				}
			}
	}

	private boolean[][] getNextBoard() {
		final boolean[][] nextBoard = new boolean[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				int count = getTileRanged(r - 1, c - 1) + getTileRanged(r, c - 1) + getTileRanged(r + 1, c - 1)
							+ getTileRanged(r - 1, c) + getTileRanged(r + 1, c) + getTileRanged(r - 1, c + 1)
							+ getTileRanged(r, c + 1) + getTileRanged(r + 1, c + 1);
				nextBoard[r][c] = count == 3 || (count == 2 && getTileRanged(r, c) == 1);
			}
		return nextBoard;
	}

	private void setBoard(boolean[][] nextBoard) {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				gameButtons[r][c].setTileType(nextBoard[r][c] ? Tile.FILLED : Tile.EMPTY);
	}

	private int getTileRanged(int r, int c) {
		return r >= 0 && r <= rows - 1 && c >= 0 && c <= columns - 1 && gameButtons[r][c].getTileType().isFilled() ? 1 : 0;
	}

	public boolean checkSolution() {
		boolean[][] nextBoard = getNextBoard();
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (!gameButtons[r][c].getTileType().isCorrect(nextBoard[r][c]))
					return false;
		return true;
	}

	public void beginStepAnimation() {
		stepAnimation = true;
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				final GameButton gameButton = gameButtons[r][c];
				gameButton.removeText();
				gameButton.getButton().setClickable(true);
				if (!(gameButton.getTileType() == Tile.EMPTY || gameButton.getTileType() == Tile.FILLED))
					gameButton.getButton().setOnClickListener(v -> buttonPanelClick(buttonMap.get(v)));
			}
		handler.post(stepNextBoard);
		// Runnable setNextBoard = null;
		// setNextBoard = () -> {
		// 	while (true) {
		// 		setBoard(getNextBoard());
		// 		try {
		// 			Thread.sleep(FRAME_LENGTH);
		// 		} catch (InterruptedException e) {
		// 			break;
		// 		}
		// 		// handler.post(setNextBoard);
		// 	}
		// };
	}

	public void stopStepAnimation() {
		handler.removeCallbacks(stepNextBoard);
		stepAnimation = false;
	}

	public void resetClickableButtons() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				final Button button = gameButtons[r][c].getButton();
				final Tile tile = gameButtons[r][c].getTileType();
				if (coloredCellsClickable || stepAnimation || tile == Tile.EMPTY || tile == Tile.FILLED) {
					button.setOnClickListener(v -> buttonPanelClick(buttonMap.get(v)));
					button.setClickable(true);
				} else {
					button.setClickable(false);
					// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					// 	button.setFocusable(View.NOT_FOCUSABLE);
					// }
				}
			}
	}

	public void deClickableButtons() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				gameButtons[r][c].getButton().setClickable(false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (handlerThread != null) {
			handlerThread.quit();
		}
		if (looper != null) {
			looper.quit();
		}
		// if (handler != null) {
		// 	handler.
		// }
	}

	@Override
	public void onStop() {
		super.onStop();
		if (handlerThread != null) {
			handlerThread.quit();
		}
		if (looper != null) {
			looper.quit();
		}
	}

}
