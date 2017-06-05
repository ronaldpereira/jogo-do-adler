/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.mygdx.game.Tiles.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static com.mygdx.game.Axis.VERTICAL_AXIS;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public class Enemy extends DynamicCollider{
    ////TODO:MUDA TUDO
    //CHANGE:
    float xOrigin;
    int direction = 1;
    
    public void setOrigin()
    {
        if(direction < 0)
        {
            //REMOVER ISSO E TROCAR POR SKIN
            xOrigin = getBoundingBox().getRight() - 1;
        }
        else
        {
            xOrigin = getBoundingBox().getLeft() + 1;
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
        if(!map.getTile((int)map.findTile(xOrigin,getBoundingBox().y).x + direction,(int)map.findTile(xOrigin,getBoundingBox().y).y - 1).getBlocks()
           || map.getTile((int)map.findTile(xOrigin,getBoundingBox().y).x + direction,(int)map.findTile(xOrigin,getBoundingBox().y).y).getBlocks())
        {
            direction *= -1;
        }
        currentSpeed.x = 2f * direction;
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
    
    @Override
    public void handleCollision(DynamicCollider player,CollisionInfo info)
    {   
        /*
        System.out.println("Estou colidindo com o player!");
        int distance = (int)boundingBox.getHorizontalDistance(player.boundingBox);
        System.out.println("distance:" + distance + "currentSpeedx: "+ currentSpeed.x);
        currentSpeed.x = min((distance),abs(currentSpeed.x)) * signum(currentSpeed.x);
        */
       
    }
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        
        //System.out.println("Colide com tile!!!!!!");
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            System.out.println("Colide com tile!!!!!!");
            if(tile.getBlocks())
            {
                int distance = (int)getBoundingBox().getHorizontalDistance(tile.getBoundingBox());
                currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);
                
                System.out.println("Enemy Speed: "+ currentSpeed.x + "TilePosition:" + tile.getBoundingBox().x +"," + tile.getBoundingBox().y);
            }
        }
        else 
        {
            //currentSpeed.y = 0;
            System.out.println("Bug?");
            if(tile.getBlocks())
            {
                System.out.println("Bug!");
                int direction = (int)signum(currentSpeed.y);
                int distance = (int)getBoundingBox().getVerticalDistance(tile.getBoundingBox());
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
