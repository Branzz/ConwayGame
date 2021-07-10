package com.wordpress.brancodes;

import static com.wordpress.brancodes.Tile.*;

public class Level {

	private final Tile[][] tiles;
	private final int rows;
	private final int columns;

	public Level(Tile[][] tiles) {
		this.tiles = tiles;
		this.rows = tiles.length;
		this.columns = tiles[0].length;
	}

	public Level(int rows, int columns) { // random grid
		this.rows = rows;
		this.columns = columns;
		this.tiles = randomGrid(rows, columns);
	}

	public Level(final String[] levelParameters) {
		this(new Tile[Integer.parseInt(levelParameters[0])][Integer.parseInt(levelParameters[1])]);
		Tile[] tileValues = Tile.values();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				tiles[i][j] = tileValues[levelParameters[2].charAt(i * columns + j) - '0'];
	}

	public Tile[][] step() {
		Tile[][] nextState = new Tile[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				int count = 0;
				for (int i = -1; i <= 1; i++)
					for (int j = -1; i <= 1; j++)
						if (i != 0 && j != 0)
							count += getTileRanged(r + i, c + j);
				if (count == 3)
					nextState[r][c] = FILLED;
				if (count == 4 && getTileRanged(r, c) == 1)
					nextState[r][c] = FILLED;
			}
		return nextState;
	}

	private int getTileRanged(int r, int c) {
		if (!(r < 0 || r > rows - 1 || c < 0 || c > columns - 1))
			if (tiles[r][c] == TOFILL_EMPTY || tiles[r][c] == TOFILL_FILL || tiles[r][c] == TOEMPTY_FILL)
				return 1;
		return 0;
	}

	private static final double SPREAD = .2;

	public static Tile[][] randomGrid(int rows, int columns) {
		Tile[][] tiles = new Tile[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++) {
				double seed = Math.random();
				tiles[r][c] = EMPTY;
				if (seed > 1 - 4 * SPREAD)
					tiles[r][c] = TOFILL_EMPTY;
				if (seed > 1 - 3 * SPREAD)
					tiles[r][c] = TOFILL_FILL;
				if (seed > 1 - 2 * SPREAD)
					tiles[r][c] = TOEMPTY_EMPTY;
				if (seed > 1 - SPREAD)
					tiles[r][c] = TOEMPTY_FILL;
			}
		return tiles;
	}

	public int getColor(int r, int c) {
		return tiles[r][c].color();
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Tile getTile(int r, int c) {
		return tiles[r][c];
	}

}
