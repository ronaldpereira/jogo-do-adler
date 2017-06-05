/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.mygdx.game.Tiles.Tile;
import com.mygdx.game.Tiles.Repulsor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Tiles.AbstractTile;
/**
 *
 * @author Hiago
 */
public abstract class CollidableObject implements ICollidable
{
    //Essa classe define todos os metodos de colisão possiveis no sistema.
    //Por default, um metodo de colisão nessa classe deve chamar o método de colisão 
    //para o mesmo objeto com typeCast para o pai. Isso é feito para manter o principio
    //de substituição de liskov.
    protected Boolean checksCollision = true;
    protected Boolean blocksMovement = false;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected Animation<TextureRegion> animation;
    private BoundingBox boundingBox;
    
    public CollidableObject(int x,int y,Texture texture)
    {
        setBoundingBox(new BoundingBox(x,y,CollisionMap.tileSize,CollisionMap.tileSize));
        this.texture = texture;
    }
    
    public void animationFromTexture()
    {
        
    }
    public Sprite getSprite()
    {
        sprite = new Sprite(texture);
        return sprite;
    }
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
    
    public void setBlocks(Boolean blocks)
    {
        if(blocks == true)
        {
            checksCollision = true;
        }
        blocksMovement = blocks;
    }
    
    @Override
    public void handleCollision(ICollidable obj,CollisionInfo info){}
    
    @Override
    public void handleCollision(AbstractTile tile,CollisionInfo info)
    {
        handleCollision((CollidableObject) tile,info);
    } 
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        handleCollision((AbstractTile) tile,info);
    }      
    
    @Override
    public void handleCollision(DynamicCollider character,CollisionInfo info)
    {
        handleCollision((CollidableObject) character,info);
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
    public void handleCollision(Enemy enemy, CollisionInfo info)
    {
        handleCollision((DynamicCollider)enemy,info);
    }
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    }

    /**
     * @return the boundingBox
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox the boundingBox to set
     */
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}
