/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.data;

/**
 *
 * @author Mathies H
 */
public class Rectangle
{
    private int width, height;

    public Rectangle() {
        this.width = 0;
        this.height = 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
}
