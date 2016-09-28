package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mikhail_Kaspiarovich on 9/28/2016.
 */
public class QueryMap {

    public static final ConcurrentHashMap<Long, Query> clientQueries = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Long, List<Query>> internalResponses = new ConcurrentHashMap<>();

    public static void appendClientQuery(Long queryId, Query query) {

        clientQueries.put(queryId, query);
    }

    public static void appendInternalResponses(Long responseQueryId, Query query) {

        List<Query> values = internalResponses.get(responseQueryId);

        if (values == null) {

            internalResponses.putIfAbsent(responseQueryId, Collections.synchronizedList(new ArrayList<Query>()));

            // At this point, there will definitely be a list for the key.
            // We do not know or care which thread new object is in there.
            values = internalResponses.get(responseQueryId);
        }

        values.add(query);
    }

}
