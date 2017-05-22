/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture; 
/**
 *
 * @author Hiago
 */
public class Tile {
    Texture sprite;
    Rectangle position;
    Boolean blocksMovement;
    
    public Tile(int x,int y)
    {
        //sprite = new Texture();
        position = new Rectangle();
        position.x = x;
        position.y = y;
        blocksMovement = false;
    }
    
    public Tile(int x,int y,Texture spr,Boolean blocks)
    {
        sprite = spr;
        position = new Rectangle();
        position.x = x;
        position.y = y;
        blocksMovement = blocks;
    }
    
    public void setSprite(Texture spr)
    {
        sprite = spr;
    }
    
    public Texture getSprite()
    {
        return sprite;
    }
    
    public void setBlock(Boolean blocks)
    {
        blocksMovement = blocks;
    }
    
    public Boolean getBlocks()
    {
        return blocksMovement;
    }
}
