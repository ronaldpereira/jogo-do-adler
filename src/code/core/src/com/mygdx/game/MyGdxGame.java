package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
        OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
        Texture droplet;
        private Rectangle bucket;
        Character chara;
        CollisionMap mp;
	
	@Override
	public void create () {
                camera = new OrthographicCamera();
                camera.setToOrtho(false,800,400);
                
                batch = new SpriteBatch();
                droplet = new Texture("drop.png");
                
                chara = new Character(new Vector2(100,100),droplet);
                mp = new CollisionMap(800,400,batch);
                mp.createBlock(1,1);
                mp.createBlock(5,7);
                mp.createBlock(40,40);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
                camera.update();
                chara.update();
                
                batch.setProjectionMatrix(camera.combined);
		batch.begin();
                mp.renderMap();
		batch.draw(droplet, (int)chara.boundingBox.x, (int)chara.boundingBox.y);
		batch.end();
                
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
