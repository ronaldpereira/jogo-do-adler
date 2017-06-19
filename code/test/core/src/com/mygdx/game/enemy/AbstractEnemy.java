
package com.mygdx.game.enemy;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.collidable.DynamicCollider;
import com.mygdx.game.IBuildableObject;
import com.mygdx.game.TextureSheet;
import com.mygdx.game.IDamageable;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.map.TileMap;

public abstract class AbstractEnemy extends DynamicCollider implements IDamageable, IBuildableObject
{
    //Todos os inimigos devem herdar dessa classe.
    
    private static final float SKIN = 1f;
    protected float xOrigin;
    
    //Direção atual do inimigo, pode ser 1,0 ou -1.
    //Futuramente pode ser trocado por enum.
    protected int direction = 1;    
    protected int life = 1;
    
    public void setOrigin()
    {
        //Determina a extremidade do inimigo daonde o movimento inicia, 
        //e aplica um pequeno ajuste(skin) para concertar o calculo posicional.
        if(direction < 0)
        {
            xOrigin = getBoundingBox().getRight() - SKIN;
        }
        else
        {
            xOrigin = getBoundingBox().getLeft() + SKIN;
        }
    }
    
    public AbstractEnemy(Vector2 initialPos, TextureSheet sheet, Boolean animates) {
        super(initialPos, sheet,animates);
        //Inimigos normalmente não aceleram, então o atrito é 0 por default.
        attrition = 0;
    }
    
    @Override
    public void takeDamage(int damage)
    {
        this.life -= damage;
        if(life <= 0)
        {
            die();
        }
    }
    
    @Override
    public void die()
    {
        //Desativa o objeto e remove ele dos calculos do mapa.
        setActive(false);
        map.removeMovingObject(this);
    }
    
    @Override
    public abstract AbstractEnemy generateFrom();
    
    @Override
    public void build(CollisionMap map,int x,int y)
    {
        //Constroi uma copia desse objeto no mapa, usando o metodo generateFrom
        //para copiar.
        AbstractEnemy objectToBuild = this.generateFrom();
        objectToBuild.getBoundingBox().setPosition(x * TileMap.getTileSize(),y * TileMap.getTileSize());
        map.createTile(x, y, objectToBuild);
    }
    
}