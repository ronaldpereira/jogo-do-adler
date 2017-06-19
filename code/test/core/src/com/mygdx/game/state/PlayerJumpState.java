
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.player.ControllableCharacter;

public class PlayerJumpState extends AnimationState
{
    //Estado do jogador quando ele está no ar.
    public PlayerJumpState(PlayerStateMachine machine,ControllableCharacter player)
    {
        super(machine,player);
        animIndex = 2;
    }
    
    public void update()
    {
        if(player.getCurrentSpeed().y == 0)
        {
            if(player.getCurrentSpeed().x != 0)
            {
                machine.setState(new PlayerMovingState(machine,player));
            }
            else if(player.isGrounded())
            {
                machine.setState(new PlayerStateIdle(machine,player));
            }
        }
    }
}
