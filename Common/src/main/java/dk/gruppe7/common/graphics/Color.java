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
    public float r,g,b;
    public float a = 1;
    
    public Color(float r, float g, float b){this(r,g,b,1);}
    public Color(float r, float g, float b, float a){
        this.r = r; this.g = g; this.b = b; this.a = a;
    }
}
