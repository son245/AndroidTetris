package com.badlogic.androidgames.textris;
//(0,0) -> (9,14)
//| | | |o|o|o| | | | |
//| | | | |o| | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
//| | | | | | | | | | |
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Block {
	public enum BlockType{T, I, S, Z, D, L,J};
	public BlockType type;
	public boolean isPlaced;
	public int myType;
	public int centerIndex;//center for rotation
	public List<BlockPart> part = new ArrayList<BlockPart>();
	public Block(){
		isPlaced = false;
		Random rand = new Random();
		myType = rand.nextInt(7);
		myType++;
		if(myType==1)
		{
			type = BlockType.T;
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(6,0));
			part.add(new BlockPart(5,1));
			centerIndex = 1;
		}
		else if(myType==2)
		{
			type = BlockType.I;
			part.add(new BlockPart(3,0));
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(6,0));
			centerIndex = 1;
		}
		else if(myType==3){
			type = BlockType.S;
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(6,0));
			part.add(new BlockPart(4,1));
			part.add(new BlockPart(5,1));
			centerIndex = 0;
		}
		else if(myType==4){
			type = BlockType.Z;
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(5,1));
			part.add(new BlockPart(6,1));
			centerIndex = 1;
		}
		else if(myType==5){
			type = BlockType.D;
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(4,1));
			part.add(new BlockPart(5,1));
			centerIndex = 0;
		}
		else if(myType==6){
			type = BlockType.L;
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(6,0));
			part.add(new BlockPart(4,1));
			centerIndex = 1;	
		}
		else{
			type = BlockType.J;
			part.add(new BlockPart(4,0));
			part.add(new BlockPart(5,0));
			part.add(new BlockPart(6,0));
			part.add(new BlockPart(6,1));		
			centerIndex = 1;
		}
	}

	//rotate the block clockwise
	/* | | | |
	 * | |i| |
	   | | | | */
	public void rotate()
	{
		if(myType == 1/*T*/ || myType == 6/*L*/ || myType == 7/*J*/
				|| myType == 4 || myType == 3)
		{
			int x = part.get(centerIndex).x;
			int y = part.get(centerIndex).y;
			for(int i = 0; i < part.size(); i++)
			{
				int tempX = part.get(i).x;
				int tempY = part.get(i).y; 
				if(i != centerIndex)
				{
					if(tempX == x-1 && tempY == y-1)
					{
						part.get(i).x += 2;
					}
					else if(tempX == x && tempY == y-1)
					{
						part.get(i).x++;
						part.get(i).y++;
					}
					else if(tempX == x+1 && tempY == y-1)
					{
						part.get(i).y += 2;
					}
					else if(tempX == x-1 && tempY == y)
					{
						part.get(i).x++;
						part.get(i).y--;
					}
					else if(tempX == x+1 && tempY == y)
					{
						part.get(i).x--;
						part.get(i).y++;
					}
					else if(tempX== x-1 && tempY == y+1){
						part.get(i).y -= 2;
					}
					else if(tempX == x && tempY == y+1){
						part.get(i).x--;
						part.get(i).y--;
					}
					else{
						part.get(i).x -=2;
					}
				}
			}
		}
		else if(myType == 2)
		{
			if(part.get(0).y == part.get(centerIndex).y)
			{
				part.get(0).x = part.get(centerIndex).x;
				part.get(2).x = part.get(centerIndex).x;
				part.get(3).x = part.get(centerIndex).x;
				part.get(0).y = part.get(centerIndex).y-1;
				part.get(2).y = part.get(centerIndex).y+1;
				part.get(3).y = part.get(centerIndex).y+2;
			}
			else
			{
				part.get(0).y = part.get(centerIndex).y;
				part.get(2).y = part.get(centerIndex).y;
				part.get(3).y = part.get(centerIndex).y;
				part.get(0).x = part.get(centerIndex).x-1;
				part.get(2).x = part.get(centerIndex).x+1;
				part.get(3).y = part.get(centerIndex).x+2;
			}
		}
	}

	//move the block down
	public void moveDown()
	{
		for(int i = 0; i < 4;i++){
			part.get(i).y += 1;
		}
	}
	
	/**
	 * move the block one square to the left
	 */
	public void moveLeft(){
		int leftMostBlock = part.get(0).x;
		for(int i = 1; i < part.size(); i++){
			if(part.get(i).x < leftMostBlock)
			{
				leftMostBlock = part.get(i).x;
			}
		}
		if(leftMostBlock == 0)
		{
			return;
		}
		else
		{
			for(int i = 0; i < part.size();i++){
				part.get(i).x--;
			}
		}
	}
	
	/**
	 * move the block one square to the right
	 */
	public void moveRight(){
		int rightMostBlock = part.get(0).x;
		for(int i = 1; i < part.size(); i++){
			if(part.get(i).x > rightMostBlock)
			{
				rightMostBlock = part.get(i).x;
			}
		}
		if(rightMostBlock == World.WORLD_WIDTH - 1)
		{
			return;
		}
		else
		{
			for(int i = 0; i < part.size();i++)
			{
				part.get(i).x++;
			}
		}
	}
}