package com.badlogic.androidgames.textris;

import java.util.List;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Canvas;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.textris.Assets;
import com.badlogic.androidgames.textris.MainMenuScreen;
import com.badlogic.androidgames.textris.Settings;
import com.badlogic.androidgames.textris.GameScreen.GameState; 

public class GameScreen extends Screen{
	enum GameState {
		Ready,
		Running,
		Paused,
		GameOver
	}
	GameState state = GameState.Ready;
	World world;
	int oldScore = 0;
	String score = "0";
	public GameScreen(Game game)
	{
		super(game);
		world = new World();
	}
	public final static int BLOCK_SIZE = 32;
	@Override
	public void update(float deltaTime)
	{
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
		
		if(state == GameState.Ready)
		{
			updateReady(touchEvents);
		}
		if(state == GameState.Running)
		{
			updateRunning(touchEvents, deltaTime);
		}
		if(state == GameState.Paused)
		{
			updatePaused(touchEvents);
		}
		if(state == GameState.GameOver)
		{
			updateGameOver(touchEvents);
		}
	}
	private void updateReady(List < TouchEvent > touchEvents)
	{
		if(touchEvents.size() > 0)
		{
			state = GameState.Running;
		}
	}
	private void updateRunning(List < TouchEvent > touchEvents, float deltaTime)
	{
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP)
			{
				if(event.x < 64 && event.y < 64)
				{
					if(Settings.soundEnabled)
					{
						Assets.click.play(1);
					}
					state = GameState.Paused;
					return;
				}
			}
			if(event.type == TouchEvent.TOUCH_DOWN)
			{
				boolean isMoveLeftable = true;
				if(event.x < 64 && event.y > 416){
					for(int j = 0; i < world.block.part.size(); i++)
					{
						if(world.block.part.get(i).x == 0)
						{
							isMoveLeftable = false;
						}
						else if(world.fields[world.block.part.get(j).x-1][world.block.part.get(j).y] != 0 )
						{
							isMoveLeftable = false;
						}
					}
					if(isMoveLeftable)
					{
						world.block.moveLeft();
					}
				}
				if(event.x > 256 && event.y > 416){
					boolean isMoveRightable = true;
					for(int j = 0; i < world.block.part.size(); i++)
					{
						if(world.block.part.get(i).x == World.WORLD_WIDTH- 1)
						{
							isMoveRightable = false;
						}
						if(world.fields[world.block.part.get(j).x+1][world.block.part.get(j).y] != 0 )
						{
							isMoveRightable = false;
						}
						/*if(world.block.part.get(i).x < World.WORLD_WIDTH)
						{
							if(world.fields[world.block.part.get(j).x+1][world.block.part.get(j).y] != 0 )
							{
								isMoveRightable = false;
							}
						}*/
					}
					if(isMoveRightable)
					{
						world.block.moveRight();
					}
				}
				if(event.y > 64 && event.y < 416){
					world.block.rotate();
				}
			}
		}
		world.update(deltaTime);
		{
			if(world.isGameOver)
			{
				state = GameState.GameOver;
			}
			if(oldScore != world.score)
			{
				oldScore = world.score;
				score = "" + oldScore;
			}
		}
		if(world.isGameOver)
		{
			state = GameState.GameOver;
		}
	}
	private void updatePaused(List < TouchEvent > touchEvents) {
		int len = touchEvents.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP) {
				if(event.x > 80 && event.x <= 240) {
					if(event.y > 100 && event.y <= 148) {
						if(Settings.soundEnabled)
							Assets.click.play(1);
							state = GameState.Running;
							return;
		}
					if(event.y > 148 && event.y < 196) {
						if(Settings.soundEnabled)
								Assets.click.play(1);
							game.setScreen(new MainMenuScreen(game));
							return;
					}
				}
			}
		}
	}
    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                   event.y >= 200 && event.y <= 264) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }
    @SuppressLint("ResourceAsColor")
	public void present(float deltaTime)
    {
    	Graphics g = game.getGraphics();
    	g.drawPixmap(Assets.background,0,0);
    	drawWorld(world);
        if(state == GameState.Ready) 
            drawReadyUI();
      	if(state == GameState.Running)
            drawRunningUI();
        if(state == GameState.Paused)
            drawPausedUI();
        if(state == GameState.GameOver)
            drawGameOverUI();
        drawText(g, score, 250,10);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(android.R.color.black);
        paint.setTextSize(20);
        int level = oldScore % 100;
        canvas.drawText("Level: "+ level, 250, 30, paint);
        
        		//g.getWidth() / 2 - score.length()*20 / 2, g.getHeight() - 42);
    }
    public void drawText(Graphics g, String line, int x, int y) 
    {
    	int len = line.length();
    	for(int i = 0; i < len; i++) {
	    	char character = line.charAt(i);
	    	if(character == ' ') {
	    		x += 20;
	    		continue;
	    	}
	    	int srcX = 0;
	    	int srcWidth = 0;
	    	if(character == '.') {
	    	srcX = 200;
	    	srcWidth = 10;
	    	} 
	    	else{
		    	srcX = (character - '0') * 20;
		    	srcWidth = 20;
	    	}
		    	g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
		    	x += srcWidth;
	    	}
    }
    private void drawWorld(World world)
    {
    	Graphics g = game.getGraphics();
    	//draw field
    	for(int i = 0; i < World.WORLD_WIDTH; i++)	
    	{
    		for(int j = 0; j < World.WORLD_HEIGHT; j++)
    		{
    			int color;
    			if(world.fields[i][j] != 0)
    			{
    				if(world.fields[i][j]==1)
    				{
    					color = Color.GREEN;
    				}
    				else if(world.fields[i][j] == 2)
    				{
    					color = Color.RED;
    				}
    				else if(world.fields[i][j] == 3)
    				{
    					color = Color.YELLOW;
    				}
    				else if(world.fields[i][j] == 4)
    				{
    					color = Color.rgb(230, 230, 250);
    				}
    				else if(world.fields[i][j] == 5)
    				{
    					color = Color.CYAN;
    				}
    				else if(world.fields[i][j] == 6)
    				{
    					color = Color.BLUE;
    				}
    				else{
    					color = Color.BLACK;
    				}
    				g.drawRect(i*32, j*32, BLOCK_SIZE, BLOCK_SIZE, color);
    			}
    		}
    	}
    }
    
    private void drawReadyUI() {
    	Graphics g = game.getGraphics();
    	g.drawPixmap(Assets.ready, 47, 100);
    	g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    
    private void drawRunningUI()
    {
    	Graphics g = game.getGraphics();
    	//draw block
    	g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
    	g.drawLine(0, 416, 480, 416, Color.BLACK);
    	g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
    	g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    	//g.drawPixmap(Assets.downbutton, 128, 416);
    	for(int i = 0; i < world.block.part.size(); i++)
    	{
    		int color;
			if(world.block.myType==1)
			{
				color = Color.GREEN;
			}
			else if(world.block.myType == 2)
			{
				color = Color.RED;
			}
			else if(world.block.myType == 3)
			{
				color = Color.YELLOW;
			}
			else if(world.block.myType == 4)
			{
				color = Color.rgb(230, 230, 250);
			}
			else if(world.block.myType == 5)
			{
				color = Color.CYAN;
			}
			else if(world.block.myType == 6)
			{
				color = Color.BLUE;
			}
			else{
				color = Color.BLACK;
			}
    		g.drawRect(world.block.part.get(i).x * 32, world.block.part.get(i).y * 32, 
    				BLOCK_SIZE, BLOCK_SIZE, color);
    	}
    }
    
    private void drawPausedUI()
    {
    	Graphics g = game.getGraphics();
    	g.drawPixmap(Assets.pause, 80, 100);
    	g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    private void drawGameOverUI() 
    {
    	Graphics g = game.getGraphics();
    	g.drawPixmap(Assets.gameOver, 62, 100);
    	g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
    	g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if(state == GameState.Running)
		{
			state = GameState.Paused;
		}
		if(world.isGameOver)
		{
			Settings.addScore(world.score);
			Settings.save(game.getFileIO());
		}
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
