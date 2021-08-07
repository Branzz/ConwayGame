package com.wordpress.brancodes;

import android.widget.Button;

public class GameButton {

	private final Button button;
	private Tile tileType;
	private final int x;
	private final int y;
	private int neighbors = 0;

	public GameButton(final Button button, final Tile tileType, final int x, final int y) {
		this.button = button;
		this.tileType = tileType;
		this.x = x;
		this.y = y;
		refreshColor();
	}

	public void click() {
		tileType = tileType.opposite();
		refreshColor();

	}

	public void refreshColor() {
		button.setBackgroundColor(tileType.color());
		button.setTextColor(tileType.textColor());
	}

	public void changeNeighborCount(final boolean increase) {
		neighbors += increase ? 1 : -1;
	}

	public void updateText() {
		if (tileType.caresAboutFill())
			button.setText(String.valueOf(neighbors));
	}

	public void removeText() {
		button.setText("");
	}

	public Button getButton() {
		return button;
	}

	public Tile getTileType() {
		return tileType;
	}

	public void setTileType(final Tile tileType) {
		this.tileType = tileType;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
