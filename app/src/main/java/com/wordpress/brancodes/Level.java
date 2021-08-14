package com.wordpress.brancodes;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.wordpress.brancodes.Tile.*;

public class Level {

	private static final Random RANDOM = new Random();

	private final Tile[][] tiles;
	private final int[] tileData;
	private final int rows;
	private final int columns;

	public Level(final Tile[][] tiles, final int rows, final int columns) {
		this.tiles = tiles;
		this.rows = rows;
		this.columns = columns;
		tileData = new int[rows * columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				tileData[r * columns + c] = tiles[r][c].ordinal();
			}
	}

	public Level(Tile[][] tiles) {
		this(tiles, tiles.length, tiles[0].length);
	}

	public Level(int rows, int columns, boolean coloredCellsClickable) { // random grid
		// this(randomGrid(rows, columns), rows, columns);
		tiles = new Tile[rows][columns];
		this.rows = rows;
		this.columns = columns;
		generateDeterministicLevel(coloredCellsClickable);
		tileData = new int[rows * columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				tileData[r * columns + c] = tiles[r][c].ordinal();
			}
	}

	public Level(final String[] levelParameters) {
		this.rows = Integer.parseInt(levelParameters[0]);
		this.columns = Integer.parseInt(levelParameters[1]);
		this.tiles = new Tile[rows][columns];
		tileData = new int[rows * columns];
		Tile[] tileValues = Tile.values();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				tiles[i][j] = tileValues[levelParameters[2].charAt(i * columns + j) - '0'];
				tileData[i * columns + j] = tiles[i][j].ordinal();
			}
	}

	public Tile[][] step() {
		Tile[][] nextState = new Tile[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				int count = 0;
				for (int i = -1; i <= 1; i++)
					for (int j = -1; j <= 1; j++)
						if (i != 0 && j != 0)
							count += getTileRanged(r + i, c + j);
				if (count == 3 || count == 2 && getTileRanged(r, c) == 1)
					nextState[r][c] = FILLED;
			}
		return nextState;
	}

	private int getTileRanged(int r, int c) {
		return r >= 0 && r <= rows - 1 && c >= 0 && c <= columns - 1 && tiles[r][c].isFilled() ? 1 : 0;
	}

	private static final double SPREAD = .2;

	public static Tile[][] randomGrid(int rows, int columns) {
		Tile[][] tiles = new Tile[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				double seed = Math.random();
				tiles[r][c] = EMPTY;
				if (seed > 1 - 4 * SPREAD)
					tiles[r][c] = TO_FILL_EMPTY;
				if (seed > 1 - 3 * SPREAD)
					tiles[r][c] = TO_FILL_FILL;
				if (seed > 1 - 2 * SPREAD)
					tiles[r][c] = TO_EMPTY_EMPTY;
				if (seed > 1 - SPREAD)
					tiles[r][c] = TO_EMPTY_FILL;
			}
		return tiles;
	}

	public void generateDeterministicLevel(boolean coloredCellsClickable) {
		do {
			boolean[][] randomFills = new boolean[rows][columns];
			for (int r = 0; r < rows; r++)
				for (int c = 0; c < columns; c++) {
					randomFills[r][c] = RANDOM.nextBoolean();
				}

			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					if (RANDOM.nextBoolean()) {
						int count = 0;
						for (int i = -1; i <= 1; i++)
							for (int j = -1; j <= 1; j++)
								if (i != 0 || j != 0)
									count += getTileRanged(r + i, c + j, randomFills);
						if (count == 3 || count == 2 && randomFills[r][c]) {
							if (coloredCellsClickable)
								// tiles[r][c] = RANDOM.nextBoolean() ? TO_FILL_FILL : TO_FILL_EMPTY;
								tiles[r][c] = TO_FILL_EMPTY;
							else
								tiles[r][c] = randomFills[r][c] ? TO_FILL_FILL : TO_FILL_EMPTY;
						} else {
							if (coloredCellsClickable)
								// tiles[r][c] = RANDOM.nextBoolean() ? TO_EMPTY_FILL : TO_EMPTY_EMPTY;
								tiles[r][c] = TO_EMPTY_EMPTY;
							else
								tiles[r][c] = randomFills[r][c] ? TO_EMPTY_FILL : TO_EMPTY_EMPTY;
						}
					} else {
						tiles[r][c] = EMPTY;
					}
				}
			}
			// System.out.println(Arrays.deepToString(tiles));
			// System.out.println("->");
		} while (checkSolution());
	}

	private int getTileRanged(int r, int c, boolean[][] grid) {
		if (!(r < 0 || r > rows - 1 || c < 0 || c > columns - 1))
			if (grid[r][c])
				return 1;
		return 0;
	}

	public boolean checkSolution() {
		boolean[][] nextBoard = getNextBoard();
		// System.out.println(Arrays.deepToString(nextBoard));
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				// System.out.print(tiles[r][c] + " " + nextBoard[r][c] + " " + !tiles[r][c].isCorrect(nextBoard[r][c]) + ",");
				if (!tiles[r][c].isCorrect(nextBoard[r][c]))
					return false;
			}
		// System.out.println();
		return true;
	}

	private boolean[][] getNextBoard() {
		final boolean[][] nextBoard = new boolean[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				int count = getTileRanged(r - 1, c - 1) + getTileRanged(r - 1, c) + getTileRanged(r - 1, c + 1)
						  + getTileRanged(r,	 c - 1)							  + getTileRanged(r,	 c + 1)
						  + getTileRanged(r + 1, c - 1) + getTileRanged(r + 1, c) + getTileRanged(r + 1, c + 1);
				// System.out.print((tiles[r][c].isFilled() ? 'F' : '0') + "," + count + " ");
				nextBoard[r][c] = count == 3 || (count == 2 && getTileRanged(r, c) == 1);
			}
		return nextBoard;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int[] getTileData() {
		return tileData;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Tile[] tileRow : tiles) {
			for (Tile tile : tileRow)
				sb.append(tile.ordinal());
			sb.append('\n');
		}
		return rows + "," + columns + ",\n" + sb.substring(0, sb.length() - 1) + ';';
	}

}
