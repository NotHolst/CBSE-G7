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
    
    public float len() {
        return ((float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2)));
    }
    
    public Vector2 normalize() {
        return new Vector2(this.x / this.len(), this.y / this.len());
    }
    
    public Vector2 clampRange(float min, float max) {
        return new Vector2(this.x, this.y) {{
            x = Math.max(min, Math.min(x, max));
            y = Math.max(min, Math.min(y, max));
        }};
    }
    
    public Vector2 clampLength(float maxLength) {
        if(len() > maxLength) {
            return normalize().mul(maxLength);
        } else {
            return this;
        }
    }
    
    public Vector2 rotated(double rotation)
    {
        Vector2 rotatedVector = new Vector2();
        rotatedVector.x = (float) (x * Math.cos(Math.toRadians(rotation)) - y * Math.sin(Math.toRadians(rotation)));
        rotatedVector.y =  (float) (x * Math.sin(Math.toRadians(rotation)) + y * Math.cos(Math.toRadians(rotation)));
        return rotatedVector;
    }
    
    @Override
    public String toString() {
        return String.format("x: %.2f | y: %.2f", this.x, this.y);
    }
}
