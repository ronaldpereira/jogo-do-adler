/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.mygdx.game.Tiles.Coin;
import com.mygdx.game.Tiles.Tile;
import com.mygdx.game.Tiles.Repulsor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static com.mygdx.game.Axis.VERTICAL_AXIS;
import com.mygdx.game.Tiles.AbstractTile;
import java.util.ArrayList;
import javafx.scene.Scene;

/**
 *
 * @author Hiago
 */
public class MapBuilder implements Disposable
{
    //UI
    Skin builderSkin;
    Table tileSelector;
    Stage uiProcessor;
    ScrollPane scroll;
    ArrayList<ImageButton> tileButtons;
    SpriteBatch uiBatch;
    //
    //
    InputMultiplexer multiplexer;
    CollisionMap map;
    ArrayList<AbstractTile> possibleTiles;
    AbstractTile selectedTile;
    Vector2 currentTile;
    Texture picker;
    FollowerCamera camera;
    int width,height;
    BoundingBox mouseBox;
    Vector3 input = new Vector3(0,0,0);
    
    public MapBuilder(int width,int height,final OrthographicCamera camera)
    {
        mouseBox = new BoundingBox(0,0,2,2);
        map = new CollisionMap(width,height);
        picker = new Texture("picker.png");
        this.width = width;
        this.height = height;
        //this.camera = camera;
        Gdx.input.setInputProcessor(new Processor());
    }
    
    public MapBuilder(CollisionMap map, SpriteBatch batch, final FollowerCamera camera,Stage stage)
    {
        possibleTiles = new ArrayList<AbstractTile>();
        ////TODO: POSSIBILITAR A CRIAÇÃO DE NOVOS TILES ATRAVES DE UM TILE GERADOR
        possibleTiles.add(new Tile(0,0,new Texture("BlackBlock.png"),true));
        possibleTiles.add(new Tile(0,0,new Texture("Ice.png"),true,0.01f,12f));
        possibleTiles.add(new Repulsor(0,0,new Texture("leftRep.png"),HORIZONTAL_AXIS,new Vector2(-1,0),25));
        possibleTiles.add(new Repulsor(0,0,new Texture("upRep.png"),VERTICAL_AXIS,new Vector2(0,1),7.5f));

        possibleTiles.add(new Repulsor(0,0,new Texture("rightRep.png"),HORIZONTAL_AXIS,new Vector2(1,0),25));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        possibleTiles.add(new Coin(0,0,new Texture("Chinelo.png")));
        //
        builderSkin = new Skin(Gdx.files.internal("uiskin.json"));
        tileSelector = new Table();
        tileButtons = new ArrayList<ImageButton>();
        
        uiProcessor = stage;
        
        for(AbstractTile tile : possibleTiles)
        {
            tileButtons.add(new ImageButton(new TextureRegionDrawable(new TextureRegion(tile.getSprite()))));
        }
        for(int i = 0;i < tileButtons.size();i++)
        {
            final int index = i;
            
            tileButtons.get(i).addListener(new ClickListener()
                {
                    @Override
                    public void clicked(InputEvent event,float x,float y)
                    {
                        selectedTile = possibleTiles.get(index);
                        System.out.println("Eu funciono!");
                    }
                }
            );
            tileSelector.add(tileButtons.get(i)).uniform();
            if((i + 1) % 4 == 0)
            {
                tileSelector.row();
            }
        }
        //tileSelector.setDebug(true); 
        scroll = new ScrollPane(tileSelector,builderSkin);
        scroll.setFillParent(false);
        scroll.setWidth(tileSelector.getPrefWidth());
        scroll.setHeight(uiProcessor.getViewport().getScreenHeight());
        scroll.setX(0);
        scroll.setY(0);
        //tileSelector.setPosition(100f,100f);
        uiProcessor.addActor(scroll);
       
        //Fim do codigo de UI
        mouseBox = new BoundingBox(0,0,2,2);
        picker = new Texture("picker.png");
        this.map = map;
        width = map.getWidth();
        height = map.getHeight();
        this.camera = camera;   
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiProcessor);
        multiplexer.addProcessor(new Processor());
        
        Gdx.input.setInputProcessor(multiplexer);
    }
    
    public void handleInput()
    {
        if(Gdx.input.isKeyJustPressed(Keys.I))
        {
            Gdx.input.setInputProcessor(multiplexer);
            if(scroll.isVisible())
            {    
                scroll.setVisible(false);
            }
            else
            {
                scroll.setVisible(true);
            }
        }
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
        {
            camera.setFollowing(true);
        }
    }
    
    public void applyTileset()
    {
        //Autotiling
        //Codigo incompleto
        int sum = 0;
        for(int i = 1;i < map.getWidth() -1 ;i++)
        {
            for(int j = 1;j < map.getHeight() -1 ;j++)
            {
                sum = 0;
                if(map.map[i + 1][j].getBlocks())
                {
                    sum += 8;
                }
                if(map.map[i][j - 1].getBlocks())
                {
                    sum += 4;
                }
                if(map.map[i - 1][j].getBlocks())
                {
                    sum += 2;
                }
                if(map.map[i][j + 1].getBlocks())
                {
                    sum +=1;
                }
                
            }
        }
    }
    public void update()
    {
        camera.setFollowing(false);
        handleInput();
        input = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(input);
        currentTile = new Vector2((int)(input.x/map.getTileSize()),(int)(input.y/map.getTileSize()));
        mouseBox.setPosition(input.x,input.y);
    }
    
    public void renderBuilder(SpriteBatch batch)
    {
        batch.draw(picker,currentTile.x * map.getTileSize() ,currentTile.y * map.getTileSize());
    }
    
    public void renderBuilderUI(SpriteBatch batch)
    {
        uiProcessor.act();
        uiProcessor.draw();
    }
    
    private class Processor extends InputAdapter
    {        
        //Recebe entrada do mouse
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) 
        {
            camera.unproject(input);
            if (button == Buttons.LEFT) 
            {
                if(selectedTile != null)
                {
                    map.createTile((int)currentTile.x,(int)currentTile.y,selectedTile);
                }
            }
            else if(button == Buttons.RIGHT)
            {

                if(map.map[(int)currentTile.x][(int)currentTile.y].getCollides())
                {
                    map.clearTile((int)currentTile.x,(int)currentTile.y);
                }
            }
            
            return false;
        }
        
        @Override
        public boolean touchDragged (int screenX, int screenY, int pointer) 
        {
            camera.unproject(input);
            if(Gdx.input.isButtonPressed(Buttons.LEFT))
            {
                if(selectedTile != null)
                {
                    map.createTile((int)currentTile.x,(int)currentTile.y,selectedTile);
                }
            }
            else if(Gdx.input.isButtonPressed(Buttons.RIGHT))
            {
                map.clearTile((int)currentTile.x,(int)currentTile.y);
            }

            return false;
        }
    }
    
    @Override
    public void dispose()
    {
        uiProcessor.dispose();
        Gdx.input.setInputProcessor(null);
    }
    
}

