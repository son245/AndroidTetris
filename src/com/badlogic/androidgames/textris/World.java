package com.badlogic.androidgames.textris;
import android.util.Log;
public class World {
	public static final int WORLD_WIDTH = 10;
	public static final int WORLD_HEIGHT = 13;
	static final float TICK_INITIAL = 0.5f;
	static final float TICK_DECREMENT = 0.05f;
	static final int SCORE_INCREMENT = 10;
	private final static String TAG = "textris";	
	public Block block;
	public boolean isGameOver = false;
	public int score = 0;
	int fields[][] = new int[WORLD_WIDTH][WORLD_HEIGHT];
	float tickTime = 0;
	float tick = TICK_INITIAL;
	public World()
	{
		block = new Block();
		for(int i = 0; i < WORLD_HEIGHT; i++)
		{
			for(int j = 0; j < WORLD_WIDTH; j++)
			{
				fields[j][i] = 0;
			}
		}
	}
	/**Check if all the blocks in a line are filled
	 * @param
	 * @return return true if one line are full
	 */
	private boolean isScore(int y)
	{
		boolean isEmpty = false;
		for(int i = 0; i < WORLD_WIDTH; i++)
		{
			if(fields[i][y] == 0)
			{
				isEmpty = true;
			}
		}
		if(isEmpty)
		{
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * remove all the scored lines and return the number of line
	 */
	public void removeScoredLine(int y)
	{
		for(int i = 0; i < WORLD_WIDTH; i++)
		{
			fields[i][y] = 0;
		}
	}

	/**check if any thing currently below the block or is it
	 * reach the bottom
	 * 
	 * @return true if the block can move down
	 */
	public boolean isMoveDownable()
	{
		for(int i = 0; i < block.part.size(); i++)
		{
			Log.i(TAG,"current y" + block.part.get(i).y);
			if(block.part.get(i).y == WORLD_HEIGHT-1)
			{
				return false;
			}
			else if(fields[block.part.get(i).x][block.part.get(i).y+1] != 0)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * place all the part of the block to the fields
	 */
	public void blockToField()
	{
		int i = 0;
		while(i < block.part.size())
		{
			fields[block.part.get(i).x][block.part.get(i).y] = block.myType;
			Log.i(TAG,"current blockType is: " + block.myType);
			i++;
		}
	}
	
	public boolean checkGameOver()
	{
		for(int i = 0; i < WORLD_WIDTH; i++)
		{
			if(fields[i][0] != 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * move all the blocks down after remove a line
	 * method: check for each column, move down to prevent floating block
	 */
	public void moveBlockDownAfterScore(int y)
	{
		for(int j = y; j > 0; j--)
		{
			for(int i = 0; i < WORLD_WIDTH; i++)
			{
				fields[i][j] = fields[i][j-1];
			}
		}
	}
	//paint the block corresponding to the block's position
	//know when block is placed
	//if the block is placed, set in it the field, and
	//then start a new block
	
	public void update(float deltaTime)
	{
		Log.i(TAG,"");
		isGameOver = checkGameOver();
		if(isGameOver)
		{
			return;
		}
		tickTime += deltaTime;
		while(tickTime > tick){
			tickTime -= tick;
			if(block.isPlaced)
			{
				Log.i(TAG,"creating new block");
				block = new Block();
			}
			else
			{
				if(!isMoveDownable())
				{
					Log.i(TAG,"changing isPlaced to true");
					block.isPlaced = true;
					blockToField();
					Log.i(TAG,"block to field done");
					for(int i = WORLD_HEIGHT - 1; i >= 0; i--)
					{
						if(isScore(i))
						{
							score += SCORE_INCREMENT;
							Log.i(TAG,"scoring");
							removeScoredLine(i);
							moveBlockDownAfterScore(i);
							if(score % 100 == 0 && tick - TICK_DECREMENT > 0)
							{
								tick -= TICK_DECREMENT;
							}
							i++;
						}
					}
				}
				else
				{
					block.moveDown();
				}
			}
		}
	}
}