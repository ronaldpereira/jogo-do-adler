/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.mygdx.game.Tiles.Tile;
import com.mygdx.game.Tiles.Repulsor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static com.mygdx.game.Axis.VERTICAL_AXIS;
import com.mygdx.game.Tiles.AbstractTile;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

/**
 *
 * @author Hiago
 */
public class ControllableCharacter extends DynamicCollider
{    
    public ControllableCharacter(Vector2 initialPos, Texture sprite) {
        super(initialPos, sprite);
        blocksMovement = false;
        //TROCAR POR SKIIIIN
        getBoundingBox().width -=1f;
        getBoundingBox().height -=1f;
    }
    
    //Recebe input e determina aceleração
    //com base nela
    private void handleInput()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            //Quando o jogador está indo em uma direção e muda para outra,
            //ele ganha um ganho em aceleração de até que ele conclua a
            //mudança de direção.
            if(acceleration.x > 0)
            {
                acceleration.x = -(XACCEL + XACCEL*REACTION);
            }
            else
            {
                acceleration.x = -XACCEL;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(acceleration.x < 0)
            {
                acceleration.x = (XACCEL + XACCEL*REACTION);
            }
            else
            {
                acceleration.x = XACCEL;
            }
        }
        else
        {
            //Se nenhuma tecla é a apertada, a aceleração é nula
            //e a velocidade decresce gradualmente com o atrito
            acceleration.x = 0;
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            //O personagem só pode pular se ele está no chão
            ////TODO: Double jump e pulo mais alto segurando a tecla
            if(grounded)
            {
                currentSpeed.y = 5f;
                grounded = false;
            }
        }
    }
    
    @Override
    public void updateMovement()
    {
        handleInput();
    }
        
    @Override
    public void handleCollision(Enemy enemy,CollisionInfo info)
    {
        if(getBoundingBox().overlaps(enemy.getBoundingBox()))
        {
            //Dependendo do eixo da colisão, o jogador
            //leva um knockback nesse eixo
            ////TODO:Criar uma variavel de knockback,
            ////possivelmente diferente para cada inimigo;
            
            if(info.getAxis() == HORIZONTAL_AXIS)
            {
                
                setXSpeed(-signum(enemy.getBoundingBox().x - getBoundingBox().x) * 5);
            }
            else
            {
                setYSpeed(-signum(enemy.getBoundingBox().y - getBoundingBox().y) * 5);
            }
        }
    }
        
    @Override
    public void handleCollision(AbstractTile tile,CollisionInfo info)
    {
        //A velocidade final após a colisão com um determinado tile
        //é o mínimo entre a velocidade do jogador e a distancia com o tile
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            if(tile.getBlocks())
            {
                int distance = (int)getBoundingBox().getHorizontalDistance(tile.getBoundingBox());
                currentSpeed.x = min(distance,abs(currentSpeed.x)) * signum(currentSpeed.x);             
            }
        }
        else
        {
            System.out.println(tile.getBlocks());
            if(tile.getBlocks())
            {
                
                int direction = (int)signum(currentSpeed.y);
                int distance = (int)getBoundingBox().getVerticalDistance(tile.getBoundingBox());
                currentSpeed.y = min(distance,abs(currentSpeed.y)) * direction;

                if(direction < 0 && distance == 0)
                {
                    //Nesse caso, o jogador está no chão e pode pular.
                    grounded = true;
                }
            }
            
        }
    }
    
    @Override
    public void handleCollision(Tile tile,CollisionInfo info)
    {
        //O atrito, e a velocidade variam com o tile que
        //o personagem está.
        ////TODO:Colocar a altura de pulo aqui também;
        handleCollision((AbstractTile)tile,info);
        if(info.getAxis() == VERTICAL_AXIS && tile.getBlocks())
        {
            if(grounded == true)
                {
                    //O atrito, e a velocidade variam com o tile que
                    //o personagem está.
                    ////TODO:Colocar a altura de pulo aqui também;
                    attrition = tile.getAttrition();
                    maximumSpeed.x = tile.getMaxSpeed();
                }
        }
    }
    
    @Override
    public void handleCollision(Repulsor repulsor,CollisionInfo info)
    {
        //O efeito de repulsão do repulsor só funciona sob certas condiçoes:
        //O jogador deve estar em colisão com o repulsor, ou seja,
        //deve existir interceção entre os boundingBoxes.
        //O jogador deve estar se movendo em sentido contrário à repulsão
        //Exemplo: Um repulsor que empurra o jogador para cima, só funciona se o
        //o jogador tem velocidade <= 0 quando entra em contato com esse.
        Vector2 repulsion = (repulsor.getRepulsion());
        if(info.getAxis() == repulsor.getAxis())
        {    
            if(info.getAxis() == HORIZONTAL_AXIS && signum(currentSpeed.x) != signum(repulsion.x))
            {
                if(getBoundingBox().overlaps(repulsor.getBoundingBox()))
                {
                    setXSpeed(repulsion.x);
                }
            }
            else if(info.getAxis() == VERTICAL_AXIS && signum(currentSpeed.y) != signum(repulsion.y))
            {   
                if(getBoundingBox().overlaps(repulsor.getBoundingBox()))
                {
                    setYSpeed(repulsion.y);
                }
            }   
            else if(!getBoundingBox().overlaps(repulsor.getBoundingBox()))
            {
                handleCollision((AbstractTile)repulsor,info);
            }
        }
        else
        {
            if(!getBoundingBox().overlaps(repulsor.getBoundingBox()))
            {
                handleCollision((AbstractTile)repulsor,info);
            }
        }
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }
}
