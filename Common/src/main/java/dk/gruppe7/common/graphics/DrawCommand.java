/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;

public class DrawCommand {
    public static enum DrawCommandType{
        LINE, RECTANGLE, CIRCLE, SPRITE
    }
    
    private DrawCommandType type;
    private Vector2 position = Vector2.zero;
    private Vector2 size = Vector2.zero;
    private Vector2 offset = null;
    private Color color;
    private InputStream inputStream;
    private float rotation;

    public DrawCommandType getType() {
        return type;
    }

    public void setType(DrawCommandType type) {
        this.type = type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }
    
    
    
        
}
