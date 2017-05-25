/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class Enemy extends DynamicCollider{

    public Enemy(Vector2 initialPos, Texture sprite) {
        super(initialPos, sprite);
    }

    @Override
    public void updateMovement() {
      
        
    }
    
}
