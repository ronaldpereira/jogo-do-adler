
package com.mygdx.game.level;

import java.util.ArrayList;

public class Level 
{
    //Simboliza um conjunto de levelNodes.
    //Na prática, é um grupo de níveis que o jogador deve enfrentar.
    
    ArrayList<LevelNode> stageNodes;
    int currentNode = 0;
    protected String folder;
    
    //Musica default:
    protected String levelMusic = "ad4.ogg";
    
    //Construtor vazio para serialização
    private Level()
    {
        
    }

    public Level(String name, ArrayList<LevelNode> stageNodes)
    {
        this.folder = "Maps/" + name;
        this.stageNodes = stageNodes;
        for(int i = 0;i < this.stageNodes.size();i++)
        {
            this.stageNodes.get(i).setMapPath(folder);
            this.stageNodes.get(i).setMapName(Integer.toString(i));
        }
    }
    
    //Reseta o nivel atual do mapa para zero
    public boolean levelFinished()
    {
        return currentNode >= (stageNodes.size() - 1);
    }
    public void reset()
    {
        currentNode = 0;
    }
    
    public LevelNode getNode(int index)
    {
        return stageNodes.get(index);
    }
     
    public LevelNode getInitialNode()
    {
        return getNode(0);
    }
    
    public String getFolder()
    {
        return folder;
    }

    public LevelNode getCurrentNode() 
    {
        return getNode(currentNode);
    }
    
    //Adiciona um nó e atualiza o nome/diretório dele:
    public void addNode(LevelNode node)
    {
        stageNodes.add(node);
        stageNodes.get(stageNodes.size() - 1).setMapPath(folder);
        stageNodes.get(stageNodes.size() - 1).setMapName(Integer.toString((stageNodes.size() - 1)));
        new LevelSerializer(getFolder()).storeStage(this);
    }
    
    //Remove um nó e seus arquivos no diretório
    public void removeNode()
    {
        int node = stageNodes.size() - 1;
        if(node != 0)
        {
            if(currentNode == node)
            {
                currentNode--;
            }
            stageNodes.get(node).deletetThis();
            stageNodes.remove(node);
            new LevelSerializer(getFolder()).storeStage(this);
        }

    }
    
    public void nextLevel()
    {
        if(!levelFinished())
        {
            currentNode++;
        }
    }
    
    public String getMusic()
    {
        return levelMusic;
    }
    
    public void setMusic(String musicPath)
    {
        levelMusic = musicPath;
    }
}
