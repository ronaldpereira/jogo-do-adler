/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class FollowerCamera extends OrthographicCamera{
    public static final float SKIN = 0.5f;
    OrthographicCamera camera;
    BoundingBox followerBounds;
    BoundingBox bounds;
    CollisionMap map;
    float translationSpeed = 0.25f;//Numero entre 1 e 0
    boolean following = true;
    
    public FollowerCamera(CollisionMap map,float initialX,float initialY,float boundsHeight,float boundsWidth,BoundingBox follow)
    {
        super();
        this.map = map;
        bounds = new BoundingBox(initialX,initialY,boundsWidth,boundsHeight);
        followerBounds = follow;
    }
    
    @Override
    public void update()
    {
        if(following)
        {
            if(!(bounds.contains(followerBounds)))
            {
                if(followerBounds.getRight() > bounds.getRight())
                {
                    bounds.translate(translationSpeed * (SKIN + followerBounds.getRight() - bounds.getRight()),0);
                }
                else if(followerBounds.getLeft() < bounds.getLeft())
                {
                    bounds.translate(translationSpeed * (-SKIN+ followerBounds.getLeft() - bounds.getLeft()),0);
                }

                if(followerBounds.getUp() > bounds.getUp())
                {
                    bounds.translate(0,translationSpeed * (SKIN + followerBounds.getUp() - bounds.getUp()));
                }   
                else if(followerBounds.getDown() < bounds.getDown())
                {
                    bounds.translate(0,translationSpeed * (-SKIN + followerBounds.getDown() - bounds.getDown()));
                }
            }

            if(bounds.x-viewportWidth/2 < 0)
            {
                bounds.x = this.viewportWidth/2;
            }
            else if(bounds.x + viewportWidth/2 > map.getWidth() * map.getTileSize())
            {
                bounds.x =  map.getWidth() * map.getTileSize() - this.viewportWidth/2;
            }

            if(bounds.y-viewportHeight/2 < 0)
            {
                bounds.y = this.viewportHeight/2;
            }
            else if(bounds.y + viewportHeight/2 > map.getHeight() * map.getTileSize())
            {
                bounds.y =  map.getHeight() * map.getTileSize() - this.viewportHeight/2;
            }

            position.x = bounds.x;
            position.y =  bounds.y;
        }
        
        super.update();
    }
    
    public boolean isFollowing()
    {
        return following;
    }
    
    public void setFollowing(boolean follow)
    {
       following = follow;
    }
}
