
package com.mygdx.game.mapBuilder;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class ConfirmableTextField extends Window 
{
    //Um campo de texto com botão de confirmação e botão de saída.
    private Label fieldLabel;
    private TextButton confirmButton;
    private TextButton cancelButton;
    private TextField field;
    protected String fieldText;
    
    public ConfirmableTextField(String name, String label,Skin uiSkin)
    {
        super(name,uiSkin);
        fieldLabel = new Label(label,uiSkin);
        field = new TextField("",uiSkin);
        confirmButton = new TextButton("Ok",uiSkin);
        
        confirmButton.addListener(new ClickListener()
        {
            //Botão de confirmação.
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    fieldText = field.getText();
                    if(!fieldText.equals(""))
                    {
                        //Se o texto não for vazio, executa o código de confirmação.
                        onConfirm();
                        //Esconde essa tela.
                        setVisible(false);
                        //Reseta a imagem.
                        field.setText("");
                        //Lança um evento de mudança. 
                        fire(new ChangeEvent());
                    }
                }
            }
        );
        cancelButton = new TextButton("Cancel",uiSkin);
        cancelButton.addListener(new ClickListener()
        {
            //Botão de saída
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    //Esconde a tela
                    setVisible(false);
                }
            }
        );
        
        this.add(fieldLabel);
        this.add(field).expand();
        this.row();
        this.add(confirmButton);
        this.add(cancelButton);
       
    }
    
    protected abstract void onConfirm();
}
