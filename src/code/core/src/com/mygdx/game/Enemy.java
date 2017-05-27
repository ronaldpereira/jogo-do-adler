/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public class Enemy extends DynamicCollider{
    
    //CHANGE:
    float xOrigin;
    int direction = 1;
    
    public void setOrigin()
    {
        if(direction < 0)
        {
            //REMOVER ISSO E TROCAR POR SKIN
            xOrigin = boundingBox.getRight() - 1;
        }
        else
        {
            xOrigin = boundingBox.getLeft() + 1;
        }
    }
    public Enemy(Vector2 initialPos, Texture sprite) {
        super(initialPos, sprite);
        attrition = 0;
    }
    
    @Override
    public void updateMovement() {
        setOrigin();
        //System.out.println("Estou com essa velocidade: " + currentSpeed.x +" essa direcao " + direction + " e essa origem:" + xOrigin);
        //System.out.println("To nesse tile " +((int)map.findTile(xOrigin,boundingBox.y).x + direction) + " y =" + (int)map.findTile(xOrigin,boundingBox.y).y);
        if(!map.getTile((int)map.findTile(xOrigin,boundingBox.y).x + direction,(int)map.findTile(xOrigin,boundingBox.y).y - 1).getBlocks()
           || map.getTile((int)map.findTile(xOrigin,boundingBox.y).x + direction,(int)map.findTile(xOrigin,boundingBox.y).y).getBlocks())
        {
            direction *= -1;
        }
        currentSpeed.x = 2 * direction;
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
    
    @Override
    public void handleCollision(DynamicCollider player,CollisionInfo info)
    {
        System.out.println("Estou colidindo com o player!");
    }
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        //System.out.println("Colide com tile!!!!!!");
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            if(tile.getBlocks())
            {
                int distance = (int)boundingBox.getHorizontalDistance(tile.boundingBox);
                currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);
                
                //System.out.println("Speed: "+ currentSpeed.x + "TilePosition:" + tile.boundingBox.x +"," + tile.boundingBox.y);
            }
        }
        else
        {
            if(tile.getBlocks())
            {
                int direction = (int)signum(currentSpeed.y);
                int distance = (int)boundingBox.getVerticalDistance(tile.boundingBox);
                currentSpeed.y = min(distance,abs(currentSpeed.y)) * direction;

                if(direction < 0 && distance == 0)
                {
                    grounded = true;
                }

                if(grounded == true)
                {
                    attrition = tile.getAttrition();
                    maximumSpeed.x = tile.getMaxSpeed();
                }
            }
            
        }
    }
    
}
