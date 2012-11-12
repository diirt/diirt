/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

/**
 * A collector can be written from one thread and read from another and provides
 * the point where two subsystems and their rate can be decoupled..
 *
 * @author carcassi
 */
public interface NewCollector<I, O> extends WriteFunction<I>, Function<O> {
    
}
