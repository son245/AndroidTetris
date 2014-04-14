package com.badlogic.androidgames.textris;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen{
	public LoadingScreen(Game game){
		super(game);
	}
	public void update(float deltaTime){
		Graphics g = game.getGraphics();
		Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
		Assets.buttons = g.newPixmap("buttons.png", PixmapFormat.ARGB4444);
		Assets.help = g.newPixmap("help.png", PixmapFormat.ARGB4444);
		Assets.gameOver = g.newPixmap("gameover.png", PixmapFormat.ARGB4444);
		Assets.logo = g.newPixmap("logo.png", PixmapFormat.ARGB4444);
		Assets.mainMenu = g.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
		Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB4444);
		Assets.pause = g.newPixmap("pausemenu.png", PixmapFormat.ARGB4444);
	    Assets.ready = g.newPixmap("ready.png", PixmapFormat.ARGB4444);   		
		Assets.score = game.getAudio().newSound("score.ogg");
		Assets.over = game.getAudio().newSound("over.ogg");
		Assets.click = game.getAudio().newSound("click.ogg");
		Assets.downbutton = g.newPixmap("downbutton.png", PixmapFormat.ARGB4444);
		Settings.load(game.getFileIO());
		game.setScreen(new MainMenuScreen(game));
	}
	public void present(float deltaTime){
	}
	public void pause(){
	}
	public void resume(){
	}
	public void dispose(){
	}
}