/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 *
 * @author Hiago
 */
public class Tile extends CollidableObject implements ItileDispatcher
{
    //protected Boolean checksCollision;
    //private Boolean blocksMovement;
    private float attrition = 0.075f;
    private float maxSpeed = 8f;  
    
    public Tile(int x,int y)
    {
        //sprite = new Texture();
        ////TODO: mudar para a constante tileSize.
        setBoundingBox(new BoundingBox(x,y,32,32));
        checksCollision = false;
        blocksMovement = false;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks)
    {
        sprite = spr;
        setBoundingBox(new BoundingBox(x,y,32,32));
        checksCollision = blocks;
        blocksMovement = blocks;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks,float attrition,float maxSpeed)
    {
        sprite = spr;
        setBoundingBox(new BoundingBox(x,y,32,32));
        checksCollision = blocks;
        blocksMovement = blocks;
        this.attrition = attrition;
        this.maxSpeed = maxSpeed;
    }
    
    public void setAttrition(float value)
    {
        attrition = value;
    }
    
    /*
    @Override   
    public void setBlocks(Boolean blocks)
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
            batch.draw(sprite,getBoundingBox().x,getBoundingBox().y);
        }
    }
    
    //Deve ser overriden em todos os filhos de Tile.
    @Override
    public Tile createNew(Tile tile,int x,int y)
    {
        return TileFactory.createTile(this, x, y);
    }
    
    //Deve ser overriden em todos os filhos de CollidableObject
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }
    
}
