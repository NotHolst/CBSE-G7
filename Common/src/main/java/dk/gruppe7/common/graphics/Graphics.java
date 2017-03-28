
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;
import java.util.ArrayList;

public class Graphics {
    
    private ArrayList<DrawCommand> drawCommands = new ArrayList<>();
        
    public void drawSprite(Vector2 position, Vector2 size, InputStream texture, float rotation){
        DrawCommand cmd = new DrawCommand();
        cmd.setPosition(position);
        cmd.setSize(size);
        cmd.setInputStream(texture);
        cmd.setRotation(rotation);
        cmd.setType(DrawCommand.DrawCommandType.SPRITE);
        drawCommands.add(cmd);
    }
    
    public ArrayList<DrawCommand> getDrawCommands(){
        return drawCommands;
    }
    
}
