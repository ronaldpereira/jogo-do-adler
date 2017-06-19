
package com.mygdx.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.tile.AbstractTile;
import com.mygdx.game.tile.EmptyTile;

public class TileMap 
{
    //Essa classe armazena uma matriz de tiles e define operações para a
    //manipulação dessa.
    private AbstractTile[][] map;
    private static int tilesize = 32;
    int height;
    int width;
           
    //Construtor vazio para serialização.
    private TileMap()
    {
        
    }
    
    public TileMap(AbstractTile[][] map)
    {
        this.width = map.length;
        this.height = map[0].length;
        
        this.map = map;
    }
    public TileMap(int width, int height)
    { 
        this.width = width;
        this.height = height;
        map = new AbstractTile[width][height];
        
        for(int i = 0;i < width;i++)
        {
            for(int j = 0;j < height;j++)
            {
                map[i][j] = new EmptyTile(i * tilesize, j *  tilesize);
            }
        }
    }
    
    //Encontra qual é a posição do objeto no tileMap.
    public Vector2 findTile(float x,float y)
    {       
        return new Vector2((int)x/tilesize,(int)y/tilesize);
    }
    
    //Retorna o tile na posição [x][y].
    public AbstractTile getTile(int x,int y)
    {
        if(checkXBounds(x) && checkYBounds(y))
        {
            return map[x][y];
        }    
        else
        {
            return null;
        }
    }
    
    public void setTileMap(AbstractTile[][] newMap)
    {
        this.map = newMap;
        this.width = newMap.length;
        this.height = newMap[0].length;
    }
    
    public void setTile(int x,int y, AbstractTile tile)
    {
        if(checkXBounds(x) && checkYBounds(y))
        {
            map[x][y] = tile;
        }
    }
    
    public static int getTileSize()
    {
        return tilesize;
    }
    
    public AbstractTile[][] getMap()
    {
        return map;
    }
    
    //Verifica se o valor está dentro dos limites do mapa.
    public Boolean checkXBounds(int x)
    {
        return (x >= 0 && x < width);
    }
    
    //Verifica se o valor está dentro dos limites do mapa.
    public Boolean checkYBounds(int y)
    {
        return (y >= 0 && y < height);
    }
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    //Encontra os tiles com o qual o objeto tem interceção horizontal
    public Vector2 getHorizontalIntersection(BoundingBox boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).x);
        int max = (int)(findTile((boundingBox.x + boundingBox.width),boundingBox.y).x);

        return new Vector2(min,max);
    }
    
    //Encontra os tiles com o qual o objeto tem interceção Vertical
    public Vector2 getVerticalIntersection(Rectangle boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).y);
        int max = (int)(findTile(boundingBox.x,(boundingBox.y + boundingBox.height)).y);
        
        return new Vector2(min,max);
    }
    
    //Verifica se existe interceção entre duas ranges de valores.
    ////(mudar para uma classe de utilidade eventualmente)
    public boolean checkIntersection(Vector2 a,Vector2 b)
    {
        if(a.x <= b.y && a.x >= b.x || a.y <= b.y && a.y >= b.x)
        {
            return true;
        }
        else if(b.x <= a.y && b.x >= a.x || b.y <= a.y && b.y >= a.x)
        {
            return true;
        }
        return false;
    }
    
    //Verifica se um valor está dentro de uma range.
    ////(mudar para uma classe de utilidade eventualmente)
    public boolean pointInRange(int point,Vector2 range)
    {
        return point >= range.x && point <= range.y;
    }
    
    //Desenha todos os tiles.
    public void renderMap(SpriteBatch batch)
    {
        for(int i = 0;i < width;i++)
          {
              for(int j =0;j < height;j++)
              {
                  map[i][j].drawSelf(batch);
              }
          }   
    }
}