/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.playercommon;

import javax.swing.text.html.parser.Entity;

/**
 *
 * @author Mathies H
 */
public class PlayerData
{
    private static Entity _player;

    public static Entity getPlayer()
    {
        return _player;
    }

    public static void setPlayer(Entity player)
    {
        _player = player;
    }
    
    public static boolean existingPlayer()
    {
        return _player != null;
    }
}
