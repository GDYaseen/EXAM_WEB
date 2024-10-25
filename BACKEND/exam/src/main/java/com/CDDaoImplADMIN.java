package com;

import java.util.List;

import javax.ejb.Stateful;


import java.sql.*;
import java.util.ArrayList;

@Stateful
public class CDDaoImplADMIN implements CDDao {

    private static final String URL = "jdbc:mysql://localhost:3306/gestioncinetheqe";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public void insert(CD cd) {
        String sql = "INSERT INTO cd (nom, duree,isEmprunt) VALUES (?, ?,?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cd.getNom());
            statement.setFloat(2, cd.getDuree());
            statement.setBoolean(3, cd.isEmprunt());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CD> getAll() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT * FROM cd";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                float id = resultSet.getFloat("id");
                String nom = resultSet.getString("nom");
                float duree = resultSet.getFloat("duree");
                boolean empr = resultSet.getBoolean("isEmprunt");
                cds.add(new CD(id,nom, duree,empr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cds;
    }

    @Override
    public CD getOne(float id) {
        
        return null;
    }

    @Override
    public CD update(CD cd) {
        String sql = "UPDATE cd SET duree = ? AND nom =? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, cd.getDuree());
            statement.setString(2, cd.getNom());
            statement.setFloat(3, cd.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return cd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(CD cd) {
        String sql = "DELETE FROM cd WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, cd.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CD> getEmpruntCDs() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT * FROM cd where isEmprunt = 1";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                float id = resultSet.getFloat("id");
                String nom = resultSet.getString("nom");
                float duree = resultSet.getFloat("duree");
                boolean empr = resultSet.getBoolean("isEmprunt");
                cds.add(new CD(id,nom, duree,empr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cds;
    }

    @Override
    public CD setEmpruntCD(CD cd) {
        
        return null;
    }
}
