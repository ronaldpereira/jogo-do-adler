
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class VictoryScreen extends GameScreen
{
    //Tela de vitória
    private Game game;
    private Label vicLabel;
    private Container vicContainer;
    private Stage stage;
    private Skin uiSkin = new Skin(Gdx.files.internal("gameskin.json"));
    private Music victoryMusic =  Gdx.audio.newMusic(Gdx.files.internal("victory2.ogg"));
    
    public VictoryScreen(Game game)
    {
        this.game = game;
        
        vicLabel = new Label("YOU WIN",uiSkin);
        vicContainer = new Container(vicLabel).center();
        vicContainer.setFillParent(true);
        
        stage = new Stage();
        stage.addActor(vicContainer);
        Gdx.input.setInputProcessor(stage);
        victoryMusic.play();
    }
    @Override
    public void initialize() 
    {
        Gdx.input.setInputProcessor(stage);
    }
     @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
    }
}
