
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.FollowerCamera;
import com.mygdx.game.player.HUD;
import com.mygdx.game.level.Level;


public class StageScreen extends GameScreen
{
    //Tela onde ocorre a gameplay.
    //Essa tela existe enquanto o jogador estiver jogando algum nível.
    //Novas instancias só serão construidas quando o jogador volta para o menu principal.
    
    protected static Game game;
    protected static Skin uiSkin = new Skin(Gdx.files.internal("uiSkin.json"));
    private static Music levelMusic;
    private static SpriteBatch batch;
    private static SpriteBatch uiBatch;
    private static FollowerCamera camera;
    private static ControllableCharacter player;
    private static CollisionMap map;
    protected static Level level;
    private static HUD hud;
    protected static BackToScreenDialog back;
    
    public StageScreen(ControllableCharacter p,Level l,Game game)
    {
        this.game = game;
        
        player = p;
        level = l;
        //Carrega a musica do nivel:
        levelMusic = Gdx.audio.newMusic(Gdx.files.internal(level.getMusic()));
        levelMusic.setLooping(true);
        levelMusic.play();
        player.setLevelNode(level.getCurrentNode());
       
        map = level.getCurrentNode().getMap();
        map.addPlayer(player);
        
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        
        //Cria a camera que segue o jogador.
        camera = new FollowerCamera(map,0,0,50,75, player.getBoundingBox());
        camera.setToOrtho(false,640,360);
        
        //Cria a UI:
        hud = new HUD(player, new Skin(Gdx.files.internal("gameSkin.json")));
        defineBackButton();
        
        hud.addActor(back);
        initialize();
    }
    @Override 
    public void initialize()
    {
        Gdx.input.setInputProcessor(hud);
    }
    protected void defineBackButton()
    {
        back = new BackToScreenDialog(uiSkin,"Return to main menu?",game,new InitialScreen(game));
        back.setVisible(false);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            //Mostra o dialogo para voltar ao menu principal.
            back.setVisible(true);
            back.show(hud);         
        }
        
        player.update();
        map.update();
        hud.update();
        camera.update();
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        map.renderMap(batch);
        player.drawSelf(batch);
        batch.end();
        
        hud.act();
        hud.draw();
    }
    
    public static void nextLevel()
    {
        //Atualiza a tela para o proximo nível.
        if(level.levelFinished())
        {
            staticDispose();
            game.setScreen(new VictoryScreen(game));
        }
        level.nextLevel();
        player.setLevelNode(level.getCurrentNode());
        map = level.getCurrentNode().getMap();
        map.addPlayer(player);
        camera.setMap(map);
    }
    
    @Override
    public void dispose()
    {
        levelMusic.stop();
        levelMusic.dispose();
        batch.dispose();
    }
    
    public static void staticDispose()
    {
        levelMusic.stop();
        levelMusic.dispose();
        batch.dispose();
    }
    
    @Override
    public void resize(int width, int height)
    {
        hud.getViewport().update(width, height, true);
    }
}
