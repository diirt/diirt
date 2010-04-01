/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.math.BigDecimal;

/**
 *
 * @author carcassi
 */
public abstract class TimedTypeSupport<T> extends TypeSupport<T> {

    public abstract BigDecimal timestampOf(T object);

}
