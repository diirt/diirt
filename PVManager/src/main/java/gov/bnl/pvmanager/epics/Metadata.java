/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Annotation to flag which fields are considered part of the metadata.
 * In Epics V3, these fields are fetched once at each connection, while
 * in Epics V4 are monitored as the rest.
 * 
 * @author carcassi
 */
public @interface Metadata {

}
