
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Graphics {
    
    private PriorityQueue<DrawCommand> drawCommands = new PriorityQueue<DrawCommand>(new DrawCommandComparator());
      
    public void drawSprite(Vector2 position, Vector2 size, InputStream texture, float rotation){
        drawSprite(position, size, texture, rotation, 0, 0);
    }
    
    public void drawSprite(Vector2 position, Vector2 size, InputStream texture, float rotation, int zIndex){
        drawSprite(position, size, texture, rotation, zIndex, 0);
    }
    
    public void drawSprite(Vector2 position, Vector2 size, InputStream texture, float rotation, int zIndex, float yPos){
        DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setInputStream(texture);
        cmd.setRotation(rotation);
        cmd.setType(DrawCommand.DrawCommandType.SPRITE);
        cmd.setzIndex(zIndex);
        cmd.setyPos(yPos);
        drawCommands.add(cmd);
    }
    
    public void drawRepeatedSprite(Vector2 position, Vector2 size, InputStream texture, float rotation){
        drawRepeatedSprite(position, size, texture, rotation, 0);
    }
    
    public void drawRepeatedSprite(Vector2 position, Vector2 size, InputStream texture, float rotation, int zIndex)
    {
        DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setInputStream(texture);
        cmd.setRotation(rotation);
        cmd.setType(DrawCommand.DrawCommandType.SPRITE);
        cmd.setSpriteRenderType(DrawCommand.SpriteRenderMode.REPEAT);
        cmd.setzIndex(zIndex);
        drawCommands.add(cmd);
    }
    
    public void drawSpriteOffset(Vector2 position, Vector2 size, Vector2 offset, InputStream texture, float rotation){
        drawSpriteOffset(position, size, offset, texture, rotation, 0);
    } 
    
    public void drawSpriteOffset(Vector2 position, Vector2 size, Vector2 offset, InputStream texture, float rotation, int zIndex)
    {
       DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setInputStream(texture);
        cmd.setRotation(rotation);
        cmd.setType(DrawCommand.DrawCommandType.SPRITE);
        cmd.setOffset(offset);
        cmd.setzIndex(zIndex);
        drawCommands.add(cmd); 
    }
    
    public void drawString(Vector2 position, String string) {
        drawString(position, string, new Color(1,1,1), 1);
    }
    
    public void drawString(Vector2 position, String string, Color color, int zIndex) {
        DrawCommand cmd = new DrawCommand();
        cmd.setString(string);
        cmd.setPosition(position);
        cmd.setType(DrawCommand.DrawCommandType.STRING);
        cmd.setColor(color);
        cmd.setzIndex(zIndex);
        drawCommands.add(cmd);
    }
    
    public void drawRectangle(Vector2 position, Vector2 size, Color color){
        drawRectangle(position, size, color, true, 0, 0);
    }
    public void drawRectangle(Vector2 position, Vector2 size, Color color, boolean filled){
        drawRectangle(position, size, color, filled, 0, 0);
    }
    public void drawRectangle(Vector2 position, Vector2 size, Color color, boolean filled, int zIndex){
        drawRectangle(position, size, color, filled, zIndex, 0);
    }
    public void drawRectangle(Vector2 position, Vector2 size, Color color, boolean filled, int zIndex, float yPos){
        DrawCommand cmd = new DrawCommand();
        cmd.setType(DrawCommand.DrawCommandType.RECTANGLE);
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setColor(color);
        cmd.setFilled(filled);
        cmd.setzIndex(zIndex);
        cmd.setyPos(yPos);
        drawCommands.add(cmd);
    }
    
    public void drawCircle(Vector2 position, float radius, Color color){
        drawCircle(position, radius, color, true, 0, 0);
    }
    public void drawCircle(Vector2 position, float radius, Color color, boolean filled){
        drawCircle(position, radius, color, filled, 0, 0);
    }
    public void drawCircle(Vector2 position, float radius, Color color, boolean filled, int zIndex){
        drawCircle(position, radius, color, filled, zIndex, 0);
    }
    public void drawCircle(Vector2 position, float radius, Color color, boolean filled, int zIndex, float yPos){
        DrawCommand cmd = new DrawCommand();
        cmd.setType(DrawCommand.DrawCommandType.RECTANGLE);
        cmd.setPosition(position);
        cmd.setSize(new Vector2(radius, radius));
        cmd.setColor(color);
        cmd.setFilled(filled);
        cmd.setzIndex(zIndex);
        cmd.setyPos(yPos);
        drawCommands.add(cmd);
    }
    
    public PriorityQueue<DrawCommand> getDrawCommands(){
        return drawCommands;
    }
    
}

class DrawCommandComparator implements Comparator<DrawCommand>{

    @Override
    public int compare(DrawCommand o1, DrawCommand o2) {
        return (int) ((o1.getzIndex()-o1.getyPos())-(o2.getzIndex()-o2.getyPos()));
    }
}
