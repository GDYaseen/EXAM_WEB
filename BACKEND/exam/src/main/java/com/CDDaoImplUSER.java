package com;

import java.util.List;

import javax.ejb.Stateless;


import java.sql.*;
import java.util.ArrayList;

@Stateless
public class CDDaoImplUSER implements CDDao {

    private static final String URL = "jdbc:mysql://localhost:3306/gestioncinetheqe";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public void insert(CD cd) {
        
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
        String sql = "SELECT * FROM cd WHERE id=?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
                 statement.setFloat(1, id);
                 if (resultSet.next()) {
                
                float i = resultSet.getFloat("id");
                String nom = resultSet.getString("nom");
                float duree = resultSet.getFloat("duree");
                boolean empr = resultSet.getBoolean("isEmprunt");
                return new CD(id,nom, duree,empr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CD update(CD cd) {
        
        return null;
    }

    @Override
    public void delete(CD cd) {
       
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
        String sql = "UPDATE cd SET isEmprunt=? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, cd.isEmprunt());
            statement.setFloat(2, cd.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return cd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
