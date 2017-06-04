/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class Repulsor extends Tile
{
    //Essa classe define tiles que "empurram" 
    //o jogador, ou possivelmente outros DynamicColliders
    //em uma determinada direção com uma certa velocidade.
    
    private Axis repulsionAxis;
    private Vector2 repulsionDirection;
    private float repulsionSpeed = 7.5f;

    public Repulsor(int x, int y, Texture spr, Axis repulsionAxis,Vector2 repulsionDirection,float repulsionSpeed) {
        super(x, y, spr, true);
        this.repulsionDirection = repulsionDirection;
        this.repulsionAxis = repulsionAxis;
        this.repulsionSpeed = repulsionSpeed;
    }
    public Vector2 getDirection()
    {
        return repulsionDirection;
    }
    
    public float getRepulsionSpeed()
    {
        return repulsionSpeed;
    }
    public Axis getAxis()
    {
        return repulsionAxis;
    }
    
    public Vector2 getRepulsion()
    {
        return new Vector2(repulsionDirection).scl(repulsionSpeed);
    }
    
    @Override
    public Repulsor createNew(Tile tile,int x,int y)
    {
        return TileFactory.createTile(this, x, y);
    }
    
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }   
}