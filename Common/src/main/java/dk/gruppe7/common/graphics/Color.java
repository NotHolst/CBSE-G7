/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.graphics;

/**
 *
 * @author movie
 */
public class Color {
    public int r,g,b, a;
    
    public Color(int r, int g, int b){
        this.r = r; this.g = g; this.b = b; this.a = 255;
    }
    
    public Color(int r, int g, int b, int a){
        this.r = r; this.g = g; this.b = b; this.a = a;
    }
}
