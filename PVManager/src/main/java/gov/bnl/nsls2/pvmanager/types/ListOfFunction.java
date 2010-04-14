/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.types;

import gov.bnl.nsls2.pvmanager.PVFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class ListOfFunction extends PVFunction<List> {

    List<PVFunction> functions;

    public ListOfFunction(List<PVFunction> functions) {
        super(List.class);
        this.functions = functions;
    }

    @Override
    public List getValue() {
        List list = new ArrayList();
        for (PVFunction function : functions) {
            list.add(function.getValue());
        }
        return list;
    }

}
