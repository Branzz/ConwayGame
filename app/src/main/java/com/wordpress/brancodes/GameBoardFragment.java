package com.wordpress.brancodes;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	public static final long FRAME_LENGTH = 1_100; // milliseconds
	private HandlerThread handlerThread;
	private Looper looper;
	private Handler handler;
	private Runnable stepNextBoard;

	private final boolean showNeighborCount = true;

	private int rows;
	private int columns;
	private int[] boardData;
	GameButton[][] gameButtons;
	private Map<View, GameButton> buttonMap = new HashMap<>(); // to track which button was clicked
	private boolean stepAnimation;

	public GameBoardFragment() {
		// Required empty public constructor
	}

	public static GameBoardFragment newInstance(int rows, int columns, int[] boardData) {
		GameBoardFragment fragment = new GameBoardFragment();
		Bundle args = new Bundle();
		args.putInt(ROWS, rows);
		args.putInt(COLUMNS, columns);
		args.putIntArray(BOARD_DATA, boardData);
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
			gameButtons = new GameButton[rows][columns];
			stepAnimation = false;
			handlerThread = new HandlerThread("gameBoardStepAnimation", 3);
			handlerThread.start();
			looper = handlerThread.getLooper();
			stepNextBoard = () -> {
				while (stepAnimation) {
					System.out.println("oeu");
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
				// GridLayout x = (GridLayout) LayoutInflater.from(getContext()).inflate(R.layout.button_template, grid);
				Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.button_template, null);
				grid.addView(button);

				button.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(r, GridLayout.FILL, 1.0F), GridLayout.spec(c, GridLayout.FILL, 1.0F)));
				// button.getRootWindowInsets().inset(0,0,0,0);
				Tile tile = tileValues[boardData[r * columns + c]];
				GameButton gameButton = new GameButton(button, tile, r, c);
				gameButtons[r][c] = gameButton;
				buttonMap.put(button, gameButton);
				button.setId(2_000_000 + r * columns + c);
				// button.setMinWidth(0);
				// button.setMinHeight(0);
				button.setWidth(22);
				// button.setHeight(22);
				if (tile == Tile.EMPTY || tile == Tile.FILLED) {
					button.setOnClickListener(v -> buttonPanelClick(buttonMap.get(v)));
					button.setClickable(true);
				} else {
					button.setClickable(false);
				}
			}
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (gameButtons[r][c].getTileType().isFilled())
					changeNeighborCount(r, c, true);
		if (showNeighborCount)
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < columns; c++)
					gameButtons[r][c].updateText();
	}

	public void buttonPanelClick(GameButton gameButton) {
		Tile tile = gameButton.getTileType();
		if (tile == Tile.EMPTY) {
			gameButton.setTileType(Tile.FILLED);
			gameButton.refreshColor();
			changeNeighborCount(gameButton.getX(), gameButton.getY(), true);
		} else if (tile == Tile.FILLED) {
			gameButton.setTileType(Tile.EMPTY);
			gameButton.refreshColor();
			changeNeighborCount(gameButton.getX(), gameButton.getY(), false);
		}
		System.out.println(gameButton.getButton().getWidth());
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
				gameButtons[r][c].removeText();
				gameButtons[r][c].getButton().setClickable(true);
				if (!(gameButtons[r][c].getTileType() == Tile.EMPTY || gameButtons[r][c].getTileType() == Tile.FILLED))
					gameButtons[r][c].getButton().setOnClickListener(v -> buttonPanelClick(buttonMap.get(v)));
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
