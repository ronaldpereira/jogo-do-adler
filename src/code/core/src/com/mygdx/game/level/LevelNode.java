
package com.mygdx.game.level;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.map.MapSerializer;


public class LevelNode 
{
    //Nó de um level.
    //Armazena um collisionMap e o nome do dirétorio onde ele é armazenado.
    //Possui metodos para salvar e carregar o mapa.
    transient CollisionMap nodeMap;
    String mapPath;
    String name;
    Vector2 initialPosition;
    transient MapSerializer loadsave;
    
    
    //Construtor vazio para serialização
    private LevelNode()
    {
        nodeMap = null;
    }
    
    public LevelNode(CollisionMap map)
    {
        nodeMap = map;
        initialPosition = new Vector2(1,2);
        startLoadSave();
    }
    
    public LevelNode(int width,int height)
    {
        nodeMap = new CollisionMap(width,height);
        initialPosition = new Vector2(1,2);
        startLoadSave();
    }
    
    public void setMapPath(String path)
    {
        mapPath = path;
        loadsave.setFiles(name,mapPath);
    }
    
    public void setMapName(String name)
    {
        this.name = name;
        loadsave.setFiles(this.name,mapPath);
    }
    
    public void saveMap()
    {
        loadsave.storeMap(nodeMap);
    }
    
    public void loadMap()
    {
        nodeMap = new CollisionMap(0,0);
        loadsave.loadMap(nodeMap);
    }
    
    //Inicializa o serializador
    public void startLoadSave()
    {
        loadsave = new MapSerializer(name,mapPath);
    }
    
    public CollisionMap getMap()
    {
        //Se o mapa ainda não foi carregado, carrega ele do diretorio.
        if(nodeMap == null)
        {
            loadMap();
        }
        
        return nodeMap;
    }
    
    public Vector2 getInitialPos()
    {
        return initialPosition;
    }
    
    public void deletetThis()
    {
        loadsave.deleteDirectory();
    }
    //Recarrega os objetos dinnamicos no mapa(por exemplo, quando o jogador morre).
    public void reloadObjects()
    {
        loadsave.reloadDynamic(nodeMap);
    }
}
