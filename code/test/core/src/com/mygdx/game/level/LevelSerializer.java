
package com.mygdx.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.map.CollisionMap;

public class LevelSerializer 
{
    //Essa classe pode salvar ou carregar Level baseando-se num dado diretorio.
    FileHandle stageFolder;
    Json jason;
    String name;
    
    public LevelSerializer(String path)
    {
        this.name = path;
        stageFolder = Gdx.files.local(name + "/" + "stage.json"); 
        jason = new Json();
    }
    
    public void storeStage(Level stage)
    {
       jason.setOutputType(JsonWriter.OutputType.json);
       //Salva as informações do level:

       stageFolder.writeString(jason.toJson(stage),false);
       
       //Salva o mapa em cada nó:
       for(LevelNode node : stage.stageNodes)
       {
           //Só salva se o mapa é diferente de null
           //Isso só vai ocorrer(barrando situações anomalas) quando o mapa 
           //não foi aberto, ou seja, não foi modificado durante a execução
           //do programa.
           if(node.getMap() != null)
           {
                node.saveMap();
           }
       }
    }
    
    public Level loadStage()
    {
       Level loaded;
       loaded = jason.fromJson(Level.class,stageFolder);

       for(LevelNode node : loaded.stageNodes)
       {
           //Carrega o serializador em cada nó
           node.startLoadSave();
       }
       return loaded;
    }
}
