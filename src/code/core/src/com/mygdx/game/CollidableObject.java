/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

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
    public BoundingBox boundingBox;
    
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
    
    public void setBlock(Boolean blocks)
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
    public void handleCollision(Tile tile,CollisionInfo info){}      
    
    @Override
    public void handleCollision(DynamicCollider character,CollisionInfo info){}
    
    @Override
    public void handleCollision(ControllableCharacter player,CollisionInfo info)
    {
        handleCollision((DynamicCollider)player,info);
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
}
