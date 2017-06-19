
package com.mygdx.game.map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BoundingBox;
import com.mygdx.game.collidable.GameObject;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.collidable.DynamicCollider;
import static com.mygdx.game.Axis.*;
import com.mygdx.game.IGenerator;
import com.mygdx.game.enemy.AbstractEnemy;
import com.mygdx.game.tile.AbstractTile;
import com.mygdx.game.tile.EmptyTile;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.Iterator;

public class CollisionMap 
{
    //Essa classe guarda todos os objetos estaticos(usando tileMap), e dinamicos.
    //Ela é também responsável por verificar colisões.
    
    protected TileMap map;
    protected Quadtree movingObjects;
    protected ArrayList<DynamicCollider> localObjs;
    private ArrayList<DynamicCollider> nearby;
    private Vector2 spawnPoint = new Vector2(100,100);
    
    public CollisionMap(int width,int height)
    {
        localObjs = new ArrayList<DynamicCollider>();
        map = new TileMap(width,height);
        movingObjects = new Quadtree(0,new Rectangle(0,0,width * TileMap.getTileSize(),height * TileMap.getTileSize())); 
    }
    
    public void setSpawnPoint(Vector2 point)
    {
        spawnPoint = point;
        
    }
    
    public Vector2 getSpawnPoint()
    {
        return spawnPoint;
    }
    public TileMap getTileMap()
    {
        return map;
    }
    public void addMovingObject(DynamicCollider obj)
    {
        obj.setMap(this);
        movingObjects.insert(obj);
        localObjs.add(obj);
    }
    
    public void addPlayer(ControllableCharacter player)
    {
        movingObjects.insert(player);
        player.setMap(this);
    }
    
    //Isso só ocorre em situações de erro(Mensagem para debug):
    public void createTile(int x,int y, IGenerator generator)
    {
        System.out.println("Builder não foi definido!");
    }
    
    //Cria um tile na posição [x][y] usando como guia um certo "gerador"
    public void createTile(int x,int y, AbstractTile generator)
    {
        if(x < 0 || x > map.getWidth() || y < 0 || y > map.getHeight())
        {
            return;
        }
        
        clearObjects(x,y);
        map.setTile(x, y, generator);
    }
    
    //Cria um inimigo na posição [x][y] usando como guia um certo "gerador"
    public void createTile(int x,int y, AbstractEnemy generator)
    {
        if(x < 0 || x > map.getWidth() || y < 0 || y > map.getHeight())
        {
            return;
        }
        
        clearObjects(x,y);
        addMovingObject(generator);
    }
    
    //Cria um tile vazio na posição[x][y]
    public void clearTile(int x,int y)
    {
        map.setTile(x, y, new EmptyTile(x*TileMap.getTileSize(),y*TileMap.getTileSize()));
    }
    
    //Limpa todos os objetos que tem interceção na posição [x][y].
    public void clearObjects(int x,int y)
    {
        Iterator<DynamicCollider> iter = localObjs.iterator();
        DynamicCollider temp;
        Vector2 position = new Vector2(x,y);
        //Percorre todos os objetos:
        while(iter.hasNext())
        {
            temp = iter.next();
            //Pega as interceções horizontais e verticais:
            Vector2 h = map.getHorizontalIntersection(temp.getBoundingBox());
            Vector2 v = map.getVerticalIntersection(temp.getBoundingBox());
            
            //Encontra o tile onde a origem do objeto se encontra((x,y) da boundingBox).
            Vector2 origin = map.findTile(temp.getBoundingBox().getX(),temp.getBoundingBox().getY());
            for(int i = (int) h.x;i <= h.y;i++)
            {
                //Percorre os tiles que o objeto tem interceção horizontalmente,
                //e verifica se algum desses tiles é a posição x,y.
                if(new Vector2(i,origin.y).equals(position))
                {
                    //Se sim,remove o objeto.
                    movingObjects.remove(temp);
                    iter.remove();
                    return;
                } 
            }
                //Percorre os tiles que o objeto tem interceção verticalmente,
                //e verifica se algum desses tiles é a posição x,y.
            for(int i = (int) v.x;i <= v.y;i++)
            {
                if(new Vector2(origin.x,i).equals(new Vector2(x,y)))
                {
                    //Se sim,remove o objeto.
                    movingObjects.remove(temp);
                    iter.remove();
                    return;
                } 
            }
        }
    }
    
