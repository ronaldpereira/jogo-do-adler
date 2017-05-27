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
import static com.mygdx.game.Axis.*;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import java.util.ArrayList;

/**
 *
 * @author Hiago
 */
public class CollisionMap 
{
    private static final Texture BLOCK = new Texture("BlackBlock.png");
    private static final Texture ICE = new Texture("ice.png");
    private static final Texture COIN = new Texture("Chinelo.png");
    public Tile[][] map;
    private SpriteBatch drawArea;
    
    private int tileSize = 32;
    private int width;
    private int height;
    private Quadtree movingObjects;
    private ArrayList<DynamicCollider> localObjs;
    
    public CollisionMap(int width,int height,SpriteBatch batch)
    {
        drawArea = batch;
        this.width = width;
        this.height = height;
        map = new Tile[width][height];
        
        //QUADTREE
        movingObjects = new Quadtree(0,new Rectangle(0,0,width * tileSize,height * tileSize)); 
        
        for(int i = 0;i < width;i++)
        {
            for(int j = 0;j < height;j++)
            {
                map[i][j] = new Tile(i * tileSize,j * tileSize);
            }
        }
    }
    public void addMovingObject(DynamicCollider obj)
    {
        movingObjects.insert(obj);
    }
    
    public void createBlock(int x,int y)
    {
        map[x][y].setBlock(true);
        map[x][y].setSprite(BLOCK);
    }
    
    public void createIce(int x,int y)
    {
        map[x][y].setBlock(true);
        map[x][y].setSprite(ICE);
        map[x][y].setAttrition(2f);
        map[x][y].setMaxSpeed(15f);
    }
    
    public void createCoin(int x,int y)
    {
        map[x][y] = new Coin(x * tileSize,y * tileSize,COIN);
    }
    
    public void renderMap()
    {
        for(int i = 0;i < width;i++)
        {
            for(int j =0;j < height;j++)
            {
                map[i][j].drawSelf(drawArea);
            }
        }
    }
    
    public Vector2 findTile(float x,float y)
    {       
        return new Vector2((int)x/tileSize,(int)y/tileSize);
    }
    
    public Tile getTile(int x,int y)
    {
        return map[x][y];
    }
    
    public Vector2 getHorizontalIntersection(BoundingBox boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).x);
        int max = (int)(findTile((boundingBox.x + boundingBox.width),boundingBox.y).x);
       // System.out.println("HORIZONTAL: " + min + " - " + max);
        return new Vector2(min,max);
    }
    
    public Vector2 getVerticalIntersection(Rectangle boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).y);
        int max = (int)(findTile(boundingBox.x,(boundingBox.y + boundingBox.height)).y);
        //System.out.println("VERTICAL: " + min + " - " + max)F;
        
        return new Vector2(min,max);
    }
    
    public void closestHorizontalCollision(DynamicCollider entity,int direction,float distance)
    {
        
        if(direction == 0)
        {
            return;
        }
        //movingObjects.clear();
        localObjs = movingObjects.getNearby(entity);
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            {
                System.out.println("eu entro aqui");
                entity.collide(obj,new CollisionInfo(entity,HORIZONTAL_AXIS));
                obj.collide(entity,new CollisionInfo(obj,HORIZONTAL_AXIS));
            }
        }
        int xPos;
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        Vector2 intersection = getVerticalIntersection(box);
        //System.out.println("Inter :" + intersection.toString());
        
        if(direction > 0)
        {
            xPos = (int)(findTile((int)box.getRight(),(int)box.y).x);
        }
        else
        {
            xPos = (int)(findTile((int)box.getLeft(),(int)box.y).x);
        }
        
        while(xPos >= 0 && xPos < width)
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(map[xPos][i].getCollides())
                {
                    entity.collide(map[xPos][i],new CollisionInfo(entity,HORIZONTAL_AXIS));
                    map[xPos][i].collide(entity,new CollisionInfo(map[xPos][i],HORIZONTAL_AXIS));
                    
                    if(map[xPos][i].getBlocks())
                    {
                        return;
                    }
                }
            }

            delta += 1;  
            xPos += (delta * direction);
            if(delta > (int)ceil(distance/tileSize))
            {
                break;
            }
        }
    }
    
    public void closestVerticalCollision(DynamicCollider entity,int direction,float distance)
    {
        if(direction == 0)
        {
            return;
        }
        
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        int yPos;
        Vector2 intersection = getHorizontalIntersection(box);
        
        if(direction > 0)
        {
            yPos = (int)(findTile((int)box.x,(int)box.getUp()).y);
        }
        else
        {
            yPos = (int)(findTile((int)box.x,(int)box.getDown()).y);
        }
        
        while(yPos >= 0 && yPos < height)
        {
            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(map[i][yPos].getCollides())
                {
                    entity.collide(map[i][yPos],new CollisionInfo(entity,VERTICAL_AXIS));
                    map[i][yPos].collide(entity,new CollisionInfo(entity,VERTICAL_AXIS));
                    
                    if(map[i][yPos].getBlocks())
                    {
                        return;
                    }
                }       
            }    

            delta += 1;
            yPos += (delta * direction);
            if(delta > (int)ceil(distance/tileSize))
            {
                break;
            }
        }
    }
}