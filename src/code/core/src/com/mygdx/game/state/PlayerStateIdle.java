
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;

public class PlayerStateIdle extends AnimationState
{
    //Estado do jogador quando ele está parado.
    private PlayerStateIdle()
    {
        super(null,null);  
        animIndex = 0;   
    }
    public PlayerStateIdle(PlayerStateMachine machine,ControllableCharacter player)
    {
        super(machine,player);
        animIndex = 0;
        
    }
    
    @Override
    public void update()
    {
        if(player.getCurrentSpeed().y != 0)
        {
            machine.setState(new PlayerJumpState(machine,player));
        }
        else if(player.getCurrentSpeed().x != 0)
        {
            machine.setState(new PlayerMovingState(machine,player));
        }
    }
}