    public void clearPosition(int x,int y)
    {
        if(x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight())
        {
            return;
        }
        
        clearTile(x,y);
        clearObjects(x,y);
    }
    
    public void renderMap(SpriteBatch batch)
    {
        //Desenha todos os objetos no mapa.
        map.renderMap(batch);
        
        for(DynamicCollider obj: localObjs)
        {
            obj.drawSelf(batch);
        }
    }
    
    public void removeMovingObject(DynamicCollider obj)
    {
        movingObjects.remove(obj);
    }
    
    public void update()
    {
        //Atualiza todos os objetos no mapa.
        for(DynamicCollider obj : localObjs)
        {
            obj.update();
        }
    }
    
    //DETECÇÃO DE COLISÕES
    
    //Atualiza o vetor nearby para guardar os objeto proximos desse
    public void updateNearby(DynamicCollider entity)
    {
        nearby = movingObjects.getNearby(entity);
    }
   
    //Essa função encontra a primeira colisão dinamica que 
    //bloqueia o movimento no eixo X,se ela existe
    private GameObject findClosestDynamicX(DynamicCollider entity,int direction,float distance)
    {
        BoundingBox box = entity.getBoundingBox();
        Vector2 intersection = map.getVerticalIntersection(box);
        float colDistance = -1;
        //-1 aqui sinaliza apenas que o valor ainda não foi inicializado.
        GameObject colObj = null;
        
         //A lista de todos os objetos dinamicos próximos a esse 
        //é percorrida, e é encontrado o objeto primeiro objeto com o qual
        //esse colide
        for(DynamicCollider obj : nearby)
        {
            //Se existe interseção entra as areas verticais:
            if(map.checkIntersection(intersection,map.getVerticalIntersection(obj.getBoundingBox())))
            {
                //Se a distancia do objeto é menor de que a distancia que o objeto deseja andar:
                if(box.getHorizontalDistance(obj.getBoundingBox()) <= distance)
                {
                    //Se o objeto está no caminho desse:
                    if((obj.getBoundingBox().getX() < entity.getBoundingBox().getX() && direction < 0) ||(obj.getBoundingBox().getX() > entity.getBoundingBox().getX() && direction > 0))
                    {
                        //Se o objeto bloqueia:
                        if(obj.getBlocks())
                        {
                            if(colDistance == -1)
                            {
                                colDistance = box.getHorizontalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                            else if(box.getHorizontalDistance(obj.getBoundingBox()) < colDistance)
                            {
                                colDistance = box.getHorizontalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                        }
                    }
                }  
            }    
        }
         
        return colObj;
    }
    
    //Essa função encontra a primeira colisão dinamica que 
    //bloqueia o movimento no eixo Y,se ela existe
    private GameObject findClosestDynamicY(DynamicCollider entity,int direction,float distance)
    {
        BoundingBox box = entity.getBoundingBox();
        Vector2 intersection = map.getHorizontalIntersection(box);
        
        float colDistance = -1;
        //-1 aqui sinaliza apenas que o valor ainda não foi inicializado.
        GameObject colObj = null;
        
        //A lista de todos os objetos dinamicos próximos a esse 
        //é percorrida, e é encontrado o objeto primeiro objeto com o qual
        //esse colide
        for(DynamicCollider obj : nearby)
        {
            //Se existe interseção entra as areas horizontais:
            if(map.checkIntersection(intersection,map.getHorizontalIntersection(obj.getBoundingBox())))
            {
                //Se a distancia do objeto é menor de que a distancia que o objeto deseja andar:
                if(box.getVerticalDistance(obj.getBoundingBox()) <= distance)
                {
                    //Se o objeto está no caminho desse:
                    if((obj.getBoundingBox().getY() < entity.getBoundingBox().getY() && direction < 0) ||(obj.getBoundingBox().getY() > entity.getBoundingBox().getY() && direction > 0))
                    {
                        //Se o objeto bloqueia:
                        if(obj.getBlocks())
                        {
                            if(colDistance == -1)
                            {
                                colDistance = box.getVerticalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                            else if(box.getVerticalDistance(obj.getBoundingBox()) < colDistance)
                            {
                                colDistance = box.getVerticalDistance(obj.getBoundingBox());  
                                colObj = obj;
                            }
                        }
                    }
                }
            }    
        }
          
        return colObj;
    }
    
    //Essa função encontra a primeira colisão que ocorre no eixo X e bloqueia o movimento.
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
        Vector2 intersection = map.getVerticalIntersection(box);

        GameObject colObj;
        
        colObj = findClosestDynamicX(entity,direction,distance);
        if(colObj != null)
        {
            //Se o valor de colObj não é null, uma colisão foi encontrada,
            //Então a distancia maxima que o objeto pode andar é então ajustada.
            tileDistance = min((int)ceil(box.getHorizontalDistance(colObj.getBoundingBox())/32),tileDistance);
        }   
        
        //Depois de verificar as colisões dinamicas, 
        //são verificadas as colisões estaticas, em busca de um tile ainda mais
        //proximo do personagem que o objeto dinamico previamente encontrado.
           
        if(direction > 0)
        {
            //Encontra o tile onde a direita do personagem se encontra
            xPos = (int)(map.findTile((int)box.getRight(),(int)box.y).x);
        }
        else
        {
            //Encontra o tile onde a esquerda do personagem se encontra
            xPos = (int)(map.findTile((int)box.getLeft(),(int)box.y).x);
        }
        
        int delta = 0;
        while(xPos >= 0 && xPos < map.getWidth())
        {
            //Percorre todos os tiles na direção do movimento,
            //e verifica se algum colide com o personagem
            //*Note que a distancia foi atualizada para levar em conta
            //a colisão com objetos dinamicos.
            
            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(i < 0 || i > map.getWidth())
                {
                    return;
                }
                
                if(map.getTile(xPos,i).getCollides())
                {    
                    if(map.getTile(xPos,i).getBlocks())
                    {
                        colObj = map.getTile(xPos,i);
                        break;
                    }
                }
            }

            delta += 1;
            //A posição a ser verificada é incrementada no sentido do movimento.
            xPos += (delta * direction);
            
            if(delta > tileDistance)
            {
                break;
            }
        }
        
        if(colObj == null)
        {
            //Se colObj ainda é null ao final da verificação, nada bloqueia o
            //caminho do personagem.
            horizontalCollision(entity,direction,distance);
        }
        else
        {
            horizontalCollision(entity,direction,box.getHorizontalDistance(colObj.getBoundingBox()));
        }
    }
    
    //Depois que a primeira colisão que bloqueia é encontrada,
    //e a distancia a ser percorrida é atualizada, todos os objetos
    //dentro dessa distancia e no caminho do personagem executam o codigo de colisão.
    private void horizontalCollision(DynamicCollider entity,int direction,float distance)
    {
        
        //A distancia a ser percorrida, em tiles.
        int tileDistance = (int)ceil(distance/32);
        int xPos;
        BoundingBox box = entity.getBoundingBox(); 
        Vector2 intersection = map.getVerticalIntersection(box);
 
        
        //Percorre todos os objetos dinamicos e executa a colisão se o objeto  
        //em questão está no caminho do personagem e dentro da distancia de 
        //movimento.
        for(DynamicCollider obj : nearby)
        {
            if(map.checkIntersection(intersection,map.getVerticalIntersection(obj.getBoundingBox())))
            {
                if(box.getHorizontalDistance(obj.getBoundingBox()) <= distance)
                {
                    if((obj.getBoundingBox().getX() < entity.getBoundingBox().getX() && direction < 0) ||(obj.getBoundingBox().getX() > entity.getBoundingBox().getX() && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,HORIZONTAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,HORIZONTAL_AXIS));
                    }
                }
            }    
        }   
        
        if(direction > 0)
        {
            xPos = (int)(map.findTile((int)box.getRight(),(int)box.y).x);
        }
        else
        {
            xPos = (int)(map.findTile((int)box.getLeft(),(int)box.y).x);
        }
        
        //Percorre todos os tiles dentro da distancia de movimento e executa a
        //colisão com esses.
        int delta = 0;
        while(xPos >= 0 && xPos < map.getWidth())
        {
            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                AbstractTile currentTile = map.getTile(xPos,i);
                if(currentTile.getCollides())
                {    
                    entity.collide(currentTile,new CollisionInfo(entity,HORIZONTAL_AXIS));
                    currentTile.collide(entity,new CollisionInfo(currentTile,HORIZONTAL_AXIS));

                    if(currentTile.getBlocks())
                    {
                        return;
                    }
                }
            }

            delta += 1;  
            //A posição a ser verificada é incrementada no sentido do movimento.
            xPos += (delta * direction);
            
            if(delta > tileDistance + 1)
            {
                break;
            }
        }
    }
    
    //Essa função é analoga à closestCollisionX, só que para colisões verticais.
    public void closestCollisionY(DynamicCollider entity,int direction,float distance)
    {
        //Se a direção é zero, o objeto não vai se mover nesse eixo
        //logo, não é necessario verificar colisões*
        //*isso só ocorre porque as colisões são sempre bilaterais,
        //Se um outro objeto se move em direção a esse, 
        //a colisão ainda vai ocorrer.
        if(abs(direction) == 0)
        {
            return;
        }
        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = map.getHorizontalIntersection(box);
        GameObject colObj;
        
        colObj = findClosestDynamicY(entity,direction,distance);
        if(colObj != null)
        {
            //Se o valor de colObj não é null, uma colisão foi encontrada,
            //Então a distancia maxima que o objeto pode andar é então ajustada.
            tileDistance = min((int)ceil(box.getHorizontalDistance(colObj.getBoundingBox())/32),tileDistance);
        }   
        if(direction > 0)
        {
            yPos = (int)(map.findTile((int)box.x,(int)box.getUp()).y);
        }
        else
        {
            yPos = (int)(map.findTile((int)box.x,(int)box.getDown()).y);
        }
        
        //Depois de verificar as colisões dinamicas, 
        //são verificadas as colisões estaticas, em busca de um tile ainda mais
        //proximo do personagem que o objeto dinamico previamente encontrado.
        while(yPos >= 0 && yPos < map.getHeight())
        {

            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(i < 0 || i > map.getHeight())
                {
                    return;
                }
                
                if(map.getTile(i,yPos).getCollides())
                {    
                    if(map.getTile(i,yPos).getBlocks())
                    {
                        colObj = map.getTile(i,yPos);
                        break;
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
    
    //Depois que a primeira colisão que bloqueia é encontrada,
    //e a distancia a ser percorrida é atualizada, todos os objetos
    //dentro dessa distancia e no caminho do personagem executam o codigo de colisão.
    private void verticalCollision(DynamicCollider entity,int direction,float distance)
    {
 
        if(direction == 0)
        {
            return;
        }

        int tileDistance = (int)ceil(distance/32);
        int yPos;
        BoundingBox box = entity.getBoundingBox();
        int delta = 0;
        Vector2 intersection = map.getHorizontalIntersection(box);

        //Percorre todos os objetos dinamicos e executa a colisão se o objeto  
        //em questão está no caminho do personagem e dentro da distancia de 
        //movimento.
        for(DynamicCollider obj : nearby)
        {
            if(map.checkIntersection(intersection,map.getHorizontalIntersection(obj.getBoundingBox())))
            {
                if(box.getVerticalDistance(obj.getBoundingBox()) <= distance)
                {
                    if((obj.getBoundingBox().getY() < entity.getBoundingBox().getY() && direction < 0) ||(obj.getBoundingBox().getY() > entity.getBoundingBox().getY() && direction > 0))
                    {     
                        entity.collide(obj,new CollisionInfo(entity,VERTICAL_AXIS));
                        obj.collide(entity,new CollisionInfo(obj,VERTICAL_AXIS));
                    }
                    
                }
            }    
        }   
        
        if(direction > 0)
        {
            yPos = (int)(map.findTile((int)box.x,(int)box.getUp()).y);
        }
        else
        {
            yPos = (int)(map.findTile((int)box.x,(int)box.getDown()).y);
        }
        
        //Percorre todos os tiles dentro da distancia de movimento e executa a
        //colisão com esses.
        while(yPos >= 0 && yPos < map.getHeight())
        {
            for(int i = (int)intersection.x; i <= (int)intersection.y; i++)
            {
                if(map.getTile(i,yPos).getCollides())
                {    
                    entity.collide(map.getTile(i,yPos),new CollisionInfo(entity,VERTICAL_AXIS));
                    map.getTile(i,yPos).collide(entity,new CollisionInfo(map.getTile(i,yPos),VERTICAL_AXIS));
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