/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author Hiago
 */
public class TileFactory 
{
    public static Tile createTile(Tile tile, int x,int y)
    {   
        System.out.println(tile.getClass());
        Tile clone = new Tile(x,y,tile.getSprite(),tile.getBlocks(),
                                tile.getAttrition(),tile.getMaxSpeed());
        return clone;
    }
    
    public static Coin createTile(Coin coin, int x,int y)
    {
        Coin clone = new Coin(x,y,coin.getSprite());
        return clone;
    }
    
    public static Repulsor createTile(Repulsor repulsor, int x,int y)
    {
        Repulsor clone = new Repulsor(x,y,repulsor.getSprite(),repulsor.getAxis(),
                repulsor.getDirection(),repulsor.getRepulsionSpeed());
        return clone;
    }

}
