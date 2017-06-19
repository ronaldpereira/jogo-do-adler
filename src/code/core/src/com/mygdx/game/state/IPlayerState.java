
package com.mygdx.game.state;

public interface IPlayerState extends IState
{    
    //Estados do jogador podem ser influenciados pela input.
    void handleInput();
}
