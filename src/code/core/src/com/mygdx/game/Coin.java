/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Hiago
 */
public class Coin extends Pickup
{
    private int value = 5;
    public Coin(int x,int y,Texture spr)
    {
        super(x,y,spr);
    }
    
    @Override
    public void effect(DynamicCollider player)
    {
        player.coins += value;
    }
}
