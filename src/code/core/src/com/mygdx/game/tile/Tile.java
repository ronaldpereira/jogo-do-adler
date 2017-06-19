
package com.mygdx.game.tile;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.TextureSheet;

public class Tile extends AbstractTile 
{
    //Tile genérico que pode ou não bloquear o caminho do personagem.
    //Quando o jogador está acima de um Tile, seu atrito/maxSpeed é atualizado
    //Isso permite a criação de tiles variados como gelo, tiles que permitem o personagem
    //andar mais rápido, etc.
    
    //Valores default:
    private float attrition = 0.25f;
    private float maxSpeed = 8f;  
    
    //Construtor vazio para serialização.
    private Tile()
    {
        super();
        autotiles = true;
    }
    
    public Tile(int x,int y,TextureSheet sheet,Boolean animates,Boolean blocks,Boolean autotiles)
    {
        super(x,y,sheet,animates,blocks);
        checksCollision = blocks;
        blocksMovement = blocks;
        this.autotiles = autotiles;      
    }
    
    public Tile(int x,int y,TextureSheet sheet,Boolean animates,Boolean blocks,float attrition,float maxSpeed,Boolean autotiles)
    {
        super(x,y,sheet,animates,blocks);
        
        checksCollision = blocks;
        blocksMovement = blocks;
        this.attrition = attrition;
        this.maxSpeed = maxSpeed;
        this.autotiles = autotiles;
    }
    
    public void setAutoTiles(Boolean value)
    {
        autotiles = value;
    }
    
    public void setAttrition(float value)
    {
        attrition = value;
    }
  
    public float getAttrition()
    {
        return attrition;
    }
    
    public void setMaxSpeed(float value)
    {
        maxSpeed = value;
    }
    
    public float getMaxSpeed()
    {
        return maxSpeed;
    }
    
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    } 

    @Override
    public Tile generateFrom() 
    {
        Tile clone = new Tile(0,0,this.getSheet(),this.getAnimated(),this.getBlocks(),
                                this.getAttrition(),this.getMaxSpeed(),this.autotiles);
        clone.autotiles = this.autotiles;
        
        return clone;
    }
}
