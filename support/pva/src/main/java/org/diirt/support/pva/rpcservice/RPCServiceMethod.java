/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.rpcservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.diirt.service.ServiceMethod;
import org.diirt.support.pva.NTUtils;
import org.diirt.support.pva.PVAToVTypes;
import org.diirt.support.pva.rpcservice.rpcclient.PooledRPCClientFactory;
import org.epics.util.array.ArrayFloat;
import org.epics.util.array.CollectionNumbers;
import org.epics.util.array.ListBoolean;
import org.epics.util.array.ListByte;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListFloat;
import org.epics.util.array.ListInteger;
import org.epics.util.array.ListInteger;
import org.epics.util.array.ListLong;
import org.epics.util.array.ListShort;
import org.epics.util.array.UnsafeUnwrapper;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VByte;
import org.epics.vtype.VByteArray;
import org.epics.vtype.VDouble;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VFloat;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VInt;
import org.epics.vtype.VIntArray;
import org.epics.vtype.VLong;
import org.epics.vtype.VLongArray;
import org.epics.vtype.VShort;
import org.epics.vtype.VShortArray;
import org.epics.vtype.VStatistics;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.VType;

import org.epics.pvaccess.client.rpc.RPCClient;
import org.epics.pvaccess.server.rpc.RPCRequestException;
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.Convert;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.FloatArrayData;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVFloatArray;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;

import static org.diirt.support.pva.PVAToVTypes.*;
/**
 * The implementation of a pvAccess RPC rpcservice method.
 *
 * @author dkumar
 */
class RPCServiceMethod extends ServiceMethod {

    private final static FieldCreate fieldCreate = FieldFactory.getFieldCreate();
    private final static Convert convert = ConvertFactory.getConvert();

    private final Structure requestStructure;
    private final Map<String, String> parameterNames;
    private final String hostName;
    private final String channelName;
    private final String methodFieldName;
    private final boolean useNTQuery;
    private final boolean isResultStandalone;
    private final Map<String, String> fieldNames;
    private final String operationName;

    private final Map<String, Class> strictArguments;

    /**
     * Creates a new rpcservice method.
     *
     * @param rpcServiceMethodDescription a method description
     */
    RPCServiceMethod(RPCServiceMethodDescription serviceMethodDescription, RPCServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);

