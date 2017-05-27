package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
    
        BitmapFont font;
        FollowerCamera camera;
	SpriteBatch batch;
	Texture img;
        Texture droplet;
        private Rectangle bucket;
        DynamicCollider chara;
        CollisionMap mp;
        ShapeRenderer shape;
        Enemy enemy;
	
	@Override
	public void create () {
                shape = new ShapeRenderer();
                font = new BitmapFont();
                //camera.setToOrtho(false,800,400);
                
                batch = new SpriteBatch();
                droplet = new Texture("Purp.png");
                
                chara = new ControllableCharacter(new Vector2(300,100),droplet);
                camera = new FollowerCamera(0f,0f,125f,150f,chara.boundingBox);
                camera.setToOrtho(false,800,400);
                mp = new CollisionMap(800,400,batch);
                mp.createBlock(1,1);
                mp.createBlock(2,1);
                mp.createBlock(5,7);
                mp.createBlock(5,6);
                mp.createBlock(15,20);
                mp.createBlock(20,15);
                mp.createBlock(7,7);
                mp.createBlock(10,5);
                mp.createBlock(5,3);
                mp.createBlock(6,3);
                mp.createBlock(7,1);
                mp.createBlock(8,1);
                mp.createBlock(9,1);
                mp.createBlock(9,2);
                mp.createBlock(6,1);
                mp.createBlock(5,2);
                mp.createIce(10,1);
                mp.createIce(11,1);
                mp.createIce(12,1);
                mp.createIce(13,1);
                mp.createIce(14,1);
                mp.createIce(15,1);
                mp.createIce(16,1);
                mp.createIce(17,1);
                mp.createIce(18,1);
                mp.createIce(19,1);
                mp.createIce(20,1);
                mp.createIce(23,2);
                mp.createIce(24,2);
                mp.createIce(25,2);
                mp.createIce(26,2);
               // mp.createBlock(18,2);
               // mp.createCoin(17,2);
               // mp.createCoin(16,2);
                mp.createCoin(10,2);
                mp.createCoin(11,2);
                mp.createCoin(15,2);
                mp.createCoin(14,2);
                mp.createCoin(13,2);
                mp.createCoin(12,2);
                mp.createCoin(12,3);
                mp.createCoin(12,4);
                mp.createCoin(12,5);
                mp.createCoin(12,6);
                enemy = new Enemy(new Vector2(320,64),new Texture("Chinelo.png"));
                enemy.setMap(mp);
                chara.setMap(mp);
                mp.addMovingObject(chara);
                mp.addMovingObject(enemy);
	}

	@Override
	public void render () {
            
            Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
            
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            chara.update();
            enemy.update();
            camera.update();
            
            
            shape.setProjectionMatrix(camera.combined);
            
            shape.begin(ShapeType.Line);
            //shape.rect(camera.bounds.x,camera.bounds.y,camera.bounds.width,camera.bounds.height);
            shape.end();
            
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            mp.renderMap();
            batch.draw(droplet, (int)chara.boundingBox.x, (int)chara.boundingBox.y);
            batch.draw(droplet, (int)enemy.boundingBox.x, (int)enemy.boundingBox.y);
            font.draw(batch,chara.getPos().toString(),100,350);
            font.draw(batch,"Coins:" + chara.coins,400,350);
            batch.end();
                
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
