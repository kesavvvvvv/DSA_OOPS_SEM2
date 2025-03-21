package com.cybersecurity;  // Add this at the top

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        String uri = "mongodb+srv://kesavvvvv_:Kesav.3999@oops-dsa-project.cmhua.mongodb.net/?retryWrites=true&w=majority&appName=OOPs-DSA-Project";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("CIRS");
            MongoCollection<Document> collection = database.getCollection("incidents");

            Document doc = new Document("id", 1)
                    .append("incident", "Test Incident")
                    .append("status", "Open");
            collection.insertOne(doc);

            System.out.println("Connected to MongoDB Atlas & inserted test document!");
        } catch (Exception e) {
            System.out.println("Error connecting to MongoDB Atlas: " + e.getMessage());
        }
    }
}
