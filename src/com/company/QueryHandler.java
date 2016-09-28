package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.runtime.logging.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by Mikhail_Kaspiarovich on 9/27/2016.
 */
public class QueryHandler implements Runnable {

    public QueryHandler(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    private final Socket clientSocket;

    public void run() {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Gson gson = new GsonBuilder().create();
            Query query = gson.fromJson(reader, Query.class);

            Logger.log("Processing query: " + query);

            if (query.getType().equals(Query.create)) {

                Query queryInternal = new Query(Query.writeInternal, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey(), query.getValue());
                QueryMap.appendClientQuery(queryInternal.getId(), query);

                sendQueryToCluster(queryInternal);
            }
            else if (query.getType().equals(Query.writeInternal)) {

                Storage.write(query.getKey(), query.getValue());

                Query queryInternal = new Query(Query.writeInternalAcknowledge, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey(), query.getValue());
                queryInternal.setResponseQueryId(query.getId());

                sendQuery(queryInternal, query.getHost(), query.getPort());
            }
            else if (query.getType().equals(Query.writeInternalAcknowledge)) {

                QueryMap.appendInternalResponses(query.getResponseQueryId(), query);

                if (QueryMap.internalResponses.get(query.getResponseQueryId()).size() == NodesList.readNodesList().size()) {

                    Query clientQuery = QueryMap.clientQueries.get(query.getResponseQueryId());
                    Logger.log("Sending acknowledgement to client " + "host: " + clientQuery.getHost() + " port: " + clientQuery.getPort());

                    Query queryClientResponse = new Query(Query.response, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey(), null);
                    queryClientResponse.setResponseQueryId(clientQuery.getId());

                    sendQuery(queryClientResponse, clientQuery.getHost(), clientQuery.getPort());
                }
            }
            else if (query.getType().equals(Query.read)) {

                Query queryInternal = new Query(Query.readInternal, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey());
                QueryMap.appendClientQuery(queryInternal.getId(), query);

                sendQueryToCluster(queryInternal);
            }
            else if (query.getType().equals(Query.readInternal)) {

                byte[] value = Storage.read(query.getKey());

                Query queryInternal = new Query(Query.readInternalAcknowledge, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey(), value);
                queryInternal.setResponseQueryId(query.getId());

                sendQuery(queryInternal, query.getHost(), query.getPort());
            }
            else if (query.getType().equals(Query.readInternalAcknowledge)) {

                QueryMap.appendInternalResponses(query.getResponseQueryId(), query);

                if (QueryMap.internalResponses.get(query.getResponseQueryId()).size() == NodesList.readNodesList().size()) {

                    Query clientQuery = QueryMap.clientQueries.get(query.getResponseQueryId());
                    Logger.log("Sending acknowledgement to client " + "host: " + clientQuery.getHost() + " port: " + clientQuery.getPort());

                    Query queryClientResponse = new Query(Query.response, NodeInfo.localNode.getHost(), NodeInfo.localNode.getPort(), query.getKey(), query.getValue());
                    queryClientResponse.setResponseQueryId(clientQuery.getId());

                    sendQuery(queryClientResponse, clientQuery.getHost(), clientQuery.getPort());
                }
            }

        }
        catch (IOException e) {

            e.printStackTrace();
        }
        finally {

            try {

                clientSocket.close();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    protected static void sendQueryToCluster(Query queryInternal) {

        List<NodeInfo> nodes = NodesList.readNodesList();

        for (NodeInfo node : nodes) {

            sendQuery(queryInternal, node.getHost(), node.getPort());
        }
    }


    private static void sendQuery(Query queryInternal, String host, Integer port) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Gson gson = new GsonBuilder().create();
                    String serialized = gson.toJson(queryInternal);

                    Logger.log("Sending '" + serialized + "' to " + host + ":" + port);

                    try (Socket socket = new Socket(host, port)) {

                        try(PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

                            writer.println(serialized);
                        }
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        });

        thread.start();
    }
}