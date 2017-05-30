/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Hiago
 */
public class BuilderScreen extends ScreenAdapter
{
    CollisionMap map;
    MapBuilder editor;
    SpriteBatch batch;
    OrthographicCamera camera;
    Game game;
    
    public BuilderScreen(CollisionMap map, Game game)
    {
       this.game = game;
       batch = new SpriteBatch();
       this.map = map;
       camera = new OrthographicCamera();
       camera.setToOrtho(false,800,400);///MUDAR PARA RESOLUÇAO VARIAVEL
       editor = new MapBuilder(map,batch,camera);
    }
    
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(Gdx.input.isKeyPressed(Input.Keys.F11))
        {
            ControllableCharacter player = new ControllableCharacter(new Vector2(200,200),new Texture("Chinelo.png"));
            player.setMap(map);
            game.setScreen(new StageScreen(player,map,game));
        }
        camera.update();
        editor.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        editor.map.renderMap(batch);
        editor.renderBuilder(batch);
        
        batch.end();
    }
}
