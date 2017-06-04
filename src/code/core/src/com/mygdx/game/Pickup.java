/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Hiago
 */
public abstract class Pickup extends Tile
{
    //Essa classe define todos os objetos que podem ser pegos pelo personagem
    //Exemplos: moedas, power ups, possivelmente itens especiais.
    protected Boolean active;

    public Pickup(int x,int y,Texture spr)
    {
        super(x,y,spr,false);
        active = true;
        checksCollision = true;
    }
    
    //O efeito é o que o objeto faz ao collidir com o player.
    //No caso de uma moeda, o efeito seria incrementar o numero de moedas do player
    public abstract void effect(DynamicCollider player);
    
    @Override 
    public void drawSelf(SpriteBatch batch)
    {
        //Uma instancia dessa classe só é desenhada se ela esta ativa,
        //ou seja, se ela ainda não foi pega pelo player.
        if(active)
        {
            batch.draw(this.getSprite(),getBoundingBox().x,getBoundingBox().y);
        }
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }
    
    @Override
    public void handleCollision(ControllableCharacter player,CollisionInfo info)
    {
        if(active)
        {
            if(getBoundingBox().overlaps(player.getBoundingBox()))
            {
                effect(player);
                checksCollision = false;
                active = false;
            }
        }
    }
}
