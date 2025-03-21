import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

// Incident Class
class Incident implements Comparable<Incident> {
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

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public int compareTo(Incident other) {
        return Integer.compare(other.severity, this.severity);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Type: " + type + ", Subtype: " + subtype +
               ", Description: " + description + ", Severity: " + severity +
               ", Resolved: " + resolved;
    }
}

// Incident Manager
class IncidentManager {
    private PriorityQueue<Incident> priorityQueue = new PriorityQueue<>();
    private java.util.LinkedList<Incident> incidentLog = new java.util.LinkedList<>();
    private int nextId = 1;

    public void addIncident(String description, String type, String subtype, int severity) {
        Incident incident = new Incident(nextId++, description, type, subtype, severity);
        priorityQueue.add(incident);
        incidentLog.add(incident);
    }

    public java.util.List<Incident> getIncidentLog() {
        return incidentLog;
    }

    public java.util.List<Incident> getIncidentsByType(String type) {
        java.util.List<Incident> filteredIncidents = new java.util.LinkedList<>();
        for (Incident incident : incidentLog) {
            if (incident.getType().equals(type)) {
                filteredIncidents.add(incident);
            }
        }
        return filteredIncidents;
    }

    public java.util.List<Incident> getPriorityIncidents() {
        // Create a new list from the priority queue
        java.util.List<Incident> sortedIncidents = new java.util.ArrayList<>();
        PriorityQueue<Incident> tempQueue = new PriorityQueue<>(priorityQueue);
        
        // Poll each element from the queue to get them in priority order
        while (!tempQueue.isEmpty()) {
            sortedIncidents.add(tempQueue.poll());
        }
        
        return sortedIncidents;
    }

    public Incident getIncidentById(int id) {
        for (Incident incident : incidentLog) {
            if (incident.getId() == id) {
                return incident;
            }
        }
        return null;
    }
}

// Main User Interface
public class CyberSecuritySystem {
    private IncidentManager manager = new IncidentManager();
    private Map<String, String[]> attackSubtypes;
    private Map<String, DefaultListModel<String>> attackTypeModels;

    public CyberSecuritySystem() {
        attackSubtypes = new HashMap<>();
        attackSubtypes.put("Phishing", new String[]{"Email Phishing", "Spear Phishing", "Whaling", "Vishing", "Smishing", "Angler Phishing"});
        attackSubtypes.put("System Vulnerability Exploitation", new String[]{"Zero-Day Exploits", "Buffer Overflow Attacks", "Privilege Escalation", "Privilege Abuse", "Side-Channel Attacks"});
        attackSubtypes.put("Malware", new String[]{"Viruses", "Trojans", "Worms", "Ransomware", "Spyware", "Adware"});
        attackSubtypes.put("Denial-of-Service (DoS) / Distributed DoS", new String[]{"Volumetric Attacks", "Protocol Attacks", "Application Layer Attacks"});
        attackSubtypes.put("Man-in-the-Middle (MitM)", new String[]{"Session Hijacking", "SSL Stripping", "DNS Spoofing"});
        attackSubtypes.put("SQL Injection", new String[]{"Classic SQL Injection", "Blind SQL Injection", "Out-of-Band SQL Injection"});
        attackSubtypes.put("Cross-Site Scripting (XSS)", new String[]{"Reflected XSS", "Stored XSS", "DOM-based XSS"});
        attackSubtypes.put("Credential Stuffing", new String[]{"Credential Stuffing via Bots", "Brute-Force Attacks"});
        attackSubtypes.put("Insider Threats", new String[]{"Malicious Insiders", "Negligent Insiders", "Data Exfiltration"});

        attackTypeModels = new HashMap<>();
        for (String attackType : attackSubtypes.keySet()) {
            attackTypeModels.put(attackType, new DefaultListModel<>());
        }
    }

    public static void main(String[] args) {
        new CyberSecuritySystem().showUI();
    }

