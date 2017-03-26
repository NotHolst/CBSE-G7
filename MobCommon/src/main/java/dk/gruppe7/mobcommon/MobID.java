/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import java.util.UUID;

/**
 *
 * @author benjaminmlynek
 */
public class MobID {
    
    private static UUID mobUUID;
    
    public static UUID getMobID() {
        return mobUUID;
    }
    
    public static void setMobID(UUID mobUuid) {
        mobUUID = mobUuid;
    }
    
    
}
