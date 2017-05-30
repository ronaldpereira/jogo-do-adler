/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Hiago
 */
public class StageScreen extends ScreenAdapter
{
    FollowerCamera camera;
    SpriteBatch batch;
    ControllableCharacter player;
    CollisionMap map;
    
    public StageScreen(ControllableCharacter player,CollisionMap map,Game game)
    {
        this.player = player;
        this.map = map;
        batch = new SpriteBatch();
        camera = new FollowerCamera(0,0,150,150,player.boundingBox);
        camera.setToOrtho(false,800,400);
    }
    
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        player.update();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.renderMap(batch);
        batch.draw(new Texture("Chinelo.png"), (int)player.boundingBox.x, (int)player.boundingBox.y);
        //batch.draw(enemy.sprite, (int)enemy.boundingBox.x, (int)enemy.boundingBox.y);
        //font.draw(batch,chara.getPos().toString(),100,350);
        //font.draw(batch,"Coins:" + chara.coins,400,350);
        batch.end();
    }
}
