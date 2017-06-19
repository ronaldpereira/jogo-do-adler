
package com.mygdx.game.collidable;

import com.mygdx.game.map.CollisionMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.map.TileMap;

public abstract class GameObject extends CollidableObject implements ICollidable
{
    //Classe que define todos os objetos que interagem com o mapa/outros personagens.
    protected Boolean checksCollision = true;
    protected Boolean blocksMovement = false;
    protected int defaultFrame = 0;
    
    private Boolean active;
    private Boolean animated;
    protected TextureSheet sheet;
    
    transient private Animation<TextureRegion>[] possibleAnimations;
    transient private Animation<TextureRegion> currentAnimation;
    
    private BoundingBox boundingBox;
    protected float stateTime;
    
    //Construtor vazio para serialização:
    private GameObject()
    {}
    
    public GameObject(int x,int y,TextureSheet sheet,boolean animated)
    {
        //A bounding box, por default tem o tamanho tileSize x tileSize,
        //mas isso pode ser mudado para objetos que dinamicos.
        
        setBoundingBox(new BoundingBox(x,y,TileMap.getTileSize(),TileMap.getTileSize()));
        active = true;
        this.sheet = sheet;
        this.animated = animated;
        stateTime = 0f;
        
        if(this.animated)
        {
            //Pega todas as animações na textureSheet
            this.possibleAnimations = this.sheet.toAnimation();
            //Usa a primeira como default.
            //*Isso pode ser mudado depois com a mudança de estado do objeto.
            this.currentAnimation = possibleAnimations[0];
        }
    }
    
    public void drawSelf(SpriteBatch batch)
    {
        if(active && sheet != null)
        {
            if(animated)
            {
                stateTime += Gdx.graphics.getDeltaTime();
                batch.draw(getCurrentAnimation().getKeyFrame(stateTime,true),boundingBox.getX(),boundingBox.getY());
            }
            else
            {
                batch.draw(sheet.getFrame(defaultFrame),boundingBox.getX(),boundingBox.getY());
            }
        }
    }
    
    //Getters/Setter:
    public Boolean getActive()
    {
        return this.active;
    }
   
    public void setActive(Boolean act)
    {
        active = act;
        if(!active)
        {
            //Se o objeto não está ativo, ele não colide nem bloqueia o caminho.
            checksCollision = false;
            blocksMovement = false;
        }
    }
    
    public void setDefaultFrame(int index)
    {
        defaultFrame = index;
    }
    
    public TextureSheet getSheet()
    {
        return sheet;
    }
    
    public Boolean getAnimated()
    {
        return animated;
    }
    
    public Boolean getCollides()
    {
        return checksCollision;
    }
    
    public void setCollides(Boolean checks)
    {
        checksCollision = checks;
    }
 
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
    
    public void setBlocks(Boolean blocks)
    {
        if(blocks == true)
        {
            //Se um objeto bloqueia o caminho, ele obviamente colide.
            checksCollision = true;
        }
        blocksMovement = blocks;
    }   
    
    public BoundingBox getBoundingBox() 
    {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) 
    {
        this.boundingBox = boundingBox;
    }
    
    public void setCurrentAnimation(int index)
    {
        if(index < possibleAnimations.length)
        {
            currentAnimation = possibleAnimations[index];
        }
    }
    public Animation<TextureRegion> getCurrentAnimation()
    {
        if(currentAnimation == null)
        {
            possibleAnimations = sheet.toAnimation();
            //Usa a primeira como default.
            //*Isso pode ser mudado depois com a mudança de estado do objeto.
            currentAnimation = possibleAnimations[0];
        }
        return currentAnimation;
    }
}
