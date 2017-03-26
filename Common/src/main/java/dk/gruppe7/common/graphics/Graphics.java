
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;
import java.util.ArrayList;

public class Graphics {
    
    private static ArrayList<DrawCommand> drawCommands = new ArrayList<>();
    
    public static void drawRectangle(Vector2 position, Vector2 size, Color color){
        DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setColor(color);
        cmd.setType(DrawCommand.DrawCommandType.RECTANGLE);
        drawCommands.add(cmd);
    }
    
    public static void drawSprite(Vector2 position, Vector2 size, InputStream texture, float rotation){
        DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setInputStream(texture);
        cmd.setRotation(rotation);
        cmd.setType(DrawCommand.DrawCommandType.SPRITE);
        drawCommands.add(cmd);
    }
    
    public static ArrayList<DrawCommand> drawCommands(){
        return drawCommands;
    }
    
}
