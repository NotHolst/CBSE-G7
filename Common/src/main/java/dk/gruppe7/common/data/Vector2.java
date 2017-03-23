package dk.gruppe7.common.data;

/**
 *
 * @author Holst & Harald
 */
public class Vector2 {
    public final static Vector2 up = new Vector2(0.f, 1.f);
    public final static Vector2 left = new Vector2(-1.f, 0.f);
    public final static Vector2 down = new Vector2(0.f, -1.f);
    public final static Vector2 right = new Vector2(1.f, 0.f);
    public final static Vector2 zero = new Vector2(0.f, 0.f);
    
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
    
    public Vector2 div(Vector2 other) {
        return new Vector2(this.x / other.x, this.y / other.y);
    }
    
    public Vector2 clamp(float min, float max) {
        Vector2 temp = new Vector2(this.x, this.y);
        
        if(temp.x > max)
            temp.x = max;
        
        if(temp.x < min)
            temp.x = min;
        
        if(temp.y > max)
            temp.y = max;
        
        if(temp.y < min)
            temp.y = min;
        
        return temp;
    }
}
