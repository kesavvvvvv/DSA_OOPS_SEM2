package com.cybersecurity;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CyberSecuritySystem {
    private IncidentManager incidentManager;
    private DefaultListModel<String> incidentListModel, priorityListModel;
    private DefaultListModel<String> dosListModel, insiderListModel, phishingListModel, vulnerabilityListModel;

    private JList<String> incidentList, priorityList, dosList, insiderList, phishingList, vulnerabilityList;
    private JTextField searchField, descriptionField, severityField;
    private JComboBox<String> attackTypeDropdown, attackSubtypeDropdown;

    public CyberSecuritySystem() {
        incidentManager = new IncidentManager();

        // Initialize list models
        incidentListModel = new DefaultListModel<>();
        priorityListModel = new DefaultListModel<>();
        dosListModel = new DefaultListModel<>();
        insiderListModel = new DefaultListModel<>();
        phishingListModel = new DefaultListModel<>();
        vulnerabilityListModel = new DefaultListModel<>();

        // Create frame
        JFrame frame = new JFrame("Cybersecurity Incident Response System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Incident Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        descriptionField = new JTextField();
        attackTypeDropdown = new JComboBox<>(new String[]{
                "Denial-of-Service (DoS) / Distributed DoS", "Insider Threats", "Phishing", "System Vulnerability Exploitation"
        });
        attackSubtypeDropdown = new JComboBox<>(new String[]{
                "Volumetric Attacks", "Credential Theft", "Email Scam", "Zero-Day Exploit"
        });
        severityField = new JTextField();
        searchField = new JTextField();

        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Attack Type:"));
        inputPanel.add(attackTypeDropdown);
        inputPanel.add(new JLabel("Attack Subtype:"));
        inputPanel.add(attackSubtypeDropdown);
        inputPanel.add(new JLabel("Severity (1-10):"));
        inputPanel.add(severityField);
        inputPanel.add(new JLabel("Search Incident ID:"));
        inputPanel.add(searchField);

        // Incident Log Panel
        JPanel incidentPanel = createListPanel("Incident Log", incidentList = new JList<>(incidentListModel));
        JPanel priorityPanel = createListPanel("Incident Priority Order", priorityList = new JList<>(priorityListModel));

        // **Incident Category Panels**
        JPanel categoryPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        categoryPanel.add(createListPanel("Denial-of-Service (DoS) / DDoS", dosList = new JList<>(dosListModel)));
        categoryPanel.add(createListPanel("Insider Threats", insiderList = new JList<>(insiderListModel)));
        categoryPanel.add(createListPanel("Phishing", phishingList = new JList<>(phishingListModel)));
        categoryPanel.add(createListPanel("System Vulnerability Exploitation", vulnerabilityList = new JList<>(vulnerabilityListModel)));

        // **Buttons**
        JButton addButton = new JButton("Add Incident");
        JButton searchButton = new JButton("Search by ID");
        JButton refreshButton = new JButton("Refresh");

        // Adding Components to GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1; gbc.weighty = 0.2;
        frame.add(inputPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.weighty = 0.4;
        frame.add(incidentPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        frame.add(priorityPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1; gbc.weighty = 0.4;
        frame.add(categoryPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0.1;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        frame.add(buttonPanel, gbc);

        // **Button Actions**
        addButton.addActionListener(e -> addIncident());
        searchButton.addActionListener(e -> updateLists());
        refreshButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Incident list refreshed!");
            updateLists();
        });

        updateLists();
        frame.setVisible(true);
    }

    private JPanel createListPanel(String title, JList<String> list) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private void addIncident() {
        String description = descriptionField.getText();
        String type = (String) attackTypeDropdown.getSelectedItem();
        String subtype = (String) attackSubtypeDropdown.getSelectedItem();
        int severity;

        try {
            severity = Integer.parseInt(severityField.getText());
            if (severity < 1 || severity > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Severity must be a number between 1 and 10.");
            return;
        }

        incidentManager.addIncident(description, type, subtype, severity);
        JOptionPane.showMessageDialog(null, "Incident Added!");
        updateLists();
    }

    private void updateLists() {
        incidentListModel.clear();
        priorityListModel.clear();
        dosListModel.clear();
        insiderListModel.clear();
        phishingListModel.clear();
        vulnerabilityListModel.clear();

        List<Incident> incidents = incidentManager.getIncidentLog();

        if (incidents != null && !incidents.isEmpty()) {
            for (Incident inc : incidents) {
                incidentListModel.addElement("ID: " + inc.getId() + " - " + inc);
            }

            // Sort incidents by severity in descending order
            List<Incident> sortedBySeverity = new ArrayList<>(incidents);
            sortedBySeverity.sort((a, b) -> Integer.compare(b.getSeverity(), a.getSeverity()));

            for (Incident inc : sortedBySeverity) {
                priorityListModel.addElement("Severity " + inc.getSeverity() + ": " + inc);
            }

            for (Incident inc : incidents) {
                switch (inc.getType()) {
                    case "Denial-of-Service (DoS) / Distributed DoS":
                        dosListModel.addElement(inc.toString());
                        break;
                    case "Insider Threats":
                        insiderListModel.addElement(inc.toString());
                        break;
                    case "Phishing":
                        phishingListModel.addElement(inc.toString());
                        break;
                    case "System Vulnerability Exploitation":
                        vulnerabilityListModel.addElement(inc.toString());
                        break;
                }
            }
        } else {
            priorityListModel.addElement("No high-severity incidents found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CyberSecuritySystem::new);
    }
}