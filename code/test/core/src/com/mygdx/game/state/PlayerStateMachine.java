
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;

public class PlayerStateMachine 
{
    //Uma máquina de estados simplificada que não tem memória de seus estados.
    IPlayerState current;
    ControllableCharacter player;
    
    //Construtor vazio para serialização
    private PlayerStateMachine()
    {
        
    }
    
    public PlayerStateMachine(ControllableCharacter player)
    {
        this.player = player;
        current = new PlayerStateIdle(this,player);
    }
    
    public void handleInput()
    {
        current.handleInput();
    }
    
    public void update()
    {
        current.update();
    }
    
    public void setState(IPlayerState state)
    {
        current.onExit();
        current = state;
        current.onEnter();
    }
}
