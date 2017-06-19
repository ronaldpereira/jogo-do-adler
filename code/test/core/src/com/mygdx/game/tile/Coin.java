
package com.mygdx.game.tile;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;

public class Coin extends Pickup
{
    //Define uma moeda que incrementa o score do player na colisão.
    
    private int value = 5;
    public Coin()
    {
        super(0,0,null,false);
    }
    
    public Coin(int x,int y,TextureSheet sheet,Boolean animates)
    {
        super(x,y,sheet,animates);
    }
    
    @Override
    public void effect(ControllableCharacter player)
    {
        player.addScore(value);
    }

    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    } 

    @Override
    public Coin generateFrom() {
        Coin clone = new Coin(0,0,this.getSheet(),this.getAnimated());
        return clone;
    }
}
