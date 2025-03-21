package com.cybersecurity;

public class Incident implements Comparable<Incident> {
    private int id;
    private String description;
    private String type;
    private String subtype;
    private int severity;
    private boolean resolved;

    public Incident(int id, String description, String type, String subtype, int severity) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.subtype = subtype;
        this.severity = severity;
        this.resolved = false;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public int getSeverity() {
        return severity;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void markResolved() {
        this.resolved = true;
    }

    @Override
    public int compareTo(Incident other) {
        return Integer.compare(other.severity, this.severity); // Higher severity first
    }

    @Override
    public String toString() {
        return "Incident ID: " + id + ", Type: " + type + ", Subtype: " + subtype +
               ", Severity: " + severity + ", Resolved: " + resolved;
    }
}
