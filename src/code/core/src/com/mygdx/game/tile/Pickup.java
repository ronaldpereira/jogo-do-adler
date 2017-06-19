
package com.mygdx.game.tile;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;

public abstract class Pickup extends AbstractTile
{
    //Essa classe define todos os objetos que podem ser pegos pelo personagem
    //Exemplos: moedas, power ups, possivelmente itens especiais.

    public Pickup(int x,int y,TextureSheet sheet,Boolean animates)
    {
        super(x,y,sheet,animates,false);
        checksCollision = true;
    }
    
    //O efeito é o que o objeto faz ao collidir com o player.
    //No caso de uma moeda, o efeito seria incrementar o numero de moedas do player
    public abstract void effect(ControllableCharacter player);
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }
    
    @Override
    public void handleCollision(ControllableCharacter player,CollisionInfo info)
    {
        if(getActive())
        {
            if(getBoundingBox().overlaps(player.getBoundingBox()))
            {
                effect(player);
                checksCollision = false;
                setActive(false);
            }
        }
    }
}
