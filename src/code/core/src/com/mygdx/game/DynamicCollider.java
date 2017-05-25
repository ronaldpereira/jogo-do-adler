/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public abstract class DynamicCollider extends CollidableObject {
    protected static final float GRAVITY = 10f;
    protected static final float XACCEL = 15f;
    protected static final float REACTION = 0.05f;
    
    protected final Vector2 maximumSpeed;
    
    protected float attrition = 17.5f;
    protected Boolean grounded = false;
    protected Vector2 currentSpeed;
    protected Vector2 acceleration;
    protected Texture sprite;
    
    protected CollisionMap map;
    //TESTE
    public int coins = 0;
    
    public DynamicCollider(Vector2 initialPos,Texture sprite)
    {
        acceleration = new Vector2(0,-GRAVITY);
        maximumSpeed = new Vector2(8f,100f);
        
        currentSpeed = new Vector2(0,0);
        
        this.boundingBox = new BoundingBox(initialPos.x,initialPos.y,sprite.getWidth(),sprite.getHeight());
        this.sprite = sprite;
    }
    public void setMap(CollisionMap map)
    {
        this.map = map;
    }
    private void applyAcceleration()
    {
        //Aplica atrito:
        if(acceleration.x == 0)
        {
            currentSpeed.x = currentSpeed.x - (signum(currentSpeed.x) * attrition) * Gdx.graphics.getDeltaTime();
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
    
    public abstract void updateMovement();
    
    public void update()
    {
        updateMovement();
        applyAcceleration();
        handleHorizontalCollision();
        handleVerticalCollision();
    }
    
    private void handleHorizontalCollision()
    {
        if(currentSpeed.x != 0)
        {   
            map.closestHorizontalCollision(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
        }
        boundingBox.translate(currentSpeed.x,0);
    }

    private void handleVerticalCollision()
    {
        if(currentSpeed.y != 0)
        {
            map.closestVerticalCollision(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
        boundingBox.translate(0,currentSpeed.y);
    }
    
    public BoundingBox getPos()
    {
        return boundingBox;
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
   
}
