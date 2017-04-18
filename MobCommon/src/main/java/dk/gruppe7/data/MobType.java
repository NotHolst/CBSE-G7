/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.data;

import dk.gruppe7.common.utils.RandomUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author benjaminmlynek
 */
public enum MobType implements Serializable {
    
    RANGED, MELEE; 
    
    private static ArrayList<MobType> listOfValues = new ArrayList<>(Arrays.asList(values()));
    
    public static MobType getRandom() {
        return listOfValues.get(RandomUtil.GetRandomInteger(listOfValues.size()));
    }    
}
