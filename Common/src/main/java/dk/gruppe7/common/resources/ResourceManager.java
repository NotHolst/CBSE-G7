/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.resources;

import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author haral
 */
public class ResourceManager {
    private HashMap<String, Image> images = new HashMap<>();
    private HashMap<String, Audio> audio = new HashMap<>();
        
    public Image getImage(String identifier){
        return images.get(getIdentifier(identifier));
    }
    public Image addImage(String identifier, InputStream image){
        
        images.put(getIdentifier(identifier), new Image(image));
        return getImage(identifier);
        
    }
    private String getCallerClassName(){
        return Thread.currentThread().getStackTrace()[1].getFileName();
    }
    private String getIdentifier(String id){
    return String.format("%s:%s", getCallerClassName(), id);
    }
    
}
