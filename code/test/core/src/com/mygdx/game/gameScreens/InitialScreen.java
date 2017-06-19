
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelSerializer;
import com.mygdx.game.mapBuilder.LevelSelector;

public class InitialScreen extends GameScreen
{        
    //Tela inicial do jogo.
    
    //Nome do mapa principal do jogo:
    private static final String INITIAL = "Maps/testLevel";
    
    private ImageButton newGame;
    private ImageButton mapBuilder;
    private ImageButton exit;
    private Table initialScreenTable;
    private Stage initialStage;
    private Skin skin;
    private OrthographicCamera camera;
    
    public InitialScreen(final Game game)
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false,640,360);
        
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        final Screen thisScreen = this;
                
        newGame = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("NewGameButtonUp.png"))),new TextureRegionDrawable(new TextureRegion(new Texture("NewGameDown.png"))));
        newGame.addListener(new ClickListener()
            {
                //Botão de novo jogo.
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Abre o primeiro mapa.
                    Level firstLevel = new LevelSerializer(INITIAL).loadStage();
                    thisScreen.dispose();
                    
                    //Cria o jogador
                    ControllableCharacter player = new ControllableCharacter(new TextureSheet(new Texture("Adler.png"),23,38),true,firstLevel);
                    
                    //Width e height devem ser configurados dependendo da imagem do jogador.
                    player.getBoundingBox().width = 22;
                    player.getBoundingBox().height = 30;
                    
                    //Muda a tela para o primeiro nível.
                    game.setScreen(new StageScreen(player,firstLevel,game));
                }
            }
        );
        
       mapBuilder = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("MpBuilderUp.png"))),new TextureRegionDrawable(new TextureRegion(new Texture("MpBuilderDown.png"))));
       final LevelSelector lv = new LevelSelector(skin,game);
       lv.setFillParent(true);
       lv.setVisible(false);

       
       mapBuilder.addListener(new ClickListener()
            {

                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    lv.setVisible(true);
                }
            }
        );
        
        exit = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("ExitUp.png"))),new TextureRegionDrawable(new TextureRegion(new Texture("QuitDown.png"))));
        exit.addListener(new ClickListener()
            {
                //Botão para sair do jogo.
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    thisScreen.dispose();
                    game.dispose();
                    Gdx.app.exit();
                }
            }
        );
        
        initialStage = new Stage();
        
        //UI da tela inicial:
        initialScreenTable = new Table();
        initialScreenTable.setPosition(initialStage.getViewport().getScreenWidth()/2, initialStage.getViewport().getScreenHeight()/2);
        //Adiciona o botão de novo jogo:
        initialScreenTable.add(newGame).padBottom(20);
        initialScreenTable.row();
        //Adiciona o botão do map builder:
        initialScreenTable.add(mapBuilder).padBottom(20);
        initialScreenTable.row();
        //Adiciona o botão de saida:
        initialScreenTable.add(exit).padBottom(20);
        initialScreenTable.row();
 
        initialStage.addActor(initialScreenTable);
        initialStage.addActor(lv);
        initialize();
    }
    
    @Override
    public void initialize()
    {
        Gdx.input.setInputProcessor(initialStage);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.4f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        initialStage.act();
        initialStage.draw();
    }
    
    @Override
    public void dispose()
    {
        initialStage.dispose();
    }
    
    @Override
    public void resize(int width, int height)
    {
        initialStage.getViewport().update(width, height, true);
    }
}
