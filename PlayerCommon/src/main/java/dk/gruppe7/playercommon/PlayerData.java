/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.playercommon;

import java.util.UUID;
import javax.swing.text.html.parser.Entity;

/**
 *
 * @author Mathies H
 */
public class PlayerData
{
    private static UUID playerUUID;

    public static UUID getPlayerUUID()
    {
        return playerUUID;
    }

    public static void setPlayerUUID(UUID playerUuid)
    {
        playerUUID = playerUuid;
    }
    
    public static boolean existingPlayer()
    {
        return playerUUID != null;
    }
}
