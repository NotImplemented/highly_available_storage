package com.company;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Mikhail_Kaspiarovich on 9/27/2016.
 */
public class Query {

    public final static String create    = "create";
    public final static String read      = "read";
    public final static String update    = "update";
    public final static String delete    = "delete";

    public final static String response    = "response";

    public final static String readInternal         = "read_internal";
    public final static String writeInternal        = "write_internal";

    public final static String readInternalAcknowledge         = "read_internal_acknowledge";
    public final static String writeInternalAcknowledge        = "write_internal_acknowledge";

    private static AtomicLong global = new AtomicLong(0);

    private String host;
    private Integer port;
    private Long id;
    private Long responseQueryId;

    public Query(String type, String host, Integer port, byte[] key, byte[] value) {

        this.type = type;
        this.key = key;
        this.value = value;
        this.host = host;
        this.port = port;

        this.id = global.incrementAndGet();
    }

    public Query(String type, String host, Integer port, byte[] key) {

        this.type = type;
        this.key = key;
        this.host = host;
        this.port = port;

        this.id = global.incrementAndGet();
    }

    @Override
    public String toString() {

        String result = "Id: " + getId() +
               ", Source Host: " + getHost() + ", Source Port: " + getPort() +
               ", Type: " + getType().toUpperCase() + ", Key: " + (new String(getKey()));

        byte[] value = getValue();
        if (value != null)
            result += ", Value: " + (new String(value));

        if (responseQueryId != null)
            result += ", ResponseId: " + responseQueryId;

        return result;
    }

    private String type;
    private byte[] key;
    private byte[] value;

    public String getType() {
        return type;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public Long getId() {
        return id;
    }

    public void setResponseQueryId(Long responseQueryId) {

        this.responseQueryId = responseQueryId;
    }

    public Long getResponseQueryId() {

        return responseQueryId;
    }
}
