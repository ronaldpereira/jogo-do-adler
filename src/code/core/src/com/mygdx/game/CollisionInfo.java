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
public class CollisionInfo
{
    //Guarda informações sobre a colisão.
    private final ICollidable collisionObject;
    private final Axis axis;

    public CollisionInfo(ICollidable obj, Axis axis)
    {
        this.collisionObject = obj;
        this.axis = axis;
    }

    public ICollidable getObj()
    {
        return collisionObject;
    }
    
    public Axis getAxis()
    {
        return axis;
    }
}