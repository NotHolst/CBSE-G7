/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.resources;

import java.io.InputStream;

/**
 *
 * @author haral
 */
public class Image {
   private InputStream image;
   
   public Image(InputStream image){
       this.image = image;
   }

    public InputStream getInputStream() {
        return image;
    } 
   
}
