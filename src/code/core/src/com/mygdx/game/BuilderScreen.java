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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author Hiago
 */
public class BuilderScreen extends ScreenAdapter
{
    Stage stage;
    //Essa classe representa a tela do construtor de mapas
    Skin skin;
    CollisionMap map;
    MapBuilder editor;
    SpriteBatch batch;
    FollowerCamera camera;
    Game game;
    
    //
    SpriteBatch uiBatch;
    
    public BuilderScreen(CollisionMap map, Game game)
    {
       stage = new Stage();
       this.game = game;
       batch = new SpriteBatch();
       uiBatch = new SpriteBatch();
       this.map = map;
       camera = new FollowerCamera(map,0,0,120,240,new BoundingBox(0,0,0,0));
       camera.translationSpeed = 0.1f;
       camera.setToOrtho(false,800,400);///MUDAR PARA RESOLUÇAO VARIAVEL
       editor = new MapBuilder(map,batch,camera,stage);

        
    }
    
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(Gdx.input.isKeyPressed(Input.Keys.F11))
        {
            ControllableCharacter player = new ControllableCharacter(new Vector2(200,200),new Texture("Chinelo.png"));
            player.setMap(map);
            this.hide();
            game.setScreen(new StageScreen(player,map,game));
            this.dispose();
            return;
            //return;
        }
        
        camera.followerBounds = editor.mouseBox;
        camera.update();
        editor.update();
        

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        

        editor.map.renderMap(batch);
        editor.renderBuilder(batch);
        
        batch.end();
        
        uiBatch.begin();
        editor.renderBuilderUI(uiBatch);
        uiBatch.end();
    }
    
    @Override
    public void dispose()
    {
        batch.dispose();
        editor.dispose();
    }
}
