/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

/**
 *
 * @author YifengYang
 */
public interface ProcessValue {
    
    void processScaledValue(int index, double valueX, double valueY, double scaledX, double scaledY);
    
}
