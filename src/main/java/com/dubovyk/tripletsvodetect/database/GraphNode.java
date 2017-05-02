package com.dubovyk.tripletsvodetect.database;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class GraphNode {
    private String type;
    private Map<String, String> properties;

    public GraphNode(String type){
        this(type, new HashMap<>());
    }

    public GraphNode(String type, Map<String, String> properties){
        this.type = type;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
