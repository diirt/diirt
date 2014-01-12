/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

/**
 *
 * @author Aaron
 */
public class Resolution implements Comparable{
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
    
    public int getPixels(){
        return this.width * this.height;
    }
    
    @Override
    public String toString(){
        return width + "x" + height;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Resolution){
            Resolution other = ((Resolution) o);
            return Integer.compare(this.getPixels(), other.getPixels());
        }
        else{
            return 0;
        }
    }
}
