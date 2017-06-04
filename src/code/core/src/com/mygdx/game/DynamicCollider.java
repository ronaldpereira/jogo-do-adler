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
    //Essa classe define todos os objetos que se movem e podem colidir
    //com algum tipo de objeto.
    
    protected static final float GRAVITY = 10f;
    protected static final float XACCEL = 15f;
    protected static final float REACTION = 0.075f;
    
    protected final Vector2 maximumSpeed;
    
    protected float attrition = 0.05f;
    protected Boolean grounded = false;
    protected Vector2 currentSpeed;
    protected Vector2 acceleration;
    
    protected CollisionMap map;
    //TESTE
    public int coins = 0;
    //
    
    public DynamicCollider(Vector2 initialPos,Texture sprite)
    {
        blocksMovement = true;
        acceleration = new Vector2(0,-GRAVITY);
        maximumSpeed = new Vector2(8f,100f);
        
        currentSpeed = new Vector2(0,0);
        
        setBoundingBox(new BoundingBox(initialPos.x,initialPos.y,sprite.getWidth(),sprite.getHeight()));
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
            //Cade frame, o objeto perde uma porcentagem da velocidade, indicada
            //pela variavel atrito.
            currentSpeed.x -= (currentSpeed.x) * attrition;
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
    
    //Esse método, define a forma com que o objeto se move.
    //Por exemplo, no caso do jogador, o movimento é definido pela input.    
    public abstract void updateMovement();
    
    public void update()
    {
        //A mudança das variaveis de aceleração/velocidade 
        //são definidas no updateMovement()
        updateMovement();
        //A aceleração é aplicada,respeitando os limites de velocidade.
        applyAcceleration();
        //São tratadas as colisões
        
        handleHorizontalCollision();
        handleVerticalCollision();
    }
    
    protected void setXSpeed(float speed)
    {
        //Quando a velocidade é mudada fora do metodo updateMovement,
        //As colisões devem ser checadas novamente.
        currentSpeed.x = speed;
        if(speed != 0)
        {
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
    }
    
    protected void setYSpeed(float speed)
    {
        currentSpeed.y = speed;
        if(speed != 0)
        {
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
    }
//    
    private void handleHorizontalCollision()
    {
        if(currentSpeed.x != 0)
        {   
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
        }
        getBoundingBox().translate(currentSpeed.x,0);
    }

    private void handleVerticalCollision()
    {
        if(currentSpeed.y != 0)
        {
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
        getBoundingBox().translate(0,currentSpeed.y);
    }
    
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
}
