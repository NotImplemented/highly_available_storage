package com.company;

import java.io.IOException;

/**
 * Created by Mikhail_Kaspiarovich on 9/28/2016.
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        String key = "Ultimate question?";
        String value = "The improbable assholes!";

        byte[] k = key.getBytes("UTF-8");
        byte[] v = value.getBytes("UTF-8");

        api.create(k, v);

        byte[] written = api.read(k);

        System.out.println(new String(written));
    }
}