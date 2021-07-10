package com.wordpress.brancodes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
public interface Old {

	boolean start();

	boolean stop();

	// private View lastButton;
	//
	// public void toggle(View v) {
	// 	if (lastButton != null)
	// 		lastButton.setEnabled(true);
	// 	lastButton = v;
	// 	v.setEnabled(false);
	// }

	// private Bot bot = new Bot() {
	// 	@Override
	// 	public boolean start() {
	// 		System.out.println("starting");
	// 		return true;
	// 	}
	//
	// 	@Override
	// 	public boolean stop() {
	// 		System.out.println("stopping");
	// 		return true;
	// 	}
	// };
	//
	// private boolean botEnabled = false;
	//
	// public void botEnableButton(View v) {
	// 	Button b = (Button) v;
	// 	b.setText("Loading...");
	// 	if (botEnabled) {
	// 		if (bot.stop()) {
	// 			b.setText("Start");
	// 			botEnabled = false;
	// 		}
	// 	} else {
	// 		if (bot.start()) {
	// 			b.setText("Stop");
	// 			botEnabled = true;
	// 		}
	// 	}
	// }

	// public class MainActivity extends AppCompatActivity {
	//
	// 	private int buttonsWidth = 3;
	// 	private int buttonsHeight = 3;
	// 	private Button[][] buttons;
	// 	private int[][] buttonIds;
	// 	private boolean[][] buttonStates = new boolean[buttonsWidth][buttonsHeight];
	// 	private View mainLayout;
	// 	private static final Random random = new Random();
	// 	private static final int defaultBackgroundColor = Color.rgb(63, 74, 78);
	//
	// 	public MainActivity() {
	// 		buttonIds = new int[][]
	// 							{ { R.id.button1, R.id.button2, R.id.button3 },
	// 									{ R.id.button4, R.id.button5, R.id.button6 },
	// 									{ R.id.button7, R.id.button8, R.id.button9 } };
	// 	}
	//
	// 	@Override
	// 	protected void onCreate(Bundle savedInstanceState) {
	// 		super.onCreate(savedInstanceState);
	// 		setContentView(R.layout.activity_main);
	// 		buttons = new Button[buttonsHeight][buttonsWidth];
	// 		for (int i = 0; i < buttonsWidth; i++)
	// 			for (int j = 0; j < buttonsHeight; j++)
	// 				buttons[i][j] = findViewById(buttonIds[i][j]);
	// 		mainLayout = findViewById(R.id.mainLayout);
	// 		resetButtons();
	// 		for (int i = 0; i < 9; i++)
	// 			System.out.println(i / 3 + " " + i % 3);
	//
	// 	}
	//
	// 	public void buttonPanelClick(View v) {
	// 		Button button = (Button) v;
	// 		int buttonId = button.getId();
	// 		for (int i = 0; i < buttonsWidth; i++)
	// 			for (int j = 0; j < buttonsHeight; j++) {
	// 				if (buttonIds[i][j] == buttonId) {
	// 					buttonNeighborsSwitch(i, j);
	// 					// if (i > 0) {
	// 					// 	if (j > 0)
	// 					// 		switchButtonState(i - 1, j - 1);
	// 					// 	if (j < buttonsWidth - 1)
	// 					// 		switchButtonState(i - 1, j + 1);
	// 					// }
	// 					// if (i < buttonsHeight - 1) {
	// 					// 	if (j > 0)
	// 					// 		switchButtonState(i + 1, j - 1);
	// 					// 	if (j < buttonsWidth - 1)
	// 					// 		switchButtonState(i + 1, j + 1);
	// 					// }
	// 					checkAllButtonsEqualStates();
	// 					return;
	// 				}
	// 			}
	// 	}
	//
	// 	private void checkAllButtonsEqualStates() {
	// 		// System.out.println(Arrays.deepToString(buttonStates));
	// 		boolean allOn = true;
	// 		boolean allOff = true;
	// 		for (boolean[] row : buttonStates)
	// 			for (boolean state : row) {
	// 				if (state)
	// 					allOff = false;
	// 				else
	// 					allOn = false;
	// 				if (!(allOn || allOff))
	// 					return;
	// 			}
	// 		// System.out.println("Match");
	// 		mainLayout.setBackgroundColor(allOn ? Color.CYAN : Color.MAGENTA);
	// 	}
	//
	// 	private void buttonNeighborsSwitch(final int i, final int j) {
	// 		switchButtonState(i, j);
	// 		// for (int i0 = i - 1; i0 >= 0 && i0 < buttonsWidth && i0 <= i + 1; i0 += 2)
	// 		// 	for (int j0 = j - 1; j0 >= 0 && j0 < buttonsWidth && j0 <= j + 1; j0 += 2)
	// 		// 		switchButtonState(i0, j0);
	// 		if (j > 0)
	// 			switchButtonState(i, j - 1);
	// 		if (j < buttonsWidth - 1)
	// 			switchButtonState(i, j + 1);
	// 		if (i > 0)
	// 			switchButtonState(i - 1, j);
	// 		if (i < buttonsHeight - 1)
	// 			switchButtonState(i + 1, j);
	// 	}
	//
	// 	private void switchButtonState(int i, int j) {
	// 		buttons[i][j].setBackgroundColor(buttonStates[i][j] ? Color.WHITE : Color.BLUE);
	// 		buttons[i][j].setTextColor(buttonStates[i][j] ? Color.BLACK : Color.WHITE);
	// 		buttonStates[i][j] ^= true;
	// 	}
	//
	// 	public void resetButtonClick(View v) {
	// 		// Button resetButton = (Button) v;
	// 		resetButtons();
	// 	}
	//
	//
	// 	private void resetButtons() {
	// 		for (int i = 0; i < buttonsWidth; i++)
	// 			for (int j = 0; j < buttonsHeight; j++) {
	// 				buttonStates[i][j] = random.nextBoolean();
	// 				buttons[i][j].setBackgroundColor(buttonStates[i][j] ? Color.WHITE : Color.BLUE);
	// 				buttons[i][j].setTextColor(buttonStates[i][j] ? Color.BLACK : Color.WHITE);
	// 			}
	// 		mainLayout.setBackgroundColor(defaultBackgroundColor);
	// 		((TextView) findViewById(R.id.editText1)).setText("");
	// 	}
	//
	// 	public void textEdit(View v) {
	// 		TextView textView = findViewById(R.id.editText1);
	// 		String input = textView.getText().toString();
	// 		Log.d("info", input);
	// 		int limit = Math.min(input.length(), 9);
	// 		for (int i = 0; i < limit; i++)
	// 			buttons[i / 3][i % 3].setText(String.valueOf(input.charAt(i)));
	// 		textView.setText("");
	// 	}
	//
	// }

}
