/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;
import com.mygdx.game.Tiles.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.*;
import com.mygdx.game.Tiles.AbstractTile;
import com.mygdx.game.Tiles.TileFactory;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.min;
import java.util.ArrayList;
/**
 *
 * @author Hiago
 */
public class CollisionMap 
{
    private static final Texture BLOCK = new Texture("BlackBlock.png");
    private static final Texture ICE = new Texture("ice.png");
    private static final Texture COIN = new Texture("Chinelo.png");
    public AbstractTile[][] map;
    
    public static final int tileSize = 32;
    private int width;
    private int height;
    private Quadtree movingObjects;
    private ArrayList<DynamicCollider> localObjs;
    
    public CollisionMap(int width,int height)
    {
        this.width = width;
        this.height = height;
        map = new AbstractTile[width][height];
        
        //QUADTREE
        movingObjects = new Quadtree(0,new Rectangle(0,0,width * tileSize,height * tileSize)); 
        
        for(int i = 0;i < width;i++)
        {
            for(int j = 0;j < height;j++)
            {
                map[i][j] = TileFactory.createEmpty(i, j);
            }
        }
    }
    public void addMovingObject(DynamicCollider obj)
    {
        movingObjects.insert(obj);
    }
    
    //Cria um tile na posição [x][y] usando como guia um certo "gerador"
    //Na prática, o objeto criado será uma copia do gerador mas com posição diferente
    public void createTile(int x,int y,AbstractTile generator)
    {
        if(x < 0 || x > width || y < 0 || y > height)
        {
            return;
        }
        map[x][y] = generator.createNew(generator, x*tileSize,y*tileSize);
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    public void clearTile(int x,int y)
    {
        if(x < 0 || x > width || y < 0 || y > height)
        {
            return;
        }
        map[x][y] = TileFactory.createEmpty(x*tileSize,y*tileSize);
    }
    
    public void renderMap(SpriteBatch batch)
    {
        for(int i = 0;i < width;i++)
        {
            for(int j =0;j < height;j++)
            {
                map[i][j].drawSelf(batch);
            }
        }
    }
    
    //Retorna uma dada posição para uma posição no mapa(um tile).
    public Vector2 findTile(float x,float y)
    {       
        return new Vector2((int)x/tileSize,(int)y/tileSize);
    }
    
    //Retorna o tile na posição [x][y].
    public AbstractTile getTile(int x,int y)
    {
        return map[x][y];
    }
    
    public int getTileSize()
    {
        return tileSize;
    }
    
    public Vector2 getHorizontalIntersection(BoundingBox boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).x);
        int max = (int)(findTile((boundingBox.x + boundingBox.width),boundingBox.y).x);

