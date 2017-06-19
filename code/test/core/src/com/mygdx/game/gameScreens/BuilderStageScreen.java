
package com.mygdx.game.gameScreens;

import com.badlogic.gdx.Game;
import com.mygdx.game.player.ControllableCharacter;
import com.mygdx.game.level.Level;

public class BuilderStageScreen extends StageScreen 
{
    //Essa classe representa a tela de um nível que está sendo jogada através do
    //map builder.
    public BuilderStageScreen(ControllableCharacter p,Level l,Game game)
    {
        super(p,l,game);
    }
    
    //Nessa classe, pressionar esc invoca um dialogo de voltar para o map builder,
    //e não para o menu principal.
    @Override 
    protected void defineBackButton()
    {
        back = new BackToScreenDialog(uiSkin,"Return to map builder?",game,new BuilderScreen(game,level.getFolder()));
        back.setVisible(false);
    }
}
