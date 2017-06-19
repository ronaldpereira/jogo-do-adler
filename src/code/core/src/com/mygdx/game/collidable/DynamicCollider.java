
package com.mygdx.game.collidable;
import com.mygdx.game.map.CollisionMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Axis;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.TextureSheet;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

public abstract class DynamicCollider extends GameObject 
{
    //Essa classe define todos os objetos que se movem e podem colidir
    //com algum tipo de objeto.
    
    protected static final float GRAVITY = 10f;
    
    protected float jumpSpeed = 4.2f;
    protected float xAccel = 7f;
    protected float reaction = 0.075f;
    protected final Vector2 maximumSpeed;
    protected float attrition = 0.05f;
    protected Boolean grounded = false;
    protected Vector2 currentSpeed;
    protected Vector2 currentAccel;
    protected Vector2 SpawnPoint;
    transient protected CollisionMap map;
    protected Boolean flipImageDirection = false;
    
    public DynamicCollider(Vector2 initialPos,TextureSheet sheet,Boolean animates)
    {
        super((int)initialPos.x,(int)initialPos.y,sheet,animates);
        SpawnPoint = initialPos;
        blocksMovement = true;
        //Todos os objetos dinamicos são afetados pela gravidade, por default.
        currentAccel = new Vector2(0,-GRAVITY);
        maximumSpeed = new Vector2(7f,20f);
        currentSpeed = new Vector2(0,0);
    }
   
    private void applyAcceleration()
    {
        //Aplica atrito:
        if(currentAccel.x == 0 && grounded)
        {
            //Cade frame, o objeto perde uma porcentagem da velocidade, indicada
            //pela variavel atrito.
            currentSpeed.x -= (currentSpeed.x) * attrition;
            if(abs(currentSpeed.x) < 1f)
            {
                currentSpeed.x = 0;
            }
        }
        currentSpeed.mulAdd(currentAccel,Gdx.graphics.getDeltaTime());
        
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
    @Override
    public void drawSelf(SpriteBatch batch)
    {
        if(getActive() && sheet != null)
        {
            //Só desenha o personagem se ele está ativo.
            TextureRegion temp;
            if(getAnimated())
            {
                stateTime += Gdx.graphics.getDeltaTime();
                temp = getCurrentAnimation().getKeyFrame(stateTime,true);
                //Desenha o personagem dependendo de sua direção:
                batch.draw(temp,(flipImageDirection ? getBoundingBox().getX() : getBoundingBox().getX() + temp.getRegionWidth()),getBoundingBox().getY(),(flipImageDirection ? 1 : -1) * temp.getRegionWidth(),temp.getRegionHeight());
            }
            else
            {
                temp = sheet.getFrame(defaultFrame);
                //Desenha o personagem dependendo de sua direção:
                batch.draw(temp,(flipImageDirection ? getBoundingBox().getX() : getBoundingBox().getX() + temp.getRegionWidth()),getBoundingBox().getY(),(flipImageDirection ? 1 : -1) * temp.getRegionWidth(),temp.getRegionHeight());
            }
        }
    }
    
    //Esse método, define a forma com que o objeto se move.
    //Por exemplo, no caso do jogador, o movimento é definido pela input.    
    public abstract void updateMovement();
     
    public void update()
    {
        if(getActive())
        {
            
            //A mudança das variaveis de aceleração/velocidade 
            //são definidas no updateMovement()
            updateMovement();
            //A aceleração é aplicada,respeitando os limites de velocidade.
            applyAcceleration();
            //São tratadas as colisões
            
            map.updateNearby(this);
            handleHorizontalCollision();
            handleVerticalCollision();
        }
    }
    
    //Os metodos setXSpeed,setYSpeed e setSpeed permitem que o personagem mude sua velocidade
    //dentro de qualquer método, mas para que isso seja possivel, as colisões devem ser
    //checadas novamente.
    protected void setSpeed(float xSpeed,float ySpeed)
    {
       
        currentSpeed = new Vector2(xSpeed,ySpeed);
        if(xSpeed != 0|| ySpeed != 0)
        {
            //Quando a velocidade é mudada fora do metodo updateMovement,
            //As colisões devem ser checadas novamente.
            map.updateNearby(this);
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
    }
    
    protected void setXSpeed(float speed)
    {
        currentSpeed.x = speed;
        if(speed != 0)
        {
            map.updateNearby(this);
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
    }
    
    protected void setYSpeed(float speed)
    {
        //Quando a velocidade é mudada fora do metodo updateMovement,
        //As colisões devem ser checadas novamente.
        currentSpeed.y = speed;
        if(speed != 0)
        {
            map.updateNearby(this);
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
            map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
    }

    private void handleHorizontalCollision()
    {
        //Executa as colisões horizontais e aplica o movimento horizontal;
        if(currentSpeed.x != 0)
        {   
            map.closestCollisionX(this, (int)signum(currentSpeed.x),abs(currentSpeed.x));
        }
        
        getBoundingBox().translate(currentSpeed.x,0);
    }

    private void handleVerticalCollision()
    {
        //Executa as colisões verticais e aplica o movimento vertical;
        if(currentSpeed.y != 0)
        {
             map.closestCollisionY(this, (int)signum(currentSpeed.y),abs(currentSpeed.y));
        }
        
        getBoundingBox().translate(0,currentSpeed.y);
    }
    
    protected void collideStopMovement(BoundingBox box, Axis axis) 
    {
        //Para o movimento no eixo se ouver uma colisão no caminho.
        if(axis == HORIZONTAL_AXIS)
        {
            int direction = (int)signum(currentSpeed.x);
            int distance = (int)getBoundingBox().getHorizontalDistance(box);
            currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);   
        }
        else
        {
            int direction = (int)signum(currentSpeed.y);
            int distance = (int)getBoundingBox().getVerticalDistance(box);
            currentSpeed.y = min(distance,abs(currentSpeed.y)) * direction;
        }
    }
    
    //Getters/setters:
    public boolean isGrounded()
    {
        return grounded;
    }
     public Vector2 getCurrentSpeed()
    {
        return currentSpeed;
    }
    public void setXAccel(float accel)
    {
        currentAccel.x = accel;
    }
    public void setYAccel(float accel)
    {
        currentAccel.y = accel;
    }
    public Vector2 getAccel()
    {
        return currentAccel;
    }
    public float getAccelSpeed()
    {
        return xAccel;
    }
    public float getReaction()
    {
        return reaction;
    }
    public float getJumpSpeed()
    {
        return jumpSpeed;
    }
    public void setMap(CollisionMap map)
    {
        this.map = map;
    }
    
}
