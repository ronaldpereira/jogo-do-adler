
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureSheet 
{
    //Esse classe representa uma imagem com varias texturas.
    //Ela possui diferentes operações para facilitar animações e autotiling.
    private Texture originTexture;
    private int imgWidth;
    private int imgHeight;
   
    private TextureRegion[][] images;
    
    //Construtor vazio para serialização:
    private TextureSheet()
    {
        
    }
    public TextureSheet(Texture texture,int imgWidth,int imgHeight)
    {
        setTexture(texture,imgWidth,imgHeight);
    }
    
    public Animation[] toAnimation()
    {
        //Cada linha da textureSheet corresponde a uma animação distinta
        int numLines = images.length;
        Animation<TextureRegion>[] animations = new Animation[numLines];
        for(int i = 0;i < numLines; i++)
        {
            animations[i] = new Animation<TextureRegion>(0.12f,images[i]);
        }
        return animations;
    }
    
    public TextureRegion[] getLine(int index)
    {
        return images[index];
    }
    
    public TextureRegion getFrame(int index) 
    {
        //Retorna uma frame da animação default(0).
        return images[0][index];
    }
   
    
    public void setTexture(Texture texture,int imgWidth,int imgHeight)
    {
        //Seta a textura e corta ela em várias regiões de tamanho imgWidth x imgHeight
        originTexture = texture;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        
        TextureRegion temp = new TextureRegion(texture);
        images = temp.split(imgWidth, imgHeight);
    }
    
    public Texture getTexture()
    {
        return originTexture;
    }
}
