package com.wordpress.brancodes;

import android.graphics.Color;
public enum Tile {
	EMPTY(Color.rgb(255, 255, 255)), // empty
	FILLED(Color.rgb(0, 0, 0)), // filled by player
	TOFILL_EMPTY(Color.rgb(115, 115, 255)), // to be filled (empty)
	TOFILL_FILL(Color.rgb(15, 15, 100)), // to be filled (filled)
	TOEMPTY_EMPTY(Color.rgb(255, 115, 115)), // to be unfilled (empty)
	TOEMPTY_FILL(Color.rgb(100, 15, 15)), // to be unfilled (filled)
	TOFILL_CORRECT(Color.rgb(40, 40, 40)),
	TOEMPTY_CORRECT(Color.rgb(150, 150, 150));

	private final int color;

	Tile(int color) {
		this.color = color;
	}

	public int color() {
		return color;
	}

	/*
	 1 : return FILLED;
	 2 : return TOFILL_EMPTY;
	 3 : return TOFILL_FILL;
	 4 : return TOEMPTY_EMPTY;
	 5 : return TOEMPTY_FILL;
	 6 : return TOEMPTY_CORRECT;
	 7 : return TOFILL_CORRECT;
	 0 : EMPTY
	 */

}
