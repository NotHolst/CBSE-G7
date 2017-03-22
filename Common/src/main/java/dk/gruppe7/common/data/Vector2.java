package dk.gruppe7.common.data;

/**
 *
 * @author Holst & Harald
 */
public class Vector2 {
    public float x,y;
    
    public Vector2(){this.x = 0; this.y = 0;}
    public Vector2(float x, float y){this.x = x; this.y = y;}
   
    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }
    
    public Vector2 add(Vector2 other){
        return new Vector2(this.x + other.x, this.y + other.y);
    }
    
    public Vector2 sub(float x, float y) {
        return new Vector2(this.x - x, this.y - y);
    }
    
    public Vector2 sub(Vector2 other){
        return new Vector2(this.x - other.x, this.y - other.y);
    }
    
    public Vector2 mul(float a) {
        return new Vector2(this.x * a, this.y * a);
    }
    
    public Vector2 mul(Vector2 other){
        return new Vector2(this.x * other.x, this.y * other.y);
    }
    
    public Vector2 div(float a) {
        return new Vector2(this.x / a, this.y / a);
    }
    
    public Vector2 div(Vector2 other){
        return new Vector2(this.x / other.x, this.y / other.y);
    }
    
    
}
