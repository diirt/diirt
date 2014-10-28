/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.pva.rpcservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.diirt.datasource.WriteFunction;

import org.epics.pvaccess.client.rpc.RPCClient;
import org.epics.pvaccess.server.rpc.RPCRequestException;
import org.epics.pvdata.factory.ConvertFactory;
import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.Convert;
import org.epics.pvdata.pv.Field;
import org.epics.pvdata.pv.FieldCreate;
import org.epics.pvdata.pv.PVField;
import org.epics.pvdata.pv.PVScalar;
import org.epics.pvdata.pv.PVScalarArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvdata.pv.Structure;
import org.diirt.datasource.pva.adapters.NTUtils;
import org.diirt.datasource.pva.adapters.PVFieldNTMatrixToVDoubleArray;
import org.diirt.datasource.pva.adapters.PVFieldNTNameValueToVTable;
import org.diirt.datasource.pva.adapters.PVFieldToVByteArray;
import org.diirt.datasource.pva.adapters.PVFieldToVDoubleArray;
import org.diirt.datasource.pva.adapters.PVFieldToVFloatArray;
import org.diirt.datasource.pva.adapters.PVFieldToVIntArray;
import org.diirt.datasource.pva.adapters.PVFieldToVShortArray;
import org.diirt.datasource.pva.adapters.PVFieldToVStatistics;
import org.diirt.datasource.pva.adapters.PVFieldToVStringArray;
import org.diirt.datasource.pva.adapters.PVFieldToVTable;
import org.diirt.service.ServiceMethod;
import org.diirt.service.pva.rpcservice.rpcclient.PooledRPCClientFactory;
import org.diirt.util.array.CollectionNumbers;
import org.diirt.util.array.ListBoolean;
import org.diirt.util.array.ListByte;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListFloat;
import org.diirt.util.array.ListInt;
import org.diirt.util.array.ListLong;
import org.diirt.util.array.ListShort;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VBooleanArray;
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
import org.diirt.vtype.VShort;
import org.diirt.vtype.VShortArray;
import org.diirt.vtype.VStatistics;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

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
  private final RPCServiceMethodDescription rpcServiceMethodDescription;
  private final String hostName;
  private final String channelName;
  private final String methodFieldName;
  private final boolean useNTQuery;


  /**
   * Creates a new rpcservice method.
   *
   * @param rpcServiceMethodDescription a method description
   */
  RPCServiceMethod(RPCServiceMethodDescription rpcServiceMethodDescription, String hostName, String channelName,
		  String methodFieldName, boolean useNTQuery) {
    super(rpcServiceMethodDescription.relaxedServiceMethodDescription);
    //super(rpcServiceMethodDescription.serviceMethodDescription);
    this.rpcServiceMethodDescription = rpcServiceMethodDescription;
    this.parameterNames = rpcServiceMethodDescription.orderedParameterNames;
    this.hostName = hostName;
    this.channelName = channelName;
    this.methodFieldName = methodFieldName;
    this.useNTQuery = useNTQuery;

    this.requestStructure = createRequestStructure(rpcServiceMethodDescription.structureId);
  }

  private Structure createRequestStructure(String structureId) {
	  Structure paramStructure = createParametersRequestStructure(structureId);

	  if (!useNTQuery)
		  return paramStructure;
	  else
	  {
		  return fieldCreate.createStructure("ev4:nt/NTURI:1.0", 
						  new String[] { "scheme", "path", "query" },
						  new Field[] { 
						  	fieldCreate.createScalar(ScalarType.pvString),
						  	fieldCreate.createScalar(ScalarType.pvString),
						  	paramStructure,
				  });
	  }
  }
  
  private Structure createParametersRequestStructure(String structureId) {
    if ((structureId == null) || (structureId.isEmpty())) {
      return fieldCreate.createStructure(createRequestFieldNames(), createRequestFieldTypes());
    } else
      return fieldCreate.createStructure(structureId, createRequestFieldNames(), createRequestFieldTypes());
  }


  private String[] createRequestFieldNames() {

    //only operation name, if specified
    if ((this.parameterNames == null) || (parameterNames.isEmpty())) {
      return methodFieldName != null ? new String[]{ methodFieldName } : new String[0];
    }

    //operation name (optional) + parameter names/fieldnames
    List<String> fieldNames = new ArrayList<String>(this.parameterNames.size() + 1);
    if (methodFieldName != null)
    	fieldNames.add(methodFieldName);
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

    //Map<String, Class<?>> argumentTypes = getArgumentTypes();
    // create ServiceMethod to get access to argumentTypes of serviceMethodDescription (non-relaxed)
    ServiceMethod sm = new ServiceMethod(rpcServiceMethodDescription.serviceMethodDescription) {
		@Override
		public void executeMethod(Map<String, Object> parameters,
				WriteFunction<Map<String, Object>> callback,
				WriteFunction<Exception> errorCallback) {
			// noop
		}
    };
    Map<String, Class<?>> argumentTypes = sm.getArgumentTypes();
    
    // operation name type + parameter types
    List<Field> fieldList = new ArrayList<Field>(this.parameterNames.size() + 1);
    if (methodFieldName != null)
    	fieldList.add(fieldCreate.createScalar(ScalarType.pvString));
    for (String parameterName : this.parameterNames.keySet()) {
      fieldList.add(convertToPvType(argumentTypes.get(parameterName)));
    }

    return fieldList.toArray(new Field[fieldList.size()]);
  }

  private Field convertToPvType(Class<?> argType) {
	  return NTUtils.vtypeToField(argType);
  }

  /*
  private ExecutorService getExecutorService() {
    return this.rpcServiceMethodDescription.executorService;
  }
  */

  private boolean isResultQuery() {
    return !getResultDescriptions().isEmpty();
  }


  @Override
  public void executeMethod(final Map<String, Object> parameters, final WriteFunction<Map<String, Object>> callback, final WriteFunction<Exception> errorCallback) {

    RPCClient rpcClient = null;

    try {

      rpcClient = PooledRPCClientFactory.getRPCClient(this.hostName, this.channelName);

    } catch (RPCRequestException e) {
      errorCallback.writeValue(e);
      return;
    }


    try {
      String methodName = this.rpcServiceMethodDescription.getOperationName() != null ?
        this.rpcServiceMethodDescription.getOperationName() : this.rpcServiceMethodDescription.getName();

      PVStructure pvRequest = createPvRequest(parameters, methodName);
      PVStructure pvResult = rpcClient.request(pvRequest, 3.0);

      VType vResult = createResult(pvResult);
      if (vResult != null) {
        Map<String, Object> resultMap = new HashMap<>();
        String resultName = getResultDescriptions().keySet().toArray(new String[getResultDescriptions().size()])[0];
        resultMap.put(resultName, vResult);
        callback.writeValue(resultMap);
      }
    } catch (RPCRequestException rre) {
      errorCallback.writeValue(rre);

    } finally {
      //back to the pool
      rpcClient.destroy();
    }
  }


  PVStructure createPvRequest(final Map<String, Object> parameters, String methodName) {

    PVStructure pvRequest = PVDataFactory.getPVDataCreate().createPVStructure(this.requestStructure);
    PVStructure retVal = pvRequest;
    if (useNTQuery)
    {
    	pvRequest.getStringField("scheme").put("pva");
    	pvRequest.getStringField("path").put(channelName);
    	pvRequest = pvRequest.getStructureField("query");
    }

    if (methodFieldName != null)
    	pvRequest.getStringField(methodFieldName).put(methodName);

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
      if (value == null)
    	  continue;

      PVField pvField = pvRequest.getSubField((fieldName != null ? fieldName : parameterName));
      
      
      if (pvField instanceof PVScalar)
      {
    	  PVScalar pvScalar = (PVScalar)pvField;
    	  if (value instanceof VDouble)
    		  convert.fromDouble(pvScalar, ((VDouble)value).getValue());
    	  else if (value instanceof VInt)
    		  convert.fromInt(pvScalar, ((VInt)value).getValue());
    	  else if (value instanceof VFloat)
    		  convert.fromFloat(pvScalar, ((VFloat)value).getValue());
    	  else if (value instanceof VLong)
    		  convert.fromLong(pvScalar, ((VLong)value).getValue());
    	  else if (value instanceof VShort)
    		  convert.fromShort(pvScalar, ((VShort)value).getValue());
    	  else if (value instanceof VByte)
    		  convert.fromByte(pvScalar, ((VByte)value).getValue());
    	  else if (value instanceof VBoolean)
    		  // Convert is missing fromBoolean
    	  	  convert.fromByte(pvScalar, ((VBoolean)value).getValue() ? (byte)1 : (byte)0);
       	  else if (value instanceof VString)
    		  convert.fromString(pvScalar, ((VString)value).getValue());
      }
      else if (pvField instanceof PVScalarArray)
      {
    	  PVScalarArray pvScalarArray = (PVScalarArray)pvField;
    	  if (value instanceof VDoubleArray)
    	  {
    		  ListDouble list = ((VDoubleArray)value).getData();
              double[] arr = CollectionNumbers.doubleArrayWrappedOrCopy(list);
    		  convert.fromDoubleArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
    	  else if (value instanceof VIntArray)
    	  {
    		  ListInt list = ((VIntArray)value).getData();
              int[] arr = CollectionNumbers.intArrayWrappedOrCopy(list);
    		  convert.fromIntArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
    	  else if (value instanceof VFloatArray)
    	  {
    		  ListFloat list = ((VFloatArray)value).getData();
              float[] arr = CollectionNumbers.floatArrayWrappedOrCopy(list);
    		  convert.fromFloatArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
    	  else if (value instanceof VLongArray)
    	  {
    		  ListLong list = ((VLongArray)value).getData();
              long[] arr = CollectionNumbers.longArrayWrappedOrCopy(list);
    		  convert.fromLongArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
    	  else if (value instanceof VShortArray)
    	  {
    		  ListShort list = ((VShortArray)value).getData();
              short[] arr = CollectionNumbers.shortArrayWrappedOrCopy(list);
    		  convert.fromShortArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
    	  else if (value instanceof VByteArray)
    	  {
    		  ListByte list = ((VByteArray)value).getData();
              byte[] arr = CollectionNumbers.byteArrayWrappedOrCopy(list);
    		  convert.fromByteArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
       	  else if (value instanceof VStringArray)
    	  {
    		  List<String> list = ((VStringArray)value).getData();
    		  String[] arr = list.toArray(new String[list.size()]);
    		  convert.fromStringArray(pvScalarArray, 0, arr.length, arr, 0);
    	  }
       	  else if (value instanceof VBooleanArray)
    	  {
    		  ListBoolean list = ((VBooleanArray)value).getData();
    		  int len = list.size();
              byte[] arr = new byte[len];
              for (int i = 0; i < len; i++)
            	  arr[i] = list.getBoolean(i) ? (byte)1 : (byte)0;
    		  convert.fromByteArray(pvScalarArray, 0, len, arr, 0);
    	  }
      }
      else
      {
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

    Class<?> resultType = getResultTypes().values().toArray(new Class<?>[getResultTypes().size()])[0];

    if (this.rpcServiceMethodDescription.isResultStandalone) {
      if (resultType.isAssignableFrom(VTable.class)) {

        if ("ev4:nt/NTNameValue:1.0".equals(pvResult.getStructure().getID()))
          return new PVFieldNTNameValueToVTable(pvResult, false);
        else
        	return new PVFieldToVTable(pvResult, false);
        
        // TODO !!!
      //} else if (resultType.isAssignableFrom(VImage.class)) {
      //  return new PVFieldToVImage(pvResult, false);
      } else if (resultType.isAssignableFrom(VStatistics.class)) {
          return new PVFieldToVStatistics(pvResult, false);
      } else if (resultType.isAssignableFrom(VDoubleArray.class) &&
    		  "ev4:nt/NTMatrix:1.0".equals(pvResult.getStructure().getID())) {
          return new PVFieldNTMatrixToVDoubleArray(pvResult, false);
      }

      throw new IllegalArgumentException("Standalone result type " + resultType.getSimpleName() + " not supported in pvAccess RPC rpcservice");
    }

    // TODO unsigned types, complex types 

    String resultName = getResultDescriptions().keySet().toArray(new String[getResultDescriptions().size()])[0];
    String fieldName = this.rpcServiceMethodDescription.getFieldNames().get(resultName);

    // TODO missing ValueFactory.newVLong
    
    if (resultType.isAssignableFrom(VDouble.class)) {
        return ValueFactory.newVDouble(pvResult.getDoubleField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
    } else if (resultType.isAssignableFrom(VFloat.class)) {
        return ValueFactory.newVFloat(pvResult.getFloatField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
    } else if (resultType.isAssignableFrom(VString.class)) {
        return ValueFactory.newVString(pvResult.getStringField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow());
    } else if (resultType.isAssignableFrom(VInt.class)) {
        return ValueFactory.newVInt(pvResult.getIntField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
    } else if (resultType.isAssignableFrom(VShort.class)) {
        return ValueFactory.newVShort(pvResult.getShortField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
    } else if (resultType.isAssignableFrom(VByte.class)) {
        return ValueFactory.newVByte(pvResult.getByteField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone());
    } else if (resultType.isAssignableFrom(VBoolean.class)) {
        return ValueFactory.newVBoolean(pvResult.getBooleanField(fieldName != null ? fieldName : resultName).get(), ValueFactory.alarmNone(), ValueFactory.timeNow());
    } else if (resultType.isAssignableFrom(VDoubleArray.class)) {
    	
		if ("ev4:nt/NTMatrix:1.0".equals(pvResult.getStructure().getID()))
	      return new PVFieldNTMatrixToVDoubleArray(pvResult, false);
	    else
          return new PVFieldToVDoubleArray(pvResult, fieldName != null ? fieldName : resultName, true);
		
    } else if (resultType.isAssignableFrom(VFloatArray.class)) {
        return new PVFieldToVFloatArray(pvResult, fieldName != null ? fieldName : resultName, true);
    } else if (resultType.isAssignableFrom(VIntArray.class)) {
        return new PVFieldToVIntArray(pvResult, fieldName != null ? fieldName : resultName, true);
    } else if (resultType.isAssignableFrom(VShortArray.class)) {
        return new PVFieldToVShortArray(pvResult, fieldName != null ? fieldName : resultName, true);
    } else if (resultType.isAssignableFrom(VByteArray.class)) {
        return new PVFieldToVByteArray(pvResult, fieldName != null ? fieldName : resultName, true);
    } else if (resultType.isAssignableFrom(VStringArray.class)) {
        return new PVFieldToVStringArray(pvResult, fieldName != null ? fieldName : resultName, true);
    } else if (resultType.isAssignableFrom(VTable.class)) {
        if ("ev4:nt/NTNameValue:1.0".equals(pvResult.getStructure().getID()))
            return new PVFieldNTNameValueToVTable(pvResult, false);
          else
          	return new PVFieldToVTable(pvResult, false);
        // TODO !!!
    //} else if (resultType.isAssignableFrom(VImage.class)) {
     //   return new PVFieldToVImage(pvResult,false);
    } else if (resultType.isAssignableFrom(VStatistics.class)) {
        return new PVFieldToVStatistics(pvResult,false);
    }
    
    throw new IllegalArgumentException("Result type " + resultType.getSimpleName() + " not supported in pvAccess RPC rpcservice");

  }
}
