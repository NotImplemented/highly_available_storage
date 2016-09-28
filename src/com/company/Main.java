package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.in;

public class Main {

    static Integer listeningPort;
    static String workingDirectory;

    static String helpMessage = "Mandatory parameters are:\r\n" +
    "\t-p or --port \t\tlistening port\r\n" +
    "\t-d or --directory \t\tworking directory\r\n";

    public static void main(String[] args) throws SocketException {

        String commandLine = "Starting cluster node with command line: ";

        for (int i = 0; i < args.length; ++i) {

            if (i > 0)
                commandLine += " ";
            commandLine += args[i];
        }

        System.out.println(commandLine);

        for (int i = 0; i < args.length; ++i) {

            if (args[i].equals("-p") || args[i].equals("--port")) {

                if (i + 1 < args.length) {
                    listeningPort = Integer.parseInt(args[i + 1]);
                }
            } else if (args[i].equals("-d") || args[i].equals("--directory")) {

                if (i + 1 < args.length) {
                    workingDirectory = args[i + 1];
                }
            }
        }

        if (listeningPort == null) {
            System.out.println(helpMessage);
            throw new IllegalArgumentException("Specify listening port number.");
        }

        if (workingDirectory == null) {
            System.out.println(helpMessage);
            throw new IllegalArgumentException("Specify working directory.");
        }

        NodeInfo.localNode = new NodeInfo("localhost", listeningPort);

        Logger.log("Starting service on port " + listeningPort + " and directory '" + workingDirectory + "'.");

        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

            while (true) {

                try {

                    Socket connectionSocket = serverSocket.accept();
                    QueryHandler handler = new QueryHandler(connectionSocket);

                    (new Thread(handler)).start();

                } catch (IOException e) {

                    Logger.log("Cannot receive data from socket: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        Logger.log("Stopping service on port " + listeningPort + " and directory '" + workingDirectory + "'.");
    }
}
