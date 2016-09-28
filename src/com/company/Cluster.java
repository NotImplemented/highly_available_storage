package com.company;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail_Kaspiarovich on 9/28/2016.
 */
public class Cluster {

    public static void main(String[] args) throws IOException, InterruptedException {

        List<NodeInfo> nodes = NodesList.readNodesList();

        List<Process> processes = new ArrayList<>();

        for (NodeInfo node : nodes) {

            System.out.println("Starting service on " + node.getHost() + " and listening on port " + node.getPort());
            Process process = execute(Main.class, node.getHost(), node.getPort());
            processes.add(process);
        }

        System.out.println("Cluster has started. Press any key to continue...");
        System.in.read();

        for (Process process : processes) {

            // TODO: bow out gracefully
            if (process.isAlive())
                process.destroy();
        }
    }

    public static Process execute(Class c, String host, Integer port) throws IOException, InterruptedException {

        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        String classpath = System.getProperty("java.class.path");
        String className = c.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, "--port", port.toString(), "--directory", "temp");
        builder.inheritIO();

        Process process = builder.start();

        return process;
    }
}