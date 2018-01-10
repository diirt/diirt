/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the CacheCollector.
 *
 * @author carcassi
 */
public class BasicTypeSupportTest {

    @BeforeClass
    public static void installSupport() {
        BasicTypeSupport.install();
    }

    @Test
    public void listSupport1() {
        List<Object> oldValues = null;
        List<Object> newValues = new ArrayList<>();
        newValues.add("this");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
    }

    @Test
    public void listSupport2() {
        List<Object> oldValues = new ArrayList<>();
        List<Object> newValues = new ArrayList<>();
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(false));
    }

    @Test
    public void listSupport3() {
        List<Object> oldValues = new ArrayList<>();
        oldValues.add("This");
        List<Object> newValues = new ArrayList<>();
        newValues.add("That");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
        assertThat(notification.getNewValue(), not(sameInstance(oldValues)));
    }

    @Test
    public void listSupport4() {
        List<Object> oldValues = new ArrayList<>();
        oldValues.add("This");
        List<Object> newValues = new ArrayList<>();
        newValues.add("This");
        Notification<List<Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(false));
    }

    @Test
    public void mapSupport1() {
        Map<String, Object> oldValues = null;
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("a", "this");
        Notification<Map<String, Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
    }

    @Test
    public void mapSupport2() {
        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("a", "this");
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("a", "that");
        Notification<Map<String, Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
    }

    @Test
    public void mapSupport3() {
        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("a", "this");
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("a", "this");
        Notification<Map<String, Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(false));
    }

    @Test
    public void mapSupport4() {
        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("a", "this");
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("b", "this");
        Notification<Map<String, Object>> notification = NotificationSupport.notification(oldValues, newValues);
        assertThat(notification.isNotificationNeeded(), equalTo(true));
        assertThat(notification.getNewValue(), equalTo(newValues));
        assertThat(notification.getNewValue(), not(sameInstance(newValues)));
    }

}
