/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;

/**
 *
 * @author Hiago
 */
public class MapBuilder 
{
    CollisionMap map;
    ArrayList<Tile> possibleTiles;
    Vector2 currentTile;
    Texture picker;
    OrthographicCamera camera;
    int width,height;
    
    public MapBuilder(int width,int height,final OrthographicCamera camera)
    {
        map = new CollisionMap(width,height);
        picker = new Texture("picker.png");
        width = width;
        height = height;
        this.camera = camera;
        Gdx.input.setInputProcessor(new Processor());
    }
    
    public MapBuilder(CollisionMap map, SpriteBatch batch, final OrthographicCamera camera)
    {
        picker = new Texture("picker.png");
        this.map = map;
        width = map.getWidth();
        height = map.getHeight();
        this.camera = camera;
        Gdx.input.setInputProcessor(new Processor());
    }
    
    public void update()
    {
        Vector3 inputLixo = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(inputLixo);
        currentTile = new Vector2((int)(inputLixo.x/map.getTileSize()),(int)(inputLixo.y/map.getTileSize()));
        System.out.println("X: " + Gdx.input.getX() + "Y: "+ Gdx.input.getY());
    }
    
    public void renderBuilder(SpriteBatch batch)
    {
        batch.draw(picker,currentTile.x * map.getTileSize() ,currentTile.y * map.getTileSize());
    }
    private class Processor extends InputAdapter
    {        
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            //System.out.println("Estou clicando!");
            if (button == Buttons.LEFT) {
                Vector3 inputLixo = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(inputLixo);
                map.createBlock((int)inputLixo.x/map.getTileSize(),(int)inputLixo.y/map.getTileSize());
            }
            else if(button == Buttons.RIGHT)
            {
                Vector3 inputLixo = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(inputLixo);
                map.clearTile((int)inputLixo.x/map.getTileSize(),(int)inputLixo.y/map.getTileSize());
            }
        return false;
        }
        
        public boolean touchDragged (int screenX, int screenY, int pointer) {
            if(Gdx.input.isButtonPressed(Buttons.LEFT))
            {
                Vector3 inputLixo = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(inputLixo);
                map.createBlock((int)inputLixo.x/map.getTileSize(),(int)inputLixo.y/map.getTileSize());
            }
            else if(Gdx.input.isButtonPressed(Buttons.RIGHT))
            {
                Vector3 inputLixo = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(inputLixo);
                map.clearTile((int)inputLixo.x/map.getTileSize(),(int)inputLixo.y/map.getTileSize());
            }

        return false;
        }
    }
}

