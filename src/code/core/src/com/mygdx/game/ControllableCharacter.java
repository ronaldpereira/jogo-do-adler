/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public class ControllableCharacter extends DynamicCollider
{
    public ControllableCharacter(Vector2 initialPos, Texture sprite) {
        super(initialPos, sprite);
        blocksMovement = false;
        //TROCAR POR SKIIIIN
        boundingBox.width -=1f;
        boundingBox.height -=1f;
    }
    
    private void handleInput()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(acceleration.x > 0)
            {
                acceleration.x = -(XACCEL + XACCEL*REACTION);
            }
            else
            {
                acceleration.x = -XACCEL;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(acceleration.x < 0)
            {
                acceleration.x = (XACCEL + XACCEL*REACTION);
            }
            else
            {
                acceleration.x = XACCEL;
            }
        }
        else
        {
            acceleration.x = 0;
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            //PULA
            if(grounded)
            {
                currentSpeed.y = 5f;
                grounded = false;
            }
        }
    }
    
    @Override
    public void updateMovement()
    {
        handleInput();
    }
        
    @Override
    public void handleCollision(Enemy enemy,CollisionInfo info)
    {
        if(boundingBox.overlaps(enemy.boundingBox))
        {
            if(info.getAxis() == HORIZONTAL_AXIS)
            {
                setXSpeed(-signum(enemy.boundingBox.x - boundingBox.x) * 5);
            }
            else
            {
                setYSpeed(-signum(enemy.boundingBox.y - boundingBox.y) * 5);
            }
        }
    }
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        //System.out.println("Colide com tile!!!!!!");
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            if(tile.getBlocks())
            {
                int distance = (int)boundingBox.getHorizontalDistance(tile.boundingBox);
                currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);
                
                //System.out.println("Speed: "+ currentSpeed.x + "TilePosition:" + tile.boundingBox.x +"," + tile.boundingBox.y);
            }
        }
        else
        {
            System.out.println(tile.getBlocks());
            if(tile.getBlocks())
            {
                
                int direction = (int)signum(currentSpeed.y);
                int distance = (int)boundingBox.getVerticalDistance(tile.boundingBox);
                currentSpeed.y = min(distance,abs(currentSpeed.y)) * direction;

                if(direction < 0 && distance == 0)
                {
                    grounded = true;
                }

                if(grounded == true)
                {
                    attrition = tile.getAttrition();
                    maximumSpeed.x = tile.getMaxSpeed();
                }
            }
            
        }
    }
    
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
    
}
