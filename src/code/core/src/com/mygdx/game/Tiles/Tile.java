/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Tiles;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.ICollidable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 *
 * @author Hiago
 */
public class Tile extends AbstractTile/*extends Tile*/ implements ItileDispatcher
{
    //protected Boolean checksCollision;
    //private Boolean blocksMovement;
    private float attrition = 0.075f;
    private float maxSpeed = 8f;  
    
    public Tile(int x,int y)
    {
        //sprite = new Texture();
        ////TODO: mudar para a constante tileSize.
        super(x,y,null,false);
        checksCollision = false;
        blocksMovement = false;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks)
    {
        super(x,y,spr,blocks);
        checksCollision = blocks;
        blocksMovement = blocks;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks,float attrition,float maxSpeed)
    {
        super(x,y,spr,blocks);
        checksCollision = blocks;
        blocksMovement = blocks;
        this.attrition = attrition;
        this.maxSpeed = maxSpeed;
    }
    
    public void setAttrition(float value)
    {
        attrition = value;
    }
  
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

    //Deve ser overriden em todos os filhos de Tile.
    @Override
    public Tile createNew(AbstractTile tile,int x,int y)
    {
        return TileFactory.createTile(this, x, y);
    }
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    } 


    
}
