/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.core;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.gruppe7.common.graphics.DrawCommand;
import dk.gruppe7.common.graphics.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.openide.util.Exceptions;

/**
 *
 * @author pc4
 */
public class LibGDXGraphicsInterpreter
{

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    
    private HashMap<Integer, Texture> cachedTextures = new HashMap<>();
    private InputStream fallbackTextureInputStream; 
    
    public LibGDXGraphicsInterpreter()
    {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font  = new BitmapFont();
        fallbackTextureInputStream = getClass().getResourceAsStream("texture.png");
    }
    
    public void render(Graphics graphics)
    {
         while(graphics.getDrawCommands().size() > 0){
            DrawCommand cmd = graphics.getDrawCommands().poll();
            switch(cmd.getType()){
                case SPRITE:
                    batch.begin();
                    
                    //Texture tex = inputStreamToTexture(cmd.getInputStream());
                    Texture tex = (cmd.getInputStream() != null) ? inputStreamToTexture(cmd.getInputStream()) : inputStreamToTexture(fallbackTextureInputStream);
                    
                    float repeatX = 1;
                    float repeatY = 1;
                    if (cmd.getSpriteRenderType() == DrawCommand.SpriteRenderMode.REPEAT)
                    {
                        tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                        repeatX = cmd.getSize().x /tex.getWidth();
                        repeatY = cmd.getSize().y/  tex.getHeight(); 
                        //LibGDX will always scale the texture to the bounds,
                        //Forcing this approach where amount of wraps are,
                        //calculated manually
                    }
                    
                    Sprite s = new Sprite(tex);
                    s.setPosition(cmd.getPosition().x, cmd.getPosition().y);
                    s.setRotation(cmd.getRotation());
                    s.setSize(cmd.getSize().x, cmd.getSize().y);
                    s.setOriginCenter();
                    if(cmd.getSpriteRenderType() == DrawCommand.SpriteRenderMode.REPEAT){
                        s.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                        s.setRegionWidth((int)(tex.getWidth()*repeatX));                        
                        s.setRegionHeight((int)(tex.getHeight()*repeatY));                        
                    }
                    
                    s.draw(batch);
                    batch.end();
                    break;
                    
                case STRING: 
                    batch.begin();

                    font.setColor(cmd.getColor().r, cmd.getColor().g, cmd.getColor().b, cmd.getColor().a);
                    font.draw(batch, cmd.getString(), cmd.getPosition().x, cmd.getPosition().y);

                    batch.end();
                    break;
                case RECTANGLE:
                    shapeRenderer.begin(cmd.isFilled()?ShapeRenderer.ShapeType.Filled:ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(cmd.getColor().r, cmd.getColor().g, cmd.getColor().b, cmd.getColor().a);
                    shapeRenderer.rect(cmd.getPosition().x, cmd.getPosition().y, cmd.getSize().x, cmd.getSize().y);
                    shapeRenderer.end();
            }
        }
    }
    
    
    private Texture inputStreamToTexture(InputStream inputStream) {
        if(cachedTextures.containsKey(inputStream.hashCode()))
            return cachedTextures.get(inputStream.hashCode());
    
        Gdx2DPixmap gpm = null;
        
        try {
            gpm = new Gdx2DPixmap(inputStream, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        Pixmap pixmap = new Pixmap(gpm);
        Texture texture = new Texture(pixmap);
        
        cachedTextures.put(inputStream.hashCode(), texture);
        return texture;
    }
   
}
