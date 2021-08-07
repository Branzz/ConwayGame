package com.wordpress.brancodes;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
public enum Tile {
	EMPTY(false, false, false, R.color.light, R.color.dark), // empty
	FILLED(true, true, false, R.color.dark, R.color.light), // filled by player
	TO_FILL_EMPTY(false, true, true, R.color.blue, R.color.dark), // to be filled (empty)
	TO_FILL_FILL(true, true, true, R.color.darkBlue, R.color.light), // to be filled (filled)
	TO_EMPTY_EMPTY(false, false, true, R.color.lightRed, R.color.dark), // to be unfilled (empty)
	TO_EMPTY_FILL(true, false, true, R.color.darkRed, R.color.light), // to be unfilled (filled)
	TO_FILL_CORRECT(true, true, false, R.color.green, R.color.dark),
	TO_EMPTY_CORRECT(false, false, false, R.color.green, R.color.dark);

	private final boolean filled;
	private final boolean targetFill;
	private final boolean caresAboutFill;
	private final int colorID;
	private int color = 0;
	private final int textColorID;
	private int textColor = 0;
	private Tile opposite;

	static {
		EMPTY.opposite = FILLED;
		FILLED.opposite = EMPTY;
		TO_FILL_EMPTY.opposite = TO_FILL_FILL;
		TO_FILL_FILL.opposite = TO_FILL_EMPTY;
		TO_EMPTY_EMPTY.opposite = TO_EMPTY_FILL;
		TO_EMPTY_FILL.opposite = TO_EMPTY_EMPTY;
		TO_FILL_CORRECT.opposite = TO_EMPTY_CORRECT;
		TO_EMPTY_CORRECT.opposite = TO_FILL_CORRECT;
	}

	Tile(boolean filled, boolean targetFill, boolean caresAboutFill, int colorID, int textColorID) {
		this.filled = filled;
		this.targetFill = targetFill;
		this.caresAboutFill = caresAboutFill;
		this.colorID = colorID;
		this.textColorID = textColorID;
	}

	/**
	 * To be called by an activity once, before tile colors are used
	 */
	public static void generateColors(Context context) {
		for (Tile tile : values()) {
			tile.color = ResourcesCompat.getColor(context.getResources(), tile.colorID, null);
			tile.textColor = ResourcesCompat.getColor(context.getResources(), tile.textColorID, null);
		}
	}

	public boolean isFilled() {
		return filled;
	}

	public boolean caresAboutFill() {
		return caresAboutFill;
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

	public Tile opposite() {
		return opposite;
	}

}
