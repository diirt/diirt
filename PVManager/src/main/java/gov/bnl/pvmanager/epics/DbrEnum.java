/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 * Basic enum type. The value will be the current string being selected and
 * the index is the offset in the labels.
 *
 * @author carcassi
 */
public interface DbrEnum extends Scalar<String> {
    int getIndex();
    void setIndex(int index);
    @Metadata
    List<String> getLabels();
    void setLabels(List<String> labels);
}
