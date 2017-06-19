
package com.mygdx.game.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.IDestroyable;
import com.mygdx.game.collidable.DynamicCollider;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;


public abstract class AbstractProjectile extends DynamicCollider implements IDestroyable
{
    //Essa classe represental projéteis disparados por inimigos/armadilhas 
    //ou pelo próprio jogador.
    //*A funcionalidade não está completamente implementada ainda.
    Vector2 direction;
    float speed;
    
    public AbstractProjectile(Vector2 initialPosition,TextureSheet sheet,Boolean animates,Vector2 direction,float speed)
    {
        super(initialPosition,sheet,animates);
        this.direction = direction;
        
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }

    @Override
    public abstract void updateMovement();
    
    @Override
    public void handleCollision(ICollidable obj,CollisionInfo info)
    {
        setActive(false);
    }
   
}
