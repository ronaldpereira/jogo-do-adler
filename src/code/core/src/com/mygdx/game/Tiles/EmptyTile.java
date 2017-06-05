/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.Tiles;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.ICollidable;

/**
 *
 * @author Hiago
 */
public class EmptyTile extends AbstractTile
{
    public EmptyTile(int x,int y)
    {
        super(x,y,null,false);
    }
    @Override
    public EmptyTile createNew(AbstractTile tile,int x,int y)
    {
        return TileFactory.createEmpty(x,y);
    }
    
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }   
}