        return new Vector2(min,max);
    }
    
    public Vector2 getVerticalIntersection(Rectangle boundingBox)
    {
        int min = (int)(findTile(boundingBox.x,boundingBox.y).y);
        int max = (int)(findTile(boundingBox.x,(boundingBox.y + boundingBox.height)).y);
        
        return new Vector2(min,max);
    }
    
    private boolean checkIntersection(Vector2 a,Vector2 b)
    {
        if(a.x <= b.y && a.x >= b.x)
        {
            return true;
        }
        else if(b.x <= a.y && b.x >= a.y)
        {
            return true;
        }
        return false;
    }
    
    //DETECÇÃO DE COLISÕES//
    public void closestCollisionX(DynamicCollider entity,int direction,float distance)
    {
        //Se a direção é zero, o objeto não vai se mover nesse eixo
        //logo, não é necessario verificar colisões*
        //*isso só ocorre porque as colisões são sempre bilaterais,
        //Se um outro objeto se move em direção a esse, 
        //a colisão ainda vai ocorrer.
        if(direction == 0)
        {
            return;
        }
        
        //O numero de tiles que o objeto "deseja" andar:
        int tileDistance = (int)ceil(distance/32);
        int xPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = getVerticalIntersection(box);
       
        localObjs = movingObjects.getNearby(entity);
        
        float colDistance = -1;
        //-1 aqui sinaliza apenas que o valor ainda não foi inicializado.
        CollidableObject colObj = null;
        
        //A lista de todos os objetos dinamicos próximos a esse 
        //é percorrida, e é encontrado o objeto primeiro objeto com o qual
        //esse colide
        for(DynamicCollider obj : localObjs)
        {
            if(checkIntersection(intersection,getVerticalIntersection(obj.getBoundingBox())))
            {
                if(box.getHorizontalDistance(obj.getBoundingBox()) < distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.getBoundingBox().x < entity.getBoundingBox().x && direction < 0) ||(obj.getBoundingBox().x > entity.getBoundingBox().x && direction > 0))
                    {
                        if(obj.getBlocks())
                        {
                            System.out.println("eu entro aqui");
                            if(colDistance == -1)
                            {
                                colDistance = box.getHorizontalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            //colDistance = box.getHorizontalDistance(obj.boundingBox)
                            }
                            else if(box.getHorizontalDistance(obj.getBoundingBox()) < colDistance)
                            {
                                colDistance = box.getHorizontalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                        }
                    }//colObj = obj;
                }  
            }    
        }
        
        if(colDistance != -1)
        {
            //Se o valor de colDistance não é -1, uma colisão foi encontrada,
            //Então a distancia maxima que o objeto pode andar é então ajustada.
            tileDistance = min((int)ceil(colDistance/32),tileDistance);
        }   
        
        //Depois de verificar as colisões dinamicas, 
        //são verificadas as colisões estaticas, em busca de um tile ainda mais
        //proximo do personagem que o objeto dinamico previamente encontrado.
           
        if(direction > 0)
        {
            xPos = (int)(findTile((int)box.getRight(),(int)box.y).x);
        }
        else
        {
            xPos = (int)(findTile((int)box.getLeft(),(int)box.y).x);
        }
        
        while(xPos >= 0 && xPos < width)
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(i < 0 || i > width)
                {
                    return;
                }
                if(map[xPos][i].getCollides())
                {    
                    if(map[xPos][i].getBlocks())
                    {
                        if(colObj != null)
                        {
                            //Acho que isso é desnecessário:
                            //if(box.getHorizontalDistance(colObj.getBoundingBox()) < box.getHorizontalDistance(map[xPos][i].getBoundingBox()))
                            //{
                            colObj = map[xPos][i];
                            break;
                            //}
                        }
                        else
                        {
                            colObj = map[xPos][i];
                            break;
                        }
                    }
                }
            }

            delta += 1;  
            xPos += (delta * direction);
            
            if(delta > tileDistance)
            {
                break;
            }
        }
        
        if(colObj == null)
        {
            horizontalCollision(entity,direction,distance);
        }
        else
        {
            horizontalCollision(entity,direction,box.getHorizontalDistance(colObj.getBoundingBox()));
        }
    }
    
    private void horizontalCollision(DynamicCollider entity,int direction,float distance)
    {
        
        //movingObjects.clear();
        int tileDistance = (int)ceil(distance/32);
        int xPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = getVerticalIntersection(box);
 
        localObjs = movingObjects.getNearby(entity);
        
        for(DynamicCollider obj : localObjs)
        {
            if(checkIntersection(intersection,getVerticalIntersection(obj.getBoundingBox())))
            {
                if(box.getHorizontalDistance(obj.getBoundingBox()) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.getBoundingBox().x < entity.getBoundingBox().x && direction < 0) ||(obj.getBoundingBox().x > entity.getBoundingBox().x && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,HORIZONTAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,HORIZONTAL_AXIS));
                    }//colObj = obj;
                }
            }    
        }   
        
        if(direction > 0)
        {
            xPos = (int)(findTile((int)box.getRight(),(int)box.y).x);
        }
        else
        {
            xPos = (int)(findTile((int)box.getLeft(),(int)box.y).x);
        }
        
        while(xPos >= 0 && xPos < width)
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(map[xPos][i].getCollides())
                {    
                    entity.collide(map[xPos][i],new CollisionInfo(entity,HORIZONTAL_AXIS));
                    map[xPos][i].collide(entity,new CollisionInfo(map[xPos][i],HORIZONTAL_AXIS));
                }
            }

            delta += 1;  
            xPos += (delta * direction);
            
            if(delta > tileDistance + 1)
            {
                break;
            }
        }
    }
    
    public void closestCollisionY(DynamicCollider entity,int direction,float distance)
    {
        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = getHorizontalIntersection(box);
        
        localObjs = movingObjects.getNearby(entity);
        
        float colDistance = -1;
        CollidableObject colObj = null;
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getHorizontalIntersection(obj.getBoundingBox())))
            {
                if(box.getVerticalDistance(obj.getBoundingBox()) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.getBoundingBox().y < entity.getBoundingBox().y && direction < 0) ||(obj.getBoundingBox().y > entity.getBoundingBox().y && direction > 0))
                    {
                        if(obj.getBlocks())
                        {
                            System.out.println("eu entro aqui");
                            if(colDistance == -1)
                            {
                                colDistance = box.getVerticalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            //colDistance = box.getHorizontalDistance(obj.boundingBox)
                            }
                            else if(box.getVerticalDistance(obj.getBoundingBox()) < colDistance)
                            {
                                colDistance = box.getVerticalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                        }
                    }//colObj = obj;
                }
            }    
        }
        
        if(colDistance != -1)
        {
            tileDistance = min((int)ceil(colDistance/32),tileDistance);
        }
        if(direction > 0)
        {
            yPos = (int)(findTile((int)box.x,(int)box.getUp()).y);
        }
        else
        {
            yPos = (int)(findTile((int)box.x,(int)box.getDown()).y);
        }
        
        while(yPos >= 0 && yPos < height)
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(i < 0 || i > height)
                {
                    return;
                }
                
                if(map[i][yPos].getCollides())
                {    
                    if(map[i][yPos].getBlocks())
                    {
                        if(colObj != null)
                        {
                            if(box.getVerticalDistance(colObj.getBoundingBox()) < box.getVerticalDistance(map[i][yPos].getBoundingBox()))
                            {
                                colObj = map[i][yPos];
                                break;
                            }
                        }
                        else
                        {
                            colObj = map[i][yPos];
                            break;
                        }
                    }
                }
            }

            delta += 1;  
            yPos += (delta * direction);
            
            if(delta > tileDistance)
            {
                break;
            }
        }
        
        if(colObj == null)
        {
            verticalCollision(entity,direction,distance);
        }
        
        else
        {
            verticalCollision(entity,direction,box.getVerticalDistance(colObj.getBoundingBox()));
        }
    }
    
    private void verticalCollision(DynamicCollider entity,int direction,float distance)
    {
        if(direction == 0)
        {
            return;
        }
        //movingObjects.clear();
        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = getHorizontalIntersection(box);
        //System.out.println("Inter :" + intersection.toString());
        

                //if(localObjs)
                //if
            //}
        localObjs = movingObjects.getNearby(entity);
        
        for(DynamicCollider obj : localObjs)
        {
            //System.out.println(localObjs.size());
            //if(entity.boundingBox.overlaps(obj.boundingBox) && entity != obj)
            //{
            if(checkIntersection(intersection,getHorizontalIntersection(obj.getBoundingBox())))
            {
                if(box.getVerticalDistance(obj.getBoundingBox()) <= distance)
                {
                    System.out.println("eu entro aqui 1");
                    if((obj.getBoundingBox().y < entity.getBoundingBox().y && direction < 0) ||(obj.getBoundingBox().y > entity.getBoundingBox().y && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,VERTICAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,VERTICAL_AXIS));
                    }//colObj = obj;
                }
            }    
        }   
        
        if(direction > 0)
        {
            yPos = (int)(findTile((int)box.x,(int)box.getUp()).y);
        }
        else
        {
            yPos = (int)(findTile((int)box.x,(int)box.getDown()).y);
        }
        
        while(yPos >= 0 && yPos < height)
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(map[i][yPos].getCollides())
                {    
                    entity.collide(map[i][yPos],new CollisionInfo(entity,VERTICAL_AXIS));
                    map[i][yPos].collide(entity,new CollisionInfo(map[i][yPos],VERTICAL_AXIS));
                }
            }

            delta += 1;  
            yPos += (delta * direction);
            
            if(delta > tileDistance + 1)
            {
                break;
            }
        }
    }
}