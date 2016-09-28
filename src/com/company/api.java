package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Mikhail_Kaspiarovich on 9/23/2016.
 */
public class api {

    private static Integer responsePort = 4096;

    public static void create(byte[] key, byte[] value) {

        System.out.println("Creating a record with key '" + new String(key) + "' and value '" + new String(value));

        Query q = new Query(Query.create, "localhost", responsePort, key, value);
        sendQuery(q, "localhost", 8192);
        waitForResponse(q);

        System.out.println("Creation was successfull.");
    }

    public static byte[] read(byte[] key) {

        System.out.println("Reading a record with key '" + new String(key) + "'");

        Query q = new Query(Query.read, "localhost", responsePort, key);
        sendQuery(q, "localhost", 8192);
        Query response = waitForResponse(q);

        System.out.println("Reading was successfull.");
        return response.getValue();
    }

    public static void update(byte[] key, byte[] value) {

    }

    public static void delete(byte[] key) {

    }

    private static Query waitForResponse(Query q) {

        System.out.println("Waiting for response for query: " + q);

        try (ServerSocket serverSocket = new ServerSocket(responsePort)) {

            while (true) {

                try {

                    Socket connectionSocket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                    Gson gson = new GsonBuilder().create();
                    Query query = gson.fromJson(reader, Query.class);

                    System.out.println("Received response for query with id = " + q.getId() + ": " + query);

                    if (query.getResponseQueryId() == q.getId()) {

                        return query;
                    }

                }
                catch (IOException e) {

                    System.out.println("Cannot receive data from socket: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    protected static void sendQuery(Query queryInternal, String host, Integer port) {

        try {

            Gson gson = new GsonBuilder().create();
            String serialized = gson.toJson(queryInternal);

            System.out.println("Sending '" + serialized + "' to " + host + ":" + port);

            try (Socket socket = new Socket(host, port)) {

                try(PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

                    writer.println(serialized);
                }
            }

        } catch (IOException e) {

            System.out.println("Sending has failed.");
            e.printStackTrace();
        }
    }


}
