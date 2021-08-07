package com.wordpress.brancodes;

public interface SharedPrefAccess {

	String PREFS_NAME = "lifeGameData";

	int getSavedCurrentLevel();

	void setCurrentLevel(int currentLevel);

}
