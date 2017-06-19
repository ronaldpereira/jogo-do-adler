
package com.mygdx.game.tile;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.map.TileMap;

public class EmptyTile extends AbstractTile implements Serializable
{
    //Define um tile vazio.
    
    //Construtor vazio para serialização:
    public EmptyTile()
    {
        super(0,0,null,false,false);
    }
    public EmptyTile(int x,int y)
    {
        super(x,y,null,false,false);
    }
    
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }   

    //A serialização do tile vazio é customizada para guardar espaço.
    @Override
    public void write(Json json) 
    {
        //As únicas variaveis relevantes são o x e o y do objeto.
        json.writeValue("x",this.getBoundingBox().getX());
        json.writeValue("y",this.getBoundingBox().getY());
    }

    @Override
    public void read(Json json, JsonValue jsonData) 
    {
        this.setBoundingBox(new BoundingBox(jsonData.getInt("x"),
            jsonData.getInt("y"),TileMap.getTileSize(),TileMap.getTileSize()));
    }

    @Override
    public EmptyTile generateFrom() {
        return new EmptyTile(0,0);
    }
}
