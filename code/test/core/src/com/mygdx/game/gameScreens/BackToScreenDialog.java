
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BackToScreenDialog extends Dialog 
{
    //Dialogo que transporta o usuário à uma dada tela.
    TextButton confirmButton;
    TextButton cancelButton;
    GameScreen screen;
    Game game;
            
    public BackToScreenDialog(Skin skin,String text,Game game,GameScreen screen) {
        super("Exit", skin);
        
        this.game = game;
        this.screen = screen;
        confirmButton = new TextButton("Ok",skin);
        cancelButton = new TextButton("Cancel",skin);
        
        text(text);
        button(confirmButton,1L);
        button(cancelButton,2L);
    }
    
    @Override
    protected void result(Object object)
    {
        if(object.equals(1L))
        {
            game.getScreen().dispose();
            game.setScreen(screen);
            screen.initialize();
        }
        else
        {
            this.setVisible(false);
        }
    }
    
}
