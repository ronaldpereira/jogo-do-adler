
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.IBuildableObject;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.map.TileMap;
import java.util.ArrayList;

public class TileSelector extends Table      
{
    //Seletor de tiles do mapBuilder.
    ArrayList<ImageButton> tileButtons;
    Image tilePicker;
    int tileIndex = 0;
    
    public TileSelector(ArrayList<IBuildableObject> possibleTiles)
    {
        super();
        TextureRegion temp;
        tileButtons = new ArrayList<ImageButton>();
        tilePicker = new Image(new Texture("picker.png"));
        
        for(IBuildableObject  tile : possibleTiles)
        {
            if(tile.getSheet() != null)
            {
                //Cria a imagem no seletor.
                temp = new TextureRegion(tile.getSheet().getFrame(0));
                temp.setRegionWidth(TileMap.getTileSize());
                temp.setRegionHeight(TileMap.getTileSize());
                tileButtons.add(new ImageButton(new TextureRegionDrawable(temp)));
            }
        }
        
        for(int i = 0;i < tileButtons.size();i++)
        {
            final int index = i;
            
            tileButtons.get(i).addListener(new ClickListener()
            {
                //Listener diferente para cada objeto.
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Quando clicado, cada objeto retorna seu index na lista de objetos possiveis.
                    tilePicker.setVisible(true);
                    tileIndex = index;
                    tilePicker.setPosition(tileButtons.get(index).getX(),tileButtons.get(index).getY());
                }
            }
            );
            
            add(tileButtons.get(i)).uniform();
            if((i + 1) % 4 == 0)
            {
                //A cada 4 objetos, cria uma nova linha.
                this.row();
            }
        }
        tilePicker.setVisible(false);
        add(tilePicker);
        
    }
    
    public int getTileIndex()
    {
        return tileIndex;
    }
}
