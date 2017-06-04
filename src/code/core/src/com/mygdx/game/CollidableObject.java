/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
/**
 *
 * @author Hiago
 */
public abstract class CollidableObject implements ICollidable
{
    //Essa classe define todos os metodos de colisão possiveis no sistema.
    //Por default, um metodo de colisão nessa classe deve chamar o método de colisão 
    //para o mesmo objeto com typeCast para o pai. Isso é feito para manter o principio
    //de substituição de liskov.
    protected Boolean checksCollision = true;
    protected Boolean blocksMovement = false;
    protected Texture sprite;
    private BoundingBox boundingBox;
    
    public Texture getSprite()
    {
        return sprite;
    }
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
    
    public void setBlocks(Boolean blocks)
    {
        if(blocks == true)
        {
            checksCollision = true;
        }
        blocksMovement = blocks;
    }
    
    @Override
    public void handleCollision(ICollidable obj,CollisionInfo info){}
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        handleCollision((CollidableObject) tile,info);
    }      
    
    @Override
    public void handleCollision(DynamicCollider character,CollisionInfo info)
    {
        handleCollision((CollidableObject) character,info);
    }
    
    @Override
    public void handleCollision(ControllableCharacter player,CollisionInfo info)
    {
        handleCollision((DynamicCollider)player,info);
    }
    @Override
    public void handleCollision(Repulsor repulsor,CollisionInfo info)
    {
        handleCollision((Tile)repulsor,info);
    }
    @Override
    public void handleCollision(Enemy enemy, CollisionInfo info)
    {
        handleCollision((DynamicCollider)enemy,info);
    }
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }

    /**
     * @return the boundingBox
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox the boundingBox to set
     */
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}
