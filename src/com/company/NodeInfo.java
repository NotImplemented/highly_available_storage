package com.company;

/**
 * Created by Mikhail_Kaspiarovich on 9/27/2016.
 */
public class NodeInfo {

    public static NodeInfo localNode = null;

    public NodeInfo(String host, Integer port) {

        this.host = host;
        this.port = port;
    }

    private String host;
    private Integer port;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
