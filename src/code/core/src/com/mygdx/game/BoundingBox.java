/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class BoundingBox extends Rectangle{
    //TODO:ADICIONAR SKIN
    public BoundingBox(float x,float y,float width,float height)
    {
       super();
       this.x = x;
       this.y = y;
       this.width = width;
       this.height = height;
    }
   
    public void translate(float xDir,float yDir)
    { 
        x += xDir;
        y += yDir;
    }
    
    public float getHorizontalDistance(BoundingBox b)
    {
        if(this.overlaps(b))
        {
            if(this.getRight() >= b.getCenter(Vector2.Zero).x)
            {
                return -(b.getRight() - this.getLeft());
            }
            else
            {
                return -(this.getRight() - b.getLeft());
            }
            //return 0;
        }
        if(b.getLeft() >= this.getRight())
        {
            return b.getLeft() - this.getRight();
        }
        else
        {
            return this.getLeft() - b.getRight();
        }
    }
    
    public float getVerticalDistance(BoundingBox b)
    {
        if(b.getDown() >= this.getUp())
        {
            return b.getDown() - this.getUp();
        }
        else
        {
            return this.getDown() - b.getUp();
        }
    }
    public float getLeft()
    {
        return x;
    }
    public float getRight()
    {
        return x + width;
    }
    public float getUp()
    {
        return y + height;
    }
    public float getDown()
    {
        return y;
    }
}
