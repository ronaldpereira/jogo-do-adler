
package com.mygdx.game.player;

import com.mygdx.game.state.PlayerStateMachine;
import com.mygdx.game.collidable.DynamicCollider;
import com.mygdx.game.collidable.ICollidable;
import com.mygdx.game.enemy.AbstractEnemy;
import com.mygdx.game.tile.Tile;
import com.mygdx.game.tile.Repulsor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.game.CollisionInfo;
import com.mygdx.game.IDamageable;
import com.mygdx.game.TextureSheet;
import static com.mygdx.game.Axis.HORIZONTAL_AXIS;
import static com.mygdx.game.Axis.VERTICAL_AXIS;
import com.mygdx.game.gameScreens.StageScreen;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelNode;
import com.mygdx.game.map.CollisionMap;
import com.mygdx.game.tile.AbstractTile;
import com.mygdx.game.tile.Hazard;
import com.mygdx.game.tile.LevelEndingTile;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

public class ControllableCharacter extends DynamicCollider implements IDamageable
{    
    //Essa classe representa um personagem que pode ser controlado pelo jogador
    private LevelNode levelNode;

    protected int maxLife = 3;
    protected int life = maxLife;
    public int coins = 0;
    private boolean isInvencible = false;
    private Timer timer; 
    private float invTime = 0.75f;
    private float respawnTime = 2f;
    private PlayerStateMachine machine;
    private Level currentlevel;
    
    public ControllableCharacter(TextureSheet sheet,Boolean animates,Level currentLevel) 
    {
        //A posição inicial do personagem é determinada quando ele entra em um
        //levelNode.
        super(new Vector2(0,0), sheet,animates);
        this.currentlevel = currentLevel;
        blocksMovement = false;
        setCollides(true);
        timer = new Timer();
        machine = new PlayerStateMachine(this);       
    }
    
