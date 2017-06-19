
package com.mygdx.game.tile;

import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.level.Level;

public class LevelEndingTile extends AbstractTile
{
    //Sinaliza o fim de um levelNode
    
    Level level;
    private LevelEndingTile()
    {
        super();
        
    }
    
    public LevelEndingTile(int x,int y,TextureSheet sheet,Boolean animates,Level level)
    {
        super(x,y,sheet,animates,true);
        this.level = level;
    }
    
    public void effect()
    {
        //Atualiza o level atual.
        level.nextLevel();
    }
    
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    } 
    
    public Level getLevel()
    {
        return level;
    }

    @Override
    public LevelEndingTile generateFrom() 
    {
        LevelEndingTile clone = new LevelEndingTile(0,0,this.getSheet(),this.getAnimated(),this.getLevel());
        return clone;
    }
}
