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
    public BoundingBox boundingBox;
    
    @Override
    public void handleCollision(ICollidable obj,CollisionInfo info){}
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info){}      
    
    @Override
    public void handleCollision(DynamicCollider character,CollisionInfo info){}        

    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }
}
