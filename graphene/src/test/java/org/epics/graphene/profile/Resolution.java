package org.epics.graphene.profile;

/**
 *
 * @author Aaron
 */
public class Resolution {
    private int width, height;
    
    public Resolution (int width, int height){
        this.setWidth(width);
        this.setHeight(height);
    }
    
    public final void setWidth(int width){
        if (width <= 0){
            throw new IllegalArgumentException("Width must be a postive non-zero integer.");            
        }
        
        this.width = width;
    }
    
    public final void setHeight(int height){
        if (height <= 0){
            throw new IllegalArgumentException("Height must be a postive non-zero integer.");
        }
        
        this.height = height;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
}
