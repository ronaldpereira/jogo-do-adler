
package com.mygdx.game;

import com.mygdx.game.map.TileMap;
import com.mygdx.game.map.CollisionMap;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class FollowerCamera extends OrthographicCamera
{    
    //Camera que segue uma dada boundingBox.
    public static final float SKIN = 0.5f;
    OrthographicCamera camera;
    BoundingBox followerBounds;
    BoundingBox bounds;
    CollisionMap map;
     
    //A porcentagem da distancia entre bounds e o jogador que a camera percorre 
    //por frame(Numero entre 1 e 0):
    float translationSpeed = 0.05f;
    
    boolean following = true;
    
    public FollowerCamera(CollisionMap map,float initialX,float initialY,float boundsHeight,float boundsWidth,BoundingBox follow)
    {
        super();
        this.map = map;
        //Bounds é um retangulo que segue o jogador quando ele se move.
        bounds = new BoundingBox(initialX,initialY,boundsWidth,boundsHeight);
        followerBounds = follow;
    }
    
    @Override
    public void update()
    {
        TileMap tilemp = map.getTileMap();
        if(following)
        {
            if(!(bounds.contains(followerBounds)))
            {//Se bounds não contém o jogador:
                if(followerBounds.getRight() > bounds.getRight())
                {
                    //Se a direita do jogador está a frente da direita de bounds:
                    bounds.translate(translationSpeed * (SKIN + followerBounds.getRight() - bounds.getRight()),0);
                }
                else if(followerBounds.getLeft() < bounds.getLeft())
                {
                    //Se a esquerda do jogador está antes da esquerda de bounds:
                    bounds.translate(translationSpeed * (-SKIN+ followerBounds.getLeft() - bounds.getLeft()),0);
                }
                
                if(followerBounds.getUp() > bounds.getUp())
                {
                    //Se a parte de cima do jogador está acima da parte de cima de bounds:
                    bounds.translate(0,translationSpeed * (SKIN + followerBounds.getUp() - bounds.getUp()));
                }   
                else if(followerBounds.getDown() < bounds.getDown())
                {
                    //Se a parte de baixo do jogador está abaixo da parte de baixo de bounds:
                    bounds.translate(0,translationSpeed * (-SKIN + followerBounds.getDown() - bounds.getDown()));
                }
            }

            //O código a seguir impede que bounds exiba uma parte fora do mapa(o mapa começa em (0,0)):
            if(bounds.x-viewportWidth/2 < 0)
            {
                bounds.x = viewportWidth/2;
            }
            else if(bounds.x + viewportWidth/2 > tilemp.getWidth() * TileMap.getTileSize())
            {
                bounds.x =  tilemp.getWidth() * TileMap.getTileSize() - viewportWidth/2;
            }

            if(bounds.y - viewportHeight/2 < 0)
            {
                bounds.y = viewportHeight/2;
            }
            else if(bounds.y + viewportHeight/2 > tilemp.getHeight() * TileMap.getTileSize())
            {
                bounds.y =  tilemp.getHeight() * TileMap.getTileSize() - viewportHeight/2;
            }

            position.x = bounds.x;
            position.y =  bounds.y;
        }
        
        super.update();
    }
    
    public boolean isFollowing()
    {
        return following;
    }
    
    public void setBounds(BoundingBox box)
    {
        followerBounds = box;
    }
    public void setFollowing(boolean follow)
    {
        following = follow;
    }
    
    public void setSpeed(float speed)
    {
        translationSpeed = speed;
    }
    
    public void setMap(CollisionMap mp)
    {
        map = mp;
    }
}
