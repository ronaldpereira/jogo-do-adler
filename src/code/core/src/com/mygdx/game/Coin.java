/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Hiago
 */
public class Coin extends Pickup
{
    //
    Animation<TextureRegion> coinspr;
    Texture coinSheet;
    
    float stateTime;
    //TextureAtlas atlas;
   
    private int value = 5;
    public Coin(int x,int y,Texture spr)
    {
        
        //atlas = new TextureAtlas(Gdx.files.internal("CoinAnim"))
        super(x,y,spr);
        coinSheet = new Texture("Coin.png");
        if(coinSheet == null)
        {
            System.out.println("Ue");
        }
        
        //TESTE DE ANIMAÇÃO, MUDAR!!!
        TextureRegion[][] temp = TextureRegion.split(coinSheet,32,32);
        TextureRegion[] frames = new TextureRegion[4];
        for(int i = 0;i < 4;i++)
        {
            frames[i] = temp[0][i];
        }
        coinspr = new Animation<TextureRegion>(0.08f,frames);
        stateTime = 0f;
        //coinSpr = new Animation<TextureRegion>(0.033f,atlas.findRegion("Coin"),PlayMode.LOOP);
    }
    
    @Override
    public void drawSelf(SpriteBatch batch)
    {
        //batch.draw(coinSheet,boundingBox.x,boundingBox.y);
        System.out.println("Colisao com coin?");
        if(active)
        {
            stateTime += Gdx.graphics.getDeltaTime();
            
            batch.draw(coinspr.getKeyFrame(stateTime, true),getBoundingBox().x,getBoundingBox().y);
        }
    }
    
    @Override
    public void effect(DynamicCollider player)
    {
        player.coins += value;
    }
    
    @Override
    public Coin createNew(Tile tile,int x,int y)
    {
        return TileFactory.createTile(this, x, y);
    }
    @Override    
    public void collide(ICollidable obj,CollisionInfo info)
    {   
        obj.handleCollision(this,info);
    } 
}
