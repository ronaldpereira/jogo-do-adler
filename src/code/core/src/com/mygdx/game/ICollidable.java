/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author Hiago
 */
public interface ICollidable
{
    public void handleCollision(ICollidable obj,CollisionInfo info);
    public void handleCollision(Tile tile,CollisionInfo info);
    public void handleCollision(DynamicCollider character,CollisionInfo info);
    public void collide(ICollidable obj,CollisionInfo info);
    public void handleCollision(ControllableCharacter player,CollisionInfo info);
    public void handleCollision(Enemy enemy,CollisionInfo info);
    public void handleCollision(Repulsor repulsor,CollisionInfo info);
}
