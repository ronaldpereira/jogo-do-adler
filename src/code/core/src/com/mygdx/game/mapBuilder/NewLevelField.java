
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelNode;
import com.mygdx.game.level.LevelSerializer;
import com.mygdx.game.map.CollisionMap;
import java.util.ArrayList;

public class NewLevelField extends ConfirmableTextField 
{
    public NewLevelField(Skin uiSkin)
    {
        super("New Level","Level Name:",uiSkin);
    } 

    @Override
    protected void onConfirm() 
    {
        ArrayList<LevelNode> nodes = new ArrayList<LevelNode>();
        nodes.add(new LevelNode(new CollisionMap(100,100)));
        Level level = new Level(fieldText,nodes);
        LevelSerializer save = new LevelSerializer(level.getFolder());
        save.storeStage(level);
    }
    
}
