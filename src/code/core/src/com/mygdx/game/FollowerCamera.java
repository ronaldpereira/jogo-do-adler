/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 *
 * @author Hiago
 */
public class FollowerCamera extends OrthographicCamera{
    public static final float SKIN = 0.5f;
    OrthographicCamera camera;
    BoundingBox followerBounds;
    BoundingBox bounds;
    
    public FollowerCamera(float initialX,float initialY,float boundsHeight,float boundsWidth,BoundingBox follow)
    {
        super();
        bounds = new BoundingBox(initialX,initialY,boundsWidth,boundsHeight);
        followerBounds = follow;
    }
    
    @Override
    public void update()
    {
        if(!(bounds.contains(followerBounds)))
        {
            if(followerBounds.getRight() > bounds.getRight())
            {
                bounds.translate(SKIN + followerBounds.getRight() - bounds.getRight(),0);
            }
            else if(followerBounds.getLeft() < bounds.getLeft())
            {
                bounds.translate(-SKIN+ followerBounds.getLeft() - bounds.getLeft(),0);
            }
            
            if(followerBounds.getUp() > bounds.getUp())
            {
                bounds.translate(0,SKIN + followerBounds.getUp() - bounds.getUp());
            }   
            else if(followerBounds.getDown() < bounds.getDown())
            {
                bounds.translate(0,-SKIN + followerBounds.getDown() - bounds.getDown());
            }
        }
        
        position.x = bounds.x;
        position.y = bounds.y;
        super.update();
    }
    
    
}
