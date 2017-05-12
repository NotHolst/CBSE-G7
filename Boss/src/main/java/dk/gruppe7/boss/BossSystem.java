
package dk.gruppe7.boss;

import dk.gruppe7.boss.types.Dragon;
import dk.gruppe7.bosscommon.Boss;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.eventtypes.DisposeEvent;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.graphics.Color;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.damagecommon.DamageEvent;
import dk.gruppe7.levelcommon.events.LevelChangedEvent;
import dk.gruppe7.levelcommon.events.LevelGenerationEvent;
import dk.gruppe7.playercommon.Player;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class BossSystem implements IProcess, IRender{
        
    Room currentLevelBossRoom;
    boolean bossDead = false;
    
    Image shadow;
    private ArrayList<Boss> disposeList = new ArrayList<>();
    
    private int screenHeight,screenWidth;
    
    @Override
    public void start(GameData gameData, World world) {
        shadow = gameData.getResourceManager().addImage("shadow", getClass().getResourceAsStream("Shadow.png"));
        screenHeight = gameData.getScreenHeight();
        screenWidth = gameData.getScreenWidth();
        
        findBossRoom(world);
        //world.setCurrentRoom(currentLevelBossRoom);
        if(currentLevelBossRoom != null){
            Boss boss = new Dragon(gameData, world);
            boss.setTarget(world.getEntitiesByClass(Player.class).get(0));
            boss.setRoomPersistent(false);
            boss.setPositionCentered(new Vector2(gameData.getScreenWidth()/2f, (gameData.getScreenHeight()-100)/2f));
            currentLevelBossRoom.getEntities().add(boss);
        }
        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
    }

    @Override
    public void process(GameData gameData, World world) {
        for(Boss boss : world.<Boss>getEntitiesByClass(Boss.class)){
            boss.setPosition(boss.getPosition().add(boss.getVelocity().mul(gameData.getDeltaTime())));
            boss.process();
            boss.getAnimator().update(gameData);
            if(boss.getHealthData().getHealth() <= 0){
                bossDead=true;
                disposeList.add(boss);
            }
        }
        if(bossDead){
            Dispatcher.post(new LevelChangedEvent(), world);
            bossDead = false;
        }
    }
    ActionEventHandler<DisposeEvent> disposalHandler = (event, world) -> {
        world.removeEntities(disposeList);
        disposeList.clear();
    };
    ActionEventHandler<DamageEvent> damageHandler = (event, world) -> {
        if (world.isEntityOfClass(event.getTarget(), Boss.class))
            world.<Boss>getEntityByID(event.getTarget()).getHealthData().decreaseHealth(event.getDamageDealt().getDamage());
    };

    @Override
    public void render(Graphics g, World world) {
        if(world.getCurrentRoom().equals(currentLevelBossRoom)){
            // g.drawString(new Vector2(300, 300), "U IN DA BAWS RUM! PRESS ENTER TO DEFEAT THE MIGHTY  BOSS");
        }
        
        for(Boss boss : world.<Boss>getEntitiesByClass(Boss.class)){
            g.drawSprite(boss.getPosition().add(new Vector2(-12, -25)), new Vector2(150,100), shadow.getInputStream(), 0, -900);
            g.drawSprite(boss.getPosition().sub(new Vector2(26,0)), new Vector2(86*2,128*2), boss.getAnimator().getTexture(), 0, 0, boss.getPositionCentered().y);
            float hp = (screenWidth/2 - 64) * boss.getHealthData().getHealth() / boss.getHealthData().getStartHealth(); 
            g.drawRectangle(new Vector2(screenWidth/4, screenHeight-16), new Vector2(hp, 16), new Color(1f,.2f,.2f), true, 9001);
        }
    }
    
    ActionEventHandler<LevelGenerationEvent> levelGenerationHandler = (event, world) -> {
        findBossRoom(world);
    };
    
    ActionEventHandler<LevelChangedEvent> levelChangedHandler = (event, world) -> {
        //TODO: Remove this
        System.out.println("Level should change now, this is from bossSystem line 63");
    };
    
    private void findBossRoom(World world){
        currentLevelBossRoom = findHighestDistanceRoom(world.getCurrentRoom());
    }
    
    private Room findHighestDistanceRoom(Room startRoom){
       
       ArrayList<Room> openList = new ArrayList<>();
       ArrayList<Room> closedList = new ArrayList<>();
       
       Room highest = startRoom;
       openList.add(highest);
       while(openList.size() > 0){
           Room current = openList.remove(0);
           closedList.add(current);
           
           if(current.getDistanceFromStart() > highest.getDistanceFromStart())
               highest = current;
           
           openList.addAll(
                getNeighbours(current).stream()
                   .filter(
                       room -> !closedList.contains(room)
                   ).collect(Collectors.toList())
           );
       }
       return highest;
    }
    
    private ArrayList<Room> getNeighbours(Room room){
        ArrayList<Room> result = new ArrayList<>();
            if(room.getNorth() != null)result.add(room.getNorth());
            if(room.getSouth() != null)result.add(room.getSouth());
            if(room.getWest()!= null)result.add(room.getWest());
            if(room.getEast()!= null)result.add(room.getEast());
        return result;
    }
    
    KeyEventHandler<KeyPressedEvent> enterKeyHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_ENTER) {
        @Override
        public void call(KeyPressedEvent event) {
            bossDead = true;
        }
    };
    
    ActionEventHandler<CollisionEvent> obstacleCollisionHandler = (event, world) -> {
        if(world.getEntityByID(event.getOtherID()) instanceof Player && world.getEntityByID(event.getTargetID()) instanceof Boss){
            Player player = world.getEntityByID(event.getOtherID());
            Boss boss = world.getEntityByID(event.getTargetID());
            
            player.setVelocity(player.getPositionCentered().sub(boss.getPositionCentered()).normalize().mul(1337));
        }
    };

}