    //Recebe input e determina aceleração
    //com base nela
    private void handleInput()
    {
        machine.handleInput();
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            //Quando o jogador está indo em uma direção e muda para outra,
            //ele ganha um impulso até que ele conclua a
            //mudança de direção.
            if(currentAccel.x > 0)
            {
                currentAccel.x = -(xAccel + xAccel*reaction);
            }
            else
            {
                currentAccel.x = -xAccel;
            }
            flipImageDirection = true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            if(currentAccel.x < 0)
            {
                currentAccel.x = (xAccel + xAccel*reaction);
            }
            else
            {
                currentAccel.x = xAccel;
            }
            flipImageDirection = false;
        }
        else
        {
            //Se nenhuma tecla é a apertada, a aceleração é nula
            //e a velocidade decresce gradualmente com o atrito
 
            currentAccel.x = 0;

        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            //O personagem só pode pular se ele está no chão
            ////TODO: Double jump e pulo mais alto segurando a tecla
            if(grounded)
            {
                currentSpeed.y = 4.2f;
                grounded = false;
            }
        }
    }
    
    private void triggerIFrames()
    {
        isInvencible = true;
        timer.scheduleTask( new Task()
        {
            @Override 
            public void run()
            {
                isInvencible = false;
            }
        }, invTime);
    }
    
    @Override
    public void updateMovement()
    {
        handleInput();
    }
    
    @Override 
    public void update()
    {
        machine.update();
        super.update();
        if(getBoundingBox().getUp()< 0) 
        {
            takeDamage(maxLife);
        }
    }

    @Override
    public void handleCollision(AbstractEnemy enemy,CollisionInfo info)
    {
        //Colisão com inimigos em geral.
 
        if(getBoundingBox().overlaps(enemy.getBoundingBox()))
        {
            if(getBoundingBox().getY() > enemy.getBoundingBox().getY() + enemy.getBoundingBox().height/2 && currentSpeed.y < 0)
            {
                enemy.die();
                setYSpeed(-signum(enemy.getBoundingBox().getY() - getBoundingBox().getY()) * 5);
                return;
            }   
                
            //Dependendo do eixo da colisão, o jogador
            //leva um knockback nesse eixo
            ////TODO:Criar uma variavel de knockback,
            ////possivelmente diferente para cada inimigo;
            
            if(!isInvencible)
            {
                //Se o objeto não está ivulneravel, ele toma dano e knockback.
                triggerIFrames();
                //Atualmente o jogador sempre toma 1 de dano quando encosta no
                //inimigo, mas uma variavel de dano pode ser criada para inimigos
                //diferentes no futuro.
                this.takeDamage(1);
                setXSpeed(-signum((int)(enemy.getBoundingBox().getX() - getBoundingBox().getX())) * 7.5f);
                setYSpeed(-signum((int)(enemy.getBoundingBox().getY() - getBoundingBox().getY())) * 5);
            }
        }
    }
        
    @Override
    public void handleCollision(AbstractTile tile,CollisionInfo info)
    {
        //A velocidade final após a colisão com um determinado tile
        //é o mínimo entre a velocidade do jogador e a distancia com o tile
        if(tile.getBlocks())
        {
            int direction = (int)signum(currentSpeed.y);
            int distance = (int)getBoundingBox().getVerticalDistance(tile.getBoundingBox());
            
            collideStopMovement(tile.getBoundingBox(),info.getAxis());
            
            if(info.getAxis() == VERTICAL_AXIS)
            {
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
        handleCollision((AbstractTile)tile,info);
        if(info.getAxis() == VERTICAL_AXIS && tile.getBlocks())
        {
            if(grounded == true && getBoundingBox().getVerticalDistance(tile.getBoundingBox()) <= 1f)
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
    public void handleCollision(LevelEndingTile end,CollisionInfo info)
    {
        if(getBoundingBox().overlaps(end.getBoundingBox()))
        {
            //Muda o nível na stageScreen.
            StageScreen.nextLevel();
        }
    }
    
    @Override
    public void setMap(CollisionMap map)
    {
        this.map = map;
        this.SpawnPoint = map.getSpawnPoint();
        getBoundingBox().setPosition(SpawnPoint);
        setSpeed(0,0);
    }
    
    @Override
    public void handleCollision(Repulsor repulsor,CollisionInfo info)
    {
        //O efeito de repulsão do repulsor só funciona sob certas condiçoes:
        //O jogador deve estar em colisão com o repulsor, ou seja,
        //deve existir interceção entre os boundingBoxes.
        //O jogador deve estar se movendo em sentido contrário à repulsão
        //Exemplo: Um repulsor que empurra o jogador para cima, só funciona se o
        //o jogador tem velocidade <= 0 quando entra em colisão vertical com esse.
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
    public void handleCollision(Hazard hazard, CollisionInfo info)
    {
        if(info.getAxis() == HORIZONTAL_AXIS)
        {
            int distance = (int)getBoundingBox().getHorizontalDistance(hazard.getBoundingBox());
            distance = (int) min(distance,abs(currentSpeed.x));
            
            if(distance >= (int)getBoundingBox().getHorizontalDistance(hazard.getDangerArea()))
            {
                takeDamage(maxLife);
            }
        }
        
        else
        {
            int distance = (int)getBoundingBox().getVerticalDistance(hazard.getBoundingBox());
            distance = (int) min(distance,abs(currentSpeed.y));
            
            if(distance >= (int)getBoundingBox().getVerticalDistance(hazard.getDangerArea()))
            {
                takeDamage(maxLife);
            }
        }
        handleCollision((AbstractTile)hazard,info);
    }
    
    @Override
    public void collide(ICollidable obj,CollisionInfo info)
    {
        obj.handleCollision(this,info);
    }

    @Override
    public void takeDamage(int damage) 
    {
        life -= damage;
        if(life <= 0)
        {
            die();
        }
    }
    
    public void respawnTimer()
    {
        //Quando o personagem morre, um timer é ativado.
        final ControllableCharacter thisC = this;
        timer.scheduleTask(new Task()
        {
            @Override 
            public void run()
            {   
                //Código de respawn.
                setActive(true);
                life = maxLife;
                //Os inimigos são recarregados.
                levelNode.reloadObjects();
                //O jogador é readicionado ao nível.
                levelNode.getMap().addPlayer(thisC);
                setSpeed(0,0);
                grounded = false;
                //Reseta a posição do jogador.
                getBoundingBox().setPosition(SpawnPoint);
                
            }
        }, respawnTime);
    }
    
    @Override
    public void die() 
    {
        if(getActive())
        {
            setActive(false);
            respawnTimer();
        }
    }
    
    public int getLife()
    {
        return this.life;
    }
    
    public void addScore(int value)
    {
        coins += value;
    }
    
    public int getScore()
    {
        return this.coins;
    }
        
    public void setLevelNode(LevelNode levelNode)
    {
        this.levelNode = levelNode;
    }
    
}
