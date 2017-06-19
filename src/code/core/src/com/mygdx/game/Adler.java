package com.mygdx.game;

import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.mapBuilder.MapBuilder;
import com.mygdx.game.gameScreens.BuilderScreen;
import com.mygdx.game.enemy.AbstractEnemy;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.gameScreens.InitialScreen;

public class Adler extends Game 
{     
	@Override
	public void create () 
        {
            this.setScreen(new InitialScreen(this));
	}

	@Override
	public void render () 
        {
            super.render();
	}
}
