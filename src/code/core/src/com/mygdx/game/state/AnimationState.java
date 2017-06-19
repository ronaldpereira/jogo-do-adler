
package com.mygdx.game.state;

import com.mygdx.game.state.EmptyPlayerState;
import com.mygdx.game.state.PlayerStateMachine;
import com.mygdx.game.player.ControllableCharacter;

public class AnimationState extends EmptyPlayerState
{
    //Estado que aplica uma mudança de animação.
    int animIndex = 0;
    public AnimationState(PlayerStateMachine machine,ControllableCharacter player)
    {
        super(machine,player);
    }
    
    @Override
    public void onEnter()
    {
        player.setCurrentAnimation(animIndex);
    }
}
