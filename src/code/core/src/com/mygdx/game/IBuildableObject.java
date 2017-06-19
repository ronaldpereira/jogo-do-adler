
package com.mygdx.game;

import com.mygdx.game.map.CollisionMap;

public interface IBuildableObject extends IGenerator
{
    //Interface dos objetos que podem ser criados através do mapBuilder.
    public TextureSheet getSheet();
    public void build(CollisionMap map,int x,int y);
    
}
