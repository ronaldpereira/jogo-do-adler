
package com.mygdx.game;

public interface IDamageable extends IDestroyable
{
    //Interface dos objetos que podem ser danificados(e consequentemente, destruidos).
    public void takeDamage(int damage);
}
