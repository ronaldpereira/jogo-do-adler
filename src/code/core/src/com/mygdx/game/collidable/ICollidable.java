
package com.mygdx.game.collidable;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.enemy.AbstractEnemy;
import com.mygdx.game.enemy.WalkerEnemy;
import com.mygdx.game.tile.*;

public interface ICollidable
{
    //Interface dos objetos que colidem.
    //Define todas as colisões possíveis.
    public void handleCollision(ICollidable obj,CollisionInfo info);
    public void handleCollision(AbstractTile tile,CollisionInfo info);
    public void handleCollision(Tile tile,CollisionInfo info);
    public void handleCollision(DynamicCollider character,CollisionInfo info);
    public void handleCollision(ControllableCharacter player,CollisionInfo info);
    public void handleCollision(AbstractEnemy enemy,CollisionInfo info);
    public void handleCollision(Repulsor repulsor,CollisionInfo info);
    public void handleCollision(WalkerEnemy enemy, CollisionInfo info);
    public void handleCollision(Hazard hazard, CollisionInfo info);
    public void handleCollision(LevelEndingTile end, CollisionInfo info);
    public void collide(ICollidable obj,CollisionInfo info);
}
