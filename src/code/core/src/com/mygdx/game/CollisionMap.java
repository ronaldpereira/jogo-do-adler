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
import static java.lang.Math.min;
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
        System.out.println("olhe " + map[x][y].getBlocks());
    }
    public void clearTile(int x,int y)
    {
        map[x][y] = new Tile(x,y);
    }
    
    public void createIce(int x,int y)
    {
        map[x][y].setBlock(true);
        map[x][y].setSprite(ICE);
        map[x][y].setAttrition(2f);
        map[x][y].setMaxSpeed(15f);
        System.out.println("olhe " + map[x][y].getBlocks());
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
    
    public int getTileSize()
    {
        return tileSize;
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
    
    private boolean checkIntersection(Vector2 a,Vector2 b)
    {
        if(a.x <= b.y && a.x >= b.x)
        {
            return true;
        }
        else if(b.x <= a.y && b.x >= a.y)
        {
            return true;
        }
        return false;
    }
    
    public void closestCollisionX(DynamicCollider entity,int direction,float distance)
    {
        int tileDistance = (int)ceil(distance/32);
        int xPos;
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        Vector2 intersection = getVerticalIntersection(box);
        
        localObjs = movingObjects.getNearby(entity);
        
        float colDistance = -1;
        CollidableObject colObj = null;
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getVerticalIntersection(obj.boundingBox)))
            {
                if(box.getHorizontalDistance(obj.boundingBox) < distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.boundingBox.x < entity.boundingBox.x && direction < 0) ||(obj.boundingBox.x > entity.boundingBox.x && direction > 0))
                    {
                        if(obj.getBlocks())
                        {
                            System.out.println("eu entro aqui");
                            if(colDistance == -1)
                            {
                                colDistance = box.getHorizontalDistance(obj.boundingBox);  
                                colObj = obj;
                            //colDistance = box.getHorizontalDistance(obj.boundingBox)
                            }
                            else if(box.getHorizontalDistance(obj.boundingBox) < colDistance)
                            {
                                colDistance = box.getHorizontalDistance(obj.boundingBox);  
                                colObj = obj;
                            }
                        }
                    }//colObj = obj;
                }
            }    
        }
        
        if(colDistance != -1)
        {
            tileDistance = min((int)ceil(colDistance/32),tileDistance);
        }
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
                    if(map[xPos][i].getBlocks())
                    {
                        if(colObj != null)
                        {
                            if(box.getHorizontalDistance(colObj.boundingBox) < box.getHorizontalDistance(map[xPos][i].boundingBox))
                            {
                                colObj = map[xPos][i];
                                break;
                            }
                        }
                        else
                        {
                            colObj = map[xPos][i];
                            break;
                        }
                    }
                }
            }

            delta += 1;  
            xPos += (delta * direction);
            
            if(delta > tileDistance)
            {
                break;
            }
        }
        
        if(colObj == null)
        {
            horizontalCollision(entity,direction,distance);
        }
        else
        {
            horizontalCollision(entity,direction,box.getHorizontalDistance(colObj.boundingBox));
        }
    }
    
    private void horizontalCollision(DynamicCollider entity,int direction,float distance)
    {
        
        if(direction == 0)
        {
            return;
        }
        //movingObjects.clear();
        int tileDistance = (int)ceil(distance/32);
        int xPos;
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        Vector2 intersection = getVerticalIntersection(box);
        //System.out.println("Inter :" + intersection.toString());
        

                //if(localObjs)
                //if
            //}
        localObjs = movingObjects.getNearby(entity);
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getVerticalIntersection(obj.boundingBox)))
            {
                if(box.getHorizontalDistance(obj.boundingBox) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.boundingBox.x < entity.boundingBox.x && direction < 0) ||(obj.boundingBox.x > entity.boundingBox.x && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,HORIZONTAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,HORIZONTAL_AXIS));
                    }//colObj = obj;
                }
            }    
        }   
        
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
                }
            }

            delta += 1;  
            xPos += (delta * direction);
            
            if(delta > tileDistance + 1)
            {
                break;
            }
        }
    }
    
    public void closestCollisionY(DynamicCollider entity,int direction,float distance)
    {
        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        Vector2 intersection = getHorizontalIntersection(box);
        
        localObjs = movingObjects.getNearby(entity);
        
        float colDistance = -1;
        CollidableObject colObj = null;
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getHorizontalIntersection(obj.boundingBox)))
            {
                if(box.getVerticalDistance(obj.boundingBox) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.boundingBox.y < entity.boundingBox.y && direction < 0) ||(obj.boundingBox.y > entity.boundingBox.y && direction > 0))
                    {
                        if(obj.getBlocks())
                        {
                            System.out.println("eu entro aqui");
                            if(colDistance == -1)
                            {
                                colDistance = box.getVerticalDistance(obj.boundingBox);  
                                colObj = obj;
                            //colDistance = box.getHorizontalDistance(obj.boundingBox)
                            }
                            else if(box.getVerticalDistance(obj.boundingBox) < colDistance)
                            {
                                colDistance = box.getVerticalDistance(obj.boundingBox);  
                                colObj = obj;
                            }
                        }
                    }//colObj = obj;
                }
            }    
        }
        
        if(colDistance != -1)
        {
            tileDistance = min((int)ceil(colDistance/32),tileDistance);
        }
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
                    if(map[i][yPos].getBlocks())
                    {
                        if(colObj != null)
                        {
                            if(box.getVerticalDistance(colObj.boundingBox) < box.getVerticalDistance(map[i][yPos].boundingBox))
                            {
                                colObj = map[i][yPos];
                                break;
                            }
                        }
                        else
                        {
                            colObj = map[i][yPos];
                            break;
                        }
                    }
                }
            }

            delta += 1;  
            yPos += (delta * direction);
            
            if(delta > tileDistance)
            {
                break;
            }
        }
        
        if(colObj == null)
        {
            verticalCollision(entity,direction,distance);
        }
        else
        {
            verticalCollision(entity,direction,box.getVerticalDistance(colObj.boundingBox));
        }
    }
    
    private void verticalCollision(DynamicCollider entity,int direction,float distance)
    {
        if(direction == 0)
        {
            return;
        }
        //movingObjects.clear();
        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.boundingBox;
        int delta = 0;
        Vector2 intersection = getHorizontalIntersection(box);
        //System.out.println("Inter :" + intersection.toString());
        

                //if(localObjs)
                //if
            //}
        localObjs = movingObjects.getNearby(entity);
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getHorizontalIntersection(obj.boundingBox)))
            {
                if(box.getVerticalDistance(obj.boundingBox) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.boundingBox.y < entity.boundingBox.y && direction < 0) ||(obj.boundingBox.y > entity.boundingBox.y && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,VERTICAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,VERTICAL_AXIS));
                    }//colObj = obj;
                }
            }    
        }   
        
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
                    map[i][yPos].collide(entity,new CollisionInfo(map[i][yPos],VERTICAL_AXIS));
                }
            }

            delta += 1;  
            yPos += (delta * direction);
            
            if(delta > tileDistance + 1)
            {
                break;
            }
        }
    }
}