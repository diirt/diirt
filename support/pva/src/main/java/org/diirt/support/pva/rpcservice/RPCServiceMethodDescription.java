/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.rpcservice;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;

import org.diirt.vtype.VByte;
import org.diirt.vtype.VByteArray;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VDoubleArray;
import org.diirt.vtype.VFloat;
import org.diirt.vtype.VFloatArray;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VIntArray;
import org.diirt.vtype.VLong;
import org.diirt.vtype.VLongArray;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VShort;
import org.diirt.vtype.VShortArray;

/**
 * The description for a pvAccess RPC rpcservice method.
 *
 * @author dkumar
 */
public class RPCServiceMethodDescription extends ServiceMethodDescription {

    boolean resultAdded = false;
    final Map<String, String> orderedParameterNames = new HashMap<>();
    final Map<String, String> fieldNames = new HashMap<>();
    final String structureId;
    final boolean isResultStandalone;
    final String operationName;

    final Map<String, Class> strictArguments = new HashMap<>();

    final static String FIELD_NAME_EQUALS_NAME = "__NOALIAS__";

    /**
     * A new rpcservice method with the given name and description.
     *
     * @param name the method name
     * @param description the method description
     * @param operationName operation name
     * @param structureId pvStructure id
     * @param isResultStandalone is result standalone i.e. image, table
     */
    public RPCServiceMethodDescription(String name, String description, String operationName,
            String structureId, boolean isResultStandalone) {
        super(name, description);

        this.operationName = operationName;
        this.structureId = structureId;
        this.isResultStandalone = isResultStandalone;
    }

    /**
     * Get structure id
     *
     * @return structure id
     */
    public String getStructureId() {
        return this.structureId;
    }

    /**
     * Get operation name
     *
     * @return operation name
     */
    public String getOperationName() {
        return this.operationName;
    }

    /**
     * Get field names
     *
     * @return field names
     */
    public Map<String, String> getFieldNames() {
        return this.fieldNames;
    }

    static Class<?> relaxArgumentType(Class<?> type) {
        if (type.equals(VDouble.class)
                || type.equals(VFloat.class)
                || type.equals(VInt.class)
                || type.equals(VLong.class)
                || type.equals(VShort.class)
                || type.equals(VByte.class)) {
            type = VNumber.class;
        } else if (type.equals(VDoubleArray.class)
                || type.equals(VFloatArray.class)
                || type.equals(VIntArray.class)
                || type.equals(VLongArray.class)
                || type.equals(VShortArray.class)
                || type.equals(VByteArray.class)) {
            type = VNumberArray.class;
        }

        return type;

    }

    /**
     * Adds an argument for pvAccess RPC rpcservice.
     *
     * @param name argument name
     * @param description argument description
     * @param type the expected type of the argument
     * @return this
     */
    public RPCServiceMethodDescription addArgument(String name, String fieldName, String description, Class<?> type) {
        super.addArgument(name, description, relaxArgumentType(type));
        strictArguments.put(name, type);

        orderedParameterNames.put(name, fieldName != null ? fieldName : FIELD_NAME_EQUALS_NAME);
        return this;
    }

    /**
     * Adds a result for the pvAccess RPC Service.
     *
     * @param name the result name
     * @param description the result description
     * @return this
     */
    public RPCServiceMethodDescription addRPCResult(String name, String fieldName, String description, Class<?> type) {
        if (resultAdded) {
            throw new IllegalArgumentException("The pvAccess RPC rpcservice can only have one result");
        }

        if (fieldName != null) {
            this.fieldNames.put(name, fieldName);
        } else {
            this.fieldNames.put(name, FIELD_NAME_EQUALS_NAME);
        }
        super.addResult(name, description, type);

        this.resultAdded = true;
        return this;
    }

    @Override
    public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
        return new RPCServiceMethod(this, (RPCServiceDescription) serviceDescription);
    }
}
