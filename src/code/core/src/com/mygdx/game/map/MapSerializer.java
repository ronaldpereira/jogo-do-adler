package com.mygdx.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.mygdx.game.collidable.DynamicCollider;
import com.mygdx.game.tile.AbstractTile;
import java.util.ArrayList;

public class MapSerializer 
{
    //Essa classe é usada para armazenar CollisionMaps em arquivos json.
    //Na pratica, só o tileMap e os objetos dinamicos são guardados.
    
    FileHandle fileTiles;
    FileHandle fileDynamic;
    Json jason;
    String path;
    
    public MapSerializer(String mapName, String mapPath)
    {
        path = mapPath + "/" + mapName;
        setFiles(mapName,mapPath);
        
        jason = new Json();
        jason.setSerializer(Texture.class,new Serializer<Texture>()
        {
            //Serializador customizado para texturas;
            //O serializador guarda apenas o caminho onde a textura é guardada
            //para economizar espaço.
            @Override
            public void write(Json json,Texture texture,Class knowType)
            {   
                json.writeObjectStart();
                if(texture == null)
                {
                    json.writeValue("texturePath","null");
                    json.writeObjectEnd();
                    return;
                }
                
                //Isso escreve o caminho no arquivo:
                json.writeValue("texturePath",((FileTextureData)texture.getTextureData()).getFileHandle().path());
                json.writeObjectEnd();
            }
            
            @Override
            public Texture read(Json json,JsonValue jsonData,Class type)
            {
                //Le o caminho da textura e recria ela:
                String path = jsonData.getString("texturePath");
                if(path.equals("null"))
                {
                    return null;
                }
                return new Texture(jsonData.getString("texturePath"));
            }
        }
        );
    }
    
    //Reseta os arquivos com base no mapName e mapPath.
    public void setFiles(String mapName, String mapPath)
    {
        path = mapPath + "/" + mapName;
        fileTiles = Gdx.files.local(mapPath + "/" + mapName +"/" + mapName +"_tiles.json");
        fileDynamic = Gdx.files.local(mapPath + "/" + mapName +"/" + mapName +"_dynamic.json");
    }
    
    public void storeMap(CollisionMap map)
    {
        //Armazena os tiles e os objetos dinamicos em arquivos separados.
        jason.setOutputType(OutputType.json);
        fileTiles.writeString(jason.toJson(map.getTileMap().getMap(),AbstractTile[][].class,AbstractTile.class),false);
        fileDynamic.writeString(jason.toJson(map.localObjs,ArrayList.class,DynamicCollider.class),false);
    }
    
    public void loadMap(CollisionMap map)
    {
        //Le os tiles:
        AbstractTile[][] mapLoad;
        mapLoad = jason.fromJson(AbstractTile[][].class,fileTiles);
        map.map = new TileMap(mapLoad);
        
        //Le os objetos dinamicos:
        ArrayList<DynamicCollider> objs = jason.fromJson(ArrayList.class, fileDynamic);
        
        //Adiciona cada objeto na QuadTree do mapa.
        for(DynamicCollider obj : objs)
        {
            map.addMovingObject(obj);
        }
    }
    
    public void reloadDynamic(CollisionMap map)
    {
        //Essa função recarrega os objetos dinamicos no mapa.
        //Ela é usada quando o personagem morre e os monstros devem renascer.
        
        map.movingObjects.clear();
        map.localObjs.clear();
        ArrayList<DynamicCollider> objs = jason.fromJson(ArrayList.class, fileDynamic);
        
        //Adiciona cada objeto na QuadTree do mapa.
        for(DynamicCollider obj : objs)
        {
            map.addMovingObject(obj);
        }
    }
    
    public void deleteDirectory()
    {

        Gdx.files.local(path).deleteDirectory();
    }

}
