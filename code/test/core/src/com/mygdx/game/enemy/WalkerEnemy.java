package com.mygdx.game.enemy;

import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.tile.AbstractTile;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

public class WalkerEnemy extends AbstractEnemy
{
    //Inimigo que anda em uma direção até encontrar um obstaculo, depois muda
    //para a direção contrária.
    
    protected float walkSpeed = 3f;
    private WalkerEnemy()
    {
        super(new Vector2(0,0),null,false);
    }
    
    public WalkerEnemy(Vector2 initialPos, TextureSheet sheet, Boolean animates) {
        super(initialPos,sheet,animates);
    }
    
    @Override
    public void updateMovement() 
    {
        setOrigin();
        
        //Interceção vertical do inimigo com o mapa:
        Vector2 inter = map.getTileMap().getVerticalIntersection(this.getBoundingBox());
        
        Boolean frontBlocked = false;
        Vector2 tilePosition;
        
        for(int i = (int)inter.x ; i <= inter.y ;i++)
        {
            //Verifica se o(s) tile(s) imediatamente na frente desse personagem esta bloqueado:
            tilePosition = map.getTileMap().findTile(xOrigin,i * map.getTileMap().getTileSize());
            
            if(map.getTileMap().getTile((int)tilePosition.x + direction, (int)tilePosition.y).getBlocks())
            {
                frontBlocked = true;
            }
            
        }
        
        Boolean frontEmpty;
        tilePosition = map.getTileMap().findTile(xOrigin, getBoundingBox().getY());
        //Verifica se o personagem vai cair se ele andar mais um tile:
        frontEmpty = !map.getTileMap().getTile((int)tilePosition.x + direction, (int)tilePosition.y - 1).getBlocks();
        
        //Se um dos dois é verdade, o personagem muda de direção.
        if(frontEmpty || frontBlocked)
        {

            direction *= -1;

        }
        currentSpeed.x = walkSpeed * direction;
        flipImageDirection = signum(currentSpeed.x) < 0;  
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }

    @Override
    public AbstractEnemy generateFrom() 
    {
        WalkerEnemy clone =  new WalkerEnemy(new Vector2(0,0), this.getSheet(), this.getAnimated());
        clone.setBoundingBox(new BoundingBox(0,0,this.getBoundingBox().width,this.getBoundingBox().height));
        return clone;
    }
    
        @Override
    public void handleCollision(AbstractTile tile,CollisionInfo info)
    {
        //Bloqueia o caminho desse objeto na colisão.
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            if(tile.getBlocks())
            {
                int distance = (int)getBoundingBox().getHorizontalDistance(tile.getBoundingBox());
                currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);
            }
        }
        else 
        {
            if(tile.getBlocks())
            {
                int yDirection = (int)signum(currentSpeed.y);
                int distance = (int)getBoundingBox().getVerticalDistance(tile.getBoundingBox());
                currentSpeed.y = min(distance,abs(currentSpeed.y)) * yDirection;

                if(yDirection <= 0 && abs((int)distance) == 0)
                {
                    grounded = true;
                }
            } 
        }
    }
    
}
