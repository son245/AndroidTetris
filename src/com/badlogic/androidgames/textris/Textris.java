package com.badlogic.androidgames.textris;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class Textris extends AndroidGame {
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}
}