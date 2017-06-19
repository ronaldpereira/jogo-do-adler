
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.gameScreens.BuilderScreen;
import com.mygdx.game.level.LevelSerializer;

public class LevelSelector extends Window
{
    //Janela para selecionar qual nível será editado no editor de mapas.
    //Tem também a opção de criar um novo nível.
    private ScrollPane levelPane;
    private ConfirmableTextField field;
    private FolderSelector levels;
    private TextButton newButton;
    private TextButton cancelButton;
    private TextButton okButton;
    private String selectedLevel;
    
    public LevelSelector(Skin uiSkin,final Game game)
    {
        super("Level Selector",uiSkin);
        center();
        levels = new FolderSelector("Maps/",uiSkin);
        newButton = new TextButton("New",uiSkin);
        okButton = new TextButton("Ok",uiSkin);
        cancelButton = new TextButton("Cancel",uiSkin);
        field = new NewLevelField(uiSkin);
        
        field.addListener(new ChangeListener() 
        {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) 
            {
                //Quando ocorre uma mudança no diretorio, os leveis possiveis
                //São atualizados
                levels.updateList();
            }
        }); 
        
        field.setVisible(false);
        newButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Torna o campo field visivel,
                    //o que permite a criação de novos níveis
                    field.setVisible(true);
                }
            }
        );
        
        cancelButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Deixa esse objeto invisível.
                    setVisible(false);
                }
            }
        );
        
        okButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Aceita o nível selecionado e abre o mapBuilder com esse nível.
                    selectedLevel = levels.currentMap;
                    if(selectedLevel != null)
                    {
                        game.getScreen().dispose();
                        game.setScreen(new BuilderScreen(game, selectedLevel));
                    }
                }
            }
        );

       

        levelPane = new ScrollPane(levels);
        this.add(levelPane).pad(30);
        this.row();
        this.add(okButton);
        this.add(newButton);
        this.add(cancelButton);
        this.addActor(field);
    }
}
