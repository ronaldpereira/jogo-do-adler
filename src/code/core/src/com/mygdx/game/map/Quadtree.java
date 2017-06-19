/*Forma da Quadtree

      2 | 1
    ____|____
      3 | 4
        | 
*/


package com.mygdx.game.map;

import com.mygdx.game.collidable.DynamicCollider;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Quadtree 
{
    //limite de cobjetos que o no pode guardar
    private int MAX_OBJECTS = 10;
   
    private int level;
    private ArrayList<DynamicCollider> objects;
    private Rectangle bounds;
    private Quadtree[] nodes;
   
    public Quadtree(int pLevel, Rectangle pBounds)  
    {
        level = pLevel;
        objects = new ArrayList<DynamicCollider>();
        bounds = pBounds; 
        nodes = new Quadtree[4];
    }
    
    public boolean isLeaf()
    {
        if(nodes[0] != null) return false;
        return true;
    }

    //limpa a quadtree
    public void clear()
    {
        objects.clear();
        
        for (int i = 0; i < nodes.length; i++) 
        {
            if (nodes[i] != null)
            {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }
    public void remove(DynamicCollider obj)
    {
        if(isLeaf()) objects.remove(obj);
        else 
        {
            int index = getIndex(obj);
            nodes[index].remove(obj);
        }
    }
    
    //cria os quatro filhos
    private void split() 
    {
        int subWidth = (int)(bounds.getWidth() / 2); 
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX(); 
        int y = (int)bounds.getY();
         
        nodes[0] = new Quadtree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new Quadtree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new Quadtree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    //objeto nem sempre vai ser um retangulo
    private int getIndex(DynamicCollider thing)
    {
       int index = -1;
       double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
       double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
     
       // objeto ta nos quadrantes superiores
       boolean topQuadrant = (thing.getBoundingBox().y + thing.getBoundingBox().height > horizontalMidpoint);////
       // objeto ta nos quadrantes inferiores
       boolean bottomQuadrant = (thing.getBoundingBox().y <= horizontalMidpoint);
     
       // objeto ta nos quadrantes da esquerda
       //isso deve ser mudado pra isso:
       if (thing.getBoundingBox().x < verticalMidpoint)
        {
            //quadrante 2
            if (topQuadrant) 
            {
                index = 1;
            }
            //quadrante 3
            else if (bottomQuadrant) 
            {
                index = 2;
            }
        }
        // objeto ta nos quadrantes da direita
        else if (thing.getBoundingBox().x >= verticalMidpoint)
        {
            //quadrante 1
            if (topQuadrant) 
            {
                index = 0;
            }
            //quadrante 4
            else if (bottomQuadrant)
            {
                index = 3;
            }
       }
       return index;
    }


    public void insert(DynamicCollider thing)
    {
        if (!isLeaf()) 
        {
            int index = getIndex(thing);
            nodes[index].insert(thing);
            return;

        }
 
        if (objects.size() + 1> MAX_OBJECTS) 
        {
            if (isLeaf()) 
            {
                split();
            
                int i = 0;

                while (i < objects.size()) 
                {
                        int index = getIndex(objects.get(i));
                    nodes[index].insert(objects.get(i));
                    objects.remove(i);
                    i++;

                }
            }
        }
        else objects.add(thing);
    }
    //thing eh o objeto que vc quer ver se colide com alguem
    public  ArrayList getNearby(DynamicCollider entity)
    {
        ArrayList<DynamicCollider> returnObjects = new ArrayList<DynamicCollider>();
        retrieve(returnObjects,entity);
        return returnObjects;
    }
    private ArrayList retrieve(ArrayList<DynamicCollider> returnObjects, DynamicCollider thing)
    {
        if (!isLeaf())
        {
            int index = getIndex(thing);
            if (index != -1) 
            {
                nodes[index].retrieve(returnObjects, thing);
            }
            else 
            {
                for (Quadtree node : nodes) {
                    node.retrieve(returnObjects, thing);
                }
            }
        }
    
        returnObjects.addAll(objects);
        return returnObjects; //retorna os objetos os quais 'thing' pode colidir
    }
}

/*
    //cria a arvore e passa os limites da tela
    Quadtree tree = new Quadtree(0, new Rectangle(0,0, 600,600));
    ArrayList<DynamicCollider> allObjects = new ArrayList<DynamicCollider>();
    ArrayList<DynamicCollider> possibleCollisions = new ArrayList<DynamicCollider>();

    //fazer isso em todo frame
    tree.clear();
    for(int i = 0; i < allObjects.size(); i++)
    {
        tree.insert(allObjects.get(i));
    }

    for(int i = 0; i < allObjects.size(); i++)
    {
        possibleCollisions.clear();
                                          //objeto q vc quer ver se colide
        tree.retrieve(possibleCollisions, tree.objects.get(i)); //??

        for(int j = 0; j < possibleObjects.size(); j++)
        {
            //faz o algoritmo de colisao pra esses objetos.
        }
    }
*/