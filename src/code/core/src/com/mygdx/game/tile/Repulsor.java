
package com.mygdx.game.tile;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Axis;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;

public class Repulsor extends AbstractTile
{
    //Essa classe define tiles que "empurram" 
    //o jogador, ou possivelmente outros DynamicColliders
    //em uma determinada direção com uma certa velocidade.
    
    private Axis repulsionAxis;
    private Vector2 repulsionDirection;
    private float repulsionSpeed = 7.5f;
    public Repulsor()
    {
        super(0,0,null,false,false);
        repulsionDirection = new Vector2(0,0);
        
    }
    public Repulsor(int x, int y, TextureSheet sheet,Boolean animates, Axis repulsionAxis,Vector2 repulsionDirection,float repulsionSpeed) {
        super(x, y, sheet , animates, true);
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
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }   

    @Override
    public Repulsor generateFrom() 
    {
        Repulsor clone = new Repulsor(0,0,this.getSheet(),this.getAnimated(),this.getAxis(),
                                    this.getDirection(),this.getRepulsionSpeed());
        return clone;
    }
}