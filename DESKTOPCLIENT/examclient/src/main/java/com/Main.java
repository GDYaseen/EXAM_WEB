package com;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame{
    private static CDDao cddao;
    private JTable cdTable;
    private JTable empruntTable;
    private JTextField idField, nameField, durationField;
    private JCheckBox empruntCheckBox;


    public Main(){
        setTitle("CD Manager Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with tabbed layout
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("All CDs", createAllCDPanel());
        tabbedPane.add("Emprunt CDs", createEmpruntCDPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAllCDPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel cdTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Duration", "Emprunt"}, 0);
        cdTable = new JTable(cdTableModel);
        refreshCDTable(cdTableModel);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Duration:"));
        durationField = new JTextField();
        formPanel.add(durationField);

        formPanel.add(new JLabel("Emprunt:"));
        empruntCheckBox = new JCheckBox();
        formPanel.add(empruntCheckBox);

        JButton addButton = new JButton("Add CD");
        addButton.addActionListener(e -> addCD());

        JButton modifyButton = new JButton("Modify CD");
        modifyButton.addActionListener(e -> modifyCD());

        JButton deleteButton = new JButton("Delete CD");
        deleteButton.addActionListener(e -> deleteCD());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        panel.add(new JScrollPane(cdTable), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Panel for displaying emprunt CDs
    private JPanel createEmpruntCDPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel empruntTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Duration", "Emprunt"}, 0);
        empruntTable = new JTable(empruntTableModel);
        refreshEmpruntTable(empruntTableModel);

        panel.add(new JScrollPane(empruntTable), BorderLayout.CENTER);
        return panel;
    }

    // Refresh the CD table with data from the database
    private void refreshCDTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing data
        List<CD> cds = cddao.getAll();
        for (CD cd : cds) {
            tableModel.addRow(new Object[]{cd.getId(), cd.getNom(), cd.getDuree(), cd.isEmprunt()});
        }
    }

    // Refresh the emprunt CD table with data from the database
    private void refreshEmpruntTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing data
        List<CD> empruntCds = cddao.getEmpruntCDs();
        for (CD cd : empruntCds) {
            tableModel.addRow(new Object[]{cd.getId(), cd.getNom(), cd.getDuree(), cd.isEmprunt()});
        }
    }

    // Add a new CD
    private void addCD() {
        String name = nameField.getText();
        float duration;
        try {
            duration = Float.parseFloat(durationField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration must be a valid number.");
            return;
        }
        boolean isEmprunt = empruntCheckBox.isSelected();

        CD cd = new CD(0, name, duration, isEmprunt); // ID is auto-assigned
        cddao.insert(cd);
        JOptionPane.showMessageDialog(this, "CD added successfully.");
        refreshCDTable((DefaultTableModel) cdTable.getModel());
        clearForm();
    }

    // Modify an existing CD
    private void modifyCD() {
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a valid number.");
            return;
        }

        String name = nameField.getText();
        float duration;
        try {
            duration = Float.parseFloat(durationField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration must be a valid number.");
            return;
        }
        boolean isEmprunt = empruntCheckBox.isSelected();

        CD cd = new CD(id, name, duration, isEmprunt);
        cddao.update(cd);
        JOptionPane.showMessageDialog(this, "CD modified successfully.");
        refreshCDTable((DefaultTableModel) cdTable.getModel());
        clearForm();
    }

    // Delete an existing CD
    private void deleteCD() {
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a valid number.");
            return;
        }

        CD cd = cddao.getOne(id);
        if (cd != null) {
            cddao.delete(cd);
            JOptionPane.showMessageDialog(this, "CD deleted successfully.");
            refreshCDTable((DefaultTableModel) cdTable.getModel());
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "CD not found.");
        }
    }

    // Clear input form fields
    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        durationField.setText("");
        empruntCheckBox.setSelected(false);
    }

    public static void main(String[] args) {
        Properties jndiProperties = new Properties();
        jndiProperties.put("java.naming.factory.initial", "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put("java.naming.provider.url", "http-remoting://localhost:8080"); // Replace localhost with WildFly server IP
        jndiProperties.put("jboss.naming.client.ejb.context", true);

        // Create the InitialContext with properties
        InitialContext context;
        try {
            context = new InitialContext(jndiProperties);
            cddao = (CDDao) context.lookup("ejb:/exam-1.0-SNAPSHOT/CDDaoImplADMIN!com.CDDao?stateful");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);


        });
    }
    //    ejb:/exam-1.0-SNAPSHOT/CDDaoImplADMIN!com.CDDao?stateful

}