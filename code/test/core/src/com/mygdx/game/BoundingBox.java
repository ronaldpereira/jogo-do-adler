
package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundingBox extends Rectangle
{   
    //Essa classe representa o boundingBox de um personagem,
    //ou seja, um retangulo que delimita seu espaço no mundo.
    
    //Construtor vazio para serialização.
    private BoundingBox()
    {
        super();
    }
    
    public BoundingBox(float x,float y,float width,float height)
    {
       super();
       this.x = x;
       this.y = y;
       this.width = width;
       this.height = height;
    }
    
    public BoundingBox(BoundingBox box)
    {
       //Copy constructor.
       super();
       this.x = box.x;
       this.y = box.y;
       this.width = box.width;
       this.height = box.height;
    }
   
    public void setPositionRelative(BoundingBox origin)
    {
        //Seta a posição dessa boundingBox em relação à uma outra.
        this.x += origin.x;
        this.y += origin.y;
    }
    public void translate(float xDir,float yDir)
    { 
        //Aplica uma translação de (xDir,yDir) no objeto.
        x += xDir;
        y += yDir;
    }
    
    //Econtra a distancia entre as extremidadades de duas boundingBoxes.
    public float getHorizontalDistance(BoundingBox b)
    {
        //Quando um objeto está em interceção com outro,a a distancia é negativa:
        if(this.overlaps(b))
        {
            if(this.getRight() >= b.getCenter(Vector2.Zero).x)
            {
                return -(b.getRight() - this.getLeft());
            }
            else
            {
                return -(this.getRight() - b.getLeft());
            }
        }
        if(b.getLeft() >= this.getRight())
        {
            return b.getLeft() - this.getRight();
        }
        else
        {
            return this.getLeft() - b.getRight();
        }
    }
   
    //Econtra a distancia entre as extremidadades de duas boundingBoxes.   
    public float getVerticalDistance(BoundingBox b)
    {
        //Quando um objeto está em interceção com outro,a a distancia é negativa:
        if(this.overlaps(b))
        {
            if(this.getDown() > b.getCenter(Vector2.Zero).y)
            {
                return -(b.getUp() - this.getDown());
            }
            else
            {
                return -(this.getUp() - b.getDown());
            }
        }
        
        if(b.getDown() >= this.getUp())
        {
            return b.getDown() - this.getUp();
        }
        else
        {
            return this.getDown() - b.getUp();
        }
    }
    public float getLeft()
    {
        return x;
    }
    public float getRight()
    {
        return x + width;
    }
    public float getUp()
    {
        return y + height;
    }
    public float getDown()
    {
        return y;
    }
}
