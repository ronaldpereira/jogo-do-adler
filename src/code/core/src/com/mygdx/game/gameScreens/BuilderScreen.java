
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.FollowerCamera;
import com.mygdx.game.mapBuilder.MapBuilder;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelSerializer;

public class BuilderScreen extends GameScreen
{
    //Essa classe representa a tela do construtor de mapas 
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private SpriteBatch uiBatch;
    protected BackToScreenDialog back;
    
    private CollisionMap map;
    private MapBuilder editor;
    
    private SpriteBatch batch;
    private FollowerCamera camera;
    private Game game;
    
    private Level level;
    private LevelSerializer serializer;
  
    public BuilderScreen(Game game, String levelFolder)
    {
       serializer = new LevelSerializer(levelFolder);
       this.level = serializer.loadStage();
       map = level.getInitialNode().getMap();
       
       stage = new Stage();
       this.game = game;
       batch = new SpriteBatch();
       uiBatch = new SpriteBatch();
       
       //Configura a camera:
       camera = new FollowerCamera(map,0,0,120,200,new BoundingBox(0,0,0,0));
       camera.setSpeed(0.1f);
       camera.setToOrtho(false,640,360);
       
       //UI:
       back = new BackToScreenDialog(skin,"Return to main menu?",game,new InitialScreen(game));
       back.setVisible(false);
            
       stage.addActor(back);
       //Cria o construtor de mapas
       editor = new MapBuilder(this.level,batch,camera,stage);
    }
    
    @Override
    public void initialize()
    {
        editor.reinitializeProcessor();
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
        
        //Permite que o usuário jogue o mapa sendo editado.
        if(Gdx.input.isKeyPressed(Input.Keys.F11))
        {
            level.reset();
            serializer.storeStage(level);
            
            //Cria o jogador e define a boundingBox.
            ControllableCharacter player = new ControllableCharacter(new TextureSheet(new Texture("Adler.png"),23,38),true,level);
            player.getBoundingBox().setWidth(23);
            player.getBoundingBox().setHeight(31);
            
            this.dispose();
            game.setScreen(new BuilderStageScreen(player,level,game));

            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            back.setVisible(true);
            back.show(stage);
        }
        //Atualiza o boudingBox que a camera está seguindo dependendo da posição do mouse:
        camera.setBounds(editor.getMouseBox());
        camera.update();
        
        editor.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        editor.renderBuilder(batch);
        batch.end();
        
        uiBatch.begin();
        editor.renderBuilderUI();
        uiBatch.end();
    }
    
    @Override
    public void dispose()
    {
        batch.dispose();
        editor.dispose();
    }
}
