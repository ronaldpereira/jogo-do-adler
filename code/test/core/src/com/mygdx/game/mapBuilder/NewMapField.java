
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelNode;
import com.mygdx.game.level.LevelSerializer;

public class NewMapField extends ConfirmableTextField
{
    Level level;
    public NewMapField(Level l, Skin uiSkin) {
        super("New Map", "Map size:", uiSkin);
        level = l;
    }

    @Override
    protected void onConfirm() 
    {
        LevelNode newNode = new LevelNode(Integer.parseInt(fieldText),Integer.parseInt(fieldText));
        level.addNode(newNode);
        newNode.saveMap();
    }
    
}
