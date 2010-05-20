/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.types;

import gov.bnl.pvmanager.Function;
import java.util.ArrayList;
import java.util.List;

/**
 * A function that takes many inputs and creates a list with them.
 *
 * @author carcassi
 */
class ListOfFunction extends Function<List> {

    List<Function> functions;

    public ListOfFunction(List<Function> functions) {
        super(List.class);
        this.functions = functions;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getValue() {
        List list = new ArrayList();
        for (Function function : functions) {
            list.add(function.getValue());
        }
        return list;
    }

}
