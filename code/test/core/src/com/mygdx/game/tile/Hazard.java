
package com.mygdx.game.tile;

import com.mygdx.game.BoundingBox;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.map.TileMap;


public class Hazard extends AbstractTile
{
    //Representa obstaculos que podem matar/causar dano ao personagem. 
    //Objetos dessa classe possuem uma area retangular "dangerArea" que
    //causa danos quando o jogador entra em colisão com ela.
    //Essa danger area pode ser customizada para criar obstaculos variados.
    BoundingBox dangerArea;
    private Hazard()
    {
        super();
    }
    public Hazard(int x,int y,TextureSheet sheet,Boolean animates)
    {
        super(x,y,sheet,animates,true);
        this.checksCollision = true;
        dangerArea = new BoundingBox(0,0,getBoundingBox().width,getBoundingBox().height);
    }
    
    @Override
    public void collide(ICollidable obj, CollisionInfo info) {
        obj.handleCollision(this,info);
    }
    public void setDangerArea(BoundingBox box)
    {
        //A posição da danger area dever ser relativa à posição da boundingBox
        dangerArea = new BoundingBox(box);
        dangerArea.setPositionRelative(getBoundingBox());
    }
    
    public BoundingBox getDangerArea()
    {
        return dangerArea;
    }
    
    @Override
    public void build(CollisionMap map,int x,int y)
    {
        //O método é sobescrito pois a dangerArea do objeto criado tem que ser 
        //atualizada quando sua posição é mudada.
        Hazard objectToBuild = this.generateFrom();
        objectToBuild.getBoundingBox().setPosition(x * TileMap.getTileSize(),y * TileMap.getTileSize());
        objectToBuild.setDangerArea(getDangerArea());
        map.createTile(x, y, objectToBuild);
    }
    
    @Override
    public Hazard generateFrom() {
        Hazard clone = new Hazard(0,0,this.getSheet(),this.getAnimated());
        clone.setDangerArea(this.getDangerArea());
        return clone; 
    }
}
