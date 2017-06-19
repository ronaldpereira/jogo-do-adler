
package com.mygdx.game.state;

import com.mygdx.game.player.ControllableCharacter;


public class EmptyPlayerState implements IPlayerState
{
    //Estado vazio do jogador.
    PlayerStateMachine machine;
    protected ControllableCharacter player;
    public EmptyPlayerState(PlayerStateMachine machine,ControllableCharacter player)
    {
        this.machine = machine;
        this.player = player;
    }
    @Override
    public void update()
    {}
    @Override
    public void handleInput()
    {}
    @Override
    public void onEnter()
    {}
    @Override
    public void onExit()
    {}
}
