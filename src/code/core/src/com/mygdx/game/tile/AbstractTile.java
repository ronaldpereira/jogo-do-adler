
package com.mygdx.game.tile;

import com.mygdx.game.collidable.GameObject;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.IBuildableObject;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.map.TileMap;

public abstract class AbstractTile extends GameObject implements IBuildableObject
{   
    //Classe da qual todos os objetos estaticos(tiles) devem herdar.
    public Boolean autotiles = false;
    public AbstractTile()
    {
        super(0,0,null,false);
    }
    public AbstractTile(int x,int y,TextureSheet sheet,Boolean animates,Boolean blocks)
    {
        super(x,y,sheet,animates);
        checksCollision = blocks;
        blocksMovement = blocks;
    }
    
    @Override
    public abstract void collide(ICollidable obj,CollisionInfo info);

    @Override
    public abstract AbstractTile generateFrom();
    
    //Método para construção no mapBuilder:
    @Override
    public void build(CollisionMap map,int x,int y)
    {
        AbstractTile objectToBuild = this.generateFrom();
        objectToBuild.getBoundingBox().setPosition(x * TileMap.getTileSize(),y * TileMap.getTileSize());
        map.createTile(x, y, objectToBuild);
    }

}
