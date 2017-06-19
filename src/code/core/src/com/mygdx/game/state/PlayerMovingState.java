
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;
import static java.lang.Math.signum;

public class PlayerMovingState extends AnimationState
{
    //Estado do jogador quando ele se move no chão.
    public PlayerMovingState(PlayerStateMachine machine,ControllableCharacter player)
    {
        super(machine,player);
        animIndex = 1;
    } 
    
    @Override
    public void update()
    {
        if(player.getCurrentSpeed().y != 0)
        {
            machine.setState(new PlayerJumpState(machine,player));
        }
        else if(player.getCurrentSpeed().x == 0)
        {
            machine.setState(new PlayerStateIdle(machine,player));
        }
        else if(signum(player.getCurrentSpeed().x) != signum(player.getAccel().x))
        {
            machine.setState(new PlayerDirChangeState(machine,player));
        }
    }
}
