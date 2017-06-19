package com.mygdx.game.collidable;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.enemy.AbstractEnemy;
import com.mygdx.game.enemy.WalkerEnemy;
import com.mygdx.game.tile.AbstractTile;
import com.mygdx.game.tile.Hazard;
import com.mygdx.game.tile.LevelEndingTile;
import com.mygdx.game.tile.Repulsor;
import com.mygdx.game.tile.Tile;

public abstract class CollidableObject implements ICollidable
{
    //Essa classe define todos os metodos de colisão possiveis no sistema.
    //Por default, um metodo de colisão nessa classe deve chamar o método de colisão 
    //para o mesmo objeto com typeCast para o pai. Isso é feito para manter o principio
    //de substituição de liskov.
    @Override
    public void handleCollision(ICollidable obj,CollisionInfo info){}
    
    @Override
    public void handleCollision(AbstractTile tile,CollisionInfo info)
    {
        handleCollision((GameObject) tile,info);
    } 
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        handleCollision((AbstractTile) tile,info);
    }      
    
    @Override
    public void handleCollision(DynamicCollider character,CollisionInfo info)
    {
        handleCollision((GameObject) character,info);
    }
    
    @Override
    public void handleCollision(ControllableCharacter player,CollisionInfo info)
    {
        handleCollision((DynamicCollider)player,info);
    }
    @Override
    public void handleCollision(Repulsor repulsor,CollisionInfo info)
    {
        handleCollision((AbstractTile)repulsor,info);
    }
    @Override
    public void handleCollision(AbstractEnemy enemy, CollisionInfo info)
    {
        handleCollision((DynamicCollider)enemy,info);
    }
    
    @Override
    public void handleCollision(WalkerEnemy enemy, CollisionInfo info)
    {
        handleCollision((AbstractEnemy)enemy,info);
    }
    
    @Override
    public void handleCollision(Hazard hazard, CollisionInfo info)
    {
        handleCollision((AbstractTile)hazard,info);
    }
    
    @Override
    public void handleCollision(LevelEndingTile end, CollisionInfo info)
    {
        handleCollision((AbstractTile)end,info);
    }
    
    @Override    
    public abstract void collide(ICollidable obj,CollisionInfo info);

}
