
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelNode;

public class LevelNodeSelector extends FolderSelector
{
    //Seletor de mapas no mapBuilder
    TextButton newButton;
    TextButton removeMap;
    NewMapField newMap;

    public LevelNodeSelector(final Level level, Skin uiSkin) 
    {
        super(level.getFolder(), uiSkin);
        this.row();
        newButton = new TextButton("+",uiSkin);
        newButton = new TextButton("+",uiSkin);
        removeMap = new TextButton("-",uiSkin);
        removeMap.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event,float x,float y)
            {
                //Remove o ultimo mapa adicionado no level.
                level.removeNode();
                updateList();
            }
        });  
        newButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event,float x,float y)
            {
                //Quando esse botão é cliacado, ele permita a criação de um novo mapa.
                //Para simplificar a implementação(menos código de interface),
                //todos os mapas são 100x100.
                //Isso pode ser facilmente mudado no futuro.
                LevelNode newNode = new LevelNode(100,100);
                level.addNode(newNode);
                newNode.saveMap();
                updateList();
            }
        });  
        add(new SplitPane(newButton,removeMap,false,uiSkin));
    }
    
}
