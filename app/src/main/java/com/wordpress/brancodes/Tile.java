package com.wordpress.brancodes;

import android.graphics.Color;

public enum Tile {
	EMPTY(false, false, false, Color.rgb(255, 255, 255), Color.rgb(0, 0, 0)), // empty
	FILLED(true, true, false, Color.rgb(0, 0, 0), Color.rgb(255, 255, 255)), // filled by player
	TOFILL_EMPTY(false, true, true, Color.rgb(115, 115, 255), Color.rgb(255, 20, 20)), // to be filled (empty)
	TOFILL_FILL(true, true, true, Color.rgb(15, 15, 100), Color.rgb(255, 80, 80)), // to be filled (filled)
	TOEMPTY_EMPTY(false, false, true, Color.rgb(255, 115, 115), Color.rgb(20, 20, 255)), // to be unfilled (empty)
	TOEMPTY_FILL(true, false, true, Color.rgb(100, 15, 15), Color.rgb(80, 80, 255)), // to be unfilled (filled)
	TOFILL_CORRECT(true, true, false, Color.rgb(40, 40, 40), Color.rgb(0, 0, 0)),
	TOEMPTY_CORRECT(false, false, false, Color.rgb(150, 150, 150), Color.rgb(0, 0, 0));

	private final boolean filled;
	private final boolean targetFill;
	private final boolean caresAboutFill;
	private final int color;
	private final int textColor;

	Tile(boolean filled, boolean targetFill, boolean caresAboutFill, int color, int textColor) {
		this.filled = filled;
		this.targetFill = targetFill;
		this.caresAboutFill = caresAboutFill;
		this.color = color;
		this.textColor = textColor;
	}

	public boolean isFilled() {
		return filled;
	}

	public boolean isCorrect(boolean isFilled) {
		return !caresAboutFill || isFilled == targetFill;
	}

	public int color() {
		return color;
	}

	public int textColor() {
		return textColor;
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
