
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.util.ArrayList;

public class FolderSelector extends Table 
{   
    //Essa clase representa um seletor de diretorios.
  
    protected String currentMap;      
    private String stageName;
    private FileHandle[] maps;
    private FileHandle file;
    private List foundMaps;
    protected int selectedStage = 0;
    
    public FolderSelector(String directory, Skin uiSkin)
    {
       
        this.stageName = directory;
        file = Gdx.files.local(stageName);
        maps = file.list();
        
        //Cada diretorio representa um mapa diferente e tem um "botão" designado a ele
        Label tmpLabel;
        foundMaps = new List(uiSkin);
        ArrayList<FileHandle> folders = new ArrayList<FileHandle>();
        for(FileHandle map : maps)
        {
            if(map.isDirectory())
            {
                //Pega todos os arquivos que são diretórios.
                folders.add(map);
            }
        }
        
        foundMaps.setItems(folders.toArray(new FileHandle[0]));
        add(foundMaps);
        addListener(new ChangeListener() 
        {
            @Override
                 public void changed(ChangeListener.ChangeEvent event, Actor actor) 
                 {
                    //Quando ocorre uma mudança, o diretorio atual escolhido é atualizado.
                    currentMap = foundMaps.getSelected().toString();
                    selectedStage = foundMaps.getSelectedIndex();
                 }
        });
        currentMap = foundMaps.getSelected().toString();
    }

    public void updateList() 
    {
        //Recalcula as pastas no diretório.
        file = Gdx.files.local(stageName);
        maps = file.list();
        ArrayList<FileHandle> folders = new ArrayList<FileHandle>();
        for(FileHandle map : maps)
        {
            if(map.isDirectory())
            {
                folders.add(map);
            }
        }
        foundMaps.setItems(folders.toArray((FileHandle[]) new FileHandle[0]));
    }
}
