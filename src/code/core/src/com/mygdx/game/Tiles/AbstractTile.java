/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Tiles;

import com.mygdx.game.Tiles.ItileDispatcher;
import com.mygdx.game.Tiles.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CollidableObject;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.ICollidable;

/**
 *
 * @author Hiago
 */
public abstract class AbstractTile extends CollidableObject implements ItileDispatcher
{   
    public AbstractTile(int x,int y,Texture spr,Boolean blocks)
    {
        super(x,y,spr);
        checksCollision = blocks;
        blocksMovement = blocks;
    }
    
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
        if(texture != null)
        {
            batch.draw(texture,getBoundingBox().x,getBoundingBox().y);
        }
    }
    //Deve ser overriden em todos os filhos de AbstractTile
    @Override
    public abstract AbstractTile createNew(AbstractTile tile,int x,int y);
    //Deve ser overriden em todos os filhos de CollidableObject
    @Override
    public abstract void collide(ICollidable obj,CollisionInfo info);
}
