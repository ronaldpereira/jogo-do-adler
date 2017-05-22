/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public class Character {
    private static final float ATTRITION = 50f;
    private static final float GRAVITY = 0.9f;
    private static final float XACCEL = 20f;
    private static final float REACTION = 0.5f;
    private final Vector2 maximumSpeed;

    private Vector2 currentSpeed;
    private Vector2 acceleration;
    public Rectangle boundingBox;
    private Texture sprite;
    
    private CollisionMap map;
    
    public Character(Vector2 initialPos,Texture sprite)
    {
        acceleration = new Vector2(0,0);
        maximumSpeed = new Vector2(10f,10f);
        
        currentSpeed = new Vector2(0,0);
        
        this.boundingBox = new Rectangle();
        this.boundingBox.x = initialPos.x;
        this.boundingBox.y = initialPos.y;
        this.boundingBox.width = sprite.getWidth();
        this.boundingBox.height = sprite.getHeight();
        this.sprite = sprite;
    }
    
    private void applyAcceleration()
    {
        //Aplica atrito:
        if(acceleration.x == 0)
        {
            currentSpeed.x = currentSpeed.x - (signum(currentSpeed.x) * ATTRITION) * Gdx.graphics.getDeltaTime();
            if(abs(currentSpeed.x) < 1f)
            {
                currentSpeed.x = 0;
            }
        }
        
        currentSpeed.mulAdd(acceleration,Gdx.graphics.getDeltaTime());
        
        //Impede que os valores excedam o máximo       
        if(abs(currentSpeed.x) > maximumSpeed.x)
        {
            currentSpeed.x = signum(currentSpeed.x) * maximumSpeed.x;
        }
        if(abs(currentSpeed.y) > maximumSpeed.y)
        {
            currentSpeed.y = signum(currentSpeed.x)* maximumSpeed.y;
        }
    }
    
    private void handleInput()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            if(acceleration.x > 0)
            {
                acceleration.x = -(XACCEL + XACCEL*REACTION);
            }
            acceleration.x = -XACCEL;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(acceleration.x < 0)
            {
                acceleration.x = (XACCEL + XACCEL*REACTION);
            }
            acceleration.x = XACCEL;
        }
        else
        {
            acceleration.x = 0;
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            //Pulo
        }

    }
    public void update()
    {
        handleInput();
        applyAcceleration();
        boundingBox.setPosition(boundingBox.x + currentSpeed.x,currentSpeed.y + boundingBox.y);
    }
    
    private void moveApplyCollisionX()
    {
        Vector2 intersection = map.getVerticalIntersection(boundingBox);
        
    }
}
