package com.company;

import java.util.HashMap;

/**
 * Created by Mikhail_Kaspiarovich on 9/27/2016.
 */
public class Storage {

    public static HashMap<String, byte[]> map = new HashMap<>();

    public static void write(byte[] key, byte[] value) {

        synchronized (map) {

            Object existing = map.get(new String(key));

            if (existing == null) {

                Logger.log("Storing record with key: " + key.toString() + " value: " + value.toString());
                map.put(new String(key),value);
            }
            else {

                Logger.log("Replacing record with key: " + key.toString() + " value: " + value.toString());
                map.replace(new String(key), value);
            }
        }
    }

    public static byte[] read(byte[] key) {

        synchronized (map) {

            Object value = map.get(new String(key));

            if (value == null)
                Logger.log("Reading record with key: " + key.toString());
            else
                Logger.log("Reading record with key: " + key.toString() + " value: " + value.toString());

            return (byte[])value;
        }
    }
}
