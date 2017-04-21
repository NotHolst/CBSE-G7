
package dk.gruppe7.boss;

import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.graphics.Graphics;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class BossSystem implements IProcess, IRender{
        
    Room[] currentLevelBossRooms;
    
    @Override
    public void start(GameData gameData, World world) {
        
    }

    @Override
    public void stop(GameData gameData, World world) {
        
    }

    @Override
    public void process(GameData gameData, World world) {
        
    }

    @Override
    public void render(Graphics g, World world) {
        
    }

}
