package com.company;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mikhail_Kaspiarovich on 9/28/2016.
 */
public class Logger {

    private static String getCurrentTimeStamp() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
        Date now = new Date();
        return format.format(now);
    }

    public static void log(String message) {

        System.out.println(getCurrentTimeStamp() + " " + NodeInfo.localNode.getHost() + ":" + NodeInfo.localNode.getPort() + " " + message);
    }

}
