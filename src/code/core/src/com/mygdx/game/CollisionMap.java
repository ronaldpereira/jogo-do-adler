/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class CollisionMap 
{
    private static final Texture BLOCK = new Texture("BlackBlock.png");
    private Tile[][] map;
    private SpriteBatch drawArea;
    
    private int tileSize = 32;
    private int width;
    private int height;
    
    public CollisionMap(int width,int height,SpriteBatch batch)
    {
        drawArea = batch;
        this.width = width;
        this.height = height;
        map = new Tile[width][height];
        
        for(int i = 0;i < width;i++)
        {
            for(int j = 0;j < height;j++)
            {
                map[i][j] = new Tile(i * tileSize,j * tileSize);
            }
        }
    }
    
    public void createBlock(int x,int y)
    {
        map[x][y].setBlock(true);
        map[x][y].setSprite(BLOCK);
    }
    
    public void renderMap()
    {
        for(int i = 0;i < width;i++)
        {
            for(int j =0;j < height;j++)
            {
                if(map[i][j].getSprite() != null)
                {
                    drawArea.draw(map[i][j].getSprite(),i * tileSize,j * tileSize);
                }
            }
        }
    }
    
    public Vector2 getTile(int x,int y)
    {
        return new Vector2((int)x/tileSize,(int)y/tileSize);
    }
    
    public Vector2 getHorizontalIntersection(Rectangle boundingBox)
    {
        int min = (int)(getTile((int)boundingBox.x,(int)boundingBox.y).x);
        int max = (int)(getTile((int)(boundingBox.x + boundingBox.width),(int)boundingBox.y).x);
        
        return new Vector2(min,max);
    }
    
    public Vector2 getVerticalIntersection(Rectangle boundingBox)
    {
        int min = (int)(getTile((int)boundingBox.x,(int)boundingBox.y).y);
        int max = (int)(getTile((int)boundingBox.x,(int)(boundingBox.y + boundingBox.height)).y);
        
        return new Vector2(min,max);
    }
    
    public float getHorizontalDistance(Rectangle boundingBox,int direction)
    {
        Boolean collision = false;
        int delta = 0;
        float distance;
        Vector2 intersection = getVerticalIntersection(boundingBox);
        
        while(!collision)
        {
            delta++;
            for(int i = (int)intersection.x;i <(int)intersection.y;i++)
            {
                if(map[(int)(getTile((int)boundingBox.x,(int)boundingBox.y).x + delta * direction)][i].getBlocks())
                {
                    collision = true;
                    distance = 
                    break;
                }
            }
        }
        return 0f;
    }
}