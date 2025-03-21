package com.cybersecurity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class IncidentManager {
    private PriorityQueue<Incident> priorityQueue = new PriorityQueue<>(); // Based on severity
    private List<Incident> incidentLog = new LinkedList<>();
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public IncidentManager() {
        try {
            mongoClient = MongoClients.create("PRIVATE ID");
            database = mongoClient.getDatabase("CIRS");
            collection = database.getCollection("incidents");
            loadIncidentsFromDatabase();
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    public void addIncident(String description, String type, String subtype, int severity) {
        int id = incidentLog.size() + 1;
        Incident incident = new Incident(id, description, type, subtype, severity);
        priorityQueue.add(incident);
        incidentLog.add(incident);

        // Storing incidents in MongoDB
        Document doc = new Document("id", id)
                .append("description", description)
                .append("type", type)
                .append("subtype", subtype)
                .append("severity", severity)
                .append("resolved", false);
        collection.insertOne(doc);
    }

    private void loadIncidentsFromDatabase() {
        FindIterable<Document> documents = collection.find();
        for (Document doc : documents) {
            Integer id = doc.getInteger("id", -1);
            String description = doc.getString("description");
            String type = doc.getString("type");
            String subtype = doc.getString("subtype");
            Integer severity = doc.getInteger("severity", 1);

            if (id != -1 && description != null && type != null && subtype != null && severity != null) { //for null pointer exception
                Incident incident = new Incident(id, description, type, subtype, severity);
                incidentLog.add(incident);
                priorityQueue.add(incident);
            } else {
                System.err.println("Skipping incomplete document: " + doc.toJson());
            }
        }
    }

    public List<Incident> getIncidentLog() {
        return incidentLog;
    }

    public List<Incident> getPriorityIncidents() {
        List<Incident> sortedIncidents = new ArrayList<>(priorityQueue);
        Collections.sort(sortedIncidents); // Sort by severity (higher severity first)
        return sortedIncidents;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
