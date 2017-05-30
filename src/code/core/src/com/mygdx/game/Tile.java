/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 *
 * @author Hiago
 */
public class Tile extends CollidableObject
{
    private Texture sprite;
    //protected Boolean checksCollision;
    //private Boolean blocksMovement;
    private float attrition = 17.5f;
    private float maxSpeed = 8f;
    
    public Tile(int x,int y)
    {
        //sprite = new Texture();
        boundingBox = new BoundingBox(x,y,32,32);
        checksCollision = false;
        blocksMovement = false;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks)
    {
        sprite = spr;
        boundingBox = new BoundingBox(x,y,32,32);
        checksCollision = blocks;
        blocksMovement = blocks;
    }
    public void setAttrition(float value)
    {
        attrition = value;
    }
    
    /*
    @Override   
    public void setBlock(Boolean blocks)
    {
        if(blocks == true)
        {
            checksCollision = true;
        }
        blocksMovement = blocks;
    }*/
  
    public float getAttrition()
    {
        return attrition;
    }
    
    public void setMaxSpeed(float value)
    {
        maxSpeed = value;
    }
    
    public float getMaxSpeed()
    {
        return maxSpeed;
    }
    public void setSprite(Texture spr)
    {
        sprite = spr;
    }
    
    public Texture getSprite()
    {
        return sprite;
    }
    
    /*
    @Override
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
    */
    
    public Boolean getCollides()
    {
        return checksCollision;
    }
    
    public void setCollides(Boolean checks)
    {
        checksCollision = checks;
    }
    
    public void drawSelf(SpriteBatch batch)
    {
        if(sprite != null)
        {
            batch.draw(sprite,boundingBox.x,boundingBox.y);
        }
    }
    
    public BoundingBox getPos()
    {
        return boundingBox;
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }
    
}
