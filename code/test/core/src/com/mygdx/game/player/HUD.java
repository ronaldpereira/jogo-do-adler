
package com.mygdx.game.player;

import com.mygdx.game.player.ControllableCharacter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class HUD extends Stage
{
    //Classe que define a HUD do personagem(vidas,score,etc).
    Texture lifeTexture = new Texture("heart.png");
    Texture emptyLife = new Texture("emptyHeart.png");
    
    TextureRegionDrawable lifeDrawable;
    TextureRegionDrawable emptyDrawable;
    Image[] lifeImages;
    Label scoreLabel;
    
    private ControllableCharacter player;
    private int life;
    private int score;
    private Vector2 lifePosition = new Vector2(80, 400);
    private Vector2 scorePosition;
    private int lifeSpacing = 5;
    
    
    public HUD(ControllableCharacter player,Skin uiSkin)
    {
        super();
        setViewport(new StretchViewport(800,480));
        this.player = player;
        
        //Define as imagens do coração vazio e cheio:
        lifeDrawable = new TextureRegionDrawable(new TextureRegion(lifeTexture));
        emptyDrawable = new TextureRegionDrawable(new TextureRegion(emptyLife));
        lifeImages = new Image[player.getLife()];
        
        for(int i = 0; i < lifeImages.length; i++)
        {
            //Cria cada coração e determina a posição dele na tela.
            lifeImages[i] = new Image(lifeDrawable);
            lifeImages[i].setScale(2f,2f);
            
            lifeImages[i].setPosition(lifePosition.x + (lifeSpacing *i) + (lifeImages[i].getWidth() * lifeImages[i].getScaleX() * i) ,lifePosition.y);
            addActor(lifeImages[i]);
        }
       
        scoreLabel = new Label("",uiSkin);
        scoreLabel.setPosition(500, 420);
        addActor(scoreLabel);
    }
    
    public void update()
    {
        //Atualiza os valores de vida e score.
        life = player.getLife();
        score = player.getScore();
        scoreLabel.setText("Score :" + this.score);
        
        for(int i = 0; i < lifeImages.length;i++)
        {
            //Desenha corações cheios até que i seja igual a vida atual do jogador.
            if(i < life)
            {
                lifeImages[i].setDrawable(lifeDrawable);                
            }
            //Desenha corações vazios.
            else
            {
                lifeImages[i].setDrawable(emptyDrawable);
            }
        }   
    }
}