        this.parameterNames = serviceMethodDescription.orderedParameterNames;
        this.hostName = serviceDescription.hostName;
        this.channelName = serviceDescription.channelName;
        this.methodFieldName = serviceDescription.methodFieldName;
        this.useNTQuery = serviceDescription.useNTQuery;
        this.isResultStandalone = serviceMethodDescription.isResultStandalone;
        this.fieldNames = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.getFieldNames()));
        this.operationName = serviceMethodDescription.operationName;
        this.strictArguments = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.strictArguments));
        this.requestStructure = createRequestStructure(serviceMethodDescription.structureId);

    }

    private Structure createRequestStructure(String structureId) {
        Structure paramStructure = createParametersRequestStructure(structureId);

        if (!useNTQuery) {
            return paramStructure;
        } else {
            return fieldCreate.createStructure("ev4:nt/NTURI:1.0",
                    new String[]{"scheme", "path", "query"},
                    new Field[]{
                        fieldCreate.createScalar(ScalarType.pvString),
                        fieldCreate.createScalar(ScalarType.pvString),
                        paramStructure,});
        }
    }

    private Structure createParametersRequestStructure(String structureId) {
        if ((structureId == null) || (structureId.isEmpty())) {
            return fieldCreate.createStructure(createRequestFieldNames(), createRequestFieldTypes());
        } else {
            return fieldCreate.createStructure(structureId, createRequestFieldNames(), createRequestFieldTypes());
        }
    }

    private String[] createRequestFieldNames() {
        //only operation name, if specified
        if ((this.parameterNames == null) || (parameterNames.isEmpty())) {
            return methodFieldName != null ? new String[]{methodFieldName} : new String[0];
        }

        //operation name (optional) + parameter names/fieldnames
        List<String> fieldNames = new ArrayList<String>(this.parameterNames.size() + 1);
        if (methodFieldName != null) {
            fieldNames.add(methodFieldName);
        }
        for (String parameterName : this.parameterNames.keySet()) {
            String fieldName = this.parameterNames.get(parameterName);
            if (fieldName.equals(RPCServiceMethodDescription.FIELD_NAME_EQUALS_NAME)) {
                fieldNames.add(parameterName);
            } else {
                fieldNames.add(fieldName);
            }
        }

        return fieldNames.toArray(new String[fieldNames.size()]);
    }

    private Field[] createRequestFieldTypes() {
        //only operation name type
        if ((this.parameterNames == null) || (parameterNames.isEmpty())) {
            return methodFieldName != null ? new Field[]{fieldCreate.createScalar(ScalarType.pvString)} : new Field[0];
        }

        // operation name type + parameter types
        List<Field> fieldList = new ArrayList<Field>(this.parameterNames.size() + 1);
        if (methodFieldName != null) {
            fieldList.add(fieldCreate.createScalar(ScalarType.pvString));
        }

        // non-relaxed argument types
        for (String parameterName : this.parameterNames.keySet()) {
            fieldList.add(convertToPvType(strictArguments.get(parameterName)));
        }

        return fieldList.toArray(new Field[fieldList.size()]);
    }

    private Field convertToPvType(Class<?> argType) {
        return NTUtils.vtypeToField(argType);
    }

    private boolean isResultQuery() {
        return !getResults().isEmpty();
    }

    @Override
    public void asyncExecImpl(final Map<String, Object> parameters, final Consumer<Map<String, Object>> callback, final Consumer<Exception> errorCallback) {
        RPCClient rpcClient = null;

        try {
            rpcClient = PooledRPCClientFactory.getRPCClient(this.hostName, this.channelName);
        } catch (RPCRequestException e) {
            errorCallback.accept(e);
            return;
        }

        try {
            String methodName = operationName != null ? operationName : getName();

            PVStructure pvRequest = createPvRequest(parameters, methodName);
            PVStructure pvResult = rpcClient.request(pvRequest, 3.0);

            VType vResult = createResult(pvResult);
            if (vResult != null) {
                Map<String, Object> resultMap = new HashMap<>();
                String resultName = getResultMap().keySet().toArray(new String[getResultMap().size()])[0];
                resultMap.put(resultName, vResult);
                callback.accept(resultMap);
            }
        } catch (RPCRequestException rre) {
            errorCallback.accept(rre);
        } finally {
            //back to the pool
            rpcClient.destroy();
        }
    }   

    PVStructure createPvRequest(final Map<String, Object> parameters, String methodName) {
        PVStructure pvRequest = PVDataFactory.getPVDataCreate().createPVStructure(this.requestStructure);
        PVStructure retVal = pvRequest;
        if (useNTQuery) {
            pvRequest.getStringField("scheme").put("pva");
            pvRequest.getStringField("path").put(channelName);
            pvRequest = pvRequest.getStructureField("query");
        }

        if (methodFieldName != null) {
            pvRequest.getStringField(methodFieldName).put(methodName);
        }

        if ((parameters == null) || (parameters.isEmpty())) {
            return retVal;
        }

        for (String parameterName : parameters.keySet()) {
            //check if the parameter name is only an alias (if we get a field name back, it's an alias)
            String fieldName = this.parameterNames.get(parameterName);

            if ((fieldName == null) || (fieldName.equals(RPCServiceMethodDescription.FIELD_NAME_EQUALS_NAME))) {
                fieldName = null;
            }

            Object value = parameters.get(parameterName);

            // consider it as optional
            if (value == null) {
                continue;
            }

            PVField pvField = pvRequest.getSubField((fieldName != null ? fieldName : parameterName));

            if (pvField instanceof PVScalar) {
                PVScalar pvScalar = (PVScalar) pvField;
                if (value instanceof VDouble) {
                    convert.fromDouble(pvScalar, ((VDouble) value).getValue());
                } else if (value instanceof VInt) {
                    convert.fromInt(pvScalar, ((VInt) value).getValue());
                } else if (value instanceof VFloat) {
                    convert.fromFloat(pvScalar, ((VFloat) value).getValue());
                } else if (value instanceof VLong) {
                    convert.fromLong(pvScalar, ((VLong) value).getValue());
                } else if (value instanceof VShort) {
                    convert.fromShort(pvScalar, ((VShort) value).getValue());
                } else if (value instanceof VByte) {
                    convert.fromByte(pvScalar, ((VByte) value).getValue());
                } else if (value instanceof VBoolean) // Convert is missing fromBoolean
                {
                    convert.fromByte(pvScalar, ((VBoolean) value).getValue() ? (byte) 1 : (byte) 0);
                } else if (value instanceof VString) {
                    convert.fromString(pvScalar, ((VString) value).getValue());
                }
            } else if (pvField instanceof PVScalarArray) {
                PVScalarArray pvScalarArray = (PVScalarArray) pvField;
                if (value instanceof VDoubleArray) {
                    ListDouble list = ((VDoubleArray) value).getData();
                    double[] arr = UnsafeUnwrapper.wrappedDoubleArray(list).array;
                    convert.fromDoubleArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VIntArray) {
                    ListInteger list = ((VIntArray) value).getData();
                    int[] arr = UnsafeUnwrapper.wrappedIntArray(list).array;
                    convert.fromIntArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VFloatArray) {
                    ListFloat list = ((VFloatArray) value).getData();
                    float[] arr = UnsafeUnwrapper.wrappedFloatArray(list).array;
                    convert.fromFloatArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VLongArray) {
                    ListLong list = ((VLongArray) value).getData();
                    long[] arr = UnsafeUnwrapper.wrappedLongArray(list).array;
                    convert.fromLongArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VShortArray) {
                    ListShort list = ((VShortArray) value).getData();
                    short[] arr = UnsafeUnwrapper.wrappedShortArray(list).array;
                    convert.fromShortArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VByteArray) {
                    ListByte list = ((VByteArray) value).getData();
                    byte[] arr = UnsafeUnwrapper.wrappedByteArray(list).array;
                    convert.fromByteArray(pvScalarArray, 0, arr.length, arr, 0);
                } else if (value instanceof VStringArray) {
                    List<String> list = ((VStringArray) value).getData();
                    String[] arr = list.toArray(new String[list.size()]);
                    convert.fromStringArray(pvScalarArray, 0, arr.length, arr, 0);
                }
//                else if (value instanceof VBooleanArray) {
//                    ListBoolean list = ((VBooleanArray) value).getData();
//                    int len = list.size();
//                    byte[] arr = new byte[len];
//                    for (int i = 0; i < len; i++) {
//                        arr[i] = list.getBoolean(i) ? (byte) 1 : (byte) 0;
//                    }
//                    convert.fromByteArray(pvScalarArray, 0, len, arr, 0);
//                }
            } else {
                // TODO complex types (do we need to support them?)
                throw new RuntimeException("pvAccess RPC Service mapping support for " + value.getClass().getSimpleName() + " not implemented");
            }
        }

        return retVal;
    }

    private VType createResult(PVStructure pvResult) {
        if (pvResult == null) {
            return null;
        }

        if (!isResultQuery()) {
            return null;
        }

        Class<?> resultType = getResults().get(0).getType();

//        if (isResultStandalone) {
//            if (resultType.isAssignableFrom(VTable.class)) {
//
//                if ("ev4:nt/NTNameValue:1.0".equals(pvResult.getStructure().getID())) {
//                    return new PVFieldNTNameValueToVTable(pvResult, false);
//                } else {
//                    return new PVFieldToVTable(pvResult, false);
//                }
//
//                // TODO !!!
//                // } else if (resultType.isAssignableFrom(VImage.class)) {
//                // return new PVFieldToVImage(pvResult, false);
//            } else if (resultType.isAssignableFrom(VStatistics.class)) {
//                return new PVFieldToVStatistics(pvResult, false);
//            } else if (resultType.isAssignableFrom(VDoubleArray.class)
//                    && "ev4:nt/NTMatrix:1.0".equals(pvResult.getStructure().getID())) {
//                return new PVFieldNTMatrixToVDoubleArray(pvResult, false);
//            }
//
//            throw new IllegalArgumentException("Standalone result type " + resultType.getSimpleName()
//                    + " not supported in pvAccess RPC rpcservice");
//        }

        // TODO unsigned types, complex types
        String resultName = getResults().get(0).getName();
        String fieldName = fieldNames.get(resultName);

        if (resultType.isAssignableFrom(VDouble.class)) {
            return VDouble.of(pvResult.getDoubleField(fieldName != null ? fieldName : resultName).get(),
                    Alarm.none(), Time.now(), Display.none());
        } else if (resultType.isAssignableFrom(VFloat.class)) {
            return VFloat.of(pvResult.getFloatField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now(), Display.none());
        } else if (resultType.isAssignableFrom(VString.class)) {
            return VString.of(pvResult.getStringField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now());
        } else if (resultType.isAssignableFrom(VInt.class)) {
            return VInt.of(pvResult.getIntField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now(), Display.none());
        } else if (resultType.isAssignableFrom(VShort.class)) {
            return VShort.of(pvResult.getShortField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now(), Display.none());
        } else if (resultType.isAssignableFrom(VByte.class)) {
            return VByte.of(pvResult.getByteField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now(), Display.none());
        } else if (resultType.isAssignableFrom(VBoolean.class)) {
            return VBoolean.of(pvResult.getBooleanField(fieldName != null ? fieldName : resultName).get(), Alarm.none(), Time.now());
        } else if (resultType.isAssignableFrom(VDoubleArray.class)) {
            return vDoubleArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvDouble), pvResult, true);
        } else if (resultType.isAssignableFrom(VFloatArray.class)) {
            return vFloatArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvFloat), pvResult, true);
        } else if (resultType.isAssignableFrom(VIntArray.class)) {
            return vIntArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvInt), pvResult, true);
        } else if (resultType.isAssignableFrom(VShortArray.class)) {
            return vShortArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvShort), pvResult, true);
        } else if (resultType.isAssignableFrom(VByteArray.class)) {
            return vByteArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvByte), pvResult, true);
        }
        // TODO the following type support needs to be updated and added.

