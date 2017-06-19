
package com.mygdx.game.mapBuilder;

import com.mygdx.game.tile.Coin;
import com.mygdx.game.tile.Tile;
import com.mygdx.game.tile.Repulsor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.FollowerCamera;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.map.TileMap;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static com.mygdx.game.Axis.VERTICAL_AXIS;
import com.mygdx.game.IBuildableObject;
import com.mygdx.game.enemy.WalkerEnemy;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelNode;
import com.mygdx.game.tile.Hazard;
import com.mygdx.game.tile.LevelEndingTile;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;

public class MapBuilder implements Disposable
{
    private Skin builderSkin;
    private Stage uiProcessor;
    private InputMultiplexer multiplexer;
    private CollisionMap currentMap;
    private ArrayList<IBuildableObject> possibleTiles;
    private TileSelector pick;
    private ScrollPane tileScroll;
    private FolderSelector mpPick;
    private ScrollPane mpScroll;
    private IBuildableObject selectedTile;
    private Vector2 currentTile;
    private Texture picker;
    private FollowerCamera camera;
    private int width,height;
    private BoundingBox mouseBox;
    private Vector3 input = new Vector3(0,0,0);
    private LevelNode currentNode;
    private Level level;
  
    public MapBuilder(Level l, SpriteBatch batch, final FollowerCamera camera,Stage stage)
    {
        definePossibleTiles();  
        builderSkin = new Skin(Gdx.files.internal("uiskin.json"));
        uiProcessor = stage;
        level = l;
        currentMap = l.getInitialNode().getMap();
        currentNode = l.getInitialNode();
        //Essa boundingBox definida na posição do mouse será seguida pela camera
        //no modo de scrolling.
        mouseBox = new BoundingBox(0,0,2,2);
        picker = new Texture("picker.png");
        
        width = currentMap.getTileMap().getWidth();
        height = currentMap.getTileMap().getHeight();
        this.camera = camera;
        
        //Cria um scrollpane para guardar o seletor de mapas:
        mpPick = new LevelNodeSelector(level,builderSkin); 
        mpScroll = new ScrollPane(mpPick,builderSkin);
        mpScroll.setFillParent(false);
        mpScroll.setY(0);
        mpScroll.setWidth(mpPick.getPrefWidth());
        mpScroll.setHeight(uiProcessor.getViewport().getScreenHeight());
        mpScroll.setX(uiProcessor.getViewport().getScreenWidth() - mpScroll.getPrefWidth());
        mpScroll.setVisible(false);
        
        mpPick.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) 
            {
                //Atualiza o mapa sendo editado quando ocorrem mudanças no
                //seletor de mapas.
                currentNode = level.getNode(mpPick.selectedStage);
                currentMap = currentNode.getMap();
            }
            
        });
        uiProcessor.addActor(mpScroll);
        
        //Cria um scrollPane para guardar o seletor de tiles:
        pick = new TileSelector(possibleTiles);
        tileScroll = new ScrollPane(pick,builderSkin);
        tileScroll.setFillParent(false);
        tileScroll.setWidth(pick.getPrefWidth());
        tileScroll.setHeight(uiProcessor.getViewport().getScreenHeight());
        tileScroll.setX(0);
        tileScroll.setY(0);
        uiProcessor.addActor(tileScroll);
                
        
        //Cria um multiplexador de input para a UI:
        //Ela é necessaria para a UI captar input tanto no seletor de tiles/mapas
        //quanto no próprio construtor de mapas.
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiProcessor);
        multiplexer.addProcessor(new Processor());
        
        Gdx.input.setInputProcessor(multiplexer);
    }
    
    public void handleInput()
    {
        
        if(Gdx.input.isKeyJustPressed(Keys.I))
        {
            //Esconde ou revela o tilePicker dependendo do seu estado
            if(tileScroll.isVisible())
            {    
                tileScroll.setVisible(false);
            }
            else
            {
                tileScroll.setVisible(true);
            }
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.U))
        {
            //Esconde ou revela o mapPicker dependendo do seu estado
            if(mpScroll.isVisible())
            {    
                mpScroll.setVisible(false);
            }
            else
            {
                mpScroll.setVisible(true);
            }
        }
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
        {
            camera.setFollowing(true);
        }
    }
    
    public void applyTileSet(int x,int y)
    {
        //Aplica um simples algoritmo de autotiling
        //para determinar a imagem de cada bloco.
        int sum;    
        TileMap tilemp = currentMap.getTileMap();
        //Percorre todos os tiles vizinhos à posição dada:
        for(int i = max(x - 1,0);i <= min(x + 1,width -1) ;i++)
        {
            for(int j = max(y - 1,0);j <= min(y + 1,height - 1) ;j++)
            {
                if(tilemp.getTile(i,j).autotiles == true)
                {
                    sum = 0;
                    
                    //tile a direita bloqueia
                    if(i == tilemp.getWidth() - 1 || tilemp.getTile(i + 1,j).autotiles)
                    {
                        sum += 8;
                    }
                    
                    //tile abaixo bloqueia
                    if(j == 0 ||tilemp.getTile(i,j - 1).autotiles)
                    {
                        sum += 4;
                    }
                    
                    //tile a esquerda bloqueia
                    if(i == 0 || tilemp.getTile(i - 1,j).autotiles)
                    {
                        sum += 2;
                    }
                    
                    //tile acima bloqueia
                    if(j == tilemp.getHeight() - 1 || tilemp.getTile(i,j + 1).autotiles)
                    {
                        sum +=1;
                    }
                    
                    //Muda a imagem.
                    tilemp.getTile(i,j).setDefaultFrame(sum);
                }
            }
        }
    }
    
    //Essa função defines os objetos que poderão ser criados através do mapBuilder.
    //Seria uma boa ideia ler esses objetos de um arquivo eventualemente.
    private void definePossibleTiles()
    {
        possibleTiles = new ArrayList<IBuildableObject>();
        
        //Tile de grama:
        possibleTiles.add(new Tile(0,0,new TextureSheet(new Texture("azuis.png"),32,32),false,true,true));
        
        //Tile de gelo:
        possibleTiles.add(new Tile(0,0,new TextureSheet(new Texture("azuis (1).png"),32,32),false,true,0.0075f,10f,true));
        
        //Espinhos:
        Hazard spike = new Hazard(0,0,new TextureSheet(new Texture("Spikes.png"),32,32),false);
        spike.setDangerArea(new BoundingBox(1,20,30,12));
        possibleTiles.add(spike);
        spike = new Hazard(0,0,new TextureSheet(new Texture("RightSpikes.png"),32,32),false);
        spike.setDangerArea(new BoundingBox(0,1,10,30));
        possibleTiles.add(spike);
        spike = new Hazard(0,0,new TextureSheet(new Texture("LeftSpikes.png"),32,32),false);
        spike.setDangerArea(new BoundingBox(22,1,10,30));
        possibleTiles.add(spike);
        spike = new Hazard(0,0,new TextureSheet(new Texture("DownSpikes.png"),32,32),false);
        spike.setDangerArea(new BoundingBox(1,0,30,12));
        possibleTiles.add(spike);
        
        //Arvore decorativa:
        Tile arvore = new Tile(0,0,new TextureSheet(new Texture("Arvore.png"),64,96),false,false,false);
        possibleTiles.add(arvore);
        
        //Cogumelos:
        possibleTiles.add(new Repulsor(0,0,new TextureSheet(new Texture("rightRep.png"),32,32),true,HORIZONTAL_AXIS,new Vector2(-1,0),25));
        possibleTiles.add(new Repulsor(0,0,new TextureSheet(new Texture("upRep.png"),32,32),true,VERTICAL_AXIS,new Vector2(0,1),7.5f));
        possibleTiles.add(new Repulsor(0,0,new TextureSheet(new Texture("DownRep.png"),32,32),true,VERTICAL_AXIS,new Vector2(0,-1),7.5f));
        possibleTiles.add(new Repulsor(0,0,new TextureSheet(new Texture("LeftRep.png"),32,32),true,HORIZONTAL_AXIS,new Vector2(1,0),25));
        possibleTiles.add(new Coin(0,0,new TextureSheet(new Texture("Chineloa.png"),32,32),true));
        possibleTiles.add(new LevelEndingTile(0,0,new TextureSheet(new Texture("Victory.png"),32,32),true,this.level));

        //Inimigos:
        //Cenorito:
        possibleTiles.add(new WalkerEnemy(new Vector2(0,0),new TextureSheet(new Texture("CenouraAnim.png"),34,35),true));

    }
    public void update()
    {   
        camera.setFollowing(false);
        handleInput();
        
        //Atualiza a posição do mouse:
        input = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(input);
        currentTile = new Vector2((int)(input.x/TileMap.getTileSize()),(int)(input.y/TileMap.getTileSize()));
        mouseBox.setPosition(input.x,input.y);
    }
    
    public void reinitializeProcessor()
    {
        Gdx.input.setInputProcessor(multiplexer);
    }
    public void renderBuilder(SpriteBatch batch)
    {
        //Desenha o mapa que está sendo editado atualmente e o seletor de posições
        currentMap.renderMap(batch);
        batch.draw(picker,currentTile.x * TileMap.getTileSize() ,currentTile.y * TileMap.getTileSize());
    }
    
    public void renderBuilderUI()
    {
        uiProcessor.act();
        uiProcessor.draw();
    }
    
    public BoundingBox getMouseBox()
    {
        return mouseBox;
    }
    
    @Override
    public void dispose()
    {
        uiProcessor.dispose();
    }
    
    //Essa classe trata a input do mouse durante a construção do mapa:
    private class Processor extends InputAdapter
    {        
        //Recebe entrada do mouse
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) 
        {
            selectedTile = possibleTiles.get(pick.getTileIndex());
            camera.unproject(input);
            //Quando o botão esquerdo é clicado, o tile selecionado é adicionado ao mapa
            if (button == Buttons.LEFT) 
            {
                if(selectedTile != null)
                {
                    selectedTile.build(currentMap,(int)currentTile.x,(int)currentTile.y);
                    //A imagem é recalculdade se o tile usa autotiling.
                    applyTileSet((int)currentTile.x,(int)currentTile.y);
                }
            }
            else if(button == Buttons.RIGHT)
            {   
                //Remove o tile na posição do mouse.
                currentMap.clearPosition((int)currentTile.x,(int)currentTile.y);
                applyTileSet((int)currentTile.x,(int)currentTile.y);

            }
            
            return false;
        }
        
        //Ocorre quando o mouse é arrastado:
        @Override
        public boolean touchDragged (int screenX, int screenY, int pointer) 
        {
            selectedTile = possibleTiles.get(pick.getTileIndex());
            camera.unproject(input);
            //Quando o botão esquerdo é clicado, o tile selecionado é adicionado ao mapa
            if(Gdx.input.isButtonPressed(Buttons.LEFT))
            {
                if(selectedTile != null)
                {
                    //A imagem é recalculdade se o tile usa autotiling.
                    selectedTile.build(currentMap,(int)currentTile.x,(int)currentTile.y);
                    applyTileSet((int)currentTile.x,(int)currentTile.y);
                }
            }
            else if(Gdx.input.isButtonPressed(Buttons.RIGHT))
            {
                //Remove o tile na posição do mouse.
                currentMap.clearPosition((int)currentTile.x,(int)currentTile.y);
                applyTileSet((int)currentTile.x,(int)currentTile.y);
            }

            return false;
        }
    }
}

