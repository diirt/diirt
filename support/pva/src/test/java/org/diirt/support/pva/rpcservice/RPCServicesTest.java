/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva.rpcservice;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.WriteCache;
import org.diirt.service.Service;
import org.diirt.service.ServiceMethod;
import org.diirt.support.pva.rpcservice.rpcclient.PooledRPCClientFactory;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayFloat;
import org.diirt.vtype.*;
import org.epics.pvaccess.PVAException;
import org.epics.pvaccess.server.rpc.RPCRequestException;
import org.epics.pvaccess.server.rpc.RPCServer;
import org.epics.pvdata.factory.FieldFactory;
import org.epics.pvdata.factory.PVDataFactory;
import org.epics.pvdata.pv.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testing the pvAccess RPC rpcservice
 *
 * @author dkumar
 */
// TODO
@Ignore
public class RPCServicesTest implements Runnable {

    private RPCServer rpcServer;

    private final static FieldCreate fieldCreate = FieldFactory.getFieldCreate();

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() throws Exception {
        Service service = RPCServices.createFromXml(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromXmlWithInvalidXml() {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceInvalidXml.xml");
        RPCServices.createFromXml(stream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromXmlWithInvalidVersion() {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceWithInvalidVersion.xml");
        RPCServices.createFromXml(stream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromXmlWithNonSupportedMethodParameterType() {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceWithInvalidParameterType.xml");
        RPCServices.createFromXml(stream);
    }

    @Test
    public void testCreateFromXml() {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);

        assertThat(service.getName(), equalTo("pvRPCServiceSample"));
        assertThat(service.getDescription(), equalTo("A test pvRPC service"));
        assertThat(service.getServiceMethods().size(), equalTo(7));

        //checking the specifics of pva rpc
        assertThat(((RPCServiceMethod) service.getServiceMethods().get("sum")).getStructureID(),
                equalTo("uri:ev4:nt/2012/pwd:NTXY"));

        assertThat(service.getServiceMethods().get("sum").getName(), equalTo("sum"));
        assertThat(service.getServiceMethods().get("sum").getDescription(), equalTo("sum the two numbers"));
        assertThat(service.getServiceMethods().get("sum").getResultMap().get("c").getType(), equalTo((Class) VDouble.class));
        assertThat(service.getServiceMethods().get("sum").getResultMap().get("c").getDescription(), equalTo("sum result"));
        assertThat(service.getServiceMethods().get("sum").getArgumentMap().get("a").getDescription(), equalTo("first number"));
        assertThat(service.getServiceMethods().get("sum").getArgumentMap().get("a").getType(), equalTo((Class) VDouble.class));
        assertThat(service.getServiceMethods().get("sum").getArgumentMap().get("b").getDescription(), equalTo("second number"));
        assertThat(service.getServiceMethods().get("sum").getArgumentMap().get("b").getType(), equalTo((Class) VDouble.class));

        assertThat(service.getServiceMethods().get("concate").getName(), equalTo("concate"));
        assertThat(service.getServiceMethods().get("concate").getDescription(), equalTo("concate two strings"));
        assertThat(service.getServiceMethods().get("concate").getResultMap().get("c").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("concate").getResultMap().get("c").getDescription(), equalTo("concate result"));
        assertThat(service.getServiceMethods().get("concate").getArgumentMap().get("a").getDescription(), equalTo("first string"));
        assertThat(service.getServiceMethods().get("concate").getArgumentMap().get("a").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("concate").getArgumentMap().get("b").getDescription(), equalTo("second string"));
        assertThat(service.getServiceMethods().get("concate").getArgumentMap().get("b").getType(), equalTo((Class) VString.class));

        assertThat(service.getServiceMethods().get("getTable").getName(), equalTo("getTable"));
        assertThat(service.getServiceMethods().get("getTable").getDescription(), equalTo("get table as a standalone result"));
        assertThat(service.getServiceMethods().get("getTable").getResultMap().get("result").getType(), equalTo((Class) VTable.class));
        assertThat(service.getServiceMethods().get("getTable").getResultMap().get("result").getDescription(), equalTo("table result"));
        assertThat(service.getServiceMethods().get("getTable").getArgumentMap().isEmpty(), equalTo(true));

        assertThat(service.getServiceMethods().get("getImage").getName(), equalTo("getImage"));
        assertThat(service.getServiceMethods().get("getImage").getDescription(), equalTo("get image as a standalone result"));
        assertThat(service.getServiceMethods().get("getImage").getResultMap().get("result").getType(), equalTo((Class) VImage.class));
        assertThat(service.getServiceMethods().get("getImage").getResultMap().get("result").getDescription(), equalTo("image result"));
        assertThat(service.getServiceMethods().get("getImage").getArgumentMap().isEmpty(), equalTo(true));

        assertThat(service.getServiceMethods().get("multipyString").getName(), equalTo("multipyString"));
        assertThat(service.getServiceMethods().get("multipyString").getDescription(), equalTo("multiply the string"));
        assertThat(service.getServiceMethods().get("multipyString").getResultMap().get("c").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("multipyString").getResultMap().get("c").getDescription(), equalTo("multiply result"));
        assertThat(service.getServiceMethods().get("multipyString").getArgumentMap().get("string").getDescription(), equalTo("string to multiply"));
        assertThat(service.getServiceMethods().get("multipyString").getArgumentMap().get("string").getType(), equalTo((Class) VString.class));
        assertThat(service.getServiceMethods().get("multipyString").getArgumentMap().get("times").getDescription(), equalTo("how many times to multiply"));
        assertThat(service.getServiceMethods().get("multipyString").getArgumentMap().get("times").getType(), equalTo((Class) VInt.class));

        assertThat(service.getServiceMethods().get("getTimeInMiliseconds").getName(), equalTo("getTimeInMiliseconds"));
        assertThat(service.getServiceMethods().get("getTimeInMiliseconds").getDescription(), equalTo("get time in milliseconds"));
        assertThat(service.getServiceMethods().get("getTimeInMiliseconds").getResultMap().get("c").getType(), equalTo((Class) VDouble.class));
        assertThat(service.getServiceMethods().get("getTimeInMiliseconds").getResultMap().get("c").getDescription(), equalTo("time result"));
        assertThat(service.getServiceMethods().get("getTimeInMiliseconds").getArgumentMap().isEmpty(), equalTo(true));

        assertThat(service.getServiceMethods().get("nop").getName(), equalTo("nop"));
        assertThat(service.getServiceMethods().get("nop").getDescription(), equalTo("method without arguments and no result"));
        assertThat(service.getServiceMethods().get("nop").getResultMap().isEmpty(), equalTo(true));
        assertThat(service.getServiceMethods().get("nop").getResultMap().isEmpty(), equalTo(true));
        assertThat(service.getServiceMethods().get("nop").getArgumentMap().isEmpty(), equalTo(true));

    }

    @Test
    public void testMethodWithStringParameter() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("string", ValueFactory.newVString("thedude", null, null));
        parameters.put("times", ValueFactory.newVInt(3, null, null, null));
        ServiceMethod serviceMethod = service.getServiceMethods().get("multipyString");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VString vstring = (VString) result.get("c");
        assertThat(vstring.getValue(), equalTo("thedudethedudethedude"));
    }

    @Test
    public void testMethodWithStringParameter1() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", ValueFactory.newVString("hello ", null, null));
        parameters.put("b", ValueFactory.newVString("world", null, null));
        ServiceMethod serviceMethod = service.getServiceMethods().get("concate");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VString vstring = (VString) result.get("c");
        assertThat(vstring.getValue(), equalTo("hello world"));
    }

    @Test
    public void testMethodWithDoubleParameters() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", ValueFactory.newVDouble(10.0));
        parameters.put("b", ValueFactory.newVDouble(20.0));
        ServiceMethod serviceMethod = service.getServiceMethods().get("sum");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VDouble vdouble = (VDouble) result.get("c");
        assertThat(vdouble.getValue(), equalTo(30.0));
    }

    /* TODO
     @Test
     public void testMethodWithFloatParameters() throws Exception {
     InputStream stream = getClass().getResourceAsStream("RPCSumServiceFloat.xml");
     RPCService service = RPCServices.createFromXml(stream);
     Map<String, Object> parameters = new HashMap<>();
     WriteCache<Map<String, Object>> cache = new WriteCache<>();
     WriteCache<Exception> exceptionCache = new WriteCache<>();
     parameters.put("a", ValueFactory.newVFloat(10.0f));
     parameters.put("b", ValueFactory.newVFloat(20.0f));
     ServiceMethod serviceMethod = service.getServiceMethods().get("sumFloatAlias");
     assertNotNull("serviceMethod is null", serviceMethod);
     serviceMethod.execute(parameters, cache, exceptionCache);
     if (exceptionCache.getValue() != null) {
     throw exceptionCache.getValue();
     }
     VFloat vFloat = (VFloat) cache.getValue().get("c");
     assertThat(vFloat.getValue(), equalTo(30.0f));
     }pvmanager-sample/src/main/java/org/epics/pvmanager/sample/SetupUtil.java
     */
    @Test
    public void testMethodWithFloatArrayParameter() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceFloatArray.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", ValueFactory.newVFloatArray(new ArrayFloat(new float[]{10.0f, 20.0f, 30.0f}),
                ValueFactory.alarmNone(), ValueFactory.timeNow(), ValueFactory.displayNone()));
        parameters.put("b", ValueFactory.newVInt(1, null, null, null));
        ServiceMethod serviceMethod = service.getServiceMethods().get("addToFloatArray");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VFloatArray vFloatArray = (VFloatArray) result.get("c");
        assertThat(vFloatArray.getData().size(), equalTo(3));
        assertThat(vFloatArray.getData().getFloat(0), equalTo(11.0f));
        assertThat(vFloatArray.getData().getFloat(1), equalTo(21.0f));
        assertThat(vFloatArray.getData().getFloat(2), equalTo(31.0f));
    }

    @Test
    public void testMethodWithBooleanParameters() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceBoolean.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("a", ValueFactory.newVBoolean(true, ValueFactory.alarmNone(), ValueFactory.timeNow()));
        parameters.put("b", ValueFactory.newVBoolean(true, ValueFactory.alarmNone(), ValueFactory.timeNow()));
        ServiceMethod serviceMethod = service.getServiceMethods().get("andOperation");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VBoolean vIsGreaterThen = (VBoolean) result.get("c");
        assertThat(vIsGreaterThen.getValue(), equalTo(true));
    }

    /*TODO
     @Test
     public void testMethodWithArgumentAliases() throws Exception {
     InputStream stream = getClass().getResourceAsStream("RPCSumServiceAliases.xml");
     RPCService service = RPCServices.createFromXml(stream);
     Map<String, Object> parameters = new HashMap<>();
     WriteCache<Map<String, Object>> cache = new WriteCache<>();
     WriteCache<Exception> exceptionCache = new WriteCache<>();
     parameters.put("a_alias", ValueFactory.newVFloat(10.0f));
     parameters.put("b_alias", ValueFactory.newVFloat(20.0f));
     ServiceMethod serviceMethod = service.getServiceMethods().get("sumFloat");
     assertNotNull("serviceMethod is null", serviceMethod);
     serviceMethod.execute(parameters, cache, exceptionCache);
     if (exceptionCache.getValue() != null) {
     throw exceptionCache.getValue();
     }
     VFloat vFloat = (VFloat) cache.getValue().get("c");
     assertThat(vFloat.getValue(), equalTo(30.0f));
     }

     @Test
     public void testMethodWithResultAlias() throws Exception {
     InputStream stream = getClass().getResourceAsStream("RPCSumServiceAliases.xml");
     RPCService service = RPCServices.createFromXml(stream);
     Map<String, Object> parameters = new HashMap<>();
     WriteCache<Map<String, Object>> cache = new WriteCache<>();
     WriteCache<Exception> exceptionCache = new WriteCache<>();
     parameters.put("a_alias", ValueFactory.newVFloat(10.0f));
     parameters.put("b_alias", ValueFactory.newVFloat(20.0f));
     ServiceMethod serviceMethod = service.getServiceMethods().get("sumFloatWithAlias");
     assertNotNull("serviceMethod is null", serviceMethod);
     serviceMethod.execute(parameters, cache, exceptionCache);
     if (exceptionCache.getValue() != null) {
     throw exceptionCache.getValue();
     }
     VFloat vFloat = (VFloat) cache.getValue().get("c_alias");
     assertThat(vFloat.getValue(), equalTo(30.0f));
     }

     @Test
     public void testMethodWithBooleanResult() throws Exception {
     InputStream stream = getClass().getResourceAsStream("RPCSumServiceBoolean.xml");
     RPCService service = RPCServices.createFromXml(stream);
     Map<String, Object> parameters = new HashMap<>();
     WriteCache<Map<String, Object>> cache = new WriteCache<>();
     WriteCache<Exception> exceptionCache = new WriteCache<>();
     parameters.put("a", ValueFactory.newVFloat(10.0f));
     parameters.put("b", ValueFactory.newVFloat(20.0f));
     ServiceMethod serviceMethod = service.getServiceMethods().get("isGreaterThen");
     assertNotNull("serviceMethod is null", serviceMethod);
     serviceMethod.execute(parameters, cache, exceptionCache);
     if (exceptionCache.getValue() != null) {
     throw exceptionCache.getValue();
     }
     VBoolean vIsGreaterThen = (VBoolean) cache.getValue().get("c");
     assertThat(vIsGreaterThen.getValue(), equalTo(false));
     }
     */
    @Test
    public void testMethodWithTableResult() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        ServiceMethod serviceMethod = service.getServiceMethods().get("getTable");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        ArrayDouble array = (ArrayDouble) table.getColumnData(0);
        assertNotNull("array1 is null", array);
        assertThat(array.getDouble(0), equalTo(1.0));
        assertThat(array.getDouble(1), equalTo(2.0));
        assertThat(array.getDouble(2), equalTo(3.0));
        array = (ArrayDouble) table.getColumnData(1);
        assertNotNull("array2 is null", array);
        assertThat(array.getDouble(0), equalTo(4.0));
        assertThat(array.getDouble(1), equalTo(5.0));
        assertThat(array.getDouble(2), equalTo(6.0));
    }

    @Test
    @Ignore
    public void testMethodWithImageResult() throws Exception {

        //TODO write getImage RPC rpcservice and add asserts at the end of the test
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        ServiceMethod serviceMethod = service.getServiceMethods().get("getImage");
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VImage image = (VImage) result.get("result");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodWithNonStandaloneResultNotNamed() {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceUnnamedResult.xml");
        RPCServices.createFromXml(stream);
    }

    @Test
    public void testMethodWithResultLongNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("long");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));
    }

    private Map<String, Object> callNTNameValueService(String arrType) throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceNTNameValueResult.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arrType", ValueFactory.newVString(arrType, null, null));
        ServiceMethod serviceMethod = service.getServiceMethods().get("createNTNameValue");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        return result;
    }

    @Test
    public void testMethodWithResultULongNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("ulong");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));
    }

    @Test
    public void testMethodWithResultShortNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("short");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));
    }

    @Test
    public void testMethodWithResultUShortNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("ushort");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));

    }

    @Test
    public void testMethodWithResultByteNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("byte");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));

    }

    @Test
    public void testMethodWithResultUByteNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("ubyte");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));

    }

    @Test
    public void testMethodWithResultIntNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("int");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));

    }

    @Test
    public void testMethodWithResultUIntNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("uint");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VInt vSetVal = (VInt) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1));
        vSetVal = (VInt) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2));

    }

    @Test
    public void testMethodWithResultFloatNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("float");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VFloat vSetVal = (VFloat) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1.0f));
        vSetVal = (VFloat) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2.0f));

    }

    @Test
    public void testMethodWithResultDoubleNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("double");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VDouble vSetVal = (VDouble) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo(1.0));
        vSetVal = (VDouble) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo(2.0));

    }

    @Test
    public void testMethodWithResultStringNTNameValue() throws Exception {

        Map<String, Object> result = callNTNameValueService("string");

        VTable table = (VTable) result.get("result");
        assertNotNull("table is null", table);
        assertEquals("column count mismatch", 2, table.getColumnCount());
        assertThat(table.getColumnName(0), equalTo("sett1"));
        assertThat(table.getColumnName(1), equalTo("sett2"));
        VString vSetVal = (VString) table.getColumnData(0);
        assertThat(vSetVal.getValue(), equalTo("1"));
        vSetVal = (VString) table.getColumnData(1);
        assertThat(vSetVal.getValue(), equalTo("2"));

    }

    @Test
    public void testMethodWithOperationName() {

        InputStream stream = getClass().getResourceAsStream("RPCSumServiceWithOperationName.xml");
        Service service = RPCServices.createFromXml(stream);

        //checking the specifics of pva rpc
        assertThat(((RPCServiceMethod) service.getServiceMethods().get("sumFloat")).getOperationName(),
                equalTo("sumFoo"));
    }

    @Test
    public void testMethodWithoutArgumentsAndNoResult() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        ServiceMethod serviceMethod = service.getServiceMethods().get("nop");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodWithTwoResults() {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceTwoResults.xml");
        RPCServices.createFromXml(stream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodCallWithInvalidArgumentType() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumService.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        WriteCache<Map<String, Object>> cache = new WriteCache<>();
        WriteCache<Exception> exceptionCache = new WriteCache<>();
        parameters.put("a", ValueFactory.newVString("10.0", null, null));
        parameters.put("b", ValueFactory.newVDouble(20.0));
        ServiceMethod serviceMethod = service.getServiceMethods().get("sum");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServiceWithMissingChannelName() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceMissingChannelName.xml");
        Service service = RPCServices.createFromXml(stream);
    }

    @Test
    public void testServiceWithHostName() throws Exception {
        InputStream stream = getClass().getResourceAsStream("RPCSumServiceWithHostName.xml");
        Service service = RPCServices.createFromXml(stream);
        Map<String, Object> parameters = new HashMap<>();
        WriteCache<Map<String, Object>> cache = new WriteCache<>();
        WriteCache<Exception> exceptionCache = new WriteCache<>();
        parameters.put("a", ValueFactory.newVDouble(10.0));
        parameters.put("b", ValueFactory.newVDouble(20.0));
        ServiceMethod serviceMethod = service.getServiceMethods().get("sum");
        assertNotNull("serviceMethod is null", serviceMethod);
        Map<String, Object> result = serviceMethod.executeSync(parameters);
        VDouble vdouble = (VDouble) cache.getValue().get("c");
        assertThat(vdouble.getValue(), equalTo(30.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRPCXmlServiceFactoryWithFileAsConstructorArgument() throws IOException {

        final File tempDir = Files.createTempDirectory("rpcService").toFile();
        File tempFile = new File(tempDir, "rpcService.xml");

        try {

            boolean created = tempFile.createNewFile();
            if (!created) {
                fail("an empty test file not created");
            }

            PVARPCServiceProvider factory = new PVARPCServiceProvider(tempFile);
            factory.createServices();

        } finally {
            tempFile.delete();
            tempDir.delete();
        }
    }

    @Test
    public void testRPCXmlServiceFactoryNonExistentDir() throws IOException {

        PVARPCServiceProvider factory = new PVARPCServiceProvider(new File("nonexistentdirectory"));
        factory.createServices();
    }

    @Test
    public void testRPCXmlServiceFactoryWithInvalidXml() throws IOException {

        final File tempDir = Files.createTempDirectory("rpcService").toFile();
        File tempFile = new File(tempDir, "rpcService.xml");

        try {

            try (BufferedWriter out = Files.newBufferedWriter(tempFile.toPath(), Charset.defaultCharset())) {

                out.write("<invalidxml>");
            }

            PVARPCServiceProvider factory = new PVARPCServiceProvider(tempDir);
            factory.createServices();

        } finally {

            tempFile.delete();
            tempDir.delete();
        }
    }

    @Test
    public void testRPCXmlServiceFactory() throws IOException {

        final File tempDir = Files.createTempDirectory("rpcService").toFile();
        File tempFile = new File(tempDir, "rpcService.xml");

        try {

            try (BufferedWriter out = Files.newBufferedWriter(tempFile.toPath(), Charset.defaultCharset())) {

                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<pvAccessRPCService ver=\"1\" name=\"pvRPCServiceSample\" description=\"A test pvRPC rpcservice\">\n"
                        + "    <channelName>testChannel</channelName>\n"
                        + "    <methods>\n"
                        + "        <method name=\"sumFloatAlias\" operationName=\"sumFloat\" description=\"sum the two float numbers\">\n"
                        + "            <structureid>uri:ev4:nt/2012/pwd:NTXY</structureid>\n"
                        + "            <argument name=\"a\" description=\"first number\" type=\"VFloat\"/>\n"
                        + "            <argument name=\"b\" description=\"second number\" type=\"VFloat\"/>\n"
                        + "            <result name=\"c\" description=\"sum result\" type=\"VFloat\"/>\n"
                        + "        </method>\n"
                        + "    </methods>\n"
                        + "</pvAccessRPCService>");
            }

            PVARPCServiceProvider factory = new PVARPCServiceProvider(tempDir);
            factory.createServices();

        } finally {

            tempFile.delete();
            tempDir.delete();
        }
    }

    @Override
    public void run() {
        try {
            this.rpcServer.run(0);
        } catch (PVAException ex) {
            Logger.getLogger(RPCServicesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class TestRPCServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            String methodName = args.getStringField("op").get();

            if ("sum".equals(methodName)) {
                return new SumServiceImpl().request(args);
            } else if ("sumFloat".equals(methodName)) {
                return new SumFloatServiceImpl().request(args);
            } else if ("isGreaterThen".equals(methodName)) {
                return new GreaterThenServiceImpl().request(args);
            } else if ("andOperation".equals(methodName)) {
                return new AndOperationServiceImpl().request(args);
            } else if ("addToFloatArray".equals(methodName)) {
                return new AddToFloatArrayServiceImpl().request(args);
            } else if ("createNTNameValue".equals(methodName)) {
                return new NTNameValueServiceImpl().request(args);
            } else if ("concate".equals(methodName)) {
                return new ConcateStringServiceImpl().request(args);
            } else if ("getTimeInMiliseconds".equals(methodName)) {
                return new TimeServiceImpl().request(args);
            } else if ("multipyString".equals(methodName)) {
                return new MultiplyStringServiceImpl().request(args);
            } else if ("nop".equals(methodName)) {
                return new NopServiceImpl().request(args);
            } else if ("getTable".equals(methodName)) {
                return new TableServiceImpl().request(args);
            }

            return null;
        }
    }

    static class NTNameValueServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure nameValueStructure
                = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                        new String[]{"name", "value"},
                        new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                            fieldCreate.createScalarArray(ScalarType.pvLong)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            String arrType = args.getStringField("arrType").get();

            PVStructure result = null;

            if ("double".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvDouble)});

                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);
                ((PVDoubleArray) result.getScalarArrayField("value", ScalarType.pvDouble)).put(0, 2, new double[]{1.0, 2.0}, 0);

            } else if ("float".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvFloat)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVFloatArray) result.getScalarArrayField("value", ScalarType.pvFloat)).put(0, 2, new float[]{1.0f, 2.0f}, 0);

            } else if ("int".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvInt)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVIntArray) result.getScalarArrayField("value", ScalarType.pvInt)).put(0, 2, new int[]{1, 2}, 0);

            } else if ("uint".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvUInt)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVUIntArray) result.getScalarArrayField("value", ScalarType.pvUInt)).put(0, 2, new int[]{1, 2}, 0);

            } else if ("byte".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvByte)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVByteArray) result.getScalarArrayField("value", ScalarType.pvByte)).put(0, 2, new byte[]{1, 2}, 0);

            } else if ("ubyte".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvUByte)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVUByteArray) result.getScalarArrayField("value", ScalarType.pvUByte)).put(0, 2, new byte[]{1, 2}, 0);

            } else if ("long".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvLong)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVLongArray) result.getScalarArrayField("value", ScalarType.pvLong)).put(0, 2, new long[]{1L, 2L}, 0);

            } else if ("ulong".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvULong)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVULongArray) result.getScalarArrayField("value", ScalarType.pvULong)).put(0, 2, new long[]{1L, 2L}, 0);

            } else if ("short".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvShort)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVShortArray) result.getScalarArrayField("value", ScalarType.pvShort)).put(0, 2, new short[]{1, 2}, 0);

            } else if ("ushort".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvUShort)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVUShortArray) result.getScalarArrayField("value", ScalarType.pvUShort)).put(0, 2, new short[]{1, 2}, 0);

            } else if ("string".equals(arrType)) {

                Structure nameValueStructure
                        = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTNameValue",
                                new String[]{"name", "value"},
                                new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                                    fieldCreate.createScalarArray(ScalarType.pvString)});
                result = PVDataFactory.getPVDataCreate().createPVStructure(nameValueStructure);

                ((PVStringArray) result.getScalarArrayField("value", ScalarType.pvString)).put(0, 2, new String[]{"1", "2"}, 0);
            }

            if (result != null) {
                ((PVStringArray) result.getScalarArrayField("name", ScalarType.pvString)).put(0, 2, new String[]{"sett1", "sett2"}, 0);
            }

            return result;
        }
    }

    static class TableServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure valueStructure
                = fieldCreate.createStructure(
                        new String[]{"col1", "col2"},
                        new Field[]{fieldCreate.createScalarArray(ScalarType.pvDouble),
                            fieldCreate.createScalarArray(ScalarType.pvDouble)});

        private final static Structure tableStructure
                = fieldCreate.createStructure("uri:ev4:nt/2012/pwd:NTTable",
                        new String[]{"labels", "value"},
                        new Field[]{fieldCreate.createScalarArray(ScalarType.pvString),
                            fieldCreate.createStructure(valueStructure)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(tableStructure);
            ((PVStringArray) result.getScalarArrayField("labels", ScalarType.pvString)).put(0, 2, new String[]{"col1", "col2"}, 0);
            ((PVDoubleArray) result.getStructureField("value").getScalarArrayField("col1", ScalarType.pvDouble)).put(0, 3, new double[]{1.0, 2.0, 3.0}, 0);
            ((PVDoubleArray) result.getStructureField("value").getScalarArrayField("col2", ScalarType.pvDouble)).put(0, 3, new double[]{4.0, 5.0, 6.0}, 0);
            System.out.println("returning table");
            return result;
        }
    }

    static class AddToFloatArrayServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalarArray(ScalarType.pvFloat)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            FloatArrayData floatArrayData = new FloatArrayData();
            ((PVFloatArray) args.getScalarArrayField("a", ScalarType.pvFloat)).get(0, 100000, floatArrayData);
            int b = args.getIntField("b").get();
            for (int i = 0; i < floatArrayData.data.length; i++) {
                floatArrayData.data[i] = floatArrayData.data[i] + b;
            }
            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            ((PVFloatArray) result.getScalarArrayField("c", ScalarType.pvFloat)).put(0, floatArrayData.data.length, floatArrayData.data, 0);
            return result;
        }
    }

    static class TimeServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvDouble)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getDoubleField("c").put(new Date().getTime());

            return result;
        }
    }

    static class SumServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvDouble)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            double a = args.getDoubleField("a").get();
            double b = args.getDoubleField("b").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getDoubleField("c").put(a + b);

            return result;
        }
    }

    static class AndOperationServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvBoolean)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            boolean a = args.getBooleanField("a").get();
            boolean b = args.getBooleanField("b").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getBooleanField("c").put(a & b);

            return result;
        }
    }

    static class SumFloatServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvFloat)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            float a = args.getFloatField("a").get();
            float b = args.getFloatField("b").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getFloatField("c").put(a + b);

            return result;
        }
    }

    static class GreaterThenServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvBoolean)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            float a = args.getFloatField("a").get();
            float b = args.getFloatField("b").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getBooleanField("c").put(a > b);

            return result;
        }
    }

    static class ConcateStringServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvString)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            String a = args.getStringField("a").get();
            String b = args.getStringField("b").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            result.getStringField("c").put(a + b);

            return result;
        }
    }

    static class MultiplyStringServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[]{"c"},
                        new Field[]{fieldCreate.createScalar(ScalarType.pvString)});

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {

            String stringToMultiply = args.getStringField("string").get();
            int howManyTimes = args.getIntField("times").get();

            PVStructure result = PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < howManyTimes; i++) {
                sb.append(stringToMultiply);
            }
            result.getStringField("c").put(sb.toString());

            return result;
        }
    }

    static class NopServiceImpl implements org.epics.pvaccess.server.rpc.RPCService {

        private final static Structure resultStructure
                = fieldCreate.createStructure(
                        new String[0],
                        new Field[0]);

        @Override
        public PVStructure request(PVStructure args) throws RPCRequestException {
            return PVDataFactory.getPVDataCreate().createPVStructure(resultStructure);
        }
    }

    @Before
    public void onSetUp() throws PVAException {
        this.rpcServer = new RPCServer();
        this.rpcServer.registerService("testChannel", new TestRPCServiceImpl());
        this.rpcServer.printInfo();
        Thread thread = new Thread(this);
        thread.start();
    }

    @After
    public void onTearDown() throws PVAException {
        PooledRPCClientFactory.close();
        this.rpcServer.destroy();
    }
}