//        } else if (resultType.isAssignableFrom(VStringArray.class)) {
//            return vStringArrayOf(pvResult.getScalarArrayField(fieldName != null ? fieldName : resultName, ScalarType.pvString), pvResult, true);
//        } else if (resultType.isAssignableFrom(VBooleanArray.class)) {
//            return new PVFieldToVBooleanArray(fieldName != null ? fieldName : resultName, pvResult, true);
//        } else if (resultType.isAssignableFrom(VTable.class)) {
//            if ("epics:nt/NTNameValue:1.0".equals(pvResult.getStructure().getID())) {
//                return new PVFieldNTNameValueToVTable(pvResult, false);
//            } else {
//                return new PVFieldToVTable(pvResult, false);
//            }
            // TODO !!!
            // } else if (resultType.isAssignableFrom(VImage.class)) {
            // return new PVFieldToVImage(pvResult,false);
//        } else if (resultType.isAssignableFrom(VStatistics.class)) {
//            return new PVFieldToVStatistics(pvResult, false);
//        }
        throw new IllegalArgumentException("Result type " + resultType.getSimpleName() + " not supported in pvAccess RPC rpcservice");

    }

    public String getStructureID(){
        return requestStructure.getID();
    }

    public String getOperationName(){
        return operationName;
    }
}