    public void showUI() {
        JFrame frame = new JFrame("Cybersecurity Incident Response System");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField descriptionField = new JTextField();
        JComboBox<String> attackTypeBox = new JComboBox<>(attackSubtypes.keySet().toArray(new String[0]));
        JComboBox<String> attackSubtypeBox = new JComboBox<>();
        JSpinner severitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JButton addButton = new JButton("Add Incident");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search by ID");
        DefaultListModel<String> logListModel = new DefaultListModel<>();
        JList<String> incidentLogList = new JList<>(logListModel);

        // Update subtypes based on selected type
        attackTypeBox.addActionListener(e -> {
            String selectedType = (String) attackTypeBox.getSelectedItem();
            attackSubtypeBox.removeAllItems();
            if (selectedType != null) {
                for (String subtype : attackSubtypes.get(selectedType)) {
                    attackSubtypeBox.addItem(subtype);
                }
            }
        });

        attackTypeBox.setSelectedIndex(0);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Attack Type:"));
        inputPanel.add(attackTypeBox);
        inputPanel.add(new JLabel("Attack Subtype:"));
        inputPanel.add(attackSubtypeBox);
        inputPanel.add(new JLabel("Severity (1-10):"));
        inputPanel.add(severitySpinner);
        inputPanel.add(new JLabel("Search Incident ID:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(addButton);

        // Incident Log Panel
        JPanel incidentLogPanel = new JPanel(new BorderLayout());
        incidentLogPanel.setBorder(BorderFactory.createTitledBorder("Incident Log"));
        incidentLogPanel.add(new JScrollPane(incidentLogList), BorderLayout.CENTER);

        // Priority Incident List Panel
        JPanel priorityPanel = new JPanel(new BorderLayout());
        priorityPanel.setBorder(BorderFactory.createTitledBorder("Incident priority order"));
        DefaultListModel<String> priorityListModel = new DefaultListModel<>();
        JList<String> priorityIncidentList = new JList<>(priorityListModel);
        priorityPanel.add(new JScrollPane(priorityIncidentList), BorderLayout.CENTER);

        // Attack Panels
        JPanel attackPanels = new JPanel(new GridLayout(0, 1));
        attackPanels.setBorder(BorderFactory.createTitledBorder("Incident by Type"));
        for (String attackType : attackSubtypes.keySet()) {
            JPanel attackPanel = new JPanel(new BorderLayout());
            attackPanel.setBorder(BorderFactory.createTitledBorder(attackType));
            DefaultListModel<String> attackListModel = attackTypeModels.get(attackType);
            JList<String> attackList = new JList<>(attackListModel);
            attackPanel.add(new JScrollPane(attackList), BorderLayout.CENTER);
            attackPanels.add(attackPanel);
        }

        // Split panels
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, incidentLogPanel, priorityPanel);
        leftSplitPane.setDividerLocation(400);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, new JScrollPane(attackPanels));
        mainSplitPane.setDividerLocation(600);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(mainSplitPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> {
            String description = descriptionField.getText();
            String type = (String) attackTypeBox.getSelectedItem();
            String subtype = (String) attackSubtypeBox.getSelectedItem();
            int severity = (int) severitySpinner.getValue();
            if (!description.isEmpty() && type != null && subtype != null) {
                manager.addIncident(description, type, subtype, severity);
                descriptionField.setText("");
                updateLog(logListModel);
                updatePriorityList(priorityListModel);
                updateAttackPanels(type);
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                try {
                    int id = Integer.parseInt(searchText);
                    Incident incident = manager.getIncidentById(id);
                    if (incident != null) {
                        JOptionPane.showMessageDialog(frame, incident.toString(), "Incident Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incident not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private void updateLog(DefaultListModel<String> logListModel) {
        logListModel.clear();
        for (Incident incident : manager.getIncidentLog()) {
            logListModel.addElement(incident.toString());
        }
    }

    private void updatePriorityList(DefaultListModel<String> priorityListModel) {
        priorityListModel.clear();
        for (Incident incident : manager.getPriorityIncidents()) {
            priorityListModel.addElement(incident.toString());
        }
    }

    private void updateAttackPanels(String type) {
        DefaultListModel<String> model = attackTypeModels.get(type);
        if (model != null) {
            model.clear();
            for (Incident incident : manager.getIncidentsByType(type)) {
                model.addElement(incident.toString());
            }
        }
    }
}