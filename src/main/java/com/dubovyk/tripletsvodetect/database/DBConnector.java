package com.dubovyk.tripletsvodetect.database;

import com.dubovyk.tripletsvodetect.NLP.model.TripletSVO;
import org.neo4j.driver.v1.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class DBConnector {
    private Driver driver;
    private Session session;
    private static DBConnector instance;

    public static DBConnector getInstance(){
        if(instance == null){
            instance = new DBConnector();
        }
        return instance;
    }

    private DBConnector(){

    }

    public boolean connect(String address, Integer port, String username, String password){
        try {
            driver = GraphDatabase.driver(address + ":" + port.toString(), AuthTokens.basic(username, password));
            session = driver.session();
            return true;
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    public void addNode(GraphNode node){
        StringBuilder query = new StringBuilder();
        query.append("MERGE (n: " + node.getType() + " {");
        int i = 0;
        for(Map.Entry<String, String> property: node.getProperties().entrySet()){
            query.append(property.getKey() + ":" + '"' + property.getValue() + '"');
            if (i  < node.getProperties().entrySet().size() - 1){
                query.append(",");
            }
            i++;
        }
        query.append("}) RETURN n");
        System.out.println(query.toString());
        session.run(query.toString());
    }


    public void addRelation(String relationType, GraphNode from, GraphNode target){
        addNode(from);
        addNode(target);
        StringBuilder query = new StringBuilder();
        query.append("MATCH (a: ").append(from.getType());
        query.append(getFieldsForNode(from));
        query.append("), (b: ").append(from.getType());
        query.append(getFieldsForNode(target));
        query.append(") MERGE (a)-[:").append(relationType).append("]->(b)");
        System.out.println(query.toString());
        session.run(query.toString());
    }

    private String getFieldsForNode(GraphNode node){
        StringBuilder query = new StringBuilder();
        int i = 0;
        query.append("{");
        for(Map.Entry<String, String> field: node.getProperties().entrySet()){
            query.append(field.getKey()).append(":").append('"').append(field.getValue()).append('"');
            if (i  < node.getProperties().entrySet().size() - 1){
                query.append(",");
            }
            i++;
        }
        query.append("}");
        return query.toString();
    }

    public boolean addTriplet(TripletSVO triplet){
        try{
            Map<String, String> source = new HashMap<String, String>();
            Map<String, String> target = new HashMap<String, String>();

            source.put("value", triplet.getSubject());
            target.put("value", triplet.getObject());

            GraphNode nodeSource = new GraphNode("Noun", source);
            GraphNode nodeTarget = new GraphNode("Noun", target);

            addNode(nodeSource);
            addNode(nodeTarget);
            addRelation(triplet.getRelation().replace(" ", "_"), nodeSource, nodeTarget);
         } catch (Exception ex){
            return false;
        }
        return true;
    }

    public boolean addAll(Collection<TripletSVO> triplets){
        try{
            triplets.forEach(this::addTriplet);
        } catch (Exception ex){
            return false;
        }
        return true;

    }
}
