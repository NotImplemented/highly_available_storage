package com.company;

import jdk.nashorn.internal.runtime.logging.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail_Kaspiarovich on 9/27/2016.
 */
public class NodesList {

    public static List<NodeInfo> nodesList = null;

    public static List<NodeInfo> readNodesList() {

        if (nodesList != null)
            return nodesList;

        System.out.println("Reading nodes list.");
        nodesList = new ArrayList<>();

        String path = System.getProperty("user.dir") + File.separator + "nodes.txt";

        int node = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String line;
            while ((line = reader.readLine()) != null) {

                System.out.println("Node #" + node++ + ": " + line);

                String[] splitted = line.split(":");

                String host = splitted[0];
                String port = splitted[1];

                nodesList.add(new NodeInfo(host, Integer.parseInt(port)));
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        return nodesList;
    }

}