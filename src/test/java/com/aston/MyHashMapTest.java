package com.aston;

import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

public class MyHashMapTest {

    private MyHashMap<String, String> getMyHashMap() {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            myHashMap.put(String.valueOf(i), String.valueOf(i));
        }
        return myHashMap;
    }

    @Test
    public void put() {
        MyHashMap<String, String> myHashMap = getMyHashMap();

        assertEquals(1000, myHashMap.getSize());
        myHashMap.put(null, null);
        assertEquals(1001, myHashMap.getSize());
        assertNull(myHashMap.get(null));

        assertEquals("101", myHashMap.put("0", "101"));
        assertEquals("101", myHashMap.get("101"));
    }

    @Test
    public void get() {
        MyHashMap<String, String> myHashMap = getMyHashMap();
        assertNull(myHashMap.get(null));
        assertEquals("0", myHashMap.get("0"));
        assertEquals("999", myHashMap.get("999"));
    }

    @Test
    public void remove() {
        MyHashMap<String, String> myHashMap = getMyHashMap();
        assertNull(myHashMap.remove(null));
        assertEquals("0", myHashMap.remove("0"));
        assertNull(myHashMap.remove("0"));
        assertEquals(999, myHashMap.getSize());

        for (int i = 1; i < 1000; i++) {
            myHashMap.remove(String.valueOf(i));
        }

        assertEquals(0, myHashMap.getSize());
        assertNull(myHashMap.remove("0"));
    }

    @Test
    public void keySet() {
        MyHashMap<String, String> myHashMap = getMyHashMap();
        Set<String> keySetOfMyHashMap = myHashMap.keySet();
        keySetOfMyHashMap.remove("0");
        assertEquals(999, keySetOfMyHashMap.size());
        assertEquals(1000, myHashMap.getSize());
        keySetOfMyHashMap.add("1001");
        assertNull(myHashMap.get("1001"));
    }

    @Test
    public void values() {
        MyHashMap<String, String> myHashMap = getMyHashMap();
        Collection<String> valuesOfMyHashMap = myHashMap.values();
        valuesOfMyHashMap.remove("0");
        assertEquals(999, valuesOfMyHashMap.size());
    }

    @Test
    public void entrySet() {
        MyHashMap<String, String> myHashMap = getMyHashMap();
        Set<MyHashMap.Entry<String, String>> SetOfMyHashMap = myHashMap.entrySet();
        SetOfMyHashMap.remove(new MyHashMap.Entry<>("0", "0", null));
        assertEquals(999, SetOfMyHashMap.size());
        assertEquals(1000, myHashMap.getSize());
        SetOfMyHashMap.add(new MyHashMap.Entry<>("0", "1000", null));
        assertEquals(1000, SetOfMyHashMap.size());
        assertEquals("0", myHashMap.get("0"));
    }
}