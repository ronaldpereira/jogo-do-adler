
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;
import static java.lang.Math.signum;

public class PlayerDirChangeState extends AnimationState
{
    //Estado do jogador quando ele está mudando de direção.
  private PlayerDirChangeState()
    {
        super(null,null);  
        animIndex = 3;   
    }
    public PlayerDirChangeState(PlayerStateMachine machine,ControllableCharacter player)
    {
        super(machine,player);
        animIndex = 3;
        
    }
    
    @Override
    public void update()
    {
        if(player.getCurrentSpeed().y != 0)
        {
            machine.setState(new PlayerJumpState(machine,player));
        }
        else if(signum(player.getCurrentSpeed().x) == signum(player.getAccel().x))
        {
            machine.setState(new PlayerMovingState(machine,player));
        }
    }  
}
